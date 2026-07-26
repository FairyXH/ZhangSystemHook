package co.nstant.in.cbor.decoder;

/* JADX INFO: loaded from: classes.dex */
public class UnsignedIntegerDecoder extends co.nstant.in.cbor.decoder.AbstractDecoder<co.nstant.in.cbor.model.UnsignedInteger> {
    public UnsignedIntegerDecoder(co.nstant.in.cbor.CborDecoder decoder, java.io.InputStream inputStream) {
        super(decoder, inputStream);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // co.nstant.in.cbor.decoder.AbstractDecoder
    public co.nstant.in.cbor.model.UnsignedInteger decode(int initialByte) throws co.nstant.in.cbor.CborException {
        return new co.nstant.in.cbor.model.UnsignedInteger(getLengthAsBigInteger(initialByte));
    }
}
