package android.net.shared;

/* JADX INFO: loaded from: classes.dex */
public final class IpConfigurationParcelableUtil {
    public static java.lang.String parcelAddress(java.net.InetAddress addr) {
        if (addr == null) {
            return null;
        }
        return addr.getHostAddress();
    }

    public static java.net.InetAddress unparcelAddress(java.lang.String addr) {
        if (addr == null) {
            return null;
        }
        return android.net.InetAddresses.parseNumericAddress(addr);
    }
}
