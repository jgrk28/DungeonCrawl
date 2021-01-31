package traveller_server;

import java.util.Objects;

/**
 * This class represents a character
 * Overrides equals and hashcode
 */
public class TownImpl implements Town {
	String name;
	
	/**
	 * Constructs a new town
	 * @param name - the name of the town
	 */
	public TownImpl(String name) {
		this.name = name;
	}

	@Override
	public Boolean checkTownName(String name) {
		return name.equals(this.name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Town)) {
			return false;
		}
		Town otherTown = (Town) obj;
		return otherTown.checkTownName(this.name);	
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
