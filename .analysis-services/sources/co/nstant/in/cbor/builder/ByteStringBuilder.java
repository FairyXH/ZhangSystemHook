package co.nstant.in.cbor.builder;

/* JADX INFO: loaded from: classes.dex */
public class ByteStringBuilder<T extends co.nstant.in.cbor.builder.AbstractBuilder<?>> extends co.nstant.in.cbor.builder.AbstractBuilder<T> {
    public ByteStringBuilder(T parent) {
        super(parent);
    }

    public co.nstant.in.cbor.builder.ByteStringBuilder<T> add(byte[] bytes) {
        getParent().addChunk(convert(bytes));
        return this;
    }

    public T end() {
        getParent().addChunk(co.nstant.in.cbor.model.SimpleValue.BREAK);
        return getParent();
    }
}
