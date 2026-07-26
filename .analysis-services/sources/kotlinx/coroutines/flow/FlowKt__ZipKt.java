package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Zip.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000d\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0006\u001an\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0003\u0018\u0001\"\u0004\b\u0001\u0010\u00022\u001e\u0010\u0004\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u0005\"\b\u0012\u0004\u0012\u0002H\u00030\u00012*\b\u0004\u0010\u0006\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007H\u0086\b¢\u0006\u0002\u0010\n\u001ab\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0003\u0018\u0001\"\u0004\b\u0001\u0010\u00022\u0012\u0010\u0004\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u000b2*\b\u0004\u0010\u0006\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007H\u0086\b¢\u0006\u0002\u0010\f\u001a·\u0001\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u000f\"\u0004\b\u0003\u0010\u0010\"\u0004\b\u0004\u0010\u0011\"\u0004\b\u0005\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u00012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00110\u00012:\u0010\u0006\u001a6\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H\u000f\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0017¢\u0006\u0002\u0010\u0018\u001a\u009d\u0001\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u000f\"\u0004\b\u0003\u0010\u0010\"\u0004\b\u0004\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u000124\u0010\u0006\u001a0\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H\u000f\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0019¢\u0006\u0002\u0010\u001a\u001a\u0085\u0001\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u000f\"\u0004\b\u0003\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u000120\b\u0001\u0010\u0006\u001a*\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001b¢\u0006\u0002\u0010\u001c\u001a\u0087\u0001\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012F\u0010\u0006\u001aB\b\u0001\u0012\u0013\u0012\u0011H\r¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0013\u0012\u0011H\u000e¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(!\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001d¢\u0006\u0002\u0010\"\u001a\u007f\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0003\u0018\u0001\"\u0004\b\u0001\u0010\u00022\u001e\u0010\u0004\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u0005\"\b\u0012\u0004\u0012\u0002H\u00030\u00012;\b\u0005\u0010\u0006\u001a5\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001d¢\u0006\u0002\b&H\u0086\b¢\u0006\u0002\u0010'\u001as\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0003\u0018\u0001\"\u0004\b\u0001\u0010\u00022\u0012\u0010\u0004\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u000b2;\b\u0005\u0010\u0006\u001a5\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001d¢\u0006\u0002\b&H\u0086\b¢\u0006\u0002\u0010(\u001aÊ\u0001\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u000f\"\u0004\b\u0003\u0010\u0010\"\u0004\b\u0004\u0010\u0011\"\u0004\b\u0005\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u00012\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u0002H\u00110\u00012M\b\u0001\u0010\u0006\u001aG\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H\u000f\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u0002H\u0011\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0)¢\u0006\u0002\b&¢\u0006\u0002\u0010*\u001a°\u0001\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u000f\"\u0004\b\u0003\u0010\u0010\"\u0004\b\u0004\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u00012G\b\u0001\u0010\u0006\u001aA\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H\u000f\u0012\u0004\u0012\u0002H\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0017¢\u0006\u0002\b&¢\u0006\u0002\u0010+\u001a\u0096\u0001\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u000f\"\u0004\b\u0003\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000f0\u00012A\b\u0001\u0010\u0006\u001a;\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0019¢\u0006\u0002\b&¢\u0006\u0002\u0010,\u001a\u009a\u0001\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u00022\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012Y\b\u0001\u0010\u0006\u001aS\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\u0013\u0012\u0011H\r¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0013\u0012\u0011H\u000e¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(!\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001b¢\u0006\u0002\b&¢\u0006\u0002\u0010-\u001a\u0081\u0001\u0010.\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0003\u0018\u0001\"\u0004\b\u0001\u0010\u00022\u001e\u0010\u0004\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u0005\"\b\u0012\u0004\u0012\u0002H\u00030\u00012;\b\u0005\u0010\u0006\u001a5\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001d¢\u0006\u0002\b&H\u0082\b¢\u0006\u0004\b/\u0010'\u001ap\u00100\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0003\u0018\u0001\"\u0004\b\u0001\u0010\u00022\u001e\u0010\u0004\u001a\u0010\u0012\f\b\u0001\u0012\b\u0012\u0004\u0012\u0002H\u00030\u00010\u0005\"\b\u0012\u0004\u0012\u0002H\u00030\u00012*\b\u0004\u0010\u0006\u001a$\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0005\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007H\u0082\b¢\u0006\u0004\b1\u0010\n\u001a!\u00102\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u000503\"\u0004\b\u0000\u0010\u0003H\u0002¢\u0006\u0002\b4\u001a\u0087\u0001\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u0002*\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012F\u0010\u0006\u001aB\b\u0001\u0012\u0013\u0012\u0011H\r¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0013\u0012\u0011H\u000e¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(!\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001dH\u0007¢\u0006\u0004\b5\u0010\"\u001a\u009a\u0001\u0010#\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u0002*\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012Y\b\u0001\u0010\u0006\u001aS\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020$\u0012\u0013\u0012\u0011H\r¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b( \u0012\u0013\u0012\u0011H\u000e¢\u0006\f\b\u001e\u0012\b\b\u001f\u0012\u0004\b\b(!\u0012\n\u0012\b\u0012\u0004\u0012\u00020%0\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001b¢\u0006\u0002\b&H\u0007¢\u0006\u0004\b6\u0010-\u001ae\u00107\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\r\"\u0004\b\u0001\u0010\u000e\"\u0004\b\u0002\u0010\u0002*\b\u0012\u0004\u0012\u0002H\r0\u00012\f\u00108\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u00012(\u0010\u0006\u001a$\b\u0001\u0012\u0004\u0012\u0002H\r\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001d¢\u0006\u0002\u0010\"¨\u00069"}, d2 = {"combine", "Lkotlinx/coroutines/flow/Flow;", "R", "T", "flows", "", "transform", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "([Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "", "(Ljava/lang/Iterable;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/flow/Flow;", "T1", "T2", "T3", "T4", "T5", "flow", "flow2", "flow3", "flow4", "flow5", "Lkotlin/Function6;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function6;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function5;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function5;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function4;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function4;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, "b", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "combineTransform", "Lkotlinx/coroutines/flow/FlowCollector;", "", "Lkotlin/ExtensionFunctionType;", "([Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "(Ljava/lang/Iterable;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/flow/Flow;", "Lkotlin/Function7;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function7;)Lkotlinx/coroutines/flow/Flow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function6;)Lkotlinx/coroutines/flow/Flow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function5;)Lkotlinx/coroutines/flow/Flow;", "(Lkotlinx/coroutines/flow/Flow;Lkotlinx/coroutines/flow/Flow;Lkotlin/jvm/functions/Function4;)Lkotlinx/coroutines/flow/Flow;", "combineTransformUnsafe", "combineTransformUnsafe$FlowKt__ZipKt", "combineUnsafe", "combineUnsafe$FlowKt__ZipKt", "nullArrayFactory", "Lkotlin/Function0;", "nullArrayFactory$FlowKt__ZipKt", "flowCombine", "flowCombineTransform", "zip", "other", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__ZipKt {
    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> flowCombine(final kotlinx.coroutines.flow.Flow<? extends T1> flow, final kotlinx.coroutines.flow.Flow<? extends T2> flow2, final kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return new kotlinx.coroutines.flow.Flow<R>() { // from class: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$unsafeFlow$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                java.lang.Object objCombineInternal = kotlinx.coroutines.flow.internal.CombineKt.combineInternal(flowCollector, new kotlinx.coroutines.flow.Flow[]{flow, flow2}, kotlinx.coroutines.flow.FlowKt__ZipKt.nullArrayFactory$FlowKt__ZipKt(), new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$1$1(transform, null), continuation);
                return objCombineInternal == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCombineInternal : kotlin.Unit.INSTANCE;
            }
        };
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.FlowKt.flowCombine(flow, flow2, transform);
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> flowCombineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function4<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2};
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$1(flows$iv, null, transform));
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlin.jvm.functions.Function4<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2};
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$2(flows$iv, null, transform));
    }

    public static final <T1, T2, T3, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, final kotlin.jvm.functions.Function4<? super T1, ? super T2, ? super T3, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow3, "flow3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        final kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2, flow3};
        return new kotlinx.coroutines.flow.Flow<R>() { // from class: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$1
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector collector, kotlin.coroutines.Continuation $completion) {
                java.lang.Object objCombineInternal = kotlinx.coroutines.flow.internal.CombineKt.combineInternal(collector, flows$iv, kotlinx.coroutines.flow.FlowKt__ZipKt.nullArrayFactory$FlowKt__ZipKt(), new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$1.AnonymousClass2(null, transform), $completion);
                return objCombineInternal == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCombineInternal : kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$1$2, reason: invalid class name */
            /* JADX INFO: compiled from: Zip.kt */
            @kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u008a@¨\u0006\u0007"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", "", "kotlinx/coroutines/flow/FlowKt__ZipKt$combineUnsafe$1$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
            @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$1$2", f = "Zip.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.DEVICE_ROTATED, 262}, m = "invokeSuspend", n = {}, s = {})
            public static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, java.lang.Object[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
                final /* synthetic */ kotlin.jvm.functions.Function4 $transform$inlined;
                private /* synthetic */ java.lang.Object L$0;
                /* synthetic */ java.lang.Object L$1;
                int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                public AnonymousClass2(kotlin.coroutines.Continuation continuation, kotlin.jvm.functions.Function4 function4) {
                    super(3, continuation);
                    this.$transform$inlined = function4;
                }

                @Override // kotlin.jvm.functions.Function3
                public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, java.lang.Object[] objArr, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                    kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$1.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$1.AnonymousClass2(continuation, this.$transform$inlined);
                    anonymousClass2.L$0 = flowCollector;
                    anonymousClass2.L$1 = objArr;
                    return anonymousClass2.invokeSuspend(kotlin.Unit.INSTANCE);
                }

                /* JADX WARN: Removed duplicated region for block: B:14:0x0065 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:15:0x0066  */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object invokeSuspend(java.lang.Object r13) throws java.lang.Throwable {
                    /*
                        r12 = this;
                        java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r1 = r12.label
                        r2 = 2
                        switch(r1) {
                            case 0: goto L24;
                            case 1: goto L17;
                            case 2: goto L12;
                            default: goto La;
                        }
                    La:
                        java.lang.IllegalStateException r13 = new java.lang.IllegalStateException
                        java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                        r13.<init>(r0)
                        throw r13
                    L12:
                        r0 = r12
                        kotlin.ResultKt.throwOnFailure(r13)
                        goto L68
                    L17:
                        r1 = r12
                        r3 = 0
                        java.lang.Object r4 = r1.L$0
                        kotlinx.coroutines.flow.FlowCollector r4 = (kotlinx.coroutines.flow.FlowCollector) r4
                        kotlin.ResultKt.throwOnFailure(r13)
                        r5 = r3
                        r3 = r1
                        r1 = r13
                        goto L56
                    L24:
                        kotlin.ResultKt.throwOnFailure(r13)
                        r1 = r12
                        java.lang.Object r3 = r1.L$0
                        r4 = r3
                        kotlinx.coroutines.flow.FlowCollector r4 = (kotlinx.coroutines.flow.FlowCollector) r4
                        java.lang.Object r3 = r1.L$1
                        java.lang.Object[] r3 = (java.lang.Object[]) r3
                        r5 = r1
                        kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                        r5 = 0
                        kotlin.jvm.functions.Function4 r6 = r1.$transform$inlined
                        r7 = 0
                        r7 = r3[r7]
                        r8 = 1
                        r9 = r3[r8]
                        r10 = r3[r2]
                        r1.L$0 = r4
                        r1.label = r8
                        r3 = 6
                        kotlin.jvm.internal.InlineMarker.mark(r3)
                        java.lang.Object r3 = r6.invoke(r7, r9, r10, r1)
                        r6 = 7
                        kotlin.jvm.internal.InlineMarker.mark(r6)
                        if (r3 != r0) goto L52
                        return r0
                    L52:
                        r11 = r1
                        r1 = r13
                        r13 = r3
                        r3 = r11
                    L56:
                        r5 = r3
                        kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                        r6 = 0
                        r3.L$0 = r6
                        r3.label = r2
                        java.lang.Object r13 = r4.emit(r13, r5)
                        if (r13 != r0) goto L66
                        return r0
                    L66:
                        r13 = r1
                        r0 = r3
                    L68:
                        kotlin.Unit r1 = kotlin.Unit.INSTANCE
                        return r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$1.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
                }
            }
        };
    }

    public static final <T1, T2, T3, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlin.jvm.functions.Function5<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super T3, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow3, "flow3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2, flow3};
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$3(flows$iv, null, transform));
    }

    public static final <T1, T2, T3, T4, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, final kotlin.jvm.functions.Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow3, "flow3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow4, "flow4");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        final kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2, flow3, flow4};
        return new kotlinx.coroutines.flow.Flow<R>() { // from class: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$2
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector collector, kotlin.coroutines.Continuation $completion) {
                java.lang.Object objCombineInternal = kotlinx.coroutines.flow.internal.CombineKt.combineInternal(collector, flows$iv, kotlinx.coroutines.flow.FlowKt__ZipKt.nullArrayFactory$FlowKt__ZipKt(), new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$2.AnonymousClass2(null, transform), $completion);
                return objCombineInternal == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCombineInternal : kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$2$2, reason: invalid class name */
            /* JADX INFO: compiled from: Zip.kt */
            @kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u008a@¨\u0006\u0007"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", "", "kotlinx/coroutines/flow/FlowKt__ZipKt$combineUnsafe$1$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
            @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$2$2", f = "Zip.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.DEVICE_ROTATED, 262}, m = "invokeSuspend", n = {}, s = {})
            public static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, java.lang.Object[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
                final /* synthetic */ kotlin.jvm.functions.Function5 $transform$inlined;
                private /* synthetic */ java.lang.Object L$0;
                /* synthetic */ java.lang.Object L$1;
                int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                public AnonymousClass2(kotlin.coroutines.Continuation continuation, kotlin.jvm.functions.Function5 function5) {
                    super(3, continuation);
                    this.$transform$inlined = function5;
                }

                @Override // kotlin.jvm.functions.Function3
                public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, java.lang.Object[] objArr, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                    kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$2.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$2.AnonymousClass2(continuation, this.$transform$inlined);
                    anonymousClass2.L$0 = flowCollector;
                    anonymousClass2.L$1 = objArr;
                    return anonymousClass2.invokeSuspend(kotlin.Unit.INSTANCE);
                }

                /* JADX WARN: Removed duplicated region for block: B:14:0x006c A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:15:0x006d  */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object invokeSuspend(java.lang.Object r14) throws java.lang.Throwable {
                    /*
                        r13 = this;
                        java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r1 = r13.label
                        r2 = 2
                        switch(r1) {
                            case 0: goto L24;
                            case 1: goto L17;
                            case 2: goto L12;
                            default: goto La;
                        }
                    La:
                        java.lang.IllegalStateException r14 = new java.lang.IllegalStateException
                        java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                        r14.<init>(r0)
                        throw r14
                    L12:
                        r0 = r13
                        kotlin.ResultKt.throwOnFailure(r14)
                        goto L6f
                    L17:
                        r1 = r13
                        r3 = 0
                        java.lang.Object r4 = r1.L$0
                        kotlinx.coroutines.flow.FlowCollector r4 = (kotlinx.coroutines.flow.FlowCollector) r4
                        kotlin.ResultKt.throwOnFailure(r14)
                        r11 = r3
                        r3 = r1
                        r1 = r14
                        goto L5d
                    L24:
                        kotlin.ResultKt.throwOnFailure(r14)
                        r1 = r13
                        java.lang.Object r3 = r1.L$0
                        r4 = r3
                        kotlinx.coroutines.flow.FlowCollector r4 = (kotlinx.coroutines.flow.FlowCollector) r4
                        java.lang.Object r3 = r1.L$1
                        java.lang.Object[] r3 = (java.lang.Object[]) r3
                        r5 = r1
                        kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                        r11 = 0
                        kotlin.jvm.functions.Function5 r5 = r1.$transform$inlined
                        r6 = 0
                        r6 = r3[r6]
                        r7 = 1
                        r8 = r3[r7]
                        r9 = r3[r2]
                        r10 = 3
                        r10 = r3[r10]
                        r1.L$0 = r4
                        r1.label = r7
                        r3 = 6
                        kotlin.jvm.internal.InlineMarker.mark(r3)
                        r7 = r8
                        r8 = r9
                        r9 = r10
                        r10 = r1
                        java.lang.Object r3 = r5.invoke(r6, r7, r8, r9, r10)
                        r5 = 7
                        kotlin.jvm.internal.InlineMarker.mark(r5)
                        if (r3 != r0) goto L59
                        return r0
                    L59:
                        r12 = r1
                        r1 = r14
                        r14 = r3
                        r3 = r12
                    L5d:
                        r5 = r3
                        kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                        r6 = 0
                        r3.L$0 = r6
                        r3.label = r2
                        java.lang.Object r14 = r4.emit(r14, r5)
                        if (r14 != r0) goto L6d
                        return r0
                    L6d:
                        r14 = r1
                        r0 = r3
                    L6f:
                        kotlin.Unit r1 = kotlin.Unit.INSTANCE
                        return r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$2.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
                }
            }
        };
    }

    public static final <T1, T2, T3, T4, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlin.jvm.functions.Function6<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super T3, ? super T4, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow3, "flow3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow4, "flow4");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2, flow3, flow4};
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$4(flows$iv, null, transform));
    }

    public static final <T1, T2, T3, T4, T5, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlinx.coroutines.flow.Flow<? extends T5> flow5, final kotlin.jvm.functions.Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow3, "flow3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow4, "flow4");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow5, "flow5");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        final kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2, flow3, flow4, flow5};
        return new kotlinx.coroutines.flow.Flow<R>() { // from class: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$3
            @Override // kotlinx.coroutines.flow.Flow
            public java.lang.Object collect(kotlinx.coroutines.flow.FlowCollector collector, kotlin.coroutines.Continuation $completion) {
                java.lang.Object objCombineInternal = kotlinx.coroutines.flow.internal.CombineKt.combineInternal(collector, flows$iv, kotlinx.coroutines.flow.FlowKt__ZipKt.nullArrayFactory$FlowKt__ZipKt(), new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$3.AnonymousClass2(null, transform), $completion);
                return objCombineInternal == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCombineInternal : kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$3$2, reason: invalid class name */
            /* JADX INFO: compiled from: Zip.kt */
            @kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u008a@¨\u0006\u0007"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", "", "kotlinx/coroutines/flow/FlowKt__ZipKt$combineUnsafe$1$1"}, k = 3, mv = {1, 9, 0}, xi = 48)
            @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$3$2", f = "Zip.kt", i = {}, l = {com.android.internal.util.FrameworkStatsLog.DEVICE_ROTATED, 262}, m = "invokeSuspend", n = {}, s = {})
            public static final class AnonymousClass2 extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, java.lang.Object[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
                final /* synthetic */ kotlin.jvm.functions.Function6 $transform$inlined;
                private /* synthetic */ java.lang.Object L$0;
                /* synthetic */ java.lang.Object L$1;
                int label;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                public AnonymousClass2(kotlin.coroutines.Continuation continuation, kotlin.jvm.functions.Function6 function6) {
                    super(3, continuation);
                    this.$transform$inlined = function6;
                }

                @Override // kotlin.jvm.functions.Function3
                public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, java.lang.Object[] objArr, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                    kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$3.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$3.AnonymousClass2(continuation, this.$transform$inlined);
                    anonymousClass2.L$0 = flowCollector;
                    anonymousClass2.L$1 = objArr;
                    return anonymousClass2.invokeSuspend(kotlin.Unit.INSTANCE);
                }

                /* JADX WARN: Removed duplicated region for block: B:14:0x0071 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:15:0x0072  */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final java.lang.Object invokeSuspend(java.lang.Object r15) throws java.lang.Throwable {
                    /*
                        r14 = this;
                        java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
                        int r1 = r14.label
                        r2 = 2
                        switch(r1) {
                            case 0: goto L25;
                            case 1: goto L18;
                            case 2: goto L12;
                            default: goto La;
                        }
                    La:
                        java.lang.IllegalStateException r15 = new java.lang.IllegalStateException
                        java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
                        r15.<init>(r0)
                        throw r15
                    L12:
                        r0 = r14
                        kotlin.ResultKt.throwOnFailure(r15)
                        goto L74
                    L18:
                        r1 = r14
                        r3 = 0
                        java.lang.Object r4 = r1.L$0
                        kotlinx.coroutines.flow.FlowCollector r4 = (kotlinx.coroutines.flow.FlowCollector) r4
                        kotlin.ResultKt.throwOnFailure(r15)
                        r12 = r3
                        r3 = r1
                        r1 = r15
                        goto L62
                    L25:
                        kotlin.ResultKt.throwOnFailure(r15)
                        r1 = r14
                        java.lang.Object r3 = r1.L$0
                        r4 = r3
                        kotlinx.coroutines.flow.FlowCollector r4 = (kotlinx.coroutines.flow.FlowCollector) r4
                        java.lang.Object r3 = r1.L$1
                        java.lang.Object[] r3 = (java.lang.Object[]) r3
                        r5 = r1
                        kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                        r12 = 0
                        kotlin.jvm.functions.Function6 r5 = r1.$transform$inlined
                        r6 = 0
                        r6 = r3[r6]
                        r7 = 1
                        r8 = r3[r7]
                        r9 = r3[r2]
                        r10 = 3
                        r10 = r3[r10]
                        r11 = 4
                        r11 = r3[r11]
                        r1.L$0 = r4
                        r1.label = r7
                        r3 = 6
                        kotlin.jvm.internal.InlineMarker.mark(r3)
                        r7 = r8
                        r8 = r9
                        r9 = r10
                        r10 = r11
                        r11 = r1
                        java.lang.Object r3 = r5.invoke(r6, r7, r8, r9, r10, r11)
                        r5 = 7
                        kotlin.jvm.internal.InlineMarker.mark(r5)
                        if (r3 != r0) goto L5e
                        return r0
                    L5e:
                        r13 = r1
                        r1 = r15
                        r15 = r3
                        r3 = r13
                    L62:
                        r5 = r3
                        kotlin.coroutines.Continuation r5 = (kotlin.coroutines.Continuation) r5
                        r6 = 0
                        r3.L$0 = r6
                        r3.label = r2
                        java.lang.Object r15 = r4.emit(r15, r5)
                        if (r15 != r0) goto L72
                        return r0
                    L72:
                        r15 = r1
                        r0 = r3
                    L74:
                        kotlin.Unit r1 = kotlin.Unit.INSTANCE
                        return r1
                    */
                    throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$combineUnsafe$FlowKt__ZipKt$3.AnonymousClass2.invokeSuspend(java.lang.Object):java.lang.Object");
                }
            }
        };
    }

    public static final <T1, T2, T3, T4, T5, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> flow2, kotlinx.coroutines.flow.Flow<? extends T3> flow3, kotlinx.coroutines.flow.Flow<? extends T4> flow4, kotlinx.coroutines.flow.Flow<? extends T5> flow5, kotlin.jvm.functions.Function7<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "flow");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow2, "flow2");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow3, "flow3");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow4, "flow4");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow5, "flow5");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlinx.coroutines.flow.Flow[] flows$iv = {flow, flow2, flow3, flow4, flow5};
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$$inlined$combineTransformUnsafe$FlowKt__ZipKt$5(flows$iv, null, transform));
    }

    public static final /* synthetic */ <T, R> kotlinx.coroutines.flow.Flow<R> combine(kotlinx.coroutines.flow.Flow<? extends T>[] flows, kotlin.jvm.functions.Function2<? super T[], ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flows, "flows");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlin.jvm.internal.Intrinsics.needClassReification();
        return new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$unsafeFlow$2(flows, transform);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$6, reason: invalid class name */
    /* JADX INFO: compiled from: Zip.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$6", f = "Zip.kt", i = {}, l = {251}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass6<R> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super R>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T>[] $flows;
        final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $transform;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass6(kotlinx.coroutines.flow.Flow<? extends T>[] flowArr, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6> continuation) {
            super(2, continuation);
            this.$flows = flowArr;
            this.$transform = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6 anonymousClass6 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6(this.$flows, this.$transform, continuation);
            anonymousClass6.L$0 = obj;
            return anonymousClass6;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX INFO: Add missing generic type declarations: [T] */
        /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$6$1, reason: invalid class name */
        /* JADX INFO: compiled from: Zip.kt */
        @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\u0010\u0000\u001a\f\u0012\u0006\u0012\u0004\u0018\u0001H\u0002\u0018\u00010\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "R", "invoke", "()[Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 176)
        public static final class AnonymousClass1<T> extends kotlin.jvm.internal.Lambda implements kotlin.jvm.functions.Function0<T[]> {
            final /* synthetic */ kotlinx.coroutines.flow.Flow<T>[] $flows;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            public AnonymousClass1(kotlinx.coroutines.flow.Flow<? extends T>[] flowArr) {
                super(0);
                this.$flows = flowArr;
            }

            @Override // kotlin.jvm.functions.Function0
            public final T[] invoke() {
                int length = this.$flows.length;
                kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(0, "T?");
                return (T[]) new java.lang.Object[length];
            }
        }

        /* JADX INFO: Add missing generic type declarations: [T] */
        /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$6$2, reason: invalid class name */
        /* JADX INFO: compiled from: Zip.kt */
        @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", ""}, k = 3, mv = {1, 9, 0}, xi = 176)
        @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$6$2", f = "Zip.kt", i = {}, l = {251}, m = "invokeSuspend", n = {}, s = {})
        public static final class AnonymousClass2<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
            final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $transform;
            private /* synthetic */ java.lang.Object L$0;
            /* synthetic */ java.lang.Object L$1;
            int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            public AnonymousClass2(kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass2> continuation) {
                super(3, continuation);
                this.$transform = function3;
            }

            @Override // kotlin.jvm.functions.Function3
            public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, T[] tArr, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                kotlin.jvm.internal.Intrinsics.needClassReification();
                kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass2(this.$transform, continuation);
                anonymousClass2.L$0 = flowCollector;
                anonymousClass2.L$1 = tArr;
                return anonymousClass2.invokeSuspend(kotlin.Unit.INSTANCE);
            }

            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$ArrayArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
                java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        kotlin.ResultKt.throwOnFailure(obj);
                        kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                        java.lang.Object[] objArr = (java.lang.Object[]) this.L$1;
                        kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> function3 = this.$transform;
                        this.L$0 = null;
                        this.label = 1;
                        if (function3.invoke(flowCollector, objArr, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        kotlin.ResultKt.throwOnFailure(obj);
                        break;
                    default:
                        throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return kotlin.Unit.INSTANCE;
            }

            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$ArrayArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            public final java.lang.Object invokeSuspend$$forInline(java.lang.Object obj) {
                this.$transform.invoke((kotlinx.coroutines.flow.FlowCollector) this.L$0, (java.lang.Object[]) this.L$1, this);
                return kotlin.Unit.INSTANCE;
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.flow.FlowCollector $this$flow = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    kotlinx.coroutines.flow.Flow<T>[] flowArr = this.$flows;
                    kotlin.jvm.internal.Intrinsics.needClassReification();
                    kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass1(this.$flows);
                    kotlin.jvm.internal.Intrinsics.needClassReification();
                    this.label = 1;
                    if (kotlinx.coroutines.flow.internal.CombineKt.combineInternal($this$flow, flowArr, anonymousClass1, new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass2(this.$transform, null), this) == coroutine_suspended) {
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

        public final java.lang.Object invokeSuspend$$forInline(java.lang.Object $result) {
            kotlinx.coroutines.flow.FlowCollector $this$flow = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
            kotlinx.coroutines.flow.Flow<T>[] flowArr = this.$flows;
            kotlin.jvm.internal.Intrinsics.needClassReification();
            kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass1(this.$flows);
            kotlin.jvm.internal.Intrinsics.needClassReification();
            kotlin.jvm.internal.InlineMarker.mark(0);
            kotlinx.coroutines.flow.internal.CombineKt.combineInternal($this$flow, flowArr, anonymousClass1, new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6.AnonymousClass2(this.$transform, null), this);
            kotlin.jvm.internal.InlineMarker.mark(1);
            return kotlin.Unit.INSTANCE;
        }
    }

    public static final /* synthetic */ <T, R> kotlinx.coroutines.flow.Flow<R> combineTransform(kotlinx.coroutines.flow.Flow<? extends T>[] flows, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flows, "flows");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        kotlin.jvm.internal.Intrinsics.needClassReification();
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass6(flows, transform, null));
    }

    private static final /* synthetic */ <T, R> kotlinx.coroutines.flow.Flow<R> combineUnsafe$FlowKt__ZipKt(kotlinx.coroutines.flow.Flow<? extends T>[] flowArr, kotlin.jvm.functions.Function2<? super T[], ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2) {
        kotlin.jvm.internal.Intrinsics.needClassReification();
        return new kotlinx.coroutines.flow.FlowKt__ZipKt$combineUnsafe$$inlined$unsafeFlow$1(flowArr, function2);
    }

    private static final /* synthetic */ <T, R> kotlinx.coroutines.flow.Flow<R> combineTransformUnsafe$FlowKt__ZipKt(kotlinx.coroutines.flow.Flow<? extends T>[] flowArr, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3) {
        kotlin.jvm.internal.Intrinsics.needClassReification();
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransformUnsafe$1(flowArr, function3, null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <T> kotlin.jvm.functions.Function0<T[]> nullArrayFactory$FlowKt__ZipKt() {
        return new kotlin.jvm.functions.Function0() { // from class: kotlinx.coroutines.flow.FlowKt__ZipKt$nullArrayFactory$1
            @Override // kotlin.jvm.functions.Function0
            public final java.lang.Void invoke() {
                return null;
            }
        };
    }

    public static final /* synthetic */ <T, R> kotlinx.coroutines.flow.Flow<R> combine(java.lang.Iterable<? extends kotlinx.coroutines.flow.Flow<? extends T>> flows, kotlin.jvm.functions.Function2<? super T[], ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flows, "flows");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Collection $this$toTypedArray$iv = kotlin.collections.CollectionsKt.toList(flows);
        kotlinx.coroutines.flow.Flow[] flowArray = (kotlinx.coroutines.flow.Flow[]) $this$toTypedArray$iv.toArray(new kotlinx.coroutines.flow.Flow[0]);
        kotlin.jvm.internal.Intrinsics.needClassReification();
        return new kotlinx.coroutines.flow.FlowKt__ZipKt$combine$$inlined$unsafeFlow$3(flowArray, transform);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$7, reason: invalid class name */
    /* JADX INFO: compiled from: Zip.kt */
    @kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;"}, k = 3, mv = {1, 9, 0}, xi = 176)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$7", f = "Zip.kt", i = {}, l = {308}, m = "invokeSuspend", n = {}, s = {})
    public static final class AnonymousClass7<R> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function2<kotlinx.coroutines.flow.FlowCollector<? super R>, kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
        final /* synthetic */ kotlinx.coroutines.flow.Flow<T>[] $flowArray;
        final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $transform;
        private /* synthetic */ java.lang.Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass7(kotlinx.coroutines.flow.Flow<T>[] flowArr, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7> continuation) {
            super(2, continuation);
            this.$flowArray = flowArr;
            this.$transform = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final kotlin.coroutines.Continuation<kotlin.Unit> create(java.lang.Object obj, kotlin.coroutines.Continuation<?> continuation) {
            kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7 anonymousClass7 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7(this.$flowArray, this.$transform, continuation);
            anonymousClass7.L$0 = obj;
            return anonymousClass7;
        }

        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
            return ((kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7) create(flowCollector, continuation)).invokeSuspend(kotlin.Unit.INSTANCE);
        }

        /* JADX INFO: Add missing generic type declarations: [T] */
        /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$7$1, reason: invalid class name */
        /* JADX INFO: compiled from: Zip.kt */
        @kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\u0010\u0000\u001a\f\u0012\u0006\u0012\u0004\u0018\u0001H\u0002\u0018\u00010\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "R", "invoke", "()[Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 176)
        public static final class AnonymousClass1<T> extends kotlin.jvm.internal.Lambda implements kotlin.jvm.functions.Function0<T[]> {
            final /* synthetic */ kotlinx.coroutines.flow.Flow<T>[] $flowArray;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public AnonymousClass1(kotlinx.coroutines.flow.Flow<T>[] flowArr) {
                super(0);
                this.$flowArray = flowArr;
            }

            @Override // kotlin.jvm.functions.Function0
            public final T[] invoke() {
                int length = this.$flowArray.length;
                kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(0, "T?");
                return (T[]) new java.lang.Object[length];
            }
        }

        /* JADX INFO: Add missing generic type declarations: [T] */
        /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$7$2, reason: invalid class name */
        /* JADX INFO: compiled from: Zip.kt */
        @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", ""}, k = 3, mv = {1, 9, 0}, xi = 176)
        @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineTransform$7$2", f = "Zip.kt", i = {}, l = {308}, m = "invokeSuspend", n = {}, s = {})
        public static final class AnonymousClass2<T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
            final /* synthetic */ kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> $transform;
            private /* synthetic */ java.lang.Object L$0;
            /* synthetic */ java.lang.Object L$1;
            int label;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            public AnonymousClass2(kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function3, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass2> continuation) {
                super(3, continuation);
                this.$transform = function3;
            }

            @Override // kotlin.jvm.functions.Function3
            public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, T[] tArr, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
                kotlin.jvm.internal.Intrinsics.needClassReification();
                kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass2 anonymousClass2 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass2(this.$transform, continuation);
                anonymousClass2.L$0 = flowCollector;
                anonymousClass2.L$1 = tArr;
                return anonymousClass2.invokeSuspend(kotlin.Unit.INSTANCE);
            }

            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$ArrayArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            public final java.lang.Object invokeSuspend(java.lang.Object obj) throws java.lang.Throwable {
                java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        kotlin.ResultKt.throwOnFailure(obj);
                        kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                        java.lang.Object[] objArr = (java.lang.Object[]) this.L$1;
                        kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> function3 = this.$transform;
                        this.L$0 = null;
                        this.label = 1;
                        if (function3.invoke(flowCollector, objArr, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        kotlin.ResultKt.throwOnFailure(obj);
                        break;
                    default:
                        throw new java.lang.IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                return kotlin.Unit.INSTANCE;
            }

            /* JADX WARN: Type inference fix 'apply assigned field type' failed
            java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$ArrayArg
            	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
            	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
            	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
            	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
             */
            public final java.lang.Object invokeSuspend$$forInline(java.lang.Object obj) {
                this.$transform.invoke((kotlinx.coroutines.flow.FlowCollector) this.L$0, (java.lang.Object[]) this.L$1, this);
                return kotlin.Unit.INSTANCE;
            }
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object $result) throws java.lang.Throwable {
            java.lang.Object coroutine_suspended = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    kotlin.ResultKt.throwOnFailure($result);
                    kotlinx.coroutines.flow.FlowCollector $this$flow = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
                    kotlinx.coroutines.flow.Flow<T>[] flowArr = this.$flowArray;
                    kotlin.jvm.internal.Intrinsics.needClassReification();
                    kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass1(this.$flowArray);
                    kotlin.jvm.internal.Intrinsics.needClassReification();
                    this.label = 1;
                    if (kotlinx.coroutines.flow.internal.CombineKt.combineInternal($this$flow, flowArr, anonymousClass1, new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass2(this.$transform, null), this) == coroutine_suspended) {
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

        public final java.lang.Object invokeSuspend$$forInline(java.lang.Object $result) {
            kotlinx.coroutines.flow.FlowCollector $this$flow = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
            kotlinx.coroutines.flow.Flow<T>[] flowArr = this.$flowArray;
            kotlin.jvm.internal.Intrinsics.needClassReification();
            kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass1 anonymousClass1 = new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass1(this.$flowArray);
            kotlin.jvm.internal.Intrinsics.needClassReification();
            kotlin.jvm.internal.InlineMarker.mark(0);
            kotlinx.coroutines.flow.internal.CombineKt.combineInternal($this$flow, flowArr, anonymousClass1, new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7.AnonymousClass2(this.$transform, null), this);
            kotlin.jvm.internal.InlineMarker.mark(1);
            return kotlin.Unit.INSTANCE;
        }
    }

    public static final /* synthetic */ <T, R> kotlinx.coroutines.flow.Flow<R> combineTransform(java.lang.Iterable<? extends kotlinx.coroutines.flow.Flow<? extends T>> flows, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.flow.FlowCollector<? super R>, ? super T[], ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flows, "flows");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Collection $this$toTypedArray$iv = kotlin.collections.CollectionsKt.toList(flows);
        kotlinx.coroutines.flow.Flow[] flowArray = (kotlinx.coroutines.flow.Flow[]) $this$toTypedArray$iv.toArray(new kotlinx.coroutines.flow.Flow[0]);
        kotlin.jvm.internal.Intrinsics.needClassReification();
        return kotlinx.coroutines.flow.FlowKt.flow(new kotlinx.coroutines.flow.FlowKt__ZipKt.AnonymousClass7(flowArray, transform, null));
    }

    public static final <T1, T2, R> kotlinx.coroutines.flow.Flow<R> zip(kotlinx.coroutines.flow.Flow<? extends T1> flow, kotlinx.coroutines.flow.Flow<? extends T2> other, kotlin.jvm.functions.Function3<? super T1, ? super T2, ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(flow, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return kotlinx.coroutines.flow.internal.CombineKt.zipImpl(flow, other, transform);
    }
}
