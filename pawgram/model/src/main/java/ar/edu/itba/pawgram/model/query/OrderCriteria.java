package ar.edu.itba.pawgram.model.query;

public enum OrderCriteria {
    ASC,
    DESC;

    public static OrderCriteria fromString(final String str) {
        return valueOf(str.toUpperCase());
    }
}
