package com.android.server.companion.datatransfer.contextsync;

/* JADX INFO: loaded from: classes.dex */
class CallMetadataSyncData {
    final java.util.Map<java.lang.String, com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call> mCalls = new java.util.HashMap();
    final java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest> mCallCreateRequests = new java.util.ArrayList();
    final java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallControlRequest> mCallControlRequests = new java.util.ArrayList();
    final java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator> mCallFacilitators = new java.util.ArrayList();

    CallMetadataSyncData() {
    }

    public void addCall(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call) {
        this.mCalls.put(call.getId(), call);
    }

    public boolean hasCall(java.lang.String id) {
        return this.mCalls.containsKey(id);
    }

    public java.util.Collection<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call> getCalls() {
        return this.mCalls.values();
    }

    public void addCallCreateRequest(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest request) {
        this.mCallCreateRequests.add(request);
    }

    public java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallCreateRequest> getCallCreateRequests() {
        return this.mCallCreateRequests;
    }

    public void addCallControlRequest(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallControlRequest request) {
        this.mCallControlRequests.add(request);
    }

    public java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallControlRequest> getCallControlRequests() {
        return this.mCallControlRequests;
    }

    public void addFacilitator(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator) {
        this.mCallFacilitators.add(facilitator);
    }

    public java.util.List<com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator> getFacilitators() {
        return this.mCallFacilitators;
    }

    public static class CallFacilitator {
        private java.lang.String mExtendedIdentifier;
        private java.lang.String mIdentifier;
        private boolean mIsTel;
        private java.lang.String mName;

        CallFacilitator() {
        }

        CallFacilitator(java.lang.String name, java.lang.String identifier, java.lang.String extendedIdentifier) {
            this.mName = name;
            this.mIdentifier = identifier;
            this.mExtendedIdentifier = extendedIdentifier;
        }

        public java.lang.String getName() {
            return this.mName;
        }

        public java.lang.String getIdentifier() {
            return this.mIdentifier;
        }

        public java.lang.String getExtendedIdentifier() {
            return this.mExtendedIdentifier;
        }

        public boolean isTel() {
            return this.mIsTel;
        }

        public void setName(java.lang.String name) {
            this.mName = name;
        }

        public void setIdentifier(java.lang.String identifier) {
            this.mIdentifier = identifier;
        }

        public void setExtendedIdentifier(java.lang.String extendedIdentifier) {
            this.mExtendedIdentifier = extendedIdentifier;
        }

        public void setIsTel(boolean isTel) {
            this.mIsTel = isTel;
        }
    }

    public static class CallControlRequest {
        private int mControl;
        private java.lang.String mId;

        public void setId(java.lang.String id) {
            this.mId = id;
        }

        public void setControl(int control) {
            this.mControl = control;
        }

        public java.lang.String getId() {
            return this.mId;
        }

        public int getControl() {
            return this.mControl;
        }
    }

    public static class CallCreateRequest {
        private java.lang.String mAddress;
        private com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator mFacilitator;
        private java.lang.String mId;

        public void setId(java.lang.String id) {
            this.mId = id;
        }

        public void setAddress(java.lang.String address) {
            this.mAddress = address;
        }

        public void setFacilitator(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator) {
            this.mFacilitator = facilitator;
        }

        public java.lang.String getId() {
            return this.mId;
        }

        public java.lang.String getAddress() {
            return this.mAddress;
        }

        public com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator getFacilitator() {
            return this.mFacilitator;
        }
    }

    public static class Call {
        private static final java.lang.String EXTRA_APP_ICON = "com.android.server.companion.datatransfer.contextsync.extra.APP_ICON";
        private static final java.lang.String EXTRA_CALLER_ID = "com.android.server.companion.datatransfer.contextsync.extra.CALLER_ID";
        private static final java.lang.String EXTRA_CONTROLS = "com.android.server.companion.datatransfer.contextsync.extra.CONTROLS";
        private static final java.lang.String EXTRA_DIRECTION = "com.android.server.companion.datatransfer.contextsync.extra.DIRECTION";
        private static final java.lang.String EXTRA_FACILITATOR_EXT_ID = "com.android.server.companion.datatransfer.contextsync.extra.FACILITATOR_EXT_ID";
        private static final java.lang.String EXTRA_FACILITATOR_ID = "com.android.server.companion.datatransfer.contextsync.extra.FACILITATOR_ID";
        private static final java.lang.String EXTRA_FACILITATOR_NAME = "com.android.server.companion.datatransfer.contextsync.extra.FACILITATOR_NAME";
        private static final java.lang.String EXTRA_STATUS = "com.android.server.companion.datatransfer.contextsync.extra.STATUS";
        private byte[] mAppIcon;
        private java.lang.String mCallerId;
        private final java.util.Set<java.lang.Integer> mControls = new java.util.HashSet();
        private int mDirection;
        private com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator mFacilitator;
        private java.lang.String mId;
        private int mStatus;

