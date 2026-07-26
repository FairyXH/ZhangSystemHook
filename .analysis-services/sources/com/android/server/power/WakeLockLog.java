package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
final class WakeLockLog {
    private static final boolean DEBUG = false;
    private static final int FLAG_ACQUIRE_CAUSES_WAKEUP = 16;
    private static final int FLAG_ON_AFTER_RELEASE = 8;
    private static final int FLAG_SYSTEM_WAKELOCK = 32;
    private static final int LEVEL_DOZE_WAKE_LOCK = 6;
    private static final int LEVEL_DRAW_WAKE_LOCK = 7;
    private static final int LEVEL_FULL_WAKE_LOCK = 2;
    private static final int LEVEL_PARTIAL_WAKE_LOCK = 1;
    private static final int LEVEL_PROXIMITY_SCREEN_OFF_WAKE_LOCK = 5;
    private static final int LEVEL_SCREEN_BRIGHT_WAKE_LOCK = 4;
    private static final int LEVEL_SCREEN_DIM_WAKE_LOCK = 3;
    private static final int LEVEL_SCREEN_TIMEOUT_OVERRIDE_WAKE_LOCK = 0;
    private static final int LOG_SIZE = 10240;
    private static final int LOG_SIZE_MIN = 10;
    private static final int MASK_LOWER_6_BITS = 63;
    private static final int MASK_LOWER_7_BITS = 127;
    private static final int MAX_LOG_ENTRY_BYTE_SIZE = 9;
    private static final java.lang.String TAG = "PowerManagerService.WLLog";
    private static final int TAG_DATABASE_SIZE = 128;
    private static final int TAG_DATABASE_SIZE_MAX = 128;
    private static final int TYPE_ACQUIRE = 1;
    private static final int TYPE_RELEASE = 2;
    private static final int TYPE_TIME_RESET = 0;
    private final android.content.Context mContext;
    private final java.text.SimpleDateFormat mDumpsysDateFormat;
    private final com.android.server.power.WakeLockLog.Injector mInjector;
    private final java.lang.Object mLock;
    private final com.android.server.power.WakeLockLog.TheLog mLog;
    private final com.android.server.power.WakeLockLog.TagDatabase mTagDatabase;
    private static final java.lang.String[] LEVEL_TO_STRING = {"override", "partial", "full", "screen-dim", "screen-bright", "prox", "doze", "draw"};
    private static final java.lang.String[] REDUCED_TAG_PREFIXES = {"*job*/", "*gms_scheduler*/", "IntentOp:"};
    private static final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    WakeLockLog(android.content.Context context) {
        this(new com.android.server.power.WakeLockLog.Injector(), context);
    }

    WakeLockLog(com.android.server.power.WakeLockLog.Injector injector, android.content.Context context) {
        this.mLock = new java.lang.Object();
        this.mInjector = injector;
        this.mTagDatabase = new com.android.server.power.WakeLockLog.TagDatabase(injector);
        com.android.server.power.WakeLockLog.EntryByteTranslator translator = new com.android.server.power.WakeLockLog.EntryByteTranslator(this.mTagDatabase);
        this.mLog = new com.android.server.power.WakeLockLog.TheLog(injector, translator, this.mTagDatabase);
        this.mDumpsysDateFormat = injector.getDateFormat();
        this.mContext = context;
    }

    public void onWakeLockAcquired(java.lang.String tag, int ownerUid, int flags, long eventTime) {
        onWakeLockEvent(1, tag, ownerUid, flags, eventTime);
    }

    public void onWakeLockReleased(java.lang.String tag, int ownerUid, long eventTime) {
        onWakeLockEvent(2, tag, ownerUid, 0, eventTime);
    }

    public void dump(java.io.PrintWriter pw) {
        dump(pw, false);
    }

