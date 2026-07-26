package com.android.server.permission.jarjar.kotlin.properties;

/* JADX INFO: compiled from: Delegates.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0002\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0012\u0004\u0012\u0002H\u00010\u0003B\u0005¢\u0006\u0002\u0010\u0004J$\u0010\u0007\u001a\u00028\u00002\b\u0010\b\u001a\u0004\u0018\u00010\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\nH\u0096\u0002¢\u0006\u0002\u0010\u000bJ,\u0010\f\u001a\u00020\r2\b\u0010\b\u001a\u0004\u0018\u00010\u00022\n\u0010\t\u001a\u0006\u0012\u0002\b\u00030\n2\u0006\u0010\u0005\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u0012\u0010\u0005\u001a\u0004\u0018\u00018\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0006¨\u0006\u0011"}, d2 = {"Lkotlin/properties/NotNullVar;", "T", "", "Lkotlin/properties/ReadWriteProperty;", "()V", "value", "Ljava/lang/Object;", "getValue", "thisRef", "property", "Lkotlin/reflect/KProperty;", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "setValue", "", "(Ljava/lang/Object;Lkotlin/reflect/KProperty;Ljava/lang/Object;)V", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class NotNullVar<T> implements com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty<java.lang.Object, T> {
    private T value;

    @Override // com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty, com.android.server.permission.jarjar.kotlin.properties.ReadOnlyProperty
    public T getValue(java.lang.Object thisRef, com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
        T t = this.value;
        if (t != null) {
            return t;
        }
        throw new java.lang.IllegalStateException("Property " + kProperty.getName() + " should be initialized before get.");
    }

    @Override // com.android.server.permission.jarjar.kotlin.properties.ReadWriteProperty
    public void setValue(java.lang.Object thisRef, com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "value");
        this.value = t;
    }

    public java.lang.String toString() {
        return "NotNullProperty(" + (this.value != null ? "value=" + this.value : "value not initialized yet") + ')';
    }
}
