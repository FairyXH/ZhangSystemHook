package co.nstant.in.cbor;

/* JADX INFO: loaded from: classes.dex */
public class CborDecoder {
    private final co.nstant.in.cbor.decoder.ArrayDecoder arrayDecoder;
    private final co.nstant.in.cbor.decoder.ByteStringDecoder byteStringDecoder;
    private final java.io.InputStream inputStream;
    private final co.nstant.in.cbor.decoder.MapDecoder mapDecoder;
    private final co.nstant.in.cbor.decoder.NegativeIntegerDecoder negativeIntegerDecoder;
    private final co.nstant.in.cbor.decoder.SpecialDecoder specialDecoder;
    private final co.nstant.in.cbor.decoder.TagDecoder tagDecoder;
    private final co.nstant.in.cbor.decoder.UnicodeStringDecoder unicodeStringDecoder;
    private final co.nstant.in.cbor.decoder.UnsignedIntegerDecoder unsignedIntegerDecoder;
    private boolean autoDecodeInfinitiveArrays = true;
    private boolean autoDecodeInfinitiveMaps = true;
    private boolean autoDecodeInfinitiveByteStrings = true;
    private boolean autoDecodeInfinitiveUnicodeStrings = true;
    private boolean autoDecodeRationalNumbers = true;
    private boolean autoDecodeLanguageTaggedStrings = true;
    private boolean rejectDuplicateKeys = false;

    public CborDecoder(java.io.InputStream inputStream) {
        java.util.Objects.requireNonNull(inputStream);
        this.inputStream = inputStream;
        this.unsignedIntegerDecoder = new co.nstant.in.cbor.decoder.UnsignedIntegerDecoder(this, inputStream);
        this.negativeIntegerDecoder = new co.nstant.in.cbor.decoder.NegativeIntegerDecoder(this, inputStream);
        this.byteStringDecoder = new co.nstant.in.cbor.decoder.ByteStringDecoder(this, inputStream);
        this.unicodeStringDecoder = new co.nstant.in.cbor.decoder.UnicodeStringDecoder(this, inputStream);
        this.arrayDecoder = new co.nstant.in.cbor.decoder.ArrayDecoder(this, inputStream);
        this.mapDecoder = new co.nstant.in.cbor.decoder.MapDecoder(this, inputStream);
        this.tagDecoder = new co.nstant.in.cbor.decoder.TagDecoder(this, inputStream);
        this.specialDecoder = new co.nstant.in.cbor.decoder.SpecialDecoder(this, inputStream);
    }

    public static java.util.List<co.nstant.in.cbor.model.DataItem> decode(byte[] bytes) throws co.nstant.in.cbor.CborException {
        return new co.nstant.in.cbor.CborDecoder(new java.io.ByteArrayInputStream(bytes)).decode();
    }

    public java.util.List<co.nstant.in.cbor.model.DataItem> decode() throws co.nstant.in.cbor.CborException {
        java.util.List<co.nstant.in.cbor.model.DataItem> dataItems = new java.util.LinkedList<>();
        while (true) {
            co.nstant.in.cbor.model.DataItem dataItem = decodeNext();
            if (dataItem != null) {
                dataItems.add(dataItem);
            } else {
                return dataItems;
            }
        }
    }

    public void decode(co.nstant.in.cbor.DataItemListener dataItemListener) throws co.nstant.in.cbor.CborException {
        java.util.Objects.requireNonNull(dataItemListener);
        co.nstant.in.cbor.model.DataItem dataItem = decodeNext();
        while (dataItem != null) {
            dataItemListener.onDataItem(dataItem);
            dataItem = decodeNext();
        }
    }

    public co.nstant.in.cbor.model.DataItem decodeNext() throws co.nstant.in.cbor.CborException {
        try {
            int symbol = this.inputStream.read();
            if (symbol == -1) {
                return null;
            }
            switch (co.nstant.in.cbor.model.MajorType.ofByte(symbol)) {
                case ARRAY:
                    return this.arrayDecoder.decode(symbol);
                case BYTE_STRING:
                    return this.byteStringDecoder.decode(symbol);
                case MAP:
                    return this.mapDecoder.decode(symbol);
                case NEGATIVE_INTEGER:
                    return this.negativeIntegerDecoder.decode(symbol);
                case UNICODE_STRING:
                    return this.unicodeStringDecoder.decode(symbol);
                case UNSIGNED_INTEGER:
                    return this.unsignedIntegerDecoder.decode(symbol);
                case SPECIAL:
                    return this.specialDecoder.decode(symbol);
                case TAG:
                    co.nstant.in.cbor.model.Tag tag = this.tagDecoder.decode(symbol);
                    co.nstant.in.cbor.model.DataItem next = decodeNext();
                    if (next == null) {
                        throw new co.nstant.in.cbor.CborException("Unexpected end of stream: tag without following data item.");
                    }
                    if (this.autoDecodeRationalNumbers && tag.getValue() == 30) {
                        return decodeRationalNumber(next);
                    }
                    if (this.autoDecodeLanguageTaggedStrings && tag.getValue() == 38) {
                        return decodeLanguageTaggedString(next);
                    }
                    co.nstant.in.cbor.model.DataItem itemToTag = next;
                    while (itemToTag.hasTag()) {
                        itemToTag = itemToTag.getTag();
                    }
                    itemToTag.setTag(tag);
                    return next;
                default:
                    throw new co.nstant.in.cbor.CborException("Not implemented major type " + symbol);
            }
        } catch (java.io.IOException ioException) {
            throw new co.nstant.in.cbor.CborException(ioException);
        }
    }

