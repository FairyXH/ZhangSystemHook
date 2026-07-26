package com.android.server.power.stats.wakeups;

/* JADX INFO: loaded from: classes3.dex */
public class IrqDeviceMap {
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String TAG_DEVICE = "device";
    private static final java.lang.String TAG_IRQ_DEVICE_MAP = "irq-device-map";
    private static final java.lang.String TAG_SUBSYSTEM = "subsystem";
    private static android.util.LongSparseArray<com.android.server.power.stats.wakeups.IrqDeviceMap> sInstanceMap = new android.util.LongSparseArray<>(1);
    private final android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> mSubsystemsForDevice = new android.util.ArrayMap<>();

    private IrqDeviceMap(android.content.res.XmlResourceParser parser) {
        try {
            try {
                com.android.internal.util.XmlUtils.beginDocument(parser, TAG_IRQ_DEVICE_MAP);
                java.lang.String currentDevice = null;
                android.util.ArraySet<java.lang.String> subsystems = new android.util.ArraySet<>();
                while (true) {
                    int type = parser.getEventType();
                    if (type != 1) {
                        if (type == 2 && parser.getName().equals(TAG_DEVICE)) {
                            currentDevice = parser.getAttributeValue(null, "name");
                        }
                        if (currentDevice != null && type == 3 && parser.getName().equals(TAG_DEVICE)) {
                            int n = subsystems.size();
                            if (n > 0) {
                                this.mSubsystemsForDevice.put(currentDevice, java.util.Collections.unmodifiableList(new java.util.ArrayList(subsystems)));
                            }
                            subsystems.clear();
                            currentDevice = null;
                        }
                        if (currentDevice != null && type == 2 && parser.getName().equals(TAG_SUBSYSTEM)) {
                            parser.next();
                            if (parser.getEventType() == 4) {
                                subsystems.add(parser.getText());
                            }
                        }
                        parser.next();
                    } else {
                        return;
                    }
                }
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException(e);
            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                throw new java.lang.RuntimeException(e2);
            }
        } finally {
            parser.close();
        }
    }

    public static com.android.server.power.stats.wakeups.IrqDeviceMap getInstance(android.content.Context context, int resId) {
        synchronized (com.android.server.power.stats.wakeups.IrqDeviceMap.class) {
            int idx = sInstanceMap.indexOfKey(resId);
            if (idx >= 0) {
                return sInstanceMap.valueAt(idx);
            }
            android.content.res.XmlResourceParser parser = context.getResources().getXml(resId);
            com.android.server.power.stats.wakeups.IrqDeviceMap irqDeviceMap = new com.android.server.power.stats.wakeups.IrqDeviceMap(parser);
            synchronized (com.android.server.power.stats.wakeups.IrqDeviceMap.class) {
                sInstanceMap.put(resId, irqDeviceMap);
            }
            return irqDeviceMap;
        }
    }

    java.util.List<java.lang.String> getSubsystemsForDevice(java.lang.String device) {
        return this.mSubsystemsForDevice.get(device);
    }

    void dump(android.util.IndentingPrintWriter pw) {
        android.util.LongSparseArray<com.android.server.power.stats.wakeups.IrqDeviceMap> instanceMap;
        pw.println("Irq device map:");
        pw.increaseIndent();
        synchronized (com.android.server.power.stats.wakeups.IrqDeviceMap.class) {
            instanceMap = sInstanceMap;
        }
        int idx = instanceMap.indexOfValue(this);
        java.lang.String res = idx >= 0 ? "0x" + java.lang.Long.toHexString(instanceMap.keyAt(idx)) : null;
        pw.println("Loaded from xml resource: " + res);
        pw.println("Map:");
        pw.increaseIndent();
        for (int i = 0; i < this.mSubsystemsForDevice.size(); i++) {
            pw.print(this.mSubsystemsForDevice.keyAt(i) + ": ");
            pw.println(this.mSubsystemsForDevice.valueAt(i));
        }
        pw.decreaseIndent();
        pw.decreaseIndent();
    }
}
