package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class FunctionReferenceImpl extends com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference {
    public FunctionReferenceImpl(int i, com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer kDeclarationContainer, java.lang.String str, java.lang.String str2) {
        super(i, NO_RECEIVER, ((com.android.server.permission.jarjar.kotlin.jvm.internal.ClassBasedDeclarationContainer) kDeclarationContainer).getJClass(), str, str2, !(kDeclarationContainer instanceof com.android.server.permission.jarjar.kotlin.reflect.KClass) ? 1 : 0);
    }

    public FunctionReferenceImpl(int arity, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(arity, NO_RECEIVER, owner, name, signature, flags);
    }

    public FunctionReferenceImpl(int arity, java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(arity, receiver, owner, name, signature, flags);
    }
}
