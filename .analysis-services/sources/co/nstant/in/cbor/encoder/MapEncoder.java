package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class MapEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.Map> {
    public MapEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.Map map) throws co.nstant.in.cbor.CborException {
        java.util.Collection<co.nstant.in.cbor.model.DataItem> keys = map.getKeys();
        if (map.isChunked()) {
            encodeTypeChunked(co.nstant.in.cbor.model.MajorType.MAP);
        } else {
            encodeTypeAndLength(co.nstant.in.cbor.model.MajorType.MAP, keys.size());
        }
        if (keys.isEmpty()) {
            return;
        }
        if (map.isChunked()) {
            for (co.nstant.in.cbor.model.DataItem key : keys) {
                this.encoder.encode(key);
                this.encoder.encode(map.get(key));
            }
            this.encoder.encode(co.nstant.in.cbor.model.SimpleValue.BREAK);
            return;
        }
        java.util.TreeMap<byte[], byte[]> sortedMap = new java.util.TreeMap<>((java.util.Comparator<? super byte[]>) new java.util.Comparator<byte[]>() { // from class: co.nstant.in.cbor.encoder.MapEncoder.1
            @Override // java.util.Comparator
            public int compare(byte[] o1, byte[] o2) {
                if (o1.length < o2.length) {
                    return -1;
                }
                if (o1.length > o2.length) {
                    return 1;
                }
                for (int i = 0; i < o1.length; i++) {
                    if (o1[i] < o2[i]) {
                        return -1;
                    }
                    if (o1[i] > o2[i]) {
                        return 1;
                    }
                }
                return 0;
            }
        });
        java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();
        co.nstant.in.cbor.CborEncoder e = new co.nstant.in.cbor.CborEncoder(byteArrayOutputStream);
        for (co.nstant.in.cbor.model.DataItem key2 : keys) {
            e.encode(key2);
            byte[] keyBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.reset();
            e.encode(map.get(key2));
            byte[] valueBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.reset();
            sortedMap.put(keyBytes, valueBytes);
        }
        for (java.util.Map.Entry<byte[], byte[]> entry : sortedMap.entrySet()) {
            write(entry.getKey());
            write(entry.getValue());
        }
    }
}
