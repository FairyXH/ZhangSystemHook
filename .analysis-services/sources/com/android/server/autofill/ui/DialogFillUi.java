package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
final class DialogFillUi {
    private static final java.lang.String TAG = "DialogFillUi";
    private static final int THEME_ID_DARK = 16974836;
    private static final int THEME_ID_LIGHT = 16974847;
    private final com.android.server.autofill.ui.DialogFillUi.ItemsAdapter mAdapter;
    private com.android.server.autofill.ui.DialogFillUi.AnnounceFilterResult mAnnounceFilterResult;
    private final com.android.server.autofill.ui.DialogFillUi.UiCallback mCallback;
    private final android.content.ComponentName mComponentName;
    private final android.content.Context mContext;
    private boolean mDestroyed;
    private final android.app.Dialog mDialog;
    private java.lang.String mFilterText;
    private final android.widget.ListView mListView;
    private final com.android.server.autofill.ui.OverlayControl mOverlayControl;
    private final java.lang.String mServicePackageName;
    private final int mThemeId;
    private final int mVisibleDatasetsMaxCount;

    interface UiCallback {
        void onCanceled();

        void onDatasetPicked(android.service.autofill.Dataset dataset);

        void onDismissed();

        void onResponsePicked(android.service.autofill.FillResponse fillResponse);

        void onShown();

        void startIntentSender(android.content.IntentSender intentSender);
    }

