package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public abstract class ExtconUEventObserver extends android.os.UEventObserver {
    private static final boolean LOG = false;
    private static final java.lang.String SELINUX_POLICIES_NEED_TO_BE_CHANGED = "This probably means the selinux policies need to be changed.";
    private static final java.lang.String TAG = "ExtconUEventObserver";
    private final java.util.Map<java.lang.String, com.android.server.ExtconUEventObserver.ExtconInfo> mExtconInfos = new android.util.ArrayMap();

    protected abstract void onUEvent(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, android.os.UEventObserver.UEvent uEvent);

    public final void onUEvent(android.os.UEventObserver.UEvent event) {
        java.lang.String devPath = event.get("DEVPATH");
        com.android.server.ExtconUEventObserver.ExtconInfo info = this.mExtconInfos.get(devPath);
        if (info != null) {
            onUEvent(info, event);
        } else {
            android.util.Slog.w(TAG, "No match found for DEVPATH of " + event + " in " + this.mExtconInfos);
        }
    }

    public void startObserving(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo) {
        java.lang.String devicePath = extconInfo.getDevicePath();
        if (devicePath == null) {
            android.util.Slog.wtf(TAG, "Unable to start observing  " + extconInfo.getName() + " because the device path is null. " + SELINUX_POLICIES_NEED_TO_BE_CHANGED);
        } else {
            this.mExtconInfos.put(devicePath, extconInfo);
            startObserving("DEVPATH=" + devicePath);
        }
    }

    public static final class ExtconInfo {
        public static final java.lang.String EXTCON_CHARGE_DOWNSTREAM = "CHARGE-DOWNSTREAM";
        public static final java.lang.String EXTCON_DOCK = "DOCK";
        public static final java.lang.String EXTCON_DVI = "DVI";
        public static final java.lang.String EXTCON_FAST_CHARGER = "FAST-CHARGER";
        public static final java.lang.String EXTCON_HDMI = "HDMI";
        public static final java.lang.String EXTCON_HEADPHONE = "HEADPHONE";
        public static final java.lang.String EXTCON_JIG = "JIG";
        public static final java.lang.String EXTCON_LINE_IN = "LINE-IN";
        public static final java.lang.String EXTCON_LINE_OUT = "LINE-OUT";
        public static final java.lang.String EXTCON_MECHANICAL = "MECHANICAL";
        public static final java.lang.String EXTCON_MHL = "MHL";
        public static final java.lang.String EXTCON_MICROPHONE = "MICROPHONE";
        public static final java.lang.String EXTCON_SLOW_CHARGER = "SLOW-CHARGER";
        public static final java.lang.String EXTCON_SPDIF_IN = "SPDIF-IN";
        public static final java.lang.String EXTCON_SPDIF_OUT = "SPDIF-OUT";
        public static final java.lang.String EXTCON_TA = "TA";
        public static final java.lang.String EXTCON_USB = "USB";
        public static final java.lang.String EXTCON_USB_HOST = "USB-HOST";
        public static final java.lang.String EXTCON_VGA = "VGA";
        public static final java.lang.String EXTCON_VIDEO_IN = "VIDEO-IN";
        public static final java.lang.String EXTCON_VIDEO_OUT = "VIDEO-OUT";
        private final java.util.HashSet<java.lang.String> mDeviceTypes = new java.util.HashSet<>();
        private final java.lang.String mName;
        private static final java.lang.Object sLock = new java.lang.Object();
        private static com.android.server.ExtconUEventObserver.ExtconInfo[] sExtconInfos = null;

        public @interface ExtconDeviceType {
        }

        private static void initExtconInfos() {
            if (sExtconInfos != null) {
                return;
            }
            java.io.File file = new java.io.File("/sys/class/extcon");
            java.io.File[] files = file.listFiles();
            if (files == null) {
                android.util.Slog.w(com.android.server.ExtconUEventObserver.TAG, file + " exists " + file.exists() + " isDir " + file.isDirectory() + " but listFiles returns null." + com.android.server.ExtconUEventObserver.SELINUX_POLICIES_NEED_TO_BE_CHANGED);
                sExtconInfos = new com.android.server.ExtconUEventObserver.ExtconInfo[0];
                return;
            }
            java.util.List<com.android.server.ExtconUEventObserver.ExtconInfo> list = new java.util.ArrayList<>(files.length);
            for (java.io.File f : files) {
                list.add(new com.android.server.ExtconUEventObserver.ExtconInfo(f.getName()));
            }
            sExtconInfos = (com.android.server.ExtconUEventObserver.ExtconInfo[]) list.toArray(new com.android.server.ExtconUEventObserver.ExtconInfo[0]);
        }

        public static java.util.List<com.android.server.ExtconUEventObserver.ExtconInfo> getExtconInfoForTypes(java.lang.String[] extconTypes) {
            synchronized (sLock) {
                initExtconInfos();
            }
            java.util.List<com.android.server.ExtconUEventObserver.ExtconInfo> extcons = new java.util.ArrayList<>();
            for (com.android.server.ExtconUEventObserver.ExtconInfo extcon : sExtconInfos) {
                int length = extconTypes.length;
                int i = 0;
                while (true) {
                    if (i < length) {
                        java.lang.String type = extconTypes[i];
                        if (!extcon.hasCableType(type)) {
                            i++;
                        } else {
                            extcons.add(extcon);
                            break;
                        }
                    }
                }
            }
            return extcons;
        }

        public boolean hasCableType(java.lang.String type) {
            return this.mDeviceTypes.contains(type);
        }

        private ExtconInfo(java.lang.String extconName) {
            this.mName = extconName;
            java.io.File[] cableDirs = android.os.FileUtils.listFilesOrEmpty(new java.io.File("/sys/class/extcon", this.mName), new java.io.FilenameFilter() { // from class: com.android.server.ExtconUEventObserver$ExtconInfo$$ExternalSyntheticLambda0
                @Override // java.io.FilenameFilter
                public final boolean accept(java.io.File file, java.lang.String str) {
                    return str.startsWith("cable.");
                }
            });
            if (cableDirs.length == 0) {
                android.util.Slog.d(com.android.server.ExtconUEventObserver.TAG, "Unable to list cables in /sys/class/extcon/" + this.mName + ". " + com.android.server.ExtconUEventObserver.SELINUX_POLICIES_NEED_TO_BE_CHANGED);
            }
            for (java.io.File cableDir : cableDirs) {
                java.lang.String cableCanonicalPath = null;
                try {
                    cableCanonicalPath = cableDir.getCanonicalPath();
                    java.lang.String name = android.os.FileUtils.readTextFile(new java.io.File(cableDir, "name"), 0, null);
                    this.mDeviceTypes.add(name.replace("\n", "").replace("\r", ""));
                } catch (java.io.IOException ex) {
                    android.util.Slog.w(com.android.server.ExtconUEventObserver.TAG, "Unable to read " + cableCanonicalPath + "/name. " + com.android.server.ExtconUEventObserver.SELINUX_POLICIES_NEED_TO_BE_CHANGED, ex);
                }
            }
        }

        public java.lang.String getName() {
            return this.mName;
        }

        public java.lang.String getDevicePath() {
            try {
                java.lang.String extconPath = android.text.TextUtils.formatSimple("/sys/class/extcon/%s", new java.lang.Object[]{this.mName});
                java.io.File devPath = new java.io.File(extconPath);
                if (!devPath.exists()) {
                    return null;
                }
                java.lang.String canonicalPath = devPath.getCanonicalPath();
                int start = canonicalPath.indexOf("/devices");
                return canonicalPath.substring(start);
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.ExtconUEventObserver.TAG, "Could not get the extcon device path for " + this.mName, e);
                return null;
            }
        }

        public java.lang.String getStatePath() {
            return android.text.TextUtils.formatSimple("/sys/class/extcon/%s/state", new java.lang.Object[]{this.mName});
        }
    }

    public static boolean extconExists() {
        java.io.File extconDir = new java.io.File("/sys/class/extcon");
        return extconDir.exists() && extconDir.isDirectory();
    }
}
