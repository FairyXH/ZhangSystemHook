package android.net.shared;

/* JADX INFO: loaded from: classes.dex */
public class PrivateDnsConfig {
    public final java.net.InetAddress[] dohIps;
    public final java.lang.String dohName;
    public final java.lang.String dohPath;
    public final int dohPort;
    public final java.lang.String hostname;
    public final java.net.InetAddress[] ips;
    public final int mode;

    public PrivateDnsConfig() {
        this(false);
    }

    public PrivateDnsConfig(boolean useTls) {
        this(useTls ? 2 : 1, null, null, null, null, null, -1);
    }

    public PrivateDnsConfig(java.lang.String hostname, java.net.InetAddress[] ips) {
        this(android.text.TextUtils.isEmpty(hostname) ? 1 : 3, hostname, ips, null, null, null, -1);
    }

    public PrivateDnsConfig(int mode, java.lang.String hostname, java.net.InetAddress[] ips, java.lang.String dohName, java.net.InetAddress[] dohIps, java.lang.String dohPath, int dohPort) {
        this.mode = mode;
        this.hostname = hostname != null ? hostname : "";
        this.ips = ips != null ? (java.net.InetAddress[]) ips.clone() : new java.net.InetAddress[0];
        this.dohName = dohName != null ? dohName : "";
        this.dohIps = dohIps != null ? (java.net.InetAddress[]) dohIps.clone() : new java.net.InetAddress[0];
        this.dohPath = dohPath != null ? dohPath : "";
        this.dohPort = dohPort;
    }

    public PrivateDnsConfig(android.net.shared.PrivateDnsConfig cfg) {
        this.mode = cfg.mode;
        this.hostname = cfg.hostname;
        this.ips = cfg.ips;
        this.dohName = cfg.dohName;
        this.dohIps = cfg.dohIps;
        this.dohPath = cfg.dohPath;
        this.dohPort = cfg.dohPort;
    }

    public boolean inStrictMode() {
        return this.mode == 3;
    }

    public boolean inOpportunisticMode() {
        return this.mode == 2;
    }

    public java.lang.String toString() {
        return android.net.shared.PrivateDnsConfig.class.getSimpleName() + "{" + modeAsString(this.mode) + ":" + this.hostname + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + java.util.Arrays.toString(this.ips) + ", dohName=" + this.dohName + ", dohIps=" + java.util.Arrays.toString(this.dohIps) + ", dohPath=" + this.dohPath + ", dohPort=" + this.dohPort + "}";
    }

    private static java.lang.String modeAsString(int mode) {
        switch (mode) {
            case 1:
                return kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF;
            case 2:
                return "opportunistic";
            case 3:
                return "strict";
            default:
                return "unknown";
        }
    }

    public android.net.PrivateDnsConfigParcel toParcel() {
        android.net.PrivateDnsConfigParcel parcel = new android.net.PrivateDnsConfigParcel();
        parcel.hostname = this.hostname;
        parcel.ips = (java.lang.String[]) android.net.shared.ParcelableUtil.toParcelableArray(java.util.Arrays.asList(this.ips), new android.net.shared.InitialConfiguration$$ExternalSyntheticLambda11(), java.lang.String.class);
        parcel.privateDnsMode = this.mode;
        parcel.dohName = this.dohName;
        parcel.dohIps = (java.lang.String[]) android.net.shared.ParcelableUtil.toParcelableArray(java.util.Arrays.asList(this.dohIps), new android.net.shared.InitialConfiguration$$ExternalSyntheticLambda11(), java.lang.String.class);
        parcel.dohPath = this.dohPath;
        parcel.dohPort = this.dohPort;
        return parcel;
    }

    public static android.net.shared.PrivateDnsConfig fromParcel(android.net.PrivateDnsConfigParcel parcel) {
        java.net.InetAddress[] ips = (java.net.InetAddress[]) android.net.shared.ParcelableUtil.fromParcelableArray(parcel.ips, new android.net.shared.InitialConfiguration$$ExternalSyntheticLambda8()).toArray(new java.net.InetAddress[parcel.ips.length]);
        if (parcel.privateDnsMode == -1) {
            return new android.net.shared.PrivateDnsConfig(parcel.hostname, ips);
        }
        java.net.InetAddress[] dohIps = new java.net.InetAddress[parcel.dohIps.length];
        return new android.net.shared.PrivateDnsConfig(parcel.privateDnsMode, parcel.hostname, ips, parcel.dohName, (java.net.InetAddress[]) android.net.shared.ParcelableUtil.fromParcelableArray(parcel.dohIps, new android.net.shared.InitialConfiguration$$ExternalSyntheticLambda8()).toArray(dohIps), parcel.dohPath, parcel.dohPort);
    }
}
