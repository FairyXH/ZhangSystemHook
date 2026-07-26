package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class Array extends co.nstant.in.cbor.model.ChunkableDataItem {
    private final java.util.ArrayList<co.nstant.in.cbor.model.DataItem> objects;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ co.nstant.in.cbor.model.ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public Array() {
        super(co.nstant.in.cbor.model.MajorType.ARRAY);
        this.objects = new java.util.ArrayList<>();
    }

    public co.nstant.in.cbor.model.Array add(co.nstant.in.cbor.model.DataItem object) {
        this.objects.add(object);
        return this;
    }

    public java.util.List<co.nstant.in.cbor.model.DataItem> getDataItems() {
        return this.objects;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.Array)) {
            return false;
        }
        co.nstant.in.cbor.model.Array other = (co.nstant.in.cbor.model.Array) object;
        return super.equals(object) && this.objects.equals(other.objects);
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ this.objects.hashCode();
    }

    public java.lang.String toString() {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder("[");
        if (isChunked()) {
            stringBuilder.append("_ ");
        }
        stringBuilder.append(java.util.Arrays.toString(this.objects.toArray()).substring(1));
        return stringBuilder.toString();
    }
}
