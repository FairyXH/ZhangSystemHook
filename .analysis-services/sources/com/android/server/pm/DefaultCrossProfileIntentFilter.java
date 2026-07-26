package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class DefaultCrossProfileIntentFilter {
    public final int direction;
    public final com.android.server.pm.WatchedIntentFilter filter;
    public final int flags;
    public final boolean letsPersonalDataIntoProfile;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Direction {
        public static final int TO_PARENT = 0;
        public static final int TO_PROFILE = 1;
    }

    private DefaultCrossProfileIntentFilter(com.android.server.pm.WatchedIntentFilter filter, int flags, int direction, boolean letsPersonalDataIntoProfile) {
        this.filter = (com.android.server.pm.WatchedIntentFilter) java.util.Objects.requireNonNull(filter);
        this.flags = flags;
        this.direction = direction;
        this.letsPersonalDataIntoProfile = letsPersonalDataIntoProfile;
    }

    static final class Builder {
        private final int mDirection;
        private final com.android.server.pm.WatchedIntentFilter mFilter = new com.android.server.pm.WatchedIntentFilter();
        private final int mFlags;
        private final boolean mLetsPersonalDataIntoProfile;

        Builder(int direction, int flags, boolean letsPersonalDataIntoProfile) {
            this.mDirection = direction;
            this.mFlags = flags;
            this.mLetsPersonalDataIntoProfile = letsPersonalDataIntoProfile;
        }

        com.android.server.pm.DefaultCrossProfileIntentFilter.Builder addAction(java.lang.String action) {
            this.mFilter.addAction(action);
            return this;
        }

        com.android.server.pm.DefaultCrossProfileIntentFilter.Builder addCategory(java.lang.String category) {
            this.mFilter.addCategory(category);
            return this;
        }

        com.android.server.pm.DefaultCrossProfileIntentFilter.Builder addDataType(java.lang.String type) {
            try {
                this.mFilter.addDataType(type);
            } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
            }
            return this;
        }

        com.android.server.pm.DefaultCrossProfileIntentFilter.Builder addDataScheme(java.lang.String scheme) {
            this.mFilter.addDataScheme(scheme);
            return this;
        }

        com.android.server.pm.DefaultCrossProfileIntentFilter build() {
            return new com.android.server.pm.DefaultCrossProfileIntentFilter(this.mFilter, this.mFlags, this.mDirection, this.mLetsPersonalDataIntoProfile);
        }
    }
}
