package traveller_server;

/**
 * This interface represents a character in the town network.
 */
public interface Character {
	
	/**
	 * Returns true if the given Character is the same as this Character
	 * @param name - the name of the given Character
	 * @return true if the character name is the same, false otherwise
	 */
	public Boolean checkCharacterName(String name);

}
