package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class PropertyReference2Impl extends com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference2 {
    public PropertyReference2Impl(com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(((com.android.server.permission.jarjar.kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof com.android.server.permission.jarjar.kotlin.reflect.KClass) ? 1 : 0);
    }

    public PropertyReference2Impl(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(owner, name, signature, flags);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty2
    public java.lang.Object get(java.lang.Object receiver1, java.lang.Object receiver2) {
        return getGetter().call(receiver1, receiver2);
    }
}
