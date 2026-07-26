package com.android.server.integrity.serializer;

/* JADX INFO: loaded from: classes2.dex */
public interface RuleSerializer {
    void serialize(java.util.List<android.content.integrity.Rule> list, java.util.Optional<java.lang.Integer> optional, java.io.OutputStream outputStream, java.io.OutputStream outputStream2) throws com.android.server.integrity.serializer.RuleSerializeException;

    byte[] serialize(java.util.List<android.content.integrity.Rule> list, java.util.Optional<java.lang.Integer> optional) throws com.android.server.integrity.serializer.RuleSerializeException;
}
