package com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: DebugMetadata.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bÂ\u0002\u0018\u00002\u00020\u0001:\u0001\u000bB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0007\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\f"}, d2 = {"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever;", "", "()V", "cache", "Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "notOnJava9", "buildCache", "continuation", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getModuleName", "", "Cache", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ModuleNameRetriever {
    private static com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache cache;
    public static final com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever INSTANCE = new com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever();
    private static final com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache notOnJava9 = new com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache(null, null, null);

    /* JADX INFO: compiled from: DebugMetadata.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0002\u0018\u00002\u00020\u0001B#\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0006R\u0012\u0010\u0004\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, d2 = {"Lkotlin/coroutines/jvm/internal/ModuleNameRetriever$Cache;", "", "getModuleMethod", "Ljava/lang/reflect/Method;", "getDescriptorMethod", "nameMethod", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Cache {
        public final java.lang.reflect.Method getDescriptorMethod;
        public final java.lang.reflect.Method getModuleMethod;
        public final java.lang.reflect.Method nameMethod;

        public Cache(java.lang.reflect.Method getModuleMethod, java.lang.reflect.Method getDescriptorMethod, java.lang.reflect.Method nameMethod) {
            this.getModuleMethod = getModuleMethod;
            this.getDescriptorMethod = getDescriptorMethod;
            this.nameMethod = nameMethod;
        }
    }

    private ModuleNameRetriever() {
    }

    public final java.lang.String getModuleName(com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache cache2 = cache;
        if (cache2 == null) {
            cache2 = buildCache(continuation);
        }
        if (cache2 == notOnJava9) {
            return null;
        }
        java.lang.reflect.Method method = cache2.getModuleMethod;
        java.lang.Object module = method != null ? method.invoke(continuation.getClass(), new java.lang.Object[0]) : null;
        if (module == null) {
            return null;
        }
        java.lang.reflect.Method method2 = cache2.getDescriptorMethod;
        java.lang.Object descriptor = method2 != null ? method2.invoke(module, new java.lang.Object[0]) : null;
        if (descriptor == null) {
            return null;
        }
        java.lang.reflect.Method method3 = cache2.nameMethod;
        java.lang.Object objInvoke = method3 != null ? method3.invoke(descriptor, new java.lang.Object[0]) : null;
        if (objInvoke instanceof java.lang.String) {
            return (java.lang.String) objInvoke;
        }
        return null;
    }

    private final com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache buildCache(com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.BaseContinuationImpl continuation) {
        try {
            java.lang.reflect.Method getModuleMethod = java.lang.Class.class.getDeclaredMethod("getModule", new java.lang.Class[0]);
            java.lang.reflect.Method getDescriptorMethod = continuation.getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", new java.lang.Class[0]);
            java.lang.reflect.Method nameMethod = continuation.getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod("name", new java.lang.Class[0]);
            com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache it = new com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache(getModuleMethod, getDescriptorMethod, nameMethod);
            cache = it;
            return it;
        } catch (java.lang.Exception e) {
            com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.ModuleNameRetriever.Cache it2 = notOnJava9;
            cache = it2;
            return it2;
        }
    }
}
