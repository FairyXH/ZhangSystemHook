package com.android.server.powerstats;

/* JADX INFO: loaded from: classes3.dex */
public class ProtoStreamUtils {
    private static final java.lang.String TAG = com.android.server.powerstats.ProtoStreamUtils.class.getSimpleName();

    static class PowerEntityUtils {
        PowerEntityUtils() {
        }

        public static byte[] getProtoBytes(android.hardware.power.stats.PowerEntity[] powerEntity) {
            android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
            packProtoMessage(powerEntity, pos);
            return pos.getBytes();
        }

        public static void packProtoMessage(android.hardware.power.stats.PowerEntity[] powerEntity, android.util.proto.ProtoOutputStream pos) {
            if (powerEntity == null) {
                return;
            }
            for (int i = 0; i < powerEntity.length; i++) {
                long peToken = pos.start(2246267895809L);
                pos.write(1120986464257L, powerEntity[i].id);
                pos.write(1138166333442L, powerEntity[i].name);
                if (powerEntity[i].states != null) {
                    int statesLength = powerEntity[i].states.length;
                    for (int j = 0; j < statesLength; j++) {
                        android.hardware.power.stats.State state = powerEntity[i].states[j];
                        long stateToken = pos.start(2246267895811L);
                        pos.write(1120986464257L, state.id);
                        pos.write(1138166333442L, state.name);
                        pos.end(stateToken);
                    }
                }
                pos.end(peToken);
            }
        }

