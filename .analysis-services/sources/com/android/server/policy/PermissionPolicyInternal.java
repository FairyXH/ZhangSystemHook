package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public abstract class PermissionPolicyInternal {

    public interface OnInitializedCallback {
        void onInitialized(int i);
    }

    public abstract boolean checkStartActivity(android.content.Intent intent, int i, java.lang.String str);

    public abstract boolean isInitialized(int i);

    public abstract boolean isIntentToPermissionDialog(android.content.Intent intent);

    public abstract void setOnInitializedCallback(com.android.server.policy.PermissionPolicyInternal.OnInitializedCallback onInitializedCallback);

    public abstract boolean shouldShowNotificationDialogForTask(android.app.TaskInfo taskInfo, java.lang.String str, java.lang.String str2, android.content.Intent intent, java.lang.String str3);

    public abstract void showNotificationPromptIfNeeded(java.lang.String str, int i, int i2);
}
