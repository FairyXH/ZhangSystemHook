package org.apache.commons.math.dfp;

/* JADX INFO: loaded from: classes4.dex */
public class DfpField implements org.apache.commons.math.Field<org.apache.commons.math.dfp.Dfp> {
    public static final int FLAG_DIV_ZERO = 2;
    public static final int FLAG_INEXACT = 16;
    public static final int FLAG_INVALID = 1;
    public static final int FLAG_OVERFLOW = 4;
    public static final int FLAG_UNDERFLOW = 8;
    private static java.lang.String eString;
    private static java.lang.String ln10String;
    private static java.lang.String ln2String;
    private static java.lang.String ln5String;
    private static java.lang.String piString;
    private static java.lang.String sqr2ReciprocalString;
    private static java.lang.String sqr2String;
    private static java.lang.String sqr3ReciprocalString;
    private static java.lang.String sqr3String;
    private final org.apache.commons.math.dfp.Dfp e;
    private final org.apache.commons.math.dfp.Dfp[] eSplit;
    private int ieeeFlags;
    private final org.apache.commons.math.dfp.Dfp ln10;
    private final org.apache.commons.math.dfp.Dfp ln2;
    private final org.apache.commons.math.dfp.Dfp[] ln2Split;
    private final org.apache.commons.math.dfp.Dfp ln5;
    private final org.apache.commons.math.dfp.Dfp[] ln5Split;
    private final org.apache.commons.math.dfp.Dfp one;
    private final org.apache.commons.math.dfp.Dfp pi;
    private final org.apache.commons.math.dfp.Dfp[] piSplit;
    private org.apache.commons.math.dfp.DfpField.RoundingMode rMode;
    private final int radixDigits;
    private final org.apache.commons.math.dfp.Dfp sqr2;
    private final org.apache.commons.math.dfp.Dfp sqr2Reciprocal;
    private final org.apache.commons.math.dfp.Dfp[] sqr2Split;
    private final org.apache.commons.math.dfp.Dfp sqr3;
    private final org.apache.commons.math.dfp.Dfp sqr3Reciprocal;
    private final org.apache.commons.math.dfp.Dfp two;
    private final org.apache.commons.math.dfp.Dfp zero;

    public enum RoundingMode {
        ROUND_DOWN,
        ROUND_UP,
        ROUND_HALF_UP,
        ROUND_HALF_DOWN,
        ROUND_HALF_EVEN,
        ROUND_HALF_ODD,
        ROUND_CEIL,
        ROUND_FLOOR
    }

    public DfpField(int decimalDigits) {
        this(decimalDigits, true);
    }

    private DfpField(int decimalDigits, boolean computeConstants) {
        this.radixDigits = decimalDigits >= 13 ? (decimalDigits + 3) / 4 : 4;
        this.rMode = org.apache.commons.math.dfp.DfpField.RoundingMode.ROUND_HALF_EVEN;
        this.ieeeFlags = 0;
        this.zero = new org.apache.commons.math.dfp.Dfp(this, 0);
        this.one = new org.apache.commons.math.dfp.Dfp(this, 1);
        this.two = new org.apache.commons.math.dfp.Dfp(this, 2);
        if (computeConstants) {
            synchronized (org.apache.commons.math.dfp.DfpField.class) {
                computeStringConstants(decimalDigits < 67 ? 200 : decimalDigits * 3);
                this.sqr2 = new org.apache.commons.math.dfp.Dfp(this, sqr2String);
                this.sqr2Split = split(sqr2String);
                this.sqr2Reciprocal = new org.apache.commons.math.dfp.Dfp(this, sqr2ReciprocalString);
                this.sqr3 = new org.apache.commons.math.dfp.Dfp(this, sqr3String);
                this.sqr3Reciprocal = new org.apache.commons.math.dfp.Dfp(this, sqr3ReciprocalString);
                this.pi = new org.apache.commons.math.dfp.Dfp(this, piString);
                this.piSplit = split(piString);
                this.e = new org.apache.commons.math.dfp.Dfp(this, eString);
                this.eSplit = split(eString);
                this.ln2 = new org.apache.commons.math.dfp.Dfp(this, ln2String);
                this.ln2Split = split(ln2String);
                this.ln5 = new org.apache.commons.math.dfp.Dfp(this, ln5String);
                this.ln5Split = split(ln5String);
                this.ln10 = new org.apache.commons.math.dfp.Dfp(this, ln10String);
            }
            return;
        }
        this.sqr2 = null;
        this.sqr2Split = null;
        this.sqr2Reciprocal = null;
        this.sqr3 = null;
        this.sqr3Reciprocal = null;
        this.pi = null;
        this.piSplit = null;
        this.e = null;
        this.eSplit = null;
        this.ln2 = null;
        this.ln2Split = null;
        this.ln5 = null;
        this.ln5Split = null;
        this.ln10 = null;
    }

