package org.apache.commons.math.dfp;

/* JADX INFO: loaded from: classes4.dex */
public class DfpDec extends org.apache.commons.math.dfp.Dfp {
    protected DfpDec(org.apache.commons.math.dfp.DfpField factory) {
        super(factory);
    }

    protected DfpDec(org.apache.commons.math.dfp.DfpField factory, byte x) {
        super(factory, x);
    }

    protected DfpDec(org.apache.commons.math.dfp.DfpField factory, int x) {
        super(factory, x);
    }

    protected DfpDec(org.apache.commons.math.dfp.DfpField factory, long x) {
        super(factory, x);
    }

    protected DfpDec(org.apache.commons.math.dfp.DfpField factory, double x) {
        super(factory, x);
        round(0);
    }

    public DfpDec(org.apache.commons.math.dfp.Dfp d) {
        super(d);
        round(0);
    }

    protected DfpDec(org.apache.commons.math.dfp.DfpField factory, java.lang.String s) {
        super(factory, s);
        round(0);
    }

    protected DfpDec(org.apache.commons.math.dfp.DfpField factory, byte sign, byte nans) {
        super(factory, sign, nans);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance() {
        return new org.apache.commons.math.dfp.DfpDec((org.apache.commons.math.dfp.DfpField) getField());
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance(byte x) {
        return new org.apache.commons.math.dfp.DfpDec((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance(int x) {
        return new org.apache.commons.math.dfp.DfpDec((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance(long x) {
        return new org.apache.commons.math.dfp.DfpDec((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance(double x) {
        return new org.apache.commons.math.dfp.DfpDec((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r0v3, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance(org.apache.commons.math.dfp.Dfp d) {
        if (getField().getRadixDigits() != d.getField().getRadixDigits()) {
            getField().setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, "newInstance", d, result);
        }
        return new org.apache.commons.math.dfp.DfpDec(d);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance(java.lang.String s) {
        return new org.apache.commons.math.dfp.DfpDec((org.apache.commons.math.dfp.DfpField) getField(), s);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp newInstance(byte sign, byte nans) {
        return new org.apache.commons.math.dfp.DfpDec(getField(), sign, nans);
    }

    protected int getDecimalDigits() {
        return (getRadixDigits() * 4) - 3;
    }

    /* JADX WARN: Type inference failed for: r14v2, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r2v6, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r2v8, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r2v9, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    protected int round(int in) {
        int n;
        int discarded;
        boolean inc;
        int msb = this.mant[this.mant.length - 1];
        if (msb == 0) {
            return 0;
        }
        int cmaxdigits = this.mant.length * 4;
        int lsbthreshold = 1000;
        while (lsbthreshold > msb) {
            lsbthreshold /= 10;
            cmaxdigits--;
        }
        int digits = getDecimalDigits();
        int lsbshift = cmaxdigits - digits;
        int lsd = lsbshift / 4;
        int lsbthreshold2 = 1;
        for (int i = 0; i < lsbshift % 4; i++) {
            lsbthreshold2 *= 10;
        }
        int lsb = this.mant[lsd];
        if (lsbthreshold2 <= 1 && digits == (this.mant.length * 4) - 3) {
            return super.round(in);
        }
        if (lsbthreshold2 == 1) {
            n = (this.mant[lsd - 1] / 1000) % 10;
            int[] iArr = this.mant;
            int i2 = lsd - 1;
            iArr[i2] = iArr[i2] % 1000;
            discarded = in | this.mant[lsd - 1];
        } else {
            int n2 = lsb * 10;
            n = (n2 / lsbthreshold2) % 10;
            discarded = in | (lsb % (lsbthreshold2 / 10));
        }
        for (int i3 = 0; i3 < lsd; i3++) {
            discarded |= this.mant[i3];
            this.mant[i3] = 0;
        }
        this.mant[lsd] = (lsb / lsbthreshold2) * lsbthreshold2;
        switch (getField().getRoundingMode()) {
            case ROUND_DOWN:
                inc = false;
                break;
            case ROUND_UP:
                inc = n != 0 || discarded != 0;
                break;
            case ROUND_HALF_UP:
                inc = n >= 5;
                break;
            case ROUND_HALF_DOWN:
                inc = n > 5;
                break;
            case ROUND_HALF_EVEN:
                inc = n > 5 || (n == 5 && discarded != 0) || (n == 5 && discarded == 0 && ((lsb / lsbthreshold2) & 1) == 1);
                break;
            case ROUND_HALF_ODD:
                inc = n > 5 || (n == 5 && discarded != 0) || (n == 5 && discarded == 0 && ((lsb / lsbthreshold2) & 1) == 0);
                break;
            case ROUND_CEIL:
                inc = this.sign == 1 && (n != 0 || discarded != 0);
                break;
            default:
                if (this.sign != -1 || (n == 0 && discarded == 0)) {
                    inc = false;
                } else {
                    inc = true;
                }
                break;
        }
        if (inc) {
            int rh = lsbthreshold2;
            for (int i4 = lsd; i4 < this.mant.length; i4++) {
                int r = this.mant[i4] + rh;
                rh = r / 10000;
                this.mant[i4] = r % 10000;
            }
            if (rh != 0) {
                shiftRight();
                this.mant[this.mant.length - 1] = rh;
            }
        }
        if (this.exp < -32767) {
            getField().setIEEEFlagsBits(8);
            return 8;
        }
        if (this.exp > 32768) {
            getField().setIEEEFlagsBits(4);
            return 4;
        }
        if (n != 0 || discarded != 0) {
            getField().setIEEEFlagsBits(16);
            return 16;
        }
        return 0;
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r1v7, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r2v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r4v5, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r4v6, types: [org.apache.commons.math.dfp.DfpField] */
    @Override // org.apache.commons.math.dfp.Dfp
    public org.apache.commons.math.dfp.Dfp nextAfter(org.apache.commons.math.dfp.Dfp x) {
        org.apache.commons.math.dfp.Dfp inc;
        org.apache.commons.math.dfp.Dfp result;
        if (getField().getRadixDigits() != x.getField().getRadixDigits()) {
            getField().setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result2 = newInstance(getZero());
            result2.nans = (byte) 3;
            return dotrap(1, "nextAfter", x, result2);
        }
        boolean up = false;
        if (lessThan(x)) {
            up = true;
        }
        if (equals(x)) {
            return newInstance(x);
        }
        if (lessThan(getZero())) {
            up = !up;
        }
        if (up) {
            org.apache.commons.math.dfp.Dfp inc2 = copysign(power10((log10() - getDecimalDigits()) + 1), this);
            if (equals(getZero())) {
                inc2 = power10K(((-32767) - this.mant.length) - 1);
            }
            if (inc2.equals(getZero())) {
                result = copysign(newInstance(getZero()), this);
            } else {
                result = add(inc2);
            }
        } else {
            org.apache.commons.math.dfp.Dfp inc3 = copysign(power10(log10()), this);
            if (equals(inc3)) {
                inc = inc3.divide(power10(getDecimalDigits()));
            } else {
                inc = inc3.divide(power10(getDecimalDigits() - 1));
            }
            if (equals(getZero())) {
                inc = power10K(((-32767) - this.mant.length) - 1);
            }
            if (inc.equals(getZero())) {
                result = copysign(newInstance(getZero()), this);
            } else {
                result = subtract(inc);
            }
        }
        if (result.classify() == 1 && classify() != 1) {
            getField().setIEEEFlagsBits(16);
            result = dotrap(16, "nextAfter", x, result);
        }
        if (result.equals(getZero()) && !equals(getZero())) {
            getField().setIEEEFlagsBits(16);
            return dotrap(16, "nextAfter", x, result);
        }
        return result;
    }
}
