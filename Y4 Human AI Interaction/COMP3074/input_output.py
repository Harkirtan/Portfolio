from discoverability import processDiscoverability
import datetime
import re

#User input without discoverability to avoid infinite discoverability loops
def inputUserNoDisc (book_data):
    user_input = input (" "+ book_data.nameOfUser +" : ")

    #Exit program if stop
    if(user_input == 'stop'):
        outputBot("Ending transaction...")
        exit(1)
    if('thank you' in user_input.lower() or 'thanks' in user_input.lower()):
        outputBot("You're welcome")
        return inputUser (book_data)
    else:
        return user_input


#All user input is filtered through here
def inputUser (book_data):
    user_input = input (" "+ book_data.nameOfUser +" : ")

    #Exit program if stop
    if(user_input == 'stop'):
        outputBot("Ending transaction...")
        exit(1)
    #Conversational Markers
    if('thank you' in user_input.lower() or 'thanks' in user_input.lower()):
        outputBot("You're welcome")
        return inputUser (book_data)
    #Check discoverability on all input to aid the user in finding help at any point
    discoverability = processDiscoverability(user_input, book_data, inputUserNoDisc, outputBot)
    if(discoverability != '' and book_data.getContext() == "asked_movie_name"):
        outputBot(discoverability)
        return input (" "+ book_data.nameOfUser +" : ")
    elif(discoverability != ''):
        outputBot(discoverability)
        return inputUser(book_data)
    else:
        return user_input

#All bot output is filtered through here
def outputBot (text):
    print (f" MovieMaster : " + text)

#Use regex to extract dates
def getDateFromInput(user_input):
    
    date_patterns = [
    r'\d{4}-\d{2}-\d{2}',  # YYYY-MM-DD
    r'\d{1,2}/\d{1,2}/\d{4}',  # MM/DD/YYYY or M/D/YYYY
    r'\d{1,2}-\d{1,2}-\d{4}',  # MM-DD-YYYY or M-D-YYYY
    ]

    date = ''
    #Try to match the patterns to user input 
    for pattern in date_patterns:
        dates = re.search(pattern, user_input)
        if(dates):
            try:
                date = datetime.datetime.strptime(dates.group(), '%Y-%m-%d').date()
            except ValueError:
                try:
                    date = datetime.datetime.strptime(dates.group(), '%d-%m-%Y').date()
                except ValueError:
                    try:
                        date = datetime.datetime.strptime(dates.group(), '%d/%m/%Y').date()
                    except ValueError:
                        ""
    #Markers
    if('today' in user_input):
        date = datetime.date.today()
    elif('tomorrow' in user_input):
        date = datetime.date.today() + datetime.timedelta(days=1)

    #If the date is in the past return error 
    if(date != '' ):
       if(date < datetime.date.today()):
        return "Invalid date, please enter a date from today"
    return date

#Save the booking to a txt
def saveBooking(details):
    with open('booking.txt', 'w') as f:
        f.write(details)

#Read booking from txt
def readBooking():
    with open('booking.txt', 'r') as f:
        lines = f.readlines()

        if lines:
            for line in lines:
                return line
        else:
            return ''
