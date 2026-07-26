package com.android.server.locksettings.recoverablekeystore.serialization;

/* JADX INFO: loaded from: classes2.dex */
public class KeyChainSnapshotSerializer {
    public static void serialize(android.security.keystore.recovery.KeyChainSnapshot keyChainSnapshot, java.io.OutputStream outputStream) throws java.io.IOException, java.security.cert.CertificateEncodingException {
        com.android.modules.utils.TypedXmlSerializer xmlSerializer = android.util.Xml.resolveSerializer(outputStream);
        xmlSerializer.startDocument((java.lang.String) null, (java.lang.Boolean) null);
        xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainSnapshot");
        writeKeyChainSnapshotProperties(xmlSerializer, keyChainSnapshot);
        writeKeyChainProtectionParams(xmlSerializer, keyChainSnapshot.getKeyChainProtectionParams());
        writeApplicationKeys(xmlSerializer, keyChainSnapshot.getWrappedApplicationKeys());
        xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainSnapshot");
        xmlSerializer.endDocument();
    }

    private static void writeApplicationKeys(com.android.modules.utils.TypedXmlSerializer xmlSerializer, java.util.List<android.security.keystore.recovery.WrappedApplicationKey> wrappedApplicationKeys) throws java.io.IOException {
        xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "applicationKeysList");
        for (android.security.keystore.recovery.WrappedApplicationKey key : wrappedApplicationKeys) {
            xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "applicationKey");
            writeApplicationKeyProperties(xmlSerializer, key);
            xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "applicationKey");
        }
        xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "applicationKeysList");
    }

    private static void writeApplicationKeyProperties(com.android.modules.utils.TypedXmlSerializer xmlSerializer, android.security.keystore.recovery.WrappedApplicationKey applicationKey) throws java.io.IOException {
        writePropertyTag(xmlSerializer, "alias", applicationKey.getAlias());
        writePropertyTag(xmlSerializer, "keyMaterial", applicationKey.getEncryptedKeyMaterial());
        writePropertyTag(xmlSerializer, "keyMetadata", applicationKey.getMetadata());
    }

    private static void writeKeyChainProtectionParams(com.android.modules.utils.TypedXmlSerializer xmlSerializer, java.util.List<android.security.keystore.recovery.KeyChainProtectionParams> keyChainProtectionParamsList) throws java.io.IOException {
        xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParamsList");
        for (android.security.keystore.recovery.KeyChainProtectionParams keyChainProtectionParams : keyChainProtectionParamsList) {
            xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParams");
            writeKeyChainProtectionParamsProperties(xmlSerializer, keyChainProtectionParams);
            xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParams");
        }
        xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParamsList");
    }

    private static void writeKeyChainProtectionParamsProperties(com.android.modules.utils.TypedXmlSerializer xmlSerializer, android.security.keystore.recovery.KeyChainProtectionParams keyChainProtectionParams) throws java.io.IOException {
        writePropertyTag(xmlSerializer, "userSecretType", keyChainProtectionParams.getUserSecretType());
        writePropertyTag(xmlSerializer, "lockScreenUiType", keyChainProtectionParams.getLockScreenUiFormat());
        writeKeyDerivationParams(xmlSerializer, keyChainProtectionParams.getKeyDerivationParams());
    }

    private static void writeKeyDerivationParams(com.android.modules.utils.TypedXmlSerializer xmlSerializer, android.security.keystore.recovery.KeyDerivationParams keyDerivationParams) throws java.io.IOException {
        xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyDerivationParams");
        writeKeyDerivationParamsProperties(xmlSerializer, keyDerivationParams);
        xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyDerivationParams");
    }

    private static void writeKeyDerivationParamsProperties(com.android.modules.utils.TypedXmlSerializer xmlSerializer, android.security.keystore.recovery.KeyDerivationParams keyDerivationParams) throws java.io.IOException {
        writePropertyTag(xmlSerializer, "algorithm", keyDerivationParams.getAlgorithm());
        writePropertyTag(xmlSerializer, "salt", keyDerivationParams.getSalt());
        writePropertyTag(xmlSerializer, "memoryDifficulty", keyDerivationParams.getMemoryDifficulty());
    }

    private static void writeKeyChainSnapshotProperties(com.android.modules.utils.TypedXmlSerializer xmlSerializer, android.security.keystore.recovery.KeyChainSnapshot keyChainSnapshot) throws java.io.IOException, java.security.cert.CertificateEncodingException {
        writePropertyTag(xmlSerializer, "snapshotVersion", keyChainSnapshot.getSnapshotVersion());
        writePropertyTag(xmlSerializer, "maxAttempts", keyChainSnapshot.getMaxAttempts());
        writePropertyTag(xmlSerializer, "counterId", keyChainSnapshot.getCounterId());
        writePropertyTag(xmlSerializer, "recoveryKeyMaterial", keyChainSnapshot.getEncryptedRecoveryKeyBlob());
        writePropertyTag(xmlSerializer, "serverParams", keyChainSnapshot.getServerParams());
        writePropertyTag(xmlSerializer, "thmCertPath", keyChainSnapshot.getTrustedHardwareCertPath());
    }

    private static void writePropertyTag(com.android.modules.utils.TypedXmlSerializer xmlSerializer, java.lang.String propertyName, long propertyValue) throws java.io.IOException {
        xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, propertyName);
        xmlSerializer.text(java.lang.Long.toString(propertyValue));
        xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, propertyName);
    }

    private static void writePropertyTag(com.android.modules.utils.TypedXmlSerializer xmlSerializer, java.lang.String propertyName, java.lang.String propertyValue) throws java.io.IOException {
        xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, propertyName);
        xmlSerializer.text(propertyValue);
        xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, propertyName);
    }

    private static void writePropertyTag(com.android.modules.utils.TypedXmlSerializer xmlSerializer, java.lang.String propertyName, byte[] propertyValue) throws java.io.IOException {
        if (propertyValue == null) {
            return;
        }
        xmlSerializer.startTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, propertyName);
        xmlSerializer.text(android.util.Base64.encodeToString(propertyValue, 0));
        xmlSerializer.endTag(com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, propertyName);
    }

    private static void writePropertyTag(com.android.modules.utils.TypedXmlSerializer xmlSerializer, java.lang.String propertyName, java.security.cert.CertPath certPath) throws java.io.IOException, java.security.cert.CertificateEncodingException {
        writePropertyTag(xmlSerializer, propertyName, certPath.getEncoded("PkiPath"));
    }

    private KeyChainSnapshotSerializer() {
    }
}
