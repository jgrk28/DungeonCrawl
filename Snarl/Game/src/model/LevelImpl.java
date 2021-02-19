package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import modelView.EntityType;

/**
 * Represents a Level within a game of Snarl
 * An ASCII representation of the Level may like this:
 * 
 * XXXX         
 * X..X         
 * X..X         
 * X..X    XXXXX
 * X...****....X
 * XXXX    X...X
 *         X...X
 *         X...X
 *         XXXXX
 *
 * Where each Entity corresponds to the following:
 * - Wall (X)
 * - Space (.)
 * - Hall Space (*)
 * - Key (!)
 * - Exit (@)
 * - Player (P)
 * - Adversary - Ghost (G)
 * - Adversary - Zombie (Z)
 * - Empty - where no entities have been placed  (" ")
 *  
 * For more examples, see test/view/TextualLevelView.java
 */
public class LevelImpl implements Level {
	
	//The map of LevelComponents that compose the Level
	private List<LevelComponent> levelMap;
	
	//The upper and lower bounds of the level within a Cartesian plane
	private Point topLeftBound;
	private Point bottomRightBound;
	
	//The map of all EntityTypes used to render the Level
	private ArrayList<ArrayList<EntityType>> viewableMap;
	
	//Ordered map of players that reflects the turn order 
	//and maps to each player to their current location
	private LinkedHashMap<Player,LevelComponent> playerLocations;
	
	//Ordered map of adversaries that reflects the turn order 
	//and maps to each adversary to their current location
	private LinkedHashMap<Adversary,LevelComponent> adversaryLocations;
	
	//True if exit has been unlocked by finding the key
	private Boolean exitUnlocked;
	
	//True if at least one player has exited the level
	private Boolean levelExited;
	
	/** 
	 * Initializes a new level
	 * @param levelMap - the map of all LevelComponents within the level
	 */
	public LevelImpl(List<LevelComponent> levelMap) {
		/** 
		 * A level is comprised of a series of rooms connected by hallways.
		 * A level is valid if no two rooms overlap, no two hallways overlap,
		 * and no hallways overlap with any rooms.
		 * 
		 * This validation will be implemented when levels are generated 
		 * automatically
		 */
		this.levelMap = levelMap;
	}
	
	//Initialize a random level from the given seed. Level will place the given players 
	//and adversaries
	public LevelImpl(List<Player> players, List<Adversary> adversaries, long seed) {
		//Create all the rooms, then connect with halls
		//Make sure the room does not overlap with existing rooms
		//Randomize dimensions of room
		//Helper function that takes in a list of level components and the random num, gives
		//us a size and location for a new room that is valid
		//Pass this to the level component and add to the levelMap
		//Set bounds for min and max number of rooms in a level
	}
	
	//Create a new random LevelComponent 
	private LevelComponent generateRandomRoom(Random generator) {
		//Generate a random size and position
		//Check if valid
		//Loop until valid values are found
		//Create the room
	}
	
	//
	private Boolean validRoomPlacement(Point topLeftPos, Point bottomRightPos) {
		//Loop through all things in the LevelMap
		for (LevelComponent component : levelMap) {
			Point componentTopLeft = component.getTopLeftBound();
			Point componentBottomRight = component.getBottomRightBound();
			
			//Check if the top left position is inside the component
			if (topLeftPos.x < componentBottomRight.x && ) {
				
			}
			//Check if rooms overlap, return false if this is the case
		}
		return true;
	}
	

	@Override
	public ArrayList<ArrayList<EntityType>> getMap() {
		this.topLeftBound = getTopLeft();
		this.bottomRightBound = getBottomRight();
		this.viewableMap = initializeEmptyMap();

		for (LevelComponent component : levelMap) {
			addToViewableMap(component);
		}

		return this.viewableMap;
	}

