package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: KParameter.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0001\u0018R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\bR\u001a\u0010\t\u001a\u00020\u00078&X§\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\t\u0010\bR\u0012\u0010\f\u001a\u00020\rX¦\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u0004\u0018\u00010\u0011X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0012\u0010\u0014\u001a\u00020\u0015X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u0019"}, d2 = {"Lkotlin/reflect/KParameter;", "Lkotlin/reflect/KAnnotatedElement;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "getIndex", "()I", "isOptional", "", "()Z", "isVararg", "isVararg$annotations", "()V", "kind", "Lkotlin/reflect/KParameter$Kind;", "getKind", "()Lkotlin/reflect/KParameter$Kind;", "name", "", "getName", "()Ljava/lang/String;", "type", "Lkotlin/reflect/KType;", "getType", "()Lkotlin/reflect/KType;", "Kind", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface KParameter extends com.android.server.permission.jarjar.kotlin.reflect.KAnnotatedElement {

    /* JADX INFO: compiled from: KParameter.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void isVararg$annotations() {
        }
    }

    /* JADX INFO: compiled from: KParameter.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, d2 = {"Lkotlin/reflect/KParameter$Kind;", "", "(Ljava/lang/String;I)V", "INSTANCE", "EXTENSION_RECEIVER", "VALUE", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum Kind {
        INSTANCE,
        EXTENSION_RECEIVER,
        VALUE;

        private static final /* synthetic */ com.android.server.permission.jarjar.kotlin.enums.EnumEntries $ENTRIES = com.android.server.permission.jarjar.kotlin.enums.EnumEntriesKt.enumEntries($VALUES);

        public static com.android.server.permission.jarjar.kotlin.enums.EnumEntries<com.android.server.permission.jarjar.kotlin.reflect.KParameter.Kind> getEntries() {
            return $ENTRIES;
        }
    }

    int getIndex();

    com.android.server.permission.jarjar.kotlin.reflect.KParameter.Kind getKind();

    java.lang.String getName();

    com.android.server.permission.jarjar.kotlin.reflect.KType getType();

    boolean isOptional();

    boolean isVararg();
}
