package kotlin.collections;

/* JADX INFO: compiled from: Maps.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\u0010\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010&\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\u000e\n\u0000\bÂ\u0002\u0018\u00002\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00060\u0004j\u0002`\u0005B\u0007\b\u0002¢\u0006\u0002\u0010\u0006J\u0012\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u0002H\u0016J\u0010\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u0003H\u0016J\u0013\u0010\u001d\u001a\u00020\u00192\b\u0010\u001e\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\u0015\u0010\u001f\u001a\u0004\u0018\u00010\u00032\b\u0010\u001a\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010 \u001a\u00020\u0011H\u0016J\b\u0010!\u001a\u00020\u0019H\u0016J\b\u0010\"\u001a\u00020\u0002H\u0002J\b\u0010#\u001a\u00020$H\u0016R(\u0010\u0007\u001a\u0016\u0012\u0012\u0012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u00020\u00030\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001c\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\u00020\u00118VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u00158VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006%"}, d2 = {"Lkotlin/collections/EmptyMap;", "", "", "", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "entries", "", "", "getEntries", "()Ljava/util/Set;", "keys", "getKeys", "serialVersionUID", "", "size", "", "getSize", "()I", "values", "", "getValues", "()Ljava/util/Collection;", "containsKey", "", "key", "containsValue", "value", "equals", "other", "get", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "isEmpty", "readResolve", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class EmptyMap implements java.util.Map, java.io.Serializable, kotlin.jvm.internal.markers.KMappedMarker {
    public static final kotlin.collections.EmptyMap INSTANCE = new kotlin.collections.EmptyMap();
    private static final long serialVersionUID = 8246714829545688274L;

    @Override // java.util.Map
    public void clear() {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public /* bridge */ /* synthetic */ java.lang.Object put(java.lang.Object obj, java.lang.Object obj2) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public java.lang.Void put(java.lang.Object obj, java.lang.Void r4) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public void putAll(java.util.Map map) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override // java.util.Map
    public java.lang.Void remove(java.lang.Object obj) {
        throw new java.lang.UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    private EmptyMap() {
    }

    @Override // java.util.Map
    public final /* bridge */ boolean containsValue(java.lang.Object value) {
        if (value instanceof java.lang.Void) {
            return containsValue((java.lang.Void) value);
        }
        return false;
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Set<java.util.Map.Entry> entrySet() {
        return getEntries();
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Set<java.lang.Object> keySet() {
        return getKeys();
    }

    @Override // java.util.Map
    public final /* bridge */ int size() {
        return getSize();
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Collection values() {
        return getValues();
    }

    @Override // java.util.Map
    public boolean equals(java.lang.Object other) {
        return (other instanceof java.util.Map) && ((java.util.Map) other).isEmpty();
    }

    @Override // java.util.Map
    public int hashCode() {
        return 0;
    }

    public java.lang.String toString() {
        return "{}";
    }

    public int getSize() {
        return 0;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return true;
    }

    @Override // java.util.Map
    public boolean containsKey(java.lang.Object key) {
        return false;
    }

    public boolean containsValue(java.lang.Void value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        return false;
    }

    @Override // java.util.Map
    public java.lang.Void get(java.lang.Object key) {
        return null;
    }

    public java.util.Set<java.util.Map.Entry> getEntries() {
        return kotlin.collections.EmptySet.INSTANCE;
    }

    public java.util.Set<java.lang.Object> getKeys() {
        return kotlin.collections.EmptySet.INSTANCE;
    }

    public java.util.Collection getValues() {
        return kotlin.collections.EmptyList.INSTANCE;
    }

    private final java.lang.Object readResolve() {
        return INSTANCE;
    }
}
