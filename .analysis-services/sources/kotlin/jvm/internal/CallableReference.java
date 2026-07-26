package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public abstract class CallableReference implements kotlin.reflect.KCallable, java.io.Serializable {
    public static final java.lang.Object NO_RECEIVER = kotlin.jvm.internal.CallableReference.NoReceiver.INSTANCE;
    private final boolean isTopLevel;
    private final java.lang.String name;
    private final java.lang.Class owner;
    protected final java.lang.Object receiver;
    private transient kotlin.reflect.KCallable reflected;
    private final java.lang.String signature;

    protected abstract kotlin.reflect.KCallable computeReflected();

    private static class NoReceiver implements java.io.Serializable {
        private static final kotlin.jvm.internal.CallableReference.NoReceiver INSTANCE = new kotlin.jvm.internal.CallableReference.NoReceiver();

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

    public kotlin.reflect.KCallable compute() {
        kotlin.reflect.KCallable result = this.reflected;
        if (result == null) {
            kotlin.reflect.KCallable result2 = computeReflected();
            this.reflected = result2;
            return result2;
        }
        return result;
    }

    protected kotlin.reflect.KCallable getReflected() {
        kotlin.reflect.KCallable result = compute();
        if (result == this) {
            throw new kotlin.jvm.KotlinReflectionNotSupportedError();
        }
        return result;
    }

    public kotlin.reflect.KDeclarationContainer getOwner() {
        if (this.owner == null) {
            return null;
        }
        return this.isTopLevel ? kotlin.jvm.internal.Reflection.getOrCreateKotlinPackage(this.owner) : kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(this.owner);
    }

    @Override // kotlin.reflect.KCallable
    public java.lang.String getName() {
        return this.name;
    }

    public java.lang.String getSignature() {
        return this.signature;
    }

    @Override // kotlin.reflect.KCallable
    public java.util.List<kotlin.reflect.KParameter> getParameters() {
        return getReflected().getParameters();
    }

    @Override // kotlin.reflect.KCallable
    public kotlin.reflect.KType getReturnType() {
        return getReflected().getReturnType();
    }

    @Override // kotlin.reflect.KAnnotatedElement
    public java.util.List<java.lang.annotation.Annotation> getAnnotations() {
        return getReflected().getAnnotations();
    }

    @Override // kotlin.reflect.KCallable
    public java.util.List<kotlin.reflect.KTypeParameter> getTypeParameters() {
        return getReflected().getTypeParameters();
    }

    @Override // kotlin.reflect.KCallable
    public java.lang.Object call(java.lang.Object... args) {
        return getReflected().call(args);
    }

    @Override // kotlin.reflect.KCallable
    public java.lang.Object callBy(java.util.Map args) {
        return getReflected().callBy(args);
    }

    @Override // kotlin.reflect.KCallable
    public kotlin.reflect.KVisibility getVisibility() {
        return getReflected().getVisibility();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isFinal() {
        return getReflected().isFinal();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isOpen() {
        return getReflected().isOpen();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isAbstract() {
        return getReflected().isAbstract();
    }

    @Override // kotlin.reflect.KCallable
    public boolean isSuspend() {
        return getReflected().isSuspend();
    }
}
