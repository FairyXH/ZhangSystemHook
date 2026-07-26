package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public class SharedLibraryWrapper implements com.android.server.pm.pkg.SharedLibrary {
    private java.util.List<com.android.server.pm.pkg.SharedLibrary> cachedDependenciesList;
    private final android.content.pm.SharedLibraryInfo mInfo;

    public SharedLibraryWrapper(android.content.pm.SharedLibraryInfo info) {
        this.mInfo = info;
    }

    public android.content.pm.SharedLibraryInfo getInfo() {
        return this.mInfo;
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public java.lang.String getPath() {
        return this.mInfo.getPath();
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public java.lang.String getPackageName() {
        return this.mInfo.getPackageName();
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public java.lang.String getName() {
        return this.mInfo.getName();
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public java.util.List<java.lang.String> getAllCodePaths() {
        return java.util.Collections.unmodifiableList(this.mInfo.getAllCodePaths());
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public long getVersion() {
        return this.mInfo.getLongVersion();
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public int getType() {
        return this.mInfo.getType();
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public boolean isNative() {
        return this.mInfo.isNative();
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public android.content.pm.VersionedPackage getDeclaringPackage() {
        return this.mInfo.getDeclaringPackage();
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public java.util.List<android.content.pm.VersionedPackage> getDependentPackages() {
        return java.util.Collections.unmodifiableList(this.mInfo.getDependentPackages());
    }

    @Override // com.android.server.pm.pkg.SharedLibrary
    public java.util.List<com.android.server.pm.pkg.SharedLibrary> getDependencies() {
        if (this.cachedDependenciesList == null) {
            java.util.List<android.content.pm.SharedLibraryInfo> dependencies = this.mInfo.getDependencies();
            if (dependencies == null) {
                this.cachedDependenciesList = java.util.Collections.emptyList();
            } else {
                java.util.ArrayList<com.android.server.pm.pkg.SharedLibrary> list = new java.util.ArrayList<>();
                for (int index = 0; index < dependencies.size(); index++) {
                    list.add(new com.android.server.pm.pkg.SharedLibraryWrapper(dependencies.get(index)));
                }
                this.cachedDependenciesList = java.util.Collections.unmodifiableList(list);
            }
        }
        return this.cachedDependenciesList;
    }
}
