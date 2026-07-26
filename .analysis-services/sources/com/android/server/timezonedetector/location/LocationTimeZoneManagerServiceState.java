package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
final class LocationTimeZoneManagerServiceState {
    private final java.lang.String mControllerState;
    private final java.util.List<java.lang.String> mControllerStates;
    private final com.android.server.timezonedetector.LocationAlgorithmEvent mLastEvent;
    private final java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> mPrimaryProviderStates;
    private final java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> mSecondaryProviderStates;

    LocationTimeZoneManagerServiceState(com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder builder) {
        this.mControllerState = builder.mControllerState;
        this.mLastEvent = builder.mLastEvent;
        this.mControllerStates = (java.util.List) java.util.Objects.requireNonNull(builder.mControllerStates);
        this.mPrimaryProviderStates = (java.util.List) java.util.Objects.requireNonNull(builder.mPrimaryProviderStates);
        this.mSecondaryProviderStates = (java.util.List) java.util.Objects.requireNonNull(builder.mSecondaryProviderStates);
    }

    public java.lang.String getControllerState() {
        return this.mControllerState;
    }

    public com.android.server.timezonedetector.LocationAlgorithmEvent getLastEvent() {
        return this.mLastEvent;
    }

    public java.util.List<java.lang.String> getControllerStates() {
        return this.mControllerStates;
    }

    public java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> getPrimaryProviderStates() {
        return java.util.Collections.unmodifiableList(this.mPrimaryProviderStates);
    }

    public java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> getSecondaryProviderStates() {
        return java.util.Collections.unmodifiableList(this.mSecondaryProviderStates);
    }

    public java.lang.String toString() {
        return "LocationTimeZoneManagerServiceState{mControllerState=" + this.mControllerState + ", mLastEvent=" + this.mLastEvent + ", mControllerStates=" + this.mControllerStates + ", mPrimaryProviderStates=" + this.mPrimaryProviderStates + ", mSecondaryProviderStates=" + this.mSecondaryProviderStates + '}';
    }

    static final class Builder {
        private java.lang.String mControllerState;
        private java.util.List<java.lang.String> mControllerStates;
        private com.android.server.timezonedetector.LocationAlgorithmEvent mLastEvent;
        private java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> mPrimaryProviderStates;
        private java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> mSecondaryProviderStates;

        Builder() {
        }

        public com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder setControllerState(java.lang.String stateEnum) {
            this.mControllerState = stateEnum;
            return this;
        }

        com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder setLastEvent(com.android.server.timezonedetector.LocationAlgorithmEvent lastEvent) {
            this.mLastEvent = (com.android.server.timezonedetector.LocationAlgorithmEvent) java.util.Objects.requireNonNull(lastEvent);
            return this;
        }

        public com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder setStateChanges(java.util.List<java.lang.String> states) {
            this.mControllerStates = new java.util.ArrayList(states);
            return this;
        }

        com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder setPrimaryProviderStateChanges(java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> primaryProviderStates) {
            this.mPrimaryProviderStates = new java.util.ArrayList(primaryProviderStates);
            return this;
        }

        com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState.Builder setSecondaryProviderStateChanges(java.util.List<com.android.server.timezonedetector.location.LocationTimeZoneProvider.ProviderState> secondaryProviderStates) {
            this.mSecondaryProviderStates = new java.util.ArrayList(secondaryProviderStates);
            return this;
        }

        com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState build() {
            return new com.android.server.timezonedetector.location.LocationTimeZoneManagerServiceState(this);
        }
    }
}
