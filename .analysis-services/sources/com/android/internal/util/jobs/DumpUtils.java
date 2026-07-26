package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public final class DumpUtils {
    public static final android.content.ComponentName[] CRITICAL_SECTION_COMPONENTS = {new android.content.ComponentName("com.android.systemui", "com.android.systemui.SystemUIService")};
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "DumpUtils";

    public interface Dump {
        void dump(java.io.PrintWriter printWriter, java.lang.String str);
    }

    public interface KeyDumper {
        void dump(int i, int i2);
    }

    public interface ValueDumper<T> {
        void dump(T t);
    }

    private DumpUtils() {
    }

    public static void dumpAsync(android.os.Handler handler, final com.android.internal.util.jobs.DumpUtils.Dump dump, java.io.PrintWriter pw, final java.lang.String prefix, long timeout) {
        final java.io.StringWriter sw = new java.io.StringWriter();
        if (handler.runWithScissors(new java.lang.Runnable() { // from class: com.android.internal.util.jobs.DumpUtils.1
            @Override // java.lang.Runnable
            public void run() {
                java.io.PrintWriter lpw = new com.android.internal.util.jobs.FastPrintWriter(sw);
                dump.dump(lpw, prefix);
                lpw.close();
            }
        }, timeout)) {
            pw.print(sw.toString());
        } else {
            pw.println("... timed out");
        }
    }

    private static void logMessage(java.io.PrintWriter pw, java.lang.String msg) {
        pw.println(msg);
    }

    public static boolean checkDumpPermission(android.content.Context context, java.lang.String tag, java.io.PrintWriter pw) {
        if (context.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            logMessage(pw, "Permission Denial: can't dump " + tag + " from from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " due to missing android.permission.DUMP permission");
            return false;
        }
        return true;
    }

    public static boolean checkUsageStatsPermission(android.content.Context context, java.lang.String tag, java.io.PrintWriter pw) {
        int uid = android.os.Binder.getCallingUid();
        switch (uid) {
            case 0:
            case 1000:
            case 1067:
            case 2000:
                return true;
            default:
                if (context.checkCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS") != 0) {
                    logMessage(pw, "Permission Denial: can't dump " + tag + " from from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " due to missing android.permission.PACKAGE_USAGE_STATS permission");
                    return false;
                }
                android.app.AppOpsManager appOps = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
                java.lang.String[] pkgs = context.getPackageManager().getPackagesForUid(uid);
                if (pkgs != null) {
                    for (java.lang.String pkg : pkgs) {
                        switch (appOps.noteOpNoThrow(43, uid, pkg)) {
                            case 0:
                                return true;
                            case 3:
                                return true;
                            default:
                                break;
                        }
                    }
                }
                logMessage(pw, "Permission Denial: can't dump " + tag + " from from pid=" + android.os.Binder.getCallingPid() + ", uid=" + android.os.Binder.getCallingUid() + " due to android:get_usage_stats app-op not allowed");
                return false;
        }
    }

    public static boolean checkDumpAndUsageStatsPermission(android.content.Context context, java.lang.String tag, java.io.PrintWriter pw) {
        return checkDumpPermission(context, tag, pw) && checkUsageStatsPermission(context, tag, pw);
    }

    public static boolean isPlatformPackage(java.lang.String packageName) {
        return packageName != null && (packageName.equals(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME) || packageName.startsWith("android.") || packageName.startsWith("com.android."));
    }

    public static boolean isPlatformPackage(android.content.ComponentName cname) {
        return cname != null && isPlatformPackage(cname.getPackageName());
    }

    public static boolean isPlatformPackage(android.content.ComponentName.WithComponentName wcn) {
        return wcn != null && isPlatformPackage(wcn.getComponentName());
    }

    public static boolean isNonPlatformPackage(java.lang.String packageName) {
        return (packageName == null || isPlatformPackage(packageName)) ? false : true;
    }

    public static boolean isNonPlatformPackage(android.content.ComponentName cname) {
        return cname != null && isNonPlatformPackage(cname.getPackageName());
    }

    public static boolean isNonPlatformPackage(android.content.ComponentName.WithComponentName wcn) {
        return (wcn == null || isPlatformPackage(wcn.getComponentName())) ? false : true;
    }

    private static boolean isCriticalPackage(android.content.ComponentName cname) {
        if (cname == null) {
            return false;
        }
        for (int i = 0; i < CRITICAL_SECTION_COMPONENTS.length; i++) {
            if (cname.equals(CRITICAL_SECTION_COMPONENTS[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPlatformCriticalPackage(android.content.ComponentName.WithComponentName wcn) {
        return wcn != null && isPlatformPackage(wcn.getComponentName()) && isCriticalPackage(wcn.getComponentName());
    }

    public static boolean isPlatformNonCriticalPackage(android.content.ComponentName.WithComponentName wcn) {
        return (wcn == null || !isPlatformPackage(wcn.getComponentName()) || isCriticalPackage(wcn.getComponentName())) ? false : true;
    }

    public static <TRec extends android.content.ComponentName.WithComponentName> java.util.function.Predicate<TRec> filterRecord(final java.lang.String filterString) {
        if (android.text.TextUtils.isEmpty(filterString)) {
            return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.internal.util.jobs.DumpUtils.lambda$filterRecord$0((android.content.ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all".equals(filterString)) {
            return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda2
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return java.util.Objects.nonNull((android.content.ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-platform".equals(filterString)) {
            return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.internal.util.jobs.DumpUtils.isPlatformPackage((android.content.ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-non-platform".equals(filterString)) {
            return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.internal.util.jobs.DumpUtils.isNonPlatformPackage((android.content.ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-platform-critical".equals(filterString)) {
            return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.internal.util.jobs.DumpUtils.isPlatformCriticalPackage((android.content.ComponentName.WithComponentName) obj);
                }
            };
        }
        if ("all-platform-non-critical".equals(filterString)) {
            return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda6
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.internal.util.jobs.DumpUtils.isPlatformNonCriticalPackage((android.content.ComponentName.WithComponentName) obj);
                }
            };
        }
        final android.content.ComponentName filterCname = android.content.ComponentName.unflattenFromString(filterString);
        if (filterCname != null) {
            return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda7
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.internal.util.jobs.DumpUtils.lambda$filterRecord$1(filterCname, (android.content.ComponentName.WithComponentName) obj);
                }
            };
        }
        final int id = com.android.internal.util.jobs.ParseUtils.parseIntWithBase(filterString, 16, -1);
        return new java.util.function.Predicate() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.internal.util.jobs.DumpUtils.lambda$filterRecord$2(id, filterString, (android.content.ComponentName.WithComponentName) obj);
            }
        };
    }

    static /* synthetic */ boolean lambda$filterRecord$0(android.content.ComponentName.WithComponentName rec) {
        return false;
    }

    static /* synthetic */ boolean lambda$filterRecord$1(android.content.ComponentName filterCname, android.content.ComponentName.WithComponentName rec) {
        return rec != null && filterCname.equals(rec.getComponentName());
    }

    static /* synthetic */ boolean lambda$filterRecord$2(int id, java.lang.String filterString, android.content.ComponentName.WithComponentName rec) {
        android.content.ComponentName cn = rec.getComponentName();
        return (id != -1 && java.lang.System.identityHashCode(rec) == id) || cn.flattenToString().toLowerCase().contains(filterString.toLowerCase());
    }

    public static void dumpSparseArray(java.io.PrintWriter pw, java.lang.String prefix, android.util.SparseArray<?> array, java.lang.String name) {
        dumpSparseArray(pw, prefix, array, name, null, null);
    }

    public static <T> void dumpSparseArrayValues(final java.io.PrintWriter pw, final java.lang.String prefix, android.util.SparseArray<T> array, java.lang.String name) {
        dumpSparseArray(pw, prefix, array, name, new com.android.internal.util.jobs.DumpUtils.KeyDumper() { // from class: com.android.internal.util.jobs.DumpUtils$$ExternalSyntheticLambda0
            @Override // com.android.internal.util.jobs.DumpUtils.KeyDumper
            public final void dump(int i, int i2) {
                java.io.PrintWriter printWriter = pw;
                java.lang.String str = prefix;
                printWriter.printf("%s%s", str, str);
            }
        }, null);
    }

    public static <T> void dumpSparseArray(java.io.PrintWriter pw, java.lang.String prefix, android.util.SparseArray<T> array, java.lang.String name, com.android.internal.util.jobs.DumpUtils.KeyDumper keyDumper, com.android.internal.util.jobs.DumpUtils.ValueDumper<T> valueDumper) {
        int size = array.size();
        if (size == 0) {
            pw.print(prefix);
            pw.print("No ");
            pw.print(name);
            pw.println("s");
            return;
        }
        pw.print(prefix);
        pw.print(size);
        pw.print(' ');
        pw.print(name);
        pw.println("(s):");
        java.lang.String prefix2 = prefix + prefix;
        for (int i = 0; i < size; i++) {
            int key = array.keyAt(i);
            T value = array.valueAt(i);
            if (keyDumper != null) {
                keyDumper.dump(i, key);
            } else {
                pw.print(prefix2);
                pw.print(i);
                pw.print(": ");
                pw.print(key);
                pw.print("->");
            }
            if (value == null) {
                pw.print("(null)");
            } else if (valueDumper != null) {
                valueDumper.dump(value);
            } else {
                pw.print(value);
            }
            pw.println();
        }
    }
}
