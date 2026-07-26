package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class PhoneWindowManagerSocExtImpl implements com.android.server.policy.IPhoneWindowManagerSocExt {
    com.android.server.wm.DisplayPolicy mDefaultDisplayPolicy;
    com.android.server.policy.PhoneWindowManager mPhoneWindowManager;
    private android.os.UEventObserver mHDMISwitchObserver = new android.os.UEventObserver() { // from class: com.android.server.policy.PhoneWindowManagerSocExtImpl.1
        public void onUEvent(android.os.UEventObserver.UEvent event) {
            com.android.server.policy.PhoneWindowManagerSocExtImpl.this.mDefaultDisplayPolicy.setHdmiPlugged("1".equals(event.get("STATUS")));
        }
    };
    private android.os.UEventObserver mExtEventObserver = new android.os.UEventObserver() { // from class: com.android.server.policy.PhoneWindowManagerSocExtImpl.2
        public void onUEvent(android.os.UEventObserver.UEvent event) {
            if (event.get("status") != null) {
                com.android.server.policy.PhoneWindowManagerSocExtImpl.this.mDefaultDisplayPolicy.setHdmiPlugged("connected".equals(event.get("status")));
            }
        }
    };

    public PhoneWindowManagerSocExtImpl(java.lang.Object obj) {
        this.mPhoneWindowManager = (com.android.server.policy.PhoneWindowManager) obj;
    }

    @Override // com.android.server.policy.IPhoneWindowManagerSocExt
    public void hookSetDefaultDisplay(com.android.server.wm.DisplayPolicy displayPolicy) {
        this.mDefaultDisplayPolicy = displayPolicy;
    }

    @Override // com.android.server.policy.IPhoneWindowManagerSocExt
    public void hookInitializeHdmiStateInternal() {
        this.mExtEventObserver.startObserving("mdss_mdp/drm/card");
        this.mHDMISwitchObserver.startObserving("change@/devices/virtual/graphics/fb2");
    }
}
