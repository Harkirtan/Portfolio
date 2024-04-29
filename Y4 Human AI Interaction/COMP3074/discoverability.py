import numpy as np
import pandas as pd
import math
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from data_preprocess import stemmed_words

#Conversational markers
convMarkersAck =['Thank you', 'Got it', 'Alright', 'Sorry about that', 'Sorry']

#Print all movies in the results provided
def getMoviesFromList(results, outputBot):

    for i in range(0, len(results)):
        outputBot(f"{results['movie_name'].iloc[i]}")


#Allow user to search by categories/criteria
def giveMoviesAvailable(data, inputUser, outputBot):
    mov = pd.read_csv('corpus/movies_with_categories.csv')

    #Give option for user to exit this by doing string checking
    outputBot("Which criteria would you like to search by: Release, Certificate, Genres or Rating or 'done' to exit")
    reply = inputUser(data)
    while(reply != 'done'):
        if('release' in reply.lower()):
            outputBot("Please enter a release year to search by:")
            reply = inputUser(data)
            reply = 'in ' + reply
            results = mov.loc[mov['Released'] == reply]
            if not(results.empty):
                getMoviesFromList(results, outputBot)
            else:
                outputBot("No movies with that criteria")
        elif('certificate' in reply.lower()): 
            outputBot("Please enter an age rating from U, UA, PG13, R:")
            reply = inputUser(data)
            results = mov.loc[mov['Certificate'] == reply]
            if not(results.empty):
                getMoviesFromList(results, outputBot)
            else:
                outputBot("No movies with that criteria")
        elif('genre' in reply.lower()):
            outputBot("Please enter a genre to search by:")
            reply = inputUser(data)
            results = mov.loc[mov['Genre'].str.contains(reply.title())]
            if not(results.empty):
                getMoviesFromList(results, outputBot)
            else:
                outputBot("No movies with that criteria")
        elif('rating' in reply.lower()):
            outputBot("Please enter a minimum rating:")
            reply = inputUser(data)
            if(reply == '' or not reply.isdigit()):
                reply = '0'
            results = mov.loc[mov['IMDB_Rating'] > float(reply)]
            if not(results.empty):
                getMoviesFromList(results, outputBot)
            else:
                outputBot("No movies with that criteria")
        else:
            outputBot("Enter one of: Release, Certificate, Genres or Rating or 'done' to exit") 
            reply = inputUser(data)
        


#Process if input is discoverability
def processDiscoverability(user_input, data, i, o):
    context = data.getContext()

    #TF-IDF matrix generation
    discoverability_data = pd.read_csv('corpus/discoverability.csv')
    vecD = CountVectorizer(analyzer=stemmed_words)
    X_d = vecD.fit_transform(discoverability_data['prompt'])

    tfidf_d = TfidfTransformer()
    d_tfidf = tfidf_d.fit_transform(X_d).toarray()

    user_input_v = vecD.transform([user_input])
    user_input_vector = tfidf_d.transform(user_input_v).toarray().reshape(1, -1)
  
    #Cosine similarity
    similarities = cosine_similarity (user_input_vector, d_tfidf)

    sorted_indices = np.argsort(similarities[0])[::-1]
    best_document_index = sorted_indices[0]

    #Context management
    #Threshold 80% to check if the result is similar enough, if is then implicit confirmation
    if(similarities[0][best_document_index] < 0.80 or math.isnan(similarities[0][best_document_index])):
        return ""
    else:
        #For each context, there is a custom response
        response = ""
        if(context == "general"):
            response ="You can make a booking by typing 'I would like to make a booking', or you can type a movie question and I will try answer that for you"
        elif(context == "booking_process"):
            response ="Please continue to entering seats and row or type stop to exit the transaction"
        elif(context == "booking_end"):
            response ="You are able to make another booking, or ask me trivia or we can just chat"
        elif(context == "date_entering"):
            response ="Type as dd-mm-yyyy or yyyy-mm-dd"
        elif(context == "asked_movie_name"):
            giveMoviesAvailable(data, i, o)
            response = "If you are happy with a movie, please type the name of the movie now:"
        elif(context == "asked_date"):
            response ="Please type the date as dd-mm-yyyy or 'today'/'tomorrow' and I can show you availability"
        elif(context == "dislike_option"):
            response ="Please type 'another movie' or similar to see other movies, or type 'another date' or similar to see other dates"
        return response


