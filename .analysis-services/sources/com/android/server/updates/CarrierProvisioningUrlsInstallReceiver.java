package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class CarrierProvisioningUrlsInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    public CarrierProvisioningUrlsInstallReceiver() {
        super("/data/misc/radio/", "provisioning_urls.xml", "metadata/", "version");
    }
}
