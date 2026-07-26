package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: Channel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u000b\n\u0002\u0010\u0003\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0087@\u0018\u0000 %*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002:\u0003$%&B\u0013\b\u0001\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u001a\u0010\u0010\u001a\u00020\t2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0002HÖ\u0003¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0014\u001a\u0004\u0018\u00010\u0015¢\u0006\u0004\b\u0016\u0010\u0017J\u000f\u0010\u0018\u001a\u0004\u0018\u00018\u0000¢\u0006\u0004\b\u0019\u0010\u0005J\r\u0010\u001a\u001a\u00028\u0000¢\u0006\u0004\b\u001b\u0010\u0005J\u0010\u0010\u001c\u001a\u00020\u001dHÖ\u0001¢\u0006\u0004\b\u001e\u0010\u001fJ\u000f\u0010 \u001a\u00020!H\u0016¢\u0006\u0004\b\"\u0010#R\u0018\u0010\u0003\u001a\u0004\u0018\u00010\u00028\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u000e\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u000b\u0088\u0001\u0003\u0092\u0001\u0004\u0018\u00010\u0002¨\u0006'"}, d2 = {"Lkotlinx/coroutines/channels/ChannelResult;", "T", "", "holder", "constructor-impl", "(Ljava/lang/Object;)Ljava/lang/Object;", "getHolder$annotations", "()V", "isClosed", "", "isClosed-impl", "(Ljava/lang/Object;)Z", "isFailure", "isFailure-impl", "isSuccess", "isSuccess-impl", "equals", "other", "equals-impl", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "exceptionOrNull", "", "exceptionOrNull-impl", "(Ljava/lang/Object;)Ljava/lang/Throwable;", "getOrNull", "getOrNull-impl", "getOrThrow", "getOrThrow-impl", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "hashCode-impl", "(Ljava/lang/Object;)I", "toString", "", "toString-impl", "(Ljava/lang/Object;)Ljava/lang/String;", "Closed", "Companion", "Failed", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class ChannelResult<T> {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlinx.coroutines.channels.ChannelResult.Companion INSTANCE = new kotlinx.coroutines.channels.ChannelResult.Companion(null);
    private static final kotlinx.coroutines.channels.ChannelResult.Failed failed = new kotlinx.coroutines.channels.ChannelResult.Failed();
    private final java.lang.Object holder;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlinx.coroutines.channels.ChannelResult m12819boximpl(java.lang.Object obj) {
        return new kotlinx.coroutines.channels.ChannelResult(obj);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static <T> java.lang.Object m12820constructorimpl(java.lang.Object obj) {
        return obj;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m12821equalsimpl(java.lang.Object obj, java.lang.Object obj2) {
        return (obj2 instanceof kotlinx.coroutines.channels.ChannelResult) && kotlin.jvm.internal.Intrinsics.areEqual(obj, ((kotlinx.coroutines.channels.ChannelResult) obj2).getHolder());
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m12822equalsimpl0(java.lang.Object obj, java.lang.Object obj2) {
        return kotlin.jvm.internal.Intrinsics.areEqual(obj, obj2);
    }

    public static /* synthetic */ void getHolder$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m12826hashCodeimpl(java.lang.Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public boolean equals(java.lang.Object obj) {
        return m12821equalsimpl(this.holder, obj);
    }

    public int hashCode() {
        return m12826hashCodeimpl(this.holder);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ java.lang.Object getHolder() {
        return this.holder;
    }

    private /* synthetic */ ChannelResult(java.lang.Object holder) {
        this.holder = holder;
    }

    /* JADX INFO: renamed from: isSuccess-impl, reason: not valid java name */
    public static final boolean m12829isSuccessimpl(java.lang.Object arg0) {
        return !(arg0 instanceof kotlinx.coroutines.channels.ChannelResult.Failed);
    }

    /* JADX INFO: renamed from: isFailure-impl, reason: not valid java name */
    public static final boolean m12828isFailureimpl(java.lang.Object arg0) {
        return arg0 instanceof kotlinx.coroutines.channels.ChannelResult.Failed;
    }

    /* JADX INFO: renamed from: isClosed-impl, reason: not valid java name */
    public static final boolean m12827isClosedimpl(java.lang.Object arg0) {
        return arg0 instanceof kotlinx.coroutines.channels.ChannelResult.Closed;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: getOrNull-impl, reason: not valid java name */
    public static final T m12824getOrNullimpl(java.lang.Object obj) {
        if (obj instanceof kotlinx.coroutines.channels.ChannelResult.Failed) {
            return null;
        }
        return obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: getOrThrow-impl, reason: not valid java name */
    public static final T m12825getOrThrowimpl(java.lang.Object obj) throws java.lang.Throwable {
        if (!(obj instanceof kotlinx.coroutines.channels.ChannelResult.Failed)) {
            return obj;
        }
        if (!(obj instanceof kotlinx.coroutines.channels.ChannelResult.Closed) || ((kotlinx.coroutines.channels.ChannelResult.Closed) obj).cause == null) {
            throw new java.lang.IllegalStateException(("Trying to call 'getOrThrow' on a failed channel result: " + obj).toString());
        }
        throw ((kotlinx.coroutines.channels.ChannelResult.Closed) obj).cause;
    }

    /* JADX INFO: renamed from: exceptionOrNull-impl, reason: not valid java name */
    public static final java.lang.Throwable m12823exceptionOrNullimpl(java.lang.Object arg0) {
        kotlinx.coroutines.channels.ChannelResult.Closed closed = arg0 instanceof kotlinx.coroutines.channels.ChannelResult.Closed ? (kotlinx.coroutines.channels.ChannelResult.Closed) arg0 : null;
        if (closed != null) {
            return closed.cause;
        }
        return null;
    }

    /* JADX INFO: compiled from: Channel.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0010\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, d2 = {"Lkotlinx/coroutines/channels/ChannelResult$Failed;", "", "()V", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static class Failed {
        public java.lang.String toString() {
            return "Failed";
        }
    }

    /* JADX INFO: compiled from: Channel.kt */
    @kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0000\u0018\u00002\u00020\u0001B\u000f\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\u0013\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0096\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016R\u0012\u0010\u0002\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/channels/ChannelResult$Closed;", "Lkotlinx/coroutines/channels/ChannelResult$Failed;", "cause", "", "(Ljava/lang/Throwable;)V", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Closed extends kotlinx.coroutines.channels.ChannelResult.Failed {
        public final java.lang.Throwable cause;

        public Closed(java.lang.Throwable cause) {
            this.cause = cause;
        }

        public boolean equals(java.lang.Object other) {
            return (other instanceof kotlinx.coroutines.channels.ChannelResult.Closed) && kotlin.jvm.internal.Intrinsics.areEqual(this.cause, ((kotlinx.coroutines.channels.ChannelResult.Closed) other).cause);
        }

        public int hashCode() {
            java.lang.Throwable th = this.cause;
            if (th != null) {
                return th.hashCode();
            }
            return 0;
        }

        @Override // kotlinx.coroutines.channels.ChannelResult.Failed
        public java.lang.String toString() {
            return "Closed(" + this.cause + ")";
        }
    }

    /* JADX INFO: compiled from: Channel.kt */
    @kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\n\b\u0087\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J+\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0001\u0010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0007ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\n\u0010\u000bJ!\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0001\u0010\u0007H\u0007ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\r\u0010\u000eJ)\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0001\u0010\u00072\u0006\u0010\u0010\u001a\u0002H\u0007H\u0007ø\u0001\u0000ø\u0001\u0001¢\u0006\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/channels/ChannelResult$Companion;", "", "()V", "failed", "Lkotlinx/coroutines/channels/ChannelResult$Failed;", "closed", "Lkotlinx/coroutines/channels/ChannelResult;", "E", "cause", "", "closed-JP2dKIU", "(Ljava/lang/Throwable;)Ljava/lang/Object;", "failure", "failure-PtdJZtk", "()Ljava/lang/Object;", com.android.server.content.SyncStorageEngine.MESG_SUCCESS, "value", "success-JP2dKIU", "(Ljava/lang/Object;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: success-JP2dKIU, reason: not valid java name */
        public final <E> java.lang.Object m12834successJP2dKIU(E value) {
            return kotlinx.coroutines.channels.ChannelResult.m12820constructorimpl(value);
        }

        /* JADX INFO: renamed from: failure-PtdJZtk, reason: not valid java name */
        public final <E> java.lang.Object m12833failurePtdJZtk() {
            return kotlinx.coroutines.channels.ChannelResult.m12820constructorimpl(kotlinx.coroutines.channels.ChannelResult.failed);
        }

        /* JADX INFO: renamed from: closed-JP2dKIU, reason: not valid java name */
        public final <E> java.lang.Object m12832closedJP2dKIU(java.lang.Throwable cause) {
            return kotlinx.coroutines.channels.ChannelResult.m12820constructorimpl(new kotlinx.coroutines.channels.ChannelResult.Closed(cause));
        }
    }

    public java.lang.String toString() {
        return m12830toStringimpl(this.holder);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m12830toStringimpl(java.lang.Object arg0) {
        return arg0 instanceof kotlinx.coroutines.channels.ChannelResult.Closed ? ((kotlinx.coroutines.channels.ChannelResult.Closed) arg0).toString() : "Value(" + arg0 + ")";
    }
}
