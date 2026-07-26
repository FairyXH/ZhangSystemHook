package com.android.server.location.listeners;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ListenerMultiplexer<TKey, TListener, TRegistration extends com.android.server.location.listeners.ListenerRegistration<TListener>, TMergedRegistration> {
    private TMergedRegistration mMerged;
    protected final java.lang.Object mMultiplexerLock = new java.lang.Object();
    private final android.util.ArrayMap<TKey, TRegistration> mRegistrations = new android.util.ArrayMap<>();
    private final com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer mUpdateServiceBuffer = new com.android.server.location.listeners.ListenerMultiplexer.UpdateServiceBuffer();
    private final com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard mReentrancyGuard = new com.android.server.location.listeners.ListenerMultiplexer.ReentrancyGuard();
    private int mActiveRegistrationsCount = 0;
    private boolean mServiceRegistered = false;

    protected abstract boolean isActive(TRegistration tregistration);

    protected abstract TMergedRegistration mergeRegistrations(java.util.Collection<TRegistration> collection);

    protected abstract boolean registerWithService(TMergedRegistration tmergedregistration, java.util.Collection<TRegistration> collection);

    protected abstract void unregisterWithService();

    protected boolean reregisterWithService(TMergedRegistration oldMerged, TMergedRegistration newMerged, java.util.Collection<TRegistration> registrations) {
        return registerWithService(newMerged, registrations);
    }

    protected void onRegister() {
    }

    protected void onUnregister() {
    }

    protected void onRegistrationAdded(TKey key, TRegistration registration) {
    }

    protected void onRegistrationReplaced(TKey oldKey, TRegistration oldRegistration, TKey newKey, TRegistration newRegistration) {
        onRegistrationRemoved(oldKey, oldRegistration);
        onRegistrationAdded(newKey, newRegistration);
    }

    protected void onRegistrationRemoved(TKey key, TRegistration registration) {
    }

    protected void onActive() {
    }

    protected void onInactive() {
    }

    protected final void putRegistration(TKey key, TRegistration registration) {
        replaceRegistration(key, key, registration);
    }

    protected final void replaceRegistration(TKey oldKey, TKey key, TRegistration registration) {
        java.util.Objects.requireNonNull(oldKey);
        java.util.Objects.requireNonNull(key);
        java.util.Objects.requireNonNull(registration);
        synchronized (this.mMultiplexerLock) {
            boolean z = true;
            com.android.internal.util.Preconditions.checkState(!this.mReentrancyGuard.isReentrant());
            if (oldKey != key && this.mRegistrations.containsKey(key)) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkArgument(z);
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer ignored1 = this.mUpdateServiceBuffer.acquire();
            try {
                com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard ignored2 = this.mReentrancyGuard.acquire();
                try {
                    boolean wasEmpty = this.mRegistrations.isEmpty();
                    TRegistration oldRegistration = null;
                    int oldIndex = this.mRegistrations.indexOfKey(oldKey);
                    if (oldIndex >= 0) {
                        oldRegistration = this.mRegistrations.valueAt(oldIndex);
                        unregister(oldRegistration);
                        oldRegistration.onUnregister();
                        if (oldKey != key) {
                            this.mRegistrations.removeAt(oldIndex);
                        }
                    }
                    if (oldKey == key && oldIndex >= 0) {
                        this.mRegistrations.setValueAt(oldIndex, registration);
                    } else {
                        this.mRegistrations.put(key, registration);
                    }
                    if (wasEmpty) {
                        onRegister();
                    }
                    registration.onRegister(key);
                    if (oldRegistration == null) {
                        onRegistrationAdded(key, registration);
                    } else {
                        onRegistrationReplaced(oldKey, oldRegistration, key, registration);
                    }
                    onRegistrationActiveChanged(registration);
                    if (ignored2 != null) {
                        ignored2.close();
                    }
                    if (ignored1 != null) {
                        ignored1.close();
                    }
                } finally {
                }
            } finally {
            }
        }
    }

    protected final void removeRegistrationIf(java.util.function.Predicate<TKey> predicate) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(!this.mReentrancyGuard.isReentrant());
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer ignored1 = this.mUpdateServiceBuffer.acquire();
            try {
                com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard ignored2 = this.mReentrancyGuard.acquire();
                try {
                    int size = this.mRegistrations.size();
                    for (int i = 0; i < size; i++) {
                        TKey key = this.mRegistrations.keyAt(i);
                        if (predicate.test(key)) {
                            removeRegistration(key, this.mRegistrations.valueAt(i));
                        }
                    }
                    if (ignored2 != null) {
                        ignored2.close();
                    }
                    if (ignored1 != null) {
                        ignored1.close();
                    }
                } finally {
                }
            } finally {
            }
        }
    }

    protected final void removeRegistration(TKey key) {
        synchronized (this.mMultiplexerLock) {
            com.android.internal.util.Preconditions.checkState(!this.mReentrancyGuard.isReentrant());
            int index = this.mRegistrations.indexOfKey(key);
            if (index < 0) {
                return;
            }
            removeRegistration(index);
        }
    }

    protected final void removeRegistration(TKey key, com.android.server.location.listeners.ListenerRegistration<?> registration) {
        synchronized (this.mMultiplexerLock) {
            int index = this.mRegistrations.indexOfKey(key);
            if (index < 0) {
                return;
            }
            TRegistration tregistrationValueAt = this.mRegistrations.valueAt(index);
            if (tregistrationValueAt != registration) {
                return;
            }
            if (this.mReentrancyGuard.isReentrant()) {
                unregister(tregistrationValueAt);
                this.mReentrancyGuard.markForRemoval(key, tregistrationValueAt);
            } else {
                removeRegistration(index);
            }
        }
    }

    private void removeRegistration(int index) {
        TKey key = this.mRegistrations.keyAt(index);
        TRegistration registration = this.mRegistrations.valueAt(index);
        com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer ignored1 = this.mUpdateServiceBuffer.acquire();
        try {
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard ignored2 = this.mReentrancyGuard.acquire();
            try {
                unregister(registration);
                onRegistrationRemoved(key, registration);
                registration.onUnregister();
                this.mRegistrations.removeAt(index);
                if (this.mRegistrations.isEmpty()) {
                    onUnregister();
                }
                if (ignored2 != null) {
                    ignored2.close();
                }
                if (ignored1 != null) {
                    ignored1.close();
                }
            } finally {
            }
        } catch (java.lang.Throwable th) {
            if (ignored1 != null) {
                try {
                    ignored1.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    protected final void updateService() {
        synchronized (this.mMultiplexerLock) {
            if (this.mUpdateServiceBuffer.isBuffered()) {
                this.mUpdateServiceBuffer.markUpdateServiceRequired();
                return;
            }
            int size = this.mRegistrations.size();
            java.util.ArrayList<TRegistration> actives = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TRegistration registration = this.mRegistrations.valueAt(i);
                if (registration.isActive()) {
                    actives.add(registration);
                }
            }
            if (actives.isEmpty()) {
                if (this.mServiceRegistered) {
                    this.mMerged = null;
                    this.mServiceRegistered = false;
                    unregisterWithService();
                }
            } else {
                TMergedRegistration merged = mergeRegistrations(actives);
                if (this.mServiceRegistered) {
                    if (!java.util.Objects.equals(merged, this.mMerged)) {
                        this.mServiceRegistered = reregisterWithService(this.mMerged, merged, actives);
                        this.mMerged = this.mServiceRegistered ? merged : null;
                    }
                } else {
                    this.mServiceRegistered = registerWithService(merged, actives);
                    this.mMerged = this.mServiceRegistered ? merged : null;
                }
            }
        }
    }

    protected final void resetService() {
        synchronized (this.mMultiplexerLock) {
            if (this.mServiceRegistered) {
                this.mMerged = null;
                this.mServiceRegistered = false;
                unregisterWithService();
                updateService();
            }
        }
    }

    public com.android.server.location.listeners.ListenerMultiplexer.UpdateServiceLock newUpdateServiceLock() {
        return new com.android.server.location.listeners.ListenerMultiplexer.UpdateServiceLock(this.mUpdateServiceBuffer);
    }

    protected final boolean findRegistration(java.util.function.Predicate<TRegistration> predicate) {
        synchronized (this.mMultiplexerLock) {
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard ignored = this.mReentrancyGuard.acquire();
            try {
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    TRegistration registration = this.mRegistrations.valueAt(i);
                    if (predicate.test(registration)) {
                        if (ignored != null) {
                            ignored.close();
                        }
                        return true;
                    }
                }
                if (ignored != null) {
                    ignored.close();
                }
                return false;
            } finally {
            }
        }
    }

    protected final void updateRegistrations(java.util.function.Predicate<TRegistration> predicate) {
        synchronized (this.mMultiplexerLock) {
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer ignored1 = this.mUpdateServiceBuffer.acquire();
            try {
                com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard ignored2 = this.mReentrancyGuard.acquire();
                try {
                    int size = this.mRegistrations.size();
                    for (int i = 0; i < size; i++) {
                        TRegistration registration = this.mRegistrations.valueAt(i);
                        if (predicate.test(registration)) {
                            onRegistrationActiveChanged(registration);
                        }
                    }
                    if (ignored2 != null) {
                        ignored2.close();
                    }
                    if (ignored1 != null) {
                        ignored1.close();
                    }
                } finally {
                }
            } finally {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final boolean updateRegistration(java.lang.Object r7, java.util.function.Predicate<TRegistration> r8) {
        /*
            r6 = this;
            java.lang.Object r0 = r6.mMultiplexerLock
            monitor-enter(r0)
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>$UpdateServiceBuffer r1 = r6.mUpdateServiceBuffer     // Catch: java.lang.Throwable -> L5c
            com.android.server.location.listeners.ListenerMultiplexer$UpdateServiceBuffer r1 = r1.acquire()     // Catch: java.lang.Throwable -> L5c
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>$ReentrancyGuard r2 = r6.mReentrancyGuard     // Catch: java.lang.Throwable -> L50
            com.android.server.location.listeners.ListenerMultiplexer$ReentrancyGuard r2 = r2.acquire()     // Catch: java.lang.Throwable -> L50
            android.util.ArrayMap<TKey, TRegistration extends com.android.server.location.listeners.ListenerRegistration<TListener>> r3 = r6.mRegistrations     // Catch: java.lang.Throwable -> L44
            int r3 = r3.indexOfKey(r7)     // Catch: java.lang.Throwable -> L44
            if (r3 >= 0) goto L25
        L18:
            if (r2 == 0) goto L1d
            r2.close()     // Catch: java.lang.Throwable -> L50
        L1d:
            if (r1 == 0) goto L22
            r1.close()     // Catch: java.lang.Throwable -> L5c
        L22:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5c
            r0 = 0
            return r0
        L25:
            android.util.ArrayMap<TKey, TRegistration extends com.android.server.location.listeners.ListenerRegistration<TListener>> r4 = r6.mRegistrations     // Catch: java.lang.Throwable -> L44
            java.lang.Object r4 = r4.valueAt(r3)     // Catch: java.lang.Throwable -> L44
            com.android.server.location.listeners.ListenerRegistration r4 = (com.android.server.location.listeners.ListenerRegistration) r4     // Catch: java.lang.Throwable -> L44
            boolean r5 = r8.test(r4)     // Catch: java.lang.Throwable -> L44
            if (r5 == 0) goto L36
            r6.onRegistrationActiveChanged(r4)     // Catch: java.lang.Throwable -> L44
        L36:
            if (r2 == 0) goto L3c
            r2.close()     // Catch: java.lang.Throwable -> L50
        L3c:
            if (r1 == 0) goto L41
            r1.close()     // Catch: java.lang.Throwable -> L5c
        L41:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5c
            r0 = 1
            return r0
        L44:
            r3 = move-exception
            if (r2 == 0) goto L4f
            r2.close()     // Catch: java.lang.Throwable -> L4b
            goto L4f
        L4b:
            r4 = move-exception
            r3.addSuppressed(r4)     // Catch: java.lang.Throwable -> L50
        L4f:
            throw r3     // Catch: java.lang.Throwable -> L50
        L50:
            r2 = move-exception
            if (r1 == 0) goto L5b
            r1.close()     // Catch: java.lang.Throwable -> L57
            goto L5b
        L57:
            r3 = move-exception
            r2.addSuppressed(r3)     // Catch: java.lang.Throwable -> L5c
        L5b:
            throw r2     // Catch: java.lang.Throwable -> L5c
        L5c:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L5c
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.location.listeners.ListenerMultiplexer.updateRegistration(java.lang.Object, java.util.function.Predicate):boolean");
    }

    private void onRegistrationActiveChanged(TRegistration registration) {
        boolean active = registration.isRegistered() && isActive(registration);
        boolean changed = registration.setActive(active);
        if (changed) {
            if (active) {
                int i = this.mActiveRegistrationsCount + 1;
                this.mActiveRegistrationsCount = i;
                if (i == 1) {
                    onActive();
                }
                registration.onActive();
            } else {
                registration.onInactive();
                int i2 = this.mActiveRegistrationsCount - 1;
                this.mActiveRegistrationsCount = i2;
                if (i2 == 0) {
                    onInactive();
                }
            }
            updateService();
        }
    }

    protected final void deliverToListeners(java.util.function.Function<TRegistration, com.android.internal.listeners.ListenerExecutor.ListenerOperation<TListener>> function) {
        com.android.internal.listeners.ListenerExecutor.ListenerOperation<TListener> operation;
        synchronized (this.mMultiplexerLock) {
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard ignored = this.mReentrancyGuard.acquire();
            try {
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    TRegistration registration = this.mRegistrations.valueAt(i);
                    if (registration.isActive() && (operation = function.apply(registration)) != null) {
                        registration.executeOperation(operation);
                    }
                }
                if (ignored != null) {
                    ignored.close();
                }
            } finally {
            }
        }
    }

    protected final void deliverToListeners(com.android.internal.listeners.ListenerExecutor.ListenerOperation<TListener> operation) {
        synchronized (this.mMultiplexerLock) {
            com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard ignored = this.mReentrancyGuard.acquire();
            try {
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    TRegistration registration = this.mRegistrations.valueAt(i);
                    if (registration.isActive()) {
                        registration.executeOperation(operation);
                    }
                }
                if (ignored != null) {
                    ignored.close();
                }
            } finally {
            }
        }
    }

    private void unregister(TRegistration registration) {
        registration.unregisterInternal();
        onRegistrationActiveChanged(registration);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        synchronized (this.mMultiplexerLock) {
            pw.print("service: ");
            pw.print(getServiceState());
            pw.println();
            if (!this.mRegistrations.isEmpty()) {
                pw.println("listeners:");
                int size = this.mRegistrations.size();
                for (int i = 0; i < size; i++) {
                    TRegistration registration = this.mRegistrations.valueAt(i);
                    pw.print("  ");
                    pw.print(registration);
                    if (!registration.isActive()) {
                        pw.println(" (inactive)");
                    } else {
                        pw.println();
                    }
                }
            }
        }
    }

    protected java.lang.String getServiceState() {
        if (this.mServiceRegistered) {
            if (this.mMerged != null) {
                return this.mMerged.toString();
            }
            return "registered";
        }
        return "unregistered";
    }

    private final class ReentrancyGuard implements java.lang.AutoCloseable {
        private int mGuardCount = 0;
        private android.util.ArraySet<java.util.Map.Entry<TKey, com.android.server.location.listeners.ListenerRegistration<?>>> mScheduledRemovals = null;

        ReentrancyGuard() {
        }

        boolean isReentrant() {
            boolean z;
            synchronized (com.android.server.location.listeners.ListenerMultiplexer.this.mMultiplexerLock) {
                z = this.mGuardCount != 0;
            }
            return z;
        }

        void markForRemoval(TKey key, com.android.server.location.listeners.ListenerRegistration<?> registration) {
            synchronized (com.android.server.location.listeners.ListenerMultiplexer.this.mMultiplexerLock) {
                com.android.internal.util.Preconditions.checkState(isReentrant());
                if (this.mScheduledRemovals == null) {
                    this.mScheduledRemovals = new android.util.ArraySet<>(com.android.server.location.listeners.ListenerMultiplexer.this.mRegistrations.size());
                }
                this.mScheduledRemovals.add(new java.util.AbstractMap.SimpleImmutableEntry(key, registration));
            }
        }

        com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.ReentrancyGuard acquire() {
            synchronized (com.android.server.location.listeners.ListenerMultiplexer.this.mMultiplexerLock) {
                this.mGuardCount++;
            }
            return this;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            synchronized (com.android.server.location.listeners.ListenerMultiplexer.this.mMultiplexerLock) {
                com.android.internal.util.Preconditions.checkState(this.mGuardCount > 0);
                android.util.ArraySet<java.util.Map.Entry<TKey, com.android.server.location.listeners.ListenerRegistration<?>>> scheduledRemovals = null;
                int i = this.mGuardCount - 1;
                this.mGuardCount = i;
                if (i == 0) {
                    scheduledRemovals = this.mScheduledRemovals;
                    this.mScheduledRemovals = null;
                }
                if (scheduledRemovals == null) {
                    return;
                }
                com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer ignored = com.android.server.location.listeners.ListenerMultiplexer.this.mUpdateServiceBuffer.acquire();
                try {
                    int size = scheduledRemovals.size();
                    for (int i2 = 0; i2 < size; i2++) {
                        java.util.Map.Entry<TKey, com.android.server.location.listeners.ListenerRegistration<?>> entry = scheduledRemovals.valueAt(i2);
                        com.android.server.location.listeners.ListenerMultiplexer.this.removeRegistration(entry.getKey(), entry.getValue());
                    }
                    if (ignored != null) {
                        ignored.close();
                    }
                } finally {
                }
            }
        }
    }

    private final class UpdateServiceBuffer implements java.lang.AutoCloseable {
        private int mBufferCount = 0;
        private boolean mUpdateServiceRequired = false;

        UpdateServiceBuffer() {
        }

        synchronized boolean isBuffered() {
            return this.mBufferCount != 0;
        }

        synchronized void markUpdateServiceRequired() {
            com.android.internal.util.Preconditions.checkState(isBuffered());
            this.mUpdateServiceRequired = true;
        }

        synchronized com.android.server.location.listeners.ListenerMultiplexer<TKey, TListener, TRegistration, TMergedRegistration>.UpdateServiceBuffer acquire() {
            this.mBufferCount++;
            return this;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            boolean updateServiceRequired = false;
            synchronized (this) {
                com.android.internal.util.Preconditions.checkState(this.mBufferCount > 0);
                int i = this.mBufferCount - 1;
                this.mBufferCount = i;
                if (i == 0) {
                    updateServiceRequired = this.mUpdateServiceRequired;
                    this.mUpdateServiceRequired = false;
                }
            }
            if (updateServiceRequired) {
                com.android.server.location.listeners.ListenerMultiplexer.this.updateService();
            }
        }
    }

    public static final class UpdateServiceLock implements java.lang.AutoCloseable {
        private com.android.server.location.listeners.ListenerMultiplexer<?, ?, ?, ?>.UpdateServiceBuffer mUpdateServiceBuffer;

        UpdateServiceLock(com.android.server.location.listeners.ListenerMultiplexer<?, ?, ?, ?>.UpdateServiceBuffer updateServiceBuffer) {
            this.mUpdateServiceBuffer = updateServiceBuffer.acquire();
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            if (this.mUpdateServiceBuffer != null) {
                com.android.server.location.listeners.ListenerMultiplexer<?, ?, ?, ?>.UpdateServiceBuffer buffer = this.mUpdateServiceBuffer;
                this.mUpdateServiceBuffer = null;
                buffer.close();
            }
        }
    }
}
