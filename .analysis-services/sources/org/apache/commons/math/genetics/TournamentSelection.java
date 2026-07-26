package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public class TournamentSelection implements org.apache.commons.math.genetics.SelectionPolicy {
    private int arity;

    public TournamentSelection(int arity) {
        this.arity = arity;
    }

    @Override // org.apache.commons.math.genetics.SelectionPolicy
    public org.apache.commons.math.genetics.ChromosomePair select(org.apache.commons.math.genetics.Population population) {
        return new org.apache.commons.math.genetics.ChromosomePair(tournament((org.apache.commons.math.genetics.ListPopulation) population), tournament((org.apache.commons.math.genetics.ListPopulation) population));
    }

    private org.apache.commons.math.genetics.Chromosome tournament(org.apache.commons.math.genetics.ListPopulation population) {
        if (population.getPopulationSize() < this.arity) {
            throw new java.lang.IllegalArgumentException("Tournament arity cannot be bigger than population size.");
        }
        org.apache.commons.math.genetics.ListPopulation tournamentPopulation = new org.apache.commons.math.genetics.ListPopulation(this.arity) { // from class: org.apache.commons.math.genetics.TournamentSelection.1
            @Override // org.apache.commons.math.genetics.Population
            public org.apache.commons.math.genetics.Population nextGeneration() {
                return null;
            }
        };
        java.util.List<org.apache.commons.math.genetics.Chromosome> chromosomes = new java.util.ArrayList<>(population.getChromosomes());
        for (int i = 0; i < this.arity; i++) {
            int rind = org.apache.commons.math.genetics.GeneticAlgorithm.getRandomGenerator().nextInt(chromosomes.size());
            tournamentPopulation.addChromosome(chromosomes.get(rind));
            chromosomes.remove(rind);
        }
        return tournamentPopulation.getFittestChromosome();
    }

    public int getArity() {
        return this.arity;
    }

    public void setArity(int arity) {
        this.arity = arity;
    }
}
