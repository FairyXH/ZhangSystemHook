package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public interface PolicyPathProvider {
    default java.io.File getDataSystemDirectory() {
        return android.os.Environment.getDataSystemDirectory();
    }

    default java.io.File getUserSystemDirectory(int userId) {
        return android.os.Environment.getUserSystemDirectory(userId);
    }
}
