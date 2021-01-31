package traveller_server;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Network implements TownNetwork{
	
	// A map of towns to the towns they are connected to
	private Map<Town, Set<Town>> townConnections;
	
	// A map of towns to characters placed in them
	private Map<Town, Set<Character>> townInhabitants;

	@Override
	public void createTown(String townName) throws IllegalArgumentException {
		Town newTown = new TownImpl(townName);
		Set<Town> emptySet = new HashSet<Town>();
		townConnections.put(newTown, emptySet);
	}

	@Override
	public void connectTowns(String townNameA, String townNameB) throws IllegalArgumentException {
		Map.Entry<Town, Set<Town>> townA = getTown(townNameA);
		Map.Entry<Town, Set<Town>> townB = getTown(townNameB);
		
		if (townA.equals(null) || townB.equals(null)) {
			throw new IllegalArgumentException("Not a valid town name");
		} else if (townA.getValue().contains(townB.getKey()) || townB.getValue().contains(townA.getKey())) {
			throw new IllegalArgumentException("Towns are already connected");
		} else if (townA.getValue().equals(townB.getValue())) {
			throw new IllegalArgumentException("Cannot connect town to itself");
		} 
		
		Set<Town> newASet = townA.getValue();
		newASet.add(townB.getKey());
		Set<Town> newBSet = townB.getValue();
		newBSet.add(townA.getKey());
		
		townConnections.replace(townA.getKey(), newASet);
		townConnections.replace(townB.getKey(), newBSet);
	}

	@Override
	public void placeCharacter(String characterName, String townName) throws IllegalArgumentException {
		Character newCharacter = new CharacterImpl(characterName);
		Map.Entry<Town, Set<Character>> townCharacters = getInhabitants(townName);
		if (townCharacters.equals(null)) {
			throw new IllegalArgumentException("No town found");
		}
		
		Set<Character> inhabitantSet = townCharacters.getValue();
		
		inhabitantSet.add(newCharacter);
		townInhabitants.replace(townCharacters.getKey(), inhabitantSet);	
	}

	@Override
	public boolean hasClearPath(String characterName, String townName) throws IllegalArgumentException {
		Set<Town> visitedTowns = new HashSet<Town>();
		Town currTown = findInhabitant(characterName);
		Map.Entry<Town, Set<Town>> destTown = getTown(townName);
		
		if (currTown.equals(null)) {
			throw new IllegalArgumentException("Could not find character");
		}
		if (destTown.equals(null)) {
			throw new IllegalArgumentException("Could not find town");
		}
		
		return recursiveClearPath(currTown, destTown.getKey(), visitedTowns);
	}
	
	private boolean recursiveClearPath(Town currTown, Town destTown, Set<Town> visitedTowns) {
		if (currTown.equals(destTown)) {
			return true;
		}
		visitedTowns.add(currTown);
	
		if (townConnections.get(currTown).equals(null)) {
			throw new IllegalArgumentException("Current town not in town network");
		}
		for (Town adjacentTown : townConnections.get(currTown)) {
			if (townInhabitants.get(adjacentTown).equals(null)) {
				//if the town does not exist, continue
			}
			else if (townInhabitants.get(adjacentTown).isEmpty()) {
				if (recursiveClearPath(adjacentTown, destTown, visitedTowns)) {
					return true;
				}
			}
		}
		return false;		
	}
	
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
	
	private Map.Entry<Town, Set<Character>> getInhabitants(String townName) {
		for (Map.Entry<Town, Set<Character>> entry : townInhabitants.entrySet()) {
			if (entry.getKey().checkTownName(townName)); {
				return entry;
			}
		}
		return null;
	}
	
	private Map.Entry<Town, Set<Town>> getTown(String townName) {
		for (Map.Entry<Town, Set<Town>> entry : townConnections.entrySet()) {
			if (entry.getKey().checkTownName(townName)); {
				return entry;
			}
		}
		return null;
	}
	
	@Override
	public Boolean checkSameNetwork(Map<Town, Set<Town>> townConnections, Map<Town, Set<Character>> townInhabitants) {
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
