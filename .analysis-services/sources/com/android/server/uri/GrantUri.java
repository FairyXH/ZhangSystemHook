package com.android.server.uri;

/* JADX INFO: loaded from: classes3.dex */
public class GrantUri {
    public final boolean prefix;
    public final int sourceUserId;
    public final android.net.Uri uri;

    public GrantUri(int sourceUserId, android.net.Uri uri, int modeFlags) {
        this.sourceUserId = sourceUserId;
        this.uri = uri;
        this.prefix = (modeFlags & 128) != 0;
    }

    public int hashCode() {
        int hashCode = (1 * 31) + this.sourceUserId;
        return (((hashCode * 31) + this.uri.hashCode()) * 31) + (this.prefix ? 1231 : 1237);
    }

    public boolean equals(java.lang.Object o) {
        if (!(o instanceof com.android.server.uri.GrantUri)) {
            return false;
        }
        com.android.server.uri.GrantUri other = (com.android.server.uri.GrantUri) o;
        return this.uri.equals(other.uri) && this.sourceUserId == other.sourceUserId && this.prefix == other.prefix;
    }

    public java.lang.String toString() {
        java.lang.String result = this.uri.toString() + " [user " + this.sourceUserId + "]";
        return this.prefix ? result + " [prefix]" : result;
    }

    public java.lang.String toSafeString() {
        java.lang.String result = this.uri.toSafeString() + " [user " + this.sourceUserId + "]";
        return this.prefix ? result + " [prefix]" : result;
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333442L, this.uri.toString());
        proto.write(1120986464257L, this.sourceUserId);
        proto.end(token);
    }

    public static com.android.server.uri.GrantUri resolve(int defaultSourceUserHandle, android.net.Uri uri, int modeFlags) {
        if (com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            return new com.android.server.uri.GrantUri(android.content.ContentProvider.getUserIdFromUri(uri, defaultSourceUserHandle), android.content.ContentProvider.getUriWithoutUserId(uri), modeFlags);
        }
        return new com.android.server.uri.GrantUri(defaultSourceUserHandle, uri, modeFlags);
    }
}