    public int getRadixDigits() {
        return this.radixDigits;
    }

    public void setRoundingMode(org.apache.commons.math.dfp.DfpField.RoundingMode mode) {
        this.rMode = mode;
    }

    public org.apache.commons.math.dfp.DfpField.RoundingMode getRoundingMode() {
        return this.rMode;
    }

    public int getIEEEFlags() {
        return this.ieeeFlags;
    }

    public void clearIEEEFlags() {
        this.ieeeFlags = 0;
    }

    public void setIEEEFlags(int flags) {
        this.ieeeFlags = flags & 31;
    }

    public void setIEEEFlagsBits(int bits) {
        this.ieeeFlags |= bits & 31;
    }

    public org.apache.commons.math.dfp.Dfp newDfp() {
        return new org.apache.commons.math.dfp.Dfp(this);
    }

    public org.apache.commons.math.dfp.Dfp newDfp(byte x) {
        return new org.apache.commons.math.dfp.Dfp(this, x);
    }

    public org.apache.commons.math.dfp.Dfp newDfp(int x) {
        return new org.apache.commons.math.dfp.Dfp(this, x);
    }

    public org.apache.commons.math.dfp.Dfp newDfp(long x) {
        return new org.apache.commons.math.dfp.Dfp(this, x);
    }

    public org.apache.commons.math.dfp.Dfp newDfp(double x) {
        return new org.apache.commons.math.dfp.Dfp(this, x);
    }

    public org.apache.commons.math.dfp.Dfp newDfp(org.apache.commons.math.dfp.Dfp d) {
        return new org.apache.commons.math.dfp.Dfp(d);
    }

    public org.apache.commons.math.dfp.Dfp newDfp(java.lang.String s) {
        return new org.apache.commons.math.dfp.Dfp(this, s);
    }

