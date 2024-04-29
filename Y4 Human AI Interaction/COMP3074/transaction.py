import random
import nltk
from nltk.tokenize import word_tokenize
import numpy as np
import pandas as pd
import datetime
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from databaseManager import getMoviebyDate, getDatesFromMovie, getMoviesFromDate
from data_preprocess import stemmed_words
from classifier_sentiment import useSentimentClassifier

from input_output import inputUser, outputBot, readBooking, saveBooking, getDateFromInput

#Conversational markers
convMarkersTimelines =  ['firstly', 'Halfway there!...', 'Finally']
convMarkersAck =        ['Thank you', 'Got it', 'Alright', 'Sorry about that', 'Sorry']
convMarkersPosFeed =    ['Good job', 'Nice to hear that']

#Function to check if the input is similar to movie names
def processMovieName(user_input, data) :

    #Similarity matching to find similar movie names
    datasetMovies = pd.read_csv('corpus/movies.csv')
    data.movies = datasetMovies['movie_name']
    movies = data.movies

    #TF-IDF matrix generation
    vecMovieName = CountVectorizer(analyzer=stemmed_words)
    X_movieName = vecMovieName.fit_transform(movies)

    tfidf_movieName = TfidfTransformer()
    movieName_tfidf = tfidf_movieName.fit_transform(X_movieName).toarray()

    user_input_v = vecMovieName.transform([user_input])
    user_input_vector = tfidf_movieName.transform(user_input_v).toarray().reshape(1, -1)
  
    similarities = cosine_similarity (user_input_vector, movieName_tfidf)
    sorted_indices = np.argsort(similarities[0])[::-1]
    best_document_index = sorted_indices[0]
    #print(similarities[0][best_document_index])
    #print(movies[best_document_index])

    #Three tiered confidence - 80% implicit, 45% to 79% explicit, and less than 45%
    if(similarities[0][best_document_index] > 0.79):
        return movies[best_document_index]
    if(similarities[0][best_document_index] > 0.45 and similarities[0][best_document_index] < 0.79):
        outputBot(f"I thought you said {movies[best_document_index]}. Is that the correct movie?")
        user_input = inputUser(data)
        sentiment = useSentimentClassifier(user_input)
        while(sentiment == ''):
            outputBot(f"{convMarkersAck[4]}, please answer yes/no or similar")
            user_input = inputUser(data)
            sentiment = useSentimentClassifier(user_input)
        if(sentiment == 'yes'):
            return movies[best_document_index]
        else:
            return ""
    if(similarities[0][best_document_index] < 0.45):
        return ""

#Converter to transform word numbers to integers
def wordToNum(word):
    word_dict = {
        'one': 1, 'two': 2, 'three': 3, 'four': 4,
        'five': 5, 'six': 6, 'seven': 7, 'eight': 8, 'nine': 9, 'ten': 10
    }

    word = word.lower()
    return word_dict.get(word, None)

#Function to use POS tagging to extract Cardinal Digits and convert to integer if in string format
def processSeatAmount(user_input):

    tokens = word_tokenize(user_input)
    pos_tags = nltk.pos_tag(tokens)

    for token, pos_tag in pos_tags:
        if pos_tag == 'CD':  
            if(token.isdigit()):
                return token
            else:
                return wordToNum(token)
        
    return ''

#Function to check if input is a valid row letter
def processRowSelection(row_reserve):
    valid_rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H']
    if(row_reserve.upper() not in valid_rows):
        return ''
    else:
        return row_reserve.upper()

#Function to check the name of the movie after user provides it
def userGiveMovieName(user_input, data):

    #User provides a name of movie and while name is not correctly identified keep trying
    movie = ''

    #Error Handling
    while (movie == ''):
        movie = processMovieName(user_input, data)
        
        if(movie != ''):
            break
        else:
            output = f"{convMarkersAck[4]} {data.getNameofUser()} I couldn't find a match, try again with no spelling mistakes or type 'help'"
            outputBot(output)
            user_input = inputUser(data)
            
    data.chosenMovie = movie
    chosenMovie = data.chosenMovie
    chosenDate = data.chosenDate
    data.stepsRemaining -= 1
    #Ask for date after
    if(chosenDate == ''):
        data.updateContext("asked_date")
        return f"{convMarkersAck[0]} {data.getNameofUser()}! Which date are you thinking of going?"
    else:
        return processTransaction(chosenDate, chosenMovie, data)

#User provides date after movie name
def userGiveMovieDate(date, data):
    data.stepsRemaining -= 1
    return processTransaction(date, data.chosenMovie, data)

