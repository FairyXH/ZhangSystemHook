package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class NetworkScorerAppManager {
    private final android.content.Context mContext;
    private final com.android.server.NetworkScorerAppManager.SettingsFacade mSettingsFacade;
    private static final java.lang.String TAG = "NetworkScorerAppManager";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final boolean VERBOSE = android.util.Log.isLoggable(TAG, 2);

    public NetworkScorerAppManager(android.content.Context context) {
        this(context, new com.android.server.NetworkScorerAppManager.SettingsFacade());
    }

    public NetworkScorerAppManager(android.content.Context context, com.android.server.NetworkScorerAppManager.SettingsFacade settingsFacade) {
        this.mContext = context;
        this.mSettingsFacade = settingsFacade;
    }

    public java.util.List<android.net.NetworkScorerAppData> getAllValidScorers() {
        com.android.server.NetworkScorerAppManager networkScorerAppManager = this;
        if (VERBOSE) {
            android.util.Log.v(TAG, "getAllValidScorers()");
        }
        android.content.pm.PackageManager pm = networkScorerAppManager.mContext.getPackageManager();
        android.content.Intent serviceIntent = new android.content.Intent("android.net.action.RECOMMEND_NETWORKS");
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = pm.queryIntentServices(serviceIntent, 128);
        if (resolveInfos == null || resolveInfos.isEmpty()) {
            if (DEBUG) {
                android.util.Log.d(TAG, "Found 0 Services able to handle " + serviceIntent);
            }
            return java.util.Collections.emptyList();
        }
        java.util.List<android.net.NetworkScorerAppData> appDataList = new java.util.ArrayList<>();
        int i = 0;
        while (i < resolveInfos.size()) {
            android.content.pm.ServiceInfo serviceInfo = resolveInfos.get(i).serviceInfo;
            if (networkScorerAppManager.hasPermissions(serviceInfo.applicationInfo.uid, serviceInfo.packageName)) {
                if (VERBOSE) {
                    android.util.Log.v(TAG, serviceInfo.packageName + " is a valid scorer/recommender.");
                }
                android.content.ComponentName serviceComponentName = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
                java.lang.String serviceLabel = networkScorerAppManager.getRecommendationServiceLabel(serviceInfo, pm);
                android.content.ComponentName useOpenWifiNetworksActivity = networkScorerAppManager.findUseOpenWifiNetworksActivity(serviceInfo);
                java.lang.String networkAvailableNotificationChannelId = getNetworkAvailableNotificationChannelId(serviceInfo);
                appDataList.add(new android.net.NetworkScorerAppData(serviceInfo.applicationInfo.uid, serviceComponentName, serviceLabel, useOpenWifiNetworksActivity, networkAvailableNotificationChannelId));
            } else if (VERBOSE) {
                android.util.Log.v(TAG, serviceInfo.packageName + " is NOT a valid scorer/recommender.");
            }
            i++;
            networkScorerAppManager = this;
        }
        return appDataList;
    }

    private java.lang.String getRecommendationServiceLabel(android.content.pm.ServiceInfo serviceInfo, android.content.pm.PackageManager pm) {
        if (serviceInfo.metaData != null) {
            java.lang.String label = serviceInfo.metaData.getString("android.net.scoring.recommendation_service_label");
            if (!android.text.TextUtils.isEmpty(label)) {
                return label;
            }
        }
        java.lang.CharSequence label2 = serviceInfo.loadLabel(pm);
        if (label2 == null) {
            return null;
        }
        return label2.toString();
    }

    private android.content.ComponentName findUseOpenWifiNetworksActivity(android.content.pm.ServiceInfo serviceInfo) {
        if (serviceInfo.metaData == null) {
            if (DEBUG) {
                android.util.Log.d(TAG, "No metadata found on " + serviceInfo.getComponentName());
            }
            return null;
        }
        java.lang.String useOpenWifiPackage = serviceInfo.metaData.getString("android.net.wifi.use_open_wifi_package");
        if (android.text.TextUtils.isEmpty(useOpenWifiPackage)) {
            if (DEBUG) {
                android.util.Log.d(TAG, "No use_open_wifi_package metadata found on " + serviceInfo.getComponentName());
            }
            return null;
        }
        android.content.Intent enableUseOpenWifiIntent = new android.content.Intent("android.net.scoring.CUSTOM_ENABLE").setPackage(useOpenWifiPackage);
        android.content.pm.ResolveInfo resolveActivityInfo = this.mContext.getPackageManager().resolveActivity(enableUseOpenWifiIntent, 0);
        if (VERBOSE) {
            android.util.Log.d(TAG, "Resolved " + enableUseOpenWifiIntent + " to " + resolveActivityInfo);
        }
        if (resolveActivityInfo == null || resolveActivityInfo.activityInfo == null) {
            return null;
        }
        return resolveActivityInfo.activityInfo.getComponentName();
    }

    private static java.lang.String getNetworkAvailableNotificationChannelId(android.content.pm.ServiceInfo serviceInfo) {
        if (serviceInfo.metaData == null) {
            if (DEBUG) {
                android.util.Log.d(TAG, "No metadata found on " + serviceInfo.getComponentName());
                return null;
            }
            return null;
        }
        return serviceInfo.metaData.getString("android.net.wifi.notification_channel_id_network_available");
    }

    public android.net.NetworkScorerAppData getActiveScorer() {
        int enabledSetting = getNetworkRecommendationsEnabledSetting();
        if (enabledSetting == -1) {
            return null;
        }
        return getScorer(getNetworkRecommendationsPackage());
    }

    private android.net.NetworkScorerAppData getScorer(java.lang.String packageName) {
        if (android.text.TextUtils.isEmpty(packageName)) {
            return null;
        }
        java.util.List<android.net.NetworkScorerAppData> apps = getAllValidScorers();
        for (int i = 0; i < apps.size(); i++) {
            android.net.NetworkScorerAppData app = apps.get(i);
            if (app.getRecommendationServicePackageName().equals(packageName)) {
                return app;
            }
        }
        return null;
    }

    private boolean hasPermissions(int uid, java.lang.String packageName) {
        return hasScoreNetworksPermission(packageName) && canAccessLocation(uid, packageName);
    }

    private boolean hasScoreNetworksPermission(java.lang.String packageName) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        return pm.checkPermission("android.permission.SCORE_NETWORKS", packageName) == 0;
    }

    private boolean canAccessLocation(int uid, java.lang.String packageName) {
        return isLocationModeEnabled() && android.content.PermissionChecker.checkPermissionForPreflight(this.mContext, "android.permission.ACCESS_COARSE_LOCATION", -1, uid, packageName) == 0;
    }

    private boolean isLocationModeEnabled() {
        return this.mSettingsFacade.getSecureInt(this.mContext, "location_mode", 0) != 0;
    }

    public boolean setActiveScorer(java.lang.String packageName) {
        java.lang.String oldPackageName = getNetworkRecommendationsPackage();
        if (android.text.TextUtils.equals(oldPackageName, packageName)) {
            return true;
        }
        if (android.text.TextUtils.isEmpty(packageName)) {
            android.util.Log.i(TAG, "Network scorer forced off, was: " + oldPackageName);
            setNetworkRecommendationsPackage(null);
            setNetworkRecommendationsEnabledSetting(-1);
            return true;
        }
        if (getScorer(packageName) != null) {
            android.util.Log.i(TAG, "Changing network scorer from " + oldPackageName + " to " + packageName);
            setNetworkRecommendationsPackage(packageName);
            setNetworkRecommendationsEnabledSetting(1);
            return true;
        }
        android.util.Log.w(TAG, "Requested network scorer is not valid: " + packageName);
        return false;
    }

    public void updateState() {
        int enabledSetting = getNetworkRecommendationsEnabledSetting();
        if (enabledSetting == -1) {
            if (DEBUG) {
                android.util.Log.d(TAG, "Recommendations forced off.");
                return;
            }
            return;
        }
        java.lang.String currentPackageName = getNetworkRecommendationsPackage();
        if (getScorer(currentPackageName) != null) {
            if (VERBOSE) {
                android.util.Log.v(TAG, currentPackageName + " is the active scorer.");
            }
            setNetworkRecommendationsEnabledSetting(1);
            return;
        }
        int newEnabledSetting = 0;
        java.lang.String defaultPackageName = getDefaultPackageSetting();
        if (!android.text.TextUtils.equals(currentPackageName, defaultPackageName) && getScorer(defaultPackageName) != null) {
            if (DEBUG) {
                android.util.Log.d(TAG, "Defaulting the network recommendations app to: " + defaultPackageName);
            }
            setNetworkRecommendationsPackage(defaultPackageName);
            newEnabledSetting = 1;
        }
        setNetworkRecommendationsEnabledSetting(newEnabledSetting);
    }

    public void migrateNetworkScorerAppSettingIfNeeded() {
        android.net.NetworkScorerAppData currentAppData;
        java.lang.String scorerAppPkgNameSetting = this.mSettingsFacade.getString(this.mContext, "network_scorer_app");
        if (android.text.TextUtils.isEmpty(scorerAppPkgNameSetting) || (currentAppData = getActiveScorer()) == null) {
            return;
        }
        if (DEBUG) {
            android.util.Log.d(TAG, "Migrating Settings.Global.NETWORK_SCORER_APP (" + scorerAppPkgNameSetting + ")...");
        }
        android.content.ComponentName enableUseOpenWifiActivity = currentAppData.getEnableUseOpenWifiActivity();
        java.lang.String useOpenWifiSetting = this.mSettingsFacade.getString(this.mContext, "use_open_wifi_package");
        if (android.text.TextUtils.isEmpty(useOpenWifiSetting) && enableUseOpenWifiActivity != null && scorerAppPkgNameSetting.equals(enableUseOpenWifiActivity.getPackageName())) {
            this.mSettingsFacade.putString(this.mContext, "use_open_wifi_package", scorerAppPkgNameSetting);
            if (DEBUG) {
                android.util.Log.d(TAG, "Settings.Global.USE_OPEN_WIFI_PACKAGE set to '" + scorerAppPkgNameSetting + "'.");
            }
        }
        this.mSettingsFacade.putString(this.mContext, "network_scorer_app", null);
        if (DEBUG) {
            android.util.Log.d(TAG, "Settings.Global.NETWORK_SCORER_APP migration complete.");
            java.lang.String setting = this.mSettingsFacade.getString(this.mContext, "use_open_wifi_package");
            android.util.Log.d(TAG, "Settings.Global.USE_OPEN_WIFI_PACKAGE is: '" + setting + "'.");
        }
    }

    private java.lang.String getDefaultPackageSetting() {
        return this.mContext.getResources().getString(android.R.string.config_defaultShutdownVibrationFile);
    }

    private java.lang.String getNetworkRecommendationsPackage() {
        return this.mSettingsFacade.getString(this.mContext, "network_recommendations_package");
    }

    private void setNetworkRecommendationsPackage(java.lang.String packageName) {
        this.mSettingsFacade.putString(this.mContext, "network_recommendations_package", packageName);
        if (VERBOSE) {
            android.util.Log.d(TAG, "network_recommendations_package set to " + packageName);
        }
    }

    private int getNetworkRecommendationsEnabledSetting() {
        return this.mSettingsFacade.getInt(this.mContext, "network_recommendations_enabled", 0);
    }

    private void setNetworkRecommendationsEnabledSetting(int value) {
        this.mSettingsFacade.putInt(this.mContext, "network_recommendations_enabled", value);
        if (VERBOSE) {
            android.util.Log.d(TAG, "network_recommendations_enabled set to " + value);
        }
    }

    public static class SettingsFacade {
        public boolean putString(android.content.Context context, java.lang.String name, java.lang.String value) {
            return android.provider.Settings.Global.putString(context.getContentResolver(), name, value);
        }

        public java.lang.String getString(android.content.Context context, java.lang.String name) {
            return android.provider.Settings.Global.getString(context.getContentResolver(), name);
        }

        public boolean putInt(android.content.Context context, java.lang.String name, int value) {
            return android.provider.Settings.Global.putInt(context.getContentResolver(), name, value);
        }

        public int getInt(android.content.Context context, java.lang.String name, int defaultValue) {
            return android.provider.Settings.Global.getInt(context.getContentResolver(), name, defaultValue);
        }

        public int getSecureInt(android.content.Context context, java.lang.String name, int defaultValue) {
            android.content.ContentResolver cr = context.getContentResolver();
            return android.provider.Settings.Secure.getIntForUser(cr, name, defaultValue, cr.getUserId());
        }
    }
}
