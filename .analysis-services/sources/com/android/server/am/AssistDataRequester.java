package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class AssistDataRequester extends android.app.IAssistDataReceiver.Stub {
    public static final java.lang.String KEY_RECEIVER_EXTRA_COUNT = "count";
    public static final java.lang.String KEY_RECEIVER_EXTRA_INDEX = "index";
    private android.app.AppOpsManager mAppOpsManager;
    private com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks mCallbacks;
    private java.lang.Object mCallbacksLock;
    private boolean mCanceled;
    private android.content.Context mContext;
    private int mPendingDataCount;
    private int mPendingScreenshotCount;
    private int mRequestScreenshotAppOps;
    private int mRequestStructureAppOps;
    private android.view.IWindowManager mWindowManager;
    private final java.util.ArrayList<android.os.Bundle> mAssistData = new java.util.ArrayList<>();
    private final java.util.ArrayList<android.graphics.Bitmap> mAssistScreenshot = new java.util.ArrayList<>();
    public android.app.IActivityTaskManager mActivityTaskManager = android.app.ActivityTaskManager.getService();

    public interface AssistDataRequesterCallbacks {
        boolean canHandleReceivedAssistDataLocked();

        default void onAssistDataReceivedLocked(android.os.Bundle data, int activityIndex, int activityCount) {
        }

        default void onAssistScreenshotReceivedLocked(android.graphics.Bitmap screenshot) {
        }

        default void onAssistRequestCompleted() {
        }
    }

    public AssistDataRequester(android.content.Context context, android.view.IWindowManager windowManager, android.app.AppOpsManager appOpsManager, com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks callbacks, java.lang.Object callbacksLock, int requestStructureAppOps, int requestScreenshotAppOps) {
        this.mCallbacks = callbacks;
        this.mCallbacksLock = callbacksLock;
        this.mWindowManager = windowManager;
        this.mContext = context;
        this.mAppOpsManager = appOpsManager;
        this.mRequestStructureAppOps = requestStructureAppOps;
        this.mRequestScreenshotAppOps = requestScreenshotAppOps;
    }

    public void requestAssistData(java.util.List<android.os.IBinder> activityTokens, boolean fetchData, boolean fetchScreenshot, boolean allowFetchData, boolean allowFetchScreenshot, int callingUid, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
        requestAssistData(activityTokens, fetchData, fetchScreenshot, true, allowFetchData, allowFetchScreenshot, false, callingUid, callingPackage, callingAttributionTag);
    }

    public void requestAssistData(java.util.List<android.os.IBinder> activityTokens, boolean fetchData, boolean fetchScreenshot, boolean fetchStructure, boolean allowFetchData, boolean allowFetchScreenshot, boolean ignoreTopActivityCheck, int callingUid, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
        requestData(activityTokens, false, fetchData, fetchScreenshot, fetchStructure, allowFetchData, allowFetchScreenshot, ignoreTopActivityCheck, callingUid, callingPackage, callingAttributionTag);
    }

    private void requestData(java.util.List<android.os.IBinder> activityTokens, boolean requestAutofillData, boolean fetchData, boolean fetchScreenshot, boolean fetchStructure, boolean allowFetchData, boolean allowFetchScreenshot, boolean ignoreTopActivityCheck, int callingUid, java.lang.String callingPackage, java.lang.String callingAttributionTag) {
        boolean isAssistDataAllowed;
        int i;
        boolean result;
        java.util.Objects.requireNonNull(activityTokens);
        java.util.Objects.requireNonNull(callingPackage);
        if (activityTokens.isEmpty()) {
            tryDispatchRequestComplete();
            return;
        }
        try {
            boolean isAssistDataAllowed2 = this.mActivityTaskManager.isAssistDataAllowed();
            isAssistDataAllowed = isAssistDataAllowed2;
        } catch (android.os.RemoteException e) {
            isAssistDataAllowed = false;
        }
        boolean allowFetchData2 = allowFetchData & isAssistDataAllowed;
        boolean allowFetchScreenshot2 = allowFetchScreenshot & (fetchData && isAssistDataAllowed && this.mRequestScreenshotAppOps != -1);
        this.mCanceled = false;
        this.mPendingDataCount = 0;
        this.mPendingScreenshotCount = 0;
        this.mAssistData.clear();
        this.mAssistScreenshot.clear();
        if (fetchData) {
            if (this.mAppOpsManager.noteOpNoThrow(this.mRequestStructureAppOps, callingUid, callingPackage, callingAttributionTag, (java.lang.String) null) == 0 && allowFetchData2) {
                int numActivities = activityTokens.size();
                int i2 = 0;
                while (true) {
                    if (i2 >= numActivities) {
                        break;
                    }
                    android.os.IBinder topActivity = activityTokens.get(i2);
                    try {
                        com.android.internal.logging.MetricsLogger.count(this.mContext, "assist_with_context", 1);
                        android.os.Bundle receiverExtras = new android.os.Bundle();
                        receiverExtras.putInt(KEY_RECEIVER_EXTRA_INDEX, i2);
                        receiverExtras.putInt(KEY_RECEIVER_EXTRA_COUNT, numActivities);
                        if (requestAutofillData) {
                            try {
                                result = this.mActivityTaskManager.requestAutofillData(this, receiverExtras, topActivity, 0);
                                i = i2;
                            } catch (android.os.RemoteException e2) {
                                i = i2;
                            }
                        } else {
                            int requestType = fetchStructure ? 1 : 3;
                            i = i2;
                            result = this.mActivityTaskManager.requestAssistContextExtras(requestType, this, receiverExtras, topActivity, i2 == 0 && !ignoreTopActivityCheck, i2 == 0);
                        }
                        if (result) {
                            try {
                                this.mPendingDataCount++;
                            } catch (android.os.RemoteException e3) {
                            }
                        } else if (i == 0) {
                            if (!this.mCallbacks.canHandleReceivedAssistDataLocked()) {
                                this.mAssistData.add(null);
                            } else {
                                dispatchAssistDataReceived(null);
                            }
                            allowFetchScreenshot2 = false;
                        }
                    } catch (android.os.RemoteException e4) {
                        i = i2;
                    }
                    i2 = i + 1;
                }
            } else {
                if (!this.mCallbacks.canHandleReceivedAssistDataLocked()) {
                    this.mAssistData.add(null);
                } else {
                    dispatchAssistDataReceived(null);
                }
                allowFetchScreenshot2 = false;
            }
        }
        if (fetchScreenshot) {
            if (this.mAppOpsManager.noteOpNoThrow(this.mRequestScreenshotAppOps, callingUid, callingPackage, callingAttributionTag, (java.lang.String) null) == 0 && allowFetchScreenshot2) {
                try {
                    com.android.internal.logging.MetricsLogger.count(this.mContext, "assist_with_screen", 1);
                    this.mPendingScreenshotCount++;
                    this.mWindowManager.requestAssistScreenshot(this);
                } catch (android.os.RemoteException e5) {
                }
            } else if (!this.mCallbacks.canHandleReceivedAssistDataLocked()) {
                this.mAssistScreenshot.add(null);
            } else {
                dispatchAssistScreenshotReceived(null);
            }
        }
        tryDispatchRequestComplete();
    }

    public void processPendingAssistData() {
        flushPendingAssistData();
        tryDispatchRequestComplete();
    }

    private void flushPendingAssistData() {
        int dataCount = this.mAssistData.size();
        for (int i = 0; i < dataCount; i++) {
            dispatchAssistDataReceived(this.mAssistData.get(i));
        }
        this.mAssistData.clear();
        int screenshotsCount = this.mAssistScreenshot.size();
        for (int i2 = 0; i2 < screenshotsCount; i2++) {
            dispatchAssistScreenshotReceived(this.mAssistScreenshot.get(i2));
        }
        this.mAssistScreenshot.clear();
    }

    public int getPendingDataCount() {
        return this.mPendingDataCount;
    }

    public int getPendingScreenshotCount() {
        return this.mPendingScreenshotCount;
    }

    public void cancel() {
        this.mCanceled = true;
        this.mPendingDataCount = 0;
        this.mPendingScreenshotCount = 0;
        this.mAssistData.clear();
        this.mAssistScreenshot.clear();
    }

    public void onHandleAssistData(android.os.Bundle data) {
        synchronized (this.mCallbacksLock) {
            if (this.mCanceled) {
                return;
            }
            this.mPendingDataCount--;
            if (this.mCallbacks.canHandleReceivedAssistDataLocked()) {
                flushPendingAssistData();
                dispatchAssistDataReceived(data);
                tryDispatchRequestComplete();
            } else {
                this.mAssistData.add(data);
            }
        }
    }

    public void onHandleAssistScreenshot(android.graphics.Bitmap screenshot) {
        synchronized (this.mCallbacksLock) {
            if (this.mCanceled) {
                return;
            }
            this.mPendingScreenshotCount--;
            if (this.mCallbacks.canHandleReceivedAssistDataLocked()) {
                flushPendingAssistData();
                dispatchAssistScreenshotReceived(screenshot);
                tryDispatchRequestComplete();
            } else {
                this.mAssistScreenshot.add(screenshot);
            }
        }
    }

    private void dispatchAssistDataReceived(android.os.Bundle data) {
        int activityIndex = 0;
        int activityCount = 0;
        android.os.Bundle receiverExtras = data != null ? data.getBundle(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_RECEIVER_EXTRAS) : null;
        if (receiverExtras != null) {
            activityIndex = receiverExtras.getInt(KEY_RECEIVER_EXTRA_INDEX);
            activityCount = receiverExtras.getInt(KEY_RECEIVER_EXTRA_COUNT);
        }
        this.mCallbacks.onAssistDataReceivedLocked(data, activityIndex, activityCount);
    }

    private void dispatchAssistScreenshotReceived(android.graphics.Bitmap screenshot) {
        this.mCallbacks.onAssistScreenshotReceivedLocked(screenshot);
    }

    private void tryDispatchRequestComplete() {
        if (this.mPendingDataCount == 0 && this.mPendingScreenshotCount == 0 && this.mAssistData.isEmpty() && this.mAssistScreenshot.isEmpty()) {
            this.mCallbacks.onAssistRequestCompleted();
        }
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("mPendingDataCount=");
        pw.println(this.mPendingDataCount);
        pw.print(prefix);
        pw.print("mAssistData=");
        pw.println(this.mAssistData);
        pw.print(prefix);
        pw.print("mPendingScreenshotCount=");
        pw.println(this.mPendingScreenshotCount);
        pw.print(prefix);
        pw.print("mAssistScreenshot=");
        pw.println(this.mAssistScreenshot);
    }
}
