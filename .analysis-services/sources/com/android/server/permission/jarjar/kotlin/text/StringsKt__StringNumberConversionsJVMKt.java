package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: StringNumberConversionsJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000X\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a4\u0010\u0000\u001a\u0004\u0018\u0001H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u0002H\u00010\u0005H\u0082\b¢\u0006\u0004\b\u0006\u0010\u0007\u001a\r\u0010\b\u001a\u00020\t*\u00020\u0003H\u0087\b\u001a\u0015\u0010\b\u001a\u00020\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u000e\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u0003H\u0007\u001a\u0016\u0010\f\u001a\u0004\u0018\u00010\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u000bH\u0007\u001a\r\u0010\r\u001a\u00020\u000e*\u00020\u0003H\u0087\b\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u000e\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007\u001a\u0016\u0010\u0011\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\u000f\u0010\u0012\u001a\u00020\u0013*\u0004\u0018\u00010\u0003H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0015*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u0017*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u0018\u001a\u0004\u0018\u00010\u0017*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0019\u001a\r\u0010\u001a\u001a\u00020\u001b*\u00020\u0003H\u0087\b\u001a\u0013\u0010\u001c\u001a\u0004\u0018\u00010\u001b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u001d\u001a\r\u0010\u001e\u001a\u00020\u0010*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001e\u001a\u00020\u0010*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010\u001f\u001a\u00020 *\u00020\u0003H\u0087\b\u001a\u0015\u0010\u001f\u001a\u00020 *\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\r\u0010!\u001a\u00020\"*\u00020\u0003H\u0087\b\u001a\u0015\u0010!\u001a\u00020\"*\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020 2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b\u001a\u0015\u0010#\u001a\u00020\u0003*\u00020\"2\u0006\u0010\u000f\u001a\u00020\u0010H\u0087\b¨\u0006$"}, d2 = {"screenFloatValue", "T", "str", "", "parse", "Lkotlin/Function1;", "screenFloatValue$StringsKt__StringNumberConversionsJVMKt", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "toBigDecimal", "Ljava/math/BigDecimal;", "mathContext", "Ljava/math/MathContext;", "toBigDecimalOrNull", "toBigInteger", "Ljava/math/BigInteger;", "radix", "", "toBigIntegerOrNull", "toBoolean", "", "toByte", "", "toDouble", "", "toDoubleOrNull", "(Ljava/lang/String;)Ljava/lang/Double;", "toFloat", "", "toFloatOrNull", "(Ljava/lang/String;)Ljava/lang/Float;", "toInt", "toLong", "", "toShort", "", "toString", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsJVMKt extends com.android.server.permission.jarjar.kotlin.text.StringsKt__StringBuilderKt {
    private static final java.lang.String toString(byte $this$toString, int radix) {
        java.lang.String string = java.lang.Integer.toString($this$toString, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private static final java.lang.String toString(short $this$toString, int radix) {
        java.lang.String string = java.lang.Integer.toString($this$toString, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private static final java.lang.String toString(int $this$toString, int radix) {
        java.lang.String string = java.lang.Integer.toString($this$toString, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private static final java.lang.String toString(long $this$toString, int radix) {
        java.lang.String string = java.lang.Long.toString($this$toString, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private static final boolean toBoolean(java.lang.String $this$toBoolean) {
        return java.lang.Boolean.parseBoolean($this$toBoolean);
    }

    private static final byte toByte(java.lang.String $this$toByte) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toByte, "<this>");
        return java.lang.Byte.parseByte($this$toByte);
    }

    private static final byte toByte(java.lang.String $this$toByte, int radix) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toByte, "<this>");
        return java.lang.Byte.parseByte($this$toByte, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
    }

    private static final short toShort(java.lang.String $this$toShort) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toShort, "<this>");
        return java.lang.Short.parseShort($this$toShort);
    }

    private static final short toShort(java.lang.String $this$toShort, int radix) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toShort, "<this>");
        return java.lang.Short.parseShort($this$toShort, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
    }

    private static final int toInt(java.lang.String $this$toInt) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toInt, "<this>");
        return java.lang.Integer.parseInt($this$toInt);
    }

    private static final int toInt(java.lang.String $this$toInt, int radix) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toInt, "<this>");
        return java.lang.Integer.parseInt($this$toInt, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
    }

    private static final long toLong(java.lang.String $this$toLong) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toLong, "<this>");
        return java.lang.Long.parseLong($this$toLong);
    }

    private static final long toLong(java.lang.String $this$toLong, int radix) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toLong, "<this>");
        return java.lang.Long.parseLong($this$toLong, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
    }

    private static final float toFloat(java.lang.String $this$toFloat) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toFloat, "<this>");
        return java.lang.Float.parseFloat($this$toFloat);
    }

    private static final double toDouble(java.lang.String $this$toDouble) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toDouble, "<this>");
        return java.lang.Double.parseDouble($this$toDouble);
    }

    public static final java.lang.Float toFloatOrNull(java.lang.String $this$toFloatOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toFloatOrNull, "<this>");
        try {
            if (com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx.value.matches($this$toFloatOrNull)) {
                return java.lang.Float.valueOf(java.lang.Float.parseFloat($this$toFloatOrNull));
            }
            return null;
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    public static final java.lang.Double toDoubleOrNull(java.lang.String $this$toDoubleOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toDoubleOrNull, "<this>");
        try {
            if (com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx.value.matches($this$toDoubleOrNull)) {
                return java.lang.Double.valueOf(java.lang.Double.parseDouble($this$toDoubleOrNull));
            }
            return null;
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private static final java.math.BigInteger toBigInteger(java.lang.String $this$toBigInteger) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigInteger, "<this>");
        return new java.math.BigInteger($this$toBigInteger);
    }

    private static final java.math.BigInteger toBigInteger(java.lang.String $this$toBigInteger, int radix) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigInteger, "<this>");
        return new java.math.BigInteger($this$toBigInteger, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
    }

    public static final java.math.BigInteger toBigIntegerOrNull(java.lang.String $this$toBigIntegerOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigIntegerOrNull, "<this>");
        return com.android.server.permission.jarjar.kotlin.text.StringsKt.toBigIntegerOrNull($this$toBigIntegerOrNull, 10);
    }

    public static final java.math.BigInteger toBigIntegerOrNull(java.lang.String $this$toBigIntegerOrNull, int radix) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigIntegerOrNull, "<this>");
        com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix);
        int length = $this$toBigIntegerOrNull.length();
        switch (length) {
            case 0:
                return null;
            case 1:
                if (com.android.server.permission.jarjar.kotlin.text.CharsKt.digitOf($this$toBigIntegerOrNull.charAt(0), radix) < 0) {
                    return null;
                }
                break;
            default:
                int start = $this$toBigIntegerOrNull.charAt(0) == '-' ? 1 : 0;
                for (int index = start; index < length; index++) {
                    if (com.android.server.permission.jarjar.kotlin.text.CharsKt.digitOf($this$toBigIntegerOrNull.charAt(index), radix) < 0) {
                        return null;
                    }
                }
                break;
        }
        return new java.math.BigInteger($this$toBigIntegerOrNull, com.android.server.permission.jarjar.kotlin.text.CharsKt.checkRadix(radix));
    }

    private static final java.math.BigDecimal toBigDecimal(java.lang.String $this$toBigDecimal) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        return new java.math.BigDecimal($this$toBigDecimal);
    }

    private static final java.math.BigDecimal toBigDecimal(java.lang.String $this$toBigDecimal, java.math.MathContext mathContext) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new java.math.BigDecimal($this$toBigDecimal, mathContext);
    }

    public static final java.math.BigDecimal toBigDecimalOrNull(java.lang.String $this$toBigDecimalOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigDecimalOrNull, "<this>");
        try {
            if (com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx.value.matches($this$toBigDecimalOrNull)) {
                return new java.math.BigDecimal($this$toBigDecimalOrNull);
            }
            return null;
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    public static final java.math.BigDecimal toBigDecimalOrNull(java.lang.String $this$toBigDecimalOrNull, java.math.MathContext mathContext) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBigDecimalOrNull, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        try {
            if (com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx.value.matches($this$toBigDecimalOrNull)) {
                return new java.math.BigDecimal($this$toBigDecimalOrNull, mathContext);
            }
            return null;
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private static final <T> T screenFloatValue$StringsKt__StringNumberConversionsJVMKt(java.lang.String str, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.String, ? extends T> function1) {
        try {
            if (!com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx.value.matches(str)) {
                return null;
            }
            return function1.invoke(str);
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }
}
