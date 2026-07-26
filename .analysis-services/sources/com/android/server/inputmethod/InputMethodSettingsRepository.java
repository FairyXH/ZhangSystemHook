package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodSettingsRepository {
    private static final android.util.SparseArray<com.android.server.inputmethod.InputMethodSettings> sPerUserMap = new android.util.SparseArray<>();

    private InputMethodSettingsRepository() {
    }

    static com.android.server.inputmethod.InputMethodSettings get(int userId) {
        com.android.server.inputmethod.InputMethodSettings obj = sPerUserMap.get(userId);
        if (obj != null) {
            return obj;
        }
        return com.android.server.inputmethod.InputMethodSettings.createEmptyMap(userId);
    }

    static void put(int userId, com.android.server.inputmethod.InputMethodSettings obj) {
        sPerUserMap.put(userId, obj);
    }

    static void initialize(final android.os.Handler handler, final android.content.Context context) {
        final com.android.server.pm.UserManagerInternal userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.InputMethodSettingsRepository$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.inputmethod.InputMethodSettingsRepository.lambda$initialize$0(userManagerInternal, handler, context);
            }
        });
    }

    /* JADX INFO: renamed from: com.android.server.inputmethod.InputMethodSettingsRepository$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.pm.UserManagerInternal.UserLifecycleListener {
        final /* synthetic */ android.os.Handler val$handler;

        AnonymousClass1(android.os.Handler handler) {
            this.val$handler = handler;
        }

        @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
        public void onUserRemoved(android.content.pm.UserInfo user) {
            final int userId = user.id;
            this.val$handler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.InputMethodSettingsRepository$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.inputmethod.InputMethodSettingsRepository.AnonymousClass1.lambda$onUserRemoved$0(userId);
                }
            });
        }

        static /* synthetic */ void lambda$onUserRemoved$0(int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.InputMethodSettingsRepository.sPerUserMap.remove(userId);
            }
        }
    }

    static /* synthetic */ void lambda$initialize$0(com.android.server.pm.UserManagerInternal userManagerInternal, android.os.Handler handler, android.content.Context context) {
        userManagerInternal.addUserLifecycleListener(new com.android.server.inputmethod.InputMethodSettingsRepository.AnonymousClass1(handler));
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            for (int userId : userManagerInternal.getUserIds()) {
                com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodManagerService.queryInputMethodServicesInternal(context, userId, com.android.server.inputmethod.AdditionalSubtypeMapRepository.get(userId), 0);
                put(userId, settings);
            }
        }
    }
}
