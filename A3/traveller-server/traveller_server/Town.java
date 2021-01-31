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

}
