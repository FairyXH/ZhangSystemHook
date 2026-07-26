package kotlin.text;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Appendable.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u001a5\u0010\u0000\u001a\u0002H\u0001\"\f\b\u0000\u0010\u0001*\u00060\u0002j\u0002`\u0003*\u0002H\u00012\u0016\u0010\u0004\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00060\u0005\"\u0004\u0018\u00010\u0006¢\u0006\u0002\u0010\u0007\u001a9\u0010\b\u001a\u00020\t\"\u0004\b\u0000\u0010\u0001*\u00060\u0002j\u0002`\u00032\u0006\u0010\n\u001a\u0002H\u00012\u0014\u0010\u000b\u001a\u0010\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u00020\u0006\u0018\u00010\fH\u0000¢\u0006\u0002\u0010\r\u001a\u0015\u0010\u000e\u001a\u00060\u0002j\u0002`\u0003*\u00060\u0002j\u0002`\u0003H\u0087\b\u001a\u001d\u0010\u000e\u001a\u00060\u0002j\u0002`\u0003*\u00060\u0002j\u0002`\u00032\u0006\u0010\u0004\u001a\u00020\u000fH\u0087\b\u001a\u001f\u0010\u000e\u001a\u00060\u0002j\u0002`\u0003*\u00060\u0002j\u0002`\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0006H\u0087\b\u001a7\u0010\u0010\u001a\u0002H\u0001\"\f\b\u0000\u0010\u0001*\u00060\u0002j\u0002`\u0003*\u0002H\u00012\u0006\u0010\u0004\u001a\u00020\u00062\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0007¢\u0006\u0002\u0010\u0014¨\u0006\u0015"}, d2 = {"append", "T", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "value", "", "", "(Ljava/lang/Appendable;[Ljava/lang/CharSequence;)Ljava/lang/Appendable;", "appendElement", "", "element", "transform", "Lkotlin/Function1;", "(Ljava/lang/Appendable;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "appendLine", "", "appendRange", "startIndex", "", "endIndex", "(Ljava/lang/Appendable;Ljava/lang/CharSequence;II)Ljava/lang/Appendable;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
public class StringsKt__AppendableKt {
    public static final <T extends java.lang.Appendable> T appendRange(T t, java.lang.CharSequence value, int i, int i2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        T t2 = (T) t.append(value, i, i2);
        kotlin.jvm.internal.Intrinsics.checkNotNull(t2, "null cannot be cast to non-null type T of kotlin.text.StringsKt__AppendableKt.appendRange");
        return t2;
    }

    public static final <T extends java.lang.Appendable> T append(T t, java.lang.CharSequence... value) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        for (java.lang.CharSequence item : value) {
            t.append(item);
        }
        return t;
    }

    private static final java.lang.Appendable appendLine(java.lang.Appendable $this$appendLine) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.Appendable appendableAppend = $this$appendLine.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend, "append(...)");
        return appendableAppend;
    }

    private static final java.lang.Appendable appendLine(java.lang.Appendable $this$appendLine, java.lang.CharSequence value) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.Appendable appendableAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend, "append(...)");
        java.lang.Appendable appendableAppend2 = appendableAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend2, "append(...)");
        return appendableAppend2;
    }

    private static final java.lang.Appendable appendLine(java.lang.Appendable $this$appendLine, char value) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendLine, "<this>");
        java.lang.Appendable appendableAppend = $this$appendLine.append(value);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend, "append(...)");
        java.lang.Appendable appendableAppend2 = appendableAppend.append('\n');
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendableAppend2, "append(...)");
        return appendableAppend2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> void appendElement(java.lang.Appendable $this$appendElement, T t, kotlin.jvm.functions.Function1<? super T, ? extends java.lang.CharSequence> function1) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$appendElement, "<this>");
        if (function1 == null) {
            if (!(t == 0 ? true : t instanceof java.lang.CharSequence)) {
                if (!(t instanceof java.lang.Character)) {
                    $this$appendElement.append(java.lang.String.valueOf(t));
                    return;
                } else {
                    $this$appendElement.append(((java.lang.Character) t).charValue());
                    return;
                }
            }
            $this$appendElement.append((java.lang.CharSequence) t);
            return;
        }
        $this$appendElement.append(function1.invoke(t));
    }
}
