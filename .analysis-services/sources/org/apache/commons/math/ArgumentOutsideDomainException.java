package org.apache.commons.math;

/* JADX INFO: loaded from: classes4.dex */
public class ArgumentOutsideDomainException extends org.apache.commons.math.FunctionEvaluationException {
    private static final long serialVersionUID = -4965972841162580234L;

    public ArgumentOutsideDomainException(double argument, double lower, double upper) {
        super(argument, org.apache.commons.math.exception.util.LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, java.lang.Double.valueOf(argument), java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper));
    }
}
