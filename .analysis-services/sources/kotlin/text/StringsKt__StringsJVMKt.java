package kotlin.text;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: StringsJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000~\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0019\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\r\n\u0002\b\n\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\f\n\u0002\b\u0011\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\tH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000bH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\u0019\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a)\u0010\u0007\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\u0011\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a!\u0010\u0007\u001a\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0087\b\u001a\f\u0010\u0017\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010\u0017\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\u0015\u0010\u001a\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u0015\u0010\u001c\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010\u001d\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u001c\u0010 \u001a\u00020\u0011*\u00020\u00022\u0006\u0010!\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\f\u0010$\u001a\u00020\u0002*\u00020\u0014H\u0007\u001a \u0010$\u001a\u00020\u0002*\u00020\u00142\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\u0019\u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'H\u0087\u0004\u001a \u0010&\u001a\u00020#*\u0004\u0018\u00010'2\b\u0010!\u001a\u0004\u0018\u00010'2\u0006\u0010\"\u001a\u00020#H\u0007\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010\n\u001a\u00020\tH\u0087\b\u001a\u0015\u0010&\u001a\u00020#*\u00020\u00022\u0006\u0010(\u001a\u00020'H\u0087\b\u001a\f\u0010)\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0014\u0010)\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\f\u0010*\u001a\u00020\u0002*\u00020\rH\u0007\u001a*\u0010*\u001a\u00020\u0002*\u00020\r2\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\f\u0010,\u001a\u00020\r*\u00020\u0002H\u0007\u001a*\u0010,\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u00112\b\b\u0002\u0010+\u001a\u00020#H\u0007\u001a\u001c\u0010-\u001a\u00020#*\u00020\u00022\u0006\u0010.\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a \u0010/\u001a\u00020#*\u0004\u0018\u00010\u00022\b\u0010!\u001a\u0004\u0018\u00010\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a4\u00100\u001a\u00020\u0002*\u00020\u00022\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00104\u001a*\u00100\u001a\u00020\u0002*\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00105\u001a<\u00100\u001a\u00020\u0002*\u00020\u00042\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00106\u001a2\u00100\u001a\u00020\u0002*\u00020\u00042\u0006\u00100\u001a\u00020\u00022\u0016\u00101\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010302\"\u0004\u0018\u000103H\u0087\b¢\u0006\u0002\u00107\u001a\r\u00108\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\n\u00109\u001a\u00020#*\u00020'\u001a\r\u0010:\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010:\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u001d\u0010;\u001a\u00020\u0011*\u00020\u00022\u0006\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010;\u001a\u00020\u0011*\u00020\u00022\u0006\u0010?\u001a\u00020\u00022\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010@\u001a\u00020\u0011*\u00020\u00022\u0006\u0010<\u001a\u00020=2\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010@\u001a\u00020\u0011*\u00020\u00022\u0006\u0010?\u001a\u00020\u00022\u0006\u0010>\u001a\u00020\u0011H\u0081\b\u001a\u001d\u0010A\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010B\u001a\u00020\u0011H\u0087\b\u001a4\u0010C\u001a\u00020#*\u00020'2\u0006\u0010D\u001a\u00020\u00112\u0006\u0010!\u001a\u00020'2\u0006\u0010E\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a4\u0010C\u001a\u00020#*\u00020\u00022\u0006\u0010D\u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u00022\u0006\u0010E\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0012\u0010F\u001a\u00020\u0002*\u00020'2\u0006\u0010G\u001a\u00020\u0011\u001a$\u0010H\u001a\u00020\u0002*\u00020\u00022\u0006\u0010I\u001a\u00020=2\u0006\u0010J\u001a\u00020=2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010H\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010M\u001a\u00020\u0002*\u00020\u00022\u0006\u0010I\u001a\u00020=2\u0006\u0010J\u001a\u00020=2\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010M\u001a\u00020\u0002*\u00020\u00022\u0006\u0010K\u001a\u00020\u00022\u0006\u0010L\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a\"\u0010N\u001a\b\u0012\u0004\u0012\u00020\u00020O*\u00020'2\u0006\u0010P\u001a\u00020Q2\b\b\u0002\u0010R\u001a\u00020\u0011\u001a\u001c\u0010S\u001a\u00020#*\u00020\u00022\u0006\u0010T\u001a\u00020\u00022\b\b\u0002\u0010\"\u001a\u00020#\u001a$\u0010S\u001a\u00020#*\u00020\u00022\u0006\u0010T\u001a\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020#\u001a\u0015\u0010U\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u0011H\u0087\b\u001a\u001d\u0010U\u001a\u00020\u0002*\u00020\u00022\u0006\u0010%\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a\u0017\u0010V\u001a\u00020\r*\u00020\u00022\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0087\b\u001a\r\u0010W\u001a\u00020\u0014*\u00020\u0002H\u0087\b\u001a3\u0010W\u001a\u00020\u0014*\u00020\u00022\u0006\u0010X\u001a\u00020\u00142\b\b\u0002\u0010Y\u001a\u00020\u00112\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0087\b\u001a \u0010W\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010%\u001a\u00020\u00112\b\b\u0002\u0010\u001f\u001a\u00020\u0011H\u0007\u001a\r\u0010Z\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010Z\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\u0017\u0010[\u001a\u00020Q*\u00020\u00022\b\b\u0002\u0010\\\u001a\u00020\u0011H\u0087\b\u001a\r\u0010]\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010]\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\u001a\r\u0010^\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0015\u0010^\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0019H\u0087\b\"%\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00020\u0002`\u0003*\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006_"}, d2 = {"CASE_INSENSITIVE_ORDER", "Ljava/util/Comparator;", "", "Lkotlin/Comparator;", "Lkotlin/String$Companion;", "getCASE_INSENSITIVE_ORDER", "(Lkotlin/jvm/internal/StringCompanionObject;)Ljava/util/Comparator;", "String", "stringBuffer", "Ljava/lang/StringBuffer;", "stringBuilder", "Ljava/lang/StringBuilder;", "bytes", "", "charset", "Ljava/nio/charset/Charset;", "offset", "", "length", "chars", "", "codePoints", "", "capitalize", com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE, "Ljava/util/Locale;", "codePointAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "codePointBefore", "codePointCount", "beginIndex", "endIndex", "compareTo", "other", "ignoreCase", "", "concatToString", "startIndex", "contentEquals", "", "charSequence", "decapitalize", "decodeToString", "throwOnInvalidSequence", "encodeToByteArray", "endsWith", "suffix", "equals", "format", "args", "", "", "(Ljava/lang/String;Ljava/util/Locale;[Ljava/lang/Object;)Ljava/lang/String;", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/util/Locale;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "(Lkotlin/jvm/internal/StringCompanionObject;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", "intern", "isBlank", "lowercase", "nativeIndexOf", "ch", "", "fromIndex", "str", "nativeLastIndexOf", "offsetByCodePoints", "codePointOffset", "regionMatches", "thisOffset", "otherOffset", "repeat", "n", "replace", "oldChar", "newChar", "oldValue", "newValue", "replaceFirst", "split", "", "regex", "Ljava/util/regex/Pattern;", "limit", "startsWith", "prefix", "substring", "toByteArray", "toCharArray", "destination", "destinationOffset", "toLowerCase", "toPattern", "flags", "toUpperCase", "uppercase", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
public class StringsKt__StringsJVMKt extends kotlin.text.StringsKt__StringNumberConversionsKt {
    private static final int nativeIndexOf(java.lang.String $this$nativeIndexOf, char ch, int fromIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nativeIndexOf, "<this>");
        return $this$nativeIndexOf.indexOf(ch, fromIndex);
    }

    private static final int nativeIndexOf(java.lang.String $this$nativeIndexOf, java.lang.String str, int fromIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nativeIndexOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "str");
        return $this$nativeIndexOf.indexOf(str, fromIndex);
    }

    private static final int nativeLastIndexOf(java.lang.String $this$nativeLastIndexOf, char ch, int fromIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nativeLastIndexOf, "<this>");
        return $this$nativeLastIndexOf.lastIndexOf(ch, fromIndex);
    }

    private static final int nativeLastIndexOf(java.lang.String $this$nativeLastIndexOf, java.lang.String str, int fromIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$nativeLastIndexOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "str");
        return $this$nativeLastIndexOf.lastIndexOf(str, fromIndex);
    }

    public static /* synthetic */ boolean equals$default(java.lang.String str, java.lang.String str2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.equals(str, str2, z);
    }

    public static final boolean equals(java.lang.String $this$equals, java.lang.String other, boolean ignoreCase) {
        if ($this$equals == null) {
            return other == null;
        }
        if (!ignoreCase) {
            return $this$equals.equals(other);
        }
        return $this$equals.equalsIgnoreCase(other);
    }

    public static /* synthetic */ java.lang.String replace$default(java.lang.String str, char c, char c2, boolean z, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.replace(str, c, c2, z);
    }

    public static final java.lang.String replace(java.lang.String $this$replace, char oldChar, char newChar, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replace, "<this>");
        if (!ignoreCase) {
            java.lang.String strReplace = $this$replace.replace(oldChar, newChar);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strReplace, "replace(...)");
            return strReplace;
        }
        java.lang.StringBuilder $this$replace_u24lambda_u241 = new java.lang.StringBuilder($this$replace.length());
        java.lang.String $this$forEach$iv = $this$replace;
        for (int i = 0; i < $this$forEach$iv.length(); i++) {
            char element$iv = $this$forEach$iv.charAt(i);
            $this$replace_u24lambda_u241.append(kotlin.text.CharsKt.equals(element$iv, oldChar, ignoreCase) ? newChar : element$iv);
        }
        java.lang.String string = $this$replace_u24lambda_u241.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static /* synthetic */ java.lang.String replace$default(java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.replace(str, str2, str3, z);
    }

    public static final java.lang.String replace(java.lang.String $this$replace, java.lang.String oldValue, java.lang.String newValue, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replace, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(oldValue, "oldValue");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newValue, "newValue");
        int occurrenceIndex = kotlin.text.StringsKt.indexOf($this$replace, oldValue, 0, ignoreCase);
        if (occurrenceIndex < 0) {
            return $this$replace;
        }
        int oldValueLength = oldValue.length();
        int searchStep = kotlin.ranges.RangesKt.coerceAtLeast(oldValueLength, 1);
        int newLengthHint = ($this$replace.length() - oldValueLength) + newValue.length();
        if (newLengthHint < 0) {
            throw new java.lang.OutOfMemoryError();
        }
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder(newLengthHint);
        int i = 0;
        do {
            stringBuilder.append((java.lang.CharSequence) $this$replace, i, occurrenceIndex).append(newValue);
            i = occurrenceIndex + oldValueLength;
            if (occurrenceIndex >= $this$replace.length()) {
                break;
            }
            occurrenceIndex = kotlin.text.StringsKt.indexOf($this$replace, oldValue, occurrenceIndex + searchStep, ignoreCase);
        } while (occurrenceIndex > 0);
        java.lang.String string = stringBuilder.append((java.lang.CharSequence) $this$replace, i, $this$replace.length()).toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static /* synthetic */ java.lang.String replaceFirst$default(java.lang.String str, char c, char c2, boolean z, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.replaceFirst(str, c, c2, z);
    }

    public static final java.lang.String replaceFirst(java.lang.String $this$replaceFirst, char oldChar, char newChar, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceFirst, "<this>");
        int index = kotlin.text.StringsKt.indexOf$default($this$replaceFirst, oldChar, 0, ignoreCase, 2, (java.lang.Object) null);
        if (index < 0) {
            return $this$replaceFirst;
        }
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceFirst, index, index + 1, (java.lang.CharSequence) java.lang.String.valueOf(newChar)).toString();
    }

    public static /* synthetic */ java.lang.String replaceFirst$default(java.lang.String str, java.lang.String str2, java.lang.String str3, boolean z, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.replaceFirst(str, str2, str3, z);
    }

    public static final java.lang.String replaceFirst(java.lang.String $this$replaceFirst, java.lang.String oldValue, java.lang.String newValue, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceFirst, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(oldValue, "oldValue");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newValue, "newValue");
        int index = kotlin.text.StringsKt.indexOf$default($this$replaceFirst, oldValue, 0, ignoreCase, 2, (java.lang.Object) null);
        if (index < 0) {
            return $this$replaceFirst;
        }
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceFirst, index, oldValue.length() + index, (java.lang.CharSequence) newValue).toString();
    }

    @kotlin.Deprecated(message = "Use uppercase() instead.", replaceWith = @kotlin.ReplaceWith(expression = "uppercase(Locale.getDefault())", imports = {"java.util.Locale"}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    private static final java.lang.String toUpperCase(java.lang.String $this$toUpperCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUpperCase, "<this>");
        java.lang.String upperCase = $this$toUpperCase.toUpperCase();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        return upperCase;
    }

    private static final java.lang.String uppercase(java.lang.String $this$uppercase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$uppercase, "<this>");
        java.lang.String upperCase = $this$uppercase.toUpperCase(java.util.Locale.ROOT);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        return upperCase;
    }

    @kotlin.Deprecated(message = "Use lowercase() instead.", replaceWith = @kotlin.ReplaceWith(expression = "lowercase(Locale.getDefault())", imports = {"java.util.Locale"}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    private static final java.lang.String toLowerCase(java.lang.String $this$toLowerCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toLowerCase, "<this>");
        java.lang.String lowerCase = $this$toLowerCase.toLowerCase();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return lowerCase;
    }

    private static final java.lang.String lowercase(java.lang.String $this$lowercase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lowercase, "<this>");
        java.lang.String lowerCase = $this$lowercase.toLowerCase(java.util.Locale.ROOT);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return lowerCase;
    }

    public static final java.lang.String concatToString(char[] $this$concatToString) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$concatToString, "<this>");
        return new java.lang.String($this$concatToString);
    }

    public static /* synthetic */ java.lang.String concatToString$default(char[] cArr, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = cArr.length;
        }
        return kotlin.text.StringsKt.concatToString(cArr, i, i2);
    }

    public static final java.lang.String concatToString(char[] $this$concatToString, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$concatToString, "<this>");
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$concatToString.length);
        return new java.lang.String($this$concatToString, startIndex, endIndex - startIndex);
    }

    public static /* synthetic */ char[] toCharArray$default(java.lang.String str, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        return kotlin.text.StringsKt.toCharArray(str, i, i2);
    }

    public static final char[] toCharArray(java.lang.String $this$toCharArray, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$toCharArray.length());
        char[] cArr = new char[endIndex - startIndex];
        $this$toCharArray.getChars(startIndex, endIndex, cArr, 0);
        return cArr;
    }

    public static final java.lang.String decodeToString(byte[] $this$decodeToString) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$decodeToString, "<this>");
        return new java.lang.String($this$decodeToString, kotlin.text.Charsets.UTF_8);
    }

    public static /* synthetic */ java.lang.String decodeToString$default(byte[] bArr, int i, int i2, boolean z, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.decodeToString(bArr, i, i2, z);
    }

    public static final java.lang.String decodeToString(byte[] $this$decodeToString, int startIndex, int endIndex, boolean throwOnInvalidSequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$decodeToString, "<this>");
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$decodeToString.length);
        if (!throwOnInvalidSequence) {
            return new java.lang.String($this$decodeToString, startIndex, endIndex - startIndex, kotlin.text.Charsets.UTF_8);
        }
        java.nio.charset.CharsetDecoder decoder = kotlin.text.Charsets.UTF_8.newDecoder().onMalformedInput(java.nio.charset.CodingErrorAction.REPORT).onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT);
        java.lang.String string = decoder.decode(java.nio.ByteBuffer.wrap($this$decodeToString, startIndex, endIndex - startIndex)).toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static final byte[] encodeToByteArray(java.lang.String $this$encodeToByteArray) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$encodeToByteArray, "<this>");
        byte[] bytes = $this$encodeToByteArray.getBytes(kotlin.text.Charsets.UTF_8);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return bytes;
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(java.lang.String str, int i, int i2, boolean z, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.encodeToByteArray(str, i, i2, z);
    }

    public static final byte[] encodeToByteArray(java.lang.String $this$encodeToByteArray, int startIndex, int endIndex, boolean throwOnInvalidSequence) throws java.nio.charset.CharacterCodingException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$encodeToByteArray, "<this>");
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$encodeToByteArray.length());
        if (!throwOnInvalidSequence) {
            java.lang.String strSubstring = $this$encodeToByteArray.substring(startIndex, endIndex);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            java.nio.charset.Charset charset = kotlin.text.Charsets.UTF_8;
            kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
            byte[] bytes = strSubstring.getBytes(charset);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            return bytes;
        }
        java.nio.charset.CharsetEncoder encoder = kotlin.text.Charsets.UTF_8.newEncoder().onMalformedInput(java.nio.charset.CodingErrorAction.REPORT).onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPORT);
        java.nio.ByteBuffer byteBuffer = encoder.encode(java.nio.CharBuffer.wrap($this$encodeToByteArray, startIndex, endIndex));
        if (byteBuffer.hasArray() && byteBuffer.arrayOffset() == 0) {
            int iRemaining = byteBuffer.remaining();
            byte[] bArrArray = byteBuffer.array();
            kotlin.jvm.internal.Intrinsics.checkNotNull(bArrArray);
            if (iRemaining == bArrArray.length) {
                byte[] bArrArray2 = byteBuffer.array();
                kotlin.jvm.internal.Intrinsics.checkNotNull(bArrArray2);
                return bArrArray2;
            }
        }
        byte[] it = new byte[byteBuffer.remaining()];
        byteBuffer.get(it);
        return it;
    }

    private static final char[] toCharArray(java.lang.String $this$toCharArray) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        char[] charArray = $this$toCharArray.toCharArray();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(charArray, "toCharArray(...)");
        return charArray;
    }

    static /* synthetic */ char[] toCharArray$default(java.lang.String $this$toCharArray_u24default, char[] destination, int destinationOffset, int startIndex, int endIndex, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            destinationOffset = 0;
        }
        if ((i & 4) != 0) {
            startIndex = 0;
        }
        if ((i & 8) != 0) {
            endIndex = $this$toCharArray_u24default.length();
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toCharArray_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        $this$toCharArray_u24default.getChars(startIndex, endIndex, destination, destinationOffset);
        return destination;
    }

    private static final char[] toCharArray(java.lang.String $this$toCharArray, char[] destination, int destinationOffset, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        $this$toCharArray.getChars(startIndex, endIndex, destination, destinationOffset);
        return destination;
    }

    private static final java.lang.String format(java.lang.String $this$format, java.lang.Object... args) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$format, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(args, "args");
        java.lang.String str = java.lang.String.format($this$format, java.util.Arrays.copyOf(args, args.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private static final java.lang.String format(kotlin.jvm.internal.StringCompanionObject $this$format, java.lang.String format, java.lang.Object... args) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$format, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(args, "args");
        java.lang.String str = java.lang.String.format(format, java.util.Arrays.copyOf(args, args.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private static final java.lang.String format(java.lang.String $this$format, java.util.Locale locale, java.lang.Object... args) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$format, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(args, "args");
        java.lang.String str = java.lang.String.format(locale, $this$format, java.util.Arrays.copyOf(args, args.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    private static final java.lang.String format(kotlin.jvm.internal.StringCompanionObject $this$format, java.util.Locale locale, java.lang.String format, java.lang.Object... args) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$format, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(args, "args");
        java.lang.String str = java.lang.String.format(locale, format, java.util.Arrays.copyOf(args, args.length));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(str, "format(...)");
        return str;
    }

    public static /* synthetic */ java.util.List split$default(java.lang.CharSequence charSequence, java.util.regex.Pattern pattern, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return kotlin.text.StringsKt.split(charSequence, pattern, i);
    }

    public static final java.util.List<java.lang.String> split(java.lang.CharSequence $this$split, java.util.regex.Pattern regex, int limit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$split, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        kotlin.text.StringsKt.requireNonNegativeLimit(limit);
        java.lang.String[] strArrSplit = regex.split($this$split, limit == 0 ? -1 : limit);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strArrSplit, "split(...)");
        return kotlin.collections.ArraysKt.asList(strArrSplit);
    }

    private static final java.lang.String substring(java.lang.String $this$substring, int startIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substring, "<this>");
        java.lang.String strSubstring = $this$substring.substring(startIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    private static final java.lang.String substring(java.lang.String $this$substring, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substring, "<this>");
        java.lang.String strSubstring = $this$substring.substring(startIndex, endIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ boolean startsWith$default(java.lang.String str, java.lang.String str2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.startsWith(str, str2, z);
    }

    public static final boolean startsWith(java.lang.String $this$startsWith, java.lang.String prefix, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase) {
            return $this$startsWith.startsWith(prefix);
        }
        return kotlin.text.StringsKt.regionMatches($this$startsWith, 0, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(java.lang.String str, java.lang.String str2, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.startsWith(str, str2, i, z);
    }

    public static final boolean startsWith(java.lang.String $this$startsWith, java.lang.String prefix, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase) {
            return $this$startsWith.startsWith(prefix, startIndex);
        }
        return kotlin.text.StringsKt.regionMatches($this$startsWith, startIndex, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(java.lang.String str, java.lang.String str2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.endsWith(str, str2, z);
    }

    public static final boolean endsWith(java.lang.String $this$endsWith, java.lang.String suffix, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (!ignoreCase) {
            return $this$endsWith.endsWith(suffix);
        }
        return kotlin.text.StringsKt.regionMatches($this$endsWith, $this$endsWith.length() - suffix.length(), suffix, 0, suffix.length(), true);
    }

    private static final java.lang.String String(byte[] bytes, int offset, int length, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(bytes, "bytes");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.lang.String(bytes, offset, length, charset);
    }

    private static final java.lang.String String(byte[] bytes, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(bytes, "bytes");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.lang.String(bytes, charset);
    }

    private static final java.lang.String String(byte[] bytes, int offset, int length) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(bytes, "bytes");
        return new java.lang.String(bytes, offset, length, kotlin.text.Charsets.UTF_8);
    }

    private static final java.lang.String String(byte[] bytes) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(bytes, "bytes");
        return new java.lang.String(bytes, kotlin.text.Charsets.UTF_8);
    }

    private static final java.lang.String String(char[] chars) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        return new java.lang.String(chars);
    }

    private static final java.lang.String String(char[] chars, int offset, int length) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        return new java.lang.String(chars, offset, length);
    }

    private static final java.lang.String String(int[] codePoints, int offset, int length) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(codePoints, "codePoints");
        return new java.lang.String(codePoints, offset, length);
    }

    private static final java.lang.String String(java.lang.StringBuffer stringBuffer) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stringBuffer, "stringBuffer");
        return new java.lang.String(stringBuffer);
    }

    private static final java.lang.String String(java.lang.StringBuilder stringBuilder) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stringBuilder, "stringBuilder");
        return new java.lang.String(stringBuilder);
    }

    private static final int codePointAt(java.lang.String $this$codePointAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$codePointAt, "<this>");
        return $this$codePointAt.codePointAt(index);
    }

    private static final int codePointBefore(java.lang.String $this$codePointBefore, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$codePointBefore, "<this>");
        return $this$codePointBefore.codePointBefore(index);
    }

    private static final int codePointCount(java.lang.String $this$codePointCount, int beginIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$codePointCount, "<this>");
        return $this$codePointCount.codePointCount(beginIndex, endIndex);
    }

    public static /* synthetic */ int compareTo$default(java.lang.String str, java.lang.String str2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.compareTo(str, str2, z);
    }

    public static final int compareTo(java.lang.String $this$compareTo, java.lang.String other, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$compareTo, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        if (ignoreCase) {
            return $this$compareTo.compareToIgnoreCase(other);
        }
        return $this$compareTo.compareTo(other);
    }

    private static final boolean contentEquals(java.lang.String $this$contentEquals, java.lang.CharSequence charSequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contentEquals, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charSequence, "charSequence");
        return $this$contentEquals.contentEquals(charSequence);
    }

    private static final boolean contentEquals(java.lang.String $this$contentEquals, java.lang.StringBuffer stringBuilder) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contentEquals, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stringBuilder, "stringBuilder");
        return $this$contentEquals.contentEquals(stringBuilder);
    }

    public static final boolean contentEquals(java.lang.CharSequence $this$contentEquals, java.lang.CharSequence other) {
        if (($this$contentEquals instanceof java.lang.String) && other != null) {
            return ((java.lang.String) $this$contentEquals).contentEquals(other);
        }
        return kotlin.text.StringsKt.contentEqualsImpl($this$contentEquals, other);
    }

    public static final boolean contentEquals(java.lang.CharSequence $this$contentEquals, java.lang.CharSequence other, boolean ignoreCase) {
        if (ignoreCase) {
            return kotlin.text.StringsKt.contentEqualsIgnoreCaseImpl($this$contentEquals, other);
        }
        return kotlin.text.StringsKt.contentEquals($this$contentEquals, other);
    }

    private static final java.lang.String intern(java.lang.String $this$intern) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$intern, "<this>");
        java.lang.String strIntern = $this$intern.intern();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strIntern, "intern(...)");
        return strIntern;
    }

    public static final boolean isBlank(java.lang.CharSequence $this$isBlank) {
        boolean z;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isBlank, "<this>");
        if ($this$isBlank.length() == 0) {
            return true;
        }
        java.lang.Iterable $this$all$iv = kotlin.text.StringsKt.getIndices($this$isBlank);
        if (!($this$all$iv instanceof java.util.Collection) || !((java.util.Collection) $this$all$iv).isEmpty()) {
            java.util.Iterator it = $this$all$iv.iterator();
            while (true) {
                if (it.hasNext()) {
                    int element$iv = ((kotlin.collections.IntIterator) it).nextInt();
                    if (!kotlin.text.CharsKt.isWhitespace($this$isBlank.charAt(element$iv))) {
                        z = false;
                        break;
                    }
                } else {
                    z = true;
                    break;
                }
            }
        } else {
            z = true;
        }
        return z;
    }

    private static final int offsetByCodePoints(java.lang.String $this$offsetByCodePoints, int index, int codePointOffset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$offsetByCodePoints, "<this>");
        return $this$offsetByCodePoints.offsetByCodePoints(index, codePointOffset);
    }

    public static /* synthetic */ boolean regionMatches$default(java.lang.CharSequence charSequence, int i, java.lang.CharSequence charSequence2, int i2, int i3, boolean z, int i4, java.lang.Object obj) {
        if ((i4 & 16) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.regionMatches(charSequence, i, charSequence2, i2, i3, z);
    }

    public static final boolean regionMatches(java.lang.CharSequence $this$regionMatches, int thisOffset, java.lang.CharSequence other, int otherOffset, int length, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$regionMatches, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        if (($this$regionMatches instanceof java.lang.String) && (other instanceof java.lang.String)) {
            return kotlin.text.StringsKt.regionMatches((java.lang.String) $this$regionMatches, thisOffset, (java.lang.String) other, otherOffset, length, ignoreCase);
        }
        return kotlin.text.StringsKt.regionMatchesImpl($this$regionMatches, thisOffset, other, otherOffset, length, ignoreCase);
    }

    public static /* synthetic */ boolean regionMatches$default(java.lang.String str, int i, java.lang.String str2, int i2, int i3, boolean z, int i4, java.lang.Object obj) {
        if ((i4 & 16) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.regionMatches(str, i, str2, i2, i3, z);
    }

    public static final boolean regionMatches(java.lang.String $this$regionMatches, int thisOffset, java.lang.String other, int otherOffset, int length, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$regionMatches, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        if (!ignoreCase) {
            return $this$regionMatches.regionMatches(thisOffset, other, otherOffset, length);
        }
        return $this$regionMatches.regionMatches(ignoreCase, thisOffset, other, otherOffset, length);
    }

    @kotlin.Deprecated(message = "Use lowercase() instead.", replaceWith = @kotlin.ReplaceWith(expression = "lowercase(locale)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    private static final java.lang.String toLowerCase(java.lang.String $this$toLowerCase, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toLowerCase, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        java.lang.String lowerCase = $this$toLowerCase.toLowerCase(locale);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return lowerCase;
    }

    private static final java.lang.String lowercase(java.lang.String $this$lowercase, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lowercase, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        java.lang.String lowerCase = $this$lowercase.toLowerCase(locale);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return lowerCase;
    }

    @kotlin.Deprecated(message = "Use uppercase() instead.", replaceWith = @kotlin.ReplaceWith(expression = "uppercase(locale)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    private static final java.lang.String toUpperCase(java.lang.String $this$toUpperCase, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toUpperCase, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        java.lang.String upperCase = $this$toUpperCase.toUpperCase(locale);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        return upperCase;
    }

    private static final java.lang.String uppercase(java.lang.String $this$uppercase, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$uppercase, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        java.lang.String upperCase = $this$uppercase.toUpperCase(locale);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        return upperCase;
    }

    private static final byte[] toByteArray(java.lang.String $this$toByteArray, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toByteArray, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] bytes = $this$toByteArray.getBytes(charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return bytes;
    }

    static /* synthetic */ byte[] toByteArray$default(java.lang.String $this$toByteArray_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toByteArray_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] bytes = $this$toByteArray_u24default.getBytes(charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return bytes;
    }

    static /* synthetic */ java.util.regex.Pattern toPattern$default(java.lang.String $this$toPattern_u24default, int flags, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            flags = 0;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toPattern_u24default, "<this>");
        java.util.regex.Pattern patternCompile = java.util.regex.Pattern.compile($this$toPattern_u24default, flags);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(patternCompile, "compile(...)");
        return patternCompile;
    }

    private static final java.util.regex.Pattern toPattern(java.lang.String $this$toPattern, int flags) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toPattern, "<this>");
        java.util.regex.Pattern patternCompile = java.util.regex.Pattern.compile($this$toPattern, flags);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(patternCompile, "compile(...)");
        return patternCompile;
    }

    @kotlin.Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @kotlin.ReplaceWith(expression = "replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }", imports = {"java.util.Locale"}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    public static final java.lang.String capitalize(java.lang.String $this$capitalize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$capitalize, "<this>");
        java.util.Locale locale = java.util.Locale.getDefault();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(locale, "getDefault(...)");
        return kotlin.text.StringsKt.capitalize($this$capitalize, locale);
    }

    @kotlin.Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @kotlin.ReplaceWith(expression = "replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }", imports = {}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    public static final java.lang.String capitalize(java.lang.String $this$capitalize, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$capitalize, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        if ($this$capitalize.length() > 0) {
            char firstChar = $this$capitalize.charAt(0);
            if (java.lang.Character.isLowerCase(firstChar)) {
                java.lang.StringBuilder $this$capitalize_u24lambda_u245 = new java.lang.StringBuilder();
                char titleChar = java.lang.Character.toTitleCase(firstChar);
                if (titleChar != java.lang.Character.toUpperCase(firstChar)) {
                    $this$capitalize_u24lambda_u245.append(titleChar);
                } else {
                    java.lang.String strSubstring = $this$capitalize.substring(0, 1);
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                    kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
                    java.lang.String upperCase = strSubstring.toUpperCase(locale);
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
                    $this$capitalize_u24lambda_u245.append(upperCase);
                }
                java.lang.String strSubstring2 = $this$capitalize.substring(1);
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
                $this$capitalize_u24lambda_u245.append(strSubstring2);
                java.lang.String string = $this$capitalize_u24lambda_u245.toString();
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                return string;
            }
        }
        return $this$capitalize;
    }

    @kotlin.Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @kotlin.ReplaceWith(expression = "replaceFirstChar { it.lowercase(Locale.getDefault()) }", imports = {"java.util.Locale"}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    public static final java.lang.String decapitalize(java.lang.String $this$decapitalize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$decapitalize, "<this>");
        if (!($this$decapitalize.length() > 0) || java.lang.Character.isLowerCase($this$decapitalize.charAt(0))) {
            return $this$decapitalize;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String strSubstring = $this$decapitalize.substring(0, 1);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
        java.lang.String lowerCase = strSubstring.toLowerCase();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        java.lang.StringBuilder sbAppend = sb.append(lowerCase);
        java.lang.String strSubstring2 = $this$decapitalize.substring(1);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
        return sbAppend.append(strSubstring2).toString();
    }

    @kotlin.Deprecated(message = "Use replaceFirstChar instead.", replaceWith = @kotlin.ReplaceWith(expression = "replaceFirstChar { it.lowercase(locale) }", imports = {}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    public static final java.lang.String decapitalize(java.lang.String $this$decapitalize, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$decapitalize, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        if (!($this$decapitalize.length() > 0) || java.lang.Character.isLowerCase($this$decapitalize.charAt(0))) {
            return $this$decapitalize;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        java.lang.String strSubstring = $this$decapitalize.substring(0, 1);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
        java.lang.String lowerCase = strSubstring.toLowerCase(locale);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        java.lang.StringBuilder sbAppend = sb.append(lowerCase);
        java.lang.String strSubstring2 = $this$decapitalize.substring(1);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring2, "substring(...)");
        return sbAppend.append(strSubstring2).toString();
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [kotlin.collections.IntIterator] */
    public static final java.lang.String repeat(java.lang.CharSequence $this$repeat, int n) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$repeat, "<this>");
        if (!(n >= 0)) {
            throw new java.lang.IllegalArgumentException(("Count 'n' must be non-negative, but was " + n + '.').toString());
        }
        switch (n) {
            case 0:
                return "";
            case 1:
                return $this$repeat.toString();
            default:
                switch ($this$repeat.length()) {
                    case 0:
                        return "";
                    case 1:
                        char cCharAt = $this$repeat.charAt(0);
                        char[] cArr = new char[n];
                        for (int i = 0; i < n; i++) {
                            cArr[i] = cCharAt;
                        }
                        return new java.lang.String(cArr);
                    default:
                        java.lang.StringBuilder sb = new java.lang.StringBuilder($this$repeat.length() * n);
                        ?? it = new kotlin.ranges.IntRange(1, n).iterator();
                        while (it.hasNext()) {
                            it.nextInt();
                            sb.append($this$repeat);
                        }
                        java.lang.String string = sb.toString();
                        kotlin.jvm.internal.Intrinsics.checkNotNull(string);
                        return string;
                }
        }
    }

    public static final java.util.Comparator<java.lang.String> getCASE_INSENSITIVE_ORDER(kotlin.jvm.internal.StringCompanionObject $this$CASE_INSENSITIVE_ORDER) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$CASE_INSENSITIVE_ORDER, "<this>");
        java.util.Comparator<java.lang.String> CASE_INSENSITIVE_ORDER = java.lang.String.CASE_INSENSITIVE_ORDER;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(CASE_INSENSITIVE_ORDER, "CASE_INSENSITIVE_ORDER");
        return CASE_INSENSITIVE_ORDER;
    }
}
