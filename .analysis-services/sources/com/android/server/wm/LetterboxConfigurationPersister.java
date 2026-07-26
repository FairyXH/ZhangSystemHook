package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class LetterboxConfigurationPersister {
    private static final java.lang.String LETTERBOX_CONFIGURATION_FILENAME = "letterbox_config";
    private static final java.lang.String TAG = "WindowManager";
    private final java.util.function.Consumer<java.lang.String> mCompletionCallback;
    private final android.util.AtomicFile mConfigurationFile;
    private final java.util.function.Supplier<java.lang.Integer> mDefaultBookModeReachabilitySupplier;
    private final java.util.function.Supplier<java.lang.Integer> mDefaultHorizontalReachabilitySupplier;
    private final java.util.function.Supplier<java.lang.Integer> mDefaultTabletopModeReachabilitySupplier;
    private final java.util.function.Supplier<java.lang.Integer> mDefaultVerticalReachabilitySupplier;
    private volatile int mLetterboxPositionForBookModeReachability;
    private volatile int mLetterboxPositionForHorizontalReachability;
    private volatile int mLetterboxPositionForTabletopModeReachability;
    private volatile int mLetterboxPositionForVerticalReachability;
    private final com.android.server.wm.PersisterQueue mPersisterQueue;

    LetterboxConfigurationPersister(java.util.function.Supplier<java.lang.Integer> defaultHorizontalReachabilitySupplier, java.util.function.Supplier<java.lang.Integer> defaultVerticalReachabilitySupplier, java.util.function.Supplier<java.lang.Integer> defaultBookModeReachabilitySupplier, java.util.function.Supplier<java.lang.Integer> defaultTabletopModeReachabilitySupplier) {
        this(defaultHorizontalReachabilitySupplier, defaultVerticalReachabilitySupplier, defaultBookModeReachabilitySupplier, defaultTabletopModeReachabilitySupplier, android.os.Environment.getDataSystemDirectory(), new com.android.server.wm.PersisterQueue(), null, LETTERBOX_CONFIGURATION_FILENAME);
    }

    LetterboxConfigurationPersister(java.util.function.Supplier<java.lang.Integer> defaultHorizontalReachabilitySupplier, java.util.function.Supplier<java.lang.Integer> defaultVerticalReachabilitySupplier, java.util.function.Supplier<java.lang.Integer> defaultBookModeReachabilitySupplier, java.util.function.Supplier<java.lang.Integer> defaultTabletopModeReachabilitySupplier, java.io.File configFolder, com.android.server.wm.PersisterQueue persisterQueue, java.util.function.Consumer<java.lang.String> completionCallback, java.lang.String letterboxConfigurationFileName) {
        this.mDefaultHorizontalReachabilitySupplier = defaultHorizontalReachabilitySupplier;
        this.mDefaultVerticalReachabilitySupplier = defaultVerticalReachabilitySupplier;
        this.mDefaultBookModeReachabilitySupplier = defaultBookModeReachabilitySupplier;
        this.mDefaultTabletopModeReachabilitySupplier = defaultTabletopModeReachabilitySupplier;
        this.mCompletionCallback = completionCallback;
        java.io.File prefFiles = new java.io.File(configFolder, letterboxConfigurationFileName);
        this.mConfigurationFile = new android.util.AtomicFile(prefFiles);
        this.mPersisterQueue = persisterQueue;
        runWithDiskReadsThreadPolicy(new java.lang.Runnable() { // from class: com.android.server.wm.LetterboxConfigurationPersister$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.readCurrentConfiguration();
            }
        });
    }

    void start() {
        this.mPersisterQueue.startPersisting();
    }

    int getLetterboxPositionForHorizontalReachability(boolean forBookMode) {
        if (forBookMode) {
            return this.mLetterboxPositionForBookModeReachability;
        }
        return this.mLetterboxPositionForHorizontalReachability;
    }

    int getLetterboxPositionForVerticalReachability(boolean forTabletopMode) {
        if (forTabletopMode) {
            return this.mLetterboxPositionForTabletopModeReachability;
        }
        return this.mLetterboxPositionForVerticalReachability;
    }

    void setLetterboxPositionForHorizontalReachability(boolean forBookMode, int letterboxPositionForHorizontalReachability) {
        if (forBookMode) {
            if (this.mLetterboxPositionForBookModeReachability != letterboxPositionForHorizontalReachability) {
                this.mLetterboxPositionForBookModeReachability = letterboxPositionForHorizontalReachability;
                updateConfiguration();
                return;
            }
            return;
        }
        if (this.mLetterboxPositionForHorizontalReachability != letterboxPositionForHorizontalReachability) {
            this.mLetterboxPositionForHorizontalReachability = letterboxPositionForHorizontalReachability;
            updateConfiguration();
        }
    }

    void setLetterboxPositionForVerticalReachability(boolean forTabletopMode, int letterboxPositionForVerticalReachability) {
        if (forTabletopMode) {
            if (this.mLetterboxPositionForTabletopModeReachability != letterboxPositionForVerticalReachability) {
                this.mLetterboxPositionForTabletopModeReachability = letterboxPositionForVerticalReachability;
                updateConfiguration();
                return;
            }
            return;
        }
        if (this.mLetterboxPositionForVerticalReachability != letterboxPositionForVerticalReachability) {
            this.mLetterboxPositionForVerticalReachability = letterboxPositionForVerticalReachability;
            updateConfiguration();
        }
    }

    void useDefaultValue() {
        this.mLetterboxPositionForHorizontalReachability = this.mDefaultHorizontalReachabilitySupplier.get().intValue();
        this.mLetterboxPositionForVerticalReachability = this.mDefaultVerticalReachabilitySupplier.get().intValue();
        this.mLetterboxPositionForBookModeReachability = this.mDefaultBookModeReachabilitySupplier.get().intValue();
        this.mLetterboxPositionForTabletopModeReachability = this.mDefaultTabletopModeReachabilitySupplier.get().intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:12:0x0038 -> B:29:0x0050). Please report as a decompilation issue!!! */
    public void readCurrentConfiguration() {
        if (!this.mConfigurationFile.exists()) {
            useDefaultValue();
            return;
        }
        java.io.FileInputStream fis = null;
        try {
            try {
                try {
                    fis = this.mConfigurationFile.openRead();
                    byte[] protoData = readInputStream(fis);
                    com.android.server.wm.nano.WindowManagerProtos.LetterboxProto letterboxData = com.android.server.wm.nano.WindowManagerProtos.LetterboxProto.parseFrom(protoData);
                    this.mLetterboxPositionForHorizontalReachability = letterboxData.letterboxPositionForHorizontalReachability;
                    this.mLetterboxPositionForVerticalReachability = letterboxData.letterboxPositionForVerticalReachability;
                    this.mLetterboxPositionForBookModeReachability = letterboxData.letterboxPositionForBookModeReachability;
                    this.mLetterboxPositionForTabletopModeReachability = letterboxData.letterboxPositionForTabletopModeReachability;
                    if (fis != null) {
                        fis.close();
                    }
                } catch (java.io.IOException ioe) {
                    android.util.Slog.e(TAG, "Error reading from LetterboxConfigurationPersister. Using default values!", ioe);
                    useDefaultValue();
                    if (fis == null) {
                    } else {
                        fis.close();
                    }
                }
            } catch (java.io.IOException e) {
                useDefaultValue();
                android.util.Slog.e(TAG, "Error reading from LetterboxConfigurationPersister ", e);
            }
        } catch (java.lang.Throwable th) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (java.io.IOException e2) {
                    useDefaultValue();
                    android.util.Slog.e(TAG, "Error reading from LetterboxConfigurationPersister ", e2);
                }
            }
            throw th;
        }
    }

    private void updateConfiguration() {
        this.mPersisterQueue.addItem(new com.android.server.wm.LetterboxConfigurationPersister.UpdateValuesCommand(this.mConfigurationFile, this.mLetterboxPositionForHorizontalReachability, this.mLetterboxPositionForVerticalReachability, this.mLetterboxPositionForBookModeReachability, this.mLetterboxPositionForTabletopModeReachability, this.mCompletionCallback), true);
    }

    private static byte[] readInputStream(java.io.InputStream in) throws java.io.IOException {
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int size = in.read(buffer);
            while (size > 0) {
                outputStream.write(buffer, 0, size);
                size = in.read(buffer);
            }
            return outputStream.toByteArray();
        } finally {
            outputStream.close();
        }
    }

    private void runWithDiskReadsThreadPolicy(java.lang.Runnable runnable) {
        android.os.StrictMode.ThreadPolicy currentPolicy = android.os.StrictMode.getThreadPolicy();
        android.os.StrictMode.setThreadPolicy(new android.os.StrictMode.ThreadPolicy.Builder().permitDiskReads().build());
        runnable.run();
        android.os.StrictMode.setThreadPolicy(currentPolicy);
    }

    private static class UpdateValuesCommand implements com.android.server.wm.PersisterQueue.WriteQueueItem<com.android.server.wm.LetterboxConfigurationPersister.UpdateValuesCommand> {
        private final int mBookModeReachability;
        private final android.util.AtomicFile mFileToUpdate;
        private final int mHorizontalReachability;
        private final java.util.function.Consumer<java.lang.String> mOnComplete;
        private final int mTabletopModeReachability;
        private final int mVerticalReachability;

        UpdateValuesCommand(android.util.AtomicFile fileToUpdate, int horizontalReachability, int verticalReachability, int bookModeReachability, int tabletopModeReachability, java.util.function.Consumer<java.lang.String> onComplete) {
            this.mFileToUpdate = fileToUpdate;
            this.mHorizontalReachability = horizontalReachability;
            this.mVerticalReachability = verticalReachability;
            this.mBookModeReachability = bookModeReachability;
            this.mTabletopModeReachability = tabletopModeReachability;
            this.mOnComplete = onComplete;
        }

        @Override // com.android.server.wm.PersisterQueue.WriteQueueItem
        public void process() {
            com.android.server.wm.nano.WindowManagerProtos.LetterboxProto letterboxData = new com.android.server.wm.nano.WindowManagerProtos.LetterboxProto();
            letterboxData.letterboxPositionForHorizontalReachability = this.mHorizontalReachability;
            letterboxData.letterboxPositionForVerticalReachability = this.mVerticalReachability;
            letterboxData.letterboxPositionForBookModeReachability = this.mBookModeReachability;
            letterboxData.letterboxPositionForTabletopModeReachability = this.mTabletopModeReachability;
            byte[] bytes = com.android.server.wm.nano.WindowManagerProtos.LetterboxProto.toByteArray(letterboxData);
            java.io.FileOutputStream fos = null;
            try {
                try {
                    fos = this.mFileToUpdate.startWrite();
                    fos.write(bytes);
                    this.mFileToUpdate.finishWrite(fos);
                    if (this.mOnComplete == null) {
                        return;
                    }
                } catch (java.io.IOException ioe) {
                    this.mFileToUpdate.failWrite(fos);
                    android.util.Slog.e(com.android.server.wm.LetterboxConfigurationPersister.TAG, "Error writing to LetterboxConfigurationPersister. Using default values!", ioe);
                    if (this.mOnComplete == null) {
                        return;
                    }
                }
                this.mOnComplete.accept("UpdateValuesCommand");
            } catch (java.lang.Throwable th) {
                if (this.mOnComplete != null) {
                    this.mOnComplete.accept("UpdateValuesCommand");
                }
                throw th;
            }
        }
    }
}
