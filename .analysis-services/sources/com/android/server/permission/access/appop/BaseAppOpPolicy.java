package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: BaseAppOpPolicy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001c\u0010\t\u001a\u00020\n*\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u001c\u0010\u0010\u001a\u00020\n*\u00020\u00112\u0006\u0010\f\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000fH\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/android/server/permission/access/appop/BaseAppOpPolicy;", "Lcom/android/server/permission/access/SchemePolicy;", "persistence", "Lcom/android/server/permission/access/appop/BaseAppOpPersistence;", "(Lcom/android/server/permission/access/appop/BaseAppOpPersistence;)V", "objectScheme", "", "getObjectScheme", "()Ljava/lang/String;", "parseUserState", "", "Lcom/android/modules/utils/BinaryXmlPullParser;", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "", "serializeUserState", "Lcom/android/modules/utils/BinaryXmlSerializer;", "Lcom/android/server/permission/access/AccessState;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class BaseAppOpPolicy extends com.android.server.permission.access.SchemePolicy {
    private final com.android.server.permission.access.appop.BaseAppOpPersistence persistence;

    public BaseAppOpPolicy(com.android.server.permission.access.appop.BaseAppOpPersistence persistence) {
        this.persistence = persistence;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public java.lang.String getObjectScheme() {
        return com.android.server.permission.access.AppOpUri.SCHEME;
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void parseUserState(com.android.modules.utils.BinaryXmlPullParser $this$parseUserState, com.android.server.permission.access.MutableAccessState state, int userId) {
        com.android.server.permission.access.appop.BaseAppOpPersistence $this$parseUserState_u24lambda_u240 = this.persistence;
        $this$parseUserState_u24lambda_u240.parseUserState($this$parseUserState, state, userId);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void serializeUserState(com.android.modules.utils.BinaryXmlSerializer $this$serializeUserState, com.android.server.permission.access.AccessState state, int userId) {
        com.android.server.permission.access.appop.BaseAppOpPersistence $this$serializeUserState_u24lambda_u241 = this.persistence;
        $this$serializeUserState_u24lambda_u241.serializeUserState($this$serializeUserState, state, userId);
    }
}
