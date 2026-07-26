package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodMenuController {
    private static final java.lang.String TAG = com.android.server.inputmethod.InputMethodMenuController.class.getSimpleName();
    private android.app.AlertDialog.Builder mDialogBuilder;
    private com.android.server.inputmethod.InputMethodDialogWindowContext mDialogWindowContext;
    private android.view.inputmethod.InputMethodInfo[] mIms;
    private final com.android.server.inputmethod.InputMethodManagerService mService;
    private boolean mShowImeWithHardKeyboard;
    private int[] mSubtypeIds;
    private android.app.AlertDialog mSwitchingDialog;
    private android.view.View mSwitchingDialogTitleView;
    private com.android.server.inputmethod.InputMethodMenuController.InputMethodMenuControllerWrapper mImmcWrapper = new com.android.server.inputmethod.InputMethodMenuController.InputMethodMenuControllerWrapper();
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);

    InputMethodMenuController(com.android.server.inputmethod.InputMethodManagerService service) {
        this.mService = service;
    }

    void showInputMethodMenuLocked(boolean showAuxSubtypes, int displayId, java.lang.String preferredInputMethodId, int preferredInputMethodSubtypeId, java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> imList) throws java.lang.Throwable {
        int subtypeId;
        android.view.inputmethod.InputMethodSubtype currentSubtype;
        if (com.android.server.inputmethod.InputMethodManagerService.DEBUG) {
            android.util.Slog.v(TAG, "Show switching menu. showAuxSubtypes=" + showAuxSubtypes);
        }
        final int userId = this.mService.getCurrentImeUserIdLocked();
        hideInputMethodMenuLocked();
        int preferredInputMethodSubtypeId2 = preferredInputMethodSubtypeId;
        if (preferredInputMethodSubtypeId2 == -1 && (currentSubtype = this.mService.getCurrentInputMethodSubtypeLocked()) != null) {
            java.lang.String curMethodId = this.mService.getSelectedMethodIdLocked();
            android.view.inputmethod.InputMethodInfo currentImi = this.mService.queryInputMethodForCurrentUserLocked(curMethodId);
            preferredInputMethodSubtypeId2 = com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(currentImi, currentSubtype.hashCode());
        }
        int size = imList.size();
        this.mIms = new android.view.inputmethod.InputMethodInfo[size];
        this.mSubtypeIds = new int[size];
        int checkedItem = -1;
        for (int i = 0; i < size; i++) {
            com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem item = imList.get(i);
            this.mIms[i] = item.mImi;
            this.mSubtypeIds[i] = item.mSubtypeId;
            if (this.mIms[i].getId().equals(preferredInputMethodId) && ((subtypeId = this.mSubtypeIds[i]) == -1 || ((preferredInputMethodSubtypeId2 == -1 && subtypeId == 0) || subtypeId == preferredInputMethodSubtypeId2))) {
                checkedItem = i;
            }
        }
        if (checkedItem == -1) {
            android.util.Slog.w(TAG, "Switching menu shown with no item selected, IME id: " + preferredInputMethodId + ", subtype index: " + preferredInputMethodSubtypeId2);
        }
        if (this.mDialogWindowContext == null) {
            this.mDialogWindowContext = new com.android.server.inputmethod.InputMethodDialogWindowContext();
        }
        android.content.Context dialogWindowContext = this.mDialogWindowContext.get(displayId);
        this.mDialogBuilder = new android.app.AlertDialog.Builder(dialogWindowContext);
        this.mDialogBuilder.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(android.content.DialogInterface dialogInterface) {
                this.f$0.lambda$showInputMethodMenuLocked$0(dialogInterface);
            }
        });
        android.content.Context dialogContext = this.mDialogBuilder.getContext();
        android.content.res.TypedArray a = dialogContext.obtainStyledAttributes(null, com.android.internal.R.styleable.DialogPreference, android.R.attr.alertDialogStyle, 0);
        android.graphics.drawable.Drawable dialogIcon = a.getDrawable(2);
        a.recycle();
        this.mDialogBuilder.setIcon(dialogIcon);
        android.view.LayoutInflater inflater = (android.view.LayoutInflater) dialogContext.getSystemService(android.view.LayoutInflater.class);
        android.view.View tv = inflater.inflate(android.R.layout.input_method_nav_ime_switcher, (android.view.ViewGroup) null);
        this.mDialogBuilder.setCustomTitle(tv);
        this.mSwitchingDialogTitleView = tv;
        this.mSwitchingDialogTitleView.findViewById(android.R.id.flagNoPersonalizedLearning).setVisibility(this.mWindowManagerInternal.isHardKeyboardAvailable() ? 0 : 8);
        android.widget.Switch hardKeySwitch = (android.widget.Switch) this.mSwitchingDialogTitleView.findViewById(android.R.id.flagReportViewIds);
        hardKeySwitch.setChecked(this.mShowImeWithHardKeyboard);
        hardKeySwitch.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
                this.f$0.lambda$showInputMethodMenuLocked$1(userId, compoundButton, z);
            }
        });
        final com.android.server.inputmethod.InputMethodMenuController.ImeSubtypeListAdapter adapter = new com.android.server.inputmethod.InputMethodMenuController.ImeSubtypeListAdapter(dialogContext, android.R.layout.input_method_navigation_bar, imList, checkedItem);
        android.content.DialogInterface.OnClickListener choiceListener = new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(android.content.DialogInterface dialogInterface, int i2) {
                this.f$0.lambda$showInputMethodMenuLocked$2(adapter, dialogInterface, i2);
            }
        };
        this.mDialogBuilder.setSingleChoiceItems(adapter, checkedItem, choiceListener);
        this.mSwitchingDialog = this.mDialogBuilder.create();
        this.mSwitchingDialog.setCanceledOnTouchOutside(true);
        android.view.Window w = this.mSwitchingDialog.getWindow();
        android.view.WindowManager.LayoutParams attrs = w.getAttributes();
        w.setType(2012);
        w.setHideOverlayWindows(true);
        attrs.token = dialogWindowContext.getWindowContextToken();
        attrs.privateFlags |= 16;
        attrs.setTitle("Select input method");
        w.setAttributes(attrs);
        this.mService.updateSystemUiLocked();
        this.mService.sendOnNavButtonFlagsChangedLocked();
        if (this.mImmcWrapper.getExtImpl().showInputMethodMenu(this.mService.mContext, this.mService.getCurrentImeUserIdLocked(), displayId, attrs.token, imList, checkedItem, this.mWindowManagerInternal.isHardKeyboardAvailable(), this.mShowImeWithHardKeyboard, new android.widget.CompoundButton.OnCheckedChangeListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(android.widget.CompoundButton compoundButton, boolean z) {
                this.f$0.lambda$showInputMethodMenuLocked$3(userId, compoundButton, z);
            }
        }, choiceListener, new android.content.DialogInterface.OnCancelListener() { // from class: com.android.server.inputmethod.InputMethodMenuController$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(android.content.DialogInterface dialogInterface) {
                this.f$0.lambda$showInputMethodMenuLocked$4(dialogInterface);
            }
        })) {
            return;
        }
        this.mSwitchingDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenuLocked$0(android.content.DialogInterface dialog) {
        hideInputMethodMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenuLocked$1(int userId, android.widget.CompoundButton buttonView, boolean isChecked) {
        com.android.server.inputmethod.SecureSettingsWrapper.putBoolean("show_ime_with_hard_keyboard", isChecked, userId);
        hideInputMethodMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenuLocked$2(com.android.server.inputmethod.InputMethodMenuController.ImeSubtypeListAdapter adapter, android.content.DialogInterface dialog, int which) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            if (this.mIms != null && this.mIms.length > which && this.mSubtypeIds != null && this.mSubtypeIds.length > which) {
                android.view.inputmethod.InputMethodInfo im = this.mIms[which];
                int subtypeId = this.mSubtypeIds[which];
                int lastChecked = adapter.mCheckedItem;
                adapter.mCheckedItem = which;
                adapter.notifyDataSetChanged();
                if (im != null) {
                    if (subtypeId < 0 || subtypeId >= im.getSubtypeCount()) {
                        subtypeId = -1;
                    }
                    this.mService.setInputMethodLocked(im.getId(), subtypeId);
                    if (lastChecked != -1) {
                        this.mService.getWrapper().getExtImpl().onInputMethodPickByUser(adapter.getItem(lastChecked), adapter.getItem(which));
                    }
                }
                hideInputMethodMenuLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenuLocked$3(int userId, android.widget.CompoundButton compoundButton, boolean isChecked) {
        com.android.server.inputmethod.SecureSettingsWrapper.putBoolean("show_ime_with_hard_keyboard", isChecked, userId);
        hideInputMethodMenu();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodMenuLocked$4(android.content.DialogInterface dialogInterface) {
        hideInputMethodMenu();
    }

    void updateKeyboardFromSettingsLocked() {
        this.mShowImeWithHardKeyboard = com.android.server.inputmethod.SecureSettingsWrapper.getBoolean("show_ime_with_hard_keyboard", false, this.mService.getCurrentImeUserIdLocked());
        this.mImmcWrapper.getExtImpl().setShowImeWithHardKeyboard(this.mShowImeWithHardKeyboard);
        if (this.mSwitchingDialog != null && this.mSwitchingDialogTitleView != null && this.mSwitchingDialog.isShowing()) {
            android.widget.Switch hardKeySwitch = (android.widget.Switch) this.mSwitchingDialogTitleView.findViewById(android.R.id.flagReportViewIds);
            hardKeySwitch.setChecked(this.mShowImeWithHardKeyboard);
        }
    }

    void hideInputMethodMenu() {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            hideInputMethodMenuLocked();
        }
    }

    void hideInputMethodMenuLocked() {
        if (com.android.server.inputmethod.InputMethodManagerService.DEBUG) {
            android.util.Slog.v(TAG, "Hide switching menu");
        }
        if (this.mSwitchingDialog != null) {
            this.mImmcWrapper.getExtImpl().hideInputMethodMenu();
            this.mSwitchingDialog.dismiss();
            this.mSwitchingDialog = null;
            this.mSwitchingDialogTitleView = null;
            this.mService.updateSystemUiLocked();
            this.mService.sendOnNavButtonFlagsChangedLocked();
            this.mDialogBuilder = null;
            this.mIms = null;
        }
    }

    android.app.AlertDialog getSwitchingDialogLocked() {
        return this.mSwitchingDialog;
    }

    boolean getShowImeWithHardKeyboard() {
        return this.mShowImeWithHardKeyboard;
    }

    boolean isisInputMethodPickerShownForTestLocked() {
        if (this.mSwitchingDialog == null) {
            return false;
        }
        if (this.mImmcWrapper.getExtImpl().isInputMethodMenuShowing()) {
            return true;
        }
        return this.mSwitchingDialog.isShowing();
    }

    void handleHardKeyboardStatusChange(boolean available) {
        if (com.android.server.inputmethod.InputMethodManagerService.DEBUG) {
            android.util.Slog.w(TAG, "HardKeyboardStatusChanged: available=" + available);
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            this.mImmcWrapper.getExtImpl().setShowHardKeyboardSwitch(available);
            if (this.mSwitchingDialog != null && this.mSwitchingDialogTitleView != null && this.mSwitchingDialog.isShowing()) {
                this.mSwitchingDialogTitleView.findViewById(android.R.id.flagNoPersonalizedLearning).setVisibility(available ? 0 : 8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class ImeSubtypeListAdapter extends android.widget.ArrayAdapter<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> {
        public int mCheckedItem;
        private final android.view.LayoutInflater mInflater;
        private final java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> mItemsList;
        private final int mTextViewResourceId;

        private ImeSubtypeListAdapter(android.content.Context context, int textViewResourceId, java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> itemsList, int checkedItem) {
            super(context, textViewResourceId, itemsList);
            this.mTextViewResourceId = textViewResourceId;
            this.mItemsList = itemsList;
            this.mCheckedItem = checkedItem;
            this.mInflater = android.view.LayoutInflater.from(context);
        }

        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.view.View view = convertView != null ? convertView : this.mInflater.inflate(this.mTextViewResourceId, (android.view.ViewGroup) null);
            if (position < 0 || position >= this.mItemsList.size()) {
                return view;
            }
            com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem item = this.mItemsList.get(position);
            java.lang.CharSequence imeName = item.mImeName;
            java.lang.CharSequence subtypeName = item.mSubtypeName;
            android.widget.TextView firstTextView = (android.widget.TextView) view.findViewById(android.R.id.text1);
            android.widget.TextView secondTextView = (android.widget.TextView) view.findViewById(android.R.id.text2);
            if (android.text.TextUtils.isEmpty(subtypeName)) {
                firstTextView.setText(imeName);
                secondTextView.setVisibility(8);
            } else {
                firstTextView.setText(subtypeName);
                secondTextView.setText(imeName);
                secondTextView.setVisibility(0);
            }
            android.widget.RadioButton radioButton = (android.widget.RadioButton) view.findViewById(android.R.id.personalInfo);
            radioButton.setChecked(position == this.mCheckedItem);
            return view;
        }
    }

    public com.android.server.inputmethod.IInputMethodMenuControllerWrapper getWrapper() {
        return this.mImmcWrapper;
    }

    private class InputMethodMenuControllerWrapper implements com.android.server.inputmethod.IInputMethodMenuControllerWrapper {
        private final com.android.server.inputmethod.IInputMethodMenuControllerExt mImmcExt;

        private InputMethodMenuControllerWrapper() {
            this.mImmcExt = (com.android.server.inputmethod.IInputMethodMenuControllerExt) system.ext.loader.core.ExtLoader.type(com.android.server.inputmethod.IInputMethodMenuControllerExt.class).base(com.android.server.inputmethod.InputMethodMenuController.this).create();
        }

        public com.android.server.inputmethod.IInputMethodMenuControllerExt getExtImpl() {
            return this.mImmcExt;
        }
    }
}
