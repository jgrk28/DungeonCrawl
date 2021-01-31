package traveller_server;

import java.util.Map;
import java.util.Set;

public interface TownNetwork {
	
	/*Purpose: Create a new town in the town network with the given name
	 * @param townName - a unique town name
	 * @throws IllegalArgumentException - if the given town name already exists in the town network*/
	void createTown(String townName) throws IllegalArgumentException;
	
	/*Purpose: Add connection2 between two given towns in the town network
	 * @param townNameA - an existing town name
	 * @param townNameB - an existing town name
	 * @throws IllegalArgumentException - if the given town names are the same, if either of the given towns 
	 * do not already exist in the town network, or if there already exists a path between the two given towns 
	 * in the town network*/
	void connectTowns(String townNameA, String townNameB) throws IllegalArgumentException;
	
	/*Purpose: Create a named character and place them in the given town
	 * @param characterName - a unique character name
	 * @param townName - an existing town name
	 * @throws IllegalArgumentException - if the given character name already exists in the town network or if 
	 * the given town does not already exist in the town network*/
	void placeCharacter(String characterName, String townName) throws IllegalArgumentException;
	
	/*purpose: Returns true if there exists a path3 through which a character could traverse to reach the given 
	 * town without running into any other characters, otherwise returns false
	 * @param townName - an existing town name
	 * @param characterName - an existing character name
	 * @throws IllegalArgumentException - if either the given character or town do not already exist in the town 
	 * network*/
	boolean hasClearPath(String characterName, String townName) throws IllegalArgumentException;
	
	public Boolean checkSameNetwork(Map<Town, Set<Town>> townConnections, Map<Town, Set<Character>> townInhabitants);

}
