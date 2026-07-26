package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public class RuleBinaryParser implements com.android.server.integrity.parser.RuleParser {
    @Override // com.android.server.integrity.parser.RuleParser
    public java.util.List<android.content.integrity.Rule> parse(byte[] ruleBytes) throws com.android.server.integrity.parser.RuleParseException {
        return parse(com.android.server.integrity.parser.RandomAccessObject.ofBytes(ruleBytes), java.util.Collections.emptyList());
    }

    @Override // com.android.server.integrity.parser.RuleParser
    public java.util.List<android.content.integrity.Rule> parse(com.android.server.integrity.parser.RandomAccessObject randomAccessObject, java.util.List<com.android.server.integrity.parser.RuleIndexRange> indexRanges) throws com.android.server.integrity.parser.RuleParseException {
        try {
            com.android.server.integrity.parser.RandomAccessInputStream randomAccessInputStream = new com.android.server.integrity.parser.RandomAccessInputStream(randomAccessObject);
            try {
                java.util.List<android.content.integrity.Rule> rules = parseRules(randomAccessInputStream, indexRanges);
                randomAccessInputStream.close();
                return rules;
            } finally {
            }
        } catch (java.lang.Exception e) {
            throw new com.android.server.integrity.parser.RuleParseException(e.getMessage(), e);
        }
    }

    private java.util.List<android.content.integrity.Rule> parseRules(com.android.server.integrity.parser.RandomAccessInputStream randomAccessInputStream, java.util.List<com.android.server.integrity.parser.RuleIndexRange> indexRanges) throws java.io.IOException {
        randomAccessInputStream.skip(1L);
        if (indexRanges.isEmpty()) {
            return parseAllRules(randomAccessInputStream);
        }
        return parseIndexedRules(randomAccessInputStream, indexRanges);
    }

    private java.util.List<android.content.integrity.Rule> parseAllRules(com.android.server.integrity.parser.RandomAccessInputStream randomAccessInputStream) throws java.io.IOException {
        java.util.List<android.content.integrity.Rule> parsedRules = new java.util.ArrayList<>();
        com.android.server.integrity.model.BitInputStream inputStream = new com.android.server.integrity.model.BitInputStream(new java.io.BufferedInputStream(randomAccessInputStream));
        while (inputStream.hasNext()) {
            if (inputStream.getNext(1) == 1) {
                parsedRules.add(parseRule(inputStream));
            }
        }
        return parsedRules;
    }

    private java.util.List<android.content.integrity.Rule> parseIndexedRules(com.android.server.integrity.parser.RandomAccessInputStream randomAccessInputStream, java.util.List<com.android.server.integrity.parser.RuleIndexRange> indexRanges) throws java.io.IOException {
        java.util.List<android.content.integrity.Rule> parsedRules = new java.util.ArrayList<>();
        for (com.android.server.integrity.parser.RuleIndexRange range : indexRanges) {
            randomAccessInputStream.seek(range.getStartIndex());
            com.android.server.integrity.model.BitInputStream inputStream = new com.android.server.integrity.model.BitInputStream(new java.io.BufferedInputStream(new com.android.server.integrity.parser.LimitInputStream(randomAccessInputStream, range.getEndIndex() - range.getStartIndex())));
            while (inputStream.hasNext()) {
                if (inputStream.getNext(1) == 1) {
                    parsedRules.add(parseRule(inputStream));
                }
            }
        }
        return parsedRules;
    }

    private android.content.integrity.Rule parseRule(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        android.content.integrity.IntegrityFormula formula = parseFormula(bitInputStream);
        int effect = bitInputStream.getNext(3);
        if (bitInputStream.getNext(1) != 1) {
            throw new java.lang.IllegalArgumentException("A rule must end with a '1' bit.");
        }
        return new android.content.integrity.Rule(formula, effect);
    }

    private android.content.integrity.IntegrityFormula parseFormula(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        int separator = bitInputStream.getNext(3);
        switch (separator) {
            case 0:
                return parseAtomicFormula(bitInputStream);
            case 1:
                return parseCompoundFormula(bitInputStream);
            case 2:
                return null;
            case 3:
                return new android.content.integrity.InstallerAllowedByManifestFormula();
            default:
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Unknown formula separator: %s", java.lang.Integer.valueOf(separator)));
        }
    }

    private android.content.integrity.CompoundFormula parseCompoundFormula(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        int connector = bitInputStream.getNext(2);
        java.util.List<android.content.integrity.IntegrityFormula> formulas = new java.util.ArrayList<>();
        android.content.integrity.IntegrityFormula parsedFormula = parseFormula(bitInputStream);
        while (parsedFormula != null) {
            formulas.add(parsedFormula);
            parsedFormula = parseFormula(bitInputStream);
        }
        return new android.content.integrity.CompoundFormula(connector, formulas);
    }

    private android.content.integrity.AtomicFormula parseAtomicFormula(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        int key = bitInputStream.getNext(4);
        int operator = bitInputStream.getNext(3);
        switch (key) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 7:
            case 8:
                boolean isHashedValue = bitInputStream.getNext(1) == 1;
                int valueSize = bitInputStream.getNext(8);
                java.lang.String stringValue = com.android.server.integrity.parser.BinaryFileOperations.getStringValue(bitInputStream, valueSize, isHashedValue);
                return new android.content.integrity.AtomicFormula.StringAtomicFormula(key, stringValue, isHashedValue);
            case 4:
                long upper = com.android.server.integrity.parser.BinaryFileOperations.getIntValue(bitInputStream);
                long lower = com.android.server.integrity.parser.BinaryFileOperations.getIntValue(bitInputStream);
                long longValue = (upper << 32) | lower;
                return new android.content.integrity.AtomicFormula.LongAtomicFormula(key, operator, longValue);
            case 5:
            case 6:
                boolean booleanValue = com.android.server.integrity.parser.BinaryFileOperations.getBooleanValue(bitInputStream);
                return new android.content.integrity.AtomicFormula.BooleanAtomicFormula(key, booleanValue);
            default:
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Unknown key: %d", java.lang.Integer.valueOf(key)));
        }
    }
}
