package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
public abstract class VirtualDeviceManagerInternal {

    public interface AppsOnVirtualDeviceListener {
        void onAppsOnAnyVirtualDeviceChanged(java.util.Set<java.lang.Integer> set);
    }

    public abstract java.util.Set<java.lang.String> getAllPersistentDeviceIds();

    public abstract int getBaseVirtualDisplayFlags(android.companion.virtual.IVirtualDevice iVirtualDevice);

    public abstract int getDeviceIdForDisplayId(int i);

    public abstract android.util.ArraySet<java.lang.Integer> getDeviceIdsForUid(int i);

    public abstract int getDeviceOwnerUid(int i);

    public abstract android.util.ArraySet<java.lang.Integer> getDisplayIdsForDevice(int i);

    public abstract java.lang.String getPersistentIdForDevice(int i);

    public abstract android.os.LocaleList getPreferredLocaleListForUid(int i);

    public abstract android.companion.virtual.sensor.VirtualSensor getVirtualSensor(int i, int i2);

    public abstract boolean isAppRunningOnAnyVirtualDevice(int i);

    public abstract boolean isInputDeviceOwnedByVirtualDevice(int i);

    public abstract boolean isValidVirtualDeviceId(int i);

    public abstract void onAppsOnVirtualDeviceChanged();

    public abstract void onAuthenticationPrompt(int i);

    public abstract void onPersistentDeviceIdsRemoved(java.util.Set<java.lang.String> set);

    public abstract void onVirtualDisplayRemoved(android.companion.virtual.IVirtualDevice iVirtualDevice, int i);

    public abstract void registerAppsOnVirtualDeviceListener(com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener appsOnVirtualDeviceListener);

    public abstract void registerPersistentDeviceIdRemovedListener(java.util.function.Consumer<java.lang.String> consumer);

    public abstract void unregisterAppsOnVirtualDeviceListener(com.android.server.companion.virtual.VirtualDeviceManagerInternal.AppsOnVirtualDeviceListener appsOnVirtualDeviceListener);

    public abstract void unregisterPersistentDeviceIdRemovedListener(java.util.function.Consumer<java.lang.String> consumer);
}
