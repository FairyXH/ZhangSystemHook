package com.android.server.permission.access.util;

/* JADX INFO: compiled from: BinaryXmlSerializerExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000V\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001d\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0086\b\u001a\u001d\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0007H\u0086\b\u001a%\u0010\b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u0007H\u0086\b\u001a\u001d\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000bH\u0086\b\u001a\u001d\u0010\f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000bH\u0086\b\u001a\u001d\u0010\r\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000eH\u0086\b\u001a%\u0010\u000f\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u000eH\u0086\b\u001a\u001d\u0010\u0010\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0011H\u0086\b\u001a%\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0086\b\u001a\u001d\u0010\u0013\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0014H\u0086\b\u001a\u001d\u0010\u0015\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0014H\u0086\b\u001a%\u0010\u0016\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0086\b\u001a%\u0010\u0017\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00142\u0006\u0010\t\u001a\u00020\u0014H\u0086\b\u001a\u001d\u0010\u0018\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0086\b\u001a\u001d\u0010\u0019\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001aH\u0086\b\u001a\u001d\u0010\u001b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001aH\u0086\b\u001a%\u0010\u001c\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001a2\u0006\u0010\t\u001a\u00020\u001aH\u0086\b\u001a%\u0010\u001d\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u001a2\u0006\u0010\t\u001a\u00020\u001aH\u0086\b\u001a&\u0010\u001e\u001a\u00020\u0001*\u00020\u00022\u0017\u0010\u001f\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010 ¢\u0006\u0002\b!H\u0086\b\u001a&\u0010\"\u001a\u00020\u0001*\u00020#2\u0017\u0010\u001f\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010 ¢\u0006\u0002\b!H\u0086\b\u001a.\u0010$\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u001f\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010 ¢\u0006\u0002\b!H\u0086\b¨\u0006%"}, d2 = {"attribute", "", "Lcom/android/modules/utils/BinaryXmlSerializer;", "name", "", "value", "attributeBoolean", "", "attributeBooleanWithDefault", "defaultValue", "attributeBytesBase64", "", "attributeBytesHex", "attributeDouble", "", "attributeDoubleWithDefault", "attributeFloat", "", "attributeFloatWithDefault", "attributeInt", "", "attributeIntHex", "attributeIntHexWithDefault", "attributeIntWithDefault", "attributeInterned", "attributeLong", "", "attributeLongHex", "attributeLongHexWithDefault", "attributeLongWithDefault", "document", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "serializeBinaryXml", "Ljava/io/OutputStream;", "tag", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class BinaryXmlSerializerExtensionsKt {
    public static final void serializeBinaryXml(java.io.OutputStream $this$serializeBinaryXml, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.modules.utils.BinaryXmlSerializer, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.io.IOException {
        com.android.modules.utils.BinaryXmlSerializer $this$serializeBinaryXml_u24lambda_u240 = new com.android.modules.utils.BinaryXmlSerializer();
        $this$serializeBinaryXml_u24lambda_u240.setOutput($this$serializeBinaryXml, (java.lang.String) null);
        $this$serializeBinaryXml_u24lambda_u240.startDocument((java.lang.String) null, true);
        function1.invoke($this$serializeBinaryXml_u24lambda_u240);
        $this$serializeBinaryXml_u24lambda_u240.endDocument();
    }

    public static final void document(com.android.modules.utils.BinaryXmlSerializer $this$document, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.modules.utils.BinaryXmlSerializer, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.io.IOException {
        $this$document.startDocument((java.lang.String) null, true);
        function1.invoke($this$document);
        $this$document.endDocument();
    }

    public static final void tag(com.android.modules.utils.BinaryXmlSerializer $this$tag, java.lang.String name, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.modules.utils.BinaryXmlSerializer, com.android.server.permission.jarjar.kotlin.Unit> function1) throws java.io.IOException {
        $this$tag.startTag((java.lang.String) null, name);
        function1.invoke($this$tag);
        $this$tag.endTag((java.lang.String) null, name);
    }

    public static final void attribute(com.android.modules.utils.BinaryXmlSerializer $this$attribute, java.lang.String name, java.lang.String value) throws java.io.IOException {
        $this$attribute.attribute((java.lang.String) null, name, value);
    }

    public static final void attributeInterned(com.android.modules.utils.BinaryXmlSerializer $this$attributeInterned, java.lang.String name, java.lang.String value) throws java.io.IOException {
        $this$attributeInterned.attributeInterned((java.lang.String) null, name, value);
    }

    public static final void attributeBytesHex(com.android.modules.utils.BinaryXmlSerializer $this$attributeBytesHex, java.lang.String name, byte[] value) throws java.io.IOException {
        $this$attributeBytesHex.attributeBytesHex((java.lang.String) null, name, value);
    }

    public static final void attributeBytesBase64(com.android.modules.utils.BinaryXmlSerializer $this$attributeBytesBase64, java.lang.String name, byte[] value) throws java.io.IOException {
        $this$attributeBytesBase64.attributeBytesBase64((java.lang.String) null, name, value);
    }

    public static final void attributeInt(com.android.modules.utils.BinaryXmlSerializer $this$attributeInt, java.lang.String name, int value) throws java.io.IOException {
        $this$attributeInt.attributeInt((java.lang.String) null, name, value);
    }

    public static final void attributeIntWithDefault(com.android.modules.utils.BinaryXmlSerializer $this$attributeIntWithDefault, java.lang.String name, int value, int defaultValue) throws java.io.IOException {
        if (value != defaultValue) {
            $this$attributeIntWithDefault.attributeInt((java.lang.String) null, name, value);
        }
    }

    public static final void attributeIntHex(com.android.modules.utils.BinaryXmlSerializer $this$attributeIntHex, java.lang.String name, int value) throws java.io.IOException {
        $this$attributeIntHex.attributeIntHex((java.lang.String) null, name, value);
    }

    public static final void attributeIntHexWithDefault(com.android.modules.utils.BinaryXmlSerializer $this$attributeIntHexWithDefault, java.lang.String name, int value, int defaultValue) throws java.io.IOException {
        if (value != defaultValue) {
            $this$attributeIntHexWithDefault.attributeIntHex((java.lang.String) null, name, value);
        }
    }

    public static final void attributeLong(com.android.modules.utils.BinaryXmlSerializer $this$attributeLong, java.lang.String name, long value) throws java.io.IOException {
        $this$attributeLong.attributeLong((java.lang.String) null, name, value);
    }

    public static final void attributeLongWithDefault(com.android.modules.utils.BinaryXmlSerializer $this$attributeLongWithDefault, java.lang.String name, long value, long defaultValue) throws java.io.IOException {
        if (value != defaultValue) {
            $this$attributeLongWithDefault.attributeLong((java.lang.String) null, name, value);
        }
    }

    public static final void attributeLongHex(com.android.modules.utils.BinaryXmlSerializer $this$attributeLongHex, java.lang.String name, long value) throws java.io.IOException {
        $this$attributeLongHex.attributeLongHex((java.lang.String) null, name, value);
    }

    public static final void attributeLongHexWithDefault(com.android.modules.utils.BinaryXmlSerializer $this$attributeLongHexWithDefault, java.lang.String name, long value, long defaultValue) throws java.io.IOException {
        if (value != defaultValue) {
            $this$attributeLongHexWithDefault.attributeLongHex((java.lang.String) null, name, value);
        }
    }

    public static final void attributeFloat(com.android.modules.utils.BinaryXmlSerializer $this$attributeFloat, java.lang.String name, float value) throws java.io.IOException {
        $this$attributeFloat.attributeFloat((java.lang.String) null, name, value);
    }

    public static final void attributeFloatWithDefault(com.android.modules.utils.BinaryXmlSerializer $this$attributeFloatWithDefault, java.lang.String name, float value, float defaultValue) throws java.io.IOException {
        if (!(value == defaultValue)) {
            $this$attributeFloatWithDefault.attributeFloat((java.lang.String) null, name, value);
        }
    }

    public static final void attributeDouble(com.android.modules.utils.BinaryXmlSerializer $this$attributeDouble, java.lang.String name, double value) throws java.io.IOException {
        $this$attributeDouble.attributeDouble((java.lang.String) null, name, value);
    }

    public static final void attributeDoubleWithDefault(com.android.modules.utils.BinaryXmlSerializer $this$attributeDoubleWithDefault, java.lang.String name, double value, double defaultValue) throws java.io.IOException {
        if (!(value == defaultValue)) {
            $this$attributeDoubleWithDefault.attributeDouble((java.lang.String) null, name, value);
        }
    }

    public static final void attributeBoolean(com.android.modules.utils.BinaryXmlSerializer $this$attributeBoolean, java.lang.String name, boolean value) throws java.io.IOException {
        $this$attributeBoolean.attributeBoolean((java.lang.String) null, name, value);
    }

    public static final void attributeBooleanWithDefault(com.android.modules.utils.BinaryXmlSerializer $this$attributeBooleanWithDefault, java.lang.String name, boolean value, boolean defaultValue) throws java.io.IOException {
        if (value != defaultValue) {
            $this$attributeBooleanWithDefault.attributeBoolean((java.lang.String) null, name, value);
        }
    }
}
