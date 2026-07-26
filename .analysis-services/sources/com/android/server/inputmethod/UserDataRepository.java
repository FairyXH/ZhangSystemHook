package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class UserDataRepository {
    private final java.util.function.IntFunction<com.android.server.inputmethod.InputMethodBindingController> mBindingControllerFactory;
    private final android.util.SparseArray<com.android.server.inputmethod.UserDataRepository.UserData> mUserData = new android.util.SparseArray<>();

    com.android.server.inputmethod.UserDataRepository.UserData getOrCreate(int userId) {
        com.android.server.inputmethod.UserDataRepository.UserData userData = this.mUserData.get(userId);
        if (userData == null) {
            com.android.server.inputmethod.UserDataRepository.UserData userData2 = new com.android.server.inputmethod.UserDataRepository.UserData(userId, this.mBindingControllerFactory.apply(userId));
            this.mUserData.put(userId, userData2);
            return userData2;
        }
        return userData;
    }

    void forAllUserData(java.util.function.Consumer<com.android.server.inputmethod.UserDataRepository.UserData> consumer) {
        for (int i = 0; i < this.mUserData.size(); i++) {
            consumer.accept(this.mUserData.valueAt(i));
        }
    }

    UserDataRepository(android.os.Handler handler, com.android.server.pm.UserManagerInternal userManagerInternal, java.util.function.IntFunction<com.android.server.inputmethod.InputMethodBindingController> bindingControllerFactory) {
        this.mBindingControllerFactory = bindingControllerFactory;
        userManagerInternal.addUserLifecycleListener(new com.android.server.inputmethod.UserDataRepository.AnonymousClass1(handler));
    }

    /* JADX INFO: renamed from: com.android.server.inputmethod.UserDataRepository$1, reason: invalid class name */
    class AnonymousClass1 implements com.android.server.pm.UserManagerInternal.UserLifecycleListener {
        final /* synthetic */ android.os.Handler val$handler;

        AnonymousClass1(android.os.Handler handler) {
            this.val$handler = handler;
        }

        @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
        public void onUserRemoved(android.content.pm.UserInfo user) {
            final int userId = user.id;
            this.val$handler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.UserDataRepository$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUserRemoved$0(userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserRemoved$0(int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.UserDataRepository.this.mUserData.remove(userId);
            }
        }

        @Override // com.android.server.pm.UserManagerInternal.UserLifecycleListener
        public void onUserCreated(android.content.pm.UserInfo user, java.lang.Object unusedToken) {
            final int userId = user.id;
            this.val$handler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.UserDataRepository$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUserCreated$1(userId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserCreated$1(int userId) {
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.UserDataRepository.this.getOrCreate(userId);
            }
        }
    }

    static final class UserData {
        final com.android.server.inputmethod.InputMethodBindingController mBindingController;
        final int mUserId;

        private UserData(int userId, com.android.server.inputmethod.InputMethodBindingController bindingController) {
            this.mUserId = userId;
            this.mBindingController = bindingController;
        }

        public java.lang.String toString() {
            return "UserData{mUserId=" + this.mUserId + '}';
        }
    }
}
