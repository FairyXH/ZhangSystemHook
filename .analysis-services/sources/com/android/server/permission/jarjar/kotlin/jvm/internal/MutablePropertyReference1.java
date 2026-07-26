package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MutablePropertyReference1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.MutablePropertyReference implements com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty1 {
    public MutablePropertyReference1() {
    }

    public MutablePropertyReference1(java.lang.Object receiver) {
        super(receiver);
    }

    public MutablePropertyReference1(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    protected com.android.server.permission.jarjar.kotlin.reflect.KCallable computeReflected() {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.mutableProperty1(this);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public java.lang.Object invoke(java.lang.Object receiver) {
        return get(receiver);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty
    public com.android.server.permission.jarjar.kotlin.reflect.KProperty1.Getter getGetter() {
        return ((com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty1) getReflected()).getGetter();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty
    public com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty1.Setter getSetter() {
        return ((com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty1) getReflected()).getSetter();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty1
    public java.lang.Object getDelegate(java.lang.Object receiver) {
        return ((com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty1) getReflected()).getDelegate(receiver);
    }
}
