package kotlin.text;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Indent.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u000b\u001a!\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0002¢\u0006\u0002\b\u0004\u001a\u0011\u0010\u0005\u001a\u00020\u0006*\u00020\u0002H\u0002¢\u0006\u0002\b\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0002\u001aJ\u0010\t\u001a\u00020\u0002*\b\u0012\u0004\u0012\u00020\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00020\u00012\u0014\u0010\r\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001H\u0082\b¢\u0006\u0002\b\u000e\u001a\u0014\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u0002\u001a\u001e\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0010\u001a\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002\u001a\f\u0010\u0013\u001a\u00020\u0002*\u00020\u0002H\u0007\u001a\u0016\u0010\u0014\u001a\u00020\u0002*\u00020\u00022\b\b\u0002\u0010\u0012\u001a\u00020\u0002H\u0007¨\u0006\u0015"}, d2 = {"getIndentFunction", "Lkotlin/Function1;", "", "indent", "getIndentFunction$StringsKt__IndentKt", "indentWidth", "", "indentWidth$StringsKt__IndentKt", "prependIndent", "reindent", "", "resultSizeEstimate", "indentAddFunction", "indentCutFunction", "reindent$StringsKt__IndentKt", "replaceIndent", "newIndent", "replaceIndentByMargin", "marginPrefix", "trimIndent", "trimMargin", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
public class StringsKt__IndentKt extends kotlin.text.StringsKt__AppendableKt {
    public static /* synthetic */ java.lang.String trimMargin$default(java.lang.String str, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str2 = "|";
        }
        return kotlin.text.StringsKt.trimMargin(str, str2);
    }

    public static final java.lang.String trimMargin(java.lang.String $this$trimMargin, java.lang.String marginPrefix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimMargin, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(marginPrefix, "marginPrefix");
        return kotlin.text.StringsKt.replaceIndentByMargin($this$trimMargin, "", marginPrefix);
    }

    public static /* synthetic */ java.lang.String replaceIndentByMargin$default(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str2 = "";
        }
        if ((i & 2) != 0) {
            str3 = "|";
        }
        return kotlin.text.StringsKt.replaceIndentByMargin(str, str2, str3);
    }

    public static final java.lang.String replaceIndentByMargin(java.lang.String $this$replaceIndentByMargin, java.lang.String newIndent, java.lang.String marginPrefix) {
        java.util.Collection destination$iv$iv$iv;
        java.lang.String strSubstring;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceIndentByMargin, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(marginPrefix, "marginPrefix");
        if (!(!kotlin.text.StringsKt.isBlank(marginPrefix))) {
            throw new java.lang.IllegalArgumentException("marginPrefix must be non-blank string.".toString());
        }
        java.util.List<java.lang.String> listLines = kotlin.text.StringsKt.lines($this$replaceIndentByMargin);
        int resultSizeEstimate$iv = $this$replaceIndentByMargin.length() + (newIndent.length() * listLines.size());
        kotlin.jvm.functions.Function1<java.lang.String, java.lang.String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(newIndent);
        int lastIndex$iv = kotlin.collections.CollectionsKt.getLastIndex(listLines);
        java.util.List<java.lang.String> $this$mapIndexedNotNull$iv$iv = listLines;
        java.util.Collection destination$iv$iv$iv2 = new java.util.ArrayList();
        int index$iv$iv$iv$iv = 0;
        for (java.lang.Object item$iv$iv$iv$iv : $this$mapIndexedNotNull$iv$iv) {
            int index$iv$iv$iv$iv2 = index$iv$iv$iv$iv + 1;
            if (index$iv$iv$iv$iv < 0) {
                kotlin.collections.CollectionsKt.throwIndexOverflow();
            }
            int index$iv$iv$iv = index$iv$iv$iv$iv;
            java.lang.String value$iv = (java.lang.String) item$iv$iv$iv$iv;
            java.lang.String strInvoke = null;
            if ((index$iv$iv$iv == 0 || index$iv$iv$iv == lastIndex$iv) && kotlin.text.StringsKt.isBlank(value$iv)) {
                destination$iv$iv$iv = destination$iv$iv$iv2;
            } else {
                java.lang.String $this$indexOfFirst$iv = value$iv;
                int $i$f$indexOfFirst = 0;
                int index$iv = 0;
                int length = $this$indexOfFirst$iv.length();
                while (true) {
                    int $i$f$indexOfFirst2 = $i$f$indexOfFirst;
                    if (index$iv >= length) {
                        index$iv = -1;
                        break;
                    }
                    char it = $this$indexOfFirst$iv.charAt(index$iv);
                    if (!kotlin.text.CharsKt.isWhitespace(it)) {
                        break;
                    }
                    index$iv++;
                    $i$f$indexOfFirst = $i$f$indexOfFirst2;
                }
                int firstNonWhitespaceIndex = index$iv;
                if (firstNonWhitespaceIndex == -1) {
                    destination$iv$iv$iv = destination$iv$iv$iv2;
                    strSubstring = null;
                } else {
                    destination$iv$iv$iv = destination$iv$iv$iv2;
                    if (kotlin.text.StringsKt.startsWith$default(value$iv, marginPrefix, firstNonWhitespaceIndex, false, 4, (java.lang.Object) null)) {
                        int length2 = marginPrefix.length() + firstNonWhitespaceIndex;
                        kotlin.jvm.internal.Intrinsics.checkNotNull(value$iv, "null cannot be cast to non-null type java.lang.String");
                        strSubstring = value$iv.substring(length2);
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
                    } else {
                        strSubstring = null;
                    }
                }
                if (strSubstring == null || (strInvoke = indentFunction$StringsKt__IndentKt.invoke(strSubstring)) == null) {
                    strInvoke = value$iv;
                }
            }
            if (strInvoke != null) {
                destination$iv$iv$iv.add(strInvoke);
            }
            destination$iv$iv$iv2 = destination$iv$iv$iv;
            index$iv$iv$iv$iv = index$iv$iv$iv$iv2;
        }
        java.lang.String string = ((java.lang.StringBuilder) kotlin.collections.CollectionsKt.joinTo((java.util.List) destination$iv$iv$iv2, new java.lang.StringBuilder(resultSizeEstimate$iv), (124 & 2) != 0 ? ", " : "\n", (124 & 4) != 0 ? "" : null, (124 & 8) != 0 ? "" : null, (124 & 16) != 0 ? -1 : 0, (124 & 32) != 0 ? "..." : null, (124 & 64) != 0 ? null : null)).toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static final java.lang.String trimIndent(java.lang.String $this$trimIndent) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimIndent, "<this>");
        return kotlin.text.StringsKt.replaceIndent($this$trimIndent, "");
    }

    public static /* synthetic */ java.lang.String replaceIndent$default(java.lang.String str, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str2 = "";
        }
        return kotlin.text.StringsKt.replaceIndent(str, str2);
    }

    public static final java.lang.String replaceIndent(java.lang.String $this$replaceIndent, java.lang.String newIndent) {
        java.lang.String strInvoke;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceIndent, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        java.util.List<java.lang.String> listLines = kotlin.text.StringsKt.lines($this$replaceIndent);
        java.util.List<java.lang.String> $this$filter$iv = listLines;
        java.util.Collection destination$iv$iv = new java.util.ArrayList();
        for (java.lang.Object element$iv$iv : $this$filter$iv) {
            java.lang.String p0 = (java.lang.String) element$iv$iv;
            if (!kotlin.text.StringsKt.isBlank(p0)) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        java.lang.Iterable $this$map$iv = (java.util.List) destination$iv$iv;
        java.util.Collection destination$iv$iv2 = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (java.lang.Object item$iv$iv : $this$map$iv) {
            java.lang.String p02 = (java.lang.String) item$iv$iv;
            destination$iv$iv2.add(java.lang.Integer.valueOf(indentWidth$StringsKt__IndentKt(p02)));
        }
        java.lang.Integer num = (java.lang.Integer) kotlin.collections.CollectionsKt.minOrNull(destination$iv$iv2);
        int minCommonIndent = num != null ? num.intValue() : 0;
        int resultSizeEstimate$iv = $this$replaceIndent.length() + (newIndent.length() * listLines.size());
        kotlin.jvm.functions.Function1<java.lang.String, java.lang.String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(newIndent);
        int lastIndex$iv = kotlin.collections.CollectionsKt.getLastIndex(listLines);
        java.util.List<java.lang.String> $this$mapIndexedNotNull$iv$iv = listLines;
        java.util.Collection destination$iv$iv$iv = new java.util.ArrayList();
        int index$iv$iv$iv$iv = 0;
        for (java.lang.Object item$iv$iv$iv$iv : $this$mapIndexedNotNull$iv$iv) {
            int index$iv$iv$iv$iv2 = index$iv$iv$iv$iv + 1;
            if (index$iv$iv$iv$iv < 0) {
                kotlin.collections.CollectionsKt.throwIndexOverflow();
            }
            java.lang.String value$iv = (java.lang.String) item$iv$iv$iv$iv;
            int index$iv = index$iv$iv$iv$iv;
            if ((index$iv == 0 || index$iv == lastIndex$iv) && kotlin.text.StringsKt.isBlank(value$iv)) {
                strInvoke = null;
            } else {
                java.lang.String line = kotlin.text.StringsKt.drop(value$iv, minCommonIndent);
                if (line == null || (strInvoke = indentFunction$StringsKt__IndentKt.invoke(line)) == null) {
                    strInvoke = value$iv;
                }
            }
            if (strInvoke != null) {
                destination$iv$iv$iv.add(strInvoke);
            }
            index$iv$iv$iv$iv = index$iv$iv$iv$iv2;
        }
        java.lang.String string = ((java.lang.StringBuilder) kotlin.collections.CollectionsKt.joinTo((java.util.List) destination$iv$iv$iv, new java.lang.StringBuilder(resultSizeEstimate$iv), (124 & 2) != 0 ? ", " : "\n", (124 & 4) != 0 ? "" : null, (124 & 8) != 0 ? "" : null, (124 & 16) != 0 ? -1 : 0, (124 & 32) != 0 ? "..." : null, (124 & 64) != 0 ? null : null)).toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static /* synthetic */ java.lang.String prependIndent$default(java.lang.String str, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str2 = "    ";
        }
        return kotlin.text.StringsKt.prependIndent(str, str2);
    }

    public static final java.lang.String prependIndent(java.lang.String $this$prependIndent, final java.lang.String indent) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$prependIndent, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(indent, "indent");
        return kotlin.sequences.SequencesKt.joinToString$default(kotlin.sequences.SequencesKt.map(kotlin.text.StringsKt.lineSequence($this$prependIndent), new kotlin.jvm.functions.Function1<java.lang.String, java.lang.String>() { // from class: kotlin.text.StringsKt__IndentKt.prependIndent.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final java.lang.String invoke(java.lang.String it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                if (kotlin.text.StringsKt.isBlank(it)) {
                    return it.length() < indent.length() ? indent : it;
                }
                return indent + it;
            }
        }), "\n", null, null, 0, null, null, 62, null);
    }

    private static final int indentWidth$StringsKt__IndentKt(java.lang.String $this$indentWidth) {
        java.lang.String $this$indexOfFirst$iv = $this$indentWidth;
        int index$iv = 0;
        int length = $this$indexOfFirst$iv.length();
        while (true) {
            if (index$iv >= length) {
                index$iv = -1;
                break;
            }
            if (!kotlin.text.CharsKt.isWhitespace($this$indexOfFirst$iv.charAt(index$iv))) {
                break;
            }
            index$iv++;
        }
        int it = index$iv;
        return it == -1 ? $this$indentWidth.length() : it;
    }

    private static final kotlin.jvm.functions.Function1<java.lang.String, java.lang.String> getIndentFunction$StringsKt__IndentKt(final java.lang.String indent) {
        return indent.length() == 0 ? new kotlin.jvm.functions.Function1<java.lang.String, java.lang.String>() { // from class: kotlin.text.StringsKt__IndentKt$getIndentFunction$1
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.String invoke(java.lang.String line) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(line, "line");
                return line;
            }
        } : new kotlin.jvm.functions.Function1<java.lang.String, java.lang.String>() { // from class: kotlin.text.StringsKt__IndentKt$getIndentFunction$2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final java.lang.String invoke(java.lang.String line) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(line, "line");
                return indent + line;
            }
        };
    }

    private static final java.lang.String reindent$StringsKt__IndentKt(java.util.List<java.lang.String> list, int resultSizeEstimate, kotlin.jvm.functions.Function1<? super java.lang.String, java.lang.String> function1, kotlin.jvm.functions.Function1<? super java.lang.String, java.lang.String> function12) {
        int lastIndex;
        java.lang.String strInvoke;
        int $i$f$reindent = 0;
        int lastIndex2 = kotlin.collections.CollectionsKt.getLastIndex(list);
        java.util.List<java.lang.String> $this$mapIndexedNotNull$iv = list;
        java.util.Collection destination$iv$iv = new java.util.ArrayList();
        int index$iv$iv = 0;
        for (java.lang.Object item$iv$iv$iv : $this$mapIndexedNotNull$iv) {
            int index$iv$iv$iv = index$iv$iv + 1;
            if (index$iv$iv < 0) {
                if (!kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 3, 0)) {
                    throw new java.lang.ArithmeticException("Index overflow has happened.");
                }
                kotlin.collections.CollectionsKt.throwIndexOverflow();
            }
            java.lang.String value = (java.lang.String) item$iv$iv$iv;
            int index = index$iv$iv;
            int $i$f$reindent2 = $i$f$reindent;
            if ((index == 0 || index == lastIndex2) && kotlin.text.StringsKt.isBlank(value)) {
                lastIndex = lastIndex2;
                strInvoke = null;
            } else {
                java.lang.String strInvoke2 = function12.invoke(value);
                if (strInvoke2 != null) {
                    lastIndex = lastIndex2;
                    strInvoke = function1.invoke(strInvoke2);
                    if (strInvoke == null) {
                    }
                } else {
                    lastIndex = lastIndex2;
                }
                strInvoke = value;
            }
            if (strInvoke != null) {
                destination$iv$iv.add(strInvoke);
            }
            index$iv$iv = index$iv$iv$iv;
            $i$f$reindent = $i$f$reindent2;
            lastIndex2 = lastIndex;
        }
        java.lang.String string = ((java.lang.StringBuilder) kotlin.collections.CollectionsKt.joinTo((java.util.List) destination$iv$iv, new java.lang.StringBuilder(resultSizeEstimate), (124 & 2) != 0 ? ", " : "\n", (124 & 4) != 0 ? "" : null, (124 & 8) != 0 ? "" : null, (124 & 16) != 0 ? -1 : 0, (124 & 32) != 0 ? "..." : null, (124 & 64) != 0 ? null : null)).toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }
}
