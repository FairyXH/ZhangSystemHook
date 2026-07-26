package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public interface IAppRestrictionControllerWrapper {
    default com.android.server.am.IAppRestrictionControllerExt.IStaticExt getStaticExtImpl() {
        return new com.android.server.am.IAppRestrictionControllerExt.IStaticExt() { // from class: com.android.server.am.IAppRestrictionControllerWrapper.1
        };
    }

    default com.android.server.am.AppRestrictionController.Injector getInjector() {
        return null;
    }

    default java.lang.Object getSettingsLock() {
        return null;
    }
}
