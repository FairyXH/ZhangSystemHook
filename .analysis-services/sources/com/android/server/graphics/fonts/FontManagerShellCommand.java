package com.android.server.graphics.fonts;

/* JADX INFO: loaded from: classes2.dex */
public class FontManagerShellCommand extends android.os.ShellCommand {
    private static final int MAX_SIGNATURE_FILE_SIZE_BYTES = 8192;
    private static final java.lang.String TAG = "FontManagerShellCommand";
    private final com.android.server.graphics.fonts.FontManagerService mService;

    FontManagerShellCommand(com.android.server.graphics.fonts.FontManagerService service) {
        this.mService = service;
    }

    public int onCommand(java.lang.String cmd) {
        int callingUid = android.os.Binder.getCallingUid();
        if (callingUid != 0 && callingUid != 2000) {
            getErrPrintWriter().println("Only shell or root user can execute font command.");
            return 1;
        }
        return execCommand(this, cmd);
    }

    public void onHelp() {
        java.io.PrintWriter w = getOutPrintWriter();
        w.println("Font service (font) commands");
        w.println("help");
        w.println("    Print this help text.");
        w.println();
        w.println("dump [family name]");
        w.println("    Dump all font files in the specified family name.");
        w.println("    Dump current system font configuration if no family name was specified.");
        w.println();
        w.println("update [font file path] [signature file path]");
        w.println("    Update installed font files with new font file.");
        w.println();
        w.println("update-family [family definition XML path]");
        w.println("    Update font families with the new definitions.");
        w.println();
        w.println("install-debug-cert [cert file path]");
        w.println("    Install debug certificate file. This command can be used only on");
        w.println("    debuggable device with root user.");
        w.println();
        w.println("clear");
        w.println("    Remove all installed font files and reset to the initial state.");
        w.println();
        w.println(com.android.server.am.HostingRecord.HOSTING_TYPE_RESTART);
        w.println("    Restart FontManagerService emulating device reboot.");
        w.println("    WARNING: this is not a safe operation. Other processes may misbehave if");
        w.println("    they are using fonts updated by FontManagerService.");
        w.println("    This command exists merely for testing.");
        w.println();
        w.println("status");
        w.println("    Prints status of current system font configuration.");
    }

    void dumpAll(android.util.IndentingPrintWriter w) {
        android.text.FontConfig fontConfig = this.mService.getSystemFontConfig();
        dumpFontConfig(w, fontConfig);
    }

