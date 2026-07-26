package com.android.server.companion.virtual;

/* JADX INFO: loaded from: classes.dex */
class InputController {
    static final java.lang.String NAVIGATION_TOUCHPAD_DEVICE_TYPE = "touchNavigation";
    static final java.lang.String PHYS_TYPE_DPAD = "Dpad";
    static final java.lang.String PHYS_TYPE_KEYBOARD = "Keyboard";
    static final java.lang.String PHYS_TYPE_MOUSE = "Mouse";
    static final java.lang.String PHYS_TYPE_NAVIGATION_TOUCHPAD = "NavigationTouchpad";
    static final java.lang.String PHYS_TYPE_STYLUS = "Stylus";
    static final java.lang.String PHYS_TYPE_TOUCHSCREEN = "Touchscreen";
    private static final java.lang.String TAG = "VirtualInputController";
    private static final java.util.concurrent.atomic.AtomicLong sNextPhysId = new java.util.concurrent.atomic.AtomicLong(1);
    private final android.content.AttributionSource mAttributionSource;
    private final android.hardware.display.DisplayManagerInternal mDisplayManagerInternal;
    private final android.os.Handler mHandler;
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.companion.virtual.InputController.InputDeviceDescriptor> mInputDeviceDescriptors;
    private final com.android.server.input.InputManagerInternal mInputManagerInternal;
    final java.lang.Object mLock;
    private final com.android.server.companion.virtual.InputController.NativeWrapper mNativeWrapper;
    private final com.android.server.companion.virtual.InputController.DeviceCreationThreadVerifier mThreadVerifier;
    private final android.view.WindowManager mWindowManager;

