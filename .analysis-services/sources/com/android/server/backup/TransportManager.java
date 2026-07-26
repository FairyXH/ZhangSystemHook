package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class TransportManager {
    private static final boolean MORE_DEBUG = false;
    public static final java.lang.String SERVICE_ACTION_TRANSPORT_HOST = "android.backup.TRANSPORT_HOST";
    private static final java.lang.String TAG = "BackupTransportManager";
    private volatile java.lang.String mCurrentTransportName;
    private final android.content.pm.PackageManager mPackageManager;
    private final com.android.server.backup.transport.TransportConnectionManager mTransportConnectionManager;
    private final java.util.Set<android.content.ComponentName> mTransportWhitelist;
    private final int mUserId;
    private final android.content.Intent mTransportServiceIntent = new android.content.Intent(SERVICE_ACTION_TRANSPORT_HOST);
    private com.android.server.backup.transport.OnTransportRegisteredListener mOnTransportRegisteredListener = new com.android.server.backup.transport.OnTransportRegisteredListener() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda0
        @Override // com.android.server.backup.transport.OnTransportRegisteredListener
        public final void onTransportRegistered(java.lang.String str, java.lang.String str2) {
            com.android.server.backup.TransportManager.lambda$new$0(str, str2);
        }
    };
    private final java.lang.Object mTransportLock = new java.lang.Object();
    private final java.util.Map<android.content.ComponentName, com.android.server.backup.TransportManager.TransportDescription> mRegisteredTransportsDescriptionMap = new android.util.ArrayMap();
    private final com.android.server.backup.transport.TransportStats mTransportStats = new com.android.server.backup.transport.TransportStats();

    static /* synthetic */ void lambda$new$0(java.lang.String c, java.lang.String n) {
    }

    TransportManager(int userId, android.content.Context context, java.util.Set<android.content.ComponentName> whitelist, java.lang.String selectedTransport) {
        this.mUserId = userId;
        this.mPackageManager = context.getPackageManager();
        this.mTransportWhitelist = (java.util.Set) com.android.internal.util.Preconditions.checkNotNull(whitelist);
        this.mCurrentTransportName = selectedTransport;
        this.mTransportConnectionManager = new com.android.server.backup.transport.TransportConnectionManager(this.mUserId, context, this.mTransportStats);
    }

    TransportManager(int userId, android.content.Context context, java.util.Set<android.content.ComponentName> whitelist, java.lang.String selectedTransport, com.android.server.backup.transport.TransportConnectionManager transportConnectionManager) {
        this.mUserId = userId;
        this.mPackageManager = context.getPackageManager();
        this.mTransportWhitelist = (java.util.Set) com.android.internal.util.Preconditions.checkNotNull(whitelist);
        this.mCurrentTransportName = selectedTransport;
        this.mTransportConnectionManager = transportConnectionManager;
    }

    public void setOnTransportRegisteredListener(com.android.server.backup.transport.OnTransportRegisteredListener listener) {
        this.mOnTransportRegisteredListener = listener;
    }

    static /* synthetic */ boolean lambda$onPackageAdded$1(android.content.ComponentName transportComponent) {
        return true;
    }

    void onPackageAdded(java.lang.String packageName) {
        registerTransportsFromPackage(packageName, new java.util.function.Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.backup.TransportManager.lambda$onPackageAdded$1((android.content.ComponentName) obj);
            }
        });
    }

    void onPackageRemoved(java.lang.String packageName) {
        synchronized (this.mTransportLock) {
            this.mRegisteredTransportsDescriptionMap.keySet().removeIf(fromPackageFilter(packageName));
        }
    }

    void onPackageEnabled(java.lang.String packageName) {
        onPackageAdded(packageName);
    }

    void onPackageDisabled(java.lang.String packageName) {
        onPackageRemoved(packageName);
    }

    void onPackageChanged(java.lang.String packageName, java.lang.String... components) {
        if (components.length == 1 && components[0].equals(packageName)) {
            try {
                int enabled = this.mPackageManager.getApplicationEnabledSetting(packageName);
                switch (enabled) {
                    case 0:
                        onPackageEnabled(packageName);
                        return;
                    case 1:
                        onPackageEnabled(packageName);
                        return;
                    case 2:
                        onPackageDisabled(packageName);
                        return;
                    case 3:
                        onPackageDisabled(packageName);
                        return;
                    default:
                        android.util.Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Package " + packageName + " enabled setting: " + enabled));
                        return;
                }
            } catch (java.lang.IllegalArgumentException e) {
                return;
            }
        }
        final java.util.Set<android.content.ComponentName> transportComponents = new android.util.ArraySet<>(components.length);
        for (java.lang.String componentName : components) {
            transportComponents.add(new android.content.ComponentName(packageName, componentName));
        }
        if (transportComponents.isEmpty()) {
            return;
        }
        synchronized (this.mTransportLock) {
            java.util.Set<android.content.ComponentName> setKeySet = this.mRegisteredTransportsDescriptionMap.keySet();
            java.util.Objects.requireNonNull(transportComponents);
            setKeySet.removeIf(new java.util.function.Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return transportComponents.contains((android.content.ComponentName) obj);
                }
            });
        }
        java.util.Objects.requireNonNull(transportComponents);
        registerTransportsFromPackage(packageName, new java.util.function.Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return transportComponents.contains((android.content.ComponentName) obj);
            }
        });
    }

    android.content.ComponentName[] getRegisteredTransportComponents() {
        android.content.ComponentName[] componentNameArr;
        synchronized (this.mTransportLock) {
            componentNameArr = (android.content.ComponentName[]) this.mRegisteredTransportsDescriptionMap.keySet().toArray(new android.content.ComponentName[this.mRegisteredTransportsDescriptionMap.size()]);
        }
        return componentNameArr;
    }

    java.lang.String[] getRegisteredTransportNames() {
        java.lang.String[] transportNames;
        synchronized (this.mTransportLock) {
            transportNames = new java.lang.String[this.mRegisteredTransportsDescriptionMap.size()];
            int i = 0;
            for (com.android.server.backup.TransportManager.TransportDescription description : this.mRegisteredTransportsDescriptionMap.values()) {
                transportNames[i] = description.name;
                i++;
            }
        }
        return transportNames;
    }

    java.util.Set<android.content.ComponentName> getTransportWhitelist() {
        return this.mTransportWhitelist;
    }

    public java.lang.String getCurrentTransportName() {
        return this.mCurrentTransportName;
    }

    public android.content.ComponentName getCurrentTransportComponent() throws com.android.server.backup.transport.TransportNotRegisteredException {
        synchronized (this.mTransportLock) {
            if (this.mCurrentTransportName == null) {
                return null;
            }
            return getRegisteredTransportComponentOrThrowLocked(this.mCurrentTransportName);
        }
    }

    public java.lang.String getTransportName(android.content.ComponentName transportComponent) throws com.android.server.backup.transport.TransportNotRegisteredException {
        java.lang.String str;
        synchronized (this.mTransportLock) {
            str = getRegisteredTransportDescriptionOrThrowLocked(transportComponent).name;
        }
        return str;
    }

    public java.lang.String getTransportDirName(android.content.ComponentName transportComponent) throws com.android.server.backup.transport.TransportNotRegisteredException {
        java.lang.String str;
        synchronized (this.mTransportLock) {
            str = getRegisteredTransportDescriptionOrThrowLocked(transportComponent).transportDirName;
        }
        return str;
    }

    public java.lang.String getTransportDirName(java.lang.String transportName) throws com.android.server.backup.transport.TransportNotRegisteredException {
        java.lang.String str;
        synchronized (this.mTransportLock) {
            str = getRegisteredTransportDescriptionOrThrowLocked(transportName).transportDirName;
        }
        return str;
    }

    public android.content.Intent getTransportConfigurationIntent(java.lang.String transportName) throws com.android.server.backup.transport.TransportNotRegisteredException {
        android.content.Intent intent;
        synchronized (this.mTransportLock) {
            intent = getRegisteredTransportDescriptionOrThrowLocked(transportName).configurationIntent;
        }
        return intent;
    }

    public java.lang.String getTransportCurrentDestinationString(java.lang.String transportName) throws com.android.server.backup.transport.TransportNotRegisteredException {
        java.lang.String str;
        synchronized (this.mTransportLock) {
            str = getRegisteredTransportDescriptionOrThrowLocked(transportName).currentDestinationString;
        }
        return str;
    }

    public android.content.Intent getTransportDataManagementIntent(java.lang.String transportName) throws com.android.server.backup.transport.TransportNotRegisteredException {
        android.content.Intent intent;
        synchronized (this.mTransportLock) {
            intent = getRegisteredTransportDescriptionOrThrowLocked(transportName).dataManagementIntent;
        }
        return intent;
    }

    public java.lang.CharSequence getTransportDataManagementLabel(java.lang.String transportName) throws com.android.server.backup.transport.TransportNotRegisteredException {
        java.lang.CharSequence charSequence;
        synchronized (this.mTransportLock) {
            charSequence = getRegisteredTransportDescriptionOrThrowLocked(transportName).dataManagementLabel;
        }
        return charSequence;
    }

    public boolean isTransportRegistered(java.lang.String transportName) {
        boolean z;
        synchronized (this.mTransportLock) {
            z = getRegisteredTransportEntryLocked(transportName) != null;
        }
        return z;
    }

    public void forEachRegisteredTransport(java.util.function.Consumer<java.lang.String> transportConsumer) {
        synchronized (this.mTransportLock) {
            for (com.android.server.backup.TransportManager.TransportDescription transportDescription : this.mRegisteredTransportsDescriptionMap.values()) {
                transportConsumer.accept(transportDescription.name);
            }
        }
    }

    public void updateTransportAttributes(android.content.ComponentName transportComponent, java.lang.String name, android.content.Intent configurationIntent, java.lang.String currentDestinationString, android.content.Intent dataManagementIntent, java.lang.CharSequence dataManagementLabel) {
        synchronized (this.mTransportLock) {
            com.android.server.backup.TransportManager.TransportDescription description = this.mRegisteredTransportsDescriptionMap.get(transportComponent);
            if (description == null) {
                android.util.Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + name + " not registered tried to change description"));
                return;
            }
            description.name = name;
            description.configurationIntent = configurationIntent;
            description.currentDestinationString = currentDestinationString;
            description.dataManagementIntent = dataManagementIntent;
            description.dataManagementLabel = dataManagementLabel;
            android.util.Slog.d(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + name + " updated its attributes"));
        }
    }

    private android.content.ComponentName getRegisteredTransportComponentOrThrowLocked(java.lang.String transportName) throws com.android.server.backup.transport.TransportNotRegisteredException {
        android.content.ComponentName transportComponent = getRegisteredTransportComponentLocked(transportName);
        if (transportComponent == null) {
            throw new com.android.server.backup.transport.TransportNotRegisteredException(transportName);
        }
        return transportComponent;
    }

    private com.android.server.backup.TransportManager.TransportDescription getRegisteredTransportDescriptionOrThrowLocked(android.content.ComponentName transportComponent) throws com.android.server.backup.transport.TransportNotRegisteredException {
        com.android.server.backup.TransportManager.TransportDescription description = this.mRegisteredTransportsDescriptionMap.get(transportComponent);
        if (description == null) {
            throw new com.android.server.backup.transport.TransportNotRegisteredException(transportComponent);
        }
        return description;
    }

    private com.android.server.backup.TransportManager.TransportDescription getRegisteredTransportDescriptionOrThrowLocked(java.lang.String transportName) throws com.android.server.backup.transport.TransportNotRegisteredException {
        com.android.server.backup.TransportManager.TransportDescription description = getRegisteredTransportDescriptionLocked(transportName);
        if (description == null) {
            throw new com.android.server.backup.transport.TransportNotRegisteredException(transportName);
        }
        return description;
    }

    private android.content.ComponentName getRegisteredTransportComponentLocked(java.lang.String transportName) {
        java.util.Map.Entry<android.content.ComponentName, com.android.server.backup.TransportManager.TransportDescription> entry = getRegisteredTransportEntryLocked(transportName);
        if (entry == null) {
            return null;
        }
        return entry.getKey();
    }

    private com.android.server.backup.TransportManager.TransportDescription getRegisteredTransportDescriptionLocked(java.lang.String transportName) {
        java.util.Map.Entry<android.content.ComponentName, com.android.server.backup.TransportManager.TransportDescription> entry = getRegisteredTransportEntryLocked(transportName);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    private java.util.Map.Entry<android.content.ComponentName, com.android.server.backup.TransportManager.TransportDescription> getRegisteredTransportEntryLocked(java.lang.String transportName) {
        for (java.util.Map.Entry<android.content.ComponentName, com.android.server.backup.TransportManager.TransportDescription> entry : this.mRegisteredTransportsDescriptionMap.entrySet()) {
            com.android.server.backup.TransportManager.TransportDescription description = entry.getValue();
            if (transportName.equals(description.name)) {
                return entry;
            }
        }
        return null;
    }

    public com.android.server.backup.transport.TransportConnection getTransportClient(java.lang.String transportName, java.lang.String caller) {
        try {
            return getTransportClientOrThrow(transportName, caller);
        } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
            android.util.Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + transportName + " not registered"));
            return null;
        }
    }

    public com.android.server.backup.transport.TransportConnection getTransportClientOrThrow(java.lang.String transportName, java.lang.String caller) throws com.android.server.backup.transport.TransportNotRegisteredException {
        com.android.server.backup.transport.TransportConnection transportClient;
        synchronized (this.mTransportLock) {
            android.content.ComponentName component = getRegisteredTransportComponentLocked(transportName);
            if (component == null) {
                throw new com.android.server.backup.transport.TransportNotRegisteredException(transportName);
            }
            transportClient = this.mTransportConnectionManager.getTransportClient(component, caller);
        }
        return transportClient;
    }

    public com.android.server.backup.transport.TransportConnection getCurrentTransportClient(java.lang.String caller) {
        com.android.server.backup.transport.TransportConnection transportClient;
        if (this.mCurrentTransportName == null) {
            throw new java.lang.IllegalStateException("No transport selected");
        }
        synchronized (this.mTransportLock) {
            transportClient = getTransportClient(this.mCurrentTransportName, caller);
        }
        return transportClient;
    }

    public com.android.server.backup.transport.TransportConnection getCurrentTransportClientOrThrow(java.lang.String caller) throws com.android.server.backup.transport.TransportNotRegisteredException {
        com.android.server.backup.transport.TransportConnection transportClientOrThrow;
        if (this.mCurrentTransportName == null) {
            throw new java.lang.IllegalStateException("No transport selected");
        }
        synchronized (this.mTransportLock) {
            transportClientOrThrow = getTransportClientOrThrow(this.mCurrentTransportName, caller);
        }
        return transportClientOrThrow;
    }

    public void disposeOfTransportClient(com.android.server.backup.transport.TransportConnection transportConnection, java.lang.String caller) {
        this.mTransportConnectionManager.disposeOfTransportClient(transportConnection, caller);
    }

    @java.lang.Deprecated
    java.lang.String selectTransport(java.lang.String transportName) {
        java.lang.String prevTransport;
        synchronized (this.mTransportLock) {
            prevTransport = this.mCurrentTransportName;
            this.mCurrentTransportName = transportName;
        }
        return prevTransport;
    }

    public int registerAndSelectTransport(android.content.ComponentName transportComponent) {
        synchronized (this.mTransportLock) {
            try {
                try {
                    selectTransport(getTransportName(transportComponent));
                } catch (com.android.server.backup.transport.TransportNotRegisteredException e) {
                    int result = registerTransport(transportComponent);
                    if (result != 0) {
                        return result;
                    }
                    synchronized (this.mTransportLock) {
                        try {
                            try {
                                selectTransport(getTransportName(transportComponent));
                                return 0;
                            } finally {
                            }
                        } catch (com.android.server.backup.transport.TransportNotRegisteredException e2) {
                            android.util.Slog.wtf(TAG, addUserIdToLogMessage(this.mUserId, "Transport got unregistered"));
                            return -1;
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return 0;
    }

    static /* synthetic */ boolean lambda$registerTransports$2(android.content.ComponentName transportComponent) {
        return true;
    }

    public void registerTransports() {
        registerTransportsForIntent(this.mTransportServiceIntent, new java.util.function.Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.backup.TransportManager.lambda$registerTransports$2((android.content.ComponentName) obj);
            }
        });
    }

    private void registerTransportsFromPackage(java.lang.String packageName, java.util.function.Predicate<android.content.ComponentName> transportComponentFilter) {
        try {
            this.mPackageManager.getPackageInfoAsUser(packageName, 0, this.mUserId);
            registerTransportsForIntent(new android.content.Intent(this.mTransportServiceIntent).setPackage(packageName), transportComponentFilter.and(fromPackageFilter(packageName)));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Trying to register transports from package not found " + packageName));
        }
    }

    private void registerTransportsForIntent(android.content.Intent intent, java.util.function.Predicate<android.content.ComponentName> transportComponentFilter) {
        java.util.List<android.content.pm.ResolveInfo> hosts = this.mPackageManager.queryIntentServicesAsUser(intent, 0, this.mUserId);
        if (hosts == null) {
            return;
        }
        for (android.content.pm.ResolveInfo host : hosts) {
            android.content.ComponentName transportComponent = host.serviceInfo.getComponentName();
            if (transportComponentFilter.test(transportComponent) && isTransportTrusted(transportComponent)) {
                registerTransport(transportComponent);
            }
        }
    }

    private boolean isTransportTrusted(android.content.ComponentName transport) {
        if (!this.mTransportWhitelist.contains(transport)) {
            android.util.Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "BackupTransport " + transport.flattenToShortString() + " not whitelisted."));
            return false;
        }
        try {
            android.content.pm.PackageInfo packInfo = this.mPackageManager.getPackageInfoAsUser(transport.getPackageName(), 0, this.mUserId);
            if ((packInfo.applicationInfo.privateFlags & 8) == 0) {
                android.util.Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Transport package " + transport.getPackageName() + " not privileged"));
                return false;
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, addUserIdToLogMessage(this.mUserId, "Package not found."), e);
            return false;
        }
    }

    private int registerTransport(android.content.ComponentName transportComponent) {
        int result;
        java.lang.String transportName;
        java.lang.String transportDirName;
        checkCanUseTransport();
        if (!isTransportTrusted(transportComponent)) {
            return -2;
        }
        java.lang.String transportString = transportComponent.flattenToShortString();
        android.os.Bundle extras = new android.os.Bundle();
        extras.putBoolean("android.app.backup.extra.TRANSPORT_REGISTRATION", true);
        com.android.server.backup.transport.TransportConnection transportConnection = this.mTransportConnectionManager.getTransportClient(transportComponent, extras, "TransportManager.registerTransport()");
        try {
            com.android.server.backup.transport.BackupTransportClient transport = transportConnection.connectOrThrow("TransportManager.registerTransport()");
            try {
                transportName = transport.name();
                transportDirName = transport.transportDirName();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + transportString + " died while registering"));
                result = -1;
            }
            if (transportName != null && transportDirName != null) {
                registerTransport(transportComponent, transport);
                android.util.Slog.d(TAG, addUserIdToLogMessage(this.mUserId, "Transport " + transportString + " registered"));
                this.mOnTransportRegisteredListener.onTransportRegistered(transportName, transportDirName);
                result = 0;
                this.mTransportConnectionManager.disposeOfTransportClient(transportConnection, "TransportManager.registerTransport()");
                return result;
            }
            return -2;
        } catch (com.android.server.backup.transport.TransportNotAvailableException e2) {
            android.util.Slog.e(TAG, addUserIdToLogMessage(this.mUserId, "Couldn't connect to transport " + transportString + " for registration"));
            this.mTransportConnectionManager.disposeOfTransportClient(transportConnection, "TransportManager.registerTransport()");
            return -1;
        }
    }

    private void registerTransport(android.content.ComponentName transportComponent, com.android.server.backup.transport.BackupTransportClient transport) throws android.os.RemoteException {
        checkCanUseTransport();
        com.android.server.backup.TransportManager.TransportDescription description = new com.android.server.backup.TransportManager.TransportDescription(transport.name(), transport.transportDirName(), transport.configurationIntent(), transport.currentDestinationString(), transport.dataManagementIntent(), transport.dataManagementIntentLabel());
        synchronized (this.mTransportLock) {
            this.mRegisteredTransportsDescriptionMap.put(transportComponent, description);
        }
    }

    private void checkCanUseTransport() {
        com.android.internal.util.Preconditions.checkState(!java.lang.Thread.holdsLock(this.mTransportLock), "Can't call transport with transport lock held");
    }

    public void dumpTransportClients(java.io.PrintWriter pw) {
        this.mTransportConnectionManager.dump(pw);
    }

    public void dumpTransportStats(java.io.PrintWriter pw) {
        this.mTransportStats.dump(pw);
    }

    private static java.util.function.Predicate<android.content.ComponentName> fromPackageFilter(final java.lang.String packageName) {
        return new java.util.function.Predicate() { // from class: com.android.server.backup.TransportManager$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return packageName.equals(((android.content.ComponentName) obj).getPackageName());
            }
        };
    }

    private static class TransportDescription {
        private android.content.Intent configurationIntent;
        private java.lang.String currentDestinationString;
        private android.content.Intent dataManagementIntent;
        private java.lang.CharSequence dataManagementLabel;
        private java.lang.String name;
        private final java.lang.String transportDirName;

        private TransportDescription(java.lang.String name, java.lang.String transportDirName, android.content.Intent configurationIntent, java.lang.String currentDestinationString, android.content.Intent dataManagementIntent, java.lang.CharSequence dataManagementLabel) {
            this.name = name;
            this.transportDirName = transportDirName;
            this.configurationIntent = configurationIntent;
            this.currentDestinationString = currentDestinationString;
            this.dataManagementIntent = dataManagementIntent;
            this.dataManagementLabel = dataManagementLabel;
        }
    }

    private static java.lang.String addUserIdToLogMessage(int userId, java.lang.String message) {
        return "[UserID:" + userId + "] " + message;
    }
}
