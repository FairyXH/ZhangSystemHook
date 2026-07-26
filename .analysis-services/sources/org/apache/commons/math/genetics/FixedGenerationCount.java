package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public class FixedGenerationCount implements org.apache.commons.math.genetics.StoppingCondition {
    private final int maxGenerations;
    private int numGenerations = 0;

    public FixedGenerationCount(int maxGenerations) {
        if (maxGenerations <= 0) {
            throw new java.lang.IllegalArgumentException("The number of generations has to be >= 0");
        }
        this.maxGenerations = maxGenerations;
    }

    @Override // org.apache.commons.math.genetics.StoppingCondition
    public boolean isSatisfied(org.apache.commons.math.genetics.Population population) {
        if (this.numGenerations >= this.maxGenerations) {
            return true;
        }
        this.numGenerations++;
        return false;
    }

    public int getNumGenerations() {
        return this.numGenerations;
    }
}
