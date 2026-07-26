package com.android.server.permission.jarjar.kotlin.properties;

/* JADX INFO: compiled from: ObservableProperty.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\b&\u0018\u0000*\u0004\b\u0000\u0010\u00012\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0003\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000¢\u0006\u0002\u0010\u0005J)\u0010\b\u001a\u00020\t2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\f\u001a\u00028\u00002\u0006\u0010\r\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u000eJ)\u0010\u000f\u001a\u00020\u00102\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\f\u001a\u00028\u00002\u0006\u0010\r\u001a\u00028\u0000H\u0014¢\u0006\u0002\u0010\u0011J$\u0010\u0012\u001a\u00028\u00002\b\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000bH\u0096\u0002¢\u0006\u0002\u0010\u0014J,\u0010\u0015\u001a\u00020\t2\b\u0010\u0013\u001a\u0004\u0018\u00010\u00032\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\u0006\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u0010\u0010\u0006\u001a\u00028\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\u0019"}, d2 = {"Lkotlin/properties/ObservableProperty;", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "Lkotlin/properties/ReadWriteProperty;", "", "initialValue", "(Ljava/lang/Object;)V", "value", "Ljava/lang/Object;", "afterChange", "", "property", "Lkotlin/reflect/KProperty;", "oldValue", "newValue", "(Lkotlin/reflect/KProperty;Ljava/lang/Object;Ljava/lang/Object;)V", "beforeChange", "", "(Lkotlin/reflect/KProperty;Ljava/lang/Object;Ljava/lang/Object;)Z", "getValue", "thisRef", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class ObservableProperty<V> implements com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty<java.lang.Object, V> {
    private V value;

    public ObservableProperty(V v) {
        this.value = v;
    }

    protected boolean beforeChange(com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty, V v, V v2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
        return true;
    }

    protected void afterChange(com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty, V v, V v2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
    }

    @Override // com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty, com.android.server.permission.jarjar.kotlin.properties.ReadOnlyProperty
    public V getValue(java.lang.Object thisRef, com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
        return this.value;
    }

    @Override // com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty
    public void setValue(java.lang.Object thisRef, com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty, V v) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
        V v2 = this.value;
        if (!beforeChange(kProperty, v2, v)) {
            return;
        }
        this.value = v;
        afterChange(kProperty, v2, v);
    }

    public java.lang.String toString() {
        return "ObservableProperty(value=" + this.value + ')';
    }
}
