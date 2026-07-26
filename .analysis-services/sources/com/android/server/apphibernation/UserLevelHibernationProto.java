package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
final class UserLevelHibernationProto implements com.android.server.apphibernation.ProtoReadWriter<java.util.List<com.android.server.apphibernation.UserLevelState>> {
    private static final java.lang.String TAG = "UserLevelHibernationProtoReadWriter";

    UserLevelHibernationProto() {
    }

    @Override // com.android.server.apphibernation.ProtoReadWriter
    public void writeToProto(android.util.proto.ProtoOutputStream stream, java.util.List<com.android.server.apphibernation.UserLevelState> data) {
        int size = data.size();
        for (int i = 0; i < size; i++) {
            long token = stream.start(2246267895809L);
            com.android.server.apphibernation.UserLevelState state = data.get(i);
            stream.write(1138166333441L, state.packageName);
            stream.write(1133871366146L, state.hibernated);
            stream.end(token);
        }
    }

    @Override // com.android.server.apphibernation.ProtoReadWriter
    public java.util.List<com.android.server.apphibernation.UserLevelState> readFromProto(android.util.proto.ProtoInputStream stream) throws java.io.IOException {
        java.util.List<com.android.server.apphibernation.UserLevelState> list = new java.util.ArrayList<>();
        while (stream.nextField() != -1) {
            if (stream.getFieldNumber() == 1) {
                com.android.server.apphibernation.UserLevelState state = new com.android.server.apphibernation.UserLevelState();
                long token = stream.start(2246267895809L);
                while (stream.nextField() != -1) {
                    switch (stream.getFieldNumber()) {
                        case 1:
                            state.packageName = stream.readString(1138166333441L);
                            break;
                        case 2:
                            state.hibernated = stream.readBoolean(1133871366146L);
                            break;
                        default:
                            android.util.Slog.w(TAG, "Undefined field in proto: " + stream.getFieldNumber());
                            break;
                    }
                }
                stream.end(token);
                list.add(state);
            }
        }
        return list;
    }
}
