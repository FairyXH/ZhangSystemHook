package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class CountryDetectorService extends android.location.ICountryDetector.Stub {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "CountryDetector";
    private final android.content.Context mContext;
    private com.android.server.location.countrydetector.CountryDetectorBase mCountryDetector;
    private android.os.Handler mHandler;
    private android.location.CountryListener mLocationBasedDetectorListener;
    private final java.util.HashMap<android.os.IBinder, com.android.server.CountryDetectorService.Receiver> mReceivers;
    private boolean mSystemReady;

    private final class Receiver implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder mKey;
        private final android.location.ICountryListener mListener;

        public Receiver(android.location.ICountryListener listener) {
            this.mListener = listener;
            this.mKey = listener.asBinder();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.CountryDetectorService.this.removeListener(this.mKey);
        }

        public boolean equals(java.lang.Object otherObj) {
            if (otherObj instanceof com.android.server.CountryDetectorService.Receiver) {
                return this.mKey.equals(((com.android.server.CountryDetectorService.Receiver) otherObj).mKey);
            }
            return false;
        }

        public int hashCode() {
            return this.mKey.hashCode();
        }

        public android.location.ICountryListener getListener() {
            return this.mListener;
        }
    }

    public CountryDetectorService(android.content.Context context) {
        this(context, com.android.internal.os.BackgroundThread.getHandler());
    }

    CountryDetectorService(android.content.Context context, android.os.Handler handler) {
        this.mReceivers = new java.util.HashMap<>();
        this.mContext = context;
        this.mHandler = handler;
    }

    public android.location.Country detectCountry() {
        if (!this.mSystemReady) {
            return null;
        }
        return this.mCountryDetector.detectCountry();
    }

    public void addCountryListener(android.location.ICountryListener listener) throws android.os.RemoteException {
        if (!this.mSystemReady) {
            throw new android.os.RemoteException();
        }
        addListener(listener);
    }

    public void removeCountryListener(android.location.ICountryListener listener) throws android.os.RemoteException {
        if (!this.mSystemReady) {
            throw new android.os.RemoteException();
        }
        removeListener(listener.asBinder());
    }

    private void addListener(android.location.ICountryListener listener) {
        synchronized (this.mReceivers) {
            com.android.server.CountryDetectorService.Receiver r = new com.android.server.CountryDetectorService.Receiver(listener);
            try {
                listener.asBinder().linkToDeath(r, 0);
                android.location.Country country = detectCountry();
                if (country != null) {
                    listener.onCountryDetected(country);
                }
                this.mReceivers.put(listener.asBinder(), r);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "linkToDeath failed:", e);
            }
            if (this.mReceivers.size() == 1) {
                android.util.Slog.d(TAG, "The first listener is added");
                setCountryListener(this.mLocationBasedDetectorListener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeListener(android.os.IBinder key) {
        synchronized (this.mReceivers) {
            this.mReceivers.remove(key);
            if (this.mReceivers.isEmpty()) {
                setCountryListener(null);
                android.util.Slog.d(TAG, "No listener is left");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: notifyReceivers, reason: merged with bridge method [inline-methods] */
    public void lambda$initialize$1(android.location.Country country) {
        synchronized (this.mReceivers) {
            for (com.android.server.CountryDetectorService.Receiver receiver : this.mReceivers.values()) {
                try {
                    receiver.getListener().onCountryDetected(country);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "notifyReceivers failed:", e);
                }
            }
        }
    }

    void systemRunning() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.CountryDetectorService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$systemRunning$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$systemRunning$0() {
        initialize();
        this.mSystemReady = true;
    }

    void initialize() {
        java.lang.String customCountryClass = this.mContext.getString(android.R.string.config_defaultAppPredictionService);
        if (!android.text.TextUtils.isEmpty(customCountryClass)) {
            this.mCountryDetector = loadCustomCountryDetectorIfAvailable(customCountryClass);
        }
        if (this.mCountryDetector == null) {
            android.util.Slog.d(TAG, "Using default country detector");
            this.mCountryDetector = new com.android.server.location.countrydetector.ComprehensiveCountryDetector(this.mContext);
        }
        this.mLocationBasedDetectorListener = new android.location.CountryListener() { // from class: com.android.server.CountryDetectorService$$ExternalSyntheticLambda2
            public final void onCountryDetected(android.location.Country country) {
                this.f$0.lambda$initialize$2(country);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$2(final android.location.Country country) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.CountryDetectorService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$initialize$1(country);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setCountryListener$3(android.location.CountryListener listener) {
        this.mCountryDetector.setCountryListener(listener);
    }

    protected void setCountryListener(final android.location.CountryListener listener) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.CountryDetectorService$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setCountryListener$3(listener);
            }
        });
    }

    com.android.server.location.countrydetector.CountryDetectorBase getCountryDetector() {
        return this.mCountryDetector;
    }

    boolean isSystemReady() {
        return this.mSystemReady;
    }

    private com.android.server.location.countrydetector.CountryDetectorBase loadCustomCountryDetectorIfAvailable(java.lang.String customCountryClass) {
        android.util.Slog.d(TAG, "Using custom country detector class: " + customCountryClass);
        try {
            com.android.server.location.countrydetector.CountryDetectorBase customCountryDetector = (com.android.server.location.countrydetector.CountryDetectorBase) java.lang.Class.forName(customCountryClass).asSubclass(com.android.server.location.countrydetector.CountryDetectorBase.class).getConstructor(android.content.Context.class).newInstance(this.mContext);
            return customCountryDetector;
        } catch (java.lang.ClassNotFoundException | java.lang.IllegalAccessException | java.lang.InstantiationException | java.lang.NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            android.util.Slog.e(TAG, "Could not instantiate the custom country detector class");
            return null;
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, fout);
    }
}
