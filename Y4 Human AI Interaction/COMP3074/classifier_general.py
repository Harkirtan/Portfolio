import pandas as pd
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.linear_model import LogisticRegression
from data_preprocess import stemmed_words

#Classifier to match intent into name_ask, name_give, question, transaction, small_talk
class classifier:
    def __init__(self) -> None:
        #Classifier dataset
        dataset = pd.read_csv('corpus/classifier_data.csv')

        data = dataset['prompt']
        labels = dataset['intent']
        #TF-IDF matrix generation
        self.count_vect = CountVectorizer(analyzer=stemmed_words)
        X_train_counts = self.count_vect.fit_transform( data )

        self.tfidf_transformer = TfidfTransformer(sublinear_tf = True).fit (X_train_counts )
        X_train_tf = self.tfidf_transformer.transform ( X_train_counts )

        #Parameters set to balanced for class imbalance and ovr for onevrest strategy
        self.classifier = LogisticRegression ( random_state =0, class_weight='balanced', multi_class='ovr').fit(X_train_tf , labels )

    def useClassifier(self, user_input):

        new_input = self.count_vect.transform([user_input])
        new_input = self.tfidf_transformer.transform(new_input)

        new_input = self.count_vect.transform([user_input])
        new_input = self.tfidf_transformer.transform(new_input)

        predictedIntent =  self.classifier.predict(new_input)
        #print(predictedIntent)
        #print(self.classifier.predict_proba(new_input))
        #print(self.classifier.classes_)

        return predictedIntent