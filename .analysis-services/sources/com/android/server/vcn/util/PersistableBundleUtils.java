package com.android.server.vcn.util;

/* JADX INFO: loaded from: classes3.dex */
public class PersistableBundleUtils {
    private static final java.lang.String BYTE_ARRAY_KEY = "BYTE_ARRAY_KEY";
    private static final java.lang.String COLLECTION_SIZE_KEY = "COLLECTION_LENGTH";
    private static final java.lang.String INTEGER_KEY = "INTEGER_KEY";
    private static final java.lang.String LIST_KEY_FORMAT = "LIST_ITEM_%d";
    private static final java.lang.String MAP_KEY_FORMAT = "MAP_KEY_%d";
    private static final java.lang.String MAP_VALUE_FORMAT = "MAP_VALUE_%d";
    private static final java.lang.String PARCEL_UUID_KEY = "PARCEL_UUID";
    private static final java.lang.String STRING_KEY = "STRING_KEY";
    public static final com.android.server.vcn.util.PersistableBundleUtils.Serializer<java.lang.Integer> INTEGER_SERIALIZER = new com.android.server.vcn.util.PersistableBundleUtils.Serializer() { // from class: com.android.server.vcn.util.PersistableBundleUtils$$ExternalSyntheticLambda0
        @Override // com.android.server.vcn.util.PersistableBundleUtils.Serializer
        public final android.os.PersistableBundle toPersistableBundle(java.lang.Object obj) {
            return com.android.server.vcn.util.PersistableBundleUtils.lambda$static$0((java.lang.Integer) obj);
        }
    };
    public static final com.android.server.vcn.util.PersistableBundleUtils.Deserializer<java.lang.Integer> INTEGER_DESERIALIZER = new com.android.server.vcn.util.PersistableBundleUtils.Deserializer() { // from class: com.android.server.vcn.util.PersistableBundleUtils$$ExternalSyntheticLambda1
        @Override // com.android.server.vcn.util.PersistableBundleUtils.Deserializer
        public final java.lang.Object fromPersistableBundle(android.os.PersistableBundle persistableBundle) {
            return com.android.server.vcn.util.PersistableBundleUtils.lambda$static$1(persistableBundle);
        }
    };
    public static final com.android.server.vcn.util.PersistableBundleUtils.Serializer<java.lang.String> STRING_SERIALIZER = new com.android.server.vcn.util.PersistableBundleUtils.Serializer() { // from class: com.android.server.vcn.util.PersistableBundleUtils$$ExternalSyntheticLambda2
        @Override // com.android.server.vcn.util.PersistableBundleUtils.Serializer
        public final android.os.PersistableBundle toPersistableBundle(java.lang.Object obj) {
            return com.android.server.vcn.util.PersistableBundleUtils.lambda$static$2((java.lang.String) obj);
        }
    };
    public static final com.android.server.vcn.util.PersistableBundleUtils.Deserializer<java.lang.String> STRING_DESERIALIZER = new com.android.server.vcn.util.PersistableBundleUtils.Deserializer() { // from class: com.android.server.vcn.util.PersistableBundleUtils$$ExternalSyntheticLambda3
        @Override // com.android.server.vcn.util.PersistableBundleUtils.Deserializer
        public final java.lang.Object fromPersistableBundle(android.os.PersistableBundle persistableBundle) {
            return com.android.server.vcn.util.PersistableBundleUtils.lambda$static$3(persistableBundle);
        }
    };

    public interface Deserializer<T> {
        T fromPersistableBundle(android.os.PersistableBundle persistableBundle);
    }

    public interface Serializer<T> {
        android.os.PersistableBundle toPersistableBundle(T t);
    }

    static /* synthetic */ android.os.PersistableBundle lambda$static$0(java.lang.Integer i) {
        android.os.PersistableBundle result = new android.os.PersistableBundle();
        result.putInt(INTEGER_KEY, i.intValue());
        return result;
    }

    static /* synthetic */ java.lang.Integer lambda$static$1(android.os.PersistableBundle bundle) {
        java.util.Objects.requireNonNull(bundle, "PersistableBundle is null");
        return java.lang.Integer.valueOf(bundle.getInt(INTEGER_KEY));
    }

