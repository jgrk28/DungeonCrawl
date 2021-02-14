package model;

import java.awt.Point;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import modelView.EntityType;

public class Hall implements LevelComponent {
	List<Entity> componentMap;
	List<Point> waypoints;
	Room startRoom;
	Room endRoom;

	
	public Hall(List<Entity> componentMap, List<Point> waypoints) {
		this.componentMap = componentMap;
		this.waypoints = waypoints;
		this.startRoom = null;
		this.endRoom = null;
	}

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

	private boolean validStart(Point positionStart) {
		if (this.waypoints.isEmpty()) {
			return true;
		}
		Point firstWaypoint = this.waypoints.get(0);
		return firstWaypoint.x == positionStart.x ||
				firstWaypoint.y == positionStart.y;
	}

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
		switch (generalType) {
			case SPACE:
				return EntityType.HALL_SPACE;
			default:
				return generalType;
		}
	}

	//List<Entity> componentMap;
//	List<Point> waypoints;
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