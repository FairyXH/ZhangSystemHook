package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class ServiceHolder<I extends android.os.IInterface> implements android.os.IBinder.DeathRecipient {
    private final java.util.function.Function<? super android.os.IBinder, ? extends I> mCastFunction;
    private final java.util.concurrent.Executor mExecutor;
    private final java.util.Set<java.util.function.Consumer<I>> mOnDeathTasks;
    private final java.util.Set<java.util.function.Consumer<I>> mOnStartTasks;
    private final java.util.concurrent.atomic.AtomicReference<I> mService;
    private final android.os.IServiceCallback mServiceListener;
    private final java.lang.String mServiceName;
    private final com.android.server.audio.ServiceHolder.ServiceProviderFacade mServiceProvider;
    private final java.lang.String mTag;

    public interface ServiceProviderFacade {
        android.os.IBinder checkService(java.lang.String str);

        void registerForNotifications(java.lang.String str, android.os.IServiceCallback iServiceCallback);

        android.os.IBinder waitForService(java.lang.String str);
    }

    public ServiceHolder(java.lang.String serviceName, java.util.function.Function<? super android.os.IBinder, ? extends I> castFunction, java.util.concurrent.Executor executor) {
        this(serviceName, castFunction, executor, new com.android.server.audio.ServiceHolder.ServiceProviderFacade() { // from class: com.android.server.audio.ServiceHolder.2
            @Override // com.android.server.audio.ServiceHolder.ServiceProviderFacade
            public void registerForNotifications(java.lang.String name, android.os.IServiceCallback listener) {
                try {
                    android.os.ServiceManager.registerForNotifications(name, listener);
                } catch (android.os.RemoteException e) {
                    throw new java.lang.IllegalStateException("ServiceManager died!!", e);
                }
            }

            @Override // com.android.server.audio.ServiceHolder.ServiceProviderFacade
            public android.os.IBinder checkService(java.lang.String name) {
                return android.os.ServiceManager.checkService(name);
            }

            @Override // com.android.server.audio.ServiceHolder.ServiceProviderFacade
            public android.os.IBinder waitForService(java.lang.String name) {
                return android.os.ServiceManager.waitForService(name);
            }
        });
    }

    public ServiceHolder(java.lang.String serviceName, java.util.function.Function<? super android.os.IBinder, ? extends I> castFunction, java.util.concurrent.Executor executor, com.android.server.audio.ServiceHolder.ServiceProviderFacade provider) {
        this.mService = new java.util.concurrent.atomic.AtomicReference<>();
        this.mOnStartTasks = java.util.concurrent.ConcurrentHashMap.newKeySet();
        this.mOnDeathTasks = java.util.concurrent.ConcurrentHashMap.newKeySet();
        this.mServiceListener = new android.os.IServiceCallback.Stub() { // from class: com.android.server.audio.ServiceHolder.1
            public void onRegistration(java.lang.String name, android.os.IBinder binder) {
                com.android.server.audio.ServiceHolder.this.onServiceInited(binder);
            }
        };
        this.mServiceName = (java.lang.String) java.util.Objects.requireNonNull(serviceName);
        this.mCastFunction = (java.util.function.Function) java.util.Objects.requireNonNull(castFunction);
        this.mExecutor = (java.util.concurrent.Executor) java.util.Objects.requireNonNull(executor);
        this.mServiceProvider = (com.android.server.audio.ServiceHolder.ServiceProviderFacade) java.util.Objects.requireNonNull(provider);
        this.mTag = "ServiceHolder: " + serviceName;
        this.mServiceProvider.registerForNotifications(this.mServiceName, this.mServiceListener);
    }

    public void registerOnStartTask(final java.util.function.Consumer<I> task) {
        this.mOnStartTasks.add(task);
        final I i = this.mService.get();
        if (i != null) {
            this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.audio.ServiceHolder$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    task.accept(i);
                }
            });
        }
    }

    public void unregisterOnStartTask(java.util.function.Consumer<I> task) {
        this.mOnStartTasks.remove(task);
    }

    public void registerOnDeathTask(java.util.function.Consumer<I> task) {
        this.mOnDeathTasks.add(task);
    }

    public void unregisterOnDeathTask(java.util.function.Consumer<I> task) {
        this.mOnDeathTasks.remove(task);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(android.os.IBinder who) {
        attemptClear(who);
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        throw new java.lang.AssertionError("Wrong binderDied called, this should never happen");
    }

    public void attemptClear(android.os.IBinder who) {
        final I current = this.mService.get();
        if (current != null && java.util.Objects.equals(current.asBinder(), who) && this.mService.compareAndSet(current, null)) {
            who.unlinkToDeath(this, 0);
            for (final java.util.function.Consumer<I> r : this.mOnDeathTasks) {
                this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.audio.ServiceHolder$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        r.accept(current);
                    }
                });
            }
        }
    }

    public I checkService() {
        I i = this.mService.get();
        if (i != null) {
            return i;
        }
        android.os.IBinder iBinderCheckService = this.mServiceProvider.checkService(this.mServiceName);
        if (iBinderCheckService == null) {
            return null;
        }
        return (I) onServiceInited(iBinderCheckService);
    }

    public I waitForService() {
        I i = this.mService.get();
        return i != null ? i : (I) onServiceInited(this.mServiceProvider.waitForService(this.mServiceName));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public I onServiceInited(android.os.IBinder who) {
        final I service = this.mCastFunction.apply(who);
        java.util.Objects.requireNonNull(service);
        if (!this.mService.compareAndSet(null, service)) {
            return service;
        }
        for (final java.util.function.Consumer<I> r : this.mOnStartTasks) {
            this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.audio.ServiceHolder$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    r.accept(service);
                }
            });
        }
        try {
            who.linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(this.mTag, "Immediate service death. Service crash-looping");
            attemptClear(who);
        }
        return service;
    }
}
