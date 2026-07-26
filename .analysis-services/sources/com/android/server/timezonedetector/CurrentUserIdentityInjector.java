package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public interface CurrentUserIdentityInjector {
    public static final com.android.server.timezonedetector.CurrentUserIdentityInjector REAL = new com.android.server.timezonedetector.CurrentUserIdentityInjector.Real();

    int getCurrentUserId();

    public static class Real implements com.android.server.timezonedetector.CurrentUserIdentityInjector {
        protected Real() {
        }

        @Override // com.android.server.timezonedetector.CurrentUserIdentityInjector
        public int getCurrentUserId() {
            return ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCurrentUserId();
        }
    }
}
