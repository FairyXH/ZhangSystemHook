package com.android.server.display.layout;

/* JADX INFO: loaded from: classes2.dex */
public class Layout {
    public static final java.lang.String DEFAULT_DISPLAY_GROUP_NAME = "";
    public static final int NO_LEAD_DISPLAY = -1;
    public static final int SECOND_DEFAULT_DISPLAY = 1;
    private static final java.lang.String TAG = "Layout";
    private static int sNextNonDefaultDisplayId = 2;
    private final java.util.List<com.android.server.display.layout.Layout.Display> mDisplays = new java.util.ArrayList(2);

    public static int assignDisplayIdLocked(boolean isDefault) {
        if (isDefault) {
            return 0;
        }
        int i = sNextNonDefaultDisplayId;
        sNextNonDefaultDisplayId = i + 1;
        return i;
    }

    public static int assignDisplayIdLocked(boolean isDefault, android.view.DisplayAddress address) {
        boolean isDisplayBuiltIn = false;
        if (address != null && (address instanceof android.view.DisplayAddress.Physical)) {
            isDisplayBuiltIn = ((android.view.DisplayAddress.Physical) address).getPort() == 3;
        }
        if (!isDefault && isDisplayBuiltIn) {
            return 1;
        }
        return assignDisplayIdLocked(isDefault);
    }

