package co.nstant.in.cbor.builder;

/* JADX INFO: loaded from: classes.dex */
public abstract class AbstractBuilder<T> {
    private final T parent;

    public AbstractBuilder(T parent) {
        this.parent = parent;
    }

    protected T getParent() {
        return this.parent;
    }

    protected void addChunk(co.nstant.in.cbor.model.DataItem dataItem) {
        throw new java.lang.IllegalStateException();
    }

    protected co.nstant.in.cbor.model.DataItem convert(long value) {
        if (value >= 0) {
            return new co.nstant.in.cbor.model.UnsignedInteger(value);
        }
        return new co.nstant.in.cbor.model.NegativeInteger(value);
    }

    protected co.nstant.in.cbor.model.DataItem convert(java.math.BigInteger value) {
        if (value.signum() == -1) {
            return new co.nstant.in.cbor.model.NegativeInteger(value);
        }
        return new co.nstant.in.cbor.model.UnsignedInteger(value);
    }

    protected co.nstant.in.cbor.model.DataItem convert(boolean value) {
        if (value) {
            return co.nstant.in.cbor.model.SimpleValue.TRUE;
        }
        return co.nstant.in.cbor.model.SimpleValue.FALSE;
    }

    protected co.nstant.in.cbor.model.DataItem convert(byte[] bytes) {
        return new co.nstant.in.cbor.model.ByteString(bytes);
    }

    protected co.nstant.in.cbor.model.DataItem convert(java.lang.String string) {
        return new co.nstant.in.cbor.model.UnicodeString(string);
    }

    protected co.nstant.in.cbor.model.DataItem convert(float value) {
        if (isHalfPrecisionEnough(value)) {
            return new co.nstant.in.cbor.model.HalfPrecisionFloat(value);
        }
        return new co.nstant.in.cbor.model.SinglePrecisionFloat(value);
    }

    protected co.nstant.in.cbor.model.DataItem convert(double value) {
        return new co.nstant.in.cbor.model.DoublePrecisionFloat(value);
    }

    protected co.nstant.in.cbor.model.Tag tag(long value) {
        return new co.nstant.in.cbor.model.Tag(value);
    }

    private boolean isHalfPrecisionEnough(float value) {
        try {
            java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
            co.nstant.in.cbor.encoder.HalfPrecisionFloatEncoder encoder = getHalfPrecisionFloatEncoder(outputStream);
            encoder.encode(new co.nstant.in.cbor.model.HalfPrecisionFloat(value));
            byte[] bytes = outputStream.toByteArray();
            java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(bytes);
            co.nstant.in.cbor.decoder.HalfPrecisionFloatDecoder decoder = getHalfPrecisionFloatDecoder(inputStream);
            if (inputStream.read() == -1) {
                throw new co.nstant.in.cbor.CborException("unexpected end of stream");
            }
            co.nstant.in.cbor.model.HalfPrecisionFloat halfPrecisionFloat = decoder.decode(0);
            return value == halfPrecisionFloat.getValue();
        } catch (co.nstant.in.cbor.CborException e) {
            return false;
        }
    }

    protected co.nstant.in.cbor.encoder.HalfPrecisionFloatEncoder getHalfPrecisionFloatEncoder(java.io.OutputStream outputStream) {
        return new co.nstant.in.cbor.encoder.HalfPrecisionFloatEncoder(null, outputStream);
    }

    protected co.nstant.in.cbor.decoder.HalfPrecisionFloatDecoder getHalfPrecisionFloatDecoder(java.io.InputStream inputStream) {
        return new co.nstant.in.cbor.decoder.HalfPrecisionFloatDecoder(null, inputStream);
    }
}
