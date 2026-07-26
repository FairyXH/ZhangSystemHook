package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class TypesJVMKt$typeToString$unwrap$1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReferenceImpl implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.lang.Class<?>, java.lang.Class<?>> {
    public static final com.android.server.permission.jarjar.kotlin.reflect.TypesJVMKt$typeToString$unwrap$1 INSTANCE = new com.android.server.permission.jarjar.kotlin.reflect.TypesJVMKt$typeToString$unwrap$1();

    TypesJVMKt$typeToString$unwrap$1() {
        super(1, java.lang.Class.class, "getComponentType", "getComponentType()Ljava/lang/Class;", 0);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.lang.Class<?> invoke(java.lang.Class<?> cls) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cls, "p0");
        return cls.getComponentType();
    }
}
