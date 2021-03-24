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
 * The PlayerClient runs on the user's side (locally or through TCP connection)
 * This displays relevant information to the user, and sends moves and other 
 * commands that the user would like to perform in the game to the GameManager 
 */
public class LocalPlayer implements Player {

	/**
	 * Prompts the player for a move and sends it to the GameManager
     * @return the point that the player would like to move to
     */
	@Override
    public Point takeTurn(List<Point> validMoves) {
		System.out.println("Please enter a valid move from the below list:");
		System.out.println(pointsToString(validMoves));
		
		Scanner in = new Scanner(System.in);
		System.out.println("Enter x position");
		int x = in.nextInt();
		System.out.println("Enter y position");
		int y = in.nextInt();
		in.close();
		return new Point(x,y);
    }
	
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
	//state of their immediate surroundings in string form. This allows us to 
	//reduce the overhead of sending and parsing large JSONs
    @Override
    public void update(PlayerModelView gameState) {    	
 	    //ByteArrayOutputStream output = new ByteArrayOutputStream();
 	    //PrintStream printStream = new PrintStream(output);
 	    //TextualPlayerView playerView = new TextualPlayerView(gameState, printStream);
			TextualPlayerView playerView = new TextualPlayerView(gameState, System.out);
 	    playerView.draw();
    }

	@Override
	public void displayMessage(String message) {
		System.out.println(message);
	}
}