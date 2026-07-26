package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class HalfPrecisionFloatDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.HalfPrecisionFloat> {
    public HalfPrecisionFloatDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.HalfPrecisionFloat decode(int initialByte) throws co.nstant.in.cbor.CborException {
        int bits = (nextSymbol() << 8) | nextSymbol();
        return new co.nstant.in.cbor.model.HalfPrecisionFloat(toFloat(bits));
    }

    private static float toFloat(int bits) {
        int s = (32768 & bits) >> 15;
        int e = (bits & 31744) >> 10;
        int f = bits & 1023;
        if (e == 0) {
            return (float) (((double) (s == 0 ? 1 : -1)) * java.lang.Math.pow(2.0d, -14.0d) * (((double) f) / java.lang.Math.pow(2.0d, 10.0d)));
        }
        if (e == 31) {
            if (f != 0) {
                return Float.NaN;
            }
            return (s == 0 ? 1 : -1) * Float.POSITIVE_INFINITY;
        }
        return (float) (((double) (s == 0 ? 1 : -1)) * java.lang.Math.pow(2.0d, e - 15) * ((((double) f) / java.lang.Math.pow(2.0d, 10.0d)) + 1.0d));
    }
}