#Function to make sure the seats and row are selected
def checkSeatsAndRow(data):
    data.updateContext("booking_process")
    #Has the user given seats 
    amountOfSeats = data.amountOfSeats
    if(amountOfSeats == '' or int(amountOfSeats) not in range(11)):
        #Keep trying to get seat amount
        while not(str(amountOfSeats).isdigit() and int(amountOfSeats) in range(11)):
            if(amountOfSeats == ''):
                outputBot("How many seats would you like?")
            #Error Handling
            elif(int(amountOfSeats) not in range(11)):
                outputBot("We can only book 10 seats per transaction, please enter a number from 1 to 10")
            input = inputUser(data)
            if(input == ''):
                input = 11
            else:
                amountOfSeats = processSeatAmount(input)

    data.amountOfSeats = amountOfSeats
    #Get the row letter
    outputBot(f"{convMarkersTimelines[2]}, please enter a row letter in which you would like to sit")
    input = inputUser(data)
    temp_row = processRowSelection(input)
    #Error Handling to Keep trying to get row letter
    while(temp_row == ''):
        outputBot(f"{convMarkersAck[4]}, please choose a valid row (A to H) within our cinema.")
        input = inputUser(data)
        temp_row = processRowSelection(input)

    data.rowSelection = temp_row

#Function to confirm booking one last time 
def confirmBooking(data):
    date = data.chosenDate
    movie = data.chosenMovie
    #Save booking in a txt
    timesAvail = getMoviebyDate(date, movie)
    if not(timesAvail):
        data.chosenMovie = ''
        data.chosenDate = ''
        data.amountOfSeats = ''
        data.rowSelection = ''
        return "There has been an error with the system, please restart booking"

   
    date = datetime.datetime.strptime(str(date), "%Y-%m-%d")
    date = date.strftime("%d-%m")

    details = f"Booking for {movie} on the {str(date)} at {timesAvail[0][0]} for {str(data.amountOfSeats)} seats at row {data.rowSelection}"

    #Explicit Confirmation
    outputBot(f"Is the {details} okay?")
    user_input = inputUser(data)
    sentiment = useSentimentClassifier(user_input)

    while(sentiment == ''):
        outputBot(f"{convMarkersAck[4]}, please answer yes/no or similar")
        user_input = inputUser(data)
        sentiment = useSentimentClassifier(user_input)
    #Save booking if it is correct
    if(sentiment == 'yes'):
        saveBooking(details)
        data.updateContext("booking_end")

        ref = random.randint (1000 , 9999)

        output = f"{convMarkersPosFeed[0]} {data.getNameofUser()}, your reference is {str(ref)}"
    #If not start again
    else:
        data.updateContext("general")
        output =  f"No problem {data.getNameofUser()}, you can start again"
    
    data.chosenMovie = ''
    data.chosenDate = ''
    data.amountOfSeats = ''
    data.rowSelection = ''
    return output

#Function to offer movies from chosen date
def chooseOtherMovie(data):
    movies = data.movies
    chosenDate = data.chosenDate
    #From the date try to find other movies
    moviesAvail = getMoviesFromDate(chosenDate)
    if(len(moviesAvail) == 0):
        data.updateContext("general")
        data.chosenMovie = ''
        data.chosenDate = ''
        data.amountOfSeats = ''
        data.rowSelection = ''
        return "No options found, please restart booking"
    moviesText = ''
    movieOpt = []
    for movieIndex in moviesAvail:
        time = getMoviebyDate(chosenDate, movies.iloc[movieIndex])
        moviesText = moviesText + "\n   " + movies.iloc[movieIndex] + " at " + time[0][0]
        movieOpt.append(movies.iloc[movieIndex])
    output = f"Here are the other movies available on this day :{moviesText}\n Which one would you like to book?"
    outputBot(output)
    user_input = inputUser(data)

    #Error Handling to Keep asking for a name until one is found from results
    movie_name = processMovieName(user_input, data)
    while(movie_name not in movieOpt):
        output = "Please type one of the movies offered, or 'stop' to exit"
        outputBot(output)
        user_input = inputUser(data)
        movie_name = processMovieName(user_input, data)
    
    #Conversational Markers
    if(data.stepsRemaining == 2):
        outputBot(convMarkersTimelines[1])
    elif(data.stepsRemaining == 1):
        outputBot(f"Only {data.stepsRemaining} step remaining...")
    #Make sure the seats and row are filled in
    checkSeatsAndRow(data)

    data.chosenMovie = movie_name
    output = confirmBooking(data)
    return output

