package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodSubtypeSwitchingController {
    private static final boolean DEBUG = false;
    private static final int NOT_A_SUBTYPE_ID = -1;
    private static final java.lang.String TAG = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.class.getSimpleName();
    private final android.content.Context mContext;
    private com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ControllerImpl mController;
    private final int mUserId;

    public static class ImeSubtypeListItem implements java.lang.Comparable<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> {
        public final java.lang.CharSequence mImeName;
        public final android.view.inputmethod.InputMethodInfo mImi;
        public final boolean mIsSystemLanguage;
        public final boolean mIsSystemLocale;
        public final int mSubtypeId;
        public final java.lang.CharSequence mSubtypeName;

        ImeSubtypeListItem(java.lang.CharSequence imeName, java.lang.CharSequence subtypeName, android.view.inputmethod.InputMethodInfo imi, int subtypeId, java.lang.String subtypeLocale, java.lang.String systemLocale) {
            this.mImeName = imeName;
            this.mSubtypeName = subtypeName;
            this.mImi = imi;
            this.mSubtypeId = subtypeId;
            boolean z = false;
            if (android.text.TextUtils.isEmpty(subtypeLocale)) {
                this.mIsSystemLocale = false;
                this.mIsSystemLanguage = false;
                return;
            }
            this.mIsSystemLocale = subtypeLocale.equals(systemLocale);
            if (this.mIsSystemLocale) {
                this.mIsSystemLanguage = true;
                return;
            }
            java.lang.String systemLanguage = com.android.server.inputmethod.LocaleUtils.getLanguageFromLocaleString(systemLocale);
            java.lang.String subtypeLanguage = com.android.server.inputmethod.LocaleUtils.getLanguageFromLocaleString(subtypeLocale);
            if (systemLanguage.length() >= 2 && systemLanguage.equals(subtypeLanguage)) {
                z = true;
            }
            this.mIsSystemLanguage = z;
        }

        private static int compareNullableCharSequences(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2) {
            boolean zIsEmpty = android.text.TextUtils.isEmpty(charSequence);
            boolean zIsEmpty2 = android.text.TextUtils.isEmpty(charSequence2);
            if (zIsEmpty || zIsEmpty2) {
                return (zIsEmpty ? 1 : 0) - (zIsEmpty2 ? 1 : 0);
            }
            return charSequence.toString().compareTo(charSequence2.toString());
        }

        @Override // java.lang.Comparable
        public int compareTo(com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem other) {
            int result = compareNullableCharSequences(this.mImeName, other.mImeName);
            if (result != 0) {
                return result;
            }
            int result2 = (this.mIsSystemLocale ? -1 : 0) - (other.mIsSystemLocale ? -1 : 0);
            if (result2 != 0) {
                return result2;
            }
            int result3 = (this.mIsSystemLanguage ? -1 : 0) - (other.mIsSystemLanguage ? -1 : 0);
            if (result3 != 0) {
                return result3;
            }
            int result4 = compareNullableCharSequences(this.mSubtypeName, other.mSubtypeName);
            if (result4 != 0) {
                return result4;
            }
            return this.mImi.getId().compareTo(other.mImi.getId());
        }

        public java.lang.String toString() {
            return "ImeSubtypeListItem{mImeName=" + ((java.lang.Object) this.mImeName) + " mSubtypeName=" + ((java.lang.Object) this.mSubtypeName) + " mSubtypeId=" + this.mSubtypeId + " mIsSystemLocale=" + this.mIsSystemLocale + " mIsSystemLanguage=" + this.mIsSystemLanguage + "}";
        }

        public boolean equals(java.lang.Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem)) {
                return false;
            }
            com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem that = (com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem) o;
            return java.util.Objects.equals(this.mImi, that.mImi) && this.mSubtypeId == that.mSubtypeId;
        }
    }

    static java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> getSortedInputMethodAndSubtypeList(boolean includeAuxiliarySubtypes, boolean isScreenLocked, boolean forImeMenu, android.content.Context context, com.android.server.inputmethod.InputMethodMap methodMap, int userId) {
        android.content.Context userAwareContext;
        boolean includeAuxiliarySubtypes2;
        android.content.Context userAwareContext2;
        int i;
        android.content.Context userAwareContext3;
        int j;
        int subtypeCount;
        android.util.ArraySet<java.lang.String> enabledSubtypeSet;
        android.view.inputmethod.InputMethodInfo imi;
        int i2;
        if (context.getUserId() == userId) {
            userAwareContext = context;
        } else {
            userAwareContext = context.createContextAsUser(android.os.UserHandle.of(userId), 0);
        }
        java.lang.String mSystemLocaleStr = com.android.server.inputmethod.SystemLocaleWrapper.get(userId).get(0).toLanguageTag();
        com.android.server.inputmethod.InputMethodSettings settings = com.android.server.inputmethod.InputMethodSettings.create(methodMap, userId);
        java.util.ArrayList<android.view.inputmethod.InputMethodInfo> imis = settings.getEnabledInputMethodList();
        if (imis.isEmpty()) {
            android.util.Slog.w(TAG, "Enabled input method list is empty.");
            return new java.util.ArrayList();
        }
        if (isScreenLocked && includeAuxiliarySubtypes) {
            includeAuxiliarySubtypes2 = false;
        } else {
            includeAuxiliarySubtypes2 = includeAuxiliarySubtypes;
        }
        java.util.ArrayList<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> imList = new java.util.ArrayList<>();
        int numImes = imis.size();
        int i3 = 0;
        while (i3 < numImes) {
            android.view.inputmethod.InputMethodInfo imi2 = imis.get(i3);
            if (forImeMenu && !imi2.shouldShowInInputMethodPicker()) {
                userAwareContext2 = userAwareContext;
                i = i3;
            } else {
                java.util.List<android.view.inputmethod.InputMethodSubtype> explicitlyOrImplicitlyEnabledSubtypeList = settings.getEnabledInputMethodSubtypeList(imi2, true);
                android.util.ArraySet<java.lang.String> enabledSubtypeSet2 = new android.util.ArraySet<>();
                java.util.Iterator<android.view.inputmethod.InputMethodSubtype> it = explicitlyOrImplicitlyEnabledSubtypeList.iterator();
                while (it.hasNext()) {
                    enabledSubtypeSet2.add(java.lang.String.valueOf(it.next().hashCode()));
                }
                java.lang.CharSequence imeLabel = imi2.loadLabel(userAwareContext.getPackageManager());
                if (enabledSubtypeSet2.size() > 0) {
                    int subtypeCount2 = imi2.getSubtypeCount();
                    int j2 = 0;
                    while (j2 < subtypeCount2) {
                        android.view.inputmethod.InputMethodSubtype subtype = imi2.getSubtypeAt(j2);
                        java.lang.String subtypeHashCode = java.lang.String.valueOf(subtype.hashCode());
                        if (!enabledSubtypeSet2.contains(subtypeHashCode)) {
                            userAwareContext3 = userAwareContext;
                            j = j2;
                            subtypeCount = subtypeCount2;
                            enabledSubtypeSet = enabledSubtypeSet2;
                            imi = imi2;
                            i2 = i3;
                        } else if (includeAuxiliarySubtypes2 || !subtype.isAuxiliary()) {
                            java.lang.CharSequence subtypeLabel = subtype.overridesImplicitlyEnabledSubtype() ? null : subtype.getDisplayName(userAwareContext, imi2.getPackageName(), imi2.getServiceInfo().applicationInfo);
                            j = j2;
                            subtypeCount = subtypeCount2;
                            userAwareContext3 = userAwareContext;
                            enabledSubtypeSet = enabledSubtypeSet2;
                            imi = imi2;
                            i2 = i3;
                            imList.add(new com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem(imeLabel, subtypeLabel, imi2, j, subtype.getLocale(), mSystemLocaleStr));
                            enabledSubtypeSet.remove(subtypeHashCode);
                        } else {
                            userAwareContext3 = userAwareContext;
                            j = j2;
                            subtypeCount = subtypeCount2;
                            enabledSubtypeSet = enabledSubtypeSet2;
                            imi = imi2;
                            i2 = i3;
                        }
                        j2 = j + 1;
                        enabledSubtypeSet2 = enabledSubtypeSet;
                        i3 = i2;
                        subtypeCount2 = subtypeCount;
                        userAwareContext = userAwareContext3;
                        imi2 = imi;
                    }
                    userAwareContext2 = userAwareContext;
                    i = i3;
                } else {
                    userAwareContext2 = userAwareContext;
                    i = i3;
                    imList.add(new com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem(imeLabel, null, imi2, -1, null, mSystemLocaleStr));
                }
            }
            i3 = i + 1;
            userAwareContext = userAwareContext2;
        }
        java.util.Collections.sort(imList);
        return imList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int calculateSubtypeId(android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
        if (subtype != null) {
            return com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(imi, subtype.hashCode());
        }
        return -1;
    }

    private static class StaticRotationList {
        private final java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> mImeSubtypeList;

        StaticRotationList(java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> imeSubtypeList) {
            this.mImeSubtypeList = imeSubtypeList;
        }

        private int getIndex(android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
            int currentSubtypeId = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.calculateSubtypeId(imi, subtype);
            int numSubtypes = this.mImeSubtypeList.size();
            for (int i = 0; i < numSubtypes; i++) {
                com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem isli = this.mImeSubtypeList.get(i);
                if (imi.equals(isli.mImi) && isli.mSubtypeId == currentSubtypeId) {
                    return i;
                }
            }
            return -1;
        }

        public com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
            int currentIndex;
            if (imi == null || this.mImeSubtypeList.size() <= 1 || (currentIndex = getIndex(imi, subtype)) < 0) {
                return null;
            }
            int numSubtypes = this.mImeSubtypeList.size();
            for (int offset = 1; offset < numSubtypes; offset++) {
                int candidateIndex = (currentIndex + offset) % numSubtypes;
                com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem candidate = this.mImeSubtypeList.get(candidateIndex);
                if (!onlyCurrentIme || imi.equals(candidate.mImi)) {
                    return candidate;
                }
            }
            return null;
        }

        protected void dump(android.util.Printer pw, java.lang.String prefix) {
            int numSubtypes = this.mImeSubtypeList.size();
            for (int i = 0; i < numSubtypes; i++) {
                int rank = i;
                com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem item = this.mImeSubtypeList.get(i);
                pw.println(prefix + "rank=" + rank + " item=" + item);
            }
        }
    }

    private static class DynamicRotationList {
        private static final java.lang.String TAG = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.DynamicRotationList.class.getSimpleName();
        private final java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> mImeSubtypeList;
        private final int[] mUsageHistoryOfSubtypeListItemIndex;

        private DynamicRotationList(java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> imeSubtypeListItems) {
            this.mImeSubtypeList = imeSubtypeListItems;
            this.mUsageHistoryOfSubtypeListItemIndex = new int[this.mImeSubtypeList.size()];
            int numSubtypes = this.mImeSubtypeList.size();
            for (int i = 0; i < numSubtypes; i++) {
                this.mUsageHistoryOfSubtypeListItemIndex[i] = i;
            }
        }

        private int getUsageRank(android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
            int currentSubtypeId = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.calculateSubtypeId(imi, subtype);
            int numItems = this.mUsageHistoryOfSubtypeListItemIndex.length;
            for (int usageRank = 0; usageRank < numItems; usageRank++) {
                int subtypeListItemIndex = this.mUsageHistoryOfSubtypeListItemIndex[usageRank];
                com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem subtypeListItem = this.mImeSubtypeList.get(subtypeListItemIndex);
                if (subtypeListItem.mImi.equals(imi) && subtypeListItem.mSubtypeId == currentSubtypeId) {
                    return usageRank;
                }
            }
            return -1;
        }

        public void onUserAction(android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
            int currentUsageRank = getUsageRank(imi, subtype);
            if (currentUsageRank <= 0) {
                return;
            }
            int currentItemIndex = this.mUsageHistoryOfSubtypeListItemIndex[currentUsageRank];
            java.lang.System.arraycopy(this.mUsageHistoryOfSubtypeListItemIndex, 0, this.mUsageHistoryOfSubtypeListItemIndex, 1, currentUsageRank);
            this.mUsageHistoryOfSubtypeListItemIndex[0] = currentItemIndex;
        }

        public com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
            int currentUsageRank = getUsageRank(imi, subtype);
            if (currentUsageRank < 0) {
                return null;
            }
            int numItems = this.mUsageHistoryOfSubtypeListItemIndex.length;
            for (int i = 1; i < numItems; i++) {
                int subtypeListItemRank = (currentUsageRank + i) % numItems;
                int subtypeListItemIndex = this.mUsageHistoryOfSubtypeListItemIndex[subtypeListItemRank];
                com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem subtypeListItem = this.mImeSubtypeList.get(subtypeListItemIndex);
                if (!onlyCurrentIme || imi.equals(subtypeListItem.mImi)) {
                    return subtypeListItem;
                }
            }
            return null;
        }

        protected void dump(android.util.Printer pw, java.lang.String prefix) {
            for (int rank = 0; rank < this.mUsageHistoryOfSubtypeListItemIndex.length; rank++) {
                int index = this.mUsageHistoryOfSubtypeListItemIndex[rank];
                com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem item = this.mImeSubtypeList.get(index);
                pw.println(prefix + "rank=" + rank + " item=" + item);
            }
        }
    }

    public static class ControllerImpl {
        private final com.android.server.inputmethod.InputMethodSubtypeSwitchingController.DynamicRotationList mSwitchingAwareRotationList;
        private final com.android.server.inputmethod.InputMethodSubtypeSwitchingController.StaticRotationList mSwitchingUnawareRotationList;

        public static com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ControllerImpl createFrom(com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ControllerImpl currentInstance, java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> sortedEnabledItems) {
            com.android.server.inputmethod.InputMethodSubtypeSwitchingController.DynamicRotationList switchingAwareRotationList = null;
            java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> switchingAwareImeSubtypes = filterImeSubtypeList(sortedEnabledItems, true);
            if (currentInstance != null && currentInstance.mSwitchingAwareRotationList != null && java.util.Objects.equals(currentInstance.mSwitchingAwareRotationList.mImeSubtypeList, switchingAwareImeSubtypes)) {
                switchingAwareRotationList = currentInstance.mSwitchingAwareRotationList;
            }
            if (switchingAwareRotationList == null) {
                switchingAwareRotationList = new com.android.server.inputmethod.InputMethodSubtypeSwitchingController.DynamicRotationList(switchingAwareImeSubtypes);
            }
            com.android.server.inputmethod.InputMethodSubtypeSwitchingController.StaticRotationList switchingUnawareRotationList = null;
            java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> switchingUnawareImeSubtypes = filterImeSubtypeList(sortedEnabledItems, false);
            if (currentInstance != null && currentInstance.mSwitchingUnawareRotationList != null && java.util.Objects.equals(currentInstance.mSwitchingUnawareRotationList.mImeSubtypeList, switchingUnawareImeSubtypes)) {
                switchingUnawareRotationList = currentInstance.mSwitchingUnawareRotationList;
            }
            if (switchingUnawareRotationList == null) {
                switchingUnawareRotationList = new com.android.server.inputmethod.InputMethodSubtypeSwitchingController.StaticRotationList(switchingUnawareImeSubtypes);
            }
            return new com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ControllerImpl(switchingAwareRotationList, switchingUnawareRotationList);
        }

        private ControllerImpl(com.android.server.inputmethod.InputMethodSubtypeSwitchingController.DynamicRotationList switchingAwareRotationList, com.android.server.inputmethod.InputMethodSubtypeSwitchingController.StaticRotationList switchingUnawareRotationList) {
            this.mSwitchingAwareRotationList = switchingAwareRotationList;
            this.mSwitchingUnawareRotationList = switchingUnawareRotationList;
        }

        public com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem getNextInputMethod(boolean onlyCurrentIme, android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
            if (imi == null) {
                return null;
            }
            if (imi.supportsSwitchingToNextInputMethod()) {
                return this.mSwitchingAwareRotationList.getNextInputMethodLocked(onlyCurrentIme, imi, subtype);
            }
            return this.mSwitchingUnawareRotationList.getNextInputMethodLocked(onlyCurrentIme, imi, subtype);
        }

        public void onUserActionLocked(android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
            if (imi != null && imi.supportsSwitchingToNextInputMethod()) {
                this.mSwitchingAwareRotationList.onUserAction(imi, subtype);
            }
        }

        private static java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> filterImeSubtypeList(java.util.List<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> items, boolean supportsSwitchingToNextInputMethod) {
            java.util.ArrayList<com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem> result = new java.util.ArrayList<>();
            int numItems = items.size();
            for (int i = 0; i < numItems; i++) {
                com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem item = items.get(i);
                if (item.mImi.supportsSwitchingToNextInputMethod() == supportsSwitchingToNextInputMethod) {
                    result.add(item);
                }
            }
            return result;
        }

        protected void dump(android.util.Printer pw, java.lang.String prefix) {
            pw.println(prefix + "mSwitchingAwareRotationList:");
            this.mSwitchingAwareRotationList.dump(pw, prefix + "  ");
            pw.println(prefix + "mSwitchingUnawareRotationList:");
            this.mSwitchingUnawareRotationList.dump(pw, prefix + "  ");
        }
    }

    private InputMethodSubtypeSwitchingController(android.content.Context context, com.android.server.inputmethod.InputMethodMap methodMap, int userId) {
        this.mContext = context;
        this.mUserId = userId;
        this.mController = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ControllerImpl.createFrom(null, getSortedInputMethodAndSubtypeList(false, false, false, context, methodMap, userId));
    }

    public static com.android.server.inputmethod.InputMethodSubtypeSwitchingController createInstanceLocked(android.content.Context context, com.android.server.inputmethod.InputMethodMap methodMap, int userId) {
        return new com.android.server.inputmethod.InputMethodSubtypeSwitchingController(context, methodMap, userId);
    }

    int getUserId() {
        return this.mUserId;
    }

    public void onUserActionLocked(android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
        if (this.mController == null) {
            return;
        }
        this.mController.onUserActionLocked(imi, subtype);
    }

    public void resetCircularListLocked(com.android.server.inputmethod.InputMethodMap methodMap) {
        this.mController = com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ControllerImpl.createFrom(this.mController, getSortedInputMethodAndSubtypeList(false, false, false, this.mContext, methodMap, this.mUserId));
    }

    public com.android.server.inputmethod.InputMethodSubtypeSwitchingController.ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, android.view.inputmethod.InputMethodInfo imi, android.view.inputmethod.InputMethodSubtype subtype) {
        if (this.mController == null) {
            return null;
        }
        return this.mController.getNextInputMethod(onlyCurrentIme, imi, subtype);
    }

    public void dump(android.util.Printer pw, java.lang.String prefix) {
        if (this.mController != null) {
            this.mController.dump(pw, prefix);
        } else {
            pw.println(prefix + "mController=null");
        }
    }
}
