package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface ActivityInterceptorCallback {
    public static final int DREAM_MANAGER_ORDERED_ID = 4;
    public static final int MAINLINE_FIRST_ORDERED_ID = 1000;
    public static final int MAINLINE_LAST_ORDERED_ID = 1001;
    public static final int MAINLINE_SDK_SANDBOX_ORDER_ID = 1001;
    public static final int PERMISSION_POLICY_ORDERED_ID = 1;
    public static final int PRODUCT_ORDERED_ID = 5;
    public static final int SYSTEM_FIRST_ORDERED_ID = 0;
    public static final int SYSTEM_LAST_ORDERED_ID = 5;
    public static final int VIRTUAL_DEVICE_SERVICE_ORDERED_ID = 3;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface OrderedId {
    }

    com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptResult onInterceptActivityLaunch(com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo activityInterceptorInfo);

    default void onActivityLaunched(android.app.TaskInfo taskInfo, android.content.pm.ActivityInfo activityInfo, com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo info) {
    }

    static boolean isValidOrderId(int id) {
        return isValidMainlineOrderId(id) || (id >= 0 && id <= 5);
    }

    static boolean isValidMainlineOrderId(int id) {
        return id >= 1000 && id <= 1001;
    }

    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
    public static final class ActivityInterceptorInfo {
        private final android.content.pm.ActivityInfo mActivityInfo;
        private final java.lang.String mCallingFeatureId;
        private final java.lang.String mCallingPackage;
        private final int mCallingPid;
        private final int mCallingUid;
        private final android.app.ActivityOptions mCheckedOptions;
        private final java.lang.Runnable mClearOptionsAnimation;
        private final android.content.Intent mIntent;
        private final int mRealCallingPid;
        private final int mRealCallingUid;
        private final android.content.pm.ResolveInfo mResolveInfo;
        private final java.lang.String mResolvedType;
        private final int mUserId;

        public ActivityInterceptorInfo(com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo.Builder builder) {
            this.mCallingUid = builder.mCallingUid;
            this.mCallingPid = builder.mCallingPid;
            this.mRealCallingUid = builder.mRealCallingUid;
            this.mRealCallingPid = builder.mRealCallingPid;
            this.mUserId = builder.mUserId;
            this.mIntent = builder.mIntent;
            this.mResolveInfo = builder.mResolveInfo;
            this.mActivityInfo = builder.mActivityInfo;
            this.mResolvedType = builder.mResolvedType;
            this.mCallingPackage = builder.mCallingPackage;
            this.mCallingFeatureId = builder.mCallingFeatureId;
            this.mCheckedOptions = builder.mCheckedOptions;
            this.mClearOptionsAnimation = builder.mClearOptionsAnimation;
        }

        public static final class Builder {
            private final android.content.pm.ActivityInfo mActivityInfo;
            private final int mCallingPid;
            private final int mCallingUid;
            private final android.content.Intent mIntent;
            private final int mRealCallingPid;
            private final int mRealCallingUid;
            private final android.content.pm.ResolveInfo mResolveInfo;
            private java.lang.String mResolvedType;
            private final int mUserId;
            private java.lang.String mCallingPackage = null;
            private java.lang.String mCallingFeatureId = null;
            private android.app.ActivityOptions mCheckedOptions = null;
            private java.lang.Runnable mClearOptionsAnimation = null;

            public Builder(int callingUid, int callingPid, int realCallingUid, int realCallingPid, int userId, android.content.Intent intent, android.content.pm.ResolveInfo rInfo, android.content.pm.ActivityInfo aInfo) {
                this.mCallingUid = callingUid;
                this.mCallingPid = callingPid;
                this.mRealCallingUid = realCallingUid;
                this.mRealCallingPid = realCallingPid;
                this.mUserId = userId;
                this.mIntent = intent;
                this.mResolveInfo = rInfo;
                this.mActivityInfo = aInfo;
            }

            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo build() {
                return new com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo(this);
            }

            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo.Builder setResolvedType(java.lang.String resolvedType) {
                this.mResolvedType = resolvedType;
                return this;
            }

            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo.Builder setCallingPackage(java.lang.String callingPackage) {
                this.mCallingPackage = callingPackage;
                return this;
            }

            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo.Builder setCallingFeatureId(java.lang.String callingFeatureId) {
                this.mCallingFeatureId = callingFeatureId;
                return this;
            }

            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo.Builder setCheckedOptions(android.app.ActivityOptions checkedOptions) {
                this.mCheckedOptions = checkedOptions;
                return this;
            }

            public com.android.server.wm.ActivityInterceptorCallback.ActivityInterceptorInfo.Builder setClearOptionsAnimationRunnable(java.lang.Runnable clearOptionsAnimationRunnable) {
                this.mClearOptionsAnimation = clearOptionsAnimationRunnable;
                return this;
            }
        }

        public int getCallingUid() {
            return this.mCallingUid;
        }

        public int getCallingPid() {
            return this.mCallingPid;
        }

        public int getRealCallingUid() {
            return this.mRealCallingUid;
        }

        public int getRealCallingPid() {
            return this.mRealCallingPid;
        }

        public int getUserId() {
            return this.mUserId;
        }

        public android.content.Intent getIntent() {
            return this.mIntent;
        }

        public android.content.pm.ResolveInfo getResolveInfo() {
            return this.mResolveInfo;
        }

        public android.content.pm.ActivityInfo getActivityInfo() {
            return this.mActivityInfo;
        }

        public java.lang.String getResolvedType() {
            return this.mResolvedType;
        }

        public java.lang.String getCallingPackage() {
            return this.mCallingPackage;
        }

        public java.lang.String getCallingFeatureId() {
            return this.mCallingFeatureId;
        }

        public android.app.ActivityOptions getCheckedOptions() {
            return this.mCheckedOptions;
        }

        public java.lang.Runnable getClearOptionsAnimationRunnable() {
            return this.mClearOptionsAnimation;
        }
    }

    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
    public static final class ActivityInterceptResult {
        private final android.app.ActivityOptions mActivityOptions;
        private final boolean mActivityResolved;
        private final android.content.Intent mIntent;

        public ActivityInterceptResult(android.content.Intent intent, android.app.ActivityOptions activityOptions) {
            this(intent, activityOptions, false);
        }

        public ActivityInterceptResult(android.content.Intent intent, android.app.ActivityOptions activityOptions, boolean activityResolved) {
            this.mIntent = intent;
            this.mActivityOptions = activityOptions;
            this.mActivityResolved = activityResolved;
        }

        public android.content.Intent getIntent() {
            return this.mIntent;
        }

        public android.app.ActivityOptions getActivityOptions() {
            return this.mActivityOptions;
        }

        public boolean isActivityResolved() {
            return this.mActivityResolved;
        }
    }
}
