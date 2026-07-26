package com.android.server.permission.jarjar.kotlin.io;

/* JADX INFO: compiled from: ReadWrite.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u001c\u0010\u0007\u001a\u00020\b*\u00020\u00022\u0006\u0010\t\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\u001e\u0010\n\u001a\u00020\u000b*\u00020\u00022\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000b0\r\u001a\u0010\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0010*\u00020\u0001\u001a\n\u0010\u0011\u001a\u00020\u0012*\u00020\u0013\u001a\u0010\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0015*\u00020\u0002\u001a\n\u0010\u0016\u001a\u00020\u000e*\u00020\u0002\u001a\u0017\u0010\u0016\u001a\u00020\u000e*\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u0018H\u0087\b\u001a\r\u0010\u0019\u001a\u00020\u001a*\u00020\u000eH\u0087\b\u001a5\u0010\u001b\u001a\u0002H\u001c\"\u0004\b\u0000\u0010\u001c*\u00020\u00022\u0018\u0010\u001d\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u0010\u0012\u0004\u0012\u0002H\u001c0\rH\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001e\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001f"}, d2 = {"buffered", "Ljava/io/BufferedReader;", "Ljava/io/Reader;", "bufferSize", "", "Ljava/io/BufferedWriter;", "Ljava/io/Writer;", "copyTo", "", "out", "forEachLine", "", "action", "Lkotlin/Function1;", "", "lineSequence", "Lkotlin/sequences/Sequence;", "readBytes", "", "Ljava/net/URL;", "readLines", "", "readText", "charset", "Ljava/nio/charset/Charset;", "reader", "Ljava/io/StringReader;", "useLines", "T", "block", "(Ljava/io/Reader;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class TextStreamsKt {
    static /* synthetic */ java.io.BufferedReader buffered$default(java.io.Reader $this$buffered_u24default, int bufferSize, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            bufferSize = 8192;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered_u24default, "<this>");
        return $this$buffered_u24default instanceof java.io.BufferedReader ? (java.io.BufferedReader) $this$buffered_u24default : new java.io.BufferedReader($this$buffered_u24default, bufferSize);
    }

    private static final java.io.BufferedReader buffered(java.io.Reader $this$buffered, int bufferSize) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered, "<this>");
        return $this$buffered instanceof java.io.BufferedReader ? (java.io.BufferedReader) $this$buffered : new java.io.BufferedReader($this$buffered, bufferSize);
    }

    static /* synthetic */ java.io.BufferedWriter buffered$default(java.io.Writer $this$buffered_u24default, int bufferSize, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            bufferSize = 8192;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered_u24default, "<this>");
        return $this$buffered_u24default instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) $this$buffered_u24default : new java.io.BufferedWriter($this$buffered_u24default, bufferSize);
    }

    private static final java.io.BufferedWriter buffered(java.io.Writer $this$buffered, int bufferSize) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered, "<this>");
        return $this$buffered instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) $this$buffered : new java.io.BufferedWriter($this$buffered, bufferSize);
    }

    public static final void forEachLine(java.io.Reader $this$forEachLine, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.String, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$forEachLine, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.io.BufferedReader bufferedReader = $this$forEachLine instanceof java.io.BufferedReader ? (java.io.BufferedReader) $this$forEachLine : new java.io.BufferedReader($this$forEachLine, 8192);
        try {
            java.io.BufferedReader it$iv = bufferedReader;
            for (java.lang.Object element$iv : lineSequence(it$iv)) {
                function1.invoke(element$iv);
            }
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
        } finally {
        }
    }

    /* JADX INFO: renamed from: com.android.server.permission.jarjar.kotlin.io.TextStreamsKt$readLines$1, reason: invalid class name */
    /* JADX INFO: compiled from: ReadWrite.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
    static final class AnonymousClass1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<java.lang.String, com.android.server.permission.jarjar.kotlin.Unit> {
        final /* synthetic */ java.util.ArrayList<java.lang.String> $result;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(java.util.ArrayList<java.lang.String> arrayList) {
            super(1);
            this.$result = arrayList;
        }

        @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ com.android.server.permission.jarjar.kotlin.Unit invoke(java.lang.String str) {
            invoke2(str);
            return com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(java.lang.String it) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
            this.$result.add(it);
        }
    }

    public static final java.util.List<java.lang.String> readLines(java.io.Reader $this$readLines) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readLines, "<this>");
        java.util.ArrayList result = new java.util.ArrayList();
        forEachLine($this$readLines, new com.android.server.permission.jarjar.kotlin.io.TextStreamsKt.AnonymousClass1(result));
        return result;
    }

    public static final <T> T useLines(java.io.Reader $this$useLines, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.String>, ? extends T> function1) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$useLines, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "block");
        java.io.BufferedReader bufferedReader = $this$useLines instanceof java.io.BufferedReader ? (java.io.BufferedReader) $this$useLines : new java.io.BufferedReader($this$useLines, 8192);
        try {
            java.io.BufferedReader it = bufferedReader;
            T tInvoke = function1.invoke(lineSequence(it));
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
            if (com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(bufferedReader, null);
            } else {
                bufferedReader.close();
            }
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
            return tInvoke;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                if (com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0)) {
                    com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(bufferedReader, th);
                } else {
                    try {
                        bufferedReader.close();
                    } catch (java.lang.Throwable th3) {
                    }
                }
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th2;
            }
        }
    }

    private static final java.io.StringReader reader(java.lang.String $this$reader) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reader, "<this>");
        return new java.io.StringReader($this$reader);
    }

    public static final com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.String> lineSequence(java.io.BufferedReader $this$lineSequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lineSequence, "<this>");
        return com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.constrainOnce(new com.android.server.permission.jarjar.kotlin.io.LinesSequence($this$lineSequence));
    }

    public static final java.lang.String readText(java.io.Reader $this$readText) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readText, "<this>");
        java.io.StringWriter buffer = new java.io.StringWriter();
        copyTo$default($this$readText, buffer, 0, 2, null);
        java.lang.String string = buffer.toString();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public static /* synthetic */ long copyTo$default(java.io.Reader reader, java.io.Writer writer, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 8192;
        }
        return copyTo(reader, writer, i);
    }

    public static final long copyTo(java.io.Reader $this$copyTo, java.io.Writer out, int bufferSize) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$copyTo, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(out, "out");
        long charsCopied = 0;
        char[] buffer = new char[bufferSize];
        int chars = $this$copyTo.read(buffer);
        while (chars >= 0) {
            out.write(buffer, 0, chars);
            charsCopied += (long) chars;
            chars = $this$copyTo.read(buffer);
        }
        return charsCopied;
    }

    private static final java.lang.String readText(java.net.URL $this$readText, java.nio.charset.Charset charset) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readText, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.lang.String(readBytes($this$readText), charset);
    }

    static /* synthetic */ java.lang.String readText$default(java.net.URL $this$readText_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = com.android.server.permission.jarjar.kotlin.text.Charsets.UTF_8;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readText_u24default, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.lang.String(readBytes($this$readText_u24default), charset);
    }

    public static final byte[] readBytes(java.net.URL $this$readBytes) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        java.io.InputStream inputStreamOpenStream = $this$readBytes.openStream();
        try {
            java.io.InputStream it = inputStreamOpenStream;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(it);
            byte[] bytes = com.android.server.permission.jarjar.kotlin.io.ByteStreamsKt.readBytes(it);
            com.android.server.permission.jarjar.kotlin.io.CloseableKt.closeFinally(inputStreamOpenStream, null);
            return bytes;
        } finally {
        }
    }
}
