package org.apache.commons.math.stat.clustering;

/* JADX INFO: loaded from: classes4.dex */
public class KMeansPlusPlusClusterer<T extends org.apache.commons.math.stat.clustering.Clusterable<T>> {
    private final org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy emptyStrategy;
    private final java.util.Random random;

    public enum EmptyClusterStrategy {
        LARGEST_VARIANCE,
        LARGEST_POINTS_NUMBER,
        FARTHEST_POINT,
        ERROR
    }

    public KMeansPlusPlusClusterer(java.util.Random random) {
        this(random, org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy.LARGEST_VARIANCE);
    }

    public KMeansPlusPlusClusterer(java.util.Random random, org.apache.commons.math.stat.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy emptyStrategy) {
        this.random = random;
        this.emptyStrategy = emptyStrategy;
    }

    public java.util.List<org.apache.commons.math.stat.clustering.Cluster<T>> cluster(java.util.Collection<T> points, int k, int maxIterations) {
        org.apache.commons.math.stat.clustering.Clusterable pointFromLargestVarianceCluster;
        java.util.List<org.apache.commons.math.stat.clustering.Cluster<T>> clusters = chooseInitialCenters(points, k, this.random);
        assignPointsToClusters(clusters, points);
        int max = maxIterations < 0 ? Integer.MAX_VALUE : maxIterations;
        for (int count = 0; count < max; count++) {
            boolean clusteringChanged = false;
            java.util.List<org.apache.commons.math.stat.clustering.Cluster<T>> newClusters = new java.util.ArrayList<>();
            for (org.apache.commons.math.stat.clustering.Cluster<T> cluster : clusters) {
                if (cluster.getPoints().isEmpty()) {
                    switch (this.emptyStrategy) {
                        case LARGEST_VARIANCE:
                            pointFromLargestVarianceCluster = getPointFromLargestVarianceCluster(clusters);
                            break;
                        case LARGEST_POINTS_NUMBER:
                            pointFromLargestVarianceCluster = getPointFromLargestNumberCluster(clusters);
                            break;
                        case FARTHEST_POINT:
                            pointFromLargestVarianceCluster = getFarthestPoint(clusters);
                            break;
                        default:
                            throw new org.apache.commons.math.exception.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
                    }
                    clusteringChanged = true;
                } else {
                    pointFromLargestVarianceCluster = (org.apache.commons.math.stat.clustering.Clusterable) cluster.getCenter().centroidOf(cluster.getPoints());
                    if (!pointFromLargestVarianceCluster.equals(cluster.getCenter())) {
                        clusteringChanged = true;
                    }
                }
                newClusters.add(new org.apache.commons.math.stat.clustering.Cluster<>(pointFromLargestVarianceCluster));
            }
            if (!clusteringChanged) {
                return clusters;
            }
            assignPointsToClusters(newClusters, points);
            clusters = newClusters;
        }
        return clusters;
    }

    private static <T extends org.apache.commons.math.stat.clustering.Clusterable<T>> void assignPointsToClusters(java.util.Collection<org.apache.commons.math.stat.clustering.Cluster<T>> clusters, java.util.Collection<T> points) {
        for (T p : points) {
            org.apache.commons.math.stat.clustering.Cluster<T> cluster = getNearestCluster(clusters, p);
            cluster.addPoint(p);
        }
    }

    private static <T extends org.apache.commons.math.stat.clustering.Clusterable<T>> java.util.List<org.apache.commons.math.stat.clustering.Cluster<T>> chooseInitialCenters(java.util.Collection<T> points, int k, java.util.Random random) {
        java.util.List<T> pointSet = new java.util.ArrayList<>((java.util.Collection<? extends T>) points);
        java.util.List<org.apache.commons.math.stat.clustering.Cluster<T>> resultSet = new java.util.ArrayList<>();
        T firstPoint = pointSet.remove(random.nextInt(pointSet.size()));
        resultSet.add(new org.apache.commons.math.stat.clustering.Cluster<>(firstPoint));
        double[] dx2 = new double[pointSet.size()];
        while (resultSet.size() < k) {
            int sum = 0;
            for (int i = 0; i < pointSet.size(); i++) {
                T p = pointSet.get(i);
                org.apache.commons.math.stat.clustering.Cluster<T> nearest = getNearestCluster(resultSet, p);
                double d = p.distanceFrom(nearest.getCenter());
                sum = (int) (((double) sum) + (d * d));
                dx2[i] = sum;
            }
            double r = random.nextDouble() * ((double) sum);
            int i2 = 0;
            while (true) {
                if (i2 >= dx2.length) {
                    break;
                }
                if (dx2[i2] < r) {
                    i2++;
                } else {
                    resultSet.add(new org.apache.commons.math.stat.clustering.Cluster<>(pointSet.remove(i2)));
                    break;
                }
            }
        }
        return resultSet;
    }

