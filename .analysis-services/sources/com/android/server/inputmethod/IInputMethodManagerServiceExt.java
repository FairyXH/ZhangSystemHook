package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public interface IInputMethodManagerServiceExt {
    default void initServiceUx(java.lang.String threadName, int tid) {
    }

    default void setServiceUxEnabled(boolean enabled, java.lang.String reason) {
    }

    default boolean isMultiAppUserId(int userId) {
        return false;
    }

    default void configInputMethodAfterQuery() {
    }

    default void updateDefaultEnabledImes(java.util.List<android.view.inputmethod.InputMethodInfo> defaultEnabledIme) {
    }

    default void onServerRegisterContentObserver(android.database.ContentObserver contentObserver, int userId) {
    }

    default boolean onServerSettingsObserverChanged(android.database.ContentObserver contentObserver, int userId, boolean selfChange, android.net.Uri uri) {
        return false;
    }

    default void onFinishPackageChanges(int userId) {
    }

    default void notifyImeAttributeChanged(boolean isTextEditor, android.view.inputmethod.EditorInfo attribute, boolean sameWindowFocused, int displayId) {
    }

    default android.view.inputmethod.InputMethodInfo getDefaultInputMethodByConfig(int userId) {
        return null;
    }

    default java.lang.String onSetSelectedMethodId(java.lang.String selectedMethodId) {
        return selectedMethodId;
    }

    default void configDebug(java.io.FileDescriptor fd, java.lang.String[] args) {
    }

    default void onInputMethodPickByUser(com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem oldItem, com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem newItem) {
    }

    default void unfreezeInputMethodPackage(android.view.inputmethod.InputMethodInfo info) {
    }

    default void onImeInitialized(int displayId) {
    }

    default void startInputToSynergy(com.android.internal.inputmethod.IInputMethod.StartInputParams params) {
    }

    default boolean shouldIgnoreShowBySynergy(java.lang.String inputmethodId) {
        return false;
    }

    default void onClientStateSwitch(com.android.server.inputmethod.ClientState clientState) {
    }

    default boolean shouldIgnoreStartInput(android.content.Context context, int startInputFlags, android.view.inputmethod.EditorInfo attribute, int windowDisplayId, int curTokenDisplayId, boolean inputShown) {
        return false;
    }

    default boolean shouldIgnoreFocusCheck(android.os.IBinder windowToken, int imeClientFocus) {
        return false;
    }

    default void updateOsenseAction() {
    }

    default boolean shouldHideImeSwitcher() {
        return false;
    }

    default boolean onApplyImeVisibility(boolean setVisible) {
        return false;
    }

    default boolean isCarDisplayId(int displayId) {
        return false;
    }

    default boolean shouldInterceptImeForZoom(android.os.IBinder token) {
        return false;
    }

    default boolean isInputMethodAccessible(java.lang.String packageName) {
        return true;
    }

    default void setBinderService(android.os.Binder binderService) {
    }

    default boolean setInputMethodLocked(android.view.inputmethod.InputMethodInfo info, int subtypeId) {
        return false;
    }

    default boolean shouldForceHideSoftInput(com.android.server.inputmethod.ImeVisibilityStateComputer visibilityStateComputer, int reason) {
        return false;
    }

    default void setAsyncBinderUxFlag(boolean applyToUx) {
    }

    default boolean shouldInterceptInputMethodPicker(int auxiliarySubtypeMode, int displayId) {
        return false;
    }

    default void showInputMethodPickerIfNeeded() {
    }

    public interface IStaticExt {
        default void logDebug(java.lang.String tag, java.lang.String msg) {
        }

        default void logDebugIme(java.lang.String tag, java.lang.String msg) {
        }

        default void logMethodCallers(java.lang.String tag, java.lang.String msg) {
        }

        default boolean shouldHideInputMethodService(java.lang.String packageName, java.lang.String methodId) {
            return false;
        }
    }

    public interface ILifecycleExt {
        default com.android.server.inputmethod.InputMethodManagerService initInputMethodManagerService(android.content.Context context) {
            return null;
        }
    }
}
