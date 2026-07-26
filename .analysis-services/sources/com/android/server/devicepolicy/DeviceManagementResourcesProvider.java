package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class DeviceManagementResourcesProvider {
    private static final java.lang.String ATTR_DRAWABLE_ID = "drawable-id";
    private static final java.lang.String ATTR_DRAWABLE_SOURCE = "drawable-source";
    private static final java.lang.String ATTR_DRAWABLE_STYLE = "drawable-style";
    private static final java.lang.String ATTR_SOURCE_ID = "source-id";
    private static final java.lang.String TAG = "DevicePolicyManagerService";
    private static final java.lang.String TAG_DRAWABLE_SOURCE_ENTRY = "drawable-source-entry";
    private static final java.lang.String TAG_DRAWABLE_STYLE_ENTRY = "drawable-style-entry";
    private static final java.lang.String TAG_ROOT = "root";
    private static final java.lang.String TAG_STRING_ENTRY = "string-entry";
    private static final java.lang.String UPDATED_RESOURCES_XML = "updated_resources.xml";
    private final com.android.server.devicepolicy.DeviceManagementResourcesProvider.Injector mInjector;
    private final java.lang.Object mLock;
    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, android.app.admin.ParcelableResource>>> mUpdatedDrawablesForSource;
    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, android.app.admin.ParcelableResource>> mUpdatedDrawablesForStyle;
    private final java.util.Map<java.lang.String, android.app.admin.ParcelableResource> mUpdatedStrings;

    DeviceManagementResourcesProvider() {
        this(new com.android.server.devicepolicy.DeviceManagementResourcesProvider.Injector());
    }

    DeviceManagementResourcesProvider(com.android.server.devicepolicy.DeviceManagementResourcesProvider.Injector injector) {
        this.mUpdatedDrawablesForStyle = new java.util.HashMap();
        this.mUpdatedDrawablesForSource = new java.util.HashMap();
        this.mUpdatedStrings = new java.util.HashMap();
        this.mLock = new java.lang.Object();
        this.mInjector = (com.android.server.devicepolicy.DeviceManagementResourcesProvider.Injector) java.util.Objects.requireNonNull(injector);
    }

    boolean updateDrawables(java.util.List<android.app.admin.DevicePolicyDrawableResource> drawables) {
        boolean zUpdateDrawableForSource;
        boolean updated = false;
        for (int i = 0; i < drawables.size(); i++) {
            java.lang.String drawableId = drawables.get(i).getDrawableId();
            java.lang.String drawableStyle = drawables.get(i).getDrawableStyle();
            java.lang.String drawableSource = drawables.get(i).getDrawableSource();
            android.app.admin.ParcelableResource resource = drawables.get(i).getResource();
            java.util.Objects.requireNonNull(drawableId, "drawableId must be provided.");
            java.util.Objects.requireNonNull(drawableStyle, "drawableStyle must be provided.");
            java.util.Objects.requireNonNull(drawableSource, "drawableSource must be provided.");
            java.util.Objects.requireNonNull(resource, "ParcelableResource must be provided.");
            if ("UNDEFINED".equals(drawableSource)) {
                zUpdateDrawableForSource = updateDrawable(drawableId, drawableStyle, resource);
            } else {
                zUpdateDrawableForSource = updateDrawableForSource(drawableId, drawableSource, drawableStyle, resource);
            }
            updated |= zUpdateDrawableForSource;
        }
        if (!updated) {
            return false;
        }
        synchronized (this.mLock) {
            write();
        }
        return true;
    }

    private boolean updateDrawable(java.lang.String drawableId, java.lang.String drawableStyle, android.app.admin.ParcelableResource updatableResource) {
        synchronized (this.mLock) {
            if (!this.mUpdatedDrawablesForStyle.containsKey(drawableId)) {
                this.mUpdatedDrawablesForStyle.put(drawableId, new java.util.HashMap());
            }
            android.app.admin.ParcelableResource current = this.mUpdatedDrawablesForStyle.get(drawableId).get(drawableStyle);
            if (updatableResource.equals(current)) {
                return false;
            }
            this.mUpdatedDrawablesForStyle.get(drawableId).put(drawableStyle, updatableResource);
            return true;
        }
    }

    private boolean updateDrawableForSource(java.lang.String drawableId, java.lang.String drawableSource, java.lang.String drawableStyle, android.app.admin.ParcelableResource updatableResource) {
        synchronized (this.mLock) {
            if (!this.mUpdatedDrawablesForSource.containsKey(drawableId)) {
                this.mUpdatedDrawablesForSource.put(drawableId, new java.util.HashMap());
            }
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, android.app.admin.ParcelableResource>> drawablesForId = this.mUpdatedDrawablesForSource.get(drawableId);
            if (!drawablesForId.containsKey(drawableSource)) {
                this.mUpdatedDrawablesForSource.get(drawableId).put(drawableSource, new java.util.HashMap());
            }
            android.app.admin.ParcelableResource current = drawablesForId.get(drawableSource).get(drawableStyle);
            if (updatableResource.equals(current)) {
                return false;
            }
            drawablesForId.get(drawableSource).put(drawableStyle, updatableResource);
            return true;
        }
    }

    boolean removeDrawables(java.util.List<java.lang.String> drawableIds) {
        synchronized (this.mLock) {
            boolean removed = false;
            int i = 0;
            while (true) {
                boolean z = false;
                if (i >= drawableIds.size()) {
                    break;
                }
                java.lang.String drawableId = drawableIds.get(i);
                if (this.mUpdatedDrawablesForStyle.remove(drawableId) != null || this.mUpdatedDrawablesForSource.remove(drawableId) != null) {
                    z = true;
                }
                removed |= z;
                i++;
            }
            if (!removed) {
                return false;
            }
            write();
            return true;
        }
    }

    android.app.admin.ParcelableResource getDrawable(java.lang.String drawableId, java.lang.String drawableStyle, java.lang.String drawableSource) {
        synchronized (this.mLock) {
            android.app.admin.ParcelableResource resource = getDrawableForSourceLocked(drawableId, drawableStyle, drawableSource);
            if (resource != null) {
                return resource;
            }
            if (!this.mUpdatedDrawablesForStyle.containsKey(drawableId)) {
                return null;
            }
            return this.mUpdatedDrawablesForStyle.get(drawableId).get(drawableStyle);
        }
    }

    android.app.admin.ParcelableResource getDrawableForSourceLocked(java.lang.String drawableId, java.lang.String drawableStyle, java.lang.String drawableSource) {
        if (this.mUpdatedDrawablesForSource.containsKey(drawableId) && this.mUpdatedDrawablesForSource.get(drawableId).containsKey(drawableSource)) {
            return this.mUpdatedDrawablesForSource.get(drawableId).get(drawableSource).get(drawableStyle);
        }
        return null;
    }

    boolean updateStrings(java.util.List<android.app.admin.DevicePolicyStringResource> strings) {
        boolean updated = false;
        for (int i = 0; i < strings.size(); i++) {
            java.lang.String stringId = strings.get(i).getStringId();
            android.app.admin.ParcelableResource resource = strings.get(i).getResource();
            java.util.Objects.requireNonNull(stringId, "stringId must be provided.");
            java.util.Objects.requireNonNull(resource, "ParcelableResource must be provided.");
            updated |= updateString(stringId, resource);
        }
        if (!updated) {
            return false;
        }
        synchronized (this.mLock) {
            write();
        }
        return true;
    }

    private boolean updateString(java.lang.String stringId, android.app.admin.ParcelableResource updatableResource) {
        synchronized (this.mLock) {
            android.app.admin.ParcelableResource current = this.mUpdatedStrings.get(stringId);
            if (updatableResource.equals(current)) {
                return false;
            }
            this.mUpdatedStrings.put(stringId, updatableResource);
            return true;
        }
    }

    boolean removeStrings(java.util.List<java.lang.String> stringIds) {
        synchronized (this.mLock) {
            boolean removed = false;
            int i = 0;
            while (true) {
                boolean z = false;
                if (i >= stringIds.size()) {
                    break;
                }
                java.lang.String stringId = stringIds.get(i);
                if (this.mUpdatedStrings.remove(stringId) != null) {
                    z = true;
                }
                removed |= z;
                i++;
            }
            if (!removed) {
                return false;
            }
            write();
            return true;
        }
    }

    android.app.admin.ParcelableResource getString(java.lang.String stringId) {
        android.app.admin.ParcelableResource parcelableResource;
        synchronized (this.mLock) {
            parcelableResource = this.mUpdatedStrings.get(stringId);
        }
        return parcelableResource;
    }

    private void write() {
        android.util.Log.d(TAG, "Writing updated resources to file.");
        new com.android.server.devicepolicy.DeviceManagementResourcesProvider.ResourcesReaderWriter().writeToFileLocked();
    }

    void load() {
        synchronized (this.mLock) {
            new com.android.server.devicepolicy.DeviceManagementResourcesProvider.ResourcesReaderWriter().readFromFileLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.io.File getResourcesFile() {
        return new java.io.File(this.mInjector.environmentGetDataSystemDirectory(), UPDATED_RESOURCES_XML);
    }

    private class ResourcesReaderWriter {
        private final java.io.File mFile;

        private ResourcesReaderWriter() {
            this.mFile = com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.getResourcesFile();
        }

        void writeToFileLocked() {
            android.util.Log.d(com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG, "Writing to " + this.mFile);
            android.util.AtomicFile f = new android.util.AtomicFile(this.mFile);
            java.io.FileOutputStream outputStream = null;
            try {
                outputStream = f.startWrite();
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(outputStream);
                out.startDocument((java.lang.String) null, true);
                out.startTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_ROOT);
                writeInner(out);
                out.endTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_ROOT);
                out.endDocument();
                out.flush();
                f.finishWrite(outputStream);
            } catch (java.io.IOException e) {
                android.util.Log.e(com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG, "Exception when writing", e);
                if (outputStream != null) {
                    f.failWrite(outputStream);
                }
            }
        }

        void readFromFileLocked() {
            if (!this.mFile.exists()) {
                android.util.Log.d(com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG, "" + this.mFile + " doesn't exist");
                return;
            }
            android.util.Log.d(com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG, "Reading from " + this.mFile);
            android.util.AtomicFile f = new android.util.AtomicFile(this.mFile);
            java.io.InputStream input = null;
            try {
                try {
                    input = f.openRead();
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(input);
                    int depth = 0;
                    while (true) {
                        int type = parser.next();
                        if (type != 1) {
                            switch (type) {
                                case 2:
                                    depth++;
                                    java.lang.String tag = parser.getName();
                                    if (depth == 1) {
                                        if (!com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_ROOT.equals(tag)) {
                                            android.util.Log.e(com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG, "Invalid root tag: " + tag);
                                            return;
                                        }
                                    } else if (!readInner(parser, depth, tag)) {
                                        return;
                                    }
                                case 3:
                                    depth--;
                                    break;
                            }
                        }
                    }
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Log.e(com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG, "Error parsing resources file", e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(input);
            }
        }

        void writeInner(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            writeDrawablesForStylesInner(out);
            writeDrawablesForSourcesInner(out);
            writeStringsInner(out);
        }

        private void writeDrawablesForStylesInner(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            if (com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle != null && !com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle.isEmpty()) {
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, android.app.admin.ParcelableResource>> drawableEntry : com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedDrawablesForStyle.entrySet()) {
                    for (java.util.Map.Entry<java.lang.String, android.app.admin.ParcelableResource> styleEntry : drawableEntry.getValue().entrySet()) {
                        out.startTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_DRAWABLE_STYLE_ENTRY);
                        out.attribute((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.ATTR_DRAWABLE_ID, drawableEntry.getKey());
                        out.attribute((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.ATTR_DRAWABLE_STYLE, styleEntry.getKey());
                        styleEntry.getValue().writeToXmlFile(out);
                        out.endTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_DRAWABLE_STYLE_ENTRY);
                    }
                }
            }
        }

        private void writeDrawablesForSourcesInner(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            if (com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource != null && !com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.isEmpty()) {
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, android.app.admin.ParcelableResource>>> drawableEntry : com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedDrawablesForSource.entrySet()) {
                    for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, android.app.admin.ParcelableResource>> sourceEntry : drawableEntry.getValue().entrySet()) {
                        for (java.util.Map.Entry<java.lang.String, android.app.admin.ParcelableResource> styleEntry : sourceEntry.getValue().entrySet()) {
                            out.startTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_DRAWABLE_SOURCE_ENTRY);
                            out.attribute((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.ATTR_DRAWABLE_ID, drawableEntry.getKey());
                            out.attribute((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.ATTR_DRAWABLE_SOURCE, sourceEntry.getKey());
                            out.attribute((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.ATTR_DRAWABLE_STYLE, styleEntry.getKey());
                            styleEntry.getValue().writeToXmlFile(out);
                            out.endTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_DRAWABLE_SOURCE_ENTRY);
                        }
                    }
                }
            }
        }

        private void writeStringsInner(com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            if (com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedStrings != null && !com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedStrings.isEmpty()) {
                for (java.util.Map.Entry<java.lang.String, android.app.admin.ParcelableResource> entry : com.android.server.devicepolicy.DeviceManagementResourcesProvider.this.mUpdatedStrings.entrySet()) {
                    out.startTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_STRING_ENTRY);
                    out.attribute((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.ATTR_SOURCE_ID, entry.getKey());
                    entry.getValue().writeToXmlFile(out);
                    out.endTag((java.lang.String) null, com.android.server.devicepolicy.DeviceManagementResourcesProvider.TAG_STRING_ENTRY);
                }
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:17:0x002c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private boolean readInner(com.android.modules.utils.TypedXmlPullParser r8, int r9, java.lang.String r10) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                Method dump skipped, instruction units count: 284
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DeviceManagementResourcesProvider.ResourcesReaderWriter.readInner(com.android.modules.utils.TypedXmlPullParser, int, java.lang.String):boolean");
        }
    }

    public static class Injector {
        java.io.File environmentGetDataSystemDirectory() {
            return android.os.Environment.getDataSystemDirectory();
        }
    }
}
