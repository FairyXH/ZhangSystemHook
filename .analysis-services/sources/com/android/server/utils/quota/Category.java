package com.android.server.utils.quota;

/* JADX INFO: loaded from: classes3.dex */
public final class Category {
    public static final com.android.server.utils.quota.Category SINGLE_CATEGORY = new com.android.server.utils.quota.Category("SINGLE");
    private final int mHash;
    private final java.lang.String mName;

    public Category(java.lang.String name) {
        this.mName = name;
        this.mHash = name.hashCode();
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof com.android.server.utils.quota.Category) {
            return this.mName.equals(((com.android.server.utils.quota.Category) other).mName);
        }
        return false;
    }

    public int hashCode() {
        return this.mHash;
    }

    public java.lang.String toString() {
        return "Category{" + this.mName + "}";
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, this.mName);
        proto.end(token);
    }
}
