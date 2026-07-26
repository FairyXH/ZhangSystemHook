package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class Tag extends co.nstant.in.cbor.model.DataItem {
    private final long value;

    public Tag(long value) {
        super(co.nstant.in.cbor.model.MajorType.TAG);
        this.value = value;
    }

    public long getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.Tag)) {
            return false;
        }
        co.nstant.in.cbor.model.Tag other = (co.nstant.in.cbor.model.Tag) object;
        return super.equals(object) && this.value == other.value;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ java.util.Objects.hashCode(java.lang.Long.valueOf(this.value));
    }

    public java.lang.String toString() {
        return "Tag(" + this.value + ")";
    }
}
