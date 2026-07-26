package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class FastPrintWriter extends java.io.PrintWriter {
    private final boolean mAutoFlush;
    private final int mBufferLen;
    private final java.nio.ByteBuffer mBytes;
    private java.nio.charset.CharsetEncoder mCharset;
    private boolean mIoError;
    private final java.io.OutputStream mOutputStream;
    private int mPos;
    private final android.util.Printer mPrinter;
    private final java.lang.String mSeparator;
    private final char[] mText;
    private final java.io.Writer mWriter;

    private static class DummyWriter extends java.io.Writer {
        private DummyWriter() {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws java.io.IOException {
            java.lang.UnsupportedOperationException ex = new java.lang.UnsupportedOperationException("Shouldn't be here");
            throw ex;
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() throws java.io.IOException {
            close();
        }

        @Override // java.io.Writer
        public void write(char[] buf, int offset, int count) throws java.io.IOException {
            close();
        }
    }

    public FastPrintWriter(java.io.OutputStream out) {
        this(out, false, 8192);
    }

    public FastPrintWriter(java.io.OutputStream out, boolean autoFlush) {
        this(out, autoFlush, 8192);
    }

    public FastPrintWriter(java.io.OutputStream out, boolean autoFlush, int bufferLen) {
        super(new com.android.internal.util.jobs.FastPrintWriter.DummyWriter(), autoFlush);
        if (out == null) {
            throw new java.lang.NullPointerException("out is null");
        }
        this.mBufferLen = bufferLen;
        this.mText = new char[bufferLen];
        this.mBytes = java.nio.ByteBuffer.allocate(this.mBufferLen);
        this.mOutputStream = out;
        this.mWriter = null;
        this.mPrinter = null;
        this.mAutoFlush = autoFlush;
        this.mSeparator = java.lang.System.lineSeparator();
        initDefaultEncoder();
    }

    public FastPrintWriter(java.io.Writer wr) {
        this(wr, false, 8192);
    }

    public FastPrintWriter(java.io.Writer wr, boolean autoFlush) {
        this(wr, autoFlush, 8192);
    }

    public FastPrintWriter(java.io.Writer wr, boolean autoFlush, int bufferLen) {
        super(new com.android.internal.util.jobs.FastPrintWriter.DummyWriter(), autoFlush);
        if (wr == null) {
            throw new java.lang.NullPointerException("wr is null");
        }
        this.mBufferLen = bufferLen;
        this.mText = new char[bufferLen];
        this.mBytes = null;
        this.mOutputStream = null;
        this.mWriter = wr;
        this.mPrinter = null;
        this.mAutoFlush = autoFlush;
        this.mSeparator = java.lang.System.lineSeparator();
        initDefaultEncoder();
    }

    public FastPrintWriter(android.util.Printer pr) {
        this(pr, 512);
    }

    public FastPrintWriter(android.util.Printer pr, int bufferLen) {
        super((java.io.Writer) new com.android.internal.util.jobs.FastPrintWriter.DummyWriter(), true);
        if (pr == null) {
            throw new java.lang.NullPointerException("pr is null");
        }
        this.mBufferLen = bufferLen;
        this.mText = new char[bufferLen];
        this.mBytes = null;
        this.mOutputStream = null;
        this.mWriter = null;
        this.mPrinter = pr;
        this.mAutoFlush = true;
        this.mSeparator = java.lang.System.lineSeparator();
        initDefaultEncoder();
    }

    private final void initEncoder(java.lang.String csn) throws java.io.UnsupportedEncodingException {
        try {
            this.mCharset = java.nio.charset.Charset.forName(csn).newEncoder();
            this.mCharset.onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE);
            this.mCharset.onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE);
        } catch (java.lang.Exception e) {
            throw new java.io.UnsupportedEncodingException(csn);
        }
    }

    @Override // java.io.PrintWriter
    public boolean checkError() {
        boolean z;
        flush();
        synchronized (this.lock) {
            z = this.mIoError;
        }
        return z;
    }

    @Override // java.io.PrintWriter
    protected void clearError() {
        synchronized (this.lock) {
            this.mIoError = false;
        }
    }

    @Override // java.io.PrintWriter
    protected void setError() {
        synchronized (this.lock) {
            this.mIoError = true;
        }
    }

    private final void initDefaultEncoder() {
        this.mCharset = java.nio.charset.Charset.defaultCharset().newEncoder();
        this.mCharset.onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE);
        this.mCharset.onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE);
    }

    private void appendLocked(char c) throws java.io.IOException {
        int pos = this.mPos;
        if (pos >= this.mBufferLen - 1) {
            flushLocked();
            pos = this.mPos;
        }
        this.mText[pos] = c;
        this.mPos = pos + 1;
    }

    private void appendLocked(java.lang.String str, int i, int length) throws java.io.IOException {
        int BUFFER_LEN = this.mBufferLen;
        if (length > BUFFER_LEN) {
            int end = i + length;
            while (i < end) {
                int next = i + BUFFER_LEN;
                appendLocked(str, i, next < end ? BUFFER_LEN : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > BUFFER_LEN) {
            flushLocked();
            pos = this.mPos;
        }
        str.getChars(i, i + length, this.mText, pos);
        this.mPos = pos + length;
    }

    private void appendLocked(char[] buf, int i, int length) throws java.io.IOException {
        int BUFFER_LEN = this.mBufferLen;
        if (length > BUFFER_LEN) {
            int end = i + length;
            while (i < end) {
                int next = i + BUFFER_LEN;
                appendLocked(buf, i, next < end ? BUFFER_LEN : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > BUFFER_LEN) {
            flushLocked();
            pos = this.mPos;
        }
        java.lang.System.arraycopy(buf, i, this.mText, pos, length);
        this.mPos = pos + length;
    }

    private void flushBytesLocked() throws java.io.IOException {
        int position;
        if (!this.mIoError && (position = this.mBytes.position()) > 0) {
            this.mBytes.flip();
            this.mOutputStream.write(this.mBytes.array(), 0, position);
            this.mBytes.clear();
        }
    }

    private void flushLocked() throws java.io.IOException {
        if (this.mPos > 0) {
            if (this.mOutputStream != null) {
                java.nio.CharBuffer charBuffer = java.nio.CharBuffer.wrap(this.mText, 0, this.mPos);
                java.nio.charset.CoderResult result = this.mCharset.encode(charBuffer, this.mBytes, true);
                while (!this.mIoError) {
                    if (result.isError()) {
                        throw new java.io.IOException(result.toString());
                    }
                    if (!result.isOverflow()) {
                        break;
                    }
                    flushBytesLocked();
                    result = this.mCharset.encode(charBuffer, this.mBytes, true);
                }
                if (!this.mIoError) {
                    flushBytesLocked();
                    this.mOutputStream.flush();
                }
            } else if (this.mWriter != null) {
                if (!this.mIoError) {
                    this.mWriter.write(this.mText, 0, this.mPos);
                    this.mWriter.flush();
                }
            } else {
                int nonEolOff = 0;
                int sepLen = this.mSeparator.length();
                int len = sepLen < this.mPos ? sepLen : this.mPos;
                while (nonEolOff < len && this.mText[(this.mPos - 1) - nonEolOff] == this.mSeparator.charAt((this.mSeparator.length() - 1) - nonEolOff)) {
                    nonEolOff++;
                }
                if (nonEolOff >= this.mPos) {
                    this.mPrinter.println("");
                } else {
                    this.mPrinter.println(new java.lang.String(this.mText, 0, this.mPos - nonEolOff));
                }
            }
            this.mPos = 0;
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer, java.io.Flushable
    public void flush() {
        synchronized (this.lock) {
            try {
                flushLocked();
                if (!this.mIoError) {
                    if (this.mOutputStream != null) {
                        this.mOutputStream.flush();
                    } else if (this.mWriter != null) {
                        this.mWriter.flush();
                    }
                }
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this.lock) {
            try {
                flushLocked();
                if (this.mOutputStream != null) {
                    this.mOutputStream.close();
                } else if (this.mWriter != null) {
                    this.mWriter.close();
                }
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter
    public void print(char[] charArray) {
        synchronized (this.lock) {
            try {
                appendLocked(charArray, 0, charArray.length);
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter
    public void print(char ch) {
        synchronized (this.lock) {
            try {
                appendLocked(ch);
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter
    public void print(java.lang.String str) {
        if (str == null) {
            str = java.lang.String.valueOf((java.lang.Object) null);
        }
        synchronized (this.lock) {
            try {
                appendLocked(str, 0, str.length());
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter
    public void print(int inum) {
        if (inum == 0) {
            print("0");
        } else {
            super.print(inum);
        }
    }

    @Override // java.io.PrintWriter
    public void print(long lnum) {
        if (lnum == 0) {
            print("0");
        } else {
            super.print(lnum);
        }
    }

    @Override // java.io.PrintWriter
    public void println() {
        synchronized (this.lock) {
            try {
                appendLocked(this.mSeparator, 0, this.mSeparator.length());
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
            if (this.mAutoFlush) {
                flushLocked();
            }
        }
    }

    @Override // java.io.PrintWriter
    public void println(int inum) {
        if (inum == 0) {
            println("0");
        } else {
            super.println(inum);
        }
    }

    @Override // java.io.PrintWriter
    public void println(long lnum) {
        if (lnum == 0) {
            println("0");
        } else {
            super.println(lnum);
        }
    }

    @Override // java.io.PrintWriter
    public void println(char[] chars) {
        print(chars);
        println();
    }

    @Override // java.io.PrintWriter
    public void println(char c) {
        print(c);
        println();
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(char[] buf, int offset, int count) {
        synchronized (this.lock) {
            try {
                appendLocked(buf, offset, count);
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(int oneChar) {
        synchronized (this.lock) {
            try {
                appendLocked((char) oneChar);
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(java.lang.String str) {
        synchronized (this.lock) {
            try {
                appendLocked(str, 0, str.length());
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer
    public void write(java.lang.String str, int offset, int count) {
        synchronized (this.lock) {
            try {
                appendLocked(str, offset, count);
            } catch (java.io.IOException e) {
                android.util.Log.w("FastPrintWriter", "Write failure", e);
                setError();
            }
        }
    }

    @Override // java.io.PrintWriter, java.io.Writer, java.lang.Appendable
    public java.io.PrintWriter append(java.lang.CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        java.lang.String output = csq.subSequence(start, end).toString();
        write(output, 0, output.length());
        return this;
    }
}
