package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
final class UserLevelState {
    private static final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public boolean hibernated;
    public long lastUnhibernatedMs;
    public java.lang.String packageName;
    public long savedByte;

    UserLevelState() {
    }

    UserLevelState(com.android.server.apphibernation.UserLevelState state) {
        this.packageName = state.packageName;
        this.hibernated = state.hibernated;
        this.savedByte = state.savedByte;
        this.lastUnhibernatedMs = state.lastUnhibernatedMs;
    }

    public java.lang.String toString() {
        return "UserLevelState{packageName='" + this.packageName + "', hibernated=" + this.hibernated + "', savedByte=" + this.savedByte + "', lastUnhibernated=" + DATE_FORMAT.format(java.lang.Long.valueOf(this.lastUnhibernatedMs)) + '}';
    }
}
