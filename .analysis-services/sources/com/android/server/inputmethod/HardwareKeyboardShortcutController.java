package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class HardwareKeyboardShortcutController {
    private final java.util.ArrayList<com.android.internal.inputmethod.InputMethodSubtypeHandle> mSubtypeHandles = new java.util.ArrayList<>();
    private final int mUserId;

    int getUserId() {
        return this.mUserId;
    }

    HardwareKeyboardShortcutController(com.android.server.inputmethod.InputMethodMap methodMap, int userId) {
        this.mUserId = userId;
        reset(methodMap);
    }

    void reset(com.android.server.inputmethod.InputMethodMap methodMap) {
        this.mSubtypeHandles.clear();
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettings.create(methodMap, this.mUserId);
        java.util.List<android.view.inputmethod.InputMethodInfo> inputMethods = settings.getEnabledInputMethodList();
        for (int i = 0; i < inputMethods.size(); i++) {
            android.view.inputmethod.InputMethodInfo imi = inputMethods.get(i);
            if (imi.shouldShowInInputMethodPicker()) {
                java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes = settings.getEnabledInputMethodSubtypeList(imi, true);
                if (subtypes.isEmpty()) {
                    this.mSubtypeHandles.add(com.android.internal.inputmethod.InputMethodSubtypeHandle.of(imi, (android.view.inputmethod.InputMethodSubtype) null));
                } else {
                    for (android.view.inputmethod.InputMethodSubtype subtype : subtypes) {
                        if (subtype.isSuitableForPhysicalKeyboardLayoutMapping()) {
                            this.mSubtypeHandles.add(com.android.internal.inputmethod.InputMethodSubtypeHandle.of(imi, subtype));
                        }
                    }
                }
            }
        }
    }

    static <T> T getNeighborItem(java.util.List<T> list, T value, boolean next) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (java.util.Objects.equals(value, list.get(i))) {
                int nextIndex = (((next ? 1 : -1) + i) + size) % size;
                return list.get(nextIndex);
            }
        }
        return null;
    }

    com.android.internal.inputmethod.InputMethodSubtypeHandle onSubtypeSwitch(com.android.internal.inputmethod.InputMethodSubtypeHandle currentImeAndSubtype, boolean forward) {
        return (com.android.internal.inputmethod.InputMethodSubtypeHandle) getNeighborItem(this.mSubtypeHandles, currentImeAndSubtype, forward);
    }
}
