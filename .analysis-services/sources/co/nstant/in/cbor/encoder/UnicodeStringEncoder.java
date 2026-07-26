package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class UnicodeStringEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.UnicodeString> {
    public UnicodeStringEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.UnicodeString dataItem) throws co.nstant.in.cbor.CborException {
        java.lang.String string = dataItem.getString();
        if (dataItem.isChunked()) {
            encodeTypeChunked(co.nstant.in.cbor.model.MajorType.UNICODE_STRING);
            if (string != null) {
                encode(new co.nstant.in.cbor.model.UnicodeString(string));
                return;
            }
            return;
        }
        if (string == null) {
            this.encoder.encode(co.nstant.in.cbor.model.SimpleValue.NULL);
            return;
        }
        byte[] bytes = string.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        encodeTypeAndLength(co.nstant.in.cbor.model.MajorType.UNICODE_STRING, bytes.length);
        write(bytes);
    }
}