    interface DeviceCreationThreadVerifier {
        boolean isValidThread();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface PhysType {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeCloseUinput(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputDpad(java.lang.String str, int i, int i2, java.lang.String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputKeyboard(java.lang.String str, int i, int i2, java.lang.String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputMouse(java.lang.String str, int i, int i2, java.lang.String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputStylus(java.lang.String str, int i, int i2, java.lang.String str2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeOpenUinputTouchscreen(java.lang.String str, int i, int i2, java.lang.String str2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteButtonEvent(long j, int i, int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteDpadKeyEvent(long j, int i, int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteKeyEvent(long j, int i, int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteRelativeEvent(long j, float f, float f2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteScrollEvent(long j, float f, float f2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteStylusButtonEvent(long j, int i, int i2, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteStylusMotionEvent(long j, int i, int i2, int i3, int i4, int i5, int i6, int i7, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nativeWriteTouchEvent(long j, int i, int i2, int i3, float f, float f2, float f3, float f4, long j2);

    InputController(final android.os.Handler handler, android.view.WindowManager windowManager, android.content.AttributionSource attributionSource) {
        this(new com.android.server.companion.virtual.InputController.NativeWrapper(), handler, windowManager, attributionSource, new com.android.server.companion.virtual.InputController.DeviceCreationThreadVerifier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda1
            @Override // com.android.server.companion.virtual.InputController.DeviceCreationThreadVerifier
            public final boolean isValidThread() {
                return com.android.server.companion.virtual.InputController.lambda$new$0(handler);
            }
        });
    }

    static /* synthetic */ boolean lambda$new$0(android.os.Handler handler) {
        return !handler.getLooper().isCurrentThread();
    }

    InputController(com.android.server.companion.virtual.InputController.NativeWrapper nativeWrapper, android.os.Handler handler, android.view.WindowManager windowManager, android.content.AttributionSource attributionSource, com.android.server.companion.virtual.InputController.DeviceCreationThreadVerifier threadVerifier) {
        this.mLock = new java.lang.Object();
        this.mInputDeviceDescriptors = new android.util.ArrayMap<>();
        this.mHandler = handler;
        this.mNativeWrapper = nativeWrapper;
        this.mDisplayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
        this.mInputManagerInternal = (com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class);
        this.mWindowManager = windowManager;
        this.mAttributionSource = attributionSource;
        this.mThreadVerifier = threadVerifier;
    }

    void close() {
        synchronized (this.mLock) {
            java.util.Iterator<java.util.Map.Entry<android.os.IBinder, com.android.server.companion.virtual.InputController.InputDeviceDescriptor>> iterator = this.mInputDeviceDescriptors.entrySet().iterator();
            if (iterator.hasNext()) {
                java.util.Map.Entry<android.os.IBinder, com.android.server.companion.virtual.InputController.InputDeviceDescriptor> entry = iterator.next();
                android.os.IBinder token = entry.getKey();
                com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = entry.getValue();
                iterator.remove();
                closeInputDeviceDescriptorLocked(token, inputDeviceDescriptor);
            }
        }
    }

    void createDpad(final java.lang.String deviceName, final int vendorId, final int productId, android.os.IBinder deviceToken, int displayId) throws java.lang.Throwable {
        final java.lang.String phys = createPhys(PHYS_TYPE_DPAD);
        createDeviceInternal(4, deviceName, vendorId, productId, deviceToken, displayId, phys, new java.util.function.Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$createDpad$1(deviceName, vendorId, productId, phys);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Long lambda$createDpad$1(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys) {
        return java.lang.Long.valueOf(this.mNativeWrapper.openUinputDpad(deviceName, vendorId, productId, phys));
    }

    void createKeyboard(final java.lang.String deviceName, final int vendorId, final int productId, android.os.IBinder deviceToken, int displayId, java.lang.String languageTag, java.lang.String layoutType) throws java.lang.Throwable {
        final java.lang.String phys = createPhys(PHYS_TYPE_KEYBOARD);
        this.mInputManagerInternal.addKeyboardLayoutAssociation(phys, languageTag, layoutType);
        try {
            createDeviceInternal(1, deviceName, vendorId, productId, deviceToken, displayId, phys, new java.util.function.Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$createKeyboard$2(deviceName, vendorId, productId, phys);
                }
            });
        } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
            this.mInputManagerInternal.removeKeyboardLayoutAssociation(phys);
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Long lambda$createKeyboard$2(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys) {
        return java.lang.Long.valueOf(this.mNativeWrapper.openUinputKeyboard(deviceName, vendorId, productId, phys));
    }

    void createMouse(final java.lang.String deviceName, final int vendorId, final int productId, android.os.IBinder deviceToken, int displayId) throws java.lang.Throwable {
        final java.lang.String phys = createPhys(PHYS_TYPE_MOUSE);
        createDeviceInternal(2, deviceName, vendorId, productId, deviceToken, displayId, phys, new java.util.function.Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda2
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$createMouse$3(deviceName, vendorId, productId, phys);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Long lambda$createMouse$3(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys) {
        return java.lang.Long.valueOf(this.mNativeWrapper.openUinputMouse(deviceName, vendorId, productId, phys));
    }

    void createTouchscreen(final java.lang.String deviceName, final int vendorId, final int productId, android.os.IBinder deviceToken, int displayId, final int height, final int width) throws java.lang.Throwable {
        final java.lang.String phys = createPhys(PHYS_TYPE_TOUCHSCREEN);
        createDeviceInternal(3, deviceName, vendorId, productId, deviceToken, displayId, phys, new java.util.function.Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$createTouchscreen$4(deviceName, vendorId, productId, phys, height, width);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Long lambda$createTouchscreen$4(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys, int height, int width) {
        return java.lang.Long.valueOf(this.mNativeWrapper.openUinputTouchscreen(deviceName, vendorId, productId, phys, height, width));
    }

    void createNavigationTouchpad(final java.lang.String deviceName, final int vendorId, final int productId, android.os.IBinder deviceToken, int displayId, final int height, final int width) throws java.lang.Throwable {
        final java.lang.String phys = createPhys(PHYS_TYPE_NAVIGATION_TOUCHPAD);
        this.mInputManagerInternal.setTypeAssociation(phys, NAVIGATION_TOUCHPAD_DEVICE_TYPE);
        try {
            createDeviceInternal(5, deviceName, vendorId, productId, deviceToken, displayId, phys, new java.util.function.Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda6
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$createNavigationTouchpad$5(deviceName, vendorId, productId, phys, height, width);
                }
            });
        } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e) {
            this.mInputManagerInternal.unsetTypeAssociation(phys);
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Long lambda$createNavigationTouchpad$5(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys, int height, int width) {
        return java.lang.Long.valueOf(this.mNativeWrapper.openUinputTouchscreen(deviceName, vendorId, productId, phys, height, width));
    }

    void createStylus(final java.lang.String deviceName, final int vendorId, final int productId, android.os.IBinder deviceToken, int displayId, final int height, final int width) throws java.lang.Throwable {
        final java.lang.String phys = createPhys(PHYS_TYPE_STYLUS);
        createDeviceInternal(6, deviceName, vendorId, productId, deviceToken, displayId, phys, new java.util.function.Supplier() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda7
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$createStylus$6(deviceName, vendorId, productId, phys, height, width);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Long lambda$createStylus$6(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys, int height, int width) {
        return java.lang.Long.valueOf(this.mNativeWrapper.openUinputStylus(deviceName, vendorId, productId, phys, height, width));
    }

    void unregisterInputDevice(android.os.IBinder token) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.remove(token);
            if (inputDeviceDescriptor == null) {
                android.util.Slog.w(TAG, "Could not unregister input device for given token.");
            } else {
                closeInputDeviceDescriptorLocked(token, inputDeviceDescriptor);
            }
        }
    }

    private void closeInputDeviceDescriptorLocked(android.os.IBinder token, com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor) {
        token.unlinkToDeath(inputDeviceDescriptor.getDeathRecipient(), 0);
        this.mNativeWrapper.closeUinput(inputDeviceDescriptor.getNativePointer());
        java.lang.String phys = inputDeviceDescriptor.getPhys();
        android.hardware.input.InputManagerGlobal.getInstance().removeUniqueIdAssociationByPort(phys);
        if (inputDeviceDescriptor.getType() == 5) {
            this.mInputManagerInternal.unsetTypeAssociation(phys);
        }
        if (inputDeviceDescriptor.getType() == 1) {
            this.mInputManagerInternal.removeKeyboardLayoutAssociation(phys);
        }
    }

    int getInputDeviceId(android.os.IBinder token) {
        int inputDeviceId;
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                throw new java.lang.IllegalArgumentException("Could not get device id for given token");
            }
            inputDeviceId = inputDeviceDescriptor.getInputDeviceId();
        }
        return inputDeviceId;
    }

    void setShowPointerIcon(boolean visible, int displayId) {
        this.mInputManagerInternal.setPointerIconVisible(visible, displayId);
    }

    void setMousePointerAccelerationEnabled(boolean enabled, int displayId) {
        this.mInputManagerInternal.setMousePointerAccelerationEnabled(enabled, displayId);
    }

    void setDisplayEligibilityForPointerCapture(boolean isEligible, int displayId) {
        this.mInputManagerInternal.setDisplayEligibilityForPointerCapture(displayId, isEligible);
    }

    void setDisplayImePolicy(int displayId, int policy) {
        this.mWindowManager.setDisplayImePolicy(displayId, policy);
    }

    private void validateDeviceName(java.lang.String deviceName) throws com.android.server.companion.virtual.InputController.DeviceCreationException {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mInputDeviceDescriptors.size(); i++) {
                if (this.mInputDeviceDescriptors.valueAt(i).mName.equals(deviceName)) {
                    throw new com.android.server.companion.virtual.InputController.DeviceCreationException("Input device name already in use: " + deviceName);
                }
            }
        }
    }

    private static java.lang.String createPhys(java.lang.String type) {
        return android.text.TextUtils.formatSimple("virtual%s:%d", new java.lang.Object[]{type, java.lang.Long.valueOf(sNextPhysId.getAndIncrement())});
    }

    private void setUniqueIdAssociation(int displayId, java.lang.String phys) {
        java.lang.String displayUniqueId = this.mDisplayManagerInternal.getDisplayInfo(displayId).uniqueId;
        android.hardware.input.InputManagerGlobal.getInstance().addUniqueIdAssociationByPort(phys, displayUniqueId);
    }

    boolean sendDpadKeyEvent(android.os.IBinder token, android.hardware.input.VirtualKeyEvent event) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                return false;
            }
            return this.mNativeWrapper.writeDpadKeyEvent(inputDeviceDescriptor.getNativePointer(), event.getKeyCode(), event.getAction(), event.getEventTimeNanos());
        }
    }

    boolean sendKeyEvent(android.os.IBinder token, android.hardware.input.VirtualKeyEvent event) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                return false;
            }
            return this.mNativeWrapper.writeKeyEvent(inputDeviceDescriptor.getNativePointer(), event.getKeyCode(), event.getAction(), event.getEventTimeNanos());
        }
    }

    boolean sendButtonEvent(android.os.IBinder token, android.hardware.input.VirtualMouseButtonEvent event) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                return false;
            }
            return this.mNativeWrapper.writeButtonEvent(inputDeviceDescriptor.getNativePointer(), event.getButtonCode(), event.getAction(), event.getEventTimeNanos());
        }
    }

    boolean sendTouchEvent(android.os.IBinder token, android.hardware.input.VirtualTouchEvent event) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
                    if (inputDeviceDescriptor == null) {
                        return false;
                    }
                    return this.mNativeWrapper.writeTouchEvent(inputDeviceDescriptor.getNativePointer(), event.getPointerId(), event.getToolType(), event.getAction(), event.getX(), event.getY(), event.getPressure(), event.getMajorAxisSize(), event.getEventTimeNanos());
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    boolean sendRelativeEvent(android.os.IBinder token, android.hardware.input.VirtualMouseRelativeEvent event) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                return false;
            }
            return this.mNativeWrapper.writeRelativeEvent(inputDeviceDescriptor.getNativePointer(), event.getRelativeX(), event.getRelativeY(), event.getEventTimeNanos());
        }
    }

    boolean sendScrollEvent(android.os.IBinder token, android.hardware.input.VirtualMouseScrollEvent event) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                return false;
            }
            return this.mNativeWrapper.writeScrollEvent(inputDeviceDescriptor.getNativePointer(), event.getXAxisMovement(), event.getYAxisMovement(), event.getEventTimeNanos());
        }
    }

    public android.graphics.PointF getCursorPosition(android.os.IBinder token) {
        android.graphics.PointF cursorPosition;
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                throw new java.lang.IllegalArgumentException("Could not get cursor position for input device for given token");
            }
            cursorPosition = ((com.android.server.input.InputManagerInternal) com.android.server.LocalServices.getService(com.android.server.input.InputManagerInternal.class)).getCursorPosition(inputDeviceDescriptor.getDisplayId());
        }
        return cursorPosition;
    }

    boolean sendStylusMotionEvent(android.os.IBinder token, android.hardware.input.VirtualStylusMotionEvent event) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
                    if (inputDeviceDescriptor == null) {
                        return false;
                    }
                    return this.mNativeWrapper.writeStylusMotionEvent(inputDeviceDescriptor.getNativePointer(), event.getToolType(), event.getAction(), event.getX(), event.getY(), event.getPressure(), event.getTiltX(), event.getTiltY(), event.getEventTimeNanos());
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    boolean sendStylusButtonEvent(android.os.IBinder token, android.hardware.input.VirtualStylusButtonEvent event) {
        synchronized (this.mLock) {
            com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.get(token);
            if (inputDeviceDescriptor == null) {
                return false;
            }
            return this.mNativeWrapper.writeStylusButtonEvent(inputDeviceDescriptor.getNativePointer(), event.getButtonCode(), event.getAction(), event.getEventTimeNanos());
        }
    }

    public void dump(java.io.PrintWriter fout) {
        fout.println("    InputController: ");
        synchronized (this.mLock) {
            fout.println("      Active descriptors: ");
            for (int i = 0; i < this.mInputDeviceDescriptors.size(); i++) {
                com.android.server.companion.virtual.InputController.InputDeviceDescriptor inputDeviceDescriptor = this.mInputDeviceDescriptors.valueAt(i);
                fout.println("        ptr: " + inputDeviceDescriptor.getNativePointer());
                fout.println("          displayId: " + inputDeviceDescriptor.getDisplayId());
                fout.println("          creationOrder: " + inputDeviceDescriptor.getCreationOrderNumber());
                fout.println("          type: " + inputDeviceDescriptor.getType());
                fout.println("          phys: " + inputDeviceDescriptor.getPhys());
                fout.println("          inputDeviceId: " + inputDeviceDescriptor.getInputDeviceId());
            }
        }
    }

    void addDeviceForTesting(android.os.IBinder deviceToken, long ptr, int type, int displayId, java.lang.String phys, java.lang.String deviceName, int inputDeviceId) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    this.mInputDeviceDescriptors.put(deviceToken, new com.android.server.companion.virtual.InputController.InputDeviceDescriptor(ptr, new android.os.IBinder.DeathRecipient() { // from class: com.android.server.companion.virtual.InputController$$ExternalSyntheticLambda4
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            com.android.server.companion.virtual.InputController.lambda$addDeviceForTesting$7();
                        }
                    }, type, displayId, phys, deviceName, inputDeviceId));
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    static /* synthetic */ void lambda$addDeviceForTesting$7() {
    }

    java.util.Map<android.os.IBinder, com.android.server.companion.virtual.InputController.InputDeviceDescriptor> getInputDeviceDescriptors() {
        java.util.Map<android.os.IBinder, com.android.server.companion.virtual.InputController.InputDeviceDescriptor> inputDeviceDescriptors = new android.util.ArrayMap<>();
        synchronized (this.mLock) {
            inputDeviceDescriptors.putAll(this.mInputDeviceDescriptors);
        }
        return inputDeviceDescriptors;
    }

    protected static class NativeWrapper {
        protected NativeWrapper() {
        }

        public long openUinputDpad(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys) {
            return com.android.server.companion.virtual.InputController.nativeOpenUinputDpad(deviceName, vendorId, productId, phys);
        }

        public long openUinputKeyboard(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys) {
            return com.android.server.companion.virtual.InputController.nativeOpenUinputKeyboard(deviceName, vendorId, productId, phys);
        }

        public long openUinputMouse(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys) {
            return com.android.server.companion.virtual.InputController.nativeOpenUinputMouse(deviceName, vendorId, productId, phys);
        }

        public long openUinputTouchscreen(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys, int height, int width) {
            return com.android.server.companion.virtual.InputController.nativeOpenUinputTouchscreen(deviceName, vendorId, productId, phys, height, width);
        }

        public long openUinputStylus(java.lang.String deviceName, int vendorId, int productId, java.lang.String phys, int height, int width) {
            return com.android.server.companion.virtual.InputController.nativeOpenUinputStylus(deviceName, vendorId, productId, phys, height, width);
        }

        public void closeUinput(long ptr) {
            com.android.server.companion.virtual.InputController.nativeCloseUinput(ptr);
        }

        public boolean writeDpadKeyEvent(long ptr, int androidKeyCode, int action, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteDpadKeyEvent(ptr, androidKeyCode, action, eventTimeNanos);
        }

        public boolean writeKeyEvent(long ptr, int androidKeyCode, int action, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteKeyEvent(ptr, androidKeyCode, action, eventTimeNanos);
        }

        public boolean writeButtonEvent(long ptr, int buttonCode, int action, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteButtonEvent(ptr, buttonCode, action, eventTimeNanos);
        }

        public boolean writeTouchEvent(long ptr, int pointerId, int toolType, int action, float locationX, float locationY, float pressure, float majorAxisSize, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteTouchEvent(ptr, pointerId, toolType, action, locationX, locationY, pressure, majorAxisSize, eventTimeNanos);
        }

        public boolean writeRelativeEvent(long ptr, float relativeX, float relativeY, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteRelativeEvent(ptr, relativeX, relativeY, eventTimeNanos);
        }

        public boolean writeScrollEvent(long ptr, float xAxisMovement, float yAxisMovement, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteScrollEvent(ptr, xAxisMovement, yAxisMovement, eventTimeNanos);
        }

        public boolean writeStylusMotionEvent(long ptr, int toolType, int action, int locationX, int locationY, int pressure, int tiltX, int tiltY, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteStylusMotionEvent(ptr, toolType, action, locationX, locationY, pressure, tiltX, tiltY, eventTimeNanos);
        }

        public boolean writeStylusButtonEvent(long ptr, int buttonCode, int action, long eventTimeNanos) {
            return com.android.server.companion.virtual.InputController.nativeWriteStylusButtonEvent(ptr, buttonCode, action, eventTimeNanos);
        }
    }

    static final class InputDeviceDescriptor {
        static final int TYPE_DPAD = 4;
        static final int TYPE_KEYBOARD = 1;
        static final int TYPE_MOUSE = 2;
        static final int TYPE_NAVIGATION_TOUCHPAD = 5;
        static final int TYPE_STYLUS = 6;
        static final int TYPE_TOUCHSCREEN = 3;
        private static final java.util.concurrent.atomic.AtomicLong sNextCreationOrderNumber = new java.util.concurrent.atomic.AtomicLong(1);
        private final long mCreationOrderNumber = sNextCreationOrderNumber.getAndIncrement();
        private final android.os.IBinder.DeathRecipient mDeathRecipient;
        private final int mDisplayId;
        private final int mInputDeviceId;
        private final java.lang.String mName;
        private final java.lang.String mPhys;
        private final long mPtr;
        private final int mType;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface Type {
        }

        InputDeviceDescriptor(long ptr, android.os.IBinder.DeathRecipient deathRecipient, int type, int displayId, java.lang.String phys, java.lang.String name, int inputDeviceId) {
            this.mPtr = ptr;
            this.mDeathRecipient = deathRecipient;
            this.mType = type;
            this.mDisplayId = displayId;
            this.mPhys = phys;
            this.mName = name;
            this.mInputDeviceId = inputDeviceId;
        }

        public long getNativePointer() {
            return this.mPtr;
        }

        public int getType() {
            return this.mType;
        }

        public boolean isMouse() {
            return this.mType == 2;
        }

        public android.os.IBinder.DeathRecipient getDeathRecipient() {
            return this.mDeathRecipient;
        }

        public int getDisplayId() {
            return this.mDisplayId;
        }

        public long getCreationOrderNumber() {
            return this.mCreationOrderNumber;
        }

        public java.lang.String getPhys() {
            return this.mPhys;
        }

        public int getInputDeviceId() {
            return this.mInputDeviceId;
        }
    }

    private final class BinderDeathRecipient implements android.os.IBinder.DeathRecipient {
        private final android.os.IBinder mDeviceToken;

        BinderDeathRecipient(android.os.IBinder deviceToken) {
            this.mDeviceToken = deviceToken;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Slog.e(com.android.server.companion.virtual.InputController.TAG, "Virtual input controller binder died");
            com.android.server.companion.virtual.InputController.this.unregisterInputDevice(this.mDeviceToken);
        }
    }

    private class WaitForDevice implements java.lang.AutoCloseable {
        private final java.util.concurrent.CountDownLatch mDeviceAddedLatch = new java.util.concurrent.CountDownLatch(1);
        private int mInputDeviceId = -2;
        private final android.hardware.input.InputManager.InputDeviceListener mListener;

        WaitForDevice(final java.lang.String deviceName, final int vendorId, final int productId, final int associatedDisplayId) {
            this.mListener = new android.hardware.input.InputManager.InputDeviceListener() { // from class: com.android.server.companion.virtual.InputController.WaitForDevice.1
                @Override // android.hardware.input.InputManager.InputDeviceListener
                public void onInputDeviceAdded(int deviceId) {
                    onInputDeviceChanged(deviceId);
                }

                @Override // android.hardware.input.InputManager.InputDeviceListener
                public void onInputDeviceRemoved(int deviceId) {
                }

                @Override // android.hardware.input.InputManager.InputDeviceListener
                public void onInputDeviceChanged(int deviceId) {
                    if (isMatchingDevice(deviceId)) {
                        com.android.server.companion.virtual.InputController.WaitForDevice.this.mInputDeviceId = deviceId;
                        com.android.server.companion.virtual.InputController.WaitForDevice.this.mDeviceAddedLatch.countDown();
                    }
                }

                private boolean isMatchingDevice(int deviceId) {
                    android.view.InputDevice device = android.hardware.input.InputManagerGlobal.getInstance().getInputDevice(deviceId);
                    java.util.Objects.requireNonNull(device, "Newly added input device was null.");
                    if (!device.getName().equals(deviceName)) {
                        return false;
                    }
                    android.hardware.input.InputDeviceIdentifier id = device.getIdentifier();
                    return id.getVendorId() == vendorId && id.getProductId() == productId && device.getAssociatedDisplayId() == associatedDisplayId;
                }
            };
            android.hardware.input.InputManagerGlobal.getInstance().registerInputDeviceListener(this.mListener, com.android.server.companion.virtual.InputController.this.mHandler);
        }

        int waitForDeviceCreation() throws com.android.server.companion.virtual.InputController.DeviceCreationException {
            try {
                if (!this.mDeviceAddedLatch.await(1L, java.util.concurrent.TimeUnit.MINUTES)) {
                    throw new com.android.server.companion.virtual.InputController.DeviceCreationException("Timed out waiting for virtual device to be created.");
                }
                if (this.mInputDeviceId == -2) {
                    throw new java.lang.IllegalStateException("Virtual input device was created with an invalid id=" + this.mInputDeviceId);
                }
                return this.mInputDeviceId;
            } catch (java.lang.InterruptedException e) {
                throw new com.android.server.companion.virtual.InputController.DeviceCreationException("Interrupted while waiting for virtual device to be created.", e);
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            android.hardware.input.InputManagerGlobal.getInstance().unregisterInputDeviceListener(this.mListener);
        }
    }

    static class DeviceCreationException extends java.lang.Exception {
        DeviceCreationException() {
        }

        DeviceCreationException(java.lang.String message) {
            super(message);
        }

        DeviceCreationException(java.lang.String message, java.lang.Throwable cause) {
            super(message, cause);
        }

        DeviceCreationException(java.lang.Throwable cause) {
            super(cause);
        }
    }

    private void createDeviceInternal(int type, java.lang.String deviceName, int vendorId, int productId, android.os.IBinder deviceToken, int displayId, java.lang.String phys, java.util.function.Supplier<java.lang.Long> deviceOpener) throws java.lang.Throwable {
        java.lang.String metricId;
        if (!this.mThreadVerifier.isValidThread()) {
            throw new java.lang.IllegalStateException("Virtual device creation should happen on an auxiliary thread (e.g. binder thread) and not from the handler's thread.");
        }
        validateDeviceName(deviceName);
        setUniqueIdAssociation(displayId, phys);
        try {
            com.android.server.companion.virtual.InputController.WaitForDevice waiter = new com.android.server.companion.virtual.InputController.WaitForDevice(deviceName, vendorId, productId, displayId);
            try {
                long ptr = deviceOpener.get().longValue();
                try {
                    if (ptr == 0) {
                        throw new com.android.server.companion.virtual.InputController.DeviceCreationException("A native error occurred when creating virtual input device: " + deviceName);
                    }
                    try {
                        int inputDeviceId = waiter.waitForDeviceCreation();
                        com.android.server.companion.virtual.InputController.BinderDeathRecipient binderDeathRecipient = new com.android.server.companion.virtual.InputController.BinderDeathRecipient(deviceToken);
                        try {
                            deviceToken.linkToDeath(binderDeathRecipient, 0);
                            waiter.close();
                            synchronized (this.mLock) {
                                try {
                                    try {
                                        this.mInputDeviceDescriptors.put(deviceToken, new com.android.server.companion.virtual.InputController.InputDeviceDescriptor(ptr, binderDeathRecipient, type, displayId, phys, deviceName, inputDeviceId));
                                        if (android.companion.virtualdevice.flags.Flags.metricsCollection() && (metricId = getMetricIdForInputType(type)) != null) {
                                            com.android.modules.expresslog.Counter.logIncrementWithUid(metricId, this.mAttributionSource.getUid());
                                            return;
                                        }
                                        return;
                                    } catch (java.lang.Throwable th) {
                                        th = th;
                                        throw th;
                                    }
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                }
                            }
                        } catch (android.os.RemoteException e) {
                            try {
                                throw new com.android.server.companion.virtual.InputController.DeviceCreationException("Client died before virtual device could be created.", e);
                            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e2) {
                                e = e2;
                                this.mNativeWrapper.closeUinput(ptr);
                                throw e;
                            }
                        }
                    } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e3) {
                        e = e3;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
            java.lang.Throwable th5 = th;
            try {
                try {
                    waiter.close();
                    throw th5;
                } catch (java.lang.Throwable th6) {
                    th5.addSuppressed(th6);
                    throw th5;
                }
            } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e4) {
                e = e4;
                android.hardware.input.InputManagerGlobal.getInstance().removeUniqueIdAssociationByPort(phys);
                throw e;
            }
        } catch (com.android.server.companion.virtual.InputController.DeviceCreationException e5) {
            e = e5;
        }
    }

    private static java.lang.String getMetricIdForInputType(int type) {
        switch (type) {
            case 1:
                return "virtual_devices.value_virtual_keyboard_created_count";
            case 2:
                return "virtual_devices.value_virtual_mouse_created_count";
            case 3:
                return "virtual_devices.value_virtual_touchscreen_created_count";
            case 4:
                return "virtual_devices.value_virtual_dpad_created_count";
            case 5:
                return "virtual_devices.value_virtual_navigationtouchpad_created_count";
            case 6:
                return "virtual_devices.value_virtual_stylus_created_count";
            default:
                android.util.Log.e(TAG, "No metric known for input type: " + type);
                return null;
        }
    }
}
