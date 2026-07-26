package org.apache.commons.math.optimization.linear;

/* JADX INFO: loaded from: classes4.dex */
public interface LinearOptimizer {
    int getIterations();

    int getMaxIterations();

    org.apache.commons.math.optimization.RealPointValuePair optimize(org.apache.commons.math.optimization.linear.LinearObjectiveFunction linearObjectiveFunction, java.util.Collection<org.apache.commons.math.optimization.linear.LinearConstraint> collection, org.apache.commons.math.optimization.GoalType goalType, boolean z) throws org.apache.commons.math.optimization.OptimizationException;

    void setMaxIterations(int i);
}
