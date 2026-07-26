package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayAreaOrganizerController extends android.window.IDisplayAreaOrganizerController.Stub {
    private static final java.lang.String TAG = "DisplayAreaOrganizerController";
    private final com.android.server.wm.WindowManagerGlobalLock mGlobalLock;
    private int mNextTaskDisplayAreaFeatureId = com.android.bluetooth.BluetoothStatsLog.BLUETOOTH_CONTENT_PROFILE_ERROR_REPORTED__FILE_NAME__BLUETOOTH_PBAP_AUTHENTICATOR;
    private final java.util.HashMap<java.lang.Integer, com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState> mOrganizersByFeatureIds = new java.util.HashMap<>();
    final com.android.server.wm.ActivityTaskManagerService mService;

    private class DeathRecipient implements android.os.IBinder.DeathRecipient {
        int mFeature;
        android.window.IDisplayAreaOrganizer mOrganizer;

        DeathRecipient(android.window.IDisplayAreaOrganizer organizer, int feature) {
            this.mOrganizer = organizer;
            this.mFeature = feature;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = com.android.server.wm.DisplayAreaOrganizerController.this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    android.window.IDisplayAreaOrganizer featureOrganizer = com.android.server.wm.DisplayAreaOrganizerController.this.getOrganizerByFeature(this.mFeature);
                    if (featureOrganizer != null) {
                        android.os.IBinder organizerBinder = featureOrganizer.asBinder();
                        if (!organizerBinder.equals(this.mOrganizer.asBinder()) && organizerBinder.isBinderAlive()) {
                            android.util.Slog.d(com.android.server.wm.DisplayAreaOrganizerController.TAG, "Dead organizer replaced for feature=" + this.mFeature);
                            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                            return;
                        }
                        ((com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState) com.android.server.wm.DisplayAreaOrganizerController.this.mOrganizersByFeatureIds.remove(java.lang.Integer.valueOf(this.mFeature))).destroy();
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class DisplayAreaOrganizerState {
        private final com.android.server.wm.DisplayAreaOrganizerController.DeathRecipient mDeathRecipient;
        private final android.window.IDisplayAreaOrganizer mOrganizer;

        DisplayAreaOrganizerState(android.window.IDisplayAreaOrganizer organizer, int feature) {
            this.mOrganizer = organizer;
            this.mDeathRecipient = com.android.server.wm.DisplayAreaOrganizerController.this.new DeathRecipient(organizer, feature);
            try {
                organizer.asBinder().linkToDeath(this.mDeathRecipient, 0);
            } catch (android.os.RemoteException e) {
            }
        }

        void destroy() {
            final android.os.IBinder organizerBinder = this.mOrganizer.asBinder();
            com.android.server.wm.DisplayAreaOrganizerController.this.mService.mRootWindowContainer.forAllDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayAreaOrganizerController$DisplayAreaOrganizerState$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$destroy$0(organizerBinder, (com.android.server.wm.DisplayArea) obj);
                }
            });
            organizerBinder.unlinkToDeath(this.mDeathRecipient, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$destroy$0(android.os.IBinder organizerBinder, com.android.server.wm.DisplayArea da) {
            if (da.mOrganizer != null && da.mOrganizer.asBinder().equals(organizerBinder)) {
                if (da.isTaskDisplayArea() && da.asTaskDisplayArea().mCreatedByOrganizer) {
                    com.android.server.wm.DisplayAreaOrganizerController.this.deleteTaskDisplayArea(da.asTaskDisplayArea());
                } else {
                    da.setOrganizer(null);
                }
            }
        }
    }

    DisplayAreaOrganizerController(com.android.server.wm.ActivityTaskManagerService atm) {
        this.mService = atm;
        this.mGlobalLock = atm.mGlobalLock;
    }

    private void enforceTaskPermission(java.lang.String func) {
        com.android.server.wm.ActivityTaskManagerService.enforceTaskPermission(func);
    }

    android.window.IDisplayAreaOrganizer getOrganizerByFeature(int featureId) {
        com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState state = this.mOrganizersByFeatureIds.get(java.lang.Integer.valueOf(featureId));
        if (state != null) {
            return state.mOrganizer;
        }
        return null;
    }

    public android.content.pm.ParceledListSlice<android.window.DisplayAreaAppearedInfo> registerOrganizer(final android.window.IDisplayAreaOrganizer organizer, final int feature) {
        android.content.pm.ParceledListSlice<android.window.DisplayAreaAppearedInfo> parceledListSlice;
        enforceTaskPermission("registerOrganizer()");
        long uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 3968604152682328317L, 4, null, protoLogParam0, java.lang.Long.valueOf(uid));
                    }
                    if (this.mOrganizersByFeatureIds.get(java.lang.Integer.valueOf(feature)) != null) {
                        this.mOrganizersByFeatureIds.remove(java.lang.Integer.valueOf(feature)).destroy();
                        android.util.Slog.d(TAG, "Replacing dead organizer for feature=" + feature);
                    }
                    com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState state = new com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState(organizer, feature);
                    final java.util.List<android.window.DisplayAreaAppearedInfo> displayAreaInfos = new java.util.ArrayList<>();
                    this.mService.mRootWindowContainer.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayAreaOrganizerController$$ExternalSyntheticLambda5
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            this.f$0.lambda$registerOrganizer$1(feature, displayAreaInfos, organizer, (com.android.server.wm.DisplayContent) obj);
                        }
                    });
                    this.mOrganizersByFeatureIds.put(java.lang.Integer.valueOf(feature), state);
                    parceledListSlice = new android.content.pm.ParceledListSlice<>(displayAreaInfos);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return parceledListSlice;
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerOrganizer$1(final int feature, final java.util.List displayAreaInfos, final android.window.IDisplayAreaOrganizer organizer, com.android.server.wm.DisplayContent dc) {
        if (!dc.isTrusted()) {
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[3]) {
                long protoLogParam0 = dc.getDisplayId();
                com.android.internal.protolog.ProtoLogImpl_209941506.w(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -3066370283926570943L, 1, null, java.lang.Long.valueOf(protoLogParam0));
                return;
            }
            return;
        }
        dc.forAllDisplayAreas(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayAreaOrganizerController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$registerOrganizer$0(feature, displayAreaInfos, organizer, (com.android.server.wm.DisplayArea) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerOrganizer$0(int feature, java.util.List displayAreaInfos, android.window.IDisplayAreaOrganizer organizer, com.android.server.wm.DisplayArea da) {
        if (da.mFeatureId != feature) {
            return;
        }
        displayAreaInfos.add(organizeDisplayArea(organizer, da, "DisplayAreaOrganizerController.registerOrganizer"));
    }

    public void unregisterOrganizer(final android.window.IDisplayAreaOrganizer organizer) {
        enforceTaskPermission("unregisterTaskOrganizer()");
        long uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                        java.lang.String protoLogParam0 = java.lang.String.valueOf(organizer.asBinder());
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -943497726140336963L, 4, null, protoLogParam0, java.lang.Long.valueOf(uid));
                    }
                    this.mOrganizersByFeatureIds.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.wm.DisplayAreaOrganizerController$$ExternalSyntheticLambda2
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.wm.DisplayAreaOrganizerController.lambda$unregisterOrganizer$2(organizer, (java.util.Map.Entry) obj);
                        }
                    });
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    static /* synthetic */ boolean lambda$unregisterOrganizer$2(android.window.IDisplayAreaOrganizer organizer, java.util.Map.Entry entry) {
        boolean matches = ((com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState) entry.getValue()).mOrganizer.asBinder().equals(organizer.asBinder());
        if (matches) {
            ((com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState) entry.getValue()).destroy();
        }
        return matches;
    }

    public android.window.DisplayAreaAppearedInfo createTaskDisplayArea(android.window.IDisplayAreaOrganizer organizer, int displayId, final int parentFeatureId, java.lang.String name) {
        com.android.server.wm.TaskDisplayArea parentTda;
        com.android.server.wm.TaskDisplayArea tda;
        android.window.DisplayAreaAppearedInfo tdaInfo;
        enforceTaskPermission("createTaskDisplayArea()");
        long uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 5147103403966149923L, 1, null, java.lang.Long.valueOf(uid));
                    }
                    com.android.server.wm.DisplayContent display = this.mService.mRootWindowContainer.getDisplayContent(displayId);
                    if (display == null) {
                        throw new java.lang.IllegalArgumentException("createTaskDisplayArea unknown displayId=" + displayId);
                    }
                    if (!display.isTrusted()) {
                        throw new java.lang.IllegalArgumentException("createTaskDisplayArea untrusted displayId=" + displayId);
                    }
                    com.android.server.wm.RootDisplayArea parentRoot = (com.android.server.wm.RootDisplayArea) display.getItemFromDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayAreaOrganizerController$$ExternalSyntheticLambda3
                        @Override // java.util.function.Function
                        public final java.lang.Object apply(java.lang.Object obj) {
                            return com.android.server.wm.DisplayAreaOrganizerController.lambda$createTaskDisplayArea$3(parentFeatureId, (com.android.server.wm.DisplayArea) obj);
                        }
                    });
                    if (parentRoot == null) {
                        parentTda = (com.android.server.wm.TaskDisplayArea) display.getItemFromTaskDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayAreaOrganizerController$$ExternalSyntheticLambda4
                            @Override // java.util.function.Function
                            public final java.lang.Object apply(java.lang.Object obj) {
                                return com.android.server.wm.DisplayAreaOrganizerController.lambda$createTaskDisplayArea$4(parentFeatureId, (com.android.server.wm.TaskDisplayArea) obj);
                            }
                        });
                    } else {
                        parentTda = null;
                    }
                    if (parentRoot == null && parentTda == null) {
                        throw new java.lang.IllegalArgumentException("Can't find a parent DisplayArea with featureId=" + parentFeatureId);
                    }
                    int taskDisplayAreaFeatureId = this.mNextTaskDisplayAreaFeatureId;
                    this.mNextTaskDisplayAreaFeatureId = taskDisplayAreaFeatureId + 1;
                    com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState state = new com.android.server.wm.DisplayAreaOrganizerController.DisplayAreaOrganizerState(organizer, taskDisplayAreaFeatureId);
                    if (parentRoot != null) {
                        tda = createTaskDisplayArea(parentRoot, name, taskDisplayAreaFeatureId);
                    } else {
                        tda = createTaskDisplayArea(parentTda, name, taskDisplayAreaFeatureId);
                    }
                    tdaInfo = organizeDisplayArea(organizer, tda, "DisplayAreaOrganizerController.createTaskDisplayArea");
                    this.mOrganizersByFeatureIds.put(java.lang.Integer.valueOf(taskDisplayAreaFeatureId), state);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return tdaInfo;
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    static /* synthetic */ com.android.server.wm.RootDisplayArea lambda$createTaskDisplayArea$3(int parentFeatureId, com.android.server.wm.DisplayArea da) {
        if (da.asRootDisplayArea() != null && da.mFeatureId == parentFeatureId) {
            return da.asRootDisplayArea();
        }
        return null;
    }

    static /* synthetic */ com.android.server.wm.TaskDisplayArea lambda$createTaskDisplayArea$4(int parentFeatureId, com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        if (taskDisplayArea.mFeatureId == parentFeatureId) {
            return taskDisplayArea;
        }
        return null;
    }

    public void deleteTaskDisplayArea(android.window.WindowContainerToken token) {
        enforceTaskPermission("deleteTaskDisplayArea()");
        long uid = android.os.Binder.getCallingUid();
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
                        com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1659480097203667175L, 1, null, java.lang.Long.valueOf(uid));
                    }
                    com.android.server.wm.WindowContainer wc = com.android.server.wm.WindowContainer.fromBinder(token.asBinder());
                    if (wc == null || wc.asTaskDisplayArea() == null) {
                        throw new java.lang.IllegalArgumentException("Can't resolve TaskDisplayArea from token");
                    }
                    com.android.server.wm.TaskDisplayArea taskDisplayArea = wc.asTaskDisplayArea();
                    if (!taskDisplayArea.mCreatedByOrganizer) {
                        throw new java.lang.IllegalArgumentException("Attempt to delete TaskDisplayArea not created by organizer TaskDisplayArea=" + taskDisplayArea);
                    }
                    this.mOrganizersByFeatureIds.remove(java.lang.Integer.valueOf(taskDisplayArea.mFeatureId)).destroy();
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    void onDisplayAreaAppeared(android.window.IDisplayAreaOrganizer organizer, com.android.server.wm.DisplayArea da) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(da.getName());
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -4514772405648277945L, 0, null, protoLogParam0);
        }
        try {
            android.view.SurfaceControl outSurfaceControl = new android.view.SurfaceControl(da.getSurfaceControl(), "DisplayAreaOrganizerController.onDisplayAreaAppeared");
            organizer.onDisplayAreaAppeared(da.getDisplayAreaInfo(), outSurfaceControl);
        } catch (android.os.RemoteException e) {
        }
    }

    void onDisplayAreaVanished(android.window.IDisplayAreaOrganizer organizer, com.android.server.wm.DisplayArea da) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(da.getName());
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, 995846188225477231L, 0, null, protoLogParam0);
        }
        if (!organizer.asBinder().isBinderAlive()) {
            android.util.Slog.d(TAG, "Organizer died before sending onDisplayAreaVanished");
        } else {
            try {
                organizer.onDisplayAreaVanished(da.getDisplayAreaInfo());
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void onDisplayAreaInfoChanged(android.window.IDisplayAreaOrganizer organizer, com.android.server.wm.DisplayArea da) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_WINDOW_ORGANIZER_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(da.getName());
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_WINDOW_ORGANIZER, -1007032390526684388L, 0, null, protoLogParam0);
        }
        try {
            organizer.onDisplayAreaInfoChanged(da.getDisplayAreaInfo());
        } catch (android.os.RemoteException e) {
        }
    }

    private android.window.DisplayAreaAppearedInfo organizeDisplayArea(android.window.IDisplayAreaOrganizer organizer, com.android.server.wm.DisplayArea displayArea, java.lang.String callsite) {
        displayArea.setOrganizer(organizer, true);
        return new android.window.DisplayAreaAppearedInfo(displayArea.getDisplayAreaInfo(), new android.view.SurfaceControl(displayArea.getSurfaceControl(), callsite));
    }

    private com.android.server.wm.TaskDisplayArea createTaskDisplayArea(final com.android.server.wm.RootDisplayArea root, java.lang.String name, int taskDisplayAreaFeatureId) {
        com.android.server.wm.TaskDisplayArea taskDisplayArea = new com.android.server.wm.TaskDisplayArea(root.mDisplayContent, root.mWmService, name, taskDisplayAreaFeatureId, true);
        com.android.server.wm.DisplayArea topTaskContainer = (com.android.server.wm.DisplayArea) root.getItemFromDisplayAreas(new java.util.function.Function() { // from class: com.android.server.wm.DisplayAreaOrganizerController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.wm.DisplayAreaOrganizerController.lambda$createTaskDisplayArea$5(root, (com.android.server.wm.DisplayArea) obj);
            }
        });
        if (topTaskContainer == null) {
            throw new java.lang.IllegalStateException("Root must either contain TDA or DAG root=" + root);
        }
        com.android.server.wm.WindowContainer parent = topTaskContainer.getParent();
        int index = parent.mChildren.indexOf(topTaskContainer) + 1;
        parent.addChild(taskDisplayArea, index);
        return taskDisplayArea;
    }

    static /* synthetic */ com.android.server.wm.DisplayArea lambda$createTaskDisplayArea$5(com.android.server.wm.RootDisplayArea root, com.android.server.wm.DisplayArea da) {
        if (da.mType != com.android.server.wm.DisplayArea.Type.ANY) {
            return null;
        }
        com.android.server.wm.RootDisplayArea rootDA = da.getRootDisplayArea();
        if (rootDA == root || rootDA == da) {
            return da;
        }
        return null;
    }

    private com.android.server.wm.TaskDisplayArea createTaskDisplayArea(com.android.server.wm.TaskDisplayArea parentTda, java.lang.String name, int taskDisplayAreaFeatureId) {
        com.android.server.wm.TaskDisplayArea taskDisplayArea = new com.android.server.wm.TaskDisplayArea(parentTda.mDisplayContent, parentTda.mWmService, name, taskDisplayAreaFeatureId, true);
        parentTda.addChild(taskDisplayArea, Integer.MAX_VALUE);
        return taskDisplayArea;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deleteTaskDisplayArea(com.android.server.wm.TaskDisplayArea taskDisplayArea) {
        taskDisplayArea.setOrganizer(null);
        this.mService.mRootWindowContainer.mTaskSupervisor.beginDeferResume();
        try {
            com.android.server.wm.Task lastReparentedRootTask = taskDisplayArea.remove();
            this.mService.mRootWindowContainer.mTaskSupervisor.endDeferResume();
            taskDisplayArea.removeImmediately();
            if (lastReparentedRootTask != null) {
                lastReparentedRootTask.resumeNextFocusAfterReparent();
            }
        } catch (java.lang.Throwable th) {
            this.mService.mRootWindowContainer.mTaskSupervisor.endDeferResume();
            throw th;
        }
    }
}
