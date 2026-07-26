package com.android.server.stats.pull;

/* JADX INFO: loaded from: classes3.dex */
final class SettingsStatsUtil {
    private static final java.lang.String NAMESPACE_OPLUS_SETTINGS_STATS = "settings_ostats";
    private static final java.lang.String TAG = "SettingsStatsUtil";
    private static final com.android.server.stats.pull.SettingsStatsUtil.FlagsData[] GLOBAL_SETTINGS = {new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("GlobalFeature__boolean_whitelist", 1), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("GlobalFeature__integer_whitelist", 2), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("GlobalFeature__float_whitelist", 3), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("GlobalFeature__string_whitelist", 4)};
    private static final com.android.server.stats.pull.SettingsStatsUtil.FlagsData[] SECURE_SETTINGS = {new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SecureFeature__boolean_whitelist", 1), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SecureFeature__integer_whitelist", 2), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SecureFeature__float_whitelist", 3), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SecureFeature__string_whitelist", 4)};
    private static final com.android.server.stats.pull.SettingsStatsUtil.FlagsData[] SYSTEM_SETTINGS = {new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SystemFeature__boolean_whitelist", 1), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SystemFeature__integer_whitelist", 2), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SystemFeature__float_whitelist", 3), new com.android.server.stats.pull.SettingsStatsUtil.FlagsData("SystemFeature__string_whitelist", 4)};
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.settings.ostats.debug", false);

    SettingsStatsUtil() {
    }

    static java.util.List<android.util.StatsEvent> logGlobalSettings(android.content.Context context, int atomTag, int userId) {
        java.util.List<android.util.StatsEvent> output = new java.util.ArrayList<>();
        android.content.ContentResolver resolver = context.getContentResolver();
        for (com.android.server.stats.pull.SettingsStatsUtil.FlagsData flagsData : GLOBAL_SETTINGS) {
            com.android.service.nano.StringListParamProto proto = getList(flagsData.mFlagName);
            if (proto != null) {
                for (java.lang.String key : proto.element) {
                    java.lang.String value = android.provider.Settings.Global.getStringForUser(resolver, key, userId);
                    output.add(createStatsEvent(atomTag, key, value, userId, flagsData.mDataType));
                }
            }
        }
        return output;
    }

    static java.util.List<android.util.StatsEvent> logSystemSettings(android.content.Context context, int atomTag, int userId) {
        java.util.List<android.util.StatsEvent> output = new java.util.ArrayList<>();
        android.content.ContentResolver resolver = context.getContentResolver();
        for (com.android.server.stats.pull.SettingsStatsUtil.FlagsData flagsData : SYSTEM_SETTINGS) {
            com.android.service.nano.StringListParamProto proto = getList(flagsData.mFlagName);
            if (proto != null) {
                for (java.lang.String key : proto.element) {
                    java.lang.String value = android.provider.Settings.System.getStringForUser(resolver, key, userId);
                    output.add(createStatsEvent(atomTag, key, value, userId, flagsData.mDataType));
                }
            }
        }
        return output;
    }

    static java.util.List<android.util.StatsEvent> logSecureSettings(android.content.Context context, int atomTag, int userId) {
        java.util.List<android.util.StatsEvent> output = new java.util.ArrayList<>();
        android.content.ContentResolver resolver = context.getContentResolver();
        for (com.android.server.stats.pull.SettingsStatsUtil.FlagsData flagsData : SECURE_SETTINGS) {
            com.android.service.nano.StringListParamProto proto = getList(flagsData.mFlagName);
            if (proto != null) {
                for (java.lang.String key : proto.element) {
                    java.lang.String value = android.provider.Settings.Secure.getStringForUser(resolver, key, userId);
                    output.add(createStatsEvent(atomTag, key, value, userId, flagsData.mDataType));
                }
            }
        }
        return output;
    }

    static com.android.service.nano.StringListParamProto getList(java.lang.String flag) {
        java.util.Set<java.lang.String> stringSet = new android.util.ArraySet<>();
        com.android.service.nano.StringListParamProto originProto = getList(flag, "settings_stats");
        if (originProto != null) {
            if (DEBUG) {
                logDetails("settings_stats", flag, originProto.element);
            }
            stringSet.addAll((java.util.Collection) java.util.Arrays.stream(originProto.element).collect(java.util.stream.Collectors.toList()));
        }
        com.android.service.nano.StringListParamProto ostatsProto = getList(flag, NAMESPACE_OPLUS_SETTINGS_STATS);
        if (ostatsProto != null) {
            if (DEBUG) {
                logDetails(NAMESPACE_OPLUS_SETTINGS_STATS, flag, ostatsProto.element);
            }
            stringSet.addAll((java.util.Collection) java.util.Arrays.stream(ostatsProto.element).collect(java.util.stream.Collectors.toList()));
        }
        if (stringSet.isEmpty()) {
            return null;
        }
        com.android.service.nano.StringListParamProto mergedStringProto = new com.android.service.nano.StringListParamProto();
        mergedStringProto.element = (java.lang.String[]) stringSet.toArray(new java.util.function.IntFunction() { // from class: com.android.server.stats.pull.SettingsStatsUtil$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.stats.pull.SettingsStatsUtil.lambda$getList$0(i);
            }
        });
        if (DEBUG) {
            android.util.Slog.d(TAG, "Get string list=" + java.util.Arrays.toString(mergedStringProto.element));
        }
        return mergedStringProto;
    }

    static /* synthetic */ java.lang.String[] lambda$getList$0(int x$0) {
        return new java.lang.String[x$0];
    }

    private static void logDetails(java.lang.String namespace, java.lang.String flag, java.lang.String[] element) {
        android.util.Slog.d(TAG, java.lang.String.format("Receive list with namespace:%s flag:%s with elements=%s", namespace, flag, java.util.Arrays.toString(element)));
    }

    static com.android.service.nano.StringListParamProto getList(java.lang.String flag, java.lang.String namespace) {
        java.lang.String base64 = android.provider.DeviceConfig.getProperty(namespace, flag);
        if (android.text.TextUtils.isEmpty(base64)) {
            return null;
        }
        byte[] decode = android.util.Base64.decode(base64, 3);
        try {
            com.android.service.nano.StringListParamProto list = com.android.service.nano.StringListParamProto.parseFrom(decode);
            return list;
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error parsing string list proto", e);
            return null;
        }
    }

    private static android.util.StatsEvent createStatsEvent(int atomTag, java.lang.String key, java.lang.String value, int userId, int type) {
        android.util.StatsEvent.Builder builder = android.util.StatsEvent.newBuilder().setAtomId(atomTag).writeString(key);
        boolean booleanValue = false;
        int intValue = 0;
        float floatValue = 0.0f;
        java.lang.String stringValue = "";
        if (android.text.TextUtils.isEmpty(value)) {
            builder.writeInt(0).writeBoolean(false).writeInt(0).writeFloat(0.0f).writeString("").writeInt(userId);
        } else {
            switch (type) {
                case 1:
                    booleanValue = "1".equals(value);
                    break;
                case 2:
                    try {
                        intValue = java.lang.Integer.parseInt(value);
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Slog.w(TAG, "Can not parse value to float: " + value);
                    }
                    break;
                case 3:
                    try {
                        floatValue = java.lang.Float.parseFloat(value);
                    } catch (java.lang.NumberFormatException e2) {
                        android.util.Slog.w(TAG, "Can not parse value to float: " + value);
                    }
                    break;
                case 4:
                    stringValue = value;
                    break;
                default:
                    android.util.Slog.w(TAG, "Unexpected value type " + type);
                    break;
            }
            builder.writeInt(type).writeBoolean(booleanValue).writeInt(intValue).writeFloat(floatValue).writeString(stringValue).writeInt(userId);
        }
        return builder.build();
    }

    static final class FlagsData {
        int mDataType;
        java.lang.String mFlagName;

        FlagsData(java.lang.String flagName, int dataType) {
            this.mFlagName = flagName;
            this.mDataType = dataType;
        }
    }
}
