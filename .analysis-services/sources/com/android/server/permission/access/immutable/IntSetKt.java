package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: IntSet.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0002¨\u0006\u0005"}, d2 = {"contains", "", "Landroid/util/SparseBooleanArray;", "key", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class IntSetKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean contains(android.util.SparseBooleanArray $this$contains, int key) {
        return $this$contains.indexOfKey(key) >= 0;
    }
}
