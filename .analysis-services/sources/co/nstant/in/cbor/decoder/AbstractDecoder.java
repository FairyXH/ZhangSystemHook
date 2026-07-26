package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractDecoder<T> {
    protected static final int INFINITY = -1;
    protected final co.nstant.in.cbor.CborDecoder decoder;
    protected final java.io.InputStream inputStream;

    public abstract T decode(int i) throws co.nstant.in.cbor.CborException;

    public AbstractDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        this.decoder = decoder;
        this.inputStream = inputStream;
    }

    protected int nextSymbol() throws co.nstant.in.cbor.CborException {
        try {
            int symbol = this.inputStream.read();
            if (symbol == -1) {
                throw new java.io.IOException("Unexpected end of stream");
            }
            return symbol;
        } catch (java.io.IOException ioException) {
            throw new co.nstant.in.cbor.CborException(ioException);
        }
    }

    protected long getLength(int initialByte) throws co.nstant.in.cbor.CborException {
        switch (co.nstant.in.cbor.model.AdditionalInformation.ofByte(initialByte)) {
            case DIRECT:
                return initialByte & 31;
            case ONE_BYTE:
                return nextSymbol();
            case TWO_BYTES:
                long twoByteValue = 0 | ((long) (nextSymbol() << 8));
                return twoByteValue | ((long) (nextSymbol() << 0));
            case FOUR_BYTES:
                long fourByteValue = 0 | (((long) nextSymbol()) << 24);
                return (((long) nextSymbol()) << 16) | fourByteValue | (((long) nextSymbol()) << 8) | (((long) nextSymbol()) << 0);
            case EIGHT_BYTES:
                long eightByteValue = 0 | (((long) nextSymbol()) << 56);
                return (((long) nextSymbol()) << 16) | eightByteValue | (((long) nextSymbol()) << 48) | (((long) nextSymbol()) << 40) | (((long) nextSymbol()) << 32) | (((long) nextSymbol()) << 24) | (((long) nextSymbol()) << 8) | (((long) nextSymbol()) << 0);
            case INDEFINITE:
                return -1L;
            default:
                throw new co.nstant.in.cbor.CborException("Reserved additional information");
        }
    }

    protected java.math.BigInteger getLengthAsBigInteger(int initialByte) throws co.nstant.in.cbor.CborException {
        switch (co.nstant.in.cbor.model.AdditionalInformation.ofByte(initialByte)) {
            case DIRECT:
                return java.math.BigInteger.valueOf(initialByte & 31);
            case ONE_BYTE:
                return java.math.BigInteger.valueOf(nextSymbol());
            case TWO_BYTES:
                long twoByteValue = 0 | ((long) (nextSymbol() << 8));
                return java.math.BigInteger.valueOf(twoByteValue | ((long) (nextSymbol() << 0)));
            case FOUR_BYTES:
                long fourByteValue = 0 | (((long) nextSymbol()) << 24);
                return java.math.BigInteger.valueOf((((long) nextSymbol()) << 16) | fourByteValue | (((long) nextSymbol()) << 8) | (((long) nextSymbol()) << 0));
            case EIGHT_BYTES:
                java.math.BigInteger eightByteValue = java.math.BigInteger.ZERO;
                return eightByteValue.or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(56)).or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(48)).or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(40)).or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(32)).or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(24)).or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(16)).or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(8)).or(java.math.BigInteger.valueOf(nextSymbol()).shiftLeft(0));
            case INDEFINITE:
                return java.math.BigInteger.valueOf(-1L);
            default:
                throw new co.nstant.in.cbor.CborException("Reserved additional information");
        }
    }
}
