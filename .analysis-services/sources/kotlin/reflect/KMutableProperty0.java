package kotlin.reflect;

/* JADX INFO: compiled from: KProperty.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\bf\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003:\u0001\u000eJ\u0015\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00028\u0000H&¢\u0006\u0002\u0010\rR\u001e\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005X¦\u0004¢\u0006\f\u0012\u0004\b\u0006\u0010\u0007\u001a\u0004\b\b\u0010\t¨\u0006\u000f"}, d2 = {"Lkotlin/reflect/KMutableProperty0;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/reflect/KProperty0;", "Lkotlin/reflect/KMutableProperty;", "setter", "Lkotlin/reflect/KMutableProperty0$Setter;", "getSetter$annotations", "()V", "getSetter", "()Lkotlin/reflect/KMutableProperty0$Setter;", "set", "", "value", "(Ljava/lang/Object;)V", "Setter", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface KMutableProperty0<V> extends kotlin.reflect.KProperty0<V>, kotlin.reflect.KMutableProperty<V> {

    /* JADX INFO: compiled from: KProperty.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void getSetter$annotations() {
        }
    }

    /* JADX INFO: compiled from: KProperty.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\bf\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u00020\u00040\u0003¨\u0006\u0005"}, d2 = {"Lkotlin/reflect/KMutableProperty0$Setter;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/reflect/KMutableProperty$Setter;", "Lkotlin/Function1;", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public interface Setter<V> extends kotlin.reflect.KMutableProperty.Setter<V>, kotlin.jvm.functions.Function1<V, kotlin.Unit> {
    }

    @Override // kotlin.reflect.KMutableProperty
    kotlin.reflect.KMutableProperty0.Setter<V> getSetter();

    void set(V value);
}
