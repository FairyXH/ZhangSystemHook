package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public abstract class ListPopulation implements org.apache.commons.math.genetics.Population {
    private java.util.List<org.apache.commons.math.genetics.Chromosome> chromosomes;
    private int populationLimit;

    public ListPopulation(java.util.List<org.apache.commons.math.genetics.Chromosome> chromosomes, int populationLimit) {
        if (chromosomes.size() > populationLimit) {
            throw new org.apache.commons.math.exception.NumberIsTooLargeException(org.apache.commons.math.exception.util.LocalizedFormats.LIST_OF_CHROMOSOMES_BIGGER_THAN_POPULATION_SIZE, java.lang.Integer.valueOf(chromosomes.size()), java.lang.Integer.valueOf(populationLimit), false);
        }
        if (populationLimit < 0) {
            throw new org.apache.commons.math.exception.NotPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, java.lang.Integer.valueOf(populationLimit));
        }
        this.chromosomes = chromosomes;
        this.populationLimit = populationLimit;
    }

    public ListPopulation(int populationLimit) {
        if (populationLimit < 0) {
            throw new org.apache.commons.math.exception.NotPositiveException(org.apache.commons.math.exception.util.LocalizedFormats.POPULATION_LIMIT_NOT_POSITIVE, java.lang.Integer.valueOf(populationLimit));
        }
        this.populationLimit = populationLimit;
        this.chromosomes = new java.util.ArrayList(populationLimit);
    }

    public void setChromosomes(java.util.List<org.apache.commons.math.genetics.Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    public java.util.List<org.apache.commons.math.genetics.Chromosome> getChromosomes() {
        return this.chromosomes;
    }

    @Override // org.apache.commons.math.genetics.Population
    public void addChromosome(org.apache.commons.math.genetics.Chromosome chromosome) {
        this.chromosomes.add(chromosome);
    }

    @Override // org.apache.commons.math.genetics.Population
    public org.apache.commons.math.genetics.Chromosome getFittestChromosome() {
        org.apache.commons.math.genetics.Chromosome bestChromosome = this.chromosomes.get(0);
        for (org.apache.commons.math.genetics.Chromosome chromosome : this.chromosomes) {
            if (chromosome.compareTo(bestChromosome) > 0) {
                bestChromosome = chromosome;
            }
        }
        return bestChromosome;
    }

    @Override // org.apache.commons.math.genetics.Population
    public int getPopulationLimit() {
        return this.populationLimit;
    }

    public void setPopulationLimit(int populationLimit) {
        this.populationLimit = populationLimit;
    }

    @Override // org.apache.commons.math.genetics.Population
    public int getPopulationSize() {
        return this.chromosomes.size();
    }

    public java.lang.String toString() {
        return this.chromosomes.toString();
    }

    @Override // java.lang.Iterable
    public java.util.Iterator<org.apache.commons.math.genetics.Chromosome> iterator() {
        return this.chromosomes.iterator();
    }
}
