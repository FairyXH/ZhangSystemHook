package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageInstallerSession extends android.content.pm.IPackageInstallerSession.Stub {
    private static final java.lang.String APEX_FILE_EXTENSION = ".apex";
    static final int APP_METADATA_FILE_ACCESS_MODE = 416;
    private static final java.lang.String ATTR_ABI_OVERRIDE = "abiOverride";
    private static final java.lang.String ATTR_APPLICATION_ENABLED_SETTING_PERSISTENT = "applicationEnabledSettingPersistent";

    @java.lang.Deprecated
    private static final java.lang.String ATTR_APP_ICON = "appIcon";
    private static final java.lang.String ATTR_APP_LABEL = "appLabel";
    private static final java.lang.String ATTR_APP_PACKAGE_NAME = "appPackageName";
    private static final java.lang.String ATTR_CHECKSUM_KIND = "checksumKind";
    private static final java.lang.String ATTR_CHECKSUM_VALUE = "checksumValue";
    private static final java.lang.String ATTR_COMMITTED = "committed";
    private static final java.lang.String ATTR_COMMITTED_MILLIS = "committedMillis";
    private static final java.lang.String ATTR_CREATED_MILLIS = "createdMillis";
    private static final java.lang.String ATTR_DATALOADER_ARGUMENTS = "dataLoaderArguments";
    private static final java.lang.String ATTR_DATALOADER_CLASS_NAME = "dataLoaderClassName";
    private static final java.lang.String ATTR_DATALOADER_PACKAGE_NAME = "dataLoaderPackageName";
    private static final java.lang.String ATTR_DATALOADER_TYPE = "dataLoaderType";
    private static final java.lang.String ATTR_DESTROYED = "destroyed";
    private static final java.lang.String ATTR_DOMAIN = "domain";
    private static final java.lang.String ATTR_INITIATING_PACKAGE_NAME = "installInitiatingPackageName";
    private static final java.lang.String ATTR_INSTALLER_ATTRIBUTION_TAG = "installerAttributionTag";
    private static final java.lang.String ATTR_INSTALLER_PACKAGE_NAME = "installerPackageName";
    private static final java.lang.String ATTR_INSTALLER_PACKAGE_UID = "installerPackageUid";
    private static final java.lang.String ATTR_INSTALLER_UID = "installerUid";
    private static final java.lang.String ATTR_INSTALL_FLAGS = "installFlags";
    private static final java.lang.String ATTR_INSTALL_LOCATION = "installLocation";
    private static final java.lang.String ATTR_INSTALL_REASON = "installRason";
    private static final java.lang.String ATTR_IS_APPLIED = "isApplied";
    private static final java.lang.String ATTR_IS_DATALOADER = "isDataLoader";
    private static final java.lang.String ATTR_IS_FAILED = "isFailed";
    private static final java.lang.String ATTR_IS_READY = "isReady";
    private static final java.lang.String ATTR_LENGTH_BYTES = "lengthBytes";
    private static final java.lang.String ATTR_LOCATION = "location";
    private static final java.lang.String ATTR_METADATA = "metadata";
    private static final java.lang.String ATTR_MODE = "mode";
    private static final java.lang.String ATTR_MULTI_PACKAGE = "multiPackage";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_ORIGINATING_PACKAGE_NAME = "installOriginatingPackageName";
    private static final java.lang.String ATTR_ORIGINATING_UID = "originatingUid";
    private static final java.lang.String ATTR_ORIGINATING_URI = "originatingUri";
    private static final java.lang.String ATTR_PACKAGE_SOURCE = "packageSource";
    private static final java.lang.String ATTR_PARENT_SESSION_ID = "parentSessionId";
    private static final java.lang.String ATTR_PREPARED = "prepared";
    private static final java.lang.String ATTR_REFERRER_URI = "referrerUri";
    private static final java.lang.String ATTR_SEALED = "sealed";
    private static final java.lang.String ATTR_SESSION_ERROR_CODE = "errorCode";
    private static final java.lang.String ATTR_SESSION_ERROR_MESSAGE = "errorMessage";
    private static final java.lang.String ATTR_SESSION_ID = "sessionId";
    private static final java.lang.String ATTR_SESSION_STAGE_CID = "sessionStageCid";
    private static final java.lang.String ATTR_SESSION_STAGE_DIR = "sessionStageDir";
    private static final java.lang.String ATTR_SIGNATURE = "signature";
    private static final java.lang.String ATTR_SIZE_BYTES = "sizeBytes";
    private static final java.lang.String ATTR_STAGED_SESSION = "stagedSession";
    private static final java.lang.String ATTR_UPDATED_MILLIS = "updatedMillis";
    private static final java.lang.String ATTR_UPDATE_OWNER_PACKAGE_NAME = "updateOwnererPackageName";
    private static final java.lang.String ATTR_USER_ID = "userId";
    private static final java.lang.String ATTR_VOLUME_UUID = "volumeUuid";
    private static final long DEFAULT_APP_METADATA_BYTE_SIZE_LIMIT = 32000;
    private static final long DEFAULT_PRE_VERIFIED_DOMAINS_COUNT_LIMIT = 1000;
    private static final long DEFAULT_PRE_VERIFIED_DOMAIN_LENGTH_LIMIT = 256;
    private static final int INCREMENTAL_STORAGE_BLOCKED_TIMEOUT_MS = 2000;
    private static final int INCREMENTAL_STORAGE_UNHEALTHY_MONITORING_MS = 60000;
    private static final int INCREMENTAL_STORAGE_UNHEALTHY_TIMEOUT_MS = 7000;
    private static final int INVALID_TARGET_SDK_VERSION = Integer.MAX_VALUE;
    private static final boolean LOGD = true;
    private static final int MSG_INSTALL = 3;
    private static final int MSG_ON_PACKAGE_INSTALLED = 4;
    private static final int MSG_ON_SESSION_SEALED = 1;
    private static final int MSG_PRE_APPROVAL_REQUEST = 6;
    private static final int MSG_SESSION_VALIDATION_FAILURE = 5;
    private static final int MSG_STREAM_VALIDATE_AND_COMMIT = 2;
    private static final long PRE_APPROVAL_WITH_UPDATE_OWNERSHIP_FIX = 293644536;
    private static final java.lang.String PROPERTY_APP_METADATA_BYTE_SIZE_LIMIT = "app_metadata_byte_size_limit";
    private static final java.lang.String PROPERTY_NAME_INHERIT_NATIVE = "pi.inherit_native_on_dont_kill";
    private static final java.lang.String PROPERTY_PRE_VERIFIED_DOMAINS_COUNT_LIMIT = "pre_verified_domains_count_limit";
    private static final java.lang.String PROPERTY_PRE_VERIFIED_DOMAIN_LENGTH_LIMIT = "pre_verified_domain_length_limit";
    private static final java.lang.String REMOVE_MARKER_EXTENSION = ".removed";
    private static final long SILENT_INSTALL_ALLOWED = 325888262;
    private static final java.lang.String SYSTEM_DATA_LOADER_PACKAGE = "android";
    private static final java.lang.String TAG = "PackageInstallerSession";
    private static final java.lang.String TAG_AUTO_REVOKE_PERMISSIONS_MODE = "auto-revoke-permissions-mode";
    static final java.lang.String TAG_CHILD_SESSION = "childSession";
    private static final java.lang.String TAG_DENY_PERMISSION = "deny-permission";
    private static final java.lang.String TAG_GRANTED_RUNTIME_PERMISSION = "granted-runtime-permission";
    private static final java.lang.String TAG_GRANT_PERMISSION = "grant-permission";
    static final java.lang.String TAG_PRE_VERIFIED_DOMAINS = "preVerifiedDomains";
    static final java.lang.String TAG_SESSION = "session";
    static final java.lang.String TAG_SESSION_CHECKSUM = "sessionChecksum";
    static final java.lang.String TAG_SESSION_CHECKSUM_SIGNATURE = "sessionChecksumSignature";
    static final java.lang.String TAG_SESSION_FILE = "sessionFile";
    private static final java.lang.String TAG_WHITELISTED_RESTRICTED_PERMISSION = "whitelisted-restricted-permission";
    private static final long THROW_EXCEPTION_COMMIT_WITH_IMMUTABLE_PENDING_INTENT = 240618202;
    private static final int USER_ACTION_NOT_NEEDED = 0;
    private static final int USER_ACTION_PENDING_APK_PARSING = 2;
    private static final int USER_ACTION_REQUIRED = 1;
    private static final int USER_ACTION_REQUIRED_UPDATE_OWNER_REMINDER = 3;
    private long committedMillis;
    final long createdMillis;
    private final com.android.server.pm.PackageInstallerService.InternalCallback mCallback;
    private final android.content.Context mContext;
    private volatile boolean mDestroyed;
    private java.lang.String mFinalMessage;
    private int mFinalStatus;
    private final android.os.Handler mHandler;
    private boolean mHasDeviceAdminReceiver;
    private android.os.incremental.IncrementalFileStorages mIncrementalFileStorages;
    private java.io.File mInheritedFilesBase;
    private com.android.server.pm.InstallSource mInstallSource;
    private final com.android.server.pm.Installer mInstaller;
    private volatile int mInstallerUid;
    private final java.lang.String mOriginalInstallerPackageName;
    private final int mOriginalInstallerUid;
    private android.content.pm.parsing.PackageLite mPackageLite;
    private java.lang.String mPackageName;
    private int mParentSessionId;
    private java.lang.Runnable mPendingAbandonCallback;
    private final com.android.server.pm.PackageManagerService mPm;
    private android.content.pm.verify.domain.DomainSet mPreVerifiedDomains;
    private android.content.pm.PackageInstaller.PreapprovalDetails mPreapprovalDetails;
    private android.content.IntentSender mPreapprovalRemoteStatusReceiver;
    private boolean mPrepared;
    private android.content.IntentSender mRemoteStatusReceiver;
    private java.io.File mResolvedBaseFile;
    private boolean mSessionApplied;
    private int mSessionErrorCode;
    private java.lang.String mSessionErrorMessage;
    private boolean mSessionFailed;
    private final com.android.server.pm.PackageSessionProvider mSessionProvider;
    private boolean mSessionReady;
    private boolean mShouldBeSealed;
    private android.content.pm.SigningDetails mSigningDetails;
    private final com.android.server.pm.SilentUpdatePolicy mSilentUpdatePolicy;
    final com.android.server.pm.PackageInstallerSession.StagedSession mStagedSession;
    private final com.android.server.pm.StagingManager mStagingManager;
    private java.lang.Boolean mUserActionRequired;
    private int mUserActionRequirement;
    private boolean mVerityFoundForApks;
    private long mVersionCode;
    final android.content.pm.PackageInstaller.SessionParams params;
    final int sessionId;
    final java.lang.String stageCid;
    final java.io.File stageDir;
    private long updatedMillis;
    final int userId;
    private static final int[] EMPTY_CHILD_SESSION_ARRAY = libcore.util.EmptyArray.INT;
    private static final android.content.pm.InstallationFile[] EMPTY_INSTALLATION_FILE_ARRAY = new android.content.pm.InstallationFile[0];
    public static com.android.server.pm.IPackageInstallerSessionExt.IStaticExt sStaticExt = (com.android.server.pm.IPackageInstallerSessionExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageInstallerSessionExt.IStaticExt.class).create();
    private static final java.io.FileFilter sAddedApkFilter = new java.io.FileFilter() { // from class: com.android.server.pm.PackageInstallerSession.1
        @Override // java.io.FileFilter
        public boolean accept(java.io.File file) {
            return (file.isDirectory() || file.getName().endsWith(com.android.server.pm.PackageInstallerSession.REMOVE_MARKER_EXTENSION) || file.getName().endsWith(".idsig") || com.android.server.pm.PackageInstallerSession.isAppMetadata(file) || android.content.pm.dex.DexMetadataHelper.isDexMetadataFile(file) || com.android.internal.security.VerityUtils.isFsveritySignatureFile(file) || com.android.server.pm.ApkChecksums.isDigestOrDigestSignatureFile(file)) ? false : true;
        }
    };
    private static final java.io.FileFilter sAddedFilter = new java.io.FileFilter() { // from class: com.android.server.pm.PackageInstallerSession.2
        @Override // java.io.FileFilter
        public boolean accept(java.io.File file) {
            return (file.isDirectory() || file.getName().endsWith(com.android.server.pm.PackageInstallerSession.REMOVE_MARKER_EXTENSION)) ? false : true;
        }
    };
    private static final java.io.FileFilter sRemovedFilter = new java.io.FileFilter() { // from class: com.android.server.pm.PackageInstallerSession.3
        @Override // java.io.FileFilter
        public boolean accept(java.io.File file) {
            return !file.isDirectory() && file.getName().endsWith(com.android.server.pm.PackageInstallerSession.REMOVE_MARKER_EXTENSION);
        }
    };
    private final java.util.concurrent.atomic.AtomicInteger mActiveCount = new java.util.concurrent.atomic.AtomicInteger();
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.concurrent.atomic.AtomicBoolean mTransactionLock = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final java.lang.Object mProgressLock = new java.lang.Object();
    private float mClientProgress = 0.0f;
    private float mInternalProgress = 0.0f;
    private float mProgress = 0.0f;
    private float mReportedProgress = -1.0f;
    private float mIncrementalProgress = 0.0f;
    private boolean mSealed = false;
    private final java.util.concurrent.atomic.AtomicBoolean mPreapprovalRequested = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final java.util.concurrent.atomic.AtomicBoolean mCommitted = new java.util.concurrent.atomic.AtomicBoolean(false);
    private boolean mStageDirInUse = false;
    private boolean mVerificationInProgress = false;
    private boolean mPermissionsManuallyAccepted = false;
    private final java.util.ArrayList<android.os.RevocableFileDescriptor> mFds = new java.util.ArrayList<>();
    private final java.util.ArrayList<android.os.FileBridge> mBridges = new java.util.ArrayList<>();
    private final android.util.SparseArray<com.android.server.pm.PackageInstallerSession> mChildSessions = new android.util.SparseArray<>();
    com.android.server.pm.IPackageInstallerSessionExt mPackageInstallerSessionExt = (com.android.server.pm.IPackageInstallerSessionExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageInstallerSessionExt.class).base(this).create();
    com.android.server.pm.IPackageInstallerSessionSocExt mPackageInstallerSessionSocExt = (com.android.server.pm.IPackageInstallerSessionSocExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageInstallerSessionSocExt.class).base(this).create();
    private boolean mMarkStageDirUserNoConsume = false;
    private final android.util.ArraySet<com.android.server.pm.PackageInstallerSession.FileEntry> mFiles = new android.util.ArraySet<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.PackageInstallerSession.PerFileChecksum> mChecksums = new android.util.ArrayMap<>();
    private boolean mHasAppMetadataFile = false;
    private final java.util.List<java.io.File> mResolvedStagedFiles = new java.util.ArrayList();
    private final java.util.List<java.io.File> mResolvedInheritedFiles = new java.util.ArrayList();
    private final java.util.List<java.lang.String> mResolvedInstructionSets = new java.util.ArrayList();
    private final java.util.List<java.lang.String> mResolvedNativeLibPaths = new java.util.ArrayList();
    private final java.util.Set<android.content.IntentSender> mUnarchivalListeners = new android.util.ArraySet();
    private volatile boolean mDataLoaderFinished = false;
    private int mValidatedTargetSdk = Integer.MAX_VALUE;
    private int mUnarchivalStatus = -1;
    private final android.os.Handler.Callback mHandlerCallback = new android.os.Handler.Callback() { // from class: com.android.server.pm.PackageInstallerSession.4
        @Override // android.os.Handler.Callback
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.pm.PackageInstallerSession.this.handleSessionSealed();
                    break;
                case 2:
                    com.android.server.pm.PackageInstallerSession.this.handleStreamValidateAndCommit();
                    break;
                case 3:
                    com.android.server.pm.PackageInstallerSession.this.handleInstall();
                    break;
                case 4:
                    com.android.internal.os.SomeArgs args = (com.android.internal.os.SomeArgs) msg.obj;
                    java.lang.String packageName = (java.lang.String) args.arg1;
                    java.lang.String message = (java.lang.String) args.arg2;
                    android.os.Bundle extras = (android.os.Bundle) args.arg3;
                    android.content.IntentSender statusReceiver = (android.content.IntentSender) args.arg4;
                    int returnCode = args.argi1;
                    boolean isPreapproval = args.argi2 == 1;
                    args.recycle();
                    com.android.server.pm.PackageInstallerSession.sendOnPackageInstalled(com.android.server.pm.PackageInstallerSession.this.mContext, statusReceiver, com.android.server.pm.PackageInstallerSession.this.sessionId, com.android.server.pm.PackageInstallerSession.this.isInstallerDeviceOwnerOrAffiliatedProfileOwner(), com.android.server.pm.PackageInstallerSession.this.userId, packageName, returnCode, isPreapproval, message, extras);
                    break;
                case 5:
                    int error = msg.arg1;
                    java.lang.String detailMessage = (java.lang.String) msg.obj;
                    com.android.server.pm.PackageInstallerSession.this.onSessionValidationFailure(error, detailMessage);
                    break;
                case 6:
                    com.android.server.pm.PackageInstallerSession.this.handlePreapprovalRequest();
                    break;
            }
            com.android.server.pm.PackageInstallerSession.this.mPackageInstallerSessionExt.handleInHandlerCallback(msg, com.android.server.pm.PackageInstallerSession.this, com.android.server.pm.PackageInstallerSession.this.mPm);
            return true;
        }
    };
    private final com.android.server.pm.IPackageInstallerSessionWrapper mWrapper = new com.android.server.pm.PackageInstallerSession.PackageInstallerSessionWrapper();

    @interface UserActionRequirement {
    }

    static class FileEntry {
        private final android.content.pm.InstallationFile mFile;
        private final int mIndex;

        FileEntry(int index, android.content.pm.InstallationFile file) {
            this.mIndex = index;
            this.mFile = file;
        }

        int getIndex() {
            return this.mIndex;
        }

        android.content.pm.InstallationFile getFile() {
            return this.mFile;
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.pm.PackageInstallerSession.FileEntry)) {
                return false;
            }
            com.android.server.pm.PackageInstallerSession.FileEntry rhs = (com.android.server.pm.PackageInstallerSession.FileEntry) obj;
            return this.mFile.getLocation() == rhs.mFile.getLocation() && android.text.TextUtils.equals(this.mFile.getName(), rhs.mFile.getName());
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mFile.getLocation()), this.mFile.getName());
        }
    }

    static class PerFileChecksum {
        private final android.content.pm.Checksum[] mChecksums;
        private final byte[] mSignature;

        PerFileChecksum(android.content.pm.Checksum[] checksums, byte[] signature) {
            this.mChecksums = checksums;
            this.mSignature = signature;
        }

        android.content.pm.Checksum[] getChecksums() {
            return this.mChecksums;
        }

        byte[] getSignature() {
            return this.mSignature;
        }
    }

    public class StagedSession implements com.android.server.pm.StagingManager.StagedSession {
        public StagedSession() {
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public java.util.List<com.android.server.pm.StagingManager.StagedSession> getChildSessions() {
            java.util.List<com.android.server.pm.StagingManager.StagedSession> childSessions;
            if (!com.android.server.pm.PackageInstallerSession.this.params.isMultiPackage) {
                return java.util.Collections.EMPTY_LIST;
            }
            synchronized (com.android.server.pm.PackageInstallerSession.this.mLock) {
                int size = com.android.server.pm.PackageInstallerSession.this.mChildSessions.size();
                childSessions = new java.util.ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    childSessions.add(((com.android.server.pm.PackageInstallerSession) com.android.server.pm.PackageInstallerSession.this.mChildSessions.valueAt(i)).mStagedSession);
                }
            }
            return childSessions;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public android.content.pm.PackageInstaller.SessionParams sessionParams() {
            return com.android.server.pm.PackageInstallerSession.this.params;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isMultiPackage() {
            return com.android.server.pm.PackageInstallerSession.this.params.isMultiPackage;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isApexSession() {
            return (com.android.server.pm.PackageInstallerSession.this.params.installFlags & 131072) != 0;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public int sessionId() {
            return com.android.server.pm.PackageInstallerSession.this.sessionId;
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean containsApexSession() {
            return sessionContains(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerSession$StagedSession$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.pm.StagingManager.StagedSession) obj).isApexSession();
                }
            });
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public java.lang.String getPackageName() {
            return com.android.server.pm.PackageInstallerSession.this.getPackageName();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void setSessionReady() {
            com.android.server.pm.PackageInstallerSession.this.setSessionReady();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void setSessionFailed(int errorCode, java.lang.String errorMessage) {
            com.android.server.pm.PackageInstallerSession.this.mPackageInstallerSessionExt.recordSotaAppResult(com.android.server.pm.PackageInstallerSession.this.mStagedSession, errorCode, errorMessage);
            com.android.server.pm.PackageInstallerSession.this.setSessionFailed(errorCode, errorMessage);
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void setSessionApplied() {
            com.android.server.pm.PackageInstallerSession.this.setSessionApplied();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean containsApkSession() {
            return com.android.server.pm.PackageInstallerSession.this.containsApkSession();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public java.util.concurrent.CompletableFuture<java.lang.Void> installSession() {
            com.android.server.pm.PackageInstallerSession.this.assertCallerIsOwnerOrRootOrSystem();
            com.android.server.pm.PackageInstallerSession.this.assertNotChild("StagedSession#installSession");
            com.android.internal.util.Preconditions.checkArgument(isCommitted() && isSessionReady());
            return com.android.server.pm.PackageInstallerSession.this.install();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean hasParentSessionId() {
            return com.android.server.pm.PackageInstallerSession.this.hasParentSessionId();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public int getParentSessionId() {
            return com.android.server.pm.PackageInstallerSession.this.getParentSessionId();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isCommitted() {
            return com.android.server.pm.PackageInstallerSession.this.isCommitted();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isInTerminalState() {
            return com.android.server.pm.PackageInstallerSession.this.isInTerminalState();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isDestroyed() {
            return com.android.server.pm.PackageInstallerSession.this.isDestroyed();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public long getCommittedMillis() {
            return com.android.server.pm.PackageInstallerSession.this.getCommittedMillis();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean sessionContains(final java.util.function.Predicate<com.android.server.pm.StagingManager.StagedSession> filter) {
            return com.android.server.pm.PackageInstallerSession.this.sessionContains(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerSession$StagedSession$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return filter.test(((com.android.server.pm.PackageInstallerSession) obj).mStagedSession);
                }
            });
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isSessionReady() {
            return com.android.server.pm.PackageInstallerSession.this.isSessionReady();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isSessionApplied() {
            return com.android.server.pm.PackageInstallerSession.this.isSessionApplied();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public boolean isSessionFailed() {
            return com.android.server.pm.PackageInstallerSession.this.isSessionFailed();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void abandon() {
            com.android.server.pm.PackageInstallerSession.this.abandon();
        }

        @Override // com.android.server.pm.StagingManager.StagedSession
        public void verifySession() {
            com.android.server.pm.PackageInstallerSession.this.assertCallerIsOwnerOrRootOrSystem();
            if (isCommittedAndNotInTerminalState()) {
                com.android.server.pm.PackageInstallerSession.this.verify();
            }
        }

        private boolean isCommittedAndNotInTerminalState() {
            java.lang.String errorMsg = null;
            if (!isCommitted()) {
                errorMsg = android.text.TextUtils.formatSimple("The session %d should be committed", new java.lang.Object[]{java.lang.Integer.valueOf(com.android.server.pm.PackageInstallerSession.this.sessionId)});
            } else if (isSessionApplied()) {
                errorMsg = android.text.TextUtils.formatSimple("The session %d has applied", new java.lang.Object[]{java.lang.Integer.valueOf(com.android.server.pm.PackageInstallerSession.this.sessionId)});
            } else if (isSessionFailed()) {
                synchronized (com.android.server.pm.PackageInstallerSession.this.mLock) {
                    errorMsg = android.text.TextUtils.formatSimple("The session %d has failed with error: %s", new java.lang.Object[]{java.lang.Integer.valueOf(com.android.server.pm.PackageInstallerSession.this.sessionId), com.android.server.pm.PackageInstallerSession.this.mSessionErrorMessage});
                }
            }
            if (errorMsg != null) {
                android.util.Slog.e(com.android.server.pm.PackageInstallerSession.TAG, "verifySession error: " + errorMsg);
                setSessionFailed(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, errorMsg);
                com.android.server.pm.PackageInstallerSession.this.onSessionVerificationFailure(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, errorMsg);
                return false;
            }
            return true;
        }
    }

    static boolean isDataLoaderInstallation(android.content.pm.PackageInstaller.SessionParams params) {
        return params.dataLoaderParams != null;
    }

    static boolean isSystemDataLoaderInstallation(android.content.pm.PackageInstaller.SessionParams params) {
        if (!isDataLoaderInstallation(params)) {
            return false;
        }
        return "android".equals(params.dataLoaderParams.getComponentName().getPackageName());
    }

    static boolean isArchivedInstallation(int installFlags) {
        return (134217728 & installFlags) != 0;
    }

    private boolean isDataLoaderInstallation() {
        return isDataLoaderInstallation(this.params);
    }

    private boolean isStreamingInstallation() {
        return isDataLoaderInstallation() && this.params.dataLoaderParams.getType() == 1;
    }

    private boolean isIncrementalInstallation() {
        return isDataLoaderInstallation() && this.params.dataLoaderParams.getType() == 2;
    }

    private boolean isSystemDataLoaderInstallation() {
        return isSystemDataLoaderInstallation(this.params);
    }

    private boolean isArchivedInstallation() {
        return isArchivedInstallation(this.params.installFlags);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInstallerDeviceOwnerOrAffiliatedProfileOwner() {
        android.app.admin.DevicePolicyManagerInternal dpmi;
        assertNotLocked("isInstallerDeviceOwnerOrAffiliatedProfileOwner");
        return this.userId == android.os.UserHandle.getUserId(getInstallerUid()) && (dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class)) != null && dpmi.canSilentlyInstallPackage(getInstallSource().mInstallerPackageName, this.mInstallerUid);
    }

    static boolean isEmergencyInstallerEnabled(java.lang.String packageName, com.android.server.pm.Computer snapshot, int userId, int installerUid) {
        com.android.server.pm.pkg.PackageStateInternal ps = snapshot.getPackageStateInternal(packageName);
        if (ps == null || ps.getPkg() == null || !ps.isSystem()) {
            return false;
        }
        int uid = android.os.UserHandle.getUid(userId, ps.getAppId());
        java.lang.String emergencyInstaller = ps.getPkg().getEmergencyInstaller();
        if (emergencyInstaller == null || !com.android.internal.util.ArrayUtils.contains(snapshot.getPackagesForUid(installerUid), emergencyInstaller)) {
            return false;
        }
        return (snapshot.checkUidPermission("android.permission.INSTALL_PACKAGES", uid) == 0 || snapshot.checkUidPermission("android.permission.INSTALL_PACKAGE_UPDATES", uid) == 0 || snapshot.checkUidPermission("android.permission.INSTALL_SELF_UPDATES", uid) == 0) && snapshot.checkUidPermission("android.permission.EMERGENCY_INSTALL_PACKAGES", installerUid) == 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:104:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x013e  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0158  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x017f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x010a  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0113  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int computeUserActionRequirement() {
        /*
            Method dump skipped, instruction units count: 458
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageInstallerSession.computeUserActionRequirement():int");
    }

    private void updateUserActionRequirement(int requirement) {
        synchronized (this.mLock) {
            this.mUserActionRequirement = requirement;
        }
    }

    public PackageInstallerSession(com.android.server.pm.PackageInstallerService.InternalCallback callback, android.content.Context context, com.android.server.pm.PackageManagerService pm, com.android.server.pm.PackageSessionProvider sessionProvider, com.android.server.pm.SilentUpdatePolicy silentUpdatePolicy, android.os.Looper looper, com.android.server.pm.StagingManager stagingManager, int sessionId, int userId, int installerUid, com.android.server.pm.InstallSource installSource, android.content.pm.PackageInstaller.SessionParams params, long createdMillis, long committedMillis, java.io.File stageDir, java.lang.String stageCid, android.content.pm.InstallationFile[] files, android.util.ArrayMap<java.lang.String, com.android.server.pm.PackageInstallerSession.PerFileChecksum> checksums, boolean prepared, boolean committed, boolean destroyed, boolean sealed, int[] childSessionIds, int parentSessionId, boolean isReady, boolean isFailed, boolean isApplied, int sessionErrorCode, java.lang.String sessionErrorMessage, android.content.pm.verify.domain.DomainSet preVerifiedDomains) {
        int[] iArr = childSessionIds;
        this.mPrepared = false;
        this.mShouldBeSealed = false;
        this.mSessionErrorCode = 0;
        this.mDestroyed = false;
        this.mCallback = callback;
        this.mContext = context;
        this.mPm = pm;
        this.mInstaller = this.mPm != null ? this.mPm.mInstaller : null;
        this.mSessionProvider = sessionProvider;
        this.mSilentUpdatePolicy = silentUpdatePolicy;
        this.mHandler = new android.os.Handler(looper, this.mHandlerCallback);
        this.mStagingManager = stagingManager;
        this.sessionId = sessionId;
        this.userId = userId;
        this.mOriginalInstallerUid = installerUid;
        this.mInstallerUid = installerUid;
        this.mInstallSource = (com.android.server.pm.InstallSource) java.util.Objects.requireNonNull(installSource);
        this.mOriginalInstallerPackageName = this.mInstallSource.mInstallerPackageName;
        this.params = params;
        this.createdMillis = createdMillis;
        this.updatedMillis = createdMillis;
        this.committedMillis = committedMillis;
        this.stageDir = stageDir;
        this.stageCid = stageCid;
        this.mShouldBeSealed = sealed;
        if (iArr != null) {
            int i = 0;
            for (int length = iArr.length; i < length; length = length) {
                int childSessionId = iArr[i];
                this.mChildSessions.put(childSessionId, null);
                i++;
                iArr = childSessionIds;
            }
        }
        this.mParentSessionId = parentSessionId;
        if (files != null) {
            this.mFiles.ensureCapacity(files.length);
            int i2 = 0;
            int size = files.length;
            while (i2 < size) {
                android.content.pm.InstallationFile file = files[i2];
                int size2 = size;
                if (this.mFiles.add(new com.android.server.pm.PackageInstallerSession.FileEntry(i2, file))) {
                    i2++;
                    size = size2;
                } else {
                    throw new java.lang.IllegalArgumentException("Trying to add a duplicate installation file");
                }
            }
        }
        if (checksums != null) {
            this.mChecksums.putAll((android.util.ArrayMap<? extends java.lang.String, ? extends com.android.server.pm.PackageInstallerSession.PerFileChecksum>) checksums);
        }
        if (!params.isMultiPackage) {
            if ((stageDir == null) == (stageCid == null)) {
                throw new java.lang.IllegalArgumentException("Exactly one of stageDir or stageCid stage must be set");
            }
        }
        this.mPrepared = prepared;
        this.mCommitted.set(committed);
        this.mDestroyed = destroyed;
        this.mSessionReady = isReady;
        this.mSessionApplied = isApplied;
        this.mSessionFailed = isFailed;
        this.mSessionErrorCode = sessionErrorCode;
        this.mSessionErrorMessage = sessionErrorMessage != null ? sessionErrorMessage : "";
        this.mStagedSession = params.isStaged ? new com.android.server.pm.PackageInstallerSession.StagedSession() : null;
        this.mPreVerifiedDomains = preVerifiedDomains;
        if (isDataLoaderInstallation()) {
            if (isApexSession()) {
                throw new java.lang.IllegalArgumentException("DataLoader installation of APEX modules is not allowed.");
            }
            if (isSystemDataLoaderInstallation() && this.mContext.checkCallingOrSelfPermission("com.android.permission.USE_SYSTEM_DATA_LOADERS") != 0) {
                throw new java.lang.SecurityException("You need the com.android.permission.USE_SYSTEM_DATA_LOADERS permission to use system data loaders");
            }
        }
        if (isIncrementalInstallation() && !android.os.incremental.IncrementalManager.isAllowed()) {
            throw new java.lang.IllegalArgumentException("Incremental installation not allowed.");
        }
        if (isArchivedInstallation()) {
            if (params.mode != 1) {
                throw new java.lang.IllegalArgumentException("Archived installation can only be full install.");
            }
            if (!isStreamingInstallation() || !isSystemDataLoaderInstallation()) {
                throw new java.lang.IllegalArgumentException("Archived installation can only use Streaming System DataLoader.");
            }
        }
    }

    com.android.server.pm.PackageInstallerHistoricalSession createHistoricalSession() {
        float progress;
        float clientProgress;
        com.android.server.pm.PackageInstallerHistoricalSession packageInstallerHistoricalSession;
        synchronized (this.mProgressLock) {
            progress = this.mProgress;
            clientProgress = this.mClientProgress;
        }
        synchronized (this.mLock) {
            packageInstallerHistoricalSession = new com.android.server.pm.PackageInstallerHistoricalSession(this.sessionId, this.userId, this.mOriginalInstallerUid, this.mOriginalInstallerPackageName, this.mInstallSource, this.mInstallerUid, this.createdMillis, this.updatedMillis, this.committedMillis, this.stageDir, this.stageCid, clientProgress, progress, isCommitted(), isPreapprovalRequested(), this.mSealed, this.mPermissionsManuallyAccepted, this.mStageDirInUse, this.mDestroyed, this.mFds.size(), this.mBridges.size(), this.mFinalStatus, this.mFinalMessage, this.params, this.mParentSessionId, getChildSessionIdsLocked(), this.mSessionApplied, this.mSessionFailed, this.mSessionReady, this.mSessionErrorCode, this.mSessionErrorMessage, this.mPreapprovalDetails, this.mPreVerifiedDomains, this.mPackageName);
        }
        return packageInstallerHistoricalSession;
    }

    private boolean shouldScrubData(int callingUid) {
        return callingUid >= 10000 && getInstallerUid() != callingUid;
    }

    public android.content.pm.PackageInstaller.SessionInfo generateInfoForCaller(boolean includeIcon, int callingUid) {
        return generateInfoInternal(includeIcon, shouldScrubData(callingUid));
    }

    public android.content.pm.PackageInstaller.SessionInfo generateInfoScrubbed(boolean includeIcon) {
        return generateInfoInternal(includeIcon, true);
    }

    private android.content.pm.PackageInstaller.SessionInfo generateInfoInternal(boolean includeIcon, boolean scrubData) {
        float progress;
        java.lang.String packageName;
        android.content.pm.PackageInstaller.SessionInfo info = new android.content.pm.PackageInstaller.SessionInfo();
        synchronized (this.mProgressLock) {
            progress = this.mProgress;
        }
        synchronized (this.mLock) {
            info.sessionId = this.sessionId;
            info.userId = this.userId;
            info.installerPackageName = this.mInstallSource.mInstallerPackageName;
            info.installerAttributionTag = this.mInstallSource.mInstallerAttributionTag;
            info.resolvedBaseCodePath = null;
            if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_INSTALLED_SESSION_PATHS") == 0) {
                java.io.File file = this.mResolvedBaseFile;
                if (file == null) {
                    java.util.List<java.io.File> addedFiles = getAddedApksLocked();
                    if (addedFiles.size() > 0) {
                        file = addedFiles.get(0);
                    }
                }
                if (file != null) {
                    info.resolvedBaseCodePath = file.getAbsolutePath();
                }
            }
            info.progress = progress;
            info.sealed = this.mSealed;
            info.isCommitted = isCommitted();
            info.isPreapprovalRequested = isPreapprovalRequested();
            info.active = this.mActiveCount.get() > 0;
            info.mode = this.params.mode;
            info.installReason = this.params.installReason;
            info.installScenario = this.params.installScenario;
            info.sizeBytes = this.params.sizeBytes;
            if (this.mPreapprovalDetails != null) {
                packageName = this.mPreapprovalDetails.getPackageName();
            } else {
                packageName = this.mPackageName != null ? this.mPackageName : this.params.appPackageName;
            }
            info.appPackageName = packageName;
            if (includeIcon) {
                info.appIcon = (this.mPreapprovalDetails == null || this.mPreapprovalDetails.getIcon() == null) ? this.params.appIcon : this.mPreapprovalDetails.getIcon();
            }
            info.appLabel = this.mPreapprovalDetails != null ? this.mPreapprovalDetails.getLabel() : this.params.appLabel;
            info.installLocation = this.params.installLocation;
            if (!scrubData) {
                info.originatingUri = this.params.originatingUri;
            }
            info.originatingUid = this.params.originatingUid;
            if (!scrubData) {
                info.referrerUri = this.params.referrerUri;
            }
            info.grantedRuntimePermissions = this.params.getLegacyGrantedRuntimePermissions();
            info.whitelistedRestrictedPermissions = this.params.whitelistedRestrictedPermissions;
            info.autoRevokePermissionsMode = this.params.autoRevokePermissionsMode;
            info.installFlags = this.params.installFlags;
            info.rollbackLifetimeMillis = this.params.rollbackLifetimeMillis;
            info.rollbackImpactLevel = this.params.rollbackImpactLevel;
            info.isMultiPackage = this.params.isMultiPackage;
            info.isStaged = this.params.isStaged;
            info.rollbackDataPolicy = this.params.rollbackDataPolicy;
            info.parentSessionId = this.mParentSessionId;
            info.childSessionIds = getChildSessionIdsLocked();
            info.isSessionApplied = this.mSessionApplied;
            info.isSessionReady = this.mSessionReady;
            info.isSessionFailed = this.mSessionFailed;
            info.setSessionErrorCode(this.mSessionErrorCode, this.mSessionErrorMessage);
            info.createdMillis = this.createdMillis;
            info.updatedMillis = this.updatedMillis;
            info.requireUserAction = this.params.requireUserAction;
            info.installerUid = this.mInstallerUid;
            info.packageSource = this.params.packageSource;
            info.applicationEnabledSettingPersistent = this.params.applicationEnabledSettingPersistent;
            info.pendingUserActionReason = userActionRequirementToReason(this.mUserActionRequirement);
        }
        return info;
    }

    public boolean isPrepared() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mPrepared;
        }
        return z;
    }

    public boolean isSealed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSealed;
        }
        return z;
    }

    boolean isPreapprovalRequested() {
        return this.mPreapprovalRequested.get();
    }

    boolean isCommitted() {
        return this.mCommitted.get();
    }

    boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isInTerminalState() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionApplied || this.mSessionFailed;
        }
        return z;
    }

    public boolean isStagedAndInTerminalState() {
        return this.params.isStaged && isInTerminalState();
    }

    private void assertNotLocked(java.lang.String cookie) {
        if (java.lang.Thread.holdsLock(this.mLock)) {
            throw new java.lang.IllegalStateException(cookie + " is holding mLock");
        }
    }

    private void assertSealed(java.lang.String cookie) {
        if (!isSealed()) {
            throw new java.lang.IllegalStateException(cookie + " before sealing");
        }
    }

    private void assertPreparedAndNotPreapprovalRequestedLocked(java.lang.String cookie) {
        assertPreparedAndNotSealedLocked(cookie);
        if (isPreapprovalRequested()) {
            throw new java.lang.IllegalStateException(cookie + " not allowed after requesting");
        }
    }

    private void assertPreparedAndNotSealedLocked(java.lang.String cookie) {
        assertPreparedAndNotCommittedOrDestroyedLocked(cookie);
        if (this.mSealed) {
            throw new java.lang.SecurityException(cookie + " not allowed after sealing");
        }
    }

    private void assertPreparedAndNotCommittedOrDestroyedLocked(java.lang.String cookie) {
        assertPreparedAndNotDestroyedLocked(cookie);
        if (isCommitted()) {
            throw new java.lang.SecurityException(cookie + " not allowed after commit");
        }
    }

    private void assertPreparedAndNotDestroyedLocked(java.lang.String cookie) {
        if (!this.mPrepared) {
            throw new java.lang.IllegalStateException(cookie + " before prepared");
        }
        if (this.mDestroyed) {
            throw new java.lang.SecurityException(cookie + " not allowed after destruction");
        }
    }

    private void setClientProgressLocked(float progress) {
        boolean forcePublish = this.mClientProgress == 0.0f;
        this.mClientProgress = progress;
        computeProgressLocked(forcePublish);
    }

    public void setClientProgress(float progress) {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mProgressLock) {
            setClientProgressLocked(progress);
        }
    }

    public void addClientProgress(float progress) {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mProgressLock) {
            setClientProgressLocked(this.mClientProgress + progress);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void computeProgressLocked(boolean forcePublish) {
        if (!isIncrementalInstallation() || !isCommitted()) {
            this.mProgress = android.util.MathUtils.constrain(this.mClientProgress * 0.8f, 0.0f, 0.8f) + android.util.MathUtils.constrain(this.mInternalProgress * 0.2f, 0.0f, 0.2f);
        } else if (this.mIncrementalProgress - this.mProgress >= 0.01d) {
            this.mProgress = this.mIncrementalProgress;
        }
        if (forcePublish || this.mProgress - this.mReportedProgress >= 0.01d) {
            this.mReportedProgress = this.mProgress;
            this.mCallback.onSessionProgressChanged(this, this.mProgress);
        }
    }

    public java.lang.String[] getNames() {
        java.lang.String[] names;
        java.lang.String[] strArrRemoveString;
        assertCallerIsOwnerRootOrVerifier();
        synchronized (this.mLock) {
            assertPreparedAndNotDestroyedLocked("getNames");
            if (!isCommitted()) {
                names = getNamesLocked();
            } else {
                names = getStageDirContentsLocked();
            }
            strArrRemoveString = com.android.internal.util.ArrayUtils.removeString(names, com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME);
        }
        return strArrRemoveString;
    }

    private java.lang.String[] getStageDirContentsLocked() {
        if (this.stageDir == null) {
            return libcore.util.EmptyArray.STRING;
        }
        java.lang.String[] result = this.stageDir.list();
        if (result == null) {
            return libcore.util.EmptyArray.STRING;
        }
        return result;
    }

    private java.lang.String[] getNamesLocked() {
        if (!isDataLoaderInstallation()) {
            return getStageDirContentsLocked();
        }
        android.content.pm.InstallationFile[] files = getInstallationFilesLocked();
        java.lang.String[] result = new java.lang.String[files.length];
        int size = files.length;
        for (int i = 0; i < size; i++) {
            result[i] = files[i].getName();
        }
        return result;
    }

    private android.content.pm.InstallationFile[] getInstallationFilesLocked() {
        android.content.pm.InstallationFile[] result = new android.content.pm.InstallationFile[this.mFiles.size()];
        for (com.android.server.pm.PackageInstallerSession.FileEntry fileEntry : this.mFiles) {
            result[fileEntry.getIndex()] = fileEntry.getFile();
        }
        return result;
    }

    private static java.util.ArrayList<java.io.File> filterFiles(java.io.File parent, java.lang.String[] names, java.io.FileFilter filter) {
        java.util.ArrayList<java.io.File> result = new java.util.ArrayList<>(names.length);
        for (java.lang.String name : names) {
            java.io.File file = new java.io.File(parent, name);
            if (filter.accept(file)) {
                result.add(file);
            }
        }
        return result;
    }

    private java.util.List<java.io.File> getAddedApksLocked() {
        java.lang.String[] names = getNamesLocked();
        return filterFiles(this.stageDir, names, sAddedApkFilter);
    }

    private void enableFsVerityToAddedApksWithIdsig() throws com.android.server.pm.PackageManagerException {
        try {
            java.util.List<java.io.File> files = getAddedApksLocked();
            for (java.io.File file : files) {
                if (new java.io.File(file.getPath() + ".idsig").exists()) {
                    com.android.internal.security.VerityUtils.setUpFsverity(file.getPath());
                }
            }
        } catch (java.io.IOException e) {
            throw new com.android.server.pm.PrepareFailure(-118, "Failed to enable fs-verity to verify with idsig: " + e);
        }
    }

    private java.util.List<android.content.pm.parsing.ApkLite> getAddedApkLitesLocked() throws com.android.server.pm.PackageManagerException {
        if (!isArchivedInstallation()) {
            java.util.List<java.io.File> files = getAddedApksLocked();
            java.util.List<android.content.pm.parsing.ApkLite> result = new java.util.ArrayList<>(files.size());
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            int size = files.size();
            for (int i = 0; i < size; i++) {
                android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ApkLite> preParseRet = this.mPackageInstallerSessionExt.getPreParseRetInValidateApkInstall(files.get(i));
                android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ApkLite> parseResult = preParseRet != null ? preParseRet : android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input.reset(), files.get(i), 32);
                if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                    android.util.Slog.d(TAG, "parseApkLite end in validateApkInstall: " + (files.get(i) == null ? null : files.get(i).getAbsolutePath()));
                }
                if (parseResult.isError()) {
                    throw new com.android.server.pm.PackageManagerException(parseResult.getErrorCode(), parseResult.getErrorMessage(), parseResult.getException());
                }
                result.add((android.content.pm.parsing.ApkLite) parseResult.getResult());
            }
            return result;
        }
        android.content.pm.InstallationFile[] files2 = getInstallationFilesLocked();
        java.util.List<android.content.pm.parsing.ApkLite> result2 = new java.util.ArrayList<>(files2.length);
        int size2 = files2.length;
        for (int i2 = 0; i2 < size2; i2++) {
            java.io.File file = new java.io.File(this.stageDir, files2[i2].getName());
            if (sAddedApkFilter.accept(file)) {
                try {
                    com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata metadata = com.android.server.pm.PackageManagerShellCommandDataLoader.Metadata.fromByteArray(files2[i2].getMetadata());
                    if (metadata.getMode() != 4) {
                        throw new com.android.server.pm.PackageManagerException(-22, "File metadata is not for ARCHIVED package: " + file);
                    }
                    android.content.pm.ArchivedPackageParcel archPkg = metadata.getArchivedPackage();
                    if (archPkg == null) {
                        throw new com.android.server.pm.PackageManagerException(-22, "Metadata does not contain ArchivedPackage: " + file);
                    }
                    if (archPkg.packageName == null || archPkg.signingDetails == null) {
                        throw new com.android.server.pm.PackageManagerException(-22, "ArchivedPackage does not contain required info: " + file);
                    }
                    result2.add(new android.content.pm.parsing.ApkLite(file.getAbsolutePath(), archPkg));
                } catch (java.io.IOException e) {
                    throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Failed to ", e);
                }
            }
        }
        return result2;
    }

    private java.util.List<java.io.File> getRemovedFilesLocked() {
        java.lang.String[] names = getNamesLocked();
        return filterFiles(this.stageDir, names, sRemovedFilter);
    }

    public void setChecksums(java.lang.String name, android.content.pm.Checksum[] checksums, byte[] signature) {
        java.lang.String installerPackageName;
        if (checksums.length == 0) {
            return;
        }
        java.lang.String initiatingPackageName = getInstallSource().mInitiatingPackageName;
        if (!com.android.server.pm.PackageManagerServiceUtils.isInstalledByAdb(initiatingPackageName)) {
            installerPackageName = initiatingPackageName;
        } else {
            installerPackageName = getInstallSource().mInstallerPackageName;
        }
        if (android.text.TextUtils.isEmpty(installerPackageName)) {
            throw new java.lang.IllegalStateException("Installer package is empty.");
        }
        android.app.AppOpsManager appOps = (android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class);
        appOps.checkPackage(android.os.Binder.getCallingUid(), installerPackageName);
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        com.android.server.pm.pkg.AndroidPackage callingInstaller = pmi.getPackage(installerPackageName);
        if (callingInstaller == null) {
            throw new java.lang.IllegalStateException("Can't obtain calling installer's package.");
        }
        if (signature != null && signature.length != 0) {
            try {
                com.android.server.pm.ApkChecksums.verifySignature(checksums, signature);
            } catch (java.io.IOException | java.security.NoSuchAlgorithmException | java.security.SignatureException e) {
                throw new java.lang.IllegalArgumentException("Can't verify signature: " + e.getMessage(), e);
            }
        }
        for (android.content.pm.Checksum checksum : checksums) {
            if (checksum.getValue() == null || checksum.getValue().length > 64) {
                throw new java.lang.IllegalArgumentException("Invalid checksum.");
            }
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("addChecksums");
            if (this.mChecksums.containsKey(name)) {
                throw new java.lang.IllegalStateException("Duplicate checksums.");
            }
            this.mChecksums.put(name, new com.android.server.pm.PackageInstallerSession.PerFileChecksum(checksums, signature));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public void requestChecksums(java.lang.String name, int optional, int required, java.util.List trustedInstallers, android.content.pm.IOnChecksumsReadyListener onChecksumsReadyListener) throws android.os.ParcelableException {
        java.lang.String installerPackageName;
        assertCallerIsOwnerRootOrVerifier();
        java.io.File file = new java.io.File(this.stageDir, name);
        if (com.android.server.pm.PackageManagerServiceUtils.isInstalledByAdb(getInstallSource().mInitiatingPackageName)) {
            installerPackageName = getInstallSource().mInstallerPackageName;
        } else {
            installerPackageName = getInstallSource().mInitiatingPackageName;
        }
        try {
            this.mPm.requestFileChecksums(file, installerPackageName, optional, required, trustedInstallers, onChecksumsReadyListener);
        } catch (java.io.FileNotFoundException e) {
            throw new android.os.ParcelableException(e);
        }
    }

    public void removeSplit(java.lang.String splitName) {
        if (isDataLoaderInstallation()) {
            throw new java.lang.IllegalStateException("Cannot remove splits in a data loader installation session.");
        }
        if (android.text.TextUtils.isEmpty(this.params.appPackageName)) {
            throw new java.lang.IllegalStateException("Must specify package name to remove a split");
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("removeSplit");
            try {
                createRemoveSplitMarkerLocked(splitName);
            } catch (java.io.IOException e) {
                throw android.util.ExceptionUtils.wrap(e);
            }
        }
    }

    private static java.lang.String getRemoveMarkerName(java.lang.String name) {
        java.lang.String markerName = name + REMOVE_MARKER_EXTENSION;
        if (!android.os.FileUtils.isValidExtFilename(markerName)) {
            throw new java.lang.IllegalArgumentException("Invalid marker: " + markerName);
        }
        return markerName;
    }

    private void createRemoveSplitMarkerLocked(java.lang.String splitName) throws java.io.IOException {
        try {
            java.io.File target = new java.io.File(this.stageDir, getRemoveMarkerName(splitName));
            target.createNewFile();
            android.system.Os.chmod(target.getAbsolutePath(), 0);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    private void assertShellOrSystemCalling(java.lang.String operation) {
        switch (android.os.Binder.getCallingUid()) {
            case 0:
            case 1000:
            case 2000:
                break;
            default:
                this.mPackageInstallerSessionExt.adjustAssertShellOrSystemCallingThrowException(this.mPm, operation);
                break;
        }
    }

    private void assertCanWrite(boolean reverseMode) {
        if (isDataLoaderInstallation()) {
            throw new java.lang.IllegalStateException("Cannot write regular files in a data loader installation session.");
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotSealedLocked("assertCanWrite");
        }
        if (reverseMode) {
            assertShellOrSystemCalling("Reverse mode");
        }
    }

    private java.io.File getTmpAppMetadataFile() {
        return new java.io.File(android.os.Environment.getDataAppDirectory(this.params.volumeUuid), this.sessionId + "-" + com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME);
    }

    private java.io.File getStagedAppMetadataFile() {
        return new java.io.File(this.stageDir, com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME);
    }

    private static boolean isAppMetadata(java.lang.String name) {
        return name.endsWith(com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAppMetadata(java.io.File file) {
        return isAppMetadata(file.getName());
    }

    public android.os.ParcelFileDescriptor getAppMetadataFd() {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("getAppMetadataFd");
            if (!this.mHasAppMetadataFile) {
                return null;
            }
            try {
                return openReadInternalLocked(com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME);
            } catch (java.io.IOException e) {
                throw android.util.ExceptionUtils.wrap(e);
            }
        }
    }

    public void removeAppMetadata() {
        synchronized (this.mLock) {
            if (this.mHasAppMetadataFile) {
                getStagedAppMetadataFile().delete();
                this.mHasAppMetadataFile = false;
            }
        }
    }

    static long getAppMetadataSizeLimit() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getLong("package_manager_service", PROPERTY_APP_METADATA_BYTE_SIZE_LIMIT, DEFAULT_APP_METADATA_BYTE_SIZE_LIMIT);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public android.os.ParcelFileDescriptor openWriteAppMetadata() throws java.lang.Throwable {
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotSealedLocked("openWriteAppMetadata");
        }
        try {
            android.os.ParcelFileDescriptor fd = doWriteInternal(com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME, 0L, -1L, null);
            synchronized (this.mLock) {
                this.mHasAppMetadataFile = true;
            }
            return fd;
        } catch (java.io.IOException e) {
            throw android.util.ExceptionUtils.wrap(e);
        }
    }

    public android.os.ParcelFileDescriptor openWrite(java.lang.String name, long offsetBytes, long lengthBytes) throws java.lang.Throwable {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d(TAG, "openWrite: " + name + ", " + offsetBytes + ", " + lengthBytes);
        }
        assertCanWrite(false);
        try {
            this.mPackageInstallerSessionSocExt.boostBeforeOpenWrite();
            android.os.ParcelFileDescriptor pfd = doWriteInternal(name, offsetBytes, lengthBytes, null);
            return this.mPackageInstallerSessionExt.adjustResultInOpenWrite(pfd, name, this.mHandler, this.mPm, this.mInstallerUid, this.stageDir);
        } catch (java.io.IOException e) {
            throw android.util.ExceptionUtils.wrap(e);
        }
    }

    public void write(java.lang.String name, long offsetBytes, long lengthBytes, android.os.ParcelFileDescriptor fd) throws java.lang.Throwable {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d(TAG, "write: " + name + ", " + offsetBytes + ", " + lengthBytes);
        }
        assertCanWrite(fd != null);
        try {
            doWriteInternal(name, offsetBytes, lengthBytes, fd);
            this.mPackageInstallerSessionExt.afterWrite(name, this.mHandler, this.mPm, this.mInstallerUid, this.stageDir);
        } catch (java.io.IOException e) {
            throw android.util.ExceptionUtils.wrap(e);
        }
    }

    public void stageViaHardLink(java.lang.String path) throws java.lang.Exception {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 1000) {
            throw new java.lang.SecurityException("link() can only be run by the system");
        }
        java.io.File target = new java.io.File(path);
        java.io.File source = new java.io.File(this.stageDir, target.getName());
        java.lang.String sourcePath = source.getAbsolutePath();
        try {
            try {
                android.system.Os.link(path, sourcePath);
                android.system.Os.chmod(sourcePath, com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED);
            } catch (android.system.ErrnoException e) {
                e.rethrowAsIOException();
            }
            if (!android.os.SELinux.restorecon(source)) {
                throw new java.io.IOException("Can't relabel file: " + source);
            }
        } catch (java.io.IOException e2) {
            try {
                android.system.Os.unlink(sourcePath);
            } catch (java.lang.Exception e3) {
                android.util.Slog.d(TAG, "Failed to unlink session file: " + sourcePath);
            }
            java.lang.Exception ignored = android.util.ExceptionUtils.wrap(e2);
            throw ignored;
        }
    }

    private android.os.ParcelFileDescriptor openTargetInternal(java.lang.String path, int flags, int mode) throws java.io.IOException, android.system.ErrnoException {
        java.io.FileDescriptor fd = android.system.Os.open(path, flags, mode);
        return new android.os.ParcelFileDescriptor(fd);
    }

    private android.os.ParcelFileDescriptor createRevocableFdInternal(android.os.RevocableFileDescriptor fd, android.os.ParcelFileDescriptor pfd) throws java.io.IOException {
        int releasedFdInt = pfd.detachFd();
        java.io.FileDescriptor releasedFd = new java.io.FileDescriptor();
        releasedFd.setInt$(releasedFdInt);
        fd.init(this.mContext, releasedFd);
        return fd.getRevocableFileDescriptor();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.os.ParcelFileDescriptor doWriteInternal(java.lang.String name, long offsetBytes, long lengthBytes, android.os.ParcelFileDescriptor incomingFd) throws java.lang.Throwable {
        android.os.RevocableFileDescriptor fd;
        android.os.FileBridge bridge;
        android.os.ParcelFileDescriptor targetPfd;
        synchronized (this.mLock) {
            if (android.content.pm.PackageInstaller.ENABLE_REVOCABLE_FD) {
                android.os.RevocableFileDescriptor fd2 = new android.os.RevocableFileDescriptor();
                this.mFds.add(fd2);
                fd = fd2;
                bridge = null;
            } else {
                android.os.FileBridge bridge2 = new android.os.FileBridge();
                this.mBridges.add(bridge2);
                fd = null;
                bridge = bridge2;
            }
        }
        try {
            if (!android.os.FileUtils.isValidExtFilename(name)) {
                throw new java.lang.IllegalArgumentException("Invalid name: " + name);
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                java.io.File target = new java.io.File(this.stageDir, name);
                android.os.Binder.restoreCallingIdentity(identity);
                this.mPackageInstallerSessionExt.beforeOpenInDoWriteInternal(target);
                int mode = name.equals(com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME) ? 416 : com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED;
                android.os.ParcelFileDescriptor targetPfd2 = openTargetInternal(target.getAbsolutePath(), android.system.OsConstants.O_CREAT | android.system.OsConstants.O_WRONLY, mode);
                android.system.Os.chmod(target.getAbsolutePath(), mode);
                if (this.stageDir != null && lengthBytes > 0) {
                    ((android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class)).allocateBytes(targetPfd2.getFileDescriptor(), lengthBytes, com.android.internal.content.InstallLocationUtils.translateAllocateFlags(this.params.installFlags));
                }
                if (offsetBytes > 0) {
                    android.system.Os.lseek(targetPfd2.getFileDescriptor(), offsetBytes, android.system.OsConstants.SEEK_SET);
                }
                if (incomingFd == null) {
                    if (android.content.pm.PackageInstaller.ENABLE_REVOCABLE_FD) {
                        return createRevocableFdInternal(fd, targetPfd2);
                    }
                    bridge.setTargetFile(targetPfd2);
                    bridge.start();
                    return bridge.getClientSocket();
                }
                try {
                    final android.system.Int64Ref last = new android.system.Int64Ref(0L);
                    targetPfd = targetPfd2;
                    try {
                        android.os.FileUtils.copy(incomingFd.getFileDescriptor(), targetPfd2.getFileDescriptor(), lengthBytes, null, new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), new android.os.FileUtils.ProgressListener() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda11
                            @Override // android.os.FileUtils.ProgressListener
                            public final void onProgress(long j) {
                                this.f$0.lambda$doWriteInternal$0(last, j);
                            }
                        });
                        libcore.io.IoUtils.closeQuietly(targetPfd);
                        libcore.io.IoUtils.closeQuietly(incomingFd);
                        synchronized (this.mLock) {
                            try {
                                if (android.content.pm.PackageInstaller.ENABLE_REVOCABLE_FD) {
                                    this.mFds.remove(fd);
                                } else {
                                    bridge.forceClose();
                                    this.mBridges.remove(bridge);
                                }
                            } catch (java.lang.Throwable th) {
                                throw th;
                            }
                        }
                        return null;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        libcore.io.IoUtils.closeQuietly(targetPfd);
                        libcore.io.IoUtils.closeQuietly(incomingFd);
                        synchronized (this.mLock) {
                            if (android.content.pm.PackageInstaller.ENABLE_REVOCABLE_FD) {
                                this.mFds.remove(fd);
                            } else {
                                bridge.forceClose();
                                this.mBridges.remove(bridge);
                            }
                        }
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    targetPfd = targetPfd2;
                }
            } catch (java.lang.Throwable th4) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th4;
            }
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doWriteInternal$0(android.system.Int64Ref last, long progress) {
        if (this.params.sizeBytes > 0) {
            long delta = progress - last.value;
            last.value = progress;
            synchronized (this.mProgressLock) {
                setClientProgressLocked(this.mClientProgress + (delta / this.params.sizeBytes));
            }
        }
    }

    public android.os.ParcelFileDescriptor openRead(java.lang.String name) {
        android.os.ParcelFileDescriptor parcelFileDescriptorOpenReadInternalLocked;
        if (isDataLoaderInstallation()) {
            throw new java.lang.IllegalStateException("Cannot read regular files in a data loader installation session.");
        }
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("openRead");
            try {
                parcelFileDescriptorOpenReadInternalLocked = openReadInternalLocked(name);
            } catch (java.io.IOException e) {
                throw android.util.ExceptionUtils.wrap(e);
            }
        }
        return parcelFileDescriptorOpenReadInternalLocked;
    }

    private android.os.ParcelFileDescriptor openReadInternalLocked(java.lang.String name) throws java.io.IOException {
        try {
            if (!android.os.FileUtils.isValidExtFilename(name)) {
                throw new java.lang.IllegalArgumentException("Invalid name: " + name);
            }
            java.io.File target = new java.io.File(this.stageDir, name);
            java.io.FileDescriptor targetFd = android.system.Os.open(target.getAbsolutePath(), android.system.OsConstants.O_RDONLY, 0);
            return new android.os.ParcelFileDescriptor(targetFd);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    private void assertCallerIsOwnerRootOrVerifier() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid == 0 || callingUid == this.mInstallerUid) {
            return;
        }
        if (isSealed() && this.mContext.checkCallingOrSelfPermission("android.permission.PACKAGE_VERIFICATION_AGENT") == 0) {
        } else {
            throw new java.lang.SecurityException("Session does not belong to uid " + callingUid);
        }
    }

    private void assertCallerIsOwnerOrRoot() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 0 && callingUid != this.mInstallerUid) {
            throw new java.lang.SecurityException("Session does not belong to uid " + callingUid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertCallerIsOwnerOrRootOrSystem() {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 0 && callingUid != this.mInstallerUid && callingUid != 1000) {
            throw new java.lang.SecurityException("Session does not belong to uid " + callingUid);
        }
    }

    private void assertNoWriteFileTransfersOpenLocked() {
        for (android.os.RevocableFileDescriptor fd : this.mFds) {
            if (!fd.isRevoked()) {
                throw new java.lang.SecurityException("Files still open");
            }
        }
        for (android.os.FileBridge bridge : this.mBridges) {
            if (!bridge.isClosed()) {
                throw new java.lang.SecurityException("Files still open");
            }
        }
    }

    public void commit(android.content.IntentSender statusReceiver, boolean forTransfer) {
        assertNotChild("commit");
        this.mPackageInstallerSessionExt.beforeSessionCommit(this);
        this.mPackageInstallerSessionSocExt.boostBeforeCommit();
        boolean throwsExceptionCommitImmutableCheck = android.app.compat.CompatChanges.isChangeEnabled(THROW_EXCEPTION_COMMIT_WITH_IMMUTABLE_PENDING_INTENT, android.os.Binder.getCallingUid());
        if (throwsExceptionCommitImmutableCheck && statusReceiver.isImmutable()) {
            throw new java.lang.IllegalArgumentException("The commit() status receiver should come from a mutable PendingIntent");
        }
        if (!markAsSealed(statusReceiver, forTransfer)) {
            return;
        }
        if (isMultiPackage()) {
            synchronized (this.mLock) {
                boolean sealFailed = false;
                for (int i = this.mChildSessions.size() - 1; i >= 0; i--) {
                    if (!this.mChildSessions.valueAt(i).markAsSealed(null, forTransfer)) {
                        sealFailed = true;
                    }
                }
                if (sealFailed) {
                    return;
                }
            }
        }
        synchronized (this.mLock) {
            if (this.mHasAppMetadataFile) {
                java.io.File appMetadataFile = getStagedAppMetadataFile();
                long sizeLimit = getAppMetadataSizeLimit();
                if (appMetadataFile.length() > sizeLimit) {
                    appMetadataFile.delete();
                    this.mHasAppMetadataFile = false;
                    throw new java.lang.IllegalArgumentException("App metadata size exceeds the maximum allowed limit of " + sizeLimit);
                }
                if (isIncrementalInstallation()) {
                    appMetadataFile.renameTo(getTmpAppMetadataFile());
                }
            }
        }
        dispatchSessionSealed();
    }

    public void seal() {
        assertNotChild("seal");
        assertCallerIsOwnerOrRoot();
        try {
            sealInternal();
            for (com.android.server.pm.PackageInstallerSession child : getChildSessions()) {
                child.sealInternal();
            }
        } catch (com.android.server.pm.PackageManagerException e) {
            throw new java.lang.IllegalStateException("Package is not valid", e);
        }
    }

    private void sealInternal() throws com.android.server.pm.PackageManagerException {
        synchronized (this.mLock) {
            sealLocked();
        }
    }

    public java.util.List<java.lang.String> fetchPackageNames() {
        assertNotChild("fetchPackageNames");
        assertCallerIsOwnerOrRoot();
        java.util.List<com.android.server.pm.PackageInstallerSession> sessions = getSelfOrChildSessions();
        java.util.ArrayList<java.lang.String> result = new java.util.ArrayList<>(sessions.size());
        for (com.android.server.pm.PackageInstallerSession s : sessions) {
            result.add(s.fetchPackageName());
        }
        return result;
    }

    private java.lang.String fetchPackageName() {
        java.lang.String packageName;
        assertSealed("fetchPackageName");
        synchronized (this.mLock) {
            android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
            java.util.List<java.io.File> addedFiles = getAddedApksLocked();
            for (java.io.File addedFile : addedFiles) {
                android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ApkLite> result = android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input.reset(), addedFile, 0);
                if (result.isError()) {
                    throw new java.lang.IllegalStateException("Can't parse package for session=" + this.sessionId, result.getException());
                }
                android.content.pm.parsing.ApkLite apk = (android.content.pm.parsing.ApkLite) result.getResult();
                packageName = apk.getPackageName();
                if (packageName != null) {
                }
            }
            throw new java.lang.IllegalStateException("Can't fetch package name for session=" + this.sessionId);
        }
        return packageName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSessionSealed() {
        this.mHandler.obtainMessage(1).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSessionSealed() {
        assertSealed("dispatchSessionSealed");
        this.mCallback.onSessionSealedBlocking(this);
        dispatchStreamValidateAndCommit();
    }

    private void dispatchStreamValidateAndCommit() {
        this.mHandler.obtainMessage(2).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleStreamValidateAndCommit() {
        this.mPackageInstallerSessionExt.beforeHandleStreamValidateAndCommit();
        try {
            this.mPackageInstallerSessionExt.checkMainlineLimited(this);
            boolean allSessionsReady = true;
            for (com.android.server.pm.PackageInstallerSession child : getChildSessions()) {
                allSessionsReady &= child.streamValidateAndCommit();
            }
            if (allSessionsReady && streamValidateAndCommit()) {
                this.mHandler.obtainMessage(3).sendToTarget();
            }
        } catch (com.android.server.pm.PackageManagerException e) {
            java.lang.String msg = android.util.ExceptionUtils.getCompleteMessage(e);
            destroy(msg);
            dispatchSessionFinished(e.error, msg, null);
            maybeFinishChildSessions(e.error, msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePreapprovalRequest() {
        if (sendPendingUserActionIntentIfNeeded(true)) {
            return;
        }
        dispatchSessionPreapproved();
    }

    private final class FileSystemConnector extends android.content.pm.IPackageInstallerSessionFileSystemConnector.Stub {
        final java.util.Set<java.lang.String> mAddedFiles = new android.util.ArraySet();

        FileSystemConnector(java.util.List<android.content.pm.InstallationFileParcel> addedFiles) {
            for (android.content.pm.InstallationFileParcel file : addedFiles) {
                this.mAddedFiles.add(file.name);
            }
        }

        public void writeData(java.lang.String name, long offsetBytes, long lengthBytes, android.os.ParcelFileDescriptor incomingFd) throws java.lang.Throwable {
            if (incomingFd == null) {
                throw new java.lang.IllegalArgumentException("incomingFd can't be null");
            }
            if (!this.mAddedFiles.contains(name)) {
                throw new java.lang.SecurityException("File name is not in the list of added files.");
            }
            try {
                com.android.server.pm.PackageInstallerSession.this.doWriteInternal(name, offsetBytes, lengthBytes, incomingFd);
            } catch (java.io.IOException e) {
                throw android.util.ExceptionUtils.wrap(e);
            }
        }
    }

    private static boolean isSecureFrpInstallAllowed(android.content.Context context, int callingUid) {
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        java.lang.String[] systemInstaller = pmi.getKnownPackageNames(2, 0);
        com.android.server.pm.pkg.AndroidPackage callingInstaller = pmi.getPackage(callingUid);
        if (callingInstaller == null || !sStaticExt.secureFrpWhiteList(callingInstaller.getPackageName())) {
            return (callingInstaller == null || !com.android.internal.util.ArrayUtils.contains(systemInstaller, callingInstaller.getPackageName())) && context.checkCallingOrSelfPermission("android.permission.INSTALL_PACKAGES") == 0;
        }
        return true;
    }

    private boolean isInstallationAllowed(com.android.server.pm.pkg.PackageStateInternal psi) {
        if (psi == null || psi.getPkg() == null || psi.getPkg().isUpdatableSystem()) {
            return true;
        }
        if (this.mOriginalInstallerUid == 0) {
            android.util.Slog.w(TAG, "Overriding updatableSystem because the installer is root: " + psi.getPackageName());
            return true;
        }
        return false;
    }

    private static boolean isArchivedInstallationAllowed(com.android.server.pm.pkg.PackageStateInternal psi) {
        if (psi == null) {
            return true;
        }
        return false;
    }

    private static boolean isIncrementalInstallationAllowed(com.android.server.pm.pkg.PackageStateInternal psi) {
        if (psi == null || psi.getPkg() == null) {
            return true;
        }
        return (psi.isSystem() || psi.isUpdatedSystemApp()) ? false : true;
    }

    private boolean markAsSealed(android.content.IntentSender statusReceiver, boolean forTransfer) {
        boolean isSecureFrpEnabled;
        com.android.internal.util.Preconditions.checkState(statusReceiver != null || hasParentSessionId(), "statusReceiver can't be null for the root session");
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotDestroyedLocked("commit of session " + this.sessionId);
            assertNoWriteFileTransfersOpenLocked();
            if (!android.security.Flags.frpEnforcement()) {
                isSecureFrpEnabled = android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "secure_frp_mode", 0) == 1;
            } else {
                android.service.persistentdata.PersistentDataBlockManager pdbManager = (android.service.persistentdata.PersistentDataBlockManager) this.mContext.getSystemService(android.service.persistentdata.PersistentDataBlockManager.class);
                if (pdbManager == null) {
                    isSecureFrpEnabled = false;
                } else {
                    isSecureFrpEnabled = pdbManager.isFactoryResetProtectionActive();
                }
            }
            if (isSecureFrpEnabled && !isSecureFrpInstallAllowed(this.mContext, android.os.Binder.getCallingUid())) {
                throw new java.lang.SecurityException("Can't install packages while in secure FRP");
            }
            if (forTransfer) {
                this.mContext.enforceCallingOrSelfPermission("android.permission.INSTALL_PACKAGES", null);
                if (this.mInstallerUid == this.mOriginalInstallerUid) {
                    throw new java.lang.IllegalArgumentException("Session has not been transferred");
                }
            } else if (this.mInstallerUid != this.mOriginalInstallerUid) {
                throw new java.lang.IllegalArgumentException("Session has been transferred");
            }
            setRemoteStatusReceiver(statusReceiver);
            if (this.mSealed) {
                return true;
            }
            try {
                sealLocked();
                return true;
            } catch (com.android.server.pm.PackageManagerException e) {
                return false;
            }
        }
    }

    private boolean streamValidateAndCommit() throws com.android.server.pm.PackageManagerException {
        try {
            synchronized (this.mLock) {
                if (isCommitted()) {
                    return true;
                }
                if (!this.params.isMultiPackage) {
                    if (!prepareDataLoaderLocked()) {
                        return false;
                    }
                    if (isApexSession()) {
                        validateApexInstallLocked();
                    } else {
                        validateApkInstallLocked();
                    }
                }
                if (this.mDestroyed) {
                    throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session destroyed");
                }
                if (!isIncrementalInstallation()) {
                    synchronized (this.mProgressLock) {
                        this.mClientProgress = 1.0f;
                        computeProgressLocked(true);
                    }
                }
                this.mActiveCount.incrementAndGet();
                if (!this.mCommitted.compareAndSet(false, true)) {
                    throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, android.text.TextUtils.formatSimple("The mCommitted of session %d should be false originally", new java.lang.Object[]{java.lang.Integer.valueOf(this.sessionId)}));
                }
                this.committedMillis = java.lang.System.currentTimeMillis();
                return true;
            }
        } catch (com.android.server.pm.PackageManagerException e) {
            throw e;
        } catch (java.lang.Throwable e2) {
            throw new com.android.server.pm.PackageManagerException(e2);
        }
    }

    private java.util.List<com.android.server.pm.PackageInstallerSession> getChildSessionsLocked() {
        java.util.List<com.android.server.pm.PackageInstallerSession> childSessions = java.util.Collections.EMPTY_LIST;
        if (isMultiPackage()) {
            int size = this.mChildSessions.size();
            childSessions = new java.util.ArrayList(size);
            for (int i = 0; i < size; i++) {
                childSessions.add(this.mChildSessions.valueAt(i));
            }
        }
        return childSessions;
    }

    java.util.List<com.android.server.pm.PackageInstallerSession> getChildSessions() {
        java.util.List<com.android.server.pm.PackageInstallerSession> childSessionsLocked;
        synchronized (this.mLock) {
            childSessionsLocked = getChildSessionsLocked();
        }
        return childSessionsLocked;
    }

    private java.util.List<com.android.server.pm.PackageInstallerSession> getSelfOrChildSessions() {
        return isMultiPackage() ? getChildSessions() : java.util.Collections.singletonList(this);
    }

    private void sealLocked() throws com.android.server.pm.PackageManagerException {
        try {
            assertNoWriteFileTransfersOpenLocked();
            assertPreparedAndNotDestroyedLocked("sealing of session " + this.sessionId);
            this.mSealed = true;
        } catch (java.lang.Throwable e) {
            throw onSessionValidationFailure(new com.android.server.pm.PackageManagerException(e));
        }
    }

    private com.android.server.pm.PackageManagerException onSessionValidationFailure(com.android.server.pm.PackageManagerException e) {
        onSessionValidationFailure(e.error, android.util.ExceptionUtils.getCompleteMessage(e));
        return e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSessionValidationFailure(int error, java.lang.String detailMessage) {
        destroyInternal("Failed to validate session, error: " + error + ", " + detailMessage);
        dispatchSessionFinished(error, detailMessage, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSessionVerificationFailure(int error, java.lang.String msg) {
        android.util.Slog.e(TAG, "Failed to verify session " + this.sessionId);
        dispatchSessionFinished(error, msg, null);
        maybeFinishChildSessions(error, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSystemDataLoaderUnrecoverable() {
        final java.lang.String packageName = getPackageName();
        if (android.text.TextUtils.isEmpty(packageName)) {
            return;
        }
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onSystemDataLoaderUnrecoverable$1(packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemDataLoaderUnrecoverable$1(java.lang.String packageName) {
        if (this.mPm.deletePackageX(packageName, -1L, 0, 2, true) != 1) {
            android.util.Slog.e(TAG, "Failed to uninstall package with failed dataloader: " + packageName);
        }
    }

    void onAfterSessionRead(android.util.SparseArray<com.android.server.pm.PackageInstallerSession> allSessions) {
        com.android.server.pm.PackageInstallerSession root;
        synchronized (this.mLock) {
            for (int i = this.mChildSessions.size() - 1; i >= 0; i--) {
                int childSessionId = this.mChildSessions.keyAt(i);
                com.android.server.pm.PackageInstallerSession childSession = allSessions.get(childSessionId);
                if (childSession != null) {
                    this.mChildSessions.setValueAt(i, childSession);
                } else {
                    android.util.Slog.e(TAG, "Child session not existed: " + childSessionId);
                    this.mChildSessions.removeAt(i);
                }
            }
            if (!this.mShouldBeSealed || isStagedAndInTerminalState()) {
                return;
            }
            try {
                sealLocked();
            } catch (com.android.server.pm.PackageManagerException e) {
                android.util.Slog.e(TAG, "Package not valid", e);
            }
            if (!isMultiPackage() && isStaged() && isCommitted()) {
                if (hasParentSessionId()) {
                    root = allSessions.get(getParentSessionId());
                } else {
                    root = this;
                }
                if (root != null && !root.isStagedAndInTerminalState()) {
                    if (isApexSession()) {
                        validateApexInstallLocked();
                    } else {
                        validateApkInstallLocked();
                    }
                }
            }
        }
    }

    public void markUpdated() {
        synchronized (this.mLock) {
            this.updatedMillis = java.lang.System.currentTimeMillis();
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    public void transfer(java.lang.String packageName) throws android.os.ParcelableException {
        com.android.internal.util.Preconditions.checkArgument(!android.text.TextUtils.isEmpty(packageName));
        com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
        android.content.pm.ApplicationInfo newOwnerAppInfo = snapshot.getApplicationInfo(packageName, 0L, this.userId);
        if (newOwnerAppInfo == null) {
            throw new android.os.ParcelableException(new android.content.pm.PackageManager.NameNotFoundException(packageName));
        }
        if (snapshot.checkUidPermission("android.permission.INSTALL_PACKAGES", newOwnerAppInfo.uid) != 0) {
            throw new java.lang.SecurityException("Destination package " + packageName + " does not have the android.permission.INSTALL_PACKAGES permission");
        }
        if (!this.params.areHiddenOptionsSet()) {
            throw new java.lang.SecurityException("Can only transfer sessions that use public options");
        }
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("transfer");
            try {
                sealLocked();
                this.mInstallerUid = newOwnerAppInfo.uid;
                this.mInstallSource = com.android.server.pm.InstallSource.create(packageName, null, packageName, this.mInstallerUid, packageName, null, this.params.packageSource);
            } catch (com.android.server.pm.PackageManagerException e) {
                throw new java.lang.IllegalStateException("Package is not valid", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean checkUserActionRequirement(com.android.server.pm.PackageInstallerSession session, android.content.IntentSender target) {
        if (session.isMultiPackage()) {
            return false;
        }
        int userActionRequirement = session.computeUserActionRequirement();
        session.updateUserActionRequirement(userActionRequirement);
        if (userActionRequirement == 1 || userActionRequirement == 3) {
            session.sendPendingUserActionIntent(target);
            return true;
        }
        if (!session.isApexSession() && userActionRequirement == 2) {
            if (!isTargetSdkConditionSatisfied(session)) {
                session.sendPendingUserActionIntent(target);
                return true;
            }
            if (session.params.requireUserAction == 2) {
                if (!session.mSilentUpdatePolicy.isSilentUpdateAllowed(session.getInstallerPackageName(), session.getPackageName())) {
                    session.sendPendingUserActionIntent(target);
                    return true;
                }
                session.mSilentUpdatePolicy.track(session.getInstallerPackageName(), session.getPackageName());
            }
        }
        return false;
    }

    private static boolean isTargetSdkConditionSatisfied(com.android.server.pm.PackageInstallerSession session) {
        int validatedTargetSdk;
        java.lang.String packageName;
        synchronized (session.mLock) {
            validatedTargetSdk = session.mValidatedTargetSdk;
            packageName = session.mPackageName;
        }
        android.content.pm.ApplicationInfo appInfo = new android.content.pm.ApplicationInfo();
        appInfo.packageName = packageName;
        appInfo.targetSdkVersion = validatedTargetSdk;
        com.android.internal.compat.IPlatformCompat platformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));
        if (validatedTargetSdk == Integer.MAX_VALUE) {
            return false;
        }
        try {
            return platformCompat.isChangeEnabled(SILENT_INSTALL_ALLOWED, appInfo);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failed to get a response from PLATFORM_COMPAT_SERVICE", e);
            return false;
        }
    }

    private static int userActionRequirementToReason(int requirement) {
        switch (requirement) {
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    private boolean sendPendingUserActionIntentIfNeeded(boolean forPreapproval) {
        if (isCommitted()) {
            assertNotChild("PackageInstallerSession#sendPendingUserActionIntentIfNeeded");
        }
        final android.content.IntentSender statusReceiver = forPreapproval ? getPreapprovalRemoteStatusReceiver() : getRemoteStatusReceiver();
        return sessionContains(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.pm.PackageInstallerSession.checkUserActionRequirement((com.android.server.pm.PackageInstallerSession) obj, statusReceiver);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInstall() {
        if (isInstallerDeviceOwnerOrAffiliatedProfileOwner()) {
            android.app.admin.DevicePolicyEventLogger.createEvent(112).setAdmin(getInstallSource().mInstallerPackageName).write();
        }
        boolean wasUserActionIntentSent = sendPendingUserActionIntentIfNeeded(false);
        if (this.mUserActionRequired == null) {
            this.mUserActionRequired = java.lang.Boolean.valueOf(wasUserActionIntentSent);
        }
        if (wasUserActionIntentSent) {
            deactivate();
            return;
        }
        if (this.mUserActionRequired.booleanValue()) {
            activate();
        }
        if (this.mVerificationInProgress) {
            android.util.Slog.w(TAG, "Verification is already in progress for session " + this.sessionId);
            return;
        }
        this.mVerificationInProgress = true;
        if (this.params.isStaged) {
            this.mStagedSession.verifySession();
        } else {
            verify();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verify() {
        try {
            java.util.List<com.android.server.pm.PackageInstallerSession> children = getChildSessions();
            if (isMultiPackage()) {
                for (com.android.server.pm.PackageInstallerSession child : children) {
                    child.prepareInheritedFiles();
                    child.parseApkAndExtractNativeLibraries();
                }
            } else {
                prepareInheritedFiles();
                parseApkAndExtractNativeLibraries();
            }
            verifyNonStaged();
        } catch (com.android.server.pm.PackageManagerException e) {
            java.lang.String completeMsg = android.util.ExceptionUtils.getCompleteMessage(e);
            java.lang.String errorMsg = android.content.pm.PackageManager.installStatusToString(e.error, completeMsg);
            setSessionFailed(e.error, errorMsg);
            onSessionVerificationFailure(e.error, errorMsg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.IntentSender getRemoteStatusReceiver() {
        android.content.IntentSender intentSender;
        synchronized (this.mLock) {
            intentSender = this.mRemoteStatusReceiver;
        }
        return intentSender;
    }

    private void setRemoteStatusReceiver(android.content.IntentSender remoteStatusReceiver) {
        synchronized (this.mLock) {
            this.mRemoteStatusReceiver = remoteStatusReceiver;
        }
    }

    private android.content.IntentSender getPreapprovalRemoteStatusReceiver() {
        android.content.IntentSender intentSender;
        synchronized (this.mLock) {
            intentSender = this.mPreapprovalRemoteStatusReceiver;
        }
        return intentSender;
    }

    private void setPreapprovalRemoteStatusReceiver(android.content.IntentSender remoteStatusReceiver) {
        synchronized (this.mLock) {
            this.mPreapprovalRemoteStatusReceiver = remoteStatusReceiver;
        }
    }

    private void prepareInheritedFiles() throws com.android.server.pm.PackageManagerException {
        if (isApexSession() || this.params.mode != 2) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mStageDirInUse) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session files in use");
            }
            if (this.mDestroyed) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session destroyed");
            }
            if (!this.mSealed) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session not sealed");
            }
            try {
                java.util.List<java.io.File> fromFiles = this.mResolvedInheritedFiles;
                java.io.File toDir = this.stageDir;
                java.lang.String tempPackageName = toDir.getName();
                android.util.Slog.d(TAG, "Inherited files: " + this.mResolvedInheritedFiles);
                if (!this.mResolvedInheritedFiles.isEmpty() && this.mInheritedFilesBase == null) {
                    throw new java.lang.IllegalStateException("mInheritedFilesBase == null");
                }
                if (isLinkPossible(fromFiles, toDir)) {
                    if (!this.mResolvedInstructionSets.isEmpty()) {
                        java.io.File oatDir = new java.io.File(toDir, "oat");
                        createOatDirs(tempPackageName, this.mResolvedInstructionSets, oatDir);
                    }
                    if (!this.mResolvedNativeLibPaths.isEmpty()) {
                        for (java.lang.String libPath : this.mResolvedNativeLibPaths) {
                            int splitIndex = libPath.lastIndexOf(47);
                            if (splitIndex < 0 || splitIndex >= libPath.length() - 1) {
                                android.util.Slog.e(TAG, "Skipping native library creation for linking due to invalid path: " + libPath);
                            } else {
                                java.lang.String libDirPath = libPath.substring(1, splitIndex);
                                java.io.File libDir = new java.io.File(toDir, libDirPath);
                                if (!libDir.exists()) {
                                    com.android.internal.content.NativeLibraryHelper.createNativeLibrarySubdir(libDir);
                                }
                                java.lang.String archDirPath = libPath.substring(splitIndex + 1);
                                com.android.internal.content.NativeLibraryHelper.createNativeLibrarySubdir(new java.io.File(libDir, archDirPath));
                            }
                        }
                    }
                    linkFiles(tempPackageName, fromFiles, toDir, this.mInheritedFilesBase);
                } else {
                    copyFiles(fromFiles, toDir);
                }
            } catch (java.io.IOException e) {
                throw new com.android.server.pm.PackageManagerException(-4, "Failed to inherit existing install", e);
            }
        }
    }

    private void markStageDirInUseLocked() throws com.android.server.pm.PackageManagerException {
        if (this.mDestroyed) {
            throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session destroyed");
        }
        this.mStageDirInUse = true;
        this.mMarkStageDirUserNoConsume = true;
    }

    private void parseApkAndExtractNativeLibraries() throws com.android.server.pm.PackageManagerException {
        android.content.pm.parsing.PackageLite result;
        synchronized (this.mLock) {
            if (this.mStageDirInUse) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session files in use");
            }
            if (this.mDestroyed) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session destroyed");
            }
            if (!this.mSealed) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session not sealed");
            }
            if (this.mPackageName == null) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session no package name");
            }
            if (this.mSigningDetails == null) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session no signing data");
            }
            if (this.mResolvedBaseFile == null) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session no resolved base file");
            }
            if (!isApexSession()) {
                result = getOrParsePackageLiteLocked(this.stageDir, 0);
            } else {
                result = getOrParsePackageLiteLocked(this.mResolvedBaseFile, 0);
            }
            if (result != null) {
                this.mPackageLite = result;
                if (!isApexSession()) {
                    synchronized (this.mProgressLock) {
                        this.mInternalProgress = 0.5f;
                        computeProgressLocked(true);
                    }
                    if (!this.mPackageInstallerSessionExt.hasPreExtractNativeLibsFinished()) {
                        extractNativeLibraries(this.mPackageLite, this.stageDir, this.params.abiOverride, mayInheritNativeLibs());
                    }
                    if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                        android.util.Slog.d(TAG, "extractNativeLibraries end");
                    }
                }
            }
        }
    }

    private void verifyNonStaged() throws com.android.server.pm.PackageManagerException {
        synchronized (this.mLock) {
            markStageDirInUseLocked();
        }
        this.mSessionProvider.getSessionVerifier().verify(this, new com.android.server.pm.PackageSessionVerifier.Callback() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda6
            @Override // com.android.server.pm.PackageSessionVerifier.Callback
            public final void onResult(int i, java.lang.String str) {
                this.f$0.lambda$verifyNonStaged$4(i, str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyNonStaged$4(final int error, final java.lang.String msg) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$verifyNonStaged$3(error, msg);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyNonStaged$3(int error, java.lang.String msg) {
        if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
            android.util.Slog.d(TAG, "SessionVerifier verify callback: " + error + ", " + msg);
        }
        if (dispatchPendingAbandonCallback()) {
            return;
        }
        if (error == 1) {
            onVerificationComplete();
        } else {
            onSessionVerificationFailure(error, msg);
        }
    }

    private static class InstallResult {
        public final android.os.Bundle extras;
        public final com.android.server.pm.PackageInstallerSession session;

        InstallResult(com.android.server.pm.PackageInstallerSession session, android.os.Bundle extras) {
            this.session = session;
            this.extras = extras;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.concurrent.CompletableFuture<java.lang.Void> install() {
        final java.util.List<java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult>> futures = installNonStaged();
        java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult>[] arr = new java.util.concurrent.CompletableFuture[futures.size()];
        return java.util.concurrent.CompletableFuture.allOf((java.util.concurrent.CompletableFuture[]) futures.toArray(arr)).whenComplete(new java.util.function.BiConsumer() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda9
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$install$5(futures, (java.lang.Void) obj, (java.lang.Throwable) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$install$5(java.util.List futures, java.lang.Void r, java.lang.Throwable t) {
        if (t == null) {
            setSessionApplied();
            java.util.ArrayList<java.lang.String> multiPackageWarnings = new java.util.ArrayList<>();
            if (isMultiPackage()) {
                java.util.Iterator it = futures.iterator();
                while (it.hasNext()) {
                    java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult> f = (java.util.concurrent.CompletableFuture) it.next();
                    com.android.server.pm.PackageInstallerSession.InstallResult result = f.join();
                    if (result.session != this && result.extras != null) {
                        java.util.ArrayList<java.lang.String> childWarnings = result.extras.getStringArrayList("android.content.pm.extra.WARNINGS");
                        if (!com.android.internal.util.ArrayUtils.isEmpty(childWarnings)) {
                            multiPackageWarnings.addAll(childWarnings);
                        }
                    }
                }
            }
            java.util.Iterator it2 = futures.iterator();
            while (it2.hasNext()) {
                java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult> f2 = (java.util.concurrent.CompletableFuture) it2.next();
                com.android.server.pm.PackageInstallerSession.InstallResult result2 = f2.join();
                android.os.Bundle extras = result2.extras;
                if (isMultiPackage() && result2.session == this && !multiPackageWarnings.isEmpty()) {
                    if (extras == null) {
                        extras = new android.os.Bundle();
                    }
                    extras.putStringArrayList("android.content.pm.extra.WARNINGS", multiPackageWarnings);
                }
                result2.session.dispatchSessionFinished(1, "Session installed", extras);
            }
            return;
        }
        com.android.server.pm.PackageManagerException e = (com.android.server.pm.PackageManagerException) t.getCause();
        setSessionFailed(e.error, android.content.pm.PackageManager.installStatusToString(e.error, e.getMessage()));
        dispatchSessionFinished(e.error, e.getMessage(), null);
        maybeFinishChildSessions(e.error, e.getMessage());
    }

    private java.util.List<java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult>> installNonStaged() {
        try {
            java.util.List<java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult>> futures = new java.util.ArrayList<>();
            java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult> future = new java.util.concurrent.CompletableFuture<>();
            futures.add(future);
            com.android.server.pm.InstallingSession installingSession = createInstallingSession(future);
            if (isMultiPackage()) {
                java.util.List<com.android.server.pm.PackageInstallerSession> childSessions = getChildSessions();
                java.util.List<com.android.server.pm.InstallingSession> installingChildSessions = new java.util.ArrayList<>(childSessions.size());
                for (int i = 0; i < childSessions.size(); i++) {
                    com.android.server.pm.PackageInstallerSession session = childSessions.get(i);
                    java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult> future2 = new java.util.concurrent.CompletableFuture<>();
                    futures.add(future2);
                    com.android.server.pm.InstallingSession installingChildSession = session.createInstallingSession(future2);
                    if (installingChildSession != null) {
                        installingChildSessions.add(installingChildSession);
                    }
                }
                if (!installingChildSessions.isEmpty()) {
                    ((com.android.server.pm.InstallingSession) java.util.Objects.requireNonNull(installingSession)).installStage(installingChildSessions);
                }
            } else {
                if (installingSession != null) {
                    installingSession.installStage();
                }
                return futures;
            }
            return futures;
        } catch (com.android.server.pm.PackageManagerException e) {
            java.util.List<java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult>> futures2 = new java.util.ArrayList<>();
            futures2.add(java.util.concurrent.CompletableFuture.failedFuture(e));
            return futures2;
        }
    }

    private void sendPendingUserActionIntent(android.content.IntentSender target) {
        boolean isPreapproval = isPreapprovalRequested() && !isCommitted();
        android.content.Intent intent = new android.content.Intent(isPreapproval ? "android.content.pm.action.CONFIRM_PRE_APPROVAL" : "android.content.pm.action.CONFIRM_INSTALL");
        intent.setPackage(this.mPm.getPackageInstallerPackageName());
        intent.putExtra("android.content.pm.extra.SESSION_ID", this.sessionId);
        sendOnUserActionRequired(this.mContext, target, this.sessionId, intent);
    }

    private void onVerificationComplete() {
        if (isStaged()) {
            this.mPackageInstallerSessionExt.onStagedSessionVerificationComplete(this, this.mStagedSession, this.mPm);
            this.mStagingManager.commitSession(this.mStagedSession);
            sendUpdateToRemoteStatusReceiver(1, "Session staged", null, false);
            return;
        }
        install();
    }

    private com.android.server.pm.InstallingSession createInstallingSession(final java.util.concurrent.CompletableFuture<com.android.server.pm.PackageInstallerSession.InstallResult> future) throws com.android.server.pm.PackageManagerException {
        android.os.UserHandle user;
        com.android.server.pm.InstallingSession installingSession;
        synchronized (this.mLock) {
            if (!this.mSealed) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Session not sealed");
            }
            markStageDirInUseLocked();
        }
        if (isMultiPackage()) {
            future.complete(new com.android.server.pm.PackageInstallerSession.InstallResult(this, null));
        } else if (isApexSession() && this.params.isStaged) {
            future.complete(new com.android.server.pm.PackageInstallerSession.InstallResult(this, null));
            return null;
        }
        android.content.pm.IPackageInstallObserver2.Stub stub = new android.content.pm.IPackageInstallObserver2.Stub() { // from class: com.android.server.pm.PackageInstallerSession.5
            public void onUserActionRequired(android.content.Intent intent) {
                throw new java.lang.IllegalStateException();
            }

            public void onPackageInstalled(java.lang.String basePackageName, int returnCode, java.lang.String msg, android.os.Bundle extras) {
                if (returnCode == 1) {
                    future.complete(new com.android.server.pm.PackageInstallerSession.InstallResult(com.android.server.pm.PackageInstallerSession.this, extras));
                } else {
                    future.completeExceptionally(new com.android.server.pm.PackageManagerException(returnCode, msg));
                }
            }
        };
        if ((this.params.installFlags & 64) != 0) {
            user = android.os.UserHandle.ALL;
        } else {
            user = new android.os.UserHandle(this.userId);
        }
        if (this.params.isStaged) {
            this.params.installFlags |= 2097152;
        }
        if (!isMultiPackage() && !isApexSession()) {
            synchronized (this.mLock) {
                if (this.mPackageLite == null) {
                    android.util.Slog.wtf(TAG, "Session: " + this.sessionId + ". Don't have a valid PackageLite.");
                }
                this.mPackageLite = getOrParsePackageLiteLocked(this.stageDir, 0);
            }
        }
        synchronized (this.mLock) {
            installingSession = new com.android.server.pm.InstallingSession(this.sessionId, this.stageDir, (android.content.pm.IPackageInstallObserver2) stub, this.params, this.mInstallSource, user, this.mSigningDetails, this.mInstallerUid, this.mPackageLite, this.mPreVerifiedDomains, this.mPm, this.mHasAppMetadataFile);
        }
        return installingSession;
    }

    private android.content.pm.parsing.PackageLite getOrParsePackageLiteLocked(java.io.File packageFile, int flags) throws com.android.server.pm.PackageManagerException {
        if (this.mPackageLite != null) {
            return this.mPackageLite;
        }
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.PackageLite> result = android.content.pm.parsing.ApkLiteParseUtils.parsePackageLite(input, packageFile, flags);
        if (result.isError()) {
            throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, result.getErrorMessage(), result.getException());
        }
        return (android.content.pm.parsing.PackageLite) result.getResult();
    }

    private static void maybeRenameFile(java.io.File from, java.io.File to) throws com.android.server.pm.PackageManagerException {
        if (!from.equals(to) && !from.renameTo(to)) {
            throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Could not rename file " + from + " to " + to);
        }
    }

    private void logDataLoaderInstallationSession(int returnCode) {
        int packageUid;
        java.lang.String packageName = getPackageName();
        java.lang.String packageNameToLog = (this.params.installFlags & 32) == 0 ? packageName : "";
        long currentTimestamp = java.lang.System.currentTimeMillis();
        if (returnCode != 1) {
            packageUid = -1;
        } else {
            packageUid = this.mPm.snapshotComputer().getPackageUid(packageName, 0L, this.userId);
        }
        com.android.internal.util.FrameworkStatsLog.write(263, isIncrementalInstallation(), packageNameToLog, currentTimestamp - this.createdMillis, returnCode, getApksSize(packageName), packageUid);
    }

    private long getApksSize(java.lang.String packageName) {
        java.io.File apkDirOrPath;
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        com.android.server.pm.pkg.PackageStateInternal ps = pmi.getPackageStateInternal(packageName);
        if (ps == null || (apkDirOrPath = ps.getPath()) == null) {
            return 0L;
        }
        if (apkDirOrPath.isFile() && apkDirOrPath.getName().toLowerCase().endsWith(".apk")) {
            return apkDirOrPath.length();
        }
        if (!apkDirOrPath.isDirectory()) {
            return 0L;
        }
        java.io.File[] files = apkDirOrPath.listFiles();
        long apksSize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().toLowerCase().endsWith(".apk")) {
                apksSize += files[i].length();
            }
        }
        return apksSize;
    }

    private boolean mayInheritNativeLibs() {
        return android.os.SystemProperties.getBoolean(PROPERTY_NAME_INHERIT_NATIVE, true) && this.params.mode == 2 && (this.params.installFlags & 1) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isApexSession() {
        return (this.params.installFlags & 131072) != 0;
    }

    boolean sessionContains(java.util.function.Predicate<com.android.server.pm.PackageInstallerSession> filter) {
        java.util.List<com.android.server.pm.PackageInstallerSession> childSessions;
        if (!isMultiPackage()) {
            return filter.test(this);
        }
        synchronized (this.mLock) {
            childSessions = getChildSessionsLocked();
        }
        for (com.android.server.pm.PackageInstallerSession child : childSessions) {
            if (filter.test(child)) {
                return true;
            }
        }
        return false;
    }

    static /* synthetic */ boolean lambda$containsApkSession$6(com.android.server.pm.PackageInstallerSession s) {
        return !s.isApexSession();
    }

    boolean containsApkSession() {
        return sessionContains(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.pm.PackageInstallerSession.lambda$containsApkSession$6((com.android.server.pm.PackageInstallerSession) obj);
            }
        });
    }

    private void validateApexInstallLocked() throws com.android.server.pm.PackageManagerException {
        java.lang.String targetName;
        java.util.List<java.io.File> addedFiles = getAddedApksLocked();
        if (addedFiles.isEmpty()) {
            throw new com.android.server.pm.PackageManagerException(-2, android.text.TextUtils.formatSimple("Session: %d. No packages staged in %s", new java.lang.Object[]{java.lang.Integer.valueOf(this.sessionId), this.stageDir.getAbsolutePath()}));
        }
        if (com.android.internal.util.ArrayUtils.size(addedFiles) > 1) {
            throw new com.android.server.pm.PackageManagerException(-2, "Too many files for apex install");
        }
        java.io.File addedFile = addedFiles.get(0);
        java.lang.String sourceName = addedFile.getName();
        if (!sourceName.endsWith(APEX_FILE_EXTENSION)) {
            targetName = sourceName + APEX_FILE_EXTENSION;
        } else {
            targetName = sourceName;
        }
        if (!android.os.FileUtils.isValidExtFilename(targetName)) {
            throw new com.android.server.pm.PackageManagerException(-2, "Invalid filename: " + targetName);
        }
        java.io.File targetFile = new java.io.File(this.stageDir, targetName);
        resolveAndStageFileLocked(addedFile, targetFile, null);
        this.mResolvedBaseFile = targetFile;
        this.mPackageName = null;
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.ApkLite> ret = android.content.pm.parsing.ApkLiteParseUtils.parseApkLite(input.reset(), this.mResolvedBaseFile, 32);
        if (ret.isError()) {
            throw new com.android.server.pm.PackageManagerException(ret.getErrorCode(), ret.getErrorMessage(), ret.getException());
        }
        android.content.pm.parsing.ApkLite apk = (android.content.pm.parsing.ApkLite) ret.getResult();
        if (this.mPackageName == null) {
            this.mPackageName = apk.getPackageName();
            this.mVersionCode = apk.getLongVersionCode();
        }
        this.mSigningDetails = apk.getSigningDetails();
        this.mHasDeviceAdminReceiver = apk.isHasDeviceAdminReceiver();
    }

    private android.content.pm.parsing.PackageLite validateApkInstallLocked() throws java.lang.Throwable {
        boolean existingSplitReplacedOrRemoved;
        boolean z;
        android.content.pm.parsing.PackageLite packageLite;
        java.io.File packageInstallDir;
        java.io.File[] libDirs;
        int i;
        java.util.List<java.lang.String> libDirsToInherit;
        java.util.List<java.io.File> libFilesToInherit;
        java.io.File[] fileArr;
        java.io.File packageInstallDir2;
        int i2;
        java.util.List<java.lang.String> libDirsToInherit2;
        java.util.List<java.io.File> libFilesToInherit2;
        java.io.File[] archSubdirs;
        int i3;
        java.io.File oatDir;
        java.lang.String installerPackageName;
        this.mPackageLite = null;
        this.mPackageName = null;
        this.mVersionCode = -1L;
        this.mSigningDetails = android.content.pm.SigningDetails.UNKNOWN;
        this.mResolvedBaseFile = null;
        this.mResolvedStagedFiles.clear();
        this.mResolvedInheritedFiles.clear();
        android.content.pm.PackageInfo pkgInfo = this.mPm.snapshotComputer().getPackageInfo(this.params.appPackageName, 67108928L, this.userId);
        int i4 = -2;
        if (this.params.mode == 2 && (pkgInfo == null || pkgInfo.applicationInfo == null)) {
            throw new com.android.server.pm.PackageManagerException(-2, "Missing existing base package");
        }
        this.mVerityFoundForApks = com.android.server.pm.PackageManagerServiceUtils.isApkVerityEnabled() && this.params.mode == 2 && com.android.internal.security.VerityUtils.hasFsverity(pkgInfo.applicationInfo.getBaseCodePath()) && new java.io.File(com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(pkgInfo.applicationInfo.getBaseCodePath())).exists();
        java.util.List<java.io.File> removedFiles = getRemovedFilesLocked();
        java.util.List<java.lang.String> removeSplitList = new java.util.ArrayList<>();
        if (!removedFiles.isEmpty()) {
            for (java.io.File removedFile : removedFiles) {
                java.lang.String fileName = removedFile.getName();
                removeSplitList.add(fileName.substring(0, fileName.length() - REMOVE_MARKER_EXTENSION.length()));
            }
        }
        if (android.security.Flags.extendVbChainToUpdatedApk() && !isIncrementalInstallation()) {
            enableFsVerityToAddedApksWithIdsig();
        }
        java.util.List<android.content.pm.parsing.ApkLite> addedFiles = getAddedApkLitesLocked();
        if (addedFiles.isEmpty() && (removeSplitList.size() == 0 || this.mHasAppMetadataFile)) {
            throw new com.android.server.pm.PackageManagerException(-2, android.text.TextUtils.formatSimple("Session: %d. No packages staged in %s", new java.lang.Object[]{java.lang.Integer.valueOf(this.sessionId), this.stageDir.getAbsolutePath()}));
        }
        android.util.ArraySet<java.lang.String> stagedSplits = new android.util.ArraySet<>();
        android.util.ArraySet<java.lang.String> stagedSplitTypes = new android.util.ArraySet<>();
        android.util.ArraySet<java.lang.String> requiredSplitTypes = new android.util.ArraySet<>();
        android.util.ArrayMap<java.lang.String, android.content.pm.parsing.ApkLite> splitApks = new android.util.ArrayMap<>();
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        android.content.pm.parsing.ApkLite baseApk = null;
        for (android.content.pm.parsing.ApkLite apk : addedFiles) {
            if (!stagedSplits.add(apk.getSplitName())) {
                throw new com.android.server.pm.PackageManagerException(-2, "Split " + apk.getSplitName() + " was defined multiple times");
            }
            if (!apk.isUpdatableSystem()) {
                if (this.mOriginalInstallerUid != 0) {
                    throw new com.android.server.pm.PackageManagerException(i4, "Non updatable system package can't be installed or updated");
                }
                android.util.Slog.w(TAG, "Overriding updatableSystem because the installer is root for: " + apk.getPackageName());
            }
            if (this.mPackageName == null) {
                this.mPackageName = apk.getPackageName();
                this.mVersionCode = apk.getLongVersionCode();
            }
            if (this.mSigningDetails == android.content.pm.SigningDetails.UNKNOWN) {
                this.mSigningDetails = apk.getSigningDetails();
            }
            this.mHasDeviceAdminReceiver = apk.isHasDeviceAdminReceiver();
            assertApkConsistentLocked(java.lang.String.valueOf(apk), apk);
            java.lang.String targetName = android.content.pm.parsing.ApkLiteParseUtils.splitNameToFileName(apk);
            if (!android.os.FileUtils.isValidExtFilename(targetName)) {
                throw new com.android.server.pm.PackageManagerException(-2, "Invalid filename: " + targetName);
            }
            if (apk.getInstallLocation() != -1 && (installerPackageName = getInstallerPackageName()) != null && this.params.installLocation != apk.getInstallLocation()) {
                android.util.Slog.wtf(TAG, installerPackageName + " drops manifest attribute android:installLocation in " + targetName + " for " + this.mPackageName);
            }
            java.io.File targetFile = new java.io.File(this.stageDir, targetName);
            if (!isArchivedInstallation()) {
                java.io.File sourceFile = new java.io.File(apk.getPath());
                resolveAndStageFileLocked(sourceFile, targetFile, apk.getSplitName());
            }
            if (apk.getSplitName() == null) {
                this.mResolvedBaseFile = targetFile;
                baseApk = apk;
            } else {
                splitApks.put(apk.getSplitName(), apk);
            }
            com.android.internal.util.CollectionUtils.addAll(requiredSplitTypes, apk.getRequiredSplitTypes());
            com.android.internal.util.CollectionUtils.addAll(stagedSplitTypes, apk.getSplitTypes());
            i4 = -2;
        }
        if (removeSplitList.size() > 0) {
            if (pkgInfo == null) {
                throw new com.android.server.pm.PackageManagerException(-2, "Missing existing base package for " + this.mPackageName);
            }
            for (java.lang.String splitName : removeSplitList) {
                if (!com.android.internal.util.ArrayUtils.contains(pkgInfo.splitNames, splitName)) {
                    throw new com.android.server.pm.PackageManagerException(-2, "Split not found: " + splitName);
                }
            }
            if (this.mPackageName == null) {
                this.mPackageName = pkgInfo.packageName;
                this.mVersionCode = pkgInfo.getLongVersionCode();
            }
            if (this.mSigningDetails == android.content.pm.SigningDetails.UNKNOWN) {
                this.mSigningDetails = unsafeGetCertsWithoutVerification(pkgInfo.applicationInfo.sourceDir);
            }
        }
        android.content.pm.PackageManagerInternal pmi = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        com.android.server.pm.pkg.PackageStateInternal existingPkgSetting = pmi.getPackageStateInternal(this.mPackageName);
        if (!isInstallationAllowed(existingPkgSetting)) {
            throw new com.android.server.pm.PackageManagerException(-116, "Installation of this package is not allowed.");
        }
        if (isArchivedInstallation()) {
            if (!isArchivedInstallationAllowed(existingPkgSetting)) {
                throw new com.android.server.pm.PackageManagerException(-116, "Archived installation of this package is not allowed.");
            }
            if (!this.mPm.mInstallerService.mPackageArchiver.verifySupportsUnarchival(getInstallSource().mInstallerPackageName, this.userId)) {
                throw new com.android.server.pm.PackageManagerException(-116, "Installer has to support unarchival in order to install archived packages.");
            }
        }
        java.io.File stagedAppMetadataFile = isIncrementalInstallation() ? getTmpAppMetadataFile() : getStagedAppMetadataFile();
        if (this.mHasAppMetadataFile && !stagedAppMetadataFile.exists()) {
            throw new com.android.server.pm.PackageManagerException(-116, "App metadata file expected but not found in " + this.stageDir.getAbsolutePath());
        }
        if (isIncrementalInstallation()) {
            if (!isIncrementalInstallationAllowed(existingPkgSetting)) {
                throw new com.android.server.pm.PackageManagerException(-116, "Incremental installation of this package is not allowed.");
            }
            if (this.mHasAppMetadataFile) {
                java.io.File appMetadataFile = getTmpAppMetadataFile();
                android.os.incremental.IncrementalFileStorages incrementalFileStorages = getIncrementalFileStorages();
                try {
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                    try {
                        try {
                            incrementalFileStorages.makeFile(com.android.server.pm.PackageManagerService.APP_METADATA_FILE_NAME, java.nio.file.Files.readAllBytes(appMetadataFile.toPath()), 416);
                        } catch (java.io.IOException e) {
                            e = e;
                            android.util.Slog.e(TAG, "Failed to write app metadata to incremental storage", e);
                        }
                    } catch (java.io.IOException e2) {
                        e = e2;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        appMetadataFile.delete();
                        throw th;
                    }
                } catch (java.io.IOException e3) {
                    e = e3;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
                appMetadataFile.delete();
            }
        }
        if (this.mInstallerUid != this.mOriginalInstallerUid && (android.text.TextUtils.isEmpty(this.mPackageName) || !this.mPackageName.equals(this.mOriginalInstallerPackageName))) {
            throw new com.android.server.pm.PackageManagerException(-23, "Can only transfer sessions that update the original installer");
        }
        if (!this.mChecksums.isEmpty()) {
            throw new com.android.server.pm.PackageManagerException(-116, "Invalid checksum name(s): " + java.lang.String.join(",", this.mChecksums.keySet()));
        }
        if (this.params.mode != 1) {
            android.content.pm.ApplicationInfo appInfo = pkgInfo.applicationInfo;
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.PackageLite> pkgLiteResult = android.content.pm.parsing.ApkLiteParseUtils.parsePackageLite(input.reset(), new java.io.File(appInfo.getCodePath()), 0);
            if (pkgLiteResult.isError()) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, pkgLiteResult.getErrorMessage(), pkgLiteResult.getException());
            }
            android.content.pm.parsing.PackageLite existing = (android.content.pm.parsing.PackageLite) pkgLiteResult.getResult();
            assertPackageConsistentLocked("Existing", existing.getPackageName(), existing.getLongVersionCode());
            android.content.pm.SigningDetails signingDetails = unsafeGetCertsWithoutVerification(existing.getBaseApkPath());
            if (!this.mSigningDetails.signaturesMatchExactly(signingDetails)) {
                throw new com.android.server.pm.PackageManagerException(-2, "Existing signatures are inconsistent");
            }
            if (this.mResolvedBaseFile == null) {
                this.mResolvedBaseFile = new java.io.File(appInfo.getBaseCodePath());
                inheritFileLocked(this.mResolvedBaseFile);
                com.android.internal.util.CollectionUtils.addAll(requiredSplitTypes, existing.getBaseRequiredSplitTypes());
            } else if ((this.params.installFlags & 4096) != 0) {
                android.util.EventLog.writeEvent(1397638484, "219044664");
                this.params.setDontKillApp(false);
            }
            boolean existingSplitReplacedOrRemoved2 = false;
            if (com.android.internal.util.ArrayUtils.isEmpty(existing.getSplitNames())) {
                existingSplitReplacedOrRemoved = false;
            } else {
                for (int i5 = 0; i5 < existing.getSplitNames().length; i5++) {
                    java.lang.String splitName2 = existing.getSplitNames()[i5];
                    boolean existingSplitReplacedOrRemoved3 = existingSplitReplacedOrRemoved2;
                    java.io.File splitFile = new java.io.File(existing.getSplitApkPaths()[i5]);
                    boolean splitRemoved = removeSplitList.contains(splitName2);
                    boolean splitReplaced = stagedSplits.contains(splitName2);
                    if (splitReplaced || splitRemoved) {
                        existingSplitReplacedOrRemoved2 = true;
                    } else {
                        inheritFileLocked(splitFile);
                        com.android.internal.util.CollectionUtils.addAll(requiredSplitTypes, existing.getRequiredSplitTypes()[i5]);
                        com.android.internal.util.CollectionUtils.addAll(stagedSplitTypes, existing.getSplitTypes()[i5]);
                        existingSplitReplacedOrRemoved2 = existingSplitReplacedOrRemoved3;
                    }
                }
                existingSplitReplacedOrRemoved = existingSplitReplacedOrRemoved2;
            }
            if (existingSplitReplacedOrRemoved && (this.params.installFlags & 4096) != 0) {
                this.params.setDontKillApp(false);
            }
            java.io.File packageInstallDir3 = new java.io.File(appInfo.getBaseCodePath()).getParentFile();
            this.mInheritedFilesBase = packageInstallDir3;
            java.io.File oatDir2 = new java.io.File(packageInstallDir3, "oat");
            if (oatDir2.exists() && (archSubdirs = oatDir2.listFiles()) != null && archSubdirs.length > 0) {
                java.lang.String[] instructionSets = com.android.server.pm.InstructionSets.getAllDexCodeInstructionSets();
                int length = archSubdirs.length;
                int i6 = 0;
                while (i6 < length) {
                    java.io.File archSubDir = archSubdirs[i6];
                    java.io.File[] archSubdirs2 = archSubdirs;
                    if (com.android.internal.util.ArrayUtils.contains(instructionSets, archSubDir.getName())) {
                        java.io.File[] files = archSubDir.listFiles();
                        if (files != null) {
                            i3 = length;
                            if (files.length == 0) {
                                oatDir = oatDir2;
                            } else {
                                oatDir = oatDir2;
                                this.mResolvedInstructionSets.add(archSubDir.getName());
                                this.mResolvedInheritedFiles.addAll(java.util.Arrays.asList(files));
                            }
                        } else {
                            i3 = length;
                            oatDir = oatDir2;
                        }
                    } else {
                        i3 = length;
                        oatDir = oatDir2;
                    }
                    i6++;
                    archSubdirs = archSubdirs2;
                    length = i3;
                    oatDir2 = oatDir;
                }
            }
            if (mayInheritNativeLibs() && removeSplitList.isEmpty()) {
                java.io.File[] libDirs2 = {new java.io.File(packageInstallDir3, "lib"), new java.io.File(packageInstallDir3, "lib64")};
                int length2 = libDirs2.length;
                int i7 = 0;
                while (i7 < length2) {
                    java.io.File libDir = libDirs2[i7];
                    if (!libDir.exists()) {
                        packageInstallDir = packageInstallDir3;
                        libDirs = libDirs2;
                        i = length2;
                    } else if (libDir.isDirectory()) {
                        java.util.List<java.lang.String> libDirsToInherit3 = new java.util.ArrayList<>();
                        java.util.List<java.io.File> libFilesToInherit3 = new java.util.ArrayList<>();
                        java.io.File[] fileArrListFiles = libDir.listFiles();
                        int length3 = fileArrListFiles.length;
                        libDirs = libDirs2;
                        int i8 = 0;
                        while (i8 < length3) {
                            int i9 = length3;
                            java.io.File archSubDir2 = fileArrListFiles[i8];
                            if (archSubDir2.isDirectory()) {
                                try {
                                    java.lang.String relLibPath = getRelativePath(archSubDir2, packageInstallDir3);
                                    fileArr = fileArrListFiles;
                                    java.io.File[] files2 = archSubDir2.listFiles();
                                    if (files2 != null) {
                                        packageInstallDir2 = packageInstallDir3;
                                        if (files2.length == 0) {
                                            i2 = length2;
                                            libDirsToInherit2 = libDirsToInherit3;
                                            libFilesToInherit2 = libFilesToInherit3;
                                        } else {
                                            libDirsToInherit2 = libDirsToInherit3;
                                            libDirsToInherit2.add(relLibPath);
                                            i2 = length2;
                                            libFilesToInherit2 = libFilesToInherit3;
                                            libFilesToInherit2.addAll(java.util.Arrays.asList(files2));
                                        }
                                    } else {
                                        packageInstallDir2 = packageInstallDir3;
                                        i2 = length2;
                                        libDirsToInherit2 = libDirsToInherit3;
                                        libFilesToInherit2 = libFilesToInherit3;
                                    }
                                } catch (java.io.IOException e4) {
                                    packageInstallDir = packageInstallDir3;
                                    i = length2;
                                    libDirsToInherit = libDirsToInherit3;
                                    libFilesToInherit = libFilesToInherit3;
                                    android.util.Slog.e(TAG, "Skipping linking of native library directory!", e4);
                                    libDirsToInherit.clear();
                                    libFilesToInherit.clear();
                                }
                            } else {
                                fileArr = fileArrListFiles;
                                packageInstallDir2 = packageInstallDir3;
                                i2 = length2;
                                libDirsToInherit2 = libDirsToInherit3;
                                libFilesToInherit2 = libFilesToInherit3;
                            }
                            i8++;
                            libDirsToInherit3 = libDirsToInherit2;
                            libFilesToInherit3 = libFilesToInherit2;
                            length3 = i9;
                            fileArrListFiles = fileArr;
                            packageInstallDir3 = packageInstallDir2;
                            length2 = i2;
                        }
                        packageInstallDir = packageInstallDir3;
                        i = length2;
                        libDirsToInherit = libDirsToInherit3;
                        libFilesToInherit = libFilesToInherit3;
                        for (java.lang.String subDir : libDirsToInherit) {
                            if (!this.mResolvedNativeLibPaths.contains(subDir)) {
                                this.mResolvedNativeLibPaths.add(subDir);
                            }
                        }
                        this.mResolvedInheritedFiles.addAll(libFilesToInherit);
                    } else {
                        packageInstallDir = packageInstallDir3;
                        libDirs = libDirs2;
                        i = length2;
                    }
                    i7++;
                    libDirs2 = libDirs;
                    packageInstallDir3 = packageInstallDir;
                    length2 = i;
                }
            }
            if (existing.isSplitRequired()) {
                int existingSplits = com.android.internal.util.ArrayUtils.size(existing.getSplitNames());
                boolean allSplitsRemoved = existingSplits == removeSplitList.size();
                z = true;
                boolean onlyBaseFileStaged = stagedSplits.size() == 1 && stagedSplits.contains(null);
                if ((allSplitsRemoved && (stagedSplits.isEmpty() || onlyBaseFileStaged)) || !stagedSplitTypes.containsAll(requiredSplitTypes)) {
                    throw new com.android.server.pm.PackageManagerException(-28, "Missing split for " + this.mPackageName);
                }
            } else {
                z = true;
            }
            packageLite = existing;
        } else {
            if (!stagedSplits.contains(null)) {
                throw new com.android.server.pm.PackageManagerException(-2, "Full install must include a base package");
            }
            if ((this.params.installFlags & 4096) != 0) {
                android.util.EventLog.writeEvent(1397638484, "219044664");
                this.params.setDontKillApp(false);
            }
            if (baseApk.isSplitRequired() && (stagedSplits.size() <= 1 || !stagedSplitTypes.containsAll(requiredSplitTypes))) {
                throw new com.android.server.pm.PackageManagerException(-28, "Missing split for " + this.mPackageName);
            }
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.PackageLite> pkgLiteResult2 = android.content.pm.parsing.ApkLiteParseUtils.composePackageLiteFromApks(input.reset(), this.stageDir, baseApk, splitApks, true);
            if (pkgLiteResult2.isError()) {
                throw new com.android.server.pm.PackageManagerException(pkgLiteResult2.getErrorCode(), pkgLiteResult2.getErrorMessage(), pkgLiteResult2.getException());
            }
            this.mPackageLite = (android.content.pm.parsing.PackageLite) pkgLiteResult2.getResult();
            packageLite = this.mPackageLite;
            z = true;
        }
        assertPreapprovalDetailsConsistentIfNeededLocked(packageLite, pkgInfo);
        if (packageLite.isUseEmbeddedDex()) {
            for (java.io.File file : this.mResolvedStagedFiles) {
                if (file.getName().endsWith(".apk") && !com.android.server.pm.dex.DexManager.auditUncompressedDexInApk(file.getPath())) {
                    throw new com.android.server.pm.PackageManagerException(-2, "Some dex are not uncompressed and aligned correctly for " + this.mPackageName);
                }
            }
        }
        if (this.mInstallerUid != 2000) {
            z = false;
        }
        boolean isInstallerShell = z;
        if (isInstallerShell && isIncrementalInstallation() && this.mIncrementalFileStorages != null && !packageLite.isDebuggable() && !packageLite.isProfileableByShell()) {
            this.mIncrementalFileStorages.disallowReadLogs();
        }
        this.mValidatedTargetSdk = packageLite.getTargetSdk();
        return packageLite;
    }

    private void stageFileLocked(java.io.File origFile, java.io.File targetFile) throws com.android.server.pm.PackageManagerException {
        this.mResolvedStagedFiles.add(targetFile);
        maybeRenameFile(origFile, targetFile);
    }

    private void maybeStageFsveritySignatureLocked(java.io.File origFile, java.io.File targetFile, boolean fsVerityRequired) throws com.android.server.pm.PackageManagerException {
        if (android.security.Flags.deprecateFsvSig()) {
            return;
        }
        java.io.File originalSignature = new java.io.File(com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(origFile.getPath()));
        if (originalSignature.exists()) {
            java.io.File stagedSignature = new java.io.File(com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(targetFile.getPath()));
            stageFileLocked(originalSignature, stagedSignature);
        } else if (fsVerityRequired) {
            throw new com.android.server.pm.PackageManagerException(-118, "Missing corresponding fs-verity signature to " + origFile);
        }
    }

    private void maybeStageV4SignatureLocked(java.io.File origFile, java.io.File targetFile) throws com.android.server.pm.PackageManagerException {
        java.io.File originalSignature = new java.io.File(origFile.getPath() + ".idsig");
        if (originalSignature.exists()) {
            java.io.File stagedSignature = new java.io.File(targetFile.getPath() + ".idsig");
            stageFileLocked(originalSignature, stagedSignature);
        }
    }

    private void maybeStageDexMetadataLocked(java.io.File origFile, java.io.File targetFile) throws com.android.server.pm.PackageManagerException {
        java.io.File dexMetadataFile = android.content.pm.dex.DexMetadataHelper.findDexMetadataForFile(origFile);
        if (dexMetadataFile == null) {
            return;
        }
        if (!android.os.FileUtils.isValidExtFilename(dexMetadataFile.getName())) {
            throw new com.android.server.pm.PackageManagerException(-2, "Invalid filename: " + dexMetadataFile);
        }
        java.io.File targetDexMetadataFile = new java.io.File(this.stageDir, android.content.pm.dex.DexMetadataHelper.buildDexMetadataPathForApk(targetFile.getName()));
        stageFileLocked(dexMetadataFile, targetDexMetadataFile);
        maybeStageFsveritySignatureLocked(dexMetadataFile, targetDexMetadataFile, android.content.pm.dex.DexMetadataHelper.isFsVerityRequired());
    }

    private android.os.incremental.IncrementalFileStorages getIncrementalFileStorages() {
        android.os.incremental.IncrementalFileStorages incrementalFileStorages;
        synchronized (this.mLock) {
            incrementalFileStorages = this.mIncrementalFileStorages;
        }
        return incrementalFileStorages;
    }

    private void storeBytesToInstallationFile(java.lang.String localPath, java.lang.String absolutePath, byte[] bytes) throws java.io.IOException {
        android.os.incremental.IncrementalFileStorages incrementalFileStorages = getIncrementalFileStorages();
        if (!isIncrementalInstallation() || incrementalFileStorages == null) {
            android.os.FileUtils.bytesToFile(absolutePath, bytes);
        } else {
            incrementalFileStorages.makeFile(localPath, bytes, vendor.pixelworks.hardware.display.VendorConfig.TYPE_MAX);
        }
    }

    private void maybeStageDigestsLocked(java.io.File origFile, java.io.File targetFile, java.lang.String splitName) throws com.android.server.pm.PackageManagerException {
        com.android.server.pm.PackageInstallerSession.PerFileChecksum perFileChecksum = this.mChecksums.get(origFile.getName());
        if (perFileChecksum == null) {
            return;
        }
        this.mChecksums.remove(origFile.getName());
        android.content.pm.Checksum[] checksums = perFileChecksum.getChecksums();
        if (checksums.length == 0) {
            return;
        }
        java.lang.String targetDigestsPath = com.android.server.pm.ApkChecksums.buildDigestsPathForApk(targetFile.getName());
        java.io.File targetDigestsFile = new java.io.File(this.stageDir, targetDigestsPath);
        try {
            try {
                java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
                try {
                    com.android.server.pm.ApkChecksums.writeChecksums(os, checksums);
                    byte[] signature = perFileChecksum.getSignature();
                    if (signature != null && signature.length > 0) {
                        com.android.server.pm.ApkChecksums.verifySignature(checksums, signature);
                    }
                    storeBytesToInstallationFile(targetDigestsPath, targetDigestsFile.getAbsolutePath(), os.toByteArray());
                    stageFileLocked(targetDigestsFile, targetDigestsFile);
                    if (signature != null && signature.length != 0) {
                        java.lang.String targetDigestsSignaturePath = com.android.server.pm.ApkChecksums.buildSignaturePathForDigests(targetDigestsPath);
                        java.io.File targetDigestsSignatureFile = new java.io.File(this.stageDir, targetDigestsSignaturePath);
                        storeBytesToInstallationFile(targetDigestsSignaturePath, targetDigestsSignatureFile.getAbsolutePath(), signature);
                        stageFileLocked(targetDigestsSignatureFile, targetDigestsSignatureFile);
                        os.close();
                        return;
                    }
                    os.close();
                } catch (java.lang.Throwable th) {
                    try {
                        os.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.security.NoSuchAlgorithmException | java.security.SignatureException e) {
                throw new com.android.server.pm.PackageManagerException(com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_INVALID_TRANSITION, "Failed to verify digests' signature for " + this.mPackageName, e);
            }
        } catch (java.io.IOException e2) {
            throw new com.android.server.pm.PackageManagerException(-4, "Failed to store digests for " + this.mPackageName, e2);
        }
    }

    private boolean isFsVerityRequiredForApk(java.io.File origFile, java.io.File targetFile) throws com.android.server.pm.PackageManagerException {
        if (this.mVerityFoundForApks) {
            return true;
        }
        java.io.File originalSignature = new java.io.File(com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(origFile.getPath()));
        if (!originalSignature.exists()) {
            return false;
        }
        this.mVerityFoundForApks = true;
        for (java.io.File file : this.mResolvedStagedFiles) {
            if (file.getName().endsWith(".apk") && !targetFile.getName().equals(file.getName())) {
                throw new com.android.server.pm.PackageManagerException(-118, "Previously staged apk is missing fs-verity signature");
            }
        }
        return true;
    }

    private void resolveAndStageFileLocked(java.io.File origFile, java.io.File targetFile, java.lang.String splitName) throws com.android.server.pm.PackageManagerException {
        stageFileLocked(origFile, targetFile);
        maybeStageFsveritySignatureLocked(origFile, targetFile, isFsVerityRequiredForApk(origFile, targetFile));
        if (android.security.Flags.extendVbChainToUpdatedApk() && com.android.internal.security.VerityUtils.isFsVeritySupported()) {
            maybeStageV4SignatureLocked(origFile, targetFile);
        }
        maybeStageDexMetadataLocked(origFile, targetFile);
        maybeStageDigestsLocked(origFile, targetFile, splitName);
    }

    private void maybeInheritFsveritySignatureLocked(java.io.File origFile) {
        java.io.File fsveritySignatureFile = new java.io.File(com.android.internal.security.VerityUtils.getFsveritySignatureFilePath(origFile.getPath()));
        if (fsveritySignatureFile.exists()) {
            this.mResolvedInheritedFiles.add(fsveritySignatureFile);
        }
    }

    private void maybeInheritV4SignatureLocked(java.io.File origFile) {
        java.io.File v4SignatureFile = new java.io.File(origFile.getPath() + ".idsig");
        if (v4SignatureFile.exists()) {
            this.mResolvedInheritedFiles.add(v4SignatureFile);
        }
    }

    private void inheritFileLocked(java.io.File origFile) {
        this.mResolvedInheritedFiles.add(origFile);
        maybeInheritFsveritySignatureLocked(origFile);
        if (android.security.Flags.extendVbChainToUpdatedApk()) {
            maybeInheritV4SignatureLocked(origFile);
        }
        java.io.File dexMetadataFile = android.content.pm.dex.DexMetadataHelper.findDexMetadataForFile(origFile);
        if (dexMetadataFile != null) {
            this.mResolvedInheritedFiles.add(dexMetadataFile);
            maybeInheritFsveritySignatureLocked(dexMetadataFile);
        }
        java.io.File digestsFile = com.android.server.pm.ApkChecksums.findDigestsForFile(origFile);
        if (digestsFile != null) {
            this.mResolvedInheritedFiles.add(digestsFile);
            java.io.File signatureFile = com.android.server.pm.ApkChecksums.findSignatureForDigests(digestsFile);
            if (signatureFile != null) {
                this.mResolvedInheritedFiles.add(signatureFile);
            }
        }
    }

    private void assertApkConsistentLocked(java.lang.String tag, android.content.pm.parsing.ApkLite apk) throws com.android.server.pm.PackageManagerException {
        assertPackageConsistentLocked(tag, apk.getPackageName(), apk.getLongVersionCode());
        if (!this.mSigningDetails.signaturesMatchExactly(apk.getSigningDetails())) {
            throw new com.android.server.pm.PackageManagerException(-2, tag + " signatures are inconsistent");
        }
    }

    private void assertPackageConsistentLocked(java.lang.String tag, java.lang.String packageName, long versionCode) throws com.android.server.pm.PackageManagerException {
        if (!this.mPackageName.equals(packageName)) {
            throw new com.android.server.pm.PackageManagerException(-2, tag + " package " + packageName + " inconsistent with " + this.mPackageName);
        }
        if (this.params.appPackageName != null && !this.params.appPackageName.equals(packageName)) {
            throw new com.android.server.pm.PackageManagerException(-2, tag + " specified package " + this.params.appPackageName + " inconsistent with " + packageName);
        }
        if (this.mVersionCode != versionCode) {
            throw new com.android.server.pm.PackageManagerException(-2, tag + " version code " + versionCode + " inconsistent with " + this.mVersionCode);
        }
    }

    private void assertPreapprovalDetailsConsistentIfNeededLocked(android.content.pm.parsing.PackageLite packageLite, android.content.pm.PackageInfo info) throws com.android.server.pm.PackageManagerException {
        if (this.mPreapprovalDetails == null || !isPreapprovalRequested()) {
            return;
        }
        if (!android.text.TextUtils.equals(this.mPackageName, this.mPreapprovalDetails.getPackageName())) {
            throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, this.mPreapprovalDetails + " inconsistent with " + this.mPackageName);
        }
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        android.content.pm.PackageInfo existingPackageInfo = info != null ? info : this.mPm.snapshotComputer().getPackageInfo(this.mPackageName, 0L, this.userId);
        java.lang.CharSequence appLabel = this.mPreapprovalDetails.getLabel();
        if (existingPackageInfo != null) {
            android.content.pm.ApplicationInfo existingAppInfo = existingPackageInfo.applicationInfo;
            java.lang.CharSequence existingAppLabel = packageManager.getApplicationLabel(existingAppInfo);
            if (android.text.TextUtils.equals(appLabel, existingAppLabel)) {
                return;
            }
        }
        android.content.pm.PackageInfo packageInfoFromApk = packageManager.getPackageArchiveInfo(packageLite.getPath(), android.content.pm.PackageManager.PackageInfoFlags.of(0L));
        if (packageInfoFromApk == null) {
            throw new com.android.server.pm.PackageManagerException(-2, "Failure to obtain package info from APK files.");
        }
        java.util.List<java.lang.String> filePaths = packageLite.getAllApkPaths();
        android.icu.util.ULocale appLocale = this.mPreapprovalDetails.getLocale();
        android.content.pm.ApplicationInfo appInfo = packageInfoFromApk.applicationInfo;
        boolean appLabelMatched = false;
        for (int i = filePaths.size() - 1; i >= 0 && !appLabelMatched; i--) {
            appLabelMatched |= android.text.TextUtils.equals(getAppLabel(filePaths.get(i), appLocale, appInfo), appLabel);
        }
        if (!appLabelMatched) {
            throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, this.mPreapprovalDetails + " inconsistent with app label");
        }
    }

    private java.lang.CharSequence getAppLabel(java.lang.String path, android.icu.util.ULocale locale, android.content.pm.ApplicationInfo appInfo) throws com.android.server.pm.PackageManagerException {
        android.content.res.Resources pRes = this.mContext.getResources();
        android.content.res.AssetManager assetManager = new android.content.res.AssetManager();
        android.content.res.Configuration config = new android.content.res.Configuration(pRes.getConfiguration());
        try {
            android.content.res.ApkAssets apkAssets = android.content.res.ApkAssets.loadFromPath(path);
            assetManager.setApkAssets(new android.content.res.ApkAssets[]{apkAssets}, false);
            config.setLocale(locale.toLocale());
            android.content.res.Resources res = new android.content.res.Resources(assetManager, pRes.getDisplayMetrics(), config);
            return android.text.TextUtils.trimToSize(tryLoadingAppLabel(res, appInfo), 1000);
        } catch (java.io.IOException e) {
            throw new com.android.server.pm.PackageManagerException(-2, "Failure to get resources from package archive " + path);
        }
    }

    private java.lang.CharSequence tryLoadingAppLabel(android.content.res.Resources res, android.content.pm.ApplicationInfo info) {
        java.lang.CharSequence label = null;
        if (info.labelRes != 0) {
            try {
                label = res.getText(info.labelRes).toString().trim();
            } catch (android.content.res.Resources.NotFoundException e) {
            }
        }
        if (label == null) {
            return info.nonLocalizedLabel != null ? info.nonLocalizedLabel : info.packageName;
        }
        return label;
    }

    private android.content.pm.SigningDetails unsafeGetCertsWithoutVerification(java.lang.String path) throws com.android.server.pm.PackageManagerException {
        android.content.pm.parsing.result.ParseTypeImpl input = android.content.pm.parsing.result.ParseTypeImpl.forDefaultParsing();
        android.content.pm.parsing.result.ParseResult<android.content.pm.SigningDetails> result = android.util.apk.ApkSignatureVerifier.unsafeGetCertsWithoutVerification(input, path, 1);
        if (result.isError()) {
            throw new com.android.server.pm.PackageManagerException(-2, "Couldn't obtain signatures from APK : " + path);
        }
        return (android.content.pm.SigningDetails) result.getResult();
    }

    private static boolean isLinkPossible(java.util.List<java.io.File> fromFiles, java.io.File toDir) {
        try {
            android.system.StructStat toStat = android.system.Os.stat(toDir.getAbsolutePath());
            for (java.io.File fromFile : fromFiles) {
                android.system.StructStat fromStat = android.system.Os.stat(fromFile.getAbsolutePath());
                if (fromStat.st_dev != toStat.st_dev) {
                    return false;
                }
            }
            return true;
        } catch (android.system.ErrnoException e) {
            android.util.Slog.w(TAG, "Failed to detect if linking possible: " + e);
            return false;
        }
    }

    public int getInstallerUid() {
        int i;
        synchronized (this.mLock) {
            i = this.mInstallerUid;
        }
        return i;
    }

    public java.lang.String getPackageName() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mPackageName;
        }
        return str;
    }

    public long getUpdatedMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.updatedMillis;
        }
        return j;
    }

    long getCommittedMillis() {
        long j;
        synchronized (this.mLock) {
            j = this.committedMillis;
        }
        return j;
    }

    java.lang.String getInstallerPackageName() {
        return getInstallSource().mInstallerPackageName;
    }

    java.lang.String getInstallerAttributionTag() {
        return getInstallSource().mInstallerAttributionTag;
    }

    com.android.server.pm.InstallSource getInstallSource() {
        com.android.server.pm.InstallSource installSource;
        synchronized (this.mLock) {
            installSource = this.mInstallSource;
        }
        return installSource;
    }

    android.content.pm.SigningDetails getSigningDetails() {
        android.content.pm.SigningDetails signingDetails;
        synchronized (this.mLock) {
            signingDetails = this.mSigningDetails;
        }
        return signingDetails;
    }

    android.content.pm.parsing.PackageLite getPackageLite() {
        android.content.pm.parsing.PackageLite packageLite;
        synchronized (this.mLock) {
            packageLite = this.mPackageLite;
        }
        return packageLite;
    }

    public boolean getUserActionRequired() {
        if (this.mUserActionRequired != null) {
            return this.mUserActionRequired.booleanValue();
        }
        android.util.Slog.wtf(TAG, "mUserActionRequired should not be null.");
        return false;
    }

    private static java.lang.String getRelativePath(java.io.File file, java.io.File base) throws java.io.IOException {
        java.lang.String pathStr = file.getAbsolutePath();
        java.lang.String baseStr = base.getAbsolutePath();
        if (pathStr.contains("/.")) {
            throw new java.io.IOException("Invalid path (was relative) : " + pathStr);
        }
        if (pathStr.startsWith(baseStr)) {
            return pathStr.substring(baseStr.length());
        }
        throw new java.io.IOException("File: " + pathStr + " outside base: " + baseStr);
    }

    private void createOatDirs(java.lang.String packageName, java.util.List<java.lang.String> instructionSets, java.io.File fromDir) throws com.android.server.pm.PackageManagerException {
        this.mPackageInstallerSessionExt.beforeCreateOatDirs();
        for (java.lang.String instructionSet : instructionSets) {
            try {
                this.mInstaller.createOatDir(packageName, fromDir.getAbsolutePath(), instructionSet);
            } catch (com.android.server.pm.Installer.InstallerException e) {
                throw com.android.server.pm.PackageManagerException.from(e);
            }
        }
    }

    private void linkFile(java.lang.String packageName, java.lang.String relativePath, java.lang.String fromBase, java.lang.String toBase) throws java.io.IOException {
        try {
            android.os.incremental.IncrementalFileStorages incrementalFileStorages = getIncrementalFileStorages();
            if (incrementalFileStorages != null && incrementalFileStorages.makeLink(relativePath, fromBase, toBase)) {
                return;
            }
            this.mInstaller.linkFile(packageName, relativePath, fromBase, toBase);
        } catch (com.android.server.pm.Installer.InstallerException | java.io.IOException e) {
            throw new java.io.IOException("failed linkOrCreateDir(" + relativePath + ", " + fromBase + ", " + toBase + ")", e);
        }
    }

    private void linkFiles(java.lang.String packageName, java.util.List<java.io.File> fromFiles, java.io.File toDir, java.io.File fromDir) throws java.io.IOException {
        for (java.io.File fromFile : fromFiles) {
            java.lang.String relativePath = getRelativePath(fromFile, fromDir);
            java.lang.String fromBase = fromDir.getAbsolutePath();
            java.lang.String toBase = toDir.getAbsolutePath();
            linkFile(packageName, relativePath, fromBase, toBase);
        }
        android.util.Slog.d(TAG, "Linked " + fromFiles.size() + " files into " + toDir);
    }

    private static void copyFiles(java.util.List<java.io.File> fromFiles, java.io.File toDir) throws java.io.IOException {
        for (java.io.File file : toDir.listFiles()) {
            if (file.getName().endsWith(".tmp")) {
                file.delete();
            }
        }
        for (java.io.File fromFile : fromFiles) {
            java.io.File tmpFile = java.io.File.createTempFile("inherit", ".tmp", toDir);
            android.util.Slog.d(TAG, "Copying " + fromFile + " to " + tmpFile);
            if (!android.os.FileUtils.copyFile(fromFile, tmpFile)) {
                throw new java.io.IOException("Failed to copy " + fromFile + " to " + tmpFile);
            }
            try {
                android.system.Os.chmod(tmpFile.getAbsolutePath(), com.android.internal.util.FrameworkStatsLog.VBMETA_DIGEST_REPORTED);
                java.io.File toFile = new java.io.File(toDir, fromFile.getName());
                android.util.Slog.d(TAG, "Renaming " + tmpFile + " to " + toFile);
                if (!tmpFile.renameTo(toFile)) {
                    throw new java.io.IOException("Failed to rename " + tmpFile + " to " + toFile);
                }
            } catch (android.system.ErrnoException e) {
                throw new java.io.IOException("Failed to chmod " + tmpFile);
            }
        }
        android.util.Slog.d(TAG, "Copied " + fromFiles.size() + " files into " + toDir);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void extractNativeLibraries(android.content.pm.parsing.PackageLite packageLite, java.io.File packageDir, java.lang.String abiOverride, boolean inherit) throws com.android.server.pm.PackageManagerException {
        java.util.Objects.requireNonNull(packageLite);
        java.io.File libDir = new java.io.File(packageDir, "lib");
        if (!inherit) {
            com.android.internal.content.NativeLibraryHelper.removeNativeBinariesFromDirLI(libDir, true);
        }
        if (isArchivedInstallation()) {
            return;
        }
        com.android.internal.content.NativeLibraryHelper.Handle handle = null;
        try {
            try {
                handle = com.android.internal.content.NativeLibraryHelper.Handle.create(packageLite);
                int res = com.android.internal.content.NativeLibraryHelper.copyNativeBinariesWithOverride(handle, libDir, abiOverride, isIncrementalInstallation());
                if (res != 1) {
                    throw new com.android.server.pm.PackageManagerException(res, "Failed to extract native libraries, res=" + res);
                }
                return;
            } catch (java.io.IOException e) {
                throw new com.android.server.pm.PackageManagerException(android.hardware.biometrics.fingerprint.V2_1.RequestStatus.SYS_ETIMEDOUT, "Failed to extract native libraries", e);
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(handle);
        }
        libcore.io.IoUtils.closeQuietly(handle);
    }

    void setPermissionsResult(boolean accepted) {
        if (!isSealed() && !isPreapprovalRequested()) {
            throw new java.lang.SecurityException("Must be sealed to accept permissions");
        }
        com.android.server.pm.PackageInstallerSession root = (hasParentSessionId() && isCommitted()) ? this.mSessionProvider.getSession(getParentSessionId()) : this;
        if (accepted) {
            synchronized (this.mLock) {
                this.mPermissionsManuallyAccepted = true;
            }
            root.mHandler.obtainMessage(isCommitted() ? 3 : 6).sendToTarget();
        } else {
            root.destroy("User rejected permissions");
            root.dispatchSessionFinished(-115, "User rejected permissions", null);
            root.maybeFinishChildSessions(-115, "User rejected permissions");
        }
    }

    public void open() throws java.io.IOException {
        boolean wasPrepared;
        activate();
        synchronized (this.mLock) {
            wasPrepared = this.mPrepared;
            if (!this.mPrepared) {
                if (this.stageDir != null) {
                    com.android.server.pm.PackageInstallerService.prepareStageDir(this.stageDir);
                } else if (!this.params.isMultiPackage) {
                    throw new java.lang.IllegalArgumentException("stageDir must be set");
                }
                this.mPrepared = true;
            }
        }
        if (!wasPrepared) {
            this.mCallback.onSessionPrepared(this);
        }
    }

    private void activate() {
        if (this.mActiveCount.getAndIncrement() == 0) {
            this.mCallback.onSessionActiveChanged(this, true);
        }
    }

    public void close() {
        closeInternal(true);
    }

    private void closeInternal(boolean checkCaller) {
        synchronized (this.mLock) {
            if (checkCaller) {
                assertCallerIsOwnerOrRoot();
            }
        }
        deactivate();
    }

    private void deactivate() {
        int activeCount;
        synchronized (this.mLock) {
            activeCount = this.mActiveCount.decrementAndGet();
        }
        if (activeCount == 0) {
            this.mCallback.onSessionActiveChanged(this, false);
        }
    }

    private void maybeFinishChildSessions(int returnCode, java.lang.String msg) {
        for (com.android.server.pm.PackageInstallerSession child : getChildSessions()) {
            child.dispatchSessionFinished(returnCode, msg, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertNotChild(java.lang.String cookie) {
        if (hasParentSessionId()) {
            throw new java.lang.IllegalStateException(cookie + " can't be called on a child session, id=" + this.sessionId + " parentId=" + getParentSessionId());
        }
    }

    private boolean dispatchPendingAbandonCallback() {
        synchronized (this.mLock) {
            if (!this.mStageDirInUse) {
                return false;
            }
            if (!this.mMarkStageDirUserNoConsume) {
                return true;
            }
            this.mMarkStageDirUserNoConsume = false;
            this.mStageDirInUse = false;
            java.lang.Runnable callback = this.mPendingAbandonCallback;
            this.mPendingAbandonCallback = null;
            if (callback == null) {
                return false;
            }
            callback.run();
            return true;
        }
    }

    public void abandon() {
        this.mPackageInstallerSessionSocExt.boostBeforeAbandon();
        synchronized (this.mLock) {
            assertNotChild("abandon");
            assertCallerIsOwnerOrRootOrSystem();
            if (isInTerminalState()) {
                return;
            }
            this.mDestroyed = true;
            java.lang.Runnable r = new java.lang.Runnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$abandon$7();
                }
            };
            if (this.mStageDirInUse) {
                this.mPendingAbandonCallback = r;
                this.mCallback.onSessionChanged(this);
            } else {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    r.run();
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$abandon$7() {
        assertNotLocked("abandonStaged");
        if (isStaged() && isCommitted()) {
            this.mStagingManager.abortCommittedSession(this.mStagedSession);
        }
        destroy("Session was abandoned");
        dispatchSessionFinished(-115, "Session was abandoned", null);
        maybeFinishChildSessions(-115, "Session was abandoned because the parent session is abandoned");
    }

    public boolean isMultiPackage() {
        return this.params.isMultiPackage;
    }

    public boolean isStaged() {
        return this.params.isStaged;
    }

    public int getInstallFlags() {
        return this.params.installFlags;
    }

    public android.content.pm.DataLoaderParamsParcel getDataLoaderParams() {
        getDataLoaderParams_enforcePermission();
        if (this.params.dataLoaderParams != null) {
            return this.params.dataLoaderParams.getData();
        }
        return null;
    }

    public void addFile(int location, java.lang.String name, long lengthBytes, byte[] metadata, byte[] signature) {
        addFile_enforcePermission();
        if (!isDataLoaderInstallation()) {
            throw new java.lang.IllegalStateException("Cannot add files to non-data loader installation session.");
        }
        if (isStreamingInstallation() && location != 0) {
            throw new java.lang.IllegalArgumentException("Non-incremental installation only supports /data/app placement: " + name);
        }
        if (metadata == null) {
            throw new java.lang.IllegalArgumentException("DataLoader installation requires valid metadata: " + name);
        }
        if (!android.os.FileUtils.isValidExtFilename(name)) {
            throw new java.lang.IllegalArgumentException("Invalid name: " + name);
        }
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("addFile");
            if (!this.mFiles.add(new com.android.server.pm.PackageInstallerSession.FileEntry(this.mFiles.size(), new android.content.pm.InstallationFile(location, name, lengthBytes, metadata, signature)))) {
                throw new java.lang.IllegalArgumentException("File already added: " + name);
            }
        }
    }

    public void removeFile(int location, java.lang.String name) {
        removeFile_enforcePermission();
        if (!isDataLoaderInstallation()) {
            throw new java.lang.IllegalStateException("Cannot add files to non-data loader installation session.");
        }
        if (android.text.TextUtils.isEmpty(this.params.appPackageName)) {
            throw new java.lang.IllegalStateException("Must specify package name to remove a split");
        }
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("removeFile");
            if (!this.mFiles.add(new com.android.server.pm.PackageInstallerSession.FileEntry(this.mFiles.size(), new android.content.pm.InstallationFile(location, getRemoveMarkerName(name), -1L, (byte[]) null, (byte[]) null)))) {
                throw new java.lang.IllegalArgumentException("File already removed: " + name);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00ed  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean prepareDataLoaderLocked() throws com.android.server.pm.PackageManagerException {
        /*
            Method dump skipped, instruction units count: 398
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageInstallerSession.prepareDataLoaderLocked():boolean");
    }

    private android.content.pm.DataLoaderManager getDataLoaderManager() throws com.android.server.pm.PackageManagerException {
        android.content.pm.DataLoaderManager dataLoaderManager = (android.content.pm.DataLoaderManager) this.mContext.getSystemService(android.content.pm.DataLoaderManager.class);
        if (dataLoaderManager == null) {
            throw new com.android.server.pm.PackageManagerException(-20, "Failed to find data loader manager service");
        }
        return dataLoaderManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.content.pm.IDataLoader getDataLoader(int dataLoaderId) throws com.android.server.pm.PackageManagerException {
        android.content.pm.IDataLoader dataLoader = getDataLoaderManager().getDataLoader(dataLoaderId);
        if (dataLoader == null) {
            throw new com.android.server.pm.PackageManagerException(-20, "Failure to obtain data loader");
        }
        return dataLoader;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSessionValidationFailure(int error, java.lang.String detailMessage) {
        this.mHandler.obtainMessage(5, error, -1, detailMessage).sendToTarget();
    }

    private int[] getChildSessionIdsLocked() {
        int size = this.mChildSessions.size();
        if (size == 0) {
            return EMPTY_CHILD_SESSION_ARRAY;
        }
        int[] childSessionIds = new int[size];
        for (int i = 0; i < size; i++) {
            childSessionIds[i] = this.mChildSessions.keyAt(i);
        }
        return childSessionIds;
    }

    public int[] getChildSessionIds() {
        int[] childSessionIdsLocked;
        synchronized (this.mLock) {
            childSessionIdsLocked = getChildSessionIdsLocked();
        }
        return childSessionIdsLocked;
    }

    private boolean canBeAddedAsChild(int parentCandidate) {
        boolean z;
        synchronized (this.mLock) {
            z = ((hasParentSessionId() && this.mParentSessionId != parentCandidate) || isCommitted() || this.mDestroyed) ? false : true;
        }
        return z;
    }

    private void acquireTransactionLock() {
        if (!this.mTransactionLock.compareAndSet(false, true)) {
            throw new java.lang.UnsupportedOperationException("Concurrent access not supported");
        }
    }

    private void releaseTransactionLock() {
        this.mTransactionLock.compareAndSet(true, false);
    }

    public void addChildSessionId(int childSessionId) {
        if (!this.params.isMultiPackage) {
            throw new java.lang.IllegalStateException("Single-session " + this.sessionId + " can't have child.");
        }
        com.android.server.pm.PackageInstallerSession childSession = this.mSessionProvider.getSession(childSessionId);
        if (childSession == null) {
            throw new java.lang.IllegalStateException("Unable to add child session " + childSessionId + " as it does not exist.");
        }
        if (childSession.params.isMultiPackage) {
            throw new java.lang.IllegalStateException("Multi-session " + childSessionId + " can't be a child.");
        }
        if (this.params.isStaged != childSession.params.isStaged) {
            throw new java.lang.IllegalStateException("Multipackage Inconsistency: session " + childSession.sessionId + " and session " + this.sessionId + " have inconsistent staged settings");
        }
        if (this.params.getEnableRollback() != childSession.params.getEnableRollback()) {
            throw new java.lang.IllegalStateException("Multipackage Inconsistency: session " + childSession.sessionId + " and session " + this.sessionId + " have inconsistent rollback settings");
        }
        boolean hasAPK = containsApkSession() || !childSession.isApexSession();
        boolean hasAPEX = sessionContains(new java.util.function.Predicate() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.pm.PackageInstallerSession) obj).isApexSession();
            }
        }) || childSession.isApexSession();
        if (!this.params.isStaged && hasAPK && hasAPEX) {
            throw new java.lang.IllegalStateException("Mix of APK and APEX is not supported for non-staged multi-package session");
        }
        try {
            acquireTransactionLock();
            childSession.acquireTransactionLock();
            if (!childSession.canBeAddedAsChild(this.sessionId)) {
                throw new java.lang.IllegalStateException("Unable to add child session " + childSessionId + " as it is in an invalid state.");
            }
            synchronized (this.mLock) {
                assertCallerIsOwnerOrRoot();
                assertPreparedAndNotSealedLocked("addChildSessionId");
                int indexOfSession = this.mChildSessions.indexOfKey(childSessionId);
                if (indexOfSession >= 0) {
                    return;
                }
                childSession.setParentSessionId(this.sessionId);
                this.mChildSessions.put(childSessionId, childSession);
            }
        } finally {
            releaseTransactionLock();
            childSession.releaseTransactionLock();
        }
    }

    public void removeChildSessionId(int sessionId) {
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("removeChildSessionId");
            int indexOfSession = this.mChildSessions.indexOfKey(sessionId);
            if (indexOfSession < 0) {
                return;
            }
            com.android.server.pm.PackageInstallerSession session = this.mChildSessions.valueAt(indexOfSession);
            try {
                acquireTransactionLock();
                session.acquireTransactionLock();
                session.setParentSessionId(-1);
                this.mChildSessions.removeAt(indexOfSession);
            } finally {
                releaseTransactionLock();
                session.releaseTransactionLock();
            }
        }
    }

    void setParentSessionId(int parentSessionId) {
        synchronized (this.mLock) {
            if (parentSessionId != -1) {
                if (this.mParentSessionId != -1) {
                    throw new java.lang.IllegalStateException("The parent of " + this.sessionId + " is alreadyset to " + this.mParentSessionId);
                }
            }
            this.mParentSessionId = parentSessionId;
        }
    }

    boolean hasParentSessionId() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mParentSessionId != -1;
        }
        return z;
    }

    public int getParentSessionId() {
        int i;
        synchronized (this.mLock) {
            i = this.mParentSessionId;
        }
        return i;
    }

    private void dispatchSessionFinished(int returnCode, java.lang.String msg, android.os.Bundle extras) {
        this.mPackageInstallerSessionExt.beforeDispatchSessionFinished(this);
        sendUpdateToRemoteStatusReceiver(returnCode, msg, extras, isPreapprovalRequested() && !isCommitted());
        synchronized (this.mLock) {
            this.mFinalStatus = returnCode;
            this.mFinalMessage = msg;
        }
        boolean success = returnCode == 1;
        boolean isNewInstall = extras == null || !extras.getBoolean("android.intent.extra.REPLACING");
        if (success && isNewInstall && this.mPm.mInstallerService.okToSendBroadcasts()) {
            this.mPm.sendSessionCommitBroadcast(generateInfoScrubbed(true), this.userId);
        }
        this.mCallback.onSessionFinished(this, success);
        if (isDataLoaderInstallation()) {
            logDataLoaderInstallationSession(returnCode);
        }
        this.mPackageInstallerSessionExt.afterDispatchSessionFinished(this, this.mPm);
    }

    private void sendUpdateToRemoteStatusReceiver(int returnCode, java.lang.String msg, android.os.Bundle extras, boolean forPreapproval) {
        android.content.IntentSender statusReceiver = forPreapproval ? getPreapprovalRemoteStatusReceiver() : getRemoteStatusReceiver();
        if (statusReceiver != null) {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = getPackageName();
            args.arg2 = msg;
            args.arg3 = extras;
            args.arg4 = statusReceiver;
            args.argi1 = returnCode;
            args.argi2 = (!isPreapprovalRequested() || isCommitted()) ? 0 : 1;
            this.mHandler.obtainMessage(4, args).sendToTarget();
        }
    }

    private void dispatchSessionPreapproved() {
        android.content.IntentSender target = getPreapprovalRemoteStatusReceiver();
        if (target == null) {
            android.util.Slog.e(TAG, "Missing receiver for session preapproved status.");
            return;
        }
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.content.pm.extra.SESSION_ID", this.sessionId);
        intent.putExtra("android.content.pm.extra.STATUS", 0);
        intent.putExtra("android.content.pm.extra.PRE_APPROVAL", true);
        try {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setPendingIntentBackgroundActivityLaunchAllowed(false);
            target.sendIntent(this.mContext, 0, intent, null, null, null, options.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
        }
    }

    public void requestUserPreapproval(android.content.pm.PackageInstaller.PreapprovalDetails details, android.content.IntentSender statusReceiver) {
        validatePreapprovalRequest(details, statusReceiver);
        this.mPackageInstallerSessionExt.beforeRequestUserPreapprovalAvailable(this.mPm.mRequiredInstallerPackage);
        if (!com.android.server.pm.PackageManagerService.isPreapprovalRequestAvailable()) {
            sendUpdateToRemoteStatusReceiver(-129, "Request user pre-approval is currently not available.", null, true);
        } else {
            dispatchPreapprovalRequest();
        }
    }

    private void validatePreapprovalRequest(android.content.pm.PackageInstaller.PreapprovalDetails details, android.content.IntentSender statusReceiver) {
        assertCallerIsOwnerOrRoot();
        if (isMultiPackage()) {
            throw new java.lang.IllegalStateException("Session " + this.sessionId + " is a parent of multi-package session and requestUserPreapproval on the parent session isn't supported.");
        }
        synchronized (this.mLock) {
            assertPreparedAndNotSealedLocked("request of session " + this.sessionId);
            this.mPreapprovalDetails = details;
            setPreapprovalRemoteStatusReceiver(statusReceiver);
        }
    }

    private void dispatchPreapprovalRequest() {
        synchronized (this.mLock) {
            assertPreparedAndNotPreapprovalRequestedLocked("dispatchPreapprovalRequest");
        }
        markAsPreapprovalRequested();
        this.mHandler.obtainMessage(6).sendToTarget();
    }

    private void markAsPreapprovalRequested() {
        this.mPreapprovalRequested.set(true);
    }

    public boolean isApplicationEnabledSettingPersistent() {
        return this.params.applicationEnabledSettingPersistent;
    }

    public boolean isRequestUpdateOwnership() {
        return (this.params.installFlags & 33554432) != 0;
    }

    public void setPreVerifiedDomains(android.content.pm.verify.domain.DomainSet preVerifiedDomains) {
        boolean exemptFromPermissionChecks = this.mInstallerUid == 0 || this.mInstallerUid == 2000;
        if (!exemptFromPermissionChecks) {
            com.android.server.pm.Computer snapshot = this.mPm.snapshotComputer();
            if (snapshot.checkUidPermission("android.permission.ACCESS_INSTANT_APPS", this.mInstallerUid) != 0) {
                throw new java.lang.SecurityException("You need android.permission.ACCESS_INSTANT_APPS permission to set pre-verified domains.");
            }
            android.content.ComponentName instantAppInstallerComponent = snapshot.getInstantAppInstallerComponent();
            if (instantAppInstallerComponent == null) {
                throw new java.lang.IllegalStateException("Instant app installer is not available. Only the instant app installer can call this API.");
            }
            if (!instantAppInstallerComponent.getPackageName().equals(getInstallerPackageName())) {
                throw new java.lang.SecurityException("Only the instant app installer can call this API.");
            }
        }
        long preVerifiedDomainsCountLimit = getPreVerifiedDomainsCountLimit();
        if (preVerifiedDomains.getDomains().size() > preVerifiedDomainsCountLimit) {
            throw new java.lang.IllegalArgumentException("The number of pre-verified domains have exceeded the maximum of " + preVerifiedDomainsCountLimit);
        }
        long preVerifiedDomainLengthLimit = getPreVerifiedDomainLengthLimit();
        for (java.lang.String domain : preVerifiedDomains.getDomains()) {
            if (domain.length() > preVerifiedDomainLengthLimit) {
                throw new java.lang.IllegalArgumentException("Pre-verified domain: [" + domain + " ] exceeds maximum length allowed: " + preVerifiedDomainLengthLimit);
            }
        }
        synchronized (this.mLock) {
            assertCallerIsOwnerOrRoot();
            assertPreparedAndNotSealedLocked("setPreVerifiedDomains");
            this.mPreVerifiedDomains = preVerifiedDomains;
        }
    }

    private static long getPreVerifiedDomainsCountLimit() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getLong("package_manager_service", PROPERTY_PRE_VERIFIED_DOMAINS_COUNT_LIMIT, 1000L);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private static long getPreVerifiedDomainLengthLimit() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.DeviceConfig.getLong("package_manager_service", PROPERTY_PRE_VERIFIED_DOMAIN_LENGTH_LIMIT, DEFAULT_PRE_VERIFIED_DOMAIN_LENGTH_LIMIT);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    public android.content.pm.verify.domain.DomainSet getPreVerifiedDomains() {
        android.content.pm.verify.domain.DomainSet domainSet;
        assertCallerIsOwnerOrRoot();
        synchronized (this.mLock) {
            assertPreparedAndNotCommittedOrDestroyedLocked("getPreVerifiedDomains");
            domainSet = this.mPreVerifiedDomains;
        }
        return domainSet;
    }

    void setSessionReady() {
        synchronized (this.mLock) {
            if (!this.mDestroyed && !this.mSessionFailed) {
                this.mSessionReady = true;
                this.mSessionApplied = false;
                this.mSessionFailed = false;
                this.mSessionErrorCode = 0;
                this.mSessionErrorMessage = "";
                this.mCallback.onSessionChanged(this);
            }
        }
    }

    void setSessionFailed(int errorCode, java.lang.String errorMessage) {
        synchronized (this.mLock) {
            if (!this.mDestroyed && !this.mSessionFailed) {
                this.mSessionReady = false;
                this.mSessionApplied = false;
                this.mSessionFailed = true;
                this.mSessionErrorCode = errorCode;
                this.mSessionErrorMessage = errorMessage;
                android.util.Slog.d(TAG, "Marking session " + this.sessionId + " as failed: " + errorMessage);
                destroy("Session marked as failed: " + errorMessage);
                this.mCallback.onSessionChanged(this);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSessionApplied() {
        synchronized (this.mLock) {
            this.mPackageInstallerSessionExt.recordSotaAppResult(this.mStagedSession, 1, "");
            if (!this.mDestroyed && !this.mSessionFailed) {
                this.mSessionReady = false;
                this.mSessionApplied = true;
                this.mSessionFailed = false;
                this.mSessionErrorCode = 1;
                this.mSessionErrorMessage = "";
                android.util.Slog.d(TAG, "Marking session " + this.sessionId + " as applied");
                destroy(null);
                this.mCallback.onSessionChanged(this);
            }
        }
    }

    boolean isSessionReady() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionReady;
        }
        return z;
    }

    boolean isSessionApplied() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionApplied;
        }
        return z;
    }

    boolean isSessionFailed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mSessionFailed;
        }
        return z;
    }

    int getSessionErrorCode() {
        int i;
        synchronized (this.mLock) {
            i = this.mSessionErrorCode;
        }
        return i;
    }

    java.lang.String getSessionErrorMessage() {
        java.lang.String str;
        synchronized (this.mLock) {
            str = this.mSessionErrorMessage;
        }
        return str;
    }

    void registerUnarchivalListener(android.content.IntentSender intentSender) {
        synchronized (this.mLock) {
            this.mUnarchivalListeners.add(intentSender);
        }
    }

    java.util.Set<android.content.IntentSender> getUnarchivalListeners() {
        android.util.ArraySet arraySet;
        synchronized (this.mLock) {
            arraySet = new android.util.ArraySet(this.mUnarchivalListeners);
        }
        return arraySet;
    }

    void reportUnarchivalStatus(final int status, int unarchiveId, final long requiredStorageBytes, final android.app.PendingIntent userActionIntent) {
        if (getUnarchivalStatus() != -1) {
            throw new java.lang.IllegalStateException(android.text.TextUtils.formatSimple("Unarchival status for ID %s has already been set or a session has been created for it already by the caller.", new java.lang.Object[]{java.lang.Integer.valueOf(unarchiveId)}));
        }
        this.mUnarchivalStatus = status;
        this.mPm.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reportUnarchivalStatus$9(status, requiredStorageBytes, userActionIntent);
            }
        });
        if (status != 0) {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda8
                public final void runOrThrow() {
                    this.f$0.abandon();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$reportUnarchivalStatus$9(int status, long requiredStorageBytes, android.app.PendingIntent userActionIntent) {
        this.mPm.mInstallerService.mPackageArchiver.notifyUnarchivalListener(status, getInstallerPackageName(), this.params.appPackageName, requiredStorageBytes, userActionIntent, getUnarchivalListeners(), this.userId);
    }

    int getUnarchivalStatus() {
        return this.mUnarchivalStatus;
    }

    private void destroy(java.lang.String reason) {
        destroyInternal(reason);
        for (com.android.server.pm.PackageInstallerSession child : getChildSessions()) {
            child.destroyInternal(reason);
        }
    }

    private void destroyInternal(java.lang.String reason) {
        android.os.incremental.IncrementalFileStorages incrementalFileStorages;
        if (reason != null) {
            android.util.Slog.i(TAG, "Session [" + this.sessionId + "] was destroyed because of [" + reason + "]");
        }
        synchronized (this.mLock) {
            this.mSealed = true;
            if (!this.params.isStaged) {
                this.mDestroyed = true;
            }
            for (android.os.RevocableFileDescriptor fd : this.mFds) {
                fd.revoke();
            }
            for (android.os.FileBridge bridge : this.mBridges) {
                bridge.forceClose();
            }
            incrementalFileStorages = this.mIncrementalFileStorages;
            this.mIncrementalFileStorages = null;
        }
        if (incrementalFileStorages != null) {
            try {
                incrementalFileStorages.cleanUpAndMarkComplete();
            } catch (com.android.server.pm.Installer.InstallerException e) {
                return;
            }
        }
        if (this.stageDir != null) {
            java.lang.String tempPackageName = this.stageDir.getName();
            this.mInstaller.rmPackageDir(tempPackageName, this.stageDir.getAbsolutePath());
        }
    }

    void dump(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            dumpLocked(pw);
        }
    }

    private void dumpLocked(com.android.internal.util.IndentingPrintWriter pw) {
        float clientProgress;
        float progress;
        pw.println("Session " + this.sessionId + ":");
        pw.increaseIndent();
        pw.printPair("userId", java.lang.Integer.valueOf(this.userId));
        pw.printPair("mOriginalInstallerUid", java.lang.Integer.valueOf(this.mOriginalInstallerUid));
        pw.printPair("mOriginalInstallerPackageName", this.mOriginalInstallerPackageName);
        pw.printPair(ATTR_INSTALLER_PACKAGE_NAME, this.mInstallSource.mInstallerPackageName);
        pw.printPair(ATTR_INITIATING_PACKAGE_NAME, this.mInstallSource.mInitiatingPackageName);
        pw.printPair(ATTR_ORIGINATING_PACKAGE_NAME, this.mInstallSource.mOriginatingPackageName);
        pw.printPair("mInstallerUid", java.lang.Integer.valueOf(this.mInstallerUid));
        pw.printPair(ATTR_CREATED_MILLIS, java.lang.Long.valueOf(this.createdMillis));
        pw.printPair(ATTR_UPDATED_MILLIS, java.lang.Long.valueOf(this.updatedMillis));
        pw.printPair(ATTR_COMMITTED_MILLIS, java.lang.Long.valueOf(this.committedMillis));
        pw.printPair("stageDir", this.stageDir);
        pw.printPair("stageCid", this.stageCid);
        pw.println();
        this.params.dump(pw);
        synchronized (this.mProgressLock) {
            clientProgress = this.mClientProgress;
            progress = this.mProgress;
        }
        pw.printPair("mClientProgress", java.lang.Float.valueOf(clientProgress));
        pw.printPair("mProgress", java.lang.Float.valueOf(progress));
        pw.printPair("mCommitted", this.mCommitted);
        pw.printPair("mPreapprovalRequested", this.mPreapprovalRequested);
        pw.printPair("mSealed", java.lang.Boolean.valueOf(this.mSealed));
        pw.printPair("mPermissionsManuallyAccepted", java.lang.Boolean.valueOf(this.mPermissionsManuallyAccepted));
        pw.printPair("mStageDirInUse", java.lang.Boolean.valueOf(this.mStageDirInUse));
        pw.printPair("mDestroyed", java.lang.Boolean.valueOf(this.mDestroyed));
        pw.printPair("mFds", java.lang.Integer.valueOf(this.mFds.size()));
        pw.printPair("mBridges", java.lang.Integer.valueOf(this.mBridges.size()));
        pw.printPair("mFinalStatus", java.lang.Integer.valueOf(this.mFinalStatus));
        pw.printPair("mFinalMessage", this.mFinalMessage);
        pw.printPair("params.isMultiPackage", java.lang.Boolean.valueOf(this.params.isMultiPackage));
        pw.printPair("params.isStaged", java.lang.Boolean.valueOf(this.params.isStaged));
        pw.printPair("mParentSessionId", java.lang.Integer.valueOf(this.mParentSessionId));
        pw.printPair("mChildSessionIds", getChildSessionIdsLocked());
        pw.printPair("mSessionApplied", java.lang.Boolean.valueOf(this.mSessionApplied));
        pw.printPair("mSessionFailed", java.lang.Boolean.valueOf(this.mSessionFailed));
        pw.printPair("mSessionReady", java.lang.Boolean.valueOf(this.mSessionReady));
        pw.printPair("mSessionErrorCode", java.lang.Integer.valueOf(this.mSessionErrorCode));
        pw.printPair("mSessionErrorMessage", this.mSessionErrorMessage);
        pw.printPair("mPreapprovalDetails", this.mPreapprovalDetails);
        if (this.mPreVerifiedDomains != null) {
            pw.printPair("mPreVerifiedDomains", this.mPreVerifiedDomains);
        }
        pw.println();
        pw.decreaseIndent();
    }

    private static void sendOnUserActionRequired(android.content.Context context, android.content.IntentSender target, int sessionId, android.content.Intent intent) {
        if (target == null) {
            android.util.Slog.e(TAG, "Missing receiver for user action required status.");
            return;
        }
        android.content.Intent fillIn = new android.content.Intent();
        fillIn.putExtra("android.content.pm.extra.SESSION_ID", sessionId);
        fillIn.putExtra("android.content.pm.extra.STATUS", -1);
        fillIn.putExtra("android.content.pm.extra.PRE_APPROVAL", "android.content.pm.action.CONFIRM_PRE_APPROVAL".equals(intent.getAction()));
        fillIn.putExtra("android.intent.extra.INTENT", intent);
        try {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setPendingIntentBackgroundActivityLaunchAllowed(false);
            target.sendIntent(context, 0, fillIn, null, null, null, options.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendOnPackageInstalled(android.content.Context context, android.content.IntentSender target, int sessionId, boolean showNotification, int userId, java.lang.String basePackageName, int returnCode, boolean isPreapproval, java.lang.String msg, android.os.Bundle extras) {
        if (1 == returnCode && showNotification) {
            boolean update = extras != null && extras.getBoolean("android.intent.extra.REPLACING");
            android.app.Notification notification = com.android.server.pm.PackageInstallerService.buildSuccessNotification(context, getDeviceOwnerInstalledPackageMsg(context, update), basePackageName, userId);
            if (notification != null) {
                android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService("notification");
                notificationManager.notify(basePackageName, 21, notification);
            }
        }
        android.content.Intent fillIn = new android.content.Intent();
        fillIn.putExtra("android.content.pm.extra.PACKAGE_NAME", basePackageName);
        fillIn.putExtra("android.content.pm.extra.SESSION_ID", sessionId);
        fillIn.putExtra("android.content.pm.extra.STATUS", android.content.pm.PackageManager.installStatusToPublicStatus(returnCode));
        fillIn.putExtra("android.content.pm.extra.STATUS_MESSAGE", android.content.pm.PackageManager.installStatusToString(returnCode, msg));
        fillIn.putExtra("android.content.pm.extra.LEGACY_STATUS", returnCode);
        fillIn.putExtra("android.content.pm.extra.PRE_APPROVAL", isPreapproval);
        if (extras != null) {
            java.lang.String existing = extras.getString("android.content.pm.extra.FAILURE_EXISTING_PACKAGE");
            if (!android.text.TextUtils.isEmpty(existing)) {
                fillIn.putExtra("android.content.pm.extra.OTHER_PACKAGE_NAME", existing);
            }
            java.util.ArrayList<java.lang.String> warnings = extras.getStringArrayList("android.content.pm.extra.WARNINGS");
            if (!com.android.internal.util.ArrayUtils.isEmpty(warnings)) {
                fillIn.putStringArrayListExtra("android.content.pm.extra.WARNINGS", warnings);
            }
        }
        try {
            if (com.android.server.pm.PackageManagerService.DEBUG_INSTALL) {
                try {
                    android.util.Slog.d(TAG, "sendIntent in sendOnPackageInstalled: " + sessionId + ", " + userId + ", " + basePackageName + ", " + returnCode + ", " + msg);
                } catch (android.content.IntentSender.SendIntentException e) {
                    return;
                }
            }
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setPendingIntentBackgroundActivityLaunchAllowed(false);
            try {
                target.sendIntent(context, 0, fillIn, null, null, null, options.toBundle());
            } catch (android.content.IntentSender.SendIntentException e2) {
            }
        } catch (android.content.IntentSender.SendIntentException e3) {
        }
    }

    private static java.lang.String getDeviceOwnerInstalledPackageMsg(final android.content.Context context, boolean update) {
        android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) context.getSystemService(android.app.admin.DevicePolicyManager.class);
        if (update) {
            return dpm.getResources().getString("Core.PACKAGE_UPDATED_BY_DO", new java.util.function.Supplier() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return context.getString(android.R.string.notification_title_long_running_fgs);
                }
            });
        }
        return dpm.getResources().getString("Core.PACKAGE_INSTALLED_BY_DO", new java.util.function.Supplier() { // from class: com.android.server.pm.PackageInstallerSession$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return context.getString(android.R.string.notification_title_abusive_bg_apps);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendPendingStreaming(android.content.Context context, android.content.IntentSender target, int sessionId, java.lang.String cause) {
        if (target == null) {
            android.util.Slog.e(TAG, "Missing receiver for pending streaming status.");
            return;
        }
        android.content.Intent intent = new android.content.Intent();
        intent.putExtra("android.content.pm.extra.SESSION_ID", sessionId);
        intent.putExtra("android.content.pm.extra.STATUS", -2);
        if (!android.text.TextUtils.isEmpty(cause)) {
            intent.putExtra("android.content.pm.extra.STATUS_MESSAGE", "Staging Image Not Ready [" + cause + "]");
        } else {
            intent.putExtra("android.content.pm.extra.STATUS_MESSAGE", "Staging Image Not Ready");
        }
        try {
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            options.setPendingIntentBackgroundActivityLaunchAllowed(false);
            target.sendIntent(context, 0, intent, null, null, null, options.toBundle());
        } catch (android.content.IntentSender.SendIntentException e) {
        }
    }

    private static void writePermissionsLocked(com.android.modules.utils.TypedXmlSerializer out, android.content.pm.PackageInstaller.SessionParams params) throws java.io.IOException {
        android.util.ArrayMap<java.lang.String, java.lang.Integer> permissionStates = params.getPermissionStates();
        for (int index = 0; index < permissionStates.size(); index++) {
            java.lang.String permissionName = permissionStates.keyAt(index);
            java.lang.Integer state = permissionStates.valueAt(index);
            java.lang.String tag = state.intValue() == 1 ? TAG_GRANT_PERMISSION : TAG_DENY_PERMISSION;
            out.startTag((java.lang.String) null, tag);
            com.android.internal.util.XmlUtils.writeStringAttribute(out, "name", permissionName);
            out.endTag((java.lang.String) null, tag);
        }
    }

    private static void writeWhitelistedRestrictedPermissionsLocked(com.android.modules.utils.TypedXmlSerializer out, java.util.List<java.lang.String> whitelistedRestrictedPermissions) throws java.io.IOException {
        if (whitelistedRestrictedPermissions != null) {
            int permissionCount = whitelistedRestrictedPermissions.size();
            for (int i = 0; i < permissionCount; i++) {
                out.startTag((java.lang.String) null, TAG_WHITELISTED_RESTRICTED_PERMISSION);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, "name", whitelistedRestrictedPermissions.get(i));
                out.endTag((java.lang.String) null, TAG_WHITELISTED_RESTRICTED_PERMISSION);
            }
        }
    }

    private static void writeAutoRevokePermissionsMode(com.android.modules.utils.TypedXmlSerializer out, int mode) throws java.io.IOException {
        out.startTag((java.lang.String) null, TAG_AUTO_REVOKE_PERMISSIONS_MODE);
        out.attributeInt((java.lang.String) null, "mode", mode);
        out.endTag((java.lang.String) null, TAG_AUTO_REVOKE_PERMISSIONS_MODE);
    }

    private static java.io.File buildAppIconFile(int sessionId, java.io.File sessionsDir) {
        return new java.io.File(sessionsDir, "app_icon." + sessionId + ".png");
    }

    void write(com.android.modules.utils.TypedXmlSerializer out, java.io.File sessionsDir) throws java.io.IOException {
        synchronized (this.mLock) {
            if (!this.mDestroyed || this.params.isStaged) {
                out.startTag((java.lang.String) null, TAG_SESSION);
                out.attributeInt((java.lang.String) null, ATTR_SESSION_ID, this.sessionId);
                out.attributeInt((java.lang.String) null, "userId", this.userId);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_INSTALLER_PACKAGE_NAME, this.mInstallSource.mInstallerPackageName);
                out.attributeInt((java.lang.String) null, ATTR_INSTALLER_PACKAGE_UID, this.mInstallSource.mInstallerPackageUid);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_UPDATE_OWNER_PACKAGE_NAME, this.mInstallSource.mUpdateOwnerPackageName);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_INSTALLER_ATTRIBUTION_TAG, this.mInstallSource.mInstallerAttributionTag);
                out.attributeInt((java.lang.String) null, ATTR_INSTALLER_UID, this.mInstallerUid);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_INITIATING_PACKAGE_NAME, this.mInstallSource.mInitiatingPackageName);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_ORIGINATING_PACKAGE_NAME, this.mInstallSource.mOriginatingPackageName);
                out.attributeLong((java.lang.String) null, ATTR_CREATED_MILLIS, this.createdMillis);
                out.attributeLong((java.lang.String) null, ATTR_UPDATED_MILLIS, this.updatedMillis);
                out.attributeLong((java.lang.String) null, ATTR_COMMITTED_MILLIS, this.committedMillis);
                if (this.stageDir != null) {
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_SESSION_STAGE_DIR, this.stageDir.getAbsolutePath());
                }
                if (this.stageCid != null) {
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_SESSION_STAGE_CID, this.stageCid);
                }
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_PREPARED, this.mPrepared);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_COMMITTED, isCommitted());
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_DESTROYED, this.mDestroyed);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_SEALED, this.mSealed);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_MULTI_PACKAGE, this.params.isMultiPackage);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_STAGED_SESSION, this.params.isStaged);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_IS_READY, this.mSessionReady);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_IS_FAILED, this.mSessionFailed);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_IS_APPLIED, this.mSessionApplied);
                out.attributeInt((java.lang.String) null, ATTR_PACKAGE_SOURCE, this.params.packageSource);
                out.attributeInt((java.lang.String) null, ATTR_SESSION_ERROR_CODE, this.mSessionErrorCode);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_SESSION_ERROR_MESSAGE, this.mSessionErrorMessage);
                out.attributeInt((java.lang.String) null, ATTR_PARENT_SESSION_ID, this.mParentSessionId);
                out.attributeInt((java.lang.String) null, "mode", this.params.mode);
                out.attributeInt((java.lang.String) null, ATTR_INSTALL_FLAGS, this.params.installFlags);
                out.attributeInt((java.lang.String) null, ATTR_INSTALL_LOCATION, this.params.installLocation);
                out.attributeLong((java.lang.String) null, ATTR_SIZE_BYTES, this.params.sizeBytes);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_APP_PACKAGE_NAME, this.params.appPackageName);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_APP_LABEL, this.params.appLabel);
                com.android.internal.util.XmlUtils.writeUriAttribute(out, ATTR_ORIGINATING_URI, this.params.originatingUri);
                out.attributeInt((java.lang.String) null, ATTR_ORIGINATING_UID, this.params.originatingUid);
                com.android.internal.util.XmlUtils.writeUriAttribute(out, ATTR_REFERRER_URI, this.params.referrerUri);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_ABI_OVERRIDE, this.params.abiOverride);
                com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_VOLUME_UUID, this.params.volumeUuid);
                out.attributeInt((java.lang.String) null, ATTR_INSTALL_REASON, this.params.installReason);
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_APPLICATION_ENABLED_SETTING_PERSISTENT, this.params.applicationEnabledSettingPersistent);
                boolean isDataLoader = this.params.dataLoaderParams != null;
                com.android.internal.util.XmlUtils.writeBooleanAttribute(out, ATTR_IS_DATALOADER, isDataLoader);
                if (isDataLoader) {
                    out.attributeInt((java.lang.String) null, ATTR_DATALOADER_TYPE, this.params.dataLoaderParams.getType());
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_DATALOADER_PACKAGE_NAME, this.params.dataLoaderParams.getComponentName().getPackageName());
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_DATALOADER_CLASS_NAME, this.params.dataLoaderParams.getComponentName().getClassName());
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, ATTR_DATALOADER_ARGUMENTS, this.params.dataLoaderParams.getArguments());
                }
                writePermissionsLocked(out, this.params);
                writeWhitelistedRestrictedPermissionsLocked(out, this.params.whitelistedRestrictedPermissions);
                writeAutoRevokePermissionsMode(out, this.params.autoRevokePermissionsMode);
                java.io.File appIconFile = buildAppIconFile(this.sessionId, sessionsDir);
                if (this.params.appIcon == null && appIconFile.exists()) {
                    appIconFile.delete();
                } else if (this.params.appIcon != null && appIconFile.lastModified() != this.params.appIconLastModified) {
                    android.util.Slog.w(TAG, "Writing changed icon " + appIconFile);
                    java.io.FileOutputStream os = null;
                    try {
                        try {
                            os = new java.io.FileOutputStream(appIconFile);
                            this.params.appIcon.compress(android.graphics.Bitmap.CompressFormat.PNG, 90, os);
                        } catch (java.io.IOException e) {
                            android.util.Slog.w(TAG, "Failed to write icon " + appIconFile + ": " + e.getMessage());
                        }
                        this.params.appIconLastModified = appIconFile.lastModified();
                    } finally {
                        libcore.io.IoUtils.closeQuietly(os);
                    }
                }
                int[] childSessionIds = getChildSessionIdsLocked();
                for (int childSessionId : childSessionIds) {
                    out.startTag((java.lang.String) null, TAG_CHILD_SESSION);
                    out.attributeInt((java.lang.String) null, ATTR_SESSION_ID, childSessionId);
                    out.endTag((java.lang.String) null, TAG_CHILD_SESSION);
                }
                android.content.pm.InstallationFile[] files = getInstallationFilesLocked();
                for (android.content.pm.InstallationFile file : files) {
                    out.startTag((java.lang.String) null, TAG_SESSION_FILE);
                    out.attributeInt((java.lang.String) null, ATTR_LOCATION, file.getLocation());
                    com.android.internal.util.XmlUtils.writeStringAttribute(out, "name", file.getName());
                    out.attributeLong((java.lang.String) null, ATTR_LENGTH_BYTES, file.getLengthBytes());
                    com.android.internal.util.XmlUtils.writeByteArrayAttribute(out, ATTR_METADATA, file.getMetadata());
                    com.android.internal.util.XmlUtils.writeByteArrayAttribute(out, ATTR_SIGNATURE, file.getSignature());
                    out.endTag((java.lang.String) null, TAG_SESSION_FILE);
                }
                int isize = this.mChecksums.size();
                for (int i = 0; i < isize; i++) {
                    java.lang.String fileName = this.mChecksums.keyAt(i);
                    com.android.server.pm.PackageInstallerSession.PerFileChecksum perFileChecksum = this.mChecksums.valueAt(i);
                    android.content.pm.Checksum[] checksums = perFileChecksum.getChecksums();
                    int length = checksums.length;
                    int i2 = 0;
                    while (i2 < length) {
                        android.content.pm.Checksum checksum = checksums[i2];
                        out.startTag((java.lang.String) null, TAG_SESSION_CHECKSUM);
                        com.android.internal.util.XmlUtils.writeStringAttribute(out, "name", fileName);
                        out.attributeInt((java.lang.String) null, ATTR_CHECKSUM_KIND, checksum.getType());
                        com.android.internal.util.XmlUtils.writeByteArrayAttribute(out, ATTR_CHECKSUM_VALUE, checksum.getValue());
                        out.endTag((java.lang.String) null, TAG_SESSION_CHECKSUM);
                        i2++;
                        isDataLoader = isDataLoader;
                    }
                }
                int isize2 = this.mChecksums.size();
                for (int i3 = 0; i3 < isize2; i3++) {
                    java.lang.String fileName2 = this.mChecksums.keyAt(i3);
                    com.android.server.pm.PackageInstallerSession.PerFileChecksum perFileChecksum2 = this.mChecksums.valueAt(i3);
                    byte[] signature = perFileChecksum2.getSignature();
                    if (signature != null && signature.length != 0) {
                        out.startTag((java.lang.String) null, TAG_SESSION_CHECKSUM_SIGNATURE);
                        com.android.internal.util.XmlUtils.writeStringAttribute(out, "name", fileName2);
                        com.android.internal.util.XmlUtils.writeByteArrayAttribute(out, ATTR_SIGNATURE, signature);
                        out.endTag((java.lang.String) null, TAG_SESSION_CHECKSUM_SIGNATURE);
                    }
                }
                if (this.mPreVerifiedDomains != null) {
                    for (java.lang.String domain : this.mPreVerifiedDomains.getDomains()) {
                        out.startTag((java.lang.String) null, TAG_PRE_VERIFIED_DOMAINS);
                        com.android.internal.util.XmlUtils.writeStringAttribute(out, "domain", domain);
                        out.endTag((java.lang.String) null, TAG_PRE_VERIFIED_DOMAINS);
                    }
                }
                out.endTag((java.lang.String) null, TAG_SESSION);
            }
        }
    }

    private static boolean isStagedSessionStateValid(boolean isReady, boolean isApplied, boolean isFailed) {
        return ((isReady || isApplied || isFailed) && (!isReady || isApplied || isFailed) && ((isReady || !isApplied || isFailed) && (isReady || isApplied || !isFailed))) ? false : true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:60:0x02ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.pm.PackageInstallerSession readFromXml(com.android.modules.utils.TypedXmlPullParser r79, com.android.server.pm.PackageInstallerService.InternalCallback r80, android.content.Context r81, com.android.server.pm.PackageManagerService r82, android.os.Looper r83, com.android.server.pm.StagingManager r84, java.io.File r85, com.android.server.pm.PackageSessionProvider r86, com.android.server.pm.SilentUpdatePolicy r87) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 1810
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.PackageInstallerSession.readFromXml(com.android.modules.utils.TypedXmlPullParser, com.android.server.pm.PackageInstallerService$InternalCallback, android.content.Context, com.android.server.pm.PackageManagerService, android.os.Looper, com.android.server.pm.StagingManager, java.io.File, com.android.server.pm.PackageSessionProvider, com.android.server.pm.SilentUpdatePolicy):com.android.server.pm.PackageInstallerSession");
    }

    public com.android.server.pm.IPackageInstallerSessionWrapper getWrapper() {
        return this.mWrapper;
    }

    private class PackageInstallerSessionWrapper implements com.android.server.pm.IPackageInstallerSessionWrapper {
        private PackageInstallerSessionWrapper() {
        }

        @Override // com.android.server.pm.IPackageInstallerSessionWrapper
        public void extractNativeLibraries(android.content.pm.parsing.PackageLite packageLite, java.io.File packageDir, java.lang.String abiOverride, boolean inherit) throws com.android.server.pm.PackageManagerException {
            com.android.server.pm.PackageInstallerSession.this.extractNativeLibraries(packageLite, packageDir, abiOverride, inherit);
        }

        @Override // com.android.server.pm.IPackageInstallerSessionWrapper
        public android.content.pm.parsing.PackageLite getPackageLite() {
            return com.android.server.pm.PackageInstallerSession.this.mPackageLite;
        }

        @Override // com.android.server.pm.IPackageInstallerSessionWrapper
        public com.android.server.pm.InstallSource getInstallSource() {
            return com.android.server.pm.PackageInstallerSession.this.mInstallSource;
        }

        @Override // com.android.server.pm.IPackageInstallerSessionWrapper
        public int getFinalStatus() {
            return com.android.server.pm.PackageInstallerSession.this.mFinalStatus;
        }

        @Override // com.android.server.pm.IPackageInstallerSessionWrapper
        public java.lang.String getFinalMessage() {
            return com.android.server.pm.PackageInstallerSession.this.mFinalMessage;
        }
    }
}