    public org.apache.commons.math.dfp.Dfp newDfp(byte sign, byte nans) {
        return new org.apache.commons.math.dfp.Dfp(this, sign, nans);
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.dfp.Dfp getZero() {
        return this.zero;
    }

    @Override // org.apache.commons.math.Field
    public org.apache.commons.math.dfp.Dfp getOne() {
        return this.one;
    }

    public org.apache.commons.math.dfp.Dfp getTwo() {
        return this.two;
    }

    public org.apache.commons.math.dfp.Dfp getSqr2() {
        return this.sqr2;
    }

    public org.apache.commons.math.dfp.Dfp[] getSqr2Split() {
        return (org.apache.commons.math.dfp.Dfp[]) this.sqr2Split.clone();
    }

    public org.apache.commons.math.dfp.Dfp getSqr2Reciprocal() {
        return this.sqr2Reciprocal;
    }

    public org.apache.commons.math.dfp.Dfp getSqr3() {
        return this.sqr3;
    }

    public org.apache.commons.math.dfp.Dfp getSqr3Reciprocal() {
        return this.sqr3Reciprocal;
    }

    public org.apache.commons.math.dfp.Dfp getPi() {
        return this.pi;
    }

    public org.apache.commons.math.dfp.Dfp[] getPiSplit() {
        return (org.apache.commons.math.dfp.Dfp[]) this.piSplit.clone();
    }

    public org.apache.commons.math.dfp.Dfp getE() {
        return this.e;
    }

    public org.apache.commons.math.dfp.Dfp[] getESplit() {
        return (org.apache.commons.math.dfp.Dfp[]) this.eSplit.clone();
    }

    public org.apache.commons.math.dfp.Dfp getLn2() {
        return this.ln2;
    }

    public org.apache.commons.math.dfp.Dfp[] getLn2Split() {
        return (org.apache.commons.math.dfp.Dfp[]) this.ln2Split.clone();
    }

    public org.apache.commons.math.dfp.Dfp getLn5() {
        return this.ln5;
    }

    public org.apache.commons.math.dfp.Dfp[] getLn5Split() {
        return (org.apache.commons.math.dfp.Dfp[]) this.ln5Split.clone();
    }

    public org.apache.commons.math.dfp.Dfp getLn10() {
        return this.ln10;
    }

    private org.apache.commons.math.dfp.Dfp[] split(java.lang.String a) {
        org.apache.commons.math.dfp.Dfp[] result = new org.apache.commons.math.dfp.Dfp[2];
        boolean leading = true;
        int sp = 0;
        int sig = 0;
        char[] buf = new char[a.length()];
        int i = 0;
        while (true) {
            if (i >= buf.length) {
                break;
            }
            buf[i] = a.charAt(i);
            if (buf[i] >= '1' && buf[i] <= '9') {
                leading = false;
            }
            if (buf[i] == '.') {
                sig += (400 - sig) % 4;
                leading = false;
            }
            if (sig == (this.radixDigits / 2) * 4) {
                sp = i;
                break;
            }
            if (buf[i] >= '0' && buf[i] <= '9' && !leading) {
                sig++;
            }
            i++;
        }
        result[0] = new org.apache.commons.math.dfp.Dfp(this, new java.lang.String(buf, 0, sp));
        for (int i2 = 0; i2 < buf.length; i2++) {
            buf[i2] = a.charAt(i2);
            if (buf[i2] >= '0' && buf[i2] <= '9' && i2 < sp) {
                buf[i2] = '0';
            }
        }
        result[1] = new org.apache.commons.math.dfp.Dfp(this, new java.lang.String(buf));
        return result;
    }

    private static void computeStringConstants(int highPrecisionDecimalDigits) {
        if (sqr2String == null || sqr2String.length() < highPrecisionDecimalDigits - 3) {
            org.apache.commons.math.dfp.DfpField highPrecisionField = new org.apache.commons.math.dfp.DfpField(highPrecisionDecimalDigits, false);
            org.apache.commons.math.dfp.Dfp highPrecisionOne = new org.apache.commons.math.dfp.Dfp(highPrecisionField, 1);
            org.apache.commons.math.dfp.Dfp highPrecisionTwo = new org.apache.commons.math.dfp.Dfp(highPrecisionField, 2);
            org.apache.commons.math.dfp.Dfp highPrecisionThree = new org.apache.commons.math.dfp.Dfp(highPrecisionField, 3);
            org.apache.commons.math.dfp.Dfp highPrecisionSqr2 = highPrecisionTwo.sqrt();
            sqr2String = highPrecisionSqr2.toString();
            sqr2ReciprocalString = highPrecisionOne.divide(highPrecisionSqr2).toString();
            org.apache.commons.math.dfp.Dfp highPrecisionSqr3 = highPrecisionThree.sqrt();
            sqr3String = highPrecisionSqr3.toString();
            sqr3ReciprocalString = highPrecisionOne.divide(highPrecisionSqr3).toString();
            piString = computePi(highPrecisionOne, highPrecisionTwo, highPrecisionThree).toString();
            eString = computeExp(highPrecisionOne, highPrecisionOne).toString();
            ln2String = computeLn(highPrecisionTwo, highPrecisionOne, highPrecisionTwo).toString();
            ln5String = computeLn(new org.apache.commons.math.dfp.Dfp(highPrecisionField, 5), highPrecisionOne, highPrecisionTwo).toString();
            ln10String = computeLn(new org.apache.commons.math.dfp.Dfp(highPrecisionField, 10), highPrecisionOne, highPrecisionTwo).toString();
        }
    }

    private static org.apache.commons.math.dfp.Dfp computePi(org.apache.commons.math.dfp.Dfp dfp, org.apache.commons.math.dfp.Dfp two, org.apache.commons.math.dfp.Dfp three) {
        org.apache.commons.math.dfp.Dfp sqrt2 = two.sqrt();
        org.apache.commons.math.dfp.Dfp yk = sqrt2.subtract(dfp);
        org.apache.commons.math.dfp.Dfp four = two.add(two);
        org.apache.commons.math.dfp.Dfp two2kp3 = two;
        org.apache.commons.math.dfp.Dfp ak = two.multiply(three.subtract(two.multiply(sqrt2)));
        int i = 1;
        while (i < 20) {
            java.lang.Object ykM1 = yk;
            org.apache.commons.math.dfp.Dfp y2 = yk.multiply(yk);
            org.apache.commons.math.dfp.Dfp oneMinusY4 = dfp.subtract(y2.multiply(y2));
            org.apache.commons.math.dfp.Dfp s = oneMinusY4.sqrt().sqrt();
            yk = dfp.subtract(s).divide(dfp.add(s));
            two2kp3 = two2kp3.multiply(four);
            org.apache.commons.math.dfp.Dfp p = dfp.add(yk);
            org.apache.commons.math.dfp.Dfp p2 = p.multiply(p);
            org.apache.commons.math.dfp.Dfp dfpMultiply2 = ak.multiply(p2.multiply(p2));
            org.apache.commons.math.dfp.Dfp dfpMultiply22 = two2kp3.multiply(yk);
            org.apache.commons.math.dfp.Dfp sqrt22 = sqrt2;
            org.apache.commons.math.dfp.Dfp sqrt23 = dfp.add(yk);
            org.apache.commons.math.dfp.Dfp four2 = four;
            org.apache.commons.math.dfp.Dfp four3 = yk.multiply(yk);
            ak = dfpMultiply2.subtract(dfpMultiply22.multiply(sqrt23.add(four3)));
            if (yk.equals(ykM1)) {
                break;
            }
            i++;
            sqrt2 = sqrt22;
            four = four2;
        }
        return dfp.divide(ak);
    }

    public static org.apache.commons.math.dfp.Dfp computeExp(org.apache.commons.math.dfp.Dfp a, org.apache.commons.math.dfp.Dfp one) {
        org.apache.commons.math.dfp.Dfp y = new org.apache.commons.math.dfp.Dfp(one);
        org.apache.commons.math.dfp.Dfp py = new org.apache.commons.math.dfp.Dfp(one);
        org.apache.commons.math.dfp.Dfp f = new org.apache.commons.math.dfp.Dfp(one);
        org.apache.commons.math.dfp.Dfp fi = new org.apache.commons.math.dfp.Dfp(one);
        org.apache.commons.math.dfp.Dfp x = new org.apache.commons.math.dfp.Dfp(one);
        for (int i = 0; i < 10000; i++) {
            x = x.multiply(a);
            y = y.add(x.divide(f));
            fi = fi.add(one);
            f = f.multiply(fi);
            if (y.equals(py)) {
                break;
            }
            py = new org.apache.commons.math.dfp.Dfp(y);
        }
        return y;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp computeLn(org.apache.commons.math.dfp.Dfp a, org.apache.commons.math.dfp.Dfp one, org.apache.commons.math.dfp.Dfp two) {
        int den = 1;
        org.apache.commons.math.dfp.Dfp x = a.add(new org.apache.commons.math.dfp.Dfp((org.apache.commons.math.dfp.DfpField) a.getField(), -1)).divide(a.add(one));
        org.apache.commons.math.dfp.Dfp y = new org.apache.commons.math.dfp.Dfp(x);
        org.apache.commons.math.dfp.Dfp num = new org.apache.commons.math.dfp.Dfp(x);
        org.apache.commons.math.dfp.Dfp py = new org.apache.commons.math.dfp.Dfp(y);
        for (int i = 0; i < 10000; i++) {
            num = num.multiply(x).multiply(x);
            den += 2;
            org.apache.commons.math.dfp.Dfp t = num.divide(den);
            y = y.add(t);
            if (y.equals(py)) {
                break;
            }
            py = new org.apache.commons.math.dfp.Dfp(y);
        }
        return y.multiply(two);
    }
}
