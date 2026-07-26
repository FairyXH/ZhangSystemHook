package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
public class RecursiveStringArrayResourceResolver {
    private static final java.lang.String IMPORT_PREFIX = "#import:";
    private static final java.lang.String PWP = ".";
    private static final java.lang.String SEPARATOR = "/";
    private final android.content.res.Resources mResources;

    public RecursiveStringArrayResourceResolver(android.content.res.Resources resources) {
        this.mResources = resources;
    }

    public java.util.Set<java.lang.String> resolve(java.lang.String pkg, int rootId) {
        return resolve(java.util.List.of(), pkg, rootId);
    }

    private java.util.Set<java.lang.String> resolve(java.util.Collection<java.lang.String> cache, java.lang.String pkg, int rootId) {
        java.lang.String ref;
        java.lang.String[] strings = this.mResources.getStringArray(rootId);
        java.util.ArrayList<java.lang.String> runningCache = new java.util.ArrayList<>(cache);
        java.util.HashSet<java.lang.String> result = new java.util.HashSet<>();
        for (java.lang.String string : strings) {
            if (string.startsWith(IMPORT_PREFIX)) {
                ref = string.substring(IMPORT_PREFIX.length());
            } else {
                ref = null;
            }
            if (ref == null) {
                result.add(string);
            } else if (!runningCache.contains(ref)) {
                java.util.Set<java.lang.String> next = resolveImport(runningCache, pkg, ref);
                runningCache.addAll(next);
                result.addAll(next);
            }
        }
        return result;
    }

    private java.util.Set<java.lang.String> resolveImport(java.util.Collection<java.lang.String> cache, java.lang.String importingPackage, java.lang.String ref) {
        java.lang.String resolvedPkg;
        java.lang.String[] chunks = ref.split("/", 2);
        java.lang.String pkg = chunks[0];
        java.lang.String name = chunks[1];
        if (java.util.Objects.equals(pkg, PWP)) {
            resolvedPkg = importingPackage;
        } else {
            resolvedPkg = pkg;
        }
        int importId = this.mResources.getIdentifier(name, "array", resolvedPkg);
        if (importId == 0) {
            throw new android.content.res.Resources.NotFoundException(java.lang.String.format("%s:array/%s", resolvedPkg, name));
        }
        return resolve(cache, resolvedPkg, importId);
    }
}
