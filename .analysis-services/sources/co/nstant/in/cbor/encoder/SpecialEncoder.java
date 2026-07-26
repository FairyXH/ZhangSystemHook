package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class SpecialEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.Special> {
    private final co.nstant.in.cbor.encoder.DoublePrecisionFloatEncoder doublePrecisionFloatEncoder;
    private final co.nstant.in.cbor.encoder.HalfPrecisionFloatEncoder halfPrecisionFloatEncoder;
    private final co.nstant.in.cbor.encoder.SinglePrecisionFloatEncoder singlePrecisionFloatEncoder;

    public SpecialEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
        this.halfPrecisionFloatEncoder = new co.nstant.in.cbor.encoder.HalfPrecisionFloatEncoder(encoder, outputStream);
        this.singlePrecisionFloatEncoder = new co.nstant.in.cbor.encoder.SinglePrecisionFloatEncoder(encoder, outputStream);
        this.doublePrecisionFloatEncoder = new co.nstant.in.cbor.encoder.DoublePrecisionFloatEncoder(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.Special dataItem) throws co.nstant.in.cbor.CborException {
        switch (dataItem.getSpecialType()) {
            case BREAK:
                write(255);
                return;
            case SIMPLE_VALUE:
                co.nstant.in.cbor.model.SimpleValue simpleValue = (co.nstant.in.cbor.model.SimpleValue) dataItem;
                switch (simpleValue.getSimpleValueType()) {
                    case FALSE:
                    case NULL:
                    case TRUE:
                    case UNDEFINED:
                        co.nstant.in.cbor.model.SimpleValueType type = simpleValue.getSimpleValueType();
                        write(type.getValue() | com.android.server.usb.descriptors.UsbDescriptor.CLASSID_WIRELESS);
                        return;
                    case UNALLOCATED:
                        write(simpleValue.getValue() | com.android.server.usb.descriptors.UsbDescriptor.CLASSID_WIRELESS);
                        return;
                    default:
                        return;
                }
            case UNALLOCATED:
                throw new co.nstant.in.cbor.CborException("Unallocated special type");
            case IEEE_754_HALF_PRECISION_FLOAT:
                if (!(dataItem instanceof co.nstant.in.cbor.model.HalfPrecisionFloat)) {
                    throw new co.nstant.in.cbor.CborException("Wrong data item type");
                }
                this.halfPrecisionFloatEncoder.encode((co.nstant.in.cbor.model.HalfPrecisionFloat) dataItem);
                return;
            case IEEE_754_SINGLE_PRECISION_FLOAT:
                if (!(dataItem instanceof co.nstant.in.cbor.model.SinglePrecisionFloat)) {
                    throw new co.nstant.in.cbor.CborException("Wrong data item type");
                }
                this.singlePrecisionFloatEncoder.encode((co.nstant.in.cbor.model.SinglePrecisionFloat) dataItem);
                return;
            case IEEE_754_DOUBLE_PRECISION_FLOAT:
                if (!(dataItem instanceof co.nstant.in.cbor.model.DoublePrecisionFloat)) {
                    throw new co.nstant.in.cbor.CborException("Wrong data item type");
                }
                this.doublePrecisionFloatEncoder.encode((co.nstant.in.cbor.model.DoublePrecisionFloat) dataItem);
                return;
            case SIMPLE_VALUE_NEXT_BYTE:
                if (!(dataItem instanceof co.nstant.in.cbor.model.SimpleValue)) {
                    throw new co.nstant.in.cbor.CborException("Wrong data item type");
                }
                co.nstant.in.cbor.model.SimpleValue simpleValueNextByte = (co.nstant.in.cbor.model.SimpleValue) dataItem;
                write(com.android.internal.util.FrameworkStatsLog.INTEGRITY_RULES_PUSHED);
                write(simpleValueNextByte.getValue());
                return;
            default:
                return;
        }
    }
}