#Function to offer dates from chosen movie
def chooseOtherDate(data):

    #From the movie try to find other dates
    chosenMovie = data.chosenMovie
    datesAvail = getDatesFromMovie(chosenMovie)
    datesText = ''
    datesPick = []
    for dates in datesAvail:
        date = datetime.datetime.strptime(dates[0], "%Y-%m-%d %H:%M:%S")
        dateOrd = date.strftime("%H:%M:%S %d-%m-%Y")
        datesPick.append(date.strftime("%Y-%m-%d"))

        datesText = datesText + "\n     " + dateOrd
    output = f"Here are the other showings for this movie: {datesText},\n  Which date would you like to book?"
    outputBot(output)

    #Keep asking for a valid date
    user_input = inputUser(data)
    date = getDateFromInput(user_input)
    #Error Handling
    data.updateContext("date_entering")
    while(str(date) not in datesPick):
        outputBot("Please type one of the dates offered. Type help to know more")
        user_input = inputUser(data)
        date = getDateFromInput(user_input)


 
    #Conversational Markers
    if(data.stepsRemaining == 2):
        outputBot(convMarkersTimelines[1])
    elif(data.stepsRemaining == 1):
        outputBot(f"Only {data.stepsRemaining} step remaining...")
    #Make sure the seats and row are filled in
    checkSeatsAndRow(data) 

    data.chosenDate= date
    output = confirmBooking(data)
    return output

#After movie and date chosen, complete transaction
def processTransaction(date, movie, data):
    
    #Get times available if any
    timesAvail = getMoviebyDate(date, movie)
    data.chosenDate = date
    data.chosenMovie = movie


    #If only 1 time found
    if(len(timesAvail) == 1):
        output = f"{convMarkersAck[2]} I can book this time for that film: {timesAvail[0][0]}, Is this okay {data.getNameofUser()}?"
        outputBot(output)
        user_input = inputUser(data)

        sentiment = useSentimentClassifier(user_input)
        while(sentiment == ''):
            outputBot(f"{convMarkersAck[4]}, please answer yes/no or similar")
            user_input = inputUser(data)
            sentiment = useSentimentClassifier(user_input)
        
        #If user doesn't like the option try to offer solutions
        if(sentiment != 'yes'):
            output = f"Would you like to view other dates {data.getNameofUser()}, or other movies?"
            data.updateContext("dislike_option")
            return output
        #Or just end booking if they like it 
        else:
            #Conversational Markers
            if(data.stepsRemaining == 2):
                outputBot(convMarkersTimelines[1])
            elif(data.stepsRemaining == 1):
                outputBot(f"Only {data.stepsRemaining} step remaining...")

            #Make sure the seats and row are filled in
            checkSeatsAndRow(data)

            output = confirmBooking(data)
            return output
    
    else:
        #If no times found
        date = datetime.datetime.strptime(str(date), "%Y-%m-%d")
        date = date.strftime("%d-%m-%Y")
        output = f"{convMarkersAck[4]} {data.getNameofUser()}, No times found for the {date}"
        outputBot(output)
        output = f"Would you like to view other dates {data.getNameofUser()}, or other movies?"
        data.updateContext("dislike_option")
        return output

#Function to start the transaction
def transaction (user_input, data):
    potentialDate = user_input
    movie = ''

    #Check seat amount - shortcut
    seats = processSeatAmount(potentialDate)
    if(seats != '' and 'seat' in potentialDate or 'people' in potentialDate):
        data.amountOfSeats = seats
        data.stepsRemaining -= 1

    #Check if user provided a name in input - shortcut
    movie = processMovieName(user_input, data)
    if(movie == ''):
        movieOutput = f"Okay {data.getNameofUser()}, {convMarkersTimelines[0]}, which movie would you like to watch? or 'help'"
    
    #Check if user provides date in input - shortcut
    date = getDateFromInput(potentialDate)
    #No date given
    if(date == ''):
        data.chosenMovie = movie
        #If not then ask for it
        dateOutput = f"{convMarkersAck[0]} {data.getNameofUser()}! {convMarkersTimelines[0]}, which date are you thinking of going?"
    else:
        data.chosenDate = date

    
    #If movie and/or movie blank route output and context accordingly
    if(date == '' and movie == ''):
        data.updateContext("asked_movie_name")
        return movieOutput
    elif(date != '' and movie == ''):
        data.updateContext("asked_movie_name")
        data.stepsRemaining -= 1
        return movieOutput
    elif(date == '' and movie != ''):
        data.updateContext("asked_date")
        data.stepsRemaining -= 1
        return dateOutput
        
    data.stepsRemaining -= 2
    #If everything provided
    return processTransaction(date, movie, data)



#Check existing booking
def checkCurrentBooking (data):

    output = ''

    bookingDetails = readBooking()
    #If there is a booking
    if(bookingDetails):
        outputBot(f'You have a {bookingDetails}')
        outputBot("If you are happy to keep your booking click enter or type 'cancel' to cancel this booking")
        reply = inputUser(data)
        while(reply != '' or reply != 'cancel'):
            #Don't cancel
            if(reply == ''):
                return 'Great, see you soon'
            #Cancel booking
            elif(reply == 'cancel'):
                saveBooking("")
                return 'Booking cancelled for you'
            #Try again
            else:
                outputBot("Please2 click enter or type cancel")
                reply = inputUser(data)

            
    #If there is no booking
    else:
        output = 'No booking found, you can start by typing "I want to make a booking..."'

    return output
