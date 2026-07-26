package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class UserDataPreparer {
    private static final java.lang.String TAG = "UserDataPreparer";
    private static final java.lang.String XATTR_SERIAL = "user.serial";
    private final android.content.Context mContext;
    private final com.android.server.pm.PackageManagerTracedLock mInstallLock;
    private final com.android.server.pm.Installer mInstaller;
    com.android.server.pm.IUserDataPreparerExt userDataExt = (com.android.server.pm.IUserDataPreparerExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IUserDataPreparerExt.class).create();

    UserDataPreparer(com.android.server.pm.Installer installer, com.android.server.pm.PackageManagerTracedLock installLock, android.content.Context context) {
        this.mInstallLock = installLock;
        this.mContext = context;
        this.mInstaller = installer;
    }

    void prepareUserData(android.content.pm.UserInfo userInfo, int flags) {
        if (this.userDataExt != null) {
            this.userDataExt.prepareUserData(userInfo.id, userInfo.serialNumber, flags);
        }
        com.android.server.pm.PackageManagerTracedLock installLock = this.mInstallLock.acquireLock();
        try {
            android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
            prepareUserDataLI(null, userInfo, flags, true);
            for (android.os.storage.VolumeInfo vol : storage.getWritablePrivateVolumes()) {
                java.lang.String volumeUuid = vol.getFsUuid();
                if (volumeUuid != null) {
                    prepareUserDataLI(volumeUuid, userInfo, flags, true);
                }
            }
            if (installLock != null) {
                installLock.close();
            }
        } catch (java.lang.Throwable th) {
            if (installLock != null) {
                try {
                    installLock.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void prepareUserDataLI(java.lang.String volumeUuid, android.content.pm.UserInfo userInfo, int flags, boolean allowRecover) {
        int userId = userInfo.id;
        int userSerial = userInfo.serialNumber;
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
        boolean isNewUser = userInfo.lastLoggedInTime == 0;
        com.android.server.utils.Slogf.d(TAG, "Preparing user data; volumeUuid=%s, userId=%d, flags=0x%x, isNewUser=%s", volumeUuid, java.lang.Integer.valueOf(userId), java.lang.Integer.valueOf(flags), java.lang.Boolean.valueOf(isNewUser));
        try {
            storage.prepareUserStorage(volumeUuid, userId, flags);
            if ((flags & 1) != 0) {
                enforceSerialNumber(getDataUserDeDirectory(volumeUuid, userId), userSerial);
                if (java.util.Objects.equals(volumeUuid, android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL)) {
                    enforceSerialNumber(getDataSystemDeDirectory(userId), userSerial);
                }
            }
            if ((flags & 2) != 0) {
                enforceSerialNumber(getDataUserCeDirectory(volumeUuid, userId), userSerial);
                if (java.util.Objects.equals(volumeUuid, android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL)) {
                    enforceSerialNumber(getDataSystemCeDirectory(userId), userSerial);
                }
            }
            this.mInstaller.createUserData(volumeUuid, userId, userSerial, flags);
            if ((flags & 2) != 0 && userId == 0) {
                java.lang.String propertyName = "sys.user." + userId + ".ce_available";
                android.util.Slog.d(TAG, "Setting property: " + propertyName + "=true");
                android.os.SystemProperties.set(propertyName, "true");
            }
        } catch (java.lang.Exception e) {
            if (isNewUser) {
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(6, "Destroying user " + userId + " on volume " + volumeUuid + " because we failed to prepare: " + e);
                destroyUserDataLI(volumeUuid, userId, flags);
            } else {
                com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(6, "Failed to prepare user " + userId + " on volume " + volumeUuid + ": " + e);
            }
            if (!allowRecover) {
                try {
                    android.util.Log.e(TAG, "prepareUserData failed for user " + userId, e);
                    if (isNewUser && userId == 0 && volumeUuid == null) {
                        android.os.RecoverySystem.rebootPromptAndWipeUserData(this.mContext, "failed to prepare internal storage for system user");
                        return;
                    }
                    return;
                } catch (java.io.IOException e2) {
                    throw new java.lang.RuntimeException("error rebooting into recovery", e2);
                }
            }
            prepareUserDataLI(volumeUuid, userInfo, flags | 1, false);
        }
    }

    void destroyUserData(int userId, int flags) {
        if (this.userDataExt != null) {
            this.userDataExt.destroyUserData(userId, flags);
        }
        com.android.server.pm.PackageManagerTracedLock installLock = this.mInstallLock.acquireLock();
        try {
            android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
            for (android.os.storage.VolumeInfo vol : storage.getWritablePrivateVolumes()) {
                java.lang.String volumeUuid = vol.getFsUuid();
                if (volumeUuid != null) {
                    destroyUserDataLI(volumeUuid, userId, flags);
                }
            }
            destroyUserDataLI(null, userId, flags);
            if (installLock != null) {
                installLock.close();
            }
        } catch (java.lang.Throwable th) {
            if (installLock != null) {
                try {
                    installLock.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    void destroyUserDataLI(java.lang.String volumeUuid, int userId, int flags) {
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class);
        try {
            this.mInstaller.destroyUserData(volumeUuid, userId, flags);
            if (java.util.Objects.equals(volumeUuid, android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL)) {
                if ((flags & 1) != 0) {
                    android.os.FileUtils.deleteContentsAndDir(getUserSystemDirectory(userId));
                    android.os.FileUtils.deleteContents(getDataSystemDeDirectory(userId));
                }
                if ((flags & 2) != 0) {
                    android.os.FileUtils.deleteContents(getDataSystemCeDirectory(userId));
                }
            }
            storage.destroyUserStorage(volumeUuid, userId, flags);
        } catch (java.lang.Exception e) {
            com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Failed to destroy user " + userId + " on volume " + volumeUuid + ": " + e);
        }
    }

    void reconcileUsers(java.lang.String volumeUuid, java.util.List<android.content.pm.UserInfo> validUsersList) {
        java.util.List<java.io.File> files = new java.util.ArrayList<>();
        java.util.Collections.addAll(files, android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getDataUserDeDirectory(volumeUuid)));
        java.util.Collections.addAll(files, android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getDataUserCeDirectory(volumeUuid)));
        java.util.Collections.addAll(files, android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getDataSystemDeDirectory()));
        java.util.Collections.addAll(files, android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getDataSystemCeDirectory()));
        java.util.Collections.addAll(files, android.os.FileUtils.listFilesOrEmpty(android.os.Environment.getDataMiscCeDirectory()));
        reconcileUsers(volumeUuid, validUsersList, files);
    }

    void reconcileUsers(java.lang.String volumeUuid, java.util.List<android.content.pm.UserInfo> validUsersList, java.util.List<java.io.File> files) {
        int userCount = validUsersList.size();
        android.util.SparseArray<android.content.pm.UserInfo> users = new android.util.SparseArray<>(userCount);
        for (int i = 0; i < userCount; i++) {
            android.content.pm.UserInfo user = validUsersList.get(i);
            users.put(user.id, user);
        }
        for (java.io.File file : files) {
            if (file.isDirectory()) {
                try {
                    int userId = java.lang.Integer.parseInt(file.getName());
                    android.content.pm.UserInfo info = users.get(userId);
                    boolean destroyUser = false;
                    if (info == null) {
                        com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Destroying user directory " + file + " because no matching user was found");
                        destroyUser = true;
                    } else {
                        try {
                            enforceSerialNumber(file, info.serialNumber);
                        } catch (java.io.IOException e) {
                            com.android.server.pm.PackageManagerServiceUtils.logCriticalInfo(5, "Destroying user directory " + file + " because we failed to enforce serial number: " + e);
                            destroyUser = true;
                        }
                    }
                    if (destroyUser) {
                        if (this.userDataExt != null) {
                            this.userDataExt.reconcileUsers(volumeUuid, validUsersList, files);
                        }
                        com.android.server.pm.PackageManagerTracedLock installLock = this.mInstallLock.acquireLock();
                        try {
                            destroyUserDataLI(volumeUuid, userId, 3);
                            if (installLock != null) {
                                installLock.close();
                            }
                        } catch (java.lang.Throwable th) {
                            if (installLock != null) {
                                try {
                                    installLock.close();
                                } catch (java.lang.Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    } else {
                        continue;
                    }
                } catch (java.lang.NumberFormatException e2) {
                    android.util.Slog.w(TAG, "Invalid user directory " + file);
                }
            }
        }
    }

    protected java.io.File getDataMiscCeDirectory(int userId) {
        return android.os.Environment.getDataMiscCeDirectory(userId);
    }

    protected java.io.File getDataSystemCeDirectory(int userId) {
        return android.os.Environment.getDataSystemCeDirectory(userId);
    }

    protected java.io.File getDataMiscDeDirectory(int userId) {
        return android.os.Environment.getDataMiscDeDirectory(userId);
    }

    protected java.io.File getUserSystemDirectory(int userId) {
        return android.os.Environment.getUserSystemDirectory(userId);
    }

    protected java.io.File getDataUserCeDirectory(java.lang.String volumeUuid, int userId) {
        return android.os.Environment.getDataUserCeDirectory(volumeUuid, userId);
    }

    protected java.io.File getDataSystemDeDirectory(int userId) {
        return android.os.Environment.getDataSystemDeDirectory(userId);
    }

    protected java.io.File getDataUserDeDirectory(java.lang.String volumeUuid, int userId) {
        return android.os.Environment.getDataUserDeDirectory(volumeUuid, userId);
    }

    void enforceSerialNumber(java.io.File file, int serialNumber) throws java.io.IOException {
        int foundSerial = getSerialNumber(file);
        android.util.Slog.v(TAG, "Found " + file + " with serial number " + foundSerial);
        if (foundSerial == -1) {
            android.util.Slog.d(TAG, "Serial number missing on " + file + "; assuming current is valid");
            try {
                setSerialNumber(file, serialNumber);
                return;
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Failed to set serial number on " + file, e);
                return;
            }
        }
        if (foundSerial != serialNumber) {
            throw new java.io.IOException("Found serial number " + foundSerial + " doesn't match expected " + serialNumber);
        }
    }

    private static void setSerialNumber(java.io.File file, int serialNumber) throws java.io.IOException {
        try {
            byte[] buf = java.lang.Integer.toString(serialNumber).getBytes(java.nio.charset.StandardCharsets.UTF_8);
            android.system.Os.setxattr(file.getAbsolutePath(), XATTR_SERIAL, buf, android.system.OsConstants.XATTR_CREATE);
        } catch (android.system.ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    static int getSerialNumber(java.io.File file) throws java.io.IOException {
        try {
            byte[] buf = android.system.Os.getxattr(file.getAbsolutePath(), XATTR_SERIAL);
            java.lang.String serial = new java.lang.String(buf);
            try {
                return java.lang.Integer.parseInt(serial);
            } catch (java.lang.NumberFormatException e) {
                throw new java.io.IOException("Bad serial number: " + serial);
            }
        } catch (android.system.ErrnoException e2) {
            if (e2.errno == android.system.OsConstants.ENODATA) {
                return -1;
            }
            throw e2.rethrowAsIOException();
        }
    }
}
