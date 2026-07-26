package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public final class PriorityDump {
    public static final java.lang.String PRIORITY_ARG = "--dump-priority";
    public static final java.lang.String PRIORITY_ARG_CRITICAL = "CRITICAL";
    public static final java.lang.String PRIORITY_ARG_HIGH = "HIGH";
    public static final java.lang.String PRIORITY_ARG_NORMAL = "NORMAL";
    private static final int PRIORITY_TYPE_CRITICAL = 1;
    private static final int PRIORITY_TYPE_HIGH = 2;
    private static final int PRIORITY_TYPE_INVALID = 0;
    private static final int PRIORITY_TYPE_NORMAL = 3;
    public static final java.lang.String PROTO_ARG = "--proto";

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface PriorityType {
    }

    private PriorityDump() {
        throw new java.lang.UnsupportedOperationException();
    }

    public static void dump(com.android.server.utils.PriorityDump.PriorityDumper dumper, java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        boolean asProto = false;
        int priority = 0;
        if (args == null) {
            dumper.dump(fd, pw, args, false);
        }
        java.lang.String[] strippedArgs = new java.lang.String[args.length];
        int strippedCount = 0;
        int argIndex = 0;
        while (argIndex < args.length) {
            if (args[argIndex].equals("--proto")) {
                asProto = true;
            } else if (args[argIndex].equals(PRIORITY_ARG)) {
                if (argIndex + 1 < args.length) {
                    argIndex++;
                    priority = getPriorityType(args[argIndex]);
                }
            } else {
                strippedArgs[strippedCount] = args[argIndex];
                strippedCount++;
            }
            argIndex++;
        }
        int argIndex2 = args.length;
        if (strippedCount < argIndex2) {
            strippedArgs = (java.lang.String[]) java.util.Arrays.copyOf(strippedArgs, strippedCount);
        }
        switch (priority) {
            case 1:
                dumper.dumpCritical(fd, pw, strippedArgs, asProto);
                break;
            case 2:
                dumper.dumpHigh(fd, pw, strippedArgs, asProto);
                break;
            case 3:
                dumper.dumpNormal(fd, pw, strippedArgs, asProto);
                break;
            default:
                dumper.dump(fd, pw, strippedArgs, asProto);
                break;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int getPriorityType(java.lang.String r4) {
        /*
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 2
            r3 = 1
            switch(r0) {
                case -1986416409: goto L1f;
                case -1560189025: goto L15;
                case 2217378: goto Lb;
                default: goto La;
            }
        La:
            goto L29
        Lb:
            java.lang.String r0 = "HIGH"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r3
            goto L2a
        L15:
            java.lang.String r0 = "CRITICAL"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r1
            goto L2a
        L1f:
            java.lang.String r0 = "NORMAL"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r2
            goto L2a
        L29:
            r0 = -1
        L2a:
            switch(r0) {
                case 0: goto L31;
                case 1: goto L30;
                case 2: goto L2e;
                default: goto L2d;
            }
        L2d:
            return r1
        L2e:
            r0 = 3
            return r0
        L30:
            return r2
        L31:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.utils.PriorityDump.getPriorityType(java.lang.String):int");
    }

    public interface PriorityDumper {
        default void dumpCritical(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
        }

        default void dumpHigh(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
        }

        default void dumpNormal(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
        }

        default void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, boolean asProto) {
            dumpCritical(fd, pw, args, asProto);
            dumpHigh(fd, pw, args, asProto);
            dumpNormal(fd, pw, args, asProto);
        }
    }
}
