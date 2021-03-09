package modelView;

import java.util.ArrayList;

/**
 * Represents the model view from a players perspective
 */
public interface PlayerModelView {
  /**
   * Gets the map for the level and returns a read-only version of the part that this player can
   * see.
   * @return a map of all EntityTypes within the Level
   */
  ArrayList<ArrayList<EntityType>> getMap();
}
