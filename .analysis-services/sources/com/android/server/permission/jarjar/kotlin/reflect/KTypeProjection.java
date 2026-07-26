package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: KTypeProjection.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087\b\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005HÆ\u0003J!\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0016"}, d2 = {"Lkotlin/reflect/KTypeProjection;", "", "variance", "Lkotlin/reflect/KVariance;", "type", "Lkotlin/reflect/KType;", "(Lkotlin/reflect/KVariance;Lkotlin/reflect/KType;)V", "getType", "()Lkotlin/reflect/KType;", "getVariance", "()Lkotlin/reflect/KVariance;", "component1", "component2", "copy", "equals", "", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class KTypeProjection {
    public static final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection.Companion Companion = new com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection.Companion(null);
    public static final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection star = new com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection(null, null);
    private final com.android.server.permission.jarjar.kotlin.reflect.KType type;
    private final com.android.server.permission.jarjar.kotlin.reflect.KVariance variance;

    /* JADX INFO: compiled from: KTypeProjection.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[com.android.server.permission.jarjar.kotlin.reflect.KVariance.values().length];
            try {
                iArr[com.android.server.permission.jarjar.kotlin.reflect.KVariance.INVARIANT.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.reflect.KVariance.IN.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[com.android.server.permission.jarjar.kotlin.reflect.KVariance.OUT.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    @com.android.server.permission.jarjar.kotlin.jvm.JvmStatic
    public static final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection contravariant(com.android.server.permission.jarjar.kotlin.reflect.KType kType) {
        return Companion.contravariant(kType);
    }

    public static /* synthetic */ com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection copy$default(com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection kTypeProjection, com.android.server.permission.jarjar.kotlin.reflect.KVariance kVariance, com.android.server.permission.jarjar.kotlin.reflect.KType kType, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            kVariance = kTypeProjection.variance;
        }
        if ((i & 2) != 0) {
            kType = kTypeProjection.type;
        }
        return kTypeProjection.copy(kVariance, kType);
    }

    @com.android.server.permission.jarjar.kotlin.jvm.JvmStatic
    public static final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection covariant(com.android.server.permission.jarjar.kotlin.reflect.KType kType) {
        return Companion.covariant(kType);
    }

    @com.android.server.permission.jarjar.kotlin.jvm.JvmStatic
    public static final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection invariant(com.android.server.permission.jarjar.kotlin.reflect.KType kType) {
        return Companion.invariant(kType);
    }

    public final com.android.server.permission.jarjar.kotlin.reflect.KVariance component1() {
        return this.variance;
    }

    public final com.android.server.permission.jarjar.kotlin.reflect.KType component2() {
        return this.type;
    }

    public final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection copy(com.android.server.permission.jarjar.kotlin.reflect.KVariance kVariance, com.android.server.permission.jarjar.kotlin.reflect.KType kType) {
        return new com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection(kVariance, kType);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection kTypeProjection = (com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection) obj;
        return this.variance == kTypeProjection.variance && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.type, kTypeProjection.type);
    }

    public int hashCode() {
        return ((this.variance == null ? 0 : this.variance.hashCode()) * 31) + (this.type != null ? this.type.hashCode() : 0);
    }

    public KTypeProjection(com.android.server.permission.jarjar.kotlin.reflect.KVariance variance, com.android.server.permission.jarjar.kotlin.reflect.KType type) {
        java.lang.String str;
        this.variance = variance;
        this.type = type;
        if ((this.variance == null) == (this.type == null)) {
            return;
        }
        if (this.variance == null) {
            str = "Star projection must have no type specified.";
        } else {
            str = "The projection variance " + this.variance + " requires type to be specified.";
        }
        throw new java.lang.IllegalArgumentException(str.toString());
    }

    public final com.android.server.permission.jarjar.kotlin.reflect.KVariance getVariance() {
        return this.variance;
    }

    public final com.android.server.permission.jarjar.kotlin.reflect.KType getType() {
        return this.type;
    }

    public java.lang.String toString() {
        com.android.server.permission.jarjar.kotlin.reflect.KVariance kVariance = this.variance;
        switch (kVariance == null ? -1 : com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection.WhenMappings.$EnumSwitchMapping$0[kVariance.ordinal()]) {
            case -1:
                return com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER;
            case 0:
            default:
                throw new com.android.server.permission.jarjar.kotlin.NoWhenBranchMatchedException();
            case 1:
                return java.lang.String.valueOf(this.type);
            case 2:
                return "in " + this.type;
            case 3:
                return "out " + this.type;
        }
    }

    /* JADX INFO: compiled from: KTypeProjection.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0007\u001a\u00020\u00048\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\b\u0010\u0002¨\u0006\u000e"}, d2 = {"Lkotlin/reflect/KTypeProjection$Companion;", "", "()V", "STAR", "Lkotlin/reflect/KTypeProjection;", "getSTAR", "()Lkotlin/reflect/KTypeProjection;", "star", "getStar$annotations", "contravariant", "type", "Lkotlin/reflect/KType;", "covariant", "invariant", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getStar$annotations() {
        }

        private Companion() {
        }

        public final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection getSTAR() {
            return com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection.star;
        }

        @com.android.server.permission.jarjar.kotlin.jvm.JvmStatic
        public final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection invariant(com.android.server.permission.jarjar.kotlin.reflect.KType type) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(type, "type");
            return new com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection(com.android.server.permission.jarjar.kotlin.reflect.KVariance.INVARIANT, type);
        }

        @com.android.server.permission.jarjar.kotlin.jvm.JvmStatic
        public final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection contravariant(com.android.server.permission.jarjar.kotlin.reflect.KType type) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(type, "type");
            return new com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection(com.android.server.permission.jarjar.kotlin.reflect.KVariance.IN, type);
        }

        @com.android.server.permission.jarjar.kotlin.jvm.JvmStatic
        public final com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection covariant(com.android.server.permission.jarjar.kotlin.reflect.KType type) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(type, "type");
            return new com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection(com.android.server.permission.jarjar.kotlin.reflect.KVariance.OUT, type);
        }
    }
}
