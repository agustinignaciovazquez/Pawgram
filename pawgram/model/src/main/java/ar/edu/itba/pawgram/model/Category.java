package ar.edu.itba.pawgram.model;

public enum Category {
	LOST ("lost"),
	FOUND ("found"),
	ADOPT ("adopt"),
	EMERGENCY ("emergency");

	
	private final String name;
	
	private Category(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getLowerName() {
		return name;
	}
}
