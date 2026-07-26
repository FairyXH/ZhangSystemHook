package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: UHexExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000<\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\f\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u0005\u001a\u001c\u0010\u0006\u001a\u00020\u0007*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\b\u001a\u001c\u0010\t\u001a\u00020\n*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u000b\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u000e\u001a\u001c\u0010\u000f\u001a\u00020\u0010*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0002\u0010\u0011\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u0013\u0010\u0014\u001a2\u0010\u0012\u001a\u00020\u0002*\u00020\u00072\b\b\u0002\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0017\u001a\u00020\u00162\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u0018\u0010\u0019\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\u00072\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u001a\u0010\u001b\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\n2\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u001c\u0010\u001d\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\r2\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b\u001e\u0010\u001f\u001a\u001e\u0010\u0012\u001a\u00020\u0002*\u00020\u00102\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b¢\u0006\u0004\b \u0010!¨\u0006\""}, d2 = {"hexToUByte", "Lkotlin/UByte;", "", "format", "Lkotlin/text/HexFormat;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)B", "hexToUByteArray", "Lkotlin/UByteArray;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)[B", "hexToUInt", "Lkotlin/UInt;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)I", "hexToULong", "Lkotlin/ULong;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)J", "hexToUShort", "Lkotlin/UShort;", "(Ljava/lang/String;Lkotlin/text/HexFormat;)S", "toHexString", "toHexString-ZQbaR00", "(BLkotlin/text/HexFormat;)Ljava/lang/String;", "startIndex", "", "endIndex", "toHexString-lZCiFrA", "([BIILkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-zHuV2wU", "([BLkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-8M7LxHw", "(ILkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-8UJCm-I", "(JLkotlin/text/HexFormat;)Ljava/lang/String;", "toHexString-r3ox_E0", "(SLkotlin/text/HexFormat;)Ljava/lang/String;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UHexExtensionsKt {
    /* JADX INFO: renamed from: toHexString-zHuV2wU, reason: not valid java name */
    private static final java.lang.String m7398toHexStringzHuV2wU(byte[] $this$toHexString_u2dzHuV2wU, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toHexString_u2dzHuV2wU, "$this$toHexString");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dzHuV2wU, format);
    }

    /* JADX INFO: renamed from: toHexString-zHuV2wU$default, reason: not valid java name */
    static /* synthetic */ java.lang.String m7399toHexStringzHuV2wU$default(byte[] $this$toHexString_u2dzHuV2wU_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toHexString_u2dzHuV2wU_u24default, "$this$toHexString");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dzHuV2wU_u24default, format);
    }

    /* JADX INFO: renamed from: toHexString-lZCiFrA$default, reason: not valid java name */
    static /* synthetic */ java.lang.String m7395toHexStringlZCiFrA$default(byte[] $this$toHexString_u2dlZCiFrA_u24default, int startIndex, int endIndex, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            startIndex = 0;
        }
        if ((i & 2) != 0) {
            endIndex = com.android.server.permission.jarjar.kotlin.UByteArray.m6166getSizeimpl($this$toHexString_u2dlZCiFrA_u24default);
        }
        if ((i & 4) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toHexString_u2dlZCiFrA_u24default, "$this$toHexString");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dlZCiFrA_u24default, startIndex, endIndex, format);
    }

    /* JADX INFO: renamed from: toHexString-lZCiFrA, reason: not valid java name */
    private static final java.lang.String m7394toHexStringlZCiFrA(byte[] $this$toHexString_u2dlZCiFrA, int startIndex, int endIndex, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toHexString_u2dlZCiFrA, "$this$toHexString");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dlZCiFrA, startIndex, endIndex, format);
    }

    static /* synthetic */ byte[] hexToUByteArray$default(java.lang.String $this$hexToUByteArray_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUByteArray_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6160constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToByteArray($this$hexToUByteArray_u24default, format));
    }

    private static final byte[] hexToUByteArray(java.lang.String $this$hexToUByteArray, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUByteArray, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6160constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToByteArray($this$hexToUByteArray, format));
    }

    /* JADX INFO: renamed from: toHexString-ZQbaR00, reason: not valid java name */
    private static final java.lang.String m7392toHexStringZQbaR00(byte $this$toHexString_u2dZQbaR00, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dZQbaR00, format);
    }

    /* JADX INFO: renamed from: toHexString-ZQbaR00$default, reason: not valid java name */
    static /* synthetic */ java.lang.String m7393toHexStringZQbaR00$default(byte $this$toHexString_u2dZQbaR00_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dZQbaR00_u24default, format);
    }

    private static final byte hexToUByte(java.lang.String $this$hexToUByte, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUByte, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToByte($this$hexToUByte, format));
    }

    static /* synthetic */ byte hexToUByte$default(java.lang.String $this$hexToUByte_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUByte_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UByte.m6107constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToByte($this$hexToUByte_u24default, format));
    }

    /* JADX INFO: renamed from: toHexString-r3ox_E0, reason: not valid java name */
    private static final java.lang.String m7396toHexStringr3ox_E0(short $this$toHexString_u2dr3ox_E0, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dr3ox_E0, format);
    }

    /* JADX INFO: renamed from: toHexString-r3ox_E0$default, reason: not valid java name */
    static /* synthetic */ java.lang.String m7397toHexStringr3ox_E0$default(short $this$toHexString_u2dr3ox_E0_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2dr3ox_E0_u24default, format);
    }

    private static final short hexToUShort(java.lang.String $this$hexToUShort, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUShort, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UShort.m6370constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToShort($this$hexToUShort, format));
    }

    static /* synthetic */ short hexToUShort$default(java.lang.String $this$hexToUShort_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUShort_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UShort.m6370constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToShort($this$hexToUShort_u24default, format));
    }

    /* JADX INFO: renamed from: toHexString-8M7LxHw, reason: not valid java name */
    private static final java.lang.String m7388toHexString8M7LxHw(int $this$toHexString_u2d8M7LxHw, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2d8M7LxHw, format);
    }

    /* JADX INFO: renamed from: toHexString-8M7LxHw$default, reason: not valid java name */
    static /* synthetic */ java.lang.String m7389toHexString8M7LxHw$default(int $this$toHexString_u2d8M7LxHw_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2d8M7LxHw_u24default, format);
    }

    private static final int hexToUInt(java.lang.String $this$hexToUInt, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUInt, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToInt($this$hexToUInt, format));
    }

    static /* synthetic */ int hexToUInt$default(java.lang.String $this$hexToUInt_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToUInt_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.UInt.m6184constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToInt($this$hexToUInt_u24default, format));
    }

    /* JADX INFO: renamed from: toHexString-8UJCm-I, reason: not valid java name */
    private static final java.lang.String m7390toHexString8UJCmI(long $this$toHexString_u2d8UJCm_u2dI, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2d8UJCm_u2dI, format);
    }

    /* JADX INFO: renamed from: toHexString-8UJCm-I$default, reason: not valid java name */
    static /* synthetic */ java.lang.String m7391toHexString8UJCmI$default(long $this$toHexString_u2d8UJCm_u2dI_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.toHexString($this$toHexString_u2d8UJCm_u2dI_u24default, format);
    }

    private static final long hexToULong(java.lang.String $this$hexToULong, com.android.server.permission.jarjar.kotlin.text.HexFormat format) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToULong, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToLong($this$hexToULong, format));
    }

    static /* synthetic */ long hexToULong$default(java.lang.String $this$hexToULong_u24default, com.android.server.permission.jarjar.kotlin.text.HexFormat format, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            format = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault();
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToULong_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return com.android.server.permission.jarjar.kotlin.ULong.m6263constructorimpl(com.android.server.permission.jarjar.kotlin.text.HexExtensionsKt.hexToLong($this$hexToULong_u24default, format));
    }
}
