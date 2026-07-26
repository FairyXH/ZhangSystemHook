package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
abstract class StringFilter implements com.android.server.firewall.Filter {
    private static final java.lang.String ATTR_CONTAINS = "contains";
    private static final java.lang.String ATTR_EQUALS = "equals";
    private static final java.lang.String ATTR_IS_NULL = "isNull";
    private static final java.lang.String ATTR_PATTERN = "pattern";
    private static final java.lang.String ATTR_REGEX = "regex";
    private static final java.lang.String ATTR_STARTS_WITH = "startsWith";
    private final com.android.server.firewall.StringFilter.ValueProvider mValueProvider;
    public static final com.android.server.firewall.StringFilter.ValueProvider COMPONENT = new com.android.server.firewall.StringFilter.ValueProvider("component") { // from class: com.android.server.firewall.StringFilter.1
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            if (resolvedComponent != null) {
                return resolvedComponent.flattenToString();
            }
            return null;
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider COMPONENT_NAME = new com.android.server.firewall.StringFilter.ValueProvider("component-name") { // from class: com.android.server.firewall.StringFilter.2
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            if (resolvedComponent != null) {
                return resolvedComponent.getClassName();
            }
            return null;
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider COMPONENT_PACKAGE = new com.android.server.firewall.StringFilter.ValueProvider("component-package") { // from class: com.android.server.firewall.StringFilter.3
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            if (resolvedComponent != null) {
                return resolvedComponent.getPackageName();
            }
            return null;
        }
    };
    public static final com.android.server.firewall.FilterFactory ACTION = new com.android.server.firewall.StringFilter.ValueProvider("action") { // from class: com.android.server.firewall.StringFilter.4
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            return intent.getAction();
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider DATA = new com.android.server.firewall.StringFilter.ValueProvider("data") { // from class: com.android.server.firewall.StringFilter.5
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            android.net.Uri data = intent.getData();
            if (data != null) {
                return data.toString();
            }
            return null;
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider MIME_TYPE = new com.android.server.firewall.StringFilter.ValueProvider("mime-type") { // from class: com.android.server.firewall.StringFilter.6
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            return resolvedType;
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider SCHEME = new com.android.server.firewall.StringFilter.ValueProvider("scheme") { // from class: com.android.server.firewall.StringFilter.7
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            android.net.Uri data = intent.getData();
            if (data != null) {
                return data.getScheme();
            }
            return null;
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider SSP = new com.android.server.firewall.StringFilter.ValueProvider("scheme-specific-part") { // from class: com.android.server.firewall.StringFilter.8
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            android.net.Uri data = intent.getData();
            if (data != null) {
                return data.getSchemeSpecificPart();
            }
            return null;
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider HOST = new com.android.server.firewall.StringFilter.ValueProvider("host") { // from class: com.android.server.firewall.StringFilter.9
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            android.net.Uri data = intent.getData();
            if (data != null) {
                return data.getHost();
            }
            return null;
        }
    };
    public static final com.android.server.firewall.StringFilter.ValueProvider PATH = new com.android.server.firewall.StringFilter.ValueProvider("path") { // from class: com.android.server.firewall.StringFilter.10
        @Override // com.android.server.firewall.StringFilter.ValueProvider
        public java.lang.String getValue(android.content.ComponentName resolvedComponent, android.content.Intent intent, java.lang.String resolvedType) {
            android.net.Uri data = intent.getData();
            if (data != null) {
                return data.getPath();
            }
            return null;
        }
    };

    protected abstract boolean matchesValue(java.lang.String str);

    private StringFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider) {
        this.mValueProvider = valueProvider;
    }

    public static com.android.server.firewall.StringFilter readFromXml(com.android.server.firewall.StringFilter.ValueProvider valueProvider, org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.firewall.StringFilter filter = null;
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            com.android.server.firewall.StringFilter newFilter = getFilter(valueProvider, parser, i);
            if (newFilter != null) {
                if (filter != null) {
                    throw new org.xmlpull.v1.XmlPullParserException("Multiple string filter attributes found");
                }
                filter = newFilter;
            }
        }
        if (filter == null) {
            com.android.server.firewall.StringFilter filter2 = new com.android.server.firewall.StringFilter.IsNullFilter(valueProvider, false);
            return filter2;
        }
        return filter;
    }

    private static com.android.server.firewall.StringFilter getFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, org.xmlpull.v1.XmlPullParser parser, int attributeIndex) {
        java.lang.String attributeName = parser.getAttributeName(attributeIndex);
        switch (attributeName.charAt(0)) {
            case 'c':
                if (attributeName.equals(ATTR_CONTAINS)) {
                    break;
                }
                break;
            case 'e':
                if (attributeName.equals(ATTR_EQUALS)) {
                    break;
                }
                break;
            case 'i':
                if (attributeName.equals(ATTR_IS_NULL)) {
                    break;
                }
                break;
            case 'p':
                if (attributeName.equals(ATTR_PATTERN)) {
                    break;
                }
                break;
            case 'r':
                if (attributeName.equals(ATTR_REGEX)) {
                    break;
                }
                break;
            case 's':
                if (attributeName.equals(ATTR_STARTS_WITH)) {
                    break;
                }
                break;
        }
        return null;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        java.lang.String value = this.mValueProvider.getValue(resolvedComponent, intent, resolvedType);
        return matchesValue(value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    static abstract class ValueProvider extends com.android.server.firewall.FilterFactory {
        public abstract java.lang.String getValue(android.content.ComponentName componentName, android.content.Intent intent, java.lang.String str);

        protected ValueProvider(java.lang.String tag) {
            super(tag);
        }

        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            return com.android.server.firewall.StringFilter.readFromXml(this, parser);
        }
    }

    private static class EqualsFilter extends com.android.server.firewall.StringFilter {
        private final java.lang.String mFilterValue;

        public EqualsFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, java.lang.String attrValue) {
            super(valueProvider);
            this.mFilterValue = attrValue;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(java.lang.String value) {
            return value != null && value.equals(this.mFilterValue);
        }
    }

    private static class ContainsFilter extends com.android.server.firewall.StringFilter {
        private final java.lang.String mFilterValue;

        public ContainsFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, java.lang.String attrValue) {
            super(valueProvider);
            this.mFilterValue = attrValue;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(java.lang.String value) {
            return value != null && value.contains(this.mFilterValue);
        }
    }

    private static class StartsWithFilter extends com.android.server.firewall.StringFilter {
        private final java.lang.String mFilterValue;

        public StartsWithFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, java.lang.String attrValue) {
            super(valueProvider);
            this.mFilterValue = attrValue;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(java.lang.String value) {
            return value != null && value.startsWith(this.mFilterValue);
        }
    }

    private static class PatternStringFilter extends com.android.server.firewall.StringFilter {
        private final android.os.PatternMatcher mPattern;

        public PatternStringFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, java.lang.String attrValue) {
            super(valueProvider);
            this.mPattern = new android.os.PatternMatcher(attrValue, 2);
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(java.lang.String value) {
            return value != null && this.mPattern.match(value);
        }
    }

    private static class RegexFilter extends com.android.server.firewall.StringFilter {
        private final java.util.regex.Pattern mPattern;

        public RegexFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, java.lang.String attrValue) {
            super(valueProvider);
            this.mPattern = java.util.regex.Pattern.compile(attrValue);
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(java.lang.String value) {
            return value != null && this.mPattern.matcher(value).matches();
        }
    }

    private static class IsNullFilter extends com.android.server.firewall.StringFilter {
        private final boolean mIsNull;

        public IsNullFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, java.lang.String attrValue) {
            super(valueProvider);
            this.mIsNull = java.lang.Boolean.parseBoolean(attrValue);
        }

        public IsNullFilter(com.android.server.firewall.StringFilter.ValueProvider valueProvider, boolean isNull) {
            super(valueProvider);
            this.mIsNull = isNull;
        }

        @Override // com.android.server.firewall.StringFilter
        public boolean matchesValue(java.lang.String value) {
            return (value == null) == this.mIsNull;
        }
    }
}
