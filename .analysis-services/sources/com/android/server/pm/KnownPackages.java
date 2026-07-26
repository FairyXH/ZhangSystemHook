package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class KnownPackages {
    public static final int LAST_KNOWN_PACKAGE = 19;
    public static final int PACKAGE_AMBIENT_CONTEXT_DETECTION = 18;
    public static final int PACKAGE_APP_PREDICTOR = 12;
    public static final int PACKAGE_BROWSER = 5;
    public static final int PACKAGE_COMPANION = 15;
    public static final int PACKAGE_CONFIGURATOR = 10;
    public static final int PACKAGE_DOCUMENTER = 9;
    public static final int PACKAGE_INCIDENT_REPORT_APPROVER = 11;
    public static final int PACKAGE_INSTALLER = 2;
    public static final int PACKAGE_OVERLAY_CONFIG_SIGNATURE = 13;
    public static final int PACKAGE_PERMISSION_CONTROLLER = 7;
    public static final int PACKAGE_RECENTS = 17;
    public static final int PACKAGE_RETAIL_DEMO = 16;
    public static final int PACKAGE_SETUP_WIZARD = 1;
    public static final int PACKAGE_SYSTEM = 0;
    public static final int PACKAGE_SYSTEM_TEXT_CLASSIFIER = 6;
    public static final int PACKAGE_UNINSTALLER = 3;
    public static final int PACKAGE_VERIFIER = 4;
    public static final int PACKAGE_WEARABLE_SENSING = 19;
    public static final int PACKAGE_WELLBEING = 8;
    public static final int PACKAGE_WIFI = 14;
    static final java.lang.String SYSTEM_PACKAGE_NAME = "android";
    private final java.lang.String mAmbientContextDetectionPackage;
    private final java.lang.String mAppPredictionServicePackage;
    private final java.lang.String mCompanionPackage;
    private final java.lang.String mConfiguratorPackage;
    private final com.android.server.pm.DefaultAppProvider mDefaultAppProvider;
    private final java.lang.String mDefaultTextClassifierPackage;
    private final java.lang.String mIncidentReportApproverPackage;
    private final java.lang.String mOverlayConfigSignaturePackage;
    private final java.lang.String mRecentsPackage;
    private final java.lang.String mRequiredInstallerPackage;
    private final java.lang.String mRequiredPermissionControllerPackage;
    private final java.lang.String mRequiredUninstallerPackage;
    private final java.lang.String[] mRequiredVerifierPackages;
    private final java.lang.String mRetailDemoPackage;
    private final java.lang.String mSetupWizardPackage;
    private final java.lang.String mSystemTextClassifierPackageName;
    private final java.lang.String mWearableSensingPackage;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface KnownPackage {
    }

    KnownPackages(com.android.server.pm.DefaultAppProvider defaultAppProvider, java.lang.String requiredInstallerPackage, java.lang.String requiredUninstallerPackage, java.lang.String setupWizardPackage, java.lang.String[] requiredVerifierPackages, java.lang.String defaultTextClassifierPackage, java.lang.String systemTextClassifierPackageName, java.lang.String requiredPermissionControllerPackage, java.lang.String configuratorPackage, java.lang.String incidentReportApproverPackage, java.lang.String ambientContextDetectionPackage, java.lang.String wearableSensingPackage, java.lang.String appPredictionServicePackage, java.lang.String companionPackageName, java.lang.String retailDemoPackage, java.lang.String overlayConfigSignaturePackage, java.lang.String recentsPackage) {
        this.mDefaultAppProvider = defaultAppProvider;
        this.mRequiredInstallerPackage = requiredInstallerPackage;
        this.mRequiredUninstallerPackage = requiredUninstallerPackage;
        this.mSetupWizardPackage = setupWizardPackage;
        this.mRequiredVerifierPackages = requiredVerifierPackages;
        this.mDefaultTextClassifierPackage = defaultTextClassifierPackage;
        this.mSystemTextClassifierPackageName = systemTextClassifierPackageName;
        this.mRequiredPermissionControllerPackage = requiredPermissionControllerPackage;
        this.mConfiguratorPackage = configuratorPackage;
        this.mIncidentReportApproverPackage = incidentReportApproverPackage;
        this.mAmbientContextDetectionPackage = ambientContextDetectionPackage;
        this.mWearableSensingPackage = wearableSensingPackage;
        this.mAppPredictionServicePackage = appPredictionServicePackage;
        this.mCompanionPackage = companionPackageName;
        this.mRetailDemoPackage = retailDemoPackage;
        this.mOverlayConfigSignaturePackage = overlayConfigSignaturePackage;
        this.mRecentsPackage = recentsPackage;
    }

    static java.lang.String knownPackageToString(int knownPackage) {
        switch (knownPackage) {
            case 0:
                return "System";
            case 1:
                return "Setup Wizard";
            case 2:
                return "Installer";
            case 3:
                return "Uninstaller";
            case 4:
                return "Verifier";
            case 5:
                return "Browser";
            case 6:
                return "System Text Classifier";
            case 7:
                return "Permission Controller";
            case 8:
                return "Wellbeing";
            case 9:
                return "Documenter";
            case 10:
                return "Configurator";
            case 11:
                return "Incident Report Approver";
            case 12:
                return "App Predictor";
            case 13:
                return "Overlay Config Signature";
            case 14:
                return "Wi-Fi";
            case 15:
                return "Companion";
            case 16:
                return "Retail Demo";
            case 17:
                return "Recents";
            case 18:
                return "Ambient Context Detection";
            case 19:
                return "Wearable sensing";
            default:
                return "Unknown";
        }
    }

    java.lang.String[] getKnownPackageNames(com.android.server.pm.Computer snapshot, int knownPackage, int userId) {
        switch (knownPackage) {
            case 0:
                return new java.lang.String[]{"android"};
            case 1:
                return snapshot.filterOnlySystemPackages(this.mSetupWizardPackage);
            case 2:
                return snapshot.filterOnlySystemPackages(this.mRequiredInstallerPackage);
            case 3:
                return snapshot.filterOnlySystemPackages(this.mRequiredUninstallerPackage);
            case 4:
                return snapshot.filterOnlySystemPackages(this.mRequiredVerifierPackages);
            case 5:
                return new java.lang.String[]{this.mDefaultAppProvider.getDefaultBrowser(userId)};
            case 6:
                return snapshot.filterOnlySystemPackages(this.mDefaultTextClassifierPackage, this.mSystemTextClassifierPackageName);
            case 7:
                return snapshot.filterOnlySystemPackages(this.mRequiredPermissionControllerPackage);
            case 8:
            case 9:
            case 14:
            default:
                return (java.lang.String[]) com.android.internal.util.ArrayUtils.emptyArray(java.lang.String.class);
            case 10:
                return snapshot.filterOnlySystemPackages(this.mConfiguratorPackage);
            case 11:
                return snapshot.filterOnlySystemPackages(this.mIncidentReportApproverPackage);
            case 12:
                return snapshot.filterOnlySystemPackages(this.mAppPredictionServicePackage);
            case 13:
                return snapshot.filterOnlySystemPackages(this.mOverlayConfigSignaturePackage);
            case 15:
                return snapshot.filterOnlySystemPackages(this.mCompanionPackage);
            case 16:
                if (android.text.TextUtils.isEmpty(this.mRetailDemoPackage)) {
                    return (java.lang.String[]) com.android.internal.util.ArrayUtils.emptyArray(java.lang.String.class);
                }
                return new java.lang.String[]{this.mRetailDemoPackage};
            case 17:
                return snapshot.filterOnlySystemPackages(this.mRecentsPackage);
            case 18:
                return snapshot.filterOnlySystemPackages(this.mAmbientContextDetectionPackage);
            case 19:
                return snapshot.filterOnlySystemPackages(this.mWearableSensingPackage);
        }
    }
}
