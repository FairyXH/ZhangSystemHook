package kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
/* synthetic */ class ParameterizedTypeImpl$getTypeName$1$1 extends kotlin.jvm.internal.FunctionReferenceImpl implements kotlin.jvm.functions.Function1<java.lang.reflect.Type, java.lang.String> {
    public static final kotlin.reflect.ParameterizedTypeImpl$getTypeName$1$1 INSTANCE = new kotlin.reflect.ParameterizedTypeImpl$getTypeName$1$1();

    ParameterizedTypeImpl$getTypeName$1$1() {
        super(1, kotlin.reflect.TypesJVMKt.class, "typeToString", "typeToString(Ljava/lang/reflect/Type;)Ljava/lang/String;", 1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final java.lang.String invoke(java.lang.reflect.Type p0) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(p0, "p0");
        return kotlin.reflect.TypesJVMKt.typeToString(p0);
    }
}
