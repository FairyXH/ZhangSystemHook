package com.android.server.display.util;

/* JADX INFO: loaded from: classes2.dex */
public class AidlDisplayPanelFeature {
    private static final int DEFAULT_LENGTH = 1;
    public static final int FINGER_LAYER_HIDE = 0;
    public static final int FINGER_LAYER_SHOW = 1;
    private static final int GET_DISPLAY_PANEL_FEATURE_VALUE_PASS = 0;
    public static final int MTK_FINGER_LAYER_HIDE = 1;
    public static final int MTK_FINGER_LAYER_SHOW = 0;
    private static final int NOTIFY_DISP_FINGER_LAYER = 20004;
    public static final int OMMDPPANEL_ID = 5;
    private static final int OMMDPPANEL_ID_LENGTH = 5;
    public static final int OMMDP_FPPRESS = 28;
    private static final java.lang.String TAG = "AidlDisplayPanelFeature";
    private static vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature mDisplayPanelFeature = null;

    public static java.util.ArrayList<java.lang.Integer> getDisplayPanelFeatureValue(int featureID) {
        if (featureID == 5) {
            return getDisplayPanelFeatureValue(featureID, 5);
        }
        return getDisplayPanelFeatureValue(featureID, 1);
    }

    public static java.util.ArrayList<java.lang.Integer> getDisplayPanelFeatureValue(int featureID, int length) {
        android.util.Slog.d(TAG, "getDisplayPanelFeatureValue +++, featureID:" + featureID);
        java.util.ArrayList<java.lang.Integer> result = null;
        int[] tempResult = new int[length];
        com.android.server.display.util.PendingResult<java.lang.Integer> pendingResult = new com.android.server.display.util.PendingResult<>(-1);
        try {
            if (mDisplayPanelFeature == null) {
                getService();
            }
            if (mDisplayPanelFeature != null) {
                int temp = mDisplayPanelFeature.getDisplayPanelFeatureValue(featureID, tempResult);
                pendingResult.setResult(java.lang.Integer.valueOf(temp));
                int ret = pendingResult.await(10L, java.util.concurrent.TimeUnit.MILLISECONDS).intValue();
                if (ret == 0) {
                    result = new java.util.ArrayList<>();
                    for (int i : tempResult) {
                        result.add(java.lang.Integer.valueOf(i));
                    }
                    if (!result.isEmpty()) {
                        android.util.Slog.i(TAG, java.util.Arrays.toString(result.toArray()));
                    }
                } else {
                    android.util.Slog.e(TAG, "PendingResultTimeout getDisplayPanelFeatureValue, ret = " + ret);
                }
            } else {
                android.util.Slog.e(TAG, "achieve displayPanelFeature failed");
            }
        } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
            android.util.Slog.e(TAG, "getDisplayPanelFeatureValue error");
        }
        android.util.Slog.d(TAG, "getDisplayPanelFeatureValue ---, result = " + result);
        return result;
    }

    public static boolean isAvailable() {
        if (mDisplayPanelFeature == null) {
            getService();
        }
        if (mDisplayPanelFeature == null) {
            return false;
        }
        return true;
    }

    private static void getService() {
        try {
            android.os.IBinder b = android.os.ServiceManager.getService("vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature/default");
            mDisplayPanelFeature = vendor.oplus.hardware.displaypanelfeature.IDisplayPanelFeature.Stub.asInterface(b);
        } catch (java.util.NoSuchElementException e) {
            android.util.Slog.e(TAG, "getAidlDisplayPanelFeatureService error");
        }
    }

    public static java.lang.String getDisplayPanelFeatureValueAsString(int featureID) {
        java.lang.String result = "";
        java.util.ArrayList<java.lang.Integer> temp = getDisplayPanelFeatureValue(featureID);
        if (temp != null && !temp.isEmpty()) {
            java.util.Iterator<java.lang.Integer> it = temp.iterator();
            while (it.hasNext()) {
                int tem = it.next().intValue();
                result = result + tem + " ";
            }
            return result.trim();
        }
        return null;
    }

    public static int[] getDisplayPanelFeatureValueAsIntArray(int featureID) {
        int[] result = null;
        java.util.ArrayList<java.lang.Integer> temp = getDisplayPanelFeatureValue(featureID);
        if (temp != null && !temp.isEmpty()) {
            result = new int[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                result[i] = temp.get(i).intValue();
            }
        }
        return result;
    }

    public static int getDisplayPanelFeatureValueAsInt(int featureID) {
        java.util.ArrayList<java.lang.Integer> temp = getDisplayPanelFeatureValue(featureID);
        if (temp == null || temp.isEmpty()) {
            return -1;
        }
        int result = temp.get(0).intValue();
        return result;
    }

    public static boolean isFODHwDimlayer() {
        int type = getDisplayPanelFeatureValueAsInt(com.android.internal.util.FrameworkStatsLog.DEVICE_POLICY_EVENT__EVENT_ID__ROLE_HOLDER_UPDATER_UPDATE_RETRY);
        return (type == -1 || (type & 4) == 0) ? false : true;
    }

    public static void setDisplayPanelFeatureValue(int featureID, int mode) {
        android.util.Slog.d(TAG, "setDisplayPanelFeatureValue +++, ID:" + featureID + ", value:" + mode);
        if (featureID == 28) {
            try {
                if (!isFODHwDimlayer()) {
                    setDisplayPanelFeatureValueForMtk(featureID, mode);
                    return;
                }
            } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
                android.util.Slog.e(TAG, "setDisplayPanelFeatureValue error");
                return;
            }
        }
        int[] modes = {mode};
        if (mDisplayPanelFeature == null) {
            getService();
        }
        if (mDisplayPanelFeature != null) {
            mDisplayPanelFeature.setDisplayPanelFeatureValue(featureID, modes);
        }
    }

    public static void setDisplayPanelFeatureValueArray(int featureID, int[] modes) {
        if (modes == null) {
            return;
        }
        try {
            if (mDisplayPanelFeature == null) {
                getService();
            }
            if (mDisplayPanelFeature != null) {
                mDisplayPanelFeature.setDisplayPanelFeatureValue(featureID, modes);
            }
        } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
            android.util.Slog.e(TAG, "setDisplayPanelFeatureValue error");
        }
    }

    private static int notifyDispFingerLayer(int value) {
        int result = -1;
        android.util.Slog.d(TAG, "notifyDispFingerLayer " + value);
        try {
            android.os.IBinder flinger = android.os.ServiceManager.getService("SurfaceFlinger");
            if (flinger != null) {
                synchronized (flinger) {
                    android.os.Parcel data = android.os.Parcel.obtain();
                    android.os.Parcel reply = android.os.Parcel.obtain();
                    data.writeInterfaceToken("android.ui.ISurfaceComposer");
                    data.writeInt(value);
                    flinger.transact(20004, data, reply, 0);
                    result = reply.readInt();
                    data.recycle();
                    reply.recycle();
                }
                return result;
            }
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(TAG, "notifyDispFingerLayer exception " + ex);
        }
        return result;
    }

    public static int setDisplayPanelFeatureValueForMtk(int featureID, int mode) {
        int value;
        switch (featureID) {
            case 28:
                if (mode == 1) {
                    value = 0;
                } else {
                    value = 1;
                }
                int result = notifyDispFingerLayer(value);
                return result;
            default:
                return -1;
        }
    }

    public static java.util.ArrayList<java.lang.String> getDisplayPanelInfo(int featureID) {
        android.util.Slog.d(TAG, "getDisplayPanelInfo +++, featureID:" + featureID);
        java.util.ArrayList<java.lang.String> result = new java.util.ArrayList<>();
        com.android.server.display.util.PendingResult<java.lang.Integer> pendingResult = new com.android.server.display.util.PendingResult<>(-1);
        try {
            if (mDisplayPanelFeature == null) {
                getService();
            }
            if (mDisplayPanelFeature != null) {
                int temp = mDisplayPanelFeature.getDisplayPanelInfo(featureID, result);
                pendingResult.setResult(java.lang.Integer.valueOf(temp));
                int ret = pendingResult.await(10L, java.util.concurrent.TimeUnit.MILLISECONDS).intValue();
                if (ret == 0) {
                    if (!result.isEmpty()) {
                        android.util.Slog.i(TAG, java.util.Arrays.toString(result.toArray()));
                    }
                } else {
                    android.util.Slog.e(TAG, "PendingResultTimeout getDisplayPanelInfo, ret = " + ret);
                }
            } else {
                android.util.Slog.e(TAG, "achieve getDisplayPanelInfo failed");
            }
        } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
            android.util.Slog.e(TAG, "getDisplayPanelInfo Error");
        }
        android.util.Slog.d(TAG, "getDisplayPanelInfo ---, result = " + result);
        return result;
    }

    public static java.lang.String getDisplayPanelInfoAsString(int featureID) {
        java.lang.String result = "";
        java.util.ArrayList<java.lang.String> temp = getDisplayPanelInfo(featureID);
        if (temp != null && !temp.isEmpty()) {
            for (java.lang.String tem : temp) {
                result = result + tem + " ";
            }
            return result.trim();
        }
        return null;
    }
}
