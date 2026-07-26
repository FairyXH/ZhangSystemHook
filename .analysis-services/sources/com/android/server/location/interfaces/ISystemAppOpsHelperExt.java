package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface ISystemAppOpsHelperExt extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.ISystemAppOpsHelperExt DEFAULT = new com.android.server.location.interfaces.ISystemAppOpsHelperExt() { // from class: com.android.server.location.interfaces.ISystemAppOpsHelperExt.1
    };

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.ISystemAppOpsHelperExt;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default boolean needNoteOp(int appOp, android.location.util.identity.CallerIdentity callerIdentity) {
        return true;
    }

    default void updateNoteStatus(int appOp, android.location.util.identity.CallerIdentity callerIdentity, boolean status) {
    }

    default boolean getNoteOpStatus(int appOp, android.location.util.identity.CallerIdentity callerIdentity) {
        return false;
    }
}
