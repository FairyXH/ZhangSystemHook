package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public interface OnSameL3NetworkResponseListener {
    void onSameL3NetworkResponse(android.net.ipmemorystore.Status status, android.net.ipmemorystore.SameL3NetworkResponse sameL3NetworkResponse);

    static android.net.ipmemorystore.IOnSameL3NetworkResponseListener toAIDL(android.net.ipmemorystore.OnSameL3NetworkResponseListener listener) {
        return new android.net.ipmemorystore.IOnSameL3NetworkResponseListener.Stub() { // from class: android.net.ipmemorystore.OnSameL3NetworkResponseListener.1
            @Override // android.net.ipmemorystore.IOnSameL3NetworkResponseListener
            public void onSameL3NetworkResponse(android.net.ipmemorystore.StatusParcelable statusParcelable, android.net.ipmemorystore.SameL3NetworkResponseParcelable sameL3NetworkResponseParcelable) {
                if (android.net.ipmemorystore.OnSameL3NetworkResponseListener.this != null) {
                    android.net.ipmemorystore.OnSameL3NetworkResponseListener.this.onSameL3NetworkResponse(new android.net.ipmemorystore.Status(statusParcelable), new android.net.ipmemorystore.SameL3NetworkResponse(sameL3NetworkResponseParcelable));
                }
            }

            @Override // android.net.ipmemorystore.IOnSameL3NetworkResponseListener
            public int getInterfaceVersion() {
                return 10;
            }

            @Override // android.net.ipmemorystore.IOnSameL3NetworkResponseListener
            public java.lang.String getInterfaceHash() {
                return "d5ea5eb3ddbdaa9a986ce6ba70b0804ca3e39b0c";
            }
        };
    }
}
