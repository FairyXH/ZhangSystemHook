package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
final class SaveUi {
    private static final int SCROLL_BAR_DEFAULT_DELAY_BEFORE_FADE_MS = 500;
    private static final java.lang.String TAG = "SaveUi";
    private static final int THEME_ID_DARK = 16974836;
    private static final int THEME_ID_LIGHT = 16974847;
    private final boolean mCompatMode;
    private final android.content.ComponentName mComponentName;
    private boolean mDestroyed;
    private final android.app.Dialog mDialog;
    private final com.android.server.autofill.ui.SaveUi.OneActionThenDestroyListener mListener;
    private final com.android.server.autofill.ui.OverlayControl mOverlayControl;
    private final com.android.server.autofill.ui.PendingUi mPendingUi;
    private final java.lang.String mServicePackageName;
    private final java.lang.CharSequence mSubTitle;
    private final int mThemeId;
    private final java.lang.CharSequence mTitle;
    private final int mType;
    private final android.os.Handler mHandler = com.android.server.UiThread.getHandler();
    private final com.android.internal.logging.MetricsLogger mMetricsLogger = new com.android.internal.logging.MetricsLogger();

    public interface OnSaveListener {
        void onCancel(android.content.IntentSender intentSender);

        void onDestroy();

        void onSave();

        void startIntentSender(android.content.IntentSender intentSender, android.content.Intent intent);
    }

    private class OneActionThenDestroyListener implements com.android.server.autofill.ui.SaveUi.OnSaveListener {
        private boolean mDone;
        private final com.android.server.autofill.ui.SaveUi.OnSaveListener mRealListener;

