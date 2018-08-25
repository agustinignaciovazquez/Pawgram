package ar.edu.itba.pawgram.model;

public enum Pet {
	DOG, CAT, OTHER;


	public static Pet fromString(final String str) {
		return valueOf(str.toUpperCase());
	}

	public String getLowerName() {
		return name().toLowerCase();
	}
}