    private void dumpSingleFontConfig(android.util.IndentingPrintWriter w, android.text.FontConfig.Font font) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("style = ");
        sb.append(font.getStyle());
        sb.append(", path = ");
        sb.append(font.getFile().getAbsolutePath());
        if (font.getTtcIndex() != 0) {
            sb.append(", index = ");
            sb.append(font.getTtcIndex());
        }
        if (!font.getFontVariationSettings().isEmpty()) {
            sb.append(", axes = ");
            sb.append(font.getFontVariationSettings());
        }
        if (font.getFontFamilyName() != null) {
            sb.append(", fallback = ");
            sb.append(font.getFontFamilyName());
        }
        w.println(sb.toString());
        if (font.getOriginalFile() != null) {
            w.increaseIndent();
            w.println("Font is updated from " + font.getOriginalFile());
            w.decreaseIndent();
        }
    }

    private void dumpFontConfig(android.util.IndentingPrintWriter w, android.text.FontConfig fontConfig) {
        java.util.List<android.text.FontConfig.FontFamily> families = fontConfig.getFontFamilies();
        w.println("Named Family List");
        w.increaseIndent();
        java.util.List<android.text.FontConfig.NamedFamilyList> namedFamilyLists = fontConfig.getNamedFamilyLists();
        for (int i = 0; i < namedFamilyLists.size(); i++) {
            android.text.FontConfig.NamedFamilyList namedFamilyList = namedFamilyLists.get(i);
            w.println("Named Family (" + namedFamilyList.getName() + ")");
            w.increaseIndent();
            java.util.List<android.text.FontConfig.FontFamily> namedFamilies = namedFamilyList.getFamilies();
            for (int j = 0; j < namedFamilies.size(); j++) {
                android.text.FontConfig.FontFamily family = namedFamilies.get(j);
                w.println("Family");
                java.util.List<android.text.FontConfig.Font> fonts = family.getFontList();
                w.increaseIndent();
                for (int k = 0; k < fonts.size(); k++) {
                    dumpSingleFontConfig(w, fonts.get(k));
                }
                w.decreaseIndent();
            }
            w.decreaseIndent();
        }
        w.decreaseIndent();
        w.println("Dump Fallback Families");
        w.increaseIndent();
        int c = 0;
        for (int i2 = 0; i2 < families.size(); i2++) {
            android.text.FontConfig.FontFamily family2 = families.get(i2);
            if (family2.getName() == null) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder("Fallback Family [");
                int c2 = c + 1;
                sb.append(c);
                sb.append("]: lang=\"");
                sb.append(family2.getLocaleList().toLanguageTags());
                sb.append("\"");
                if (family2.getVariant() != 0) {
                    sb.append(", variant=");
                    switch (family2.getVariant()) {
                        case 1:
                            sb.append("Compact");
                            break;
                        case 2:
                            sb.append("Elegant");
                            break;
                        default:
                            sb.append("Unknown");
                            break;
                    }
                }
                w.println(sb.toString());
                java.util.List<android.text.FontConfig.Font> fonts2 = family2.getFontList();
                w.increaseIndent();
                for (int j2 = 0; j2 < fonts2.size(); j2++) {
                    dumpSingleFontConfig(w, fonts2.get(j2));
                }
                w.decreaseIndent();
                c = c2;
            }
        }
        w.decreaseIndent();
        w.println("Dump Family Aliases");
        w.increaseIndent();
        java.util.List<android.text.FontConfig.Alias> aliases = fontConfig.getAliases();
        for (int i3 = 0; i3 < aliases.size(); i3++) {
            android.text.FontConfig.Alias alias = aliases.get(i3);
            w.println("alias = " + alias.getName() + ", reference = " + alias.getOriginal() + ", width = " + alias.getWeight());
        }
        w.decreaseIndent();
    }

    private void dumpFallback(android.util.IndentingPrintWriter writer, android.graphics.fonts.FontFamily[] families) {
        for (android.graphics.fonts.FontFamily family : families) {
            dumpFamily(writer, family);
        }
    }

    private void dumpFamily(android.util.IndentingPrintWriter writer, android.graphics.fonts.FontFamily family) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Family:");
        if (family.getLangTags() != null) {
            sb.append(" langTag = ");
            sb.append(family.getLangTags());
        }
        if (family.getVariant() != 0) {
            sb.append(" variant = ");
            switch (family.getVariant()) {
                case 1:
                    sb.append("Compact");
                    break;
                case 2:
                    sb.append("Elegant");
                    break;
                default:
                    sb.append("UNKNOWN");
                    break;
            }
        }
        writer.println(sb.toString());
        for (int i = 0; i < family.getSize(); i++) {
            writer.increaseIndent();
            try {
                dumpFont(writer, family.getFont(i));
                writer.decreaseIndent();
            } catch (java.lang.Throwable th) {
                writer.decreaseIndent();
                throw th;
            }
        }
    }

    private void dumpFont(android.util.IndentingPrintWriter writer, android.graphics.fonts.Font font) {
        java.io.File file = font.getFile();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(font.getStyle());
        sb.append(", path = ");
        sb.append(file == null ? "[Not a file]" : file.getAbsolutePath());
        if (font.getTtcIndex() != 0) {
            sb.append(", index = ");
            sb.append(font.getTtcIndex());
        }
        android.graphics.fonts.FontVariationAxis[] axes = font.getAxes();
        if (axes != null && axes.length != 0) {
            sb.append(", axes = \"");
            sb.append(android.graphics.fonts.FontVariationAxis.toFontVariationSettings(axes));
            sb.append("\"");
        }
        writer.println(sb.toString());
    }

    private void writeCommandResult(android.os.ShellCommand shell, com.android.server.graphics.fonts.FontManagerService.SystemFontException e) {
        java.io.PrintWriter pw = shell.getErrPrintWriter();
        pw.println(e.getErrorCode());
        pw.println(e.getMessage());
        android.util.Slog.e(TAG, "Command failed: " + java.util.Arrays.toString(shell.getAllArgs()), e);
    }

    private int dump(android.os.ShellCommand shell) {
        android.content.Context ctx = this.mService.getContext();
        if (!com.android.internal.util.DumpUtils.checkDumpPermission(ctx, TAG, shell.getErrPrintWriter())) {
            return 1;
        }
        android.util.IndentingPrintWriter writer = new android.util.IndentingPrintWriter(shell.getOutPrintWriter(), "  ");
        java.lang.String nextArg = shell.getNextArg();
        android.text.FontConfig fontConfig = this.mService.getSystemFontConfig();
        if (nextArg == null) {
            dumpFontConfig(writer, fontConfig);
            return 0;
        }
        java.util.Map<java.lang.String, android.graphics.fonts.FontFamily[]> fallbackMap = android.graphics.fonts.SystemFonts.buildSystemFallback(fontConfig);
        android.graphics.fonts.FontFamily[] families = fallbackMap.get(nextArg);
        if (families == null) {
            writer.println("Font Family \"" + nextArg + "\" not found");
            return 0;
        }
        dumpFallback(writer, families);
        return 0;
    }

    private int installCert(android.os.ShellCommand shell) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        if (!android.os.Build.IS_DEBUGGABLE) {
            throw new java.lang.SecurityException("Only debuggable device can add debug certificate");
        }
        if (android.os.Binder.getCallingUid() != 0) {
            throw new java.lang.SecurityException("Only root can add debug certificate");
        }
        java.lang.String certPath = shell.getNextArg();
        if (certPath == null) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-10008, "Cert file path argument is required.");
        }
        java.io.File file = new java.io.File(certPath);
        if (!file.isFile()) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-10008, "Cert file (" + file + ") is not found");
        }
        this.mService.addDebugCertificate(certPath);
        this.mService.restart();
        shell.getOutPrintWriter().println("Success");
        return 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x009f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int update(android.os.ShellCommand r12) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        /*
            Method dump skipped, instruction units count: 202
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.graphics.fonts.FontManagerShellCommand.update(android.os.ShellCommand):int");
    }

    private int updateFamily(android.os.ShellCommand shell) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        java.lang.String xmlPath = shell.getNextArg();
        if (xmlPath == null) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-10003, "XML file path argument is required.");
        }
        try {
            android.os.ParcelFileDescriptor xmlFd = shell.openFileForSystem(xmlPath, com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD);
            try {
                java.util.List<android.graphics.fonts.FontUpdateRequest> requests = parseFontFamilyUpdateXml(new java.io.FileInputStream(xmlFd.getFileDescriptor()));
                if (xmlFd != null) {
                    xmlFd.close();
                }
                this.mService.update(-1, requests);
                shell.getOutPrintWriter().println("Success");
                return 0;
            } finally {
            }
        } catch (java.io.IOException e) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-10006, "Failed to open XML file.", e);
        }
    }

    private static java.util.List<android.graphics.fonts.FontUpdateRequest> parseFontFamilyUpdateXml(java.io.InputStream inputStream) throws com.android.server.graphics.fonts.FontManagerService.SystemFontException {
        try {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(inputStream);
            java.util.List<android.graphics.fonts.FontUpdateRequest> requests = new java.util.ArrayList<>();
            while (true) {
                int type = parser.next();
                if (type != 1) {
                    if (type == 2) {
                        int depth = parser.getDepth();
                        java.lang.String tag = parser.getName();
                        if (depth == 1) {
                            if (!"fontFamilyUpdateRequest".equals(tag)) {
                                throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-10007, "Expected <fontFamilyUpdateRequest> but got: " + tag);
                            }
                        } else if (depth != 2) {
                            continue;
                        } else if ("family".equals(tag)) {
                            requests.add(new android.graphics.fonts.FontUpdateRequest(android.graphics.fonts.FontUpdateRequest.Family.readFromXml(parser)));
                        } else {
                            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(-10007, "Expected <family> but got: " + tag);
                        }
                    }
                } else {
                    return requests;
                }
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            throw new com.android.server.graphics.fonts.FontManagerService.SystemFontException(0, "Failed to parse xml", e);
        }
    }

    private int clear(android.os.ShellCommand shell) {
        this.mService.clearUpdates();
        shell.getOutPrintWriter().println("Success");
        return 0;
    }

    private int restart(android.os.ShellCommand shell) {
        this.mService.restart();
        shell.getOutPrintWriter().println("Success");
        return 0;
    }

    private int status(android.os.ShellCommand shell) {
        android.util.IndentingPrintWriter writer = new android.util.IndentingPrintWriter(shell.getOutPrintWriter(), "  ");
        android.text.FontConfig config = this.mService.getSystemFontConfig();
        writer.println("Current Version: " + config.getConfigVersion());
        java.time.LocalDateTime dt = java.time.LocalDateTime.ofEpochSecond(config.getLastModifiedTimeMillis(), 0, java.time.ZoneOffset.UTC);
        writer.println("Last Modified Date: " + dt.format(java.time.format.DateTimeFormatter.ISO_DATE_TIME));
        java.util.Map<java.lang.String, java.io.File> fontFileMap = this.mService.getFontFileMap();
        writer.println("Number of updated font files: " + fontFileMap.size());
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0010  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int execCommand(android.os.ShellCommand r3, java.lang.String r4) {
        /*
            r2 = this;
            if (r4 != 0) goto L8
            r0 = 0
            int r0 = r3.handleDefaultCommands(r0)
            return r0
        L8:
            r0 = 1
            int r1 = r4.hashCode()     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            switch(r1) {
                case -2084349744: goto L51;
                case -892481550: goto L46;
                case -838846263: goto L3b;
                case 3095028: goto L31;
                case 94746189: goto L27;
                case 1097506319: goto L1c;
                case 1135462632: goto L11;
                default: goto L10;
            }     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
        L10:
            goto L5c
        L11:
            java.lang.String r1 = "update-family"
            boolean r1 = r4.equals(r1)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            if (r1 == 0) goto L10
            r1 = 2
            goto L5d
        L1c:
            java.lang.String r1 = "restart"
            boolean r1 = r4.equals(r1)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            if (r1 == 0) goto L10
            r1 = 4
            goto L5d
        L27:
            java.lang.String r1 = "clear"
            boolean r1 = r4.equals(r1)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            if (r1 == 0) goto L10
            r1 = 3
            goto L5d
        L31:
            java.lang.String r1 = "dump"
            boolean r1 = r4.equals(r1)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            if (r1 == 0) goto L10
            r1 = 0
            goto L5d
        L3b:
            java.lang.String r1 = "update"
            boolean r1 = r4.equals(r1)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            if (r1 == 0) goto L10
            r1 = r0
            goto L5d
        L46:
            java.lang.String r1 = "status"
            boolean r1 = r4.equals(r1)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            if (r1 == 0) goto L10
            r1 = 5
            goto L5d
        L51:
            java.lang.String r1 = "install-debug-cert"
            boolean r1 = r4.equals(r1)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            if (r1 == 0) goto L10
            r1 = 6
            goto L5d
        L5c:
            r1 = -1
        L5d:
            switch(r1) {
                case 0: goto L83;
                case 1: goto L7e;
                case 2: goto L79;
                case 3: goto L74;
                case 4: goto L6f;
                case 5: goto L6a;
                case 6: goto L65;
                default: goto L60;
            }     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
        L60:
            int r0 = r3.handleDefaultCommands(r4)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            goto L88
        L65:
            int r0 = r2.installCert(r3)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            return r0
        L6a:
            int r0 = r2.status(r3)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            return r0
        L6f:
            int r0 = r2.restart(r3)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            return r0
        L74:
            int r0 = r2.clear(r3)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            return r0
        L79:
            int r0 = r2.updateFamily(r3)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            return r0
        L7e:
            int r0 = r2.update(r3)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            return r0
        L83:
            int r0 = r2.dump(r3)     // Catch: com.android.server.graphics.fonts.FontManagerService.SystemFontException -> L89
            return r0
        L88:
            return r0
        L89:
            r1 = move-exception
            r2.writeCommandResult(r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.graphics.fonts.FontManagerShellCommand.execCommand(android.os.ShellCommand, java.lang.String):int");
    }
}
