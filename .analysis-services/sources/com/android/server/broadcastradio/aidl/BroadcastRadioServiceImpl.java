package com.android.server.broadcastradio.aidl;

/* JADX INFO: loaded from: classes.dex */
public final class BroadcastRadioServiceImpl {
    private static final java.lang.String TAG = "BcRadioAidlSrv";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Map<java.lang.String, java.lang.Integer> mServiceNameToModuleIdMap = new android.util.ArrayMap();
    private final android.util.SparseArray<com.android.server.broadcastradio.aidl.RadioModule> mModules = new android.util.SparseArray<>();
    private final android.os.IServiceCallback.Stub mServiceListener = new android.os.IServiceCallback.Stub() { // from class: com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.1
        public void onRegistration(java.lang.String name, android.os.IBinder newBinder) {
            com.android.server.utils.Slogf.i(com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.TAG, "onRegistration for %s", name);
            synchronized (com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mLock) {
                java.lang.Integer moduleId = (java.lang.Integer) com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mServiceNameToModuleIdMap.get(name);
                boolean newService = false;
                if (moduleId == null) {
                    newService = true;
                    moduleId = java.lang.Integer.valueOf(com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mNextModuleId);
                }
                com.android.server.broadcastradio.aidl.RadioModule radioModule = com.android.server.broadcastradio.aidl.RadioModule.tryLoadingModule(moduleId.intValue(), name, newBinder);
                if (radioModule == null) {
                    com.android.server.utils.Slogf.w(com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.TAG, "No module %s with id %d (HAL AIDL)", name, moduleId);
                    return;
                }
                if (com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.DEBUG) {
                    com.android.server.utils.Slogf.d(com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.TAG, "Loaded broadcast radio module %s with id %d (HAL AIDL)", name, moduleId);
                }
                com.android.server.broadcastradio.aidl.RadioModule prevModule = (com.android.server.broadcastradio.aidl.RadioModule) com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mModules.get(moduleId.intValue());
                com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mModules.put(moduleId.intValue(), radioModule);
                if (prevModule != null) {
                    prevModule.closeSessions(0);
                }
                if (newService) {
                    com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mServiceNameToModuleIdMap.put(name, moduleId);
                    com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mNextModuleId++;
                }
                try {
                    com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.BroadcastRadioDeathRecipient deathRecipient = com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.new BroadcastRadioDeathRecipient(moduleId.intValue());
                    radioModule.getService().asBinder().linkToDeath(deathRecipient, moduleId.intValue());
                } catch (android.os.RemoteException e) {
                    com.android.server.utils.Slogf.w(com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.TAG, "Service has already died, so remove its entry from mModules.");
                    com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mModules.remove(moduleId.intValue());
                }
            }
        }
    };
    private int mNextModuleId = 0;

    private final class BroadcastRadioDeathRecipient implements android.os.IBinder.DeathRecipient {
        private final int mModuleId;

