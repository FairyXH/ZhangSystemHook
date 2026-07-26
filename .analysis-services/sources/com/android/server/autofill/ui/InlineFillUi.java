package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
public final class InlineFillUi {
    private static final java.lang.String TAG = "InlineFillUi";
    final android.view.autofill.AutofillId mAutofillId;
    private final java.util.ArrayList<android.service.autofill.Dataset> mDatasets;
    private boolean mFilterMatchingDisabled;
    private java.lang.String mFilterText;
    private final java.util.ArrayList<android.view.inputmethod.InlineSuggestion> mInlineSuggestions;
    private int mMaxInputLengthForAutofill;

    public interface InlineSuggestionUiCallback {
        void authenticate(int i, int i2);

        void autofill(android.service.autofill.Dataset dataset, int i);

        void onError();

        void onInflate();

        void startIntentSender(android.content.IntentSender intentSender);
    }

    public interface InlineUiEventCallback {
        void notifyInlineUiHidden(android.view.autofill.AutofillId autofillId);

        void notifyInlineUiShown(android.view.autofill.AutofillId autofillId);
    }

    public static com.android.server.autofill.ui.InlineFillUi emptyUi(android.view.autofill.AutofillId autofillId) {
        return new com.android.server.autofill.ui.InlineFillUi(autofillId);
    }

    public static class InlineFillUiInfo {
        public java.lang.String mFilterText;
        public android.view.autofill.AutofillId mFocusedId;
        public android.view.inputmethod.InlineSuggestionsRequest mInlineRequest;
        public com.android.server.autofill.RemoteInlineSuggestionRenderService mRemoteRenderService;
        public int mSessionId;
        public int mUserId;

        public InlineFillUiInfo(android.view.inputmethod.InlineSuggestionsRequest inlineRequest, android.view.autofill.AutofillId focusedId, java.lang.String filterText, com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService, int userId, int sessionId) {
            this.mUserId = userId;
            this.mSessionId = sessionId;
            this.mInlineRequest = inlineRequest;
            this.mFocusedId = focusedId;
            this.mFilterText = filterText;
            this.mRemoteRenderService = remoteRenderService;
        }
    }

    public static com.android.server.autofill.ui.InlineFillUi forAutofill(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, android.service.autofill.FillResponse response, com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback, int maxInputLengthForAutofill) {
        if (response.getAuthentication() != null && response.getInlinePresentation() != null) {
            android.view.inputmethod.InlineSuggestion inlineAuthentication = com.android.server.autofill.ui.InlineSuggestionFactory.createInlineAuthentication(inlineFillUiInfo, response, uiCallback);
            return new com.android.server.autofill.ui.InlineFillUi(inlineFillUiInfo, inlineAuthentication, maxInputLengthForAutofill);
        }
        if (response.getDatasets() != null) {
            boolean ignoreHostSpec = android.service.autofill.Flags.autofillCredmanIntegration() && (response.getFlags() & 8) != 0;
            android.util.SparseArray<android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion>> inlineSuggestions = com.android.server.autofill.ui.InlineSuggestionFactory.createInlineSuggestions(inlineFillUiInfo, "android:autofill", response.getDatasets(), uiCallback, ignoreHostSpec);
            return new com.android.server.autofill.ui.InlineFillUi(inlineFillUiInfo, inlineSuggestions, maxInputLengthForAutofill);
        }
        return new com.android.server.autofill.ui.InlineFillUi(inlineFillUiInfo, (android.util.SparseArray<android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion>>) new android.util.SparseArray(), maxInputLengthForAutofill);
    }

    public static com.android.server.autofill.ui.InlineFillUi forAugmentedAutofill(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, java.util.List<android.service.autofill.Dataset> datasets, com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback) {
        android.util.SparseArray<android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion>> inlineSuggestions = com.android.server.autofill.ui.InlineSuggestionFactory.createInlineSuggestions(inlineFillUiInfo, "android:platform", datasets, uiCallback, false);
        return new com.android.server.autofill.ui.InlineFillUi(inlineFillUiInfo, inlineSuggestions);
    }

