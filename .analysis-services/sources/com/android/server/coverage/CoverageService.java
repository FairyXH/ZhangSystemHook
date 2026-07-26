package com.android.server.coverage;

/* JADX INFO: loaded from: classes.dex */
public class CoverageService extends android.os.Binder {
    public static final java.lang.String COVERAGE_SERVICE = "coverage";
    public static final boolean ENABLED;

    static {
        boolean shouldEnable = true;
        try {
            java.lang.Class.forName("org.jacoco.agent.rt.RT");
        } catch (java.lang.ClassNotFoundException e) {
            shouldEnable = false;
        }
        ENABLED = shouldEnable;
    }

    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.coverage.CoverageService.CoverageCommand().exec(this, in, out, err, args, callback, resultReceiver);
    }

    private static class CoverageCommand extends android.os.ShellCommand {
        private CoverageCommand() {
        }

        public int onCommand(java.lang.String cmd) {
            if ("dump".equals(cmd)) {
                return onDump();
            }
            if ("reset".equals(cmd)) {
                return onReset();
            }
            return handleDefaultCommands(cmd);
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("Coverage commands:");
            pw.println("  help");
            pw.println("    Print this help text.");
            pw.println("  dump [FILE]");
            pw.println("    Dump code coverage to FILE.");
            pw.println("  reset");
            pw.println("    Reset coverage information.");
        }

        private int onDump() {
            java.lang.String dest = getNextArg();
            if (dest == null) {
                dest = "/data/local/tmp/coverage.ec";
            } else {
                java.io.File f = new java.io.File(dest);
                if (f.isDirectory()) {
                    dest = new java.io.File(f, "coverage.ec").getAbsolutePath();
                }
            }
            android.os.ParcelFileDescriptor fd = openFileForSystem(dest, "w");
            if (fd == null) {
                return -1;
            }
            try {
                java.io.BufferedOutputStream output = new java.io.BufferedOutputStream(new android.os.ParcelFileDescriptor.AutoCloseOutputStream(fd));
                try {
                    output.write(org.jacoco.agent.rt.RT.getAgent().getExecutionData(false));
                    output.flush();
                    getOutPrintWriter().println(java.lang.String.format("Dumped coverage data to %s", dest));
                    output.close();
                    return 0;
                } finally {
                }
            } catch (java.io.IOException e) {
                getErrPrintWriter().println("Failed to dump coverage data: " + e.getMessage());
                return -1;
            }
        }

        private int onReset() {
            org.jacoco.agent.rt.RT.getAgent().reset();
            getOutPrintWriter().println("Reset coverage data");
            return 0;
        }
    }
}
