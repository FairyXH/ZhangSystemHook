package co.nstant.in.cbor.builder;

/* JADX INFO: loaded from: classes.dex */
public class MapBuilder<T extends co.nstant.in.cbor.builder.AbstractBuilder<?>> extends co.nstant.in.cbor.builder.AbstractBuilder<T> {
    private final co.nstant.in.cbor.model.Map map;

    public MapBuilder(T parent, co.nstant.in.cbor.model.Map map) {
        super(parent);
        this.map = map;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(co.nstant.in.cbor.model.DataItem key, co.nstant.in.cbor.model.DataItem value) {
        this.map.put(key, value);
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(long key, long value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(long key, boolean value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(long key, float value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(long key, double value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(long key, byte[] value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(long key, java.lang.String value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(java.lang.String key, long value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(java.lang.String key, boolean value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(java.lang.String key, float value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(java.lang.String key, double value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(java.lang.String key, byte[] value) {
        this.map.put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.MapBuilder<T> put(java.lang.String key, java.lang.String value) {
        put(convert(key), convert(value));
        return this;
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> putArray(co.nstant.in.cbor.model.DataItem key) {
        co.nstant.in.cbor.model.Array array = new co.nstant.in.cbor.model.Array();
        put(key, array);
        return new co.nstant.in.cbor.builder.ArrayBuilder<>(this, array);
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> putArray(long key) {
        co.nstant.in.cbor.model.Array array = new co.nstant.in.cbor.model.Array();
        put(convert(key), array);
        return new co.nstant.in.cbor.builder.ArrayBuilder<>(this, array);
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> putArray(java.lang.String key) {
        co.nstant.in.cbor.model.Array array = new co.nstant.in.cbor.model.Array();
        put(convert(key), array);
        return new co.nstant.in.cbor.builder.ArrayBuilder<>(this, array);
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> startArray(co.nstant.in.cbor.model.DataItem key) {
        co.nstant.in.cbor.model.Array array = new co.nstant.in.cbor.model.Array();
        array.setChunked(true);
        put(key, array);
        return new co.nstant.in.cbor.builder.ArrayBuilder<>(this, array);
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> startArray(long key) {
        return startArray(convert(key));
    }

    public co.nstant.in.cbor.builder.ArrayBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> startArray(java.lang.String key) {
        co.nstant.in.cbor.model.Array array = new co.nstant.in.cbor.model.Array();
        array.setChunked(true);
        put(convert(key), array);
        return new co.nstant.in.cbor.builder.ArrayBuilder<>(this, array);
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> putMap(co.nstant.in.cbor.model.DataItem key) {
        co.nstant.in.cbor.model.Map nestedMap = new co.nstant.in.cbor.model.Map();
        put(key, nestedMap);
        return new co.nstant.in.cbor.builder.MapBuilder<>(this, nestedMap);
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> putMap(long key) {
        co.nstant.in.cbor.model.Map nestedMap = new co.nstant.in.cbor.model.Map();
        put(convert(key), nestedMap);
        return new co.nstant.in.cbor.builder.MapBuilder<>(this, nestedMap);
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> putMap(java.lang.String key) {
        co.nstant.in.cbor.model.Map nestedMap = new co.nstant.in.cbor.model.Map();
        put(convert(key), nestedMap);
        return new co.nstant.in.cbor.builder.MapBuilder<>(this, nestedMap);
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> startMap(co.nstant.in.cbor.model.DataItem key) {
        co.nstant.in.cbor.model.Map nestedMap = new co.nstant.in.cbor.model.Map();
        nestedMap.setChunked(true);
        put(key, nestedMap);
        return new co.nstant.in.cbor.builder.MapBuilder<>(this, nestedMap);
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> startMap(long key) {
        return startMap(convert(key));
    }

    public co.nstant.in.cbor.builder.MapBuilder<co.nstant.in.cbor.builder.MapBuilder<T>> startMap(java.lang.String key) {
        return startMap(convert(key));
    }

    public T end() {
        return getParent();
    }
}
