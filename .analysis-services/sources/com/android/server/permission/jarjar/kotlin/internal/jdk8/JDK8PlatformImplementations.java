package com.android.server.permission.jarjar.kotlin.internal.jdk8;

/* JADX INFO: compiled from: JDK8PlatformImplementations.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u000fB\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u001a\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0002¨\u0006\u0010"}, d2 = {"Lkotlin/internal/jdk8/JDK8PlatformImplementations;", "Lkotlin/internal/jdk7/JDK7PlatformImplementations;", "()V", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "sdkIsNullOrAtLeast", "", "version", "", "ReflectSdkVersion", "kotlin-stdlib-jdk8"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class JDK8PlatformImplementations extends com.android.server.permission.jarjar.kotlin.internal.jdk7.JDK7PlatformImplementations {

    /* JADX INFO: compiled from: JDK8PlatformImplementations.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0004\n\u0002\u0010\u0005¨\u0006\u0006"}, d2 = {"Lkotlin/internal/jdk8/JDK8PlatformImplementations$ReflectSdkVersion;", "", "()V", "sdkVersion", "", "Ljava/lang/Integer;", "kotlin-stdlib-jdk8"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class ReflectSdkVersion {
        public static final com.android.server.permission.jarjar.kotlin.internal.jdk8.JDK8PlatformImplementations.ReflectSdkVersion INSTANCE = new com.android.server.permission.jarjar.kotlin.internal.jdk8.JDK8PlatformImplementations.ReflectSdkVersion();
        public static final java.lang.Integer sdkVersion;

        private ReflectSdkVersion() {
        }

        static {
            java.lang.Integer num;
            java.lang.Integer num2 = null;
            try {
                java.lang.Object obj = java.lang.Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
                num = obj instanceof java.lang.Integer ? (java.lang.Integer) obj : null;
            } catch (java.lang.Throwable th) {
                num = null;
            }
            if (num != null) {
                int it = num.intValue();
                if (it > 0) {
                    num2 = num;
                }
            }
            sdkVersion = num2;
        }
    }

    private final boolean sdkIsNullOrAtLeast(int version) {
        return com.android.server.permission.jarjar.kotlin.internal.jdk8.JDK8PlatformImplementations.ReflectSdkVersion.sdkVersion == null || com.android.server.permission.jarjar.kotlin.internal.jdk8.JDK8PlatformImplementations.ReflectSdkVersion.sdkVersion.intValue() >= version;
    }

    @Override // com.android.server.permission.jarjar.kotlin.internal.PlatformImplementations
    public com.android.server.permission.jarjar.kotlin.text.MatchGroup getMatchResultNamedGroup(java.util.regex.MatchResult matchResult, java.lang.String name) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(matchResult, "matchResult");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        java.util.regex.Matcher matcher = matchResult instanceof java.util.regex.Matcher ? (java.util.regex.Matcher) matchResult : null;
        if (matcher == null) {
            throw new java.lang.UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
        }
        com.android.server.permission.jarjar.kotlin.ranges.IntRange range = new com.android.server.permission.jarjar.kotlin.ranges.IntRange(matcher.start(name), matcher.end(name) - 1);
        if (range.getStart().intValue() < 0) {
            return null;
        }
        java.lang.String strGroup = matcher.group(name);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strGroup, "group(...)");
        return new com.android.server.permission.jarjar.kotlin.text.MatchGroup(strGroup, range);
    }

    @Override // com.android.server.permission.jarjar.kotlin.internal.PlatformImplementations
    public com.android.server.permission.jarjar.kotlin.random.Random defaultPlatformRandom() {
        return sdkIsNullOrAtLeast(34) ? new com.android.server.permission.jarjar.kotlin.random.jdk8.PlatformThreadLocalRandom() : super.defaultPlatformRandom();
    }
}
