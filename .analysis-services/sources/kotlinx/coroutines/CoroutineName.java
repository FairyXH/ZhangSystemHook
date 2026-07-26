package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineName.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\b\u0010\u000f\u001a\u00020\u0003H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, d2 = {"Lkotlinx/coroutines/CoroutineName;", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "name", "", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "Key", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class CoroutineName extends kotlin.coroutines.AbstractCoroutineContextElement {

    /* JADX INFO: renamed from: Key, reason: from kotlin metadata */
    public static final kotlinx.coroutines.CoroutineName.Companion INSTANCE = new kotlinx.coroutines.CoroutineName.Companion(null);
    private final java.lang.String name;

    public static /* synthetic */ kotlinx.coroutines.CoroutineName copy$default(kotlinx.coroutines.CoroutineName coroutineName, java.lang.String str, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = coroutineName.name;
        }
        return coroutineName.copy(str);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.String getName() {
        return this.name;
    }

    public final kotlinx.coroutines.CoroutineName copy(java.lang.String name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        return new kotlinx.coroutines.CoroutineName(name);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof kotlinx.coroutines.CoroutineName) && kotlin.jvm.internal.Intrinsics.areEqual(this.name, ((kotlinx.coroutines.CoroutineName) other).name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public final java.lang.String getName() {
        return this.name;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CoroutineName(java.lang.String name) {
        super(INSTANCE);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        this.name = name;
    }

    /* JADX INFO: renamed from: kotlinx.coroutines.CoroutineName$Key, reason: from kotlin metadata */
    /* JADX INFO: compiled from: CoroutineName.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Lkotlinx/coroutines/CoroutineName$Key;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlinx/coroutines/CoroutineName;", "()V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion implements kotlin.coroutines.CoroutineContext.Key<kotlinx.coroutines.CoroutineName> {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public java.lang.String toString() {
        return "CoroutineName(" + this.name + ")";
    }
}
