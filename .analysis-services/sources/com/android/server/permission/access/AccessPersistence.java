package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessPersistence.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 )2\u00020\u0001:\u0002)*B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0012\u001a\u00020\f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0006\u0010\u0015\u001a\u00020\u0016J\u000e\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019J\u0010\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0018\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u000e\u0010\u001c\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\tJ\u0010\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0010\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\tH\u0002J\u0018\u0010\u001f\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J&\u0010 \u001a\u00020!*\u00020\f2\u0017\u0010\"\u001a\u0013\u0012\u0004\u0012\u00020$\u0012\u0004\u0012\u00020\u00160#¢\u0006\u0002\b%H\u0082\bJ&\u0010&\u001a\u00020\u0016*\u00020\f2\u0017\u0010\"\u001a\u0013\u0012\u0004\u0012\u00020'\u0012\u0004\u0012\u00020\u00160#¢\u0006\u0002\b%H\u0082\bJ\u001c\u0010\u001c\u001a\u00020\u0016*\u00020(2\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0013\u001a\u00020\u0014H\u0002R\u0010\u0010\u0005\u001a\u00020\u00068\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u000f\u001a\u00060\u0010R\u00020\u00008\u0002@\u0002X\u0083.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006+"}, d2 = {"Lcom/android/server/permission/access/AccessPersistence;", "", "policy", "Lcom/android/server/permission/access/AccessPolicy;", "(Lcom/android/server/permission/access/AccessPolicy;)V", "pendingMutationTimesMillis", "Landroid/util/SparseLongArray;", "pendingStates", "Lcom/android/server/permission/access/immutable/MutableIntMap;", "Lcom/android/server/permission/access/AccessState;", "scheduleLock", "systemFile", "Ljava/io/File;", "getSystemFile", "()Ljava/io/File;", "writeHandler", "Lcom/android/server/permission/access/AccessPersistence$WriteHandler;", "writeLock", "getUserFile", "userId", "", "initialize", "", "read", "state", "Lcom/android/server/permission/access/MutableAccessState;", "readSystemState", "readUserState", "write", "writePendingState", "writeSystemState", "writeUserState", "parse", "", "block", "Lkotlin/Function1;", "Lcom/android/modules/utils/BinaryXmlPullParser;", "Lkotlin/ExtensionFunctionType;", "serialize", "Lcom/android/modules/utils/BinaryXmlSerializer;", "Lcom/android/server/permission/access/WritableState;", "Companion", "WriteHandler", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AccessPersistence {
    private static final java.lang.String FILE_NAME = "access.abx";
    private static final long MAX_WRITE_DELAY_MILLIS = 2000;
    private static final long WRITE_DELAY_TIME_MILLIS = 1000;
    private final com.android.server.permission.access.AccessPolicy policy;
    private com.android.server.permission.access.AccessPersistence.WriteHandler writeHandler;
    public static final com.android.server.permission.access.AccessPersistence.Companion Companion = new com.android.server.permission.access.AccessPersistence.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.AccessPersistence.class.getSimpleName();
    private final java.lang.Object scheduleLock = new java.lang.Object();
    private final android.util.SparseLongArray pendingMutationTimesMillis = new android.util.SparseLongArray();
    private final com.android.server.permission.access.immutable.MutableIntMap<com.android.server.permission.access.AccessState> pendingStates = new com.android.server.permission.access.immutable.MutableIntMap<>(null, 1, null);
    private final java.lang.Object writeLock = new java.lang.Object();

    public AccessPersistence(com.android.server.permission.access.AccessPolicy policy) {
        this.policy = policy;
    }

    public final void initialize() {
        this.writeHandler = new com.android.server.permission.access.AccessPersistence.WriteHandler(com.android.server.IoThread.getHandler().getLooper());
    }

    public final void read(com.android.server.permission.access.MutableAccessState state) {
        readSystemState(state);
        com.android.server.permission.access.immutable.IntSet $this$forEachIndexed$iv = state.getExternalState().getUserIds();
        int size = $this$forEachIndexed$iv.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int userId = $this$forEachIndexed$iv.elementAt(index$iv);
            readUserState(state, userId);
        }
    }

    private final void readSystemState(com.android.server.permission.access.MutableAccessState state) {
        boolean fileExists;
        java.lang.Throwable th;
        java.io.File $this$parse$iv = getSystemFile();
        try {
            try {
                android.util.AtomicFile $this$readWithReserveCopy$iv$iv = new android.util.AtomicFile($this$parse$iv);
                try {
                    try {
                        try {
                            try {
                                try {
                                    java.io.InputStream inputStreamOpenRead = $this$readWithReserveCopy$iv$iv.openRead();
                                    try {
                                        java.io.InputStream it$iv = (java.io.FileInputStream) inputStreamOpenRead;
                                        java.io.InputStream $this$parseBinaryXml$iv$iv = it$iv;
                                        com.android.modules.utils.BinaryXmlPullParser $this$parseBinaryXml_u24lambda_u240$iv$iv = new com.android.modules.utils.BinaryXmlPullParser();
                                        $this$parseBinaryXml_u24lambda_u240$iv$iv.setInput($this$parseBinaryXml$iv$iv, (java.lang.String) null);
                                        com.android.server.permission.access.AccessPolicy $this$readSystemState_u24lambda_u242_u24lambda_u241 = this.policy;
                                        $this$readSystemState_u24lambda_u242_u24lambda_u241.parseSystemState($this$parseBinaryXml_u24lambda_u240$iv$iv, state);
                                        com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead, null);
                                    } catch (java.lang.Throwable th2) {
                                        try {
                                            throw th2;
                                        } catch (java.lang.Throwable th3) {
                                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead, th2);
                                            throw th3;
                                        }
                                    }
                                } catch (java.io.FileNotFoundException e$iv$iv) {
                                    throw e$iv$iv;
                                }
                            } catch (java.io.FileNotFoundException e) {
                                android.util.Slog.i(LOG_TAG, $this$parse$iv + " not found");
                                fileExists = false;
                            }
                        } catch (java.io.FileNotFoundException e$iv$iv2) {
                            throw e$iv$iv2;
                        }
                    } catch (java.lang.Exception e$iv$iv3) {
                        android.util.Slog.wtf("AccessPersistence", "Failed to read " + $this$readWithReserveCopy$iv$iv, e$iv$iv3);
                        java.io.File reserveFile$iv$iv = new java.io.File($this$readWithReserveCopy$iv$iv.getBaseFile().getParentFile(), $this$readWithReserveCopy$iv$iv.getBaseFile().getName() + ".reservecopy");
                        try {
                            try {
                                java.io.InputStream inputStreamOpenRead2 = new android.util.AtomicFile(reserveFile$iv$iv).openRead();
                                try {
                                    java.io.InputStream it$iv2 = (java.io.FileInputStream) inputStreamOpenRead2;
                                    java.io.InputStream $this$parseBinaryXml$iv$iv2 = it$iv2;
                                    com.android.modules.utils.BinaryXmlPullParser $this$parseBinaryXml_u24lambda_u240$iv$iv2 = new com.android.modules.utils.BinaryXmlPullParser();
                                    try {
                                        $this$parseBinaryXml_u24lambda_u240$iv$iv2.setInput($this$parseBinaryXml$iv$iv2, (java.lang.String) null);
                                        com.android.server.permission.access.AccessPolicy $this$readSystemState_u24lambda_u242_u24lambda_u2412 = this.policy;
                                        $this$readSystemState_u24lambda_u242_u24lambda_u2412.parseSystemState($this$parseBinaryXml_u24lambda_u240$iv$iv2, state);
                                        com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead2, null);
                                    } catch (java.lang.Throwable th4) {
                                        th = th4;
                                        try {
                                            throw th;
                                        } catch (java.lang.Throwable th5) {
                                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead2, th);
                                            throw th5;
                                        }
                                    }
                                } catch (java.lang.Throwable th6) {
                                    th = th6;
                                }
                            } catch (java.lang.Exception e2) {
                                e2$iv$iv = e2;
                                android.util.Slog.e("AccessPersistence", "Failed to read " + reserveFile$iv$iv, e2$iv$iv);
                                throw e$iv$iv3;
                            }
                        } catch (java.lang.Exception e3) {
                            e2$iv$iv = e3;
                            android.util.Slog.e("AccessPersistence", "Failed to read " + reserveFile$iv$iv, e2$iv$iv);
                            throw e$iv$iv3;
                        }
                    }
                    fileExists = true;
                } catch (java.lang.Exception e4) {
                    e$iv = e4;
                    throw new java.lang.IllegalStateException("Failed to read " + $this$parse$iv, e$iv);
                }
            } catch (java.io.FileNotFoundException e5) {
            }
            if (fileExists) {
                return;
            }
            this.policy.migrateSystemState(state);
            write(state.getSystemState(), state, -1);
        } catch (java.lang.Exception e6) {
            e$iv = e6;
        }
    }

    private final void readUserState(com.android.server.permission.access.MutableAccessState state, int userId) {
        boolean fileExists;
        android.util.AtomicFile $this$readWithReserveCopy$iv$iv;
        java.lang.Throwable th;
        java.io.File $this$parse$iv = getUserFile(userId);
        try {
            try {
                $this$readWithReserveCopy$iv$iv = new android.util.AtomicFile($this$parse$iv);
            } catch (java.io.FileNotFoundException e) {
            }
            try {
                try {
                    try {
                        try {
                            try {
                                java.io.InputStream inputStreamOpenRead = $this$readWithReserveCopy$iv$iv.openRead();
                                try {
                                    java.io.InputStream it$iv = (java.io.FileInputStream) inputStreamOpenRead;
                                    java.io.InputStream $this$parseBinaryXml$iv$iv = it$iv;
                                    com.android.modules.utils.BinaryXmlPullParser $this$parseBinaryXml_u24lambda_u240$iv$iv = new com.android.modules.utils.BinaryXmlPullParser();
                                    $this$parseBinaryXml_u24lambda_u240$iv$iv.setInput($this$parseBinaryXml$iv$iv, (java.lang.String) null);
                                    com.android.server.permission.access.AccessPolicy $this$readUserState_u24lambda_u244_u24lambda_u243 = this.policy;
                                    $this$readUserState_u24lambda_u244_u24lambda_u243.parseUserState($this$parseBinaryXml_u24lambda_u240$iv$iv, state, userId);
                                    com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                    com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead, null);
                                } catch (java.lang.Throwable th2) {
                                    try {
                                        throw th2;
                                    } catch (java.lang.Throwable th3) {
                                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead, th2);
                                        throw th3;
                                    }
                                }
                            } catch (java.io.FileNotFoundException e$iv$iv) {
                                throw e$iv$iv;
                            }
                        } catch (java.io.FileNotFoundException e2) {
                            android.util.Slog.i(LOG_TAG, $this$parse$iv + " not found");
                            fileExists = false;
                        }
                    } catch (java.io.FileNotFoundException e$iv$iv2) {
                        throw e$iv$iv2;
                    }
                } catch (java.lang.Exception e$iv$iv3) {
                    android.util.Slog.wtf("AccessPersistence", "Failed to read " + $this$readWithReserveCopy$iv$iv, e$iv$iv3);
                    java.io.File reserveFile$iv$iv = new java.io.File($this$readWithReserveCopy$iv$iv.getBaseFile().getParentFile(), $this$readWithReserveCopy$iv$iv.getBaseFile().getName() + ".reservecopy");
                    try {
                        try {
                            java.io.InputStream inputStreamOpenRead2 = new android.util.AtomicFile(reserveFile$iv$iv).openRead();
                            try {
                                java.io.InputStream it$iv2 = (java.io.FileInputStream) inputStreamOpenRead2;
                                java.io.InputStream $this$parseBinaryXml$iv$iv2 = it$iv2;
                                com.android.modules.utils.BinaryXmlPullParser $this$parseBinaryXml_u24lambda_u240$iv$iv2 = new com.android.modules.utils.BinaryXmlPullParser();
                                try {
                                    $this$parseBinaryXml_u24lambda_u240$iv$iv2.setInput($this$parseBinaryXml$iv$iv2, (java.lang.String) null);
                                    com.android.server.permission.access.AccessPolicy $this$readUserState_u24lambda_u244_u24lambda_u2432 = this.policy;
                                    $this$readUserState_u24lambda_u244_u24lambda_u2432.parseUserState($this$parseBinaryXml_u24lambda_u240$iv$iv2, state, userId);
                                    com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                    com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead2, null);
                                } catch (java.lang.Throwable th4) {
                                    th = th4;
                                    try {
                                        throw th;
                                    } catch (java.lang.Throwable th5) {
                                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead2, th);
                                        throw th5;
                                    }
                                }
                            } catch (java.lang.Throwable th6) {
                                th = th6;
                            }
                        } catch (java.lang.Exception e3) {
                            e2$iv$iv = e3;
                            android.util.Slog.e("AccessPersistence", "Failed to read " + reserveFile$iv$iv, e2$iv$iv);
                            throw e$iv$iv3;
                        }
                    } catch (java.lang.Exception e4) {
                        e2$iv$iv = e4;
                        android.util.Slog.e("AccessPersistence", "Failed to read " + reserveFile$iv$iv, e2$iv$iv);
                        throw e$iv$iv3;
                    }
                }
                fileExists = true;
                if (fileExists) {
                    return;
                }
                this.policy.migrateUserState(state, userId);
                com.android.server.permission.access.immutable.Immutable immutable = state.getUserStates().get(userId);
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
                write((com.android.server.permission.access.WritableState) immutable, state, userId);
            } catch (java.lang.Exception e5) {
                e$iv = e5;
                throw new java.lang.IllegalStateException("Failed to read " + $this$parse$iv, e$iv);
            }
        } catch (java.lang.Exception e6) {
            e$iv = e6;
        }
    }

    private final boolean parse(java.io.File $this$parse, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.modules.utils.BinaryXmlPullParser, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        try {
            try {
                android.util.AtomicFile $this$readWithReserveCopy$iv = new android.util.AtomicFile($this$parse);
                try {
                    java.io.InputStream inputStreamOpenRead = $this$readWithReserveCopy$iv.openRead();
                    try {
                        java.io.InputStream it = (java.io.FileInputStream) inputStreamOpenRead;
                        java.io.InputStream $this$parseBinaryXml$iv = it;
                        com.android.modules.utils.BinaryXmlPullParser $this$parseBinaryXml_u24lambda_u240$iv = new com.android.modules.utils.BinaryXmlPullParser();
                        $this$parseBinaryXml_u24lambda_u240$iv.setInput($this$parseBinaryXml$iv, (java.lang.String) null);
                        function1.invoke($this$parseBinaryXml_u24lambda_u240$iv);
                        com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead, null);
                        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                    } finally {
                    }
                } catch (java.io.FileNotFoundException e$iv) {
                    throw e$iv;
                } catch (java.lang.Exception e$iv2) {
                    android.util.Slog.wtf("AccessPersistence", "Failed to read " + $this$readWithReserveCopy$iv, e$iv2);
                    java.io.File reserveFile$iv = new java.io.File($this$readWithReserveCopy$iv.getBaseFile().getParentFile(), $this$readWithReserveCopy$iv.getBaseFile().getName() + ".reservecopy");
                    try {
                        java.io.InputStream inputStreamOpenRead2 = new android.util.AtomicFile(reserveFile$iv).openRead();
                        try {
                            java.io.InputStream it2 = (java.io.FileInputStream) inputStreamOpenRead2;
                            java.io.InputStream $this$parseBinaryXml$iv2 = it2;
                            com.android.modules.utils.BinaryXmlPullParser $this$parseBinaryXml_u24lambda_u240$iv2 = new com.android.modules.utils.BinaryXmlPullParser();
                            $this$parseBinaryXml_u24lambda_u240$iv2.setInput($this$parseBinaryXml$iv2, (java.lang.String) null);
                            function1.invoke($this$parseBinaryXml_u24lambda_u240$iv2);
                            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead2, null);
                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                        } catch (java.lang.Throwable th) {
                            try {
                                throw th;
                            } catch (java.lang.Throwable th2) {
                                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenRead2, th);
                                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                                throw th2;
                            }
                        }
                    } catch (java.lang.Exception e2$iv) {
                        android.util.Slog.e("AccessPersistence", "Failed to read " + reserveFile$iv, e2$iv);
                        throw e$iv2;
                    }
                }
                return true;
            } catch (java.io.FileNotFoundException e) {
                android.util.Slog.i(LOG_TAG, $this$parse + " not found");
                return false;
            }
        } catch (java.lang.Exception e2) {
            throw new java.lang.IllegalStateException("Failed to read " + $this$parse, e2);
        }
    }

    public final void write(com.android.server.permission.access.AccessState state) {
        write(state.getSystemState(), state, -1);
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = state.getUserStates();
        int size = userStates.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            int userId = userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            write(userState, state, userId);
        }
    }

    private final void write(com.android.server.permission.access.WritableState $this$write, com.android.server.permission.access.AccessState state, int userId) {
        long it$iv;
        int writeMode = $this$write.getWriteMode();
        switch (writeMode) {
            case 0:
                return;
            case 1:
                synchronized (this.scheduleLock) {
                    com.android.server.permission.access.AccessPersistence.WriteHandler writeHandler = this.writeHandler;
                    if (writeHandler == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                        writeHandler = null;
                    }
                    writeHandler.removeMessages(userId);
                    com.android.server.permission.access.immutable.IntMapExtensionsKt.set(this.pendingStates, userId, state);
                    long currentTimeMillis = android.os.SystemClock.uptimeMillis();
                    android.util.SparseLongArray $this$getOrPut$iv = this.pendingMutationTimesMillis;
                    int index$iv = $this$getOrPut$iv.indexOfKey(userId);
                    if (index$iv >= 0) {
                        it$iv = $this$getOrPut$iv.valueAt(index$iv);
                    } else {
                        $this$getOrPut$iv.put(userId, currentTimeMillis);
                        it$iv = currentTimeMillis;
                    }
                    long pendingMutationTimeMillis = it$iv;
                    long currentDelayMillis = currentTimeMillis - pendingMutationTimeMillis;
                    com.android.server.permission.access.AccessPersistence.WriteHandler writeHandler2 = this.writeHandler;
                    if (writeHandler2 == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                        writeHandler2 = null;
                    }
                    android.os.Message message = writeHandler2.obtainMessage(userId);
                    if (currentDelayMillis > MAX_WRITE_DELAY_MILLIS) {
                        message.sendToTarget();
                        com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                    } else {
                        long newDelayMillis = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceAtMost(1000L, MAX_WRITE_DELAY_MILLIS - currentDelayMillis);
                        com.android.server.permission.access.AccessPersistence.WriteHandler writeHandler3 = this.writeHandler;
                        if (writeHandler3 == null) {
                            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                            writeHandler3 = null;
                        }
                        java.lang.Boolean.valueOf(writeHandler3.sendMessageDelayed(message, newDelayMillis));
                    }
                }
                return;
            case 2:
                synchronized (this.scheduleLock) {
                    com.android.server.permission.access.immutable.IntMapExtensionsKt.set(this.pendingStates, userId, state);
                    com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                }
                writePendingState(userId);
                return;
            default:
                throw new java.lang.IllegalStateException(java.lang.Integer.valueOf(writeMode).toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v2, types: [T, java.lang.Object] */
    public final void writePendingState(int userId) {
        synchronized (this.writeLock) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef state = new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef();
            synchronized (this.scheduleLock) {
                android.util.SparseLongArray $this$minusAssign$iv = this.pendingMutationTimesMillis;
                $this$minusAssign$iv.delete(userId);
                state.element = this.pendingStates.remove(userId);
                com.android.server.permission.access.AccessPersistence.WriteHandler writeHandler = this.writeHandler;
                if (writeHandler == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("writeHandler");
                    writeHandler = null;
                }
                writeHandler.removeMessages(userId);
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            }
            if (state.element == 0) {
                return;
            }
            if (userId == -1) {
                writeSystemState((com.android.server.permission.access.AccessState) state.element);
            } else {
                writeUserState((com.android.server.permission.access.AccessState) state.element, userId);
            }
            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    private final void writeSystemState(com.android.server.permission.access.AccessState state) {
        java.lang.Throwable th;
        java.io.File reserveFile$iv$iv;
        java.io.File $this$serialize$iv = getSystemFile();
        try {
            try {
                android.util.AtomicFile $this$writeWithReserveCopy$iv$iv = new android.util.AtomicFile($this$serialize$iv);
                java.io.FileOutputStream fileOutputStreamStartWrite = $this$writeWithReserveCopy$iv$iv.startWrite();
                try {
                    java.io.FileOutputStream it$iv$iv$iv = fileOutputStreamStartWrite;
                    try {
                        try {
                            java.io.FileOutputStream $this$serializeBinaryXml$iv$iv = it$iv$iv$iv;
                            com.android.modules.utils.BinaryXmlSerializer $this$serializeBinaryXml_u24lambda_u240$iv$iv = new com.android.modules.utils.BinaryXmlSerializer();
                            $this$serializeBinaryXml_u24lambda_u240$iv$iv.setOutput($this$serializeBinaryXml$iv$iv, (java.lang.String) null);
                            try {
                                $this$serializeBinaryXml_u24lambda_u240$iv$iv.startDocument((java.lang.String) null, true);
                                try {
                                    com.android.server.permission.access.AccessPolicy $this$writeSystemState_u24lambda_u2413_u24lambda_u2412 = this.policy;
                                    try {
                                        $this$writeSystemState_u24lambda_u2413_u24lambda_u2412.serializeSystemState($this$serializeBinaryXml_u24lambda_u240$iv$iv, state);
                                        $this$serializeBinaryXml_u24lambda_u240$iv$iv.endDocument();
                                        $this$writeWithReserveCopy$iv$iv.finishWrite(it$iv$iv$iv);
                                        com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, null);
                                        reserveFile$iv$iv = new java.io.File($this$writeWithReserveCopy$iv$iv.getBaseFile().getParentFile(), $this$writeWithReserveCopy$iv$iv.getBaseFile().getName() + ".reservecopy");
                                    } catch (java.lang.Throwable th2) {
                                        t$iv$iv$iv = th2;
                                        $this$writeWithReserveCopy$iv$iv.failWrite(it$iv$iv$iv);
                                        throw t$iv$iv$iv;
                                    }
                                } catch (java.lang.Throwable th3) {
                                    t$iv$iv$iv = th3;
                                }
                            } catch (java.lang.Throwable th4) {
                                t$iv$iv$iv = th4;
                            }
                        } catch (java.lang.Throwable th5) {
                            t$iv$iv$iv = th5;
                        }
                    } catch (java.lang.Throwable t$iv$iv$iv) {
                        th = t$iv$iv$iv;
                        try {
                            throw th;
                        } catch (java.lang.Throwable th6) {
                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, th);
                            throw th6;
                        }
                    }
                } catch (java.lang.Throwable th7) {
                    th = th7;
                }
                try {
                    java.io.FileOutputStream fileInputStream = new java.io.FileInputStream($this$writeWithReserveCopy$iv$iv.getBaseFile());
                    try {
                        java.io.FileInputStream inputStream$iv$iv = fileInputStream;
                        fileInputStream = new java.io.FileOutputStream(reserveFile$iv$iv);
                        try {
                            java.io.FileOutputStream outputStream$iv$iv = fileInputStream;
                            android.os.FileUtils.copy(inputStream$iv$iv, outputStream$iv$iv);
                            outputStream$iv$iv.getFD().sync();
                            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                            com.android.server.permission.jarjar.kotlin.Unit unit3 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                        } finally {
                        }
                    } finally {
                    }
                } catch (java.lang.Exception e$iv$iv) {
                    android.util.Slog.e("AccessPersistence", "Failed to write " + reserveFile$iv$iv, e$iv$iv);
                }
            } catch (java.lang.Exception e) {
                e$iv = e;
                android.util.Slog.e(LOG_TAG, "Failed to serialize " + $this$serialize$iv, e$iv);
            }
        } catch (java.lang.Exception e2) {
            e$iv = e2;
            android.util.Slog.e(LOG_TAG, "Failed to serialize " + $this$serialize$iv, e$iv);
        }
    }

    private final void writeUserState(com.android.server.permission.access.AccessState state, int userId) {
        java.lang.Throwable th;
        java.io.FileOutputStream it$iv$iv$iv;
        java.io.File $this$serialize$iv = getUserFile(userId);
        try {
            try {
                android.util.AtomicFile $this$writeWithReserveCopy$iv$iv = new android.util.AtomicFile($this$serialize$iv);
                java.io.FileOutputStream fileOutputStreamStartWrite = $this$writeWithReserveCopy$iv$iv.startWrite();
                try {
                    it$iv$iv$iv = fileOutputStreamStartWrite;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
                try {
                    try {
                        java.io.FileOutputStream $this$serializeBinaryXml$iv$iv = it$iv$iv$iv;
                        com.android.modules.utils.BinaryXmlSerializer $this$serializeBinaryXml_u24lambda_u240$iv$iv = new com.android.modules.utils.BinaryXmlSerializer();
                        try {
                            $this$serializeBinaryXml_u24lambda_u240$iv$iv.setOutput($this$serializeBinaryXml$iv$iv, (java.lang.String) null);
                            try {
                                $this$serializeBinaryXml_u24lambda_u240$iv$iv.startDocument((java.lang.String) null, true);
                                com.android.server.permission.access.AccessPolicy $this$writeUserState_u24lambda_u2415_u24lambda_u2414 = this.policy;
                                try {
                                    $this$writeUserState_u24lambda_u2415_u24lambda_u2414.serializeUserState($this$serializeBinaryXml_u24lambda_u240$iv$iv, state, userId);
                                    $this$serializeBinaryXml_u24lambda_u240$iv$iv.endDocument();
                                    $this$writeWithReserveCopy$iv$iv.finishWrite(it$iv$iv$iv);
                                    com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                    com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, null);
                                    java.io.File reserveFile$iv$iv = new java.io.File($this$writeWithReserveCopy$iv$iv.getBaseFile().getParentFile(), $this$writeWithReserveCopy$iv$iv.getBaseFile().getName() + ".reservecopy");
                                    try {
                                        java.io.FileOutputStream fileInputStream = new java.io.FileInputStream($this$writeWithReserveCopy$iv$iv.getBaseFile());
                                        try {
                                            java.io.FileInputStream inputStream$iv$iv = fileInputStream;
                                            fileInputStream = new java.io.FileOutputStream(reserveFile$iv$iv);
                                            try {
                                                java.io.FileOutputStream outputStream$iv$iv = fileInputStream;
                                                android.os.FileUtils.copy(inputStream$iv$iv, outputStream$iv$iv);
                                                outputStream$iv$iv.getFD().sync();
                                                com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                                                com.android.server.permission.jarjar.kotlin.Unit unit3 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                                            } finally {
                                            }
                                        } finally {
                                        }
                                    } catch (java.lang.Exception e$iv$iv) {
                                        android.util.Slog.e("AccessPersistence", "Failed to write " + reserveFile$iv$iv, e$iv$iv);
                                    }
                                } catch (java.lang.Throwable th3) {
                                    t$iv$iv$iv = th3;
                                    $this$writeWithReserveCopy$iv$iv.failWrite(it$iv$iv$iv);
                                    throw t$iv$iv$iv;
                                }
                            } catch (java.lang.Throwable th4) {
                                t$iv$iv$iv = th4;
                            }
                        } catch (java.lang.Throwable th5) {
                            t$iv$iv$iv = th5;
                        }
                    } catch (java.lang.Throwable th6) {
                        t$iv$iv$iv = th6;
                    }
                } catch (java.lang.Throwable t$iv$iv$iv) {
                    th = t$iv$iv$iv;
                    try {
                        throw th;
                    } catch (java.lang.Throwable th7) {
                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, th);
                        throw th7;
                    }
                }
            } catch (java.lang.Exception e) {
                e$iv = e;
                android.util.Slog.e(LOG_TAG, "Failed to serialize " + $this$serialize$iv, e$iv);
            }
        } catch (java.lang.Exception e2) {
            e$iv = e2;
            android.util.Slog.e(LOG_TAG, "Failed to serialize " + $this$serialize$iv, e$iv);
        }
    }

    /* JADX WARN: Finally extract failed */
    private final void serialize(java.io.File $this$serialize, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.modules.utils.BinaryXmlSerializer, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        java.lang.Throwable th;
        java.io.FileOutputStream it$iv$iv;
        try {
            try {
                android.util.AtomicFile $this$writeWithReserveCopy$iv = new android.util.AtomicFile($this$serialize);
                java.io.FileOutputStream fileOutputStreamStartWrite = $this$writeWithReserveCopy$iv.startWrite();
                try {
                    it$iv$iv = fileOutputStreamStartWrite;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
                try {
                    try {
                        java.io.FileOutputStream $this$serializeBinaryXml$iv = it$iv$iv;
                        com.android.modules.utils.BinaryXmlSerializer $this$serializeBinaryXml_u24lambda_u240$iv = new com.android.modules.utils.BinaryXmlSerializer();
                        $this$serializeBinaryXml_u24lambda_u240$iv.setOutput($this$serializeBinaryXml$iv, (java.lang.String) null);
                        try {
                            $this$serializeBinaryXml_u24lambda_u240$iv.startDocument((java.lang.String) null, true);
                            try {
                                function1.invoke($this$serializeBinaryXml_u24lambda_u240$iv);
                                $this$serializeBinaryXml_u24lambda_u240$iv.endDocument();
                                $this$writeWithReserveCopy$iv.finishWrite(it$iv$iv);
                                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, null);
                                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                                java.io.File reserveFile$iv = new java.io.File($this$writeWithReserveCopy$iv.getBaseFile().getParentFile(), $this$writeWithReserveCopy$iv.getBaseFile().getName() + ".reservecopy");
                                try {
                                    java.io.FileInputStream fileInputStream = new java.io.FileInputStream($this$writeWithReserveCopy$iv.getBaseFile());
                                    try {
                                        java.io.FileInputStream inputStream$iv = fileInputStream;
                                        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(reserveFile$iv);
                                        try {
                                            java.io.FileOutputStream outputStream$iv = fileOutputStream;
                                            android.os.FileUtils.copy(inputStream$iv, outputStream$iv);
                                            outputStream$iv.getFD().sync();
                                            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStream, null);
                                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                                            com.android.server.permission.jarjar.kotlin.Unit unit3 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                                            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileInputStream, null);
                                            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                                        } finally {
                                        }
                                    } finally {
                                    }
                                } catch (java.lang.Exception e$iv) {
                                    android.util.Slog.e("AccessPersistence", "Failed to write " + reserveFile$iv, e$iv);
                                }
                            } catch (java.lang.Throwable th3) {
                                t$iv$iv = th3;
                                $this$writeWithReserveCopy$iv.failWrite(it$iv$iv);
                                throw t$iv$iv;
                            }
                        } catch (java.lang.Throwable th4) {
                            t$iv$iv = th4;
                        }
                    } catch (java.lang.Throwable th5) {
                        t$iv$iv = th5;
                    }
                } catch (java.lang.Throwable t$iv$iv) {
                    th = t$iv$iv;
                    try {
                        throw th;
                    } catch (java.lang.Throwable th6) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                        com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(fileOutputStreamStartWrite, th);
                        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                        throw th6;
                    }
                }
            } catch (java.lang.Exception e) {
                e = e;
                android.util.Slog.e(LOG_TAG, "Failed to serialize " + $this$serialize, e);
            }
        } catch (java.lang.Exception e2) {
            e = e2;
            android.util.Slog.e(LOG_TAG, "Failed to serialize " + $this$serialize, e);
        }
    }

    private final java.io.File getSystemFile() {
        return new java.io.File(com.android.server.permission.access.util.PermissionApex.INSTANCE.getSystemDataDirectory(), FILE_NAME);
    }

    private final java.io.File getUserFile(int userId) {
        return new java.io.File(com.android.server.permission.access.util.PermissionApex.INSTANCE.getUserDataDirectory(userId), FILE_NAME);
    }

    /* JADX INFO: compiled from: AccessPersistence.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0006*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lcom/android/server/permission/access/AccessPersistence$Companion;", "", "()V", "FILE_NAME", "", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "MAX_WRITE_DELAY_MILLIS", "", "WRITE_DELAY_TIME_MILLIS", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: compiled from: AccessPersistence.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"Lcom/android/server/permission/access/AccessPersistence$WriteHandler;", "Landroid/os/Handler;", "looper", "Landroid/os/Looper;", "(Lcom/android/server/permission/access/AccessPersistence;Landroid/os/Looper;)V", "handleMessage", "", "message", "Landroid/os/Message;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class WriteHandler extends android.os.Handler {
        public WriteHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            int userId = message.what;
            com.android.server.permission.access.AccessPersistence.this.writePendingState(userId);
        }
    }
}
