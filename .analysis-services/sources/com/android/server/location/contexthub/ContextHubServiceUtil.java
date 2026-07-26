package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
class ContextHubServiceUtil {
    private static final java.lang.String CONTEXT_HUB_PERMISSION = "android.permission.ACCESS_CONTEXT_HUB";
    private static final java.lang.String DATE_FORMAT = "MM/dd HH:mm:ss.SSS";
    private static final java.time.format.DateTimeFormatter DATE_FORMATTER = java.time.format.DateTimeFormatter.ofPattern(DATE_FORMAT).withZone(java.time.ZoneId.systemDefault());
    private static final char HOST_ENDPOINT_BROADCAST = 65535;
    private static final java.lang.String TAG = "ContextHubServiceUtil";

    ContextHubServiceUtil() {
    }

    static java.util.HashMap<java.lang.Integer, android.hardware.location.ContextHubInfo> createContextHubInfoMap(java.util.List<android.hardware.location.ContextHubInfo> hubList) {
        java.util.HashMap<java.lang.Integer, android.hardware.location.ContextHubInfo> contextHubIdToInfoMap = new java.util.HashMap<>();
        for (android.hardware.location.ContextHubInfo contextHubInfo : hubList) {
            contextHubIdToInfoMap.put(java.lang.Integer.valueOf(contextHubInfo.getId()), contextHubInfo);
        }
        return contextHubIdToInfoMap;
    }

    static void copyToByteArrayList(byte[] inputArray, java.util.ArrayList<java.lang.Byte> outputArray) {
        outputArray.clear();
        outputArray.ensureCapacity(inputArray.length);
        for (byte element : inputArray) {
            outputArray.add(java.lang.Byte.valueOf(element));
        }
    }

    static byte[] createPrimitiveByteArray(java.util.ArrayList<java.lang.Byte> array) {
        byte[] primitiveArray = new byte[array.size()];
        for (int i = 0; i < array.size(); i++) {
            primitiveArray[i] = array.get(i).byteValue();
        }
        return primitiveArray;
    }

    static int[] createPrimitiveIntArray(java.util.Collection<java.lang.Integer> collection) {
        int[] primitiveArray = new int[collection.size()];
        int i = 0;
        java.util.Iterator<java.lang.Integer> it = collection.iterator();
        while (it.hasNext()) {
            int contextHubId = it.next().intValue();
            primitiveArray[i] = contextHubId;
            i++;
        }
        return primitiveArray;
    }

    static android.hardware.contexthub.V1_0.NanoAppBinary createHidlNanoAppBinary(android.hardware.location.NanoAppBinary nanoAppBinary) {
        android.hardware.contexthub.V1_0.NanoAppBinary hidlNanoAppBinary = new android.hardware.contexthub.V1_0.NanoAppBinary();
        hidlNanoAppBinary.appId = nanoAppBinary.getNanoAppId();
        hidlNanoAppBinary.appVersion = nanoAppBinary.getNanoAppVersion();
        hidlNanoAppBinary.flags = nanoAppBinary.getFlags();
        hidlNanoAppBinary.targetChreApiMajorVersion = nanoAppBinary.getTargetChreApiMajorVersion();
        hidlNanoAppBinary.targetChreApiMinorVersion = nanoAppBinary.getTargetChreApiMinorVersion();
        try {
            copyToByteArrayList(nanoAppBinary.getBinaryNoHeader(), hidlNanoAppBinary.customBinary);
        } catch (java.lang.IndexOutOfBoundsException e) {
            android.util.Log.w(TAG, e.getMessage());
        } catch (java.lang.NullPointerException e2) {
            android.util.Log.w(TAG, "NanoApp binary was null");
        }
        return hidlNanoAppBinary;
    }

    static android.hardware.contexthub.NanoappBinary createAidlNanoAppBinary(android.hardware.location.NanoAppBinary nanoAppBinary) {
        android.hardware.contexthub.NanoappBinary aidlNanoAppBinary = new android.hardware.contexthub.NanoappBinary();
        aidlNanoAppBinary.nanoappId = nanoAppBinary.getNanoAppId();
        aidlNanoAppBinary.nanoappVersion = nanoAppBinary.getNanoAppVersion();
        aidlNanoAppBinary.flags = nanoAppBinary.getFlags();
        aidlNanoAppBinary.targetChreApiMajorVersion = nanoAppBinary.getTargetChreApiMajorVersion();
        aidlNanoAppBinary.targetChreApiMinorVersion = nanoAppBinary.getTargetChreApiMinorVersion();
        aidlNanoAppBinary.customBinary = new byte[0];
        try {
            aidlNanoAppBinary.customBinary = nanoAppBinary.getBinaryNoHeader();
        } catch (java.lang.IndexOutOfBoundsException e) {
            android.util.Log.w(TAG, e.getMessage());
        } catch (java.lang.NullPointerException e2) {
            android.util.Log.w(TAG, "NanoApp binary was null");
        }
        return aidlNanoAppBinary;
    }

    static java.util.List<android.hardware.location.NanoAppState> createNanoAppStateList(java.util.List<android.hardware.contexthub.V1_2.HubAppInfo> nanoAppInfoList) {
        java.util.ArrayList<android.hardware.location.NanoAppState> nanoAppStateList = new java.util.ArrayList<>();
        for (android.hardware.contexthub.V1_2.HubAppInfo appInfo : nanoAppInfoList) {
            nanoAppStateList.add(new android.hardware.location.NanoAppState(appInfo.info_1_0.appId, appInfo.info_1_0.version, appInfo.info_1_0.enabled, appInfo.permissions));
        }
        return nanoAppStateList;
    }

