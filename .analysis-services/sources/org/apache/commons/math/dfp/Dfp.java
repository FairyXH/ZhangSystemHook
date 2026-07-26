package org.apache.commons.math.dfp;

/* JADX INFO: loaded from: classes4.dex */
public class Dfp implements org.apache.commons.math.FieldElement<org.apache.commons.math.dfp.Dfp> {
    private static final java.lang.String ADD_TRAP = "add";
    private static final java.lang.String ALIGN_TRAP = "align";
    private static final java.lang.String DIVIDE_TRAP = "divide";
    public static final int ERR_SCALE = 32760;
    public static final byte FINITE = 0;
    private static final java.lang.String GREATER_THAN_TRAP = "greaterThan";
    public static final byte INFINITE = 1;
    private static final java.lang.String LESS_THAN_TRAP = "lessThan";
    public static final int MAX_EXP = 32768;
    public static final int MIN_EXP = -32767;
    private static final java.lang.String MULTIPLY_TRAP = "multiply";
    private static final java.lang.String NAN_STRING = "NaN";
    private static final java.lang.String NEG_INFINITY_STRING = "-Infinity";
    private static final java.lang.String NEW_INSTANCE_TRAP = "newInstance";
    private static final java.lang.String NEXT_AFTER_TRAP = "nextAfter";
    private static final java.lang.String POS_INFINITY_STRING = "Infinity";
    public static final byte QNAN = 3;
    public static final int RADIX = 10000;
    public static final byte SNAN = 2;
    private static final java.lang.String SQRT_TRAP = "sqrt";
    private static final java.lang.String TRUNC_TRAP = "trunc";
    protected int exp;
    private final org.apache.commons.math.dfp.DfpField field;
    protected int[] mant;
    protected byte nans;
    protected byte sign;

    protected Dfp(org.apache.commons.math.dfp.DfpField field) {
        this.mant = new int[field.getRadixDigits()];
        this.sign = (byte) 1;
        this.exp = 0;
        this.nans = (byte) 0;
        this.field = field;
    }

    protected Dfp(org.apache.commons.math.dfp.DfpField field, byte x) {
        this(field, x);
    }

    protected Dfp(org.apache.commons.math.dfp.DfpField field, int x) {
        this(field, x);
    }

    protected Dfp(org.apache.commons.math.dfp.DfpField field, long x) {
        this.mant = new int[field.getRadixDigits()];
        this.nans = (byte) 0;
        this.field = field;
        boolean isLongMin = false;
        if (x == Long.MIN_VALUE) {
            isLongMin = true;
            x++;
        }
        if (x < 0) {
            this.sign = (byte) -1;
            x = -x;
        } else {
            this.sign = (byte) 1;
        }
        this.exp = 0;
        while (x != 0) {
            java.lang.System.arraycopy(this.mant, this.mant.length - this.exp, this.mant, (this.mant.length - 1) - this.exp, this.exp);
            this.mant[this.mant.length - 1] = (int) (x % 10000);
            x /= 10000;
            this.exp++;
        }
        if (isLongMin) {
            for (int i = 0; i < this.mant.length - 1; i++) {
                if (this.mant[i] != 0) {
                    int[] iArr = this.mant;
                    iArr[i] = iArr[i] + 1;
                    return;
                }
            }
        }
    }

    protected Dfp(org.apache.commons.math.dfp.DfpField field, double x) {
        this.mant = new int[field.getRadixDigits()];
        this.sign = (byte) 1;
        this.exp = 0;
        this.nans = (byte) 0;
        this.field = field;
        long bits = java.lang.Double.doubleToLongBits(x);
        long mantissa = bits & 4503599627370495L;
        int exponent = ((int) ((9218868437227405312L & bits) >> 52)) - 1023;
        if (exponent == -1023) {
            if (x == 0.0d) {
                return;
            }
            exponent++;
            while ((mantissa & 4503599627370496L) == 0) {
                exponent--;
                mantissa <<= 1;
            }
            mantissa &= 4503599627370495L;
        }
        if (exponent != 1024) {
            org.apache.commons.math.dfp.Dfp xdfp = new org.apache.commons.math.dfp.Dfp(field, mantissa).divide(new org.apache.commons.math.dfp.Dfp(field, 4503599627370496L)).add(field.getOne()).multiply(org.apache.commons.math.dfp.DfpMath.pow(field.getTwo(), exponent));
            xdfp = (Long.MIN_VALUE & bits) != 0 ? xdfp.negate() : xdfp;
            java.lang.System.arraycopy(xdfp.mant, 0, this.mant, 0, this.mant.length);
            this.sign = xdfp.sign;
            this.exp = xdfp.exp;
            this.nans = xdfp.nans;
            return;
        }
        if (x != x) {
            this.sign = (byte) 1;
            this.nans = (byte) 3;
        } else if (x < 0.0d) {
            this.sign = (byte) -1;
            this.nans = (byte) 1;
        } else {
            this.sign = (byte) 1;
            this.nans = (byte) 1;
        }
    }

    public Dfp(org.apache.commons.math.dfp.Dfp d) {
        this.mant = (int[]) d.mant.clone();
        this.sign = d.sign;
        this.exp = d.exp;
        this.nans = d.nans;
        this.field = d.field;
    }

