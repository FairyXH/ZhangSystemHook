package kotlin.collections.builders;

/* JADX INFO: compiled from: ListBuilder.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u0019\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0002J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lkotlin/collections/builders/SerializedCollection;", "Ljava/io/Externalizable;", "()V", "collection", "", "tag", "", "(Ljava/util/Collection;I)V", "readExternal", "", com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT, "Ljava/io/ObjectInput;", "readResolve", "", "writeExternal", "output", "Ljava/io/ObjectOutput;", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SerializedCollection implements java.io.Externalizable {
    private static final long serialVersionUID = 0;
    public static final int tagList = 0;
    public static final int tagSet = 1;
    private java.util.Collection<?> collection;
    private final int tag;

    public SerializedCollection(java.util.Collection<?> collection, int tag) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(collection, "collection");
        this.collection = collection;
        this.tag = tag;
    }

    public SerializedCollection() {
        this(kotlin.collections.CollectionsKt.emptyList(), 0);
    }

    @Override // java.io.Externalizable
    public void writeExternal(java.io.ObjectOutput output) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(output, "output");
        output.writeByte(this.tag);
        output.writeInt(this.collection.size());
        for (java.lang.Object element : this.collection) {
            output.writeObject(element);
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(java.io.ObjectInput input) throws java.io.InvalidObjectException {
        java.util.List listBuild;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(input, "input");
        int flags = input.readByte();
        int tag = flags & 1;
        int other = flags & (-2);
        if (other != 0) {
            throw new java.io.InvalidObjectException("Unsupported flags value: " + flags + '.');
        }
        int size = input.readInt();
        if (size < 0) {
            throw new java.io.InvalidObjectException("Illegal size value: " + size + '.');
        }
        int i = 0;
        switch (tag) {
            case 0:
                java.util.List $this$readExternal_u24lambda_u241 = kotlin.collections.CollectionsKt.createListBuilder(size);
                while (i < size) {
                    $this$readExternal_u24lambda_u241.add(input.readObject());
                    i++;
                }
                listBuild = kotlin.collections.CollectionsKt.build($this$readExternal_u24lambda_u241);
                break;
            case 1:
                java.util.Set $this$readExternal_u24lambda_u243 = kotlin.collections.SetsKt.createSetBuilder(size);
                while (i < size) {
                    $this$readExternal_u24lambda_u243.add(input.readObject());
                    i++;
                }
                listBuild = kotlin.collections.SetsKt.build($this$readExternal_u24lambda_u243);
                break;
            default:
                throw new java.io.InvalidObjectException("Unsupported collection type tag: " + tag + '.');
        }
        this.collection = listBuild;
    }

    private final java.lang.Object readResolve() {
        return this.collection;
    }
}
