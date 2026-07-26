package android.net.ip;

/* JADX INFO: loaded from: classes.dex */
public class IpClientUtil {
    public static final java.lang.String DUMP_ARG = "ipclient";

    public static class WaitForProvisioningCallbacks extends android.net.ip.IpClientCallbacks {
        private final android.os.ConditionVariable mCV = new android.os.ConditionVariable();
        private android.net.LinkProperties mCallbackLinkProperties;

        public android.net.LinkProperties waitForProvisioning() {
            this.mCV.block();
            return this.mCallbackLinkProperties;
        }

        @Override // android.net.ip.IpClientCallbacks
        public void onProvisioningSuccess(android.net.LinkProperties newLp) {
            this.mCallbackLinkProperties = newLp;
            this.mCV.open();
        }

        @Override // android.net.ip.IpClientCallbacks
        public void onProvisioningFailure(android.net.LinkProperties newLp) {
            this.mCallbackLinkProperties = null;
            this.mCV.open();
        }
    }

    public static void makeIpClient(android.content.Context context, java.lang.String ifName, android.net.ip.IpClientCallbacks callback) {
        android.net.networkstack.ModuleNetworkStackClient.getInstance(context).makeIpClient(ifName, new android.net.ip.IpClientUtil.IpClientCallbacksProxy(callback));
    }

    private static class IpClientCallbacksProxy extends android.net.ip.IIpClientCallbacks.Stub {
        protected final android.net.ip.IpClientCallbacks mCb;

        IpClientCallbacksProxy(android.net.ip.IpClientCallbacks cb) {
            this.mCb = cb;
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onIpClientCreated(android.net.ip.IIpClient ipClient) {
            this.mCb.onIpClientCreated(ipClient);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPreDhcpAction() {
            this.mCb.onPreDhcpAction();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPostDhcpAction() {
            this.mCb.onPostDhcpAction();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onNewDhcpResults(android.net.DhcpResultsParcelable dhcpResults) {
            this.mCb.onNewDhcpResults(dhcpResults);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onProvisioningSuccess(android.net.LinkProperties newLp) {
            this.mCb.onProvisioningSuccess(new android.net.LinkProperties(newLp));
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onProvisioningFailure(android.net.LinkProperties newLp) {
            this.mCb.onProvisioningFailure(new android.net.LinkProperties(newLp));
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onLinkPropertiesChange(android.net.LinkProperties newLp) {
            this.mCb.onLinkPropertiesChange(new android.net.LinkProperties(newLp));
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onReachabilityLost(java.lang.String logMsg) {
            this.mCb.onReachabilityLost(logMsg);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onQuit() {
            this.mCb.onQuit();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void installPacketFilter(byte[] filter) {
            this.mCb.installPacketFilter(filter);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void startReadPacketFilter() {
            this.mCb.startReadPacketFilter();
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setFallbackMulticastFilter(boolean enabled) {
            this.mCb.setFallbackMulticastFilter(enabled);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setNeighborDiscoveryOffload(boolean enable) {
            this.mCb.setNeighborDiscoveryOffload(enable);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onPreconnectionStart(java.util.List<android.net.Layer2PacketParcelable> packets) {
            this.mCb.onPreconnectionStart(packets);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void onReachabilityFailure(android.net.networkstack.aidl.ip.ReachabilityLossInfoParcelable lossInfo) {
            this.mCb.onReachabilityFailure(lossInfo);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public void setMaxDtimMultiplier(int multiplier) {
            this.mCb.setMaxDtimMultiplier(multiplier);
        }

        @Override // android.net.ip.IIpClientCallbacks
        public int getInterfaceVersion() {
            return 21;
        }

        @Override // android.net.ip.IIpClientCallbacks
        public java.lang.String getInterfaceHash() {
            return "9bd9d687ddb816baf1faabcad0d56ac15b22c56e";
        }
    }

    public static void dumpIpClient(android.net.ip.IIpClient connector, java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        pw.println("IpClient logs have moved to dumpsys network_stack");
    }
}
