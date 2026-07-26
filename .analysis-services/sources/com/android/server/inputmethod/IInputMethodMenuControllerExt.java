package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public interface IInputMethodMenuControllerExt {
    default boolean showInputMethodMenu(android.content.Context context, int userId, int displayId, android.os.IBinder dialogToken, java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> imList, int checkItem, boolean showHardKeyboardSwitch, boolean showImeWithHardKeyboard, android.widget.CompoundButton.OnCheckedChangeListener onCheckedChangeListener, android.content.DialogInterface.OnClickListener onClickListener, android.content.DialogInterface.OnCancelListener onCancelListener) {
        return false;
    }

    default void setShowHardKeyboardSwitch(boolean showHardKeyboardSwitch) {
    }

    default void setShowImeWithHardKeyboard(boolean showImeWithHardKeyboard) {
    }

    default boolean isInputMethodMenuShowing() {
        return false;
    }

    default void hideInputMethodMenu() {
    }
}
