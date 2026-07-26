package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: KProperty.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0000\n\u0002\b\u0002\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001\u000eJ\r\u0010\n\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u000bJ\n\u0010\f\u001a\u0004\u0018\u00010\rH'R\u001e\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X¦\u0004¢\u0006\f\u0012\u0004\b\u0006\u0010\u0007\u001a\u0004\b\b\u0010\t¨\u0006\u000f"}, d2 = {"Lkotlin/reflect/KProperty0;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/reflect/KProperty;", "Lkotlin/Function0;", "getter", "Lkotlin/reflect/KProperty0$Getter;", "getGetter$annotations", "()V", "getGetter", "()Lkotlin/reflect/KProperty0$Getter;", "get", "()Ljava/lang/Object;", "getDelegate", "", "Getter", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface KProperty0<V> extends com.android.server.permission.jarjar.kotlin.reflect.KProperty<V>, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<V> {

    /* JADX INFO: compiled from: KProperty.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void getGetter$annotations() {
        }
    }

    /* JADX INFO: compiled from: KProperty.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003¨\u0006\u0004"}, d2 = {"Lkotlin/reflect/KProperty0$Getter;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/reflect/KProperty$Getter;", "Lkotlin/Function0;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Getter<V> extends com.android.server.permission.jarjar.kotlin.reflect.KProperty.Getter<V>, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<V> {
    }

    V get();

    java.lang.Object getDelegate();

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty
    com.android.server.permission.jarjar.kotlin.reflect.KProperty0.Getter<V> getGetter();
}
