package traveller_server;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a network of towns and characters. The network of towns is represented as a simple,
 * undirected graph. Each town in the network and each character in the network must have a unique
 * name.
 */
public class TownNetwork {

  private final Map<String, Town> towns;
  private final Map<String, Character> characters;

  /**
   * Constructs an empty network of towns with an empty list of towns and an empty list of
   * characters.
   */
  public TownNetwork() {
    this.towns = new HashMap<>();
    this.characters = new HashMap<>();
  }

  /**
   * Creates a character with the given name and adds the character to this network.
   *
   * @param name the name for the character
   * @return the new character that has been created
   * @throws IllegalArgumentException if there is already a character with the given name in this
   *                                  network
   */
  public Character createCharacter(String name) throws IllegalArgumentException {
    if (this.characters.containsKey(name)) {
      throw new IllegalArgumentException("Character with given name already exists");
    }

    Character character = new Character(name);

    this.characters.put(name, character);
    return character;
  }

  /**
   * Creates a town with the given name and adds the town to this network.
   *
   * @param name the name for the town
   * @throws IllegalArgumentException if there is already a town with the given name in this
   *                                  network
   */
  public Town createTown(String name) throws IllegalArgumentException {
    if (this.towns.containsKey(name)) {
      throw new IllegalArgumentException("Town with given name already exists");
    }

    Town town = new Town(name);

    this.towns.put(name, town);
    return town;
  }
}
