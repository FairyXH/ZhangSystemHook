package com.android.server.backup;

/* JADX INFO: loaded from: classes.dex */
public class BackupUtils {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "BackupUtils";

    public static boolean signaturesMatch(java.util.ArrayList<byte[]> storedSigHashes, android.content.pm.PackageInfo target, android.content.pm.PackageManagerInternal pmi) {
        if (target == null || target.packageName == null) {
            return false;
        }
        if ((target.applicationInfo.flags & 1) != 0) {
            return true;
        }
        if (com.android.internal.util.ArrayUtils.isEmpty(storedSigHashes)) {
            return false;
        }
        android.content.pm.SigningInfo signingInfo = target.signingInfo;
        if (signingInfo == null) {
            android.util.Slog.w(TAG, "signingInfo is empty, app was either unsigned or the flag PackageManager#GET_SIGNING_CERTIFICATES was not specified");
            return false;
        }
        int nStored = storedSigHashes.size();
        if (nStored == 1) {
            return pmi.isDataRestoreSafe(storedSigHashes.get(0), target.packageName);
        }
        java.util.ArrayList<byte[]> deviceHashes = hashSignatureArray(signingInfo.getApkContentsSigners());
        int nDevice = deviceHashes.size();
        for (int i = 0; i < nStored; i++) {
            boolean match = false;
            int j = 0;
            while (true) {
                if (j >= nDevice) {
                    break;
                }
                if (!java.util.Arrays.equals(storedSigHashes.get(i), deviceHashes.get(j))) {
                    j++;
                } else {
                    match = true;
                    break;
                }
            }
            if (!match) {
                return false;
            }
        }
        return true;
    }

    public static byte[] hashSignature(byte[] signature) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            digest.update(signature);
            return digest.digest();
        } catch (java.security.NoSuchAlgorithmException e) {
            android.util.Slog.w(TAG, "No SHA-256 algorithm found!");
            return null;
        }
    }

    public static byte[] hashSignature(android.content.pm.Signature signature) {
        return hashSignature(signature.toByteArray());
    }

    public static java.util.ArrayList<byte[]> hashSignatureArray(android.content.pm.Signature[] sigs) {
        if (sigs == null) {
            return null;
        }
        java.util.ArrayList<byte[]> hashes = new java.util.ArrayList<>(sigs.length);
        for (android.content.pm.Signature s : sigs) {
            hashes.add(hashSignature(s));
        }
        return hashes;
    }

    public static java.util.ArrayList<byte[]> hashSignatureArray(java.util.List<byte[]> sigs) {
        if (sigs == null) {
            return null;
        }
        java.util.ArrayList<byte[]> hashes = new java.util.ArrayList<>(sigs.size());
        for (byte[] s : sigs) {
            hashes.add(hashSignature(s));
        }
        return hashes;
    }
}
