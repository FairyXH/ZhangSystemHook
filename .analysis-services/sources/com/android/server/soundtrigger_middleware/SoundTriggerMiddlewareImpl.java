package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerMiddlewareImpl implements com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal {
    private static final java.lang.String TAG = "SoundTriggerMiddlewareImpl";
    private final com.android.server.soundtrigger_middleware.SoundTriggerModule[] mModules;

    public static abstract class AudioSessionProvider {
        public abstract com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider.AudioSession acquireSession();

        public abstract void releaseSession(int i);

        public static final class AudioSession {
            final int mDeviceHandle;
            final int mIoHandle;
            final int mSessionHandle;

            AudioSession(int sessionHandle, int ioHandle, int deviceHandle) {
                this.mSessionHandle = sessionHandle;
                this.mIoHandle = ioHandle;
                this.mDeviceHandle = deviceHandle;
            }
        }
    }

    public SoundTriggerMiddlewareImpl(com.android.server.soundtrigger_middleware.HalFactory[] halFactories, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider audioSessionProvider) {
        java.util.List<com.android.server.soundtrigger_middleware.SoundTriggerModule> modules = new java.util.ArrayList<>(halFactories.length);
        for (com.android.server.soundtrigger_middleware.HalFactory halFactory : halFactories) {
            try {
                modules.add(new com.android.server.soundtrigger_middleware.SoundTriggerModule(halFactory, audioSessionProvider));
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to add a SoundTriggerModule instance", e);
            }
        }
        this.mModules = (com.android.server.soundtrigger_middleware.SoundTriggerModule[]) modules.toArray(new com.android.server.soundtrigger_middleware.SoundTriggerModule[0]);
    }

    public SoundTriggerMiddlewareImpl(com.android.server.soundtrigger_middleware.HalFactory factory, com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl.AudioSessionProvider audioSessionProvider) {
        this(new com.android.server.soundtrigger_middleware.HalFactory[]{factory}, audioSessionProvider);
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] listModules() {
        android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] result = new android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[this.mModules.length];
        for (int i = 0; i < this.mModules.length; i++) {
            android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor desc = new android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor();
            desc.handle = i;
            desc.properties = this.mModules[i].getProperties();
            result[i] = desc;
        }
        return result;
    }

    @Override // com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal
    public android.media.soundtrigger_middleware.ISoundTriggerModule attach(int handle, android.media.soundtrigger_middleware.ISoundTriggerCallback callback, boolean isTrusted) {
        return this.mModules[handle].attach(callback);
    }
}
