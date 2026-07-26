package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public class ArchiveState {
    private final java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> mActivityInfos;
    private final long mArchiveTimeMillis;
    private final java.lang.String mInstallerTitle;

    public ArchiveState(java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> activityInfos, java.lang.String installerTitle) {
        this(activityInfos, installerTitle, java.lang.System.currentTimeMillis());
    }

    public static class ArchiveActivityInfo {
        private final java.nio.file.Path mIconBitmap;
        private final java.nio.file.Path mMonochromeIconBitmap;
        private final android.content.ComponentName mOriginalComponentName;
        private final java.lang.String mTitle;

        public ArchiveActivityInfo(java.lang.String title, android.content.ComponentName originalComponentName, java.nio.file.Path iconBitmap, java.nio.file.Path monochromeIconBitmap) {
            this.mTitle = title;
            com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mTitle);
            this.mOriginalComponentName = originalComponentName;
            com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mOriginalComponentName);
            this.mIconBitmap = iconBitmap;
            this.mMonochromeIconBitmap = monochromeIconBitmap;
        }

        public java.lang.String getTitle() {
            return this.mTitle;
        }

        public android.content.ComponentName getOriginalComponentName() {
            return this.mOriginalComponentName;
        }

        public java.nio.file.Path getIconBitmap() {
            return this.mIconBitmap;
        }

        public java.nio.file.Path getMonochromeIconBitmap() {
            return this.mMonochromeIconBitmap;
        }

        public java.lang.String toString() {
            return "ArchiveActivityInfo { title = " + this.mTitle + ", originalComponentName = " + this.mOriginalComponentName + ", iconBitmap = " + this.mIconBitmap + ", monochromeIconBitmap = " + this.mMonochromeIconBitmap + " }";
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo that = (com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo) o;
            if (java.util.Objects.equals(this.mTitle, that.mTitle) && java.util.Objects.equals(this.mOriginalComponentName, that.mOriginalComponentName) && java.util.Objects.equals(this.mIconBitmap, that.mIconBitmap) && java.util.Objects.equals(this.mMonochromeIconBitmap, that.mMonochromeIconBitmap)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int _hash = (1 * 31) + java.util.Objects.hashCode(this.mTitle);
            return (((((_hash * 31) + java.util.Objects.hashCode(this.mOriginalComponentName)) * 31) + java.util.Objects.hashCode(this.mIconBitmap)) * 31) + java.util.Objects.hashCode(this.mMonochromeIconBitmap);
        }

        @java.lang.Deprecated
        private void __metadata() {
        }
    }

    public ArchiveState(java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> activityInfos, java.lang.String installerTitle, long archiveTimeMillis) {
        this.mActivityInfos = activityInfos;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mActivityInfos);
        this.mInstallerTitle = installerTitle;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mInstallerTitle);
        this.mArchiveTimeMillis = archiveTimeMillis;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.CurrentTimeMillisLong.class, (java.lang.annotation.Annotation) null, this.mArchiveTimeMillis);
    }

    public java.util.List<com.android.server.pm.pkg.ArchiveState.ArchiveActivityInfo> getActivityInfos() {
        return this.mActivityInfos;
    }

    public java.lang.String getInstallerTitle() {
        return this.mInstallerTitle;
    }

    public long getArchiveTimeMillis() {
        return this.mArchiveTimeMillis;
    }

    public java.lang.String toString() {
        return "ArchiveState { activityInfos = " + this.mActivityInfos + ", installerTitle = " + this.mInstallerTitle + ", archiveTimeMillis = " + this.mArchiveTimeMillis + " }";
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.pm.pkg.ArchiveState that = (com.android.server.pm.pkg.ArchiveState) o;
        if (java.util.Objects.equals(this.mActivityInfos, that.mActivityInfos) && java.util.Objects.equals(this.mInstallerTitle, that.mInstallerTitle) && this.mArchiveTimeMillis == that.mArchiveTimeMillis) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int _hash = (1 * 31) + java.util.Objects.hashCode(this.mActivityInfos);
        return (((_hash * 31) + java.util.Objects.hashCode(this.mInstallerTitle)) * 31) + java.lang.Long.hashCode(this.mArchiveTimeMillis);
    }

    @java.lang.Deprecated
    private void __metadata() {
    }
}
