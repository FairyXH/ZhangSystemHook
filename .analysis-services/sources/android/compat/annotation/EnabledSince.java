package android.compat.annotation;

/* JADX INFO: loaded from: classes.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
public @interface EnabledSince {
    int targetSdkVersion();
}
