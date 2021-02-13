package model;

import java.awt.Point;
import java.util.List;

public class Hall {
	List<Entity> componentMap;
	List<Point> waypoints;
	
	Hall(List<Entity> componentMap, List<Point> waypoints) {
		this.componentMap = componentMap;
		this.waypoints = waypoints;
	}
	
	void placeDoors(Point startPosition, LevelComponent startRoom, Point endPosition, LevelComponent endRoom) {
		//Check that the position of the start is orthogonally in line with the first waypoint (same with end)
		//Add door locations to the beginning and end of the list of waypoints
		//place door within componentList		
	}

}