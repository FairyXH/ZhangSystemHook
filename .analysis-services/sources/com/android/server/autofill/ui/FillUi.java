package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
final class FillUi {
    private static final int AUTOFILL_CREDMAN_MAX_VISIBLE_DATASETS = 5;
    private static final java.lang.String TAG = "FillUi";
    private static final int THEME_ID_DARK = 16974834;
    private static final int THEME_ID_LIGHT = 16974846;
    private static final android.util.TypedValue sTempTypedValue = new android.util.TypedValue();
    private final com.android.server.autofill.ui.FillUi.ItemsAdapter mAdapter;
    private com.android.server.autofill.ui.FillUi.AnnounceFilterResult mAnnounceFilterResult;
    private final com.android.server.autofill.ui.FillUi.Callback mCallback;
    private int mContentHeight;
    private int mContentWidth;
    private final android.content.Context mContext;
    private boolean mDestroyed;
    private java.lang.String mFilterText;
    private final android.view.View mFooter;
    private final boolean mFullScreen;
    private final android.view.View mHeader;
    private final android.widget.ListView mListView;
    private int mMaxInputLengthForAutofill;
    private final int mThemeId;
    private final int mVisibleDatasetsMaxCount;
    private final com.android.server.autofill.ui.FillUi.AnchoredWindow mWindow;
    private final android.graphics.Point mTempPoint = new android.graphics.Point();
    private final com.android.server.autofill.ui.FillUi.AutofillWindowPresenter mWindowPresenter = new com.android.server.autofill.ui.FillUi.AutofillWindowPresenter();

    interface Callback {
        void cancelSession();

        void dispatchUnhandledKey(android.view.KeyEvent keyEvent);

        void onCanceled();

        void onDatasetPicked(android.service.autofill.Dataset dataset);

        void onDestroy();

        void onResponsePicked(android.service.autofill.FillResponse fillResponse);

        void onShown(int i);

        void requestHideFillUi();

        void requestHideFillUiWhenDestroyed();

        void requestShowFillUi(int i, int i2, android.view.autofill.IAutofillWindowPresenter iAutofillWindowPresenter);

        void startIntentSender(android.content.IntentSender intentSender);
    }