    public java.lang.String toString() {
        return this.mDisplays.toString();
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof com.android.server.display.layout.Layout)) {
            return false;
        }
        com.android.server.display.layout.Layout otherLayout = (com.android.server.display.layout.Layout) obj;
        return this.mDisplays.equals(otherLayout.mDisplays);
    }

    public int hashCode() {
        return this.mDisplays.hashCode();
    }

    public void createDefaultDisplayLocked(android.view.DisplayAddress address, com.android.server.display.layout.DisplayIdProducer idProducer) {
        createDisplayLocked(address, true, true, "", idProducer, -1, null, null, null, null, null);
    }

    public void createDisplayLocked(android.view.DisplayAddress address, boolean isDefault, boolean isEnabled, java.lang.String displayGroupName, com.android.server.display.layout.DisplayIdProducer idProducer, int position, android.view.DisplayAddress leadDisplayAddress, java.lang.String brightnessThrottlingMapId, java.lang.String refreshRateZoneId, java.lang.String refreshRateThermalThrottlingMapId, java.lang.String powerThrottlingMapId) {
        java.lang.String displayGroupName2;
        if (contains(address)) {
            android.util.Slog.w(TAG, "Attempting to add second definition for display-device: " + address);
            return;
        }
        if (isDefault && getById(0) != null) {
            android.util.Slog.w(TAG, "Ignoring attempt to add a second default display: " + address);
            return;
        }
        if (displayGroupName != null) {
            displayGroupName2 = displayGroupName;
        } else {
            displayGroupName2 = "";
        }
        if (isDefault && !displayGroupName2.equals("")) {
            throw new java.lang.IllegalArgumentException("Default display should own DEFAULT_DISPLAY_GROUP");
        }
        if (isDefault && leadDisplayAddress != null) {
            throw new java.lang.IllegalArgumentException("Default display cannot have a lead display");
        }
        if (address.equals(leadDisplayAddress)) {
            throw new java.lang.IllegalArgumentException("Lead display address cannot be the same as display  address");
        }
        int logicalDisplayId = assignDisplayIdLocked(isDefault, address);
        com.android.server.display.layout.Layout.Display display = new com.android.server.display.layout.Layout.Display(address, logicalDisplayId, isEnabled, displayGroupName2, brightnessThrottlingMapId, position, leadDisplayAddress, refreshRateZoneId, refreshRateThermalThrottlingMapId, powerThrottlingMapId);
        this.mDisplays.add(display);
    }

    public void removeDisplayLocked(int id) {
        com.android.server.display.layout.Layout.Display display = getById(id);
        if (display != null) {
            this.mDisplays.remove(display);
        }
    }

    public void postProcessLocked() {
        for (int i = 0; i < this.mDisplays.size(); i++) {
            com.android.server.display.layout.Layout.Display display = this.mDisplays.get(i);
            if (display.getLogicalDisplayId() == 0) {
                display.setLeadDisplayId(-1);
            } else {
                android.view.DisplayAddress leadDisplayAddress = display.getLeadDisplayAddress();
                if (leadDisplayAddress == null) {
                    display.setLeadDisplayId(-1);
                } else {
                    com.android.server.display.layout.Layout.Display leadDisplay = getByAddress(leadDisplayAddress);
                    if (leadDisplay == null) {
                        throw new java.lang.IllegalArgumentException("Cannot find a lead display whose address is " + leadDisplayAddress);
                    }
                    if (!android.text.TextUtils.equals(display.getDisplayGroupName(), leadDisplay.getDisplayGroupName())) {
                        throw new java.lang.IllegalArgumentException("Lead display(" + leadDisplay + ") should be in the same display group of the display(" + display + ")");
                    }
                    if (hasCyclicLeadDisplay(display)) {
                        throw new java.lang.IllegalArgumentException("Display(" + display + ") has a cyclic lead display");
                    }
                    display.setLeadDisplayId(leadDisplay.getLogicalDisplayId());
                }
            }
        }
    }

    public boolean contains(android.view.DisplayAddress address) {
        return getByAddress(address) != null;
    }

    public com.android.server.display.layout.Layout.Display getById(int id) {
        for (int i = 0; i < this.mDisplays.size(); i++) {
            com.android.server.display.layout.Layout.Display display = this.mDisplays.get(i);
            if (id == display.getLogicalDisplayId()) {
                return display;
            }
        }
        return null;
    }

    public com.android.server.display.layout.Layout.Display getByAddress(android.view.DisplayAddress address) {
        for (int i = 0; i < this.mDisplays.size(); i++) {
            com.android.server.display.layout.Layout.Display display = this.mDisplays.get(i);
            if (address.equals(display.getAddress())) {
                return display;
            }
            if (android.view.DisplayAddress.Physical.isPortMatch(address, display.getAddress())) {
                return display;
            }
        }
        return null;
    }

    public com.android.server.display.layout.Layout.Display getAt(int index) {
        return this.mDisplays.get(index);
    }

    public int size() {
        return this.mDisplays.size();
    }

    private boolean hasCyclicLeadDisplay(com.android.server.display.layout.Layout.Display display) {
        android.util.ArraySet<com.android.server.display.layout.Layout.Display> visited = new android.util.ArraySet<>();
        while (display != null) {
            if (visited.contains(display)) {
                return true;
            }
            visited.add(display);
            android.view.DisplayAddress leadDisplayAddress = display.getLeadDisplayAddress();
            display = leadDisplayAddress == null ? null : getByAddress(leadDisplayAddress);
        }
        return false;
    }

    public static class Display {
        public static final int POSITION_FRONT = 0;
        public static final int POSITION_REAR = 1;
        public static final int POSITION_UNKNOWN = -1;
        private final android.view.DisplayAddress mAddress;
        private final java.lang.String mDisplayGroupName;
        private final boolean mIsEnabled;
        private final android.view.DisplayAddress mLeadDisplayAddress;
        private int mLeadDisplayId;
        private final int mLogicalDisplayId;
        private final int mPosition;
        private final java.lang.String mPowerThrottlingMapId;
        private final java.lang.String mRefreshRateZoneId;
        private final java.lang.String mThermalBrightnessThrottlingMapId;
        private final java.lang.String mThermalRefreshRateThrottlingMapId;

        private Display(android.view.DisplayAddress address, int logicalDisplayId, boolean isEnabled, java.lang.String displayGroupName, java.lang.String brightnessThrottlingMapId, int position, android.view.DisplayAddress leadDisplayAddress, java.lang.String refreshRateZoneId, java.lang.String refreshRateThermalThrottlingMapId, java.lang.String powerThrottlingMapId) {
            this.mAddress = address;
            this.mLogicalDisplayId = logicalDisplayId;
            this.mIsEnabled = isEnabled;
            this.mDisplayGroupName = displayGroupName;
            this.mPosition = position;
            this.mThermalBrightnessThrottlingMapId = brightnessThrottlingMapId;
            this.mLeadDisplayAddress = leadDisplayAddress;
            this.mRefreshRateZoneId = refreshRateZoneId;
            this.mThermalRefreshRateThrottlingMapId = refreshRateThermalThrottlingMapId;
            this.mPowerThrottlingMapId = powerThrottlingMapId;
            this.mLeadDisplayId = -1;
        }

        public java.lang.String toString() {
            return "{dispId: " + this.mLogicalDisplayId + "(" + (this.mIsEnabled ? "ON" : "OFF") + "), displayGroupName: " + this.mDisplayGroupName + ", addr: " + this.mAddress + (this.mPosition == -1 ? "" : ", position: " + this.mPosition) + ", mThermalBrightnessThrottlingMapId: " + this.mThermalBrightnessThrottlingMapId + ", mRefreshRateZoneId: " + this.mRefreshRateZoneId + ", mLeadDisplayId: " + this.mLeadDisplayId + ", mLeadDisplayAddress: " + this.mLeadDisplayAddress + ", mThermalRefreshRateThrottlingMapId: " + this.mThermalRefreshRateThrottlingMapId + ", mPowerThrottlingMapId: " + this.mPowerThrottlingMapId + "}";
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.display.layout.Layout.Display)) {
                return false;
            }
            com.android.server.display.layout.Layout.Display otherDisplay = (com.android.server.display.layout.Layout.Display) obj;
            return otherDisplay.mIsEnabled == this.mIsEnabled && otherDisplay.mPosition == this.mPosition && otherDisplay.mLogicalDisplayId == this.mLogicalDisplayId && this.mDisplayGroupName.equals(otherDisplay.mDisplayGroupName) && this.mAddress.equals(otherDisplay.mAddress) && java.util.Objects.equals(this.mThermalBrightnessThrottlingMapId, otherDisplay.mThermalBrightnessThrottlingMapId) && java.util.Objects.equals(otherDisplay.mRefreshRateZoneId, this.mRefreshRateZoneId) && this.mLeadDisplayId == otherDisplay.mLeadDisplayId && java.util.Objects.equals(this.mLeadDisplayAddress, otherDisplay.mLeadDisplayAddress) && java.util.Objects.equals(this.mThermalRefreshRateThrottlingMapId, otherDisplay.mThermalRefreshRateThrottlingMapId) && java.util.Objects.equals(this.mPowerThrottlingMapId, otherDisplay.mPowerThrottlingMapId);
        }

        public int hashCode() {
            int result = (1 * 31) + java.lang.Boolean.hashCode(this.mIsEnabled);
            return (((((((((((((((((((result * 31) + this.mPosition) * 31) + this.mLogicalDisplayId) * 31) + this.mDisplayGroupName.hashCode()) * 31) + this.mAddress.hashCode()) * 31) + java.util.Objects.hashCode(this.mThermalBrightnessThrottlingMapId)) * 31) + java.util.Objects.hashCode(this.mRefreshRateZoneId)) * 31) + this.mLeadDisplayId) * 31) + java.util.Objects.hashCode(this.mLeadDisplayAddress)) * 31) + java.util.Objects.hashCode(this.mThermalRefreshRateThrottlingMapId)) * 31) + java.util.Objects.hashCode(this.mPowerThrottlingMapId);
        }

        public android.view.DisplayAddress getAddress() {
            return this.mAddress;
        }

        public int getLogicalDisplayId() {
            return this.mLogicalDisplayId;
        }

        public boolean isEnabled() {
            return this.mIsEnabled;
        }

        public java.lang.String getDisplayGroupName() {
            return this.mDisplayGroupName;
        }

        public java.lang.String getRefreshRateZoneId() {
            return this.mRefreshRateZoneId;
        }

        public java.lang.String getThermalBrightnessThrottlingMapId() {
            return this.mThermalBrightnessThrottlingMapId;
        }

        public int getPosition() {
            return this.mPosition;
        }

        public int getLeadDisplayId() {
            return this.mLeadDisplayId;
        }

        public android.view.DisplayAddress getLeadDisplayAddress() {
            return this.mLeadDisplayAddress;
        }

        public java.lang.String getRefreshRateThermalThrottlingMapId() {
            return this.mThermalRefreshRateThrottlingMapId;
        }

        public java.lang.String getPowerThrottlingMapId() {
            return this.mPowerThrottlingMapId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setLeadDisplayId(int id) {
            this.mLeadDisplayId = id;
        }
    }
}
