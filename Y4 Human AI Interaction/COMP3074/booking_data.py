#Class which stores information on context, name of user, transactional data such as movie name, date, seats, rows and steps remaining
class booking_data:
    def __init__(self) -> None:
        self.context = "general"
        self.nameOfUser = "You"
        self.movies = []
        self.chosenMovie = ''
        self.chosenDate = ''
        self.amountOfSeats = ''
        self.rowSelection = ''
        self.stepsRemaining = 4 

    #Update the context stored using currentContext
    def updateContext(self, currentContext):
        self.context = currentContext

    #Get the current context
    def getContext(self):
        return self.context

    #Update the name of the user with username
    def updateNameOfUser(self, username):
        self.nameOfUser = username

    #Return the name of the user only if it is not the default
    def getNameofUser(self):
        if(self.nameOfUser == "You"):
            return ""
        else:
            return self.nameOfUser

    


    