package co.nstant.in.cbor.model;

/* JADX INFO: loaded from: classes.dex */
public class RationalNumber extends co.nstant.in.cbor.model.Array {
    public RationalNumber(co.nstant.in.cbor.model.Number numerator, co.nstant.in.cbor.model.Number denominator) throws co.nstant.in.cbor.CborException {
        setTag(30);
        if (numerator == null) {
            throw new co.nstant.in.cbor.CborException("Numerator is null");
        }
        if (denominator == null) {
            throw new co.nstant.in.cbor.CborException("Denominator is null");
        }
        if (denominator.getValue().equals(java.math.BigInteger.ZERO)) {
            throw new co.nstant.in.cbor.CborException("Denominator is zero");
        }
        add(numerator);
        add(denominator);
    }

    public co.nstant.in.cbor.model.Number getNumerator() {
        return (co.nstant.in.cbor.model.Number) getDataItems().get(0);
    }

    public co.nstant.in.cbor.model.Number getDenominator() {
        return (co.nstant.in.cbor.model.Number) getDataItems().get(1);
    }
}
