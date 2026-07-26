package com.android.server.integrity.serializer;

/* JADX INFO: loaded from: classes2.dex */
public class RuleBinarySerializer implements com.android.server.integrity.serializer.RuleSerializer {
    static final int INDEXED_RULE_SIZE_LIMIT = 100000;
    static final int NONINDEXED_RULE_SIZE_LIMIT = 1000;
    static final int TOTAL_RULE_SIZE_LIMIT = 200000;

    @Override // com.android.server.integrity.serializer.RuleSerializer
    public byte[] serialize(java.util.List<android.content.integrity.Rule> rules, java.util.Optional<java.lang.Integer> formatVersion) throws com.android.server.integrity.serializer.RuleSerializeException {
        try {
            java.io.ByteArrayOutputStream rulesOutputStream = new java.io.ByteArrayOutputStream();
            serialize(rules, formatVersion, rulesOutputStream, new java.io.ByteArrayOutputStream());
            return rulesOutputStream.toByteArray();
        } catch (java.lang.Exception e) {
            throw new com.android.server.integrity.serializer.RuleSerializeException(e.getMessage(), e);
        }
    }

    @Override // com.android.server.integrity.serializer.RuleSerializer
    public void serialize(java.util.List<android.content.integrity.Rule> rules, java.util.Optional<java.lang.Integer> formatVersion, java.io.OutputStream rulesFileOutputStream, java.io.OutputStream indexingFileOutputStream) throws com.android.server.integrity.serializer.RuleSerializeException {
        try {
            if (rules == null) {
                throw new java.lang.IllegalArgumentException("Null rules cannot be serialized.");
            }
            if (rules.size() > TOTAL_RULE_SIZE_LIMIT) {
                throw new java.lang.IllegalArgumentException("Too many rules provided: " + rules.size());
            }
            java.util.Map<java.lang.Integer, java.util.Map<java.lang.String, java.util.List<android.content.integrity.Rule>>> indexedRules = com.android.server.integrity.serializer.RuleIndexingDetailsIdentifier.splitRulesIntoIndexBuckets(rules);
            verifySize(indexedRules.get(1), 100000);
            verifySize(indexedRules.get(2), 100000);
            verifySize(indexedRules.get(0), 1000);
            com.android.server.integrity.model.ByteTrackedOutputStream ruleFileByteTrackedOutputStream = new com.android.server.integrity.model.ByteTrackedOutputStream(rulesFileOutputStream);
            serializeRuleFileMetadata(formatVersion, ruleFileByteTrackedOutputStream);
            java.util.LinkedHashMap<java.lang.String, java.lang.Integer> packageNameIndexes = serializeRuleList(indexedRules.get(1), ruleFileByteTrackedOutputStream);
            java.util.LinkedHashMap<java.lang.String, java.lang.Integer> appCertificateIndexes = serializeRuleList(indexedRules.get(2), ruleFileByteTrackedOutputStream);
            java.util.LinkedHashMap<java.lang.String, java.lang.Integer> unindexedRulesIndexes = serializeRuleList(indexedRules.get(0), ruleFileByteTrackedOutputStream);
            com.android.server.integrity.model.BitOutputStream indexingBitOutputStream = new com.android.server.integrity.model.BitOutputStream(indexingFileOutputStream);
            serializeIndexGroup(packageNameIndexes, indexingBitOutputStream, true);
            serializeIndexGroup(appCertificateIndexes, indexingBitOutputStream, true);
            serializeIndexGroup(unindexedRulesIndexes, indexingBitOutputStream, false);
            indexingBitOutputStream.flush();
        } catch (java.lang.Exception e) {
            throw new com.android.server.integrity.serializer.RuleSerializeException(e.getMessage(), e);
        }
    }

