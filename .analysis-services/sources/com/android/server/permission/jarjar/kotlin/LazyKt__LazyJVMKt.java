package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: LazyJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a*\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004\u001a(\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¨\u0006\t"}, d2 = {"lazy", "Lkotlin/Lazy;", "T", "initializer", "Lkotlin/Function0;", "lock", "", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "Lkotlin/LazyThreadSafetyMode;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/LazyKt")
public class LazyKt__LazyJVMKt {

    /* JADX INFO: compiled from: LazyJVM.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[com.android.server.permission.jarjar.kotlin.LazyThreadSafetyMode.values().length];
            try {
                iArr[com.android.server.permission.jarjar.kotlin.LazyThreadSafetyMode.SYNCHRONIZED.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.LazyThreadSafetyMode.PUBLICATION.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.LazyThreadSafetyMode.NONE.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final <T> com.android.server.permission.jarjar.kotlin.Lazy<T> lazy(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "initializer");
        com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker = null;
        return new com.android.server.permission.jarjar.kotlin.SynchronizedLazyImpl(function0, defaultConstructorMarker, 2, defaultConstructorMarker);
    }

    public static final <T> com.android.server.permission.jarjar.kotlin.Lazy<T> lazy(com.android.server.permission.jarjar.kotlin.LazyThreadSafetyMode mode, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mode, com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "initializer");
        switch (com.android.server.permission.jarjar.kotlin.LazyKt__LazyJVMKt.WhenMappings.$EnumSwitchMapping$0[mode.ordinal()]) {
            case 1:
                com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker = null;
                return new com.android.server.permission.jarjar.kotlin.SynchronizedLazyImpl(function0, defaultConstructorMarker, 2, defaultConstructorMarker);
            case 2:
                return new com.android.server.permission.jarjar.kotlin.SafePublicationLazyImpl(function0);
            case 3:
                return new com.android.server.permission.jarjar.kotlin.UnsafeLazyImpl(function0);
            default:
                throw new com.android.server.permission.jarjar.kotlin.NoWhenBranchMatchedException();
        }
    }

    public static final <T> com.android.server.permission.jarjar.kotlin.Lazy<T> lazy(java.lang.Object lock, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "initializer");
        return new com.android.server.permission.jarjar.kotlin.SynchronizedLazyImpl(function0, lock);
    }
}
