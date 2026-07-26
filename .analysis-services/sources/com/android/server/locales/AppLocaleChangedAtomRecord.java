package com.android.server.locales;

/* JADX INFO: loaded from: classes2.dex */
public final class AppLocaleChangedAtomRecord {
    private static final java.lang.String DEFAULT_PREFIX = "default-";
    final int mCallingUid;
    java.lang.String mNewLocales;
    java.lang.String mPrevLocales;
    int mTargetUid = -1;
    int mStatus = 0;
    int mCaller = 0;

    AppLocaleChangedAtomRecord(int callingUid) {
        this.mNewLocales = DEFAULT_PREFIX;
        this.mPrevLocales = DEFAULT_PREFIX;
        this.mCallingUid = callingUid;
        java.util.Locale defaultLocale = java.util.Locale.getDefault();
        if (defaultLocale != null) {
            this.mNewLocales = DEFAULT_PREFIX + defaultLocale.toLanguageTag();
            this.mPrevLocales = DEFAULT_PREFIX + defaultLocale.toLanguageTag();
        }
    }

    void setNewLocales(java.lang.String newLocales) {
        this.mNewLocales = convertEmptyLocales(newLocales);
    }

    void setTargetUid(int targetUid) {
        this.mTargetUid = targetUid;
    }

    void setPrevLocales(java.lang.String prevLocales) {
        this.mPrevLocales = convertEmptyLocales(prevLocales);
    }

    void setStatus(int status) {
        this.mStatus = status;
    }

    void setCaller(int caller) {
        this.mCaller = caller;
    }

    private java.lang.String convertEmptyLocales(java.lang.String locales) {
        java.util.Locale defaultLocale;
        if (!"".equals(locales) || (defaultLocale = java.util.Locale.getDefault()) == null) {
            return locales;
        }
        java.lang.String target = DEFAULT_PREFIX + defaultLocale.toLanguageTag();
        return target;
    }
}
