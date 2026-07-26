package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class MutablePropertyReference1Impl extends kotlin.jvm.internal.MutablePropertyReference1 {
    public MutablePropertyReference1Impl(kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(NO_RECEIVER, ((kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof kotlin.reflect.KClass) ? 1 : 0);
    }

    public MutablePropertyReference1Impl(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(NO_RECEIVER, owner, name, signature, flags);
    }

    public MutablePropertyReference1Impl(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // kotlin.reflect.KProperty1
    public java.lang.Object get(java.lang.Object receiver) {
        return getGetter().call(receiver);
    }

    @Override // kotlin.reflect.KMutableProperty1
    public void set(java.lang.Object receiver, java.lang.Object value) {
        getSetter().call(receiver, value);
    }
}
