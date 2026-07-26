package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public class BinaryMutation implements org.apache.commons.math.genetics.MutationPolicy {
    @Override // org.apache.commons.math.genetics.MutationPolicy
    public org.apache.commons.math.genetics.Chromosome mutate(org.apache.commons.math.genetics.Chromosome original) {
        if (!(original instanceof org.apache.commons.math.genetics.BinaryChromosome)) {
            throw new java.lang.IllegalArgumentException("Binary mutation works on BinaryChromosome only.");
        }
        org.apache.commons.math.genetics.BinaryChromosome origChrom = (org.apache.commons.math.genetics.BinaryChromosome) original;
        java.util.ArrayList arrayList = new java.util.ArrayList(origChrom.getRepresentation());
        int geneIndex = org.apache.commons.math.genetics.GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
        arrayList.set(geneIndex, java.lang.Integer.valueOf(origChrom.getRepresentation().get(geneIndex).intValue() == 0 ? 1 : 0));
        org.apache.commons.math.genetics.Chromosome newChrom = origChrom.newFixedLengthChromosome(arrayList);
        return newChrom;
    }
}
