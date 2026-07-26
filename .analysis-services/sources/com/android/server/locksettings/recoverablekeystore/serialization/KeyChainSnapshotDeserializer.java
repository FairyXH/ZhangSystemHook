package com.android.server.locksettings.recoverablekeystore.serialization;

/* JADX INFO: loaded from: classes2.dex */
public class KeyChainSnapshotDeserializer {
    public static android.security.keystore.recovery.KeyChainSnapshot deserialize(java.io.InputStream inputStream) throws com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, java.io.IOException {
        try {
            return deserializeInternal(inputStream);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            throw new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException("Malformed KeyChainSnapshot XML", e);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:38:0x008f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.security.keystore.recovery.KeyChainSnapshot deserializeInternal(java.io.InputStream r13) throws com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 322
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotDeserializer.deserializeInternal(java.io.InputStream):android.security.keystore.recovery.KeyChainSnapshot");
    }

    private static java.util.List<android.security.keystore.recovery.WrappedApplicationKey> readWrappedApplicationKeys(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, java.io.IOException {
        parser.require(2, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "applicationKeysList");
        java.util.ArrayList<android.security.keystore.recovery.WrappedApplicationKey> keys = new java.util.ArrayList<>();
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                keys.add(readWrappedApplicationKey(parser));
            }
        }
        parser.require(3, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "applicationKeysList");
        return keys;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0047  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.security.keystore.recovery.WrappedApplicationKey readWrappedApplicationKey(com.android.modules.utils.TypedXmlPullParser r8) throws com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            java.lang.String r0 = com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE
            r1 = 2
            java.lang.String r2 = "applicationKey"
            r8.require(r1, r0, r2)
            android.security.keystore.recovery.WrappedApplicationKey$Builder r0 = new android.security.keystore.recovery.WrappedApplicationKey$Builder
            r0.<init>()
        Ld:
            int r3 = r8.next()
            r4 = 3
            if (r3 == r4) goto L76
            int r3 = r8.getEventType()
            if (r3 == r1) goto L1b
            goto Ld
        L1b:
            java.lang.String r3 = r8.getName()
            int r4 = r3.hashCode()
            java.lang.String r5 = "alias"
            java.lang.String r6 = "keyMaterial"
            java.lang.String r7 = "keyMetadata"
            switch(r4) {
                case -1712279890: goto L3f;
                case -963209050: goto L37;
                case 92902992: goto L2f;
                default: goto L2e;
            }
        L2e:
            goto L47
        L2f:
            boolean r4 = r3.equals(r5)
            if (r4 == 0) goto L2e
            r4 = 0
            goto L48
        L37:
            boolean r4 = r3.equals(r6)
            if (r4 == 0) goto L2e
            r4 = 1
            goto L48
        L3f:
            boolean r4 = r3.equals(r7)
            if (r4 == 0) goto L2e
            r4 = r1
            goto L48
        L47:
            r4 = -1
        L48:
            switch(r4) {
                case 0: goto L6d;
                case 1: goto L65;
                case 2: goto L5d;
                default: goto L4b;
            }
        L4b:
            com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException r1 = new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException
            java.util.Locale r2 = java.util.Locale.US
            java.lang.String r4 = "Unexpected tag %s in wrappedApplicationKey"
            java.lang.Object[] r5 = new java.lang.Object[]{r3}
            java.lang.String r2 = java.lang.String.format(r2, r4, r5)
            r1.<init>(r2)
            throw r1
        L5d:
            byte[] r4 = readBlobTag(r8, r7)
            r0.setMetadata(r4)
            goto L75
        L65:
            byte[] r4 = readBlobTag(r8, r6)
            r0.setEncryptedKeyMaterial(r4)
            goto L75
        L6d:
            java.lang.String r4 = readStringTag(r8, r5)
            r0.setAlias(r4)
        L75:
            goto Ld
        L76:
            java.lang.String r1 = com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE
            r8.require(r4, r1, r2)
            android.security.keystore.recovery.WrappedApplicationKey r1 = r0.build()     // Catch: java.lang.NullPointerException -> L80
            return r1
        L80:
            r1 = move-exception
            com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException r2 = new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException
            java.lang.String r3 = "Failed to build WrappedApplicationKey"
            r2.<init>(r3, r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotDeserializer.readWrappedApplicationKey(com.android.modules.utils.TypedXmlPullParser):android.security.keystore.recovery.WrappedApplicationKey");
    }

    private static java.util.List<android.security.keystore.recovery.KeyChainProtectionParams> readKeyChainProtectionParamsList(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, java.io.IOException {
        parser.require(2, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParamsList");
        java.util.ArrayList<android.security.keystore.recovery.KeyChainProtectionParams> keyChainProtectionParamsList = new java.util.ArrayList<>();
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                keyChainProtectionParamsList.add(readKeyChainProtectionParams(parser));
            }
        }
        parser.require(3, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, "keyChainProtectionParamsList");
        return keyChainProtectionParamsList;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.security.keystore.recovery.KeyChainProtectionParams readKeyChainProtectionParams(com.android.modules.utils.TypedXmlPullParser r7) throws com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            java.lang.String r0 = com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE
            r1 = 2
            java.lang.String r2 = "keyChainProtectionParams"
            r7.require(r1, r0, r2)
            android.security.keystore.recovery.KeyChainProtectionParams$Builder r0 = new android.security.keystore.recovery.KeyChainProtectionParams$Builder
            r0.<init>()
        Le:
            int r3 = r7.next()
            r4 = 3
            if (r3 == r4) goto L78
            int r3 = r7.getEventType()
            if (r3 == r1) goto L1c
            goto Le
        L1c:
            java.lang.String r3 = r7.getName()
            int r4 = r3.hashCode()
            java.lang.String r5 = "userSecretType"
            java.lang.String r6 = "lockScreenUiType"
            switch(r4) {
                case -776797115: goto L41;
                case -696958923: goto L39;
                case 912448924: goto L2e;
                default: goto L2d;
            }
        L2d:
            goto L49
        L2e:
            java.lang.String r4 = "keyDerivationParams"
            boolean r4 = r3.equals(r4)
            if (r4 == 0) goto L2d
            r4 = r1
            goto L4a
        L39:
            boolean r4 = r3.equals(r5)
            if (r4 == 0) goto L2d
            r4 = 1
            goto L4a
        L41:
            boolean r4 = r3.equals(r6)
            if (r4 == 0) goto L2d
            r4 = 0
            goto L4a
        L49:
            r4 = -1
        L4a:
            switch(r4) {
                case 0: goto L6f;
                case 1: goto L67;
                case 2: goto L5f;
                default: goto L4d;
            }
        L4d:
            com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException r1 = new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException
            java.util.Locale r2 = java.util.Locale.US
            java.lang.String r4 = "Unexpected tag %s in keyChainProtectionParams"
            java.lang.Object[] r5 = new java.lang.Object[]{r3}
            java.lang.String r2 = java.lang.String.format(r2, r4, r5)
            r1.<init>(r2)
            throw r1
        L5f:
            android.security.keystore.recovery.KeyDerivationParams r4 = readKeyDerivationParams(r7)
            r0.setKeyDerivationParams(r4)
            goto L77
        L67:
            int r4 = readIntTag(r7, r5)
            r0.setUserSecretType(r4)
            goto L77
        L6f:
            int r4 = readIntTag(r7, r6)
            r0.setLockScreenUiFormat(r4)
        L77:
            goto Le
        L78:
            java.lang.String r1 = com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE
            r7.require(r4, r1, r2)
            android.security.keystore.recovery.KeyChainProtectionParams r1 = r0.build()     // Catch: java.lang.NullPointerException -> L82
            return r1
        L82:
            r1 = move-exception
            com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException r2 = new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException
            java.lang.String r3 = "Failed to build KeyChainProtectionParams"
            r2.<init>(r3, r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotDeserializer.readKeyChainProtectionParams(com.android.modules.utils.TypedXmlPullParser):android.security.keystore.recovery.KeyChainProtectionParams");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.security.keystore.recovery.KeyDerivationParams readKeyDerivationParams(com.android.modules.utils.TypedXmlPullParser r10) throws com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            java.lang.String r0 = com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE
            r1 = 2
            java.lang.String r2 = "keyDerivationParams"
            r10.require(r1, r0, r2)
            r0 = -1
            r3 = -1
            r4 = 0
        Lc:
            int r5 = r10.next()
            r6 = 3
            if (r5 == r6) goto L6c
            int r5 = r10.getEventType()
            if (r5 == r1) goto L1a
            goto Lc
        L1a:
            java.lang.String r5 = r10.getName()
            int r6 = r5.hashCode()
            java.lang.String r7 = "algorithm"
            java.lang.String r8 = "salt"
            java.lang.String r9 = "memoryDifficulty"
            switch(r6) {
                case -973274212: goto L3e;
                case 3522646: goto L36;
                case 225490031: goto L2e;
                default: goto L2d;
            }
        L2d:
            goto L46
        L2e:
            boolean r6 = r5.equals(r7)
            if (r6 == 0) goto L2d
            r6 = 1
            goto L47
        L36:
            boolean r6 = r5.equals(r8)
            if (r6 == 0) goto L2d
            r6 = r1
            goto L47
        L3e:
            boolean r6 = r5.equals(r9)
            if (r6 == 0) goto L2d
            r6 = 0
            goto L47
        L46:
            r6 = -1
        L47:
            switch(r6) {
                case 0: goto L66;
                case 1: goto L61;
                case 2: goto L5c;
                default: goto L4a;
            }
        L4a:
            com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException r1 = new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException
            java.util.Locale r2 = java.util.Locale.US
            java.lang.Object[] r6 = new java.lang.Object[]{r5}
            java.lang.String r7 = "Unexpected tag %s in keyDerivationParams"
            java.lang.String r2 = java.lang.String.format(r2, r7, r6)
            r1.<init>(r2)
            throw r1
        L5c:
            byte[] r4 = readBlobTag(r10, r8)
            goto L6b
        L61:
            int r3 = readIntTag(r10, r7)
            goto L6b
        L66:
            int r0 = readIntTag(r10, r9)
        L6b:
            goto Lc
        L6c:
            if (r4 == 0) goto L8a
            r1 = 0
            switch(r3) {
                case 1: goto L7f;
                case 2: goto L7a;
                default: goto L72;
            }
        L72:
            com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException r2 = new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException
            java.lang.String r5 = "Unknown algorithm in keyDerivationParams"
            r2.<init>(r5)
            throw r2
        L7a:
            android.security.keystore.recovery.KeyDerivationParams r1 = android.security.keystore.recovery.KeyDerivationParams.createScryptParams(r4, r0)
            goto L84
        L7f:
            android.security.keystore.recovery.KeyDerivationParams r1 = android.security.keystore.recovery.KeyDerivationParams.createSha256Params(r4)
        L84:
            java.lang.String r5 = com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE
            r10.require(r6, r5, r2)
            return r1
        L8a:
            com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException r1 = new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException
            java.lang.String r2 = "salt was not set in keyDerivationParams"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotDeserializer.readKeyDerivationParams(com.android.modules.utils.TypedXmlPullParser):android.security.keystore.recovery.KeyDerivationParams");
    }

    private static int readIntTag(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tagName) throws org.xmlpull.v1.XmlPullParserException, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, java.io.IOException {
        parser.require(2, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        java.lang.String text = readText(parser);
        parser.require(3, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        try {
            return java.lang.Integer.valueOf(text).intValue();
        } catch (java.lang.NumberFormatException e) {
            throw new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException(java.lang.String.format(java.util.Locale.US, "%s expected int but got '%s'", tagName, text), e);
        }
    }

    private static long readLongTag(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tagName) throws org.xmlpull.v1.XmlPullParserException, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, java.io.IOException {
        parser.require(2, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        java.lang.String text = readText(parser);
        parser.require(3, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        try {
            return java.lang.Long.valueOf(text).longValue();
        } catch (java.lang.NumberFormatException e) {
            throw new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException(java.lang.String.format(java.util.Locale.US, "%s expected long but got '%s'", tagName, text), e);
        }
    }

    private static java.lang.String readStringTag(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tagName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        java.lang.String text = readText(parser);
        parser.require(3, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        return text;
    }

    private static byte[] readBlobTag(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tagName) throws org.xmlpull.v1.XmlPullParserException, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, java.io.IOException {
        parser.require(2, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        java.lang.String text = readText(parser);
        parser.require(3, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotSchema.NAMESPACE, tagName);
        try {
            return android.util.Base64.decode(text, 0);
        } catch (java.lang.IllegalArgumentException e) {
            throw new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException(java.lang.String.format(java.util.Locale.US, "%s expected base64 encoded bytes but got '%s'", tagName, text), e);
        }
    }

    private static java.security.cert.CertPath readCertPathTag(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tagName) throws org.xmlpull.v1.XmlPullParserException, com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException, java.io.IOException {
        byte[] bytes = readBlobTag(parser, tagName);
        try {
            return java.security.cert.CertificateFactory.getInstance("X.509").generateCertPath(new java.io.ByteArrayInputStream(bytes));
        } catch (java.security.cert.CertificateException e) {
            throw new com.android.server.locksettings.recoverablekeystore.serialization.KeyChainSnapshotParserException("Could not parse CertPath in tag " + tagName, e);
        }
    }

    private static java.lang.String readText(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (parser.next() != 4) {
            return "";
        }
        java.lang.String result = parser.getText();
        parser.nextTag();
        return result;
    }

    private KeyChainSnapshotDeserializer() {
    }
}
