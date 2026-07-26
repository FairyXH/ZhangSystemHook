package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class ViewState {
    public static final int STATE_AUTOFILLED = 4;
    public static final int STATE_AUTOFILLED_ONCE = 2048;
    public static final int STATE_AUTOFILL_FAILED = 1024;
    public static final int STATE_CHANGED = 8;
    public static final int STATE_CHAR_REMOVED = 16384;
    public static final int STATE_FILLABLE = 2;
    public static final int STATE_FILL_DIALOG_SHOWN = 131072;
    public static final int STATE_IGNORED = 128;
    public static final int STATE_INITIAL = 1;
    public static final int STATE_INLINE_DISABLED = 32768;
    public static final int STATE_INLINE_SHOWN = 8192;
    public static final int STATE_PENDING_CREATE_INLINE_REQUEST = 65536;
    public static final int STATE_RESTARTED_SESSION = 256;
    public static final int STATE_STARTED_PARTITION = 32;
    public static final int STATE_STARTED_SESSION = 16;
    public static final int STATE_TRIGGERED_AUGMENTED_AUTOFILL = 4096;
    public static final int STATE_URL_BAR = 512;
    public static final int STATE_WAITING_DATASET_AUTH = 64;
    private static final java.lang.String TAG = "ViewState";
    public final android.view.autofill.AutofillId id;
    private android.view.autofill.AutofillValue mAutofilledValue;
    private android.view.autofill.AutofillValue mCandidateSaveValue;
    private android.view.autofill.AutofillValue mCurrentValue;
    private java.lang.String mDatasetId;
    private final boolean mIsPrimaryCredential;
    private final com.android.server.autofill.ViewState.Listener mListener;
    private android.service.autofill.FillResponse mPrimaryFillResponse;
    private android.view.autofill.AutofillValue mSanitizedValue;
    private android.service.autofill.FillResponse mSecondaryFillResponse;
    private int mState;
    private android.graphics.Rect mVirtualBounds;

    interface Listener {
        void onFillReady(android.service.autofill.FillResponse fillResponse, android.view.autofill.AutofillId autofillId, android.view.autofill.AutofillValue autofillValue, int i);
    }

    ViewState(android.view.autofill.AutofillId id, com.android.server.autofill.ViewState.Listener listener, int state, boolean isPrimaryCredential) {
        this.id = id;
        this.mListener = listener;
        this.mState = state;
        this.mIsPrimaryCredential = isPrimaryCredential;
    }

    android.graphics.Rect getVirtualBounds() {
        return this.mVirtualBounds;
    }

    android.view.autofill.AutofillValue getCurrentValue() {
        return this.mCurrentValue;
    }

    void setCurrentValue(android.view.autofill.AutofillValue value) {
        this.mCurrentValue = value;
    }

    android.view.autofill.AutofillValue getCandidateSaveValue() {
        return this.mCandidateSaveValue;
    }

    void setCandidateSaveValue(android.view.autofill.AutofillValue value) {
        this.mCandidateSaveValue = value;
    }

    android.view.autofill.AutofillValue getAutofilledValue() {
        return this.mAutofilledValue;
    }

    void setAutofilledValue(android.view.autofill.AutofillValue value) {
        this.mAutofilledValue = value;
    }

    android.view.autofill.AutofillValue getSanitizedValue() {
        return this.mSanitizedValue;
    }

    void setSanitizedValue(android.view.autofill.AutofillValue value) {
        this.mSanitizedValue = value;
    }

    android.service.autofill.FillResponse getResponse() {
        return this.mPrimaryFillResponse;
    }

    android.service.autofill.FillResponse getSecondaryResponse() {
        return this.mSecondaryFillResponse;
    }

    void setResponse(android.service.autofill.FillResponse response) {
        setResponse(response, true);
    }

    void setResponse(android.service.autofill.FillResponse response, boolean isPrimary) {
        if (isPrimary) {
            this.mPrimaryFillResponse = response;
        } else {
            this.mSecondaryFillResponse = response;
        }
    }

    int getState() {
        return this.mState;
    }

    java.lang.String getStateAsString() {
        return getStateAsString(this.mState);
    }

    static java.lang.String getStateAsString(int state) {
        return android.util.DebugUtils.flagsToString(com.android.server.autofill.ViewState.class, "STATE_", state);
    }

    void setState(int state) {
        if (this.mState == 1) {
            this.mState = state;
        } else {
            this.mState |= state;
        }
        if (state == 4) {
            this.mState |= 2048;
        }
    }

    void resetState(int state) {
        this.mState &= ~state;
    }

    java.lang.String getDatasetId() {
        return this.mDatasetId;
    }

    void setDatasetId(java.lang.String datasetId) {
        this.mDatasetId = datasetId;
    }

    void update(android.view.autofill.AutofillValue autofillValue, android.graphics.Rect virtualBounds, int flags) {
        if (autofillValue != null) {
            this.mCurrentValue = autofillValue;
        }
        if (virtualBounds != null) {
            this.mVirtualBounds = virtualBounds;
        }
        maybeCallOnFillReady(flags);
    }

    void maybeCallOnFillReady(int flags) {
        if ((this.mState & 4) != 0 && (flags & 1) == 0) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Ignoring UI for " + this.id + " on " + getStateAsString());
                return;
            }
            return;
        }
        android.service.autofill.FillResponse requestedResponse = requestingPrimaryResponse(flags) ? this.mPrimaryFillResponse : this.mSecondaryFillResponse;
        if (requestedResponse != null) {
            if (requestedResponse.getDatasets() != null || requestedResponse.getAuthentication() != null) {
                this.mListener.onFillReady(requestedResponse, this.id, this.mCurrentValue, flags);
            }
        }
    }

    private boolean requestingPrimaryResponse(int flags) {
        return this.mIsPrimaryCredential ? (flags & 2048) != 0 : (flags & 2048) == 0;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder("ViewState: [id=").append(this.id);
        if (this.mDatasetId != null) {
            builder.append(", datasetId:").append(this.mDatasetId);
        }
        builder.append(", state:").append(getStateAsString());
        if (this.mCurrentValue != null) {
            builder.append(", currentValue:").append(this.mCurrentValue);
        }
        if (this.mCandidateSaveValue != null) {
            builder.append(", candidateSaveValue:").append(this.mCandidateSaveValue);
        }
        if (this.mAutofilledValue != null) {
            builder.append(", autofilledValue:").append(this.mAutofilledValue);
        }
        if (this.mSanitizedValue != null) {
            builder.append(", sanitizedValue:").append(this.mSanitizedValue);
        }
        if (this.mVirtualBounds != null) {
            builder.append(", virtualBounds:").append(this.mVirtualBounds);
        }
        builder.append("]");
        return builder.toString();
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("id:");
        pw.println(this.id);
        if (this.mDatasetId != null) {
            pw.print(prefix);
            pw.print("datasetId:");
            pw.println(this.mDatasetId);
        }
        pw.print(prefix);
        pw.print("state:");
        pw.println(getStateAsString());
        pw.print(prefix);
        pw.print("is primary credential:");
        pw.println(this.mIsPrimaryCredential);
        if (this.mPrimaryFillResponse != null) {
            pw.print(prefix);
            pw.print("primary response id:");
            pw.println(this.mPrimaryFillResponse.getRequestId());
        }
        if (this.mSecondaryFillResponse != null) {
            pw.print(prefix);
            pw.print("secondary response id:");
            pw.println(this.mSecondaryFillResponse.getRequestId());
        }
        if (this.mCurrentValue != null) {
            pw.print(prefix);
            pw.print("currentValue:");
            pw.println(this.mCurrentValue);
        }
        if (this.mAutofilledValue != null) {
            pw.print(prefix);
            pw.print("autofilledValue:");
            pw.println(this.mAutofilledValue);
        }
        if (this.mCandidateSaveValue != null) {
            pw.print(prefix);
            pw.print("candidateSaveValue:");
            pw.println(this.mCandidateSaveValue);
        }
        if (this.mSanitizedValue != null) {
            pw.print(prefix);
            pw.print("sanitizedValue:");
            pw.println(this.mSanitizedValue);
        }
        if (this.mVirtualBounds != null) {
            pw.print(prefix);
            pw.print("virtualBounds:");
            pw.println(this.mVirtualBounds);
        }
    }
}