        public static void print(android.hardware.power.stats.PowerEntity[] powerEntity) {
            if (powerEntity == null) {
                return;
            }
            for (int i = 0; i < powerEntity.length; i++) {
                android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "powerEntityId: " + powerEntity[i].id + ", powerEntityName: " + powerEntity[i].name);
                if (powerEntity[i].states != null) {
                    for (int j = 0; j < powerEntity[i].states.length; j++) {
                        android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "  StateId: " + powerEntity[i].states[j].id + ", StateName: " + powerEntity[i].states[j].name);
                    }
                }
            }
        }

        public static void dumpsys(android.hardware.power.stats.PowerEntity[] powerEntity, java.io.PrintWriter pw) {
            if (powerEntity == null) {
                return;
            }
            for (int i = 0; i < powerEntity.length; i++) {
                pw.println("PowerEntityId: " + powerEntity[i].id + ", PowerEntityName: " + powerEntity[i].name);
                if (powerEntity[i].states != null) {
                    for (int j = 0; j < powerEntity[i].states.length; j++) {
                        pw.println("  StateId: " + powerEntity[i].states[j].id + ", StateName: " + powerEntity[i].states[j].name);
                    }
                }
            }
        }
    }

    static class StateResidencyResultUtils {
        StateResidencyResultUtils() {
        }

        public static void adjustTimeSinceBootToEpoch(android.hardware.power.stats.StateResidencyResult[] stateResidencyResult, long startWallTime) {
            if (stateResidencyResult == null) {
                return;
            }
            for (int i = 0; i < stateResidencyResult.length; i++) {
                int stateLength = stateResidencyResult[i].stateResidencyData.length;
                for (int j = 0; j < stateLength; j++) {
                    android.hardware.power.stats.StateResidency stateResidencyData = stateResidencyResult[i].stateResidencyData[j];
                    stateResidencyData.lastEntryTimestampMs += startWallTime;
                }
            }
        }

        public static byte[] getProtoBytes(android.hardware.power.stats.StateResidencyResult[] stateResidencyResult) {
            android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
            packProtoMessage(stateResidencyResult, pos);
            return pos.getBytes();
        }

        public static void packProtoMessage(android.hardware.power.stats.StateResidencyResult[] stateResidencyResult, android.util.proto.ProtoOutputStream pos) {
            if (stateResidencyResult == null) {
                return;
            }
            for (int i = 0; i < stateResidencyResult.length; i++) {
                int stateLength = stateResidencyResult[i].stateResidencyData.length;
                long j = 2246267895810L;
                long srrToken = pos.start(2246267895810L);
                pos.write(1120986464257L, stateResidencyResult[i].id);
                int j2 = 0;
                while (j2 < stateLength) {
                    android.hardware.power.stats.StateResidency stateResidencyData = stateResidencyResult[i].stateResidencyData[j2];
                    long srdToken = pos.start(j);
                    pos.write(1120986464257L, stateResidencyData.id);
                    pos.write(1112396529666L, stateResidencyData.totalTimeInStateMs);
                    pos.write(1112396529667L, stateResidencyData.totalStateEntryCount);
                    pos.write(1112396529668L, stateResidencyData.lastEntryTimestampMs);
                    pos.end(srdToken);
                    j2++;
                    j = 2246267895810L;
                }
                pos.end(srrToken);
            }
        }

        public static android.hardware.power.stats.StateResidencyResult[] unpackProtoMessage(byte[] data) throws java.io.IOException {
            android.util.proto.ProtoInputStream pis = new android.util.proto.ProtoInputStream(new java.io.ByteArrayInputStream(data));
            java.util.List<android.hardware.power.stats.StateResidencyResult> stateResidencyResultList = new java.util.ArrayList<>();
            while (true) {
                try {
                    int nextField = pis.nextField();
                    new android.hardware.power.stats.StateResidencyResult();
                    if (nextField == 2) {
                        long token = pis.start(2246267895810L);
                        stateResidencyResultList.add(unpackStateResidencyResultProto(pis));
                        pis.end(token);
                    } else {
                        if (nextField == -1) {
                            return (android.hardware.power.stats.StateResidencyResult[]) stateResidencyResultList.toArray(new android.hardware.power.stats.StateResidencyResult[stateResidencyResultList.size()]);
                        }
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in PowerStatsServiceResidencyProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    }
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in PowerStatsServiceResidencyProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
            }
        }

        private static android.hardware.power.stats.StateResidencyResult unpackStateResidencyResultProto(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
            android.hardware.power.stats.StateResidencyResult stateResidencyResult = new android.hardware.power.stats.StateResidencyResult();
            java.util.List<android.hardware.power.stats.StateResidency> stateResidencyList = new java.util.ArrayList<>();
            while (true) {
                try {
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in StateResidencyResultProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
                switch (pis.nextField()) {
                    case -1:
                        stateResidencyResult.stateResidencyData = (android.hardware.power.stats.StateResidency[]) stateResidencyList.toArray(new android.hardware.power.stats.StateResidency[stateResidencyList.size()]);
                        return stateResidencyResult;
                    case 0:
                    default:
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in StateResidencyResultProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                        continue;
                    case 1:
                        stateResidencyResult.id = pis.readInt(1120986464257L);
                        continue;
                    case 2:
                        long token = pis.start(2246267895810L);
                        stateResidencyList.add(unpackStateResidencyProto(pis));
                        pis.end(token);
                        continue;
                }
                android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in StateResidencyResultProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
            }
        }

        private static android.hardware.power.stats.StateResidency unpackStateResidencyProto(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
            android.hardware.power.stats.StateResidency stateResidency = new android.hardware.power.stats.StateResidency();
            while (true) {
                try {
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in StateResidencyProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
                switch (pis.nextField()) {
                    case -1:
                        return stateResidency;
                    case 0:
                    default:
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in StateResidencyProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                        continue;
                    case 1:
                        stateResidency.id = pis.readInt(1120986464257L);
                        continue;
                    case 2:
                        stateResidency.totalTimeInStateMs = pis.readLong(1112396529666L);
                        continue;
                    case 3:
                        stateResidency.totalStateEntryCount = pis.readLong(1112396529667L);
                        continue;
                    case 4:
                        stateResidency.lastEntryTimestampMs = pis.readLong(1112396529668L);
                        continue;
                }
                android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in StateResidencyProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
            }
        }

        public static void print(android.hardware.power.stats.StateResidencyResult[] stateResidencyResult) {
            if (stateResidencyResult == null) {
                return;
            }
            for (int i = 0; i < stateResidencyResult.length; i++) {
                android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "PowerEntityId: " + stateResidencyResult[i].id);
                for (int j = 0; j < stateResidencyResult[i].stateResidencyData.length; j++) {
                    android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "  StateId: " + stateResidencyResult[i].stateResidencyData[j].id + ", TotalTimeInStateMs: " + stateResidencyResult[i].stateResidencyData[j].totalTimeInStateMs + ", TotalStateEntryCount: " + stateResidencyResult[i].stateResidencyData[j].totalStateEntryCount + ", LastEntryTimestampMs: " + stateResidencyResult[i].stateResidencyData[j].lastEntryTimestampMs);
                }
            }
        }
    }

    static class ChannelUtils {
        ChannelUtils() {
        }

        public static byte[] getProtoBytes(android.hardware.power.stats.Channel[] channel) {
            android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
            packProtoMessage(channel, pos);
            return pos.getBytes();
        }

        public static void packProtoMessage(android.hardware.power.stats.Channel[] channel, android.util.proto.ProtoOutputStream pos) {
            if (channel == null) {
                return;
            }
            for (int i = 0; i < channel.length; i++) {
                long token = pos.start(2246267895809L);
                pos.write(1120986464257L, channel[i].id);
                pos.write(1138166333442L, channel[i].name);
                pos.write(1138166333443L, channel[i].subsystem);
                pos.end(token);
            }
        }

        public static void print(android.hardware.power.stats.Channel[] channel) {
            if (channel == null) {
                return;
            }
            for (int i = 0; i < channel.length; i++) {
                android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "ChannelId: " + channel[i].id + ", ChannelName: " + channel[i].name + ", ChannelSubsystem: " + channel[i].subsystem);
            }
        }

        public static void dumpsys(android.hardware.power.stats.Channel[] channel, java.io.PrintWriter pw) {
            if (channel == null) {
                return;
            }
            for (int i = 0; i < channel.length; i++) {
                pw.println("ChannelId: " + channel[i].id + ", ChannelName: " + channel[i].name + ", ChannelSubsystem: " + channel[i].subsystem);
            }
        }
    }

    static class EnergyMeasurementUtils {
        EnergyMeasurementUtils() {
        }

        public static void adjustTimeSinceBootToEpoch(android.hardware.power.stats.EnergyMeasurement[] energyMeasurement, long startWallTime) {
            if (energyMeasurement == null) {
                return;
            }
            for (android.hardware.power.stats.EnergyMeasurement energyMeasurement2 : energyMeasurement) {
                energyMeasurement2.timestampMs += startWallTime;
            }
        }

        public static byte[] getProtoBytes(android.hardware.power.stats.EnergyMeasurement[] energyMeasurement) {
            android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
            packProtoMessage(energyMeasurement, pos);
            return pos.getBytes();
        }

        public static void packProtoMessage(android.hardware.power.stats.EnergyMeasurement[] energyMeasurement, android.util.proto.ProtoOutputStream pos) {
            if (energyMeasurement == null) {
                return;
            }
            for (int i = 0; i < energyMeasurement.length; i++) {
                long token = pos.start(2246267895810L);
                pos.write(1120986464257L, energyMeasurement[i].id);
                pos.write(1112396529666L, energyMeasurement[i].timestampMs);
                pos.write(1112396529668L, energyMeasurement[i].durationMs);
                pos.write(1112396529667L, energyMeasurement[i].energyUWs);
                pos.end(token);
            }
        }

        public static android.hardware.power.stats.EnergyMeasurement[] unpackProtoMessage(byte[] data) throws java.io.IOException {
            android.util.proto.ProtoInputStream pis = new android.util.proto.ProtoInputStream(new java.io.ByteArrayInputStream(data));
            java.util.List<android.hardware.power.stats.EnergyMeasurement> energyMeasurementList = new java.util.ArrayList<>();
            while (true) {
                try {
                    int nextField = pis.nextField();
                    new android.hardware.power.stats.EnergyMeasurement();
                    if (nextField == 2) {
                        long token = pis.start(2246267895810L);
                        energyMeasurementList.add(unpackEnergyMeasurementProto(pis));
                        pis.end(token);
                    } else {
                        if (nextField == -1) {
                            return (android.hardware.power.stats.EnergyMeasurement[]) energyMeasurementList.toArray(new android.hardware.power.stats.EnergyMeasurement[energyMeasurementList.size()]);
                        }
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in proto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    }
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in proto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
            }
        }

        private static android.hardware.power.stats.EnergyMeasurement unpackEnergyMeasurementProto(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
            android.hardware.power.stats.EnergyMeasurement energyMeasurement = new android.hardware.power.stats.EnergyMeasurement();
            while (true) {
                try {
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyMeasurementProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
                switch (pis.nextField()) {
                    case -1:
                        return energyMeasurement;
                    case 0:
                    default:
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in EnergyMeasurementProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                        continue;
                    case 1:
                        energyMeasurement.id = pis.readInt(1120986464257L);
                        continue;
                    case 2:
                        energyMeasurement.timestampMs = pis.readLong(1112396529666L);
                        continue;
                    case 3:
                        energyMeasurement.energyUWs = pis.readLong(1112396529667L);
                        continue;
                    case 4:
                        energyMeasurement.durationMs = pis.readLong(1112396529668L);
                        continue;
                }
                android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyMeasurementProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
            }
        }

        public static void print(android.hardware.power.stats.EnergyMeasurement[] energyMeasurement) {
            if (energyMeasurement == null) {
                return;
            }
            for (int i = 0; i < energyMeasurement.length; i++) {
                android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "ChannelId: " + energyMeasurement[i].id + ", Timestamp (ms): " + energyMeasurement[i].timestampMs + ", Duration (ms): " + energyMeasurement[i].durationMs + ", Energy (uWs): " + energyMeasurement[i].energyUWs);
            }
        }
    }

    static class EnergyConsumerUtils {
        EnergyConsumerUtils() {
        }

        public static byte[] getProtoBytes(android.hardware.power.stats.EnergyConsumer[] energyConsumer) {
            android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
            packProtoMessage(energyConsumer, pos);
            return pos.getBytes();
        }

        public static void packProtoMessage(android.hardware.power.stats.EnergyConsumer[] energyConsumer, android.util.proto.ProtoOutputStream pos) {
            if (energyConsumer == null) {
                return;
            }
            for (int i = 0; i < energyConsumer.length; i++) {
                long token = pos.start(2246267895809L);
                pos.write(1120986464257L, energyConsumer[i].id);
                pos.write(1120986464258L, energyConsumer[i].ordinal);
                pos.write(1120986464259L, (int) energyConsumer[i].type);
                pos.write(1138166333444L, energyConsumer[i].name);
                pos.end(token);
            }
        }

        public static android.hardware.power.stats.EnergyConsumer[] unpackProtoMessage(byte[] data) throws java.io.IOException {
            android.util.proto.ProtoInputStream pis = new android.util.proto.ProtoInputStream(new java.io.ByteArrayInputStream(data));
            java.util.List<android.hardware.power.stats.EnergyConsumer> energyConsumerList = new java.util.ArrayList<>();
            while (true) {
                try {
                    int nextField = pis.nextField();
                    new android.hardware.power.stats.EnergyConsumer();
                    if (nextField == 1) {
                        long token = pis.start(2246267895809L);
                        energyConsumerList.add(unpackEnergyConsumerProto(pis));
                        pis.end(token);
                    } else {
                        if (nextField == -1) {
                            return (android.hardware.power.stats.EnergyConsumer[]) energyConsumerList.toArray(new android.hardware.power.stats.EnergyConsumer[energyConsumerList.size()]);
                        }
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in proto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    }
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in proto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
            }
        }

        private static android.hardware.power.stats.EnergyConsumer unpackEnergyConsumerProto(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
            android.hardware.power.stats.EnergyConsumer energyConsumer = new android.hardware.power.stats.EnergyConsumer();
            while (true) {
                try {
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
                switch (pis.nextField()) {
                    case -1:
                        return energyConsumer;
                    case 0:
                    default:
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in EnergyConsumerProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                        continue;
                    case 1:
                        energyConsumer.id = pis.readInt(1120986464257L);
                        continue;
                    case 2:
                        energyConsumer.ordinal = pis.readInt(1120986464258L);
                        continue;
                    case 3:
                        energyConsumer.type = (byte) pis.readInt(1120986464259L);
                        continue;
                    case 4:
                        energyConsumer.name = pis.readString(1138166333444L);
                        continue;
                }
                android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
            }
        }

        public static void print(android.hardware.power.stats.EnergyConsumer[] energyConsumer) {
            if (energyConsumer == null) {
                return;
            }
            for (int i = 0; i < energyConsumer.length; i++) {
                android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "EnergyConsumerId: " + energyConsumer[i].id + ", Ordinal: " + energyConsumer[i].ordinal + ", Type: " + ((int) energyConsumer[i].type) + ", Name: " + energyConsumer[i].name);
            }
        }

        public static void dumpsys(android.hardware.power.stats.EnergyConsumer[] energyConsumer, java.io.PrintWriter pw) {
            if (energyConsumer == null) {
                return;
            }
            for (int i = 0; i < energyConsumer.length; i++) {
                pw.println("EnergyConsumerId: " + energyConsumer[i].id + ", Ordinal: " + energyConsumer[i].ordinal + ", Type: " + ((int) energyConsumer[i].type) + ", Name: " + energyConsumer[i].name);
            }
        }
    }

    static class EnergyConsumerResultUtils {
        EnergyConsumerResultUtils() {
        }

        public static void adjustTimeSinceBootToEpoch(android.hardware.power.stats.EnergyConsumerResult[] energyConsumerResult, long startWallTime) {
            if (energyConsumerResult == null) {
                return;
            }
            for (android.hardware.power.stats.EnergyConsumerResult energyConsumerResult2 : energyConsumerResult) {
                energyConsumerResult2.timestampMs += startWallTime;
            }
        }

        public static byte[] getProtoBytes(android.hardware.power.stats.EnergyConsumerResult[] energyConsumerResult, boolean includeAttribution) {
            android.util.proto.ProtoOutputStream pos = new android.util.proto.ProtoOutputStream();
            packProtoMessage(energyConsumerResult, pos, includeAttribution);
            return pos.getBytes();
        }

        public static void packProtoMessage(android.hardware.power.stats.EnergyConsumerResult[] energyConsumerResult, android.util.proto.ProtoOutputStream pos, boolean includeAttribution) {
            if (energyConsumerResult == null) {
                return;
            }
            for (int i = 0; i < energyConsumerResult.length; i++) {
                long ecrToken = pos.start(2246267895810L);
                pos.write(1120986464257L, energyConsumerResult[i].id);
                pos.write(1112396529666L, energyConsumerResult[i].timestampMs);
                pos.write(1112396529667L, energyConsumerResult[i].energyUWs);
                if (includeAttribution) {
                    int attributionLength = energyConsumerResult[i].attribution.length;
                    for (int j = 0; j < attributionLength; j++) {
                        android.hardware.power.stats.EnergyConsumerAttribution energyConsumerAttribution = energyConsumerResult[i].attribution[j];
                        long ecaToken = pos.start(2246267895812L);
                        pos.write(1120986464257L, energyConsumerAttribution.uid);
                        pos.write(1112396529666L, energyConsumerAttribution.energyUWs);
                        pos.end(ecaToken);
                    }
                }
                pos.end(ecrToken);
            }
        }

        public static android.hardware.power.stats.EnergyConsumerResult[] unpackProtoMessage(byte[] data) throws java.io.IOException {
            android.util.proto.ProtoInputStream pis = new android.util.proto.ProtoInputStream(new java.io.ByteArrayInputStream(data));
            java.util.List<android.hardware.power.stats.EnergyConsumerResult> energyConsumerResultList = new java.util.ArrayList<>();
            while (true) {
                try {
                    int nextField = pis.nextField();
                    new android.hardware.power.stats.EnergyConsumerResult();
                    if (nextField == 2) {
                        long token = pis.start(2246267895810L);
                        energyConsumerResultList.add(unpackEnergyConsumerResultProto(pis));
                        pis.end(token);
                    } else {
                        if (nextField == -1) {
                            return (android.hardware.power.stats.EnergyConsumerResult[]) energyConsumerResultList.toArray(new android.hardware.power.stats.EnergyConsumerResult[energyConsumerResultList.size()]);
                        }
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in proto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                    }
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in proto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
            }
        }

        private static android.hardware.power.stats.EnergyConsumerAttribution unpackEnergyConsumerAttributionProto(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
            android.hardware.power.stats.EnergyConsumerAttribution energyConsumerAttribution = new android.hardware.power.stats.EnergyConsumerAttribution();
            while (true) {
                try {
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerAttributionProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
                switch (pis.nextField()) {
                    case -1:
                        return energyConsumerAttribution;
                    case 0:
                    default:
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in EnergyConsumerAttributionProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                        continue;
                    case 1:
                        energyConsumerAttribution.uid = pis.readInt(1120986464257L);
                        continue;
                    case 2:
                        energyConsumerAttribution.energyUWs = pis.readLong(1112396529666L);
                        continue;
                }
                android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerAttributionProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
            }
        }

        private static android.hardware.power.stats.EnergyConsumerResult unpackEnergyConsumerResultProto(android.util.proto.ProtoInputStream pis) throws java.io.IOException {
            android.hardware.power.stats.EnergyConsumerResult energyConsumerResult = new android.hardware.power.stats.EnergyConsumerResult();
            java.util.List<android.hardware.power.stats.EnergyConsumerAttribution> energyConsumerAttributionList = new java.util.ArrayList<>();
            while (true) {
                try {
                } catch (android.util.proto.WireTypeMismatchException e) {
                    android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerResultProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                }
                switch (pis.nextField()) {
                    case -1:
                        energyConsumerResult.attribution = (android.hardware.power.stats.EnergyConsumerAttribution[]) energyConsumerAttributionList.toArray(new android.hardware.power.stats.EnergyConsumerAttribution[energyConsumerAttributionList.size()]);
                        return energyConsumerResult;
                    case 0:
                    default:
                        android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Unhandled field in EnergyConsumerResultProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
                        continue;
                    case 1:
                        energyConsumerResult.id = pis.readInt(1120986464257L);
                        continue;
                    case 2:
                        energyConsumerResult.timestampMs = pis.readLong(1112396529666L);
                        continue;
                    case 3:
                        energyConsumerResult.energyUWs = pis.readLong(1112396529667L);
                        continue;
                    case 4:
                        long token = pis.start(2246267895812L);
                        energyConsumerAttributionList.add(unpackEnergyConsumerAttributionProto(pis));
                        pis.end(token);
                        continue;
                }
                android.util.Slog.e(com.android.server.powerstats.ProtoStreamUtils.TAG, "Wire Type mismatch in EnergyConsumerResultProto: " + android.util.proto.ProtoUtils.currentFieldToString(pis));
            }
        }

        public static void print(android.hardware.power.stats.EnergyConsumerResult[] energyConsumerResult) {
            if (energyConsumerResult == null) {
                return;
            }
            for (android.hardware.power.stats.EnergyConsumerResult result : energyConsumerResult) {
                android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "EnergyConsumerId: " + result.id + ", Timestamp (ms): " + result.timestampMs + ", Energy (uWs): " + result.energyUWs);
                int attributionLength = result.attribution.length;
                for (int j = 0; j < attributionLength; j++) {
                    android.hardware.power.stats.EnergyConsumerAttribution attribution = result.attribution[j];
                    android.util.Slog.d(com.android.server.powerstats.ProtoStreamUtils.TAG, "  UID: " + attribution.uid + "  Energy (uWs): " + attribution.energyUWs);
                }
            }
        }
    }
}
