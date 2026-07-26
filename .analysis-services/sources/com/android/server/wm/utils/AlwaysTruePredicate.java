package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class AlwaysTruePredicate implements java.util.function.Predicate<java.lang.Object> {
    public static final com.android.server.wm.utils.AlwaysTruePredicate INSTANCE = new com.android.server.wm.utils.AlwaysTruePredicate();

    private AlwaysTruePredicate() {
    }

    @Override // java.util.function.Predicate
    public boolean test(java.lang.Object o) {
        return true;
    }
}
