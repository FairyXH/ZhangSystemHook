package com.android.server.tv;

/* JADX INFO: loaded from: classes3.dex */
final class PersistentDataStore {
    private static final java.lang.String ATTR_ENABLED = "enabled";
    private static final java.lang.String ATTR_STRING = "string";
    private static final java.lang.String TAG = "TvInputManagerService";
    private static final java.lang.String TAG_BLOCKED_RATINGS = "blocked-ratings";
    private static final java.lang.String TAG_PARENTAL_CONTROLS = "parental-controls";
    private static final java.lang.String TAG_RATING = "rating";
    private static final java.lang.String TAG_TV_INPUT_MANAGER_STATE = "tv-input-manager-state";
    private final android.util.AtomicFile mAtomicFile;
    private boolean mBlockedRatingsChanged;
    private final android.content.Context mContext;
    private boolean mLoaded;
    private boolean mParentalControlsEnabled;
    private boolean mParentalControlsEnabledChanged;
    private final android.os.Handler mHandler = new android.os.Handler();
    private final java.util.List<android.media.tv.TvContentRating> mBlockedRatings = java.util.Collections.synchronizedList(new java.util.ArrayList());
    private final java.lang.Runnable mSaveRunnable = new java.lang.Runnable() { // from class: com.android.server.tv.PersistentDataStore.1
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.tv.PersistentDataStore.this.save();
        }
    };

    public PersistentDataStore(android.content.Context context, int userId) {
        this.mContext = context;
        java.io.File userDir = android.os.Environment.getUserSystemDirectory(userId);
        if (!userDir.exists() && !userDir.mkdirs()) {
            throw new java.lang.IllegalStateException("User dir cannot be created: " + userDir);
        }
        this.mAtomicFile = new android.util.AtomicFile(new java.io.File(userDir, "tv-input-manager-state.xml"), "tv-input-state");
    }

    public boolean isParentalControlsEnabled() {
        loadIfNeeded();
        return this.mParentalControlsEnabled;
    }

    public void setParentalControlsEnabled(boolean enabled) {
        loadIfNeeded();
        if (this.mParentalControlsEnabled != enabled) {
            this.mParentalControlsEnabled = enabled;
            this.mParentalControlsEnabledChanged = true;
            postSave();
        }
    }

    public boolean isRatingBlocked(android.media.tv.TvContentRating rating) {
        loadIfNeeded();
        synchronized (this.mBlockedRatings) {
            for (android.media.tv.TvContentRating blockedRating : this.mBlockedRatings) {
                if (rating.contains(blockedRating)) {
                    return true;
                }
            }
            return false;
        }
    }

    public android.media.tv.TvContentRating[] getBlockedRatings() {
        loadIfNeeded();
        return (android.media.tv.TvContentRating[]) this.mBlockedRatings.toArray(new android.media.tv.TvContentRating[this.mBlockedRatings.size()]);
    }

    public void addBlockedRating(android.media.tv.TvContentRating rating) {
        loadIfNeeded();
        if (rating != null && !this.mBlockedRatings.contains(rating)) {
            this.mBlockedRatings.add(rating);
            this.mBlockedRatingsChanged = true;
            postSave();
        }
    }

    public void removeBlockedRating(android.media.tv.TvContentRating rating) {
        loadIfNeeded();
        if (rating != null && this.mBlockedRatings.contains(rating)) {
            this.mBlockedRatings.remove(rating);
            this.mBlockedRatingsChanged = true;
            postSave();
        }
    }

    private void loadIfNeeded() {
        if (!this.mLoaded) {
            load();
            this.mLoaded = true;
        }
    }

    private void clearState() {
        this.mBlockedRatings.clear();
        this.mParentalControlsEnabled = false;
    }

    private void load() {
        clearState();
        try {
            java.io.InputStream is = this.mAtomicFile.openRead();
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(is);
                loadFromXml(parser);
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException ex) {
                android.util.Slog.w(TAG, "Failed to load tv input manager persistent store data.", ex);
                clearState();
            } finally {
                libcore.io.IoUtils.closeQuietly(is);
            }
        } catch (java.io.FileNotFoundException e) {
        }
    }

    private void postSave() {
        this.mHandler.removeCallbacks(this.mSaveRunnable);
        this.mHandler.post(this.mSaveRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void save() {
        try {
            java.io.FileOutputStream os = this.mAtomicFile.startWrite();
            boolean success = false;
            try {
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(os);
                saveToXml(serializer);
                serializer.flush();
                success = true;
            } finally {
                if (success) {
                    this.mAtomicFile.finishWrite(os);
                    broadcastChangesIfNeeded();
                } else {
                    this.mAtomicFile.failWrite(os);
                }
            }
        } catch (java.io.IOException ex) {
            android.util.Slog.w(TAG, "Failed to save tv input manager persistent store data.", ex);
        }
    }

    private void broadcastChangesIfNeeded() {
        if (this.mParentalControlsEnabledChanged) {
            this.mParentalControlsEnabledChanged = false;
            this.mContext.sendBroadcastAsUser(new android.content.Intent("android.media.tv.action.PARENTAL_CONTROLS_ENABLED_CHANGED"), android.os.UserHandle.ALL);
        }
        if (this.mBlockedRatingsChanged) {
            this.mBlockedRatingsChanged = false;
            this.mContext.sendBroadcastAsUser(new android.content.Intent("android.media.tv.action.BLOCKED_RATINGS_CHANGED"), android.os.UserHandle.ALL);
        }
    }

    private void loadFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.internal.util.XmlUtils.beginDocument(parser, TAG_TV_INPUT_MANAGER_STATE);
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(TAG_BLOCKED_RATINGS)) {
                loadBlockedRatingsFromXml(parser);
            } else if (parser.getName().equals(TAG_PARENTAL_CONTROLS)) {
                this.mParentalControlsEnabled = parser.getAttributeBoolean((java.lang.String) null, "enabled");
            }
        }
    }

    private void loadBlockedRatingsFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(TAG_RATING)) {
                java.lang.String ratingString = parser.getAttributeValue((java.lang.String) null, ATTR_STRING);
                if (android.text.TextUtils.isEmpty(ratingString)) {
                    throw new org.xmlpull.v1.XmlPullParserException("Missing string attribute on rating");
                }
                this.mBlockedRatings.add(android.media.tv.TvContentRating.unflattenFromString(ratingString));
            }
        }
    }

    private void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.startDocument((java.lang.String) null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        serializer.startTag((java.lang.String) null, TAG_TV_INPUT_MANAGER_STATE);
        serializer.startTag((java.lang.String) null, TAG_BLOCKED_RATINGS);
        synchronized (this.mBlockedRatings) {
            for (android.media.tv.TvContentRating rating : this.mBlockedRatings) {
                serializer.startTag((java.lang.String) null, TAG_RATING);
                serializer.attribute((java.lang.String) null, ATTR_STRING, rating.flattenToString());
                serializer.endTag((java.lang.String) null, TAG_RATING);
            }
        }
        serializer.endTag((java.lang.String) null, TAG_BLOCKED_RATINGS);
        serializer.startTag((java.lang.String) null, TAG_PARENTAL_CONTROLS);
        serializer.attributeBoolean((java.lang.String) null, "enabled", this.mParentalControlsEnabled);
        serializer.endTag((java.lang.String) null, TAG_PARENTAL_CONTROLS);
        serializer.endTag((java.lang.String) null, TAG_TV_INPUT_MANAGER_STATE);
        serializer.endDocument();
    }
}
