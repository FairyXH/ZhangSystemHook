package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public interface IKeyCombinationManagerWrapper {
    default android.util.SparseLongArray getDownTimes() {
        return new android.util.SparseLongArray(2);
    }

    default java.util.ArrayList<com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule> getRules() {
        return new java.util.ArrayList<>();
    }
}
