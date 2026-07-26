package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class UnicodeStringDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.UnicodeString> {
    public UnicodeStringDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.UnicodeString decode(int initialByte) throws co.nstant.in.cbor.CborException {
        long length = getLength(initialByte);
        if (length == -1) {
            if (this.decoder.isAutoDecodeInfinitiveUnicodeStrings()) {
                return decodeInfinitiveLength();
            }
            co.nstant.in.cbor.model.UnicodeString unicodeString = new co.nstant.in.cbor.model.UnicodeString(null);
            unicodeString.setChunked(true);
            return unicodeString;
        }
        return decodeFixedLength(length);
    }

    private co.nstant.in.cbor.model.UnicodeString decodeInfinitiveLength() throws co.nstant.in.cbor.CborException {
        java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream();
        while (true) {
            co.nstant.in.cbor.model.DataItem dataItem = this.decoder.decodeNext();
            if (dataItem == null) {
                throw new co.nstant.in.cbor.CborException("Unexpected end of stream");
            }
            co.nstant.in.cbor.model.MajorType majorType = dataItem.getMajorType();
            if (!co.nstant.in.cbor.model.Special.BREAK.equals(dataItem)) {
                if (majorType == co.nstant.in.cbor.model.MajorType.UNICODE_STRING) {
                    co.nstant.in.cbor.model.UnicodeString unicodeString = (co.nstant.in.cbor.model.UnicodeString) dataItem;
                    byte[] byteArray = unicodeString.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
                    bytes.write(byteArray, 0, byteArray.length);
                } else {
                    throw new co.nstant.in.cbor.CborException("Unexpected major type " + majorType);
                }
            } else {
                return new co.nstant.in.cbor.model.UnicodeString(new java.lang.String(bytes.toByteArray(), java.nio.charset.StandardCharsets.UTF_8));
            }
        }
    }

    private co.nstant.in.cbor.model.UnicodeString decodeFixedLength(long length) throws co.nstant.in.cbor.CborException {
        java.io.ByteArrayOutputStream bytes = new java.io.ByteArrayOutputStream((int) length);
        for (long i = 0; i < length; i++) {
            bytes.write(nextSymbol());
        }
        return new co.nstant.in.cbor.model.UnicodeString(new java.lang.String(bytes.toByteArray(), java.nio.charset.StandardCharsets.UTF_8));
    }
}
