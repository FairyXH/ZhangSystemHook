package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class EventLogTags {
    public static final int AM_ANR = 30008;
    public static final int AM_BROADCAST_DISCARD_APP = 30025;
    public static final int AM_BROADCAST_DISCARD_FILTER = 30024;
    public static final int AM_CLEAR_APP_DATA_CALLER = 30120;
    public static final int AM_COMPACT = 30063;
    public static final int AM_CPU = 30104;
    public static final int AM_CRASH = 30039;
    public static final int AM_CREATE_SERVICE = 30030;
    public static final int AM_DESTROY_SERVICE = 30031;
    public static final int AM_DROP_PROCESS = 30033;
    public static final int AM_FOREGROUND_SERVICE_DENIED = 30101;
    public static final int AM_FOREGROUND_SERVICE_START = 30100;
    public static final int AM_FOREGROUND_SERVICE_STOP = 30102;
    public static final int AM_FOREGROUND_SERVICE_TIMED_OUT = 30103;
    public static final int AM_FREEZE = 30068;
    public static final int AM_INTENT_SENDER_REDIRECT_USER = 30110;
    public static final int AM_KILL = 30023;
    public static final int AM_LOW_MEMORY = 30017;
    public static final int AM_MEMINFO = 30046;
    public static final int AM_MEM_FACTOR = 30050;
    public static final int AM_OOM_ADJ_MISC = 30113;
    public static final int AM_PRE_BOOT = 30045;
    public static final int AM_PROCESS_CRASHED_TOO_MUCH = 30032;
    public static final int AM_PROCESS_START_TIMEOUT = 30037;
    public static final int AM_PROC_BAD = 30015;
    public static final int AM_PROC_BOUND = 30010;
    public static final int AM_PROC_DIED = 30011;
    public static final int AM_PROC_GOOD = 30016;
    public static final int AM_PROC_START = 30014;
    public static final int AM_PROC_STATE_CHANGED = 30112;
    public static final int AM_PROVIDER_LOST_PROCESS = 30036;
    public static final int AM_PSS = 30047;
    public static final int AM_SCHEDULE_SERVICE_RESTART = 30035;
    public static final int AM_SERVICE_CRASHED_TOO_MUCH = 30034;
    public static final int AM_STOP_IDLE_SERVICE = 30056;
    public static final int AM_SWITCH_USER = 30041;
    public static final int AM_UID_ACTIVE = 30054;
    public static final int AM_UID_IDLE = 30055;
    public static final int AM_UID_RUNNING = 30052;
    public static final int AM_UID_STATE_CHANGED = 30111;
    public static final int AM_UID_STOPPED = 30053;
    public static final int AM_UNFREEZE = 30069;
    public static final int AM_USER_STATE_CHANGED = 30051;
    public static final int AM_WTF = 30040;
    public static final int BOOT_PROGRESS_AMS_READY = 3040;
    public static final int BOOT_PROGRESS_ENABLE_SCREEN = 3050;
    public static final int CONFIGURATION_CHANGED = 2719;
    public static final int CPU = 2721;
    public static final int SSM_USER_COMPLETED_EVENT = 30088;
    public static final int SSM_USER_STARTING = 30082;
    public static final int SSM_USER_STOPPED = 30087;
    public static final int SSM_USER_STOPPING = 30086;
    public static final int SSM_USER_SWITCHING = 30083;
    public static final int SSM_USER_UNLOCKED = 30085;
    public static final int SSM_USER_UNLOCKING = 30084;
    public static final int UC_CONTINUE_USER_SWITCH = 30080;
    public static final int UC_DISPATCH_USER_SWITCH = 30079;
    public static final int UC_FINISH_USER_BOOT = 30078;
    public static final int UC_FINISH_USER_STOPPED = 30074;
    public static final int UC_FINISH_USER_STOPPING = 30073;
    public static final int UC_FINISH_USER_UNLOCKED = 30071;
    public static final int UC_FINISH_USER_UNLOCKED_COMPLETED = 30072;
    public static final int UC_FINISH_USER_UNLOCKING = 30070;
    public static final int UC_SEND_USER_BROADCAST = 30081;
    public static final int UC_START_USER_INTERNAL = 30076;
    public static final int UC_SWITCH_USER = 30075;
    public static final int UC_UNLOCK_USER = 30077;
    public static final int UM_USER_VISIBILITY_CHANGED = 30091;

    private EventLogTags() {
    }

    public static void writeConfigurationChanged(int configMask) {
        android.util.EventLog.writeEvent(CONFIGURATION_CHANGED, configMask);
    }

    public static void writeCpu(int total, int user, int system2, int iowait, int irq, int softirq) {
        android.util.EventLog.writeEvent(CPU, java.lang.Integer.valueOf(total), java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(system2), java.lang.Integer.valueOf(iowait), java.lang.Integer.valueOf(irq), java.lang.Integer.valueOf(softirq));
    }

    public static void writeBootProgressAmsReady(long time) {
        android.util.EventLog.writeEvent(BOOT_PROGRESS_AMS_READY, time);
    }

    public static void writeBootProgressEnableScreen(long time) {
        android.util.EventLog.writeEvent(BOOT_PROGRESS_ENABLE_SCREEN, time);
    }

    public static void writeAmAnr(int user, int pid, java.lang.String packageName, int flags, java.lang.String reason) {
        android.util.EventLog.writeEvent(AM_ANR, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), packageName, java.lang.Integer.valueOf(flags), reason);
    }

    public static void writeAmProcBound(int user, int pid, java.lang.String processName) {
        android.util.EventLog.writeEvent(AM_PROC_BOUND, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), processName);
    }

    public static void writeAmProcDied(int user, int pid, java.lang.String processName, int oomadj, int procstate) {
        android.util.EventLog.writeEvent(AM_PROC_DIED, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), processName, java.lang.Integer.valueOf(oomadj), java.lang.Integer.valueOf(procstate));
    }

    public static void writeAmProcStart(int user, int pid, int uid, java.lang.String processName, java.lang.String type, java.lang.String component) {
        android.util.EventLog.writeEvent(AM_PROC_START, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid), processName, type, component);
    }

    public static void writeAmProcBad(int user, int uid, java.lang.String processName) {
        android.util.EventLog.writeEvent(AM_PROC_BAD, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(uid), processName);
    }

    public static void writeAmProcGood(int user, int uid, java.lang.String processName) {
        android.util.EventLog.writeEvent(AM_PROC_GOOD, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(uid), processName);
    }

    public static void writeAmLowMemory(int numProcesses) {
        android.util.EventLog.writeEvent(AM_LOW_MEMORY, numProcesses);
    }

    public static void writeAmKill(int user, int pid, java.lang.String processName, int oomadj, java.lang.String reason, long rss) {
        android.util.EventLog.writeEvent(AM_KILL, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), processName, java.lang.Integer.valueOf(oomadj), reason, java.lang.Long.valueOf(rss));
    }

    public static void writeAmBroadcastDiscardFilter(int user, int broadcast, java.lang.String action, int receiverNumber, int broadcastfilter) {
        android.util.EventLog.writeEvent(AM_BROADCAST_DISCARD_FILTER, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(broadcast), action, java.lang.Integer.valueOf(receiverNumber), java.lang.Integer.valueOf(broadcastfilter));
    }

    public static void writeAmBroadcastDiscardApp(int user, int broadcast, java.lang.String action, int receiverNumber, java.lang.String app) {
        android.util.EventLog.writeEvent(AM_BROADCAST_DISCARD_APP, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(broadcast), action, java.lang.Integer.valueOf(receiverNumber), app);
    }

    public static void writeAmCreateService(int user, int serviceRecord, java.lang.String name, int uid, int pid) {
        android.util.EventLog.writeEvent(AM_CREATE_SERVICE, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(serviceRecord), name, java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid));
    }

    public static void writeAmDestroyService(int user, int serviceRecord, int pid) {
        android.util.EventLog.writeEvent(AM_DESTROY_SERVICE, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(serviceRecord), java.lang.Integer.valueOf(pid));
    }

    public static void writeAmProcessCrashedTooMuch(int user, java.lang.String name, int pid) {
        android.util.EventLog.writeEvent(AM_PROCESS_CRASHED_TOO_MUCH, java.lang.Integer.valueOf(user), name, java.lang.Integer.valueOf(pid));
    }

    public static void writeAmDropProcess(int pid) {
        android.util.EventLog.writeEvent(AM_DROP_PROCESS, pid);
    }

    public static void writeAmServiceCrashedTooMuch(int user, int crashCount, java.lang.String componentName, int pid) {
        android.util.EventLog.writeEvent(AM_SERVICE_CRASHED_TOO_MUCH, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(crashCount), componentName, java.lang.Integer.valueOf(pid));
    }

    public static void writeAmScheduleServiceRestart(int user, java.lang.String componentName, long time) {
        android.util.EventLog.writeEvent(AM_SCHEDULE_SERVICE_RESTART, java.lang.Integer.valueOf(user), componentName, java.lang.Long.valueOf(time));
    }

    public static void writeAmProviderLostProcess(int user, java.lang.String packageName, int uid, java.lang.String name) {
        android.util.EventLog.writeEvent(AM_PROVIDER_LOST_PROCESS, java.lang.Integer.valueOf(user), packageName, java.lang.Integer.valueOf(uid), name);
    }

    public static void writeAmProcessStartTimeout(int user, int pid, int uid, java.lang.String processName) {
        android.util.EventLog.writeEvent(AM_PROCESS_START_TIMEOUT, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid), processName);
    }

    public static void writeAmCrash(int user, int pid, java.lang.String processName, int flags, java.lang.String exception, java.lang.String message, java.lang.String file, int line, int recoverable) {
        android.util.EventLog.writeEvent(AM_CRASH, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), processName, java.lang.Integer.valueOf(flags), exception, message, file, java.lang.Integer.valueOf(line), java.lang.Integer.valueOf(recoverable));
    }

    public static void writeAmWtf(int user, int pid, java.lang.String processName, int flags, java.lang.String tag, java.lang.String message) {
        android.util.EventLog.writeEvent(AM_WTF, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(pid), processName, java.lang.Integer.valueOf(flags), tag, message);
    }

    public static void writeAmSwitchUser(int id) {
        android.util.EventLog.writeEvent(AM_SWITCH_USER, id);
    }

    public static void writeAmPreBoot(int user, java.lang.String package_) {
        android.util.EventLog.writeEvent(AM_PRE_BOOT, java.lang.Integer.valueOf(user), package_);
    }

    public static void writeAmMeminfo(long cached, long free, long zram, long kernel, long native_) {
        android.util.EventLog.writeEvent(AM_MEMINFO, java.lang.Long.valueOf(cached), java.lang.Long.valueOf(free), java.lang.Long.valueOf(zram), java.lang.Long.valueOf(kernel), java.lang.Long.valueOf(native_));
    }

    public static void writeAmPss(int pid, int uid, java.lang.String processName, long pss, long uss, long swappss, long rss, int stattype, int procstate, long timetocollect) {
        android.util.EventLog.writeEvent(AM_PSS, java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid), processName, java.lang.Long.valueOf(pss), java.lang.Long.valueOf(uss), java.lang.Long.valueOf(swappss), java.lang.Long.valueOf(rss), java.lang.Integer.valueOf(stattype), java.lang.Integer.valueOf(procstate), java.lang.Long.valueOf(timetocollect));
    }

    public static void writeAmMemFactor(int current, int previous) {
        android.util.EventLog.writeEvent(AM_MEM_FACTOR, java.lang.Integer.valueOf(current), java.lang.Integer.valueOf(previous));
    }

    public static void writeAmUserStateChanged(int id, int state) {
        android.util.EventLog.writeEvent(AM_USER_STATE_CHANGED, java.lang.Integer.valueOf(id), java.lang.Integer.valueOf(state));
    }

    public static void writeAmUidRunning(int uid) {
        android.util.EventLog.writeEvent(AM_UID_RUNNING, uid);
    }

    public static void writeAmUidStopped(int uid) {
        android.util.EventLog.writeEvent(AM_UID_STOPPED, uid);
    }

    public static void writeAmUidActive(int uid) {
        android.util.EventLog.writeEvent(AM_UID_ACTIVE, uid);
    }

    public static void writeAmUidIdle(int uid) {
        android.util.EventLog.writeEvent(AM_UID_IDLE, uid);
    }

    public static void writeAmStopIdleService(int uid, java.lang.String componentName) {
        android.util.EventLog.writeEvent(AM_STOP_IDLE_SERVICE, java.lang.Integer.valueOf(uid), componentName);
    }

    public static void writeAmCompact(int pid, java.lang.String processName, java.lang.String action, long beforersstotal, long beforerssfile, long beforerssanon, long beforerssswap, long deltarsstotal, long deltarssfile, long deltarssanon, long deltarssswap, long time, int lastaction, long lastactiontimestamp, int setadj, int procstate, long beforezramfree, long deltazramfree) {
        android.util.EventLog.writeEvent(AM_COMPACT, java.lang.Integer.valueOf(pid), processName, action, java.lang.Long.valueOf(beforersstotal), java.lang.Long.valueOf(beforerssfile), java.lang.Long.valueOf(beforerssanon), java.lang.Long.valueOf(beforerssswap), java.lang.Long.valueOf(deltarsstotal), java.lang.Long.valueOf(deltarssfile), java.lang.Long.valueOf(deltarssanon), java.lang.Long.valueOf(deltarssswap), java.lang.Long.valueOf(time), java.lang.Integer.valueOf(lastaction), java.lang.Long.valueOf(lastactiontimestamp), java.lang.Integer.valueOf(setadj), java.lang.Integer.valueOf(procstate), java.lang.Long.valueOf(beforezramfree), java.lang.Long.valueOf(deltazramfree));
    }

    public static void writeAmFreeze(int pid, java.lang.String processName) {
        android.util.EventLog.writeEvent(AM_FREEZE, java.lang.Integer.valueOf(pid), processName);
    }

    public static void writeAmUnfreeze(int pid, java.lang.String processName) {
        android.util.EventLog.writeEvent(AM_UNFREEZE, java.lang.Integer.valueOf(pid), processName);
    }

    public static void writeUcFinishUserUnlocking(int userid) {
        android.util.EventLog.writeEvent(UC_FINISH_USER_UNLOCKING, userid);
    }

    public static void writeUcFinishUserUnlocked(int userid) {
        android.util.EventLog.writeEvent(UC_FINISH_USER_UNLOCKED, userid);
    }

    public static void writeUcFinishUserUnlockedCompleted(int userid) {
        android.util.EventLog.writeEvent(UC_FINISH_USER_UNLOCKED_COMPLETED, userid);
    }

    public static void writeUcFinishUserStopping(int userid) {
        android.util.EventLog.writeEvent(UC_FINISH_USER_STOPPING, userid);
    }

    public static void writeUcFinishUserStopped(int userid) {
        android.util.EventLog.writeEvent(UC_FINISH_USER_STOPPED, userid);
    }

    public static void writeUcSwitchUser(int userid) {
        android.util.EventLog.writeEvent(UC_SWITCH_USER, userid);
    }

    public static void writeUcStartUserInternal(int userid, int foreground, int displayid) {
        android.util.EventLog.writeEvent(UC_START_USER_INTERNAL, java.lang.Integer.valueOf(userid), java.lang.Integer.valueOf(foreground), java.lang.Integer.valueOf(displayid));
    }

    public static void writeUcUnlockUser(int userid) {
        android.util.EventLog.writeEvent(UC_UNLOCK_USER, userid);
    }

    public static void writeUcFinishUserBoot(int userid) {
        android.util.EventLog.writeEvent(UC_FINISH_USER_BOOT, userid);
    }

    public static void writeUcDispatchUserSwitch(int olduserid, int newuserid) {
        android.util.EventLog.writeEvent(UC_DISPATCH_USER_SWITCH, java.lang.Integer.valueOf(olduserid), java.lang.Integer.valueOf(newuserid));
    }

    public static void writeUcContinueUserSwitch(int olduserid, int newuserid) {
        android.util.EventLog.writeEvent(UC_CONTINUE_USER_SWITCH, java.lang.Integer.valueOf(olduserid), java.lang.Integer.valueOf(newuserid));
    }

    public static void writeUcSendUserBroadcast(int userid, java.lang.String intentaction) {
        android.util.EventLog.writeEvent(UC_SEND_USER_BROADCAST, java.lang.Integer.valueOf(userid), intentaction);
    }

    public static void writeSsmUserStarting(int userid) {
        android.util.EventLog.writeEvent(SSM_USER_STARTING, userid);
    }

    public static void writeSsmUserSwitching(int olduserid, int newuserid) {
        android.util.EventLog.writeEvent(SSM_USER_SWITCHING, java.lang.Integer.valueOf(olduserid), java.lang.Integer.valueOf(newuserid));
    }

    public static void writeSsmUserUnlocking(int userid) {
        android.util.EventLog.writeEvent(SSM_USER_UNLOCKING, userid);
    }

    public static void writeSsmUserUnlocked(int userid) {
        android.util.EventLog.writeEvent(SSM_USER_UNLOCKED, userid);
    }

    public static void writeSsmUserStopping(int userid) {
        android.util.EventLog.writeEvent(SSM_USER_STOPPING, userid);
    }

    public static void writeSsmUserStopped(int userid) {
        android.util.EventLog.writeEvent(SSM_USER_STOPPED, userid);
    }

    public static void writeSsmUserCompletedEvent(int userid, int eventflag) {
        android.util.EventLog.writeEvent(SSM_USER_COMPLETED_EVENT, java.lang.Integer.valueOf(userid), java.lang.Integer.valueOf(eventflag));
    }

    public static void writeUmUserVisibilityChanged(int userid, int visible) {
        android.util.EventLog.writeEvent(UM_USER_VISIBILITY_CHANGED, java.lang.Integer.valueOf(userid), java.lang.Integer.valueOf(visible));
    }

    public static void writeAmForegroundServiceStart(int user, java.lang.String componentName, int allowwhileinuse, java.lang.String startreasoncode, int targetsdk, int callertargetsdk, int notificationwasdeferred, int notificationshown, int durationms, int startforegroundcount, java.lang.String stopreason, int fgstype) {
        android.util.EventLog.writeEvent(AM_FOREGROUND_SERVICE_START, java.lang.Integer.valueOf(user), componentName, java.lang.Integer.valueOf(allowwhileinuse), startreasoncode, java.lang.Integer.valueOf(targetsdk), java.lang.Integer.valueOf(callertargetsdk), java.lang.Integer.valueOf(notificationwasdeferred), java.lang.Integer.valueOf(notificationshown), java.lang.Integer.valueOf(durationms), java.lang.Integer.valueOf(startforegroundcount), stopreason, java.lang.Integer.valueOf(fgstype));
    }

    public static void writeAmForegroundServiceDenied(int user, java.lang.String componentName, int allowwhileinuse, java.lang.String startreasoncode, int targetsdk, int callertargetsdk, int notificationwasdeferred, int notificationshown, int durationms, int startforegroundcount, java.lang.String stopreason, int fgstype) {
        android.util.EventLog.writeEvent(AM_FOREGROUND_SERVICE_DENIED, java.lang.Integer.valueOf(user), componentName, java.lang.Integer.valueOf(allowwhileinuse), startreasoncode, java.lang.Integer.valueOf(targetsdk), java.lang.Integer.valueOf(callertargetsdk), java.lang.Integer.valueOf(notificationwasdeferred), java.lang.Integer.valueOf(notificationshown), java.lang.Integer.valueOf(durationms), java.lang.Integer.valueOf(startforegroundcount), stopreason, java.lang.Integer.valueOf(fgstype));
    }

    public static void writeAmForegroundServiceStop(int user, java.lang.String componentName, int allowwhileinuse, java.lang.String startreasoncode, int targetsdk, int callertargetsdk, int notificationwasdeferred, int notificationshown, int durationms, int startforegroundcount, java.lang.String stopreason, int fgstype) {
        android.util.EventLog.writeEvent(AM_FOREGROUND_SERVICE_STOP, java.lang.Integer.valueOf(user), componentName, java.lang.Integer.valueOf(allowwhileinuse), startreasoncode, java.lang.Integer.valueOf(targetsdk), java.lang.Integer.valueOf(callertargetsdk), java.lang.Integer.valueOf(notificationwasdeferred), java.lang.Integer.valueOf(notificationshown), java.lang.Integer.valueOf(durationms), java.lang.Integer.valueOf(startforegroundcount), stopreason, java.lang.Integer.valueOf(fgstype));
    }

    public static void writeAmForegroundServiceTimedOut(int user, java.lang.String componentName, int allowwhileinuse, java.lang.String startreasoncode, int targetsdk, int callertargetsdk, int notificationwasdeferred, int notificationshown, int durationms, int startforegroundcount, java.lang.String stopreason, int fgstype) {
        android.util.EventLog.writeEvent(AM_FOREGROUND_SERVICE_TIMED_OUT, java.lang.Integer.valueOf(user), componentName, java.lang.Integer.valueOf(allowwhileinuse), startreasoncode, java.lang.Integer.valueOf(targetsdk), java.lang.Integer.valueOf(callertargetsdk), java.lang.Integer.valueOf(notificationwasdeferred), java.lang.Integer.valueOf(notificationshown), java.lang.Integer.valueOf(durationms), java.lang.Integer.valueOf(startforegroundcount), stopreason, java.lang.Integer.valueOf(fgstype));
    }

    public static void writeAmCpu(long pid, long uid, java.lang.String baseName, long uptime, long stime, long utime) {
        android.util.EventLog.writeEvent(AM_CPU, java.lang.Long.valueOf(pid), java.lang.Long.valueOf(uid), baseName, java.lang.Long.valueOf(uptime), java.lang.Long.valueOf(stime), java.lang.Long.valueOf(utime));
    }

    public static void writeAmIntentSenderRedirectUser(int userid) {
        android.util.EventLog.writeEvent(AM_INTENT_SENDER_REDIRECT_USER, userid);
    }

    public static void writeAmClearAppDataCaller(int pid, int uid, java.lang.String package_) {
        android.util.EventLog.writeEvent(AM_CLEAR_APP_DATA_CALLER, java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(uid), package_);
    }

    public static void writeAmUidStateChanged(int uid, int seq, int uidstate, int olduidstate, int capability, int oldcapability, int flags, java.lang.String reason) {
        android.util.EventLog.writeEvent(AM_UID_STATE_CHANGED, java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(seq), java.lang.Integer.valueOf(uidstate), java.lang.Integer.valueOf(olduidstate), java.lang.Integer.valueOf(capability), java.lang.Integer.valueOf(oldcapability), java.lang.Integer.valueOf(flags), reason);
    }

    public static void writeAmProcStateChanged(int uid, int pid, int seq, int procstate, int oldprocstate, int oomadj, int oldoomadj, java.lang.String reason) {
        android.util.EventLog.writeEvent(AM_PROC_STATE_CHANGED, java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(seq), java.lang.Integer.valueOf(procstate), java.lang.Integer.valueOf(oldprocstate), java.lang.Integer.valueOf(oomadj), java.lang.Integer.valueOf(oldoomadj), reason);
    }

    public static void writeAmOomAdjMisc(int event, int uid, int pid, int seq, int arg1, int arg2, java.lang.String reason) {
        android.util.EventLog.writeEvent(AM_OOM_ADJ_MISC, java.lang.Integer.valueOf(event), java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid), java.lang.Integer.valueOf(seq), java.lang.Integer.valueOf(arg1), java.lang.Integer.valueOf(arg2), reason);
    }
}
