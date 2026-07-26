package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbAlsaMidiDevice implements java.io.Closeable {
    private static final int BUFFER_SIZE = 512;
    private static final java.lang.String TAG = "UsbAlsaMidiDevice";
    private final int mAlsaCard;
    private final int mAlsaDevice;
    private com.android.internal.midi.MidiEventScheduler[] mEventSchedulers;
    private java.io.FileDescriptor[] mFileDescriptors;
    private java.io.FileInputStream[] mInputStreams;
    private boolean mIsOpen;
    private final com.android.server.usb.UsbAlsaMidiDevice.InputReceiverProxy[] mMidiInputPortReceivers;
    private final int mNumInputs;
    private final int mNumOutputs;
    private java.io.FileOutputStream[] mOutputStreams;
    private android.system.StructPollfd[] mPollFDs;
    private com.android.server.usb.PowerBoostSetter mPowerBoostSetter;
    private android.media.midi.MidiDeviceServer mServer;
    private boolean mServerAvailable;
    private final java.lang.Object mLock = new java.lang.Object();
    private int mPipeFD = -1;
    private final android.media.midi.MidiDeviceServer.Callback mCallback = new android.media.midi.MidiDeviceServer.Callback() { // from class: com.android.server.usb.UsbAlsaMidiDevice.1
        public void onDeviceStatusChanged(android.media.midi.MidiDeviceServer server, android.media.midi.MidiDeviceStatus status) {
            android.media.midi.MidiDeviceInfo deviceInfo = status.getDeviceInfo();
            int numInputPorts = deviceInfo.getInputPortCount();
            int numOutputPorts = deviceInfo.getOutputPortCount();
            int numOpenPorts = 0;
            for (int i = 0; i < numInputPorts; i++) {
                if (status.isInputPortOpen(i)) {
                    numOpenPorts++;
                }
            }
            for (int i2 = 0; i2 < numOutputPorts; i2++) {
                if (status.getOutputPortOpenCount(i2) > 0) {
                    numOpenPorts += status.getOutputPortOpenCount(i2);
                }
            }
            synchronized (com.android.server.usb.UsbAlsaMidiDevice.this.mLock) {
                android.util.Log.d(com.android.server.usb.UsbAlsaMidiDevice.TAG, "numOpenPorts: " + numOpenPorts + " isOpen: " + com.android.server.usb.UsbAlsaMidiDevice.this.mIsOpen + " mServerAvailable: " + com.android.server.usb.UsbAlsaMidiDevice.this.mServerAvailable);
                if (numOpenPorts > 0 && !com.android.server.usb.UsbAlsaMidiDevice.this.mIsOpen && com.android.server.usb.UsbAlsaMidiDevice.this.mServerAvailable) {
                    com.android.server.usb.UsbAlsaMidiDevice.this.openLocked();
                } else if (numOpenPorts == 0 && com.android.server.usb.UsbAlsaMidiDevice.this.mIsOpen) {
                    com.android.server.usb.UsbAlsaMidiDevice.this.closeLocked();
                }
            }
        }

        public void onClose() {
        }
    };

    private native void nativeClose(java.io.FileDescriptor[] fileDescriptorArr);

    private native java.io.FileDescriptor[] nativeOpen(int i, int i2, int i3, int i4);

    private final class InputReceiverProxy extends android.media.midi.MidiReceiver {
        private android.media.midi.MidiReceiver mReceiver;

        private InputReceiverProxy() {
        }

        @Override // android.media.midi.MidiReceiver
        public void onSend(byte[] msg, int offset, int count, long timestamp) throws java.io.IOException {
            android.media.midi.MidiReceiver receiver = this.mReceiver;
            if (receiver != null) {
                receiver.send(msg, offset, count, timestamp);
            }
        }

        public void setReceiver(android.media.midi.MidiReceiver receiver) {
            this.mReceiver = receiver;
        }

        @Override // android.media.midi.MidiReceiver
        public void onFlush() throws java.io.IOException {
            android.media.midi.MidiReceiver receiver = this.mReceiver;
            if (receiver != null) {
                receiver.flush();
            }
        }
    }

    public static com.android.server.usb.UsbAlsaMidiDevice create(android.content.Context context, android.os.Bundle properties, int card, int device, int numInputs, int numOutputs) {
        com.android.server.usb.UsbAlsaMidiDevice midiDevice = new com.android.server.usb.UsbAlsaMidiDevice(card, device, numInputs, numOutputs);
        if (!midiDevice.register(context, properties)) {
            libcore.io.IoUtils.closeQuietly(midiDevice);
            android.util.Log.e(TAG, "createDeviceServer failed");
            return null;
        }
        return midiDevice;
    }

    private UsbAlsaMidiDevice(int card, int device, int numInputs, int numOutputs) {
        this.mPowerBoostSetter = null;
        this.mAlsaCard = card;
        this.mAlsaDevice = device;
        this.mNumInputs = numInputs;
        this.mNumOutputs = numOutputs;
        this.mMidiInputPortReceivers = new com.android.server.usb.UsbAlsaMidiDevice.InputReceiverProxy[numOutputs];
        for (int port = 0; port < numOutputs; port++) {
            this.mMidiInputPortReceivers[port] = new com.android.server.usb.UsbAlsaMidiDevice.InputReceiverProxy();
        }
        this.mPowerBoostSetter = new com.android.server.usb.PowerBoostSetter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r14v0, types: [com.android.server.usb.UsbAlsaMidiDevice$3] */
    /* JADX WARN: Type inference failed for: r5v4, types: [com.android.server.usb.UsbAlsaMidiDevice$2] */
    public boolean openLocked() {
        int inputStreamCount = this.mNumInputs;
        if (inputStreamCount > 0) {
            inputStreamCount++;
        }
        int outputStreamCount = this.mNumOutputs;
        java.io.FileDescriptor[] fileDescriptors = nativeOpen(this.mAlsaCard, this.mAlsaDevice, inputStreamCount, outputStreamCount);
        if (fileDescriptors == null) {
            android.util.Log.e(TAG, "nativeOpen failed");
            return false;
        }
        this.mFileDescriptors = fileDescriptors;
        this.mPollFDs = new android.system.StructPollfd[inputStreamCount];
        this.mInputStreams = new java.io.FileInputStream[inputStreamCount];
        for (int i = 0; i < inputStreamCount; i++) {
            java.io.FileDescriptor fd = fileDescriptors[i];
            android.system.StructPollfd pollfd = new android.system.StructPollfd();
            pollfd.fd = fd;
            pollfd.events = (short) android.system.OsConstants.POLLIN;
            this.mPollFDs[i] = pollfd;
            this.mInputStreams[i] = new java.io.FileInputStream(fd);
        }
        this.mOutputStreams = new java.io.FileOutputStream[outputStreamCount];
        this.mEventSchedulers = new com.android.internal.midi.MidiEventScheduler[outputStreamCount];
        for (int i2 = 0; i2 < outputStreamCount; i2++) {
            this.mOutputStreams[i2] = new java.io.FileOutputStream(fileDescriptors[inputStreamCount + i2]);
            com.android.internal.midi.MidiEventScheduler scheduler = new com.android.internal.midi.MidiEventScheduler();
            this.mEventSchedulers[i2] = scheduler;
            this.mMidiInputPortReceivers[i2].setReceiver(scheduler.getReceiver());
        }
        final android.media.midi.MidiReceiver[] outputReceivers = this.mServer.getOutputPortReceivers();
        if (inputStreamCount > 0) {
            new java.lang.Thread("UsbAlsaMidiDevice input thread") { // from class: com.android.server.usb.UsbAlsaMidiDevice.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    long timestamp;
                    byte[] buffer = new byte[512];
                    while (true) {
                        try {
                            timestamp = java.lang.System.nanoTime();
                        } catch (android.system.ErrnoException e) {
                            android.util.Log.d(com.android.server.usb.UsbAlsaMidiDevice.TAG, "reader thread exiting");
                        } catch (java.io.IOException e2) {
                            android.util.Log.d(com.android.server.usb.UsbAlsaMidiDevice.TAG, "reader thread exiting");
                        }
                        synchronized (com.android.server.usb.UsbAlsaMidiDevice.this.mLock) {
                            if (!com.android.server.usb.UsbAlsaMidiDevice.this.mIsOpen) {
                                break;
                            }
                            for (int index = 0; index < com.android.server.usb.UsbAlsaMidiDevice.this.mPollFDs.length; index++) {
                                android.system.StructPollfd pfd = com.android.server.usb.UsbAlsaMidiDevice.this.mPollFDs[index];
                                if ((pfd.revents & (android.system.OsConstants.POLLERR | android.system.OsConstants.POLLHUP)) != 0) {
                                    break;
                                }
                                if ((pfd.revents & android.system.OsConstants.POLLIN) != 0) {
                                    pfd.revents = (short) 0;
                                    if (index == com.android.server.usb.UsbAlsaMidiDevice.this.mInputStreams.length - 1) {
                                        break;
                                    }
                                    int count = com.android.server.usb.UsbAlsaMidiDevice.this.mInputStreams[index].read(buffer);
                                    outputReceivers[index].send(buffer, 0, count, timestamp);
                                    if (com.android.server.usb.UsbAlsaMidiDevice.this.mPowerBoostSetter != null && count > 1) {
                                        com.android.server.usb.UsbAlsaMidiDevice.this.mPowerBoostSetter.boostPower();
                                    }
                                }
                            }
                            android.util.Log.d(com.android.server.usb.UsbAlsaMidiDevice.TAG, "reader thread exiting");
                            android.util.Log.d(com.android.server.usb.UsbAlsaMidiDevice.TAG, "input thread exit");
                        }
                        android.system.Os.poll(com.android.server.usb.UsbAlsaMidiDevice.this.mPollFDs, -1);
                    }
                    android.util.Log.d(com.android.server.usb.UsbAlsaMidiDevice.TAG, "input thread exit");
                }
            }.start();
        }
        for (int port = 0; port < outputStreamCount; port++) {
            final com.android.internal.midi.MidiEventScheduler eventSchedulerF = this.mEventSchedulers[port];
            final java.io.FileOutputStream outputStreamF = this.mOutputStreams[port];
            final int portF = port;
            new java.lang.Thread("UsbAlsaMidiDevice output thread " + port) { // from class: com.android.server.usb.UsbAlsaMidiDevice.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    com.android.internal.midi.MidiEventScheduler.MidiEvent event;
                    while (true) {
                        try {
                            event = eventSchedulerF.waitNextEvent();
                        } catch (java.lang.InterruptedException e) {
                        }
                        if (event == null) {
                            android.util.Log.d(com.android.server.usb.UsbAlsaMidiDevice.TAG, "output thread exit");
                            return;
                        } else {
                            try {
                                outputStreamF.write(event.data, 0, event.count);
                            } catch (java.io.IOException e2) {
                                android.util.Log.e(com.android.server.usb.UsbAlsaMidiDevice.TAG, "write failed for port " + portF);
                            }
                            eventSchedulerF.addEventToPool(event);
                        }
                    }
                }
            }.start();
        }
        this.mIsOpen = true;
        return true;
    }

    private boolean register(android.content.Context context, android.os.Bundle properties) {
        android.media.midi.MidiManager midiManager = (android.media.midi.MidiManager) context.getSystemService(android.media.midi.MidiManager.class);
        if (midiManager == null) {
            android.util.Log.e(TAG, "No MidiManager in UsbAlsaMidiDevice.register()");
            return false;
        }
        this.mServerAvailable = true;
        this.mServer = midiManager.createDeviceServer(this.mMidiInputPortReceivers, this.mNumInputs, null, null, properties, 1, -1, this.mCallback);
        if (this.mServer == null) {
            return false;
        }
        return true;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws java.io.IOException {
        synchronized (this.mLock) {
            if (this.mIsOpen) {
                closeLocked();
            }
            this.mServerAvailable = false;
        }
        if (this.mServer != null) {
            libcore.io.IoUtils.closeQuietly(this.mServer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeLocked() {
        for (int i = 0; i < this.mEventSchedulers.length; i++) {
            this.mMidiInputPortReceivers[i].setReceiver(null);
            this.mEventSchedulers[i].close();
        }
        this.mEventSchedulers = null;
        for (int i2 = 0; i2 < this.mInputStreams.length; i2++) {
            libcore.io.IoUtils.closeQuietly(this.mInputStreams[i2]);
        }
        this.mInputStreams = null;
        for (int i3 = 0; i3 < this.mOutputStreams.length; i3++) {
            libcore.io.IoUtils.closeQuietly(this.mOutputStreams[i3]);
        }
        this.mOutputStreams = null;
        nativeClose(this.mFileDescriptors);
        this.mFileDescriptors = null;
        this.mIsOpen = false;
    }

    public void dump(java.lang.String deviceAddr, com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("device_address", 1138166333443L, deviceAddr);
        dump.write("card", 1120986464257L, this.mAlsaCard);
        dump.write("device", 1120986464258L, this.mAlsaDevice);
        dump.end(token);
    }
}
