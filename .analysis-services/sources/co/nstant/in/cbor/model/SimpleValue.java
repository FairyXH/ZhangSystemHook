package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class SimpleValue extends co.nstant.in.cbor.model.Special {
    private final co.nstant.in.cbor.model.SimpleValueType simpleValueType;
    private final int value;
    public static final co.nstant.in.cbor.model.SimpleValue FALSE = new co.nstant.in.cbor.model.SimpleValue(co.nstant.in.cbor.model.SimpleValueType.FALSE);
    public static final co.nstant.in.cbor.model.SimpleValue TRUE = new co.nstant.in.cbor.model.SimpleValue(co.nstant.in.cbor.model.SimpleValueType.TRUE);
    public static final co.nstant.in.cbor.model.SimpleValue NULL = new co.nstant.in.cbor.model.SimpleValue(co.nstant.in.cbor.model.SimpleValueType.NULL);
    public static final co.nstant.in.cbor.model.SimpleValue UNDEFINED = new co.nstant.in.cbor.model.SimpleValue(co.nstant.in.cbor.model.SimpleValueType.UNDEFINED);

    public SimpleValue(co.nstant.in.cbor.model.SimpleValueType simpleValueType) {
        super(co.nstant.in.cbor.model.SpecialType.SIMPLE_VALUE);
        this.value = simpleValueType.getValue();
        this.simpleValueType = simpleValueType;
    }

    public SimpleValue(int value) {
        super(value <= 23 ? co.nstant.in.cbor.model.SpecialType.SIMPLE_VALUE : co.nstant.in.cbor.model.SpecialType.SIMPLE_VALUE_NEXT_BYTE);
        this.value = value;
        this.simpleValueType = co.nstant.in.cbor.model.SimpleValueType.ofByte(value);
    }

    public co.nstant.in.cbor.model.SimpleValueType getSimpleValueType() {
        return this.simpleValueType;
    }

    public int getValue() {
        return this.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public boolean equals(java.lang.Object object) {
        if (!(object instanceof co.nstant.in.cbor.model.SimpleValue)) {
            return false;
        }
        co.nstant.in.cbor.model.SimpleValue other = (co.nstant.in.cbor.model.SimpleValue) object;
        return super.equals(object) && this.value == other.value;
    }

    @Override // co.nstant.in.cbor.model.Special, co.nstant.in.cbor.model.DataItem
    public int hashCode() {
        return super.hashCode() ^ java.util.Objects.hashCode(java.lang.Integer.valueOf(this.value));
    }

    @Override // co.nstant.in.cbor.model.Special
    public java.lang.String toString() {
        return this.simpleValueType.toString();
    }
}
