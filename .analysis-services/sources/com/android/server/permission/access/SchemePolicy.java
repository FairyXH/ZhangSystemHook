package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessPolicy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0014\u0010\u0010\u001a\u00020\n*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\u0014\u0010\u0013\u001a\u00020\n*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J\u0014\u0010\u0014\u001a\u00020\n*\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u001c\u0010\u0017\u001a\u00020\n*\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u000fH\u0016J$\u0010\u001a\u001a\u00020\n*\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\f\u0010\u001b\u001a\u00020\n*\u00020\u001cH\u0016J,\u0010\u001d\u001a\u00020\n*\u00020\u00112\b\u0010\u001e\u001a\u0004\u0018\u00010\u00042\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00040 2\u0006\u0010!\u001a\u00020\"H\u0016J\f\u0010#\u001a\u00020\n*\u00020\u0011H\u0016J\u0014\u0010$\u001a\u00020\n*\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0014\u0010%\u001a\u00020\n*\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0014\u0010&\u001a\u00020\n*\u00020'2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u001c\u0010(\u001a\u00020\n*\u00020'2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0014\u0010)\u001a\u00020\n*\u00020*2\u0006\u0010\u000b\u001a\u00020+H\u0016J\u001c\u0010,\u001a\u00020\n*\u00020*2\u0006\u0010\u000b\u001a\u00020+2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J$\u0010-\u001a\u00020\n*\u00020\u00112\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010.\u001a\u00020\u000fH\u0016R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\u0006¨\u0006/"}, d2 = {"Lcom/android/server/permission/access/SchemePolicy;", "", "()V", "objectScheme", "", "getObjectScheme", "()Ljava/lang/String;", "subjectScheme", "getSubjectScheme", "migrateSystemState", "", "state", "Lcom/android/server/permission/access/MutableAccessState;", "migrateUserState", "userId", "", "onAppIdAdded", "Lcom/android/server/permission/access/MutateStateScope;", "appId", "onAppIdRemoved", "onPackageAdded", "packageState", "Lcom/android/server/pm/pkg/PackageState;", "onPackageInstalled", "onPackageRemoved", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "onPackageUninstalled", "onStateMutated", "Lcom/android/server/permission/access/GetStateScope;", "onStorageVolumeMounted", "volumeUuid", com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY, "", "isSystemUpdated", "", "onSystemReady", "onUserAdded", "onUserRemoved", "parseSystemState", "Lcom/android/modules/utils/BinaryXmlPullParser;", "parseUserState", "serializeSystemState", "Lcom/android/modules/utils/BinaryXmlSerializer;", "Lcom/android/server/permission/access/AccessState;", "serializeUserState", "upgradePackageState", "version", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class SchemePolicy {
    public abstract java.lang.String getObjectScheme();

    public abstract java.lang.String getSubjectScheme();

    public void onStateMutated(com.android.server.permission.access.GetStateScope $this$onStateMutated) {
    }

    public void onUserAdded(com.android.server.permission.access.MutateStateScope $this$onUserAdded, int userId) {
    }

    public void onUserRemoved(com.android.server.permission.access.MutateStateScope $this$onUserRemoved, int userId) {
    }

    public void onAppIdAdded(com.android.server.permission.access.MutateStateScope $this$onAppIdAdded, int appId) {
    }

    public void onAppIdRemoved(com.android.server.permission.access.MutateStateScope $this$onAppIdRemoved, int appId) {
    }

    public void onStorageVolumeMounted(com.android.server.permission.access.MutateStateScope $this$onStorageVolumeMounted, java.lang.String volumeUuid, java.util.List<java.lang.String> list, boolean isSystemUpdated) {
    }

    public void onPackageAdded(com.android.server.permission.access.MutateStateScope $this$onPackageAdded, com.android.server.pm.pkg.PackageState packageState) {
    }

    public void onPackageRemoved(com.android.server.permission.access.MutateStateScope $this$onPackageRemoved, java.lang.String packageName, int appId) {
    }

    public void onPackageInstalled(com.android.server.permission.access.MutateStateScope $this$onPackageInstalled, com.android.server.pm.pkg.PackageState packageState, int userId) {
    }

    public void onPackageUninstalled(com.android.server.permission.access.MutateStateScope $this$onPackageUninstalled, java.lang.String packageName, int appId, int userId) {
    }

    public void onSystemReady(com.android.server.permission.access.MutateStateScope $this$onSystemReady) {
    }

    public void migrateSystemState(com.android.server.permission.access.MutableAccessState state) {
    }

    public void migrateUserState(com.android.server.permission.access.MutableAccessState state, int userId) {
    }

    public void upgradePackageState(com.android.server.permission.access.MutateStateScope $this$upgradePackageState, com.android.server.pm.pkg.PackageState packageState, int userId, int version) {
    }

    public void parseSystemState(com.android.modules.utils.BinaryXmlPullParser $this$parseSystemState, com.android.server.permission.access.MutableAccessState state) {
    }

    public void serializeSystemState(com.android.modules.utils.BinaryXmlSerializer $this$serializeSystemState, com.android.server.permission.access.AccessState state) {
    }

    public void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) {
    }

    public void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
    }
}
