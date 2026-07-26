package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: Preconditions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0001\n\u0002\b\u0004\u001a\u001c\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a-\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a/\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\bH\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\t\u001a@\u0010\u0007\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\n\u001a\u0011\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0006H\u0087\b\u001a\u001c\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a-\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\b\n\u0006\b\u0000\u001a\u0002\u0010\u0001\u001a/\u0010\u000f\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\bH\u0087\b\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\t\u001a@\u0010\u000f\u001a\u0002H\b\"\b\b\u0000\u0010\b*\u00020\u00062\b\u0010\u0002\u001a\u0004\u0018\u0001H\b2\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0000\u001a\u0004\b\u0003\u0010\u0001¢\u0006\u0002\u0010\n\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0010"}, d2 = {"check", "", "value", "", "lazyMessage", "Lkotlin/Function0;", "", "checkNotNull", "T", "(Ljava/lang/Object;)Ljava/lang/Object;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "error", "", "message", "require", "requireNotNull", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/PreconditionsKt")
class PreconditionsKt__PreconditionsKt extends com.android.server.permission.jarjar.kotlin.PreconditionsKt__AssertionsJVMKt {
    private static final void require(boolean value) {
        if (!value) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
    }

    private static final void require(boolean value, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends java.lang.Object> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "lazyMessage");
        if (!value) {
            java.lang.Object message = function0.invoke();
            throw new java.lang.IllegalArgumentException(message.toString());
        }
    }

    private static final <T> T requireNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new java.lang.IllegalArgumentException("Required value was null.".toString());
    }

    private static final <T> T requireNotNull(T t, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends java.lang.Object> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "lazyMessage");
        if (t == null) {
            java.lang.Object message = function0.invoke();
            throw new java.lang.IllegalArgumentException(message.toString());
        }
        return t;
    }

    private static final void check(boolean value) {
        if (!value) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
    }

    private static final void check(boolean value, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends java.lang.Object> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "lazyMessage");
        if (!value) {
            java.lang.Object message = function0.invoke();
            throw new java.lang.IllegalStateException(message.toString());
        }
    }

    private static final <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new java.lang.IllegalStateException("Required value was null.".toString());
    }

    private static final <T> T checkNotNull(T t, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends java.lang.Object> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "lazyMessage");
        if (t == null) {
            java.lang.Object message = function0.invoke();
            throw new java.lang.IllegalStateException(message.toString());
        }
        return t;
    }

    private static final java.lang.Void error(java.lang.Object message) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(message, "message");
        throw new java.lang.IllegalStateException(message.toString());
    }
}
