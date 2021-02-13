package model;

import java.awt.Point;
import java.util.List;

public class Hall implements LevelComponent {
	//Changing to a matrix since halls should have walls
	List<List<Entity>> componentMap;
	List<Point> waypoints;
	
	public Hall(List<List<Entity>> componentMap, List<Point> waypoints) {
		this.componentMap = componentMap;
		this.waypoints = waypoints;
	}
	
	public void placeDoors(Point startPosition, LevelComponent startRoom, Point endPosition, LevelComponent endRoom) {
		//Check that the position of the start is orthogonally in line with the first waypoint (same with end)
		//Add door locations to the beginning and end of the list of waypoints
		//place door within componentList		
	}



}