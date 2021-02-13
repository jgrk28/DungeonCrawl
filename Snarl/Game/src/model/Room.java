package model;

import java.awt.Point;
import java.util.List;

public class Room {
	Point position;
	List<List<Entity>> componentMap;
	
	Room(Point position, List<List<Entity>> componentMap) {
		this.position = position;
		this.componentMap = componentMap;
	}
	
	void placeDoor(Point position, LevelComponent connection) {
		//Find the relative position within the matrix	
		//check if it is a valid place to put it
		//place door within componentMap		
	}

}

