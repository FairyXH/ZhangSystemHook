package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class ValidateNotificationPeople implements com.android.server.notification.NotificationSignalExtractor {
    private static final boolean ENABLE_PEOPLE_VALIDATOR = true;
    private static final int MAX_PEOPLE = 10;
    static final float NONE = 0.0f;
    private static final int PEOPLE_CACHE_SIZE = 200;
    private static final java.lang.String SETTING_ENABLE_PEOPLE_VALIDATOR = "validate_notification_people_enabled";
    static final float STARRED_CONTACT = 1.0f;
    static final float VALID_CONTACT = 0.5f;
    private android.content.Context mBaseContext;
    protected boolean mEnabled;
    private int mEvictionCount;
    private android.os.Handler mHandler;
    private android.database.ContentObserver mObserver;
    private android.util.LruCache<java.lang.String, com.android.server.notification.ValidateNotificationPeople.LookupResult> mPeopleCache;
    private com.android.server.notification.NotificationUsageStats mUsageStats;
    private java.util.Map<java.lang.Integer, android.content.Context> mUserToContextMap;
    private static final java.lang.String TAG = "ValidateNoPeople";
    private static final boolean VERBOSE = android.util.Log.isLoggable(TAG, 2);
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.lang.String[] LOOKUP_PROJECTION = {"_id", "lookup", "starred", "has_phone_number"};
    static final java.lang.String[] PHONE_LOOKUP_PROJECTION = {"data4", "data1"};

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void initialize(android.content.Context context, com.android.server.notification.NotificationUsageStats usageStats) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Initializing  " + getClass().getSimpleName() + ".");
        }
        this.mUserToContextMap = new android.util.ArrayMap();
        this.mBaseContext = context;
        this.mUsageStats = usageStats;
        this.mPeopleCache = new android.util.LruCache<>(200);
        this.mEnabled = 1 == android.provider.Settings.Global.getInt(this.mBaseContext.getContentResolver(), SETTING_ENABLE_PEOPLE_VALIDATOR, 1);
        if (this.mEnabled) {
            this.mHandler = new android.os.Handler();
            this.mObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.notification.ValidateNotificationPeople.1
                @Override // android.database.ContentObserver
                public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
                    super.onChange(selfChange, uri, userId);
                    if ((com.android.server.notification.ValidateNotificationPeople.DEBUG || com.android.server.notification.ValidateNotificationPeople.this.mEvictionCount % 100 == 0) && com.android.server.notification.ValidateNotificationPeople.VERBOSE) {
                        android.util.Slog.i(com.android.server.notification.ValidateNotificationPeople.TAG, "mEvictionCount: " + com.android.server.notification.ValidateNotificationPeople.this.mEvictionCount);
                    }
                    com.android.server.notification.ValidateNotificationPeople.this.mPeopleCache.evictAll();
                    com.android.server.notification.ValidateNotificationPeople.this.mEvictionCount++;
                }
            };
            this.mBaseContext.getContentResolver().registerContentObserver(android.provider.ContactsContract.Contacts.CONTENT_URI, true, this.mObserver, -1);
        }
    }

    protected void initForTests(android.content.Context context, com.android.server.notification.NotificationUsageStats usageStats, android.util.LruCache<java.lang.String, com.android.server.notification.ValidateNotificationPeople.LookupResult> peopleCache) {
        this.mUserToContextMap = new android.util.ArrayMap();
        this.mBaseContext = context;
        this.mUsageStats = usageStats;
        this.mPeopleCache = peopleCache;
        this.mEnabled = true;
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public com.android.server.notification.RankingReconsideration process(com.android.server.notification.NotificationRecord record) {
        if (!this.mEnabled) {
            if (VERBOSE) {
                android.util.Slog.i(TAG, com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
            }
            return null;
        }
        if (record == null || record.getNotification() == null) {
            if (VERBOSE) {
                android.util.Slog.i(TAG, "skipping empty notification");
            }
            return null;
        }
        if (record.getUserId() == -1) {
            if (VERBOSE) {
                android.util.Slog.i(TAG, "skipping global notification");
            }
            return null;
        }
        android.content.Context context = getContextAsUser(record.getUser());
        if (context == null) {
            if (VERBOSE) {
                android.util.Slog.i(TAG, "skipping notification that lacks a context");
            }
            return null;
        }
        return validatePeople(context, record);
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setConfig(com.android.server.notification.RankingConfig config) {
    }

    @Override // com.android.server.notification.NotificationSignalExtractor
    public void setZenHelper(com.android.server.notification.ZenModeHelper helper) {
    }

    public float getContactAffinity(android.os.UserHandle userHandle, android.os.Bundle extras, int timeoutMs, float timeoutAffinity) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "checking affinity for " + userHandle);
        }
        if (extras == null) {
            return NONE;
        }
        java.lang.String key = java.lang.Long.toString(java.lang.System.nanoTime());
        float[] affinityOut = new float[1];
        android.content.Context context = getContextAsUser(userHandle);
        if (context == null) {
            return NONE;
        }
        final com.android.server.notification.ValidateNotificationPeople.PeopleRankingReconsideration prr = validatePeople(context, key, extras, null, affinityOut, null);
        float affinity = affinityOut[0];
        if (prr != null) {
            final java.util.concurrent.Semaphore s = new java.util.concurrent.Semaphore(0);
            android.os.AsyncTask.THREAD_POOL_EXECUTOR.execute(new java.lang.Runnable() { // from class: com.android.server.notification.ValidateNotificationPeople.2
                @Override // java.lang.Runnable
                public void run() {
                    prr.work();
                    s.release();
                }
            });
            try {
                if (!s.tryAcquire(timeoutMs, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    android.util.Slog.w(TAG, "Timeout while waiting for affinity: " + key + ". Returning timeoutAffinity=" + timeoutAffinity);
                    return timeoutAffinity;
                }
                return java.lang.Math.max(prr.getContactAffinity(), affinity);
            } catch (java.lang.InterruptedException e) {
                android.util.Slog.w(TAG, "InterruptedException while waiting for affinity: " + key + ". Returning affinity=" + affinity, e);
                return affinity;
            }
        }
        return affinity;
    }

    private android.content.Context getContextAsUser(android.os.UserHandle userHandle) {
        android.content.Context context = this.mUserToContextMap.get(java.lang.Integer.valueOf(userHandle.getIdentifier()));
        if (context == null) {
            try {
                context = this.mBaseContext.createPackageContextAsUser(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0, userHandle);
                this.mUserToContextMap.put(java.lang.Integer.valueOf(userHandle.getIdentifier()), context);
                return context;
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                android.util.Log.e(TAG, "failed to create package context for lookups", e);
                return context;
            }
        }
        return context;
    }

    protected com.android.server.notification.RankingReconsideration validatePeople(android.content.Context context, com.android.server.notification.NotificationRecord record) {
        boolean z;
        java.lang.String key = record.getKey();
        android.os.Bundle extras = record.getNotification().extras;
        float[] affinityOut = new float[1];
        android.util.ArraySet<java.lang.String> phoneNumbersOut = new android.util.ArraySet<>();
        com.android.server.notification.ValidateNotificationPeople.PeopleRankingReconsideration rr = validatePeople(context, key, extras, record.getPeopleOverride(), affinityOut, phoneNumbersOut);
        boolean z2 = false;
        float affinity = affinityOut[0];
        record.setContactAffinity(affinity);
        if (phoneNumbersOut.size() > 0) {
            record.mergePhoneNumbers(phoneNumbersOut);
        }
        if (rr == null) {
            com.android.server.notification.NotificationUsageStats notificationUsageStats = this.mUsageStats;
            if (affinity > NONE) {
                z = true;
            } else {
                z = false;
            }
            if (affinity == 1.0f) {
                z2 = true;
            }
            notificationUsageStats.registerPeopleAffinity(record, z, z2, true);
        } else {
            rr.setRecord(record);
        }
        return rr;
    }

    private com.android.server.notification.ValidateNotificationPeople.PeopleRankingReconsideration validatePeople(android.content.Context context, java.lang.String key, android.os.Bundle extras, java.util.List<java.lang.String> peopleOverride, float[] affinityOut, android.util.ArraySet<java.lang.String> phoneNumbersOut) {
        float affinity;
        android.util.ArraySet<java.lang.String> phoneNumbers;
        if (extras == null) {
            return null;
        }
        java.util.Set<java.lang.String> people = new android.util.ArraySet<>(peopleOverride);
        java.lang.String[] notificationPeople = getExtraPeople(extras);
        if (notificationPeople != null) {
            people.addAll(java.util.Arrays.asList(notificationPeople));
        }
        if (VERBOSE) {
            android.util.Slog.i(TAG, "Validating: " + key + " for " + context.getUserId());
        }
        java.util.LinkedList<java.lang.String> pendingLookups = new java.util.LinkedList<>();
        java.util.Iterator<java.lang.String> it = people.iterator();
        int personIdx = 0;
        float affinity2 = 0.0f;
        while (true) {
            if (!it.hasNext()) {
                affinity = affinity2;
                break;
            }
            java.lang.String handle = it.next();
            if (!android.text.TextUtils.isEmpty(handle)) {
                synchronized (this.mPeopleCache) {
                    java.lang.String cacheKey = getCacheKey(context.getUserId(), handle);
                    com.android.server.notification.ValidateNotificationPeople.LookupResult lookupResult = this.mPeopleCache.get(cacheKey);
                    if (lookupResult == null || lookupResult.isExpired()) {
                        pendingLookups.add(handle);
                    } else if (DEBUG) {
                        android.util.Slog.d(TAG, "using cached lookupResult");
                    }
                    if (lookupResult != null) {
                        affinity2 = java.lang.Math.max(affinity2, lookupResult.getAffinity());
                        if (phoneNumbersOut != null && (phoneNumbers = lookupResult.getPhoneNumbers()) != null && phoneNumbers.size() > 0) {
                            phoneNumbersOut.addAll((android.util.ArraySet<? extends java.lang.String>) phoneNumbers);
                        }
                    }
                }
                personIdx++;
                if (personIdx == 10) {
                    affinity = affinity2;
                    break;
                }
            }
        }
        affinityOut[0] = affinity;
        if (pendingLookups.isEmpty()) {
            if (VERBOSE) {
                android.util.Slog.i(TAG, "final affinity: " + affinity);
                return null;
            }
            return null;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Pending: future work scheduled for: " + key);
        }
        return new com.android.server.notification.ValidateNotificationPeople.PeopleRankingReconsideration(context, key, pendingLookups);
    }

    protected static java.lang.String getCacheKey(int userId, java.lang.String handle) {
        return java.lang.Integer.toString(userId) + ":" + handle;
    }

    public static java.lang.String[] getExtraPeople(android.os.Bundle extras) {
        java.lang.String[] peopleList = getExtraPeopleForKey(extras, "android.people.list");
        java.lang.String[] legacyPeople = getExtraPeopleForKey(extras, "android.people");
        return combineLists(legacyPeople, peopleList);
    }

    private static java.lang.String[] combineLists(java.lang.String[] first, java.lang.String[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        android.util.ArraySet<java.lang.String> people = new android.util.ArraySet<>(first.length + second.length);
        for (java.lang.String person : first) {
            people.add(person);
        }
        for (java.lang.String person2 : second) {
            people.add(person2);
        }
        return (java.lang.String[]) people.toArray(libcore.util.EmptyArray.STRING);
    }

    private static java.lang.String[] getExtraPeopleForKey(android.os.Bundle extras, java.lang.String key) {
        java.lang.Object people = extras.get(key);
        if (people instanceof java.lang.String[]) {
            return (java.lang.String[]) people;
        }
        if (people instanceof java.util.ArrayList) {
            java.util.ArrayList arrayList = (java.util.ArrayList) people;
            if (arrayList.isEmpty()) {
                return null;
            }
            if (arrayList.get(0) instanceof java.lang.String) {
                return (java.lang.String[]) arrayList.toArray(new java.lang.String[arrayList.size()]);
            }
            if (arrayList.get(0) instanceof java.lang.CharSequence) {
                int N = arrayList.size();
                java.lang.String[] array = new java.lang.String[N];
                for (int i = 0; i < N; i++) {
                    array[i] = ((java.lang.CharSequence) arrayList.get(i)).toString();
                }
                return array;
            }
            if (!(arrayList.get(0) instanceof android.app.Person)) {
                return null;
            }
            int N2 = arrayList.size();
            java.lang.String[] array2 = new java.lang.String[N2];
            for (int i2 = 0; i2 < N2; i2++) {
                array2[i2] = ((android.app.Person) arrayList.get(i2)).resolveToLegacyUri();
            }
            return array2;
        }
        if (people instanceof java.lang.String) {
            return new java.lang.String[]{(java.lang.String) people};
        }
        if (people instanceof char[]) {
            return new java.lang.String[]{new java.lang.String((char[]) people)};
        }
        if (people instanceof java.lang.CharSequence) {
            return new java.lang.String[]{((java.lang.CharSequence) people).toString()};
        }
        if (!(people instanceof java.lang.CharSequence[])) {
            return null;
        }
        java.lang.CharSequence[] charSeqArray = (java.lang.CharSequence[]) people;
        int N3 = charSeqArray.length;
        java.lang.String[] array3 = new java.lang.String[N3];
        for (int i3 = 0; i3 < N3; i3++) {
            array3[i3] = charSeqArray[i3].toString();
        }
        return array3;
    }

    protected static class LookupResult {
        private static final long CONTACT_REFRESH_MILLIS = 3600000;
        private float mAffinity = com.android.server.notification.ValidateNotificationPeople.NONE;
        private boolean mHasPhone = false;
        private java.lang.String mPhoneLookupKey = null;
        private android.util.ArraySet<java.lang.String> mPhoneNumbers = new android.util.ArraySet<>();
        private final long mExpireMillis = java.lang.System.currentTimeMillis() + 3600000;

        public void mergeContact(android.database.Cursor cursor) {
            this.mAffinity = java.lang.Math.max(this.mAffinity, 0.5f);
            int idIdx = cursor.getColumnIndex("_id");
            if (idIdx < 0) {
                android.util.Slog.i(com.android.server.notification.ValidateNotificationPeople.TAG, "invalid cursor: no _ID");
            } else {
                int id = cursor.getInt(idIdx);
                if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                    android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "contact _ID is: " + id);
                }
            }
            int lookupKeyIdx = cursor.getColumnIndex("lookup");
            if (lookupKeyIdx >= 0) {
                this.mPhoneLookupKey = cursor.getString(lookupKeyIdx);
                if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                    android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "contact LOOKUP_KEY is: " + this.mPhoneLookupKey);
                }
            } else if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "invalid cursor: no LOOKUP_KEY");
            }
            int starIdx = cursor.getColumnIndex("starred");
            if (starIdx >= 0) {
                boolean isStarred = cursor.getInt(starIdx) != 0;
                if (isStarred) {
                    this.mAffinity = java.lang.Math.max(this.mAffinity, 1.0f);
                }
                if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                    android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "contact STARRED is: " + isStarred);
                }
            } else if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "invalid cursor: no STARRED");
            }
            int hasPhoneIdx = cursor.getColumnIndex("has_phone_number");
            if (hasPhoneIdx >= 0) {
                this.mHasPhone = cursor.getInt(hasPhoneIdx) != 0;
                if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                    android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "contact HAS_PHONE_NUMBER is: " + this.mHasPhone);
                    return;
                }
                return;
            }
            if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "invalid cursor: no HAS_PHONE_NUMBER");
            }
        }

        public java.lang.String getPhoneLookupKey() {
            if (!this.mHasPhone) {
                return null;
            }
            return this.mPhoneLookupKey;
        }

        public void mergePhoneNumber(android.database.Cursor cursor) {
            int normalizedNumIdx = cursor.getColumnIndex("data4");
            if (normalizedNumIdx >= 0) {
                this.mPhoneNumbers.add(cursor.getString(normalizedNumIdx));
            } else if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "cursor data not found: no NORMALIZED_NUMBER");
            }
            int numIdx = cursor.getColumnIndex("data1");
            if (numIdx >= 0) {
                this.mPhoneNumbers.add(cursor.getString(numIdx));
            } else if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "cursor data not found: no NUMBER");
            }
        }

        public android.util.ArraySet<java.lang.String> getPhoneNumbers() {
            return this.mPhoneNumbers;
        }

        protected boolean isExpired() {
            return this.mExpireMillis < java.lang.System.currentTimeMillis();
        }

        private boolean isInvalid() {
            return this.mAffinity == com.android.server.notification.ValidateNotificationPeople.NONE || isExpired();
        }

        public float getAffinity() {
            if (isInvalid()) {
                return com.android.server.notification.ValidateNotificationPeople.NONE;
            }
            return this.mAffinity;
        }
    }

    class PeopleRankingReconsideration extends com.android.server.notification.RankingReconsideration {
        private float mContactAffinity;
        private final android.content.Context mContext;
        private final java.util.LinkedList<java.lang.String> mPendingLookups;
        private android.util.ArraySet<java.lang.String> mPhoneNumbers;
        private com.android.server.notification.NotificationRecord mRecord;

        private PeopleRankingReconsideration(android.content.Context context, java.lang.String key, java.util.LinkedList<java.lang.String> pendingLookups) {
            super(key);
            this.mContactAffinity = com.android.server.notification.ValidateNotificationPeople.NONE;
            this.mPhoneNumbers = null;
            this.mContext = context;
            this.mPendingLookups = pendingLookups;
        }

        @Override // com.android.server.notification.RankingReconsideration
        public void work() {
            com.android.server.notification.ValidateNotificationPeople.LookupResult lookupResult;
            if (com.android.server.notification.ValidateNotificationPeople.VERBOSE) {
                android.util.Slog.i(com.android.server.notification.ValidateNotificationPeople.TAG, "Executing: validation for: " + this.mKey);
            }
            long timeStartMs = java.lang.System.currentTimeMillis();
            for (java.lang.String handle : this.mPendingLookups) {
                java.lang.String cacheKey = com.android.server.notification.ValidateNotificationPeople.getCacheKey(this.mContext.getUserId(), handle);
                boolean cacheHit = false;
                synchronized (com.android.server.notification.ValidateNotificationPeople.this.mPeopleCache) {
                    lookupResult = (com.android.server.notification.ValidateNotificationPeople.LookupResult) com.android.server.notification.ValidateNotificationPeople.this.mPeopleCache.get(cacheKey);
                    if (lookupResult != null && !lookupResult.isExpired()) {
                        cacheHit = true;
                    }
                }
                if (!cacheHit) {
                    android.net.Uri uri = android.net.Uri.parse(handle);
                    if ("tel".equals(uri.getScheme())) {
                        if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                            android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "checking telephone URI: " + handle);
                        }
                        lookupResult = resolvePhoneContact(this.mContext, uri.getSchemeSpecificPart());
                    } else if ("mailto".equals(uri.getScheme())) {
                        if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                            android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "checking mailto URI: " + handle);
                        }
                        lookupResult = resolveEmailContact(this.mContext, uri.getSchemeSpecificPart());
                    } else if (handle.startsWith(android.provider.ContactsContract.Contacts.CONTENT_LOOKUP_URI.toString())) {
                        if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                            android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "checking lookup URI: " + handle);
                        }
                        lookupResult = searchContactsAndLookupNumbers(this.mContext, uri);
                    } else {
                        lookupResult = new com.android.server.notification.ValidateNotificationPeople.LookupResult();
                        if (!"name".equals(uri.getScheme())) {
                            android.util.Slog.w(com.android.server.notification.ValidateNotificationPeople.TAG, "unsupported URI " + handle);
                        }
                    }
                }
                if (lookupResult != null) {
                    if (!cacheHit) {
                        synchronized (com.android.server.notification.ValidateNotificationPeople.this.mPeopleCache) {
                            com.android.server.notification.ValidateNotificationPeople.this.mPeopleCache.put(cacheKey, lookupResult);
                        }
                    }
                    if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                        android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "lookup contactAffinity is " + lookupResult.getAffinity());
                    }
                    this.mContactAffinity = java.lang.Math.max(this.mContactAffinity, lookupResult.getAffinity());
                    if (lookupResult.getPhoneNumbers() != null) {
                        if (this.mPhoneNumbers == null) {
                            this.mPhoneNumbers = new android.util.ArraySet<>();
                        }
                        this.mPhoneNumbers.addAll((android.util.ArraySet<? extends java.lang.String>) lookupResult.getPhoneNumbers());
                    }
                } else if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                    android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "lookupResult is null");
                }
            }
            if (com.android.server.notification.ValidateNotificationPeople.DEBUG) {
                android.util.Slog.d(com.android.server.notification.ValidateNotificationPeople.TAG, "Validation finished in " + (java.lang.System.currentTimeMillis() - timeStartMs) + "ms");
            }
            if (this.mRecord != null) {
                com.android.server.notification.ValidateNotificationPeople.this.mUsageStats.registerPeopleAffinity(this.mRecord, this.mContactAffinity > com.android.server.notification.ValidateNotificationPeople.NONE, this.mContactAffinity == 1.0f, false);
            }
        }

        private static com.android.server.notification.ValidateNotificationPeople.LookupResult resolvePhoneContact(android.content.Context context, java.lang.String number) {
            android.net.Uri phoneUri = android.net.Uri.withAppendedPath(android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI, android.net.Uri.encode(number));
            return searchContacts(context, phoneUri);
        }

        private static com.android.server.notification.ValidateNotificationPeople.LookupResult resolveEmailContact(android.content.Context context, java.lang.String email) {
            android.net.Uri numberUri = android.net.Uri.withAppendedPath(android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, android.net.Uri.encode(email));
            return searchContacts(context, numberUri);
        }

        static com.android.server.notification.ValidateNotificationPeople.LookupResult searchContacts(android.content.Context context, android.net.Uri lookupUri) {
            com.android.server.notification.ValidateNotificationPeople.LookupResult lookupResult = new com.android.server.notification.ValidateNotificationPeople.LookupResult();
            android.net.Uri corpLookupUri = android.provider.ContactsContract.Contacts.createCorpLookupUriFromEnterpriseLookupUri(lookupUri);
            if (corpLookupUri == null) {
                addContacts(lookupResult, context, lookupUri);
            } else {
                addWorkContacts(lookupResult, context, corpLookupUri);
            }
            return lookupResult;
        }

        static com.android.server.notification.ValidateNotificationPeople.LookupResult searchContactsAndLookupNumbers(android.content.Context context, android.net.Uri lookupUri) {
            com.android.server.notification.ValidateNotificationPeople.LookupResult lookupResult = searchContacts(context, lookupUri);
            java.lang.String phoneLookupKey = lookupResult.getPhoneLookupKey();
            if (phoneLookupKey != null) {
                java.lang.String[] selectionArgs = {phoneLookupKey};
                try {
                    android.database.Cursor cursor = context.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, com.android.server.notification.ValidateNotificationPeople.PHONE_LOOKUP_PROJECTION, "lookup = ?", selectionArgs, null);
                    try {
                        if (cursor == null) {
                            android.util.Slog.w(com.android.server.notification.ValidateNotificationPeople.TAG, "Cursor is null when querying contact phone number.");
                            if (cursor != null) {
                                cursor.close();
                            }
                            return lookupResult;
                        }
                        while (cursor.moveToNext()) {
                            lookupResult.mergePhoneNumber(cursor);
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                    } finally {
                    }
                } catch (java.lang.Throwable t) {
                    android.util.Slog.w(com.android.server.notification.ValidateNotificationPeople.TAG, "Problem getting content resolver or querying phone numbers.", t);
                }
            }
            return lookupResult;
        }

        private static void addWorkContacts(com.android.server.notification.ValidateNotificationPeople.LookupResult lookupResult, android.content.Context context, android.net.Uri corpLookupUri) {
            int workUserId = findWorkUserId(context);
            if (workUserId == -1) {
                android.util.Slog.w(com.android.server.notification.ValidateNotificationPeople.TAG, "Work profile user ID not found for work contact: " + corpLookupUri);
            } else {
                android.net.Uri corpLookupUriWithUserId = android.content.ContentProvider.maybeAddUserId(corpLookupUri, workUserId);
                addContacts(lookupResult, context, corpLookupUriWithUserId);
            }
        }

        private static int findWorkUserId(android.content.Context context) {
            android.os.UserManager userManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
            int[] profileIds = userManager.getProfileIds(context.getUserId(), true);
            for (int profileId : profileIds) {
                if (userManager.isManagedProfile(profileId)) {
                    return profileId;
                }
            }
            return -1;
        }

        private static void addContacts(com.android.server.notification.ValidateNotificationPeople.LookupResult lookupResult, android.content.Context context, android.net.Uri uri) {
            try {
                android.database.Cursor c = context.getContentResolver().query(uri, com.android.server.notification.ValidateNotificationPeople.LOOKUP_PROJECTION, null, null, null);
                try {
                    if (c == null) {
                        android.util.Slog.w(com.android.server.notification.ValidateNotificationPeople.TAG, "Null cursor from contacts query.");
                        if (c != null) {
                            c.close();
                            return;
                        }
                        return;
                    }
                    while (c.moveToNext()) {
                        lookupResult.mergeContact(c);
                    }
                    if (c != null) {
                        c.close();
                    }
                } finally {
                }
            } catch (java.lang.Throwable t) {
                android.util.Slog.w(com.android.server.notification.ValidateNotificationPeople.TAG, "Problem getting content resolver or performing contacts query.", t);
            }
        }

        @Override // com.android.server.notification.RankingReconsideration
        public void applyChangesLocked(com.android.server.notification.NotificationRecord operand) {
            float affinityBound = operand.getContactAffinity();
            operand.setContactAffinity(java.lang.Math.max(this.mContactAffinity, affinityBound));
            if (com.android.server.notification.ValidateNotificationPeople.VERBOSE) {
                android.util.Slog.i(com.android.server.notification.ValidateNotificationPeople.TAG, "final affinity: " + operand.getContactAffinity());
            }
            operand.mergePhoneNumbers(this.mPhoneNumbers);
        }

        public float getContactAffinity() {
            return this.mContactAffinity;
        }

        public void setRecord(com.android.server.notification.NotificationRecord record) {
            this.mRecord = record;
        }
    }
}
