package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbAlsaManager {
    private static final int ALSA_DEVICE_TYPE_CAPTURE = 2;
    private static final int ALSA_DEVICE_TYPE_MIDI = 3;
    private static final int ALSA_DEVICE_TYPE_PLAYBACK = 1;
    private static final int ALSA_DEVICE_TYPE_UNKNOWN = 0;
    private static final java.lang.String ALSA_DIRECTORY = "/dev/snd/";
    private static final boolean DEBUG = false;
    private static final int USB_DENYLIST_INPUT = 2;
    private static final int USB_DENYLIST_OUTPUT = 1;
    private android.media.IAudioService mAudioService;
    private final android.content.Context mContext;
    private final boolean mHasMidiFeature;
    private static final java.lang.String TAG = com.android.server.usb.UsbAlsaManager.class.getSimpleName();
    private static final boolean IS_MULTI_MODE = android.os.SystemProperties.getBoolean("ro.audio.multi_usb_mode", false);
    private static final int USB_VENDORID_SONY = 1356;
    private static final int USB_PRODUCTID_PS4CONTROLLER_ZCT1 = 1476;
    private static final int USB_PRODUCTID_PS4CONTROLLER_ZCT2 = 2508;
    private static final int USB_PRODUCTID_PS5CONTROLLER = 3302;
    static final java.util.List<com.android.server.usb.UsbAlsaManager.DenyListEntry> sDeviceDenylist = java.util.Arrays.asList(new com.android.server.usb.UsbAlsaManager.DenyListEntry(USB_VENDORID_SONY, USB_PRODUCTID_PS4CONTROLLER_ZCT1, 1), new com.android.server.usb.UsbAlsaManager.DenyListEntry(USB_VENDORID_SONY, USB_PRODUCTID_PS4CONTROLLER_ZCT2, 1), new com.android.server.usb.UsbAlsaManager.DenyListEntry(USB_VENDORID_SONY, USB_PRODUCTID_PS5CONTROLLER, 1));
    private final com.android.internal.alsa.AlsaCardsParser mCardsParser = new com.android.internal.alsa.AlsaCardsParser();
    private final java.util.ArrayList<com.android.server.usb.UsbAlsaDevice> mAlsaDevices = new java.util.ArrayList<>();
    private java.util.HashMap<java.lang.Integer, java.util.Stack<com.android.server.usb.UsbAlsaDevice>> mAttachedDevices = new java.util.HashMap<>();
    private final java.util.HashMap<java.lang.String, com.android.server.usb.UsbAlsaMidiDevice> mMidiDevices = new java.util.HashMap<>();
    private com.android.server.usb.UsbAlsaMidiDevice mPeripheralMidiDevice = null;
    private final java.util.HashSet<java.lang.Integer> mAlsaCards = new java.util.HashSet<>();
    private final android.os.FileObserver mAlsaObserver = new android.os.FileObserver(new java.io.File(ALSA_DIRECTORY), 768) { // from class: com.android.server.usb.UsbAlsaManager.1
        @Override // android.os.FileObserver
        public void onEvent(int event, java.lang.String path) {
            switch (event) {
                case 256:
                    com.android.server.usb.UsbAlsaManager.this.alsaFileAdded(path);
                    break;
                case 512:
                    com.android.server.usb.UsbAlsaManager.this.alsaFileRemoved(path);
                    break;
            }
        }
    };

    private static class DenyListEntry {
        final int mFlags;
        final int mProductId;
        final int mVendorId;

        DenyListEntry(int vendorId, int productId, int flags) {
            this.mVendorId = vendorId;
            this.mProductId = productId;
            this.mFlags = flags;
        }
    }

    private static boolean isDeviceDenylisted(int vendorId, int productId, int flags) {
        for (com.android.server.usb.UsbAlsaManager.DenyListEntry entry : sDeviceDenylist) {
            if (entry.mVendorId == vendorId && entry.mProductId == productId) {
                return (entry.mFlags & flags) != 0;
            }
        }
        return false;
    }

    UsbAlsaManager(android.content.Context context) {
        this.mContext = context;
        this.mHasMidiFeature = context.getPackageManager().hasSystemFeature("android.software.midi");
    }

    public void systemReady() {
        this.mAudioService = android.media.IAudioService.Stub.asInterface(android.os.ServiceManager.getService("audio"));
        this.mAlsaObserver.startWatching();
    }

    private synchronized void selectAlsaDevice(com.android.server.usb.UsbAlsaDevice alsaDevice) {
        int isDisabled = android.provider.Settings.Secure.getInt(this.mContext.getContentResolver(), "usb_audio_automatic_routing_disabled", 0);
        if (isDisabled != 0) {
            return;
        }
        alsaDevice.start();
    }

    private synchronized void deselectAlsaDevice(com.android.server.usb.UsbAlsaDevice selectedDevice) {
        selectedDevice.stop();
    }

    private int getAlsaDeviceListIndexFor(java.lang.String deviceAddress) {
        for (int index = 0; index < this.mAlsaDevices.size(); index++) {
            if (this.mAlsaDevices.get(index).getDeviceAddress().equals(deviceAddress)) {
                return index;
            }
        }
        return -1;
    }

    private void addDeviceToAttachedDevicesMap(int deviceType, com.android.server.usb.UsbAlsaDevice device) {
        if (deviceType == 0) {
            android.util.Slog.i(TAG, "Ignore caching device as the type is NONE, device=" + device);
            return;
        }
        java.util.Stack<com.android.server.usb.UsbAlsaDevice> devices = this.mAttachedDevices.get(java.lang.Integer.valueOf(deviceType));
        if (devices == null) {
            this.mAttachedDevices.put(java.lang.Integer.valueOf(deviceType), new java.util.Stack<>());
            devices = this.mAttachedDevices.get(java.lang.Integer.valueOf(deviceType));
        }
        devices.push(device);
    }

    private void addAlsaDevice(com.android.server.usb.UsbAlsaDevice device) {
        this.mAlsaDevices.add(0, device);
        addDeviceToAttachedDevicesMap(device.getInputDeviceType(), device);
        addDeviceToAttachedDevicesMap(device.getOutputDeviceType(), device);
    }

    private void removeDeviceFromAttachedDevicesMap(int deviceType, com.android.server.usb.UsbAlsaDevice device) {
        java.util.Stack<com.android.server.usb.UsbAlsaDevice> devices = this.mAttachedDevices.get(java.lang.Integer.valueOf(deviceType));
        if (devices == null) {
            return;
        }
        devices.remove(device);
        if (devices.isEmpty()) {
            this.mAttachedDevices.remove(java.lang.Integer.valueOf(deviceType));
        }
    }

    private com.android.server.usb.UsbAlsaDevice removeAlsaDevice(java.lang.String deviceAddress) {
        int index = getAlsaDeviceListIndexFor(deviceAddress);
        if (index > -1) {
            com.android.server.usb.UsbAlsaDevice device = this.mAlsaDevices.remove(index);
            removeDeviceFromAttachedDevicesMap(device.getOutputDeviceType(), device);
            removeDeviceFromAttachedDevicesMap(device.getInputDeviceType(), device);
            return device;
        }
        return null;
    }

    private com.android.server.usb.UsbAlsaDevice selectDefaultDevice(int deviceType) {
        java.util.Stack<com.android.server.usb.UsbAlsaDevice> devices = this.mAttachedDevices.get(java.lang.Integer.valueOf(deviceType));
        if (devices == null || devices.isEmpty()) {
            return null;
        }
        com.android.server.usb.UsbAlsaDevice alsaDevice = devices.peek();
        android.util.Slog.d(TAG, "select default device:" + alsaDevice);
        if (android.media.AudioManager.isInputDevice(deviceType)) {
            alsaDevice.startInput();
        } else {
            alsaDevice.startOutput();
        }
        return alsaDevice;
    }

    private void deselectCurrentDevice(int deviceType) {
        java.util.Stack<com.android.server.usb.UsbAlsaDevice> devices;
        if (deviceType == 0 || (devices = this.mAttachedDevices.get(java.lang.Integer.valueOf(deviceType))) == null || devices.isEmpty()) {
            return;
        }
        com.android.server.usb.UsbAlsaDevice alsaDevice = devices.peek();
        android.util.Slog.d(TAG, "deselect current device:" + alsaDevice);
        if (android.media.AudioManager.isInputDevice(deviceType)) {
            alsaDevice.stopInput();
        } else {
            alsaDevice.stopOutput();
        }
    }

    void usbDeviceAdded(java.lang.String deviceAddress, android.hardware.usb.UsbDevice usbDevice, com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        this.mCardsParser.scan();
        com.android.internal.alsa.AlsaCardsParser.AlsaCardRecord cardRec = this.mCardsParser.findCardNumFor(deviceAddress);
        if (cardRec != null) {
            waitForAlsaDevice(cardRec.getCardNum(), true);
            boolean hasInput = parser.hasInput() && !isDeviceDenylisted(usbDevice.getVendorId(), usbDevice.getProductId(), 2);
            boolean hasOutput = parser.hasOutput() && !isDeviceDenylisted(usbDevice.getVendorId(), usbDevice.getProductId(), 1);
            if (hasInput || hasOutput) {
                boolean isInputHeadset = parser.isInputHeadset();
                boolean isOutputHeadset = parser.isOutputHeadset();
                boolean isDock = parser.isDock();
                if (this.mAudioService == null) {
                    android.util.Slog.e(TAG, "no AudioService");
                    return;
                }
                com.android.server.usb.UsbAlsaDevice alsaDevice = new com.android.server.usb.UsbAlsaDevice(this.mAudioService, cardRec.getCardNum(), 0, deviceAddress, hasOutput, hasInput, isInputHeadset, isOutputHeadset, isDock);
                alsaDevice.setDeviceNameAndDescription(cardRec.getCardName(), cardRec.getCardDescription());
                if (IS_MULTI_MODE) {
                    deselectCurrentDevice(alsaDevice.getInputDeviceType());
                    deselectCurrentDevice(alsaDevice.getOutputDeviceType());
                } else if (!this.mAlsaDevices.isEmpty()) {
                    deselectAlsaDevice(this.mAlsaDevices.get(0));
                }
                addAlsaDevice(alsaDevice);
                selectAlsaDevice(alsaDevice);
            }
            addMidiDevice(deviceAddress, usbDevice, parser, cardRec);
            logDevices("deviceAdded()");
            return;
        }
        if (parser.hasAudioInterface()) {
            android.util.Slog.e(TAG, "usbDeviceAdded(): cannot find sound card for " + deviceAddress);
        }
    }

    private void addMidiDevice(java.lang.String deviceAddress, android.hardware.usb.UsbDevice usbDevice, com.android.server.usb.descriptors.UsbDescriptorParser parser, com.android.internal.alsa.AlsaCardsParser.AlsaCardRecord cardRec) {
        java.lang.String name;
        boolean hasMidi = parser.hasMIDIInterface();
        boolean hasMidi2 = parser.containsUniversalMidiDeviceEndpoint();
        if (this.mHasMidiFeature && hasMidi && !hasMidi2) {
            android.os.Bundle properties = new android.os.Bundle();
            java.lang.String manufacturer = usbDevice.getManufacturerName();
            java.lang.String product = usbDevice.getProductName();
            java.lang.String version = usbDevice.getVersion();
            if (manufacturer == null || manufacturer.isEmpty()) {
                name = product;
            } else if (product == null || product.isEmpty()) {
                name = manufacturer;
            } else {
                name = manufacturer + " " + product;
            }
            properties.putString("name", name);
            properties.putString("manufacturer", manufacturer);
            properties.putString("product", product);
            properties.putString("version", version);
            properties.putString("serial_number", usbDevice.getSerialNumber());
            properties.putInt("alsa_card", cardRec.getCardNum());
            properties.putInt("alsa_device", 0);
            properties.putParcelable("usb_device", usbDevice);
            int numLegacyMidiInputs = parser.calculateNumLegacyMidiInputs();
            int numLegacyMidiOutputs = parser.calculateNumLegacyMidiOutputs();
            com.android.server.usb.UsbAlsaMidiDevice midiDevice = com.android.server.usb.UsbAlsaMidiDevice.create(this.mContext, properties, cardRec.getCardNum(), 0, numLegacyMidiInputs, numLegacyMidiOutputs);
            if (midiDevice != null) {
                this.mMidiDevices.put(deviceAddress, midiDevice);
            }
        }
    }

    synchronized void usbDeviceRemoved(java.lang.String deviceAddress) {
        com.android.server.usb.UsbAlsaDevice alsaDevice = removeAlsaDevice(deviceAddress);
        android.util.Slog.i(TAG, "USB Audio Device Removed: " + alsaDevice);
        if (alsaDevice != null) {
            waitForAlsaDevice(alsaDevice.getCardNum(), false);
            deselectAlsaDevice(alsaDevice);
            if (IS_MULTI_MODE) {
                selectDefaultDevice(alsaDevice.getOutputDeviceType());
                selectDefaultDevice(alsaDevice.getInputDeviceType());
            } else if (!this.mAlsaDevices.isEmpty() && this.mAlsaDevices.get(0) != null) {
                selectAlsaDevice(this.mAlsaDevices.get(0));
            }
        }
        com.android.server.usb.UsbAlsaMidiDevice midiDevice = this.mMidiDevices.remove(deviceAddress);
        if (midiDevice != null) {
            android.util.Slog.i(TAG, "USB MIDI Device Removed: " + deviceAddress);
            libcore.io.IoUtils.closeQuietly(midiDevice);
        }
        logDevices("usbDeviceRemoved()");
    }

    void setPeripheralMidiState(boolean enabled, int card, int device) {
        if (!this.mHasMidiFeature) {
            return;
        }
        if (enabled && this.mPeripheralMidiDevice == null) {
            android.os.Bundle properties = new android.os.Bundle();
            android.content.res.Resources r = this.mContext.getResources();
            properties.putString("name", r.getString(android.R.string.sync_too_many_deletes));
            properties.putString("manufacturer", r.getString(android.R.string.sync_really_delete));
            properties.putString("product", r.getString(android.R.string.sync_too_many_deletes_desc));
            properties.putInt("alsa_card", card);
            properties.putInt("alsa_device", device);
            this.mPeripheralMidiDevice = com.android.server.usb.UsbAlsaMidiDevice.create(this.mContext, properties, card, device, 1, 1);
            return;
        }
        if (!enabled && this.mPeripheralMidiDevice != null) {
            libcore.io.IoUtils.closeQuietly(this.mPeripheralMidiDevice);
            this.mPeripheralMidiDevice = null;
        }
    }

    private boolean waitForAlsaDevice(int card, boolean isAdded) {
        boolean cardFound;
        synchronized (this.mAlsaCards) {
            long timeoutMs = android.os.SystemClock.elapsedRealtime() + 2500;
            while ((this.mAlsaCards.contains(java.lang.Integer.valueOf(card)) ^ isAdded) && timeoutMs > android.os.SystemClock.elapsedRealtime()) {
                long waitTimeMs = timeoutMs - android.os.SystemClock.elapsedRealtime();
                if (waitTimeMs > 0) {
                    try {
                        this.mAlsaCards.wait(waitTimeMs);
                    } catch (java.lang.InterruptedException e) {
                        android.util.Slog.d(TAG, "usb: InterruptedException while waiting for ALSA file.");
                    }
                }
            }
            cardFound = this.mAlsaCards.contains(java.lang.Integer.valueOf(card));
            if ((isAdded ^ cardFound) && timeoutMs > android.os.SystemClock.elapsedRealtime()) {
                android.util.Slog.e(TAG, "waitForAlsaDevice(" + card + ") timeout");
            } else {
                android.util.Slog.i(TAG, "waitForAlsaDevice for device card=" + card + ", isAdded=" + isAdded + ", found=" + cardFound);
            }
        }
        return cardFound;
    }

    private int getCardNumberFromAlsaFilePath(java.lang.String path) {
        int type = 0;
        if (path.startsWith("pcmC")) {
            if (path.endsWith("p")) {
                type = 1;
            } else if (path.endsWith("c")) {
                type = 2;
            }
        } else if (path.startsWith("midiC")) {
            type = 3;
        }
        if (type == 0) {
            android.util.Slog.i(TAG, "Unknown type file(" + path + ") added.");
            return -1;
        }
        try {
            int c_index = path.indexOf(67);
            int d_index = path.indexOf(68);
            return java.lang.Integer.parseInt(path.substring(c_index + 1, d_index));
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Could not parse ALSA file name " + path, e);
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alsaFileAdded(java.lang.String path) {
        android.util.Slog.i(TAG, "alsaFileAdded(" + path + ")");
        int card = getCardNumberFromAlsaFilePath(path);
        if (card == -1) {
            return;
        }
        synchronized (this.mAlsaCards) {
            if (!this.mAlsaCards.contains(java.lang.Integer.valueOf(card))) {
                android.util.Slog.d(TAG, "Adding ALSA device card=" + card);
                this.mAlsaCards.add(java.lang.Integer.valueOf(card));
                this.mAlsaCards.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void alsaFileRemoved(java.lang.String path) {
        int card = getCardNumberFromAlsaFilePath(path);
        if (card == -1) {
            return;
        }
        synchronized (this.mAlsaCards) {
            this.mAlsaCards.remove(java.lang.Integer.valueOf(card));
        }
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("cards_parser", 1120986464257L, this.mCardsParser.getScanStatus());
        for (com.android.server.usb.UsbAlsaDevice usbAlsaDevice : this.mAlsaDevices) {
            usbAlsaDevice.dump(dump, "alsa_devices", 2246267895810L);
        }
        for (java.lang.String deviceAddr : this.mMidiDevices.keySet()) {
            this.mMidiDevices.get(deviceAddr).dump(deviceAddr, dump, "alsa_midi_devices", 2246267895812L);
        }
        dump.end(token);
    }

    public void logDevicesList(java.lang.String title) {
    }

    public void logDevices(java.lang.String title) {
    }
}
