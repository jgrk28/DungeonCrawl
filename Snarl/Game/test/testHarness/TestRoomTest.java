package testHarness;

import Level.TestRoom;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.apache.commons.io.FileUtils;

// Tests that the methods for TestRoom work as expected
public class TestRoomTest {
	
	//Check that an exception is thrown when invalid input is provided
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidJSONFormat() {
		String input = "12";
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		TestRoom.main(new String[] {});
	}
	
	//Test that the first test file can be read in correctly, and that
	//the corresponding result populates the output file
	@Test
	public void testInputFileOne() throws IOException {
		System.setIn(new FileInputStream("tests/Level/1-in.json"));
		PrintStream fileOut = new PrintStream("tests/Level/1-out.json");
		System.setOut(fileOut);
		
		TestRoom.main(new String[] {});
		
		String expectedOutput = "[\"Success: Traversable points "
				+ "from \",[1,3],\" in room at \",[0,1],\" are \","
				+ "[[0,3],[1,2],[1,4],[2,3]]]";
		
		List<String> fileContent = FileUtils.readLines(new File("tests/Level/1-out.json"), Charset.defaultCharset());
		assertEquals(expectedOutput, fileContent.get(0));	
	}
	
	//Test that the second test file can be read in correctly, and that
	//the corresponding result populates the output file
	@Test
	public void testInputFileTwo() throws IOException  {
		System.setIn(new FileInputStream("tests/Level/2-in.json"));
		PrintStream fileOut = new PrintStream("tests/Level/2-out.json");
		System.setOut(fileOut);
		
		TestRoom.main(new String[] {});
		
		String expectedOutput = "[\"Failure: Point \",[5,4],\" is not in room at \",[0,1]]";
		
		List<String> fileContent = FileUtils.readLines(new File("tests/Level/2-out.json"), Charset.defaultCharset());
		assertEquals(expectedOutput, fileContent.get(0));
	}
	
	//Test that the third test file can be read in correctly, and that
	//the corresponding result populates the output file
	@Test
	public void testInputFileThree() throws IOException  {
		System.setIn(new FileInputStream("tests/Level/3-in.json"));
		PrintStream fileOut = new PrintStream("tests/Level/3-out.json");
		System.setOut(fileOut);
		
		TestRoom.main(new String[] {});
		
		String expectedOutput = "[\"Success: Traversable points from \",[3,1],"
				+ "\" in room at \",[1,1],\" are \",[[3,2],[4,1]]]";
		
		List<String> fileContent = FileUtils.readLines(new File("tests/Level/3-out.json"), Charset.defaultCharset());
		assertEquals(expectedOutput, fileContent.get(0));
	}

	//Test that the fourth test file can be read in correctly, and that
	//the corresponding result populates the output file
	@Test
	public void testInputFileFour() throws IOException  {
		System.setIn(new FileInputStream("tests/Level/4-in.json"));
		PrintStream fileOut = new PrintStream("tests/Level/4-out.json");
		System.setOut(fileOut);

		TestRoom.main(new String[] {});

		String expectedOutput = "[\"Success: Traversable points from \",[2,1],"
				+ "\" in room at \",[0,0],\" are \",[[1,1],[2,2],[3,1]]]";

		List<String> fileContent = FileUtils.readLines(new File("tests/Level/4-out.json"), Charset.defaultCharset());
		assertEquals(expectedOutput, fileContent.get(0));
	}

	//Test that the fifth test file can be read in correctly, and that
	//the corresponding result populates the output file
	@Test
	public void testInputFileFive() throws IOException  {
		System.setIn(new FileInputStream("tests/Level/5-in.json"));
		PrintStream fileOut = new PrintStream("tests/Level/5-out.json");
		System.setOut(fileOut);

		TestRoom.main(new String[] {});

		String expectedOutput = "[\"Success: Traversable points from \",[3,0],"
				+ "\" in room at \",[0,0],\" are \",[]]";

		List<String> fileContent = FileUtils.readLines(new File("tests/Level/5-out.json"), Charset.defaultCharset());
		assertEquals(expectedOutput, fileContent.get(0));
	}
	
