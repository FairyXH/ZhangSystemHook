package org.apache.commons.math.optimization.fitting;

/* JADX INFO: loaded from: classes4.dex */
public class WeightedObservedPoint implements java.io.Serializable {
    private static final long serialVersionUID = 5306874947404636157L;
    private final double weight;
    private final double x;
    private final double y;

    public WeightedObservedPoint(double weight, double x, double y) {
        this.weight = weight;
        this.x = x;
        this.y = y;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }
}
