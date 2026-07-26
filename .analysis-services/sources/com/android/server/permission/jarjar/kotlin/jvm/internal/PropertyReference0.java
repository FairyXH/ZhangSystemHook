package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PropertyReference0 extends com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference implements com.android.server.permission.jarjar.kotlin.reflect.KProperty0 {
    public PropertyReference0() {
    }

    public PropertyReference0(java.lang.Object receiver) {
        super(receiver);
    }

    public PropertyReference0(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    protected com.android.server.permission.jarjar.kotlin.reflect.KCallable computeReflected() {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.property0(this);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function0
    public java.lang.Object invoke() {
        return get();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty
    public com.android.server.permission.jarjar.kotlin.reflect.KProperty0.Getter getGetter() {
        return ((com.android.server.permission.jarjar.kotlin.reflect.KProperty0) getReflected()).getGetter();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty0
    public java.lang.Object getDelegate() {
        return ((com.android.server.permission.jarjar.kotlin.reflect.KProperty0) getReflected()).getDelegate();
    }
}
