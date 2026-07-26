package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class ArrayDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.Array> {
    public ArrayDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.Array decode(int initialByte) throws co.nstant.in.cbor.CborException {
        long length = getLength(initialByte);
        if (length == -1) {
            return decodeInfinitiveLength();
        }
        return decodeFixedLength(length);
    }

    private co.nstant.in.cbor.model.Array decodeInfinitiveLength() throws co.nstant.in.cbor.CborException {
        co.nstant.in.cbor.model.Array array = new co.nstant.in.cbor.model.Array();
        array.setChunked(true);
        if (this.decoder.isAutoDecodeInfinitiveArrays()) {
            while (true) {
                co.nstant.in.cbor.model.DataItem dataItem = this.decoder.decodeNext();
                if (dataItem == null) {
                    throw new co.nstant.in.cbor.CborException("Unexpected end of stream");
                }
                if (co.nstant.in.cbor.model.Special.BREAK.equals(dataItem)) {
                    array.add(co.nstant.in.cbor.model.Special.BREAK);
                    break;
                }
                array.add(dataItem);
            }
        }
        return array;
    }

    private co.nstant.in.cbor.model.Array decodeFixedLength(long length) throws co.nstant.in.cbor.CborException {
        co.nstant.in.cbor.model.Array array = new co.nstant.in.cbor.model.Array();
        for (long i = 0; i < length; i++) {
            co.nstant.in.cbor.model.DataItem dataItem = this.decoder.decodeNext();
            if (dataItem == null) {
                throw new co.nstant.in.cbor.CborException("Unexpected end of stream");
            }
            array.add(dataItem);
        }
        return array;
    }
}
