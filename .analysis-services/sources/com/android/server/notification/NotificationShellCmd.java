package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationShellCmd extends android.os.ShellCommand {
    public static final java.lang.String CHANNEL_ID = "shell_cmd";
    public static final int CHANNEL_IMP = 3;
    public static final java.lang.String CHANNEL_NAME = "Shell command";
    public static final int NOTIFICATION_ID = 2020;
    private static final java.lang.String NOTIFY_USAGE = "usage: cmd notification post [flags] <tag> <text>\n\nflags:\n  -h|--help\n  -v|--verbose\n  -t|--title <text>\n  -i|--icon <iconspec>\n  -I|--large-icon <iconspec>\n  -S|--style <style> [styleargs]\n  -c|--content-intent <intentspec>\n\nstyles: (default none)\n  bigtext\n  bigpicture --picture <iconspec>\n  inbox --line <text> --line <text> ...\n  messaging --conversation <title> --message <who>:<text> ...\n  media\n\nan <iconspec> is one of\n  file:///data/local/tmp/<img.png>\n  content://<provider>/<path>\n  @[<package>:]drawable/<img>\n  data:base64,<B64DATA==>\n\nan <intentspec> is (broadcast|service|activity) <args>\n  <args> are as described in `am start`";
    private static final java.lang.String TAG = "NotifShellCmd";
    private static final java.lang.String USAGE = "usage: cmd notification SUBCMD [args]\n\nSUBCMDs:\n  allow_listener COMPONENT [user_id (current user if not specified)]\n  disallow_listener COMPONENT [user_id (current user if not specified)]\n  allow_assistant COMPONENT [user_id (current user if not specified)]\n  remove_assistant COMPONENT [user_id (current user if not specified)]\n  set_dnd [on|none (same as on)|priority|alarms|all|off (same as all)]\n  allow_dnd PACKAGE [user_id (current user if not specified)]\n  disallow_dnd PACKAGE [user_id (current user if not specified)]\n  reset_assistant_user_set [user_id (current user if not specified)]\n  get_approved_assistant [user_id (current user if not specified)]\n  post [--help | flags] TAG TEXT\n  set_bubbles PACKAGE PREFERENCE (0=none 1=all 2=selected) [user_id (current user if not specified)]\n  set_bubbles_channel PACKAGE CHANNEL_ID ALLOW [user_id (current user if not specified)]\n  list\n  get <notification-key>\n  snooze --for <msec> <notification-key>\n  unsnooze <notification-key>\n";
    private final android.app.INotificationManager mBinderService;
    private final com.android.server.notification.NotificationManagerService mDirectService;
    private final android.content.pm.PackageManager mPm;

    public NotificationShellCmd(com.android.server.notification.NotificationManagerService service) {
        this.mDirectService = service;
        this.mBinderService = service.getBinderService();
        this.mPm = this.mDirectService.getContext().getPackageManager();
    }

    protected boolean checkShellCommandPermission(int callingUid) {
        return callingUid == 0 || callingUid == 2000;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01f7  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0593  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0171  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r26) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1774
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationShellCmd.onCommand(java.lang.String):int");
    }

    void ensureChannel(java.lang.String callingPackage, int callingUid) throws android.os.RemoteException {
        android.app.NotificationChannel channel = new android.app.NotificationChannel(CHANNEL_ID, CHANNEL_NAME, 3);
        this.mBinderService.createNotificationChannels(callingPackage, new android.content.pm.ParceledListSlice(java.util.Collections.singletonList(channel)));
        android.util.Slog.v(com.android.server.notification.NotificationManagerService.TAG, "created channel: " + this.mBinderService.getNotificationChannel(callingPackage, android.os.UserHandle.getUserId(callingUid), callingPackage, CHANNEL_ID));
    }

    android.graphics.drawable.Icon parseIcon(android.content.res.Resources res, java.lang.String encoded) throws java.lang.IllegalArgumentException {
        if (android.text.TextUtils.isEmpty(encoded)) {
            return null;
        }
        if (encoded.startsWith(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER)) {
            encoded = "file://" + encoded;
        }
        if (encoded.startsWith("http:") || encoded.startsWith("https:") || encoded.startsWith("content:") || encoded.startsWith("file:") || encoded.startsWith("android.resource:")) {
            android.net.Uri asUri = android.net.Uri.parse(encoded);
            return android.graphics.drawable.Icon.createWithContentUri(asUri);
        }
        if (encoded.startsWith("@")) {
            int resid = res.getIdentifier(encoded.substring(1), "drawable", com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
            if (resid != 0) {
                return android.graphics.drawable.Icon.createWithResource(res, resid);
            }
        } else if (encoded.startsWith("data:")) {
            byte[] bits = android.util.Base64.decode(encoded.substring(encoded.indexOf(44) + 1), 0);
            return android.graphics.drawable.Icon.createWithData(bits, 0, bits.length);
        }
        return null;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0330  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x03df  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0490  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0478 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x019d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int doNotify(java.io.PrintWriter r29, java.lang.String r30, int r31) throws java.net.URISyntaxException, android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 1692
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.notification.NotificationShellCmd.doNotify(java.io.PrintWriter, java.lang.String, int):int");
    }

    private void waitForSnooze(com.android.server.notification.NotificationShellCmd.ShellNls nls, java.lang.String key) {
        for (int i = 0; i < 20; i++) {
            android.service.notification.StatusBarNotification[] sbns = nls.getSnoozedNotifications();
            for (android.service.notification.StatusBarNotification sbn : sbns) {
                if (sbn.getKey().equals(key)) {
                    return;
                }
            }
            try {
                java.lang.Thread.sleep(100L);
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean waitForBind(com.android.server.notification.NotificationShellCmd.ShellNls nls) {
        for (int i = 0; i < 20; i++) {
            if (nls.isConnected) {
                android.util.Slog.i(TAG, "Bound Shell NLS");
                return true;
            }
            try {
                java.lang.Thread.sleep(100L);
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void waitForUnbind(com.android.server.notification.NotificationShellCmd.ShellNls nls) {
        for (int i = 0; i < 10; i++) {
            if (!nls.isConnected) {
                android.util.Slog.i(TAG, "Unbound Shell NLS");
                return;
            }
            try {
                java.lang.Thread.sleep(100L);
            } catch (java.lang.InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onHelp() {
        getOutPrintWriter().println(USAGE);
    }

    private static class ShellNls extends android.service.notification.NotificationListenerService {
        private static com.android.server.notification.NotificationShellCmd.ShellNls sNotificationListenerInstance = null;
        boolean isConnected;

        private ShellNls() {
        }

        @Override // android.service.notification.NotificationListenerService
        public void onListenerConnected() {
            super.onListenerConnected();
            sNotificationListenerInstance = this;
            this.isConnected = true;
        }

        @Override // android.service.notification.NotificationListenerService
        public void onListenerDisconnected() {
            this.isConnected = false;
        }

        public static com.android.server.notification.NotificationShellCmd.ShellNls getInstance() {
            return sNotificationListenerInstance;
        }
    }
}
