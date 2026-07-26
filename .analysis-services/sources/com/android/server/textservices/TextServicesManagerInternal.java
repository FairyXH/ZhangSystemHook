package com.android.server.textservices;

/* JADX INFO: loaded from: classes3.dex */
public abstract class TextServicesManagerInternal {
    private static final com.android.server.textservices.TextServicesManagerInternal NOP = new com.android.server.textservices.TextServicesManagerInternal() { // from class: com.android.server.textservices.TextServicesManagerInternal.1
        @Override // com.android.server.textservices.TextServicesManagerInternal
        public android.view.textservice.SpellCheckerInfo getCurrentSpellCheckerForUser(int userId) {
            return null;
        }
    };

    public abstract android.view.textservice.SpellCheckerInfo getCurrentSpellCheckerForUser(int i);

    public static com.android.server.textservices.TextServicesManagerInternal get() {
        com.android.server.textservices.TextServicesManagerInternal instance = (com.android.server.textservices.TextServicesManagerInternal) com.android.server.LocalServices.getService(com.android.server.textservices.TextServicesManagerInternal.class);
        return instance != null ? instance : NOP;
    }
}
