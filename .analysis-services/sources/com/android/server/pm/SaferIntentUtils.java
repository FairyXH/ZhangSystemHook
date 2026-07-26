package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class SaferIntentUtils {
    public static final java.lang.ThreadLocal<java.lang.Boolean> DISABLE_ENFORCE_INTENTS_TO_MATCH_INTENT_FILTERS = java.lang.ThreadLocal.withInitial(new java.util.function.Supplier() { // from class: com.android.server.pm.SaferIntentUtils$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return com.android.server.pm.SaferIntentUtils.lambda$static$0();
        }
    });
    private static final long ENFORCE_INTENTS_TO_MATCH_INTENT_FILTERS = 161252188;
    private static final long IMPLICIT_INTENTS_ONLY_MATCH_EXPORTED_COMPONENTS = 229362273;

    static /* synthetic */ java.lang.Boolean lambda$static$0() {
        return false;
    }

    private static com.android.internal.pm.pkg.component.ParsedMainComponent infoToComponent(android.content.pm.ComponentInfo info, com.android.server.pm.resolution.ComponentResolverApi resolver, boolean isReceiver) {
        if (info instanceof android.content.pm.ActivityInfo) {
            if (isReceiver) {
                return resolver.getReceiver(info.getComponentName());
            }
            return resolver.getActivity(info.getComponentName());
        }
        if (info instanceof android.content.pm.ServiceInfo) {
            return resolver.getService(info.getComponentName());
        }
        throw new java.lang.IllegalArgumentException("Unsupported component type");
    }

    public static void reportUnsafeIntentEvent(int event, int callingUid, int callingPid, android.content.Intent intent, java.lang.String resolvedType, boolean blocked) {
        java.lang.String[] categories = intent.getCategories() == null ? new java.lang.String[0] : (java.lang.String[]) intent.getCategories().toArray(new java.util.function.IntFunction() { // from class: com.android.server.pm.SaferIntentUtils$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.pm.SaferIntentUtils.lambda$reportUnsafeIntentEvent$1(i);
            }
        });
        java.lang.String component = intent.getComponent() == null ? null : intent.getComponent().flattenToString();
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.UNSAFE_INTENT_EVENT_REPORTED, event, callingUid, component, intent.getPackage(), intent.getAction(), categories, resolvedType, intent.getScheme(), blocked);
        ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).triggerUnsafeIntentStrictMode(callingPid, event, intent);
    }

    static /* synthetic */ java.lang.String[] lambda$reportUnsafeIntentEvent$1(int x$0) {
        return new java.lang.String[x$0];
    }

    public static class IntentArgs {
        public int callingPid;
        public int callingUid;
        public android.content.Intent intent;
        public boolean isReceiver;
        public com.android.server.compat.PlatformCompat platformCompat;
        public boolean resolveForStart;
        public java.lang.String resolvedType;
        public com.android.server.pm.snapshot.PackageDataSnapshot snapshot;

        public IntentArgs(android.content.Intent intent, java.lang.String resolvedType, boolean isReceiver, boolean resolveForStart, int callingUid, int callingPid) {
            this.isReceiver = isReceiver;
            this.intent = intent;
            this.resolvedType = resolvedType;
            this.resolveForStart = resolveForStart;
            this.callingUid = callingUid;
            this.callingPid = resolveForStart ? callingPid : -1;
        }

        boolean isChangeEnabled(long changeId) {
            return this.platformCompat == null || this.platformCompat.isChangeEnabledByUidInternalNoLogging(changeId, this.callingUid);
        }

        void reportEvent(int event, boolean blocked) {
            if (this.resolveForStart) {
                com.android.server.pm.SaferIntentUtils.reportUnsafeIntentEvent(event, this.callingUid, this.callingPid, this.intent, this.resolvedType, blocked);
            }
        }
    }

    public static void blockNullAction(com.android.server.pm.SaferIntentUtils.IntentArgs args, java.util.List componentList) {
        if (android.app.ActivityManager.canAccessUnexportedComponents(args.callingUid)) {
            return;
        }
        com.android.server.pm.Computer computer = (com.android.server.pm.Computer) args.snapshot;
        com.android.server.pm.resolution.ComponentResolverApi resolver = null;
        boolean enforce = android.security.Flags.blockNullActionIntents() && args.isChangeEnabled(293560872L);
        for (int i = componentList.size() - 1; i >= 0; i--) {
            boolean match = true;
            java.lang.Object c = componentList.get(i);
            if (c instanceof android.content.pm.ResolveInfo) {
                android.content.pm.ResolveInfo resolveInfo = (android.content.pm.ResolveInfo) c;
                if (computer == null) {
                    return;
                }
                if (resolver == null) {
                    resolver = computer.getComponentResolver();
                }
                com.android.internal.pm.pkg.component.ParsedMainComponent comp = infoToComponent(resolveInfo.getComponentInfo(), resolver, args.isReceiver);
                if (comp != null && !comp.getIntents().isEmpty() && args.intent.getAction() == null) {
                    match = false;
                }
            } else if ((c instanceof android.content.IntentFilter) && args.intent.getAction() == null) {
                match = false;
            }
            if (!match) {
                args.reportEvent(1, enforce);
                if (enforce) {
                    android.util.Slog.w("PackageManager", "Blocking intent with null action: " + args.intent);
                    componentList.remove(i);
                }
            }
        }
    }

    public static void enforceIntentFilterMatching(com.android.server.pm.SaferIntentUtils.IntentArgs args, java.util.List<android.content.pm.ResolveInfo> resolveInfos) {
        final android.util.Printer logPrinter;
        com.android.server.pm.Computer computer;
        char c;
        boolean z;
        if (DISABLE_ENFORCE_INTENTS_TO_MATCH_INTENT_FILTERS.get().booleanValue() || android.app.ActivityManager.canAccessUnexportedComponents(args.callingUid)) {
            return;
        }
        com.android.server.pm.Computer computer2 = (com.android.server.pm.Computer) args.snapshot;
        com.android.server.pm.resolution.ComponentResolverApi resolver = computer2.getComponentResolver();
        char c2 = 3;
        if (com.android.server.pm.PackageManagerService.DEBUG_INTENT_MATCHING) {
            logPrinter = new android.util.LogPrinter(2, "PackageManager", 3);
        } else {
            logPrinter = null;
        }
        boolean z2 = false;
        int i = 1;
        boolean enforceMatch = android.security.Flags.enforceIntentFilterMatch() && args.isChangeEnabled(ENFORCE_INTENTS_TO_MATCH_INTENT_FILTERS);
        boolean blockNullAction = android.security.Flags.blockNullActionIntents() && args.isChangeEnabled(293560872L);
        int i2 = resolveInfos.size() - 1;
        while (i2 >= 0) {
            android.content.pm.ComponentInfo info = resolveInfos.get(i2).getComponentInfo();
            if (android.os.UserHandle.isSameApp(args.callingUid, info.applicationInfo.uid)) {
                computer = computer2;
                c = c2;
                z = z2;
            } else {
                com.android.internal.pm.pkg.component.ParsedMainComponent comp = infoToComponent(info, resolver, args.isReceiver);
                if (comp == null) {
                    computer = computer2;
                    c = c2;
                    z = z2;
                } else if (comp.getIntents().isEmpty()) {
                    computer = computer2;
                    c = c2;
                    z = z2;
                } else {
                    java.lang.Boolean match = null;
                    if (args.intent.getAction() == null) {
                        args.reportEvent(i, (enforceMatch && blockNullAction) ? i : z2);
                        if (blockNullAction) {
                            match = java.lang.Boolean.valueOf(z2);
                        }
                    }
                    if (match != null) {
                        computer = computer2;
                    } else {
                        int j = 0;
                        int size = comp.getIntents().size();
                        while (true) {
                            if (j >= size) {
                                computer = computer2;
                                break;
                            }
                            android.content.IntentFilter intentFilter = ((com.android.internal.pm.pkg.component.ParsedIntentInfo) comp.getIntents().get(j)).getIntentFilter();
                            computer = computer2;
                            if (!com.android.server.IntentResolver.intentMatchesFilter(intentFilter, args.intent, args.resolvedType)) {
                                j++;
                                computer2 = computer;
                            } else {
                                match = true;
                                break;
                            }
                        }
                    }
                    if (match != null) {
                        c = 3;
                        z = false;
                    } else {
                        c = 3;
                        args.reportEvent(3, enforceMatch);
                        z = false;
                        match = false;
                    }
                    if (match.booleanValue()) {
                        i = 1;
                    } else {
                        if (!android.security.Flags.enforceIntentFilterMatch()) {
                            i = 1;
                        } else {
                            i = 1;
                            args.intent.addExtendedFlags(1);
                        }
                        if (enforceMatch) {
                            android.util.Slog.w("PackageManager", "Intent does not match component's intent filter: " + args.intent);
                            android.util.Slog.w("PackageManager", "Access blocked: " + comp.getComponentName());
                            if (com.android.server.pm.PackageManagerService.DEBUG_INTENT_MATCHING) {
                                android.util.Slog.v("PackageManager", "Component intent filters:");
                                comp.getIntents().forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.SaferIntentUtils$$ExternalSyntheticLambda0
                                    @Override // java.util.function.Consumer
                                    public final void accept(java.lang.Object obj) {
                                        ((com.android.internal.pm.pkg.component.ParsedIntentInfo) obj).getIntentFilter().dump(logPrinter, "  ");
                                    }
                                });
                                android.util.Slog.v("PackageManager", "-----------------------------");
                            }
                            resolveInfos.remove(i2);
                        }
                    }
                }
            }
            i2--;
            z2 = z;
            c2 = c;
            computer2 = computer;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x004c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void filterNonExportedComponents(com.android.server.pm.SaferIntentUtils.IntentArgs r6, java.util.List r7) {
        /*
            if (r7 == 0) goto L5d
            android.content.Intent r0 = r6.intent
            java.lang.String r0 = r0.getPackage()
            if (r0 != 0) goto L5d
            android.content.Intent r0 = r6.intent
            android.content.ComponentName r0 = r0.getComponent()
            if (r0 != 0) goto L5d
            int r0 = r6.callingUid
            boolean r0 = android.app.ActivityManager.canAccessUnexportedComponents(r0)
            if (r0 == 0) goto L1b
            goto L5d
        L1b:
            r0 = 229362273(0xdabca61, double:1.133200195E-315)
            boolean r0 = r6.isChangeEnabled(r0)
            r1 = 0
            int r2 = r7.size()
            int r2 = r2 + (-1)
        L2a:
            if (r2 < 0) goto L56
            java.lang.Object r3 = r7.get(r2)
            boolean r4 = r3 instanceof android.content.pm.ResolveInfo
            if (r4 == 0) goto L40
            r4 = r3
            android.content.pm.ResolveInfo r4 = (android.content.pm.ResolveInfo) r4
            android.content.pm.ComponentInfo r5 = r4.getComponentInfo()
            boolean r5 = r5.exported
            if (r5 == 0) goto L4c
            goto L53
        L40:
            boolean r4 = r3 instanceof com.android.server.am.BroadcastFilter
            if (r4 == 0) goto L53
            r4 = r3
            com.android.server.am.BroadcastFilter r4 = (com.android.server.am.BroadcastFilter) r4
            boolean r5 = r4.exported
            if (r5 == 0) goto L4c
            goto L53
        L4c:
            r1 = 1
            if (r0 != 0) goto L50
            goto L56
        L50:
            r7.remove(r2)
        L53:
            int r2 = r2 + (-1)
            goto L2a
        L56:
            if (r1 == 0) goto L5c
            r2 = 2
            r6.reportEvent(r2, r0)
        L5c:
            return
        L5d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.SaferIntentUtils.filterNonExportedComponents(com.android.server.pm.SaferIntentUtils$IntentArgs, java.util.List):void");
    }
}
