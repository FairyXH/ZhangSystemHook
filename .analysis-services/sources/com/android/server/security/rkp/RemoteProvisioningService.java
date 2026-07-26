package com.android.server.security.rkp;

/* JADX INFO: loaded from: classes3.dex */
public class RemoteProvisioningService extends com.android.server.SystemService {
    private static final java.time.Duration CREATE_REGISTRATION_TIMEOUT = java.time.Duration.ofSeconds(10);
    public static final java.lang.String TAG = "RemoteProvisionSysSvc";
    private final com.android.server.security.rkp.RemoteProvisioningService.RemoteProvisioningImpl mBinderImpl;

    private static class RegistrationReceiver implements android.os.OutcomeReceiver<android.security.rkp.service.RegistrationProxy, java.lang.Exception> {
        private final android.security.rkp.IGetRegistrationCallback mCallback;
        private final java.util.concurrent.Executor mExecutor;

        RegistrationReceiver(java.util.concurrent.Executor executor, android.security.rkp.IGetRegistrationCallback callback) {
            this.mExecutor = executor;
            this.mCallback = callback;
        }

        @Override // android.os.OutcomeReceiver
        public void onResult(android.security.rkp.service.RegistrationProxy registration) {
            try {
                this.mCallback.onSuccess(new com.android.server.security.rkp.RemoteProvisioningRegistration(registration, this.mExecutor));
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.security.rkp.RemoteProvisioningService.TAG, "Error calling success callback " + this.mCallback.asBinder().hashCode(), e);
            }
        }

        @Override // android.os.OutcomeReceiver
        public void onError(java.lang.Exception error) {
            try {
                this.mCallback.onError(error.toString());
            } catch (android.os.RemoteException e) {
                android.util.Log.e(com.android.server.security.rkp.RemoteProvisioningService.TAG, "Error calling error callback " + this.mCallback.asBinder().hashCode(), e);
            }
        }
    }

    public RemoteProvisioningService(android.content.Context context) {
        super(context);
        this.mBinderImpl = new com.android.server.security.rkp.RemoteProvisioningService.RemoteProvisioningImpl();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("remote_provisioning", this.mBinderImpl);
    }

    private final class RemoteProvisioningImpl extends android.security.rkp.IRemoteProvisioning.Stub {
        private RemoteProvisioningImpl() {
        }

        public void getRegistration(java.lang.String irpcName, android.security.rkp.IGetRegistrationCallback callback) throws android.os.RemoteException {
            int callerUid = android.os.Binder.getCallingUidOrThrow();
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            java.util.concurrent.Executor executor = com.android.server.security.rkp.RemoteProvisioningService.this.getContext().getMainExecutor();
            try {
                android.util.Log.i(com.android.server.security.rkp.RemoteProvisioningService.TAG, "getRegistration(" + irpcName + ")");
                android.security.rkp.service.RegistrationProxy.createAsync(com.android.server.security.rkp.RemoteProvisioningService.this.getContext(), callerUid, irpcName, com.android.server.security.rkp.RemoteProvisioningService.CREATE_REGISTRATION_TIMEOUT, executor, new com.android.server.security.rkp.RemoteProvisioningService.RegistrationReceiver(executor, callback));
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }

        protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.security.rkp.RemoteProvisioningService.this.getContext(), com.android.server.security.rkp.RemoteProvisioningService.TAG, pw)) {
                int callerUid = android.os.Binder.getCallingUidOrThrow();
                long callingIdentity = android.os.Binder.clearCallingIdentity();
                try {
                    new com.android.server.security.rkp.RemoteProvisioningShellCommand(com.android.server.security.rkp.RemoteProvisioningService.this.getContext(), callerUid).dump(pw);
                } finally {
                    android.os.Binder.restoreCallingIdentity(callingIdentity);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
            int callerUid = android.os.Binder.getCallingUidOrThrow();
            long callingIdentity = android.os.Binder.clearCallingIdentity();
            try {
                return new com.android.server.security.rkp.RemoteProvisioningShellCommand(com.android.server.security.rkp.RemoteProvisioningService.this.getContext(), callerUid).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
            } finally {
                android.os.Binder.restoreCallingIdentity(callingIdentity);
            }
        }
    }
}
