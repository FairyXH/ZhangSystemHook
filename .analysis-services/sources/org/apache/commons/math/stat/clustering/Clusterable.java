package org.apache.commons.math.stat.clustering;

/* JADX INFO: loaded from: classes4.dex */
public interface Clusterable<T> {
    T centroidOf(java.util.Collection<T> collection);

    double distanceFrom(T t);
}
