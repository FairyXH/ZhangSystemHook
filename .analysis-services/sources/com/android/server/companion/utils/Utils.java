package com.android.server.companion.utils;

/* JADX INFO: loaded from: classes.dex */
public final class Utils {
    public static <T extends android.os.ResultReceiver> android.os.ResultReceiver prepareForIpc(T resultReceiver) {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        resultReceiver.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        android.os.ResultReceiver ipcFriendly = (android.os.ResultReceiver) android.os.ResultReceiver.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return ipcFriendly;
    }

    public static java.lang.String btDeviceToString(android.bluetooth.BluetoothDevice btDevice) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(btDevice.getAddress());
        sb.append(" [name=");
        java.lang.String name = btDevice.getName();
        if (name != null) {
            sb.append('\'').append(name).append('\'');
        } else {
            sb.append("null");
        }
        java.lang.String alias = btDevice.getAlias();
        if (alias != null) {
            sb.append(", alias='").append(alias).append("'");
        }
        return sb.append(']').toString();
    }

    private Utils() {
    }
}
