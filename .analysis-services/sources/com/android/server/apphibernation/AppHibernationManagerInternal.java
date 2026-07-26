package com.android.server.apphibernation;

/* JADX INFO: loaded from: classes.dex */
public abstract class AppHibernationManagerInternal {
    public abstract boolean isHibernatingForUser(java.lang.String str, int i);

    public abstract boolean isHibernatingGlobally(java.lang.String str);

    public abstract boolean isOatArtifactDeletionEnabled();

    public abstract void setHibernatingForUser(java.lang.String str, int i, boolean z);

    public abstract void setHibernatingGlobally(java.lang.String str, boolean z);
}
