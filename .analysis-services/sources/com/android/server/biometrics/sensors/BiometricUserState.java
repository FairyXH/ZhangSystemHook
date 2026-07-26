package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public abstract class BiometricUserState<T extends android.hardware.biometrics.BiometricAuthenticator.Identifier> {
    private static final java.lang.String ATTR_INVALIDATION = "authenticatorIdInvalidation_attr";
    private static final boolean DEBUG_FINGER = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String TAG = "UserState";
    private static final java.lang.String TAG_INVALIDATION = "authenticatorIdInvalidation_tag";
    protected final android.content.Context mContext;
    protected final java.io.File mFile;
    protected boolean mInvalidationInProgress;
    protected final java.util.ArrayList<T> mBiometrics = new java.util.ArrayList<>();
    private final java.lang.Runnable mWriteStateRunnable = new java.lang.Runnable() { // from class: com.android.server.biometrics.sensors.BiometricUserState$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.doWriteStateInternal();
        }
    };

    protected abstract void doWriteState(com.android.modules.utils.TypedXmlSerializer typedXmlSerializer) throws java.lang.Exception;

    protected abstract java.lang.String getBiometricsTag();

    protected abstract java.util.ArrayList<T> getCopy(java.util.ArrayList<T> arrayList);

    protected abstract int getNameTemplateResource();

    protected abstract void parseBiometricsLocked(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public void doWriteStateInternal() {
        android.util.AtomicFile destination = new android.util.AtomicFile(this.mFile);
        java.io.FileOutputStream out = null;
        try {
            out = destination.startWrite();
            com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(out);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startDocument((java.lang.String) null, true);
            serializer.startTag((java.lang.String) null, TAG_INVALIDATION);
            serializer.attributeBoolean((java.lang.String) null, ATTR_INVALIDATION, this.mInvalidationInProgress);
            serializer.endTag((java.lang.String) null, TAG_INVALIDATION);
            doWriteState(serializer);
            serializer.endDocument();
            destination.finishWrite(out);
        } finally {
        }
    }

    public BiometricUserState(android.content.Context context, int userId, java.lang.String fileName) {
        this.mFile = getFileForUser(userId, fileName);
        this.mContext = context;
        synchronized (this) {
            readStateSyncLocked();
        }
    }

    public void setInvalidationInProgress(boolean invalidationInProgress) {
        synchronized (this) {
            this.mInvalidationInProgress = invalidationInProgress;
            scheduleWriteStateLocked();
        }
    }

    public boolean isInvalidationInProgress() {
        boolean z;
        synchronized (this) {
            z = this.mInvalidationInProgress;
        }
        return z;
    }

    public void addBiometric(T identifier) {
        synchronized (this) {
            this.mBiometrics.add(identifier);
            scheduleWriteStateLocked();
        }
    }

    public void removeBiometric(int biometricId) {
        synchronized (this) {
            int i = 0;
            while (true) {
                if (i >= this.mBiometrics.size()) {
                    break;
                }
                if (this.mBiometrics.get(i).getBiometricId() != biometricId) {
                    i++;
                } else {
                    this.mBiometrics.remove(i);
                    scheduleWriteStateLocked();
                    break;
                }
            }
        }
    }

    public void renameBiometric(int biometricId, java.lang.CharSequence name) {
        synchronized (this) {
            int i = 0;
            while (true) {
                if (i >= this.mBiometrics.size()) {
                    break;
                }
                if (this.mBiometrics.get(i).getBiometricId() != biometricId) {
                    i++;
                } else {
                    android.hardware.biometrics.BiometricAuthenticator.Identifier identifier = this.mBiometrics.get(i);
                    identifier.setName(name);
                    scheduleWriteStateLocked();
                    break;
                }
            }
        }
    }

    public java.util.List<T> getBiometrics() {
        java.util.ArrayList<T> copy;
        synchronized (this) {
            copy = getCopy(this.mBiometrics);
        }
        return copy;
    }

    public java.lang.String getUniqueName() {
        java.lang.String name;
        int guess = 1;
        while (true) {
            name = this.mContext.getString(getNameTemplateResource(), java.lang.Integer.valueOf(guess));
            if (isUnique(name)) {
                break;
            }
            guess++;
        }
        if (name != null && DEBUG_FINGER) {
            android.util.Slog.d(TAG, "getUniqueName name:" + name + " nameSize:" + name.length());
        }
        return name;
    }

    private boolean isUnique(java.lang.String name) {
        for (T identifier : this.mBiometrics) {
            if (identifier.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private java.io.File getFileForUser(int userId, java.lang.String fileName) {
        return new java.io.File(android.os.Environment.getUserSystemDirectory(userId), fileName);
    }

    private void scheduleWriteStateLocked() {
        android.os.AsyncTask.execute(this.mWriteStateRunnable);
    }

    private void readStateSyncLocked() {
        if (!this.mFile.exists()) {
            return;
        }
        try {
            java.io.FileInputStream in = new java.io.FileInputStream(this.mFile);
            try {
                try {
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                    parseStateLocked(parser);
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    throw new java.lang.IllegalStateException("Failed parsing settings file: " + this.mFile, e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(in);
            }
        } catch (java.io.FileNotFoundException e2) {
            android.util.Slog.i(TAG, "No fingerprint state");
        }
    }

    private void parseStateLocked(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals(getBiometricsTag())) {
                            parseBiometricsLocked(parser);
                        } else if (tagName.equals(TAG_INVALIDATION)) {
                            this.mInvalidationInProgress = parser.getAttributeBoolean((java.lang.String) null, ATTR_INVALIDATION);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }
}
