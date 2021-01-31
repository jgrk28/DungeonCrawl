package traveller_server;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents a network of towns. It has the ability to create new towns,
 * connect existing towns, place characters in a town, and determine if they have
 * a clear path to a different town.
 */
public class Network implements TownNetwork{
	
	// A map of towns to the towns they are connected to
	private Map<Town, Set<Town>> townConnections;
	
	// A map of towns to characters placed in them
	private Map<Town, Set<Character>> townInhabitants;

	@Override
	public void createTown(String townName) throws IllegalArgumentException {
		// Check to see if the given town name already exists
		// If it does not, create the town and add it to the network
		if (getTown(townName).equals(null)) {
			Town newTown = new TownImpl(townName);
			Set<Town> emptySet = new HashSet<Town>();
			townConnections.put(newTown, emptySet);
		} else {
			throw new IllegalArgumentException("A town with this name already exists");
		}	
	}

	@Override
	public void connectTowns(String townNameA, String townNameB) throws IllegalArgumentException {
		// Get the town objects associated with the provided town names
		Map.Entry<Town, Set<Town>> townA = getTown(townNameA);
		Map.Entry<Town, Set<Town>> townB = getTown(townNameB);
		
		// Check to ensure that both towns exist in the network, that they are not already connected,
		// and the town names are not the same
		if (townA.equals(null) || townB.equals(null)) {
			throw new IllegalArgumentException("Town does not exist in the network");
		} else if (townA.getValue().contains(townB.getKey()) || townB.getValue().contains(townA.getKey())) {
			throw new IllegalArgumentException("Towns are already connected");
		} else if (townA.getValue().equals(townB.getValue())) {
			throw new IllegalArgumentException("Cannot connect town to itself");
		} 
		
		// Add the town to the set of towns for Town A and Town B
		Set<Town> newASet = townA.getValue();
		newASet.add(townB.getKey());
		Set<Town> newBSet = townB.getValue();
		newBSet.add(townA.getKey());
		
		// Replace the existing town set in the town network for the given towns
		townConnections.replace(townA.getKey(), newASet);
		townConnections.replace(townB.getKey(), newBSet);
	}

	@Override
	public void placeCharacter(String characterName, String townName) throws IllegalArgumentException {
		
		// Identify the inhabitants associated with the given town
		Map.Entry<Town, Set<Character>> townCharacters = getInhabitants(townName);
		
		// Check that the town exists
		if (townCharacters.equals(null)) {
			throw new IllegalArgumentException("No town found");
		}
		
		Set<Character> inhabitantSet = townCharacters.getValue();
		
		// Check that the given character name does not already exist in the network
		for (Character currCharacter : inhabitantSet) {
			if (currCharacter.checkCharacterName(characterName)) {
				throw new IllegalArgumentException("This character already exists in the network");
			}		
		}
		
		Character newCharacter = new CharacterImpl(characterName);
		inhabitantSet.add(newCharacter);
		townInhabitants.replace(townCharacters.getKey(), inhabitantSet);	
	}

	@Override
	public boolean hasClearPath(String characterName, String townName) throws IllegalArgumentException {
		// Track all towns that have been visited 
		Set<Town> visitedTowns = new HashSet<Town>();
		
		// Identify the current town of the given character
		Town currTown = findInhabitant(characterName);
		
		Map.Entry<Town, Set<Town>> destTown = getTown(townName);
		
		// Check if the character or town do not exist in the town network
		if (currTown.equals(null)) {
			throw new IllegalArgumentException("Could not find character");
		}
		if (destTown.equals(null)) {
			throw new IllegalArgumentException("Could not find town");
		}
		
		// Identify if there is a clear path from the current town to the destination down
		return recursiveClearPath(currTown, destTown.getKey(), visitedTowns);
	}
	
	/**
	 * Perform depth first search on the town network to determine if the character
	 * can move to the destination town without encountering other characters
	 * @param currTown - the town the character is currently placed at
	 * @param destTown - the town the character would like to move to
	 * @param visitedTowns - the set of all visited towns in the network
	 * @throws IllegalArgumentException - if the current town is not in the network
	 * @return true if the path to the destination town is clear, false otherwise
	 */
	private boolean recursiveClearPath(Town currTown, Town destTown, Set<Town> visitedTowns) {
		// Check to see if the current town is the destination
		if (currTown.equals(destTown)) {
			return true;
		}
		
		// Mark the town as visited if this is not the destination
		visitedTowns.add(currTown);
	
		// Check that the current town is in the network
		if (townConnections.get(currTown).equals(null)) {
			throw new IllegalArgumentException("Current town not in town network");
		}
		
		// Iterate over all adjacent towns to the current town
		for (Town adjacentTown : townConnections.get(currTown)) {
			if (townInhabitants.get(adjacentTown).equals(null)) {
				// If the town does not exist, continue
			}
			// If the town is empty, check to see if the rest of the path is clear 
			else if (townInhabitants.get(adjacentTown).isEmpty()) {
				if (recursiveClearPath(adjacentTown, destTown, visitedTowns)) {
					return true;
				}
			}
		}
		return false;		
	}
	
	/**
	 * Find the town for the given character name
	 * @param characterName - the name of the given character
	 * @return the town that the character is currently placed in
	 */
	private Town findInhabitant(String characterName) {
		for (Map.Entry<Town, Set<Character>> entry : townInhabitants.entrySet()) {
			for (Character currCharacter : entry.getValue()) {
				if (currCharacter.checkCharacterName(characterName)) {
					return entry.getKey();
				}
			}
		}
		return null;
	}
	
	/**
	 * Find the inhabitants located at a town 
	 * @param townName - the name of the given town
	 * @return the town and set of characters associated with the town name
	 */
	private Map.Entry<Town, Set<Character>> getInhabitants(String townName) {
		for (Map.Entry<Town, Set<Character>> entry : townInhabitants.entrySet()) {
			if (entry.getKey().checkTownName(townName)); {
				return entry;
			}
		}
		return null;
	}
	
	/**
	 * Find the town object for the associated town name
	 * @param townName - the name of the given town
	 * @return the town and set of adjacent towns associated with the given town name
	 */
	private Map.Entry<Town, Set<Town>> getTown(String townName) {
		for (Map.Entry<Town, Set<Town>> entry : townConnections.entrySet()) {
			if (entry.getKey().checkTownName(townName)); {
				return entry;
			}
		}
		return null;
	}
	
	@Override
	public boolean checkSameNetwork(Map<Town, Set<Town>> townConnections, Map<Town, Set<Character>> townInhabitants) {
		return townConnections.equals(this.townConnections) && townInhabitants.equals(this.townInhabitants);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Network)) {
			return false;
		}
		
		Network otherNetwork = (Network) obj;
		return otherNetwork.checkSameNetwork(this.townConnections, this.townInhabitants);	
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.townConnections, this.townInhabitants);
	}
}
