package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class UnicodeString extends co.nstant.in.cbor.model.ChunkableDataItem {
    private final java.lang.String string;

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ boolean isChunked() {
        return super.isChunked();
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem
    public /* bridge */ /* synthetic */ co.nstant.in.cbor.model.ChunkableDataItem setChunked(boolean z) {
        return super.setChunked(z);
    }

    public UnicodeString(java.lang.String string) {
        super(co.nstant.in.cbor.model.MajorType.UNICODE_STRING);
        this.string = string;
    }

    public java.lang.String toString() {
        if (this.string == null) {
            return "null";
        }
        return this.string;
    }

    public java.lang.String getString() {
        return this.string;
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.UnicodeString) || !super.equals(object)) {
            return false;
        }
        co.nstant.in.cbor.model.UnicodeString other = (co.nstant.in.cbor.model.UnicodeString) object;
        if (this.string == null) {
            return other.string == null;
        }
        return this.string.equals(other.string);
    }

    @Override // co.nstant.in.cbor.model.ChunkableDataItem, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        if (this.string == null) {
            return 0;
        }
        int hash = super.hashCode();
        return hash + this.string.hashCode();
    }
}
