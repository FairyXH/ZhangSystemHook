package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class SettingsXml {
    private static final boolean DEBUG_THROW_EXCEPTIONS = false;
    private static final int DEFAULT_NUMBER = -1;
    private static final java.lang.String FEATURE_INDENT = "http://xmlpull.org/v1/doc/features.html#indent-output";
    private static final java.lang.String TAG = "SettingsXml";

    public interface ChildSection extends com.android.server.pm.SettingsXml.ReadSection {
        boolean moveToNext();

        boolean moveToNext(java.lang.String str);
    }

    public interface ReadSection extends java.lang.AutoCloseable {
        com.android.server.pm.SettingsXml.ChildSection children();

        boolean getBoolean(java.lang.String str);

        boolean getBoolean(java.lang.String str, boolean z);

        java.lang.String getDescription();

        int getInt(java.lang.String str);

        int getInt(java.lang.String str, int i);

        long getLong(java.lang.String str);

        long getLong(java.lang.String str, int i);

        java.lang.String getName();

        java.lang.String getString(java.lang.String str);

        java.lang.String getString(java.lang.String str, java.lang.String str2);

        boolean has(java.lang.String str);
    }

    public interface WriteSection extends java.lang.AutoCloseable {
        com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String str, int i) throws java.io.IOException;

        com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String str, long j) throws java.io.IOException;

        com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String str, java.lang.String str2) throws java.io.IOException;

        com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String str, boolean z) throws java.io.IOException;

        @Override // java.lang.AutoCloseable
        void close() throws java.io.IOException;

        void finish() throws java.io.IOException;

        com.android.server.pm.SettingsXml.WriteSection startSection(java.lang.String str) throws java.io.IOException;
    }

    public static com.android.server.pm.SettingsXml.Serializer serializer(com.android.modules.utils.TypedXmlSerializer serializer) {
        return new com.android.server.pm.SettingsXml.Serializer(serializer);
    }

    public static com.android.server.pm.SettingsXml.ReadSection parser(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        return new com.android.server.pm.SettingsXml.ReadSectionImpl(parser);
    }

    public static class Serializer implements java.lang.AutoCloseable {
        private final com.android.server.pm.SettingsXml.WriteSectionImpl mWriteSection;
        private final com.android.modules.utils.TypedXmlSerializer mXmlSerializer;

        private Serializer(com.android.modules.utils.TypedXmlSerializer serializer) {
            this.mXmlSerializer = serializer;
            this.mWriteSection = new com.android.server.pm.SettingsXml.WriteSectionImpl(this.mXmlSerializer);
        }

        public com.android.server.pm.SettingsXml.WriteSection startSection(java.lang.String sectionName) throws java.io.IOException {
            return this.mWriteSection.startSection(sectionName);
        }

        @Override // java.lang.AutoCloseable
        public void close() throws java.io.IOException {
            this.mWriteSection.closeCompletely();
            this.mXmlSerializer.flush();
        }
    }

    public static class ReadSectionImpl implements com.android.server.pm.SettingsXml.ChildSection {
        private final java.util.Stack<java.lang.Integer> mDepthStack;
        private final java.io.InputStream mInput;
        private final com.android.modules.utils.TypedXmlPullParser mParser;

        public ReadSectionImpl(java.io.InputStream input) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            this.mDepthStack = new java.util.Stack<>();
            this.mInput = input;
            this.mParser = android.util.Xml.newFastPullParser();
            this.mParser.setInput(this.mInput, java.nio.charset.StandardCharsets.UTF_8.name());
            moveToFirstTag();
        }

        public ReadSectionImpl(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            this.mDepthStack = new java.util.Stack<>();
            this.mInput = null;
            this.mParser = parser;
            moveToFirstTag();
        }

        private void moveToFirstTag() throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int type;
            if (this.mParser.getEventType() == 2) {
                return;
            }
            do {
                type = this.mParser.next();
                if (type == 2) {
                    return;
                }
            } while (type != 1);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public java.lang.String getName() {
            return this.mParser.getName();
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public java.lang.String getDescription() {
            return this.mParser.getPositionDescription();
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public boolean has(java.lang.String attrName) {
            return this.mParser.getAttributeValue((java.lang.String) null, attrName) != null;
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public java.lang.String getString(java.lang.String attrName) {
            return this.mParser.getAttributeValue((java.lang.String) null, attrName);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public java.lang.String getString(java.lang.String attrName, java.lang.String defaultValue) {
            java.lang.String value = this.mParser.getAttributeValue((java.lang.String) null, attrName);
            if (value == null) {
                return defaultValue;
            }
            return value;
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public boolean getBoolean(java.lang.String attrName) {
            return getBoolean(attrName, false);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public boolean getBoolean(java.lang.String attrName, boolean defaultValue) {
            return this.mParser.getAttributeBoolean((java.lang.String) null, attrName, defaultValue);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public int getInt(java.lang.String attrName) {
            return getInt(attrName, -1);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public int getInt(java.lang.String attrName, int defaultValue) {
            return this.mParser.getAttributeInt((java.lang.String) null, attrName, defaultValue);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public long getLong(java.lang.String attrName) {
            return getLong(attrName, -1);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public long getLong(java.lang.String attrName, int defaultValue) {
            return this.mParser.getAttributeLong((java.lang.String) null, attrName, defaultValue);
        }

        @Override // com.android.server.pm.SettingsXml.ReadSection
        public com.android.server.pm.SettingsXml.ChildSection children() {
            this.mDepthStack.push(java.lang.Integer.valueOf(this.mParser.getDepth()));
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.ChildSection
        public boolean moveToNext() {
            return moveToNextInternal(null);
        }

        @Override // com.android.server.pm.SettingsXml.ChildSection
        public boolean moveToNext(java.lang.String expectedChildTagName) {
            return moveToNextInternal(expectedChildTagName);
        }

        private boolean moveToNextInternal(java.lang.String expectedChildTagName) {
            try {
                int depth = this.mDepthStack.peek().intValue();
                boolean hasTag = false;
                while (!hasTag) {
                    int type = this.mParser.next();
                    if (type == 1 || (type == 3 && this.mParser.getDepth() <= depth)) {
                        break;
                    }
                    if (type == 2 && (expectedChildTagName == null || expectedChildTagName.equals(this.mParser.getName()))) {
                        hasTag = true;
                    }
                }
                if (!hasTag) {
                    this.mDepthStack.pop();
                }
                return hasTag;
            } catch (java.lang.Exception e) {
                return false;
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() throws java.lang.Exception {
            if (this.mDepthStack.isEmpty()) {
                android.util.Slog.wtf(com.android.server.pm.SettingsXml.TAG, "Children depth stack was not empty, data may have been lost", new java.lang.Exception());
            }
            if (this.mInput != null) {
                this.mInput.close();
            }
        }
    }

    private static class WriteSectionImpl implements com.android.server.pm.SettingsXml.WriteSection {
        private final java.util.Stack<java.lang.String> mTagStack;
        private final com.android.modules.utils.TypedXmlSerializer mXmlSerializer;

        private WriteSectionImpl(com.android.modules.utils.TypedXmlSerializer xmlSerializer) {
            this.mTagStack = new java.util.Stack<>();
            this.mXmlSerializer = xmlSerializer;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public com.android.server.pm.SettingsXml.WriteSection startSection(java.lang.String sectionName) throws java.io.IOException {
            this.mXmlSerializer.startTag((java.lang.String) null, sectionName);
            this.mTagStack.push(sectionName);
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String attrName, java.lang.String value) throws java.io.IOException {
            if (value != null) {
                this.mXmlSerializer.attribute((java.lang.String) null, attrName, value);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String attrName, int value) throws java.io.IOException {
            if (value != -1) {
                this.mXmlSerializer.attributeInt((java.lang.String) null, attrName, value);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String attrName, long value) throws java.io.IOException {
            if (value != -1) {
                this.mXmlSerializer.attributeLong((java.lang.String) null, attrName, value);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public com.android.server.pm.SettingsXml.WriteSection attribute(java.lang.String attrName, boolean value) throws java.io.IOException {
            if (value) {
                this.mXmlSerializer.attributeBoolean((java.lang.String) null, attrName, value);
            }
            return this;
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection
        public void finish() throws java.io.IOException {
            close();
        }

        @Override // com.android.server.pm.SettingsXml.WriteSection, java.lang.AutoCloseable
        public void close() throws java.io.IOException {
            this.mXmlSerializer.endTag((java.lang.String) null, this.mTagStack.pop());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void closeCompletely() throws java.io.IOException {
            if (this.mTagStack != null) {
                while (!this.mTagStack.isEmpty()) {
                    close();
                }
            }
        }
    }
}
