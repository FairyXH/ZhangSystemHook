package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class PropertyReference0Impl extends kotlin.jvm.internal.PropertyReference0 {
    public PropertyReference0Impl(kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(NO_RECEIVER, ((kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof kotlin.reflect.KClass) ? 1 : 0);
    }

    public PropertyReference0Impl(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(NO_RECEIVER, owner, name, signature, flags);
    }

    public PropertyReference0Impl(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // kotlin.reflect.KProperty0
    public java.lang.Object get() {
        return getGetter().call(new java.lang.Object[0]);
    }
}
