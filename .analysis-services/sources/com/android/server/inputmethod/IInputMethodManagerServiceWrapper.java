package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public interface IInputMethodManagerServiceWrapper {
    default com.android.server.inputmethod.IInputMethodManagerServiceExt getExtImpl() {
        return new com.android.server.inputmethod.IInputMethodManagerServiceExt() { // from class: com.android.server.inputmethod.IInputMethodManagerServiceWrapper.1
        };
    }

    default com.android.server.inputmethod.ClientState getCurClient() {
        return null;
    }

    default android.os.Handler getHandler() {
        return null;
    }

    default void setSelectedMethodIdLocked(java.lang.String selectedMethodId) {
    }

    default boolean isShowRequested() {
        return false;
    }

    default com.android.server.inputmethod.InputMethodBindingController getBindingController() {
        return null;
    }

    default com.android.server.inputmethod.InputMethodMenuController getInputMethodMenuController() {
        return null;
    }

    default com.android.server.inputmethod.InputMethodSettings getSettings() {
        return null;
    }

    default com.android.server.inputmethod.ClientController getClientController() {
        return null;
    }

    default void setSelectedInputMethodAndSubtypeLocked(android.view.inputmethod.InputMethodInfo imi, int subtypeId, boolean setSubtypeOnly) {
    }

    default void showInputMethodPickerFromDelay(int auxiliarySubtypeMode, int displayId) {
    }
}
