package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class MutablePropertyReference2Impl extends kotlin.jvm.internal.MutablePropertyReference2 {
    public MutablePropertyReference2Impl(kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(((kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof kotlin.reflect.KClass) ? 1 : 0);
    }

    public MutablePropertyReference2Impl(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(owner, name, signature, flags);
    }

    @Override // kotlin.reflect.KProperty2
    public java.lang.Object get(java.lang.Object receiver1, java.lang.Object receiver2) {
        return getGetter().call(receiver1, receiver2);
    }

    @Override // kotlin.reflect.KMutableProperty2
    public void set(java.lang.Object receiver1, java.lang.Object receiver2, java.lang.Object value) {
        getSetter().call(receiver1, receiver2, value);
    }
}