        public static com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call fromBundle(android.os.Bundle bundle) {
            com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call call = new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call();
            if (bundle != null) {
                call.setId(bundle.getString(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_CALL_ID));
                call.setCallerId(bundle.getString(EXTRA_CALLER_ID));
                call.setAppIcon(bundle.getByteArray(EXTRA_APP_ICON));
                java.lang.String facilitatorName = bundle.getString(EXTRA_FACILITATOR_NAME);
                java.lang.String facilitatorIdentifier = bundle.getString(EXTRA_FACILITATOR_ID);
                java.lang.String facilitatorExtendedIdentifier = bundle.getString(EXTRA_FACILITATOR_EXT_ID);
                call.setFacilitator(new com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator(facilitatorName, facilitatorIdentifier, facilitatorExtendedIdentifier));
                call.setStatus(bundle.getInt(EXTRA_STATUS));
                call.setDirection(bundle.getInt(EXTRA_DIRECTION));
                call.setControls(new java.util.HashSet(bundle.getIntegerArrayList(EXTRA_CONTROLS)));
            }
            return call;
        }

        public android.os.Bundle writeToBundle() {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putString(com.android.server.companion.datatransfer.contextsync.CrossDeviceSyncController.EXTRA_CALL_ID, this.mId);
            bundle.putString(EXTRA_CALLER_ID, this.mCallerId);
            bundle.putByteArray(EXTRA_APP_ICON, this.mAppIcon);
            bundle.putString(EXTRA_FACILITATOR_NAME, this.mFacilitator.getName());
            bundle.putString(EXTRA_FACILITATOR_ID, this.mFacilitator.getIdentifier());
            bundle.putString(EXTRA_FACILITATOR_EXT_ID, this.mFacilitator.getExtendedIdentifier());
            bundle.putInt(EXTRA_STATUS, this.mStatus);
            bundle.putInt(EXTRA_DIRECTION, this.mDirection);
            bundle.putIntegerArrayList(EXTRA_CONTROLS, new java.util.ArrayList<>(this.mControls));
            return bundle;
        }

        void setId(java.lang.String id) {
            this.mId = id;
        }

        void setCallerId(java.lang.String callerId) {
            this.mCallerId = callerId;
        }

        void setAppIcon(byte[] appIcon) {
            this.mAppIcon = appIcon;
        }

        void setFacilitator(com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator facilitator) {
            this.mFacilitator = facilitator;
        }

        void setStatus(int status) {
            this.mStatus = status;
        }

        void setDirection(int direction) {
            this.mDirection = direction;
        }

        void addControl(int control) {
            this.mControls.add(java.lang.Integer.valueOf(control));
        }

        void setControls(java.util.Set<java.lang.Integer> controls) {
            this.mControls.clear();
            this.mControls.addAll(controls);
        }

        java.lang.String getId() {
            return this.mId;
        }

        java.lang.String getCallerId() {
            return this.mCallerId;
        }

        byte[] getAppIcon() {
            return this.mAppIcon;
        }

        com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.CallFacilitator getFacilitator() {
            return this.mFacilitator;
        }

        int getStatus() {
            return this.mStatus;
        }

        int getDirection() {
            return this.mDirection;
        }

        java.util.Set<java.lang.Integer> getControls() {
            return this.mControls;
        }

        boolean hasControl(int control) {
            return this.mControls.contains(java.lang.Integer.valueOf(control));
        }

        public boolean equals(java.lang.Object other) {
            return (other instanceof com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call) && this.mId != null && this.mId.equals(((com.android.server.companion.datatransfer.contextsync.CallMetadataSyncData.Call) other).getId());
        }

        public int hashCode() {
            return java.util.Objects.hashCode(this.mId);
        }
    }
}
