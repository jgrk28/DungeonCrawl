## PlayerClient ##

The PlayerClient is a class that runs on the user's side of the TCP connection. This displays relevant information to the user, and sends moves and other commands that the user would like to perform in the game to the GameManager. The GameManager sends the game state specific to the user. This includes what the user can see in the level, if they are still in the active level, and whether or not the game is over. 

### Class PlayerClient ###

**Fields**
* Point location
  * Current overall location in the dungeon
* String name
  * Unique identifying name
* List\<List\<EntityType\>\> viewableMap
  * A subsection of the level where a player can see 2 grid units away in any cardinal or diagonal direction
* Boolean inLevel
  * True if the player is still active in the level. False if the player has been killed or has exited the level

**Methods**
* void joinGame()
  * Registers the player with the GameManager using a unique name
  * This may be called from the constructor
* void displayGameState()
  * Shows the player’s view of the level based on their current location
* void takeTurn()
  * Prompts the player for a move and sends it to the GameManager
* boolean checkReturnMoveValid()
  * Checks that the turn is valid and correctly processed by the GameManager
* void displayGameResult()
  * Displays the results of the game to the player

