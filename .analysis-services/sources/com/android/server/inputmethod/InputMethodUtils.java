package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodUtils {
    public static final boolean DEBUG = false;
    static final char INPUT_METHOD_SEPARATOR = ':';
    static final char INPUT_METHOD_SUBTYPE_SEPARATOR = ';';
    static final int NOT_A_SUBTYPE_ID = -1;
    private static final java.lang.String TAG = "InputMethodUtils";

    private InputMethodUtils() {
    }

    static boolean canAddToLastInputMethod(android.view.inputmethod.InputMethodSubtype subtype) {
        if (subtype == null) {
            return true;
        }
        return true ^ subtype.isAuxiliary();
    }

    static void setNonSelectedSystemImesDisabledUntilUsed(android.content.pm.PackageManager packageManagerForUser, java.util.List<android.view.inputmethod.InputMethodInfo> enabledImis) {
        java.lang.String[] systemImesDisabledUntilUsed = android.content.res.Resources.getSystem().getStringArray(android.R.array.config_device_state_postures);
        if (systemImesDisabledUntilUsed == null || systemImesDisabledUntilUsed.length == 0) {
            return;
        }
        android.view.textservice.SpellCheckerInfo currentSpellChecker = com.android.server.textservices.TextServicesManagerInternal.get().getCurrentSpellCheckerForUser(packageManagerForUser.getUserId());
        for (java.lang.String packageName : systemImesDisabledUntilUsed) {
            boolean enabledIme = false;
            int j = 0;
            while (true) {
                if (j >= enabledImis.size()) {
                    break;
                }
                android.view.inputmethod.InputMethodInfo imi = enabledImis.get(j);
                if (!packageName.equals(imi.getPackageName())) {
                    j++;
                } else {
                    enabledIme = true;
                    break;
                }
            }
            if (!enabledIme && (currentSpellChecker == null || !packageName.equals(currentSpellChecker.getPackageName()))) {
                try {
                    android.content.pm.ApplicationInfo ai = packageManagerForUser.getApplicationInfo(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(32768L));
                    if (ai != null) {
                        boolean isSystemPackage = (ai.flags & 1) != 0;
                        if (isSystemPackage) {
                            setDisabledUntilUsed(packageManagerForUser, packageName);
                        }
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                }
            }
        }
    }

    private static void setDisabledUntilUsed(android.content.pm.PackageManager packageManagerForUser, java.lang.String packageName) {
        try {
            int state = packageManagerForUser.getApplicationEnabledSetting(packageName);
            if (state == 0 || state == 1) {
                try {
                    packageManagerForUser.setApplicationEnabledSetting(packageName, 4, 0);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.w(TAG, "setApplicationEnabledSetting failed. packageName=" + packageName + " userId=" + packageManagerForUser.getUserId(), e);
                }
            }
        } catch (java.lang.IllegalArgumentException e2) {
            android.util.Slog.w(TAG, "getApplicationEnabledSetting failed. packageName=" + packageName + " userId=" + packageManagerForUser.getUserId(), e2);
        }
    }

    static boolean checkIfPackageBelongsToUid(android.content.pm.PackageManagerInternal packageManagerInternal, int uid, java.lang.String packageName) {
        return packageManagerInternal.isSameApp(packageName, 0L, uid, android.os.UserHandle.getUserId(uid));
    }

    static boolean isSoftInputModeStateVisibleAllowed(int targetSdkVersion, int startInputFlags) {
        if (targetSdkVersion < 28) {
            return true;
        }
        return ((startInputFlags & 1) == 0 || (startInputFlags & 2) == 0) ? false : true;
    }

    static int[] resolveUserId(int userIdToBeResolved, int currentUserId, java.io.PrintWriter warningWriter) {
        int sourceUserId;
        com.android.server.pm.UserManagerInternal userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        if (userIdToBeResolved == -1) {
            return userManagerInternal.getUserIds();
        }
        if (userIdToBeResolved == -2) {
            sourceUserId = currentUserId;
        } else {
            if (userIdToBeResolved < 0) {
                if (warningWriter != null) {
                    warningWriter.print("Pseudo user ID ");
                    warningWriter.print(userIdToBeResolved);
                    warningWriter.println(" is not supported.");
                }
                return new int[0];
            }
            if (userManagerInternal.exists(userIdToBeResolved)) {
                sourceUserId = userIdToBeResolved;
            } else {
                if (warningWriter != null) {
                    warningWriter.print("User #");
                    warningWriter.print(userIdToBeResolved);
                    warningWriter.println(" does not exit.");
                }
                return new int[0];
            }
        }
        return new int[]{sourceUserId};
    }

    static java.util.List<java.lang.String> getEnabledInputMethodIdsForFiltering(android.content.Context context, int userId) {
        java.lang.String enabledInputMethodsStr = android.text.TextUtils.nullIfEmpty(com.android.server.inputmethod.SecureSettingsWrapper.getString("enabled_input_methods", null, userId));
        final java.util.ArrayList<java.lang.String> result = new java.util.ArrayList<>();
        java.util.Objects.requireNonNull(result);
        splitEnabledImeStr(enabledInputMethodsStr, new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                result.add((java.lang.String) obj);
            }
        });
        return result;
    }

    static void splitEnabledImeStr(java.lang.String text, java.util.function.Consumer<java.lang.String> consumer) {
        if (android.text.TextUtils.isEmpty(text)) {
            return;
        }
        android.text.TextUtils.SimpleStringSplitter inputMethodSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SEPARATOR);
        android.text.TextUtils.SimpleStringSplitter subtypeSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SUBTYPE_SEPARATOR);
        inputMethodSplitter.setString(text);
        while (inputMethodSplitter.hasNext()) {
            java.lang.String nextImsStr = inputMethodSplitter.next();
            subtypeSplitter.setString(nextImsStr);
            if (subtypeSplitter.hasNext()) {
                consumer.accept(subtypeSplitter.next());
            }
        }
    }

    static java.lang.String concatEnabledImeIds(java.lang.String existingEnabledImeId, java.lang.String... imeIds) {
        final android.util.ArraySet<java.lang.String> alreadyEnabledIds = new android.util.ArraySet<>();
        java.util.StringJoiner joiner = new java.util.StringJoiner(java.lang.Character.toString(INPUT_METHOD_SEPARATOR));
        if (!android.text.TextUtils.isEmpty(existingEnabledImeId)) {
            java.util.Objects.requireNonNull(alreadyEnabledIds);
            splitEnabledImeStr(existingEnabledImeId, new java.util.function.Consumer() { // from class: com.android.server.inputmethod.InputMethodUtils$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    alreadyEnabledIds.add((java.lang.String) obj);
                }
            });
            joiner.add(existingEnabledImeId);
        }
        for (java.lang.String id : imeIds) {
            if (!alreadyEnabledIds.contains(id)) {
                joiner.add(id);
                alreadyEnabledIds.add(id);
            }
        }
        return joiner.toString();
    }

    public static android.content.ComponentName convertIdToComponentName(java.lang.String id) {
        return android.content.ComponentName.unflattenFromString(id);
    }
}
