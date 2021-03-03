## Game Manager ##
The GameManager acts as the controller for our MVC design. It communicates between the model and the views. It will eventually send and receive information over TCP connection with the PlayerClient view. For now the PlayerClient will be run locally. It has an instance of the model, and calls into it to change the game state. 

### Class GameManager ###

**Fields**
* Dungeon dungeon
  * Represents the entire game state, including levels, rooms, hallways, players, adversaries, and other entities
* Map<String, PlayerClient> playerClients
  * Represents each player name and corresponding PlayerClient in the game
  * The PlayerClient value will be changed to a Socket when we move to using a TCP connection for communication
* Map<String, AdversaryClient> adversaryClients
  * Represents each adversary name and corresponding AdversaryClient in the game
  * The AdversaryClient value will be changed to a Socket when we move to using a TCP connection for communication

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

