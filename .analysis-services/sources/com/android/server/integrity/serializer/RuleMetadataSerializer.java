package com.android.server.integrity.serializer;

/* JADX INFO: loaded from: classes2.dex */
public class RuleMetadataSerializer {
    public static void serialize(com.android.server.integrity.model.RuleMetadata ruleMetadata, java.io.OutputStream outputStream) throws java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer xmlSerializer = android.util.Xml.resolveSerializer(outputStream);
        serializeTaggedValue(xmlSerializer, com.android.server.integrity.parser.RuleMetadataParser.RULE_PROVIDER_TAG, ruleMetadata.getRuleProvider());
        serializeTaggedValue(xmlSerializer, com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, ruleMetadata.getVersion());
        xmlSerializer.endDocument();
    }

    private static void serializeTaggedValue(com.android.modules.utils.TypedXmlSerializer xmlSerializer, java.lang.String tag, java.lang.String value) throws java.io.IOException {
        xmlSerializer.startTag((java.lang.String) null, tag);
        xmlSerializer.text(value);
        xmlSerializer.endTag((java.lang.String) null, tag);
    }
}
