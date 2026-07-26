package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class EventList {
    private final java.util.List<com.android.server.people.data.Event> mEvents = new java.util.ArrayList();

    EventList() {
    }

    void add(com.android.server.people.data.Event event) {
        int index = firstIndexOnOrAfter(event.getTimestamp());
        if (index < this.mEvents.size() && this.mEvents.get(index).getTimestamp() == event.getTimestamp() && isDuplicate(event, index)) {
            return;
        }
        this.mEvents.add(index, event);
    }

    void addAll(java.util.List<com.android.server.people.data.Event> events) {
        for (com.android.server.people.data.Event event : events) {
            add(event);
        }
    }

    java.util.List<com.android.server.people.data.Event> queryEvents(java.util.Set<java.lang.Integer> eventTypes, long fromTimestamp, long toTimestamp) {
        int fromIndex = firstIndexOnOrAfter(fromTimestamp);
        if (fromIndex == this.mEvents.size()) {
            return new java.util.ArrayList();
        }
        int toIndex = firstIndexOnOrAfter(toTimestamp);
        if (toIndex < fromIndex) {
            return new java.util.ArrayList();
        }
        java.util.List<com.android.server.people.data.Event> result = new java.util.ArrayList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            com.android.server.people.data.Event e = this.mEvents.get(i);
            if (eventTypes.contains(java.lang.Integer.valueOf(e.getType()))) {
                result.add(e);
            }
        }
        return result;
    }

    void clear() {
        this.mEvents.clear();
    }

    java.util.List<com.android.server.people.data.Event> getAllEvents() {
        return com.android.internal.util.CollectionUtils.copyOf(this.mEvents);
    }

    void removeOldEvents(long cutOffThreshold) {
        int cutOffIndex = firstIndexOnOrAfter(cutOffThreshold);
        if (cutOffIndex == 0) {
            return;
        }
        int eventsSize = this.mEvents.size();
        if (cutOffIndex == eventsSize) {
            this.mEvents.clear();
            return;
        }
        int i = 0;
        while (cutOffIndex < eventsSize) {
            this.mEvents.set(i, this.mEvents.get(cutOffIndex));
            i++;
            cutOffIndex++;
        }
        if (eventsSize > i) {
            this.mEvents.subList(i, eventsSize).clear();
        }
    }

    private int firstIndexOnOrAfter(long timestamp) {
        int result = this.mEvents.size();
        int low = 0;
        int high = this.mEvents.size() - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            if (this.mEvents.get(mid).getTimestamp() >= timestamp) {
                high = mid - 1;
                result = mid;
            } else {
                low = mid + 1;
            }
        }
        return result;
    }

    private boolean isDuplicate(com.android.server.people.data.Event event, int startIndex) {
        int size = this.mEvents.size();
        int index = startIndex;
        while (index < size && this.mEvents.get(index).getTimestamp() <= event.getTimestamp()) {
            int index2 = index + 1;
            if (this.mEvents.get(index).getType() != event.getType()) {
                index = index2;
            } else {
                return true;
            }
        }
        return false;
    }
}
