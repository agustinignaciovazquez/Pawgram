package ar.edu.itba.pawgram.model.validate;

public class PrimitiveValidation {

    private PrimitiveValidation() {
    }

    public static byte[] notEmptyByteArray(byte[] byteArray, String nullMsg, String emptyMsg) {
        if (byteArray == null)
            throw new NullPointerException(nullMsg);
        if (byteArray.length == 0)
            throw new IllegalArgumentException(emptyMsg);
        return byteArray;
    }
}