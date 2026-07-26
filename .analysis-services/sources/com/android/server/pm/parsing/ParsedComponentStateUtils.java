package com.android.server.pm.parsing;

/* JADX INFO: loaded from: classes2.dex */
public class ParsedComponentStateUtils {
    public static android.util.Pair<java.lang.CharSequence, java.lang.Integer> getNonLocalizedLabelAndIcon(com.android.internal.pm.pkg.component.ParsedComponent component, com.android.server.pm.pkg.PackageStateInternal pkgSetting, int userId) {
        java.lang.CharSequence label = component.getNonLocalizedLabel();
        int icon = component.getIcon();
        android.util.Pair<java.lang.String, java.lang.Integer> overrideLabelIcon = pkgSetting == null ? null : pkgSetting.getUserStateOrDefault(userId).getOverrideLabelIconForComponent(component.getComponentName());
        if (overrideLabelIcon != null) {
            if (overrideLabelIcon.first != null) {
                label = (java.lang.CharSequence) overrideLabelIcon.first;
            }
            if (overrideLabelIcon.second != null) {
                icon = ((java.lang.Integer) overrideLabelIcon.second).intValue();
            }
        }
        return android.util.Pair.create(label, java.lang.Integer.valueOf(icon));
    }
}
