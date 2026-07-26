package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public class TunerResourceManagerService extends com.android.server.SystemService implements android.os.IBinder.DeathRecipient {
    public static final int INVALID_CLIENT_ID = -1;
    private static final int INVALID_FE_COUNT = -1;
    private static final long INVALID_THREAD_ID = -1;
    private static final int MAX_CLIENT_PRIORITY = 1000;
    private static final long TRMS_LOCK_TIMEOUT = 500;
    private android.app.ActivityManager mActivityManager;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.CasResource> mCasResources;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.CiCamResource> mCiCamResources;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.ClientProfile> mClientProfiles;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.DemuxResource> mDemuxResources;
    private android.util.SparseIntArray mFrontendExistingNums;
    private android.util.SparseIntArray mFrontendExistingNumsBackup;
    private android.util.SparseIntArray mFrontendMaxUsableNums;
    private android.util.SparseIntArray mFrontendMaxUsableNumsBackup;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.FrontendResource> mFrontendResources;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.FrontendResource> mFrontendResourcesBackup;
    private android.util.SparseIntArray mFrontendUsedNums;
    private android.util.SparseIntArray mFrontendUsedNumsBackup;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.ResourcesReclaimListenerRecord> mListeners;
    private java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.LnbResource> mLnbResources;
    private final java.lang.Object mLock;
    private final java.util.concurrent.locks.ReentrantLock mLockForTRMSLock;
    private android.media.IResourceManagerService mMediaResourceManager;
    private int mNextUnusedClientId;
    private com.android.server.tv.tunerresourcemanager.UseCasePriorityHints mPriorityCongfig;
    private int mResourceRequestCount;
    private int mTunerApiLockHolder;
    private long mTunerApiLockHolderThreadId;
    private int mTunerApiLockNestedCount;
    private final java.util.concurrent.locks.Condition mTunerApiLockReleasedCV;
    private android.media.tv.TvInputManager mTvInputManager;
    private static final java.lang.String TAG = "TunerResourceManagerService";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    public TunerResourceManagerService(android.content.Context context) {
        super(context);
        this.mClientProfiles = new java.util.HashMap();
        this.mNextUnusedClientId = 0;
        this.mFrontendResources = new java.util.HashMap();
        this.mFrontendMaxUsableNums = new android.util.SparseIntArray();
        this.mFrontendUsedNums = new android.util.SparseIntArray();
        this.mFrontendExistingNums = new android.util.SparseIntArray();
        this.mFrontendResourcesBackup = new java.util.HashMap();
        this.mFrontendMaxUsableNumsBackup = new android.util.SparseIntArray();
        this.mFrontendUsedNumsBackup = new android.util.SparseIntArray();
        this.mFrontendExistingNumsBackup = new android.util.SparseIntArray();
        this.mDemuxResources = new java.util.HashMap();
        this.mLnbResources = new java.util.HashMap();
        this.mCasResources = new java.util.HashMap();
        this.mCiCamResources = new java.util.HashMap();
        this.mListeners = new java.util.HashMap();
        this.mPriorityCongfig = new com.android.server.tv.tunerresourcemanager.UseCasePriorityHints();
        this.mResourceRequestCount = 0;
        this.mLock = new java.lang.Object();
        this.mLockForTRMSLock = new java.util.concurrent.locks.ReentrantLock();
        this.mTunerApiLockReleasedCV = this.mLockForTRMSLock.newCondition();
        this.mTunerApiLockHolder = -1;
        this.mTunerApiLockHolderThreadId = -1L;
        this.mTunerApiLockNestedCount = 0;
    }

    @Override // com.android.server.SystemService
    public void onStart() throws java.lang.Exception {
        onStart(false);
    }

    protected void onStart(boolean isForTesting) throws java.lang.Exception {
        if (!isForTesting) {
            publishBinderService("tv_tuner_resource_mgr", new com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.BinderService());
        }
        this.mTvInputManager = (android.media.tv.TvInputManager) getContext().getSystemService("tv_input");
        this.mActivityManager = (android.app.ActivityManager) getContext().getSystemService(com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY);
        this.mPriorityCongfig.parse();
        if (!isForTesting) {
            boolean lazyHal = android.os.SystemProperties.getBoolean("ro.tuner.lazyhal", false);
            if (!lazyHal) {
                android.os.SystemProperties.set("tuner.server.enable", "true");
            }
        }
        if (this.mMediaResourceManager == null) {
            android.os.IBinder mediaResourceManagerBinder = getBinderService("media.resource_manager");
            if (mediaResourceManagerBinder == null) {
                android.util.Slog.w(TAG, "Resource Manager Service not available.");
                return;
            }
            try {
                mediaResourceManagerBinder.linkToDeath(this, 0);
                this.mMediaResourceManager = android.media.IResourceManagerService.Stub.asInterface(mediaResourceManagerBinder);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Could not link to death of native resource manager service.");
            }
        }
    }

    private final class BinderService extends android.media.tv.tunerresourcemanager.ITunerResourceManager.Stub {
        private BinderService() {
        }

        public void registerClientProfile(android.media.tv.tunerresourcemanager.ResourceClientProfile profile, android.media.tv.tunerresourcemanager.IResourcesReclaimListener listener, int[] clientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("registerClientProfile");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("registerClientProfile");
            if (profile == null) {
                throw new android.os.RemoteException("ResourceClientProfile can't be null");
            }
            if (clientId == null) {
                throw new android.os.RemoteException("clientId can't be null!");
            }
            if (listener == null) {
                throw new android.os.RemoteException("IResourcesReclaimListener can't be null!");
            }
            if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mPriorityCongfig.isDefinedUseCase(profile.useCase)) {
                throw new android.os.RemoteException("Use undefined client use case:" + profile.useCase);
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.registerClientProfileInternal(profile, listener, clientId);
            }
        }

        public void unregisterClientProfile(int clientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("unregisterClientProfile");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(clientId)) {
                    android.util.Slog.e(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "Unregistering non exists client:" + clientId);
                } else {
                    com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.unregisterClientProfileInternal(clientId);
                }
            }
        }

        public boolean updateClientPriority(int clientId, int priority, int niceValue) {
            boolean zUpdateClientPriorityInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("updateClientPriority");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                zUpdateClientPriorityInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.updateClientPriorityInternal(clientId, priority, niceValue);
            }
            return zUpdateClientPriorityInternal;
        }

        public boolean hasUnusedFrontend(int frontendType) {
            boolean zHasUnusedFrontendInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("hasUnusedFrontend");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                zHasUnusedFrontendInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.hasUnusedFrontendInternal(frontendType);
            }
            return zHasUnusedFrontendInternal;
        }

        public boolean isLowestPriority(int clientId, int frontendType) throws android.os.RemoteException {
            boolean zIsLowestPriorityInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("isLowestPriority");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(clientId)) {
                    throw new android.os.RemoteException("isLowestPriority called from unregistered client: " + clientId);
                }
                zIsLowestPriorityInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.isLowestPriorityInternal(clientId, frontendType);
            }
            return zIsLowestPriorityInternal;
        }

        public void setFrontendInfoList(android.media.tv.tunerresourcemanager.TunerFrontendInfo[] infos) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("setFrontendInfoList");
            if (infos == null) {
                throw new android.os.RemoteException("TunerFrontendInfo can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.setFrontendInfoListInternal(infos);
            }
        }

        public void setDemuxInfoList(android.media.tv.tunerresourcemanager.TunerDemuxInfo[] infos) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("setDemuxInfoList");
            if (infos == null) {
                throw new android.os.RemoteException("TunerDemuxInfo can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.setDemuxInfoListInternal(infos);
            }
        }

        public void updateCasInfo(int casSystemId, int maxSessionNum) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("updateCasInfo");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.updateCasInfoInternal(casSystemId, maxSessionNum);
            }
        }

        public void setLnbInfoList(int[] lnbHandles) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("setLnbInfoList");
            if (lnbHandles == null) {
                throw new android.os.RemoteException("Lnb handle list can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.setLnbInfoListInternal(lnbHandles);
            }
        }

        public boolean requestFrontend(android.media.tv.tunerresourcemanager.TunerFrontendRequest request, int[] frontendHandle) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("requestFrontend");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("requestFrontend");
            if (frontendHandle == null) {
                android.util.Slog.e(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "frontendHandle can't be null");
                return false;
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(request.clientId)) {
                    android.util.Slog.e(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "Request frontend from unregistered client: " + request.clientId);
                    return false;
                }
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getClientProfile(request.clientId).getInUseFrontendHandles().isEmpty()) {
                    android.util.Slog.e(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "Release frontend before requesting another one. Client id: " + request.clientId);
                    return false;
                }
                return com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.requestFrontendInternal(request, frontendHandle);
            }
        }

        public boolean setMaxNumberOfFrontends(int frontendType, int maxUsableNum) {
            boolean maxNumberOfFrontendsInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("setMaxNumberOfFrontends");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("setMaxNumberOfFrontends");
            if (maxUsableNum < 0) {
                android.util.Slog.w(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "setMaxNumberOfFrontends failed with maxUsableNum:" + maxUsableNum + " frontendType:" + frontendType);
                return false;
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                maxNumberOfFrontendsInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.setMaxNumberOfFrontendsInternal(frontendType, maxUsableNum);
            }
            return maxNumberOfFrontendsInternal;
        }

        public int getMaxNumberOfFrontends(int frontendType) {
            int maxNumberOfFrontendsInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("getMaxNumberOfFrontends");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("getMaxNumberOfFrontends");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                maxNumberOfFrontendsInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getMaxNumberOfFrontendsInternal(frontendType);
            }
            return maxNumberOfFrontendsInternal;
        }

        public void shareFrontend(int selfClientId, int targetClientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("shareFrontend");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("shareFrontend");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(selfClientId)) {
                    throw new android.os.RemoteException("Share frontend request from an unregistered client:" + selfClientId);
                }
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(targetClientId)) {
                    throw new android.os.RemoteException("Request to share frontend with an unregistered client:" + targetClientId);
                }
                if (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getClientProfile(targetClientId).getInUseFrontendHandles().isEmpty()) {
                    throw new android.os.RemoteException("Request to share frontend with a client that has no frontend resources. Target client id:" + targetClientId);
                }
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.shareFrontendInternal(selfClientId, targetClientId);
            }
        }

        public boolean transferOwner(int resourceType, int currentOwnerId, int newOwnerId) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("transferOwner");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("transferOwner");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(currentOwnerId)) {
                    android.util.Slog.e(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "currentOwnerId:" + currentOwnerId + " does not exit");
                    return false;
                }
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(newOwnerId)) {
                    android.util.Slog.e(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "newOwnerId:" + newOwnerId + " does not exit");
                    return false;
                }
                return com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.transferOwnerInternal(resourceType, currentOwnerId, newOwnerId);
            }
        }

        public boolean requestDemux(android.media.tv.tunerresourcemanager.TunerDemuxRequest request, int[] demuxHandle) throws android.os.RemoteException {
            boolean zRequestDemuxInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("requestDemux");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("requestDemux");
            if (demuxHandle == null) {
                throw new android.os.RemoteException("demuxHandle can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(request.clientId)) {
                    throw new android.os.RemoteException("Request demux from unregistered client:" + request.clientId);
                }
                zRequestDemuxInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.requestDemuxInternal(request, demuxHandle);
            }
            return zRequestDemuxInternal;
        }

        public boolean requestDescrambler(android.media.tv.tunerresourcemanager.TunerDescramblerRequest request, int[] descramblerHandle) throws android.os.RemoteException {
            boolean zRequestDescramblerInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceDescramblerAccessPermission("requestDescrambler");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("requestDescrambler");
            if (descramblerHandle == null) {
                throw new android.os.RemoteException("descramblerHandle can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(request.clientId)) {
                    throw new android.os.RemoteException("Request descrambler from unregistered client:" + request.clientId);
                }
                zRequestDescramblerInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.requestDescramblerInternal(request, descramblerHandle);
            }
            return zRequestDescramblerInternal;
        }

        public boolean requestCasSession(android.media.tv.tunerresourcemanager.CasSessionRequest request, int[] casSessionHandle) throws android.os.RemoteException {
            boolean zRequestCasSessionInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("requestCasSession");
            if (casSessionHandle == null) {
                throw new android.os.RemoteException("casSessionHandle can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(request.clientId)) {
                    throw new android.os.RemoteException("Request cas from unregistered client:" + request.clientId);
                }
                zRequestCasSessionInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.requestCasSessionInternal(request, casSessionHandle);
            }
            return zRequestCasSessionInternal;
        }

        public boolean requestCiCam(android.media.tv.tunerresourcemanager.TunerCiCamRequest request, int[] ciCamHandle) throws android.os.RemoteException {
            boolean zRequestCiCamInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("requestCiCam");
            if (ciCamHandle == null) {
                throw new android.os.RemoteException("ciCamHandle can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(request.clientId)) {
                    throw new android.os.RemoteException("Request ciCam from unregistered client:" + request.clientId);
                }
                zRequestCiCamInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.requestCiCamInternal(request, ciCamHandle);
            }
            return zRequestCiCamInternal;
        }

        public boolean requestLnb(android.media.tv.tunerresourcemanager.TunerLnbRequest request, int[] lnbHandle) throws android.os.RemoteException {
            boolean zRequestLnbInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("requestLnb");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("requestLnb");
            if (lnbHandle == null) {
                throw new android.os.RemoteException("lnbHandle can't be null");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(request.clientId)) {
                    throw new android.os.RemoteException("Request lnb from unregistered client:" + request.clientId);
                }
                zRequestLnbInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.requestLnbInternal(request, lnbHandle);
            }
            return zRequestLnbInternal;
        }

        public void releaseFrontend(int frontendHandle, int clientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("releaseFrontend");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("releaseFrontend");
            if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.validateResourceHandle(0, frontendHandle)) {
                throw new android.os.RemoteException("frontendHandle can't be invalid");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(clientId)) {
                    throw new android.os.RemoteException("Release frontend from unregistered client:" + clientId);
                }
                com.android.server.tv.tunerresourcemanager.FrontendResource fe = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getFrontendResource(frontendHandle);
                if (fe == null) {
                    throw new android.os.RemoteException("Releasing frontend does not exist.");
                }
                int ownerClientId = fe.getOwnerClientId();
                com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getClientProfile(ownerClientId);
                if (ownerClientId != clientId && ownerProfile != null && !ownerProfile.getShareFeClientIds().contains(java.lang.Integer.valueOf(clientId))) {
                    throw new android.os.RemoteException("Client is not the current owner of the releasing fe.");
                }
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.releaseFrontendInternal(fe, clientId);
            }
        }

        public void releaseDemux(int demuxHandle, int clientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("releaseDemux");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("releaseDemux");
            if (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.DEBUG) {
                android.util.Slog.e(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "releaseDemux(demuxHandle=" + demuxHandle + ")");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mDemuxResources.size() == 0) {
                    return;
                }
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(clientId)) {
                    throw new android.os.RemoteException("Release demux for unregistered client:" + clientId);
                }
                com.android.server.tv.tunerresourcemanager.DemuxResource demux = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getDemuxResource(demuxHandle);
                if (demux == null) {
                    throw new android.os.RemoteException("Releasing demux does not exist.");
                }
                if (demux.getOwnerClientId() != clientId) {
                    throw new android.os.RemoteException("Client is not the current owner of the releasing demux.");
                }
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.releaseDemuxInternal(demux);
            }
        }

        public void releaseDescrambler(int descramblerHandle, int clientId) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("releaseDescrambler");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("releaseDescrambler");
            if (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.DEBUG) {
                android.util.Slog.d(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.TAG, "releaseDescrambler(descramblerHandle=" + descramblerHandle + ")");
            }
        }

        public void releaseCasSession(int casSessionHandle, int clientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("releaseCasSession");
            if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.validateResourceHandle(4, casSessionHandle)) {
                throw new android.os.RemoteException("casSessionHandle can't be invalid");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(clientId)) {
                    throw new android.os.RemoteException("Release cas from unregistered client:" + clientId);
                }
                int casSystemId = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getClientProfile(clientId).getInUseCasSystemId();
                com.android.server.tv.tunerresourcemanager.CasResource cas = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getCasResource(casSystemId);
                if (cas == null) {
                    throw new android.os.RemoteException("Releasing cas does not exist.");
                }
                if (!cas.getOwnerClientIds().contains(java.lang.Integer.valueOf(clientId))) {
                    throw new android.os.RemoteException("Client is not the current owner of the releasing cas.");
                }
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.releaseCasSessionInternal(cas, clientId);
            }
        }

        public void releaseCiCam(int ciCamHandle, int clientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("releaseCiCam");
            if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.validateResourceHandle(5, ciCamHandle)) {
                throw new android.os.RemoteException("ciCamHandle can't be invalid");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(clientId)) {
                    throw new android.os.RemoteException("Release ciCam from unregistered client:" + clientId);
                }
                int ciCamId = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getClientProfile(clientId).getInUseCiCamId();
                if (ciCamId != com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getResourceIdFromHandle(ciCamHandle)) {
                    throw new android.os.RemoteException("The client " + clientId + " is not the owner of the releasing ciCam.");
                }
                com.android.server.tv.tunerresourcemanager.CiCamResource ciCam = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getCiCamResource(ciCamId);
                if (ciCam == null) {
                    throw new android.os.RemoteException("Releasing ciCam does not exist.");
                }
                if (!ciCam.getOwnerClientIds().contains(java.lang.Integer.valueOf(clientId))) {
                    throw new android.os.RemoteException("Client is not the current owner of the releasing ciCam.");
                }
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.releaseCiCamInternal(ciCam, clientId);
            }
        }

        public void releaseLnb(int lnbHandle, int clientId) throws android.os.RemoteException {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTunerAccessPermission("releaseLnb");
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("releaseLnb");
            if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.validateResourceHandle(3, lnbHandle)) {
                throw new android.os.RemoteException("lnbHandle can't be invalid");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                if (!com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(clientId)) {
                    throw new android.os.RemoteException("Release lnb from unregistered client:" + clientId);
                }
                com.android.server.tv.tunerresourcemanager.LnbResource lnb = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getLnbResource(lnbHandle);
                if (lnb == null) {
                    throw new android.os.RemoteException("Releasing lnb does not exist.");
                }
                if (lnb.getOwnerClientId() != clientId) {
                    throw new android.os.RemoteException("Client is not the current owner of the releasing lnb.");
                }
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.releaseLnbInternal(lnb);
            }
        }

        public boolean isHigherPriority(android.media.tv.tunerresourcemanager.ResourceClientProfile challengerProfile, android.media.tv.tunerresourcemanager.ResourceClientProfile holderProfile) throws android.os.RemoteException {
            boolean zIsHigherPriorityInternal;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("isHigherPriority");
            if (challengerProfile == null || holderProfile == null) {
                throw new android.os.RemoteException("Client profiles can't be null.");
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                zIsHigherPriorityInternal = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.isHigherPriorityInternal(challengerProfile, holderProfile);
            }
            return zIsHigherPriorityInternal;
        }

        public void storeResourceMap(int resourceType) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("storeResourceMap");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.storeResourceMapInternal(resourceType);
            }
        }

        public void clearResourceMap(int resourceType) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("clearResourceMap");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.clearResourceMapInternal(resourceType);
            }
        }

        public void restoreResourceMap(int resourceType) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("restoreResourceMap");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.restoreResourceMapInternal(resourceType);
            }
        }

        public boolean acquireLock(int clientId, long clientThreadId) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("acquireLock");
            return com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.acquireLockInternal(clientId, clientThreadId, 500L);
        }

        public boolean releaseLock(int clientId) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("releaseLock");
            return com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.releaseLockInternal(clientId, 500L, false, false);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
            android.util.IndentingPrintWriter pw = new android.util.IndentingPrintWriter(writer, "  ");
            if (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getContext().checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
                pw.println("Permission Denial: can't dump!");
                return;
            }
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mClientProfiles, "ClientProfiles:", "\n", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendResources, "FrontendResources:", "\n", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpSIA(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendExistingNums, "FrontendExistingNums:", ", ", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpSIA(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendUsedNums, "FrontendUsedNums:", ", ", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpSIA(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendMaxUsableNums, "FrontendMaxUsableNums:", ", ", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendResourcesBackup, "FrontendResourcesBackUp:", "\n", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpSIA(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendExistingNumsBackup, "FrontendExistingNumsBackup:", ", ", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpSIA(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendUsedNumsBackup, "FrontendUsedNumsBackup:", ", ", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpSIA(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mFrontendMaxUsableNumsBackup, "FrontendUsedNumsBackup:", ", ", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mDemuxResources, "DemuxResource:", "\n", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLnbResources, "LnbResource:", "\n", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mCasResources, "CasResource:", "\n", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mCiCamResources, "CiCamResource:", "\n", pw);
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.dumpMap(com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mListeners, "Listners:", "\n", pw);
            }
        }

        public int getClientPriority(int useCase, int pid) throws android.os.RemoteException {
            int clientPriority;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("getClientPriority");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                clientPriority = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getClientPriority(useCase, com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkIsForeground(pid));
            }
            return clientPriority;
        }

        public int getConfigPriority(int useCase, boolean isForeground) throws android.os.RemoteException {
            int clientPriority;
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.enforceTrmAccessPermission("getConfigPriority");
            synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                clientPriority = com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.getClientPriority(useCase, isForeground);
            }
            return clientPriority;
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        if (DEBUG) {
            android.util.Slog.w(TAG, "Native media resource manager service has died");
        }
        synchronized (this.mLock) {
            this.mMediaResourceManager = null;
        }
    }

    protected void registerClientProfileInternal(android.media.tv.tunerresourcemanager.ResourceClientProfile profile, android.media.tv.tunerresourcemanager.IResourcesReclaimListener listener, int[] clientId) {
        int pid;
        if (DEBUG) {
            android.util.Slog.d(TAG, "registerClientProfile(clientProfile=" + profile + ")");
        }
        clientId[0] = -1;
        if (this.mTvInputManager == null) {
            android.util.Slog.e(TAG, "TvInputManager is null. Can't register client profile.");
            return;
        }
        int i = this.mNextUnusedClientId;
        this.mNextUnusedClientId = i + 1;
        clientId[0] = i;
        if (profile.tvInputSessionId == null) {
            pid = android.os.Binder.getCallingPid();
        } else {
            pid = this.mTvInputManager.getClientPid(profile.tvInputSessionId);
        }
        if (profile.tvInputSessionId != null && this.mMediaResourceManager != null) {
            try {
                this.mMediaResourceManager.overridePid(android.os.Binder.getCallingPid(), pid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Could not overridePid in resourceManagerSercice, remote exception: " + e);
            }
        }
        com.android.server.tv.tunerresourcemanager.ClientProfile clientProfile = new com.android.server.tv.tunerresourcemanager.ClientProfile.Builder(clientId[0]).tvInputSessionId(profile.tvInputSessionId).useCase(profile.useCase).processId(pid).build();
        clientProfile.setPriority(getClientPriority(profile.useCase, checkIsForeground(pid)));
        addClientProfile(clientId[0], clientProfile, listener);
    }

    protected void unregisterClientProfileInternal(int clientId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "unregisterClientProfile(clientId=" + clientId + ")");
        }
        removeClientProfile(clientId);
        if (this.mMediaResourceManager != null) {
            try {
                this.mMediaResourceManager.overridePid(android.os.Binder.getCallingPid(), -1);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Could not overridePid in resourceManagerSercice when unregister, remote exception: " + e);
            }
        }
    }

    protected boolean updateClientPriorityInternal(int clientId, int priority, int niceValue) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateClientPriority(clientId=" + clientId + ", priority=" + priority + ", niceValue=" + niceValue + ")");
        }
        com.android.server.tv.tunerresourcemanager.ClientProfile profile = getClientProfile(clientId);
        if (profile == null) {
            android.util.Slog.e(TAG, "Can not find client profile with id " + clientId + " when trying to update the client priority.");
            return false;
        }
        profile.overwritePriority(priority);
        profile.setNiceValue(niceValue);
        return true;
    }

    protected boolean hasUnusedFrontendInternal(int frontendType) {
        for (com.android.server.tv.tunerresourcemanager.FrontendResource fr : getFrontendResources().values()) {
            if (fr.getType() == frontendType && !fr.isInUse()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isLowestPriorityInternal(int clientId, int frontendType) throws android.os.RemoteException {
        com.android.server.tv.tunerresourcemanager.ClientProfile requestClient = getClientProfile(clientId);
        if (requestClient == null) {
            return true;
        }
        clientPriorityUpdateOnRequest(requestClient);
        int clientPriority = requestClient.getPriority();
        for (com.android.server.tv.tunerresourcemanager.FrontendResource fr : getFrontendResources().values()) {
            if (fr.getType() == frontendType && fr.isInUse()) {
                int priority = updateAndGetOwnerClientPriority(fr.getOwnerClientId());
                if (clientPriority > priority) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void storeResourceMapInternal(int resourceType) {
        switch (resourceType) {
            case 0:
                replaceFeResourceMap(this.mFrontendResources, this.mFrontendResourcesBackup);
                replaceFeCounts(this.mFrontendExistingNums, this.mFrontendExistingNumsBackup);
                replaceFeCounts(this.mFrontendUsedNums, this.mFrontendUsedNumsBackup);
                replaceFeCounts(this.mFrontendMaxUsableNums, this.mFrontendMaxUsableNumsBackup);
                break;
        }
    }

    protected void clearResourceMapInternal(int resourceType) {
        switch (resourceType) {
            case 0:
                replaceFeResourceMap(null, this.mFrontendResources);
                replaceFeCounts(null, this.mFrontendExistingNums);
                replaceFeCounts(null, this.mFrontendUsedNums);
                replaceFeCounts(null, this.mFrontendMaxUsableNums);
                break;
        }
    }

    protected void restoreResourceMapInternal(int resourceType) {
        switch (resourceType) {
            case 0:
                replaceFeResourceMap(this.mFrontendResourcesBackup, this.mFrontendResources);
                replaceFeCounts(this.mFrontendExistingNumsBackup, this.mFrontendExistingNums);
                replaceFeCounts(this.mFrontendUsedNumsBackup, this.mFrontendUsedNums);
                replaceFeCounts(this.mFrontendMaxUsableNumsBackup, this.mFrontendMaxUsableNums);
                break;
        }
    }

    protected void setFrontendInfoListInternal(android.media.tv.tunerresourcemanager.TunerFrontendInfo[] infos) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateFrontendInfo:");
            for (android.media.tv.tunerresourcemanager.TunerFrontendInfo tunerFrontendInfo : infos) {
                android.util.Slog.d(TAG, tunerFrontendInfo.toString());
            }
        }
        java.util.Set<java.lang.Integer> updatingFrontendHandles = new java.util.HashSet<>(getFrontendResources().keySet());
        for (int i = 0; i < infos.length; i++) {
            if (getFrontendResource(infos[i].handle) != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Frontend handle=" + infos[i].handle + "exists.");
                }
                updatingFrontendHandles.remove(java.lang.Integer.valueOf(infos[i].handle));
            } else {
                com.android.server.tv.tunerresourcemanager.FrontendResource newFe = new com.android.server.tv.tunerresourcemanager.FrontendResource.Builder(infos[i].handle).type(infos[i].type).exclusiveGroupId(infos[i].exclusiveGroupId).build();
                addFrontendResource(newFe);
            }
        }
        java.util.Iterator<java.lang.Integer> it = updatingFrontendHandles.iterator();
        while (it.hasNext()) {
            int removingHandle = it.next().intValue();
            removeFrontendResource(removingHandle);
        }
    }

    protected void setDemuxInfoListInternal(android.media.tv.tunerresourcemanager.TunerDemuxInfo[] infos) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateDemuxInfo:");
            for (android.media.tv.tunerresourcemanager.TunerDemuxInfo tunerDemuxInfo : infos) {
                android.util.Slog.d(TAG, tunerDemuxInfo.toString());
            }
        }
        java.util.Set<java.lang.Integer> updatingDemuxHandles = new java.util.HashSet<>(getDemuxResources().keySet());
        for (int i = 0; i < infos.length; i++) {
            if (getDemuxResource(infos[i].handle) != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Demux handle=" + infos[i].handle + "exists.");
                }
                updatingDemuxHandles.remove(java.lang.Integer.valueOf(infos[i].handle));
            } else {
                com.android.server.tv.tunerresourcemanager.DemuxResource newDemux = new com.android.server.tv.tunerresourcemanager.DemuxResource.Builder(infos[i].handle).filterTypes(infos[i].filterTypes).build();
                addDemuxResource(newDemux);
            }
        }
        java.util.Iterator<java.lang.Integer> it = updatingDemuxHandles.iterator();
        while (it.hasNext()) {
            int removingHandle = it.next().intValue();
            removeDemuxResource(removingHandle);
        }
    }

    protected void setLnbInfoListInternal(int[] lnbHandles) {
        if (DEBUG) {
            for (int i : lnbHandles) {
                android.util.Slog.d(TAG, "updateLnbInfo(lnbHanle=" + i + ")");
            }
        }
        java.util.Set<java.lang.Integer> updatingLnbHandles = new java.util.HashSet<>(getLnbResources().keySet());
        for (int i2 = 0; i2 < lnbHandles.length; i2++) {
            if (getLnbResource(lnbHandles[i2]) != null) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Lnb handle=" + lnbHandles[i2] + "exists.");
                }
                updatingLnbHandles.remove(java.lang.Integer.valueOf(lnbHandles[i2]));
            } else {
                com.android.server.tv.tunerresourcemanager.LnbResource newLnb = new com.android.server.tv.tunerresourcemanager.LnbResource.Builder(lnbHandles[i2]).build();
                addLnbResource(newLnb);
            }
        }
        java.util.Iterator<java.lang.Integer> it = updatingLnbHandles.iterator();
        while (it.hasNext()) {
            int removingHandle = it.next().intValue();
            removeLnbResource(removingHandle);
        }
    }

    protected void updateCasInfoInternal(int casSystemId, int maxSessionNum) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateCasInfo(casSystemId=" + casSystemId + ", maxSessionNum=" + maxSessionNum + ")");
        }
        if (maxSessionNum == 0) {
            removeCasResource(casSystemId);
            removeCiCamResource(casSystemId);
            return;
        }
        com.android.server.tv.tunerresourcemanager.CasResource cas = getCasResource(casSystemId);
        com.android.server.tv.tunerresourcemanager.CiCamResource ciCam = getCiCamResource(casSystemId);
        if (cas != null) {
            if (cas.getUsedSessionNum() > maxSessionNum) {
                cas.getUsedSessionNum();
            }
            cas.updateMaxSessionNum(maxSessionNum);
            if (ciCam != null) {
                ciCam.updateMaxSessionNum(maxSessionNum);
                return;
            }
            return;
        }
        com.android.server.tv.tunerresourcemanager.CasResource cas2 = new com.android.server.tv.tunerresourcemanager.CasResource.Builder(casSystemId).maxSessionNum(maxSessionNum).build();
        com.android.server.tv.tunerresourcemanager.CiCamResource ciCam2 = new com.android.server.tv.tunerresourcemanager.CiCamResource.Builder(casSystemId).maxSessionNum(maxSessionNum).build();
        addCasResource(cas2);
        addCiCamResource(ciCam2);
    }

    protected boolean requestFrontendInternal(android.media.tv.tunerresourcemanager.TunerFrontendRequest request, int[] frontendHandle) {
        int priority;
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestFrontend(request=" + request + ")");
        }
        int i = -1;
        frontendHandle[0] = -1;
        com.android.server.tv.tunerresourcemanager.ClientProfile requestClient = getClientProfile(request.clientId);
        if (requestClient == null) {
            return false;
        }
        clientPriorityUpdateOnRequest(requestClient);
        int grantingFrontendHandle = -1;
        int inUseLowestPriorityFrHandle = -1;
        int currentLowestPriority = 1001;
        boolean isRequestFromSameProcess = false;
        boolean hasDesiredFrontend = request.desiredId != -1;
        java.util.Iterator<com.android.server.tv.tunerresourcemanager.FrontendResource> it = getFrontendResources().values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.tv.tunerresourcemanager.FrontendResource fr = it.next();
            int frontendId = getResourceIdFromHandle(fr.getHandle());
            if (fr.getType() == request.frontendType && (!hasDesiredFrontend || frontendId == request.desiredId)) {
                if (!fr.isInUse()) {
                    if (isFrontendMaxNumUseReached(request.frontendType)) {
                        i = -1;
                    } else {
                        if (fr.getExclusiveGroupMemberFeHandles().isEmpty()) {
                            grantingFrontendHandle = fr.getHandle();
                            break;
                        }
                        if (grantingFrontendHandle == i) {
                            grantingFrontendHandle = fr.getHandle();
                        }
                    }
                } else if (grantingFrontendHandle == i && currentLowestPriority > (priority = getFrontendHighestClientPriority(fr.getOwnerClientId()))) {
                    com.android.server.tv.tunerresourcemanager.ClientProfile targetOwnerProfile = getClientProfile(fr.getOwnerClientId());
                    int primaryFeId = targetOwnerProfile.getPrimaryFrontend();
                    com.android.server.tv.tunerresourcemanager.FrontendResource primaryFe = getFrontendResource(primaryFeId);
                    if (fr.getType() == primaryFe.getType() || !isFrontendMaxNumUseReached(fr.getType())) {
                        int inUseLowestPriorityFrHandle2 = fr.getHandle();
                        currentLowestPriority = priority;
                        isRequestFromSameProcess = requestClient.getProcessId() == getClientProfile(fr.getOwnerClientId()).getProcessId();
                        inUseLowestPriorityFrHandle = inUseLowestPriorityFrHandle2;
                    } else {
                        i = -1;
                    }
                }
            }
            i = -1;
        }
        if (grantingFrontendHandle != -1) {
            frontendHandle[0] = grantingFrontendHandle;
            updateFrontendClientMappingOnNewGrant(grantingFrontendHandle, request.clientId);
            return true;
        }
        if (inUseLowestPriorityFrHandle == -1) {
            return false;
        }
        if ((requestClient.getPriority() <= currentLowestPriority && (requestClient.getPriority() != currentLowestPriority || !isRequestFromSameProcess)) || !reclaimResource(getFrontendResource(inUseLowestPriorityFrHandle).getOwnerClientId(), 0)) {
            return false;
        }
        frontendHandle[0] = inUseLowestPriorityFrHandle;
        updateFrontendClientMappingOnNewGrant(inUseLowestPriorityFrHandle, request.clientId);
        return true;
    }

    protected void shareFrontendInternal(int selfClientId, int targetClientId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "shareFrontend from " + selfClientId + " with " + targetClientId);
        }
        java.lang.Integer shareeFeClientId = getClientProfile(selfClientId).getShareeFeClientId();
        if (shareeFeClientId.intValue() != -1) {
            getClientProfile(shareeFeClientId.intValue()).stopSharingFrontend(selfClientId);
            getClientProfile(selfClientId).releaseFrontend();
        }
        java.util.Iterator<java.lang.Integer> it = getClientProfile(targetClientId).getInUseFrontendHandles().iterator();
        while (it.hasNext()) {
            int feId = it.next().intValue();
            getClientProfile(selfClientId).useFrontend(feId);
        }
        getClientProfile(selfClientId).setShareeFeClientId(java.lang.Integer.valueOf(targetClientId));
        getClientProfile(targetClientId).shareFrontend(selfClientId);
    }

    private boolean transferFeOwner(int currentOwnerId, int newOwnerId) {
        com.android.server.tv.tunerresourcemanager.ClientProfile currentOwnerProfile = getClientProfile(currentOwnerId);
        com.android.server.tv.tunerresourcemanager.ClientProfile newOwnerProfile = getClientProfile(newOwnerId);
        newOwnerProfile.shareFrontend(currentOwnerId);
        currentOwnerProfile.stopSharingFrontend(newOwnerId);
        newOwnerProfile.setShareeFeClientId(-1);
        currentOwnerProfile.setShareeFeClientId(java.lang.Integer.valueOf(newOwnerId));
        java.util.Iterator<java.lang.Integer> it = newOwnerProfile.getInUseFrontendHandles().iterator();
        while (it.hasNext()) {
            getFrontendResource(it.next().intValue()).setOwner(newOwnerId);
        }
        newOwnerProfile.setPrimaryFrontend(currentOwnerProfile.getPrimaryFrontend());
        currentOwnerProfile.setPrimaryFrontend(-1);
        java.util.Iterator<java.lang.Integer> it2 = currentOwnerProfile.getInUseFrontendHandles().iterator();
        while (it2.hasNext()) {
            int inUseHandle = it2.next().intValue();
            int ownerId = getFrontendResource(inUseHandle).getOwnerClientId();
            if (ownerId != newOwnerId) {
                android.util.Slog.e(TAG, "something is wrong in transferFeOwner:" + inUseHandle + ", " + ownerId + ", " + newOwnerId);
                return false;
            }
        }
        return true;
    }

    private boolean transferFeCiCamOwner(int currentOwnerId, int newOwnerId) {
        com.android.server.tv.tunerresourcemanager.ClientProfile currentOwnerProfile = getClientProfile(currentOwnerId);
        com.android.server.tv.tunerresourcemanager.ClientProfile newOwnerProfile = getClientProfile(newOwnerId);
        int ciCamId = currentOwnerProfile.getInUseCiCamId();
        newOwnerProfile.useCiCam(ciCamId);
        com.android.server.tv.tunerresourcemanager.CiCamResource ciCam = getCiCamResource(ciCamId);
        ciCam.setOwner(newOwnerId);
        currentOwnerProfile.releaseCiCam();
        return true;
    }

    private boolean transferLnbOwner(int currentOwnerId, int newOwnerId) {
        com.android.server.tv.tunerresourcemanager.ClientProfile currentOwnerProfile = getClientProfile(currentOwnerId);
        com.android.server.tv.tunerresourcemanager.ClientProfile newOwnerProfile = getClientProfile(newOwnerId);
        java.util.Set<java.lang.Integer> inUseLnbHandles = new java.util.HashSet<>();
        for (java.lang.Integer lnbHandle : currentOwnerProfile.getInUseLnbHandles()) {
            newOwnerProfile.useLnb(lnbHandle.intValue());
            com.android.server.tv.tunerresourcemanager.LnbResource lnb = getLnbResource(lnbHandle.intValue());
            lnb.setOwner(newOwnerId);
            inUseLnbHandles.add(lnbHandle);
        }
        java.util.Iterator<java.lang.Integer> it = inUseLnbHandles.iterator();
        while (it.hasNext()) {
            currentOwnerProfile.releaseLnb(it.next().intValue());
        }
        return true;
    }

    protected boolean transferOwnerInternal(int resourceType, int currentOwnerId, int newOwnerId) {
        switch (resourceType) {
            case 0:
                return transferFeOwner(currentOwnerId, newOwnerId);
            case 3:
                return transferLnbOwner(currentOwnerId, newOwnerId);
            case 5:
                return transferFeCiCamOwner(currentOwnerId, newOwnerId);
            default:
                android.util.Slog.e(TAG, "transferOwnerInternal. unsupported resourceType: " + resourceType);
                return false;
        }
    }

    protected boolean requestLnbInternal(android.media.tv.tunerresourcemanager.TunerLnbRequest request, int[] lnbHandle) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestLnb(request=" + request + ")");
        }
        lnbHandle[0] = -1;
        com.android.server.tv.tunerresourcemanager.ClientProfile requestClient = getClientProfile(request.clientId);
        clientPriorityUpdateOnRequest(requestClient);
        int grantingLnbHandle = -1;
        int inUseLowestPriorityLnbHandle = -1;
        int currentLowestPriority = 1001;
        boolean isRequestFromSameProcess = false;
        java.util.Iterator<com.android.server.tv.tunerresourcemanager.LnbResource> it = getLnbResources().values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.tv.tunerresourcemanager.LnbResource lnb = it.next();
            if (!lnb.isInUse()) {
                grantingLnbHandle = lnb.getHandle();
                break;
            }
            int priority = updateAndGetOwnerClientPriority(lnb.getOwnerClientId());
            if (currentLowestPriority > priority) {
                inUseLowestPriorityLnbHandle = lnb.getHandle();
                currentLowestPriority = priority;
                isRequestFromSameProcess = requestClient.getProcessId() == getClientProfile(lnb.getOwnerClientId()).getProcessId();
            }
        }
        if (grantingLnbHandle > -1) {
            lnbHandle[0] = grantingLnbHandle;
            updateLnbClientMappingOnNewGrant(grantingLnbHandle, request.clientId);
            return true;
        }
        if (inUseLowestPriorityLnbHandle <= -1 || ((requestClient.getPriority() <= currentLowestPriority && (requestClient.getPriority() != currentLowestPriority || !isRequestFromSameProcess)) || !reclaimResource(getLnbResource(inUseLowestPriorityLnbHandle).getOwnerClientId(), 3))) {
            return false;
        }
        lnbHandle[0] = inUseLowestPriorityLnbHandle;
        updateLnbClientMappingOnNewGrant(inUseLowestPriorityLnbHandle, request.clientId);
        return true;
    }

    protected boolean requestCasSessionInternal(android.media.tv.tunerresourcemanager.CasSessionRequest request, int[] casSessionHandle) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestCasSession(request=" + request + ")");
        }
        com.android.server.tv.tunerresourcemanager.CasResource cas = getCasResource(request.casSystemId);
        if (cas == null) {
            cas = new com.android.server.tv.tunerresourcemanager.CasResource.Builder(request.casSystemId).maxSessionNum(Integer.MAX_VALUE).build();
            addCasResource(cas);
        }
        casSessionHandle[0] = -1;
        com.android.server.tv.tunerresourcemanager.ClientProfile requestClient = getClientProfile(request.clientId);
        clientPriorityUpdateOnRequest(requestClient);
        int lowestPriorityOwnerId = -1;
        int currentLowestPriority = 1001;
        boolean isRequestFromSameProcess = false;
        if (!cas.isFullyUsed()) {
            casSessionHandle[0] = generateResourceHandle(4, cas.getSystemId());
            updateCasClientMappingOnNewGrant(request.casSystemId, request.clientId);
            return true;
        }
        java.util.Iterator<java.lang.Integer> it = cas.getOwnerClientIds().iterator();
        while (it.hasNext()) {
            int ownerId = it.next().intValue();
            int priority = updateAndGetOwnerClientPriority(ownerId);
            if (currentLowestPriority > priority) {
                lowestPriorityOwnerId = ownerId;
                currentLowestPriority = priority;
                isRequestFromSameProcess = requestClient.getProcessId() == getClientProfile(ownerId).getProcessId();
            }
        }
        if (lowestPriorityOwnerId <= -1 || ((requestClient.getPriority() <= currentLowestPriority && (requestClient.getPriority() != currentLowestPriority || !isRequestFromSameProcess)) || !reclaimResource(lowestPriorityOwnerId, 4))) {
            return false;
        }
        casSessionHandle[0] = generateResourceHandle(4, cas.getSystemId());
        updateCasClientMappingOnNewGrant(request.casSystemId, request.clientId);
        return true;
    }

    protected boolean requestCiCamInternal(android.media.tv.tunerresourcemanager.TunerCiCamRequest request, int[] ciCamHandle) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestCiCamInternal(TunerCiCamRequest=" + request + ")");
        }
        com.android.server.tv.tunerresourcemanager.CiCamResource ciCam = getCiCamResource(request.ciCamId);
        if (ciCam == null) {
            ciCam = new com.android.server.tv.tunerresourcemanager.CiCamResource.Builder(request.ciCamId).maxSessionNum(Integer.MAX_VALUE).build();
            addCiCamResource(ciCam);
        }
        ciCamHandle[0] = -1;
        com.android.server.tv.tunerresourcemanager.ClientProfile requestClient = getClientProfile(request.clientId);
        clientPriorityUpdateOnRequest(requestClient);
        int lowestPriorityOwnerId = -1;
        int currentLowestPriority = 1001;
        boolean isRequestFromSameProcess = false;
        if (!ciCam.isFullyUsed()) {
            ciCamHandle[0] = generateResourceHandle(5, ciCam.getCiCamId());
            updateCiCamClientMappingOnNewGrant(request.ciCamId, request.clientId);
            return true;
        }
        java.util.Iterator<java.lang.Integer> it = ciCam.getOwnerClientIds().iterator();
        while (it.hasNext()) {
            int ownerId = it.next().intValue();
            int priority = updateAndGetOwnerClientPriority(ownerId);
            if (currentLowestPriority > priority) {
                lowestPriorityOwnerId = ownerId;
                currentLowestPriority = priority;
                isRequestFromSameProcess = requestClient.getProcessId() == getClientProfile(ownerId).getProcessId();
            }
        }
        if (lowestPriorityOwnerId <= -1 || ((requestClient.getPriority() <= currentLowestPriority && (requestClient.getPriority() != currentLowestPriority || !isRequestFromSameProcess)) || !reclaimResource(lowestPriorityOwnerId, 5))) {
            return false;
        }
        ciCamHandle[0] = generateResourceHandle(5, ciCam.getCiCamId());
        updateCiCamClientMappingOnNewGrant(request.ciCamId, request.clientId);
        return true;
    }

    protected boolean isHigherPriorityInternal(android.media.tv.tunerresourcemanager.ResourceClientProfile challengerProfile, android.media.tv.tunerresourcemanager.ResourceClientProfile holderProfile) {
        int challengerPid;
        int holderPid;
        if (DEBUG) {
            android.util.Slog.d(TAG, "isHigherPriority(challengerProfile=" + challengerProfile + ", holderProfile=" + challengerProfile + ")");
        }
        if (this.mTvInputManager == null) {
            android.util.Slog.e(TAG, "TvInputManager is null. Can't compare the priority.");
            return true;
        }
        if (challengerProfile.tvInputSessionId == null) {
            challengerPid = android.os.Binder.getCallingPid();
        } else {
            challengerPid = this.mTvInputManager.getClientPid(challengerProfile.tvInputSessionId);
        }
        if (holderProfile.tvInputSessionId == null) {
            holderPid = android.os.Binder.getCallingPid();
        } else {
            holderPid = this.mTvInputManager.getClientPid(holderProfile.tvInputSessionId);
        }
        int challengerPriority = getClientPriority(challengerProfile.useCase, checkIsForeground(challengerPid));
        int holderPriority = getClientPriority(holderProfile.useCase, checkIsForeground(holderPid));
        return challengerPriority > holderPriority;
    }

    protected void releaseFrontendInternal(com.android.server.tv.tunerresourcemanager.FrontendResource fe, int clientId) {
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerClient;
        if (DEBUG) {
            android.util.Slog.d(TAG, "releaseFrontend(id=" + fe.getHandle() + ", clientId=" + clientId + " )");
        }
        if (clientId == fe.getOwnerClientId() && (ownerClient = getClientProfile(fe.getOwnerClientId())) != null) {
            java.util.Iterator<java.lang.Integer> it = ownerClient.getShareFeClientIds().iterator();
            while (it.hasNext()) {
                int shareOwnerId = it.next().intValue();
                reclaimResource(shareOwnerId, 0);
            }
        }
        clearFrontendAndClientMapping(getClientProfile(clientId));
    }

    protected void releaseDemuxInternal(com.android.server.tv.tunerresourcemanager.DemuxResource demux) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "releaseDemux(DemuxHandle=" + demux.getHandle() + ")");
        }
        updateDemuxClientMappingOnRelease(demux);
    }

    protected void releaseLnbInternal(com.android.server.tv.tunerresourcemanager.LnbResource lnb) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "releaseLnb(lnbHandle=" + lnb.getHandle() + ")");
        }
        updateLnbClientMappingOnRelease(lnb);
    }

    protected void releaseCasSessionInternal(com.android.server.tv.tunerresourcemanager.CasResource cas, int ownerClientId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "releaseCasSession(sessionResourceId=" + cas.getSystemId() + ")");
        }
        updateCasClientMappingOnRelease(cas, ownerClientId);
    }

    protected void releaseCiCamInternal(com.android.server.tv.tunerresourcemanager.CiCamResource ciCam, int ownerClientId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "releaseCiCamInternal(ciCamId=" + ciCam.getCiCamId() + ")");
        }
        updateCiCamClientMappingOnRelease(ciCam, ownerClientId);
    }

    protected boolean requestDemuxInternal(android.media.tv.tunerresourcemanager.TunerDemuxRequest request, int[] demuxHandle) {
        int priority;
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestDemux(request=" + request + ")");
        }
        if (this.mDemuxResources.size() == 0) {
            demuxHandle[0] = generateResourceHandle(1, 0);
            return true;
        }
        int i = -1;
        demuxHandle[0] = -1;
        com.android.server.tv.tunerresourcemanager.ClientProfile requestClient = getClientProfile(request.clientId);
        if (requestClient == null) {
            return false;
        }
        clientPriorityUpdateOnRequest(requestClient);
        int grantingDemuxHandle = -1;
        int inUseLowestPriorityDrHandle = -1;
        int currentLowestPriority = 1001;
        boolean isRequestFromSameProcess = false;
        boolean hasDesiredDemuxCap = request.desiredFilterTypes != 0;
        int smallestNumOfSupportedCaps = 33;
        int smallestNumOfSupportedCapsInUse = 33;
        for (com.android.server.tv.tunerresourcemanager.DemuxResource dr : getDemuxResources().values()) {
            if (!hasDesiredDemuxCap || dr.hasSufficientCaps(request.desiredFilterTypes)) {
                if (!dr.isInUse()) {
                    int numOfSupportedCaps = dr.getNumOfCaps();
                    if (smallestNumOfSupportedCaps > numOfSupportedCaps) {
                        smallestNumOfSupportedCaps = numOfSupportedCaps;
                        grantingDemuxHandle = dr.getHandle();
                    }
                } else if (grantingDemuxHandle == i && currentLowestPriority >= (priority = updateAndGetOwnerClientPriority(dr.getOwnerClientId()))) {
                    int numOfSupportedCaps2 = dr.getNumOfCaps();
                    boolean shouldUpdate = false;
                    if (currentLowestPriority > priority) {
                        currentLowestPriority = priority;
                        isRequestFromSameProcess = requestClient.getProcessId() == getClientProfile(dr.getOwnerClientId()).getProcessId();
                        smallestNumOfSupportedCapsInUse = numOfSupportedCaps2;
                        shouldUpdate = true;
                    } else if (smallestNumOfSupportedCapsInUse > numOfSupportedCaps2) {
                        smallestNumOfSupportedCapsInUse = numOfSupportedCaps2;
                        shouldUpdate = true;
                    }
                    if (shouldUpdate) {
                        inUseLowestPriorityDrHandle = dr.getHandle();
                    }
                }
            }
            i = -1;
        }
        if (grantingDemuxHandle != -1) {
            demuxHandle[0] = grantingDemuxHandle;
            updateDemuxClientMappingOnNewGrant(grantingDemuxHandle, request.clientId);
            return true;
        }
        if (inUseLowestPriorityDrHandle == -1) {
            return false;
        }
        if ((requestClient.getPriority() <= currentLowestPriority && (requestClient.getPriority() != currentLowestPriority || !isRequestFromSameProcess)) || !reclaimResource(getDemuxResource(inUseLowestPriorityDrHandle).getOwnerClientId(), 1)) {
            return false;
        }
        demuxHandle[0] = inUseLowestPriorityDrHandle;
        updateDemuxClientMappingOnNewGrant(inUseLowestPriorityDrHandle, request.clientId);
        return true;
    }

    protected void clientPriorityUpdateOnRequest(com.android.server.tv.tunerresourcemanager.ClientProfile profile) {
        if (profile.isPriorityOverwritten()) {
            return;
        }
        int pid = profile.getProcessId();
        boolean currentIsForeground = checkIsForeground(pid);
        profile.setPriority(getClientPriority(profile.getUseCase(), currentIsForeground));
    }

    protected boolean requestDescramblerInternal(android.media.tv.tunerresourcemanager.TunerDescramblerRequest request, int[] descramblerHandle) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "requestDescrambler(request=" + request + ")");
        }
        descramblerHandle[0] = generateResourceHandle(2, 0);
        return true;
    }

    private long getElapsedTime(long begin) {
        long now = android.os.SystemClock.uptimeMillis();
        if (now >= begin) {
            return now - begin;
        }
        long elapsed = (Long.MAX_VALUE - begin) + now;
        if (elapsed < 0) {
            return Long.MAX_VALUE;
        }
        return elapsed;
    }

    private boolean lockForTunerApiLock(int clientId, long timeoutMS, java.lang.String callerFunction) {
        try {
            if (!this.mLockForTRMSLock.tryLock(timeoutMS, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                android.util.Slog.e(TAG, "FAILED to lock mLockForTRMSLock in " + callerFunction + ", clientId:" + clientId + ", timeoutMS:" + timeoutMS + ", mTunerApiLockHolder:" + this.mTunerApiLockHolder);
                return false;
            }
            return true;
        } catch (java.lang.InterruptedException ie) {
            android.util.Slog.e(TAG, "exception thrown in " + callerFunction + ":" + ie);
            if (this.mLockForTRMSLock.isHeldByCurrentThread()) {
                this.mLockForTRMSLock.unlock();
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Not found exit edge by exit block: B:22:0x003e
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.checkLoopExits(LoopRegionMaker.java:226)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.makeLoopRegion(LoopRegionMaker.java:196)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:63)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public boolean acquireLockInternal(int r22, long r23, long r25) {
        /*
            Method dump skipped, instruction units count: 586
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.acquireLockInternal(int, long, long):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean releaseLockInternal(int clientId, long timeoutMS, boolean ignoreNestedCount, boolean suppressError) {
        if (!lockForTunerApiLock(clientId, timeoutMS, "releaseLockInternal()")) {
            return false;
        }
        try {
            if (this.mTunerApiLockHolder != clientId) {
                if (this.mTunerApiLockHolder == -1) {
                    if (!suppressError) {
                        android.util.Slog.w(TAG, "releaseLockInternal(" + clientId + ", " + timeoutMS + ") - called while there is no current holder");
                    }
                    if (this.mLockForTRMSLock.isHeldByCurrentThread()) {
                        this.mLockForTRMSLock.unlock();
                    }
                    return false;
                }
                if (!suppressError) {
                    android.util.Slog.e(TAG, "releaseLockInternal(" + clientId + ", " + timeoutMS + ") - called while someone else:" + this.mTunerApiLockHolder + "is the current holder");
                }
                if (this.mLockForTRMSLock.isHeldByCurrentThread()) {
                    this.mLockForTRMSLock.unlock();
                }
                return false;
            }
            this.mTunerApiLockNestedCount--;
            if (ignoreNestedCount || this.mTunerApiLockNestedCount <= 0) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "SUCCESS:releaseLockInternal(" + clientId + ", " + timeoutMS + ", " + ignoreNestedCount + ", " + suppressError + ") - signaling!");
                }
                this.mTunerApiLockHolder = -1;
                this.mTunerApiLockHolderThreadId = -1L;
                this.mTunerApiLockNestedCount = 0;
                this.mTunerApiLockReleasedCV.signal();
            } else if (DEBUG) {
                android.util.Slog.d(TAG, "releaseLockInternal(" + clientId + ", " + timeoutMS + ", " + ignoreNestedCount + ", " + suppressError + ") - NOT signaling because nested count is not zero (" + this.mTunerApiLockNestedCount + ")");
            }
            return true;
        } finally {
            if (this.mLockForTRMSLock.isHeldByCurrentThread()) {
                this.mLockForTRMSLock.unlock();
            }
        }
    }

    protected class ResourcesReclaimListenerRecord implements android.os.IBinder.DeathRecipient {
        private final int mClientId;
        private final android.media.tv.tunerresourcemanager.IResourcesReclaimListener mListener;

        public ResourcesReclaimListenerRecord(android.media.tv.tunerresourcemanager.IResourcesReclaimListener listener, int clientId) {
            this.mListener = listener;
            this.mClientId = clientId;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                synchronized (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.mLock) {
                    if (com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.checkClientExists(this.mClientId)) {
                        com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.removeClientProfile(this.mClientId);
                    }
                }
            } finally {
                com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.this.releaseLockInternal(this.mClientId, 500L, true, true);
            }
        }

        public int getId() {
            return this.mClientId;
        }

        public android.media.tv.tunerresourcemanager.IResourcesReclaimListener getListener() {
            return this.mListener;
        }
    }

    private void addResourcesReclaimListener(int clientId, android.media.tv.tunerresourcemanager.IResourcesReclaimListener listener) {
        if (listener == null) {
            if (DEBUG) {
                android.util.Slog.w(TAG, "Listener is null when client " + clientId + " registered!");
            }
        } else {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.ResourcesReclaimListenerRecord record = new com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.ResourcesReclaimListenerRecord(listener, clientId);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mListeners.put(java.lang.Integer.valueOf(clientId), record);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Listener already died.");
            }
        }
    }

    protected boolean reclaimResource(int reclaimingClientId, int resourceType) {
        android.os.Binder.allowBlockingForCurrentThread();
        com.android.server.tv.tunerresourcemanager.ClientProfile profile = getClientProfile(reclaimingClientId);
        if (profile == null) {
            return true;
        }
        java.util.Set<java.lang.Integer> shareFeClientIds = profile.getShareFeClientIds();
        java.util.Iterator<java.lang.Integer> it = shareFeClientIds.iterator();
        while (it.hasNext()) {
            int clientId = it.next().intValue();
            try {
                this.mListeners.get(java.lang.Integer.valueOf(clientId)).getListener().onReclaimResources();
                clearAllResourcesAndClientMapping(getClientProfile(clientId));
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Failed to reclaim resources on client " + clientId, e);
                return false;
            }
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Reclaiming resources because higher priority client request resource type " + resourceType + ", clientId:" + reclaimingClientId);
        }
        try {
            this.mListeners.get(java.lang.Integer.valueOf(reclaimingClientId)).getListener().onReclaimResources();
            clearAllResourcesAndClientMapping(profile);
            return true;
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(TAG, "Failed to reclaim resources on client " + reclaimingClientId, e2);
            return false;
        }
    }

    protected int getClientPriority(int useCase, boolean isForeground) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "getClientPriority useCase=" + useCase + ", isForeground=" + isForeground + ")");
        }
        if (isForeground) {
            return this.mPriorityCongfig.getForegroundPriority(useCase);
        }
        return this.mPriorityCongfig.getBackgroundPriority(useCase);
    }

    protected boolean checkIsForeground(int pid) {
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses;
        if (this.mActivityManager == null || (appProcesses = this.mActivityManager.getRunningAppProcesses()) == null) {
            return false;
        }
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.pid == pid && appProcess.importance == 100) {
                return true;
            }
        }
        return false;
    }

    private void updateFrontendClientMappingOnNewGrant(int grantingHandle, int ownerClientId) {
        com.android.server.tv.tunerresourcemanager.FrontendResource grantingFrontend = getFrontendResource(grantingHandle);
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(ownerClientId);
        grantingFrontend.setOwner(ownerClientId);
        increFrontendNum(this.mFrontendUsedNums, grantingFrontend.getType());
        ownerProfile.useFrontend(grantingHandle);
        java.util.Iterator<java.lang.Integer> it = grantingFrontend.getExclusiveGroupMemberFeHandles().iterator();
        while (it.hasNext()) {
            int exclusiveGroupMember = it.next().intValue();
            getFrontendResource(exclusiveGroupMember).setOwner(ownerClientId);
            ownerProfile.useFrontend(exclusiveGroupMember);
        }
        ownerProfile.setPrimaryFrontend(grantingHandle);
    }

    private void updateDemuxClientMappingOnNewGrant(int grantingHandle, int ownerClientId) {
        com.android.server.tv.tunerresourcemanager.DemuxResource grantingDemux = getDemuxResource(grantingHandle);
        if (grantingDemux != null) {
            com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(ownerClientId);
            grantingDemux.setOwner(ownerClientId);
            ownerProfile.useDemux(grantingHandle);
        }
    }

    private void updateDemuxClientMappingOnRelease(com.android.server.tv.tunerresourcemanager.DemuxResource releasingDemux) {
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(releasingDemux.getOwnerClientId());
        releasingDemux.removeOwner();
        ownerProfile.releaseDemux(releasingDemux.getHandle());
    }

    private void updateLnbClientMappingOnNewGrant(int grantingHandle, int ownerClientId) {
        com.android.server.tv.tunerresourcemanager.LnbResource grantingLnb = getLnbResource(grantingHandle);
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(ownerClientId);
        grantingLnb.setOwner(ownerClientId);
        ownerProfile.useLnb(grantingHandle);
    }

    private void updateLnbClientMappingOnRelease(com.android.server.tv.tunerresourcemanager.LnbResource releasingLnb) {
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(releasingLnb.getOwnerClientId());
        releasingLnb.removeOwner();
        ownerProfile.releaseLnb(releasingLnb.getHandle());
    }

    private void updateCasClientMappingOnNewGrant(int grantingId, int ownerClientId) {
        com.android.server.tv.tunerresourcemanager.CasResource grantingCas = getCasResource(grantingId);
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(ownerClientId);
        grantingCas.setOwner(ownerClientId);
        ownerProfile.useCas(grantingId);
    }

    private void updateCiCamClientMappingOnNewGrant(int grantingId, int ownerClientId) {
        com.android.server.tv.tunerresourcemanager.CiCamResource grantingCiCam = getCiCamResource(grantingId);
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(ownerClientId);
        grantingCiCam.setOwner(ownerClientId);
        ownerProfile.useCiCam(grantingId);
    }

    private void updateCasClientMappingOnRelease(com.android.server.tv.tunerresourcemanager.CasResource cas, int ownerClientId) {
        cas.removeSession(ownerClientId);
        if (!cas.hasOpenSessions(ownerClientId)) {
            com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(ownerClientId);
            cas.removeOwner(ownerClientId);
            ownerProfile.releaseCas();
        }
    }

    private void updateCiCamClientMappingOnRelease(com.android.server.tv.tunerresourcemanager.CiCamResource releasingCiCam, int ownerClientId) {
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerProfile = getClientProfile(ownerClientId);
        releasingCiCam.removeOwner(ownerClientId);
        ownerProfile.releaseCiCam();
    }

    private int updateAndGetOwnerClientPriority(int clientId) {
        com.android.server.tv.tunerresourcemanager.ClientProfile profile = getClientProfile(clientId);
        clientPriorityUpdateOnRequest(profile);
        return profile.getPriority();
    }

    private int getFrontendHighestClientPriority(int clientId) {
        com.android.server.tv.tunerresourcemanager.ClientProfile ownerClient = getClientProfile(clientId);
        if (ownerClient == null) {
            return 0;
        }
        int highestPriority = updateAndGetOwnerClientPriority(clientId);
        java.util.Iterator<java.lang.Integer> it = ownerClient.getShareFeClientIds().iterator();
        while (it.hasNext()) {
            int shareeId = it.next().intValue();
            int priority = updateAndGetOwnerClientPriority(shareeId);
            if (priority > highestPriority) {
                highestPriority = priority;
            }
        }
        return highestPriority;
    }

    protected com.android.server.tv.tunerresourcemanager.FrontendResource getFrontendResource(int frontendHandle) {
        return this.mFrontendResources.get(java.lang.Integer.valueOf(frontendHandle));
    }

    protected java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.FrontendResource> getFrontendResources() {
        return this.mFrontendResources;
    }

    protected com.android.server.tv.tunerresourcemanager.DemuxResource getDemuxResource(int demuxHandle) {
        return this.mDemuxResources.get(java.lang.Integer.valueOf(demuxHandle));
    }

    protected java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.DemuxResource> getDemuxResources() {
        return this.mDemuxResources;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setMaxNumberOfFrontendsInternal(int frontendType, int maxUsableNum) {
        int usedNum = this.mFrontendUsedNums.get(frontendType, -1);
        if (usedNum == -1 || usedNum <= maxUsableNum) {
            this.mFrontendMaxUsableNums.put(frontendType, maxUsableNum);
            return true;
        }
        android.util.Slog.e(TAG, "max number of frontend for frontendType: " + frontendType + " cannot be set to a value lower than the current usage count. (requested max num = " + maxUsableNum + ", current usage = " + usedNum);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMaxNumberOfFrontendsInternal(int frontendType) {
        int existingNum = this.mFrontendExistingNums.get(frontendType, -1);
        if (existingNum == -1) {
            android.util.Log.e(TAG, "existingNum is -1 for " + frontendType);
            return -1;
        }
        int maxUsableNum = this.mFrontendMaxUsableNums.get(frontendType, -1);
        if (maxUsableNum == -1) {
            return existingNum;
        }
        return maxUsableNum;
    }

    private boolean isFrontendMaxNumUseReached(int frontendType) {
        int maxUsableNum = this.mFrontendMaxUsableNums.get(frontendType, -1);
        if (maxUsableNum == -1) {
            return false;
        }
        int useNum = this.mFrontendUsedNums.get(frontendType, -1);
        if (useNum == -1) {
            useNum = 0;
        }
        return useNum >= maxUsableNum;
    }

    private void increFrontendNum(android.util.SparseIntArray targetNums, int frontendType) {
        int num = targetNums.get(frontendType, -1);
        if (num == -1) {
            targetNums.put(frontendType, 1);
        } else {
            targetNums.put(frontendType, num + 1);
        }
    }

    private void decreFrontendNum(android.util.SparseIntArray targetNums, int frontendType) {
        int num = targetNums.get(frontendType, -1);
        if (num != -1) {
            targetNums.put(frontendType, num - 1);
        }
    }

    private void replaceFeResourceMap(java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.FrontendResource> srcMap, java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.FrontendResource> dstMap) {
        if (dstMap != null) {
            dstMap.clear();
            if (srcMap != null && srcMap.size() > 0) {
                dstMap.putAll(srcMap);
            }
        }
    }

    private void replaceFeCounts(android.util.SparseIntArray srcCounts, android.util.SparseIntArray dstCounts) {
        if (dstCounts != null) {
            dstCounts.clear();
            if (srcCounts != null) {
                for (int i = 0; i < srcCounts.size(); i++) {
                    dstCounts.put(srcCounts.keyAt(i), srcCounts.valueAt(i));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpMap(java.util.Map<?, ?> targetMap, java.lang.String headline, java.lang.String delimiter, android.util.IndentingPrintWriter pw) {
        if (targetMap != null) {
            pw.println(headline);
            pw.increaseIndent();
            for (java.util.Map.Entry<?, ?> entry : targetMap.entrySet()) {
                pw.print(entry.getKey() + " : " + entry.getValue());
                pw.print(delimiter);
            }
            pw.println();
            pw.decreaseIndent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpSIA(android.util.SparseIntArray array, java.lang.String headline, java.lang.String delimiter, android.util.IndentingPrintWriter pw) {
        if (array != null) {
            pw.println(headline);
            pw.increaseIndent();
            for (int i = 0; i < array.size(); i++) {
                pw.print(array.keyAt(i) + " : " + array.valueAt(i));
                pw.print(delimiter);
            }
            pw.println();
            pw.decreaseIndent();
        }
    }

    private void addFrontendResource(com.android.server.tv.tunerresourcemanager.FrontendResource newFe) {
        java.util.Iterator<com.android.server.tv.tunerresourcemanager.FrontendResource> it = getFrontendResources().values().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            com.android.server.tv.tunerresourcemanager.FrontendResource fe = it.next();
            if (fe.getExclusiveGroupId() == newFe.getExclusiveGroupId()) {
                newFe.addExclusiveGroupMemberFeHandle(fe.getHandle());
                newFe.addExclusiveGroupMemberFeHandles(fe.getExclusiveGroupMemberFeHandles());
                java.util.Iterator<java.lang.Integer> it2 = fe.getExclusiveGroupMemberFeHandles().iterator();
                while (it2.hasNext()) {
                    int excGroupmemberFeHandle = it2.next().intValue();
                    getFrontendResource(excGroupmemberFeHandle).addExclusiveGroupMemberFeHandle(newFe.getHandle());
                }
                fe.addExclusiveGroupMemberFeHandle(newFe.getHandle());
            }
        }
        this.mFrontendResources.put(java.lang.Integer.valueOf(newFe.getHandle()), newFe);
        increFrontendNum(this.mFrontendExistingNums, newFe.getType());
    }

    private void addDemuxResource(com.android.server.tv.tunerresourcemanager.DemuxResource newDemux) {
        this.mDemuxResources.put(java.lang.Integer.valueOf(newDemux.getHandle()), newDemux);
    }

    private void removeFrontendResource(int removingHandle) {
        com.android.server.tv.tunerresourcemanager.FrontendResource fe = getFrontendResource(removingHandle);
        if (fe == null) {
            return;
        }
        if (fe.isInUse()) {
            com.android.server.tv.tunerresourcemanager.ClientProfile ownerClient = getClientProfile(fe.getOwnerClientId());
            java.util.Iterator<java.lang.Integer> it = ownerClient.getShareFeClientIds().iterator();
            while (it.hasNext()) {
                int shareOwnerId = it.next().intValue();
                clearFrontendAndClientMapping(getClientProfile(shareOwnerId));
            }
            clearFrontendAndClientMapping(ownerClient);
        }
        java.util.Iterator<java.lang.Integer> it2 = fe.getExclusiveGroupMemberFeHandles().iterator();
        while (it2.hasNext()) {
            int excGroupmemberFeHandle = it2.next().intValue();
            getFrontendResource(excGroupmemberFeHandle).removeExclusiveGroupMemberFeId(fe.getHandle());
        }
        decreFrontendNum(this.mFrontendExistingNums, fe.getType());
        this.mFrontendResources.remove(java.lang.Integer.valueOf(removingHandle));
    }

    private void removeDemuxResource(int removingHandle) {
        com.android.server.tv.tunerresourcemanager.DemuxResource demux = getDemuxResource(removingHandle);
        if (demux == null) {
            return;
        }
        if (demux.isInUse()) {
            releaseDemuxInternal(demux);
        }
        this.mDemuxResources.remove(java.lang.Integer.valueOf(removingHandle));
    }

    protected com.android.server.tv.tunerresourcemanager.LnbResource getLnbResource(int lnbHandle) {
        return this.mLnbResources.get(java.lang.Integer.valueOf(lnbHandle));
    }

    protected java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.LnbResource> getLnbResources() {
        return this.mLnbResources;
    }

    private void addLnbResource(com.android.server.tv.tunerresourcemanager.LnbResource newLnb) {
        this.mLnbResources.put(java.lang.Integer.valueOf(newLnb.getHandle()), newLnb);
    }

    private void removeLnbResource(int removingHandle) {
        com.android.server.tv.tunerresourcemanager.LnbResource lnb = getLnbResource(removingHandle);
        if (lnb == null) {
            return;
        }
        if (lnb.isInUse()) {
            releaseLnbInternal(lnb);
        }
        this.mLnbResources.remove(java.lang.Integer.valueOf(removingHandle));
    }

    protected com.android.server.tv.tunerresourcemanager.CasResource getCasResource(int systemId) {
        return this.mCasResources.get(java.lang.Integer.valueOf(systemId));
    }

    protected com.android.server.tv.tunerresourcemanager.CiCamResource getCiCamResource(int ciCamId) {
        return this.mCiCamResources.get(java.lang.Integer.valueOf(ciCamId));
    }

    protected java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.CasResource> getCasResources() {
        return this.mCasResources;
    }

    protected java.util.Map<java.lang.Integer, com.android.server.tv.tunerresourcemanager.CiCamResource> getCiCamResources() {
        return this.mCiCamResources;
    }

    private void addCasResource(com.android.server.tv.tunerresourcemanager.CasResource newCas) {
        this.mCasResources.put(java.lang.Integer.valueOf(newCas.getSystemId()), newCas);
    }

    private void addCiCamResource(com.android.server.tv.tunerresourcemanager.CiCamResource newCiCam) {
        this.mCiCamResources.put(java.lang.Integer.valueOf(newCiCam.getCiCamId()), newCiCam);
    }

    private void removeCasResource(int removingId) {
        com.android.server.tv.tunerresourcemanager.CasResource cas = getCasResource(removingId);
        if (cas == null) {
            return;
        }
        java.util.Iterator<java.lang.Integer> it = cas.getOwnerClientIds().iterator();
        while (it.hasNext()) {
            int ownerId = it.next().intValue();
            getClientProfile(ownerId).releaseCas();
        }
        this.mCasResources.remove(java.lang.Integer.valueOf(removingId));
    }

    private void removeCiCamResource(int removingId) {
        com.android.server.tv.tunerresourcemanager.CiCamResource ciCam = getCiCamResource(removingId);
        if (ciCam == null) {
            return;
        }
        java.util.Iterator<java.lang.Integer> it = ciCam.getOwnerClientIds().iterator();
        while (it.hasNext()) {
            int ownerId = it.next().intValue();
            getClientProfile(ownerId).releaseCiCam();
        }
        this.mCiCamResources.remove(java.lang.Integer.valueOf(removingId));
    }

    private void releaseLowerPriorityClientCasResources(int releasingCasResourceNum) {
    }

    protected com.android.server.tv.tunerresourcemanager.ClientProfile getClientProfile(int clientId) {
        return this.mClientProfiles.get(java.lang.Integer.valueOf(clientId));
    }

    private void addClientProfile(int clientId, com.android.server.tv.tunerresourcemanager.ClientProfile profile, android.media.tv.tunerresourcemanager.IResourcesReclaimListener listener) {
        this.mClientProfiles.put(java.lang.Integer.valueOf(clientId), profile);
        addResourcesReclaimListener(clientId, listener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeClientProfile(int clientId) {
        java.util.Iterator<java.lang.Integer> it = getClientProfile(clientId).getShareFeClientIds().iterator();
        while (it.hasNext()) {
            int shareOwnerId = it.next().intValue();
            clearFrontendAndClientMapping(getClientProfile(shareOwnerId));
        }
        clearAllResourcesAndClientMapping(getClientProfile(clientId));
        this.mClientProfiles.remove(java.lang.Integer.valueOf(clientId));
        synchronized (this.mLock) {
            com.android.server.tv.tunerresourcemanager.TunerResourceManagerService.ResourcesReclaimListenerRecord record = this.mListeners.remove(java.lang.Integer.valueOf(clientId));
            if (record != null) {
                record.getListener().asBinder().unlinkToDeath(record, 0);
            }
        }
    }

    private void clearFrontendAndClientMapping(com.android.server.tv.tunerresourcemanager.ClientProfile profile) {
        com.android.server.tv.tunerresourcemanager.FrontendResource primaryFe;
        if (profile == null) {
            return;
        }
        for (java.lang.Integer feId : profile.getInUseFrontendHandles()) {
            com.android.server.tv.tunerresourcemanager.FrontendResource fe = getFrontendResource(feId.intValue());
            int ownerClientId = fe.getOwnerClientId();
            if (ownerClientId == profile.getId()) {
                fe.removeOwner();
            } else {
                com.android.server.tv.tunerresourcemanager.ClientProfile ownerClientProfile = getClientProfile(ownerClientId);
                if (ownerClientProfile != null) {
                    ownerClientProfile.stopSharingFrontend(profile.getId());
                }
            }
        }
        int primaryFeId = profile.getPrimaryFrontend();
        if (primaryFeId != -1 && (primaryFe = getFrontendResource(primaryFeId)) != null) {
            decreFrontendNum(this.mFrontendUsedNums, primaryFe.getType());
        }
        profile.releaseFrontend();
    }

    private void clearAllResourcesAndClientMapping(com.android.server.tv.tunerresourcemanager.ClientProfile profile) {
        if (profile == null) {
            return;
        }
        for (java.lang.Integer lnbHandle : profile.getInUseLnbHandles()) {
            getLnbResource(lnbHandle.intValue()).removeOwner();
        }
        if (profile.getInUseCasSystemId() != -1) {
            getCasResource(profile.getInUseCasSystemId()).removeOwner(profile.getId());
        }
        if (profile.getInUseCiCamId() != -1) {
            getCiCamResource(profile.getInUseCiCamId()).removeOwner(profile.getId());
        }
        for (java.lang.Integer demuxHandle : profile.getInUseDemuxHandles()) {
            getDemuxResource(demuxHandle.intValue()).removeOwner();
        }
        clearFrontendAndClientMapping(profile);
        profile.reclaimAllResources();
    }

    protected boolean checkClientExists(int clientId) {
        return this.mClientProfiles.keySet().contains(java.lang.Integer.valueOf(clientId));
    }

    private int generateResourceHandle(int resourceType, int resourceId) {
        int i = ((resourceType & 255) << 24) | (resourceId << 16);
        int i2 = this.mResourceRequestCount;
        this.mResourceRequestCount = i2 + 1;
        return i | (i2 & 65535);
    }

    protected int getResourceIdFromHandle(int resourceHandle) {
        if (resourceHandle == -1) {
            return resourceHandle;
        }
        return (16711680 & resourceHandle) >> 16;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateResourceHandle(int resourceType, int resourceHandle) {
        if (resourceHandle == -1 || (((-16777216) & resourceHandle) >> 24) != resourceType) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceTrmAccessPermission(java.lang.String apiName) {
        getContext().enforceCallingOrSelfPermission("android.permission.TUNER_RESOURCE_ACCESS", "TunerResourceManagerService: " + apiName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceTunerAccessPermission(java.lang.String apiName) {
        getContext().enforceCallingPermission("android.permission.ACCESS_TV_TUNER", "TunerResourceManagerService: " + apiName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enforceDescramblerAccessPermission(java.lang.String apiName) {
        getContext().enforceCallingPermission("android.permission.ACCESS_TV_DESCRAMBLER", "TunerResourceManagerService: " + apiName);
    }
}
