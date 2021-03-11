## Interface View ##
**Methods**
* void draw()
  * Draws the current game state using the corresponding viewing method

## Class TextualObserverView implements View ##
**Methods**
* void draw()
  * Implements the interface method and displays the current level as a textual view string, along with other information pertinent to the game (total number of levels in the game, the current level number, and the game status)

The observer view will render the game as a string which can then be sent over TCP protocol or simply through a local call to the observer.
```
{ 
gameState : (String)
}
```

The below is an example of the output for the TextualObserverView
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
* List\<Observer\> observers
  * List of all observers in the game to notify when the game state changes

**Methods**
* void attachObserver(Observer observer)
  * Adds the given Observer to the list of observers
* void detachObserver(Observer observer)
  * Removes the given Observer from the list of observers
* JSONObject getState()
  * Returns the current game state in the form of a JSONObject that is parsable by an Observer
* void notifyObservers()
  * Calls update() on all of the observers with the JSONObject from getState
  
## Class Observer ##
  **Methods**
  * void update(JSONObject gameState)
    * Displays the given game state. In the future this will get the game state over TCP


