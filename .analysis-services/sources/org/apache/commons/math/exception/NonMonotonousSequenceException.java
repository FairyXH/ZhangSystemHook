package org.apache.commons.math.exception;

/* JADX INFO: loaded from: classes4.dex */
public class NonMonotonousSequenceException extends org.apache.commons.math.exception.MathIllegalNumberException {
    private static final long serialVersionUID = 3596849179428944575L;
    private final org.apache.commons.math.util.MathUtils.OrderDirection direction;
    private final int index;
    private final java.lang.Number previous;
    private final boolean strict;

    public NonMonotonousSequenceException(java.lang.Number wrong, java.lang.Number previous, int index) {
        this(wrong, previous, index, org.apache.commons.math.util.MathUtils.OrderDirection.INCREASING, true);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public NonMonotonousSequenceException(java.lang.Number wrong, java.lang.Number previous, int index, org.apache.commons.math.util.MathUtils.OrderDirection direction, boolean strict) {
        org.apache.commons.math.exception.util.LocalizedFormats localizedFormats;
        if (direction == org.apache.commons.math.util.MathUtils.OrderDirection.INCREASING) {
            if (strict) {
                localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NOT_STRICTLY_INCREASING_SEQUENCE;
            } else {
                localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NOT_INCREASING_SEQUENCE;
            }
        } else if (strict) {
            localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NOT_STRICTLY_DECREASING_SEQUENCE;
        } else {
            localizedFormats = org.apache.commons.math.exception.util.LocalizedFormats.NOT_DECREASING_SEQUENCE;
        }
        super(localizedFormats, wrong, previous, java.lang.Integer.valueOf(index), java.lang.Integer.valueOf(index - 1));
        this.direction = direction;
        this.strict = strict;
        this.index = index;
        this.previous = previous;
    }

    public org.apache.commons.math.util.MathUtils.OrderDirection getDirection() {
        return this.direction;
    }

    public boolean getStrict() {
        return this.strict;
    }

    public int getIndex() {
        return this.index;
    }

    public java.lang.Number getPrevious() {
        return this.previous;
    }
}
