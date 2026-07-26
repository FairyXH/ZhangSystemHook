package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class RegionUtils {
    private RegionUtils() {
    }

    public static void rectListToRegion(java.util.List<android.graphics.Rect> rects, android.graphics.Region outRegion) {
        outRegion.setEmpty();
        int n = rects.size();
        for (int i = 0; i < n; i++) {
            outRegion.union(rects.get(i));
        }
    }

    public static void forEachRect(android.graphics.Region region, java.util.function.Consumer<android.graphics.Rect> rectConsumer) {
        android.graphics.RegionIterator it = new android.graphics.RegionIterator(region);
        android.graphics.Rect rect = new android.graphics.Rect();
        while (it.next(rect)) {
            rectConsumer.accept(rect);
        }
    }

    public static void forEachRectReverse(android.graphics.Region region, java.util.function.Consumer<android.graphics.Rect> rectConsumer) {
        android.graphics.RegionIterator it = new android.graphics.RegionIterator(region);
        java.util.ArrayList<android.graphics.Rect> rects = new java.util.ArrayList<>();
        android.graphics.Rect rect = new android.graphics.Rect();
        while (it.next(rect)) {
            rects.add(new android.graphics.Rect(rect));
        }
        java.util.Collections.reverse(rects);
        rects.forEach(rectConsumer);
    }
}
