package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public abstract class InputMethodManagerInternal {
    private static final com.android.server.inputmethod.InputMethodManagerInternal NOP = new com.android.server.inputmethod.InputMethodManagerInternal() { // from class: com.android.server.inputmethod.InputMethodManagerInternal.1
        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void setInteractive(boolean interactive) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void hideAllInputMethods(int reason, int originatingDisplayId) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListAsUser(int userId) {
            return java.util.Collections.emptyList();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListAsUser(int userId) {
            return java.util.Collections.emptyList();
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onCreateInlineSuggestionsRequest(int userId, com.android.internal.inputmethod.InlineSuggestionsRequestInfo requestInfo, com.android.internal.inputmethod.InlineSuggestionsRequestCallback cb) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean switchToInputMethod(java.lang.String imeId, int userId) {
            return false;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean setInputMethodEnabled(java.lang.String imeId, boolean enabled, int userId) {
            return false;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void setVirtualDeviceInputMethodForAllUsers(int deviceId, java.lang.String imeId) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void registerInputMethodListListener(com.android.server.inputmethod.InputMethodManagerInternal.InputMethodListListener listener) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public boolean transferTouchFocusToImeWindow(android.os.IBinder sourceInputToken, int displayId, int userId) {
            return false;
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void reportImeControl(android.os.IBinder windowToken) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onImeParentChanged(int displayId) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void removeImeSurface(int displayId) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void updateImeWindowStatus(boolean disableImeIcon, int displayId) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onSessionForAccessibilityCreated(int accessibilityConnectionId, com.android.internal.inputmethod.IAccessibilityInputMethodSession session, int userId) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void unbindAccessibilityFromCurrentClient(int accessibilityConnectionId, int userId) {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void maybeFinishStylusHandwriting() {
        }

        @Override // com.android.server.inputmethod.InputMethodManagerInternal
        public void onSwitchKeyboardLayoutShortcut(int direction, int displayId, android.os.IBinder targetWindowToken) {
        }
    };

    @java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ImfLockFree {
    }

    public interface InputMethodListListener {
        void onInputMethodListUpdated(java.util.List<android.view.inputmethod.InputMethodInfo> list, int i);
    }

    public abstract java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListAsUser(int i);

    public abstract java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListAsUser(int i);

    public abstract void hideAllInputMethods(int i, int i2);

    public abstract void maybeFinishStylusHandwriting();

    public abstract void onCreateInlineSuggestionsRequest(int i, com.android.internal.inputmethod.InlineSuggestionsRequestInfo inlineSuggestionsRequestInfo, com.android.internal.inputmethod.InlineSuggestionsRequestCallback inlineSuggestionsRequestCallback);

    public abstract void onImeParentChanged(int i);

    public abstract void onSessionForAccessibilityCreated(int i, com.android.internal.inputmethod.IAccessibilityInputMethodSession iAccessibilityInputMethodSession, int i2);

    public abstract void onSwitchKeyboardLayoutShortcut(int i, int i2, android.os.IBinder iBinder);

    public abstract void registerInputMethodListListener(com.android.server.inputmethod.InputMethodManagerInternal.InputMethodListListener inputMethodListListener);

    public abstract void removeImeSurface(int i);

    public abstract void reportImeControl(android.os.IBinder iBinder);

    public abstract boolean setInputMethodEnabled(java.lang.String str, boolean z, int i);

    public abstract void setInteractive(boolean z);

    public abstract void setVirtualDeviceInputMethodForAllUsers(int i, java.lang.String str);

    public abstract boolean switchToInputMethod(java.lang.String str, int i);

    public abstract boolean transferTouchFocusToImeWindow(android.os.IBinder iBinder, int i, int i2);

    public abstract void unbindAccessibilityFromCurrentClient(int i, int i2);

    public abstract void updateImeWindowStatus(boolean z, int i);

    public static com.android.server.inputmethod.InputMethodManagerInternal get() {
        com.android.server.inputmethod.InputMethodManagerInternal instance = (com.android.server.inputmethod.InputMethodManagerInternal) com.android.server.LocalServices.getService(com.android.server.inputmethod.InputMethodManagerInternal.class);
        return instance != null ? instance : NOP;
    }
}
