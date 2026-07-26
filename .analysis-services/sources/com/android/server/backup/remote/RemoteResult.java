package com.android.server.backup.remote;

/* JADX INFO: loaded from: classes.dex */
public class RemoteResult {
    private final int mType;
    private final long mValue;
    public static final com.android.server.backup.remote.RemoteResult FAILED_TIMED_OUT = new com.android.server.backup.remote.RemoteResult(1, 0);
    public static final com.android.server.backup.remote.RemoteResult FAILED_CANCELLED = new com.android.server.backup.remote.RemoteResult(2, 0);
    public static final com.android.server.backup.remote.RemoteResult FAILED_THREAD_INTERRUPTED = new com.android.server.backup.remote.RemoteResult(3, 0);

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface Type {
        public static final int FAILED_CANCELLED = 2;
        public static final int FAILED_THREAD_INTERRUPTED = 3;
        public static final int FAILED_TIMED_OUT = 1;
        public static final int SUCCESS = 0;
    }

    public static com.android.server.backup.remote.RemoteResult of(long value) {
        return new com.android.server.backup.remote.RemoteResult(0, value);
    }

    private RemoteResult(int type, long value) {
        this.mType = type;
        this.mValue = value;
    }

    public boolean isPresent() {
        return this.mType == 0;
    }

    public long get() {
        com.android.internal.util.Preconditions.checkState(isPresent(), "Can't obtain value of failed result");
        return this.mValue;
    }

    public java.lang.String toString() {
        return "RemoteResult{" + toStringDescription() + "}";
    }

    private java.lang.String toStringDescription() {
        switch (this.mType) {
            case 0:
                return java.lang.Long.toString(this.mValue);
            case 1:
                return "FAILED_TIMED_OUT";
            case 2:
                return "FAILED_CANCELLED";
            case 3:
                return "FAILED_THREAD_INTERRUPTED";
            default:
                throw new java.lang.AssertionError("Unknown type");
        }
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.backup.remote.RemoteResult)) {
            return false;
        }
        com.android.server.backup.remote.RemoteResult that = (com.android.server.backup.remote.RemoteResult) o;
        return this.mType == that.mType && this.mValue == that.mValue;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mType), java.lang.Long.valueOf(this.mValue));
    }
}
