package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbHostManager {
    private static final boolean DEBUG = true;
    private static final int LINUX_FOUNDATION_VID = 7531;
    private static final int MAX_CONNECT_RECORDS = 32;
    private static final int MAX_UNIQUE_CODE_GENERATION_ATTEMPTS = 10;
    private static final java.lang.String TAG = com.android.server.usb.UsbHostManager.class.getSimpleName();
    static final java.text.SimpleDateFormat sFormat = new java.text.SimpleDateFormat("MM-dd HH:mm:ss:SSS");
    private final android.content.Context mContext;
    private com.android.server.usb.UsbProfileGroupSettingsManager mCurrentSettings;
    private final boolean mHasMidiFeature;
    private final java.lang.String[] mHostDenyList;
    private com.android.server.usb.UsbHostManager.ConnectionRecord mLastConnect;
    private int mNumConnects;
    private final com.android.server.usb.UsbPermissionManager mPermissionManager;
    private final com.android.server.usb.UsbAlsaManager mUsbAlsaManager;
    private android.content.ComponentName mUsbDeviceConnectionHandler;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.HashMap<java.lang.String, android.hardware.usb.UsbDevice> mDevices = new java.util.HashMap<>();
    private java.lang.Object mSettingsLock = new java.lang.Object();
    private java.lang.Object mHandlerLock = new java.lang.Object();
    private final java.util.LinkedList<com.android.server.usb.UsbHostManager.ConnectionRecord> mConnections = new java.util.LinkedList<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.usb.UsbHostManager.ConnectionRecord> mConnected = new android.util.ArrayMap<>();
    private final java.util.HashMap<java.lang.String, java.util.ArrayList<com.android.server.usb.UsbDirectMidiDevice>> mMidiDevices = new java.util.HashMap<>();
    private final java.util.HashSet<java.lang.String> mMidiUniqueCodes = new java.util.HashSet<>();
    private final java.util.Random mRandom = new java.util.Random();

    /* JADX INFO: Access modifiers changed from: private */
    public native void monitorUsbHostBus();

    private native android.os.ParcelFileDescriptor nativeOpenDevice(java.lang.String str);

    class ConnectionRecord {
        static final int CONNECT = 0;
        static final int CONNECT_BADDEVICE = 2;
        static final int CONNECT_BADPARSE = 1;
        static final int DISCONNECT = -1;
        private static final int kDumpBytesPerLine = 16;
        final byte[] mDescriptors;
        java.lang.String mDeviceAddress;
        final int mMode;
        long mTimestamp = java.lang.System.currentTimeMillis();

        ConnectionRecord(java.lang.String deviceAddress, int mode, byte[] descriptors) {
            this.mDeviceAddress = deviceAddress;
            this.mMode = mode;
            this.mDescriptors = descriptors;
        }

        private java.lang.String formatTime() {
            return new java.lang.StringBuilder(com.android.server.usb.UsbHostManager.sFormat.format(new java.util.Date(this.mTimestamp))).toString();
        }

        void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
            long token = dump.start(idName, id);
            dump.write("device_address", 1138166333441L, this.mDeviceAddress);
            dump.write(com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, 1159641169922L, this.mMode);
            dump.write(com.android.server.net.watchlist.WatchlistLoggingHandler.WatchlistEventKeys.TIMESTAMP, 1112396529667L, this.mTimestamp);
            if (this.mMode != -1) {
                com.android.server.usb.descriptors.UsbDescriptorParser parser = new com.android.server.usb.descriptors.UsbDescriptorParser(this.mDeviceAddress, this.mDescriptors);
                com.android.server.usb.descriptors.UsbDeviceDescriptor deviceDescriptor = parser.getDeviceDescriptor();
                dump.write("manufacturer", 1120986464260L, deviceDescriptor.getVendorID());
                dump.write("product", 1120986464261L, deviceDescriptor.getProductID());
                long isHeadSetToken = dump.start("is_headset", 1146756268038L);
                dump.write("in", 1133871366145L, parser.isInputHeadset());
                dump.write("out", 1133871366146L, parser.isOutputHeadset());
                dump.end(isHeadSetToken);
            }
            dump.end(token);
        }

        void dumpShort(com.android.internal.util.IndentingPrintWriter pw) {
            if (this.mMode != -1) {
                pw.println(formatTime() + " Connect " + this.mDeviceAddress + " mode:" + this.mMode);
                com.android.server.usb.descriptors.UsbDescriptorParser parser = new com.android.server.usb.descriptors.UsbDescriptorParser(this.mDeviceAddress, this.mDescriptors);
                com.android.server.usb.descriptors.UsbDeviceDescriptor deviceDescriptor = parser.getDeviceDescriptor();
                pw.println("manfacturer:0x" + java.lang.Integer.toHexString(deviceDescriptor.getVendorID()) + " product:" + java.lang.Integer.toHexString(deviceDescriptor.getProductID()));
                pw.println("isHeadset[in: " + parser.isInputHeadset() + " , out: " + parser.isOutputHeadset() + "], isDock: " + parser.isDock());
                return;
            }
            pw.println(formatTime() + " Disconnect " + this.mDeviceAddress);
        }

        void dumpTree(com.android.internal.util.IndentingPrintWriter pw) {
            if (this.mMode != -1) {
                pw.println(formatTime() + " Connect " + this.mDeviceAddress + " mode:" + this.mMode);
                com.android.server.usb.descriptors.UsbDescriptorParser parser = new com.android.server.usb.descriptors.UsbDescriptorParser(this.mDeviceAddress, this.mDescriptors);
                java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
                com.android.server.usb.descriptors.tree.UsbDescriptorsTree descriptorTree = new com.android.server.usb.descriptors.tree.UsbDescriptorsTree();
                descriptorTree.parse(parser);
                descriptorTree.report(new com.android.server.usb.descriptors.report.TextReportCanvas(parser, stringBuilder));
                stringBuilder.append("isHeadset[in: " + parser.isInputHeadset() + " , out: " + parser.isOutputHeadset() + "], isDock: " + parser.isDock());
                pw.println(stringBuilder.toString());
                return;
            }
            pw.println(formatTime() + " Disconnect " + this.mDeviceAddress);
        }

        void dumpList(com.android.internal.util.IndentingPrintWriter pw) {
            if (this.mMode != -1) {
                pw.println(formatTime() + " Connect " + this.mDeviceAddress + " mode:" + this.mMode);
                com.android.server.usb.descriptors.UsbDescriptorParser parser = new com.android.server.usb.descriptors.UsbDescriptorParser(this.mDeviceAddress, this.mDescriptors);
                java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
                com.android.server.usb.descriptors.report.TextReportCanvas canvas = new com.android.server.usb.descriptors.report.TextReportCanvas(parser, stringBuilder);
                for (com.android.server.usb.descriptors.UsbDescriptor descriptor : parser.getDescriptors()) {
                    descriptor.report(canvas);
                }
                pw.println(stringBuilder.toString());
                pw.println("isHeadset[in: " + parser.isInputHeadset() + " , out: " + parser.isOutputHeadset() + "], isDock: " + parser.isDock());
                return;
            }
            pw.println(formatTime() + " Disconnect " + this.mDeviceAddress);
        }

        void dumpRaw(com.android.internal.util.IndentingPrintWriter pw) {
            if (this.mMode != -1) {
                pw.println(formatTime() + " Connect " + this.mDeviceAddress + " mode:" + this.mMode);
                int length = this.mDescriptors.length;
                pw.println("Raw Descriptors " + length + " bytes");
                int dataOffset = 0;
                for (int line = 0; line < length / 16; line++) {
                    java.lang.StringBuilder sb = new java.lang.StringBuilder();
                    int offset = 0;
                    while (offset < 16) {
                        sb.append(java.lang.String.format("0x%02X", java.lang.Byte.valueOf(this.mDescriptors[dataOffset]))).append(" ");
                        offset++;
                        dataOffset++;
                    }
                    pw.println(sb.toString());
                }
                java.lang.StringBuilder sb2 = new java.lang.StringBuilder();
                while (dataOffset < length) {
                    sb2.append(java.lang.String.format("0x%02X", java.lang.Byte.valueOf(this.mDescriptors[dataOffset]))).append(" ");
                    dataOffset++;
                }
                pw.println(sb2.toString());
                return;
            }
            pw.println(formatTime() + " Disconnect " + this.mDeviceAddress);
        }
    }

    public UsbHostManager(android.content.Context context, com.android.server.usb.UsbAlsaManager alsaManager, com.android.server.usb.UsbPermissionManager permissionManager) {
        this.mContext = context;
        this.mHostDenyList = context.getResources().getStringArray(android.R.array.config_toastCrossUserPackages);
        this.mUsbAlsaManager = alsaManager;
        this.mPermissionManager = permissionManager;
        java.lang.String deviceConnectionHandler = context.getResources().getString(android.R.string.config_batterySaverDeviceSpecificConfig);
        if (!android.text.TextUtils.isEmpty(deviceConnectionHandler)) {
            setUsbDeviceConnectionHandler(android.content.ComponentName.unflattenFromString(deviceConnectionHandler));
        }
        this.mHasMidiFeature = context.getPackageManager().hasSystemFeature("android.software.midi");
    }

    public void setCurrentUserSettings(com.android.server.usb.UsbProfileGroupSettingsManager settings) {
        synchronized (this.mSettingsLock) {
            this.mCurrentSettings = settings;
        }
    }

    private com.android.server.usb.UsbProfileGroupSettingsManager getCurrentUserSettings() {
        com.android.server.usb.UsbProfileGroupSettingsManager usbProfileGroupSettingsManager;
        synchronized (this.mSettingsLock) {
            usbProfileGroupSettingsManager = this.mCurrentSettings;
        }
        return usbProfileGroupSettingsManager;
    }

    public void setUsbDeviceConnectionHandler(android.content.ComponentName usbDeviceConnectionHandler) {
        synchronized (this.mHandlerLock) {
            this.mUsbDeviceConnectionHandler = usbDeviceConnectionHandler;
        }
    }

    private android.content.ComponentName getUsbDeviceConnectionHandler() {
        android.content.ComponentName componentName;
        synchronized (this.mHandlerLock) {
            componentName = this.mUsbDeviceConnectionHandler;
        }
        return componentName;
    }

    private boolean isDenyListed(java.lang.String deviceAddress) {
        int count = this.mHostDenyList.length;
        for (int i = 0; i < count; i++) {
            if (deviceAddress.startsWith(this.mHostDenyList[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean isDenyListed(int clazz, int subClass) {
        if (clazz == 9) {
            return true;
        }
        return clazz == 3 && subClass == 1;
    }

    private void addConnectionRecord(java.lang.String deviceAddress, int mode, byte[] rawDescriptors) {
        this.mNumConnects++;
        while (this.mConnections.size() >= 32) {
            this.mConnections.removeFirst();
        }
        com.android.server.usb.UsbHostManager.ConnectionRecord rec = new com.android.server.usb.UsbHostManager.ConnectionRecord(deviceAddress, mode, rawDescriptors);
        this.mConnections.add(rec);
        if (mode != -1) {
            this.mLastConnect = rec;
        }
        if (mode == 0) {
            this.mConnected.put(deviceAddress, rec);
        } else if (mode == -1) {
            this.mConnected.remove(deviceAddress);
        }
    }

    private void logUsbDevice(com.android.server.usb.descriptors.UsbDescriptorParser descriptorParser) {
        int vid = 0;
        int pid = 0;
        java.lang.String mfg = "<unknown>";
        java.lang.String product = "<unknown>";
        java.lang.String version = "<unknown>";
        java.lang.String serial = "<unknown>";
        com.android.server.usb.descriptors.UsbDeviceDescriptor deviceDescriptor = descriptorParser.getDeviceDescriptor();
        if (deviceDescriptor != null) {
            vid = deviceDescriptor.getVendorID();
            pid = deviceDescriptor.getProductID();
            mfg = deviceDescriptor.getMfgString(descriptorParser);
            product = deviceDescriptor.getProductString(descriptorParser);
            version = deviceDescriptor.getDeviceReleaseString();
            serial = deviceDescriptor.getSerialString(descriptorParser);
        }
        if (vid == LINUX_FOUNDATION_VID) {
            return;
        }
        boolean hasAudio = descriptorParser.hasAudioInterface();
        boolean hasHid = descriptorParser.hasHIDInterface();
        boolean hasStorage = descriptorParser.hasStorageInterface();
        java.lang.String attachedString = "USB device attached: " + java.lang.String.format("vidpid %04x:%04x", java.lang.Integer.valueOf(vid), java.lang.Integer.valueOf(pid));
        android.util.Slog.d(TAG, (attachedString + java.lang.String.format(" mfg/product/ver/serial %s/%s/%s/%s", mfg, product, version, serial)) + java.lang.String.format(" hasAudio/HID/Storage: %b/%b/%b", java.lang.Boolean.valueOf(hasAudio), java.lang.Boolean.valueOf(hasHid), java.lang.Boolean.valueOf(hasStorage)));
    }

    private boolean usbDeviceAdded(java.lang.String deviceAddress, int deviceClass, int deviceSubclass, byte[] descriptors) {
        android.util.Slog.d(TAG, "usbDeviceAdded(" + deviceAddress + ") - start");
        if (!isDenyListed(deviceAddress)) {
            if (isDenyListed(deviceClass, deviceSubclass)) {
                android.util.Slog.d(TAG, "device class is deny listed");
                return false;
            }
            if (descriptors == null) {
                android.util.Slog.e(TAG, "Failed to add device as the descriptor is null");
                return false;
            }
            com.android.server.usb.descriptors.UsbDescriptorParser parser = new com.android.server.usb.descriptors.UsbDescriptorParser(deviceAddress, descriptors);
            if (deviceClass == 0 && !checkUsbInterfacesDenyListed(parser)) {
                return false;
            }
            logUsbDevice(parser);
            synchronized (this.mLock) {
                if (this.mDevices.get(deviceAddress) != null) {
                    android.util.Slog.w(TAG, "device already on mDevices list: " + deviceAddress);
                    return false;
                }
                android.hardware.usb.UsbDevice.Builder newDeviceBuilder = parser.toAndroidUsbDeviceBuilder();
                if (newDeviceBuilder == null) {
                    android.util.Slog.e(TAG, "Couldn't create UsbDevice object.");
                    addConnectionRecord(deviceAddress, 2, parser.getRawDescriptors());
                } else {
                    com.android.server.usb.UsbSerialReader serialNumberReader = new com.android.server.usb.UsbSerialReader(this.mContext, this.mPermissionManager, newDeviceBuilder.serialNumber);
                    android.hardware.usb.UsbDevice newDevice = newDeviceBuilder.build(serialNumberReader);
                    serialNumberReader.setDevice(newDevice);
                    this.mDevices.put(deviceAddress, newDevice);
                    android.util.Slog.d(TAG, "Added device " + newDevice);
                    com.android.server.usb.UsbDeviceManager.getOplusUsbDeviceFeature().usbHostRecord(this.mContext, newDevice);
                    android.content.ComponentName usbDeviceConnectionHandler = getUsbDeviceConnectionHandler();
                    if (usbDeviceConnectionHandler == null) {
                        getCurrentUserSettings().deviceAttached(newDevice);
                    } else {
                        getCurrentUserSettings().deviceAttachedForFixedHandler(newDevice, usbDeviceConnectionHandler);
                    }
                    this.mUsbAlsaManager.usbDeviceAdded(deviceAddress, newDevice, parser);
                    if (this.mHasMidiFeature) {
                        java.lang.String uniqueUsbDeviceIdentifier = generateNewUsbDeviceIdentifier();
                        java.util.ArrayList<com.android.server.usb.UsbDirectMidiDevice> midiDevices = new java.util.ArrayList<>();
                        if (parser.containsUniversalMidiDeviceEndpoint()) {
                            com.android.server.usb.UsbDirectMidiDevice midiDevice = com.android.server.usb.UsbDirectMidiDevice.create(this.mContext, newDevice, parser, true, uniqueUsbDeviceIdentifier);
                            if (midiDevice != null) {
                                midiDevices.add(midiDevice);
                            } else {
                                android.util.Slog.e(TAG, "Universal Midi Device is null.");
                            }
                            if (parser.containsLegacyMidiDeviceEndpoint()) {
                                com.android.server.usb.UsbDirectMidiDevice midiDevice2 = com.android.server.usb.UsbDirectMidiDevice.create(this.mContext, newDevice, parser, false, uniqueUsbDeviceIdentifier);
                                if (midiDevice2 != null) {
                                    midiDevices.add(midiDevice2);
                                } else {
                                    android.util.Slog.e(TAG, "Legacy Midi Device is null.");
                                }
                            }
                        }
                        if (!midiDevices.isEmpty()) {
                            this.mMidiDevices.put(deviceAddress, midiDevices);
                        }
                    }
                    addConnectionRecord(deviceAddress, 0, descriptors);
                    com.android.internal.util.FrameworkStatsLog.write(77, newDevice.getVendorId(), newDevice.getProductId(), parser.hasAudioInterface(), parser.hasHIDInterface(), parser.hasStorageInterface(), 1, 0L);
                }
                android.util.Slog.d(TAG, "beginUsbDeviceAdded(" + deviceAddress + ") end");
                return true;
            }
        }
        android.util.Slog.d(TAG, "device address is Deny listed");
        return false;
    }

    private void usbDeviceRemoved(java.lang.String deviceAddress) {
        android.util.Slog.d(TAG, "usbDeviceRemoved(" + deviceAddress + ") end");
        synchronized (this.mLock) {
            android.hardware.usb.UsbDevice device = this.mDevices.remove(deviceAddress);
            if (device != null) {
                android.util.Slog.d(TAG, "Removed device at " + deviceAddress + ": " + device.getProductName());
                this.mUsbAlsaManager.usbDeviceRemoved(deviceAddress);
                this.mPermissionManager.usbDeviceRemoved(device);
                java.util.ArrayList<com.android.server.usb.UsbDirectMidiDevice> midiDevices = this.mMidiDevices.remove(deviceAddress);
                if (midiDevices != null) {
                    for (com.android.server.usb.UsbDirectMidiDevice midiDevice : midiDevices) {
                        if (midiDevice != null) {
                            libcore.io.IoUtils.closeQuietly(midiDevice);
                        }
                    }
                    android.util.Slog.i(TAG, "USB MIDI Devices Removed: " + deviceAddress);
                }
                getCurrentUserSettings().usbDeviceRemoved(device);
                com.android.server.usb.UsbHostManager.ConnectionRecord current = this.mConnected.get(deviceAddress);
                addConnectionRecord(deviceAddress, -1, null);
                if (current != null) {
                    com.android.server.usb.descriptors.UsbDescriptorParser parser = new com.android.server.usb.descriptors.UsbDescriptorParser(deviceAddress, current.mDescriptors);
                    com.android.internal.util.FrameworkStatsLog.write(77, device.getVendorId(), device.getProductId(), parser.hasAudioInterface(), parser.hasHIDInterface(), parser.hasStorageInterface(), 0, java.lang.System.currentTimeMillis() - current.mTimestamp);
                }
            } else {
                android.util.Slog.d(TAG, "Removed device at " + deviceAddress + " was already gone");
            }
        }
    }

    public void systemReady() {
        synchronized (this.mLock) {
            java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.usb.UsbHostManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.monitorUsbHostBus();
                }
            };
            new java.lang.Thread(null, runnable, "UsbService host thread").start();
        }
    }

    public void getDeviceList(android.os.Bundle devices) {
        synchronized (this.mLock) {
            for (java.lang.String name : this.mDevices.keySet()) {
                devices.putParcelable(name, this.mDevices.get(name));
            }
        }
    }

    public android.os.ParcelFileDescriptor openDevice(java.lang.String deviceAddress, com.android.server.usb.UsbUserPermissionManager permissions, java.lang.String packageName, int pid, int uid) {
        android.os.ParcelFileDescriptor parcelFileDescriptorNativeOpenDevice;
        synchronized (this.mLock) {
            if (isDenyListed(deviceAddress)) {
                throw new java.lang.SecurityException("USB device is on a restricted bus");
            }
            android.hardware.usb.UsbDevice device = this.mDevices.get(deviceAddress);
            if (device == null) {
                throw new java.lang.IllegalArgumentException("device " + deviceAddress + " does not exist or is restricted");
            }
            permissions.checkPermission(device, packageName, pid, uid);
            parcelFileDescriptorNativeOpenDevice = nativeOpenDevice(deviceAddress);
        }
        return parcelFileDescriptorNativeOpenDevice;
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        synchronized (this.mHandlerLock) {
            if (this.mUsbDeviceConnectionHandler != null) {
                com.android.internal.util.dump.DumpUtils.writeComponentName(dump, "default_usb_host_connection_handler", 1146756268033L, this.mUsbDeviceConnectionHandler);
            }
        }
        synchronized (this.mLock) {
            for (java.lang.String name : this.mDevices.keySet()) {
                com.android.internal.usb.DumpUtils.writeDevice(dump, "devices", 2246267895810L, this.mDevices.get(name));
            }
            dump.write("num_connects", 1120986464259L, this.mNumConnects);
            for (com.android.server.usb.UsbHostManager.ConnectionRecord rec : this.mConnections) {
                rec.dump(dump, "connections", 2246267895812L);
            }
            for (java.util.ArrayList<com.android.server.usb.UsbDirectMidiDevice> directMidiDevices : this.mMidiDevices.values()) {
                for (com.android.server.usb.UsbDirectMidiDevice directMidiDevice : directMidiDevices) {
                    directMidiDevice.dump(dump, "midi_devices", 2246267895813L);
                }
            }
        }
        dump.end(token);
    }

    public void dumpDescriptors(com.android.internal.util.IndentingPrintWriter pw, java.lang.String[] args) {
        if (this.mLastConnect != null) {
            pw.println("Last Connected USB Device:");
            if (args.length <= 1 || args[1].equals("-dump-short")) {
                this.mLastConnect.dumpShort(pw);
                return;
            }
            if (args[1].equals("-dump-tree")) {
                this.mLastConnect.dumpTree(pw);
                return;
            } else if (args[1].equals("-dump-list")) {
                this.mLastConnect.dumpList(pw);
                return;
            } else {
                if (args[1].equals("-dump-raw")) {
                    this.mLastConnect.dumpRaw(pw);
                    return;
                }
                return;
            }
        }
        pw.println("No USB Devices have been connected.");
    }

    private boolean checkUsbInterfacesDenyListed(com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        boolean shouldIgnoreDevice = false;
        for (com.android.server.usb.descriptors.UsbDescriptor descriptor : parser.getDescriptors()) {
            if (descriptor instanceof com.android.server.usb.descriptors.UsbInterfaceDescriptor) {
                com.android.server.usb.descriptors.UsbInterfaceDescriptor iface = (com.android.server.usb.descriptors.UsbInterfaceDescriptor) descriptor;
                shouldIgnoreDevice = isDenyListed(iface.getUsbClass(), iface.getUsbSubclass());
                if (!shouldIgnoreDevice) {
                    break;
                }
            }
        }
        if (shouldIgnoreDevice) {
            android.util.Slog.d(TAG, "usb interface class is deny listed");
            return false;
        }
        return true;
    }

    private java.lang.String generateNewUsbDeviceIdentifier() {
        java.lang.String code;
        int numberOfAttempts = 0;
        do {
            if (numberOfAttempts > 10) {
                android.util.Slog.w(TAG, "MIDI unique code array resetting");
                this.mMidiUniqueCodes.clear();
                numberOfAttempts = 0;
            }
            code = "";
            for (int i = 0; i < 3; i++) {
                code = code + this.mRandom.nextInt(10);
            }
            numberOfAttempts++;
        } while (this.mMidiUniqueCodes.contains(code));
        this.mMidiUniqueCodes.add(code);
        return code;
    }
}
