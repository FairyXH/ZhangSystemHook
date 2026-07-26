package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class DoublePrecisionFloatEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.DoublePrecisionFloat> {
    public DoublePrecisionFloatEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.DoublePrecisionFloat dataItem) throws co.nstant.in.cbor.CborException {
        write(251);
        long bits = java.lang.Double.doubleToRawLongBits(dataItem.getValue());
        write((int) ((bits >> 56) & 255));
        write((int) ((bits >> 48) & 255));
        write((int) ((bits >> 40) & 255));
        write((int) ((bits >> 32) & 255));
        write((int) ((bits >> 24) & 255));
        write((int) ((bits >> 16) & 255));
        write((int) ((bits >> 8) & 255));
        write((int) ((bits >> 0) & 255));
    }
}