    static /* synthetic */ android.os.PersistableBundle lambda$static$2(java.lang.String i) {
        android.os.PersistableBundle result = new android.os.PersistableBundle();
        result.putString(STRING_KEY, i);
        return result;
    }

    static /* synthetic */ java.lang.String lambda$static$3(android.os.PersistableBundle bundle) {
        java.util.Objects.requireNonNull(bundle, "PersistableBundle is null");
        return bundle.getString(STRING_KEY);
    }

    public static android.os.PersistableBundle fromParcelUuid(android.os.ParcelUuid uuid) {
        android.os.PersistableBundle result = new android.os.PersistableBundle();
        result.putString(PARCEL_UUID_KEY, uuid.toString());
        return result;
    }

    public static android.os.ParcelUuid toParcelUuid(android.os.PersistableBundle bundle) {
        return android.os.ParcelUuid.fromString(bundle.getString(PARCEL_UUID_KEY));
    }

    public static <T> android.os.PersistableBundle fromList(java.util.List<T> in, com.android.server.vcn.util.PersistableBundleUtils.Serializer<T> serializer) {
        android.os.PersistableBundle result = new android.os.PersistableBundle();
        result.putInt(COLLECTION_SIZE_KEY, in.size());
        for (int i = 0; i < in.size(); i++) {
            java.lang.String key = java.lang.String.format(LIST_KEY_FORMAT, java.lang.Integer.valueOf(i));
            result.putPersistableBundle(key, serializer.toPersistableBundle(in.get(i)));
        }
        return result;
    }

    public static <T> java.util.List<T> toList(android.os.PersistableBundle in, com.android.server.vcn.util.PersistableBundleUtils.Deserializer<T> deserializer) {
        int listLength = in.getInt(COLLECTION_SIZE_KEY);
        java.util.ArrayList<T> result = new java.util.ArrayList<>(listLength);
        for (int i = 0; i < listLength; i++) {
            java.lang.String key = java.lang.String.format(LIST_KEY_FORMAT, java.lang.Integer.valueOf(i));
            android.os.PersistableBundle item = in.getPersistableBundle(key);
            result.add(deserializer.fromPersistableBundle(item));
        }
        return result;
    }

    public static android.os.PersistableBundle fromByteArray(byte[] array) {
        android.os.PersistableBundle result = new android.os.PersistableBundle();
        result.putString(BYTE_ARRAY_KEY, com.android.internal.util.HexDump.toHexString(array));
        return result;
    }

    public static byte[] toByteArray(android.os.PersistableBundle bundle) {
        java.util.Objects.requireNonNull(bundle, "PersistableBundle is null");
        java.lang.String hex = bundle.getString(BYTE_ARRAY_KEY);
        if (hex == null || hex.length() % 2 != 0) {
            throw new java.lang.IllegalArgumentException("PersistableBundle contains invalid byte array");
        }
        return com.android.internal.util.HexDump.hexStringToByteArray(hex);
    }

    public static <K, V> android.os.PersistableBundle fromMap(java.util.Map<K, V> in, com.android.server.vcn.util.PersistableBundleUtils.Serializer<K> keySerializer, com.android.server.vcn.util.PersistableBundleUtils.Serializer<V> valueSerializer) {
        android.os.PersistableBundle result = new android.os.PersistableBundle();
        result.putInt(COLLECTION_SIZE_KEY, in.size());
        int i = 0;
        for (java.util.Map.Entry<K, V> entry : in.entrySet()) {
            java.lang.String keyKey = java.lang.String.format(MAP_KEY_FORMAT, java.lang.Integer.valueOf(i));
            java.lang.String valueKey = java.lang.String.format(MAP_VALUE_FORMAT, java.lang.Integer.valueOf(i));
            result.putPersistableBundle(keyKey, keySerializer.toPersistableBundle(entry.getKey()));
            result.putPersistableBundle(valueKey, valueSerializer.toPersistableBundle(entry.getValue()));
            i++;
        }
        return result;
    }

