package kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: DebugMetadata.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0002\u001a\u000e\u0010\u0006\u001a\u0004\u0018\u00010\u0007*\u00020\bH\u0002\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0002\u001a\u0019\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b*\u00020\bH\u0001¢\u0006\u0002\u0010\r\u001a\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u000f*\u00020\bH\u0001¢\u0006\u0002\b\u0010\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"COROUTINES_DEBUG_METADATA_VERSION", "", "checkDebugMetadataVersion", "", "expected", "actual", "getDebugMetadataAnnotation", "Lkotlin/coroutines/jvm/internal/DebugMetadata;", "Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;", "getLabel", "getSpilledVariableFieldMapping", "", "", "(Lkotlin/coroutines/jvm/internal/BaseContinuationImpl;)[Ljava/lang/String;", "getStackTraceElementImpl", "Ljava/lang/StackTraceElement;", "getStackTraceElement", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DebugMetadataKt {
    private static final int COROUTINES_DEBUG_METADATA_VERSION = 1;

    public static final java.lang.StackTraceElement getStackTraceElement(kotlin.coroutines.jvm.internal.BaseContinuationImpl $this$getStackTraceElementImpl) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$getStackTraceElementImpl, "<this>");
        kotlin.coroutines.jvm.internal.DebugMetadata debugMetadata = getDebugMetadataAnnotation($this$getStackTraceElementImpl);
        if (debugMetadata == null) {
            return null;
        }
        checkDebugMetadataVersion(1, debugMetadata.v());
        int label = getLabel($this$getStackTraceElementImpl);
        int lineNumber = label < 0 ? -1 : debugMetadata.l()[label];
        java.lang.String moduleName = kotlin.coroutines.jvm.internal.ModuleNameRetriever.INSTANCE.getModuleName($this$getStackTraceElementImpl);
        java.lang.String moduleAndClass = moduleName == null ? debugMetadata.c() : moduleName + '/' + debugMetadata.c();
        return new java.lang.StackTraceElement(moduleAndClass, debugMetadata.m(), debugMetadata.f(), lineNumber);
    }

    private static final kotlin.coroutines.jvm.internal.DebugMetadata getDebugMetadataAnnotation(kotlin.coroutines.jvm.internal.BaseContinuationImpl $this$getDebugMetadataAnnotation) {
        return (kotlin.coroutines.jvm.internal.DebugMetadata) $this$getDebugMetadataAnnotation.getClass().getAnnotation(kotlin.coroutines.jvm.internal.DebugMetadata.class);
    }

    private static final int getLabel(kotlin.coroutines.jvm.internal.BaseContinuationImpl $this$getLabel) {
        try {
            java.lang.reflect.Field field = $this$getLabel.getClass().getDeclaredField("label");
            field.setAccessible(true);
            java.lang.Object obj = field.get($this$getLabel);
            java.lang.Integer num = obj instanceof java.lang.Integer ? (java.lang.Integer) obj : null;
            return (num != null ? num.intValue() : 0) - 1;
        } catch (java.lang.Exception e) {
            return -1;
        }
    }

    private static final void checkDebugMetadataVersion(int expected, int actual) {
        if (actual > expected) {
            throw new java.lang.IllegalStateException(("Debug metadata version mismatch. Expected: " + expected + ", got " + actual + ". Please update the Kotlin standard library.").toString());
        }
    }

    public static final java.lang.String[] getSpilledVariableFieldMapping(kotlin.coroutines.jvm.internal.BaseContinuationImpl $this$getSpilledVariableFieldMapping) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$getSpilledVariableFieldMapping, "<this>");
        kotlin.coroutines.jvm.internal.DebugMetadata debugMetadata = getDebugMetadataAnnotation($this$getSpilledVariableFieldMapping);
        if (debugMetadata == null) {
            return null;
        }
        checkDebugMetadataVersion(1, debugMetadata.v());
        java.util.ArrayList res = new java.util.ArrayList();
        int label = getLabel($this$getSpilledVariableFieldMapping);
        int[] iArrI = debugMetadata.i();
        int length = iArrI.length;
        for (int i = 0; i < length; i++) {
            int i2 = i;
            int labelOfIndex = iArrI[i];
            if (labelOfIndex == label) {
                res.add(debugMetadata.s()[i2]);
                res.add(debugMetadata.n()[i2]);
            }
        }
        java.util.ArrayList $this$toTypedArray$iv = res;
        return (java.lang.String[]) $this$toTypedArray$iv.toArray(new java.lang.String[0]);
    }
}
