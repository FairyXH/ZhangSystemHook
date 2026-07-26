package com.android.server.midi;

/* JADX INFO: loaded from: classes2.dex */
public class MidiService extends android.media.midi.IMidiManager.Stub {
    private static final int MAX_CONNECTIONS_PER_CLIENT = 64;
    private static final int MAX_DEVICE_SERVERS_PER_UID = 16;
    private static final int MAX_LISTENERS_PER_CLIENT = 16;
    private static final java.lang.String MIDI_LEGACY_STRING = "MIDI 1.0";
    private static final java.lang.String MIDI_UNIVERSAL_STRING = "MIDI 2.0";
    private static final java.lang.String TAG = "MidiService";
    private int mBluetoothServiceUid;
    private final android.content.Context mContext;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.os.UserManager mUserManager;
    private static final java.util.UUID MIDI_SERVICE = java.util.UUID.fromString("03B80E5A-EDE8-4B33-A751-6CE34EC4C700");
    private static final android.media.midi.MidiDeviceInfo[] EMPTY_DEVICE_INFO_ARRAY = new android.media.midi.MidiDeviceInfo[0];
    private static final java.lang.String[] EMPTY_STRING_ARRAY = new java.lang.String[0];
    private final java.util.HashMap<android.os.IBinder, com.android.server.midi.MidiService.Client> mClients = new java.util.HashMap<>();
    private final java.util.HashMap<android.media.midi.MidiDeviceInfo, com.android.server.midi.MidiService.Device> mDevicesByInfo = new java.util.HashMap<>();
    private final java.util.HashMap<android.bluetooth.BluetoothDevice, com.android.server.midi.MidiService.Device> mBluetoothDevices = new java.util.HashMap<>();
    private final java.util.HashMap<android.bluetooth.BluetoothDevice, android.media.midi.MidiDevice> mBleMidiDeviceMap = new java.util.HashMap<>();
    private final java.util.HashMap<android.os.IBinder, com.android.server.midi.MidiService.Device> mDevicesByServer = new java.util.HashMap<>();
    private int mNextDeviceId = 1;
    private final java.lang.Object mUsbMidiLock = new java.lang.Object();
    private final java.util.HashMap<java.lang.String, java.lang.Integer> mUsbMidiLegacyDeviceOpenCount = new java.util.HashMap<>();
    private final java.util.HashSet<java.lang.String> mUsbMidiUniversalDeviceInUse = new java.util.HashSet<>();
    private final java.util.HashSet<android.os.ParcelUuid> mNonMidiUUIDs = new java.util.HashSet<>();
    private final com.android.internal.content.PackageMonitor mPackageMonitor = new com.android.internal.content.PackageMonitor() { // from class: com.android.server.midi.MidiService.1
        public void onPackageAdded(java.lang.String packageName, int uid) throws java.lang.Throwable {
            com.android.server.midi.MidiService.this.addPackageDeviceServers(packageName, getChangingUserId());
        }

        public void onPackageModified(java.lang.String packageName) throws java.lang.Throwable {
            com.android.server.midi.MidiService.this.removePackageDeviceServers(packageName, getChangingUserId());
            com.android.server.midi.MidiService.this.addPackageDeviceServers(packageName, getChangingUserId());
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            com.android.server.midi.MidiService.this.removePackageDeviceServers(packageName, getChangingUserId());
        }
    };
    private final android.content.BroadcastReceiver mBleMidiReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.midi.MidiService.2
        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:21:0x003e  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r5, android.content.Intent r6) {
            /*
                Method dump skipped, instruction units count: 228
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.midi.MidiService.AnonymousClass2.onReceive(android.content.Context, android.content.Intent):void");
        }
    };

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.midi.MidiService mMidiService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mMidiService = new com.android.server.midi.MidiService(getContext());
            publishBinderService("midi", this.mMidiService);
        }

        @Override // com.android.server.SystemService
        public void onUserStarting(com.android.server.SystemService.TargetUser user) throws java.lang.Throwable {
            this.mMidiService.onStartOrUnlockUser(user, false);
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) throws java.lang.Throwable {
            this.mMidiService.onStartOrUnlockUser(user, true);
        }
    }

    private final class Client implements android.os.IBinder.DeathRecipient {
        private static final java.lang.String TAG = "MidiService.Client";
        private final android.os.IBinder mToken;
        private final java.util.HashMap<android.os.IBinder, android.media.midi.IMidiDeviceListener> mListeners = new java.util.HashMap<>();
        private final java.util.HashMap<android.os.IBinder, com.android.server.midi.MidiService.DeviceConnection> mDeviceConnections = new java.util.HashMap<>();
        private final int mUid = android.os.Binder.getCallingUid();
        private final int mPid = android.os.Binder.getCallingPid();

        public Client(android.os.IBinder token) {
            this.mToken = token;
        }

        public int getUid() {
            return this.mUid;
        }

        private int getUserId() {
            return android.os.UserHandle.getUserId(this.mUid);
        }

        public void addListener(android.media.midi.IMidiDeviceListener listener) {
            if (this.mListeners.size() >= 16) {
                throw new java.lang.SecurityException("too many MIDI listeners for UID = " + this.mUid);
            }
            this.mListeners.put(listener.asBinder(), listener);
        }

        public void removeListener(android.media.midi.IMidiDeviceListener listener) {
            this.mListeners.remove(listener.asBinder());
            if (this.mListeners.size() == 0 && this.mDeviceConnections.size() == 0) {
                close();
            }
        }

        public void addDeviceConnection(com.android.server.midi.MidiService.Device device, android.media.midi.IMidiDeviceOpenCallback callback, int userId) {
            android.util.Log.d(TAG, "addDeviceConnection() device:" + device + " userId:" + userId);
            if (this.mDeviceConnections.size() >= 64) {
                android.util.Log.i(TAG, "too many MIDI connections for UID = " + this.mUid);
                throw new java.lang.SecurityException("too many MIDI connections for UID = " + this.mUid);
            }
            com.android.server.midi.MidiService.DeviceConnection connection = com.android.server.midi.MidiService.this.new DeviceConnection(device, this, callback);
            this.mDeviceConnections.put(connection.getToken(), connection);
            device.addDeviceConnection(connection, userId);
        }

        public void removeDeviceConnection(android.os.IBinder token) {
            com.android.server.midi.MidiService.DeviceConnection connection = this.mDeviceConnections.remove(token);
            if (connection != null) {
                connection.getDevice().removeDeviceConnection(connection);
            }
            if (this.mListeners.size() == 0 && this.mDeviceConnections.size() == 0) {
                close();
            }
        }

        public void removeDeviceConnection(com.android.server.midi.MidiService.DeviceConnection connection) {
            this.mDeviceConnections.remove(connection.getToken());
            if (this.mListeners.size() == 0 && this.mDeviceConnections.size() == 0) {
                close();
            }
        }

        public void deviceAdded(com.android.server.midi.MidiService.Device device) {
            android.util.Log.d(TAG, "deviceAdded() " + device.getUserId() + " userId:" + getUserId());
            if (!device.isUidAllowed(this.mUid) || !device.isUserIdAllowed(getUserId())) {
                return;
            }
            android.media.midi.MidiDeviceInfo deviceInfo = device.getDeviceInfo();
            try {
                for (android.media.midi.IMidiDeviceListener listener : this.mListeners.values()) {
                    listener.onDeviceAdded(deviceInfo);
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "remote exception", e);
            }
        }

        public void deviceRemoved(com.android.server.midi.MidiService.Device device) {
            android.util.Log.d(TAG, "deviceRemoved() " + device.getUserId() + " userId:" + getUserId());
            if (!device.isUidAllowed(this.mUid) || !device.isUserIdAllowed(getUserId())) {
                return;
            }
            android.media.midi.MidiDeviceInfo deviceInfo = device.getDeviceInfo();
            try {
                for (android.media.midi.IMidiDeviceListener listener : this.mListeners.values()) {
                    listener.onDeviceRemoved(deviceInfo);
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "remote exception", e);
            }
        }

        public void deviceStatusChanged(com.android.server.midi.MidiService.Device device, android.media.midi.MidiDeviceStatus status) {
            android.util.Log.d(TAG, "deviceStatusChanged() " + device.getUserId() + " userId:" + getUserId());
            if (!device.isUidAllowed(this.mUid) || !device.isUserIdAllowed(getUserId())) {
                return;
            }
            try {
                for (android.media.midi.IMidiDeviceListener listener : this.mListeners.values()) {
                    listener.onDeviceStatusChanged(status);
                }
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "remote exception", e);
            }
        }

        private void close() {
            synchronized (com.android.server.midi.MidiService.this.mClients) {
                com.android.server.midi.MidiService.this.mClients.remove(this.mToken);
                this.mToken.unlinkToDeath(this, 0);
            }
            for (com.android.server.midi.MidiService.DeviceConnection connection : this.mDeviceConnections.values()) {
                connection.getDevice().removeDeviceConnection(connection);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.d(TAG, "Client died: " + this);
            close();
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("Client: UID: ");
            sb.append(this.mUid);
            sb.append(" PID: ");
            sb.append(this.mPid);
            sb.append(" listener count: ");
            sb.append(this.mListeners.size());
            sb.append(" Device Connections:");
            for (com.android.server.midi.MidiService.DeviceConnection connection : this.mDeviceConnections.values()) {
                sb.append(" <device ");
                sb.append(connection.getDevice().getDeviceInfo().getId());
                sb.append(">");
            }
            return sb.toString();
        }
    }

    private com.android.server.midi.MidiService.Client getClient(android.os.IBinder token) {
        com.android.server.midi.MidiService.Client client;
        synchronized (this.mClients) {
            client = this.mClients.get(token);
            if (client == null) {
                client = new com.android.server.midi.MidiService.Client(token);
                try {
                    token.linkToDeath(client, 0);
                    this.mClients.put(token, client);
                } catch (android.os.RemoteException e) {
                    return null;
                }
            }
        }
        return client;
    }

    private final class Device implements android.os.IBinder.DeathRecipient {
        private static final java.lang.String TAG = "MidiService.Device";
        private final android.bluetooth.BluetoothDevice mBluetoothDevice;
        private final java.util.ArrayList<com.android.server.midi.MidiService.DeviceConnection> mDeviceConnections;
        private java.util.concurrent.atomic.AtomicInteger mDeviceConnectionsAdded;
        private java.util.concurrent.atomic.AtomicInteger mDeviceConnectionsRemoved;
        private android.media.midi.MidiDeviceInfo mDeviceInfo;
        private android.media.midi.MidiDeviceStatus mDeviceStatus;
        private java.time.Instant mPreviousCounterInstant;
        private android.media.midi.IMidiDeviceServer mServer;
        private android.content.ServiceConnection mServiceConnection;
        private final android.content.pm.ServiceInfo mServiceInfo;
        private java.util.concurrent.atomic.AtomicInteger mTotalInputBytes;
        private java.util.concurrent.atomic.AtomicInteger mTotalOutputBytes;
        private java.util.concurrent.atomic.AtomicLong mTotalTimeConnectedNs;
        private final int mUid;
        private final int mUserId;

        public Device(android.media.midi.IMidiDeviceServer server, android.media.midi.MidiDeviceInfo deviceInfo, android.content.pm.ServiceInfo serviceInfo, int uid, int userId) {
            this.mDeviceConnections = new java.util.ArrayList<>();
            this.mDeviceConnectionsAdded = new java.util.concurrent.atomic.AtomicInteger();
            this.mDeviceConnectionsRemoved = new java.util.concurrent.atomic.AtomicInteger();
            this.mTotalTimeConnectedNs = new java.util.concurrent.atomic.AtomicLong();
            this.mPreviousCounterInstant = null;
            this.mTotalInputBytes = new java.util.concurrent.atomic.AtomicInteger();
            this.mTotalOutputBytes = new java.util.concurrent.atomic.AtomicInteger();
            this.mDeviceInfo = deviceInfo;
            this.mServiceInfo = serviceInfo;
            this.mUid = uid;
            this.mUserId = userId;
            this.mBluetoothDevice = (android.bluetooth.BluetoothDevice) deviceInfo.getProperties().getParcelable("bluetooth_device", android.bluetooth.BluetoothDevice.class);
            setDeviceServer(server);
        }

        public Device(android.bluetooth.BluetoothDevice bluetoothDevice) {
            this.mDeviceConnections = new java.util.ArrayList<>();
            this.mDeviceConnectionsAdded = new java.util.concurrent.atomic.AtomicInteger();
            this.mDeviceConnectionsRemoved = new java.util.concurrent.atomic.AtomicInteger();
            this.mTotalTimeConnectedNs = new java.util.concurrent.atomic.AtomicLong();
            this.mPreviousCounterInstant = null;
            this.mTotalInputBytes = new java.util.concurrent.atomic.AtomicInteger();
            this.mTotalOutputBytes = new java.util.concurrent.atomic.AtomicInteger();
            this.mBluetoothDevice = bluetoothDevice;
            this.mServiceInfo = null;
            this.mUid = com.android.server.midi.MidiService.this.mBluetoothServiceUid;
            this.mUserId = android.os.UserHandle.getUserId(this.mUid);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setDeviceServer(android.media.midi.IMidiDeviceServer server) {
            android.util.Log.i(TAG, "setDeviceServer()");
            if (server != null) {
                if (this.mServer != null) {
                    android.util.Log.e(TAG, "mServer already set in setDeviceServer");
                    return;
                }
                android.os.IBinder binder = server.asBinder();
                try {
                    binder.linkToDeath(this, 0);
                    this.mServer = server;
                    com.android.server.midi.MidiService.this.mDevicesByServer.put(binder, this);
                } catch (android.os.RemoteException e) {
                    this.mServer = null;
                    return;
                }
            } else if (this.mServer != null) {
                server = this.mServer;
                this.mServer = null;
                android.os.IBinder binder2 = server.asBinder();
                com.android.server.midi.MidiService.this.mDevicesByServer.remove(binder2);
                this.mDeviceStatus = null;
                try {
                    server.closeDevice();
                    binder2.unlinkToDeath(this, 0);
                } catch (android.os.RemoteException e2) {
                }
            }
            if (this.mDeviceConnections != null) {
                synchronized (this.mDeviceConnections) {
                    for (com.android.server.midi.MidiService.DeviceConnection connection : this.mDeviceConnections) {
                        connection.notifyClient(server);
                    }
                }
            }
        }

        public android.media.midi.MidiDeviceInfo getDeviceInfo() {
            return this.mDeviceInfo;
        }

        public void setDeviceInfo(android.media.midi.MidiDeviceInfo deviceInfo) {
            this.mDeviceInfo = deviceInfo;
        }

        public android.media.midi.MidiDeviceStatus getDeviceStatus() {
            return this.mDeviceStatus;
        }

        public void setDeviceStatus(android.media.midi.MidiDeviceStatus status) {
            this.mDeviceStatus = status;
        }

        public android.media.midi.IMidiDeviceServer getDeviceServer() {
            return this.mServer;
        }

        public android.content.pm.ServiceInfo getServiceInfo() {
            return this.mServiceInfo;
        }

        public java.lang.String getPackageName() {
            if (this.mServiceInfo == null) {
                return null;
            }
            return this.mServiceInfo.packageName;
        }

        public int getUid() {
            return this.mUid;
        }

        public int getUserId() {
            return this.mUserId;
        }

        public boolean isUidAllowed(int uid) {
            return !this.mDeviceInfo.isPrivate() || this.mUid == uid;
        }

        public boolean isUserIdAllowed(int userId) {
            return this.mDeviceInfo.getType() != 2 || this.mUserId == userId;
        }

        public void addDeviceConnection(com.android.server.midi.MidiService.DeviceConnection connection, int userId) {
            android.content.Intent intent;
            android.util.Log.d(TAG, "addDeviceConnection() [A] connection:" + connection);
            synchronized (this.mDeviceConnections) {
                this.mDeviceConnectionsAdded.incrementAndGet();
                if (this.mPreviousCounterInstant == null) {
                    this.mPreviousCounterInstant = java.time.Instant.now();
                }
                android.util.Log.d(TAG, "  mServer:" + this.mServer);
                if (this.mServer != null) {
                    android.util.Log.i(TAG, "++++ A");
                    this.mDeviceConnections.add(connection);
                    connection.notifyClient(this.mServer);
                } else if (this.mServiceConnection == null && (this.mServiceInfo != null || this.mBluetoothDevice != null)) {
                    android.util.Log.i(TAG, "++++ B");
                    this.mDeviceConnections.add(connection);
                    this.mServiceConnection = new android.content.ServiceConnection() { // from class: com.android.server.midi.MidiService.Device.1
                        @Override // android.content.ServiceConnection
                        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                            android.util.Log.i(com.android.server.midi.MidiService.Device.TAG, "++++ onServiceConnected() mBluetoothDevice:" + com.android.server.midi.MidiService.Device.this.mBluetoothDevice);
                            android.media.midi.IMidiDeviceServer server = null;
                            if (com.android.server.midi.MidiService.Device.this.mBluetoothDevice != null) {
                                android.media.midi.IBluetoothMidiService mBluetoothMidiService = android.media.midi.IBluetoothMidiService.Stub.asInterface(service);
                                android.util.Log.i(com.android.server.midi.MidiService.Device.TAG, "++++ mBluetoothMidiService:" + mBluetoothMidiService);
                                if (mBluetoothMidiService != null) {
                                    try {
                                        android.os.IBinder deviceBinder = mBluetoothMidiService.addBluetoothDevice(com.android.server.midi.MidiService.Device.this.mBluetoothDevice);
                                        server = android.media.midi.IMidiDeviceServer.Stub.asInterface(deviceBinder);
                                    } catch (android.os.RemoteException e) {
                                        android.util.Log.e(com.android.server.midi.MidiService.Device.TAG, "Could not call addBluetoothDevice()", e);
                                    } catch (java.lang.NullPointerException e2) {
                                        android.util.Log.e(com.android.server.midi.MidiService.Device.TAG, "Could not call addBluetoothDevice()", e2);
                                    }
                                }
                            } else {
                                server = android.media.midi.IMidiDeviceServer.Stub.asInterface(service);
                            }
                            com.android.server.midi.MidiService.Device.this.setDeviceServer(server);
                        }

                        @Override // android.content.ServiceConnection
                        public void onServiceDisconnected(android.content.ComponentName name) {
                            com.android.server.midi.MidiService.Device.this.setDeviceServer(null);
                            com.android.server.midi.MidiService.Device.this.mServiceConnection = null;
                        }
                    };
                    if (this.mBluetoothDevice != null) {
                        intent = new android.content.Intent("android.media.midi.BluetoothMidiService");
                        intent.setComponent(new android.content.ComponentName("com.android.bluetoothmidiservice", "com.android.bluetoothmidiservice.BluetoothMidiService"));
                    } else if (!com.android.server.midi.MidiService.this.isUmpDevice(this.mDeviceInfo)) {
                        intent = new android.content.Intent("android.media.midi.MidiDeviceService");
                        intent.setComponent(new android.content.ComponentName(this.mServiceInfo.packageName, this.mServiceInfo.name));
                    } else {
                        intent = new android.content.Intent("android.media.midi.MidiUmpDeviceService");
                        intent.setComponent(new android.content.ComponentName(this.mServiceInfo.packageName, this.mServiceInfo.name));
                    }
                    if (!com.android.server.midi.MidiService.this.mContext.bindServiceAsUser(intent, this.mServiceConnection, 1, android.os.UserHandle.of(this.mUserId))) {
                        android.util.Log.e(TAG, "Unable to bind service: " + intent);
                        setDeviceServer(null);
                        this.mServiceConnection = null;
                    }
                } else {
                    android.util.Log.e(TAG, "No way to connect to device in addDeviceConnection");
                    connection.notifyClient(null);
                }
            }
        }

        public void removeDeviceConnection(com.android.server.midi.MidiService.DeviceConnection connection) {
            synchronized (com.android.server.midi.MidiService.this.mDevicesByInfo) {
                synchronized (this.mDeviceConnections) {
                    int numRemovedConnections = this.mDeviceConnectionsRemoved.incrementAndGet();
                    if (this.mPreviousCounterInstant != null) {
                        this.mTotalTimeConnectedNs.addAndGet(java.time.Duration.between(this.mPreviousCounterInstant, java.time.Instant.now()).toNanos());
                    }
                    if (numRemovedConnections >= this.mDeviceConnectionsAdded.get()) {
                        this.mPreviousCounterInstant = null;
                    } else {
                        this.mPreviousCounterInstant = java.time.Instant.now();
                    }
                    logMetrics(false);
                    this.mDeviceConnections.remove(connection);
                    if (connection.getDevice().getDeviceInfo().getType() == 1) {
                        synchronized (com.android.server.midi.MidiService.this.mUsbMidiLock) {
                            com.android.server.midi.MidiService.this.removeUsbMidiDeviceLocked(connection.getDevice().getDeviceInfo());
                        }
                    }
                    if (this.mDeviceConnections.size() == 0 && this.mServiceConnection != null) {
                        com.android.server.midi.MidiService.this.mContext.unbindService(this.mServiceConnection);
                        this.mServiceConnection = null;
                        if (this.mBluetoothDevice != null) {
                            closeLocked();
                        } else {
                            setDeviceServer(null);
                        }
                    }
                }
            }
        }

        public void closeLocked() {
            synchronized (this.mDeviceConnections) {
                for (com.android.server.midi.MidiService.DeviceConnection connection : this.mDeviceConnections) {
                    if (connection.getDevice().getDeviceInfo().getType() == 1) {
                        synchronized (com.android.server.midi.MidiService.this.mUsbMidiLock) {
                            com.android.server.midi.MidiService.this.removeUsbMidiDeviceLocked(connection.getDevice().getDeviceInfo());
                        }
                    }
                    connection.getClient().removeDeviceConnection(connection);
                }
                this.mDeviceConnections.clear();
                if (this.mPreviousCounterInstant != null) {
                    java.time.Instant currentInstant = java.time.Instant.now();
                    this.mTotalTimeConnectedNs.addAndGet(java.time.Duration.between(this.mPreviousCounterInstant, currentInstant).toNanos());
                    this.mPreviousCounterInstant = currentInstant;
                }
                logMetrics(true);
            }
            setDeviceServer(null);
            if (this.mServiceInfo == null) {
                com.android.server.midi.MidiService.this.removeDeviceLocked(this);
            } else {
                this.mDeviceStatus = new android.media.midi.MidiDeviceStatus(this.mDeviceInfo);
            }
            if (this.mBluetoothDevice != null) {
                com.android.server.midi.MidiService.this.mBluetoothDevices.remove(this.mBluetoothDevice);
            }
        }

        private void logMetrics(boolean isDeviceDisconnected) {
            int numDeviceConnectionAdded = this.mDeviceConnectionsAdded.get();
            if (this.mDeviceInfo != null && numDeviceConnectionAdded > 0) {
                new android.media.MediaMetrics.Item("audio.midi").setUid(this.mUid).set(android.media.MediaMetrics.Property.DEVICE_ID, java.lang.Integer.valueOf(this.mDeviceInfo.getId())).set(android.media.MediaMetrics.Property.INPUT_PORT_COUNT, java.lang.Integer.valueOf(this.mDeviceInfo.getInputPortCount())).set(android.media.MediaMetrics.Property.OUTPUT_PORT_COUNT, java.lang.Integer.valueOf(this.mDeviceInfo.getOutputPortCount())).set(android.media.MediaMetrics.Property.HARDWARE_TYPE, java.lang.Integer.valueOf(this.mDeviceInfo.getType())).set(android.media.MediaMetrics.Property.DURATION_NS, java.lang.Long.valueOf(this.mTotalTimeConnectedNs.get())).set(android.media.MediaMetrics.Property.OPENED_COUNT, java.lang.Integer.valueOf(numDeviceConnectionAdded)).set(android.media.MediaMetrics.Property.CLOSED_COUNT, java.lang.Integer.valueOf(this.mDeviceConnectionsRemoved.get())).set(android.media.MediaMetrics.Property.DEVICE_DISCONNECTED, isDeviceDisconnected ? "true" : "false").set(android.media.MediaMetrics.Property.IS_SHARED, !this.mDeviceInfo.isPrivate() ? "true" : "false").set(android.media.MediaMetrics.Property.SUPPORTS_MIDI_UMP, com.android.server.midi.MidiService.this.isUmpDevice(this.mDeviceInfo) ? "true" : "false").set(android.media.MediaMetrics.Property.USING_ALSA, this.mDeviceInfo.getProperties().get("alsa_card") == null ? "false" : "true").set(android.media.MediaMetrics.Property.EVENT, "deviceClosed").set(android.media.MediaMetrics.Property.TOTAL_INPUT_BYTES, java.lang.Integer.valueOf(this.mTotalInputBytes.get())).set(android.media.MediaMetrics.Property.TOTAL_OUTPUT_BYTES, java.lang.Integer.valueOf(this.mTotalOutputBytes.get())).record();
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.d(TAG, "Device died: " + this);
            synchronized (com.android.server.midi.MidiService.this.mDevicesByInfo) {
                closeLocked();
            }
        }

        public void updateTotalBytes(int totalInputBytes, int totalOutputBytes) {
            this.mTotalInputBytes.set(totalInputBytes);
            this.mTotalOutputBytes.set(totalOutputBytes);
        }

        public java.lang.String toString() {
            return "Device Info: " + this.mDeviceInfo + " Status: " + this.mDeviceStatus + " UID: " + this.mUid + " DeviceConnection count: " + this.mDeviceConnections.size() + " mServiceConnection: " + this.mServiceConnection;
        }
    }

    private final class DeviceConnection {
        private static final java.lang.String TAG = "MidiService.DeviceConnection";
        private android.media.midi.IMidiDeviceOpenCallback mCallback;
        private final com.android.server.midi.MidiService.Client mClient;
        private final com.android.server.midi.MidiService.Device mDevice;
        private final android.os.IBinder mToken = new android.os.Binder();

        public DeviceConnection(com.android.server.midi.MidiService.Device device, com.android.server.midi.MidiService.Client client, android.media.midi.IMidiDeviceOpenCallback callback) {
            this.mDevice = device;
            this.mClient = client;
            this.mCallback = callback;
        }

        public com.android.server.midi.MidiService.Device getDevice() {
            return this.mDevice;
        }

        public com.android.server.midi.MidiService.Client getClient() {
            return this.mClient;
        }

        public android.os.IBinder getToken() {
            return this.mToken;
        }

        public void notifyClient(android.media.midi.IMidiDeviceServer deviceServer) {
            android.util.Log.d(TAG, "notifyClient");
            if (this.mCallback != null) {
                try {
                    this.mCallback.onDeviceOpened(deviceServer, deviceServer == null ? null : this.mToken);
                } catch (android.os.RemoteException e) {
                }
                this.mCallback = null;
            }
        }

        public java.lang.String toString() {
            return (this.mDevice == null || this.mDevice.getDeviceInfo() == null) ? "null" : "" + this.mDevice.getDeviceInfo().getId();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBLEMIDIDevice(android.bluetooth.BluetoothDevice btDevice) {
        android.os.ParcelUuid[] uuids = btDevice.getUuids();
        if (uuids != null) {
            for (android.os.ParcelUuid uuid : uuids) {
                if (uuid.getUuid().equals(MIDI_SERVICE)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void dumpIntentExtras(android.content.Intent intent) {
        java.lang.String action = intent.getAction();
        android.util.Log.d(TAG, "Intent: " + action);
        android.os.Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (java.lang.String key : bundle.keySet()) {
                android.util.Log.d(TAG, "  " + key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isBleTransport(android.content.Intent intent) {
        android.os.Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return false;
        }
        boolean isBle = bundle.getInt("android.bluetooth.device.extra.TRANSPORT", 0) == 2;
        return isBle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpUuids(android.bluetooth.BluetoothDevice btDevice) {
        android.os.ParcelUuid[] uuidParcels = btDevice.getUuids();
        android.util.Log.d(TAG, "dumpUuids(" + btDevice + ") numParcels:" + (uuidParcels != null ? uuidParcels.length : 0));
        if (uuidParcels == null) {
            android.util.Log.d(TAG, "No UUID Parcels");
            return;
        }
        for (android.os.ParcelUuid parcel : uuidParcels) {
            java.util.UUID uuid = parcel.getUuid();
            android.util.Log.d(TAG, " uuid:" + uuid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasNonMidiUuids(android.bluetooth.BluetoothDevice btDevice) {
        android.os.ParcelUuid[] uuidParcels = btDevice.getUuids();
        if (uuidParcels != null) {
            for (android.os.ParcelUuid parcel : uuidParcels) {
                if (this.mNonMidiUUIDs.contains(parcel)) {
                    return true;
                }
            }
        }
        return false;
    }

    public MidiService(android.content.Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        this.mPackageMonitor.register(this.mContext, (android.os.Looper) null, android.os.UserHandle.ALL, true);
        this.mBluetoothServiceUid = -1;
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.A2DP_SINK);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.A2DP_SOURCE);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.ADV_AUDIO_DIST);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.AVRCP_CONTROLLER);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.HFP);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.HSP);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.HID);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.LE_AUDIO);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.HOGP);
        this.mNonMidiUUIDs.add(android.bluetooth.BluetoothUuid.HEARING_AID);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStartOrUnlockUser(com.android.server.SystemService.TargetUser user, boolean matchDirectBootUnaware) throws java.lang.Throwable {
        android.content.pm.PackageInfo info;
        android.util.Log.d(TAG, "onStartOrUnlockUser " + user.getUserIdentifier() + " matchDirectBootUnaware: " + matchDirectBootUnaware);
        int resolveFlags = matchDirectBootUnaware ? 128 | 262144 : 128;
        android.content.Intent intent = new android.content.Intent("android.media.midi.MidiDeviceService");
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = this.mPackageManager.queryIntentServicesAsUser(intent, resolveFlags, user.getUserIdentifier());
        if (resolveInfos != null) {
            int count = resolveInfos.size();
            for (int i = 0; i < count; i++) {
                android.content.pm.ServiceInfo serviceInfo = resolveInfos.get(i).serviceInfo;
                if (serviceInfo != null) {
                    addLegacyPackageDeviceServer(serviceInfo, user.getUserIdentifier());
                }
            }
        }
        android.content.Intent intent2 = new android.content.Intent("android.media.midi.MidiUmpDeviceService");
        java.util.List<android.content.pm.ResolveInfo> resolveInfos2 = this.mPackageManager.queryIntentServicesAsUser(intent2, resolveFlags, user.getUserIdentifier());
        if (resolveInfos2 != null) {
            int count2 = resolveInfos2.size();
            for (int i2 = 0; i2 < count2; i2++) {
                android.content.pm.ServiceInfo serviceInfo2 = resolveInfos2.get(i2).serviceInfo;
                if (serviceInfo2 != null) {
                    addUmpPackageDeviceServer(serviceInfo2, user.getUserIdentifier());
                }
            }
        }
        android.os.UserHandle mainUser = this.mUserManager.getMainUser();
        if (mainUser == null || user.getUserIdentifier() == mainUser.getIdentifier()) {
            try {
                info = this.mPackageManager.getPackageInfoAsUser("com.android.bluetoothmidiservice", 0, user.getUserIdentifier());
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                info = null;
            }
            if (info != null && info.applicationInfo != null) {
                this.mBluetoothServiceUid = info.applicationInfo.uid;
            }
        }
    }

    public void registerListener(android.os.IBinder token, android.media.midi.IMidiDeviceListener listener) {
        com.android.server.midi.MidiService.Client client = getClient(token);
        if (client == null) {
            return;
        }
        client.addListener(listener);
        updateStickyDeviceStatus(client.mUid, listener);
    }

    public void unregisterListener(android.os.IBinder token, android.media.midi.IMidiDeviceListener listener) {
        com.android.server.midi.MidiService.Client client = getClient(token);
        if (client == null) {
            return;
        }
        client.removeListener(listener);
    }

    private void updateStickyDeviceStatus(int uid, android.media.midi.IMidiDeviceListener listener) {
        int userId = android.os.UserHandle.getUserId(uid);
        synchronized (this.mDevicesByInfo) {
            for (com.android.server.midi.MidiService.Device device : this.mDevicesByInfo.values()) {
                if (device.isUidAllowed(uid) && device.isUserIdAllowed(userId)) {
                    try {
                        android.media.midi.MidiDeviceStatus status = device.getDeviceStatus();
                        if (status != null) {
                            listener.onDeviceStatusChanged(status);
                        }
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(TAG, "remote exception", e);
                    }
                }
            }
        }
    }

    public android.media.midi.MidiDeviceInfo[] getDevices() {
        return getDevicesForTransport(1);
    }

    public android.media.midi.MidiDeviceInfo[] getDevicesForTransport(int transport) {
        java.util.ArrayList<android.media.midi.MidiDeviceInfo> deviceInfos = new java.util.ArrayList<>();
        int uid = android.os.Binder.getCallingUid();
        int userId = getCallingUserId();
        synchronized (this.mDevicesByInfo) {
            for (com.android.server.midi.MidiService.Device device : this.mDevicesByInfo.values()) {
                if (device.isUidAllowed(uid) && device.isUserIdAllowed(userId)) {
                    if (transport == 2) {
                        if (isUmpDevice(device.getDeviceInfo())) {
                            deviceInfos.add(device.getDeviceInfo());
                        }
                    } else if (transport == 1 && !isUmpDevice(device.getDeviceInfo())) {
                        deviceInfos.add(device.getDeviceInfo());
                    }
                }
            }
        }
        return (android.media.midi.MidiDeviceInfo[]) deviceInfos.toArray(EMPTY_DEVICE_INFO_ARRAY);
    }

    public void openDevice(android.os.IBinder token, android.media.midi.MidiDeviceInfo deviceInfo, android.media.midi.IMidiDeviceOpenCallback callback) {
        com.android.server.midi.MidiService.Device device;
        com.android.server.midi.MidiService.Client client = getClient(token);
        android.util.Log.d(TAG, "openDevice() client:" + client);
        if (client == null) {
            return;
        }
        synchronized (this.mDevicesByInfo) {
            device = this.mDevicesByInfo.get(deviceInfo);
            android.util.Log.d(TAG, "  device:" + device);
            if (device == null) {
                throw new java.lang.IllegalArgumentException("device does not exist: " + deviceInfo);
            }
            if (!device.isUidAllowed(android.os.Binder.getCallingUid())) {
                throw new java.lang.SecurityException("Attempt to open private device with wrong UID");
            }
            if (!device.isUserIdAllowed(getCallingUserId())) {
                throw new java.lang.SecurityException("Attempt to open virtual device with wrong user id");
            }
        }
        if (deviceInfo.getType() == 1) {
            synchronized (this.mUsbMidiLock) {
                if (isUsbMidiDeviceInUseLocked(deviceInfo)) {
                    throw new java.lang.IllegalArgumentException("device already in use: " + deviceInfo);
                }
                addUsbMidiDeviceLocked(deviceInfo);
            }
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.util.Log.i(TAG, "addDeviceConnection() [B] device:" + device);
            client.addDeviceConnection(device, callback, getCallingUserId());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openBluetoothDevice(final android.bluetooth.BluetoothDevice bluetoothDevice) {
        android.util.Log.d(TAG, "openBluetoothDevice() device: " + bluetoothDevice);
        android.media.midi.MidiManager midiManager = (android.media.midi.MidiManager) this.mContext.getSystemService(android.media.midi.MidiManager.class);
        midiManager.openBluetoothDevice(bluetoothDevice, new android.media.midi.MidiManager.OnDeviceOpenedListener() { // from class: com.android.server.midi.MidiService.3
            @Override // android.media.midi.MidiManager.OnDeviceOpenedListener
            public void onDeviceOpened(android.media.midi.MidiDevice device) {
                synchronized (com.android.server.midi.MidiService.this.mBleMidiDeviceMap) {
                    android.util.Log.i(com.android.server.midi.MidiService.TAG, "onDeviceOpened() device:" + device);
                    com.android.server.midi.MidiService.this.mBleMidiDeviceMap.put(bluetoothDevice, device);
                }
            }
        }, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeBluetoothDevice(android.bluetooth.BluetoothDevice bluetoothDevice) {
        android.media.midi.MidiDevice midiDevice;
        android.util.Log.d(TAG, "closeBluetoothDevice() device: " + bluetoothDevice);
        synchronized (this.mBleMidiDeviceMap) {
            midiDevice = this.mBleMidiDeviceMap.remove(bluetoothDevice);
        }
        if (midiDevice != null) {
            try {
                midiDevice.close();
            } catch (java.io.IOException ex) {
                android.util.Log.e(TAG, "Exception closing BLE-MIDI device" + ex);
            }
        }
    }

    public void openBluetoothDevice(android.os.IBinder token, android.bluetooth.BluetoothDevice bluetoothDevice, android.media.midi.IMidiDeviceOpenCallback callback) {
        com.android.server.midi.MidiService.Device device;
        android.util.Log.d(TAG, "openBluetoothDevice()");
        com.android.server.midi.MidiService.Client client = getClient(token);
        if (client == null) {
            return;
        }
        android.util.Log.i(TAG, "alloc device...");
        synchronized (this.mDevicesByInfo) {
            device = this.mBluetoothDevices.get(bluetoothDevice);
            if (device == null) {
                device = new com.android.server.midi.MidiService.Device(bluetoothDevice);
                this.mBluetoothDevices.put(bluetoothDevice, device);
            }
        }
        android.util.Log.i(TAG, "device: " + device);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.util.Log.i(TAG, "addDeviceConnection() [C] device:" + device);
            client.addDeviceConnection(device, callback, getCallingUserId());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void closeDevice(android.os.IBinder clientToken, android.os.IBinder deviceToken) {
        com.android.server.midi.MidiService.Client client = getClient(clientToken);
        if (client == null) {
            return;
        }
        client.removeDeviceConnection(deviceToken);
    }

    public android.media.midi.MidiDeviceInfo registerDeviceServer(android.media.midi.IMidiDeviceServer server, int numInputPorts, int numOutputPorts, java.lang.String[] inputPortNames, java.lang.String[] outputPortNames, android.os.Bundle properties, int type, int defaultProtocol) {
        android.media.midi.MidiDeviceInfo midiDeviceInfoAddDeviceLocked;
        int uid = android.os.Binder.getCallingUid();
        int userId = getCallingUserId();
        if (type == 1 && uid != 1000) {
            throw new java.lang.SecurityException("only system can create USB devices");
        }
        if (type == 3 && uid != this.mBluetoothServiceUid) {
            throw new java.lang.SecurityException("only MidiBluetoothService can create Bluetooth devices");
        }
        synchronized (this.mDevicesByInfo) {
            midiDeviceInfoAddDeviceLocked = addDeviceLocked(type, numInputPorts, numOutputPorts, inputPortNames, outputPortNames, properties, server, null, false, uid, defaultProtocol, userId);
        }
        return midiDeviceInfoAddDeviceLocked;
    }

    public void unregisterDeviceServer(android.media.midi.IMidiDeviceServer server) {
        synchronized (this.mDevicesByInfo) {
            com.android.server.midi.MidiService.Device device = this.mDevicesByServer.get(server.asBinder());
            if (device != null) {
                device.closeLocked();
            }
        }
    }

    public android.media.midi.MidiDeviceInfo getServiceDeviceInfo(java.lang.String packageName, java.lang.String className) {
        int uid = android.os.Binder.getCallingUid();
        synchronized (this.mDevicesByInfo) {
            for (com.android.server.midi.MidiService.Device device : this.mDevicesByInfo.values()) {
                android.content.pm.ServiceInfo serviceInfo = device.getServiceInfo();
                if (serviceInfo != null && packageName.equals(serviceInfo.packageName) && className.equals(serviceInfo.name)) {
                    if (device.isUidAllowed(uid)) {
                        return device.getDeviceInfo();
                    }
                    android.util.EventLog.writeEvent(1397638484, "185796676", -1, "");
                    return null;
                }
            }
            return null;
        }
    }

    public android.media.midi.MidiDeviceStatus getDeviceStatus(android.media.midi.MidiDeviceInfo deviceInfo) {
        com.android.server.midi.MidiService.Device device = this.mDevicesByInfo.get(deviceInfo);
        if (device == null) {
            throw new java.lang.IllegalArgumentException("no such device for " + deviceInfo);
        }
        int uid = android.os.Binder.getCallingUid();
        if (device.isUidAllowed(uid)) {
            return device.getDeviceStatus();
        }
        android.util.Log.e(TAG, "getDeviceStatus() invalid UID = " + uid);
        android.util.EventLog.writeEvent(1397638484, "203549963", java.lang.Integer.valueOf(uid), "getDeviceStatus: invalid uid");
        return null;
    }

    public void setDeviceStatus(android.media.midi.IMidiDeviceServer server, android.media.midi.MidiDeviceStatus status) {
        com.android.server.midi.MidiService.Device device = this.mDevicesByServer.get(server.asBinder());
        if (device != null) {
            if (android.os.Binder.getCallingUid() != device.getUid()) {
                throw new java.lang.SecurityException("setDeviceStatus() caller UID " + android.os.Binder.getCallingUid() + " does not match device's UID " + device.getUid());
            }
            device.setDeviceStatus(status);
            notifyDeviceStatusChanged(device, status);
        }
    }

    private void notifyDeviceStatusChanged(com.android.server.midi.MidiService.Device device, android.media.midi.MidiDeviceStatus status) {
        synchronized (this.mClients) {
            for (com.android.server.midi.MidiService.Client c : this.mClients.values()) {
                c.deviceStatusChanged(device, status);
            }
        }
    }

    private android.media.midi.MidiDeviceInfo addDeviceLocked(int type, int numInputPorts, int numOutputPorts, java.lang.String[] inputPortNames, java.lang.String[] outputPortNames, android.os.Bundle properties, android.media.midi.IMidiDeviceServer server, android.content.pm.ServiceInfo serviceInfo, boolean isPrivate, int uid, int defaultProtocol, int userId) {
        android.bluetooth.BluetoothDevice bluetoothDevice;
        com.android.server.midi.MidiService.Device device;
        android.util.Log.d(TAG, "addDeviceLocked() " + uid + " type:" + type + " userId:" + userId);
        int deviceCountForApp = 0;
        for (com.android.server.midi.MidiService.Device device2 : this.mDevicesByInfo.values()) {
            if (device2.getUid() == uid) {
                deviceCountForApp++;
            }
        }
        if (deviceCountForApp >= 16) {
            throw new java.lang.SecurityException("too many MIDI devices already created for UID = " + uid);
        }
        int id = this.mNextDeviceId;
        this.mNextDeviceId = id + 1;
        android.media.midi.MidiDeviceInfo deviceInfo = new android.media.midi.MidiDeviceInfo(type, id, numInputPorts, numOutputPorts, inputPortNames, outputPortNames, properties, isPrivate, defaultProtocol);
        if (server != null) {
            try {
                server.setDeviceInfo(deviceInfo);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "RemoteException in setDeviceInfo()");
                return null;
            }
        }
        com.android.server.midi.MidiService.Device device3 = null;
        if (type != 3) {
            bluetoothDevice = null;
        } else {
            android.bluetooth.BluetoothDevice bluetoothDevice2 = (android.bluetooth.BluetoothDevice) properties.getParcelable("bluetooth_device", android.bluetooth.BluetoothDevice.class);
            com.android.server.midi.MidiService.Device device4 = this.mBluetoothDevices.get(bluetoothDevice2);
            device3 = device4;
            if (device3 != null) {
                device3.setDeviceInfo(deviceInfo);
            }
            bluetoothDevice = bluetoothDevice2;
        }
        if (device3 == null) {
            com.android.server.midi.MidiService.Device device5 = new com.android.server.midi.MidiService.Device(server, deviceInfo, serviceInfo, uid, userId);
            device = device5;
        } else {
            device = device3;
        }
        this.mDevicesByInfo.put(deviceInfo, device);
        if (bluetoothDevice != null) {
            this.mBluetoothDevices.put(bluetoothDevice, device);
        }
        synchronized (this.mClients) {
            for (com.android.server.midi.MidiService.Client c : this.mClients.values()) {
                c.deviceAdded(device);
            }
        }
        return deviceInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDeviceLocked(com.android.server.midi.MidiService.Device device) {
        android.media.midi.IMidiDeviceServer server = device.getDeviceServer();
        if (server != null) {
            this.mDevicesByServer.remove(server.asBinder());
        }
        this.mDevicesByInfo.remove(device.getDeviceInfo());
        synchronized (this.mClients) {
            for (com.android.server.midi.MidiService.Client c : this.mClients.values()) {
                c.deviceRemoved(device);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPackageDeviceServers(java.lang.String packageName, int userId) throws java.lang.Throwable {
        try {
            android.content.pm.PackageInfo info = this.mPackageManager.getPackageInfoAsUser(packageName, 262276, userId);
            android.content.pm.ServiceInfo[] services = info.services;
            if (services == null) {
                return;
            }
            for (int i = 0; i < services.length; i++) {
                addLegacyPackageDeviceServer(services[i], userId);
                addUmpPackageDeviceServer(services[i], userId);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(TAG, "handlePackageUpdate could not find package " + packageName, e);
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:125:? -> B:93:0x0228). Please report as a decompilation issue!!! */
    private void addLegacyPackageDeviceServer(android.content.pm.ServiceInfo serviceInfo, int userId) throws java.lang.Throwable {
        java.util.ArrayList<java.lang.String> outputPortNames;
        java.util.ArrayList<java.lang.String> inputPortNames;
        android.content.res.XmlResourceParser parser;
        java.util.ArrayList<java.lang.String> outputPortNames2;
        java.util.ArrayList<java.lang.String> inputPortNames2;
        android.content.res.XmlResourceParser parser2;
        java.util.HashMap<android.media.midi.MidiDeviceInfo, com.android.server.midi.MidiService.Device> map;
        android.content.res.XmlResourceParser parser3 = null;
        if (serviceInfo == null) {
            return;
        }
        try {
            try {
                if (serviceInfo == null) {
                    android.util.Log.w(TAG, "Skipping null service info");
                    if (0 != 0) {
                        parser3.close();
                        return;
                    }
                    return;
                }
                if (!"android.permission.BIND_MIDI_DEVICE_SERVICE".equals(serviceInfo.permission)) {
                    if (0 != 0) {
                        parser3.close();
                        return;
                    }
                    return;
                }
                android.content.res.XmlResourceParser parser4 = serviceInfo.loadXmlMetaData(this.mPackageManager, "android.media.midi.MidiDeviceService");
                try {
                    if (parser4 == null) {
                        android.util.Log.w(TAG, "loading xml metadata failed");
                        if (parser4 != null) {
                            parser4.close();
                            return;
                        }
                        return;
                    }
                    try {
                        java.util.ArrayList<java.lang.String> inputPortNames3 = new java.util.ArrayList<>();
                        java.util.ArrayList<java.lang.String> outputPortNames3 = new java.util.ArrayList<>();
                        android.os.Bundle properties = null;
                        int numInputPorts = 0;
                        int numOutputPorts = 0;
                        boolean isPrivate = false;
                        while (true) {
                            int eventType = parser4.next();
                            if (eventType == 1) {
                                break;
                            }
                            if (eventType == 2) {
                                java.lang.String tagName = parser4.getName();
                                if ("device".equals(tagName)) {
                                    if (properties != null) {
                                        android.util.Log.w(TAG, "nested <device> elements in metadata for " + serviceInfo.packageName);
                                        outputPortNames2 = outputPortNames3;
                                        inputPortNames2 = inputPortNames3;
                                        parser2 = parser4;
                                        parser4 = parser2;
                                        outputPortNames3 = outputPortNames2;
                                        inputPortNames3 = inputPortNames2;
                                    } else {
                                        android.os.Bundle properties2 = new android.os.Bundle();
                                        properties2.putParcelable("service_info", serviceInfo);
                                        numInputPorts = 0;
                                        numOutputPorts = 0;
                                        int count = parser4.getAttributeCount();
                                        isPrivate = false;
                                        for (int i = 0; i < count; i++) {
                                            java.lang.String name = parser4.getAttributeName(i);
                                            java.lang.String value = parser4.getAttributeValue(i);
                                            if ("private".equals(name)) {
                                                isPrivate = "true".equals(value);
                                            } else {
                                                properties2.putString(name, value);
                                            }
                                        }
                                        properties = properties2;
                                        outputPortNames = outputPortNames3;
                                        inputPortNames = inputPortNames3;
                                        parser = parser4;
                                        parser4 = parser;
                                        outputPortNames3 = outputPortNames;
                                        inputPortNames3 = inputPortNames;
                                    }
                                } else if (!"input-port".equals(tagName)) {
                                    if ("output-port".equals(tagName)) {
                                        if (properties == null) {
                                            android.util.Log.w(TAG, "<output-port> outside of <device> in metadata for " + serviceInfo.packageName);
                                            outputPortNames2 = outputPortNames3;
                                            inputPortNames2 = inputPortNames3;
                                            parser2 = parser4;
                                            parser4 = parser2;
                                            outputPortNames3 = outputPortNames2;
                                            inputPortNames3 = inputPortNames2;
                                        } else {
                                            numOutputPorts++;
                                            java.lang.String portName = null;
                                            int count2 = parser4.getAttributeCount();
                                            int i2 = 0;
                                            while (true) {
                                                if (i2 >= count2) {
                                                    break;
                                                }
                                                java.lang.String name2 = parser4.getAttributeName(i2);
                                                java.lang.String value2 = parser4.getAttributeValue(i2);
                                                if ("name".equals(name2)) {
                                                    portName = value2;
                                                    break;
                                                }
                                                i2++;
                                            }
                                            outputPortNames3.add(portName);
                                        }
                                    }
                                    outputPortNames = outputPortNames3;
                                    inputPortNames = inputPortNames3;
                                    parser = parser4;
                                    parser4 = parser;
                                    outputPortNames3 = outputPortNames;
                                    inputPortNames3 = inputPortNames;
                                } else if (properties == null) {
                                    android.util.Log.w(TAG, "<input-port> outside of <device> in metadata for " + serviceInfo.packageName);
                                    outputPortNames2 = outputPortNames3;
                                    inputPortNames2 = inputPortNames3;
                                    parser2 = parser4;
                                    parser4 = parser2;
                                    outputPortNames3 = outputPortNames2;
                                    inputPortNames3 = inputPortNames2;
                                } else {
                                    numInputPorts++;
                                    java.lang.String portName2 = null;
                                    int count3 = parser4.getAttributeCount();
                                    int i3 = 0;
                                    while (true) {
                                        if (i3 >= count3) {
                                            break;
                                        }
                                        java.lang.String name3 = parser4.getAttributeName(i3);
                                        java.lang.String value3 = parser4.getAttributeValue(i3);
                                        if ("name".equals(name3)) {
                                            portName2 = value3;
                                            break;
                                        }
                                        i3++;
                                    }
                                    inputPortNames3.add(portName2);
                                    outputPortNames = outputPortNames3;
                                    inputPortNames = inputPortNames3;
                                    parser = parser4;
                                    parser4 = parser;
                                    outputPortNames3 = outputPortNames;
                                    inputPortNames3 = inputPortNames;
                                }
                            } else {
                                if (eventType != 3) {
                                    outputPortNames = outputPortNames3;
                                    inputPortNames = inputPortNames3;
                                    parser = parser4;
                                } else if (!"device".equals(parser4.getName())) {
                                    outputPortNames = outputPortNames3;
                                    inputPortNames = inputPortNames3;
                                    parser = parser4;
                                } else if (properties != null) {
                                    if (numInputPorts == 0 && numOutputPorts == 0) {
                                        android.util.Log.w(TAG, "<device> with no ports in metadata for " + serviceInfo.packageName);
                                        outputPortNames2 = outputPortNames3;
                                        inputPortNames2 = inputPortNames3;
                                        parser2 = parser4;
                                    } else {
                                        try {
                                            android.content.pm.ApplicationInfo appInfo = this.mPackageManager.getApplicationInfoAsUser(serviceInfo.packageName, 0, userId);
                                            int uid = appInfo.uid;
                                            java.util.HashMap<android.media.midi.MidiDeviceInfo, com.android.server.midi.MidiService.Device> map2 = this.mDevicesByInfo;
                                            synchronized (map2) {
                                                try {
                                                    map = map2;
                                                    outputPortNames = outputPortNames3;
                                                    inputPortNames = inputPortNames3;
                                                    parser = parser4;
                                                    try {
                                                        addDeviceLocked(2, numInputPorts, numOutputPorts, (java.lang.String[]) inputPortNames3.toArray(EMPTY_STRING_ARRAY), (java.lang.String[]) outputPortNames3.toArray(EMPTY_STRING_ARRAY), properties, null, serviceInfo, isPrivate, uid, -1, userId);
                                                        try {
                                                            inputPortNames.clear();
                                                            outputPortNames.clear();
                                                            properties = null;
                                                        } catch (java.lang.Exception e) {
                                                            e = e;
                                                            parser3 = parser;
                                                            android.util.Log.w(TAG, "Unable to load component info " + serviceInfo.toString(), e);
                                                            if (parser3 != null) {
                                                                parser3.close();
                                                            }
                                                            return;
                                                        } catch (java.lang.Throwable th) {
                                                            th = th;
                                                            parser3 = parser;
                                                            if (parser3 != null) {
                                                                parser3.close();
                                                            }
                                                            throw th;
                                                        }
                                                    } catch (java.lang.Throwable th2) {
                                                        th = th2;
                                                        throw th;
                                                    }
                                                } catch (java.lang.Throwable th3) {
                                                    th = th3;
                                                    map = map2;
                                                    parser = parser4;
                                                    throw th;
                                                }
                                            }
                                        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
                                            outputPortNames2 = outputPortNames3;
                                            inputPortNames2 = inputPortNames3;
                                            parser2 = parser4;
                                            android.util.Log.e(TAG, "could not fetch ApplicationInfo for " + serviceInfo.packageName);
                                            parser4 = parser2;
                                            outputPortNames3 = outputPortNames2;
                                            inputPortNames3 = inputPortNames2;
                                        }
                                    }
                                    parser4 = parser2;
                                    outputPortNames3 = outputPortNames2;
                                    inputPortNames3 = inputPortNames2;
                                } else {
                                    outputPortNames = outputPortNames3;
                                    inputPortNames = inputPortNames3;
                                    parser = parser4;
                                }
                                parser4 = parser;
                                outputPortNames3 = outputPortNames;
                                inputPortNames3 = inputPortNames;
                            }
                        }
                        if (parser4 != null) {
                            parser4.close();
                        }
                    } catch (java.lang.Exception e3) {
                        e = e3;
                        parser3 = parser4;
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                        parser3 = parser4;
                    }
                } catch (java.lang.Exception e4) {
                    e = e4;
                    parser3 = parser4;
                } catch (java.lang.Throwable th5) {
                    th = th5;
                    parser3 = parser4;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
        } catch (java.lang.Exception e5) {
            e = e5;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:137:0x0336 A[PHI: r1
  0x0336: PHI (r1v4 'parser' android.content.res.XmlResourceParser) = (r1v2 'parser' android.content.res.XmlResourceParser), (r1v5 'parser' android.content.res.XmlResourceParser) binds: [B:130:0x032a, B:136:0x0334] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:146:? -> B:105:0x027b). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void addUmpPackageDeviceServer(android.content.pm.ServiceInfo r29, int r30) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 827
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.midi.MidiService.addUmpPackageDeviceServer(android.content.pm.ServiceInfo, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePackageDeviceServers(java.lang.String packageName, int userId) {
        synchronized (this.mDevicesByInfo) {
            java.util.Iterator<com.android.server.midi.MidiService.Device> iterator = this.mDevicesByInfo.values().iterator();
            while (iterator.hasNext()) {
                com.android.server.midi.MidiService.Device device = iterator.next();
                if (packageName.equals(device.getPackageName()) && device.getUserId() == userId) {
                    iterator.remove();
                    removeDeviceLocked(device);
                }
            }
        }
    }

    public void updateTotalBytes(android.media.midi.IMidiDeviceServer server, int totalInputBytes, int totalOutputBytes) {
        synchronized (this.mDevicesByInfo) {
            com.android.server.midi.MidiService.Device device = this.mDevicesByServer.get(server.asBinder());
            if (device != null) {
                device.updateTotalBytes(totalInputBytes, totalOutputBytes);
            }
        }
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, writer)) {
            com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
            pw.println("MIDI Manager State:");
            pw.increaseIndent();
            pw.println("Devices:");
            pw.increaseIndent();
            synchronized (this.mDevicesByInfo) {
                for (com.android.server.midi.MidiService.Device device : this.mDevicesByInfo.values()) {
                    pw.println(device.toString());
                }
            }
            pw.decreaseIndent();
            pw.println("Clients:");
            pw.increaseIndent();
            synchronized (this.mClients) {
                for (com.android.server.midi.MidiService.Client client : this.mClients.values()) {
                    pw.println(client.toString());
                }
            }
            pw.decreaseIndent();
        }
    }

    private boolean isUsbMidiDeviceInUseLocked(android.media.midi.MidiDeviceInfo info) {
        java.lang.String name = info.getProperties().getString("name");
        if (name.length() < MIDI_LEGACY_STRING.length()) {
            return false;
        }
        java.lang.String deviceName = extractUsbDeviceName(name);
        java.lang.String tagName = extractUsbDeviceTag(name);
        android.util.Log.i(TAG, "Checking " + deviceName + " " + tagName);
        if (this.mUsbMidiUniversalDeviceInUse.contains(deviceName)) {
            return true;
        }
        return tagName.equals(MIDI_UNIVERSAL_STRING) && this.mUsbMidiLegacyDeviceOpenCount.containsKey(deviceName);
    }

    void addUsbMidiDeviceLocked(android.media.midi.MidiDeviceInfo info) {
        java.lang.String name = info.getProperties().getString("name");
        if (name.length() < MIDI_LEGACY_STRING.length()) {
            return;
        }
        java.lang.String deviceName = extractUsbDeviceName(name);
        java.lang.String tagName = extractUsbDeviceTag(name);
        android.util.Log.i(TAG, "Adding " + deviceName + " " + tagName);
        if (tagName.equals(MIDI_UNIVERSAL_STRING)) {
            this.mUsbMidiUniversalDeviceInUse.add(deviceName);
        } else if (tagName.equals(MIDI_LEGACY_STRING)) {
            int count = this.mUsbMidiLegacyDeviceOpenCount.getOrDefault(deviceName, 0).intValue() + 1;
            this.mUsbMidiLegacyDeviceOpenCount.put(deviceName, java.lang.Integer.valueOf(count));
        }
    }

    void removeUsbMidiDeviceLocked(android.media.midi.MidiDeviceInfo info) {
        java.lang.String name = info.getProperties().getString("name");
        if (name.length() < MIDI_LEGACY_STRING.length()) {
            return;
        }
        java.lang.String deviceName = extractUsbDeviceName(name);
        java.lang.String tagName = extractUsbDeviceTag(name);
        android.util.Log.i(TAG, "Removing " + deviceName + " " + tagName);
        if (tagName.equals(MIDI_UNIVERSAL_STRING)) {
            this.mUsbMidiUniversalDeviceInUse.remove(deviceName);
            return;
        }
        if (tagName.equals(MIDI_LEGACY_STRING) && this.mUsbMidiLegacyDeviceOpenCount.containsKey(deviceName)) {
            int count = this.mUsbMidiLegacyDeviceOpenCount.get(deviceName).intValue();
            if (count > 1) {
                this.mUsbMidiLegacyDeviceOpenCount.put(deviceName, java.lang.Integer.valueOf(count - 1));
            } else {
                this.mUsbMidiLegacyDeviceOpenCount.remove(deviceName);
            }
        }
    }

    java.lang.String extractUsbDeviceName(java.lang.String propertyName) {
        return propertyName.substring(0, propertyName.length() - MIDI_LEGACY_STRING.length());
    }

    java.lang.String extractUsbDeviceTag(java.lang.String propertyName) {
        return propertyName.substring(propertyName.length() - MIDI_LEGACY_STRING.length());
    }

    private int getCallingUserId() {
        return android.os.UserHandle.getUserId(android.os.Binder.getCallingUid());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUmpDevice(android.media.midi.MidiDeviceInfo deviceInfo) {
        return deviceInfo.getDefaultProtocol() != -1;
    }
}
