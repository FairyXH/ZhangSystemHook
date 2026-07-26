package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class VpnProfileStore {
    public boolean put(java.lang.String alias, byte[] profile) {
        return android.security.LegacyVpnProfileStore.put(alias, profile);
    }

    public byte[] get(java.lang.String alias) {
        return android.security.LegacyVpnProfileStore.get(alias);
    }

    public boolean remove(java.lang.String alias) {
        return android.security.LegacyVpnProfileStore.remove(alias);
    }

    public java.lang.String[] list(java.lang.String prefix) {
        return android.security.LegacyVpnProfileStore.list(prefix);
    }
}
