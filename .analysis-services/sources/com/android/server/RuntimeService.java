package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class RuntimeService extends android.os.Binder {
    private static final java.lang.String TAG = "RuntimeService";
    private final android.content.Context mContext;

    public RuntimeService(android.content.Context context) {
        this.mContext = context;
    }

    @Override // android.os.Binder
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mContext, TAG, pw)) {
            return;
        }
        boolean protoFormat = hasOption(args, "--proto");
        android.util.proto.ProtoOutputStream proto = null;
        com.android.i18n.timezone.DebugInfo i18nLibraryDebugInfo = com.android.i18n.timezone.I18nModuleDebug.getDebugInfo();
        if (protoFormat) {
            proto = new android.util.proto.ProtoOutputStream(fd);
            reportTimeZoneInfoProto(i18nLibraryDebugInfo, proto);
        } else {
            reportTimeZoneInfo(i18nLibraryDebugInfo, pw);
        }
        if (protoFormat) {
            proto.flush();
        }
    }

    private static boolean hasOption(java.lang.String[] args, java.lang.String arg) {
        for (java.lang.String opt : args) {
            if (arg.equals(opt)) {
                return true;
            }
        }
        return false;
    }

    private static void reportTimeZoneInfo(com.android.i18n.timezone.DebugInfo coreLibraryDebugInfo, java.io.PrintWriter pw) {
        pw.println("Core Library Debug Info: ");
        for (com.android.i18n.timezone.DebugInfo.DebugEntry debugEntry : coreLibraryDebugInfo.getDebugEntries()) {
            pw.print(debugEntry.getKey());
            pw.print(": \"");
            pw.print(debugEntry.getStringValue());
            pw.println("\"");
        }
    }

    private static void reportTimeZoneInfoProto(com.android.i18n.timezone.DebugInfo coreLibraryDebugInfo, android.util.proto.ProtoOutputStream protoStream) {
        for (com.android.i18n.timezone.DebugInfo.DebugEntry debugEntry : coreLibraryDebugInfo.getDebugEntries()) {
            long entryToken = protoStream.start(2246267895809L);
            protoStream.write(1138166333441L, debugEntry.getKey());
            protoStream.write(1138166333442L, debugEntry.getStringValue());
            protoStream.end(entryToken);
        }
    }
}
