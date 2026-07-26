package com.android.server.vcn.routeselection;

/* JADX INFO: loaded from: classes3.dex */
class NetworkPriorityClassifier {
    static final int PRIORITY_FALLBACK = Integer.MAX_VALUE;
    static final int PRIORITY_INVALID = -1;
    private static final java.lang.String TAG = com.android.server.vcn.routeselection.NetworkPriorityClassifier.class.getSimpleName();
    static final int WIFI_ENTRY_RSSI_THRESHOLD_DEFAULT = -70;
    static final int WIFI_EXIT_RSSI_THRESHOLD_DEFAULT = -74;

    NetworkPriorityClassifier() {
    }

    public static int calculatePriorityClass(com.android.server.vcn.VcnContext vcnContext, com.android.server.vcn.routeselection.UnderlyingNetworkRecord networkRecord, java.util.List<android.net.vcn.VcnUnderlyingNetworkTemplate> underlyingNetworkTemplates, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, boolean isSelected, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        if (networkRecord.isBlocked) {
            logWtf("Network blocked for System Server: " + networkRecord.network);
            return -1;
        }
        if (snapshot == null) {
            logWtf("Got null snapshot");
            return -1;
        }
        int priorityIndex = 0;
        for (android.net.vcn.VcnUnderlyingNetworkTemplate nwPriority : underlyingNetworkTemplates) {
            if (checkMatchesPriorityRule(vcnContext, nwPriority, networkRecord, subscriptionGroup, snapshot, isSelected, carrierConfig)) {
                return priorityIndex;
            }
            priorityIndex++;
        }
        android.net.NetworkCapabilities caps = networkRecord.networkCapabilities;
        if (caps.hasCapability(12)) {
            return Integer.MAX_VALUE;
        }
        return (vcnContext.isInTestMode() && caps.hasTransport(7)) ? Integer.MAX_VALUE : -1;
    }

    public static boolean checkMatchesPriorityRule(com.android.server.vcn.VcnContext vcnContext, android.net.vcn.VcnUnderlyingNetworkTemplate networkPriority, com.android.server.vcn.routeselection.UnderlyingNetworkRecord networkRecord, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, boolean isSelected, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        android.net.NetworkCapabilities caps = networkRecord.networkCapabilities;
        int meteredMatch = networkPriority.getMetered();
        boolean isMetered = !caps.hasCapability(11);
        if ((meteredMatch != 1 || isMetered) && ((meteredMatch != 2 || !isMetered) && caps.getLinkUpstreamBandwidthKbps() >= networkPriority.getMinExitUpstreamBandwidthKbps() && ((caps.getLinkUpstreamBandwidthKbps() >= networkPriority.getMinEntryUpstreamBandwidthKbps() || isSelected) && caps.getLinkDownstreamBandwidthKbps() >= networkPriority.getMinExitDownstreamBandwidthKbps() && (caps.getLinkDownstreamBandwidthKbps() >= networkPriority.getMinEntryDownstreamBandwidthKbps() || isSelected)))) {
            for (java.util.Map.Entry<java.lang.Integer, java.lang.Integer> entry : networkPriority.getCapabilitiesMatchCriteria().entrySet()) {
                int cap = entry.getKey().intValue();
                int matchCriteria = entry.getValue().intValue();
                if (matchCriteria == 1 && !caps.hasCapability(cap)) {
                    return false;
                }
                if (matchCriteria == 2 && caps.hasCapability(cap)) {
                    return false;
                }
            }
            if (vcnContext.isInTestMode() && caps.hasTransport(7)) {
                return true;
            }
            if (!(networkPriority instanceof android.net.vcn.VcnWifiUnderlyingNetworkTemplate)) {
                if (networkPriority instanceof android.net.vcn.VcnCellUnderlyingNetworkTemplate) {
                    return checkMatchesCellPriorityRule(vcnContext, (android.net.vcn.VcnCellUnderlyingNetworkTemplate) networkPriority, networkRecord, subscriptionGroup, snapshot);
                }
                logWtf("Got unknown VcnUnderlyingNetworkTemplate class: " + networkPriority.getClass().getSimpleName());
                return false;
            }
            return checkMatchesWifiPriorityRule((android.net.vcn.VcnWifiUnderlyingNetworkTemplate) networkPriority, networkRecord, isSelected, carrierConfig);
        }
        return false;
    }

    public static boolean checkMatchesWifiPriorityRule(android.net.vcn.VcnWifiUnderlyingNetworkTemplate networkPriority, com.android.server.vcn.routeselection.UnderlyingNetworkRecord networkRecord, boolean isSelected, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        android.net.NetworkCapabilities caps = networkRecord.networkCapabilities;
        if (caps.hasTransport(1) && isWifiRssiAcceptable(networkRecord, isSelected, carrierConfig)) {
            return networkPriority.getSsids().isEmpty() || networkPriority.getSsids().contains(caps.getSsid());
        }
        return false;
    }

