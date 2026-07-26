package com.android.net.module.util;

/* JADX INFO: loaded from: classes.dex */
public class NetdUtils {
    private static final java.lang.String TAG = com.android.net.module.util.NetdUtils.class.getSimpleName();

    public enum ModifyOperation {
        ADD,
        REMOVE
    }

    public static android.net.InterfaceConfigurationParcel getInterfaceConfigParcel(android.net.INetd netd, java.lang.String iface) {
        try {
            return netd.interfaceGetCfg(iface);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    private static void validateFlag(java.lang.String flag) {
        if (flag.indexOf(32) >= 0) {
            throw new java.lang.IllegalArgumentException("flag contains space: " + flag);
        }
    }

    public static boolean hasFlag(android.net.InterfaceConfigurationParcel config, java.lang.String flag) {
        validateFlag(flag);
        java.util.Set<java.lang.String> flagList = new java.util.HashSet<>(java.util.Arrays.asList(config.flags));
        return flagList.contains(flag);
    }

    protected static java.lang.String[] removeAndAddFlags(java.lang.String[] flags, java.lang.String remove, java.lang.String add) {
        java.util.ArrayList<java.lang.String> result = new java.util.ArrayList<>();
        try {
            validateFlag(add);
            for (java.lang.String flag : flags) {
                if (!remove.equals(flag) && !add.equals(flag)) {
                    result.add(flag);
                }
            }
            result.add(add);
            return (java.lang.String[]) result.toArray(new java.lang.String[result.size()]);
        } catch (java.lang.IllegalArgumentException iae) {
            throw new java.lang.IllegalStateException("Invalid InterfaceConfigurationParcel", iae);
        }
    }

    public static void setInterfaceConfig(android.net.INetd netd, android.net.InterfaceConfigurationParcel configParcel) {
        try {
            netd.interfaceSetCfg(configParcel);
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public static void setInterfaceUp(android.net.INetd netd, java.lang.String iface) {
        android.net.InterfaceConfigurationParcel configParcel = getInterfaceConfigParcel(netd, iface);
        configParcel.flags = removeAndAddFlags(configParcel.flags, android.net.INetd.IF_STATE_DOWN, android.net.INetd.IF_STATE_UP);
        setInterfaceConfig(netd, configParcel);
    }

    public static void setInterfaceDown(android.net.INetd netd, java.lang.String iface) {
        android.net.InterfaceConfigurationParcel configParcel = getInterfaceConfigParcel(netd, iface);
        configParcel.flags = removeAndAddFlags(configParcel.flags, android.net.INetd.IF_STATE_UP, android.net.INetd.IF_STATE_DOWN);
        setInterfaceConfig(netd, configParcel);
    }

    public static void tetherStart(android.net.INetd netd, boolean usingLegacyDnsProxy, java.lang.String[] dhcpRange) throws android.os.RemoteException, android.os.ServiceSpecificException {
        android.net.TetherConfigParcel config = new android.net.TetherConfigParcel();
        config.usingLegacyDnsProxy = usingLegacyDnsProxy;
        config.dhcpRanges = dhcpRange;
        netd.tetherStartWithConfiguration(config);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public static void tetherInterface(android.net.INetd netd, java.lang.String iface, android.net.IpPrefix dest) throws android.os.RemoteException, android.os.ServiceSpecificException {
        tetherInterface(netd, iface, dest, 20, 50);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public static void tetherInterface(android.net.INetd netd, java.lang.String iface, android.net.IpPrefix dest, int maxAttempts, int pollingIntervalMs) throws android.os.RemoteException, android.os.ServiceSpecificException {
        netd.tetherInterfaceAdd(iface);
        networkAddInterface(netd, iface, maxAttempts, pollingIntervalMs);
        modifyRoute(netd, com.android.net.module.util.NetdUtils.ModifyOperation.ADD, 99, new android.net.RouteInfo(dest, null, iface, 1));
        modifyRoute(netd, com.android.net.module.util.NetdUtils.ModifyOperation.ADD, 99, new android.net.RouteInfo(new android.net.IpPrefix("fe80::/64"), null, iface, 1));
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private static void networkAddInterface(android.net.INetd netd, java.lang.String iface, int maxAttempts, int pollingIntervalMs) throws android.os.RemoteException, android.os.ServiceSpecificException {
        for (int i = 1; i <= maxAttempts; i++) {
            try {
                netd.networkAddInterface(99, iface);
                return;
            } catch (android.os.ServiceSpecificException e) {
                if (e.errorCode == android.system.OsConstants.EBUSY && i < maxAttempts) {
                    android.os.SystemClock.sleep(pollingIntervalMs);
                } else {
                    android.util.Log.e(TAG, "Retry Netd#networkAddInterface failure: " + e);
                    throw e;
                }
            }
        }
    }

    public static void untetherInterface(android.net.INetd netd, java.lang.String iface) throws android.os.RemoteException, android.os.ServiceSpecificException {
        try {
            netd.tetherInterfaceRemove(iface);
        } finally {
            netd.networkRemoveInterface(99, iface);
        }
    }

    public static void addRoutesToLocalNetwork(android.net.INetd netd, java.lang.String iface, java.util.List<android.net.RouteInfo> routes) {
        for (android.net.RouteInfo route : routes) {
            if (!route.isDefaultRoute()) {
                modifyRoute(netd, com.android.net.module.util.NetdUtils.ModifyOperation.ADD, 99, route);
            }
        }
        modifyRoute(netd, com.android.net.module.util.NetdUtils.ModifyOperation.ADD, 99, new android.net.RouteInfo(new android.net.IpPrefix("fe80::/64"), null, iface, 1));
    }

    public static int removeRoutesFromLocalNetwork(android.net.INetd netd, java.util.List<android.net.RouteInfo> routes) {
        int failures = 0;
        for (android.net.RouteInfo route : routes) {
            try {
                modifyRoute(netd, com.android.net.module.util.NetdUtils.ModifyOperation.REMOVE, 99, route);
            } catch (java.lang.IllegalStateException e) {
                failures++;
            }
        }
        return failures;
    }

    private static java.lang.String findNextHop(android.net.RouteInfo route) {
        switch (route.getType()) {
            case 1:
                if (route.hasGateway()) {
                    java.lang.String nextHop = route.getGateway().getHostAddress();
                    return nextHop;
                }
                return "";
            case 7:
                return android.net.INetd.NEXTHOP_UNREACHABLE;
            case 9:
                return android.net.INetd.NEXTHOP_THROW;
            default:
                return "";
        }
    }

    public static void modifyRoute(android.net.INetd netd, com.android.net.module.util.NetdUtils.ModifyOperation op, int netId, android.net.RouteInfo route) {
        java.lang.String ifName = route.getInterface();
        java.lang.String dst = route.getDestination().toString();
        java.lang.String nextHop = findNextHop(route);
        try {
            switch (op) {
                case ADD:
                    netd.networkAddRoute(netId, ifName, dst, nextHop);
                    return;
                case REMOVE:
                    netd.networkRemoveRoute(netId, ifName, dst, nextHop);
                    return;
                default:
                    throw new java.lang.IllegalStateException("Unsupported modify operation:" + op);
            }
        } catch (android.os.RemoteException | android.os.ServiceSpecificException e) {
            throw new java.lang.IllegalStateException(e);
        }
    }

    public static android.net.RouteInfoParcel toRouteInfoParcel(android.net.RouteInfo route) {
        java.lang.String nextHop;
        switch (route.getType()) {
            case 1:
                if (route.hasGateway()) {
                    nextHop = route.getGateway().getHostAddress();
                } else {
                    nextHop = "";
                }
                break;
            case 7:
                nextHop = android.net.INetd.NEXTHOP_UNREACHABLE;
                break;
            case 9:
                nextHop = android.net.INetd.NEXTHOP_THROW;
                break;
            default:
                nextHop = "";
                break;
        }
        android.net.RouteInfoParcel rip = new android.net.RouteInfoParcel();
        rip.ifName = route.getInterface();
        rip.destination = route.getDestination().toString();
        rip.nextHop = nextHop;
        rip.mtu = route.getMtu();
        return rip;
    }
}
