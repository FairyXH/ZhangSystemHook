package org.apache.commons.math.genetics;

/* JADX INFO: loaded from: classes4.dex */
public class ChromosomePair {
    private final org.apache.commons.math.genetics.Chromosome first;
    private final org.apache.commons.math.genetics.Chromosome second;

    public ChromosomePair(org.apache.commons.math.genetics.Chromosome c1, org.apache.commons.math.genetics.Chromosome c2) {
        this.first = c1;
        this.second = c2;
    }

    public org.apache.commons.math.genetics.Chromosome getFirst() {
        return this.first;
    }

    public org.apache.commons.math.genetics.Chromosome getSecond() {
        return this.second;
    }

    public java.lang.String toString() {
        return java.lang.String.format("(%s,%s)", getFirst(), getSecond());
    }
}
