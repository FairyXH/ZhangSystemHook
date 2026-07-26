package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class PropertyReference2Impl extends kotlin.jvm.internal.PropertyReference2 {
    public PropertyReference2Impl(kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(((kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof kotlin.reflect.KClass) ? 1 : 0);
    }

    public PropertyReference2Impl(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(owner, name, signature, flags);
    }

    @Override // kotlin.reflect.KProperty2
    public java.lang.Object get(java.lang.Object receiver1, java.lang.Object receiver2) {
        return getGetter().call(receiver1, receiver2);
    }
}
