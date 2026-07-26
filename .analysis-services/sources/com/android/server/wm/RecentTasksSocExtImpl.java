package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class RecentTasksSocExtImpl implements com.android.server.wm.IRecentTasksSocExt {
    com.android.server.wm.RecentTasks mRecentTasks;
    private final android.util.BoostFramework mUxPerf = new android.util.BoostFramework();

    public RecentTasksSocExtImpl(java.lang.Object recentTasks) {
        this.mRecentTasks = (com.android.server.wm.RecentTasks) recentTasks;
    }

    @Override // com.android.server.wm.IRecentTasksSocExt
    public void removeTaskUxPerf(com.android.server.wm.Task task) {
        android.content.Intent intent;
        android.content.ComponentName componentName;
        if (task == null || (intent = task.getBaseIntent()) == null || (componentName = intent.getComponent()) == null) {
            return;
        }
        java.lang.String taskPkgName = componentName.getPackageName();
        if (this.mUxPerf != null) {
            if (this.mUxPerf.board_first_api_lvl < 33 && this.mUxPerf.board_api_lvl < 33) {
                this.mUxPerf.perfUXEngine_events(4, 0, taskPkgName, 0);
            } else {
                this.mUxPerf.perfEvent(4243, taskPkgName, 2, new int[]{0, 0});
            }
        }
    }
}
