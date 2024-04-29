from classifier_sentiment import useSentimentClassifier
import nltk
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
import datetime
import numpy as np
import pandas as pd
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from data_preprocess import stemDF, stemmed_words
from input_output import inputUser, outputBot, getDateFromInput
from personalisation import greeting, writeNameOfUser
from transaction import checkCurrentBooking, chooseOtherDate, chooseOtherMovie, transaction, userGiveMovieDate, userGiveMovieName

#Small Talk processing function 
def processSmallTalk(user_input):
    #TF-IDF matrix generation
    smallTalk_dataset = pd.read_csv('corpus/small_talk.csv')
    vecST = CountVectorizer(analyzer=stemmed_words)
    X_st = vecST.fit_transform(smallTalk_dataset['prompt'])

    tfidf_st = TfidfTransformer()
    st_tfidf = tfidf_st.fit_transform(X_st).toarray()

    user_input_v = vecST.transform([user_input])
    user_input_vector = tfidf_st.transform(user_input_v).toarray().reshape(1, -1)
  
    #Cosine Similarity
    similarities = cosine_similarity (user_input_vector, st_tfidf)

    sorted_indices = np.argsort(similarities[0])[::-1]
    best_document_index = sorted_indices[0]

    processed_input = smallTalk_dataset['response'].iloc[best_document_index]

    if(processed_input == "currentDate"):
        processed_input = datetime.date.today().strftime("%d/%m/%Y")
    elif(processed_input == "currentTime"):
        processed_input = datetime.datetime.now().strftime("%H:%M:%S")

    return processed_input


#Identity Management function
def processIdentity (user_input, data) :
    #Error Handling
    while(user_input == '' or user_input.isdigit()):
        if(user_input == ''):
            outputBot("Please try again, your name should not be empty")
        elif(user_input.isdigit()):
            outputBot("Please try again, your name should not be a number")
        user_input = inputUser(data)

 
    #Pre process input via stemming, stop word removal and tokenization
    stop_words = set(stopwords.words('english'))

    user_input = stemDF(user_input)

    tokens = word_tokenize(user_input.title())
    tokens_without_sw = [ word for word in tokens if not word.lower() in stop_words ]
    #POS tag for grammatical meaning
    tagSentence = nltk.pos_tag(tokens_without_sw)

    #NER to extract name
    result = nltk.ne_chunk(tagSentence)
    #print(result)
    name = ""
    for subtree in result.subtrees():
        #Get name
        if subtree.label() == 'PERSON': 
            name = subtree.leaves()[0][0] 
        #Try to get as many names as possible
        elif subtree.label() == 'GPE': 
            name = subtree.leaves()[0][0]

    #Greet the user with time of day (good morning/afternoon/evening)
    if(name):
        data.updateNameOfUser(name)
        writeNameOfUser(name)
        return greeting() + name 
    else:
        return ""
    


#Question answering function
def processQuestion (user_input, data) :
    #Pre-process Dataset, via Vectorisation and TF-IDF to create Term-Document
    qa_dataset = pd.read_csv('corpus/movieQA.csv')
    #TF-IDF matrix generation
    vec = CountVectorizer(analyzer=stemmed_words)
    X = vec.fit_transform(qa_dataset['Question'])
    tfidf = TfidfTransformer(sublinear_tf=True)
    X_tfidf = tfidf.fit_transform(X).toarray()
    user_input_processed = stemDF(user_input)
    user_input_v = vec.transform([user_input_processed])
    user_input_vector = tfidf.transform(user_input_v).toarray().reshape(1, -1)
    
    #Cosine Similarity
    similarities = cosine_similarity (user_input_vector, X_tfidf)

    sorted_indices = np.argsort(similarities[0])[::-1]
    best_document_index = sorted_indices[0]

    #print(best_document_index)
    #print(similarities[0][best_document_index])

    #Three tiered confidence - 80% implicit, 45% to 79% explicit, and less than 45%
    answer = qa_dataset['Answer'].iloc[best_document_index]
    if(similarities[0][best_document_index] > 0.79):
        return answer
    if(similarities[0][best_document_index] > 0.45 and similarities[0][best_document_index] < 0.79):
        outputBot(answer)
        #Check if correct answer given
        outputBot("Did that answer your question?")
        reply = inputUser(data)
        sentiment = useSentimentClassifier(reply)
        while(sentiment == ''):
            outputBot("Sorry, please answer yes/no or similar")
            reply = inputUser(data)
            sentiment = useSentimentClassifier(reply)
        #If yes add to database
        if(sentiment == 'yes'):
            #Add to QA
            qa = []
            qa.append({'Question': user_input, 'Answer': answer})
            df = pd.DataFrame(qa)
            df.to_csv('corpus/movieQA.csv', mode='a', index=False, header=False)
            #Add to Classifier
            qa = []
            qa.append({'prompt': user_input, 'intent': 'question'})
            df = pd.DataFrame(qa)
            df.to_csv('corpus/classifier_data.csv', mode='a', index=False, header=False)

            return "Thank you for letting me know"
        else:
            return "Sorry " + data.getNameofUser() + ", please try again"
    if(similarities[0][best_document_index] < 0.45):
        return "Sorry " + data.getNameofUser() + ", please try again"


#Function to route all intents based on context and what the intent is
def processPredictedIntent(user_input, intent, data):    
    if(getDateFromInput(user_input) != '' and data.getContext() == "asked_date"):
        if(type(getDateFromInput(user_input)) == str):
            return getDateFromInput(user_input)
        return userGiveMovieDate(getDateFromInput(user_input), data)

    elif(intent == 'small_talk'):
        return processSmallTalk(user_input)
    
    elif(intent == 'name_give'):
        outputBot("Sure, what is your name?")
        user_input = inputUser(data)
        return processIdentity(user_input, data)
    
    elif(intent == 'name_ask'):
        if(data.getNameofUser() == ""):
            return "I am not sure"
        else:
            return "You are " + data.getNameofUser()
    
    elif(intent == 'booking_start'):
        return transaction(user_input, data)

    elif(intent == 'give_movie_name' and data.getContext() == 'asked_movie_name'):
        return userGiveMovieName(user_input, data)
    #Disambiguation
    elif(intent == 'option_ambig'):
        return "Please be clearer if you would like other movies or other dates"

    elif(intent == 'choose_other_movie' and data.getContext() == "dislike_option"):
        return chooseOtherMovie(data)
    
    elif(intent == 'choose_other_date'and data.getContext() == "dislike_option"):
        return chooseOtherDate(data)

    elif(intent == 'view_booking'):
        return checkCurrentBooking(data)

    elif(intent == 'question'):
        return processQuestion(user_input, data)
    
    else:
        return "I am not sure what you mean, please try re-wording or 'help'"

