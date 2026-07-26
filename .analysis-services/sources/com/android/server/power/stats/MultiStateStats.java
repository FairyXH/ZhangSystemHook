package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class MultiStateStats {
    public static final int STATE_DOES_NOT_EXIST = -1;
    private static final java.lang.String TAG = "MultiStateStats";
    private static final java.lang.String XML_TAG_STATS = "stats";
    private int mCompositeState;
    private final com.android.internal.os.LongArrayMultiStateCounter mCounter;
    private final com.android.server.power.stats.MultiStateStats.Factory mFactory;
    private boolean mTracking;

    public static class States {
        final java.lang.String[] mLabels;
        final java.lang.String mName;
        final boolean mTracked;

        public States(java.lang.String name, boolean tracked, java.lang.String... labels) {
            this.mName = name;
            this.mTracked = tracked;
            this.mLabels = labels;
        }

        public boolean isTracked() {
            return this.mTracked;
        }

        public java.lang.String getName() {
            return this.mName;
        }

        public java.lang.String[] getLabels() {
            return this.mLabels;
        }

        public static int findTrackedStateByName(com.android.server.power.stats.MultiStateStats.States[] states, java.lang.String name) {
            for (int i = 0; i < states.length; i++) {
                if (states[i].getName().equals(name)) {
                    return i;
                }
            }
            return -1;
        }

        public static void forEachTrackedStateCombination(com.android.server.power.stats.MultiStateStats.States[] states, java.util.function.Consumer<int[]> consumer) {
            forEachTrackedStateCombination(consumer, states, new int[states.length], 0);
        }

        private static void forEachTrackedStateCombination(java.util.function.Consumer<int[]> consumer, com.android.server.power.stats.MultiStateStats.States[] states, int[] statesValues, int stateIndex) {
            if (stateIndex < statesValues.length) {
                if (!states[stateIndex].mTracked) {
                    forEachTrackedStateCombination(consumer, states, statesValues, stateIndex + 1);
                    return;
                }
                for (int i = 0; i < states[stateIndex].mLabels.length; i++) {
                    statesValues[stateIndex] = i;
                    forEachTrackedStateCombination(consumer, states, statesValues, stateIndex + 1);
                }
                return;
            }
            consumer.accept(statesValues);
        }
    }

    public static class Factory {
        private static final int INVALID_SERIAL_STATE = -1;
        final int[] mCompositeToSerialState;
        final int mDimensionCount;
        final int mSerialStateCount;
        private final int[] mStateBitFieldMasks;
        private final short[] mStateBitFieldShifts;
        final com.android.server.power.stats.MultiStateStats.States[] mStates;

        public Factory(int dimensionCount, com.android.server.power.stats.MultiStateStats.States... states) {
            this.mDimensionCount = dimensionCount;
            this.mStates = states;
            int serialStateCount = 1;
            for (com.android.server.power.stats.MultiStateStats.States state : this.mStates) {
                if (state.mTracked) {
                    serialStateCount *= state.mLabels.length;
                }
            }
            this.mSerialStateCount = serialStateCount;
            this.mStateBitFieldMasks = new int[this.mStates.length];
            this.mStateBitFieldShifts = new short[this.mStates.length];
            int shift = 0;
            for (int i = 0; i < this.mStates.length; i++) {
                this.mStateBitFieldShifts[i] = (short) shift;
                if (this.mStates[i].mLabels.length < 2) {
                    throw new java.lang.IllegalArgumentException("Invalid state: " + java.util.Arrays.toString(this.mStates[i].mLabels) + ". Should have at least two values.");
                }
                int max = this.mStates[i].mLabels.length - 1;
                int bitcount = 32 - java.lang.Integer.numberOfLeadingZeros(max);
                this.mStateBitFieldMasks[i] = ((1 << bitcount) - 1) << shift;
                shift += bitcount;
            }
            if (shift >= 31) {
                throw new java.lang.IllegalArgumentException("Too many states: " + shift + " bits are required to represent the composite state, but only 31 are available");
            }
            int trackedMask = -1;
            for (int state2 = 0; state2 < this.mStates.length; state2++) {
                if (!this.mStates[state2].mTracked) {
                    trackedMask &= ~this.mStateBitFieldMasks[state2];
                }
            }
            int state3 = 1 << shift;
            this.mCompositeToSerialState = new int[state3];
            java.util.Arrays.fill(this.mCompositeToSerialState, -1);
            int nextSerialState = 0;
            for (int composite = 0; composite < this.mCompositeToSerialState.length; composite++) {
                if (isValidCompositeState(composite)) {
                    int baseComposite = composite & trackedMask;
                    if (this.mCompositeToSerialState[baseComposite] != -1) {
                        this.mCompositeToSerialState[composite] = this.mCompositeToSerialState[baseComposite];
                    } else {
                        this.mCompositeToSerialState[composite] = nextSerialState;
                        nextSerialState++;
                    }
                }
            }
        }

        private boolean isValidCompositeState(int composite) {
            for (int stateIndex = 0; stateIndex < this.mStates.length; stateIndex++) {
                int state = extractStateFromComposite(composite, stateIndex);
                if (state >= this.mStates[stateIndex].mLabels.length) {
                    return false;
                }
            }
            return true;
        }

        private int extractStateFromComposite(int compositeState, int stateIndex) {
            return (this.mStateBitFieldMasks[stateIndex] & compositeState) >>> this.mStateBitFieldShifts[stateIndex];
        }

        int setStateInComposite(int baseCompositeState, int stateIndex, int value) {
            return ((~this.mStateBitFieldMasks[stateIndex]) & baseCompositeState) | (value << this.mStateBitFieldShifts[stateIndex]);
        }

        int setStateInComposite(int compositeState, java.lang.String stateName, java.lang.String stateLabel) {
            for (int stateIndex = 0; stateIndex < this.mStates.length; stateIndex++) {
                com.android.server.power.stats.MultiStateStats.States stateConfig = this.mStates[stateIndex];
                if (stateConfig.mName.equals(stateName)) {
                    for (int state = 0; state < stateConfig.mLabels.length; state++) {
                        if (stateConfig.mLabels[state].equals(stateLabel)) {
                            return setStateInComposite(compositeState, stateIndex, state);
                        }
                    }
                    android.util.Slog.e(com.android.server.power.stats.MultiStateStats.TAG, "Unexpected label '" + stateLabel + "' for state: " + stateName);
                    return -1;
                }
            }
            android.util.Slog.e(com.android.server.power.stats.MultiStateStats.TAG, "Unsupported state: " + stateName);
            return -1;
        }

        public com.android.server.power.stats.MultiStateStats create() {
            return new com.android.server.power.stats.MultiStateStats(this, this.mDimensionCount);
        }

        public int getSerialStateCount() {
            return this.mSerialStateCount;
        }

        public int getSerialState(int[] states) {
            com.android.internal.util.Preconditions.checkArgument(states.length == this.mStates.length);
            int compositeState = 0;
            for (int i = 0; i < states.length; i++) {
                compositeState = setStateInComposite(compositeState, i, states[i]);
            }
            int serialState = this.mCompositeToSerialState[compositeState];
            if (serialState == -1) {
                throw new java.lang.IllegalArgumentException("State values out of bounds: " + java.util.Arrays.toString(states));
            }
            return serialState;
        }

        int getSerialState(int compositeState) {
            return this.mCompositeToSerialState[compositeState];
        }
    }

    public MultiStateStats(com.android.server.power.stats.MultiStateStats.Factory factory, int dimensionCount) {
        this.mFactory = factory;
        this.mCounter = new com.android.internal.os.LongArrayMultiStateCounter(factory.mSerialStateCount, dimensionCount);
    }

    public int getDimensionCount() {
        return this.mFactory.mDimensionCount;
    }

    public com.android.server.power.stats.MultiStateStats.States[] getStates() {
        return this.mFactory.mStates;
    }

    public void copyStatesFrom(com.android.server.power.stats.MultiStateStats otherStats) {
        this.mCounter.copyStatesFrom(otherStats.mCounter);
    }

    public void setState(int stateIndex, int state, long timestampMs) {
        if (!this.mTracking) {
            this.mCounter.updateValues(new long[this.mCounter.getArrayLength()], timestampMs);
            this.mTracking = true;
        }
        this.mCompositeState = this.mFactory.setStateInComposite(this.mCompositeState, stateIndex, state);
        this.mCounter.setState(this.mFactory.mCompositeToSerialState[this.mCompositeState], timestampMs);
    }

    public void increment(long[] values, long timestampMs) {
        this.mCounter.incrementValues(values, timestampMs);
        this.mTracking = true;
    }

    public void getStats(long[] outValues, int[] states) {
        if (this.mCounter != null) {
            this.mCounter.getCounts(outValues, this.mFactory.getSerialState(states));
        } else {
            android.util.Slog.e(TAG, "mCounter is null");
        }
    }

    public void setStats(int[] states, long[] values) {
        this.mCounter.setValues(this.mFactory.getSerialState(states), values);
    }

    public void reset() {
        this.mCounter.reset();
        this.mTracking = false;
    }

    public void writeXml(final com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        final long[] tmpArray = new long[this.mCounter.getArrayLength()];
        try {
            com.android.server.power.stats.MultiStateStats.States.forEachTrackedStateCombination(this.mFactory.mStates, new java.util.function.Consumer() { // from class: com.android.server.power.stats.MultiStateStats$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$writeXml$0(serializer, tmpArray, (int[]) obj);
                }
            });
        } catch (java.lang.RuntimeException e) {
            if (e.getCause() instanceof java.io.IOException) {
                throw ((java.io.IOException) e.getCause());
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$writeXml$0(com.android.modules.utils.TypedXmlSerializer serializer, long[] tmpArray, int[] states) {
        try {
            writeXmlForStates(serializer, states, tmpArray);
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private void writeXmlForStates(com.android.modules.utils.TypedXmlSerializer serializer, int[] states, long[] values) throws java.io.IOException {
        this.mCounter.getCounts(values, this.mFactory.getSerialState(states));
        boolean nonZero = false;
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            long value = values[i];
            if (value == 0) {
                i++;
            } else {
                nonZero = true;
                break;
            }
        }
        if (!nonZero) {
            return;
        }
        serializer.startTag((java.lang.String) null, XML_TAG_STATS);
        for (int i2 = 0; i2 < states.length; i2++) {
            if (this.mFactory.mStates[i2].mTracked && states[i2] != 0) {
                serializer.attribute((java.lang.String) null, this.mFactory.mStates[i2].mName, this.mFactory.mStates[i2].mLabels[states[i2]]);
            }
        }
        for (int i3 = 0; i3 < values.length; i3++) {
            if (values[i3] != 0) {
                serializer.attributeLong((java.lang.String) null, "_" + i3, values[i3]);
            }
        }
        serializer.endTag((java.lang.String) null, XML_TAG_STATS);
    }

    public boolean readFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String outerTag = parser.getName();
        long[] tmpArray = new long[this.mCounter.getArrayLength()];
        int eventType = parser.getEventType();
        while (eventType != 1 && (eventType != 3 || !parser.getName().equals(outerTag))) {
            if (eventType == 2 && parser.getName().equals(XML_TAG_STATS)) {
                java.util.Arrays.fill(tmpArray, 0L);
                int compositeState = 0;
                int attributeCount = parser.getAttributeCount();
                for (int i = 0; i < attributeCount; i++) {
                    java.lang.String attributeName = parser.getAttributeName(i);
                    if (attributeName.startsWith("_")) {
                        try {
                            int index = java.lang.Integer.parseInt(attributeName.substring(1));
                            if (index < 0 || index >= tmpArray.length) {
                                android.util.Slog.e(TAG, "State index out of bounds: " + index + " length: " + tmpArray.length);
                                return false;
                            }
                            tmpArray[index] = parser.getAttributeLong(i);
                        } catch (java.lang.NumberFormatException e) {
                            throw new org.xmlpull.v1.XmlPullParserException("Unexpected index syntax: " + attributeName, parser, e);
                        }
                    } else {
                        java.lang.String attributeValue = parser.getAttributeValue(i);
                        compositeState = this.mFactory.setStateInComposite(compositeState, attributeName, attributeValue);
                        if (compositeState == -1) {
                            return false;
                        }
                    }
                }
                this.mCounter.setValues(this.mFactory.getSerialState(compositeState), tmpArray);
            }
            eventType = parser.next();
        }
        return true;
    }

    public java.lang.String toString() {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder();
        final long[] values = new long[this.mCounter.getArrayLength()];
        com.android.server.power.stats.MultiStateStats.States.forEachTrackedStateCombination(this.mFactory.mStates, new java.util.function.Consumer() { // from class: com.android.server.power.stats.MultiStateStats$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$toString$1(values, sb, (int[]) obj);
            }
        });
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$toString$1(long[] values, java.lang.StringBuilder sb, int[] states) {
        this.mCounter.getCounts(values, this.mFactory.getSerialState(states));
        boolean nonZero = false;
        int length = values.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            long value = values[i];
            if (value == 0) {
                i++;
            } else {
                nonZero = true;
                break;
            }
        }
        if (!nonZero) {
            return;
        }
        if (!sb.isEmpty()) {
            sb.append("\n");
        }
        sb.append("(");
        boolean first = true;
        for (int i2 = 0; i2 < states.length; i2++) {
            if (this.mFactory.mStates[i2].mTracked) {
                if (!first) {
                    sb.append(" ");
                }
                first = false;
                sb.append(this.mFactory.mStates[i2].mLabels[states[i2]]);
            }
        }
        sb.append(") ");
        sb.append(java.util.Arrays.toString(values));
    }
}
