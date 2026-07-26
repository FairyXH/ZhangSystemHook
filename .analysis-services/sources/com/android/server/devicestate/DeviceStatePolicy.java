package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
public abstract class DeviceStatePolicy implements android.util.Dumpable {
    protected final android.content.Context mContext;

    public abstract void configureDeviceForState(int i, java.lang.Runnable runnable);

    public abstract com.android.server.devicestate.DeviceStateProvider getDeviceStateProvider();

    protected DeviceStatePolicy(android.content.Context context) {
        this.mContext = context;
    }

    static final class DefaultProvider implements com.android.server.devicestate.DeviceStatePolicy.Provider {
        DefaultProvider() {
        }

        @Override // com.android.server.devicestate.DeviceStatePolicy.Provider
        public com.android.server.devicestate.DeviceStatePolicy instantiate(android.content.Context context) {
            return new com.android.server.policy.DeviceStatePolicyImpl(context);
        }
    }

    public interface Provider {
        com.android.server.devicestate.DeviceStatePolicy instantiate(android.content.Context context);

        static com.android.server.devicestate.DeviceStatePolicy.Provider fromResources(android.content.res.Resources res) {
            java.lang.String name = res.getString(android.R.string.config_dozeUdfpsLongPressSensorType);
            if (android.text.TextUtils.isEmpty(name)) {
                return new com.android.server.devicestate.DeviceStatePolicy.DefaultProvider();
            }
            try {
                return (com.android.server.devicestate.DeviceStatePolicy.Provider) java.lang.Class.forName(name).newInstance();
            } catch (java.lang.ClassCastException | java.lang.ReflectiveOperationException e) {
                throw new java.lang.IllegalStateException("Couldn't instantiate class " + name + " for config_deviceSpecificDeviceStatePolicyProvider: make sure it has a public zero-argument constructor and implements DeviceStatePolicy.Provider", e);
            }
        }
    }
}
