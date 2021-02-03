package traveller_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a character in the game. A character has a name and a location.
 */
public class Character {

  private final String name;
  private Town location;

  /**
   * Creates a character with the given name. The location of the character is initially set to
   * {@code null}.
   *
   * @param name the name for the character
   */
  public Character(String name) {
    this.name = name;
    this.location = null;
  }

  /**
   * Places this character in the given town. This should only be called once to give the character
   * an initial town.
   *
   * @param town the town to place this character in
   * @throws IllegalArgumentException if this character has already been placed in a town
   */
  public void placeCharacter(Town town) throws IllegalArgumentException {
    if (this.location != null) {
      throw new IllegalArgumentException("Character has already been placed in a town.");
    }

    this.location = town;
    town.addCharacter(this);
  }

  /**
   * Moves this character from their current town to the given town by removing this character from
   * their current town and adding them to the given town.
   *
   * @param town the new town for this character
   */
  public void moveCharacter(Town town) {
    this.location.removeCharacter(this);

    this.location = town;
    town.addCharacter(this);
  }

  /**
   * Determines whether or not this character can reach the given town without going through a town
   * with any characters in it.
   *
   * @param dest the destination town
   * @return whether or not this character can reach the given town without encountering any other
   * characters. If the character does not have a town yet, returns false.
   * @throws IllegalArgumentException if this character has not yet been placed in a town
   */
  public boolean validateMove(Town dest) throws IllegalArgumentException {
    if (this.location == null) {
      throw new IllegalArgumentException("Character has not yet been placed in a town.");
    }

    if (this.location.equals(dest)) {
      return true;
    }

    Map<Town, Boolean> visited = new HashMap<>();
    visited.put(this.location, true);

    List<List<Town>> layers = new ArrayList<>();

    List<Town> layer0 = new ArrayList<>();
    layer0.add(this.location);

    layers.add(layer0);

    int i = 0;
    while (!layers.get(i).isEmpty()) {

      layers.add(new ArrayList<>());
      List<Town> currentLayer = layers.get(i);

      for (Town town : currentLayer) {
        for (Town neighbor : town.getNeighbors()) {
          if (neighbor.equals(dest)) {
            return true;
          } else {
            if (!neighbor.charactersPresent()) {
              if (visited.get(neighbor) == null || !visited.get(neighbor)) {
                visited.put(neighbor, true);
                layers.get(i + 1).add(neighbor);
              }
            }
          }
        }
      }
      i++;
    }

    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Character)) {
      return false;
    }

    Character that = (Character) obj;

    return (this.name.equals(that.name)); // characters must have unique names
  }
}
