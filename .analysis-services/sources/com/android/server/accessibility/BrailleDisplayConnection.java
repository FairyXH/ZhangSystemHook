package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
class BrailleDisplayConnection extends android.accessibilityservice.IBrailleDisplayConnection.Stub {
    static final int BUS_BLUETOOTH = 5;
    static final int BUS_UNKNOWN = -1;
    static final int BUS_USB = 3;
    private static final java.lang.String LOG_TAG = "BrailleDisplayConnection";
    private static final java.util.Set<java.io.File> sConnectedNodes = new android.util.ArraySet();
    private android.accessibilityservice.IBrailleDisplayController mController;
    private java.io.File mHidrawNode;
    private java.lang.Thread mInputThread;
    private final java.lang.Object mLock;
    private java.io.OutputStream mOutputStream;
    private android.os.HandlerThread mOutputThread;
    private com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner mScanner = getDefaultNativeScanner(new com.android.server.accessibility.BrailleDisplayConnection.DefaultNativeInterface());
    private final com.android.server.accessibility.AccessibilityServiceConnection mServiceConnection;

    interface BrailleDisplayScanner {
        int getDeviceBusType(java.nio.file.Path path);

        byte[] getDeviceReportDescriptor(java.nio.file.Path path);

        java.util.Collection<java.nio.file.Path> getHidrawNodePaths(java.nio.file.Path path);

        java.lang.String getName(java.nio.file.Path path);

        java.lang.String getUniqueId(java.nio.file.Path path);
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface BusType {
    }

    interface NativeInterface {
        int getHidrawBusType(int i);

        byte[] getHidrawDesc(int i, int i2);

        int getHidrawDescSize(int i);

        java.lang.String getHidrawName(int i);

