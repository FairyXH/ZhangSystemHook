package kotlin.io;

/* JADX INFO: compiled from: IOStreams.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000Z\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0000\u001a\u00020\u0005*\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0017\u0010\u0007\u001a\u00020\b*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u000b\u001a\u00020\f*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\r\u001a\u00020\u000e*\u00020\u000f2\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u001c\u0010\u0010\u001a\u00020\u0011*\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00062\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u001a\r\u0010\u0013\u001a\u00020\u000e*\u00020\u0014H\u0087\b\u001a\u001d\u0010\u0013\u001a\u00020\u000e*\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0004H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0018*\u00020\u0001H\u0086\u0002\u001a\f\u0010\u0019\u001a\u00020\u0014*\u00020\u0002H\u0007\u001a\u0016\u0010\u0019\u001a\u00020\u0014*\u00020\u00022\b\b\u0002\u0010\u001a\u001a\u00020\u0004H\u0007\u001a\u0017\u0010\u001b\u001a\u00020\u001c*\u00020\u00022\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b\u001a\u0017\u0010\u001d\u001a\u00020\u001e*\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\nH\u0087\b¨\u0006\u001f"}, d2 = {"buffered", "Ljava/io/BufferedInputStream;", "Ljava/io/InputStream;", "bufferSize", "", "Ljava/io/BufferedOutputStream;", "Ljava/io/OutputStream;", "bufferedReader", "Ljava/io/BufferedReader;", "charset", "Ljava/nio/charset/Charset;", "bufferedWriter", "Ljava/io/BufferedWriter;", "byteInputStream", "Ljava/io/ByteArrayInputStream;", "", "copyTo", "", "out", "inputStream", "", "offset", "length", "iterator", "Lkotlin/collections/ByteIterator;", "readBytes", "estimatedSize", "reader", "Ljava/io/InputStreamReader;", "writer", "Ljava/io/OutputStreamWriter;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ByteStreamsKt {
    public static final kotlin.collections.ByteIterator iterator(final java.io.BufferedInputStream $this$iterator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new kotlin.collections.ByteIterator() { // from class: kotlin.io.ByteStreamsKt.iterator.1
            private boolean finished;
            private int nextByte = -1;
            private boolean nextPrepared;

            public final int getNextByte() {
                return this.nextByte;
            }

            public final void setNextByte(int i) {
                this.nextByte = i;
            }

            public final boolean getNextPrepared() {
                return this.nextPrepared;
            }

            public final void setNextPrepared(boolean z) {
                this.nextPrepared = z;
            }

            public final boolean getFinished() {
                return this.finished;
            }

            public final void setFinished(boolean z) {
                this.finished = z;
            }

            private final void prepareNext() {
                if (!this.nextPrepared && !this.finished) {
                    this.nextByte = $this$iterator.read();
                    this.nextPrepared = true;
                    this.finished = this.nextByte == -1;
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                prepareNext();
                return !this.finished;
            }

            @Override // kotlin.collections.ByteIterator
            public byte nextByte() {
                prepareNext();
                if (this.finished) {
                    throw new java.util.NoSuchElementException("Input stream is over.");
                }
                byte res = (byte) this.nextByte;
                this.nextPrepared = false;
                return res;
            }
        };
    }

    private static final java.io.ByteArrayInputStream byteInputStream(java.lang.String $this$byteInputStream, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$byteInputStream, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] bytes = $this$byteInputStream.getBytes(charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return new java.io.ByteArrayInputStream(bytes);
    }

    static /* synthetic */ java.io.ByteArrayInputStream byteInputStream$default(java.lang.String $this$byteInputStream_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$byteInputStream_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        byte[] bytes = $this$byteInputStream_u24default.getBytes(charset);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        return new java.io.ByteArrayInputStream(bytes);
    }

    private static final java.io.ByteArrayInputStream inputStream(byte[] $this$inputStream) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$inputStream, "<this>");
        return new java.io.ByteArrayInputStream($this$inputStream);
    }

    private static final java.io.ByteArrayInputStream inputStream(byte[] $this$inputStream, int offset, int length) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$inputStream, "<this>");
        return new java.io.ByteArrayInputStream($this$inputStream, offset, length);
    }

    static /* synthetic */ java.io.BufferedInputStream buffered$default(java.io.InputStream $this$buffered_u24default, int bufferSize, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            bufferSize = 8192;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered_u24default, "<this>");
        return $this$buffered_u24default instanceof java.io.BufferedInputStream ? (java.io.BufferedInputStream) $this$buffered_u24default : new java.io.BufferedInputStream($this$buffered_u24default, bufferSize);
    }

    private static final java.io.BufferedInputStream buffered(java.io.InputStream $this$buffered, int bufferSize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered, "<this>");
        return $this$buffered instanceof java.io.BufferedInputStream ? (java.io.BufferedInputStream) $this$buffered : new java.io.BufferedInputStream($this$buffered, bufferSize);
    }

    private static final java.io.InputStreamReader reader(java.io.InputStream $this$reader, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reader, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.InputStreamReader($this$reader, charset);
    }

    static /* synthetic */ java.io.InputStreamReader reader$default(java.io.InputStream $this$reader_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reader_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.InputStreamReader($this$reader_u24default, charset);
    }

    private static final java.io.BufferedReader bufferedReader(java.io.InputStream $this$bufferedReader, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedReader, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Reader inputStreamReader = new java.io.InputStreamReader($this$bufferedReader, charset);
        return inputStreamReader instanceof java.io.BufferedReader ? (java.io.BufferedReader) inputStreamReader : new java.io.BufferedReader(inputStreamReader, 8192);
    }

    static /* synthetic */ java.io.BufferedReader bufferedReader$default(java.io.InputStream $this$bufferedReader_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedReader_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Reader inputStreamReader = new java.io.InputStreamReader($this$bufferedReader_u24default, charset);
        return inputStreamReader instanceof java.io.BufferedReader ? (java.io.BufferedReader) inputStreamReader : new java.io.BufferedReader(inputStreamReader, 8192);
    }

    static /* synthetic */ java.io.BufferedOutputStream buffered$default(java.io.OutputStream $this$buffered_u24default, int bufferSize, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            bufferSize = 8192;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered_u24default, "<this>");
        return $this$buffered_u24default instanceof java.io.BufferedOutputStream ? (java.io.BufferedOutputStream) $this$buffered_u24default : new java.io.BufferedOutputStream($this$buffered_u24default, bufferSize);
    }

    private static final java.io.BufferedOutputStream buffered(java.io.OutputStream $this$buffered, int bufferSize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$buffered, "<this>");
        return $this$buffered instanceof java.io.BufferedOutputStream ? (java.io.BufferedOutputStream) $this$buffered : new java.io.BufferedOutputStream($this$buffered, bufferSize);
    }

    private static final java.io.OutputStreamWriter writer(java.io.OutputStream $this$writer, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writer, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.OutputStreamWriter($this$writer, charset);
    }

    static /* synthetic */ java.io.OutputStreamWriter writer$default(java.io.OutputStream $this$writer_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$writer_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        return new java.io.OutputStreamWriter($this$writer_u24default, charset);
    }

    private static final java.io.BufferedWriter bufferedWriter(java.io.OutputStream $this$bufferedWriter, java.nio.charset.Charset charset) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedWriter, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Writer outputStreamWriter = new java.io.OutputStreamWriter($this$bufferedWriter, charset);
        return outputStreamWriter instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) outputStreamWriter : new java.io.BufferedWriter(outputStreamWriter, 8192);
    }

    static /* synthetic */ java.io.BufferedWriter bufferedWriter$default(java.io.OutputStream $this$bufferedWriter_u24default, java.nio.charset.Charset charset, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            charset = kotlin.text.Charsets.UTF_8;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$bufferedWriter_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charset, "charset");
        java.io.Writer outputStreamWriter = new java.io.OutputStreamWriter($this$bufferedWriter_u24default, charset);
        return outputStreamWriter instanceof java.io.BufferedWriter ? (java.io.BufferedWriter) outputStreamWriter : new java.io.BufferedWriter(outputStreamWriter, 8192);
    }

    public static /* synthetic */ long copyTo$default(java.io.InputStream inputStream, java.io.OutputStream outputStream, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 8192;
        }
        return copyTo(inputStream, outputStream, i);
    }

    public static final long copyTo(java.io.InputStream $this$copyTo, java.io.OutputStream out, int bufferSize) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$copyTo, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(out, "out");
        long bytesCopied = 0;
        byte[] buffer = new byte[bufferSize];
        int bytes = $this$copyTo.read(buffer);
        while (bytes >= 0) {
            out.write(buffer, 0, bytes);
            bytesCopied += (long) bytes;
            bytes = $this$copyTo.read(buffer);
        }
        return bytesCopied;
    }

    public static /* synthetic */ byte[] readBytes$default(java.io.InputStream inputStream, int i, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = 8192;
        }
        return readBytes(inputStream, i);
    }

    @kotlin.Deprecated(message = "Use readBytes() overload without estimatedSize parameter", replaceWith = @kotlin.ReplaceWith(expression = "readBytes()", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.5", warningSince = "1.3")
    public static final byte[] readBytes(java.io.InputStream $this$readBytes, int estimatedSize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream(java.lang.Math.max(estimatedSize, $this$readBytes.available()));
        copyTo$default($this$readBytes, buffer, 0, 2, null);
        byte[] byteArray = buffer.toByteArray();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return byteArray;
    }

    public static final byte[] readBytes(java.io.InputStream $this$readBytes) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$readBytes, "<this>");
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream(java.lang.Math.max(8192, $this$readBytes.available()));
        copyTo$default($this$readBytes, buffer, 0, 2, null);
        byte[] byteArray = buffer.toByteArray();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(byteArray, "toByteArray(...)");
        return byteArray;
    }
}
