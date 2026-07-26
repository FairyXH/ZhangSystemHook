package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public interface DeviceConfigInterface {
    public static final com.android.server.utils.DeviceConfigInterface REAL = new com.android.server.utils.DeviceConfigInterface() { // from class: com.android.server.utils.DeviceConfigInterface.1
        @Override // com.android.server.utils.DeviceConfigInterface
        public java.lang.String getProperty(java.lang.String namespace, java.lang.String name) {
            return android.provider.DeviceConfig.getProperty(namespace, name);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public java.lang.String getString(java.lang.String namespace, java.lang.String name, java.lang.String defaultValue) {
            return android.provider.DeviceConfig.getString(namespace, name, defaultValue);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public int getInt(java.lang.String namespace, java.lang.String name, int defaultValue) {
            return android.provider.DeviceConfig.getInt(namespace, name, defaultValue);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public long getLong(java.lang.String namespace, java.lang.String name, long defaultValue) {
            return android.provider.DeviceConfig.getLong(namespace, name, defaultValue);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public boolean getBoolean(java.lang.String namespace, java.lang.String name, boolean defaultValue) {
            return android.provider.DeviceConfig.getBoolean(namespace, name, defaultValue);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public float getFloat(java.lang.String namespace, java.lang.String name, float defaultValue) {
            return android.provider.DeviceConfig.getFloat(namespace, name, defaultValue);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public void addOnPropertiesChangedListener(java.lang.String namespace, java.util.concurrent.Executor executor, android.provider.DeviceConfig.OnPropertiesChangedListener listener) {
            android.provider.DeviceConfig.addOnPropertiesChangedListener(namespace, executor, listener);
        }

        @Override // com.android.server.utils.DeviceConfigInterface
        public void removeOnPropertiesChangedListener(android.provider.DeviceConfig.OnPropertiesChangedListener listener) {
            android.provider.DeviceConfig.removeOnPropertiesChangedListener(listener);
        }
    };

    void addOnPropertiesChangedListener(java.lang.String str, java.util.concurrent.Executor executor, android.provider.DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener);

    boolean getBoolean(java.lang.String str, java.lang.String str2, boolean z);

    float getFloat(java.lang.String str, java.lang.String str2, float f);

    int getInt(java.lang.String str, java.lang.String str2, int i);

    long getLong(java.lang.String str, java.lang.String str2, long j);

    java.lang.String getProperty(java.lang.String str, java.lang.String str2);

    java.lang.String getString(java.lang.String str, java.lang.String str2, java.lang.String str3);

    void removeOnPropertiesChangedListener(android.provider.DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener);
}
