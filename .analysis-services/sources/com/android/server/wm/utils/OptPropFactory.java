package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class OptPropFactory {
    private final android.content.pm.PackageManager mPackageManager;
    private final java.lang.String mPackageName;

    /* JADX INFO: Access modifiers changed from: private */
    @java.lang.FunctionalInterface
    interface ThrowableBooleanSupplier {
        boolean get() throws java.lang.Exception;
    }

    public OptPropFactory(android.content.pm.PackageManager packageManager, java.lang.String packageName) {
        this.mPackageManager = packageManager;
        this.mPackageName = packageName;
    }

    public com.android.server.wm.utils.OptPropFactory.OptProp create(final java.lang.String propertyName) {
        return com.android.server.wm.utils.OptPropFactory.OptProp.create(new com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier() { // from class: com.android.server.wm.utils.OptPropFactory$$ExternalSyntheticLambda0
            @Override // com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier
            public final boolean get() {
                return this.f$0.lambda$create$0(propertyName);
            }
        }, propertyName);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$create$0(java.lang.String propertyName) throws java.lang.Exception {
        return this.mPackageManager.getProperty(propertyName, this.mPackageName).getBoolean();
    }

    public com.android.server.wm.utils.OptPropFactory.OptProp create(final java.lang.String propertyName, java.util.function.BooleanSupplier gateCondition) {
        return com.android.server.wm.utils.OptPropFactory.OptProp.create(new com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier() { // from class: com.android.server.wm.utils.OptPropFactory$$ExternalSyntheticLambda1
            @Override // com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier
            public final boolean get() {
                return this.f$0.lambda$create$1(propertyName);
            }
        }, propertyName, gateCondition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$create$1(java.lang.String propertyName) throws java.lang.Exception {
        return this.mPackageManager.getProperty(propertyName, this.mPackageName).getBoolean();
    }

    public static class OptProp {
        private static final java.lang.String TAG = "OptProp";
        private static final int VALUE_FALSE = 0;
        private static final int VALUE_TRUE = 1;
        private static final int VALUE_UNDEFINED = -1;
        private static final int VALUE_UNSET = -2;
        private final java.util.function.BooleanSupplier mCondition;
        private final java.lang.String mPropertyName;
        private int mValue = -1;
        private final com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier mValueSupplier;

        @interface OptionalValue {
        }

        private OptProp(com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier valueSupplier, java.lang.String propertyName, java.util.function.BooleanSupplier condition) {
            this.mValueSupplier = valueSupplier;
            this.mPropertyName = propertyName;
            this.mCondition = condition;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.wm.utils.OptPropFactory.OptProp create(com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier valueSupplier, java.lang.String propertyName) {
            return new com.android.server.wm.utils.OptPropFactory.OptProp(valueSupplier, propertyName, new java.util.function.BooleanSupplier() { // from class: com.android.server.wm.utils.OptPropFactory$OptProp$$ExternalSyntheticLambda0
                @Override // java.util.function.BooleanSupplier
                public final boolean getAsBoolean() {
                    return com.android.server.wm.utils.OptPropFactory.OptProp.lambda$create$0();
                }
            });
        }

        static /* synthetic */ boolean lambda$create$0() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static com.android.server.wm.utils.OptPropFactory.OptProp create(com.android.server.wm.utils.OptPropFactory.ThrowableBooleanSupplier valueSupplier, java.lang.String propertyName, java.util.function.BooleanSupplier condition) {
            return new com.android.server.wm.utils.OptPropFactory.OptProp(valueSupplier, propertyName, condition);
        }

        public boolean isTrue() {
            return this.mCondition.getAsBoolean() && getValue() == 1;
        }

        public boolean isFalse() {
            return this.mCondition.getAsBoolean() && getValue() == 0;
        }

        public boolean shouldEnableWithOverrideAndProperty(boolean overrideValue) {
            if (this.mCondition.getAsBoolean() && getValue() != 0) {
                return getValue() == 1 || overrideValue;
            }
            return false;
        }

        public boolean shouldEnableWithOptInOverrideAndOptOutProperty(boolean overrideValue) {
            return this.mCondition.getAsBoolean() && getValue() != 0 && overrideValue;
        }

        public boolean shouldEnableWithOptOutOverrideAndProperty(boolean overrideValue) {
            return (!this.mCondition.getAsBoolean() || getValue() == 0 || overrideValue) ? false : true;
        }

        private int getValue() {
            if (this.mValue == -1) {
                try {
                    java.lang.Boolean value = java.lang.Boolean.valueOf(this.mValueSupplier.get());
                    if (java.lang.Boolean.TRUE.equals(value)) {
                        this.mValue = 1;
                    } else if (java.lang.Boolean.FALSE.equals(value)) {
                        this.mValue = 0;
                    } else {
                        this.mValue = -2;
                    }
                } catch (java.lang.Exception e) {
                    com.android.server.wm.utils.LogUtil.sDebugE(TAG, "Cannot read opt property " + this.mPropertyName);
                    this.mValue = -2;
                }
            }
            return this.mValue;
        }
    }
}
