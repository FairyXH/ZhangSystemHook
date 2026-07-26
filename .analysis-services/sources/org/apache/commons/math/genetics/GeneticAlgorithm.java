package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public class GeneticAlgorithm {
    private static org.apache.commons.math.random.RandomGenerator randomGenerator = new org.apache.commons.math.random.JDKRandomGenerator();
    private final org.apache.commons.math.genetics.CrossoverPolicy crossoverPolicy;
    private final double crossoverRate;
    private int generationsEvolved = 0;
    private final org.apache.commons.math.genetics.MutationPolicy mutationPolicy;
    private final double mutationRate;
    private final org.apache.commons.math.genetics.SelectionPolicy selectionPolicy;

    public GeneticAlgorithm(org.apache.commons.math.genetics.CrossoverPolicy crossoverPolicy, double crossoverRate, org.apache.commons.math.genetics.MutationPolicy mutationPolicy, double mutationRate, org.apache.commons.math.genetics.SelectionPolicy selectionPolicy) {
        if (crossoverRate < 0.0d || crossoverRate > 1.0d) {
            throw new java.lang.IllegalArgumentException("crossoverRate must be between 0 and 1");
        }
        if (mutationRate < 0.0d || mutationRate > 1.0d) {
            throw new java.lang.IllegalArgumentException("mutationRate must be between 0 and 1");
        }
        this.crossoverPolicy = crossoverPolicy;
        this.crossoverRate = crossoverRate;
        this.mutationPolicy = mutationPolicy;
        this.mutationRate = mutationRate;
        this.selectionPolicy = selectionPolicy;
    }

    public static synchronized void setRandomGenerator(org.apache.commons.math.random.RandomGenerator random) {
        randomGenerator = random;
    }

    public static synchronized org.apache.commons.math.random.RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }

    public org.apache.commons.math.genetics.Population evolve(org.apache.commons.math.genetics.Population initial, org.apache.commons.math.genetics.StoppingCondition condition) {
        org.apache.commons.math.genetics.Population current = initial;
        this.generationsEvolved = 0;
        while (!condition.isSatisfied(current)) {
            current = nextGeneration(current);
            this.generationsEvolved++;
        }
        return current;
    }

    public org.apache.commons.math.genetics.Population nextGeneration(org.apache.commons.math.genetics.Population current) {
        org.apache.commons.math.genetics.Population nextGeneration = current.nextGeneration();
        org.apache.commons.math.random.RandomGenerator randGen = getRandomGenerator();
        while (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
            org.apache.commons.math.genetics.ChromosomePair pair = getSelectionPolicy().select(current);
            if (randGen.nextDouble() < getCrossoverRate()) {
                pair = getCrossoverPolicy().crossover(pair.getFirst(), pair.getSecond());
            }
            if (randGen.nextDouble() < getMutationRate()) {
                pair = new org.apache.commons.math.genetics.ChromosomePair(getMutationPolicy().mutate(pair.getFirst()), getMutationPolicy().mutate(pair.getSecond()));
            }
            nextGeneration.addChromosome(pair.getFirst());
            if (nextGeneration.getPopulationSize() < nextGeneration.getPopulationLimit()) {
                nextGeneration.addChromosome(pair.getSecond());
            }
        }
        return nextGeneration;
    }

    public org.apache.commons.math.genetics.CrossoverPolicy getCrossoverPolicy() {
        return this.crossoverPolicy;
    }

    public double getCrossoverRate() {
        return this.crossoverRate;
    }

    public org.apache.commons.math.genetics.MutationPolicy getMutationPolicy() {
        return this.mutationPolicy;
    }

    public double getMutationRate() {
        return this.mutationRate;
    }

    public org.apache.commons.math.genetics.SelectionPolicy getSelectionPolicy() {
        return this.selectionPolicy;
    }

    public int getGenerationsEvolved() {
        return this.generationsEvolved;
    }
}
