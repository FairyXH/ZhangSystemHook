package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
public class TestModelEnrollmentDatabase implements com.android.server.voiceinteraction.IEnrolledModelDb {
    private final java.util.Map<com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey, android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel> mModelMap = new java.util.HashMap();

    /* JADX INFO: Access modifiers changed from: private */
    static final class EnrollmentKey {
        private final int mKeyphraseId;
        private final java.lang.String mLocale;
        private final java.util.List<java.lang.Integer> mUserIds;

        EnrollmentKey(int keyphraseId, java.util.List<java.lang.Integer> userIds, java.lang.String locale) {
            this.mKeyphraseId = keyphraseId;
            this.mUserIds = (java.util.List) java.util.Objects.requireNonNull(userIds);
            this.mLocale = (java.lang.String) java.util.Objects.requireNonNull(locale);
        }

        int keyphraseId() {
            return this.mKeyphraseId;
        }

        java.util.List<java.lang.Integer> userIds() {
            return this.mUserIds;
        }

        java.lang.String locale() {
            return this.mLocale;
        }

        public java.lang.String toString() {
            java.util.StringJoiner sj = new java.util.StringJoiner(", ", "{", "}");
            sj.add("keyphraseId: " + this.mKeyphraseId);
            sj.add("userIds: " + this.mUserIds.toString());
            sj.add("locale: " + this.mLocale.toString());
            return "EnrollmentKey: " + sj.toString();
        }

        public int hashCode() {
            int res = (1 * 31) + this.mKeyphraseId;
            return (((res * 31) + this.mUserIds.hashCode()) * 31) + this.mLocale.hashCode();
        }

        public boolean equals(java.lang.Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || !(other instanceof com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey)) {
                return false;
            }
            com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey that = (com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey) other;
            if (this.mKeyphraseId == that.mKeyphraseId && this.mUserIds.equals(that.mUserIds) && this.mLocale.equals(that.mLocale)) {
                return true;
            }
            return false;
        }
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean updateKeyphraseSoundModel(android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel soundModel) {
        android.hardware.soundtrigger.SoundTrigger.Keyphrase keyphrase = soundModel.getKeyphrases()[0];
        this.mModelMap.put(new com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey(keyphrase.getId(), java.util.Arrays.stream(keyphrase.getUsers()).boxed().toList(), keyphrase.getLocale().toLanguageTag()), soundModel);
        return true;
    }

    static /* synthetic */ boolean lambda$deleteKeyphraseSoundModel$0(int keyphraseId, java.lang.String bcp47Locale, int userHandle, com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey key) {
        return key.keyphraseId() == keyphraseId && key.locale().equals(bcp47Locale) && key.userIds().contains(java.lang.Integer.valueOf(userHandle));
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public boolean deleteKeyphraseSoundModel(final int keyphraseId, final int userHandle, final java.lang.String bcp47Locale) {
        return this.mModelMap.keySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.voiceinteraction.TestModelEnrollmentDatabase.lambda$deleteKeyphraseSoundModel$0(keyphraseId, bcp47Locale, userHandle, (com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey) obj);
            }
        });
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(final int keyphraseId, final int userHandle, final java.lang.String bcp47Locale) {
        return (android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel) this.mModelMap.entrySet().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.voiceinteraction.TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$1(keyphraseId, bcp47Locale, userHandle, (java.util.Map.Entry) obj);
            }
        }).findFirst().map(new java.util.function.Function() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.voiceinteraction.TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$2((java.util.Map.Entry) obj);
            }
        }).orElse(null);
    }

    static /* synthetic */ boolean lambda$getKeyphraseSoundModel$1(int keyphraseId, java.lang.String bcp47Locale, int userHandle, java.util.Map.Entry entry) {
        return ((com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey) entry.getKey()).keyphraseId() == keyphraseId && ((com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey) entry.getKey()).locale().equals(bcp47Locale) && ((com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey) entry.getKey()).userIds().contains(java.lang.Integer.valueOf(userHandle));
    }

    static /* synthetic */ android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel lambda$getKeyphraseSoundModel$2(java.util.Map.Entry entry) {
        return (android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel) entry.getValue();
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel getKeyphraseSoundModel(final java.lang.String keyphrase, final int userHandle, final java.lang.String bcp47Locale) {
        return (android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel) this.mModelMap.entrySet().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.voiceinteraction.TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$3(keyphrase, bcp47Locale, userHandle, (java.util.Map.Entry) obj);
            }
        }).findFirst().map(new java.util.function.Function() { // from class: com.android.server.voiceinteraction.TestModelEnrollmentDatabase$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.voiceinteraction.TestModelEnrollmentDatabase.lambda$getKeyphraseSoundModel$4((java.util.Map.Entry) obj);
            }
        }).orElse(null);
    }

    static /* synthetic */ boolean lambda$getKeyphraseSoundModel$3(java.lang.String keyphrase, java.lang.String bcp47Locale, int userHandle, java.util.Map.Entry entry) {
        return ((android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel) entry.getValue()).getKeyphrases()[0].getText().equals(keyphrase) && ((com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey) entry.getKey()).locale().equals(bcp47Locale) && ((com.android.server.voiceinteraction.TestModelEnrollmentDatabase.EnrollmentKey) entry.getKey()).userIds().contains(java.lang.Integer.valueOf(userHandle));
    }

    static /* synthetic */ android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel lambda$getKeyphraseSoundModel$4(java.util.Map.Entry entry) {
        return (android.hardware.soundtrigger.SoundTrigger.KeyphraseSoundModel) entry.getValue();
    }

    @Override // com.android.server.voiceinteraction.IEnrolledModelDb
    public void dump(java.io.PrintWriter pw) {
        pw.println("Using test enrollment database, with enrolled models:");
        pw.println(this.mModelMap);
    }
}
