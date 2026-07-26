package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
final class GlobalLevelState {
    private static final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd");
    public boolean hibernated;
    public long lastUnhibernatedMs;
    public java.lang.String packageName;
    public long savedByte;

    GlobalLevelState() {
    }

    public java.lang.String toString() {
        return "GlobalLevelState{packageName='" + this.packageName + "', hibernated=" + this.hibernated + "', savedByte=" + this.savedByte + "', lastUnhibernated=" + DATE_FORMAT.format(java.lang.Long.valueOf(this.lastUnhibernatedMs)) + '}';
    }
}
