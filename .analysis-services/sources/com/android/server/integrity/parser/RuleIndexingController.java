package com.android.server.integrity.parser;

/* JADX INFO: loaded from: classes2.dex */
public class RuleIndexingController {
    private static java.util.LinkedHashMap<java.lang.String, java.lang.Integer> sAppCertificateBasedIndexes;
    private static java.util.LinkedHashMap<java.lang.String, java.lang.Integer> sPackageNameBasedIndexes;
    private static java.util.LinkedHashMap<java.lang.String, java.lang.Integer> sUnindexedRuleIndexes;

    public RuleIndexingController(java.io.InputStream inputStream) throws java.io.IOException {
        com.android.server.integrity.model.BitInputStream bitInputStream = new com.android.server.integrity.model.BitInputStream(inputStream);
        sPackageNameBasedIndexes = getNextIndexGroup(bitInputStream);
        sAppCertificateBasedIndexes = getNextIndexGroup(bitInputStream);
        sUnindexedRuleIndexes = getNextIndexGroup(bitInputStream);
    }

    public java.util.List<com.android.server.integrity.parser.RuleIndexRange> identifyRulesToEvaluate(android.content.integrity.AppInstallMetadata appInstallMetadata) {
        java.util.List<com.android.server.integrity.parser.RuleIndexRange> indexRanges = new java.util.ArrayList<>();
        indexRanges.add(searchIndexingKeysRangeContainingKey(sPackageNameBasedIndexes, appInstallMetadata.getPackageName()));
        for (java.lang.String appCertificate : appInstallMetadata.getAppCertificates()) {
            indexRanges.add(searchIndexingKeysRangeContainingKey(sAppCertificateBasedIndexes, appCertificate));
        }
        indexRanges.add(new com.android.server.integrity.parser.RuleIndexRange(sUnindexedRuleIndexes.get(com.android.server.integrity.model.IndexingFileConstants.START_INDEXING_KEY).intValue(), sUnindexedRuleIndexes.get(com.android.server.integrity.model.IndexingFileConstants.END_INDEXING_KEY).intValue()));
        return indexRanges;
    }

    private java.util.LinkedHashMap<java.lang.String, java.lang.Integer> getNextIndexGroup(com.android.server.integrity.model.BitInputStream bitInputStream) throws java.io.IOException {
        java.util.LinkedHashMap<java.lang.String, java.lang.Integer> keyToIndexMap = new java.util.LinkedHashMap<>();
        while (bitInputStream.hasNext()) {
            java.lang.String key = com.android.server.integrity.parser.BinaryFileOperations.getStringValue(bitInputStream);
            int value = com.android.server.integrity.parser.BinaryFileOperations.getIntValue(bitInputStream);
            keyToIndexMap.put(key, java.lang.Integer.valueOf(value));
            if (key.matches(com.android.server.integrity.model.IndexingFileConstants.END_INDEXING_KEY)) {
                break;
            }
        }
        if (keyToIndexMap.size() < 2) {
            throw new java.lang.IllegalStateException("Indexing file is corrupt.");
        }
        return keyToIndexMap;
    }

    private static com.android.server.integrity.parser.RuleIndexRange searchIndexingKeysRangeContainingKey(java.util.LinkedHashMap<java.lang.String, java.lang.Integer> indexMap, java.lang.String searchedKey) {
        java.util.List<java.lang.String> keys = (java.util.List) indexMap.keySet().stream().collect(java.util.stream.Collectors.toList());
        if (keys.size() < 2) {
            throw new java.lang.RuntimeException("Key list size < 2, read all rules!");
        }
        java.util.List<java.lang.String> identifiedKeyRange = searchKeysRangeContainingKey(keys, searchedKey, 0, keys.size() - 1);
        return new com.android.server.integrity.parser.RuleIndexRange(indexMap.get(identifiedKeyRange.get(0)).intValue(), indexMap.get(identifiedKeyRange.get(1)).intValue());
    }

    private static java.util.List<java.lang.String> searchKeysRangeContainingKey(java.util.List<java.lang.String> sortedKeyList, java.lang.String key, int startIndex, int endIndex) {
        if (endIndex <= startIndex) {
            throw new java.lang.IllegalStateException("Indexing file is corrupt.");
        }
        if (endIndex - startIndex == 1) {
            return java.util.Arrays.asList(sortedKeyList.get(startIndex), sortedKeyList.get(endIndex));
        }
        int midKeyIndex = ((endIndex - startIndex) / 2) + startIndex;
        java.lang.String midKey = sortedKeyList.get(midKeyIndex);
        if (key.compareTo(midKey) >= 0) {
            return searchKeysRangeContainingKey(sortedKeyList, key, midKeyIndex, endIndex);
        }
        return searchKeysRangeContainingKey(sortedKeyList, key, startIndex, midKeyIndex);
    }
}
