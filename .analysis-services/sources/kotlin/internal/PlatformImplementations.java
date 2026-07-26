package kotlin.internal;

/* JADX INFO: compiled from: PlatformImplementations.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\b\u0010\u0018\u00002\u00020\u0001:\u0001\u0012B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J\u001a\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00060\u00112\u0006\u0010\u0007\u001a\u00020\u0006H\u0016¨\u0006\u0013"}, d2 = {"Lkotlin/internal/PlatformImplementations;", "", "()V", "addSuppressed", "", "cause", "", "exception", "defaultPlatformRandom", "Lkotlin/random/Random;", "getMatchResultNamedGroup", "Lkotlin/text/MatchGroup;", "matchResult", "Ljava/util/regex/MatchResult;", "name", "", "getSuppressed", "", "ReflectThrowable", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class PlatformImplementations {

    /* JADX INFO: compiled from: PlatformImplementations.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lkotlin/internal/PlatformImplementations$ReflectThrowable;", "", "()V", "addSuppressed", "Ljava/lang/reflect/Method;", "getSuppressed", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class ReflectThrowable {
        public static final kotlin.internal.PlatformImplementations.ReflectThrowable INSTANCE = new kotlin.internal.PlatformImplementations.ReflectThrowable();
        public static final java.lang.reflect.Method addSuppressed;
        public static final java.lang.reflect.Method getSuppressed;

        private ReflectThrowable() {
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x003e  */
        static {
            /*
                kotlin.internal.PlatformImplementations$ReflectThrowable r0 = new kotlin.internal.PlatformImplementations$ReflectThrowable
                r0.<init>()
                kotlin.internal.PlatformImplementations.ReflectThrowable.INSTANCE = r0
                java.lang.Class<java.lang.Throwable> r0 = java.lang.Throwable.class
                java.lang.reflect.Method[] r1 = r0.getMethods()
                kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
                int r2 = r1.length
                r3 = 0
                r4 = r3
            L14:
                r5 = 0
                if (r4 >= r2) goto L45
                r6 = r1[r4]
                r7 = r6
                r8 = 0
                java.lang.String r9 = r7.getName()
                java.lang.String r10 = "addSuppressed"
                boolean r9 = kotlin.jvm.internal.Intrinsics.areEqual(r9, r10)
                if (r9 == 0) goto L3e
                java.lang.Class[] r9 = r7.getParameterTypes()
                java.lang.String r10 = "getParameterTypes(...)"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r10)
                java.lang.Object[] r9 = (java.lang.Object[]) r9
                java.lang.Object r9 = kotlin.collections.ArraysKt.singleOrNull(r9)
                boolean r9 = kotlin.jvm.internal.Intrinsics.areEqual(r9, r0)
                if (r9 == 0) goto L3e
                r9 = 1
                goto L3f
            L3e:
                r9 = r3
            L3f:
                if (r9 == 0) goto L42
                goto L46
            L42:
                int r4 = r4 + 1
                goto L14
            L45:
                r6 = r5
            L46:
                kotlin.internal.PlatformImplementations.ReflectThrowable.addSuppressed = r6
                int r2 = r1.length
            L49:
                if (r3 >= r2) goto L60
                r4 = r1[r3]
                r6 = r4
                r7 = 0
                java.lang.String r8 = r6.getName()
                java.lang.String r9 = "getSuppressed"
                boolean r6 = kotlin.jvm.internal.Intrinsics.areEqual(r8, r9)
                if (r6 == 0) goto L5d
                r5 = r4
                goto L60
            L5d:
                int r3 = r3 + 1
                goto L49
            L60:
                kotlin.internal.PlatformImplementations.ReflectThrowable.getSuppressed = r5
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.internal.PlatformImplementations.ReflectThrowable.<clinit>():void");
        }
    }

    public void addSuppressed(java.lang.Throwable cause, java.lang.Throwable exception) throws java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        java.lang.reflect.Method method = kotlin.internal.PlatformImplementations.ReflectThrowable.addSuppressed;
        if (method != null) {
            method.invoke(cause, exception);
        }
    }

    public java.util.List<java.lang.Throwable> getSuppressed(java.lang.Throwable exception) {
        java.lang.Object it;
        java.util.List<java.lang.Throwable> listAsList;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        java.lang.reflect.Method method = kotlin.internal.PlatformImplementations.ReflectThrowable.getSuppressed;
        if (method != null && (it = method.invoke(exception, new java.lang.Object[0])) != null && (listAsList = kotlin.collections.ArraysKt.asList((java.lang.Throwable[]) it)) != null) {
            return listAsList;
        }
        return kotlin.collections.CollectionsKt.emptyList();
    }

    public kotlin.text.MatchGroup getMatchResultNamedGroup(java.util.regex.MatchResult matchResult, java.lang.String name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(matchResult, "matchResult");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        throw new java.lang.UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
    }

    public kotlin.random.Random defaultPlatformRandom() {
        return new kotlin.random.FallbackThreadLocalRandom();
    }
}
