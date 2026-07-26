package com.android.server.contentprotection;

/* JADX INFO: loaded from: classes.dex */
public class ContentProtectionConsentManager {
    private static final java.lang.String KEY_CONTENT_PROTECTION_USER_CONSENT = "content_protection_user_consent";
    private static final java.lang.String KEY_PACKAGE_VERIFIER_USER_CONSENT = "package_verifier_user_consent";
    private static final java.lang.String TAG = "ContentProtectionConsentManager";
    private volatile boolean mCachedContentProtectionUserConsent;
    private volatile boolean mCachedPackageVerifierConsent;
    public final android.database.ContentObserver mContentObserver;
    private final android.content.ContentResolver mContentResolver;
    private final android.app.admin.DevicePolicyCache mDevicePolicyCache;
    private final android.app.admin.DevicePolicyManagerInternal mDevicePolicyManagerInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);

    public ContentProtectionConsentManager(android.os.Handler handler, android.content.ContentResolver contentResolver, android.app.admin.DevicePolicyCache devicePolicyCache) {
        this.mContentResolver = contentResolver;
        this.mDevicePolicyCache = devicePolicyCache;
        this.mContentObserver = new com.android.server.contentprotection.ContentProtectionConsentManager.SettingsObserver(handler);
        registerSettingsGlobalObserver(KEY_PACKAGE_VERIFIER_USER_CONSENT);
        registerSettingsGlobalObserver(KEY_CONTENT_PROTECTION_USER_CONSENT);
        readPackageVerifierConsentGranted();
        readContentProtectionUserConsentGranted();
    }

    public boolean isConsentGranted(int userId) {
        return this.mCachedPackageVerifierConsent && isContentProtectionConsentGranted(userId);
    }

    private boolean isPackageVerifierConsentGranted() {
        return android.provider.Settings.Global.getInt(this.mContentResolver, KEY_PACKAGE_VERIFIER_USER_CONSENT, 0) >= 1;
    }

    private boolean isContentProtectionUserConsentGranted() {
        return android.provider.Settings.Global.getInt(this.mContentResolver, KEY_CONTENT_PROTECTION_USER_CONSENT, 0) >= 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readPackageVerifierConsentGranted() {
        this.mCachedPackageVerifierConsent = isPackageVerifierConsentGranted();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readContentProtectionUserConsentGranted() {
        this.mCachedContentProtectionUserConsent = isContentProtectionUserConsentGranted();
    }

    private boolean isUserOrganizationManaged(int userId) {
        return this.mDevicePolicyManagerInternal.isUserOrganizationManaged(userId);
    }

    private boolean isContentProtectionPolicyGranted(int userId) {
        if (!com.android.internal.hidden_from_bootclasspath.android.view.contentprotection.flags.Flags.manageDevicePolicyEnabled()) {
            return false;
        }
        int policy = this.mDevicePolicyCache.getContentProtectionPolicy(userId);
        switch (policy) {
            case 0:
                return this.mCachedContentProtectionUserConsent;
            case 1:
            default:
                return false;
            case 2:
                return true;
        }
    }

    private boolean isContentProtectionConsentGranted(int userId) {
        if (!com.android.internal.hidden_from_bootclasspath.android.view.contentprotection.flags.Flags.manageDevicePolicyEnabled()) {
            return this.mCachedContentProtectionUserConsent && !isUserOrganizationManaged(userId);
        }
        if (isUserOrganizationManaged(userId)) {
            return isContentProtectionPolicyGranted(userId);
        }
        return this.mCachedContentProtectionUserConsent;
    }

    private void registerSettingsGlobalObserver(java.lang.String key) {
        registerSettingsObserver(android.provider.Settings.Global.getUriFor(key));
    }

    private void registerSettingsObserver(android.net.Uri uri) {
        this.mContentResolver.registerContentObserver(uri, false, this.mContentObserver, -1);
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0027  */
        @Override // android.database.ContentObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onChange(boolean r4, android.net.Uri r5, int r6) {
            /*
                r3 = this;
                if (r5 != 0) goto L3
                return
            L3:
                java.lang.String r0 = r5.getLastPathSegment()
                if (r0 != 0) goto La
                return
            La:
                int r1 = r0.hashCode()
                switch(r1) {
                    case 480463670: goto L1c;
                    case 802188678: goto L12;
                    default: goto L11;
                }
            L11:
                goto L27
            L12:
                java.lang.String r1 = "content_protection_user_consent"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L11
                r1 = 1
                goto L28
            L1c:
                java.lang.String r1 = "package_verifier_user_consent"
                boolean r1 = r0.equals(r1)
                if (r1 == 0) goto L11
                r1 = 0
                goto L28
            L27:
                r1 = -1
            L28:
                switch(r1) {
                    case 0: goto L4a;
                    case 1: goto L44;
                    default: goto L2b;
                }
            L2b:
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "Ignoring unexpected property: "
                java.lang.StringBuilder r1 = r1.append(r2)
                java.lang.StringBuilder r1 = r1.append(r0)
                java.lang.String r1 = r1.toString()
                java.lang.String r2 = "ContentProtectionConsentManager"
                android.util.Slog.w(r2, r1)
                return
            L44:
                com.android.server.contentprotection.ContentProtectionConsentManager r1 = com.android.server.contentprotection.ContentProtectionConsentManager.this
                com.android.server.contentprotection.ContentProtectionConsentManager.m3041$$Nest$mreadContentProtectionUserConsentGranted(r1)
                return
            L4a:
                com.android.server.contentprotection.ContentProtectionConsentManager r1 = com.android.server.contentprotection.ContentProtectionConsentManager.this
                com.android.server.contentprotection.ContentProtectionConsentManager.m3042$$Nest$mreadPackageVerifierConsentGranted(r1)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.contentprotection.ContentProtectionConsentManager.SettingsObserver.onChange(boolean, android.net.Uri, int):void");
        }
    }
}
