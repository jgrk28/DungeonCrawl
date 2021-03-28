package Common;

import java.awt.Point;
import java.util.Map;

import Game.model.Adversary;
import Game.model.Level;
import Game.model.Player;

/**
 * TODO Add comments
 */
public interface AdversaryClient {
	
	//Gets the full level information at the beginning of the level  
	void getLevelStart(Level startLevel);
	
	//Update the adversary on all actor locations  
	//This will be called at the beginning of the adversary’s turn 
	void updateActorLocations(Map<Player, Point> playerLocations, 
			Map<Adversary, Point> adversaryLocations,
			Adversary adversaryAvatar); 
	
	//Prompts the adversary for a move and sends it to the GameManager  
	//Returns the point that the adversary is attempting to move to 
	Point takeTurn();  

}
