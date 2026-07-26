package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class MathInternalError extends org.apache.commons.math.exception.MathIllegalStateException {
    private static final java.lang.String REPORT_URL = "https://issues.apache.org/jira/browse/MATH";
    private static final long serialVersionUID = -6276776513966934846L;

    public MathInternalError() {
        super(org.apache.commons.math.exception.util.LocalizedFormats.INTERNAL_ERROR, REPORT_URL);
    }

    public MathInternalError(java.lang.Throwable cause) {
        super(org.apache.commons.math.exception.util.LocalizedFormats.INTERNAL_ERROR, REPORT_URL);
    }
}
