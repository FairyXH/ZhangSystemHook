package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class NegativeInteger extends co.nstant.in.cbor.model.Number {
    public NegativeInteger(long value) {
        this(java.math.BigInteger.valueOf(value));
        assertTrue(value < 0, "value " + value + " is not < 0");
    }

    public NegativeInteger(java.math.BigInteger value) {
        super(co.nstant.in.cbor.model.MajorType.NEGATIVE_INTEGER, value);
    }
}
