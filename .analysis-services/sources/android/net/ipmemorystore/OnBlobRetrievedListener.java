package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public interface OnBlobRetrievedListener {
    void onBlobRetrieved(android.net.ipmemorystore.Status status, java.lang.String str, java.lang.String str2, android.net.ipmemorystore.Blob blob);

    static android.net.ipmemorystore.IOnBlobRetrievedListener toAIDL(android.net.ipmemorystore.OnBlobRetrievedListener listener) {
        return new android.net.ipmemorystore.IOnBlobRetrievedListener.Stub() { // from class: android.net.ipmemorystore.OnBlobRetrievedListener.1
            @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
            public void onBlobRetrieved(android.net.ipmemorystore.StatusParcelable statusParcelable, java.lang.String l2Key, java.lang.String name, android.net.ipmemorystore.Blob blob) {
                if (android.net.ipmemorystore.OnBlobRetrievedListener.this != null) {
                    android.net.ipmemorystore.OnBlobRetrievedListener.this.onBlobRetrieved(new android.net.ipmemorystore.Status(statusParcelable), l2Key, name, blob);
                }
            }

            @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
            public int getInterfaceVersion() {
                return 10;
            }

            @Override // android.net.ipmemorystore.IOnBlobRetrievedListener
            public java.lang.String getInterfaceHash() {
                return "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
            }
        };
    }
}
