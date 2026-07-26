package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Migration.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000x\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0013\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0000\u001a\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001aµ\u0001\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0007\"\u0004\b\u0001\u0010\b\"\u0004\b\u0002\u0010\t\"\u0004\b\u0003\u0010\n\"\u0004\b\u0004\u0010\u000b\"\u0004\b\u0005\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00070\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\u00032\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\t0\u00032\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\n0\u00032\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u00032:\u0010\u0010\u001a6\b\u0001\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u0002H\n\u0012\u0004\u0012\u0002H\u000b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0011H\u0007¢\u0006\u0002\u0010\u0014\u001a\u009b\u0001\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0007\"\u0004\b\u0001\u0010\b\"\u0004\b\u0002\u0010\t\"\u0004\b\u0003\u0010\n\"\u0004\b\u0004\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00070\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\u00032\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\t0\u00032\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\n0\u000324\u0010\u0010\u001a0\b\u0001\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u0002H\n\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0015H\u0007¢\u0006\u0002\u0010\u0016\u001a\u0081\u0001\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0007\"\u0004\b\u0001\u0010\b\"\u0004\b\u0002\u0010\t\"\u0004\b\u0003\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00070\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\u00032\f\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\t0\u00032.\u0010\u0010\u001a*\b\u0001\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\b\u0012\u0004\u0012\u0002H\t\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0017H\u0007¢\u0006\u0002\u0010\u0018\u001ag\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0007\"\u0004\b\u0001\u0010\b\"\u0004\b\u0002\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00070\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\b0\u00032(\u0010\u0010\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u0007\u0012\u0004\u0012\u0002H\b\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0019H\u0007¢\u0006\u0002\u0010\u001a\u001aI\u0010\u001b\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00040\u00032#\u0010\u001c\u001a\u001f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0003\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u00030\u001d¢\u0006\u0002\b\u001eH\u0007\u001a>\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0018\u0010 \u001a\u0014\u0012\u0004\u0012\u0002H\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u00030\u001dH\u0007\u001a+\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010\"\u001a\u0002H\u0004H\u0007¢\u0006\u0002\u0010#\u001a,\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001a&\u0010$\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010%\u001a\u00020&H\u0007\u001a&\u0010'\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010%\u001a\u00020&H\u0007\u001aS\u0010(\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00040\u00032(\u0010 \u001a$\b\u0001\u0012\u0004\u0012\u0002H\u0004\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u00030\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130)H\u0007¢\u0006\u0002\u0010*\u001a$\u0010+\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u00030\u0003H\u0007\u001aP\u0010,\u001a\u00020-\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u000321\u0010.\u001a-\b\u0001\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(\"\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130)H\u0007¢\u0006\u0002\u00101\u001a$\u00102\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u00030\u0003H\u0007\u001a&\u00103\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u00104\u001a\u000205H\u0007\u001a,\u00106\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\f\u00107\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001a,\u00108\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\f\u00107\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001a+\u00109\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u00107\u001a\u0002H\u0004H\u0007¢\u0006\u0002\u0010#\u001aA\u00109\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u00107\u001a\u0002H\u00042\u0014\b\u0002\u0010:\u001a\u000e\u0012\u0004\u0012\u00020;\u0012\u0004\u0012\u00020<0\u001dH\u0007¢\u0006\u0002\u0010=\u001a\u001e\u0010>\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001a&\u0010>\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010?\u001a\u00020@H\u0007\u001a&\u0010A\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u00104\u001a\u000205H\u0007\u001a\u001e\u0010B\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001a&\u0010B\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010?\u001a\u00020@H\u0007\u001a{\u0010C\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010D\u001a\u0002H\u00062H\b\u0001\u0010E\u001aB\b\u0001\u0012\u0013\u0012\u0011H\u0006¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(F\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(\"\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0019H\u0007¢\u0006\u0002\u0010G\u001ak\u0010H\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032F\u0010E\u001aB\b\u0001\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(F\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(\"\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u0019H\u0007¢\u0006\u0002\u0010I\u001a&\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010K\u001a\u00020@H\u0007\u001a+\u0010L\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u0010\"\u001a\u0002H\u0004H\u0007¢\u0006\u0002\u0010#\u001a,\u0010L\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\f\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001a\u0018\u0010M\u001a\u00020-\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0003H\u0007\u001aA\u0010M\u001a\u00020-\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\"\u0010N\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130)H\u0007¢\u0006\u0002\u00101\u001ae\u0010M\u001a\u00020-\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\"\u0010N\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130)2\"\u0010O\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020;\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130)H\u0007¢\u0006\u0002\u0010P\u001a&\u0010Q\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0003\"\u0004\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0006\u00104\u001a\u000205H\u0007\u001ab\u0010R\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0003\"\u0004\b\u0000\u0010\u0004\"\u0004\b\u0001\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00040\u000327\u0010\u0010\u001a3\b\u0001\u0012\u0013\u0012\u0011H\u0004¢\u0006\f\b/\u0012\b\b0\u0012\u0004\b\b(\"\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00060\u00030\u0012\u0012\u0006\u0012\u0004\u0018\u00010\u00130)H\u0007¢\u0006\u0002\u0010*¨\u0006S"}, d2 = {"noImpl", "", "cache", "Lkotlinx/coroutines/flow/Flow;", "T", "combineLatest", "R", "T1", "T2", "T3", "T4", "T5", "other", "other2", "other3", "other4", "transform", "Lkotlin/Function6;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function6;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function5;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function5;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function4;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function4;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function3;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "compose", "transformer", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "concatMap", "mapper", "concatWith", "value", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;)Lkotlinx/coroutines/flow/Flow;", "delayEach", "timeMillis", "", "delayFlow", "flatMap", "Lkotlin/Function2;", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "flatten", "forEach", "", "action", "Lkotlin/ParameterName;", "name", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)V", "merge", "observeOn", "context", "Lkotlin/coroutines/CoroutineContext;", "onErrorResume", "fallback", "onErrorResumeNext", "onErrorReturn", "predicate", "", "", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/flow/Flow;", "publish", "bufferSize", "", "publishOn", "replay", "scanFold", "initial", "operation", "accumulator", "(Lkotlinx/coroutines/flow/Flow;Ljava/lang/Object;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "scanReduce", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "skip", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, "startWith", "subscribe", "onEach", "onError", "(Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;)V", "subscribeOn", "switchMap", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__MigrationKt {
    public static final java.lang.Void noImpl() {
        throw new java.lang.UnsupportedOperationException("Not implemented, should not be called");
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    public static final <T> kotlinx.coroutines.flow.Flow<T> observeOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Collect flow in the desired context instead")
    public static final <T> kotlinx.coroutines.flow.Flow<T> publishOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'flowOn' instead")
    public static final <T> kotlinx.coroutines.flow.Flow<T> subscribeOn(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emitAll(fallback) }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { emitAll(fallback) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorResume(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> fallback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(fallback, "fallback");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emitAll(fallback) }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { emitAll(fallback) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorResumeNext(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> fallback) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(fallback, "fallback");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'launchIn' with 'onEach', 'onCompletion' and 'catch' instead")
    public static final <T> void subscribe(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'launchIn' with 'onEach', 'onCompletion' and 'catch' instead")
    public static final <T> void subscribe(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onEach) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onEach, "onEach");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'launchIn' with 'onEach', 'onCompletion' and 'catch' instead")
    public static final <T> void subscribe(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onEach, kotlin.jvm.functions.Function2<? super java.lang.Throwable, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onError) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onEach, "onEach");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onError, "onError");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue is 'flatMapConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> flatMap(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends R>>, ? extends java.lang.Object> mapper) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mapper, "mapper");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'concatMap' is 'flatMapConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flatMapConcat(mapper)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> concatMap(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super T, ? extends kotlinx.coroutines.flow.Flow<? extends R>> mapper) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(mapper, "mapper");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'merge' is 'flattenConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flattenConcat()", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> merge(kotlinx.coroutines.flow.Flow<? extends kotlinx.coroutines.flow.Flow<? extends T>> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'flatten' is 'flattenConcat'", replaceWith = @kotlin.ReplaceWith(expression = "flattenConcat()", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> flatten(kotlinx.coroutines.flow.Flow<? extends kotlinx.coroutines.flow.Flow<? extends T>> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'compose' is 'let'", replaceWith = @kotlin.ReplaceWith(expression = "let(transformer)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> compose(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function1<? super kotlinx.coroutines.flow.Flow<? extends T>, ? extends kotlinx.coroutines.flow.Flow<? extends R>> transformer) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transformer, "transformer");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'skip' is 'drop'", replaceWith = @kotlin.ReplaceWith(expression = "drop(count)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> skip(kotlinx.coroutines.flow.Flow<? extends T> flow, int count) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'forEach' is 'collect'", replaceWith = @kotlin.ReplaceWith(expression = "collect(action)", imports = {}))
    public static final <T> void forEach(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow has less verbose 'scan' shortcut", replaceWith = @kotlin.ReplaceWith(expression = "scan(initial, operation)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> scanFold(kotlinx.coroutines.flow.Flow<? extends T> flow, R r, kotlin.jvm.functions.Function3<? super R, ? super T, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> operation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { emit(fallback) }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { emit(fallback) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorReturn(kotlinx.coroutines.flow.Flow<? extends T> flow, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__MigrationKt$onErrorReturn$2, reason: invalid class name */
    /* JADX INFO: compiled from: Migration.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "e", ""}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MigrationKt$onErrorReturn$2", f = "Migration.kt", i = {}, l = {306}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super T>, java.lang.Throwable, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ T $fallback;
        final /* synthetic */ kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Boolean> $predicate;
        private /* synthetic */ java.lang.Object L$0;
        /* synthetic */ java.lang.Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass2(kotlin.jvm.functions.Function1<? super java.lang.Throwable, java.lang.Boolean> function1, T t, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass2> continuation) {
            super(3, continuation);
            this.$predicate = function1;
            this.$fallback = t;
        }

        @Override // kotlin.jvm.functions.Function3
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, java.lang.Throwable th, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass2(this.$predicate, this.$fallback, continuation);
            anonymousClass2.L$0 = flowCollector;
            anonymousClass2.L$1 = th;
            return anonymousClass2.invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.flow.FlowCollector $this$catch = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    java.lang.Throwable e = (java.lang.Throwable) this.L$1;
                    if (!this.$predicate.invoke(e).booleanValue()) {
                        throw e;
                    }
                    this.L$0 = null;
                    this.label = 1;
                    if ($this$catch.emit(this.$fallback, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return kotlin.Unit.INSTANCE;
        }
    }

    public static /* synthetic */ kotlinx.coroutines.flow.Flow onErrorReturn$default(kotlinx.coroutines.flow.Flow flow, java.lang.Object obj, kotlin.jvm.functions.Function1 function1, int i, java.lang.Object obj2) {
        if ((i & 2) != 0) {
            function1 = new kotlin.jvm.functions.Function1<java.lang.Throwable, java.lang.Boolean>() { // from class: kotlinx.coroutines.flow.FlowKt__MigrationKt.onErrorReturn.1
                @Override // kotlin.jvm.functions.Function1
                public final java.lang.Boolean invoke(java.lang.Throwable it) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                    return true;
                }
            };
        }
        return kotlinx.coroutines.flow.FlowKt.onErrorReturn(flow, obj, function1);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'onErrorXxx' is 'catch'. Use 'catch { e -> if (predicate(e)) emit(fallback) else throw e }'", replaceWith = @kotlin.ReplaceWith(expression = "catch { e -> if (predicate(e)) emit(fallback) else throw e }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> onErrorReturn(kotlinx.coroutines.flow.Flow<? extends T> flow, T t, kotlin.jvm.functions.Function1<? super java.lang.Throwable, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        return kotlinx.coroutines.flow.FlowKt.m12838catch(flow, new kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass2(predicate, t, null));
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'startWith' is 'onStart'. Use 'onStart { emit(value) }'", replaceWith = @kotlin.ReplaceWith(expression = "onStart { emit(value) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> startWith(kotlinx.coroutines.flow.Flow<? extends T> flow, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'startWith' is 'onStart'. Use 'onStart { emitAll(other) }'", replaceWith = @kotlin.ReplaceWith(expression = "onStart { emitAll(other) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> startWith(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'concatWith' is 'onCompletion'. Use 'onCompletion { emit(value) }'", replaceWith = @kotlin.ReplaceWith(expression = "onCompletion { emit(value) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> concatWith(kotlinx.coroutines.flow.Flow<? extends T> flow, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'concatWith' is 'onCompletion'. Use 'onCompletion { if (it == null) emitAll(other) }'", replaceWith = @kotlin.ReplaceWith(expression = "onCompletion { if (it == null) emitAll(other) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> concatWith(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlinx.coroutines.flow.Flow<? extends T> other) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "this.combine(other, transform)", imports = {}))
    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> other, kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.combine(flow, other, transform);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "combine(this, other, other2, transform)", imports = {}))
    public static final <T1, T2, T3, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> other, kotlinx.coroutines.flow.Flow<? extends T3> other2, kotlin.jvm.functions.Function4<? super T1, ? super T2, ? super T3, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other2, "other2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.combine(flow, other, other2, transform);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "combine(this, other, other2, other3, transform)", imports = {}))
    public static final <T1, T2, T3, T4, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> other, kotlinx.coroutines.flow.Flow<? extends T3> other2, kotlinx.coroutines.flow.Flow<? extends T4> other3, kotlin.jvm.functions.Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other2, "other2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other3, "other3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.combine(flow, other, other2, other3, transform);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'combineLatest' is 'combine'", replaceWith = @kotlin.ReplaceWith(expression = "combine(this, other, other2, other3, transform)", imports = {}))
    public static final <T1, T2, T3, T4, T5, R> kotlinx.coroutines.flow.Flow<R> combineLatest(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> other, kotlinx.coroutines.flow.Flow<? extends T3> other2, kotlinx.coroutines.flow.Flow<? extends T4> other3, kotlinx.coroutines.flow.Flow<? extends T5> other4, kotlin.jvm.functions.Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other2, "other2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other3, "other3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other4, "other4");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.combine(flow, other, other2, other3, other4, transform);
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__MigrationKt$delayFlow$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Migration.kt */
    @kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", "", "T", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MigrationKt$delayFlow$1", f = "Migration.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.LOCATION_TIME_ZONE_PROVIDER_CONTROLLER_STATE_CHANGED}, m = "invokeSuspend", n = {}, s = {})
    static final class C01641<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super T>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ long $timeMillis;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C01641(long j, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__MigrationKt.C01641> continuation) {
            super(2, continuation);
            this.$timeMillis = j;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new kotlinx.coroutines.flow.FlowKt__MigrationKt.C01641(this.$timeMillis, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__MigrationKt.C01641) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (kotlinx.coroutines.DelayKt.delay(this.$timeMillis, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return kotlin.Unit.INSTANCE;
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'onStart { delay(timeMillis) }'", replaceWith = @kotlin.ReplaceWith(expression = "onStart { delay(timeMillis) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> delayFlow(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeMillis) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        return kotlinx.coroutines.flow.FlowKt.onStart(flow, new kotlinx.coroutines.flow.FlowKt__MigrationKt.C01641(timeMillis, null));
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__MigrationKt$delayEach$1, reason: invalid class name */
    /* JADX INFO: compiled from: Migration.kt */
    @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@"}, d2 = {"<anonymous>", "", "T", "it"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__MigrationKt$delayEach$1", f = "Migration.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.DROPBOX_ENTRY_DROPPED}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass1<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<T, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ long $timeMillis;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(long j, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass1> continuation) {
            super(2, continuation);
            this.$timeMillis = j;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            return new kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass1(this.$timeMillis, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(T t, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass1) create(t, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (kotlinx.coroutines.DelayKt.delay(this.$timeMillis, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    kotlin.ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return kotlin.Unit.INSTANCE;
        }
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'onEach { delay(timeMillis) }'", replaceWith = @kotlin.ReplaceWith(expression = "onEach { delay(timeMillis) }", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> delayEach(kotlinx.coroutines.flow.Flow<? extends T> flow, long timeMillis) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        return kotlinx.coroutines.flow.FlowKt.onEach(flow, new kotlinx.coroutines.flow.FlowKt__MigrationKt.AnonymousClass1(timeMillis, null));
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogues of 'switchMap' are 'transformLatest', 'flatMapLatest' and 'mapLatest'", replaceWith = @kotlin.ReplaceWith(expression = "this.flatMapLatest(transform)", imports = {}))
    public static final <T, R> kotlinx.coroutines.flow.Flow<R> switchMap(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.Flow<? extends R>>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.transformLatest(flow, new kotlinx.coroutines.flow.FlowKt__MigrationKt$switchMap$$inlined$flatMapLatest$1(transform, null));
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "'scanReduce' was renamed to 'runningReduce' to be consistent with Kotlin standard library", replaceWith = @kotlin.ReplaceWith(expression = "runningReduce(operation)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> scanReduce(kotlinx.coroutines.flow.Flow<? extends T> flow, kotlin.jvm.functions.Function3<? super T, ? super T, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> operation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(operation, "operation");
        return kotlinx.coroutines.flow.FlowKt.runningReduce(flow, operation);
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'publish()' is 'shareIn'. \npublish().connect() is the default strategy (no extra call is needed), \npublish().autoConnect() translates to 'started = SharingStared.Lazily' argument, \npublish().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, 0)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> publish(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'publish(bufferSize)' is 'buffer' followed by 'shareIn'. \npublish().connect() is the default strategy (no extra call is needed), \npublish().autoConnect() translates to 'started = SharingStared.Lazily' argument, \npublish().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.buffer(bufferSize).shareIn(scope, 0)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> publish(kotlinx.coroutines.flow.Flow<? extends T> flow, int bufferSize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'replay()' is 'shareIn' with unlimited replay. \nreplay().connect() is the default strategy (no extra call is needed), \nreplay().autoConnect() translates to 'started = SharingStared.Lazily' argument, \nreplay().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, Int.MAX_VALUE)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> replay(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'replay(bufferSize)' is 'shareIn' with the specified replay parameter. \nreplay().connect() is the default strategy (no extra call is needed), \nreplay().autoConnect() translates to 'started = SharingStared.Lazily' argument, \nreplay().refCount() translates to 'started = SharingStared.WhileSubscribed()' argument.", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, bufferSize)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> replay(kotlinx.coroutines.flow.Flow<? extends T> flow, int bufferSize) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Flow analogue of 'cache()' is 'shareIn' with unlimited replay and 'started = SharingStared.Lazily' argument'", replaceWith = @kotlin.ReplaceWith(expression = "this.shareIn(scope, Int.MAX_VALUE, started = SharingStared.Lazily)", imports = {}))
    public static final <T> kotlinx.coroutines.flow.Flow<T> cache(kotlinx.coroutines.flow.Flow<? extends T> flow) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlinx.coroutines.flow.FlowKt.noImpl();
        throw new kotlin.KotlinNothingValueException();
    }
}
