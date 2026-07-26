package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class ByteString extends co.nstant.in.cbor.model.ChunkableDataItem {
    private final byte[] bytes;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ co.nstant.in.cbor.model.ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public ByteString(byte[] bytes) {
        super(co.nstant.in.cbor.model.MajorType.BYTE_STRING);
        if (bytes == null) {
            this.bytes = null;
        } else {
            this.bytes = bytes;
        }
    }

    public byte[] getBytes() {
        if (this.bytes == null) {
            return null;
        }
        return this.bytes;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.ByteString)) {
            return false;
        }
        co.nstant.in.cbor.model.ByteString other = (co.nstant.in.cbor.model.ByteString) object;
        return super.equals(object) && java.util.Arrays.equals(this.bytes, other.bytes);
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ java.util.Arrays.hashCode(this.bytes);
    }
}
