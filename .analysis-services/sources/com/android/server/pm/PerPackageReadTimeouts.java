package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PerPackageReadTimeouts {
    public final java.lang.String packageName;
    public final byte[] sha256certificate;
    public final com.android.server.pm.PerPackageReadTimeouts.Timeouts timeouts;
    public final com.android.server.pm.PerPackageReadTimeouts.VersionCodes versionCodes;

    static long tryParseLong(java.lang.String str, long defaultValue) {
        try {
            return java.lang.Long.parseLong(str);
        } catch (java.lang.NumberFormatException e) {
            return defaultValue;
        }
    }

    static byte[] tryParseSha256(java.lang.String str) {
        if (android.text.TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return com.android.internal.util.HexDump.hexStringToByteArray(str);
        } catch (java.lang.RuntimeException e) {
            return null;
        }
    }

    static class Timeouts {
        public static final com.android.server.pm.PerPackageReadTimeouts.Timeouts DEFAULT = new com.android.server.pm.PerPackageReadTimeouts.Timeouts(3600000000L, 3600000000L, 3600000000L);
        public final long maxPendingTimeUs;
        public final long minPendingTimeUs;
        public final long minTimeUs;

        private Timeouts(long minTimeUs, long minPendingTimeUs, long maxPendingTimeUs) {
            this.minTimeUs = minTimeUs;
            this.minPendingTimeUs = minPendingTimeUs;
            this.maxPendingTimeUs = maxPendingTimeUs;
        }

        static com.android.server.pm.PerPackageReadTimeouts.Timeouts parse(java.lang.String timeouts) {
            java.lang.String[] splits = timeouts.split(":", 3);
            if (splits.length != 3) {
                return DEFAULT;
            }
            long minTimeUs = com.android.server.pm.PerPackageReadTimeouts.tryParseLong(splits[0], DEFAULT.minTimeUs);
            long minPendingTimeUs = com.android.server.pm.PerPackageReadTimeouts.tryParseLong(splits[1], DEFAULT.minPendingTimeUs);
            long maxPendingTimeUs = com.android.server.pm.PerPackageReadTimeouts.tryParseLong(splits[2], DEFAULT.maxPendingTimeUs);
            if (0 <= minTimeUs && minTimeUs <= minPendingTimeUs && minPendingTimeUs <= maxPendingTimeUs) {
                return new com.android.server.pm.PerPackageReadTimeouts.Timeouts(minTimeUs, minPendingTimeUs, maxPendingTimeUs);
            }
            return DEFAULT;
        }
    }

    static class VersionCodes {
        public static final com.android.server.pm.PerPackageReadTimeouts.VersionCodes ALL_VERSION_CODES = new com.android.server.pm.PerPackageReadTimeouts.VersionCodes(Long.MIN_VALUE, Long.MAX_VALUE);
        public final long maxVersionCode;
        public final long minVersionCode;

        private VersionCodes(long minVersionCode, long maxVersionCode) {
            this.minVersionCode = minVersionCode;
            this.maxVersionCode = maxVersionCode;
        }

        static com.android.server.pm.PerPackageReadTimeouts.VersionCodes parse(java.lang.String codes) {
            if (android.text.TextUtils.isEmpty(codes)) {
                return ALL_VERSION_CODES;
            }
            java.lang.String[] splits = codes.split("-", 2);
            switch (splits.length) {
                case 1:
                    try {
                        long versionCode = java.lang.Long.parseLong(splits[0]);
                        return new com.android.server.pm.PerPackageReadTimeouts.VersionCodes(versionCode, versionCode);
                    } catch (java.lang.NumberFormatException e) {
                        return ALL_VERSION_CODES;
                    }
                case 2:
                    long minVersionCode = com.android.server.pm.PerPackageReadTimeouts.tryParseLong(splits[0], ALL_VERSION_CODES.minVersionCode);
                    long maxVersionCode = com.android.server.pm.PerPackageReadTimeouts.tryParseLong(splits[1], ALL_VERSION_CODES.maxVersionCode);
                    if (minVersionCode <= maxVersionCode) {
                        return new com.android.server.pm.PerPackageReadTimeouts.VersionCodes(minVersionCode, maxVersionCode);
                    }
                    break;
            }
            return ALL_VERSION_CODES;
        }
    }

    private PerPackageReadTimeouts(java.lang.String packageName, byte[] sha256certificate, com.android.server.pm.PerPackageReadTimeouts.VersionCodes versionCodes, com.android.server.pm.PerPackageReadTimeouts.Timeouts timeouts) {
        this.packageName = packageName;
        this.sha256certificate = sha256certificate;
        this.versionCodes = versionCodes;
        this.timeouts = timeouts;
    }

    static com.android.server.pm.PerPackageReadTimeouts parse(java.lang.String timeoutsStr, com.android.server.pm.PerPackageReadTimeouts.VersionCodes defaultVersionCodes, com.android.server.pm.PerPackageReadTimeouts.Timeouts defaultTimeouts) {
        byte[] sha256certificate = null;
        com.android.server.pm.PerPackageReadTimeouts.VersionCodes versionCodes = defaultVersionCodes;
        com.android.server.pm.PerPackageReadTimeouts.Timeouts timeouts = defaultTimeouts;
        java.lang.String[] splits = timeoutsStr.split(":", 4);
        switch (splits.length) {
            case 4:
                timeouts = com.android.server.pm.PerPackageReadTimeouts.Timeouts.parse(splits[3]);
            case 3:
                versionCodes = com.android.server.pm.PerPackageReadTimeouts.VersionCodes.parse(splits[2]);
            case 2:
                sha256certificate = tryParseSha256(splits[1]);
            case 1:
                java.lang.String packageName = splits[0];
                if (!android.text.TextUtils.isEmpty(packageName)) {
                    break;
                }
                break;
        }
        return null;
    }

    static java.util.List<com.android.server.pm.PerPackageReadTimeouts> parseDigestersList(java.lang.String defaultTimeoutsStr, java.lang.String knownDigestersList) {
        if (android.text.TextUtils.isEmpty(knownDigestersList)) {
            return java.util.Collections.emptyList();
        }
        com.android.server.pm.PerPackageReadTimeouts.VersionCodes defaultVersionCodes = com.android.server.pm.PerPackageReadTimeouts.VersionCodes.ALL_VERSION_CODES;
        com.android.server.pm.PerPackageReadTimeouts.Timeouts defaultTimeouts = com.android.server.pm.PerPackageReadTimeouts.Timeouts.parse(defaultTimeoutsStr);
        java.lang.String[] packages = knownDigestersList.split(",");
        java.util.List<com.android.server.pm.PerPackageReadTimeouts> result = new java.util.ArrayList<>(packages.length);
        for (java.lang.String str : packages) {
            com.android.server.pm.PerPackageReadTimeouts timeouts = parse(str, defaultVersionCodes, defaultTimeouts);
            if (timeouts != null) {
                result.add(timeouts);
            }
        }
        return result;
    }
}
