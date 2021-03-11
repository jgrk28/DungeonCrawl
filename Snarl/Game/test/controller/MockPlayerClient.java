package controller;

import java.awt.Point;

import clients.PlayerClient;

public class MockPlayerClient extends PlayerClient {
	
	int turnCount;
	
	public MockPlayerClient() {
		this.turnCount = 0;
	}
	
	/**
	 * Prompts the player for a move and sends it to the GameManager
	 * @return the point that the player would like to move to
	 */
	@Override
	public Point takeTurn() {
	   turnCount++;
	   return new Point(turnCount, 2);  
	}
	 
	/**
	 * Displays the game state relevant to the user. This includes what the 
	 * player can see in the level, the current level, and whether or not
	 * they are still active in the level
	 * @param gameState - the string containing the relevant game state
	 */
	@Override
	public void displayGameState(String gameState) {
	  System.out.print(gameState);
	}

}
