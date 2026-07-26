package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class UserData {
    private static final int CONVERSATIONS_END_TOKEN = -1;
    private static final java.lang.String TAG = com.android.server.people.data.UserData.class.getSimpleName();
    private java.lang.String mDefaultDialer;
    private java.lang.String mDefaultSmsApp;
    private boolean mIsUnlocked;
    private java.util.Map<java.lang.String, com.android.server.people.data.PackageData> mPackageDataMap = new android.util.ArrayMap();
    private final java.io.File mPerUserPeopleDataDir;
    private final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService;
    private final int mUserId;

    UserData(int userId, java.util.concurrent.ScheduledExecutorService scheduledExecutorService) {
        this.mUserId = userId;
        this.mPerUserPeopleDataDir = new java.io.File(android.os.Environment.getDataSystemCeDirectory(this.mUserId), "people");
        this.mScheduledExecutorService = scheduledExecutorService;
    }

    int getUserId() {
        return this.mUserId;
    }

    void forAllPackages(java.util.function.Consumer<com.android.server.people.data.PackageData> consumer) {
        for (com.android.server.people.data.PackageData packageData : this.mPackageDataMap.values()) {
            consumer.accept(packageData);
        }
    }

    void setUserUnlocked() {
        this.mIsUnlocked = true;
    }

    void setUserStopped() {
        this.mIsUnlocked = false;
    }

    boolean isUnlocked() {
        return this.mIsUnlocked;
    }

    void loadUserData() {
        this.mPerUserPeopleDataDir.mkdir();
        java.util.Map<java.lang.String, com.android.server.people.data.PackageData> packageDataMap = com.android.server.people.data.PackageData.packagesDataFromDisk(this.mUserId, new com.android.server.people.data.UserData$$ExternalSyntheticLambda0(this), new com.android.server.people.data.UserData$$ExternalSyntheticLambda1(this), this.mScheduledExecutorService, this.mPerUserPeopleDataDir);
        this.mPackageDataMap.putAll(packageDataMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ com.android.server.people.data.PackageData lambda$getOrCreatePackageData$0(java.lang.String packageName, java.lang.String key) {
        return createPackageData(packageName);
    }

    com.android.server.people.data.PackageData getOrCreatePackageData(final java.lang.String packageName) {
        return this.mPackageDataMap.computeIfAbsent(packageName, new java.util.function.Function() { // from class: com.android.server.people.data.UserData$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$getOrCreatePackageData$0(packageName, (java.lang.String) obj);
            }
        });
    }

    com.android.server.people.data.PackageData getPackageData(java.lang.String packageName) {
        return this.mPackageDataMap.get(packageName);
    }

    void deletePackageData(java.lang.String packageName) {
        com.android.server.people.data.PackageData packageData = this.mPackageDataMap.remove(packageName);
        if (packageData != null) {
            packageData.onDestroy();
        }
    }

    void setDefaultDialer(java.lang.String packageName) {
        this.mDefaultDialer = packageName;
    }

    com.android.server.people.data.PackageData getDefaultDialer() {
        if (this.mDefaultDialer != null) {
            return getPackageData(this.mDefaultDialer);
        }
        return null;
    }

    void setDefaultSmsApp(java.lang.String packageName) {
        this.mDefaultSmsApp = packageName;
    }

    com.android.server.people.data.PackageData getDefaultSmsApp() {
        if (this.mDefaultSmsApp != null) {
            return getPackageData(this.mDefaultSmsApp);
        }
        return null;
    }

    byte[] getBackupPayload() {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.DataOutputStream out = new java.io.DataOutputStream(baos);
        for (com.android.server.people.data.PackageData packageData : this.mPackageDataMap.values()) {
            try {
                byte[] conversationsBackupPayload = packageData.getConversationStore().getBackupPayload();
                out.writeInt(conversationsBackupPayload.length);
                out.write(conversationsBackupPayload);
                out.writeUTF(packageData.getPackageName());
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to write conversations to backup payload.", e);
                return null;
            }
        }
        try {
            out.writeInt(-1);
            return baos.toByteArray();
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Failed to write conversations end token to backup payload.", e2);
            return null;
        }
    }

    void restore(byte[] payload) throws java.io.IOException {
        java.io.DataInputStream in = new java.io.DataInputStream(new java.io.ByteArrayInputStream(payload));
        try {
            for (int conversationsPayloadSize = in.readInt(); conversationsPayloadSize != -1; conversationsPayloadSize = in.readInt()) {
                byte[] conversationsPayload = new byte[conversationsPayloadSize];
                in.readFully(conversationsPayload, 0, conversationsPayloadSize);
                java.lang.String packageName = in.readUTF();
                getOrCreatePackageData(packageName).getConversationStore().restore(conversationsPayload);
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to restore conversations from backup payload.", e);
        }
    }

    private com.android.server.people.data.PackageData createPackageData(java.lang.String packageName) {
        return new com.android.server.people.data.PackageData(packageName, this.mUserId, new com.android.server.people.data.UserData$$ExternalSyntheticLambda0(this), new com.android.server.people.data.UserData$$ExternalSyntheticLambda1(this), this.mScheduledExecutorService, this.mPerUserPeopleDataDir);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDefaultDialer(java.lang.String packageName) {
        return android.text.TextUtils.equals(this.mDefaultDialer, packageName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDefaultSmsApp(java.lang.String packageName) {
        return android.text.TextUtils.equals(this.mDefaultSmsApp, packageName);
    }
}
