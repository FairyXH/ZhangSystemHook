package com.android.server.lights;

/* JADX INFO: loaded from: classes2.dex */
public class LightsService extends com.android.server.SystemService {
    static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    static final java.lang.String TAG = "LightsService";
    private android.os.Handler mH;
    private final android.util.SparseArray<com.android.server.lights.LightsService.LightImpl> mLightsById;
    private final com.android.server.lights.LightsService.LightImpl[] mLightsByType;
    private com.android.server.lights.LightsService.LightsServiceWrapper mLsWrapper;
    final com.android.server.lights.LightsService.LightsManagerBinderService mManagerService;
    private final com.android.server.lights.LightsManager mService;
    private final java.util.function.Supplier<android.hardware.light.ILights> mVintfLights;

    static native void setLight_native(int i, int i2, int i3, int i4, int i5, int i6);

    /* JADX INFO: Access modifiers changed from: private */
    final class LightsManagerBinderService extends android.hardware.lights.ILightsManager.Stub {
        private final java.util.List<com.android.server.lights.LightsService.LightsManagerBinderService.Session> mSessions;

        LightsManagerBinderService() {
            super(android.os.PermissionEnforcer.fromContext(com.android.server.lights.LightsService.this.getContext()));
            this.mSessions = new java.util.ArrayList();
        }

        private final class Session implements java.lang.Comparable<com.android.server.lights.LightsService.LightsManagerBinderService.Session> {
            final int mPriority;
            final android.util.SparseArray<android.hardware.lights.LightState> mRequests = new android.util.SparseArray<>();
            final android.os.IBinder mToken;

            Session(android.os.IBinder token, int priority) {
                this.mToken = token;
                this.mPriority = priority;
            }

            void setRequest(int lightId, android.hardware.lights.LightState state) {
                if (state != null) {
                    this.mRequests.put(lightId, state);
                } else {
                    this.mRequests.remove(lightId);
                }
            }

            @Override // java.lang.Comparable
            public int compareTo(com.android.server.lights.LightsService.LightsManagerBinderService.Session otherSession) {
                return java.lang.Integer.compare(otherSession.mPriority, this.mPriority);
            }
        }

        public java.util.List<android.hardware.lights.Light> getLights() {
            java.util.List<android.hardware.lights.Light> lights;
            getLights_enforcePermission();
            synchronized (com.android.server.lights.LightsService.this) {
                lights = new java.util.ArrayList<>();
                for (int i = 0; i < com.android.server.lights.LightsService.this.mLightsById.size(); i++) {
                    if (!((com.android.server.lights.LightsService.LightImpl) com.android.server.lights.LightsService.this.mLightsById.valueAt(i)).isSystemLight()) {
                        android.hardware.light.HwLight hwLight = ((com.android.server.lights.LightsService.LightImpl) com.android.server.lights.LightsService.this.mLightsById.valueAt(i)).mHwLight;
                        lights.add(new android.hardware.lights.Light(hwLight.id, hwLight.ordinal, hwLight.type));
                    }
                }
            }
            return lights;
        }

        public void setLightStates(android.os.IBinder token, int[] lightIds, android.hardware.lights.LightState[] lightStates) {
            setLightStates_enforcePermission();
            com.android.internal.util.Preconditions.checkState(lightIds.length == lightStates.length);
            synchronized (com.android.server.lights.LightsService.this) {
                com.android.server.lights.LightsService.LightsManagerBinderService.Session session = getSessionLocked((android.os.IBinder) com.android.internal.util.Preconditions.checkNotNull(token));
                com.android.internal.util.Preconditions.checkState(session != null, "not registered");
                checkRequestIsValid(lightIds);
                for (int i = 0; i < lightIds.length; i++) {
                    session.setRequest(lightIds[i], lightStates[i]);
                }
                invalidateLightStatesLocked();
            }
        }

        public android.hardware.lights.LightState getLightState(int lightId) {
            android.hardware.lights.LightState lightState;
            getLightState_enforcePermission();
            synchronized (com.android.server.lights.LightsService.this) {
                com.android.server.lights.LightsService.LightImpl light = (com.android.server.lights.LightsService.LightImpl) com.android.server.lights.LightsService.this.mLightsById.get(lightId);
                if (light == null || light.isSystemLight()) {
                    throw new java.lang.IllegalArgumentException("Invalid light: " + lightId);
                }
                lightState = new android.hardware.lights.LightState(light.getColor());
            }
            return lightState;
        }

