package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: InlineList.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0081@\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0013\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00010\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\u001a\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0002HÖ\u0003¢\u0006\u0004\b\t\u0010\nJ'\u0010\u000b\u001a\u00020\f2\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\f0\u000eH\u0086\bø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0010J\u0010\u0010\u0011\u001a\u00020\u0012HÖ\u0001¢\u0006\u0004\b\u0013\u0010\u0014J$\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u0016\u001a\u00028\u0000H\u0086\u0002ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0017\u0010\u0018J\u0010\u0010\u0019\u001a\u00020\u001aHÖ\u0001¢\u0006\u0004\b\u001b\u0010\u001cR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0002X\u0082\u0004¢\u0006\u0002\n\u0000\u0088\u0001\u0003\u0092\u0001\u0004\u0018\u00010\u0002\u0082\u0002\u0012\n\u0005\b\u009920\u0001\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/internal/InlineList;", "E", "", "holder", "constructor-impl", "(Ljava/lang/Object;)Ljava/lang/Object;", "equals", "", "other", "equals-impl", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "forEachReversed", "", "action", "Lkotlin/Function1;", "forEachReversed-impl", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "hashCode-impl", "(Ljava/lang/Object;)I", "plus", "element", "plus-FjFbRPM", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "toString", "", "toString-impl", "(Ljava/lang/Object;)Ljava/lang/String;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class InlineList<E> {
    private final java.lang.Object holder;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlinx.coroutines.internal.InlineList m12853boximpl(java.lang.Object obj) {
        return new kotlinx.coroutines.internal.InlineList(obj);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static <E> java.lang.Object m12854constructorimpl(java.lang.Object obj) {
        return obj;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m12856equalsimpl(java.lang.Object obj, java.lang.Object obj2) {
        return (obj2 instanceof kotlinx.coroutines.internal.InlineList) && kotlin.jvm.internal.Intrinsics.areEqual(obj, ((kotlinx.coroutines.internal.InlineList) obj2).getHolder());
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m12857equalsimpl0(java.lang.Object obj, java.lang.Object obj2) {
        return kotlin.jvm.internal.Intrinsics.areEqual(obj, obj2);
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m12859hashCodeimpl(java.lang.Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m12861toStringimpl(java.lang.Object obj) {
        return "InlineList(holder=" + obj + ")";
    }

    public boolean equals(java.lang.Object obj) {
        return m12856equalsimpl(this.holder, obj);
    }

    public int hashCode() {
        return m12859hashCodeimpl(this.holder);
    }

    public java.lang.String toString() {
        return m12861toStringimpl(this.holder);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ java.lang.Object getHolder() {
        return this.holder;
    }

    private /* synthetic */ InlineList(java.lang.Object holder) {
        this.holder = holder;
    }

    /* JADX INFO: renamed from: constructor-impl$default, reason: not valid java name */
    public static /* synthetic */ java.lang.Object m12855constructorimpl$default(java.lang.Object obj, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 1) != 0) {
            obj = null;
        }
        return m12854constructorimpl(obj);
    }

    /* JADX INFO: renamed from: plus-FjFbRPM, reason: not valid java name */
    public static final java.lang.Object m12860plusFjFbRPM(java.lang.Object arg0, E e) {
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !(!(e instanceof java.util.List))) {
            throw new java.lang.AssertionError();
        }
        if (arg0 == null) {
            return m12854constructorimpl(e);
        }
        if (arg0 instanceof java.util.ArrayList) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(arg0, "null cannot be cast to non-null type java.util.ArrayList<E of kotlinx.coroutines.internal.InlineList>{ kotlin.collections.TypeAliasesKt.ArrayList<E of kotlinx.coroutines.internal.InlineList> }");
            ((java.util.ArrayList) arg0).add(e);
            return m12854constructorimpl(arg0);
        }
        java.util.ArrayList list = new java.util.ArrayList(4);
        list.add(arg0);
        list.add(e);
        return m12854constructorimpl(list);
    }

    /* JADX INFO: renamed from: forEachReversed-impl, reason: not valid java name */
    public static final void m12858forEachReversedimpl(java.lang.Object obj, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        if (obj == null) {
            return;
        }
        if (obj instanceof java.util.ArrayList) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type java.util.ArrayList<E of kotlinx.coroutines.internal.InlineList>{ kotlin.collections.TypeAliasesKt.ArrayList<E of kotlinx.coroutines.internal.InlineList> }");
            java.util.ArrayList arrayList = (java.util.ArrayList) obj;
            int size = arrayList.size();
            while (true) {
                size--;
                if (-1 < size) {
                    action.invoke((java.lang.Object) arrayList.get(size));
                } else {
                    return;
                }
            }
        } else {
            action.invoke(obj);
        }
    }
}
