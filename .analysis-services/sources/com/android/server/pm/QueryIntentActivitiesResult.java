package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class QueryIntentActivitiesResult {
    public boolean addInstant;
    public java.util.List<android.content.pm.ResolveInfo> answer;
    public java.util.List<android.content.pm.ResolveInfo> result;
    public boolean sortResult;

    QueryIntentActivitiesResult(java.util.List<android.content.pm.ResolveInfo> l) {
        this.sortResult = false;
        this.addInstant = false;
        this.result = null;
        this.answer = null;
        this.answer = l;
    }

    QueryIntentActivitiesResult(boolean s, boolean a, java.util.List<android.content.pm.ResolveInfo> l) {
        this.sortResult = false;
        this.addInstant = false;
        this.result = null;
        this.answer = null;
        this.sortResult = s;
        this.addInstant = a;
        this.result = l;
    }
}