    private co.nstant.in.cbor.model.DataItem decodeLanguageTaggedString(co.nstant.in.cbor.model.DataItem dataItem) throws co.nstant.in.cbor.CborException {
        if (!(dataItem instanceof co.nstant.in.cbor.model.Array)) {
            throw new co.nstant.in.cbor.CborException("Error decoding LanguageTaggedString: not an array");
        }
        co.nstant.in.cbor.model.Array array = (co.nstant.in.cbor.model.Array) dataItem;
        if (array.getDataItems().size() != 2) {
            throw new co.nstant.in.cbor.CborException("Error decoding LanguageTaggedString: array size is not 2");
        }
        co.nstant.in.cbor.model.DataItem languageDataItem = array.getDataItems().get(0);
        if (!(languageDataItem instanceof co.nstant.in.cbor.model.UnicodeString)) {
            throw new co.nstant.in.cbor.CborException("Error decoding LanguageTaggedString: first data item is not an UnicodeString");
        }
        co.nstant.in.cbor.model.DataItem stringDataItem = array.getDataItems().get(1);
        if (!(stringDataItem instanceof co.nstant.in.cbor.model.UnicodeString)) {
            throw new co.nstant.in.cbor.CborException("Error decoding LanguageTaggedString: second data item is not an UnicodeString");
        }
        co.nstant.in.cbor.model.UnicodeString language = (co.nstant.in.cbor.model.UnicodeString) languageDataItem;
        co.nstant.in.cbor.model.UnicodeString string = (co.nstant.in.cbor.model.UnicodeString) stringDataItem;
        return new co.nstant.in.cbor.model.LanguageTaggedString(language, string);
    }

    private co.nstant.in.cbor.model.DataItem decodeRationalNumber(co.nstant.in.cbor.model.DataItem dataItem) throws co.nstant.in.cbor.CborException {
        if (!(dataItem instanceof co.nstant.in.cbor.model.Array)) {
            throw new co.nstant.in.cbor.CborException("Error decoding RationalNumber: not an array");
        }
        co.nstant.in.cbor.model.Array array = (co.nstant.in.cbor.model.Array) dataItem;
        if (array.getDataItems().size() != 2) {
            throw new co.nstant.in.cbor.CborException("Error decoding RationalNumber: array size is not 2");
        }
        co.nstant.in.cbor.model.DataItem numeratorDataItem = array.getDataItems().get(0);
        if (!(numeratorDataItem instanceof co.nstant.in.cbor.model.Number)) {
            throw new co.nstant.in.cbor.CborException("Error decoding RationalNumber: first data item is not a number");
        }
        co.nstant.in.cbor.model.DataItem denominatorDataItem = array.getDataItems().get(1);
        if (!(denominatorDataItem instanceof co.nstant.in.cbor.model.Number)) {
            throw new co.nstant.in.cbor.CborException("Error decoding RationalNumber: second data item is not a number");
        }
        co.nstant.in.cbor.model.Number numerator = (co.nstant.in.cbor.model.Number) numeratorDataItem;
        co.nstant.in.cbor.model.Number denominator = (co.nstant.in.cbor.model.Number) denominatorDataItem;
        return new co.nstant.in.cbor.model.RationalNumber(numerator, denominator);
    }

    public boolean isAutoDecodeInfinitiveArrays() {
        return this.autoDecodeInfinitiveArrays;
    }

    public void setAutoDecodeInfinitiveArrays(boolean autoDecodeInfinitiveArrays) {
        this.autoDecodeInfinitiveArrays = autoDecodeInfinitiveArrays;
    }

    public boolean isAutoDecodeInfinitiveMaps() {
        return this.autoDecodeInfinitiveMaps;
    }

    public void setAutoDecodeInfinitiveMaps(boolean autoDecodeInfinitiveMaps) {
        this.autoDecodeInfinitiveMaps = autoDecodeInfinitiveMaps;
    }

    public boolean isAutoDecodeInfinitiveByteStrings() {
        return this.autoDecodeInfinitiveByteStrings;
    }

    public void setAutoDecodeInfinitiveByteStrings(boolean autoDecodeInfinitiveByteStrings) {
        this.autoDecodeInfinitiveByteStrings = autoDecodeInfinitiveByteStrings;
    }

    public boolean isAutoDecodeInfinitiveUnicodeStrings() {
        return this.autoDecodeInfinitiveUnicodeStrings;
    }

    public void setAutoDecodeInfinitiveUnicodeStrings(boolean autoDecodeInfinitiveUnicodeStrings) {
        this.autoDecodeInfinitiveUnicodeStrings = autoDecodeInfinitiveUnicodeStrings;
    }

    public boolean isAutoDecodeRationalNumbers() {
        return this.autoDecodeRationalNumbers;
    }

    public void setAutoDecodeRationalNumbers(boolean autoDecodeRationalNumbers) {
        this.autoDecodeRationalNumbers = autoDecodeRationalNumbers;
    }

    public boolean isAutoDecodeLanguageTaggedStrings() {
        return this.autoDecodeLanguageTaggedStrings;
    }

    public void setAutoDecodeLanguageTaggedStrings(boolean autoDecodeLanguageTaggedStrings) {
        this.autoDecodeLanguageTaggedStrings = autoDecodeLanguageTaggedStrings;
    }

    public boolean isRejectDuplicateKeys() {
        return this.rejectDuplicateKeys;
    }

    public void setRejectDuplicateKeys(boolean rejectDuplicateKeys) {
        this.rejectDuplicateKeys = rejectDuplicateKeys;
    }
}
