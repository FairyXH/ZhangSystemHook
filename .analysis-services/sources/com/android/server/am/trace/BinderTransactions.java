package com.android.server.am.trace;

/* JADX INFO: loaded from: classes.dex */
public class BinderTransactions {
    private static final java.lang.String BINDER_TRANSATION_FILE = "/dev/binderfs/binder_logs/state";
    private static final int DUMP_MAX_COUNT = 10;
    private static final java.lang.String REGEX_PATTERN = "\\s+(outgoing|incoming|pending)\\s+transaction.*from\\s+(\\d+):\\d+\\s+to\\s+(\\d+):\\d+\\s+.*";
    private static final java.lang.String TAG = "BinderTransactions";
    private int mCheckPid;
    private boolean mRecursiveMode;
    private java.util.Map<java.lang.Integer, java.util.HashSet<java.lang.Integer>> mLocalToRemotesMap = new java.util.HashMap();
    private java.util.Set<java.lang.Integer> mRemotePids = new java.util.HashSet();

    public BinderTransactions(boolean recursive) {
        this.mRecursiveMode = recursive;
    }

    public java.util.Set<java.lang.Integer> getTargetPidsStuckInBinder(int pid) {
        this.mCheckPid = pid;
        parseFromFile();
        if (!this.mRecursiveMode) {
            if (this.mLocalToRemotesMap.containsKey(java.lang.Integer.valueOf(pid))) {
                java.util.Iterator<java.lang.Integer> it = this.mLocalToRemotesMap.get(java.lang.Integer.valueOf(pid)).iterator();
                while (it.hasNext()) {
                    int remotePid = it.next().intValue();
                    this.mRemotePids.add(java.lang.Integer.valueOf(remotePid));
                }
            }
        } else {
            java.util.Set<java.lang.Integer> keyPids = this.mLocalToRemotesMap.keySet();
            for (java.lang.Integer p : keyPids) {
                if (p.intValue() == this.mCheckPid) {
                    this.mRemotePids.add(p);
                    findRemotePid(p.intValue());
                }
            }
        }
        final java.util.Set<java.lang.Integer> ret = new java.util.HashSet<>();
        this.mRemotePids.forEach(new java.util.function.Consumer() { // from class: com.android.server.am.trace.BinderTransactions$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.am.trace.BinderTransactions.lambda$getTargetPidsStuckInBinder$0(ret, (java.lang.Integer) obj);
            }
        });
        return ret;
    }

    static /* synthetic */ void lambda$getTargetPidsStuckInBinder$0(java.util.Set ret, java.lang.Integer p) {
        if (p.intValue() == 0 || ret.size() > 10) {
            return;
        }
        ret.add(p);
    }

    public void binderStateRead(java.io.File outputFile) {
        boolean binderfsNodePresent = false;
        java.io.BufferedReader in = null;
        try {
            android.util.Slog.i(TAG, "Collecting Binder Transaction Status Information");
            try {
                in = new java.io.BufferedReader(new java.io.FileReader(BINDER_TRANSATION_FILE));
                android.util.Slog.i(TAG, "Collecting Binder state file from binderfs");
                binderfsNodePresent = true;
            } catch (java.io.IOException e) {
                android.util.Slog.i(TAG, "Binderfs node not found, Trying to collect it from debugfs", e);
            }
            if (!binderfsNodePresent) {
                try {
                    in = new java.io.BufferedReader(new java.io.FileReader("/sys/kernel/debug/binder/state"));
                    android.util.Slog.i(TAG, "Collecting Binder state file from debugfs");
                } catch (java.io.IOException e2) {
                    android.util.Slog.i(TAG, "Debugfs node not found", e2);
                }
            }
            if (in == null) {
                return;
            }
            new java.text.SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", java.util.Locale.US).format(new java.util.Date());
            java.io.FileWriter out = new java.io.FileWriter(outputFile, true);
            while (true) {
                java.lang.String line = in.readLine();
                if (line != null) {
                    out.write(line);
                    out.write(10);
                } else {
                    in.close();
                    out.close();
                    return;
                }
            }
        } catch (java.io.IOException e3) {
            android.util.Slog.w(TAG, "Failed to collect state file", e3);
        }
    }

    private void findRemotePid(int s) {
        if (this.mLocalToRemotesMap.containsKey(java.lang.Integer.valueOf(s))) {
            for (java.lang.Integer p : this.mLocalToRemotesMap.get(java.lang.Integer.valueOf(s))) {
                if (!this.mRemotePids.contains(p)) {
                    this.mRemotePids.add(p);
                    findRemotePid(p.intValue());
                }
            }
        }
    }

    private void parseFromFile() {
        try {
            this.mLocalToRemotesMap.clear();
            this.mRemotePids.clear();
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(BINDER_TRANSATION_FILE));
            java.util.regex.Pattern outP = java.util.regex.Pattern.compile(REGEX_PATTERN);
            while (true) {
                java.lang.String line = in.readLine();
                if (line != null) {
                    java.util.regex.Matcher m = outP.matcher(line);
                    if (m.find()) {
                        addItem(java.lang.Integer.parseInt(m.group(2)), java.lang.Integer.parseInt(m.group(3)), m.group(1).equals("outgoing"));
                    }
                } else {
                    in.close();
                    return;
                }
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.w(TAG, "Unexpected FileNotFoundException", e);
        } catch (java.io.IOException e2) {
            android.util.Slog.w(TAG, "Unexpected IOException", e2);
        } catch (java.lang.NumberFormatException e3) {
            android.util.Slog.w(TAG, "Unexpected NumberFormatException ", e3);
        }
    }

    private void addItem(int spid, int tpid, boolean outgoing) {
        if (outgoing) {
            if (this.mLocalToRemotesMap.containsKey(java.lang.Integer.valueOf(spid))) {
                this.mLocalToRemotesMap.get(java.lang.Integer.valueOf(spid)).add(java.lang.Integer.valueOf(tpid));
                return;
            }
            java.util.HashSet<java.lang.Integer> set = new java.util.HashSet<>();
            set.add(java.lang.Integer.valueOf(tpid));
            this.mLocalToRemotesMap.put(java.lang.Integer.valueOf(spid), set);
            return;
        }
        if (tpid == this.mCheckPid) {
            this.mRemotePids.add(java.lang.Integer.valueOf(spid));
        }
    }
}
