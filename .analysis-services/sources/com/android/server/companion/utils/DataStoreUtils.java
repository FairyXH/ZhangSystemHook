package com.android.server.companion.utils;

/* JADX INFO: loaded from: classes.dex */
public final class DataStoreUtils {
    private static final java.lang.String TAG = "CDM_DataStoreUtils";

    public static boolean isStartOfTag(org.xmlpull.v1.XmlPullParser parser, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException {
        return parser.getEventType() == 2 && tag.equals(parser.getName());
    }

    public static boolean isEndOfTag(org.xmlpull.v1.XmlPullParser parser, java.lang.String tag) throws org.xmlpull.v1.XmlPullParserException {
        return parser.getEventType() == 3 && tag.equals(parser.getName());
    }

    public static android.util.AtomicFile createStorageFileForUser(int userId, java.lang.String fileName) {
        return new android.util.AtomicFile(getBaseStorageFileForUser(userId, fileName));
    }

    private static java.io.File getBaseStorageFileForUser(int userId, java.lang.String fileName) {
        return new java.io.File(android.os.Environment.getDataSystemDeDirectory(userId), fileName);
    }

    public static void writeToFileSafely(android.util.AtomicFile file, com.android.internal.util.FunctionalUtils.ThrowingConsumer<java.io.FileOutputStream> consumer) {
        try {
            file.write(consumer);
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error while writing to file " + file, e);
        }
    }

    public static byte[] fileToByteArray(android.util.AtomicFile file) {
        java.io.FileInputStream in;
        java.io.ByteArrayOutputStream bytes;
        byte[] buffer;
        if (!file.getBaseFile().exists()) {
            android.util.Slog.d(TAG, "File does not exist");
            return new byte[0];
        }
        try {
            in = file.openRead();
            try {
                bytes = new java.io.ByteArrayOutputStream();
                buffer = new byte[1024];
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error while reading requests file", e);
            return new byte[0];
        }
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            bytes.write(buffer, 0, read);
            android.util.Slog.e(TAG, "Error while reading requests file", e);
            return new byte[0];
        }
        byte[] byteArray = bytes.toByteArray();
        if (in != null) {
            in.close();
        }
        return byteArray;
    }

    private DataStoreUtils() {
    }
}