    protected Dfp(org.apache.commons.math.dfp.DfpField field, java.lang.String s) {
        java.lang.String fpdecimal;
        int i;
        char c;
        this.mant = new int[field.getRadixDigits()];
        this.sign = (byte) 1;
        this.exp = 0;
        this.nans = (byte) 0;
        this.field = field;
        boolean decimalFound = false;
        char[] striped = new char[(getRadixDigits() * 4) + 8];
        if (s.equals(POS_INFINITY_STRING)) {
            this.sign = (byte) 1;
            this.nans = (byte) 1;
            return;
        }
        if (s.equals(NEG_INFINITY_STRING)) {
            this.sign = (byte) -1;
            this.nans = (byte) 1;
            return;
        }
        if (s.equals(NAN_STRING)) {
            this.sign = (byte) 1;
            this.nans = (byte) 3;
            return;
        }
        int p = s.indexOf("e");
        p = p == -1 ? s.indexOf("E") : p;
        int sciexp = 0;
        char c2 = '9';
        char c3 = '0';
        if (p != -1) {
            java.lang.String fpdecimal2 = s.substring(0, p);
            java.lang.String fpexp = s.substring(p + 1);
            boolean negative = false;
            for (int i2 = 0; i2 < fpexp.length(); i2++) {
                if (fpexp.charAt(i2) == '-') {
                    negative = true;
                } else if (fpexp.charAt(i2) >= '0' && fpexp.charAt(i2) <= '9') {
                    sciexp = ((sciexp * 10) + fpexp.charAt(i2)) - 48;
                }
            }
            sciexp = negative ? -sciexp : sciexp;
            fpdecimal = fpdecimal2;
        } else {
            fpdecimal = s;
        }
        if (fpdecimal.indexOf("-") != -1) {
            this.sign = (byte) -1;
        }
        int p2 = 0;
        int decimalPos = 0;
        while (true) {
            if (fpdecimal.charAt(p2) >= '1' && fpdecimal.charAt(p2) <= c2) {
                break;
            }
            if (decimalFound && fpdecimal.charAt(p2) == c3) {
                decimalPos--;
            }
            decimalFound = fpdecimal.charAt(p2) == '.' ? true : decimalFound;
            p2++;
            if (p2 == fpdecimal.length()) {
                break;
            }
            c3 = '0';
            c2 = c2;
        }
        int q = 4;
        striped[0] = c3;
        striped[1] = c3;
        striped[2] = c3;
        striped[3] = c3;
        int significantDigits = 0;
        while (true) {
            if (p2 == fpdecimal.length()) {
                i = 4;
                break;
            }
            i = 4;
            if (q == (this.mant.length * 4) + 4 + 1) {
                break;
            }
            if (fpdecimal.charAt(p2) == '.') {
                decimalFound = true;
                decimalPos = significantDigits;
                p2++;
                c3 = '0';
            } else {
                if (fpdecimal.charAt(p2) >= '0') {
                    c = '9';
                    if (fpdecimal.charAt(p2) <= '9') {
                        striped[q] = fpdecimal.charAt(p2);
                        q++;
                        p2++;
                        significantDigits++;
                        c3 = '0';
                    }
                } else {
                    c = '9';
                }
                p2++;
                c3 = '0';
            }
        }
        if (decimalFound && q != i) {
            while (true) {
                q--;
                if (q == i || striped[q] != c3) {
                    break;
                }
                significantDigits--;
                i = 4;
            }
        }
        if (decimalFound && significantDigits == 0) {
            decimalPos = 0;
        }
        decimalPos = decimalFound ? decimalPos : q - 4;
        int p3 = (significantDigits - 1) + 4;
        int trailingZeros = 0;
        while (p3 > 4 && striped[p3] == c3) {
            trailingZeros++;
            p3--;
        }
        int i3 = ((400 - decimalPos) - (sciexp % 4)) % 4;
        int q2 = 4 - i3;
        int decimalPos2 = decimalPos + i3;
        while (true) {
            int trailingZeros2 = trailingZeros;
            if (p3 - q2 >= this.mant.length * 4) {
                break;
            }
            int i4 = 0;
            for (int trailingZeros3 = 4; i4 < trailingZeros3; trailingZeros3 = 4) {
                p3++;
                striped[p3] = '0';
                i4++;
            }
            trailingZeros = trailingZeros2;
        }
        for (int i5 = this.mant.length - 1; i5 >= 0; i5--) {
            this.mant[i5] = ((striped[q2] - '0') * 1000) + ((striped[q2 + 1] - '0') * 100) + ((striped[q2 + 2] - '0') * 10) + (striped[q2 + 3] - '0');
            q2 += 4;
        }
        this.exp = (decimalPos2 + sciexp) / 4;
        if (q2 < striped.length) {
            round((striped[q2] - '0') * 1000);
        }
    }

