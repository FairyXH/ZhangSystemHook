package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class SysUiStatsEvent {

    static class Builder {
        private final android.util.StatsEvent.Builder mBuilder;

        protected Builder(android.util.StatsEvent.Builder builder) {
            this.mBuilder = builder;
        }

        public android.util.StatsEvent build() {
            return this.mBuilder.build();
        }

        public com.android.server.notification.SysUiStatsEvent.Builder setAtomId(int atomId) {
            this.mBuilder.setAtomId(atomId);
            return this;
        }

        public com.android.server.notification.SysUiStatsEvent.Builder writeInt(int value) {
            this.mBuilder.writeInt(value);
            return this;
        }

        public com.android.server.notification.SysUiStatsEvent.Builder addBooleanAnnotation(byte annotation, boolean value) {
            this.mBuilder.addBooleanAnnotation(annotation, value);
            return this;
        }

        public com.android.server.notification.SysUiStatsEvent.Builder writeString(java.lang.String value) {
            this.mBuilder.writeString(value);
            return this;
        }

        public com.android.server.notification.SysUiStatsEvent.Builder writeBoolean(boolean value) {
            this.mBuilder.writeBoolean(value);
            return this;
        }

        public com.android.server.notification.SysUiStatsEvent.Builder writeByteArray(byte[] value) {
            this.mBuilder.writeByteArray(value);
            return this;
        }
    }

    static class BuilderFactory {
        BuilderFactory() {
        }

        com.android.server.notification.SysUiStatsEvent.Builder newBuilder() {
            return new com.android.server.notification.SysUiStatsEvent.Builder(android.util.StatsEvent.newBuilder());
        }
    }
}
