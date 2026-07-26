package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public interface IShortcutServiceWrapper {
    default com.android.server.pm.IShortcutServiceExt getExtImpl() {
        return new com.android.server.pm.IShortcutServiceExt() { // from class: com.android.server.pm.IShortcutServiceWrapper.1
        };
    }
}
