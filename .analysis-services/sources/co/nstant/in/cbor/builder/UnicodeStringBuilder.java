package co.nstant.in.cbor.builder;

/* JADX INFO: loaded from: classes.dex */
public class UnicodeStringBuilder<T extends co.nstant.in.cbor.builder.AbstractBuilder<?>> extends co.nstant.in.cbor.builder.AbstractBuilder<T> {
    public UnicodeStringBuilder(T parent) {
        super(parent);
    }

    public co.nstant.in.cbor.builder.UnicodeStringBuilder<T> add(java.lang.String string) {
        getParent().addChunk(convert(string));
        return this;
    }

    public T end() {
        getParent().addChunk(co.nstant.in.cbor.model.SimpleValue.BREAK);
        return getParent();
    }
}
