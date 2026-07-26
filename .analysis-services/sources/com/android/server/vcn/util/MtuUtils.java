package com.android.server.vcn.util;

/* JADX INFO: loaded from: classes3.dex */
public class MtuUtils {
    private static final java.util.Map<java.lang.Integer, java.lang.Integer> AUTHCRYPT_ALGORITHM_OVERHEAD;
    private static final java.util.Map<java.lang.Integer, java.lang.Integer> AUTH_ALGORITHM_OVERHEAD;
    private static final java.util.Map<java.lang.Integer, java.lang.Integer> CRYPT_ALGORITHM_OVERHEAD;
    private static final int GENERIC_ESP_OVERHEAD_MAX_V4 = 78;
    private static final int GENERIC_ESP_OVERHEAD_MAX_V6 = 50;
    private static final java.lang.String TAG = com.android.server.vcn.util.MtuUtils.class.getSimpleName();

    static {
        java.util.Map<java.lang.Integer, java.lang.Integer> map = new android.util.ArrayMap<>();
        map.put(0, 0);
        map.put(2, 12);
        map.put(5, 12);
        map.put(12, 32);
        map.put(13, 48);
        map.put(14, 64);
        map.put(8, 12);
        AUTH_ALGORITHM_OVERHEAD = java.util.Collections.unmodifiableMap(map);
        java.util.Map<java.lang.Integer, java.lang.Integer> map2 = new android.util.ArrayMap<>();
        map2.put(3, 15);
        map2.put(12, 31);
        map2.put(13, 11);
        CRYPT_ALGORITHM_OVERHEAD = java.util.Collections.unmodifiableMap(map2);
        java.util.Map<java.lang.Integer, java.lang.Integer> map3 = new android.util.ArrayMap<>();
        map3.put(18, 19);
        map3.put(19, 23);
        map3.put(20, 27);
        map3.put(28, 27);
        AUTHCRYPT_ALGORITHM_OVERHEAD = java.util.Collections.unmodifiableMap(map3);
    }

    public static int getMtu(java.util.List<android.net.ipsec.ike.ChildSaProposal> childProposals, int maxMtu, int underlyingMtu, boolean isIpv4) {
        if (underlyingMtu <= 0) {
            return 1280;
        }
        int maxAuthOverhead = 0;
        int maxCryptOverhead = 0;
        int maxAuthCryptOverhead = 0;
        for (android.net.ipsec.ike.ChildSaProposal proposal : childProposals) {
            for (android.util.Pair<java.lang.Integer, java.lang.Integer> encryptionAlgoPair : proposal.getEncryptionAlgorithms()) {
                int algo = ((java.lang.Integer) encryptionAlgoPair.first).intValue();
                if (AUTHCRYPT_ALGORITHM_OVERHEAD.containsKey(java.lang.Integer.valueOf(algo))) {
                    maxAuthCryptOverhead = java.lang.Math.max(maxAuthCryptOverhead, AUTHCRYPT_ALGORITHM_OVERHEAD.get(java.lang.Integer.valueOf(algo)).intValue());
                } else if (CRYPT_ALGORITHM_OVERHEAD.containsKey(java.lang.Integer.valueOf(algo))) {
                    maxCryptOverhead = java.lang.Math.max(maxCryptOverhead, CRYPT_ALGORITHM_OVERHEAD.get(java.lang.Integer.valueOf(algo)).intValue());
                } else {
                    android.util.Slog.wtf(TAG, "Unknown encryption algorithm requested: " + algo);
                    return 1280;
                }
            }
            java.util.Iterator<java.lang.Integer> it = proposal.getIntegrityAlgorithms().iterator();
            while (it.hasNext()) {
                int algo2 = it.next().intValue();
                if (AUTH_ALGORITHM_OVERHEAD.containsKey(java.lang.Integer.valueOf(algo2))) {
                    maxAuthOverhead = java.lang.Math.max(maxAuthOverhead, AUTH_ALGORITHM_OVERHEAD.get(java.lang.Integer.valueOf(algo2)).intValue());
                } else {
                    android.util.Slog.wtf(TAG, "Unknown integrity algorithm requested: " + algo2);
                    return 1280;
                }
            }
        }
        int genericEspOverheadMax = isIpv4 ? 78 : 50;
        int combinedModeMtu = (underlyingMtu - maxAuthCryptOverhead) - genericEspOverheadMax;
        int normalModeMtu = ((underlyingMtu - maxCryptOverhead) - maxAuthOverhead) - genericEspOverheadMax;
        return java.lang.Math.min(java.lang.Math.min(maxMtu, combinedModeMtu), normalModeMtu);
    }
}
