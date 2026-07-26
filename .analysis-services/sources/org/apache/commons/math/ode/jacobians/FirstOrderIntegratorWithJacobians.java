package org.apache.commons.math.ode.jacobians;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.Deprecated
public class FirstOrderIntegratorWithJacobians {
    private int evaluations;
    private final org.apache.commons.math.ode.FirstOrderIntegrator integrator;
    private int maxEvaluations;
    private final org.apache.commons.math.ode.jacobians.ODEWithJacobians ode;

    static /* synthetic */ int access$104(org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians x0) {
        int i = x0.evaluations + 1;
        x0.evaluations = i;
        return i;
    }

    static /* synthetic */ int access$112(org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians x0, int x1) {
        int i = x0.evaluations + x1;
        x0.evaluations = i;
        return i;
    }

    public FirstOrderIntegratorWithJacobians(org.apache.commons.math.ode.FirstOrderIntegrator integrator, org.apache.commons.math.ode.jacobians.ParameterizedODE ode, double[] p, double[] hY, double[] hP) {
        checkDimension(ode.getDimension(), hY);
        checkDimension(ode.getParametersDimension(), p);
        checkDimension(ode.getParametersDimension(), hP);
        this.integrator = integrator;
        this.ode = new org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.FiniteDifferencesWrapper(ode, p, hY, hP);
        setMaxEvaluations(-1);
    }

    public FirstOrderIntegratorWithJacobians(org.apache.commons.math.ode.FirstOrderIntegrator integrator, org.apache.commons.math.ode.jacobians.ODEWithJacobians ode) {
        this.integrator = integrator;
        this.ode = ode;
        setMaxEvaluations(-1);
    }

    public void addStepHandler(org.apache.commons.math.ode.jacobians.StepHandlerWithJacobians handler) {
        int n = this.ode.getDimension();
        int k = this.ode.getParametersDimension();
        this.integrator.addStepHandler(new org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.StepHandlerWrapper(handler, n, k));
    }

    public java.util.Collection<org.apache.commons.math.ode.jacobians.StepHandlerWithJacobians> getStepHandlers() {
        java.util.Collection<org.apache.commons.math.ode.jacobians.StepHandlerWithJacobians> handlers = new java.util.ArrayList<>();
        for (org.apache.commons.math.ode.sampling.StepHandler handler : this.integrator.getStepHandlers()) {
            if (handler instanceof org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.StepHandlerWrapper) {
                handlers.add(((org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.StepHandlerWrapper) handler).getHandler());
            }
        }
        return handlers;
    }

    public void clearStepHandlers() {
        this.integrator.clearStepHandlers();
    }

    public void addEventHandler(org.apache.commons.math.ode.jacobians.EventHandlerWithJacobians handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        int n = this.ode.getDimension();
        int k = this.ode.getParametersDimension();
        this.integrator.addEventHandler(new org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.EventHandlerWrapper(handler, n, k), maxCheckInterval, convergence, maxIterationCount);
    }

    public java.util.Collection<org.apache.commons.math.ode.jacobians.EventHandlerWithJacobians> getEventHandlers() {
        java.util.Collection<org.apache.commons.math.ode.jacobians.EventHandlerWithJacobians> handlers = new java.util.ArrayList<>();
        for (org.apache.commons.math.ode.events.EventHandler handler : this.integrator.getEventHandlers()) {
            if (handler instanceof org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.EventHandlerWrapper) {
                handlers.add(((org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.EventHandlerWrapper) handler).getHandler());
            }
        }
        return handlers;
    }

    public void clearEventHandlers() {
        this.integrator.clearEventHandlers();
    }

