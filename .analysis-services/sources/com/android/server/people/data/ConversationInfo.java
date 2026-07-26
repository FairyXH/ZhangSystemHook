package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class ConversationInfo {
    private static final boolean DEBUG = false;
    private static final int FLAG_BUBBLED = 4;
    private static final int FLAG_CONTACT_STARRED = 32;
    private static final int FLAG_DEMOTED = 64;
    private static final int FLAG_IMPORTANT = 1;
    private static final int FLAG_NOTIFICATION_SILENCED = 2;
    private static final int FLAG_PERSON_BOT = 16;
    private static final int FLAG_PERSON_IMPORTANT = 8;
    private static final java.lang.String TAG = com.android.server.people.data.ConversationInfo.class.getSimpleName();
    private static final int VERSION = 1;
    private java.lang.String mContactPhoneNumber;
    private android.net.Uri mContactUri;
    private int mConversationFlags;
    private long mCreationTimestamp;
    private java.util.Map<java.lang.String, android.app.people.ConversationStatus> mCurrStatuses;
    private long mLastEventTimestamp;
    private android.content.LocusId mLocusId;
    private java.lang.String mNotificationChannelId;
    private java.lang.String mParentNotificationChannelId;
    private int mShortcutFlags;
    private java.lang.String mShortcutId;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface ConversationFlags {
    }

    private ConversationInfo(com.android.server.people.data.ConversationInfo.Builder builder) {
        this.mShortcutId = builder.mShortcutId;
        this.mLocusId = builder.mLocusId;
        this.mContactUri = builder.mContactUri;
        this.mContactPhoneNumber = builder.mContactPhoneNumber;
        this.mNotificationChannelId = builder.mNotificationChannelId;
        this.mParentNotificationChannelId = builder.mParentNotificationChannelId;
        this.mLastEventTimestamp = builder.mLastEventTimestamp;
        this.mCreationTimestamp = builder.mCreationTimestamp;
        this.mShortcutFlags = builder.mShortcutFlags;
        this.mConversationFlags = builder.mConversationFlags;
        this.mCurrStatuses = builder.mCurrStatuses;
    }

    public java.lang.String getShortcutId() {
        return this.mShortcutId;
    }

    android.content.LocusId getLocusId() {
        return this.mLocusId;
    }

    android.net.Uri getContactUri() {
        return this.mContactUri;
    }

    java.lang.String getContactPhoneNumber() {
        return this.mContactPhoneNumber;
    }

    java.lang.String getNotificationChannelId() {
        return this.mNotificationChannelId;
    }

    java.lang.String getParentNotificationChannelId() {
        return this.mParentNotificationChannelId;
    }

    long getLastEventTimestamp() {
        return this.mLastEventTimestamp;
    }

    long getCreationTimestamp() {
        return this.mCreationTimestamp;
    }

    public boolean isShortcutLongLived() {
        return hasShortcutFlags(8192);
    }

    public boolean isShortcutCachedForNotification() {
        return hasShortcutFlags(16384);
    }

    public boolean isImportant() {
        return hasConversationFlags(1);
    }

    public boolean isNotificationSilenced() {
        return hasConversationFlags(2);
    }

    public boolean isBubbled() {
        return hasConversationFlags(4);
    }

    public boolean isDemoted() {
        return hasConversationFlags(64);
    }

    public boolean isPersonImportant() {
        return hasConversationFlags(8);
    }

    public boolean isPersonBot() {
        return hasConversationFlags(16);
    }

    public boolean isContactStarred() {
        return hasConversationFlags(32);
    }

    public java.util.Collection<android.app.people.ConversationStatus> getStatuses() {
        return this.mCurrStatuses.values();
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.people.data.ConversationInfo)) {
            return false;
        }
        com.android.server.people.data.ConversationInfo other = (com.android.server.people.data.ConversationInfo) obj;
        return java.util.Objects.equals(this.mShortcutId, other.mShortcutId) && java.util.Objects.equals(this.mLocusId, other.mLocusId) && java.util.Objects.equals(this.mContactUri, other.mContactUri) && java.util.Objects.equals(this.mContactPhoneNumber, other.mContactPhoneNumber) && java.util.Objects.equals(this.mNotificationChannelId, other.mNotificationChannelId) && java.util.Objects.equals(this.mParentNotificationChannelId, other.mParentNotificationChannelId) && java.util.Objects.equals(java.lang.Long.valueOf(this.mLastEventTimestamp), java.lang.Long.valueOf(other.mLastEventTimestamp)) && this.mCreationTimestamp == other.mCreationTimestamp && this.mShortcutFlags == other.mShortcutFlags && this.mConversationFlags == other.mConversationFlags && java.util.Objects.equals(this.mCurrStatuses, other.mCurrStatuses);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mShortcutId, this.mLocusId, this.mContactUri, this.mContactPhoneNumber, this.mNotificationChannelId, this.mParentNotificationChannelId, java.lang.Long.valueOf(this.mLastEventTimestamp), java.lang.Long.valueOf(this.mCreationTimestamp), java.lang.Integer.valueOf(this.mShortcutFlags), java.lang.Integer.valueOf(this.mConversationFlags), this.mCurrStatuses);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("ConversationInfo {");
        sb.append("shortcutId=").append(this.mShortcutId);
        sb.append(", locusId=").append(this.mLocusId);
        sb.append(", contactUri=").append(this.mContactUri);
        sb.append(", phoneNumber=").append(this.mContactPhoneNumber);
        sb.append(", notificationChannelId=").append(this.mNotificationChannelId);
        sb.append(", parentNotificationChannelId=").append(this.mParentNotificationChannelId);
        sb.append(", lastEventTimestamp=").append(this.mLastEventTimestamp);
        sb.append(", creationTimestamp=").append(this.mCreationTimestamp);
        sb.append(", statuses=").append(this.mCurrStatuses);
        sb.append(", shortcutFlags=0x").append(java.lang.Integer.toHexString(this.mShortcutFlags));
        sb.append(" [");
        if (isShortcutLongLived()) {
            sb.append("Liv");
        }
        if (isShortcutCachedForNotification()) {
            sb.append("Cac");
        }
        sb.append("]");
        sb.append(", conversationFlags=0x").append(java.lang.Integer.toHexString(this.mConversationFlags));
        sb.append(" [");
        if (isImportant()) {
            sb.append("Imp");
        }
        if (isNotificationSilenced()) {
            sb.append("Sil");
        }
        if (isBubbled()) {
            sb.append("Bub");
        }
        if (isDemoted()) {
            sb.append("Dem");
        }
        if (isPersonImportant()) {
            sb.append("PIm");
        }
        if (isPersonBot()) {
            sb.append("Bot");
        }
        if (isContactStarred()) {
            sb.append("Sta");
        }
        sb.append("]}");
        return sb.toString();
    }

    private boolean hasShortcutFlags(int flags) {
        return (this.mShortcutFlags & flags) == flags;
    }

    private boolean hasConversationFlags(int flags) {
        return (this.mConversationFlags & flags) == flags;
    }

    void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream) {
        protoOutputStream.write(1138166333441L, this.mShortcutId);
        if (this.mLocusId != null) {
            long locusIdToken = protoOutputStream.start(1146756268034L);
            protoOutputStream.write(1138166333441L, this.mLocusId.getId());
            protoOutputStream.end(locusIdToken);
        }
        if (this.mContactUri != null) {
            protoOutputStream.write(1138166333443L, this.mContactUri.toString());
        }
        if (this.mNotificationChannelId != null) {
            protoOutputStream.write(1138166333444L, this.mNotificationChannelId);
        }
        if (this.mParentNotificationChannelId != null) {
            protoOutputStream.write(1138166333448L, this.mParentNotificationChannelId);
        }
        protoOutputStream.write(1112396529673L, this.mLastEventTimestamp);
        protoOutputStream.write(1112396529674L, this.mCreationTimestamp);
        protoOutputStream.write(1120986464261L, this.mShortcutFlags);
        protoOutputStream.write(1120986464262L, this.mConversationFlags);
        if (this.mContactPhoneNumber != null) {
            protoOutputStream.write(1138166333447L, this.mContactPhoneNumber);
        }
    }

    byte[] getBackupPayload() {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream out = new java.io.DataOutputStream(baos);
        try {
            out.writeUTF(this.mShortcutId);
            out.writeUTF(this.mLocusId != null ? this.mLocusId.getId() : "");
            out.writeUTF(this.mContactUri != null ? this.mContactUri.toString() : "");
            out.writeUTF(this.mNotificationChannelId != null ? this.mNotificationChannelId : "");
            out.writeInt(this.mShortcutFlags);
            out.writeInt(this.mConversationFlags);
            out.writeUTF(this.mContactPhoneNumber != null ? this.mContactPhoneNumber : "");
            out.writeUTF(this.mParentNotificationChannelId != null ? this.mParentNotificationChannelId : "");
            out.writeLong(this.mLastEventTimestamp);
            out.writeInt(1);
            out.writeLong(this.mCreationTimestamp);
            return baos.toByteArray();
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to write fields to backup payload.", e);
            return null;
        }
    }

    static com.android.server.people.data.ConversationInfo readFromProto(android.util.proto.ProtoInputStream protoInputStream) throws java.io.IOException {
        com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder();
        while (protoInputStream.nextField() != -1) {
            switch (protoInputStream.getFieldNumber()) {
                case 1:
                    builder.setShortcutId(protoInputStream.readString(1138166333441L));
                    break;
                case 2:
                    long locusIdToken = protoInputStream.start(1146756268034L);
                    while (protoInputStream.nextField() != -1) {
                        if (protoInputStream.getFieldNumber() == 1) {
                            builder.setLocusId(new android.content.LocusId(protoInputStream.readString(1138166333441L)));
                        }
                    }
                    protoInputStream.end(locusIdToken);
                    break;
                case 3:
                    builder.setContactUri(android.net.Uri.parse(protoInputStream.readString(1138166333443L)));
                    break;
                case 4:
                    builder.setNotificationChannelId(protoInputStream.readString(1138166333444L));
                    break;
                case 5:
                    builder.setShortcutFlags(protoInputStream.readInt(1120986464261L));
                    break;
                case 6:
                    builder.setConversationFlags(protoInputStream.readInt(1120986464262L));
                    break;
                case 7:
                    builder.setContactPhoneNumber(protoInputStream.readString(1138166333447L));
                    break;
                case 8:
                    builder.setParentNotificationChannelId(protoInputStream.readString(1138166333448L));
                    break;
                case 9:
                    builder.setLastEventTimestamp(protoInputStream.readLong(1112396529673L));
                    break;
                case 10:
                    builder.setCreationTimestamp(protoInputStream.readLong(1112396529674L));
                    break;
                default:
                    android.util.Slog.w(TAG, "Could not read undefined field: " + protoInputStream.getFieldNumber());
                    break;
            }
        }
        return builder.build();
    }

    static com.android.server.people.data.ConversationInfo readFromBackupPayload(byte[] payload) {
        com.android.server.people.data.ConversationInfo.Builder builder = new com.android.server.people.data.ConversationInfo.Builder();
        java.io.DataInputStream in = new java.io.DataInputStream(new java.io.ByteArrayInputStream(payload));
        try {
            builder.setShortcutId(in.readUTF());
            java.lang.String locusId = in.readUTF();
            if (!android.text.TextUtils.isEmpty(locusId)) {
                builder.setLocusId(new android.content.LocusId(locusId));
            }
            java.lang.String contactUri = in.readUTF();
            if (!android.text.TextUtils.isEmpty(contactUri)) {
                builder.setContactUri(android.net.Uri.parse(contactUri));
            }
            java.lang.String notificationChannelId = in.readUTF();
            if (!android.text.TextUtils.isEmpty(notificationChannelId)) {
                builder.setNotificationChannelId(notificationChannelId);
            }
            builder.setShortcutFlags(in.readInt());
            builder.setConversationFlags(in.readInt());
            java.lang.String contactPhoneNumber = in.readUTF();
            if (!android.text.TextUtils.isEmpty(contactPhoneNumber)) {
                builder.setContactPhoneNumber(contactPhoneNumber);
            }
            java.lang.String parentNotificationChannelId = in.readUTF();
            if (!android.text.TextUtils.isEmpty(parentNotificationChannelId)) {
                builder.setParentNotificationChannelId(parentNotificationChannelId);
            }
            builder.setLastEventTimestamp(in.readLong());
            int payloadVersion = maybeReadVersion(in);
            if (payloadVersion == 1) {
                builder.setCreationTimestamp(in.readLong());
            }
            return builder.build();
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to read conversation info fields from backup payload.", e);
            return null;
        }
    }

    private static int maybeReadVersion(java.io.DataInputStream in) throws java.io.IOException {
        try {
            return in.readInt();
        } catch (java.io.EOFException e) {
            return 0;
        }
    }

    static class Builder {
        private java.lang.String mContactPhoneNumber;
        private android.net.Uri mContactUri;
        private int mConversationFlags;
        private long mCreationTimestamp;
        private java.util.Map<java.lang.String, android.app.people.ConversationStatus> mCurrStatuses;
        private long mLastEventTimestamp;
        private android.content.LocusId mLocusId;
        private java.lang.String mNotificationChannelId;
        private java.lang.String mParentNotificationChannelId;
        private int mShortcutFlags;
        private java.lang.String mShortcutId;

        Builder() {
            this.mCurrStatuses = new java.util.HashMap();
        }

        Builder(com.android.server.people.data.ConversationInfo conversationInfo) {
            this.mCurrStatuses = new java.util.HashMap();
            if (this.mShortcutId == null) {
                this.mShortcutId = conversationInfo.mShortcutId;
            } else {
                com.android.internal.util.Preconditions.checkArgument(this.mShortcutId.equals(conversationInfo.mShortcutId));
            }
            this.mLocusId = conversationInfo.mLocusId;
            this.mContactUri = conversationInfo.mContactUri;
            this.mContactPhoneNumber = conversationInfo.mContactPhoneNumber;
            this.mNotificationChannelId = conversationInfo.mNotificationChannelId;
            this.mParentNotificationChannelId = conversationInfo.mParentNotificationChannelId;
            this.mLastEventTimestamp = conversationInfo.mLastEventTimestamp;
            this.mCreationTimestamp = conversationInfo.mCreationTimestamp;
            this.mShortcutFlags = conversationInfo.mShortcutFlags;
            this.mConversationFlags = conversationInfo.mConversationFlags;
            this.mCurrStatuses = conversationInfo.mCurrStatuses;
        }

        com.android.server.people.data.ConversationInfo.Builder setShortcutId(java.lang.String shortcutId) {
            this.mShortcutId = shortcutId;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setLocusId(android.content.LocusId locusId) {
            this.mLocusId = locusId;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setContactUri(android.net.Uri contactUri) {
            this.mContactUri = contactUri;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setContactPhoneNumber(java.lang.String phoneNumber) {
            this.mContactPhoneNumber = phoneNumber;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setNotificationChannelId(java.lang.String notificationChannelId) {
            this.mNotificationChannelId = notificationChannelId;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setParentNotificationChannelId(java.lang.String parentNotificationChannelId) {
            this.mParentNotificationChannelId = parentNotificationChannelId;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setLastEventTimestamp(long lastEventTimestamp) {
            this.mLastEventTimestamp = lastEventTimestamp;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setCreationTimestamp(long creationTimestamp) {
            this.mCreationTimestamp = creationTimestamp;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setShortcutFlags(int shortcutFlags) {
            this.mShortcutFlags = shortcutFlags;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setConversationFlags(int conversationFlags) {
            this.mConversationFlags = conversationFlags;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setImportant(boolean value) {
            return setConversationFlag(1, value);
        }

        com.android.server.people.data.ConversationInfo.Builder setNotificationSilenced(boolean value) {
            return setConversationFlag(2, value);
        }

        com.android.server.people.data.ConversationInfo.Builder setBubbled(boolean value) {
            return setConversationFlag(4, value);
        }

        com.android.server.people.data.ConversationInfo.Builder setDemoted(boolean value) {
            return setConversationFlag(64, value);
        }

        com.android.server.people.data.ConversationInfo.Builder setPersonImportant(boolean value) {
            return setConversationFlag(8, value);
        }

        com.android.server.people.data.ConversationInfo.Builder setPersonBot(boolean value) {
            return setConversationFlag(16, value);
        }

        com.android.server.people.data.ConversationInfo.Builder setContactStarred(boolean value) {
            return setConversationFlag(32, value);
        }

        private com.android.server.people.data.ConversationInfo.Builder setConversationFlag(int flags, boolean value) {
            if (value) {
                return addConversationFlags(flags);
            }
            return removeConversationFlags(flags);
        }

        private com.android.server.people.data.ConversationInfo.Builder addConversationFlags(int flags) {
            this.mConversationFlags |= flags;
            return this;
        }

        private com.android.server.people.data.ConversationInfo.Builder removeConversationFlags(int flags) {
            this.mConversationFlags &= ~flags;
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder setStatuses(java.util.List<android.app.people.ConversationStatus> statuses) {
            this.mCurrStatuses.clear();
            if (statuses != null) {
                for (android.app.people.ConversationStatus status : statuses) {
                    this.mCurrStatuses.put(status.getId(), status);
                }
            }
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder addOrUpdateStatus(android.app.people.ConversationStatus status) {
            this.mCurrStatuses.put(status.getId(), status);
            return this;
        }

        com.android.server.people.data.ConversationInfo.Builder clearStatus(java.lang.String statusId) {
            this.mCurrStatuses.remove(statusId);
            return this;
        }

        com.android.server.people.data.ConversationInfo build() {
            java.util.Objects.requireNonNull(this.mShortcutId);
            return new com.android.server.people.data.ConversationInfo(this);
        }
    }
}
