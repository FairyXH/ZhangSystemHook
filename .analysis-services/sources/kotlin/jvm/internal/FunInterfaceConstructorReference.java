package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class FunInterfaceConstructorReference extends kotlin.jvm.internal.FunctionReference implements java.io.Serializable {
    private final java.lang.Class funInterface;

    public FunInterfaceConstructorReference(java.lang.Class funInterface) {
        super(1);
        this.funInterface = funInterface;
    }

    @Override // kotlin.jvm.internal.FunctionReference
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof kotlin.jvm.internal.FunInterfaceConstructorReference)) {
            return false;
        }
        kotlin.jvm.internal.FunInterfaceConstructorReference other = (kotlin.jvm.internal.FunInterfaceConstructorReference) o;
        return this.funInterface.equals(other.funInterface);
    }

    @Override // kotlin.jvm.internal.FunctionReference
    public int hashCode() {
        return this.funInterface.hashCode();
    }

    @Override // kotlin.jvm.internal.FunctionReference
    public java.lang.String toString() {
        return "fun interface " + this.funInterface.getName();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // kotlin.jvm.internal.FunctionReference, kotlin.jvm.internal.CallableReference
    public kotlin.reflect.KFunction getReflected() {
        throw new java.lang.UnsupportedOperationException("Functional interface constructor does not support reflection");
    }
}