    public double integrate(double t0, double[] y0, double[][] dY0dP, double t, double[] y, double[][] dYdY0, double[][] dYdP) throws org.apache.commons.math.ode.DerivativeException, org.apache.commons.math.ode.IntegratorException {
        int n = this.ode.getDimension();
        int k = this.ode.getParametersDimension();
        checkDimension(n, y0);
        checkDimension(n, y);
        checkDimension(n, dYdY0);
        checkDimension(n, dYdY0[0]);
        if (k != 0) {
            checkDimension(n, dY0dP);
            checkDimension(k, dY0dP[0]);
            checkDimension(n, dYdP);
            checkDimension(k, dYdP[0]);
        }
        double[] z = new double[(n + 1 + k) * n];
        java.lang.System.arraycopy(y0, 0, z, 0, n);
        for (int i = 0; i < n; i++) {
            z[((n + 1) * i) + n] = 1.0d;
            java.lang.System.arraycopy(dY0dP[i], 0, z, ((n + 1) * n) + (i * k), k);
        }
        this.evaluations = 0;
        double stopTime = this.integrator.integrate(new org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.MappingWrapper(), t0, z, t, z);
        dispatchCompoundState(z, y, dYdY0, dYdP);
        return stopTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dispatchCompoundState(double[] z, double[] y, double[][] dydy0, double[][] dydp) {
        int n = y.length;
        int k = dydp[0].length;
        java.lang.System.arraycopy(z, 0, y, 0, n);
        for (int i = 0; i < n; i++) {
            java.lang.System.arraycopy(z, (i + 1) * n, dydy0[i], 0, n);
        }
        for (int i2 = 0; i2 < n; i2++) {
            java.lang.System.arraycopy(z, ((n + 1) * n) + (i2 * k), dydp[i2], 0, k);
        }
    }

    public double getCurrentStepStart() {
        return this.integrator.getCurrentStepStart();
    }

    public double getCurrentSignedStepsize() {
        return this.integrator.getCurrentSignedStepsize();
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.maxEvaluations = maxEvaluations < 0 ? Integer.MAX_VALUE : maxEvaluations;
    }

    public int getMaxEvaluations() {
        return this.maxEvaluations;
    }

    public int getEvaluations() {
        return this.evaluations;
    }

    private void checkDimension(int expected, java.lang.Object array) throws java.lang.IllegalArgumentException {
        int arrayDimension = array == null ? 0 : java.lang.reflect.Array.getLength(array);
        if (arrayDimension != expected) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, java.lang.Integer.valueOf(arrayDimension), java.lang.Integer.valueOf(expected));
        }
    }

    private class MappingWrapper implements org.apache.commons.math.ode.ExtendedFirstOrderDifferentialEquations {
        private final double[][] dFdP;
        private final double[][] dFdY;
        private final double[] y;
        private final double[] yDot;

        public MappingWrapper() {
            int n = org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.ode.getDimension();
            int k = org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.ode.getParametersDimension();
            this.y = new double[n];
            this.yDot = new double[n];
            this.dFdY = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
            this.dFdP = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, k);
        }

        @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
        public int getDimension() {
            int n = this.y.length;
            int k = this.dFdP[0].length;
            return (n + 1 + k) * n;
        }

        @Override // org.apache.commons.math.ode.ExtendedFirstOrderDifferentialEquations
        public int getMainSetDimension() {
            return org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.ode.getDimension();
        }

        @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
        public void computeDerivatives(double t, double[] z, double[] zDot) throws org.apache.commons.math.ode.DerivativeException {
            int n = this.y.length;
            int k = this.dFdP[0].length;
            java.lang.System.arraycopy(z, 0, this.y, 0, n);
            if (org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.access$104(org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this) <= org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.maxEvaluations) {
                org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.ode.computeDerivatives(t, this.y, this.yDot);
                org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.ode.computeJacobians(t, this.y, this.yDot, this.dFdY, this.dFdP);
                java.lang.System.arraycopy(this.yDot, 0, zDot, 0, n);
                for (int i = 0; i < n; i++) {
                    double[] dFdYi = this.dFdY[i];
                    for (int j = 0; j < n; j++) {
                        double s = 0.0d;
                        int startIndex = n + j;
                        int zIndex = startIndex;
                        for (int l = 0; l < n; l++) {
                            s += dFdYi[l] * z[zIndex];
                            zIndex += n;
                        }
                        int l2 = i * n;
                        zDot[l2 + startIndex] = s;
                    }
                }
                for (int i2 = 0; i2 < n; i2++) {
                    double[] dFdYi2 = this.dFdY[i2];
                    double[] dFdPi = this.dFdP[i2];
                    for (int j2 = 0; j2 < k; j2++) {
                        double s2 = dFdPi[j2];
                        int startIndex2 = ((n + 1) * n) + j2;
                        int zIndex2 = startIndex2;
                        for (int l3 = 0; l3 < n; l3++) {
                            s2 += dFdYi2[l3] * z[zIndex2];
                            zIndex2 += k;
                        }
                        int l4 = i2 * k;
                        zDot[l4 + startIndex2] = s2;
                    }
                }
                return;
            }
            throw new org.apache.commons.math.ode.DerivativeException(new org.apache.commons.math.MaxEvaluationsExceededException(org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.maxEvaluations));
        }
    }

    private class FiniteDifferencesWrapper implements org.apache.commons.math.ode.jacobians.ODEWithJacobians {
        private final double[] hP;
        private final double[] hY;
        private final org.apache.commons.math.ode.jacobians.ParameterizedODE ode;
        private final double[] p;
        private final double[] tmpDot;

        public FiniteDifferencesWrapper(org.apache.commons.math.ode.jacobians.ParameterizedODE ode, double[] p, double[] hY, double[] hP) {
            this.ode = ode;
            this.p = (double[]) p.clone();
            this.hY = (double[]) hY.clone();
            this.hP = (double[]) hP.clone();
            this.tmpDot = new double[ode.getDimension()];
        }

        @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
        public int getDimension() {
            return this.ode.getDimension();
        }

        @Override // org.apache.commons.math.ode.FirstOrderDifferentialEquations
        public void computeDerivatives(double t, double[] y, double[] yDot) throws org.apache.commons.math.ode.DerivativeException {
            this.ode.computeDerivatives(t, y, yDot);
        }

        @Override // org.apache.commons.math.ode.jacobians.ODEWithJacobians
        public int getParametersDimension() {
            return this.ode.getParametersDimension();
        }

        @Override // org.apache.commons.math.ode.jacobians.ODEWithJacobians
        public void computeJacobians(double t, double[] y, double[] yDot, double[][] dFdY, double[][] dFdP) throws org.apache.commons.math.ode.DerivativeException {
            int n = this.hY.length;
            int k = this.hP.length;
            org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.access$112(org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this, n + k);
            if (org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.evaluations > org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.maxEvaluations) {
                throw new org.apache.commons.math.ode.DerivativeException(new org.apache.commons.math.MaxEvaluationsExceededException(org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.this.maxEvaluations));
            }
            for (int j = 0; j < n; j++) {
                double savedYj = y[j];
                y[j] = y[j] + this.hY[j];
                this.ode.computeDerivatives(t, y, this.tmpDot);
                for (int i = 0; i < n; i++) {
                    dFdY[i][j] = (this.tmpDot[i] - yDot[i]) / this.hY[j];
                }
                y[j] = savedYj;
            }
            for (int j2 = 0; j2 < k; j2++) {
                this.ode.setParameter(j2, this.p[j2] + this.hP[j2]);
                this.ode.computeDerivatives(t, y, this.tmpDot);
                for (int i2 = 0; i2 < n; i2++) {
                    dFdP[i2][j2] = (this.tmpDot[i2] - yDot[i2]) / this.hP[j2];
                }
                this.ode.setParameter(j2, this.p[j2]);
            }
        }
    }

    private static class StepHandlerWrapper implements org.apache.commons.math.ode.sampling.StepHandler {
        private final org.apache.commons.math.ode.jacobians.StepHandlerWithJacobians handler;
        private final int k;
        private final int n;

        public StepHandlerWrapper(org.apache.commons.math.ode.jacobians.StepHandlerWithJacobians handler, int n, int k) {
            this.handler = handler;
            this.n = n;
            this.k = k;
        }

        public org.apache.commons.math.ode.jacobians.StepHandlerWithJacobians getHandler() {
            return this.handler;
        }

        @Override // org.apache.commons.math.ode.sampling.StepHandler
        public void handleStep(org.apache.commons.math.ode.sampling.StepInterpolator interpolator, boolean isLast) throws org.apache.commons.math.ode.DerivativeException {
            this.handler.handleStep(new org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.StepInterpolatorWrapper(interpolator, this.n, this.k), isLast);
        }

        @Override // org.apache.commons.math.ode.sampling.StepHandler
        public boolean requiresDenseOutput() {
            return this.handler.requiresDenseOutput();
        }

        @Override // org.apache.commons.math.ode.sampling.StepHandler
        public void reset() {
            this.handler.reset();
        }
    }

    private static class StepInterpolatorWrapper implements org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians {
        private double[][] dydp;
        private double[][] dydpDot;
        private double[][] dydy0;
        private double[][] dydy0Dot;
        private org.apache.commons.math.ode.sampling.StepInterpolator interpolator;
        private double[] y;
        private double[] yDot;

        public StepInterpolatorWrapper() {
        }

        public StepInterpolatorWrapper(org.apache.commons.math.ode.sampling.StepInterpolator interpolator, int n, int k) {
            this.interpolator = interpolator;
            this.y = new double[n];
            this.dydy0 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
            this.dydp = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, k);
            this.yDot = new double[n];
            this.dydy0Dot = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
            this.dydpDot = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, k);
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public void setInterpolatedTime(double time) {
            this.interpolator.setInterpolatedTime(time);
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public boolean isForward() {
            return this.interpolator.isForward();
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double getPreviousTime() {
            return this.interpolator.getPreviousTime();
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double getInterpolatedTime() {
            return this.interpolator.getInterpolatedTime();
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double[] getInterpolatedY() throws org.apache.commons.math.ode.DerivativeException {
            double[] extendedState = this.interpolator.getInterpolatedState();
            java.lang.System.arraycopy(extendedState, 0, this.y, 0, this.y.length);
            return this.y;
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double[][] getInterpolatedDyDy0() throws org.apache.commons.math.ode.DerivativeException {
            double[] extendedState = this.interpolator.getInterpolatedState();
            int n = this.y.length;
            int start = n;
            for (int i = 0; i < n; i++) {
                java.lang.System.arraycopy(extendedState, start, this.dydy0[i], 0, n);
                start += n;
            }
            return this.dydy0;
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double[][] getInterpolatedDyDp() throws org.apache.commons.math.ode.DerivativeException {
            double[] extendedState = this.interpolator.getInterpolatedState();
            int n = this.y.length;
            int k = this.dydp[0].length;
            int start = (n + 1) * n;
            for (int i = 0; i < n; i++) {
                java.lang.System.arraycopy(extendedState, start, this.dydp[i], 0, k);
                start += k;
            }
            return this.dydp;
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double[] getInterpolatedYDot() throws org.apache.commons.math.ode.DerivativeException {
            double[] extendedDerivatives = this.interpolator.getInterpolatedDerivatives();
            java.lang.System.arraycopy(extendedDerivatives, 0, this.yDot, 0, this.yDot.length);
            return this.yDot;
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double[][] getInterpolatedDyDy0Dot() throws org.apache.commons.math.ode.DerivativeException {
            double[] extendedDerivatives = this.interpolator.getInterpolatedDerivatives();
            int n = this.y.length;
            int start = n;
            for (int i = 0; i < n; i++) {
                java.lang.System.arraycopy(extendedDerivatives, start, this.dydy0Dot[i], 0, n);
                start += n;
            }
            return this.dydy0Dot;
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double[][] getInterpolatedDyDpDot() throws org.apache.commons.math.ode.DerivativeException {
            double[] extendedDerivatives = this.interpolator.getInterpolatedDerivatives();
            int n = this.y.length;
            int k = this.dydpDot[0].length;
            int start = (n + 1) * n;
            for (int i = 0; i < n; i++) {
                java.lang.System.arraycopy(extendedDerivatives, start, this.dydpDot[i], 0, k);
                start += k;
            }
            return this.dydpDot;
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public double getCurrentTime() {
            return this.interpolator.getCurrentTime();
        }

        @Override // org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians
        public org.apache.commons.math.ode.jacobians.StepInterpolatorWithJacobians copy() throws org.apache.commons.math.ode.DerivativeException {
            int n = this.y.length;
            int k = this.dydp[0].length;
            org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.StepInterpolatorWrapper copied = new org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.StepInterpolatorWrapper(this.interpolator.copy(), n, k);
            copyArray(this.y, copied.y);
            copyArray(this.dydy0, copied.dydy0);
            copyArray(this.dydp, copied.dydp);
            copyArray(this.yDot, copied.yDot);
            copyArray(this.dydy0Dot, copied.dydy0Dot);
            copyArray(this.dydpDot, copied.dydpDot);
            return copied;
        }

        @Override // java.io.Externalizable
        public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
            out.writeObject(this.interpolator);
            out.writeInt(this.y.length);
            out.writeInt(this.dydp[0].length);
            writeArray(out, this.y);
            writeArray(out, this.dydy0);
            writeArray(out, this.dydp);
            writeArray(out, this.yDot);
            writeArray(out, this.dydy0Dot);
            writeArray(out, this.dydpDot);
        }

        @Override // java.io.Externalizable
        public void readExternal(java.io.ObjectInput in) throws java.io.IOException, java.lang.ClassNotFoundException {
            this.interpolator = (org.apache.commons.math.ode.sampling.StepInterpolator) in.readObject();
            int n = in.readInt();
            int k = in.readInt();
            this.y = new double[n];
            this.dydy0 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
            this.dydp = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, k);
            this.yDot = new double[n];
            this.dydy0Dot = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
            this.dydpDot = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, k);
            readArray(in, this.y);
            readArray(in, this.dydy0);
            readArray(in, this.dydp);
            readArray(in, this.yDot);
            readArray(in, this.dydy0Dot);
            readArray(in, this.dydpDot);
        }

        private static void copyArray(double[] src, double[] dest) {
            java.lang.System.arraycopy(src, 0, dest, 0, src.length);
        }

        private static void copyArray(double[][] src, double[][] dest) {
            for (int i = 0; i < src.length; i++) {
                copyArray(src[i], dest[i]);
            }
        }

        private static void writeArray(java.io.ObjectOutput out, double[] array) throws java.io.IOException {
            for (double d : array) {
                out.writeDouble(d);
            }
        }

        private static void writeArray(java.io.ObjectOutput out, double[][] array) throws java.io.IOException {
            for (double[] dArr : array) {
                writeArray(out, dArr);
            }
        }

        private static void readArray(java.io.ObjectInput in, double[] array) throws java.io.IOException {
            for (int i = 0; i < array.length; i++) {
                array[i] = in.readDouble();
            }
        }

        private static void readArray(java.io.ObjectInput in, double[][] array) throws java.io.IOException {
            for (double[] dArr : array) {
                readArray(in, dArr);
            }
        }
    }

    private static class EventHandlerWrapper implements org.apache.commons.math.ode.events.EventHandler {
        private double[][] dydp;
        private double[][] dydy0;
        private final org.apache.commons.math.ode.jacobians.EventHandlerWithJacobians handler;
        private double[] y;

        public EventHandlerWrapper(org.apache.commons.math.ode.jacobians.EventHandlerWithJacobians handler, int n, int k) {
            this.handler = handler;
            this.y = new double[n];
            this.dydy0 = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, n);
            this.dydp = (double[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Double.TYPE, n, k);
        }

        public org.apache.commons.math.ode.jacobians.EventHandlerWithJacobians getHandler() {
            return this.handler;
        }

        @Override // org.apache.commons.math.ode.events.EventHandler
        public int eventOccurred(double t, double[] z, boolean increasing) throws org.apache.commons.math.ode.events.EventException {
            org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.dispatchCompoundState(z, this.y, this.dydy0, this.dydp);
            return this.handler.eventOccurred(t, this.y, this.dydy0, this.dydp, increasing);
        }

        @Override // org.apache.commons.math.ode.events.EventHandler
        public double g(double t, double[] z) throws org.apache.commons.math.ode.events.EventException {
            org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.dispatchCompoundState(z, this.y, this.dydy0, this.dydp);
            return this.handler.g(t, this.y, this.dydy0, this.dydp);
        }

        @Override // org.apache.commons.math.ode.events.EventHandler
        public void resetState(double t, double[] z) throws org.apache.commons.math.ode.events.EventException {
            org.apache.commons.math.ode.jacobians.FirstOrderIntegratorWithJacobians.dispatchCompoundState(z, this.y, this.dydy0, this.dydp);
            this.handler.resetState(t, this.y, this.dydy0, this.dydp);
        }
    }
}
