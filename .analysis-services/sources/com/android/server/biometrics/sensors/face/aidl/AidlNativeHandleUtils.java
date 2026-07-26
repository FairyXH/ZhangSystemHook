package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public final class AidlNativeHandleUtils {
    public static android.hardware.common.NativeHandle dup(android.os.NativeHandle handle) throws java.io.IOException {
        if (handle == null) {
            return null;
        }
        android.hardware.common.NativeHandle res = new android.hardware.common.NativeHandle();
        java.io.FileDescriptor[] fds = handle.getFileDescriptors();
        res.ints = (int[]) handle.getInts().clone();
        res.fds = new android.os.ParcelFileDescriptor[fds.length];
        for (int i = 0; i < fds.length; i++) {
            res.fds[i] = android.os.ParcelFileDescriptor.dup(fds[i]);
        }
        return res;
    }

    public static void close(android.hardware.common.NativeHandle handle) throws java.io.IOException {
        if (handle != null) {
            for (android.os.ParcelFileDescriptor fd : handle.fds) {
                if (fd != null) {
                    fd.close();
                }
            }
        }
    }
}
