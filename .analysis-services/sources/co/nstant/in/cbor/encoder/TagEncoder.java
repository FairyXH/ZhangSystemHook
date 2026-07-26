package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class TagEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.Tag> {
    public TagEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.Tag tag) throws co.nstant.in.cbor.CborException {
        encodeTypeAndLength(co.nstant.in.cbor.model.MajorType.TAG, tag.getValue());
    }
}
