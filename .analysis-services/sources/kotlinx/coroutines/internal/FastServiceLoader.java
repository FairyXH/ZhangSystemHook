package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: FastServiceLoader.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J!\u0010\u0005\u001a\u0004\u0018\u00010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\u0006\u0010\t\u001a\u00020\u0004H\u0082\bJ1\u0010\n\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\bH\u0002¢\u0006\u0002\u0010\u0010J*\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0012\"\u0004\b\u0000\u0010\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\b2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0013\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u0012H\u0000¢\u0006\u0002\b\u0014J/\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0012\"\u0004\b\u0000\u0010\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\b2\u0006\u0010\r\u001a\u00020\u000eH\u0000¢\u0006\u0002\b\u0016J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u00122\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0016\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u00122\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J,\u0010\u001d\u001a\u0002H\u001e\"\u0004\b\u0000\u0010\u001e*\u00020\u001f2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u0002H\u001e0!H\u0082\b¢\u0006\u0002\u0010\"R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006#"}, d2 = {"Lkotlinx/coroutines/internal/FastServiceLoader;", "", "()V", "PREFIX", "", "createInstanceOf", "Lkotlinx/coroutines/internal/MainDispatcherFactory;", "baseClass", "Ljava/lang/Class;", "serviceClass", "getProviderInstance", "S", "name", "loader", "Ljava/lang/ClassLoader;", com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, "(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Object;", "load", "", "loadMainDispatcherFactory", "loadMainDispatcherFactory$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "loadProviders", "loadProviders$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "parse", "url", "Ljava/net/URL;", "parseFile", com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD, "Ljava/io/BufferedReader;", "use", "R", "Ljava/util/jar/JarFile;", "block", "Lkotlin/Function1;", "(Ljava/util/jar/JarFile;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class FastServiceLoader {
    public static final kotlinx.coroutines.internal.FastServiceLoader INSTANCE = new kotlinx.coroutines.internal.FastServiceLoader();
    private static final java.lang.String PREFIX = "META-INF/services/";

    private FastServiceLoader() {
    }

    public final java.util.List<kotlinx.coroutines.internal.MainDispatcherFactory> loadMainDispatcherFactory$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        kotlinx.coroutines.internal.MainDispatcherFactory mainDispatcherFactory;
        if (!kotlinx.coroutines.internal.FastServiceLoaderKt.getANDROID_DETECTED()) {
            java.lang.ClassLoader classLoader = kotlinx.coroutines.internal.MainDispatcherFactory.class.getClassLoader();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(classLoader, "getClassLoader(...)");
            return load(kotlinx.coroutines.internal.MainDispatcherFactory.class, classLoader);
        }
        try {
            java.util.ArrayList result = new java.util.ArrayList(2);
            kotlinx.coroutines.internal.MainDispatcherFactory mainDispatcherFactory2 = null;
            try {
                mainDispatcherFactory = (kotlinx.coroutines.internal.MainDispatcherFactory) kotlinx.coroutines.internal.MainDispatcherFactory.class.cast(java.lang.Class.forName("kotlinx.coroutines.android.AndroidDispatcherFactory", true, kotlinx.coroutines.internal.MainDispatcherFactory.class.getClassLoader()).getDeclaredConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]));
            } catch (java.lang.ClassNotFoundException e) {
                mainDispatcherFactory = null;
            }
            if (mainDispatcherFactory != null) {
                kotlinx.coroutines.internal.MainDispatcherFactory $this$loadMainDispatcherFactory_u24lambda_u240 = mainDispatcherFactory;
                result.add($this$loadMainDispatcherFactory_u24lambda_u240);
            }
            try {
                mainDispatcherFactory2 = (kotlinx.coroutines.internal.MainDispatcherFactory) kotlinx.coroutines.internal.MainDispatcherFactory.class.cast(java.lang.Class.forName("kotlinx.coroutines.test.internal.TestMainDispatcherFactory", true, kotlinx.coroutines.internal.MainDispatcherFactory.class.getClassLoader()).getDeclaredConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]));
            } catch (java.lang.ClassNotFoundException e2) {
            }
            if (mainDispatcherFactory2 != null) {
                kotlinx.coroutines.internal.MainDispatcherFactory $this$loadMainDispatcherFactory_u24lambda_u241 = mainDispatcherFactory2;
                result.add($this$loadMainDispatcherFactory_u24lambda_u241);
            }
            return result;
        } catch (java.lang.Throwable th) {
            java.lang.ClassLoader classLoader2 = kotlinx.coroutines.internal.MainDispatcherFactory.class.getClassLoader();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(classLoader2, "getClassLoader(...)");
            return load(kotlinx.coroutines.internal.MainDispatcherFactory.class, classLoader2);
        }
    }

    private final kotlinx.coroutines.internal.MainDispatcherFactory createInstanceOf(java.lang.Class<kotlinx.coroutines.internal.MainDispatcherFactory> baseClass, java.lang.String serviceClass) {
        try {
            return baseClass.cast(java.lang.Class.forName(serviceClass, true, baseClass.getClassLoader()).getDeclaredConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]));
        } catch (java.lang.ClassNotFoundException e) {
            return null;
        }
    }

    private final <S> java.util.List<S> load(java.lang.Class<S> service, java.lang.ClassLoader loader) {
        try {
            return loadProviders$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(service, loader);
        } catch (java.lang.Throwable th) {
            java.util.ServiceLoader serviceLoaderLoad = java.util.ServiceLoader.load(service, loader);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(serviceLoaderLoad, "load(...)");
            return kotlin.collections.CollectionsKt.toList(serviceLoaderLoad);
        }
    }

    public final <S> java.util.List<S> loadProviders$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Class<S> service, java.lang.ClassLoader loader) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(service, "service");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(loader, "loader");
        java.lang.String fullServiceName = PREFIX + service.getName();
        java.util.Enumeration<java.net.URL> resources = loader.getResources(fullServiceName);
        kotlin.jvm.internal.Intrinsics.checkNotNull(resources);
        java.lang.Iterable list = java.util.Collections.list(resources);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(list, "list(...)");
        java.lang.Iterable $this$flatMap$iv = (java.util.List) list;
        java.util.Collection destination$iv$iv = new java.util.ArrayList();
        for (java.lang.Object element$iv$iv : $this$flatMap$iv) {
            java.net.URL it = (java.net.URL) element$iv$iv;
            kotlinx.coroutines.internal.FastServiceLoader fastServiceLoader = INSTANCE;
            kotlin.jvm.internal.Intrinsics.checkNotNull(it);
            java.lang.Iterable list$iv$iv = fastServiceLoader.parse(it);
            kotlin.collections.CollectionsKt.addAll(destination$iv$iv, list$iv$iv);
        }
        java.lang.Iterable providers = kotlin.collections.CollectionsKt.toSet((java.util.List) destination$iv$iv);
        if (!(!((java.util.Collection) providers).isEmpty())) {
            throw new java.lang.IllegalArgumentException("No providers were loaded with FastServiceLoader".toString());
        }
        java.lang.Iterable $this$map$iv = providers;
        java.util.Collection destination$iv$iv2 = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (java.lang.Object item$iv$iv : $this$map$iv) {
            destination$iv$iv2.add(INSTANCE.getProviderInstance((java.lang.String) item$iv$iv, loader, service));
        }
        return (java.util.List) destination$iv$iv2;
    }

    private final <S> S getProviderInstance(java.lang.String name, java.lang.ClassLoader loader, java.lang.Class<S> service) throws java.lang.ClassNotFoundException {
        java.lang.Class<?> cls = java.lang.Class.forName(name, false, loader);
        if (service.isAssignableFrom(cls)) {
            return service.cast(cls.getDeclaredConstructor(new java.lang.Class[0]).newInstance(new java.lang.Object[0]));
        }
        throw new java.lang.IllegalArgumentException(("Expected service of class " + service + ", but found " + cls).toString());
    }

    private final java.util.List<java.lang.String> parse(java.net.URL url) throws java.io.IOException {
        java.io.BufferedReader bufferedReader;
        java.lang.String path = url.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(path, "toString(...)");
        if (kotlin.text.StringsKt.startsWith$default(path, "jar", false, 2, (java.lang.Object) null)) {
            java.lang.String pathToJar = kotlin.text.StringsKt.substringBefore$default(kotlin.text.StringsKt.substringAfter$default(path, "jar:file:", (java.lang.String) null, 2, (java.lang.Object) null), '!', (java.lang.String) null, 2, (java.lang.Object) null);
            java.lang.String entry = kotlin.text.StringsKt.substringAfter$default(path, "!/", (java.lang.String) null, 2, (java.lang.Object) null);
            java.util.jar.JarFile $this$use$iv = new java.util.jar.JarFile(pathToJar, false);
            try {
                bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader($this$use$iv.getInputStream(new java.util.zip.ZipEntry(entry)), "UTF-8"));
                try {
                    java.io.BufferedReader r = bufferedReader;
                    java.util.List<java.lang.String> file = INSTANCE.parseFile(r);
                    kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
                    $this$use$iv.close();
                    return file;
                } finally {
                }
            } catch (java.lang.Throwable e$iv) {
                try {
                    throw e$iv;
                } catch (java.lang.Throwable e$iv2) {
                    try {
                        $this$use$iv.close();
                        throw e$iv2;
                    } catch (java.lang.Throwable closeException$iv) {
                        kotlin.ExceptionsKt.addSuppressed(e$iv, closeException$iv);
                        throw e$iv;
                    }
                }
            }
        } else {
            bufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream()));
            try {
                java.io.BufferedReader reader = bufferedReader;
                java.util.List<java.lang.String> file2 = INSTANCE.parseFile(reader);
                kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
                return file2;
            } catch (java.lang.Throwable th) {
                try {
                    throw th;
                } finally {
                }
            }
        }
    }

    private final <R> R use(java.util.jar.JarFile $this$use, kotlin.jvm.functions.Function1<? super java.util.jar.JarFile, ? extends R> function1) {
        try {
            R rInvoke = function1.invoke($this$use);
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            $this$use.close();
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            return rInvoke;
        } catch (java.lang.Throwable e) {
            try {
                throw e;
            } catch (java.lang.Throwable e2) {
                kotlin.jvm.internal.InlineMarker.finallyStart(1);
                try {
                    $this$use.close();
                    kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                    throw e2;
                } catch (java.lang.Throwable closeException) {
                    kotlin.ExceptionsKt.addSuppressed(e, closeException);
                    throw e;
                }
            }
        }
    }

    private final java.util.List<java.lang.String> parseFile(java.io.BufferedReader r) throws java.io.IOException {
        boolean z;
        java.util.Set names = new java.util.LinkedHashSet();
        while (true) {
            java.lang.String line = r.readLine();
            if (line == null) {
                return kotlin.collections.CollectionsKt.toList(names);
            }
            java.lang.String serviceName = kotlin.text.StringsKt.trim((java.lang.CharSequence) kotlin.text.StringsKt.substringBefore$default(line, "#", (java.lang.String) null, 2, (java.lang.Object) null)).toString();
            java.lang.String $this$all$iv = serviceName;
            int i = 0;
            while (true) {
                if (i >= $this$all$iv.length()) {
                    z = true;
                    break;
                }
                char element$iv = $this$all$iv.charAt(i);
                char it = (element$iv == '.' || java.lang.Character.isJavaIdentifierPart(element$iv)) ? (char) 1 : (char) 0;
                if (it == 0) {
                    z = false;
                    break;
                }
                i++;
            }
            if (!z) {
                throw new java.lang.IllegalArgumentException(("Illegal service provider class name: " + serviceName).toString());
            }
            if (serviceName.length() > 0) {
                names.add(serviceName);
            }
        }
    }
}
