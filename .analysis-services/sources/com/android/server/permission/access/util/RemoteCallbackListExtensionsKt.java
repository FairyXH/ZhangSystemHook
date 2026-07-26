package com.android.server.permission.access.util;

/* JADX INFO: compiled from: RemoteCallbackListExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a1\u0010\u0000\u001a\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\b¨\u0006\u0007"}, d2 = {"broadcast", "", "T", "Landroid/os/IInterface;", "Landroid/os/RemoteCallbackList;", "action", "Lkotlin/Function1;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class RemoteCallbackListExtensionsKt {
    public static final <T extends android.os.IInterface> void broadcast(android.os.RemoteCallbackList<T> remoteCallbackList, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        int itemCount = remoteCallbackList.beginBroadcast();
        int i = 0;
        while (true) {
            if (i < itemCount) {
                try {
                    function1.invoke(remoteCallbackList.getBroadcastItem(i));
                    i++;
                } finally {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                    remoteCallbackList.finishBroadcast();
                    com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                }
            } else {
                return;
            }
        }
    }
}
