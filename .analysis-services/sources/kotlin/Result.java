package kotlin;

/* JADX INFO: compiled from: Result.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0087@\u0018\u0000 \"*\u0006\b\u0000\u0010\u0001 \u00012\u00060\u0002j\u0002`\u0003:\u0002\"#B\u0013\b\u0001\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0004\b\u0006\u0010\u0007J\u001a\u0010\u0010\u001a\u00020\t2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0005HÖ\u0003¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0014\u001a\u0004\u0018\u00010\u0015¢\u0006\u0004\b\u0016\u0010\u0017J\u0012\u0010\u0018\u001a\u0004\u0018\u00018\u0000H\u0087\b¢\u0006\u0004\b\u0019\u0010\u0007J\u0010\u0010\u001a\u001a\u00020\u001bHÖ\u0001¢\u0006\u0004\b\u001c\u0010\u001dJ\u000f\u0010\u001e\u001a\u00020\u001fH\u0016¢\u0006\u0004\b \u0010!R\u0011\u0010\b\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u000e\u0010\u000f\u0088\u0001\u0004\u0092\u0001\u0004\u0018\u00010\u0005¨\u0006$"}, d2 = {"Lkotlin/Result;", "T", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "value", "", "constructor-impl", "(Ljava/lang/Object;)Ljava/lang/Object;", "isFailure", "", "isFailure-impl", "(Ljava/lang/Object;)Z", "isSuccess", "isSuccess-impl", "getValue$annotations", "()V", "equals", "other", "equals-impl", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "exceptionOrNull", "", "exceptionOrNull-impl", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "getOrNull", "getOrNull-impl", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "hashCode-impl", "(Ljava/lang/Object;)I", "toString", "", "toString-impl", "(Ljava/lang/Object;)Ljava/lang/String;", "Companion", "Failure", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class Result<T> implements java.io.Serializable {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.Result.Companion INSTANCE = new kotlin.Result.Companion(null);
    private final java.lang.Object value;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlin.Result m11306boximpl(java.lang.Object obj) {
        return new kotlin.Result(obj);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static <T> java.lang.Object m11307constructorimpl(java.lang.Object obj) {
        return obj;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m11308equalsimpl(java.lang.Object obj, java.lang.Object obj2) {
        return (obj2 instanceof kotlin.Result) && kotlin.jvm.internal.Intrinsics.areEqual(obj, ((kotlin.Result) obj2).getValue());
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m11309equalsimpl0(java.lang.Object obj, java.lang.Object obj2) {
        return kotlin.jvm.internal.Intrinsics.areEqual(obj, obj2);
    }

    public static /* synthetic */ void getValue$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m11312hashCodeimpl(java.lang.Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public boolean equals(java.lang.Object obj) {
        return m11308equalsimpl(this.value, obj);
    }

    public int hashCode() {
        return m11312hashCodeimpl(this.value);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ java.lang.Object getValue() {
        return this.value;
    }

    private /* synthetic */ Result(java.lang.Object value) {
        this.value = value;
    }

    /* JADX INFO: renamed from: isSuccess-impl, reason: not valid java name */
    public static final boolean m11314isSuccessimpl(java.lang.Object arg0) {
        return !(arg0 instanceof kotlin.Result.Failure);
    }

    /* JADX INFO: renamed from: isFailure-impl, reason: not valid java name */
    public static final boolean m11313isFailureimpl(java.lang.Object arg0) {
        return arg0 instanceof kotlin.Result.Failure;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: getOrNull-impl, reason: not valid java name */
    private static final T m11311getOrNullimpl(java.lang.Object obj) {
        if (m11313isFailureimpl(obj)) {
            return null;
        }
        return obj;
    }

    /* JADX INFO: renamed from: exceptionOrNull-impl, reason: not valid java name */
    public static final java.lang.Throwable m11310exceptionOrNullimpl(java.lang.Object arg0) {
        if (arg0 instanceof kotlin.Result.Failure) {
            return ((kotlin.Result.Failure) arg0).exception;
        }
        return null;
    }

    public java.lang.String toString() {
        return m11315toStringimpl(this.value);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m11315toStringimpl(java.lang.Object arg0) {
        return arg0 instanceof kotlin.Result.Failure ? ((kotlin.Result.Failure) arg0).toString() : "Success(" + arg0 + ')';
    }

    /* JADX INFO: compiled from: Result.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0001\u0010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0087\b¢\u0006\u0002\u0010\bJ\"\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0001\u0010\u00052\u0006\u0010\n\u001a\u0002H\u0005H\u0087\b¢\u0006\u0002\u0010\u000b¨\u0006\f"}, d2 = {"Lkotlin/Result$Companion;", "", "()V", "failure", "Lkotlin/Result;", "T", "exception", "", "(Ljava/lang/Throwable;)Ljava/lang/Object;", com.android.server.content.SyncStorageEngine.MESG_SUCCESS, "value", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        private final <T> java.lang.Object success(T value) {
            return kotlin.Result.m11307constructorimpl(value);
        }

        private final <T> java.lang.Object failure(java.lang.Throwable exception) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
            return kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(exception));
        }
    }

    /* JADX INFO: compiled from: Result.kt */
    @kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0096\u0002J\b\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0016R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlin/Result$Failure;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "exception", "", "(Ljava/lang/Throwable;)V", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Failure implements java.io.Serializable {
        public final java.lang.Throwable exception;

        public Failure(java.lang.Throwable exception) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
            this.exception = exception;
        }

        public boolean equals(java.lang.Object other) {
            return (other instanceof kotlin.Result.Failure) && kotlin.jvm.internal.Intrinsics.areEqual(this.exception, ((kotlin.Result.Failure) other).exception);
        }

        public int hashCode() {
            return this.exception.hashCode();
        }

        public java.lang.String toString() {
            return "Failure(" + this.exception + ')';
        }
    }
}
