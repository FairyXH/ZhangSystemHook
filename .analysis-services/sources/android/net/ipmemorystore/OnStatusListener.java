package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public interface OnStatusListener {
    void onComplete(android.net.ipmemorystore.Status status);

    static android.net.ipmemorystore.IOnStatusListener toAIDL(android.net.ipmemorystore.OnStatusListener listener) {
        return new android.net.ipmemorystore.IOnStatusListener.Stub() { // from class: android.net.ipmemorystore.OnStatusListener.1
            @Override // android.net.ipmemorystore.IOnStatusListener
            public void onComplete(android.net.ipmemorystore.StatusParcelable statusParcelable) {
                if (android.net.ipmemorystore.OnStatusListener.this != null) {
                    android.net.ipmemorystore.OnStatusListener.this.onComplete(new android.net.ipmemorystore.Status(statusParcelable));
                }
            }

            @Override // android.net.ipmemorystore.IOnStatusListener
            public int getInterfaceVersion() {
                return 10;
            }

            @Override // android.net.ipmemorystore.IOnStatusListener
            public java.lang.String getInterfaceHash() {
                return "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
            }
        };
    }
}