        BroadcastRadioDeathRecipient(int moduleId) {
            this.mModuleId = moduleId;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.utils.Slogf.i(com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.TAG, "ServiceDied for module id %d", java.lang.Integer.valueOf(this.mModuleId));
            synchronized (com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mLock) {
                com.android.server.broadcastradio.aidl.RadioModule prevModule = (com.android.server.broadcastradio.aidl.RadioModule) com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mModules.removeReturnOld(this.mModuleId);
                if (prevModule != null) {
                    prevModule.closeSessions(0);
                }
                java.util.Iterator it = com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.this.mServiceNameToModuleIdMap.entrySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    java.util.Map.Entry<java.lang.String, java.lang.Integer> entry = (java.util.Map.Entry) it.next();
                    if (entry.getValue().intValue() == this.mModuleId) {
                        com.android.server.utils.Slogf.w(com.android.server.broadcastradio.aidl.BroadcastRadioServiceImpl.TAG, "Service %s died, removed RadioModule with ID %d", entry.getKey(), java.lang.Integer.valueOf(this.mModuleId));
                        break;
                    }
                }
            }
        }
    }

    public BroadcastRadioServiceImpl(java.util.ArrayList<java.lang.String> serviceNameList) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "Initializing BroadcastRadioServiceImpl %s", android.hardware.broadcastradio.IBroadcastRadio.DESCRIPTOR);
        }
        for (int i = 0; i < serviceNameList.size(); i++) {
            try {
                android.os.ServiceManager.registerForNotifications(serviceNameList.get(i), this.mServiceListener);
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, ex, "failed to register for service notifications for service %s", serviceNameList.get(i));
            }
        }
    }

    public java.util.List<android.hardware.radio.RadioManager.ModuleProperties> listModules() {
        java.util.List<android.hardware.radio.RadioManager.ModuleProperties> moduleList;
        synchronized (this.mLock) {
            moduleList = new java.util.ArrayList<>(this.mModules.size());
            for (int i = 0; i < this.mModules.size(); i++) {
                moduleList.add(this.mModules.valueAt(i).getProperties());
            }
        }
        return moduleList;
    }

    public boolean hasModule(int id) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = this.mModules.contains(id);
        }
        return zContains;
    }

    public boolean hasAnyModules() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mModules.size() != 0;
        }
        return z;
    }

    public android.hardware.radio.ITuner openSession(int moduleId, android.hardware.radio.RadioManager.BandConfig legacyConfig, boolean withAudio, android.hardware.radio.ITunerCallback callback) throws android.os.RemoteException {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "Open AIDL radio session");
        }
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.e(TAG, "Cannot open tuner on AIDL HAL client for non-current user");
            throw new java.lang.IllegalStateException("Cannot open session for non-current user");
        }
        java.util.Objects.requireNonNull(callback);
        if (!withAudio) {
            throw new java.lang.IllegalArgumentException("Non-audio sessions not supported with AIDL HAL");
        }
        synchronized (this.mLock) {
            com.android.server.broadcastradio.aidl.RadioModule radioModule = this.mModules.get(moduleId);
            if (radioModule == null) {
                com.android.server.utils.Slogf.e(TAG, "Invalid module ID %d", java.lang.Integer.valueOf(moduleId));
                return null;
            }
            com.android.server.broadcastradio.aidl.TunerSession tunerSession = radioModule.openSession(callback);
            if (legacyConfig != null) {
                tunerSession.setConfiguration(legacyConfig);
            }
            return tunerSession;
        }
    }

    public android.hardware.radio.ICloseHandle addAnnouncementListener(int[] enabledTypes, android.hardware.radio.IAnnouncementListener listener) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "Add AnnouncementListener with enable types %s", java.util.Arrays.toString(enabledTypes));
        }
        com.android.server.broadcastradio.aidl.AnnouncementAggregator aggregator = new com.android.server.broadcastradio.aidl.AnnouncementAggregator(listener, this.mLock);
        boolean anySupported = false;
        synchronized (this.mLock) {
            for (int i = 0; i < this.mModules.size(); i++) {
                try {
                    aggregator.watchModule(this.mModules.valueAt(i), enabledTypes);
                    anySupported = true;
                } catch (java.lang.UnsupportedOperationException ex) {
                    com.android.server.utils.Slogf.w(TAG, ex, "Announcements not supported for this module", new java.lang.Object[0]);
                }
            }
        }
        if (!anySupported) {
            com.android.server.utils.Slogf.w(TAG, "There are no HAL modules that support announcements");
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
            pw.printf("Radio modules [%d]:\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mModules.size())});
            pw.increaseIndent();
            for (int i = 0; i < this.mModules.size(); i++) {
                pw.printf("Module id=%d:\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mModules.keyAt(i))});
                pw.increaseIndent();
                this.mModules.valueAt(i).dumpInfo(pw);
                pw.decreaseIndent();
            }
            pw.decreaseIndent();
        }
    }
}
