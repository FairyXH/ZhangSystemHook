package com.android.server.broadcastradio.hal2;

/* JADX INFO: loaded from: classes.dex */
public final class BroadcastRadioService {
    private static final java.lang.String TAG = "BcRadio2Srv";
    private int mNextModuleId;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Map<java.lang.String, java.lang.Integer> mServiceNameToModuleIdMap = new android.util.ArrayMap();
    private final java.util.Map<java.lang.Integer, com.android.server.broadcastradio.hal2.RadioModule> mModules = new android.util.ArrayMap();
    private final android.hidl.manager.V1_0.IServiceNotification.Stub mServiceListener = new android.hidl.manager.V1_0.IServiceNotification.Stub() { // from class: com.android.server.broadcastradio.hal2.BroadcastRadioService.1
        @Override // android.hidl.manager.V1_0.IServiceNotification
        public void onRegistration(java.lang.String fqName, java.lang.String serviceName, boolean preexisting) {
            com.android.server.utils.Slogf.v(com.android.server.broadcastradio.hal2.BroadcastRadioService.TAG, "onRegistration(" + fqName + ", " + serviceName + ", " + preexisting + ")");
            synchronized (com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mLock) {
                java.lang.Integer moduleId = (java.lang.Integer) com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mServiceNameToModuleIdMap.get(serviceName);
                boolean newService = false;
                if (moduleId == null) {
                    newService = true;
                    moduleId = java.lang.Integer.valueOf(com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mNextModuleId);
                }
                com.android.server.broadcastradio.hal2.RadioModule radioModule = com.android.server.broadcastradio.hal2.RadioModule.tryLoadingModule(moduleId.intValue(), serviceName);
                if (radioModule == null) {
                    return;
                }
                com.android.server.utils.Slogf.v(com.android.server.broadcastradio.hal2.BroadcastRadioService.TAG, "loaded broadcast radio module " + moduleId + ": " + serviceName + " (HAL 2.0)");
                com.android.server.broadcastradio.hal2.RadioModule prevModule = (com.android.server.broadcastradio.hal2.RadioModule) com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mModules.put(moduleId, radioModule);
                if (prevModule != null) {
                    prevModule.closeSessions(0);
                }
                if (newService) {
                    com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mServiceNameToModuleIdMap.put(serviceName, moduleId);
                    com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mNextModuleId++;
                }
                try {
                    radioModule.getService().linkToDeath(com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mDeathRecipient, moduleId.intValue());
                } catch (android.os.RemoteException e) {
                    com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mModules.remove(moduleId);
                }
            }
        }
    };
    private final android.os.IHwBinder.DeathRecipient mDeathRecipient = new android.os.IHwBinder.DeathRecipient() { // from class: com.android.server.broadcastradio.hal2.BroadcastRadioService.2
        public void serviceDied(long cookie) {
            com.android.server.utils.Slogf.v(com.android.server.broadcastradio.hal2.BroadcastRadioService.TAG, "serviceDied(" + cookie + ")");
            synchronized (com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mLock) {
                int moduleId = (int) cookie;
                com.android.server.broadcastradio.hal2.RadioModule prevModule = (com.android.server.broadcastradio.hal2.RadioModule) com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mModules.remove(java.lang.Integer.valueOf(moduleId));
                if (prevModule != null) {
                    prevModule.closeSessions(0);
                }
                java.util.Iterator it = com.android.server.broadcastradio.hal2.BroadcastRadioService.this.mServiceNameToModuleIdMap.entrySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    java.util.Map.Entry<java.lang.String, java.lang.Integer> entry = (java.util.Map.Entry) it.next();
                    if (entry.getValue().intValue() == moduleId) {
                        com.android.server.utils.Slogf.i(com.android.server.broadcastradio.hal2.BroadcastRadioService.TAG, "service " + entry.getKey() + " died; removed RadioModule with ID " + moduleId);
                        break;
                    }
                }
            }
        }
    };

    public BroadcastRadioService(int nextModuleId) {
        this.mNextModuleId = nextModuleId;
        try {
            android.hidl.manager.V1_0.IServiceManager manager = android.hidl.manager.V1_0.IServiceManager.getService();
            if (manager == null) {
                com.android.server.utils.Slogf.e(TAG, "failed to get HIDL Service Manager");
            } else {
                manager.registerForNotifications(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName, "", this.mServiceListener);
            }
        } catch (android.os.RemoteException ex) {
            com.android.server.utils.Slogf.e(TAG, "failed to register for service notifications: ", ex);
        }
    }

    BroadcastRadioService(int nextModuleId, android.hidl.manager.V1_0.IServiceManager manager) {
        this.mNextModuleId = nextModuleId;
        java.util.Objects.requireNonNull(manager, "Service manager cannot be null");
        try {
            manager.registerForNotifications(android.hardware.broadcastradio.V2_0.IBroadcastRadio.kInterfaceName, "", this.mServiceListener);
        } catch (android.os.RemoteException ex) {
            com.android.server.utils.Slogf.e(TAG, "Failed to register for service notifications: ", ex);
        }
    }

    public java.util.Collection<android.hardware.radio.RadioManager.ModuleProperties> listModules() {
        java.util.Collection<android.hardware.radio.RadioManager.ModuleProperties> collection;
        com.android.server.utils.Slogf.v(TAG, "List HIDL 2.0 modules");
        synchronized (this.mLock) {
            collection = (java.util.Collection) this.mModules.values().stream().map(new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.BroadcastRadioService$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((com.android.server.broadcastradio.hal2.RadioModule) obj).getProperties();
                }
            }).collect(java.util.stream.Collectors.toList());
        }
        return collection;
    }

    public boolean hasModule(int id) {
        boolean zContainsKey;
        synchronized (this.mLock) {
            zContainsKey = this.mModules.containsKey(java.lang.Integer.valueOf(id));
        }
        return zContainsKey;
    }

    public boolean hasAnyModules() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mModules.isEmpty();
        }
        return z;
    }

    public android.hardware.radio.ITuner openSession(int moduleId, android.hardware.radio.RadioManager.BandConfig legacyConfig, boolean withAudio, android.hardware.radio.ITunerCallback callback) throws android.os.RemoteException {
        com.android.server.broadcastradio.hal2.RadioModule module;
        com.android.server.utils.Slogf.v(TAG, "Open HIDL 2.0 session with module id " + moduleId);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.e(TAG, "Cannot open tuner on HAL 2.0 client for non-current user");
            throw new java.lang.IllegalStateException("Cannot open session for non-current user");
        }
        java.util.Objects.requireNonNull(callback);
        if (!withAudio) {
            throw new java.lang.IllegalArgumentException("Non-audio sessions not supported with HAL 2.0");
        }
        synchronized (this.mLock) {
            module = this.mModules.get(java.lang.Integer.valueOf(moduleId));
            if (module == null) {
                throw new java.lang.IllegalArgumentException("Invalid module ID");
            }
        }
        com.android.server.broadcastradio.hal2.TunerSession tunerSession = module.openSession(callback);
        if (legacyConfig != null) {
            tunerSession.setConfiguration(legacyConfig);
        }
        return tunerSession;
    }

    public android.hardware.radio.ICloseHandle addAnnouncementListener(int[] enabledTypes, android.hardware.radio.IAnnouncementListener listener) {
        com.android.server.utils.Slogf.v(TAG, "Add announcementListener");
        com.android.server.broadcastradio.hal2.AnnouncementAggregator aggregator = new com.android.server.broadcastradio.hal2.AnnouncementAggregator(listener, this.mLock);
        boolean anySupported = false;
        synchronized (this.mLock) {
            for (com.android.server.broadcastradio.hal2.RadioModule module : this.mModules.values()) {
                try {
                    aggregator.watchModule(module, enabledTypes);
                    anySupported = true;
                } catch (java.lang.UnsupportedOperationException ex) {
                    com.android.server.utils.Slogf.v(TAG, "Announcements not supported for this module", ex);
                }
            }
        }
        if (!anySupported) {
            com.android.server.utils.Slogf.i(TAG, "There are no HAL modules that support announcements");
        }
        return aggregator;
    }

    public void dumpInfo(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.printf("Next module id available: %d\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mNextModuleId)});
            pw.printf("ServiceName to module id map:\n", new java.lang.Object[0]);
            pw.increaseIndent();
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : this.mServiceNameToModuleIdMap.entrySet()) {
                pw.printf("Service name: %s, module id: %d\n", new java.lang.Object[]{entry.getKey(), entry.getValue()});
            }
            pw.decreaseIndent();
            pw.printf("Radio modules:\n", new java.lang.Object[0]);
            pw.increaseIndent();
            for (java.util.Map.Entry<java.lang.Integer, com.android.server.broadcastradio.hal2.RadioModule> moduleEntry : this.mModules.entrySet()) {
                pw.printf("Module id=%d:\n", new java.lang.Object[]{moduleEntry.getKey()});
                pw.increaseIndent();
                moduleEntry.getValue().dumpInfo(pw);
                pw.decreaseIndent();
            }
            pw.decreaseIndent();
        }
    }
}
