import datetime

#Get name of user from file
def readNameOfUser():
    with open("nameOfUser.txt", "r") as file:
        name = file.read().strip()
    return name


#Save name of user from file
def writeNameOfUser(name):
    with open("nameOfUser.txt", "w") as file:
        file.write(name)

#Time of day greeting
def greeting():
    hour = datetime.datetime.now().hour

    if 5 <= hour < 12:
        greeting = "Good Morning, "
    elif 12 <= hour < 17:
        greeting = "Good Afternoon, "
    else:
        greeting = "Good Evening, "

    return greeting

#Log the time of user accessing bot
def user_login():
    time = datetime.datetime.now()
    with open("last_login.txt", "w") as file:
        file.write(str(time))
    return time

#Greeting based on last login
def lastLoginGreeting():
    with open("last_login.txt", "r") as file:
        timestamp = file.read().strip()
    try:
        last_time = datetime.datetime.strptime(timestamp, "%Y-%m-%d %H:%M:%S.%f")
        current_time = datetime.datetime.now()
        difference = current_time - last_time

        if(difference.days <= 1):
            return "Hello again "
        elif(difference.days > 7):
            return "Long time no see, Welcome Back! "
        elif(difference.days > 1):
            return "Welcome "
    except ValueError:
        return "Hello, "

