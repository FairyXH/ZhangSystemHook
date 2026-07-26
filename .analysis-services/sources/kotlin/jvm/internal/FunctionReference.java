package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class FunctionReference extends kotlin.jvm.internal.CallableReference implements kotlin.jvm.internal.FunctionBase, kotlin.reflect.KFunction {
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

    @Override // kotlin.jvm.internal.FunctionBase
    public int getArity() {
        return this.arity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlin.jvm.internal.CallableReference
    public kotlin.reflect.KFunction getReflected() {
        return (kotlin.reflect.KFunction) super.getReflected();
    }

    @Override // kotlin.jvm.internal.CallableReference
    protected kotlin.reflect.KCallable computeReflected() {
        return kotlin.jvm.internal.Reflection.function(this);
    }

    @Override // kotlin.reflect.KFunction
    public boolean isInline() {
        return getReflected().isInline();
    }

    @Override // kotlin.reflect.KFunction
    public boolean isExternal() {
        return getReflected().isExternal();
    }

    @Override // kotlin.reflect.KFunction
    public boolean isOperator() {
        return getReflected().isOperator();
    }

    @Override // kotlin.reflect.KFunction
    public boolean isInfix() {
        return getReflected().isInfix();
    }

    @Override // kotlin.jvm.internal.CallableReference, kotlin.reflect.KCallable
    public boolean isSuspend() {
        return getReflected().isSuspend();
    }

    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof kotlin.jvm.internal.FunctionReference) {
            kotlin.jvm.internal.FunctionReference other = (kotlin.jvm.internal.FunctionReference) obj;
            return getName().equals(other.getName()) && getSignature().equals(other.getSignature()) && this.flags == other.flags && this.arity == other.arity && kotlin.jvm.internal.Intrinsics.areEqual(getBoundReceiver(), other.getBoundReceiver()) && kotlin.jvm.internal.Intrinsics.areEqual(getOwner(), other.getOwner());
        }
        if (obj instanceof kotlin.reflect.KFunction) {
            return obj.equals(compute());
        }
        return false;
    }

    public int hashCode() {
        return (((getOwner() == null ? 0 : getOwner().hashCode() * 31) + getName().hashCode()) * 31) + getSignature().hashCode();
    }

    public java.lang.String toString() {
        kotlin.reflect.KCallable reflected = compute();
        if (reflected != this) {
            return reflected.toString();
        }
        return "<init>".equals(getName()) ? "constructor (Kotlin reflection is not available)" : "function " + getName() + " (Kotlin reflection is not available)";
    }
}
