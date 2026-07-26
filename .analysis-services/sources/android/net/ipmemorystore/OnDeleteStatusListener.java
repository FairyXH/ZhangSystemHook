package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public interface OnDeleteStatusListener {
    void onComplete(android.net.ipmemorystore.Status status, int i);

    static android.net.ipmemorystore.IOnStatusAndCountListener toAIDL(android.net.ipmemorystore.OnDeleteStatusListener listener) {
        return new android.net.ipmemorystore.IOnStatusAndCountListener.Stub() { // from class: android.net.ipmemorystore.OnDeleteStatusListener.1
            @Override // android.net.ipmemorystore.IOnStatusAndCountListener
            public void onComplete(android.net.ipmemorystore.StatusParcelable statusParcelable, int deletedRecords) {
                if (android.net.ipmemorystore.OnDeleteStatusListener.this != null) {
                    android.net.ipmemorystore.OnDeleteStatusListener.this.onComplete(new android.net.ipmemorystore.Status(statusParcelable), deletedRecords);
                }
            }

            @Override // android.net.ipmemorystore.IOnStatusAndCountListener
            public int getInterfaceVersion() {
                return 10;
            }

            @Override // android.net.ipmemorystore.IOnStatusAndCountListener
            public java.lang.String getInterfaceHash() {
                return "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
            }
        };
    }
}
