package com.android.server.permission.jarjar.kotlin.io.encoding;

/* JADX INFO: compiled from: Base64IOStream.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\r\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0016J \u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH\u0002J\b\u0010\u0015\u001a\u00020\u000fH\u0002J \u0010\u0016\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\tH\u0002J\b\u0010\u0017\u001a\u00020\u000fH\u0016J \u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\tH\u0016J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lkotlin/io/encoding/EncodeOutputStream;", "Ljava/io/OutputStream;", "output", "base64", "Lkotlin/io/encoding/Base64;", "(Ljava/io/OutputStream;Lkotlin/io/encoding/Base64;)V", "byteBuffer", "", "byteBufferLength", "", "isClosed", "", "lineLength", "symbolBuffer", "checkOpen", "", "close", "copyIntoByteBuffer", "source", "startIndex", "endIndex", "encodeByteBufferIntoOutput", "encodeIntoOutput", "flush", "write", "offset", "length", "b", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class EncodeOutputStream extends java.io.OutputStream {
    private final com.android.server.permission.jarjar.kotlin.io.encoding.Base64 base64;
    private final byte[] byteBuffer;
    private int byteBufferLength;
    private boolean isClosed;
    private int lineLength;
    private final java.io.OutputStream output;
    private final byte[] symbolBuffer;

    public EncodeOutputStream(java.io.OutputStream output, com.android.server.permission.jarjar.kotlin.io.encoding.Base64 base64) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(output, "output");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base64, "base64");
        this.output = output;
        this.base64 = base64;
        this.lineLength = this.base64.isMimeScheme$kotlin_stdlib() ? 76 : -1;
        this.symbolBuffer = new byte[1024];
        this.byteBuffer = new byte[3];
    }

    @Override // java.io.OutputStream
    public void write(int b) throws java.io.IOException {
        checkOpen();
        byte[] bArr = this.byteBuffer;
        int i = this.byteBufferLength;
        this.byteBufferLength = i + 1;
        bArr[i] = (byte) b;
        if (this.byteBufferLength == 3) {
            encodeByteBufferIntoOutput();
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] source, int offset, int length) throws java.io.IOException {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        checkOpen();
        if (offset < 0 || length < 0 || offset + length > source.length) {
            throw new java.lang.IndexOutOfBoundsException("offset: " + offset + ", length: " + length + ", source size: " + source.length);
        }
        if (length == 0) {
            return;
        }
        if (!(this.byteBufferLength < 3)) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        int startIndex = offset;
        int endIndex = startIndex + length;
        if (this.byteBufferLength != 0) {
            startIndex += copyIntoByteBuffer(source, startIndex, endIndex);
            if (this.byteBufferLength != 0) {
                return;
            }
        }
        while (startIndex + 3 <= endIndex) {
            int groupCapacity = (this.base64.isMimeScheme$kotlin_stdlib() ? this.lineLength : this.symbolBuffer.length) / 4;
            int groupsToEncode = java.lang.Math.min(groupCapacity, (endIndex - startIndex) / 3);
            int bytesToEncode = groupsToEncode * 3;
            int symbolsEncoded = encodeIntoOutput(source, startIndex, startIndex + bytesToEncode);
            if (!(symbolsEncoded == groupsToEncode * 4)) {
                throw new java.lang.IllegalStateException("Check failed.".toString());
            }
            startIndex += bytesToEncode;
        }
        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(source, this.byteBuffer, 0, startIndex, endIndex);
        this.byteBufferLength = endIndex - startIndex;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws java.io.IOException {
        checkOpen();
        this.output.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws java.io.IOException {
        if (!this.isClosed) {
            this.isClosed = true;
            if (this.byteBufferLength != 0) {
                encodeByteBufferIntoOutput();
            }
            this.output.close();
        }
    }

    private final int copyIntoByteBuffer(byte[] source, int startIndex, int endIndex) throws java.io.IOException {
        int bytesToCopy = java.lang.Math.min(3 - this.byteBufferLength, endIndex - startIndex);
        com.android.server.permission.jarjar.kotlin.collections.ArraysKt.copyInto(source, this.byteBuffer, this.byteBufferLength, startIndex, startIndex + bytesToCopy);
        this.byteBufferLength += bytesToCopy;
        if (this.byteBufferLength == 3) {
            encodeByteBufferIntoOutput();
        }
        return bytesToCopy;
    }

    private final void encodeByteBufferIntoOutput() throws java.io.IOException {
        int symbolsEncoded = encodeIntoOutput(this.byteBuffer, 0, this.byteBufferLength);
        if (!(symbolsEncoded == 4)) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        this.byteBufferLength = 0;
    }

    private final int encodeIntoOutput(byte[] source, int startIndex, int endIndex) throws java.io.IOException {
        int symbolsEncoded = this.base64.encodeIntoByteArray(source, this.symbolBuffer, 0, startIndex, endIndex);
        if (this.lineLength == 0) {
            this.output.write(com.android.server.permission.jarjar.kotlin.io.encoding.Base64.Default.getMimeLineSeparatorSymbols$kotlin_stdlib());
            this.lineLength = 76;
            if (!(symbolsEncoded <= 76)) {
                throw new java.lang.IllegalStateException("Check failed.".toString());
            }
        }
        this.output.write(this.symbolBuffer, 0, symbolsEncoded);
        this.lineLength -= symbolsEncoded;
        return symbolsEncoded;
    }

    private final void checkOpen() throws java.io.IOException {
        if (this.isClosed) {
            throw new java.io.IOException("The output stream is closed.");
        }
    }
}
