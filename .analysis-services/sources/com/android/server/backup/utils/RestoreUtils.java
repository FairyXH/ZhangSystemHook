package com.android.server.backup.utils;

/* JADX INFO: loaded from: classes.dex */
public class RestoreUtils {
    public static boolean installApk(java.io.InputStream instream, android.content.Context context, com.android.server.backup.restore.RestoreDeleteObserver deleteObserver, java.util.HashMap<java.lang.String, android.content.pm.Signature[]> manifestSignatures, java.util.HashMap<java.lang.String, com.android.server.backup.restore.RestorePolicy> packagePolicies, com.android.server.backup.FileMetadata info, java.lang.String installerPackageName, com.android.server.backup.utils.BytesReadListener bytesReadListener, int userId) {
        return installApk(instream, context, deleteObserver, manifestSignatures, packagePolicies, info, installerPackageName, bytesReadListener, userId, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:213:0x02d5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x031b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:227:? A[Catch: all -> 0x02df, SYNTHETIC, TRY_LEAVE, TryCatch #28 {all -> 0x02df, blocks: (B:144:0x02de, B:143:0x02db, B:139:0x02d5), top: B:213:0x02d5, inners: #24 }] */
    /* JADX WARN: Removed duplicated region for block: B:229:? A[Catch: Exception -> 0x0325, IOException -> 0x033e, SYNTHETIC, TRY_LEAVE, TryCatch #13 {Exception -> 0x0325, blocks: (B:160:0x0324, B:159:0x0321), top: B:197:0x0321 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:118:0x028e -> B:180:0x0290). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static boolean installApk(java.io.InputStream r23, android.content.Context r24, com.android.server.backup.restore.RestoreDeleteObserver r25, java.util.HashMap<java.lang.String, android.content.pm.Signature[]> r26, java.util.HashMap<java.lang.String, com.android.server.backup.restore.RestorePolicy> r27, com.android.server.backup.FileMetadata r28, java.lang.String r29, com.android.server.backup.utils.BytesReadListener r30, int r31, boolean r32) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 855
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.utils.RestoreUtils.installApk(java.io.InputStream, android.content.Context, com.android.server.backup.restore.RestoreDeleteObserver, java.util.HashMap, java.util.HashMap, com.android.server.backup.FileMetadata, java.lang.String, com.android.server.backup.utils.BytesReadListener, int, boolean):boolean");
    }

    private static class LocalIntentReceiver {
        private android.content.IIntentSender.Stub mLocalSender;
        private final java.lang.Object mLock;
        private android.content.Intent mResult;

        private LocalIntentReceiver() {
            this.mLock = new java.lang.Object();
            this.mResult = null;
            this.mLocalSender = new android.content.IIntentSender.Stub() { // from class: com.android.server.backup.utils.RestoreUtils.LocalIntentReceiver.1
                public void send(int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder whitelistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) {
                    synchronized (com.android.server.backup.utils.RestoreUtils.LocalIntentReceiver.this.mLock) {
                        com.android.server.backup.utils.RestoreUtils.LocalIntentReceiver.this.mResult = intent;
                        com.android.server.backup.utils.RestoreUtils.LocalIntentReceiver.this.mLock.notifyAll();
                    }
                }
            };
        }

        public android.content.IntentSender getIntentSender() {
            return new android.content.IntentSender(this.mLocalSender);
        }

        public android.content.Intent getResult() {
            android.content.Intent intent;
            synchronized (this.mLock) {
                while (this.mResult == null) {
                    try {
                        this.mLock.wait();
                    } catch (java.lang.InterruptedException e) {
                    }
                }
                intent = this.mResult;
            }
            return intent;
        }
    }
}
