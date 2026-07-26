package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ContentProviderHelper {
    private static boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final int[] PROCESS_STATE_STATS_FORMAT = {32, com.android.internal.util.FrameworkStatsLog.PACKAGE_MANAGER_SNAPSHOT_REPORTED, 10272};
    private static final java.lang.String TAG = "ContentProviderHelper";
    private final com.android.server.am.ProviderMap mProviderMap;
    private final com.android.server.am.ActivityManagerService mService;
    private boolean mSystemProvidersInstalled;
    private final java.util.ArrayList<com.android.server.am.ContentProviderRecord> mLaunchingProviders = new java.util.ArrayList<>();
    private final java.util.Map<java.lang.String, java.lang.Boolean> mCloneProfileAuthorityRedirectionCache = new java.util.HashMap();
    private boolean mIsKillOneTimes = false;
    public com.android.server.am.IContentProviderHelperExt mContentProviderHelperExt = (com.android.server.am.IContentProviderHelperExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IContentProviderHelperExt.class).create();
    private final long[] mProcessStateStatsLongs = new long[1];

    ContentProviderHelper(com.android.server.am.ActivityManagerService service, boolean createProviderMap) {
        this.mService = service;
        this.mProviderMap = createProviderMap ? new com.android.server.am.ProviderMap(this.mService) : null;
    }

    com.android.server.am.ProviderMap getProviderMap() {
        return this.mProviderMap;
    }

    android.app.ContentProviderHolder getContentProvider(android.app.IApplicationThread caller, java.lang.String callingPackage, java.lang.String name, int userId, boolean stable) {
        this.mService.enforceNotIsolatedCaller("getContentProvider");
        if (caller == null) {
            java.lang.String msg = "null IApplicationThread when getting content provider " + name;
            android.util.Slog.w(TAG, msg);
            throw new java.lang.SecurityException(msg);
        }
        int callingUid = android.os.Binder.getCallingUid();
        if (callingPackage != null && this.mService.mAppOpsService.checkPackage(callingUid, callingPackage) != 0) {
            throw new java.lang.SecurityException("Given calling package " + callingPackage + " does not match caller's uid " + callingUid);
        }
        return getContentProviderImpl(caller, name, null, callingUid, callingPackage, null, stable, userId);
    }

    android.app.ContentProviderHolder getContentProviderExternal(java.lang.String name, int userId, android.os.IBinder token, java.lang.String tag) {
        this.mService.enforceCallingPermission("android.permission.ACCESS_CONTENT_PROVIDERS_EXTERNALLY", "Do not have permission in call getContentProviderExternal()");
        return getContentProviderExternalUnchecked(name, token, android.os.Binder.getCallingUid(), tag != null ? tag : "*external*", this.mService.mUserController.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, 2, "getContentProvider", null));
    }

    android.app.ContentProviderHolder getContentProviderExternalUnchecked(java.lang.String name, android.os.IBinder token, int callingUid, java.lang.String callingTag, int userId) {
        return getContentProviderImpl(null, name, token, callingUid, null, callingTag, true, userId);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:445|446|447|(3:914|449|(4:451|(18:453|(7:455|456|878|457|458|856|459)(1:467)|861|468|(5:470|875|471|(1:473)|474)|477|478|847|479|480|839|481|482|552|553|943|554|555)(1:489)|576|577)(1:490))(1:493)|996|494|(5:980|496|497|498|499)(23:502|503|(1:505)(1:506)|507|(1:509)(1:510)|511|512|984|513|(1:515)(1:516)|517|518|969|519|(5:896|521|522|890|523)(1:529)|530|531|960|532|830|(6:534|823|535|536|537|538)(15:543|544|(1:546)|547|548|954|549|550|947|551|552|553|943|554|555)|576|577)) */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0893, code lost:
    
        if (r14.mService.mUserController.isUserRunning(r13, 0) != false) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0895, code lost:
    
        android.util.Slog.w(com.android.server.am.ContentProviderHelper.TAG, "Unable to launch app " + r11.applicationInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + r11.applicationInfo.uid + " for provider " + r12 + ": user " + r13 + " is stopped");
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x08d9, code lost:
    
        monitor-exit(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x08da, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x08de, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x08df, code lost:
    
        r1 = new android.content.ComponentName(r11.packageName, r11.name);
        r14.checkTime(r5, "getContentProviderImpl: before getProviderByClass");
        r1 = r14.mProviderMap.getProviderByClass(r1, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x08f8, code lost:
    
        r14.checkTime(r5, "getContentProviderImpl: after getProviderByClass");
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x08fe, code lost:
    
        if (r1 == null) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x0902, code lost:
    
        if (r10 != r1.proc) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0904, code lost:
    
        if (r10 == null) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x0907, code lost:
    
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x0909, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x090a, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0915, code lost:
    
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0916, code lost:
    
        r18 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0918, code lost:
    
        if (r18 == false) goto L385;
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x091a, code lost:
    
        r3 = android.os.Binder.clearCallingIdentity();
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0926, code lost:
    
        if (r14.requestTargetProviderPermissionsReviewIfNeededLocked(r11, r15, r13, r14.mService.mContext) != false) goto L998;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0928, code lost:
    
        monitor-exit(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x0929, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x092d, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x092e, code lost:
    
        r14.checkTime(r5, "getContentProviderImpl: before getApplicationInfo");
        r1 = android.app.AppGlobals.getPackageManager();
        r5 = r11.applicationInfo.packageName;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x093c, code lost:
    
        r21 = r1;
        r20 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x0942, code lost:
    
        r1 = r1.getApplicationInfo(r5, 1024, (int) r13);
        r14.checkTime(r5, "getContentProviderImpl: after getApplicationInfo");
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x094c, code lost:
    
        if (r1 != null) goto L370;
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x094e, code lost:
    
        android.util.Slog.w(com.android.server.am.ContentProviderHelper.TAG, "No package info for content provider " + r11.name);
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x0969, code lost:
    
        android.os.Binder.restoreCallingIdentity(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x096c, code lost:
    
        monitor-exit(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x096d, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x0971, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0972, code lost:
    
        r27 = r14.mService.getAppInfoForUser(r1, r13);
        r5 = r14.mService;
        r1 = new com.android.server.am.ContentProviderRecord(r5, r11, r27, r21, r16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x098a, code lost:
    
        android.os.Binder.restoreCallingIdentity(r3);
        r10 = r1;
        r5 = r5;
        r27 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x098f, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x0990, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x0993, code lost:
    
        r5 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x0994, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0995, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x099a, code lost:
    
        android.os.Binder.restoreCallingIdentity(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x099e, code lost:
    
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x09a0, code lost:
    
        r21 = r1;
        r20 = r10;
        r5 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x09a4, code lost:
    
        android.os.Binder.restoreCallingIdentity(r3);
        r5 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x09a8, code lost:
    
        r21 = r1;
        r20 = r10;
        r5 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x09ac, code lost:
    
        r10 = r1;
        r5 = r5;
        r27 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x09ad, code lost:
    
        r14.checkTime(r5, "getContentProviderImpl: now have ContentProviderRecord");
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x09b3, code lost:
    
        if (r15 != null) goto L931;
     */
    /* JADX WARN: Code restructure failed: missing block: B:390:0x09b9, code lost:
    
        if (r10.canRunHere(r15) != false) goto L391;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x09bb, code lost:
    
        r14.mContentProviderHelperExt.handleReturnHolder(r11, r89, r88, false, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x09cd, code lost:
    
        r14.mContentProviderHelperExt.logStatsRecord(r10, r57, false);
        r1 = r10.newHolder(null, true);
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x09d6, code lost:
    
        monitor-exit(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x09d7, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x09da, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:397:0x09db, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:398:0x09dc, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x09ea, code lost:
    
        r5 = r57;
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x09ef, code lost:
    
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROVIDER != false) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x09f1, code lost:
    
        r2 = new java.lang.StringBuilder().append("LAUNCHING REMOTE PROVIDER (myuid ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x09fe, code lost:
    
        if (r15 != null) goto L404;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x0a00, code lost:
    
        r3 = java.lang.Integer.valueOf(r15.uid);
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x0a07, code lost:
    
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0a08, code lost:
    
        android.util.Slog.w(com.android.server.am.ContentProviderHelper.TAG, r2.append(r3).append(" pruid ").append(r10.appInfo.uid).append("): ").append(r10.info.name).append(" callers=").append(android.os.Debug.getCallers(6)).toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:408:0x0a3f, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0a40, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x0a4c, code lost:
    
        r1 = r14.mLaunchingProviders.size();
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x0a52, code lost:
    
        r3 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x0a60, code lost:
    
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0a63, code lost:
    
        if (r3 < r1) goto L580;
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x0a65, code lost:
    
        r1 = android.os.Binder.getCallingPid();
        r24 = android.os.Binder.clearCallingIdentity();
     */
    /* JADX WARN: Code restructure failed: missing block: B:420:0x0a78, code lost:
    
        if (android.text.TextUtils.equals(r10.appInfo.packageName, r89) == false) goto L882;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0a86, code lost:
    
        r14.mService.mUsageStatsService.reportEvent(r10.appInfo.packageName, r13, 31);
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x0a8a, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x0a8b, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:428:0x0aa6, code lost:
    
        r14.checkTime(r5, "getContentProviderImpl: before set stopped state");
        r14.mService.mPackageManagerInt.notifyComponentUsed(r10.appInfo.packageName, r13, r89, r10.toString());
        r14.checkTime(r5, "getContentProviderImpl: after set stopped state");
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0ac1, code lost:
    
        r27 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:430:0x0ac4, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:431:0x0ac5, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x0adc, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x0ae5, code lost:
    
        r27 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0ae7, code lost:
    
        android.util.Slog.w(com.android.server.am.ContentProviderHelper.TAG, "Failed trying to unstop package " + r10.appInfo.packageName + ": " + r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:438:0x0b0e, code lost:
    
        if (r14.mContentProviderHelperExt.hookHansProviderIfNeeded(r11, r1, r88, r89) != false) goto L994;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x0b11, code lost:
    
        android.os.Binder.restoreCallingIdentity(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x0b14, code lost:
    
        monitor-exit(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0b15, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0b1a, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:443:0x0b1b, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0b1c, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0b2b, code lost:
    
        r14.checkTime(r5, "getContentProviderImpl: looking for process record");
        r1 = r14.mService.getProcessRecordLocked(r11.processName, r10.appInfo.uid);
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x0b3e, code lost:
    
        if (r1 != null) goto L914;
     */
    /* JADX WARN: Code restructure failed: missing block: B:449:0x0b40, code lost:
    
        r1 = r1.getThread();
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0b45, code lost:
    
        if (r1 != null) goto L451;
     */
    /* JADX WARN: Code restructure failed: missing block: B:452:0x0b4b, code lost:
    
        if (r1.isKilled() == false) goto L453;
     */
    /* JADX WARN: Code restructure failed: missing block: B:454:0x0b4f, code lost:
    
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROVIDER != false) goto L455;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0b53, code lost:
    
        r92 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x0b5a, code lost:
    
        r54 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x0b5c, code lost:
    
        android.util.Slog.d(com.android.server.am.ContentProviderHelper.TAG, "Installing in existing process " + r1);
        r54 = r54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x0b6f, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:462:0x0b70, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0b87, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:464:0x0b88, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x0bbd, code lost:
    
        r92 = r1;
        r54 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:468:0x0bc1, code lost:
    
        r1 = r1.mProviders;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0bca, code lost:
    
        if (r1.hasProvider(r11.name) == false) goto L470;
     */
    /* JADX WARN: Code restructure failed: missing block: B:470:0x0bcc, code lost:
    
        r14.checkTime(r5, "getContentProviderImpl: scheduling install");
        r1.installProvider(r11.name, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:472:0x0bd9, code lost:
    
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROVIDER != false) goto L473;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0bdb, code lost:
    
        android.util.Slog.d(com.android.server.am.ContentProviderHelper.TAG, "Installing provider " + r1 + " cpr " + r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:474:0x0bfd, code lost:
    
        r1.scheduleInstallProvider(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0c02, code lost:
    
        r2 = r1.uid;
        r5 = r11.packageName;
        r29 = r1.mState.getCurProcState();
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0c0c, code lost:
    
        r63 = r54;
        r67 = r21;
        r68 = r10;
        r92 = r11;
        r69 = r17 ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x0c42, code lost:
    
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PROVIDER_ACQUISITION_EVENT_REPORTED, r2, r88, 1, 1, r5, r89, r30, r29, false, 0L);
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0c47, code lost:
    
        r68.mContentProviderRecordExt.setLogState(2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:482:0x0c4d, code lost:
    
        r81 = r92;
        r17 = r13;
        r4 = r1;
        r79 = r5;
        r13 = r68;
        r10 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0c5a, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:484:0x0c5b, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:485:0x0c67, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x0c68, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:487:0x0c76, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x0c77, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x0c94, code lost:
    
        r34 = r1;
        r63 = r5;
        r65 = r5;
        r92 = r11;
        r69 = r17 ? 1 : 0;
        r67 = r21;
        r8 = 2;
        r11 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x0cab, code lost:
    
        r34 = r1;
        r63 = r5;
        r65 = r5;
        r92 = r11;
        r69 = r17 ? 1 : 0;
        r67 = r21;
        r8 = 2;
        r11 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:491:0x0cc2, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x0cc3, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:493:0x0ce0, code lost:
    
        r34 = r1;
        r63 = r5;
        r65 = r5;
        r92 = r11;
        r69 = r17 ? 1 : 0;
        r67 = r21;
        r8 = 2;
        r11 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:495:0x0d04, code lost:
    
        if (r14.mContentProviderHelperExt.hookPreloadProviderBlock(r34, r92, r88, r89, r15, r11) != false) goto L980;
     */
    /* JADX WARN: Code restructure failed: missing block: B:496:0x0d07, code lost:
    
        android.os.Binder.restoreCallingIdentity(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x0d0a, code lost:
    
        monitor-exit(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:498:0x0d0b, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x0d0f, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:500:0x0d10, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x0d11, code lost:
    
        r5 = r0;
        r69 = r69 ? 1 : 0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0d23, code lost:
    
        r14.mContentProviderHelperExt.hookComsumeTokenIfNeeded(r14.mService, r11, r92, r88, r89);
        r14.mContentProviderHelperExt.handleReturnHolder(r92, r89, r88, true, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0d46, code lost:
    
        if ((r11.appInfo.flags & 2097152) != 0) goto L505;
     */
    /* JADX WARN: Code restructure failed: missing block: B:505:0x0d48, code lost:
    
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x0d4a, code lost:
    
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x0d4b, code lost:
    
        r10 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x0d4c, code lost:
    
        if (r10 != false) goto L509;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x0d4e, code lost:
    
        r5 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x0d50, code lost:
    
        r5 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x0d54, code lost:
    
        r9 = r92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x0d5c, code lost:
    
        if (r14.mService.wasPackageEverLaunched(r9.packageName, r13) == false) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x0d5e, code lost:
    
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x0d60, code lost:
    
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x0d61, code lost:
    
        r8 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x0d65, code lost:
    
        r6 = r65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x0d67, code lost:
    
        r14.checkTime(r6, "getContentProviderImpl: before start process");
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x0d6c, code lost:
    
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROVIDER != false) goto L896;
     */
    /* JADX WARN: Code restructure failed: missing block: B:522:0x0d87, code lost:
    
        r4 = r86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x0d89, code lost:
    
        android.util.Slog.d(com.android.server.am.ContentProviderHelper.TAG, "Start process " + r9.processName + " for " + r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0d95, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0d96, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0da1, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0da2, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0daf, code lost:
    
        r4 = r86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0dbb, code lost:
    
        r17 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0dbd, code lost:
    
        r1 = new com.android.server.am.HostingRecord(com.android.server.am.HostingRecord.HOSTING_TYPE_CONTENT_PROVIDER, new android.content.ComponentName(r9.applicationInfo.packageName, r9.name));
        r1.getWrapper().getExtImpl().setCallerName(r89);
        r1 = r14.mService.startProcessLocked(r9.processName, r11.appInfo, false, 0, r1, 0, false, false);
        r14.checkTime(r6, "getContentProviderImpl: after start process");
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0df5, code lost:
    
        if (r1 == null) goto L534;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0df7, code lost:
    
        android.util.Slog.w(com.android.server.am.ContentProviderHelper.TAG, "Unable to launch app " + r9.applicationInfo.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + r9.applicationInfo.uid + " for provider " + r4 + ": process is bad");
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0e32, code lost:
    
        android.os.Binder.restoreCallingIdentity(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0e35, code lost:
    
        monitor-exit(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0e36, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0e3b, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0e3c, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:540:0x0e3d, code lost:
    
        r5 = r0;
        r69 = r69 ? 1 : 0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0e4d, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:542:0x0e4e, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0e5b, code lost:
    
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PROCESSES != false) goto L546;
     */
    /* JADX WARN: Code restructure failed: missing block: B:546:0x0e5d, code lost:
    
        android.util.Slog.d(com.android.server.am.ContentProviderHelper.TAG, "Logging provider access for " + r9.packageName + ", stopped=" + r10 + ", firstLaunch=" + r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x0e8f, code lost:
    
        r79 = r6;
        r81 = r9;
        r13 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0eb4, code lost:
    
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.PROVIDER_ACQUISITION_EVENT_REPORTED, r1.uid, r88, 3, r5, r9.packageName, r89, r30, 20, r8, 0);
        r14.mContentProviderHelperExt.hookGetContentProviderImplAfterStartProc(r15, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0ec4, code lost:
    
        r10 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x0ec8, code lost:
    
        r14.mService.mProcessList.getAppStartInfoTracker().handleProcessContentProviderStart(r10, r1);
        r13.mContentProviderRecordExt.setLogState(4);
        r4 = r1;
        r63 = r63;
        r69 = r69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x0ed2, code lost:
    
        r13.launchingApp = r4;
        r14.mLaunchingProviders.add(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:553:0x0edb, code lost:
    
        r12 = r81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0edd, code lost:
    
        r9 = true;
        r14.mContentProviderHelperExt.updateExecutingComponent(r12.applicationInfo.uid, 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0ee5, code lost:
    
        android.os.Binder.restoreCallingIdentity(r24);
        r17 = r17;
        r63 = r63;
        r69 = r69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:556:0x0eea, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x0eeb, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0eee, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0eef, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x0ef4, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0ef5, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0efc, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x0efd, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x0f06, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:565:0x0f07, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x0f12, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x0f13, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x0f1e, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:569:0x0f1f, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x0f2a, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x0f2b, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:572:0x0f41, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:573:0x0f42, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:574:0x0f58, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:575:0x0f59, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:576:0x0f70, code lost:
    
        android.os.Binder.restoreCallingIdentity(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:577:0x0f74, code lost:
    
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:578:0x0f75, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:579:0x0f76, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:580:0x0f8f, code lost:
    
        r63 = r5;
        r79 = r5;
        r12 = r11;
        r69 = r17 ? 1 : 0;
        r67 = r21;
        r17 = r13;
        r13 = r10;
        r10 = r16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:581:0x0fa3, code lost:
    
        r7 = r79;
        r14.checkTime(r7, "getContentProviderImpl: updating data structures");
     */
    /* JADX WARN: Code restructure failed: missing block: B:582:0x0fab, code lost:
    
        if (r18 == false) goto L587;
     */
    /* JADX WARN: Code restructure failed: missing block: B:583:0x0fad, code lost:
    
        r6 = r67;
        r14.mProviderMap.putProviderByClass(r6, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:585:0x0fb5, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:586:0x0fb6, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:587:0x0fc7, code lost:
    
        r6 = r67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:588:0x0fc9, code lost:
    
        r14.mProviderMap.putProviderByName(r86, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:589:0x0fd4, code lost:
    
        r2 = r15;
        r4 = r87;
        r51 = r7;
        r15 = r9;
        r20 = r12;
        r21 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:590:0x0ffb, code lost:
    
        r1 = incProviderCountLocked(r2, r13, r4, r88, r89, r90, r91, false, r51, r14.mService.mProcessList, r92);
     */
    /* JADX WARN: Code restructure failed: missing block: B:591:0x0fff, code lost:
    
        if (r1 != null) goto L967;
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x1001, code lost:
    
        r1.waiting = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x1004, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:595:0x1005, code lost:
    
        r5 = r0;
        r69 = r69 ? 1 : 0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:596:0x101a, code lost:
    
        r10 = r1;
        r7 = r17;
        r9 = r20;
        r8 = r21;
        r4 = r4;
        r63 = r63;
        r69 = r69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:597:0x1023, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:598:0x1024, code lost:
    
        r5 = r0;
        r69 = r69 ? 1 : 0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:599:0x1037, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:600:0x1038, code lost:
    
        r5 = r0;
        r69 = r69 ? 1 : 0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:601:0x104f, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:602:0x1050, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:603:0x106d, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:604:0x106e, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:605:0x108b, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:606:0x108c, code lost:
    
        r5 = r0;
        r23 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:736:0x1392, code lost:
    
        if (r6 == false) goto L768;
     */
    /* JADX WARN: Code restructure failed: missing block: B:737:0x1394, code lost:
    
        r1 = "unknown";
     */
    /* JADX WARN: Code restructure failed: missing block: B:738:0x1399, code lost:
    
        if (r85 == null) goto L766;
     */
    /* JADX WARN: Code restructure failed: missing block: B:739:0x139b, code lost:
    
        r2 = r14.mService.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:740:0x13a2, code lost:
    
        monitor-enter(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:741:0x13a3, code lost:
    
        r3 = r14.mService.mProcessList.getLRURecordForAppLOSP(r85);
     */
    /* JADX WARN: Code restructure failed: missing block: B:742:0x13ab, code lost:
    
        if (r3 == null) goto L744;
     */
    /* JADX WARN: Code restructure failed: missing block: B:743:0x13ad, code lost:
    
        r1 = r3.processName;
     */
    /* JADX WARN: Code restructure failed: missing block: B:744:0x13b0, code lost:
    
        monitor-exit(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:745:0x13b1, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:746:0x13b6, code lost:
    
        if (r14.mIsKillOneTimes != false) goto L752;
     */
    /* JADX WARN: Code restructure failed: missing block: B:748:0x13ba, code lost:
    
        if (r8.launchingApp == null) goto L752;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x13c6, code lost:
    
        if ("android.process.acore".equals(r8.launchingApp.processName) == false) goto L752;
     */
    /* JADX WARN: Code restructure failed: missing block: B:751:0x13c8, code lost:
    
        android.util.Slog.w("ActivityManager_MU", "force stop com.android.providers.contacts");
        r2 = android.os.Binder.clearCallingIdentity();
        r14.mService.forceStopPackage("com.android.providers.contacts", r7);
        r14.mIsKillOneTimes = r15;
        android.os.Binder.restoreCallingIdentity(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x13e1, code lost:
    
        android.util.Slog.v("ActivityManager_MU", "cleanupAppInLaunchingProvidersLocked " + r8 + " cpr.launchingApp " + r8.launchingApp);
     */
    /* JADX WARN: Code restructure failed: missing block: B:753:0x1405, code lost:
    
        monitor-enter(r84);
     */
    /* JADX WARN: Code restructure failed: missing block: B:754:0x1406, code lost:
    
        r14.cleanupAppInLaunchingProvidersLocked(r8.launchingApp, r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:761:0x1411, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:764:0x1414, code lost:
    
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
     */
    /* JADX WARN: Code restructure failed: missing block: B:765:0x1417, code lost:
    
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:769:0x1477, code lost:
    
        return r8.newHolder(r10, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:770:0x1478, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:771:0x1479, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 17, insn: 0x007b: MOVE (r3 I:??[OBJECT, ARRAY]) = (r17 I:??[OBJECT, ARRAY] A[D('cpi' android.content.pm.ProviderInfo)]), block:B:18:0x0072 */
    /* JADX WARN: Not initialized variable reg: 18, insn: 0x007d: MOVE (r17 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('cpi' android.content.pm.ProviderInfo)]) = (r18 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('providerRunning' boolean)]), block:B:18:0x0072 */
    /* JADX WARN: Not initialized variable reg: 60, insn: 0x06cf: MOVE (r1 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r60 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('userId' int)]), block:B:261:0x06c7 */
    /* JADX WARN: Not initialized variable reg: 63, insn: 0x0fbd: MOVE (r54 I:??[long, double]) = (r63 I:??[long, double] A[D('callStartTime' long)]), block:B:586:0x0fb6 */
    /* JADX WARN: Not initialized variable reg: 69, insn: 0x0fbf: MOVE (r17 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('userId' int)]) = (r69 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('providerRunning' boolean)]), block:B:586:0x0fb6 */
    /* JADX WARN: Removed duplicated region for block: B:120:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:166:0x043a  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x055a  */
    /* JADX WARN: Removed duplicated region for block: B:217:0x056c A[Catch: all -> 0x0519, TRY_ENTER, TRY_LEAVE, TryCatch #119 {all -> 0x0519, blocks: (B:197:0x0510, B:209:0x0545, B:211:0x054f, B:217:0x056c), top: B:992:0x0510 }] */
    /* JADX WARN: Removed duplicated region for block: B:238:0x0611  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x07e0  */
    /* JADX WARN: Removed duplicated region for block: B:402:0x09f1 A[Catch: all -> 0x0a3f, TRY_ENTER, TryCatch #15 {all -> 0x0a3f, blocks: (B:393:0x09cd, B:394:0x09d6, B:402:0x09f1, B:404:0x0a00, B:406:0x0a08, B:413:0x0a57), top: B:825:0x09b3 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:580:0x0f8f  */
    /* JADX WARN: Removed duplicated region for block: B:583:0x0fad A[Catch: all -> 0x0fb5, TRY_ENTER, TRY_LEAVE, TryCatch #67 {all -> 0x0fb5, blocks: (B:555:0x0ee5, B:583:0x0fad, B:576:0x0f70, B:577:0x0f74), top: B:907:0x0a63 }] */
    /* JADX WARN: Removed duplicated region for block: B:587:0x0fc7  */
    /* JADX WARN: Removed duplicated region for block: B:675:0x123f  */
    /* JADX WARN: Removed duplicated region for block: B:808:0x1156 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0204 A[Catch: all -> 0x0238, TryCatch #12 {all -> 0x0238, blocks: (B:75:0x01e6, B:77:0x01f2, B:81:0x01fa, B:80:0x01f8, B:83:0x0204, B:86:0x0215, B:89:0x021d), top: B:819:0x01e6 }] */
    /* JADX WARN: Removed duplicated region for block: B:859:0x0a65 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x020c  */
    /* JADX WARN: Removed duplicated region for block: B:900:0x083e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:965:0x0586 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:967:0x1001 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r13v49 */
    /* JADX WARN: Type inference failed for: r13v50, types: [int] */
    /* JADX WARN: Type inference failed for: r13v96 */
    /* JADX WARN: Type inference failed for: r14v36 */
    /* JADX WARN: Type inference failed for: r14v37 */
    /* JADX WARN: Type inference failed for: r14v38 */
    /* JADX WARN: Type inference failed for: r14v40, types: [com.android.server.am.ContentProviderHelper] */
    /* JADX WARN: Type inference failed for: r14v41 */
    /* JADX WARN: Type inference failed for: r14v51 */
    /* JADX WARN: Type inference failed for: r17v100 */
    /* JADX WARN: Type inference failed for: r17v40 */
    /* JADX WARN: Type inference failed for: r17v43 */
    /* JADX WARN: Type inference failed for: r17v45 */
    /* JADX WARN: Type inference failed for: r17v46 */
    /* JADX WARN: Type inference failed for: r17v48 */
    /* JADX WARN: Type inference failed for: r17v49 */
    /* JADX WARN: Type inference failed for: r17v51 */
    /* JADX WARN: Type inference failed for: r17v53 */
    /* JADX WARN: Type inference failed for: r17v56 */
    /* JADX WARN: Type inference failed for: r17v58 */
    /* JADX WARN: Type inference failed for: r17v59 */
    /* JADX WARN: Type inference failed for: r17v60 */
    /* JADX WARN: Type inference failed for: r17v61 */
    /* JADX WARN: Type inference failed for: r17v62 */
    /* JADX WARN: Type inference failed for: r17v63 */
    /* JADX WARN: Type inference failed for: r17v64 */
    /* JADX WARN: Type inference failed for: r17v65 */
    /* JADX WARN: Type inference failed for: r17v66 */
    /* JADX WARN: Type inference failed for: r17v67 */
    /* JADX WARN: Type inference failed for: r17v68 */
    /* JADX WARN: Type inference failed for: r17v69 */
    /* JADX WARN: Type inference failed for: r17v70 */
    /* JADX WARN: Type inference failed for: r17v71 */
    /* JADX WARN: Type inference failed for: r17v73 */
    /* JADX WARN: Type inference failed for: r17v74 */
    /* JADX WARN: Type inference failed for: r17v75 */
    /* JADX WARN: Type inference failed for: r17v76 */
    /* JADX WARN: Type inference failed for: r17v77 */
    /* JADX WARN: Type inference failed for: r17v79 */
    /* JADX WARN: Type inference failed for: r17v83 */
    /* JADX WARN: Type inference failed for: r1v101 */
    /* JADX WARN: Type inference failed for: r1v107 */
    /* JADX WARN: Type inference failed for: r1v152, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r1v159 */
    /* JADX WARN: Type inference failed for: r1v167 */
    /* JADX WARN: Type inference failed for: r1v176, types: [com.android.server.am.UserController] */
    /* JADX WARN: Type inference failed for: r1v180, types: [com.android.server.am.ProviderMap] */
    /* JADX WARN: Type inference failed for: r1v184 */
    /* JADX WARN: Type inference failed for: r1v188 */
    /* JADX WARN: Type inference failed for: r1v189 */
    /* JADX WARN: Type inference failed for: r1v210, types: [android.app.usage.UsageStatsManagerInternal] */
    /* JADX WARN: Type inference failed for: r1v217 */
    /* JADX WARN: Type inference failed for: r1v248 */
    /* JADX WARN: Type inference failed for: r1v254, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r1v284, types: [android.content.pm.PackageManagerInternal] */
    /* JADX WARN: Type inference failed for: r1v286 */
    /* JADX WARN: Type inference failed for: r1v291 */
    /* JADX WARN: Type inference failed for: r1v293 */
    /* JADX WARN: Type inference failed for: r1v83 */
    /* JADX WARN: Type inference failed for: r1v85, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r1v86 */
    /* JADX WARN: Type inference failed for: r1v87 */
    /* JADX WARN: Type inference failed for: r23v0 */
    /* JADX WARN: Type inference failed for: r23v1 */
    /* JADX WARN: Type inference failed for: r23v13 */
    /* JADX WARN: Type inference failed for: r23v14 */
    /* JADX WARN: Type inference failed for: r23v16 */
    /* JADX WARN: Type inference failed for: r23v17 */
    /* JADX WARN: Type inference failed for: r23v18 */
    /* JADX WARN: Type inference failed for: r23v19 */
    /* JADX WARN: Type inference failed for: r23v2 */
    /* JADX WARN: Type inference failed for: r23v20 */
    /* JADX WARN: Type inference failed for: r23v21 */
    /* JADX WARN: Type inference failed for: r23v22 */
    /* JADX WARN: Type inference failed for: r23v23 */
    /* JADX WARN: Type inference failed for: r23v24 */
    /* JADX WARN: Type inference failed for: r23v25 */
    /* JADX WARN: Type inference failed for: r23v27 */
    /* JADX WARN: Type inference failed for: r23v28 */
    /* JADX WARN: Type inference failed for: r23v29 */
    /* JADX WARN: Type inference failed for: r23v3 */
    /* JADX WARN: Type inference failed for: r23v30 */
    /* JADX WARN: Type inference failed for: r23v33 */
    /* JADX WARN: Type inference failed for: r23v36 */
    /* JADX WARN: Type inference failed for: r23v37 */
    /* JADX WARN: Type inference failed for: r23v38 */
    /* JADX WARN: Type inference failed for: r23v4 */
    /* JADX WARN: Type inference failed for: r23v42 */
    /* JADX WARN: Type inference failed for: r23v43 */
    /* JADX WARN: Type inference failed for: r23v5 */
    /* JADX WARN: Type inference failed for: r23v54 */
    /* JADX WARN: Type inference failed for: r23v55 */
    /* JADX WARN: Type inference failed for: r23v56 */
    /* JADX WARN: Type inference failed for: r23v57 */
    /* JADX WARN: Type inference failed for: r23v58 */
    /* JADX WARN: Type inference failed for: r23v59 */
    /* JADX WARN: Type inference failed for: r23v60 */
    /* JADX WARN: Type inference failed for: r23v61 */
    /* JADX WARN: Type inference failed for: r23v62 */
    /* JADX WARN: Type inference failed for: r23v63 */
    /* JADX WARN: Type inference failed for: r23v64 */
    /* JADX WARN: Type inference failed for: r23v65 */
    /* JADX WARN: Type inference failed for: r23v66 */
    /* JADX WARN: Type inference failed for: r23v67 */
    /* JADX WARN: Type inference failed for: r23v68 */
    /* JADX WARN: Type inference failed for: r23v69 */
    /* JADX WARN: Type inference failed for: r23v7 */
    /* JADX WARN: Type inference failed for: r23v70 */
    /* JADX WARN: Type inference failed for: r23v71 */
    /* JADX WARN: Type inference failed for: r23v72 */
    /* JADX WARN: Type inference failed for: r23v73 */
    /* JADX WARN: Type inference failed for: r23v74 */
    /* JADX WARN: Type inference failed for: r23v75 */
    /* JADX WARN: Type inference failed for: r23v76 */
    /* JADX WARN: Type inference failed for: r23v77 */
    /* JADX WARN: Type inference failed for: r23v78 */
    /* JADX WARN: Type inference failed for: r23v79 */
    /* JADX WARN: Type inference failed for: r23v8 */
    /* JADX WARN: Type inference failed for: r23v80 */
    /* JADX WARN: Type inference failed for: r23v81 */
    /* JADX WARN: Type inference failed for: r23v82 */
    /* JADX WARN: Type inference failed for: r27v15 */
    /* JADX WARN: Type inference failed for: r27v26 */
    /* JADX WARN: Type inference failed for: r27v27 */
    /* JADX WARN: Type inference failed for: r27v6 */
    /* JADX WARN: Type inference failed for: r27v7 */
    /* JADX WARN: Type inference failed for: r27v8 */
    /* JADX WARN: Type inference failed for: r27v9 */
    /* JADX WARN: Type inference failed for: r2v123 */
    /* JADX WARN: Type inference failed for: r2v196, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r39v10 */
    /* JADX WARN: Type inference failed for: r39v11 */
    /* JADX WARN: Type inference failed for: r39v12 */
    /* JADX WARN: Type inference failed for: r39v13 */
    /* JADX WARN: Type inference failed for: r39v5 */
    /* JADX WARN: Type inference failed for: r39v6 */
    /* JADX WARN: Type inference failed for: r39v7 */
    /* JADX WARN: Type inference failed for: r39v8 */
    /* JADX WARN: Type inference failed for: r39v9 */
    /* JADX WARN: Type inference failed for: r4v144 */
    /* JADX WARN: Type inference failed for: r4v145 */
    /* JADX WARN: Type inference failed for: r4v146 */
    /* JADX WARN: Type inference failed for: r4v147 */
    /* JADX WARN: Type inference failed for: r4v50 */
    /* JADX WARN: Type inference failed for: r4v51 */
    /* JADX WARN: Type inference failed for: r54v26 */
    /* JADX WARN: Type inference failed for: r54v51 */
    /* JADX WARN: Type inference failed for: r5v120 */
    /* JADX WARN: Type inference failed for: r5v124, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r5v135 */
    /* JADX WARN: Type inference failed for: r5v136 */
    /* JADX WARN: Type inference failed for: r5v137 */
    /* JADX WARN: Type inference failed for: r5v138 */
    /* JADX WARN: Type inference failed for: r5v139 */
    /* JADX WARN: Type inference failed for: r5v140 */
    /* JADX WARN: Type inference failed for: r5v70, types: [com.android.server.am.ActivityManagerService] */
    /* JADX WARN: Type inference failed for: r5v82, types: [int] */
    /* JADX WARN: Type inference failed for: r5v88 */
    /* JADX WARN: Type inference failed for: r5v89 */
    /* JADX WARN: Type inference failed for: r7v21 */
    /* JADX WARN: Type inference failed for: r7v22, types: [int] */
    /* JADX WARN: Type inference failed for: r7v30 */
    /* JADX WARN: Type inference failed for: r84v0, types: [com.android.server.am.ContentProviderHelper] */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 13 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 15 */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.app.ContentProviderHolder getContentProviderImpl(android.app.IApplicationThread r85, java.lang.String r86, android.os.IBinder r87, int r88, java.lang.String r89, java.lang.String r90, boolean r91, int r92) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 5425
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ContentProviderHelper.getContentProviderImpl(android.app.IApplicationThread, java.lang.String, android.os.IBinder, int, java.lang.String, java.lang.String, boolean, int):android.app.ContentProviderHolder");
    }

    private void checkAssociationAndPermissionLocked(com.android.server.am.ProcessRecord callingApp, android.content.pm.ProviderInfo cpi, int callingUid, int userId, boolean checkUser, java.lang.String cprName, long startTime) {
        java.lang.String msg = checkContentProviderAssociation(callingApp, callingUid, cpi);
        if (msg != null) {
            throw new java.lang.SecurityException("Content provider lookup " + cprName + " failed: association not allowed with package " + msg);
        }
        checkTime(startTime, "getContentProviderImpl: before checkContentProviderPermission");
        java.lang.String msg2 = checkContentProviderPermission(cpi, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, checkUser, callingApp != null ? callingApp.toString() : null);
        if (msg2 != null) {
            throw new java.lang.SecurityException(msg2);
        }
        checkTime(startTime, "getContentProviderImpl: after checkContentProviderPermission");
    }

    void publishContentProviders(android.app.IApplicationThread caller, java.util.List<android.app.ContentProviderHolder> providers) {
        int size;
        boolean providersPublished;
        if (providers == null) {
            return;
        }
        this.mService.enforceNotIsolatedOrSdkSandboxCaller("publishContentProviders");
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ProcessRecord r = this.mService.getRecordForAppLOSP(caller);
                if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU && r != null) {
                    android.util.Slog.v("ActivityManager_MU", "ProcessRecord uid = " + r.uid);
                }
                if (r == null) {
                    throw new java.lang.SecurityException("Unable to find app for caller " + caller + " (pid=" + android.os.Binder.getCallingPid() + ") when publishing content providers");
                }
                long origId = android.os.Binder.clearCallingIdentity();
                boolean providersPublished2 = false;
                int i = 0;
                int size2 = providers.size();
                while (i < size2) {
                    android.app.ContentProviderHolder src = providers.get(i);
                    if (src == null || src.info == null) {
                        size = size2;
                    } else if (src.provider == null) {
                        size = size2;
                    } else {
                        com.android.server.am.ContentProviderRecord dst = r.mProviders.getProvider(src.info.name);
                        if (dst == null) {
                            size = size2;
                        } else {
                            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
                                android.util.Slog.v("ActivityManager_MU", "ContentProviderRecord uid = " + dst.uid);
                            }
                            boolean providersPublished3 = true;
                            android.content.ComponentName comp = new android.content.ComponentName(dst.info.packageName, dst.info.name);
                            this.mProviderMap.putProviderByClass(comp, dst);
                            java.lang.String[] names = dst.info.authority.split(";");
                            int j = 0;
                            while (j < names.length) {
                                this.mProviderMap.putProviderByName(names[j], dst);
                                j++;
                                size2 = size2;
                            }
                            size = size2;
                            int numLaunching = this.mLaunchingProviders.size();
                            int j2 = 0;
                            int j3 = 0;
                            while (true) {
                                providersPublished = providersPublished3;
                                if (j3 >= numLaunching) {
                                    break;
                                }
                                if (this.mLaunchingProviders.get(j3) == dst) {
                                    this.mLaunchingProviders.remove(j3);
                                    numLaunching--;
                                    this.mContentProviderHelperExt.updateExecutingComponent(dst.info.applicationInfo.uid, 2);
                                    j3--;
                                    j2 = 1;
                                }
                                j3++;
                                providersPublished3 = providersPublished;
                            }
                            if (j2 != 0) {
                                this.mService.mHandler.removeMessages(73, dst);
                                this.mService.mHandler.removeMessages(57, r);
                            }
                            r.addPackage(dst.info.applicationInfo.packageName, dst.info.applicationInfo.longVersionCode, this.mService.mProcessStats);
                            synchronized (dst) {
                                dst.provider = src.provider;
                                dst.setProcess(r);
                                dst.notifyAll();
                                dst.onProviderPublishStatusLocked(true);
                            }
                            dst.mRestartCount = 0;
                            if (hasProviderConnectionLocked(r)) {
                                r.mProfile.addHostingComponentType(64);
                            }
                            providersPublished2 = providersPublished;
                        }
                    }
                    i++;
                    size2 = size;
                }
                if (providersPublished2) {
                    this.mService.updateOomAdjLocked(r, 7);
                    int size3 = providers.size();
                    for (int i2 = 0; i2 < size3; i2++) {
                        android.app.ContentProviderHolder src2 = providers.get(i2);
                        if (src2 != null && src2.info != null && src2.provider != null) {
                            maybeUpdateProviderUsageStatsLocked(r, src2.info.packageName, src2.info.authority);
                        }
                    }
                }
                android.os.Binder.restoreCallingIdentity(origId);
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    void removeContentProvider(android.os.IBinder connection, boolean stable) {
        this.mService.enforceNotIsolatedCaller("removeContentProvider");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            try {
                com.android.server.am.ContentProviderConnection conn = (com.android.server.am.ContentProviderConnection) connection;
                if (conn == null) {
                    throw new java.lang.NullPointerException("connection is null");
                }
                com.android.server.am.ActivityManagerService.traceBegin(64L, "removeContentProvider: ", (conn.provider == null || conn.provider.info == null) ? "" : conn.provider.info.authority);
                try {
                    com.android.server.am.ActivityManagerService activityManagerService = this.mService;
                    com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                    synchronized (activityManagerService) {
                        try {
                            decProviderCountLocked(conn, null, null, stable, true, true);
                        } catch (java.lang.Throwable th) {
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            throw th;
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    android.os.Binder.restoreCallingIdentity(ident);
                } finally {
                    android.os.Trace.traceEnd(64L);
                }
            } catch (java.lang.ClassCastException e) {
                java.lang.String msg = "removeContentProvider: " + connection + " not a ContentProviderConnection";
                android.util.Slog.w(TAG, msg);
                throw new java.lang.IllegalArgumentException(msg);
            }
        } catch (java.lang.Throwable th2) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th2;
        }
    }

    void removeContentProviderExternalAsUser(java.lang.String name, android.os.IBinder token, int userId) {
        this.mService.enforceCallingPermission("android.permission.ACCESS_CONTENT_PROVIDERS_EXTERNALLY", "Do not have permission in call removeContentProviderExternal()");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            removeContentProviderExternalUnchecked(name, token, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void removeContentProviderExternalUnchecked(java.lang.String name, android.os.IBinder token, int userId) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ContentProviderRecord cpr = this.mProviderMap.getProviderByName(name, userId);
                if (cpr == null) {
                    if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_ALL) {
                        android.util.Slog.v(TAG, name + " content provider not found in providers list");
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                android.content.ComponentName comp = new android.content.ComponentName(cpr.info.packageName, cpr.info.name);
                com.android.server.am.ContentProviderRecord localCpr = this.mProviderMap.getProviderByClass(comp, userId);
                if (localCpr.hasExternalProcessHandles()) {
                    if (localCpr.removeExternalProcessHandleLocked(token)) {
                        this.mService.updateOomAdjLocked(localCpr.proc, 8);
                    } else {
                        android.util.Slog.e(TAG, "Attempt to remove content provider " + localCpr + " with no external reference for token: " + token + ".");
                    }
                } else {
                    android.util.Slog.e(TAG, "Attempt to remove content provider: " + localCpr + " with no external references.");
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    boolean refContentProvider(android.os.IBinder connection, int stable, int unstable) {
        try {
            com.android.server.am.ContentProviderConnection conn = (com.android.server.am.ContentProviderConnection) connection;
            if (conn == null) {
                throw new java.lang.NullPointerException("connection is null");
            }
            com.android.server.am.ActivityManagerService.traceBegin(64L, "refContentProvider: ", (conn.provider == null || conn.provider.info == null) ? "" : conn.provider.info.authority);
            try {
                conn.adjustCounts(stable, unstable);
                return !conn.dead;
            } finally {
                android.os.Trace.traceEnd(64L);
            }
        } catch (java.lang.ClassCastException e) {
            java.lang.String msg = "refContentProvider: " + connection + " not a ContentProviderConnection";
            android.util.Slog.w(TAG, msg);
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    /* JADX WARN: Finally extract failed */
    void unstableProviderDied(android.os.IBinder connection) {
        android.content.IContentProvider provider;
        try {
            com.android.server.am.ContentProviderConnection conn = (com.android.server.am.ContentProviderConnection) connection;
            if (conn == null) {
                throw new java.lang.NullPointerException("connection is null");
            }
            com.android.server.am.ActivityManagerService.traceBegin(64L, "unstableProviderDied: ", (conn.provider == null || conn.provider.info == null) ? "" : conn.provider.info.authority);
            try {
                com.android.server.am.ActivityManagerService activityManagerService = this.mService;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService) {
                    try {
                        provider = conn.provider.provider;
                    } finally {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
            } finally {
            }
            if (provider == null) {
                return;
            }
            if (provider.asBinder().pingBinder()) {
                com.android.server.am.ActivityManagerService activityManagerService2 = this.mService;
                com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
                synchronized (activityManagerService2) {
                    try {
                        android.util.Slog.w(TAG, "unstableProviderDied: caller " + android.os.Binder.getCallingUid() + " says " + conn + " died, but we don't agree");
                    } catch (java.lang.Throwable th) {
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                return;
            }
            com.android.server.am.ActivityManagerService activityManagerService3 = this.mService;
            com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
            synchronized (activityManagerService3) {
                try {
                    if (conn.provider.provider != provider) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                    com.android.server.am.ProcessRecord proc = conn.provider.proc;
                    if (proc != null && proc.getThread() != null) {
                        this.mService.reportUidInfoMessageLocked(TAG, "Process " + proc.processName + " (pid " + proc.getPid() + ") early provider death", proc.info.uid);
                        long token = android.os.Binder.clearCallingIdentity();
                        try {
                            this.mService.appDiedLocked(proc, "unstable content provider");
                            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                            return;
                        } finally {
                            android.os.Binder.restoreCallingIdentity(token);
                        }
                    }
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    return;
                } finally {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                }
            }
            android.os.Trace.traceEnd(64L);
        } catch (java.lang.ClassCastException e) {
            java.lang.String msg = "refContentProvider: " + connection + " not a ContentProviderConnection";
            android.util.Slog.w(TAG, msg);
            throw new java.lang.IllegalArgumentException(msg);
        }
    }

    void appNotRespondingViaProvider(android.os.IBinder connection) {
        this.mService.enforceCallingPermission("android.permission.REMOVE_TASKS", "appNotRespondingViaProvider()");
        com.android.server.am.ContentProviderConnection conn = (com.android.server.am.ContentProviderConnection) connection;
        if (conn == null) {
            android.util.Slog.w(TAG, "ContentProviderConnection is null");
            return;
        }
        com.android.server.am.ActivityManagerService.traceBegin(64L, "appNotRespondingViaProvider: ", (conn.provider == null || conn.provider.info == null) ? "" : conn.provider.info.authority);
        try {
            com.android.server.am.ProcessRecord host = conn.provider.proc;
            if (host == null) {
                android.util.Slog.w(TAG, "Failed to find hosting ProcessRecord");
            } else {
                com.android.internal.os.TimeoutRecord timeoutRecord = com.android.internal.os.TimeoutRecord.forContentProvider("ContentProvider not responding");
                this.mService.mAnrHelper.appNotResponding(host, timeoutRecord);
            }
        } finally {
            android.os.Trace.traceEnd(64L);
        }
    }

    void getMimeTypeFilterAsync(final android.net.Uri uri, int userId, final android.os.RemoteCallback resultCallback) {
        this.mService.enforceNotIsolatedCaller("getProviderMimeTypeAsync");
        final java.lang.String name = uri.getAuthority();
        final int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        final int safeUserId = this.mService.mUserController.unsafeConvertIncomingUser(userId);
        long ident = canClearIdentity(callingPid, callingUid, safeUserId) ? android.os.Binder.clearCallingIdentity() : 0L;
        try {
            android.app.ContentProviderHolder holder = getContentProviderExternalUnchecked(name, null, callingUid, "*getmimetype*", safeUserId);
            try {
                try {
                } catch (android.os.RemoteException e) {
                    e = e;
                }
            } catch (android.os.RemoteException e2) {
                e = e2;
            }
            if (isHolderVisibleToCaller(holder, callingUid, safeUserId)) {
                if (checkGetAnyTypePermission(callingUid, callingPid)) {
                    try {
                        android.content.AttributionSource attributionSource = new android.content.AttributionSource.Builder(callingUid).build();
                        holder.provider.getTypeAsync(attributionSource, uri, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda0
                            public final void onResult(android.os.Bundle bundle) {
                                this.f$0.lambda$getMimeTypeFilterAsync$0(name, safeUserId, resultCallback, bundle);
                            }
                        }));
                        return;
                    } catch (android.os.RemoteException e3) {
                        e = e3;
                    }
                } else {
                    holder.provider.getTypeAnonymousAsync(uri, new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda1
                        public final void onResult(android.os.Bundle bundle) {
                            this.f$0.lambda$getMimeTypeFilterAsync$1(name, safeUserId, resultCallback, callingUid, uri, bundle);
                        }
                    }));
                }
                android.util.Log.w(TAG, "Content provider dead retrieving " + uri, e);
                resultCallback.sendResult(android.os.Bundle.EMPTY);
                return;
            }
            resultCallback.sendResult(android.os.Bundle.EMPTY);
        } finally {
            if (ident != 0) {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMimeTypeFilterAsync$0(java.lang.String name, int safeUserId, android.os.RemoteCallback resultCallback, android.os.Bundle result) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            removeContentProviderExternalUnchecked(name, null, safeUserId);
            android.os.Binder.restoreCallingIdentity(identity);
            resultCallback.sendResult(result);
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getMimeTypeFilterAsync$1(java.lang.String name, int safeUserId, android.os.RemoteCallback resultCallback, int callingUid, android.net.Uri uri, android.os.Bundle result) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            removeContentProviderExternalUnchecked(name, null, safeUserId);
            android.os.Binder.restoreCallingIdentity(identity);
            resultCallback.sendResult(result);
            java.lang.String type = result.getPairValue();
            if (type != null) {
                logGetTypeData(callingUid, uri, type);
            }
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    private boolean checkGetAnyTypePermission(int callingUid, int callingPid) {
        if (this.mService.checkPermission("android.permission.GET_ANY_PROVIDER_TYPE", callingPid, callingUid) == 0) {
            return true;
        }
        return false;
    }

    private void logGetTypeData(int callingUid, android.net.Uri uri, java.lang.String type) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.GET_TYPE_ACCESSED_WITHOUT_PERMISSION, 1, callingUid, uri.getAuthority(), type);
    }

    private boolean canClearIdentity(int callingPid, int callingUid, int userId) {
        return android.os.UserHandle.getUserId(callingUid) == userId || com.android.server.am.ActivityManagerService.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS", callingPid, callingUid, -1, true) == 0 || com.android.server.am.ActivityManagerService.checkComponentPermission("android.permission.INTERACT_ACROSS_USERS_FULL", callingPid, callingUid, -1, true) == 0;
    }

    private boolean isHolderVisibleToCaller(android.app.ContentProviderHolder holder, int callingUid, int userId) {
        if (holder == null || holder.info == null) {
            return false;
        }
        if (isAuthorityRedirectedForCloneProfileCached(holder.info.authority) && resolveParentUserIdForCloneProfile(userId) != userId) {
            return !this.mService.getPackageManagerInternal().filterAppAccess(holder.info.packageName, callingUid, userId, false);
        }
        return !this.mService.getPackageManagerInternal().filterAppAccess(holder.info.packageName, callingUid, userId);
    }

    private static int resolveParentUserIdForCloneProfile(int userId) {
        com.android.server.pm.UserManagerInternal umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        android.content.pm.UserInfo userInfo = umInternal.getUserInfo(userId);
        if (userInfo == null || !userInfo.isCloneProfile()) {
            return userId;
        }
        return umInternal.getProfileParentId(userId);
    }

    java.lang.String checkContentProviderAccess(java.lang.String authority, int userId) {
        com.android.server.pm.UserManagerInternal umInternal;
        android.content.pm.UserInfo userInfo;
        boolean checkUser = true;
        if (userId == -1) {
            this.mService.mContext.enforceCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL", TAG);
            userId = android.os.UserHandle.getCallingUserId();
        }
        if (isAuthorityRedirectedForCloneProfileCached(authority) && (userInfo = (umInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class)).getUserInfo(userId)) != null && userInfo.isCloneProfile()) {
            userId = umInternal.getProfileParentId(userId);
            checkUser = false;
        }
        android.content.pm.ProviderInfo cpi = null;
        try {
            cpi = android.app.AppGlobals.getPackageManager().resolveContentProvider(authority, 790016L, userId);
        } catch (android.os.RemoteException e) {
        }
        if (cpi == null) {
            return "Failed to find provider " + authority + " for user " + userId + "; expected to find a valid ContentProvider for this authority";
        }
        int callingPid = android.os.Binder.getCallingPid();
        synchronized (this.mService.mPidsSelfLocked) {
            com.android.server.am.ProcessRecord r = this.mService.mPidsSelfLocked.get(callingPid);
            if (r == null) {
                return "Failed to find PID " + callingPid;
            }
            java.lang.String appName = r.toString();
            return checkContentProviderPermission(cpi, callingPid, android.os.Binder.getCallingUid(), userId, checkUser, appName);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0 */
    /* JADX WARN: Type inference failed for: r12v1, types: [android.os.IBinder] */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r17v0, types: [com.android.server.am.ContentProviderHelper] */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 3 */
    int checkContentProviderUriPermission(android.net.Uri uri, int userId, int callingUid, int modeFlags) throws java.lang.Throwable {
        android.app.ContentProviderHolder holder;
        ?? r12 = -1;
        if (java.lang.Thread.holdsLock(this.mService.mActivityTaskManager.getGlobalLock())) {
            android.util.Slog.wtf(TAG, new java.lang.IllegalStateException("Unable to check Uri permission because caller is holding WM lock; assuming permission denied"));
            return -1;
        }
        java.lang.String name = uri.getAuthority();
        long ident = android.os.Binder.clearCallingIdentity();
        android.app.ContentProviderHolder holder2 = null;
        try {
            r12 = 0;
            try {
                holder = getContentProviderExternalUnchecked(name, null, callingUid, "*checkContentProviderUriPermission*", userId);
            } catch (android.os.RemoteException e) {
                e = e;
            } catch (java.lang.Exception e2) {
                e = e2;
            } catch (java.lang.Throwable th) {
                th = th;
            }
            if (holder == null) {
                if (holder != null) {
                    try {
                        removeContentProviderExternalUnchecked(name, null, userId);
                    } finally {
                    }
                }
                android.os.Binder.restoreCallingIdentity(ident);
                return -1;
            }
            try {
                com.android.server.pm.pkg.AndroidPackage androidPackage = this.mService.getPackageManagerInternal().getPackage(android.os.Binder.getCallingUid());
                if (androidPackage == null) {
                    if (holder != null) {
                        try {
                            removeContentProviderExternalUnchecked(name, null, userId);
                        } finally {
                        }
                    }
                    android.os.Binder.restoreCallingIdentity(ident);
                    return -1;
                }
                android.content.AttributionSource attributionSource = new android.content.AttributionSource(callingUid, androidPackage.getPackageName(), null);
                try {
                    int iCheckUriPermission = holder.provider.checkUriPermission(attributionSource, uri, callingUid, modeFlags);
                    if (holder != null) {
                        try {
                            removeContentProviderExternalUnchecked(name, null, userId);
                        } finally {
                        }
                    }
                    return iCheckUriPermission;
                } catch (android.os.RemoteException e3) {
                    e = e3;
                } catch (java.lang.Exception e4) {
                    e = e4;
                    holder2 = holder;
                    android.util.Log.w(TAG, "Exception while determining type of " + uri, e);
                    if (holder2 != null) {
                        try {
                            removeContentProviderExternalUnchecked(name, null, userId);
                        } finally {
                        }
                    }
                    android.os.Binder.restoreCallingIdentity(ident);
                    return -1;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    holder2 = holder;
                    if (holder2 != null) {
                        try {
                            removeContentProviderExternalUnchecked(name, r12, userId);
                        } finally {
                        }
                    }
                    throw th;
                }
            } catch (android.os.RemoteException e5) {
                e = e5;
            } catch (java.lang.Exception e6) {
                e = e6;
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
            holder2 = holder;
            android.util.Log.w(TAG, "Content provider dead retrieving " + uri, e);
            if (holder2 != null) {
                try {
                    removeContentProviderExternalUnchecked(name, null, userId);
                } finally {
                }
            }
            android.os.Binder.restoreCallingIdentity(ident);
            return -1;
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    void processContentProviderPublishTimedOutLocked(com.android.server.am.ProcessRecord app) {
        cleanupAppInLaunchingProvidersLocked(app, true);
        this.mService.mProcessList.removeProcessLocked(app, false, true, 7, 0, "timeout publishing content providers");
    }

    java.util.List<android.content.pm.ProviderInfo> generateApplicationProvidersLocked(com.android.server.am.ProcessRecord app) {
        com.android.server.am.ContentProviderRecord cpr;
        try {
            java.util.List<android.content.pm.ProviderInfo> providers = android.app.AppGlobals.getPackageManager().queryContentProviders(app.processName, app.uid, 268438528L, (java.lang.String) null).getList();
            if (providers == null) {
                return null;
            }
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
                android.util.Slog.v("ActivityManager_MU", "generateApplicationProvidersLocked, app.info.uid = " + app.uid);
            }
            int numProviders = providers.size();
            com.android.server.am.ProcessProviderRecord pr = app.mProviders;
            pr.ensureProviderCapacity(pr.numberOfProviders() + numProviders);
            int i = 0;
            while (i < numProviders) {
                android.content.pm.ProviderInfo cpi = providers.get(i);
                boolean singleton = this.mService.isSingleton(cpi.processName, cpi.applicationInfo, cpi.name, cpi.flags);
                if (isSingletonOrSystemUserOnly(cpi) && app.userId != 0) {
                    providers.remove(i);
                    numProviders--;
                    i--;
                } else {
                    boolean isInstantApp = cpi.applicationInfo.isInstantApp();
                    boolean splitInstalled = cpi.splitName == null || com.android.internal.util.ArrayUtils.contains(cpi.applicationInfo.splitNames, cpi.splitName);
                    if (isInstantApp && !splitInstalled) {
                        providers.remove(i);
                        numProviders--;
                        i--;
                    } else {
                        android.content.ComponentName comp = new android.content.ComponentName(cpi.packageName, cpi.name);
                        com.android.server.am.ContentProviderRecord cpr2 = this.mProviderMap.getProviderByClass(comp, app.userId);
                        if (cpr2 == null) {
                            cpr = new com.android.server.am.ContentProviderRecord(this.mService, cpi, app.info, comp, singleton);
                            this.mProviderMap.putProviderByClass(comp, cpr);
                        } else {
                            cpr = cpr2;
                        }
                        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_MU) {
                            android.util.Slog.v("ActivityManager_MU", "generateApplicationProvidersLocked, cpi.uid = " + cpr.uid);
                        }
                        pr.installProvider(cpi.name, cpr);
                        if (!cpi.multiprocess || !com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(cpi.packageName)) {
                            app.addPackage(cpi.applicationInfo.packageName, cpi.applicationInfo.longVersionCode, this.mService.mProcessStats);
                        }
                        this.mService.notifyPackageUse(cpi.applicationInfo.packageName, 4);
                    }
                }
                i++;
            }
            if (providers.isEmpty()) {
                return null;
            }
            return providers;
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    private final class DevelopmentSettingsObserver extends android.database.ContentObserver {
        private final android.content.ComponentName mBugreportStorageProvider;
        private final android.net.Uri mUri;

        DevelopmentSettingsObserver() {
            super(com.android.server.am.ContentProviderHelper.this.mService.mHandler);
            this.mUri = android.provider.Settings.Global.getUriFor("development_settings_enabled");
            this.mBugreportStorageProvider = new android.content.ComponentName("com.android.shell", "com.android.shell.BugreportStorageProvider");
            com.android.server.am.ContentProviderHelper.this.mService.mContext.getContentResolver().registerContentObserver(this.mUri, false, this, -1);
            onChange();
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (this.mUri.equals(uri)) {
                onChange();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private void onChange() {
            com.android.server.am.ContentProviderHelper.this.mService.mContext.getPackageManager().setComponentEnabledSetting(this.mBugreportStorageProvider, (android.provider.Settings.Global.getInt(com.android.server.am.ContentProviderHelper.this.mService.mContext.getContentResolver(), "development_settings_enabled", android.os.Build.IS_ENG ? 1 : 0) != 0) == true ? 1 : 0, 0);
        }
    }

    public final void installSystemProviders() {
        java.util.List<android.content.pm.ProviderInfo> providers;
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ProcessRecord app = (com.android.server.am.ProcessRecord) this.mService.mProcessList.getProcessNamesLOSP().get("system", 1000);
                providers = generateApplicationProvidersLocked(app);
                if (providers != null) {
                    for (int i = providers.size() - 1; i >= 0; i--) {
                        android.content.pm.ProviderInfo pi = providers.get(i);
                        if ((pi.applicationInfo.flags & 1) == 0) {
                            android.util.Slog.w(TAG, "Not installing system proc provider " + pi.name + ": not system .apk");
                            providers.remove(i);
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        if (providers != null) {
            this.mService.mSystemThread.installSystemProviders(providers);
        }
        synchronized (this) {
            this.mSystemProvidersInstalled = true;
        }
        this.mService.mConstants.start(this.mService.mContext.getContentResolver());
        this.mService.mCoreSettingsObserver = new com.android.server.am.CoreSettingsObserver(this.mService);
        this.mService.mActivityTaskManager.installSystemProviders();
        new com.android.server.am.ContentProviderHelper.DevelopmentSettingsObserver();
        com.android.server.am.SettingsToPropertiesMapper.start(this.mService.mContext.getContentResolver());
        this.mService.mOomAdjuster.initSettings();
        com.android.server.RescueParty.onSettingsProviderPublished(this.mService.mContext);
    }

    void installEncryptionUnawareProviders(int userId) {
        com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mService.mProcLock;
        com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
        synchronized (activityManagerGlobalLock) {
            try {
                android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.ProcessRecord>> pmap = this.mService.mProcessList.getProcessNamesLOSP().getMap();
                int numProc = pmap.size();
                for (int iProc = 0; iProc < numProc; iProc++) {
                    android.util.SparseArray<com.android.server.am.ProcessRecord> apps = pmap.valueAt(iProc);
                    int numApps = apps.size();
                    for (int iApp = 0; iApp < numApps; iApp++) {
                        final com.android.server.am.ProcessRecord app = apps.valueAt(iApp);
                        if (app.userId == userId && app.getThread() != null && !app.isUnlocked()) {
                            app.getPkgList().forEachPackage(new java.util.function.Consumer() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda4
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    this.f$0.lambda$installEncryptionUnawareProviders$2(app, (java.lang.String) obj);
                                }
                            });
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$installEncryptionUnawareProviders$2(com.android.server.am.ProcessRecord app, java.lang.String pkgName) {
        try {
            try {
                android.content.pm.PackageInfo pkgInfo = android.app.AppGlobals.getPackageManager().getPackageInfo(pkgName, 262152L, app.userId);
                android.app.IApplicationThread thread = app.getThread();
                if (pkgInfo != null && !com.android.internal.util.ArrayUtils.isEmpty(pkgInfo.providers)) {
                    for (android.content.pm.ProviderInfo pi : pkgInfo.providers) {
                        boolean splitInstalled = true;
                        boolean processMatch = java.util.Objects.equals(pi.processName, app.processName) || pi.multiprocess;
                        try {
                            boolean userMatch = !isSingletonOrSystemUserOnly(pi) || app.userId == 0;
                            boolean isInstantApp = pi.applicationInfo.isInstantApp();
                            if (pi.splitName != null && !com.android.internal.util.ArrayUtils.contains(pi.applicationInfo.splitNames, pi.splitName)) {
                                splitInstalled = false;
                            }
                            if (!processMatch || !userMatch || (isInstantApp && !splitInstalled)) {
                                android.util.Log.v(TAG, "Skipping " + pi);
                            } else {
                                android.util.Log.v(TAG, "Installing " + pi);
                                thread.scheduleInstallProvider(pi);
                            }
                        } catch (android.os.RemoteException e) {
                            return;
                        }
                    }
                }
            } catch (android.os.RemoteException e2) {
            }
        } catch (android.os.RemoteException e3) {
        }
    }

    private com.android.server.am.ContentProviderConnection incProviderCountLocked(com.android.server.am.ProcessRecord r, com.android.server.am.ContentProviderRecord cpr, android.os.IBinder externalProcessToken, int callingUid, java.lang.String callingPackage, java.lang.String callingTag, boolean stable, boolean updateLru, long startTime, com.android.server.am.ProcessList processList, int expectedUserId) {
        if (r == null) {
            cpr.addExternalProcessHandleLocked(externalProcessToken, callingUid, callingTag);
            return null;
        }
        com.android.server.am.ProcessProviderRecord pr = r.mProviders;
        int size = pr.numberOfProviderConnections();
        for (int i = 0; i < size; i++) {
            com.android.server.am.ContentProviderConnection conn = pr.getProviderConnectionAt(i);
            if (conn.provider == cpr) {
                conn.incrementCount(stable);
                return conn;
            }
        }
        com.android.server.am.ContentProviderConnection conn2 = new com.android.server.am.ContentProviderConnection(cpr, r, callingPackage, expectedUserId);
        conn2.startAssociationIfNeeded();
        conn2.initializeCount(stable);
        cpr.connections.add(conn2);
        if (cpr.proc != null) {
            cpr.proc.mProfile.addHostingComponentType(64);
        }
        pr.addProviderConnection(conn2);
        this.mService.startAssociationLocked(r.uid, r.processName, r.mState.getCurProcState(), cpr.uid, cpr.appInfo.longVersionCode, cpr.name, cpr.info.processName);
        this.mContentProviderHelperExt.noteAssociation(r.uid, cpr.uid, true);
        if (updateLru && cpr.proc != null && r.mState.getSetAdj() <= 250) {
            checkTime(startTime, "getContentProviderImpl: before updateLruProcess");
            processList.updateLruProcessLocked(cpr.proc, false, null);
            checkTime(startTime, "getContentProviderImpl: after updateLruProcess");
        }
        return conn2;
    }

    private boolean decProviderCountLocked(final com.android.server.am.ContentProviderConnection conn, com.android.server.am.ContentProviderRecord cpr, android.os.IBinder externalProcessToken, final boolean stable, boolean enforceDelay, final boolean updateOomAdj) {
        if (conn == null) {
            cpr.removeExternalProcessHandleLocked(externalProcessToken);
            return false;
        }
        if (conn.totalRefCount() > 1) {
            conn.decrementCount(stable);
            return false;
        }
        if (enforceDelay) {
            com.android.server.OplusIoThread.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$decProviderCountLocked$3(conn, stable, updateOomAdj);
                }
            }, 5000L);
        } else {
            lambda$decProviderCountLocked$3(conn, stable, updateOomAdj);
        }
        return true;
    }

    private boolean hasProviderConnectionLocked(com.android.server.am.ProcessRecord proc) {
        for (int i = proc.mProviders.numberOfProviders() - 1; i >= 0; i--) {
            if (!proc.mProviders.getProviderAt(i).connections.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleProviderRemoval, reason: merged with bridge method [inline-methods] */
    public void lambda$decProviderCountLocked$3(com.android.server.am.ContentProviderConnection conn, boolean stable, boolean updateOomAdj) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            if (conn != null) {
                try {
                    if (conn.provider != null && conn.decrementCount(stable) == 0) {
                        com.android.server.am.ActivityManagerService.traceBegin(64L, "handleProviderRemoval: ", conn.toString());
                        android.os.Trace.traceEnd(64L);
                        if (DEBUG) {
                            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "handleProviderRemoval " + conn.toString());
                        }
                        com.android.server.am.ContentProviderRecord cpr = conn.provider;
                        conn.stopAssociation();
                        cpr.connections.remove(conn);
                        if (cpr.proc != null && !hasProviderConnectionLocked(cpr.proc)) {
                            cpr.proc.mProfile.clearHostingComponentType(64);
                        }
                        conn.client.mProviders.removeProviderConnection(conn);
                        if (conn.client.mState.getSetProcState() < 15 && cpr.proc != null) {
                            cpr.proc.mProviders.setLastProviderTime(android.os.SystemClock.uptimeMillis());
                        }
                        this.mService.stopAssociationLocked(conn.client.uid, conn.client.processName, cpr.uid, cpr.appInfo.longVersionCode, cpr.name, cpr.info.processName);
                        this.mContentProviderHelperExt.noteAssociation(conn.client.uid, cpr.uid, true);
                        if (updateOomAdj && (!com.android.server.am.Flags.serviceBindingOomAdjPolicy() || this.mService.mOomAdjuster.evaluateProviderConnectionRemoval(conn.client, cpr.proc))) {
                            this.mService.updateOomAdjLocked(conn.provider.proc, 8);
                        }
                        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                        return;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        }
    }

    private java.lang.String checkContentProviderPermission(android.content.pm.ProviderInfo cpi, int callingPid, int callingUid, int userId, boolean checkUser, java.lang.String appName) {
        boolean checkedGrants;
        int userId2;
        java.lang.String suffix;
        if (!canAccessContentProviderFromSdkSandbox(cpi, callingUid)) {
            return "ContentProvider access not allowed from sdk sandbox UID. ProviderInfo: " + cpi.toString();
        }
        if (!checkUser) {
            checkedGrants = false;
            userId2 = userId;
        } else {
            int tmpTargetUserId = this.mService.mUserController.unsafeConvertIncomingUser(userId);
            if (tmpTargetUserId == android.os.UserHandle.getUserId(callingUid)) {
                checkedGrants = false;
            } else {
                if (this.mService.mUgmInternal.checkAuthorityGrants(callingUid, cpi, tmpTargetUserId, checkUser)) {
                    return null;
                }
                checkedGrants = true;
            }
            userId2 = this.mService.mUserController.handleIncomingUser(callingPid, callingUid, userId, false, 0, "checkContentProviderPermissionLocked " + cpi.authority, null);
            if (userId2 != tmpTargetUserId) {
                checkedGrants = false;
            }
        }
        if (com.android.server.am.ActivityManagerService.checkComponentPermission(cpi.readPermission, callingPid, callingUid, cpi.applicationInfo.uid, cpi.exported) == 0 || com.android.server.am.ActivityManagerService.checkComponentPermission(cpi.writePermission, callingPid, callingUid, cpi.applicationInfo.uid, cpi.exported) == 0) {
            return null;
        }
        android.content.pm.PathPermission[] pps = cpi.pathPermissions;
        if (pps != null) {
            int i = pps.length;
            while (i > 0) {
                i--;
                android.content.pm.PathPermission pp = pps[i];
                java.lang.String pprperm = pp.getReadPermission();
                if (pprperm != null && com.android.server.am.ActivityManagerService.checkComponentPermission(pprperm, callingPid, callingUid, cpi.applicationInfo.uid, cpi.exported) == 0) {
                    return null;
                }
                java.lang.String ppwperm = pp.getWritePermission();
                if (ppwperm != null && com.android.server.am.ActivityManagerService.checkComponentPermission(ppwperm, callingPid, callingUid, cpi.applicationInfo.uid, cpi.exported) == 0) {
                    return null;
                }
            }
        }
        if (!checkedGrants && this.mService.mUgmInternal.checkAuthorityGrants(callingUid, cpi, userId2, checkUser)) {
            return null;
        }
        if (!cpi.exported) {
            suffix = " that is not exported from UID " + cpi.applicationInfo.uid;
        } else if ("android.permission.MANAGE_DOCUMENTS".equals(cpi.readPermission)) {
            suffix = " requires that you obtain access using ACTION_OPEN_DOCUMENT or related APIs";
        } else {
            suffix = " requires " + cpi.readPermission + " or " + cpi.writePermission;
        }
        java.lang.String msg = "Permission Denial: opening provider " + cpi.name + " from " + (appName != null ? appName : "(null)") + " (pid=" + callingPid + ", uid=" + callingUid + ")" + suffix;
        android.util.Slog.w(TAG, msg);
        return msg;
    }

    private java.lang.String checkContentProviderAssociation(final com.android.server.am.ProcessRecord callingApp, int callingUid, final android.content.pm.ProviderInfo cpi) {
        if (callingApp == null) {
            if (this.mService.validateAssociationAllowedLocked(cpi.packageName, cpi.applicationInfo.uid, null, callingUid)) {
                return null;
            }
            return "<null>";
        }
        java.lang.String r = (java.lang.String) callingApp.getPkgList().searchEachPackage(new java.util.function.Function() { // from class: com.android.server.am.ContentProviderHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$checkContentProviderAssociation$4(callingApp, cpi, (java.lang.String) obj);
            }
        });
        return r;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.String lambda$checkContentProviderAssociation$4(com.android.server.am.ProcessRecord callingApp, android.content.pm.ProviderInfo cpi, java.lang.String pkgName) {
        if (!this.mService.validateAssociationAllowedLocked(pkgName, callingApp.uid, cpi.packageName, cpi.applicationInfo.uid)) {
            return cpi.packageName;
        }
        return null;
    }

    android.content.pm.ProviderInfo getProviderInfoLocked(java.lang.String authority, int userId, int pmFlags) {
        com.android.server.am.ContentProviderRecord cpr = this.mProviderMap.getProviderByName(authority, userId);
        if (cpr != null) {
            return cpr.info;
        }
        try {
            return android.app.AppGlobals.getPackageManager().resolveContentProvider(authority, pmFlags | 2048, userId);
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    private void maybeUpdateProviderUsageStatsLocked(com.android.server.am.ProcessRecord app, java.lang.String providerPkgName, java.lang.String authority) {
        com.android.server.am.UserState userState;
        if (app == null || app.mState.getCurProcState() > 6 || (userState = this.mService.mUserController.getStartedUserState(app.userId)) == null) {
            return;
        }
        long now = android.os.SystemClock.elapsedRealtime();
        java.lang.Long lastReported = userState.mProviderLastReportedFg.get(authority);
        if (lastReported == null || lastReported.longValue() < now - 60000) {
            if (this.mService.mSystemReady) {
                this.mService.mUsageStatsService.reportContentProviderUsage(authority, providerPkgName, app.userId);
            }
            userState.mProviderLastReportedFg.put(authority, java.lang.Long.valueOf(now));
        }
    }

    private boolean isProcessAliveLocked(com.android.server.am.ProcessRecord proc) {
        int pid = proc.getPid();
        if (pid <= 0) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "Process hasn't started yet: " + proc);
            }
            return false;
        }
        java.lang.String procStatFile = "/proc/" + pid + "/stat";
        this.mProcessStateStatsLongs[0] = 0;
        if (!android.os.Process.readProcFile(procStatFile, PROCESS_STATE_STATS_FORMAT, null, this.mProcessStateStatsLongs, null)) {
            if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
                android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "UNABLE TO RETRIEVE STATE FOR " + procStatFile);
            }
            return false;
        }
        long state = this.mProcessStateStatsLongs[0];
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_OOM_ADJ) {
            android.util.Slog.d(com.android.server.am.IActivityManagerServiceExt.TAG, "RETRIEVED STATE FOR " + procStatFile + ": " + ((char) state));
        }
        return (state == 90 || state == 88 || state == 120 || state == 75 || android.os.Process.getUidForPid(pid) != proc.uid) ? false : true;
    }

    private static final class StartActivityRunnable implements java.lang.Runnable {
        private final android.content.Context mContext;
        private final android.content.Intent mIntent;
        private final android.os.UserHandle mUserHandle;

        StartActivityRunnable(android.content.Context context, android.content.Intent intent, android.os.UserHandle userHandle) {
            this.mContext = context;
            this.mIntent = intent;
            this.mUserHandle = userHandle;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mContext.startActivityAsUser(this.mIntent, this.mUserHandle);
        }
    }

    private boolean requestTargetProviderPermissionsReviewIfNeededLocked(android.content.pm.ProviderInfo cpi, com.android.server.am.ProcessRecord r, int userId, android.content.Context context) {
        boolean callerForeground = true;
        if (!this.mService.getPackageManagerInternal().isPermissionsReviewRequired(cpi.packageName, userId)) {
            return true;
        }
        if (r != null && r.mState.getSetSchedGroup() == 0) {
            callerForeground = false;
        }
        if (!callerForeground) {
            android.util.Slog.w(TAG, "u" + userId + " Instantiating a provider in package " + cpi.packageName + " requires a permissions review");
            return false;
        }
        android.content.Intent intent = new android.content.Intent("android.intent.action.REVIEW_PERMISSIONS");
        intent.addFlags(276824064);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", cpi.packageName);
        if (com.android.server.am.ActivityManagerDebugConfig.DEBUG_PERMISSIONS_REVIEW) {
            android.util.Slog.i(TAG, "u" + userId + " Launching permission review for package " + cpi.packageName);
        }
        android.os.UserHandle userHandle = new android.os.UserHandle(userId);
        this.mService.mHandler.post(new com.android.server.am.ContentProviderHelper.StartActivityRunnable(context, intent, userHandle));
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x001c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean removeDyingProviderLocked(com.android.server.am.ProcessRecord r22, com.android.server.am.ContentProviderRecord r23, boolean r24) {
        /*
            Method dump skipped, instruction units count: 423
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ContentProviderHelper.removeDyingProviderLocked(com.android.server.am.ProcessRecord, com.android.server.am.ContentProviderRecord, boolean):boolean");
    }

    boolean checkAppInLaunchingProvidersLocked(com.android.server.am.ProcessRecord app) {
        for (int i = this.mLaunchingProviders.size() - 1; i >= 0; i--) {
            com.android.server.am.ContentProviderRecord cpr = this.mLaunchingProviders.get(i);
            if (cpr.launchingApp == app) {
                return true;
            }
        }
        return false;
    }

    boolean cleanupAppInLaunchingProvidersLocked(com.android.server.am.ProcessRecord app, boolean alwaysBad) {
        boolean restart = false;
        for (int i = this.mLaunchingProviders.size() - 1; i >= 0; i--) {
            com.android.server.am.ContentProviderRecord cpr = this.mLaunchingProviders.get(i);
            if (cpr.launchingApp == app) {
                int i2 = cpr.mRestartCount + 1;
                cpr.mRestartCount = i2;
                if (i2 > 3) {
                    alwaysBad = true;
                }
                if (!alwaysBad && !app.mErrorState.isBad() && cpr.hasConnectionOrHandle()) {
                    restart = true;
                } else {
                    removeDyingProviderLocked(app, cpr, true);
                }
            }
        }
        return restart;
    }

    void cleanupLaunchingProvidersLocked() {
        for (int i = this.mLaunchingProviders.size() - 1; i >= 0; i--) {
            com.android.server.am.ContentProviderRecord cpr = this.mLaunchingProviders.get(i);
            if (cpr.connections.size() <= 0 && !cpr.hasExternalProcessHandles()) {
                synchronized (cpr) {
                    cpr.launchingApp = null;
                    cpr.notifyAll();
                }
            }
        }
    }

    private void checkTime(long startTime, java.lang.String where) {
        long now = android.os.SystemClock.uptimeMillis();
        if (now - startTime > 50) {
            android.util.Slog.w(TAG, "Slow operation: " + (now - startTime) + "ms so far, now at " + where);
        }
    }

    void dumpProvidersLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args, int opti, boolean dumpAll, java.lang.String dumpPackage) {
        com.android.server.am.ActivityManagerService.ItemMatcher matcher = new com.android.server.am.ActivityManagerService.ItemMatcher();
        matcher.build(args, opti);
        pw.println("ACTIVITY MANAGER CONTENT PROVIDERS (dumpsys activity providers)");
        boolean needSep = this.mProviderMap.dumpProvidersLocked(pw, dumpAll, dumpPackage);
        boolean printedAnything = needSep;
        if (this.mLaunchingProviders.size() > 0) {
            boolean printed = false;
            for (int i = this.mLaunchingProviders.size() - 1; i >= 0; i--) {
                com.android.server.am.ContentProviderRecord r = this.mLaunchingProviders.get(i);
                if (dumpPackage == null || dumpPackage.equals(r.name.getPackageName())) {
                    if (!printed) {
                        if (needSep) {
                            pw.println();
                        }
                        needSep = true;
                        pw.println("  Launching content providers:");
                        printed = true;
                        printedAnything = true;
                    }
                    pw.print("  Launching #");
                    pw.print(i);
                    pw.print(": ");
                    pw.println(r);
                }
            }
        }
        if (!printedAnything) {
            pw.println("  (nothing)");
        }
    }

    private boolean canAccessContentProviderFromSdkSandbox(android.content.pm.ProviderInfo cpi, int callingUid) {
        if (!android.os.Process.isSdkSandboxUid(callingUid)) {
            return true;
        }
        com.android.server.sdksandbox.SdkSandboxManagerLocal sdkSandboxManagerLocal = (com.android.server.sdksandbox.SdkSandboxManagerLocal) com.android.server.LocalManagerRegistry.getManager(com.android.server.sdksandbox.SdkSandboxManagerLocal.class);
        if (sdkSandboxManagerLocal == null) {
            throw new java.lang.IllegalStateException("SdkSandboxManagerLocal not found when checking whether SDK sandbox uid may access the contentprovider.");
        }
        return sdkSandboxManagerLocal.canAccessContentProviderFromSdkSandbox(cpi);
    }

    protected boolean dumpProvider(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String name, java.lang.String[] args, int opti, boolean dumpAll) {
        return this.mProviderMap.dumpProvider(fd, pw, name, args, opti, dumpAll);
    }

    protected boolean dumpProviderProto(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String name, java.lang.String[] args) {
        return this.mProviderMap.dumpProviderProto(fd, pw, name, args);
    }

    private boolean isAuthorityRedirectedForCloneProfileCached(java.lang.String auth) {
        if (this.mCloneProfileAuthorityRedirectionCache.containsKey(auth)) {
            java.lang.Boolean retVal = this.mCloneProfileAuthorityRedirectionCache.get(auth);
            if (retVal == null) {
                return false;
            }
            return retVal.booleanValue();
        }
        boolean isAuthRedirected = android.content.ContentProvider.isAuthorityRedirectedForCloneProfile(auth);
        this.mCloneProfileAuthorityRedirectionCache.put(auth, java.lang.Boolean.valueOf(isAuthRedirected));
        return isAuthRedirected;
    }

    private boolean isSingletonOrSystemUserOnly(android.content.pm.ProviderInfo pi) {
        return (android.multiuser.Flags.enableSystemUserOnlyForServicesAndProviders() && this.mService.isSystemUserOnly(pi.flags)) || this.mService.isSingleton(pi.processName, pi.applicationInfo, pi.name, pi.flags);
    }
}
