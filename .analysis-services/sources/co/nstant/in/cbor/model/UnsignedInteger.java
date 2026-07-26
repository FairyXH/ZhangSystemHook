package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class UnsignedInteger extends co.nstant.in.cbor.model.Number {
    public UnsignedInteger(long value) {
        this(java.math.BigInteger.valueOf(value));
        assertTrue(value >= 0, "value " + value + " is not >= 0");
    }

    public UnsignedInteger(java.math.BigInteger value) {
        super(co.nstant.in.cbor.model.MajorType.UNSIGNED_INTEGER, value);
    }
}
