package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerServiceInjector {
    private final com.android.server.pm.PackageAbiHelper mAbiHelper;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.ApexManager> mApexManagerProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.AppsFilterImpl> mAppsFilterProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.dex.ArtManagerService> mArtManagerServiceProducer;
    private final java.util.concurrent.Executor mBackgroundExecutor;
    private final android.os.Handler mBackgroundHandler;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.resolution.ComponentResolver> mComponentResolverProducer;
    private final android.content.Context mContext;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.CrossProfileIntentFilterHelper> mCrossProfileIntentFilterHelperProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.DefaultAppProvider> mDefaultAppProviderProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.dex.DexManager> mDexManagerProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<android.util.DisplayMetrics> mDisplayMetricsProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.verify.domain.DomainVerificationManagerInternal> mDomainVerificationManagerInternalProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.dex.DynamicCodeLogger> mDynamicCodeLoggerProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.ServiceProducer mGetLocalServiceProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.ServiceProducer mGetSystemServiceProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<android.os.Handler> mHandlerProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<android.app.backup.IBackupManager> mIBackupManager;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<android.os.incremental.IncrementalManager> mIncrementalManagerProducer;
    private final com.android.server.pm.PackageManagerTracedLock mInstallLock;
    private final com.android.server.pm.Installer mInstaller;
    private final com.android.server.pm.PackageManagerServiceInjector.ProducerWithArgument<com.android.server.pm.InstantAppResolverConnection, android.content.ComponentName> mInstantAppResolverConnectionProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.permission.LegacyPermissionManagerInternal> mLegacyPermissionManagerInternalProducer;
    private final com.android.server.pm.PackageManagerTracedLock mLock;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.ModuleInfoProvider> mModuleInfoProviderProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.PackageDexOptimizer> mPackageDexOptimizerProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.PackageInstallerService> mPackageInstallerServiceProducer;
    private com.android.server.pm.PackageManagerService mPackageManager;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.PackageMonitorCallbackHelper> mPackageMonitorCallbackHelper;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.permission.PermissionManagerServiceInternal> mPermissionManagerServiceProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.compat.PlatformCompat> mPlatformCompatProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.internal.pm.parsing.PackageParser2> mPreparingPackageParserProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.internal.pm.parsing.PackageParser2> mScanningCachingPackageParserProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.internal.pm.parsing.PackageParser2> mScanningPackageParserProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.Settings> mSettingsProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.SharedLibrariesImpl> mSharedLibrariesProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.SystemConfig> mSystemConfigProducer;
    private final java.util.List<com.android.server.pm.ScanPartition> mSystemPartitions;
    private final com.android.server.pm.PackageManagerServiceInjector.SystemWrapper mSystemWrapper;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.UpdateOwnershipHelper> mUpdateOwnershipHelperProducer;
    private final com.android.server.pm.PackageManagerServiceInjector.Singleton<com.android.server.pm.UserManagerService> mUserManagerProducer;

    interface Producer<T> {
        T produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService);
    }

    interface ProducerWithArgument<T, R> {
        T produce(com.android.server.pm.PackageManagerServiceInjector packageManagerServiceInjector, com.android.server.pm.PackageManagerService packageManagerService, R r);
    }

    interface ServiceProducer {
        <T> T produce(java.lang.Class<T> cls);
    }

    public interface SystemWrapper {
        void disablePackageCaches();

        void enablePackageCaches();
    }

    static class Singleton<T> {
        private volatile T mInstance = null;
        private final com.android.server.pm.PackageManagerServiceInjector.Producer<T> mProducer;

        Singleton(com.android.server.pm.PackageManagerServiceInjector.Producer<T> producer) {
            this.mProducer = producer;
        }

        T get(com.android.server.pm.PackageManagerServiceInjector injector, com.android.server.pm.PackageManagerService packageManagerService) {
            if (this.mInstance == null) {
                this.mInstance = this.mProducer.produce(injector, packageManagerService);
            }
            return this.mInstance;
        }
    }

    PackageManagerServiceInjector(android.content.Context context, com.android.server.pm.PackageManagerTracedLock lock, com.android.server.pm.Installer installer, com.android.server.pm.PackageManagerTracedLock installLock, com.android.server.pm.PackageAbiHelper abiHelper, android.os.Handler backgroundHandler, java.util.List<com.android.server.pm.ScanPartition> systemPartitions, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.resolution.ComponentResolver> componentResolverProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.permission.PermissionManagerServiceInternal> permissionManagerServiceProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.UserManagerService> userManagerProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.Settings> settingsProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.AppsFilterImpl> appsFilterProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.compat.PlatformCompat> platformCompatProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.SystemConfig> systemConfigProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.PackageDexOptimizer> packageDexOptimizerProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.dex.DexManager> dexManagerProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.dex.DynamicCodeLogger> dynamicCodeLoggerProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.dex.ArtManagerService> artManagerServiceProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.ApexManager> apexManagerProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<android.os.incremental.IncrementalManager> incrementalManagerProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.DefaultAppProvider> defaultAppProviderProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<android.util.DisplayMetrics> displayMetricsProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.internal.pm.parsing.PackageParser2> scanningCachingPackageParserProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.internal.pm.parsing.PackageParser2> scanningPackageParserProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.internal.pm.parsing.PackageParser2> preparingPackageParserProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.PackageInstallerService> packageInstallerServiceProducer, com.android.server.pm.PackageManagerServiceInjector.ProducerWithArgument<com.android.server.pm.InstantAppResolverConnection, android.content.ComponentName> instantAppResolverConnectionProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.ModuleInfoProvider> moduleInfoProviderProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.permission.LegacyPermissionManagerInternal> legacyPermissionManagerInternalProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.verify.domain.DomainVerificationManagerInternal> domainVerificationManagerInternalProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<android.os.Handler> handlerProducer, com.android.server.pm.PackageManagerServiceInjector.SystemWrapper systemWrapper, com.android.server.pm.PackageManagerServiceInjector.ServiceProducer getLocalServiceProducer, com.android.server.pm.PackageManagerServiceInjector.ServiceProducer getSystemServiceProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<android.app.backup.IBackupManager> iBackupManager, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.SharedLibrariesImpl> sharedLibrariesProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.CrossProfileIntentFilterHelper> crossProfileIntentFilterHelperProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.UpdateOwnershipHelper> updateOwnershipHelperProducer, com.android.server.pm.PackageManagerServiceInjector.Producer<com.android.server.pm.PackageMonitorCallbackHelper> packageMonitorCallbackHelper) {
        this.mContext = context;
        this.mLock = lock;
        this.mInstaller = installer;
        this.mAbiHelper = abiHelper;
        this.mInstallLock = installLock;
        this.mBackgroundHandler = backgroundHandler;
        this.mBackgroundExecutor = new android.os.HandlerExecutor(backgroundHandler);
        this.mSystemPartitions = systemPartitions;
        this.mComponentResolverProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(componentResolverProducer);
        this.mPermissionManagerServiceProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(permissionManagerServiceProducer);
        this.mUserManagerProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(userManagerProducer);
        this.mSettingsProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(settingsProducer);
        this.mAppsFilterProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(appsFilterProducer);
        this.mPlatformCompatProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(platformCompatProducer);
        this.mSystemConfigProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(systemConfigProducer);
        this.mPackageDexOptimizerProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(packageDexOptimizerProducer);
        this.mDexManagerProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(dexManagerProducer);
        this.mDynamicCodeLoggerProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(dynamicCodeLoggerProducer);
        this.mArtManagerServiceProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(artManagerServiceProducer);
        this.mApexManagerProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(apexManagerProducer);
        this.mIncrementalManagerProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(incrementalManagerProducer);
        this.mDefaultAppProviderProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(defaultAppProviderProducer);
        this.mDisplayMetricsProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(displayMetricsProducer);
        this.mScanningCachingPackageParserProducer = scanningCachingPackageParserProducer;
        this.mScanningPackageParserProducer = scanningPackageParserProducer;
        this.mPreparingPackageParserProducer = preparingPackageParserProducer;
        this.mPackageInstallerServiceProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(packageInstallerServiceProducer);
        this.mInstantAppResolverConnectionProducer = instantAppResolverConnectionProducer;
        this.mModuleInfoProviderProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(moduleInfoProviderProducer);
        this.mLegacyPermissionManagerInternalProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(legacyPermissionManagerInternalProducer);
        this.mSystemWrapper = systemWrapper;
        this.mGetLocalServiceProducer = getLocalServiceProducer;
        this.mGetSystemServiceProducer = getSystemServiceProducer;
        this.mDomainVerificationManagerInternalProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(domainVerificationManagerInternalProducer);
        this.mHandlerProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(handlerProducer);
        this.mIBackupManager = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(iBackupManager);
        this.mSharedLibrariesProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(sharedLibrariesProducer);
        this.mCrossProfileIntentFilterHelperProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(crossProfileIntentFilterHelperProducer);
        this.mUpdateOwnershipHelperProducer = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(updateOwnershipHelperProducer);
        this.mPackageMonitorCallbackHelper = new com.android.server.pm.PackageManagerServiceInjector.Singleton<>(packageMonitorCallbackHelper);
    }

    public void bootstrap(com.android.server.pm.PackageManagerService pm) {
        this.mPackageManager = pm;
    }

    public com.android.server.pm.UserManagerInternal getUserManagerInternal() {
        return getUserManagerService().getInternalForInjectorOnly();
    }

    public com.android.server.pm.PackageAbiHelper getAbiHelper() {
        return this.mAbiHelper;
    }

    public com.android.server.pm.PackageManagerTracedLock getInstallLock() {
        return this.mInstallLock;
    }

    public java.util.List<com.android.server.pm.ScanPartition> getSystemPartitions() {
        return this.mSystemPartitions;
    }

    public com.android.server.pm.UserManagerService getUserManagerService() {
        return this.mUserManagerProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.PackageManagerTracedLock getLock() {
        return this.mLock;
    }

    public com.android.server.pm.CrossProfileIntentFilterHelper getCrossProfileIntentFilterHelper() {
        return this.mCrossProfileIntentFilterHelperProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.Installer getInstaller() {
        return this.mInstaller;
    }

    public com.android.server.pm.resolution.ComponentResolver getComponentResolver() {
        return this.mComponentResolverProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.permission.PermissionManagerServiceInternal getPermissionManagerServiceInternal() {
        return this.mPermissionManagerServiceProducer.get(this, this.mPackageManager);
    }

    public android.content.Context getContext() {
        return this.mContext;
    }

    public com.android.server.pm.Settings getSettings() {
        return this.mSettingsProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.AppsFilterImpl getAppsFilter() {
        return this.mAppsFilterProducer.get(this, this.mPackageManager);
    }

    public com.android.server.compat.PlatformCompat getCompatibility() {
        return this.mPlatformCompatProducer.get(this, this.mPackageManager);
    }

    public com.android.server.SystemConfig getSystemConfig() {
        return this.mSystemConfigProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.PackageDexOptimizer getPackageDexOptimizer() {
        return this.mPackageDexOptimizerProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.dex.DexManager getDexManager() {
        return this.mDexManagerProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.dex.DynamicCodeLogger getDynamicCodeLogger() {
        return this.mDynamicCodeLoggerProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.dex.ArtManagerService getArtManagerService() {
        return this.mArtManagerServiceProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.ApexManager getApexManager() {
        return this.mApexManagerProducer.get(this, this.mPackageManager);
    }

    public android.os.Handler getBackgroundHandler() {
        return this.mBackgroundHandler;
    }

    public java.util.concurrent.Executor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }

    public android.util.DisplayMetrics getDisplayMetrics() {
        return this.mDisplayMetricsProducer.get(this, this.mPackageManager);
    }

    public <T> T getLocalService(java.lang.Class<T> cls) {
        return (T) this.mGetLocalServiceProducer.produce(cls);
    }

    public <T> T getSystemService(java.lang.Class<T> cls) {
        return (T) this.mGetSystemServiceProducer.produce(cls);
    }

    public com.android.server.pm.PackageManagerServiceInjector.SystemWrapper getSystemWrapper() {
        return this.mSystemWrapper;
    }

    public android.os.incremental.IncrementalManager getIncrementalManager() {
        return this.mIncrementalManagerProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.DefaultAppProvider getDefaultAppProvider() {
        return this.mDefaultAppProviderProducer.get(this, this.mPackageManager);
    }

    public com.android.internal.pm.parsing.PackageParser2 getScanningCachingPackageParser() {
        return this.mScanningCachingPackageParserProducer.produce(this, this.mPackageManager);
    }

    public com.android.internal.pm.parsing.PackageParser2 getScanningPackageParser() {
        return this.mScanningPackageParserProducer.produce(this, this.mPackageManager);
    }

    public com.android.internal.pm.parsing.PackageParser2 getPreparingPackageParser() {
        return this.mPreparingPackageParserProducer.produce(this, this.mPackageManager);
    }

    public com.android.server.pm.PackageInstallerService getPackageInstallerService() {
        return this.mPackageInstallerServiceProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.InstantAppResolverConnection getInstantAppResolverConnection(android.content.ComponentName instantAppResolverComponent) {
        return this.mInstantAppResolverConnectionProducer.produce(this, this.mPackageManager, instantAppResolverComponent);
    }

    public com.android.server.pm.ModuleInfoProvider getModuleInfoProvider() {
        return this.mModuleInfoProviderProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.permission.LegacyPermissionManagerInternal getLegacyPermissionManagerInternal() {
        return this.mLegacyPermissionManagerInternalProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.verify.domain.DomainVerificationManagerInternal getDomainVerificationManagerInternal() {
        return this.mDomainVerificationManagerInternalProducer.get(this, this.mPackageManager);
    }

    public android.os.Handler getHandler() {
        return this.mHandlerProducer.get(this, this.mPackageManager);
    }

    public android.app.ActivityManagerInternal getActivityManagerInternal() {
        return (android.app.ActivityManagerInternal) getLocalService(android.app.ActivityManagerInternal.class);
    }

    public android.app.backup.IBackupManager getIBackupManager() {
        return this.mIBackupManager.get(this, this.mPackageManager);
    }

    public com.android.server.pm.SharedLibrariesImpl getSharedLibrariesImpl() {
        return this.mSharedLibrariesProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.UpdateOwnershipHelper getUpdateOwnershipHelper() {
        return this.mUpdateOwnershipHelperProducer.get(this, this.mPackageManager);
    }

    public com.android.server.pm.PackageMonitorCallbackHelper getPackageMonitorCallbackHelper() {
        return this.mPackageMonitorCallbackHelper.get(this, this.mPackageManager);
    }
}
