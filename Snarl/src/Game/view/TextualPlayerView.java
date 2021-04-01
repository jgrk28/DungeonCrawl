package Game.view;

import Game.model.GameState;
import java.io.PrintStream;
import java.util.List;

import Game.modelView.EntityType;
import Game.modelView.PlayerModelView;

/**
 * Represents an ACSII art view for a player within
 * a level
 * 
 * An textual view for a player may look like this
 * 
 * X.G.X
 * X...X
 * X.P.X
 * ..@..
 * XXXXZ 
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
		GameState gameStatus = playerModelView.isGameOver();
		GameState levelStatus = playerModelView.isLevelOver();

		StringBuilder toOutput = new StringBuilder();
		switch (gameStatus) {
			case WON:
				toOutput.append("Congratulations! You Won!\n");
				break;
			case LOST:
				toOutput.append("Sorry. You lost on level " + playerModelView.getCurrentLevel() + " :(\n");
				break;
			case ACTIVE:
				if (levelStatus.equals(GameState.WON)) {
					toOutput.append("You beat the level! Continuing to next level.\n");
				} else {
					toOutput.append(drawActiveLevel());
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown game state");
		}
		this.output.print(toOutput.toString());
	}

	/**
	 * Draws the active level of the dungeon that the player is a part of
	 * @return a string representing the state of the level that the player
	 * can view
	 */
	private String drawActiveLevel() {
		int currLevelIndex = playerModelView.getCurrentLevel();
		Boolean isAlive = playerModelView.isPlayerAlive();

		StringBuilder toOutput = new StringBuilder();
		toOutput.append("You are currently on level: " + currLevelIndex + "\n");
		if (isAlive) {
			toOutput.append("You are active in the level\n");
			List<List<EntityType>> level = playerModelView.getMap();
			toOutput.append(drawLevelMap(level));
		} else {
			toOutput.append("You are no longer active in the level\n");
		}
		return toOutput.toString();
	}
}
