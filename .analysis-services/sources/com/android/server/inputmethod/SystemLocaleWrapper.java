package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class SystemLocaleWrapper {
    private static final java.util.concurrent.atomic.AtomicReference<android.os.LocaleList> sSystemLocale = new java.util.concurrent.atomic.AtomicReference<>(new android.os.LocaleList(java.util.Locale.getDefault()));

    interface Callback {
        void onLocaleChanged(android.os.LocaleList localeList, android.os.LocaleList localeList2);
    }

    private SystemLocaleWrapper() {
    }

    static android.os.LocaleList get(int userId) {
        return sSystemLocale.get();
    }

    static void onStart(android.content.Context context, com.android.server.inputmethod.SystemLocaleWrapper.Callback callback, android.os.Handler handler) {
        sSystemLocale.set(context.getResources().getConfiguration().getLocales());
        context.registerReceiver(new com.android.server.inputmethod.SystemLocaleWrapper.LocaleChangeListener(context, callback), new android.content.IntentFilter("android.intent.action.LOCALE_CHANGED"), null, handler);
    }

    private static final class LocaleChangeListener extends android.content.BroadcastReceiver {
        private final com.android.server.inputmethod.SystemLocaleWrapper.Callback mCallback;
        private final android.content.Context mContext;

        LocaleChangeListener(android.content.Context context, com.android.server.inputmethod.SystemLocaleWrapper.Callback callback) {
            this.mContext = context;
            this.mCallback = callback;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (!"android.intent.action.LOCALE_CHANGED".equals(intent.getAction())) {
                return;
            }
            android.os.LocaleList newLocales = this.mContext.getResources().getConfiguration().getLocales();
            android.os.LocaleList prevLocales = (android.os.LocaleList) com.android.server.inputmethod.SystemLocaleWrapper.sSystemLocale.getAndSet(newLocales);
            if (!java.util.Objects.equals(newLocales, prevLocales)) {
                this.mCallback.onLocaleChanged(prevLocales, newLocales);
            }
        }
    }
}
