package view;

import java.io.PrintStream;
import java.util.List;

import modelView.EntityType;
import modelView.PlayerModelView;

/**
 * Represents an ACSII art view for a player within
 * a level
 * 
 * An textual view for a player may look like this
 * 
 * X.GX
 * X..X
 * X.PX
 * ..@.*
 * XXXX 
 *
 * Where each Entity corresponds to the following:
 * - Wall (X)
 * - Space (.)
 * - Hall Space (*)
 * - Key (!)
 * - Exit (@)
 * - Player (P)
 * - Adversary - Ghost (G)
 * - Adversary - Zombie (Z)
 * - Empty - where no entities have been placed  (" ")
 */
public class TextualPlayerView extends AbstractTextualView {
	//The read-only version of the player model
	protected PlayerModelView playerModelView;
	
	public TextualPlayerView(PlayerModelView playerModelView, PrintStream output) {
		super(output);
		this.playerModelView = playerModelView;
	}

	@Override
	public void draw() {
		int currLevelIndex = playerModelView.getCurrentLevel();
		Boolean isAlive = playerModelView.isPlayerAlive();
		StringBuilder output = new StringBuilder();
		output.append("You are currently on level: " + currLevelIndex + "\n");
		if (isAlive) {
			output.append("You are active in the level\n");
		} else {
			output.append("You are no longer active in the level\n");
		}
		this.output.print(output.toString());

		List<List<EntityType>> level = playerModelView.getMap();
		drawLevelMap(level);
	}
}
