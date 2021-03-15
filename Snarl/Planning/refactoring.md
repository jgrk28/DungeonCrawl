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

Summarize the work you have performed during this week.


## Future Work

Summarize work you'd still like to do if there's time. This can include features 
you'd like to implement if given time.


## Conclusion

Any concluding remarks.