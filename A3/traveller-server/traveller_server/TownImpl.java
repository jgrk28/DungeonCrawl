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

	/**
	 * Returns true if the given Town is the same as this Town
	 * @param name - the name of the given town
	 * @return true if the town name is the same, false otherwise
	 */
	private Boolean checkTownName(String name) {
		return name.equals(this.name);
	}
	
	/**
	 * Overrides the equals method
	 * @param obj - the given object
	 * @return true if the towns are the same town, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Town)) {
			return false;
		}
		TownImpl otherTown = (TownImpl) obj;
		return otherTown.checkTownName(this.name);	
	}
	
	/**
	 * Overrides the hashcode method
	 * @return the corresponding hashcode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
}
