package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: KType.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001R \u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00038&X§\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\n8&X§\u0004¢\u0006\f\u0012\u0004\b\u000b\u0010\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000fX¦\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u0010¨\u0006\u0011"}, d2 = {"Lkotlin/reflect/KType;", "Lkotlin/reflect/KAnnotatedElement;", "arguments", "", "Lkotlin/reflect/KTypeProjection;", "getArguments$annotations", "()V", "getArguments", "()Ljava/util/List;", "classifier", "Lkotlin/reflect/KClassifier;", "getClassifier$annotations", "getClassifier", "()Lkotlin/reflect/KClassifier;", "isMarkedNullable", "", "()Z", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface KType extends com.android.server.permission.jarjar.kotlin.reflect.KAnnotatedElement {

    /* JADX INFO: compiled from: KType.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void getArguments$annotations() {
        }

        public static /* synthetic */ void getClassifier$annotations() {
        }
    }

    java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection> getArguments();

    com.android.server.permission.jarjar.kotlin.reflect.KClassifier getClassifier();

    boolean isMarkedNullable();
}