	/**
	 * Returns the top left boundary for the level
	 * This is the left most coordinate at which a LevelComponent is placed
	 * @return the coordinates of the top left boundary
	 */
	private Point getTopLeft() {
		//Level will always have at least one component
		LevelComponent firstComponent = this.levelMap.get(0);
		Point initTopLeft = firstComponent.getTopLeftBound();
		Integer minX = initTopLeft.x;
		Integer minY = initTopLeft.y;

		//Iterate through all LevelComponents
		//Return the minimum X and minimum Y values that are found
		for (LevelComponent component : levelMap) {
			Point topLeft = component.getTopLeftBound();
			if (topLeft.x < minX) {
				minX = topLeft.x;
			}
			if (topLeft.y < minY) {
				minY = topLeft.y;
			}
		}
		return new Point(minX, minY);
	}

	/**
	 * Returns the bottom right boundary for the level
	 * This is the right most coordinate at which a LevelComponent is placed
	 * @return the coordinates of the bottom right boundary
	 */
	private Point getBottomRight() {
		//Level will always have at least one component
		LevelComponent firstComponent = this.levelMap.get(0);
		Point initBottomRight = firstComponent.getBottomRightBound();
		Integer maxX = initBottomRight.x;
		Integer maxY = initBottomRight.y;
		
		//Iterate through all LevelComponents
		//Return the maximum X and maximum Y values that are found
		for (LevelComponent component : levelMap) {
			Point bottomRight = component.getBottomRightBound();
			if (bottomRight.x > maxX) {
				maxX = bottomRight.x;
			}
			if (bottomRight.y > maxY) {
				maxY = bottomRight.y;
			}
		}
		return new Point(maxX, maxY);
	}

	/**
	 * Initializes a new map of EntityTypes as EMPTY
	 * @return the map of EMPTY EntityTypes
	 */
	ArrayList<ArrayList<EntityType>> initializeEmptyMap() {
		//Determine the size of the map based on the bounds
		int xSize = this.bottomRightBound.x - this.topLeftBound.x + 1;
		int ySize = this.bottomRightBound.y - this.topLeftBound.y + 1;

		ArrayList<ArrayList<EntityType>> emptyMap = new ArrayList<>();

		//Iterate through the map and place an EMPTY EntityType at
		//each coordinate
		for (int i = 0; i < ySize; i++) {
			ArrayList<EntityType> emptyRow = new ArrayList<>();
			for (int j = 0; j < xSize; j++) {
				emptyRow.add(j, EntityType.EMPTY);
			}
			emptyMap.add(i, emptyRow);
		}
		return emptyMap;
	}

	/**
	 * Adds each Entity within a LevelComponent to the viewableMap
	 * @param component - the LevelComponent to add to the viewableMap
	 */
	private void addToViewableMap(LevelComponent component) {
		//Iterate through the viewableMap
		for (int i = this.topLeftBound.y; i <= this.bottomRightBound.y; i++) {
			for (int j = this.topLeftBound.x; j <= this.bottomRightBound.x; j++) {
				try {
					//Check if an Entity exists at the given coordinates in the LevelComponent
					//If these coordinates are not available for this LevelComponent, no changes are made
					Entity destEntity = component.getDestinationEntity(new Point(j, i));
					
					//Get the EntityType of the entity at these coordinates
					EntityType destEntityDrawable = component.getEntityType(destEntity);
					
					//Add the EntityType to the viewableMap
					int croppedYIndex = i - this.topLeftBound.y;
					int croppedXIndex = j - this.topLeftBound.x;
					ArrayList<EntityType> editRow = this.viewableMap.get(croppedYIndex);
					editRow.set(croppedXIndex, destEntityDrawable);
				} catch (IllegalArgumentException e) {
					//Do Nothing
				}
			}
		}
	}

	@Override
	public void actorAction(Actor actor, Point destination) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GameState isLevelOver() {
		if (!playerLocations.isEmpty()) {
			return GameState.ACTIVE;
		}
		if (levelExited) {
			return GameState.WON;
		}
		else {
			return GameState.LOST;
		}
	}

}
