## AdversaryClient ##
The AdversaryClient is a class that will eventually run on the client-side of the TCP connection. This design is currently specific to a local adversary, which will later be turned into a remote version by wrapping an adapter around the local version. For the local version, AdversaryClients will be created in the GameManger. When working over TCP, the adapter would handle new registrations. These registrations will map adversary names to sockets, and the GameManager would map names to Adversaries. 

The GameManager will send the level information at the start of each level, and player locations at the start of the adversary’s turn. The adversary will be sending moves to the GameManager when it is their turn. In the event that an adversary submits an invalid move, their turn would be skipped.

### Class AdversaryClient ###
**Fields**

* Level level
  * The full information for a level, provided at the beginning of a level
  * This level will be a copy of the actual level  
* Map\<Player, Point\> playerLocations
  * All locations of players currently in the level

**Methods**

* void getLevelStart(Level startLevel)
  * Gets the full level information at the beginning of the level  
* void updatePlayerLocations(Map\<Player, Point\> playerLocations)  
  * Update the adversary on all player locations  
  * This will be called at the beginning of the adversary’s turn  
* Point takeTurn()  
  * Prompts the adversary for a move and sends it to the GameManager  
  * Returns the point that the adversary is attempting to move to  

