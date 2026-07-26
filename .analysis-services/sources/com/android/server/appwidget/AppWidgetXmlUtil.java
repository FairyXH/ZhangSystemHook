package com.android.server.appwidget;

/* JADX INFO: loaded from: classes.dex */
public class AppWidgetXmlUtil {
    private static final java.lang.String ATTR_AUTO_ADVANCED_VIEW_ID = "auto_advance_view_id";
    private static final java.lang.String ATTR_CONFIGURE = "configure";
    private static final java.lang.String ATTR_DESCRIPTION_RES = "description_res";
    private static final java.lang.String ATTR_ICON = "icon";
    private static final java.lang.String ATTR_INITIAL_KEYGUARD_LAYOUT = "initial_keyguard_layout";
    private static final java.lang.String ATTR_INITIAL_LAYOUT = "initial_layout";
    private static final java.lang.String ATTR_LABEL = "label";
    private static final java.lang.String ATTR_MAX_RESIZE_HEIGHT = "max_resize_height";
    private static final java.lang.String ATTR_MAX_RESIZE_WIDTH = "max_resize_width";
    private static final java.lang.String ATTR_MIN_HEIGHT = "min_height";
    private static final java.lang.String ATTR_MIN_RESIZE_HEIGHT = "min_resize_height";
    private static final java.lang.String ATTR_MIN_RESIZE_WIDTH = "min_resize_width";
    private static final java.lang.String ATTR_MIN_WIDTH = "min_width";
    private static final java.lang.String ATTR_OS_FINGERPRINT = "os_fingerprint";
    private static final java.lang.String ATTR_PREVIEW_IMAGE = "preview_image";
    private static final java.lang.String ATTR_PREVIEW_LAYOUT = "preview_layout";
    private static final java.lang.String ATTR_PROVIDER_INHERITANCE = "provider_inheritance";
    private static final java.lang.String ATTR_RESIZE_MODE = "resize_mode";
    private static final java.lang.String ATTR_TARGET_CELL_HEIGHT = "target_cell_height";
    private static final java.lang.String ATTR_TARGET_CELL_WIDTH = "target_cell_width";
    private static final java.lang.String ATTR_UPDATE_PERIOD_MILLIS = "update_period_millis";
    private static final java.lang.String ATTR_WIDGET_CATEGORY = "widget_category";
    private static final java.lang.String ATTR_WIDGET_FEATURES = "widget_features";
    private static final java.lang.String SIZE_SEPARATOR = ",";
    private static final java.lang.String TAG = "AppWidgetXmlUtil";

    public static void writeAppWidgetProviderInfoLocked(com.android.modules.utils.TypedXmlSerializer out, android.appwidget.AppWidgetProviderInfo info) throws java.io.IOException {
        java.util.Objects.requireNonNull(out);
        java.util.Objects.requireNonNull(info);
        out.attributeInt((java.lang.String) null, ATTR_MIN_WIDTH, info.minWidth);
        out.attributeInt((java.lang.String) null, ATTR_MIN_HEIGHT, info.minHeight);
        out.attributeInt((java.lang.String) null, ATTR_MIN_RESIZE_WIDTH, info.minResizeWidth);
        out.attributeInt((java.lang.String) null, ATTR_MIN_RESIZE_HEIGHT, info.minResizeHeight);
        out.attributeInt((java.lang.String) null, ATTR_MAX_RESIZE_WIDTH, info.maxResizeWidth);
        out.attributeInt((java.lang.String) null, ATTR_MAX_RESIZE_HEIGHT, info.maxResizeHeight);
        out.attributeInt((java.lang.String) null, ATTR_TARGET_CELL_WIDTH, info.targetCellWidth);
        out.attributeInt((java.lang.String) null, ATTR_TARGET_CELL_HEIGHT, info.targetCellHeight);
        out.attributeInt((java.lang.String) null, ATTR_UPDATE_PERIOD_MILLIS, info.updatePeriodMillis);
        out.attributeInt((java.lang.String) null, ATTR_INITIAL_LAYOUT, info.initialLayout);
        out.attributeInt((java.lang.String) null, ATTR_INITIAL_KEYGUARD_LAYOUT, info.initialKeyguardLayout);
        if (info.configure != null) {
            out.attribute((java.lang.String) null, ATTR_CONFIGURE, info.configure.flattenToShortString());
        }
        if (info.label != null) {
            out.attribute((java.lang.String) null, ATTR_LABEL, info.label);
        }
        out.attributeInt((java.lang.String) null, ATTR_ICON, info.icon);
        out.attributeInt((java.lang.String) null, ATTR_PREVIEW_IMAGE, info.previewImage);
        out.attributeInt((java.lang.String) null, ATTR_PREVIEW_LAYOUT, info.previewLayout);
        out.attributeInt((java.lang.String) null, ATTR_AUTO_ADVANCED_VIEW_ID, info.autoAdvanceViewId);
        out.attributeInt((java.lang.String) null, ATTR_RESIZE_MODE, info.resizeMode);
        out.attributeInt((java.lang.String) null, ATTR_WIDGET_CATEGORY, info.widgetCategory);
        out.attributeInt((java.lang.String) null, ATTR_WIDGET_FEATURES, info.widgetFeatures);
        out.attributeInt((java.lang.String) null, ATTR_DESCRIPTION_RES, info.descriptionRes);
        out.attributeBoolean((java.lang.String) null, ATTR_PROVIDER_INHERITANCE, info.isExtendedFromAppWidgetProvider);
        out.attribute((java.lang.String) null, ATTR_OS_FINGERPRINT, android.os.Build.FINGERPRINT);
    }