    void dump(java.io.PrintWriter pw, boolean includeTagDb) {
        try {
            synchronized (this.mLock) {
                pw.println("Wake Lock Log");
                int numEvents = 0;
                int numResets = 0;
                android.util.SparseArray<java.lang.String[]> uidToPackagesCache = new android.util.SparseArray<>();
                for (int i = 0; i < this.mLog.mSavedAcquisitions.size(); i++) {
                    numEvents++;
                    com.android.server.power.WakeLockLog.LogEntry entry = (com.android.server.power.WakeLockLog.LogEntry) this.mLog.mSavedAcquisitions.get(i);
                    entry.updatePackageName(uidToPackagesCache, this.mContext.getPackageManager());
                    entry.dump(pw, this.mDumpsysDateFormat);
                }
                com.android.server.power.WakeLockLog.LogEntry tempEntry = new com.android.server.power.WakeLockLog.LogEntry();
                java.util.Iterator<com.android.server.power.WakeLockLog.LogEntry> iterator = this.mLog.getAllItems(tempEntry);
                while (iterator.hasNext()) {
                    com.android.server.power.WakeLockLog.LogEntry entry2 = iterator.next();
                    if (entry2 != null) {
                        if (entry2.type == 0) {
                            numResets++;
                        } else {
                            numEvents++;
                            entry2.updatePackageName(uidToPackagesCache, this.mContext.getPackageManager());
                            entry2.dump(pw, this.mDumpsysDateFormat);
                        }
                    }
                }
                pw.println("  -");
                pw.println("  Events: " + numEvents + ", Time-Resets: " + numResets);
                pw.println("  Buffer, Bytes used: " + this.mLog.getUsedBufferSize());
                if (includeTagDb) {
                    pw.println("  " + this.mTagDatabase);
                }
            }
        } catch (java.lang.Exception e) {
            pw.println("Exception dumping wake-lock log: " + e.toString());
        }
    }

    private void onWakeLockEvent(int eventType, java.lang.String tag, int ownerUid, int flags, long eventTime) {
        int translatedFlags;
        if (tag == null) {
            android.util.Slog.w(TAG, "Insufficient data to log wakelock [tag: " + tag + ", ownerUid: " + ownerUid + ", flags: 0x" + java.lang.Integer.toHexString(flags));
            return;
        }
        long time = eventTime == -1 ? this.mInjector.currentTimeMillis() : eventTime;
        if (eventType == 1) {
            translatedFlags = translateFlagsFromPowerManager(flags);
        } else {
            translatedFlags = 0;
        }
        handleWakeLockEventInternal(eventType, tagNameReducer(tag), ownerUid, translatedFlags, time);
    }

    private void handleWakeLockEventInternal(int eventType, java.lang.String tag, int ownerUid, int flags, long time) {
        synchronized (this.mLock) {
            com.android.server.power.WakeLockLog.TagData tagData = this.mTagDatabase.findOrCreateTag(tag, ownerUid, true);
            this.mLog.addEntry(new com.android.server.power.WakeLockLog.LogEntry(time, eventType, tagData, flags));
        }
    }

    int translateFlagsFromPowerManager(int flags) {
        int newFlags = 0;
        switch (65535 & flags) {
            case 1:
                newFlags = 1;
                break;
            case 6:
                newFlags = 3;
                break;
            case 10:
                newFlags = 4;
                break;
            case 26:
                newFlags = 2;
                break;
            case 32:
                newFlags = 5;
                break;
            case 64:
                newFlags = 6;
                break;
            case 128:
                newFlags = 7;
                break;
            case 256:
                newFlags = 0;
                break;
            default:
                android.util.Slog.w(TAG, "Unsupported lock level for logging, flags: " + flags);
                break;
        }
        if ((268435456 & flags) != 0) {
            newFlags |= 16;
        }
        if ((536870912 & flags) != 0) {
            newFlags |= 8;
        }
        if ((Integer.MIN_VALUE & flags) != 0) {
            return newFlags | 32;
        }
        return newFlags;
    }