    DialogFillUi(android.content.Context context, android.service.autofill.FillResponse response, android.view.autofill.AutofillId focusedViewId, java.lang.String filterText, android.graphics.drawable.Drawable serviceIcon, java.lang.String servicePackageName, android.content.ComponentName componentName, com.android.server.autofill.ui.OverlayControl overlayControl, boolean nightMode, com.android.server.autofill.ui.DialogFillUi.UiCallback callback) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "nightMode: " + nightMode);
        }
        this.mThemeId = nightMode ? 16974836 : 16974847;
        this.mCallback = callback;
        this.mOverlayControl = overlayControl;
        this.mServicePackageName = servicePackageName;
        this.mComponentName = componentName;
        this.mContext = new android.view.ContextThemeWrapper(context, this.mThemeId);
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this.mContext);
        android.view.View decor = inflater.inflate(android.R.layout.app_perms_summary, (android.view.ViewGroup) null);
        if (response.getShowFillDialogIcon()) {
            setServiceIcon(decor, serviceIcon);
        }
        setHeader(decor, response);
        this.mVisibleDatasetsMaxCount = getVisibleDatasetsMaxCount();
        if (response.getAuthentication() == null) {
            java.util.List<com.android.server.autofill.ui.DialogFillUi.ViewItem> items = createDatasetItems(response, focusedViewId);
            this.mAdapter = new com.android.server.autofill.ui.DialogFillUi.ItemsAdapter(items);
            this.mListView = (android.widget.ListView) decor.findViewById(android.R.id.alternate_expand_target);
            initialDatasetLayout(decor, filterText);
        } else {
            this.mListView = null;
            this.mAdapter = null;
            try {
                initialAuthenticationLayout(decor, response);
            } catch (java.lang.RuntimeException e) {
                callback.onCanceled();
                android.util.Slog.e(TAG, "Error inflating remote views", e);
                this.mDialog = null;
                return;
            }
        }
        setDismissButton(decor);
        this.mDialog = new android.app.Dialog(this.mContext, this.mThemeId);
        this.mDialog.setContentView(decor);
        setDialogParamsAsBottomSheet();
        this.mDialog.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda6
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(android.content.DialogInterface dialogInterface) {
                this.f$0.lambda$new$0(dialogInterface);
            }
        });
        this.mDialog.setOnShowListener(new android.content.DialogInterface.OnShowListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda7
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(android.content.DialogInterface dialogInterface) {
                this.f$0.lambda$new$1(dialogInterface);
            }
        });
        show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.content.DialogInterface d) {
        this.mCallback.onCanceled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(android.content.DialogInterface d) {
        this.mCallback.onShown();
    }

    private int getVisibleDatasetsMaxCount() {
        if (com.android.server.autofill.AutofillManagerService.getVisibleDatasetsMaxCount() > 0) {
            int maxCount = com.android.server.autofill.AutofillManagerService.getVisibleDatasetsMaxCount();
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "overriding maximum visible datasets to " + maxCount);
            }
            return maxCount;
        }
        return this.mContext.getResources().getInteger(android.R.integer.auto_data_switch_validation_max_retry);
    }

    private void setDialogParamsAsBottomSheet() {
        android.view.Window window = this.mDialog.getWindow();
        window.setType(2038);
        window.addFlags(131074);
        window.setDimAmount(0.6f);
        window.addPrivateFlags(16);
        window.setSoftInputMode(32);
        window.setGravity(81);
        window.setCloseOnTouchOutside(true);
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        android.util.DisplayMetrics displayMetrics = new android.util.DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int maxWidth = this.mContext.getResources().getDimensionPixelSize(android.R.dimen.alertDialog_material_text_size_body_1);
        params.width = java.lang.Math.min(screenWidth, maxWidth);
        params.accessibilityTitle = this.mContext.getString(android.R.string.autofill_continue_yes);
        params.windowAnimations = android.R.style.AutofillHalfScreenAnimation;
    }

    private void setServiceIcon(android.view.View decor, android.graphics.drawable.Drawable serviceIcon) {
        if (serviceIcon == null) {
            return;
        }
        android.widget.ImageView iconView = (android.widget.ImageView) decor.findViewById(android.R.id.anyRtl);
        int actualWidth = serviceIcon.getMinimumWidth();
        int actualHeight = serviceIcon.getMinimumHeight();
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "Adding service icon (" + actualWidth + "x" + actualHeight + ")");
        }
        iconView.setImageDrawable(serviceIcon);
        iconView.setVisibility(0);
    }

    private void setHeader(android.view.View decor, android.service.autofill.FillResponse response) {
        android.widget.RemoteViews presentation = com.android.server.autofill.Helper.sanitizeRemoteView(response.getDialogHeader());
        if (presentation == null) {
            return;
        }
        android.view.ViewGroup container = (android.view.ViewGroup) decor.findViewById(android.R.id.allowed);
        android.widget.RemoteViews.InteractionHandler interceptionHandler = new android.widget.RemoteViews.InteractionHandler() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda8
            public final boolean onInteraction(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse remoteResponse) {
                return this.f$0.lambda$setHeader$2(view, pendingIntent, remoteResponse);
            }
        };
        android.view.View content = presentation.applyWithTheme(this.mContext, (android.view.ViewGroup) decor, interceptionHandler, this.mThemeId);
        container.addView(content);
        container.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setHeader$2(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse r) {
        if (pendingIntent != null) {
            this.mCallback.startIntentSender(pendingIntent.getIntentSender());
            return true;
        }
        return true;
    }

    private void setDismissButton(android.view.View decor) {
        android.widget.TextView noButton = (android.widget.TextView) decor.findViewById(android.R.id.alternative);
        noButton.setText(android.R.string.autofill_save_notnow);
        noButton.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(android.view.View view) {
                this.f$0.lambda$setDismissButton$3(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDismissButton$3(android.view.View v) {
        this.mCallback.onDismissed();
    }

    private void setContinueButton(android.view.View decor, android.view.View.OnClickListener listener) {
        android.widget.TextView yesButton = (android.widget.TextView) decor.findViewById(android.R.id.alwaysScroll);
        yesButton.setText(android.R.string.app_suspended_more_details);
        yesButton.setOnClickListener(listener);
        yesButton.setVisibility(0);
    }

    private void initialAuthenticationLayout(android.view.View decor, final android.service.autofill.FillResponse response) {
        android.widget.RemoteViews presentation = com.android.server.autofill.Helper.sanitizeRemoteView(response.getDialogPresentation());
        if (presentation == null) {
            presentation = com.android.server.autofill.Helper.sanitizeRemoteView(response.getPresentation());
        }
        if (presentation == null) {
            throw new java.lang.RuntimeException("No presentation for fill dialog authentication");
        }
        android.view.ViewGroup container = (android.view.ViewGroup) decor.findViewById(android.R.id.allow_button);
        android.widget.RemoteViews.InteractionHandler interceptionHandler = new android.widget.RemoteViews.InteractionHandler() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda3
            public final boolean onInteraction(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse remoteResponse) {
                return this.f$0.lambda$initialAuthenticationLayout$4(view, pendingIntent, remoteResponse);
            }
        };
        android.view.View content = presentation.applyWithTheme(this.mContext, (android.view.ViewGroup) decor, interceptionHandler, this.mThemeId);
        container.addView(content);
        container.setVisibility(0);
        container.setFocusable(true);
        container.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(android.view.View view) {
                this.f$0.lambda$initialAuthenticationLayout$5(response, view);
            }
        });
        setContinueButton(decor, new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(android.view.View view) {
                this.f$0.lambda$initialAuthenticationLayout$6(response, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$initialAuthenticationLayout$4(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse r) {
        if (pendingIntent != null) {
            this.mCallback.startIntentSender(pendingIntent.getIntentSender());
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialAuthenticationLayout$5(android.service.autofill.FillResponse response, android.view.View v) {
        this.mCallback.onResponsePicked(response);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialAuthenticationLayout$6(android.service.autofill.FillResponse response, android.view.View v) {
        this.mCallback.onResponsePicked(response);
    }

    private java.util.ArrayList<com.android.server.autofill.ui.DialogFillUi.ViewItem> createDatasetItems(android.service.autofill.FillResponse response, android.view.autofill.AutofillId focusedViewId) {
        java.util.regex.Pattern filterPattern;
        java.lang.String valueText;
        boolean filterable;
        int datasetCount = response.getDatasets().size();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Number datasets: " + datasetCount + " max visible: " + this.mVisibleDatasetsMaxCount);
        }
        android.widget.RemoteViews.InteractionHandler interceptionHandler = new android.widget.RemoteViews.InteractionHandler() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda10
            public final boolean onInteraction(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse remoteResponse) {
                return this.f$0.lambda$createDatasetItems$7(view, pendingIntent, remoteResponse);
            }
        };
        java.util.ArrayList<com.android.server.autofill.ui.DialogFillUi.ViewItem> items = new java.util.ArrayList<>(datasetCount);
        for (int i = 0; i < datasetCount; i++) {
            android.service.autofill.Dataset dataset = (android.service.autofill.Dataset) response.getDatasets().get(i);
            int index = dataset.getFieldIds().indexOf(focusedViewId);
            if (index >= 0) {
                android.widget.RemoteViews presentation = com.android.server.autofill.Helper.sanitizeRemoteView(dataset.getFieldDialogPresentation(index));
                if (presentation == null) {
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.w(TAG, "not displaying UI on field " + focusedViewId + " because service didn't provide a presentation for it on " + dataset);
                    }
                } else {
                    try {
                        if (com.android.server.autofill.Helper.sVerbose) {
                            try {
                                android.util.Slog.v(TAG, "setting remote view for " + focusedViewId);
                            } catch (java.lang.RuntimeException e) {
                                e = e;
                                android.util.Slog.e(TAG, "Error inflating remote views", e);
                            }
                        }
                        android.view.View view = presentation.applyWithTheme(this.mContext, null, interceptionHandler, this.mThemeId);
                        android.service.autofill.Dataset.DatasetFieldFilter filter = dataset.getFilter(index);
                        java.lang.String valueText2 = null;
                        if (filter == null) {
                            android.view.autofill.AutofillValue value = (android.view.autofill.AutofillValue) dataset.getFieldValues().get(index);
                            if (value != null && value.isText()) {
                                valueText2 = value.getTextValue().toString().toLowerCase();
                            }
                            filterPattern = null;
                            valueText = valueText2;
                            filterable = true;
                        } else {
                            java.util.regex.Pattern filterPattern2 = filter.pattern;
                            if (filterPattern2 != null) {
                                filterPattern = filterPattern2;
                                valueText = null;
                                filterable = true;
                            } else {
                                if (com.android.server.autofill.Helper.sVerbose) {
                                    android.util.Slog.v(TAG, "Explicitly disabling filter at id " + focusedViewId + " for dataset #" + index);
                                }
                                filterPattern = filterPattern2;
                                valueText = null;
                                filterable = false;
                            }
                        }
                        items.add(new com.android.server.autofill.ui.DialogFillUi.ViewItem(dataset, filterPattern, filterable, valueText, view));
                    } catch (java.lang.RuntimeException e2) {
                        e = e2;
                    }
                }
            }
        }
        return items;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createDatasetItems$7(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse r) {
        if (pendingIntent != null) {
            this.mCallback.startIntentSender(pendingIntent.getIntentSender());
            return true;
        }
        return true;
    }

    private void initialDatasetLayout(android.view.View decor, java.lang.String filterText) {
        final android.widget.AdapterView.OnItemClickListener onItemClickListener = new android.widget.AdapterView.OnItemClickListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda0
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(android.widget.AdapterView adapterView, android.view.View view, int i, long j) {
                this.f$0.lambda$initialDatasetLayout$8(adapterView, view, i, j);
            }
        };
        this.mListView.setAdapter((android.widget.ListAdapter) this.mAdapter);
        this.mListView.setVisibility(0);
        this.mListView.setOnItemClickListener(onItemClickListener);
        if (this.mAdapter.getCount() == 1) {
            setContinueButton(decor, new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(android.view.View view) {
                    onItemClickListener.onItemClick(null, null, 0, 0L);
                }
            });
        }
        if (filterText == null) {
            this.mFilterText = null;
        } else {
            this.mFilterText = filterText.toLowerCase();
        }
        final int oldCount = this.mAdapter.getCount();
        this.mAdapter.getFilter().filter(this.mFilterText, new android.widget.Filter.FilterListener() { // from class: com.android.server.autofill.ui.DialogFillUi$$ExternalSyntheticLambda2
            @Override // android.widget.Filter.FilterListener
            public final void onFilterComplete(int i) {
                this.f$0.lambda$initialDatasetLayout$10(oldCount, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialDatasetLayout$8(android.widget.AdapterView adapter, android.view.View view, int position, long id) {
        com.android.server.autofill.ui.DialogFillUi.ViewItem vi = this.mAdapter.getItem(position);
        this.mCallback.onDatasetPicked(vi.dataset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialDatasetLayout$10(int oldCount, int count) {
        if (this.mDestroyed) {
            return;
        }
        if (count <= 0) {
            if (com.android.server.autofill.Helper.sDebug) {
                int size = this.mFilterText != null ? this.mFilterText.length() : 0;
                android.util.Slog.d(TAG, "No dataset matches filter with " + size + " chars");
            }
            this.mCallback.onCanceled();
            return;
        }
        if (this.mAdapter.getCount() <= this.mVisibleDatasetsMaxCount) {
            this.mListView.setVerticalScrollBarEnabled(false);
        } else {
            this.mListView.setVerticalScrollBarEnabled(true);
            this.mListView.onVisibilityAggregated(true);
        }
        if (this.mAdapter.getCount() != oldCount) {
            this.mListView.requestLayout();
        }
    }

    private void show() {
        android.util.Slog.i(TAG, "Showing fill dialog");
        this.mDialog.show();
        this.mOverlayControl.hideOverlays();
    }

    boolean isShowing() {
        return this.mDialog.isShowing();
    }

    void hide() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Hiding fill dialog.");
        }
        try {
            this.mDialog.hide();
        } finally {
            this.mOverlayControl.showOverlays();
        }
    }

    void destroy() {
        try {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "destroy()");
            }
            throwIfDestroyed();
            this.mDialog.dismiss();
            this.mDestroyed = true;
        } finally {
            this.mOverlayControl.showOverlays();
        }
    }

    private void throwIfDestroyed() {
        if (this.mDestroyed) {
            throw new java.lang.IllegalStateException("cannot interact with a destroyed instance");
        }
    }

    public java.lang.String toString() {
        return "NO TITLE";
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("service: ");
        pw.println(this.mServicePackageName);
        pw.print(prefix);
        pw.print("app: ");
        pw.println(this.mComponentName.toShortString());
        pw.print(prefix);
        pw.print("theme id: ");
        pw.print(this.mThemeId);
        switch (this.mThemeId) {
            case 16974836:
                pw.println(" (dark)");
                break;
            case 16974847:
                pw.println(" (light)");
                break;
            default:
                pw.println("(UNKNOWN_MODE)");
                break;
        }
        android.view.View view = this.mDialog.getWindow().getDecorView();
        int[] loc = view.getLocationOnScreen();
        pw.print(prefix);
        pw.print("coordinates: ");
        pw.print('(');
        pw.print(loc[0]);
        pw.print(',');
        pw.print(loc[1]);
        pw.print(')');
        pw.print('(');
        pw.print(loc[0] + view.getWidth());
        pw.print(',');
        pw.print(loc[1] + view.getHeight());
        pw.println(')');
        pw.print(prefix);
        pw.print("destroyed: ");
        pw.println(this.mDestroyed);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void announceSearchResultIfNeeded() {
        if (android.view.accessibility.AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            if (this.mAnnounceFilterResult == null) {
                this.mAnnounceFilterResult = new com.android.server.autofill.ui.DialogFillUi.AnnounceFilterResult();
            }
            this.mAnnounceFilterResult.post();
        }
    }

    private final class AnnounceFilterResult implements java.lang.Runnable {
        private static final int SEARCH_RESULT_ANNOUNCEMENT_DELAY = 1000;

        private AnnounceFilterResult() {
        }

        public void post() {
            remove();
            com.android.server.autofill.ui.DialogFillUi.this.mListView.postDelayed(this, 1000L);
        }

        public void remove() {
            com.android.server.autofill.ui.DialogFillUi.this.mListView.removeCallbacks(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            java.lang.String text;
            int count = com.android.server.autofill.ui.DialogFillUi.this.mListView.getAdapter().getCount();
            if (count <= 0) {
                text = com.android.server.autofill.ui.DialogFillUi.this.mContext.getString(android.R.string.autofill_error_cannot_autofill);
            } else {
                java.util.Map<java.lang.String, java.lang.Object> arguments = new java.util.HashMap<>();
                arguments.put(com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, java.lang.Integer.valueOf(count));
                text = android.util.PluralsMessageFormatter.format(com.android.server.autofill.ui.DialogFillUi.this.mContext.getResources(), arguments, android.R.string.autofill_picker_accessibility_title);
            }
            com.android.server.autofill.ui.DialogFillUi.this.mListView.announceForAccessibility(text);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ItemsAdapter extends android.widget.BaseAdapter implements android.widget.Filterable {
        private final java.util.List<com.android.server.autofill.ui.DialogFillUi.ViewItem> mAllItems;
        private final java.util.List<com.android.server.autofill.ui.DialogFillUi.ViewItem> mFilteredItems = new java.util.ArrayList();

        ItemsAdapter(java.util.List<com.android.server.autofill.ui.DialogFillUi.ViewItem> items) {
            this.mAllItems = java.util.Collections.unmodifiableList(new java.util.ArrayList(items));
            this.mFilteredItems.addAll(items);
        }

        /* JADX INFO: renamed from: com.android.server.autofill.ui.DialogFillUi$ItemsAdapter$1, reason: invalid class name */
        class AnonymousClass1 extends android.widget.Filter {
            AnonymousClass1() {
            }

            @Override // android.widget.Filter
            protected android.widget.Filter.FilterResults performFiltering(final java.lang.CharSequence filterText) {
                java.util.List<com.android.server.autofill.ui.DialogFillUi.ViewItem> filtered = (java.util.List) com.android.server.autofill.ui.DialogFillUi.ItemsAdapter.this.mAllItems.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.autofill.ui.DialogFillUi$ItemsAdapter$1$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return ((com.android.server.autofill.ui.DialogFillUi.ViewItem) obj).matches(filterText);
                    }
                }).collect(java.util.stream.Collectors.toList());
                android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();
                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override // android.widget.Filter
            protected void publishResults(java.lang.CharSequence constraint, android.widget.Filter.FilterResults results) {
                int oldItemCount = com.android.server.autofill.ui.DialogFillUi.ItemsAdapter.this.mFilteredItems.size();
                com.android.server.autofill.ui.DialogFillUi.ItemsAdapter.this.mFilteredItems.clear();
                if (results.count > 0) {
                    java.util.List<com.android.server.autofill.ui.DialogFillUi.ViewItem> items = (java.util.List) results.values;
                    com.android.server.autofill.ui.DialogFillUi.ItemsAdapter.this.mFilteredItems.addAll(items);
                }
                boolean resultCountChanged = oldItemCount != com.android.server.autofill.ui.DialogFillUi.ItemsAdapter.this.mFilteredItems.size();
                if (resultCountChanged) {
                    com.android.server.autofill.ui.DialogFillUi.this.announceSearchResultIfNeeded();
                }
                com.android.server.autofill.ui.DialogFillUi.ItemsAdapter.this.notifyDataSetChanged();
            }
        }

        @Override // android.widget.Filterable
        public android.widget.Filter getFilter() {
            return new com.android.server.autofill.ui.DialogFillUi.ItemsAdapter.AnonymousClass1();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mFilteredItems.size();
        }

        @Override // android.widget.Adapter
        public com.android.server.autofill.ui.DialogFillUi.ViewItem getItem(int position) {
            return this.mFilteredItems.get(position);
        }

        @Override // android.widget.Adapter
        public long getItemId(int position) {
            return position;
        }

        @Override // android.widget.Adapter
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            return getItem(position).view;
        }

        public java.lang.String toString() {
            return "ItemsAdapter: [all=" + this.mAllItems + ", filtered=" + this.mFilteredItems + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ViewItem {
        public final android.service.autofill.Dataset dataset;
        public final java.util.regex.Pattern filter;
        public final boolean filterable;
        public final java.lang.String value;
        public final android.view.View view;

        ViewItem(android.service.autofill.Dataset dataset, java.util.regex.Pattern filter, boolean filterable, java.lang.String value, android.view.View view) {
            this.dataset = dataset;
            this.value = value;
            this.view = view;
            this.filter = filter;
            this.filterable = filterable;
        }

        public boolean matches(java.lang.CharSequence filterText) {
            if (android.text.TextUtils.isEmpty(filterText)) {
                return true;
            }
            if (!this.filterable) {
                return false;
            }
            java.lang.String constraintLowerCase = filterText.toString().toLowerCase();
            if (this.filter != null) {
                return this.filter.matcher(constraintLowerCase).matches();
            }
            if (this.value == null) {
                return this.dataset.getAuthentication() == null;
            }
            return this.value.toLowerCase().startsWith(constraintLowerCase);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder("ViewItem:[view=").append(this.view.getAutofillId());
            java.lang.String datasetId = this.dataset == null ? null : this.dataset.getId();
            if (datasetId != null) {
                builder.append(", dataset=").append(datasetId);
            }
            if (this.value != null) {
                builder.append(", value=").append(this.value.length()).append("_chars");
            }
            if (this.filterable) {
                builder.append(", filterable");
            }
            if (this.filter != null) {
                builder.append(", filter=").append(this.filter.pattern().length()).append("_chars");
            }
            return builder.append(']').toString();
        }
    }
}
