# Milestone 6 - Refactoring Report

**Team members:**
Jacob Kaplan, ArDelia MacPhail

**Github team/repo:**
https://github.ccs.neu.edu/CS4500-S21/Menande/tree/master/


## Plan
Main Goals:
1) Changing structure to allow for easier extension and satisfy later expected design goals.
* Combine Key and Exit into Item class
* Look into if changing to tiles that can hold entities instead of just having single entities in the map
* When getting cropped player view, indicate that we are there or stop sending empty list
2) Address outstanding gaps in our code from previous milestone descriptions
* Refactor PlayerClient so that it has knowledge of its location relative to the levels origin
* Confirm if player can jump over another player and fix code if it can
3) Abstracting code and breaking up large methods to make things easier to read/understand
* Unify test setup of all the model structures to test and make them easily accessible
* Refactor playerAction and adversaryAction
  * If we don't end up using moveActor, remove it
* In general reduce long function into smaller subcomponents
* Combine getOrigin and getTopLeftBound into one function
4) Finish testing on anything that is not fully tested
* Tests for TexualPlayerView when player is not active and when they have moved to the next level
* Tests for the GameManager
* Tests for placeKey and placeExit


## Changes

Over the course of this week, we made some substantial changes to our model and representation of entities within the game. We separated how items (like exit and key) are represented in contrast to tiles. Instead of having items, spaces, walls, and actors all being represented as entities, we created a more structured implementation to account for the differences between them. Spaces and walls are now represented as tiles, which can contain both items and actors. This more easily allows for actors to be located at the same position as a key or exit. We also combined key and exit into an Item class, which allowed us to abstract out common fields and methods. This will also be useful if there is more than one exit and key within the level, allowing us to potentially support Snarl games with multiple keys and exits.

We also cleaned up outstanding technical debt to ensure our representation follows the description outlined in the project milestones. This included modifying the player's cropped view of the level to include empty space when their view exceeds the boundaries of the level. We also made a small modification to allow players to jump over other players during a move, based on conversations in Piazza. 

We focused on small changes as well that would make our code more readable and easier to work with for future milestones. This included abstracting helper methods in playerAction and adversaryAction, combining getOrigin and getTopLeftBound since both methods had the same functionality, and removing the moveActor method that we were not utilizing.

We unified how the models were created for our test, such that we only need to make changes in one place if the model changes. This made changing our tests after refactoring entities and items much easier. We also added tests for tiles, items (including placeItem), and TextualPlayerView. 


## Future Work

There are some additional changes we would like to make moving forward. This includes additional testing for the GameManager, which we will likely address during Milestone 7. We also need to ensure that our PlayerClient receives all the necessary information about the GameState, including their position relative to the origin. The information that is needed will be clearer once more milestones have been released. 


## Conclusion

We thought refactor week was helpful since we were able to make changes that will definitely help us in the coming milestones. Our code now much closer resembles what we believe has been asked for, and should be more extensible than it was previously. 