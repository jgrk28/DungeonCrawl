package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Line;
import javax.swing.border.EmptyBorder;
import modelView.EntityType;

public class LevelImpl implements Level {
	
	private List<LevelComponent> levelMap;
	private Point topLeftBound;
	private Point bottomRightBound;
	private ArrayList<ArrayList<EntityType>> viewableMap;
	
	public LevelImpl(List<LevelComponent> levelMap) {
		// A level is comprised of a series of rooms connected by hallways.
		// A level is valid if no two rooms overlap, no two hallways overlap,
		// and no hallways overlap with any rooms.
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

	private Point getTopLeft() {
		//Level will always have at least one component
		LevelComponent firstComponent = this.levelMap.get(0);
		Point initTopLeft = firstComponent.getTopLeftBound();
		Integer minX = initTopLeft.x;
		Integer minY = initTopLeft.y;

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

	private Point getBottomRight() {
		//Level will always have at least one component
		LevelComponent firstComponent = this.levelMap.get(0);
		Point initBottomRight = firstComponent.getBottomRightBound();
		Integer maxX = initBottomRight.x;
		Integer maxY = initBottomRight.y;

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

	ArrayList<ArrayList<EntityType>> initializeEmptyMap() {
		int xSize = this.bottomRightBound.x - this.topLeftBound.x;
		int ySize = this.bottomRightBound.y - this.topLeftBound.y;

		ArrayList<ArrayList<EntityType>> emptyMap = new ArrayList<>();

		for (int i = 0; i < ySize; i++) {
			ArrayList<EntityType> emptyRow = new ArrayList<>();
			for (int j = 0; j < xSize; j++) {
				emptyRow.add(j, EntityType.EMPTY);
			}
			emptyMap.add(i, emptyRow);
		}
		return emptyMap;
	}

	private void addToViewableMap(LevelComponent component) {
		for (int i = this.topLeftBound.y; i < this.bottomRightBound.y; i++) {
			for (int j = topLeftBound.x; j < bottomRightBound.x; j++) {
				try {
					Entity destEntity = component.getDestinationEntity(new Point(j, i));
					EntityType destEntityDrawable = component.getEntityType(destEntity);
					ArrayList<EntityType> editRow = this.viewableMap.get(i);
					editRow.set(j, destEntityDrawable);
				} catch (IllegalArgumentException e) {
					//Do Nothing
				}
			}
		}
	}



}
