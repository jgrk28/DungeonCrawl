Assignment 2 Task 1: Interface Specification  
Jacob Kaplan and ArDelia MacPhail  

Package: Traveller  

The package Traveller provides the services of a route planner through a network of towns for a role-playing game. We request that this package be implemented using Java 8 (OpenJDK version 1.8.0_262) and should satisfy the definition outlined below:  

### Data ###  
Character class with the following fields:  
* Name (String)  
* Location (Town) 

Town class with the following fields:  
* Name (String)
* List of towns that the town is connected to (ArrayList<Town>)

### Operations ###  
Characters  
* createCharacter  
  * String -> Character
  * Initializes a new character with a unique name
* placeCharacter  
  * Town -> Void
  * Places this character in a town and sets their location  
* moveCharacter 
  * Town -> Void
  * Moves a character to another town and updates their location  
* validateMove
  * Town -> Boolean
  * Checks that this character can reach a designed town without running into other characters

Towns  
* createTown
  * String -> Town
  * Initializes a new town with a unique name  
* connectTowns
  * Town -> ArrayList<Town>
  * Adds a new town this towns list of neighbors 
  * Returns an ArrayList of adjacent towns  

### Behavior ###
* Errors thrown if these rules are violated:
  * Characters and towns must have unique names  
  * The connectTowns method must validate that the network satisfies the rules of a simple graph  
  * Connected towns must have a unique list of towns, and cannot include itself  
