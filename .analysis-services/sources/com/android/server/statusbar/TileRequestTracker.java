package com.android.server.statusbar;

/* JADX INFO: loaded from: classes3.dex */
public class TileRequestTracker {
    static final int MAX_NUM_DENIALS = 3;
    private final android.content.Context mContext;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArrayMap<android.content.ComponentName, java.lang.Integer> mTrackingMap = new android.util.SparseArrayMap<>();
    private final android.util.ArraySet<android.content.ComponentName> mComponentsToRemove = new android.util.ArraySet<>();
    private final android.content.BroadcastReceiver mUninstallReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.statusbar.TileRequestTracker.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (intent.getBooleanExtra("android.intent.extra.REPLACING", false)) {
                return;
            }
            android.net.Uri data = intent.getData();
            java.lang.String packageName = data.getEncodedSchemeSpecificPart();
            if (!intent.hasExtra("android.intent.extra.UID")) {
                return;
            }
            int userId = android.os.UserHandle.getUserId(intent.getIntExtra("android.intent.extra.UID", -1));
            synchronized (com.android.server.statusbar.TileRequestTracker.this.mLock) {
                com.android.server.statusbar.TileRequestTracker.this.mComponentsToRemove.clear();
                int elementsForUser = com.android.server.statusbar.TileRequestTracker.this.mTrackingMap.numElementsForKey(userId);
                int userKeyIndex = com.android.server.statusbar.TileRequestTracker.this.mTrackingMap.indexOfKey(userId);
                for (int compKeyIndex = 0; compKeyIndex < elementsForUser; compKeyIndex++) {
                    android.content.ComponentName c = (android.content.ComponentName) com.android.server.statusbar.TileRequestTracker.this.mTrackingMap.keyAt(userKeyIndex, compKeyIndex);
                    if (c.getPackageName().equals(packageName)) {
                        com.android.server.statusbar.TileRequestTracker.this.mComponentsToRemove.add(c);
                    }
                }
                int compsToRemoveNum = com.android.server.statusbar.TileRequestTracker.this.mComponentsToRemove.size();
                for (int i = 0; i < compsToRemoveNum; i++) {
                    com.android.server.statusbar.TileRequestTracker.this.mTrackingMap.delete(userId, (android.content.ComponentName) com.android.server.statusbar.TileRequestTracker.this.mComponentsToRemove.valueAt(i));
                }
            }
        }
    };

    TileRequestTracker(android.content.Context context) {
        this.mContext = context;
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_DATA_CLEARED");
        intentFilter.addDataScheme("package");
        intentFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_REMOVED@PACKAGE=NOREPLACING");
        this.mContext.registerReceiverAsUser(this.mUninstallReceiver, android.os.UserHandle.ALL, intentFilter, null, null);
    }

    boolean shouldBeDenied(int userId, android.content.ComponentName componentName) {
        boolean z;
        synchronized (this.mLock) {
            z = ((java.lang.Integer) this.mTrackingMap.getOrDefault(userId, componentName, 0)).intValue() >= 3;
        }
        return z;
    }

    void addDenial(int userId, android.content.ComponentName componentName) {
        synchronized (this.mLock) {
            int current = ((java.lang.Integer) this.mTrackingMap.getOrDefault(userId, componentName, 0)).intValue();
            this.mTrackingMap.add(userId, componentName, java.lang.Integer.valueOf(current + 1));
        }
    }

    void resetRequests(int userId, android.content.ComponentName componentName) {
        synchronized (this.mLock) {
            this.mTrackingMap.delete(userId, componentName);
        }
    }

    void dump(java.io.FileDescriptor fd, final android.util.IndentingPrintWriter pw, java.lang.String[] args) {
        pw.println("TileRequestTracker:");
        pw.increaseIndent();
        synchronized (this.mLock) {
            this.mTrackingMap.forEach(new android.util.SparseArrayMap.TriConsumer() { // from class: com.android.server.statusbar.TileRequestTracker$$ExternalSyntheticLambda0
                public final void accept(int i, java.lang.Object obj, java.lang.Object obj2) {
                    pw.println("user=" + i + ", " + ((android.content.ComponentName) obj).toShortString() + ": " + ((java.lang.Integer) obj2));
                }
            });
        }
        pw.decreaseIndent();
    }
}
