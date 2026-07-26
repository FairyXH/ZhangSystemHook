package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
final class MediaButtonReceiverHolder {
    private static final java.lang.String COMPONENT_NAME_USER_ID_DELIM = ",";
    public static final int COMPONENT_TYPE_ACTIVITY = 2;
    public static final int COMPONENT_TYPE_BROADCAST = 1;
    public static final int COMPONENT_TYPE_INVALID = 0;
    public static final int COMPONENT_TYPE_SERVICE = 3;
    private static final boolean DEBUG_KEY_EVENT = true;
    private static final int PACKAGE_MANAGER_COMMON_FLAGS = 786432;
    private static final java.lang.String TAG = "PendingIntentHolder";
    private final android.content.ComponentName mComponentName;
    private final int mComponentType;
    private final java.lang.String mPackageName;
    private final android.app.PendingIntent mPendingIntent;
    private final int mUserId;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ComponentType {
    }

    public static com.android.server.media.MediaButtonReceiverHolder unflattenFromString(android.content.Context context, java.lang.String mediaButtonReceiverInfo) {
        java.lang.String[] tokens;
        android.content.ComponentName componentName;
        int componentType;
        if (android.text.TextUtils.isEmpty(mediaButtonReceiverInfo) || (tokens = mediaButtonReceiverInfo.split(COMPONENT_NAME_USER_ID_DELIM)) == null || ((tokens.length != 2 && tokens.length != 3) || (componentName = android.content.ComponentName.unflattenFromString(tokens[0])) == null)) {
            return null;
        }
        int userId = java.lang.Integer.parseInt(tokens[1]);
        if (tokens.length == 3) {
            componentType = java.lang.Integer.parseInt(tokens[2]);
        } else {
            componentType = getComponentType(context, componentName);
        }
        return new com.android.server.media.MediaButtonReceiverHolder(userId, null, componentName, componentType);
    }

    public static com.android.server.media.MediaButtonReceiverHolder create(int userId, android.app.PendingIntent pendingIntent, java.lang.String sessionPackageName) {
        if (pendingIntent == null) {
            return null;
        }
        int componentType = getComponentType(pendingIntent);
        android.content.ComponentName componentName = getComponentName(pendingIntent, componentType);
        if (componentName != null) {
            return new com.android.server.media.MediaButtonReceiverHolder(userId, pendingIntent, componentName, componentType);
        }
        android.util.Log.w(TAG, "Unresolvable implicit intent is set, pi=" + pendingIntent);
        return new com.android.server.media.MediaButtonReceiverHolder(userId, pendingIntent, sessionPackageName);
    }

    public static com.android.server.media.MediaButtonReceiverHolder create(int userId, android.content.ComponentName broadcastReceiver) {
        return new com.android.server.media.MediaButtonReceiverHolder(userId, null, broadcastReceiver, 1);
    }

    private MediaButtonReceiverHolder(int userId, android.app.PendingIntent pendingIntent, android.content.ComponentName componentName, int componentType) {
        this.mUserId = userId;
        this.mPendingIntent = pendingIntent;
        this.mComponentName = componentName;
        this.mPackageName = componentName.getPackageName();
        this.mComponentType = componentType;
    }

    private MediaButtonReceiverHolder(int userId, android.app.PendingIntent pendingIntent, java.lang.String packageName) {
        this.mUserId = userId;
        this.mPendingIntent = pendingIntent;
        this.mComponentName = null;
        this.mPackageName = packageName;
        this.mComponentType = 0;
    }

