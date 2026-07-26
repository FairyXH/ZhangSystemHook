package com.android.server.infra;

/* JADX INFO: loaded from: classes2.dex */
public interface ServiceNameResolver {

    public interface NameResolverListener {
        void onNameResolved(int i, java.lang.String str, boolean z);
    }

    void dumpShort(java.io.PrintWriter printWriter);

    void dumpShort(java.io.PrintWriter printWriter, int i);

    java.lang.String getDefaultServiceName(int i);

    default void setOnTemporaryServiceNameChangedCallback(com.android.server.infra.ServiceNameResolver.NameResolverListener callback) {
    }

    default java.lang.String[] getDefaultServiceNameList(int userId) {
        if (isConfiguredInMultipleMode()) {
            throw new java.lang.UnsupportedOperationException("getting default service list not supported");
        }
        return new java.lang.String[]{getDefaultServiceName(userId)};
    }

    default void setServiceNameList(java.util.List<java.lang.String> services, int userId) {
    }

    default boolean isConfiguredInMultipleMode() {
        return false;
    }

    default java.lang.String getServiceName(int userId) {
        return getDefaultServiceName(userId);
    }

    default java.lang.String[] getServiceNameList(int userId) {
        return getDefaultServiceNameList(userId);
    }

    default boolean isTemporary(int userId) {
        return false;
    }

    default void setTemporaryService(int userId, java.lang.String componentName, int durationMs) {
        throw new java.lang.UnsupportedOperationException("temporary user not supported");
    }

    default void setTemporaryServices(int userId, java.lang.String[] componentNames, int durationMs) {
        throw new java.lang.UnsupportedOperationException("temporary user not supported");
    }

    default void resetTemporaryService(int userId) {
        throw new java.lang.UnsupportedOperationException("temporary user not supported");
    }

    default boolean setDefaultServiceEnabled(int userId, boolean enabled) {
        throw new java.lang.UnsupportedOperationException("changing default service not supported");
    }

    default boolean isDefaultServiceEnabled(int userId) {
        throw new java.lang.UnsupportedOperationException("checking default service not supported");
    }
}
