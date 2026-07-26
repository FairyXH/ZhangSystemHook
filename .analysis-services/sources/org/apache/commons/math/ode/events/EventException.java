package org.apache.commons.math.ode.events;

/* JADX INFO: loaded from: classes4.dex */
public class EventException extends org.apache.commons.math.MathException {
    private static final long serialVersionUID = -898215297400035290L;

    @java.lang.Deprecated
    public EventException(java.lang.String specifier, java.lang.Object... parts) {
        super(specifier, parts);
    }

    public EventException(org.apache.commons.math.exception.util.Localizable specifier, java.lang.Object... parts) {
        super(specifier, parts);
    }

    public EventException(java.lang.Throwable cause) {
        super(cause);
    }
}
