package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class FunctionalUtils {

    @java.lang.FunctionalInterface
    public interface ThrowingChecked2Consumer<Input, ExceptionOne extends java.lang.Exception, ExceptionTwo extends java.lang.Exception> {
        void accept(Input input) throws java.lang.Exception;
    }

    @java.lang.FunctionalInterface
    public interface ThrowingCheckedConsumer<Input, ExceptionType extends java.lang.Exception> {
        void accept(Input input) throws java.lang.Exception;
    }

    @java.lang.FunctionalInterface
    public interface ThrowingCheckedFunction<Input, Output, ExceptionType extends java.lang.Exception> {
        Output apply(Input input) throws java.lang.Exception;
    }

    @java.lang.FunctionalInterface
    public interface ThrowingCheckedSupplier<Output, ExceptionType extends java.lang.Exception> {
        Output get() throws java.lang.Exception;
    }

    private FunctionalUtils() {
    }

    public static <T> java.util.function.Consumer<T> uncheckExceptions(com.android.internal.util.jobs.FunctionalUtils.ThrowingConsumer<T> action) {
        return action;
    }

    public static <I, O> java.util.function.Function<I, O> uncheckExceptions(com.android.internal.util.jobs.FunctionalUtils.ThrowingFunction<I, O> action) {
        return action;
    }

    public static java.lang.Runnable uncheckExceptions(com.android.internal.util.jobs.FunctionalUtils.ThrowingRunnable action) {
        return action;
    }

    public static <A, B> java.util.function.BiConsumer<A, B> uncheckExceptions(com.android.internal.util.jobs.FunctionalUtils.ThrowingBiConsumer<A, B> action) {
        return action;
    }

    public static <T> java.util.function.Supplier<T> uncheckExceptions(com.android.internal.util.jobs.FunctionalUtils.ThrowingSupplier<T> action) {
        return action;
    }

    public static <T> java.util.function.Consumer<T> ignoreRemoteException(com.android.internal.util.jobs.FunctionalUtils.RemoteExceptionIgnoringConsumer<T> action) {
        return action;
    }

    public static java.lang.Runnable handleExceptions(final com.android.internal.util.jobs.FunctionalUtils.ThrowingRunnable r, final java.util.function.Consumer<java.lang.Throwable> handler) {
        return new java.lang.Runnable() { // from class: com.android.internal.util.jobs.FunctionalUtils$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.internal.util.jobs.FunctionalUtils.lambda$handleExceptions$0(r, handler);
            }
        };
    }

    static /* synthetic */ void lambda$handleExceptions$0(com.android.internal.util.jobs.FunctionalUtils.ThrowingRunnable r, java.util.function.Consumer handler) {
        try {
            r.run();
        } catch (java.lang.Throwable t) {
            handler.accept(t);
        }
    }

    @java.lang.FunctionalInterface
    public interface ThrowingRunnable extends java.lang.Runnable {
        void runOrThrow() throws java.lang.Exception;

        @Override // java.lang.Runnable
        default void run() {
            try {
                runOrThrow();
            } catch (java.lang.Exception ex) {
                throw android.util.ExceptionUtils.propagate(ex);
            }
        }
    }

    @java.lang.FunctionalInterface
    public interface ThrowingSupplier<T> extends java.util.function.Supplier<T> {
        T getOrThrow() throws java.lang.Exception;

        @Override // java.util.function.Supplier
        default T get() {
            try {
                return getOrThrow();
            } catch (java.lang.Exception ex) {
                throw android.util.ExceptionUtils.propagate(ex);
            }
        }
    }

    @java.lang.FunctionalInterface
    public interface ThrowingConsumer<T> extends java.util.function.Consumer<T> {
        void acceptOrThrow(T t) throws java.lang.Exception;

        @Override // java.util.function.Consumer
        default void accept(T t) {
            try {
                acceptOrThrow(t);
            } catch (java.lang.Exception ex) {
                throw android.util.ExceptionUtils.propagate(ex);
            }
        }
    }

    @java.lang.FunctionalInterface
    public interface RemoteExceptionIgnoringConsumer<T> extends java.util.function.Consumer<T> {
        void acceptOrThrow(T t) throws android.os.RemoteException;

        @Override // java.util.function.Consumer
        default void accept(T t) {
            try {
                acceptOrThrow(t);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    @java.lang.FunctionalInterface
    public interface ThrowingFunction<T, R> extends java.util.function.Function<T, R> {
        R applyOrThrow(T t) throws java.lang.Exception;

        @Override // java.util.function.Function
        default R apply(T t) {
            try {
                return applyOrThrow(t);
            } catch (java.lang.Exception ex) {
                throw android.util.ExceptionUtils.propagate(ex);
            }
        }
    }

    @java.lang.FunctionalInterface
    public interface ThrowingBiFunction<T, U, R> extends java.util.function.BiFunction<T, U, R> {
        R applyOrThrow(T t, U u) throws java.lang.Exception;

        @Override // java.util.function.BiFunction
        default R apply(T t, U u) {
            try {
                return applyOrThrow(t, u);
            } catch (java.lang.Exception ex) {
                throw android.util.ExceptionUtils.propagate(ex);
            }
        }
    }

    @java.lang.FunctionalInterface
    public interface ThrowingBiConsumer<A, B> extends java.util.function.BiConsumer<A, B> {
        void acceptOrThrow(A a, B b) throws java.lang.Exception;

        @Override // java.util.function.BiConsumer
        default void accept(A a, B b) {
            try {
                acceptOrThrow(a, b);
            } catch (java.lang.Exception ex) {
                throw android.util.ExceptionUtils.propagate(ex);
            }
        }
    }

    public static java.lang.String getLambdaName(java.lang.Object function) {
        int firstDollarIdx;
        java.lang.String fullFunction = function.toString();
        int endPkgIdx = fullFunction.indexOf("-$$");
        if (endPkgIdx == -1 || (firstDollarIdx = fullFunction.indexOf(36, endPkgIdx + 3)) == -1) {
            return fullFunction;
        }
        int endClassIdx = fullFunction.indexOf(36, firstDollarIdx + 1);
        return endClassIdx == -1 ? fullFunction.substring(0, endPkgIdx - 1) + "$Lambda" : fullFunction.substring(0, endPkgIdx) + fullFunction.substring(firstDollarIdx + 1, endClassIdx) + "$Lambda";
    }
}
