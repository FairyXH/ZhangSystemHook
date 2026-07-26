package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class LanguageTaggedString extends co.nstant.in.cbor.model.Array {
    public LanguageTaggedString(java.lang.String language, java.lang.String string) {
        this(new co.nstant.in.cbor.model.UnicodeString(language), new co.nstant.in.cbor.model.UnicodeString(string));
    }

    public LanguageTaggedString(co.nstant.in.cbor.model.UnicodeString language, co.nstant.in.cbor.model.UnicodeString string) {
        setTag(38);
        add((co.nstant.in.cbor.model.DataItem) java.util.Objects.requireNonNull(language));
        add((co.nstant.in.cbor.model.DataItem) java.util.Objects.requireNonNull(string));
    }

    public co.nstant.in.cbor.model.UnicodeString getLanguage() {
        return (co.nstant.in.cbor.model.UnicodeString) getDataItems().get(0);
    }

    public co.nstant.in.cbor.model.UnicodeString getString() {
        return (co.nstant.in.cbor.model.UnicodeString) getDataItems().get(1);
    }
}
