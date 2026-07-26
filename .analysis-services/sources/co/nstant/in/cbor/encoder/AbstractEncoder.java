package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractEncoder<T> {
    protected final co.nstant.in.cbor.CborEncoder encoder;
    private final java.io.OutputStream outputStream;

    public abstract void encode(T t) throws co.nstant.in.cbor.CborException;

    public AbstractEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        this.encoder = encoder;
        this.outputStream = outputStream;
    }

    protected void encodeTypeChunked(co.nstant.in.cbor.model.MajorType majorType) throws co.nstant.in.cbor.CborException {
        int symbol = majorType.getValue() << 5;
        try {
            this.outputStream.write(symbol | co.nstant.in.cbor.model.AdditionalInformation.INDEFINITE.getValue());
        } catch (java.io.IOException ioException) {
            throw new co.nstant.in.cbor.CborException(ioException);
        }
    }

    protected void encodeTypeAndLength(co.nstant.in.cbor.model.MajorType majorType, long length) throws co.nstant.in.cbor.CborException {
        int symbol = majorType.getValue() << 5;
        if (length <= 23) {
            write((int) (((long) symbol) | length));
            return;
        }
        if (length <= 255) {
            write(symbol | co.nstant.in.cbor.model.AdditionalInformation.ONE_BYTE.getValue());
            write((int) length);
            return;
        }
        if (length <= 65535) {
            write(symbol | co.nstant.in.cbor.model.AdditionalInformation.TWO_BYTES.getValue());
            write((int) (length >> 8));
            write((int) (255 & length));
            return;
        }
        if (length <= 4294967295L) {
            write(symbol | co.nstant.in.cbor.model.AdditionalInformation.FOUR_BYTES.getValue());
            write((int) ((length >> 24) & 255));
            write((int) ((length >> 16) & 255));
            write((int) ((length >> 8) & 255));
            write((int) (255 & length));
            return;
        }
        write(symbol | co.nstant.in.cbor.model.AdditionalInformation.EIGHT_BYTES.getValue());
        write((int) ((length >> 56) & 255));
        write((int) ((length >> 48) & 255));
        write((int) ((length >> 40) & 255));
        write((int) ((length >> 32) & 255));
        write((int) ((length >> 24) & 255));
        write((int) ((length >> 16) & 255));
        write((int) ((length >> 8) & 255));
        write((int) (255 & length));
    }

    protected void encodeTypeAndLength(co.nstant.in.cbor.model.MajorType majorType, java.math.BigInteger length) throws co.nstant.in.cbor.CborException {
        boolean negative = majorType == co.nstant.in.cbor.model.MajorType.NEGATIVE_INTEGER;
        int symbol = majorType.getValue() << 5;
        if (length.compareTo(java.math.BigInteger.valueOf(24L)) == -1) {
            write(length.intValue() | symbol);
            return;
        }
        if (length.compareTo(java.math.BigInteger.valueOf(256L)) == -1) {
            write(symbol | co.nstant.in.cbor.model.AdditionalInformation.ONE_BYTE.getValue());
            write(length.intValue());
            return;
        }
        if (length.compareTo(java.math.BigInteger.valueOf(65536L)) == -1) {
            write(symbol | co.nstant.in.cbor.model.AdditionalInformation.TWO_BYTES.getValue());
            long twoByteValue = length.longValue();
            write((int) (twoByteValue >> 8));
            write((int) (twoByteValue & 255));
            return;
        }
        if (length.compareTo(java.math.BigInteger.valueOf(4294967296L)) == -1) {
            write(symbol | co.nstant.in.cbor.model.AdditionalInformation.FOUR_BYTES.getValue());
            long fourByteValue = length.longValue();
            write((int) ((fourByteValue >> 24) & 255));
            write((int) ((fourByteValue >> 16) & 255));
            write((int) ((fourByteValue >> 8) & 255));
            write((int) (fourByteValue & 255));
            return;
        }
        if (length.compareTo(new java.math.BigInteger("18446744073709551616")) == -1) {
            write(symbol | co.nstant.in.cbor.model.AdditionalInformation.EIGHT_BYTES.getValue());
            java.math.BigInteger mask = java.math.BigInteger.valueOf(255L);
            write(length.shiftRight(56).and(mask).intValue());
            write(length.shiftRight(48).and(mask).intValue());
            write(length.shiftRight(40).and(mask).intValue());
            write(length.shiftRight(32).and(mask).intValue());
            write(length.shiftRight(24).and(mask).intValue());
            write(length.shiftRight(16).and(mask).intValue());
            write(length.shiftRight(8).and(mask).intValue());
            write(length.and(mask).intValue());
            return;
        }
        if (negative) {
            this.encoder.encode(new co.nstant.in.cbor.model.Tag(3L));
        } else {
            this.encoder.encode(new co.nstant.in.cbor.model.Tag(2L));
        }
        this.encoder.encode(new co.nstant.in.cbor.model.ByteString(length.toByteArray()));
    }

    protected void write(int b) throws co.nstant.in.cbor.CborException {
        try {
            this.outputStream.write(b);
        } catch (java.io.IOException ioException) {
            throw new co.nstant.in.cbor.CborException(ioException);
        }
    }

    protected void write(byte[] bytes) throws co.nstant.in.cbor.CborException {
        try {
            this.outputStream.write(bytes);
        } catch (java.io.IOException ioException) {
            throw new co.nstant.in.cbor.CborException(ioException);
        }
    }
}
