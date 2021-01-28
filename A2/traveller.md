Assignment 2 Task 1: Interface Specification  
Jacob Kaplan and ArDelia MacPhail  

Package: Traveller  

The package Traveller provides the services of a route planner through a network of towns for a role-playing game. We request that this package be implemented using Java 8 (OpenJDK version 1.8.0_262) and should satisfy the definition outlined below:  

### Data ###  
Character object with the following fields:  
* Name  
* Location   

Town object with the following fields:  
* Name  
* List of towns that the town is connected to  

### Operations ###  
Characters  
* createCharacter  
  * Initializes a new character with a unique name  
  * Returns a character object  
* placeCharacter  
  * Void method that places a character in a town and sets their location  
* moveCharacter  
  * Void method that moves a character to another town and updates their location  
* validateMove  
  * Checks that a character can reach a designed town without running into other characters  
  * Returns a boolean. True if the character is able to move, false otherwise    

Towns  
* createTown  
  * Initializes a new town with a unique town  
  * Returns a town object  
* connectTowns  
  * Connects towns to one another to form a network  
  * Returns an ArrayList of adjacent towns  

### Behavior ###
* Characters and towns must have unique names  
* The connectTowns method must validate that the network satisfies the rules of a simple graph  
* Connected towns must have a unique list of towns, and cannot include itself  
