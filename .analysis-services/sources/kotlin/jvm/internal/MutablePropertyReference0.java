package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public abstract class MutablePropertyReference0 extends kotlin.jvm.internal.MutablePropertyReference implements kotlin.reflect.KMutableProperty0 {
    public MutablePropertyReference0() {
    }

    public MutablePropertyReference0(java.lang.Object receiver) {
        super(receiver);
    }

    public MutablePropertyReference0(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected kotlin.reflect.KCallable computeReflected() {
        return kotlin.jvm.internal.Reflection.mutableProperty0(this);
    }

    @Override // kotlin.jvm.functions.Function0
    public java.lang.Object invoke() {
        return get();
    }

    @Override // kotlin.reflect.KProperty
    public kotlin.reflect.KProperty0.Getter getGetter() {
        return ((kotlin.reflect.KMutableProperty0) getReflected()).getGetter();
    }

    @Override // kotlin.reflect.KMutableProperty
    public kotlin.reflect.KMutableProperty0.Setter getSetter() {
        return ((kotlin.reflect.KMutableProperty0) getReflected()).getSetter();
    }

    @Override // kotlin.reflect.KProperty0
    public java.lang.Object getDelegate() {
        return ((kotlin.reflect.KMutableProperty0) getReflected()).getDelegate();
    }
}
