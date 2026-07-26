package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PackageSignatures {
    android.content.pm.SigningDetails mSigningDetails;

    PackageSignatures(com.android.server.pm.PackageSignatures orig) {
        if (orig != null && orig.mSigningDetails != android.content.pm.SigningDetails.UNKNOWN) {
            this.mSigningDetails = new android.content.pm.SigningDetails(orig.mSigningDetails);
        } else {
            this.mSigningDetails = android.content.pm.SigningDetails.UNKNOWN;
        }
    }

    PackageSignatures(android.content.pm.SigningDetails signingDetails) {
        this.mSigningDetails = signingDetails;
    }

    PackageSignatures() {
        this.mSigningDetails = android.content.pm.SigningDetails.UNKNOWN;
    }

    void writeXml(com.android.modules.utils.TypedXmlSerializer serializer, java.lang.String tagName, java.util.ArrayList<android.content.pm.Signature> writtenSignatures) throws java.io.IOException {
        if (this.mSigningDetails.getSignatures() == null) {
            return;
        }
        serializer.startTag((java.lang.String) null, tagName);
        serializer.attributeInt((java.lang.String) null, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, this.mSigningDetails.getSignatures().length);
        serializer.attributeInt((java.lang.String) null, "schemeVersion", this.mSigningDetails.getSignatureSchemeVersion());
        writeCertsListXml(serializer, writtenSignatures, this.mSigningDetails.getSignatures(), false);
        if (this.mSigningDetails.getPastSigningCertificates() != null) {
            serializer.startTag((java.lang.String) null, "pastSigs");
            serializer.attributeInt((java.lang.String) null, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, this.mSigningDetails.getPastSigningCertificates().length);
            writeCertsListXml(serializer, writtenSignatures, this.mSigningDetails.getPastSigningCertificates(), true);
            serializer.endTag((java.lang.String) null, "pastSigs");
        }
        serializer.endTag((java.lang.String) null, tagName);
    }

    private void writeCertsListXml(com.android.modules.utils.TypedXmlSerializer serializer, java.util.ArrayList<android.content.pm.Signature> writtenSignatures, android.content.pm.Signature[] signatures, boolean isPastSigs) throws java.io.IOException {
        for (android.content.pm.Signature sig : signatures) {
            serializer.startTag((java.lang.String) null, "cert");
            int sigHash = sig.hashCode();
            int numWritten = writtenSignatures.size();
            int j = 0;
            while (true) {
                if (j >= numWritten) {
                    break;
                }
                android.content.pm.Signature writtenSig = writtenSignatures.get(j);
                if (writtenSig.hashCode() != sigHash || !writtenSig.equals(sig)) {
                    j++;
                } else {
                    serializer.attributeInt((java.lang.String) null, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, j);
                    break;
                }
            }
            if (j >= numWritten) {
                writtenSignatures.add(sig);
                serializer.attributeInt((java.lang.String) null, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, numWritten);
                sig.writeToXmlAttributeBytesHex(serializer, null, "key");
            }
            if (isPastSigs) {
                serializer.attributeInt((java.lang.String) null, "flags", sig.getFlags());
            }
            serializer.endTag((java.lang.String) null, "cert");
        }
    }

    void readXml(com.android.modules.utils.TypedXmlPullParser parser, java.util.ArrayList<android.content.pm.Signature> readSignatures) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.content.pm.SigningDetails.Builder builder = new android.content.pm.SigningDetails.Builder();
        int count = parser.getAttributeInt((java.lang.String) null, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, -1);
        if (count != -1) {
            int signatureSchemeVersion = parser.getAttributeInt((java.lang.String) null, "schemeVersion", 0);
            if (signatureSchemeVersion == 0) {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <sigs> has no schemeVersion at " + parser.getPositionDescription());
            }
            builder.setSignatureSchemeVersion(signatureSchemeVersion);
            java.util.ArrayList<android.content.pm.Signature> signatureList = new java.util.ArrayList<>();
            int pos = readCertsListXml(parser, readSignatures, signatureList, count, false, builder);
            android.content.pm.Signature[] signatures = (android.content.pm.Signature[]) signatureList.toArray(new android.content.pm.Signature[signatureList.size()]);
            builder.setSignatures(signatures);
            if (pos < count) {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <sigs> count does not match number of  <cert> entries" + parser.getPositionDescription());
            }
            try {
                this.mSigningDetails = builder.build();
                return;
            } catch (java.security.cert.CertificateException e) {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <sigs> unable to convert certificate(s) to public key(s).");
                this.mSigningDetails = android.content.pm.SigningDetails.UNKNOWN;
                return;
            }
        }
        com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <sigs> has no count at " + parser.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
    }

    private int readCertsListXml(com.android.modules.utils.TypedXmlPullParser parser, java.util.ArrayList<android.content.pm.Signature> readSignatures, java.util.ArrayList<android.content.pm.Signature> signatures, int count, boolean isPastSigs, android.content.pm.SigningDetails.Builder builder) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String str;
        int i;
        int pastSigsCount;
        com.android.modules.utils.TypedXmlPullParser typedXmlPullParser = parser;
        int outerDepth = parser.getDepth();
        android.content.pm.SigningDetails.Builder builder2 = builder;
        int pos = 0;
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
                if (type == 3 || type == 4) {
                    typedXmlPullParser = parser;
                } else {
                    java.lang.String tagName = parser.getName();
                    if (tagName.equals("cert")) {
                        if (pos < count) {
                            int index = typedXmlPullParser.getAttributeInt((java.lang.String) null, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, -1);
                            if (index != -1) {
                                boolean signatureParsed = false;
                                try {
                                    byte[] key = typedXmlPullParser.getAttributeBytesHex((java.lang.String) null, "key", (byte[]) null);
                                    if (key == null) {
                                        if (index >= 0 && index < readSignatures.size()) {
                                            android.content.pm.Signature sig = readSignatures.get(index);
                                            if (sig != null) {
                                                if (isPastSigs) {
                                                    signatures.add(new android.content.pm.Signature(sig));
                                                } else {
                                                    signatures.add(sig);
                                                }
                                                signatureParsed = true;
                                            } else {
                                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <cert> index " + index + " is not defined at " + parser.getPositionDescription());
                                            }
                                        } else {
                                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <cert> index " + index + " is out of bounds at " + parser.getPositionDescription());
                                        }
                                    } else {
                                        android.content.pm.Signature sig2 = new android.content.pm.Signature(key);
                                        while (readSignatures.size() < index) {
                                            readSignatures.add(null);
                                        }
                                        readSignatures.add(sig2);
                                        signatures.add(sig2);
                                        signatureParsed = true;
                                    }
                                } catch (java.lang.NumberFormatException e) {
                                    com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <cert> index " + index + " is not a number at " + parser.getPositionDescription());
                                } catch (java.lang.IllegalArgumentException e2) {
                                    com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <cert> index " + index + " has an invalid signature at " + parser.getPositionDescription() + ": " + e2.getMessage());
                                }
                                if (isPastSigs) {
                                    int flagsValue = typedXmlPullParser.getAttributeInt((java.lang.String) null, "flags", -1);
                                    if (flagsValue != -1) {
                                        if (signatureParsed) {
                                            try {
                                                signatures.get(signatures.size() - 1).setFlags(flagsValue);
                                            } catch (java.lang.NumberFormatException e3) {
                                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <cert> flags " + flagsValue + " is not a number at " + parser.getPositionDescription());
                                            }
                                        } else {
                                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: signature not available at index " + pos + " to set flags at " + parser.getPositionDescription());
                                        }
                                    } else {
                                        com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <cert> has no flags at " + parser.getPositionDescription());
                                    }
                                }
                            } else {
                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <cert> has no index at " + parser.getPositionDescription());
                            }
                        } else {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: too many <cert> tags, expected " + count + " at " + parser.getPositionDescription());
                        }
                        pos++;
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    } else if (tagName.equals("pastSigs")) {
                        if (!isPastSigs) {
                            int pastSigsCount2 = typedXmlPullParser.getAttributeInt((java.lang.String) null, com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, -1);
                            if (pastSigsCount2 == -1) {
                                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <pastSigs> has no count at " + parser.getPositionDescription());
                                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                                typedXmlPullParser = parser;
                            } else {
                                try {
                                    java.util.ArrayList<android.content.pm.Signature> pastSignatureList = new java.util.ArrayList<>();
                                    str = " is not a number at ";
                                    try {
                                        int pastSigsPos = readCertsListXml(parser, readSignatures, pastSignatureList, pastSigsCount2, true, builder2);
                                        android.content.pm.Signature[] pastSignatures = (android.content.pm.Signature[]) pastSignatureList.toArray(new android.content.pm.Signature[pastSignatureList.size()]);
                                        builder2 = builder2.setPastSigningCertificates(pastSignatures);
                                        pastSigsCount = pastSigsCount2;
                                        if (pastSigsPos < pastSigsCount) {
                                            try {
                                                i = 5;
                                                try {
                                                    com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: <pastSigs> count does not match number of <cert> entries " + parser.getPositionDescription());
                                                } catch (java.lang.NumberFormatException e4) {
                                                    com.android.server.pm.PackageManagerService.reportSettingsProblem(i, "Error in package manager settings: <pastSigs> count " + pastSigsCount + str + parser.getPositionDescription());
                                                }
                                            } catch (java.lang.NumberFormatException e5) {
                                                i = 5;
                                            }
                                        }
                                    } catch (java.lang.NumberFormatException e6) {
                                        pastSigsCount = pastSigsCount2;
                                        i = 5;
                                    }
                                } catch (java.lang.NumberFormatException e7) {
                                    str = " is not a number at ";
                                    i = 5;
                                    pastSigsCount = pastSigsCount2;
                                }
                            }
                        } else {
                            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "<pastSigs> encountered multiple times under the same <sigs> at " + parser.getPositionDescription());
                            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                        }
                    } else {
                        com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Unknown element under <sigs>: " + parser.getName());
                        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                    }
                    typedXmlPullParser = parser;
                }
            }
        }
        return pos;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder buf = new java.lang.StringBuilder(128);
        buf.append("PackageSignatures{");
        buf.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        buf.append(" version:");
        buf.append(this.mSigningDetails.getSignatureSchemeVersion());
        buf.append(", signatures:[");
        if (this.mSigningDetails.getSignatures() != null) {
            for (int i = 0; i < this.mSigningDetails.getSignatures().length; i++) {
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(java.lang.Integer.toHexString(this.mSigningDetails.getSignatures()[i].hashCode()));
            }
        }
        buf.append("]");
        buf.append(", past signatures:[");
        if (this.mSigningDetails.getPastSigningCertificates() != null) {
            for (int i2 = 0; i2 < this.mSigningDetails.getPastSigningCertificates().length; i2++) {
                if (i2 > 0) {
                    buf.append(", ");
                }
                buf.append(java.lang.Integer.toHexString(this.mSigningDetails.getPastSigningCertificates()[i2].hashCode()));
                buf.append(" flags: ");
                buf.append(java.lang.Integer.toHexString(this.mSigningDetails.getPastSigningCertificates()[i2].getFlags()));
            }
        }
        buf.append("]}");
        return buf.toString();
    }
}
