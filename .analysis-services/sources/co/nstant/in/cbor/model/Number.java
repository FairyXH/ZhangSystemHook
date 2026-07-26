package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public abstract class Number extends co.nstant.in.cbor.model.DataItem {
    private final java.math.BigInteger value;

    protected Number(co.nstant.in.cbor.model.MajorType majorType, java.math.BigInteger value) {
        super(majorType);
        this.value = (java.math.BigInteger) java.util.Objects.requireNonNull(value);
    }

    public java.math.BigInteger getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.Number)) {
            return false;
        }
        co.nstant.in.cbor.model.Number other = (co.nstant.in.cbor.model.Number) object;
        return super.equals(object) && this.value.equals(other.value);
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ this.value.hashCode();
    }

    public java.lang.String toString() {
        return this.value.toString();
    }
}
