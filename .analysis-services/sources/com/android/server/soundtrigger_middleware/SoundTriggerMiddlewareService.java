package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class SoundTriggerMiddlewareService extends android.media.soundtrigger_middleware.ISoundTriggerMiddlewareService.Stub {
    private static final java.lang.String TAG = "SoundTriggerMiddlewareService";
    private final android.content.Context mContext;
    private final com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal mDelegate;
    private final com.android.server.soundtrigger_middleware.SoundTriggerInjection mInjection;

    private SoundTriggerMiddlewareService(com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal delegate, android.content.Context context, com.android.server.soundtrigger_middleware.SoundTriggerInjection injection) {
        this.mDelegate = (com.android.server.soundtrigger_middleware.ISoundTriggerMiddlewareInternal) java.util.Objects.requireNonNull(delegate);
        this.mContext = context;
        this.mInjection = injection;
    }

    public android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] listModulesAsOriginator(android.media.permission.Identity identity) {
        android.media.permission.SafeCloseable ignored = establishIdentityDirect(identity);
        try {
            android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] soundTriggerModuleDescriptorArrListModules = this.mDelegate.listModules();
            if (ignored != null) {
                ignored.close();
            }
            return soundTriggerModuleDescriptorArrListModules;
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] listModulesAsMiddleman(android.media.permission.Identity middlemanIdentity, android.media.permission.Identity originatorIdentity) {
        android.media.permission.SafeCloseable ignored = establishIdentityIndirect(middlemanIdentity, originatorIdentity);
        try {
            android.media.soundtrigger_middleware.SoundTriggerModuleDescriptor[] soundTriggerModuleDescriptorArrListModules = this.mDelegate.listModules();
            if (ignored != null) {
                ignored.close();
            }
            return soundTriggerModuleDescriptorArrListModules;
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public android.media.soundtrigger_middleware.ISoundTriggerModule attachAsOriginator(int handle, android.media.permission.Identity identity, android.media.soundtrigger_middleware.ISoundTriggerCallback callback) {
        android.media.permission.SafeCloseable ignored = establishIdentityDirect((android.media.permission.Identity) java.util.Objects.requireNonNull(identity));
        try {
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareService.ModuleService moduleService = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareService.ModuleService(this.mDelegate.attach(handle, callback, false));
            if (ignored != null) {
                ignored.close();
            }
            return moduleService;
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public android.media.soundtrigger_middleware.ISoundTriggerModule attachAsMiddleman(int handle, android.media.permission.Identity middlemanIdentity, android.media.permission.Identity originatorIdentity, android.media.soundtrigger_middleware.ISoundTriggerCallback callback, boolean isTrusted) {
        android.media.permission.SafeCloseable ignored = establishIdentityIndirect((android.media.permission.Identity) java.util.Objects.requireNonNull(middlemanIdentity), (android.media.permission.Identity) java.util.Objects.requireNonNull(originatorIdentity));
        try {
            com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareService.ModuleService moduleService = new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareService.ModuleService(this.mDelegate.attach(handle, callback, isTrusted));
            if (ignored != null) {
                ignored.close();
            }
            return moduleService;
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public void attachFakeHalInjection(android.media.soundtrigger_middleware.ISoundTriggerInjection injection) {
        android.content.PermissionChecker.checkCallingOrSelfPermissionForPreflight(this.mContext, "android.permission.MANAGE_SOUND_TRIGGER");
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            this.mInjection.registerClient((android.media.soundtrigger_middleware.ISoundTriggerInjection) java.util.Objects.requireNonNull(injection));
            if (ignored != null) {
                ignored.close();
            }
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        if (this.mDelegate instanceof com.android.server.soundtrigger_middleware.Dumpable) {
            ((com.android.server.soundtrigger_middleware.Dumpable) this.mDelegate).dump(fout);
        }
    }

    private android.media.permission.SafeCloseable establishIdentityIndirect(android.media.permission.Identity middlemanIdentity, android.media.permission.Identity originatorIdentity) {
        return android.media.permission.PermissionUtil.establishIdentityIndirect(this.mContext, "android.permission.SOUNDTRIGGER_DELEGATE_IDENTITY", middlemanIdentity, originatorIdentity);
    }

    private android.media.permission.SafeCloseable establishIdentityDirect(android.media.permission.Identity originatorIdentity) {
        return android.media.permission.PermissionUtil.establishIdentityDirect(originatorIdentity);
    }

    private static final class ModuleService extends android.media.soundtrigger_middleware.ISoundTriggerModule.Stub {
        private final android.media.soundtrigger_middleware.ISoundTriggerModule mDelegate;

        private ModuleService(android.media.soundtrigger_middleware.ISoundTriggerModule delegate) {
            this.mDelegate = delegate;
        }

        public int loadModel(android.media.soundtrigger.SoundModel model) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                int iLoadModel = this.mDelegate.loadModel(model);
                if (ignored != null) {
                    ignored.close();
                }
                return iLoadModel;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int loadPhraseModel(android.media.soundtrigger.PhraseSoundModel model) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                int iLoadPhraseModel = this.mDelegate.loadPhraseModel(model);
                if (ignored != null) {
                    ignored.close();
                }
                return iLoadPhraseModel;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void unloadModel(int modelHandle) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                this.mDelegate.unloadModel(modelHandle);
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public android.os.IBinder startRecognition(int modelHandle, android.media.soundtrigger.RecognitionConfig config) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                android.os.IBinder iBinderStartRecognition = this.mDelegate.startRecognition(modelHandle, config);
                if (ignored != null) {
                    ignored.close();
                }
                return iBinderStartRecognition;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void stopRecognition(int modelHandle) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                this.mDelegate.stopRecognition(modelHandle);
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void forceRecognitionEvent(int modelHandle) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                this.mDelegate.forceRecognitionEvent(modelHandle);
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void setModelParameter(int modelHandle, int modelParam, int value) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                this.mDelegate.setModelParameter(modelHandle, modelParam, value);
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public int getModelParameter(int modelHandle, int modelParam) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                int modelParameter = this.mDelegate.getModelParameter(modelHandle, modelParam);
                if (ignored != null) {
                    ignored.close();
                }
                return modelParameter;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public android.media.soundtrigger.ModelParameterRange queryModelParameterSupport(int modelHandle, int modelParam) throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                android.media.soundtrigger.ModelParameterRange modelParameterRangeQueryModelParameterSupport = this.mDelegate.queryModelParameterSupport(modelHandle, modelParam);
                if (ignored != null) {
                    ignored.close();
                }
                return modelParameterRangeQueryModelParameterSupport;
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void detach() throws android.os.RemoteException {
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                this.mDelegate.detach();
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    public static final class Lifecycle extends com.android.server.SystemService {
        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            com.android.server.soundtrigger_middleware.SoundTriggerInjection injection = new com.android.server.soundtrigger_middleware.SoundTriggerInjection();
            com.android.server.soundtrigger_middleware.HalFactory[] factories = {new com.android.server.soundtrigger_middleware.DefaultHalFactory(), new com.android.server.soundtrigger_middleware.FakeHalFactory(injection)};
            publishBinderService("soundtrigger_middleware", new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareService(new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareLogging(getContext(), new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewarePermission(new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareValidation(new com.android.server.soundtrigger_middleware.SoundTriggerMiddlewareImpl(factories, new com.android.server.soundtrigger_middleware.AudioSessionProviderImpl())), getContext())), getContext(), injection));
        }
    }
}
