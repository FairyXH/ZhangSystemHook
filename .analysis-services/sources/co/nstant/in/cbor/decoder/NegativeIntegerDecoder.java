package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class NegativeIntegerDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.NegativeInteger> {
    private static final java.math.BigInteger MINUS_ONE = java.math.BigInteger.valueOf(-1);

    public NegativeIntegerDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.NegativeInteger decode(int initialByte) throws co.nstant.in.cbor.CborException {
        return new co.nstant.in.cbor.model.NegativeInteger(MINUS_ONE.subtract(getLengthAsBigInteger(initialByte)));
    }
}
