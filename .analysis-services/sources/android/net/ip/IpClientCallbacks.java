package android.net.ip;

/* JADX INFO: loaded from: classes.dex */
public class IpClientCallbacks {
    public void onIpClientCreated(android.net.ip.IIpClient ipClient) {
    }

    public void onPreDhcpAction() {
    }

    public void onPostDhcpAction() {
    }

    public void onNewDhcpResults(android.net.DhcpResultsParcelable dhcpResults) {
    }

    public void onProvisioningSuccess(android.net.LinkProperties newLp) {
    }

    public void onProvisioningFailure(android.net.LinkProperties newLp) {
    }

    public void onLinkPropertiesChange(android.net.LinkProperties newLp) {
    }

    public void onReachabilityLost(java.lang.String logMsg) {
    }

    public void onQuit() {
    }

    public void installPacketFilter(byte[] filter) {
    }

    public void startReadPacketFilter() {
    }

    public void setFallbackMulticastFilter(boolean enabled) {
    }

    public void setNeighborDiscoveryOffload(boolean enable) {
    }

    public void onPreconnectionStart(java.util.List<android.net.Layer2PacketParcelable> packets) {
    }

    public void onReachabilityFailure(android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable lossInfo) {
        onReachabilityLost(lossInfo.message);
    }

    public void setMaxDtimMultiplier(int multiplier) {
    }
}
