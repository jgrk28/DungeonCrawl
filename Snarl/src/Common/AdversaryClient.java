package Common;

import Game.model.Actor;
import java.awt.Point;
import java.util.Map;

import Game.model.Adversary;
import Game.model.Level;

/**
 * Represents an AdversaryClient within a game of Snarl. 
 * AdversaryClients make moves automatically based on the
 * strategies relevant to the adversary type (Zombie or Ghost)
 */
public interface AdversaryClient {
	
	/**
	 * Gets the full level information at the beginning of the level,
	 * comprised of rooms, hallways and objects
	 * @param startLevel
	 */
	void getLevelStart(Level startLevel);
	
	/**
	 * Updates the adversary on all actor locations, and provides the
	 * adversary avatar that corresponds to this AdversaryClient 
	 * @param playerLocations - all active player locations for the 
	 * current level
	 * @param adversaryLocations - all active adversary locations for the 
	 * current level
	 * @param adversaryAvatar - the adversary avatar that corresponds to 
	 * this AdversaryClient 
	 */ 
	void updateActorLocations(Map<Game.model.Player, Point> playerLocations,
			Map<Adversary, Point> adversaryLocations,
			Adversary adversaryAvatar); 
	
	/**
	 * Prompts the adversary for a move and sends it to the GameManager  
	 * @return the point that the adversary is attempting to move to 
	 */
	Point takeTurn();  

}
