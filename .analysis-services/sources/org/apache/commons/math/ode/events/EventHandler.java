package org.apache.commons.math.ode.events;

/* JADX INFO: loaded from: classes4.dex */
public interface EventHandler {
    public static final int CONTINUE = 3;
    public static final int RESET_DERIVATIVES = 2;
    public static final int RESET_STATE = 1;
    public static final int STOP = 0;

    int eventOccurred(double d, double[] dArr, boolean z) throws org.apache.commons.math.ode.events.EventException;

    double g(double d, double[] dArr) throws org.apache.commons.math.ode.events.EventException;

    void resetState(double d, double[] dArr) throws org.apache.commons.math.ode.events.EventException;
}
