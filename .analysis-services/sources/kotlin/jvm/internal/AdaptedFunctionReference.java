package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class AdaptedFunctionReference implements kotlin.jvm.internal.FunctionBase, java.io.Serializable {
    private final int arity;
    private final int flags;
    private final boolean isTopLevel;
    private final java.lang.String name;
    private final java.lang.Class owner;
    protected final java.lang.Object receiver;
    private final java.lang.String signature;

    public AdaptedFunctionReference(int arity, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        this(arity, kotlin.jvm.internal.CallableReference.NO_RECEIVER, owner, name, signature, flags);
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

    @Override // kotlin.jvm.internal.FunctionBase
    public int getArity() {
        return this.arity;
    }

    public kotlin.reflect.KDeclarationContainer getOwner() {
        if (this.owner == null) {
            return null;
        }
        return this.isTopLevel ? kotlin.jvm.internal.Reflection.getOrCreateKotlinPackage(this.owner) : kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(this.owner);
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof kotlin.jvm.internal.AdaptedFunctionReference)) {
            return false;
        }
        kotlin.jvm.internal.AdaptedFunctionReference other = (kotlin.jvm.internal.AdaptedFunctionReference) o;
        return this.isTopLevel == other.isTopLevel && this.arity == other.arity && this.flags == other.flags && kotlin.jvm.internal.Intrinsics.areEqual(this.receiver, other.receiver) && kotlin.jvm.internal.Intrinsics.areEqual(this.owner, other.owner) && this.name.equals(other.name) && this.signature.equals(other.signature);
    }

    public int hashCode() {
        int result = this.receiver != null ? this.receiver.hashCode() : 0;
        return (((((((((((result * 31) + (this.owner != null ? this.owner.hashCode() : 0)) * 31) + this.name.hashCode()) * 31) + this.signature.hashCode()) * 31) + (this.isTopLevel ? 1231 : 1237)) * 31) + this.arity) * 31) + this.flags;
    }

    public java.lang.String toString() {
        return kotlin.jvm.internal.Reflection.renderLambdaToString(this);
    }
}
