package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class ParameterizedTypeImpl$getTypeName$1$1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReferenceImpl implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.lang.reflect.Type, java.lang.String> {
    public static final com.android.server.permission.jarjar.kotlin.reflect.ParameterizedTypeImpl$getTypeName$1$1 INSTANCE = new com.android.server.permission.jarjar.kotlin.reflect.ParameterizedTypeImpl$getTypeName$1$1();

    ParameterizedTypeImpl$getTypeName$1$1() {
        super(1, com.android.server.permission.jarjar.kotlin.reflect.TypesJVMKt.class, "typeToString", "typeToString(Ljava/lang/reflect/Type;)Ljava/lang/String;", 1);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.lang.String invoke(java.lang.reflect.Type p0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        return com.android.server.permission.jarjar.kotlin.reflect.TypesJVMKt.typeToString(p0);
    }
}
