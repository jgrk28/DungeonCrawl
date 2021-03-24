package Game.view;

import Game.model.GameState;
import Game.modelView.DungeonModelView;
import Game.modelView.LevelModelView;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Represents a textual level view of the dungeon, including the
 * current level, game status, and level status
 */
public class TextualDungeonView extends AbstractTextualView {
  //The read-only version of the Dungeon model
  private DungeonModelView modelView;

  public TextualDungeonView(DungeonModelView modelView, PrintStream output) {
    super(output);
    this.modelView = modelView;
  }

  @Override
  public void draw() {
	//Get the current level, the game status, and the level status
    int currLevelIndex = this.modelView.getCurrentLevelIndex();
    GameState gameStatus = this.modelView.isGameOver();
    GameState levelStatus = this.modelView.isLevelOver();

    StringBuilder toOutput = new StringBuilder();
    switch (gameStatus) {
      case WON:
        toOutput.append("The game is Won!\n");
        break;
      case LOST:
        toOutput.append("The game is Lost.\n");
        break;
      case ACTIVE:
        if (levelStatus.equals(GameState.WON)) {
          toOutput.append("The level is Won! Continuing to next level.\n");
        } else {
          toOutput.append("Active level: " + currLevelIndex + "\n");
        }
        break;
      default:
        throw new IllegalArgumentException("Unknown game state");
    }

    ByteArrayOutputStream drawnLevel = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(drawnLevel);
    LevelModelView levelModelView = this.modelView.getCurrentLevelModelView();
    TextualLevelView levelView = new TextualLevelView(levelModelView, printStream);
    levelView.draw();

    toOutput.append(drawnLevel.toString() + "\n");
    this.output.println(toOutput.toString());
  }
}
