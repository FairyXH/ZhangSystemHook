package kotlin.text;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: CharJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0001\u001a\u0018\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u00022\u0006\u0010\u000b\u001a\u00020\nH\u0000\u001a\r\u0010\u000e\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0016\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0018\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001a\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\r\u0010\u001b\u001a\u00020\u000f*\u00020\u0002H\u0087\b\u001a\n\u0010\u001c\u001a\u00020\u000f*\u00020\u0002\u001a\r\u0010\u001d\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010\u001d\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010!\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0014\u0010\"\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010#\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010$\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010%\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010&\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010'\u001a\u00020\u001e*\u00020\u0002H\u0087\b\u001a\u0014\u0010'\u001a\u00020\u001e*\u00020\u00022\u0006\u0010\u001f\u001a\u00020 H\u0007\u001a\r\u0010(\u001a\u00020\u0002*\u00020\u0002H\u0087\b\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006)"}, d2 = {"category", "Lkotlin/text/CharCategory;", "", "getCategory", "(C)Lkotlin/text/CharCategory;", "directionality", "Lkotlin/text/CharDirectionality;", "getDirectionality", "(C)Lkotlin/text/CharDirectionality;", "checkRadix", "", "radix", "digitOf", "char", "isDefined", "", "isDigit", "isHighSurrogate", "isISOControl", "isIdentifierIgnorable", "isJavaIdentifierPart", "isJavaIdentifierStart", "isLetter", "isLetterOrDigit", "isLowSurrogate", "isLowerCase", "isTitleCase", "isUpperCase", "isWhitespace", "lowercase", "", com.android.server.voiceinteraction.DatabaseHelper.SoundModelContract.KEY_LOCALE, "Ljava/util/Locale;", "lowercaseChar", "titlecase", "titlecaseChar", "toLowerCase", "toTitleCase", "toUpperCase", "uppercase", "uppercaseChar", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/CharsKt")
public class CharsKt__CharJVMKt {
    public static final kotlin.text.CharCategory getCategory(char $this$category) {
        return kotlin.text.CharCategory.INSTANCE.valueOf(java.lang.Character.getType($this$category));
    }

    private static final boolean isDefined(char $this$isDefined) {
        return java.lang.Character.isDefined($this$isDefined);
    }

    private static final boolean isLetter(char $this$isLetter) {
        return java.lang.Character.isLetter($this$isLetter);
    }

    private static final boolean isLetterOrDigit(char $this$isLetterOrDigit) {
        return java.lang.Character.isLetterOrDigit($this$isLetterOrDigit);
    }

    private static final boolean isDigit(char $this$isDigit) {
        return java.lang.Character.isDigit($this$isDigit);
    }

    private static final boolean isIdentifierIgnorable(char $this$isIdentifierIgnorable) {
        return java.lang.Character.isIdentifierIgnorable($this$isIdentifierIgnorable);
    }

    private static final boolean isISOControl(char $this$isISOControl) {
        return java.lang.Character.isISOControl($this$isISOControl);
    }

    private static final boolean isJavaIdentifierPart(char $this$isJavaIdentifierPart) {
        return java.lang.Character.isJavaIdentifierPart($this$isJavaIdentifierPart);
    }

    private static final boolean isJavaIdentifierStart(char $this$isJavaIdentifierStart) {
        return java.lang.Character.isJavaIdentifierStart($this$isJavaIdentifierStart);
    }

    public static final boolean isWhitespace(char $this$isWhitespace) {
        return java.lang.Character.isWhitespace($this$isWhitespace) || java.lang.Character.isSpaceChar($this$isWhitespace);
    }

    private static final boolean isUpperCase(char $this$isUpperCase) {
        return java.lang.Character.isUpperCase($this$isUpperCase);
    }

    private static final boolean isLowerCase(char $this$isLowerCase) {
        return java.lang.Character.isLowerCase($this$isLowerCase);
    }

    @kotlin.Deprecated(message = "Use uppercaseChar() instead.", replaceWith = @kotlin.ReplaceWith(expression = "uppercaseChar()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    private static final char toUpperCase(char $this$toUpperCase) {
        return java.lang.Character.toUpperCase($this$toUpperCase);
    }

    private static final char uppercaseChar(char $this$uppercaseChar) {
        return java.lang.Character.toUpperCase($this$uppercaseChar);
    }

    private static final java.lang.String uppercase(char $this$uppercase) {
        java.lang.String strValueOf = java.lang.String.valueOf($this$uppercase);
        kotlin.jvm.internal.Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
        java.lang.String upperCase = strValueOf.toUpperCase(java.util.Locale.ROOT);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        return upperCase;
    }

    public static final java.lang.String uppercase(char $this$uppercase, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        java.lang.String strValueOf = java.lang.String.valueOf($this$uppercase);
        kotlin.jvm.internal.Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
        java.lang.String upperCase = strValueOf.toUpperCase(locale);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        return upperCase;
    }

    @kotlin.Deprecated(message = "Use lowercaseChar() instead.", replaceWith = @kotlin.ReplaceWith(expression = "lowercaseChar()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    private static final char toLowerCase(char $this$toLowerCase) {
        return java.lang.Character.toLowerCase($this$toLowerCase);
    }

    private static final char lowercaseChar(char $this$lowercaseChar) {
        return java.lang.Character.toLowerCase($this$lowercaseChar);
    }

    private static final java.lang.String lowercase(char $this$lowercase) {
        java.lang.String strValueOf = java.lang.String.valueOf($this$lowercase);
        kotlin.jvm.internal.Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
        java.lang.String lowerCase = strValueOf.toLowerCase(java.util.Locale.ROOT);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return lowerCase;
    }

    public static final java.lang.String lowercase(char $this$lowercase, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        java.lang.String strValueOf = java.lang.String.valueOf($this$lowercase);
        kotlin.jvm.internal.Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
        java.lang.String lowerCase = strValueOf.toLowerCase(locale);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
        return lowerCase;
    }

    private static final boolean isTitleCase(char $this$isTitleCase) {
        return java.lang.Character.isTitleCase($this$isTitleCase);
    }

    @kotlin.Deprecated(message = "Use titlecaseChar() instead.", replaceWith = @kotlin.ReplaceWith(expression = "titlecaseChar()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(warningSince = "1.5")
    private static final char toTitleCase(char $this$toTitleCase) {
        return java.lang.Character.toTitleCase($this$toTitleCase);
    }

    private static final char titlecaseChar(char $this$titlecaseChar) {
        return java.lang.Character.toTitleCase($this$titlecaseChar);
    }

    public static final java.lang.String titlecase(char $this$titlecase, java.util.Locale locale) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(locale, "locale");
        java.lang.String localizedUppercase = kotlin.text.CharsKt.uppercase($this$titlecase, locale);
        if (localizedUppercase.length() > 1) {
            if ($this$titlecase == 329) {
                return localizedUppercase;
            }
            char cCharAt = localizedUppercase.charAt(0);
            kotlin.jvm.internal.Intrinsics.checkNotNull(localizedUppercase, "null cannot be cast to non-null type java.lang.String");
            java.lang.String strSubstring = localizedUppercase.substring(1);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
            java.lang.String lowerCase = strSubstring.toLowerCase(java.util.Locale.ROOT);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
            return cCharAt + lowerCase;
        }
        java.lang.String strValueOf = java.lang.String.valueOf($this$titlecase);
        kotlin.jvm.internal.Intrinsics.checkNotNull(strValueOf, "null cannot be cast to non-null type java.lang.String");
        java.lang.String upperCase = strValueOf.toUpperCase(java.util.Locale.ROOT);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(upperCase, "toUpperCase(...)");
        if (!kotlin.jvm.internal.Intrinsics.areEqual(localizedUppercase, upperCase)) {
            return localizedUppercase;
        }
        return java.lang.String.valueOf(java.lang.Character.toTitleCase($this$titlecase));
    }

    public static final kotlin.text.CharDirectionality getDirectionality(char $this$directionality) {
        return kotlin.text.CharDirectionality.INSTANCE.valueOf(java.lang.Character.getDirectionality($this$directionality));
    }

    private static final boolean isHighSurrogate(char $this$isHighSurrogate) {
        return java.lang.Character.isHighSurrogate($this$isHighSurrogate);
    }

    private static final boolean isLowSurrogate(char $this$isLowSurrogate) {
        return java.lang.Character.isLowSurrogate($this$isLowSurrogate);
    }

    public static final int digitOf(char c, int radix) {
        return java.lang.Character.digit((int) c, radix);
    }

    public static final int checkRadix(int radix) {
        if (!new kotlin.ranges.IntRange(2, 36).contains(radix)) {
            throw new java.lang.IllegalArgumentException("radix " + radix + " was not in valid range " + new kotlin.ranges.IntRange(2, 36));
        }
        return radix;
    }
}
