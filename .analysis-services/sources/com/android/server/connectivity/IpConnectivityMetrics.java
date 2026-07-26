package com.android.server.connectivity;

/* JADX INFO: loaded from: classes.dex */
public final class IpConnectivityMetrics extends com.android.server.SystemService {
    private static final boolean DBG = false;
    private static final int DEFAULT_BUFFER_SIZE = 2000;
    private static final int DEFAULT_LOG_SIZE = 500;
    private static final int ERROR_RATE_LIMITED = -1;
    private static final int MAXIMUM_BUFFER_SIZE = 20000;
    private static final int MAXIMUM_CONNECT_LATENCY_RECORDS = 20000;
    private static final int NYC = 0;
    private static final int NYC_MR1 = 1;
    private static final int NYC_MR2 = 2;
    private static final java.lang.String SERVICE_NAME = "connmetrics";
    public static final int VERSION = 2;
    public final com.android.server.connectivity.IpConnectivityMetrics.Impl impl;
    private final android.util.ArrayMap<java.lang.Class<?>, com.android.internal.util.TokenBucket> mBuckets;
    private java.util.ArrayList<android.net.ConnectivityMetricsEvent> mBuffer;
    private int mCapacity;
    private final java.util.function.ToIntFunction<android.content.Context> mCapacityGetter;
    final com.android.server.connectivity.DefaultNetworkMetrics mDefaultNetworkMetrics;
    private int mDropped;
    private final com.android.internal.util.RingBuffer<android.net.ConnectivityMetricsEvent> mEventLog;
    private final java.lang.Object mLock;
    com.android.server.connectivity.NetdEventListenerService mNetdListener;
    private static final java.lang.String TAG = com.android.server.connectivity.IpConnectivityMetrics.class.getSimpleName();
    private static final java.util.function.ToIntFunction<android.content.Context> READ_BUFFER_SIZE = new java.util.function.ToIntFunction() { // from class: com.android.server.connectivity.IpConnectivityMetrics$$ExternalSyntheticLambda1
        @Override // java.util.function.ToIntFunction
        public final int applyAsInt(java.lang.Object obj) {
            return com.android.server.connectivity.IpConnectivityMetrics.lambda$static$1((android.content.Context) obj);
        }
    };

    public interface Logger {
        com.android.server.connectivity.DefaultNetworkMetrics defaultNetworkMetrics();
    }

    public IpConnectivityMetrics(android.content.Context ctx, java.util.function.ToIntFunction<android.content.Context> capacityGetter) {
        super(ctx);
        this.mLock = new java.lang.Object();
        this.impl = new com.android.server.connectivity.IpConnectivityMetrics.Impl();
        this.mEventLog = new com.android.internal.util.RingBuffer<>(android.net.ConnectivityMetricsEvent.class, 500);
        this.mBuckets = makeRateLimitingBuckets();
        this.mDefaultNetworkMetrics = new com.android.server.connectivity.DefaultNetworkMetrics();
        this.mCapacityGetter = capacityGetter;
        initBuffer();
    }

