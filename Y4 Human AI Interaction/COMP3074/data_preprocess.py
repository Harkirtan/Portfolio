import nltk
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem.wordnet import WordNetLemmatizer
from nltk.stem.porter import PorterStemmer
from sklearn.feature_extraction.text import CountVectorizer

#Utility function to stem text
def stemDF(text):
    stem_tokens = []

    tokens = word_tokenize(text)
    tokens_without_sw = [ word.lower() for word in tokens if not word in stopwords.words() ]

    p_stemmer = PorterStemmer()

    for token in tokens_without_sw :
        stem_tokens.append(p_stemmer.stem(token))
        
    return ' '.join(stem_tokens)

#Porter stemmer
p_stemmer = PorterStemmer()
analyzer = CountVectorizer().build_analyzer()

#Utility function for stemming analyzer 
def stemmed_words (doc):
    return ( p_stemmer.stem(w) for w in analyzer (doc))

#Utility function to lemmatize text
def lemmatizeDF(text):
    lemma_tokens = []
    lemmatiser = WordNetLemmatizer()
    posmap = {
        'ADJ': 'a',
        'ADV': 'r',
        'NOUN': 'n',
        'VERB': 'v'
    }
    tokens = word_tokenize(text)
    tokens_without_sw = [ word.lower() for word in tokens if not word in stopwords.words() ]

    post = nltk.pos_tag ( tokens_without_sw , tagset ='universal')
    for token in post :
        word = token [0]
        tag = token [1]
        if tag in posmap . keys () :
            lemma_tokens.append(lemmatiser.lemmatize(word, posmap[tag]))
        else :
            lemma_tokens.append(lemmatiser.lemmatize(word))
    
    return ' '.join(lemma_tokens)    



