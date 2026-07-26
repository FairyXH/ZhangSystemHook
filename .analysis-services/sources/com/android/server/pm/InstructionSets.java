package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class InstructionSets {
    private static final java.lang.String PREFERRED_INSTRUCTION_SET = dalvik.system.VMRuntime.getInstructionSet(android.os.Build.SUPPORTED_ABIS[0]);

    public static java.lang.String[] getAppDexInstructionSets(java.lang.String primaryCpuAbi, java.lang.String secondaryCpuAbi) {
        if (primaryCpuAbi != null) {
            if (secondaryCpuAbi != null) {
                return new java.lang.String[]{dalvik.system.VMRuntime.getInstructionSet(primaryCpuAbi), dalvik.system.VMRuntime.getInstructionSet(secondaryCpuAbi)};
            }
            return new java.lang.String[]{dalvik.system.VMRuntime.getInstructionSet(primaryCpuAbi)};
        }
        return new java.lang.String[]{getPreferredInstructionSet()};
    }

    public static java.lang.String getPreferredInstructionSet() {
        return PREFERRED_INSTRUCTION_SET;
    }

    public static java.lang.String getDexCodeInstructionSet(java.lang.String sharedLibraryIsa) {
        java.lang.String dexCodeIsa = android.os.SystemProperties.get("ro.dalvik.vm.isa." + sharedLibraryIsa);
        return android.text.TextUtils.isEmpty(dexCodeIsa) ? sharedLibraryIsa : dexCodeIsa;
    }

    public static java.lang.String[] getDexCodeInstructionSets(java.lang.String[] instructionSets) {
        android.util.ArraySet<java.lang.String> dexCodeInstructionSets = new android.util.ArraySet<>(instructionSets.length);
        for (java.lang.String instructionSet : instructionSets) {
            dexCodeInstructionSets.add(getDexCodeInstructionSet(instructionSet));
        }
        return (java.lang.String[]) dexCodeInstructionSets.toArray(new java.lang.String[dexCodeInstructionSets.size()]);
    }

    public static java.lang.String[] getAllDexCodeInstructionSets() {
        java.lang.String[] supportedInstructionSets = new java.lang.String[android.os.Build.SUPPORTED_ABIS.length];
        for (int i = 0; i < supportedInstructionSets.length; i++) {
            java.lang.String abi = android.os.Build.SUPPORTED_ABIS[i];
            supportedInstructionSets[i] = dalvik.system.VMRuntime.getInstructionSet(abi);
        }
        return getDexCodeInstructionSets(supportedInstructionSets);
    }

    public static java.util.List<java.lang.String> getAllInstructionSets() {
        java.lang.String[] allAbis = android.os.Build.SUPPORTED_ABIS;
        java.util.List<java.lang.String> allInstructionSets = new java.util.ArrayList<>(allAbis.length);
        for (java.lang.String abi : allAbis) {
            java.lang.String instructionSet = dalvik.system.VMRuntime.getInstructionSet(abi);
            if (!allInstructionSets.contains(instructionSet)) {
                allInstructionSets.add(instructionSet);
            }
        }
        return allInstructionSets;
    }

    public static java.lang.String getPrimaryInstructionSet(com.android.server.pm.PackageAbiHelper.Abis abis) {
        if (abis.primary == null) {
            return getPreferredInstructionSet();
        }
        return dalvik.system.VMRuntime.getInstructionSet(abis.primary);
    }
}
