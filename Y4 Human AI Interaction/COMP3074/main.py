from booking_data import booking_data
from classifier_transaction import transaction_classifier
from databaseManager import setUpDatabase
from intent_match import processIdentity, processPredictedIntent

from classifier_general import classifier
from classifier_sentiment import setUpSentimentClassifier


from input_output import inputUser, outputBot

from personalisation import greeting, readNameOfUser, user_login, lastLoginGreeting, writeNameOfUser

import random
import nltk


####UNCOMMENT IF CHATBOT DOES NOT RUN
#nltk.download('words')
#nltk.download('stopwords')
#nltk.download('maxent_ne_chunker')
#nltk.download('punkt')
#nltk.download('averaged_perceptron_tagger')
#nltk.download('wordnet')

#Main function as start point to chatbot
def main () :
    #Get the personalisation data
    lastGreeting = lastLoginGreeting()
    user_login()
    
    #Create sole instance for data management
    data = booking_data()

    #Get the name of the user if there is one saved
    existingName = readNameOfUser()
    if(existingName):
        #Randomly pick one greeting
        num = random.randint(0,1)
        if(num == 0):
            output = lastGreeting + existingName + ", I am a movie chatbot called MovieMaster, how can I help you?"
        else:
            output = greeting() + existingName + ", I am a movie chatbot called MovieMaster, how can I help you?"
        outputBot(output)
        data.updateNameOfUser(existingName)
    #If no name saved, first login
    else:
        output = lastGreeting + "I am a movie chatbot called MovieMaster, what is your name?"
        outputBot(output)
        #Get user name
        user_input = inputUser(data)
        outputBot(processIdentity(user_input,data))


    
    #Set up classifiers, general, transaction, sentiment
    generalClassifier = classifier()
    setUpSentimentClassifier()
    transactionClassifier = transaction_classifier()

    #Chatbot loop
    while True :
        user_input = inputUser(data)
        
        if user_input.lower() == 'exit':
            outputBot(" Bye ")
            break
        #If the user would like the bot to not store the name until next session
        elif('forget me' in user_input.lower() or 'forget my name' in user_input.lower()):
            outputBot(" No problem, I will forget your name after this conversation ")
            writeNameOfUser("")
            continue

        #Predict intent from input using classifier 
        predictedIntent = generalClassifier.useClassifier(user_input)

        #If transaction, use transactional classifier
        if(predictedIntent == 'transaction'):
            predictedIntent = transactionClassifier.useTransactionClassifier(user_input, data)

        output = processPredictedIntent(user_input, predictedIntent, data)
        
        outputBot(output)

if __name__ == "__main__":

    #Delete movieAvailability.db and uncomment to regenerate database only if absolutely necessary
    ####setUpDatabase()


    main ()