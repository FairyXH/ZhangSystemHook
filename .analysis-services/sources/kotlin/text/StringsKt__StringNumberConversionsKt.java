package kotlin.text;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: StringNumberConversions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007¢\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015"}, d2 = {"numberFormatError", "", com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT, "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
public class StringsKt__StringNumberConversionsKt extends kotlin.text.StringsKt__StringNumberConversionsJVMKt {
    public static final java.lang.Byte toByteOrNull(java.lang.String $this$toByteOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toByteOrNull, "<this>");
        return kotlin.text.StringsKt.toByteOrNull($this$toByteOrNull, 10);
    }

    public static final java.lang.Byte toByteOrNull(java.lang.String $this$toByteOrNull, int radix) {
        int iIntValue;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toByteOrNull, "<this>");
        java.lang.Integer intOrNull = kotlin.text.StringsKt.toIntOrNull($this$toByteOrNull, radix);
        if (intOrNull == null || (iIntValue = intOrNull.intValue()) < -128 || iIntValue > 127) {
            return null;
        }
        return java.lang.Byte.valueOf((byte) iIntValue);
    }

    public static final java.lang.Short toShortOrNull(java.lang.String $this$toShortOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toShortOrNull, "<this>");
        return kotlin.text.StringsKt.toShortOrNull($this$toShortOrNull, 10);
    }

    public static final java.lang.Short toShortOrNull(java.lang.String $this$toShortOrNull, int radix) {
        int iIntValue;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toShortOrNull, "<this>");
        java.lang.Integer intOrNull = kotlin.text.StringsKt.toIntOrNull($this$toShortOrNull, radix);
        if (intOrNull == null || (iIntValue = intOrNull.intValue()) < -32768 || iIntValue > 32767) {
            return null;
        }
        return java.lang.Short.valueOf((short) iIntValue);
    }

    public static final java.lang.Integer toIntOrNull(java.lang.String $this$toIntOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toIntOrNull, "<this>");
        return kotlin.text.StringsKt.toIntOrNull($this$toIntOrNull, 10);
    }

    public static final java.lang.Integer toIntOrNull(java.lang.String $this$toIntOrNull, int radix) {
        int start;
        boolean isNegative;
        int limit;
        int result;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toIntOrNull, "<this>");
        kotlin.text.CharsKt.checkRadix(radix);
        int length = $this$toIntOrNull.length();
        if (length == 0) {
            return null;
        }
        char firstChar = $this$toIntOrNull.charAt(0);
        if (kotlin.jvm.internal.Intrinsics.compare((int) firstChar, 48) < 0) {
            if (length == 1) {
                return null;
            }
            start = 1;
            if (firstChar == '-') {
                isNegative = true;
                limit = Integer.MIN_VALUE;
            } else {
                if (firstChar != '+') {
                    return null;
                }
                isNegative = false;
                limit = -2147483647;
            }
        } else {
            start = 0;
            isNegative = false;
            limit = -2147483647;
        }
        int limitBeforeMul = -59652323;
        int result2 = 0;
        for (int i = start; i < length; i++) {
            int digit = kotlin.text.CharsKt.digitOf($this$toIntOrNull.charAt(i), radix);
            if (digit < 0) {
                return null;
            }
            if ((result2 < limitBeforeMul && (limitBeforeMul != -59652323 || result2 < (limitBeforeMul = limit / radix))) || (result = result2 * radix) < limit + digit) {
                return null;
            }
            result2 = result - digit;
        }
        return isNegative ? java.lang.Integer.valueOf(result2) : java.lang.Integer.valueOf(-result2);
    }

    public static final java.lang.Long toLongOrNull(java.lang.String $this$toLongOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toLongOrNull, "<this>");
        return kotlin.text.StringsKt.toLongOrNull($this$toLongOrNull, 10);
    }

    public static final java.lang.Long toLongOrNull(java.lang.String $this$toLongOrNull, int radix) {
        int start;
        boolean isNegative;
        long limit;
        char firstChar;
        long limitForMaxRadix;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toLongOrNull, "<this>");
        kotlin.text.CharsKt.checkRadix(radix);
        int length = $this$toLongOrNull.length();
        if (length == 0) {
            return null;
        }
        char firstChar2 = $this$toLongOrNull.charAt(0);
        if (kotlin.jvm.internal.Intrinsics.compare((int) firstChar2, 48) < 0) {
            if (length == 1) {
                return null;
            }
            start = 1;
            if (firstChar2 == '-') {
                isNegative = true;
                limit = Long.MIN_VALUE;
            } else {
                if (firstChar2 != '+') {
                    return null;
                }
                isNegative = false;
                limit = -9223372036854775807L;
            }
        } else {
            start = 0;
            isNegative = false;
            limit = -9223372036854775807L;
        }
        long limitForMaxRadix2 = -256204778801521550L;
        long limitBeforeMul = -256204778801521550L;
        long result = 0;
        int i = start;
        while (i < length) {
            int digit = kotlin.text.CharsKt.digitOf($this$toLongOrNull.charAt(i), radix);
            if (digit < 0) {
                return null;
            }
            if (result >= limitBeforeMul) {
                firstChar = firstChar2;
                limitForMaxRadix = limitForMaxRadix2;
            } else if (limitBeforeMul == limitForMaxRadix2) {
                firstChar = firstChar2;
                limitForMaxRadix = limitForMaxRadix2;
                long limitBeforeMul2 = limit / ((long) radix);
                if (result < limitBeforeMul2) {
                    return null;
                }
                limitBeforeMul = limitBeforeMul2;
            } else {
                return null;
            }
            long result2 = result * ((long) radix);
            if (result2 < ((long) digit) + limit) {
                return null;
            }
            result = result2 - ((long) digit);
            i++;
            firstChar2 = firstChar;
            limitForMaxRadix2 = limitForMaxRadix;
        }
        return isNegative ? java.lang.Long.valueOf(result) : java.lang.Long.valueOf(-result);
    }

    public static final java.lang.Void numberFormatError(java.lang.String input) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(input, "input");
        throw new java.lang.NumberFormatException("Invalid number format: '" + input + '\'');
    }
}
