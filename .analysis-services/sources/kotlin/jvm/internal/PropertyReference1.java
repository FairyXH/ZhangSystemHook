package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public abstract class PropertyReference1 extends kotlin.jvm.internal.PropertyReference implements kotlin.reflect.KProperty1 {
    public PropertyReference1() {
    }

    public PropertyReference1(java.lang.Object receiver) {
        super(receiver);
    }

    public PropertyReference1(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected kotlin.reflect.KCallable computeReflected() {
        return kotlin.jvm.internal.Reflection.property1(this);
    }

    @Override // kotlin.jvm.functions.Function1
    public java.lang.Object invoke(java.lang.Object receiver) {
        return get(receiver);
    }

    @Override // kotlin.reflect.KProperty
    public kotlin.reflect.KProperty1.Getter getGetter() {
        return ((kotlin.reflect.KProperty1) getReflected()).getGetter();
    }

    @Override // kotlin.reflect.KProperty1
    public java.lang.Object getDelegate(java.lang.Object receiver) {
        return ((kotlin.reflect.KProperty1) getReflected()).getDelegate(receiver);
    }
}
