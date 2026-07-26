package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: HexFormat.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u0000 \u00132\u00020\u0001:\u0004\u0011\u0012\u0013\u0014B\u001f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0015"}, d2 = {"Lkotlin/text/HexFormat;", "", "upperCase", "", "bytes", "Lkotlin/text/HexFormat$BytesHexFormat;", "number", "Lkotlin/text/HexFormat$NumberHexFormat;", "(ZLkotlin/text/HexFormat$BytesHexFormat;Lkotlin/text/HexFormat$NumberHexFormat;)V", "getBytes", "()Lkotlin/text/HexFormat$BytesHexFormat;", "getNumber", "()Lkotlin/text/HexFormat$NumberHexFormat;", "getUpperCase", "()Z", "toString", "", "Builder", "BytesHexFormat", "Companion", "NumberHexFormat", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class HexFormat {
    public static final com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion Companion = new com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion(null);
    private static final com.android.server.permission.jarjar.kotlin.text.HexFormat Default = new com.android.server.permission.jarjar.kotlin.text.HexFormat(false, com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib(), com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion.getDefault$kotlin_stdlib());
    private static final com.android.server.permission.jarjar.kotlin.text.HexFormat UpperCase = new com.android.server.permission.jarjar.kotlin.text.HexFormat(true, com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib(), com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion.getDefault$kotlin_stdlib());
    private final com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat bytes;
    private final com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat number;
    private final boolean upperCase;

    public HexFormat(boolean upperCase, com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat bytes, com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat number) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(bytes, "bytes");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(number, "number");
        this.upperCase = upperCase;
        this.bytes = bytes;
        this.number = number;
    }

    public final boolean getUpperCase() {
        return this.upperCase;
    }

    public final com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat getBytes() {
        return this.bytes;
    }

    public final com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat getNumber() {
        return this.number;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder $this$toString_u24lambda_u240 = new java.lang.StringBuilder();
        java.lang.StringBuilder sbAppend = $this$toString_u24lambda_u240.append("HexFormat(");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend.append('\n'), "append(...)");
        java.lang.StringBuilder sbAppend2 = $this$toString_u24lambda_u240.append("    upperCase = ").append(this.upperCase);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
        java.lang.StringBuilder sbAppend3 = sbAppend2.append(",");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend3, "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend3.append('\n'), "append(...)");
        java.lang.StringBuilder sbAppend4 = $this$toString_u24lambda_u240.append("    bytes = BytesHexFormat(");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend4, "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend4.append('\n'), "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(this.bytes.appendOptionsTo$kotlin_stdlib($this$toString_u24lambda_u240, "        ").append('\n'), "append(...)");
        java.lang.StringBuilder sbAppend5 = $this$toString_u24lambda_u240.append("    ),");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend5, "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend5.append('\n'), "append(...)");
        java.lang.StringBuilder sbAppend6 = $this$toString_u24lambda_u240.append("    number = NumberHexFormat(");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend6, "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend6.append('\n'), "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(this.number.appendOptionsTo$kotlin_stdlib($this$toString_u24lambda_u240, "        ").append('\n'), "append(...)");
        java.lang.StringBuilder sbAppend7 = $this$toString_u24lambda_u240.append("    )");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend7, "append(...)");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend7.append('\n'), "append(...)");
        $this$toString_u24lambda_u240.append(")");
        java.lang.String string = $this$toString_u24lambda_u240.toString();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    /* JADX INFO: compiled from: HexFormat.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u001b2\u00020\u0001:\u0002\u001a\u001bB7\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0006¢\u0006\u0002\u0010\nJ%\u0010\u0013\u001a\u00060\u0014j\u0002`\u00152\n\u0010\u0016\u001a\u00060\u0014j\u0002`\u00152\u0006\u0010\u0017\u001a\u00020\u0006H\u0000¢\u0006\u0002\b\u0018J\b\u0010\u0019\u001a\u00020\u0006H\u0016R\u0011\u0010\b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\t\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f¨\u0006\u001c"}, d2 = {"Lkotlin/text/HexFormat$BytesHexFormat;", "", "bytesPerLine", "", "bytesPerGroup", "groupSeparator", "", "byteSeparator", "bytePrefix", "byteSuffix", "(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getBytePrefix", "()Ljava/lang/String;", "getByteSeparator", "getByteSuffix", "getBytesPerGroup", "()I", "getBytesPerLine", "getGroupSeparator", "appendOptionsTo", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "sb", "indent", "appendOptionsTo$kotlin_stdlib", "toString", "Builder", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class BytesHexFormat {
        public static final com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion Companion = new com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion(null);
        private static final com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat Default = new com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat(Integer.MAX_VALUE, Integer.MAX_VALUE, "  ", "", "", "");
        private final java.lang.String bytePrefix;
        private final java.lang.String byteSeparator;
        private final java.lang.String byteSuffix;
        private final int bytesPerGroup;
        private final int bytesPerLine;
        private final java.lang.String groupSeparator;

        public BytesHexFormat(int bytesPerLine, int bytesPerGroup, java.lang.String groupSeparator, java.lang.String byteSeparator, java.lang.String bytePrefix, java.lang.String byteSuffix) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(groupSeparator, "groupSeparator");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(byteSeparator, "byteSeparator");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(bytePrefix, "bytePrefix");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(byteSuffix, "byteSuffix");
            this.bytesPerLine = bytesPerLine;
            this.bytesPerGroup = bytesPerGroup;
            this.groupSeparator = groupSeparator;
            this.byteSeparator = byteSeparator;
            this.bytePrefix = bytePrefix;
            this.byteSuffix = byteSuffix;
        }

        public final int getBytesPerLine() {
            return this.bytesPerLine;
        }

        public final int getBytesPerGroup() {
            return this.bytesPerGroup;
        }

        public final java.lang.String getGroupSeparator() {
            return this.groupSeparator;
        }

        public final java.lang.String getByteSeparator() {
            return this.byteSeparator;
        }

        public final java.lang.String getBytePrefix() {
            return this.bytePrefix;
        }

        public final java.lang.String getByteSuffix() {
            return this.byteSuffix;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder $this$toString_u24lambda_u240 = new java.lang.StringBuilder();
            java.lang.StringBuilder sbAppend = $this$toString_u24lambda_u240.append("BytesHexFormat(");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend.append('\n'), "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendOptionsTo$kotlin_stdlib($this$toString_u24lambda_u240, "    ").append('\n'), "append(...)");
            $this$toString_u24lambda_u240.append(")");
            java.lang.String string = $this$toString_u24lambda_u240.toString();
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return string;
        }

        public final java.lang.StringBuilder appendOptionsTo$kotlin_stdlib(java.lang.StringBuilder sb, java.lang.String indent) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sb, "sb");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(indent, "indent");
            java.lang.StringBuilder sbAppend = sb.append(indent).append("bytesPerLine = ").append(this.bytesPerLine);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
            java.lang.StringBuilder sbAppend2 = sbAppend.append(",");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2.append('\n'), "append(...)");
            java.lang.StringBuilder sbAppend3 = sb.append(indent).append("bytesPerGroup = ").append(this.bytesPerGroup);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend3, "append(...)");
            java.lang.StringBuilder sbAppend4 = sbAppend3.append(",");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend4, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend4.append('\n'), "append(...)");
            java.lang.StringBuilder sbAppend5 = sb.append(indent).append("groupSeparator = \"").append(this.groupSeparator);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend5, "append(...)");
            java.lang.StringBuilder sbAppend6 = sbAppend5.append("\",");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend6, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend6.append('\n'), "append(...)");
            java.lang.StringBuilder sbAppend7 = sb.append(indent).append("byteSeparator = \"").append(this.byteSeparator);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend7, "append(...)");
            java.lang.StringBuilder sbAppend8 = sbAppend7.append("\",");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend8, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend8.append('\n'), "append(...)");
            java.lang.StringBuilder sbAppend9 = sb.append(indent).append("bytePrefix = \"").append(this.bytePrefix);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend9, "append(...)");
            java.lang.StringBuilder sbAppend10 = sbAppend9.append("\",");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend10, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend10.append('\n'), "append(...)");
            sb.append(indent).append("byteSuffix = \"").append(this.byteSuffix).append("\"");
            return sb;
        }

        /* JADX INFO: compiled from: HexFormat.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\r\u0010\u001c\u001a\u00020\u001dH\u0000¢\u0006\u0002\b\u001eR$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR$\u0010\n\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\tR$\u0010\r\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u0007\"\u0004\b\u000f\u0010\tR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0010@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R$\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0010@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0015R\u001a\u0010\u0019\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0007\"\u0004\b\u001b\u0010\t¨\u0006\u001f"}, d2 = {"Lkotlin/text/HexFormat$BytesHexFormat$Builder;", "", "()V", "value", "", "bytePrefix", "getBytePrefix", "()Ljava/lang/String;", "setBytePrefix", "(Ljava/lang/String;)V", "byteSeparator", "getByteSeparator", "setByteSeparator", "byteSuffix", "getByteSuffix", "setByteSuffix", "", "bytesPerGroup", "getBytesPerGroup", "()I", "setBytesPerGroup", "(I)V", "bytesPerLine", "getBytesPerLine", "setBytesPerLine", "groupSeparator", "getGroupSeparator", "setGroupSeparator", "build", "Lkotlin/text/HexFormat$BytesHexFormat;", "build$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final class Builder {
            private int bytesPerLine = com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib().getBytesPerLine();
            private int bytesPerGroup = com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib().getBytesPerGroup();
            private java.lang.String groupSeparator = com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib().getGroupSeparator();
            private java.lang.String byteSeparator = com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib().getByteSeparator();
            private java.lang.String bytePrefix = com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib().getBytePrefix();
            private java.lang.String byteSuffix = com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib().getByteSuffix();

            public final int getBytesPerLine() {
                return this.bytesPerLine;
            }

            public final void setBytesPerLine(int value) {
                if (value <= 0) {
                    throw new java.lang.IllegalArgumentException("Non-positive values are prohibited for bytesPerLine, but was " + value);
                }
                this.bytesPerLine = value;
            }

            public final int getBytesPerGroup() {
                return this.bytesPerGroup;
            }

            public final void setBytesPerGroup(int value) {
                if (value <= 0) {
                    throw new java.lang.IllegalArgumentException("Non-positive values are prohibited for bytesPerGroup, but was " + value);
                }
                this.bytesPerGroup = value;
            }

            public final java.lang.String getGroupSeparator() {
                return this.groupSeparator;
            }

            public final void setGroupSeparator(java.lang.String str) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "<set-?>");
                this.groupSeparator = str;
            }

            public final java.lang.String getByteSeparator() {
                return this.byteSeparator;
            }

            public final void setByteSeparator(java.lang.String value) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
                if (com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\n', false, 2, (java.lang.Object) null) || com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\r', false, 2, (java.lang.Object) null)) {
                    throw new java.lang.IllegalArgumentException("LF and CR characters are prohibited in byteSeparator, but was " + value);
                }
                this.byteSeparator = value;
            }

            public final java.lang.String getBytePrefix() {
                return this.bytePrefix;
            }

            public final void setBytePrefix(java.lang.String value) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
                if (com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\n', false, 2, (java.lang.Object) null) || com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\r', false, 2, (java.lang.Object) null)) {
                    throw new java.lang.IllegalArgumentException("LF and CR characters are prohibited in bytePrefix, but was " + value);
                }
                this.bytePrefix = value;
            }

            public final java.lang.String getByteSuffix() {
                return this.byteSuffix;
            }

            public final void setByteSuffix(java.lang.String value) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
                if (com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\n', false, 2, (java.lang.Object) null) || com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\r', false, 2, (java.lang.Object) null)) {
                    throw new java.lang.IllegalArgumentException("LF and CR characters are prohibited in byteSuffix, but was " + value);
                }
                this.byteSuffix = value;
            }

            public final com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat build$kotlin_stdlib() {
                return new com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat(this.bytesPerLine, this.bytesPerGroup, this.groupSeparator, this.byteSeparator, this.bytePrefix, this.byteSuffix);
            }
        }

        /* JADX INFO: compiled from: HexFormat.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/text/HexFormat$BytesHexFormat$Companion;", "", "()V", "Default", "Lkotlin/text/HexFormat$BytesHexFormat;", "getDefault$kotlin_stdlib", "()Lkotlin/text/HexFormat$BytesHexFormat;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final class Companion {
            public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat getDefault$kotlin_stdlib() {
                return com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Default;
            }
        }
    }

    /* JADX INFO: compiled from: HexFormat.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u00152\u00020\u0001:\u0002\u0014\u0015B\u001f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J%\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\n\u0010\u0010\u001a\u00060\u000ej\u0002`\u000f2\u0006\u0010\u0011\u001a\u00020\u0003H\u0000¢\u0006\u0002\b\u0012J\b\u0010\u0013\u001a\u00020\u0003H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0016"}, d2 = {"Lkotlin/text/HexFormat$NumberHexFormat;", "", "prefix", "", "suffix", "removeLeadingZeros", "", "(Ljava/lang/String;Ljava/lang/String;Z)V", "getPrefix", "()Ljava/lang/String;", "getRemoveLeadingZeros", "()Z", "getSuffix", "appendOptionsTo", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "sb", "indent", "appendOptionsTo$kotlin_stdlib", "toString", "Builder", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class NumberHexFormat {
        public static final com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion Companion = new com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion(null);
        private static final com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat Default = new com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat("", "", false);
        private final java.lang.String prefix;
        private final boolean removeLeadingZeros;
        private final java.lang.String suffix;

        public NumberHexFormat(java.lang.String prefix, java.lang.String suffix, boolean removeLeadingZeros) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(suffix, "suffix");
            this.prefix = prefix;
            this.suffix = suffix;
            this.removeLeadingZeros = removeLeadingZeros;
        }

        public final java.lang.String getPrefix() {
            return this.prefix;
        }

        public final java.lang.String getSuffix() {
            return this.suffix;
        }

        public final boolean getRemoveLeadingZeros() {
            return this.removeLeadingZeros;
        }

        public java.lang.String toString() {
            java.lang.StringBuilder $this$toString_u24lambda_u240 = new java.lang.StringBuilder();
            java.lang.StringBuilder sbAppend = $this$toString_u24lambda_u240.append("NumberHexFormat(");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend.append('\n'), "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(appendOptionsTo$kotlin_stdlib($this$toString_u24lambda_u240, "    ").append('\n'), "append(...)");
            $this$toString_u24lambda_u240.append(")");
            java.lang.String string = $this$toString_u24lambda_u240.toString();
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return string;
        }

        public final java.lang.StringBuilder appendOptionsTo$kotlin_stdlib(java.lang.StringBuilder sb, java.lang.String indent) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sb, "sb");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(indent, "indent");
            java.lang.StringBuilder sbAppend = sb.append(indent).append("prefix = \"").append(this.prefix);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend, "append(...)");
            java.lang.StringBuilder sbAppend2 = sbAppend.append("\",");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend2.append('\n'), "append(...)");
            java.lang.StringBuilder sbAppend3 = sb.append(indent).append("suffix = \"").append(this.suffix);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend3, "append(...)");
            java.lang.StringBuilder sbAppend4 = sbAppend3.append("\",");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend4, "append(...)");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sbAppend4.append('\n'), "append(...)");
            sb.append(indent).append("removeLeadingZeros = ").append(this.removeLeadingZeros);
            return sb;
        }

        /* JADX INFO: compiled from: HexFormat.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\r\u0010\u0013\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\u0015R$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR$\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0007\"\u0004\b\u0012\u0010\t¨\u0006\u0016"}, d2 = {"Lkotlin/text/HexFormat$NumberHexFormat$Builder;", "", "()V", "value", "", "prefix", "getPrefix", "()Ljava/lang/String;", "setPrefix", "(Ljava/lang/String;)V", "removeLeadingZeros", "", "getRemoveLeadingZeros", "()Z", "setRemoveLeadingZeros", "(Z)V", "suffix", "getSuffix", "setSuffix", "build", "Lkotlin/text/HexFormat$NumberHexFormat;", "build$kotlin_stdlib", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final class Builder {
            private java.lang.String prefix = com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion.getDefault$kotlin_stdlib().getPrefix();
            private java.lang.String suffix = com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion.getDefault$kotlin_stdlib().getSuffix();
            private boolean removeLeadingZeros = com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion.getDefault$kotlin_stdlib().getRemoveLeadingZeros();

            public final java.lang.String getPrefix() {
                return this.prefix;
            }

            public final void setPrefix(java.lang.String value) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
                if (com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\n', false, 2, (java.lang.Object) null) || com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\r', false, 2, (java.lang.Object) null)) {
                    throw new java.lang.IllegalArgumentException("LF and CR characters are prohibited in prefix, but was " + value);
                }
                this.prefix = value;
            }

            public final java.lang.String getSuffix() {
                return this.suffix;
            }

            public final void setSuffix(java.lang.String value) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
                if (com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\n', false, 2, (java.lang.Object) null) || com.android.server.permission.jarjar.kotlin.text.StringsKt.contains$default((java.lang.CharSequence) value, '\r', false, 2, (java.lang.Object) null)) {
                    throw new java.lang.IllegalArgumentException("LF and CR characters are prohibited in suffix, but was " + value);
                }
                this.suffix = value;
            }

            public final boolean getRemoveLeadingZeros() {
                return this.removeLeadingZeros;
            }

            public final void setRemoveLeadingZeros(boolean z) {
                this.removeLeadingZeros = z;
            }

            public final com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat build$kotlin_stdlib() {
                return new com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat(this.prefix, this.suffix, this.removeLeadingZeros);
            }
        }

        /* JADX INFO: compiled from: HexFormat.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/text/HexFormat$NumberHexFormat$Companion;", "", "()V", "Default", "Lkotlin/text/HexFormat$NumberHexFormat;", "getDefault$kotlin_stdlib", "()Lkotlin/text/HexFormat$NumberHexFormat;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final class Companion {
            public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }

            public final com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat getDefault$kotlin_stdlib() {
                return com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Default;
            }
        }
    }

    /* JADX INFO: compiled from: HexFormat.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007\b\u0001¢\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0001J%\u0010\u0007\u001a\u00020\u00152\u0017\u0010\u0016\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150\u0017¢\u0006\u0002\b\u0018H\u0087\bø\u0001\u0000J%\u0010\n\u001a\u00020\u00152\u0017\u0010\u0016\u001a\u0013\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00150\u0017¢\u0006\u0002\b\u0018H\u0087\bø\u0001\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0019"}, d2 = {"Lkotlin/text/HexFormat$Builder;", "", "()V", "_bytes", "Lkotlin/text/HexFormat$BytesHexFormat$Builder;", "_number", "Lkotlin/text/HexFormat$NumberHexFormat$Builder;", "bytes", "getBytes", "()Lkotlin/text/HexFormat$BytesHexFormat$Builder;", "number", "getNumber", "()Lkotlin/text/HexFormat$NumberHexFormat$Builder;", "upperCase", "", "getUpperCase", "()Z", "setUpperCase", "(Z)V", "build", "Lkotlin/text/HexFormat;", "", "builderAction", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Builder {
        private com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Builder _bytes;
        private com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Builder _number;
        private boolean upperCase = com.android.server.permission.jarjar.kotlin.text.HexFormat.Companion.getDefault().getUpperCase();

        public final boolean getUpperCase() {
            return this.upperCase;
        }

        public final void setUpperCase(boolean z) {
            this.upperCase = z;
        }

        public final com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Builder getBytes() {
            if (this._bytes == null) {
                this._bytes = new com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Builder();
            }
            com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Builder builder = this._bytes;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(builder);
            return builder;
        }

        public final com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Builder getNumber() {
            if (this._number == null) {
                this._number = new com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Builder();
            }
            com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Builder builder = this._number;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(builder);
            return builder;
        }

        private final void bytes(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Builder, com.android.server.permission.jarjar.kotlin.Unit> function1) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "builderAction");
            function1.invoke(getBytes());
        }

        private final void number(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Builder, com.android.server.permission.jarjar.kotlin.Unit> function1) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "builderAction");
            function1.invoke(getNumber());
        }

        public final com.android.server.permission.jarjar.kotlin.text.HexFormat build() {
            com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat default$kotlin_stdlib;
            com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat default$kotlin_stdlib2;
            boolean z = this.upperCase;
            com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Builder builder = this._bytes;
            if (builder == null || (default$kotlin_stdlib = builder.build$kotlin_stdlib()) == null) {
                default$kotlin_stdlib = com.android.server.permission.jarjar.kotlin.text.HexFormat.BytesHexFormat.Companion.getDefault$kotlin_stdlib();
            }
            com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Builder builder2 = this._number;
            if (builder2 == null || (default$kotlin_stdlib2 = builder2.build$kotlin_stdlib()) == null) {
                default$kotlin_stdlib2 = com.android.server.permission.jarjar.kotlin.text.HexFormat.NumberHexFormat.Companion.getDefault$kotlin_stdlib();
            }
            return new com.android.server.permission.jarjar.kotlin.text.HexFormat(z, default$kotlin_stdlib, default$kotlin_stdlib2);
        }
    }

    /* JADX INFO: compiled from: HexFormat.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006¨\u0006\t"}, d2 = {"Lkotlin/text/HexFormat$Companion;", "", "()V", "Default", "Lkotlin/text/HexFormat;", "getDefault", "()Lkotlin/text/HexFormat;", "UpperCase", "getUpperCase", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final com.android.server.permission.jarjar.kotlin.text.HexFormat getDefault() {
            return com.android.server.permission.jarjar.kotlin.text.HexFormat.Default;
        }

        public final com.android.server.permission.jarjar.kotlin.text.HexFormat getUpperCase() {
            return com.android.server.permission.jarjar.kotlin.text.HexFormat.UpperCase;
        }
    }
}
