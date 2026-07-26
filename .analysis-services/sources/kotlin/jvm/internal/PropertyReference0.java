package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public abstract class PropertyReference0 extends kotlin.jvm.internal.PropertyReference implements kotlin.reflect.KProperty0 {
    public PropertyReference0() {
    }

    public PropertyReference0(java.lang.Object receiver) {
        super(receiver);
    }

    public PropertyReference0(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected kotlin.reflect.KCallable computeReflected() {
        return kotlin.jvm.internal.Reflection.property0(this);
    }

    @Override // kotlin.jvm.functions.Function0
    public java.lang.Object invoke() {
        return get();
    }

    @Override // kotlin.reflect.KProperty
    public kotlin.reflect.KProperty0.Getter getGetter() {
        return ((kotlin.reflect.KProperty0) getReflected()).getGetter();
    }

    @Override // kotlin.reflect.KProperty0
    public java.lang.Object getDelegate() {
        return ((kotlin.reflect.KProperty0) getReflected()).getDelegate();
    }
}
