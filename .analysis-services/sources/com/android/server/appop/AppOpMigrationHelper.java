package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public interface AppOpMigrationHelper {
    java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.lang.Integer>> getLegacyAppIdAppOpModes(int i);

    int getLegacyAppOpVersion();

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Integer>> getLegacyPackageAppOpModes(int i);

    boolean hasLegacyAppOpState();
}
