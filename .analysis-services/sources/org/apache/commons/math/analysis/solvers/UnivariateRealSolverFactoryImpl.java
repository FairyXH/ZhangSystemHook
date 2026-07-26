package org.apache.commons.math.analysis.solvers;

/* JADX INFO: loaded from: classes4.dex */
public class UnivariateRealSolverFactoryImpl extends org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory {
    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory
    public org.apache.commons.math.analysis.solvers.UnivariateRealSolver newDefaultSolver() {
        return newBrentSolver();
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory
    public org.apache.commons.math.analysis.solvers.UnivariateRealSolver newBisectionSolver() {
        return new org.apache.commons.math.analysis.solvers.BisectionSolver();
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory
    public org.apache.commons.math.analysis.solvers.UnivariateRealSolver newBrentSolver() {
        return new org.apache.commons.math.analysis.solvers.BrentSolver();
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory
    public org.apache.commons.math.analysis.solvers.UnivariateRealSolver newNewtonSolver() {
        return new org.apache.commons.math.analysis.solvers.NewtonSolver();
    }

    @Override // org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory
    public org.apache.commons.math.analysis.solvers.UnivariateRealSolver newSecantSolver() {
        return new org.apache.commons.math.analysis.solvers.SecantSolver();
    }
}
