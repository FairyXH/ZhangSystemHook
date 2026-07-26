package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerException extends java.lang.Exception {
    public static final int INTERNAL_ERROR_APEX_MORE_THAN_ONE_FILE = -37;
    public static final int INTERNAL_ERROR_APEX_NOT_DIRECTORY = -36;
    public static final int INTERNAL_ERROR_ARCHIVE_NO_INSTALLER_TITLE = -39;
    public static final int INTERNAL_ERROR_DECOMPRESS_STUB = -11;
    public static final int INTERNAL_ERROR_DERIVING_ABI = -4;
    public static final int INTERNAL_ERROR_DUP_STATIC_SHARED_LIB_PROVIDER = -13;
    public static final int INTERNAL_ERROR_INSTALL_MISSING_CHILD_SESSIONS = -20;
    public static final int INTERNAL_ERROR_MISSING_SETTING_FOR_MOVE = -3;
    public static final int INTERNAL_ERROR_MISSING_USER = -38;
    public static final int INTERNAL_ERROR_MOVE = -2;
    public static final int INTERNAL_ERROR_NATIVE_LIBRARY_COPY = -1;
    public static final int INTERNAL_ERROR_NOT_PRIV_SHARED_USER = -19;
    public static final int INTERNAL_ERROR_OVERLAY_LOW_TARGET_SDK = -16;
    public static final int INTERNAL_ERROR_OVERLAY_SIGNATURE1 = -17;
    public static final int INTERNAL_ERROR_OVERLAY_SIGNATURE2 = -18;
    public static final int INTERNAL_ERROR_SHARED_LIB_INSTALLED_TWICE = -6;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_ACTIVITY = -27;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_BROADCAST_RECEIVER = -30;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_CONTENT_PROVIDER = -29;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_DYNAMIC = -25;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_FEATURE = -32;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_INSTANT = -23;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_LOW_SDK = -22;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_OVERLAY_TARGETS = -35;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_PERMISSION = -33;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_PERMISSION_GROUP = -31;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_PROTECTED_BROADCAST = -34;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_RENAMED = -24;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_SERVICE = -28;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_SHARED_USER = -26;
    public static final int INTERNAL_ERROR_STATIC_SHARED_LIB_VERSION_CODES_ORDER = -14;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_NOT_INSTALLED_FOR_USER = -9;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_PACKAGE_UNKNOWN = -7;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_SHOULD_NOT_HAVE_STORAGE = -10;
    public static final int INTERNAL_ERROR_STORAGE_INVALID_VOLUME_UNKNOWN = -8;
    public static final int INTERNAL_ERROR_SYSTEM_OVERLAY_STATIC = -15;
    public static final int INTERNAL_ERROR_UPDATED_VERSION_BETTER_THAN_SYSTEM = -12;
    public static final int INTERNAL_ERROR_VERIFY_MISSING_CHILD_SESSIONS = -21;
    public static final int INTERNAL_ERROR_VERITY_SETUP = -5;
    public final int error;
    public final int internalErrorCode;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface InternalErrorCode {
    }

    public static com.android.server.pm.PackageManagerException ofInternalError(java.lang.String detailMessage, int internalErrorCode) {
        return new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, detailMessage, internalErrorCode);
    }

    protected PackageManagerException(int error, java.lang.String detailMessage, int internalErrorCode) {
        super(detailMessage);
        this.error = error;
        this.internalErrorCode = internalErrorCode;
    }

    public PackageManagerException(int error, java.lang.String detailMessage) {
        super(detailMessage);
        this.error = error;
        this.internalErrorCode = 0;
    }

    public PackageManagerException(int error, java.lang.String detailMessage, java.lang.Throwable throwable) {
        super(detailMessage, throwable);
        this.error = error;
        this.internalErrorCode = 0;
    }

    public PackageManagerException(java.lang.Throwable e) {
        super(e);
        this.error = android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT;
        this.internalErrorCode = 0;
    }

    public static com.android.server.pm.PackageManagerException from(com.android.server.pm.Installer.InstallerException e) throws com.android.server.pm.PackageManagerException {
        throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, e.getMessage(), e.getCause());
    }
}
