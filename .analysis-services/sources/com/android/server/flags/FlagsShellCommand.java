package com.android.server.flags;

/* JADX INFO: loaded from: classes2.dex */
public class FlagsShellCommand {
    private final com.android.server.flags.FlagOverrideStore mFlagStore;

    FlagsShellCommand(com.android.server.flags.FlagOverrideStore flagStore) {
        this.mFlagStore = flagStore;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int process(java.lang.String[] r6, java.io.OutputStream r7, java.io.OutputStream r8) {
        /*
            r5 = this;
            com.android.internal.util.FastPrintWriter r0 = new com.android.internal.util.FastPrintWriter
            r0.<init>(r7)
            com.android.internal.util.FastPrintWriter r1 = new com.android.internal.util.FastPrintWriter
            r1.<init>(r8)
            int r2 = r6.length
            if (r2 != 0) goto L12
            int r2 = r5.printHelp(r0)
            return r2
        L12:
            r2 = 0
            r3 = r6[r2]
            java.util.Locale r4 = java.util.Locale.ROOT
            java.lang.String r3 = r3.toLowerCase(r4)
            int r4 = r3.hashCode()
            switch(r4) {
                case 102230: goto L4d;
                case 113762: goto L42;
                case 3198785: goto L38;
                case 3322014: goto L2d;
                case 96768678: goto L23;
                default: goto L22;
            }
        L22:
            goto L57
        L23:
            java.lang.String r2 = "erase"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L22
            r2 = 4
            goto L58
        L2d:
            java.lang.String r2 = "list"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L22
            r2 = 1
            goto L58
        L38:
            java.lang.String r4 = "help"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L22
            goto L58
        L42:
            java.lang.String r2 = "set"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L22
            r2 = 2
            goto L58
        L4d:
            java.lang.String r2 = "get"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L22
            r2 = 3
            goto L58
        L57:
            r2 = -1
        L58:
            switch(r2) {
                case 0: goto L74;
                case 1: goto L6f;
                case 2: goto L6a;
                case 3: goto L65;
                case 4: goto L60;
                default: goto L5b;
            }
        L5b:
            int r2 = r5.unknownCmd(r0)
            return r2
        L60:
            int r2 = r5.eraseCmd(r6, r0, r1)
            return r2
        L65:
            int r2 = r5.getCmd(r6, r0, r1)
            return r2
        L6a:
            int r2 = r5.setCmd(r6, r0, r1)
            return r2
        L6f:
            int r2 = r5.listCmd(r6, r0, r1)
            return r2
        L74:
            int r2 = r5.printHelp(r0)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.flags.FlagsShellCommand.process(java.lang.String[], java.io.OutputStream, java.io.OutputStream):int");
    }

    private int printHelp(java.io.PrintWriter outPw) {
        outPw.println("Feature Flags command, allowing listing, setting, getting, and erasing of");
        outPw.println("local flag overrides on a device.");
        outPw.println();
        outPw.println("Commands:");
        outPw.println("  list [namespace]");
        outPw.println("    List all flag overrides. Namespace is optional.");
        outPw.println();
        outPw.println("  get <namespace> <name>");
        outPw.println("    Return the string value of a specific flag, or <unset>");
        outPw.println();
        outPw.println("  set <namespace> <name> <value>");
        outPw.println("    Set a specific flag");
        outPw.println();
        outPw.println("  erase <namespace> <name>");
        outPw.println("    Unset a specific flag");
        outPw.flush();
        return 0;
    }

    private int listCmd(java.lang.String[] args, java.io.PrintWriter outPw, java.io.PrintWriter errPw) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> overrides;
        if (!validateNumArguments(args, 0, 1, args[0], errPw)) {
            errPw.println("Expected `" + args[0] + " [namespace]`");
            errPw.flush();
            return -1;
        }
        if (args.length == 2) {
            overrides = this.mFlagStore.getFlagsForNamespace(args[1]);
        } else {
            overrides = this.mFlagStore.getFlags();
        }
        if (overrides.isEmpty()) {
            outPw.println("No overrides set");
        } else {
            int longestNamespaceLen = "namespace".length();
            int longestFlagLen = "flag".length();
            int longestValLen = "value".length();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> namespace : overrides.entrySet()) {
                longestNamespaceLen = java.lang.Math.max(longestNamespaceLen, namespace.getKey().length());
                for (java.util.Map.Entry<java.lang.String, java.lang.String> flag : namespace.getValue().entrySet()) {
                    longestFlagLen = java.lang.Math.max(longestFlagLen, flag.getKey().length());
                    longestValLen = java.lang.Math.max(longestValLen, flag.getValue().length());
                }
            }
            outPw.print(java.lang.String.format("%-" + longestNamespaceLen + "s", "namespace"));
            outPw.print(' ');
            outPw.print(java.lang.String.format("%-" + longestFlagLen + "s", "flag"));
            outPw.print(' ');
            outPw.println("value");
            for (int i = 0; i < longestNamespaceLen; i++) {
                outPw.print('=');
            }
            outPw.print(' ');
            for (int i2 = 0; i2 < longestFlagLen; i2++) {
                outPw.print('=');
            }
            outPw.print(' ');
            for (int i3 = 0; i3 < longestValLen; i3++) {
                outPw.print('=');
            }
            outPw.println();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> namespace2 : overrides.entrySet()) {
                for (java.util.Map.Entry<java.lang.String, java.lang.String> flag2 : namespace2.getValue().entrySet()) {
                    outPw.print(java.lang.String.format("%-" + longestNamespaceLen + "s", namespace2.getKey()));
                    outPw.print(' ');
                    outPw.print(java.lang.String.format("%-" + longestFlagLen + "s", flag2.getKey()));
                    outPw.print(' ');
                    outPw.println(flag2.getValue());
                }
            }
        }
        outPw.flush();
        return 0;
    }

    private int setCmd(java.lang.String[] args, java.io.PrintWriter outPw, java.io.PrintWriter errPw) {
        if (!validateNumArguments(args, 3, args[0], errPw)) {
            errPw.println("Expected `" + args[0] + " <namespace> <name> <value>`");
            errPw.flush();
            return -1;
        }
        this.mFlagStore.set(args[1], args[2], args[3]);
        outPw.println("Flag " + args[1] + "." + args[2] + " is now " + args[3]);
        outPw.flush();
        return 0;
    }

    private int getCmd(java.lang.String[] args, java.io.PrintWriter outPw, java.io.PrintWriter errPw) {
        if (!validateNumArguments(args, 2, args[0], errPw)) {
            errPw.println("Expected `" + args[0] + " <namespace> <name>`");
            errPw.flush();
            return -1;
        }
        java.lang.String value = this.mFlagStore.get(args[1], args[2]);
        outPw.print(args[1] + "." + args[2] + " is ");
        if (value == null || value.isEmpty()) {
            outPw.println("<unset>");
        } else {
            outPw.println("\"" + value.translateEscapes() + "\"");
        }
        outPw.flush();
        return 0;
    }

    private int eraseCmd(java.lang.String[] args, java.io.PrintWriter outPw, java.io.PrintWriter errPw) {
        if (!validateNumArguments(args, 2, args[0], errPw)) {
            errPw.println("Expected `" + args[0] + " <namespace> <name>`");
            errPw.flush();
            return -1;
        }
        this.mFlagStore.erase(args[1], args[2]);
        outPw.println("Erased " + args[1] + "." + args[2]);
        return 0;
    }

    private int unknownCmd(java.io.PrintWriter outPw) {
        outPw.println("This command is unknown.");
        printHelp(outPw);
        outPw.flush();
        return -1;
    }

    private boolean validateNumArguments(java.lang.String[] args, int exactly, java.lang.String cmdName, java.io.PrintWriter errPw) {
        return validateNumArguments(args, exactly, exactly, cmdName, errPw);
    }

    private boolean validateNumArguments(java.lang.String[] args, int min, int max, java.lang.String cmdName, java.io.PrintWriter errPw) {
        int len = args.length - 1;
        if (len < min) {
            errPw.println("Less than " + min + " arguments provided for \"" + cmdName + "\" command.");
            return false;
        }
        if (len <= max) {
            return true;
        }
        errPw.println("More than " + max + " arguments provided for \"" + cmdName + "\" command.");
        return false;
    }
}
