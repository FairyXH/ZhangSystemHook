package com.android.server.textclassifier;

/* JADX INFO: loaded from: classes3.dex */
public final class IconsContentProvider extends android.content.ContentProvider {
    private static final java.lang.String MIME_TYPE = "image/png";
    private static final java.lang.String TAG = "IconsContentProvider";
    private final android.content.ContentProvider.PipeDataWriter<android.util.Pair<com.android.server.textclassifier.IconsUriHelper.ResourceInfo, java.lang.Integer>> mWriter = new android.content.ContentProvider.PipeDataWriter() { // from class: com.android.server.textclassifier.IconsContentProvider$$ExternalSyntheticLambda0
        @Override // android.content.ContentProvider.PipeDataWriter
        public final void writeDataToPipe(android.os.ParcelFileDescriptor parcelFileDescriptor, android.net.Uri uri, java.lang.String str, android.os.Bundle bundle, java.lang.Object obj) {
            this.f$0.lambda$new$0(parcelFileDescriptor, uri, str, bundle, (android.util.Pair) obj);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.os.ParcelFileDescriptor writeSide, android.net.Uri uri, java.lang.String mimeType, android.os.Bundle bundle, android.util.Pair args) {
        try {
            java.io.OutputStream out = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(writeSide);
            try {
                com.android.server.textclassifier.IconsUriHelper.ResourceInfo res = (com.android.server.textclassifier.IconsUriHelper.ResourceInfo) args.first;
                int userId = ((java.lang.Integer) args.second).intValue();
                android.graphics.drawable.Drawable drawable = android.graphics.drawable.Icon.createWithResource(res.packageName, res.id).loadDrawableAsUser(getContext(), userId);
                getBitmap(drawable).compress(android.graphics.Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Error retrieving icon for uri: " + uri, e);
        }
    }

    @Override // android.content.ContentProvider
    public android.os.ParcelFileDescriptor openFile(android.net.Uri uri, java.lang.String mode) {
        com.android.server.textclassifier.IconsUriHelper.ResourceInfo res = com.android.server.textclassifier.IconsUriHelper.getInstance().getResourceInfo(uri);
        if (res == null) {
            android.util.Log.e(TAG, "No icon found for uri: " + uri);
            return null;
        }
        try {
            android.util.Pair<com.android.server.textclassifier.IconsUriHelper.ResourceInfo, java.lang.Integer> args = new android.util.Pair<>(res, java.lang.Integer.valueOf(android.os.UserHandle.getCallingUserId()));
            return openPipeHelper(uri, MIME_TYPE, null, args, this.mWriter);
        } catch (java.io.IOException e) {
            android.util.Log.e(TAG, "Error opening pipe helper for icon at uri: " + uri, e);
            return null;
        }
    }

    private static android.graphics.Bitmap getBitmap(android.graphics.drawable.Drawable drawable) {
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            throw new java.lang.IllegalStateException("The icon is zero-sized");
        }
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean sameIcon(android.graphics.drawable.Drawable one, android.graphics.drawable.Drawable two) {
        java.io.ByteArrayOutputStream stream1 = new java.io.ByteArrayOutputStream();
        getBitmap(one).compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream1);
        java.io.ByteArrayOutputStream stream2 = new java.io.ByteArrayOutputStream();
        getBitmap(two).compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream2);
        return java.util.Arrays.equals(stream1.toByteArray(), stream2.toByteArray());
    }

    @Override // android.content.ContentProvider
    public java.lang.String getType(android.net.Uri uri) {
        return MIME_TYPE;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public android.database.Cursor query(android.net.Uri uri, java.lang.String[] projection, java.lang.String selection, java.lang.String[] selectionArgs, java.lang.String sortOrder) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int delete(android.net.Uri uri, java.lang.String selection, java.lang.String[] selectionArgs) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public android.net.Uri insert(android.net.Uri uri, android.content.ContentValues values) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(android.net.Uri uri, android.content.ContentValues values, java.lang.String selection, java.lang.String[] selectionArgs) {
        return 0;
    }
}
