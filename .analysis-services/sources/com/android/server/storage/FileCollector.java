package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public class FileCollector {
    private static final int AUDIO = 2;
    private static final java.util.Map<java.lang.String, java.lang.Integer> EXTENSION_MAP = new android.util.ArrayMap();
    private static final int IMAGES = 0;
    private static final int UNRECOGNIZED = -1;
    private static final int VIDEO = 1;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface FileTypes {
    }

    static {
        EXTENSION_MAP.put("aac", 2);
        EXTENSION_MAP.put("amr", 2);
        EXTENSION_MAP.put("awb", 2);
        EXTENSION_MAP.put("snd", 2);
        EXTENSION_MAP.put("flac", 2);
        EXTENSION_MAP.put("mp3", 2);
        EXTENSION_MAP.put("mpga", 2);
        EXTENSION_MAP.put("mpega", 2);
        EXTENSION_MAP.put("mp2", 2);
        EXTENSION_MAP.put("m4a", 2);
        EXTENSION_MAP.put("aif", 2);
        EXTENSION_MAP.put("aiff", 2);
        EXTENSION_MAP.put("aifc", 2);
        EXTENSION_MAP.put("gsm", 2);
        EXTENSION_MAP.put("mka", 2);
        EXTENSION_MAP.put("m3u", 2);
        EXTENSION_MAP.put("wma", 2);
        EXTENSION_MAP.put("wax", 2);
        EXTENSION_MAP.put("ra", 2);
        EXTENSION_MAP.put("rm", 2);
        EXTENSION_MAP.put("ram", 2);
        EXTENSION_MAP.put("pls", 2);
        EXTENSION_MAP.put("sd2", 2);
        EXTENSION_MAP.put("wav", 2);
        EXTENSION_MAP.put("ogg", 2);
        EXTENSION_MAP.put("oga", 2);
        EXTENSION_MAP.put("3gpp", 1);
        EXTENSION_MAP.put("3gp", 1);
        EXTENSION_MAP.put("3gpp2", 1);
        EXTENSION_MAP.put("3g2", 1);
        EXTENSION_MAP.put("avi", 1);
        EXTENSION_MAP.put("dl", 1);
        EXTENSION_MAP.put("dif", 1);
        EXTENSION_MAP.put("dv", 1);
        EXTENSION_MAP.put("fli", 1);
        EXTENSION_MAP.put("m4v", 1);
        EXTENSION_MAP.put("ts", 1);
        EXTENSION_MAP.put("mpeg", 1);
        EXTENSION_MAP.put("mpg", 1);
        EXTENSION_MAP.put("mpe", 1);
        EXTENSION_MAP.put("mp4", 1);
        EXTENSION_MAP.put("vob", 1);
        EXTENSION_MAP.put("qt", 1);
        EXTENSION_MAP.put("mov", 1);
        EXTENSION_MAP.put("mxu", 1);
        EXTENSION_MAP.put("webm", 1);
        EXTENSION_MAP.put("lsf", 1);
        EXTENSION_MAP.put("lsx", 1);
        EXTENSION_MAP.put("mkv", 1);
        EXTENSION_MAP.put("mng", 1);
        EXTENSION_MAP.put("asf", 1);
        EXTENSION_MAP.put("asx", 1);
        EXTENSION_MAP.put("wm", 1);
        EXTENSION_MAP.put("wmv", 1);
        EXTENSION_MAP.put("wmx", 1);
        EXTENSION_MAP.put("wvx", 1);
        EXTENSION_MAP.put("movie", 1);
        EXTENSION_MAP.put("wrf", 1);
        EXTENSION_MAP.put("bmp", 0);
        EXTENSION_MAP.put("gif", 0);
        EXTENSION_MAP.put("jpg", 0);
        EXTENSION_MAP.put("jpeg", 0);
        EXTENSION_MAP.put("jpe", 0);
        EXTENSION_MAP.put("pcx", 0);
        EXTENSION_MAP.put("png", 0);
        EXTENSION_MAP.put("svg", 0);
        EXTENSION_MAP.put("svgz", 0);
        EXTENSION_MAP.put("tiff", 0);
        EXTENSION_MAP.put("tif", 0);
        EXTENSION_MAP.put("wbmp", 0);
        EXTENSION_MAP.put("webp", 0);
        EXTENSION_MAP.put("dng", 0);
        EXTENSION_MAP.put("cr2", 0);
        EXTENSION_MAP.put("ras", 0);
        EXTENSION_MAP.put("art", 0);
        EXTENSION_MAP.put("jng", 0);
        EXTENSION_MAP.put("nef", 0);
        EXTENSION_MAP.put("nrw", 0);
        EXTENSION_MAP.put("orf", 0);
        EXTENSION_MAP.put("rw2", 0);
        EXTENSION_MAP.put("pef", 0);
        EXTENSION_MAP.put("psd", 0);
        EXTENSION_MAP.put("pnm", 0);
        EXTENSION_MAP.put("pbm", 0);
        EXTENSION_MAP.put("pgm", 0);
        EXTENSION_MAP.put("ppm", 0);
        EXTENSION_MAP.put("srw", 0);
        EXTENSION_MAP.put("arw", 0);
        EXTENSION_MAP.put("rgb", 0);
        EXTENSION_MAP.put("xbm", 0);
        EXTENSION_MAP.put("xpm", 0);
        EXTENSION_MAP.put("xwd", 0);
    }

    public static com.android.server.storage.FileCollector.MeasurementResult getMeasurementResult(java.io.File path) {
        return collectFiles(android.os.storage.StorageManager.maybeTranslateEmulatedPathToInternal(path), new com.android.server.storage.FileCollector.MeasurementResult());
    }

    public static com.android.server.storage.FileCollector.MeasurementResult getMeasurementResult(android.content.Context context) {
        com.android.server.storage.FileCollector.MeasurementResult result = new com.android.server.storage.FileCollector.MeasurementResult();
        android.app.usage.StorageStatsManager ssm = (android.app.usage.StorageStatsManager) context.getSystemService("storagestats");
        try {
            android.app.usage.ExternalStorageStats stats = ssm.queryExternalStatsForUser(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, android.os.UserHandle.of(context.getUserId()));
            result.imagesSize = stats.getImageBytes();
            result.videosSize = stats.getVideoBytes();
            result.audioSize = stats.getAudioBytes();
            result.miscSize = ((stats.getTotalBytes() - result.imagesSize) - result.videosSize) - result.audioSize;
            return result;
        } catch (java.io.IOException e) {
            throw new java.lang.IllegalStateException("Could not query storage");
        }
    }

    public static long getSystemSize(android.content.Context context) {
        java.io.File sharedPath;
        android.content.pm.PackageManager pm = context.getPackageManager();
        android.os.storage.VolumeInfo primaryVolume = pm.getPrimaryStorageCurrentVolume();
        android.os.storage.StorageManager sm = (android.os.storage.StorageManager) context.getSystemService("storage");
        android.os.storage.VolumeInfo shared = sm.findEmulatedForPrivate(primaryVolume);
        if (shared == null || (sharedPath = shared.getPath()) == null) {
            return 0L;
        }
        long sharedDataSize = sharedPath.getTotalSpace();
        long systemSize = sm.getPrimaryStorageSize() - sharedDataSize;
        if (systemSize <= 0) {
            return 0L;
        }
        return systemSize;
    }

    private static com.android.server.storage.FileCollector.MeasurementResult collectFiles(java.io.File file, com.android.server.storage.FileCollector.MeasurementResult result) {
        java.io.File[] files = file.listFiles();
        if (files == null) {
            return result;
        }
        for (java.io.File f : files) {
            if (f.isDirectory()) {
                try {
                    collectFiles(f, result);
                } catch (java.lang.StackOverflowError e) {
                    return result;
                }
            } else {
                handleFile(result, f);
            }
        }
        return result;
    }

    private static void handleFile(com.android.server.storage.FileCollector.MeasurementResult result, java.io.File f) {
        long fileSize = f.length();
        int fileType = EXTENSION_MAP.getOrDefault(getExtensionForFile(f), -1).intValue();
        switch (fileType) {
            case 0:
                result.imagesSize += fileSize;
                break;
            case 1:
                result.videosSize += fileSize;
                break;
            case 2:
                result.audioSize += fileSize;
                break;
            default:
                result.miscSize += fileSize;
                break;
        }
    }

    private static java.lang.String getExtensionForFile(java.io.File file) {
        java.lang.String fileName = file.getName();
        int index = fileName.lastIndexOf(46);
        if (index == -1) {
            return "";
        }
        return fileName.substring(index + 1).toLowerCase();
    }

    public static class MeasurementResult {
        public long audioSize;
        public long imagesSize;
        public long miscSize;
        public long videosSize;

        public long totalAccountedSize() {
            return this.imagesSize + this.videosSize + this.miscSize + this.audioSize;
        }
    }
}
