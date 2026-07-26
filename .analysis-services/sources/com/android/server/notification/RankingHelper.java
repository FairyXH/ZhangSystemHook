package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class RankingHelper {
    private static final java.lang.String TAG = "RankingHelper";
    private final android.content.Context mContext;
    private final java.util.Comparator mPreliminaryComparator;
    private final com.android.server.notification.RankingHandler mRankingHandler;
    private final com.android.server.notification.NotificationSignalExtractor[] mSignalExtractors;
    private final com.android.server.notification.GlobalSortKeyComparator mFinalComparator = new com.android.server.notification.GlobalSortKeyComparator();
    private final android.util.ArrayMap<java.lang.String, com.android.server.notification.NotificationRecord> mProxyByGroupTmp = new android.util.ArrayMap<>();

    public RankingHelper(android.content.Context context, com.android.server.notification.RankingHandler rankingHandler, com.android.server.notification.RankingConfig config, com.android.server.notification.ZenModeHelper zenHelper, com.android.server.notification.NotificationUsageStats usageStats, java.lang.String[] extractorNames, com.android.internal.compat.IPlatformCompat platformCompat) {
        this.mContext = context;
        this.mRankingHandler = rankingHandler;
        if (android.app.Flags.sortSectionByTime()) {
            this.mPreliminaryComparator = new com.android.server.notification.NotificationTimeComparator();
        } else {
            this.mPreliminaryComparator = new com.android.server.notification.NotificationComparator(this.mContext);
        }
        int N = extractorNames.length;
        this.mSignalExtractors = new com.android.server.notification.NotificationSignalExtractor[N];
        for (int i = 0; i < N; i++) {
            try {
                java.lang.Class<?> extractorClass = this.mContext.getClassLoader().loadClass(extractorNames[i]);
                com.android.server.notification.NotificationSignalExtractor extractor = (com.android.server.notification.NotificationSignalExtractor) extractorClass.newInstance();
                extractor.initialize(this.mContext, usageStats);
                extractor.setConfig(config);
                extractor.setZenHelper(zenHelper);
                if (android.app.Flags.restrictAudioAttributesAlarm() || android.app.Flags.restrictAudioAttributesMedia() || android.app.Flags.restrictAudioAttributesCall()) {
                    extractor.setCompatChangeLogger(platformCompat);
                }
                this.mSignalExtractors[i] = extractor;
            } catch (java.lang.ClassNotFoundException e) {
                android.util.Slog.w(TAG, "Couldn't find extractor " + extractorNames[i] + ".", e);
            } catch (java.lang.IllegalAccessException e2) {
                android.util.Slog.w(TAG, "Problem accessing extractor " + extractorNames[i] + ".", e2);
            } catch (java.lang.InstantiationException e3) {
                android.util.Slog.w(TAG, "Couldn't instantiate extractor " + extractorNames[i] + ".", e3);
            }
        }
    }

    public <T extends com.android.server.notification.NotificationSignalExtractor> T findExtractor(java.lang.Class<T> cls) {
        int length = this.mSignalExtractors.length;
        for (int i = 0; i < length; i++) {
            T t = (T) this.mSignalExtractors[i];
            if (cls.equals(t.getClass())) {
                return t;
            }
        }
        return null;
    }

    public void extractSignals(com.android.server.notification.NotificationRecord r) {
        int N = this.mSignalExtractors.length;
        for (int i = 0; i < N; i++) {
            com.android.server.notification.NotificationSignalExtractor extractor = this.mSignalExtractors[i];
            try {
                com.android.server.notification.RankingReconsideration recon = extractor.process(r);
                if (recon != null) {
                    this.mRankingHandler.requestReconsideration(recon);
                }
            } catch (java.lang.Throwable t) {
                android.util.Slog.w(TAG, "NotificationSignalExtractor failed.", t);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x00f2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void sort(java.util.ArrayList<com.android.server.notification.NotificationRecord> r20) {
        /*
            Method dump skipped, instruction units count: 331
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.RankingHelper.sort(java.util.ArrayList):void");
    }

    public int indexOf(java.util.ArrayList<com.android.server.notification.NotificationRecord> notificationList, com.android.server.notification.NotificationRecord target) {
        return java.util.Collections.binarySearch(notificationList, target, this.mFinalComparator);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        int N = this.mSignalExtractors.length;
        pw.print(prefix);
        pw.print("mSignalExtractors.length = ");
        pw.println(N);
        for (int i = 0; i < N; i++) {
            pw.print(prefix);
            pw.print("  ");
            pw.println(this.mSignalExtractors[i].getClass().getSimpleName());
        }
    }

    public void dump(android.util.proto.ProtoOutputStream proto, com.android.server.notification.NotificationManagerService.DumpFilter filter) {
        int N = this.mSignalExtractors.length;
        for (int i = 0; i < N; i++) {
            proto.write(2237677961217L, this.mSignalExtractors[i].getClass().getSimpleName());
        }
    }
}
