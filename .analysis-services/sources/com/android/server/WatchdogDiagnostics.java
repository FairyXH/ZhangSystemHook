package com.android.server;

/* JADX INFO: loaded from: classes.dex */
class WatchdogDiagnostics {
    WatchdogDiagnostics() {
    }

    private static java.lang.String getBlockedOnString(java.lang.Object blockedOn) {
        return java.lang.String.format("- waiting to lock <0x%08x> (a %s)", java.lang.Integer.valueOf(java.lang.System.identityHashCode(blockedOn)), blockedOn.getClass().getName());
    }

    private static java.lang.String getLockedString(java.lang.Object heldLock) {
        return java.lang.String.format("- locked <0x%08x> (a %s)", java.lang.Integer.valueOf(java.lang.System.identityHashCode(heldLock)), heldLock.getClass().getName());
    }

    public static boolean printAnnotatedStack(java.lang.Thread thread, java.io.PrintWriter out) {
        dalvik.system.AnnotatedStackTraceElement[] stack = dalvik.system.VMStack.getAnnotatedThreadStackTrace(thread);
        if (stack == null) {
            return false;
        }
        out.println(thread.getName() + " annotated stack trace:");
        for (dalvik.system.AnnotatedStackTraceElement element : stack) {
            out.println("    at " + element.getStackTraceElement());
            if (element.getBlockedOn() != null) {
                out.println("    " + getBlockedOnString(element.getBlockedOn()));
            }
            if (element.getHeldLocks() != null) {
                for (java.lang.Object held : element.getHeldLocks()) {
                    out.println("    " + getLockedString(held));
                }
            }
        }
        return true;
    }

    public static void diagnoseCheckers(java.util.List<com.android.server.Watchdog.HandlerChecker> blockedCheckers) {
        java.io.PrintWriter out = new java.io.PrintWriter((java.io.Writer) new android.util.LogWriter(5, "Watchdog", 3), true);
        for (int i = 0; i < blockedCheckers.size(); i++) {
            java.lang.Thread blockedThread = blockedCheckers.get(i).getThread();
            if (!printAnnotatedStack(blockedThread, out)) {
                android.util.Slog.w("Watchdog", blockedThread.getName() + " stack trace:");
                java.lang.StackTraceElement[] stackTrace = blockedThread.getStackTrace();
                for (java.lang.StackTraceElement element : stackTrace) {
                    android.util.Slog.w("Watchdog", "    at " + element);
                }
            }
        }
    }
}
