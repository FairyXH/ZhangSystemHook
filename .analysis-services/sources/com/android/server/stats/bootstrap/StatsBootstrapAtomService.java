package com.android.server.stats.bootstrap;

/* JADX INFO: loaded from: classes3.dex */
public class StatsBootstrapAtomService extends android.os.IStatsBootstrapAtomService.Stub {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "StatsBootstrapAtomService";

    public void reportBootstrapAtom(android.os.StatsBootstrapAtom atom) {
        if (atom.atomId < 1 || atom.atomId >= 10000) {
            android.util.Slog.e(TAG, "Atom ID " + atom.atomId + " is not a valid atom ID");
            return;
        }
        android.util.StatsEvent.Builder builder = android.util.StatsEvent.newBuilder().setAtomId(atom.atomId);
        for (android.os.StatsBootstrapAtomValue value : atom.values) {
            switch (value.getTag()) {
                case 0:
                    builder.writeBoolean(value.getBoolValue());
                    break;
                case 1:
                    builder.writeInt(value.getIntValue());
                    break;
                case 2:
                    builder.writeLong(value.getLongValue());
                    break;
                case 3:
                    builder.writeFloat(value.getFloatValue());
                    break;
                case 4:
                    builder.writeString(value.getStringValue());
                    break;
                case 5:
                    builder.writeByteArray(value.getBytesValue());
                    break;
                default:
                    android.util.Slog.e(TAG, "Unexpected value type " + value.getTag() + " when logging atom " + atom.atomId);
                    return;
            }
        }
        android.util.StatsLog.write(builder.usePooledBuffer().build());
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        private com.android.server.stats.bootstrap.StatsBootstrapAtomService mStatsBootstrapAtomService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mStatsBootstrapAtomService = new com.android.server.stats.bootstrap.StatsBootstrapAtomService();
            try {
                publishBinderService("statsbootstrap", this.mStatsBootstrapAtomService);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.stats.bootstrap.StatsBootstrapAtomService.TAG, "Failed to publishBinderService", e);
            }
        }
    }
}
