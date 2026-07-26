package com.android.server.bluetooth;

/* JADX INFO: compiled from: AdapterBinder.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000f\u001a\u00020\u0010J\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0017\u001a\u00020\u00132\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0018\u001a\u00020\u000eJ\u0016\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010\u001c\u001a\u00020\u000e2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u001f\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010 \u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\b\u0010!\u001a\u00020\u0006H\u0016J\u000e\u0010\"\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0016\u0010#\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D¢\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lcom/android/server/bluetooth/AdapterBinder;", "", "rawBinder", "Landroid/os/IBinder;", "(Landroid/os/IBinder;)V", "TAG", "", "adapterBinder", "Landroid/bluetooth/IBluetooth;", "getAdapterBinder", "()Landroid/bluetooth/IBluetooth;", "createdAt", "", "disable", "", "source", "Landroid/content/AttributionSource;", com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE, "quietMode", "", "factoryReset", "getAddress", "getName", "isMediaProfileConnected", "killBluetoothProcess", "registerCallback", "callback", "Landroid/bluetooth/IBluetoothCallback;", "setForegroundUserId", "userId", "", "startBrEdr", "stopBle", "toString", "unregAllGattClient", "unregisterCallback", "frameworks__base__services__android_common__services"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AdapterBinder {
    private final android.bluetooth.IBluetooth adapterBinder;
    private final java.lang.String TAG = "AdapterBinder";
    private final long createdAt = java.lang.System.currentTimeMillis();

    public AdapterBinder(android.os.IBinder rawBinder) {
        this.adapterBinder = android.bluetooth.IBluetooth.Stub.asInterface(rawBinder);
    }

    public final android.bluetooth.IBluetooth getAdapterBinder() {
        return this.adapterBinder;
    }

    public java.lang.String toString() {
        return "[Binder=" + this.adapterBinder.hashCode() + ", createdAt=" + com.android.server.bluetooth.BluetoothManagerService.timeToLog(this.createdAt) + "]";
    }

    public final void disable(android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.disable(source);
    }

    public final void enable(boolean quietMode, android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.enable(quietMode, source);
    }

    public final java.lang.String getAddress(android.content.AttributionSource source) throws android.os.RemoteException {
        return this.adapterBinder.getAddress(source);
    }

    public final java.lang.String getName(android.content.AttributionSource source) throws android.os.RemoteException {
        return this.adapterBinder.getName(source);
    }

    public final void stopBle(android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.stopBle(source);
    }

    public final void startBrEdr(android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.startBrEdr(source);
    }

    public final void registerCallback(android.bluetooth.IBluetoothCallback callback, android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.registerCallback(callback, source);
    }

    public final void unregisterCallback(android.bluetooth.IBluetoothCallback callback, android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.unregisterCallback(callback, source);
    }

    public final void setForegroundUserId(int userId, android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.setForegroundUserId(userId, source);
    }

    public final void unregAllGattClient(android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.unregAllGattClient(source);
    }

    public final boolean isMediaProfileConnected(android.content.AttributionSource source) {
        try {
            return this.adapterBinder.isMediaProfileConnected(source);
        } catch (android.os.RemoteException ex) {
            com.android.server.bluetooth.Log.INSTANCE.e(this.TAG, "Error when calling isMediaProfileConnected", ex);
            return false;
        }
    }

    public final void killBluetoothProcess() throws android.os.RemoteException {
        this.adapterBinder.killBluetoothProcess();
    }

    public final void factoryReset(android.content.AttributionSource source) throws android.os.RemoteException {
        this.adapterBinder.factoryReset(source);
    }
}
