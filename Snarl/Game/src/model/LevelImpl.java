package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import modelView.EntityType;

/**
 * Represents a Level within a game of Snarl
 */
public class LevelImpl implements Level {
	
	//The map of LevelComponents that compose the Level
	private List<LevelComponent> levelMap;
	
	//The upper and lower bounds of the level within a Cartesian plane
	private Point topLeftBound;
	private Point bottomRightBound;
	
	//The map of all EntityTypes used to render the Level
	private ArrayList<ArrayList<EntityType>> viewableMap;
	
	/** 
	 * Initializes a new level
	 * @param levelMap - the map of all LevelComponents within the level
	 */
	public LevelImpl(List<LevelComponent> levelMap) {
		//TO-DO
		//A level is comprised of a series of rooms connected by hallways.
		//A level is valid if no two rooms overlap, no two hallways overlap,
		//and no hallways overlap with any rooms.
		this.levelMap = levelMap;
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



}