    public static boolean isFullScreen(android.content.Context context) {
        if (com.android.server.autofill.Helper.sFullScreenMode != null) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "forcing full-screen mode to " + com.android.server.autofill.Helper.sFullScreenMode);
            }
            return com.android.server.autofill.Helper.sFullScreenMode.booleanValue();
        }
        return context.getPackageManager().hasSystemFeature("android.software.leanback");
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x037e  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x03a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    FillUi(android.content.Context r33, final android.service.autofill.FillResponse r34, android.view.autofill.AutofillId r35, java.lang.String r36, com.android.server.autofill.ui.OverlayControl r37, java.lang.CharSequence r38, android.graphics.drawable.Drawable r39, boolean r40, int r41, com.android.server.autofill.ui.FillUi.Callback r42) {
        /*
            Method dump skipped, instruction units count: 1136
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.autofill.ui.FillUi.<init>(android.content.Context, android.service.autofill.FillResponse, android.view.autofill.AutofillId, java.lang.String, com.android.server.autofill.ui.OverlayControl, java.lang.CharSequence, android.graphics.drawable.Drawable, boolean, int, com.android.server.autofill.ui.FillUi$Callback):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(android.view.View view, android.view.KeyEvent event) {
        switch (event.getKeyCode()) {
            case 4:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 66:
            case 111:
                return false;
            default:
                this.mCallback.dispatchUnhandledKey(event);
                return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse r) {
        if (pendingIntent != null) {
            this.mCallback.startIntentSender(pendingIntent.getIntentSender());
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(android.service.autofill.FillResponse response, android.view.View v) {
        this.mCallback.onResponsePicked(response);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(android.widget.AdapterView adapter, android.view.View view, int position, long id) {
        com.android.server.autofill.ui.FillUi.ViewItem vi = this.mAdapter.getItem(position);
        this.mCallback.onDatasetPicked(vi.dataset);
    }

    private void applyCancelAction(android.view.View rootView, int[] ids) {
        if (ids == null) {
            return;
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "fill UI has " + ids.length + " actions");
        }
        if (!(rootView instanceof android.view.ViewGroup)) {
            android.util.Slog.w(TAG, "cannot apply actions because fill UI root is not a ViewGroup: " + rootView);
            return;
        }
        android.view.ViewGroup root = (android.view.ViewGroup) rootView;
        for (int id : ids) {
            android.view.View child = root.findViewById(id);
            if (child == null) {
                android.util.Slog.w(TAG, "Ignoring cancel action for view " + id + " because it's not on " + root);
            } else {
                child.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.FillUi$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(android.view.View view) {
                        this.f$0.lambda$applyCancelAction$4(view);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyCancelAction$4(android.view.View v) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, " Cancelling session after " + v + " clicked");
        }
        this.mCallback.cancelSession();
    }

    void requestShowFillUi() {
        this.mCallback.requestShowFillUi(this.mContentWidth, this.mContentHeight, this.mWindowPresenter);
    }

    private android.widget.RemoteViews.InteractionHandler newInteractionBlocker() {
        return new android.widget.RemoteViews.InteractionHandler() { // from class: com.android.server.autofill.ui.FillUi$$ExternalSyntheticLambda0
            public final boolean onInteraction(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse remoteResponse) {
                return com.android.server.autofill.ui.FillUi.lambda$newInteractionBlocker$5(view, pendingIntent, remoteResponse);
            }
        };
    }

    static /* synthetic */ boolean lambda$newInteractionBlocker$5(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse response) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Ignoring click on " + view);
            return true;
        }
        return true;
    }

    private void applyNewFilterText() {
        final int oldCount = this.mAdapter.getCount();
        this.mAdapter.getFilter().filter(this.mFilterText, new android.widget.Filter.FilterListener() { // from class: com.android.server.autofill.ui.FillUi$$ExternalSyntheticLambda2
            @Override // android.widget.Filter.FilterListener
            public final void onFilterComplete(int i) {
                this.f$0.lambda$applyNewFilterText$6(oldCount, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyNewFilterText$6(int oldCount, int count) {
        if (this.mDestroyed) {
            return;
        }
        int size = this.mFilterText == null ? 0 : this.mFilterText.length();
        if (count <= 0) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "No dataset matches filter with " + size + " chars");
            }
            this.mCallback.requestHideFillUi();
        } else {
            if (size > this.mMaxInputLengthForAutofill) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "Not showing fill UI because user entered more than " + this.mMaxInputLengthForAutofill + " characters");
                }
                this.mCallback.requestHideFillUi();
                return;
            }
            if (updateContentSize()) {
                requestShowFillUi();
            }
            if (this.mAdapter.getCount() > this.mVisibleDatasetsMaxCount) {
                this.mListView.setVerticalScrollBarEnabled(true);
                this.mListView.onVisibilityAggregated(true);
            } else {
                this.mListView.setVerticalScrollBarEnabled(false);
            }
            if (this.mAdapter.getCount() != oldCount) {
                this.mListView.requestLayout();
            }
        }
    }

    public void setFilterText(java.lang.String filterText) {
        java.lang.String filterText2;
        throwIfDestroyed();
        if (this.mAdapter == null) {
            if (android.text.TextUtils.isEmpty(filterText)) {
                requestShowFillUi();
                return;
            } else {
                this.mCallback.requestHideFillUi();
                return;
            }
        }
        if (filterText == null) {
            filterText2 = null;
        } else {
            filterText2 = filterText.toLowerCase();
        }
        if (java.util.Objects.equals(this.mFilterText, filterText2)) {
            return;
        }
        this.mFilterText = filterText2;
        applyNewFilterText();
    }

    public void destroy(boolean notifyClient) {
        throwIfDestroyed();
        if (this.mWindow != null) {
            this.mWindow.hide(false);
        }
        this.mCallback.onDestroy();
        if (notifyClient) {
            this.mCallback.requestHideFillUiWhenDestroyed();
        }
        this.mDestroyed = true;
    }

    private boolean updateContentSize() {
        if (this.mAdapter == null) {
            return false;
        }
        if (this.mFullScreen) {
            return true;
        }
        boolean changed = false;
        if (this.mAdapter.getCount() <= 0) {
            if (this.mContentWidth != 0) {
                this.mContentWidth = 0;
                changed = true;
            }
            if (this.mContentHeight != 0) {
                this.mContentHeight = 0;
                return true;
            }
            return changed;
        }
        android.graphics.Point maxSize = this.mTempPoint;
        resolveMaxWindowSize(this.mContext, maxSize);
        this.mContentWidth = 0;
        this.mContentHeight = 0;
        int widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxSize.x, Integer.MIN_VALUE);
        int heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(maxSize.y, Integer.MIN_VALUE);
        int itemCount = this.mAdapter.getCount();
        if (this.mHeader != null) {
            this.mHeader.measure(widthMeasureSpec, heightMeasureSpec);
            changed = false | updateWidth(this.mHeader, maxSize) | updateHeight(this.mHeader, maxSize);
        }
        for (int i = 0; i < itemCount; i++) {
            android.view.View view = this.mAdapter.getItem(i).view;
            view.measure(widthMeasureSpec, heightMeasureSpec);
            changed |= updateWidth(view, maxSize);
            if (i < this.mVisibleDatasetsMaxCount) {
                changed |= updateHeight(view, maxSize);
            }
        }
        if (this.mFooter != null) {
            this.mFooter.measure(widthMeasureSpec, heightMeasureSpec);
            return changed | updateWidth(this.mFooter, maxSize) | updateHeight(this.mFooter, maxSize);
        }
        return changed;
    }

    private boolean updateWidth(android.view.View view, android.graphics.Point maxSize) {
        int clampedMeasuredWidth = java.lang.Math.min(view.getMeasuredWidth(), maxSize.x);
        int newContentWidth = java.lang.Math.max(this.mContentWidth, clampedMeasuredWidth);
        if (newContentWidth == this.mContentWidth) {
            return false;
        }
        this.mContentWidth = newContentWidth;
        return true;
    }

    private boolean updateHeight(android.view.View view, android.graphics.Point maxSize) {
        int clampedMeasuredHeight = java.lang.Math.min(view.getMeasuredHeight(), maxSize.y);
        int newContentHeight = this.mContentHeight + clampedMeasuredHeight;
        if (newContentHeight == this.mContentHeight) {
            return false;
        }
        this.mContentHeight = newContentHeight;
        return true;
    }

    private void throwIfDestroyed() {
        if (this.mDestroyed) {
            throw new java.lang.IllegalStateException("cannot interact with a destroyed instance");
        }
    }

    private static void resolveMaxWindowSize(android.content.Context context, android.graphics.Point outPoint) {
        context.getDisplayNoVerify().getSize(outPoint);
        android.util.TypedValue typedValue = sTempTypedValue;
        context.getTheme().resolveAttribute(android.R.^attr-private.autofillDatasetPickerMaxWidth, typedValue, true);
        outPoint.x = (int) typedValue.getFraction(outPoint.x, outPoint.x);
        context.getTheme().resolveAttribute(android.R.^attr-private.autofillDatasetPickerMaxHeight, typedValue, true);
        outPoint.y = (int) typedValue.getFraction(outPoint.y, outPoint.y);
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

    /* JADX INFO: Access modifiers changed from: private */
    final class AutofillWindowPresenter extends android.view.autofill.IAutofillWindowPresenter.Stub {
        private AutofillWindowPresenter() {
        }

        public void show(final android.view.WindowManager.LayoutParams p, android.graphics.Rect transitionEpicenter, boolean fitsSystemWindows, int layoutDirection) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.ui.FillUi.TAG, "AutofillWindowPresenter.show(): fit=" + fitsSystemWindows + ", params=" + com.android.server.autofill.Helper.paramsToString(p));
            }
            com.android.server.UiThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.FillUi$AutofillWindowPresenter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$show$0(p);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$show$0(android.view.WindowManager.LayoutParams p) {
            if (com.android.server.autofill.ui.FillUi.this.mWindow != null) {
                com.android.server.autofill.ui.FillUi.this.mWindow.show(p);
            }
        }

        public void hide(android.graphics.Rect transitionEpicenter) {
            com.android.server.UiThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.FillUi$AutofillWindowPresenter$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$hide$1();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$hide$1() {
            if (com.android.server.autofill.ui.FillUi.this.mWindow != null) {
                com.android.server.autofill.ui.FillUi.this.mWindow.hide();
            }
        }
    }

    final class AnchoredWindow {
        private final android.view.View mContentView;
        private final com.android.server.autofill.ui.OverlayControl mOverlayControl;
        private android.view.WindowManager.LayoutParams mShowParams;
        private boolean mShowing;
        private final android.view.WindowManager mWm;

        AnchoredWindow(android.view.View contentView, com.android.server.autofill.ui.OverlayControl overlayControl) {
            this.mWm = (android.view.WindowManager) contentView.getContext().getSystemService(android.view.WindowManager.class);
            this.mContentView = contentView;
            this.mOverlayControl = overlayControl;
        }

        public void show(android.view.WindowManager.LayoutParams params) {
            this.mShowParams = params;
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.ui.FillUi.TAG, "show(): showing=" + this.mShowing + ", params=" + com.android.server.autofill.Helper.paramsToString(params));
            }
            try {
                params.packageName = com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME;
                params.setTitle("Autofill UI");
                if (!this.mShowing) {
                    params.accessibilityTitle = this.mContentView.getContext().getString(android.R.string.autofill_continue_yes);
                    this.mWm.addView(this.mContentView, params);
                    this.mOverlayControl.hideOverlays();
                    this.mShowing = true;
                    int numShownDatasets = com.android.server.autofill.ui.FillUi.this.mAdapter == null ? 0 : com.android.server.autofill.ui.FillUi.this.mAdapter.getCount();
                    com.android.server.autofill.ui.FillUi.this.mCallback.onShown(numShownDatasets);
                    return;
                }
                this.mWm.updateViewLayout(this.mContentView, params);
            } catch (android.view.WindowManager.BadTokenException e) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(com.android.server.autofill.ui.FillUi.TAG, "Filed with with token " + params.token + " gone.");
                }
                com.android.server.autofill.ui.FillUi.this.mCallback.onDestroy();
            } catch (android.view.WindowManager.InvalidDisplayException e2) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(com.android.server.autofill.ui.FillUi.TAG, "catch InvalidDisplayException");
                }
                com.android.server.autofill.ui.FillUi.this.mCallback.onDestroy();
            } catch (java.lang.IllegalStateException e3) {
                android.util.Slog.wtf(com.android.server.autofill.ui.FillUi.TAG, "Exception showing window " + params, e3);
                com.android.server.autofill.ui.FillUi.this.mCallback.onDestroy();
            }
        }

        void hide() {
            hide(true);
        }

        void hide(boolean destroyCallbackOnError) {
            try {
                try {
                    if (this.mShowing) {
                        this.mWm.removeView(this.mContentView);
                        this.mShowing = false;
                    }
                } catch (java.lang.IllegalStateException e) {
                    android.util.Slog.e(com.android.server.autofill.ui.FillUi.TAG, "Exception hiding window ", e);
                    if (destroyCallbackOnError) {
                        com.android.server.autofill.ui.FillUi.this.mCallback.onDestroy();
                    }
                }
            } finally {
                this.mOverlayControl.showOverlays();
            }
        }
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mCallback: ");
        pw.println(this.mCallback != null);
        pw.print(prefix);
        pw.print("mFullScreen: ");
        pw.println(this.mFullScreen);
        pw.print(prefix);
        pw.print("mVisibleDatasetsMaxCount: ");
        pw.println(this.mVisibleDatasetsMaxCount);
        if (this.mHeader != null) {
            pw.print(prefix);
            pw.print("mHeader: ");
            pw.println(this.mHeader);
        }
        if (this.mListView != null) {
            pw.print(prefix);
            pw.print("mListView: ");
            pw.println(this.mListView);
        }
        if (this.mFooter != null) {
            pw.print(prefix);
            pw.print("mFooter: ");
            pw.println(this.mFooter);
        }
        if (this.mAdapter != null) {
            pw.print(prefix);
            pw.print("mAdapter: ");
            pw.println(this.mAdapter);
        }
        if (this.mFilterText != null) {
            pw.print(prefix);
            pw.print("mFilterText: ");
            com.android.server.autofill.Helper.printlnRedactedText(pw, this.mFilterText);
        }
        pw.print(prefix);
        pw.print("mContentWidth: ");
        pw.println(this.mContentWidth);
        pw.print(prefix);
        pw.print("mContentHeight: ");
        pw.println(this.mContentHeight);
        pw.print(prefix);
        pw.print("mDestroyed: ");
        pw.println(this.mDestroyed);
        pw.print(prefix);
        pw.print("mContext: ");
        pw.println(this.mContext);
        pw.print(prefix);
        pw.print("theme id: ");
        pw.print(this.mThemeId);
        switch (this.mThemeId) {
            case 16974834:
                pw.println(" (dark)");
                break;
            case 16974846:
                pw.println(" (light)");
                break;
            default:
                pw.println("(UNKNOWN_MODE)");
                break;
        }
        if (this.mWindow != null) {
            pw.print(prefix);
            pw.print("mWindow: ");
            java.lang.String prefix2 = prefix + "  ";
            pw.println();
            pw.print(prefix2);
            pw.print("showing: ");
            pw.println(this.mWindow.mShowing);
            pw.print(prefix2);
            pw.print("view: ");
            pw.println(this.mWindow.mContentView);
            if (this.mWindow.mShowParams != null) {
                pw.print(prefix2);
                pw.print("params: ");
                pw.println(this.mWindow.mShowParams);
            }
            pw.print(prefix2);
            pw.print("screen coordinates: ");
            if (this.mWindow.mContentView == null) {
                pw.println("N/A");
                return;
            }
            int[] coordinates = this.mWindow.mContentView.getLocationOnScreen();
            pw.print(coordinates[0]);
            pw.print("x");
            pw.println(coordinates[1]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void announceSearchResultIfNeeded() {
        if (android.view.accessibility.AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            if (this.mAnnounceFilterResult == null) {
                this.mAnnounceFilterResult = new com.android.server.autofill.ui.FillUi.AnnounceFilterResult();
            }
            this.mAnnounceFilterResult.post();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class ItemsAdapter extends android.widget.BaseAdapter implements android.widget.Filterable {
        private final java.util.List<com.android.server.autofill.ui.FillUi.ViewItem> mAllItems;
        private final java.util.List<com.android.server.autofill.ui.FillUi.ViewItem> mFilteredItems = new java.util.ArrayList();

        ItemsAdapter(java.util.List<com.android.server.autofill.ui.FillUi.ViewItem> items) {
            this.mAllItems = java.util.Collections.unmodifiableList(new java.util.ArrayList(items));
            this.mFilteredItems.addAll(items);
        }

        /* JADX INFO: renamed from: com.android.server.autofill.ui.FillUi$ItemsAdapter$1, reason: invalid class name */
        class AnonymousClass1 extends android.widget.Filter {
            AnonymousClass1() {
            }

            @Override // android.widget.Filter
            protected android.widget.Filter.FilterResults performFiltering(final java.lang.CharSequence filterText) {
                java.util.List<com.android.server.autofill.ui.FillUi.ViewItem> filtered = (java.util.List) com.android.server.autofill.ui.FillUi.ItemsAdapter.this.mAllItems.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.autofill.ui.FillUi$ItemsAdapter$1$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return ((com.android.server.autofill.ui.FillUi.ViewItem) obj).matches(filterText);
                    }
                }).collect(java.util.stream.Collectors.toList());
                android.widget.Filter.FilterResults results = new android.widget.Filter.FilterResults();
                results.values = filtered;
                results.count = filtered.size();
                return results;
            }

            @Override // android.widget.Filter
            protected void publishResults(java.lang.CharSequence constraint, android.widget.Filter.FilterResults results) {
                int oldItemCount = com.android.server.autofill.ui.FillUi.ItemsAdapter.this.mFilteredItems.size();
                com.android.server.autofill.ui.FillUi.ItemsAdapter.this.mFilteredItems.clear();
                if (results.count > 0) {
                    java.util.List<com.android.server.autofill.ui.FillUi.ViewItem> items = (java.util.List) results.values;
                    com.android.server.autofill.ui.FillUi.ItemsAdapter.this.mFilteredItems.addAll(items);
                }
                boolean resultCountChanged = oldItemCount != com.android.server.autofill.ui.FillUi.ItemsAdapter.this.mFilteredItems.size();
                if (resultCountChanged) {
                    com.android.server.autofill.ui.FillUi.this.announceSearchResultIfNeeded();
                }
                com.android.server.autofill.ui.FillUi.ItemsAdapter.this.notifyDataSetChanged();
            }
        }

        @Override // android.widget.Filterable
        public android.widget.Filter getFilter() {
            return new com.android.server.autofill.ui.FillUi.ItemsAdapter.AnonymousClass1();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mFilteredItems.size();
        }

        @Override // android.widget.Adapter
        public com.android.server.autofill.ui.FillUi.ViewItem getItem(int position) {
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

    private final class AnnounceFilterResult implements java.lang.Runnable {
        private static final int SEARCH_RESULT_ANNOUNCEMENT_DELAY = 1000;

        private AnnounceFilterResult() {
        }

        public void post() {
            remove();
            com.android.server.autofill.ui.FillUi.this.mListView.postDelayed(this, 1000L);
        }

        public void remove() {
            com.android.server.autofill.ui.FillUi.this.mListView.removeCallbacks(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            java.lang.String text;
            int count = com.android.server.autofill.ui.FillUi.this.mListView.getAdapter().getCount();
            if (count <= 0) {
                text = com.android.server.autofill.ui.FillUi.this.mContext.getString(android.R.string.autofill_error_cannot_autofill);
            } else {
                java.util.Map<java.lang.String, java.lang.Object> arguments = new java.util.HashMap<>();
                arguments.put(com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, java.lang.Integer.valueOf(count));
                text = android.util.PluralsMessageFormatter.format(com.android.server.autofill.ui.FillUi.this.mContext.getResources(), arguments, android.R.string.autofill_picker_accessibility_title);
            }
            com.android.server.autofill.ui.FillUi.this.mListView.announceForAccessibility(text);
        }
    }
}
