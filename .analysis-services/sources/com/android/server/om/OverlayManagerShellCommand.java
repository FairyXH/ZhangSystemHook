package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
final class OverlayManagerShellCommand extends android.os.ShellCommand {
    private static final java.util.Map<java.lang.String, java.lang.Integer> TYPE_MAP = java.util.Map.of("color", 28, "string", 3, "drawable", -1);
    private final android.content.Context mContext;
    private final android.content.om.IOverlayManager mInterface;

    OverlayManagerShellCommand(android.content.Context ctx, android.content.om.IOverlayManager iom) {
        this.mContext = ctx;
        this.mInterface = iom;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            Method dump skipped, instruction units count: 264
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.om.OverlayManagerShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter out = getOutPrintWriter();
        out.println("Overlay manager (overlay) commands:");
        out.println("  help");
        out.println("    Print this help text.");
        out.println("  dump [--verbose] [--user USER_ID] [[FIELD] PACKAGE[:NAME]]");
        out.println("    Print debugging information about the overlay manager.");
        out.println("    With optional parameters PACKAGE and NAME, limit output to the specified");
        out.println("    overlay or target. With optional parameter FIELD, limit output to");
        out.println("    the corresponding SettingsItem field. Field names are all lower case");
        out.println("    and omit the m prefix, i.e. 'userid' for SettingsItem.mUserId.");
        out.println("  list [--user USER_ID] [PACKAGE[:NAME]]");
        out.println("    Print information about target and overlay packages.");
        out.println("    Overlay packages are printed in priority order. With optional");
        out.println("    parameters PACKAGE and NAME, limit output to the specified overlay or");
        out.println("    target.");
        out.println("  enable [--user USER_ID] PACKAGE[:NAME]");
        out.println("    Enable overlay within or owned by PACKAGE with optional unique NAME.");
        out.println("  disable [--user USER_ID] PACKAGE[:NAME]");
        out.println("    Disable overlay within or owned by PACKAGE with optional unique NAME.");
        out.println("  enable-exclusive [--user USER_ID] [--category] PACKAGE");
        out.println("    Enable overlay within or owned by PACKAGE and disable all other overlays");
        out.println("    for its target package. If the --category option is given, only disables");
        out.println("    other overlays in the same category.");
        out.println("  set-priority [--user USER_ID] PACKAGE PARENT|lowest|highest");
        out.println("    Change the priority of the overlay to be just higher than");
        out.println("    the priority of PARENT If PARENT is the special keyword");
        out.println("    'lowest', change priority of PACKAGE to the lowest priority.");
        out.println("    If PARENT is the special keyword 'highest', change priority of");
        out.println("    PACKAGE to the highest priority.");
        out.println("  lookup [--user USER_ID] [--verbose] PACKAGE-TO-LOAD PACKAGE:TYPE/NAME");
        out.println("    Load a package and print the value of a given resource");
        out.println("    applying the current configuration and enabled overlays.");
        out.println("    For a more fine-grained alternative, use 'idmap2 lookup'.");
        out.println("  fabricate [--user USER_ID] [--target-name OVERLAYABLE] --target PACKAGE");
        out.println("            --name NAME [--file FILE] ");
        out.println("            PACKAGE:TYPE/NAME ENCODED-TYPE-ID|TYPE-NAME ENCODED-VALUE");
        out.println("    Create an overlay from a single resource. Caller must be root. Example:");
        out.println("      fabricate --target android --name LighterGray \\");
        out.println("                android:color/lighter_gray 0x1c 0xffeeeeee");
        out.println("  partition-order");
        out.println("    Print the partition order from overlay config and how this order");
        out.println("    got established, by default or by /product/overlay/partition_order.xml");
    }

