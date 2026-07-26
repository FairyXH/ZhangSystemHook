package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class AdaptedFunctionReference implements com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionBase, java.io.Serializable {
    private final int arity;
    private final int flags;
    private final boolean isTopLevel;
    private final java.lang.String name;
    private final java.lang.Class owner;
    protected final java.lang.Object receiver;
    private final java.lang.String signature;

    public AdaptedFunctionReference(int arity, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        this(arity, com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference.NO_RECEIVER, owner, name, signature, flags);
    }

    public AdaptedFunctionReference(int arity, java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        this.receiver = receiver;
        this.owner = owner;
        this.name = name;
        this.signature = signature;
        this.isTopLevel = (flags & 1) == 1;
        this.arity = arity;
        this.flags = flags >> 1;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionBase
    public int getArity() {
        return this.arity;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer getOwner() {
        if (this.owner == null) {
            return null;
        }
        return this.isTopLevel ? com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.getOrCreateKotlinPackage(this.owner) : com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(this.owner);
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.permission.jarjar.kotlin.jvm.internal.AdaptedFunctionReference)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.AdaptedFunctionReference other = (com.android.server.permission.jarjar.kotlin.jvm.internal.AdaptedFunctionReference) o;
        return this.isTopLevel == other.isTopLevel && this.arity == other.arity && this.flags == other.flags && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.receiver, other.receiver) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.owner, other.owner) && this.name.equals(other.name) && this.signature.equals(other.signature);
    }

    public int hashCode() {
        int result = this.receiver != null ? this.receiver.hashCode() : 0;
        return (((((((((((result * 31) + (this.owner != null ? this.owner.hashCode() : 0)) * 31) + this.name.hashCode()) * 31) + this.signature.hashCode()) * 31) + (this.isTopLevel ? 1231 : 1237)) * 31) + this.arity) * 31) + this.flags;
    }

    public java.lang.String toString() {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.renderLambdaToString(this);
    }
}
