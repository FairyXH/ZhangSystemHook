package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
@android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
public interface PackageManagerLocal {
    public static final int FLAG_STORAGE_CE = 2;
    public static final int FLAG_STORAGE_DE = 1;

    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
    public interface FilteredSnapshot extends java.lang.AutoCloseable {
        @Override // java.lang.AutoCloseable
        void close();

        com.android.server.pm.pkg.PackageState getPackageState(java.lang.String str);

        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getPackageStates();
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface StorageFlags {
    }

    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.SYSTEM_SERVER)
    public interface UnfilteredSnapshot extends java.lang.AutoCloseable {
        @Override // java.lang.AutoCloseable
        void close();

        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filtered(int i, android.os.UserHandle userHandle);

        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getDisabledSystemPackageStates();

        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> getPackageStates();

        java.util.Map<java.lang.String, com.android.server.pm.pkg.SharedUserApi> getSharedUsers();
    }

    void addOverrideSigningDetails(android.content.pm.SigningDetails signingDetails, android.content.pm.SigningDetails signingDetails2);

    void clearOverrideSigningDetails();

    void reconcileSdkData(java.lang.String str, java.lang.String str2, java.util.List<java.lang.String> list, int i, int i2, int i3, java.lang.String str3, int i4) throws java.io.IOException;

    void removeOverrideSigningDetails(android.content.pm.SigningDetails signingDetails);

    com.android.server.pm.PackageManagerLocal.FilteredSnapshot withFilteredSnapshot();

    com.android.server.pm.PackageManagerLocal.FilteredSnapshot withFilteredSnapshot(int i, android.os.UserHandle userHandle);

    com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot withUnfilteredSnapshot();
}
