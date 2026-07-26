package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class DropBoxManagerService extends com.android.server.SystemService {
    private static final long COMPRESS_THRESHOLD_BYTES = 16384;
    private static final int DEFAULT_AGE_SECONDS = 259200;
    private static final int DEFAULT_MAX_FILES = 1000;
    private static final int DEFAULT_MAX_FILES_LOWRAM = 300;
    public static final int DEFAULT_QUOTA_KB;
    private static final int DEFAULT_QUOTA_PERCENT = 10;
    private static final int DEFAULT_RESERVE_PERCENT = 0;
    private static final java.util.List<java.lang.String> DISABLED_BY_DEFAULT_TAGS;
    private static final long ENFORCE_READ_DROPBOX_DATA = 296060945;
    private static final boolean PROFILE_DUMP = false;
    private static final int PROTO_MAX_DATA_BYTES = 262144;
    private static final int QUOTA_RESCAN_MILLIS = 5000;
    private static final java.lang.String TAG = "DropBoxManagerService";
    private static com.android.server.IDropBoxManagerServiceExt mDmsExt;
    private static final android.os.BundleMerger sDropboxEntryAddedExtrasMerger;
    private static final java.util.concurrent.Executor sTrimExecutor;
    private com.android.server.DropBoxManagerService.FileList mAllFiles;
    private int mBlockSize;
    private volatile boolean mBooted;
    private int mCachedQuotaBlocks;
    private long mCachedQuotaUptimeMillis;
    private final android.content.ContentResolver mContentResolver;
    private final java.io.File mDropBoxDir;
    private android.util.ArrayMap<java.lang.String, com.android.server.DropBoxManagerService.FileList> mFilesByTag;
    private final com.android.server.DropBoxManagerService.DropBoxManagerBroadcastHandler mHandler;
    private long mLowPriorityRateLimitPeriod;
    private android.util.ArraySet<java.lang.String> mLowPriorityTags;
    private int mMaxFiles;
    private final android.content.BroadcastReceiver mReceiver;
    private android.os.StatFs mStatFs;
    private final com.android.internal.os.IDropBoxManagerService.Stub mStub;

    static {
        DEFAULT_QUOTA_KB = android.os.Build.IS_USERDEBUG ? 20480 : 10240;
        DISABLED_BY_DEFAULT_TAGS = java.util.List.of("data_app_wtf", "system_app_wtf", "system_server_wtf");
        mDmsExt = (com.android.server.IDropBoxManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.IDropBoxManagerServiceExt.class).create();
        android.util.IAsyncTaskSchedulers schedulers = (android.util.IAsyncTaskSchedulers) system.ext.loader.core.ExtLoader.type(android.util.IAsyncTaskSchedulers.class).create();
        sTrimExecutor = schedulers == null ? null : schedulers.ioScheduler();
        sDropboxEntryAddedExtrasMerger = new android.os.BundleMerger();
        sDropboxEntryAddedExtrasMerger.setDefaultMergeStrategy(1);
        sDropboxEntryAddedExtrasMerger.setMergeStrategy("time", 4);
        sDropboxEntryAddedExtrasMerger.setMergeStrategy("android.os.extra.DROPPED_COUNT", 25);
    }

    /* JADX INFO: renamed from: com.android.server.DropBoxManagerService$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            com.android.server.DropBoxManagerService.this.mCachedQuotaUptimeMillis = 0L;
            java.lang.Runnable trimTask = new java.lang.Runnable() { // from class: com.android.server.DropBoxManagerService$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onReceive$0();
                }
            };
            if (com.android.server.DropBoxManagerService.sTrimExecutor != null) {
                com.android.server.DropBoxManagerService.sTrimExecutor.execute(trimTask);
            } else {
                new java.lang.Thread(trimTask, "DropBoxTrim").start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            android.os.Trace.traceBegin(524288L, "DropBox-TrimToFit");
            try {
                try {
                    com.android.server.DropBoxManagerService.this.init();
                    com.android.server.DropBoxManagerService.this.trimToFit();
                } catch (java.io.IOException e) {
                    android.util.Slog.e(com.android.server.DropBoxManagerService.TAG, "Can't init", e);
                }
            } finally {
                android.os.Trace.traceEnd(524288L);
            }
        }
    }

    private class ShellCmd extends android.os.ShellCommand {
        private ShellCmd() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:8:0x0013  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public int onCommand(java.lang.String r6) {
            /*
                r5 = this;
                if (r6 != 0) goto L7
                int r0 = r5.handleDefaultCommands(r6)
                return r0
            L7:
                java.io.PrintWriter r0 = r5.getOutPrintWriter()
                r1 = 0
                int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L72
                switch(r2) {
                    case -1412652367: goto L34;
                    case -529247831: goto L2a;
                    case -444925274: goto L1f;
                    case 1936917209: goto L14;
                    default: goto L13;
                }     // Catch: java.lang.Exception -> L72
            L13:
                goto L3f
            L14:
                java.lang.String r2 = "set-rate-limit"
                boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L72
                if (r2 == 0) goto L13
                r2 = r1
                goto L40
            L1f:
                java.lang.String r2 = "remove-low-priority"
                boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L72
                if (r2 == 0) goto L13
                r2 = 2
                goto L40
            L2a:
                java.lang.String r2 = "add-low-priority"
                boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L72
                if (r2 == 0) goto L13
                r2 = 1
                goto L40
            L34:
                java.lang.String r2 = "restore-defaults"
                boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L72
                if (r2 == 0) goto L13
                r2 = 3
                goto L40
            L3f:
                r2 = -1
            L40:
                switch(r2) {
                    case 0: goto L62;
                    case 1: goto L58;
                    case 2: goto L4e;
                    case 3: goto L48;
                    default: goto L43;
                }     // Catch: java.lang.Exception -> L72
            L43:
                int r1 = r5.handleDefaultCommands(r6)     // Catch: java.lang.Exception -> L72
                goto L71
            L48:
                com.android.server.DropBoxManagerService r2 = com.android.server.DropBoxManagerService.this     // Catch: java.lang.Exception -> L72
                com.android.server.DropBoxManagerService.m196$$Nest$mrestoreDefaults(r2)     // Catch: java.lang.Exception -> L72
                goto L70
            L4e:
                java.lang.String r2 = r5.getNextArgRequired()     // Catch: java.lang.Exception -> L72
                com.android.server.DropBoxManagerService r3 = com.android.server.DropBoxManagerService.this     // Catch: java.lang.Exception -> L72
                com.android.server.DropBoxManagerService.m195$$Nest$mremoveLowPriorityTag(r3, r2)     // Catch: java.lang.Exception -> L72
                goto L70
            L58:
                java.lang.String r2 = r5.getNextArgRequired()     // Catch: java.lang.Exception -> L72
                com.android.server.DropBoxManagerService r3 = com.android.server.DropBoxManagerService.this     // Catch: java.lang.Exception -> L72
                com.android.server.DropBoxManagerService.m193$$Nest$maddLowPriorityTag(r3, r2)     // Catch: java.lang.Exception -> L72
                goto L70
            L62:
                java.lang.String r2 = r5.getNextArgRequired()     // Catch: java.lang.Exception -> L72
                long r2 = java.lang.Long.parseLong(r2)     // Catch: java.lang.Exception -> L72
                com.android.server.DropBoxManagerService r4 = com.android.server.DropBoxManagerService.this     // Catch: java.lang.Exception -> L72
                com.android.server.DropBoxManagerService.m197$$Nest$msetLowPriorityRateLimit(r4, r2)     // Catch: java.lang.Exception -> L72
            L70:
                goto L76
            L71:
                return r1
            L72:
                r2 = move-exception
                r0.println(r2)
            L76:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.DropBoxManagerService.ShellCmd.onCommand(java.lang.String):int");
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("Dropbox manager service commands:");
            pw.println("  help");
            pw.println("    Print this help text.");
            pw.println("  set-rate-limit PERIOD");
            pw.println("    Sets low priority broadcast rate limit period to PERIOD ms");
            pw.println("  add-low-priority TAG");
            pw.println("    Add TAG to dropbox low priority list");
            pw.println("  remove-low-priority TAG");
            pw.println("    Remove TAG from dropbox low priority list");
            pw.println("  restore-defaults");
            pw.println("    restore dropbox settings to defaults");
        }
    }

    private class DropBoxManagerBroadcastHandler extends android.os.Handler {
        static final int MSG_SEND_BROADCAST = 1;
        static final int MSG_SEND_DEFERRED_BROADCAST = 2;
        private final android.util.ArrayMap<java.lang.String, android.content.Intent> mDeferredMap;
        private final java.lang.Object mLock;

        DropBoxManagerBroadcastHandler(android.os.Looper looper) {
            super(looper);
            this.mLock = new java.lang.Object();
            this.mDeferredMap = new android.util.ArrayMap<>();
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            android.content.Intent deferredIntent;
            switch (msg.what) {
                case 1:
                    prepareAndSendBroadcast((android.content.Intent) msg.obj, false);
                    return;
                case 2:
                    synchronized (this.mLock) {
                        deferredIntent = this.mDeferredMap.remove((java.lang.String) msg.obj);
                        break;
                    }
                    if (deferredIntent != null) {
                        prepareAndSendBroadcast(deferredIntent, true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private void prepareAndSendBroadcast(android.content.Intent intent, boolean deferrable) {
            if (!com.android.server.DropBoxManagerService.this.mBooted) {
                intent.addFlags(1073741824);
            }
            android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
            if (com.android.server.feature.flags.Flags.enableReadDropboxPermission()) {
                options.setRequireCompatChange(com.android.server.DropBoxManagerService.ENFORCE_READ_DROPBOX_DATA, true);
                if (deferrable) {
                    java.lang.String matchingKey = intent.getStringExtra("tag") + "-READ_DROPBOX_DATA";
                    setBroadcastOptionsForDeferral(options, matchingKey);
                }
                com.android.server.DropBoxManagerService.this.getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.READ_DROPBOX_DATA", options.toBundle());
                options.setRequireCompatChange(com.android.server.DropBoxManagerService.ENFORCE_READ_DROPBOX_DATA, false);
                if (deferrable) {
                    java.lang.String matchingKey2 = intent.getStringExtra("tag") + "-READ_LOGS";
                    setBroadcastOptionsForDeferral(options, matchingKey2);
                }
                com.android.server.DropBoxManagerService.this.getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.READ_LOGS", options.toBundle());
                return;
            }
            if (deferrable) {
                java.lang.String matchingKey3 = intent.getStringExtra("tag");
                setBroadcastOptionsForDeferral(options, matchingKey3);
            }
            com.android.server.DropBoxManagerService.this.getContext().sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.READ_LOGS", options.toBundle());
        }

        private android.content.Intent createIntent(java.lang.String tag, long time) {
            android.content.Intent dropboxIntent = new android.content.Intent("android.intent.action.DROPBOX_ENTRY_ADDED");
            dropboxIntent.putExtra("tag", tag);
            dropboxIntent.putExtra("time", time);
            dropboxIntent.putExtra("android.os.extra.DROPPED_COUNT", 0);
            return dropboxIntent;
        }

        private void setBroadcastOptionsForDeferral(android.app.BroadcastOptions options, java.lang.String matchingKey) {
            options.setDeliveryGroupPolicy(2).setDeliveryGroupMatchingKey("android.intent.action.DROPBOX_ENTRY_ADDED", matchingKey).setDeliveryGroupExtrasMerger(com.android.server.DropBoxManagerService.sDropboxEntryAddedExtrasMerger).setDeferralPolicy(2);
        }

        public void sendBroadcast(java.lang.String tag, long time) {
            sendMessage(obtainMessage(1, createIntent(tag, time)));
        }

        public void maybeDeferBroadcast(java.lang.String tag, long time) {
            synchronized (this.mLock) {
                android.content.Intent intent = this.mDeferredMap.get(tag);
                if (intent == null) {
                    this.mDeferredMap.put(tag, createIntent(tag, time));
                    sendMessageDelayed(obtainMessage(2, tag), com.android.server.DropBoxManagerService.this.mLowPriorityRateLimitPeriod);
                } else {
                    intent.putExtra("time", time);
                    int dropped = intent.getIntExtra("android.os.extra.DROPPED_COUNT", 0);
                    intent.putExtra("android.os.extra.DROPPED_COUNT", dropped + 1);
                }
            }
        }
    }

    public DropBoxManagerService(android.content.Context context) {
        this(context, new java.io.File("/data/system/dropbox"), com.android.server.FgThread.get().getLooper());
    }

    public DropBoxManagerService(android.content.Context context, java.io.File path, android.os.Looper looper) {
        super(context);
        this.mAllFiles = null;
        this.mFilesByTag = null;
        this.mLowPriorityRateLimitPeriod = 0L;
        this.mLowPriorityTags = null;
        this.mStatFs = null;
        this.mBlockSize = 0;
        this.mCachedQuotaBlocks = 0;
        this.mCachedQuotaUptimeMillis = 0L;
        this.mBooted = false;
        this.mMaxFiles = -1;
        this.mReceiver = new com.android.server.DropBoxManagerService.AnonymousClass1();
        this.mStub = new com.android.internal.os.IDropBoxManagerService.Stub() { // from class: com.android.server.DropBoxManagerService.2
            public void addData(java.lang.String tag, byte[] data, int flags) throws java.lang.Throwable {
                com.android.server.DropBoxManagerService.this.addData(tag, data, flags);
            }

            public void addFile(java.lang.String tag, android.os.ParcelFileDescriptor fd, int flags) throws java.lang.Throwable {
                com.android.server.DropBoxManagerService.this.addFile(tag, fd, flags);
            }

            public boolean isTagEnabled(java.lang.String tag) {
                return com.android.server.DropBoxManagerService.this.isTagEnabled(tag);
            }

            public android.os.DropBoxManager.Entry getNextEntry(java.lang.String tag, long millis, java.lang.String callingPackage) {
                return getNextEntryWithAttribution(tag, millis, callingPackage, null);
            }

            public android.os.DropBoxManager.Entry getNextEntryWithAttribution(java.lang.String tag, long millis, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
                return com.android.server.DropBoxManagerService.this.getNextEntry(tag, millis, callingPackage, callingAttributionTag);
            }

            public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
                com.android.server.DropBoxManagerService.this.dump(fd, pw, args);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
                new com.android.server.DropBoxManagerService.ShellCmd().exec(this, in, out, err, args, callback, resultReceiver);
            }
        };
        this.mDropBoxDir = path;
        this.mContentResolver = getContext().getContentResolver();
        this.mHandler = new com.android.server.DropBoxManagerService.DropBoxManagerBroadcastHandler(looper);
        com.android.server.LocalServices.addService(com.android.server.DropBoxManagerInternal.class, new com.android.server.DropBoxManagerService.DropBoxManagerInternalImpl());
        mDmsExt.init(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("dropbox", this.mStub);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        switch (phase) {
            case 500:
                android.content.IntentFilter filter = new android.content.IntentFilter();
                filter.addAction("android.intent.action.DEVICE_STORAGE_LOW");
                getContext().registerReceiver(this.mReceiver, filter);
                this.mContentResolver.registerContentObserver(android.provider.Settings.Global.CONTENT_URI, true, new android.database.ContentObserver(new android.os.Handler()) { // from class: com.android.server.DropBoxManagerService.3
                    @Override // android.database.ContentObserver
                    public void onChange(boolean selfChange) {
                        com.android.server.DropBoxManagerService.this.mReceiver.onReceive(com.android.server.DropBoxManagerService.this.getContext(), null);
                    }
                });
                getLowPriorityResourceConfigs();
                break;
            case 1000:
                this.mBooted = true;
                break;
        }
    }

    public com.android.internal.os.IDropBoxManagerService getServiceStub() {
        return this.mStub;
    }

    public void addData(java.lang.String tag, byte[] data, int flags) throws java.lang.Throwable {
        mDmsExt.handleEapData(tag, data, flags);
        addEntry(tag, new java.io.ByteArrayInputStream(data), data.length, flags);
        mDmsExt.addSystemLogFile(tag, data, flags);
    }

    public void addFile(java.lang.String tag, android.os.ParcelFileDescriptor fd, int flags) throws java.lang.Throwable {
        try {
            android.system.StructStat stat = android.system.Os.fstat(fd.getFileDescriptor());
            if (!android.system.OsConstants.S_ISREG(stat.st_mode)) {
                throw new java.lang.IllegalArgumentException(tag + " entry must be real file");
            }
            addEntry(tag, new android.os.ParcelFileDescriptor.AutoCloseInputStream(fd), stat.st_size, flags);
        } catch (android.system.ErrnoException e) {
            throw new java.lang.IllegalArgumentException(e);
        }
    }

    public void addEntry(java.lang.String tag, java.io.InputStream in, long length, int flags) throws java.lang.Throwable {
        boolean forceCompress = false;
        if ((flags & 4) == 0 && length > COMPRESS_THRESHOLD_BYTES) {
            forceCompress = true;
            flags |= 4;
        }
        addEntry(tag, new com.android.server.DropBoxManagerService.SimpleEntrySource(in, length, forceCompress), flags);
    }

    public static class SimpleEntrySource implements com.android.server.DropBoxManagerInternal.EntrySource {
        private final boolean forceCompress;
        private final java.io.InputStream in;
        private final long length;

        public SimpleEntrySource(java.io.InputStream in, long length, boolean forceCompress) {
            this.in = in;
            this.length = length;
            this.forceCompress = forceCompress;
        }

        @Override // com.android.server.DropBoxManagerInternal.EntrySource
        public long length() {
            return this.length;
        }

        @Override // com.android.server.DropBoxManagerInternal.EntrySource
        public void writeTo(java.io.FileDescriptor fd) throws java.io.IOException {
            if (this.forceCompress) {
                java.util.zip.GZIPOutputStream gzipOutputStream = new java.util.zip.GZIPOutputStream(new java.io.FileOutputStream(fd));
                android.os.FileUtils.copy(this.in, gzipOutputStream);
                gzipOutputStream.close();
                return;
            }
            android.os.FileUtils.copy(this.in, new java.io.FileOutputStream(fd));
        }

        @Override // com.android.server.DropBoxManagerInternal.EntrySource, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws java.io.IOException {
            android.os.FileUtils.closeQuietly(this.in);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x0143  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void addEntry(java.lang.String r17, com.android.server.DropBoxManagerInternal.EntrySource r18, int r19) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 327
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.DropBoxManagerService.addEntry(java.lang.String, com.android.server.DropBoxManagerInternal$EntrySource, int):void");
    }

    private void logDropboxDropped(int reason, java.lang.String tag, long entryAge) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.DROPBOX_ENTRY_DROPPED, reason, tag, entryAge);
    }

    public boolean isTagEnabled(java.lang.String tag) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            if (DISABLED_BY_DEFAULT_TAGS.contains(tag)) {
                return com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED.equals(android.provider.Settings.Global.getString(this.mContentResolver, "dropbox:" + tag));
            }
            return !com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED.equals(android.provider.Settings.Global.getString(this.mContentResolver, "dropbox:" + tag));
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private boolean checkPermission(int callingUid, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
        if (getContext().checkCallingPermission("android.permission.PEEK_DROPBOX_DATA") == 0) {
            return true;
        }
        java.lang.String permission = "android.permission.READ_LOGS";
        if (com.android.server.feature.flags.Flags.enableReadDropboxPermission() && android.app.compat.CompatChanges.isChangeEnabled(ENFORCE_READ_DROPBOX_DATA, callingUid)) {
            permission = "android.permission.READ_DROPBOX_DATA";
        }
        getContext().enforceCallingOrSelfPermission(permission, TAG);
        switch (((android.app.AppOpsManager) getContext().getSystemService(android.app.AppOpsManager.class)).noteOp(43, callingUid, callingPackage, callingAttributionTag, (java.lang.String) null)) {
            case 0:
                break;
            case 3:
                getContext().enforceCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS", TAG);
                break;
        }
        return true;
    }

    public synchronized android.os.DropBoxManager.Entry getNextEntry(java.lang.String tag, long millis, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
        if (!checkPermission(android.os.Binder.getCallingUid(), callingPackage, callingAttributionTag)) {
            return null;
        }
        try {
            init();
            com.android.server.DropBoxManagerService.FileList list = tag == null ? this.mAllFiles : this.mFilesByTag.get(tag);
            if (list == null) {
                return null;
            }
            for (com.android.server.DropBoxManagerService.EntryFile entry : list.contents.tailSet(new com.android.server.DropBoxManagerService.EntryFile(millis + 1))) {
                if (entry.tag != null) {
                    if ((entry.flags & 1) != 0) {
                        return new android.os.DropBoxManager.Entry(entry.tag, entry.timestampMillis);
                    }
                    java.io.File file = entry.getFile(this.mDropBoxDir);
                    try {
                        return new android.os.DropBoxManager.Entry(entry.tag, entry.timestampMillis, file, entry.flags);
                    } catch (java.io.IOException e) {
                        android.util.Slog.wtf(TAG, "Can't read: " + file, e);
                    }
                }
            }
            return null;
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Can't init", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setLowPriorityRateLimit(long period) {
        this.mLowPriorityRateLimitPeriod = period;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addLowPriorityTag(java.lang.String tag) {
        this.mLowPriorityTags.add(tag);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void removeLowPriorityTag(java.lang.String tag) {
        this.mLowPriorityTags.remove(tag);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void restoreDefaults() {
        getLowPriorityResourceConfigs();
    }

    /* JADX WARN: Removed duplicated region for block: B:164:0x0337 A[Catch: all -> 0x03a0, TRY_ENTER, TRY_LEAVE, TryCatch #1 {, blocks: (B:4:0x0007, B:8:0x0015, B:9:0x0019, B:11:0x0031, B:13:0x0034, B:15:0x003e, B:18:0x004a, B:20:0x0054, B:23:0x005f, B:26:0x006b, B:28:0x0075, B:31:0x0080, B:33:0x008a, B:34:0x009c, B:35:0x00a2, B:43:0x00d2, B:47:0x00d9, B:49:0x0128, B:50:0x0131, B:52:0x0137, B:53:0x0147, B:54:0x014c, B:55:0x015a, B:57:0x0160, B:60:0x016d, B:62:0x0171, B:63:0x0176, B:67:0x0190, B:69:0x019c, B:70:0x01a9, B:72:0x01b1, B:73:0x01be, B:75:0x01c9, B:76:0x01ce, B:80:0x01da, B:83:0x01f8, B:88:0x0212, B:126:0x029b, B:128:0x02a0, B:172:0x0346, B:164:0x0337, B:166:0x033c, B:169:0x0341, B:153:0x0323, B:155:0x0328, B:86:0x0200, B:87:0x0205, B:66:0x018e, B:177:0x035d, B:179:0x0364, B:184:0x0373, B:182:0x0369, B:183:0x036e, B:189:0x0381), top: B:195:0x0007, inners: #18 }] */
    /* JADX WARN: Removed duplicated region for block: B:204:0x033c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:239:? A[Catch: all -> 0x03a0, SYNTHETIC, TRY_ENTER, TryCatch #1 {, blocks: (B:4:0x0007, B:8:0x0015, B:9:0x0019, B:11:0x0031, B:13:0x0034, B:15:0x003e, B:18:0x004a, B:20:0x0054, B:23:0x005f, B:26:0x006b, B:28:0x0075, B:31:0x0080, B:33:0x008a, B:34:0x009c, B:35:0x00a2, B:43:0x00d2, B:47:0x00d9, B:49:0x0128, B:50:0x0131, B:52:0x0137, B:53:0x0147, B:54:0x014c, B:55:0x015a, B:57:0x0160, B:60:0x016d, B:62:0x0171, B:63:0x0176, B:67:0x0190, B:69:0x019c, B:70:0x01a9, B:72:0x01b1, B:73:0x01be, B:75:0x01c9, B:76:0x01ce, B:80:0x01da, B:83:0x01f8, B:88:0x0212, B:126:0x029b, B:128:0x02a0, B:172:0x0346, B:164:0x0337, B:166:0x033c, B:169:0x0341, B:153:0x0323, B:155:0x0328, B:86:0x0200, B:87:0x0205, B:66:0x018e, B:177:0x035d, B:179:0x0364, B:184:0x0373, B:182:0x0369, B:183:0x036e, B:189:0x0381), top: B:195:0x0007, inners: #18 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void dump(java.io.FileDescriptor r29, java.io.PrintWriter r30, java.lang.String[] r31) {
        /*
            Method dump skipped, instruction units count: 931
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.DropBoxManagerService.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    private boolean matchEntry(com.android.server.DropBoxManagerService.EntryFile entry, java.util.ArrayList<java.lang.String> searchArgs) {
        java.lang.String date = android.text.format.TimeMigrationUtils.formatMillisWithFixedFormat(entry.timestampMillis);
        boolean match = true;
        int numArgs = searchArgs.size();
        for (int i = 0; i < numArgs && match; i++) {
            java.lang.String arg = searchArgs.get(i);
            match = date.contains(arg) || arg.equals(entry.tag);
        }
        return match;
    }

    private void dumpProtoLocked(java.io.FileDescriptor fd, java.util.ArrayList<java.lang.String> searchArgs) {
        com.android.server.DropBoxManagerService dropBoxManagerService = this;
        android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(fd);
        for (com.android.server.DropBoxManagerService.EntryFile entry : dropBoxManagerService.mAllFiles.contents) {
            if (dropBoxManagerService.matchEntry(entry, searchArgs)) {
                java.io.File file = entry.getFile(dropBoxManagerService.mDropBoxDir);
                if (file == null) {
                    dropBoxManagerService = this;
                } else if ((entry.flags & 1) != 0) {
                    continue;
                } else {
                    long bToken = proto.start(2246267895809L);
                    proto.write(1112396529665L, entry.timestampMillis);
                    try {
                        android.os.DropBoxManager.Entry dbe = new android.os.DropBoxManager.Entry(entry.tag, entry.timestampMillis, file, entry.flags);
                        try {
                            java.io.InputStream is = dbe.getInputStream();
                            if (is != null) {
                                try {
                                    byte[] buf = new byte[262144];
                                    int readBytes = 0;
                                    int n = 0;
                                    while (n >= 0) {
                                        int i = readBytes + n;
                                        readBytes = i;
                                        if (i >= 262144) {
                                            break;
                                        } else {
                                            n = is.read(buf, readBytes, 262144 - readBytes);
                                        }
                                    }
                                    proto.write(1151051235330L, java.util.Arrays.copyOf(buf, readBytes));
                                } catch (java.lang.Throwable th) {
                                    if (is != null) {
                                        try {
                                            is.close();
                                        } catch (java.lang.Throwable th2) {
                                            th.addSuppressed(th2);
                                        }
                                    }
                                    throw th;
                                }
                            }
                            if (is != null) {
                                is.close();
                            }
                            dbe.close();
                        } finally {
                        }
                    } catch (java.io.IOException e) {
                        android.util.Slog.e(TAG, "Can't read: " + file, e);
                    }
                    proto.end(bToken);
                    dropBoxManagerService = this;
                }
            }
        }
        proto.flush();
    }

    private static final class FileList implements java.lang.Comparable<com.android.server.DropBoxManagerService.FileList> {
        public int blocks;
        public final java.util.TreeSet<com.android.server.DropBoxManagerService.EntryFile> contents;

        private FileList() {
            this.blocks = 0;
            this.contents = new java.util.TreeSet<>();
        }

        @Override // java.lang.Comparable
        public final int compareTo(com.android.server.DropBoxManagerService.FileList o) {
            if (this.blocks != o.blocks) {
                return o.blocks - this.blocks;
            }
            if (this == o) {
                return 0;
            }
            if (hashCode() < o.hashCode()) {
                return -1;
            }
            return hashCode() > o.hashCode() ? 1 : 0;
        }
    }

    static final class EntryFile implements java.lang.Comparable<com.android.server.DropBoxManagerService.EntryFile> {
        public final int blocks;
        public final int flags;
        public final java.lang.String tag;
        public final long timestampMillis;

        @Override // java.lang.Comparable
        public final int compareTo(com.android.server.DropBoxManagerService.EntryFile o) {
            int comp = java.lang.Long.compare(this.timestampMillis, o.timestampMillis);
            if (comp != 0) {
                return comp;
            }
            int comp2 = com.android.internal.util.ObjectUtils.compare(this.tag, o.tag);
            if (comp2 != 0) {
                return comp2;
            }
            int comp3 = java.lang.Integer.compare(this.flags, o.flags);
            return comp3 != 0 ? comp3 : java.lang.Integer.compare(hashCode(), o.hashCode());
        }

        public EntryFile(java.io.File temp, java.io.File dir, java.lang.String tag, long timestampMillis, int flags, int blockSize) throws java.io.IOException {
            if ((flags & 1) != 0) {
                throw new java.lang.IllegalArgumentException();
            }
            this.tag = android.text.TextUtils.safeIntern(tag);
            this.timestampMillis = timestampMillis;
            this.flags = flags;
            java.io.File file = getFile(dir);
            if (!temp.renameTo(file)) {
                throw new java.io.IOException("Can't rename " + temp + " to " + file);
            }
            this.blocks = (int) (((file.length() + ((long) blockSize)) - 1) / ((long) blockSize));
        }

        public EntryFile(java.io.File dir, java.lang.String tag, long timestampMillis) throws java.io.IOException {
            this.tag = android.text.TextUtils.safeIntern(tag);
            this.timestampMillis = timestampMillis;
            this.flags = 1;
            this.blocks = 0;
            new java.io.FileOutputStream(getFile(dir)).close();
        }

        public EntryFile(java.io.File file, int blockSize) {
            boolean parseFailure = false;
            java.lang.String name = file.getName();
            int flags = 0;
            java.lang.String tag = null;
            long millis = 0;
            int at = name.lastIndexOf(64);
            if (at < 0) {
                parseFailure = true;
            } else {
                tag = android.net.Uri.decode(name.substring(0, at));
                if (name.endsWith(com.android.server.pm.PackageManagerService.COMPRESSED_EXTENSION)) {
                    flags = 0 | 4;
                    name = name.substring(0, name.length() - 3);
                }
                if (name.endsWith(".lost")) {
                    flags |= 1;
                    name = name.substring(at + 1, name.length() - 5);
                } else if (name.endsWith(".txt")) {
                    flags |= 2;
                    name = name.substring(at + 1, name.length() - 4);
                } else if (name.endsWith(".dat")) {
                    name = name.substring(at + 1, name.length() - 4);
                } else {
                    parseFailure = true;
                }
                if (!parseFailure) {
                    try {
                        millis = java.lang.Long.parseLong(name);
                    } catch (java.lang.NumberFormatException e) {
                        parseFailure = true;
                    }
                }
            }
            if (parseFailure) {
                android.util.Slog.wtf(com.android.server.DropBoxManagerService.TAG, "Invalid filename: " + file);
                file.delete();
                this.tag = null;
                this.flags = 1;
                this.timestampMillis = 0L;
                this.blocks = 0;
                return;
            }
            this.blocks = (int) (((file.length() + ((long) blockSize)) - 1) / ((long) blockSize));
            this.tag = android.text.TextUtils.safeIntern(tag);
            this.flags = flags;
            this.timestampMillis = millis;
        }

        public EntryFile(long millis) {
            this.tag = null;
            this.timestampMillis = millis;
            this.flags = 1;
            this.blocks = 0;
        }

        public boolean hasFile() {
            return this.tag != null;
        }

        private java.lang.String getExtension() {
            if ((this.flags & 1) != 0) {
                return ".lost";
            }
            return ((this.flags & 2) != 0 ? ".txt" : ".dat") + ((this.flags & 4) != 0 ? com.android.server.pm.PackageManagerService.COMPRESSED_EXTENSION : "");
        }

        public java.lang.String getFilename() {
            if (hasFile()) {
                return android.net.Uri.encode(this.tag) + "@" + this.timestampMillis + getExtension();
            }
            return null;
        }

        public java.io.File getFile(java.io.File dir) {
            if (hasFile()) {
                return new java.io.File(dir, getFilename());
            }
            return null;
        }

        public void deleteFile(java.io.File dir) {
            if (hasFile()) {
                getFile(dir).delete();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void init() throws java.io.IOException {
        if (this.mStatFs == null) {
            if (!this.mDropBoxDir.isDirectory() && !this.mDropBoxDir.mkdirs()) {
                throw new java.io.IOException("Can't mkdir: " + this.mDropBoxDir);
            }
            try {
                this.mStatFs = new android.os.StatFs(this.mDropBoxDir.getPath());
                this.mBlockSize = this.mStatFs.getBlockSize();
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.io.IOException("Can't statfs: " + this.mDropBoxDir);
            }
        }
        if (this.mAllFiles == null) {
            java.io.File[] files = this.mDropBoxDir.listFiles();
            if (files == null) {
                throw new java.io.IOException("Can't list files: " + this.mDropBoxDir);
            }
            this.mAllFiles = new com.android.server.DropBoxManagerService.FileList();
            this.mFilesByTag = new android.util.ArrayMap<>();
            for (java.io.File file : files) {
                if (file.getName().endsWith(".tmp")) {
                    android.util.Slog.i(TAG, "Cleaning temp file: " + file);
                    file.delete();
                } else {
                    com.android.server.DropBoxManagerService.EntryFile entry = new com.android.server.DropBoxManagerService.EntryFile(file, this.mBlockSize);
                    if (entry.hasFile()) {
                        enrollEntry(entry);
                    }
                }
            }
        }
    }

    private synchronized void enrollEntry(com.android.server.DropBoxManagerService.EntryFile entry) {
        this.mAllFiles.contents.add(entry);
        this.mAllFiles.blocks += entry.blocks;
        if (entry.hasFile() && entry.blocks > 0) {
            com.android.server.DropBoxManagerService.FileList tagFiles = this.mFilesByTag.get(entry.tag);
            if (tagFiles == null) {
                tagFiles = new com.android.server.DropBoxManagerService.FileList();
                this.mFilesByTag.put(android.text.TextUtils.safeIntern(entry.tag), tagFiles);
            }
            tagFiles.contents.add(entry);
            tagFiles.blocks += entry.blocks;
        }
    }

    private synchronized long createEntry(java.io.File temp, java.lang.String tag, int flags) throws java.io.IOException {
        long t;
        java.util.SortedSet<com.android.server.DropBoxManagerService.EntryFile> tail;
        long j;
        t = java.lang.System.currentTimeMillis();
        java.util.SortedSet<com.android.server.DropBoxManagerService.EntryFile> tail2 = this.mAllFiles.contents.tailSet(new com.android.server.DropBoxManagerService.EntryFile(10000 + t));
        com.android.server.DropBoxManagerService.EntryFile[] future = null;
        if (!tail2.isEmpty()) {
            future = (com.android.server.DropBoxManagerService.EntryFile[]) tail2.toArray(new com.android.server.DropBoxManagerService.EntryFile[tail2.size()]);
            tail2.clear();
        }
        long j2 = 1;
        if (!this.mAllFiles.contents.isEmpty()) {
            t = java.lang.Math.max(t, this.mAllFiles.contents.last().timestampMillis + 1);
        }
        if (future != null) {
            int length = future.length;
            int i = 0;
            long t2 = t;
            while (i < length) {
                com.android.server.DropBoxManagerService.EntryFile late = future[i];
                this.mAllFiles.blocks -= late.blocks;
                com.android.server.DropBoxManagerService.FileList tagFiles = this.mFilesByTag.get(late.tag);
                if (tagFiles != null && tagFiles.contents.remove(late)) {
                    tagFiles.blocks -= late.blocks;
                }
                if ((late.flags & 1) == 0) {
                    tail = tail2;
                    enrollEntry(new com.android.server.DropBoxManagerService.EntryFile(late.getFile(this.mDropBoxDir), this.mDropBoxDir, late.tag, t2, late.flags, this.mBlockSize));
                    t2 += j2;
                    j = 1;
                } else {
                    tail = tail2;
                    j = 1;
                    enrollEntry(new com.android.server.DropBoxManagerService.EntryFile(this.mDropBoxDir, late.tag, t2));
                    t2++;
                }
                i++;
                j2 = j;
                tail2 = tail;
            }
            t = t2;
        }
        if (temp == null) {
            enrollEntry(new com.android.server.DropBoxManagerService.EntryFile(this.mDropBoxDir, tag, t));
        } else {
            enrollEntry(new com.android.server.DropBoxManagerService.EntryFile(temp, this.mDropBoxDir, tag, t, flags, this.mBlockSize));
        }
        return t;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long trimToFit() throws java.io.IOException {
        long curTimeMillis;
        int ageSeconds = android.provider.Settings.Global.getInt(this.mContentResolver, "dropbox_age_seconds", DEFAULT_AGE_SECONDS);
        this.mMaxFiles = android.provider.Settings.Global.getInt(this.mContentResolver, "dropbox_max_files", android.app.ActivityManager.isLowRamDeviceStatic() ? 300 : 1000);
        long curTimeMillis2 = java.lang.System.currentTimeMillis();
        long cutoffMillis = curTimeMillis2 - ((long) (ageSeconds * 1000));
        while (!this.mAllFiles.contents.isEmpty()) {
            com.android.server.DropBoxManagerService.EntryFile entry = this.mAllFiles.contents.first();
            if (entry.timestampMillis > cutoffMillis && this.mAllFiles.contents.size() < this.mMaxFiles) {
                break;
            }
            logDropboxDropped(4, entry.tag, curTimeMillis2 - entry.timestampMillis);
            com.android.server.DropBoxManagerService.FileList tag = this.mFilesByTag.get(entry.tag);
            if (tag != null && tag.contents.remove(entry)) {
                tag.blocks -= entry.blocks;
            }
            if (this.mAllFiles.contents.remove(entry)) {
                this.mAllFiles.blocks -= entry.blocks;
            }
            entry.deleteFile(this.mDropBoxDir);
        }
        long uptimeMillis = android.os.SystemClock.uptimeMillis();
        if (uptimeMillis > this.mCachedQuotaUptimeMillis + 5000) {
            int quotaPercent = android.provider.Settings.Global.getInt(this.mContentResolver, "dropbox_quota_percent", 10);
            int reservePercent = android.provider.Settings.Global.getInt(this.mContentResolver, "dropbox_reserve_percent", 0);
            int quotaKb = android.provider.Settings.Global.getInt(this.mContentResolver, "dropbox_quota_kb", DEFAULT_QUOTA_KB);
            java.lang.String dirPath = this.mDropBoxDir.getPath();
            try {
                this.mStatFs.restat(dirPath);
                long available = this.mStatFs.getAvailableBlocksLong();
                long cutoffMillis2 = reservePercent;
                long nonreserved = available - ((this.mStatFs.getBlockCountLong() * cutoffMillis2) / 100);
                long maxAvailableLong = (((long) quotaPercent) * nonreserved) / 100;
                int maxAvailable = java.lang.Math.toIntExact(java.lang.Math.max(0L, java.lang.Math.min(maxAvailableLong, 2147483647L)));
                int maximum = (quotaKb * 1024) / this.mBlockSize;
                mDmsExt.dumpLowStorageLog(available, nonreserved, quotaPercent, this.mBlockSize, maximum);
                this.mCachedQuotaBlocks = java.lang.Math.min(maximum, maxAvailable);
                this.mCachedQuotaUptimeMillis = uptimeMillis;
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.io.IOException("Can't restat: " + this.mDropBoxDir);
            }
        }
        if (this.mAllFiles.blocks > this.mCachedQuotaBlocks) {
            int unsqueezed = this.mAllFiles.blocks;
            java.util.TreeSet<com.android.server.DropBoxManagerService.FileList> tags = new java.util.TreeSet<>(this.mFilesByTag.values());
            int squeezed = 0;
            int squeezed2 = unsqueezed;
            for (com.android.server.DropBoxManagerService.FileList tag2 : tags) {
                if (squeezed > 0 && tag2.blocks <= (this.mCachedQuotaBlocks - squeezed2) / squeezed) {
                    break;
                }
                squeezed2 -= tag2.blocks;
                squeezed++;
            }
            int tagQuota = (this.mCachedQuotaBlocks - squeezed2) / squeezed;
            for (com.android.server.DropBoxManagerService.FileList tag3 : tags) {
                if (this.mAllFiles.blocks < this.mCachedQuotaBlocks) {
                    break;
                }
                while (tag3.blocks > tagQuota && !tag3.contents.isEmpty()) {
                    com.android.server.DropBoxManagerService.EntryFile entry2 = tag3.contents.first();
                    int ageSeconds2 = ageSeconds;
                    logDropboxDropped(3, entry2.tag, curTimeMillis2 - entry2.timestampMillis);
                    if (tag3.contents.remove(entry2)) {
                        tag3.blocks -= entry2.blocks;
                    }
                    if (this.mAllFiles.contents.remove(entry2)) {
                        this.mAllFiles.blocks -= entry2.blocks;
                    }
                    try {
                        entry2.deleteFile(this.mDropBoxDir);
                        curTimeMillis = curTimeMillis2;
                    } catch (java.io.IOException e2) {
                        e = e2;
                        curTimeMillis = curTimeMillis2;
                    }
                    try {
                        enrollEntry(new com.android.server.DropBoxManagerService.EntryFile(this.mDropBoxDir, entry2.tag, entry2.timestampMillis));
                    } catch (java.io.IOException e3) {
                        e = e3;
                        android.util.Slog.e(TAG, "Can't write tombstone file", e);
                    }
                    ageSeconds = ageSeconds2;
                    curTimeMillis2 = curTimeMillis;
                }
                ageSeconds = ageSeconds;
                curTimeMillis2 = curTimeMillis2;
            }
        }
        return this.mCachedQuotaBlocks * this.mBlockSize;
    }

    private void getLowPriorityResourceConfigs() {
        this.mLowPriorityRateLimitPeriod = android.content.res.Resources.getSystem().getInteger(android.R.integer.config_doubleTapTimeoutMillis);
        java.lang.String[] lowPrioritytags = android.content.res.Resources.getSystem().getStringArray(android.R.array.config_doubleClickVibePattern);
        int size = lowPrioritytags.length;
        if (size == 0) {
            this.mLowPriorityTags = null;
            return;
        }
        this.mLowPriorityTags = new android.util.ArraySet<>(size);
        for (java.lang.String str : lowPrioritytags) {
            this.mLowPriorityTags.add(str);
        }
    }

    private final class DropBoxManagerInternalImpl extends com.android.server.DropBoxManagerInternal {
        private DropBoxManagerInternalImpl() {
        }

        @Override // com.android.server.DropBoxManagerInternal
        public void addEntry(java.lang.String tag, com.android.server.DropBoxManagerInternal.EntrySource entry, int flags) throws java.lang.Throwable {
            com.android.server.DropBoxManagerService.this.addEntry(tag, entry, flags);
        }
    }
}
