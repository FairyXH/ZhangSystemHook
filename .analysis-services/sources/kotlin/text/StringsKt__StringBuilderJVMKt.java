package kotlin.text;

/* JADX INFO: compiled from: StringBuilderJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\\\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0019\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\u0000\n\u0002\u0010\u000b\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0005\u001a\u001f\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a%\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a-\u0010\u000b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u0012H\u0007\u001a\u001d\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0011j\u0002`\u0012*\u00060\u0011j\u0002`\u00122\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u0014\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004H\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0014H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0015H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\fH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u000fH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0006H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\tH\u0087\b\u001a\u001d\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0003\u001a\u00020\nH\u0087\b\u001a\u001f\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u0016H\u0087\b\u001a%\u0010\u0010\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u000e\u0010\u0003\u001a\n\u0018\u00010\u0001j\u0004\u0018\u0001`\u0002H\u0087\b\u001a\u0014\u0010\u0017\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u0002H\u0007\u001a\u001d\u0010\u0018\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\bH\u0087\b\u001a%\u0010\u001a\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a5\u0010\u001b\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\bH\u0087\b\u001a!\u0010\u001c\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010\u0019\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0013H\u0087\n\u001a-\u0010\u001e\u001a\u00060\u0001j\u0002`\u0002*\u00060\u0001j\u0002`\u00022\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0016H\u0087\b\u001a7\u0010\u001f\u001a\u00020\u001d*\u00060\u0001j\u0002`\u00022\u0006\u0010 \u001a\u00020\f2\b\b\u0002\u0010!\u001a\u00020\b2\b\b\u0002\u0010\r\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\bH\u0087\b¨\u0006\""}, d2 = {"appendLine", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "value", "Ljava/lang/StringBuffer;", "", "", "", "", "", "", "appendRange", "", "startIndex", "endIndex", "", "appendln", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "", "", "", "", "clear", "deleteAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "deleteRange", "insertRange", "set", "", "setRange", "toCharArray", "destination", "destinationOffset", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
class StringsKt__StringBuilderJVMKt extends kotlin.text.StringsKt__RegexExtensionsKt {
    public static final java.lang.StringBuilder clear(java.lang.StringBuilder $this$clear) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$clear, "<this>");
        $this$clear.setLength(0);
        return $this$clear;
    }

    private static final void set(java.lang.StringBuilder $this$set, int index, char value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$set, "<this>");
        $this$set.setCharAt(index, value);
    }

    private static final java.lang.StringBuilder setRange(java.lang.StringBuilder $this$setRange, int startIndex, int endIndex, java.lang.String value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$setRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        java.lang.StringBuilder sbReplace = $this$setRange.replace(startIndex, endIndex, value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbReplace, "replace(...)");
        return sbReplace;
    }

    private static final java.lang.StringBuilder deleteAt(java.lang.StringBuilder $this$deleteAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$deleteAt, "<this>");
        java.lang.StringBuilder sbDeleteCharAt = $this$deleteAt.deleteCharAt(index);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbDeleteCharAt, "deleteCharAt(...)");
        return sbDeleteCharAt;
    }

    private static final java.lang.StringBuilder deleteRange(java.lang.StringBuilder $this$deleteRange, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$deleteRange, "<this>");
        java.lang.StringBuilder sbDelete = $this$deleteRange.delete(startIndex, endIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbDelete, "delete(...)");
        return sbDelete;
    }

    static /* synthetic */ void toCharArray$default(java.lang.StringBuilder $this$toCharArray_u24default, char[] destination, int destinationOffset, int startIndex, int endIndex, int i, java.lang.Object obj) {
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
    }

    private static final void toCharArray(java.lang.StringBuilder $this$toCharArray, char[] destination, int destinationOffset, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toCharArray, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        $this$toCharArray.getChars(startIndex, endIndex, destination, destinationOffset);
    }

    private static final java.lang.StringBuilder appendRange(java.lang.StringBuilder $this$appendRange, char[] value, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        java.lang.StringBuilder sbAppend = $this$appendRange.append(value, startIndex, endIndex - startIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return sbAppend;
    }

    private static final java.lang.StringBuilder appendRange(java.lang.StringBuilder $this$appendRange, java.lang.CharSequence value, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        java.lang.StringBuilder sbAppend = $this$appendRange.append(value, startIndex, endIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return sbAppend;
    }

    private static final java.lang.StringBuilder insertRange(java.lang.StringBuilder $this$insertRange, int index, char[] value, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$insertRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        java.lang.StringBuilder sbInsert = $this$insertRange.insert(index, value, startIndex, endIndex - startIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbInsert, "insert(...)");
        return sbInsert;
    }

    private static final java.lang.StringBuilder insertRange(java.lang.StringBuilder $this$insertRange, int index, java.lang.CharSequence value, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$insertRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        java.lang.StringBuilder sbInsert = $this$insertRange.insert(index, value, startIndex, endIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbInsert, "insert(...)");
        return sbInsert;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, java.lang.StringBuffer value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, java.lang.StringBuilder value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append((java.lang.CharSequence) value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append((int) value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append((int) value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, double value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine()", imports = {}))
    public static final java.lang.Appendable appendln(java.lang.Appendable $this$appendln) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.Appendable appendableAppend = $this$appendln.append(kotlin.text.SystemProperties.LINE_SEPARATOR);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend, "append(...)");
        return appendableAppend;
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.Appendable appendln(java.lang.Appendable $this$appendln, java.lang.CharSequence value) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.Appendable appendableAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(appendableAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.Appendable appendln(java.lang.Appendable $this$appendln, char value) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.Appendable appendableAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(appendableAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine()", imports = {}))
    public static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(kotlin.text.SystemProperties.LINE_SEPARATOR);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return sbAppend;
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, java.lang.StringBuffer value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, java.lang.CharSequence value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, java.lang.String value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, java.lang.Object value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, java.lang.StringBuilder value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append((java.lang.CharSequence) value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, char[] value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, char value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, boolean value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append((int) value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append((int) value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use appendLine instead. Note that the new method always appends the line feed character '\\n' regardless of the system line separator.", replaceWith = @kotlin.ReplaceWith(expression = "appendLine(value)", imports = {}))
    private static final java.lang.StringBuilder appendln(java.lang.StringBuilder $this$appendln, double value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendln, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendln.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return kotlin.text.StringsKt.appendln(sbAppend);
    }
}
