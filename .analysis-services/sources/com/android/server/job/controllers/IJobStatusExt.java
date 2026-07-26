package com.android.server.job.controllers;

/* JADX INFO: loaded from: classes2.dex */
public interface IJobStatusExt {
    public static final int CONSTRAINT_BATTERY_DILE = 1024;
    public static final int CONSTRAINT_CONNECTIVITY = 7;
    public static final int CONSTRAINT_CPU = 4096;
    public static final int CONSTRAINT_FORE_APP = 2048;
    public static final int CONSTRAINT_PROTECT_SCENE = 16384;
    public static final int CONSTRAINT_TEMPERATURE = 8192;

    default int initRequiredConstraints(android.app.job.JobInfo job) {
        return 0;
    }

    default boolean isReady(boolean deadlineSatisfied, boolean notDozing, com.android.server.job.controllers.JobStatus jobStatus, int requiredConstraints, int satisfiedConstraints, int constraintsOfNetIntrest, int constraintsOfIntrest, int softOverrideConstraints) {
        return true;
    }

    default boolean getBooleanValue(java.lang.String method, java.lang.String key, boolean defValue) {
        return defValue;
    }

    default int getIntValue(java.lang.String method, java.lang.String key, int defValue) {
        return defValue;
    }

    default boolean setBooleanValue(java.lang.String method, java.lang.String key, boolean value, boolean defValue) {
        return defValue;
    }

    default int setIntValue(java.lang.String method, java.lang.String key, int value) {
        return 0;
    }

    default java.lang.String getOplusExtraStr(com.android.server.job.controllers.JobStatus job) {
        return null;
    }

    default int getProtectForeType(com.android.server.job.controllers.JobStatus job) {
        return 0;
    }

    default int getProtectForeScene(com.android.server.job.controllers.JobStatus job) {
        return 0;
    }

    default void setSyncJobAbnormal(android.app.job.JobInfo job) {
    }

    default boolean updateOsenseRestrictMode(int mode) {
        return false;
    }

    default int getOsenseRestrictMode() {
        return -1;
    }

    static void dumpConstraints(java.io.PrintWriter pw, int constraints) {
        if ((constraints & 1024) != 0) {
            pw.print(" BATT_IDLE");
        }
        if ((constraints & 2048) != 0) {
            pw.print(" PROTECT_FORE");
        }
        if ((constraints & 4096) != 0) {
            pw.print(" CPU");
        }
        if ((constraints & 8192) != 0) {
            pw.print(" TEMPERATURE");
        }
        if ((constraints & 16384) != 0) {
            pw.print(" PROTECT_SCENE");
        }
    }
}
