package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public interface IOplusDisplayManagerServiceEx extends com.android.server.IOplusCommonManagerServiceEx {
    public static final com.android.server.display.IOplusDisplayManagerServiceEx DEFAULT = new com.android.server.display.IOplusDisplayManagerServiceEx() { // from class: com.android.server.display.IOplusDisplayManagerServiceEx.1
    };
    public static final java.lang.String NAME = "IOplusDisplayManagerServiceEx";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusDisplayManagerServiceEx;
    }

    default com.android.server.display.IOplusDisplayManagerServiceEx getDefault() {
        return DEFAULT;
    }

    default com.android.server.display.DisplayManagerService getDisplayManagerService() {
        return null;
    }
}
