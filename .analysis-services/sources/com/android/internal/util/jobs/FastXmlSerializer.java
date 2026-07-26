package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class FastXmlSerializer implements org.xmlpull.v1.XmlSerializer {
    private static final int DEFAULT_BUFFER_LEN = 32768;
    private static final java.lang.String[] ESCAPE_TABLE = {"&#0;", "&#1;", "&#2;", "&#3;", "&#4;", "&#5;", "&#6;", "&#7;", "&#8;", "&#9;", "&#10;", "&#11;", "&#12;", "&#13;", "&#14;", "&#15;", "&#16;", "&#17;", "&#18;", "&#19;", "&#20;", "&#21;", "&#22;", "&#23;", "&#24;", "&#25;", "&#26;", "&#27;", "&#28;", "&#29;", "&#30;", "&#31;", null, null, "&quot;", null, null, null, "&amp;", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "&lt;", null, "&gt;", null};
    private static java.lang.String sSpace = "                                                              ";
    private final int mBufferLen;
    private java.nio.ByteBuffer mBytes;
    private java.nio.charset.CharsetEncoder mCharset;
    private boolean mInTag;
    private boolean mIndent;
    private boolean mLineStart;
    private int mNesting;
    private java.io.OutputStream mOutputStream;
    private int mPos;
    private final char[] mText;
    private java.io.Writer mWriter;

    public FastXmlSerializer() {
        this(32768);
    }

    public FastXmlSerializer(int bufferSize) {
        this.mIndent = false;
        this.mNesting = 0;
        this.mLineStart = true;
        this.mBufferLen = bufferSize > 0 ? bufferSize : 32768;
        this.mText = new char[this.mBufferLen];
        this.mBytes = java.nio.ByteBuffer.allocate(this.mBufferLen);
    }

    private void append(char c) throws java.io.IOException {
        int pos = this.mPos;
        if (pos >= this.mBufferLen - 1) {
            flush();
            pos = this.mPos;
        }
        this.mText[pos] = c;
        this.mPos = pos + 1;
    }

    private void append(java.lang.String str, int i, int length) throws java.io.IOException {
        if (length > this.mBufferLen) {
            int end = i + length;
            while (i < end) {
                int next = this.mBufferLen + i;
                append(str, i, next < end ? this.mBufferLen : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > this.mBufferLen) {
            flush();
            pos = this.mPos;
        }
        str.getChars(i, i + length, this.mText, pos);
        this.mPos = pos + length;
    }

    private void append(char[] buf, int i, int length) throws java.io.IOException {
        if (length > this.mBufferLen) {
            int end = i + length;
            while (i < end) {
                int next = this.mBufferLen + i;
                append(buf, i, next < end ? this.mBufferLen : end - i);
                i = next;
            }
            return;
        }
        int pos = this.mPos;
        if (pos + length > this.mBufferLen) {
            flush();
            pos = this.mPos;
        }
        java.lang.System.arraycopy(buf, i, this.mText, pos, length);
        this.mPos = pos + length;
    }

    private void append(java.lang.String str) throws java.io.IOException {
        append(str, 0, str.length());
    }

    private void appendIndent(int indent) throws java.io.IOException {
        int indent2 = indent * 4;
        if (indent2 > sSpace.length()) {
            indent2 = sSpace.length();
        }
        append(sSpace, 0, indent2);
    }

    private void escapeAndAppendString(java.lang.String string) throws java.io.IOException {
        java.lang.String escape;
        int N = string.length();
        char NE = (char) ESCAPE_TABLE.length;
        java.lang.String[] escapes = ESCAPE_TABLE;
        int lastPos = 0;
        int pos = 0;
        while (pos < N) {
            char c = string.charAt(pos);
            if (c < NE && (escape = escapes[c]) != null) {
                if (lastPos < pos) {
                    append(string, lastPos, pos - lastPos);
                }
                lastPos = pos + 1;
                append(escape);
            }
            pos++;
        }
        if (lastPos < pos) {
            append(string, lastPos, pos - lastPos);
        }
    }

    private void escapeAndAppendString(char[] buf, int start, int len) throws java.io.IOException {
        java.lang.String escape;
        char NE = (char) ESCAPE_TABLE.length;
        java.lang.String[] escapes = ESCAPE_TABLE;
        int end = start + len;
        int lastPos = start;
        int pos = start;
        while (pos < end) {
            char c = buf[pos];
            if (c < NE && (escape = escapes[c]) != null) {
                if (lastPos < pos) {
                    append(buf, lastPos, pos - lastPos);
                }
                lastPos = pos + 1;
                append(escape);
            }
            pos++;
        }
        if (lastPos < pos) {
            append(buf, lastPos, pos - lastPos);
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public org.xmlpull.v1.XmlSerializer attribute(java.lang.String namespace, java.lang.String name, java.lang.String value) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        append(' ');
        if (namespace != null) {
            append(namespace);
            append(':');
        }
        append(name);
        append("=\"");
        escapeAndAppendString(value);
        append('\"');
        this.mLineStart = false;
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void cdsect(java.lang.String text) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void comment(java.lang.String text) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void docdecl(java.lang.String text) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void endDocument() throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        flush();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public org.xmlpull.v1.XmlSerializer endTag(java.lang.String namespace, java.lang.String name) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        this.mNesting--;
        if (this.mInTag) {
            append(" />\n");
        } else {
            if (this.mIndent && this.mLineStart) {
                appendIndent(this.mNesting);
            }
            append("</");
            if (namespace != null) {
                append(namespace);
                append(':');
            }
            append(name);
            append(">\n");
        }
        this.mLineStart = true;
        this.mInTag = false;
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void entityRef(java.lang.String text) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    private void flushBytes() throws java.io.IOException {
        int position = this.mBytes.position();
        if (position > 0) {
            this.mBytes.flip();
            this.mOutputStream.write(this.mBytes.array(), 0, position);
            this.mBytes.clear();
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void flush() throws java.io.IOException {
        if (this.mPos > 0) {
            if (this.mOutputStream != null) {
                java.nio.CharBuffer charBuffer = java.nio.CharBuffer.wrap(this.mText, 0, this.mPos);
                java.nio.charset.CoderResult result = this.mCharset.encode(charBuffer, this.mBytes, true);
                while (!result.isError()) {
                    if (result.isOverflow()) {
                        flushBytes();
                        result = this.mCharset.encode(charBuffer, this.mBytes, true);
                    } else {
                        flushBytes();
                        this.mOutputStream.flush();
                    }
                }
                throw new java.io.IOException(result.toString());
            }
            this.mWriter.write(this.mText, 0, this.mPos);
            this.mWriter.flush();
            this.mPos = 0;
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public int getDepth() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public boolean getFeature(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public java.lang.String getName() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public java.lang.String getNamespace() {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public java.lang.String getPrefix(java.lang.String namespace, boolean generatePrefix) throws java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public java.lang.Object getProperty(java.lang.String name) {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void ignorableWhitespace(java.lang.String text) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void processingInstruction(java.lang.String text) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setFeature(java.lang.String name, boolean state) throws java.lang.IllegalStateException, java.lang.IllegalArgumentException {
        if (name.equals("http://xmlpull.org/v1/doc/features.html#indent-output")) {
            this.mIndent = true;
            return;
        }
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(java.io.OutputStream os, java.lang.String encoding) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        if (os == null) {
            throw new java.lang.IllegalArgumentException();
        }
        try {
            this.mCharset = java.nio.charset.Charset.forName(encoding).newEncoder().onMalformedInput(java.nio.charset.CodingErrorAction.REPLACE).onUnmappableCharacter(java.nio.charset.CodingErrorAction.REPLACE);
            this.mOutputStream = os;
        } catch (java.nio.charset.IllegalCharsetNameException e) {
            throw ((java.io.UnsupportedEncodingException) new java.io.UnsupportedEncodingException(encoding).initCause(e));
        } catch (java.nio.charset.UnsupportedCharsetException e2) {
            throw ((java.io.UnsupportedEncodingException) new java.io.UnsupportedEncodingException(encoding).initCause(e2));
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(java.io.Writer writer) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        this.mWriter = writer;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setPrefix(java.lang.String prefix, java.lang.String namespace) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setProperty(java.lang.String name, java.lang.Object value) throws java.lang.IllegalStateException, java.lang.IllegalArgumentException {
        throw new java.lang.UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void startDocument(java.lang.String encoding, java.lang.Boolean standalone) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        append("<?xml version='1.0' encoding='utf-8'");
        if (standalone != null) {
            append(" standalone='" + (standalone.booleanValue() ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO) + "'");
        }
        append(" ?>\n");
        this.mLineStart = true;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public org.xmlpull.v1.XmlSerializer startTag(java.lang.String namespace, java.lang.String name) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        if (this.mInTag) {
            append(">\n");
        }
        if (this.mIndent) {
            appendIndent(this.mNesting);
        }
        this.mNesting++;
        append('<');
        if (namespace != null) {
            append(namespace);
            append(':');
        }
        append(name);
        this.mInTag = true;
        this.mLineStart = false;
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public org.xmlpull.v1.XmlSerializer text(char[] buf, int start, int len) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        if (this.mInTag) {
            append(">");
            this.mInTag = false;
        }
        escapeAndAppendString(buf, start, len);
        if (this.mIndent) {
            this.mLineStart = buf[(start + len) - 1] == '\n';
        }
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public org.xmlpull.v1.XmlSerializer text(java.lang.String text) throws java.lang.IllegalStateException, java.io.IOException, java.lang.IllegalArgumentException {
        boolean z = false;
        if (this.mInTag) {
            append(">");
            this.mInTag = false;
        }
        escapeAndAppendString(text);
        if (this.mIndent) {
            if (text.length() > 0 && text.charAt(text.length() - 1) == '\n') {
                z = true;
            }
            this.mLineStart = z;
        }
        return this;
    }
}
