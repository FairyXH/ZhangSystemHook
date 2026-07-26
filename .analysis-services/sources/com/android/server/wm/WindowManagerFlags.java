package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class WindowManagerFlags {
    final boolean mWallpaperOffsetAsync = com.android.window.flags.Flags.wallpaperOffsetAsync();
    final boolean mAllowsScreenSizeDecoupledFromStatusBarAndCutout = com.android.window.flags.Flags.allowsScreenSizeDecoupledFromStatusBarAndCutout();
    final boolean mInsetsDecoupledConfiguration = android.os.SystemProperties.getBoolean("persist.sys.switch.insetsconfig", true) & com.android.window.flags.Flags.insetsDecoupledConfiguration();

    WindowManagerFlags() {
    }
}
