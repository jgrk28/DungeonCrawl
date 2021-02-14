package model;

import java.awt.Point;
import modelView.EntityType;

public interface LevelComponent {
  Point getTopLeftBound();
  Point getBottomRightBound();
  
  EntityType getEntityType(Entity entity);

  Entity getDestinationEntity(Point point);
}
