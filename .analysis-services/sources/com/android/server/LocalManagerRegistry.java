package com.android.server;

/* JADX INFO: loaded from: classes.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public final class LocalManagerRegistry {
    private static final java.util.Map<java.lang.Class<?>, java.lang.Object> sManagers = new android.util.ArrayMap();

    private LocalManagerRegistry() {
    }

    public static <T> T getManager(java.lang.Class<T> cls) {
        T t;
        synchronized (sManagers) {
            t = (T) sManagers.get(cls);
        }
        return t;
    }

    public static <T> T getManagerOrThrow(java.lang.Class<T> cls) throws com.android.server.LocalManagerRegistry.ManagerNotFoundException {
        T t = (T) getManager(cls);
        if (t == null) {
            throw new com.android.server.LocalManagerRegistry.ManagerNotFoundException(cls);
        }
        return t;
    }

    public static <T> void addManager(java.lang.Class<T> managerClass, T manager) {
        java.util.Objects.requireNonNull(managerClass, "managerClass");
        java.util.Objects.requireNonNull(manager, "manager");
        synchronized (sManagers) {
            if (sManagers.containsKey(managerClass)) {
                throw new java.lang.IllegalStateException(managerClass.getName() + " is already registered");
            }
            sManagers.put(managerClass, manager);
        }
    }

    public static class ManagerNotFoundException extends java.lang.Exception {
        public <T> ManagerNotFoundException(java.lang.Class<T> managerClass) {
            super("Local manager " + managerClass.getName() + " does not exist or is not ready");
        }
    }
}
