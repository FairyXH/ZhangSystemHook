package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayGroup {
    private int mChangeCount;
    private final java.util.List<com.android.server.display.LogicalDisplay> mDisplays = new java.util.ArrayList();
    private final int mGroupId;

    DisplayGroup(int groupId) {
        this.mGroupId = groupId;
    }

    int getGroupId() {
        return this.mGroupId;
    }

    void addDisplayLocked(com.android.server.display.LogicalDisplay display) {
        if (!containsLocked(display)) {
            this.mChangeCount++;
            this.mDisplays.add(display);
        }
    }

    boolean containsLocked(com.android.server.display.LogicalDisplay display) {
        return this.mDisplays.contains(display);
    }

    boolean removeDisplayLocked(com.android.server.display.LogicalDisplay display) {
        this.mChangeCount++;
        return this.mDisplays.remove(display);
    }

    boolean isEmptyLocked() {
        return this.mDisplays.isEmpty();
    }

    int getChangeCountLocked() {
        return this.mChangeCount;
    }

    int getSizeLocked() {
        return this.mDisplays.size();
    }

    int getIdLocked(int index) {
        return this.mDisplays.get(index).getDisplayIdLocked();
    }
}
