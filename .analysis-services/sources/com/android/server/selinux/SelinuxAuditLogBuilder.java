package com.android.server.selinux;

/* JADX INFO: loaded from: classes3.dex */
class SelinuxAuditLogBuilder {
    static final java.lang.String CONFIG_SELINUX_AUDIT_DOMAIN = "selinux_audit_domain";
    private static final java.util.regex.Matcher NO_OP_MATCHER = java.util.regex.Pattern.compile("no-op^").matcher("");
    private static final java.lang.String PATH_PATTERN = "\"(?<path>/\\w+(/\\w+)?)(/\\w+)*\"";
    private static final java.lang.String TAG = "SelinuxAuditLogs";
    private static final java.lang.String TCONTEXT_PATTERN = "u:object_r:(?<ttype>\\w+):s0(:c)?(?<tcategories>((,c)?\\d+)+)*";
    private final com.android.server.selinux.SelinuxAuditLogBuilder.SelinuxAuditLog mAuditLog = new com.android.server.selinux.SelinuxAuditLogBuilder.SelinuxAuditLog();
    final java.util.regex.Matcher mPathMatcher;
    final java.util.regex.Matcher mScontextMatcher;
    final java.util.regex.Matcher mTcontextMatcher;
    private java.util.Iterator<java.lang.String> mTokens;

    SelinuxAuditLogBuilder() {
        java.util.regex.Matcher scontextMatcher = NO_OP_MATCHER;
        java.util.regex.Matcher tcontextMatcher = NO_OP_MATCHER;
        java.util.regex.Matcher pathMatcher = NO_OP_MATCHER;
        try {
            scontextMatcher = java.util.regex.Pattern.compile(android.text.TextUtils.formatSimple("u:r:(?<stype>%s):s0(:c)?(?<scategories>((,c)?\\d+)+)*", new java.lang.Object[]{android.provider.DeviceConfig.getString("adservices", CONFIG_SELINUX_AUDIT_DOMAIN, "no_match^")})).matcher("");
            tcontextMatcher = java.util.regex.Pattern.compile(TCONTEXT_PATTERN).matcher("");
            pathMatcher = java.util.regex.Pattern.compile(PATH_PATTERN).matcher("");
        } catch (java.util.regex.PatternSyntaxException e) {
            android.util.Slog.e(TAG, "Invalid pattern, setting every matcher to no-op.", e);
        }
        this.mScontextMatcher = scontextMatcher;
        this.mTcontextMatcher = tcontextMatcher;
        this.mPathMatcher = pathMatcher;
    }

    void reset(java.lang.String denialString) {
        this.mTokens = java.util.Arrays.asList((java.lang.String[]) java.util.Optional.ofNullable(denialString).map(new java.util.function.Function() { // from class: com.android.server.selinux.SelinuxAuditLogBuilder$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((java.lang.String) obj).split("\\s+|=");
            }
        }).orElse(new java.lang.String[0])).iterator();
        this.mAuditLog.reset();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0071  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    com.android.server.selinux.SelinuxAuditLogBuilder.SelinuxAuditLog build() {
        /*
            Method dump skipped, instruction units count: 400
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.selinux.SelinuxAuditLogBuilder.build():com.android.server.selinux.SelinuxAuditLogBuilder$SelinuxAuditLog");
    }

    static /* synthetic */ java.lang.String[] lambda$build$1(int x$0) {
        return new java.lang.String[x$0];
    }

    boolean nextTokenMatches(java.util.regex.Matcher matcher) {
        return this.mTokens.hasNext() && matcher.reset(this.mTokens.next()).matches();
    }

    static int[] toCategories(java.lang.String categories) {
        if (categories == null) {
            return null;
        }
        return java.util.Arrays.stream(categories.split(",c")).mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda23()).toArray();
    }

    static class SelinuxAuditLog {
        boolean mGranted = false;
        java.lang.String[] mPermissions = null;
        java.lang.String mSType = null;
        int[] mSCategories = null;
        java.lang.String mTType = null;
        int[] mTCategories = null;
        java.lang.String mTClass = null;
        java.lang.String mPath = null;
        boolean mPermissive = false;

        SelinuxAuditLog() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reset() {
            this.mGranted = false;
            this.mPermissions = null;
            this.mSType = null;
            this.mSCategories = null;
            this.mTType = null;
            this.mTCategories = null;
            this.mTClass = null;
            this.mPath = null;
            this.mPermissive = false;
        }
    }
}
