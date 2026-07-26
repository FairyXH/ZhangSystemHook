package com.android.server.graphics.fonts;

/* JADX INFO: loaded from: classes2.dex */
class OtfFontFileParser implements com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser {
    OtfFontFileParser() {
    }

    @Override // com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser
    public java.lang.String getPostScriptName(java.io.File file) throws java.io.IOException {
        java.nio.ByteBuffer buffer = mmap(file);
        try {
            return android.graphics.fonts.FontFileUtil.getPostScriptName(buffer, 0);
        } finally {
            unmap(buffer);
        }
    }

    @Override // com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser
    public java.lang.String buildFontFileName(java.io.File file) throws java.io.IOException {
        java.lang.String extension;
        java.nio.ByteBuffer buffer = mmap(file);
        try {
            java.lang.String psName = android.graphics.fonts.FontFileUtil.getPostScriptName(buffer, 0);
            int isType1Font = android.graphics.fonts.FontFileUtil.isPostScriptType1Font(buffer, 0);
            int isCollection = android.graphics.fonts.FontFileUtil.isCollectionFont(buffer);
            if (!android.text.TextUtils.isEmpty(psName) && isType1Font != -1 && isCollection != -1) {
                if (isCollection == 1) {
                    extension = isType1Font == 1 ? ".otc" : ".ttc";
                } else {
                    extension = isType1Font == 1 ? ".otf" : ".ttf";
                }
                return psName + extension;
            }
            unmap(buffer);
            return null;
        } finally {
            unmap(buffer);
        }
    }

    @Override // com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser
    public long getRevision(java.io.File file) throws java.io.IOException {
        java.nio.ByteBuffer buffer = mmap(file);
        try {
            return android.graphics.fonts.FontFileUtil.getRevision(buffer, 0);
        } finally {
            unmap(buffer);
        }
    }

    @Override // com.android.server.graphics.fonts.UpdatableFontDir.FontFileParser
    public void tryToCreateTypeface(java.io.File file) throws java.lang.Throwable {
        java.nio.ByteBuffer buffer = mmap(file);
        try {
            android.graphics.fonts.Font font = new android.graphics.fonts.Font.Builder(buffer).build();
            android.graphics.fonts.FontFamily family = new android.graphics.fonts.FontFamily.Builder(font).build();
            android.graphics.Typeface typeface = new android.graphics.Typeface.CustomFallbackBuilder(family).build();
            android.text.TextPaint p = new android.text.TextPaint();
            p.setTextSize(24.0f);
            p.setTypeface(typeface);
            int width = (int) java.lang.Math.ceil(android.text.Layout.getDesiredWidth("abcXYZ@- 🫖🇺🇸💏🏻👨🏼\u200d❤️\u200d💋\u200d👨🏿", p));
            android.text.StaticLayout layout = android.text.StaticLayout.Builder.obtain("abcXYZ@- 🫖🇺🇸💏🏻👨🏼\u200d❤️\u200d💋\u200d👨🏿", 0, "abcXYZ@- 🫖🇺🇸💏🏻👨🏼\u200d❤️\u200d💋\u200d👨🏿".length(), p, width).build();
            android.graphics.Bitmap bmp = android.graphics.Bitmap.createBitmap(layout.getWidth(), layout.getHeight(), android.graphics.Bitmap.Config.ALPHA_8);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bmp);
            layout.draw(canvas);
        } finally {
            unmap(buffer);
        }
    }

    private static java.nio.ByteBuffer mmap(java.io.File file) throws java.io.IOException {
        java.io.FileInputStream in = new java.io.FileInputStream(file);
        try {
            java.nio.channels.FileChannel fileChannel = in.getChannel();
            java.nio.MappedByteBuffer map = fileChannel.map(java.nio.channels.FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size());
            in.close();
            return map;
        } catch (java.lang.Throwable th) {
            try {
                in.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static void unmap(java.nio.ByteBuffer buffer) {
        if (buffer instanceof java.nio.DirectByteBuffer) {
            java.nio.NioUtils.freeDirectBuffer(buffer);
        }
    }
}