    public static android.appwidget.AppWidgetProviderInfo readAppWidgetProviderInfoLocked(com.android.modules.utils.TypedXmlPullParser parser) {
        java.util.Objects.requireNonNull(parser);
        java.lang.String fingerprint = parser.getAttributeValue((java.lang.String) null, ATTR_OS_FINGERPRINT);
        boolean isDeviceUpgrading = false;
        android.content.pm.IPackageManager packageManager = android.app.AppGlobals.getPackageManager();
        if (packageManager != null) {
            try {
                isDeviceUpgrading = packageManager.isDeviceUpgrading();
                android.util.Slog.d(TAG, "packageManager.isDeviceUpgrading(): " + isDeviceUpgrading);
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Failed to get packageManager.isDeviceUpgrading()", e);
            }
        }
        boolean isFingerPrintChanged = !android.os.Build.FINGERPRINT.equals(fingerprint);
        android.util.Slog.d(TAG, "Build.FINGERPRINT: " + android.os.Build.FINGERPRINT + " ,fingerprint: " + fingerprint + ", isFingerPrintChanged" + isFingerPrintChanged + ", isDeviceUpgrading:" + isDeviceUpgrading);
        if (isFingerPrintChanged || isDeviceUpgrading) {
            return null;
        }
        android.appwidget.AppWidgetProviderInfo info = new android.appwidget.AppWidgetProviderInfo();
        info.minWidth = parser.getAttributeInt((java.lang.String) null, ATTR_MIN_WIDTH, 0);
        info.minHeight = parser.getAttributeInt((java.lang.String) null, ATTR_MIN_HEIGHT, 0);
        info.minResizeWidth = parser.getAttributeInt((java.lang.String) null, ATTR_MIN_RESIZE_WIDTH, 0);
        info.minResizeHeight = parser.getAttributeInt((java.lang.String) null, ATTR_MIN_RESIZE_HEIGHT, 0);
        info.maxResizeWidth = parser.getAttributeInt((java.lang.String) null, ATTR_MAX_RESIZE_WIDTH, 0);
        info.maxResizeHeight = parser.getAttributeInt((java.lang.String) null, ATTR_MAX_RESIZE_HEIGHT, 0);
        info.targetCellWidth = parser.getAttributeInt((java.lang.String) null, ATTR_TARGET_CELL_WIDTH, 0);
        info.targetCellHeight = parser.getAttributeInt((java.lang.String) null, ATTR_TARGET_CELL_HEIGHT, 0);
        info.updatePeriodMillis = parser.getAttributeInt((java.lang.String) null, ATTR_UPDATE_PERIOD_MILLIS, 0);
        info.initialLayout = parser.getAttributeInt((java.lang.String) null, ATTR_INITIAL_LAYOUT, 0);
        info.initialKeyguardLayout = parser.getAttributeInt((java.lang.String) null, ATTR_INITIAL_KEYGUARD_LAYOUT, 0);
        java.lang.String configure = parser.getAttributeValue((java.lang.String) null, ATTR_CONFIGURE);
        if (!android.text.TextUtils.isEmpty(configure)) {
            info.configure = android.content.ComponentName.unflattenFromString(configure);
        }
        info.label = parser.getAttributeValue((java.lang.String) null, ATTR_LABEL);
        info.icon = parser.getAttributeInt((java.lang.String) null, ATTR_ICON, 0);
        info.previewImage = parser.getAttributeInt((java.lang.String) null, ATTR_PREVIEW_IMAGE, 0);
        info.previewLayout = parser.getAttributeInt((java.lang.String) null, ATTR_PREVIEW_LAYOUT, 0);
        info.autoAdvanceViewId = parser.getAttributeInt((java.lang.String) null, ATTR_AUTO_ADVANCED_VIEW_ID, 0);
        info.resizeMode = parser.getAttributeInt((java.lang.String) null, ATTR_RESIZE_MODE, 0);
        info.widgetCategory = parser.getAttributeInt((java.lang.String) null, ATTR_WIDGET_CATEGORY, 0);
        info.widgetFeatures = parser.getAttributeInt((java.lang.String) null, ATTR_WIDGET_FEATURES, 0);
        info.descriptionRes = parser.getAttributeInt((java.lang.String) null, ATTR_DESCRIPTION_RES, 0);
        info.isExtendedFromAppWidgetProvider = parser.getAttributeBoolean((java.lang.String) null, ATTR_PROVIDER_INHERITANCE, false);
        return info;
    }

    static java.lang.String serializeWidgetSizes(java.util.List<android.util.SizeF> sizes) {
        return (java.lang.String) sizes.stream().map(new java.util.function.Function() { // from class: com.android.server.appwidget.AppWidgetXmlUtil$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.util.SizeF) obj).toString();
            }
        }).collect(java.util.stream.Collectors.joining(SIZE_SEPARATOR));
    }

    static java.util.ArrayList<android.util.SizeF> deserializeWidgetSizesStr(java.lang.String sizesStr) {
        if (sizesStr == null || sizesStr.isEmpty()) {
            return null;
        }
        try {
            return (java.util.ArrayList) java.util.Arrays.stream(sizesStr.split(SIZE_SEPARATOR)).map(new java.util.function.Function() { // from class: com.android.server.appwidget.AppWidgetXmlUtil$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.util.SizeF.parseSizeF((java.lang.String) obj);
                }
            }).collect(java.util.stream.Collectors.toCollection(new java.util.function.Supplier() { // from class: com.android.server.appwidget.AppWidgetXmlUtil$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return new java.util.ArrayList();
                }
            }));
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "Error parsing widget sizes", e);
            return null;
        }
    }
}
