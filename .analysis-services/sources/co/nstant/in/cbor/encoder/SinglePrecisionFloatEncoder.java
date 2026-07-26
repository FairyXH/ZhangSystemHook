package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class SinglePrecisionFloatEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.SinglePrecisionFloat> {
    public SinglePrecisionFloatEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.SinglePrecisionFloat dataItem) throws co.nstant.in.cbor.CborException {
        write(250);
        int bits = java.lang.Float.floatToRawIntBits(dataItem.getValue());
        write((bits >> 24) & 255);
        write((bits >> 16) & 255);
        write((bits >> 8) & 255);
        write((bits >> 0) & 255);
    }
}
