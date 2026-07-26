package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class DoublePrecisionFloat extends co.nstant.in.cbor.model.Special {
    private final double value;

    public DoublePrecisionFloat(double value) {
        super(co.nstant.in.cbor.model.SpecialType.IEEE_754_DOUBLE_PRECISION_FLOAT);
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.DoublePrecisionFloat)) {
            return false;
        }
        co.nstant.in.cbor.model.DoublePrecisionFloat other = (co.nstant.in.cbor.model.DoublePrecisionFloat) object;
        return super.equals(object) && this.value == other.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ java.util.Objects.hashCode(java.lang.Double.valueOf(this.value));
    }

    @Override // co.nstant.in.cbor.model.Special
    public java.lang.String toString() {
        return java.lang.String.valueOf(this.value);
    }
}
