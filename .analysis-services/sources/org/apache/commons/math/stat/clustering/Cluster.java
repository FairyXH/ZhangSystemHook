package org.apache.commons.math.stat.clustering;

/* JADX INFO: loaded from: classes4.dex */
public class Cluster<T extends org.apache.commons.math.stat.clustering.Clusterable<T>> implements java.io.Serializable {
    private static final long serialVersionUID = -3442297081515880464L;
    private final T center;
    private final java.util.List<T> points = new java.util.ArrayList();

    public Cluster(T center) {
        this.center = center;
    }

    public void addPoint(T point) {
        this.points.add(point);
    }

    public java.util.List<T> getPoints() {
        return this.points;
    }

    public T getCenter() {
        return this.center;
    }
}
