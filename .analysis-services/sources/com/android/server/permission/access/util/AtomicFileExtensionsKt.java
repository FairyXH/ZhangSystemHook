package com.android.server.permission.access.util;

/* JADX INFO: compiled from: AtomicFileExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a!\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b\u001a!\u0010\b\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u0004H\u0086\b¨\u0006\t"}, d2 = {"readWithReserveCopy", "", "Landroid/util/AtomicFile;", "block", "Lkotlin/Function1;", "Ljava/io/FileInputStream;", "writeInlined", "Ljava/io/FileOutputStream;", "writeWithReserveCopy", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class AtomicFileExtensionsKt {
    public static final void readWithReserveCopy(android.util.AtomicFile $this$readWithReserveCopy, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.io.FileInputStream, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.lang.Exception {
        java.io.FileInputStream fileInputStreamOpenRead;
        try {
            fileInputStreamOpenRead = $this$readWithReserveCopy.openRead();
            try {
                function1.invoke(fileInputStreamOpenRead);
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStreamOpenRead, null);
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            } catch (java.lang.Throwable th) {
                try {
                    throw th;
                } finally {
                }
            }
        } catch (java.io.FileNotFoundException e) {
            throw e;
        } catch (java.lang.Exception e2) {
            android.util.Slog.wtf("AccessPersistence", "Failed to read " + $this$readWithReserveCopy, e2);
            java.io.File reserveFile = new java.io.File($this$readWithReserveCopy.getBaseFile().getParentFile(), $this$readWithReserveCopy.getBaseFile().getName() + ".reservecopy");
            try {
                fileInputStreamOpenRead = new android.util.AtomicFile(reserveFile).openRead();
                try {
                    function1.invoke(fileInputStreamOpenRead);
                    com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                    com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStreamOpenRead, null);
                    com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                } finally {
                }
            } catch (java.lang.Exception e22) {
                android.util.Slog.e("AccessPersistence", "Failed to read " + reserveFile, e22);
                throw e2;
            }
        }
    }

    public static final void writeWithReserveCopy(android.util.AtomicFile $this$writeWithReserveCopy, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.io.FileOutputStream, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.io.IOException {
        java.io.FileOutputStream fileOutputStreamStartWrite = $this$writeWithReserveCopy.startWrite();
        try {
            java.io.FileOutputStream it$iv = fileOutputStreamStartWrite;
            try {
                function1.invoke(it$iv);
                $this$writeWithReserveCopy.finishWrite(it$iv);
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, null);
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                java.io.File reserveFile = new java.io.File($this$writeWithReserveCopy.getBaseFile().getParentFile(), $this$writeWithReserveCopy.getBaseFile().getName() + ".reservecopy");
                try {
                    fileOutputStreamStartWrite = new java.io.FileInputStream($this$writeWithReserveCopy.getBaseFile());
                    try {
                        java.io.FileInputStream inputStream = fileOutputStreamStartWrite;
                        fileOutputStreamStartWrite = new java.io.FileOutputStream(reserveFile);
                        try {
                            java.io.FileOutputStream outputStream = fileOutputStreamStartWrite;
                            android.os.FileUtils.copy(inputStream, outputStream);
                            outputStream.getFD().sync();
                            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, null);
                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                            com.android.server.permission.jarjar.kotlin.Unit unit3 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, null);
                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                        } finally {
                        }
                    } finally {
                    }
                } catch (java.lang.Exception e) {
                    android.util.Slog.e("AccessPersistence", "Failed to write " + reserveFile, e);
                }
            } catch (java.lang.Throwable t$iv) {
                $this$writeWithReserveCopy.failWrite(it$iv);
                throw t$iv;
            }
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } finally {
            }
        }
    }

    public static final void writeInlined(android.util.AtomicFile $this$writeInlined, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.io.FileOutputStream, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.io.IOException {
        java.io.FileOutputStream fileOutputStreamStartWrite = $this$writeInlined.startWrite();
        try {
            java.io.FileOutputStream it = fileOutputStreamStartWrite;
            try {
                function1.invoke(it);
                $this$writeInlined.finishWrite(it);
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, null);
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            } catch (java.lang.Throwable t) {
                $this$writeInlined.failWrite(it);
                throw t;
            }
        } finally {
        }
    }
}
