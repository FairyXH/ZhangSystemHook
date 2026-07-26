package com.android.server.pm.dex;

/* JADX INFO: loaded from: classes2.dex */
public final class DexoptUtils {
    private static final java.lang.String TAG = "DexoptUtils";
    private static final java.lang.String SHARED_LIBRARY_LOADER_TYPE = com.android.internal.os.ClassLoaderFactory.getPathClassLoaderName();
    private static com.android.server.pm.dex.IDexoptUtilsExt.IStaticExt mIDexoptUtilsExt = (com.android.server.pm.dex.IDexoptUtilsExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.dex.IDexoptUtilsExt.IStaticExt.class).create();

    private DexoptUtils() {
    }

    public static java.lang.String[] getClassLoaderContexts(com.android.server.pm.pkg.AndroidPackage pkg, java.util.List<android.content.pm.SharedLibraryInfo> sharedLibraries, boolean[] pathsWithCode) {
        java.lang.String str;
        java.lang.String sharedLibrariesContext = "";
        if (sharedLibraries != null) {
            sharedLibrariesContext = encodeSharedLibraries(sharedLibraries);
        }
        java.lang.String customClassPath = "";
        if (pkg != null) {
            customClassPath = mIDexoptUtilsExt.getClassLoaderContext(pkg.getPackageName(), pkg.getBaseApkPath());
        }
        java.lang.String baseApkContextClassLoader = encodeClassLoader(customClassPath, pkg.getClassLoaderName(), sharedLibrariesContext);
        if (com.android.internal.util.ArrayUtils.isEmpty(pkg.getSplitCodePaths())) {
            return new java.lang.String[]{baseApkContextClassLoader};
        }
        java.lang.String[] splitRelativeCodePaths = getSplitRelativeCodePaths(pkg);
        java.lang.String baseApkName = new java.io.File(pkg.getBaseApkPath()).getName();
        java.lang.String[] classLoaderContexts = new java.lang.String[splitRelativeCodePaths.length + 1];
        classLoaderContexts[0] = pathsWithCode[0] ? baseApkContextClassLoader : null;
        android.util.SparseArray<int[]> splitDependencies = pkg.getSplitDependencies();
        if (!pkg.isIsolatedSplitLoading() || splitDependencies == null || splitDependencies.size() == 0) {
            java.lang.String classpath = baseApkName;
            for (int i = 1; i < classLoaderContexts.length; i++) {
                if (pathsWithCode[i]) {
                    classLoaderContexts[i] = encodeClassLoader(classpath, pkg.getClassLoaderName(), sharedLibrariesContext);
                } else {
                    classLoaderContexts[i] = null;
                }
                classpath = encodeClasspath(classpath, splitRelativeCodePaths[i - 1]);
            }
        } else {
            java.lang.String[] splitClassLoaderEncodingCache = new java.lang.String[splitRelativeCodePaths.length];
            for (int i2 = 0; i2 < splitRelativeCodePaths.length; i2++) {
                splitClassLoaderEncodingCache[i2] = encodeClassLoader(splitRelativeCodePaths[i2], pkg.getSplitClassLoaderNames()[i2]);
            }
            java.lang.String splitDependencyOnBase = encodeClassLoader(baseApkName, pkg.getClassLoaderName());
            for (int i3 = 1; i3 < splitDependencies.size(); i3++) {
                int splitIndex = splitDependencies.keyAt(i3);
                if (pathsWithCode[splitIndex]) {
                    getParentDependencies(splitIndex, splitClassLoaderEncodingCache, splitDependencies, classLoaderContexts, splitDependencyOnBase);
                }
            }
            for (int i4 = 1; i4 < classLoaderContexts.length; i4++) {
                java.lang.String splitClassLoader = encodeClassLoader("", pkg.getSplitClassLoaderNames()[i4 - 1]);
                if (pathsWithCode[i4]) {
                    if (classLoaderContexts[i4] == null) {
                        str = splitClassLoader;
                    } else {
                        str = encodeClassLoaderChain(splitClassLoader, classLoaderContexts[i4]) + sharedLibrariesContext;
                    }
                    classLoaderContexts[i4] = str;
                } else {
                    classLoaderContexts[i4] = null;
                }
            }
        }
        return classLoaderContexts;
    }

    public static java.lang.String getClassLoaderContext(android.content.pm.SharedLibraryInfo info) {
        java.lang.String sharedLibrariesContext = "";
        if (info.getDependencies() != null) {
            sharedLibrariesContext = encodeSharedLibraries(info.getDependencies());
        }
        return encodeClassLoader("", SHARED_LIBRARY_LOADER_TYPE, sharedLibrariesContext);
    }

    private static java.lang.String getParentDependencies(int index, java.lang.String[] splitClassLoaderEncodingCache, android.util.SparseArray<int[]> splitDependencies, java.lang.String[] classLoaderContexts, java.lang.String splitDependencyOnBase) {
        java.lang.String splitContext;
        if (index == 0) {
            return splitDependencyOnBase;
        }
        if (classLoaderContexts[index] != null) {
            return classLoaderContexts[index];
        }
        int parent = splitDependencies.get(index)[0];
        java.lang.String parentDependencies = getParentDependencies(parent, splitClassLoaderEncodingCache, splitDependencies, classLoaderContexts, splitDependencyOnBase);
        if (parent == 0) {
            splitContext = parentDependencies;
        } else {
            splitContext = encodeClassLoaderChain(splitClassLoaderEncodingCache[parent - 1], parentDependencies);
        }
        classLoaderContexts[index] = splitContext;
        return splitContext;
    }

