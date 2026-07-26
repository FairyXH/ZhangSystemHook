package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ScanPartition extends android.content.pm.PackagePartitions.SystemPartition {
    public final com.android.server.pm.ApexManager.ActiveApexInfo apexInfo;
    public final int scanFlag;

    public ScanPartition(android.content.pm.PackagePartitions.SystemPartition partition) {
        super(partition);
        this.scanFlag = scanFlagForPartition(partition);
        this.apexInfo = null;
    }

    public ScanPartition(java.io.File folder, com.android.server.pm.ScanPartition original, com.android.server.pm.ApexManager.ActiveApexInfo apexInfo) {
        super(folder, original);
        int scanFlags = original.scanFlag;
        this.apexInfo = apexInfo;
        if (apexInfo != null) {
            scanFlags |= 8388608;
            scanFlags = apexInfo.isFactory ? scanFlags | 33554432 : scanFlags;
            if (apexInfo.activeApexChanged) {
                scanFlags |= 16777216;
            }
        }
        this.scanFlag = scanFlags;
    }

    private static int scanFlagForPartition(android.content.pm.PackagePartitions.SystemPartition partition) {
        switch (partition.type) {
            case 0:
                return 0;
            case 1:
                return 524288;
            case 2:
                return 4194304;
            case 3:
                return 262144;
            case 4:
                return 1048576;
            case 5:
                return 2097152;
            default:
                throw new java.lang.IllegalStateException("Unable to determine scan flag for " + partition.getFolder());
        }
    }

    public java.lang.String toString() {
        return getFolder().getAbsolutePath() + ":" + this.scanFlag;
    }
}
