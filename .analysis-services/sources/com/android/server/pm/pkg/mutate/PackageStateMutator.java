package com.android.server.pm.pkg.mutate;

/* JADX INFO: loaded from: classes2.dex */
public class PackageStateMutator {
    private static final java.util.concurrent.atomic.AtomicLong sStateChangeSequence = new java.util.concurrent.atomic.AtomicLong();
    private final java.util.function.Function<java.lang.String, com.android.server.pm.PackageSetting> mActiveStateFunction;
    private final java.util.function.Function<java.lang.String, com.android.server.pm.PackageSetting> mDisabledStateFunction;
    private final com.android.server.pm.pkg.mutate.PackageStateMutator.StateWriteWrapper mStateWrite = new com.android.server.pm.pkg.mutate.PackageStateMutator.StateWriteWrapper();
    private final android.util.ArraySet<com.android.server.pm.PackageSetting> mChangedStates = new android.util.ArraySet<>();

    public PackageStateMutator(java.util.function.Function<java.lang.String, com.android.server.pm.PackageSetting> activeStateFunction, java.util.function.Function<java.lang.String, com.android.server.pm.PackageSetting> disabledStateFunction) {
        this.mActiveStateFunction = activeStateFunction;
        this.mDisabledStateFunction = disabledStateFunction;
    }

    public static void onPackageStateChanged() {
        sStateChangeSequence.incrementAndGet();
    }

    public com.android.server.pm.pkg.mutate.PackageStateWrite forPackage(java.lang.String packageName) {
        return setState(this.mActiveStateFunction.apply(packageName));
    }

    public com.android.server.pm.pkg.mutate.PackageStateWrite forPackageNullable(java.lang.String packageName) {
        com.android.server.pm.PackageSetting packageState = this.mActiveStateFunction.apply(packageName);
        setState(packageState);
        if (packageState == null) {
            return null;
        }
        return setState(packageState);
    }

    public com.android.server.pm.pkg.mutate.PackageStateWrite forDisabledSystemPackage(java.lang.String packageName) {
        return setState(this.mDisabledStateFunction.apply(packageName));
    }

    public com.android.server.pm.pkg.mutate.PackageStateWrite forDisabledSystemPackageNullable(java.lang.String packageName) {
        com.android.server.pm.PackageSetting packageState = this.mDisabledStateFunction.apply(packageName);
        if (packageState == null) {
            return null;
        }
        return setState(packageState);
    }

    public com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState initialState(int changedPackagesSequenceNumber) {
        return new com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState(changedPackagesSequenceNumber, sStateChangeSequence.get());
    }

    public com.android.server.pm.pkg.mutate.PackageStateMutator.Result generateResult(com.android.server.pm.pkg.mutate.PackageStateMutator.InitialState state, int changedPackagesSequenceNumber) {
        if (state == null) {
            return com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SUCCESS;
        }
        boolean packagesChanged = changedPackagesSequenceNumber != state.mPackageSequence;
        boolean stateChanged = sStateChangeSequence.get() != state.mStateSequence;
        if (packagesChanged && stateChanged) {
            return com.android.server.pm.pkg.mutate.PackageStateMutator.Result.PACKAGES_AND_STATE_CHANGED;
        }
        if (packagesChanged) {
            return com.android.server.pm.pkg.mutate.PackageStateMutator.Result.PACKAGES_CHANGED;
        }
        if (stateChanged) {
            return com.android.server.pm.pkg.mutate.PackageStateMutator.Result.STATE_CHANGED;
        }
        return com.android.server.pm.pkg.mutate.PackageStateMutator.Result.SUCCESS;
    }

    public void onFinished() {
        for (int index = 0; index < this.mChangedStates.size(); index++) {
            this.mChangedStates.valueAt(index).onChanged();
        }
    }

    private com.android.server.pm.pkg.mutate.PackageStateMutator.StateWriteWrapper setState(com.android.server.pm.PackageSetting state) {
        if (state != null) {
            this.mChangedStates.add(state);
        }
        return this.mStateWrite.setState(state);
    }

    public static class InitialState {
        private final int mPackageSequence;
        private final long mStateSequence;

