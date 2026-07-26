package com.android.server.utils.quota;

/* JADX INFO: loaded from: classes3.dex */
public interface Categorizer {
    public static final com.android.server.utils.quota.Categorizer SINGLE_CATEGORIZER = new com.android.server.utils.quota.Categorizer() { // from class: com.android.server.utils.quota.Categorizer$$ExternalSyntheticLambda0
        @Override // com.android.server.utils.quota.Categorizer
        public final com.android.server.utils.quota.Category getCategory(int i, java.lang.String str, java.lang.String str2) {
            return com.android.server.utils.quota.Category.SINGLE_CATEGORY;
        }
    };

    com.android.server.utils.quota.Category getCategory(int i, java.lang.String str, java.lang.String str2);
}
