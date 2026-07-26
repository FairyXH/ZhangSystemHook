package com.android.server.location.common;

/* JADX INFO: loaded from: classes2.dex */
public class OplusLbsFactory implements com.android.server.location.common.IOplusCommonFactory {
    private static final java.lang.String LBS_FEATURE_SERVICE_FACTORY_IMPL_NAME = "com.android.server.location.OplusLbsFactoryImpl";
    private static final java.lang.String OPLUS_LBS_COMMON_EXT_JAR_PATH = "/system_ext/framework/oplus-lbs-services.jar";
    private static final java.lang.String OPLUS_LMS = "com.android.server.location.OplusLocationManagerService";
    private static final java.lang.String TAG = "OplusLbsFactory";
    private static android.content.Context sContext;
    private static volatile com.android.server.location.common.OplusLbsFactory sInstance = null;

    public static com.android.server.location.common.OplusLbsFactory getInstance() {
        if (sInstance == null) {
            synchronized (com.android.server.location.common.OplusLbsFactory.class) {
                if (sInstance == null) {
                    try {
                        sInstance = (com.android.server.location.common.OplusLbsFactory) newInstance(OPLUS_LBS_COMMON_EXT_JAR_PATH, LBS_FEATURE_SERVICE_FACTORY_IMPL_NAME);
                    } catch (java.lang.Exception e) {
                        android.util.Log.e(TAG, " Reflect exception getInstance: " + e.toString());
                        sInstance = new com.android.server.location.common.OplusLbsFactory();
                    }
                }
            }
        }
        return sInstance;
    }

    @Override // com.android.server.location.common.IOplusCommonFactory
    public boolean isValid(int index) {
        return index < com.android.server.location.common.OplusLbsFeatureList.OplusIndex.EndLbsFrameworkFactory.ordinal() && index > com.android.server.location.common.OplusLbsFeatureList.OplusIndex.StartLbsFrameworkFactory.ordinal();
    }

    public static void init(android.content.Context context) {
        sContext = context;
    }

    private static java.lang.Object newInstance(java.lang.String className) throws java.lang.Exception {
        java.lang.Class<?> clazz = java.lang.Class.forName(className);
        return clazz.getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
    }

    static java.lang.Object newInstance(java.lang.String libPath, java.lang.String className) throws java.lang.Exception {
        dalvik.system.PathClassLoader classLoader = new dalvik.system.PathClassLoader(libPath, com.android.server.location.common.OplusLbsFactory.class.getClassLoader());
        java.lang.Class<?> clazz = java.lang.Class.forName(className, false, classLoader);
        return clazz.getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
    }
}
