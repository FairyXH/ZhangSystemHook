package com.android.server.utils.quota;

/* JADX INFO: loaded from: classes3.dex */
final class Uptc {
    private final int mHash;
    public final java.lang.String packageName;
    public final java.lang.String tag;
    public final int userId;

    Uptc(int userId, java.lang.String packageName, java.lang.String tag) {
        this.userId = userId;
        this.packageName = packageName;
        this.tag = tag;
        this.mHash = new java.lang.StringBuilder().append((userId * 31) + (packageName.hashCode() * 31)).append(tag).toString() == null ? 0 : tag.hashCode() * 31;
    }

    public java.lang.String toString() {
        return string(this.userId, this.packageName, this.tag);
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.userId);
        proto.write(1138166333442L, this.packageName);
        proto.write(1138166333443L, this.tag);
        proto.end(token);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.utils.quota.Uptc)) {
            return false;
        }
        com.android.server.utils.quota.Uptc other = (com.android.server.utils.quota.Uptc) obj;
        return this.userId == other.userId && java.util.Objects.equals(this.packageName, other.packageName) && java.util.Objects.equals(this.tag, other.tag);
    }

    public int hashCode() {
        return this.mHash;
    }

    static java.lang.String string(int userId, java.lang.String packageName, java.lang.String tag) {
        return "<" + userId + ">" + packageName + (tag == null ? "" : "::" + tag);
    }
}
