package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public class EventLogger {
    private static final java.lang.String DUMP_TITLE_PREFIX = "Events log: ";
    private final java.util.ArrayDeque<com.android.server.utils.EventLogger.Event> mEvents;
    private final int mMemSize;
    private final java.lang.String mTag;

    public interface DumpSink {
        void sink(java.lang.String str, java.util.List<com.android.server.utils.EventLogger.Event> list);
    }

    public EventLogger(int size, java.lang.String tag) {
        this.mEvents = new java.util.ArrayDeque<>(size);
        this.mMemSize = size;
        this.mTag = tag;
    }

    public synchronized void enqueue(com.android.server.utils.EventLogger.Event event) {
        if (this.mEvents.size() >= this.mMemSize) {
            this.mEvents.removeFirst();
        }
        this.mEvents.addLast(event);
    }

    public synchronized void enqueueAndLog(java.lang.String msg, int logType, java.lang.String tag) {
        com.android.server.utils.EventLogger.Event event = new com.android.server.utils.EventLogger.StringEvent(msg);
        enqueue(event.printLog(logType, tag));
    }

    public synchronized void enqueueAndSlog(java.lang.String msg, int logType, java.lang.String tag) {
        com.android.server.utils.EventLogger.Event event = new com.android.server.utils.EventLogger.StringEvent(msg);
        enqueue(event.printSlog(logType, tag));
    }

    public synchronized void dump(com.android.server.utils.EventLogger.DumpSink dumpSink) {
        dumpSink.sink(this.mTag, new java.util.ArrayList(this.mEvents));
    }

    public synchronized void dump(java.io.PrintWriter pw) {
        dump(pw, "");
    }

    protected java.lang.String getDumpTitle() {
        return this.mTag == null ? DUMP_TITLE_PREFIX : DUMP_TITLE_PREFIX + this.mTag;
    }

    public synchronized void dump(java.io.PrintWriter pw, java.lang.String indent) {
        pw.println(getDumpTitle());
        for (com.android.server.utils.EventLogger.Event evt : this.mEvents) {
            pw.println(indent + evt.toString());
        }
    }

    public static abstract class Event {
        public static final int ALOGE = 1;
        public static final int ALOGI = 0;
        public static final int ALOGV = 3;
        public static final int ALOGW = 2;
        private static final java.text.SimpleDateFormat sFormat = new java.text.SimpleDateFormat("MM-dd HH:mm:ss:SSS", java.util.Locale.US);
        private final long mTimestamp = java.lang.System.currentTimeMillis();

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        public @interface LogType {
        }

        public abstract java.lang.String eventToString();

        public java.lang.String toString() {
            return sFormat.format(new java.util.Date(this.mTimestamp)) + " " + eventToString();
        }

        public com.android.server.utils.EventLogger.Event printLog(java.lang.String tag) {
            return printLog(0, tag);
        }

        public com.android.server.utils.EventLogger.Event printLog(int type, java.lang.String tag) {
            switch (type) {
                case 0:
                    android.util.Log.i(tag, eventToString());
                    return this;
                case 1:
                    android.util.Log.e(tag, eventToString());
                    return this;
                case 2:
                    android.util.Log.w(tag, eventToString());
                    return this;
                default:
                    android.util.Log.v(tag, eventToString());
                    return this;
            }
        }

        public com.android.server.utils.EventLogger.Event printSlog(int type, java.lang.String tag) {
            switch (type) {
                case 0:
                    android.util.Slog.i(tag, eventToString());
                    return this;
                case 1:
                    android.util.Slog.e(tag, eventToString());
                    return this;
                case 2:
                    android.util.Slog.w(tag, eventToString());
                    return this;
                default:
                    android.util.Slog.v(tag, eventToString());
                    return this;
            }
        }
    }

    public static class StringEvent extends com.android.server.utils.EventLogger.Event {
        private final java.lang.String mDescription;
        private final java.lang.String mSource;

        public static com.android.server.utils.EventLogger.StringEvent from(java.lang.String source, java.lang.String description, java.lang.Object... args) {
            return new com.android.server.utils.EventLogger.StringEvent(source, java.lang.String.format(java.util.Locale.US, description, args));
        }

        public StringEvent(java.lang.String description) {
            this(null, description);
        }

        public StringEvent(java.lang.String source, java.lang.String description) {
            this.mSource = source;
            this.mDescription = description;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            if (this.mSource == null) {
                return this.mDescription;
            }
            return java.lang.String.format("[%-40s] %s", this.mSource, this.mDescription == null ? "" : this.mDescription);
        }
    }
}