    public static <K, V> java.util.LinkedHashMap<K, V> toMap(android.os.PersistableBundle in, com.android.server.vcn.util.PersistableBundleUtils.Deserializer<K> keyDeserializer, com.android.server.vcn.util.PersistableBundleUtils.Deserializer<V> valueDeserializer) {
        int mapSize = in.getInt(COLLECTION_SIZE_KEY);
        java.util.LinkedHashMap<K, V> result = new java.util.LinkedHashMap<>(mapSize);
        for (int i = 0; i < mapSize; i++) {
            java.lang.String keyKey = java.lang.String.format(MAP_KEY_FORMAT, java.lang.Integer.valueOf(i));
            java.lang.String valueKey = java.lang.String.format(MAP_VALUE_FORMAT, java.lang.Integer.valueOf(i));
            android.os.PersistableBundle keyBundle = in.getPersistableBundle(keyKey);
            android.os.PersistableBundle valueBundle = in.getPersistableBundle(valueKey);
            K key = keyDeserializer.fromPersistableBundle(keyBundle);
            V value = valueDeserializer.fromPersistableBundle(valueBundle);
            result.put(key, value);
        }
        return result;
    }

    public static byte[] toDiskStableBytes(android.os.PersistableBundle bundle) throws java.io.IOException {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        bundle.writeToStream(outputStream);
        return outputStream.toByteArray();
    }

    public static android.os.PersistableBundle fromDiskStableBytes(byte[] bytes) throws java.io.IOException {
        java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(bytes);
        return android.os.PersistableBundle.readFromStream(inputStream);
    }

    public static class LockingReadWriteHelper {
        private final java.util.concurrent.locks.ReadWriteLock mDiskLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
        private final java.lang.String mPath;

        public LockingReadWriteHelper(java.lang.String path) {
            this.mPath = (java.lang.String) java.util.Objects.requireNonNull(path, "fileName was null");
        }

        public android.os.PersistableBundle readFromDisk() throws java.io.IOException {
            try {
                this.mDiskLock.readLock().lock();
                java.io.File file = new java.io.File(this.mPath);
                if (file.exists()) {
                    java.io.FileInputStream fis = new java.io.FileInputStream(file);
                    try {
                        android.os.PersistableBundle fromStream = android.os.PersistableBundle.readFromStream(fis);
                        fis.close();
                        return fromStream;
                    } finally {
                    }
                }
                this.mDiskLock.readLock().unlock();
                return null;
            } finally {
                this.mDiskLock.readLock().unlock();
            }
        }

