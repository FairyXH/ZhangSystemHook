package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class PropertyReference1Impl extends com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference1 {
    public PropertyReference1Impl(com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(NO_RECEIVER, ((com.android.server.permission.jarjar.kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof com.android.server.permission.jarjar.kotlin.reflect.KClass) ? 1 : 0);
    }

    public PropertyReference1Impl(java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(NO_RECEIVER, owner, name, signature, flags);
    }

    public PropertyReference1Impl(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty1
    public java.lang.Object get(java.lang.Object receiver) {
        return getGetter().call(receiver);
    }
}
