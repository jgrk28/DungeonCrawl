package model;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.UIDefaults.LazyInputMap;
import modelView.EntityType;

public class Room implements LevelComponent {
	Point position;
	List<List<Entity>> componentMap;
	Map<Point, Hall> doors;
	
	public Room(Point position, List<List<Entity>> componentMap) {
		this.position = position;
		//Ensure component map is not empty
		this.componentMap = componentMap;
		this.doors = new HashMap();
	}

	public void connectHall(Point doorPosition, Hall adjHall) {
		Point bottomRightPosition = getBottomRightBound();
		if (validDoor(doorPosition, bottomRightPosition)) {
			doors.put(position, adjHall);
		} else {
			throw new IllegalArgumentException("Door is not on the boundary of the room");
		}
	}

	private boolean validDoor(Point doorPosition, Point bottomRightPosition) {
		return doorPosition.x == this.position.x ||
				doorPosition.x == bottomRightPosition.x ||
				doorPosition.y == this.position.y ||
				doorPosition.y == bottomRightPosition.y;
	}

	private boolean validPoint(Point point, Point bottomRightPosition) {
		return point.x > this.position.x &&
				point.x < bottomRightPosition.x &&
				point.y > this.position.y &&
				point.y < bottomRightPosition.y;
	}

	@Override
	public Point getTopLeftBound() {
		return this.position;
	}

	@Override
	public Point getBottomRightBound() {
		int roomY = this.componentMap.size();
		List<Entity> roomTopRow = this.componentMap.get(0);
		int roomX = roomTopRow.size();

		Point bottomRightPosition = new Point(this.position.x + roomX, this.position.y + roomY);
		return bottomRightPosition;
	}

	@Override
	public EntityType getEntityType(Entity entity) {
		return entity.getEntityType();
	}

	@Override
	public Entity getDestinationEntity(Point point) {
		Point bottomRight = getBottomRightBound();
		if (!validPoint(point, bottomRight)) {
			throw new IllegalArgumentException("Point not in component");
		}

		int relativeX = point.x - this.position.x;
		int relativeY = point.y - this.position.y;
		List<Entity> row = this.componentMap.get(relativeY);
		return row.get(relativeX);
	}
}

