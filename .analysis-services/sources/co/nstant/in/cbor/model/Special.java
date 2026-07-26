package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class Special extends co.nstant.in.cbor.model.DataItem {
    public static final co.nstant.in.cbor.model.Special BREAK = new co.nstant.in.cbor.model.Special(co.nstant.in.cbor.model.SpecialType.BREAK);
    private final co.nstant.in.cbor.model.SpecialType specialType;

    protected Special(co.nstant.in.cbor.model.SpecialType specialType) {
        super(co.nstant.in.cbor.model.MajorType.SPECIAL);
        this.specialType = (co.nstant.in.cbor.model.SpecialType) java.util.Objects.requireNonNull(specialType);
    }

    public co.nstant.in.cbor.model.SpecialType getSpecialType() {
        return this.specialType;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.Special)) {
            return false;
        }
        co.nstant.in.cbor.model.Special other = (co.nstant.in.cbor.model.Special) object;
        return super.equals(object) && this.specialType == other.specialType;
    }

    @Override // co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ java.util.Objects.hashCode(this.specialType);
    }

    public java.lang.String toString() {
        return this.specialType.name();
    }
}
