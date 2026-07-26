package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class ConfigurationInternal {
    private final boolean mAutoDetectionEnabledSetting;
    private final boolean mAutoDetectionSupported;
    private final java.time.Instant mAutoSuggestionLowerBound;
    private final java.time.Instant mManualSuggestionLowerBound;
    private final int[] mOriginPriorities;
    private final java.time.Instant mSuggestionUpperBound;
    private final int mSystemClockConfidenceThresholdMillis;
    private final int mSystemClockUpdateThresholdMillis;
    private final boolean mUserConfigAllowed;
    private final int mUserId;

    private ConfigurationInternal(com.android.server.timedetector.ConfigurationInternal.Builder builder) {
        this.mAutoDetectionSupported = builder.mAutoDetectionSupported;
        this.mSystemClockUpdateThresholdMillis = builder.mSystemClockUpdateThresholdMillis;
        this.mSystemClockConfidenceThresholdMillis = builder.mSystemClockConfidenceThresholdMillis;
        this.mAutoSuggestionLowerBound = (java.time.Instant) java.util.Objects.requireNonNull(builder.mAutoSuggestionLowerBound);
        this.mManualSuggestionLowerBound = (java.time.Instant) java.util.Objects.requireNonNull(builder.mManualSuggestionLowerBound);
        this.mSuggestionUpperBound = (java.time.Instant) java.util.Objects.requireNonNull(builder.mSuggestionUpperBound);
        this.mOriginPriorities = (int[]) java.util.Objects.requireNonNull(builder.mOriginPriorities);
        this.mAutoDetectionEnabledSetting = builder.mAutoDetectionEnabledSetting;
        this.mUserId = builder.mUserId;
        this.mUserConfigAllowed = builder.mUserConfigAllowed;
    }

    public boolean isAutoDetectionSupported() {
        return this.mAutoDetectionSupported;
    }

    public int getSystemClockUpdateThresholdMillis() {
        return this.mSystemClockUpdateThresholdMillis;
    }

    public int getSystemClockConfidenceThresholdMillis() {
        return this.mSystemClockConfidenceThresholdMillis;
    }

    public java.time.Instant getAutoSuggestionLowerBound() {
        return this.mAutoSuggestionLowerBound;
    }

    public java.time.Instant getManualSuggestionLowerBound() {
        return this.mManualSuggestionLowerBound;
    }

    public java.time.Instant getSuggestionUpperBound() {
        return this.mSuggestionUpperBound;
    }

    public int[] getAutoOriginPriorities() {
        return this.mOriginPriorities;
    }

    public boolean getAutoDetectionEnabledSetting() {
        return this.mAutoDetectionEnabledSetting;
    }

    public boolean getAutoDetectionEnabledBehavior() {
        return isAutoDetectionSupported() && this.mAutoDetectionEnabledSetting;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public android.os.UserHandle getUserHandle() {
        return android.os.UserHandle.of(this.mUserId);
    }

    public boolean isUserConfigAllowed() {
        return this.mUserConfigAllowed;
    }

    public android.app.time.TimeCapabilitiesAndConfig createCapabilitiesAndConfig(boolean bypassUserPolicyChecks) {
        return new android.app.time.TimeCapabilitiesAndConfig(timeCapabilities(bypassUserPolicyChecks), timeConfiguration());
    }

    private android.app.time.TimeCapabilities timeCapabilities(boolean bypassUserPolicyChecks) {
        int configureAutoDetectionEnabledCapability;
        int suggestManualTimeCapability;
        android.os.UserHandle userHandle = android.os.UserHandle.of(this.mUserId);
        android.app.time.TimeCapabilities.Builder builder = new android.app.time.TimeCapabilities.Builder(userHandle);
        boolean allowConfigDateTime = isUserConfigAllowed() || bypassUserPolicyChecks;
        boolean deviceHasAutoTimeDetection = isAutoDetectionSupported();
        if (!deviceHasAutoTimeDetection) {
            configureAutoDetectionEnabledCapability = 10;
        } else if (!allowConfigDateTime) {
            configureAutoDetectionEnabledCapability = 20;
        } else {
            configureAutoDetectionEnabledCapability = 40;
        }
        builder.setConfigureAutoDetectionEnabledCapability(configureAutoDetectionEnabledCapability);
        if (!allowConfigDateTime) {
            suggestManualTimeCapability = 20;
        } else if (getAutoDetectionEnabledBehavior()) {
            suggestManualTimeCapability = 30;
        } else {
            suggestManualTimeCapability = 40;
        }
        builder.setSetManualTimeCapability(suggestManualTimeCapability);
        return builder.build();
    }

    private android.app.time.TimeConfiguration timeConfiguration() {
        return new android.app.time.TimeConfiguration.Builder().setAutoDetectionEnabled(getAutoDetectionEnabledSetting()).build();
    }

    public com.android.server.timedetector.ConfigurationInternal merge(android.app.time.TimeConfiguration newConfiguration) {
        com.android.server.timedetector.ConfigurationInternal.Builder builder = new com.android.server.timedetector.ConfigurationInternal.Builder(this);
        if (newConfiguration.hasIsAutoDetectionEnabled()) {
            builder.setAutoDetectionEnabledSetting(newConfiguration.isAutoDetectionEnabled());
        }
        return builder.build();
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.timedetector.ConfigurationInternal)) {
            return false;
        }
        com.android.server.timedetector.ConfigurationInternal that = (com.android.server.timedetector.ConfigurationInternal) o;
        return this.mAutoDetectionSupported == that.mAutoDetectionSupported && this.mAutoDetectionEnabledSetting == that.mAutoDetectionEnabledSetting && this.mUserId == that.mUserId && this.mUserConfigAllowed == that.mUserConfigAllowed && this.mSystemClockUpdateThresholdMillis == that.mSystemClockUpdateThresholdMillis && this.mSystemClockConfidenceThresholdMillis == that.mSystemClockConfidenceThresholdMillis && this.mAutoSuggestionLowerBound.equals(that.mAutoSuggestionLowerBound) && this.mManualSuggestionLowerBound.equals(that.mManualSuggestionLowerBound) && this.mSuggestionUpperBound.equals(that.mSuggestionUpperBound) && java.util.Arrays.equals(this.mOriginPriorities, that.mOriginPriorities);
    }

    public int hashCode() {
        int result = java.util.Objects.hash(java.lang.Boolean.valueOf(this.mAutoDetectionSupported), java.lang.Boolean.valueOf(this.mAutoDetectionEnabledSetting), java.lang.Integer.valueOf(this.mUserId), java.lang.Boolean.valueOf(this.mUserConfigAllowed), java.lang.Integer.valueOf(this.mSystemClockUpdateThresholdMillis), java.lang.Integer.valueOf(this.mSystemClockConfidenceThresholdMillis), this.mAutoSuggestionLowerBound, this.mManualSuggestionLowerBound, this.mSuggestionUpperBound);
        return (result * 31) + java.util.Arrays.hashCode(this.mOriginPriorities);
    }

    public java.lang.String toString() {
        java.lang.String originPrioritiesString = (java.lang.String) java.util.Arrays.stream(this.mOriginPriorities).mapToObj(new java.util.function.IntFunction() { // from class: com.android.server.timedetector.ConfigurationInternal$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.timedetector.TimeDetectorStrategy.originToString(i);
            }
        }).collect(java.util.stream.Collectors.joining(",", "[", "]"));
        return "ConfigurationInternal{mAutoDetectionSupported=" + this.mAutoDetectionSupported + ", mSystemClockUpdateThresholdMillis=" + this.mSystemClockUpdateThresholdMillis + ", mSystemClockConfidenceThresholdMillis=" + this.mSystemClockConfidenceThresholdMillis + ", mAutoSuggestionLowerBound=" + this.mAutoSuggestionLowerBound + "(" + this.mAutoSuggestionLowerBound.toEpochMilli() + "), mManualSuggestionLowerBound=" + this.mManualSuggestionLowerBound + "(" + this.mManualSuggestionLowerBound.toEpochMilli() + "), mSuggestionUpperBound=" + this.mSuggestionUpperBound + "(" + this.mSuggestionUpperBound.toEpochMilli() + "), mOriginPriorities=" + originPrioritiesString + ", mAutoDetectionEnabled=" + this.mAutoDetectionEnabledSetting + ", mUserId=" + this.mUserId + ", mUserConfigAllowed=" + this.mUserConfigAllowed + '}';
    }

    static final class Builder {
        private boolean mAutoDetectionEnabledSetting;
        private boolean mAutoDetectionSupported;
        private java.time.Instant mAutoSuggestionLowerBound;
        private java.time.Instant mManualSuggestionLowerBound;
        private int[] mOriginPriorities;
        private java.time.Instant mSuggestionUpperBound;
        private int mSystemClockConfidenceThresholdMillis;
        private int mSystemClockUpdateThresholdMillis;
        private boolean mUserConfigAllowed;
        private final int mUserId;

        Builder(int userId) {
            this.mUserId = userId;
        }

        Builder(com.android.server.timedetector.ConfigurationInternal toCopy) {
            this.mUserId = toCopy.mUserId;
            this.mUserConfigAllowed = toCopy.mUserConfigAllowed;
            this.mAutoDetectionSupported = toCopy.mAutoDetectionSupported;
            this.mSystemClockUpdateThresholdMillis = toCopy.mSystemClockUpdateThresholdMillis;
            this.mAutoSuggestionLowerBound = toCopy.mAutoSuggestionLowerBound;
            this.mManualSuggestionLowerBound = toCopy.mManualSuggestionLowerBound;
            this.mSuggestionUpperBound = toCopy.mSuggestionUpperBound;
            this.mOriginPriorities = toCopy.mOriginPriorities;
            this.mAutoDetectionEnabledSetting = toCopy.mAutoDetectionEnabledSetting;
        }

        com.android.server.timedetector.ConfigurationInternal.Builder setUserConfigAllowed(boolean userConfigAllowed) {
            this.mUserConfigAllowed = userConfigAllowed;
            return this;
        }

        public com.android.server.timedetector.ConfigurationInternal.Builder setAutoDetectionSupported(boolean supported) {
            this.mAutoDetectionSupported = supported;
            return this;
        }

        public com.android.server.timedetector.ConfigurationInternal.Builder setSystemClockUpdateThresholdMillis(int systemClockUpdateThresholdMillis) {
            this.mSystemClockUpdateThresholdMillis = systemClockUpdateThresholdMillis;
            return this;
        }

        public com.android.server.timedetector.ConfigurationInternal.Builder setSystemClockConfidenceThresholdMillis(int thresholdMillis) {
            this.mSystemClockConfidenceThresholdMillis = thresholdMillis;
            return this;
        }

        public com.android.server.timedetector.ConfigurationInternal.Builder setAutoSuggestionLowerBound(java.time.Instant autoSuggestionLowerBound) {
            this.mAutoSuggestionLowerBound = (java.time.Instant) java.util.Objects.requireNonNull(autoSuggestionLowerBound);
            return this;
        }

        public com.android.server.timedetector.ConfigurationInternal.Builder setManualSuggestionLowerBound(java.time.Instant manualSuggestionLowerBound) {
            this.mManualSuggestionLowerBound = (java.time.Instant) java.util.Objects.requireNonNull(manualSuggestionLowerBound);
            return this;
        }

        public com.android.server.timedetector.ConfigurationInternal.Builder setSuggestionUpperBound(java.time.Instant suggestionUpperBound) {
            this.mSuggestionUpperBound = (java.time.Instant) java.util.Objects.requireNonNull(suggestionUpperBound);
            return this;
        }

        public com.android.server.timedetector.ConfigurationInternal.Builder setOriginPriorities(int... originPriorities) {
            this.mOriginPriorities = (int[]) java.util.Objects.requireNonNull(originPriorities);
            return this;
        }

        com.android.server.timedetector.ConfigurationInternal.Builder setAutoDetectionEnabledSetting(boolean autoDetectionEnabledSetting) {
            this.mAutoDetectionEnabledSetting = autoDetectionEnabledSetting;
            return this;
        }

        com.android.server.timedetector.ConfigurationInternal build() {
            return new com.android.server.timedetector.ConfigurationInternal(this);
        }
    }
}
