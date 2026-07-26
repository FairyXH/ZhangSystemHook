package com.oplus.theia.saupwk;

/* JADX INFO: loaded from: classes3.dex */
public class SaupwkManager {
    static final java.lang.String TAG = "SAUPWK";
    private static com.oplus.theia.saupwk.SaupwkManager sInstance = null;
    android.os.Handler mHandler = new android.os.Handler();

    public static synchronized com.oplus.theia.saupwk.SaupwkManager getInstance() {
        if (sInstance == null) {
            sInstance = new com.oplus.theia.saupwk.SaupwkManager();
        }
        return sInstance;
    }

    public void saupwkLogDumpTrigger() {
        java.lang.String saupwk_en = android.os.SystemProperties.get("persist.sys.saupwk_en", "");
        if (saupwk_en.equals("1")) {
            java.util.Calendar currentTime = java.util.Calendar.getInstance();
            java.lang.StringBuilder currentDateStr = new java.lang.StringBuilder();
            currentDateStr.append(currentTime.get(1)).append("-").append(currentTime.get(2)).append("-").append(currentTime.get(5)).append(" ").append(currentTime.get(11)).append(":").append(currentTime.get(12)).append(":").append(currentTime.get(13));
            java.lang.String date = currentDateStr.toString();
            android.os.SystemProperties.set("sys.bootfinish.timestamp", date);
            android.util.Slog.w(TAG, "[SAUPWK]: marking sys.bootfinish.timestamp as " + date);
            this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.oplus.theia.saupwk.SaupwkManager.1
                @Override // java.lang.Runnable
                public void run() {
                    java.lang.String reason = android.os.SystemProperties.get("persist.sys.rbsreason", "na");
                    android.os.SystemProperties.set("sys.sr.reboot_reason", reason);
                    android.os.SystemProperties.set("persist.sys.rbsreason", "");
                    java.lang.String rk = android.os.SystemProperties.get("persist.sys.saupwknum.rk", "0");
                    java.lang.String ru = android.os.SystemProperties.get("persist.sys.saupwknum.ru", "0");
                    java.lang.String nk = android.os.SystemProperties.get("persist.sys.saupwknum.nk", "0");
                    java.lang.String nu = android.os.SystemProperties.get("persist.sys.saupwknum.nu", "0");
                    android.util.Slog.d(com.oplus.theia.saupwk.SaupwkManager.TAG, "[SAUPWK]: rk:" + rk + " ru:" + ru + " nk:" + nk + " nu:" + nu + " rbsreason:" + reason);
                    int numpwk = java.lang.Integer.parseInt(rk) + java.lang.Integer.parseInt(ru) + java.lang.Integer.parseInt(nk) + java.lang.Integer.parseInt(nu);
                    android.os.SystemProperties.set("sys.saupwknum", java.lang.String.valueOf(numpwk));
                    android.os.SystemProperties.set("persist.sys.saupwknum.rk", "0");
                    android.os.SystemProperties.set("persist.sys.saupwknum.ru", "0");
                    android.os.SystemProperties.set("persist.sys.saupwknum.nk", "0");
                    android.os.SystemProperties.set("persist.sys.saupwknum.nu", "0");
                    if (numpwk > 0) {
                        android.util.Slog.d(com.oplus.theia.saupwk.SaupwkManager.TAG, "[SAUPWK]: setting sys.saupwk.logdump to true ...\n");
                        android.os.SystemProperties.set("sys.saupwk.logdump", "true");
                    } else {
                        android.util.Slog.d(com.oplus.theia.saupwk.SaupwkManager.TAG, "[SAUPWK]: none power key detected ....\n");
                    }
                }
            }, 10000L);
        }
    }

    public static void saupwkLogDumpTrigger(android.os.Handler handler) {
        java.lang.String saupwk_en = android.os.SystemProperties.get("persist.sys.saupwk_en", "");
        if (saupwk_en.equals("1")) {
            java.util.Calendar currentTime = java.util.Calendar.getInstance();
            java.lang.StringBuilder currentDateStr = new java.lang.StringBuilder();
            currentDateStr.append(currentTime.get(1)).append("-").append(currentTime.get(2)).append("-").append(currentTime.get(5)).append(" ").append(currentTime.get(11)).append(":").append(currentTime.get(12)).append(":").append(currentTime.get(13));
            java.lang.String date = currentDateStr.toString();
            android.os.SystemProperties.set("sys.bootfinish.timestamp", date);
            android.util.Slog.w(TAG, "[SAUPWK]: marking sys.bootfinish.timestamp as " + date);
            handler.postDelayed(new java.lang.Runnable() { // from class: com.oplus.theia.saupwk.SaupwkManager.2
                @Override // java.lang.Runnable
                public void run() {
                    java.lang.String reason = android.os.SystemProperties.get("persist.sys.rbsreason", "na");
                    android.os.SystemProperties.set("sys.sr.reboot_reason", reason);
                    android.os.SystemProperties.set("persist.sys.rbsreason", "");
                    java.lang.String rk = android.os.SystemProperties.get("persist.sys.saupwknum.rk", "0");
                    java.lang.String ru = android.os.SystemProperties.get("persist.sys.saupwknum.ru", "0");
                    java.lang.String nk = android.os.SystemProperties.get("persist.sys.saupwknum.nk", "0");
                    java.lang.String nu = android.os.SystemProperties.get("persist.sys.saupwknum.nu", "0");
                    android.util.Slog.d(com.oplus.theia.saupwk.SaupwkManager.TAG, "[SAUPWK]: rk:" + rk + " ru:" + ru + " nk:" + nk + " nu:" + nu + " rbsreason:" + reason);
                    int numpwk = java.lang.Integer.parseInt(rk) + java.lang.Integer.parseInt(ru) + java.lang.Integer.parseInt(nk) + java.lang.Integer.parseInt(nu);
                    android.os.SystemProperties.set("sys.saupwknum", java.lang.String.valueOf(numpwk));
                    android.os.SystemProperties.set("persist.sys.saupwknum.rk", "0");
                    android.os.SystemProperties.set("persist.sys.saupwknum.ru", "0");
                    android.os.SystemProperties.set("persist.sys.saupwknum.nk", "0");
                    android.os.SystemProperties.set("persist.sys.saupwknum.nu", "0");
                    if (numpwk > 0) {
                        android.util.Slog.d(com.oplus.theia.saupwk.SaupwkManager.TAG, "[SAUPWK]: setting sys.saupwk.logdump to true ...\n");
                        android.os.SystemProperties.set("sys.saupwk.logdump", "true");
                    } else {
                        android.util.Slog.d(com.oplus.theia.saupwk.SaupwkManager.TAG, "[SAUPWK]: none power key detected ....\n");
                    }
                }
            }, 10000L);
        }
    }

    public static void saupwkStaticEnterSR(java.lang.String reason) {
        java.lang.String saupwk_en = android.os.SystemProperties.get("persist.sys.saupwk_en", "");
        if (saupwk_en.equals("1")) {
            if (reason.equals("sau") || reason.equals("silence")) {
                java.util.Calendar currentTime = java.util.Calendar.getInstance();
                java.lang.StringBuilder currentDateStr = new java.lang.StringBuilder();
                currentDateStr.append(currentTime.get(1)).append("-").append(currentTime.get(2)).append("-").append(currentTime.get(5)).append(" ").append(currentTime.get(11)).append(":").append(currentTime.get(12)).append(":").append(currentTime.get(13));
                java.lang.String date = currentDateStr.toString();
                android.os.SystemProperties.set("persist.sys.sr_start", date);
                java.lang.String from = android.os.SystemProperties.get("ro.build.version.ota", "na");
                android.os.SystemProperties.set("persist.sys.sau_from_ver", from);
                android.util.Slog.w(TAG, "[SAUPWK]: sau START from " + from + "@" + date + ", reason=" + (reason.equals("silence") ? "slc" : "sau"));
                android.os.SystemProperties.set("persist.sys.rbsreason", reason.equals("silence") ? "slc" : "sau");
                java.lang.String sr_reason = android.os.SystemProperties.get("persist.sys.rbsreason", "na");
                android.util.Slog.w(TAG, "[SAUPWK]: persist.sys.rbsreason:" + sr_reason);
            }
        }
    }

    public void saupwkEnterSR(java.lang.String reason) {
        java.lang.String saupwk_en = android.os.SystemProperties.get("persist.sys.saupwk_en", "");
        if (saupwk_en.equals("1")) {
            if (reason.equals("sau") || reason.equals("silence")) {
                java.util.Calendar currentTime = java.util.Calendar.getInstance();
                java.lang.StringBuilder currentDateStr = new java.lang.StringBuilder();
                currentDateStr.append(currentTime.get(1)).append("-").append(currentTime.get(2)).append("-").append(currentTime.get(5)).append(" ").append(currentTime.get(11)).append(":").append(currentTime.get(12)).append(":").append(currentTime.get(13));
                java.lang.String date = currentDateStr.toString();
                android.os.SystemProperties.set("persist.sys.sr_start", date);
                java.lang.String from = android.os.SystemProperties.get("ro.build.version.ota", "na");
                android.os.SystemProperties.set("persist.sys.sau_from_ver", from);
                android.util.Slog.w(TAG, "[SAUPWK]: sau START from " + from + "@" + date + ", reason=" + (reason.equals("silence") ? "slc" : "sau"));
                android.os.SystemProperties.set("persist.sys.rbsreason", reason.equals("silence") ? "slc" : "sau");
                java.lang.String sr_reason = android.os.SystemProperties.get("persist.sys.rbsreason", "na");
                android.util.Slog.w(TAG, "[SAUPWK]: persist.sys.rbsreason:" + sr_reason);
            }
        }
    }

    public void saupwkMarkSlsauEnd() {
        java.lang.String saupwk_en = android.os.SystemProperties.get("persist.sys.saupwk_en", "");
        if (saupwk_en.equals("1")) {
            java.util.Calendar currentTime = java.util.Calendar.getInstance();
            java.lang.StringBuilder currentDateStr = new java.lang.StringBuilder();
            currentDateStr.append(currentTime.get(1)).append("-").append(currentTime.get(2)).append("-").append(currentTime.get(5)).append(" ").append(currentTime.get(11)).append(":").append(currentTime.get(12)).append(":").append(currentTime.get(13));
            java.lang.String date = currentDateStr.toString();
            android.os.SystemProperties.set("persist.sys.sr_end", date);
            android.util.Slog.w(TAG, "[SAUPWK]: marking persist.sys.sr_end as " + date);
            java.lang.String to = android.os.SystemProperties.get("ro.build.version.ota", "na");
            android.os.SystemProperties.set("persist.sys.sau_to_ver", to);
            android.util.Slog.w(TAG, "[SAUPWK]: sau END with " + to + "@ " + date);
            java.lang.String str_old = android.os.SystemProperties.get("sys.slsau_finished", "");
            android.os.SystemProperties.set("sys.slsau_finished", "true");
            java.lang.String str_new = android.os.SystemProperties.get("sys.slsau_finished", "");
            android.util.Slog.d(TAG, "[SAUPWK]: setting property sys.slsau_finished:" + str_old + " to " + str_new);
        }
    }
}
