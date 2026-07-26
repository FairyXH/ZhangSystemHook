package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ExceptionsConstructor.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\u001a2\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0005j\u0002`\u0007\"\b\b\u0000\u0010\b*\u00020\u00062\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\b0\nH\u0002\u001a.\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\u0006\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0005j\u0002`\u00072\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0005H\u0002\u001a!\u0010\r\u001a\u0004\u0018\u0001H\b\"\b\b\u0000\u0010\b*\u00020\u00062\u0006\u0010\u000e\u001a\u0002H\bH\u0000¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\u0010\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\n2\b\b\u0002\u0010\u0011\u001a\u00020\u0003H\u0082\u0010\u001a\u0018\u0010\u0012\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\n2\u0006\u0010\u0013\u001a\u00020\u0003H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000*(\b\u0002\u0010\u0014\"\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u00052\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0006\u0012\u0004\u0018\u00010\u00060\u0005¨\u0006\u0015"}, d2 = {"ctorCache", "Lkotlinx/coroutines/internal/CtorCache;", "throwableFields", "", "createConstructor", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/Ctor;", "E", "clz", "Ljava/lang/Class;", "safeCtor", "block", "tryCopyException", "exception", "(Ljava/lang/Throwable;)Ljava/lang/Throwable;", "fieldsCount", "accumulator", "fieldsCountOrDefault", "defaultValue", "Ctor", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ExceptionsConstructorKt {
    private static final kotlinx.coroutines.internal.CtorCache ctorCache;
    private static final int throwableFields = fieldsCountOrDefault(java.lang.Throwable.class, -1);

    static {
        kotlinx.coroutines.internal.ClassValueCtorCache classValueCtorCache;
        try {
            classValueCtorCache = kotlinx.coroutines.internal.FastServiceLoaderKt.getANDROID_DETECTED() ? kotlinx.coroutines.internal.WeakMapCtorCache.INSTANCE : kotlinx.coroutines.internal.ClassValueCtorCache.INSTANCE;
        } catch (java.lang.Throwable th) {
            classValueCtorCache = kotlinx.coroutines.internal.WeakMapCtorCache.INSTANCE;
        }
        ctorCache = classValueCtorCache;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <E extends java.lang.Throwable> E tryCopyException(E exception) {
        java.lang.Object objM11307constructorimpl;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        if (exception instanceof kotlinx.coroutines.CopyableThrowable) {
            try {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                objM11307constructorimpl = kotlin.Result.m11307constructorimpl(((kotlinx.coroutines.CopyableThrowable) exception).createCopy());
            } catch (java.lang.Throwable th) {
                kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
            }
            if (kotlin.Result.m11313isFailureimpl(objM11307constructorimpl)) {
                objM11307constructorimpl = null;
            }
            return (E) objM11307constructorimpl;
        }
        return (E) ctorCache.get(exception.getClass()).invoke(exception);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <E extends java.lang.Throwable> kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> createConstructor(java.lang.Class<E> cls) {
        kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> function1;
        char c;
        kotlin.Pair pair;
        kotlin.jvm.functions.Function1 nullResult = new kotlin.jvm.functions.Function1() { // from class: kotlinx.coroutines.internal.ExceptionsConstructorKt$createConstructor$nullResult$1
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Void invoke(java.lang.Throwable it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                return null;
            }
        };
        char c2 = 0;
        if (throwableFields != fieldsCountOrDefault(cls, 0)) {
            return nullResult;
        }
        java.lang.Object[] constructors = cls.getConstructors();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(constructors, "getConstructors(...)");
        java.lang.Object[] $this$map$iv = constructors;
        java.util.Collection destination$iv$iv = new java.util.ArrayList($this$map$iv.length);
        int length = $this$map$iv.length;
        int i = 0;
        while (true) {
            java.lang.Object maxElem$iv = null;
            if (i >= length) {
                java.lang.Iterable $this$maxByOrNull$iv = (java.util.List) destination$iv$iv;
                java.util.Iterator iterator$iv = $this$maxByOrNull$iv.iterator();
                if (iterator$iv.hasNext()) {
                    maxElem$iv = iterator$iv.next();
                    if (iterator$iv.hasNext()) {
                        kotlin.Pair p0 = (kotlin.Pair) maxElem$iv;
                        int maxValue$iv = ((java.lang.Number) p0.getSecond()).intValue();
                        do {
                            java.lang.Object e$iv = iterator$iv.next();
                            kotlin.Pair p02 = (kotlin.Pair) e$iv;
                            int v$iv = ((java.lang.Number) p02.getSecond()).intValue();
                            if (maxValue$iv < v$iv) {
                                maxValue$iv = v$iv;
                                maxElem$iv = e$iv;
                            }
                        } while (iterator$iv.hasNext());
                    }
                }
                kotlin.Pair pair2 = (kotlin.Pair) maxElem$iv;
                return (pair2 == null || (function1 = (kotlin.jvm.functions.Function1) pair2.getFirst()) == null) ? nullResult : function1;
            }
            java.lang.Object item$iv$iv = $this$map$iv[i];
            final java.lang.reflect.Constructor<?> constructor = (java.lang.reflect.Constructor) item$iv$iv;
            java.lang.Class<?>[] parameterTypes = constructor.getParameterTypes();
            switch (parameterTypes.length) {
                case 0:
                    c = 0;
                    pair = kotlin.TuplesKt.to(safeCtor(new kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>() { // from class: kotlinx.coroutines.internal.ExceptionsConstructorKt$createConstructor$1$4
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @Override // kotlin.jvm.functions.Function1
                        public final java.lang.Throwable invoke(java.lang.Throwable e) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.reflect.InvocationTargetException {
                            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
                            java.lang.Object objNewInstance = constructor.newInstance(new java.lang.Object[0]);
                            kotlin.jvm.internal.Intrinsics.checkNotNull(objNewInstance, "null cannot be cast to non-null type kotlin.Throwable");
                            java.lang.Throwable it = (java.lang.Throwable) objNewInstance;
                            it.initCause(e);
                            return it;
                        }
                    }), 0);
                    break;
                case 1:
                    java.lang.Class<?> cls2 = parameterTypes[0];
                    if (kotlin.jvm.internal.Intrinsics.areEqual(cls2, java.lang.String.class)) {
                        pair = kotlin.TuplesKt.to(safeCtor(new kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>() { // from class: kotlinx.coroutines.internal.ExceptionsConstructorKt$createConstructor$1$2
                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            {
                                super(1);
                            }

                            @Override // kotlin.jvm.functions.Function1
                            public final java.lang.Throwable invoke(java.lang.Throwable e) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.reflect.InvocationTargetException {
                                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
                                java.lang.Object objNewInstance = constructor.newInstance(e.getMessage());
                                kotlin.jvm.internal.Intrinsics.checkNotNull(objNewInstance, "null cannot be cast to non-null type kotlin.Throwable");
                                java.lang.Throwable it = (java.lang.Throwable) objNewInstance;
                                it.initCause(e);
                                return it;
                            }
                        }), 2);
                        c = 0;
                    } else if (!kotlin.jvm.internal.Intrinsics.areEqual(cls2, java.lang.Throwable.class)) {
                        pair = kotlin.TuplesKt.to(null, -1);
                        c = 0;
                    } else {
                        pair = kotlin.TuplesKt.to(safeCtor(new kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>() { // from class: kotlinx.coroutines.internal.ExceptionsConstructorKt$createConstructor$1$3
                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            {
                                super(1);
                            }

                            @Override // kotlin.jvm.functions.Function1
                            public final java.lang.Throwable invoke(java.lang.Throwable e) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.reflect.InvocationTargetException {
                                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
                                java.lang.Object objNewInstance = constructor.newInstance(e);
                                kotlin.jvm.internal.Intrinsics.checkNotNull(objNewInstance, "null cannot be cast to non-null type kotlin.Throwable");
                                return (java.lang.Throwable) objNewInstance;
                            }
                        }), 1);
                        c = 0;
                    }
                    break;
                case 2:
                    if (kotlin.jvm.internal.Intrinsics.areEqual(parameterTypes[c2], java.lang.String.class) && kotlin.jvm.internal.Intrinsics.areEqual(parameterTypes[1], java.lang.Throwable.class)) {
                        pair = kotlin.TuplesKt.to(safeCtor(new kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>() { // from class: kotlinx.coroutines.internal.ExceptionsConstructorKt$createConstructor$1$1
                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            {
                                super(1);
                            }

                            @Override // kotlin.jvm.functions.Function1
                            public final java.lang.Throwable invoke(java.lang.Throwable e) throws java.lang.IllegalAccessException, java.lang.InstantiationException, java.lang.reflect.InvocationTargetException {
                                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
                                java.lang.Object objNewInstance = constructor.newInstance(e.getMessage(), e);
                                kotlin.jvm.internal.Intrinsics.checkNotNull(objNewInstance, "null cannot be cast to non-null type kotlin.Throwable");
                                return (java.lang.Throwable) objNewInstance;
                            }
                        }), 3);
                        c = 0;
                    } else {
                        pair = kotlin.TuplesKt.to(null, -1);
                        c = 0;
                    }
                    break;
                default:
                    c = c2;
                    pair = kotlin.TuplesKt.to(null, -1);
                    break;
            }
            destination$iv$iv.add(pair);
            i++;
            c2 = c;
        }
    }

    private static final kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> safeCtor(final kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends java.lang.Throwable> function1) {
        return new kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable>() { // from class: kotlinx.coroutines.internal.ExceptionsConstructorKt.safeCtor.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Throwable invoke(java.lang.Throwable e) {
                java.lang.Object objM11307constructorimpl;
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(e, "e");
                kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Throwable> function12 = function1;
                try {
                    kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                    java.lang.Throwable result = function12.invoke(e);
                    objM11307constructorimpl = kotlin.Result.m11307constructorimpl((kotlin.jvm.internal.Intrinsics.areEqual(e.getMessage(), result.getMessage()) || kotlin.jvm.internal.Intrinsics.areEqual(result.getMessage(), e.toString())) ? result : null);
                } catch (java.lang.Throwable th) {
                    kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                    objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
                }
                return (java.lang.Throwable) (kotlin.Result.m11313isFailureimpl(objM11307constructorimpl) ? null : objM11307constructorimpl);
            }
        };
    }

    private static final int fieldsCountOrDefault(java.lang.Class<?> cls, int defaultValue) {
        java.lang.Object objM11307constructorimpl;
        kotlin.jvm.JvmClassMappingKt.getKotlinClass(cls);
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(java.lang.Integer.valueOf(fieldsCount$default(cls, 0, 1, null)));
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        java.lang.Integer numValueOf = java.lang.Integer.valueOf(defaultValue);
        if (kotlin.Result.m11313isFailureimpl(objM11307constructorimpl)) {
            objM11307constructorimpl = numValueOf;
        }
        return ((java.lang.Number) objM11307constructorimpl).intValue();
    }

    static /* synthetic */ int fieldsCount$default(java.lang.Class cls, int i, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return fieldsCount(cls, i);
    }

    private static final int fieldsCount(java.lang.Class<?> cls, int accumulator) {
        while (true) {
            java.lang.Object[] declaredFields = cls.getDeclaredFields();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(declaredFields, "getDeclaredFields(...)");
            java.lang.Object[] $this$count$iv = declaredFields;
            int count$iv = 0;
            for (java.lang.Object element$iv : $this$count$iv) {
                java.lang.reflect.Field it = (java.lang.reflect.Field) element$iv;
                if (!java.lang.reflect.Modifier.isStatic(it.getModifiers())) {
                    count$iv++;
                }
            }
            int fieldsCount = count$iv;
            int totalFields = accumulator + fieldsCount;
            java.lang.Class<? super java.lang.Object> superclass = cls.getSuperclass();
            if (superclass == null) {
                return totalFields;
            }
            cls = superclass;
            accumulator = totalFields;
        }
    }
}
