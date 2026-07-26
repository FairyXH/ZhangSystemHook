package com.android.server.pm.parsing.library;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PackageSharedLibraryUpdater {
    public abstract void updatePackage(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, boolean z, boolean z2);

    static void removeLibrary(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, java.lang.String libraryName) {
        parsedPackage.removeUsesLibrary(libraryName).removeUsesOptionalLibrary(libraryName);
    }

    static <T> java.util.ArrayList<T> prefix(java.util.ArrayList<T> cur, T val) {
        if (cur == null) {
            cur = new java.util.ArrayList<>();
        }
        cur.add(0, val);
        return cur;
    }

    private static boolean isLibraryPresent(java.util.List<java.lang.String> usesLibraries, java.util.List<java.lang.String> usesOptionalLibraries, java.lang.String apacheHttpLegacy) {
        return com.android.internal.util.ArrayUtils.contains(usesLibraries, apacheHttpLegacy) || com.android.internal.util.ArrayUtils.contains(usesOptionalLibraries, apacheHttpLegacy);
    }

    void prefixImplicitDependency(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, java.lang.String existingLibrary, java.lang.String implicitDependency) {
        java.util.List<java.lang.String> usesLibraries = parsedPackage.getUsesLibraries();
        java.util.List<java.lang.String> usesOptionalLibraries = parsedPackage.getUsesOptionalLibraries();
        if (!isLibraryPresent(usesLibraries, usesOptionalLibraries, implicitDependency)) {
            if (com.android.internal.util.ArrayUtils.contains(usesLibraries, existingLibrary)) {
                parsedPackage.addUsesLibrary(0, implicitDependency);
            } else if (com.android.internal.util.ArrayUtils.contains(usesOptionalLibraries, existingLibrary)) {
                parsedPackage.addUsesOptionalLibrary(0, implicitDependency);
            }
        }
    }

    void prefixRequiredLibrary(com.android.internal.pm.parsing.pkg.ParsedPackage parsedPackage, java.lang.String libraryName) {
        java.util.List<java.lang.String> usesLibraries = parsedPackage.getUsesLibraries();
        java.util.List<java.lang.String> usesOptionalLibraries = parsedPackage.getUsesOptionalLibraries();
        boolean alreadyPresent = isLibraryPresent(usesLibraries, usesOptionalLibraries, libraryName);
        if (!alreadyPresent) {
            parsedPackage.addUsesLibrary(0, libraryName);
        }
    }
}
