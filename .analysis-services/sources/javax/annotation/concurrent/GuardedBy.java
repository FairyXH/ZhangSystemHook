package javax.annotation.concurrent;

/* JADX INFO: loaded from: classes3.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
public @interface GuardedBy {
    java.lang.String value();
}
