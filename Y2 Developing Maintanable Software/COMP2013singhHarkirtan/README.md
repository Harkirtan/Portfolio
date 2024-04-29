# COMP2013 Developing Maintainable Software - Breakout

## Working features

- JavaFX development of both debug classes, in forms of a stage for the console and gridPane for the panel. Added JAVAFX buttons and sliders
- Wall class breakdown into LevelManager, where the levels are generated, performed to split responsibilities of the level generation from wall
- Added menu in JAVAFX contains launch button and exit button. Launch leads to a start options stage that allows user to specify how many lines per level and which ball to play with, contains a button launch to go to game
- Addition of a Ball which is a larger blue ball, added a blue clay brick too.
- Renamed all bricks and balls to make it clearer which ones they are
- Addition of a Ball factory class which creates ball objects using factory pattern to hide classes from initialisation
- Addition of a Brick factory class which creates brick objects using factory pattern to hide classes from initialisation
- Addition of 3 levels, alternating layer level, arrow (true checkerboard) level and a random generating level
- Refactored m_ to all member variables, made them private and added accessors to all classes to support encapsulation
- Addition of a general alertbox class which creates JAVAFX stages to display a message passed or take input to update high score
- Added CSS to all JAVAFX added for style.
- Extracted game menu logic from GameBoard class to new GameMenu class
- GameBoard class split to GameBoardView and GameBoardController to split responsibilities in order to adhere to MVC principles
- Packaged all code in MVC to make it easier to understand which classes perform which actions. Added sub packages such as debug, wall, brick, ball to differentiate functionalities.
- Refactored Crack class as extracted from Brick, performed because Crack was only used in Cement Brick
- Addition of a ScoreManager class to keep track of currentscore and totalscore. Totalscore and currentscore displayed to user at the end of each level or game over. Update of highscore in highscore.dat as name:score
- makeBall method separated from wall constructor 
- Added Singleton pattern to Wall class to ensure only 1 wall instance is present. getInstance is used to maintain consistency. This required a static member variable m_wall. This is required so 1 instance is available in the game.
- Removed wall constructor and replaced with initialise
- Added music via MusicManager class, allows player to start/stop music
- Added Sprites to Ball and Paddle via SpriteLoader class
- Deleted images and colours from balls and paddle as unused

## Incorrectly functioning features

- Nothing to my knowledge

## Unimplemented features

- JAVAFX conversion for the rest of the classes, not implemented because lack of experience and knowledge in how to convert
- Brick Sprites because of game lag when attempting to add

## New Java classes introduced

- MusicManager 
- ScoreManager 
- SpriteLoader 
- BallBlue 
- BallFactory 
- BrickClayBlue
- BrickFactory 
- LevelManager
- AlertBox
- StartOption
- StartScreen
- GameMenu

## Modified Java classes

I modified all classes provided to adhere to Bob's coding conventions.

- GameBoardController
- GameFrame
- Ball
- BallRubber
- Brick
- BrickCement
- BrickClayRed
- BrickSteel
- Crack
- Paddle
- Wall
- DebugConsole
- DebugPanel
- GameBoardView
