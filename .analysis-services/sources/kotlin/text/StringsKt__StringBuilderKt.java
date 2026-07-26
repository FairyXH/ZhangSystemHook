package kotlin.text;

/* JADX INFO: compiled from: StringBuilder.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000J\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u0019\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\u001a>\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u001b\u0010\u0004\u001a\u0017\u0012\b\u0012\u00060\u0006j\u0002`\u0007\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\u0002\b\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001a6\u0010\u0000\u001a\u00020\u00012\u001b\u0010\u0004\u001a\u0017\u0012\b\u0012\u00060\u0006j\u0002`\u0007\u0012\u0004\u0012\u00020\b0\u0005¢\u0006\u0002\b\tH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\u001f\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0087\b\u001a/\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0016\u0010\r\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\f0\u000e\"\u0004\u0018\u00010\f¢\u0006\u0002\u0010\u000f\u001a/\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0016\u0010\r\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00010\u000e\"\u0004\u0018\u00010\u0001¢\u0006\u0002\u0010\u0010\u001a-\u0010\n\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0015\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u0007H\u0087\b\u001a\u001f\u0010\u0015\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\r\u001a\u0004\u0018\u00010\fH\u0087\b\u001a\u001d\u0010\u0015\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\r\u001a\u00020\u0016H\u0087\b\u001a\u001d\u0010\u0015\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\r\u001a\u00020\u0017H\u0087\b\u001a\u001d\u0010\u0015\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\u0006\u0010\r\u001a\u00020\u0012H\u0087\b\u001a\u001f\u0010\u0015\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\r\u001a\u0004\u0018\u00010\u0018H\u0087\b\u001a\u001f\u0010\u0015\u001a\u00060\u0006j\u0002`\u0007*\u00060\u0006j\u0002`\u00072\b\u0010\r\u001a\u0004\u0018\u00010\u0001H\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0019"}, d2 = {"buildString", "", "capacity", "", "builderAction", "Lkotlin/Function1;", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "", "Lkotlin/ExtensionFunctionType;", "append", "obj", "", "value", "", "(Ljava/lang/StringBuilder;[Ljava/lang/Object;)Ljava/lang/StringBuilder;", "(Ljava/lang/StringBuilder;[Ljava/lang/String;)Ljava/lang/StringBuilder;", "str", "", "offset", "len", "appendLine", "", "", "", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
class StringsKt__StringBuilderKt extends kotlin.text.StringsKt__StringBuilderJVMKt {
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.WARNING, message = "Use append(value: Any?) instead", replaceWith = @kotlin.ReplaceWith(expression = "append(value = obj)", imports = {}))
    private static final java.lang.StringBuilder append(java.lang.StringBuilder $this$append, java.lang.Object obj) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$append, "<this>");
        java.lang.StringBuilder sbAppend = $this$append.append(obj);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return sbAppend;
    }

    private static final java.lang.String buildString(kotlin.jvm.functions.Function1<? super java.lang.StringBuilder, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        builderAction.invoke(sb);
        java.lang.String string = sb.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private static final java.lang.String buildString(int capacity, kotlin.jvm.functions.Function1<? super java.lang.StringBuilder, kotlin.Unit> builderAction) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(builderAction, "builderAction");
        java.lang.StringBuilder sb = new java.lang.StringBuilder(capacity);
        builderAction.invoke(sb);
        java.lang.String string = sb.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static final java.lang.StringBuilder append(java.lang.StringBuilder $this$append, java.lang.String... value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$append, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        for (java.lang.String item : value) {
            $this$append.append(item);
        }
        return $this$append;
    }

    public static final java.lang.StringBuilder append(java.lang.StringBuilder $this$append, java.lang.Object... value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$append, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        for (java.lang.Object item : value) {
            $this$append.append(item);
        }
        return $this$append;
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use appendRange instead.", replaceWith = @kotlin.ReplaceWith(expression = "this.appendRange(str, offset, offset + len)", imports = {}))
    private static final java.lang.StringBuilder append(java.lang.StringBuilder $this$append, char[] str, int offset, int len) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$append, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "str");
        throw new kotlin.NotImplementedError(null, 1, null);
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        return sbAppend;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, java.lang.CharSequence value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, java.lang.String value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, java.lang.Object value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, char[] value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, char value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }

    private static final java.lang.StringBuilder appendLine(java.lang.StringBuilder $this$appendLine, boolean value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.StringBuilder sbAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        java.lang.StringBuilder sbAppend2 = sbAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        return sbAppend2;
    }
}
