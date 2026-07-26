package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CallableReference implements com.android.server.permission.jarjar.kotlin.reflect.KCallable, java.io.Serializable {
    public static final java.lang.Object NO_RECEIVER = com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference.NoReceiver.INSTANCE;
    private final boolean isTopLevel;
    private final java.lang.String name;
    private final java.lang.Class owner;
    protected final java.lang.Object receiver;
    private transient com.android.server.permission.jarjar.kotlin.reflect.KCallable reflected;
    private final java.lang.String signature;

    protected abstract com.android.server.permission.jarjar.kotlin.reflect.KCallable computeReflected();

    private static class NoReceiver implements java.io.Serializable {
        private static final com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference.NoReceiver INSTANCE = new com.android.server.permission.jarjar.kotlin.jvm.internal.CallableReference.NoReceiver();

        private NoReceiver() {
        }

        private java.lang.Object readResolve() throws java.io.ObjectStreamException {
            return INSTANCE;
        }
    }

    public CallableReference() {
        this(NO_RECEIVER);
    }

    protected CallableReference(java.lang.Object receiver) {
        this(receiver, null, null, null, false);
    }

    protected CallableReference(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, boolean isTopLevel) {
        this.receiver = receiver;
        this.owner = owner;
        this.name = name;
        this.signature = signature;
        this.isTopLevel = isTopLevel;
    }

    public java.lang.Object getBoundReceiver() {
        return this.receiver;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KCallable compute() {
        com.android.server.permission.jarjar.kotlin.reflect.KCallable result = this.reflected;
        if (result == null) {
            com.android.server.permission.jarjar.kotlin.reflect.KCallable result2 = computeReflected();
            this.reflected = result2;
            return result2;
        }
        return result;
    }

    protected com.android.server.permission.jarjar.kotlin.reflect.KCallable getReflected() {
        com.android.server.permission.jarjar.kotlin.reflect.KCallable result = compute();
        if (result == this) {
            throw new com.android.server.permission.jarjar.kotlin.jvm.KotlinReflectionNotSupportedError();
        }
        return result;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer getOwner() {
        if (this.owner == null) {
            return null;
        }
        return this.isTopLevel ? com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.getOrCreateKotlinPackage(this.owner) : com.android.server.permission.jarjar.kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(this.owner);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public java.lang.String getName() {
        return this.name;
    }

    public java.lang.String getSignature() {
        return this.signature;
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KParameter> getParameters() {
        return getReflected().getParameters();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public com.android.server.permission.jarjar.kotlin.reflect.KType getReturnType() {
        return getReflected().getReturnType();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KAnnotatedElement
    public java.util.List<java.lang.annotation.Annotation> getAnnotations() {
        return getReflected().getAnnotations();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KTypeParameter> getTypeParameters() {
        return getReflected().getTypeParameters();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public java.lang.Object call(java.lang.Object... args) {
        return getReflected().call(args);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public java.lang.Object callBy(java.util.Map args) {
        return getReflected().callBy(args);
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public com.android.server.permission.jarjar.kotlin.reflect.KVisibility getVisibility() {
        return getReflected().getVisibility();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public boolean isFinal() {
        return getReflected().isFinal();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public boolean isOpen() {
        return getReflected().isOpen();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public boolean isAbstract() {
        return getReflected().isAbstract();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KCallable
    public boolean isSuspend() {
        return getReflected().isSuspend();
    }
}
