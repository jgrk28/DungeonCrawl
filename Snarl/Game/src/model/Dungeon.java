package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dungeon {
	
	//All players in the game regardless of status in current level
	private List<Player> players;
	
	//All adversaries in the game regardless of status in current level
	private List<Adversary> adversaries;
	
	//Current number of the level that the players are on. The first level
	//starts at 1
	private int currLevel;
	
	//All levels in the game
	private List<Level> levels;
	
	//Initializes all the levels, sets the players and adversaries for the dungeon
	public Dungeon(List<Player> players, List<Adversary> adversaries, int currLevel, List<Level> levels) {
		if (players.size() < 1 || players.size() > 4) {
			throw new IllegalArgumentException("Invalid number of players");
		}
		this.players = players;
		this.adversaries = adversaries;
		this.currLevel = currLevel;	
		this.levels = levels;
	}
	
	public Level getNextLevel() {
		Level nextLevel = levels.get(currLevel - 1); 
		currLevel++;
		return nextLevel;
	}
	
	public Boolean isLastLevel() {
		return currLevel == levels.size();		
	}
 
}
