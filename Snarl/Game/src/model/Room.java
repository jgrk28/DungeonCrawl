package model;

import java.awt.Point;
import java.util.List;

public class Room implements LevelComponent {
	Point position;
	List<List<Entity>> componentMap;
	
	public Room(Point position, List<List<Entity>> componentMap) {
		this.position = position;
		this.componentMap = componentMap;
	}
	
	public void placeDoor(Point position, LevelComponent connection) {
		//Find the relative position within the matrix	
		//check if it is a valid place to put it
		//place door within componentMap		
	}

}

