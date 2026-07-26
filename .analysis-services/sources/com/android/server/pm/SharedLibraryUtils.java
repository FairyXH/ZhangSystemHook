package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class SharedLibraryUtils {
    SharedLibraryUtils() {
    }

    public static boolean addSharedLibraryToPackageVersionMap(java.util.Map<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> target, android.content.pm.SharedLibraryInfo library) {
        java.lang.String name = library.getName();
        if (target.containsKey(name)) {
            if (library.getType() != 2 || target.get(name).indexOfKey(library.getLongVersion()) >= 0) {
                return false;
            }
        } else {
            target.put(name, new com.android.server.utils.WatchedLongSparseArray<>());
        }
        target.get(name).put(library.getLongVersion(), library);
        return true;
    }

    public static android.content.pm.SharedLibraryInfo getSharedLibraryInfo(java.lang.String name, long version, java.util.Map<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> existingLibraries, java.util.Map<java.lang.String, com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo>> newLibraries) {
        if (newLibraries != null) {
            com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib = newLibraries.get(name);
            android.content.pm.SharedLibraryInfo info = null;
            if (versionedLib != null) {
                android.content.pm.SharedLibraryInfo info2 = versionedLib.get(version);
                info = info2;
            }
            if (info != null) {
                return info;
            }
        }
        com.android.server.utils.WatchedLongSparseArray<android.content.pm.SharedLibraryInfo> versionedLib2 = existingLibraries.get(name);
        if (versionedLib2 == null) {
            return null;
        }
        return versionedLib2.get(version);
    }

    public static java.util.List<android.content.pm.SharedLibraryInfo> findSharedLibraries(com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        if (!pkgSetting.getTransientState().getUsesLibraryInfos().isEmpty()) {
            java.util.ArrayList<android.content.pm.SharedLibraryInfo> retValue = new java.util.ArrayList<>();
            java.util.Set<java.lang.String> collectedNames = new java.util.HashSet<>();
            for (com.android.server.pm.pkg.SharedLibraryWrapper info : pkgSetting.getTransientState().getUsesLibraryInfos()) {
                findSharedLibrariesRecursive(info.getInfo(), retValue, collectedNames);
            }
            return retValue;
        }
        return java.util.Collections.emptyList();
    }

    private static void findSharedLibrariesRecursive(android.content.pm.SharedLibraryInfo info, java.util.ArrayList<android.content.pm.SharedLibraryInfo> collected, java.util.Set<java.lang.String> collectedNames) {
        if (!collectedNames.contains(info.getName())) {
            collectedNames.add(info.getName());
            collected.add(info);
            if (info.getDependencies() != null) {
                for (android.content.pm.SharedLibraryInfo dep : info.getDependencies()) {
                    findSharedLibrariesRecursive(dep, collected, collectedNames);
                }
            }
        }
    }
}
