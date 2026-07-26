package com.android.server.integrity;

/* JADX INFO: loaded from: classes2.dex */
public class IntegrityFileManager {
    private static final java.lang.String INDEXING_FILE = "indexing";
    private static final java.lang.String METADATA_FILE = "metadata";
    private static final java.lang.String RULES_FILE = "rules";
    private static final java.lang.String TAG = "IntegrityFileManager";
    private final java.io.File mDataDir;
    private com.android.server.integrity.parser.RuleIndexingController mRuleIndexingController;
    private com.android.server.integrity.model.RuleMetadata mRuleMetadataCache;
    private final com.android.server.integrity.parser.RuleParser mRuleParser;
    private final com.android.server.integrity.serializer.RuleSerializer mRuleSerializer;
    private final java.io.File mRulesDir;
    private final java.io.File mStagingDir;
    private static final java.lang.Object RULES_LOCK = new java.lang.Object();
    private static com.android.server.integrity.IntegrityFileManager sInstance = null;

    public static synchronized com.android.server.integrity.IntegrityFileManager getInstance() {
        if (sInstance == null) {
            sInstance = new com.android.server.integrity.IntegrityFileManager();
        }
        return sInstance;
    }

    private IntegrityFileManager() {
        this(new com.android.server.integrity.parser.RuleBinaryParser(), new com.android.server.integrity.serializer.RuleBinarySerializer(), android.os.Environment.getDataSystemDirectory());
    }

    IntegrityFileManager(com.android.server.integrity.parser.RuleParser ruleParser, com.android.server.integrity.serializer.RuleSerializer ruleSerializer, java.io.File dataDir) {
        this.mRuleParser = ruleParser;
        this.mRuleSerializer = ruleSerializer;
        this.mDataDir = dataDir;
        this.mRulesDir = new java.io.File(dataDir, "integrity_rules");
        this.mStagingDir = new java.io.File(dataDir, "integrity_staging");
        if (!this.mStagingDir.mkdirs() || !this.mRulesDir.mkdirs()) {
            android.util.Slog.e(TAG, "Error creating staging and rules directory");
        }
        java.io.File metadataFile = new java.io.File(this.mRulesDir, METADATA_FILE);
        if (metadataFile.exists()) {
            try {
                java.io.FileInputStream inputStream = new java.io.FileInputStream(metadataFile);
                try {
                    this.mRuleMetadataCache = com.android.server.integrity.parser.RuleMetadataParser.parse(inputStream);
                    inputStream.close();
                } finally {
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Error reading metadata file.", e);
            }
        }
        updateRuleIndexingController();
    }

    public boolean initialized() {
        return new java.io.File(this.mRulesDir, RULES_FILE).exists() && new java.io.File(this.mRulesDir, METADATA_FILE).exists() && new java.io.File(this.mRulesDir, INDEXING_FILE).exists();
    }

    public void writeRules(java.lang.String version, java.lang.String ruleProvider, java.util.List<android.content.integrity.Rule> rules) throws com.android.server.integrity.serializer.RuleSerializeException, java.io.IOException {
        try {
            writeMetadata(this.mStagingDir, ruleProvider, version);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Error writing metadata.", e);
        }
        java.io.FileOutputStream ruleFileOutputStream = new java.io.FileOutputStream(new java.io.File(this.mStagingDir, RULES_FILE));
        try {
            java.io.FileOutputStream indexingFileOutputStream = new java.io.FileOutputStream(new java.io.File(this.mStagingDir, INDEXING_FILE));
            try {
                this.mRuleSerializer.serialize(rules, java.util.Optional.empty(), ruleFileOutputStream, indexingFileOutputStream);
                indexingFileOutputStream.close();
                ruleFileOutputStream.close();
                switchStagingRulesDir();
                updateRuleIndexingController();
            } finally {
            }
        } catch (java.lang.Throwable th) {
            try {
                ruleFileOutputStream.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public java.util.List<android.content.integrity.Rule> readRules(android.content.integrity.AppInstallMetadata appInstallMetadata) throws java.io.IOException, com.android.server.integrity.parser.RuleParseException {
        java.util.List<android.content.integrity.Rule> rules;
        synchronized (RULES_LOCK) {
            java.util.List<com.android.server.integrity.parser.RuleIndexRange> ruleReadingIndexes = java.util.Collections.emptyList();
            if (appInstallMetadata != null) {
                try {
                    ruleReadingIndexes = this.mRuleIndexingController.identifyRulesToEvaluate(appInstallMetadata);
                } catch (java.lang.Exception e) {
                    android.util.Slog.w(TAG, "Error identifying the rule indexes. Trying unindexed.", e);
                }
                java.io.File ruleFile = new java.io.File(this.mRulesDir, RULES_FILE);
                rules = this.mRuleParser.parse(com.android.server.integrity.parser.RandomAccessObject.ofFile(ruleFile), ruleReadingIndexes);
            } else {
                java.io.File ruleFile2 = new java.io.File(this.mRulesDir, RULES_FILE);
                rules = this.mRuleParser.parse(com.android.server.integrity.parser.RandomAccessObject.ofFile(ruleFile2), ruleReadingIndexes);
            }
        }
        return rules;
    }

    public com.android.server.integrity.model.RuleMetadata readMetadata() {
        return this.mRuleMetadataCache;
    }

    private void switchStagingRulesDir() throws java.io.IOException {
        synchronized (RULES_LOCK) {
            java.io.File tmpDir = new java.io.File(this.mDataDir, "temp");
            if (!this.mRulesDir.renameTo(tmpDir) || !this.mStagingDir.renameTo(this.mRulesDir) || !tmpDir.renameTo(this.mStagingDir)) {
                throw new java.io.IOException("Error switching staging/rules directory");
            }
            for (java.io.File file : this.mStagingDir.listFiles()) {
                file.delete();
            }
        }
    }

    private void updateRuleIndexingController() {
        java.io.File ruleIndexingFile = new java.io.File(this.mRulesDir, INDEXING_FILE);
        if (ruleIndexingFile.exists()) {
            try {
                java.io.FileInputStream inputStream = new java.io.FileInputStream(ruleIndexingFile);
                try {
                    this.mRuleIndexingController = new com.android.server.integrity.parser.RuleIndexingController(inputStream);
                    inputStream.close();
                } finally {
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Error parsing the rule indexing file.", e);
            }
        }
    }

    private void writeMetadata(java.io.File directory, java.lang.String ruleProvider, java.lang.String version) throws java.io.IOException {
        this.mRuleMetadataCache = new com.android.server.integrity.model.RuleMetadata(ruleProvider, version);
        java.io.File metadataFile = new java.io.File(directory, METADATA_FILE);
        java.io.FileOutputStream outputStream = new java.io.FileOutputStream(metadataFile);
        try {
            com.android.server.integrity.serializer.RuleMetadataSerializer.serialize(this.mRuleMetadataCache, outputStream);
            outputStream.close();
        } catch (java.lang.Throwable th) {
            try {
                outputStream.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
