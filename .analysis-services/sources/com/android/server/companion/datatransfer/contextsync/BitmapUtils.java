package com.android.server.companion.datatransfer.contextsync;

/* JADX INFO: loaded from: classes.dex */
public class BitmapUtils {
    private static final int APP_ICON_BITMAP_DIMENSION = 256;

    public static byte[] renderDrawableToByteArray(android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
            android.graphics.Bitmap bitmap = ((android.graphics.drawable.BitmapDrawable) drawable).getBitmap();
            if (bitmap.getWidth() > 256 || bitmap.getHeight() > 256) {
                android.graphics.Bitmap scaledBitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, 256, 256, true);
                byte[] renderedBitmap = renderBitmapToByteArray(scaledBitmap);
                scaledBitmap.recycle();
                return renderedBitmap;
            }
            return renderBitmapToByteArray(bitmap);
        }
        android.graphics.Bitmap bitmap2 = android.graphics.Bitmap.createBitmap(256, 256, android.graphics.Bitmap.Config.ARGB_8888);
        try {
            android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap2);
            drawable.setBounds(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
            drawable.draw(canvas);
            return renderBitmapToByteArray(bitmap2);
        } finally {
            bitmap2.recycle();
        }
    }

    private static byte[] renderBitmapToByteArray(android.graphics.Bitmap bitmap) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
