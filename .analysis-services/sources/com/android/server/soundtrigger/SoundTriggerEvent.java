package com.android.server.soundtrigger;

/* JADX INFO: loaded from: classes3.dex */
public abstract class SoundTriggerEvent extends com.android.server.utils.EventLogger.Event {
    @Override // com.android.server.utils.EventLogger.Event
    public com.android.server.utils.EventLogger.Event printLog(int type, java.lang.String tag) {
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

    public static class ServiceEvent extends com.android.server.soundtrigger.SoundTriggerEvent {
        private final java.lang.String mErrorString;
        private final java.lang.String mPackageName;
        private final com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type mType;

        public enum Type {
            ATTACH,
            LIST_MODULE,
            DETACH
        }

        public ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type type) {
            this(type, null, null);
        }

        public ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type type, java.lang.String packageName) {
            this(type, packageName, null);
        }

        public ServiceEvent(com.android.server.soundtrigger.SoundTriggerEvent.ServiceEvent.Type type, java.lang.String packageName, java.lang.String errorString) {
            this.mType = type;
            this.mPackageName = packageName;
            this.mErrorString = errorString;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.StringBuilder res = new java.lang.StringBuilder(java.lang.String.format("%-12s", this.mType.name()));
            if (this.mErrorString != null) {
                res.append(" ERROR: ").append(this.mErrorString);
            }
            if (this.mPackageName != null) {
                res.append(" for: ").append(this.mPackageName);
            }
            return res.toString();
        }
    }

    public static class SessionEvent extends com.android.server.soundtrigger.SoundTriggerEvent {
        private final java.lang.String mErrorString;
        private final java.util.UUID mModelUuid;
        private final com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type mType;

        public enum Type {
            START_RECOGNITION,
            STOP_RECOGNITION,
            LOAD_MODEL,
            UNLOAD_MODEL,
            UPDATE_MODEL,
            DELETE_MODEL,
            START_RECOGNITION_SERVICE,
            STOP_RECOGNITION_SERVICE,
            GET_MODEL_STATE,
            SET_PARAMETER,
            GET_MODULE_PROPERTIES,
            DETACH,
            RECOGNITION,
            RESUME,
            RESUME_FAILED,
            PAUSE,
            PAUSE_FAILED,
            RESOURCES_AVAILABLE,
            MODULE_DIED
        }

        public SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type type, java.util.UUID modelUuid, java.lang.String errorString) {
            this.mType = type;
            this.mModelUuid = modelUuid;
            this.mErrorString = errorString;
        }

        public SessionEvent(com.android.server.soundtrigger.SoundTriggerEvent.SessionEvent.Type type, java.util.UUID modelUuid) {
            this(type, modelUuid, null);
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.StringBuilder res = new java.lang.StringBuilder(java.lang.String.format("%-25s", this.mType.name()));
            if (this.mErrorString != null) {
                res.append(" ERROR: ").append(this.mErrorString);
            }
            if (this.mModelUuid != null) {
                res.append(" for: ").append(this.mModelUuid);
            }
            return res.toString();
        }
    }
}
