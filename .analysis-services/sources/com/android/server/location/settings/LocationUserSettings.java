package com.android.server.location.settings;

/* JADX INFO: loaded from: classes2.dex */
public final class LocationUserSettings implements com.android.server.location.settings.SettingsStore.VersionedSettings {
    private static final int VERSION = 1;
    private final boolean mAdasGnssLocationEnabled;

    private LocationUserSettings(boolean adasGnssLocationEnabled) {
        this.mAdasGnssLocationEnabled = adasGnssLocationEnabled;
    }

    @Override // com.android.server.location.settings.SettingsStore.VersionedSettings
    public int getVersion() {
        return 1;
    }

    public boolean isAdasGnssLocationEnabled() {
        return this.mAdasGnssLocationEnabled;
    }

    public com.android.server.location.settings.LocationUserSettings withAdasGnssLocationEnabled(boolean adasEnabled) {
        if (adasEnabled == this.mAdasGnssLocationEnabled) {
            return this;
        }
        return new com.android.server.location.settings.LocationUserSettings(adasEnabled);
    }

    void write(java.io.DataOutput out) throws java.io.IOException {
        out.writeBoolean(this.mAdasGnssLocationEnabled);
    }

    static com.android.server.location.settings.LocationUserSettings read(android.content.res.Resources resources, int version, java.io.DataInput in) throws java.io.IOException {
        boolean adasGnssLocationEnabled;
        switch (version) {
            case 1:
                adasGnssLocationEnabled = in.readBoolean();
                break;
            default:
                adasGnssLocationEnabled = resources.getBoolean(android.R.bool.config_defaultBatteryPercentageSetting);
                break;
        }
        return new com.android.server.location.settings.LocationUserSettings(adasGnssLocationEnabled);
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.location.settings.LocationUserSettings)) {
            return false;
        }
        com.android.server.location.settings.LocationUserSettings that = (com.android.server.location.settings.LocationUserSettings) o;
        return this.mAdasGnssLocationEnabled == that.mAdasGnssLocationEnabled;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Boolean.valueOf(this.mAdasGnssLocationEnabled));
    }
}
