package kotlin.time;

/* JADX INFO: compiled from: Duration.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b-\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0087@\u0018\u0000 ¦\u00012\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0002¦\u0001B\u0011\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\"\u0010D\u001a\u00020\u00002\u0006\u0010E\u001a\u00020\u00032\u0006\u0010F\u001a\u00020\u0003H\u0002ø\u0001\u0000¢\u0006\u0004\bG\u0010HJ\u0018\u0010I\u001a\u00020\t2\u0006\u0010J\u001a\u00020\u0000H\u0096\u0002¢\u0006\u0004\bK\u0010LJ\u001b\u0010M\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u000fH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bO\u0010PJ\u001b\u0010M\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\tH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bO\u0010QJ\u0018\u0010M\u001a\u00020\u000f2\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\bR\u0010SJ\u001a\u0010T\u001a\u00020U2\b\u0010J\u001a\u0004\u0018\u00010VHÖ\u0003¢\u0006\u0004\bW\u0010XJ\u0010\u0010Y\u001a\u00020\tHÖ\u0001¢\u0006\u0004\bZ\u0010\rJ\r\u0010[\u001a\u00020U¢\u0006\u0004\b\\\u0010]J\u000f\u0010^\u001a\u00020UH\u0002¢\u0006\u0004\b_\u0010]J\u000f\u0010`\u001a\u00020UH\u0002¢\u0006\u0004\ba\u0010]J\r\u0010b\u001a\u00020U¢\u0006\u0004\bc\u0010]J\r\u0010d\u001a\u00020U¢\u0006\u0004\be\u0010]J\r\u0010f\u001a\u00020U¢\u0006\u0004\bg\u0010]J\u0018\u0010h\u001a\u00020\u00002\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\bi\u0010jJ\u0018\u0010k\u001a\u00020\u00002\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002¢\u0006\u0004\bl\u0010jJ\u001b\u0010m\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u000fH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bn\u0010PJ\u001b\u0010m\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\tH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\bn\u0010QJ\u009d\u0001\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2u\u0010q\u001aq\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(u\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(v\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0rH\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\bz\u0010{J\u0088\u0001\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2`\u0010q\u001a\\\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(v\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0|H\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\bz\u0010}Js\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2K\u0010q\u001aG\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0~H\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0004\bz\u0010\u007fJ`\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p27\u0010q\u001a3\u0012\u0013\u0012\u00110\u0003¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t¢\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0\u0080\u0001H\u0086\bø\u0001\u0001\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0005\bz\u0010\u0081\u0001J\u0019\u0010\u0082\u0001\u001a\u00020\u000f2\u0007\u0010\u0083\u0001\u001a\u00020=¢\u0006\u0006\b\u0084\u0001\u0010\u0085\u0001J\u0019\u0010\u0086\u0001\u001a\u00020\t2\u0007\u0010\u0083\u0001\u001a\u00020=¢\u0006\u0006\b\u0087\u0001\u0010\u0088\u0001J\u0011\u0010\u0089\u0001\u001a\u00030\u008a\u0001¢\u0006\u0006\b\u008b\u0001\u0010\u008c\u0001J\u0019\u0010\u008d\u0001\u001a\u00020\u00032\u0007\u0010\u0083\u0001\u001a\u00020=¢\u0006\u0006\b\u008e\u0001\u0010\u008f\u0001J\u0011\u0010\u0090\u0001\u001a\u00020\u0003H\u0007¢\u0006\u0005\b\u0091\u0001\u0010\u0005J\u0011\u0010\u0092\u0001\u001a\u00020\u0003H\u0007¢\u0006\u0005\b\u0093\u0001\u0010\u0005J\u0013\u0010\u0094\u0001\u001a\u00030\u008a\u0001H\u0016¢\u0006\u0006\b\u0095\u0001\u0010\u008c\u0001J%\u0010\u0094\u0001\u001a\u00030\u008a\u00012\u0007\u0010\u0083\u0001\u001a\u00020=2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\t¢\u0006\u0006\b\u0095\u0001\u0010\u0097\u0001J\u001e\u0010\u0098\u0001\u001a\u00020\u00002\u0007\u0010\u0083\u0001\u001a\u00020=H\u0000ø\u0001\u0000¢\u0006\u0006\b\u0099\u0001\u0010\u008f\u0001J\u0015\u0010\u009a\u0001\u001a\u00020\u0000H\u0086\u0002ø\u0001\u0000¢\u0006\u0005\b\u009b\u0001\u0010\u0005JK\u0010\u009c\u0001\u001a\u00030\u009d\u0001*\b0\u009e\u0001j\u0003`\u009f\u00012\u0007\u0010 \u0001\u001a\u00020\t2\u0007\u0010¡\u0001\u001a\u00020\t2\u0007\u0010¢\u0001\u001a\u00020\t2\b\u0010\u0083\u0001\u001a\u00030\u008a\u00012\u0007\u0010£\u0001\u001a\u00020UH\u0002¢\u0006\u0006\b¤\u0001\u0010¥\u0001R\u0014\u0010\u0006\u001a\u00020\u00008Fø\u0001\u0000¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u001a\u0010\b\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\u000b\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0014\u0010\u000b\u001a\u0004\b\u0015\u0010\u0012R\u001a\u0010\u0016\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0017\u0010\u000b\u001a\u0004\b\u0018\u0010\u0012R\u001a\u0010\u0019\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001a\u0010\u000b\u001a\u0004\b\u001b\u0010\u0012R\u001a\u0010\u001c\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u000b\u001a\u0004\b\u001e\u0010\u0012R\u001a\u0010\u001f\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b \u0010\u000b\u001a\u0004\b!\u0010\u0012R\u001a\u0010\"\u001a\u00020\u000f8FX\u0087\u0004¢\u0006\f\u0012\u0004\b#\u0010\u000b\u001a\u0004\b$\u0010\u0012R\u0011\u0010%\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b&\u0010\u0005R\u0011\u0010'\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b(\u0010\u0005R\u0011\u0010)\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b*\u0010\u0005R\u0011\u0010+\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b,\u0010\u0005R\u0011\u0010-\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b.\u0010\u0005R\u0011\u0010/\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b0\u0010\u0005R\u0011\u00101\u001a\u00020\u00038F¢\u0006\u0006\u001a\u0004\b2\u0010\u0005R\u001a\u00103\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b4\u0010\u000b\u001a\u0004\b5\u0010\rR\u001a\u00106\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b7\u0010\u000b\u001a\u0004\b8\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u00109\u001a\u00020\t8@X\u0081\u0004¢\u0006\f\u0012\u0004\b:\u0010\u000b\u001a\u0004\b;\u0010\rR\u0014\u0010<\u001a\u00020=8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b>\u0010?R\u0015\u0010@\u001a\u00020\t8Â\u0002X\u0082\u0004¢\u0006\u0006\u001a\u0004\bA\u0010\rR\u0014\u0010B\u001a\u00020\u00038BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bC\u0010\u0005\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u009920\u0001¨\u0006§\u0001"}, d2 = {"Lkotlin/time/Duration;", "", "rawValue", "", "constructor-impl", "(J)J", "absoluteValue", "getAbsoluteValue-UwyO8pc", "hoursComponent", "", "getHoursComponent$annotations", "()V", "getHoursComponent-impl", "(J)I", "inDays", "", "getInDays$annotations", "getInDays-impl", "(J)D", "inHours", "getInHours$annotations", "getInHours-impl", "inMicroseconds", "getInMicroseconds$annotations", "getInMicroseconds-impl", "inMilliseconds", "getInMilliseconds$annotations", "getInMilliseconds-impl", "inMinutes", "getInMinutes$annotations", "getInMinutes-impl", "inNanoseconds", "getInNanoseconds$annotations", "getInNanoseconds-impl", "inSeconds", "getInSeconds$annotations", "getInSeconds-impl", "inWholeDays", "getInWholeDays-impl", "inWholeHours", "getInWholeHours-impl", "inWholeMicroseconds", "getInWholeMicroseconds-impl", "inWholeMilliseconds", "getInWholeMilliseconds-impl", "inWholeMinutes", "getInWholeMinutes-impl", "inWholeNanoseconds", "getInWholeNanoseconds-impl", "inWholeSeconds", "getInWholeSeconds-impl", "minutesComponent", "getMinutesComponent$annotations", "getMinutesComponent-impl", "nanosecondsComponent", "getNanosecondsComponent$annotations", "getNanosecondsComponent-impl", "secondsComponent", "getSecondsComponent$annotations", "getSecondsComponent-impl", "storageUnit", "Lkotlin/time/DurationUnit;", "getStorageUnit-impl", "(J)Lkotlin/time/DurationUnit;", "unitDiscriminator", "getUnitDiscriminator-impl", "value", "getValue-impl", "addValuesMixedRanges", "thisMillis", "otherNanos", "addValuesMixedRanges-UwyO8pc", "(JJJ)J", "compareTo", "other", "compareTo-LRDsOJo", "(JJ)I", "div", "scale", "div-UwyO8pc", "(JD)J", "(JI)J", "div-LRDsOJo", "(JJ)D", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "hashCode-impl", "isFinite", "isFinite-impl", "(J)Z", "isInMillis", "isInMillis-impl", "isInNanos", "isInNanos-impl", "isInfinite", "isInfinite-impl", "isNegative", "isNegative-impl", "isPositive", "isPositive-impl", "minus", "minus-LRDsOJo", "(JJ)J", "plus", "plus-LRDsOJo", "times", "times-UwyO8pc", "toComponents", "T", "action", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "days", "hours", "minutes", "seconds", "nanoseconds", "toComponents-impl", "(JLkotlin/jvm/functions/Function5;)Ljava/lang/Object;", "Lkotlin/Function4;", "(JLkotlin/jvm/functions/Function4;)Ljava/lang/Object;", "Lkotlin/Function3;", "(JLkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "Lkotlin/Function2;", "(JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "toDouble", "unit", "toDouble-impl", "(JLkotlin/time/DurationUnit;)D", "toInt", "toInt-impl", "(JLkotlin/time/DurationUnit;)I", "toIsoString", "", "toIsoString-impl", "(J)Ljava/lang/String;", "toLong", "toLong-impl", "(JLkotlin/time/DurationUnit;)J", "toLongMilliseconds", "toLongMilliseconds-impl", "toLongNanoseconds", "toLongNanoseconds-impl", "toString", "toString-impl", "decimals", "(JLkotlin/time/DurationUnit;I)Ljava/lang/String;", "truncateTo", "truncateTo-UwyO8pc$kotlin_stdlib", "unaryMinus", "unaryMinus-UwyO8pc", "appendFractional", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "whole", "fractional", "fractionalSize", "isoZeroes", "appendFractional-impl", "(JLjava/lang/StringBuilder;IIILjava/lang/String;Z)V", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@kotlin.jvm.JvmInline
public final class Duration implements java.lang.Comparable<kotlin.time.Duration> {
    private final long rawValue;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.time.Duration.Companion INSTANCE = new kotlin.time.Duration.Companion(null);
    private static final long ZERO = m12633constructorimpl(0);
    private static final long INFINITE = kotlin.time.DurationKt.durationOfMillis(4611686018427387903L);
    private static final long NEG_INFINITE = kotlin.time.DurationKt.durationOfMillis(-4611686018427387903L);

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ kotlin.time.Duration m12631boximpl(long j) {
        return new kotlin.time.Duration(j);
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m12637equalsimpl(long j, java.lang.Object obj) {
        return (obj instanceof kotlin.time.Duration) && j == ((kotlin.time.Duration) obj).getRawValue();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m12638equalsimpl0(long j, long j2) {
        return j == j2;
    }

    public static /* synthetic */ void getHoursComponent$annotations() {
    }

    @kotlin.Deprecated(message = "Use inWholeDays property instead or convert toDouble(DAYS) if a double value is required.", replaceWith = @kotlin.ReplaceWith(expression = "toDouble(DurationUnit.DAYS)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInDays$annotations() {
    }

    @kotlin.Deprecated(message = "Use inWholeHours property instead or convert toDouble(HOURS) if a double value is required.", replaceWith = @kotlin.ReplaceWith(expression = "toDouble(DurationUnit.HOURS)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInHours$annotations() {
    }

    @kotlin.Deprecated(message = "Use inWholeMicroseconds property instead or convert toDouble(MICROSECONDS) if a double value is required.", replaceWith = @kotlin.ReplaceWith(expression = "toDouble(DurationUnit.MICROSECONDS)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInMicroseconds$annotations() {
    }

    @kotlin.Deprecated(message = "Use inWholeMilliseconds property instead or convert toDouble(MILLISECONDS) if a double value is required.", replaceWith = @kotlin.ReplaceWith(expression = "toDouble(DurationUnit.MILLISECONDS)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInMilliseconds$annotations() {
    }

    @kotlin.Deprecated(message = "Use inWholeMinutes property instead or convert toDouble(MINUTES) if a double value is required.", replaceWith = @kotlin.ReplaceWith(expression = "toDouble(DurationUnit.MINUTES)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInMinutes$annotations() {
    }

    @kotlin.Deprecated(message = "Use inWholeNanoseconds property instead or convert toDouble(NANOSECONDS) if a double value is required.", replaceWith = @kotlin.ReplaceWith(expression = "toDouble(DurationUnit.NANOSECONDS)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInNanoseconds$annotations() {
    }

    @kotlin.Deprecated(message = "Use inWholeSeconds property instead or convert toDouble(SECONDS) if a double value is required.", replaceWith = @kotlin.ReplaceWith(expression = "toDouble(DurationUnit.SECONDS)", imports = {}))
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.5")
    public static /* synthetic */ void getInSeconds$annotations() {
    }

    public static /* synthetic */ void getMinutesComponent$annotations() {
    }

    public static /* synthetic */ void getNanosecondsComponent$annotations() {
    }

    public static /* synthetic */ void getSecondsComponent$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m12661hashCodeimpl(long j) {
        return java.lang.Long.hashCode(j);
    }

    public boolean equals(java.lang.Object obj) {
        return m12637equalsimpl(this.rawValue, obj);
    }

    public int hashCode() {
        return m12661hashCodeimpl(this.rawValue);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ long getRawValue() {
        return this.rawValue;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(kotlin.time.Duration duration) {
        return m12687compareToLRDsOJo(duration.getRawValue());
    }

    private /* synthetic */ Duration(long rawValue) {
        this.rawValue = rawValue;
    }

    /* JADX INFO: renamed from: getValue-impl, reason: not valid java name */
    private static final long m12660getValueimpl(long arg0) {
        return arg0 >> 1;
    }

    /* JADX INFO: renamed from: getUnitDiscriminator-impl, reason: not valid java name */
    private static final int m12659getUnitDiscriminatorimpl(long arg0) {
        return ((int) arg0) & 1;
    }

    /* JADX INFO: renamed from: isInNanos-impl, reason: not valid java name */
    private static final boolean m12664isInNanosimpl(long arg0) {
        return (((int) arg0) & 1) == 0;
    }

    /* JADX INFO: renamed from: isInMillis-impl, reason: not valid java name */
    private static final boolean m12663isInMillisimpl(long arg0) {
        return (((int) arg0) & 1) == 1;
    }

    /* JADX INFO: renamed from: getStorageUnit-impl, reason: not valid java name */
    private static final kotlin.time.DurationUnit m12658getStorageUnitimpl(long arg0) {
        return m12664isInNanosimpl(arg0) ? kotlin.time.DurationUnit.NANOSECONDS : kotlin.time.DurationUnit.MILLISECONDS;
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static long m12633constructorimpl(long rawValue) {
        if (kotlin.time.DurationJvmKt.getDurationAssertionsEnabled()) {
            if (m12664isInNanosimpl(rawValue)) {
                if (!new kotlin.ranges.LongRange(-4611686018426999999L, 4611686018426999999L).contains(m12660getValueimpl(rawValue))) {
                    throw new java.lang.AssertionError(m12660getValueimpl(rawValue) + " ns is out of nanoseconds range");
                }
            } else {
                if (!new kotlin.ranges.LongRange(-4611686018427387903L, 4611686018427387903L).contains(m12660getValueimpl(rawValue))) {
                    throw new java.lang.AssertionError(m12660getValueimpl(rawValue) + " ms is out of milliseconds range");
                }
                if (new kotlin.ranges.LongRange(-4611686018426L, 4611686018426L).contains(m12660getValueimpl(rawValue))) {
                    throw new java.lang.AssertionError(m12660getValueimpl(rawValue) + " ms is denormalized");
                }
            }
        }
        return rawValue;
    }

    /* JADX INFO: compiled from: Duration.kt */
    @kotlin.Metadata(d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010*\u001a\u00020\r2\u0006\u0010+\u001a\u00020\r2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020-H\u0007J\u001a\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0011J\u001a\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0014J\u001a\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b/\u0010\u0017J\u001a\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b0\u0010\u0011J\u001a\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b0\u0010\u0014J\u001a\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b0\u0010\u0017J\u001a\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0011J\u001a\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0014J\u001a\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b1\u0010\u0017J\u001a\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b2\u0010\u0011J\u001a\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b2\u0010\u0014J\u001a\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b2\u0010\u0017J\u001a\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b3\u0010\u0011J\u001a\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b3\u0010\u0014J\u001a\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b3\u0010\u0017J\u001a\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b4\u0010\u0011J\u001a\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b4\u0010\u0014J\u001a\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b4\u0010\u0017J\u0018\u00105\u001a\u00020\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0004\b7\u00108J\u0018\u00109\u001a\u00020\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0004\b:\u00108J\u0018\u0010;\u001a\u0004\u0018\u00010\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0002\b<J\u0018\u0010=\u001a\u0004\u0018\u00010\u00042\u0006\u0010+\u001a\u000206ø\u0001\u0000¢\u0006\u0002\b>J\u001a\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007ø\u0001\u0000¢\u0006\u0004\b?\u0010\u0011J\u001a\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007ø\u0001\u0000¢\u0006\u0004\b?\u0010\u0014J\u001a\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007ø\u0001\u0000¢\u0006\u0004\b?\u0010\u0017R\u0016\u0010\u0003\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006R\u0019\u0010\b\u001a\u00020\u0004X\u0080\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\t\u0010\u0006R\u0016\u0010\n\u001a\u00020\u0004ø\u0001\u0000¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u000b\u0010\u0006R\"\u0010\f\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R\"\u0010\f\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u000e\u0010\u0013\u001a\u0004\b\u0010\u0010\u0014R\"\u0010\f\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u000e\u0010\u0016\u001a\u0004\b\u0010\u0010\u0017R\"\u0010\u0018\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u0019\u0010\u000f\u001a\u0004\b\u001a\u0010\u0011R\"\u0010\u0018\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u0019\u0010\u0013\u001a\u0004\b\u001a\u0010\u0014R\"\u0010\u0018\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u0019\u0010\u0016\u001a\u0004\b\u001a\u0010\u0017R\"\u0010\u001b\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001c\u0010\u000f\u001a\u0004\b\u001d\u0010\u0011R\"\u0010\u001b\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001c\u0010\u0013\u001a\u0004\b\u001d\u0010\u0014R\"\u0010\u001b\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001c\u0010\u0016\u001a\u0004\b\u001d\u0010\u0017R\"\u0010\u001e\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001f\u0010\u000f\u001a\u0004\b \u0010\u0011R\"\u0010\u001e\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001f\u0010\u0013\u001a\u0004\b \u0010\u0014R\"\u0010\u001e\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\u001f\u0010\u0016\u001a\u0004\b \u0010\u0017R\"\u0010!\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\"\u0010\u000f\u001a\u0004\b#\u0010\u0011R\"\u0010!\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\"\u0010\u0013\u001a\u0004\b#\u0010\u0014R\"\u0010!\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b\"\u0010\u0016\u001a\u0004\b#\u0010\u0017R\"\u0010$\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b%\u0010\u000f\u001a\u0004\b&\u0010\u0011R\"\u0010$\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b%\u0010\u0013\u001a\u0004\b&\u0010\u0014R\"\u0010$\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b%\u0010\u0016\u001a\u0004\b&\u0010\u0017R\"\u0010'\u001a\u00020\u0004*\u00020\r8Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b(\u0010\u000f\u001a\u0004\b)\u0010\u0011R\"\u0010'\u001a\u00020\u0004*\u00020\u00128Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b(\u0010\u0013\u001a\u0004\b)\u0010\u0014R\"\u0010'\u001a\u00020\u0004*\u00020\u00158Æ\u0002X\u0087\u0004ø\u0001\u0000¢\u0006\f\u0012\u0004\b(\u0010\u0016\u001a\u0004\b)\u0010\u0017\u0082\u0002\u0004\n\u0002\b!¨\u0006@"}, d2 = {"Lkotlin/time/Duration$Companion;", "", "()V", "INFINITE", "Lkotlin/time/Duration;", "getINFINITE-UwyO8pc", "()J", "J", "NEG_INFINITE", "getNEG_INFINITE-UwyO8pc$kotlin_stdlib", "ZERO", "getZERO-UwyO8pc", "days", "", "getDays-UwyO8pc$annotations", "(D)V", "getDays-UwyO8pc", "(D)J", "", "(I)V", "(I)J", "", "(J)V", "(J)J", "hours", "getHours-UwyO8pc$annotations", "getHours-UwyO8pc", "microseconds", "getMicroseconds-UwyO8pc$annotations", "getMicroseconds-UwyO8pc", "milliseconds", "getMilliseconds-UwyO8pc$annotations", "getMilliseconds-UwyO8pc", "minutes", "getMinutes-UwyO8pc$annotations", "getMinutes-UwyO8pc", "nanoseconds", "getNanoseconds-UwyO8pc$annotations", "getNanoseconds-UwyO8pc", "seconds", "getSeconds-UwyO8pc$annotations", "getSeconds-UwyO8pc", "convert", "value", "sourceUnit", "Lkotlin/time/DurationUnit;", "targetUnit", "days-UwyO8pc", "hours-UwyO8pc", "microseconds-UwyO8pc", "milliseconds-UwyO8pc", "minutes-UwyO8pc", "nanoseconds-UwyO8pc", "parse", "", "parse-UwyO8pc", "(Ljava/lang/String;)J", "parseIsoString", "parseIsoString-UwyO8pc", "parseIsoStringOrNull", "parseIsoStringOrNull-FghU774", "parseOrNull", "parseOrNull-FghU774", "seconds-UwyO8pc", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12692getDaysUwyO8pc$annotations(double d) {
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12693getDaysUwyO8pc$annotations(int i) {
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12694getDaysUwyO8pc$annotations(long j) {
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12698getHoursUwyO8pc$annotations(double d) {
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12699getHoursUwyO8pc$annotations(int i) {
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12700getHoursUwyO8pc$annotations(long j) {
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12704getMicrosecondsUwyO8pc$annotations(double d) {
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12705getMicrosecondsUwyO8pc$annotations(int i) {
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12706getMicrosecondsUwyO8pc$annotations(long j) {
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12710getMillisecondsUwyO8pc$annotations(double d) {
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12711getMillisecondsUwyO8pc$annotations(int i) {
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12712getMillisecondsUwyO8pc$annotations(long j) {
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12716getMinutesUwyO8pc$annotations(double d) {
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12717getMinutesUwyO8pc$annotations(int i) {
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12718getMinutesUwyO8pc$annotations(long j) {
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12722getNanosecondsUwyO8pc$annotations(double d) {
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12723getNanosecondsUwyO8pc$annotations(int i) {
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12724getNanosecondsUwyO8pc$annotations(long j) {
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12728getSecondsUwyO8pc$annotations(double d) {
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12729getSecondsUwyO8pc$annotations(int i) {
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc$annotations, reason: not valid java name */
        public static /* synthetic */ void m12730getSecondsUwyO8pc$annotations(long j) {
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: getZERO-UwyO8pc, reason: not valid java name */
        public final long m12736getZEROUwyO8pc() {
            return kotlin.time.Duration.ZERO;
        }

        /* JADX INFO: renamed from: getINFINITE-UwyO8pc, reason: not valid java name */
        public final long m12734getINFINITEUwyO8pc() {
            return kotlin.time.Duration.INFINITE;
        }

        /* JADX INFO: renamed from: getNEG_INFINITE-UwyO8pc$kotlin_stdlib, reason: not valid java name */
        public final long m12735getNEG_INFINITEUwyO8pc$kotlin_stdlib() {
            return kotlin.time.Duration.NEG_INFINITE;
        }

        public final double convert(double value, kotlin.time.DurationUnit sourceUnit, kotlin.time.DurationUnit targetUnit) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sourceUnit, "sourceUnit");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(targetUnit, "targetUnit");
            return kotlin.time.DurationUnitKt.convertDurationUnit(value, sourceUnit, targetUnit);
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc, reason: not valid java name */
        private final long m12720getNanosecondsUwyO8pc(int $this$nanoseconds) {
            return kotlin.time.DurationKt.toDuration($this$nanoseconds, kotlin.time.DurationUnit.NANOSECONDS);
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc, reason: not valid java name */
        private final long m12721getNanosecondsUwyO8pc(long $this$nanoseconds) {
            return kotlin.time.DurationKt.toDuration($this$nanoseconds, kotlin.time.DurationUnit.NANOSECONDS);
        }

        /* JADX INFO: renamed from: getNanoseconds-UwyO8pc, reason: not valid java name */
        private final long m12719getNanosecondsUwyO8pc(double $this$nanoseconds) {
            return kotlin.time.DurationKt.toDuration($this$nanoseconds, kotlin.time.DurationUnit.NANOSECONDS);
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc, reason: not valid java name */
        private final long m12702getMicrosecondsUwyO8pc(int $this$microseconds) {
            return kotlin.time.DurationKt.toDuration($this$microseconds, kotlin.time.DurationUnit.MICROSECONDS);
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc, reason: not valid java name */
        private final long m12703getMicrosecondsUwyO8pc(long $this$microseconds) {
            return kotlin.time.DurationKt.toDuration($this$microseconds, kotlin.time.DurationUnit.MICROSECONDS);
        }

        /* JADX INFO: renamed from: getMicroseconds-UwyO8pc, reason: not valid java name */
        private final long m12701getMicrosecondsUwyO8pc(double $this$microseconds) {
            return kotlin.time.DurationKt.toDuration($this$microseconds, kotlin.time.DurationUnit.MICROSECONDS);
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc, reason: not valid java name */
        private final long m12708getMillisecondsUwyO8pc(int $this$milliseconds) {
            return kotlin.time.DurationKt.toDuration($this$milliseconds, kotlin.time.DurationUnit.MILLISECONDS);
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc, reason: not valid java name */
        private final long m12709getMillisecondsUwyO8pc(long $this$milliseconds) {
            return kotlin.time.DurationKt.toDuration($this$milliseconds, kotlin.time.DurationUnit.MILLISECONDS);
        }

        /* JADX INFO: renamed from: getMilliseconds-UwyO8pc, reason: not valid java name */
        private final long m12707getMillisecondsUwyO8pc(double $this$milliseconds) {
            return kotlin.time.DurationKt.toDuration($this$milliseconds, kotlin.time.DurationUnit.MILLISECONDS);
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc, reason: not valid java name */
        private final long m12726getSecondsUwyO8pc(int $this$seconds) {
            return kotlin.time.DurationKt.toDuration($this$seconds, kotlin.time.DurationUnit.SECONDS);
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc, reason: not valid java name */
        private final long m12727getSecondsUwyO8pc(long $this$seconds) {
            return kotlin.time.DurationKt.toDuration($this$seconds, kotlin.time.DurationUnit.SECONDS);
        }

        /* JADX INFO: renamed from: getSeconds-UwyO8pc, reason: not valid java name */
        private final long m12725getSecondsUwyO8pc(double $this$seconds) {
            return kotlin.time.DurationKt.toDuration($this$seconds, kotlin.time.DurationUnit.SECONDS);
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc, reason: not valid java name */
        private final long m12714getMinutesUwyO8pc(int $this$minutes) {
            return kotlin.time.DurationKt.toDuration($this$minutes, kotlin.time.DurationUnit.MINUTES);
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc, reason: not valid java name */
        private final long m12715getMinutesUwyO8pc(long $this$minutes) {
            return kotlin.time.DurationKt.toDuration($this$minutes, kotlin.time.DurationUnit.MINUTES);
        }

        /* JADX INFO: renamed from: getMinutes-UwyO8pc, reason: not valid java name */
        private final long m12713getMinutesUwyO8pc(double $this$minutes) {
            return kotlin.time.DurationKt.toDuration($this$minutes, kotlin.time.DurationUnit.MINUTES);
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc, reason: not valid java name */
        private final long m12696getHoursUwyO8pc(int $this$hours) {
            return kotlin.time.DurationKt.toDuration($this$hours, kotlin.time.DurationUnit.HOURS);
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc, reason: not valid java name */
        private final long m12697getHoursUwyO8pc(long $this$hours) {
            return kotlin.time.DurationKt.toDuration($this$hours, kotlin.time.DurationUnit.HOURS);
        }

        /* JADX INFO: renamed from: getHours-UwyO8pc, reason: not valid java name */
        private final long m12695getHoursUwyO8pc(double $this$hours) {
            return kotlin.time.DurationKt.toDuration($this$hours, kotlin.time.DurationUnit.HOURS);
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc, reason: not valid java name */
        private final long m12690getDaysUwyO8pc(int $this$days) {
            return kotlin.time.DurationKt.toDuration($this$days, kotlin.time.DurationUnit.DAYS);
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc, reason: not valid java name */
        private final long m12691getDaysUwyO8pc(long $this$days) {
            return kotlin.time.DurationKt.toDuration($this$days, kotlin.time.DurationUnit.DAYS);
        }

        /* JADX INFO: renamed from: getDays-UwyO8pc, reason: not valid java name */
        private final long m12689getDaysUwyO8pc(double $this$days) {
            return kotlin.time.DurationKt.toDuration($this$days, kotlin.time.DurationUnit.DAYS);
        }

        @kotlin.Deprecated(message = "Use 'Int.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: nanoseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12750nanosecondsUwyO8pc(int value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.NANOSECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Long.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: nanoseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12751nanosecondsUwyO8pc(long value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.NANOSECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Double.nanoseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.nanoseconds", imports = {"kotlin.time.Duration.Companion.nanoseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: nanoseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12749nanosecondsUwyO8pc(double value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.NANOSECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Int.microseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: microseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12741microsecondsUwyO8pc(int value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MICROSECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Long.microseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: microseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12742microsecondsUwyO8pc(long value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MICROSECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Double.microseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.microseconds", imports = {"kotlin.time.Duration.Companion.microseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: microseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12740microsecondsUwyO8pc(double value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MICROSECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Int.milliseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: milliseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12744millisecondsUwyO8pc(int value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MILLISECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Long.milliseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: milliseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12745millisecondsUwyO8pc(long value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MILLISECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Double.milliseconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.milliseconds", imports = {"kotlin.time.Duration.Companion.milliseconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: milliseconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12743millisecondsUwyO8pc(double value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MILLISECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Int.seconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: seconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12757secondsUwyO8pc(int value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.SECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Long.seconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: seconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12758secondsUwyO8pc(long value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.SECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Double.seconds' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.seconds", imports = {"kotlin.time.Duration.Companion.seconds"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: seconds-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12756secondsUwyO8pc(double value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.SECONDS);
        }

        @kotlin.Deprecated(message = "Use 'Int.minutes' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: minutes-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12747minutesUwyO8pc(int value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MINUTES);
        }

        @kotlin.Deprecated(message = "Use 'Long.minutes' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: minutes-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12748minutesUwyO8pc(long value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MINUTES);
        }

        @kotlin.Deprecated(message = "Use 'Double.minutes' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.minutes", imports = {"kotlin.time.Duration.Companion.minutes"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: minutes-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12746minutesUwyO8pc(double value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.MINUTES);
        }

        @kotlin.Deprecated(message = "Use 'Int.hours' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: hours-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12738hoursUwyO8pc(int value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.HOURS);
        }

        @kotlin.Deprecated(message = "Use 'Long.hours' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: hours-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12739hoursUwyO8pc(long value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.HOURS);
        }

        @kotlin.Deprecated(message = "Use 'Double.hours' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.hours", imports = {"kotlin.time.Duration.Companion.hours"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: hours-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12737hoursUwyO8pc(double value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.HOURS);
        }

        @kotlin.Deprecated(message = "Use 'Int.days' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.days", imports = {"kotlin.time.Duration.Companion.days"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: days-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12732daysUwyO8pc(int value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.DAYS);
        }

        @kotlin.Deprecated(message = "Use 'Long.days' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.days", imports = {"kotlin.time.Duration.Companion.days"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: days-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12733daysUwyO8pc(long value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.DAYS);
        }

        @kotlin.Deprecated(message = "Use 'Double.days' extension property from Duration.Companion instead.", replaceWith = @kotlin.ReplaceWith(expression = "value.days", imports = {"kotlin.time.Duration.Companion.days"}))
        @kotlin.DeprecatedSinceKotlin(errorSince = "1.8", hiddenSince = "1.9", warningSince = "1.6")
        /* JADX INFO: renamed from: days-UwyO8pc, reason: not valid java name */
        public final /* synthetic */ long m12731daysUwyO8pc(double value) {
            return kotlin.time.DurationKt.toDuration(value, kotlin.time.DurationUnit.DAYS);
        }

        /* JADX INFO: renamed from: parse-UwyO8pc, reason: not valid java name */
        public final long m12752parseUwyO8pc(java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            try {
                return kotlin.time.DurationKt.parseDuration(value, false);
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.lang.IllegalArgumentException("Invalid duration string format: '" + value + "'.", e);
            }
        }

        /* JADX INFO: renamed from: parseIsoString-UwyO8pc, reason: not valid java name */
        public final long m12753parseIsoStringUwyO8pc(java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            try {
                return kotlin.time.DurationKt.parseDuration(value, true);
            } catch (java.lang.IllegalArgumentException e) {
                throw new java.lang.IllegalArgumentException("Invalid ISO duration string format: '" + value + "'.", e);
            }
        }

        /* JADX INFO: renamed from: parseOrNull-FghU774, reason: not valid java name */
        public final kotlin.time.Duration m12755parseOrNullFghU774(java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            try {
                return kotlin.time.Duration.m12631boximpl(kotlin.time.DurationKt.parseDuration(value, false));
            } catch (java.lang.IllegalArgumentException e) {
                return null;
            }
        }

        /* JADX INFO: renamed from: parseIsoStringOrNull-FghU774, reason: not valid java name */
        public final kotlin.time.Duration m12754parseIsoStringOrNullFghU774(java.lang.String value) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
            try {
                return kotlin.time.Duration.m12631boximpl(kotlin.time.DurationKt.parseDuration(value, true));
            } catch (java.lang.IllegalArgumentException e) {
                return null;
            }
        }
    }

    /* JADX INFO: renamed from: unaryMinus-UwyO8pc, reason: not valid java name */
    public static final long m12686unaryMinusUwyO8pc(long arg0) {
        return kotlin.time.DurationKt.durationOf(-m12660getValueimpl(arg0), ((int) arg0) & 1);
    }

    /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
    public static final long m12669plusLRDsOJo(long arg0, long other) {
        if (m12665isInfiniteimpl(arg0)) {
            if (m12662isFiniteimpl(other) || (arg0 ^ other) >= 0) {
                return arg0;
            }
            throw new java.lang.IllegalArgumentException("Summing infinite durations of different signs yields an undefined result.");
        }
        if (m12665isInfiniteimpl(other)) {
            return other;
        }
        if ((((int) arg0) & 1) == (((int) other) & 1)) {
            long result = m12660getValueimpl(arg0) + m12660getValueimpl(other);
            return m12664isInNanosimpl(arg0) ? kotlin.time.DurationKt.durationOfNanosNormalized(result) : kotlin.time.DurationKt.durationOfMillisNormalized(result);
        }
        if (m12663isInMillisimpl(arg0)) {
            return m12629addValuesMixedRangesUwyO8pc(arg0, m12660getValueimpl(arg0), m12660getValueimpl(other));
        }
        return m12629addValuesMixedRangesUwyO8pc(arg0, m12660getValueimpl(other), m12660getValueimpl(arg0));
    }

    /* JADX INFO: renamed from: addValuesMixedRanges-UwyO8pc, reason: not valid java name */
    private static final long m12629addValuesMixedRangesUwyO8pc(long arg0, long thisMillis, long otherNanos) {
        long otherMillis = kotlin.time.DurationKt.nanosToMillis(otherNanos);
        long resultMillis = thisMillis + otherMillis;
        if (new kotlin.ranges.LongRange(-4611686018426L, 4611686018426L).contains(resultMillis)) {
            long otherNanoRemainder = otherNanos - kotlin.time.DurationKt.millisToNanos(otherMillis);
            return kotlin.time.DurationKt.durationOfNanos(kotlin.time.DurationKt.millisToNanos(resultMillis) + otherNanoRemainder);
        }
        return kotlin.time.DurationKt.durationOfMillis(kotlin.ranges.RangesKt.coerceIn(resultMillis, -4611686018427387903L, 4611686018427387903L));
    }

    /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
    public static final long m12668minusLRDsOJo(long arg0, long other) {
        return m12669plusLRDsOJo(arg0, m12686unaryMinusUwyO8pc(other));
    }

    /* JADX INFO: renamed from: times-UwyO8pc, reason: not valid java name */
    public static final long m12671timesUwyO8pc(long arg0, int scale) {
        if (m12665isInfiniteimpl(arg0)) {
            if (scale != 0) {
                return scale > 0 ? arg0 : m12686unaryMinusUwyO8pc(arg0);
            }
            throw new java.lang.IllegalArgumentException("Multiplying infinite duration by zero yields an undefined result.");
        }
        if (scale == 0) {
            return ZERO;
        }
        long value = m12660getValueimpl(arg0);
        long result = ((long) scale) * value;
        if (!m12664isInNanosimpl(arg0)) {
            if (result / ((long) scale) == value) {
                return kotlin.time.DurationKt.durationOfMillis(kotlin.ranges.RangesKt.coerceIn(result, new kotlin.ranges.LongRange(-4611686018427387903L, 4611686018427387903L)));
            }
            return kotlin.math.MathKt.getSign(value) * kotlin.math.MathKt.getSign(scale) > 0 ? INFINITE : NEG_INFINITE;
        }
        if (new kotlin.ranges.LongRange(-2147483647L, 2147483647L).contains(value)) {
            return kotlin.time.DurationKt.durationOfNanos(result);
        }
        if (result / ((long) scale) == value) {
            return kotlin.time.DurationKt.durationOfNanosNormalized(result);
        }
        long millis = kotlin.time.DurationKt.nanosToMillis(value);
        long remNanos = value - kotlin.time.DurationKt.millisToNanos(millis);
        long resultMillis = ((long) scale) * millis;
        long totalMillis = kotlin.time.DurationKt.nanosToMillis(((long) scale) * remNanos) + resultMillis;
        if (resultMillis / ((long) scale) != millis || (totalMillis ^ resultMillis) < 0) {
            return kotlin.math.MathKt.getSign(value) * kotlin.math.MathKt.getSign(scale) > 0 ? INFINITE : NEG_INFINITE;
        }
        return kotlin.time.DurationKt.durationOfMillis(kotlin.ranges.RangesKt.coerceIn(totalMillis, new kotlin.ranges.LongRange(-4611686018427387903L, 4611686018427387903L)));
    }

    /* JADX INFO: renamed from: times-UwyO8pc, reason: not valid java name */
    public static final long m12670timesUwyO8pc(long arg0, double scale) {
        int intScale = kotlin.math.MathKt.roundToInt(scale);
        if (((double) intScale) == scale) {
            return m12671timesUwyO8pc(arg0, intScale);
        }
        kotlin.time.DurationUnit unit = m12658getStorageUnitimpl(arg0);
        double result = m12676toDoubleimpl(arg0, unit) * scale;
        return kotlin.time.DurationKt.toDuration(result, unit);
    }

    /* JADX INFO: renamed from: div-UwyO8pc, reason: not valid java name */
    public static final long m12636divUwyO8pc(long arg0, int scale) {
        if (scale == 0) {
            if (m12667isPositiveimpl(arg0)) {
                return INFINITE;
            }
            if (m12666isNegativeimpl(arg0)) {
                return NEG_INFINITE;
            }
            throw new java.lang.IllegalArgumentException("Dividing zero duration by zero yields an undefined result.");
        }
        if (m12664isInNanosimpl(arg0)) {
            return kotlin.time.DurationKt.durationOfNanos(m12660getValueimpl(arg0) / ((long) scale));
        }
        if (m12665isInfiniteimpl(arg0)) {
            return m12671timesUwyO8pc(arg0, kotlin.math.MathKt.getSign(scale));
        }
        long result = m12660getValueimpl(arg0) / ((long) scale);
        if (new kotlin.ranges.LongRange(-4611686018426L, 4611686018426L).contains(result)) {
            long rem = kotlin.time.DurationKt.millisToNanos(m12660getValueimpl(arg0) - (((long) scale) * result)) / ((long) scale);
            return kotlin.time.DurationKt.durationOfNanos(kotlin.time.DurationKt.millisToNanos(result) + rem);
        }
        long rem2 = kotlin.time.DurationKt.durationOfMillis(result);
        return rem2;
    }

    /* JADX INFO: renamed from: div-UwyO8pc, reason: not valid java name */
    public static final long m12635divUwyO8pc(long arg0, double scale) {
        int intScale = kotlin.math.MathKt.roundToInt(scale);
        if ((((double) intScale) == scale) && intScale != 0) {
            return m12636divUwyO8pc(arg0, intScale);
        }
        kotlin.time.DurationUnit unit = m12658getStorageUnitimpl(arg0);
        double result = m12676toDoubleimpl(arg0, unit) / scale;
        return kotlin.time.DurationKt.toDuration(result, unit);
    }

    /* JADX INFO: renamed from: div-LRDsOJo, reason: not valid java name */
    public static final double m12634divLRDsOJo(long arg0, long other) {
        kotlin.time.DurationUnit coarserUnit = (kotlin.time.DurationUnit) kotlin.comparisons.ComparisonsKt.maxOf(m12658getStorageUnitimpl(arg0), m12658getStorageUnitimpl(other));
        return m12676toDoubleimpl(arg0, coarserUnit) / m12676toDoubleimpl(other, coarserUnit);
    }

    /* JADX INFO: renamed from: truncateTo-UwyO8pc$kotlin_stdlib, reason: not valid java name */
    public static final long m12685truncateToUwyO8pc$kotlin_stdlib(long arg0, kotlin.time.DurationUnit unit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        kotlin.time.DurationUnit storageUnit = m12658getStorageUnitimpl(arg0);
        if (unit.compareTo(storageUnit) <= 0 || m12665isInfiniteimpl(arg0)) {
            return arg0;
        }
        long scale = kotlin.time.DurationUnitKt.convertDurationUnit(1L, unit, storageUnit);
        long result = m12660getValueimpl(arg0) - (m12660getValueimpl(arg0) % scale);
        return kotlin.time.DurationKt.toDuration(result, storageUnit);
    }

    /* JADX INFO: renamed from: isNegative-impl, reason: not valid java name */
    public static final boolean m12666isNegativeimpl(long arg0) {
        return arg0 < 0;
    }

    /* JADX INFO: renamed from: isPositive-impl, reason: not valid java name */
    public static final boolean m12667isPositiveimpl(long arg0) {
        return arg0 > 0;
    }

    /* JADX INFO: renamed from: isInfinite-impl, reason: not valid java name */
    public static final boolean m12665isInfiniteimpl(long arg0) {
        return arg0 == INFINITE || arg0 == NEG_INFINITE;
    }

    /* JADX INFO: renamed from: isFinite-impl, reason: not valid java name */
    public static final boolean m12662isFiniteimpl(long arg0) {
        return !m12665isInfiniteimpl(arg0);
    }

    /* JADX INFO: renamed from: getAbsoluteValue-UwyO8pc, reason: not valid java name */
    public static final long m12639getAbsoluteValueUwyO8pc(long arg0) {
        return m12666isNegativeimpl(arg0) ? m12686unaryMinusUwyO8pc(arg0) : arg0;
    }

    /* JADX INFO: renamed from: compareTo-LRDsOJo, reason: not valid java name */
    public int m12687compareToLRDsOJo(long other) {
        return m12632compareToLRDsOJo(this.rawValue, other);
    }

    /* JADX INFO: renamed from: compareTo-LRDsOJo, reason: not valid java name */
    public static int m12632compareToLRDsOJo(long arg0, long other) {
        long compareBits = arg0 ^ other;
        if (compareBits < 0 || (((int) compareBits) & 1) == 0) {
            return kotlin.jvm.internal.Intrinsics.compare(arg0, other);
        }
        int r = (((int) arg0) & 1) - (((int) other) & 1);
        return m12666isNegativeimpl(arg0) ? -r : r;
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m12675toComponentsimpl(long arg0, kotlin.jvm.functions.Function5<? super java.lang.Long, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? extends T> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return action.invoke(java.lang.Long.valueOf(m12648getInWholeDaysimpl(arg0)), java.lang.Integer.valueOf(m12640getHoursComponentimpl(arg0)), java.lang.Integer.valueOf(m12655getMinutesComponentimpl(arg0)), java.lang.Integer.valueOf(m12657getSecondsComponentimpl(arg0)), java.lang.Integer.valueOf(m12656getNanosecondsComponentimpl(arg0)));
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m12674toComponentsimpl(long arg0, kotlin.jvm.functions.Function4<? super java.lang.Long, ? super java.lang.Integer, ? super java.lang.Integer, ? super java.lang.Integer, ? extends T> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return action.invoke(java.lang.Long.valueOf(m12649getInWholeHoursimpl(arg0)), java.lang.Integer.valueOf(m12655getMinutesComponentimpl(arg0)), java.lang.Integer.valueOf(m12657getSecondsComponentimpl(arg0)), java.lang.Integer.valueOf(m12656getNanosecondsComponentimpl(arg0)));
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m12673toComponentsimpl(long arg0, kotlin.jvm.functions.Function3<? super java.lang.Long, ? super java.lang.Integer, ? super java.lang.Integer, ? extends T> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return action.invoke(java.lang.Long.valueOf(m12652getInWholeMinutesimpl(arg0)), java.lang.Integer.valueOf(m12657getSecondsComponentimpl(arg0)), java.lang.Integer.valueOf(m12656getNanosecondsComponentimpl(arg0)));
    }

    /* JADX INFO: renamed from: toComponents-impl, reason: not valid java name */
    public static final <T> T m12672toComponentsimpl(long arg0, kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.Integer, ? extends T> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        return action.invoke(java.lang.Long.valueOf(m12654getInWholeSecondsimpl(arg0)), java.lang.Integer.valueOf(m12656getNanosecondsComponentimpl(arg0)));
    }

    /* JADX INFO: renamed from: getHoursComponent-impl, reason: not valid java name */
    public static final int m12640getHoursComponentimpl(long arg0) {
        if (m12665isInfiniteimpl(arg0)) {
            return 0;
        }
        return (int) (m12649getInWholeHoursimpl(arg0) % ((long) 24));
    }

    /* JADX INFO: renamed from: getMinutesComponent-impl, reason: not valid java name */
    public static final int m12655getMinutesComponentimpl(long arg0) {
        if (m12665isInfiniteimpl(arg0)) {
            return 0;
        }
        return (int) (m12652getInWholeMinutesimpl(arg0) % ((long) 60));
    }

    /* JADX INFO: renamed from: getSecondsComponent-impl, reason: not valid java name */
    public static final int m12657getSecondsComponentimpl(long arg0) {
        if (m12665isInfiniteimpl(arg0)) {
            return 0;
        }
        return (int) (m12654getInWholeSecondsimpl(arg0) % ((long) 60));
    }

    /* JADX INFO: renamed from: getNanosecondsComponent-impl, reason: not valid java name */
    public static final int m12656getNanosecondsComponentimpl(long arg0) {
        if (m12665isInfiniteimpl(arg0)) {
            return 0;
        }
        if (m12663isInMillisimpl(arg0)) {
            return (int) kotlin.time.DurationKt.millisToNanos(m12660getValueimpl(arg0) % ((long) 1000));
        }
        return (int) (m12660getValueimpl(arg0) % ((long) com.android.server.location.contexthub.ContextHubService.ReliableMessageRecord.TIMEOUT_NS));
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    public static final double m12676toDoubleimpl(long arg0, kotlin.time.DurationUnit unit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        if (arg0 == INFINITE) {
            return Double.POSITIVE_INFINITY;
        }
        if (arg0 == NEG_INFINITE) {
            return Double.NEGATIVE_INFINITY;
        }
        return kotlin.time.DurationUnitKt.convertDurationUnit(m12660getValueimpl(arg0), m12658getStorageUnitimpl(arg0), unit);
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    public static final long m12679toLongimpl(long arg0, kotlin.time.DurationUnit unit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        if (arg0 == INFINITE) {
            return Long.MAX_VALUE;
        }
        if (arg0 == NEG_INFINITE) {
            return Long.MIN_VALUE;
        }
        return kotlin.time.DurationUnitKt.convertDurationUnit(m12660getValueimpl(arg0), m12658getStorageUnitimpl(arg0), unit);
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    public static final int m12677toIntimpl(long arg0, kotlin.time.DurationUnit unit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        return (int) kotlin.ranges.RangesKt.coerceIn(m12679toLongimpl(arg0, unit), -2147483648L, 2147483647L);
    }

    /* JADX INFO: renamed from: getInWholeDays-impl, reason: not valid java name */
    public static final long m12648getInWholeDaysimpl(long arg0) {
        return m12679toLongimpl(arg0, kotlin.time.DurationUnit.DAYS);
    }

    /* JADX INFO: renamed from: getInWholeHours-impl, reason: not valid java name */
    public static final long m12649getInWholeHoursimpl(long arg0) {
        return m12679toLongimpl(arg0, kotlin.time.DurationUnit.HOURS);
    }

    /* JADX INFO: renamed from: getInWholeMinutes-impl, reason: not valid java name */
    public static final long m12652getInWholeMinutesimpl(long arg0) {
        return m12679toLongimpl(arg0, kotlin.time.DurationUnit.MINUTES);
    }

    /* JADX INFO: renamed from: getInWholeSeconds-impl, reason: not valid java name */
    public static final long m12654getInWholeSecondsimpl(long arg0) {
        return m12679toLongimpl(arg0, kotlin.time.DurationUnit.SECONDS);
    }

    /* JADX INFO: renamed from: getInWholeMilliseconds-impl, reason: not valid java name */
    public static final long m12651getInWholeMillisecondsimpl(long arg0) {
        return (m12663isInMillisimpl(arg0) && m12662isFiniteimpl(arg0)) ? m12660getValueimpl(arg0) : m12679toLongimpl(arg0, kotlin.time.DurationUnit.MILLISECONDS);
    }

    /* JADX INFO: renamed from: getInWholeMicroseconds-impl, reason: not valid java name */
    public static final long m12650getInWholeMicrosecondsimpl(long arg0) {
        return m12679toLongimpl(arg0, kotlin.time.DurationUnit.MICROSECONDS);
    }

    /* JADX INFO: renamed from: getInWholeNanoseconds-impl, reason: not valid java name */
    public static final long m12653getInWholeNanosecondsimpl(long arg0) {
        long value = m12660getValueimpl(arg0);
        if (m12664isInNanosimpl(arg0)) {
            return value;
        }
        if (value > 9223372036854L) {
            return Long.MAX_VALUE;
        }
        if (value < -9223372036854L) {
            return Long.MIN_VALUE;
        }
        return kotlin.time.DurationKt.millisToNanos(value);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static java.lang.String m12682toStringimpl(long arg0) {
        if (arg0 == 0) {
            return "0s";
        }
        if (arg0 == INFINITE) {
            return "Infinity";
        }
        if (arg0 == NEG_INFINITE) {
            return "-Infinity";
        }
        boolean isNegative = m12666isNegativeimpl(arg0);
        java.lang.StringBuilder $this$toString_impl_u24lambda_u245 = new java.lang.StringBuilder();
        if (isNegative) {
            $this$toString_impl_u24lambda_u245.append('-');
        }
        long arg0$iv = m12639getAbsoluteValueUwyO8pc(arg0);
        long days = m12648getInWholeDaysimpl(arg0$iv);
        int hours = m12640getHoursComponentimpl(arg0$iv);
        int minutes = m12655getMinutesComponentimpl(arg0$iv);
        int seconds = m12657getSecondsComponentimpl(arg0$iv);
        int nanoseconds = m12656getNanosecondsComponentimpl(arg0$iv);
        boolean hasDays = days != 0;
        boolean hasHours = hours != 0;
        boolean hasMinutes = minutes != 0;
        boolean hasSeconds = (seconds == 0 && nanoseconds == 0) ? false : true;
        int components = 0;
        if (hasDays) {
            $this$toString_impl_u24lambda_u245.append(days).append('d');
            components = 0 + 1;
        }
        if (hasHours || (hasDays && (hasMinutes || hasSeconds))) {
            int components2 = components + 1;
            if (components > 0) {
                $this$toString_impl_u24lambda_u245.append(' ');
            }
            $this$toString_impl_u24lambda_u245.append(hours).append('h');
            components = components2;
        }
        if (hasMinutes || (hasSeconds && (hasHours || hasDays))) {
            int components3 = components + 1;
            if (components > 0) {
                $this$toString_impl_u24lambda_u245.append(' ');
            }
            $this$toString_impl_u24lambda_u245.append(minutes).append('m');
            components = components3;
        }
        if (hasSeconds) {
            int components4 = components + 1;
            if (components > 0) {
                $this$toString_impl_u24lambda_u245.append(' ');
            }
            if (seconds != 0 || hasDays || hasHours || hasMinutes) {
                int nanoseconds2 = nanoseconds;
                m12630appendFractionalimpl(arg0, $this$toString_impl_u24lambda_u245, seconds, nanoseconds2, 9, "s", false);
                components = components4;
            } else {
                if (nanoseconds >= 1000000) {
                    m12630appendFractionalimpl(arg0, $this$toString_impl_u24lambda_u245, nanoseconds / 1000000, nanoseconds % 1000000, 6, "ms", false);
                } else if (nanoseconds >= 1000) {
                    m12630appendFractionalimpl(arg0, $this$toString_impl_u24lambda_u245, nanoseconds / 1000, nanoseconds % 1000, 3, "us", false);
                } else {
                    $this$toString_impl_u24lambda_u245.append(nanoseconds).append("ns");
                }
                components = components4;
            }
        }
        if (isNegative && components > 1) {
            $this$toString_impl_u24lambda_u245.insert(1, '(').append(')');
        }
        java.lang.String string = $this$toString_impl_u24lambda_u245.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    public java.lang.String toString() {
        return m12682toStringimpl(this.rawValue);
    }

    /* JADX INFO: renamed from: appendFractional-impl, reason: not valid java name */
    private static final void m12630appendFractionalimpl(long arg0, java.lang.StringBuilder $this$appendFractional, int whole, int fractional, int fractionalSize, java.lang.String unit, boolean isoZeroes) {
        $this$appendFractional.append(whole);
        if (fractional != 0) {
            $this$appendFractional.append('.');
            java.lang.CharSequence fracString = kotlin.text.StringsKt.padStart(java.lang.String.valueOf(fractional), fractionalSize, '0');
            java.lang.CharSequence $this$indexOfLast$iv = fracString;
            int i = -1;
            int length = $this$indexOfLast$iv.length() - 1;
            if (length >= 0) {
                while (true) {
                    int index$iv = length;
                    length--;
                    char it = $this$indexOfLast$iv.charAt(index$iv);
                    char it2 = it != '0' ? (char) 1 : (char) 0;
                    if (it2 == 0) {
                        if (length < 0) {
                            break;
                        }
                    } else {
                        i = index$iv;
                        break;
                    }
                }
            }
            int nonZeroDigits = i + 1;
            if (isoZeroes || nonZeroDigits >= 3) {
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue($this$appendFractional.append(fracString, 0, ((nonZeroDigits + 2) / 3) * 3), "append(...)");
            } else {
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue($this$appendFractional.append(fracString, 0, nonZeroDigits), "append(...)");
            }
        }
        $this$appendFractional.append(unit);
    }

    /* JADX INFO: renamed from: toString-impl$default, reason: not valid java name */
    public static /* synthetic */ java.lang.String m12684toStringimpl$default(long j, kotlin.time.DurationUnit durationUnit, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return m12683toStringimpl(j, durationUnit, i);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static final java.lang.String m12683toStringimpl(long arg0, kotlin.time.DurationUnit unit, int decimals) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(unit, "unit");
        if (!(decimals >= 0)) {
            throw new java.lang.IllegalArgumentException(("decimals must be not negative, but was " + decimals).toString());
        }
        double number = m12676toDoubleimpl(arg0, unit);
        return java.lang.Double.isInfinite(number) ? java.lang.String.valueOf(number) : kotlin.time.DurationJvmKt.formatToExactDecimals(number, kotlin.ranges.RangesKt.coerceAtMost(decimals, 12)) + kotlin.time.DurationUnitKt.shortName(unit);
    }

    /* JADX INFO: renamed from: toIsoString-impl, reason: not valid java name */
    public static final java.lang.String m12678toIsoStringimpl(long arg0) {
        long hours;
        java.lang.StringBuilder $this$toIsoString_impl_u24lambda_u249 = new java.lang.StringBuilder();
        if (m12666isNegativeimpl(arg0)) {
            $this$toIsoString_impl_u24lambda_u249.append('-');
        }
        $this$toIsoString_impl_u24lambda_u249.append("PT");
        long arg0$iv = m12639getAbsoluteValueUwyO8pc(arg0);
        long hours2 = m12649getInWholeHoursimpl(arg0$iv);
        int minutes = m12655getMinutesComponentimpl(arg0$iv);
        int seconds = m12657getSecondsComponentimpl(arg0$iv);
        int nanoseconds = m12656getNanosecondsComponentimpl(arg0$iv);
        if (!m12665isInfiniteimpl(arg0)) {
            hours = hours2;
        } else {
            hours = 9999999999999L;
        }
        boolean z = true;
        boolean hasHours = hours != 0;
        boolean hasSeconds = (seconds == 0 && nanoseconds == 0) ? false : true;
        if (minutes == 0 && (!hasSeconds || !hasHours)) {
            z = false;
        }
        boolean hasMinutes = z;
        if (hasHours) {
            $this$toIsoString_impl_u24lambda_u249.append(hours).append('H');
        }
        if (hasMinutes) {
            $this$toIsoString_impl_u24lambda_u249.append(minutes).append('M');
        }
        if (hasSeconds || (!hasHours && !hasMinutes)) {
            m12630appendFractionalimpl(arg0, $this$toIsoString_impl_u24lambda_u249, seconds, nanoseconds, 9, "S", true);
        }
        java.lang.String string = $this$toIsoString_impl_u24lambda_u249.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }
}
