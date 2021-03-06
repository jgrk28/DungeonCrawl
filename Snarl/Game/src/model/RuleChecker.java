package model;

import java.awt.Point;

public interface RuleChecker {
	
	GameState isGameOver();
	
	GameState isLevelOver();
	
	Boolean checkValidMove(Actor actor, Point destination);
	
	Boolean checkValidGameState();

}
