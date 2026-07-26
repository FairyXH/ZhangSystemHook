package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface CallerIdentityInjector {
    public static final com.android.server.timezonedetector.CallerIdentityInjector REAL = new com.android.server.timezonedetector.CallerIdentityInjector.Real();

    long clearCallingIdentity();

    int getCallingUserId();

    int resolveUserId(int i, java.lang.String str);

    void restoreCallingIdentity(long j);

    public static class Real implements com.android.server.timezonedetector.CallerIdentityInjector {
        protected Real() {
        }

        @Override // com.android.server.timezonedetector.CallerIdentityInjector
        public int resolveUserId(int userId, java.lang.String debugName) {
            return android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, false, debugName, null);
        }

        @Override // com.android.server.timezonedetector.CallerIdentityInjector
        public int getCallingUserId() {
            return android.os.UserHandle.getCallingUserId();
        }

        @Override // com.android.server.timezonedetector.CallerIdentityInjector
        public long clearCallingIdentity() {
            return android.os.Binder.clearCallingIdentity();
        }

        @Override // com.android.server.timezonedetector.CallerIdentityInjector
        public void restoreCallingIdentity(long token) {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }
}
