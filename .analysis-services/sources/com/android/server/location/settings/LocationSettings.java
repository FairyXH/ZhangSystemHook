package com.android.server.location.settings;

/* JADX INFO: loaded from: classes2.dex */
public class LocationSettings {
    private static final java.lang.String LOCATION_DIRNAME = "location";
    private static final java.lang.String LOCATION_SETTINGS_FILENAME = "settings";
    final android.content.Context mContext;
    private final android.util.SparseArray<com.android.server.location.settings.LocationSettings.LocationUserSettingsStore> mUserSettings = new android.util.SparseArray<>(1);
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.settings.LocationSettings.LocationUserSettingsListener> mUserSettingsListeners = new java.util.concurrent.CopyOnWriteArrayList<>();

    public interface LocationUserSettingsListener {
        void onLocationUserSettingsChanged(int i, com.android.server.location.settings.LocationUserSettings locationUserSettings, com.android.server.location.settings.LocationUserSettings locationUserSettings2);
    }

    public LocationSettings(android.content.Context context) {
        this.mContext = context;
    }

    public final void registerLocationUserSettingsListener(com.android.server.location.settings.LocationSettings.LocationUserSettingsListener listener) {
        this.mUserSettingsListeners.add(listener);
    }

    public final void unregisterLocationUserSettingsListener(com.android.server.location.settings.LocationSettings.LocationUserSettingsListener listener) {
        this.mUserSettingsListeners.remove(listener);
    }

    protected java.io.File getUserSettingsDir(int userId) {
        return android.os.Environment.getDataSystemDeDirectory(userId);
    }

    protected com.android.server.location.settings.LocationSettings.LocationUserSettingsStore createUserSettingsStore(int userId, java.io.File file) {
        return new com.android.server.location.settings.LocationSettings.LocationUserSettingsStore(userId, file);
    }

    private com.android.server.location.settings.LocationSettings.LocationUserSettingsStore getUserSettingsStore(int userId) {
        com.android.server.location.settings.LocationSettings.LocationUserSettingsStore settingsStore;
        synchronized (this.mUserSettings) {
            settingsStore = this.mUserSettings.get(userId);
            if (settingsStore == null) {
                java.io.File file = new java.io.File(new java.io.File(getUserSettingsDir(userId), LOCATION_DIRNAME), LOCATION_SETTINGS_FILENAME);
                settingsStore = createUserSettingsStore(userId, file);
                this.mUserSettings.put(userId, settingsStore);
            }
        }
        return settingsStore;
    }

    public final com.android.server.location.settings.LocationUserSettings getUserSettings(int userId) {
        return getUserSettingsStore(userId).get();
    }

    public final void updateUserSettings(int userId, java.util.function.Function<com.android.server.location.settings.LocationUserSettings, com.android.server.location.settings.LocationUserSettings> updater) {
        getUserSettingsStore(userId).update(updater);
    }

    public final void dump(java.io.FileDescriptor fd, android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        try {
            int[] userIds = android.app.ActivityManager.getService().getRunningUserIds();
            if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
                ipw.print("ADAS Location Setting: ");
                ipw.increaseIndent();
                if (userIds.length > 1) {
                    ipw.println();
                    for (int userId : userIds) {
                        ipw.print("[u");
                        ipw.print(userId);
                        ipw.print("] ");
                        ipw.println(getUserSettings(userId).isAdasGnssLocationEnabled());
                    }
                } else {
                    ipw.println(getUserSettings(userIds[0]).isAdasGnssLocationEnabled());
                }
                ipw.decreaseIndent();
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    final void flushFiles() throws java.lang.InterruptedException {
        synchronized (this.mUserSettings) {
            int size = this.mUserSettings.size();
            for (int i = 0; i < size; i++) {
                this.mUserSettings.valueAt(i).flushFile();
            }
        }
    }

    final void deleteFiles() throws java.lang.InterruptedException {
        synchronized (this.mUserSettings) {
            int size = this.mUserSettings.size();
            for (int i = 0; i < size; i++) {
                this.mUserSettings.valueAt(i).deleteFile();
            }
        }
    }

    protected final void fireListeners(int userId, com.android.server.location.settings.LocationUserSettings oldSettings, com.android.server.location.settings.LocationUserSettings newSettings) {
        for (com.android.server.location.settings.LocationSettings.LocationUserSettingsListener listener : this.mUserSettingsListeners) {
            listener.onLocationUserSettingsChanged(userId, oldSettings, newSettings);
        }
    }

    class LocationUserSettingsStore extends com.android.server.location.settings.SettingsStore<com.android.server.location.settings.LocationUserSettings> {
        protected final int mUserId;

        LocationUserSettingsStore(int userId, java.io.File file) {
            super(file);
            this.mUserId = userId;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.settings.SettingsStore
        public com.android.server.location.settings.LocationUserSettings read(int version, java.io.DataInput in) throws java.io.IOException {
            return filterSettings(com.android.server.location.settings.LocationUserSettings.read(com.android.server.location.settings.LocationSettings.this.mContext.getResources(), version, in));
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.settings.SettingsStore
        public void write(java.io.DataOutput out, com.android.server.location.settings.LocationUserSettings settings) throws java.io.IOException {
            settings.write(out);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.location.settings.LocationUserSettings lambda$update$0(java.util.function.Function updater, com.android.server.location.settings.LocationUserSettings settings) {
            return filterSettings((com.android.server.location.settings.LocationUserSettings) updater.apply(settings));
        }

        @Override // com.android.server.location.settings.SettingsStore
        public void update(final java.util.function.Function<com.android.server.location.settings.LocationUserSettings, com.android.server.location.settings.LocationUserSettings> updater) {
            super.update(new java.util.function.Function() { // from class: com.android.server.location.settings.LocationSettings$LocationUserSettingsStore$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$update$0(updater, (com.android.server.location.settings.LocationUserSettings) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChange$1(com.android.server.location.settings.LocationUserSettings oldSettings, com.android.server.location.settings.LocationUserSettings newSettings) {
            com.android.server.location.settings.LocationSettings.this.fireListeners(this.mUserId, oldSettings, newSettings);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.server.location.settings.SettingsStore
        public void onChange(final com.android.server.location.settings.LocationUserSettings oldSettings, final com.android.server.location.settings.LocationUserSettings newSettings) {
            com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.settings.LocationSettings$LocationUserSettingsStore$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onChange$1(oldSettings, newSettings);
                }
            });
        }

        private com.android.server.location.settings.LocationUserSettings filterSettings(com.android.server.location.settings.LocationUserSettings settings) {
            if (settings.isAdasGnssLocationEnabled() && !com.android.server.location.settings.LocationSettings.this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
                return settings.withAdasGnssLocationEnabled(false);
            }
            return settings;
        }
    }
}
