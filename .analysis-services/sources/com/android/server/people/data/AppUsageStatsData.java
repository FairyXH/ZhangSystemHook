package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class AppUsageStatsData {
    private int mChosenCount;
    private int mLaunchCount;

    public AppUsageStatsData(int chosenCount, int launchCount) {
        this.mChosenCount = chosenCount;
        this.mLaunchCount = launchCount;
    }

    public AppUsageStatsData() {
    }

    public int getLaunchCount() {
        return this.mLaunchCount;
    }

    void incrementLaunchCountBy(int launchCount) {
        this.mLaunchCount += launchCount;
    }

    public int getChosenCount() {
        return this.mChosenCount;
    }

    void incrementChosenCountBy(int chosenCount) {
        this.mChosenCount += chosenCount;
    }
}
