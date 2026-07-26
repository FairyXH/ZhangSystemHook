package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public abstract class IntentResolver<F, R> {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "IntentResolver";
    private static final boolean localLOGV = false;
    private static final boolean localVerificationLOGV = false;
    private static final java.util.Comparator mResolvePrioritySorter = new java.util.Comparator() { // from class: com.android.server.IntentResolver.1
        @Override // java.util.Comparator
        public int compare(java.lang.Object o1, java.lang.Object o2) {
            int q1 = ((android.content.IntentFilter) o1).getPriority();
            int q2 = ((android.content.IntentFilter) o2).getPriority();
            if (q1 > q2) {
                return -1;
            }
            return q1 < q2 ? 1 : 0;
        }
    }.thenComparing(new java.util.Comparator() { // from class: com.android.server.IntentResolver$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(java.lang.Object obj, java.lang.Object obj2) {
            return com.android.server.IntentResolver.lambda$static$0(obj, obj2);
        }
    });
    protected final android.util.ArraySet<F> mFilters = new android.util.ArraySet<>();
    private final android.util.ArrayMap<java.lang.String, F[]> mTypeToFilter = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, F[]> mBaseTypeToFilter = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, F[]> mWildTypeToFilter = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, F[]> mSchemeToFilter = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, F[]> mActionToFilter = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, F[]> mTypedActionToFilter = new android.util.ArrayMap<>();

    protected abstract android.content.IntentFilter getIntentFilter(F f);

    protected abstract boolean isPackageForFilter(java.lang.String str, F f);

    protected abstract F[] newArray(int i);

    public void addFilter(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, F f) {
        android.content.IntentFilter intentFilter = getIntentFilter(f);
        this.mFilters.add(f);
        int numS = register_intent_filter(f, intentFilter.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = register_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            register_intent_filter(f, intentFilter.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            register_intent_filter(f, intentFilter.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    public static boolean intentMatchesFilter(android.content.IntentFilter filter, android.content.Intent intent, java.lang.String resolvedType) {
        java.lang.String reason;
        boolean debug = (intent.getFlags() & 8) != 0;
        android.util.Printer logPrinter = debug ? new android.util.LogPrinter(2, TAG, 3) : null;
        if (debug) {
            android.util.Slog.v(TAG, "Intent: " + intent);
            android.util.Slog.v(TAG, "Matching against filter: " + filter);
            filter.dump(logPrinter, "  ");
        }
        int match = filter.match(intent.getAction(), resolvedType, intent.getScheme(), intent.getData(), intent.getCategories(), TAG);
        if (match >= 0) {
            if (debug) {
                android.util.Slog.v(TAG, "Filter matched!  match=0x" + java.lang.Integer.toHexString(match));
            }
            return true;
        }
        if (debug) {
            switch (match) {
                case -4:
                    reason = "category";
                    break;
                case -3:
                    reason = "action";
                    break;
                case -2:
                    reason = "data";
                    break;
                case -1:
                    reason = "type";
                    break;
                default:
                    reason = "unknown reason";
                    break;
            }
            android.util.Slog.v(TAG, "Filter did not match: " + reason);
        }
        return false;
    }

    private java.util.ArrayList<F> collectFilters(F[] array, android.content.IntentFilter matching) {
        F cur;
        java.util.ArrayList<F> res = null;
        if (array != null) {
            for (int i = 0; i < array.length && (cur = array[i]) != null; i++) {
                if (android.content.IntentFilter.filterEquals(getIntentFilter(cur), matching)) {
                    if (res == null) {
                        res = new java.util.ArrayList<>();
                    }
                    res.add(cur);
                }
            }
        }
        return res;
    }

    public java.util.ArrayList<F> findFilters(android.content.IntentFilter matching) {
        if (matching.countDataSchemes() == 1) {
            return collectFilters(this.mSchemeToFilter.get(matching.getDataScheme(0)), matching);
        }
        if (matching.countDataTypes() != 0 && matching.countActions() == 1) {
            return collectFilters(this.mTypedActionToFilter.get(matching.getAction(0)), matching);
        }
        if (matching.countDataTypes() == 0 && matching.countDataSchemes() == 0 && matching.countActions() == 1) {
            return collectFilters(this.mActionToFilter.get(matching.getAction(0)), matching);
        }
        java.util.ArrayList<F> res = null;
        for (F cur : this.mFilters) {
            if (android.content.IntentFilter.filterEquals(getIntentFilter(cur), matching)) {
                if (res == null) {
                    res = new java.util.ArrayList<>();
                }
                res.add(cur);
            }
        }
        return res;
    }

    public void removeFilter(F f) {
        removeFilterInternal(f);
        this.mFilters.remove(f);
    }

    protected void removeFilterInternal(F f) {
        android.content.IntentFilter intentFilter = getIntentFilter(f);
        int numS = unregister_intent_filter(f, intentFilter.schemesIterator(), this.mSchemeToFilter, "      Scheme: ");
        int numT = unregister_mime_types(f, "      Type: ");
        if (numS == 0 && numT == 0) {
            unregister_intent_filter(f, intentFilter.actionsIterator(), this.mActionToFilter, "      Action: ");
        }
        if (numT != 0) {
            unregister_intent_filter(f, intentFilter.actionsIterator(), this.mTypedActionToFilter, "      TypedAction: ");
        }
    }

    boolean dumpMap(java.io.PrintWriter out, java.lang.String titlePrefix, java.lang.String title, java.lang.String prefix, android.util.ArrayMap<java.lang.String, F[]> map, java.lang.String packageName, boolean printFilter, boolean collapseDuplicates) {
        java.lang.String str;
        android.util.ArrayMap<java.lang.Object, android.util.MutableInt> found;
        F filter;
        java.lang.String str2;
        boolean printedSomething;
        android.util.Printer printer;
        boolean printedHeader;
        android.util.Printer printer2;
        F filter2;
        boolean printedHeader2;
        boolean printedSomething2;
        java.lang.String str3;
        com.android.server.IntentResolver intentResolver = this;
        java.io.PrintWriter printWriter = out;
        android.util.ArrayMap<java.lang.String, F[]> arrayMap = map;
        java.lang.String str4 = "  ";
        java.lang.String eprefix = prefix + "  ";
        java.lang.String fprefix = prefix + "    ";
        android.util.ArrayMap<java.lang.Object, android.util.MutableInt> found2 = new android.util.ArrayMap<>();
        int mapi = 0;
        android.util.Printer printer3 = null;
        boolean printedSomething3 = false;
        java.lang.String title2 = title;
        while (mapi < map.size()) {
            F[] a = arrayMap.valueAt(mapi);
            int N = a.length;
            boolean printedHeader3 = false;
            if (!collapseDuplicates || printFilter) {
                str = str4;
                found = found2;
                int i = 0;
                title2 = title2;
                printer3 = printer3;
                boolean printedHeader4 = false;
                printedSomething3 = printedSomething3;
                while (i < N) {
                    F filter3 = a[i];
                    if (filter3 != null) {
                        if (packageName != null) {
                            filter = filter3;
                            if (!intentResolver.isPackageForFilter(packageName, filter)) {
                                str2 = str;
                            }
                            i++;
                            intentResolver = this;
                            arrayMap = map;
                            str = str2;
                            printWriter = out;
                        } else {
                            filter = filter3;
                        }
                        if (title2 != null) {
                            out.print(titlePrefix);
                            printWriter.println(title2);
                            title2 = null;
                        }
                        if (!printedHeader4) {
                            printWriter.print(eprefix);
                            printWriter.print(arrayMap.keyAt(mapi));
                            printWriter.println(":");
                            printedHeader4 = true;
                        }
                        intentResolver.dumpFilter(printWriter, fprefix, filter);
                        if (!printFilter) {
                            str2 = str;
                            printedSomething3 = true;
                        } else {
                            if (printer3 == null) {
                                printer3 = new android.util.PrintWriterPrinter(printWriter);
                            }
                            str2 = str;
                            intentResolver.getIntentFilter(filter).dump(printer3, fprefix + str2);
                            printedSomething3 = true;
                        }
                        i++;
                        intentResolver = this;
                        arrayMap = map;
                        str = str2;
                        printWriter = out;
                    }
                }
            } else {
                found2.clear();
                java.lang.String title3 = title2;
                int i2 = 0;
                while (true) {
                    if (i2 < N) {
                        F filter4 = a[i2];
                        if (filter4 == null) {
                            str = str4;
                            printedSomething = printedSomething3;
                            printer = printer3;
                            printedHeader = printedHeader3;
                            break;
                        }
                        if (packageName != null) {
                            printer2 = printer3;
                            filter2 = filter4;
                            if (!intentResolver.isPackageForFilter(packageName, filter2)) {
                                str3 = str4;
                                printedSomething2 = printedSomething3;
                                printedHeader2 = printedHeader3;
                            }
                            i2++;
                            printer3 = printer2;
                            printedHeader3 = printedHeader2;
                            printedSomething3 = printedSomething2;
                            str4 = str3;
                        } else {
                            printer2 = printer3;
                            filter2 = filter4;
                        }
                        printedHeader2 = printedHeader3;
                        java.lang.Object label = intentResolver.filterToLabel(filter2);
                        int index = found2.indexOfKey(label);
                        printedSomething2 = printedSomething3;
                        if (index < 0) {
                            str3 = str4;
                            found2.put(label, new android.util.MutableInt(1));
                        } else {
                            str3 = str4;
                            found2.valueAt(index).value++;
                        }
                        i2++;
                        printer3 = printer2;
                        printedHeader3 = printedHeader2;
                        printedSomething3 = printedSomething2;
                        str4 = str3;
                    } else {
                        str = str4;
                        printedSomething = printedSomething3;
                        printer = printer3;
                        printedHeader = printedHeader3;
                        break;
                    }
                }
                int i3 = 0;
                title2 = title3;
                boolean printedHeader5 = printedHeader;
                printedSomething3 = printedSomething;
                while (i3 < found2.size()) {
                    if (title2 != null) {
                        out.print(titlePrefix);
                        printWriter.println(title2);
                        title2 = null;
                    }
                    if (!printedHeader5) {
                        printWriter.print(eprefix);
                        printWriter.print(arrayMap.keyAt(mapi));
                        printWriter.println(":");
                        printedHeader5 = true;
                    }
                    printedSomething3 = true;
                    intentResolver.dumpFilterLabel(printWriter, fprefix, found2.keyAt(i3), found2.valueAt(i3).value);
                    i3++;
                    found2 = found2;
                }
                found = found2;
                printer3 = printer;
            }
            mapi++;
            intentResolver = this;
            arrayMap = map;
            str4 = str;
            found2 = found;
            printWriter = out;
        }
        return printedSomething3;
    }

    void writeProtoMap(android.util.proto.ProtoOutputStream proto, long fieldId, android.util.ArrayMap<java.lang.String, F[]> map) {
        int N = map.size();
        for (int mapi = 0; mapi < N; mapi++) {
            long token = proto.start(fieldId);
            proto.write(1138166333441L, map.keyAt(mapi));
            for (F f : map.valueAt(mapi)) {
                if (f != null) {
                    proto.write(2237677961218L, f.toString());
                }
            }
            proto.end(token);
        }
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        writeProtoMap(proto, 2246267895809L, this.mTypeToFilter);
        writeProtoMap(proto, 2246267895810L, this.mBaseTypeToFilter);
        writeProtoMap(proto, 2246267895811L, this.mWildTypeToFilter);
        writeProtoMap(proto, 2246267895812L, this.mSchemeToFilter);
        writeProtoMap(proto, 2246267895813L, this.mActionToFilter);
        writeProtoMap(proto, 2246267895814L, this.mTypedActionToFilter);
        proto.end(token);
    }

    public boolean dump(java.io.PrintWriter out, java.lang.String title, java.lang.String prefix, java.lang.String packageName, boolean printFilter, boolean collapseDuplicates) {
        java.lang.String innerPrefix = prefix + "  ";
        java.lang.String sepPrefix = "\n" + prefix;
        java.lang.String curPrefix = title + "\n" + prefix;
        if (dumpMap(out, curPrefix, "Full MIME Types:", innerPrefix, this.mTypeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Base MIME Types:", innerPrefix, this.mBaseTypeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Wild MIME Types:", innerPrefix, this.mWildTypeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Schemes:", innerPrefix, this.mSchemeToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "Non-Data Actions:", innerPrefix, this.mActionToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        if (dumpMap(out, curPrefix, "MIME Typed Actions:", innerPrefix, this.mTypedActionToFilter, packageName, printFilter, collapseDuplicates)) {
            curPrefix = sepPrefix;
        }
        return curPrefix == sepPrefix;
    }

    private class IteratorWrapper implements java.util.Iterator<F> {
        private F mCur;
        private final java.util.Iterator<F> mI;

        IteratorWrapper(java.util.Iterator<F> it) {
            this.mI = it;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mI.hasNext();
        }

        @Override // java.util.Iterator
        public F next() {
            F next = this.mI.next();
            this.mCur = next;
            return next;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.mCur != null) {
                com.android.server.IntentResolver.this.removeFilterInternal(this.mCur);
            }
            this.mI.remove();
        }
    }

    public java.util.Iterator<F> filterIterator() {
        return new com.android.server.IntentResolver.IteratorWrapper(this.mFilters.iterator());
    }

    public java.util.Set<F> filterSet() {
        return java.util.Collections.unmodifiableSet(this.mFilters);
    }

    public java.util.List<R> queryIntentFromList(com.android.server.pm.Computer computer, android.content.Intent intent, java.lang.String resolvedType, boolean defaultOnly, java.util.ArrayList<F[]> listCut, int userId, long customFlags) {
        if ("android.intent.action.PROCESS_TEXT".equals(intent.getAction()) && com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.ignoreProcessText()) {
            return java.util.Collections.emptyList();
        }
        java.util.ArrayList<R> resultList = new java.util.ArrayList<>();
        boolean debug = (intent.getFlags() & 8) != 0;
        android.util.FastImmutableArraySet<java.lang.String> categories = getFastIntentCategories(intent);
        java.lang.String scheme = intent.getScheme();
        int i = 0;
        for (int N = listCut.size(); i < N; N = N) {
            buildResolveList(computer, intent, categories, debug, defaultOnly, resolvedType, scheme, listCut.get(i), resultList, userId, customFlags);
            i++;
        }
        filterResults(resultList);
        sortResults(resultList);
        return resultList;
    }

    public java.util.List<R> queryIntent(com.android.server.pm.snapshot.PackageDataSnapshot snapshot, android.content.Intent intent, java.lang.String resolvedType, boolean defaultOnly, int userId) {
        return queryIntent(snapshot, intent, resolvedType, defaultOnly, userId, 0L);
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x01db  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0239  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x023e  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0276  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x029a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final java.util.List<R> queryIntent(com.android.server.pm.snapshot.PackageDataSnapshot r25, android.content.Intent r26, java.lang.String r27, boolean r28, int r29, long r30) {
        /*
            Method dump skipped, instruction units count: 708
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.IntentResolver.queryIntent(com.android.server.pm.snapshot.PackageDataSnapshot, android.content.Intent, java.lang.String, boolean, int, long):java.util.List");
    }

    protected boolean allowFilterResult(F filter, java.util.List<R> dest) {
        return true;
    }

    protected boolean isFilterStopped(com.android.server.pm.Computer computer, F filter, int userId) {
        return false;
    }

    protected boolean isFilterVerified(F filter) {
        return getIntentFilter(filter).isVerified();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected R newResult(com.android.server.pm.Computer computer, F f, int match, int userId, long customFlags) {
        return f;
    }

    protected void sortResults(java.util.List<R> results) {
        java.util.Collections.sort(results, mResolvePrioritySorter);
    }

    protected void filterResults(java.util.List<R> results) {
    }

    protected void dumpFilter(java.io.PrintWriter out, java.lang.String prefix, F filter) {
        out.print(prefix);
        out.println(filter);
    }

    protected java.lang.Object filterToLabel(F filter) {
        return "IntentFilter";
    }

    protected void dumpFilterLabel(java.io.PrintWriter out, java.lang.String prefix, java.lang.Object label, int count) {
        out.print(prefix);
        out.print(label);
        out.print(": ");
        out.println(count);
    }

    private final void addFilter(android.util.ArrayMap<java.lang.String, F[]> map, java.lang.String name, F filter) {
        F[] array = map.get(name);
        if (array == null) {
            F[] array2 = newArray(2);
            map.put(name, array2);
            array2[0] = filter;
            return;
        }
        int N = array.length;
        int i = N;
        while (i > 0 && array[i - 1] == null) {
            i--;
        }
        if (i >= N) {
            F[] newa = newArray((N * 3) / 2);
            java.lang.System.arraycopy(array, 0, newa, 0, N);
            newa[N] = filter;
            map.put(name, newa);
            return;
        }
        array[i] = filter;
    }

    private final int register_mime_types(F filter, java.lang.String prefix) {
        java.util.Iterator<java.lang.String> i = getIntentFilter(filter).typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            java.lang.String name = i.next();
            num++;
            java.lang.String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                name = name + "/*";
            }
            addFilter(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                addFilter(this.mBaseTypeToFilter, baseName, filter);
            } else {
                addFilter(this.mWildTypeToFilter, baseName, filter);
            }
        }
        return num;
    }

    private final int unregister_mime_types(F filter, java.lang.String prefix) {
        java.util.Iterator<java.lang.String> i = getIntentFilter(filter).typesIterator();
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            java.lang.String name = i.next();
            num++;
            java.lang.String baseName = name;
            int slashpos = name.indexOf(47);
            if (slashpos > 0) {
                baseName = name.substring(0, slashpos).intern();
            } else {
                name = name + "/*";
            }
            remove_all_objects(this.mTypeToFilter, name, filter);
            if (slashpos > 0) {
                remove_all_objects(this.mBaseTypeToFilter, baseName, filter);
            } else {
                remove_all_objects(this.mWildTypeToFilter, baseName, filter);
            }
        }
        return num;
    }

    protected final int register_intent_filter(F filter, java.util.Iterator<java.lang.String> i, android.util.ArrayMap<java.lang.String, F[]> dest, java.lang.String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            java.lang.String name = i.next();
            num++;
            addFilter(dest, name, filter);
        }
        return num;
    }

    protected final int unregister_intent_filter(F filter, java.util.Iterator<java.lang.String> i, android.util.ArrayMap<java.lang.String, F[]> dest, java.lang.String prefix) {
        if (i == null) {
            return 0;
        }
        int num = 0;
        while (i.hasNext()) {
            java.lang.String name = i.next();
            num++;
            remove_all_objects(dest, name, filter);
        }
        return num;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void remove_all_objects(android.util.ArrayMap<java.lang.String, F[]> arrayMap, java.lang.String name, F object) {
        F[] fArr = arrayMap.get(name);
        if (fArr != null) {
            int LAST = fArr.length - 1;
            while (LAST >= 0 && fArr[LAST] == null) {
                LAST--;
            }
            for (int idx = LAST; idx >= 0; idx--) {
                java.lang.Object obj = fArr[idx];
                if (obj != null && getIntentFilter(obj) == getIntentFilter(object)) {
                    int remain = LAST - idx;
                    if (remain > 0) {
                        java.lang.System.arraycopy(fArr, idx + 1, fArr, idx, remain);
                    }
                    fArr[LAST] = null;
                    LAST--;
                }
            }
            if (LAST < 0) {
                arrayMap.remove(name);
            } else if (LAST < fArr.length / 2) {
                F[] fArrNewArray = newArray(LAST + 2);
                java.lang.System.arraycopy(fArr, 0, fArrNewArray, 0, LAST + 1);
                arrayMap.put(name, fArrNewArray);
            }
        }
    }

    private static android.util.FastImmutableArraySet<java.lang.String> getFastIntentCategories(android.content.Intent intent) {
        java.util.Set<java.lang.String> categories = intent.getCategories();
        if (categories == null) {
            return null;
        }
        return new android.util.FastImmutableArraySet<>((java.lang.String[]) categories.toArray(new java.lang.String[categories.size()]));
    }

    private void buildResolveList(com.android.server.pm.Computer computer, android.content.Intent intent, android.util.FastImmutableArraySet<java.lang.String> categories, boolean debug, boolean defaultOnly, java.lang.String resolvedType, java.lang.String scheme, F[] src, java.util.List<R> dest, int userId, long customFlags) {
        android.util.Printer logPrinter;
        com.android.internal.util.FastPrintWriter fastPrintWriter;
        int i;
        int N;
        java.lang.String action;
        com.android.internal.util.FastPrintWriter fastPrintWriter2;
        java.lang.String reason;
        F[] fArr = src;
        java.lang.String action2 = intent.getAction();
        android.net.Uri data = intent.getData();
        java.lang.String packageName = intent.getPackage();
        boolean excludingStopped = intent.isExcludingStopped();
        if (debug) {
            android.util.Printer logPrinter2 = new android.util.LogPrinter(2, TAG, 3);
            logPrinter = logPrinter2;
            fastPrintWriter = new com.android.internal.util.FastPrintWriter(logPrinter2);
        } else {
            logPrinter = null;
            fastPrintWriter = null;
        }
        int N2 = fArr != null ? fArr.length : 0;
        boolean hasNonDefaults = false;
        int i2 = 0;
        while (i2 < N2) {
            F filter = fArr[i2];
            if (filter != null) {
                if (debug) {
                    android.util.Slog.v(TAG, "Matching against filter " + filter);
                }
                if (excludingStopped && isFilterStopped(computer, filter, userId)) {
                    if (debug) {
                        android.util.Slog.v(TAG, "  Filter's target is stopped; skipping");
                        i = i2;
                        N = N2;
                        action = action2;
                        fastPrintWriter2 = fastPrintWriter;
                    } else {
                        i = i2;
                        N = N2;
                        action = action2;
                        fastPrintWriter2 = fastPrintWriter;
                    }
                } else if (packageName != null && !isPackageForFilter(packageName, filter)) {
                    if (debug) {
                        android.util.Slog.v(TAG, "  Filter is not from package " + packageName + "; skipping");
                        i = i2;
                        N = N2;
                        action = action2;
                        fastPrintWriter2 = fastPrintWriter;
                    } else {
                        i = i2;
                        N = N2;
                        action = action2;
                        fastPrintWriter2 = fastPrintWriter;
                    }
                } else {
                    android.content.IntentFilter intentFilter = getIntentFilter(filter);
                    if (intentFilter.getAutoVerify() && debug) {
                        android.util.Slog.v(TAG, "  Filter verified: " + isFilterVerified(filter));
                        int authorities = intentFilter.countDataAuthorities();
                        int z = 0;
                        while (z < authorities) {
                            android.util.Slog.v(TAG, "   " + intentFilter.getDataAuthority(z).getHost());
                            z++;
                            authorities = authorities;
                            i2 = i2;
                        }
                        i = i2;
                    } else {
                        i = i2;
                    }
                    if (!allowFilterResult(filter, dest)) {
                        if (debug) {
                            android.util.Slog.v(TAG, "  Filter's target already added");
                            N = N2;
                            action = action2;
                            fastPrintWriter2 = fastPrintWriter;
                        } else {
                            N = N2;
                            action = action2;
                            fastPrintWriter2 = fastPrintWriter;
                        }
                    } else {
                        java.lang.String str = action2;
                        N = N2;
                        action = action2;
                        fastPrintWriter2 = fastPrintWriter;
                        int match = intentFilter.match(str, resolvedType, scheme, data, categories, TAG);
                        if (match >= 0) {
                            if (debug) {
                                android.util.Slog.v(TAG, "  Filter matched!  match=0x" + java.lang.Integer.toHexString(match) + " hasDefault=" + intentFilter.hasCategory("android.intent.category.DEFAULT"));
                            }
                            if (!defaultOnly || intentFilter.hasCategory("android.intent.category.DEFAULT")) {
                                R oneResult = newResult(computer, filter, match, userId, customFlags);
                                if (debug) {
                                    android.util.Slog.v(TAG, "    Created result: " + oneResult);
                                }
                                if (oneResult != null) {
                                    dest.add(oneResult);
                                    if (debug) {
                                        dumpFilter(fastPrintWriter2, "    ", filter);
                                        fastPrintWriter2.flush();
                                        intentFilter.dump(logPrinter, "    ");
                                    }
                                }
                            } else {
                                hasNonDefaults = true;
                            }
                        } else if (debug) {
                            switch (match) {
                                case -4:
                                    reason = "category";
                                    break;
                                case -3:
                                    reason = "action";
                                    break;
                                case -2:
                                    reason = "data";
                                    break;
                                case -1:
                                    reason = "type";
                                    break;
                                default:
                                    reason = "unknown reason";
                                    break;
                            }
                            android.util.Slog.v(TAG, "  Filter did not match: " + reason);
                        }
                    }
                }
                i2 = i + 1;
                fArr = src;
                fastPrintWriter = fastPrintWriter2;
                N2 = N;
                action2 = action;
            } else if (!debug && hasNonDefaults) {
                if (dest.size() == 0) {
                    android.util.Slog.v(TAG, "resolveIntent failed: found match, but none with CATEGORY_DEFAULT");
                    return;
                } else {
                    if (dest.size() > 1) {
                        android.util.Slog.v(TAG, "resolveIntent: multiple matches, only some with CATEGORY_DEFAULT");
                        return;
                    }
                    return;
                }
            }
        }
        if (!debug) {
        }
    }

    static /* synthetic */ int lambda$static$0(java.lang.Object o1, java.lang.Object o2) {
        int q1 = ((android.content.IntentFilter) o1).mIntentFilterExt.getOriginPriority();
        int q2 = ((android.content.IntentFilter) o2).mIntentFilterExt.getOriginPriority();
        if (q1 > q2) {
            return -1;
        }
        return q1 < q2 ? 1 : 0;
    }

    protected F snapshot(F f) {
        return f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void copyInto(android.util.ArrayMap<java.lang.String, F[]> arrayMap, android.util.ArrayMap<java.lang.String, F[]> r) {
        int end = r.size();
        arrayMap.clear();
        arrayMap.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            F[] val = r.valueAt(i);
            java.lang.String key = r.keyAt(i);
            java.lang.Object[] objArrCopyOf = java.util.Arrays.copyOf(val, val.length);
            for (int j = 0; j < objArrCopyOf.length; j++) {
                objArrCopyOf[j] = snapshot(objArrCopyOf[j]);
            }
            arrayMap.put(key, objArrCopyOf);
        }
    }

    protected void copyInto(android.util.ArraySet<F> l, android.util.ArraySet<F> r) {
        l.clear();
        int end = r.size();
        l.ensureCapacity(end);
        for (int i = 0; i < end; i++) {
            l.append(snapshot(r.valueAt(i)));
        }
    }

    protected void copyFrom(com.android.server.IntentResolver orig) {
        copyInto(this.mFilters, orig.mFilters);
        copyInto(this.mTypeToFilter, orig.mTypeToFilter);
        copyInto(this.mBaseTypeToFilter, orig.mBaseTypeToFilter);
        copyInto(this.mWildTypeToFilter, orig.mWildTypeToFilter);
        copyInto(this.mSchemeToFilter, orig.mSchemeToFilter);
        copyInto(this.mActionToFilter, orig.mActionToFilter);
        copyInto(this.mTypedActionToFilter, orig.mTypedActionToFilter);
    }
}
