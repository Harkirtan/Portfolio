import datetime
import sqlite3
import pandas as pd
from datetime import date, timedelta
import random

#Function which gets called once to setup and create database
def setUpDatabase() :

    datasetMovies = pd.read_csv('corpus/movies.csv')
    movies = datasetMovies['movie_name']

    #Create tables
    connection = sqlite3.connect('movieAvailability.db')
    cursor = connection.cursor ()
    cursor.execute ( ''' CREATE TABLE IF NOT EXISTS Movies 
                      ( movieID integer primary key, 
                        title text  ) ''')
    
    cursor.execute ( ''' CREATE TABLE IF NOT EXISTS ShowingTimes 
                      ( dateID integer primary key, 
                        dateAndTime datetime,
                        movieID integer,
                        foreign key (movieID) references Movies(movieID)) ''')
    
    #Populate tables
    i = 0
    for movie in movies:
        cursor.execute (" INSERT INTO Movies VALUES(?,?)", (i, movie))
        i += 1

    delta = 1
    movieref = 0
    #Have 4 showings for each movie every day
    for i in range(0, len(movies)*16, 4):
        day = date.today() + timedelta(delta)
        showingTime1 = str(day) + ' 09:00:00'
        showingTime2 = str(day) + ' 13:00:00'
        showingTime3 = str(day) + ' 17:00:00'
        showingTime4 = str(day) + ' 21:00:00'

     

        if(movieref+4 > len(movies)):
            movieref = 0
        
        mov_ind = [movieref, movieref+1, movieref+2, movieref+3]
        random.shuffle(mov_ind)
     
        cursor.execute (" INSERT INTO ShowingTimes VALUES (?, ?, ?)", (i, showingTime1, mov_ind[0]))
        cursor.execute (" INSERT INTO ShowingTimes VALUES (?, ?, ?)", (i+1, showingTime2, mov_ind[1]))
        cursor.execute (" INSERT INTO ShowingTimes VALUES (?, ?, ?)", (i+2, showingTime3, mov_ind[2]))
        cursor.execute (" INSERT INTO ShowingTimes VALUES (?, ?, ?)", (i+3, showingTime4, mov_ind[3]))

       
        movieref += 4

        delta+=1

        
    connection.commit ()
    connection.close ()

#Return time by parameters: name of movie and date
def getMoviebyDate (date, movieName):

    datasetMovies = pd.read_csv('corpus/movies.csv')
    movies = datasetMovies['movie_name']
    #idMovie = movies.index(movieName)
    idMovie = int(movies[movies == movieName].index[0])

    connection = sqlite3.connect('movieAvailability.db')
    cursor = connection.cursor ()

    #Don't show past dates
    c_date = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')

    cursor.execute("SELECT TIME(dateAndTime) from ShowingTimes WHERE DATE(dateAndTime) == ? AND movieID == ? AND dateAndTime > ?", (date, idMovie, c_date))

    connection.commit()

    timesFound = cursor.fetchall()
    #print(timesFound)


    connection.close ()


    return timesFound

#Return dates by parameter: name of movie
def getDatesFromMovie (movieName):

    datasetMovies = pd.read_csv('corpus/movies.csv')
    movies = datasetMovies['movie_name']
    #idMovie = movies.index(movieName)
    idMovie = int(movies[movies == movieName].index[0])

    connection = sqlite3.connect('movieAvailability.db')
    cursor = connection.cursor ()

    #Don't show past dates
    current_date = datetime.datetime.now()
    c_date = current_date.strftime('%Y-%m-%d %H:%M:%S')
    #Get 4 dates in the future from today
    cursor.execute(
        "SELECT dateAndTime FROM ShowingTimes WHERE movieID == ? AND dateAndTime > ? ORDER BY dateAndTime LIMIT 4",
        (idMovie, c_date)
    )
    connection.commit()

    timesFound = cursor.fetchall()
    #print(timesFound)

    connection.close ()

    return timesFound


#Return movies by parameter: date
def getMoviesFromDate (date):

    connection = sqlite3.connect('movieAvailability.db')
    cursor = connection.cursor ()

    #Don't show movies currently running
    today = datetime.datetime.now()

    remaining_hours = 24 - today.hour - 1 if today.hour < 24 else 0

    hour_diff = today + timedelta(hours=min(1, remaining_hours))

    if(str(date) == today.strftime('%Y-%m-%d')):
        cursor.execute("SELECT movieID FROM ShowingTimes WHERE DATE(dateAndTime) = ? AND TIME(dateAndTime) > ?", (date,hour_diff.strftime('%H:%M:%S')))
        
    else:
        cursor.execute("SELECT movieID from ShowingTimes WHERE DATE(dateAndTime) == ?", (date,))

    connection.commit()

    timesFound = cursor.fetchall()
    #print(timesFound)


    connection.close ()


    return timesFound
