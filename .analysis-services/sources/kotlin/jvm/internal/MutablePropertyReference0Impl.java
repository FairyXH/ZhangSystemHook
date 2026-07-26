package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class MutablePropertyReference0Impl extends kotlin.jvm.internal.MutablePropertyReference0 {
    public MutablePropertyReference0Impl(kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(NO_RECEIVER, ((kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof kotlin.reflect.KClass) ? 1 : 0);
    }

    public MutablePropertyReference0Impl(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(NO_RECEIVER, owner, name, signature, flags);
    }

    public MutablePropertyReference0Impl(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // kotlin.reflect.KProperty0
    public java.lang.Object get() {
        return getGetter().call(new java.lang.Object[0]);
    }

    @Override // kotlin.reflect.KMutableProperty0
    public void set(java.lang.Object value) {
        getSetter().call(value);
    }
}
