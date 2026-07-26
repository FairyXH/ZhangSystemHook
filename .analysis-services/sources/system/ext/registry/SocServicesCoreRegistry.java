package system.ext.registry;

/* JADX INFO: loaded from: classes4.dex */
public class SocServicesCoreRegistry {
    static {
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IActivityTaskSupervisorSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda0
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.ActivityTaskSupervisorSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.ISystemServerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda11
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.SystemServerSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.am.IProcessRecordSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda22
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.am.ProcessRecordSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.am.IProcessListSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda24
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.am.ProcessListSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.am.IOomAdjusterSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda25
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.am.OomAdjusterSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.am.IActivityManagerServiceSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda26
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.am.ActivityManagerServiceSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.ISystemGesturesPointerEventListenerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda27
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.SystemGesturesPointerEventListenerSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IDisplayPolicySocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda28
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.DisplayPolicySocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IDisplayRotationSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda29
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.DisplayRotationSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IScreenRotationAnimationSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda30
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.ScreenRotationAnimationSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.pm.IHbtUtilSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda1
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return com.android.server.pm.HbtUtilSocExtImpl.getInstance(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.pm.IPackageInstallerSessionSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda2
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.pm.PackageInstallerSessionSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.pm.IPackageManagerServiceSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda3
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.pm.PackageManagerServiceSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IActivityRecordSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda4
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.ActivityRecordSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IActivityStarterSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda5
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.ActivityStarterSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IActivityMetricsLoggerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda6
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.ActivityMetricsLoggerSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IWindowManagerServiceSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda7
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.WindowManagerServiceSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.policy.IPhoneWindowManagerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda8
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.policy.PhoneWindowManagerSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.ITaskFragmentSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda9
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.TaskFragmentSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IActivityTaskManagerServiceSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda10
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.ActivityTaskManagerServiceSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.am.IProcessErrorStateRecordSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda12
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.am.ProcessErrorStateRecordSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.IWatchdogSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda13
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.WatchdogSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.location.gnss.IGnssLocationProviderSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda14
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.location.gnss.GnssLPSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.display.IWifiDisplayControllerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda15
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.display.WifiDisplayControllerSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.audio.IAudioServiceSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda16
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.audio.AudioServiceSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.audio.IBtHelperSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda17
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.audio.BtHelperSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IRecentTasksSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda18
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.RecentTasksSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IRootWindowContainerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda19
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.RootWindowContainerSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.ITaskTapPointerEventListenerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda20
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.TaskTapPointerEventListenerSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.ITransitionSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda21
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.TransitionSocExtImpl(obj);
            }
        });
        system.ext.loader.core.ExtRegistry.registerExt(com.android.server.wm.IAsyncRotationControllerSocExt.class, new system.ext.loader.core.ExtCreator() { // from class: system.ext.registry.SocServicesCoreRegistry$$ExternalSyntheticLambda23
            public final java.lang.Object createExtWith(java.lang.Object obj) {
                return new com.android.server.wm.AsyncRotationControllerSocExtImpl(obj);
            }
        });
    }
}
