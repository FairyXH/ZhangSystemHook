package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
class EnterpriseSpecificIdCalculator {
    private static final int ESID_LENGTH = 16;
    private static final int PADDED_ENTERPRISE_ID_LENGTH = 64;
    private static final int PADDED_HW_ID_LENGTH = 16;
    private static final int PADDED_PROFILE_OWNER_LENGTH = 64;
    private final java.lang.String mImei;
    private final java.lang.String mMacAddress;
    private final java.lang.String mMeid;
    private final java.lang.String mSerialNumber;

    EnterpriseSpecificIdCalculator(java.lang.String imei, java.lang.String meid, java.lang.String serialNumber, java.lang.String macAddress) {
        this.mImei = imei;
        this.mMeid = meid;
        this.mSerialNumber = serialNumber;
        this.mMacAddress = macAddress;
    }

    EnterpriseSpecificIdCalculator(android.content.Context context) {
        java.lang.String imei;
        java.lang.String meid;
        android.telephony.TelephonyManager telephonyService = (android.telephony.TelephonyManager) context.getSystemService(android.telephony.TelephonyManager.class);
        com.android.internal.util.Preconditions.checkState(telephonyService != null, "Unable to access telephony service");
        try {
            imei = telephonyService.getImei(0);
        } catch (java.lang.UnsupportedOperationException e) {
            imei = null;
        }
        this.mImei = imei;
        try {
            meid = telephonyService.getMeid(0);
        } catch (java.lang.UnsupportedOperationException e2) {
            meid = null;
        }
        this.mMeid = meid;
        this.mSerialNumber = android.os.Build.getSerial();
        android.net.wifi.WifiManager wifiManager = (android.net.wifi.WifiManager) context.getSystemService(android.net.wifi.WifiManager.class);
        com.android.internal.util.Preconditions.checkState(wifiManager != null, "Unable to access WiFi service");
        java.lang.String[] macAddresses = wifiManager.getFactoryMacAddresses();
        if (macAddresses == null || macAddresses.length == 0) {
            this.mMacAddress = "";
        } else {
            this.mMacAddress = macAddresses[0];
        }
    }

    private static java.lang.String getPaddedTruncatedString(java.lang.String input, int maxLength) {
        java.lang.String paddedValue = java.lang.String.format("%" + maxLength + "s", input);
        return paddedValue.substring(0, maxLength);
    }

    private static java.lang.String getPaddedHardwareIdentifier(java.lang.String hardwareIdentifier) {
        if (hardwareIdentifier == null) {
            hardwareIdentifier = "";
        }
        return getPaddedTruncatedString(hardwareIdentifier, 16);
    }

    java.lang.String getPaddedImei() {
        return getPaddedHardwareIdentifier(this.mImei);
    }

    java.lang.String getPaddedMeid() {
        return getPaddedHardwareIdentifier(this.mMeid);
    }

    java.lang.String getPaddedSerialNumber() {
        return getPaddedHardwareIdentifier(this.mSerialNumber);
    }

    java.lang.String getPaddedProfileOwnerName(java.lang.String profileOwnerPackage) {
        return getPaddedTruncatedString(profileOwnerPackage, 64);
    }

    java.lang.String getPaddedEnterpriseId(java.lang.String enterpriseId) {
        return getPaddedTruncatedString(enterpriseId, 64);
    }

    public java.lang.String calculateEnterpriseId(java.lang.String profileOwnerPackage, java.lang.String enterpriseIdString) {
        java.lang.String enterpriseIdString2;
        boolean z = true;
        com.android.internal.util.Preconditions.checkArgument(!android.text.TextUtils.isEmpty(profileOwnerPackage), "owner package must be specified.");
        if (enterpriseIdString != null && enterpriseIdString.isEmpty()) {
            z = false;
        }
        com.android.internal.util.Preconditions.checkArgument(z, "enterprise ID must either be null or non-empty.");
        if (enterpriseIdString != null) {
            enterpriseIdString2 = enterpriseIdString;
        } else {
            enterpriseIdString2 = "";
        }
        byte[] serialNumber = getPaddedSerialNumber().getBytes();
        byte[] imei = getPaddedImei().getBytes();
        byte[] meid = getPaddedMeid().getBytes();
        byte[] macAddress = this.mMacAddress.getBytes();
        int totalIdentifiersLength = serialNumber.length + imei.length + meid.length + macAddress.length;
        java.nio.ByteBuffer fixedIdentifiers = java.nio.ByteBuffer.allocate(totalIdentifiersLength);
        fixedIdentifiers.put(serialNumber);
        fixedIdentifiers.put(imei);
        fixedIdentifiers.put(meid);
        fixedIdentifiers.put(macAddress);
        byte[] dpcPackage = getPaddedProfileOwnerName(profileOwnerPackage).getBytes();
        byte[] enterpriseId = getPaddedEnterpriseId(enterpriseIdString2).getBytes();
        java.nio.ByteBuffer info = java.nio.ByteBuffer.allocate(dpcPackage.length + enterpriseId.length);
        info.put(dpcPackage);
        info.put(enterpriseId);
        byte[] esidBytes = android.security.identity.Util.computeHkdf("HMACSHA256", fixedIdentifiers.array(), (byte[]) null, info.array(), 16);
        java.nio.ByteBuffer esidByteBuffer = java.nio.ByteBuffer.wrap(esidBytes);
        android.content.pm.VerifierDeviceIdentity firstId = new android.content.pm.VerifierDeviceIdentity(esidByteBuffer.getLong());
        android.content.pm.VerifierDeviceIdentity secondId = new android.content.pm.VerifierDeviceIdentity(esidByteBuffer.getLong());
        return firstId.toString() + secondId.toString();
    }
}