	//Tests that the traversable points are returned when moving from the center of the room
	@Test
	public void testPointCenterRoom() {
		String input = "[ { \"type\" : \"room\",\n"
				+ "    \"origin\" : [0, 1],\n"
				+ "    \"bounds\" : { \"rows\" : 3,\n"
				+ "                 \"columns\" : 5 },\n"
				+ "    \"layout\" : [ [0, 0, 2, 0, 0],\n"
				+ "                 [0, 1, 1, 1, 0],\n"
				+ "                 [0, 0, 2, 0, 0]\n"
				+ "               ]\n"
				+ "  },\n"
				+ "  [1, 3]\n"
				+ "]\n";
		
		String expectedOutput = "[\"Success: Traversable points "
				+ "from \",[1,3],\" in room at \",[0,1],\" are \","
				+ "[[0,3],[1,2],[1,4],[2,3]]]";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestRoom.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	//Tests that a point outside of the room returns the corresponding failure
	@Test
	public void testPointOutsideRoom() {
		String input = "[ { \"type\" : \"room\",\n"
				+ "    \"origin\" : [0, 1],\n"
				+ "    \"bounds\" : { \"rows\" : 3,\n"
				+ "                 \"columns\" : 5 },\n"
				+ "    \"layout\" : [ [0, 0, 2, 0, 0],\n"
				+ "                 [0, 1, 1, 1, 0],\n"
				+ "                 [0, 0, 2, 0, 0]\n"
				+ "               ]\n"
				+ "  },\n"
				+ "  [5, 4]\n"
				+ "]\n";
		
		String expectedOutput = "[\"Failure: Point \",[5,4],\" is not in room at \",[0,1]]";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestRoom.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	//Tests that a wall on the edge of the room returns traversable spaces 
	//within the room
	@Test
	public void testPointBorderWall() {
		String input = "[ { \"type\" : \"room\",\n"
				+ "    \"origin\" : [1, 1],\n"
				+ "    \"bounds\" : { \"rows\" : 7,\n"
				+ "                 \"columns\" : 4 },\n"
				+ "    \"layout\" : [ [0, 0, 0, 0],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [2, 1, 1, 2],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [0, 0, 0, 0],\n"
				+ "               ]\n"
				+ "  },\n"
				+ "  [3, 1]\n"
				+ "]\n";
		
		String expectedOutput = "[\"Success: Traversable points from \",[3,1],"
				+ "\" in room at \",[1,1],\" are \",[[3,2],[4,1]]]";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestRoom.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	//Tests that a point on the edge of the room does not include adjacent walls 
	//as traversable points
	@Test
	public void testPointEdgeRoom() {
		String input = "[ { \"type\" : \"room\",\n"
				+ "    \"origin\" : [0, 0],\n"
				+ "    \"bounds\" : { \"rows\" : 7,\n"
				+ "                 \"columns\" : 4 },\n"
				+ "    \"layout\" : [ [0, 0, 0, 0],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [2, 1, 1, 2],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [0, 0, 0, 0],\n"
				+ "               ]\n"
				+ "  },\n"
				+ "  [2, 1]\n"
				+ "]\n";
		
		String expectedOutput = "[\"Success: Traversable points from \",[2,1],"
				+ "\" in room at \",[0,0],\" are \",[[1,1],[2,2],[3,1]]]";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestRoom.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}


	//Tests a corner point with no adjacent traversable spaces
	@Test
	public void testCornerPoint() {
		String input = "[ { \"type\" : \"room\",\n"
				+ "    \"origin\" : [0, 0],\n"
				+ "    \"bounds\" : { \"rows\" : 4,\n"
				+ "                 \"columns\" : 4 },\n"
				+ "    \"layout\" : [ [0, 0, 0, 0],\n"
				+ "                 [2, 1, 1, 2],\n"
				+ "                 [0, 1, 1, 0],\n"
				+ "                 [0, 0, 0, 0],\n"
				+ "               ]\n"
				+ "  },\n"
				+ "  [3, 0]\n"
				+ "]\n";
		
		String expectedOutput = "[\"Success: Traversable points from \",[3,0],"
				+ "\" in room at \",[0,0],\" are \",[]]";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		//Assign output of STDOUT to new Stream
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		TestRoom.main(new String[] {});
		
		assertEquals(expectedOutput, output.toString());
	}

}
