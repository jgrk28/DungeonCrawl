package traveller_server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a town. A town has a name, a list of neighboring towns, and a list of characters
 * currently in the town.
 */
public class Town {

  private final String name;
  private final List<Town> neighbors;
  private final List<Character> characters;

  /**
   * Creates a town with the given name. The list of neighbors and the list of characters are
   * initialized as empty lists.
   *
   * @param name the name of the town
   */
  public Town(String name) {
    this.name = name;
    this.neighbors = new ArrayList<>();
    this.characters = new ArrayList<>();
  }

  /**
   * Gets the list of towns connected to this town.
   *
   * @return the list of towns connected to this town
   */
  public List<Town> getNeighbors() {
    return new ArrayList<>(this.neighbors);
  }

  /**
   * Connects this town bidirectionally to the given town (meaning that this town is connected to
   * the given town and the given town is connected to this town). This satisfies the property of
   * simple graphs that the edges are undirected.
   *
   * @param townToConnect the town to bidirectionally connect to this town
   * @return the list of adjacent (neighboring) towns to this town
   * @throws IllegalArgumentException if the given town to connect is the same as this town or if
   *                                  this town is already connected to the given town
   */
  public ArrayList<Town> connectTowns(Town townToConnect) throws IllegalArgumentException {
    if (this.equals(townToConnect)) {
      throw new IllegalArgumentException("Cannot connect town to itself");
    }

    if (this.neighbors.contains(townToConnect)) {
      throw new IllegalArgumentException("This town is already connected to the given town "
          + townToConnect.name + ".");
    }

    this.neighbors.add(townToConnect);
    townToConnect.neighbors.add(this);

    return new ArrayList<>(this.neighbors);
  }

  /**
   * Adds the given character to this town's list of characters if that character is not already
   * present in this town. If the character is already present in the town, nothing happens.
   *
   * @param character the character to add to this town's list of characters
   */
  public void addCharacter(Character character) {
    if (!this.characters.contains(character)) {
      this.characters.add(character);
    }
  }

  /**
   * Removes the given character from this town's list of characters if that character is present.
   *
   * @param character the character to remove
   */
  public void removeCharacter(Character character) {
    this.characters.remove(character);
  }

  /**
   * Determines if there are any characters present in this town.
   *
   * @return whether or not there are any characters currently in this town
   */
  public boolean charactersPresent() {
    return !this.characters.isEmpty();
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Town)) {
      return false;
    }

    Town that = (Town) obj;

    return (this.name.equals(that.name)); // towns must have unique names
  }
}
