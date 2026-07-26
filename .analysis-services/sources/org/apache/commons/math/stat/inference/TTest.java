package org.apache.commons.math.stat.inference;

/* JADX INFO: loaded from: classes4.dex */
public interface TTest {
    double homoscedasticT(org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary2) throws java.lang.IllegalArgumentException;

    double homoscedasticT(double[] dArr, double[] dArr2) throws java.lang.IllegalArgumentException;

    double homoscedasticTTest(org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double homoscedasticTTest(double[] dArr, double[] dArr2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean homoscedasticTTest(double[] dArr, double[] dArr2, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double pairedT(double[] dArr, double[] dArr2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double pairedTTest(double[] dArr, double[] dArr2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean pairedTTest(double[] dArr, double[] dArr2, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double t(double d, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary) throws java.lang.IllegalArgumentException;

    double t(double d, double[] dArr) throws java.lang.IllegalArgumentException;

    double t(org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary2) throws java.lang.IllegalArgumentException;

    double t(double[] dArr, double[] dArr2) throws java.lang.IllegalArgumentException;

    double tTest(double d, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double tTest(double d, double[] dArr) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double tTest(org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    double tTest(double[] dArr, double[] dArr2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean tTest(double d, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary, double d2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean tTest(double d, double[] dArr, double d2) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean tTest(org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary, org.apache.commons.math.stat.descriptive.StatisticalSummary statisticalSummary2, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;

    boolean tTest(double[] dArr, double[] dArr2, double d) throws org.apache.commons.math.MathException, java.lang.IllegalArgumentException;
}
