package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public abstract class MutablePropertyReference1 extends kotlin.jvm.internal.MutablePropertyReference implements kotlin.reflect.KMutableProperty1 {
    public MutablePropertyReference1() {
    }

    public MutablePropertyReference1(java.lang.Object receiver) {
        super(receiver);
    }

    public MutablePropertyReference1(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected kotlin.reflect.KCallable computeReflected() {
        return kotlin.jvm.internal.Reflection.mutableProperty1(this);
    }

    @Override // kotlin.jvm.functions.Function1
    public java.lang.Object invoke(java.lang.Object receiver) {
        return get(receiver);
    }

    @Override // kotlin.reflect.KProperty
    public kotlin.reflect.KProperty1.Getter getGetter() {
        return ((kotlin.reflect.KMutableProperty1) getReflected()).getGetter();
    }

    @Override // kotlin.reflect.KMutableProperty
    public kotlin.reflect.KMutableProperty1.Setter getSetter() {
        return ((kotlin.reflect.KMutableProperty1) getReflected()).getSetter();
    }

    @Override // kotlin.reflect.KProperty1
    public java.lang.Object getDelegate(java.lang.Object receiver) {
        return ((kotlin.reflect.KMutableProperty1) getReflected()).getDelegate(receiver);
    }
}
