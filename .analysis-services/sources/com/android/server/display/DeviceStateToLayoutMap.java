package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class DeviceStateToLayoutMap {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final java.lang.String CONFIG_FILE_PATH = "etc/displayconfig/display_layout_configuration.xml";
    private static final java.lang.String CONFIG_FILE_PATH_PRIVATE = "etc/displayconfig/display_layout_configuration_private.xml";
    private static final java.lang.String DATA_CONFIG_FILE_PATH = "system/displayconfig/display_layout_configuration.xml";
    private static final java.lang.String FRONT_STRING = "front";
    private static final int POSITION_FRONT = 0;
    private static final int POSITION_REAR = 1;
    private static final int POSITION_UNKNOWN = -1;
    private static final java.lang.String REAR_STRING = "rear";
    public static final int STATE_DEFAULT = -1;
    private static final java.lang.String TAG = "DeviceStateToLayoutMap";
    private com.android.server.display.IDeviceStateToLayoutMapExt mDeviceStateToLayoutMapExt;
    private final com.android.server.display.layout.DisplayIdProducer mIdProducer;
    private final boolean mIsPortInDisplayLayoutEnabled;
    private final android.util.SparseArray<com.android.server.display.layout.Layout> mLayoutMap;

    DeviceStateToLayoutMap(com.android.server.display.layout.DisplayIdProducer idProducer, com.android.server.display.feature.DisplayManagerFlags flags) {
        this(idProducer, flags, getConfigFile());
    }

    DeviceStateToLayoutMap(com.android.server.display.layout.DisplayIdProducer idProducer, com.android.server.display.feature.DisplayManagerFlags flags, java.io.File configFile) {
        this.mLayoutMap = new android.util.SparseArray<>();
        this.mDeviceStateToLayoutMapExt = (com.android.server.display.IDeviceStateToLayoutMapExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IDeviceStateToLayoutMapExt.class).base(this).create();
        this.mIsPortInDisplayLayoutEnabled = flags.isPortInDisplayLayoutEnabled();
        this.mIdProducer = idProducer;
        loadLayoutsFromConfig(configFile);
        createLayout(-1);
    }

    public android.util.SparseArray<com.android.server.display.layout.Layout> getLayoutMap() {
        return this.mLayoutMap;
    }

    private static java.io.File getConfigFile() {
        java.io.File configFileFromDataDir = android.os.Environment.buildPath(android.os.Environment.getDataDirectory(), new java.lang.String[]{DATA_CONFIG_FILE_PATH});
        if (configFileFromDataDir.exists()) {
            return configFileFromDataDir;
        }
        return android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{CONFIG_FILE_PATH});
    }

    public void dumpLocked(android.util.IndentingPrintWriter ipw) {
        ipw.println("DeviceStateToLayoutMap:");
        ipw.increaseIndent();
        ipw.println("mIsPortInDisplayLayoutEnabled=" + this.mIsPortInDisplayLayoutEnabled);
        ipw.println("Registered Layouts:");
        for (int i = 0; i < this.mLayoutMap.size(); i++) {
            ipw.println("state(" + this.mLayoutMap.keyAt(i) + "): " + this.mLayoutMap.valueAt(i));
        }
    }

    com.android.server.display.layout.Layout get(int state) {
        com.android.server.display.layout.Layout layout = this.mLayoutMap.get(state);
        if (layout == null) {
            return this.mLayoutMap.get(-1);
        }
        return layout;
    }

    int size() {
        return this.mLayoutMap.size();
    }

    void loadLayoutsFromConfig(java.io.File configFile) {
        java.io.File configFile2;
        java.io.File configFile3 = android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{CONFIG_FILE_PATH_PRIVATE});
        if (!configFile3.exists()) {
            configFile3 = android.os.Environment.buildPath(android.os.Environment.getOdmDirectory(), new java.lang.String[]{CONFIG_FILE_PATH});
        }
        if (configFile3.exists()) {
            configFile2 = configFile3;
        } else {
            java.io.File configFile4 = android.os.Environment.buildPath(android.os.Environment.getVendorDirectory(), new java.lang.String[]{CONFIG_FILE_PATH});
            configFile2 = configFile4;
        }
        if (!configFile2.exists()) {
            return;
        }
        android.util.Slog.i(TAG, "Loading display layouts from " + configFile2);
        try {
            java.io.InputStream in = new java.io.BufferedInputStream(new java.io.FileInputStream(configFile2));
            try {
                com.android.server.display.config.layout.Layouts layouts = com.android.server.display.config.layout.XmlParser.read(in);
                if (layouts == null) {
                    android.util.Slog.i(TAG, "Display layout config not found: " + configFile2);
                    in.close();
                    return;
                }
                for (com.android.server.display.config.layout.Layout l : layouts.getLayout()) {
                    int state = l.getState().intValue();
                    com.android.server.display.layout.Layout layout = createLayout(state);
                    for (com.android.server.display.config.layout.Display d : l.getDisplay()) {
                        this.mDeviceStateToLayoutMapExt.updateRealDisplayAddressId(d, state);
                        android.view.DisplayAddress address = getDisplayAddressForLayoutDisplay(d);
                        int position = getPosition(d.getPosition());
                        java.math.BigInteger leadDisplayPhysicalId = d.getLeadDisplayAddress();
                        android.view.DisplayAddress leadDisplayAddress = leadDisplayPhysicalId == null ? null : android.view.DisplayAddress.fromPhysicalDisplayId(leadDisplayPhysicalId.longValue());
                        layout.createDisplayLocked(address, d.isDefaultDisplay(), d.isEnabled(), d.getDisplayGroup(), this.mIdProducer, position, leadDisplayAddress, d.getBrightnessThrottlingMapId(), d.getRefreshRateZoneId(), d.getRefreshRateThermalThrottlingMapId(), d.getPowerThrottlingMapId());
                        layouts = layouts;
                    }
                    com.android.server.display.config.layout.Layouts layouts2 = layouts;
                    layout.postProcessLocked();
                    layouts = layouts2;
                }
                in.close();
            } finally {
            }
        } catch (java.io.IOException | javax.xml.datatype.DatatypeConfigurationException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Encountered an error while reading/parsing display layout config file: " + configFile2, e);
        }
    }

    private android.view.DisplayAddress getDisplayAddressForLayoutDisplay(com.android.server.display.config.layout.Display display) {
        java.math.BigInteger xmlAddress = display.getAddress_optional();
        if (xmlAddress != null) {
            return android.view.DisplayAddress.fromPhysicalDisplayId(xmlAddress.longValue());
        }
        if (!this.mIsPortInDisplayLayoutEnabled || display.getPort_optional() == null) {
            throw new java.lang.IllegalArgumentException("Must specify a display identifier in display layout configuration: " + display);
        }
        return android.view.DisplayAddress.fromPortAndModel((int) display.getPort_optional().longValue(), (java.lang.Long) null);
    }

    private int getPosition(java.lang.String position) {
        if (FRONT_STRING.equals(position)) {
            return 0;
        }
        if (!REAR_STRING.equals(position)) {
            return -1;
        }
        return 1;
    }

    private com.android.server.display.layout.Layout createLayout(int state) {
        if (this.mLayoutMap.contains(state)) {
            android.util.Slog.e(TAG, "Attempted to create a second layout for state " + state);
            return null;
        }
        com.android.server.display.layout.Layout layout = new com.android.server.display.layout.Layout();
        this.mLayoutMap.append(state, layout);
        return layout;
    }
}