    private T getPointFromLargestVarianceCluster(java.util.Collection<org.apache.commons.math.stat.clustering.Cluster<T>> clusters) {
        double maxVariance = Double.NEGATIVE_INFINITY;
        org.apache.commons.math.stat.clustering.Cluster<T> selected = null;
        for (org.apache.commons.math.stat.clustering.Cluster<T> cluster : clusters) {
            if (!cluster.getPoints().isEmpty()) {
                org.apache.commons.math.stat.clustering.Clusterable center = cluster.getCenter();
                org.apache.commons.math.stat.descriptive.moment.Variance stat = new org.apache.commons.math.stat.descriptive.moment.Variance();
                for (T point : cluster.getPoints()) {
                    stat.increment(point.distanceFrom(center));
                }
                double variance = stat.getResult();
                if (variance > maxVariance) {
                    maxVariance = variance;
                    selected = cluster;
                }
            }
        }
        if (selected == null) {
            throw new org.apache.commons.math.exception.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
        }
        java.util.List<T> selectedPoints = selected.getPoints();
        return selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
    }

    private T getPointFromLargestNumberCluster(java.util.Collection<org.apache.commons.math.stat.clustering.Cluster<T>> clusters) {
        int maxNumber = 0;
        org.apache.commons.math.stat.clustering.Cluster<T> selected = null;
        for (org.apache.commons.math.stat.clustering.Cluster<T> cluster : clusters) {
            int number = cluster.getPoints().size();
            if (number > maxNumber) {
                maxNumber = number;
                selected = cluster;
            }
        }
        if (selected == null) {
            throw new org.apache.commons.math.exception.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
        }
        java.util.List<T> selectedPoints = selected.getPoints();
        return selectedPoints.remove(this.random.nextInt(selectedPoints.size()));
    }

    private T getFarthestPoint(java.util.Collection<org.apache.commons.math.stat.clustering.Cluster<T>> clusters) {
        double maxDistance = Double.NEGATIVE_INFINITY;
        org.apache.commons.math.stat.clustering.Cluster<T> selectedCluster = null;
        int selectedPoint = -1;
        for (org.apache.commons.math.stat.clustering.Cluster<T> cluster : clusters) {
            org.apache.commons.math.stat.clustering.Clusterable center = cluster.getCenter();
            java.util.List<T> points = cluster.getPoints();
            for (int i = 0; i < points.size(); i++) {
                double distance = points.get(i).distanceFrom(center);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    selectedCluster = cluster;
                    selectedPoint = i;
                }
            }
        }
        if (selectedCluster == null) {
            throw new org.apache.commons.math.exception.ConvergenceException(org.apache.commons.math.exception.util.LocalizedFormats.EMPTY_CLUSTER_IN_K_MEANS);
        }
        return selectedCluster.getPoints().remove(selectedPoint);
    }

    private static <T extends org.apache.commons.math.stat.clustering.Clusterable<T>> org.apache.commons.math.stat.clustering.Cluster<T> getNearestCluster(java.util.Collection<org.apache.commons.math.stat.clustering.Cluster<T>> clusters, T point) {
        double minDistance = Double.MAX_VALUE;
        org.apache.commons.math.stat.clustering.Cluster<T> minCluster = null;
        for (org.apache.commons.math.stat.clustering.Cluster<T> c : clusters) {
            double distance = point.distanceFrom(c.getCenter());
            if (distance < minDistance) {
                minDistance = distance;
                minCluster = c;
            }
        }
        return minCluster;
    }
}
