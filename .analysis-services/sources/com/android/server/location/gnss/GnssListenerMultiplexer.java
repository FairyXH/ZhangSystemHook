package com.android.server.location.gnss;

/* JADX INFO: loaded from: classes2.dex */
public abstract class GnssListenerMultiplexer<TRequest, TListener extends android.os.IInterface, TMergedRegistration> extends com.android.server.location.listeners.ListenerMultiplexer<android.os.IBinder, TListener, com.android.server.location.gnss.GnssListenerMultiplexer<TRequest, TListener, TMergedRegistration>.GnssListenerRegistration, TMergedRegistration> {
    protected final com.android.server.location.injector.AppForegroundHelper mAppForegroundHelper;
    protected final com.android.server.location.injector.LocationPermissionsHelper mLocationPermissionsHelper;
    private final com.android.server.location.injector.PackageResetHelper mPackageResetHelper;
    protected final com.android.server.location.injector.SettingsHelper mSettingsHelper;
    protected final com.android.server.location.injector.UserInfoHelper mUserInfoHelper;
    private final com.android.server.location.injector.UserInfoHelper.UserListener mUserChangedListener = new com.android.server.location.injector.UserInfoHelper.UserListener() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda0
        @Override // com.android.server.location.injector.UserInfoHelper.UserListener
        public final void onUserChanged(int i, int i2) {
            this.f$0.onUserChanged(i, i2);
        }
    };
    private final android.location.LocationManagerInternal.ProviderEnabledListener mProviderEnabledChangedListener = new android.location.LocationManagerInternal.ProviderEnabledListener() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda1
        public final void onProviderEnabledChanged(java.lang.String str, int i, boolean z) {
            this.f$0.onProviderEnabledChanged(str, i, z);
        }
    };
    private final com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener mBackgroundThrottlePackageWhitelistChangedListener = new com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda2
        @Override // com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener
        public final void onSettingChanged() {
            this.f$0.onBackgroundThrottlePackageAllowlistChanged();
        }
    };
    private final com.android.server.location.injector.SettingsHelper.UserSettingChangedListener mLocationPackageBlacklistChangedListener = new com.android.server.location.injector.SettingsHelper.UserSettingChangedListener() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda3
        @Override // com.android.server.location.injector.SettingsHelper.UserSettingChangedListener
        public final void onSettingChanged(int i) {
            this.f$0.onLocationPackageDenylistChanged(i);
        }
    };
    private final com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener mLocationPermissionsListener = new com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer.1
        @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
        public void onLocationPermissionsChanged(java.lang.String packageName) {
            com.android.server.location.gnss.GnssListenerMultiplexer.this.onLocationPermissionsChanged(packageName);
        }

        @Override // com.android.server.location.injector.LocationPermissionsHelper.LocationPermissionsListener
        public void onLocationPermissionsChanged(int uid) {
            com.android.server.location.gnss.GnssListenerMultiplexer.this.onLocationPermissionsChanged(uid);
        }
    };
    private final com.android.server.location.injector.AppForegroundHelper.AppForegroundListener mAppForegroundChangedListener = new com.android.server.location.injector.AppForegroundHelper.AppForegroundListener() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda4
        @Override // com.android.server.location.injector.AppForegroundHelper.AppForegroundListener
        public final void onAppForegroundChanged(int i, boolean z) {
            this.f$0.onAppForegroundChanged(i, z);
        }
    };
    private final com.android.server.location.injector.PackageResetHelper.Responder mPackageResetResponder = new com.android.server.location.injector.PackageResetHelper.Responder() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer.2
        @Override // com.android.server.location.injector.PackageResetHelper.Responder
        public void onPackageReset(java.lang.String packageName) {
            com.android.server.location.gnss.GnssListenerMultiplexer.this.onPackageReset(packageName);
        }

        @Override // com.android.server.location.injector.PackageResetHelper.Responder
        public boolean isResetableForPackage(java.lang.String packageName) {
            return com.android.server.location.gnss.GnssListenerMultiplexer.this.isResetableForPackage(packageName);
        }
    };
    private com.android.server.location.gnss.IGnssListenerMultiplexerExt mGnssStatusProviderExtImpl = (com.android.server.location.gnss.IGnssListenerMultiplexerExt) system.ext.loader.core.ExtLoader.type(com.android.server.location.gnss.IGnssListenerMultiplexerExt.class).base(this).create();
    protected final android.location.LocationManagerInternal mLocationManagerInternal = (android.location.LocationManagerInternal) java.util.Objects.requireNonNull((android.location.LocationManagerInternal) com.android.server.LocalServices.getService(android.location.LocationManagerInternal.class));

    protected class GnssListenerRegistration extends com.android.server.location.listeners.BinderListenerRegistration<android.os.IBinder, TListener> {
        private boolean mForeground;
        private final android.location.util.identity.CallerIdentity mIdentity;
        private boolean mPermitted;
        private final TRequest mRequest;

        protected GnssListenerRegistration(TRequest request, android.location.util.identity.CallerIdentity identity, TListener listener) {
            super(identity.isMyProcess() ? com.android.server.FgThread.getExecutor() : com.android.internal.util.ConcurrentUtils.DIRECT_EXECUTOR, listener);
            this.mRequest = request;
            this.mIdentity = identity;
        }

        public final TRequest getRequest() {
            return this.mRequest;
        }

        public final android.location.util.identity.CallerIdentity getIdentity() {
            return this.mIdentity;
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public java.lang.String getTag() {
            return com.android.server.location.gnss.GnssManagerService.TAG;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.RemovableListenerRegistration
        public com.android.server.location.gnss.GnssListenerMultiplexer<TRequest, TListener, TMergedRegistration> getOwner() {
            return com.android.server.location.gnss.GnssListenerMultiplexer.this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.listeners.BinderListenerRegistration
        public android.os.IBinder getBinderFromKey(android.os.IBinder key) {
            return key;
        }

        public boolean isForeground() {
            return this.mForeground;
        }

        boolean isPermitted() {
            return this.mPermitted;
        }

        @Override // com.android.server.location.listeners.BinderListenerRegistration, com.android.server.location.listeners.RemovableListenerRegistration
        protected void onRegister() {
            super.onRegister();
            this.mPermitted = com.android.server.location.gnss.GnssListenerMultiplexer.this.mLocationPermissionsHelper.hasLocationPermissions(2, this.mIdentity);
            this.mForeground = com.android.server.location.gnss.GnssListenerMultiplexer.this.mAppForegroundHelper.isAppForeground(this.mIdentity.getUid());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean onLocationPermissionsChanged(java.lang.String packageName) {
            if (packageName == null || this.mIdentity.getPackageName().equals(packageName)) {
                return onLocationPermissionsChanged();
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean onLocationPermissionsChanged(int uid) {
            if (this.mIdentity.getUid() == uid) {
                return onLocationPermissionsChanged();
            }
            return false;
        }

        private boolean onLocationPermissionsChanged() {
            boolean permitted = com.android.server.location.gnss.GnssListenerMultiplexer.this.mLocationPermissionsHelper.hasLocationPermissions(2, this.mIdentity);
            if (permitted != this.mPermitted) {
                this.mPermitted = permitted;
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean onForegroundChanged(int uid, boolean foreground) {
            if (this.mIdentity.getUid() == uid && foreground != this.mForeground) {
                this.mForeground = foreground;
                return true;
            }
            return false;
        }

        @Override // com.android.server.location.listeners.ListenerRegistration
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append(this.mIdentity);
            android.util.ArraySet<java.lang.String> flags = new android.util.ArraySet<>(2);
            if (!this.mForeground) {
                flags.add("bg");
            }
            if (!this.mPermitted) {
                flags.add("na");
            }
            if (!flags.isEmpty()) {
                builder.append(" ").append(flags);
            }
            if (this.mRequest != null) {
                builder.append(" ").append(this.mRequest);
            }
            return builder.toString();
        }
    }

    protected GnssListenerMultiplexer(com.android.server.location.injector.Injector injector) {
        this.mUserInfoHelper = injector.getUserInfoHelper();
        this.mSettingsHelper = injector.getSettingsHelper();
        this.mLocationPermissionsHelper = injector.getLocationPermissionsHelper();
        this.mAppForegroundHelper = injector.getAppForegroundHelper();
        this.mPackageResetHelper = injector.getPackageResetHelper();
    }

    public boolean isSupported() {
        return true;
    }

    protected void addListener(android.location.util.identity.CallerIdentity identity, TListener listener) {
        addListener(null, identity, listener);
    }

    protected void addListener(TRequest request, android.location.util.identity.CallerIdentity callerIdentity, TListener listener) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            putRegistration(listener.asBinder(), createRegistration(request, callerIdentity, listener));
            this.mGnssStatusProviderExtImpl.addProxyBinder(listener.asBinder(), listener, callerIdentity.getUid(), callerIdentity.getPid());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    protected com.android.server.location.gnss.GnssListenerMultiplexer<TRequest, TListener, TMergedRegistration>.GnssListenerRegistration createRegistration(TRequest request, android.location.util.identity.CallerIdentity callerIdentity, TListener listener) {
        return new com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration(request, callerIdentity, listener);
    }

    public void removeListener(TListener listener) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mGnssStatusProviderExtImpl.removeProxyBinder(listener.asBinder(), listener);
            removeRegistration(listener.asBinder());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.location.listeners.ListenerMultiplexer
    public boolean isActive(com.android.server.location.gnss.GnssListenerMultiplexer<TRequest, TListener, TMergedRegistration>.GnssListenerRegistration registration) {
        if (!isSupported()) {
            return false;
        }
        android.location.util.identity.CallerIdentity identity = registration.getIdentity();
        if (registration.isPermitted()) {
            return (registration.isForeground() || isBackgroundRestrictionExempt(identity)) && isActive(identity);
        }
        return false;
    }

    private boolean isActive(android.location.util.identity.CallerIdentity identity) {
        return identity.isSystemServer() ? this.mLocationManagerInternal.isProviderEnabledForUser(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, this.mUserInfoHelper.getCurrentUserId()) : this.mLocationManagerInternal.isProviderEnabledForUser(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, identity.getUserId()) && this.mUserInfoHelper.isVisibleUserId(identity.getUserId()) && !this.mSettingsHelper.isLocationPackageBlacklisted(identity.getUserId(), identity.getPackageName());
    }

    private boolean isBackgroundRestrictionExempt(android.location.util.identity.CallerIdentity identity) {
        if (identity.getUid() == 1000 || this.mSettingsHelper.getBackgroundThrottlePackageWhitelist().contains(identity.getPackageName())) {
            return true;
        }
        return this.mLocationManagerInternal.isProvider((java.lang.String) null, identity);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected TMergedRegistration mergeRegistrations(java.util.Collection<com.android.server.location.gnss.GnssListenerMultiplexer<TRequest, TListener, TMergedRegistration>.GnssListenerRegistration> gnssListenerRegistrations) {
        if (android.os.Build.IS_DEBUGGABLE) {
            for (com.android.server.location.gnss.GnssListenerMultiplexer<TRequest, TListener, TMergedRegistration>.GnssListenerRegistration registration : gnssListenerRegistrations) {
                com.android.internal.util.Preconditions.checkState(registration.getRequest() == null);
            }
            return null;
        }
        return null;
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onRegister() {
        if (!isSupported()) {
            return;
        }
        this.mUserInfoHelper.addListener(this.mUserChangedListener);
        this.mLocationManagerInternal.addProviderEnabledListener(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, this.mProviderEnabledChangedListener);
        this.mSettingsHelper.addOnBackgroundThrottlePackageWhitelistChangedListener(this.mBackgroundThrottlePackageWhitelistChangedListener);
        this.mSettingsHelper.addOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mLocationPermissionsHelper.addListener(this.mLocationPermissionsListener);
        this.mAppForegroundHelper.addListener(this.mAppForegroundChangedListener);
        this.mPackageResetHelper.register(this.mPackageResetResponder);
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected void onUnregister() {
        if (!isSupported()) {
            return;
        }
        this.mUserInfoHelper.removeListener(this.mUserChangedListener);
        this.mLocationManagerInternal.removeProviderEnabledListener(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS, this.mProviderEnabledChangedListener);
        this.mSettingsHelper.removeOnBackgroundThrottlePackageWhitelistChangedListener(this.mBackgroundThrottlePackageWhitelistChangedListener);
        this.mSettingsHelper.removeOnLocationPackageBlacklistChangedListener(this.mLocationPackageBlacklistChangedListener);
        this.mLocationPermissionsHelper.removeListener(this.mLocationPermissionsListener);
        this.mAppForegroundHelper.removeListener(this.mAppForegroundChangedListener);
        this.mPackageResetHelper.unregister(this.mPackageResetResponder);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onUserChanged(final int userId, int change) {
        if (change == 1 || change == 4) {
            updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda8
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.location.gnss.GnssListenerMultiplexer.lambda$onUserChanged$0(userId, (com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$onUserChanged$0(int userId, com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onProviderEnabledChanged(java.lang.String provider, final int userId, boolean enabled) {
        com.android.internal.util.Preconditions.checkState(com.android.server.am.IOplusSceneManager.APP_SCENE_GPS.equals(provider));
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.gnss.GnssListenerMultiplexer.lambda$onProviderEnabledChanged$1(userId, (com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$onProviderEnabledChanged$1(int userId, com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    static /* synthetic */ boolean lambda$onBackgroundThrottlePackageAllowlistChanged$2(com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration registration) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBackgroundThrottlePackageAllowlistChanged() {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.gnss.GnssListenerMultiplexer.lambda$onBackgroundThrottlePackageAllowlistChanged$2((com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$onLocationPackageDenylistChanged$3(int userId, com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration registration) {
        return registration.getIdentity().getUserId() == userId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPackageDenylistChanged(final int userId) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.gnss.GnssListenerMultiplexer.lambda$onLocationPackageDenylistChanged$3(userId, (com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPermissionsChanged(final java.lang.String packageName) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj).onLocationPermissionsChanged(packageName);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLocationPermissionsChanged(final int uid) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj).onLocationPermissionsChanged(uid);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppForegroundChanged(final int uid, final boolean foreground) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj).onForegroundChanged(uid, foreground);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPackageReset(final java.lang.String packageName) {
        updateRegistrations(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.location.gnss.GnssListenerMultiplexer.lambda$onPackageReset$7(packageName, (com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$onPackageReset$7(java.lang.String packageName, com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration registration) {
        if (registration.getIdentity().getPackageName().equals(packageName)) {
            registration.remove();
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isResetableForPackage(final java.lang.String packageName) {
        return findRegistration(new java.util.function.Predicate() { // from class: com.android.server.location.gnss.GnssListenerMultiplexer$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.server.location.gnss.GnssListenerMultiplexer.GnssListenerRegistration) obj).getIdentity().getPackageName().equals(packageName);
            }
        });
    }

    @Override // com.android.server.location.listeners.ListenerMultiplexer
    protected java.lang.String getServiceState() {
        if (!isSupported()) {
            return "unsupported";
        }
        return super.getServiceState();
    }
}