    private int runList() throws android.os.RemoteException {
        java.io.PrintWriter out = getOutPrintWriter();
        java.io.PrintWriter err = getErrPrintWriter();
        int userId = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1333469547:
                        if (!opt.equals("--user")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        err.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                if (packageName != null) {
                    java.util.List<android.content.om.OverlayInfo> overlaysForTarget = this.mInterface.getOverlayInfosForTarget(packageName, userId);
                    if (overlaysForTarget.isEmpty()) {
                        android.content.om.OverlayInfo info = this.mInterface.getOverlayInfo(packageName, userId);
                        if (info != null) {
                            printListOverlay(out, info);
                        }
                        return 0;
                    }
                    out.println(packageName);
                    int n = overlaysForTarget.size();
                    for (int i = 0; i < n; i++) {
                        printListOverlay(out, overlaysForTarget.get(i));
                    }
                    return 0;
                }
                java.util.Map<java.lang.String, java.util.List<android.content.om.OverlayInfo>> allOverlays = this.mInterface.getAllOverlays(userId);
                for (java.lang.String targetPackageName : allOverlays.keySet()) {
                    out.println(targetPackageName);
                    java.util.List<android.content.om.OverlayInfo> overlaysForTarget2 = allOverlays.get(targetPackageName);
                    int n2 = overlaysForTarget2.size();
                    for (int i2 = 0; i2 < n2; i2++) {
                        printListOverlay(out, overlaysForTarget2.get(i2));
                    }
                    out.println();
                }
                return 0;
            }
        }
    }

    private void printListOverlay(java.io.PrintWriter out, android.content.om.OverlayInfo oi) {
        java.lang.String status;
        switch (oi.state) {
            case 2:
                status = "[ ]";
                break;
            case 3:
            case 6:
                status = "[x]";
                break;
            case 4:
            case 5:
            default:
                status = "---";
                break;
        }
        out.println(java.lang.String.format("%s %s", status, oi.getOverlayIdentifier()));
    }

    private int runEnableDisable(boolean enable) throws android.os.RemoteException {
        java.io.PrintWriter err = getErrPrintWriter();
        int userId = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1333469547:
                        if (!opt.equals("--user")) {
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        err.println("Error: Unknown option: " + opt);
                        return 1;
                }
            } else {
                android.content.om.OverlayIdentifier overlay = android.content.om.OverlayIdentifier.fromString(getNextArgRequired());
                this.mInterface.commit(new android.content.om.OverlayManagerTransaction.Builder().setEnabled(overlay, enable, userId).build());
                return 0;
            }
        }
    }

    private int runPartitionOrder() throws android.os.RemoteException {
        java.io.PrintWriter out = getOutPrintWriter();
        out.println("Partition order (low to high priority): " + this.mInterface.getPartitionOrder());
        out.println("Established by " + (this.mInterface.isDefaultPartitionOrder() ? "default" : "/product/overlay/partition_order.xml"));
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0073  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runFabricate() throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 350
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.om.OverlayManagerShellCommand.runFabricate():int");
    }

    private int addOverlayValuesFromXml(android.content.om.FabricatedOverlay overlay, java.lang.String targetPackage, java.lang.String filename) {
        int type;
        java.io.PrintWriter err = getErrPrintWriter();
        java.io.File file = new java.io.File(filename);
        if (!file.exists()) {
            err.println("Error: File does not exist");
            return 1;
        }
        if (!file.canRead()) {
            err.println("Error: File is unreadable");
            return 1;
        }
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(file);
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(fis);
                do {
                    type = parser.next();
                    if (type == 2) {
                        break;
                    }
                } while (type != 1);
                parser.require(2, (java.lang.String) null, "overlay");
                while (true) {
                    int type2 = parser.next();
                    if (type2 == 1) {
                        fis.close();
                        return 0;
                    }
                    if (type2 == 2) {
                        java.lang.String tagName = parser.getName();
                        if (!tagName.equals(com.android.server.pm.Settings.TAG_ITEM)) {
                            err.println(android.text.TextUtils.formatSimple("Error: Unexpected tag: %s at line %d", new java.lang.Object[]{tagName, java.lang.Integer.valueOf(parser.getLineNumber())}));
                        } else {
                            if (!parser.isEmptyElementTag()) {
                                err.println("Error: item tag must be empty");
                                fis.close();
                                return 1;
                            }
                            java.lang.String target = parser.getAttributeValue((java.lang.String) null, "target");
                            if (android.text.TextUtils.isEmpty(target)) {
                                err.println("Error: target name missing at line " + parser.getLineNumber());
                                fis.close();
                                return 1;
                            }
                            int index = target.indexOf(47);
                            if (index < 0) {
                                err.println("Error: target malformed, missing '/' at line " + parser.getLineNumber());
                                fis.close();
                                return 1;
                            }
                            java.lang.String overlayType = target.substring(0, index);
                            java.lang.String value = parser.getAttributeValue((java.lang.String) null, "value");
                            if (android.text.TextUtils.isEmpty(value)) {
                                err.println("Error: value missing at line " + parser.getLineNumber());
                                fis.close();
                                return 1;
                            }
                            java.lang.String config = parser.getAttributeValue((java.lang.String) null, "config");
                            if (addOverlayValue(overlay, targetPackage + ':' + target, overlayType, value, config) != 0) {
                                fis.close();
                                return 1;
                            }
                        }
                    }
                }
            } finally {
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return 1;
        } catch (org.xmlpull.v1.XmlPullParserException e2) {
            e2.printStackTrace();
            return 1;
        }
    }

    private int addOverlayValue(android.content.om.FabricatedOverlay overlay, java.lang.String resourceName, java.lang.String typeString, java.lang.String valueString, java.lang.String configuration) {
        int type;
        int intData;
        java.lang.String typeString2 = typeString.toLowerCase(java.util.Locale.getDefault());
        if (TYPE_MAP.containsKey(typeString2)) {
            type = TYPE_MAP.get(typeString2).intValue();
        } else if (typeString2.startsWith("0x")) {
            type = java.lang.Integer.parseUnsignedInt(typeString2.substring(2), 16);
        } else {
            type = java.lang.Integer.parseUnsignedInt(typeString2);
        }
        if (type == 3) {
            overlay.setResourceValue(resourceName, type, valueString, configuration);
            return 0;
        }
        if (type < 0) {
            android.os.ParcelFileDescriptor pfd = openFileForSystem(valueString, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
            if (valueString.endsWith(".9.png")) {
                overlay.setNinePatchResourceValue(resourceName, pfd, configuration);
                return 0;
            }
            overlay.setResourceValue(resourceName, pfd, configuration);
            return 0;
        }
        if (valueString.startsWith("0x")) {
            intData = java.lang.Integer.parseUnsignedInt(valueString.substring(2), 16);
        } else {
            intData = java.lang.Integer.parseUnsignedInt(valueString);
        }
        overlay.setResourceValue(resourceName, type, intData, configuration);
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runEnableExclusive() throws android.os.RemoteException {
        /*
            r7 = this;
            java.io.PrintWriter r0 = r7.getErrPrintWriter()
            r1 = 0
            r2 = 0
        L6:
            java.lang.String r3 = r7.getNextOption()
            r4 = r3
            r5 = 1
            if (r3 == 0) goto L51
            int r3 = r4.hashCode()
            switch(r3) {
                case 66265758: goto L20;
                case 1333469547: goto L16;
                default: goto L15;
            }
        L15:
            goto L2a
        L16:
            java.lang.String r3 = "--user"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L15
            r3 = 0
            goto L2b
        L20:
            java.lang.String r3 = "--category"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L15
            r3 = r5
            goto L2b
        L2a:
            r3 = -1
        L2b:
            switch(r3) {
                case 0: goto L47;
                case 1: goto L45;
                default: goto L2e;
            }
        L2e:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r6 = "Error: Unknown option: "
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r0.println(r3)
            return r5
        L45:
            r2 = 1
            goto L50
        L47:
            java.lang.String r3 = r7.getNextArgRequired()
            int r1 = android.os.UserHandle.parseUserArg(r3)
        L50:
            goto L6
        L51:
            java.lang.String r3 = r7.getNextArgRequired()
            if (r2 == 0) goto L5f
            android.content.om.IOverlayManager r6 = r7.mInterface
            boolean r6 = r6.setEnabledExclusiveInCategory(r3, r1)
            r5 = r5 ^ r6
            return r5
        L5f:
            android.content.om.IOverlayManager r6 = r7.mInterface
            boolean r6 = r6.setEnabledExclusive(r3, r5, r1)
            r5 = r5 ^ r6
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.om.OverlayManagerShellCommand.runEnableExclusive():int");
    }

    private int runSetPriority() throws android.os.RemoteException {
        byte b;
        java.io.PrintWriter errPrintWriter = getErrPrintWriter();
        int userArg = 0;
        while (true) {
            java.lang.String nextOption = getNextOption();
            if (nextOption != null) {
                switch (nextOption.hashCode()) {
                    case 1333469547:
                        if (nextOption.equals("--user")) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        userArg = android.os.UserHandle.parseUserArg(getNextArgRequired());
                        break;
                    default:
                        errPrintWriter.println("Error: Unknown option: " + nextOption);
                        return 1;
                }
            } else {
                java.lang.String nextArgRequired = getNextArgRequired();
                java.lang.String nextArgRequired2 = getNextArgRequired();
                return "highest".equals(nextArgRequired2) ? 1 ^ (this.mInterface.setHighestPriority(nextArgRequired, userArg) ? 1 : 0) : "lowest".equals(nextArgRequired2) ? 1 ^ (this.mInterface.setLowestPriority(nextArgRequired, userArg) ? 1 : 0) : 1 ^ (this.mInterface.setPriority(nextArgRequired, nextArgRequired2, userArg) ? 1 : 0);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0118 A[Catch: all -> 0x016c, NotFoundException -> 0x0170, TRY_LEAVE, TryCatch #11 {NotFoundException -> 0x0170, all -> 0x016c, blocks: (B:58:0x0104, B:60:0x0118, B:70:0x0133), top: B:102:0x0104 }] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x015e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runLookup() throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 456
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.om.OverlayManagerShellCommand.runLookup():int");
    }
}
