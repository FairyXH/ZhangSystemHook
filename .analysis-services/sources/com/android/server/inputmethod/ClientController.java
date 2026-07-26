package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class ClientController {
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.inputmethod.ClientState> mClients = new android.util.ArrayMap<>();
    private final java.util.List<com.android.server.inputmethod.ClientController.ClientControllerCallback> mCallbacks = new java.util.ArrayList();

    interface ClientControllerCallback {
        void onClientRemoved(com.android.server.inputmethod.ClientState clientState);
    }

    ClientController(android.content.pm.PackageManagerInternal packageManagerInternal) {
        this.mPackageManagerInternal = packageManagerInternal;
    }

    com.android.server.inputmethod.ClientState addClient(final com.android.server.inputmethod.IInputMethodClientInvoker clientInvoker, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, int selfReportedDisplayId, int callerUid, int callerPid) {
        android.os.IBinder.DeathRecipient deathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.inputmethod.ClientController$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$addClient$0(clientInvoker);
            }
        };
        int numClients = this.mClients.size();
        for (int i = 0; i < numClients; i++) {
            com.android.server.inputmethod.ClientState state = this.mClients.valueAt(i);
            if (state.mUid == callerUid && state.mPid == callerPid && state.mSelfReportedDisplayId == selfReportedDisplayId) {
                throw new java.lang.SecurityException("uid=" + callerUid + "/pid=" + callerPid + "/displayId=" + selfReportedDisplayId + " is already registered");
            }
        }
        try {
            clientInvoker.asBinder().linkToDeath(deathRecipient, 0);
            com.android.server.inputmethod.ClientState cs = new com.android.server.inputmethod.ClientState(clientInvoker, inputConnection, callerUid, callerPid, selfReportedDisplayId, deathRecipient);
            this.mClients.put(clientInvoker.asBinder(), cs);
            return cs;
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addClient$0(com.android.server.inputmethod.IInputMethodClientInvoker clientInvoker) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            removeClientAsBinder(clientInvoker.asBinder());
        }
    }

    boolean removeClient(com.android.internal.inputmethod.IInputMethodClient client) {
        return removeClientAsBinder(client.asBinder());
    }

    private boolean removeClientAsBinder(android.os.IBinder binder) {
        com.android.server.inputmethod.ClientState cs = this.mClients.remove(binder);
        if (cs == null) {
            return false;
        }
        binder.unlinkToDeath(cs.mClientDeathRecipient, 0);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onClientRemoved(cs);
        }
        return true;
    }

    void addClientControllerCallback(com.android.server.inputmethod.ClientController.ClientControllerCallback callback) {
        this.mCallbacks.add(callback);
    }

    com.android.server.inputmethod.ClientState getClient(android.os.IBinder binder) {
        return this.mClients.get(binder);
    }

    void forAllClients(java.util.function.Consumer<com.android.server.inputmethod.ClientState> consumer) {
        for (int i = 0; i < this.mClients.size(); i++) {
            consumer.accept(this.mClients.valueAt(i));
        }
    }

    boolean verifyClientAndPackageMatch(com.android.internal.inputmethod.IInputMethodClient client, java.lang.String packageName) {
        com.android.server.inputmethod.ClientState cs = this.mClients.get(client.asBinder());
        if (cs == null) {
            throw new java.lang.IllegalArgumentException("unknown client " + client.asBinder());
        }
        return com.android.server.inputmethod.InputMethodUtils.checkIfPackageBelongsToUid(this.mPackageManagerInternal, cs.mUid, packageName);
    }
}
