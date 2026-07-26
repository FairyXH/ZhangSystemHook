package kotlin.collections.builders;

/* JADX INFO: compiled from: MapBuilder.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u0015\u0012\u000e\u0010\u0003\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0002J\u0010\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000eH\u0016R\u0016\u0010\u0003\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlin/collections/builders/SerializedMap;", "Ljava/io/Externalizable;", "()V", "map", "", "(Ljava/util/Map;)V", "readExternal", "", com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT, "Ljava/io/ObjectInput;", "readResolve", "", "writeExternal", "output", "Ljava/io/ObjectOutput;", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class SerializedMap implements java.io.Externalizable {
    private static final long serialVersionUID = 0;
    private java.util.Map<?, ?> map;

    public SerializedMap(java.util.Map<?, ?> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        this.map = map;
    }

    public SerializedMap() {
        this(kotlin.collections.MapsKt.emptyMap());
    }

    @Override // java.io.Externalizable
    public void writeExternal(java.io.ObjectOutput output) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(output, "output");
        output.writeByte(0);
        output.writeInt(this.map.size());
        for (java.util.Map.Entry<?, ?> entry : this.map.entrySet()) {
            output.writeObject(entry.getKey());
            output.writeObject(entry.getValue());
        }
    }

    @Override // java.io.Externalizable
    public void readExternal(java.io.ObjectInput input) throws java.lang.ClassNotFoundException, java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(input, "input");
        int flags = input.readByte();
        if (flags != 0) {
            throw new java.io.InvalidObjectException("Unsupported flags value: " + flags);
        }
        int size = input.readInt();
        if (size < 0) {
            throw new java.io.InvalidObjectException("Illegal size value: " + size + '.');
        }
        java.util.Map $this$readExternal_u24lambda_u241 = kotlin.collections.MapsKt.createMapBuilder(size);
        for (int i = 0; i < size; i++) {
            java.lang.Object key = input.readObject();
            java.lang.Object value = input.readObject();
            $this$readExternal_u24lambda_u241.put(key, value);
        }
        this.map = kotlin.collections.MapsKt.build($this$readExternal_u24lambda_u241);
    }

    private final java.lang.Object readResolve() {
        return this.map;
    }
}
