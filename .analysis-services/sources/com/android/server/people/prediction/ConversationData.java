package com.android.server.people.prediction;

/* JADX INFO: loaded from: classes2.dex */
class ConversationData {
    private final com.android.server.people.data.ConversationInfo mConversationInfo;
    private final com.android.server.people.data.EventHistory mEventHistory;
    private final java.lang.String mPackageName;
    private final int mUserId;

    ConversationData(java.lang.String packageName, int userId, com.android.server.people.data.ConversationInfo conversationInfo, com.android.server.people.data.EventHistory eventHistory) {
        this.mPackageName = packageName;
        this.mUserId = userId;
        this.mConversationInfo = conversationInfo;
        this.mEventHistory = eventHistory;
    }

    java.lang.String getPackageName() {
        return this.mPackageName;
    }

    int getUserId() {
        return this.mUserId;
    }

    com.android.server.people.data.ConversationInfo getConversationInfo() {
        return this.mConversationInfo;
    }

    com.android.server.people.data.EventHistory getEventHistory() {
        return this.mEventHistory;
    }
}
