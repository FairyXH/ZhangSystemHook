package com.android.server.location.common;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusCommonFactory {
    boolean isValid(int i);

    default <T extends com.android.server.location.common.IOplusCommonFeature> T getFeature(T def, java.lang.Object... vars) {
        verityParams(def);
        return def;
    }

    default <T extends com.android.server.location.common.IOplusCommonFeature> void verityParams(T def) {
        if (def == null) {
            throw new java.lang.IllegalArgumentException("def can not be null");
        }
        if (com.android.server.location.common.OplusLbsFeatureList.OplusIndex.End == def.index()) {
            throw new java.lang.IllegalArgumentException(def + "must override index() method");
        }
    }

    default void verityParamsType(java.lang.String key, java.lang.Object[] vars, int num, java.lang.Class... types) {
        if (vars == null || types == null || vars.length != num || types.length != num) {
            throw new java.lang.IllegalArgumentException(key + " need +" + num + " params");
        }
        for (int i = 0; i < num; i++) {
            if (!types[i].isInstance(vars[i])) {
                throw new java.lang.IllegalArgumentException(types[i].getName() + " is not instance " + vars[i]);
            }
        }
    }
}