    private static java.lang.String encodeSharedLibrary(android.content.pm.SharedLibraryInfo sharedLibrary) {
        java.util.List<java.lang.String> paths = sharedLibrary.getAllCodePaths();
        java.lang.String classLoaderSpec = encodeClassLoader(encodeClasspath((java.lang.String[]) paths.toArray(new java.lang.String[paths.size()])), SHARED_LIBRARY_LOADER_TYPE);
        if (sharedLibrary.getDependencies() != null) {
            return classLoaderSpec + encodeSharedLibraries(sharedLibrary.getDependencies());
        }
        return classLoaderSpec;
    }

    private static java.lang.String encodeSharedLibraries(java.util.List<android.content.pm.SharedLibraryInfo> sharedLibraries) {
        java.lang.String sharedLibrariesContext = "{";
        boolean first = true;
        for (android.content.pm.SharedLibraryInfo info : sharedLibraries) {
            if (!first) {
                sharedLibrariesContext = sharedLibrariesContext + "#";
            }
            first = false;
            sharedLibrariesContext = sharedLibrariesContext + encodeSharedLibrary(info);
        }
        return sharedLibrariesContext + "}";
    }

    private static java.lang.String encodeClasspath(java.lang.String[] classpathElements) {
        if (classpathElements == null || classpathElements.length == 0) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.String element : classpathElements) {
            if (sb.length() != 0) {
                sb.append(":");
            }
            sb.append(element);
        }
        return sb.toString();
    }

    private static java.lang.String encodeClasspath(java.lang.String classpath, java.lang.String newElement) {
        return classpath.isEmpty() ? newElement : classpath + ":" + newElement;
    }

    static java.lang.String encodeClassLoader(java.lang.String classpath, java.lang.String classLoaderName) {
        classpath.getClass();
        java.lang.String classLoaderDexoptEncoding = classLoaderName;
        if (com.android.internal.os.ClassLoaderFactory.isPathClassLoaderName(classLoaderName)) {
            classLoaderDexoptEncoding = "PCL";
        } else if (com.android.internal.os.ClassLoaderFactory.isDelegateLastClassLoaderName(classLoaderName)) {
            classLoaderDexoptEncoding = "DLC";
        } else {
            android.util.Slog.wtf(TAG, "Unsupported classLoaderName: " + classLoaderName);
        }
        return classLoaderDexoptEncoding + "[" + classpath + "]";
    }

    private static java.lang.String encodeClassLoader(java.lang.String classpath, java.lang.String classLoaderName, java.lang.String sharedLibraries) {
        return encodeClassLoader(classpath, classLoaderName) + sharedLibraries;
    }

    static java.lang.String encodeClassLoaderChain(java.lang.String cl1, java.lang.String cl2) {
        return cl1.isEmpty() ? cl2 : cl2.isEmpty() ? cl1 : cl1 + ";" + cl2;
    }

    static java.lang.String[] processContextForDexLoad(java.util.List<java.lang.String> classLoadersNames, java.util.List<java.lang.String> classPaths) {
        if (classLoadersNames.size() != classPaths.size()) {
            throw new java.lang.IllegalArgumentException("The size of the class loader names and the dex paths do not match.");
        }
        if (classLoadersNames.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Empty classLoadersNames");
        }
        java.lang.String parentContext = "";
        for (int i = 1; i < classLoadersNames.size(); i++) {
            if (!com.android.internal.os.ClassLoaderFactory.isValidClassLoaderName(classLoadersNames.get(i)) || classPaths.get(i) == null) {
                return null;
            }
            java.lang.String classpath = encodeClasspath(classPaths.get(i).split(java.io.File.pathSeparator));
            parentContext = encodeClassLoaderChain(parentContext, encodeClassLoader(classpath, classLoadersNames.get(i)));
        }
        java.lang.String loadingClassLoader = classLoadersNames.get(0);
        if (!com.android.internal.os.ClassLoaderFactory.isValidClassLoaderName(loadingClassLoader)) {
            return null;
        }
        java.lang.String[] loadedDexPaths = classPaths.get(0).split(java.io.File.pathSeparator);
        java.lang.String[] loadedDexPathsContext = new java.lang.String[loadedDexPaths.length];
        java.lang.String currentLoadedDexPathClasspath = "";
        for (int i2 = 0; i2 < loadedDexPaths.length; i2++) {
            java.lang.String dexPath = loadedDexPaths[i2];
            java.lang.String currentContext = encodeClassLoader(currentLoadedDexPathClasspath, loadingClassLoader);
            loadedDexPathsContext[i2] = encodeClassLoaderChain(currentContext, parentContext);
            currentLoadedDexPathClasspath = encodeClasspath(currentLoadedDexPathClasspath, dexPath);
        }
        return loadedDexPathsContext;
    }

    private static java.lang.String[] getSplitRelativeCodePaths(com.android.server.pm.pkg.AndroidPackage pkg) {
        java.lang.String baseCodePath = new java.io.File(pkg.getBaseApkPath()).getParent();
        java.lang.String[] splitCodePaths = pkg.getSplitCodePaths();
        java.lang.String[] splitRelativeCodePaths = new java.lang.String[com.android.internal.util.ArrayUtils.size(splitCodePaths)];
        for (int i = 0; i < splitRelativeCodePaths.length; i++) {
            java.io.File pathFile = new java.io.File(splitCodePaths[i]);
            splitRelativeCodePaths[i] = pathFile.getName();
            java.lang.String basePath = pathFile.getParent();
            if (!basePath.equals(baseCodePath)) {
                android.util.Slog.wtf(TAG, "Split paths have different base paths: " + basePath + " and " + baseCodePath);
            }
        }
        return splitRelativeCodePaths;
    }
}
