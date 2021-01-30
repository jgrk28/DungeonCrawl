# Traveller Package Memo

The goal of this package is to provide the services needed of a route planner for a network of towns in a role-playing game. The core functionality of the package must support the creation of towns and characters,
the placement of characters in towns, and the ability query whether a specified character can reach a designated town without running into any other characters. This package should be implemented in Java 1.8.0.

## Data Definitions
Following is the high level outline of the interfaces and classes needed to model the game. Note that it is assumed all classes
override equals and hashCode.

__interface TownNetwork__<br />
- ```void createTown(String townName)```
  - purpose: Create a new town in the town network<sup>1</sup> with the given name
  - args:
    - @param townName - a unique town name
    - @throws IllegalArgumentException - if the given town name already exists in the town network<br />
- ```void connectTowns(String townNameA, String townNameB)```
  - purpose: Add connection<sup>2</sup> between two given towns in the town network
  - args:
    - @param townNameA - an existing town name
    - @param townNameB - an existing town name
    - @throws IllegalArgumentException - if the given town names are the same, if either of the given towns do not already exist in the town network, or if there already exists a path between the two given towns in the town network<br />
- ```void placeCharacter(String characterName, String townName)```
  - purpose: Create a named character and place them in the given town
  - args:
    - @param characterName - a unique character name
    - @param townName - an existing town name
    - @throws IllegalArgumentException - if the given character name already exists in the town network or if the given town does not already exist in the town network<br />
- ```boolean hasClearPath(String characterName, String townName)```
  - purpose: Returns true if there exists a path<sup>3</sup> through which a character could traverse to reach the given town without running into any other characters, otherwise returns false
  - args:
     - @param townName - an existing town name
     - @param characterName - an existing character name
     - @throws IllegalArgumentException - if either the given character or town do not already exist in the town network

__class Network implements TownNetwork__
- Map<Town, Set<Town>> townConnections;       // A map of towns to the towns they are connected to
- Map<Town, Set<Character>> townInhabitants;  // A map of towns to characters placed in them

__interface Character__

__class CharacterImpl implements Character__
- String name;

__interface Town__
  
__class TownImpl implements Town__
- String name;

<sup>1</sup>town network - a simple graph specification together with a placement of in-game characters<br />
<sup>2</sup>connection - a bidirectional route through which placed characters in the town network could theoretically traverse to get from one town to the other<br />
<sup>3</sup>path - a series of zero or more contiguous connections between towns<br />
