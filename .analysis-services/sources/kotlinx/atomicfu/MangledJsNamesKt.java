package kotlinx.atomicfu;

/* JADX INFO: compiled from: MangledJsNames.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000e\n\u0002\b(\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0007\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\b\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\t\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u000b\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\f\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u000e\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u000f\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0010\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0011\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0012\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0013\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0014\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0015\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0016\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0017\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0018\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0019\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001a\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001b\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001c\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001d\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001e\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\u001f\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010 \u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010!\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010\"\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010#\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010$\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010%\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010&\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010'\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\"\u000e\u0010(\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000¨\u0006)"}, d2 = {"ADD_AND_GET", "", "ADD_AND_GET_LONG", "ARRAY_ELEMENT_GET", "ARRAY_SIZE", "ATOMIC_ARRAY_OF_NULLS", "ATOMIC_BOOLEAN_ARRAY", "ATOMIC_BOOLEAN_FACTORY", "ATOMIC_BOOLEAN_FACTORY_BINARY_COMPATIBILITY", "ATOMIC_INT_ARRAY", "ATOMIC_INT_FACTORY", "ATOMIC_INT_FACTORY_BINARY_COMPATIBILITY", "ATOMIC_LONG_ARRAY", "ATOMIC_LONG_FACTORY", "ATOMIC_LONG_FACTORY_BINARY_COMPATIBILITY", "ATOMIC_REF_ARRAY", "ATOMIC_REF_FACTORY", "ATOMIC_REF_FACTORY_BINARY_COMPATIBILITY", "ATOMIC_VALUE", "COMPARE_AND_SET", "DECREMENT_AND_GET", "DECREMENT_AND_GET_LONG", "GET_AND_ADD", "GET_AND_ADD_LONG", "GET_AND_DECREMENT", "GET_AND_DECREMENT_LONG", "GET_AND_INCREMENT", "GET_AND_INCREMENT_LONG", "GET_AND_SET", "INCREMENT_AND_GET", "INCREMENT_AND_GET_LONG", "REENTRANT_LOCK", "TRACE_APPEND_1", "TRACE_APPEND_2", "TRACE_APPEND_3", "TRACE_APPEND_4", "TRACE_BASE_CONSTRUCTOR", "TRACE_FACTORY_FUNCTION", "TRACE_FORMAT_CLASS", "TRACE_FORMAT_FORMAT_FUNCTION", "TRACE_NAMED", "external__kotlinx.atomicfu__linux_glibc_common__kotlinx_atomicfu"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class MangledJsNamesKt {
    public static final java.lang.String ADD_AND_GET = "atomicfu$addAndGet";
    public static final java.lang.String ADD_AND_GET_LONG = "atomicfu$addAndGet$long";
    public static final java.lang.String ARRAY_ELEMENT_GET = "atomicfu$get";
    public static final java.lang.String ARRAY_SIZE = "atomicfu$size";
    public static final java.lang.String ATOMIC_ARRAY_OF_NULLS = "atomicfu$AtomicRefArray$ofNulls";
    public static final java.lang.String ATOMIC_BOOLEAN_ARRAY = "atomicfu$AtomicBooleanArray$boolean";
    public static final java.lang.String ATOMIC_BOOLEAN_FACTORY = "atomic$boolean$";
    public static final java.lang.String ATOMIC_BOOLEAN_FACTORY_BINARY_COMPATIBILITY = "atomic$boolean$1";
    public static final java.lang.String ATOMIC_INT_ARRAY = "atomicfu$AtomicIntArray$int";
    public static final java.lang.String ATOMIC_INT_FACTORY = "atomic$int$";
    public static final java.lang.String ATOMIC_INT_FACTORY_BINARY_COMPATIBILITY = "atomic$int$1";
    public static final java.lang.String ATOMIC_LONG_ARRAY = "atomicfu$AtomicLongArray$long";
    public static final java.lang.String ATOMIC_LONG_FACTORY = "atomic$long$";
    public static final java.lang.String ATOMIC_LONG_FACTORY_BINARY_COMPATIBILITY = "atomic$long$1";
    public static final java.lang.String ATOMIC_REF_ARRAY = "atomicfu$AtomicRefArray$ref";
    public static final java.lang.String ATOMIC_REF_FACTORY = "atomic$ref$";
    public static final java.lang.String ATOMIC_REF_FACTORY_BINARY_COMPATIBILITY = "atomic$ref$1";
    public static final java.lang.String ATOMIC_VALUE = "kotlinx$atomicfu$value";
    public static final java.lang.String COMPARE_AND_SET = "atomicfu$compareAndSet";
    public static final java.lang.String DECREMENT_AND_GET = "atomicfu$decrementAndGet";
    public static final java.lang.String DECREMENT_AND_GET_LONG = "atomicfu$decrementAndGet$long";
    public static final java.lang.String GET_AND_ADD = "atomicfu$getAndAdd";
    public static final java.lang.String GET_AND_ADD_LONG = "atomicfu$getAndAdd$long";
    public static final java.lang.String GET_AND_DECREMENT = "atomicfu$getAndDecrement";
    public static final java.lang.String GET_AND_DECREMENT_LONG = "atomicfu$getAndDecrement$long";
    public static final java.lang.String GET_AND_INCREMENT = "atomicfu$getAndIncrement";
    public static final java.lang.String GET_AND_INCREMENT_LONG = "atomicfu$getAndIncrement$long";
    public static final java.lang.String GET_AND_SET = "atomicfu$getAndSet";
    public static final java.lang.String INCREMENT_AND_GET = "atomicfu$incrementAndGet";
    public static final java.lang.String INCREMENT_AND_GET_LONG = "atomicfu$incrementAndGet$long";
    public static final java.lang.String REENTRANT_LOCK = "atomicfu$reentrantLock";
    public static final java.lang.String TRACE_APPEND_1 = "atomicfu$Trace$append$1";
    public static final java.lang.String TRACE_APPEND_2 = "atomicfu$Trace$append$2";
    public static final java.lang.String TRACE_APPEND_3 = "atomicfu$Trace$append$3";
    public static final java.lang.String TRACE_APPEND_4 = "atomicfu$Trace$append$4";
    public static final java.lang.String TRACE_BASE_CONSTRUCTOR = "atomicfu$TraceBase";
    public static final java.lang.String TRACE_FACTORY_FUNCTION = "atomicfu$Trace";
    public static final java.lang.String TRACE_FORMAT_CLASS = "atomicfu$TraceFormat";
    public static final java.lang.String TRACE_FORMAT_FORMAT_FUNCTION = "atomicfu$TraceFormat$format";
    public static final java.lang.String TRACE_NAMED = "atomicfu$Trace$named";
}
