package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class DumpState {
    public static final int DUMP_ACTIVITY_RESOLVERS = 4;
    public static final int DUMP_APEX = 33554432;
    public static final int DUMP_CHANGES = 4194304;
    public static final int DUMP_COMPILER_STATS = 2097152;
    public static final int DUMP_CONTENT_RESOLVERS = 32;
    public static final int DUMP_DEXOPT = 1048576;
    public static final int DUMP_DOMAIN_PREFERRED = 262144;
    public static final int DUMP_DOMAIN_VERIFIER = 131072;
    public static final int DUMP_FEATURES = 2;
    public static final int DUMP_FROZEN = 524288;
    public static final int DUMP_INSTALLS = 65536;
    public static final int DUMP_KEYSETS = 16384;
    public static final int DUMP_KNOWN_PACKAGES = 134217728;
    public static final int DUMP_LIBS = 1;
    public static final int DUMP_MESSAGES = 512;
    public static final int DUMP_PACKAGES = 128;
    public static final int DUMP_PERMISSIONS = 64;
    public static final int DUMP_PER_UID_READ_TIMEOUTS = 268435456;
    public static final int DUMP_PREFERRED = 4096;
    public static final int DUMP_PREFERRED_XML = 8192;
    public static final int DUMP_PROTECTED_BROADCASTS = 1073741824;
    public static final int DUMP_PROVIDERS = 1024;
    public static final int DUMP_QUERIES = 67108864;
    public static final int DUMP_RECEIVER_RESOLVERS = 16;
    public static final int DUMP_SERVICE_PERMISSIONS = 16777216;
    public static final int DUMP_SERVICE_RESOLVERS = 8;
    public static final int DUMP_SHARED_USERS = 256;
    public static final int DUMP_SNAPSHOT_STATISTICS = 536870912;
    public static final int DUMP_VERIFIERS = 2048;
    public static final int DUMP_VERSION = 32768;
    public static final int DUMP_VOLUMES = 8388608;
    public static final int OPTION_DUMP_ALL_COMPONENTS = 2;
    public static final int OPTION_INCLUDE_APEX = 8;
    public static final int OPTION_SHOW_FILTERS = 1;
    public static final int OPTION_SKIP_PERMISSIONS = 4;
    private boolean mBrief;
    private boolean mCheckIn;
    private boolean mFullPreferred;
    private int mOptions;
    private com.android.server.pm.SharedUserSetting mSharedUser;
    private java.lang.String mTargetPackageName;
    private boolean mTitlePrinted;
    private int mTypes;

    public boolean isDumping(int type) {
        return (this.mTypes == 0 && type != 8192) || (this.mTypes & type) != 0;
    }

    public void setDump(int type) {
        this.mTypes |= type;
    }

    public boolean isOptionEnabled(int option) {
        return (this.mOptions & option) != 0;
    }

    public void setOptionEnabled(int option) {
        this.mOptions |= option;
    }

    public boolean onTitlePrinted() {
        boolean printed = this.mTitlePrinted;
        this.mTitlePrinted = true;
        return printed;
    }

    public boolean getTitlePrinted() {
        return this.mTitlePrinted;
    }

    public void setTitlePrinted(boolean enabled) {
        this.mTitlePrinted = enabled;
    }

    public com.android.server.pm.SharedUserSetting getSharedUser() {
        return this.mSharedUser;
    }

    public void setSharedUser(com.android.server.pm.SharedUserSetting user) {
        this.mSharedUser = user;
    }

    public java.lang.String getTargetPackageName() {
        return this.mTargetPackageName;
    }

    public void setTargetPackageName(java.lang.String packageName) {
        this.mTargetPackageName = packageName;
    }

    public boolean isFullPreferred() {
        return this.mFullPreferred;
    }

    public void setFullPreferred(boolean fullPreferred) {
        this.mFullPreferred = fullPreferred;
    }

    public boolean isCheckIn() {
        return this.mCheckIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.mCheckIn = checkIn;
    }

    public boolean isBrief() {
        return this.mBrief;
    }

    public void setBrief(boolean brief) {
        this.mBrief = brief;
    }
}
