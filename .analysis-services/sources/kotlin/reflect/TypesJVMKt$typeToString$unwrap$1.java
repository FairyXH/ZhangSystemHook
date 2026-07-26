package kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class TypesJVMKt$typeToString$unwrap$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function1<java.lang.Class<?>, java.lang.Class<?>> {
    public static final kotlin.reflect.TypesJVMKt$typeToString$unwrap$1 INSTANCE = new kotlin.reflect.TypesJVMKt$typeToString$unwrap$1();

    TypesJVMKt$typeToString$unwrap$1() {
        super(1, java.lang.Class.class, "getComponentType", "getComponentType()Ljava/lang/Class;", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final java.lang.Class<?> invoke(java.lang.Class<?> p0) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        return p0.getComponentType();
    }
}
