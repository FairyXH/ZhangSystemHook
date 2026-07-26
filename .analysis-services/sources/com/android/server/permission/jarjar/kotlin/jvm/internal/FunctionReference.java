package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class FunctionReference extends com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference implements com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionBase, com.android.server.permission.jarjar.kotlin.reflect.KFunction {
    private final int arity;
    private final int flags;

    public FunctionReference(int arity) {
        this(arity, NO_RECEIVER, null, null, null, 0);
    }

    public FunctionReference(int arity, java.lang.Object receiver) {
        this(arity, receiver, null, null, null, 0);
    }

    public FunctionReference(int arity, java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, (flags & 1) == 1);
        this.arity = arity;
        this.flags = flags >> 1;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionBase
    public int getArity() {
        return this.arity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    public com.android.server.permission.jarjar.kotlin.reflect.KFunction getReflected() {
        return (com.android.server.permission.jarjar.kotlin.reflect.KFunction) super.getReflected();
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    protected com.android.server.permission.jarjar.kotlin.reflect.KCallable computeReflected() {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.function(this);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KFunction
    public boolean isInline() {
        return getReflected().isInline();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KFunction
    public boolean isExternal() {
        return getReflected().isExternal();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KFunction
    public boolean isOperator() {
        return getReflected().isOperator();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KFunction
    public boolean isInfix() {
        return getReflected().isInfix();
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference, com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public boolean isSuspend() {
        return getReflected().isSuspend();
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference other = (com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference) obj;
            return getName().equals(other.getName()) && getSignature().equals(other.getSignature()) && this.flags == other.flags && this.arity == other.arity && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(getBoundReceiver(), other.getBoundReceiver()) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(getOwner(), other.getOwner());
        }
        if (obj instanceof com.android.server.permission.jarjar.kotlin.reflect.KFunction) {
            return obj.equals(compute());
        }
        return false;
    }

    public int hashCode() {
        return (((getOwner() == null ? 0 : getOwner().hashCode() * 31) + getName().hashCode()) * 31) + getSignature().hashCode();
    }

    public java.lang.String toString() {
        com.android.server.permission.jarjar.kotlin.reflect.KCallable reflected = compute();
        if (reflected != this) {
            return reflected.toString();
        }
        return "<init>".equals(getName()) ? "constructor (Kotlin reflection is not available)" : "function " + getName() + " (Kotlin reflection is not available)";
    }
}
