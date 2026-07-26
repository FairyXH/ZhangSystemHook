package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public abstract class MutablePropertyReference extends kotlin.jvm.internal.PropertyReference implements kotlin.reflect.KMutableProperty {
    public MutablePropertyReference() {
    }

    public MutablePropertyReference(java.lang.Object receiver) {
        super(receiver);
    }

    public MutablePropertyReference(java.lang.Object receiver, java.lang.Class owner, java.lang.String name, java.lang.String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }
}
