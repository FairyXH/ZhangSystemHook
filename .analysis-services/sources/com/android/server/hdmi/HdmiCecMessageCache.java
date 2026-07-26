package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiCecMessageCache {
    private static final android.util.FastImmutableArraySet<java.lang.Integer> CACHEABLE_OPCODES = new android.util.FastImmutableArraySet<>(new java.lang.Integer[]{71, 132, 135, 158});
    private final android.util.SparseArray<android.util.SparseArray<com.android.server.hdmi.HdmiCecMessage>> mCache = new android.util.SparseArray<>();

    HdmiCecMessageCache() {
    }

    public com.android.server.hdmi.HdmiCecMessage getMessage(int address, int opcode) {
        android.util.SparseArray<com.android.server.hdmi.HdmiCecMessage> messages = this.mCache.get(address);
        if (messages == null) {
            return null;
        }
        return messages.get(opcode);
    }

    public void flushMessagesFrom(int address) {
        this.mCache.remove(address);
    }

    public void flushAll() {
        this.mCache.clear();
    }

    public void cacheMessage(com.android.server.hdmi.HdmiCecMessage message) {
        int opcode = message.getOpcode();
        if (!isCacheable(opcode)) {
            return;
        }
        int source = message.getSource();
        android.util.SparseArray<com.android.server.hdmi.HdmiCecMessage> messages = this.mCache.get(source);
        if (messages == null) {
            messages = new android.util.SparseArray<>();
            this.mCache.put(source, messages);
        }
        messages.put(opcode, message);
    }

    private boolean isCacheable(int opcode) {
        return CACHEABLE_OPCODES.contains(java.lang.Integer.valueOf(opcode));
    }
}
