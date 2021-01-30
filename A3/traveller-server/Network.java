import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Network implements TownNetwork{
	
	// A map of towns to the towns they are connected to
	private Map<Town, Set<Town>> townConnections;
	
	// A map of towns to characters placed in them
	private Map<Town, Set<CharacterImpl>> townInhabitants;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasClearPath(String characterName, String townName) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}
	
	//
	private Map.Entry<Town, Set<Town>> getTown(String townName) {
		for (Map.Entry<Town, Set<Town>> entry : townConnections.entrySet()) {
			if (entry.getKey().checkTownName(townName)); {
				return entry;
			}
		}
		return null;
	}

}