    public int getUserId() {
        return this.mUserId;
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public boolean send(android.content.Context context, android.view.KeyEvent keyEvent, java.lang.String callingPackageName, int resultCode, android.app.PendingIntent.OnFinished onFinishedListener, android.os.Handler handler, long fgsAllowlistDurationMs) {
        java.lang.String str;
        android.app.PendingIntent pendingIntent;
        android.os.Bundle bundle;
        android.content.Intent mediaButtonIntent = new android.content.Intent("android.intent.action.MEDIA_BUTTON");
        mediaButtonIntent.addFlags(268435456);
        mediaButtonIntent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
        mediaButtonIntent.putExtra("android.intent.extra.PACKAGE_NAME", callingPackageName);
        android.app.BroadcastOptions options = android.app.BroadcastOptions.makeBasic();
        options.setTemporaryAppAllowlist(fgsAllowlistDurationMs, 0, com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_MEDIA_BUTTON, "");
        options.setBackgroundActivityStartsAllowed(true);
        if (this.mPendingIntent != null) {
            android.util.Log.d(TAG, "Sending " + keyEvent + " to the last known PendingIntent " + this.mPendingIntent);
            try {
                pendingIntent = this.mPendingIntent;
                bundle = options.toBundle();
                str = TAG;
            } catch (android.app.PendingIntent.CanceledException e) {
                e = e;
                str = TAG;
            }
            try {
                pendingIntent.send(context, resultCode, mediaButtonIntent, onFinishedListener, handler, null, bundle);
            } catch (android.app.PendingIntent.CanceledException e2) {
                e = e2;
                android.util.Log.w(str, "Error sending key event to media button receiver " + this.mPendingIntent, e);
                return false;
            }
        } else if (this.mComponentName != null) {
            android.util.Log.d(TAG, "Sending " + keyEvent + " to the restored intent " + this.mComponentName + ", type=" + this.mComponentType);
            mediaButtonIntent.setComponent(this.mComponentName);
            android.os.UserHandle userHandle = android.os.UserHandle.of(this.mUserId);
            try {
                switch (this.mComponentType) {
                    case 2:
                        context.startActivityAsUser(mediaButtonIntent, userHandle);
                        break;
                    case 3:
                        context.createContextAsUser(userHandle, 0).startForegroundService(mediaButtonIntent);
                        break;
                    default:
                        context.sendBroadcastAsUser(mediaButtonIntent, userHandle, null, options.toBundle());
                        break;
                }
            } catch (java.lang.Exception e3) {
                android.util.Log.w(TAG, "Error sending media button to the restored intent " + this.mComponentName + ", type=" + this.mComponentType, e3);
                return false;
            }
        } else {
            android.util.Log.e(TAG, "Shouldn't be happen -- pending intent or component name must be set");
            return false;
        }
        return true;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("MBR {pi=").append(this.mPendingIntent);
        sb.append(", componentName=").append(this.mComponentName);
        sb.append(", type=").append(this.mComponentType);
        sb.append(", pkg=").append(this.mPackageName);
        sb.append("}");
        return sb.toString();
    }

    public java.lang.String flattenToString() {
        if (this.mComponentName == null) {
            return "";
        }
        return java.lang.String.join(COMPONENT_NAME_USER_ID_DELIM, this.mComponentName.flattenToString(), java.lang.String.valueOf(this.mUserId), java.lang.String.valueOf(this.mComponentType));
    }

    public android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    private static int getComponentType(android.app.PendingIntent pendingIntent) {
        if (pendingIntent.isBroadcast()) {
            return 1;
        }
        if (pendingIntent.isActivity()) {
            return 2;
        }
        if (pendingIntent.isForegroundService() || pendingIntent.isService()) {
            return 3;
        }
        return 0;
    }

    private static int getComponentType(android.content.Context context, android.content.ComponentName componentName) {
        if (componentName == null) {
            return 0;
        }
        android.content.pm.PackageManager pm = context.getPackageManager();
        try {
            android.content.pm.ActivityInfo activityInfo = pm.getActivityInfo(componentName, 786433);
            if (activityInfo != null) {
                return 2;
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        try {
            android.content.pm.ServiceInfo serviceInfo = pm.getServiceInfo(componentName, 786436);
            if (serviceInfo != null) {
                return 3;
            }
            return 1;
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            return 1;
        }
    }

    private static android.content.ComponentName getComponentName(android.app.PendingIntent pendingIntent, int componentType) {
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = java.util.Collections.emptyList();
        switch (componentType) {
            case 1:
                resolveInfos = pendingIntent.queryIntentComponents(786434);
                break;
            case 2:
                resolveInfos = pendingIntent.queryIntentComponents(851969);
                break;
            case 3:
                resolveInfos = pendingIntent.queryIntentComponents(786436);
                break;
        }
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
            android.content.pm.ComponentInfo componentInfo = getComponentInfo(resolveInfo);
            if (componentInfo != null && android.text.TextUtils.equals(componentInfo.packageName, pendingIntent.getCreatorPackage()) && componentInfo.packageName != null && componentInfo.name != null) {
                return new android.content.ComponentName(componentInfo.packageName, componentInfo.name);
            }
        }
        return null;
    }

    private static android.content.pm.ComponentInfo getComponentInfo(android.content.pm.ResolveInfo resolveInfo) {
        if (resolveInfo.activityInfo != null) {
            return resolveInfo.activityInfo;
        }
        if (resolveInfo.serviceInfo != null) {
            return resolveInfo.serviceInfo;
        }
        return null;
    }
}
