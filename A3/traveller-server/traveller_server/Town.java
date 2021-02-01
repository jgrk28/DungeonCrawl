package traveller_server;

/**
 * This interface represents a town in the town network.
 */
public interface Town {
	
	/**
	 * Returns true if the given Town is the same as this Town
	 * @param name - the name of the given town
	 * @return true if the town name is the same, false otherwise
	 */
	public Boolean checkTownName(String name);
	
	/**
	 * Overrides the equals method
	 * @param obj - the given object
	 * @return true if the towns are the same town, false otherwise
	 */
	public boolean equals(Object obj);
	
	/**
	 * Overrides the hashcode method
	 * @return the corresponding hashcode
	 */
	public int hashCode();

}
