package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDirectMidiDevice implements java.io.Closeable {
    private static final int BULK_TRANSFER_NUMBER_OF_RETRIES = 20;
    private static final int BULK_TRANSFER_TIMEOUT_MILLISECONDS = 50;
    private static final boolean DEBUG = true;
    private static final byte MESSAGE_TYPE_MIDI_1_CHANNEL_VOICE = 2;
    private static final byte MESSAGE_TYPE_MIDI_2_CHANNEL_VOICE = 4;
    private static final java.lang.String TAG = "UsbDirectMidiDevice";
    private static final int THREAD_JOIN_TIMEOUT_MILLISECONDS = 200;
    private android.content.Context mContext;
    private java.util.ArrayList<java.util.ArrayList<java.lang.Integer>> mInputUsbEndpointCableCounts;
    private java.util.ArrayList<java.util.ArrayList<android.hardware.usb.UsbEndpoint>> mInputUsbEndpoints;
    private boolean mIsOpen;
    private final boolean mIsUniversalMidiDevice;
    private java.util.ArrayList<java.util.ArrayList<com.android.internal.midi.MidiEventMultiScheduler>> mMidiEventMultiSchedulers;
    private final com.android.server.usb.UsbDirectMidiDevice.InputReceiverProxy[] mMidiInputPortReceivers;
    private java.lang.String mName;
    private final int mNumInputs;
    private final int mNumOutputs;
    private java.util.ArrayList<java.util.ArrayList<java.lang.Integer>> mOutputUsbEndpointCableCounts;
    private java.util.ArrayList<java.util.ArrayList<android.hardware.usb.UsbEndpoint>> mOutputUsbEndpoints;
    private com.android.server.usb.descriptors.UsbDescriptorParser mParser;
    private com.android.server.usb.PowerBoostSetter mPowerBoostSetter;
    private android.media.midi.MidiDeviceServer mServer;
    private boolean mServerAvailable;
    private final boolean mShouldCallSetInterface;
    private java.util.ArrayList<java.lang.Thread> mThreads;
    private final java.lang.String mUniqueUsbDeviceIdentifier;
    private android.hardware.usb.UsbDevice mUsbDevice;
    private java.util.ArrayList<android.hardware.usb.UsbDeviceConnection> mUsbDeviceConnections;
    private java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> mUsbInterfaces;
    private com.android.server.usb.descriptors.UsbMidiBlockParser mMidiBlockParser = new com.android.server.usb.descriptors.UsbMidiBlockParser();
    private int mDefaultMidiProtocol = 1;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.media.midi.MidiDeviceServer.Callback mCallback = new android.media.midi.MidiDeviceServer.Callback() { // from class: com.android.server.usb.UsbDirectMidiDevice.1
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
            synchronized (com.android.server.usb.UsbDirectMidiDevice.this.mLock) {
                android.util.Log.d(com.android.server.usb.UsbDirectMidiDevice.TAG, "numOpenPorts: " + numOpenPorts + " isOpen: " + com.android.server.usb.UsbDirectMidiDevice.this.mIsOpen + " mServerAvailable: " + com.android.server.usb.UsbDirectMidiDevice.this.mServerAvailable);
                if (numOpenPorts > 0 && !com.android.server.usb.UsbDirectMidiDevice.this.mIsOpen && com.android.server.usb.UsbDirectMidiDevice.this.mServerAvailable) {
                    com.android.server.usb.UsbDirectMidiDevice.this.openLocked();
                } else if (numOpenPorts == 0 && com.android.server.usb.UsbDirectMidiDevice.this.mIsOpen) {
                    com.android.server.usb.UsbDirectMidiDevice.this.closeLocked();
                }
            }
        }

        public void onClose() {
        }
    };

    private static final class InputReceiverProxy extends android.media.midi.MidiReceiver {
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

    public static com.android.server.usb.UsbDirectMidiDevice create(android.content.Context context, android.hardware.usb.UsbDevice usbDevice, com.android.server.usb.descriptors.UsbDescriptorParser parser, boolean isUniversalMidiDevice, java.lang.String uniqueUsbDeviceIdentifier) {
        com.android.server.usb.UsbDirectMidiDevice midiDevice = new com.android.server.usb.UsbDirectMidiDevice(usbDevice, parser, isUniversalMidiDevice, uniqueUsbDeviceIdentifier);
        if (!midiDevice.register(context)) {
            libcore.io.IoUtils.closeQuietly(midiDevice);
            android.util.Log.e(TAG, "createDeviceServer failed");
            return null;
        }
        return midiDevice;
    }

    private UsbDirectMidiDevice(android.hardware.usb.UsbDevice usbDevice, com.android.server.usb.descriptors.UsbDescriptorParser parser, boolean isUniversalMidiDevice, java.lang.String uniqueUsbDeviceIdentifier) {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbInterfaceDescriptor> midiInterfaceDescriptors;
        this.mPowerBoostSetter = null;
        this.mUsbDevice = usbDevice;
        this.mParser = parser;
        this.mUniqueUsbDeviceIdentifier = uniqueUsbDeviceIdentifier;
        this.mIsUniversalMidiDevice = isUniversalMidiDevice;
        this.mShouldCallSetInterface = parser.calculateMidiInterfaceDescriptorsCount() > 1;
        if (isUniversalMidiDevice) {
            midiInterfaceDescriptors = parser.findUniversalMidiInterfaceDescriptors();
        } else {
            midiInterfaceDescriptors = parser.findLegacyMidiInterfaceDescriptors();
        }
        this.mUsbInterfaces = new java.util.ArrayList<>();
        if (this.mUsbDevice.getConfigurationCount() > 0) {
            android.hardware.usb.UsbConfiguration usbConfiguration = this.mUsbDevice.getConfiguration(0);
            for (int interfaceIndex = 0; interfaceIndex < usbConfiguration.getInterfaceCount(); interfaceIndex++) {
                android.hardware.usb.UsbInterface usbInterface = usbConfiguration.getInterface(interfaceIndex);
                java.util.Iterator<com.android.server.usb.descriptors.UsbInterfaceDescriptor> it = midiInterfaceDescriptors.iterator();
                while (true) {
                    if (it.hasNext()) {
                        com.android.server.usb.descriptors.UsbInterfaceDescriptor midiInterfaceDescriptor = it.next();
                        android.hardware.usb.UsbInterface midiInterface = midiInterfaceDescriptor.toAndroid(this.mParser);
                        if (areEquivalent(usbInterface, midiInterface)) {
                            this.mUsbInterfaces.add(midiInterfaceDescriptor);
                            break;
                        }
                    }
                }
            }
            if (this.mUsbDevice.getConfigurationCount() > 1) {
                android.util.Log.w(TAG, "Skipping some USB configurations. Count: " + this.mUsbDevice.getConfigurationCount());
            }
        }
        int numInputs = 0;
        int numOutputs = 0;
        for (int interfaceIndex2 = 0; interfaceIndex2 < this.mUsbInterfaces.size(); interfaceIndex2++) {
            com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor = this.mUsbInterfaces.get(interfaceIndex2);
            for (int endpointIndex = 0; endpointIndex < interfaceDescriptor.getNumEndpoints(); endpointIndex++) {
                com.android.server.usb.descriptors.UsbEndpointDescriptor endpoint = interfaceDescriptor.getEndpointDescriptor(endpointIndex);
                if (endpoint.getDirection() == 0) {
                    numOutputs += getNumJacks(endpoint);
                } else {
                    numInputs += getNumJacks(endpoint);
                }
            }
        }
        this.mNumInputs = numInputs;
        this.mNumOutputs = numOutputs;
        android.util.Log.d(TAG, "Created UsbDirectMidiDevice with " + numInputs + " inputs and " + numOutputs + " outputs. isUniversalMidiDevice: " + isUniversalMidiDevice);
        this.mMidiInputPortReceivers = new com.android.server.usb.UsbDirectMidiDevice.InputReceiverProxy[numOutputs];
        for (int port = 0; port < numOutputs; port++) {
            this.mMidiInputPortReceivers[port] = new com.android.server.usb.UsbDirectMidiDevice.InputReceiverProxy();
        }
        this.mPowerBoostSetter = new com.android.server.usb.PowerBoostSetter();
    }

    private int calculateDefaultMidiProtocol() {
        android.hardware.usb.UsbManager manager = (android.hardware.usb.UsbManager) this.mContext.getSystemService(android.hardware.usb.UsbManager.class);
        for (int interfaceIndex = 0; interfaceIndex < this.mUsbInterfaces.size(); interfaceIndex++) {
            com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor = this.mUsbInterfaces.get(interfaceIndex);
            boolean doesInterfaceContainInput = false;
            boolean doesInterfaceContainOutput = false;
            for (int endpointIndex = 0; endpointIndex < interfaceDescriptor.getNumEndpoints() && (!doesInterfaceContainInput || !doesInterfaceContainOutput); endpointIndex++) {
                com.android.server.usb.descriptors.UsbEndpointDescriptor endpoint = interfaceDescriptor.getEndpointDescriptor(endpointIndex);
                if (endpoint.getDirection() == 0) {
                    doesInterfaceContainOutput = true;
                } else {
                    doesInterfaceContainInput = true;
                }
            }
            if (doesInterfaceContainInput && doesInterfaceContainOutput) {
                android.hardware.usb.UsbDeviceConnection connection = manager.openDevice(this.mUsbDevice);
                android.hardware.usb.UsbInterface usbInterface = interfaceDescriptor.toAndroid(this.mParser);
                if (updateUsbInterface(usbInterface, connection)) {
                    int defaultMidiProtocol = this.mMidiBlockParser.calculateMidiType(connection, interfaceDescriptor.getInterfaceNumber(), interfaceDescriptor.getAlternateSetting());
                    connection.close();
                    return defaultMidiProtocol;
                }
            }
        }
        android.util.Log.w(TAG, "Cannot find interface with both input and output endpoints");
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean openLocked() {
        android.util.Log.d(TAG, "openLocked()");
        android.hardware.usb.UsbManager manager = (android.hardware.usb.UsbManager) this.mContext.getSystemService(android.hardware.usb.UsbManager.class);
        this.mUsbDeviceConnections = new java.util.ArrayList<>();
        this.mInputUsbEndpoints = new java.util.ArrayList<>();
        this.mOutputUsbEndpoints = new java.util.ArrayList<>();
        this.mInputUsbEndpointCableCounts = new java.util.ArrayList<>();
        this.mOutputUsbEndpointCableCounts = new java.util.ArrayList<>();
        this.mMidiEventMultiSchedulers = new java.util.ArrayList<>();
        this.mThreads = new java.util.ArrayList<>();
        for (int interfaceIndex = 0; interfaceIndex < this.mUsbInterfaces.size(); interfaceIndex++) {
            java.util.ArrayList<android.hardware.usb.UsbEndpoint> inputEndpoints = new java.util.ArrayList<>();
            java.util.ArrayList<android.hardware.usb.UsbEndpoint> outputEndpoints = new java.util.ArrayList<>();
            java.util.ArrayList<java.lang.Integer> inputEndpointCableCounts = new java.util.ArrayList<>();
            java.util.ArrayList<java.lang.Integer> outputEndpointCableCounts = new java.util.ArrayList<>();
            java.util.ArrayList<com.android.internal.midi.MidiEventMultiScheduler> midiEventMultiSchedulers = new java.util.ArrayList<>();
            com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor = this.mUsbInterfaces.get(interfaceIndex);
            for (int endpointIndex = 0; endpointIndex < interfaceDescriptor.getNumEndpoints(); endpointIndex++) {
                com.android.server.usb.descriptors.UsbEndpointDescriptor endpoint = interfaceDescriptor.getEndpointDescriptor(endpointIndex);
                if (endpoint.getDirection() == 0) {
                    outputEndpoints.add(endpoint.toAndroid(this.mParser));
                    outputEndpointCableCounts.add(java.lang.Integer.valueOf(getNumJacks(endpoint)));
                    com.android.internal.midi.MidiEventMultiScheduler scheduler = new com.android.internal.midi.MidiEventMultiScheduler(getNumJacks(endpoint));
                    midiEventMultiSchedulers.add(scheduler);
                } else {
                    inputEndpoints.add(endpoint.toAndroid(this.mParser));
                    inputEndpointCableCounts.add(java.lang.Integer.valueOf(getNumJacks(endpoint)));
                }
            }
            if (!outputEndpoints.isEmpty() || !inputEndpoints.isEmpty()) {
                android.hardware.usb.UsbDeviceConnection connection = manager.openDevice(this.mUsbDevice);
                android.hardware.usb.UsbInterface usbInterface = interfaceDescriptor.toAndroid(this.mParser);
                if (updateUsbInterface(usbInterface, connection)) {
                    this.mUsbDeviceConnections.add(connection);
                    this.mInputUsbEndpoints.add(inputEndpoints);
                    this.mOutputUsbEndpoints.add(outputEndpoints);
                    this.mInputUsbEndpointCableCounts.add(inputEndpointCableCounts);
                    this.mOutputUsbEndpointCableCounts.add(outputEndpointCableCounts);
                    this.mMidiEventMultiSchedulers.add(midiEventMultiSchedulers);
                }
            }
        }
        int outputIndex = 0;
        for (int connectionIndex = 0; connectionIndex < this.mMidiEventMultiSchedulers.size(); connectionIndex++) {
            for (int endpointIndex2 = 0; endpointIndex2 < this.mMidiEventMultiSchedulers.get(connectionIndex).size(); endpointIndex2++) {
                int cableCount = this.mOutputUsbEndpointCableCounts.get(connectionIndex).get(endpointIndex2).intValue();
                com.android.internal.midi.MidiEventMultiScheduler multiScheduler = this.mMidiEventMultiSchedulers.get(connectionIndex).get(endpointIndex2);
                for (int cableNumber = 0; cableNumber < cableCount; cableNumber++) {
                    com.android.internal.midi.MidiEventScheduler scheduler2 = multiScheduler.getEventScheduler(cableNumber);
                    this.mMidiInputPortReceivers[outputIndex].setReceiver(scheduler2.getReceiver());
                    outputIndex++;
                }
            }
        }
        final android.media.midi.MidiReceiver[] outputReceivers = this.mServer.getOutputPortReceivers();
        int portStartNumber = 0;
        int connectionIndex2 = 0;
        while (connectionIndex2 < this.mInputUsbEndpoints.size()) {
            int portStartNumber2 = portStartNumber;
            for (int endpointIndex3 = 0; endpointIndex3 < this.mInputUsbEndpoints.get(connectionIndex2).size(); endpointIndex3++) {
                final android.hardware.usb.UsbDeviceConnection connectionFinal = this.mUsbDeviceConnections.get(connectionIndex2);
                final android.hardware.usb.UsbEndpoint endpointFinal = this.mInputUsbEndpoints.get(connectionIndex2).get(endpointIndex3);
                final int portStartFinal = portStartNumber2;
                final int cableCountFinal = this.mInputUsbEndpointCableCounts.get(connectionIndex2).get(endpointIndex3).intValue();
                java.lang.Thread newThread = new java.lang.Thread("UsbDirectMidiDevice input thread " + portStartFinal) { // from class: com.android.server.usb.UsbDirectMidiDevice.2
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        int cableNumber2;
                        android.hardware.usb.UsbRequest request = new android.hardware.usb.UsbRequest();
                        com.android.server.usb.UsbMidiPacketConverter packetConverter = new com.android.server.usb.UsbMidiPacketConverter();
                        packetConverter.createDecoders(cableCountFinal);
                        try {
                            try {
                                request.initialize(connectionFinal, endpointFinal);
                                byte[] inputBuffer = new byte[endpointFinal.getMaxPacketSize()];
                                boolean keepGoing = true;
                                while (true) {
                                    if (!keepGoing) {
                                        break;
                                    }
                                    java.lang.Thread.currentThread();
                                    if (java.lang.Thread.interrupted()) {
                                        android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "input thread interrupted");
                                        break;
                                    }
                                    java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.wrap(inputBuffer);
                                    if (!request.queue(byteBuffer)) {
                                        android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "Cannot queue request");
                                        break;
                                    }
                                    android.hardware.usb.UsbRequest response = connectionFinal.requestWait();
                                    if (response == null) {
                                        android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "Response is null");
                                        break;
                                    }
                                    if (request != response) {
                                        android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "Skipping response");
                                    } else {
                                        long timestamp = java.lang.System.nanoTime();
                                        int bytesRead = byteBuffer.position();
                                        if (bytesRead > 0) {
                                            int i = 0;
                                            com.android.server.usb.UsbDirectMidiDevice.logByteArray("Input before conversion ", inputBuffer, 0, bytesRead);
                                            if (!com.android.server.usb.UsbDirectMidiDevice.this.mIsUniversalMidiDevice) {
                                                packetConverter.decodeMidiPackets(inputBuffer, bytesRead);
                                            }
                                            int cableNumber3 = 0;
                                            while (cableNumber3 < cableCountFinal) {
                                                byte[] convertedArray = com.android.server.usb.UsbDirectMidiDevice.this.mIsUniversalMidiDevice ? com.android.server.usb.UsbDirectMidiDevice.this.swapEndiannessPerWord(inputBuffer, bytesRead) : packetConverter.pullDecodedMidiPackets(cableNumber3);
                                                com.android.server.usb.UsbDirectMidiDevice.logByteArray("Input " + cableNumber3 + " after conversion ", convertedArray, i, convertedArray.length);
                                                if (convertedArray.length != 0) {
                                                    if (outputReceivers != null && outputReceivers[portStartFinal + cableNumber3] != null) {
                                                        byte[] convertedArray2 = convertedArray;
                                                        cableNumber2 = cableNumber3;
                                                        outputReceivers[portStartFinal + cableNumber3].send(convertedArray, 0, convertedArray.length, timestamp);
                                                        if (com.android.server.usb.UsbDirectMidiDevice.this.mPowerBoostSetter != null && convertedArray2.length > 1 && (!com.android.server.usb.UsbDirectMidiDevice.this.mIsUniversalMidiDevice || com.android.server.usb.UsbDirectMidiDevice.this.isChannelVoiceMessage(convertedArray2))) {
                                                            com.android.server.usb.UsbDirectMidiDevice.this.mPowerBoostSetter.boostPower();
                                                        }
                                                    }
                                                    android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "outputReceivers is null");
                                                    keepGoing = false;
                                                    break;
                                                }
                                                cableNumber2 = cableNumber3;
                                                cableNumber3 = cableNumber2 + 1;
                                                i = 0;
                                            }
                                        }
                                    }
                                }
                            } catch (java.io.IOException e) {
                                android.util.Log.d(com.android.server.usb.UsbDirectMidiDevice.TAG, "reader thread exiting");
                            } catch (java.lang.NullPointerException e2) {
                                android.util.Log.e(com.android.server.usb.UsbDirectMidiDevice.TAG, "input thread: ", e2);
                            }
                            request.close();
                            android.util.Log.d(com.android.server.usb.UsbDirectMidiDevice.TAG, "input thread exit");
                        } catch (java.lang.Throwable th) {
                            request.close();
                            throw th;
                        }
                    }
                };
                newThread.start();
                this.mThreads.add(newThread);
                portStartNumber2 += cableCountFinal;
            }
            connectionIndex2++;
            portStartNumber = portStartNumber2;
        }
        int portStartNumber3 = 0;
        int connectionIndex3 = 0;
        while (connectionIndex3 < this.mOutputUsbEndpoints.size()) {
            int portStartNumber4 = portStartNumber3;
            for (int endpointIndex4 = 0; endpointIndex4 < this.mOutputUsbEndpoints.get(connectionIndex3).size(); endpointIndex4++) {
                final android.hardware.usb.UsbDeviceConnection connectionFinal2 = this.mUsbDeviceConnections.get(connectionIndex3);
                final android.hardware.usb.UsbEndpoint endpointFinal2 = this.mOutputUsbEndpoints.get(connectionIndex3).get(endpointIndex4);
                final int cableCountFinal2 = this.mOutputUsbEndpointCableCounts.get(connectionIndex3).get(endpointIndex4).intValue();
                final com.android.internal.midi.MidiEventMultiScheduler multiSchedulerFinal = this.mMidiEventMultiSchedulers.get(connectionIndex3).get(endpointIndex4);
                java.lang.Thread newThread2 = new java.lang.Thread("UsbDirectMidiDevice output write thread " + portStartNumber4) { // from class: com.android.server.usb.UsbDirectMidiDevice.3
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        byte[] convertedArray;
                        java.io.ByteArrayOutputStream midi2ByteStream;
                        try {
                            java.io.ByteArrayOutputStream midi2ByteStream2 = new java.io.ByteArrayOutputStream();
                            com.android.server.usb.UsbMidiPacketConverter packetConverter = new com.android.server.usb.UsbMidiPacketConverter();
                            packetConverter.createEncoders(cableCountFinal2);
                            boolean isInterrupted = false;
                            while (true) {
                                if (isInterrupted) {
                                    break;
                                }
                                boolean wasSuccessful = multiSchedulerFinal.waitNextEvent();
                                if (!wasSuccessful) {
                                    android.util.Log.d(com.android.server.usb.UsbDirectMidiDevice.TAG, "output thread closed");
                                    break;
                                }
                                long now = java.lang.System.nanoTime();
                                for (int cableNumber2 = 0; cableNumber2 < cableCountFinal2; cableNumber2++) {
                                    com.android.internal.midi.MidiEventScheduler eventScheduler = multiSchedulerFinal.getEventScheduler(cableNumber2);
                                    for (com.android.internal.midi.MidiEventScheduler.MidiEvent event = eventScheduler.getNextEvent(now); event != null; event = (com.android.internal.midi.MidiEventScheduler.MidiEvent) eventScheduler.getNextEvent(now)) {
                                        com.android.server.usb.UsbDirectMidiDevice.logByteArray("Output before conversion ", event.data, 0, event.count);
                                        if (com.android.server.usb.UsbDirectMidiDevice.this.mIsUniversalMidiDevice) {
                                            byte[] convertedArray2 = com.android.server.usb.UsbDirectMidiDevice.this.swapEndiannessPerWord(event.data, event.count);
                                            midi2ByteStream2.write(convertedArray2, 0, convertedArray2.length);
                                        } else {
                                            packetConverter.encodeMidiPackets(event.data, event.count, cableNumber2);
                                        }
                                        eventScheduler.addEventToPool(event);
                                    }
                                }
                                java.lang.Thread.currentThread();
                                if (java.lang.Thread.interrupted()) {
                                    android.util.Log.d(com.android.server.usb.UsbDirectMidiDevice.TAG, "output thread interrupted");
                                    break;
                                }
                                byte[] bArr = new byte[0];
                                if (com.android.server.usb.UsbDirectMidiDevice.this.mIsUniversalMidiDevice) {
                                    convertedArray = midi2ByteStream2.toByteArray();
                                    midi2ByteStream2.reset();
                                } else {
                                    convertedArray = packetConverter.pullEncodedMidiPackets();
                                }
                                com.android.server.usb.UsbDirectMidiDevice.logByteArray("Output after conversion ", convertedArray, 0, convertedArray.length);
                                int curPacketStart = 0;
                                while (curPacketStart < convertedArray.length && !isInterrupted) {
                                    int transferResult = -1;
                                    int curPacketSize = java.lang.Math.min(endpointFinal2.getMaxPacketSize(), convertedArray.length - curPacketStart);
                                    int retryCount = 0;
                                    while (transferResult < 0 && retryCount <= 20) {
                                        midi2ByteStream = midi2ByteStream2;
                                        int retryCount2 = retryCount;
                                        int retryCount3 = curPacketStart;
                                        transferResult = connectionFinal2.bulkTransfer(endpointFinal2, convertedArray, retryCount3, curPacketSize, 50);
                                        retryCount = retryCount2 + 1;
                                        java.lang.Thread.currentThread();
                                        if (java.lang.Thread.interrupted()) {
                                            android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "output thread interrupted after send");
                                            isInterrupted = true;
                                            break;
                                        } else {
                                            if (transferResult < 0) {
                                                android.util.Log.d(com.android.server.usb.UsbDirectMidiDevice.TAG, "retrying packet. retryCount = " + retryCount + " result = " + transferResult);
                                                if (retryCount > 20) {
                                                    android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "Skipping packet because timeout");
                                                }
                                            }
                                            midi2ByteStream2 = midi2ByteStream;
                                        }
                                    }
                                    midi2ByteStream = midi2ByteStream2;
                                    curPacketStart += endpointFinal2.getMaxPacketSize();
                                    midi2ByteStream2 = midi2ByteStream;
                                }
                                midi2ByteStream2 = midi2ByteStream2;
                            }
                        } catch (java.lang.InterruptedException e) {
                            android.util.Log.w(com.android.server.usb.UsbDirectMidiDevice.TAG, "output thread: ", e);
                        } catch (java.lang.NullPointerException e2) {
                            android.util.Log.e(com.android.server.usb.UsbDirectMidiDevice.TAG, "output thread: ", e2);
                        }
                        android.util.Log.d(com.android.server.usb.UsbDirectMidiDevice.TAG, "output thread exit");
                    }
                };
                newThread2.start();
                this.mThreads.add(newThread2);
                portStartNumber4 += cableCountFinal2;
            }
            connectionIndex3++;
            portStartNumber3 = portStartNumber4;
        }
        this.mIsOpen = true;
        return true;
    }

    private boolean register(android.content.Context context) {
        java.lang.String name;
        java.lang.String name2;
        this.mContext = context;
        android.media.midi.MidiManager midiManager = (android.media.midi.MidiManager) context.getSystemService(android.media.midi.MidiManager.class);
        if (midiManager == null) {
            android.util.Log.e(TAG, "No MidiManager in UsbDirectMidiDevice.register()");
            return false;
        }
        if (this.mIsUniversalMidiDevice) {
            this.mDefaultMidiProtocol = calculateDefaultMidiProtocol();
        } else {
            this.mDefaultMidiProtocol = -1;
        }
        android.os.Bundle properties = new android.os.Bundle();
        java.lang.String manufacturer = this.mUsbDevice.getManufacturerName();
        java.lang.String product = this.mUsbDevice.getProductName();
        java.lang.String version = this.mUsbDevice.getVersion();
        if (manufacturer == null || manufacturer.isEmpty()) {
            name = product;
        } else if (product == null || product.isEmpty()) {
            name = manufacturer;
        } else {
            name = manufacturer + " " + product;
        }
        java.lang.String name3 = name + "#" + this.mUniqueUsbDeviceIdentifier;
        if (this.mIsUniversalMidiDevice) {
            name2 = name3 + " MIDI 2.0";
        } else {
            name2 = name3 + " MIDI 1.0";
        }
        this.mName = name2;
        properties.putString("name", name2);
        properties.putString("manufacturer", manufacturer);
        properties.putString("product", product);
        properties.putString("version", version);
        properties.putString("serial_number", this.mUsbDevice.getSerialNumber());
        properties.putParcelable("usb_device", this.mUsbDevice);
        this.mServerAvailable = true;
        this.mServer = midiManager.createDeviceServer(this.mMidiInputPortReceivers, this.mNumInputs, null, null, properties, 1, this.mDefaultMidiProtocol, this.mCallback);
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
        android.util.Log.d(TAG, "closeLocked()");
        for (java.lang.Thread thread : this.mThreads) {
            if (thread != null) {
                thread.interrupt();
            }
        }
        for (java.lang.Thread thread2 : this.mThreads) {
            if (thread2 != null) {
                try {
                    thread2.join(200L);
                } catch (java.lang.InterruptedException e) {
                    android.util.Log.w(TAG, "thread join interrupted");
                }
            }
        }
        this.mThreads = null;
        for (int i = 0; i < this.mMidiInputPortReceivers.length; i++) {
            this.mMidiInputPortReceivers[i].setReceiver(null);
        }
        for (int connectionIndex = 0; connectionIndex < this.mMidiEventMultiSchedulers.size(); connectionIndex++) {
            for (int endpointIndex = 0; endpointIndex < this.mMidiEventMultiSchedulers.get(connectionIndex).size(); endpointIndex++) {
                com.android.internal.midi.MidiEventMultiScheduler multiScheduler = this.mMidiEventMultiSchedulers.get(connectionIndex).get(endpointIndex);
                multiScheduler.close();
            }
        }
        this.mMidiEventMultiSchedulers = null;
        for (android.hardware.usb.UsbDeviceConnection connection : this.mUsbDeviceConnections) {
            connection.close();
        }
        this.mUsbDeviceConnections = null;
        this.mInputUsbEndpoints = null;
        this.mOutputUsbEndpoints = null;
        this.mInputUsbEndpointCableCounts = null;
        this.mOutputUsbEndpointCableCounts = null;
        this.mIsOpen = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] swapEndiannessPerWord(byte[] inputArray, int size) {
        int numberOfExcessBytes = size & 3;
        if (numberOfExcessBytes != 0) {
            android.util.Log.e(TAG, "size not multiple of 4: " + size);
        }
        byte[] outputArray = new byte[size - numberOfExcessBytes];
        for (int i = 0; i + 3 < size; i += 4) {
            outputArray[i] = inputArray[i + 3];
            outputArray[i + 1] = inputArray[i + 2];
            outputArray[i + 2] = inputArray[i + 1];
            outputArray[i + 3] = inputArray[i];
        }
        return outputArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logByteArray(java.lang.String prefix, byte[] value, int offset, int count) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(prefix);
        for (int i = offset; i < offset + count; i++) {
            builder.append(java.lang.String.format("0x%02X", java.lang.Byte.valueOf(value[i])));
            if (i != value.length - 1) {
                builder.append(", ");
            }
        }
        android.util.Log.d(TAG, builder.toString());
    }

    private boolean updateUsbInterface(android.hardware.usb.UsbInterface usbInterface, android.hardware.usb.UsbDeviceConnection connection) {
        if (usbInterface == null) {
            android.util.Log.e(TAG, "Usb Interface is null");
            return false;
        }
        if (connection == null) {
            android.util.Log.e(TAG, "UsbDeviceConnection is null");
            return false;
        }
        if (!connection.claimInterface(usbInterface, true)) {
            android.util.Log.e(TAG, "Can't claim interface");
            return false;
        }
        if (this.mShouldCallSetInterface) {
            if (!connection.setInterface(usbInterface)) {
                android.util.Log.w(TAG, "Can't set interface");
            }
        } else {
            android.util.Log.w(TAG, "no alternate interface");
        }
        return true;
    }

    private boolean areEquivalent(android.hardware.usb.UsbInterface interface1, android.hardware.usb.UsbInterface interface2) {
        if (interface1.getId() != interface2.getId() || interface1.getAlternateSetting() != interface2.getAlternateSetting() || interface1.getInterfaceClass() != interface2.getInterfaceClass() || interface1.getInterfaceSubclass() != interface2.getInterfaceSubclass() || interface1.getInterfaceProtocol() != interface2.getInterfaceProtocol() || interface1.getEndpointCount() != interface2.getEndpointCount()) {
            return false;
        }
        if (interface1.getName() == null) {
            if (interface2.getName() != null) {
                return false;
            }
        } else if (!interface1.getName().equals(interface2.getName())) {
            return false;
        }
        for (int i = 0; i < interface1.getEndpointCount(); i++) {
            android.hardware.usb.UsbEndpoint endpoint1 = interface1.getEndpoint(i);
            android.hardware.usb.UsbEndpoint endpoint2 = interface2.getEndpoint(i);
            if (endpoint1.getAddress() != endpoint2.getAddress() || endpoint1.getAttributes() != endpoint2.getAttributes() || endpoint1.getMaxPacketSize() != endpoint2.getMaxPacketSize() || endpoint1.getInterval() != endpoint2.getInterval()) {
                return false;
            }
        }
        return true;
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("num_inputs", 1120986464257L, this.mNumInputs);
        dump.write("num_outputs", 1120986464258L, this.mNumOutputs);
        dump.write("is_universal", 1133871366147L, this.mIsUniversalMidiDevice);
        dump.write("name", 1138166333444L, this.mName);
        if (this.mIsUniversalMidiDevice) {
            this.mMidiBlockParser.dump(dump, "block_parser", 1146756268037L);
        }
        dump.end(token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isChannelVoiceMessage(byte[] umpMessage) {
        byte messageType = (byte) ((umpMessage[0] >> 4) & 15);
        return messageType == 2 || messageType == 4;
    }

    private int getNumJacks(com.android.server.usb.descriptors.UsbEndpointDescriptor usbEndpointDescriptor) {
        com.android.server.usb.descriptors.UsbDescriptor classSpecificEndpointDescriptor = usbEndpointDescriptor.getClassSpecificEndpointDescriptor();
        if (classSpecificEndpointDescriptor != null && (classSpecificEndpointDescriptor instanceof com.android.server.usb.descriptors.UsbACMidi10Endpoint)) {
            com.android.server.usb.descriptors.UsbACMidi10Endpoint midiEndpoint = (com.android.server.usb.descriptors.UsbACMidi10Endpoint) classSpecificEndpointDescriptor;
            return midiEndpoint.getNumJacks();
        }
        return 1;
    }
}
