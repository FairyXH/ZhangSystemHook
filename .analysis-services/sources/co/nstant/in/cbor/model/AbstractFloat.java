package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class AbstractFloat extends co.nstant.in.cbor.model.Special {
    private final float value;

    public AbstractFloat(co.nstant.in.cbor.model.SpecialType specialType, float value) {
        super(specialType);
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.AbstractFloat)) {
            return false;
        }
        co.nstant.in.cbor.model.AbstractFloat other = (co.nstant.in.cbor.model.AbstractFloat) object;
        return super.equals(object) && this.value == other.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ java.util.Objects.hashCode(java.lang.Float.valueOf(this.value));
    }
}
