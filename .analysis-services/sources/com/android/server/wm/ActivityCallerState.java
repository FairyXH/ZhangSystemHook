package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class ActivityCallerState {
    private static final java.lang.String ATTR_CALLER_IS_SHARE_ENABLED = "caller_is_share_enabled";
    private static final java.lang.String ATTR_CALLER_PACKAGE = "caller_package";
    private static final java.lang.String ATTR_CALLER_UID = "caller_uid";
    private static final java.lang.String ATTR_PREFIX = "prefix";
    private static final java.lang.String ATTR_SOURCE_USER_ID = "source_user_id";
    private static final java.lang.String ATTR_URI = "uri";
    private static final java.lang.String TAG = "ActivityTaskManager";
    private static final java.lang.String TAG_INACCESSIBLE_CONTENT_URI = "inaccessible_content_uri";
    private static final java.lang.String TAG_READABLE_CONTENT_URI = "readable_content_uri";
    private static final java.lang.String TAG_WRITABLE_CONTENT_URI = "writable_content_uri";
    final com.android.server.wm.ActivityTaskManagerService mAtmService;
    private final java.util.WeakHashMap<android.os.IBinder, com.android.server.wm.ActivityCallerState.CallerInfo> mCallerTokenInfoMap = new java.util.WeakHashMap<>();

    ActivityCallerState(com.android.server.wm.ActivityTaskManagerService service) {
        this.mAtmService = service;
    }

    com.android.server.wm.ActivityCallerState.CallerInfo getCallerInfoOrNull(android.os.IBinder callerToken) {
        return this.mCallerTokenInfoMap.getOrDefault(callerToken, null);
    }

    boolean hasCaller(android.os.IBinder callerToken) {
        return getCallerInfoOrNull(callerToken) != null;
    }

    int getUid(android.os.IBinder callerToken) {
        com.android.server.wm.ActivityCallerState.CallerInfo callerInfo = getCallerInfoOrNull(callerToken);
        if (callerInfo != null) {
            return callerInfo.mUid;
        }
        return -1;
    }

    java.lang.String getPackage(android.os.IBinder callerToken) {
        com.android.server.wm.ActivityCallerState.CallerInfo callerInfo = getCallerInfoOrNull(callerToken);
        if (callerInfo != null) {
            return callerInfo.mPackageName;
        }
        return null;
    }

    boolean isShareIdentityEnabled(android.os.IBinder callerToken) {
        com.android.server.wm.ActivityCallerState.CallerInfo callerInfo = getCallerInfoOrNull(callerToken);
        if (callerInfo != null) {
            return callerInfo.mIsShareIdentityEnabled;
        }
        return false;
    }

    void add(android.os.IBinder callerToken, com.android.server.wm.ActivityCallerState.CallerInfo callerInfo) {
        this.mCallerTokenInfoMap.put(callerToken, callerInfo);
    }

    void computeCallerInfo(android.os.IBinder callerToken, android.content.Intent intent, int callerUid, java.lang.String callerPackageName, boolean isCallerShareIdentityEnabled) {
        com.android.server.wm.ActivityCallerState.CallerInfo callerInfo = new com.android.server.wm.ActivityCallerState.CallerInfo(callerUid, callerPackageName, isCallerShareIdentityEnabled);
        this.mCallerTokenInfoMap.put(callerToken, callerInfo);
        android.util.ArraySet<android.net.Uri> contentUris = getContentUrisFromIntent(intent);
        for (int i = contentUris.size() - 1; i >= 0; i--) {
            android.net.Uri contentUri = contentUris.valueAt(i);
            boolean hasRead = addContentUriIfUidHasPermission(contentUri, callerUid, 1, callerInfo.mReadableContentUris);
            boolean hasWrite = addContentUriIfUidHasPermission(contentUri, callerUid, 2, callerInfo.mWritableContentUris);
            if (!hasRead && !hasWrite) {
                callerInfo.mInaccessibleContentUris.add(convertToGrantUri(contentUri, 0, callerUid));
            }
        }
    }

    boolean checkContentUriPermission(android.os.IBinder callerToken, com.android.server.uri.GrantUri grantUri, int modeFlags) {
        if (!android.content.Intent.isAccessUriMode(modeFlags)) {
            throw new java.lang.IllegalArgumentException("Mode flags are not access URI mode flags: " + modeFlags);
        }
        com.android.server.wm.ActivityCallerState.CallerInfo callerInfo = this.mCallerTokenInfoMap.getOrDefault(callerToken, null);
        if (callerInfo == null) {
            android.util.Slog.e(TAG, "Caller not found for checkContentUriPermission of: " + grantUri.uri.toSafeString());
            return false;
        }
        if (callerInfo.mInaccessibleContentUris.contains(grantUri)) {
            return false;
        }
        boolean readMet = callerInfo.mReadableContentUris.contains(grantUri);
        boolean writeMet = callerInfo.mWritableContentUris.contains(grantUri);
        if (!readMet && !writeMet) {
            throw new java.lang.IllegalArgumentException("The supplied URI wasn't passed at launch in #getData, #EXTRA_STREAM, nor #getClipData: " + grantUri.uri.toSafeString());
        }
        boolean checkRead = (modeFlags & 1) != 0;
        if (checkRead && !readMet) {
            return false;
        }
        boolean checkWrite = (modeFlags & 2) != 0;
        return !checkWrite || writeMet;
    }

    private boolean addContentUriIfUidHasPermission(android.net.Uri contentUri, int uid, int modeFlags, android.util.ArraySet<com.android.server.uri.GrantUri> grantUris) {
        com.android.server.uri.GrantUri grantUri = convertToGrantUri(contentUri, modeFlags, uid);
        if (this.mAtmService.mUgmInternal.checkUriPermission(grantUri, uid, modeFlags, true)) {
            grantUris.add(grantUri);
            return true;
        }
        return false;
    }

    private static com.android.server.uri.GrantUri convertToGrantUri(android.net.Uri contentUri, int modeFlags, int uid) {
        return new com.android.server.uri.GrantUri(android.content.ContentProvider.getUserIdFromUri(contentUri, android.os.UserHandle.getUserId(uid)), android.content.ContentProvider.getUriWithoutUserId(contentUri), modeFlags);
    }

    private static android.util.ArraySet<android.net.Uri> getContentUrisFromIntent(android.content.Intent intent) {
        android.util.ArraySet<android.net.Uri> uris = new android.util.ArraySet<>();
        if (intent == null) {
            return uris;
        }
        addUriIfContentUri(intent.getData(), uris);
        if (intent.hasExtra("android.intent.extra.STREAM")) {
            java.util.ArrayList<android.net.Uri> streams = tryToUnparcelArrayListExtraStreamsUri(intent);
            if (streams == null) {
                addUriIfContentUri(tryToUnparcelExtraStreamUri(intent), uris);
            } else {
                for (int i = streams.size() - 1; i >= 0; i--) {
                    addUriIfContentUri(streams.get(i), uris);
                }
            }
        }
        android.content.ClipData clipData = intent.getClipData();
        if (clipData == null) {
            return uris;
        }
        for (int i2 = 0; i2 < clipData.getItemCount(); i2++) {
            android.content.ClipData.Item item = clipData.getItemAt(i2);
            addUriIfContentUri(item.getUri(), uris);
            uris.addAll((android.util.ArraySet<? extends android.net.Uri>) getContentUrisFromIntent(item.getIntent()));
        }
        return uris;
    }

    private static android.net.Uri tryToUnparcelExtraStreamUri(android.content.Intent intent) {
        try {
            return (android.net.Uri) intent.getParcelableExtra("android.intent.extra.STREAM", android.net.Uri.class);
        } catch (android.os.BadParcelableException e) {
            android.util.Slog.w(TAG, "Failed to unparcel an URI in EXTRA_STREAM, returning null: " + e);
            return null;
        }
    }

    private static java.util.ArrayList<android.net.Uri> tryToUnparcelArrayListExtraStreamsUri(android.content.Intent intent) {
        try {
            return intent.getParcelableArrayListExtra("android.intent.extra.STREAM", android.net.Uri.class);
        } catch (android.os.BadParcelableException e) {
            android.util.Slog.w(TAG, "Failed to unparcel an ArrayList of URIs in EXTRA_STREAM, returning null: " + e);
            return null;
        }
    }

    private static void addUriIfContentUri(android.net.Uri uri, android.util.ArraySet<android.net.Uri> uris) {
        if (uri != null && com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            uris.add(uri);
        }
    }

    public static final class CallerInfo {
        final boolean mIsShareIdentityEnabled;
        final java.lang.String mPackageName;
        final int mUid;
        final android.util.ArraySet<com.android.server.uri.GrantUri> mReadableContentUris = new android.util.ArraySet<>();
        final android.util.ArraySet<com.android.server.uri.GrantUri> mWritableContentUris = new android.util.ArraySet<>();
        final android.util.ArraySet<com.android.server.uri.GrantUri> mInaccessibleContentUris = new android.util.ArraySet<>();

        CallerInfo(int uid, java.lang.String packageName, boolean isShareIdentityEnabled) {
            this.mUid = uid;
            this.mPackageName = packageName;
            this.mIsShareIdentityEnabled = isShareIdentityEnabled;
        }

        public void saveToXml(com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            out.attributeInt((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_CALLER_UID, this.mUid);
            if (this.mPackageName != null) {
                out.attribute((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_CALLER_PACKAGE, this.mPackageName);
            }
            out.attributeBoolean((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_CALLER_IS_SHARE_ENABLED, this.mIsShareIdentityEnabled);
            for (int i = this.mReadableContentUris.size() - 1; i >= 0; i--) {
                saveGrantUriToXml(out, this.mReadableContentUris.valueAt(i), com.android.server.wm.ActivityCallerState.TAG_READABLE_CONTENT_URI);
            }
            for (int i2 = this.mWritableContentUris.size() - 1; i2 >= 0; i2--) {
                saveGrantUriToXml(out, this.mWritableContentUris.valueAt(i2), com.android.server.wm.ActivityCallerState.TAG_WRITABLE_CONTENT_URI);
            }
            for (int i3 = this.mInaccessibleContentUris.size() - 1; i3 >= 0; i3--) {
                saveGrantUriToXml(out, this.mInaccessibleContentUris.valueAt(i3), com.android.server.wm.ActivityCallerState.TAG_INACCESSIBLE_CONTENT_URI);
            }
        }

        public static com.android.server.wm.ActivityCallerState.CallerInfo restoreFromXml(com.android.modules.utils.TypedXmlPullParser in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int uid = in.getAttributeInt((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_CALLER_UID, 0);
            java.lang.String packageName = in.getAttributeValue((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_CALLER_PACKAGE);
            boolean isShareIdentityEnabled = in.getAttributeBoolean((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_CALLER_IS_SHARE_ENABLED, false);
            com.android.server.wm.ActivityCallerState.CallerInfo callerInfo = new com.android.server.wm.ActivityCallerState.CallerInfo(uid, packageName, isShareIdentityEnabled);
            int outerDepth = in.getDepth();
            while (true) {
                int event = in.next();
                if (event == 1 || (event == 3 && in.getDepth() < outerDepth)) {
                    break;
                }
                if (event == 2) {
                    java.lang.String name = in.getName();
                    if (com.android.server.wm.ActivityCallerState.TAG_READABLE_CONTENT_URI.equals(name)) {
                        callerInfo.mReadableContentUris.add(restoreGrantUriFromXml(in));
                    } else if (com.android.server.wm.ActivityCallerState.TAG_WRITABLE_CONTENT_URI.equals(name)) {
                        callerInfo.mWritableContentUris.add(restoreGrantUriFromXml(in));
                    } else if (com.android.server.wm.ActivityCallerState.TAG_INACCESSIBLE_CONTENT_URI.equals(name)) {
                        callerInfo.mInaccessibleContentUris.add(restoreGrantUriFromXml(in));
                    } else {
                        android.util.Slog.w(com.android.server.wm.ActivityCallerState.TAG, "restoreActivity: unexpected name=" + name);
                        com.android.internal.util.XmlUtils.skipCurrentTag(in);
                    }
                }
            }
            return callerInfo;
        }

        private void saveGrantUriToXml(com.android.modules.utils.TypedXmlSerializer out, com.android.server.uri.GrantUri grantUri, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            out.startTag((java.lang.String) null, tag);
            out.attributeInt((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_SOURCE_USER_ID, grantUri.sourceUserId);
            out.attribute((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_URI, java.lang.String.valueOf(grantUri.uri));
            out.attributeBoolean((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_PREFIX, grantUri.prefix);
            out.endTag((java.lang.String) null, tag);
        }

        private static com.android.server.uri.GrantUri restoreGrantUriFromXml(com.android.modules.utils.TypedXmlPullParser in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int sourceUserId = in.getAttributeInt((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_SOURCE_USER_ID, 0);
            android.net.Uri uri = android.net.Uri.parse(in.getAttributeValue((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_URI));
            boolean prefix = in.getAttributeBoolean((java.lang.String) null, com.android.server.wm.ActivityCallerState.ATTR_PREFIX, false);
            return new com.android.server.uri.GrantUri(sourceUserId, uri, prefix ? 128 : 0);
        }
    }
}
