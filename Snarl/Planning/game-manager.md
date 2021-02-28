## Game Manager ##
The GameManager acts as the controller for our MVC design. It communicates between the model and the views. It sends and receives information over TCP connection with the PlayerClient view. It has an instance of the model, and calls into it to change the game state. 

### Class GameManager ###

**Fields**
* Dungeon dungeon
  * Represents the entire game state, including levels, rooms, hallways, players, adversaries, and other entities
* Map<String, Socket> playerClient
  * Represents the player name and corresponding socket for each PlayerClient in the game
* Map<String, Socket> adversaryClient
  * Represents the adversary name and corresponding socket for each AdversaryClient in the game

**Methods**
* void registerActors()
  * Accept registrations for new players and adversaries and fills in Client maps
* void startGame(Level level)
  * Initialize a Dungeon with the provided level with registered players and adversaries
* void playGame()
  * Manage levels within the dungeon
  * Starts each level and manages the result once the level ends
  * If the level was won, proceed to the next level
  * Otherwise, end the game
  * This method will primarily be needed when we start working with multiple levels
* void startLevel()
  * Place players and adversaries in the current level
* void playLevel()
  * Manage turns for players and adversaries until the level is won or lost
* void endGame()
  * Send results to players after a game has finished