    static java.util.List<android.hardware.location.NanoAppState> createNanoAppStateList(android.hardware.contexthub.NanoappInfo[] nanoAppInfoList) {
        java.util.ArrayList<android.hardware.location.NanoAppState> nanoAppStateList = new java.util.ArrayList<>();
        for (android.hardware.contexthub.NanoappInfo appInfo : nanoAppInfoList) {
            java.util.ArrayList<android.hardware.location.NanoAppRpcService> rpcServiceList = new java.util.ArrayList<>();
            for (android.hardware.contexthub.NanoappRpcService service : appInfo.rpcServices) {
                rpcServiceList.add(new android.hardware.location.NanoAppRpcService(service.id, service.version));
            }
            nanoAppStateList.add(new android.hardware.location.NanoAppState(appInfo.nanoappId, appInfo.nanoappVersion, appInfo.enabled, new java.util.ArrayList(java.util.Arrays.asList(appInfo.permissions)), rpcServiceList));
        }
        return nanoAppStateList;
    }

    static android.hardware.contexthub.V1_0.ContextHubMsg createHidlContextHubMessage(short hostEndPoint, android.hardware.location.NanoAppMessage message) {
        android.hardware.contexthub.V1_0.ContextHubMsg hidlMessage = new android.hardware.contexthub.V1_0.ContextHubMsg();
        hidlMessage.appName = message.getNanoAppId();
        hidlMessage.hostEndPoint = hostEndPoint;
        hidlMessage.msgType = message.getMessageType();
        copyToByteArrayList(message.getMessageBody(), hidlMessage.msg);
        return hidlMessage;
    }

    static android.hardware.contexthub.ContextHubMessage createAidlContextHubMessage(short hostEndPoint, android.hardware.location.NanoAppMessage message) {
        android.hardware.contexthub.ContextHubMessage aidlMessage = new android.hardware.contexthub.ContextHubMessage();
        aidlMessage.nanoappId = message.getNanoAppId();
        aidlMessage.hostEndPoint = (char) hostEndPoint;
        aidlMessage.messageType = message.getMessageType();
        aidlMessage.messageBody = message.getMessageBody();
        aidlMessage.permissions = new java.lang.String[0];
        aidlMessage.isReliable = message.isReliable();
        aidlMessage.messageSequenceNumber = message.getMessageSequenceNumber();
        return aidlMessage;
    }

    static android.hardware.location.NanoAppMessage createNanoAppMessage(android.hardware.contexthub.V1_0.ContextHubMsg message) {
        byte[] messageArray = createPrimitiveByteArray(message.msg);
        return android.hardware.location.NanoAppMessage.createMessageFromNanoApp(message.appName, message.msgType, messageArray, message.hostEndPoint == -1);
    }

    static android.hardware.location.NanoAppMessage createNanoAppMessage(android.hardware.contexthub.ContextHubMessage message) {
        return android.hardware.location.NanoAppMessage.createMessageFromNanoApp(message.nanoappId, message.messageType, message.messageBody, message.hostEndPoint == 65535, message.isReliable, message.messageSequenceNumber);
    }

    static void checkPermissions(android.content.Context context) {
        context.enforceCallingOrSelfPermission(CONTEXT_HUB_PERMISSION, "ACCESS_CONTEXT_HUB permission required to use Context Hub");
    }

    static int toTransactionResult(int halResult) {
        switch (halResult) {
            case 0:
                return 0;
            case 1:
            case 4:
            default:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 5:
                return 4;
        }
    }

    static java.util.ArrayList<android.hardware.contexthub.V1_2.HubAppInfo> toHubAppInfo_1_2(java.util.ArrayList<android.hardware.contexthub.V1_0.HubAppInfo> oldInfoList) {
        java.util.ArrayList<android.hardware.contexthub.V1_2.HubAppInfo> arrayList = new java.util.ArrayList<>();
        for (android.hardware.contexthub.V1_0.HubAppInfo oldInfo : oldInfoList) {
            android.hardware.contexthub.V1_2.HubAppInfo newInfo = new android.hardware.contexthub.V1_2.HubAppInfo();
            newInfo.info_1_0.appId = oldInfo.appId;
            newInfo.info_1_0.version = oldInfo.version;
            newInfo.info_1_0.memUsage = oldInfo.memUsage;
            newInfo.info_1_0.enabled = oldInfo.enabled;
            newInfo.permissions = new java.util.ArrayList();
            arrayList.add(newInfo);
        }
        return arrayList;
    }

    static int toContextHubEvent(int hidlEventType) {
        switch (hidlEventType) {
            case 1:
                return 1;
            default:
                android.util.Log.e(TAG, "toContextHubEvent: Unknown event type: " + hidlEventType);
                return 0;
        }
    }

    static int toContextHubEventFromAidl(int aidlEventType) {
        switch (aidlEventType) {
            case 1:
                return 1;
            default:
                android.util.Log.e(TAG, "toContextHubEventFromAidl: Unknown event type: " + aidlEventType);
                return 0;
        }
    }

    static java.lang.String formatDateFromTimestamp(long timeStampInMs) {
        return DATE_FORMATTER.format(java.time.Instant.ofEpochMilli(timeStampInMs));
    }
}
