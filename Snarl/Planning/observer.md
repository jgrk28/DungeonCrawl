## Interface View ##
**Methods**
* void drawGame()
  * Draws the current game state using the corresponding viewing method
  * This acts as our update method for the observer pattern

## Class ObserverView implements View ##
**Methods**
* void drawGame()
  * Implements the interface method and displays the current level as a textual view string, along with other information pertinent to the game (total number of levels in the game, the current level number, and the game status)

The below JSON object is an example of the game state information that will be sent to the ObserverView:

```
{ 
levelView : (String)
numLevels : (Integer)
currLevel : (Integer)
gameStatus : (gameStatus)
}
```

A *gameStatus* is one of
* “Active”
* “Won”
* “Lost”

The below is an example of the output for the ObserverView
```
This game is : “Active” 
Current level : 2/3

XXXX              
XP.X              
X...***           
XXXX  *           
      *           
      *           
      *           
     X.XX         
     X..X         
     X..X         
     X..X    XXXXX
  ***..@.****GZ..X
  *  XXXX    X...X
  *          X...X
XX.XXX       X...X
X....X       XXXXX
X....X            
X...!X            
XXXXXX
```

## Class GameManager ##
**Fields**
* List<View> observers
  * List of all observers in the game to notify when the game state changes

**Methods**
* void attachObserver(View observer)
  * Adds the given View to the list of observers
* void detachObserver(View observer)
  * Removes the given View from the list of observers
* JSONObject getState()
  * Returns the current game state in the form of a JSONObject that is parsable by a View
* void notifyObserver()
  * Calls drawGame() on all of the observers


