package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
public abstract class UserSettingDeviceConfigMediator {
    private static final java.lang.String TAG = com.android.server.utils.UserSettingDeviceConfigMediator.class.getSimpleName();
    protected android.provider.DeviceConfig.Properties mProperties;
    protected final android.util.KeyValueListParser mSettingsParser;

    public abstract boolean getBoolean(java.lang.String str, boolean z);

    public abstract float getFloat(java.lang.String str, float f);

    public abstract int getInt(java.lang.String str, int i);

    public abstract long getLong(java.lang.String str, long j);

    public abstract java.lang.String getString(java.lang.String str, java.lang.String str2);

    protected UserSettingDeviceConfigMediator(char keyValueListDelimiter) {
        this.mSettingsParser = new android.util.KeyValueListParser(keyValueListDelimiter);
    }

    public void setSettingsString(java.lang.String settings) {
        this.mSettingsParser.setString(settings);
    }

    public void setDeviceConfigProperties(android.provider.DeviceConfig.Properties properties) {
        this.mProperties = properties;
    }

    public static class SettingsOverridesAllMediator extends com.android.server.utils.UserSettingDeviceConfigMediator {
        public SettingsOverridesAllMediator(char keyValueListDelimiter) {
            super(keyValueListDelimiter);
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public boolean getBoolean(java.lang.String key, boolean defaultValue) {
            if (this.mSettingsParser.size() == 0) {
                return this.mProperties == null ? defaultValue : this.mProperties.getBoolean(key, defaultValue);
            }
            return this.mSettingsParser.getBoolean(key, defaultValue);
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public float getFloat(java.lang.String key, float defaultValue) {
            if (this.mSettingsParser.size() == 0) {
                return this.mProperties == null ? defaultValue : this.mProperties.getFloat(key, defaultValue);
            }
            return this.mSettingsParser.getFloat(key, defaultValue);
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public int getInt(java.lang.String key, int defaultValue) {
            if (this.mSettingsParser.size() == 0) {
                return this.mProperties == null ? defaultValue : this.mProperties.getInt(key, defaultValue);
            }
            return this.mSettingsParser.getInt(key, defaultValue);
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public long getLong(java.lang.String key, long defaultValue) {
            if (this.mSettingsParser.size() == 0) {
                return this.mProperties == null ? defaultValue : this.mProperties.getLong(key, defaultValue);
            }
            return this.mSettingsParser.getLong(key, defaultValue);
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
            if (this.mSettingsParser.size() == 0) {
                return this.mProperties == null ? defaultValue : this.mProperties.getString(key, defaultValue);
            }
            return this.mSettingsParser.getString(key, defaultValue);
        }
    }

    public static class SettingsOverridesIndividualMediator extends com.android.server.utils.UserSettingDeviceConfigMediator {
        public SettingsOverridesIndividualMediator(char keyValueListDelimiter) {
            super(keyValueListDelimiter);
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public boolean getBoolean(java.lang.String key, boolean defaultValue) {
            return this.mSettingsParser.getBoolean(key, this.mProperties == null ? defaultValue : this.mProperties.getBoolean(key, defaultValue));
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public float getFloat(java.lang.String key, float defaultValue) {
            return this.mSettingsParser.getFloat(key, this.mProperties == null ? defaultValue : this.mProperties.getFloat(key, defaultValue));
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public int getInt(java.lang.String key, int defaultValue) {
            return this.mSettingsParser.getInt(key, this.mProperties == null ? defaultValue : this.mProperties.getInt(key, defaultValue));
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public long getLong(java.lang.String key, long defaultValue) {
            return this.mSettingsParser.getLong(key, this.mProperties == null ? defaultValue : this.mProperties.getLong(key, defaultValue));
        }

        @Override // com.android.server.utils.UserSettingDeviceConfigMediator
        public java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
            return this.mSettingsParser.getString(key, this.mProperties == null ? defaultValue : this.mProperties.getString(key, defaultValue));
        }
    }
}
