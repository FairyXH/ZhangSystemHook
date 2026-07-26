package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class ObjectPrinter {
    public static final int kDefaultMaxCollectionLength = 16;

    ObjectPrinter() {
    }

    static java.lang.String print(java.lang.Object obj, int maxCollectionLength) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        print(builder, obj, maxCollectionLength);
        return builder.toString();
    }

    static void print(java.lang.StringBuilder builder, java.lang.Object obj) {
        print(builder, obj, 16);
    }

    static void print(java.lang.StringBuilder builder, java.lang.Object obj, int maxCollectionLength) {
        try {
            if (obj == null) {
                builder.append("null");
                return;
            }
            if (obj instanceof java.lang.Boolean) {
                builder.append(obj);
                return;
            }
            if (obj instanceof java.lang.Number) {
                builder.append(obj);
                return;
            }
            if (obj instanceof java.lang.Character) {
                builder.append('\'');
                builder.append(obj);
                builder.append('\'');
                return;
            }
            if (obj instanceof java.lang.String) {
                builder.append('\"');
                builder.append(obj.toString());
                builder.append('\"');
                return;
            }
            java.lang.Class<?> cls = obj.getClass();
            if (java.util.Collection.class.isAssignableFrom(cls)) {
                java.util.Collection collection = (java.util.Collection) obj;
                builder.append("[ ");
                int length = collection.size();
                boolean isLong = false;
                int i = 0;
                java.util.Iterator it = collection.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    java.lang.Object child = it.next();
                    if (i > 0) {
                        builder.append(", ");
                    }
                    if (i >= maxCollectionLength) {
                        isLong = true;
                        break;
                    } else {
                        print(builder, child, maxCollectionLength);
                        i++;
                    }
                }
                if (isLong) {
                    builder.append("... (+");
                    builder.append(length - maxCollectionLength);
                    builder.append(" entries)");
                }
                builder.append(" ]");
                return;
            }
            if (java.util.Map.class.isAssignableFrom(cls)) {
                java.util.Map<?, ?> map = (java.util.Map) obj;
                builder.append("< ");
                int length2 = map.size();
                boolean isLong2 = false;
                int i2 = 0;
                java.util.Iterator<java.util.Map.Entry<?, ?>> it2 = map.entrySet().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    java.util.Map.Entry<?, ?> child2 = it2.next();
                    if (i2 > 0) {
                        builder.append(", ");
                    }
                    if (i2 >= maxCollectionLength) {
                        isLong2 = true;
                        break;
                    }
                    print(builder, child2.getKey(), maxCollectionLength);
                    builder.append(": ");
                    print(builder, child2.getValue(), maxCollectionLength);
                    i2++;
                }
                if (isLong2) {
                    builder.append("... (+");
                    builder.append(length2 - maxCollectionLength);
                    builder.append(" entries)");
                }
                builder.append(" >");
                return;
            }
            if (cls.isArray()) {
                builder.append("[ ");
                int length3 = java.lang.reflect.Array.getLength(obj);
                boolean isLong3 = false;
                int i3 = 0;
                while (true) {
                    if (i3 >= length3) {
                        break;
                    }
                    if (i3 > 0) {
                        builder.append(", ");
                    }
                    if (i3 >= maxCollectionLength) {
                        isLong3 = true;
                        break;
                    } else {
                        print(builder, java.lang.reflect.Array.get(obj, i3), maxCollectionLength);
                        i3++;
                    }
                }
                if (isLong3) {
                    builder.append("... (+");
                    builder.append(length3 - maxCollectionLength);
                    builder.append(" entries)");
                }
                builder.append(" ]");
                return;
            }
            builder.append(obj);
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}