        OneActionThenDestroyListener(com.android.server.autofill.ui.SaveUi.OnSaveListener realListener) {
            this.mRealListener = realListener;
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void onSave() {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.ui.SaveUi.TAG, "OneTimeListener.onSave(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mRealListener.onSave();
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void onCancel(android.content.IntentSender listener) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.ui.SaveUi.TAG, "OneTimeListener.onCancel(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mRealListener.onCancel(listener);
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void onDestroy() {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.ui.SaveUi.TAG, "OneTimeListener.onDestroy(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mDone = true;
            this.mRealListener.onDestroy();
        }

        @Override // com.android.server.autofill.ui.SaveUi.OnSaveListener
        public void startIntentSender(android.content.IntentSender intentSender, android.content.Intent intent) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.ui.SaveUi.TAG, "OneTimeListener.startIntentSender(): " + this.mDone);
            }
            if (this.mDone) {
                return;
            }
            this.mRealListener.startIntentSender(intentSender, intent);
        }
    }

    SaveUi(android.content.Context context, com.android.server.autofill.ui.PendingUi pendingUi, java.lang.CharSequence serviceLabel, android.graphics.drawable.Drawable serviceIcon, java.lang.String servicePackageName, android.content.ComponentName componentName, final android.service.autofill.SaveInfo info, android.service.autofill.ValueFinder valueFinder, com.android.server.autofill.ui.OverlayControl overlayControl, com.android.server.autofill.ui.SaveUi.OnSaveListener listener, boolean nightMode, boolean isUpdate, boolean compatMode, boolean showServiceIcon) {
        if (com.android.server.autofill.Helper.sVerbose) {
            com.android.server.utils.Slogf.v(TAG, "nightMode: %b displayId: %d", java.lang.Boolean.valueOf(nightMode), java.lang.Integer.valueOf(context.getDisplayId()));
        }
        this.mThemeId = nightMode ? 16974836 : 16974847;
        this.mPendingUi = pendingUi;
        this.mListener = new com.android.server.autofill.ui.SaveUi.OneActionThenDestroyListener(listener);
        this.mOverlayControl = overlayControl;
        this.mServicePackageName = servicePackageName;
        this.mComponentName = componentName;
        this.mCompatMode = compatMode;
        android.content.Context context2 = new android.view.ContextThemeWrapper(context, this.mThemeId) { // from class: com.android.server.autofill.ui.SaveUi.1
            @Override // android.content.ContextWrapper, android.content.Context
            public void startActivity(android.content.Intent intent) {
                if (resolveActivity(intent) == null) {
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(com.android.server.autofill.ui.SaveUi.TAG, "Can not startActivity for save UI with intent=" + intent);
                    }
                } else {
                    intent.putExtra("android.view.autofill.extra.RESTORE_CROSS_ACTIVITY", true);
                    android.app.PendingIntent p = android.app.PendingIntent.getActivityAsUser(this, 0, intent, android.hardware.audio.common.V2_0.AudioFormat.AMR_WB, android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(1).toBundle(), android.os.UserHandle.CURRENT);
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(com.android.server.autofill.ui.SaveUi.TAG, "startActivity add save UI restored with intent=" + intent);
                    }
                    com.android.server.autofill.ui.SaveUi.this.startIntentSenderWithRestore(p, intent);
                }
            }

            private android.content.ComponentName resolveActivity(android.content.Intent intent) {
                android.content.pm.PackageManager packageManager = getPackageManager();
                android.content.ComponentName componentName2 = intent.resolveActivity(packageManager);
                if (componentName2 != null) {
                    return componentName2;
                }
                intent.addFlags(2048);
                android.content.pm.ActivityInfo ai = intent.resolveActivityInfo(packageManager, 8388608);
                if (ai != null) {
                    return new android.content.ComponentName(ai.applicationInfo.packageName, ai.name);
                }
                return null;
            }
        };
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(context2);
        android.view.View view = inflater.inflate(android.R.layout.auto_complete_list, (android.view.ViewGroup) null);
        android.widget.TextView titleView = (android.widget.TextView) view.findViewById(android.R.id.animation);
        android.util.ArraySet<java.lang.String> types = new android.util.ArraySet<>(3);
        this.mType = info.getType();
        if ((this.mType & 1) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_type_payment_card));
        }
        if ((this.mType & 2) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_type_credit_card));
        }
        int count = java.lang.Integer.bitCount(this.mType & 100);
        if (count > 1 || (this.mType & 128) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_type_password));
        } else if ((this.mType & 64) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_type_username));
        } else if ((this.mType & 4) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_type_debit_card));
        } else if ((this.mType & 32) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_type_email_address));
        }
        if ((this.mType & 8) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_yes));
        }
        if ((this.mType & 16) != 0) {
            types.add(context2.getString(android.R.string.autofill_save_type_generic_card));
        }
        switch (types.size()) {
            case 1:
                this.mTitle = android.text.Html.fromHtml(context2.getString(isUpdate ? android.R.string.back_button_label : android.R.string.autofill_save_type_address, types.valueAt(0), serviceLabel), 0);
                break;
            case 2:
                this.mTitle = android.text.Html.fromHtml(context2.getString(isUpdate ? android.R.string.autofill_update_yes : android.R.string.autofill_save_title_with_3types, types.valueAt(0), types.valueAt(1), serviceLabel), 0);
                break;
            case 3:
                this.mTitle = android.text.Html.fromHtml(context2.getString(isUpdate ? android.R.string.autofill_window_title : android.R.string.autofill_save_title_with_type, types.valueAt(0), types.valueAt(1), types.valueAt(2), serviceLabel), 0);
                break;
            default:
                this.mTitle = android.text.Html.fromHtml(context2.getString(isUpdate ? android.R.string.autofill_update_title_with_type : android.R.string.autofill_save_title_with_2types, serviceLabel), 0);
                break;
        }
        titleView.setText(this.mTitle);
        if (showServiceIcon) {
            setServiceIcon(context2, view, serviceIcon);
        }
        boolean hasCustomDescription = applyCustomDescription(context2, view, valueFinder, info);
        if (hasCustomDescription) {
            this.mSubTitle = null;
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "on constructor: applied custom description");
            }
        } else {
            this.mSubTitle = info.getDescription();
            if (this.mSubTitle != null) {
                writeLog(1131);
                android.view.ViewGroup subtitleContainer = (android.view.ViewGroup) view.findViewById(android.R.id.am_label);
                android.widget.TextView subtitleView = new android.widget.TextView(context2);
                subtitleView.setText(this.mSubTitle);
                applyMovementMethodIfNeed(subtitleView);
                subtitleContainer.addView(subtitleView, new android.view.ViewGroup.LayoutParams(-1, -2));
                subtitleContainer.setVisibility(0);
                subtitleContainer.setScrollBarDefaultDelayBeforeFade(500);
            }
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "on constructor: title=" + ((java.lang.Object) this.mTitle) + ", subTitle=" + ((java.lang.Object) this.mSubTitle));
            }
        }
        android.widget.TextView noButton = (android.widget.TextView) view.findViewById(android.R.id.ampm_layout);
        int negativeActionStyle = info.getNegativeActionStyle();
        switch (negativeActionStyle) {
            case 1:
                noButton.setText(android.R.string.autofill_save_title);
                break;
            case 2:
                noButton.setText(android.R.string.autofill_save_no);
                break;
            default:
                noButton.setText(android.R.string.autofill_save_notnow);
                break;
        }
        noButton.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(android.view.View view2) {
                this.f$0.lambda$new$0(info, view2);
            }
        });
        android.widget.TextView yesButton = (android.widget.TextView) view.findViewById(android.R.id.animator);
        if (info.getPositiveActionStyle() == 1) {
            yesButton.setText(android.R.string.app_suspended_more_details);
        } else if (isUpdate) {
            yesButton.setText(android.R.string.badPin);
        }
        yesButton.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(android.view.View view2) {
                this.f$0.lambda$new$1(view2);
            }
        });
        this.mDialog = new android.app.Dialog(context2, this.mThemeId);
        this.mDialog.setContentView(view);
        this.mDialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(android.content.DialogInterface dialogInterface) {
                this.f$0.lambda$new$2(dialogInterface);
            }
        });
        android.view.Window window = this.mDialog.getWindow();
        window.setType(2038);
        window.addFlags(131074);
        window.setDimAmount(0.6f);
        window.addPrivateFlags(16);
        window.setSoftInputMode(32);
        window.setGravity(81);
        window.setCloseOnTouchOutside(true);
        android.view.WindowManager.LayoutParams params = window.getAttributes();
        params.accessibilityTitle = context2.getString(android.R.string.autofill_save_never);
        params.windowAnimations = android.R.style.AutofillHalfScreenAnimation;
        params.setTrustedOverlay();
        final android.widget.ScrollView scrollView = (android.widget.ScrollView) view.findViewById(android.R.id.app_name_divider);
        final android.view.View divider = view.findViewById(android.R.id.appPredictor);
        android.view.ViewTreeObserver observer = scrollView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda3
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                this.f$0.lambda$new$3(scrollView, divider);
            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new android.view.ViewTreeObserver.OnScrollChangedListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda4
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public final void onScrollChanged() {
                this.f$0.lambda$new$4(scrollView, divider);
            }
        });
        show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.service.autofill.SaveInfo info, android.view.View v) {
        this.mListener.onCancel(info.getNegativeActionListener());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(android.view.View v) {
        this.mListener.onSave();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(android.content.DialogInterface d) {
        this.mListener.onCancel(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: adjustDividerVisibility, reason: merged with bridge method [inline-methods] and merged with bridge method [inline-methods] */
    public void lambda$new$4(android.widget.ScrollView scrollView, android.view.View divider) {
        boolean canScrollDown = scrollView.canScrollVertically(1);
        divider.setVisibility(canScrollDown ? 0 : 4);
    }

    private boolean applyCustomDescription(android.content.Context context, android.view.View saveUiView, android.service.autofill.ValueFinder valueFinder, android.service.autofill.SaveInfo info) {
        android.view.View customSubtitleView;
        java.util.ArrayList<android.util.Pair<java.lang.Integer, android.service.autofill.InternalTransformation>> transformations;
        android.widget.RemoteViews.InteractionHandler handler;
        java.util.ArrayList<android.util.Pair<android.service.autofill.InternalValidator, android.service.autofill.BatchUpdates>> updates;
        android.service.autofill.CustomDescription customDescription = info.getCustomDescription();
        if (customDescription != null) {
            writeLog(1129);
            android.widget.RemoteViews template = com.android.server.autofill.Helper.sanitizeRemoteView(customDescription.getPresentation());
            if (template == null) {
                android.util.Slog.w(TAG, "No remote view on custom description");
                return false;
            }
            java.util.ArrayList<android.util.Pair<java.lang.Integer, android.service.autofill.InternalTransformation>> transformations2 = customDescription.getTransformations();
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "applyCustomDescription(): transformations = " + transformations2);
            }
            if (transformations2 == null || android.service.autofill.InternalTransformation.batchApply(valueFinder, template, transformations2)) {
                android.widget.RemoteViews.InteractionHandler handler2 = new android.widget.RemoteViews.InteractionHandler() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda5
                    public final boolean onInteraction(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse remoteResponse) {
                        return this.f$0.lambda$applyCustomDescription$5(view, pendingIntent, remoteResponse);
                    }
                };
                try {
                    customSubtitleView = template.applyWithTheme(context, null, handler2, this.mThemeId);
                    java.util.ArrayList<android.util.Pair<android.service.autofill.InternalValidator, android.service.autofill.BatchUpdates>> updates2 = customDescription.getUpdates();
                    if (com.android.server.autofill.Helper.sVerbose) {
                        try {
                            android.util.Slog.v(TAG, "applyCustomDescription(): view = " + customSubtitleView + " updates=" + updates2);
                        } catch (java.lang.Exception e) {
                            e = e;
                            android.util.Slog.e(TAG, "Error applying custom description. ", e);
                            return false;
                        }
                    }
                    if (updates2 != null) {
                        int size = updates2.size();
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "custom description has " + size + " batch updates");
                        }
                        int i = 0;
                        while (i < size) {
                            android.util.Pair<android.service.autofill.InternalValidator, android.service.autofill.BatchUpdates> pair = updates2.get(i);
                            android.service.autofill.InternalValidator condition = (android.service.autofill.InternalValidator) pair.first;
                            if (condition == null || !condition.isValid(valueFinder)) {
                                transformations = transformations2;
                                handler = handler2;
                                updates = updates2;
                                try {
                                    if (com.android.server.autofill.Helper.sDebug) {
                                        android.util.Slog.d(TAG, "Skipping batch update #" + i);
                                    }
                                    i++;
                                    transformations2 = transformations;
                                    handler2 = handler;
                                    updates2 = updates;
                                } catch (java.lang.Exception e2) {
                                    e = e2;
                                    android.util.Slog.e(TAG, "Error applying custom description. ", e);
                                    return false;
                                }
                            } else {
                                android.service.autofill.BatchUpdates batchUpdates = (android.service.autofill.BatchUpdates) pair.second;
                                android.widget.RemoteViews templateUpdates = com.android.server.autofill.Helper.sanitizeRemoteView(batchUpdates.getUpdates());
                                transformations = transformations2;
                                if (templateUpdates == null) {
                                    handler = handler2;
                                    updates = updates2;
                                } else {
                                    try {
                                        if (com.android.server.autofill.Helper.sDebug) {
                                            handler = handler2;
                                            updates = updates2;
                                            android.util.Slog.d(TAG, "Applying template updates for batch update #" + i);
                                        } else {
                                            handler = handler2;
                                            updates = updates2;
                                        }
                                        templateUpdates.reapply(context, customSubtitleView);
                                    } catch (java.lang.Exception e3) {
                                        e = e3;
                                        android.util.Slog.e(TAG, "Error applying custom description. ", e);
                                        return false;
                                    }
                                }
                                java.util.ArrayList<android.util.Pair<java.lang.Integer, android.service.autofill.InternalTransformation>> batchTransformations = batchUpdates.getTransformations();
                                if (batchTransformations != null) {
                                    if (com.android.server.autofill.Helper.sDebug) {
                                        android.util.Slog.d(TAG, "Applying child transformation for batch update #" + i + ": " + batchTransformations);
                                    }
                                    if (!android.service.autofill.InternalTransformation.batchApply(valueFinder, template, batchTransformations)) {
                                        android.util.Slog.w(TAG, "Could not apply child transformation for batch update #" + i + ": " + batchTransformations);
                                        return false;
                                    }
                                    template.reapply(context, customSubtitleView);
                                }
                                i++;
                                transformations2 = transformations;
                                handler2 = handler;
                                updates2 = updates;
                            }
                        }
                    }
                    android.util.SparseArray<android.service.autofill.InternalOnClickAction> actions = customDescription.getActions();
                    if (actions != null) {
                        int size2 = actions.size();
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "custom description has " + size2 + " actions");
                        }
                        if (!(customSubtitleView instanceof android.view.ViewGroup)) {
                            android.util.Slog.w(TAG, "cannot apply actions because custom description root is not a ViewGroup: " + customSubtitleView);
                        } else {
                            final android.view.ViewGroup rootView = (android.view.ViewGroup) customSubtitleView;
                            for (int i2 = 0; i2 < size2; i2++) {
                                int id = actions.keyAt(i2);
                                final android.service.autofill.InternalOnClickAction action = actions.valueAt(i2);
                                android.view.View child = rootView.findViewById(id);
                                if (child == null) {
                                    android.util.Slog.w(TAG, "Ignoring action " + action + " for view " + id + " because it's not on " + rootView);
                                } else {
                                    child.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda6
                                        @Override // android.view.View.OnClickListener
                                        public final void onClick(android.view.View view) {
                                            com.android.server.autofill.ui.SaveUi.lambda$applyCustomDescription$6(action, rootView, view);
                                        }
                                    });
                                }
                            }
                        }
                    }
                    applyTextViewStyle(customSubtitleView);
                } catch (java.lang.Exception e4) {
                    e = e4;
                }
                try {
                    android.view.ViewGroup subtitleContainer = (android.view.ViewGroup) saveUiView.findViewById(android.R.id.am_label);
                    subtitleContainer.addView(customSubtitleView);
                    subtitleContainer.setVisibility(0);
                    subtitleContainer.setScrollBarDefaultDelayBeforeFade(500);
                    return true;
                } catch (java.lang.Exception e5) {
                    e = e5;
                    android.util.Slog.e(TAG, "Error applying custom description. ", e);
                    return false;
                }
            }
            android.util.Slog.w(TAG, "could not apply main transformations on custom description");
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$applyCustomDescription$5(android.view.View view, android.app.PendingIntent pendingIntent, android.widget.RemoteViews.RemoteResponse response) {
        android.content.Intent intent = (android.content.Intent) response.getLaunchOptions(view).first;
        boolean isValid = isValidLink(pendingIntent, intent);
        if (!isValid) {
            android.metrics.LogMaker log = newLogMaker(1132, this.mType);
            log.setType(0);
            this.mMetricsLogger.write(log);
            return false;
        }
        startIntentSenderWithRestore(pendingIntent, intent);
        return true;
    }

    static /* synthetic */ void lambda$applyCustomDescription$6(android.service.autofill.InternalOnClickAction action, android.view.ViewGroup rootView, android.view.View v) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Applying " + action + " after " + v + " was clicked");
        }
        action.onClick(rootView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startIntentSenderWithRestore(android.app.PendingIntent pendingIntent, android.content.Intent intent) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Intercepting custom description intent");
        }
        android.os.IBinder token = this.mPendingUi.getToken();
        intent.putExtra("android.view.autofill.extra.RESTORE_SESSION_TOKEN", token);
        this.mListener.startIntentSender(pendingIntent.getIntentSender(), intent);
        this.mPendingUi.setState(2);
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "hiding UI until restored with token " + token);
        }
        hide();
        android.metrics.LogMaker log = newLogMaker(1132, this.mType);
        log.setType(1);
        this.mMetricsLogger.write(log);
    }

    private void applyTextViewStyle(android.view.View rootView) {
        final java.util.List<android.widget.TextView> textViews = new java.util.ArrayList<>();
        java.util.function.Predicate<android.view.View> predicate = new java.util.function.Predicate() { // from class: com.android.server.autofill.ui.SaveUi$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.autofill.ui.SaveUi.lambda$applyTextViewStyle$7(textViews, (android.view.View) obj);
            }
        };
        rootView.findViewByPredicate(predicate);
        int size = textViews.size();
        for (int i = 0; i < size; i++) {
            applyMovementMethodIfNeed(textViews.get(i));
        }
    }

    static /* synthetic */ boolean lambda$applyTextViewStyle$7(java.util.List textViews, android.view.View view) {
        if (view instanceof android.widget.TextView) {
            textViews.add((android.widget.TextView) view);
            return false;
        }
        return false;
    }

    private void applyMovementMethodIfNeed(android.widget.TextView textView) {
        java.lang.CharSequence message = textView.getText();
        if (android.text.TextUtils.isEmpty(message)) {
            return;
        }
        android.text.SpannableStringBuilder ssb = new android.text.SpannableStringBuilder(message);
        android.text.style.ClickableSpan[] spans = (android.text.style.ClickableSpan[]) ssb.getSpans(0, ssb.length(), android.text.style.ClickableSpan.class);
        if (com.android.internal.util.ArrayUtils.isEmpty(spans)) {
            return;
        }
        textView.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
    }

    private void setServiceIcon(android.content.Context context, android.view.View view, android.graphics.drawable.Drawable serviceIcon) {
        android.widget.ImageView iconView = (android.widget.ImageView) view.findViewById(android.R.id.am_pm_spinner);
        context.getResources();
        iconView.setImageDrawable(serviceIcon);
    }

    private static boolean isValidLink(android.app.PendingIntent pendingIntent, android.content.Intent intent) {
        if (pendingIntent == null) {
            android.util.Slog.w(TAG, "isValidLink(): custom description without pending intent");
            return false;
        }
        if (!pendingIntent.isActivity()) {
            android.util.Slog.w(TAG, "isValidLink(): pending intent not for activity");
            return false;
        }
        if (intent == null) {
            android.util.Slog.w(TAG, "isValidLink(): no intent");
            return false;
        }
        return true;
    }

    private android.metrics.LogMaker newLogMaker(int category, int saveType) {
        return newLogMaker(category).addTaggedData(1130, java.lang.Integer.valueOf(saveType));
    }

    private android.metrics.LogMaker newLogMaker(int category) {
        return com.android.server.autofill.Helper.newLogMaker(category, this.mComponentName, this.mServicePackageName, this.mPendingUi.sessionId, this.mCompatMode);
    }

    private void writeLog(int category) {
        this.mMetricsLogger.write(newLogMaker(category, this.mType));
    }

    void onPendingUi(int operation, android.os.IBinder token) {
        if (!this.mPendingUi.matches(token)) {
            android.util.Slog.w(TAG, "restore(" + operation + "): got token " + token + " instead of " + this.mPendingUi.getToken());
            return;
        }
        android.metrics.LogMaker log = newLogMaker(1134);
        try {
            switch (operation) {
                case 1:
                    log.setType(5);
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "Cancelling pending save dialog for " + token);
                    }
                    hide();
                    break;
                case 2:
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "Restoring save dialog for " + token);
                    }
                    log.setType(1);
                    show();
                    break;
                default:
                    log.setType(11);
                    android.util.Slog.w(TAG, "restore(): invalid operation " + operation);
                    break;
            }
            this.mMetricsLogger.write(log);
            this.mPendingUi.setState(4);
        } catch (java.lang.Throwable th) {
            this.mMetricsLogger.write(log);
            throw th;
        }
    }

    private void show() {
        android.util.Slog.i(TAG, "Showing save dialog: " + ((java.lang.Object) this.mTitle));
        this.mDialog.show();
        this.mOverlayControl.hideOverlays();
    }

    com.android.server.autofill.ui.PendingUi hide() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Hiding save dialog.");
        }
        try {
            this.mDialog.hide();
            this.mOverlayControl.showOverlays();
            return this.mPendingUi;
        } catch (java.lang.Throwable th) {
            this.mOverlayControl.showOverlays();
            throw th;
        }
    }

    boolean isShowing() {
        return this.mDialog.isShowing();
    }

    void destroy() {
        try {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "destroy()");
            }
            throwIfDestroyed();
            this.mListener.onDestroy();
            this.mHandler.removeCallbacksAndMessages(this.mListener);
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
        return this.mTitle == null ? "NO TITLE" : this.mTitle.toString();
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("title: ");
        pw.println(this.mTitle);
        pw.print(prefix);
        pw.print("subtitle: ");
        pw.println(this.mSubTitle);
        pw.print(prefix);
        pw.print("pendingUi: ");
        pw.println(this.mPendingUi);
        pw.print(prefix);
        pw.print("service: ");
        pw.println(this.mServicePackageName);
        pw.print(prefix);
        pw.print("app: ");
        pw.println(this.mComponentName.toShortString());
        pw.print(prefix);
        pw.print("compat mode: ");
        pw.println(this.mCompatMode);
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
}
