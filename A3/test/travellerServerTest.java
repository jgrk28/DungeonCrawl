import traveller_server.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class travellerServerTest {

	// Test for CharacterImpl constructor using checkCharacterName
	@Test
	public void testCharacterImplConstructor() {		
		assertTrue(new CharacterImpl("Andy").checkCharacterName("Andy"));
		assertFalse(new CharacterImpl("Allison").checkCharacterName("Andy"));
	}
	
	// Test equals and hashcode override for CharacterImpl
	@Test
	public void testCharacterImplEquals() {	
		CharacterImpl char1 = new CharacterImpl("Andy");
		CharacterImpl char2 = new CharacterImpl("Andy") ;
		CharacterImpl char3 = new CharacterImpl("Allison");
		TownImpl town1 = new TownImpl("Boston");
		
		assertTrue(char1.equals(char2) && char2.equals(char1));
		assertTrue(char1.hashCode() == char2.hashCode());
		
		assertFalse(char1.equals(char3));
		assertFalse(char1.equals(town1));
		assertFalse(char1.hashCode() == char3.hashCode());
	}
	
	// Test for TownImpl constructor using checkTownName
	@Test
	public void testTownImplConstructor() {		
		assertTrue(new TownImpl("Boston").checkTownName("Boston"));
		assertFalse(new TownImpl("Cambridge").checkTownName("Boston"));
	}
	
	// Test equals and hashcode override for TownImpl
	@Test
	public void testTownImplEquals() {	
		TownImpl town1 = new TownImpl("Boston");
		TownImpl town2 = new TownImpl("Boston") ;
		TownImpl town3 = new TownImpl("Cambridge");
		CharacterImpl char1 = new CharacterImpl("Andy");
		
		assertTrue(town1.equals(town2) && town2.equals(town1));
		assertTrue(town1.hashCode() == town2.hashCode());
		
		assertFalse(town1.equals(town3));
		assertFalse(town1.equals(char1));
		assertFalse(town1.hashCode() == town3.hashCode());
	}
	
	// Test createTown for TownNetwork
	@Test 
	public void testCreateTownException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Somerville");
		
		try {
			testNetwork.createTown("Somerville");
		} catch (IllegalArgumentException i) {
			assertEquals("A town with this name already exists", i.getMessage());
		}
	}
	
	// Test connectTowns for TownNetwork
	@Test 
	public void testConnectTownsAlreadyConnectedException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Somerville");
		testNetwork.createTown("Boston");
		testNetwork.connectTowns("Somerville", "Boston");
		
		try {
			testNetwork.connectTowns("Somerville", "Boston");
		} catch (IllegalArgumentException i) {
			assertEquals("Towns are already connected", i.getMessage());
		}
	}
	
	@Test 
	public void testConnectTownsSameTownException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Somerville");
		
		try {
			testNetwork.connectTowns("Somerville", "Somerville");
		} catch (IllegalArgumentException i) {
			assertEquals("Cannot connect town to itself", i.getMessage());
		}
	}
	
	@Test 
	public void testConnectTownsDoesNotExistException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Somerville");
		
		try {
			testNetwork.connectTowns("Somerville", "Boston");
		} catch (IllegalArgumentException i) {
			assertEquals("Town does not exist in the network", i.getMessage());
		}
	}
	
	// Test placeCharacter for TownNetwork
	@Test 
	public void testPlaceCharacterTownDoesNotExistException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		
		try {
			testNetwork.placeCharacter("Andy", "Boston");
		} catch (IllegalArgumentException i) {
			assertEquals("No town found", i.getMessage());
		}
	}
	
	@Test 
	public void testPlaceCharacterDuplicateNameException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		
		testNetwork.placeCharacter("Andy", "Boston");
		
		try {
			testNetwork.placeCharacter("Andy", "Boston");
		} catch (IllegalArgumentException i) {
			assertEquals("This character already exists in the network", i.getMessage());
		}
	}
	
	// Test hasClearPath for TownNetwork
	
	@Test
	public void testHasClearPathTownException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		testNetwork.placeCharacter("Allison", "Boston");
		
		try {
			testNetwork.hasClearPath("Allison", "Cambridge");
		} catch (IllegalArgumentException i) {
			assertEquals("Could not find town", i.getMessage());
		}
	}
	
	@Test
	public void testHasClearPathCharacterException() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		
		try {
			testNetwork.hasClearPath("Allison", "Boston");
		} catch (IllegalArgumentException i) {
			assertEquals("Could not find character", i.getMessage());
		}
	}
	
	@Test
	public void testClearPathSameTown() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		testNetwork.placeCharacter("Allison", "Boston");
		
		assertTrue(testNetwork.hasClearPath("Allison", "Boston"));
	}
	
	@Test
	public void testClearPathTrue() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		testNetwork.createTown("Cambridge");
		testNetwork.connectTowns("Boston", "Cambridge");
		testNetwork.placeCharacter("Allison", "Boston");
		
		assertTrue(testNetwork.hasClearPath("Allison", "Cambridge"));
	}
	
	@Test
	public void testClearPathFalse() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		testNetwork.createTown("Somerville");
		testNetwork.placeCharacter("Allison", "Boston");
		testNetwork.placeCharacter("Andy", "Somerville");
		
		assertFalse(testNetwork.hasClearPath("Allison", "Somerville"));
	}
	
	@Test
	public void testClearPathTrueLargeGraph() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		testNetwork.createTown("Cambridge");
		testNetwork.createTown("Somerville");
		testNetwork.createTown("Brookline");
		testNetwork.placeCharacter("Allison", "Boston");
		testNetwork.connectTowns("Boston", "Cambridge");
		testNetwork.connectTowns("Cambridge", "Somerville");
		testNetwork.connectTowns("Somerville", "Brookline");
		
		assertTrue(testNetwork.hasClearPath("Allison", "Brookline"));
	}
	
	@Test
	public void testClearPathFalseLargeGraph() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		testNetwork.createTown("Cambridge");
		testNetwork.createTown("Somerville");
		testNetwork.createTown("Brookline");
		testNetwork.placeCharacter("Allison", "Boston");
		testNetwork.placeCharacter("Andy", "Somerville");
		testNetwork.connectTowns("Boston", "Cambridge");
		testNetwork.connectTowns("Cambridge", "Somerville");
		testNetwork.connectTowns("Somerville", "Brookline");
		
		assertFalse(testNetwork.hasClearPath("Allison", "Brookline"));
	}
	
	@Test
	public void testClearPathTrueLargeCircle() {
		Network testNetwork = new Network();
		testNetwork.createTown("Boston");
		testNetwork.createTown("Cambridge");
		testNetwork.createTown("Somerville");
		testNetwork.createTown("Brookline");
		testNetwork.placeCharacter("Allison", "Boston");
		testNetwork.placeCharacter("Andy", "Somerville");
		testNetwork.connectTowns("Boston", "Cambridge");
		testNetwork.connectTowns("Cambridge", "Somerville");
		testNetwork.connectTowns("Somerville", "Brookline");
		testNetwork.connectTowns("Boston", "Brookline");
		
		assertTrue(testNetwork.hasClearPath("Allison", "Brookline"));
	}


}
