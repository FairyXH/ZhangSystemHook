package com.oplus.util;

/* JADX INFO: loaded from: classes3.dex */
public class OplusKillMethodProvider {
    private static final java.lang.String ACTION_CLEAR_SPEC_APP = "oplus.intent.action.REQUEST_CLEAR_SPEC_APP";
    private static final java.lang.String ACTION_ONEKEY_CLEAR = "oplus.intent.action.REQUEST_APP_CLEAN_RUNNING";
    private static final java.lang.String CALLED_PACKAGE = "com.oplus.athena";
    private static final java.lang.String CALLER_PACKAGE = "caller_package";
    private static final java.lang.String CALLER_PACKAGE_THEIA_UITIMEOUT_KILL = "android.theia_UITimeout_kill";
    private static final java.lang.String FILTER_APP_LIST = "filterapplist";
    private static final java.lang.String LIST = "list";
    private static final java.lang.String PID = "pid";
    private static final java.lang.String P_NAME = "p_name";
    private static final java.lang.String REASON = "reason";
    private static final int REQUEST_TYPE_KILL = 12;
    private static final int REQUEST_TYPE_KILL_OR_STOP = 11;
    private static final int REQUEST_TYPE_REMOVE_TASK = 14;
    private static final int REQUEST_TYPE_STOP = 13;
    private static final java.lang.String TAG = "OplusKillMethodProvider";
    private static final java.lang.String TYPE = "type";

    public static void killProcess(android.content.Context context, int pid, java.lang.String procName, java.lang.String reason) {
        try {
            android.content.Intent intent = new android.content.Intent(ACTION_CLEAR_SPEC_APP);
            intent.setPackage(CALLED_PACKAGE);
            intent.putExtra("pid", pid);
            intent.putExtra(P_NAME, procName);
            intent.putExtra(CALLER_PACKAGE, CALLER_PACKAGE_THEIA_UITIMEOUT_KILL);
            intent.putExtra("reason", reason);
            intent.putExtra("type", 12);
            context.startService(intent);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public static void killPackage(android.content.Context context, java.lang.String pkgName, java.lang.String reason) {
        try {
            android.content.Intent intent = new android.content.Intent(ACTION_CLEAR_SPEC_APP);
            intent.setPackage(CALLED_PACKAGE);
            intent.putExtra(P_NAME, pkgName);
            intent.putExtra(CALLER_PACKAGE, CALLER_PACKAGE_THEIA_UITIMEOUT_KILL);
            intent.putExtra("reason", reason);
            intent.putExtra("type", 12);
            context.startService(intent);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public static void killProcessList(android.content.Context context, java.util.List<com.oplus.util.OplusKillMethodProvider.KillInfo> list, java.lang.String reason) {
        if (list == null || list.isEmpty()) {
            return;
        }
        try {
            java.util.ArrayList<java.lang.String> killList = new java.util.ArrayList<>();
            for (com.oplus.util.OplusKillMethodProvider.KillInfo info : list) {
                killList.add(info.getInfoString());
            }
            android.content.Intent intent = new android.content.Intent(ACTION_CLEAR_SPEC_APP);
            intent.setPackage(CALLED_PACKAGE);
            intent.putStringArrayListExtra(LIST, killList);
            intent.putExtra(CALLER_PACKAGE, CALLER_PACKAGE_THEIA_UITIMEOUT_KILL);
            intent.putExtra("reason", reason);
            intent.putExtra("type", 12);
            context.startService(intent);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopPackageConfig(android.content.Context context, java.util.List<java.lang.String> list, java.lang.String reason) {
        try {
            java.util.ArrayList<java.lang.String> filterList = new java.util.ArrayList<>(list);
            android.content.Intent intent = new android.content.Intent(ACTION_ONEKEY_CLEAR);
            intent.setPackage(CALLED_PACKAGE);
            intent.putExtra(CALLER_PACKAGE, context.getPackageName());
            intent.putExtra(FILTER_APP_LIST, filterList);
            intent.putExtra("reason", reason);
            context.startService(intent);
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }

    public static class KillInfo {
        public int pid;
        public java.lang.String procName;

        public java.lang.String toString() {
            return getInfoString();
        }

        java.lang.String getInfoString() {
            return this.pid + "|" + this.procName;
        }
    }
}
