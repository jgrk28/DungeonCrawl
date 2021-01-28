package test;

import src.NumJSONParser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONTokener;

public class NumJSONParserTest {
	
	@Test
	public void testIsNum() {		
	    assertTrue(NumJSONParser.isNum("0"));
	    assertTrue(NumJSONParser.isNum("-1"));
	    assertTrue(NumJSONParser.isNum("12"));	
	    assertFalse(NumJSONParser.isNum("foo"));
	}
	
	@Test
	public void testRecursiveOpNumber() {
		assertEquals(12, NumJSONParser.recursiveOp(12, "sum"));
		assertEquals(12, NumJSONParser.recursiveOp(12, "product"));
	}
	
	@Test
	public void testRecursiveOpString() {
		assertEquals(0, NumJSONParser.recursiveOp("foo", "sum"));
		assertEquals(1, NumJSONParser.recursiveOp("foo", "product"));
	}
	
	@Test
	public void testRecursiveOpArray() {
		JSONArray input = new JSONArray();
		input.put(2);
		input.put("foo");
		input.put(4);
		
		assertEquals(6, NumJSONParser.recursiveOp(input, "sum"));
		assertEquals(8, NumJSONParser.recursiveOp(input, "product"));
	}
	
	@Test
	public void testRecursiveOpNumberArray() {
		JSONArray input = new JSONArray();
		input.put(2);
		input.put(20);
		input.put(4);
		
		assertEquals(26, NumJSONParser.recursiveOp(input, "sum"));
		assertEquals(160, NumJSONParser.recursiveOp(input, "product"));
	}
	
	@Test
	public void testRecursiveOpStringArray() {
		JSONArray input = new JSONArray();
		input.put("foo");
		input.put("bar");
		input.put("baz");
		
		assertEquals(0, NumJSONParser.recursiveOp(input, "sum"));
		assertEquals(1, NumJSONParser.recursiveOp(input, "product"));
	}
	
	@Test
	public void testRecursiveOpObject() {
		String objString = "{ \"name\" : \"SwDev\", \"payload\" : \n"
				+ "  [12, 33]     , \n"
				+ "        \"other\" : { \"payload\" : [ 4, 7 ] } }";
		
		JSONTokener inputTokens = new JSONTokener(objString);
		Object value = inputTokens.nextValue();
		
		assertEquals(45, NumJSONParser.recursiveOp(value, "sum"));
		assertEquals(396, NumJSONParser.recursiveOp(value, "product"));
	}
	
	@Test
	public void testInvalidOperationException() {
		try {
			NumJSONParser.recursiveOp(6, "subtract");
		} catch (IllegalArgumentException i) {
			assertEquals("Operation must be sum or product", i.getMessage());
		}
	}

	@Test
	//Source for setting System.in and System.out
	//http://one-line-it.blogspot.com/2013/05/java-testing-with-stdin-and-stdout.html
	public void testMainSingleNumberProduct() {
		String input = "12";
		
		String expectedOutput = "[{\"total\":12,\"object\":12}]\n";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		NumJSONParser.main(new String[] {"--product"});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	@Test
	//Source for setting System.in and System.out
	//http://one-line-it.blogspot.com/2013/05/java-testing-with-stdin-and-stdout.html
	public void testMainSingleNumberSum() {
		String input = "12";
		
		String expectedOutput = "[{\"total\":12,\"object\":12}]\n";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		NumJSONParser.main(new String[] {"--product"});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	
	@Test
	//Source for setting System.in and System.out
	//http://one-line-it.blogspot.com/2013/05/java-testing-with-stdin-and-stdout.html
	public void testMainComplexNumJSONProduct() {
		String input = "12        [2, \"foo\", \n"
				+ "4]  { \"name\" : \"SwDev\", \"payload\" : \n"
				+ "  [12, 33]     , \n"
				+ "        \"other\" : { \"payload\" : [ 4, 7 ] } }";
		
		String expectedOutput = "[{\"total\":12,\"object\":12},"
				+ "{\"total\":8,\"object\":[2,\"foo\",4]},"
				+ "{\"total\":396,\"object\":{\"other\":"
				+ "{\"payload\":[4,7]},\"payload\":[12,33],\"name\":\"SwDev\"}}]\n";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		NumJSONParser.main(new String[] {"--product"});
		
		assertEquals(expectedOutput, output.toString());
	}
	
	@Test
	//Source for setting System.in and System.out
	//http://one-line-it.blogspot.com/2013/05/java-testing-with-stdin-and-stdout.html
	public void testMainComplexNumJSONSum() {
		String input = "12        [2, \"foo\", \n"
				+ "4]  { \"name\" : \"SwDev\", \"payload\" : \n"
				+ "  [12, 33]     , \n"
				+ "        \"other\" : { \"payload\" : [ 4, 7 ] } }";
		
		String expectedOutput = "[{\"total\":12,\"object\":12},"
				+ "{\"total\":6,\"object\":[2,\"foo\",4]},"
				+ "{\"total\":45,\"object\":{\"other\":{\"payload\":[4,7]},"
				+ "\"payload\":[12,33],\"name\":\"SwDev\"}}]\n";
		
		System.setIn(new ByteArrayInputStream(input.getBytes()));
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream print = new PrintStream(output);
		System.setOut(print);
		
		NumJSONParser.main(new String[] {"--sum"});
		
		assertEquals(expectedOutput, output.toString());
	}
	
}