        public InitialState(int packageSequence, long stateSequence) {
            this.mPackageSequence = packageSequence;
            this.mStateSequence = stateSequence;
        }
    }

    public static class Result {
        private final boolean mCommitted;
        private final boolean mPackagesChanged;
        private final boolean mSpecificPackageNull;
        private final boolean mStateChanged;
        public static final com.android.server.pm.pkg.mutate.PackageStateMutator.Result SUCCESS = new com.android.server.pm.pkg.mutate.PackageStateMutator.Result(true, false, false, false);
        public static final com.android.server.pm.pkg.mutate.PackageStateMutator.Result PACKAGES_CHANGED = new com.android.server.pm.pkg.mutate.PackageStateMutator.Result(false, true, false, false);
        public static final com.android.server.pm.pkg.mutate.PackageStateMutator.Result STATE_CHANGED = new com.android.server.pm.pkg.mutate.PackageStateMutator.Result(false, false, true, false);
        public static final com.android.server.pm.pkg.mutate.PackageStateMutator.Result PACKAGES_AND_STATE_CHANGED = new com.android.server.pm.pkg.mutate.PackageStateMutator.Result(false, true, true, false);
        public static final com.android.server.pm.pkg.mutate.PackageStateMutator.Result SPECIFIC_PACKAGE_NULL = new com.android.server.pm.pkg.mutate.PackageStateMutator.Result(false, false, true, true);

        public Result(boolean committed, boolean packagesChanged, boolean stateChanged, boolean specificPackageNull) {
            this.mCommitted = committed;
            this.mPackagesChanged = packagesChanged;
            this.mStateChanged = stateChanged;
            this.mSpecificPackageNull = specificPackageNull;
        }

        public boolean isCommitted() {
            return this.mCommitted;
        }

        public boolean isPackagesChanged() {
            return this.mPackagesChanged;
        }

        public boolean isStateChanged() {
            return this.mStateChanged;
        }

        public boolean isSpecificPackageNull() {
            return this.mSpecificPackageNull;
        }
    }

    private static class StateWriteWrapper implements com.android.server.pm.pkg.mutate.PackageStateWrite {
        private com.android.server.pm.PackageSetting mState;
        private final com.android.server.pm.pkg.mutate.PackageStateMutator.StateWriteWrapper.UserStateWriteWrapper mUserStateWrite;

        private StateWriteWrapper() {
            this.mUserStateWrite = new com.android.server.pm.pkg.mutate.PackageStateMutator.StateWriteWrapper.UserStateWriteWrapper();
        }

