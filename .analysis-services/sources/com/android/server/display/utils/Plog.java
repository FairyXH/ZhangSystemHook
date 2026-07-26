package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Plog {
    private long mId;

    protected abstract void emit(java.lang.String str);

    public static com.android.server.display.utils.Plog createSystemPlog(java.lang.String tag) {
        return new com.android.server.display.utils.Plog.SystemPlog(tag);
    }

    public com.android.server.display.utils.Plog start(java.lang.String title) {
        this.mId = java.lang.System.currentTimeMillis();
        write(formatTitle(title));
        return this;
    }

    public com.android.server.display.utils.Plog logPoint(java.lang.String name, float x, float y) {
        write(formatPoint(name, x, y));
        return this;
    }

    public com.android.server.display.utils.Plog logCurve(java.lang.String name, float[] xs, float[] ys) {
        write(formatCurve(name, xs, ys));
        return this;
    }

    private java.lang.String formatTitle(java.lang.String title) {
        return "title: " + title;
    }

    private java.lang.String formatPoint(java.lang.String name, float x, float y) {
        return "point: " + name + ": (" + x + "," + y + ")";
    }

    private java.lang.String formatCurve(java.lang.String name, float[] xs, float[] ys) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("curve: " + name + ": [");
        int n = xs.length <= ys.length ? xs.length : ys.length;
        for (int i = 0; i < n; i++) {
            sb.append("(" + xs[i] + "," + ys[i] + "),");
        }
        sb.append("]");
        return sb.toString();
    }

    private void write(java.lang.String message) {
        emit("[PLOG " + this.mId + "] " + message);
    }

    public static class SystemPlog extends com.android.server.display.utils.Plog {
        private final java.lang.String mTag;

        public SystemPlog(java.lang.String tag) {
            this.mTag = tag;
        }

        @Override // com.android.server.display.utils.Plog
        protected void emit(java.lang.String message) {
            android.util.Slog.d(this.mTag, message);
        }
    }
}