        public void openSession(final android.os.IBinder token, int priority) {
            openSession_enforcePermission();
            com.android.internal.util.Preconditions.checkNotNull(token);
            synchronized (com.android.server.lights.LightsService.this) {
                com.android.internal.util.Preconditions.checkState(getSessionLocked(token) == null, "already registered");
                try {
                    token.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.lights.LightsService$LightsManagerBinderService$$ExternalSyntheticLambda0
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            this.f$0.lambda$openSession$0(token);
                        }
                    }, 0);
                    this.mSessions.add(new com.android.server.lights.LightsService.LightsManagerBinderService.Session(token, priority));
                    java.util.Collections.sort(this.mSessions);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.lights.LightsService.TAG, "Couldn't open session, client already died", e);
                    throw new java.lang.IllegalArgumentException("Client is already dead.");
                }
            }
        }

        public void closeSession(android.os.IBinder token) {
            closeSession_enforcePermission();
            com.android.internal.util.Preconditions.checkNotNull(token);
            lambda$openSession$0(token);
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.lights.LightsService.this.getContext(), com.android.server.lights.LightsService.TAG, pw)) {
                int argc = args.length;
                boolean ret = false;
                if (argc > 0) {
                    if (argc == 2 && args[0].equals("set-lux")) {
                        ret = com.android.server.lights.LightsService.this.setPocketFakeLux(args[1], pw);
                    } else {
                        pw.println("Error: Invalid command!");
                    }
                    if (!ret) {
                        return;
                    }
                }
                synchronized (com.android.server.lights.LightsService.this) {
                    if (com.android.server.lights.LightsService.this.mVintfLights != null) {
                        pw.println("Service: aidl (" + com.android.server.lights.LightsService.this.mVintfLights.get() + ")");
                    } else {
                        pw.println("Service: hidl");
                    }
                    pw.println("Lights:");
                    for (int i = 0; i < com.android.server.lights.LightsService.this.mLightsById.size(); i++) {
                        com.android.server.lights.LightsService.LightImpl light = (com.android.server.lights.LightsService.LightImpl) com.android.server.lights.LightsService.this.mLightsById.valueAt(i);
                        pw.println(java.lang.String.format("  Light id=%d ordinal=%d color=%08x", java.lang.Integer.valueOf(light.mHwLight.id), java.lang.Integer.valueOf(light.mHwLight.ordinal), java.lang.Integer.valueOf(light.getColor())));
                    }
                    pw.println("Session clients:");
                    for (com.android.server.lights.LightsService.LightsManagerBinderService.Session session : this.mSessions) {
                        pw.println("  Session token=" + session.mToken);
                        for (int i2 = 0; i2 < session.mRequests.size(); i2++) {
                            pw.println(java.lang.String.format("    Request id=%d color=%08x", java.lang.Integer.valueOf(session.mRequests.keyAt(i2)), java.lang.Integer.valueOf(session.mRequests.valueAt(i2).getColor())));
                        }
                    }
                    com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().dumpOplus(pw);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: closeSessionInternal, reason: merged with bridge method [inline-methods] */
        public void lambda$openSession$0(android.os.IBinder token) {
            synchronized (com.android.server.lights.LightsService.this) {
                com.android.server.lights.LightsService.LightsManagerBinderService.Session session = getSessionLocked(token);
                if (session != null) {
                    this.mSessions.remove(session);
                    invalidateLightStatesLocked();
                }
            }
        }

        private void checkRequestIsValid(int[] lightIds) {
            for (int lightId : lightIds) {
                com.android.server.lights.LightsService.LightImpl light = (com.android.server.lights.LightsService.LightImpl) com.android.server.lights.LightsService.this.mLightsById.get(lightId);
                com.android.internal.util.Preconditions.checkState((light == null || light.isSystemLight()) ? false : true, "Invalid lightId " + lightId);
            }
        }

        private void invalidateLightStatesLocked() {
            java.util.Map<java.lang.Integer, android.hardware.lights.LightState> states = new java.util.HashMap<>();
            for (int i = this.mSessions.size() - 1; i >= 0; i--) {
                android.util.SparseArray<android.hardware.lights.LightState> requests = this.mSessions.get(i).mRequests;
                for (int j = 0; j < requests.size(); j++) {
                    states.put(java.lang.Integer.valueOf(requests.keyAt(j)), requests.valueAt(j));
                }
            }
            for (int i2 = 0; i2 < com.android.server.lights.LightsService.this.mLightsById.size(); i2++) {
                com.android.server.lights.LightsService.LightImpl light = (com.android.server.lights.LightsService.LightImpl) com.android.server.lights.LightsService.this.mLightsById.valueAt(i2);
                if (!light.isSystemLight()) {
                    android.hardware.lights.LightState state = states.get(java.lang.Integer.valueOf(light.mHwLight.id));
                    if (state != null) {
                        light.setColor(state.getColor());
                    } else {
                        light.turnOff();
                    }
                }
            }
        }

        private com.android.server.lights.LightsService.LightsManagerBinderService.Session getSessionLocked(android.os.IBinder token) {
            for (int i = 0; i < this.mSessions.size(); i++) {
                if (token.equals(this.mSessions.get(i).mToken)) {
                    return this.mSessions.get(i);
                }
            }
            return null;
        }
    }

    public final class LightImpl extends com.android.server.lights.LogicalLight {
        private int mBrightnessMode;
        private int mColor;
        private boolean mFlashing;
        private android.hardware.light.HwLight mHwLight;
        private boolean mInitialized;
        private int mLastBrightnessMode;
        private int mLastColor;
        private int mMode;
        private int mOffMS;
        private int mOnMS;
        private boolean mUseLowPersistenceForVR;
        private boolean mVrModeEnabled;

        private LightImpl(android.content.Context context, android.hardware.light.HwLight hwLight) {
            this.mHwLight = hwLight;
        }

        @Override // com.android.server.lights.LogicalLight
        public void setBrightness(float brightness) {
            setBrightness(brightness, 0);
        }

        @Override // com.android.server.lights.LogicalLight
        public void setBrightness(float brightness, int brightnessMode) {
            if (java.lang.Float.isNaN(brightness)) {
                android.util.Slog.w(com.android.server.lights.LightsService.TAG, "Brightness is not valid: " + brightness);
                return;
            }
            synchronized (this) {
                if (brightnessMode == 2) {
                    android.util.Slog.w(com.android.server.lights.LightsService.TAG, "setBrightness with LOW_PERSISTENCE unexpected #" + this.mHwLight.id + ": brightness=" + brightness);
                } else {
                    com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().onSetLight(this.mHwLight.id, (int) brightness, brightnessMode);
                }
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void setColor(int color) {
            synchronized (this) {
                com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("setColor");
                setLightLocked(color, 0, 0, 0, 0);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void setFlashing(int color, int mode, int onMS, int offMS) {
            synchronized (this) {
                com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("setFlashing");
                setLightLocked(color, mode, onMS, offMS, 0);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void setFlashing(int color, int mode, int onMS, int offMS, boolean needSetZero) {
            synchronized (this) {
                com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("setFlashing");
                if (needSetZero) {
                    setLightLocked(0, mode, onMS, offMS, 0);
                }
                setLightLocked(color, mode, onMS, offMS, 0);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void pulse() {
            pulse(android.hardware.audio.common.V2_0.AudioFormat.SUB_MASK, 7);
        }

        @Override // com.android.server.lights.LogicalLight
        public void pulse(int color, int onMS) {
            synchronized (this) {
                com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("pulse");
                if (this.mColor == 0 && !this.mFlashing) {
                    setLightLocked(color, 2, onMS, 1000, 0);
                    this.mColor = 0;
                    com.android.server.lights.LightsService.this.mH.postDelayed(new java.lang.Runnable() { // from class: com.android.server.lights.LightsService$LightImpl$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.stopFlashing();
                        }
                    }, onMS);
                }
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void turnOff() {
            synchronized (this) {
                com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("turnOff");
                setLightLocked(0, 0, 0, 0, 0);
            }
        }

        @Override // com.android.server.lights.LogicalLight
        public void setVrMode(boolean enabled) {
            synchronized (this) {
                if (this.mVrModeEnabled != enabled) {
                    this.mVrModeEnabled = enabled;
                    this.mUseLowPersistenceForVR = com.android.server.lights.LightsService.this.getVrDisplayMode() == 0;
                    if (shouldBeInLowPersistenceMode()) {
                        this.mLastBrightnessMode = this.mBrightnessMode;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stopFlashing() {
            synchronized (this) {
                com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().dumpStackTrace("stopFlashing");
                setLightLocked(this.mColor, 0, 0, 0, 0);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLightLocked(int color, int mode, int onMS, int offMS, int brightnessMode) {
            if (shouldBeInLowPersistenceMode()) {
                brightnessMode = 2;
            } else if (brightnessMode == 2) {
                brightnessMode = this.mLastBrightnessMode;
            }
            if (!this.mInitialized || color != this.mColor || mode != this.mMode || onMS != this.mOnMS || offMS != this.mOffMS || this.mBrightnessMode != brightnessMode || this.mHwLight.type == 1) {
                if (com.android.server.lights.LightsService.DEBUG) {
                    android.util.Slog.v(com.android.server.lights.LightsService.TAG, "setLight #" + this.mHwLight.id + ": color=#" + java.lang.Integer.toHexString(color) + ": brightnessMode=" + brightnessMode);
                }
                this.mInitialized = true;
                this.mLastColor = this.mColor;
                this.mColor = color;
                this.mMode = mode;
                this.mOnMS = onMS;
                this.mOffMS = offMS;
                this.mBrightnessMode = brightnessMode;
                com.android.server.lights.LightsService.this.mLsWrapper.getExtImpl().setOplusLightUnchecked(this.mHwLight.type, this.mHwLight.id, color, mode, onMS, offMS, brightnessMode);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLightUnchecked(int color, int mode, int onMS, int offMS, int brightnessMode) {
            android.os.Trace.traceBegin(131072L, "setLightState(" + this.mHwLight.id + ", 0x" + java.lang.Integer.toHexString(color) + ")");
            try {
                try {
                    if (com.android.server.lights.LightsService.DEBUG) {
                        android.util.Slog.v(com.android.server.lights.LightsService.TAG, "setLightUnchecked #" + this.mHwLight.id + ": color=#" + java.lang.Integer.toHexString(color) + ": brightnessMode=" + brightnessMode);
                    }
                    if (com.android.server.lights.LightsService.this.mVintfLights != null) {
                        android.hardware.light.HwLightState lightState = new android.hardware.light.HwLightState();
                        lightState.color = color;
                        lightState.flashMode = (byte) mode;
                        lightState.flashOnMs = onMS;
                        lightState.flashOffMs = offMS;
                        lightState.brightnessMode = (byte) brightnessMode;
                        ((android.hardware.light.ILights) com.android.server.lights.LightsService.this.mVintfLights.get()).setLightState(this.mHwLight.id, lightState);
                    } else {
                        com.android.server.lights.LightsService.setLight_native(this.mHwLight.id, color, mode, onMS, offMS, brightnessMode);
                    }
                } catch (android.os.RemoteException | java.lang.UnsupportedOperationException ex) {
                    android.util.Slog.e(com.android.server.lights.LightsService.TAG, "Failed issuing setLightState", ex);
                }
            } finally {
                android.os.Trace.traceEnd(131072L);
            }
        }

        private boolean shouldBeInLowPersistenceMode() {
            return this.mVrModeEnabled && this.mUseLowPersistenceForVR;
        }

        android.hardware.light.HwLight getHwLight() {
            return this.mHwLight;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isSystemLight() {
            return this.mHwLight.type >= 0 && this.mHwLight.type < 8;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getColor() {
            return this.mColor;
        }
    }

    public LightsService(android.content.Context context) {
        this(context, new com.android.server.lights.LightsService.VintfHalCache(), android.os.Looper.myLooper());
    }

    /* JADX WARN: Multi-variable type inference failed */
    LightsService(android.content.Context context, java.util.function.Supplier<android.hardware.light.ILights> supplier, android.os.Looper looper) {
        super(context);
        this.mLightsByType = new com.android.server.lights.LightsService.LightImpl[8];
        this.mLightsById = new android.util.SparseArray<>();
        this.mService = new com.android.server.lights.LightsManager() { // from class: com.android.server.lights.LightsService.1
            @Override // com.android.server.lights.LightsManager
            public com.android.server.lights.LogicalLight getLight(int lightType) {
                if (com.android.server.lights.LightsService.this.mLightsByType != null && lightType >= 0 && lightType < com.android.server.lights.LightsService.this.mLightsByType.length) {
                    return com.android.server.lights.LightsService.this.mLightsByType[lightType];
                }
                return null;
            }
        };
        this.mLsWrapper = new com.android.server.lights.LightsService.LightsServiceWrapper();
        this.mH = new android.os.Handler(looper);
        this.mVintfLights = supplier.get() != null ? supplier : null;
        populateAvailableLights(context);
        this.mManagerService = new com.android.server.lights.LightsService.LightsManagerBinderService();
        this.mLsWrapper.getExtImpl().init(context, looper);
    }

    private void populateAvailableLights(android.content.Context context) {
        if (this.mVintfLights != null) {
            populateAvailableLightsFromAidl(context);
        } else {
            populateAvailableLightsFromHidl(context);
        }
        for (int i = this.mLightsById.size() - 1; i >= 0; i--) {
            com.android.server.lights.LightsService.LightImpl light = this.mLightsById.valueAt(i);
            int type = light.mHwLight.type;
            if (type >= 0 && type < this.mLightsByType.length) {
                this.mLightsByType[type] = light;
            }
        }
    }

    private void populateAvailableLightsFromAidl(android.content.Context context) {
        try {
            for (android.hardware.light.HwLight hwLight : this.mVintfLights.get().getLights()) {
                this.mLightsById.put(hwLight.id, new com.android.server.lights.LightsService.LightImpl(context, hwLight));
            }
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(TAG, "Unable to get lights from HAL", ex);
        }
    }

    private void populateAvailableLightsFromHidl(android.content.Context context) {
        for (int i = 0; i < this.mLightsByType.length; i++) {
            android.hardware.light.HwLight hwLight = new android.hardware.light.HwLight();
            hwLight.id = (byte) i;
            hwLight.ordinal = 1;
            hwLight.type = (byte) i;
            this.mLightsById.put(hwLight.id, new com.android.server.lights.LightsService.LightImpl(context, hwLight));
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishLocalService(com.android.server.lights.LightsManager.class, this.mService);
        publishBinderService("lights", this.mManagerService);
        this.mLsWrapper.getExtImpl().setBootAnimationLightInternal(true, 16910351);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        this.mLsWrapper.getExtImpl().onBootComplete(phase);
    }

    public boolean setPocketFakeLux(java.lang.String fakelux, java.io.PrintWriter pw) {
        if (!this.mLsWrapper.getExtImpl().isPocketLightModeSupported()) {
            android.util.Slog.e(TAG, "Breathing Light pocket mode feature not supported!");
            return false;
        }
        try {
            int lux = java.lang.Integer.parseInt(fakelux);
            pw.println("FakeLux: " + lux);
            boolean isHBMLux = lux >= 15000;
            this.mLsWrapper.getExtImpl().setFakePocketLightMode(isHBMLux);
            return true;
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Error: invalid format of fakelux, please use integer!");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getVrDisplayMode() {
        int currentUser = android.app.ActivityManager.getCurrentUser();
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "vr_display_mode", 0, currentUser);
    }

    private static class VintfHalCache implements java.util.function.Supplier<android.hardware.light.ILights>, android.os.IBinder.DeathRecipient {
        private android.hardware.light.ILights mInstance;

        private VintfHalCache() {
            this.mInstance = null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        public synchronized android.hardware.light.ILights get() {
            android.os.IBinder binder;
            if (this.mInstance == null && (binder = android.os.Binder.allowBlocking(android.os.ServiceManager.waitForDeclaredService(android.hardware.light.ILights.DESCRIPTOR + "/default"))) != null) {
                this.mInstance = android.hardware.light.ILights.Stub.asInterface(binder);
                try {
                    binder.linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.lights.LightsService.TAG, "Unable to register DeathRecipient for " + this.mInstance);
                }
            }
            return this.mInstance;
        }

        @Override // android.os.IBinder.DeathRecipient
        public synchronized void binderDied() {
            this.mInstance = null;
        }
    }

    public com.android.server.lights.ILightsServiceWrapper getWrapper() {
        return this.mLsWrapper;
    }

    private class LightsServiceWrapper implements com.android.server.lights.ILightsServiceWrapper {
        private com.android.server.lights.ILightsServiceExt mLsExt;

        private LightsServiceWrapper() {
            this.mLsExt = (com.android.server.lights.ILightsServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.lights.ILightsServiceExt.class).base(com.android.server.lights.LightsService.this).create();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.lights.ILightsServiceExt getExtImpl() {
            return this.mLsExt;
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public boolean getDebug() {
            return com.android.server.lights.LightsService.DEBUG;
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public java.lang.Object getLightsByType() {
            return com.android.server.lights.LightsService.this.mLightsByType;
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public void setLightUnchecked(java.lang.Object object, int color, int mode, int onMS, int offMS, int brightnessMode) {
            if (object instanceof com.android.server.lights.LightsService.LightImpl) {
                com.android.server.lights.LightsService.LightImpl lightimpl = (com.android.server.lights.LightsService.LightImpl) object;
                lightimpl.setLightUnchecked(color, mode, onMS, offMS, brightnessMode);
            }
        }

        @Override // com.android.server.lights.ILightsServiceWrapper
        public void setLightLocked(java.lang.Object object, int color, int mode, int onMS, int offMS, int brightnessMode) {
            if (object instanceof com.android.server.lights.LightsService.LightImpl) {
                com.android.server.lights.LightsService.LightImpl lightimpl = (com.android.server.lights.LightsService.LightImpl) object;
                lightimpl.setLightLocked(color, mode, onMS, offMS, brightnessMode);
            }
        }
    }
}
