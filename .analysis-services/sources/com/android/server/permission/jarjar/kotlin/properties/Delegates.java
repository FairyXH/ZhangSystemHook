package com.android.server.permission.jarjar.kotlin.properties;

/* JADX INFO: compiled from: Delegates.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0000\u0010\u0005*\u00020\u0001J\u0080\u0001\u0010\u0006\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u00052\u0006\u0010\u0007\u001a\u0002H\u00052Q\b\u0004\u0010\b\u001aK\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\n¢\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0013\u0012\u0011H\u0005¢\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u0011H\u0005¢\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00100\tH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0011J\u0080\u0001\u0010\u0012\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0001\u0012\u0004\u0012\u0002H\u00050\u0004\"\u0004\b\u0000\u0010\u00052\u0006\u0010\u0007\u001a\u0002H\u00052Q\b\u0004\u0010\b\u001aK\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\n¢\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\r\u0012\u0013\u0012\u0011H\u0005¢\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000e\u0012\u0013\u0012\u0011H\u0005¢\u0006\f\b\u000b\u0012\b\b\f\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00130\tH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u0011\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0014"}, d2 = {"Lkotlin/properties/Delegates;", "", "()V", "notNull", "Lkotlin/properties/ReadWriteProperty;", "T", "observable", "initialValue", "onChange", "Lkotlin/Function3;", "Lkotlin/reflect/KProperty;", "Lkotlin/ParameterName;", "name", "property", "oldValue", "newValue", "", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Lkotlin/properties/ReadWriteProperty;", "vetoable", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class Delegates {
    public static final com.android.server.permission.jarjar.kotlin.properties.Delegates INSTANCE = new com.android.server.permission.jarjar.kotlin.properties.Delegates();

    private Delegates() {
    }

    public final <T> com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty<java.lang.Object, T> notNull() {
        return new com.android.server.permission.jarjar.kotlin.properties.NotNullVar();
    }

    public final <T> com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty<java.lang.Object, T> observable(T t, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super com.android.server.permission.jarjar.kotlin.reflect.KProperty<?>, ? super T, ? super T, com.android.server.permission.jarjar.kotlin.Unit> function3) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "onChange");
        return new com.android.server.permission.jarjar.kotlin.properties.ObservableProperty<T>(t) { // from class: com.android.server.permission.jarjar.kotlin.properties.Delegates.observable.1
            @Override // com.android.server.permission.jarjar.kotlin.properties.ObservableProperty
            protected void afterChange(com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty, T t2, T t3) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
                function3.invoke(kProperty, t2, t3);
            }
        };
    }

    public final <T> com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty<java.lang.Object, T> vetoable(T t, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function3<? super com.android.server.permission.jarjar.kotlin.reflect.KProperty<?>, ? super T, ? super T, java.lang.Boolean> function3) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function3, "onChange");
        return new com.android.server.permission.jarjar.kotlin.properties.ObservableProperty<T>(t) { // from class: com.android.server.permission.jarjar.kotlin.properties.Delegates.vetoable.1
            @Override // com.android.server.permission.jarjar.kotlin.properties.ObservableProperty
            protected boolean beforeChange(com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty, T t2, T t3) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
                return function3.invoke(kProperty, t2, t3).booleanValue();
            }
        };
    }
}
