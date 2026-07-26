package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public interface Population extends java.lang.Iterable<org.apache.commons.math.genetics.Chromosome> {
    void addChromosome(org.apache.commons.math.genetics.Chromosome chromosome);

    org.apache.commons.math.genetics.Chromosome getFittestChromosome();

    int getPopulationLimit();

    int getPopulationSize();

    org.apache.commons.math.genetics.Population nextGeneration();
}
