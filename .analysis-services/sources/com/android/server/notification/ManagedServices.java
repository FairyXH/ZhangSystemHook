package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ManagedServices {
    static final int APPROVAL_BY_COMPONENT = 1;
    static final int APPROVAL_BY_PACKAGE = 0;
    static final java.lang.String ATT_APPROVED_LIST = "approved";
    static final java.lang.String ATT_DEFAULTS = "defaults";
    static final java.lang.String ATT_IS_PRIMARY = "primary";
    static final java.lang.String ATT_USER_CHANGED = "user_changed";
    static final java.lang.String ATT_USER_ID = "user";
    static final java.lang.String ATT_USER_SET = "user_set_services";
    static final java.lang.String ATT_USER_SET_OLD = "user_set";
    static final java.lang.String ATT_VERSION = "version";
    static final java.lang.String DB_VERSION = "4";
    private static final java.lang.String DB_VERSION_1 = "1";
    private static final java.lang.String DB_VERSION_2 = "2";
    private static final java.lang.String DB_VERSION_3 = "3";
    protected static final java.lang.String ENABLED_SERVICES_SEPARATOR = ":";
    private static final int ON_BINDING_DIED_REBIND_DELAY_MS = 10000;
    static final java.lang.String TAG_MANAGED_SERVICES = "service_listing";
    protected final android.content.Context mContext;
    protected final java.lang.Object mMutex;
    protected final android.content.pm.IPackageManager mPm;
    protected final android.os.UserManager mUm;
    private boolean mUseXml;
    private final com.android.server.notification.ManagedServices.UserProfiles mUserProfiles;
    protected final java.lang.String TAG = getClass().getSimpleName();
    protected final boolean DEBUG = android.util.Log.isLoggable(this.TAG, 3);
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final java.util.ArrayList<com.android.server.notification.ManagedServices.ManagedServiceInfo> mServices = new java.util.ArrayList<>();
    private final java.util.ArrayList<android.util.Pair<android.content.ComponentName, java.lang.Integer>> mServicesBound = new java.util.ArrayList<>();
    private final android.util.ArraySet<android.util.Pair<android.content.ComponentName, java.lang.Integer>> mServicesRebinding = new android.util.ArraySet<>();
    protected final java.lang.Object mDefaultsLock = new java.lang.Object();
    protected final android.util.ArraySet<android.content.ComponentName> mDefaultComponents = new android.util.ArraySet<>();
    protected final android.util.ArraySet<java.lang.String> mDefaultPackages = new android.util.ArraySet<>();
    private final android.util.ArraySet<android.content.ComponentName> mEnabledServicesForCurrentProfiles = new android.util.ArraySet<>();
    private final android.util.ArraySet<java.lang.String> mEnabledServicesPackageNames = new android.util.ArraySet<>();
    private final android.util.SparseSetArray<android.content.ComponentName> mSnoozing = new android.util.SparseSetArray<>();
    protected final android.util.ArrayMap<java.lang.Integer, android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>>> mApproved = new android.util.ArrayMap<>();
    protected android.util.ArrayMap<java.lang.Integer, android.util.ArraySet<java.lang.String>> mUserSetServices = new android.util.ArrayMap<>();
    protected android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> mIsUserChanged = new android.util.ArrayMap<>();
    private com.android.server.notification.IManagedServicesExt mManagedServicesExt = (com.android.server.notification.IManagedServicesExt) system.ext.loader.core.ExtLoader.type(com.android.server.notification.IManagedServicesExt.class).base(this).create();
    private final com.android.server.notification.ManagedServices.Config mConfig = getConfig();
    protected int mApprovalLevel = 1;

    public static class Config {
        public java.lang.String bindPermission;
        public java.lang.String caption;
        public int clientLabel;
        public java.lang.String secondarySettingName;
        public java.lang.String secureSettingName;
        public java.lang.String serviceInterface;
        public java.lang.String settingsAction;
        public java.lang.String xmlTag;
    }

    protected abstract boolean allowRebindForParentUser();

    protected abstract android.os.IInterface asInterface(android.os.IBinder iBinder);

    protected abstract boolean checkType(android.os.IInterface iInterface);

    protected abstract void ensureFilters(android.content.pm.ServiceInfo serviceInfo, int i);

    protected abstract com.android.server.notification.ManagedServices.Config getConfig();

    protected abstract java.lang.String getRequiredPermission();

    protected abstract void loadDefaultsFromConfig();

    protected abstract void onServiceAdded(com.android.server.notification.ManagedServices.ManagedServiceInfo managedServiceInfo);

    public ManagedServices(android.content.Context context, java.lang.Object mutex, com.android.server.notification.ManagedServices.UserProfiles userProfiles, android.content.pm.IPackageManager pm) {
        this.mContext = context;
        this.mMutex = mutex;
        this.mUserProfiles = userProfiles;
        this.mPm = pm;
        this.mUm = (android.os.UserManager) this.mContext.getSystemService(ATT_USER_ID);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getCaption() {
        return this.mConfig.caption;
    }

    protected java.util.List<com.android.server.notification.ManagedServices.ManagedServiceInfo> getServices() {
        java.util.List<com.android.server.notification.ManagedServices.ManagedServiceInfo> services;
        synchronized (this.mMutex) {
            services = new java.util.ArrayList<>(this.mServices);
        }
        return services;
    }

    protected void addDefaultComponentOrPackage(java.lang.String packageOrComponent) {
        if (!android.text.TextUtils.isEmpty(packageOrComponent)) {
            synchronized (this.mDefaultsLock) {
                if (this.mApprovalLevel == 0) {
                    this.mDefaultPackages.add(packageOrComponent);
                    return;
                }
                android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(packageOrComponent);
                if (cn != null && this.mApprovalLevel == 1) {
                    this.mDefaultPackages.add(cn.getPackageName());
                    this.mDefaultComponents.add(cn);
                }
            }
        }
    }

    boolean isDefaultComponentOrPackage(java.lang.String packageOrComponent) {
        synchronized (this.mDefaultsLock) {
            android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(packageOrComponent);
            if (cn == null) {
                return this.mDefaultPackages.contains(packageOrComponent);
            }
            return this.mDefaultComponents.contains(cn);
        }
    }

    android.util.ArraySet<android.content.ComponentName> getDefaultComponents() {
        android.util.ArraySet<android.content.ComponentName> arraySet;
        synchronized (this.mDefaultsLock) {
            arraySet = new android.util.ArraySet<>(this.mDefaultComponents);
        }
        return arraySet;
    }

    android.util.ArraySet<java.lang.String> getDefaultPackages() {
        android.util.ArraySet<java.lang.String> arraySet;
        synchronized (this.mDefaultsLock) {
            arraySet = new android.util.ArraySet<>(this.mDefaultPackages);
        }
        return arraySet;
    }

    android.util.ArrayMap<java.lang.Boolean, java.util.ArrayList<android.content.ComponentName>> resetComponents(java.lang.String packageName, int userId) {
        java.util.ArrayList<android.content.ComponentName> componentsToEnable;
        java.util.ArrayList<android.content.ComponentName> disabledComponents;
        android.util.ArraySet<android.content.ComponentName> enabledComponents = new android.util.ArraySet<>(getAllowedComponents(userId));
        boolean changed = false;
        synchronized (this.mDefaultsLock) {
            componentsToEnable = new java.util.ArrayList<>(this.mDefaultComponents.size());
            disabledComponents = new java.util.ArrayList<>(this.mDefaultComponents.size());
            for (int i = 0; i < this.mDefaultComponents.size() && enabledComponents.size() > 0; i++) {
                android.content.ComponentName currentDefault = this.mDefaultComponents.valueAt(i);
                if (packageName.equals(currentDefault.getPackageName()) && !enabledComponents.contains(currentDefault)) {
                    componentsToEnable.add(currentDefault);
                }
            }
            synchronized (this.mApproved) {
                android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.get(java.lang.Integer.valueOf(userId));
                if (approvedByType != null) {
                    int M = approvedByType.size();
                    for (int j = 0; j < M; j++) {
                        android.util.ArraySet<java.lang.String> approved = approvedByType.valueAt(j);
                        for (int i2 = 0; i2 < enabledComponents.size(); i2++) {
                            android.content.ComponentName currentComponent = enabledComponents.valueAt(i2);
                            if (packageName.equals(currentComponent.getPackageName()) && !this.mDefaultComponents.contains(currentComponent) && approved.remove(currentComponent.flattenToString())) {
                                disabledComponents.add(currentComponent);
                                clearUserSetFlagLocked(currentComponent, userId);
                                changed = true;
                            }
                        }
                        for (int i3 = 0; i3 < componentsToEnable.size(); i3++) {
                            android.content.ComponentName candidate = componentsToEnable.get(i3);
                            changed |= approved.add(candidate.flattenToString());
                        }
                    }
                }
            }
        }
        if (changed) {
            rebindServices(false, -1);
        }
        android.util.ArrayMap<java.lang.Boolean, java.util.ArrayList<android.content.ComponentName>> changes = new android.util.ArrayMap<>();
        changes.put(true, componentsToEnable);
        changes.put(false, disabledComponents);
        return changes;
    }

    private boolean clearUserSetFlagLocked(android.content.ComponentName component, int userId) {
        java.lang.String approvedValue = getApprovedValue(component.flattenToString());
        android.util.ArraySet<java.lang.String> userSet = this.mUserSetServices.get(java.lang.Integer.valueOf(userId));
        return userSet != null && userSet.remove(approvedValue);
    }

    protected int getBindFlags() {
        return 83886081;
    }

    protected void onServiceRemovedLocked(com.android.server.notification.ManagedServices.ManagedServiceInfo removed) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.notification.ManagedServices.ManagedServiceInfo newServiceInfo(android.os.IInterface service, android.content.ComponentName component, int userId, boolean isSystem, android.content.ServiceConnection connection, int targetSdkVersion, int uid) {
        return new com.android.server.notification.ManagedServices.ManagedServiceInfo(service, component, userId, isSystem, connection, targetSdkVersion, uid);
    }

    public void onBootPhaseAppsCanStart() {
    }

    public void dump(java.io.PrintWriter pw, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        android.util.SparseSetArray<android.content.ComponentName> snoozingComponents;
        pw.println("    Allowed " + getCaption() + "s:");
        synchronized (this.mApproved) {
            int N = this.mApproved.size();
            for (int i = 0; i < N; i++) {
                int userId = this.mApproved.keyAt(i).intValue();
                android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.valueAt(i);
                java.lang.Boolean userChanged = this.mIsUserChanged.get(java.lang.Integer.valueOf(userId));
                if (approvedByType != null) {
                    int M = approvedByType.size();
                    for (int j = 0; j < M; j++) {
                        boolean isPrimary = approvedByType.keyAt(j).booleanValue();
                        android.util.ArraySet<java.lang.String> approved = approvedByType.valueAt(j);
                        if (approvedByType != null && approvedByType.size() > 0) {
                            pw.println("      " + java.lang.String.join(ENABLED_SERVICES_SEPARATOR, approved) + " (user: " + userId + " isPrimary: " + isPrimary + (userChanged == null ? "" : " isUserChanged: " + userChanged) + ")");
                        }
                    }
                }
            }
            pw.println("    Has user set:");
            java.util.Set<java.lang.Integer> userIds = this.mUserSetServices.keySet();
            java.util.Iterator<java.lang.Integer> it = userIds.iterator();
            while (it.hasNext()) {
                int userId2 = it.next().intValue();
                if (this.mIsUserChanged.get(java.lang.Integer.valueOf(userId2)) == null) {
                    pw.println("      userId=" + userId2 + " value=" + this.mUserSetServices.get(java.lang.Integer.valueOf(userId2)));
                }
            }
        }
        synchronized (this.mMutex) {
            pw.println("    All " + getCaption() + "s (" + this.mEnabledServicesForCurrentProfiles.size() + ") enabled for current profiles:");
            for (android.content.ComponentName cmpt : this.mEnabledServicesForCurrentProfiles) {
                if (filter == null || filter.matches(cmpt)) {
                    pw.println("      " + cmpt);
                }
            }
            pw.println("    Live " + getCaption() + "s (" + this.mServices.size() + "):");
            for (com.android.server.notification.ManagedServices.ManagedServiceInfo info : this.mServices) {
                if (filter == null || filter.matches(info.component)) {
                    pw.println("      " + info.component + " (user " + info.userid + "): " + info.service + (info.isSystem ? " SYSTEM" : "") + (info.isGuest(this) ? " GUEST" : ""));
                }
            }
        }
        synchronized (this.mSnoozing) {
            snoozingComponents = new android.util.SparseSetArray<>(this.mSnoozing);
        }
        pw.println("    Snoozed " + getCaption() + "s (" + snoozingComponents.size() + "):");
        for (int i2 = 0; i2 < snoozingComponents.size(); i2++) {
            pw.println("      User: " + snoozingComponents.keyAt(i2));
            for (android.content.ComponentName name : snoozingComponents.valuesAt(i2)) {
                pw.println("        " + name.flattenToShortString() + (isAutobindAllowed(getServiceInfo(name, snoozingComponents.keyAt(i2))) ? "" : " (META_DATA_DEFAULT_AUTOBIND=false)"));
            }
        }
    }

    public void dump(android.util.proto.ProtoOutputStream proto, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        int N;
        int N2;
        proto.write(1138166333441L, getCaption());
        synchronized (this.mApproved) {
            int N3 = this.mApproved.size();
            int i = 0;
            while (true) {
                long j = 2246267895810L;
                if (i >= N3) {
                    break;
                }
                int userId = this.mApproved.keyAt(i).intValue();
                android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.valueAt(i);
                if (approvedByType == null) {
                    N = N3;
                } else {
                    int M = approvedByType.size();
                    int j2 = 0;
                    while (j2 < M) {
                        boolean isPrimary = approvedByType.keyAt(j2).booleanValue();
                        android.util.ArraySet<java.lang.String> approved = approvedByType.valueAt(j2);
                        if (approvedByType == null || approvedByType.size() <= 0) {
                            N2 = N3;
                        } else {
                            long sToken = proto.start(j);
                            for (java.lang.String s : approved) {
                                proto.write(2237677961217L, s);
                                N3 = N3;
                            }
                            N2 = N3;
                            proto.write(1120986464258L, userId);
                            proto.write(1133871366147L, isPrimary);
                            proto.end(sToken);
                        }
                        j2++;
                        N3 = N2;
                        j = 2246267895810L;
                    }
                    N = N3;
                }
                i++;
                N3 = N;
            }
        }
        synchronized (this.mMutex) {
            for (android.content.ComponentName cmpt : this.mEnabledServicesForCurrentProfiles) {
                if (filter == null || filter.matches(cmpt)) {
                    cmpt.dumpDebug(proto, 2246267895811L);
                }
            }
            for (com.android.server.notification.ManagedServices.ManagedServiceInfo info : this.mServices) {
                if (filter == null || filter.matches(info.component)) {
                    info.dumpDebug(proto, 2246267895812L, this);
                }
            }
        }
        synchronized (this.mSnoozing) {
            for (int i2 = 0; i2 < this.mSnoozing.size(); i2++) {
                long token = proto.start(2246267895814L);
                proto.write(1120986464257L, this.mSnoozing.keyAt(i2));
                for (android.content.ComponentName name : this.mSnoozing.valuesAt(i2)) {
                    name.dumpDebug(proto, 2246267895810L);
                }
                proto.end(token);
            }
        }
    }

    protected void onSettingRestored(java.lang.String element, java.lang.String value, int backupSdkInt, int userId) {
        if (!this.mUseXml) {
            android.util.Slog.d(this.TAG, "Restored managed service setting: " + element);
            if (this.mConfig.secureSettingName.equals(element) || (this.mConfig.secondarySettingName != null && this.mConfig.secondarySettingName.equals(element))) {
                if (backupSdkInt < 26) {
                    java.lang.String currentSetting = getApproved(userId, this.mConfig.secureSettingName.equals(element));
                    if (!android.text.TextUtils.isEmpty(currentSetting)) {
                        if (!android.text.TextUtils.isEmpty(value)) {
                            value = value + ENABLED_SERVICES_SEPARATOR + currentSetting;
                        } else {
                            value = currentSetting;
                        }
                    }
                }
                if (shouldReflectToSettings()) {
                    android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), element, value, userId);
                }
                for (android.content.pm.UserInfo user : this.mUm.getUsers()) {
                    addApprovedList(value, user.id, this.mConfig.secureSettingName.equals(element));
                }
                android.util.Slog.d(this.TAG, "Done loading approved values from settings");
                rebindServices(false, userId);
            }
        }
    }

    void writeDefaults(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
        synchronized (this.mDefaultsLock) {
            java.util.List<java.lang.String> componentStrings = new java.util.ArrayList<>(this.mDefaultComponents.size());
            for (int i = 0; i < this.mDefaultComponents.size(); i++) {
                componentStrings.add(this.mDefaultComponents.valueAt(i).flattenToString());
            }
            java.lang.String defaults = java.lang.String.join(ENABLED_SERVICES_SEPARATOR, componentStrings);
            out.attribute((java.lang.String) null, ATT_DEFAULTS, defaults);
        }
    }

    public void writeXml(com.android.modules.utils.TypedXmlSerializer out, boolean forBackup, int userId) throws java.io.IOException {
        int N;
        java.lang.String allowedItems;
        int N2;
        int i = userId;
        out.startTag((java.lang.String) null, getConfig().xmlTag);
        out.attributeInt((java.lang.String) null, ATT_VERSION, java.lang.Integer.parseInt(DB_VERSION));
        writeDefaults(out);
        if (forBackup) {
            trimApprovedListsAccordingToInstalledServices(i);
        }
        synchronized (this.mApproved) {
            int N3 = this.mApproved.size();
            int i2 = 0;
            while (i2 < N3) {
                int approvedUserId = this.mApproved.keyAt(i2).intValue();
                if (forBackup && approvedUserId != i) {
                    N = N3;
                } else {
                    android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.valueAt(i2);
                    java.lang.Boolean isUserChanged = this.mIsUserChanged.get(java.lang.Integer.valueOf(approvedUserId));
                    if (approvedByType == null) {
                        N = N3;
                    } else {
                        int M = approvedByType.size();
                        int j = 0;
                        while (j < M) {
                            boolean isPrimary = approvedByType.keyAt(j).booleanValue();
                            java.util.Set<java.lang.String> approved = approvedByType.valueAt(j);
                            java.util.Set<java.lang.String> userSet = this.mUserSetServices.get(java.lang.Integer.valueOf(approvedUserId));
                            if (approved == null && userSet == null && isUserChanged == null) {
                                N2 = N3;
                            } else {
                                if (approved == null) {
                                    allowedItems = "";
                                } else {
                                    allowedItems = java.lang.String.join(ENABLED_SERVICES_SEPARATOR, approved);
                                }
                                N2 = N3;
                                out.startTag((java.lang.String) null, TAG_MANAGED_SERVICES);
                                out.attribute((java.lang.String) null, ATT_APPROVED_LIST, allowedItems);
                                out.attributeInt((java.lang.String) null, ATT_USER_ID, approvedUserId);
                                out.attributeBoolean((java.lang.String) null, ATT_IS_PRIMARY, isPrimary);
                                if (isUserChanged != null) {
                                    out.attributeBoolean((java.lang.String) null, ATT_USER_CHANGED, isUserChanged.booleanValue());
                                } else if (userSet != null) {
                                    java.lang.String userSetItems = java.lang.String.join(ENABLED_SERVICES_SEPARATOR, userSet);
                                    out.attribute((java.lang.String) null, ATT_USER_SET, userSetItems);
                                }
                                writeExtraAttributes(out, approvedUserId);
                                out.endTag((java.lang.String) null, TAG_MANAGED_SERVICES);
                                if (!forBackup && isPrimary && shouldReflectToSettings()) {
                                    android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), getConfig().secureSettingName, allowedItems, approvedUserId);
                                }
                            }
                            j++;
                            N3 = N2;
                        }
                        N = N3;
                    }
                }
                i2++;
                i = userId;
                N3 = N;
            }
        }
        writeExtraXmlTags(out);
        out.endTag((java.lang.String) null, getConfig().xmlTag);
    }

    protected boolean shouldReflectToSettings() {
        return false;
    }

    protected void writeExtraAttributes(com.android.modules.utils.TypedXmlSerializer out, int userId) throws java.io.IOException {
    }

    protected void writeExtraXmlTags(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
    }

    protected void readExtraTag(java.lang.String tag, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
    }

    protected final void migrateToXml() {
        for (android.content.pm.UserInfo user : this.mUm.getUsers()) {
            android.content.ContentResolver cr = this.mContext.getContentResolver();
            if (!android.text.TextUtils.isEmpty(getConfig().secureSettingName)) {
                addApprovedList(android.provider.Settings.Secure.getStringForUser(cr, getConfig().secureSettingName, user.id), user.id, true);
            }
            if (!android.text.TextUtils.isEmpty(getConfig().secondarySettingName)) {
                addApprovedList(android.provider.Settings.Secure.getStringForUser(cr, getConfig().secondarySettingName, user.id), user.id, false);
            }
        }
    }

    void readDefaults(com.android.modules.utils.TypedXmlPullParser parser) {
        java.lang.String defaultComponents = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_DEFAULTS);
        if (!android.text.TextUtils.isEmpty(defaultComponents)) {
            java.lang.String[] components = defaultComponents.split(ENABLED_SERVICES_SEPARATOR);
            synchronized (this.mDefaultsLock) {
                for (int i = 0; i < components.length; i++) {
                    if (!android.text.TextUtils.isEmpty(components[i])) {
                        android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(components[i]);
                        if (cn != null) {
                            this.mDefaultPackages.add(cn.getPackageName());
                            this.mDefaultComponents.add(cn);
                        } else {
                            this.mDefaultPackages.add(components[i]);
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:97:? -> B:55:0x00fa). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:99:? -> B:37:0x00ca). Please report as a decompilation issue!!! */
    public void readXml(com.android.modules.utils.TypedXmlPullParser parser, com.android.internal.util.function.TriPredicate<java.lang.String, java.lang.Integer, java.lang.String> allowedManagedServicePackages, boolean forRestore, int userId) throws java.lang.Throwable {
        boolean z;
        int resolvedUserId;
        java.lang.String version = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_VERSION);
        readDefaults(parser);
        boolean needUpgradeUserset = false;
        while (true) {
            int type = parser.next();
            if (type == 1) {
                z = true;
                break;
            }
            java.lang.String tag = parser.getName();
            if (type == 3 && getConfig().xmlTag.equals(tag)) {
                z = true;
                break;
            }
            if (type == 2) {
                if (TAG_MANAGED_SERVICES.equals(tag)) {
                    android.util.Slog.i(this.TAG, "Read " + this.mConfig.caption + " permissions from xml");
                    java.lang.String approved = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_APPROVED_LIST);
                    if (!forRestore) {
                        resolvedUserId = parser.getAttributeInt((java.lang.String) null, ATT_USER_ID, 0);
                    } else {
                        resolvedUserId = userId;
                    }
                    boolean isPrimary = parser.getAttributeBoolean((java.lang.String) null, ATT_IS_PRIMARY, true);
                    java.lang.String isUserChanged = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_USER_CHANGED);
                    java.lang.String isUserChanged_Old = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_USER_SET_OLD);
                    java.lang.String userSetComponent = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATT_USER_SET);
                    if (DB_VERSION.equals(version)) {
                        if (isUserChanged == null) {
                            userSetComponent = android.text.TextUtils.emptyIfNull(userSetComponent);
                        } else {
                            synchronized (this.mApproved) {
                                try {
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    throw th;
                                }
                                try {
                                    this.mIsUserChanged.put(java.lang.Integer.valueOf(resolvedUserId), java.lang.Boolean.valueOf(isUserChanged));
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            }
                            userSetComponent = java.lang.Boolean.valueOf(isUserChanged).booleanValue() ? approved : "";
                        }
                    } else {
                        needUpgradeUserset = true;
                        if (userSetComponent == null) {
                            if (isUserChanged_Old != null && java.lang.Boolean.valueOf(isUserChanged_Old).booleanValue()) {
                                synchronized (this.mApproved) {
                                    try {
                                    } catch (java.lang.Throwable th3) {
                                        th = th3;
                                        throw th;
                                    }
                                    try {
                                        this.mIsUserChanged.put(java.lang.Integer.valueOf(resolvedUserId), true);
                                    } catch (java.lang.Throwable th4) {
                                        th = th4;
                                        throw th;
                                    }
                                }
                                needUpgradeUserset = false;
                                userSetComponent = approved;
                            } else {
                                userSetComponent = "";
                                needUpgradeUserset = true;
                            }
                        }
                    }
                    readExtraAttributes(tag, parser, resolvedUserId);
                    if (allowedManagedServicePackages == null || allowedManagedServicePackages.test(getPackageName(approved), java.lang.Integer.valueOf(resolvedUserId), getRequiredPermission()) || approved.isEmpty()) {
                        if (this.mUm.getUserInfo(resolvedUserId) != null) {
                            addApprovedList(approved, resolvedUserId, isPrimary, userSetComponent);
                        }
                        this.mUseXml = true;
                    }
                } else {
                    readExtraTag(tag, parser);
                }
            }
        }
        boolean isOldVersion = (android.text.TextUtils.isEmpty(version) || DB_VERSION_1.equals(version) || DB_VERSION_2.equals(version) || DB_VERSION_3.equals(version)) ? z : false;
        if (isOldVersion) {
            upgradeDefaultsXmlVersion();
        }
        if (needUpgradeUserset) {
            upgradeUserSet();
        }
        rebindServices(false, -1);
    }

    void upgradeDefaultsXmlVersion() {
        int defaultsSize;
        int defaultsSize2;
        synchronized (this.mDefaultsLock) {
            defaultsSize = this.mDefaultComponents.size() + this.mDefaultPackages.size();
        }
        if (defaultsSize == 0) {
            if (this.mApprovalLevel == 1) {
                java.util.List<android.content.ComponentName> approvedComponents = getAllowedComponents(0);
                for (int i = 0; i < approvedComponents.size(); i++) {
                    addDefaultComponentOrPackage(approvedComponents.get(i).flattenToString());
                }
            }
            if (this.mApprovalLevel == 0) {
                java.util.List<java.lang.String> approvedPkgs = getAllowedPackages(0);
                for (int i2 = 0; i2 < approvedPkgs.size(); i2++) {
                    addDefaultComponentOrPackage(approvedPkgs.get(i2));
                }
            }
        }
        synchronized (this.mDefaultsLock) {
            defaultsSize2 = this.mDefaultComponents.size() + this.mDefaultPackages.size();
        }
        if (defaultsSize2 == 0) {
            loadDefaultsFromConfig();
        }
    }

    protected void upgradeUserSet() {
    }

    protected void readExtraAttributes(java.lang.String tag, com.android.modules.utils.TypedXmlPullParser parser, int userId) throws java.io.IOException {
    }

    protected void addApprovedList(java.lang.String approved, int userId, boolean isPrimary) {
        addApprovedList(approved, userId, isPrimary, approved);
    }

    protected void addApprovedList(java.lang.String approved, int userId, boolean isPrimary, java.lang.String userSet) {
        if (android.text.TextUtils.isEmpty(approved)) {
            approved = "";
        }
        if (userSet == null) {
            userSet = approved;
        }
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.get(java.lang.Integer.valueOf(userId));
            if (approvedByType == null) {
                approvedByType = new android.util.ArrayMap<>();
                this.mApproved.put(java.lang.Integer.valueOf(userId), approvedByType);
            }
            android.util.ArraySet<java.lang.String> approvedList = approvedByType.get(java.lang.Boolean.valueOf(isPrimary));
            if (approvedList == null) {
                approvedList = new android.util.ArraySet<>();
                approvedByType.put(java.lang.Boolean.valueOf(isPrimary), approvedList);
            }
            java.lang.String[] approvedArray = approved.split(ENABLED_SERVICES_SEPARATOR);
            for (java.lang.String pkgOrComponent : approvedArray) {
                java.lang.String approvedItem = getApprovedValue(pkgOrComponent);
                if (approvedItem != null) {
                    approvedList.add(approvedItem);
                }
            }
            android.util.ArraySet<java.lang.String> userSetList = this.mUserSetServices.get(java.lang.Integer.valueOf(userId));
            if (userSetList == null) {
                userSetList = new android.util.ArraySet<>();
                this.mUserSetServices.put(java.lang.Integer.valueOf(userId), userSetList);
            }
            java.lang.String[] userSetArray = userSet.split(ENABLED_SERVICES_SEPARATOR);
            for (java.lang.String pkgOrComponent2 : userSetArray) {
                java.lang.String approvedItem2 = getApprovedValue(pkgOrComponent2);
                if (approvedItem2 != null) {
                    userSetList.add(approvedItem2);
                }
            }
        }
    }

    protected boolean isComponentEnabledForPackage(java.lang.String pkg) {
        boolean zContains;
        synchronized (this.mMutex) {
            zContains = this.mEnabledServicesPackageNames.contains(pkg);
        }
        return zContains;
    }

    protected void setPackageOrComponentEnabled(java.lang.String pkgOrComponent, int userId, boolean isPrimary, boolean enabled) {
        setPackageOrComponentEnabled(pkgOrComponent, userId, isPrimary, enabled, true);
    }

    protected void setPackageOrComponentEnabled(java.lang.String pkgOrComponent, int userId, boolean isPrimary, boolean enabled, boolean userSet) {
        android.util.Slog.i(this.TAG, (enabled ? " Allowing " : "Disallowing ") + this.mConfig.caption + " " + pkgOrComponent + " (userSet: " + userSet + ")");
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> allowedByType = this.mApproved.get(java.lang.Integer.valueOf(userId));
            if (allowedByType == null) {
                allowedByType = new android.util.ArrayMap<>();
                this.mApproved.put(java.lang.Integer.valueOf(userId), allowedByType);
            }
            android.util.ArraySet<java.lang.String> approved = allowedByType.get(java.lang.Boolean.valueOf(isPrimary));
            if (approved == null) {
                approved = new android.util.ArraySet<>();
                allowedByType.put(java.lang.Boolean.valueOf(isPrimary), approved);
            }
            java.lang.String approvedItem = getApprovedValue(pkgOrComponent);
            if (approvedItem != null) {
                if (enabled) {
                    approved.add(approvedItem);
                } else {
                    approved.remove(approvedItem);
                }
            }
            android.util.ArraySet<java.lang.String> userSetServices = this.mUserSetServices.get(java.lang.Integer.valueOf(userId));
            if (userSetServices == null) {
                userSetServices = new android.util.ArraySet<>();
                this.mUserSetServices.put(java.lang.Integer.valueOf(userId), userSetServices);
            }
            if (userSet) {
                userSetServices.add(pkgOrComponent);
            } else {
                userSetServices.remove(pkgOrComponent);
            }
        }
        rebindServices(false, userId);
    }

    private java.lang.String getApprovedValue(java.lang.String pkgOrComponent) {
        if (this.mApprovalLevel == 1) {
            if (android.content.ComponentName.unflattenFromString(pkgOrComponent) != null) {
                return pkgOrComponent;
            }
            return null;
        }
        return getPackageName(pkgOrComponent);
    }

    protected java.lang.String getApproved(int userId, boolean primary) {
        java.lang.String strJoin;
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> allowedByType = this.mApproved.getOrDefault(java.lang.Integer.valueOf(userId), new android.util.ArrayMap<>());
            android.util.ArraySet<java.lang.String> approved = allowedByType.getOrDefault(java.lang.Boolean.valueOf(primary), new android.util.ArraySet<>());
            strJoin = java.lang.String.join(ENABLED_SERVICES_SEPARATOR, approved);
        }
        return strJoin;
    }

    protected java.util.List<android.content.ComponentName> getAllowedComponents(int userId) {
        java.util.List<android.content.ComponentName> allowedComponents = new java.util.ArrayList<>();
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> allowedByType = this.mApproved.getOrDefault(java.lang.Integer.valueOf(userId), new android.util.ArrayMap<>());
            for (int i = 0; i < allowedByType.size(); i++) {
                android.util.ArraySet<java.lang.String> allowed = allowedByType.valueAt(i);
                for (int j = 0; j < allowed.size(); j++) {
                    android.content.ComponentName cn = android.content.ComponentName.unflattenFromString(allowed.valueAt(j));
                    if (cn != null) {
                        allowedComponents.add(cn);
                    }
                }
            }
        }
        return allowedComponents;
    }

    protected java.util.List<java.lang.String> getAllowedPackages(int userId) {
        java.util.List<java.lang.String> allowedPackages = new java.util.ArrayList<>();
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> allowedByType = this.mApproved.getOrDefault(java.lang.Integer.valueOf(userId), new android.util.ArrayMap<>());
            for (int i = 0; i < allowedByType.size(); i++) {
                android.util.ArraySet<java.lang.String> allowed = allowedByType.valueAt(i);
                for (int j = 0; j < allowed.size(); j++) {
                    java.lang.String pkgName = getPackageName(allowed.valueAt(j));
                    if (!android.text.TextUtils.isEmpty(pkgName)) {
                        allowedPackages.add(pkgName);
                    }
                }
            }
        }
        return allowedPackages;
    }

    protected boolean isPackageOrComponentAllowed(java.lang.String pkgOrComponent, int userId) {
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> allowedByType = this.mApproved.getOrDefault(java.lang.Integer.valueOf(userId), new android.util.ArrayMap<>());
            for (int i = 0; i < allowedByType.size(); i++) {
                android.util.ArraySet<java.lang.String> allowed = allowedByType.valueAt(i);
                if (allowed.contains(pkgOrComponent)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected boolean isPackageOrComponentAllowedWithPermission(android.content.ComponentName component, int userId) {
        if (!isPackageOrComponentAllowed(component.flattenToString(), userId) && !isPackageOrComponentAllowed(component.getPackageName(), userId)) {
            return false;
        }
        return componentHasBindPermission(component, userId);
    }

    private boolean componentHasBindPermission(android.content.ComponentName component, int userId) {
        android.content.pm.ServiceInfo info = getServiceInfo(component, userId);
        if (info == null) {
            return false;
        }
        return this.mConfig.bindPermission.equals(info.permission);
    }

    boolean isPackageOrComponentUserSet(java.lang.String pkgOrComponent, int userId) {
        boolean z;
        synchronized (this.mApproved) {
            android.util.ArraySet<java.lang.String> services = this.mUserSetServices.get(java.lang.Integer.valueOf(userId));
            z = services != null && services.contains(pkgOrComponent);
        }
        return z;
    }

    protected boolean isPackageAllowed(java.lang.String pkg, int userId) {
        if (pkg == null) {
            return false;
        }
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> allowedByType = this.mApproved.getOrDefault(java.lang.Integer.valueOf(userId), new android.util.ArrayMap<>());
            for (int i = 0; i < allowedByType.size(); i++) {
                android.util.ArraySet<java.lang.String> allowed = allowedByType.valueAt(i);
                for (java.lang.String allowedEntry : allowed) {
                    if (allowedEntry != null) {
                        android.content.ComponentName component = android.content.ComponentName.unflattenFromString(allowedEntry);
                        if (component != null) {
                            if (pkg.equals(component.getPackageName())) {
                                return true;
                            }
                        } else if (pkg.equals(allowedEntry)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public void onPackagesChanged(boolean removingPackage, java.lang.String[] pkgList, int[] uidList) {
        if (this.DEBUG) {
            synchronized (this.mMutex) {
                android.util.Slog.d(this.TAG, "onPackagesChanged removingPackage=" + removingPackage + " pkgList=" + (pkgList == null ? null : java.util.Arrays.asList(pkgList)) + " mEnabledServicesPackageNames=" + this.mEnabledServicesPackageNames);
            }
        }
        if (pkgList != null && pkgList.length > 0) {
            boolean anyServicesInvolved = false;
            if (removingPackage && uidList != null) {
                int size = java.lang.Math.min(pkgList.length, uidList.length);
                for (int i = 0; i < size; i++) {
                    java.lang.String pkg = pkgList[i];
                    int userId = android.os.UserHandle.getUserId(uidList[i]);
                    anyServicesInvolved = removeUninstalledItemsFromApprovedLists(userId, pkg);
                }
            }
            synchronized (this.mMutex) {
                for (java.lang.String pkgName : pkgList) {
                    if (isComponentEnabledForPackage(pkgName)) {
                        anyServicesInvolved = true;
                    }
                    if (uidList != null && uidList.length > 0) {
                        for (int uid : uidList) {
                            if (isPackageAllowed(pkgName, android.os.UserHandle.getUserId(uid))) {
                                anyServicesInvolved = true;
                                trimApprovedListsForInvalidServices(pkgName, android.os.UserHandle.getUserId(uid));
                            }
                        }
                    }
                }
            }
            if (anyServicesInvolved) {
                rebindServices(false, -1);
            }
        }
    }

    public void onUserRemoved(int user) {
        android.util.Slog.i(this.TAG, "Removing approved services for removed user " + user);
        synchronized (this.mApproved) {
            this.mApproved.remove(java.lang.Integer.valueOf(user));
        }
        synchronized (this.mSnoozing) {
            this.mSnoozing.remove(user);
        }
        unbindUserServices(user);
    }

    public void onUserSwitched(int user) {
        if (this.DEBUG) {
            android.util.Slog.d(this.TAG, "onUserSwitched u=" + user);
        }
        unbindOtherUserServices(user);
        rebindServices(true, user);
    }

    public void onUserUnlocked(int user) {
        if (this.DEBUG) {
            android.util.Slog.d(this.TAG, "onUserUnlocked u=" + user);
        }
        rebindServices(false, user);
    }

    private com.android.server.notification.ManagedServices.ManagedServiceInfo getServiceFromTokenLocked(android.os.IInterface service) {
        if (service == null) {
            return null;
        }
        android.os.IBinder token = service.asBinder();
        synchronized (this.mMutex) {
            int nServices = this.mServices.size();
            for (int i = 0; i < nServices; i++) {
                com.android.server.notification.ManagedServices.ManagedServiceInfo info = this.mServices.get(i);
                if (info.service.asBinder() == token) {
                    return info;
                }
            }
            return null;
        }
    }

    protected boolean isServiceTokenValidLocked(android.os.IInterface service) {
        if (service == null) {
            return false;
        }
        com.android.server.notification.ManagedServices.ManagedServiceInfo info = getServiceFromTokenLocked(service);
        if (info == null) {
            return false;
        }
        return true;
    }

    protected com.android.server.notification.ManagedServices.ManagedServiceInfo checkServiceTokenLocked(android.os.IInterface service) {
        checkNotNull(service);
        com.android.server.notification.ManagedServices.ManagedServiceInfo info = getServiceFromTokenLocked(service);
        if (info != null) {
            return info;
        }
        throw new java.lang.SecurityException("Disallowed call from unknown " + getCaption() + ": " + service + " " + service.getClass());
    }

    public boolean isSameUser(android.os.IInterface service, int userId) {
        checkNotNull(service);
        synchronized (this.mMutex) {
            com.android.server.notification.ManagedServices.ManagedServiceInfo info = getServiceFromTokenLocked(service);
            if (info == null) {
                return false;
            }
            return info.isSameUser(userId);
        }
    }

    public void unregisterService(android.os.IInterface service, int userid) {
        checkNotNull(service);
        unregisterServiceImpl(service, userid);
    }

    public void registerSystemService(android.os.IInterface service, android.content.ComponentName component, int userid, int uid) {
        checkNotNull(service);
        com.android.server.notification.ManagedServices.ManagedServiceInfo info = registerServiceImpl(service, component, userid, 10000, uid);
        if (info != null) {
            onServiceAdded(info);
        }
    }

    protected void registerGuestService(com.android.server.notification.ManagedServices.ManagedServiceInfo guest) {
        checkNotNull(guest.service);
        if (!checkType(guest.service)) {
            throw new java.lang.IllegalArgumentException();
        }
        if (registerServiceImpl(guest) != null) {
            onServiceAdded(guest);
        }
    }

    protected void setComponentState(android.content.ComponentName component, int userId, boolean enabled) {
        synchronized (this.mSnoozing) {
            boolean previous = !this.mSnoozing.contains(userId, component);
            if (previous == enabled) {
                return;
            }
            if (enabled) {
                this.mSnoozing.remove(userId, component);
            } else {
                this.mSnoozing.add(userId, component);
            }
            android.util.Slog.d(this.TAG, (enabled ? "Enabling " : "Disabling ") + "component " + component.flattenToShortString());
            synchronized (this.mMutex) {
                if (enabled) {
                    if (isPackageOrComponentAllowedWithPermission(component, userId)) {
                        registerServiceLocked(component, userId);
                    } else {
                        android.util.Slog.d(this.TAG, component + " no longer has permission to be bound");
                    }
                } else {
                    unregisterServiceLocked(component, userId);
                }
            }
        }
    }

    private android.util.ArraySet<android.content.ComponentName> loadComponentNamesFromValues(android.util.ArraySet<java.lang.String> approved, int userId) {
        if (approved == null || approved.size() == 0) {
            return new android.util.ArraySet<>();
        }
        android.util.ArraySet<android.content.ComponentName> result = new android.util.ArraySet<>(approved.size());
        for (int i = 0; i < approved.size(); i++) {
            java.lang.String packageOrComponent = approved.valueAt(i);
            if (!android.text.TextUtils.isEmpty(packageOrComponent)) {
                android.content.ComponentName component = android.content.ComponentName.unflattenFromString(packageOrComponent);
                if (component != null) {
                    result.add(component);
                } else {
                    result.addAll(queryPackageForServices(packageOrComponent, userId));
                }
            }
        }
        return result;
    }

    protected java.util.Set<android.content.ComponentName> queryPackageForServices(java.lang.String packageName, int userId) {
        return queryPackageForServices(packageName, 0, userId);
    }

    protected android.util.ArraySet<android.content.ComponentName> queryPackageForServices(java.lang.String packageName, int extraFlags, int userId) {
        android.util.ArraySet<android.content.ComponentName> installed = new android.util.ArraySet<>();
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        android.content.Intent queryIntent = new android.content.Intent(this.mConfig.serviceInterface);
        if (!android.text.TextUtils.isEmpty(packageName)) {
            queryIntent.setPackage(packageName);
        }
        java.util.List<android.content.pm.ResolveInfo> installedServices = pm.queryIntentServicesAsUser(queryIntent, extraFlags | 132, userId);
        if (this.DEBUG) {
            android.util.Slog.v(this.TAG, this.mConfig.serviceInterface + " services: " + installedServices);
        }
        if (installedServices != null) {
            int count = installedServices.size();
            for (int i = 0; i < count; i++) {
                android.content.pm.ResolveInfo resolveInfo = installedServices.get(i);
                android.content.pm.ServiceInfo info = resolveInfo.serviceInfo;
                android.content.ComponentName component = new android.content.ComponentName(info.packageName, info.name);
                if (!this.mConfig.bindPermission.equals(info.permission)) {
                    android.util.Slog.w(this.TAG, "Skipping " + getCaption() + " service " + info.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + info.name + ": it does not require the permission " + this.mConfig.bindPermission);
                } else {
                    installed.add(component);
                }
            }
        }
        return installed;
    }

    private void trimApprovedListsAccordingToInstalledServices(int userId) {
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.get(java.lang.Integer.valueOf(userId));
            if (approvedByType == null) {
                return;
            }
            for (int i = 0; i < approvedByType.size(); i++) {
                android.util.ArraySet<java.lang.String> approved = approvedByType.valueAt(i);
                for (int j = approved.size() - 1; j >= 0; j--) {
                    java.lang.String approvedPackageOrComponent = approved.valueAt(j);
                    if (!isValidEntry(approvedPackageOrComponent, userId)) {
                        approved.removeAt(j);
                        android.util.Slog.v(this.TAG, "Removing " + approvedPackageOrComponent + " from approved list; no matching services found");
                    } else if (this.DEBUG) {
                        android.util.Slog.v(this.TAG, "Keeping " + approvedPackageOrComponent + " on approved list; matching services found");
                    }
                }
            }
        }
    }

    private boolean removeUninstalledItemsFromApprovedLists(int uninstalledUserId, java.lang.String pkg) {
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.get(java.lang.Integer.valueOf(uninstalledUserId));
            if (approvedByType != null) {
                int M = approvedByType.size();
                for (int j = 0; j < M; j++) {
                    android.util.ArraySet<java.lang.String> approved = approvedByType.valueAt(j);
                    int O = approved.size();
                    for (int k = O - 1; k >= 0; k--) {
                        java.lang.String packageOrComponent = approved.valueAt(k);
                        java.lang.String packageName = getPackageName(packageOrComponent);
                        if (android.text.TextUtils.equals(pkg, packageName)) {
                            approved.removeAt(k);
                            if (this.DEBUG) {
                                android.util.Slog.v(this.TAG, "Removing " + packageOrComponent + " from approved list; uninstalled");
                            }
                        }
                    }
                }
            }
            android.util.ArraySet<java.lang.String> userSet = this.mUserSetServices.get(java.lang.Integer.valueOf(uninstalledUserId));
            if (userSet != null) {
                int numServices = userSet.size();
                for (int i = numServices - 1; i >= 0; i--) {
                    java.lang.String pkgOrComponent = userSet.valueAt(i);
                    if (android.text.TextUtils.equals(pkg, getPackageName(pkgOrComponent))) {
                        userSet.removeAt(i);
                        if (this.DEBUG) {
                            android.util.Slog.v(this.TAG, "Removing " + pkgOrComponent + " from user-set list; uninstalled");
                        }
                    }
                }
            }
        }
        return false;
    }

    private void trimApprovedListsForInvalidServices(java.lang.String packageName, int userId) {
        android.content.ComponentName component;
        synchronized (this.mApproved) {
            android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedByType = this.mApproved.get(java.lang.Integer.valueOf(userId));
            if (approvedByType == null) {
                return;
            }
            for (int i = 0; i < approvedByType.size(); i++) {
                android.util.ArraySet<java.lang.String> approved = approvedByType.valueAt(i);
                for (int j = approved.size() - 1; j >= 0; j--) {
                    java.lang.String approvedPackageOrComponent = approved.valueAt(j);
                    if (android.text.TextUtils.equals(getPackageName(approvedPackageOrComponent), packageName) && (component = android.content.ComponentName.unflattenFromString(approvedPackageOrComponent)) != null && !componentHasBindPermission(component, userId)) {
                        approved.removeAt(j);
                        if (this.DEBUG) {
                            android.util.Slog.v(this.TAG, "Removing " + approvedPackageOrComponent + " from approved list; no bind permission found " + this.mConfig.bindPermission);
                        }
                    }
                }
            }
        }
    }

    protected java.lang.String getPackageName(java.lang.String packageOrComponent) {
        android.content.ComponentName component = android.content.ComponentName.unflattenFromString(packageOrComponent);
        if (component != null) {
            return component.getPackageName();
        }
        return packageOrComponent;
    }

    protected boolean isValidEntry(java.lang.String packageOrComponent, int userId) {
        return hasMatchingServices(packageOrComponent, userId);
    }

    private boolean hasMatchingServices(java.lang.String packageOrComponent, int userId) {
        if (android.text.TextUtils.isEmpty(packageOrComponent)) {
            return false;
        }
        java.lang.String packageName = getPackageName(packageOrComponent);
        return queryPackageForServices(packageName, userId).size() > 0;
    }

    protected android.util.SparseArray<android.util.ArraySet<android.content.ComponentName>> getAllowedComponents(android.util.IntArray userIds) {
        int nUserIds = userIds.size();
        android.util.SparseArray<android.util.ArraySet<android.content.ComponentName>> componentsByUser = new android.util.SparseArray<>();
        for (int i = 0; i < nUserIds; i++) {
            int userId = userIds.get(i);
            synchronized (this.mApproved) {
                android.util.ArrayMap<java.lang.Boolean, android.util.ArraySet<java.lang.String>> approvedLists = this.mApproved.get(java.lang.Integer.valueOf(userId));
                if (approvedLists != null) {
                    int N = approvedLists.size();
                    for (int j = 0; j < N; j++) {
                        android.util.ArraySet<android.content.ComponentName> approvedByUser = componentsByUser.get(userId);
                        if (approvedByUser == null) {
                            approvedByUser = new android.util.ArraySet<>();
                            componentsByUser.put(userId, approvedByUser);
                        }
                        approvedByUser.addAll((android.util.ArraySet<? extends android.content.ComponentName>) loadComponentNamesFromValues(approvedLists.valueAt(j), userId));
                    }
                }
            }
        }
        return componentsByUser;
    }

    protected void populateComponentsToBind(android.util.SparseArray<java.util.Set<android.content.ComponentName>> componentsToBind, android.util.IntArray activeUsers, android.util.SparseArray<android.util.ArraySet<android.content.ComponentName>> approvedComponentsByUser) {
        this.mEnabledServicesForCurrentProfiles.clear();
        this.mEnabledServicesPackageNames.clear();
        int nUserIds = activeUsers.size();
        for (int i = 0; i < nUserIds; i++) {
            int userId = activeUsers.get(i);
            android.util.ArraySet<android.content.ComponentName> userComponents = approvedComponentsByUser.get(userId);
            if (userComponents == null) {
                componentsToBind.put(userId, new android.util.ArraySet());
            } else {
                java.util.Set<android.content.ComponentName> add = new java.util.HashSet<>(userComponents);
                synchronized (this.mSnoozing) {
                    android.util.ArraySet<android.content.ComponentName> snoozed = this.mSnoozing.get(userId);
                    if (snoozed != null) {
                        add.removeAll(snoozed);
                    }
                }
                componentsToBind.put(userId, add);
                this.mEnabledServicesForCurrentProfiles.addAll((android.util.ArraySet<? extends android.content.ComponentName>) userComponents);
                for (int j = 0; j < userComponents.size(); j++) {
                    android.content.ComponentName component = userComponents.valueAt(j);
                    this.mEnabledServicesPackageNames.add(component.getPackageName());
                }
            }
        }
    }

    protected java.util.Set<com.android.server.notification.ManagedServices.ManagedServiceInfo> getRemovableConnectedServices() {
        java.util.Set<com.android.server.notification.ManagedServices.ManagedServiceInfo> removableBoundServices = new android.util.ArraySet<>();
        for (com.android.server.notification.ManagedServices.ManagedServiceInfo service : this.mServices) {
            if (!service.isSystem && !service.isGuest(this)) {
                removableBoundServices.add(service);
            }
        }
        return removableBoundServices;
    }

    protected void populateComponentsToUnbind(boolean forceRebind, java.util.Set<com.android.server.notification.ManagedServices.ManagedServiceInfo> removableBoundServices, android.util.SparseArray<java.util.Set<android.content.ComponentName>> allowedComponentsToBind, android.util.SparseArray<java.util.Set<android.content.ComponentName>> componentsToUnbind) {
        for (com.android.server.notification.ManagedServices.ManagedServiceInfo info : removableBoundServices) {
            java.util.Set<android.content.ComponentName> allowedComponents = allowedComponentsToBind.get(info.userid);
            if (allowedComponents != null && (forceRebind || !allowedComponents.contains(info.component))) {
                java.util.Set<android.content.ComponentName> toUnbind = componentsToUnbind.get(info.userid, new android.util.ArraySet());
                toUnbind.add(info.component);
                componentsToUnbind.put(info.userid, toUnbind);
            }
        }
    }

    protected void rebindServices(boolean forceRebind, int userToRebind) {
        if (this.DEBUG) {
            android.util.Slog.d(this.TAG, "rebindServices " + forceRebind + " " + userToRebind);
        }
        if (this.mManagedServicesExt.isInterceptRebindServices(forceRebind, userToRebind)) {
            android.util.Slog.v(this.TAG, "rebindServices : return for multi app");
            return;
        }
        android.util.IntArray userIds = this.mUserProfiles.getCurrentProfileIds();
        boolean rebindAllCurrentUsers = this.mUserProfiles.isProfileUser(userToRebind, this.mContext) && allowRebindForParentUser();
        if (userToRebind != -1 && !rebindAllCurrentUsers) {
            userIds = new android.util.IntArray(1);
            userIds.add(userToRebind);
        }
        android.util.SparseArray<java.util.Set<android.content.ComponentName>> componentsToBind = new android.util.SparseArray<>();
        android.util.SparseArray<java.util.Set<android.content.ComponentName>> componentsToUnbind = new android.util.SparseArray<>();
        synchronized (this.mMutex) {
            android.util.SparseArray<android.util.ArraySet<android.content.ComponentName>> approvedComponentsByUser = getAllowedComponents(userIds);
            java.util.Set<com.android.server.notification.ManagedServices.ManagedServiceInfo> removableBoundServices = getRemovableConnectedServices();
            populateComponentsToBind(componentsToBind, userIds, approvedComponentsByUser);
            populateComponentsToUnbind(forceRebind, removableBoundServices, componentsToBind, componentsToUnbind);
        }
        unbindFromServices(componentsToUnbind);
        bindToServices(componentsToBind);
    }

    void unbindOtherUserServices(int currentUser) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("ManagedServices.unbindOtherUserServices_current" + currentUser);
        unbindServicesImpl(currentUser, true);
        t.traceEnd();
    }

    void unbindUserServices(int user) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog();
        t.traceBegin("ManagedServices.unbindUserServices" + user);
        unbindServicesImpl(user, false);
        t.traceEnd();
    }

    void unbindServicesImpl(int user, boolean allExceptUser) {
        android.util.SparseArray<java.util.Set<android.content.ComponentName>> componentsToUnbind = new android.util.SparseArray<>();
        synchronized (this.mMutex) {
            java.util.Set<com.android.server.notification.ManagedServices.ManagedServiceInfo> removableBoundServices = getRemovableConnectedServices();
            for (com.android.server.notification.ManagedServices.ManagedServiceInfo info : removableBoundServices) {
                if ((allExceptUser && info.userid != user) || (!allExceptUser && info.userid == user)) {
                    java.util.Set<android.content.ComponentName> toUnbind = componentsToUnbind.get(info.userid, new android.util.ArraySet());
                    toUnbind.add(info.component);
                    componentsToUnbind.put(info.userid, toUnbind);
                }
            }
        }
        unbindFromServices(componentsToUnbind);
    }

    protected void unbindFromServices(android.util.SparseArray<java.util.Set<android.content.ComponentName>> componentsToUnbind) {
        for (int i = 0; i < componentsToUnbind.size(); i++) {
            int userId = componentsToUnbind.keyAt(i);
            java.util.Set<android.content.ComponentName> removableComponents = componentsToUnbind.get(userId);
            for (android.content.ComponentName cn : removableComponents) {
                android.util.Slog.v(this.TAG, "disabling " + getCaption() + " for user " + userId + ": " + cn);
                unregisterService(cn, userId);
            }
        }
    }

    private void bindToServices(android.util.SparseArray<java.util.Set<android.content.ComponentName>> componentsToBind) {
        for (int i = 0; i < componentsToBind.size(); i++) {
            int userId = componentsToBind.keyAt(i);
            java.util.Set<android.content.ComponentName> add = componentsToBind.get(userId);
            for (android.content.ComponentName component : add) {
                android.content.pm.ServiceInfo info = getServiceInfo(component, userId);
                if (info == null) {
                    android.util.Slog.w(this.TAG, "Not binding " + getCaption() + " service " + component + ": service not found");
                } else if (!this.mConfig.bindPermission.equals(info.permission)) {
                    android.util.Slog.w(this.TAG, "Not binding " + getCaption() + " service " + component + ": it does not require the permission " + this.mConfig.bindPermission);
                } else if (!isAutobindAllowed(info) && !isBoundOrRebinding(component, userId)) {
                    synchronized (this.mSnoozing) {
                        android.util.Slog.d(this.TAG, "Not binding " + getCaption() + " service " + component + ": has META_DATA_DEFAULT_AUTOBIND = false");
                        this.mSnoozing.add(userId, component);
                    }
                } else {
                    android.util.Slog.v(this.TAG, "enabling " + getCaption() + " for " + userId + ": " + component);
                    registerService(info, userId);
                }
            }
        }
    }

    void registerService(android.content.pm.ServiceInfo si, int userId) {
        ensureFilters(si, userId);
        registerService(si.getComponentName(), userId);
    }

    void registerService(android.content.ComponentName cn, int userId) {
        synchronized (this.mMutex) {
            registerServiceLocked(cn, userId);
        }
    }

    void reregisterService(android.content.ComponentName cn, int userId) {
        if (isPackageOrComponentAllowedWithPermission(cn, userId)) {
            registerService(cn, userId);
        }
    }

    public void registerSystemService(android.content.ComponentName name, int userid) {
        synchronized (this.mMutex) {
            registerServiceLocked(name, userid, true);
        }
    }

    private void registerServiceLocked(android.content.ComponentName name, int userid) {
        registerServiceLocked(name, userid, false);
    }

    private void registerServiceLocked(android.content.ComponentName name, int userid, boolean isSystem) {
        android.content.pm.ApplicationInfo appInfo;
        if (this.DEBUG) {
            android.util.Slog.v(this.TAG, "registerService: " + name + " u=" + userid);
        }
        android.util.Pair<android.content.ComponentName, java.lang.Integer> servicesBindingTag = android.util.Pair.create(name, java.lang.Integer.valueOf(userid));
        if (this.mServicesBound.contains(servicesBindingTag)) {
            android.util.Slog.v(this.TAG, "Not registering " + name + " is already bound");
            return;
        }
        this.mServicesBound.add(servicesBindingTag);
        int N = this.mServices.size();
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.notification.ManagedServices.ManagedServiceInfo info = this.mServices.get(i);
            if (name.equals(info.component) && info.userid == userid) {
                android.util.Slog.v(this.TAG, "    disconnecting old " + getCaption() + ": " + info.service);
                removeServiceLocked(i);
                if (info.connection != null) {
                    unbindService(info.connection, info.component, info.userid);
                }
            }
        }
        android.content.Intent intent = new android.content.Intent(this.mConfig.serviceInterface);
        intent.setComponent(name);
        intent.putExtra("android.intent.extra.client_label", this.mConfig.clientLabel);
        android.app.ActivityOptions activityOptions = android.app.ActivityOptions.makeBasic();
        activityOptions.setPendingIntentCreatorBackgroundActivityStartMode(2);
        android.app.PendingIntent pendingIntent = android.app.PendingIntent.getActivity(this.mContext, 0, new android.content.Intent(this.mConfig.settingsAction), 67108864, activityOptions.toBundle());
        intent.putExtra("android.intent.extra.client_intent", pendingIntent);
        try {
            android.content.pm.ApplicationInfo appInfo2 = this.mContext.getPackageManager().getApplicationInfoAsUser(name.getPackageName(), 0, userid);
            appInfo = appInfo2;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            appInfo = null;
        }
        int targetSdkVersion = appInfo != null ? appInfo.targetSdkVersion : 1;
        int uid = appInfo != null ? appInfo.uid : -1;
        try {
            android.util.Slog.v(this.TAG, "binding: " + intent);
            try {
                android.content.ServiceConnection serviceConnection = new com.android.server.notification.ManagedServices.AnonymousClass1(userid, servicesBindingTag, isSystem, targetSdkVersion, uid);
                if (!this.mContext.bindServiceAsUser(intent, serviceConnection, getBindFlags(), new android.os.UserHandle(userid))) {
                    this.mServicesBound.remove(servicesBindingTag);
                    android.util.Slog.w(this.TAG, "Unable to bind " + getCaption() + " service: " + intent + " in user " + userid);
                }
            } catch (java.lang.SecurityException e2) {
                ex = e2;
                this.mServicesBound.remove(servicesBindingTag);
                android.util.Slog.e(this.TAG, "Unable to bind " + getCaption() + " service: " + intent, ex);
            }
        } catch (java.lang.SecurityException e3) {
            ex = e3;
        }
    }

    /* JADX INFO: renamed from: com.android.server.notification.ManagedServices$1, reason: invalid class name */
    class AnonymousClass1 implements android.content.ServiceConnection {
        android.os.IInterface mService;
        final /* synthetic */ boolean val$isSystem;
        final /* synthetic */ android.util.Pair val$servicesBindingTag;
        final /* synthetic */ int val$targetSdkVersion;
        final /* synthetic */ int val$uid;
        final /* synthetic */ int val$userid;

        AnonymousClass1(int i, android.util.Pair pair, boolean z, int i2, int i3) {
            this.val$userid = i;
            this.val$servicesBindingTag = pair;
            this.val$isSystem = z;
            this.val$targetSdkVersion = i2;
            this.val$uid = i3;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder binder) {
            android.util.Slog.v(com.android.server.notification.ManagedServices.this.TAG, this.val$userid + " " + com.android.server.notification.ManagedServices.this.getCaption() + " service connected: " + name);
            boolean added = false;
            com.android.server.notification.ManagedServices.ManagedServiceInfo info = null;
            synchronized (com.android.server.notification.ManagedServices.this.mMutex) {
                com.android.server.notification.ManagedServices.this.mServicesRebinding.remove(this.val$servicesBindingTag);
                try {
                    this.mService = com.android.server.notification.ManagedServices.this.asInterface(binder);
                    info = com.android.server.notification.ManagedServices.this.newServiceInfo(this.mService, name, this.val$userid, this.val$isSystem, this, this.val$targetSdkVersion, this.val$uid);
                    binder.linkToDeath(info, 0);
                    added = com.android.server.notification.ManagedServices.this.mServices.add(info);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.notification.ManagedServices.this.TAG, "Failed to linkToDeath, already dead", e);
                }
            }
            if (added) {
                com.android.server.notification.ManagedServices.this.onServiceAdded(info);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.util.Slog.v(com.android.server.notification.ManagedServices.this.TAG, this.val$userid + " " + com.android.server.notification.ManagedServices.this.getCaption() + " connection lost: " + name);
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(final android.content.ComponentName name) {
            android.util.Slog.w(com.android.server.notification.ManagedServices.this.TAG, this.val$userid + " " + com.android.server.notification.ManagedServices.this.getCaption() + " binding died: " + name);
            synchronized (com.android.server.notification.ManagedServices.this.mMutex) {
                com.android.server.notification.ManagedServices.this.unbindService(this, name, this.val$userid);
                if (!com.android.server.notification.ManagedServices.this.mServicesRebinding.contains(this.val$servicesBindingTag)) {
                    com.android.server.notification.ManagedServices.this.mServicesRebinding.add(this.val$servicesBindingTag);
                    android.os.Handler handler = com.android.server.notification.ManagedServices.this.mHandler;
                    final int i = this.val$userid;
                    handler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.notification.ManagedServices$1$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onBindingDied$0(name, i);
                        }
                    }, 10000L);
                } else {
                    android.util.Slog.v(com.android.server.notification.ManagedServices.this.TAG, com.android.server.notification.ManagedServices.this.getCaption() + " not rebinding in user " + this.val$userid + " as a previous rebind attempt was made: " + name);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindingDied$0(android.content.ComponentName name, int userid) {
            com.android.server.notification.ManagedServices.this.reregisterService(name, userid);
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(android.content.ComponentName name) {
            android.util.Slog.v(com.android.server.notification.ManagedServices.this.TAG, "onNullBinding() called with: name = [" + name + "]");
            com.android.server.notification.ManagedServices.this.mContext.unbindService(this);
        }
    }

    boolean isBound(android.content.ComponentName cn, int userId) {
        boolean zContains;
        android.util.Pair<android.content.ComponentName, java.lang.Integer> servicesBindingTag = android.util.Pair.create(cn, java.lang.Integer.valueOf(userId));
        synchronized (this.mMutex) {
            zContains = this.mServicesBound.contains(servicesBindingTag);
        }
        return zContains;
    }

    protected boolean isBoundOrRebinding(android.content.ComponentName cn, int userId) {
        boolean z;
        synchronized (this.mMutex) {
            z = isBound(cn, userId) || this.mServicesRebinding.contains(android.util.Pair.create(cn, java.lang.Integer.valueOf(userId)));
        }
        return z;
    }

    private void unregisterService(android.content.ComponentName name, int userid) {
        synchronized (this.mMutex) {
            unregisterServiceLocked(name, userid);
        }
    }

    private void unregisterServiceLocked(android.content.ComponentName name, int userid) {
        int N = this.mServices.size();
        for (int i = N - 1; i >= 0; i--) {
            com.android.server.notification.ManagedServices.ManagedServiceInfo info = this.mServices.get(i);
            if (name.equals(info.component) && info.userid == userid) {
                removeServiceLocked(i);
                if (info.connection != null) {
                    unbindService(info.connection, info.component, info.userid);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.notification.ManagedServices.ManagedServiceInfo removeServiceImpl(android.os.IInterface service, int userid) {
        if (this.DEBUG) {
            android.util.Slog.d(this.TAG, "removeServiceImpl service=" + service + " u=" + userid);
        }
        com.android.server.notification.ManagedServices.ManagedServiceInfo serviceInfo = null;
        synchronized (this.mMutex) {
            int N = this.mServices.size();
            for (int i = N - 1; i >= 0; i--) {
                com.android.server.notification.ManagedServices.ManagedServiceInfo info = this.mServices.get(i);
                if (info.service.asBinder() == service.asBinder() && info.userid == userid) {
                    android.util.Slog.d(this.TAG, "Removing active service " + info.component);
                    serviceInfo = removeServiceLocked(i);
                }
            }
        }
        return serviceInfo;
    }

    private com.android.server.notification.ManagedServices.ManagedServiceInfo removeServiceLocked(int i) {
        com.android.server.notification.ManagedServices.ManagedServiceInfo info = this.mServices.remove(i);
        onServiceRemovedLocked(info);
        return info;
    }

    private void checkNotNull(android.os.IInterface service) {
        if (service == null) {
            throw new java.lang.IllegalArgumentException(getCaption() + " must not be null");
        }
    }

    private com.android.server.notification.ManagedServices.ManagedServiceInfo registerServiceImpl(android.os.IInterface service, android.content.ComponentName component, int userid, int targetSdk, int uid) {
        com.android.server.notification.ManagedServices.ManagedServiceInfo info = newServiceInfo(service, component, userid, true, null, targetSdk, uid);
        return registerServiceImpl(info);
    }

    private com.android.server.notification.ManagedServices.ManagedServiceInfo registerServiceImpl(com.android.server.notification.ManagedServices.ManagedServiceInfo info) {
        synchronized (this.mMutex) {
            try {
                try {
                    info.service.asBinder().linkToDeath(info, 0);
                    this.mServices.add(info);
                } catch (android.os.RemoteException e) {
                    return null;
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return info;
    }

    private void unregisterServiceImpl(android.os.IInterface service, int userid) {
        com.android.server.notification.ManagedServices.ManagedServiceInfo info = removeServiceImpl(service, userid);
        if (info != null && info.connection != null && !info.isGuest(this)) {
            unbindService(info.connection, info.component, info.userid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindService(android.content.ServiceConnection connection, android.content.ComponentName component, int userId) {
        try {
            this.mContext.unbindService(connection);
        } catch (java.lang.IllegalArgumentException e) {
            android.util.Slog.e(this.TAG, getCaption() + " " + component + " could not be unbound", e);
        }
        synchronized (this.mMutex) {
            this.mServicesBound.remove(android.util.Pair.create(component, java.lang.Integer.valueOf(userId)));
        }
    }

    private android.content.pm.ServiceInfo getServiceInfo(android.content.ComponentName component, int userId) {
        try {
            return this.mPm.getServiceInfo(component, 786560L, userId);
        } catch (android.os.RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    private boolean isAutobindAllowed(android.content.pm.ServiceInfo serviceInfo) {
        if (serviceInfo == null || serviceInfo.metaData == null || !serviceInfo.metaData.containsKey("android.service.notification.default_autobind_listenerservice")) {
            return true;
        }
        return serviceInfo.metaData.getBoolean("android.service.notification.default_autobind_listenerservice", true);
    }

    public class ManagedServiceInfo implements android.os.IBinder.DeathRecipient {
        public android.content.ComponentName component;
        public android.content.ServiceConnection connection;
        public boolean isSystem;
        public boolean isSystemUi;
        public android.util.Pair<android.content.ComponentName, java.lang.Integer> mKey;
        public android.os.IInterface service;
        public int targetSdkVersion;
        public int uid;
        public int userid;

        public ManagedServiceInfo(android.os.IInterface service, android.content.ComponentName component, int userid, boolean isSystem, android.content.ServiceConnection connection, int targetSdkVersion, int uid) {
            this.service = service;
            this.component = component;
            this.userid = userid;
            this.isSystem = isSystem;
            this.connection = connection;
            this.targetSdkVersion = targetSdkVersion;
            this.uid = uid;
            this.mKey = android.util.Pair.create(component, java.lang.Integer.valueOf(userid));
        }

        public boolean isGuest(com.android.server.notification.ManagedServices host) {
            return com.android.server.notification.ManagedServices.this != host;
        }

        public com.android.server.notification.ManagedServices getOwner() {
            return com.android.server.notification.ManagedServices.this;
        }

        public android.os.IInterface getService() {
            return this.service;
        }

        public boolean isSystem() {
            return this.isSystem;
        }

        public boolean isSystemUi() {
            return this.isSystemUi;
        }

        public java.lang.String toString() {
            return "ManagedServiceInfo[component=" + this.component + ",userid=" + this.userid + ",isSystem=" + this.isSystem + ",targetSdkVersion=" + this.targetSdkVersion + ",connection=" + (this.connection == null ? null : "<connection>") + ",service=" + this.service + ']';
        }

        public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId, com.android.server.notification.ManagedServices host) {
            long token = proto.start(fieldId);
            this.component.dumpDebug(proto, 1146756268033L);
            proto.write(1120986464258L, this.userid);
            proto.write(1138166333443L, this.service.getClass().getName());
            proto.write(1133871366148L, this.isSystem);
            proto.write(1133871366149L, isGuest(host));
            proto.end(token);
        }

        public boolean isSameUser(int userId) {
            if (isEnabledForCurrentProfiles()) {
                return userId == -1 || userId == this.userid;
            }
            return false;
        }

        public boolean enabledAndUserMatches(int nid) {
            if (!isEnabledForCurrentProfiles()) {
                return false;
            }
            if (this.userid == -1 || this.isSystem || nid == -1 || nid == this.userid) {
                return true;
            }
            return supportsProfiles() && com.android.server.notification.ManagedServices.this.mUserProfiles.isCurrentProfile(nid) && isPermittedForProfile(nid);
        }

        public boolean supportsProfiles() {
            return this.targetSdkVersion >= 21;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.notification.ManagedServices.this.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ManagedServices.this.TAG, "binderDied");
            }
            com.android.server.notification.ManagedServices.this.removeServiceImpl(this.service, this.userid);
        }

        public boolean isEnabledForCurrentProfiles() {
            boolean zContains;
            if (this.isSystem) {
                return true;
            }
            if (this.connection == null) {
                return false;
            }
            synchronized (com.android.server.notification.ManagedServices.this.mMutex) {
                zContains = com.android.server.notification.ManagedServices.this.mEnabledServicesForCurrentProfiles.contains(this.component);
            }
            return zContains;
        }

        public boolean isPermittedForProfile(int userId) {
            if (!com.android.server.notification.ManagedServices.this.mUserProfiles.isProfileUser(userId, com.android.server.notification.ManagedServices.this.mContext)) {
                return true;
            }
            android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) com.android.server.notification.ManagedServices.this.mContext.getSystemService("device_policy");
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return dpm.isNotificationListenerServicePermitted(this.component.getPackageName(), userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.notification.ManagedServices.ManagedServiceInfo that = (com.android.server.notification.ManagedServices.ManagedServiceInfo) o;
            if (this.userid == that.userid && this.isSystem == that.isSystem && this.targetSdkVersion == that.targetSdkVersion && java.util.Objects.equals(this.service, that.service) && java.util.Objects.equals(this.component, that.component) && java.util.Objects.equals(this.connection, that.connection)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.service, this.component, java.lang.Integer.valueOf(this.userid), java.lang.Boolean.valueOf(this.isSystem), this.connection, java.lang.Integer.valueOf(this.targetSdkVersion));
        }
    }

    public boolean isComponentEnabledForCurrentProfiles(android.content.ComponentName component) {
        boolean zContains;
        synchronized (this.mMutex) {
            zContains = this.mEnabledServicesForCurrentProfiles.contains(component);
        }
        return zContains;
    }

    public static class UserProfiles {
        private final android.util.SparseArray<android.content.pm.UserInfo> mCurrentProfiles = new android.util.SparseArray<>();

        public void updateCache(android.content.Context context) {
            android.os.UserManager userManager = (android.os.UserManager) context.getSystemService(com.android.server.notification.ManagedServices.ATT_USER_ID);
            if (userManager != null) {
                int currentUserId = android.app.ActivityManager.getCurrentUser();
                java.util.List<android.content.pm.UserInfo> profiles = userManager.getProfiles(currentUserId);
                synchronized (this.mCurrentProfiles) {
                    this.mCurrentProfiles.clear();
                    for (android.content.pm.UserInfo user : profiles) {
                        this.mCurrentProfiles.put(user.id, user);
                    }
                }
            }
        }

        public android.util.IntArray getCurrentProfileIds() {
            android.util.IntArray users;
            synchronized (this.mCurrentProfiles) {
                users = new android.util.IntArray(this.mCurrentProfiles.size());
                int N = this.mCurrentProfiles.size();
                for (int i = 0; i < N; i++) {
                    users.add(this.mCurrentProfiles.keyAt(i));
                }
            }
            return users;
        }

        public boolean isCurrentProfile(int userId) {
            boolean z;
            synchronized (this.mCurrentProfiles) {
                z = this.mCurrentProfiles.get(userId) != null;
            }
            return z;
        }

        public boolean isProfileUser(int userId, android.content.Context context) {
            synchronized (this.mCurrentProfiles) {
                android.content.pm.UserInfo user = this.mCurrentProfiles.get(userId);
                if (user == null) {
                    return false;
                }
                if (com.android.server.notification.NotificationManagerService.privateSpaceFlagsEnabled()) {
                    if (user.isProfile() && hasParent(user, context)) {
                        z = true;
                    }
                    return z;
                }
                return user.isManagedProfile() || user.isCloneProfile();
            }
        }

        boolean hasParent(android.content.pm.UserInfo profile, android.content.Context context) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                android.os.UserManager um = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
                return um.getProfileParent(profile.id) != null;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }
}
