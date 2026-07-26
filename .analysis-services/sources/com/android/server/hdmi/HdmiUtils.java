package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
final class HdmiUtils {
    private static final java.util.Map<java.lang.Integer, java.util.List<java.lang.Integer>> ADDRESS_TO_TYPE = java.util.Map.ofEntries(java.util.Map.entry(0, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{0})), java.util.Map.entry(1, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{1})), java.util.Map.entry(2, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{1})), java.util.Map.entry(3, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{3})), java.util.Map.entry(4, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{4})), java.util.Map.entry(5, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{5})), java.util.Map.entry(6, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{3})), java.util.Map.entry(7, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{3})), java.util.Map.entry(8, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{4})), java.util.Map.entry(9, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{1})), java.util.Map.entry(10, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{3})), java.util.Map.entry(11, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{4})), java.util.Map.entry(12, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{4, 1, 3, 7})), java.util.Map.entry(13, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{4, 1, 3, 7})), java.util.Map.entry(14, com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{0})), java.util.Map.entry(15, java.util.Collections.emptyList()));
    private static final java.lang.String[] DEFAULT_NAMES = {"TV", "Recorder_1", "Recorder_2", "Tuner_1", "Playback_1", "AudioSystem", "Tuner_2", "Tuner_3", "Playback_2", "Recorder_3", "Tuner_4", "Playback_3", "Backup_1", "Backup_2", "Secondary_TV"};
    private static final java.lang.String TAG = "HdmiUtils";
    static final int TARGET_NOT_UNDER_LOCAL_DEVICE = -1;
    static final int TARGET_SAME_PHYSICAL_ADDRESS = 0;

    private HdmiUtils() {
    }

    static boolean isValidAddress(int address) {
        return address >= 0 && address <= 14;
    }

    static boolean isEligibleAddressForDevice(int deviceType, int logicalAddress) {
        return isValidAddress(logicalAddress) && ADDRESS_TO_TYPE.get(java.lang.Integer.valueOf(logicalAddress)).contains(java.lang.Integer.valueOf(deviceType));
    }

    static boolean isEligibleAddressForCecVersion(int cecVersion, int logicalAddress) {
        if (isValidAddress(logicalAddress)) {
            return !(logicalAddress == 12 || logicalAddress == 13) || cecVersion >= 6;
        }
        return false;
    }

    static java.util.List<java.lang.Integer> getTypeFromAddress(int logicalAddress) {
        if (isValidAddress(logicalAddress)) {
            return ADDRESS_TO_TYPE.get(java.lang.Integer.valueOf(logicalAddress));
        }
        return com.google.android.collect.Lists.newArrayList(new java.lang.Integer[]{-1});
    }

    static java.lang.String getDefaultDeviceName(int address) {
        if (isValidAddress(address)) {
            return DEFAULT_NAMES[address];
        }
        return "";
    }

    static boolean verifyAddressType(int logicalAddress, int deviceType) {
        java.util.List<java.lang.Integer> actualDeviceTypes = getTypeFromAddress(logicalAddress);
        if (!actualDeviceTypes.contains(java.lang.Integer.valueOf(deviceType))) {
            android.util.Slog.w(TAG, "Device type mismatch:[Expected:" + deviceType + ", Actual:" + actualDeviceTypes + "]");
            return false;
        }
        return true;
    }

    static boolean checkCommandSource(com.android.server.hdmi.HdmiCecMessage cmd, int expectedAddress, java.lang.String tag) {
        int src = cmd.getSource();
        if (src != expectedAddress) {
            android.util.Slog.w(tag, "Invalid source [Expected:" + expectedAddress + ", Actual:" + src + "]");
            return false;
        }
        return true;
    }

    static boolean parseCommandParamSystemAudioStatus(com.android.server.hdmi.HdmiCecMessage cmd) {
        return cmd.getParams()[0] == 1;
    }

    static boolean isAudioStatusMute(com.android.server.hdmi.HdmiCecMessage cmd) {
        byte[] params = cmd.getParams();
        return (params[0] & 128) == 128;
    }

    static int getAudioStatusVolume(com.android.server.hdmi.HdmiCecMessage cmd) {
        byte[] params = cmd.getParams();
        int volume = params[0] & 127;
        if (volume < 0 || 100 < volume) {
            return -1;
        }
        return volume;
    }

    static java.util.List<java.lang.Integer> asImmutableList(int[] is) {
        java.util.ArrayList<java.lang.Integer> list = new java.util.ArrayList<>(is.length);
        for (int type : is) {
            list.add(java.lang.Integer.valueOf(type));
        }
        return java.util.Collections.unmodifiableList(list);
    }

    static int twoBytesToInt(byte[] data) {
        return ((data[0] & 255) << 8) | (data[1] & 255);
    }

    static int twoBytesToInt(byte[] data, int offset) {
        return ((data[offset] & 255) << 8) | (data[offset + 1] & 255);
    }

    static int threeBytesToInt(byte[] data) {
        return ((data[0] & 255) << 16) | ((data[1] & 255) << 8) | (data[2] & 255);
    }

    static <T> java.util.List<T> sparseArrayToList(android.util.SparseArray<T> array) {
        java.util.ArrayList<T> list = new java.util.ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            list.add(array.valueAt(i));
        }
        return list;
    }

    static <T> java.util.List<T> mergeToUnmodifiableList(java.util.List<T> a, java.util.List<T> b) {
        if (a.isEmpty() && b.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        if (a.isEmpty()) {
            return java.util.Collections.unmodifiableList(b);
        }
        if (b.isEmpty()) {
            return java.util.Collections.unmodifiableList(a);
        }
        java.util.List<T> newList = new java.util.ArrayList<>();
        newList.addAll(a);
        newList.addAll(b);
        return java.util.Collections.unmodifiableList(newList);
    }

    static boolean isAffectingActiveRoutingPath(int activePath, int newPath) {
        int i = 0;
        while (true) {
            if (i > 12) {
                break;
            }
            int nibble = (newPath >> i) & 15;
            if (nibble == 0) {
                i += 4;
            } else {
                int mask = 65520 << i;
                newPath &= mask;
                break;
            }
        }
        if (newPath == 0) {
            return true;
        }
        return isInActiveRoutingPath(activePath, newPath);
    }

    static boolean isInActiveRoutingPath(int activePath, int newPath) {
        int pathRelationship = pathRelationship(newPath, activePath);
        return pathRelationship == 2 || pathRelationship == 3 || pathRelationship == 5;
    }

    static int pathRelationship(int firstPath, int secondPath) {
        if (firstPath == 65535 || secondPath == 65535) {
            return 0;
        }
        for (int nibbleIndex = 0; nibbleIndex <= 3; nibbleIndex++) {
            int shift = 12 - (nibbleIndex * 4);
            int firstPathNibble = (firstPath >> shift) & 15;
            int secondPathNibble = (secondPath >> shift) & 15;
            if (firstPathNibble != secondPathNibble) {
                int firstPathNextNibble = (firstPath >> (shift - 4)) & 15;
                int secondPathNextNibble = (secondPath >> (shift - 4)) & 15;
                if (firstPathNibble == 0) {
                    return 2;
                }
                if (secondPathNibble == 0) {
                    return 3;
                }
                if (nibbleIndex == 3) {
                    return 4;
                }
                if (firstPathNextNibble == 0 && secondPathNextNibble == 0) {
                    return 4;
                }
                return 1;
            }
        }
        return 5;
    }

    static <T> void dumpSparseArray(com.android.internal.util.IndentingPrintWriter pw, java.lang.String name, android.util.SparseArray<T> sparseArray) {
        printWithTrailingColon(pw, name);
        pw.increaseIndent();
        int size = sparseArray.size();
        for (int i = 0; i < size; i++) {
            int key = sparseArray.keyAt(i);
            T value = sparseArray.get(key);
            pw.printPair(java.lang.Integer.toString(key), value);
            pw.println();
        }
        pw.decreaseIndent();
    }

    private static void printWithTrailingColon(com.android.internal.util.IndentingPrintWriter pw, java.lang.String name) {
        pw.println(name.endsWith(":") ? name : name.concat(":"));
    }

    static <K, V> void dumpMap(com.android.internal.util.IndentingPrintWriter pw, java.lang.String name, java.util.Map<K, V> map) {
        printWithTrailingColon(pw, name);
        pw.increaseIndent();
        for (java.util.Map.Entry<K, V> entry : map.entrySet()) {
            pw.printPair(entry.getKey().toString(), entry.getValue());
            pw.println();
        }
        pw.decreaseIndent();
    }

    static <T> void dumpIterable(com.android.internal.util.IndentingPrintWriter pw, java.lang.String name, java.lang.Iterable<T> values) {
        printWithTrailingColon(pw, name);
        pw.increaseIndent();
        for (T value : values) {
            pw.println(value);
        }
        pw.decreaseIndent();
    }

    public static int getLocalPortFromPhysicalAddress(int targetPhysicalAddress, int myPhysicalAddress) {
        if (myPhysicalAddress == targetPhysicalAddress) {
            return 0;
        }
        int mask = 61440;
        int finalMask = 61440;
        int maskedAddress = myPhysicalAddress;
        while (maskedAddress != 0) {
            maskedAddress = myPhysicalAddress & mask;
            finalMask |= mask;
            mask >>= 4;
        }
        int portAddress = targetPhysicalAddress & finalMask;
        if (((finalMask << 4) & portAddress) != myPhysicalAddress) {
            return -1;
        }
        int port = portAddress & (mask << 4);
        while ((port >> 4) != 0) {
            port >>= 4;
        }
        return port;
    }

    static int getAbortFeatureOpcode(com.android.server.hdmi.HdmiCecMessage cmd) {
        return cmd.getParams()[0] & 255;
    }

    static int getAbortReason(com.android.server.hdmi.HdmiCecMessage cmd) {
        return cmd.getParams()[1];
    }

    public static com.android.server.hdmi.HdmiCecMessage buildMessage(java.lang.String message) {
        java.lang.String[] parts = message.split(":");
        if (parts.length < 2) {
            throw new java.lang.IllegalArgumentException("Message is too short");
        }
        for (java.lang.String part : parts) {
            if (part.length() != 2) {
                throw new java.lang.IllegalArgumentException("Malformatted CEC message: " + message);
            }
        }
        int src = java.lang.Integer.parseInt(parts[0].substring(0, 1), 16);
        int dest = java.lang.Integer.parseInt(parts[0].substring(1, 2), 16);
        int opcode = java.lang.Integer.parseInt(parts[1], 16);
        byte[] params = new byte[parts.length - 2];
        for (int i = 0; i < params.length; i++) {
            params[i] = (byte) java.lang.Integer.parseInt(parts[i + 2], 16);
        }
        return com.android.server.hdmi.HdmiCecMessage.build(src, dest, opcode, params);
    }

    public static int getEndOfSequence(byte[] params, int offset) {
        if (offset < 0) {
            return -1;
        }
        while (offset < params.length && ((params[offset] >> 7) & 1) == 1) {
            offset++;
        }
        if (offset >= params.length) {
            return -1;
        }
        return offset;
    }

    public static class ShortAudioDescriptorXmlParser {
        private static final java.lang.String NS = null;

        public static java.util.List<com.android.server.hdmi.HdmiUtils.DeviceConfig> parse(java.io.InputStream in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
            parser.nextTag();
            return readDevices(parser);
        }

        private static void skip(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            if (parser.getEventType() != 2) {
                throw new java.lang.IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case 2:
                        depth++;
                        break;
                    case 3:
                        depth--;
                        break;
                }
            }
        }

        private static java.util.List<com.android.server.hdmi.HdmiUtils.DeviceConfig> readDevices(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.util.List<com.android.server.hdmi.HdmiUtils.DeviceConfig> devices = new java.util.ArrayList<>();
            parser.require(2, NS, "config");
            while (parser.next() != 3) {
                if (parser.getEventType() == 2) {
                    java.lang.String name = parser.getName();
                    if (name.equals("device")) {
                        java.lang.String deviceType = parser.getAttributeValue((java.lang.String) null, "type");
                        com.android.server.hdmi.HdmiUtils.DeviceConfig config = null;
                        if (deviceType != null) {
                            config = readDeviceConfig(parser, deviceType);
                        }
                        if (config != null) {
                            devices.add(config);
                        }
                    } else {
                        skip(parser);
                    }
                }
            }
            return devices;
        }

        private static com.android.server.hdmi.HdmiUtils.DeviceConfig readDeviceConfig(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String deviceType) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.util.List<com.android.server.hdmi.HdmiUtils.CodecSad> codecSads = new java.util.ArrayList<>();
            parser.require(2, NS, "device");
            while (parser.next() != 3) {
                if (parser.getEventType() == 2) {
                    java.lang.String tagName = parser.getName();
                    if (tagName.equals("supportedFormat")) {
                        java.lang.String codecAttriValue = parser.getAttributeValue((java.lang.String) null, "format");
                        java.lang.String sadAttriValue = parser.getAttributeValue((java.lang.String) null, "descriptor");
                        int format = codecAttriValue == null ? 0 : formatNameToNum(codecAttriValue);
                        byte[] descriptor = readSad(sadAttriValue);
                        if (format != 0 && descriptor != null) {
                            codecSads.add(new com.android.server.hdmi.HdmiUtils.CodecSad(format, descriptor));
                        }
                        parser.nextTag();
                        parser.require(3, NS, "supportedFormat");
                    } else {
                        skip(parser);
                    }
                }
            }
            if (codecSads.size() == 0) {
                return null;
            }
            return new com.android.server.hdmi.HdmiUtils.DeviceConfig(deviceType, codecSads);
        }

        private static byte[] readSad(java.lang.String sad) {
            if (sad == null || sad.length() == 0) {
                return null;
            }
            byte[] sadBytes = com.android.internal.util.HexDump.hexStringToByteArray(sad);
            if (sadBytes.length != 3) {
                android.util.Slog.w(com.android.server.hdmi.HdmiUtils.TAG, "SAD byte array length is not 3. Length = " + sadBytes.length);
                return null;
            }
            return sadBytes;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:53:0x00ce  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private static int formatNameToNum(java.lang.String r18) {
            /*
                Method dump skipped, instruction units count: 330
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.hdmi.HdmiUtils.ShortAudioDescriptorXmlParser.formatNameToNum(java.lang.String):int");
        }
    }

    public static class DeviceConfig {
        public final java.lang.String name;
        public final java.util.List<com.android.server.hdmi.HdmiUtils.CodecSad> supportedCodecs;

        public DeviceConfig(java.lang.String name, java.util.List<com.android.server.hdmi.HdmiUtils.CodecSad> supportedCodecs) {
            this.name = name;
            this.supportedCodecs = supportedCodecs;
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.hdmi.HdmiUtils.DeviceConfig)) {
                return false;
            }
            com.android.server.hdmi.HdmiUtils.DeviceConfig that = (com.android.server.hdmi.HdmiUtils.DeviceConfig) obj;
            return that.name.equals(this.name) && that.supportedCodecs.equals(this.supportedCodecs);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.name, java.lang.Integer.valueOf(this.supportedCodecs.hashCode()));
        }
    }

    public static class CodecSad {
        public final int audioCodec;
        public final byte[] sad;

        public CodecSad(int audioCodec, byte[] sad) {
            this.audioCodec = audioCodec;
            this.sad = sad;
        }

        public CodecSad(int audioCodec, java.lang.String sad) {
            this.audioCodec = audioCodec;
            this.sad = com.android.internal.util.HexDump.hexStringToByteArray(sad);
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.hdmi.HdmiUtils.CodecSad)) {
                return false;
            }
            com.android.server.hdmi.HdmiUtils.CodecSad that = (com.android.server.hdmi.HdmiUtils.CodecSad) obj;
            return that.audioCodec == this.audioCodec && java.util.Arrays.equals(that.sad, this.sad);
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.audioCodec), java.lang.Integer.valueOf(java.util.Arrays.hashCode(this.sad)));
        }
    }
}
