package kotlin;

/* JADX INFO: compiled from: OptIn.kt */
/* JADX INFO: loaded from: classes3.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
@kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0087\u0002\u0018\u00002\u00020\u0001:\u0001\bB\u0014\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005R\u000f\u0010\u0004\u001a\u00020\u0005¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0006R\u000f\u0010\u0002\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0007¨\u0006\t"}, d2 = {"Lkotlin/RequiresOptIn;", "", "message", "", "level", "Lkotlin/RequiresOptIn$Level;", "()Lkotlin/RequiresOptIn$Level;", "()Ljava/lang/String;", "Level", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.annotation.Target(allowedTargets = {kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
@kotlin.annotation.Retention(kotlin.annotation.AnnotationRetention.BINARY)
public @interface RequiresOptIn {

    /* JADX INFO: compiled from: OptIn.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, d2 = {"Lkotlin/RequiresOptIn$Level;", "", "(Ljava/lang/String;I)V", "WARNING", "ERROR", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public enum Level {
        WARNING,
        ERROR;

        private static final /* synthetic */ kotlin.enums.EnumEntries $ENTRIES = kotlin.enums.EnumEntriesKt.enumEntries($VALUES);

        public static kotlin.enums.EnumEntries<kotlin.RequiresOptIn.Level> getEntries() {
            return $ENTRIES;
        }
    }

    kotlin.RequiresOptIn.Level level() default kotlin.RequiresOptIn.Level.ERROR;

    java.lang.String message() default "";
}
