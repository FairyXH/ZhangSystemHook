package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class FieldClassificationStrategy {
    private static final java.lang.String TAG = "FieldClassificationStrategy";
    private final android.content.Context mContext;
    private final java.lang.Object mLock = new java.lang.Object();
    private java.util.ArrayList<com.android.server.autofill.FieldClassificationStrategy.Command> mQueuedCommands;
    private android.service.autofill.IAutofillFieldClassificationService mRemoteService;
    private android.content.ServiceConnection mServiceConnection;
    private final int mUserId;

    /* JADX INFO: Access modifiers changed from: private */
    interface Command {
        void run(android.service.autofill.IAutofillFieldClassificationService iAutofillFieldClassificationService) throws android.os.RemoteException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    interface MetadataParser<T> {
        T get(android.content.res.Resources resources, int i);
    }

    public FieldClassificationStrategy(android.content.Context context, int userId) {
        this.mContext = context;
        this.mUserId = userId;
    }

    android.content.pm.ServiceInfo getServiceInfo() {
        java.lang.String packageName = this.mContext.getPackageManager().getServicesSystemSharedLibraryPackageName();
        if (packageName == null) {
            android.util.Slog.w(TAG, "no external services package!");
            return null;
        }
        android.content.Intent intent = new android.content.Intent("android.service.autofill.AutofillFieldClassificationService");
        intent.setPackage(packageName);
        android.content.pm.ResolveInfo resolveInfo = this.mContext.getPackageManager().resolveService(intent, 132);
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            android.util.Slog.w(TAG, "No valid components found.");
            return null;
        }
        return resolveInfo.serviceInfo;
    }

    private android.content.ComponentName getServiceComponentName() {
        android.content.pm.ServiceInfo serviceInfo = getServiceInfo();
        if (serviceInfo == null) {
            return null;
        }
        android.content.ComponentName name = new android.content.ComponentName(serviceInfo.packageName, serviceInfo.name);
        if (!"android.permission.BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE".equals(serviceInfo.permission)) {
            android.util.Slog.w(TAG, name.flattenToShortString() + " does not require permission android.permission.BIND_AUTOFILL_FIELD_CLASSIFICATION_SERVICE");
            return null;
        }
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "getServiceComponentName(): " + name);
        }
        return name;
    }

    void reset() {
        synchronized (this.mLock) {
            if (this.mServiceConnection != null) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "reset(): unbinding service.");
                }
                try {
                    this.mContext.unbindService(this.mServiceConnection);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.w(TAG, "reset(): " + e.getMessage());
                }
                this.mServiceConnection = null;
            } else if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "reset(): service is not bound. Do nothing.");
            }
        }
    }

    private void connectAndRun(com.android.server.autofill.FieldClassificationStrategy.Command command) {
        synchronized (this.mLock) {
            if (this.mRemoteService != null) {
                try {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(TAG, "running command right away");
                    }
                    command.run(this.mRemoteService);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "exception calling service: " + e);
                }
                return;
            }
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "service is null; queuing command");
            }
            if (this.mQueuedCommands == null) {
                this.mQueuedCommands = new java.util.ArrayList<>(1);
            }
            this.mQueuedCommands.add(command);
            if (this.mServiceConnection != null) {
                return;
            }
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "creating connection");
            }
            this.mServiceConnection = new android.content.ServiceConnection() { // from class: com.android.server.autofill.FieldClassificationStrategy.1
                @Override // android.content.ServiceConnection
                public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(com.android.server.autofill.FieldClassificationStrategy.TAG, "onServiceConnected(): " + name);
                    }
                    synchronized (com.android.server.autofill.FieldClassificationStrategy.this.mLock) {
                        com.android.server.autofill.FieldClassificationStrategy.this.mRemoteService = android.service.autofill.IAutofillFieldClassificationService.Stub.asInterface(service);
                        if (com.android.server.autofill.FieldClassificationStrategy.this.mQueuedCommands != null) {
                            int size = com.android.server.autofill.FieldClassificationStrategy.this.mQueuedCommands.size();
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(com.android.server.autofill.FieldClassificationStrategy.TAG, "running " + size + " queued commands");
                            }
                            for (int i = 0; i < size; i++) {
                                com.android.server.autofill.FieldClassificationStrategy.Command queuedCommand = (com.android.server.autofill.FieldClassificationStrategy.Command) com.android.server.autofill.FieldClassificationStrategy.this.mQueuedCommands.get(i);
                                try {
                                    if (com.android.server.autofill.Helper.sVerbose) {
                                        android.util.Slog.v(com.android.server.autofill.FieldClassificationStrategy.TAG, "running queued command #" + i);
                                    }
                                    queuedCommand.run(com.android.server.autofill.FieldClassificationStrategy.this.mRemoteService);
                                } catch (android.os.RemoteException e2) {
                                    android.util.Slog.w(com.android.server.autofill.FieldClassificationStrategy.TAG, "exception calling " + name + ": " + e2);
                                }
                            }
                            com.android.server.autofill.FieldClassificationStrategy.this.mQueuedCommands = null;
                        } else if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(com.android.server.autofill.FieldClassificationStrategy.TAG, "no queued commands");
                        }
                    }
                }

                @Override // android.content.ServiceConnection
                public void onServiceDisconnected(android.content.ComponentName name) {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(com.android.server.autofill.FieldClassificationStrategy.TAG, "onServiceDisconnected(): " + name);
                    }
                    synchronized (com.android.server.autofill.FieldClassificationStrategy.this.mLock) {
                        com.android.server.autofill.FieldClassificationStrategy.this.mRemoteService = null;
                    }
                }

                @Override // android.content.ServiceConnection
                public void onBindingDied(android.content.ComponentName name) {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(com.android.server.autofill.FieldClassificationStrategy.TAG, "onBindingDied(): " + name);
                    }
                    synchronized (com.android.server.autofill.FieldClassificationStrategy.this.mLock) {
                        com.android.server.autofill.FieldClassificationStrategy.this.mRemoteService = null;
                    }
                }

                @Override // android.content.ServiceConnection
                public void onNullBinding(android.content.ComponentName name) {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(com.android.server.autofill.FieldClassificationStrategy.TAG, "onNullBinding(): " + name);
                    }
                    synchronized (com.android.server.autofill.FieldClassificationStrategy.this.mLock) {
                        com.android.server.autofill.FieldClassificationStrategy.this.mRemoteService = null;
                    }
                }
            };
            android.content.ComponentName component = getServiceComponentName();
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "binding to: " + component);
            }
            if (component != null) {
                android.content.Intent intent = new android.content.Intent();
                intent.setComponent(component);
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    this.mContext.bindServiceAsUser(intent, this.mServiceConnection, 1, android.os.UserHandle.of(this.mUserId));
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(TAG, "bound");
                    }
                    android.os.Binder.restoreCallingIdentity(token);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
            return;
        }
    }

    java.lang.String[] getAvailableAlgorithms() {
        return (java.lang.String[]) getMetadataValue("android.autofill.field_classification.available_algorithms", new com.android.server.autofill.FieldClassificationStrategy.MetadataParser() { // from class: com.android.server.autofill.FieldClassificationStrategy$$ExternalSyntheticLambda0
            @Override // com.android.server.autofill.FieldClassificationStrategy.MetadataParser
            public final java.lang.Object get(android.content.res.Resources resources, int i) {
                return resources.getStringArray(i);
            }
        });
    }

    java.lang.String getDefaultAlgorithm() {
        return (java.lang.String) getMetadataValue("android.autofill.field_classification.default_algorithm", new com.android.server.autofill.FieldClassificationStrategy.MetadataParser() { // from class: com.android.server.autofill.FieldClassificationStrategy$$ExternalSyntheticLambda1
            @Override // com.android.server.autofill.FieldClassificationStrategy.MetadataParser
            public final java.lang.Object get(android.content.res.Resources resources, int i) {
                return resources.getString(i);
            }
        });
    }

    private <T> T getMetadataValue(java.lang.String field, com.android.server.autofill.FieldClassificationStrategy.MetadataParser<T> parser) {
        android.content.pm.ServiceInfo serviceInfo = getServiceInfo();
        if (serviceInfo == null) {
            return null;
        }
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        try {
            android.content.res.Resources res = pm.getResourcesForApplication(serviceInfo.applicationInfo);
            int resourceId = serviceInfo.metaData.getInt(field);
            return parser.get(res, resourceId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Log.e(TAG, "Error getting application resources for " + serviceInfo, e);
            return null;
        }
    }

    void calculateScores(final android.os.RemoteCallback callback, final java.util.List<android.view.autofill.AutofillValue> actualValues, final java.lang.String[] userDataValues, final java.lang.String[] categoryIds, final java.lang.String defaultAlgorithm, final android.os.Bundle defaultArgs, final android.util.ArrayMap<java.lang.String, java.lang.String> algorithms, final android.util.ArrayMap<java.lang.String, android.os.Bundle> args) {
        connectAndRun(new com.android.server.autofill.FieldClassificationStrategy.Command() { // from class: com.android.server.autofill.FieldClassificationStrategy$$ExternalSyntheticLambda2
            @Override // com.android.server.autofill.FieldClassificationStrategy.Command
            public final void run(android.service.autofill.IAutofillFieldClassificationService iAutofillFieldClassificationService) {
                iAutofillFieldClassificationService.calculateScores(callback, actualValues, userDataValues, categoryIds, defaultAlgorithm, defaultArgs, algorithms, args);
            }
        });
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        android.content.ComponentName impl = getServiceComponentName();
        pw.print(prefix);
        pw.print("User ID: ");
        pw.println(this.mUserId);
        pw.print(prefix);
        pw.print("Queued commands: ");
        if (this.mQueuedCommands == null) {
            pw.println("N/A");
        } else {
            pw.println(this.mQueuedCommands.size());
        }
        pw.print(prefix);
        pw.print("Implementation: ");
        if (impl == null) {
            pw.println("N/A");
            return;
        }
        pw.println(impl.flattenToShortString());
        try {
            pw.print(prefix);
            pw.print("Available algorithms: ");
            pw.println(java.util.Arrays.toString(getAvailableAlgorithms()));
            pw.print(prefix);
            pw.print("Default algorithm: ");
            pw.println(getDefaultAlgorithm());
        } catch (java.lang.Exception e) {
            pw.print("ERROR CALLING SERVICE: ");
            pw.println(e);
        }
    }
}
