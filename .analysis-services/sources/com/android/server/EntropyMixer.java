package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class EntropyMixer extends android.os.Binder {
    static final java.lang.String DEVICE_SPECIFIC_INFO_HEADER = "Copyright (C) 2009 The Android Open Source Project\nAll Your Randomness Are Belong To Us\n";
    static final int SEED_FILE_SIZE = 512;
    private static final int SEED_UPDATE_PERIOD = 10800000;
    private static final java.lang.String TAG = "EntropyMixer";
    private static final int UPDATE_SEED_MSG = 1;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    private final android.os.Handler mHandler;
    private final java.io.File randomReadDevice;
    private final java.io.File randomWriteDevice;
    private final android.util.AtomicFile seedFile;
    private static final long START_TIME = java.lang.System.currentTimeMillis();
    private static final long START_NANOTIME = java.lang.System.nanoTime();

    public EntropyMixer(android.content.Context context) {
        this(context, new java.io.File(getSystemDir(), "entropy.dat"), new java.io.File("/dev/urandom"), new java.io.File("/dev/urandom"));
    }

    EntropyMixer(android.content.Context context, java.io.File seedFile, java.io.File randomReadDevice, java.io.File randomWriteDevice) {
        this.mHandler = new android.os.Handler(com.android.server.IoThread.getHandler().getLooper()) { // from class: com.android.server.EntropyMixer.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                if (msg.what != 1) {
                    android.util.Slog.e(com.android.server.EntropyMixer.TAG, "Will not process invalid message");
                } else {
                    com.android.server.EntropyMixer.this.updateSeedFile();
                    com.android.server.EntropyMixer.this.scheduleSeedUpdater();
                }
            }
        };
        this.mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.EntropyMixer.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.EntropyMixer.this.updateSeedFile();
            }
        };
        this.seedFile = new android.util.AtomicFile((java.io.File) com.android.internal.util.Preconditions.checkNotNull(seedFile));
        this.randomReadDevice = (java.io.File) com.android.internal.util.Preconditions.checkNotNull(randomReadDevice);
        this.randomWriteDevice = (java.io.File) com.android.internal.util.Preconditions.checkNotNull(randomWriteDevice);
        loadInitialEntropy();
        updateSeedFile();
        scheduleSeedUpdater();
        android.content.IntentFilter broadcastFilter = new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN");
        broadcastFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        broadcastFilter.addAction("android.intent.action.REBOOT");
        context.registerReceiver(this.mBroadcastReceiver, broadcastFilter, null, this.mHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleSeedUpdater() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessageDelayed(1, 10800000L);
    }

    private void loadInitialEntropy() {
        byte[] seed = readSeedFile();
        try {
            java.io.FileOutputStream out = new java.io.FileOutputStream(this.randomWriteDevice);
            try {
                if (seed.length != 0) {
                    out.write(seed);
                    android.util.Slog.i(TAG, "Loaded existing seed file");
                }
                out.write(getDeviceSpecificInformation());
                out.close();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error writing to " + this.randomWriteDevice, e);
        }
    }

    private byte[] readSeedFile() {
        try {
            return this.seedFile.readFully();
        } catch (java.io.FileNotFoundException e) {
            return new byte[0];
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Error reading " + this.seedFile.getBaseFile(), e2);
            return new byte[0];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:21:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00a4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void updateSeedFile() {
        /*
            r8 = this;
            java.lang.String r0 = "EntropyMixer"
            byte[] r1 = r8.readSeedFile()
            r2 = 512(0x200, float:7.17E-43)
            byte[] r2 = new byte[r2]
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.io.IOException -> L2f
            java.io.File r4 = r8.randomReadDevice     // Catch: java.io.IOException -> L2f
            r3.<init>(r4)     // Catch: java.io.IOException -> L2f
            int r4 = r3.read(r2)     // Catch: java.lang.Throwable -> L25
            int r5 = r2.length     // Catch: java.lang.Throwable -> L25
            if (r4 != r5) goto L1c
            r3.close()     // Catch: java.io.IOException -> L2f
            goto L4e
        L1c:
            java.io.IOException r4 = new java.io.IOException     // Catch: java.lang.Throwable -> L25
            java.lang.String r5 = "unexpected EOF"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L25
            throw r4     // Catch: java.lang.Throwable -> L25
        L25:
            r4 = move-exception
            r3.close()     // Catch: java.lang.Throwable -> L2a
            goto L2e
        L2a:
            r5 = move-exception
            r4.addSuppressed(r5)     // Catch: java.io.IOException -> L2f
        L2e:
            throw r4     // Catch: java.io.IOException -> L2f
        L2f:
            r3 = move-exception
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Error reading "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.io.File r5 = r8.randomReadDevice
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "; seed file won't be properly updated"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.util.Slog.e(r0, r4, r3)
        L4e:
            java.lang.String r3 = "SHA-256"
            java.security.MessageDigest r3 = java.security.MessageDigest.getInstance(r3)     // Catch: java.security.NoSuchAlgorithmException -> Laa
            java.lang.String r4 = "Android EntropyMixer v1"
            byte[] r4 = r4.getBytes()
            r3.update(r4)
            long r4 = java.lang.System.currentTimeMillis()
            byte[] r4 = longToBytes(r4)
            r3.update(r4)
            long r4 = java.lang.System.nanoTime()
            byte[] r4 = longToBytes(r4)
            r3.update(r4)
            int r4 = r1.length
            long r4 = (long) r4
            byte[] r4 = longToBytes(r4)
            r3.update(r4)
            r3.update(r1)
            int r4 = r2.length
            long r4 = (long) r4
            byte[] r4 = longToBytes(r4)
            r3.update(r4)
            r3.update(r2)
            byte[] r4 = r3.digest()
            int r5 = r2.length
            int r6 = r4.length
            int r5 = r5 - r6
            int r6 = r4.length
            r7 = 0
            java.lang.System.arraycopy(r4, r7, r2, r5, r6)
            r8.writeNewSeed(r2)
            int r5 = r1.length
            if (r5 != 0) goto La4
            java.lang.String r5 = "Created seed file"
            android.util.Slog.i(r0, r5)
            goto La9
        La4:
            java.lang.String r5 = "Updated seed file"
            android.util.Slog.i(r0, r5)
        La9:
            return
        Laa:
            r3 = move-exception
            java.lang.String r4 = "SHA-256 algorithm not found; seed file won't be updated"
            android.util.Slog.wtf(r0, r4, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.EntropyMixer.updateSeedFile():void");
    }

    private void writeNewSeed(byte[] newSeed) {
        java.io.FileOutputStream out = null;
        try {
            out = this.seedFile.startWrite();
            out.write(newSeed);
            this.seedFile.finishWrite(out);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error writing " + this.seedFile.getBaseFile(), e);
            this.seedFile.failWrite(out);
        }
    }

    private static byte[] longToBytes(long x) {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    private byte[] getDeviceSpecificInformation() {
        java.lang.StringBuilder b = new java.lang.StringBuilder();
        b.append(DEVICE_SPECIFIC_INFO_HEADER);
        b.append(START_TIME).append('\n');
        b.append(START_NANOTIME).append('\n');
        b.append(android.os.SystemProperties.get("ro.serialno")).append('\n');
        b.append(android.os.SystemProperties.get("ro.bootmode")).append('\n');
        b.append(android.os.SystemProperties.get("ro.baseband")).append('\n');
        b.append(android.os.SystemProperties.get("ro.carrier")).append('\n');
        b.append(android.os.SystemProperties.get("ro.bootloader")).append('\n');
        b.append(android.os.SystemProperties.get("ro.hardware")).append('\n');
        b.append(android.os.SystemProperties.get("ro.revision")).append('\n');
        b.append(android.os.SystemProperties.get("ro.build.fingerprint")).append('\n');
        b.append(new java.lang.Object().hashCode()).append('\n');
        b.append(java.lang.System.currentTimeMillis()).append('\n');
        b.append(java.lang.System.nanoTime()).append('\n');
        return b.toString().getBytes();
    }

    private static java.io.File getSystemDir() {
        java.io.File dataDir = android.os.Environment.getDataDirectory();
        java.io.File systemDir = new java.io.File(dataDir, "system");
        systemDir.mkdirs();
        return systemDir;
    }
}
