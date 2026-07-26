package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineStart.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J@\u0010\b\u001a\u00020\t\"\u0004\b\u0000\u0010\n2\u001c\u0010\u000b\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\r\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\f2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\n0\rH\u0087\u0002¢\u0006\u0002\u0010\u0010JY\u0010\b\u001a\u00020\t\"\u0004\b\u0000\u0010\u0011\"\u0004\b\u0001\u0010\n2'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u0002H\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\r\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\u0012¢\u0006\u0002\b\u00132\u0006\u0010\u0014\u001a\u0002H\u00112\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\n0\rH\u0087\u0002¢\u0006\u0002\u0010\u0015R\u001a\u0010\u0003\u001a\u00020\u00048FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0005\u0010\u0006\u001a\u0004\b\u0003\u0010\u0007j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018j\u0002\b\u0019¨\u0006\u001a"}, d2 = {"Lkotlinx/coroutines/CoroutineStart;", "", "(Ljava/lang/String;I)V", "isLazy", "", "isLazy$annotations", "()V", "()Z", "invoke", "", "T", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "completion", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)V", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)V", "DEFAULT", "LAZY", "ATOMIC", "UNDISPATCHED", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public enum CoroutineStart {
    DEFAULT,
    LAZY,
    ATOMIC,
    UNDISPATCHED;

    private static final /* synthetic */ kotlin.enums.EnumEntries $ENTRIES = kotlin.enums.EnumEntriesKt.enumEntries($VALUES);

    /* JADX INFO: compiled from: CoroutineStart.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[kotlinx.coroutines.CoroutineStart.values().length];
            try {
                iArr[kotlinx.coroutines.CoroutineStart.DEFAULT.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[kotlinx.coroutines.CoroutineStart.ATOMIC.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[kotlinx.coroutines.CoroutineStart.UNDISPATCHED.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            try {
                iArr[kotlinx.coroutines.CoroutineStart.LAZY.ordinal()] = 4;
            } catch (java.lang.NoSuchFieldError e4) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static kotlin.enums.EnumEntries<kotlinx.coroutines.CoroutineStart> getEntries() {
        return $ENTRIES;
    }

    public static /* synthetic */ void isLazy$annotations() {
    }

    public final <T> void invoke(kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> block, kotlin.coroutines.Continuation<? super T> completion) throws java.lang.Throwable {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        switch (kotlinx.coroutines.CoroutineStart.WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable(block, completion);
                return;
            case 2:
                kotlin.coroutines.ContinuationKt.startCoroutine(block, completion);
                return;
            case 3:
                kotlinx.coroutines.intrinsics.UndispatchedKt.startCoroutineUndispatched(block, completion);
                return;
            case 4:
                return;
            default:
                throw new kotlin.NoWhenBranchMatchedException();
        }
    }

    public final <R, T> void invoke(kotlin.jvm.functions.Function2<? super R, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> block, R receiver, kotlin.coroutines.Continuation<? super T> completion) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        switch (kotlinx.coroutines.CoroutineStart.WhenMappings.$EnumSwitchMapping$0[ordinal()]) {
            case 1:
                kotlinx.coroutines.intrinsics.CancellableKt.startCoroutineCancellable$default(block, receiver, completion, null, 4, null);
                return;
            case 2:
                kotlin.coroutines.ContinuationKt.startCoroutine(block, receiver, completion);
                return;
            case 3:
                kotlinx.coroutines.intrinsics.UndispatchedKt.startCoroutineUndispatched(block, receiver, completion);
                return;
            case 4:
                return;
            default:
                throw new kotlin.NoWhenBranchMatchedException();
        }
    }

    public final boolean isLazy() {
        return this == LAZY;
    }
}
