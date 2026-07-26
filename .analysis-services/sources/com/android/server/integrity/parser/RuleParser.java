package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public interface RuleParser {
    java.util.List<android.content.integrity.Rule> parse(com.android.server.integrity.parser.RandomAccessObject randomAccessObject, java.util.List<com.android.server.integrity.parser.RuleIndexRange> list) throws com.android.server.integrity.parser.RuleParseException;

    java.util.List<android.content.integrity.Rule> parse(byte[] bArr) throws com.android.server.integrity.parser.RuleParseException;
}
