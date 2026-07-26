package com.android.server.usage;

/* JADX INFO: loaded from: classes3.dex */
class UserBroadcastEvents {
    private android.util.ArrayMap<java.lang.String, android.util.ArraySet<com.android.server.usage.BroadcastEvent>> mBroadcastEvents = new android.util.ArrayMap<>();

    UserBroadcastEvents() {
    }

    android.util.ArraySet<com.android.server.usage.BroadcastEvent> getBroadcastEvents(java.lang.String packageName) {
        return this.mBroadcastEvents.get(packageName);
    }

    android.util.ArraySet<com.android.server.usage.BroadcastEvent> getOrCreateBroadcastEvents(java.lang.String packageName) {
        android.util.ArraySet<com.android.server.usage.BroadcastEvent> broadcastEvents = this.mBroadcastEvents.get(packageName);
        if (broadcastEvents == null) {
            android.util.ArraySet<com.android.server.usage.BroadcastEvent> broadcastEvents2 = new android.util.ArraySet<>();
            this.mBroadcastEvents.put(packageName, broadcastEvents2);
            return broadcastEvents2;
        }
        return broadcastEvents;
    }

    void onPackageRemoved(java.lang.String packageName) {
        this.mBroadcastEvents.remove(packageName);
    }

    void onUidRemoved(int uid) {
        clear(uid);
    }

    void clear(int uid) {
        for (int i = this.mBroadcastEvents.size() - 1; i >= 0; i--) {
            android.util.ArraySet<com.android.server.usage.BroadcastEvent> broadcastEvents = this.mBroadcastEvents.valueAt(i);
            for (int j = broadcastEvents.size() - 1; j >= 0; j--) {
                if (broadcastEvents.valueAt(j).getSourceUid() == uid) {
                    broadcastEvents.removeAt(j);
                }
            }
        }
    }

    void dump(com.android.internal.util.IndentingPrintWriter ipw) {
        for (int i = 0; i < this.mBroadcastEvents.size(); i++) {
            java.lang.String packageName = this.mBroadcastEvents.keyAt(i);
            android.util.ArraySet<com.android.server.usage.BroadcastEvent> broadcastEvents = this.mBroadcastEvents.valueAt(i);
            ipw.println(packageName + ":");
            ipw.increaseIndent();
            if (broadcastEvents.size() == 0) {
                ipw.println("<empty>");
            } else {
                for (int j = 0; j < broadcastEvents.size(); j++) {
                    com.android.server.usage.BroadcastEvent broadcastEvent = broadcastEvents.valueAt(j);
                    ipw.println(broadcastEvent);
                    ipw.increaseIndent();
                    android.util.LongArrayQueue timestampsMs = broadcastEvent.getTimestampsMs();
                    for (int timestampIdx = 0; timestampIdx < timestampsMs.size(); timestampIdx++) {
                        if (timestampIdx > 0) {
                            ipw.print(',');
                        }
                        long timestampMs = timestampsMs.get(timestampIdx);
                        android.util.TimeUtils.formatDuration(timestampMs, ipw);
                    }
                    ipw.println();
                    ipw.decreaseIndent();
                }
            }
            ipw.decreaseIndent();
        }
    }
}
