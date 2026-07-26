package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class EventIndex {
    private static final int RETENTION_DAYS = 63;
    private static final int TIME_SLOT_FOUR_HOURS = 1;
    private static final int TIME_SLOT_ONE_DAY = 0;
    private static final int TIME_SLOT_ONE_HOUR = 2;
    private static final int TIME_SLOT_TWO_MINUTES = 3;
    private static final int TIME_SLOT_TYPES_COUNT = 4;
    private final long[] mEventBitmaps;
    private final com.android.server.people.data.EventIndex.Injector mInjector;
    private long mLastUpdatedTime;
    private final java.lang.Object mLock;
    private static final java.lang.String TAG = com.android.server.people.data.EventIndex.class.getSimpleName();
    static final com.android.server.people.data.EventIndex EMPTY = new com.android.server.people.data.EventIndex();
    private static final java.util.List<java.util.function.Function<java.lang.Long, android.util.Range<java.lang.Long>>> TIME_SLOT_FACTORIES = java.util.Collections.unmodifiableList(java.util.Arrays.asList(new java.util.function.Function() { // from class: com.android.server.people.data.EventIndex$$ExternalSyntheticLambda0
        @Override // java.util.function.Function
        public final java.lang.Object apply(java.lang.Object obj) {
            return com.android.server.people.data.EventIndex.createOneDayLongTimeSlot(((java.lang.Long) obj).longValue());
        }
    }, new java.util.function.Function() { // from class: com.android.server.people.data.EventIndex$$ExternalSyntheticLambda1
        @Override // java.util.function.Function
        public final java.lang.Object apply(java.lang.Object obj) {
            return com.android.server.people.data.EventIndex.createFourHoursLongTimeSlot(((java.lang.Long) obj).longValue());
        }
    }, new java.util.function.Function() { // from class: com.android.server.people.data.EventIndex$$ExternalSyntheticLambda2
        @Override // java.util.function.Function
        public final java.lang.Object apply(java.lang.Object obj) {
            return com.android.server.people.data.EventIndex.createOneHourLongTimeSlot(((java.lang.Long) obj).longValue());
        }
    }, new java.util.function.Function() { // from class: com.android.server.people.data.EventIndex$$ExternalSyntheticLambda3
        @Override // java.util.function.Function
        public final java.lang.Object apply(java.lang.Object obj) {
            return com.android.server.people.data.EventIndex.createTwoMinutesLongTimeSlot(((java.lang.Long) obj).longValue());
        }
    }));

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface TimeSlotType {
    }

    static com.android.server.people.data.EventIndex combine(com.android.server.people.data.EventIndex lhs, com.android.server.people.data.EventIndex rhs) {
        com.android.server.people.data.EventIndex older = lhs.mLastUpdatedTime < rhs.mLastUpdatedTime ? lhs : rhs;
        com.android.server.people.data.EventIndex younger = lhs.mLastUpdatedTime >= rhs.mLastUpdatedTime ? lhs : rhs;
        com.android.server.people.data.EventIndex combined = new com.android.server.people.data.EventIndex(older);
        combined.updateEventBitmaps(younger.mLastUpdatedTime);
        for (int slotType = 0; slotType < 4; slotType++) {
            long[] jArr = combined.mEventBitmaps;
            jArr[slotType] = jArr[slotType] | younger.mEventBitmaps[slotType];
        }
        return combined;
    }

    EventIndex() {
        this(new com.android.server.people.data.EventIndex.Injector());
    }

    EventIndex(com.android.server.people.data.EventIndex from) {
        this(from.mInjector, from.mEventBitmaps, from.mLastUpdatedTime);
    }

    EventIndex(com.android.server.people.data.EventIndex.Injector injector) {
        this(injector, new long[]{0, 0, 0, 0}, injector.currentTimeMillis());
    }

    private EventIndex(com.android.server.people.data.EventIndex.Injector injector, long[] eventBitmaps, long lastUpdatedTime) {
        this.mLock = new java.lang.Object();
        this.mInjector = injector;
        this.mEventBitmaps = java.util.Arrays.copyOf(eventBitmaps, 4);
        this.mLastUpdatedTime = lastUpdatedTime;
    }

    public android.util.Range<java.lang.Long> getMostRecentActiveTimeSlot() {
        synchronized (this.mLock) {
            for (int slotType = 3; slotType >= 0; slotType--) {
                if (this.mEventBitmaps[slotType] != 0) {
                    android.util.Range<java.lang.Long> lastTimeSlot = TIME_SLOT_FACTORIES.get(slotType).apply(java.lang.Long.valueOf(this.mLastUpdatedTime));
                    int numberOfTrailingZeros = java.lang.Long.numberOfTrailingZeros(this.mEventBitmaps[slotType]);
                    long offset = getDuration(lastTimeSlot) * ((long) numberOfTrailingZeros);
                    return android.util.Range.create(java.lang.Long.valueOf(((java.lang.Long) lastTimeSlot.getLower()).longValue() - offset), java.lang.Long.valueOf(((java.lang.Long) lastTimeSlot.getUpper()).longValue() - offset));
                }
            }
            return null;
        }
    }

    public java.util.List<android.util.Range<java.lang.Long>> getActiveTimeSlots() {
        java.util.List<android.util.Range<java.lang.Long>> activeTimeSlots = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (int slotType = 0; slotType < 4; slotType++) {
                activeTimeSlots = combineTimeSlotLists(activeTimeSlots, getActiveTimeSlotsForType(slotType));
            }
        }
        java.util.Collections.reverse(activeTimeSlots);
        return activeTimeSlots;
    }

    public boolean isEmpty() {
        synchronized (this.mLock) {
            for (int slotType = 0; slotType < 4; slotType++) {
                if (this.mEventBitmaps[slotType] != 0) {
                    return false;
                }
            }
            return true;
        }
    }

    void addEvent(long eventTime) {
        if (EMPTY == this) {
            throw new java.lang.IllegalStateException("EMPTY instance is immutable");
        }
        synchronized (this.mLock) {
            long currentTime = this.mInjector.currentTimeMillis();
            updateEventBitmaps(currentTime);
            for (int slotType = 0; slotType < 4; slotType++) {
                int offset = diffTimeSlots(slotType, eventTime, currentTime);
                if (offset < 64) {
                    long[] jArr = this.mEventBitmaps;
                    jArr[slotType] = jArr[slotType] | (1 << offset);
                }
            }
        }
    }

    void update() {
        updateEventBitmaps(this.mInjector.currentTimeMillis());
    }

    public java.lang.String toString() {
        return "EventIndex {perDayEventBitmap=0b" + java.lang.Long.toBinaryString(this.mEventBitmaps[0]) + ", perFourHoursEventBitmap=0b" + java.lang.Long.toBinaryString(this.mEventBitmaps[1]) + ", perHourEventBitmap=0b" + java.lang.Long.toBinaryString(this.mEventBitmaps[2]) + ", perTwoMinutesEventBitmap=0b" + java.lang.Long.toBinaryString(this.mEventBitmaps[3]) + ", lastUpdatedTime=" + android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", this.mLastUpdatedTime) + "}";
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.people.data.EventIndex)) {
            return false;
        }
        com.android.server.people.data.EventIndex other = (com.android.server.people.data.EventIndex) obj;
        return this.mLastUpdatedTime == other.mLastUpdatedTime && java.util.Arrays.equals(this.mEventBitmaps, other.mEventBitmaps);
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Long.valueOf(this.mLastUpdatedTime), java.lang.Integer.valueOf(java.util.Arrays.hashCode(this.mEventBitmaps)));
    }

    synchronized void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream) {
        for (long bitmap : this.mEventBitmaps) {
            protoOutputStream.write(2211908157441L, bitmap);
        }
        protoOutputStream.write(1112396529666L, this.mLastUpdatedTime);
    }

    private void updateEventBitmaps(long currentTimeMillis) {
        for (int slotType = 0; slotType < 4; slotType++) {
            int offset = diffTimeSlots(slotType, this.mLastUpdatedTime, currentTimeMillis);
            if (offset < 64) {
                long[] jArr = this.mEventBitmaps;
                jArr[slotType] = jArr[slotType] << offset;
            } else {
                this.mEventBitmaps[slotType] = 0;
            }
        }
        long[] jArr2 = this.mEventBitmaps;
        jArr2[0] = jArr2[0] << 1;
        long[] jArr3 = this.mEventBitmaps;
        jArr3[0] = jArr3[0] >>> 1;
        this.mLastUpdatedTime = currentTimeMillis;
    }

    static com.android.server.people.data.EventIndex readFromProto(android.util.proto.ProtoInputStream protoInputStream) throws java.io.IOException {
        int bitmapIndex = 0;
        long[] eventBitmaps = new long[4];
        long lastUpdated = 0;
        while (protoInputStream.nextField() != -1) {
            switch (protoInputStream.getFieldNumber()) {
                case 1:
                    eventBitmaps[bitmapIndex] = protoInputStream.readLong(2211908157441L);
                    bitmapIndex++;
                    break;
                case 2:
                    lastUpdated = protoInputStream.readLong(1112396529666L);
                    break;
                default:
                    android.util.Slog.e(TAG, "Could not read undefined field: " + protoInputStream.getFieldNumber());
                    break;
            }
        }
        return new com.android.server.people.data.EventIndex(new com.android.server.people.data.EventIndex.Injector(), eventBitmaps, lastUpdated);
    }

    private static java.time.LocalDateTime toLocalDateTime(long epochMilli) {
        return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(epochMilli), java.util.TimeZone.getDefault().toZoneId());
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.time.ZonedDateTime] */
    private static long toEpochMilli(java.time.LocalDateTime localDateTime) {
        return localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static long getDuration(android.util.Range<java.lang.Long> timeSlot) {
        return ((java.lang.Long) timeSlot.getUpper()).longValue() - ((java.lang.Long) timeSlot.getLower()).longValue();
    }

    private static int diffTimeSlots(int timeSlotType, long fromTime, long toTime) {
        java.util.function.Function<java.lang.Long, android.util.Range<java.lang.Long>> timeSlotFactory = TIME_SLOT_FACTORIES.get(timeSlotType);
        android.util.Range<java.lang.Long> fromSlot = timeSlotFactory.apply(java.lang.Long.valueOf(fromTime));
        android.util.Range<java.lang.Long> toSlot = timeSlotFactory.apply(java.lang.Long.valueOf(toTime));
        return (int) ((((java.lang.Long) toSlot.getLower()).longValue() - ((java.lang.Long) fromSlot.getLower()).longValue()) / getDuration(fromSlot));
    }

    private java.util.List<android.util.Range<java.lang.Long>> getActiveTimeSlotsForType(int timeSlotType) {
        long eventBitmap = this.mEventBitmaps[timeSlotType];
        android.util.Range<java.lang.Long> latestTimeSlot = TIME_SLOT_FACTORIES.get(timeSlotType).apply(java.lang.Long.valueOf(this.mLastUpdatedTime));
        long startTime = ((java.lang.Long) latestTimeSlot.getLower()).longValue();
        long duration = getDuration(latestTimeSlot);
        java.util.List<android.util.Range<java.lang.Long>> timeSlots = new java.util.ArrayList<>();
        while (eventBitmap != 0) {
            int trailingZeros = java.lang.Long.numberOfTrailingZeros(eventBitmap);
            if (trailingZeros > 0) {
                startTime -= ((long) trailingZeros) * duration;
                eventBitmap >>>= trailingZeros;
            }
            if (eventBitmap != 0) {
                timeSlots.add(android.util.Range.create(java.lang.Long.valueOf(startTime), java.lang.Long.valueOf(startTime + duration)));
                startTime -= duration;
                eventBitmap >>>= 1;
            }
        }
        return timeSlots;
    }

    private static java.util.List<android.util.Range<java.lang.Long>> combineTimeSlotLists(java.util.List<android.util.Range<java.lang.Long>> longerSlots, java.util.List<android.util.Range<java.lang.Long>> shorterSlots) {
        java.util.List<android.util.Range<java.lang.Long>> result = new java.util.ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < longerSlots.size() && j < shorterSlots.size()) {
            android.util.Range<java.lang.Long> longerSlot = longerSlots.get(i);
            android.util.Range<java.lang.Long> shorterSlot = shorterSlots.get(j);
            if (longerSlot.contains(shorterSlot)) {
                result.add(shorterSlot);
                i++;
                j++;
            } else if (((java.lang.Long) longerSlot.getLower()).longValue() < ((java.lang.Long) shorterSlot.getLower()).longValue()) {
                result.add(shorterSlot);
                j++;
            } else {
                result.add(longerSlot);
                i++;
            }
        }
        if (i < longerSlots.size()) {
            result.addAll(longerSlots.subList(i, longerSlots.size()));
        } else if (j < shorterSlots.size()) {
            result.addAll(shorterSlots.subList(j, shorterSlots.size()));
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.util.Range<java.lang.Long> createOneDayLongTimeSlot(long time) {
        java.time.LocalDateTime beginTime = toLocalDateTime(time).truncatedTo(java.time.temporal.ChronoUnit.DAYS);
        return android.util.Range.create(java.lang.Long.valueOf(toEpochMilli(beginTime)), java.lang.Long.valueOf(toEpochMilli(beginTime.plusDays(1L))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.util.Range<java.lang.Long> createFourHoursLongTimeSlot(long time) {
        int hourOfDay = toLocalDateTime(time).getHour();
        java.time.LocalDateTime beginTime = toLocalDateTime(time).truncatedTo(java.time.temporal.ChronoUnit.HOURS).minusHours(hourOfDay % 4);
        return android.util.Range.create(java.lang.Long.valueOf(toEpochMilli(beginTime)), java.lang.Long.valueOf(toEpochMilli(beginTime.plusHours(4L))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.util.Range<java.lang.Long> createOneHourLongTimeSlot(long time) {
        java.time.LocalDateTime beginTime = toLocalDateTime(time).truncatedTo(java.time.temporal.ChronoUnit.HOURS);
        return android.util.Range.create(java.lang.Long.valueOf(toEpochMilli(beginTime)), java.lang.Long.valueOf(toEpochMilli(beginTime.plusHours(1L))));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.util.Range<java.lang.Long> createTwoMinutesLongTimeSlot(long time) {
        int minuteOfHour = toLocalDateTime(time).getMinute();
        java.time.LocalDateTime beginTime = toLocalDateTime(time).truncatedTo(java.time.temporal.ChronoUnit.MINUTES).minusMinutes(minuteOfHour % 2);
        return android.util.Range.create(java.lang.Long.valueOf(toEpochMilli(beginTime)), java.lang.Long.valueOf(toEpochMilli(beginTime.plusMinutes(2L))));
    }

    static class Injector {
        Injector() {
        }

        long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }
    }
}
