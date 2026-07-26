package com.android.server.broadcastradio.aidl;

/* JADX INFO: loaded from: classes.dex */
final class ProgramInfoCache {
    private static final int MAX_NUM_MODIFIED_PER_CHUNK = 100;
    private static final int MAX_NUM_REMOVED_PER_CHUNK = 500;
    private static final java.lang.String TAG = "BcRadioAidlSrv.cache";
    private boolean mComplete;
    private final android.hardware.radio.ProgramList.Filter mFilter;
    private final android.util.ArrayMap<android.hardware.radio.ProgramSelector.Identifier, android.util.ArrayMap<android.hardware.radio.UniqueProgramIdentifier, android.hardware.radio.RadioManager.ProgramInfo>> mProgramInfoMap;

    ProgramInfoCache(android.hardware.radio.ProgramList.Filter filter) {
        this.mProgramInfoMap = new android.util.ArrayMap<>();
        this.mComplete = true;
        this.mFilter = filter;
    }

    ProgramInfoCache(android.hardware.radio.ProgramList.Filter filter, boolean complete, android.hardware.radio.RadioManager.ProgramInfo... programInfos) {
        this.mProgramInfoMap = new android.util.ArrayMap<>();
        this.mComplete = true;
        this.mFilter = filter;
        this.mComplete = complete;
        for (android.hardware.radio.RadioManager.ProgramInfo programInfo : programInfos) {
            putInfo(programInfo);
        }
    }

