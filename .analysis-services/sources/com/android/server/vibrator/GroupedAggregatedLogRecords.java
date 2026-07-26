package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
abstract class GroupedAggregatedLogRecords<T extends com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord> {
    private final int mAggregationTimeLimitMs;
    private final android.util.SparseArray<java.util.ArrayDeque<com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<T>>> mGroupedRecords = new android.util.SparseArray<>();
    private final int mSizeLimit;

    interface SingleLogRecord {
        void dump(android.util.IndentingPrintWriter indentingPrintWriter);

        void dump(android.util.proto.ProtoOutputStream protoOutputStream, long j);

        long getCreateUptimeMs();

        int getGroupKey();

        boolean mayAggregate(com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord singleLogRecord);
    }

    abstract void dumpGroupHeader(android.util.IndentingPrintWriter indentingPrintWriter, int i);

    abstract long findGroupKeyProtoFieldId(int i);

    GroupedAggregatedLogRecords(int sizeLimit, int aggregationTimeLimitMs) {
        this.mSizeLimit = sizeLimit;
        this.mAggregationTimeLimitMs = aggregationTimeLimitMs;
    }

    final synchronized com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<T> add(T record) {
        int groupKey = record.getGroupKey();
        if (!this.mGroupedRecords.contains(groupKey)) {
            this.mGroupedRecords.put(groupKey, new java.util.ArrayDeque<>(this.mSizeLimit));
        }
        java.util.ArrayDeque<com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<T>> records = this.mGroupedRecords.get(groupKey);
        if (this.mAggregationTimeLimitMs > 0 && !records.isEmpty()) {
            com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<T> lastAggregatedRecord = records.getLast();
            if (lastAggregatedRecord.mayAggregate(record, this.mAggregationTimeLimitMs)) {
                lastAggregatedRecord.record(record);
                return null;
            }
        }
        com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<T> removedRecord = null;
        if (records.size() >= this.mSizeLimit) {
            removedRecord = records.removeFirst();
        }
        records.addLast(new com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<>(record));
        return removedRecord;
    }

    final synchronized void dump(android.util.IndentingPrintWriter pw) {
        for (int i = 0; i < this.mGroupedRecords.size(); i++) {
            dumpGroupHeader(pw, this.mGroupedRecords.keyAt(i));
            pw.increaseIndent();
            for (com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<T> records : this.mGroupedRecords.valueAt(i)) {
                records.dump(pw);
            }
            pw.decreaseIndent();
            pw.println();
        }
    }

    final synchronized void dump(android.util.proto.ProtoOutputStream proto) {
        for (int i = 0; i < this.mGroupedRecords.size(); i++) {
            long fieldId = findGroupKeyProtoFieldId(this.mGroupedRecords.keyAt(i));
            for (com.android.server.vibrator.GroupedAggregatedLogRecords.AggregatedLogRecord<T> records : this.mGroupedRecords.valueAt(i)) {
                records.dump(proto, fieldId);
            }
        }
    }

    static final class AggregatedLogRecord<T extends com.android.server.vibrator.GroupedAggregatedLogRecords.SingleLogRecord> {
        private int mCount = 1;
        private final T mFirst;
        private T mLatest;

        AggregatedLogRecord(T record) {
            this.mFirst = record;
            this.mLatest = record;
        }

        T getLatest() {
            return this.mLatest;
        }

        synchronized boolean mayAggregate(T record, long timeLimitMs) {
            long timeDeltaMs;
            timeDeltaMs = java.lang.Math.abs(this.mLatest.getCreateUptimeMs() - record.getCreateUptimeMs());
            return this.mLatest.mayAggregate(record) && timeDeltaMs < timeLimitMs;
        }

        synchronized void record(T record) {
            this.mLatest = record;
            this.mCount++;
        }

        synchronized void dump(android.util.IndentingPrintWriter pw) {
            this.mFirst.dump(pw);
            if (this.mCount == 1) {
                return;
            }
            if (this.mCount > 2) {
                pw.println("-> Skipping " + (this.mCount - 2) + " aggregated entries, latest:");
            }
            this.mLatest.dump(pw);
        }

        synchronized void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
            this.mFirst.dump(proto, fieldId);
            if (this.mCount > 1) {
                this.mLatest.dump(proto, fieldId);
            }
        }
    }
}
