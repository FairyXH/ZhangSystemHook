package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class EnergyConsumerSnapshot {
    private static final int MILLIVOLTS_PER_VOLT = 1000;
    private static final java.lang.String TAG = "EnergyConsumerSnapshot";
    public static final long UNAVAILABLE = -1;
    private final android.util.SparseArray<android.util.SparseLongArray> mAttributionSnapshots;
    private final android.util.SparseLongArray mEnergyConsumerSnapshots;
    private final android.util.SparseArray<android.hardware.power.stats.EnergyConsumer> mEnergyConsumers;
    private final int mNumCpuClusterOrdinals;
    private final int mNumDisplayOrdinals;
    private final int mNumOtherOrdinals;
    private final android.util.SparseIntArray mVoltageSnapshots;

    EnergyConsumerSnapshot(android.util.SparseArray<android.hardware.power.stats.EnergyConsumer> idToConsumerMap) {
        this.mEnergyConsumers = idToConsumerMap;
        this.mEnergyConsumerSnapshots = new android.util.SparseLongArray(this.mEnergyConsumers.size());
        this.mVoltageSnapshots = new android.util.SparseIntArray(this.mEnergyConsumers.size());
        this.mNumCpuClusterOrdinals = calculateNumOrdinals(2, idToConsumerMap);
        this.mNumDisplayOrdinals = calculateNumOrdinals(3, idToConsumerMap);
        this.mNumOtherOrdinals = calculateNumOrdinals(0, idToConsumerMap);
        this.mAttributionSnapshots = new android.util.SparseArray<>(this.mNumOtherOrdinals);
    }

    static class EnergyConsumerDeltaData {
        public long bluetoothChargeUC = -1;
        public long[] cpuClusterChargeUC = null;
        public long[] displayChargeUC = null;
        public long gnssChargeUC = -1;
        public long mobileRadioChargeUC = -1;
        public long wifiChargeUC = -1;
        public long cameraChargeUC = -1;
        public long[] otherTotalChargeUC = null;
        public android.util.SparseLongArray[] otherUidChargesUC = null;

        EnergyConsumerDeltaData() {
        }

        boolean isEmpty() {
            return this.bluetoothChargeUC <= 0 && isEmpty(this.cpuClusterChargeUC) && isEmpty(this.displayChargeUC) && this.gnssChargeUC <= 0 && this.mobileRadioChargeUC <= 0 && this.wifiChargeUC <= 0 && isEmpty(this.otherTotalChargeUC);
        }

        private boolean isEmpty(long[] values) {
            if (values == null) {
                return true;
            }
            for (long value : values) {
                if (value > 0) {
                    return false;
                }
            }
            return true;
        }
    }

    public com.android.server.power.stats.EnergyConsumerSnapshot.EnergyConsumerDeltaData updateAndGetDelta(android.hardware.power.stats.EnergyConsumerResult[] ecrs, int voltageMV) {
        int i;
        int i2;
        int avgVoltageMV;
        com.android.server.power.stats.EnergyConsumerSnapshot energyConsumerSnapshot = this;
        android.hardware.power.stats.EnergyConsumerResult[] energyConsumerResultArr = ecrs;
        int i3 = voltageMV;
        android.hardware.power.stats.EnergyConsumer energyConsumer = null;
        if (energyConsumerResultArr != null && energyConsumerResultArr.length != 0) {
            if (i3 <= 0) {
                android.util.Slog.wtf(TAG, "Unexpected battery voltage (" + i3 + " mV) when taking energy consumer snapshot");
                return null;
            }
            com.android.server.power.stats.EnergyConsumerSnapshot.EnergyConsumerDeltaData output = new com.android.server.power.stats.EnergyConsumerSnapshot.EnergyConsumerDeltaData();
            int length = energyConsumerResultArr.length;
            int i4 = 0;
            while (i4 < length) {
                android.hardware.power.stats.EnergyConsumerResult ecr = energyConsumerResultArr[i4];
                int consumerId = ecr.id;
                long newEnergyUJ = ecr.energyUWs;
                android.hardware.power.stats.EnergyConsumerAttribution[] newAttributions = ecr.attribution;
                android.hardware.power.stats.EnergyConsumer consumer = energyConsumerSnapshot.mEnergyConsumers.get(consumerId, energyConsumer);
                if (consumer == null) {
                    android.util.Slog.e(TAG, "updateAndGetDelta given invalid consumerId " + consumerId);
                    avgVoltageMV = i3;
                    i = length;
                    i2 = i4;
                } else {
                    int type = consumer.type;
                    int ordinal = consumer.ordinal;
                    i = length;
                    i2 = i4;
                    long oldEnergyUJ = energyConsumerSnapshot.mEnergyConsumerSnapshots.get(consumerId, -1L);
                    int oldVoltageMV = energyConsumerSnapshot.mVoltageSnapshots.get(consumerId);
                    energyConsumerSnapshot.mEnergyConsumerSnapshots.put(consumerId, newEnergyUJ);
                    energyConsumerSnapshot.mVoltageSnapshots.put(consumerId, i3);
                    int avgVoltageMV2 = ((oldVoltageMV + i3) + 1) / 2;
                    android.util.SparseLongArray otherUidCharges = energyConsumerSnapshot.updateAndGetDeltaForTypeOther(consumer, newAttributions, avgVoltageMV2);
                    if (oldEnergyUJ >= 0) {
                        if (newEnergyUJ != oldEnergyUJ) {
                            long deltaUJ = newEnergyUJ - oldEnergyUJ;
                            if (deltaUJ < 0 || oldVoltageMV <= 0) {
                                int oldVoltageMV2 = oldVoltageMV;
                                avgVoltageMV = voltageMV;
                                android.util.Slog.e(TAG, "Bad data! EnergyConsumer " + consumer.name + ": new energy (" + newEnergyUJ + ") < old energy (" + oldEnergyUJ + "), new voltage (" + avgVoltageMV + "), old voltage (" + oldVoltageMV2 + "). Skipping. ");
                            } else {
                                long deltaChargeUC = energyConsumerSnapshot.calculateChargeConsumedUC(deltaUJ, avgVoltageMV2);
                                switch (type) {
                                    case 0:
                                        if (output.otherTotalChargeUC == null) {
                                            output.otherTotalChargeUC = new long[energyConsumerSnapshot.mNumOtherOrdinals];
                                            output.otherUidChargesUC = new android.util.SparseLongArray[energyConsumerSnapshot.mNumOtherOrdinals];
                                        }
                                        output.otherTotalChargeUC[ordinal] = deltaChargeUC;
                                        output.otherUidChargesUC[ordinal] = otherUidCharges;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    case 1:
                                        output.bluetoothChargeUC = deltaChargeUC;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    case 2:
                                        if (output.cpuClusterChargeUC == null) {
                                            output.cpuClusterChargeUC = new long[energyConsumerSnapshot.mNumCpuClusterOrdinals];
                                        }
                                        output.cpuClusterChargeUC[ordinal] = deltaChargeUC;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    case 3:
                                        if (output.displayChargeUC == null) {
                                            output.displayChargeUC = new long[energyConsumerSnapshot.mNumDisplayOrdinals];
                                        }
                                        output.displayChargeUC[ordinal] = deltaChargeUC;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    case 4:
                                        output.gnssChargeUC = deltaChargeUC;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    case 5:
                                        output.mobileRadioChargeUC = deltaChargeUC;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    case 6:
                                        output.wifiChargeUC = deltaChargeUC;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    case 7:
                                        output.cameraChargeUC = deltaChargeUC;
                                        avgVoltageMV = voltageMV;
                                        break;
                                    default:
                                        android.util.Slog.w(TAG, "Ignoring consumer " + consumer.name + " of unknown type " + type);
                                        avgVoltageMV = voltageMV;
                                        break;
                                }
                            }
                        } else {
                            avgVoltageMV = i3;
                        }
                    } else {
                        avgVoltageMV = i3;
                    }
                }
                i4 = i2 + 1;
                energyConsumerSnapshot = this;
                i3 = avgVoltageMV;
                length = i;
                energyConsumer = null;
                energyConsumerResultArr = ecrs;
            }
            return output;
        }
        return null;
    }

    private android.util.SparseLongArray updateAndGetDeltaForTypeOther(android.hardware.power.stats.EnergyConsumer consumerInfo, android.hardware.power.stats.EnergyConsumerAttribution[] newAttributions, int avgVoltageMV) {
        android.hardware.power.stats.EnergyConsumerAttribution[] newAttributions2;
        android.hardware.power.stats.EnergyConsumerAttribution[] newAttributions3;
        android.util.SparseLongArray uidOldEnergyMap;
        if (consumerInfo.type != 0) {
            return null;
        }
        int i = 0;
        if (newAttributions != null) {
            newAttributions2 = newAttributions;
        } else {
            newAttributions2 = new android.hardware.power.stats.EnergyConsumerAttribution[0];
        }
        android.util.SparseLongArray uidOldEnergyMap2 = this.mAttributionSnapshots.get(consumerInfo.id, null);
        if (uidOldEnergyMap2 == null) {
            android.util.SparseLongArray uidOldEnergyMap3 = new android.util.SparseLongArray(newAttributions2.length);
            this.mAttributionSnapshots.put(consumerInfo.id, uidOldEnergyMap3);
            int length = newAttributions2.length;
            while (i < length) {
                android.hardware.power.stats.EnergyConsumerAttribution newAttribution = newAttributions2[i];
                uidOldEnergyMap3.put(newAttribution.uid, newAttribution.energyUWs);
                i++;
            }
            return null;
        }
        android.util.SparseLongArray uidChargeDeltas = new android.util.SparseLongArray();
        int length2 = newAttributions2.length;
        while (i < length2) {
            android.hardware.power.stats.EnergyConsumerAttribution newAttribution2 = newAttributions2[i];
            int uid = newAttribution2.uid;
            long newEnergyUJ = newAttribution2.energyUWs;
            long oldEnergyUJ = uidOldEnergyMap2.get(uid, 0L);
            uidOldEnergyMap2.put(uid, newEnergyUJ);
            if (oldEnergyUJ < 0) {
                newAttributions3 = newAttributions2;
                uidOldEnergyMap = uidOldEnergyMap2;
            } else if (newEnergyUJ == oldEnergyUJ) {
                newAttributions3 = newAttributions2;
                uidOldEnergyMap = uidOldEnergyMap2;
            } else {
                newAttributions3 = newAttributions2;
                uidOldEnergyMap = uidOldEnergyMap2;
                long deltaUJ = newEnergyUJ - oldEnergyUJ;
                if (deltaUJ < 0 || avgVoltageMV <= 0) {
                    android.util.Slog.e(TAG, "EnergyConsumer " + consumerInfo.name + ": new energy (" + newEnergyUJ + ") but old energy (" + oldEnergyUJ + "). Average voltage (" + avgVoltageMV + ")Skipping. ");
                } else {
                    long deltaChargeUC = calculateChargeConsumedUC(deltaUJ, avgVoltageMV);
                    uidChargeDeltas.put(uid, deltaChargeUC);
                }
            }
            i++;
            uidOldEnergyMap2 = uidOldEnergyMap;
            newAttributions2 = newAttributions3;
        }
        return uidChargeDeltas;
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("Energy consumer snapshot");
        pw.println("List of EnergyConsumers:");
        for (int i = 0; i < this.mEnergyConsumers.size(); i++) {
            int id = this.mEnergyConsumers.keyAt(i);
            android.hardware.power.stats.EnergyConsumer consumer = this.mEnergyConsumers.valueAt(i);
            pw.println(java.lang.String.format("    Consumer %d is {id=%d, ordinal=%d, type=%d, name=%s}", java.lang.Integer.valueOf(id), java.lang.Integer.valueOf(consumer.id), java.lang.Integer.valueOf(consumer.ordinal), java.lang.Byte.valueOf(consumer.type), consumer.name));
        }
        pw.println("Map of consumerIds to energy (in microjoules):");
        for (int i2 = 0; i2 < this.mEnergyConsumerSnapshots.size(); i2++) {
            int id2 = this.mEnergyConsumerSnapshots.keyAt(i2);
            long energyUJ = this.mEnergyConsumerSnapshots.valueAt(i2);
            long voltageMV = this.mVoltageSnapshots.valueAt(i2);
            pw.println(java.lang.String.format("    Consumer %d has energy %d uJ at %d mV", java.lang.Integer.valueOf(id2), java.lang.Long.valueOf(energyUJ), java.lang.Long.valueOf(voltageMV)));
        }
        pw.println("List of the " + this.mNumOtherOrdinals + " OTHER EnergyConsumers:");
        pw.println("    " + this.mAttributionSnapshots);
        pw.println();
    }

    public java.lang.String[] getOtherOrdinalNames() {
        java.lang.String[] names = new java.lang.String[this.mNumOtherOrdinals];
        int consumerIndex = 0;
        int size = this.mEnergyConsumers.size();
        for (int idx = 0; idx < size; idx++) {
            android.hardware.power.stats.EnergyConsumer consumer = this.mEnergyConsumers.valueAt(idx);
            if (consumer.type == 0) {
                names[consumerIndex] = sanitizeCustomBucketName(consumer.name);
                consumerIndex++;
            }
        }
        return names;
    }

    private java.lang.String sanitizeCustomBucketName(java.lang.String bucketName) {
        if (bucketName == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(bucketName.length());
        for (char c : bucketName.toCharArray()) {
            if (java.lang.Character.isWhitespace(c)) {
                sb.append(' ');
            } else if (java.lang.Character.isISOControl(c)) {
                sb.append('_');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int calculateNumOrdinals(int type, android.util.SparseArray<android.hardware.power.stats.EnergyConsumer> idToConsumer) {
        if (idToConsumer == null) {
            return 0;
        }
        int numOrdinals = 0;
        int size = idToConsumer.size();
        for (int idx = 0; idx < size; idx++) {
            android.hardware.power.stats.EnergyConsumer consumer = idToConsumer.valueAt(idx);
            if (consumer.type == type) {
                numOrdinals++;
            }
        }
        return numOrdinals;
    }

    private long calculateChargeConsumedUC(long deltaEnergyUJ, int avgVoltageMV) {
        return ((1000 * deltaEnergyUJ) + ((long) (avgVoltageMV / 2))) / ((long) avgVoltageMV);
    }
}
