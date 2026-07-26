package com.android.server.companion.devicepresence;

/* JADX INFO: loaded from: classes.dex */
public class CompanionAppBinder {
    private static final long REBIND_TIMEOUT = 10000;
    private static final java.lang.String TAG = "CDM_CompanionAppBinder";
    private final android.content.Context mContext;
    private final com.android.server.companion.devicepresence.CompanionAppBinder.CompanionServicesRegister mCompanionServicesRegister = new com.android.server.companion.devicepresence.CompanionAppBinder.CompanionServicesRegister();
    private final java.util.Map<android.util.Pair<java.lang.Integer, java.lang.String>, java.util.List<com.android.server.companion.devicepresence.CompanionServiceConnector>> mBoundCompanionApplications = new java.util.HashMap();
    private final java.util.Set<android.util.Pair<java.lang.Integer, java.lang.String>> mScheduledForRebindingCompanionApplications = new java.util.HashSet();

    public CompanionAppBinder(android.content.Context context) {
        this.mContext = context;
    }

    public void onPackagesChanged(int userId) {
        this.mCompanionServicesRegister.invalidate(userId);
    }

    public void bindCompanionApp(int userId, java.lang.String packageName, boolean isSelfManaged, com.android.server.companion.devicepresence.CompanionServiceConnector.Listener listener) {
        android.util.Slog.i(TAG, "Binding user=[" + userId + "], package=[" + packageName + "], isSelfManaged=[" + isSelfManaged + "]...");
        java.util.List<android.content.ComponentName> companionServices = this.mCompanionServicesRegister.forPackage(userId, packageName);
        if (companionServices.isEmpty()) {
            android.util.Slog.e(TAG, "Can not bind companion applications u" + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + ": eligible CompanionDeviceService not found.\nA CompanionDeviceService should declare an intent-filter for \"android.companion.CompanionDeviceService\" action and require \"android.permission.BIND_COMPANION_DEVICE_SERVICE\" permission.");
            return;
        }
        java.util.List<com.android.server.companion.devicepresence.CompanionServiceConnector> serviceConnectors = new java.util.ArrayList<>();
        synchronized (this.mBoundCompanionApplications) {
            if (this.mBoundCompanionApplications.containsKey(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName))) {
                android.util.Slog.w(TAG, "The package is ALREADY bound.");
                return;
            }
            int i = 0;
            while (i < companionServices.size()) {
                boolean isPrimary = i == 0;
                serviceConnectors.add(com.android.server.companion.devicepresence.CompanionServiceConnector.newInstance(this.mContext, userId, companionServices.get(i), isSelfManaged, isPrimary));
                i++;
            }
            this.mBoundCompanionApplications.put(new android.util.Pair<>(java.lang.Integer.valueOf(userId), packageName), serviceConnectors);
            for (com.android.server.companion.devicepresence.CompanionServiceConnector serviceConnector : serviceConnectors) {
                serviceConnector.setListener(listener);
            }
            for (com.android.server.companion.devicepresence.CompanionServiceConnector serviceConnector2 : serviceConnectors) {
                serviceConnector2.connect();
            }
        }
    }

    public void unbindCompanionApp(int userId, java.lang.String packageName) {
        java.util.List<com.android.server.companion.devicepresence.CompanionServiceConnector> serviceConnectors;
        android.util.Slog.i(TAG, "Unbinding user=[" + userId + "], package=[" + packageName + "]...");
        synchronized (this.mBoundCompanionApplications) {
            serviceConnectors = this.mBoundCompanionApplications.remove(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
        }
        synchronized (this.mScheduledForRebindingCompanionApplications) {
            this.mScheduledForRebindingCompanionApplications.remove(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
        }
        if (serviceConnectors == null) {
            android.util.Slog.e(TAG, "The package is not bound.");
            return;
        }
        for (com.android.server.companion.devicepresence.CompanionServiceConnector serviceConnector : serviceConnectors) {
            serviceConnector.postUnbind();
        }
    }

    public boolean isCompanionApplicationBound(int userId, java.lang.String packageName) {
        boolean zContainsKey;
        synchronized (this.mBoundCompanionApplications) {
            zContainsKey = this.mBoundCompanionApplications.containsKey(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
        }
        return zContainsKey;
    }

    public void removePackage(int userId, java.lang.String packageName) {
        synchronized (this.mBoundCompanionApplications) {
            this.mBoundCompanionApplications.remove(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
        }
        synchronized (this.mScheduledForRebindingCompanionApplications) {
            this.mScheduledForRebindingCompanionApplications.remove(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
        }
    }

    public void scheduleRebinding(final int userId, final java.lang.String packageName, final com.android.server.companion.devicepresence.CompanionServiceConnector serviceConnector) {
        android.util.Slog.i(TAG, "scheduleRebinding() " + userId + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName);
        if (isRebindingCompanionApplicationScheduled(userId, packageName)) {
            android.util.Slog.i(TAG, "CompanionApplication rebinding has been scheduled, skipping " + serviceConnector.getComponentName());
            return;
        }
        if (serviceConnector.isPrimary()) {
            synchronized (this.mScheduledForRebindingCompanionApplications) {
                this.mScheduledForRebindingCompanionApplications.add(new android.util.Pair<>(java.lang.Integer.valueOf(userId), packageName));
            }
        }
        android.os.Handler.getMain().postDelayed(new java.lang.Runnable() { // from class: com.android.server.companion.devicepresence.CompanionAppBinder$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRebinding$0(userId, packageName, serviceConnector);
            }
        }, 10000L);
    }

    private boolean isRebindingCompanionApplicationScheduled(int userId, java.lang.String packageName) {
        boolean zContains;
        synchronized (this.mScheduledForRebindingCompanionApplications) {
            zContains = this.mScheduledForRebindingCompanionApplications.contains(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
        }
        return zContains;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onRebindingCompanionApplicationTimeout, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleRebinding$0(int userId, java.lang.String packageName, com.android.server.companion.devicepresence.CompanionServiceConnector serviceConnector) {
        if (serviceConnector.isPrimary()) {
            synchronized (this.mBoundCompanionApplications) {
                if (!this.mBoundCompanionApplications.containsKey(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName))) {
                    java.util.List<com.android.server.companion.devicepresence.CompanionServiceConnector> serviceConnectors = java.util.Collections.singletonList(serviceConnector);
                    this.mBoundCompanionApplications.put(new android.util.Pair<>(java.lang.Integer.valueOf(userId), packageName), serviceConnectors);
                }
            }
            synchronized (this.mScheduledForRebindingCompanionApplications) {
                this.mScheduledForRebindingCompanionApplications.remove(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
            }
        }
        serviceConnector.connect();
    }

    public void dump(java.io.PrintWriter out) {
        out.append("Companion Device Application Controller: \n");
        synchronized (this.mBoundCompanionApplications) {
            out.append("  Bound Companion Applications: ");
            if (this.mBoundCompanionApplications.isEmpty()) {
                out.append("<empty>\n");
            } else {
                out.append("\n");
                for (java.util.Map.Entry<android.util.Pair<java.lang.Integer, java.lang.String>, java.util.List<com.android.server.companion.devicepresence.CompanionServiceConnector>> entry : this.mBoundCompanionApplications.entrySet()) {
                    out.append("<u").append((java.lang.CharSequence) java.lang.String.valueOf(entry.getKey().first)).append(", ").append((java.lang.CharSequence) entry.getKey().second).append(">");
                    for (com.android.server.companion.devicepresence.CompanionServiceConnector serviceConnector : entry.getValue()) {
                        out.append(", isPrimary=").append((java.lang.CharSequence) java.lang.String.valueOf(serviceConnector.isPrimary()));
                    }
                }
            }
        }
        out.append("  Companion Applications Scheduled For Rebinding: ");
        synchronized (this.mScheduledForRebindingCompanionApplications) {
            if (this.mScheduledForRebindingCompanionApplications.isEmpty()) {
                out.append("<empty>\n");
            } else {
                out.append("\n");
                for (android.util.Pair<java.lang.Integer, java.lang.String> app : this.mScheduledForRebindingCompanionApplications) {
                    out.append("<u").append((java.lang.CharSequence) java.lang.String.valueOf(app.first)).append(", ").append((java.lang.CharSequence) app.second).append(">");
                }
            }
        }
    }

    com.android.server.companion.devicepresence.CompanionServiceConnector getPrimaryServiceConnector(int userId, java.lang.String packageName) {
        java.util.List<com.android.server.companion.devicepresence.CompanionServiceConnector> connectors;
        synchronized (this.mBoundCompanionApplications) {
            connectors = this.mBoundCompanionApplications.get(new android.util.Pair(java.lang.Integer.valueOf(userId), packageName));
        }
        if (connectors != null) {
            return connectors.get(0);
        }
        return null;
    }

    private class CompanionServicesRegister extends com.android.internal.infra.PerUser<java.util.Map<java.lang.String, java.util.List<android.content.ComponentName>>> {
        private CompanionServicesRegister() {
        }

        public synchronized java.util.Map<java.lang.String, java.util.List<android.content.ComponentName>> forUser(int userId) {
            return (java.util.Map) super.forUser(userId);
        }

        synchronized java.util.List<android.content.ComponentName> forPackage(int userId, java.lang.String packageName) {
            return forUser(userId).getOrDefault(packageName, java.util.Collections.emptyList());
        }

        synchronized void invalidate(int userId) {
            remove(userId);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final java.util.Map<java.lang.String, java.util.List<android.content.ComponentName>> create(int userId) {
            return com.android.server.companion.utils.PackageUtils.getCompanionServicesForUser(com.android.server.companion.devicepresence.CompanionAppBinder.this.mContext, userId);
        }
    }
}
