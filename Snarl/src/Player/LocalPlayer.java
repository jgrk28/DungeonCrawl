package Player;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import Common.Player;
import Game.modelView.PlayerModelView;
import Game.view.TextualPlayerView;

/**
 * The LocalPlayer represents a local connection to the user in a game of Snarl.
 * This displays relevant information to the user, and sends moves and other 
 * commands that the user would like to perform in the game to the GameManager 
 */
public class LocalPlayer implements Player {

	private Point currentLocation;

	@Override
    public Point takeTurn(List<Point> validMoves) {
		//Prompt the user to take a turn
		System.out.println("Please enter a valid move from the below list:");
		System.out.println(pointsToString(validMoves));

		try {
			Scanner in = new Scanner(System.in);
			System.out.println("Enter x position");
			int x = in.nextInt();
			System.out.println("Enter y position");
			int y = in.nextInt();
			in.close();
			//Create a new point based on the input
			return new Point(x,y);
		} catch (java.util.InputMismatchException e) {
			//If the move was not an integer throw error out
			throw new java.util.InputMismatchException();
		} catch (java.util.NoSuchElementException e) {
			//If the move was empty return the players current position
			return this.currentLocation;
		}
	}
	
	/**
	 * Converts all valid moves for the player from a list of 
	 * points to a string representation that can be displayed
	 * @param points - the points to convert
	 * @return a string that represents all of the points
	 */
	private String pointsToString(List<Point> points) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			output.append("[" + point.x + "," + point.y + "]");
			if (i != points.size() - 1) {
				output.append(", ");
			}
		}
		
		return output.toString();
	}

	//This can receive an update containing the avatar's position and the current
	//state of their immediate surroundings
    @Override
    public void update(PlayerModelView gameState) {
			this.currentLocation = gameState.getPosition();
		TextualPlayerView playerView = new TextualPlayerView(gameState, System.out);
 	    playerView.draw();
    }

	@Override
	public void displayMessage(String message) {
		System.out.println(message);
	}
}