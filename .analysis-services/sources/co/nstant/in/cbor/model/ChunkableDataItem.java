package co.nstant.in.cbor.model;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: loaded from: classes.dex */
public class ChunkableDataItem extends co.nstant.in.cbor.model.DataItem {
    private boolean chunked;

    protected ChunkableDataItem(co.nstant.in.cbor.model.MajorType majorType) {
        super(majorType);
        this.chunked = false;
    }

    public boolean isChunked() {
        return this.chunked;
    }

    public co.nstant.in.cbor.model.ChunkableDataItem setChunked(boolean chunked) {
        this.chunked = chunked;
        return this;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.ChunkableDataItem)) {
            return false;
        }
        co.nstant.in.cbor.model.ChunkableDataItem other = (co.nstant.in.cbor.model.ChunkableDataItem) object;
        return super.equals(object) && this.chunked == other.chunked;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ java.util.Objects.hashCode(java.lang.Boolean.valueOf(this.chunked));
    }
}
