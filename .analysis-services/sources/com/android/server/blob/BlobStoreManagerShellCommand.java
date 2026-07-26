package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
class BlobStoreManagerShellCommand extends android.os.ShellCommand {
    private final com.android.server.blob.BlobStoreManagerService mService;

    BlobStoreManagerShellCommand(com.android.server.blob.BlobStoreManagerService blobStoreManagerService) {
        this.mService = blobStoreManagerService;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r3) {
        /*
            r2 = this;
            if (r3 != 0) goto L8
            r0 = 0
            int r0 = r2.handleDefaultCommands(r0)
            return r0
        L8:
            java.io.PrintWriter r0 = r2.getOutPrintWriter()
            int r1 = r3.hashCode()
            switch(r1) {
                case -1168531841: goto L3e;
                case -971115831: goto L34;
                case -258166326: goto L2a;
                case 712607671: goto L1f;
                case 1861559962: goto L14;
                default: goto L13;
            }
        L13:
            goto L48
        L14:
            java.lang.String r1 = "idle-maintenance"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L13
            r1 = 3
            goto L49
        L1f:
            java.lang.String r1 = "query-blob-existence"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L13
            r1 = 4
            goto L49
        L2a:
            java.lang.String r1 = "clear-all-blobs"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L13
            r1 = 1
            goto L49
        L34:
            java.lang.String r1 = "clear-all-sessions"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L13
            r1 = 0
            goto L49
        L3e:
            java.lang.String r1 = "delete-blob"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L13
            r1 = 2
            goto L49
        L48:
            r1 = -1
        L49:
            switch(r1) {
                case 0: goto L65;
                case 1: goto L60;
                case 2: goto L5b;
                case 3: goto L56;
                case 4: goto L51;
                default: goto L4c;
            }
        L4c:
            int r1 = r2.handleDefaultCommands(r3)
            return r1
        L51:
            int r1 = r2.runQueryBlobExistence(r0)
            return r1
        L56:
            int r1 = r2.runIdleMaintenance(r0)
            return r1
        L5b:
            int r1 = r2.runDeleteBlob(r0)
            return r1
        L60:
            int r1 = r2.runClearAllBlobs(r0)
            return r1
        L65:
            int r1 = r2.runClearAllSessions(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.blob.BlobStoreManagerShellCommand.onCommand(java.lang.String):int");
    }

    private int runClearAllSessions(java.io.PrintWriter pw) {
        com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs args = new com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs();
        args.userId = -1;
        if (parseOptions(pw, args) < 0) {
            return -1;
        }
        this.mService.runClearAllSessions(args.userId);
        return 0;
    }

    private int runClearAllBlobs(java.io.PrintWriter pw) {
        com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs args = new com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs();
        args.userId = -1;
        if (parseOptions(pw, args) < 0) {
            return -1;
        }
        this.mService.runClearAllBlobs(args.userId);
        return 0;
    }

    private int runDeleteBlob(java.io.PrintWriter pw) {
        com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs args = new com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs();
        if (parseOptions(pw, args) < 0) {
            return -1;
        }
        this.mService.deleteBlob(args.getBlobHandle(), args.userId);
        return 0;
    }

    private int runIdleMaintenance(java.io.PrintWriter pw) {
        this.mService.runIdleMaintenance();
        return 0;
    }

    private int runQueryBlobExistence(java.io.PrintWriter printWriter) {
        com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs parsedArgs = new com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs();
        if (parseOptions(printWriter, parsedArgs) < 0) {
            return -1;
        }
        printWriter.println(this.mService.isBlobAvailable(parsedArgs.blobId, parsedArgs.userId) ? 1 : 0);
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("BlobStore service (blob_store) commands:");
        pw.println("help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("clear-all-sessions [-u | --user USER_ID]");
        pw.println("    Remove all sessions.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's sessions to be removed.");
        pw.println("                    If not specified, sessions in all users are removed.");
        pw.println();
        pw.println("clear-all-blobs [-u | --user USER_ID]");
        pw.println("    Remove all blobs.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's blobs to be removed.");
        pw.println("                    If not specified, blobs in all users are removed.");
        pw.println("delete-blob [-u | --user USER_ID] [--digest DIGEST] [--expiry EXPIRY_TIME] [--label LABEL] [--tag TAG]");
        pw.println("    Delete a blob.");
        pw.println("    Options:");
        pw.println("      -u or --user: specify which user's blobs to be removed;");
        pw.println("                    If not specified, blobs in all users are removed.");
        pw.println("      --digest: Base64 encoded digest of the blob to delete.");
        pw.println("      --expiry: Expiry time of the blob to delete, in milliseconds.");
        pw.println("      --label: Label of the blob to delete.");
        pw.println("      --tag: Tag of the blob to delete.");
        pw.println("idle-maintenance");
        pw.println("    Run idle maintenance which takes care of removing stale data.");
        pw.println("query-blob-existence [-b BLOB_ID] [-u | --user USER_ID]");
        pw.println("    Prints 1 if blob exists, otherwise 0.");
        pw.println();
    }

    private int parseOptions(java.io.PrintWriter pw, com.android.server.blob.BlobStoreManagerShellCommand.ParsedArgs args) {
        while (true) {
            java.lang.String opt = getNextOption();
            byte b = 0;
            if (opt != null) {
                switch (opt.hashCode()) {
                    case -1620968108:
                        b = !opt.equals("--label") ? (byte) -1 : (byte) 4;
                        break;
                    case 1493:
                        b = !opt.equals("-b") ? (byte) -1 : (byte) 7;
                        break;
                    case 1512:
                        if (!opt.equals("-u")) {
                            b = -1;
                        }
                        break;
                    case 43013626:
                        b = !opt.equals("--tag") ? (byte) -1 : (byte) 6;
                        break;
                    case 1068100452:
                        b = !opt.equals("--digest") ? (byte) -1 : (byte) 3;
                        break;
                    case 1110854355:
                        b = !opt.equals("--expiry") ? (byte) -1 : (byte) 5;
                        break;
                    case 1332867059:
                        b = !opt.equals("--algo") ? (byte) -1 : (byte) 2;
                        break;
                    case 1333469547:
                        b = !opt.equals("--user") ? (byte) -1 : (byte) 1;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                    case 1:
                        args.userId = java.lang.Integer.parseInt(getNextArgRequired());
                        break;
                    case 2:
                        args.algorithm = getNextArgRequired();
                        break;
                    case 3:
                        args.digest = java.util.Base64.getDecoder().decode(getNextArgRequired());
                        break;
                    case 4:
                        args.label = getNextArgRequired();
                        break;
                    case 5:
                        args.expiryTimeMillis = java.lang.Long.parseLong(getNextArgRequired());
                        break;
                    case 6:
                        args.tag = getNextArgRequired();
                        break;
                    case 7:
                        args.blobId = java.lang.Long.parseLong(getNextArgRequired());
                        break;
                    default:
                        pw.println("Error: unknown option '" + opt + "'");
                        return -1;
                }
            } else {
                if (args.userId == -2) {
                    args.userId = android.app.ActivityManager.getCurrentUser();
                }
                return 0;
            }
        }
    }

    private static class ParsedArgs {
        public java.lang.String algorithm;
        public long blobId;
        public byte[] digest;
        public long expiryTimeMillis;
        public java.lang.CharSequence label;
        public java.lang.String tag;
        public int userId;

        private ParsedArgs() {
            this.userId = -2;
            this.algorithm = "SHA-256";
        }

        public android.app.blob.BlobHandle getBlobHandle() {
            return android.app.blob.BlobHandle.create(this.algorithm, this.digest, this.label, this.expiryTimeMillis, this.tag);
        }
    }
}
