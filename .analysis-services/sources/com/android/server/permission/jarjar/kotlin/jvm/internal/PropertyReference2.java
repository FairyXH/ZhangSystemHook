package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PropertyReference2 extends com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference implements com.android.server.permission.jarjar.kotlin.reflect.KProperty2 {
    public PropertyReference2() {
    }

    public PropertyReference2(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(NO_RECEIVER, owner, name, signature, flags);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    protected com.android.server.permission.jarjar.kotlin.reflect.KCallable computeReflected() {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.property2(this);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function2
    public java.lang.Object invoke(java.lang.Object receiver1, java.lang.Object receiver2) {
        return get(receiver1, receiver2);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty
    public com.android.server.permission.jarjar.kotlin.reflect.KProperty2.Getter getGetter() {
        return ((com.android.server.permission.jarjar.kotlin.reflect.KProperty2) getReflected()).getGetter();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty2
    public java.lang.Object getDelegate(java.lang.Object receiver1, java.lang.Object receiver2) {
        return ((com.android.server.permission.jarjar.kotlin.reflect.KProperty2) getReflected()).getDelegate(receiver1, receiver2);
    }
}
