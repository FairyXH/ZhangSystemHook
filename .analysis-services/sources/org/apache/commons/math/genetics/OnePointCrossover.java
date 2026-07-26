package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public class OnePointCrossover<T> implements org.apache.commons.math.genetics.CrossoverPolicy {
    @Override // org.apache.commons.math.genetics.CrossoverPolicy
    public org.apache.commons.math.genetics.ChromosomePair crossover(org.apache.commons.math.genetics.Chromosome first, org.apache.commons.math.genetics.Chromosome second) {
        if (!(first instanceof org.apache.commons.math.genetics.AbstractListChromosome) || !(second instanceof org.apache.commons.math.genetics.AbstractListChromosome)) {
            throw new java.lang.IllegalArgumentException("One point crossover works on FixedLengthChromosomes only.");
        }
        return crossover((org.apache.commons.math.genetics.AbstractListChromosome) first, (org.apache.commons.math.genetics.AbstractListChromosome) second);
    }

    private org.apache.commons.math.genetics.ChromosomePair crossover(org.apache.commons.math.genetics.AbstractListChromosome<T> first, org.apache.commons.math.genetics.AbstractListChromosome<T> second) {
        int length = first.getLength();
        if (length != second.getLength()) {
            throw new java.lang.IllegalArgumentException("Both chromosomes must have same lengths.");
        }
        java.util.List<T> parent1Rep = first.getRepresentation();
        java.util.List<T> parent2Rep = second.getRepresentation();
        java.util.ArrayList<T> child1Rep = new java.util.ArrayList<>(first.getLength());
        java.util.ArrayList<T> child2Rep = new java.util.ArrayList<>(second.getLength());
        int crossoverIndex = org.apache.commons.math.genetics.GeneticAlgorithm.getRandomGenerator().nextInt(length - 2) + 1;
        for (int i = 0; i < crossoverIndex; i++) {
            child1Rep.add(parent1Rep.get(i));
            child2Rep.add(parent2Rep.get(i));
        }
        for (int i2 = crossoverIndex; i2 < length; i2++) {
            child1Rep.add(parent2Rep.get(i2));
            child2Rep.add(parent1Rep.get(i2));
        }
        return new org.apache.commons.math.genetics.ChromosomePair(first.newFixedLengthChromosome(child1Rep), second.newFixedLengthChromosome(child2Rep));
    }
}
