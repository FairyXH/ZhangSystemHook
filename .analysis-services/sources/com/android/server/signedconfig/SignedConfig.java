package com.android.server.signedconfig;

/* JADX INFO: loaded from: classes3.dex */
public class SignedConfig {
    private static final java.lang.String CONFIG_KEY_MAX_SDK = "max_sdk";
    private static final java.lang.String CONFIG_KEY_MIN_SDK = "min_sdk";
    private static final java.lang.String CONFIG_KEY_VALUES = "values";
    private static final java.lang.String KEY_CONFIG = "config";
    private static final java.lang.String KEY_VERSION = "version";
    public final java.util.List<com.android.server.signedconfig.SignedConfig.PerSdkConfig> perSdkConfig;
    public final int version;

    public static class PerSdkConfig {
        public final int maxSdk;
        public final int minSdk;
        public final java.util.Map<java.lang.String, java.lang.String> values;

        public PerSdkConfig(int minSdk, int maxSdk, java.util.Map<java.lang.String, java.lang.String> values) {
            this.minSdk = minSdk;
            this.maxSdk = maxSdk;
            this.values = java.util.Collections.unmodifiableMap(values);
        }
    }

    public SignedConfig(int version, java.util.List<com.android.server.signedconfig.SignedConfig.PerSdkConfig> perSdkConfig) {
        this.version = version;
        this.perSdkConfig = java.util.Collections.unmodifiableList(perSdkConfig);
    }

    public com.android.server.signedconfig.SignedConfig.PerSdkConfig getMatchingConfig(int sdkVersion) {
        for (com.android.server.signedconfig.SignedConfig.PerSdkConfig config : this.perSdkConfig) {
            if (config.minSdk <= sdkVersion && sdkVersion <= config.maxSdk) {
                return config;
            }
        }
        return null;
    }

    public static com.android.server.signedconfig.SignedConfig parse(java.lang.String config, java.util.Set<java.lang.String> allowedKeys, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> keyValueMappers) throws com.android.server.signedconfig.InvalidConfigException {
        try {
            org.json.JSONObject json = new org.json.JSONObject(config);
            int version = json.getInt(KEY_VERSION);
            org.json.JSONArray perSdkConfig = json.getJSONArray(KEY_CONFIG);
            java.util.List<com.android.server.signedconfig.SignedConfig.PerSdkConfig> parsedConfigs = new java.util.ArrayList<>();
            for (int i = 0; i < perSdkConfig.length(); i++) {
                parsedConfigs.add(parsePerSdkConfig(perSdkConfig.getJSONObject(i), allowedKeys, keyValueMappers));
            }
            return new com.android.server.signedconfig.SignedConfig(version, parsedConfigs);
        } catch (org.json.JSONException e) {
            throw new com.android.server.signedconfig.InvalidConfigException("Could not parse JSON", e);
        }
    }

    private static java.lang.CharSequence quoted(java.lang.Object s) {
        if (s == null) {
            return "null";
        }
        return "\"" + s + "\"";
    }

    static com.android.server.signedconfig.SignedConfig.PerSdkConfig parsePerSdkConfig(org.json.JSONObject json, java.util.Set<java.lang.String> allowedKeys, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> keyValueMappers) throws org.json.JSONException, com.android.server.signedconfig.InvalidConfigException {
        java.lang.String value;
        int minSdk = json.getInt(CONFIG_KEY_MIN_SDK);
        int maxSdk = json.getInt(CONFIG_KEY_MAX_SDK);
        org.json.JSONObject valuesJson = json.getJSONObject(CONFIG_KEY_VALUES);
        java.util.Map<java.lang.String, java.lang.String> values = new java.util.HashMap<>();
        for (java.lang.String key : valuesJson.keySet()) {
            java.lang.Object valueObject = valuesJson.get(key);
            if (valueObject == org.json.JSONObject.NULL || valueObject == null) {
                value = null;
            } else {
                value = valueObject.toString();
            }
            if (!allowedKeys.contains(key)) {
                throw new com.android.server.signedconfig.InvalidConfigException("Config key " + key + " is not allowed");
            }
            if (keyValueMappers.containsKey(key)) {
                java.util.Map<java.lang.String, java.lang.String> mapper = keyValueMappers.get(key);
                if (!mapper.containsKey(value)) {
                    throw new com.android.server.signedconfig.InvalidConfigException("Config key " + key + " contains unsupported value " + ((java.lang.Object) quoted(value)));
                }
                value = mapper.get(value);
            }
            values.put(key, value);
        }
        return new com.android.server.signedconfig.SignedConfig.PerSdkConfig(minSdk, maxSdk, values);
    }
}
