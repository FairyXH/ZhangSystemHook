package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class NetworkWatchlistInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    public NetworkWatchlistInstallReceiver() {
        super("/data/misc/network_watchlist/", "network_watchlist.xml", "metadata/", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected void postInstall(android.content.Context context, android.content.Intent intent) {
        try {
            ((android.net.NetworkWatchlistManager) context.getSystemService(android.net.NetworkWatchlistManager.class)).reloadWatchlist();
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf("NetworkWatchlistInstallReceiver", "Unable to reload watchlist");
        }
    }
}
