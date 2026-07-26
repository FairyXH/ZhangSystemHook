package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
class UserBroadcastResponseStats {
    private android.util.ArrayMap<com.android.server.usage.BroadcastEvent, android.app.usage.BroadcastResponseStats> mResponseStats = new android.util.ArrayMap<>();

    UserBroadcastResponseStats() {
    }

    android.app.usage.BroadcastResponseStats getBroadcastResponseStats(com.android.server.usage.BroadcastEvent broadcastEvent) {
        return this.mResponseStats.get(broadcastEvent);
    }

    android.app.usage.BroadcastResponseStats getOrCreateBroadcastResponseStats(com.android.server.usage.BroadcastEvent broadcastEvent) {
        android.app.usage.BroadcastResponseStats responseStats = this.mResponseStats.get(broadcastEvent);
        if (responseStats == null) {
            android.app.usage.BroadcastResponseStats responseStats2 = new android.app.usage.BroadcastResponseStats(broadcastEvent.getTargetPackage(), broadcastEvent.getIdForResponseEvent());
            this.mResponseStats.put(broadcastEvent, responseStats2);
            return responseStats2;
        }
        return responseStats;
    }

    void populateAllBroadcastResponseStats(java.util.List<android.app.usage.BroadcastResponseStats> broadcastResponseStatsList, java.lang.String packageName, long id) {
        for (int i = this.mResponseStats.size() - 1; i >= 0; i--) {
            com.android.server.usage.BroadcastEvent broadcastEvent = this.mResponseStats.keyAt(i);
            if ((id == 0 || id == broadcastEvent.getIdForResponseEvent()) && (packageName == null || packageName.equals(broadcastEvent.getTargetPackage()))) {
                broadcastResponseStatsList.add(this.mResponseStats.valueAt(i));
            }
        }
    }

    void clearBroadcastResponseStats(java.lang.String packageName, long id) {
        for (int i = this.mResponseStats.size() - 1; i >= 0; i--) {
            com.android.server.usage.BroadcastEvent broadcastEvent = this.mResponseStats.keyAt(i);
            if ((id == 0 || id == broadcastEvent.getIdForResponseEvent()) && (packageName == null || packageName.equals(broadcastEvent.getTargetPackage()))) {
                this.mResponseStats.removeAt(i);
            }
        }
    }

    void onPackageRemoved(java.lang.String packageName) {
        for (int i = this.mResponseStats.size() - 1; i >= 0; i--) {
            if (this.mResponseStats.keyAt(i).getTargetPackage().equals(packageName)) {
                this.mResponseStats.removeAt(i);
            }
        }
    }

    void dump(com.android.internal.util.IndentingPrintWriter ipw) {
        for (int i = 0; i < this.mResponseStats.size(); i++) {
            com.android.server.usage.BroadcastEvent broadcastEvent = this.mResponseStats.keyAt(i);
            android.app.usage.BroadcastResponseStats responseStats = this.mResponseStats.valueAt(i);
            ipw.print(broadcastEvent);
            ipw.print(" -> ");
            ipw.println(responseStats);
        }
    }
}
