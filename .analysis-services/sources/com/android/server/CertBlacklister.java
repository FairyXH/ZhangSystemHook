package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class CertBlacklister extends android.os.Binder {
    public static final java.lang.String PUBKEY_BLACKLIST_KEY = "pubkey_blacklist";
    public static final java.lang.String SERIAL_BLACKLIST_KEY = "serial_blacklist";
    private static final java.lang.String TAG = "CertBlacklister";
    private static final java.lang.String DENYLIST_ROOT = java.lang.System.getenv("ANDROID_DATA") + "/misc/keychain/";
    public static final java.lang.String PUBKEY_PATH = DENYLIST_ROOT + "pubkey_blacklist.txt";
    public static final java.lang.String SERIAL_PATH = DENYLIST_ROOT + "serial_blacklist.txt";

    private static class BlacklistObserver extends android.database.ContentObserver {
        private final android.content.ContentResolver mContentResolver;
        private final java.lang.String mKey;
        private final java.lang.String mName;
        private final java.lang.String mPath;
        private final java.io.File mTmpDir;

        public BlacklistObserver(java.lang.String key, java.lang.String name, java.lang.String path, android.content.ContentResolver cr) {
            super(null);
            this.mKey = key;
            this.mName = name;
            this.mPath = path;
            this.mTmpDir = new java.io.File(this.mPath).getParentFile();
            this.mContentResolver = cr;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            writeDenylist();
        }

        public java.lang.String getValue() {
            return android.provider.Settings.Secure.getString(this.mContentResolver, this.mKey);
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.CertBlacklister$BlacklistObserver$1] */
        private void writeDenylist() {
            new java.lang.Thread("BlacklistUpdater") { // from class: com.android.server.CertBlacklister.BlacklistObserver.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    synchronized (com.android.server.CertBlacklister.BlacklistObserver.this.mTmpDir) {
                        java.lang.String blacklist = com.android.server.CertBlacklister.BlacklistObserver.this.getValue();
                        if (blacklist != null) {
                            android.util.Slog.i(com.android.server.CertBlacklister.TAG, "Certificate blacklist changed, updating...");
                            java.io.FileOutputStream out = null;
                            try {
                                try {
                                    java.io.File tmp = java.io.File.createTempFile("journal", "", com.android.server.CertBlacklister.BlacklistObserver.this.mTmpDir);
                                    tmp.setReadable(true, false);
                                    out = new java.io.FileOutputStream(tmp);
                                    out.write(blacklist.getBytes());
                                    android.os.FileUtils.sync(out);
                                    tmp.renameTo(new java.io.File(com.android.server.CertBlacklister.BlacklistObserver.this.mPath));
                                    android.util.Slog.i(com.android.server.CertBlacklister.TAG, "Certificate blacklist updated");
                                } catch (java.io.IOException e) {
                                    android.util.Slog.e(com.android.server.CertBlacklister.TAG, "Failed to write blacklist", e);
                                }
                                libcore.io.IoUtils.closeQuietly(out);
                            } catch (java.lang.Throwable th) {
                                libcore.io.IoUtils.closeQuietly(out);
                                throw th;
                            }
                        }
                    }
                }
            }.start();
        }
    }

    public CertBlacklister(android.content.Context context) {
        registerObservers(context.getContentResolver());
    }

    private com.android.server.CertBlacklister.BlacklistObserver buildPubkeyObserver(android.content.ContentResolver cr) {
        return new com.android.server.CertBlacklister.BlacklistObserver(PUBKEY_BLACKLIST_KEY, "pubkey", PUBKEY_PATH, cr);
    }

    private com.android.server.CertBlacklister.BlacklistObserver buildSerialObserver(android.content.ContentResolver cr) {
        return new com.android.server.CertBlacklister.BlacklistObserver(SERIAL_BLACKLIST_KEY, "serial", SERIAL_PATH, cr);
    }

    private void registerObservers(android.content.ContentResolver cr) {
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor(PUBKEY_BLACKLIST_KEY), true, buildPubkeyObserver(cr));
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor(SERIAL_BLACKLIST_KEY), true, buildSerialObserver(cr));
    }
}