    protected Dfp(org.apache.commons.math.dfp.DfpField field, byte sign, byte nans) {
        this.field = field;
        this.mant = new int[field.getRadixDigits()];
        this.sign = sign;
        this.exp = 0;
        this.nans = nans;
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    public org.apache.commons.math.dfp.Dfp newInstance() {
        return new org.apache.commons.math.dfp.Dfp((org.apache.commons.math.dfp.DfpField) getField());
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    public org.apache.commons.math.dfp.Dfp newInstance(byte x) {
        return new org.apache.commons.math.dfp.Dfp((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    public org.apache.commons.math.dfp.Dfp newInstance(int x) {
        return new org.apache.commons.math.dfp.Dfp((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    public org.apache.commons.math.dfp.Dfp newInstance(long x) {
        return new org.apache.commons.math.dfp.Dfp((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    public org.apache.commons.math.dfp.Dfp newInstance(double x) {
        return new org.apache.commons.math.dfp.Dfp((org.apache.commons.math.dfp.DfpField) getField(), x);
    }

    public org.apache.commons.math.dfp.Dfp newInstance(org.apache.commons.math.dfp.Dfp d) {
        if (this.field.getRadixDigits() != d.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, NEW_INSTANCE_TRAP, d, result);
        }
        return new org.apache.commons.math.dfp.Dfp(d);
    }

    public org.apache.commons.math.dfp.Dfp newInstance(java.lang.String s) {
        return new org.apache.commons.math.dfp.Dfp(this.field, s);
    }

    public org.apache.commons.math.dfp.Dfp newInstance(byte sig, byte code) {
        return this.field.newDfp(sig, code);
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.Field<org.apache.commons.math.dfp.Dfp> getField() {
        return this.field;
    }

    public int getRadixDigits() {
        return this.field.getRadixDigits();
    }

    public org.apache.commons.math.dfp.Dfp getZero() {
        return this.field.getZero();
    }

    public org.apache.commons.math.dfp.Dfp getOne() {
        return this.field.getOne();
    }

    public org.apache.commons.math.dfp.Dfp getTwo() {
        return this.field.getTwo();
    }

    protected void shiftLeft() {
        for (int i = this.mant.length - 1; i > 0; i--) {
            this.mant[i] = this.mant[i - 1];
        }
        this.mant[0] = 0;
        this.exp--;
    }

    protected void shiftRight() {
        for (int i = 0; i < this.mant.length - 1; i++) {
            this.mant[i] = this.mant[i + 1];
        }
        this.mant[this.mant.length - 1] = 0;
        this.exp++;
    }

    protected int align(int e) {
        int lostdigit = 0;
        boolean inexact = false;
        int diff = this.exp - e;
        int adiff = diff;
        if (adiff < 0) {
            adiff = -adiff;
        }
        if (diff == 0) {
            return 0;
        }
        if (adiff > this.mant.length + 1) {
            java.util.Arrays.fill(this.mant, 0);
            this.exp = e;
            this.field.setIEEEFlagsBits(16);
            dotrap(16, ALIGN_TRAP, this, this);
            return 0;
        }
        for (int i = 0; i < adiff; i++) {
            if (diff < 0) {
                if (lostdigit != 0) {
                    inexact = true;
                }
                lostdigit = this.mant[0];
                shiftRight();
            } else {
                shiftLeft();
            }
        }
        if (inexact) {
            this.field.setIEEEFlagsBits(16);
            dotrap(16, ALIGN_TRAP, this, this);
        }
        return lostdigit;
    }

    public boolean lessThan(org.apache.commons.math.dfp.Dfp x) {
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            dotrap(1, LESS_THAN_TRAP, x, result);
            return false;
        }
        if (!isNaN() && !x.isNaN()) {
            return compare(this, x) < 0;
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, LESS_THAN_TRAP, x, newInstance(getZero()));
        return false;
    }

    public boolean greaterThan(org.apache.commons.math.dfp.Dfp x) {
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            dotrap(1, GREATER_THAN_TRAP, x, result);
            return false;
        }
        if (!isNaN() && !x.isNaN()) {
            return compare(this, x) > 0;
        }
        this.field.setIEEEFlagsBits(1);
        dotrap(1, GREATER_THAN_TRAP, x, newInstance(getZero()));
        return false;
    }

    public boolean isInfinite() {
        return this.nans == 1;
    }

    public boolean isNaN() {
        return this.nans == 3 || this.nans == 2;
    }

    public boolean equals(java.lang.Object other) {
        if (!(other instanceof org.apache.commons.math.dfp.Dfp)) {
            return false;
        }
        org.apache.commons.math.dfp.Dfp x = (org.apache.commons.math.dfp.Dfp) other;
        return !isNaN() && !x.isNaN() && this.field.getRadixDigits() == x.field.getRadixDigits() && compare(this, x) == 0;
    }

    public int hashCode() {
        return (this.sign << 8) + 17 + (this.nans << com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CAPABILITY) + this.exp + java.util.Arrays.hashCode(this.mant);
    }

    public boolean unequal(org.apache.commons.math.dfp.Dfp x) {
        if (isNaN() || x.isNaN() || this.field.getRadixDigits() != x.field.getRadixDigits()) {
            return false;
        }
        return greaterThan(x) || lessThan(x);
    }

    private static int compare(org.apache.commons.math.dfp.Dfp a, org.apache.commons.math.dfp.Dfp b) {
        if (a.mant[a.mant.length - 1] == 0 && b.mant[b.mant.length - 1] == 0 && a.nans == 0 && b.nans == 0) {
            return 0;
        }
        if (a.sign != b.sign) {
            return a.sign == -1 ? -1 : 1;
        }
        if (a.nans == 1 && b.nans == 0) {
            return a.sign;
        }
        if (a.nans == 0 && b.nans == 1) {
            return -b.sign;
        }
        if (a.nans == 1 && b.nans == 1) {
            return 0;
        }
        if (b.mant[b.mant.length - 1] != 0 && a.mant[b.mant.length - 1] != 0) {
            if (a.exp < b.exp) {
                return -a.sign;
            }
            if (a.exp > b.exp) {
                return a.sign;
            }
        }
        for (int i = a.mant.length - 1; i >= 0; i--) {
            if (a.mant[i] > b.mant[i]) {
                return a.sign;
            }
            if (a.mant[i] < b.mant[i]) {
                return -a.sign;
            }
        }
        return 0;
    }

    public org.apache.commons.math.dfp.Dfp rint() {
        return trunc(org.apache.commons.math.dfp.DfpField.RoundingMode.ROUND_HALF_EVEN);
    }

    public org.apache.commons.math.dfp.Dfp floor() {
        return trunc(org.apache.commons.math.dfp.DfpField.RoundingMode.ROUND_FLOOR);
    }

    public org.apache.commons.math.dfp.Dfp ceil() {
        return trunc(org.apache.commons.math.dfp.DfpField.RoundingMode.ROUND_CEIL);
    }

    public org.apache.commons.math.dfp.Dfp remainder(org.apache.commons.math.dfp.Dfp d) {
        org.apache.commons.math.dfp.Dfp result = subtract(divide(d).rint().multiply(d));
        if (result.mant[this.mant.length - 1] == 0) {
            result.sign = this.sign;
        }
        return result;
    }

    protected org.apache.commons.math.dfp.Dfp trunc(org.apache.commons.math.dfp.DfpField.RoundingMode rmode) {
        boolean changed = false;
        if (isNaN()) {
            return newInstance(this);
        }
        if (this.nans == 1) {
            return newInstance(this);
        }
        if (this.mant[this.mant.length - 1] == 0) {
            return newInstance(this);
        }
        if (this.exp < 0) {
            this.field.setIEEEFlagsBits(16);
            return dotrap(16, TRUNC_TRAP, this, newInstance(getZero()));
        }
        if (this.exp >= this.mant.length) {
            return newInstance(this);
        }
        org.apache.commons.math.dfp.Dfp result = newInstance(this);
        for (int i = 0; i < this.mant.length - result.exp; i++) {
            changed |= result.mant[i] != 0;
            result.mant[i] = 0;
        }
        if (changed) {
            switch (rmode) {
                case ROUND_FLOOR:
                    if (result.sign == -1) {
                        result = result.add(newInstance(-1));
                    }
                    break;
                case ROUND_CEIL:
                    if (result.sign == 1) {
                        result = result.add(getOne());
                    }
                    break;
                default:
                    org.apache.commons.math.dfp.Dfp half = newInstance("0.5");
                    org.apache.commons.math.dfp.Dfp a = subtract(result);
                    a.sign = (byte) 1;
                    if (a.greaterThan(half)) {
                        a = newInstance(getOne());
                        a.sign = this.sign;
                        result = result.add(a);
                    }
                    if (a.equals(half) && result.exp > 0 && (1 & result.mant[this.mant.length - result.exp]) != 0) {
                        org.apache.commons.math.dfp.Dfp a2 = newInstance(getOne());
                        a2.sign = this.sign;
                        result = result.add(a2);
                    }
                    break;
            }
            this.field.setIEEEFlagsBits(16);
            return dotrap(16, TRUNC_TRAP, this, result);
        }
        return result;
    }

    public int intValue() {
        int result = 0;
        org.apache.commons.math.dfp.Dfp rounded = rint();
        if (rounded.greaterThan(newInstance(Integer.MAX_VALUE))) {
            return Integer.MAX_VALUE;
        }
        if (rounded.lessThan(newInstance(Integer.MIN_VALUE))) {
            return Integer.MIN_VALUE;
        }
        int i = this.mant.length;
        while (true) {
            i--;
            if (i < this.mant.length - rounded.exp) {
                break;
            }
            result = (result * 10000) + rounded.mant[i];
        }
        int i2 = rounded.sign;
        if (i2 == -1) {
            return -result;
        }
        return result;
    }

    public int log10K() {
        return this.exp - 1;
    }

    public org.apache.commons.math.dfp.Dfp power10K(int e) {
        org.apache.commons.math.dfp.Dfp d = newInstance(getOne());
        d.exp = e + 1;
        return d;
    }

    public int log10() {
        if (this.mant[this.mant.length - 1] > 1000) {
            return (this.exp * 4) - 1;
        }
        if (this.mant[this.mant.length - 1] > 100) {
            return (this.exp * 4) - 2;
        }
        if (this.mant[this.mant.length - 1] > 10) {
            return (this.exp * 4) - 3;
        }
        return (this.exp * 4) - 4;
    }

    public org.apache.commons.math.dfp.Dfp power10(int e) {
        org.apache.commons.math.dfp.Dfp d = newInstance(getOne());
        if (e >= 0) {
            d.exp = (e / 4) + 1;
        } else {
            d.exp = (e + 1) / 4;
        }
        switch (((e % 4) + 4) % 4) {
            case 0:
                return d;
            case 1:
                return d.multiply(10);
            case 2:
                return d.multiply(100);
            default:
                return d.multiply(1000);
        }
    }

    protected int complement(int extra) {
        int extra2 = 10000 - extra;
        for (int i = 0; i < this.mant.length; i++) {
            this.mant[i] = (10000 - this.mant[i]) - 1;
        }
        int rh = extra2 / 10000;
        int extra3 = extra2 - (rh * 10000);
        for (int i2 = 0; i2 < this.mant.length; i2++) {
            int r = this.mant[i2] + rh;
            rh = r / 10000;
            this.mant[i2] = r - (rh * 10000);
        }
        return extra3;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.dfp.Dfp add(org.apache.commons.math.dfp.Dfp x) {
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, ADD_TRAP, x, result);
        }
        if (this.nans != 0 || x.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (x.isNaN()) {
                return x;
            }
            if (this.nans == 1 && x.nans == 0) {
                return this;
            }
            if (x.nans == 1 && this.nans == 0) {
                return x;
            }
            if (x.nans == 1 && this.nans == 1 && this.sign == x.sign) {
                return x;
            }
            if (x.nans == 1 && this.nans == 1 && this.sign != x.sign) {
                this.field.setIEEEFlagsBits(1);
                org.apache.commons.math.dfp.Dfp result2 = newInstance(getZero());
                result2.nans = (byte) 3;
                return dotrap(1, ADD_TRAP, x, result2);
            }
        }
        org.apache.commons.math.dfp.Dfp a = newInstance(this);
        org.apache.commons.math.dfp.Dfp b = newInstance(x);
        org.apache.commons.math.dfp.Dfp result3 = newInstance(getZero());
        byte asign = a.sign;
        byte bsign = b.sign;
        a.sign = (byte) 1;
        b.sign = (byte) 1;
        byte rsign = bsign;
        if (compare(a, b) > 0) {
            rsign = asign;
        }
        if (b.mant[this.mant.length - 1] == 0) {
            b.exp = a.exp;
        }
        if (a.mant[this.mant.length - 1] == 0) {
            a.exp = b.exp;
        }
        int aextradigit = 0;
        int bextradigit = 0;
        if (a.exp < b.exp) {
            aextradigit = a.align(b.exp);
        } else {
            bextradigit = b.align(a.exp);
        }
        if (asign != bsign) {
            if (asign == rsign) {
                bextradigit = b.complement(bextradigit);
            } else {
                aextradigit = a.complement(aextradigit);
            }
        }
        int rh = 0;
        for (int i = 0; i < this.mant.length; i++) {
            int r = a.mant[i] + b.mant[i] + rh;
            rh = r / 10000;
            result3.mant[i] = r - (rh * 10000);
        }
        result3.exp = a.exp;
        result3.sign = rsign;
        if (rh != 0 && asign == bsign) {
            int lostdigit = result3.mant[0];
            result3.shiftRight();
            result3.mant[this.mant.length - 1] = rh;
            int excp = result3.round(lostdigit);
            if (excp != 0) {
                result3 = dotrap(excp, ADD_TRAP, x, result3);
            }
        }
        for (int i2 = 0; i2 < this.mant.length && result3.mant[this.mant.length - 1] == 0; i2++) {
            result3.shiftLeft();
            if (i2 == 0) {
                result3.mant[0] = aextradigit + bextradigit;
                aextradigit = 0;
                bextradigit = 0;
            }
        }
        if (result3.mant[this.mant.length - 1] == 0) {
            result3.exp = 0;
            if (asign != bsign) {
                result3.sign = (byte) 1;
            }
        }
        int excp2 = result3.round(aextradigit + bextradigit);
        if (excp2 != 0) {
            return dotrap(excp2, ADD_TRAP, x, result3);
        }
        return result3;
    }

    public org.apache.commons.math.dfp.Dfp negate() {
        org.apache.commons.math.dfp.Dfp result = newInstance(this);
        result.sign = (byte) (-result.sign);
        return result;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.dfp.Dfp subtract(org.apache.commons.math.dfp.Dfp x) {
        return add(x.negate());
    }

    protected int round(int n) {
        boolean inc;
        switch (this.field.getRoundingMode()) {
            case ROUND_CEIL:
                inc = this.sign == 1 && n != 0;
                break;
            case ROUND_HALF_EVEN:
                inc = n > 5000 || (n == 5000 && (this.mant[0] & 1) == 1);
                break;
            case ROUND_DOWN:
                inc = false;
                break;
            case ROUND_UP:
                inc = n != 0;
                break;
            case ROUND_HALF_UP:
                inc = n >= 5000;
                break;
            case ROUND_HALF_DOWN:
                inc = n > 5000;
                break;
            case ROUND_HALF_ODD:
                inc = n > 5000 || (n == 5000 && (this.mant[0] & 1) == 0);
                break;
            default:
                inc = this.sign == -1 && n != 0;
                break;
        }
        if (inc) {
            int rh = 1;
            for (int i = 0; i < this.mant.length; i++) {
                int r = this.mant[i] + rh;
                rh = r / 10000;
                this.mant[i] = r - (rh * 10000);
            }
            if (rh != 0) {
                shiftRight();
                this.mant[this.mant.length - 1] = rh;
            }
        }
        int rh2 = this.exp;
        if (rh2 < -32767) {
            this.field.setIEEEFlagsBits(8);
            return 8;
        }
        if (this.exp > 32768) {
            this.field.setIEEEFlagsBits(4);
            return 4;
        }
        if (n == 0) {
            return 0;
        }
        this.field.setIEEEFlagsBits(16);
        return 16;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.dfp.Dfp multiply(org.apache.commons.math.dfp.Dfp x) {
        int excp;
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            result.nans = (byte) 3;
            return dotrap(1, MULTIPLY_TRAP, x, result);
        }
        org.apache.commons.math.dfp.Dfp result2 = newInstance(getZero());
        if (this.nans != 0 || x.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (x.isNaN()) {
                return x;
            }
            if (this.nans == 1 && x.nans == 0 && x.mant[this.mant.length - 1] != 0) {
                org.apache.commons.math.dfp.Dfp result3 = newInstance(this);
                result3.sign = (byte) (this.sign * x.sign);
                return result3;
            }
            if (x.nans == 1 && this.nans == 0 && this.mant[this.mant.length - 1] != 0) {
                org.apache.commons.math.dfp.Dfp result4 = newInstance(x);
                result4.sign = (byte) (this.sign * x.sign);
                return result4;
            }
            if (x.nans == 1 && this.nans == 1) {
                org.apache.commons.math.dfp.Dfp result5 = newInstance(this);
                result5.sign = (byte) (this.sign * x.sign);
                return result5;
            }
            if ((x.nans == 1 && this.nans == 0 && this.mant[this.mant.length - 1] == 0) || (this.nans == 1 && x.nans == 0 && x.mant[this.mant.length - 1] == 0)) {
                this.field.setIEEEFlagsBits(1);
                org.apache.commons.math.dfp.Dfp result6 = newInstance(getZero());
                result6.nans = (byte) 3;
                return dotrap(1, MULTIPLY_TRAP, x, result6);
            }
        }
        int[] product = new int[this.mant.length * 2];
        for (int i = 0; i < this.mant.length; i++) {
            int rh = 0;
            for (int j = 0; j < this.mant.length; j++) {
                int r = product[i + j] + (this.mant[i] * x.mant[j]) + rh;
                rh = r / 10000;
                product[i + j] = r - (rh * 10000);
            }
            product[this.mant.length + i] = rh;
        }
        int md = (this.mant.length * 2) - 1;
        int i2 = (this.mant.length * 2) - 1;
        while (true) {
            if (i2 < 0) {
                break;
            }
            if (product[i2] != 0) {
                md = i2;
                break;
            }
            i2--;
        }
        for (int i3 = 0; i3 < this.mant.length; i3++) {
            result2.mant[(this.mant.length - i3) - 1] = product[md - i3];
        }
        int i4 = this.exp;
        result2.exp = (((i4 + x.exp) + md) - (this.mant.length * 2)) + 1;
        result2.sign = (byte) (this.sign == x.sign ? 1 : -1);
        if (result2.mant[this.mant.length - 1] == 0) {
            result2.exp = 0;
        }
        if (md > this.mant.length - 1) {
            excp = result2.round(product[md - this.mant.length]);
        } else {
            excp = result2.round(0);
        }
        if (excp != 0) {
            return dotrap(excp, MULTIPLY_TRAP, x, result2);
        }
        return result2;
    }

    public org.apache.commons.math.dfp.Dfp multiply(int x) {
        org.apache.commons.math.dfp.Dfp result = newInstance(this);
        if (this.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (this.nans == 1 && x != 0) {
                return newInstance(this);
            }
            if (this.nans == 1 && x == 0) {
                this.field.setIEEEFlagsBits(1);
                org.apache.commons.math.dfp.Dfp result2 = newInstance(getZero());
                result2.nans = (byte) 3;
                return dotrap(1, MULTIPLY_TRAP, newInstance(getZero()), result2);
            }
        }
        if (x < 0 || x >= 10000) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result3 = newInstance(getZero());
            result3.nans = (byte) 3;
            return dotrap(1, MULTIPLY_TRAP, result3, result3);
        }
        int rh = 0;
        for (int i = 0; i < this.mant.length; i++) {
            int r = (this.mant[i] * x) + rh;
            rh = r / 10000;
            result.mant[i] = r - (rh * 10000);
        }
        int lostdigit = 0;
        if (rh != 0) {
            lostdigit = result.mant[0];
            result.shiftRight();
            result.mant[this.mant.length - 1] = rh;
        }
        if (result.mant[this.mant.length - 1] == 0) {
            result.exp = 0;
        }
        int excp = result.round(lostdigit);
        if (excp != 0) {
            return dotrap(excp, MULTIPLY_TRAP, result, result);
        }
        return result;
    }

    @Override // org.apache.commons.math.FieldElement
    public org.apache.commons.math.dfp.Dfp divide(org.apache.commons.math.dfp.Dfp divisor) {
        int excp;
        boolean trialgood;
        int trial = 0;
        int md = 0;
        int i = 1;
        if (this.field.getRadixDigits() == divisor.field.getRadixDigits()) {
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            if (this.nans != 0 || divisor.nans != 0) {
                if (isNaN()) {
                    return this;
                }
                if (divisor.isNaN()) {
                    return divisor;
                }
                if (this.nans == 1 && divisor.nans == 0) {
                    org.apache.commons.math.dfp.Dfp result2 = newInstance(this);
                    result2.sign = (byte) (this.sign * divisor.sign);
                    return result2;
                }
                if (divisor.nans == 1 && this.nans == 0) {
                    org.apache.commons.math.dfp.Dfp result3 = newInstance(getZero());
                    result3.sign = (byte) (this.sign * divisor.sign);
                    return result3;
                }
                if (divisor.nans == 1 && this.nans == 1) {
                    this.field.setIEEEFlagsBits(1);
                    org.apache.commons.math.dfp.Dfp result4 = newInstance(getZero());
                    result4.nans = (byte) 3;
                    return dotrap(1, DIVIDE_TRAP, divisor, result4);
                }
            }
            if (divisor.mant[this.mant.length - 1] == 0) {
                this.field.setIEEEFlagsBits(2);
                org.apache.commons.math.dfp.Dfp result5 = newInstance(getZero());
                result5.sign = (byte) (this.sign * divisor.sign);
                result5.nans = (byte) 1;
                return dotrap(2, DIVIDE_TRAP, divisor, result5);
            }
            int[] dividend = new int[this.mant.length + 1];
            int[] quotient = new int[this.mant.length + 2];
            int[] remainder = new int[this.mant.length + 1];
            dividend[this.mant.length] = 0;
            quotient[this.mant.length] = 0;
            quotient[this.mant.length + 1] = 0;
            remainder[this.mant.length] = 0;
            for (int i2 = 0; i2 < this.mant.length; i2++) {
                dividend[i2] = this.mant[i2];
                quotient[i2] = 0;
                remainder[i2] = 0;
            }
            int nsqd = 0;
            int qd = this.mant.length + 1;
            while (qd >= 0) {
                int divMsb = (dividend[this.mant.length] * 10000) + dividend[this.mant.length - i];
                int min = divMsb / (divisor.mant[this.mant.length - i] + i);
                int trial2 = trial;
                int max = (divMsb + 1) / divisor.mant[this.mant.length - i];
                boolean trialgood2 = false;
                while (!trialgood2) {
                    trial2 = (min + max) / 2;
                    int rh = 0;
                    int md2 = 0;
                    while (true) {
                        trialgood = trialgood2;
                        int length = this.mant.length + i;
                        int i3 = md2;
                        if (i3 >= length) {
                            break;
                        }
                        int dm = ((i3 < this.mant.length ? divisor.mant[i3] : 0) * trial2) + rh;
                        rh = dm / 10000;
                        int md3 = md;
                        int md4 = rh * 10000;
                        remainder[i3] = dm - md4;
                        int r = i3 + 1;
                        md = md3;
                        i = 1;
                        md2 = r;
                        trialgood2 = trialgood;
                    }
                    int md5 = md;
                    int rh2 = 1;
                    for (int i4 = 0; i4 < this.mant.length + 1; i4++) {
                        int r2 = (9999 - remainder[i4]) + dividend[i4] + rh2;
                        rh2 = r2 / 10000;
                        remainder[i4] = r2 - (rh2 * 10000);
                    }
                    if (rh2 != 0) {
                        int minadj = ((remainder[this.mant.length] * 10000) + remainder[this.mant.length - 1]) / (divisor.mant[this.mant.length - 1] + 1);
                        if (minadj >= 2) {
                            min = trial2 + minadj;
                            trialgood2 = trialgood;
                            md = md5;
                            i = 1;
                        } else {
                            boolean trialgood3 = false;
                            int i5 = this.mant.length - 1;
                            while (i5 >= 0) {
                                int minadj2 = minadj;
                                if (divisor.mant[i5] > remainder[i5]) {
                                    trialgood3 = true;
                                }
                                if (divisor.mant[i5] < remainder[i5]) {
                                    break;
                                }
                                i5--;
                                minadj = minadj2;
                            }
                            if (remainder[this.mant.length] == 0) {
                                trialgood2 = trialgood3;
                            } else {
                                trialgood2 = false;
                            }
                            if (!trialgood2) {
                                min = trial2 + 1;
                            }
                            md = md5;
                            i = 1;
                        }
                    } else {
                        max = trial2 - 1;
                        trialgood2 = trialgood;
                        md = md5;
                        i = 1;
                    }
                }
                int md6 = md;
                quotient[qd] = trial2;
                if (trial2 != 0 || nsqd != 0) {
                    nsqd++;
                }
                if ((this.field.getRoundingMode() == org.apache.commons.math.dfp.DfpField.RoundingMode.ROUND_DOWN && nsqd == this.mant.length) || nsqd > this.mant.length) {
                    break;
                }
                dividend[0] = 0;
                for (int i6 = 0; i6 < this.mant.length; i6++) {
                    dividend[i6 + 1] = remainder[i6];
                }
                qd--;
                trial = trial2;
                md = md6;
                i = 1;
            }
            int md7 = this.mant.length;
            int i7 = this.mant.length + 1;
            while (true) {
                if (i7 < 0) {
                    break;
                }
                if (quotient[i7] != 0) {
                    md7 = i7;
                    break;
                }
                i7--;
            }
            for (int i8 = 0; i8 < this.mant.length; i8++) {
                result.mant[(this.mant.length - i8) - 1] = quotient[md7 - i8];
            }
            int i9 = this.exp;
            result.exp = ((i9 - divisor.exp) + md7) - this.mant.length;
            result.sign = (byte) (this.sign == divisor.sign ? 1 : -1);
            if (result.mant[this.mant.length - 1] == 0) {
                result.exp = 0;
            }
            if (md7 > this.mant.length - 1) {
                excp = result.round(quotient[md7 - this.mant.length]);
            } else {
                excp = result.round(0);
            }
            if (excp != 0) {
                return dotrap(excp, DIVIDE_TRAP, divisor, result);
            }
            return result;
        }
        this.field.setIEEEFlagsBits(1);
        org.apache.commons.math.dfp.Dfp result6 = newInstance(getZero());
        result6.nans = (byte) 3;
        return dotrap(1, DIVIDE_TRAP, divisor, result6);
    }

    public org.apache.commons.math.dfp.Dfp divide(int divisor) {
        if (this.nans != 0) {
            if (isNaN()) {
                return this;
            }
            if (this.nans == 1) {
                return newInstance(this);
            }
        }
        if (divisor == 0) {
            this.field.setIEEEFlagsBits(2);
            org.apache.commons.math.dfp.Dfp result = newInstance(getZero());
            result.sign = this.sign;
            result.nans = (byte) 1;
            return dotrap(2, DIVIDE_TRAP, getZero(), result);
        }
        if (divisor < 0 || divisor >= 10000) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result2 = newInstance(getZero());
            result2.nans = (byte) 3;
            return dotrap(1, DIVIDE_TRAP, result2, result2);
        }
        org.apache.commons.math.dfp.Dfp result3 = newInstance(this);
        int rl = 0;
        for (int i = this.mant.length - 1; i >= 0; i--) {
            int r = (rl * 10000) + result3.mant[i];
            int rh = r / divisor;
            rl = r - (rh * divisor);
            result3.mant[i] = rh;
        }
        if (result3.mant[this.mant.length - 1] == 0) {
            result3.shiftLeft();
            int r2 = rl * 10000;
            int rh2 = r2 / divisor;
            rl = r2 - (rh2 * divisor);
            result3.mant[0] = rh2;
        }
        int excp = result3.round((rl * 10000) / divisor);
        if (excp != 0) {
            return dotrap(excp, DIVIDE_TRAP, result3, result3);
        }
        return result3;
    }

    public org.apache.commons.math.dfp.Dfp sqrt() {
        if (this.nans == 0 && this.mant[this.mant.length - 1] == 0) {
            return newInstance(this);
        }
        if (this.nans != 0) {
            if (this.nans == 1 && this.sign == 1) {
                return newInstance(this);
            }
            if (this.nans == 3) {
                return newInstance(this);
            }
            if (this.nans == 2) {
                this.field.setIEEEFlagsBits(1);
                return dotrap(1, SQRT_TRAP, null, newInstance(this));
            }
        }
        if (this.sign == -1) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = newInstance(this);
            result.nans = (byte) 3;
            return dotrap(1, SQRT_TRAP, null, result);
        }
        org.apache.commons.math.dfp.Dfp x = newInstance(this);
        if (x.exp < -1 || x.exp > 1) {
            x.exp = this.exp / 2;
        }
        switch (x.mant[this.mant.length - 1] / 2000) {
            case 0:
                x.mant[this.mant.length - 1] = (x.mant[this.mant.length - 1] / 2) + 1;
                break;
            case 1:
            default:
                x.mant[this.mant.length - 1] = 3000;
                break;
            case 2:
                x.mant[this.mant.length - 1] = 1500;
                break;
            case 3:
                x.mant[this.mant.length - 1] = 2200;
                break;
        }
        newInstance(x);
        org.apache.commons.math.dfp.Dfp px = getZero();
        getZero();
        while (x.unequal(px)) {
            org.apache.commons.math.dfp.Dfp dx = newInstance(x);
            dx.sign = (byte) -1;
            org.apache.commons.math.dfp.Dfp dx2 = dx.add(divide(x)).divide(2);
            java.lang.Object ppx = px;
            px = x;
            x = x.add(dx2);
            if (x.equals(ppx) || dx2.mant[this.mant.length - 1] == 0) {
                return x;
            }
        }
        return x;
    }

    public java.lang.String toString() {
        if (this.nans != 0) {
            if (this.nans == 1) {
                return this.sign < 0 ? NEG_INFINITY_STRING : POS_INFINITY_STRING;
            }
            return NAN_STRING;
        }
        if (this.exp > this.mant.length || this.exp < -1) {
            return dfp2sci();
        }
        return dfp2string();
    }

    protected java.lang.String dfp2sci() {
        char[] rawdigits = new char[this.mant.length * 4];
        char[] outputbuffer = new char[(this.mant.length * 4) + 20];
        int p = 0;
        int i = this.mant.length;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            int p2 = p + 1;
            rawdigits[p] = (char) ((this.mant[i] / 1000) + 48);
            int p3 = p2 + 1;
            rawdigits[p2] = (char) (((this.mant[i] / 100) % 10) + 48);
            int p4 = p3 + 1;
            rawdigits[p3] = (char) (((this.mant[i] / 10) % 10) + 48);
            p = p4 + 1;
            rawdigits[p4] = (char) ((this.mant[i] % 10) + 48);
        }
        int p5 = 0;
        while (p5 < rawdigits.length && rawdigits[p5] == '0') {
            p5++;
        }
        int shf = p5;
        int q = 0;
        if (this.sign == -1) {
            int q2 = 0 + 1;
            outputbuffer[0] = '-';
            q = q2;
        }
        int q3 = rawdigits.length;
        if (p5 != q3) {
            int q4 = q + 1;
            outputbuffer[q] = rawdigits[p5];
            int q5 = q4 + 1;
            outputbuffer[q4] = '.';
            for (int p6 = p5 + 1; p6 < rawdigits.length; p6++) {
                outputbuffer[q5] = rawdigits[p6];
                q5++;
            }
            int q6 = q5 + 1;
            outputbuffer[q5] = 'e';
            int e = ((this.exp * 4) - shf) - 1;
            int ae = e;
            if (e < 0) {
                ae = -e;
            }
            int p7 = com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord.TIMEOUT_NS;
            while (p7 > ae) {
                p7 /= 10;
            }
            if (e < 0) {
                outputbuffer[q6] = '-';
                q6++;
            }
            while (p7 > 0) {
                outputbuffer[q6] = (char) ((ae / p7) + 48);
                ae %= p7;
                p7 /= 10;
                q6++;
            }
            return new java.lang.String(outputbuffer, 0, q6);
        }
        int ae2 = q + 1;
        outputbuffer[q] = '0';
        int q7 = ae2 + 1;
        outputbuffer[ae2] = '.';
        int q8 = q7 + 1;
        outputbuffer[q7] = '0';
        int q9 = q8 + 1;
        outputbuffer[q8] = 'e';
        int i2 = q9 + 1;
        outputbuffer[q9] = '0';
        return new java.lang.String(outputbuffer, 0, 5);
    }

    protected java.lang.String dfp2string() {
        char[] buffer = new char[(this.mant.length * 4) + 20];
        int p = 1;
        int e = this.exp;
        boolean pointInserted = false;
        buffer[0] = ' ';
        if (e <= 0) {
            int p2 = 1 + 1;
            buffer[1] = '0';
            p = p2 + 1;
            buffer[p2] = '.';
            pointInserted = true;
        }
        while (e < 0) {
            int p3 = p + 1;
            buffer[p] = '0';
            int p4 = p3 + 1;
            buffer[p3] = '0';
            int p5 = p4 + 1;
            buffer[p4] = '0';
            p = p5 + 1;
            buffer[p5] = '0';
            e++;
        }
        for (int i = this.mant.length - 1; i >= 0; i--) {
            int p6 = p + 1;
            buffer[p] = (char) ((this.mant[i] / 1000) + 48);
            int p7 = p6 + 1;
            buffer[p6] = (char) (((this.mant[i] / 100) % 10) + 48);
            int p8 = p7 + 1;
            buffer[p7] = (char) (((this.mant[i] / 10) % 10) + 48);
            p = p8 + 1;
            buffer[p8] = (char) ((this.mant[i] % 10) + 48);
            e--;
            if (e == 0) {
                buffer[p] = '.';
                pointInserted = true;
                p++;
            }
        }
        while (e > 0) {
            int p9 = p + 1;
            buffer[p] = '0';
            int p10 = p9 + 1;
            buffer[p9] = '0';
            int p11 = p10 + 1;
            buffer[p10] = '0';
            p = p11 + 1;
            buffer[p11] = '0';
            e--;
        }
        if (!pointInserted) {
            buffer[p] = '.';
            p++;
        }
        int q = 1;
        while (buffer[q] == '0') {
            q++;
        }
        if (buffer[q] == '.') {
            q--;
        }
        while (buffer[p - 1] == '0') {
            p--;
        }
        if (this.sign < 0) {
            q--;
            buffer[q] = '-';
        }
        return new java.lang.String(buffer, q, p - q);
    }

    public org.apache.commons.math.dfp.Dfp dotrap(int type, java.lang.String what, org.apache.commons.math.dfp.Dfp oper, org.apache.commons.math.dfp.Dfp result) {
        org.apache.commons.math.dfp.Dfp def = result;
        switch (type) {
            case 1:
                def = newInstance(getZero());
                def.sign = result.sign;
                def.nans = (byte) 3;
                break;
            case 2:
                if (this.nans == 0 && this.mant[this.mant.length - 1] != 0) {
                    def = newInstance(getZero());
                    def.sign = (byte) (this.sign * oper.sign);
                    def.nans = (byte) 1;
                }
                if (this.nans == 0 && this.mant[this.mant.length - 1] == 0) {
                    def = newInstance(getZero());
                    def.nans = (byte) 3;
                }
                if (this.nans == 1 || this.nans == 3) {
                    def = newInstance(getZero());
                    def.nans = (byte) 3;
                }
                if (this.nans == 1 || this.nans == 2) {
                    def = newInstance(getZero());
                    def.nans = (byte) 3;
                }
                break;
            case 4:
                result.exp -= 32760;
                def = newInstance(getZero());
                def.sign = result.sign;
                def.nans = (byte) 1;
                break;
            case 8:
                if (result.exp + this.mant.length < -32767) {
                    def = newInstance(getZero());
                    def.sign = result.sign;
                } else {
                    def = newInstance(result);
                }
                result.exp += ERR_SCALE;
                break;
            default:
                def = result;
                break;
        }
        return trap(type, what, oper, def, result);
    }

    protected org.apache.commons.math.dfp.Dfp trap(int type, java.lang.String what, org.apache.commons.math.dfp.Dfp oper, org.apache.commons.math.dfp.Dfp def, org.apache.commons.math.dfp.Dfp result) {
        return def;
    }

    public int classify() {
        return this.nans;
    }

    public static org.apache.commons.math.dfp.Dfp copysign(org.apache.commons.math.dfp.Dfp x, org.apache.commons.math.dfp.Dfp y) {
        org.apache.commons.math.dfp.Dfp result = x.newInstance(x);
        result.sign = y.sign;
        return result;
    }

    public org.apache.commons.math.dfp.Dfp nextAfter(org.apache.commons.math.dfp.Dfp x) {
        org.apache.commons.math.dfp.Dfp result;
        if (this.field.getRadixDigits() != x.field.getRadixDigits()) {
            this.field.setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result2 = newInstance(getZero());
            result2.nans = (byte) 3;
            return dotrap(1, NEXT_AFTER_TRAP, x, result2);
        }
        boolean up = false;
        if (lessThan(x)) {
            up = true;
        }
        if (compare(this, x) == 0) {
            return newInstance(x);
        }
        if (lessThan(getZero())) {
            up = !up;
        }
        if (up) {
            org.apache.commons.math.dfp.Dfp inc = newInstance(getOne());
            inc.exp = (this.exp - this.mant.length) + 1;
            inc.sign = this.sign;
            if (equals(getZero())) {
                inc.exp = (-32767) - this.mant.length;
            }
            result = add(inc);
        } else {
            org.apache.commons.math.dfp.Dfp inc2 = newInstance(getOne());
            inc2.exp = this.exp;
            inc2.sign = this.sign;
            if (equals(inc2)) {
                inc2.exp = this.exp - this.mant.length;
            } else {
                inc2.exp = (this.exp - this.mant.length) + 1;
            }
            if (equals(getZero())) {
                inc2.exp = (-32767) - this.mant.length;
            }
            result = subtract(inc2);
        }
        if (result.classify() == 1 && classify() != 1) {
            this.field.setIEEEFlagsBits(16);
            result = dotrap(16, NEXT_AFTER_TRAP, x, result);
        }
        if (result.equals(getZero()) && !equals(getZero())) {
            this.field.setIEEEFlagsBits(16);
            return dotrap(16, NEXT_AFTER_TRAP, x, result);
        }
        return result;
    }

    public double toDouble() {
        if (isInfinite()) {
            return lessThan(getZero()) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        if (isNaN()) {
            return Double.NaN;
        }
        org.apache.commons.math.dfp.Dfp y = this;
        boolean negate = false;
        if (lessThan(getZero())) {
            y = negate();
            negate = true;
        }
        int exponent = (int) (((double) y.log10()) * 3.32d);
        if (exponent < 0) {
            exponent--;
        }
        org.apache.commons.math.dfp.Dfp tempDfp = org.apache.commons.math.dfp.DfpMath.pow(getTwo(), exponent);
        while (true) {
            if (!tempDfp.lessThan(y) && !tempDfp.equals(y)) {
                break;
            }
            tempDfp = tempDfp.multiply(2);
            exponent++;
        }
        int exponent2 = exponent - 1;
        org.apache.commons.math.dfp.Dfp y2 = y.divide(org.apache.commons.math.dfp.DfpMath.pow(getTwo(), exponent2));
        if (exponent2 > -1023) {
            y2 = y2.subtract(getOne());
        }
        if (exponent2 < -1074) {
            return 0.0d;
        }
        if (exponent2 > 1023) {
            return negate ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        }
        java.lang.String str = y2.multiply(newInstance(4503599627370496L)).rint().toString();
        long mantissa = java.lang.Long.parseLong(str.substring(0, str.length() - 1));
        if (mantissa == 4503599627370496L) {
            mantissa = 0;
            exponent2++;
        }
        if (exponent2 <= -1023) {
            exponent2--;
        }
        while (exponent2 < -1023) {
            exponent2++;
            mantissa >>>= 1;
        }
        long bits = ((((long) exponent2) + 1023) << 52) | mantissa;
        double x = java.lang.Double.longBitsToDouble(bits);
        if (negate) {
            return -x;
        }
        return x;
    }

    public double[] toSplitDouble() {
        double[] split = new double[2];
        split[0] = java.lang.Double.longBitsToDouble(java.lang.Double.doubleToLongBits(toDouble()) & (-1073741824));
        split[1] = subtract(newInstance(split[0])).toDouble();
        return split;
    }
}
