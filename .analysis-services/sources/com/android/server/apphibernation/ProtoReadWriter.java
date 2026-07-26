package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
interface ProtoReadWriter<T> {
    T readFromProto(android.util.proto.ProtoInputStream protoInputStream) throws java.io.IOException;

    void writeToProto(android.util.proto.ProtoOutputStream protoOutputStream, T t);
}
