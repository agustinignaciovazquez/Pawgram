package ar.edu.itba.pawgram.model;

public enum Pet {
	DOG ("dog"),
	CAT ("cat"),
	OTHER ("other");

	
	private final String name;
	
	private Pet(String name) {
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
