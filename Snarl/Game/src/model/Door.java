package model;

public class Door implements Entity {
	public LevelComponent room;
	public LevelComponent hall;
	
	
	public Door(LevelComponent room, LevelComponent hall) {
		this.room = room;
		this.hall = hall;
	}

}
