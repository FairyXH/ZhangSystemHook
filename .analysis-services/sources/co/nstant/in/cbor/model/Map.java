package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class Map extends co.nstant.in.cbor.model.ChunkableDataItem {
    private final java.util.List<co.nstant.in.cbor.model.DataItem> keys;
    private final java.util.HashMap<co.nstant.in.cbor.model.DataItem, co.nstant.in.cbor.model.DataItem> map;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ co.nstant.in.cbor.model.ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public Map() {
        super(co.nstant.in.cbor.model.MajorType.MAP);
        this.keys = new java.util.LinkedList();
        this.map = new java.util.HashMap<>();
    }

    public Map(int initialCapacity) {
        super(co.nstant.in.cbor.model.MajorType.MAP);
        this.keys = new java.util.LinkedList();
        this.map = new java.util.HashMap<>(initialCapacity);
    }

    public co.nstant.in.cbor.model.Map put(co.nstant.in.cbor.model.DataItem key, co.nstant.in.cbor.model.DataItem value) {
        if (this.map.put(key, value) == null) {
            this.keys.add(key);
        }
        return this;
    }

    public co.nstant.in.cbor.model.DataItem get(co.nstant.in.cbor.model.DataItem key) {
        return this.map.get(key);
    }

    public co.nstant.in.cbor.model.DataItem remove(co.nstant.in.cbor.model.DataItem key) {
        this.keys.remove(key);
        return this.map.remove(key);
    }

    public java.util.Collection<co.nstant.in.cbor.model.DataItem> getKeys() {
        return this.keys;
    }

    public java.util.Collection<co.nstant.in.cbor.model.DataItem> getValues() {
        return this.map.values();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.Map)) {
            return false;
        }
        co.nstant.in.cbor.model.Map other = (co.nstant.in.cbor.model.Map) object;
        return super.equals(object) && this.map.equals(other.map);
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ this.map.hashCode();
    }

    public java.lang.String toString() {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        if (isChunked()) {
            stringBuilder.append("{_ ");
        } else {
            stringBuilder.append("{ ");
        }
        for (co.nstant.in.cbor.model.DataItem key : this.keys) {
            stringBuilder.append(key).append(": ").append(this.map.get(key)).append(", ");
        }
        if (stringBuilder.toString().endsWith(", ")) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
