
import pandas as pd
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression
from data_preprocess import stemmed_words

#Classifier to match intent into booking_start, choose_other_date, choose_other_movie, give_movie_name, view_booking
class transaction_classifier:
    def __init__(self) -> None:
        #Transaction data classifier
        dataset = pd.read_csv('corpus/transaction_data.csv')

        data = dataset['prompt']
        labels = dataset['intent']
        #TF-IDF matrix generation
        self.count_vect = CountVectorizer(analyzer=stemmed_words)
        X_train_counts = self.count_vect.fit_transform( data )

        self.tfidf_transformer = TfidfTransformer().fit(X_train_counts )
        X_train_tf = self.tfidf_transformer.transform ( X_train_counts )

        #Parameters set to balanced for class imbalance and ovr for onevrest strategy
        self.classifier = LogisticRegression ( random_state =0, class_weight='balanced', multi_class='ovr').fit(X_train_tf , labels )


    def useTransactionClassifier(self, user_input, data):

        new_input = self.count_vect.transform([user_input])
        new_input = self.tfidf_transformer.transform(new_input)

        new_input = self.count_vect.transform([user_input])
        new_input = self.tfidf_transformer.transform(new_input)

        predictedIntent =  self.classifier.predict(new_input)
        #print(predictedIntent)
        #print(self.classifier.predict_proba(new_input))
        #print(self.classifier.classes_)

        o_date = self.classifier.predict_proba(new_input)[0][1]
        o_movie = self.classifier.predict_proba(new_input)[0][2]

        #Handle ambiguity for different options and return intent to reprompt user
        if(data.getContext() == "dislike_option"):
            ambiguity = abs(o_date - o_movie)
            if(ambiguity <= 0.05):
                return 'option_ambig'
            else:
                return predictedIntent
        else:
            return predictedIntent
        