package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ShortcutDumpFiles {
    private static final boolean DEBUG = com.android.server.pm.ShortcutService.DEBUG;
    private static final java.lang.String TAG = "ShortcutService";
    private final com.android.server.pm.ShortcutService mService;

    public ShortcutDumpFiles(com.android.server.pm.ShortcutService service) {
        this.mService = service;
    }

    public boolean save(java.lang.String filename, java.util.function.Consumer<java.io.PrintWriter> dumper) {
        try {
            java.io.File directory = this.mService.getDumpPath();
            directory.mkdirs();
            if (!directory.exists()) {
                android.util.Slog.e(TAG, "Failed to create directory: " + directory);
                return false;
            }
            java.io.File path = new java.io.File(directory, filename);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Dumping to " + path);
            }
            java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.BufferedOutputStream(new java.io.FileOutputStream(path)));
            try {
                dumper.accept(pw);
                pw.close();
                return true;
            } catch (java.lang.Throwable th) {
                try {
                    pw.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (java.io.IOException | java.lang.RuntimeException e) {
            android.util.Slog.w(TAG, "Failed to create dump file: " + filename, e);
            return false;
        }
    }

    public boolean save(java.lang.String filename, final byte[] utf8bytes) {
        return save(filename, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutDumpFiles$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((java.io.PrintWriter) obj).println(java.nio.charset.StandardCharsets.UTF_8.decode(java.nio.ByteBuffer.wrap(utf8bytes)).toString());
            }
        });
    }

    public void dumpAll(java.io.PrintWriter pw) {
        try {
            java.io.File directory = this.mService.getDumpPath();
            java.io.File[] files = directory.listFiles(new java.io.FileFilter() { // from class: com.android.server.pm.ShortcutDumpFiles$$ExternalSyntheticLambda1
                @Override // java.io.FileFilter
                public final boolean accept(java.io.File file) {
                    return file.isFile();
                }
            });
            if (directory.exists() && !com.android.internal.util.ArrayUtils.isEmpty(files)) {
                java.util.Arrays.sort(files, java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutDumpFiles$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return ((java.io.File) obj).getName();
                    }
                }));
                for (java.io.File path : files) {
                    pw.print("*** Dumping: ");
                    pw.println(path.getName());
                    pw.print("mtime: ");
                    pw.println(com.android.server.pm.ShortcutService.formatTime(path.lastModified()));
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(path)));
                    while (true) {
                        try {
                            java.lang.String line = reader.readLine();
                            if (line == null) {
                                break;
                            } else {
                                pw.println(line);
                            }
                        } catch (java.lang.Throwable th) {
                            try {
                                reader.close();
                            } catch (java.lang.Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                    }
                    reader.close();
                }
                return;
            }
            pw.print("  No dump files found.");
        } catch (java.io.IOException | java.lang.RuntimeException e) {
            android.util.Slog.w(TAG, "Failed to print dump files", e);
        }
    }
}