        public void writeToDisk(android.os.PersistableBundle bundle) throws java.io.IOException {
            java.util.Objects.requireNonNull(bundle, "bundle was null");
            try {
                this.mDiskLock.writeLock().lock();
                java.io.File file = new java.io.File(this.mPath);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                }
                java.io.FileOutputStream fos = new java.io.FileOutputStream(file);
                try {
                    bundle.writeToStream(fos);
                    fos.close();
                } finally {
                }
            } finally {
                this.mDiskLock.writeLock().unlock();
            }
        }
    }

    public static android.os.PersistableBundle minimizeBundle(android.os.PersistableBundle bundle, java.lang.String... keys) {
        java.lang.Object value;
        android.os.PersistableBundle minimized = new android.os.PersistableBundle();
        if (bundle == null) {
            return minimized;
        }
        for (java.lang.String key : keys) {
            if (bundle.containsKey(key) && (value = bundle.get(key)) != null) {
                if (value instanceof java.lang.Boolean) {
                    minimized.putBoolean(key, ((java.lang.Boolean) value).booleanValue());
                } else if (value instanceof boolean[]) {
                    minimized.putBooleanArray(key, (boolean[]) value);
                } else if (value instanceof java.lang.Double) {
                    minimized.putDouble(key, ((java.lang.Double) value).doubleValue());
                } else if (value instanceof double[]) {
                    minimized.putDoubleArray(key, (double[]) value);
                } else if (value instanceof java.lang.Integer) {
                    minimized.putInt(key, ((java.lang.Integer) value).intValue());
                } else if (value instanceof int[]) {
                    minimized.putIntArray(key, (int[]) value);
                } else if (value instanceof java.lang.Long) {
                    minimized.putLong(key, ((java.lang.Long) value).longValue());
                } else if (value instanceof long[]) {
                    minimized.putLongArray(key, (long[]) value);
                } else if (value instanceof java.lang.String) {
                    minimized.putString(key, (java.lang.String) value);
                } else if (value instanceof java.lang.String[]) {
                    minimized.putStringArray(key, (java.lang.String[]) value);
                } else if (value instanceof android.os.PersistableBundle) {
                    minimized.putPersistableBundle(key, (android.os.PersistableBundle) value);
                }
            }
        }
        return minimized;
    }

    public static int getHashCode(android.os.PersistableBundle bundle) {
        if (bundle == null) {
            return -1;
        }
        int iterativeHashcode = 0;
        java.util.TreeSet<java.lang.String> treeSet = new java.util.TreeSet<>(bundle.keySet());
        for (java.lang.String key : treeSet) {
            java.lang.Object val = bundle.get(key);
            if (val instanceof android.os.PersistableBundle) {
                iterativeHashcode = java.util.Objects.hash(java.lang.Integer.valueOf(iterativeHashcode), key, java.lang.Integer.valueOf(getHashCode((android.os.PersistableBundle) val)));
            } else {
                iterativeHashcode = java.util.Objects.hash(java.lang.Integer.valueOf(iterativeHashcode), key, val);
            }
        }
        return iterativeHashcode;
    }

    public static boolean isEqual(android.os.PersistableBundle left, android.os.PersistableBundle right) {
        if (java.util.Objects.equals(left, right)) {
            return true;
        }
        if (java.util.Objects.isNull(left) != java.util.Objects.isNull(right) || !left.keySet().equals(right.keySet())) {
            return false;
        }
        for (java.lang.String key : left.keySet()) {
            java.lang.Object leftVal = left.get(key);
            java.lang.Object rightVal = right.get(key);
            if (!java.util.Objects.equals(leftVal, rightVal)) {
                if (java.util.Objects.isNull(leftVal) != java.util.Objects.isNull(rightVal) || !java.util.Objects.equals(leftVal.getClass(), rightVal.getClass())) {
                    return false;
                }
                if (leftVal instanceof android.os.PersistableBundle) {
                    if (!isEqual((android.os.PersistableBundle) leftVal, (android.os.PersistableBundle) rightVal)) {
                        return false;
                    }
                } else if (leftVal.getClass().isArray()) {
                    if (leftVal instanceof boolean[]) {
                        if (!java.util.Arrays.equals((boolean[]) leftVal, (boolean[]) rightVal)) {
                            return false;
                        }
                    } else if (leftVal instanceof double[]) {
                        if (!java.util.Arrays.equals((double[]) leftVal, (double[]) rightVal)) {
                            return false;
                        }
                    } else if (leftVal instanceof int[]) {
                        if (!java.util.Arrays.equals((int[]) leftVal, (int[]) rightVal)) {
                            return false;
                        }
                    } else if (leftVal instanceof long[]) {
                        if (!java.util.Arrays.equals((long[]) leftVal, (long[]) rightVal)) {
                            return false;
                        }
                    } else if (!java.util.Arrays.equals((java.lang.Object[]) leftVal, (java.lang.Object[]) rightVal)) {
                        return false;
                    }
                } else if (!java.util.Objects.equals(leftVal, rightVal)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class PersistableBundleWrapper {
        private final android.os.PersistableBundle mBundle;

        public PersistableBundleWrapper(android.os.PersistableBundle bundle) {
            this.mBundle = (android.os.PersistableBundle) java.util.Objects.requireNonNull(bundle, "Bundle was null");
        }

        public int getInt(java.lang.String key, int defaultValue) {
            return this.mBundle.getInt(key, defaultValue);
        }

        public int[] getIntArray(java.lang.String key, int[] defaultValue) {
            int[] value = this.mBundle.getIntArray(key);
            return value == null ? defaultValue : value;
        }

        public int hashCode() {
            return com.android.server.vcn.util.PersistableBundleUtils.getHashCode(this.mBundle);
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper)) {
                return false;
            }
            com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper other = (com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper) obj;
            return com.android.server.vcn.util.PersistableBundleUtils.isEqual(this.mBundle, other.mBundle);
        }

        public java.lang.String toString() {
            return this.mBundle.toString();
        }
    }
}
