package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class SerialService extends android.hardware.ISerialManager.Stub {
    private static final java.lang.String PREFIX_VIRTUAL = "virtual:";
    private final android.content.Context mContext;
    private final android.hardware.SerialManagerInternal mInternal;
    private final java.util.LinkedHashMap<java.lang.String, java.util.function.Supplier<android.os.ParcelFileDescriptor>> mSerialPorts;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: native_open, reason: merged with bridge method [inline-methods] */
    public native android.os.ParcelFileDescriptor lambda$new$0(java.lang.String str);

    public SerialService(android.content.Context context) {
        super(android.os.PermissionEnforcer.fromContext(context));
        this.mSerialPorts = new java.util.LinkedHashMap<>();
        this.mInternal = new android.hardware.SerialManagerInternal() { // from class: com.android.server.SerialService.1
            public void addVirtualSerialPortForTest(java.lang.String name, java.util.function.Supplier<android.os.ParcelFileDescriptor> supplier) {
                synchronized (com.android.server.SerialService.this.mSerialPorts) {
                    com.android.internal.util.Preconditions.checkState(!com.android.server.SerialService.this.mSerialPorts.containsKey(name), "Port " + name + " already defined");
                    com.android.internal.util.Preconditions.checkArgument(name.startsWith(com.android.server.SerialService.PREFIX_VIRTUAL), "Port " + name + " must be under " + com.android.server.SerialService.PREFIX_VIRTUAL);
                    com.android.server.SerialService.this.mSerialPorts.put(name, supplier);
                }
            }

            public void removeVirtualSerialPortForTest(java.lang.String name) {
                synchronized (com.android.server.SerialService.this.mSerialPorts) {
                    com.android.internal.util.Preconditions.checkState(com.android.server.SerialService.this.mSerialPorts.containsKey(name), "Port " + name + " not yet defined");
                    com.android.internal.util.Preconditions.checkArgument(name.startsWith(com.android.server.SerialService.PREFIX_VIRTUAL), "Port " + name + " must be under " + com.android.server.SerialService.PREFIX_VIRTUAL);
                    com.android.server.SerialService.this.mSerialPorts.remove(name);
                }
            }
        };
        this.mContext = context;
        synchronized (this.mSerialPorts) {
            java.lang.String[] serialPorts = getSerialPorts(context);
            for (final java.lang.String serialPort : serialPorts) {
                this.mSerialPorts.put(serialPort, new java.util.function.Supplier() { // from class: com.android.server.SerialService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Supplier
                    public final java.lang.Object get() {
                        return this.f$0.lambda$new$0(serialPort);
                    }
                });
            }
        }
    }

    private static java.lang.String[] getSerialPorts(android.content.Context context) {
        return context.getResources().getStringArray(android.R.array.config_screenBrightnessBacklight);
    }

    private static java.lang.String[] getSerialPorts$ravenwood(android.content.Context context) {
        return new java.lang.String[0];
    }

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.SerialService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.SerialService(getContext());
            publishBinderService("serial", this.mService);
            publishLocalService(android.hardware.SerialManagerInternal.class, this.mService.mInternal);
        }
    }

    public java.lang.String[] getSerialPorts() {
        java.lang.String[] strArr;
        super.getSerialPorts_enforcePermission();
        synchronized (this.mSerialPorts) {
            java.util.ArrayList<java.lang.String> ports = new java.util.ArrayList<>();
            for (java.lang.String path : this.mSerialPorts.keySet()) {
                if (path.startsWith(PREFIX_VIRTUAL) || new java.io.File(path).exists()) {
                    ports.add(path);
                }
            }
            strArr = (java.lang.String[]) ports.toArray(new java.lang.String[ports.size()]);
        }
        return strArr;
    }

    public android.os.ParcelFileDescriptor openSerialPort(java.lang.String path) {
        android.os.ParcelFileDescriptor parcelFileDescriptor;
        super.openSerialPort_enforcePermission();
        synchronized (this.mSerialPorts) {
            java.util.function.Supplier<android.os.ParcelFileDescriptor> supplier = this.mSerialPorts.get(path);
            if (supplier != null) {
                parcelFileDescriptor = supplier.get();
            } else {
                throw new java.lang.IllegalArgumentException("Invalid serial port " + path);
            }
        }
        return parcelFileDescriptor;
    }
}
