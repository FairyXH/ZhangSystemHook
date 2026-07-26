package org.apache.commons.math.dfp;

/* JADX INFO: loaded from: classes4.dex */
public class DfpMath {
    private static final java.lang.String POW_TRAP = "pow";

    private DfpMath() {
    }

    protected static org.apache.commons.math.dfp.Dfp[] split(org.apache.commons.math.dfp.DfpField field, java.lang.String a) {
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
            if (sig == (field.getRadixDigits() / 2) * 4) {
                sp = i;
                break;
            }
            if (buf[i] >= '0' && buf[i] <= '9' && !leading) {
                sig++;
            }
            i++;
        }
        result[0] = field.newDfp(new java.lang.String(buf, 0, sp));
        for (int i2 = 0; i2 < buf.length; i2++) {
            buf[i2] = a.charAt(i2);
            if (buf[i2] >= '0' && buf[i2] <= '9' && i2 < sp) {
                buf[i2] = '0';
            }
        }
        result[1] = field.newDfp(new java.lang.String(buf));
        return result;
    }

    protected static org.apache.commons.math.dfp.Dfp[] split(org.apache.commons.math.dfp.Dfp a) {
        org.apache.commons.math.dfp.Dfp[] result = new org.apache.commons.math.dfp.Dfp[2];
        org.apache.commons.math.dfp.Dfp shift = a.multiply(a.power10K(a.getRadixDigits() / 2));
        result[0] = a.add(shift).subtract(shift);
        result[1] = a.subtract(result[0]);
        return result;
    }

    protected static org.apache.commons.math.dfp.Dfp[] splitMult(org.apache.commons.math.dfp.Dfp[] a, org.apache.commons.math.dfp.Dfp[] b) {
        org.apache.commons.math.dfp.Dfp[] result = {a[0].multiply(b[0]), a[0].getZero()};
        if (result[0].classify() != 1 && !result[0].equals(result[1])) {
            result[1] = a[0].multiply(b[1]).add(a[1].multiply(b[0])).add(a[1].multiply(b[1]));
            return result;
        }
        return result;
    }

    protected static org.apache.commons.math.dfp.Dfp[] splitDiv(org.apache.commons.math.dfp.Dfp[] a, org.apache.commons.math.dfp.Dfp[] b) {
        org.apache.commons.math.dfp.Dfp[] result = {a[0].divide(b[0]), a[1].multiply(b[0]).subtract(a[0].multiply(b[1]))};
        result[1] = result[1].divide(b[0].multiply(b[0]).add(b[0].multiply(b[1])));
        return result;
    }

    protected static org.apache.commons.math.dfp.Dfp splitPow(org.apache.commons.math.dfp.Dfp[] base, int a) {
        int prevtrial;
        boolean invert = false;
        org.apache.commons.math.dfp.Dfp[] r = new org.apache.commons.math.dfp.Dfp[2];
        org.apache.commons.math.dfp.Dfp[] result = {base[0].getOne(), base[0].getZero()};
        if (a == 0) {
            return result[0].add(result[1]);
        }
        if (a < 0) {
            invert = true;
            a = -a;
        }
        do {
            r[0] = new org.apache.commons.math.dfp.Dfp(base[0]);
            r[1] = new org.apache.commons.math.dfp.Dfp(base[1]);
            int trial = 1;
            while (true) {
                prevtrial = trial;
                trial *= 2;
                if (trial > a) {
                    break;
                }
                r = splitMult(r, r);
            }
            a -= prevtrial;
            result = splitMult(result, r);
        } while (a >= 1);
        result[0] = result[0].add(result[1]);
        if (invert) {
            result[0] = base[0].getOne().divide(result[0]);
        }
        return result[0];
    }

    public static org.apache.commons.math.dfp.Dfp pow(org.apache.commons.math.dfp.Dfp base, int a) {
        org.apache.commons.math.dfp.Dfp prevr;
        int prevtrial;
        boolean invert = false;
        org.apache.commons.math.dfp.Dfp result = base.getOne();
        if (a == 0) {
            return result;
        }
        if (a < 0) {
            invert = true;
            a = -a;
        }
        do {
            org.apache.commons.math.dfp.Dfp r = new org.apache.commons.math.dfp.Dfp(base);
            int trial = 1;
            do {
                prevr = new org.apache.commons.math.dfp.Dfp(r);
                prevtrial = trial;
                r = r.multiply(r);
                trial *= 2;
            } while (a > trial);
            a -= prevtrial;
            result = result.multiply(prevr);
        } while (a >= 1);
        if (invert) {
            result = base.getOne().divide(result);
        }
        return base.newInstance(result);
    }

    /* JADX WARN: Type inference failed for: r3v2, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp exp(org.apache.commons.math.dfp.Dfp a) {
        org.apache.commons.math.dfp.Dfp inta = a.rint();
        org.apache.commons.math.dfp.Dfp fraca = a.subtract(inta);
        int ia = inta.intValue();
        if (ia > 2147483646) {
            return a.newInstance((byte) 1, (byte) 1);
        }
        if (ia < -2147483646) {
            return a.newInstance();
        }
        org.apache.commons.math.dfp.Dfp einta = splitPow(a.getField().getESplit(), ia);
        org.apache.commons.math.dfp.Dfp efraca = expInternal(fraca);
        return einta.multiply(efraca);
    }

    protected static org.apache.commons.math.dfp.Dfp expInternal(org.apache.commons.math.dfp.Dfp a) {
        org.apache.commons.math.dfp.Dfp y = a.getOne();
        org.apache.commons.math.dfp.Dfp x = a.getOne();
        org.apache.commons.math.dfp.Dfp fact = a.getOne();
        org.apache.commons.math.dfp.Dfp py = new org.apache.commons.math.dfp.Dfp(y);
        for (int i = 1; i < 90; i++) {
            x = x.multiply(a);
            fact = fact.divide(i);
            y = y.add(x.multiply(fact));
            if (y.equals(py)) {
                break;
            }
            py = new org.apache.commons.math.dfp.Dfp(y);
        }
        return y;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r8v16, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r8v27, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp log(org.apache.commons.math.dfp.Dfp a) {
        int p2 = 0;
        if (a.equals(a.getZero()) || a.lessThan(a.getZero()) || a.isNaN()) {
            a.getField().setIEEEFlagsBits(1);
            return a.dotrap(1, "ln", a, a.newInstance((byte) 1, (byte) 3));
        }
        if (a.classify() == 1) {
            return a;
        }
        org.apache.commons.math.dfp.Dfp x = new org.apache.commons.math.dfp.Dfp(a);
        int lr = x.log10K();
        org.apache.commons.math.dfp.Dfp x2 = x.divide(pow(a.newInstance(10000), lr));
        int ix = x2.floor().intValue();
        while (ix > 2) {
            ix >>= 1;
            p2++;
        }
        org.apache.commons.math.dfp.Dfp[] spx = split(x2);
        org.apache.commons.math.dfp.Dfp[] spy = new org.apache.commons.math.dfp.Dfp[2];
        spy[0] = pow(a.getTwo(), p2);
        spx[0] = spx[0].divide(spy[0]);
        spx[1] = spx[1].divide(spy[0]);
        spy[0] = a.newInstance("1.33333");
        while (spx[0].add(spx[1]).greaterThan(spy[0])) {
            spx[0] = spx[0].divide(2);
            spx[1] = spx[1].divide(2);
            p2++;
        }
        org.apache.commons.math.dfp.Dfp[] spz = logInternal(spx);
        spx[0] = a.newInstance(new java.lang.StringBuilder().append((lr * 4) + p2).toString());
        spx[1] = a.getZero();
        org.apache.commons.math.dfp.Dfp[] spy2 = splitMult(a.getField().getLn2Split(), spx);
        spz[0] = spz[0].add(spy2[0]);
        spz[1] = spz[1].add(spy2[1]);
        spx[0] = a.newInstance(new java.lang.StringBuilder().append(lr * 4).toString());
        spx[1] = a.getZero();
        org.apache.commons.math.dfp.Dfp[] spy3 = splitMult(a.getField().getLn5Split(), spx);
        spz[0] = spz[0].add(spy3[0]);
        spz[1] = spz[1].add(spy3[1]);
        return a.newInstance(spz[0].add(spz[1]));
    }

    protected static org.apache.commons.math.dfp.Dfp[] logInternal(org.apache.commons.math.dfp.Dfp[] a) {
        org.apache.commons.math.dfp.Dfp t = a[0].divide(4).add(a[1].divide(4));
        org.apache.commons.math.dfp.Dfp x = t.add(a[0].newInstance("-0.25")).divide(t.add(a[0].newInstance("0.25")));
        org.apache.commons.math.dfp.Dfp y = new org.apache.commons.math.dfp.Dfp(x);
        org.apache.commons.math.dfp.Dfp num = new org.apache.commons.math.dfp.Dfp(x);
        org.apache.commons.math.dfp.Dfp py = new org.apache.commons.math.dfp.Dfp(y);
        int den = 1;
        for (int i = 0; i < 10000; i++) {
            num = num.multiply(x).multiply(x);
            den += 2;
            y = y.add(num.divide(den));
            if (y.equals(py)) {
                break;
            }
            py = new org.apache.commons.math.dfp.Dfp(y);
        }
        return split(y.multiply(a[0].getTwo()));
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r0v3, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r1v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r2v64, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r7v23, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r7v25, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r7v3, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r8v1, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r9v0, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp pow(org.apache.commons.math.dfp.Dfp x, org.apache.commons.math.dfp.Dfp y) {
        org.apache.commons.math.dfp.Dfp a;
        if (x.getField().getRadixDigits() != y.getField().getRadixDigits()) {
            x.getField().setIEEEFlagsBits(1);
            org.apache.commons.math.dfp.Dfp result = x.newInstance(x.getZero());
            result.nans = (byte) 3;
            return x.dotrap(1, POW_TRAP, x, result);
        }
        org.apache.commons.math.dfp.Dfp zero = x.getZero();
        org.apache.commons.math.dfp.Dfp one = x.getOne();
        org.apache.commons.math.dfp.Dfp two = x.getTwo();
        boolean invert = false;
        if (y.equals(zero)) {
            return x.newInstance(one);
        }
        if (y.equals(one)) {
            if (x.isNaN()) {
                x.getField().setIEEEFlagsBits(1);
                return x.dotrap(1, POW_TRAP, x, x);
            }
            return x;
        }
        if (x.isNaN() || y.isNaN()) {
            x.getField().setIEEEFlagsBits(1);
            return x.dotrap(1, POW_TRAP, x, x.newInstance((byte) 1, (byte) 3));
        }
        if (x.equals(zero)) {
            if (org.apache.commons.math.dfp.Dfp.copysign(one, x).greaterThan(zero)) {
                if (y.greaterThan(zero)) {
                    return x.newInstance(zero);
                }
                return x.newInstance(x.newInstance((byte) 1, (byte) 1));
            }
            if (y.classify() == 0 && y.rint().equals(y) && !y.remainder(two).equals(zero)) {
                if (y.greaterThan(zero)) {
                    return x.newInstance(zero.negate());
                }
                return x.newInstance(x.newInstance((byte) -1, (byte) 1));
            }
            if (y.greaterThan(zero)) {
                return x.newInstance(zero);
            }
            return x.newInstance(x.newInstance((byte) 1, (byte) 1));
        }
        if (x.lessThan(zero)) {
            x = x.negate();
            invert = true;
        }
        if (x.greaterThan(one) && y.classify() == 1) {
            if (y.greaterThan(zero)) {
                return y;
            }
            return x.newInstance(zero);
        }
        if (x.lessThan(one) && y.classify() == 1) {
            if (y.greaterThan(zero)) {
                return x.newInstance(zero);
            }
            return x.newInstance(org.apache.commons.math.dfp.Dfp.copysign(y, one));
        }
        if (x.equals(one) && y.classify() == 1) {
            x.getField().setIEEEFlagsBits(1);
            return x.dotrap(1, POW_TRAP, x, x.newInstance((byte) 1, (byte) 3));
        }
        if (x.classify() == 1) {
            if (invert) {
                if (y.classify() == 0 && y.rint().equals(y) && !y.remainder(two).equals(zero)) {
                    if (y.greaterThan(zero)) {
                        return x.newInstance(x.newInstance((byte) -1, (byte) 1));
                    }
                    return x.newInstance(zero.negate());
                }
                if (y.greaterThan(zero)) {
                    return x.newInstance(x.newInstance((byte) 1, (byte) 1));
                }
                return x.newInstance(zero);
            }
            if (y.greaterThan(zero)) {
                return x;
            }
            return x.newInstance(zero);
        }
        if (invert && !y.rint().equals(y)) {
            x.getField().setIEEEFlagsBits(1);
            return x.dotrap(1, POW_TRAP, x, x.newInstance((byte) 1, (byte) 3));
        }
        if (y.lessThan(x.newInstance(100000000)) && y.greaterThan(x.newInstance(-100000000))) {
            org.apache.commons.math.dfp.Dfp u = y.rint();
            int ui = u.intValue();
            org.apache.commons.math.dfp.Dfp v = y.subtract(u);
            if (v.unequal(zero)) {
                org.apache.commons.math.dfp.Dfp a2 = v.multiply(log(x));
                org.apache.commons.math.dfp.Dfp b = a2.divide(x.getField().getLn2()).rint();
                org.apache.commons.math.dfp.Dfp c = a2.subtract(b.multiply(x.getField().getLn2()));
                org.apache.commons.math.dfp.Dfp r = splitPow(split(x), ui);
                a = r.multiply(pow(two, b.intValue())).multiply(exp(c));
            } else {
                a = splitPow(split(x), ui);
            }
        } else {
            a = exp(log(x).multiply(y));
        }
        if (invert && y.rint().equals(y) && !y.remainder(two).equals(zero)) {
            a = a.negate();
        }
        return x.newInstance(a);
    }

    protected static org.apache.commons.math.dfp.Dfp sinInternal(org.apache.commons.math.dfp.Dfp[] a) {
        org.apache.commons.math.dfp.Dfp c = a[0].add(a[1]);
        org.apache.commons.math.dfp.Dfp y = c;
        org.apache.commons.math.dfp.Dfp c2 = c.multiply(c);
        org.apache.commons.math.dfp.Dfp x = y;
        org.apache.commons.math.dfp.Dfp fact = a[0].getOne();
        org.apache.commons.math.dfp.Dfp py = new org.apache.commons.math.dfp.Dfp(y);
        for (int i = 3; i < 90; i += 2) {
            x = x.multiply(c2).negate();
            fact = fact.divide((i - 1) * i);
            y = y.add(x.multiply(fact));
            if (y.equals(py)) {
                break;
            }
            py = new org.apache.commons.math.dfp.Dfp(y);
        }
        return y;
    }

    protected static org.apache.commons.math.dfp.Dfp cosInternal(org.apache.commons.math.dfp.Dfp[] a) {
        org.apache.commons.math.dfp.Dfp one = a[0].getOne();
        org.apache.commons.math.dfp.Dfp x = one;
        org.apache.commons.math.dfp.Dfp y = one;
        org.apache.commons.math.dfp.Dfp c = a[0].add(a[1]);
        org.apache.commons.math.dfp.Dfp c2 = c.multiply(c);
        org.apache.commons.math.dfp.Dfp fact = one;
        org.apache.commons.math.dfp.Dfp py = new org.apache.commons.math.dfp.Dfp(y);
        for (int i = 2; i < 90; i += 2) {
            x = x.multiply(c2).negate();
            fact = fact.divide((i - 1) * i);
            y = y.add(x.multiply(fact));
            if (y.equals(py)) {
                break;
            }
            py = new org.apache.commons.math.dfp.Dfp(y);
        }
        return y;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r8v0, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp sin(org.apache.commons.math.dfp.Dfp a) {
        org.apache.commons.math.dfp.Dfp y;
        org.apache.commons.math.dfp.Dfp pi = a.getField().getPi();
        org.apache.commons.math.dfp.Dfp zero = a.getField().getZero();
        boolean neg = false;
        org.apache.commons.math.dfp.Dfp x = a.remainder(pi.multiply(2));
        if (x.lessThan(zero)) {
            x = x.negate();
            neg = true;
        }
        if (x.greaterThan(pi.divide(2))) {
            x = pi.subtract(x);
        }
        if (x.lessThan(pi.divide(4))) {
            org.apache.commons.math.dfp.Dfp[] c = {x, zero};
            y = sinInternal(split(x));
        } else {
            org.apache.commons.math.dfp.Dfp[] piSplit = a.getField().getPiSplit();
            org.apache.commons.math.dfp.Dfp[] c2 = {piSplit[0].divide(2).subtract(x), piSplit[1].divide(2)};
            y = cosInternal(c2);
        }
        if (neg) {
            y = y.negate();
        }
        return a.newInstance(y);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r8v0, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp cos(org.apache.commons.math.dfp.Dfp a) {
        org.apache.commons.math.dfp.Dfp y;
        org.apache.commons.math.dfp.Dfp pi = a.getField().getPi();
        org.apache.commons.math.dfp.Dfp zero = a.getField().getZero();
        boolean neg = false;
        org.apache.commons.math.dfp.Dfp x = a.remainder(pi.multiply(2));
        if (x.lessThan(zero)) {
            x = x.negate();
        }
        if (x.greaterThan(pi.divide(2))) {
            x = pi.subtract(x);
            neg = true;
        }
        if (x.lessThan(pi.divide(4))) {
            org.apache.commons.math.dfp.Dfp[] c = {x, zero};
            y = cosInternal(c);
        } else {
            org.apache.commons.math.dfp.Dfp[] piSplit = a.getField().getPiSplit();
            org.apache.commons.math.dfp.Dfp[] c2 = {piSplit[0].divide(2).subtract(x), piSplit[1].divide(2)};
            y = sinInternal(c2);
        }
        if (neg) {
            y = y.negate();
        }
        return a.newInstance(y);
    }

    public static org.apache.commons.math.dfp.Dfp tan(org.apache.commons.math.dfp.Dfp a) {
        return sin(a).divide(cos(a));
    }

    protected static org.apache.commons.math.dfp.Dfp atanInternal(org.apache.commons.math.dfp.Dfp a) {
        org.apache.commons.math.dfp.Dfp y = new org.apache.commons.math.dfp.Dfp(a);
        org.apache.commons.math.dfp.Dfp x = new org.apache.commons.math.dfp.Dfp(y);
        org.apache.commons.math.dfp.Dfp py = new org.apache.commons.math.dfp.Dfp(y);
        for (int i = 3; i < 90; i += 2) {
            x = x.multiply(a).multiply(a).negate();
            y = y.add(x.divide(i));
            if (y.equals(py)) {
                break;
            }
            py = new org.apache.commons.math.dfp.Dfp(y);
        }
        return y;
    }

    /* JADX WARN: Type inference failed for: r3v0, types: [org.apache.commons.math.dfp.DfpField] */
    /* JADX WARN: Type inference failed for: r4v0, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp atan(org.apache.commons.math.dfp.Dfp a) {
        org.apache.commons.math.dfp.Dfp zero = a.getField().getZero();
        org.apache.commons.math.dfp.Dfp one = a.getField().getOne();
        org.apache.commons.math.dfp.Dfp[] sqr2Split = a.getField().getSqr2Split();
        org.apache.commons.math.dfp.Dfp[] piSplit = a.getField().getPiSplit();
        boolean recp = false;
        boolean neg = false;
        boolean sub = false;
        org.apache.commons.math.dfp.Dfp ty = sqr2Split[0].subtract(one).add(sqr2Split[1]);
        org.apache.commons.math.dfp.Dfp x = new org.apache.commons.math.dfp.Dfp(a);
        if (x.lessThan(zero)) {
            neg = true;
            x = x.negate();
        }
        if (x.greaterThan(one)) {
            recp = true;
            x = one.divide(x);
        }
        if (x.greaterThan(ty)) {
            sub = true;
            org.apache.commons.math.dfp.Dfp[] sty = {sqr2Split[0].subtract(one), sqr2Split[1]};
            org.apache.commons.math.dfp.Dfp[] xs = split(x);
            org.apache.commons.math.dfp.Dfp[] ds = splitMult(xs, sty);
            ds[0] = ds[0].add(one);
            xs[0] = xs[0].subtract(sty[0]);
            xs[1] = xs[1].subtract(sty[1]);
            org.apache.commons.math.dfp.Dfp[] xs2 = splitDiv(xs, ds);
            x = xs2[0].add(xs2[1]);
        }
        org.apache.commons.math.dfp.Dfp y = atanInternal(x);
        if (sub) {
            y = y.add(piSplit[0].divide(8)).add(piSplit[1].divide(8));
        }
        if (recp) {
            y = piSplit[0].divide(2).subtract(y).add(piSplit[1].divide(2));
        }
        if (neg) {
            y = y.negate();
        }
        return a.newInstance(y);
    }

    public static org.apache.commons.math.dfp.Dfp asin(org.apache.commons.math.dfp.Dfp a) {
        return atan(a.divide(a.getOne().subtract(a.multiply(a)).sqrt()));
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [org.apache.commons.math.dfp.DfpField] */
    public static org.apache.commons.math.dfp.Dfp acos(org.apache.commons.math.dfp.Dfp a) {
        boolean negative = false;
        if (a.lessThan(a.getZero())) {
            negative = true;
        }
        org.apache.commons.math.dfp.Dfp a2 = org.apache.commons.math.dfp.Dfp.copysign(a, a.getOne());
        org.apache.commons.math.dfp.Dfp result = atan(a2.getOne().subtract(a2.multiply(a2)).sqrt().divide(a2));
        if (negative) {
            result = a2.getField().getPi().subtract(result);
        }
        return a2.newInstance(result);
    }
}
