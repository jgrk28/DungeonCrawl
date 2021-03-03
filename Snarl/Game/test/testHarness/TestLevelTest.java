package testHarness;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

import Level.TestLevel;

//Tests that the methods for TestLevel work as expected
public class TestLevelTest {
	
	String level = "{ \"type\": \"level\",\n"
			+ "  \"rooms\": [ { \"type\": \"room\",\n"
			+ "               \"origin\": [ 3, 1 ],\n"
			+ "               \"bounds\": { \"rows\": 4, \"columns\": 4 },\n"
			+ "               \"layout\": [ [ 0, 0, 2, 0 ],\n"
			+ "                           [ 0, 1, 1, 0 ],\n"
			+ "                           [ 0, 1, 1, 0 ],\n"
			+ "                           [ 0, 2, 0, 0 ] ] },\n"
			+ "             { \"type\": \"room\",\n"
			+ "               \"origin\": [ 10, 5 ],\n"
			+ "               \"bounds\": { \"rows\": 5, \"columns\": 5 },\n"
			+ "               \"layout\": [ [ 0, 0, 0, 0, 0 ],\n"
			+ "                           [ 0, 1, 1, 1, 0 ],\n"
			+ "                           [ 2, 1, 1, 1, 0 ],\n"
			+ "                           [ 0, 1, 1, 1, 0 ],\n"
			+ "                           [ 0, 0, 0, 0, 0 ] ] },\n"
			+ "             { \"type\": \"room\",\n"
			+ "               \"origin\": [ 4, 14 ],\n"
			+ "               \"bounds\": { \"rows\": 5, \"columns\": 5 },\n"
			+ "               \"layout\": [ [ 0, 0, 2, 0, 0 ],\n"
			+ "                           [ 0, 1, 1, 1, 0 ],\n"
			+ "                           [ 0, 1, 1, 1, 0 ],\n"
			+ "                           [ 0, 1, 1, 1, 0 ],\n"
			+ "                           [ 0, 0, 0, 0, 0 ] ] } ],\n"
			+ "  \"objects\": [ { \"type\": \"key\", \"position\": [ 4, 2 ] },\n"
			+ "               { \"type\": \"exit\", \"position\": [ 7, 17 ] } ],\n"
			+ "  \"hallways\": [ { \"type\": \"hallway\",\n"
			+ "                  \"from\": [ 3, 3 ],\n"
			+ "                  \"to\": [ 4, 16 ],\n"
			+ "                  \"waypoints\": [ [ 1, 3 ], [ 1, 16 ] ] },\n"
			+ "                { \"type\": \"hallway\",\n"
			+ "                  \"from\": [ 6, 2 ],\n"
			+ "                  \"to\": [ 12, 5 ],\n"
			+ "                  \"waypoints\": [ [ 12, 2 ] ] } ]\n"
			+ "}";
	
	//Check that an exception is thrown when invalid input is provided
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidJSONFormat() {
		String input = "12";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		TestLevel.main(new String[] {});
	}
	
	//Tests a point that is not located in a room or hall
	@Test
	public void testUnreachablePoint() {
		String input = "[" + level + ", [6, 10] ]";
		
		String expectedOutput = "{\"traversable\":false,\"type\":\"void\","
				+ "\"reachable\":[],\"object\":null}";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestLevel.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	//Tests a point where the exit is located in a room
	@Test
	public void testExitObject() {
		String input = "[" + level + ", [7, 17] ]";
		
		String expectedOutput = "{\"traversable\":true,\"type\":\"room\","
				+ "\"reachable\":[[3,1]],\"object\":\"exit\"}";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestLevel.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	//Tests a point located in a hallway with two reachable rooms
	@Test
	public void testHallwayPoint() {
		String input = "[" + level + ", [12, 4] ]";
		
		String expectedOutput = "{\"traversable\":true,\"type\":\"hallway\","
				+ "\"reachable\":[[3,1],[10,5]],\"object\":null}";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestLevel.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	//Tests a point in a room that is not traversable
	@Test
	public void testNonTraversableRoomTile() {
		String input = "[" + level + ", [3, 1] ]";
		
		String expectedOutput = "{\"traversable\":false,\"type\":\"room\","
				+ "\"reachable\":[[4,14],[10,5]],\"object\":null}";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestLevel.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}

	
	//Tests a point where the key is located in a room
	@Test
	public void testKeyObject() {
		String input = "[" + level + ", [4, 2] ]";
		
		String expectedOutput = "{\"traversable\":true,\"type\":\"room\","
				+ "\"reachable\":[[4,14],[10,5]],\"object\":\"key\"}";

		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestLevel.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}


}