    private void verifySize(java.util.Map<java.lang.String, java.util.List<android.content.integrity.Rule>> ruleListMap, int ruleSizeLimit) {
        int totalRuleCount = ((java.lang.Integer) ruleListMap.values().stream().map(new java.util.function.Function() { // from class: com.android.server.integrity.serializer.RuleBinarySerializer$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((java.util.List) obj).size());
            }
        }).collect(java.util.stream.Collectors.summingInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()))).intValue();
        if (totalRuleCount > ruleSizeLimit) {
            throw new java.lang.IllegalArgumentException("Too many rules provided in the indexing group. Provided " + totalRuleCount + " limit " + ruleSizeLimit);
        }
    }

    private void serializeRuleFileMetadata(java.util.Optional<java.lang.Integer> formatVersion, com.android.server.integrity.model.ByteTrackedOutputStream outputStream) throws java.io.IOException {
        int formatVersionValue = formatVersion.orElse(1).intValue();
        com.android.server.integrity.model.BitOutputStream bitOutputStream = new com.android.server.integrity.model.BitOutputStream(outputStream);
        bitOutputStream.setNext(8, formatVersionValue);
        bitOutputStream.flush();
    }

    private java.util.LinkedHashMap<java.lang.String, java.lang.Integer> serializeRuleList(java.util.Map<java.lang.String, java.util.List<android.content.integrity.Rule>> rulesMap, com.android.server.integrity.model.ByteTrackedOutputStream outputStream) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkArgument(rulesMap != null, "serializeRuleList should never be called with null rule list.");
        com.android.server.integrity.model.BitOutputStream bitOutputStream = new com.android.server.integrity.model.BitOutputStream(outputStream);
        java.util.LinkedHashMap<java.lang.String, java.lang.Integer> indexMapping = new java.util.LinkedHashMap<>();
        indexMapping.put(com.android.server.integrity.model.IndexingFileConstants.START_INDEXING_KEY, java.lang.Integer.valueOf(outputStream.getWrittenBytesCount()));
        java.util.List<java.lang.String> sortedKeys = (java.util.List) rulesMap.keySet().stream().sorted().collect(java.util.stream.Collectors.toList());
        int indexTracker = 0;
        for (java.lang.String key : sortedKeys) {
            if (indexTracker >= 50) {
                indexMapping.put(key, java.lang.Integer.valueOf(outputStream.getWrittenBytesCount()));
                indexTracker = 0;
            }
            for (android.content.integrity.Rule rule : rulesMap.get(key)) {
                serializeRule(rule, bitOutputStream);
                bitOutputStream.flush();
                indexTracker++;
            }
        }
        indexMapping.put(com.android.server.integrity.model.IndexingFileConstants.END_INDEXING_KEY, java.lang.Integer.valueOf(outputStream.getWrittenBytesCount()));
        return indexMapping;
    }

    private void serializeRule(android.content.integrity.Rule rule, com.android.server.integrity.model.BitOutputStream bitOutputStream) throws java.io.IOException {
        if (rule == null) {
            throw new java.lang.IllegalArgumentException("Null rule can not be serialized");
        }
        bitOutputStream.setNext();
        serializeFormula(rule.getFormula(), bitOutputStream);
        bitOutputStream.setNext(3, rule.getEffect());
        bitOutputStream.setNext();
    }

    private void serializeFormula(android.content.integrity.IntegrityFormula formula, com.android.server.integrity.model.BitOutputStream bitOutputStream) throws java.io.IOException {
        if (formula instanceof android.content.integrity.AtomicFormula) {
            serializeAtomicFormula((android.content.integrity.AtomicFormula) formula, bitOutputStream);
        } else if (formula instanceof android.content.integrity.CompoundFormula) {
            serializeCompoundFormula((android.content.integrity.CompoundFormula) formula, bitOutputStream);
        } else {
            if (formula instanceof android.content.integrity.InstallerAllowedByManifestFormula) {
                bitOutputStream.setNext(3, 3);
                return;
            }
            throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid formula type: %s", formula.getClass()));
        }
    }

    private void serializeCompoundFormula(android.content.integrity.CompoundFormula compoundFormula, com.android.server.integrity.model.BitOutputStream bitOutputStream) throws java.io.IOException {
        if (compoundFormula == null) {
            throw new java.lang.IllegalArgumentException("Null compound formula can not be serialized");
        }
        bitOutputStream.setNext(3, 1);
        bitOutputStream.setNext(2, compoundFormula.getConnector());
        for (android.content.integrity.IntegrityFormula formula : compoundFormula.getFormulas()) {
            serializeFormula(formula, bitOutputStream);
        }
        bitOutputStream.setNext(3, 2);
    }

    private void serializeAtomicFormula(android.content.integrity.AtomicFormula atomicFormula, com.android.server.integrity.model.BitOutputStream bitOutputStream) throws java.io.IOException {
        if (atomicFormula == null) {
            throw new java.lang.IllegalArgumentException("Null atomic formula can not be serialized");
        }
        bitOutputStream.setNext(3, 0);
        bitOutputStream.setNext(4, atomicFormula.getKey());
        if (atomicFormula.getTag() == 1) {
            android.content.integrity.AtomicFormula.StringAtomicFormula stringAtomicFormula = (android.content.integrity.AtomicFormula.StringAtomicFormula) atomicFormula;
            bitOutputStream.setNext(3, 0);
            serializeStringValue(stringAtomicFormula.getValue(), stringAtomicFormula.getIsHashedValue().booleanValue(), bitOutputStream);
        } else {
            if (atomicFormula.getTag() != 2) {
                if (atomicFormula.getTag() == 3) {
                    android.content.integrity.AtomicFormula.BooleanAtomicFormula booleanAtomicFormula = (android.content.integrity.AtomicFormula.BooleanAtomicFormula) atomicFormula;
                    bitOutputStream.setNext(3, 0);
                    serializeBooleanValue(booleanAtomicFormula.getValue().booleanValue(), bitOutputStream);
                    return;
                }
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Invalid atomic formula type: %s", atomicFormula.getClass()));
            }
            android.content.integrity.AtomicFormula.LongAtomicFormula longAtomicFormula = (android.content.integrity.AtomicFormula.LongAtomicFormula) atomicFormula;
            bitOutputStream.setNext(3, longAtomicFormula.getOperator().intValue());
            long value = longAtomicFormula.getValue().longValue();
            serializeIntValue((int) (value >>> 32), bitOutputStream);
            serializeIntValue((int) value, bitOutputStream);
        }
    }

    private void serializeIndexGroup(java.util.LinkedHashMap<java.lang.String, java.lang.Integer> indexes, com.android.server.integrity.model.BitOutputStream bitOutputStream, boolean isIndexed) throws java.io.IOException {
        serializeStringValue(com.android.server.integrity.model.IndexingFileConstants.START_INDEXING_KEY, false, bitOutputStream);
        serializeIntValue(indexes.get(com.android.server.integrity.model.IndexingFileConstants.START_INDEXING_KEY).intValue(), bitOutputStream);
        if (isIndexed) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Integer> entry : indexes.entrySet()) {
                if (!entry.getKey().equals(com.android.server.integrity.model.IndexingFileConstants.START_INDEXING_KEY) && !entry.getKey().equals(com.android.server.integrity.model.IndexingFileConstants.END_INDEXING_KEY)) {
                    serializeStringValue(entry.getKey(), false, bitOutputStream);
                    serializeIntValue(entry.getValue().intValue(), bitOutputStream);
                }
            }
        }
        serializeStringValue(com.android.server.integrity.model.IndexingFileConstants.END_INDEXING_KEY, false, bitOutputStream);
        serializeIntValue(indexes.get(com.android.server.integrity.model.IndexingFileConstants.END_INDEXING_KEY).intValue(), bitOutputStream);
    }

    private void serializeStringValue(java.lang.String value, boolean isHashedValue, com.android.server.integrity.model.BitOutputStream bitOutputStream) throws java.io.IOException {
        if (value == null) {
            throw new java.lang.IllegalArgumentException("String value can not be null.");
        }
        byte[] valueBytes = getBytesForString(value, isHashedValue);
        bitOutputStream.setNext(isHashedValue);
        bitOutputStream.setNext(8, valueBytes.length);
        for (byte valueByte : valueBytes) {
            bitOutputStream.setNext(8, valueByte);
        }
    }

    private void serializeIntValue(int value, com.android.server.integrity.model.BitOutputStream bitOutputStream) throws java.io.IOException {
        bitOutputStream.setNext(32, value);
    }

    private void serializeBooleanValue(boolean value, com.android.server.integrity.model.BitOutputStream bitOutputStream) throws java.io.IOException {
        bitOutputStream.setNext(value);
    }

    private static byte[] getBytesForString(java.lang.String value, boolean isHashedValue) {
        if (!isHashedValue) {
            return value.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        return android.content.integrity.IntegrityUtils.getBytesFromHexDigest(value);
    }
}
