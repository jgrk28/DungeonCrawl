package traveller_server;

/**
 * This interface represents a character in the town network.
 */
public interface Character {
	
	/**
	 * Returns true if the given Character has the same name as this Character
	 * @param name - the name of the given Character
	 * @return true if the character name is the same, false otherwise
	 */
	public Boolean checkCharacterName(String name);
	
	/**
	 * Overrides the equals method
	 * @param obj - the given object
	 * @return true if the Characters are the same Character, false otherwise
	 */
	public boolean equals(Object obj);
	
	/**
	 * Overrides the hashcode method
	 * @return the corresponding hashcode
	 */
	public int hashCode();

}
