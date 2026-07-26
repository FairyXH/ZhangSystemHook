package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class BroadcastRecord extends android.os.Binder {
    static final int APP_RECEIVE = 1;
    static final int CALL_DONE_RECEIVE = 3;
    static final int CALL_IN_RECEIVE = 2;
    static final int DELIVERY_DEFERRED = 6;
    static final int DELIVERY_DELIVERED = 1;
    static final int DELIVERY_FAILURE = 5;
    static final int DELIVERY_PENDING = 0;
    static final int DELIVERY_SCHEDULED = 4;
    static final int DELIVERY_SKIPPED = 2;
    static final int DELIVERY_TIMEOUT = 3;
    static final int IDLE = 0;
    static final int WAITING_SERVICES = 4;
    final boolean alarm;
    int anrCount;
    final int appOp;
    int beyondCount;
    final int[] blockedUntilBeyondCount;
    final com.android.server.am.ProcessRecord callerApp;
    final java.lang.String callerFeatureId;
    final boolean callerInstantApp;
    final boolean callerInstrumented;
    final java.lang.String callerPackage;
    final int callerProcState;
    final int callingPid;
    final int callingUid;
    com.android.server.am.ProcessRecord curApp;
    android.content.ComponentName curComponent;
    com.android.server.am.BroadcastFilter curFilter;
    android.os.Bundle curFilteredExtras;
    android.content.pm.ActivityInfo curReceiver;
    final boolean deferUntilActive;
    int deferredCount;
    final int[] delivery;
    final java.lang.String[] deliveryReasons;
    long dispatchClockTime;
    long dispatchRealTime;
    long dispatchTime;
    long enqueueClockTime;
    long enqueueRealTime;
    long enqueueTime;
    final java.lang.String[] excludedPackages;
    final java.lang.String[] excludedPermissions;
    final java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver;
    long finishTime;
    final boolean initialSticky;
    final android.content.Intent intent;
    final boolean interactive;
    final android.app.BackgroundStartPrivileges mBackgroundStartPrivileges;
    private com.android.server.am.IBroadcastRecordExt mBroadcastRecordExt;
    private java.lang.String mCachedToShortString;
    private java.lang.String mCachedToString;
    private android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> mMatchingRecordsCache;
    private com.android.server.am.BroadcastRecord.BroadcastRecordWrapper mWrapper;
    int manifestCount;
    int manifestSkipCount;
    int nextReceiver;
    int oplusState;
    final android.app.BroadcastOptions options;
    final boolean ordered;
    long originalEnqueueClockTime;
    final int originalStickyCallingUid;
    final boolean prioritized;
    final boolean pushMessage;
    final boolean pushMessageOverQuota;
    com.android.server.am.BroadcastQueue queue;
    long receiverTime;
    final java.util.List<java.lang.Object> receivers;
    final java.lang.String[] requiredPermissions;
    final java.lang.String resolvedType;
    boolean resultAbort;
    int resultCode;
    java.lang.String resultData;
    android.os.Bundle resultExtras;
    android.content.IIntentReceiver resultTo;
    com.android.server.am.ProcessRecord resultToApp;
    final long[] scheduledTime;
    final boolean shareIdentity;
    int state;
    final boolean sticky;
    final android.content.ComponentName targetComp;
    int terminalCount;
    final long[] terminalTime;
    final boolean timeoutExempt;
    final boolean urgent;
    final int userId;
    static boolean CORE_DEFER_UNTIL_ACTIVE = false;
    static final java.util.List<java.lang.Object> EMPTY_RECEIVERS = java.util.List.of();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DeliveryState {
    }

    static java.lang.String deliveryStateToString(int deliveryState) {
        switch (deliveryState) {
            case 0:
                return "PENDING";
            case 1:
                return "DELIVERED";
            case 2:
                return "SKIPPED";
            case 3:
                return "TIMEOUT";
            case 4:
                return "SCHEDULED";
            case 5:
                return "FAILURE";
            case 6:
                return "DEFERRED";
            default:
                return java.lang.Integer.toString(deliveryState);
        }
    }

    static boolean isDeliveryStateTerminal(int deliveryState) {
        switch (deliveryState) {
            case 1:
            case 2:
            case 3:
            case 5:
                return true;
            case 4:
            default:
                return false;
        }
    }

    static boolean isDeliveryStateBeyond(int deliveryState) {
        switch (deliveryState) {
            case 1:
            case 2:
            case 3:
            case 5:
            case 6:
                return true;
            case 4:
            default:
                return false;
        }
    }

    boolean isAssumedDelivered(int index) {
        return (this.receivers.get(index) instanceof com.android.server.am.BroadcastFilter) && !this.ordered && this.resultTo == null;
    }

    @dalvik.annotation.optimization.NeverCompile
    void dump(java.io.PrintWriter pw, java.lang.String prefix, java.text.SimpleDateFormat sdf) {
        long now = android.os.SystemClock.uptimeMillis();
        pw.print(prefix);
        pw.print(this);
        pw.print(" to user ");
        pw.println(this.userId);
        pw.print(prefix);
        pw.println(this.intent.toInsecureString());
        if (this.targetComp != null && this.targetComp != this.intent.getComponent()) {
            pw.print(prefix);
            pw.print("  targetComp: ");
            pw.println(this.targetComp.toShortString());
        }
        android.os.Bundle bundle = this.intent.getExtras();
        if (bundle != null) {
            pw.print(prefix);
            pw.print("  extras: ");
            pw.println(bundle.toString());
        }
        pw.print(prefix);
        pw.print("caller=");
        pw.print(this.callerPackage);
        pw.print(" ");
        pw.print(this.callerApp != null ? this.callerApp.toShortString() : "null");
        pw.print(" pid=");
        pw.print(this.callingPid);
        pw.print(" uid=");
        pw.println(this.callingUid);
        if ((this.requiredPermissions != null && this.requiredPermissions.length > 0) || this.appOp != -1) {
            pw.print(prefix);
            pw.print("requiredPermissions=");
            pw.print(java.util.Arrays.toString(this.requiredPermissions));
            pw.print("  appOp=");
            pw.println(this.appOp);
        }
        if (this.excludedPermissions != null && this.excludedPermissions.length > 0) {
            pw.print(prefix);
            pw.print("excludedPermissions=");
            pw.print(java.util.Arrays.toString(this.excludedPermissions));
        }
        if (this.excludedPackages != null && this.excludedPackages.length > 0) {
            pw.print(prefix);
            pw.print("excludedPackages=");
            pw.print(java.util.Arrays.toString(this.excludedPackages));
        }
        if (this.options != null) {
            pw.print(prefix);
            pw.print("options=");
            pw.println(this.options.toBundle());
        }
        pw.print(prefix);
        pw.print("enqueueClockTime=");
        pw.print(sdf.format(new java.util.Date(this.enqueueClockTime)));
        pw.print(" dispatchClockTime=");
        pw.print(sdf.format(new java.util.Date(this.dispatchClockTime)));
        long j = 0;
        if (this.originalEnqueueClockTime > 0) {
            pw.print(" originalEnqueueClockTime=");
            pw.print(sdf.format(new java.util.Date(this.originalEnqueueClockTime)));
        }
        pw.println();
        pw.print(prefix);
        pw.print("dispatchTime=");
        android.util.TimeUtils.formatDuration(this.dispatchTime, now, pw);
        pw.print(" (");
        android.util.TimeUtils.formatDuration(this.dispatchTime - this.enqueueTime, pw);
        pw.print(" since enq)");
        if (this.finishTime != 0) {
            pw.print(" finishTime=");
            android.util.TimeUtils.formatDuration(this.finishTime, now, pw);
            pw.print(" (");
            android.util.TimeUtils.formatDuration(this.finishTime - this.dispatchTime, pw);
            pw.print(" since disp)");
        } else {
            pw.print(" receiverTime=");
            android.util.TimeUtils.formatDuration(this.receiverTime, now, pw);
        }
        pw.println("");
        if (this.anrCount != 0) {
            pw.print(prefix);
            pw.print("anrCount=");
            pw.println(this.anrCount);
        }
        if (this.resultTo != null || this.resultCode != -1 || this.resultData != null) {
            pw.print(prefix);
            pw.print("resultTo=");
            pw.print(this.resultTo);
            pw.print(" resultCode=");
            pw.print(this.resultCode);
            pw.print(" resultData=");
            pw.println(this.resultData);
        }
        if (this.resultExtras != null) {
            pw.print(prefix);
            pw.print("resultExtras=");
            pw.println(this.resultExtras);
        }
        if (this.resultAbort || this.ordered || this.sticky || this.initialSticky) {
            pw.print(prefix);
            pw.print("resultAbort=");
            pw.print(this.resultAbort);
            pw.print(" ordered=");
            pw.print(this.ordered);
            pw.print(" sticky=");
            pw.print(this.sticky);
            pw.print(" initialSticky=");
            pw.print(this.initialSticky);
            pw.print(" originalStickyCallingUid=");
            pw.println(this.originalStickyCallingUid);
        }
        if (this.nextReceiver != 0) {
            pw.print(prefix);
            pw.print("nextReceiver=");
            pw.println(this.nextReceiver);
        }
        if (this.curFilter != null) {
            pw.print(prefix);
            pw.print("curFilter=");
            pw.println(this.curFilter);
        }
        if (this.curReceiver != null) {
            pw.print(prefix);
            pw.print("curReceiver=");
            pw.println(this.curReceiver);
        }
        if (this.curApp != null) {
            pw.print(prefix);
            pw.print("curApp=");
            pw.println(this.curApp);
            pw.print(prefix);
            pw.print("curComponent=");
            pw.println(this.curComponent != null ? this.curComponent.toShortString() : "--");
            if (this.curReceiver != null && this.curReceiver.applicationInfo != null) {
                pw.print(prefix);
                pw.print("curSourceDir=");
                pw.println(this.curReceiver.applicationInfo.sourceDir);
            }
        }
        if (this.curFilteredExtras != null) {
            pw.print(" filtered extras: ");
            pw.println(this.curFilteredExtras);
        }
        if (this.state != 0) {
            java.lang.String stateStr = " (?)";
            switch (this.state) {
                case 1:
                    stateStr = " (APP_RECEIVE)";
                    break;
                case 2:
                    stateStr = " (CALL_IN_RECEIVE)";
                    break;
                case 3:
                    stateStr = " (CALL_DONE_RECEIVE)";
                    break;
                case 4:
                    stateStr = " (WAITING_SERVICES)";
                    break;
            }
            pw.print(prefix);
            pw.print("state=");
            pw.print(this.state);
            pw.println(stateStr);
        }
        pw.print(prefix);
        pw.print("terminalCount=");
        pw.println(this.terminalCount);
        int N = this.receivers != null ? this.receivers.size() : 0;
        java.lang.String p2 = prefix + "  ";
        android.util.PrintWriterPrinter printer = new android.util.PrintWriterPrinter(pw);
        int i = 0;
        while (i < N) {
            java.lang.Object o = this.receivers.get(i);
            pw.print(prefix);
            pw.print(deliveryStateToString(this.delivery[i]));
            pw.print(' ');
            if (this.scheduledTime[i] != j) {
                pw.print("scheduled ");
                android.util.TimeUtils.formatDuration(this.scheduledTime[i] - this.enqueueTime, pw);
                pw.print(' ');
            }
            if (this.terminalTime[i] != 0) {
                pw.print("terminal ");
                android.util.TimeUtils.formatDuration(this.terminalTime[i] - this.scheduledTime[i], pw);
                pw.print(' ');
            }
            pw.print("(");
            pw.print(this.blockedUntilBeyondCount[i]);
            pw.print(") ");
            pw.print("#");
            pw.print(i);
            pw.print(": ");
            if (o instanceof com.android.server.am.BroadcastFilter) {
                pw.println(o);
                ((com.android.server.am.BroadcastFilter) o).dumpBrief(pw, p2);
            } else if (o instanceof android.content.pm.ResolveInfo) {
                pw.println("(manifest)");
                ((android.content.pm.ResolveInfo) o).dump(printer, p2, 0);
            } else {
                pw.println(o);
            }
            if (this.deliveryReasons[i] != null) {
                pw.print(p2);
                pw.print("reason: ");
                pw.println(this.deliveryReasons[i]);
            }
            this.mBroadcastRecordExt.dumpDeliveryState(pw, i);
            i++;
            j = 0;
        }
    }

    BroadcastRecord(com.android.server.am.BroadcastQueue queue, android.content.Intent intent, com.android.server.am.ProcessRecord callerApp, java.lang.String callerPackage, java.lang.String callerFeatureId, int callingPid, int callingUid, boolean callerInstantApp, java.lang.String resolvedType, java.lang.String[] requiredPermissions, java.lang.String[] excludedPermissions, java.lang.String[] excludedPackages, int appOp, android.app.BroadcastOptions options, java.util.List receivers, com.android.server.am.ProcessRecord resultToApp, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, boolean serialized, boolean sticky, boolean initialSticky, int userId, android.app.BackgroundStartPrivileges backgroundStartPrivileges, boolean timeoutExempt, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, int callerAppProcessState) {
        this(queue, intent, callerApp, callerPackage, callerFeatureId, callingPid, callingUid, callerInstantApp, resolvedType, requiredPermissions, excludedPermissions, excludedPackages, appOp, options, receivers, resultToApp, resultTo, resultCode, resultData, resultExtras, serialized, sticky, initialSticky, userId, -1, backgroundStartPrivileges, timeoutExempt, filterExtrasForReceiver, callerAppProcessState);
    }

    BroadcastRecord(com.android.server.am.BroadcastQueue _queue, android.content.Intent _intent, com.android.server.am.ProcessRecord _callerApp, java.lang.String _callerPackage, java.lang.String _callerFeatureId, int _callingPid, int _callingUid, boolean _callerInstantApp, java.lang.String _resolvedType, java.lang.String[] _requiredPermissions, java.lang.String[] _excludedPermissions, java.lang.String[] _excludedPackages, int _appOp, android.app.BroadcastOptions _options, java.util.List _receivers, com.android.server.am.ProcessRecord _resultToApp, android.content.IIntentReceiver _resultTo, int _resultCode, java.lang.String _resultData, android.os.Bundle _resultExtras, boolean _serialized, boolean _sticky, boolean _initialSticky, int _userId, int originalStickyCallingUid, android.app.BackgroundStartPrivileges backgroundStartPrivileges, boolean timeoutExempt, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasForReceiver, int callerAppProcessState) {
        this.mWrapper = new com.android.server.am.BroadcastRecord.BroadcastRecordWrapper();
        this.mBroadcastRecordExt = null;
        if (_intent == null) {
            throw new java.lang.NullPointerException("Can't construct with a null intent");
        }
        this.mBroadcastRecordExt = (com.android.server.am.IBroadcastRecordExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IBroadcastRecordExt.class).base(this).create();
        this.queue = _queue;
        this.intent = (android.content.Intent) java.util.Objects.requireNonNull(_intent);
        this.targetComp = _intent.getComponent();
        this.callerApp = _callerApp;
        this.callerPackage = _callerPackage;
        this.callerFeatureId = _callerFeatureId;
        this.callingPid = _callingPid;
        this.callingUid = _callingUid;
        this.callerProcState = callerAppProcessState;
        this.callerInstantApp = _callerInstantApp;
        this.callerInstrumented = isCallerInstrumented(_callerApp, _callingUid);
        this.resolvedType = _resolvedType;
        this.requiredPermissions = _requiredPermissions;
        this.excludedPermissions = _excludedPermissions;
        this.excludedPackages = _excludedPackages;
        this.appOp = _appOp;
        this.options = _options;
        this.receivers = _receivers != null ? _receivers : EMPTY_RECEIVERS;
        this.delivery = new int[_receivers != null ? _receivers.size() : 0];
        this.deliveryReasons = new java.lang.String[this.delivery.length];
        this.urgent = calculateUrgent(_intent, _options);
        this.deferUntilActive = calculateDeferUntilActive(_callingUid, _options, _resultTo, _serialized, this.urgent);
        this.blockedUntilBeyondCount = this.mBroadcastRecordExt.calculateBlockedUntilBeyondCount(this.receivers, _serialized, this.intent);
        this.scheduledTime = new long[this.delivery.length];
        this.terminalTime = new long[this.delivery.length];
        this.resultToApp = _resultToApp;
        this.resultTo = _resultTo;
        this.resultCode = _resultCode;
        this.resultData = _resultData;
        this.resultExtras = _resultExtras;
        this.ordered = _serialized;
        this.sticky = _sticky;
        this.initialSticky = _initialSticky;
        this.prioritized = isPrioritized(this.blockedUntilBeyondCount, _serialized);
        this.userId = _userId;
        this.nextReceiver = 0;
        this.state = 0;
        this.mBackgroundStartPrivileges = backgroundStartPrivileges;
        this.timeoutExempt = timeoutExempt;
        this.alarm = this.options != null && this.options.isAlarmBroadcast();
        this.pushMessage = this.options != null && this.options.isPushMessagingBroadcast();
        this.pushMessageOverQuota = this.options != null && this.options.isPushMessagingOverQuotaBroadcast();
        this.interactive = this.options != null && this.options.isInteractive();
        this.shareIdentity = this.options != null && this.options.isShareIdentityEnabled();
        this.filterExtrasForReceiver = filterExtrasForReceiver;
        this.originalStickyCallingUid = originalStickyCallingUid;
        this.mBroadcastRecordExt.init(this.delivery.length);
    }

    private BroadcastRecord(com.android.server.am.BroadcastRecord from, android.content.Intent newIntent) {
        this.mWrapper = new com.android.server.am.BroadcastRecord.BroadcastRecordWrapper();
        this.mBroadcastRecordExt = null;
        this.intent = (android.content.Intent) java.util.Objects.requireNonNull(newIntent);
        this.targetComp = newIntent.getComponent();
        this.callerApp = from.callerApp;
        this.callerPackage = from.callerPackage;
        this.callerFeatureId = from.callerFeatureId;
        this.callingPid = from.callingPid;
        this.callingUid = from.callingUid;
        this.callerProcState = from.callerProcState;
        this.callerInstantApp = from.callerInstantApp;
        this.callerInstrumented = from.callerInstrumented;
        this.ordered = from.ordered;
        this.sticky = from.sticky;
        this.initialSticky = from.initialSticky;
        this.prioritized = from.prioritized;
        this.userId = from.userId;
        this.resolvedType = from.resolvedType;
        this.requiredPermissions = from.requiredPermissions;
        this.excludedPermissions = from.excludedPermissions;
        this.excludedPackages = from.excludedPackages;
        this.appOp = from.appOp;
        this.options = from.options;
        this.receivers = from.receivers;
        this.delivery = from.delivery;
        this.deliveryReasons = from.deliveryReasons;
        this.deferUntilActive = from.deferUntilActive;
        this.blockedUntilBeyondCount = from.blockedUntilBeyondCount;
        this.scheduledTime = from.scheduledTime;
        this.terminalTime = from.terminalTime;
        this.resultToApp = from.resultToApp;
        this.resultTo = from.resultTo;
        this.enqueueTime = from.enqueueTime;
        this.enqueueRealTime = from.enqueueRealTime;
        this.enqueueClockTime = from.enqueueClockTime;
        this.dispatchTime = from.dispatchTime;
        this.dispatchRealTime = from.dispatchRealTime;
        this.dispatchClockTime = from.dispatchClockTime;
        this.receiverTime = from.receiverTime;
        this.finishTime = from.finishTime;
        this.resultCode = from.resultCode;
        this.resultData = from.resultData;
        this.resultExtras = from.resultExtras;
        this.resultAbort = from.resultAbort;
        this.nextReceiver = from.nextReceiver;
        this.state = from.state;
        this.anrCount = from.anrCount;
        this.manifestCount = from.manifestCount;
        this.manifestSkipCount = from.manifestSkipCount;
        this.queue = from.queue;
        this.mBackgroundStartPrivileges = from.mBackgroundStartPrivileges;
        this.timeoutExempt = from.timeoutExempt;
        this.alarm = from.alarm;
        this.pushMessage = from.pushMessage;
        this.pushMessageOverQuota = from.pushMessageOverQuota;
        this.interactive = from.interactive;
        this.shareIdentity = from.shareIdentity;
        this.urgent = from.urgent;
        this.filterExtrasForReceiver = from.filterExtrasForReceiver;
        this.originalStickyCallingUid = from.originalStickyCallingUid;
        this.mBroadcastRecordExt = from.mBroadcastRecordExt;
    }

    boolean setDeliveryState(int index, int newDeliveryState, java.lang.String reason) {
        int oldDeliveryState = this.delivery[index];
        if (isDeliveryStateTerminal(oldDeliveryState) || newDeliveryState == oldDeliveryState) {
            return false;
        }
        switch (oldDeliveryState) {
            case 6:
                this.deferredCount--;
                break;
        }
        switch (newDeliveryState) {
            case 0:
                this.scheduledTime[index] = 0;
                break;
            case 1:
            case 2:
            case 3:
            case 5:
                this.terminalTime[index] = android.os.SystemClock.uptimeMillis();
                this.terminalCount++;
                this.mBroadcastRecordExt.setSkipReason(this, index, newDeliveryState, reason);
                break;
            case 4:
                this.scheduledTime[index] = android.os.SystemClock.uptimeMillis();
                break;
            case 6:
                this.deferredCount++;
                break;
        }
        this.delivery[index] = newDeliveryState;
        this.deliveryReasons[index] = reason;
        int oldBeyondCount = this.beyondCount;
        if (index >= this.beyondCount) {
            for (int i = this.beyondCount; i < this.delivery.length && isDeliveryStateBeyond(getDeliveryState(i)); i++) {
                this.beyondCount = i + 1;
            }
        }
        int i2 = this.beyondCount;
        return i2 != oldBeyondCount;
    }

    int getDeliveryState(int index) {
        return this.delivery[index];
    }

    boolean isBlocked(int index) {
        return this.beyondCount < this.blockedUntilBeyondCount[index];
    }

    boolean wasDeliveryAttempted(int index) {
        int deliveryState = getDeliveryState(index);
        switch (deliveryState) {
            case 1:
            case 3:
            case 5:
                return true;
            case 2:
            case 4:
            default:
                return false;
        }
    }

    void copyEnqueueTimeFrom(com.android.server.am.BroadcastRecord replacedBroadcast) {
        this.originalEnqueueClockTime = this.enqueueClockTime;
        this.enqueueTime = replacedBroadcast.enqueueTime;
        this.enqueueRealTime = replacedBroadcast.enqueueRealTime;
        this.enqueueClockTime = replacedBroadcast.enqueueClockTime;
    }

    boolean isForeground() {
        return (this.intent.getFlags() & 268435456) != 0;
    }

    boolean isReplacePending() {
        return (this.intent.getFlags() & 536870912) != 0;
    }

    boolean isNoAbort() {
        return (this.intent.getFlags() & 134217728) != 0;
    }

    boolean isOffload() {
        return (this.intent.getFlags() & Integer.MIN_VALUE) != 0;
    }

    boolean isDeferUntilActive() {
        return this.deferUntilActive;
    }

    boolean isUrgent() {
        return this.urgent;
    }

    java.lang.String getHostingRecordTriggerType() {
        if (this.alarm) {
            return com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM;
        }
        if (this.pushMessage) {
            return com.android.server.am.HostingRecord.TRIGGER_TYPE_PUSH_MESSAGE;
        }
        if (this.pushMessageOverQuota) {
            return com.android.server.am.HostingRecord.TRIGGER_TYPE_PUSH_MESSAGE_OVER_QUOTA;
        }
        return "unknown";
    }

    android.content.Intent getReceiverIntent(java.lang.Object receiver) {
        android.os.Bundle extras;
        android.content.Intent newIntent = null;
        if (this.filterExtrasForReceiver != null && (extras = this.intent.getExtras()) != null) {
            int receiverUid = getReceiverUid(receiver);
            android.os.Bundle filteredExtras = this.filterExtrasForReceiver.apply(java.lang.Integer.valueOf(receiverUid), extras);
            if (filteredExtras == null) {
                return null;
            }
            newIntent = new android.content.Intent(this.intent);
            newIntent.replaceExtras(filteredExtras);
        }
        if (receiver instanceof android.content.pm.ResolveInfo) {
            if (newIntent == null) {
                newIntent = new android.content.Intent(this.intent);
            }
            newIntent.setComponent(((android.content.pm.ResolveInfo) receiver).activityInfo.getComponentName());
        }
        return newIntent != null ? newIntent : this.intent;
    }

    static boolean isCallerInstrumented(com.android.server.am.ProcessRecord callerApp, int callingUid) {
        switch (android.os.UserHandle.getAppId(callingUid)) {
            case 0:
            case 2000:
                return true;
            default:
                return (callerApp == null || callerApp.getActiveInstrumentation() == null) ? false : true;
        }
    }

    static boolean isPrioritized(int[] blockedUntilBeyondCount, boolean ordered) {
        return (ordered || blockedUntilBeyondCount.length <= 0 || blockedUntilBeyondCount[0] == -1) ? false : true;
    }

    static int[] calculateBlockedUntilBeyondCount(java.util.List<java.lang.Object> receivers, boolean ordered) {
        int N = receivers.size();
        int[] blockedUntilBeyondCount = new int[N];
        int lastPriority = 0;
        int lastPriorityIndex = 0;
        for (int i = 0; i < N; i++) {
            if (ordered) {
                blockedUntilBeyondCount[i] = i;
            } else {
                int thisPriority = getReceiverPriority(receivers.get(i));
                if (i == 0 || thisPriority != lastPriority) {
                    lastPriority = thisPriority;
                    lastPriorityIndex = i;
                    blockedUntilBeyondCount[i] = i;
                } else {
                    blockedUntilBeyondCount[i] = lastPriorityIndex;
                }
            }
        }
        if (N > 0 && blockedUntilBeyondCount[N - 1] == 0) {
            java.util.Arrays.fill(blockedUntilBeyondCount, -1);
        }
        return blockedUntilBeyondCount;
    }

    static int getReceiverUid(java.lang.Object receiver) {
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            return ((com.android.server.am.BroadcastFilter) receiver).owningUid;
        }
        return ((android.content.pm.ResolveInfo) receiver).activityInfo.applicationInfo.uid;
    }

    static java.lang.String getReceiverProcessName(java.lang.Object receiver) {
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            return ((com.android.server.am.BroadcastFilter) receiver).receiverList.app.processName;
        }
        return ((android.content.pm.ResolveInfo) receiver).activityInfo.processName;
    }

    static java.lang.String getReceiverPackageName(java.lang.Object receiver) {
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            return ((com.android.server.am.BroadcastFilter) receiver).receiverList.app.info.packageName;
        }
        return ((android.content.pm.ResolveInfo) receiver).activityInfo.packageName;
    }

    static java.lang.String getReceiverClassName(java.lang.Object receiver) {
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            return ((com.android.server.am.BroadcastFilter) receiver).getReceiverClassName();
        }
        return ((android.content.pm.ResolveInfo) receiver).activityInfo.name;
    }

    static int getReceiverPriority(java.lang.Object receiver) {
        if (receiver instanceof com.android.server.am.BroadcastFilter) {
            return ((com.android.server.am.BroadcastFilter) receiver).getPriority();
        }
        return ((android.content.pm.ResolveInfo) receiver).priority;
    }

    static boolean isReceiverEquals(java.lang.Object a, java.lang.Object b) {
        if (a == b) {
            return true;
        }
        if (!(a instanceof android.content.pm.ResolveInfo) || !(b instanceof android.content.pm.ResolveInfo)) {
            return false;
        }
        android.content.pm.ResolveInfo infoA = (android.content.pm.ResolveInfo) a;
        android.content.pm.ResolveInfo infoB = (android.content.pm.ResolveInfo) b;
        return java.util.Objects.equals(infoA.activityInfo.packageName, infoB.activityInfo.packageName) && java.util.Objects.equals(infoA.activityInfo.name, infoB.activityInfo.name);
    }

    static boolean calculateUrgent(android.content.Intent intent, android.app.BroadcastOptions options) {
        if ((intent.getFlags() & 268435456) != 0) {
            return true;
        }
        if (options != null) {
            return options.isInteractive() || options.isAlarmBroadcast();
        }
        return false;
    }

    static boolean calculateDeferUntilActive(int callingUid, android.app.BroadcastOptions options, android.content.IIntentReceiver resultTo, boolean ordered, boolean urgent) {
        if (ordered) {
            return false;
        }
        if (!ordered && resultTo != null) {
            return true;
        }
        if (options != null) {
            switch (options.getDeferralPolicy()) {
                case 1:
                    return false;
                case 2:
                    return true;
            }
        }
        if (urgent || !CORE_DEFER_UNTIL_ACTIVE || !android.os.UserHandle.isCore(callingUid)) {
            return false;
        }
        return true;
    }

    int calculateTypeForLogging() {
        int type;
        if (isForeground()) {
            type = 0 | 2;
        } else {
            type = 0 | 1;
        }
        if (this.alarm) {
            type |= 4;
        }
        if (this.interactive) {
            type |= 8;
        }
        if (this.ordered) {
            type |= 16;
        }
        if (this.prioritized) {
            type |= 32;
        }
        if (this.resultTo != null) {
            type |= 64;
        }
        if (this.deferUntilActive) {
            type |= 128;
        }
        if (this.pushMessage) {
            type |= 256;
        }
        if (this.pushMessageOverQuota) {
            type |= 512;
        }
        if (this.sticky) {
            type |= 1024;
        }
        if (this.initialSticky) {
            return type | 2048;
        }
        return type;
    }

    public com.android.server.am.BroadcastRecord maybeStripForHistory() {
        if (!this.intent.canStripForHistory()) {
            return this;
        }
        return new com.android.server.am.BroadcastRecord(this, this.intent.maybeStripForHistory());
    }

    boolean cleanupDisabledPackageReceiversLocked(java.lang.String packageName, java.util.Set<java.lang.String> filterByClasses, int userId, boolean doit) {
        if (this.receivers == null) {
            return false;
        }
        boolean cleanupAllUsers = userId == -1;
        boolean sendToAllUsers = this.userId == -1;
        if (this.userId != userId && !cleanupAllUsers && !sendToAllUsers) {
            return false;
        }
        boolean didSomething = false;
        for (int i = this.receivers.size() - 1; i >= 0; i--) {
            java.lang.Object o = this.receivers.get(i);
            if (o instanceof android.content.pm.ResolveInfo) {
                android.content.pm.ActivityInfo info = ((android.content.pm.ResolveInfo) o).activityInfo;
                boolean sameComponent = packageName == null || (info.applicationInfo.packageName.equals(packageName) && (filterByClasses == null || filterByClasses.contains(info.name)));
                if (sameComponent && (cleanupAllUsers || android.os.UserHandle.getUserId(info.applicationInfo.uid) == userId)) {
                    if (!doit) {
                        return true;
                    }
                    didSomething = true;
                    this.receivers.remove(i);
                    if (i < this.nextReceiver) {
                        this.nextReceiver--;
                    }
                }
            }
        }
        this.nextReceiver = java.lang.Math.min(this.nextReceiver, this.receivers.size());
        return didSomething;
    }

    void applySingletonPolicy(com.android.server.am.ActivityManagerService service) {
        if (this.receivers == null) {
            return;
        }
        for (int i = 0; i < this.receivers.size(); i++) {
            java.lang.Object receiver = this.receivers.get(i);
            if (receiver instanceof android.content.pm.ResolveInfo) {
                android.content.pm.ResolveInfo info = (android.content.pm.ResolveInfo) receiver;
                boolean isSingleton = false;
                try {
                    isSingleton = service.isSingleton(info.activityInfo.processName, info.activityInfo.applicationInfo, info.activityInfo.name, info.activityInfo.flags);
                } catch (java.lang.SecurityException e) {
                    com.android.server.am.BroadcastQueue.logw(e.getMessage());
                }
                int receiverUid = info.activityInfo.applicationInfo.uid;
                if (this.callingUid != 1000 && isSingleton && service.isValidSingletonCall(this.callingUid, receiverUid)) {
                    info.activityInfo = service.getActivityInfoForUser(info.activityInfo, 0);
                }
            }
        }
    }

    boolean containsReceiver(java.lang.Object receiver) {
        for (int i = this.receivers.size() - 1; i >= 0; i--) {
            if (isReceiverEquals(receiver, this.receivers.get(i))) {
                return true;
            }
        }
        return false;
    }

    boolean containsAllReceivers(java.util.List<java.lang.Object> otherReceivers) {
        for (int i = otherReceivers.size() - 1; i >= 0; i--) {
            if (!containsReceiver(otherReceivers.get(i))) {
                return false;
            }
        }
        return true;
    }

    int getDeliveryGroupPolicy() {
        if (this.options != null) {
            return this.options.getDeliveryGroupPolicy();
        }
        return 0;
    }

    boolean matchesDeliveryGroup(com.android.server.am.BroadcastRecord other) {
        return matchesDeliveryGroup(this, other);
    }

    private static boolean matchesDeliveryGroup(com.android.server.am.BroadcastRecord newRecord, com.android.server.am.BroadcastRecord oldRecord) {
        android.content.IntentFilter newMatchingFilter = getDeliveryGroupMatchingFilter(newRecord);
        if (isMatchingKeyNull(newRecord) && isMatchingKeyNull(oldRecord) && newMatchingFilter == null) {
            return newRecord.intent.filterEquals(oldRecord.intent);
        }
        if (newMatchingFilter != null && !newMatchingFilter.asPredicate().test(oldRecord.intent)) {
            return false;
        }
        return areMatchingKeysEqual(newRecord, oldRecord);
    }

    private static boolean isMatchingKeyNull(com.android.server.am.BroadcastRecord record) {
        java.lang.String namespace = getDeliveryGroupMatchingNamespaceFragment(record);
        java.lang.String key = getDeliveryGroupMatchingKeyFragment(record);
        return namespace == null || key == null;
    }

    private static boolean areMatchingKeysEqual(com.android.server.am.BroadcastRecord newRecord, com.android.server.am.BroadcastRecord oldRecord) {
        java.lang.String newNamespaceFragment = getDeliveryGroupMatchingNamespaceFragment(newRecord);
        java.lang.String oldNamespaceFragment = getDeliveryGroupMatchingNamespaceFragment(oldRecord);
        if (!java.util.Objects.equals(newNamespaceFragment, oldNamespaceFragment)) {
            return false;
        }
        java.lang.String newKeyFragment = getDeliveryGroupMatchingKeyFragment(newRecord);
        java.lang.String oldKeyFragment = getDeliveryGroupMatchingKeyFragment(oldRecord);
        return java.util.Objects.equals(newKeyFragment, oldKeyFragment);
    }

    private static java.lang.String getDeliveryGroupMatchingNamespaceFragment(com.android.server.am.BroadcastRecord record) {
        if (record.options == null) {
            return null;
        }
        return record.options.getDeliveryGroupMatchingNamespaceFragment();
    }

    private static java.lang.String getDeliveryGroupMatchingKeyFragment(com.android.server.am.BroadcastRecord record) {
        if (record.options == null) {
            return null;
        }
        return record.options.getDeliveryGroupMatchingKeyFragment();
    }

    private static android.content.IntentFilter getDeliveryGroupMatchingFilter(com.android.server.am.BroadcastRecord record) {
        if (record.options == null) {
            return null;
        }
        return record.options.getDeliveryGroupMatchingFilter();
    }

    boolean allReceiversPending() {
        return this.terminalCount == 0 && this.dispatchTime <= 0;
    }

    boolean isMatchingRecord(com.android.server.am.BroadcastRecord record) {
        int idx = this.mMatchingRecordsCache.indexOfKey(record);
        if (idx > 0) {
            return this.mMatchingRecordsCache.valueAt(idx).booleanValue();
        }
        boolean matches = this.receivers.size() == record.receivers.size();
        if (matches) {
            int i = this.receivers.size() - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                if (isReceiverEquals(this.receivers.get(i), record.receivers.get(i))) {
                    i--;
                } else {
                    matches = false;
                    break;
                }
            }
        }
        this.mMatchingRecordsCache.put(record, java.lang.Boolean.valueOf(matches));
        return matches;
    }

    void setMatchingRecordsCache(android.util.ArrayMap<com.android.server.am.BroadcastRecord, java.lang.Boolean> matchingRecordsCache) {
        this.mMatchingRecordsCache = matchingRecordsCache;
    }

    void clearMatchingRecordsCache() {
        this.mMatchingRecordsCache = null;
    }

    public java.lang.String toString() {
        if (this.mCachedToString == null) {
            java.lang.String label = this.intent.getAction();
            if (label == null) {
                this.intent.toString();
            }
            this.mCachedToString = "BroadcastRecord{" + toShortString() + "}";
        }
        java.lang.String label2 = this.mCachedToString;
        return label2;
    }

    public java.lang.String toShortString() {
        if (this.mCachedToShortString == null) {
            java.lang.String label = this.intent.getAction();
            if (label == null) {
                label = this.intent.toString();
            }
            this.mCachedToShortString = java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + " " + label + "/u" + this.userId;
        }
        return this.mCachedToShortString;
    }

    @dalvik.annotation.optimization.NeverCompile
    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1120986464257L, this.userId);
        proto.write(1138166333442L, this.intent.getAction());
        proto.end(token);
    }

    public com.android.server.am.IBroadcastRecordWrapper getWrapper() {
        return this.mWrapper;
    }

    private class BroadcastRecordWrapper implements com.android.server.am.IBroadcastRecordWrapper {
        private BroadcastRecordWrapper() {
        }

        @Override // com.android.server.am.IBroadcastRecordWrapper
        public com.android.server.am.IBroadcastRecordExt getExtImpl() {
            return com.android.server.am.BroadcastRecord.this.mBroadcastRecordExt;
        }

        @Override // com.android.server.am.IBroadcastRecordWrapper
        public void setDeliveryState(int index, long runnableAt, int runnableAtReason) {
            com.android.server.am.BroadcastRecord.this.mBroadcastRecordExt.setDeliveryState(index, runnableAt, runnableAtReason);
        }
    }
}
