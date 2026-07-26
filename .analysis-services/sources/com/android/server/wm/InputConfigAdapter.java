package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class InputConfigAdapter {
    private static final com.android.server.wm.InputConfigAdapter.FlagMapping[] INPUT_FEATURE_TO_CONFIG_MAP = {new com.android.server.wm.InputConfigAdapter.FlagMapping(1, 1, false), new com.android.server.wm.InputConfigAdapter.FlagMapping(2, 2048, false), new com.android.server.wm.InputConfigAdapter.FlagMapping(4, 16384, false), new com.android.server.wm.InputConfigAdapter.FlagMapping(8, 262144, false)};
    private static final int INPUT_FEATURE_TO_CONFIG_MASK = computeMask(INPUT_FEATURE_TO_CONFIG_MAP);
    private static final com.android.server.wm.InputConfigAdapter.FlagMapping[] LAYOUT_PARAM_FLAG_TO_CONFIG_MAP = {new com.android.server.wm.InputConfigAdapter.FlagMapping(16, 8, false), new com.android.server.wm.InputConfigAdapter.FlagMapping(8388608, 16, true), new com.android.server.wm.InputConfigAdapter.FlagMapping(262144, 512, false), new com.android.server.wm.InputConfigAdapter.FlagMapping(536870912, 1024, false)};
    private static final int LAYOUT_PARAM_FLAG_TO_CONFIG_MASK = computeMask(LAYOUT_PARAM_FLAG_TO_CONFIG_MAP);

    private InputConfigAdapter() {
    }

    private static class FlagMapping {
        final int mFlag;
        final int mInputConfig;
        final boolean mInverted;

        FlagMapping(int flag, int inputConfig, boolean inverted) {
            this.mFlag = flag;
            this.mInputConfig = inputConfig;
            this.mInverted = inverted;
        }
    }

    static int getMask() {
        return LAYOUT_PARAM_FLAG_TO_CONFIG_MASK | INPUT_FEATURE_TO_CONFIG_MASK | 64;
    }

    static int getInputConfigFromWindowParams(int type, int flags, int inputFeatures) {
        return (type == 2013 ? 64 : 0) | applyMapping(flags, LAYOUT_PARAM_FLAG_TO_CONFIG_MAP) | applyMapping(inputFeatures, INPUT_FEATURE_TO_CONFIG_MAP);
    }

    private static int applyMapping(int flags, com.android.server.wm.InputConfigAdapter.FlagMapping[] flagToConfigMap) {
        int inputConfig = 0;
        for (com.android.server.wm.InputConfigAdapter.FlagMapping mapping : flagToConfigMap) {
            boolean flagSet = (mapping.mFlag & flags) != 0;
            if (flagSet != mapping.mInverted) {
                inputConfig |= mapping.mInputConfig;
            }
        }
        return inputConfig;
    }

    private static int computeMask(com.android.server.wm.InputConfigAdapter.FlagMapping[] flagToConfigMap) {
        int mask = 0;
        for (com.android.server.wm.InputConfigAdapter.FlagMapping mapping : flagToConfigMap) {
            mask |= mapping.mInputConfig;
        }
        return mask;
    }
}
