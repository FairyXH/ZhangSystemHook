package org.apache.commons.math.analysis;

/* JADX INFO: loaded from: classes4.dex */
public interface DifferentiableMultivariateRealFunction extends org.apache.commons.math.analysis.MultivariateRealFunction {
    org.apache.commons.math.analysis.MultivariateVectorialFunction gradient();

    org.apache.commons.math.analysis.MultivariateRealFunction partialDerivative(int i);
}
