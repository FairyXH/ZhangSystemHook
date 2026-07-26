package co.nstant.in.cbor.builder;

/* JADX INFO: loaded from: classes.dex */
public class ArrayBuilder<T extends co.nstant.in.cbor.builder.AbstractBuilder<?>> extends co.nstant.in.cbor.builder.AbstractBuilder<T> {
    private final co.nstant.in.cbor.model.Array array;

    public ArrayBuilder(T parent, co.nstant.in.cbor.model.Array array) {
        super(parent);
        this.array = array;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<T> add(co.nstant.in.cbor.model.DataItem dataItem) {
        this.array.add(dataItem);
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<T> add(long value) {
        add(convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<T> add(boolean value) {
        add(convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<T> add(float value) {
        add(convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<T> add(double value) {
        add(convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<T> add(byte[] bytes) {
        add(convert(bytes));
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<T> add(java.lang.String string) {
        add(convert(string));
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.ArrayBuilder<T>> addArray() {
        co.nstant.in.cbor.model.Array nestedArray = new co.nstant.in.cbor.model.Array();
        add(nestedArray);
        return new co.nstant.in.cbor.builder.ArrayBuilder<>(this, nestedArray);
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.ArrayBuilder<T>> startArray() {
        co.nstant.in.cbor.model.Array nestedArray = new co.nstant.in.cbor.model.Array();
        nestedArray.setChunked(true);
        add(nestedArray);
        return new co.nstant.in.cbor.builder.ArrayBuilder<>(this, nestedArray);
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.ArrayBuilder<T>> addMap() {
        co.nstant.in.cbor.model.Map nestedMap = new co.nstant.in.cbor.model.Map();
        add(nestedMap);
        return new co.nstant.in.cbor.builder.MapBuilder<>(this, nestedMap);
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.ArrayBuilder<T>> startMap() {
        co.nstant.in.cbor.model.Map nestedMap = new co.nstant.in.cbor.model.Map();
        nestedMap.setChunked(true);
        add(nestedMap);
        return new co.nstant.in.cbor.builder.MapBuilder<>(this, nestedMap);
    }

    public T end() {
        if (this.array.isChunked()) {
            add(co.nstant.in.cbor.model.SimpleValue.BREAK);
        }
        return getParent();
    }
}
