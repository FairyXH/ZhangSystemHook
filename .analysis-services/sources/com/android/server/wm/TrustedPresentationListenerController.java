package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class TrustedPresentationListenerController {
    private android.os.Handler mHandler;
    private android.os.HandlerThread mHandlerThread;
    private android.view.InputWindowHandle[] mLastWindowHandles;
    private android.window.WindowInfosListener mWindowInfosListener;
    private final java.lang.Object mHandlerThreadLock = new java.lang.Object();
    com.android.server.wm.TrustedPresentationListenerController.Listeners mRegisteredListeners = new com.android.server.wm.TrustedPresentationListenerController.Listeners();

    /* JADX INFO: Access modifiers changed from: private */
    class Listeners {
        android.util.ArrayMap<android.os.IBinder, com.android.server.wm.TrustedPresentationListenerController.Listeners.ListenerDeathRecipient> mUniqueListeners;
        android.util.ArrayMap<android.os.IBinder, java.util.ArrayList<com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo>> mWindowToListeners;

        private Listeners() {
            this.mUniqueListeners = new android.util.ArrayMap<>();
            this.mWindowToListeners = new android.util.ArrayMap<>();
        }

        /* JADX INFO: Access modifiers changed from: private */
        final class ListenerDeathRecipient implements android.os.IBinder.DeathRecipient {
            int mInstances = 0;
            android.os.IBinder mListenerBinder;

            ListenerDeathRecipient(android.os.IBinder listenerBinder) {
                this.mListenerBinder = listenerBinder;
                try {
                    this.mListenerBinder.linkToDeath(this, 0);
                } catch (android.os.RemoteException e) {
                }
            }

            void addInstance() {
                this.mInstances++;
            }

            boolean removeInstance() {
                this.mInstances--;
                if (this.mInstances > 0) {
                    return false;
                }
                this.mListenerBinder.unlinkToDeath(this, 0);
                return true;
            }

            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                com.android.server.wm.TrustedPresentationListenerController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TrustedPresentationListenerController$Listeners$ListenerDeathRecipient$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$binderDied$0();
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$binderDied$0() {
                com.android.server.wm.TrustedPresentationListenerController.Listeners.this.mUniqueListeners.remove(this.mListenerBinder);
                com.android.server.wm.TrustedPresentationListenerController.Listeners.this.removeListeners(this.mListenerBinder, java.util.Optional.empty());
            }
        }

        void register(android.os.IBinder window, android.window.ITrustedPresentationListener listener, android.window.TrustedPresentationThresholds thresholds, int id) {
            java.util.ArrayList<com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo> listenersForWindow = this.mWindowToListeners.computeIfAbsent(window, new java.util.function.Function() { // from class: com.android.server.wm.TrustedPresentationListenerController$Listeners$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.wm.TrustedPresentationListenerController.Listeners.lambda$register$0((android.os.IBinder) obj);
                }
            });
            listenersForWindow.add(new com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo(thresholds, id, listener));
            android.os.IBinder listenerBinder = listener.asBinder();
            com.android.server.wm.TrustedPresentationListenerController.Listeners.ListenerDeathRecipient deathRecipient = this.mUniqueListeners.computeIfAbsent(listenerBinder, new java.util.function.Function() { // from class: com.android.server.wm.TrustedPresentationListenerController$Listeners$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$register$1((android.os.IBinder) obj);
                }
            });
            deathRecipient.addInstance();
        }

        static /* synthetic */ java.util.ArrayList lambda$register$0(android.os.IBinder iBinder) {
            return new java.util.ArrayList();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ com.android.server.wm.TrustedPresentationListenerController.Listeners.ListenerDeathRecipient lambda$register$1(android.os.IBinder x$0) {
            return new com.android.server.wm.TrustedPresentationListenerController.Listeners.ListenerDeathRecipient(x$0);
        }

        void unregister(android.window.ITrustedPresentationListener trustedPresentationListener, int id) {
            android.os.IBinder listenerBinder = trustedPresentationListener.asBinder();
            com.android.server.wm.TrustedPresentationListenerController.Listeners.ListenerDeathRecipient deathRecipient = this.mUniqueListeners.get(listenerBinder);
            if (deathRecipient == null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[4]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(trustedPresentationListener);
                    long protoLogParam1 = id;
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 3445530300764535903L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
                    return;
                }
                return;
            }
            if (deathRecipient.removeInstance()) {
                this.mUniqueListeners.remove(listenerBinder);
            }
            removeListeners(listenerBinder, java.util.Optional.of(java.lang.Integer.valueOf(id)));
        }

        boolean isEmpty() {
            return this.mWindowToListeners.isEmpty();
        }

        java.util.ArrayList<com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo> get(android.os.IBinder windowToken) {
            return this.mWindowToListeners.get(windowToken);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void removeListeners(android.os.IBinder listenerBinder, java.util.Optional<java.lang.Integer> id) {
            for (int i = this.mWindowToListeners.size() - 1; i >= 0; i--) {
                java.util.ArrayList<com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo> listeners = this.mWindowToListeners.valueAt(i);
                for (int j = listeners.size() - 1; j >= 0; j--) {
                    com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo listener = listeners.get(j);
                    if (listener.mListener.asBinder() == listenerBinder && (id.isEmpty() || listener.mId == id.get().intValue())) {
                        listeners.remove(j);
                    }
                }
                if (listeners.isEmpty()) {
                    this.mWindowToListeners.removeAt(i);
                }
            }
        }
    }

    private void startHandlerThreadIfNeeded() {
        synchronized (this.mHandlerThreadLock) {
            if (this.mHandler == null) {
                this.mHandlerThread = new android.os.HandlerThread("WindowInfosListenerForTpl");
                this.mHandlerThread.start();
                this.mHandler = new android.os.Handler(this.mHandlerThread.getLooper());
            }
        }
    }

    void registerListener(final android.os.IBinder window, final android.window.ITrustedPresentationListener listener, final android.window.TrustedPresentationThresholds thresholds, final int id) {
        startHandlerThreadIfNeeded();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TrustedPresentationListenerController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$registerListener$0(listener, id, window, thresholds);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$registerListener$0(android.window.ITrustedPresentationListener listener, int id, android.os.IBinder window, android.window.TrustedPresentationThresholds thresholds) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(listener);
            long protoLogParam1 = id;
            java.lang.String protoLogParam2 = java.lang.String.valueOf(window);
            java.lang.String protoLogParam3 = java.lang.String.valueOf(thresholds);
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, -6140852484700685564L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1), protoLogParam2, protoLogParam3);
        }
        this.mRegisteredListeners.register(window, listener, thresholds, id);
        registerWindowInfosListener();
        computeTpl(this.mLastWindowHandles);
    }

    void unregisterListener(final android.window.ITrustedPresentationListener listener, final int id) {
        startHandlerThreadIfNeeded();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TrustedPresentationListenerController$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$unregisterListener$1(listener, id);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterListener$1(android.window.ITrustedPresentationListener listener, int id) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[0]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(listener);
            long protoLogParam1 = id;
            com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 3691097873058247482L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
        }
        this.mRegisteredListeners.unregister(listener, id);
        if (this.mRegisteredListeners.isEmpty()) {
            unregisterWindowInfosListener();
        }
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("TrustedPresentationListenerController:");
        pw.println("  Active unique listeners (" + this.mRegisteredListeners.mUniqueListeners.size() + "):");
        for (int i = 0; i < this.mRegisteredListeners.mWindowToListeners.size(); i++) {
            pw.println("    window=" + this.mRegisteredListeners.mWindowToListeners.keyAt(i));
            java.util.ArrayList<com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo> listeners = this.mRegisteredListeners.mWindowToListeners.valueAt(i);
            for (int j = 0; j < listeners.size(); j++) {
                com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo listener = listeners.get(j);
                pw.println("      listener=" + listener.mListener.asBinder() + " id=" + listener.mId + " thresholds=" + listener.mThresholds);
            }
        }
    }

    private void registerWindowInfosListener() {
        if (this.mWindowInfosListener != null) {
            return;
        }
        this.mWindowInfosListener = new com.android.server.wm.TrustedPresentationListenerController.AnonymousClass1();
        this.mLastWindowHandles = (android.view.InputWindowHandle[]) this.mWindowInfosListener.register().first;
    }

    /* JADX INFO: renamed from: com.android.server.wm.TrustedPresentationListenerController$1, reason: invalid class name */
    class AnonymousClass1 extends android.window.WindowInfosListener {
        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onWindowInfosChanged$0(android.view.InputWindowHandle[] windowHandles) {
            com.android.server.wm.TrustedPresentationListenerController.this.computeTpl(windowHandles);
        }

        public void onWindowInfosChanged(final android.view.InputWindowHandle[] windowHandles, android.window.WindowInfosListener.DisplayInfo[] displayInfos) {
            com.android.server.wm.TrustedPresentationListenerController.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.wm.TrustedPresentationListenerController$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onWindowInfosChanged$0(windowHandles);
                }
            });
        }
    }

    private void unregisterWindowInfosListener() {
        if (this.mWindowInfosListener == null) {
            return;
        }
        this.mWindowInfosListener.unregister();
        this.mWindowInfosListener = null;
        this.mLastWindowHandles = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void computeTpl(android.view.InputWindowHandle[] windowHandles) {
        android.view.InputWindowHandle[] inputWindowHandleArr;
        android.view.InputWindowHandle windowHandle;
        int i;
        int i2;
        android.util.ArrayMap<android.window.ITrustedPresentationListener, android.util.Pair<android.util.IntArray, android.util.IntArray>> listenerUpdates;
        com.android.server.wm.TrustedPresentationListenerController trustedPresentationListenerController = this;
        trustedPresentationListenerController.mLastWindowHandles = windowHandles;
        if (trustedPresentationListenerController.mLastWindowHandles == null || trustedPresentationListenerController.mLastWindowHandles.length == 0 || trustedPresentationListenerController.mRegisteredListeners.isEmpty()) {
            return;
        }
        android.graphics.Rect tmpRect = new android.graphics.Rect();
        android.graphics.Matrix tmpInverseMatrix = new android.graphics.Matrix();
        float[] tmpMatrix = new float[9];
        android.graphics.Region coveredRegionsAbove = new android.graphics.Region();
        long currTimeMs = java.lang.System.currentTimeMillis();
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
            long protoLogParam0 = trustedPresentationListenerController.mLastWindowHandles.length;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 6408851516381868623L, 1, null, java.lang.Long.valueOf(protoLogParam0));
        }
        android.util.ArrayMap<android.window.ITrustedPresentationListener, android.util.Pair<android.util.IntArray, android.util.IntArray>> listenerUpdates2 = new android.util.ArrayMap<>();
        android.view.InputWindowHandle[] inputWindowHandleArr2 = trustedPresentationListenerController.mLastWindowHandles;
        int length = inputWindowHandleArr2.length;
        int i3 = 0;
        while (i3 < length) {
            android.view.InputWindowHandle windowHandle2 = inputWindowHandleArr2[i3];
            if (!windowHandle2.canOccludePresentation) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
                    java.lang.String protoLogParam02 = java.lang.String.valueOf(windowHandle2.name);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 7718187745767272532L, 0, null, protoLogParam02);
                    inputWindowHandleArr = inputWindowHandleArr2;
                    i = i3;
                    i2 = length;
                    listenerUpdates = listenerUpdates2;
                } else {
                    inputWindowHandleArr = inputWindowHandleArr2;
                    i = i3;
                    i2 = length;
                    listenerUpdates = listenerUpdates2;
                }
            } else {
                tmpRect.set(windowHandle2.frame);
                java.util.ArrayList<com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo> listeners = trustedPresentationListenerController.mRegisteredListeners.get(windowHandle2.getWindowToken());
                if (listeners == null) {
                    inputWindowHandleArr = inputWindowHandleArr2;
                    windowHandle = windowHandle2;
                    i = i3;
                    i2 = length;
                    listenerUpdates = listenerUpdates2;
                } else {
                    android.graphics.Region region = new android.graphics.Region();
                    region.op(tmpRect, coveredRegionsAbove, android.graphics.Region.Op.DIFFERENCE);
                    windowHandle2.transform.invert(tmpInverseMatrix);
                    tmpInverseMatrix.getValues(tmpMatrix);
                    float scaleX = (float) java.lang.Math.sqrt((tmpMatrix[0] * tmpMatrix[0]) + (tmpMatrix[1] * tmpMatrix[1]));
                    inputWindowHandleArr = inputWindowHandleArr2;
                    float scaleY = (float) java.lang.Math.sqrt((tmpMatrix[4] * tmpMatrix[4]) + (tmpMatrix[3] * tmpMatrix[3]));
                    i = i3;
                    windowHandle = windowHandle2;
                    i2 = length;
                    float fractionRendered = computeFractionRendered(region, new android.graphics.RectF(tmpRect), windowHandle2.contentSize, scaleX, scaleY);
                    listenerUpdates = listenerUpdates2;
                    checkIfInThreshold(listeners, listenerUpdates2, fractionRendered, windowHandle.alpha, currTimeMs);
                }
                coveredRegionsAbove.op(tmpRect, android.graphics.Region.Op.UNION);
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
                    java.lang.String protoLogParam03 = java.lang.String.valueOf(windowHandle.name);
                    java.lang.String protoLogParam1 = java.lang.String.valueOf(tmpRect.toShortString());
                    java.lang.String protoLogParam2 = java.lang.String.valueOf(coveredRegionsAbove);
                    com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, -1135667737459933313L, 0, null, protoLogParam03, protoLogParam1, protoLogParam2);
                }
            }
            i3 = i + 1;
            trustedPresentationListenerController = this;
            inputWindowHandleArr2 = inputWindowHandleArr;
            length = i2;
            listenerUpdates2 = listenerUpdates;
        }
        android.util.ArrayMap<android.window.ITrustedPresentationListener, android.util.Pair<android.util.IntArray, android.util.IntArray>> listenerUpdates3 = listenerUpdates2;
        int i4 = 0;
        while (i4 < listenerUpdates3.size()) {
            android.util.ArrayMap<android.window.ITrustedPresentationListener, android.util.Pair<android.util.IntArray, android.util.IntArray>> listenerUpdates4 = listenerUpdates3;
            android.util.Pair<android.util.IntArray, android.util.IntArray> updates = listenerUpdates4.valueAt(i4);
            android.window.ITrustedPresentationListener listener = listenerUpdates4.keyAt(i4);
            try {
                listener.onTrustedPresentationChanged(((android.util.IntArray) updates.first).toArray(), ((android.util.IntArray) updates.second).toArray());
            } catch (android.os.RemoteException e) {
            }
            i4++;
            listenerUpdates3 = listenerUpdates4;
        }
    }

    private void addListenerUpdate(android.util.ArrayMap<android.window.ITrustedPresentationListener, android.util.Pair<android.util.IntArray, android.util.IntArray>> listenerUpdates, android.window.ITrustedPresentationListener listener, int id, boolean presentationState) {
        android.util.Pair<android.util.IntArray, android.util.IntArray> updates = listenerUpdates.get(listener);
        if (updates == null) {
            updates = new android.util.Pair<>(new android.util.IntArray(), new android.util.IntArray());
            listenerUpdates.put(listener, updates);
        }
        if (presentationState) {
            ((android.util.IntArray) updates.first).add(id);
        } else {
            ((android.util.IntArray) updates.second).add(id);
        }
    }

    private void checkIfInThreshold(java.util.ArrayList<com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo> listeners, android.util.ArrayMap<android.window.ITrustedPresentationListener, android.util.Pair<android.util.IntArray, android.util.IntArray>> listenerUpdates, float fractionRendered, float alpha, long currTimeMs) {
        int i;
        final com.android.server.wm.TrustedPresentationListenerController trustedPresentationListenerController;
        android.util.ArrayMap<android.window.ITrustedPresentationListener, android.util.Pair<android.util.IntArray, android.util.IntArray>> arrayMap;
        long j;
        float f = alpha;
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
            double protoLogParam0 = fractionRendered;
            double protoLogParam1 = f;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 854487339271667012L, 26, null, java.lang.Double.valueOf(protoLogParam0), java.lang.Double.valueOf(protoLogParam1), java.lang.Long.valueOf(currTimeMs));
        }
        int i2 = 0;
        while (i2 < listeners.size()) {
            com.android.server.wm.TrustedPresentationListenerController.TrustedPresentationInfo trustedPresentationInfo = listeners.get(i2);
            android.window.ITrustedPresentationListener listener = trustedPresentationInfo.mListener;
            boolean lastState = trustedPresentationInfo.mLastComputedTrustedPresentationState;
            boolean newState = f >= trustedPresentationInfo.mThresholds.getMinAlpha() && fractionRendered >= trustedPresentationInfo.mThresholds.getMinFractionRendered();
            trustedPresentationInfo.mLastComputedTrustedPresentationState = newState;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
                java.lang.String protoLogParam02 = java.lang.String.valueOf(lastState);
                java.lang.String protoLogParam12 = java.lang.String.valueOf(newState);
                double protoLogParam2 = f;
                i = i2;
                double protoLogParam3 = trustedPresentationInfo.mThresholds.getMinAlpha();
                double protoLogParam4 = fractionRendered;
                double protoLogParam5 = trustedPresentationInfo.mThresholds.getMinFractionRendered();
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, -2248576188205088843L, 2720, null, protoLogParam02, protoLogParam12, java.lang.Double.valueOf(protoLogParam2), java.lang.Double.valueOf(protoLogParam3), java.lang.Double.valueOf(protoLogParam4), java.lang.Double.valueOf(protoLogParam5));
            } else {
                i = i2;
            }
            if (!lastState || newState) {
                trustedPresentationListenerController = this;
                arrayMap = listenerUpdates;
                if (lastState || !newState) {
                    j = currTimeMs;
                } else {
                    j = currTimeMs;
                    trustedPresentationInfo.mEnteredTrustedPresentationStateTime = j;
                    trustedPresentationListenerController.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.wm.TrustedPresentationListenerController$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$checkIfInThreshold$2();
                        }
                    }, (long) (((double) trustedPresentationInfo.mThresholds.getStabilityRequirementMillis()) * 1.5d));
                }
            } else {
                if (!trustedPresentationInfo.mLastReportedTrustedPresentationState) {
                    trustedPresentationListenerController = this;
                    arrayMap = listenerUpdates;
                } else {
                    trustedPresentationInfo.mLastReportedTrustedPresentationState = false;
                    trustedPresentationListenerController = this;
                    arrayMap = listenerUpdates;
                    trustedPresentationListenerController.addListenerUpdate(arrayMap, listener, trustedPresentationInfo.mId, false);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[0]) {
                        java.lang.String protoLogParam03 = java.lang.String.valueOf(listener);
                        long protoLogParam13 = trustedPresentationInfo.mId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 6236170793308011579L, 4, null, protoLogParam03, java.lang.Long.valueOf(protoLogParam13));
                    }
                }
                trustedPresentationInfo.mEnteredTrustedPresentationStateTime = -1L;
                j = currTimeMs;
            }
            if (!trustedPresentationInfo.mLastReportedTrustedPresentationState && newState) {
                if (j - trustedPresentationInfo.mEnteredTrustedPresentationStateTime > trustedPresentationInfo.mThresholds.getStabilityRequirementMillis()) {
                    trustedPresentationInfo.mLastReportedTrustedPresentationState = true;
                    trustedPresentationListenerController.addListenerUpdate(arrayMap, listener, trustedPresentationInfo.mId, true);
                    if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[0]) {
                        java.lang.String protoLogParam04 = java.lang.String.valueOf(listener);
                        long protoLogParam14 = trustedPresentationInfo.mId;
                        com.android.internal.protolog.ProtoLogImpl_209941506.d(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 5405816744363636527L, 4, null, protoLogParam04, java.lang.Long.valueOf(protoLogParam14));
                    }
                }
            }
            i2 = i + 1;
            f = alpha;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkIfInThreshold$2() {
        computeTpl(this.mLastWindowHandles);
    }

    private float computeFractionRendered(android.graphics.Region visibleRegion, android.graphics.RectF screenBounds, android.util.Size contentSize, float sx, float sy) {
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(visibleRegion);
            java.lang.String protoLogParam1 = java.lang.String.valueOf(screenBounds);
            java.lang.String protoLogParam2 = java.lang.String.valueOf(contentSize);
            double protoLogParam3 = sx;
            double protoLogParam4 = sy;
            com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, -5162728346383863020L, 640, null, protoLogParam0, protoLogParam1, protoLogParam2, java.lang.Double.valueOf(protoLogParam3), java.lang.Double.valueOf(protoLogParam4));
        }
        if (contentSize.getWidth() != 0 && contentSize.getHeight() != 0 && screenBounds.width() != 0.0f && screenBounds.height() != 0.0f) {
            float fractionRendered = java.lang.Math.min(sx * sy, 1.0f);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
                double protoLogParam02 = fractionRendered;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, 898769258643799441L, 2, null, java.lang.Double.valueOf(protoLogParam02));
            }
            float boundsOverSourceW = screenBounds.width() / contentSize.getWidth();
            float boundsOverSourceH = screenBounds.height() / contentSize.getHeight();
            float fractionRendered2 = fractionRendered * boundsOverSourceW * boundsOverSourceH;
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_DEBUG_TPL_enabled[1]) {
                double protoLogParam03 = fractionRendered2;
                com.android.internal.protolog.ProtoLogImpl_209941506.v(com.android.internal.protolog.ProtoLogGroup.WM_DEBUG_TPL, -455501334697331596L, 2, null, java.lang.Double.valueOf(protoLogParam03));
            }
            final float[] visibleSize = new float[1];
            com.android.server.wm.utils.RegionUtils.forEachRect(visibleRegion, new java.util.function.Consumer() { // from class: com.android.server.wm.TrustedPresentationListenerController$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wm.TrustedPresentationListenerController.lambda$computeFractionRendered$3(visibleSize, (android.graphics.Rect) obj);
                }
            });
            return fractionRendered2 * (visibleSize[0] / (screenBounds.width() * screenBounds.height()));
        }
        return -1.0f;
    }

    static /* synthetic */ void lambda$computeFractionRendered$3(float[] visibleSize, android.graphics.Rect rect) {
        float size = rect.width() * rect.height();
        visibleSize[0] = visibleSize[0] + size;
    }

    private static class TrustedPresentationInfo {
        long mEnteredTrustedPresentationStateTime;
        final int mId;
        boolean mLastComputedTrustedPresentationState;
        boolean mLastReportedTrustedPresentationState;
        final android.window.ITrustedPresentationListener mListener;
        final android.window.TrustedPresentationThresholds mThresholds;

        private TrustedPresentationInfo(android.window.TrustedPresentationThresholds thresholds, int id, android.window.ITrustedPresentationListener listener) {
            this.mLastComputedTrustedPresentationState = false;
            this.mLastReportedTrustedPresentationState = false;
            this.mEnteredTrustedPresentationStateTime = -1L;
            this.mThresholds = thresholds;
            this.mId = id;
            this.mListener = listener;
        }
    }
}
