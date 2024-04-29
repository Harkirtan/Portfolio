import pandas as pd
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression
from data_preprocess import stemmed_words

#Classifier to match intent into yes, no
def setUpSentimentClassifier ():
    #Sentiment classifier data
    dataset = pd.read_csv('corpus/sentiment.csv')

    data = dataset['prompt']
    labels = dataset['intent']
    #TF-IDF matrix generation
    global count_vect 
    count_vect = CountVectorizer( analyzer=stemmed_words)
    X_train_counts = count_vect.fit_transform( data )

    global tfidf_transformer 
    tfidf_transformer = TfidfTransformer(use_idf = True, sublinear_tf = True ). fit (X_train_counts )
    X_train_tf = tfidf_transformer.transform ( X_train_counts )

    global classifier
    #Parameters set to ovr for onevrest strategy
    classifier = LogisticRegression ( random_state =0, multi_class='ovr').fit(X_train_tf , labels )

    return 

def useSentimentClassifier(user_input):

    new_input = count_vect.transform([user_input])
    new_input = tfidf_transformer.transform(new_input)
    #If prediction is not confident or ambiguous
    maxProb = classifier.predict_proba(new_input)[0].max()
    if(maxProb < 0.55):
        return ''
    else:
        return classifier.predict(new_input)


