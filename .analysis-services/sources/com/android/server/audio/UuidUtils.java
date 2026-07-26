package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
class UuidUtils {
    private static final long LSB_PREFIX_BT = 4779445104546938880L;
    private static final long LSB_PREFIX_MASK = -281474976710656L;
    private static final long LSB_SUFFIX_MASK = 281474976710655L;
    public static final java.util.UUID STANDALONE_UUID = new java.util.UUID(0, 0);
    private static final java.lang.String TAG = "AudioService.UuidUtils";

    UuidUtils() {
    }

    public static java.util.UUID uuidFromAudioDeviceAttributes(android.media.AudioDeviceAttributes device) {
        if (!android.media.AudioSystem.isBluetoothA2dpOutDevice(device.getInternalType()) && !android.media.AudioSystem.isBluetoothLeOutDevice(device.getInternalType())) {
            return null;
        }
        java.lang.String address = device.getAddress().replace(":", "");
        if (address.length() != 12) {
            return null;
        }
        try {
            long lsb = LSB_PREFIX_BT | java.lang.Long.decode("0x" + address).longValue();
            if (com.android.server.audio.AudioService.DEBUG_DEVICES) {
                android.util.Slog.i(TAG, "uuidFromAudioDeviceAttributes lsb: " + java.lang.Long.toHexString(lsb));
            }
            return new java.util.UUID(0L, lsb);
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }
}
