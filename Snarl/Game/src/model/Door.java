package model;

public class Door implements Entity {
	public LevelComponent connectedComponent;
	
	Door(LevelComponent connectedComponent) {
		this.connectedComponent = connectedComponent;
	}

}