    java.util.List<android.hardware.radio.RadioManager.ProgramInfo> toProgramInfoList() {
        java.util.List<android.hardware.radio.RadioManager.ProgramInfo> programInfoList = new java.util.ArrayList<>();
        for (int index = 0; index < this.mProgramInfoMap.size(); index++) {
            programInfoList.addAll(this.mProgramInfoMap.valueAt(index).values());
        }
        return programInfoList;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("ProgramInfoCache(mComplete = ");
        sb.append(this.mComplete);
        sb.append(", mFilter = ");
        sb.append(this.mFilter);
        sb.append(", mProgramInfoMap = [");
        for (int index = 0; index < this.mProgramInfoMap.size(); index++) {
            android.util.ArrayMap<android.hardware.radio.UniqueProgramIdentifier, android.hardware.radio.RadioManager.ProgramInfo> entries = this.mProgramInfoMap.valueAt(index);
            for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
                sb.append(", ");
                sb.append(entries.valueAt(entryIndex));
            }
        }
        return sb.append("])").toString();
    }

    public boolean isComplete() {
        return this.mComplete;
    }

    public android.hardware.radio.ProgramList.Filter getFilter() {
        return this.mFilter;
    }

    void updateFromHalProgramListChunk(android.hardware.broadcastradio.ProgramListChunk chunk) {
        if (chunk.purge) {
            this.mProgramInfoMap.clear();
        }
        for (int i = 0; i < chunk.modified.length; i++) {
            android.hardware.radio.RadioManager.ProgramInfo programInfo = com.android.server.broadcastradio.aidl.ConversionUtils.programInfoFromHalProgramInfo(chunk.modified[i]);
            if (programInfo == null) {
                com.android.server.utils.Slogf.e(TAG, "Program info in program info %s in chunk is not valid", chunk.modified[i]);
            } else {
                putInfo(programInfo);
            }
        }
        if (chunk.removed != null) {
            for (int i2 = 0; i2 < chunk.removed.length; i2++) {
                this.mProgramInfoMap.remove(com.android.server.broadcastradio.aidl.ConversionUtils.identifierFromHalProgramIdentifier(chunk.removed[i2]));
            }
        }
        this.mComplete = chunk.complete;
    }

    java.util.List<android.hardware.radio.ProgramList.Chunk> filterAndUpdateFromInternal(com.android.server.broadcastradio.aidl.ProgramInfoCache other, boolean purge) {
        return filterAndUpdateFromInternal(other, purge, 100, 500);
    }

    java.util.List<android.hardware.radio.ProgramList.Chunk> filterAndUpdateFromInternal(com.android.server.broadcastradio.aidl.ProgramInfoCache other, boolean purge, int maxNumModifiedPerChunk, int maxNumRemovedPerChunk) {
        if (purge) {
            this.mProgramInfoMap.clear();
        }
        if (this.mProgramInfoMap.isEmpty()) {
            purge = true;
        }
        android.util.ArraySet<android.hardware.radio.RadioManager.ProgramInfo> modified = new android.util.ArraySet<>();
        android.util.ArraySet<android.hardware.radio.UniqueProgramIdentifier> removed = new android.util.ArraySet<>();
        for (int index = 0; index < this.mProgramInfoMap.size(); index++) {
            removed.addAll(this.mProgramInfoMap.valueAt(index).keySet());
        }
        for (int index2 = 0; index2 < other.mProgramInfoMap.size(); index2++) {
            android.hardware.radio.ProgramSelector.Identifier id = other.mProgramInfoMap.keyAt(index2);
            if (passesFilter(id)) {
                android.util.ArrayMap<android.hardware.radio.UniqueProgramIdentifier, android.hardware.radio.RadioManager.ProgramInfo> entries = other.mProgramInfoMap.valueAt(index2);
                for (int entryIndex = 0; entryIndex < entries.size(); entryIndex++) {
                    removed.remove(entries.keyAt(entryIndex));
                    android.hardware.radio.RadioManager.ProgramInfo newInfo = entries.valueAt(entryIndex);
                    if (shouldIncludeInModified(newInfo)) {
                        putInfo(newInfo);
                        modified.add(newInfo);
                    }
                }
            }
        }
        for (int removedIndex = 0; removedIndex < removed.size(); removedIndex++) {
            removeUniqueId(removed.valueAt(removedIndex));
        }
        this.mComplete = other.mComplete;
        return buildChunks(purge, this.mComplete, modified, maxNumModifiedPerChunk, removed, maxNumRemovedPerChunk);
    }

    java.util.List<android.hardware.radio.ProgramList.Chunk> filterAndApplyChunk(android.hardware.broadcastradio.ProgramListChunk chunk) {
        return filterAndApplyChunkInternal(chunk, 100, 500);
    }

    java.util.List<android.hardware.radio.ProgramList.Chunk> filterAndApplyChunkInternal(android.hardware.broadcastradio.ProgramListChunk chunk, int maxNumModifiedPerChunk, int maxNumRemovedPerChunk) {
        if (chunk.purge) {
            this.mProgramInfoMap.clear();
        }
        java.util.Set<android.hardware.radio.RadioManager.ProgramInfo> modified = new android.util.ArraySet<>();
        for (int i = 0; i < chunk.modified.length; i++) {
            android.hardware.radio.RadioManager.ProgramInfo info = com.android.server.broadcastradio.aidl.ConversionUtils.programInfoFromHalProgramInfo(chunk.modified[i]);
            if (info == null) {
                com.android.server.utils.Slogf.w(TAG, "Program info %s in program list chunk is not valid", chunk.modified[i]);
            } else {
                android.hardware.radio.ProgramSelector.Identifier primaryId = info.getSelector().getPrimaryId();
                if (passesFilter(primaryId) && shouldIncludeInModified(info)) {
                    putInfo(info);
                    modified.add(info);
                }
            }
        }
        java.util.Set<android.hardware.radio.UniqueProgramIdentifier> removed = new android.util.ArraySet<>();
        if (chunk.removed != null) {
            for (int i2 = 0; i2 < chunk.removed.length; i2++) {
                android.hardware.radio.ProgramSelector.Identifier removedId = com.android.server.broadcastradio.aidl.ConversionUtils.identifierFromHalProgramIdentifier(chunk.removed[i2]);
                if (removedId == null) {
                    com.android.server.utils.Slogf.w(TAG, "Removed identifier %s in program list chunk is not valid", chunk.modified[i2]);
                } else if (this.mProgramInfoMap.containsKey(removedId)) {
                    removed.addAll(this.mProgramInfoMap.get(removedId).keySet());
                    this.mProgramInfoMap.remove(removedId);
                }
            }
        }
        if (modified.isEmpty() && removed.isEmpty() && this.mComplete == chunk.complete && !chunk.purge) {
            return null;
        }
        this.mComplete = chunk.complete;
        return buildChunks(chunk.purge, this.mComplete, modified, maxNumModifiedPerChunk, removed, maxNumRemovedPerChunk);
    }

    private boolean passesFilter(android.hardware.radio.ProgramSelector.Identifier id) {
        if (this.mFilter == null) {
            return true;
        }
        if (!this.mFilter.getIdentifierTypes().isEmpty() && !this.mFilter.getIdentifierTypes().contains(java.lang.Integer.valueOf(id.getType()))) {
            return false;
        }
        if (this.mFilter.getIdentifiers().isEmpty() || this.mFilter.getIdentifiers().contains(id)) {
            return this.mFilter.areCategoriesIncluded() || !id.isCategoryType();
        }
        return false;
    }

    private void putInfo(android.hardware.radio.RadioManager.ProgramInfo info) {
        android.hardware.radio.ProgramSelector.Identifier primaryId = info.getSelector().getPrimaryId();
        if (!this.mProgramInfoMap.containsKey(primaryId)) {
            this.mProgramInfoMap.put(primaryId, new android.util.ArrayMap<>());
        }
        this.mProgramInfoMap.get(primaryId).put(new android.hardware.radio.UniqueProgramIdentifier(info.getSelector()), info);
    }

    private void removeUniqueId(android.hardware.radio.UniqueProgramIdentifier uniqueId) {
        android.hardware.radio.ProgramSelector.Identifier primaryId = uniqueId.getPrimaryId();
        if (!this.mProgramInfoMap.containsKey(primaryId)) {
            return;
        }
        this.mProgramInfoMap.get(primaryId).remove(uniqueId);
        if (this.mProgramInfoMap.get(primaryId).isEmpty()) {
            this.mProgramInfoMap.remove(primaryId);
        }
    }

    private boolean shouldIncludeInModified(android.hardware.radio.RadioManager.ProgramInfo newInfo) {
        android.hardware.radio.ProgramSelector.Identifier primaryId = newInfo.getSelector().getPrimaryId();
        android.hardware.radio.RadioManager.ProgramInfo oldInfo = null;
        if (this.mProgramInfoMap.containsKey(primaryId)) {
            android.hardware.radio.UniqueProgramIdentifier uniqueId = new android.hardware.radio.UniqueProgramIdentifier(newInfo.getSelector());
            android.hardware.radio.RadioManager.ProgramInfo oldInfo2 = this.mProgramInfoMap.get(primaryId).get(uniqueId);
            oldInfo = oldInfo2;
        }
        if (oldInfo == null) {
            return true;
        }
        if (this.mFilter == null || !this.mFilter.areModificationsExcluded()) {
            return true ^ oldInfo.equals(newInfo);
        }
        return false;
    }

    private static int roundUpFraction(int numerator, int denominator) {
        return (numerator / denominator) + (numerator % denominator > 0 ? 1 : 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [int] */
    /* JADX WARN: Type inference failed for: r1v1, types: [int] */
    /* JADX WARN: Type inference failed for: r1v2, types: [int] */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8 */
    private static java.util.List<android.hardware.radio.ProgramList.Chunk> buildChunks(boolean z, boolean z2, java.util.Collection<android.hardware.radio.RadioManager.ProgramInfo> collection, int i, java.util.Collection<android.hardware.radio.UniqueProgramIdentifier> collection2, int i2) {
        java.util.Collection<android.hardware.radio.UniqueProgramIdentifier> collection3;
        if (!z) {
            collection3 = collection2;
        } else {
            collection3 = null;
        }
        ?? r1 = z;
        ?? Max = collection != null ? java.lang.Math.max((int) r1, roundUpFraction(collection.size(), i)) : r1;
        ?? Max2 = collection3 != null ? java.lang.Math.max((int) Max, roundUpFraction(collection3.size(), i2)) : Max;
        if (Max2 == 0) {
            return new java.util.ArrayList();
        }
        int iRoundUpFraction = 0;
        int iRoundUpFraction2 = 0;
        java.util.Iterator<android.hardware.radio.RadioManager.ProgramInfo> it = null;
        java.util.Iterator<android.hardware.radio.UniqueProgramIdentifier> it2 = null;
        if (collection != null) {
            iRoundUpFraction = roundUpFraction(collection.size(), Max2);
            it = collection.iterator();
        }
        if (collection3 != null) {
            iRoundUpFraction2 = roundUpFraction(collection3.size(), Max2);
            it2 = collection3.iterator();
        }
        java.util.ArrayList arrayList = new java.util.ArrayList((int) Max2);
        int i3 = 0;
        while (i3 < Max2) {
            android.util.ArraySet arraySet = new android.util.ArraySet();
            android.util.ArraySet arraySet2 = new android.util.ArraySet();
            if (it != null) {
                for (int i4 = 0; i4 < iRoundUpFraction && it.hasNext(); i4++) {
                    arraySet.add(it.next());
                }
            }
            if (it2 != null) {
                for (int i5 = 0; i5 < iRoundUpFraction2 && it2.hasNext(); i5++) {
                    arraySet2.add(it2.next());
                }
            }
            arrayList.add(new android.hardware.radio.ProgramList.Chunk(z && i3 == 0, z2 && i3 == Max2 + (-1), arraySet, arraySet2));
            i3++;
        }
        return arrayList;
    }
}
