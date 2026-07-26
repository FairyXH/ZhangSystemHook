package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameServiceConfiguration {
    private final com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration mGameServiceComponentConfiguration;
    private final java.lang.String mPackageName;

    GameServiceConfiguration(java.lang.String packageName, com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration gameServiceComponentConfiguration) {
        java.util.Objects.requireNonNull(packageName);
        this.mPackageName = packageName;
        this.mGameServiceComponentConfiguration = gameServiceComponentConfiguration;
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration getGameServiceComponentConfiguration() {
        return this.mGameServiceComponentConfiguration;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.app.GameServiceConfiguration)) {
            return false;
        }
        com.android.server.app.GameServiceConfiguration that = (com.android.server.app.GameServiceConfiguration) o;
        return android.text.TextUtils.equals(this.mPackageName, that.mPackageName) && java.util.Objects.equals(this.mGameServiceComponentConfiguration, that.mGameServiceComponentConfiguration);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mPackageName, this.mGameServiceComponentConfiguration);
    }

    public java.lang.String toString() {
        return "GameServiceConfiguration{packageName=" + this.mPackageName + ", gameServiceComponentConfiguration=" + this.mGameServiceComponentConfiguration + '}';
    }

    static final class GameServiceComponentConfiguration {
        private final android.content.ComponentName mGameServiceComponentName;
        private final android.content.ComponentName mGameSessionServiceComponentName;
        private final android.os.UserHandle mUserHandle;

        GameServiceComponentConfiguration(android.os.UserHandle userHandle, android.content.ComponentName gameServiceComponentName, android.content.ComponentName gameSessionServiceComponentName) {
            java.util.Objects.requireNonNull(userHandle);
            java.util.Objects.requireNonNull(gameServiceComponentName);
            java.util.Objects.requireNonNull(gameSessionServiceComponentName);
            this.mUserHandle = userHandle;
            this.mGameServiceComponentName = gameServiceComponentName;
            this.mGameSessionServiceComponentName = gameSessionServiceComponentName;
        }

        public android.os.UserHandle getUserHandle() {
            return this.mUserHandle;
        }

        public android.content.ComponentName getGameServiceComponentName() {
            return this.mGameServiceComponentName;
        }

        public android.content.ComponentName getGameSessionServiceComponentName() {
            return this.mGameSessionServiceComponentName;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration)) {
                return false;
            }
            com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration that = (com.android.server.app.GameServiceConfiguration.GameServiceComponentConfiguration) o;
            return this.mUserHandle.equals(that.mUserHandle) && this.mGameServiceComponentName.equals(that.mGameServiceComponentName) && this.mGameSessionServiceComponentName.equals(that.mGameSessionServiceComponentName);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.mUserHandle, this.mGameServiceComponentName, this.mGameSessionServiceComponentName);
        }

        public java.lang.String toString() {
            return "GameServiceComponentConfiguration{userHandle=" + this.mUserHandle + ", gameServiceComponentName=" + this.mGameServiceComponentName + ", gameSessionServiceComponentName=" + this.mGameSessionServiceComponentName + "}";
        }
    }
}
