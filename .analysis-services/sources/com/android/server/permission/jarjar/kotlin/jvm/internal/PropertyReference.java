package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PropertyReference extends com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference implements com.android.server.permission.jarjar.kotlin.reflect.KProperty {
    private final boolean syntheticJavaProperty;

    public PropertyReference() {
        this.syntheticJavaProperty = false;
    }

    public PropertyReference(java.lang.Object receiver) {
        super(receiver);
        this.syntheticJavaProperty = false;
    }

    public PropertyReference(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, (flags & 1) == 1);
        this.syntheticJavaProperty = (flags & 2) == 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    public com.android.server.permission.jarjar.kotlin.reflect.KProperty getReflected() {
        if (this.syntheticJavaProperty) {
            throw new java.lang.UnsupportedOperationException("Kotlin reflection is not yet supported for synthetic Java properties");
        }
        return (com.android.server.permission.jarjar.kotlin.reflect.KProperty) super.getReflected();
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference
    public com.android.server.permission.jarjar.kotlin.reflect.KCallable compute() {
        return this.syntheticJavaProperty ? this : super.compute();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty
    public boolean isLateinit() {
        return getReflected().isLateinit();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KProperty
    public boolean isConst() {
        return getReflected().isConst();
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference other = (com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference) obj;
            return getOwner().equals(other.getOwner()) && getName().equals(other.getName()) && getSignature().equals(other.getSignature()) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(getBoundReceiver(), other.getBoundReceiver());
        }
        if (obj instanceof com.android.server.permission.jarjar.kotlin.reflect.KProperty) {
            return obj.equals(compute());
        }
        return false;
    }

    public int hashCode() {
        return (((getOwner().hashCode() * 31) + getName().hashCode()) * 31) + getSignature().hashCode();
    }

    public java.lang.String toString() {
        com.android.server.permission.jarjar.kotlin.reflect.KCallable reflected = compute();
        if (reflected != this) {
            return reflected.toString();
        }
        return "property " + getName() + " (Kotlin reflection is not available)";
    }
}
