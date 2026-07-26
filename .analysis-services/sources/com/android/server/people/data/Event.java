package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class Event {
    public static final int TYPE_CALL_INCOMING = 11;
    public static final int TYPE_CALL_MISSED = 12;
    public static final int TYPE_CALL_OUTGOING = 10;
    public static final int TYPE_IN_APP_CONVERSATION = 13;
    public static final int TYPE_NOTIFICATION_OPENED = 3;
    public static final int TYPE_NOTIFICATION_POSTED = 2;
    public static final int TYPE_SHARE_IMAGE = 5;
    public static final int TYPE_SHARE_OTHER = 7;
    public static final int TYPE_SHARE_TEXT = 4;
    public static final int TYPE_SHARE_VIDEO = 6;
    public static final int TYPE_SHORTCUT_INVOCATION = 1;
    public static final int TYPE_SMS_INCOMING = 9;
    public static final int TYPE_SMS_OUTGOING = 8;
    private final int mDurationSeconds;
    private final long mTimestamp;
    private final int mType;
    private static final java.lang.String TAG = com.android.server.people.data.Event.class.getSimpleName();
    public static final java.util.Set<java.lang.Integer> NOTIFICATION_EVENT_TYPES = new android.util.ArraySet();
    public static final java.util.Set<java.lang.Integer> SHARE_EVENT_TYPES = new android.util.ArraySet();
    public static final java.util.Set<java.lang.Integer> SMS_EVENT_TYPES = new android.util.ArraySet();
    public static final java.util.Set<java.lang.Integer> CALL_EVENT_TYPES = new android.util.ArraySet();
    public static final java.util.Set<java.lang.Integer> ALL_EVENT_TYPES = new android.util.ArraySet();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface EventType {
    }

    static {
        NOTIFICATION_EVENT_TYPES.add(2);
        NOTIFICATION_EVENT_TYPES.add(3);
        SHARE_EVENT_TYPES.add(4);
        SHARE_EVENT_TYPES.add(5);
        SHARE_EVENT_TYPES.add(6);
        SHARE_EVENT_TYPES.add(7);
        SMS_EVENT_TYPES.add(9);
        SMS_EVENT_TYPES.add(8);
        CALL_EVENT_TYPES.add(11);
        CALL_EVENT_TYPES.add(10);
        CALL_EVENT_TYPES.add(12);
        ALL_EVENT_TYPES.add(1);
        ALL_EVENT_TYPES.add(13);
        ALL_EVENT_TYPES.addAll(NOTIFICATION_EVENT_TYPES);
        ALL_EVENT_TYPES.addAll(SHARE_EVENT_TYPES);
        ALL_EVENT_TYPES.addAll(SMS_EVENT_TYPES);
        ALL_EVENT_TYPES.addAll(CALL_EVENT_TYPES);
    }

    Event(long timestamp, int type) {
        this.mTimestamp = timestamp;
        this.mType = type;
        this.mDurationSeconds = 0;
    }

    private Event(com.android.server.people.data.Event.Builder builder) {
        this.mTimestamp = builder.mTimestamp;
        this.mType = builder.mType;
        this.mDurationSeconds = builder.mDurationSeconds;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public int getType() {
        return this.mType;
    }

    public int getDurationSeconds() {
        return this.mDurationSeconds;
    }

    void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream) {
        protoOutputStream.write(1120986464257L, this.mType);
        protoOutputStream.write(1112396529666L, this.mTimestamp);
        protoOutputStream.write(1120986464259L, this.mDurationSeconds);
    }

    static com.android.server.people.data.Event readFromProto(android.util.proto.ProtoInputStream protoInputStream) throws java.io.IOException {
        com.android.server.people.data.Event.Builder builder = new com.android.server.people.data.Event.Builder();
        while (protoInputStream.nextField() != -1) {
            switch (protoInputStream.getFieldNumber()) {
                case 1:
                    builder.setType(protoInputStream.readInt(1120986464257L));
                    break;
                case 2:
                    builder.setTimestamp(protoInputStream.readLong(1112396529666L));
                    break;
                case 3:
                    builder.setDurationSeconds(protoInputStream.readInt(1120986464259L));
                    break;
                default:
                    android.util.Slog.w(TAG, "Could not read undefined field: " + protoInputStream.getFieldNumber());
                    break;
            }
        }
        return builder.build();
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.people.data.Event)) {
            return false;
        }
        com.android.server.people.data.Event other = (com.android.server.people.data.Event) obj;
        return this.mTimestamp == other.mTimestamp && this.mType == other.mType && this.mDurationSeconds == other.mDurationSeconds;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Long.valueOf(this.mTimestamp), java.lang.Integer.valueOf(this.mType), java.lang.Integer.valueOf(this.mDurationSeconds));
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Event {");
        sb.append("timestamp=").append(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", this.mTimestamp));
        sb.append(", type=").append(this.mType);
        if (this.mDurationSeconds > 0) {
            sb.append(", durationSeconds=").append(this.mDurationSeconds);
        }
        sb.append("}");
        return sb.toString();
    }

    static class Builder {
        private int mDurationSeconds;
        private long mTimestamp;
        private int mType;

        private Builder() {
        }

        Builder(long timestamp, int type) {
            this.mTimestamp = timestamp;
            this.mType = type;
        }

        com.android.server.people.data.Event.Builder setDurationSeconds(int durationSeconds) {
            this.mDurationSeconds = durationSeconds;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.people.data.Event.Builder setTimestamp(long timestamp) {
            this.mTimestamp = timestamp;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.people.data.Event.Builder setType(int type) {
            this.mType = type;
            return this;
        }

        com.android.server.people.data.Event build() {
            return new com.android.server.people.data.Event(this);
        }
    }
}
