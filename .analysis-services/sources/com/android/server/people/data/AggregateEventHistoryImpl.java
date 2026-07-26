package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class AggregateEventHistoryImpl implements com.android.server.people.data.EventHistory {
    private final java.util.List<com.android.server.people.data.EventHistory> mEventHistoryList = new java.util.ArrayList();

    AggregateEventHistoryImpl() {
    }

    @Override // com.android.server.people.data.EventHistory
    public com.android.server.people.data.EventIndex getEventIndex(int eventType) {
        for (com.android.server.people.data.EventHistory eventHistory : this.mEventHistoryList) {
            com.android.server.people.data.EventIndex eventIndex = eventHistory.getEventIndex(eventType);
            if (!eventIndex.isEmpty()) {
                return eventIndex;
            }
        }
        return com.android.server.people.data.EventIndex.EMPTY;
    }

    @Override // com.android.server.people.data.EventHistory
    public com.android.server.people.data.EventIndex getEventIndex(java.util.Set<java.lang.Integer> eventTypes) {
        com.android.server.people.data.EventIndex merged = null;
        for (com.android.server.people.data.EventHistory eventHistory : this.mEventHistoryList) {
            com.android.server.people.data.EventIndex eventIndex = eventHistory.getEventIndex(eventTypes);
            if (merged == null) {
                merged = eventIndex;
            } else if (!eventIndex.isEmpty()) {
                merged = com.android.server.people.data.EventIndex.combine(merged, eventIndex);
            }
        }
        return merged != null ? merged : com.android.server.people.data.EventIndex.EMPTY;
    }

    @Override // com.android.server.people.data.EventHistory
    public java.util.List<com.android.server.people.data.Event> queryEvents(java.util.Set<java.lang.Integer> eventTypes, long startTime, long endTime) {
        java.util.List<com.android.server.people.data.Event> results = new java.util.ArrayList<>();
        for (com.android.server.people.data.EventHistory eventHistory : this.mEventHistoryList) {
            com.android.server.people.data.EventIndex eventIndex = eventHistory.getEventIndex(eventTypes);
            if (!eventIndex.isEmpty()) {
                java.util.List<com.android.server.people.data.Event> queryResults = eventHistory.queryEvents(eventTypes, startTime, endTime);
                results = combineEventLists(results, queryResults);
            }
        }
        return results;
    }

    void addEventHistory(com.android.server.people.data.EventHistory eventHistory) {
        this.mEventHistoryList.add(eventHistory);
    }

    private java.util.List<com.android.server.people.data.Event> combineEventLists(java.util.List<com.android.server.people.data.Event> lhs, java.util.List<com.android.server.people.data.Event> rhs) {
        java.util.List<com.android.server.people.data.Event> results = new java.util.ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < lhs.size() && j < rhs.size()) {
            if (lhs.get(i).getTimestamp() < rhs.get(j).getTimestamp()) {
                results.add(lhs.get(i));
                i++;
            } else {
                int i2 = j + 1;
                results.add(rhs.get(j));
                j = i2;
            }
        }
        int j2 = lhs.size();
        if (i < j2) {
            results.addAll(lhs.subList(i, lhs.size()));
        } else if (j < rhs.size()) {
            results.addAll(rhs.subList(j, rhs.size()));
        }
        return results;
    }
}
