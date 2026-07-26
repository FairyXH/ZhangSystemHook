package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class FakeHalFactory implements com.android.server.soundtrigger_middleware.HalFactory {
    private static final java.lang.String TAG = "FakeHalFactory";
    private final android.media.soundtrigger_middleware.ISoundTriggerInjection mInjection;

    FakeHalFactory(android.media.soundtrigger_middleware.ISoundTriggerInjection injection) {
        this.mInjection = injection;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [android.os.IBinder, com.android.server.soundtrigger_middleware.FakeSoundTriggerHal] */
    @Override // com.android.server.soundtrigger_middleware.HalFactory
    public com.android.server.soundtrigger_middleware.ISoundTriggerHal create() {
        ?? fakeSoundTriggerHal = new com.android.server.soundtrigger_middleware.FakeSoundTriggerHal(this.mInjection);
        final android.media.soundtrigger_middleware.IInjectGlobalEvent session = fakeSoundTriggerHal.getGlobalEventInjection();
        com.android.server.soundtrigger_middleware.ISoundTriggerHal wrapper = new com.android.server.soundtrigger_middleware.FakeHalFactory.AnonymousClass1(fakeSoundTriggerHal, new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.soundtrigger_middleware.FakeHalFactory.lambda$create$0(session);
            }
        }, session);
        return wrapper;
    }

    static /* synthetic */ void lambda$create$0(android.media.soundtrigger_middleware.IInjectGlobalEvent session) {
        try {
            session.triggerRestart();
        } catch (android.os.RemoteException e) {
            android.util.Slog.wtf(TAG, "Unexpected RemoteException from same process");
        }
    }

    /* JADX INFO: renamed from: com.android.server.soundtrigger_middleware.FakeHalFactory$1, reason: invalid class name */
    class AnonymousClass1 extends com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat {
        final /* synthetic */ android.media.soundtrigger_middleware.IInjectGlobalEvent val$session;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(android.os.IBinder binder, java.lang.Runnable rebootRunnable, android.media.soundtrigger_middleware.IInjectGlobalEvent iInjectGlobalEvent) {
            super(binder, rebootRunnable);
            this.val$session = iInjectGlobalEvent;
        }

        @Override // com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat, com.android.server.soundtrigger_middleware.ISoundTriggerHal
        public void detach() {
            java.util.concurrent.Executor executor = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ExecutorHolder.INJECTION_EXECUTOR;
            final android.media.soundtrigger_middleware.IInjectGlobalEvent iInjectGlobalEvent = this.val$session;
            executor.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$detach$0(iInjectGlobalEvent);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$detach$0(android.media.soundtrigger_middleware.IInjectGlobalEvent session) {
            try {
                com.android.server.soundtrigger_middleware.FakeHalFactory.this.mInjection.onFrameworkDetached(session);
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(com.android.server.soundtrigger_middleware.FakeHalFactory.TAG, "Unexpected RemoteException from same process");
            }
        }

        @Override // com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat, com.android.server.soundtrigger_middleware.ISoundTriggerHal
        public void clientAttached(final android.os.IBinder token) {
            java.util.concurrent.Executor executor = com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ExecutorHolder.INJECTION_EXECUTOR;
            final android.media.soundtrigger_middleware.IInjectGlobalEvent iInjectGlobalEvent = this.val$session;
            executor.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$clientAttached$1(token, iInjectGlobalEvent);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$clientAttached$1(android.os.IBinder token, android.media.soundtrigger_middleware.IInjectGlobalEvent session) {
            try {
                com.android.server.soundtrigger_middleware.FakeHalFactory.this.mInjection.onClientAttached(token, session);
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(com.android.server.soundtrigger_middleware.FakeHalFactory.TAG, "Unexpected RemoteException from same process");
            }
        }

        @Override // com.android.server.soundtrigger_middleware.SoundTriggerHw3Compat, com.android.server.soundtrigger_middleware.ISoundTriggerHal
        public void clientDetached(final android.os.IBinder token) {
            com.android.server.soundtrigger_middleware.FakeSoundTriggerHal.ExecutorHolder.INJECTION_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger_middleware.FakeHalFactory$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$clientDetached$2(token);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$clientDetached$2(android.os.IBinder token) {
            try {
                com.android.server.soundtrigger_middleware.FakeHalFactory.this.mInjection.onClientDetached(token);
            } catch (android.os.RemoteException e) {
                android.util.Slog.wtf(com.android.server.soundtrigger_middleware.FakeHalFactory.TAG, "Unexpected RemoteException from same process");
            }
        }
    }
}
