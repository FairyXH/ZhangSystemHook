package co.nstant.in.cbor;

/* JADX INFO: loaded from: classes.dex */
public class CborEncoder {
    private final co.nstant.in.cbor.encoder.ArrayEncoder arrayEncoder;
    private final co.nstant.in.cbor.encoder.ByteStringEncoder byteStringEncoder;
    private final co.nstant.in.cbor.encoder.MapEncoder mapEncoder;
    private final co.nstant.in.cbor.encoder.NegativeIntegerEncoder negativeIntegerEncoder;
    private final co.nstant.in.cbor.encoder.SpecialEncoder specialEncoder;
    private final co.nstant.in.cbor.encoder.TagEncoder tagEncoder;
    private final co.nstant.in.cbor.encoder.UnicodeStringEncoder unicodeStringEncoder;
    private final co.nstant.in.cbor.encoder.UnsignedIntegerEncoder unsignedIntegerEncoder;

    public CborEncoder(java.io.OutputStream outputStream) {
        java.util.Objects.requireNonNull(outputStream);
        this.unsignedIntegerEncoder = new co.nstant.in.cbor.encoder.UnsignedIntegerEncoder(this, outputStream);
        this.negativeIntegerEncoder = new co.nstant.in.cbor.encoder.NegativeIntegerEncoder(this, outputStream);
        this.byteStringEncoder = new co.nstant.in.cbor.encoder.ByteStringEncoder(this, outputStream);
        this.unicodeStringEncoder = new co.nstant.in.cbor.encoder.UnicodeStringEncoder(this, outputStream);
        this.arrayEncoder = new co.nstant.in.cbor.encoder.ArrayEncoder(this, outputStream);
        this.mapEncoder = new co.nstant.in.cbor.encoder.MapEncoder(this, outputStream);
        this.tagEncoder = new co.nstant.in.cbor.encoder.TagEncoder(this, outputStream);
        this.specialEncoder = new co.nstant.in.cbor.encoder.SpecialEncoder(this, outputStream);
    }

    public void encode(java.util.List<co.nstant.in.cbor.model.DataItem> dataItems) throws co.nstant.in.cbor.CborException {
        for (co.nstant.in.cbor.model.DataItem dataItem : dataItems) {
            encode(dataItem);
        }
    }

    public void encode(co.nstant.in.cbor.model.DataItem dataItem) throws co.nstant.in.cbor.CborException {
        if (dataItem == null) {
            dataItem = co.nstant.in.cbor.model.SimpleValue.NULL;
        }
        if (dataItem.hasTag()) {
            co.nstant.in.cbor.model.Tag tagDi = dataItem.getTag();
            this.tagEncoder.encode(tagDi);
        }
        switch (dataItem.getMajorType()) {
            case UNSIGNED_INTEGER:
                this.unsignedIntegerEncoder.encode((co.nstant.in.cbor.model.UnsignedInteger) dataItem);
                return;
            case NEGATIVE_INTEGER:
                this.negativeIntegerEncoder.encode((co.nstant.in.cbor.model.NegativeInteger) dataItem);
                return;
            case BYTE_STRING:
                this.byteStringEncoder.encode((co.nstant.in.cbor.model.ByteString) dataItem);
                return;
            case UNICODE_STRING:
                this.unicodeStringEncoder.encode((co.nstant.in.cbor.model.UnicodeString) dataItem);
                return;
            case ARRAY:
                this.arrayEncoder.encode((co.nstant.in.cbor.model.Array) dataItem);
                return;
            case MAP:
                this.mapEncoder.encode((co.nstant.in.cbor.model.Map) dataItem);
                return;
            case SPECIAL:
                this.specialEncoder.encode((co.nstant.in.cbor.model.Special) dataItem);
                return;
            case TAG:
                this.tagEncoder.encode((co.nstant.in.cbor.model.Tag) dataItem);
                return;
            default:
                throw new co.nstant.in.cbor.CborException("Unknown major type");
        }
    }
}