        public com.android.server.pm.pkg.mutate.PackageStateMutator.StateWriteWrapper setState(com.android.server.pm.PackageSetting state) {
            this.mState = state;
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageUserStateWrite userState(int userId) {
            com.android.server.pm.pkg.PackageUserStateImpl userState = this.mState == null ? null : this.mState.getOrCreateUserState(userId);
            if (userState != null) {
                userState.setWatchable(this.mState);
            }
            return this.mUserStateWrite.setStates(userState);
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public void onChanged() {
            if (this.mState != null) {
                this.mState.onChanged();
            }
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setLastPackageUsageTime(int reason, long timeInMillis) {
            if (this.mState != null) {
                this.mState.getTransientState().setLastPackageUsageTimeInMills(reason, timeInMillis);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setHiddenUntilInstalled(boolean value) {
            if (this.mState != null) {
                this.mState.getTransientState().setHiddenUntilInstalled(value);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setRequiredForSystemUser(boolean requiredForSystemUser) {
            if (this.mState != null) {
                if (requiredForSystemUser) {
                    this.mState.setPrivateFlags(this.mState.getPrivateFlags() | 512);
                } else {
                    this.mState.setPrivateFlags(this.mState.getPrivateFlags() & (-513));
                }
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setMimeGroup(java.lang.String mimeGroup, android.util.ArraySet<java.lang.String> mimeTypes) {
            if (this.mState != null) {
                this.mState.setMimeGroup(mimeGroup, mimeTypes);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setCategoryOverride(int category) {
            if (this.mState != null) {
                this.mState.setCategoryOverride(category);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setUpdateAvailable(boolean updateAvailable) {
            if (this.mState != null) {
                this.mState.setUpdateAvailable(updateAvailable);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setLoadingProgress(float progress) {
            if (this.mState != null) {
                this.mState.setLoadingProgress(progress);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setLoadingCompletedTime(long loadingCompletedTime) {
            if (this.mState != null) {
                this.mState.setLoadingCompletedTime(loadingCompletedTime);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setOverrideSeInfo(java.lang.String newSeInfo) {
            if (this.mState != null) {
                this.mState.getTransientState().setOverrideSeInfo(newSeInfo);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setInstaller(java.lang.String installerPackageName, int installerPackageUid) {
            if (this.mState != null) {
                this.mState.setInstallerPackage(installerPackageName, installerPackageUid);
            }
            return this;
        }

        @Override // com.android.server.pm.pkg.mutate.PackageStateWrite
        public com.android.server.pm.pkg.mutate.PackageStateWrite setUpdateOwner(java.lang.String updateOwnerPackageName) {
            if (this.mState != null) {
                this.mState.setUpdateOwnerPackage(updateOwnerPackageName);
            }
            return this;
        }

        private static class UserStateWriteWrapper implements com.android.server.pm.pkg.mutate.PackageUserStateWrite {
            private com.android.server.pm.pkg.PackageUserStateImpl mUserState;

            private UserStateWriteWrapper() {
            }

            public com.android.server.pm.pkg.mutate.PackageStateMutator.StateWriteWrapper.UserStateWriteWrapper setStates(com.android.server.pm.pkg.PackageUserStateImpl userState) {
                this.mUserState = userState;
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setInstalled(boolean installed) {
                if (this.mUserState != null) {
                    this.mUserState.setInstalled(installed);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setUninstallReason(int reason) {
                if (this.mUserState != null) {
                    this.mUserState.setUninstallReason(reason);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setDistractionFlags(int restrictionFlags) {
                if (this.mUserState != null) {
                    this.mUserState.setDistractionFlags(restrictionFlags);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite putSuspendParams(android.content.pm.UserPackage suspendingPackage, com.android.server.pm.pkg.SuspendParams suspendParams) {
                if (this.mUserState != null) {
                    this.mUserState.putSuspendParams(suspendingPackage, suspendParams);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite removeSuspension(android.content.pm.UserPackage suspendingPackage) {
                if (this.mUserState != null) {
                    this.mUserState.removeSuspension(suspendingPackage);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setHidden(boolean hidden) {
                if (this.mUserState != null) {
                    this.mUserState.setHidden(hidden);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setStopped(boolean stopped) {
                if (this.mUserState != null) {
                    this.mUserState.setStopped(stopped);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setNotLaunched(boolean notLaunched) {
                if (this.mUserState != null) {
                    this.mUserState.setNotLaunched(notLaunched);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setOverlayPaths(android.content.pm.overlay.OverlayPaths overlayPaths) {
                if (this.mUserState != null) {
                    this.mUserState.setOverlayPaths(overlayPaths);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setOverlayPathsForLibrary(java.lang.String libraryName, android.content.pm.overlay.OverlayPaths overlayPaths) {
                if (this.mUserState != null) {
                    this.mUserState.setSharedLibraryOverlayPaths(libraryName, overlayPaths);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setHarmfulAppWarning(java.lang.String warning) {
                if (this.mUserState != null) {
                    this.mUserState.setHarmfulAppWarning(warning);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setSplashScreenTheme(java.lang.String theme) {
                if (this.mUserState != null) {
                    this.mUserState.setSplashScreenTheme(theme);
                }
                return this;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setComponentLabelIcon(android.content.ComponentName componentName, java.lang.String nonLocalizedLabel, java.lang.Integer icon) {
                if (this.mUserState != null) {
                    this.mUserState.overrideLabelAndIcon(componentName, nonLocalizedLabel, icon);
                    return null;
                }
                return null;
            }

            @Override // com.android.server.pm.pkg.mutate.PackageUserStateWrite
            public com.android.server.pm.pkg.mutate.PackageUserStateWrite setMinAspectRatio(int aspectRatio) {
                if (this.mUserState != null) {
                    this.mUserState.setMinAspectRatio(aspectRatio);
                }
                return this;
            }
        }
    }
}
