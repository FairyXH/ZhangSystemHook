package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public interface EventHistory {
    com.android.server.people.data.EventIndex getEventIndex(int i);

    com.android.server.people.data.EventIndex getEventIndex(java.util.Set<java.lang.Integer> set);

    java.util.List<com.android.server.people.data.Event> queryEvents(java.util.Set<java.lang.Integer> set, long j, long j2);
}
