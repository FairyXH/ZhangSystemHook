package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
final class VoiceInteractionManagerServiceShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = "VoiceInteractionManager";
    private static final long TIMEOUT_MS = 5000;
    private final com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub mService;

    VoiceInteractionManagerServiceShellCommand(com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r3) {
        /*
            r2 = this;
            if (r3 != 0) goto L7
            int r0 = r2.handleDefaultCommands(r3)
            return r0
        L7:
            java.io.PrintWriter r0 = r2.getOutPrintWriter()
            int r1 = r3.hashCode()
            switch(r1) {
                case -1097066044: goto L3d;
                case 3202370: goto L33;
                case 3529469: goto L28;
                case 1671308008: goto L1e;
                case 1718895687: goto L13;
                default: goto L12;
            }
        L12:
            goto L48
        L13:
            java.lang.String r1 = "restart-detection"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 3
            goto L49
        L1e:
            java.lang.String r1 = "disable"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 2
            goto L49
        L28:
            java.lang.String r1 = "show"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 0
            goto L49
        L33:
            java.lang.String r1 = "hide"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 1
            goto L49
        L3d:
            java.lang.String r1 = "set-debug-hotword-logging"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 4
            goto L49
        L48:
            r1 = -1
        L49:
            switch(r1) {
                case 0: goto L65;
                case 1: goto L60;
                case 2: goto L5b;
                case 3: goto L56;
                case 4: goto L51;
                default: goto L4c;
            }
        L4c:
            int r1 = r2.handleDefaultCommands(r3)
            return r1
        L51:
            int r1 = r2.setDebugHotwordLogging(r0)
            return r1
        L56:
            int r1 = r2.requestRestartDetection(r0)
            return r1
        L5b:
            int r1 = r2.requestDisable(r0)
            return r1
        L60:
            int r1 = r2.requestHide(r0)
            return r1
        L65:
            int r1 = r2.requestShow(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.voiceinteraction.VoiceInteractionManagerServiceShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        try {
            pw.println("VoiceInteraction Service (voiceinteraction) commands:");
            pw.println("  help");
            pw.println("    Prints this help text.");
            pw.println("");
            pw.println("  show");
            pw.println("    Shows a session for the active service");
            pw.println("");
            pw.println("  hide");
            pw.println("    Hides the current session");
            pw.println("");
            pw.println("  disable [true|false]");
            pw.println("    Temporarily disable (when true) service");
            pw.println("");
            pw.println("  restart-detection");
            pw.println("    Force a restart of a hotword detection service");
            pw.println("");
            pw.println("  set-debug-hotword-logging [true|false]");
            pw.println("    Temporarily enable or disable debug logging for hotword result.");
            pw.println("    The debug logging will be reset after one hour from last enable.");
            pw.println("");
            if (pw != null) {
                pw.close();
            }
        } catch (java.lang.Throwable th) {
            if (pw != null) {
                try {
                    pw.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private int requestShow(final java.io.PrintWriter pw) {
        android.util.Slog.i(TAG, "requestShow()");
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        final java.util.concurrent.atomic.AtomicInteger result = new java.util.concurrent.atomic.AtomicInteger();
        com.android.internal.app.IVoiceInteractionSessionShowCallback callback = new com.android.internal.app.IVoiceInteractionSessionShowCallback.Stub() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceShellCommand.1
            public void onFailed() throws android.os.RemoteException {
                android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionManagerServiceShellCommand.TAG, "onFailed()");
                pw.println("callback failed");
                result.set(1);
                latch.countDown();
            }

            public void onShown() throws android.os.RemoteException {
                android.util.Slog.d(com.android.server.voiceinteraction.VoiceInteractionManagerServiceShellCommand.TAG, "onShown()");
                result.set(0);
                latch.countDown();
            }
        };
        try {
            android.os.Bundle args = new android.os.Bundle();
            boolean ok = this.mService.showSessionForActiveService(args, 0, null, callback, null);
            if (!ok) {
                pw.println("showSessionForActiveService() returned false");
                return 1;
            }
            if (!latch.await(TIMEOUT_MS, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                pw.printf("Callback not called in %d ms\n", java.lang.Long.valueOf(TIMEOUT_MS));
                return 1;
            }
            return 0;
        } catch (java.lang.Exception e) {
            return handleError(pw, "showSessionForActiveService()", e);
        }
    }

    private int requestHide(java.io.PrintWriter pw) {
        android.util.Slog.i(TAG, "requestHide()");
        try {
            this.mService.hideCurrentSession();
            return 0;
        } catch (java.lang.Exception e) {
            return handleError(pw, "requestHide()", e);
        }
    }

    private int requestDisable(java.io.PrintWriter pw) {
        boolean disabled = java.lang.Boolean.parseBoolean(getNextArgRequired());
        android.util.Slog.i(TAG, "requestDisable(): " + disabled);
        try {
            this.mService.setDisabled(disabled);
            return 0;
        } catch (java.lang.Exception e) {
            return handleError(pw, "requestDisable()", e);
        }
    }

    private int requestRestartDetection(java.io.PrintWriter pw) {
        android.util.Slog.i(TAG, "requestRestartDetection()");
        try {
            this.mService.forceRestartHotwordDetector();
            return 0;
        } catch (java.lang.Exception e) {
            return handleError(pw, "requestRestartDetection()", e);
        }
    }

    private int setDebugHotwordLogging(java.io.PrintWriter pw) {
        boolean logging = java.lang.Boolean.parseBoolean(getNextArgRequired());
        android.util.Slog.i(TAG, "setDebugHotwordLogging(): " + logging);
        try {
            this.mService.setDebugHotwordLogging(logging);
            return 0;
        } catch (java.lang.Exception e) {
            return handleError(pw, "setDebugHotwordLogging()", e);
        }
    }

    private static int handleError(java.io.PrintWriter pw, java.lang.String message, java.lang.Exception e) {
        android.util.Slog.e(TAG, "error calling " + message, e);
        pw.printf("Error calling %s: %s\n", message, e);
        return 1;
    }
}
