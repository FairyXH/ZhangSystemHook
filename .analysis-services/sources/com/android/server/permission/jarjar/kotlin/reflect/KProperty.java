package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: KProperty.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0007\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0002\u000f\u0010R\u001e\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X¦\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\n8&X§\u0004¢\u0006\f\u0012\u0004\b\u000b\u0010\u0006\u001a\u0004\b\t\u0010\fR\u001a\u0010\r\u001a\u00020\n8&X§\u0004¢\u0006\f\u0012\u0004\b\u000e\u0010\u0006\u001a\u0004\b\r\u0010\f¨\u0006\u0011"}, d2 = {"Lkotlin/reflect/KProperty;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/reflect/KCallable;", "getter", "Lkotlin/reflect/KProperty$Getter;", "getGetter$annotations", "()V", "getGetter", "()Lkotlin/reflect/KProperty$Getter;", "isConst", "", "isConst$annotations", "()Z", "isLateinit", "isLateinit$annotations", "Accessor", "Getter", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface KProperty<V> extends com.android.server.permission.jarjar.kotlin.reflect.KCallable<V> {

    /* JADX INFO: compiled from: KProperty.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u0002R\u0018\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/reflect/KProperty$Accessor;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "", "property", "Lkotlin/reflect/KProperty;", "getProperty", "()Lkotlin/reflect/KProperty;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Accessor<V> {
        com.android.server.permission.jarjar.kotlin.reflect.KProperty<V> getProperty();
    }

    /* JADX INFO: compiled from: KProperty.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void getGetter$annotations() {
        }

        public static /* synthetic */ void isConst$annotations() {
        }

        public static /* synthetic */ void isLateinit$annotations() {
        }
    }

    /* JADX INFO: compiled from: KProperty.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003¨\u0006\u0004"}, d2 = {"Lkotlin/reflect/KProperty$Getter;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/reflect/KProperty$Accessor;", "Lkotlin/reflect/KFunction;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Getter<V> extends com.android.server.permission.jarjar.kotlin.reflect.KProperty.Accessor<V>, com.android.server.permission.jarjar.kotlin.reflect.KFunction<V> {
    }

    com.android.server.permission.jarjar.kotlin.reflect.KProperty.Getter<V> getGetter();

    boolean isConst();

    boolean isLateinit();
}
