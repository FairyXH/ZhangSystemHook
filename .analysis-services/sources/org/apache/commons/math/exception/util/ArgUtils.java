package org.apache.commons.math.exception.util;

/* JADX INFO: loaded from: classes4.dex */
public class ArgUtils {
    private ArgUtils() {
    }

    public static java.lang.Object[] flatten(java.lang.Object[] array) {
        java.util.List<java.lang.Object> list = new java.util.ArrayList<>();
        if (array != null) {
            for (java.lang.Object o : array) {
                if (o instanceof java.lang.Object[]) {
                    for (java.lang.Object oR : flatten((java.lang.Object[]) o)) {
                        list.add(oR);
                    }
                } else {
                    list.add(o);
                }
            }
        }
        return list.toArray();
    }
}
