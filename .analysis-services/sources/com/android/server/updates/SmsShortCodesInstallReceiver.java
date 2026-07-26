package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class SmsShortCodesInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    public SmsShortCodesInstallReceiver() {
        super("/data/misc/sms/", "codes", "metadata/", "version");
    }
}
