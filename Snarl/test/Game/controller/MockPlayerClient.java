package Game.controller;

import Game.modelView.PlayerModelView;
import Game.view.TextualPlayerView;
import java.awt.Point;

import Player.LocalPlayer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class MockPlayerClient extends LocalPlayer {
	
	int turnCount;
	
	public MockPlayerClient() {
		this.turnCount = 0;
	}
	
	/**
	 * Prompts the player for a move and sends it to the GameManager
	 * @return the point that the player would like to move to
	 */
	@Override
	public Point takeTurn(List<Point> validMoves) {
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
	public void displayMessage(String gameState) {
	  System.out.print(gameState);
	}

	@Override
	public void update(PlayerModelView gameState) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(output);
		TextualPlayerView playerView = new TextualPlayerView(gameState, printStream);
		playerView.draw();
	}

}
