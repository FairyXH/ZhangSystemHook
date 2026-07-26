package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public class RandomKeyMutation implements org.apache.commons.math.genetics.MutationPolicy {
    @Override // org.apache.commons.math.genetics.MutationPolicy
    public org.apache.commons.math.genetics.Chromosome mutate(org.apache.commons.math.genetics.Chromosome original) {
        if (!(original instanceof org.apache.commons.math.genetics.RandomKey)) {
            throw org.apache.commons.math.MathRuntimeException.createIllegalArgumentException(org.apache.commons.math.exception.util.LocalizedFormats.RANDOMKEY_MUTATION_WRONG_CLASS, original.getClass().getSimpleName());
        }
        org.apache.commons.math.genetics.RandomKey<?> originalRk = (org.apache.commons.math.genetics.RandomKey) original;
        java.util.List<?> representation = originalRk.getRepresentation();
        int rInd = org.apache.commons.math.genetics.GeneticAlgorithm.getRandomGenerator().nextInt(representation.size());
        java.util.List<java.lang.Double> newRepr = new java.util.ArrayList<>(representation);
        newRepr.set(rInd, java.lang.Double.valueOf(org.apache.commons.math.genetics.GeneticAlgorithm.getRandomGenerator().nextDouble()));
        return originalRk.newFixedLengthChromosome(newRepr);
    }
}
