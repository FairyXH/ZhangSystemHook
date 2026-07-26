package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiEarcLocalDeviceTx extends com.android.server.hdmi.HdmiEarcLocalDevice {
    private static final int EARC_CAPS_DATA_START = 3;
    private static final int EARC_CAPS_LENGTH_MASK = 31;
    private static final int EARC_CAPS_PAYLOAD_LENGTH = 2;
    private static final int EARC_CAPS_TAGCODE_MASK = 224;
    private static final int EARC_CAPS_TAGCODE_SHIFT = 5;
    private static final int EXTENDED_TAGCODE_VSADB = 17;
    static final long REPORT_CAPS_MAX_DELAY_MS = 2000;
    private static final java.lang.String TAG = "HdmiEarcLocalDeviceTx";
    private static final int TAGCODE_AUDIO_DATA_BLOCK = 1;
    private static final int TAGCODE_SADB_DATA_BLOCK = 4;
    private static final int TAGCODE_USE_EXTENDED_TAG = 7;
    private static final java.lang.String[] earcStatusNames = {"HDMI_EARC_STATUS_IDLE", "HDMI_EARC_STATUS_EARC_PENDING", "HDMI_EARC_STATUS_ARC_PENDING", "HDMI_EARC_STATUS_EARC_CONNECTED"};
    private android.os.Handler mReportCapsHandler;
    private com.android.server.hdmi.HdmiEarcLocalDeviceTx.ReportCapsRunnable mReportCapsRunnable;

    HdmiEarcLocalDeviceTx(com.android.server.hdmi.HdmiControlService service) {
        super(service, 0);
        synchronized (this.mLock) {
            this.mEarcStatus = 1;
        }
        this.mReportCapsHandler = new android.os.Handler(service.getServiceLooper());
        this.mReportCapsRunnable = new com.android.server.hdmi.HdmiEarcLocalDeviceTx.ReportCapsRunnable();
    }

    private java.lang.String earcStatusToString(int status) {
        return earcStatusNames[status];
    }

    @Override // com.android.server.hdmi.HdmiEarcLocalDevice
    protected void handleEarcStateChange(int status) {
        int oldEarcStatus;
        synchronized (this.mLock) {
            com.android.server.hdmi.HdmiLogger.debug("eARC state change [old: %s(%d) new: %s(%d)]", earcStatusToString(this.mEarcStatus), java.lang.Integer.valueOf(this.mEarcStatus), earcStatusToString(status), java.lang.Integer.valueOf(status));
            oldEarcStatus = this.mEarcStatus;
            this.mEarcStatus = status;
        }
        this.mReportCapsHandler.removeCallbacksAndMessages(null);
        if (status == 0) {
            this.mService.notifyEarcStatusToAudioService(false, new java.util.ArrayList());
            this.mService.startArcAction(false, null);
            return;
        }
        if (status == 2) {
            this.mService.notifyEarcStatusToAudioService(false, new java.util.ArrayList());
            this.mService.startArcAction(true, null);
        } else if (status == 1 && oldEarcStatus == 2) {
            this.mService.startArcAction(false, null);
        } else if (status == 3) {
            if (oldEarcStatus == 2) {
                this.mService.startArcAction(false, null);
            }
            this.mReportCapsHandler.postDelayed(this.mReportCapsRunnable, REPORT_CAPS_MAX_DELAY_MS);
        }
    }

    @Override // com.android.server.hdmi.HdmiEarcLocalDevice
    protected void handleEarcCapabilitiesReported(byte[] rawCapabilities) {
        synchronized (this.mLock) {
            if (this.mEarcStatus == 3 && this.mReportCapsHandler.hasCallbacks(this.mReportCapsRunnable)) {
                this.mReportCapsHandler.removeCallbacksAndMessages(null);
                java.util.List<android.media.AudioDescriptor> audioDescriptors = parseCapabilities(rawCapabilities);
                this.mService.notifyEarcStatusToAudioService(true, audioDescriptors);
            }
        }
    }

    private class ReportCapsRunnable implements java.lang.Runnable {
        private ReportCapsRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.hdmi.HdmiEarcLocalDeviceTx.this.mLock) {
                if (com.android.server.hdmi.HdmiEarcLocalDeviceTx.this.mEarcStatus == 3) {
                    com.android.server.hdmi.HdmiEarcLocalDeviceTx.this.mService.notifyEarcStatusToAudioService(true, new java.util.ArrayList());
                }
            }
        }
    }

    private java.util.List<android.media.AudioDescriptor> parseCapabilities(byte[] rawCapabilities) {
        java.util.List<android.media.AudioDescriptor> audioDescriptors = new java.util.ArrayList<>();
        if (rawCapabilities.length < 4) {
            android.util.Slog.i(TAG, "Raw eARC capabilities array doesn´t contain any blocks.");
            return audioDescriptors;
        }
        int earcCapsSize = rawCapabilities[2];
        if (rawCapabilities.length < earcCapsSize) {
            android.util.Slog.i(TAG, "Raw eARC capabilities array is shorter than the reported payload length.");
            return audioDescriptors;
        }
        int firstByteOfBlock = 3;
        while (firstByteOfBlock < earcCapsSize) {
            int tagCode = (rawCapabilities[firstByteOfBlock] & 224) >> 5;
            int length = rawCapabilities[firstByteOfBlock] & 31;
            if (length != 0) {
                switch (tagCode) {
                    case 1:
                        if (length % 3 != 0) {
                            android.util.Slog.e(TAG, "Invalid length of SAD block: expected a factor of 3 but got " + (length % 3));
                        } else {
                            byte[] earcSad = new byte[length];
                            java.lang.System.arraycopy(rawCapabilities, firstByteOfBlock + 1, earcSad, 0, length);
                            for (int i = 0; i < length; i += 3) {
                                android.media.AudioDescriptor descriptor = new android.media.AudioDescriptor(1, 0, java.util.Arrays.copyOfRange(earcSad, i, i + 3));
                                audioDescriptors.add(descriptor);
                            }
                        }
                        break;
                    case 4:
                        int earcSadbLen = length + 1;
                        byte[] earcSadb = new byte[earcSadbLen];
                        java.lang.System.arraycopy(rawCapabilities, firstByteOfBlock, earcSadb, 0, earcSadbLen);
                        android.media.AudioDescriptor descriptor2 = new android.media.AudioDescriptor(2, 0, earcSadb);
                        audioDescriptors.add(descriptor2);
                        break;
                    case 7:
                        if (rawCapabilities[firstByteOfBlock + 1] == 17) {
                            int earcVsadbLen = length + 1;
                            byte[] earcVsadb = new byte[earcVsadbLen];
                            java.lang.System.arraycopy(rawCapabilities, firstByteOfBlock, earcVsadb, 0, earcVsadbLen);
                            android.media.AudioDescriptor descriptor3 = new android.media.AudioDescriptor(3, 0, earcVsadb);
                            audioDescriptors.add(descriptor3);
                        }
                        break;
                    default:
                        android.util.Slog.w(TAG, "This tagcode was not handled: " + tagCode);
                        break;
                }
                firstByteOfBlock += length + 1;
            } else {
                return audioDescriptors;
            }
        }
        return audioDescriptors;
    }

    @Override // com.android.server.hdmi.HdmiEarcLocalDevice
    protected void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("TX, mEarcStatus: " + this.mEarcStatus);
        }
    }
}
