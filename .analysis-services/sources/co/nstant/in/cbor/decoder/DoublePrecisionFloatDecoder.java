package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class DoublePrecisionFloatDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.DoublePrecisionFloat> {
    public DoublePrecisionFloatDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.DoublePrecisionFloat decode(int initialByte) throws co.nstant.in.cbor.CborException {
        long bits = 0 | ((long) (nextSymbol() & 255));
        return new co.nstant.in.cbor.model.DoublePrecisionFloat(java.lang.Double.longBitsToDouble((((((((((((((bits << 8) | ((long) (nextSymbol() & 255))) << 8) | ((long) (nextSymbol() & 255))) << 8) | ((long) (nextSymbol() & 255))) << 8) | ((long) (nextSymbol() & 255))) << 8) | ((long) (nextSymbol() & 255))) << 8) | ((long) (nextSymbol() & 255))) << 8) | ((long) (nextSymbol() & 255))));
    }
}
