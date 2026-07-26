package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class Helper {
    private static final java.lang.String TAG = "AutofillHelper";
    public static boolean sDebug = false;
    public static boolean sVerbose = false;
    public static java.lang.Boolean sFullScreenMode = null;

    /* JADX INFO: Access modifiers changed from: private */
    interface ViewNodeFilter {
        boolean matches(android.app.assist.AssistStructure.ViewNode viewNode);
    }

    private Helper() {
        throw new java.lang.UnsupportedOperationException("contains static members only");
    }

    private static boolean checkRemoteViewUriPermissions(final int userId, android.widget.RemoteViews rView) {
        final java.util.concurrent.atomic.AtomicBoolean permissionsOk = new java.util.concurrent.atomic.AtomicBoolean(true);
        rView.visitUris(new java.util.function.Consumer() { // from class: com.android.server.autofill.Helper$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.autofill.Helper.lambda$checkRemoteViewUriPermissions$0(userId, permissionsOk, (android.net.Uri) obj);
            }
        });
        return permissionsOk.get();
    }

    static /* synthetic */ void lambda$checkRemoteViewUriPermissions$0(int userId, java.util.concurrent.atomic.AtomicBoolean permissionsOk, android.net.Uri uri) {
        int uriOwnerId = android.content.ContentProvider.getUserIdFromUri(uri, userId);
        boolean allowed = uriOwnerId == userId;
        permissionsOk.set(permissionsOk.get() & allowed);
    }

    public static android.widget.RemoteViews sanitizeRemoteView(android.widget.RemoteViews rView) {
        if (rView == null) {
            return null;
        }
        int userId = android.app.ActivityManager.getCurrentUser();
        boolean ok = checkRemoteViewUriPermissions(userId, rView);
        if (!ok) {
            android.util.Slog.w(TAG, "sanitizeRemoteView() user: " + userId + " tried accessing resource that does not belong to them");
        }
        if (ok) {
            return rView;
        }
        return null;
    }

    public static android.app.slice.Slice sanitizeSlice(android.app.slice.Slice slice) {
        if (slice == null) {
            return null;
        }
        int userId = android.app.ActivityManager.getCurrentUser();
        for (android.app.slice.SliceItem sliceItem : slice.getItems()) {
            if (sliceItem.getFormat().equals("image")) {
                android.graphics.drawable.Icon icon = sliceItem.getIcon();
                if (icon.getType() == 4 || icon.getType() == 6) {
                    int iconUriId = android.content.ContentProvider.getUserIdFromUri(icon.getUri(), userId);
                    if (iconUriId != userId) {
                        android.util.Slog.w(TAG, "sanitizeSlice() user: " + userId + " cannot access icons in Slice");
                        return null;
                    }
                }
            }
        }
        return slice;
    }

    static android.view.autofill.AutofillId[] toArray(android.util.ArraySet<android.view.autofill.AutofillId> set) {
        if (set == null) {
            return null;
        }
        android.view.autofill.AutofillId[] array = new android.view.autofill.AutofillId[set.size()];
        for (int i = 0; i < set.size(); i++) {
            array[i] = set.valueAt(i);
        }
        return array;
    }

    public static java.lang.String paramsToString(android.view.WindowManager.LayoutParams params) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(25);
        params.dumpDimensions(builder);
        return builder.toString();
    }

    static android.util.ArrayMap<android.view.autofill.AutofillId, android.view.autofill.AutofillValue> getFields(android.service.autofill.Dataset dataset) {
        java.util.ArrayList<android.view.autofill.AutofillId> ids = dataset.getFieldIds();
        java.util.ArrayList<android.view.autofill.AutofillValue> values = dataset.getFieldValues();
        int size = ids == null ? 0 : ids.size();
        android.util.ArrayMap<android.view.autofill.AutofillId, android.view.autofill.AutofillValue> fields = new android.util.ArrayMap<>(size);
        for (int i = 0; i < size; i++) {
            fields.put(ids.get(i), values.get(i));
        }
        return fields;
    }

    private static android.metrics.LogMaker newLogMaker(int category, java.lang.String servicePackageName, int sessionId, boolean compatMode) {
        android.metrics.LogMaker log = new android.metrics.LogMaker(category).addTaggedData(908, servicePackageName).addTaggedData(1456, java.lang.Integer.toString(sessionId));
        if (compatMode) {
            log.addTaggedData(1414, 1);
        }
        return log;
    }

    public static android.metrics.LogMaker newLogMaker(int category, java.lang.String packageName, java.lang.String servicePackageName, int sessionId, boolean compatMode) {
        return newLogMaker(category, servicePackageName, sessionId, compatMode).setPackageName(packageName);
    }

    public static android.metrics.LogMaker newLogMaker(int category, android.content.ComponentName componentName, java.lang.String servicePackageName, int sessionId, boolean compatMode) {
        android.content.ComponentName sanitizedComponentName = new android.content.ComponentName(componentName.getPackageName(), "");
        return newLogMaker(category, servicePackageName, sessionId, compatMode).setComponentName(sanitizedComponentName);
    }

    public static void printlnRedactedText(java.io.PrintWriter pw, java.lang.CharSequence text) {
        if (text == null) {
            pw.println("null");
        } else {
            pw.print(text.length());
            pw.println("_chars");
        }
    }

    public static android.app.assist.AssistStructure.ViewNode findViewNodeByAutofillId(android.app.assist.AssistStructure structure, final android.view.autofill.AutofillId autofillId) {
        return findViewNode(structure, new com.android.server.autofill.Helper.ViewNodeFilter() { // from class: com.android.server.autofill.Helper$$ExternalSyntheticLambda0
            @Override // com.android.server.autofill.Helper.ViewNodeFilter
            public final boolean matches(android.app.assist.AssistStructure.ViewNode viewNode) {
                return autofillId.equals(viewNode.getAutofillId());
            }
        });
    }

    private static android.app.assist.AssistStructure.ViewNode findViewNode(android.app.assist.AssistStructure structure, com.android.server.autofill.Helper.ViewNodeFilter filter) {
        java.util.ArrayDeque<android.app.assist.AssistStructure.ViewNode> nodesToProcess = new java.util.ArrayDeque<>();
        int numWindowNodes = structure.getWindowNodeCount();
        for (int i = 0; i < numWindowNodes; i++) {
            nodesToProcess.add(structure.getWindowNodeAt(i).getRootViewNode());
        }
        while (!nodesToProcess.isEmpty()) {
            android.app.assist.AssistStructure.ViewNode node = nodesToProcess.removeFirst();
            if (filter.matches(node)) {
                return node;
            }
            for (int i2 = 0; i2 < node.getChildCount(); i2++) {
                nodesToProcess.addLast(node.getChildAt(i2));
            }
        }
        return null;
    }

    public static android.app.assist.AssistStructure.ViewNode sanitizeUrlBar(android.app.assist.AssistStructure structure, final java.lang.String[] urlBarIds) {
        android.app.assist.AssistStructure.ViewNode urlBarNode = findViewNode(structure, new com.android.server.autofill.Helper.ViewNodeFilter() { // from class: com.android.server.autofill.Helper$$ExternalSyntheticLambda2
            @Override // com.android.server.autofill.Helper.ViewNodeFilter
            public final boolean matches(android.app.assist.AssistStructure.ViewNode viewNode) {
                return com.android.internal.util.ArrayUtils.contains(urlBarIds, viewNode.getIdEntry());
            }
        });
        if (urlBarNode != null) {
            java.lang.String domain = urlBarNode.getText().toString();
            if (domain.isEmpty()) {
                if (sDebug) {
                    android.util.Slog.d(TAG, "sanitizeUrlBar(): empty on " + urlBarNode.getIdEntry());
                    return null;
                }
                return null;
            }
            urlBarNode.setWebDomain(domain);
            if (sDebug) {
                android.util.Slog.d(TAG, "sanitizeUrlBar(): id=" + urlBarNode.getIdEntry() + ", domain=" + urlBarNode.getWebDomain());
            }
        }
        return urlBarNode;
    }

    static int getNumericValue(android.metrics.LogMaker log, int tag) {
        java.lang.Object value = log.getTaggedData(tag);
        if (!(value instanceof java.lang.Number)) {
            return 0;
        }
        return ((java.lang.Number) value).intValue();
    }

    static java.util.ArrayList<android.view.autofill.AutofillId> getAutofillIds(android.app.assist.AssistStructure structure, boolean autofillableOnly) {
        java.util.ArrayList<android.view.autofill.AutofillId> ids = new java.util.ArrayList<>();
        int size = structure.getWindowNodeCount();
        for (int i = 0; i < size; i++) {
            android.app.assist.AssistStructure.WindowNode node = structure.getWindowNodeAt(i);
            addAutofillableIds(node.getRootViewNode(), ids, autofillableOnly);
        }
        return ids;
    }

    private static void addAutofillableIds(android.app.assist.AssistStructure.ViewNode node, java.util.ArrayList<android.view.autofill.AutofillId> ids, boolean autofillableOnly) {
        if (!autofillableOnly || node.getAutofillType() != 0) {
            ids.add(node.getAutofillId());
        }
        int size = node.getChildCount();
        for (int i = 0; i < size; i++) {
            android.app.assist.AssistStructure.ViewNode child = node.getChildAt(i);
            addAutofillableIds(child, ids, autofillableOnly);
        }
    }

    static android.util.ArrayMap<android.view.autofill.AutofillId, android.service.autofill.InternalSanitizer> createSanitizers(android.service.autofill.SaveInfo saveInfo) {
        android.service.autofill.InternalSanitizer[] sanitizerKeys;
        if (saveInfo == null || (sanitizerKeys = saveInfo.getSanitizerKeys()) == null) {
            return null;
        }
        int size = sanitizerKeys.length;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.service.autofill.InternalSanitizer> sanitizers = new android.util.ArrayMap<>(size);
        if (sDebug) {
            android.util.Slog.d(TAG, "Service provided " + size + " sanitizers");
        }
        android.view.autofill.AutofillId[][] sanitizerValues = saveInfo.getSanitizerValues();
        for (int i = 0; i < size; i++) {
            android.service.autofill.InternalSanitizer sanitizer = sanitizerKeys[i];
            android.view.autofill.AutofillId[] ids = sanitizerValues[i];
            if (sDebug) {
                android.util.Slog.d(TAG, "sanitizer #" + i + " (" + sanitizer + ") for ids " + java.util.Arrays.toString(ids));
            }
            for (android.view.autofill.AutofillId id : ids) {
                sanitizers.put(id, sanitizer);
            }
        }
        return sanitizers;
    }

    static boolean containsCharsInOrder(java.lang.String s1, java.lang.String s2) {
        int prevIndex = -1;
        for (char ch : s2.toCharArray()) {
            int index = android.text.TextUtils.indexOf(s1, ch, prevIndex + 1);
            if (index == -1) {
                return false;
            }
            prevIndex = index;
        }
        return true;
    }

    static android.content.Context getDisplayContext(android.content.Context context, int displayId) {
        if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
            return context;
        }
        if (context.getDisplayId() == displayId) {
            if (sDebug) {
                com.android.server.utils.Slogf.d(TAG, "getDisplayContext(): context %s already has displayId %d", context, java.lang.Integer.valueOf(displayId));
            }
            return context;
        }
        if (sDebug) {
            com.android.server.utils.Slogf.d(TAG, "Creating context for display %d", java.lang.Integer.valueOf(displayId));
        }
        android.view.Display display = ((android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class)).getDisplay(displayId);
        if (display == null) {
            com.android.server.utils.Slogf.wtf(TAG, "Could not get context with displayId %d, Autofill operations will probably fail)", java.lang.Integer.valueOf(displayId));
            return context;
        }
        return context.createDisplayContext(display);
    }

    static <T> T weakDeref(java.lang.ref.WeakReference<T> weakRef, java.lang.String tag, java.lang.String prefix) {
        T deref = weakRef.get();
        if (deref == null) {
            android.util.Slog.wtf(tag, prefix + "fail to deref " + weakRef);
        }
        return deref;
    }
}
