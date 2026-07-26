package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class FunInterfaceConstructorReference extends com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference implements java.io.Serializable {
    private final java.lang.Class funInterface;

    public FunInterfaceConstructorReference(java.lang.Class funInterface) {
        super(1);
        this.funInterface = funInterface;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.permission.jarjar.kotlin.jvm.internal.FunInterfaceConstructorReference)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.FunInterfaceConstructorReference other = (com.android.server.permission.jarjar.kotlin.jvm.internal.FunInterfaceConstructorReference) o;
        return this.funInterface.equals(other.funInterface);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference
    public int hashCode() {
        return this.funInterface.hashCode();
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference
    public java.lang.String toString() {
        return "fun interface " + this.funInterface.getName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference, com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    public com.android.server.permission.jarjar.kotlin.reflect.KFunction getReflected() {
        throw new java.lang.UnsupportedOperationException("Functional interface constructor does not support reflection");
    }
}
