package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class GaussianParametersGuesser {
    private final org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] observations;
    private double[] parameters;

    public GaussianParametersGuesser(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] observations) {
        if (observations == null) {
            throw new org.apache.commons.math.exception.NullArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.INPUT_ARRAY);
        }
        if (observations.length < 3) {
            throw new org.apache.commons.math.exception.NumberIsTooSmallException(java.lang.Integer.valueOf(observations.length), 3, true);
        }
        this.observations = (org.apache.commons.math.optimization.fitting.WeightedObservedPoint[]) observations.clone();
    }

    public double[] guess() {
        if (this.parameters == null) {
            this.parameters = basicGuess(this.observations);
        }
        return (double[]) this.parameters.clone();
    }

    private double[] basicGuess(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] points) {
        double fwhmX2;
        java.util.Arrays.sort(points, createWeightedObservedPointComparator());
        double[] params = new double[4];
        int minYIdx = findMinY(points);
        params[0] = points[minYIdx].getY();
        int maxYIdx = findMaxY(points);
        params[1] = points[maxYIdx].getY();
        params[2] = points[maxYIdx].getX();
        try {
            double halfY = params[0] + ((params[1] - params[0]) / 2.0d);
            double fwhmX1 = interpolateXAtY(points, maxYIdx, -1, halfY);
            double fwhmX22 = interpolateXAtY(points, maxYIdx, 1, halfY);
            fwhmX2 = fwhmX22 - fwhmX1;
        } catch (org.apache.commons.math.exception.OutOfRangeException e) {
            fwhmX2 = points[points.length - 1].getX() - points[0].getX();
        }
        params[3] = fwhmX2 / (java.lang.Math.sqrt(java.lang.Math.log(2.0d) * 2.0d) * 2.0d);
        return params;
    }

    private int findMinY(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] points) {
        int minYIdx = 0;
        for (int i = 1; i < points.length; i++) {
            if (points[i].getY() < points[minYIdx].getY()) {
                minYIdx = i;
            }
        }
        return minYIdx;
    }

    private int findMaxY(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] points) {
        int maxYIdx = 0;
        for (int i = 1; i < points.length; i++) {
            if (points[i].getY() > points[maxYIdx].getY()) {
                maxYIdx = i;
            }
        }
        return maxYIdx;
    }

    private double interpolateXAtY(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] points, int startIdx, int idxStep, double y) throws org.apache.commons.math.exception.OutOfRangeException {
        if (idxStep == 0) {
            throw new org.apache.commons.math.exception.ZeroException();
        }
        org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] twoPoints = getInterpolationPointsForY(points, startIdx, idxStep, y);
        org.apache.commons.math.optimization.fitting.WeightedObservedPoint pointA = twoPoints[0];
        org.apache.commons.math.optimization.fitting.WeightedObservedPoint pointB = twoPoints[1];
        if (pointA.getY() == y) {
            return pointA.getX();
        }
        if (pointB.getY() == y) {
            return pointB.getX();
        }
        return pointA.getX() + (((y - pointA.getY()) * (pointB.getX() - pointA.getX())) / (pointB.getY() - pointA.getY()));
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0044, code lost:
    
        r2 = Double.POSITIVE_INFINITY;
        r4 = Double.NEGATIVE_INFINITY;
        r0 = r11.length;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0049, code lost:
    
        if (r1 >= r0) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x004b, code lost:
    
        r6 = r11[r1];
        r2 = java.lang.Math.min(r2, r6.getY());
        r4 = java.lang.Math.max(r4, r6.getY());
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0071, code lost:
    
        throw new org.apache.commons.math.exception.OutOfRangeException(java.lang.Double.valueOf(r14), java.lang.Double.valueOf(r2), java.lang.Double.valueOf(r4));
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0042 A[LOOP:0: B:4:0x0003->B:17:0x0042, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0024 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] getInterpolationPointsForY(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] r11, int r12, int r13, double r14) throws org.apache.commons.math.exception.OutOfRangeException {
        /*
            r10 = this;
            if (r13 == 0) goto L72
            r0 = r12
        L3:
            r1 = 0
            int r2 = r0 + r13
            if (r13 >= 0) goto Lb
            if (r2 < 0) goto L44
            goto Le
        Lb:
            int r3 = r11.length
            if (r2 >= r3) goto L44
        Le:
            r2 = r11[r0]
            double r6 = r2.getY()
            int r2 = r0 + r13
            r2 = r11[r2]
            double r8 = r2.getY()
            r3 = r10
            r4 = r14
            boolean r2 = r3.isBetween(r4, r6, r8)
            if (r2 == 0) goto L42
            r2 = 2
            r3 = 1
            if (r13 >= 0) goto L35
            org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] r2 = new org.apache.commons.math.optimization.fitting.WeightedObservedPoint[r2]
            int r4 = r0 + r13
            r4 = r11[r4]
            r2[r1] = r4
            r1 = r11[r0]
            r2[r3] = r1
            goto L41
        L35:
            org.apache.commons.math.optimization.fitting.WeightedObservedPoint[] r2 = new org.apache.commons.math.optimization.fitting.WeightedObservedPoint[r2]
            r4 = r11[r0]
            r2[r1] = r4
            int r1 = r0 + r13
            r1 = r11[r1]
            r2[r3] = r1
        L41:
            return r2
        L42:
            int r0 = r0 + r13
            goto L3
        L44:
            r2 = 9218868437227405312(0x7ff0000000000000, double:Infinity)
            r4 = -4503599627370496(0xfff0000000000000, double:-Infinity)
            int r0 = r11.length
        L49:
            if (r1 >= r0) goto L60
            r6 = r11[r1]
            double r7 = r6.getY()
            double r2 = java.lang.Math.min(r2, r7)
            double r7 = r6.getY()
            double r4 = java.lang.Math.max(r4, r7)
            int r1 = r1 + 1
            goto L49
        L60:
            org.apache.commons.math.exception.OutOfRangeException r0 = new org.apache.commons.math.exception.OutOfRangeException
            java.lang.Double r1 = java.lang.Double.valueOf(r14)
            java.lang.Double r6 = java.lang.Double.valueOf(r2)
            java.lang.Double r7 = java.lang.Double.valueOf(r4)
            r0.<init>(r1, r6, r7)
            throw r0
        L72:
            org.apache.commons.math.exception.ZeroException r0 = new org.apache.commons.math.exception.ZeroException
            r0.<init>()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math.optimization.fitting.GaussianParametersGuesser.getInterpolationPointsForY(org.apache.commons.math.optimization.fitting.WeightedObservedPoint[], int, int, double):org.apache.commons.math.optimization.fitting.WeightedObservedPoint[]");
    }

    private boolean isBetween(double value, double boundary1, double boundary2) {
        return (value >= boundary1 && value <= boundary2) || (value >= boundary2 && value <= boundary1);
    }

    private java.util.Comparator<org.apache.commons.math.optimization.fitting.WeightedObservedPoint> createWeightedObservedPointComparator() {
        return new java.util.Comparator<org.apache.commons.math.optimization.fitting.WeightedObservedPoint>() { // from class: org.apache.commons.math.optimization.fitting.GaussianParametersGuesser.1
            @Override // java.util.Comparator
            public int compare(org.apache.commons.math.optimization.fitting.WeightedObservedPoint p1, org.apache.commons.math.optimization.fitting.WeightedObservedPoint p2) {
                if (p1 == null && p2 == null) {
                    return 0;
                }
                if (p1 == null) {
                    return -1;
                }
                if (p2 == null) {
                    return 1;
                }
                if (p1.getX() < p2.getX()) {
                    return -1;
                }
                if (p1.getX() > p2.getX()) {
                    return 1;
                }
                if (p1.getY() < p2.getY()) {
                    return -1;
                }
                if (p1.getY() > p2.getY()) {
                    return 1;
                }
                if (p1.getWeight() < p2.getWeight()) {
                    return -1;
                }
                if (p1.getWeight() <= p2.getWeight()) {
                    return 0;
                }
                return 1;
            }
        };
    }
}
