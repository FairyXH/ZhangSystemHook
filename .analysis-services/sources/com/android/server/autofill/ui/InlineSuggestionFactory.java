package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
final class InlineSuggestionFactory {
    private static final java.lang.String TAG = "InlineSuggestionFactory";

    public static android.view.inputmethod.InlineSuggestion createInlineAuthentication(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, android.service.autofill.FillResponse response, final com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback) {
        android.service.autofill.InlinePresentation inlineAuthentication = response.getInlinePresentation();
        final int requestId = response.getRequestId();
        boolean ignoreHostSpec = android.service.autofill.Flags.autofillCredmanIntegration() && (response.getFlags() & 8) != 0;
        return createInlineSuggestion(inlineFillUiInfo, "android:autofill", "android:autofill:action", new java.lang.Runnable() { // from class: com.android.server.autofill.ui.InlineSuggestionFactory$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                uiCallback.authenticate(requestId, 65535);
            }
        }, mergedInlinePresentation(inlineFillUiInfo.mInlineRequest, 0, inlineAuthentication, ignoreHostSpec), createInlineSuggestionTooltip(inlineFillUiInfo.mInlineRequest, inlineFillUiInfo, "android:autofill", response.getInlineTooltipPresentation()), uiCallback);
    }

    public static android.util.SparseArray<android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion>> createInlineSuggestions(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, java.lang.String suggestionSource, java.util.List<android.service.autofill.Dataset> datasets, final com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback, boolean ignoreHostSpec) {
        boolean hasTooltip;
        android.view.inputmethod.InlineSuggestion inlineSuggestionTooltip;
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "createInlineSuggestions(source=" + suggestionSource + ") called");
        }
        android.view.inputmethod.InlineSuggestionsRequest request = inlineFillUiInfo.mInlineRequest;
        android.util.SparseArray<android.util.Pair<android.service.autofill.Dataset, android.view.inputmethod.InlineSuggestion>> response = new android.util.SparseArray<>(datasets.size());
        boolean hasTooltip2 = false;
        for (int datasetIndex = 0; datasetIndex < datasets.size(); datasetIndex++) {
            final android.service.autofill.Dataset dataset = datasets.get(datasetIndex);
            int fieldIndex = dataset.getFieldIds().indexOf(inlineFillUiInfo.mFocusedId);
            if (fieldIndex < 0) {
                android.util.Slog.w(TAG, "AutofillId=" + inlineFillUiInfo.mFocusedId + " not found in dataset");
            } else {
                android.service.autofill.InlinePresentation inlinePresentation = dataset.getFieldInlinePresentation(fieldIndex);
                if (inlinePresentation == null) {
                    android.util.Slog.w(TAG, "InlinePresentation not found in dataset");
                } else {
                    java.lang.String suggestionType = dataset.getAuthentication() == null ? "android:autofill:suggestion" : "android:autofill:action";
                    final int index = datasetIndex;
                    if (hasTooltip2) {
                        hasTooltip = hasTooltip2;
                        inlineSuggestionTooltip = null;
                    } else {
                        android.view.inputmethod.InlineSuggestion inlineSuggestionTooltip2 = createInlineSuggestionTooltip(request, inlineFillUiInfo, suggestionSource, dataset.getFieldInlineTooltipPresentation(fieldIndex));
                        if (inlineSuggestionTooltip2 == null) {
                            hasTooltip = hasTooltip2;
                            inlineSuggestionTooltip = inlineSuggestionTooltip2;
                        } else {
                            hasTooltip = true;
                            inlineSuggestionTooltip = inlineSuggestionTooltip2;
                        }
                    }
                    android.view.inputmethod.InlineSuggestion inlineSuggestion = createInlineSuggestion(inlineFillUiInfo, suggestionSource, suggestionType, new java.lang.Runnable() { // from class: com.android.server.autofill.ui.InlineSuggestionFactory$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            uiCallback.autofill(dataset, index);
                        }
                    }, mergedInlinePresentation(request, datasetIndex, inlinePresentation, ignoreHostSpec), inlineSuggestionTooltip, uiCallback);
                    response.append(datasetIndex, android.util.Pair.create(dataset, inlineSuggestion));
                    hasTooltip2 = hasTooltip;
                }
            }
        }
        return response;
    }

    private static android.view.inputmethod.InlineSuggestion createInlineSuggestion(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, java.lang.String suggestionSource, java.lang.String suggestionType, java.lang.Runnable onClickAction, android.service.autofill.InlinePresentation inlinePresentation, android.view.inputmethod.InlineSuggestion tooltip, com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback) {
        android.view.inputmethod.InlineSuggestionInfo inlineSuggestionInfo = new android.view.inputmethod.InlineSuggestionInfo(inlinePresentation.getInlinePresentationSpec(), suggestionSource, inlinePresentation.getAutofillHints(), suggestionType, inlinePresentation.isPinned(), tooltip);
        return new android.view.inputmethod.InlineSuggestion(inlineSuggestionInfo, createInlineContentProvider(inlineFillUiInfo, inlinePresentation, onClickAction, uiCallback));
    }

    private static android.service.autofill.InlinePresentation mergedInlinePresentation(android.view.inputmethod.InlineSuggestionsRequest request, int index, android.service.autofill.InlinePresentation inlinePresentation, boolean ignoreHostSpec) {
        java.util.List<android.widget.inline.InlinePresentationSpec> specs = request.getInlinePresentationSpecs();
        if (specs.isEmpty()) {
            return inlinePresentation;
        }
        android.widget.inline.InlinePresentationSpec specFromHost = specs.get(java.lang.Math.min(specs.size() - 1, index));
        android.widget.inline.InlinePresentationSpec specToUse = ignoreHostSpec ? inlinePresentation.getInlinePresentationSpec() : specFromHost;
        android.widget.inline.InlinePresentationSpec mergedInlinePresentation = new android.widget.inline.InlinePresentationSpec.Builder(inlinePresentation.getInlinePresentationSpec().getMinSize(), inlinePresentation.getInlinePresentationSpec().getMaxSize()).setStyle(specToUse.getStyle()).build();
        return new android.service.autofill.InlinePresentation(inlinePresentation.getSlice(), mergedInlinePresentation, inlinePresentation.isPinned());
    }

    private static android.view.inputmethod.InlineSuggestion createInlineSuggestionTooltip(android.view.inputmethod.InlineSuggestionsRequest request, com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, java.lang.String suggestionSource, android.service.autofill.InlinePresentation tooltipPresentation) {
        android.widget.inline.InlinePresentationSpec mergedSpec;
        if (tooltipPresentation == null) {
            return null;
        }
        android.widget.inline.InlinePresentationSpec spec = request.getInlineTooltipPresentationSpec();
        if (spec == null) {
            mergedSpec = tooltipPresentation.getInlinePresentationSpec();
        } else {
            mergedSpec = new android.widget.inline.InlinePresentationSpec.Builder(tooltipPresentation.getInlinePresentationSpec().getMinSize(), tooltipPresentation.getInlinePresentationSpec().getMaxSize()).setStyle(spec.getStyle()).build();
        }
        com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback = new com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback() { // from class: com.android.server.autofill.ui.InlineSuggestionFactory.1
            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void autofill(android.service.autofill.Dataset dataset, int datasetIndex) {
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void authenticate(int requestId, int datasetIndex) {
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void startIntentSender(android.content.IntentSender intentSender) {
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onError() {
                android.util.Slog.w(com.android.server.autofill.ui.InlineSuggestionFactory.TAG, "An error happened on the tooltip");
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onInflate() {
            }
        };
        android.service.autofill.InlinePresentation tooltipInline = new android.service.autofill.InlinePresentation(tooltipPresentation.getSlice(), mergedSpec, false);
        com.android.internal.view.inline.IInlineContentProvider tooltipContentProvider = createInlineContentProvider(inlineFillUiInfo, tooltipInline, new java.lang.Runnable() { // from class: com.android.server.autofill.ui.InlineSuggestionFactory$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.autofill.ui.InlineSuggestionFactory.lambda$createInlineSuggestionTooltip$2();
            }
        }, uiCallback);
        android.view.inputmethod.InlineSuggestionInfo tooltipInlineSuggestionInfo = new android.view.inputmethod.InlineSuggestionInfo(mergedSpec, suggestionSource, null, "android:autofill:suggestion", false, null);
        return new android.view.inputmethod.InlineSuggestion(tooltipInlineSuggestionInfo, tooltipContentProvider);
    }

    static /* synthetic */ void lambda$createInlineSuggestionTooltip$2() {
    }

    private static com.android.internal.view.inline.IInlineContentProvider createInlineContentProvider(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, android.service.autofill.InlinePresentation inlinePresentation, java.lang.Runnable onClickAction, com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback) {
        com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector remoteInlineSuggestionViewConnector = new com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector(inlineFillUiInfo, inlinePresentation, onClickAction, uiCallback);
        return new com.android.server.autofill.ui.InlineContentProviderImpl(remoteInlineSuggestionViewConnector, null);
    }

    private InlineSuggestionFactory() {
    }
}
