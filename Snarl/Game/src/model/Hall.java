package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import modelView.EntityType;

/**
 * A LevelComponent that represents a Hall within a Level
 * A Hall connects two rooms in the Level
 */
public class Hall implements LevelComponent {
	
	//A list of all entities within this LevelComponent
	List<Entity> componentMap;
	
	//A list of all points that represent corners in a Hall
	List<Point> waypoints;
	
	//The Room at the beginning of the Hall
	Room startRoom;
	
	//The Room at the end of the Hall
	Room endRoom;

	/**
	 * Initializes a new Hall with the componentMap and waypoints
	 * Both the start and end room are null during initialization 
	 * @param componentMap - the map of all entities in the hall
	 * @param waypoints - a list of points that represent corners in the hall
	 */
	public Hall(List<Entity> componentMap, List<Point> waypoints) {
		this.componentMap = componentMap;
		this.waypoints = waypoints;
		this.startRoom = null;
		this.endRoom = null;
	}

	/**
	 * Connects the Hall to two given rooms
	 * @param positionStart - the connecting coordinates of the startRoom
	 * @param startRoom - the Room at the beginning of the Hall
	 * @param positionEnd - the connecting coordinates of the endRoom
	 * @param endRoom - the Room at the end of the Hall
	 * @throws IllegalArgumentException if the Hall is already connected
	 * or if the start and end Rooms are not orthogonally aligned with the Hall
	 */
	public void connectRooms(Point positionStart, Room startRoom, Point positionEnd, Room endRoom) {
		if (this.startRoom != null || this.endRoom != null) {
			throw new IllegalArgumentException("Hallway already connected");
		}

		if (validStart(positionStart)) {
			this.waypoints.add(0, positionStart);
			this.startRoom = startRoom;
		} else {
			throw new IllegalArgumentException("Start position not orthogonally in line with hallway "
					+ "waypoints");
		}

		if (validEnd(positionEnd)) {
			this.waypoints.add(positionEnd);
			this.endRoom = endRoom;
		} else {
			throw new IllegalArgumentException("End position not orthogonally in line with hallway "
					+ "waypoints");
		}
	}

	/**
	 * Checks if the Room being connected to the beginning of the Hall is a valid connection
	 * @param positionStart - the connecting coordinates of the Room
	 * @return True if the room is orthogonally aligned with the first waypoint, false otherwise
	 */
	private boolean validStart(Point positionStart) {
		if (this.waypoints.isEmpty()) {
			return true;
		}
		Point firstWaypoint = this.waypoints.get(0);
		return firstWaypoint.x == positionStart.x ||
				firstWaypoint.y == positionStart.y;
	}

	/**
	 * Checks if the Room being connected to the end of the Hall is a valid connection
	 * @param positionEnd - the connecting coordinates of the Room
	 * @return True if the room is orthogonally aligned with the last waypoint, false otherwise
	 */
	private boolean validEnd(Point positionEnd) {
		if (this.waypoints.isEmpty()) {
			return true;
		}
		Point lastWaypoint = this.waypoints.get(this.waypoints.size() - 1);
		return lastWaypoint.x == positionEnd.x ||
				lastWaypoint.y == positionEnd.y;
	}


	@Override
	public Point getTopLeftBound() {
		//Always will be at least 2 waypoints because the doors are represented as waypoints
		Point firstWaypoint = this.waypoints.get(0);
		Integer minX = firstWaypoint.x;
		Integer minY = firstWaypoint.y;

		//Iterate through all waypoints to find the minimum x and y values
		for (Point waypoint : this.waypoints) {
			if (waypoint.x < minX) {
				minX = waypoint.x;
			}
			if (waypoint.y < minY) {
				minY = waypoint.y;
			}
		}
		return new Point(minX, minY);
	}

	@Override
	public Point getBottomRightBound() {
		//Always will be at least 2 waypoints because the doors are represented as waypoints
		Point firstWaypoint = this.waypoints.get(0);
		Integer maxX = firstWaypoint.x;
		Integer maxY = firstWaypoint.y;

		//Iterate through all waypoints to find the maximum x and y values
		for (Point waypoint : this.waypoints) {
			if (waypoint.x > maxX) {
				maxX = waypoint.x;
			}
			if (waypoint.y > maxY) {
				maxY = waypoint.y;
			}
		}
		return new Point(maxX, maxY);
	}

	@Override
	public EntityType getEntityType(Entity entity) {
		EntityType generalType = entity.getEntityType();
		//If the Entity is a SPACE, return the HALL_SPACE EntityType
		switch (generalType) {
			case SPACE:
				return EntityType.HALL_SPACE;
			default:
				return generalType;
		}
	}

	@Override
	public Entity getDestinationEntity(Point point) {
		List<Integer> segments = new ArrayList<>();
		for (int i = 0; i < this.waypoints.size() - 1; i++) {
			Point thisWaypoint = waypoints.get(i);
			Point nextWaypoint = waypoints.get(i + 1);
			Integer length = Math.abs(thisWaypoint.x - nextWaypoint.x +
					thisWaypoint.y - thisWaypoint.y);
			segments.add(length);
		}

		//Iterate through all distances to see when the position at that distance equals the given point
		//Then return entity at that distance.
		for (int j = 0; j < this.componentMap.size(); j++) {

		}
		return null;
	}
}