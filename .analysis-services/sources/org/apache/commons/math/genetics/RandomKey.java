package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public abstract class RandomKey<T> extends org.apache.commons.math.genetics.AbstractListChromosome<java.lang.Double> implements org.apache.commons.math.genetics.PermutationChromosome<T> {
    private final java.util.List<java.lang.Integer> baseSeqPermutation;
    private final java.util.List<java.lang.Double> sortedRepresentation;

    public RandomKey(java.util.List<java.lang.Double> representation) {
        super(representation);
        java.util.List<java.lang.Double> sortedRepr = new java.util.ArrayList<>(getRepresentation());
        java.util.Collections.sort(sortedRepr);
        this.sortedRepresentation = java.util.Collections.unmodifiableList(sortedRepr);
        this.baseSeqPermutation = java.util.Collections.unmodifiableList(decodeGeneric(baseSequence(getLength()), getRepresentation(), this.sortedRepresentation));
    }

    public RandomKey(java.lang.Double[] representation) {
        this((java.util.List<java.lang.Double>) java.util.Arrays.asList(representation));
    }

    @Override // org.apache.commons.math.genetics.PermutationChromosome
    public java.util.List<T> decode(java.util.List<T> sequence) {
        return decodeGeneric(sequence, getRepresentation(), this.sortedRepresentation);
    }

    private static <S> java.util.List<S> decodeGeneric(java.util.List<S> sequence, java.util.List<java.lang.Double> representation, java.util.List<java.lang.Double> sortedRepr) {
        int l = sequence.size();
        if (representation.size() != l) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Length of sequence for decoding (%s) has to be equal to the length of the RandomKey (%s)", java.lang.Integer.valueOf(l), java.lang.Integer.valueOf(representation.size())));
        }
        if (representation.size() != sortedRepr.size()) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Representation and sortedRepr must have same sizes, %d != %d", java.lang.Integer.valueOf(representation.size()), java.lang.Integer.valueOf(sortedRepr.size())));
        }
        java.util.List<java.lang.Double> reprCopy = new java.util.ArrayList<>(representation);
        java.util.List<S> res = new java.util.ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            int index = reprCopy.indexOf(sortedRepr.get(i));
            res.add(sequence.get(index));
            reprCopy.set(index, null);
        }
        return res;
    }

    @Override // org.apache.commons.math.genetics.Chromosome
    protected boolean isSame(org.apache.commons.math.genetics.Chromosome another) {
        if (!(another instanceof org.apache.commons.math.genetics.RandomKey)) {
            return false;
        }
        org.apache.commons.math.genetics.RandomKey<?> anotherRk = (org.apache.commons.math.genetics.RandomKey) another;
        if (getLength() != anotherRk.getLength()) {
            return false;
        }
        java.util.List<java.lang.Integer> thisPerm = this.baseSeqPermutation;
        java.util.List<java.lang.Integer> anotherPerm = anotherRk.baseSeqPermutation;
        for (int i = 0; i < getLength(); i++) {
            if (thisPerm.get(i) != anotherPerm.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override // org.apache.commons.math.genetics.AbstractListChromosome
    protected void checkValidity(java.util.List<java.lang.Double> chromosomeRepresentation) throws org.apache.commons.math.genetics.InvalidRepresentationException {
        java.util.Iterator<java.lang.Double> it = chromosomeRepresentation.iterator();
        while (it.hasNext()) {
            double val = it.next().doubleValue();
            if (val < 0.0d || val > 1.0d) {
                throw new org.apache.commons.math.genetics.InvalidRepresentationException("Values of representation must be in [0,1] interval");
            }
        }
    }

    public static final java.util.List<java.lang.Double> randomPermutation(int l) {
        java.util.List<java.lang.Double> repr = new java.util.ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            repr.add(java.lang.Double.valueOf(org.apache.commons.math.genetics.GeneticAlgorithm.getRandomGenerator().nextDouble()));
        }
        return repr;
    }

    public static final java.util.List<java.lang.Double> identityPermutation(int l) {
        java.util.List<java.lang.Double> repr = new java.util.ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            repr.add(java.lang.Double.valueOf(((double) i) / ((double) l)));
        }
        return repr;
    }

    public static <S> java.util.List<java.lang.Double> comparatorPermutation(java.util.List<S> data, java.util.Comparator<S> comparator) {
        java.util.List<S> sortedData = new java.util.ArrayList<>(data);
        java.util.Collections.sort(sortedData, comparator);
        return inducedPermutation(data, sortedData);
    }

    public static <S> java.util.List<java.lang.Double> inducedPermutation(java.util.List<S> originalData, java.util.List<S> permutedData) throws java.lang.IllegalArgumentException {
        if (originalData.size() != permutedData.size()) {
            throw new java.lang.IllegalArgumentException("originalData and permutedData must have same length");
        }
        int l = originalData.size();
        java.util.List<S> origDataCopy = new java.util.ArrayList<>(originalData);
        java.lang.Double[] res = new java.lang.Double[l];
        for (int i = 0; i < l; i++) {
            int index = origDataCopy.indexOf(permutedData.get(i));
            if (index == -1) {
                throw new java.lang.IllegalArgumentException("originalData and permutedData must contain the same objects.");
            }
            res[index] = java.lang.Double.valueOf(((double) i) / ((double) l));
            origDataCopy.set(index, null);
        }
        return java.util.Arrays.asList(res);
    }

    @Override // org.apache.commons.math.genetics.AbstractListChromosome
    public java.lang.String toString() {
        return java.lang.String.format("(f=%s pi=(%s))", java.lang.Double.valueOf(getFitness()), this.baseSeqPermutation);
    }

    private static java.util.List<java.lang.Integer> baseSequence(int l) {
        java.util.List<java.lang.Integer> baseSequence = new java.util.ArrayList<>(l);
        for (int i = 0; i < l; i++) {
            baseSequence.add(java.lang.Integer.valueOf(i));
        }
        return baseSequence;
    }
}