    private InlineFillUi(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, android.util.SparseArray<android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion>> inlineSuggestions) {
        this.mMaxInputLengthForAutofill = Integer.MAX_VALUE;
        this.mAutofillId = inlineFillUiInfo.mFocusedId;
        int size = inlineSuggestions.size();
        this.mDatasets = new java.util.ArrayList<>(size);
        this.mInlineSuggestions = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion> value = inlineSuggestions.valueAt(i);
            this.mDatasets.add((android.service.autofill.Dataset) value.first);
            this.mInlineSuggestions.add((android.view.inputmethod.InlineSuggestion) value.second);
        }
        this.mFilterText = inlineFillUiInfo.mFilterText;
    }

    private InlineFillUi(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, android.util.SparseArray<android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion>> inlineSuggestions, int maxInputLengthForAutofill) {
        this.mMaxInputLengthForAutofill = Integer.MAX_VALUE;
        this.mAutofillId = inlineFillUiInfo.mFocusedId;
        int size = inlineSuggestions.size();
        this.mDatasets = new java.util.ArrayList<>(size);
        this.mInlineSuggestions = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion> value = inlineSuggestions.valueAt(i);
            this.mDatasets.add((android.service.autofill.Dataset) value.first);
            this.mInlineSuggestions.add((android.view.inputmethod.InlineSuggestion) value.second);
        }
        this.mFilterText = inlineFillUiInfo.mFilterText;
        this.mMaxInputLengthForAutofill = maxInputLengthForAutofill;
    }

    private InlineFillUi(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, android.view.inputmethod.InlineSuggestion inlineSuggestion, int maxInputLengthForAutofill) {
        this.mMaxInputLengthForAutofill = Integer.MAX_VALUE;
        this.mAutofillId = inlineFillUiInfo.mFocusedId;
        this.mDatasets = null;
        this.mInlineSuggestions = new java.util.ArrayList<>();
        this.mInlineSuggestions.add(inlineSuggestion);
        this.mFilterText = inlineFillUiInfo.mFilterText;
        this.mMaxInputLengthForAutofill = maxInputLengthForAutofill;
    }

    private InlineFillUi(android.view.autofill.AutofillId focusedId) {
        this.mMaxInputLengthForAutofill = Integer.MAX_VALUE;
        this.mAutofillId = focusedId;
        this.mDatasets = new java.util.ArrayList<>(0);
        this.mInlineSuggestions = new java.util.ArrayList<>(0);
        this.mFilterText = null;
    }

    public android.view.autofill.AutofillId getAutofillId() {
        return this.mAutofillId;
    }

    public void setFilterText(java.lang.String filterText) {
        this.mFilterText = filterText;
    }

    public android.view.inputmethod.InlineSuggestionsResponse getInlineSuggestionsResponse() {
        int size = this.mInlineSuggestions.size();
        if (size == 0) {
            return new android.view.inputmethod.InlineSuggestionsResponse(java.util.Collections.emptyList());
        }
        java.util.List<android.view.inputmethod.InlineSuggestion> inlineSuggestions = new java.util.ArrayList<>();
        if (this.mDatasets == null || this.mDatasets.size() != size) {
            for (int i = 0; i < size; i++) {
                inlineSuggestions.add(copy(i, this.mInlineSuggestions.get(i)));
            }
            return new android.view.inputmethod.InlineSuggestionsResponse(inlineSuggestions);
        }
        if (!android.text.TextUtils.isEmpty(this.mFilterText) && this.mFilterText.length() > this.mMaxInputLengthForAutofill) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "Not showing inline suggestion when user entered more than " + this.mMaxInputLengthForAutofill + " characters");
            }
            return new android.view.inputmethod.InlineSuggestionsResponse(inlineSuggestions);
        }
        for (int i2 = 0; i2 < size; i2++) {
            android.service.autofill.Dataset dataset = this.mDatasets.get(i2);
            int fieldIndex = dataset.getFieldIds().indexOf(this.mAutofillId);
            if (fieldIndex < 0) {
                android.util.Slog.w(TAG, "AutofillId=" + this.mAutofillId + " not found in dataset");
            } else {
                android.service.autofill.InlinePresentation inlinePresentation = dataset.getFieldInlinePresentation(fieldIndex);
                if (inlinePresentation == null) {
                    android.util.Slog.w(TAG, "InlinePresentation not found in dataset");
                } else if (inlinePresentation.isPinned() || includeDataset(dataset, fieldIndex)) {
                    inlineSuggestions.add(copy(i2, this.mInlineSuggestions.get(i2)));
                }
            }
        }
        return new android.view.inputmethod.InlineSuggestionsResponse(inlineSuggestions);
    }

    private android.view.inputmethod.InlineSuggestion copy(int index, android.view.inputmethod.InlineSuggestion inlineSuggestion) {
        com.android.server.autofill.ui.InlineContentProviderImpl contentProvider = inlineSuggestion.getContentProvider();
        if (contentProvider instanceof com.android.server.autofill.ui.InlineContentProviderImpl) {
            android.view.inputmethod.InlineSuggestion newInlineSuggestion = new android.view.inputmethod.InlineSuggestion(inlineSuggestion.getInfo(), contentProvider.copy());
            this.mInlineSuggestions.set(index, newInlineSuggestion);
            return newInlineSuggestion;
        }
        return inlineSuggestion;
    }

    private boolean includeDataset(android.service.autofill.Dataset dataset, int fieldIndex) {
        if (android.text.TextUtils.isEmpty(this.mFilterText)) {
            return true;
        }
        java.lang.String constraintLowerCase = this.mFilterText.toString().toLowerCase();
        android.service.autofill.Dataset.DatasetFieldFilter filter = dataset.getFilter(fieldIndex);
        if (filter != null) {
            java.util.regex.Pattern filterPattern = filter.pattern;
            if (filterPattern == null) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "Explicitly disabling filter for dataset id" + dataset.getId());
                }
                return false;
            }
            if (this.mFilterMatchingDisabled) {
                return false;
            }
            return filterPattern.matcher(constraintLowerCase).matches();
        }
        android.view.autofill.AutofillValue value = (android.view.autofill.AutofillValue) dataset.getFieldValues().get(fieldIndex);
        if (value == null || !value.isText()) {
            return dataset.getAuthentication() == null;
        }
        if (this.mFilterMatchingDisabled) {
            return false;
        }
        java.lang.String valueText = value.getTextValue().toString().toLowerCase();
        return valueText.toLowerCase().startsWith(constraintLowerCase);
    }

    public void disableFilterMatching() {
        this.mFilterMatchingDisabled = true;
    }
}
