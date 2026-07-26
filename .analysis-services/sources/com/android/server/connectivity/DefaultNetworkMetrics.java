package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public class DefaultNetworkMetrics {
    private static final int ROLLING_LOG_SIZE = 64;
    private android.net.metrics.DefaultNetworkEvent mCurrentDefaultNetwork;
    private boolean mIsCurrentlyValid;
    private int mLastTransports;
    private long mLastValidationTimeMs;
    public final long creationTimeMs = android.os.SystemClock.elapsedRealtime();
    private final java.util.List<android.net.metrics.DefaultNetworkEvent> mEvents = new java.util.ArrayList();
    private final com.android.internal.util.RingBuffer<android.net.metrics.DefaultNetworkEvent> mEventsLog = new com.android.internal.util.RingBuffer<>(android.net.metrics.DefaultNetworkEvent.class, 64);

    public DefaultNetworkMetrics() {
        newDefaultNetwork(this.creationTimeMs, null, 0, false, null, null);
    }

    public synchronized void listEvents(java.io.PrintWriter pw) {
        pw.println("default network events:");
        long localTimeMs = java.lang.System.currentTimeMillis();
        long timeMs = android.os.SystemClock.elapsedRealtime();
        for (android.net.metrics.DefaultNetworkEvent ev : (android.net.metrics.DefaultNetworkEvent[]) this.mEventsLog.toArray()) {
            printEvent(localTimeMs, pw, ev);
        }
        this.mCurrentDefaultNetwork.updateDuration(timeMs);
        if (this.mIsCurrentlyValid) {
            updateValidationTime(timeMs);
            this.mLastValidationTimeMs = timeMs;
        }
        printEvent(localTimeMs, pw, this.mCurrentDefaultNetwork);
    }

    public synchronized java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> listEventsAsProto() {
        java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> list;
        list = new java.util.ArrayList<>();
        for (android.net.metrics.DefaultNetworkEvent ev : (android.net.metrics.DefaultNetworkEvent[]) this.mEventsLog.toArray()) {
            list.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(ev));
        }
        return list;
    }

    public synchronized void flushEvents(java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> out) {
        for (android.net.metrics.DefaultNetworkEvent ev : this.mEvents) {
            out.add(com.android.server.connectivity.IpConnectivityEventBuilder.toProto(ev));
        }
        this.mEvents.clear();
    }

    public synchronized void logDefaultNetworkValidity(long timeMs, boolean isValid) {
        if (!isValid) {
            try {
                if (this.mIsCurrentlyValid) {
                    this.mIsCurrentlyValid = false;
                    updateValidationTime(timeMs);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        if (isValid && !this.mIsCurrentlyValid) {
            this.mIsCurrentlyValid = true;
            this.mLastValidationTimeMs = timeMs;
        }
    }

    private void updateValidationTime(long timeMs) {
        this.mCurrentDefaultNetwork.validatedMs += timeMs - this.mLastValidationTimeMs;
    }

    public synchronized void logDefaultNetworkEvent(long timeMs, android.net.Network defaultNetwork, int score, boolean validated, android.net.LinkProperties lp, android.net.NetworkCapabilities nc, android.net.Network previousDefaultNetwork, int previousScore, android.net.LinkProperties previousLp, android.net.NetworkCapabilities previousNc) {
        logCurrentDefaultNetwork(timeMs, previousDefaultNetwork, previousScore, previousLp, previousNc);
        newDefaultNetwork(timeMs, defaultNetwork, score, validated, lp, nc);
    }

    private void logCurrentDefaultNetwork(long timeMs, android.net.Network network, int score, android.net.LinkProperties lp, android.net.NetworkCapabilities nc) {
        if (this.mIsCurrentlyValid) {
            updateValidationTime(timeMs);
        }
        android.net.metrics.DefaultNetworkEvent ev = this.mCurrentDefaultNetwork;
        ev.updateDuration(timeMs);
        ev.previousTransports = this.mLastTransports;
        if (network != null) {
            fillLinkInfo(ev, network, lp, nc);
            ev.finalScore = score;
        }
        if (ev.transports != 0) {
            this.mLastTransports = ev.transports;
        }
        this.mEvents.add(ev);
        this.mEventsLog.append(ev);
    }

    private void newDefaultNetwork(long timeMs, android.net.Network network, int score, boolean validated, android.net.LinkProperties lp, android.net.NetworkCapabilities nc) {
        android.net.metrics.DefaultNetworkEvent ev = new android.net.metrics.DefaultNetworkEvent(timeMs);
        ev.durationMs = timeMs;
        if (network != null) {
            fillLinkInfo(ev, network, lp, nc);
            ev.initialScore = score;
            if (validated) {
                this.mIsCurrentlyValid = true;
                this.mLastValidationTimeMs = timeMs;
            }
        } else {
            this.mIsCurrentlyValid = false;
        }
        this.mCurrentDefaultNetwork = ev;
    }

    private static void fillLinkInfo(android.net.metrics.DefaultNetworkEvent ev, android.net.Network network, android.net.LinkProperties lp, android.net.NetworkCapabilities nc) {
        ev.netId = network.getNetId();
        ev.transports = (int) (((long) ev.transports) | com.android.internal.util.BitUtils.packBits(nc.getTransportTypes()));
        ev.ipv4 |= lp.hasIpv4Address() && lp.hasIpv4DefaultRoute();
        ev.ipv6 |= lp.hasGlobalIpv6Address() && lp.hasIpv6DefaultRoute();
    }

    private static void printEvent(long localTimeMs, java.io.PrintWriter pw, android.net.metrics.DefaultNetworkEvent ev) {
        long localCreationTimeMs = localTimeMs - ev.durationMs;
        pw.println(java.lang.String.format("%tT.%tL: %s", java.lang.Long.valueOf(localCreationTimeMs), java.lang.Long.valueOf(localCreationTimeMs), ev));
    }
}