    public IpConnectivityMetrics(android.content.Context ctx) {
        this(ctx, READ_BUFFER_SIZE);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            this.mNetdListener = new com.android.server.connectivity.NetdEventListenerService(getContext());
            publishBinderService(SERVICE_NAME, this.impl);
            publishBinderService(com.android.server.connectivity.NetdEventListenerService.SERVICE_NAME, this.mNetdListener);
            com.android.server.LocalServices.addService(com.android.server.connectivity.IpConnectivityMetrics.Logger.class, new com.android.server.connectivity.IpConnectivityMetrics.LoggerImpl());
        }
    }

    public int bufferCapacity() {
        return this.mCapacityGetter.applyAsInt(getContext());
    }

    private void initBuffer() {
        synchronized (this.mLock) {
            this.mDropped = 0;
            this.mCapacity = bufferCapacity();
            this.mBuffer = new java.util.ArrayList<>(this.mCapacity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int append(android.net.ConnectivityMetricsEvent event) {
        synchronized (this.mLock) {
            this.mEventLog.append(event);
            int left = this.mCapacity - this.mBuffer.size();
            if (event == null) {
                return left;
            }
            if (isRateLimited(event)) {
                return -1;
            }
            if (left == 0) {
                this.mDropped++;
                return 0;
            }
            this.mBuffer.add(event);
            return left - 1;
        }
    }

    private boolean isRateLimited(android.net.ConnectivityMetricsEvent event) {
        com.android.internal.util.TokenBucket tb = this.mBuckets.get(event.data.getClass());
        return (tb == null || tb.get()) ? false : true;
    }

    private java.lang.String flushEncodedOutput() {
        java.util.ArrayList<android.net.ConnectivityMetricsEvent> events;
        int dropped;
        synchronized (this.mLock) {
            events = this.mBuffer;
            dropped = this.mDropped;
            initBuffer();
        }
        java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> protoEvents = com.android.server.connectivity.IpConnectivityEventBuilder.toProto(events);
        this.mDefaultNetworkMetrics.flushEvents(protoEvents);
        if (this.mNetdListener != null) {
            this.mNetdListener.flushStatistics(protoEvents);
        }
        try {
            byte[] data = com.android.server.connectivity.IpConnectivityEventBuilder.serialize(dropped, protoEvents);
            return android.util.Base64.encodeToString(data, 0);
        } catch (java.io.IOException e) {
            android.util.Log.e(TAG, "could not serialize events", e);
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cmdFlush(java.io.PrintWriter pw) {
        pw.print(flushEncodedOutput());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cmdList(java.io.PrintWriter pw) {
        pw.println("metrics events:");
        java.util.List<android.net.ConnectivityMetricsEvent> events = getEvents();
        for (android.net.ConnectivityMetricsEvent ev : events) {
            pw.println(ev.toString());
        }
        pw.println("");
        if (this.mNetdListener != null) {
            this.mNetdListener.list(pw);
        }
        pw.println("");
        this.mDefaultNetworkMetrics.listEvents(pw);
    }

    private java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> listEventsAsProtos() {
        java.util.List<com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent> events = com.android.server.connectivity.IpConnectivityEventBuilder.toProto(getEvents());
        if (this.mNetdListener != null) {
            events.addAll(this.mNetdListener.listAsProtos());
        }
        events.addAll(this.mDefaultNetworkMetrics.listEventsAsProto());
        return events;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cmdListAsTextProto(final java.io.PrintWriter pw) {
        listEventsAsProtos().forEach(new java.util.function.Consumer() { // from class: com.android.server.connectivity.IpConnectivityMetrics$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                pw.print(((com.android.server.connectivity.metrics.nano.IpConnectivityLogClass.IpConnectivityEvent) obj).toString());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cmdListAsBinaryProto(java.io.OutputStream out) {
        int dropped;
        synchronized (this.mLock) {
            dropped = this.mDropped;
        }
        try {
            byte[] data = com.android.server.connectivity.IpConnectivityEventBuilder.serialize(dropped, listEventsAsProtos());
            out.write(data);
            out.flush();
        } catch (java.io.IOException e) {
            android.util.Log.e(TAG, "could not serialize events", e);
        }
    }

    private java.util.List<android.net.ConnectivityMetricsEvent> getEvents() {
        java.util.List<android.net.ConnectivityMetricsEvent> listAsList;
        synchronized (this.mLock) {
            listAsList = java.util.Arrays.asList((android.net.ConnectivityMetricsEvent[]) this.mEventLog.toArray());
        }
        return listAsList;
    }

    public final class Impl extends android.net.IIpConnectivityMetrics.Stub {
        static final java.lang.String CMD_DEFAULT = "";
        static final java.lang.String CMD_FLUSH = "flush";
        static final java.lang.String CMD_LIST = "list";
        static final java.lang.String CMD_PROTO = "proto";
        static final java.lang.String CMD_PROTO_BIN = "--proto";

        public Impl() {
        }

        public int logEvent(android.net.ConnectivityMetricsEvent event) {
            android.net.NetworkStack.checkNetworkStackPermission(com.android.server.connectivity.IpConnectivityMetrics.this.getContext());
            return com.android.server.connectivity.IpConnectivityMetrics.this.append(event);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:21:0x003e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void dump(java.io.FileDescriptor r4, java.io.PrintWriter r5, java.lang.String[] r6) {
            /*
                r3 = this;
                r3.enforceDumpPermission()
                int r0 = r6.length
                r1 = 0
                if (r0 <= 0) goto La
                r0 = r6[r1]
                goto Lc
            La:
                java.lang.String r0 = ""
            Lc:
                int r2 = r0.hashCode()
                switch(r2) {
                    case -1616754616: goto L34;
                    case 3322014: goto L29;
                    case 97532676: goto L1f;
                    case 106940904: goto L14;
                    default: goto L13;
                }
            L13:
                goto L3e
            L14:
                java.lang.String r1 = "proto"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L13
                r1 = 1
                goto L3f
            L1f:
                java.lang.String r2 = "flush"
                boolean r2 = r0.equals(r2)
                if (r2 == 0) goto L13
                goto L3f
            L29:
                java.lang.String r1 = "list"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L13
                r1 = 3
                goto L3f
            L34:
                java.lang.String r1 = "--proto"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L13
                r1 = 2
                goto L3f
            L3e:
                r1 = -1
            L3f:
                switch(r1) {
                    case 0: goto L59;
                    case 1: goto L53;
                    case 2: goto L48;
                    default: goto L42;
                }
            L42:
                com.android.server.connectivity.IpConnectivityMetrics r1 = com.android.server.connectivity.IpConnectivityMetrics.this
                com.android.server.connectivity.IpConnectivityMetrics.m2869$$Nest$mcmdList(r1, r5)
                return
            L48:
                com.android.server.connectivity.IpConnectivityMetrics r1 = com.android.server.connectivity.IpConnectivityMetrics.this
                java.io.FileOutputStream r2 = new java.io.FileOutputStream
                r2.<init>(r4)
                com.android.server.connectivity.IpConnectivityMetrics.m2870$$Nest$mcmdListAsBinaryProto(r1, r2)
                return
            L53:
                com.android.server.connectivity.IpConnectivityMetrics r1 = com.android.server.connectivity.IpConnectivityMetrics.this
                com.android.server.connectivity.IpConnectivityMetrics.m2871$$Nest$mcmdListAsTextProto(r1, r5)
                return
            L59:
                com.android.server.connectivity.IpConnectivityMetrics r1 = com.android.server.connectivity.IpConnectivityMetrics.this
                com.android.server.connectivity.IpConnectivityMetrics.m2868$$Nest$mcmdFlush(r1, r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.connectivity.IpConnectivityMetrics.Impl.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
        }

        private void enforceDumpPermission() {
            enforcePermission("android.permission.DUMP");
        }

        private void enforcePermission(java.lang.String what) {
            com.android.server.connectivity.IpConnectivityMetrics.this.getContext().enforceCallingOrSelfPermission(what, "IpConnectivityMetrics");
        }

        private void enforceNetdEventListeningPermission() {
            int uid = android.os.Binder.getCallingUid();
            if (uid != 1000) {
                throw new java.lang.SecurityException(java.lang.String.format("Uid %d has no permission to listen for netd events.", java.lang.Integer.valueOf(uid)));
            }
        }

        public boolean addNetdEventCallback(int callerType, android.net.INetdEventCallback callback) {
            enforceNetdEventListeningPermission();
            if (com.android.server.connectivity.IpConnectivityMetrics.this.mNetdListener == null) {
                return false;
            }
            return com.android.server.connectivity.IpConnectivityMetrics.this.mNetdListener.addNetdEventCallback(callerType, callback);
        }

        public boolean removeNetdEventCallback(int callerType) {
            enforceNetdEventListeningPermission();
            if (com.android.server.connectivity.IpConnectivityMetrics.this.mNetdListener == null) {
                return true;
            }
            return com.android.server.connectivity.IpConnectivityMetrics.this.mNetdListener.removeNetdEventCallback(callerType);
        }

        public void logDefaultNetworkValidity(boolean valid) {
            android.net.NetworkStack.checkNetworkStackPermission(com.android.server.connectivity.IpConnectivityMetrics.this.getContext());
            com.android.server.connectivity.IpConnectivityMetrics.this.mDefaultNetworkMetrics.logDefaultNetworkValidity(android.os.SystemClock.elapsedRealtime(), valid);
        }

        public void logDefaultNetworkEvent(android.net.Network defaultNetwork, int score, boolean validated, android.net.LinkProperties lp, android.net.NetworkCapabilities nc, android.net.Network previousDefaultNetwork, int previousScore, android.net.LinkProperties previousLp, android.net.NetworkCapabilities previousNc) {
            android.net.NetworkStack.checkNetworkStackPermission(com.android.server.connectivity.IpConnectivityMetrics.this.getContext());
            long timeMs = android.os.SystemClock.elapsedRealtime();
            com.android.server.connectivity.IpConnectivityMetrics.this.mDefaultNetworkMetrics.logDefaultNetworkEvent(timeMs, defaultNetwork, score, validated, lp, nc, previousDefaultNetwork, previousScore, previousLp, previousNc);
        }
    }

    static /* synthetic */ int lambda$static$1(android.content.Context ctx) {
        int size = android.provider.Settings.Global.getInt(ctx.getContentResolver(), "connectivity_metrics_buffer_size", 2000);
        if (size <= 0) {
            return 2000;
        }
        return java.lang.Math.min(size, 20000);
    }

    private static android.util.ArrayMap<java.lang.Class<?>, com.android.internal.util.TokenBucket> makeRateLimitingBuckets() {
        android.util.ArrayMap<java.lang.Class<?>, com.android.internal.util.TokenBucket> map = new android.util.ArrayMap<>();
        map.put(android.net.metrics.ApfProgramEvent.class, new com.android.internal.util.TokenBucket(60000, 50));
        return map;
    }

    private class LoggerImpl implements com.android.server.connectivity.IpConnectivityMetrics.Logger {
        private LoggerImpl() {
        }

        @Override // com.android.server.connectivity.IpConnectivityMetrics.Logger
        public com.android.server.connectivity.DefaultNetworkMetrics defaultNetworkMetrics() {
            return com.android.server.connectivity.IpConnectivityMetrics.this.mDefaultNetworkMetrics;
        }
    }
}
