package kotlin.collections.builders;

/* JADX INFO: compiled from: MapBuilder.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000¨\u0001\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\b\n\u0002\u0010#\n\u0002\u0010'\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u001e\n\u0002\b\u0003\n\u0002\u0010&\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u001b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0000\u0018\u0000 \u0080\u0001*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\u00060\u0004j\u0002`\u0005:\f\u0080\u0001\u0081\u0001\u0082\u0001\u0083\u0001\u0084\u0001\u0085\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0006B\u000f\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tBE\b\u0002\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000b\u0012\u000e\u0010\f\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u000b\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u000e\u0012\u0006\u0010\u0010\u001a\u00020\b\u0012\u0006\u0010\u0011\u001a\u00020\b¢\u0006\u0002\u0010\u0012J\u0017\u00103\u001a\u00020\b2\u0006\u00104\u001a\u00028\u0000H\u0000¢\u0006\u0004\b5\u00106J\u0013\u00107\u001a\b\u0012\u0004\u0012\u00028\u00010\u000bH\u0002¢\u0006\u0002\u00108J\u0012\u00109\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010:J\r\u0010;\u001a\u00020<H\u0000¢\u0006\u0002\b=J\b\u0010>\u001a\u00020<H\u0016J\b\u0010?\u001a\u00020<H\u0002J\u0019\u0010@\u001a\u00020!2\n\u0010A\u001a\u0006\u0012\u0002\b\u00030BH\u0000¢\u0006\u0002\bCJ!\u0010D\u001a\u00020!2\u0012\u0010E\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010FH\u0000¢\u0006\u0002\bGJ\u0015\u0010H\u001a\u00020!2\u0006\u00104\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010IJ\u0015\u0010J\u001a\u00020!2\u0006\u0010K\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010IJ\u0018\u0010L\u001a\u00020!2\u000e\u0010M\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030:H\u0002J\u0010\u0010N\u001a\u00020<2\u0006\u0010O\u001a\u00020\bH\u0002J\u0010\u0010P\u001a\u00020<2\u0006\u0010Q\u001a\u00020\bH\u0002J\u0019\u0010R\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010SH\u0000¢\u0006\u0002\bTJ\u0013\u0010U\u001a\u00020!2\b\u0010M\u001a\u0004\u0018\u00010VH\u0096\u0002J\u0015\u0010W\u001a\u00020\b2\u0006\u00104\u001a\u00028\u0000H\u0002¢\u0006\u0002\u00106J\u0015\u0010X\u001a\u00020\b2\u0006\u0010K\u001a\u00028\u0001H\u0002¢\u0006\u0002\u00106J\u0018\u0010Y\u001a\u0004\u0018\u00018\u00012\u0006\u00104\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010ZJ\u0015\u0010[\u001a\u00020\b2\u0006\u00104\u001a\u00028\u0000H\u0002¢\u0006\u0002\u00106J\b\u0010\\\u001a\u00020\bH\u0016J\b\u0010]\u001a\u00020!H\u0016J\u0019\u0010^\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010_H\u0000¢\u0006\u0002\b`J\u001f\u0010a\u001a\u0004\u0018\u00018\u00012\u0006\u00104\u001a\u00028\u00002\u0006\u0010K\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010bJ\u001e\u0010c\u001a\u00020<2\u0014\u0010d\u001a\u0010\u0012\u0006\b\u0001\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010:H\u0016J\"\u0010e\u001a\u00020!2\u0018\u0010d\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010F0BH\u0002J\u001c\u0010f\u001a\u00020!2\u0012\u0010E\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010FH\u0002J\u0010\u0010g\u001a\u00020!2\u0006\u0010h\u001a\u00020\bH\u0002J\b\u0010i\u001a\u00020<H\u0002J\u0010\u0010j\u001a\u00020<2\u0006\u0010k\u001a\u00020\bH\u0002J\u0017\u0010l\u001a\u0004\u0018\u00018\u00012\u0006\u00104\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010ZJ!\u0010m\u001a\u00020!2\u0012\u0010E\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010FH\u0000¢\u0006\u0002\bnJ\u0010\u0010o\u001a\u00020<2\u0006\u0010p\u001a\u00020\bH\u0002J\u0017\u0010q\u001a\u00020\b2\u0006\u00104\u001a\u00028\u0000H\u0000¢\u0006\u0004\br\u00106J\u0010\u0010s\u001a\u00020<2\u0006\u0010t\u001a\u00020\bH\u0002J\u0017\u0010u\u001a\u00020!2\u0006\u0010v\u001a\u00028\u0001H\u0000¢\u0006\u0004\bw\u0010IJ\u0010\u0010x\u001a\u00020!2\u0006\u0010y\u001a\u00020\bH\u0002J\b\u0010z\u001a\u00020{H\u0016J\u0019\u0010|\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010}H\u0000¢\u0006\u0002\b~J\b\u0010\u007f\u001a\u00020VH\u0002R\u0014\u0010\u0013\u001a\u00020\b8@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R&\u0010\u0016\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00180\u00178VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u001aR\u001c\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u001cX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001e\u001a\u00020\b8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u0015R\u001e\u0010\"\u001a\u00020!2\u0006\u0010 \u001a\u00020!@BX\u0080\u000e¢\u0006\b\n\u0000\u001a\u0004\b#\u0010$R\u001a\u0010%\u001a\b\u0012\u0004\u0012\u00028\u00000\u00178VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b&\u0010\u001aR\u0016\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u000bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010'R\u0016\u0010(\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010)X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010+\u001a\u00020\b2\u0006\u0010 \u001a\u00020\b@RX\u0096\u000e¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0015R\u001a\u0010-\u001a\b\u0012\u0004\u0012\u00028\u00010.8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b/\u00100R\u0018\u0010\f\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u000bX\u0082\u000e¢\u0006\u0004\n\u0002\u0010'R\u0016\u00101\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u000102X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0086\u0001"}, d2 = {"Lkotlin/collections/builders/MapBuilder;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "initialCapacity", "", "(I)V", "keysArray", "", "valuesArray", "presenceArray", "", "hashArray", "maxProbeDistance", "length", "([Ljava/lang/Object;[Ljava/lang/Object;[I[III)V", "capacity", "getCapacity$kotlin_stdlib", "()I", "entries", "", "", "getEntries", "()Ljava/util/Set;", "entriesView", "Lkotlin/collections/builders/MapBuilderEntries;", "hashShift", "hashSize", "getHashSize", "<set-?>", "", "isReadOnly", "isReadOnly$kotlin_stdlib", "()Z", "keys", "getKeys", "[Ljava/lang/Object;", "keysView", "Lkotlin/collections/builders/MapBuilderKeys;", "modCount", "size", "getSize", "values", "", "getValues", "()Ljava/util/Collection;", "valuesView", "Lkotlin/collections/builders/MapBuilderValues;", "addKey", "key", "addKey$kotlin_stdlib", "(Ljava/lang/Object;)I", "allocateValuesArray", "()[Ljava/lang/Object;", "build", "", "checkIsMutable", "", "checkIsMutable$kotlin_stdlib", "clear", "compact", "containsAllEntries", "m", "", "containsAllEntries$kotlin_stdlib", "containsEntry", "entry", "", "containsEntry$kotlin_stdlib", "containsKey", "(Ljava/lang/Object;)Z", "containsValue", "value", "contentEquals", "other", "ensureCapacity", "minCapacity", "ensureExtraCapacity", "n", "entriesIterator", "Lkotlin/collections/builders/MapBuilder$EntriesItr;", "entriesIterator$kotlin_stdlib", "equals", "", "findKey", "findValue", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "hash", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "isEmpty", "keysIterator", "Lkotlin/collections/builders/MapBuilder$KeysItr;", "keysIterator$kotlin_stdlib", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "putAll", "from", "putAllEntries", "putEntry", "putRehash", "i", "registerModification", "rehash", "newHashSize", "remove", "removeEntry", "removeEntry$kotlin_stdlib", "removeHashAt", "removedHash", "removeKey", "removeKey$kotlin_stdlib", "removeKeyAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "removeValue", "element", "removeValue$kotlin_stdlib", "shouldCompact", "extraCapacity", "toString", "", "valuesIterator", "Lkotlin/collections/builders/MapBuilder$ValuesItr;", "valuesIterator$kotlin_stdlib", "writeReplace", "Companion", "EntriesItr", "EntryRef", "Itr", "KeysItr", "ValuesItr", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MapBuilder<K, V> implements java.util.Map<K, V>, java.io.Serializable, kotlin.jvm.internal.markers.KMutableMap {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.collections.builders.MapBuilder.Companion INSTANCE = new kotlin.collections.builders.MapBuilder.Companion(null);
    private static final kotlin.collections.builders.MapBuilder Empty;
    private static final int INITIAL_CAPACITY = 8;
    private static final int INITIAL_MAX_PROBE_DISTANCE = 2;
    private static final int MAGIC = -1640531527;
    private static final int TOMBSTONE = -1;
    private kotlin.collections.builders.MapBuilderEntries<K, V> entriesView;
    private int[] hashArray;
    private int hashShift;
    private boolean isReadOnly;
    private K[] keysArray;
    private kotlin.collections.builders.MapBuilderKeys<K> keysView;
    private int length;
    private int maxProbeDistance;
    private int modCount;
    private int[] presenceArray;
    private int size;
    private V[] valuesArray;
    private kotlin.collections.builders.MapBuilderValues<V> valuesView;

    private MapBuilder(K[] kArr, V[] vArr, int[] presenceArray, int[] hashArray, int maxProbeDistance, int length) {
        this.keysArray = kArr;
        this.valuesArray = vArr;
        this.presenceArray = presenceArray;
        this.hashArray = hashArray;
        this.maxProbeDistance = maxProbeDistance;
        this.length = length;
        this.hashShift = INSTANCE.computeShift(getHashSize());
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Set<java.util.Map.Entry<K, V>> entrySet() {
        return getEntries();
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Set<K> keySet() {
        return getKeys();
    }

    @Override // java.util.Map
    public final /* bridge */ int size() {
        return getSize();
    }

    @Override // java.util.Map
    public final /* bridge */ java.util.Collection<V> values() {
        return getValues();
    }

    public int getSize() {
        return this.size;
    }

    /* JADX INFO: renamed from: isReadOnly$kotlin_stdlib, reason: from getter */
    public final boolean getIsReadOnly() {
        return this.isReadOnly;
    }

    public MapBuilder() {
        this(8);
    }

    public MapBuilder(int initialCapacity) {
        this(kotlin.collections.builders.ListBuilderKt.arrayOfUninitializedElements(initialCapacity), null, new int[initialCapacity], new int[INSTANCE.computeHashSize(initialCapacity)], 2, 0);
    }

    public final java.util.Map<K, V> build() {
        checkIsMutable$kotlin_stdlib();
        this.isReadOnly = true;
        if (size() > 0) {
            return this;
        }
        kotlin.collections.builders.MapBuilder mapBuilder = Empty;
        kotlin.jvm.internal.Intrinsics.checkNotNull(mapBuilder, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.builders.MapBuilder, V of kotlin.collections.builders.MapBuilder>");
        return mapBuilder;
    }

    private final java.lang.Object writeReplace() throws java.io.NotSerializableException {
        if (this.isReadOnly) {
            return new kotlin.collections.builders.SerializedMap(this);
        }
        throw new java.io.NotSerializableException("The map cannot be serialized while it is being built.");
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public boolean containsKey(java.lang.Object key) {
        return findKey(key) >= 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public boolean containsValue(java.lang.Object value) {
        return findValue(value) >= 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V get(java.lang.Object key) {
        int index = findKey(key);
        if (index < 0) {
            return null;
        }
        V[] vArr = this.valuesArray;
        kotlin.jvm.internal.Intrinsics.checkNotNull(vArr);
        return vArr[index];
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        checkIsMutable$kotlin_stdlib();
        int index = addKey$kotlin_stdlib(key);
        V[] vArrAllocateValuesArray = allocateValuesArray();
        if (index < 0) {
            V v = vArrAllocateValuesArray[(-index) - 1];
            vArrAllocateValuesArray[(-index) - 1] = value;
            return v;
        }
        vArrAllocateValuesArray[index] = value;
        return null;
    }

    @Override // java.util.Map
    public void putAll(java.util.Map<? extends K, ? extends V> from) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(from, "from");
        checkIsMutable$kotlin_stdlib();
        putAllEntries(from.entrySet());
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public V remove(java.lang.Object key) {
        int index = removeKey$kotlin_stdlib(key);
        if (index < 0) {
            return null;
        }
        V[] vArr = this.valuesArray;
        kotlin.jvm.internal.Intrinsics.checkNotNull(vArr);
        V v = vArr[index];
        kotlin.collections.builders.ListBuilderKt.resetAt(vArr, index);
        return v;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [kotlin.collections.IntIterator] */
    @Override // java.util.Map
    public void clear() {
        checkIsMutable$kotlin_stdlib();
        ?? it = new kotlin.ranges.IntRange(0, this.length - 1).iterator();
        while (it.hasNext()) {
            int i = it.nextInt();
            int hash = this.presenceArray[i];
            if (hash >= 0) {
                this.hashArray[hash] = 0;
                this.presenceArray[i] = -1;
            }
        }
        kotlin.collections.builders.ListBuilderKt.resetRange(this.keysArray, 0, this.length);
        V[] vArr = this.valuesArray;
        if (vArr != null) {
            kotlin.collections.builders.ListBuilderKt.resetRange(vArr, 0, this.length);
        }
        this.size = 0;
        this.length = 0;
        registerModification();
    }

    public java.util.Set<K> getKeys() {
        kotlin.collections.builders.MapBuilderKeys<K> mapBuilderKeys = this.keysView;
        if (mapBuilderKeys == null) {
            kotlin.collections.builders.MapBuilderKeys<K> mapBuilderKeys2 = new kotlin.collections.builders.MapBuilderKeys<>(this);
            this.keysView = mapBuilderKeys2;
            return mapBuilderKeys2;
        }
        return mapBuilderKeys;
    }

    public java.util.Collection<V> getValues() {
        kotlin.collections.builders.MapBuilderValues<V> mapBuilderValues = this.valuesView;
        if (mapBuilderValues == null) {
            kotlin.collections.builders.MapBuilderValues<V> mapBuilderValues2 = new kotlin.collections.builders.MapBuilderValues<>(this);
            this.valuesView = mapBuilderValues2;
            return mapBuilderValues2;
        }
        return mapBuilderValues;
    }

    public java.util.Set<java.util.Map.Entry<K, V>> getEntries() {
        kotlin.collections.builders.MapBuilderEntries<K, V> mapBuilderEntries = this.entriesView;
        if (mapBuilderEntries == null) {
            kotlin.collections.builders.MapBuilderEntries<K, V> mapBuilderEntries2 = new kotlin.collections.builders.MapBuilderEntries<>(this);
            this.entriesView = mapBuilderEntries2;
            return mapBuilderEntries2;
        }
        return mapBuilderEntries;
    }

    @Override // java.util.Map
    public boolean equals(java.lang.Object other) {
        return other == this || ((other instanceof java.util.Map) && contentEquals((java.util.Map) other));
    }

    @Override // java.util.Map
    public int hashCode() {
        int result = 0;
        kotlin.collections.builders.MapBuilder.EntriesItr<K, V> entriesItrEntriesIterator$kotlin_stdlib = entriesIterator$kotlin_stdlib();
        while (entriesItrEntriesIterator$kotlin_stdlib.hasNext()) {
            result += entriesItrEntriesIterator$kotlin_stdlib.nextHashCode$kotlin_stdlib();
        }
        return result;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder((size() * 3) + 2);
        sb.append("{");
        int i = 0;
        kotlin.collections.builders.MapBuilder.EntriesItr<K, V> entriesItrEntriesIterator$kotlin_stdlib = entriesIterator$kotlin_stdlib();
        while (entriesItrEntriesIterator$kotlin_stdlib.hasNext()) {
            if (i > 0) {
                sb.append(", ");
            }
            entriesItrEntriesIterator$kotlin_stdlib.nextAppendString(sb);
            i++;
        }
        sb.append("}");
        java.lang.String string = sb.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public final int getCapacity$kotlin_stdlib() {
        return this.keysArray.length;
    }

    private final int getHashSize() {
        return this.hashArray.length;
    }

    private final void registerModification() {
        this.modCount++;
    }

    public final void checkIsMutable$kotlin_stdlib() {
        if (this.isReadOnly) {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    private final void ensureExtraCapacity(int n) {
        if (shouldCompact(n)) {
            rehash(getHashSize());
        } else {
            ensureCapacity(this.length + n);
        }
    }

    private final boolean shouldCompact(int extraCapacity) {
        int spareCapacity = getCapacity$kotlin_stdlib() - this.length;
        int gaps = this.length - size();
        return spareCapacity < extraCapacity && gaps + spareCapacity >= extraCapacity && gaps >= getCapacity$kotlin_stdlib() / 4;
    }

    private final void ensureCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new java.lang.OutOfMemoryError();
        }
        if (minCapacity > getCapacity$kotlin_stdlib()) {
            int iNewCapacity$kotlin_stdlib = kotlin.collections.AbstractList.INSTANCE.newCapacity$kotlin_stdlib(getCapacity$kotlin_stdlib(), minCapacity);
            this.keysArray = (K[]) kotlin.collections.builders.ListBuilderKt.copyOfUninitializedElements(this.keysArray, iNewCapacity$kotlin_stdlib);
            V[] vArr = this.valuesArray;
            this.valuesArray = vArr != null ? (V[]) kotlin.collections.builders.ListBuilderKt.copyOfUninitializedElements(vArr, iNewCapacity$kotlin_stdlib) : null;
            int[] iArrCopyOf = java.util.Arrays.copyOf(this.presenceArray, iNewCapacity$kotlin_stdlib);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(iArrCopyOf, "copyOf(...)");
            this.presenceArray = iArrCopyOf;
            int iComputeHashSize = INSTANCE.computeHashSize(iNewCapacity$kotlin_stdlib);
            if (iComputeHashSize > getHashSize()) {
                rehash(iComputeHashSize);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final V[] allocateValuesArray() {
        V[] vArr = this.valuesArray;
        if (vArr != null) {
            return vArr;
        }
        V[] vArr2 = (V[]) kotlin.collections.builders.ListBuilderKt.arrayOfUninitializedElements(getCapacity$kotlin_stdlib());
        this.valuesArray = vArr2;
        return vArr2;
    }

    private final int hash(K key) {
        return ((key != null ? key.hashCode() : 0) * MAGIC) >>> this.hashShift;
    }

    private final void compact() {
        int j = 0;
        java.lang.Object[] valuesArray = this.valuesArray;
        for (int i = 0; i < this.length; i++) {
            if (this.presenceArray[i] >= 0) {
                this.keysArray[j] = this.keysArray[i];
                if (valuesArray != null) {
                    valuesArray[j] = valuesArray[i];
                }
                j++;
            }
        }
        kotlin.collections.builders.ListBuilderKt.resetRange(this.keysArray, j, this.length);
        if (valuesArray != null) {
            kotlin.collections.builders.ListBuilderKt.resetRange(valuesArray, j, this.length);
        }
        this.length = j;
    }

    private final void rehash(int newHashSize) {
        registerModification();
        if (this.length > size()) {
            compact();
        }
        if (newHashSize != getHashSize()) {
            this.hashArray = new int[newHashSize];
            this.hashShift = INSTANCE.computeShift(newHashSize);
        } else {
            kotlin.collections.ArraysKt.fill(this.hashArray, 0, 0, getHashSize());
        }
        int i = 0;
        while (i < this.length) {
            int i2 = i + 1;
            if (!putRehash(i)) {
                throw new java.lang.IllegalStateException("This cannot happen with fixed magic multiplier and grow-only hash array. Have object hashCodes changed?");
            }
            i = i2;
        }
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0010 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final boolean putRehash(int r7) {
        /*
            r6 = this;
            K[] r0 = r6.keysArray
            r0 = r0[r7]
            int r0 = r6.hash(r0)
            int r1 = r6.maxProbeDistance
        La:
            int[] r2 = r6.hashArray
            r2 = r2[r0]
            r3 = 1
            if (r2 != 0) goto L1d
            int[] r4 = r6.hashArray
            int r5 = r7 + 1
            r4[r0] = r5
            int[] r4 = r6.presenceArray
            r4[r7] = r0
            return r3
        L1d:
            int r1 = r1 + (-1)
            if (r1 >= 0) goto L23
            r3 = 0
            return r3
        L23:
            int r4 = r0 + (-1)
            if (r0 != 0) goto L2d
            int r0 = r6.getHashSize()
            int r0 = r0 - r3
            goto La
        L2d:
            r0 = r4
            goto La
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.builders.MapBuilder.putRehash(int):boolean");
    }

    private final int findKey(K key) {
        int hash = hash(key);
        int probesLeft = this.maxProbeDistance;
        while (true) {
            int index = this.hashArray[hash];
            if (index == 0) {
                return -1;
            }
            if (index > 0 && kotlin.jvm.internal.Intrinsics.areEqual(this.keysArray[index - 1], key)) {
                return index - 1;
            }
            probesLeft--;
            if (probesLeft < 0) {
                return -1;
            }
            hash = hash == 0 ? getHashSize() - 1 : hash - 1;
        }
    }

    private final int findValue(V value) {
        int i = this.length;
        while (true) {
            i--;
            if (i < 0) {
                return -1;
            }
            if (this.presenceArray[i] >= 0) {
                V[] vArr = this.valuesArray;
                kotlin.jvm.internal.Intrinsics.checkNotNull(vArr);
                if (kotlin.jvm.internal.Intrinsics.areEqual(vArr[i], value)) {
                    return i;
                }
            }
        }
    }

    public final int addKey$kotlin_stdlib(K key) {
        checkIsMutable$kotlin_stdlib();
        while (true) {
            int hash = hash(key);
            int tentativeMaxProbeDistance = kotlin.ranges.RangesKt.coerceAtMost(this.maxProbeDistance * 2, getHashSize() / 2);
            int probeDistance = 0;
            while (true) {
                int index = this.hashArray[hash];
                if (index <= 0) {
                    if (this.length >= getCapacity$kotlin_stdlib()) {
                        ensureExtraCapacity(1);
                    } else {
                        int putIndex = this.length;
                        this.length = putIndex + 1;
                        this.keysArray[putIndex] = key;
                        this.presenceArray[putIndex] = hash;
                        this.hashArray[hash] = putIndex + 1;
                        this.size = size() + 1;
                        registerModification();
                        if (probeDistance > this.maxProbeDistance) {
                            this.maxProbeDistance = probeDistance;
                        }
                        return putIndex;
                    }
                } else {
                    if (kotlin.jvm.internal.Intrinsics.areEqual(this.keysArray[index - 1], key)) {
                        return -index;
                    }
                    probeDistance++;
                    if (probeDistance <= tentativeMaxProbeDistance) {
                        hash = hash == 0 ? getHashSize() - 1 : hash - 1;
                    } else {
                        rehash(getHashSize() * 2);
                        break;
                    }
                }
            }
        }
    }

    public final int removeKey$kotlin_stdlib(K key) {
        checkIsMutable$kotlin_stdlib();
        int index = findKey(key);
        if (index < 0) {
            return -1;
        }
        removeKeyAt(index);
        return index;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeKeyAt(int index) {
        kotlin.collections.builders.ListBuilderKt.resetAt(this.keysArray, index);
        removeHashAt(this.presenceArray[index]);
        this.presenceArray[index] = -1;
        this.size = size() - 1;
        registerModification();
    }

    private final void removeHashAt(int removedHash) {
        int hash = removedHash;
        int hole = removedHash;
        int probeDistance = 0;
        int patchAttemptsLeft = kotlin.ranges.RangesKt.coerceAtMost(this.maxProbeDistance * 2, getHashSize() / 2);
        do {
            hash = hash == 0 ? getHashSize() - 1 : hash - 1;
            probeDistance++;
            if (probeDistance > this.maxProbeDistance) {
                this.hashArray[hole] = 0;
                return;
            }
            int index = this.hashArray[hash];
            if (index == 0) {
                this.hashArray[hole] = 0;
                return;
            }
            if (index < 0) {
                this.hashArray[hole] = -1;
                hole = hash;
                probeDistance = 0;
            } else {
                int otherHash = hash(this.keysArray[index - 1]);
                if (((otherHash - hash) & (getHashSize() - 1)) >= probeDistance) {
                    this.hashArray[hole] = index;
                    this.presenceArray[index - 1] = hole;
                    hole = hash;
                    probeDistance = 0;
                }
            }
            patchAttemptsLeft--;
        } while (patchAttemptsLeft >= 0);
        this.hashArray[hole] = -1;
    }

    public final boolean containsEntry$kotlin_stdlib(java.util.Map.Entry<? extends K, ? extends V> entry) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(entry, "entry");
        int index = findKey(entry.getKey());
        if (index < 0) {
            return false;
        }
        V[] vArr = this.valuesArray;
        kotlin.jvm.internal.Intrinsics.checkNotNull(vArr);
        return kotlin.jvm.internal.Intrinsics.areEqual(vArr[index], entry.getValue());
    }

    private final boolean contentEquals(java.util.Map<?, ?> other) {
        return size() == other.size() && containsAllEntries$kotlin_stdlib(other.entrySet());
    }

    public final boolean containsAllEntries$kotlin_stdlib(java.util.Collection<?> m) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "m");
        for (java.lang.Object entry : m) {
            if (entry != null) {
                try {
                    if (!containsEntry$kotlin_stdlib((java.util.Map.Entry) entry)) {
                    }
                } catch (java.lang.ClassCastException e) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    private final boolean putEntry(java.util.Map.Entry<? extends K, ? extends V> entry) {
        int index = addKey$kotlin_stdlib(entry.getKey());
        java.lang.Object[] valuesArray = allocateValuesArray();
        if (index < 0) {
            java.lang.Object oldValue = valuesArray[(-index) - 1];
            if (!kotlin.jvm.internal.Intrinsics.areEqual(entry.getValue(), oldValue)) {
                valuesArray[(-index) - 1] = entry.getValue();
                return true;
            }
            return false;
        }
        valuesArray[index] = entry.getValue();
        return true;
    }

    private final boolean putAllEntries(java.util.Collection<? extends java.util.Map.Entry<? extends K, ? extends V>> from) {
        if (from.isEmpty()) {
            return false;
        }
        ensureExtraCapacity(from.size());
        java.util.Iterator<? extends java.util.Map.Entry<? extends K, ? extends V>> it = from.iterator();
        boolean updated = false;
        while (it.hasNext()) {
            if (putEntry(it.next())) {
                updated = true;
            }
        }
        return updated;
    }

    public final boolean removeEntry$kotlin_stdlib(java.util.Map.Entry<? extends K, ? extends V> entry) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(entry, "entry");
        checkIsMutable$kotlin_stdlib();
        int index = findKey(entry.getKey());
        if (index < 0) {
            return false;
        }
        V[] vArr = this.valuesArray;
        kotlin.jvm.internal.Intrinsics.checkNotNull(vArr);
        if (!kotlin.jvm.internal.Intrinsics.areEqual(vArr[index], entry.getValue())) {
            return false;
        }
        removeKeyAt(index);
        return true;
    }

    public final boolean removeValue$kotlin_stdlib(V element) {
        checkIsMutable$kotlin_stdlib();
        int index = findValue(element);
        if (index < 0) {
            return false;
        }
        removeKeyAt(index);
        return true;
    }

    public final kotlin.collections.builders.MapBuilder.KeysItr<K, V> keysIterator$kotlin_stdlib() {
        return new kotlin.collections.builders.MapBuilder.KeysItr<>(this);
    }

    public final kotlin.collections.builders.MapBuilder.ValuesItr<K, V> valuesIterator$kotlin_stdlib() {
        return new kotlin.collections.builders.MapBuilder.ValuesItr<>(this);
    }

    public final kotlin.collections.builders.MapBuilder.EntriesItr<K, V> entriesIterator$kotlin_stdlib() {
        return new kotlin.collections.builders.MapBuilder.EntriesItr<>(this);
    }

    /* JADX INFO: compiled from: MapBuilder.kt */
    @kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0001\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\b\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\tH\u0002J\u0010\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\tH\u0002R \u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u000e\u0010\b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lkotlin/collections/builders/MapBuilder$Companion;", "", "()V", "Empty", "Lkotlin/collections/builders/MapBuilder;", "", "getEmpty$kotlin_stdlib", "()Lkotlin/collections/builders/MapBuilder;", "INITIAL_CAPACITY", "", "INITIAL_MAX_PROBE_DISTANCE", "MAGIC", "TOMBSTONE", "computeHashSize", "capacity", "computeShift", "hashSize", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final kotlin.collections.builders.MapBuilder getEmpty$kotlin_stdlib() {
            return kotlin.collections.builders.MapBuilder.Empty;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int computeHashSize(int capacity) {
            return java.lang.Integer.highestOneBit(kotlin.ranges.RangesKt.coerceAtLeast(capacity, 1) * 3);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final int computeShift(int hashSize) {
            return java.lang.Integer.numberOfLeadingZeros(hashSize) + 1;
        }
    }

    static {
        kotlin.collections.builders.MapBuilder it = new kotlin.collections.builders.MapBuilder(0);
        it.isReadOnly = true;
        Empty = it;
    }

    /* JADX INFO: compiled from: MapBuilder.kt */
    @kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0010\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u00020\u0003B\u0019\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005¢\u0006\u0002\u0010\u0006J\r\u0010\u0013\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\u0015J\u0006\u0010\u0016\u001a\u00020\u0017J\r\u0010\u0018\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\u0019J\u0006\u0010\u001a\u001a\u00020\u0014R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u00020\bX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\bX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\rR \u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012¨\u0006\u001b"}, d2 = {"Lkotlin/collections/builders/MapBuilder$Itr;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "expectedModCount", "", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "getIndex$kotlin_stdlib", "()I", "setIndex$kotlin_stdlib", "(I)V", "lastIndex", "getLastIndex$kotlin_stdlib", "setLastIndex$kotlin_stdlib", "getMap$kotlin_stdlib", "()Lkotlin/collections/builders/MapBuilder;", "checkForComodification", "", "checkForComodification$kotlin_stdlib", "hasNext", "", "initNext", "initNext$kotlin_stdlib", "remove", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static class Itr<K, V> {
        private int expectedModCount;
        private int index;
        private int lastIndex;
        private final kotlin.collections.builders.MapBuilder<K, V> map;

        public Itr(kotlin.collections.builders.MapBuilder<K, V> map) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
            this.map = map;
            this.lastIndex = -1;
            this.expectedModCount = ((kotlin.collections.builders.MapBuilder) this.map).modCount;
            initNext$kotlin_stdlib();
        }

        public final kotlin.collections.builders.MapBuilder<K, V> getMap$kotlin_stdlib() {
            return this.map;
        }

        /* JADX INFO: renamed from: getIndex$kotlin_stdlib, reason: from getter */
        public final int getIndex() {
            return this.index;
        }

        public final void setIndex$kotlin_stdlib(int i) {
            this.index = i;
        }

        /* JADX INFO: renamed from: getLastIndex$kotlin_stdlib, reason: from getter */
        public final int getLastIndex() {
            return this.lastIndex;
        }

        public final void setLastIndex$kotlin_stdlib(int i) {
            this.lastIndex = i;
        }

        public final void initNext$kotlin_stdlib() {
            while (this.index < ((kotlin.collections.builders.MapBuilder) this.map).length && ((kotlin.collections.builders.MapBuilder) this.map).presenceArray[this.index] < 0) {
                this.index++;
            }
        }

        public final boolean hasNext() {
            return this.index < ((kotlin.collections.builders.MapBuilder) this.map).length;
        }

        public final void remove() {
            checkForComodification$kotlin_stdlib();
            if (!(this.lastIndex != -1)) {
                throw new java.lang.IllegalStateException("Call next() before removing element from the iterator.".toString());
            }
            this.map.checkIsMutable$kotlin_stdlib();
            this.map.removeKeyAt(this.lastIndex);
            this.lastIndex = -1;
            this.expectedModCount = ((kotlin.collections.builders.MapBuilder) this.map).modCount;
        }

        public final void checkForComodification$kotlin_stdlib() {
            if (((kotlin.collections.builders.MapBuilder) this.map).modCount != this.expectedModCount) {
                throw new java.util.ConcurrentModificationException();
            }
        }
    }

    /* JADX INFO: compiled from: MapBuilder.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00010\u0004B\u0019\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0006¢\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00028\u0002H\u0096\u0002¢\u0006\u0002\u0010\t¨\u0006\n"}, d2 = {"Lkotlin/collections/builders/MapBuilder$KeysItr;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/collections/builders/MapBuilder$Itr;", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class KeysItr<K, V> extends kotlin.collections.builders.MapBuilder.Itr<K, V> implements java.util.Iterator<K>, kotlin.jvm.internal.markers.KMutableIterator {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public KeysItr(kotlin.collections.builders.MapBuilder<K, V> map) {
            super(map);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        }

        @Override // java.util.Iterator
        public K next() {
            checkForComodification$kotlin_stdlib();
            if (getIndex() >= ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).length) {
                throw new java.util.NoSuchElementException();
            }
            int index$kotlin_stdlib = getIndex();
            setIndex$kotlin_stdlib(index$kotlin_stdlib + 1);
            setLastIndex$kotlin_stdlib(index$kotlin_stdlib);
            K k = (K) ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).keysArray[getLastIndex()];
            initNext$kotlin_stdlib();
            return k;
        }
    }

    /* JADX INFO: compiled from: MapBuilder.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\b\u0012\u0004\u0012\u0002H\u00020\u0004B\u0019\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0006¢\u0006\u0002\u0010\u0007J\u000e\u0010\b\u001a\u00028\u0003H\u0096\u0002¢\u0006\u0002\u0010\t¨\u0006\n"}, d2 = {"Lkotlin/collections/builders/MapBuilder$ValuesItr;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/collections/builders/MapBuilder$Itr;", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class ValuesItr<K, V> extends kotlin.collections.builders.MapBuilder.Itr<K, V> implements java.util.Iterator<V>, kotlin.jvm.internal.markers.KMutableIterator {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ValuesItr(kotlin.collections.builders.MapBuilder<K, V> map) {
            super(map);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        }

        @Override // java.util.Iterator
        public V next() {
            checkForComodification$kotlin_stdlib();
            if (getIndex() >= ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).length) {
                throw new java.util.NoSuchElementException();
            }
            int index$kotlin_stdlib = getIndex();
            setIndex$kotlin_stdlib(index$kotlin_stdlib + 1);
            setLastIndex$kotlin_stdlib(index$kotlin_stdlib);
            java.lang.Object[] objArr = ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).valuesArray;
            kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
            V v = (V) objArr[getLastIndex()];
            initNext$kotlin_stdlib();
            return v;
        }
    }

    /* JADX INFO: compiled from: MapBuilder.kt */
    @kotlin.Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010)\n\u0002\u0010'\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00032\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u00050\u0004B\u0019\u0012\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0007¢\u0006\u0002\u0010\bJ\u0015\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\nH\u0096\u0002J\u0012\u0010\u000b\u001a\u00020\f2\n\u0010\r\u001a\u00060\u000ej\u0002`\u000fJ\r\u0010\u0010\u001a\u00020\u0011H\u0000¢\u0006\u0002\b\u0012¨\u0006\u0013"}, d2 = {"Lkotlin/collections/builders/MapBuilder$EntriesItr;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/collections/builders/MapBuilder$Itr;", "", "", "map", "Lkotlin/collections/builders/MapBuilder;", "(Lkotlin/collections/builders/MapBuilder;)V", "next", "Lkotlin/collections/builders/MapBuilder$EntryRef;", "nextAppendString", "", "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "nextHashCode", "", "nextHashCode$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class EntriesItr<K, V> extends kotlin.collections.builders.MapBuilder.Itr<K, V> implements java.util.Iterator<java.util.Map.Entry<K, V>>, kotlin.jvm.internal.markers.KMutableIterator {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public EntriesItr(kotlin.collections.builders.MapBuilder<K, V> map) {
            super(map);
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
        }

        @Override // java.util.Iterator
        public kotlin.collections.builders.MapBuilder.EntryRef<K, V> next() {
            checkForComodification$kotlin_stdlib();
            if (getIndex() >= ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).length) {
                throw new java.util.NoSuchElementException();
            }
            int index$kotlin_stdlib = getIndex();
            setIndex$kotlin_stdlib(index$kotlin_stdlib + 1);
            setLastIndex$kotlin_stdlib(index$kotlin_stdlib);
            kotlin.collections.builders.MapBuilder.EntryRef<K, V> entryRef = new kotlin.collections.builders.MapBuilder.EntryRef<>(getMap$kotlin_stdlib(), getLastIndex());
            initNext$kotlin_stdlib();
            return entryRef;
        }

        public final int nextHashCode$kotlin_stdlib() {
            if (getIndex() >= ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).length) {
                throw new java.util.NoSuchElementException();
            }
            int index$kotlin_stdlib = getIndex();
            setIndex$kotlin_stdlib(index$kotlin_stdlib + 1);
            setLastIndex$kotlin_stdlib(index$kotlin_stdlib);
            java.lang.Object obj = ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).keysArray[getLastIndex()];
            int iHashCode = obj != null ? obj.hashCode() : 0;
            java.lang.Object[] objArr = ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).valuesArray;
            kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
            java.lang.Object obj2 = objArr[getLastIndex()];
            int result = iHashCode ^ (obj2 != null ? obj2.hashCode() : 0);
            initNext$kotlin_stdlib();
            return result;
        }

        public final void nextAppendString(java.lang.StringBuilder sb) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sb, "sb");
            if (getIndex() >= ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).length) {
                throw new java.util.NoSuchElementException();
            }
            int index$kotlin_stdlib = getIndex();
            setIndex$kotlin_stdlib(index$kotlin_stdlib + 1);
            setLastIndex$kotlin_stdlib(index$kotlin_stdlib);
            java.lang.Object key = ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).keysArray[getLastIndex()];
            if (key == getMap$kotlin_stdlib()) {
                sb.append("(this Map)");
            } else {
                sb.append(key);
            }
            sb.append('=');
            java.lang.Object[] objArr = ((kotlin.collections.builders.MapBuilder) getMap$kotlin_stdlib()).valuesArray;
            kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
            java.lang.Object value = objArr[getLastIndex()];
            if (value == getMap$kotlin_stdlib()) {
                sb.append("(this Map)");
            } else {
                sb.append(value);
            }
            initNext$kotlin_stdlib();
        }
    }

    /* JADX INFO: compiled from: MapBuilder.kt */
    @kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u0000*\u0004\b\u0002\u0010\u0001*\u0004\b\u0003\u0010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003B!\u0012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0007H\u0016J\u0015\u0010\u0013\u001a\u00028\u00032\u0006\u0010\u0014\u001a\u00028\u0003H\u0016¢\u0006\u0002\u0010\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00028\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0002\u0012\u0004\u0012\u00028\u00030\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00028\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000b¨\u0006\u0018"}, d2 = {"Lkotlin/collections/builders/MapBuilder$EntryRef;", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "", "map", "Lkotlin/collections/builders/MapBuilder;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "(Lkotlin/collections/builders/MapBuilder;I)V", "key", "getKey", "()Ljava/lang/Object;", "value", "getValue", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "setValue", "newValue", "(Ljava/lang/Object;)Ljava/lang/Object;", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class EntryRef<K, V> implements java.util.Map.Entry<K, V>, kotlin.jvm.internal.markers.KMutableMap.Entry {
        private final int index;
        private final kotlin.collections.builders.MapBuilder<K, V> map;

        public EntryRef(kotlin.collections.builders.MapBuilder<K, V> map, int index) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "map");
            this.map = map;
            this.index = index;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return (K) ((kotlin.collections.builders.MapBuilder) this.map).keysArray[this.index];
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            java.lang.Object[] objArr = ((kotlin.collections.builders.MapBuilder) this.map).valuesArray;
            kotlin.jvm.internal.Intrinsics.checkNotNull(objArr);
            return (V) objArr[this.index];
        }

        @Override // java.util.Map.Entry
        public V setValue(V newValue) {
            this.map.checkIsMutable$kotlin_stdlib();
            java.lang.Object[] objArrAllocateValuesArray = this.map.allocateValuesArray();
            V v = (V) objArrAllocateValuesArray[this.index];
            objArrAllocateValuesArray[this.index] = newValue;
            return v;
        }

        @Override // java.util.Map.Entry
        public boolean equals(java.lang.Object other) {
            return (other instanceof java.util.Map.Entry) && kotlin.jvm.internal.Intrinsics.areEqual(((java.util.Map.Entry) other).getKey(), getKey()) && kotlin.jvm.internal.Intrinsics.areEqual(((java.util.Map.Entry) other).getValue(), getValue());
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            K key = getKey();
            int iHashCode = key != null ? key.hashCode() : 0;
            V value = getValue();
            return iHashCode ^ (value != null ? value.hashCode() : 0);
        }

        public java.lang.String toString() {
            return new java.lang.StringBuilder().append(getKey()).append('=').append(getValue()).toString();
        }
    }
}
