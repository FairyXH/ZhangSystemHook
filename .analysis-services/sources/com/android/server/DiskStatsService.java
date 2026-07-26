package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class DiskStatsService extends android.os.Binder {
    private static final java.lang.String DISKSTATS_DUMP_FILE = "/data/system/diskstats_cache.json";
    private static final java.lang.String TAG = "DiskStatsService";
    private final android.content.Context mContext;

    public DiskStatsService(android.content.Context context) {
        this.mContext = context;
        com.android.server.storage.DiskStatsLoggingService.schedule(context);
    }

    @Override // android.os.Binder
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        java.io.PrintWriter pw2;
        android.util.proto.ProtoOutputStream proto;
        com.android.server.DiskStatsService diskStatsService;
        android.util.proto.ProtoOutputStream proto2;
        if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(this.mContext, TAG, pw)) {
            byte[] junk = new byte[512];
            for (int i = 0; i < junk.length; i++) {
                junk[i] = (byte) i;
            }
            java.io.File tmp = new java.io.File(android.os.Environment.getDataDirectory(), "system/perftest.tmp");
            java.io.FileOutputStream fos = null;
            java.io.IOException error = null;
            long before = android.os.SystemClock.uptimeMillis();
            try {
                fos = new java.io.FileOutputStream(tmp);
                fos.write(junk);
                try {
                    fos.close();
                } catch (java.io.IOException e) {
                }
            } catch (java.io.IOException e2) {
                error = e2;
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (java.io.IOException e3) {
                    }
                }
            } catch (java.lang.Throwable th) {
                if (fos != null) {
                    try {
                        fos.close();
                        throw th;
                    } catch (java.io.IOException e4) {
                        throw th;
                    }
                }
                throw th;
            }
            java.io.IOException error2 = error;
            long after = android.os.SystemClock.uptimeMillis();
            if (tmp.exists()) {
                tmp.delete();
            }
            boolean protoFormat = hasOption(args, "--proto");
            if (protoFormat) {
                android.util.proto.ProtoOutputStream proto3 = new android.util.proto.ProtoOutputStream(fd);
                proto3.write(1133871366145L, error2 != null);
                if (error2 != null) {
                    proto3.write(1138166333442L, error2.toString());
                } else {
                    proto3.write(1120986464259L, after - before);
                }
                pw2 = null;
                proto = proto3;
            } else {
                if (error2 != null) {
                    pw.print("Test-Error: ");
                    pw.println(error2.toString());
                } else {
                    pw.print("Latency: ");
                    pw.print(after - before);
                    pw.println("ms [512B Data Write]");
                }
                pw2 = pw;
                proto = null;
            }
            if (protoFormat) {
                diskStatsService = this;
                diskStatsService.reportDiskWriteSpeedProto(proto);
            } else {
                diskStatsService = this;
                diskStatsService.reportDiskWriteSpeed(pw2);
            }
            com.android.server.DiskStatsService diskStatsService2 = diskStatsService;
            java.io.PrintWriter printWriter = pw2;
            android.util.proto.ProtoOutputStream proto4 = proto;
            reportFreeSpace(android.os.Environment.getDataDirectory(), "Data", printWriter, proto, 0);
            reportFreeSpace(android.os.Environment.getDownloadCacheDirectory(), "Cache", printWriter, proto4, 1);
            reportFreeSpace(new java.io.File("/system"), "System", printWriter, proto4, 2);
            reportFreeSpace(android.os.Environment.getMetadataDirectory(), "Metadata", printWriter, proto4, 3);
            boolean fileBased = android.os.storage.StorageManager.isFileEncrypted();
            if (!protoFormat) {
                proto2 = proto4;
                if (fileBased) {
                    pw2.println("File-based Encryption: true");
                }
            } else if (fileBased) {
                proto2 = proto4;
                proto2.write(1159641169925L, 3);
            } else {
                proto2 = proto4;
                proto2.write(1159641169925L, 1);
            }
            if (protoFormat) {
                diskStatsService2.reportCachedValuesProto(proto2);
            } else {
                diskStatsService2.reportCachedValues(pw2);
            }
            if (protoFormat) {
                proto2.flush();
            }
        }
    }

    private void reportFreeSpace(java.io.File path, java.lang.String name, java.io.PrintWriter pw, android.util.proto.ProtoOutputStream proto, int folderType) {
        try {
            android.os.StatFs statfs = new android.os.StatFs(path.getPath());
            long bsize = statfs.getBlockSize();
            long avail = statfs.getAvailableBlocks();
            long total = statfs.getBlockCount();
            if (bsize <= 0 || total <= 0) {
                throw new java.lang.IllegalArgumentException("Invalid stat: bsize=" + bsize + " avail=" + avail + " total=" + total);
            }
            if (proto != null) {
                long freeSpaceToken = proto.start(2246267895812L);
                proto.write(1159641169921L, folderType);
                proto.write(1112396529666L, (avail * bsize) / 1024);
                proto.write(1112396529667L, (total * bsize) / 1024);
                proto.end(freeSpaceToken);
                return;
            }
            pw.print(name);
            pw.print("-Free: ");
            pw.print((avail * bsize) / 1024);
            pw.print("K / ");
            pw.print((total * bsize) / 1024);
            pw.print("K total = ");
            pw.print((100 * avail) / total);
            pw.println("% free");
        } catch (java.lang.IllegalArgumentException e) {
            if (proto == null) {
                pw.print(name);
                pw.print("-Error: ");
                pw.println(e.toString());
            }
        }
    }

    private boolean hasOption(java.lang.String[] args, java.lang.String arg) {
        for (java.lang.String opt : args) {
            if (arg.equals(opt)) {
                return true;
            }
        }
        return false;
    }

    private void reportCachedValues(java.io.PrintWriter pw) {
        try {
            java.lang.String jsonString = libcore.io.IoUtils.readFileAsString("/data/system/diskstats_cache.json");
            org.json.JSONObject json = new org.json.JSONObject(jsonString);
            pw.print("App Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.APP_SIZE_AGG_KEY));
            pw.print("App Data Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.APP_DATA_SIZE_AGG_KEY));
            pw.print("App Cache Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.APP_CACHE_AGG_KEY));
            pw.print("Photos Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.PHOTOS_KEY));
            pw.print("Videos Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.VIDEOS_KEY));
            pw.print("Audio Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.AUDIO_KEY));
            pw.print("Downloads Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.DOWNLOADS_KEY));
            pw.print("System Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.SYSTEM_KEY));
            pw.print("Other Size: ");
            pw.println(json.getLong(com.android.server.storage.DiskStatsFileLogger.MISC_KEY));
            pw.print("Package Names: ");
            pw.println(json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY));
            pw.print("App Sizes: ");
            pw.println(json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_SIZES_KEY));
            pw.print("App Data Sizes: ");
            pw.println(json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_DATA_KEY));
            pw.print("Cache Sizes: ");
            pw.println(json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_CACHES_KEY));
        } catch (java.io.IOException | org.json.JSONException e) {
            android.util.Log.w(TAG, "exception reading diskstats cache file", e);
        }
    }

    private void reportCachedValuesProto(android.util.proto.ProtoOutputStream proto) {
        long cachedValuesToken;
        try {
            java.lang.String jsonString = libcore.io.IoUtils.readFileAsString("/data/system/diskstats_cache.json");
            org.json.JSONObject json = new org.json.JSONObject(jsonString);
            long cachedValuesToken2 = proto.start(1146756268038L);
            proto.write(1112396529665L, json.getLong(com.android.server.storage.DiskStatsFileLogger.APP_SIZE_AGG_KEY));
            proto.write(1112396529674L, json.getLong(com.android.server.storage.DiskStatsFileLogger.APP_DATA_SIZE_AGG_KEY));
            proto.write(1112396529666L, json.getLong(com.android.server.storage.DiskStatsFileLogger.APP_CACHE_AGG_KEY));
            proto.write(1112396529667L, json.getLong(com.android.server.storage.DiskStatsFileLogger.PHOTOS_KEY));
            proto.write(1112396529668L, json.getLong(com.android.server.storage.DiskStatsFileLogger.VIDEOS_KEY));
            proto.write(1112396529669L, json.getLong(com.android.server.storage.DiskStatsFileLogger.AUDIO_KEY));
            proto.write(1112396529670L, json.getLong(com.android.server.storage.DiskStatsFileLogger.DOWNLOADS_KEY));
            proto.write(1112396529671L, json.getLong(com.android.server.storage.DiskStatsFileLogger.SYSTEM_KEY));
            proto.write(1112396529672L, json.getLong(com.android.server.storage.DiskStatsFileLogger.MISC_KEY));
            org.json.JSONArray packageNamesArray = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY);
            org.json.JSONArray appSizesArray = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_SIZES_KEY);
            org.json.JSONArray appDataSizesArray = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_DATA_KEY);
            org.json.JSONArray cacheSizesArray = json.getJSONArray(com.android.server.storage.DiskStatsFileLogger.APP_CACHES_KEY);
            int len = packageNamesArray.length();
            if (len == appSizesArray.length() && len == appDataSizesArray.length() && len == cacheSizesArray.length()) {
                int i = 0;
                while (i < len) {
                    long packageToken = proto.start(2246267895817L);
                    proto.write(1138166333441L, packageNamesArray.getString(i));
                    proto.write(1112396529666L, appSizesArray.getLong(i));
                    proto.write(1112396529668L, appDataSizesArray.getLong(i));
                    proto.write(1112396529667L, cacheSizesArray.getLong(i));
                    proto.end(packageToken);
                    i++;
                    packageNamesArray = packageNamesArray;
                    json = json;
                    cachedValuesToken2 = cachedValuesToken2;
                }
                cachedValuesToken = cachedValuesToken2;
                proto.end(cachedValuesToken);
            }
            cachedValuesToken = cachedValuesToken2;
            android.util.Slog.wtf(TAG, "Sizes of packageNamesArray, appSizesArray, appDataSizesArray  and cacheSizesArray are not the same");
            proto.end(cachedValuesToken);
        } catch (java.io.IOException | org.json.JSONException e) {
            android.util.Log.w(TAG, "exception reading diskstats cache file", e);
        }
    }

    private int getRecentPerf() throws java.lang.IllegalStateException, android.os.RemoteException {
        android.os.IBinder binder = android.os.ServiceManager.getService("storaged");
        if (binder == null) {
            throw new java.lang.IllegalStateException("storaged not found");
        }
        android.os.IStoraged storaged = android.os.IStoraged.Stub.asInterface(binder);
        return storaged.getRecentPerf();
    }

    private void reportDiskWriteSpeed(java.io.PrintWriter pw) {
        try {
            long perf = getRecentPerf();
            if (perf != 0) {
                pw.print("Recent Disk Write Speed (kB/s) = ");
                pw.println(perf);
            } else {
                pw.println("Recent Disk Write Speed data unavailable");
                android.util.Log.w(TAG, "Recent Disk Write Speed data unavailable!");
            }
        } catch (android.os.RemoteException | java.lang.IllegalStateException e) {
            pw.println(e.toString());
            android.util.Log.e(TAG, e.toString());
        }
    }

    private void reportDiskWriteSpeedProto(android.util.proto.ProtoOutputStream proto) {
        try {
            long perf = getRecentPerf();
            if (perf == 0) {
                android.util.Log.w(TAG, "Recent Disk Write Speed data unavailable!");
            } else {
                proto.write(1120986464263L, perf);
            }
        } catch (android.os.RemoteException | java.lang.IllegalStateException e) {
            android.util.Log.e(TAG, e.toString());
        }
    }
}