    private java.lang.String tagNameReducer(java.lang.String tag) {
        if (tag == null) {
            return null;
        }
        java.lang.String reduciblePrefix = null;
        java.lang.String[] strArr = REDUCED_TAG_PREFIXES;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            java.lang.String reducedTagPrefix = strArr[i];
            if (!tag.startsWith(reducedTagPrefix)) {
                i++;
            } else {
                reduciblePrefix = reducedTagPrefix;
                break;
            }
        }
        if (reduciblePrefix != null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append((java.lang.CharSequence) tag, 0, reduciblePrefix.length());
            int end = java.lang.Math.max(tag.lastIndexOf(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER), tag.lastIndexOf("."));
            boolean printNext = true;
            int index = sb.length();
            while (index < end) {
                char c = tag.charAt(index);
                boolean isMarker = c == '.' || c == '/';
                if (isMarker || printNext) {
                    sb.append(c);
                }
                printNext = isMarker;
                index++;
            }
            sb.append(tag.substring(index));
            return sb.toString();
        }
        return tag;
    }

    static class LogEntry {
        public int flags;
        public java.lang.String packageName;
        public com.android.server.power.WakeLockLog.TagData tag;
        public long time;
        public int type;

        LogEntry() {
        }

        LogEntry(long time, int type, com.android.server.power.WakeLockLog.TagData tag, int flags) {
            set(time, type, tag, flags);
        }

        public void set(long time, int type, com.android.server.power.WakeLockLog.TagData tag, int flags) {
            this.time = time;
            this.type = type;
            this.tag = tag;
            this.flags = flags;
        }

        public void dump(java.io.PrintWriter pw, java.text.SimpleDateFormat dateFormat) {
            pw.println("  " + toStringInternal(dateFormat));
        }

        public java.lang.String toString() {
            return toStringInternal(com.android.server.power.WakeLockLog.DATE_FORMAT);
        }

        private java.lang.String toStringInternal(java.text.SimpleDateFormat dateFormat) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            if (this.type == 0) {
                return dateFormat.format(new java.util.Date(this.time)) + " - RESET";
            }
            sb.append(dateFormat.format(new java.util.Date(this.time))).append(" - ").append(this.tag == null ? "---" : java.lang.Integer.valueOf(this.tag.ownerUid));
            if (this.packageName != null) {
                sb.append(" (");
                sb.append(this.packageName);
                sb.append(")");
            }
            sb.append(" - ").append(this.type == 1 ? "ACQ" : "REL").append(" ").append(this.tag == null ? "UNKNOWN" : this.tag.tag);
            if (this.type == 1) {
                sb.append(" (");
                flagsToString(sb);
                sb.append(")");
            }
            return sb.toString();
        }

        private void flagsToString(java.lang.StringBuilder sb) {
            sb.append(com.android.server.power.WakeLockLog.LEVEL_TO_STRING[this.flags & 7]);
            if ((this.flags & 8) == 8) {
                sb.append(",on-after-release");
            }
            if ((this.flags & 16) == 16) {
                sb.append(",acq-causes-wake");
            }
            if ((this.flags & 32) == 32) {
                sb.append(",system-wakelock");
            }
        }

        public void updatePackageName(android.util.SparseArray<java.lang.String[]> uidToPackagesCache, android.content.pm.PackageManager packageManager) {
            java.lang.String[] packages;
            if (this.tag == null) {
                return;
            }
            if (uidToPackagesCache.contains(this.tag.ownerUid)) {
                packages = uidToPackagesCache.get(this.tag.ownerUid);
            } else {
                packages = packageManager.getPackagesForUid(this.tag.ownerUid);
                uidToPackagesCache.put(this.tag.ownerUid, packages);
            }
            if (packages != null && packages.length > 0) {
                this.packageName = packages[0];
                if (packages.length > 1) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    sb.append(this.packageName).append(",...");
                    this.packageName = sb.toString();
                }
            }
        }
    }

    static class EntryByteTranslator {
        static final int ERROR_TIME_IS_NEGATIVE = -1;
        static final int ERROR_TIME_TOO_LARGE = -2;
        private final com.android.server.power.WakeLockLog.TagDatabase mTagDatabase;

        EntryByteTranslator(com.android.server.power.WakeLockLog.TagDatabase tagDatabase) {
            this.mTagDatabase = tagDatabase;
        }

        com.android.server.power.WakeLockLog.LogEntry fromBytes(byte[] bytes, long timeReference, com.android.server.power.WakeLockLog.LogEntry entryToReuse) {
            int type;
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            com.android.server.power.WakeLockLog.LogEntry entry = entryToReuse != null ? entryToReuse : new com.android.server.power.WakeLockLog.LogEntry();
            int type2 = (bytes[0] >> 6) & 3;
            if ((type2 & 2) != 2) {
                type = type2;
            } else {
                type = 2;
            }
            switch (type) {
                case 0:
                    if (bytes.length >= 9) {
                        long time = ((((long) bytes[1]) & 255) << 56) | ((((long) bytes[2]) & 255) << 48) | ((((long) bytes[3]) & 255) << 40) | ((((long) bytes[4]) & 255) << 32) | ((((long) bytes[5]) & 255) << 24) | ((((long) bytes[6]) & 255) << 16) | ((((long) bytes[7]) & 255) << 8) | (((long) bytes[8]) & 255);
                        entry.set(time, 0, null, 0);
                        return entry;
                    }
                    return null;
                case 1:
                    if (bytes.length >= 3) {
                        int flags = bytes[0] & 63;
                        int tagIndex = bytes[1] & 127;
                        com.android.server.power.WakeLockLog.TagData tag = this.mTagDatabase.getTag(tagIndex);
                        long time2 = ((long) (bytes[2] & 255)) + timeReference;
                        entry.set(time2, 1, tag, flags);
                        return entry;
                    }
                    return null;
                case 2:
                    if (bytes.length >= 2) {
                        int tagIndex2 = bytes[0] & 127;
                        com.android.server.power.WakeLockLog.TagData tag2 = this.mTagDatabase.getTag(tagIndex2);
                        long time3 = ((long) (bytes[1] & 255)) + timeReference;
                        entry.set(time3, 2, tag2, 0);
                        return entry;
                    }
                    return null;
                default:
                    android.util.Slog.w(com.android.server.power.WakeLockLog.TAG, "Type not recognized [" + type + "]", new java.lang.Exception());
                    return null;
            }
        }

        int toBytes(com.android.server.power.WakeLockLog.LogEntry entry, byte[] bytes, long timeReference) {
            int sizeNeeded;
            switch (entry.type) {
                case 0:
                    sizeNeeded = 9;
                    long time = entry.time;
                    if (bytes != null && bytes.length >= 9) {
                        bytes[0] = 0;
                        bytes[1] = (byte) ((time >> 56) & 255);
                        bytes[2] = (byte) ((time >> 48) & 255);
                        bytes[3] = (byte) ((time >> 40) & 255);
                        bytes[4] = (byte) ((time >> 32) & 255);
                        bytes[5] = (byte) ((time >> 24) & 255);
                        bytes[6] = (byte) ((time >> 16) & 255);
                        bytes[7] = (byte) ((time >> 8) & 255);
                        bytes[8] = (byte) (time & 255);
                    }
                    return sizeNeeded;
                case 1:
                    sizeNeeded = 3;
                    if (bytes != null && bytes.length >= 3) {
                        int relativeTime = getRelativeTime(timeReference, entry.time);
                        if (relativeTime < 0) {
                            return relativeTime;
                        }
                        bytes[0] = (byte) ((entry.flags & 63) | 64);
                        bytes[1] = (byte) this.mTagDatabase.getTagIndex(entry.tag);
                        bytes[2] = (byte) (relativeTime & 255);
                    }
                    return sizeNeeded;
                case 2:
                    sizeNeeded = 2;
                    if (bytes != null && bytes.length >= 2) {
                        int relativeTime2 = getRelativeTime(timeReference, entry.time);
                        if (relativeTime2 < 0) {
                            return relativeTime2;
                        }
                        bytes[0] = (byte) (this.mTagDatabase.getTagIndex(entry.tag) | 128);
                        bytes[1] = (byte) (relativeTime2 & 255);
                    }
                    return sizeNeeded;
                default:
                    throw new java.lang.RuntimeException("Unknown type " + entry);
            }
        }

        private int getRelativeTime(long timeReference, long time) {
            if (time < timeReference) {
                return -1;
            }
            long relativeTime = time - timeReference;
            if (relativeTime > 255) {
                return -2;
            }
            return (int) relativeTime;
        }
    }

    static class TheLog {
        private final byte[] mBuffer;
        private final java.util.List<com.android.server.power.WakeLockLog.LogEntry> mSavedAcquisitions;
        private final com.android.server.power.WakeLockLog.TagDatabase mTagDatabase;
        private final com.android.server.power.WakeLockLog.EntryByteTranslator mTranslator;
        private final byte[] mTempBuffer = new byte[9];
        private final byte[] mReadWriteTempBuffer = new byte[9];
        private int mStart = 0;
        private int mEnd = 0;
        private long mStartTime = 0;
        private long mLatestTime = 0;
        private long mChangeCount = 0;

        TheLog(com.android.server.power.WakeLockLog.Injector injector, com.android.server.power.WakeLockLog.EntryByteTranslator translator, com.android.server.power.WakeLockLog.TagDatabase tagDatabase) {
            int logSize = java.lang.Math.max(injector.getLogSize(), 10);
            this.mBuffer = new byte[logSize];
            this.mTranslator = translator;
            this.mTagDatabase = tagDatabase;
            this.mTagDatabase.setCallback(new com.android.server.power.WakeLockLog.TagDatabase.Callback() { // from class: com.android.server.power.WakeLockLog.TheLog.1
                @Override // com.android.server.power.WakeLockLog.TagDatabase.Callback
                public void onIndexRemoved(int index) {
                    com.android.server.power.WakeLockLog.TheLog.this.removeTagIndex(index);
                }
            });
            this.mSavedAcquisitions = new java.util.ArrayList();
        }

        int getUsedBufferSize() {
            return this.mBuffer.length - getAvailableSpace();
        }

        void addEntry(com.android.server.power.WakeLockLog.LogEntry entry) {
            if (isBufferEmpty()) {
                long j = entry.time;
                this.mLatestTime = j;
                this.mStartTime = j;
            }
            int size = this.mTranslator.toBytes(entry, this.mTempBuffer, this.mLatestTime);
            if (size == -1) {
                return;
            }
            if (size == -2) {
                addEntry(new com.android.server.power.WakeLockLog.LogEntry(entry.time, 0, null, 0));
                size = this.mTranslator.toBytes(entry, this.mTempBuffer, this.mLatestTime);
            }
            if (size > 9 || size <= 0) {
                android.util.Slog.w(com.android.server.power.WakeLockLog.TAG, "Log entry size is out of expected range: " + size);
                return;
            }
            if (!makeSpace(size)) {
                return;
            }
            writeBytesAt(this.mEnd, this.mTempBuffer, size);
            this.mEnd = (this.mEnd + size) % this.mBuffer.length;
            this.mLatestTime = entry.time;
            com.android.server.power.WakeLockLog.TagDatabase.updateTagTime(entry.tag, entry.time);
            this.mChangeCount++;
        }

        java.util.Iterator<com.android.server.power.WakeLockLog.LogEntry> getAllItems(final com.android.server.power.WakeLockLog.LogEntry tempEntry) {
            return new java.util.Iterator<com.android.server.power.WakeLockLog.LogEntry>() { // from class: com.android.server.power.WakeLockLog.TheLog.2
                private final long mChangeValue;
                private int mCurrent;
                private long mCurrentTimeReference;

                {
                    this.mCurrent = com.android.server.power.WakeLockLog.TheLog.this.mStart;
                    this.mCurrentTimeReference = com.android.server.power.WakeLockLog.TheLog.this.mStartTime;
                    this.mChangeValue = com.android.server.power.WakeLockLog.TheLog.this.mChangeCount;
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    checkState();
                    return this.mCurrent != com.android.server.power.WakeLockLog.TheLog.this.mEnd;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public com.android.server.power.WakeLockLog.LogEntry next() {
                    checkState();
                    if (!hasNext()) {
                        throw new java.util.NoSuchElementException("No more entries left.");
                    }
                    com.android.server.power.WakeLockLog.LogEntry entry = com.android.server.power.WakeLockLog.TheLog.this.readEntryAt(this.mCurrent, this.mCurrentTimeReference, tempEntry);
                    int size = com.android.server.power.WakeLockLog.TheLog.this.mTranslator.toBytes(entry, null, com.android.server.power.WakeLockLog.TheLog.this.mStartTime);
                    this.mCurrent = (this.mCurrent + size) % com.android.server.power.WakeLockLog.TheLog.this.mBuffer.length;
                    this.mCurrentTimeReference = entry.time;
                    return entry;
                }

                public java.lang.String toString() {
                    return "@" + this.mCurrent;
                }

                private void checkState() {
                    if (this.mChangeValue != com.android.server.power.WakeLockLog.TheLog.this.mChangeCount) {
                        throw new java.util.ConcurrentModificationException("Buffer modified, old change: " + this.mChangeValue + ", new change: " + com.android.server.power.WakeLockLog.TheLog.this.mChangeCount);
                    }
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeTagIndex(int tagIndex) {
            if (isBufferEmpty()) {
                return;
            }
            int readIndex = this.mStart;
            long timeReference = this.mStartTime;
            com.android.server.power.WakeLockLog.LogEntry reusableEntryInstance = new com.android.server.power.WakeLockLog.LogEntry();
            while (readIndex != this.mEnd) {
                com.android.server.power.WakeLockLog.LogEntry entry = readEntryAt(readIndex, timeReference, reusableEntryInstance);
                if (entry == null) {
                    android.util.Slog.w(com.android.server.power.WakeLockLog.TAG, "Entry is unreadable - Unexpected @ " + readIndex);
                    return;
                }
                if (entry.tag != null && entry.tag.index == tagIndex) {
                    entry.tag = null;
                    writeEntryAt(readIndex, entry, timeReference);
                }
                timeReference = entry.time;
                int entryByteSize = this.mTranslator.toBytes(entry, null, 0L);
                readIndex = (readIndex + entryByteSize) % this.mBuffer.length;
            }
        }

        private boolean makeSpace(int spaceNeeded) {
            if (this.mBuffer.length < spaceNeeded + 1) {
                return false;
            }
            while (getAvailableSpace() < spaceNeeded + 1) {
                removeOldestItem();
            }
            return true;
        }

        private int getAvailableSpace() {
            return this.mEnd > this.mStart ? this.mBuffer.length - (this.mEnd - this.mStart) : this.mEnd < this.mStart ? this.mStart - this.mEnd : this.mBuffer.length;
        }

        private void removeOldestItem() {
            if (isBufferEmpty()) {
                return;
            }
            com.android.server.power.WakeLockLog.LogEntry entry = readEntryAt(this.mStart, this.mStartTime, null);
            if (entry.type == 1) {
                this.mSavedAcquisitions.add(entry);
            } else if (entry.type == 2) {
                int i = 0;
                while (true) {
                    if (i >= this.mSavedAcquisitions.size()) {
                        break;
                    }
                    if (!java.util.Objects.equals(this.mSavedAcquisitions.get(i).tag, entry.tag)) {
                        i++;
                    } else {
                        this.mSavedAcquisitions.remove(i);
                        break;
                    }
                }
            }
            int size = this.mTranslator.toBytes(entry, null, this.mStartTime);
            this.mStart = (this.mStart + size) % this.mBuffer.length;
            this.mStartTime = entry.time;
            this.mChangeCount++;
        }

        private boolean isBufferEmpty() {
            return this.mStart == this.mEnd;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.power.WakeLockLog.LogEntry readEntryAt(int index, long timeReference, com.android.server.power.WakeLockLog.LogEntry entryToSet) {
            int indexIntoMainBuffer;
            for (int i = 0; i < 9 && (indexIntoMainBuffer = (index + i) % this.mBuffer.length) != this.mEnd; i++) {
                this.mReadWriteTempBuffer[i] = this.mBuffer[indexIntoMainBuffer];
            }
            return this.mTranslator.fromBytes(this.mReadWriteTempBuffer, timeReference, entryToSet);
        }

        private void writeEntryAt(int index, com.android.server.power.WakeLockLog.LogEntry entry, long timeReference) {
            int size = this.mTranslator.toBytes(entry, this.mReadWriteTempBuffer, timeReference);
            if (size > 0) {
                writeBytesAt(index, this.mReadWriteTempBuffer, size);
            }
        }

        private void writeBytesAt(int index, byte[] buffer, int size) {
            for (int i = 0; i < size; i++) {
                int indexIntoMainBuffer = (index + i) % this.mBuffer.length;
                this.mBuffer[indexIntoMainBuffer] = buffer[i];
            }
        }
    }

    static class TagDatabase {
        private final com.android.server.power.WakeLockLog.TagData[] mArray;
        private com.android.server.power.WakeLockLog.TagDatabase.Callback mCallback;
        private final int mInvalidIndex;

        interface Callback {
            void onIndexRemoved(int i);
        }

        TagDatabase(com.android.server.power.WakeLockLog.Injector injector) {
            int size = java.lang.Math.min(injector.getTagDatabaseSize(), 128);
            this.mArray = new com.android.server.power.WakeLockLog.TagData[size - 1];
            this.mInvalidIndex = size - 1;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("Tag Database: size(").append(this.mArray.length).append(")");
            int entries = 0;
            int byteEstimate = 0;
            int tagSize = 0;
            int tags = 0;
            for (com.android.server.power.WakeLockLog.TagData tagData : this.mArray) {
                byteEstimate += 8;
                if (tagData != null) {
                    entries++;
                    byteEstimate += tagData.getByteSize();
                    if (tagData.tag != null) {
                        tags++;
                        tagSize += tagData.tag.length();
                    }
                }
            }
            sb.append(", entries: ").append(entries);
            sb.append(", Bytes used: ").append(byteEstimate);
            return sb.toString();
        }

        public void setCallback(com.android.server.power.WakeLockLog.TagDatabase.Callback callback) {
            this.mCallback = callback;
        }

        public com.android.server.power.WakeLockLog.TagData getTag(int index) {
            if (index < 0 || index >= this.mArray.length || index == this.mInvalidIndex) {
                return null;
            }
            return this.mArray[index];
        }

        public com.android.server.power.WakeLockLog.TagData getTag(java.lang.String tag, int ownerUid) {
            return findOrCreateTag(tag, ownerUid, false);
        }

        public int getTagIndex(com.android.server.power.WakeLockLog.TagData tagData) {
            return tagData == null ? this.mInvalidIndex : tagData.index;
        }

        public com.android.server.power.WakeLockLog.TagData findOrCreateTag(java.lang.String tagStr, int ownerUid, boolean shouldCreate) {
            int firstAvailable = -1;
            com.android.server.power.WakeLockLog.TagData oldest = null;
            int oldestIndex = -1;
            com.android.server.power.WakeLockLog.TagData tag = new com.android.server.power.WakeLockLog.TagData(tagStr, ownerUid);
            for (int i = 0; i < this.mArray.length; i++) {
                com.android.server.power.WakeLockLog.TagData current = this.mArray[i];
                if (tag.equals(current)) {
                    return current;
                }
                if (shouldCreate) {
                    if (current != null) {
                        if (oldest == null || current.lastUsedTime < oldest.lastUsedTime) {
                            oldestIndex = i;
                            oldest = current;
                        }
                    } else if (firstAvailable == -1) {
                        firstAvailable = i;
                    }
                }
            }
            if (!shouldCreate) {
                return null;
            }
            boolean useOldest = firstAvailable == -1;
            if (useOldest && this.mCallback != null) {
                this.mCallback.onIndexRemoved(oldestIndex);
            }
            setToIndex(tag, firstAvailable != -1 ? firstAvailable : oldestIndex);
            return tag;
        }

        public static void updateTagTime(com.android.server.power.WakeLockLog.TagData tag, long time) {
            if (tag != null) {
                tag.lastUsedTime = time;
            }
        }

        private void setToIndex(com.android.server.power.WakeLockLog.TagData tag, int index) {
            if (index < 0 || index >= this.mArray.length) {
                return;
            }
            com.android.server.power.WakeLockLog.TagData current = this.mArray[index];
            if (current != null) {
                current.index = this.mInvalidIndex;
            }
            this.mArray[index] = tag;
            tag.index = index;
        }
    }

    static class TagData {
        public int index;
        public long lastUsedTime;
        public int ownerUid;
        public java.lang.String tag;

        TagData(java.lang.String tag, int ownerUid) {
            this.tag = tag;
            this.ownerUid = ownerUid;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.power.WakeLockLog.TagData)) {
                return false;
            }
            com.android.server.power.WakeLockLog.TagData other = (com.android.server.power.WakeLockLog.TagData) o;
            return android.text.TextUtils.equals(this.tag, other.tag) && this.ownerUid == other.ownerUid;
        }

        public java.lang.String toString() {
            return "[" + this.ownerUid + " ; " + this.tag + "]";
        }

        int getByteSize() {
            int bytes = 0 + 8;
            return bytes + (this.tag == null ? 0 : this.tag.length() * 2) + 4 + 4 + 8;
        }
    }

    public static class Injector {
        public int getTagDatabaseSize() {
            return 128;
        }

        public int getLogSize() {
            return com.android.server.power.WakeLockLog.LOG_SIZE;
        }

        public long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        public java.text.SimpleDateFormat getDateFormat() {
            return com.android.server.power.WakeLockLog.DATE_FORMAT;
        }
    }
}