    private static boolean isWifiRssiAcceptable(com.android.server.vcn.routeselection.UnderlyingNetworkRecord networkRecord, boolean isSelected, com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        android.net.NetworkCapabilities caps = networkRecord.networkCapabilities;
        if ((isSelected && caps.getSignalStrength() >= getWifiExitRssiThreshold(carrierConfig)) || caps.getSignalStrength() >= getWifiEntryRssiThreshold(carrierConfig)) {
            return true;
        }
        return false;
    }

    public static boolean checkMatchesCellPriorityRule(com.android.server.vcn.VcnContext vcnContext, android.net.vcn.VcnCellUnderlyingNetworkTemplate networkPriority, com.android.server.vcn.routeselection.UnderlyingNetworkRecord networkRecord, android.os.ParcelUuid subscriptionGroup, com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot) {
        android.net.NetworkCapabilities caps = networkRecord.networkCapabilities;
        if (!caps.hasTransport(0)) {
            return false;
        }
        android.net.TelephonyNetworkSpecifier telephonyNetworkSpecifier = (android.net.TelephonyNetworkSpecifier) caps.getNetworkSpecifier();
        if (telephonyNetworkSpecifier == null) {
            logWtf("Got null NetworkSpecifier");
            return false;
        }
        int subId = telephonyNetworkSpecifier.getSubscriptionId();
        android.telephony.TelephonyManager subIdSpecificTelephonyMgr = ((android.telephony.TelephonyManager) vcnContext.getContext().getSystemService(android.telephony.TelephonyManager.class)).createForSubscriptionId(subId);
        if (!networkPriority.getOperatorPlmnIds().isEmpty()) {
            java.lang.String plmnId = subIdSpecificTelephonyMgr.getNetworkOperator();
            if (!networkPriority.getOperatorPlmnIds().contains(plmnId)) {
                return false;
            }
        }
        if (!networkPriority.getSimSpecificCarrierIds().isEmpty()) {
            int carrierId = subIdSpecificTelephonyMgr.getSimSpecificCarrierId();
            if (!networkPriority.getSimSpecificCarrierIds().contains(java.lang.Integer.valueOf(carrierId))) {
                return false;
            }
        }
        int roamingMatch = networkPriority.getRoaming();
        boolean isRoaming = !caps.hasCapability(18);
        if ((roamingMatch == 1 && !isRoaming) || (roamingMatch == 2 && isRoaming)) {
            return false;
        }
        int opportunisticMatch = networkPriority.getOpportunistic();
        boolean isOpportunistic = isOpportunistic(snapshot, caps.getSubscriptionIds());
        if (opportunisticMatch == 1) {
            if (!isOpportunistic) {
                return false;
            }
            if (snapshot.getAllSubIdsInGroup(subscriptionGroup).contains(java.lang.Integer.valueOf(android.telephony.SubscriptionManager.getActiveDataSubscriptionId())) && !caps.getSubscriptionIds().contains(java.lang.Integer.valueOf(android.telephony.SubscriptionManager.getActiveDataSubscriptionId()))) {
                return false;
            }
        } else if (opportunisticMatch == 2 && !isOpportunistic) {
            return false;
        }
        return true;
    }

    static boolean isOpportunistic(com.android.server.vcn.TelephonySubscriptionTracker.TelephonySubscriptionSnapshot snapshot, java.util.Set<java.lang.Integer> subIds) {
        if (snapshot == null) {
            logWtf("Got null snapshot");
            return false;
        }
        java.util.Iterator<java.lang.Integer> it = subIds.iterator();
        while (it.hasNext()) {
            int subId = it.next().intValue();
            if (snapshot.isOpportunistic(subId)) {
                return true;
            }
        }
        return false;
    }

    static int getWifiEntryRssiThreshold(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        return carrierConfig != null ? carrierConfig.getInt("vcn_network_selection_wifi_entry_rssi_threshold", WIFI_ENTRY_RSSI_THRESHOLD_DEFAULT) : WIFI_ENTRY_RSSI_THRESHOLD_DEFAULT;
    }

    static int getWifiExitRssiThreshold(com.android.server.vcn.util.PersistableBundleUtils.PersistableBundleWrapper carrierConfig) {
        return carrierConfig != null ? carrierConfig.getInt("vcn_network_selection_wifi_exit_rssi_threshold", WIFI_EXIT_RSSI_THRESHOLD_DEFAULT) : WIFI_EXIT_RSSI_THRESHOLD_DEFAULT;
    }

    private static void logWtf(java.lang.String msg) {
        android.util.Slog.wtf(TAG, msg);
        com.android.server.VcnManagementService.LOCAL_LOG.log(TAG + " WTF: " + msg);
    }
}
