package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class SpecialDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.Special> {
    private final co.nstant.in.cbor.decoder.DoublePrecisionFloatDecoder doublePrecisionFloatDecoder;
    private final co.nstant.in.cbor.decoder.HalfPrecisionFloatDecoder halfPrecisionFloatDecoder;
    private final co.nstant.in.cbor.decoder.SinglePrecisionFloatDecoder singlePrecisionFloatDecoder;

    public SpecialDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
        this.halfPrecisionFloatDecoder = new co.nstant.in.cbor.decoder.HalfPrecisionFloatDecoder(decoder, inputStream);
        this.singlePrecisionFloatDecoder = new co.nstant.in.cbor.decoder.SinglePrecisionFloatDecoder(decoder, inputStream);
        this.doublePrecisionFloatDecoder = new co.nstant.in.cbor.decoder.DoublePrecisionFloatDecoder(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.Special decode(int initialByte) throws co.nstant.in.cbor.CborException {
        switch (co.nstant.in.cbor.model.SpecialType.ofByte(initialByte)) {
            case BREAK:
                return co.nstant.in.cbor.model.Special.BREAK;
            case SIMPLE_VALUE:
                switch (co.nstant.in.cbor.model.SimpleValueType.ofByte(initialByte)) {
                    case FALSE:
                        return co.nstant.in.cbor.model.SimpleValue.FALSE;
                    case TRUE:
                        return co.nstant.in.cbor.model.SimpleValue.TRUE;
                    case NULL:
                        return co.nstant.in.cbor.model.SimpleValue.NULL;
                    case UNDEFINED:
                        return co.nstant.in.cbor.model.SimpleValue.UNDEFINED;
                    case UNALLOCATED:
                        return new co.nstant.in.cbor.model.SimpleValue(initialByte & 31);
                    default:
                        throw new co.nstant.in.cbor.CborException("Not implemented");
                }
            case IEEE_754_HALF_PRECISION_FLOAT:
                return this.halfPrecisionFloatDecoder.decode(initialByte);
            case IEEE_754_SINGLE_PRECISION_FLOAT:
                return this.singlePrecisionFloatDecoder.decode(initialByte);
            case IEEE_754_DOUBLE_PRECISION_FLOAT:
                return this.doublePrecisionFloatDecoder.decode(initialByte);
            case SIMPLE_VALUE_NEXT_BYTE:
                return new co.nstant.in.cbor.model.SimpleValue(nextSymbol());
            default:
                throw new co.nstant.in.cbor.CborException("Not implemented");
        }
    }
}
