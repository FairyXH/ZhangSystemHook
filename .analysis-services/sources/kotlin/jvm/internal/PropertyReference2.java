package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public abstract class PropertyReference2 extends kotlin.jvm.internal.PropertyReference implements kotlin.reflect.KProperty2 {
    public PropertyReference2() {
    }

    public PropertyReference2(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(NO_RECEIVER, owner, name, signature, flags);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected kotlin.reflect.KCallable computeReflected() {
        return kotlin.jvm.internal.Reflection.property2(this);
    }

    @Override // kotlin.jvm.functions.Function2
    public java.lang.Object invoke(java.lang.Object receiver1, java.lang.Object receiver2) {
        return get(receiver1, receiver2);
    }

    @Override // kotlin.reflect.KProperty
    public kotlin.reflect.KProperty2.Getter getGetter() {
        return ((kotlin.reflect.KProperty2) getReflected()).getGetter();
    }

    @Override // kotlin.reflect.KProperty2
    public java.lang.Object getDelegate(java.lang.Object receiver1, java.lang.Object receiver2) {
        return ((kotlin.reflect.KProperty2) getReflected()).getDelegate(receiver1, receiver2);
    }
}