        java.lang.String getHidrawUniq(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nativeGetHidrawBusType(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native byte[] nativeGetHidrawDesc(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nativeGetHidrawDescSize(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native java.lang.String nativeGetHidrawName(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native java.lang.String nativeGetHidrawUniq(int i);

    BrailleDisplayConnection(java.lang.Object lock, com.android.server.accessibility.AccessibilityServiceConnection serviceConnection) {
        this.mLock = java.util.Objects.requireNonNull(lock);
        this.mServiceConnection = (com.android.server.accessibility.AccessibilityServiceConnection) java.util.Objects.requireNonNull(serviceConnection);
    }

    static com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner createScannerForShell() {
        return getDefaultNativeScanner(new com.android.server.accessibility.BrailleDisplayConnection.DefaultNativeInterface());
    }

    void connectLocked(java.lang.String expectedUniqueId, java.lang.String expectedName, int expectedBusType, android.accessibilityservice.IBrailleDisplayController controller) {
        boolean matchesIdentifier;
        java.util.Objects.requireNonNull(expectedUniqueId);
        this.mController = (android.accessibilityservice.IBrailleDisplayController) java.util.Objects.requireNonNull(controller);
        java.nio.file.Path devicePath = java.nio.file.Path.of("/dev", new java.lang.String[0]);
        java.util.List<android.util.Pair<java.io.File, byte[]>> result = new java.util.ArrayList<>();
        java.util.Collection<java.nio.file.Path> hidrawNodePaths = this.mScanner.getHidrawNodePaths(devicePath);
        boolean z = true;
        if (hidrawNodePaths == null) {
            android.util.Slog.w(LOG_TAG, "Unable to access the HIDRAW node directory");
            sendConnectionErrorLocked(1);
            return;
        }
        boolean unableToGetDescriptor = false;
        for (java.nio.file.Path path : hidrawNodePaths) {
            byte[] descriptor = this.mScanner.getDeviceReportDescriptor(path);
            if (descriptor == null) {
                unableToGetDescriptor = true;
            } else {
                java.lang.String uniqueId = this.mScanner.getUniqueId(path);
                if (uniqueId == null) {
                    java.lang.String name = this.mScanner.getName(path);
                    matchesIdentifier = (android.text.TextUtils.isEmpty(expectedName) || !expectedName.equals(name)) ? false : z;
                } else {
                    matchesIdentifier = expectedUniqueId.equalsIgnoreCase(uniqueId);
                }
                if (isBrailleDisplay(descriptor) && this.mScanner.getDeviceBusType(path) == expectedBusType && matchesIdentifier) {
                    result.add(android.util.Pair.create(path.toFile(), descriptor));
                }
                z = true;
            }
        }
        if (result.size() == 1) {
            this.mHidrawNode = (java.io.File) result.get(0).first;
            byte[] reportDescriptor = (byte[]) result.get(0).second;
            if (!sConnectedNodes.contains(this.mHidrawNode)) {
                sConnectedNodes.add(this.mHidrawNode);
                startReadingLocked();
                try {
                    this.mServiceConnection.onBrailleDisplayConnectedLocked(this);
                    this.mController.onConnected(this, reportDescriptor);
                    return;
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(LOG_TAG, "Error calling onConnected", e);
                    disconnect();
                    return;
                }
            }
            android.util.Slog.w(LOG_TAG, "Unable to find an unused Braille display matching the provided device");
            sendConnectionErrorLocked(2);
            return;
        }
        int errorCode = 2;
        if (unableToGetDescriptor) {
            android.util.Slog.w(LOG_TAG, "Unable to access some HIDRAW node's descriptor");
            errorCode = 2 | 1;
        } else {
            android.util.Slog.w(LOG_TAG, "Unable to find a unique Braille display matching the provided device");
        }
        sendConnectionErrorLocked(errorCode);
    }

    private void sendConnectionErrorLocked(int errorCode) {
        try {
            this.mController.onConnectionFailed(errorCode);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(LOG_TAG, "Error calling onConnectionFailed", e);
        }
    }

    static boolean isBrailleDisplay(byte[] descriptor) {
        boolean foundMatch = false;
        int i = 0;
        while (i < descriptor.length) {
            byte itemInfo = descriptor[i];
            if (!isHidItemShort(itemInfo)) {
                android.util.Slog.w(LOG_TAG, "Item " + ((int) itemInfo) + " declares unsupported long type");
                return false;
            }
            int dataSize = getHidItemDataSize(itemInfo);
            if (i + dataSize >= descriptor.length) {
                android.util.Slog.w(LOG_TAG, "Item " + ((int) itemInfo) + " specifies size past the remaining bytes");
                return false;
            }
            if (dataSize == 1) {
                byte itemData = descriptor[i + 1];
                if (isHidItemBrailleDisplayUsagePage(itemInfo, itemData)) {
                    foundMatch = true;
                }
            }
            i = i + dataSize + 1;
        }
        return foundMatch;
    }

    private static boolean isHidItemShort(byte itemInfo) {
        return (itemInfo & 240) != 240;
    }

    private static int getHidItemDataSize(byte itemInfo) {
        switch (itemInfo & 3) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                return 4;
        }
    }

    private static boolean isHidItemBrailleDisplayUsagePage(byte itemInfo, byte itemData) {
        byte itemType = (byte) (itemInfo & 252);
        return itemType == 4 && itemData == 65;
    }

    private void assertServiceIsConnectedLocked() {
        if (!this.mServiceConnection.isConnectedLocked()) {
            throw new java.lang.IllegalStateException("Accessibility service is not connected");
        }
    }

    public void disconnect() {
        synchronized (this.mLock) {
            closeInputLocked();
            closeOutputLocked();
            this.mServiceConnection.onBrailleDisplayDisconnectedLocked();
            try {
                this.mController.onDisconnected();
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(LOG_TAG, "Error calling onDisconnected");
            }
            sConnectedNodes.remove(this.mHidrawNode);
        }
    }

    public void write(final byte[] buffer) {
        java.util.Objects.requireNonNull(buffer);
        if (buffer.length > android.os.IBinder.getSuggestedMaxIpcSizeBytes()) {
            android.util.Slog.e(LOG_TAG, "Requested write of size " + buffer.length + " which is larger than maximum " + android.os.IBinder.getSuggestedMaxIpcSizeBytes());
            disconnect();
            return;
        }
        synchronized (this.mLock) {
            assertServiceIsConnectedLocked();
            if (this.mOutputThread == null) {
                try {
                    this.mOutputStream = new java.io.FileOutputStream(this.mHidrawNode);
                    this.mOutputThread = new android.os.HandlerThread("BrailleDisplayConnection output thread", 10);
                    this.mOutputThread.setDaemon(true);
                    this.mOutputThread.start();
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(LOG_TAG, "Unable to create write stream", e);
                    disconnect();
                    return;
                }
            }
            this.mOutputThread.getThreadHandler().post(new java.lang.Runnable() { // from class: com.android.server.accessibility.BrailleDisplayConnection$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$write$0(buffer);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$write$0(byte[] buffer) {
        try {
            this.mOutputStream.write(buffer);
        } catch (java.io.IOException e) {
            android.util.Slog.d(LOG_TAG, "Error writing to connected Braille display", e);
            disconnect();
        }
    }

    private void startReadingLocked() {
        this.mInputThread = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.accessibility.BrailleDisplayConnection$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startReadingLocked$1();
            }
        }, "BrailleDisplayConnection input thread");
        this.mInputThread.setDaemon(true);
        this.mInputThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startReadingLocked$1() {
        android.os.Process.setThreadPriority(10);
        try {
            java.io.InputStream inputStream = new java.io.FileInputStream(this.mHidrawNode);
            try {
                byte[] buffer = new byte[android.os.IBinder.getSuggestedMaxIpcSizeBytes()];
                while (true) {
                    if (java.lang.Thread.interrupted()) {
                        break;
                    }
                    if (!this.mHidrawNode.exists()) {
                        disconnect();
                        break;
                    }
                    int readSize = inputStream.read(buffer);
                    if (readSize > 0) {
                        try {
                            this.mController.onInput(java.util.Arrays.copyOfRange(buffer, 0, readSize));
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(LOG_TAG, "Error calling onInput", e);
                            disconnect();
                        }
                    }
                }
                inputStream.close();
            } finally {
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.d(LOG_TAG, "Error reading from connected Braille display", e2);
            disconnect();
        }
    }

    private void closeInputLocked() {
        if (this.mInputThread != null) {
            this.mInputThread.interrupt();
        }
        this.mInputThread = null;
    }

    private void closeOutputLocked() {
        if (this.mOutputThread != null) {
            this.mOutputThread.quit();
        }
        this.mOutputThread = null;
        if (this.mOutputStream != null) {
            try {
                this.mOutputStream.close();
            } catch (java.io.IOException e) {
                android.util.Slog.e(LOG_TAG, "Unable to close output stream", e);
            }
        }
        this.mOutputStream = null;
    }

    /* JADX INFO: renamed from: com.android.server.accessibility.BrailleDisplayConnection$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner {
        private static final java.lang.String HIDRAW_DEVICE_GLOB = "hidraw*";
        final /* synthetic */ com.android.server.accessibility.BrailleDisplayConnection.NativeInterface val$nativeInterface;

        AnonymousClass1(com.android.server.accessibility.BrailleDisplayConnection.NativeInterface nativeInterface) {
            this.val$nativeInterface = nativeInterface;
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
        public java.util.Collection<java.nio.file.Path> getHidrawNodePaths(java.nio.file.Path directory) {
            java.util.List<java.nio.file.Path> result = new java.util.ArrayList<>();
            try {
                java.nio.file.DirectoryStream<java.nio.file.Path> hidrawNodePaths = java.nio.file.Files.newDirectoryStream(directory, HIDRAW_DEVICE_GLOB);
                try {
                    for (java.nio.file.Path path : hidrawNodePaths) {
                        result.add(path);
                    }
                    if (hidrawNodePaths != null) {
                        hidrawNodePaths.close();
                    }
                    return result;
                } finally {
                }
            } catch (java.io.IOException e) {
                return null;
            }
        }

        private <T> T readFromFileDescriptor(java.nio.file.Path path, java.util.function.Function<java.lang.Integer, T> readFn) {
            try {
                java.io.FileInputStream stream = new java.io.FileInputStream(path.toFile());
                try {
                    T tApply = readFn.apply(java.lang.Integer.valueOf(stream.getFD().getInt$()));
                    stream.close();
                    return tApply;
                } finally {
                }
            } catch (java.io.IOException e) {
                return null;
            }
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
        public byte[] getDeviceReportDescriptor(java.nio.file.Path path) {
            java.util.Objects.requireNonNull(path);
            final com.android.server.accessibility.BrailleDisplayConnection.NativeInterface nativeInterface = this.val$nativeInterface;
            return (byte[]) readFromFileDescriptor(path, new java.util.function.Function() { // from class: com.android.server.accessibility.BrailleDisplayConnection$1$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.accessibility.BrailleDisplayConnection.AnonymousClass1.lambda$getDeviceReportDescriptor$0(nativeInterface, (java.lang.Integer) obj);
                }
            });
        }

        static /* synthetic */ byte[] lambda$getDeviceReportDescriptor$0(com.android.server.accessibility.BrailleDisplayConnection.NativeInterface nativeInterface, java.lang.Integer fd) {
            int descSize = nativeInterface.getHidrawDescSize(fd.intValue());
            if (descSize > 0) {
                return nativeInterface.getHidrawDesc(fd.intValue(), descSize);
            }
            return null;
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
        public java.lang.String getUniqueId(java.nio.file.Path path) {
            java.util.Objects.requireNonNull(path);
            final com.android.server.accessibility.BrailleDisplayConnection.NativeInterface nativeInterface = this.val$nativeInterface;
            java.util.Objects.requireNonNull(nativeInterface);
            return (java.lang.String) readFromFileDescriptor(path, new java.util.function.Function() { // from class: com.android.server.accessibility.BrailleDisplayConnection$1$$ExternalSyntheticLambda3
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return nativeInterface.getHidrawUniq(((java.lang.Integer) obj).intValue());
                }
            });
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
        public int getDeviceBusType(java.nio.file.Path path) {
            java.util.Objects.requireNonNull(path);
            final com.android.server.accessibility.BrailleDisplayConnection.NativeInterface nativeInterface = this.val$nativeInterface;
            java.util.Objects.requireNonNull(nativeInterface);
            java.lang.Integer busType = (java.lang.Integer) readFromFileDescriptor(path, new java.util.function.Function() { // from class: com.android.server.accessibility.BrailleDisplayConnection$1$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return java.lang.Integer.valueOf(nativeInterface.getHidrawBusType(((java.lang.Integer) obj).intValue()));
                }
            });
            if (busType != null) {
                return busType.intValue();
            }
            return -1;
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
        public java.lang.String getName(java.nio.file.Path path) {
            java.util.Objects.requireNonNull(path);
            final com.android.server.accessibility.BrailleDisplayConnection.NativeInterface nativeInterface = this.val$nativeInterface;
            java.util.Objects.requireNonNull(nativeInterface);
            return (java.lang.String) readFromFileDescriptor(path, new java.util.function.Function() { // from class: com.android.server.accessibility.BrailleDisplayConnection$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return nativeInterface.getHidrawName(((java.lang.Integer) obj).intValue());
                }
            });
        }
    }

    static com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner getDefaultNativeScanner(com.android.server.accessibility.BrailleDisplayConnection.NativeInterface nativeInterface) {
        java.util.Objects.requireNonNull(nativeInterface);
        return new com.android.server.accessibility.BrailleDisplayConnection.AnonymousClass1(nativeInterface);
    }

    com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner setTestData(java.util.List<android.os.Bundle> brailleDisplays) {
        com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner brailleDisplayScanner;
        java.util.Objects.requireNonNull(brailleDisplays);
        final java.util.Map<java.nio.file.Path, android.os.Bundle> brailleDisplayMap = new android.util.ArrayMap<>();
        for (android.os.Bundle brailleDisplay : brailleDisplays) {
            java.nio.file.Path hidrawNodePath = java.nio.file.Path.of(brailleDisplay.getString("HIDRAW_PATH"), new java.lang.String[0]);
            brailleDisplayMap.put(hidrawNodePath, brailleDisplay);
        }
        synchronized (this.mLock) {
            this.mScanner = new com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner() { // from class: com.android.server.accessibility.BrailleDisplayConnection.2
                @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
                public java.util.Collection<java.nio.file.Path> getHidrawNodePaths(java.nio.file.Path directory) {
                    if (brailleDisplayMap.isEmpty()) {
                        return null;
                    }
                    return brailleDisplayMap.keySet();
                }

                @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
                public byte[] getDeviceReportDescriptor(java.nio.file.Path path) {
                    return ((android.os.Bundle) brailleDisplayMap.get(path)).getByteArray("DESCRIPTOR");
                }

                @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
                public java.lang.String getUniqueId(java.nio.file.Path path) {
                    return ((android.os.Bundle) brailleDisplayMap.get(path)).getString("UNIQUE_ID");
                }

                @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
                public int getDeviceBusType(java.nio.file.Path path) {
                    return ((android.os.Bundle) brailleDisplayMap.get(path)).getBoolean("BUS_BLUETOOTH") ? 5 : 3;
                }

                @Override // com.android.server.accessibility.BrailleDisplayConnection.BrailleDisplayScanner
                public java.lang.String getName(java.nio.file.Path path) {
                    return ((android.os.Bundle) brailleDisplayMap.get(path)).getString("NAME");
                }
            };
            brailleDisplayScanner = this.mScanner;
        }
        return brailleDisplayScanner;
    }

    private static class DefaultNativeInterface implements com.android.server.accessibility.BrailleDisplayConnection.NativeInterface {
        private DefaultNativeInterface() {
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.NativeInterface
        public int getHidrawDescSize(int fd) {
            return com.android.server.accessibility.BrailleDisplayConnection.nativeGetHidrawDescSize(fd);
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.NativeInterface
        public byte[] getHidrawDesc(int fd, int descSize) {
            return com.android.server.accessibility.BrailleDisplayConnection.nativeGetHidrawDesc(fd, descSize);
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.NativeInterface
        public java.lang.String getHidrawUniq(int fd) {
            return com.android.server.accessibility.BrailleDisplayConnection.nativeGetHidrawUniq(fd);
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.NativeInterface
        public int getHidrawBusType(int fd) {
            return com.android.server.accessibility.BrailleDisplayConnection.nativeGetHidrawBusType(fd);
        }

        @Override // com.android.server.accessibility.BrailleDisplayConnection.NativeInterface
        public java.lang.String getHidrawName(int fd) {
            return com.android.server.accessibility.BrailleDisplayConnection.nativeGetHidrawName(fd);
        }
    }
}
