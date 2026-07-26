package kotlin.text;

/* JADX INFO: compiled from: UStrings.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\u0005\u0010\u0006\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\b\u0010\t\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\u000b\u0010\f\u001a\u001b\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0004\b\u000e\u0010\u000f\u001a\u0011\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0011\u001a\u0019\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u0012\u001a\u000e\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007\u001a\u0016\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0011\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0015\u001a\u0019\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u0016\u001a\u000e\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007\u001a\u0016\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0011\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u0019\u001a\u0019\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u001a\u001a\u000e\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007\u001a\u0016\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u001a\u0011\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007¢\u0006\u0002\u0010\u001d\u001a\u0019\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010\u001e\u001a\u000e\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007\u001a\u0016\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007¨\u0006 "}, d2 = {"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UStringsKt {
    /* JADX INFO: renamed from: toString-LxnNnR4, reason: not valid java name */
    public static final java.lang.String m12620toStringLxnNnR4(byte $this$toString_u2dLxnNnR4, int radix) {
        java.lang.String string = java.lang.Integer.toString($this$toString_u2dLxnNnR4 & 255, kotlin.text.CharsKt.checkRadix(radix));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    /* JADX INFO: renamed from: toString-olVBNx4, reason: not valid java name */
    public static final java.lang.String m12622toStringolVBNx4(short $this$toString_u2dolVBNx4, int radix) {
        java.lang.String string = java.lang.Integer.toString(65535 & $this$toString_u2dolVBNx4, kotlin.text.CharsKt.checkRadix(radix));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    /* JADX INFO: renamed from: toString-V7xB4Y4, reason: not valid java name */
    public static final java.lang.String m12621toStringV7xB4Y4(int $this$toString_u2dV7xB4Y4, int radix) {
        java.lang.String string = java.lang.Long.toString(((long) $this$toString_u2dV7xB4Y4) & 4294967295L, kotlin.text.CharsKt.checkRadix(radix));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    /* JADX INFO: renamed from: toString-JSWoG40, reason: not valid java name */
    public static final java.lang.String m12619toStringJSWoG40(long $this$toString_u2dJSWoG40, int radix) {
        return kotlin.UnsignedKt.ulongToString($this$toString_u2dJSWoG40, kotlin.text.CharsKt.checkRadix(radix));
    }

    public static final byte toUByte(java.lang.String $this$toUByte) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUByte, "<this>");
        kotlin.UByte uByteOrNull = toUByteOrNull($this$toUByte);
        if (uByteOrNull != null) {
            return uByteOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toUByte);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final byte toUByte(java.lang.String $this$toUByte, int radix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUByte, "<this>");
        kotlin.UByte uByteOrNull = toUByteOrNull($this$toUByte, radix);
        if (uByteOrNull != null) {
            return uByteOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toUByte);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final short toUShort(java.lang.String $this$toUShort) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUShort, "<this>");
        kotlin.UShort uShortOrNull = toUShortOrNull($this$toUShort);
        if (uShortOrNull != null) {
            return uShortOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toUShort);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final short toUShort(java.lang.String $this$toUShort, int radix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUShort, "<this>");
        kotlin.UShort uShortOrNull = toUShortOrNull($this$toUShort, radix);
        if (uShortOrNull != null) {
            return uShortOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toUShort);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final int toUInt(java.lang.String $this$toUInt) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUInt, "<this>");
        kotlin.UInt uIntOrNull = toUIntOrNull($this$toUInt);
        if (uIntOrNull != null) {
            return uIntOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toUInt);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final int toUInt(java.lang.String $this$toUInt, int radix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUInt, "<this>");
        kotlin.UInt uIntOrNull = toUIntOrNull($this$toUInt, radix);
        if (uIntOrNull != null) {
            return uIntOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toUInt);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final long toULong(java.lang.String $this$toULong) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toULong, "<this>");
        kotlin.ULong uLongOrNull = toULongOrNull($this$toULong);
        if (uLongOrNull != null) {
            return uLongOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toULong);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final long toULong(java.lang.String $this$toULong, int radix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toULong, "<this>");
        kotlin.ULong uLongOrNull = toULongOrNull($this$toULong, radix);
        if (uLongOrNull != null) {
            return uLongOrNull.getData();
        }
        kotlin.text.StringsKt.numberFormatError($this$toULong);
        throw new kotlin.KotlinNothingValueException();
    }

    public static final kotlin.UByte toUByteOrNull(java.lang.String $this$toUByteOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUByteOrNull, "<this>");
        return toUByteOrNull($this$toUByteOrNull, 10);
    }

    public static final kotlin.UByte toUByteOrNull(java.lang.String $this$toUByteOrNull, int radix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUByteOrNull, "<this>");
        kotlin.UInt uIntOrNull = toUIntOrNull($this$toUByteOrNull, radix);
        if (uIntOrNull == null) {
            return null;
        }
        int data = uIntOrNull.getData();
        if (java.lang.Integer.compareUnsigned(data, kotlin.UInt.m11402constructorimpl(255)) > 0) {
            return null;
        }
        return kotlin.UByte.m11319boximpl(kotlin.UByte.m11325constructorimpl((byte) data));
    }

    public static final kotlin.UShort toUShortOrNull(java.lang.String $this$toUShortOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUShortOrNull, "<this>");
        return toUShortOrNull($this$toUShortOrNull, 10);
    }

    public static final kotlin.UShort toUShortOrNull(java.lang.String $this$toUShortOrNull, int radix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUShortOrNull, "<this>");
        kotlin.UInt uIntOrNull = toUIntOrNull($this$toUShortOrNull, radix);
        if (uIntOrNull == null) {
            return null;
        }
        int data = uIntOrNull.getData();
        if (java.lang.Integer.compareUnsigned(data, kotlin.UInt.m11402constructorimpl(65535)) > 0) {
            return null;
        }
        return kotlin.UShort.m11582boximpl(kotlin.UShort.m11588constructorimpl((short) data));
    }

    public static final kotlin.UInt toUIntOrNull(java.lang.String $this$toUIntOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUIntOrNull, "<this>");
        return toUIntOrNull($this$toUIntOrNull, 10);
    }

    public static final kotlin.UInt toUIntOrNull(java.lang.String $this$toUIntOrNull, int radix) {
        int start;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUIntOrNull, "<this>");
        kotlin.text.CharsKt.checkRadix(radix);
        int length = $this$toUIntOrNull.length();
        if (length == 0) {
            return null;
        }
        char firstChar = $this$toUIntOrNull.charAt(0);
        if (kotlin.jvm.internal.Intrinsics.compare((int) firstChar, 48) < 0) {
            if (length == 1 || firstChar != '+') {
                return null;
            }
            start = 1;
        } else {
            start = 0;
        }
        int limitBeforeMul = 119304647;
        int uradix = kotlin.UInt.m11402constructorimpl(radix);
        int result = 0;
        for (int i = start; i < length; i++) {
            int digit = kotlin.text.CharsKt.digitOf($this$toUIntOrNull.charAt(i), radix);
            if (digit < 0) {
                return null;
            }
            if (java.lang.Integer.compareUnsigned(result, limitBeforeMul) > 0) {
                if (limitBeforeMul != 119304647) {
                    return null;
                }
                limitBeforeMul = java.lang.Integer.divideUnsigned(-1, uradix);
                if (java.lang.Integer.compareUnsigned(result, limitBeforeMul) > 0) {
                    return null;
                }
            }
            int result2 = kotlin.UInt.m11402constructorimpl(result * uradix);
            result = kotlin.UInt.m11402constructorimpl(kotlin.UInt.m11402constructorimpl(digit) + result2);
            if (java.lang.Integer.compareUnsigned(result, result2) < 0) {
                return null;
            }
        }
        return kotlin.UInt.m11396boximpl(result);
    }

    public static final kotlin.ULong toULongOrNull(java.lang.String $this$toULongOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toULongOrNull, "<this>");
        return toULongOrNull($this$toULongOrNull, 10);
    }

    public static final kotlin.ULong toULongOrNull(java.lang.String $this$toULongOrNull, int radix) {
        int start;
        java.lang.String str = $this$toULongOrNull;
        int i = radix;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "<this>");
        kotlin.text.CharsKt.checkRadix(radix);
        int length = $this$toULongOrNull.length();
        if (length == 0) {
            return null;
        }
        char firstChar = str.charAt(0);
        if (kotlin.jvm.internal.Intrinsics.compare((int) firstChar, 48) < 0) {
            if (length == 1 || firstChar != '+') {
                return null;
            }
            start = 1;
        } else {
            start = 0;
        }
        long limitBeforeMul = 512409557603043100L;
        long uradix = kotlin.ULong.m11481constructorimpl(i);
        long result = 0;
        int i2 = start;
        while (i2 < length) {
            int length2 = length;
            int digit = kotlin.text.CharsKt.digitOf(str.charAt(i2), i);
            if (digit < 0) {
                return null;
            }
            if (java.lang.Long.compareUnsigned(result, limitBeforeMul) > 0) {
                if (limitBeforeMul == 512409557603043100L) {
                    limitBeforeMul = java.lang.Long.divideUnsigned(-1L, uradix);
                    if (java.lang.Long.compareUnsigned(result, limitBeforeMul) > 0) {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            long result2 = kotlin.ULong.m11481constructorimpl(result * uradix);
            result = kotlin.ULong.m11481constructorimpl(kotlin.ULong.m11481constructorimpl(((long) kotlin.UInt.m11402constructorimpl(digit)) & 4294967295L) + result2);
            if (java.lang.Long.compareUnsigned(result, result2) < 0) {
                return null;
            }
            i2++;
            str = $this$toULongOrNull;
            i = radix;
            length = length2;
        }
        return kotlin.ULong.m11475boximpl(result);
    }
}
