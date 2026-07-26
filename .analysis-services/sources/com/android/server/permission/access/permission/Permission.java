package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: Permission.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0002\b%\n\u0002\u0010\"\n\u0002\b\u001e\b\u0086\b\u0018\u0000 Y2\u00020\u0001:\u0001YB9\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005¢\u0006\u0002\u0010\fJ\t\u0010L\u001a\u00020\u0003HÆ\u0003J\t\u0010M\u001a\u00020\u0005HÆ\u0003J\t\u0010N\u001a\u00020\u0007HÆ\u0003J\t\u0010O\u001a\u00020\u0007HÆ\u0003J\t\u0010P\u001a\u00020\nHÆ\u0003J\t\u0010Q\u001a\u00020\u0005HÆ\u0003JE\u0010R\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u0005HÆ\u0001J\u0013\u0010S\u001a\u00020\u00052\b\u0010T\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\u000e\u0010U\u001a\u00020\n2\u0006\u0010V\u001a\u00020\u0007J\t\u0010W\u001a\u00020\u0007HÖ\u0001J\t\u0010X\u001a\u00020\u0016HÖ\u0001R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0011\u001a\u00020\u00078Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u000eR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u0004\u0018\u00010\u00168Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0012\u0010\u0019\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001a\u0010\u0010R\u0012\u0010\u001b\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0010R\u0012\u0010\u001c\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u0010R\u0012\u0010\u001d\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u0010R\u0012\u0010\u001e\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001e\u0010\u0010R\u0012\u0010\u001f\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u0010R\u0012\u0010 \u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b \u0010\u0010R\u0012\u0010!\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b!\u0010\u0010R\u0012\u0010\"\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b\"\u0010\u0010R\u0012\u0010#\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b#\u0010\u0010R\u0012\u0010$\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b$\u0010\u0010R\u0012\u0010%\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b%\u0010\u0010R\u0012\u0010&\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b&\u0010\u0010R\u0012\u0010'\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b'\u0010\u0010R\u0012\u0010(\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b(\u0010\u0010R\u0012\u0010)\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b)\u0010\u0010R\u0012\u0010*\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b*\u0010\u0010R\u0012\u0010+\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b+\u0010\u0010R\u0012\u0010,\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b,\u0010\u0010R\u0012\u0010-\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b-\u0010\u0010R\u0012\u0010.\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b.\u0010\u0010R\u0012\u0010/\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b/\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0010R\u0012\u00100\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b0\u0010\u0010R\u0012\u00101\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b1\u0010\u0010R\u0012\u00102\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b2\u0010\u0010R\u0012\u00103\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b3\u0010\u0010R\u0012\u00104\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b4\u0010\u0010R\u0012\u00105\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b5\u0010\u0010R\u0012\u00106\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b6\u0010\u0010R\u0012\u00107\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b7\u0010\u0010R\u0012\u00108\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b8\u0010\u0010R\u0012\u00109\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b9\u0010\u0010R\u0012\u0010:\u001a\u00020\u00058Æ\u0002¢\u0006\u0006\u001a\u0004\b:\u0010\u0010R\u0018\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00160<8Æ\u0002¢\u0006\u0006\u001a\u0004\b=\u0010>R\u0012\u0010?\u001a\u00020\u00168Æ\u0002¢\u0006\u0006\u001a\u0004\b@\u0010\u0018R\u0012\u0010A\u001a\u00020\u00168Æ\u0002¢\u0006\u0006\u001a\u0004\bB\u0010\u0018R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\bC\u0010DR\u0012\u0010E\u001a\u00020\u00078Æ\u0002¢\u0006\u0006\u001a\u0004\bF\u0010\u000eR\u0012\u0010G\u001a\u00020\u00078Æ\u0002¢\u0006\u0006\u001a\u0004\bH\u0010\u000eR\u0012\u0010I\u001a\u00020\u00078Æ\u0002¢\u0006\u0006\u001a\u0004\bJ\u0010\u000eR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\bK\u0010\u000e¨\u0006Z"}, d2 = {"Lcom/android/server/permission/access/permission/Permission;", "", "permissionInfo", "Landroid/content/pm/PermissionInfo;", "isReconciled", "", "type", "", "appId", "gids", "", "areGidsPerUser", "(Landroid/content/pm/PermissionInfo;ZII[IZ)V", "getAppId", "()I", "getAreGidsPerUser", "()Z", "footprint", "getFootprint", "getGids", "()[I", "groupName", "", "getGroupName", "()Ljava/lang/String;", "hasGids", "getHasGids", "isAppOp", "isAppPredictor", "isCompanion", "isConfigurator", "isDevelopment", "isDynamic", "isHardOrSoftRestricted", "isHardRestricted", "isImmutablyRestricted", "isIncidentReportApprover", "isInstaller", "isInstant", "isInternal", "isKnownSigner", "isModule", "isNormal", "isOem", "isPre23", "isPreInstalled", "isPrivileged", "isRecents", "isRemoved", "isRetailDemo", "isRole", "isRuntime", "isRuntimeOnly", "isSetup", "isSignature", "isSoftRestricted", "isSystemTextClassifier", "isVendorPrivileged", "isVerifier", "knownCerts", "", "getKnownCerts", "()Ljava/util/Set;", "name", "getName", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "getPackageName", "getPermissionInfo", "()Landroid/content/pm/PermissionInfo;", "protection", "getProtection", "protectionFlags", "getProtectionFlags", "protectionLevel", "getProtectionLevel", "getType", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "getGidsForUser", "userId", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "toString", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class Permission {
    public static final com.android.server.permission.access.permission.Permission.Companion Companion = new com.android.server.permission.access.permission.Permission.Companion(null);
    public static final int TYPE_DYNAMIC = 2;
    public static final int TYPE_MANIFEST = 0;
    private final int appId;
    private final boolean areGidsPerUser;
    private final int[] gids;
    private final boolean isReconciled;
    private final android.content.pm.PermissionInfo permissionInfo;
    private final int type;

    public static /* synthetic */ com.android.server.permission.access.permission.Permission copy$default(com.android.server.permission.access.permission.Permission permission, android.content.pm.PermissionInfo permissionInfo, boolean z, int i, int i2, int[] iArr, boolean z2, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            permissionInfo = permission.permissionInfo;
        }
        if ((i3 & 2) != 0) {
            z = permission.isReconciled;
        }
        boolean z3 = z;
        if ((i3 & 4) != 0) {
            i = permission.type;
        }
        int i4 = i;
        if ((i3 & 8) != 0) {
            i2 = permission.appId;
        }
        int i5 = i2;
        if ((i3 & 16) != 0) {
            iArr = permission.gids;
        }
        int[] iArr2 = iArr;
        if ((i3 & 32) != 0) {
            z2 = permission.areGidsPerUser;
        }
        return permission.copy(permissionInfo, z3, i4, i5, iArr2, z2);
    }

    public final android.content.pm.PermissionInfo component1() {
        return this.permissionInfo;
    }

    public final boolean component2() {
        return this.isReconciled;
    }

    public final int component3() {
        return this.type;
    }

    public final int component4() {
        return this.appId;
    }

    public final int[] component5() {
        return this.gids;
    }

    public final boolean component6() {
        return this.areGidsPerUser;
    }

    public final com.android.server.permission.access.permission.Permission copy(android.content.pm.PermissionInfo permissionInfo, boolean z, int i, int i2, int[] iArr, boolean z2) {
        return new com.android.server.permission.access.permission.Permission(permissionInfo, z, i, i2, iArr, z2);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.permission.access.permission.Permission)) {
            return false;
        }
        com.android.server.permission.access.permission.Permission permission = (com.android.server.permission.access.permission.Permission) obj;
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.permissionInfo, permission.permissionInfo) && this.isReconciled == permission.isReconciled && this.type == permission.type && this.appId == permission.appId && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.gids, permission.gids) && this.areGidsPerUser == permission.areGidsPerUser;
    }

    public int hashCode() {
        return (((((((((this.permissionInfo.hashCode() * 31) + java.lang.Boolean.hashCode(this.isReconciled)) * 31) + java.lang.Integer.hashCode(this.type)) * 31) + java.lang.Integer.hashCode(this.appId)) * 31) + java.util.Arrays.hashCode(this.gids)) * 31) + java.lang.Boolean.hashCode(this.areGidsPerUser);
    }

    public java.lang.String toString() {
        return "Permission(permissionInfo=" + this.permissionInfo + ", isReconciled=" + this.isReconciled + ", type=" + this.type + ", appId=" + this.appId + ", gids=" + java.util.Arrays.toString(this.gids) + ", areGidsPerUser=" + this.areGidsPerUser + ")";
    }

    public Permission(android.content.pm.PermissionInfo permissionInfo, boolean isReconciled, int type, int appId, int[] gids, boolean areGidsPerUser) {
        this.permissionInfo = permissionInfo;
        this.isReconciled = isReconciled;
        this.type = type;
        this.appId = appId;
        this.gids = gids;
        this.areGidsPerUser = areGidsPerUser;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ Permission(android.content.pm.PermissionInfo permissionInfo, boolean z, int i, int i2, int[] iArr, boolean z2, int i3, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        int[] iArr2;
        boolean z3;
        if ((i3 & 16) == 0) {
            iArr2 = iArr;
        } else {
            iArr2 = libcore.util.EmptyArray.INT;
        }
        if ((i3 & 32) == 0) {
            z3 = z2;
        } else {
            z3 = false;
        }
        this(permissionInfo, z, i, i2, iArr2, z3);
    }

    public final android.content.pm.PermissionInfo getPermissionInfo() {
        return this.permissionInfo;
    }

    public final boolean isReconciled() {
        return this.isReconciled;
    }

    public final int getType() {
        return this.type;
    }

    public final int getAppId() {
        return this.appId;
    }

    public final int[] getGids() {
        return this.gids;
    }

    public final boolean getAreGidsPerUser() {
        return this.areGidsPerUser;
    }

    public final java.lang.String getName() {
        return getPermissionInfo().name;
    }

    public final java.lang.String getPackageName() {
        return getPermissionInfo().packageName;
    }

    public final java.lang.String getGroupName() {
        return getPermissionInfo().group;
    }

    public final boolean isDynamic() {
        return getType() == 2;
    }

    public final int getProtectionLevel() {
        return getPermissionInfo().protectionLevel;
    }

    public final int getProtection() {
        return getPermissionInfo().getProtection();
    }

    public final boolean isInternal() {
        return getPermissionInfo().getProtection() == 4;
    }

    public final boolean isNormal() {
        return getPermissionInfo().getProtection() == 0;
    }

    public final boolean isRuntime() {
        return getPermissionInfo().getProtection() == 1;
    }

    public final boolean isSignature() {
        return getPermissionInfo().getProtection() == 2;
    }

    public final int getProtectionFlags() {
        return getPermissionInfo().getProtectionFlags();
    }

    public final boolean isAppOp() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 64);
    }

    public final boolean isAppPredictor() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 2097152);
    }

    public final boolean isCompanion() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 8388608);
    }

    public final boolean isConfigurator() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 524288);
    }

    public final boolean isDevelopment() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 32);
    }

    public final boolean isIncidentReportApprover() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 1048576);
    }

    public final boolean isInstaller() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 256);
    }

    public final boolean isInstant() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 4096);
    }

    public final boolean isKnownSigner() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 134217728);
    }

    public final boolean isModule() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 4194304);
    }

    public final boolean isOem() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 16384);
    }

    public final boolean isPre23() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 128);
    }

    public final boolean isPreInstalled() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 1024);
    }

    public final boolean isPrivileged() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 16);
    }

    public final boolean isRecents() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 33554432);
    }

    public final boolean isRetailDemo() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 16777216);
    }

    public final boolean isRole() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 67108864);
    }

    public final boolean isRuntimeOnly() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 8192);
    }

    public final boolean isSetup() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 2048);
    }

    public final boolean isSystemTextClassifier() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 65536);
    }

    public final boolean isVendorPrivileged() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 32768);
    }

    public final boolean isVerifier() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().getProtectionFlags(), 512);
    }

    public final boolean isHardRestricted() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().flags, 4);
    }

    public final boolean isRemoved() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().flags, 2);
    }

    public final boolean isSoftRestricted() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().flags, 8);
    }

    public final boolean isHardOrSoftRestricted() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().flags, 4) || com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().flags, 8);
    }

    public final boolean isImmutablyRestricted() {
        return com.android.server.permission.access.util.IntExtensionsKt.hasBits(getPermissionInfo().flags, 16);
    }

    public final java.util.Set<java.lang.String> getKnownCerts() {
        return getPermissionInfo().knownCerts;
    }

    public final boolean getHasGids() {
        return !(getGids().length == 0);
    }

    public final int getFootprint() {
        return getPermissionInfo().name.length() + getPermissionInfo().calculateFootprint();
    }

    public final int[] getGidsForUser(int userId) {
        if (this.areGidsPerUser) {
            int length = this.gids.length;
            int[] iArr = new int[length];
            for (int i = 0; i < length; i++) {
                iArr[i] = android.os.UserHandle.getUid(userId, this.gids[i]);
            }
            return iArr;
        }
        int[] iArr2 = this.gids;
        int[] iArrCopyOf = java.util.Arrays.copyOf(iArr2, iArr2.length);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(iArrCopyOf, "copyOf(...)");
        return iArrCopyOf;
    }

    /* JADX INFO: compiled from: Permission.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lcom/android/server/permission/access/permission/Permission$Companion;", "", "()V", "TYPE_DYNAMIC", "", "TYPE_MANIFEST", "typeToString", "", "type", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final java.lang.String typeToString(int type) {
            switch (type) {
                case 0:
                    return "TYPE_MANIFEST";
                case 1:
                default:
                    return java.lang.String.valueOf(type);
                case 2:
                    return "TYPE_DYNAMIC";
            }
        }
    }
}
