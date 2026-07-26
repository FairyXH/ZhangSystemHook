package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class NegativeIntegerEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.NegativeInteger> {
    private static final java.math.BigInteger MINUS_ONE = java.math.BigInteger.valueOf(-1);

    public NegativeIntegerEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.NegativeInteger dataItem) throws co.nstant.in.cbor.CborException {
        encodeTypeAndLength(co.nstant.in.cbor.model.MajorType.NEGATIVE_INTEGER, MINUS_ONE.subtract(dataItem.getValue()).abs());
    }
}
