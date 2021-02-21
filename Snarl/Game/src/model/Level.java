package model;

import java.awt.Point;

import modelView.LevelModelView;

/**
 * Level extends the LevelModelView interface to provide
 * the view with the necessary information to render a
 * Level
 */
public interface Level extends LevelModelView {
	
	void playerAction(Player player, Point destination);
	
	void adversaryAction(Adversary adversary, Point destination);
	
	GameState isLevelOver();

}
