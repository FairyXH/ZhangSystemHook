package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class SinglePrecisionFloatDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.SinglePrecisionFloat> {
    public SinglePrecisionFloatDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.SinglePrecisionFloat decode(int initialByte) throws co.nstant.in.cbor.CborException {
        int bits = 0 | (nextSymbol() & 255);
        return new co.nstant.in.cbor.model.SinglePrecisionFloat(java.lang.Float.intBitsToFloat((((((bits << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)) << 8) | (nextSymbol() & 255)));
    }
}
