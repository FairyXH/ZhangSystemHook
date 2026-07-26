package android.net.ip;

/* JADX INFO: loaded from: classes.dex */
public class IpClientManager {
    private final android.net.ip.IIpClient mIpClient;
    private final java.lang.String mTag;

    public IpClientManager(android.net.ip.IIpClient ipClient, java.lang.String tag) {
        this.mIpClient = ipClient;
        this.mTag = tag;
    }

    public IpClientManager(android.net.ip.IIpClient ipClient) {
        this(ipClient, android.net.ip.IpClientManager.class.getSimpleName());
    }

    private void log(java.lang.String s, java.lang.Throwable e) {
        android.util.Log.e(this.mTag, s, e);
    }

    public boolean completedPreDhcpAction() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.completedPreDhcpAction();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error completing PreDhcpAction", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean confirmConfiguration() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.confirmConfiguration();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error confirming IpClient configuration", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean readPacketFilterComplete(byte[] data) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.readPacketFilterComplete(data);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error notifying IpClient of packet filter read", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean shutdown() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.shutdown();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error shutting down IpClient", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean startProvisioning(android.net.shared.ProvisioningConfiguration prov) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.startProvisioning(prov.toStableParcelable());
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error starting IpClient provisioning", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean stop() {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.stop();
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error stopping IpClient", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean setTcpBufferSizes(java.lang.String tcpBufferSizes) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.setTcpBufferSizes(tcpBufferSizes);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error setting IpClient TCP buffer sizes", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean setHttpProxy(android.net.ProxyInfo proxyInfo) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.setHttpProxy(proxyInfo);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error setting IpClient proxy", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean setMulticastFilter(boolean enabled) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.setMulticastFilter(enabled);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error setting multicast filter", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean addKeepalivePacketFilter(int slot, android.net.TcpKeepalivePacketData pkt) {
        return addKeepalivePacketFilter(slot, android.net.util.KeepalivePacketDataUtil.toStableParcelable(pkt));
    }

    @java.lang.Deprecated
    public boolean addKeepalivePacketFilter(int slot, android.net.TcpKeepalivePacketDataParcelable pkt) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.addKeepalivePacketFilter(slot, pkt);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error adding Keepalive Packet Filter ", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean addKeepalivePacketFilter(int slot, android.net.NattKeepalivePacketData pkt) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.addNattKeepalivePacketFilter(slot, android.net.util.KeepalivePacketDataUtil.toStableParcelable(pkt));
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error adding NAT-T Keepalive Packet Filter ", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean removeKeepalivePacketFilter(int slot) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.removeKeepalivePacketFilter(slot);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error removing Keepalive Packet Filter ", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean setL2KeyAndGroupHint(java.lang.String l2Key, java.lang.String groupHint) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.setL2KeyAndGroupHint(l2Key, groupHint);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Failed setL2KeyAndGroupHint", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean notifyPreconnectionComplete(boolean success) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.notifyPreconnectionComplete(success);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error notifying IpClient Preconnection completed", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean updateLayer2Information(android.net.shared.Layer2Information info) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.updateLayer2Information(info.toStableParcelable());
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error updating layer2 information", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }

    public boolean updateApfCapabilities(android.net.apf.ApfCapabilities apfCapabilities) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            try {
                this.mIpClient.updateApfCapabilities(apfCapabilities);
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (android.os.RemoteException e) {
                log("Error updating APF capabilities", e);
                android.os.Binder.restoreCallingIdentity(token);
                return false;
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(token);
            throw th;
        }
    }
}
