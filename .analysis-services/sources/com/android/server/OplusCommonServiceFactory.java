package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public abstract class OplusCommonServiceFactory implements android.common.IOplusCommonFactory {
    private static final java.lang.String AMS_CLASSNAME = "com.android.server.am.OplusActivityManagerService";
    private static final java.lang.String AS_CLASSNAME = "com.android.server.audio.OplusAudioService";
    private static final java.lang.String ATMS_CLASSNAME = "com.android.server.wm.OplusActivityTaskManagerService";
    private static final java.lang.String MY_TAG = "OplusCommonServiceFactory";
    private static final java.lang.String WMS_CLASSNAME = "com.android.server.wm.OplusWindowManagerService";
    private final java.lang.String TAG = getClass().getSimpleName();

    public static final com.android.server.audio.AudioService getOplusAudioService(android.content.Context context) {
        return createOplusAudioService(context);
    }

    static java.lang.Object newInstance(java.lang.String className) throws java.lang.Exception {
        java.lang.Class<?> clazz = java.lang.Class.forName(className);
        return clazz.getConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]);
    }

    protected void warn(java.lang.String methodName) {
        android.util.Slog.w(this.TAG, methodName);
    }

    private static com.android.server.am.ActivityManagerService createActivityManagerService(android.content.Context context, com.android.server.wm.ActivityTaskManagerService atms) {
        android.util.Slog.i(MY_TAG, "createActivityManagerService reflect");
        try {
            java.lang.Class<?> clazz = java.lang.Class.forName(AMS_CLASSNAME);
            return (com.android.server.am.ActivityManagerService) clazz.getDeclaredConstructor(android.content.Context.class, com.android.server.wm.ActivityTaskManagerService.class).newInstance(context, atms);
        } catch (java.lang.ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (java.lang.IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (java.lang.InstantiationException e3) {
            e3.printStackTrace();
            return null;
        } catch (java.lang.NoSuchMethodException e4) {
            e4.printStackTrace();
            return null;
        } catch (java.lang.reflect.InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }

    private static com.android.server.audio.AudioService createOplusAudioService(android.content.Context context) {
        android.util.Slog.i(MY_TAG, "createOplusAudioService reflect");
        try {
            java.lang.Class<?> clazz = java.lang.Class.forName(AS_CLASSNAME);
            return (com.android.server.audio.AudioService) clazz.getDeclaredConstructor(android.content.Context.class).newInstance(context);
        } catch (java.lang.ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (java.lang.IllegalAccessException e2) {
            e2.printStackTrace();
            return null;
        } catch (java.lang.InstantiationException e3) {
            e3.printStackTrace();
            return null;
        } catch (java.lang.NoSuchMethodException e4) {
            e4.printStackTrace();
            return null;
        } catch (java.lang.reflect.InvocationTargetException e5) {
            e5.printStackTrace();
            return null;
        }
    }
}
