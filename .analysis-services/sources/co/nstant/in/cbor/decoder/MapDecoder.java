package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class MapDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.Map> {
    public MapDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.Map decode(int initialByte) throws co.nstant.in.cbor.CborException {
        long length = getLength(initialByte);
        if (length == -1) {
            return decodeInfinitiveLength();
        }
        return decodeFixedLength(length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x004c, code lost:
    
        throw new co.nstant.in.cbor.CborException("Unexpected end of stream");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private co.nstant.in.cbor.model.Map decodeInfinitiveLength() throws co.nstant.in.cbor.CborException {
        /*
            r5 = this;
            co.nstant.in.cbor.model.Map r0 = new co.nstant.in.cbor.model.Map
            r0.<init>()
            r1 = 1
            r0.setChunked(r1)
            co.nstant.in.cbor.CborDecoder r1 = r5.decoder
            boolean r1 = r1.isAutoDecodeInfinitiveMaps()
            if (r1 == 0) goto L4d
        L11:
            co.nstant.in.cbor.CborDecoder r1 = r5.decoder
            co.nstant.in.cbor.model.DataItem r1 = r1.decodeNext()
            co.nstant.in.cbor.model.Special r2 = co.nstant.in.cbor.model.Special.BREAK
            boolean r2 = r2.equals(r1)
            if (r2 == 0) goto L20
            goto L4d
        L20:
            co.nstant.in.cbor.CborDecoder r2 = r5.decoder
            co.nstant.in.cbor.model.DataItem r2 = r2.decodeNext()
            if (r1 == 0) goto L45
            if (r2 == 0) goto L45
            co.nstant.in.cbor.CborDecoder r3 = r5.decoder
            boolean r3 = r3.isRejectDuplicateKeys()
            if (r3 == 0) goto L41
            co.nstant.in.cbor.model.DataItem r3 = r0.get(r1)
            if (r3 != 0) goto L39
            goto L41
        L39:
            co.nstant.in.cbor.CborException r3 = new co.nstant.in.cbor.CborException
            java.lang.String r4 = "Duplicate key found in map"
            r3.<init>(r4)
            throw r3
        L41:
            r0.put(r1, r2)
            goto L11
        L45:
            co.nstant.in.cbor.CborException r3 = new co.nstant.in.cbor.CborException
            java.lang.String r4 = "Unexpected end of stream"
            r3.<init>(r4)
            throw r3
        L4d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: co.nstant.in.cbor.decoder.MapDecoder.decodeInfinitiveLength():co.nstant.in.cbor.model.Map");
    }

    private co.nstant.in.cbor.model.Map decodeFixedLength(long length) throws co.nstant.in.cbor.CborException {
        co.nstant.in.cbor.model.Map map = new co.nstant.in.cbor.model.Map((int) length);
        for (long i = 0; i < length; i++) {
            co.nstant.in.cbor.model.DataItem key = this.decoder.decodeNext();
            co.nstant.in.cbor.model.DataItem value = this.decoder.decodeNext();
            if (key == null || value == null) {
                throw new co.nstant.in.cbor.CborException("Unexpected end of stream");
            }
            if (this.decoder.isRejectDuplicateKeys() && map.get(key) != null) {
                throw new co.nstant.in.cbor.CborException("Duplicate key found in map");
            }
            map.put(key, value);
        }
        return map;
    }
}
