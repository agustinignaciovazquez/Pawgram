package ar.edu.itba.pawgram.model;

public enum Category {
	LOST, FOUND, ADOPT, EMERGENCY;

	public static Category fromString(final String str) {
		return valueOf(str.toUpperCase());
	}

	public String getLowerName() {
		return name().toLowerCase();
	}

	public String getName() {
		return this.name();
	}
}
