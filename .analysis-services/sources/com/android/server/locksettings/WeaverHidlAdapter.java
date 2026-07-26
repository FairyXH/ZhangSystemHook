package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class WeaverHidlAdapter implements android.hardware.weaver.IWeaver {
    private static final java.lang.String TAG = "WeaverHidlAdapter";
    private final android.hardware.weaver.V1_0.IWeaver mImpl;

    WeaverHidlAdapter(android.hardware.weaver.V1_0.IWeaver impl) {
        this.mImpl = impl;
    }

    @Override // android.hardware.weaver.IWeaver
    public android.hardware.weaver.WeaverConfig getConfig() throws android.os.RemoteException {
        final android.hardware.weaver.WeaverConfig[] res = new android.hardware.weaver.WeaverConfig[1];
        this.mImpl.getConfig(new android.hardware.weaver.V1_0.IWeaver.getConfigCallback() { // from class: com.android.server.locksettings.WeaverHidlAdapter$$ExternalSyntheticLambda0
            @Override // android.hardware.weaver.V1_0.IWeaver.getConfigCallback
            public final void onValues(int i, android.hardware.weaver.V1_0.WeaverConfig weaverConfig) {
                com.android.server.locksettings.WeaverHidlAdapter.lambda$getConfig$0(res, i, weaverConfig);
            }
        });
        return res[0];
    }

    static /* synthetic */ void lambda$getConfig$0(android.hardware.weaver.WeaverConfig[] res, int status, android.hardware.weaver.V1_0.WeaverConfig config) {
        if (status == 0) {
            android.hardware.weaver.WeaverConfig aidlRes = new android.hardware.weaver.WeaverConfig();
            aidlRes.slots = config.slots;
            aidlRes.keySize = config.keySize;
            aidlRes.valueSize = config.valueSize;
            res[0] = aidlRes;
            return;
        }
        android.util.Slog.e(TAG, "Failed to get HIDL weaver config. status: " + status + ", slots: " + config.slots);
    }

    @Override // android.hardware.weaver.IWeaver
    public android.hardware.weaver.WeaverReadResponse read(int slotId, byte[] key) throws android.os.RemoteException {
        final android.hardware.weaver.WeaverReadResponse[] res = new android.hardware.weaver.WeaverReadResponse[1];
        this.mImpl.read(slotId, toByteArrayList(key), new android.hardware.weaver.V1_0.IWeaver.readCallback() { // from class: com.android.server.locksettings.WeaverHidlAdapter$$ExternalSyntheticLambda1
            @Override // android.hardware.weaver.V1_0.IWeaver.readCallback
            public final void onValues(int i, android.hardware.weaver.V1_0.WeaverReadResponse weaverReadResponse) {
                com.android.server.locksettings.WeaverHidlAdapter.lambda$read$1(res, i, weaverReadResponse);
            }
        });
        return res[0];
    }

    static /* synthetic */ void lambda$read$1(android.hardware.weaver.WeaverReadResponse[] res, int inStatus, android.hardware.weaver.V1_0.WeaverReadResponse readResponse) {
        android.hardware.weaver.WeaverReadResponse aidlRes = new android.hardware.weaver.WeaverReadResponse();
        switch (inStatus) {
            case 0:
                aidlRes.status = 0;
                break;
            case 1:
                aidlRes.status = 1;
                break;
            case 2:
                aidlRes.status = 2;
                break;
            case 3:
                aidlRes.status = 3;
                break;
            default:
                android.util.Slog.e(TAG, "Unexpected status in read: " + inStatus);
                aidlRes.status = 1;
                break;
        }
        aidlRes.timeout = readResponse.timeout;
        aidlRes.value = fromByteArrayList(readResponse.value);
        res[0] = aidlRes;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    @Override // android.hardware.weaver.IWeaver
    public void write(int slotId, byte[] key, byte[] value) throws android.os.RemoteException, android.os.ServiceSpecificException {
        int writeStatus = this.mImpl.write(slotId, toByteArrayList(key), toByteArrayList(value));
        if (writeStatus != 0) {
            throw new android.os.ServiceSpecificException(1, "Failed IWeaver.write call, status: " + writeStatus);
        }
    }

    @Override // android.hardware.weaver.IWeaver
    public java.lang.String getInterfaceHash() {
        throw new java.lang.UnsupportedOperationException("WeaverHidlAdapter does not support getInterfaceHash");
    }

    @Override // android.hardware.weaver.IWeaver
    public int getInterfaceVersion() {
        return 2;
    }

    @Override // android.os.IInterface
    public android.os.IBinder asBinder() {
        throw new java.lang.UnsupportedOperationException("WeaverHidlAdapter does not support asBinder");
    }

    private static java.util.ArrayList<java.lang.Byte> toByteArrayList(byte[] data) {
        java.util.ArrayList<java.lang.Byte> result = new java.util.ArrayList<>(data.length);
        for (byte b : data) {
            result.add(java.lang.Byte.valueOf(b));
        }
        return result;
    }

    private static byte[] fromByteArrayList(java.util.ArrayList<java.lang.Byte> data) {
        byte[] result = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i).byteValue();
        }
        return result;
    }
}
