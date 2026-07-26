package kotlinx.coroutines.channels;

/* JADX INFO: compiled from: BufferedChannel.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000À\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b#\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000e\n\u0002\b\"\b\u0010\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002:\u0004Þ\u0001ß\u0001B1\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\"\b\u0002\u0010\u0005\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0006j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\b¢\u0006\u0002\u0010\tJ\u0010\u0010P\u001a\u00020\u001c2\u0006\u0010Q\u001a\u00020\u0010H\u0002J\u0006\u0010R\u001a\u00020\u0007J\u0010\u0010R\u001a\u00020\u001c2\b\u0010S\u001a\u0004\u0018\u00010\u0016J\u0016\u0010R\u001a\u00020\u00072\u000e\u0010S\u001a\n\u0018\u00010Tj\u0004\u0018\u0001`UJ\u0017\u0010V\u001a\u00020\u001c2\b\u0010S\u001a\u0004\u0018\u00010\u0016H\u0010¢\u0006\u0002\bWJ\u001e\u0010X\u001a\u00020\u00072\f\u0010Y\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010K\u001a\u00020\u0010H\u0002J\u0006\u0010Z\u001a\u00020\u0007J\u0012\u0010[\u001a\u00020\u001c2\b\u0010S\u001a\u0004\u0018\u00010\u0016H\u0016J\u000e\u0010\\\u001a\b\u0012\u0004\u0012\u00028\u00000\u0014H\u0002J\u001a\u0010]\u001a\u00020\u001c2\b\u0010S\u001a\u0004\u0018\u00010\u00162\u0006\u0010R\u001a\u00020\u001cH\u0014J\u0010\u0010^\u001a\u00020\u00072\u0006\u0010_\u001a\u00020\u0010H\u0002J\u0016\u0010`\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010_\u001a\u00020\u0010H\u0002J\b\u0010a\u001a\u00020\u0007H\u0002J\u0010\u0010b\u001a\u00020\u00072\u0006\u0010c\u001a\u00020\u0010H\u0004J\b\u0010d\u001a\u00020\u0007H\u0002J.\u0010e\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00142\u0006\u0010f\u001a\u00020\u00102\f\u0010g\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010h\u001a\u00020\u0010H\u0002J&\u0010i\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00142\u0006\u0010f\u001a\u00020\u00102\f\u0010g\u001a\b\u0012\u0004\u0012\u00028\u00000\u0014H\u0002J&\u0010j\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00142\u0006\u0010f\u001a\u00020\u00102\f\u0010g\u001a\b\u0012\u0004\u0012\u00028\u00000\u0014H\u0002J\r\u0010k\u001a\u00020\u001cH\u0000¢\u0006\u0002\blJ\u0012\u0010m\u001a\u00020\u00072\b\b\u0002\u0010n\u001a\u00020\u0010H\u0002J\b\u0010o\u001a\u00020\u0007H\u0002J-\u0010p\u001a\u00020\u00072#\u0010q\u001a\u001f\u0012\u0015\u0012\u0013\u0018\u00010\u0016¢\u0006\f\b:\u0012\b\b;\u0012\u0004\b\b(S\u0012\u0004\u0012\u00020\u00070\u0006H\u0016J&\u0010r\u001a\u00020\u001c2\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0006\u0010u\u001a\u00020\u0010H\u0002J\u0018\u0010v\u001a\u00020\u001c2\u0006\u0010w\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u000f\u0010x\u001a\b\u0012\u0004\u0012\u00028\u00000yH\u0096\u0002J\u0016\u0010z\u001a\u00020\u00102\f\u0010Y\u001a\b\u0012\u0004\u0012\u00028\u00000\u0014H\u0002J\b\u0010{\u001a\u00020\u0007H\u0002J\b\u0010|\u001a\u00020\u0007H\u0002J\b\u0010}\u001a\u00020\u0007H\u0002J\u001e\u0010~\u001a\u00020\u00072\u0006\u0010f\u001a\u00020\u00102\f\u0010g\u001a\b\u0012\u0004\u0012\u00028\u00000\u0014H\u0002J\b\u0010\u007f\u001a\u00020\u0007H\u0014J\u001f\u0010\u0080\u0001\u001a\u00020\u00072\u0014\u0010\u0081\u0001\u001a\u000f\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000,0\u0082\u0001H\u0002J\u0019\u0010\u0083\u0001\u001a\u00020\u00072\u000e\u0010\u0081\u0001\u001a\t\u0012\u0004\u0012\u00028\u00000\u0082\u0001H\u0002J\u0015\u0010\u0084\u0001\u001a\u00020\u00072\n\u0010<\u001a\u0006\u0012\u0002\b\u000309H\u0002J$\u0010\u0085\u0001\u001a\u00020\u00072\u0007\u0010\u0086\u0001\u001a\u00028\u00002\n\u0010<\u001a\u0006\u0012\u0002\b\u000309H\u0002¢\u0006\u0003\u0010\u0087\u0001J\u0019\u0010\u0088\u0001\u001a\u00020\u00072\u0007\u0010\u0086\u0001\u001a\u00028\u0000H\u0082@¢\u0006\u0003\u0010\u0089\u0001J(\u0010\u008a\u0001\u001a\u00020\u00072\u0007\u0010\u0086\u0001\u001a\u00028\u00002\u000e\u0010\u0081\u0001\u001a\t\u0012\u0004\u0012\u00020\u00070\u0082\u0001H\u0002¢\u0006\u0003\u0010\u008b\u0001J\t\u0010\u008c\u0001\u001a\u00020\u0007H\u0014J\t\u0010\u008d\u0001\u001a\u00020\u0007H\u0014J!\u0010\u008e\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u008f\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u0090\u0001\u001a\u0004\u0018\u00010\fH\u0002J!\u0010\u0091\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u008f\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u0090\u0001\u001a\u0004\u0018\u00010\fH\u0002J!\u0010\u0092\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u008f\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u0090\u0001\u001a\u0004\u0018\u00010\fH\u0002J!\u0010\u0093\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u008f\u0001\u001a\u0004\u0018\u00010\f2\t\u0010\u0090\u0001\u001a\u0004\u0018\u00010\fH\u0002J\u0010\u0010\u0094\u0001\u001a\u00028\u0000H\u0096@¢\u0006\u0003\u0010\u0095\u0001J\u001f\u0010\u0096\u0001\u001a\b\u0012\u0004\u0012\u00028\u00000,H\u0096@ø\u0001\u0000ø\u0001\u0001¢\u0006\u0006\b\u0097\u0001\u0010\u0095\u0001J>\u0010\u0098\u0001\u001a\b\u0012\u0004\u0012\u00028\u00000,2\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0099\u0001\u001a\u00020\u0010H\u0082@ø\u0001\u0000ø\u0001\u0001¢\u0006\u0006\b\u009a\u0001\u0010\u009b\u0001J\u008c\u0002\u0010\u009c\u0001\u001a\u0003H\u009d\u0001\"\u0005\b\u0001\u0010\u009d\u00012\t\u0010\u009e\u0001\u001a\u0004\u0018\u00010\f2$\u0010\u009f\u0001\u001a\u001f\u0012\u0014\u0012\u00128\u0000¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(\u0086\u0001\u0012\u0005\u0012\u0003H\u009d\u00010\u00062V\u0010 \u0001\u001aQ\u0012\u001a\u0012\u0018\u0012\u0004\u0012\u00028\u00000\u0014¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¡\u0001\u0012\u0014\u0012\u00120\u0004¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¢\u0001\u0012\u0014\u0012\u00120\u0010¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(\u0099\u0001\u0012\u0005\u0012\u0003H\u009d\u0001082\u000f\u0010£\u0001\u001a\n\u0012\u0005\u0012\u0003H\u009d\u00010¤\u00012X\b\u0002\u0010¥\u0001\u001aQ\u0012\u001a\u0012\u0018\u0012\u0004\u0012\u00028\u00000\u0014¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¡\u0001\u0012\u0014\u0012\u00120\u0004¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¢\u0001\u0012\u0014\u0012\u00120\u0010¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(\u0099\u0001\u0012\u0005\u0012\u0003H\u009d\u000108H\u0082\b¢\u0006\u0003\u0010¦\u0001Jh\u0010§\u0001\u001a\u00020\u00072\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0099\u0001\u001a\u00020\u00102\b\u0010\u009e\u0001\u001a\u00030¨\u00012#\u0010\u009f\u0001\u001a\u001e\u0012\u0014\u0012\u00128\u0000¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(\u0086\u0001\u0012\u0004\u0012\u00020\u00070\u00062\u000e\u0010£\u0001\u001a\t\u0012\u0004\u0012\u00020\u00070¤\u0001H\u0082\bJ/\u0010©\u0001\u001a\u00028\u00002\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0099\u0001\u001a\u00020\u0010H\u0082@¢\u0006\u0003\u0010\u009b\u0001J \u0010ª\u0001\u001a\u00020\u00072\n\u0010<\u001a\u0006\u0012\u0002\b\u0003092\t\u0010\u008f\u0001\u001a\u0004\u0018\u00010\fH\u0002J \u0010«\u0001\u001a\u00020\u00072\n\u0010<\u001a\u0006\u0012\u0002\b\u0003092\t\u0010\u0086\u0001\u001a\u0004\u0018\u00010\fH\u0014J\u0017\u0010¬\u0001\u001a\u00020\u00072\f\u0010Y\u001a\b\u0012\u0004\u0012\u00028\u00000\u0014H\u0002J\u0019\u0010\u00ad\u0001\u001a\u00020\u00072\u0007\u0010\u0086\u0001\u001a\u00028\u0000H\u0096@¢\u0006\u0003\u0010\u0089\u0001J\u001c\u0010®\u0001\u001a\u00020\u001c2\u0007\u0010\u0086\u0001\u001a\u00028\u0000H\u0090@¢\u0006\u0006\b¯\u0001\u0010\u0089\u0001J\u0085\u0002\u0010°\u0001\u001a\u0003H\u009d\u0001\"\u0005\b\u0001\u0010\u009d\u00012\u0007\u0010\u0086\u0001\u001a\u00028\u00002\t\u0010\u009e\u0001\u001a\u0004\u0018\u00010\f2\u000f\u0010±\u0001\u001a\n\u0012\u0005\u0012\u0003H\u009d\u00010¤\u00012A\u0010 \u0001\u001a<\u0012\u001a\u0012\u0018\u0012\u0004\u0012\u00028\u00000\u0014¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¡\u0001\u0012\u0014\u0012\u00120\u0004¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¢\u0001\u0012\u0005\u0012\u0003H\u009d\u00010²\u00012\u000f\u0010£\u0001\u001a\n\u0012\u0005\u0012\u0003H\u009d\u00010¤\u00012o\b\u0002\u0010¥\u0001\u001ah\u0012\u001a\u0012\u0018\u0012\u0004\u0012\u00028\u00000\u0014¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¡\u0001\u0012\u0014\u0012\u00120\u0004¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(¢\u0001\u0012\u0014\u0012\u00128\u0000¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(\u0086\u0001\u0012\u0014\u0012\u00120\u0010¢\u0006\r\b:\u0012\t\b;\u0012\u0005\b\b(´\u0001\u0012\u0005\u0012\u0003H\u009d\u00010³\u0001H\u0084\bø\u0001\u0002¢\u0006\u0003\u0010µ\u0001Jb\u0010¶\u0001\u001a\u00020\u00072\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0086\u0001\u001a\u00028\u00002\u0007\u0010´\u0001\u001a\u00020\u00102\b\u0010\u009e\u0001\u001a\u00030¨\u00012\u000e\u0010±\u0001\u001a\t\u0012\u0004\u0012\u00020\u00070¤\u00012\u000e\u0010£\u0001\u001a\t\u0012\u0004\u0012\u00020\u00070¤\u0001H\u0082\b¢\u0006\u0003\u0010·\u0001J8\u0010¸\u0001\u001a\u00020\u00072\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0086\u0001\u001a\u00028\u00002\u0007\u0010´\u0001\u001a\u00020\u0010H\u0082@¢\u0006\u0003\u0010¹\u0001J\u000f\u0010º\u0001\u001a\u00020\u001cH\u0010¢\u0006\u0003\b»\u0001J\u0012\u0010º\u0001\u001a\u00020\u001c2\u0007\u0010¼\u0001\u001a\u00020\u0010H\u0003J\n\u0010½\u0001\u001a\u00030¾\u0001H\u0016J\u0010\u0010¿\u0001\u001a\u00030¾\u0001H\u0000¢\u0006\u0003\bÀ\u0001J\u001e\u0010Á\u0001\u001a\b\u0012\u0004\u0012\u00028\u00000,H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0006\bÂ\u0001\u0010Ã\u0001J'\u0010Ä\u0001\u001a\b\u0012\u0004\u0012\u00020\u00070,2\u0007\u0010\u0086\u0001\u001a\u00028\u0000H\u0016ø\u0001\u0000ø\u0001\u0001¢\u0006\u0006\bÅ\u0001\u0010Æ\u0001J(\u0010Ç\u0001\u001a\u00020\u001c2\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010È\u0001\u001a\u00020\u0010H\u0002J(\u0010É\u0001\u001a\u00020\u001c2\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010È\u0001\u001a\u00020\u0010H\u0002J5\u0010Ê\u0001\u001a\u0004\u0018\u00010\f2\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0099\u0001\u001a\u00020\u00102\t\u0010\u009e\u0001\u001a\u0004\u0018\u00010\fH\u0002J5\u0010Ë\u0001\u001a\u0004\u0018\u00010\f2\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0099\u0001\u001a\u00020\u00102\t\u0010\u009e\u0001\u001a\u0004\u0018\u00010\fH\u0002JK\u0010Ì\u0001\u001a\u00020\u00042\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0086\u0001\u001a\u00028\u00002\u0007\u0010´\u0001\u001a\u00020\u00102\t\u0010\u009e\u0001\u001a\u0004\u0018\u00010\f2\u0007\u0010Í\u0001\u001a\u00020\u001cH\u0002¢\u0006\u0003\u0010Î\u0001JK\u0010Ï\u0001\u001a\u00020\u00042\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u00042\u0007\u0010\u0086\u0001\u001a\u00028\u00002\u0007\u0010´\u0001\u001a\u00020\u00102\t\u0010\u009e\u0001\u001a\u0004\u0018\u00010\f2\u0007\u0010Í\u0001\u001a\u00020\u001cH\u0002¢\u0006\u0003\u0010Î\u0001J\u0012\u0010Ð\u0001\u001a\u00020\u00072\u0007\u0010Ñ\u0001\u001a\u00020\u0010H\u0002J\u0012\u0010Ò\u0001\u001a\u00020\u00072\u0007\u0010Ñ\u0001\u001a\u00020\u0010H\u0002J\u0017\u0010Ó\u0001\u001a\u00020\u00072\u0006\u0010u\u001a\u00020\u0010H\u0000¢\u0006\u0003\bÔ\u0001J$\u0010Õ\u0001\u001a\u00020\u0007*\u00030¨\u00012\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u0004H\u0002J$\u0010Ö\u0001\u001a\u00020\u0007*\u00030¨\u00012\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u0004H\u0002J\u000e\u0010×\u0001\u001a\u00020\u0007*\u00030¨\u0001H\u0002J\u000e\u0010Ø\u0001\u001a\u00020\u0007*\u00030¨\u0001H\u0002J\u0017\u0010Ù\u0001\u001a\u00020\u0007*\u00030¨\u00012\u0007\u0010Ú\u0001\u001a\u00020\u001cH\u0002J\u001c\u0010Û\u0001\u001a\u00020\u001c*\u00020\f2\u0007\u0010\u0086\u0001\u001a\u00028\u0000H\u0002¢\u0006\u0003\u0010Ü\u0001J#\u0010Ý\u0001\u001a\u00020\u001c*\u00020\f2\f\u0010s\u001a\b\u0012\u0004\u0012\u00028\u00000\u00142\u0006\u0010t\u001a\u00020\u0004H\u0002R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\u00020\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00140\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0015\u001a\u0004\u0018\u00010\u00168DX\u0084\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0016\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\u00020\u001c8VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u001e\u001a\u0004\b\u001b\u0010\u001fR\u001a\u0010 \u001a\u00020\u001c8VX\u0097\u0004¢\u0006\f\u0012\u0004\b!\u0010\u001e\u001a\u0004\b \u0010\u001fR\u0014\u0010\"\u001a\u00020\u001c8TX\u0094\u0004¢\u0006\u0006\u001a\u0004\b\"\u0010\u001fR\u001a\u0010#\u001a\u00020\u001c8VX\u0097\u0004¢\u0006\f\u0012\u0004\b$\u0010\u001e\u001a\u0004\b#\u0010\u001fR\u0014\u0010%\u001a\u00020\u001c8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b%\u0010\u001fR \u0010&\u001a\b\u0012\u0004\u0012\u00028\u00000'8VX\u0096\u0004¢\u0006\f\u0012\u0004\b(\u0010\u001e\u001a\u0004\b)\u0010*R&\u0010+\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000,0'8VX\u0096\u0004¢\u0006\f\u0012\u0004\b-\u0010\u001e\u001a\u0004\b.\u0010*R\"\u0010/\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000'8VX\u0096\u0004¢\u0006\f\u0012\u0004\b0\u0010\u001e\u001a\u0004\b1\u0010*R,\u00102\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0000038VX\u0096\u0004¢\u0006\f\u0012\u0004\b4\u0010\u001e\u001a\u0004\b5\u00106R*\u0010\u0005\u001a\u001c\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0006j\n\u0012\u0004\u0012\u00028\u0000\u0018\u0001`\b8\u0000X\u0081\u0004¢\u0006\u0002\n\u0000Ru\u00107\u001ac\u0012\u0017\u0012\u0015\u0012\u0002\b\u000309¢\u0006\f\b:\u0012\b\b;\u0012\u0004\b\b(<\u0012\u0015\u0012\u0013\u0018\u00010\f¢\u0006\f\b:\u0012\b\b;\u0012\u0004\b\b(=\u0012\u0015\u0012\u0013\u0018\u00010\f¢\u0006\f\b:\u0012\b\b;\u0012\u0004\b\b(>\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00070\u0006\u0018\u000108j\u0004\u0018\u0001`?X\u0082\u0004¢\u0006\b\n\u0000\u0012\u0004\b@\u0010\u001eR\u0014\u0010A\u001a\u00020\u00168BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bB\u0010\u0018R\u001a\u0010C\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00140\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010E\u001a\u00020\u00108@X\u0080\u0004¢\u0006\u0006\u001a\u0004\bF\u0010\u0012R\u0014\u0010G\u001a\u00020\u00168DX\u0084\u0004¢\u0006\u0006\u001a\u0004\bH\u0010\u0018R\u001a\u0010I\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00140\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010J\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010K\u001a\u00020\u00108@X\u0080\u0004¢\u0006\u0006\u001a\u0004\bL\u0010\u0012R\u0018\u0010M\u001a\u00020\u001c*\u00020\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bM\u0010NR\u0018\u0010O\u001a\u00020\u001c*\u00020\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\bO\u0010N\u0082\u0002\u0012\n\u0002\b!\n\u0005\b¡\u001e0\u0001\n\u0005\b\u009920\u0001¨\u0006à\u0001"}, d2 = {"Lkotlinx/coroutines/channels/BufferedChannel;", "E", "Lkotlinx/coroutines/channels/Channel;", "capacity", "", "onUndeliveredElement", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "(ILkotlin/jvm/functions/Function1;)V", "_closeCause", "Lkotlinx/atomicfu/AtomicRef;", "", "bufferEnd", "Lkotlinx/atomicfu/AtomicLong;", "bufferEndCounter", "", "getBufferEndCounter", "()J", "bufferEndSegment", "Lkotlinx/coroutines/channels/ChannelSegment;", "closeCause", "", "getCloseCause", "()Ljava/lang/Throwable;", "closeHandler", "completedExpandBuffersAndPauseFlag", "isClosedForReceive", "", "isClosedForReceive$annotations", "()V", "()Z", "isClosedForSend", "isClosedForSend$annotations", "isConflatedDropOldest", "isEmpty", "isEmpty$annotations", "isRendezvousOrUnlimited", "onReceive", "Lkotlinx/coroutines/selects/SelectClause1;", "getOnReceive$annotations", "getOnReceive", "()Lkotlinx/coroutines/selects/SelectClause1;", "onReceiveCatching", "Lkotlinx/coroutines/channels/ChannelResult;", "getOnReceiveCatching$annotations", "getOnReceiveCatching", "onReceiveOrNull", "getOnReceiveOrNull$annotations", "getOnReceiveOrNull", "onSend", "Lkotlinx/coroutines/selects/SelectClause2;", "getOnSend$annotations", "getOnSend", "()Lkotlinx/coroutines/selects/SelectClause2;", "onUndeliveredElementReceiveCancellationConstructor", "Lkotlin/Function3;", "Lkotlinx/coroutines/selects/SelectInstance;", "Lkotlin/ParameterName;", "name", "select", "param", "internalResult", "Lkotlinx/coroutines/selects/OnCancellationConstructor;", "getOnUndeliveredElementReceiveCancellationConstructor$annotations", "receiveException", "getReceiveException", "receiveSegment", "receivers", "receiversCounter", "getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "sendException", "getSendException", "sendSegment", "sendersAndCloseStatus", "sendersCounter", "getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "isClosedForReceive0", "(J)Z", "isClosedForSend0", "bufferOrRendezvousSend", "curSenders", "cancel", "cause", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "cancelImpl", "cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "cancelSuspendedReceiveRequests", "lastSegment", "checkSegmentStructureInvariants", "close", "closeLinkedList", "closeOrCancelImpl", "completeCancel", "sendersCur", "completeClose", "completeCloseOrCancel", "dropFirstElementUntilTheSpecifiedCellIsInTheBuffer", "globalCellIndex", "expandBuffer", "findSegmentBufferEnd", "id", "startFrom", "currentBufferEndCounter", "findSegmentReceive", "findSegmentSend", "hasElements", "hasElements$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "incCompletedExpandBufferAttempts", "nAttempts", "invokeCloseHandler", "invokeOnClose", "handler", "isCellNonEmpty", "segment", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "globalIndex", "isClosed", "sendersAndCloseStatusCur", "iterator", "Lkotlinx/coroutines/channels/ChannelIterator;", "markAllEmptyCellsAsClosed", "markCancellationStarted", "markCancelled", "markClosed", "moveSegmentBufferEndToSpecifiedOrLast", "onClosedIdempotent", "onClosedReceiveCatchingOnNoWaiterSuspend", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "onClosedReceiveOnNoWaiterSuspend", "onClosedSelectOnReceive", "onClosedSelectOnSend", "element", "(Ljava/lang/Object;Lkotlinx/coroutines/selects/SelectInstance;)V", "onClosedSend", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onClosedSendOnNoWaiterSuspend", "(Ljava/lang/Object;Lkotlinx/coroutines/CancellableContinuation;)V", "onReceiveDequeued", "onReceiveEnqueued", "processResultSelectReceive", "ignoredParam", "selectResult", "processResultSelectReceiveCatching", "processResultSelectReceiveOrNull", "processResultSelectSend", "receive", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "receiveCatching", "receiveCatching-JP2dKIU", "receiveCatchingOnNoWaiterSuspend", com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD, "receiveCatchingOnNoWaiterSuspend-GKJJFZk", "(Lkotlinx/coroutines/channels/ChannelSegment;IJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "receiveImpl", "R", "waiter", "onElementRetrieved", "onSuspend", "segm", "i", "onClosed", "Lkotlin/Function0;", "onNoWaiterSuspend", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "receiveImplOnNoWaiter", "Lkotlinx/coroutines/Waiter;", "receiveOnNoWaiterSuspend", "registerSelectForReceive", "registerSelectForSend", "removeUnprocessedElements", "send", "sendBroadcast", "sendBroadcast$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "sendImpl", "onRendezvousOrBuffered", "Lkotlin/Function2;", "Lkotlin/Function4;", "s", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function4;)Ljava/lang/Object;", "sendImplOnNoWaiter", "(Lkotlinx/coroutines/channels/ChannelSegment;ILjava/lang/Object;JLkotlinx/coroutines/Waiter;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "sendOnNoWaiterSuspend", "(Lkotlinx/coroutines/channels/ChannelSegment;ILjava/lang/Object;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "shouldSendSuspend", "shouldSendSuspend$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "curSendersAndCloseStatus", "toString", "", "toStringDebug", "toStringDebug$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "tryReceive", "tryReceive-PtdJZtk", "()Ljava/lang/Object;", "trySend", "trySend-JP2dKIU", "(Ljava/lang/Object;)Ljava/lang/Object;", "updateCellExpandBuffer", "b", "updateCellExpandBufferSlow", "updateCellReceive", "updateCellReceiveSlow", "updateCellSend", "closed", "(Lkotlinx/coroutines/channels/ChannelSegment;ILjava/lang/Object;JLjava/lang/Object;Z)I", "updateCellSendSlow", "updateReceiversCounterIfLower", "value", "updateSendersCounterIfLower", "waitExpandBufferCompletion", "waitExpandBufferCompletion$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "prepareReceiverForSuspension", "prepareSenderForSuspension", "resumeReceiverOnClosedChannel", "resumeSenderOnCancelledChannel", "resumeWaiterOnClosedChannel", "receiver", "tryResumeReceiver", "(Ljava/lang/Object;Ljava/lang/Object;)Z", "tryResumeSender", "BufferedChannelIterator", "SendBroadcast", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class BufferedChannel<E> implements kotlinx.coroutines.channels.Channel<E> {
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> _closeCause;
    private final kotlinx.atomicfu.AtomicLong bufferEnd;
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> bufferEndSegment;
    private final int capacity;
    private final kotlinx.atomicfu.AtomicRef<java.lang.Object> closeHandler;
    private final kotlinx.atomicfu.AtomicLong completedExpandBuffersAndPauseFlag;
    public final kotlin.jvm.functions.Function1<E, kotlin.Unit> onUndeliveredElement;
    private final kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> onUndeliveredElementReceiveCancellationConstructor;
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> receiveSegment;
    private final kotlinx.atomicfu.AtomicLong receivers;
    private final kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> sendSegment;
    private final kotlinx.atomicfu.AtomicLong sendersAndCloseStatus;

    public static /* synthetic */ void getOnReceive$annotations() {
    }

    public static /* synthetic */ void getOnReceiveCatching$annotations() {
    }

    public static /* synthetic */ void getOnReceiveOrNull$annotations() {
    }

    public static /* synthetic */ void getOnSend$annotations() {
    }

    private static /* synthetic */ void getOnUndeliveredElementReceiveCancellationConstructor$annotations() {
    }

    public static /* synthetic */ void isClosedForReceive$annotations() {
    }

    public static /* synthetic */ void isClosedForSend$annotations() {
    }

    public static /* synthetic */ void isEmpty$annotations() {
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public java.lang.Object receive(kotlin.coroutines.Continuation<? super E> continuation) {
        return receive$suspendImpl(this, continuation);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    /* JADX INFO: renamed from: receiveCatching-JP2dKIU, reason: not valid java name */
    public java.lang.Object mo12813receiveCatchingJP2dKIU(kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<? extends E>> continuation) {
        return m12811receiveCatchingJP2dKIU$suspendImpl(this, continuation);
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public java.lang.Object send(E e, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return send$suspendImpl(this, e, continuation);
    }

    public java.lang.Object sendBroadcast$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(E e, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        return sendBroadcast$suspendImpl(this, e, continuation);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public BufferedChannel(int capacity, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1) {
        kotlinx.coroutines.channels.ChannelSegment channelSegment;
        this.capacity = capacity;
        this.onUndeliveredElement = function1;
        if (!(this.capacity >= 0)) {
            throw new java.lang.IllegalArgumentException(("Invalid channel capacity: " + this.capacity + ", should be >=0").toString());
        }
        this.sendersAndCloseStatus = kotlinx.atomicfu.AtomicFU.atomic(0L);
        this.receivers = kotlinx.atomicfu.AtomicFU.atomic(0L);
        this.bufferEnd = kotlinx.atomicfu.AtomicFU.atomic(kotlinx.coroutines.channels.BufferedChannelKt.initialBufferEnd(this.capacity));
        this.completedExpandBuffersAndPauseFlag = kotlinx.atomicfu.AtomicFU.atomic(getBufferEndCounter());
        kotlinx.coroutines.channels.ChannelSegment firstSegment = new kotlinx.coroutines.channels.ChannelSegment(0L, null, this, 3);
        this.sendSegment = kotlinx.atomicfu.AtomicFU.atomic(firstSegment);
        this.receiveSegment = kotlinx.atomicfu.AtomicFU.atomic(firstSegment);
        if (isRendezvousOrUnlimited()) {
            channelSegment = kotlinx.coroutines.channels.BufferedChannelKt.NULL_SEGMENT;
            kotlin.jvm.internal.Intrinsics.checkNotNull(channelSegment, "null cannot be cast to non-null type kotlinx.coroutines.channels.ChannelSegment<E of kotlinx.coroutines.channels.BufferedChannel>");
        } else {
            channelSegment = firstSegment;
        }
        this.bufferEndSegment = kotlinx.atomicfu.AtomicFU.atomic(channelSegment);
        this.onUndeliveredElementReceiveCancellationConstructor = this.onUndeliveredElement != null ? (kotlin.jvm.functions.Function3) new kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends kotlin.Unit>>(this) { // from class: kotlinx.coroutines.channels.BufferedChannel$onUndeliveredElementReceiveCancellationConstructor$1$1
            final /* synthetic */ kotlinx.coroutines.channels.BufferedChannel<E> this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(3);
                this.this$0 = this;
            }

            @Override // kotlin.jvm.functions.Function3
            public final kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> invoke(final kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object obj, final java.lang.Object element) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
                final kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel = this.this$0;
                return new kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>() { // from class: kotlinx.coroutines.channels.BufferedChannel$onUndeliveredElementReceiveCancellationConstructor$1$1.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
                        invoke2(th);
                        return kotlin.Unit.INSTANCE;
                    }

                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(java.lang.Throwable it) {
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                        if (element != kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
                            kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElement(bufferedChannel.onUndeliveredElement, element, select.getContext());
                        }
                    }
                };
            }
        } : null;
        this._closeCause = kotlinx.atomicfu.AtomicFU.atomic(kotlinx.coroutines.channels.BufferedChannelKt.NO_CLOSE_CAUSE);
        this.closeHandler = kotlinx.atomicfu.AtomicFU.atomic((java.lang.Object) null);
    }

    public /* synthetic */ BufferedChannel(int i, kotlin.jvm.functions.Function1 function1, int i2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(i, (i2 & 2) != 0 ? null : function1);
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in the favour of 'trySend' method", replaceWith = @kotlin.ReplaceWith(expression = "trySend(element).isSuccess", imports = {}))
    public boolean offer(E e) {
        return kotlinx.coroutines.channels.Channel.DefaultImpls.offer(this, e);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in the favour of 'tryReceive'. Please note that the provided replacement does not rethrow channel's close cause as 'poll' did, for the precise replacement please refer to the 'poll' documentation", replaceWith = @kotlin.ReplaceWith(expression = "tryReceive().getOrNull()", imports = {}))
    public E poll() {
        return (E) kotlinx.coroutines.channels.Channel.DefaultImpls.poll(this);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated in favor of 'receiveCatching'. Please note that the provided replacement does not rethrow channel's close cause as 'receiveOrNull' did, for the detailed replacement please refer to the 'receiveOrNull' documentation", replaceWith = @kotlin.ReplaceWith(expression = "receiveCatching().getOrNull()", imports = {}))
    public java.lang.Object receiveOrNull(kotlin.coroutines.Continuation<? super E> continuation) {
        return kotlinx.coroutines.channels.Channel.DefaultImpls.receiveOrNull(this, continuation);
    }

    public final long getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        long $this$sendersCounter$iv = this.sendersAndCloseStatus.getValue();
        return $this$sendersCounter$iv & 1152921504606846975L;
    }

    public final long getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return this.receivers.getValue();
    }

    private final long getBufferEndCounter() {
        return this.bufferEnd.getValue();
    }

    private final boolean isRendezvousOrUnlimited() {
        long it = getBufferEndCounter();
        return it == 0 || it == Long.MAX_VALUE;
    }

    static /* synthetic */ <E> java.lang.Object send$suspendImpl(kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel, E e, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) throws java.lang.Throwable {
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment;
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment2 = (kotlinx.coroutines.channels.ChannelSegment) ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).sendSegment.getValue();
        while (true) {
            long sendersAndCloseStatusCur$iv = ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).sendersAndCloseStatus.getAndIncrement();
            long $this$sendersCounter$iv$iv = sendersAndCloseStatusCur$iv & 1152921504606846975L;
            boolean closed$iv = bufferedChannel.isClosedForSend0(sendersAndCloseStatusCur$iv);
            long id$iv = $this$sendersCounter$iv$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i$iv = (int) ($this$sendersCounter$iv$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (channelSegment2.id != id$iv) {
                kotlinx.coroutines.channels.ChannelSegment<E> channelSegmentFindSegmentSend = bufferedChannel.findSegmentSend(id$iv, channelSegment2);
                if (channelSegmentFindSegmentSend != null) {
                    channelSegment = channelSegmentFindSegmentSend;
                } else if (closed$iv) {
                    java.lang.Object objOnClosedSend = bufferedChannel.onClosedSend(e, continuation);
                    if (objOnClosedSend == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                        return objOnClosedSend;
                    }
                }
            } else {
                channelSegment = channelSegment2;
            }
            switch (bufferedChannel.updateCellSend(channelSegment, i$iv, e, $this$sendersCounter$iv$iv, null, closed$iv)) {
                case 0:
                    channelSegment.cleanPrev();
                    break;
                case 1:
                    break;
                case 2:
                    if (closed$iv) {
                        channelSegment.onSlotCleaned();
                        java.lang.Object objOnClosedSend2 = bufferedChannel.onClosedSend(e, continuation);
                        if (objOnClosedSend2 == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                            return objOnClosedSend2;
                        }
                    } else if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                        throw new java.lang.AssertionError();
                    }
                case 3:
                    java.lang.Object objSendOnNoWaiterSuspend = bufferedChannel.sendOnNoWaiterSuspend(channelSegment, i$iv, e, $this$sendersCounter$iv$iv, continuation);
                    if (objSendOnNoWaiterSuspend == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                        return objSendOnNoWaiterSuspend;
                    }
                    break;
                case 4:
                    if ($this$sendersCounter$iv$iv < bufferedChannel.getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        channelSegment.cleanPrev();
                    }
                    java.lang.Object objOnClosedSend3 = bufferedChannel.onClosedSend(e, continuation);
                    if (objOnClosedSend3 == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                        return objOnClosedSend3;
                    }
                    break;
                case 5:
                    channelSegment.cleanPrev();
                    channelSegment2 = channelSegment;
                    break;
                default:
                    channelSegment2 = channelSegment;
                    break;
            }
        }
        return kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object onClosedSend(E e, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        java.lang.Throwable thRecoverFromStackFrame;
        kotlinx.coroutines.internal.UndeliveredElementException it;
        kotlinx.coroutines.internal.UndeliveredElementException undeliveredElementExceptionRecoverFromStackFrame;
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl continuation2 = cancellable$iv;
        kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = this.onUndeliveredElement;
        if (function1 != null && (it = kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, e, null, 2, null)) != null) {
            kotlin.ExceptionsKt.addSuppressed(it, getSendException());
            kotlinx.coroutines.CancellableContinuationImpl $this$resumeWithStackTrace$iv = continuation2;
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && ($this$resumeWithStackTrace$iv instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                undeliveredElementExceptionRecoverFromStackFrame = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(it, $this$resumeWithStackTrace$iv);
            } else {
                undeliveredElementExceptionRecoverFromStackFrame = it;
            }
            $this$resumeWithStackTrace$iv.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(undeliveredElementExceptionRecoverFromStackFrame)));
        } else {
            kotlinx.coroutines.CancellableContinuationImpl $this$resumeWithStackTrace$iv2 = continuation2;
            java.lang.Throwable exception$iv = getSendException();
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && ($this$resumeWithStackTrace$iv2 instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                thRecoverFromStackFrame = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception$iv, $this$resumeWithStackTrace$iv2);
            } else {
                thRecoverFromStackFrame = exception$iv;
            }
            $this$resumeWithStackTrace$iv2.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(thRecoverFromStackFrame)));
        }
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED() ? result : kotlin.Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:63:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01f0 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object sendOnNoWaiterSuspend(kotlinx.coroutines.channels.ChannelSegment<E> r30, int r31, E r32, long r33, kotlin.coroutines.Continuation<? super kotlin.Unit> r35) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 554
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BufferedChannel.sendOnNoWaiterSuspend(kotlinx.coroutines.channels.ChannelSegment, int, java.lang.Object, long, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void prepareSenderForSuspension(kotlinx.coroutines.Waiter $this$prepareSenderForSuspension, kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, int index) {
        $this$prepareSenderForSuspension.invokeOnCancellation(channelSegment, kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE + index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onClosedSendOnNoWaiterSuspend(E element, kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> cont) {
        kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = this.onUndeliveredElement;
        if (function1 != null) {
            kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElement(function1, element, cont.getContext());
        }
        kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> cancellableContinuation = cont;
        java.lang.Throwable exception$iv = getSendException();
        if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (cont instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            exception$iv = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(exception$iv, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) cont);
        }
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        cancellableContinuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(exception$iv)));
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    /* JADX INFO: renamed from: trySend-JP2dKIU */
    public java.lang.Object mo12809trySendJP2dKIU(E element) {
        kotlinx.coroutines.channels.ChannelSegment segment$iv;
        kotlinx.coroutines.channels.ChannelSegment segment$iv2;
        if (!shouldSendSuspend(this.sendersAndCloseStatus.getValue())) {
            java.lang.Object waiter$iv = kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND;
            int $i$f$sendImpl = 0;
            kotlinx.coroutines.channels.ChannelSegment segment$iv3 = (kotlinx.coroutines.channels.ChannelSegment) this.sendSegment.getValue();
            while (true) {
                long sendersAndCloseStatusCur$iv = this.sendersAndCloseStatus.getAndIncrement();
                long $this$sendersCounter$iv$iv = sendersAndCloseStatusCur$iv & 1152921504606846975L;
                boolean closed$iv = isClosedForSend0(sendersAndCloseStatusCur$iv);
                long id$iv = $this$sendersCounter$iv$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
                int i$iv = (int) ($this$sendersCounter$iv$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
                if (segment$iv3.id != id$iv) {
                    kotlinx.coroutines.channels.ChannelSegment segment$iv4 = findSegmentSend(id$iv, segment$iv3);
                    if (segment$iv4 != null) {
                        segment$iv = segment$iv4;
                    } else if (closed$iv) {
                        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getSendException());
                    }
                } else {
                    segment$iv = segment$iv3;
                }
                kotlinx.coroutines.channels.ChannelSegment segment$iv5 = segment$iv;
                int $i$f$sendImpl2 = $i$f$sendImpl;
                switch (updateCellSend(segment$iv, i$iv, element, $this$sendersCounter$iv$iv, waiter$iv, closed$iv)) {
                    case 0:
                        segment$iv5.cleanPrev();
                        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(kotlin.Unit.INSTANCE);
                    case 1:
                        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(kotlin.Unit.INSTANCE);
                    case 2:
                        if (closed$iv) {
                            segment$iv5.onSlotCleaned();
                            return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getSendException());
                        }
                        kotlinx.coroutines.Waiter waiter = waiter$iv instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) waiter$iv : null;
                        if (waiter != null) {
                            segment$iv2 = segment$iv5;
                            prepareSenderForSuspension(waiter, segment$iv2, i$iv);
                        } else {
                            segment$iv2 = segment$iv5;
                        }
                        kotlinx.coroutines.channels.ChannelSegment segm = segment$iv2;
                        segm.onSlotCleaned();
                        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12833failurePtdJZtk();
                    case 3:
                        throw new java.lang.IllegalStateException("unexpected".toString());
                    case 4:
                        if ($this$sendersCounter$iv$iv < getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                            segment$iv5.cleanPrev();
                        }
                        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getSendException());
                    case 5:
                        segment$iv5.cleanPrev();
                    default:
                        segment$iv3 = segment$iv5;
                        $i$f$sendImpl = $i$f$sendImpl2;
                        break;
                }
            }
        } else {
            return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12833failurePtdJZtk();
        }
    }

    static /* synthetic */ <E> java.lang.Object sendBroadcast$suspendImpl(kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel, E e, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv;
        kotlinx.coroutines.channels.ChannelSegment segment$iv;
        int $i$f$suspendCancellableCoroutine = 0;
        kotlinx.coroutines.CancellableContinuationImpl cancellable$iv2 = new kotlinx.coroutines.CancellableContinuationImpl(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv2.initCancellability();
        kotlinx.coroutines.CancellableContinuationImpl cont = cancellable$iv2;
        if (!(bufferedChannel.onUndeliveredElement == null)) {
            throw new java.lang.IllegalStateException("the `onUndeliveredElement` feature is unsupported for `sendBroadcast(e)`".toString());
        }
        java.lang.Object waiter$iv = new kotlinx.coroutines.channels.BufferedChannel.SendBroadcast(cont);
        kotlinx.coroutines.channels.ChannelSegment segment$iv2 = (kotlinx.coroutines.channels.ChannelSegment) ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).sendSegment.getValue();
        while (true) {
            long sendersAndCloseStatusCur$iv = ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).sendersAndCloseStatus.getAndIncrement();
            long $this$sendersCounter$iv$iv = sendersAndCloseStatusCur$iv & 1152921504606846975L;
            boolean closed$iv = bufferedChannel.isClosedForSend0(sendersAndCloseStatusCur$iv);
            long id$iv = $this$sendersCounter$iv$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            cancellable$iv = cancellable$iv2;
            int i$iv = (int) ($this$sendersCounter$iv$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment$iv2.id != id$iv) {
                kotlinx.coroutines.channels.ChannelSegment segment$iv3 = bufferedChannel.findSegmentSend(id$iv, segment$iv2);
                if (segment$iv3 != null) {
                    segment$iv = segment$iv3;
                } else if (closed$iv) {
                    kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                    cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.coroutines.jvm.internal.Boxing.boxBoolean(false)));
                } else {
                    cancellable$iv2 = cancellable$iv;
                }
            } else {
                segment$iv = segment$iv2;
            }
            int $i$f$suspendCancellableCoroutine2 = $i$f$suspendCancellableCoroutine;
            java.lang.Object waiter$iv2 = waiter$iv;
            switch (bufferedChannel.updateCellSend(segment$iv, i$iv, e, $this$sendersCounter$iv$iv, waiter$iv, closed$iv)) {
                case 0:
                    segment$iv.cleanPrev();
                    kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                    cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.coroutines.jvm.internal.Boxing.boxBoolean(true)));
                    break;
                case 1:
                    kotlin.Result.Companion companion3 = kotlin.Result.INSTANCE;
                    cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.coroutines.jvm.internal.Boxing.boxBoolean(true)));
                    break;
                case 2:
                    if (closed$iv) {
                        segment$iv.onSlotCleaned();
                        kotlin.Result.Companion companion4 = kotlin.Result.INSTANCE;
                        cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.coroutines.jvm.internal.Boxing.boxBoolean(false)));
                    } else {
                        kotlinx.coroutines.channels.BufferedChannel.SendBroadcast sendBroadcast = waiter$iv2 instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) waiter$iv2 : null;
                        if (sendBroadcast != null) {
                            bufferedChannel.prepareSenderForSuspension(sendBroadcast, segment$iv, i$iv);
                        }
                    }
                    break;
                case 3:
                    throw new java.lang.IllegalStateException("unexpected".toString());
                case 4:
                    if ($this$sendersCounter$iv$iv < bufferedChannel.getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        segment$iv.cleanPrev();
                    }
                    kotlin.Result.Companion companion5 = kotlin.Result.INSTANCE;
                    cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.coroutines.jvm.internal.Boxing.boxBoolean(false)));
                    break;
                case 5:
                    segment$iv.cleanPrev();
                default:
                    waiter$iv = waiter$iv2;
                    segment$iv2 = segment$iv;
                    cancellable$iv2 = cancellable$iv;
                    $i$f$suspendCancellableCoroutine = $i$f$suspendCancellableCoroutine2;
                    break;
            }
        }
        java.lang.Object result = cancellable$iv.getResult();
        if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    /* JADX INFO: compiled from: BufferedChannel.kt */
    @kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005J\u001d\u0010\b\u001a\u00020\t2\n\u0010\n\u001a\u0006\u0012\u0002\b\u00030\u000b2\u0006\u0010\f\u001a\u00020\rH\u0096\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/channels/BufferedChannel$SendBroadcast;", "Lkotlinx/coroutines/Waiter;", "cont", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlinx/coroutines/CancellableContinuation;)V", "getCont", "()Lkotlinx/coroutines/CancellableContinuation;", "invokeOnCancellation", "", "segment", "Lkotlinx/coroutines/internal/Segment;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class SendBroadcast implements kotlinx.coroutines.Waiter {
        private final /* synthetic */ kotlinx.coroutines.CancellableContinuationImpl<java.lang.Boolean> $$delegate_0;
        private final kotlinx.coroutines.CancellableContinuation<java.lang.Boolean> cont;

        @Override // kotlinx.coroutines.Waiter
        public void invokeOnCancellation(kotlinx.coroutines.internal.Segment<?> segment, int index) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(segment, "segment");
            this.$$delegate_0.invokeOnCancellation(segment, index);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public SendBroadcast(kotlinx.coroutines.CancellableContinuation<? super java.lang.Boolean> cont) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cont, "cont");
            this.cont = cont;
            this.$$delegate_0 = (kotlinx.coroutines.CancellableContinuationImpl) cont;
        }

        public final kotlinx.coroutines.CancellableContinuation<java.lang.Boolean> getCont() {
            return this.cont;
        }
    }

    public static /* synthetic */ java.lang.Object sendImpl$default(kotlinx.coroutines.channels.BufferedChannel $this, java.lang.Object element, java.lang.Object waiter, kotlin.jvm.functions.Function0 onRendezvousOrBuffered, kotlin.jvm.functions.Function2 onSuspend, kotlin.jvm.functions.Function0 onClosed, kotlin.jvm.functions.Function4 onNoWaiterSuspend, int i, java.lang.Object obj) {
        kotlin.jvm.functions.Function4 onNoWaiterSuspend2;
        kotlinx.coroutines.channels.ChannelSegment segment;
        int i2;
        kotlinx.coroutines.channels.ChannelSegment segment2;
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: sendImpl");
        }
        if ((i & 32) == 0) {
            onNoWaiterSuspend2 = onNoWaiterSuspend;
        } else {
            onNoWaiterSuspend2 = new kotlin.jvm.functions.Function4() { // from class: kotlinx.coroutines.channels.BufferedChannel.sendImpl.1
                @Override // kotlin.jvm.functions.Function4
                public /* bridge */ /* synthetic */ java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3, java.lang.Object p4) {
                    return invoke((kotlinx.coroutines.channels.ChannelSegment<java.lang.Object>) p1, ((java.lang.Number) p2).intValue(), p3, ((java.lang.Number) p4).longValue());
                }

                public final java.lang.Void invoke(kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, int i3, E e, long j) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(channelSegment, "<anonymous parameter 0>");
                    throw new java.lang.IllegalStateException("unexpected".toString());
                }
            };
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onRendezvousOrBuffered, "onRendezvousOrBuffered");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onSuspend, "onSuspend");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onClosed, "onClosed");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onNoWaiterSuspend2, "onNoWaiterSuspend");
        kotlinx.coroutines.channels.ChannelSegment segment3 = (kotlinx.coroutines.channels.ChannelSegment) $this.sendSegment.getValue();
        while (true) {
            long sendersAndCloseStatusCur = $this.sendersAndCloseStatus.getAndIncrement();
            long $this$sendersCounter$iv = sendersAndCloseStatusCur & 1152921504606846975L;
            boolean closed = $this.isClosedForSend0(sendersAndCloseStatusCur);
            long id = $this$sendersCounter$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i3 = (int) ($this$sendersCounter$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment3.id != id) {
                kotlinx.coroutines.channels.ChannelSegment segment4 = $this.findSegmentSend(id, segment3);
                if (segment4 != null) {
                    segment = segment4;
                } else if (closed) {
                    return onClosed.invoke();
                }
            } else {
                segment = segment3;
            }
            kotlinx.coroutines.channels.ChannelSegment segment5 = segment;
            switch ($this.updateCellSend(segment, i3, element, $this$sendersCounter$iv, waiter, closed)) {
                case 0:
                    segment5.cleanPrev();
                    return onRendezvousOrBuffered.invoke();
                case 1:
                    return onRendezvousOrBuffered.invoke();
                case 2:
                    if (closed) {
                        segment5.onSlotCleaned();
                        return onClosed.invoke();
                    }
                    kotlinx.coroutines.Waiter waiter2 = waiter instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) waiter : null;
                    if (waiter2 != null) {
                        i2 = i3;
                        $this.prepareSenderForSuspension(waiter2, segment5, i2);
                    } else {
                        i2 = i3;
                    }
                    return onSuspend.invoke(segment5, java.lang.Integer.valueOf(i2));
                case 3:
                    return onNoWaiterSuspend2.invoke(segment5, java.lang.Integer.valueOf(i3), element, java.lang.Long.valueOf($this$sendersCounter$iv));
                case 4:
                    if ($this$sendersCounter$iv < $this.getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        segment5.cleanPrev();
                    }
                    return onClosed.invoke();
                case 5:
                    segment5.cleanPrev();
                    segment2 = segment5;
                    break;
                default:
                    segment2 = segment5;
                    break;
            }
            segment3 = segment2;
        }
    }

    protected final <R> R sendImpl(E element, java.lang.Object waiter, kotlin.jvm.functions.Function0<? extends R> onRendezvousOrBuffered, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.channels.ChannelSegment<E>, ? super java.lang.Integer, ? extends R> onSuspend, kotlin.jvm.functions.Function0<? extends R> onClosed, kotlin.jvm.functions.Function4<? super kotlinx.coroutines.channels.ChannelSegment<E>, ? super java.lang.Integer, ? super E, ? super java.lang.Long, ? extends R> onNoWaiterSuspend) {
        kotlinx.coroutines.channels.ChannelSegment segment;
        int i;
        kotlinx.coroutines.channels.ChannelSegment segment2;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onRendezvousOrBuffered, "onRendezvousOrBuffered");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onSuspend, "onSuspend");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onClosed, "onClosed");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onNoWaiterSuspend, "onNoWaiterSuspend");
        kotlinx.coroutines.channels.ChannelSegment segment3 = (kotlinx.coroutines.channels.ChannelSegment) this.sendSegment.getValue();
        while (true) {
            long sendersAndCloseStatusCur = this.sendersAndCloseStatus.getAndIncrement();
            long $this$sendersCounter$iv = sendersAndCloseStatusCur & 1152921504606846975L;
            boolean closed = isClosedForSend0(sendersAndCloseStatusCur);
            long id = $this$sendersCounter$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i2 = (int) ($this$sendersCounter$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment3.id != id) {
                kotlinx.coroutines.channels.ChannelSegment segment4 = findSegmentSend(id, segment3);
                if (segment4 != null) {
                    segment = segment4;
                } else if (closed) {
                    return onClosed.invoke();
                }
            } else {
                segment = segment3;
            }
            kotlinx.coroutines.channels.ChannelSegment segment5 = segment;
            switch (updateCellSend(segment, i2, element, $this$sendersCounter$iv, waiter, closed)) {
                case 0:
                    segment5.cleanPrev();
                    return onRendezvousOrBuffered.invoke();
                case 1:
                    return onRendezvousOrBuffered.invoke();
                case 2:
                    if (closed) {
                        segment5.onSlotCleaned();
                        return onClosed.invoke();
                    }
                    kotlinx.coroutines.Waiter waiter2 = waiter instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) waiter : null;
                    if (waiter2 != null) {
                        i = i2;
                        prepareSenderForSuspension(waiter2, segment5, i);
                    } else {
                        i = i2;
                    }
                    return onSuspend.invoke(segment5, java.lang.Integer.valueOf(i));
                case 3:
                    return onNoWaiterSuspend.invoke(segment5, java.lang.Integer.valueOf(i2), element, java.lang.Long.valueOf($this$sendersCounter$iv));
                case 4:
                    if ($this$sendersCounter$iv < getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        segment5.cleanPrev();
                    }
                    return onClosed.invoke();
                case 5:
                    segment5.cleanPrev();
                    segment2 = segment5;
                    break;
                default:
                    segment2 = segment5;
                    break;
            }
            segment3 = segment2;
        }
    }

    private final void sendImplOnNoWaiter(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, E element, long s, kotlinx.coroutines.Waiter waiter, kotlin.jvm.functions.Function0<kotlin.Unit> onRendezvousOrBuffered, kotlin.jvm.functions.Function0<kotlin.Unit> onClosed) {
        kotlinx.coroutines.channels.ChannelSegment segment$iv;
        kotlin.Unit unitInvoke;
        int $i$f$sendImplOnNoWaiter = 0;
        switch (updateCellSend(segment, index, element, s, waiter, false)) {
            case 0:
                segment.cleanPrev();
                onRendezvousOrBuffered.invoke();
                return;
            case 1:
                onRendezvousOrBuffered.invoke();
                return;
            case 2:
                prepareSenderForSuspension(waiter, segment, index);
                return;
            case 3:
            default:
                throw new java.lang.IllegalStateException("unexpected".toString());
            case 4:
                if (s < getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    segment.cleanPrev();
                }
                onClosed.invoke();
                return;
            case 5:
                segment.cleanPrev();
                kotlinx.coroutines.channels.ChannelSegment segment$iv2 = (kotlinx.coroutines.channels.ChannelSegment) this.sendSegment.getValue();
                while (true) {
                    long sendersAndCloseStatusCur$iv = this.sendersAndCloseStatus.getAndIncrement();
                    long $this$sendersCounter$iv$iv = sendersAndCloseStatusCur$iv & 1152921504606846975L;
                    boolean closed$iv = isClosedForSend0(sendersAndCloseStatusCur$iv);
                    long id$iv = $this$sendersCounter$iv$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
                    int i$iv = (int) ($this$sendersCounter$iv$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
                    if (segment$iv2.id != id$iv) {
                        kotlinx.coroutines.channels.ChannelSegment segment$iv3 = findSegmentSend(id$iv, segment$iv2);
                        if (segment$iv3 != null) {
                            segment$iv = segment$iv3;
                        } else if (closed$iv) {
                            unitInvoke = onClosed.invoke();
                        }
                    } else {
                        segment$iv = segment$iv2;
                    }
                    kotlinx.coroutines.channels.ChannelSegment segment$iv4 = segment$iv;
                    int $i$f$sendImplOnNoWaiter2 = $i$f$sendImplOnNoWaiter;
                    switch (updateCellSend(segment$iv, i$iv, element, $this$sendersCounter$iv$iv, waiter, closed$iv)) {
                        case 0:
                            segment$iv4.cleanPrev();
                            unitInvoke = onRendezvousOrBuffered.invoke();
                            break;
                        case 1:
                            unitInvoke = onRendezvousOrBuffered.invoke();
                            break;
                        case 2:
                            if (!closed$iv) {
                                kotlinx.coroutines.Waiter waiter2 = waiter instanceof kotlinx.coroutines.Waiter ? waiter : null;
                                if (waiter2 != null) {
                                    prepareSenderForSuspension(waiter2, segment$iv4, i$iv);
                                }
                                unitInvoke = kotlin.Unit.INSTANCE;
                            } else {
                                segment$iv4.onSlotCleaned();
                                unitInvoke = onClosed.invoke();
                            }
                            break;
                        case 3:
                            throw new java.lang.IllegalStateException("unexpected".toString());
                        case 4:
                            if ($this$sendersCounter$iv$iv < getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                                segment$iv4.cleanPrev();
                            }
                            unitInvoke = onClosed.invoke();
                            break;
                        case 5:
                            segment$iv4.cleanPrev();
                        default:
                            segment$iv2 = segment$iv4;
                            $i$f$sendImplOnNoWaiter = $i$f$sendImplOnNoWaiter2;
                            break;
                    }
                }
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int updateCellSend(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, E element, long s, java.lang.Object waiter, boolean closed) {
        segment.storeElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, element);
        if (closed) {
            return updateCellSendSlow(segment, index, element, s, waiter, closed);
        }
        java.lang.Object state = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
        if (state == null) {
            if (bufferOrRendezvousSend(s)) {
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, null, kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED)) {
                    return 1;
                }
            } else {
                if (waiter == null) {
                    return 3;
                }
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, null, waiter)) {
                    return 2;
                }
            }
        } else if (state instanceof kotlinx.coroutines.Waiter) {
            segment.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
            if (tryResumeReceiver(state, element)) {
                segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV);
                onReceiveDequeued();
                return 0;
            }
            if (segment.getAndSetState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV) != kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV) {
                segment.onCancelledRequest(index, true);
            }
            return 5;
        }
        return updateCellSendSlow(segment, index, element, s, waiter, closed);
    }

    private final int updateCellSendSlow(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, E element, long s, java.lang.Object waiter, boolean closed) {
        while (true) {
            java.lang.Object state = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
            if (state != null) {
                if (state != kotlinx.coroutines.channels.BufferedChannelKt.IN_BUFFER) {
                    if (state != kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV) {
                        if (state == kotlinx.coroutines.channels.BufferedChannelKt.POISONED) {
                            segment.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
                            return 5;
                        }
                        if (state == kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
                            segment.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
                            completeCloseOrCancel();
                            return 4;
                        }
                        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
                            if ((((state instanceof kotlinx.coroutines.Waiter) || (state instanceof kotlinx.coroutines.channels.WaiterEB)) ? 1 : 0) == 0) {
                                throw new java.lang.AssertionError();
                            }
                        }
                        segment.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
                        java.lang.Object receiver = state instanceof kotlinx.coroutines.channels.WaiterEB ? ((kotlinx.coroutines.channels.WaiterEB) state).waiter : state;
                        if (tryResumeReceiver(receiver, element)) {
                            segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV);
                            onReceiveDequeued();
                            return 0;
                        }
                        if (segment.getAndSetState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV) != kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV) {
                            segment.onCancelledRequest(index, true);
                        }
                        return 5;
                    }
                    segment.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
                    return 5;
                }
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED)) {
                    return 1;
                }
            } else if (bufferOrRendezvousSend(s) && !closed) {
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, null, kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED)) {
                    return 1;
                }
            } else if (closed) {
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, null, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND)) {
                    segment.onCancelledRequest(index, false);
                    return 4;
                }
            } else {
                if (waiter == null) {
                    return 3;
                }
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, null, waiter)) {
                    return 2;
                }
            }
        }
    }

    private final boolean shouldSendSuspend(long curSendersAndCloseStatus) {
        if (isClosedForSend0(curSendersAndCloseStatus)) {
            return false;
        }
        long $this$sendersCounter$iv = curSendersAndCloseStatus & 1152921504606846975L;
        return !bufferOrRendezvousSend($this$sendersCounter$iv);
    }

    private final boolean bufferOrRendezvousSend(long curSenders) {
        return curSenders < getBufferEndCounter() || curSenders < getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() + ((long) this.capacity);
    }

    public boolean shouldSendSuspend$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return shouldSendSuspend(this.sendersAndCloseStatus.getValue());
    }

    private final boolean tryResumeReceiver(java.lang.Object $this$tryResumeReceiver, E e) {
        if ($this$tryResumeReceiver instanceof kotlinx.coroutines.selects.SelectInstance) {
            return ((kotlinx.coroutines.selects.SelectInstance) $this$tryResumeReceiver).trySelect(this, e);
        }
        if ($this$tryResumeReceiver instanceof kotlinx.coroutines.channels.ReceiveCatching) {
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$tryResumeReceiver, "null cannot be cast to non-null type kotlinx.coroutines.channels.ReceiveCatching<E of kotlinx.coroutines.channels.BufferedChannel>");
            kotlinx.coroutines.CancellableContinuationImpl<kotlinx.coroutines.channels.ChannelResult<? extends E>> cancellableContinuationImpl = ((kotlinx.coroutines.channels.ReceiveCatching) $this$tryResumeReceiver).cont;
            kotlinx.coroutines.channels.ChannelResult channelResultM12819boximpl = kotlinx.coroutines.channels.ChannelResult.m12819boximpl(kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(e));
            kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = this.onUndeliveredElement;
            return kotlinx.coroutines.channels.BufferedChannelKt.tryResume0(cancellableContinuationImpl, channelResultM12819boximpl, function1 != null ? kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun(function1, e, ((kotlinx.coroutines.channels.ReceiveCatching) $this$tryResumeReceiver).cont.getContext()) : null);
        }
        if ($this$tryResumeReceiver instanceof kotlinx.coroutines.channels.BufferedChannel.BufferedChannelIterator) {
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$tryResumeReceiver, "null cannot be cast to non-null type kotlinx.coroutines.channels.BufferedChannel.BufferedChannelIterator<E of kotlinx.coroutines.channels.BufferedChannel>");
            return ((kotlinx.coroutines.channels.BufferedChannel.BufferedChannelIterator) $this$tryResumeReceiver).tryResumeHasNext(e);
        }
        if ($this$tryResumeReceiver instanceof kotlinx.coroutines.CancellableContinuation) {
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$tryResumeReceiver, "null cannot be cast to non-null type kotlinx.coroutines.CancellableContinuation<E of kotlinx.coroutines.channels.BufferedChannel>");
            kotlinx.coroutines.CancellableContinuation cancellableContinuation = (kotlinx.coroutines.CancellableContinuation) $this$tryResumeReceiver;
            kotlin.jvm.functions.Function1<E, kotlin.Unit> function12 = this.onUndeliveredElement;
            return kotlinx.coroutines.channels.BufferedChannelKt.tryResume0(cancellableContinuation, e, function12 != null ? kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun(function12, e, ((kotlinx.coroutines.CancellableContinuation) $this$tryResumeReceiver).getContext()) : null);
        }
        throw new java.lang.IllegalStateException(("Unexpected receiver type: " + $this$tryResumeReceiver).toString());
    }

    protected void onReceiveEnqueued() {
    }

    protected void onReceiveDequeued() {
    }

    static /* synthetic */ <E> java.lang.Object receive$suspendImpl(kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel, kotlin.coroutines.Continuation<? super E> continuation) throws java.lang.Throwable {
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment;
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment2 = (kotlinx.coroutines.channels.ChannelSegment) ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).receiveSegment.getValue();
        while (!bufferedChannel.isClosedForReceive()) {
            long r$iv = ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).receivers.getAndIncrement();
            long id$iv = r$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i$iv = (int) (r$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (channelSegment2.id != id$iv) {
                kotlinx.coroutines.channels.ChannelSegment<E> channelSegmentFindSegmentReceive = bufferedChannel.findSegmentReceive(id$iv, channelSegment2);
                if (channelSegmentFindSegmentReceive == null) {
                    continue;
                } else {
                    channelSegment = channelSegmentFindSegmentReceive;
                }
            } else {
                channelSegment = channelSegment2;
            }
            java.lang.Object updCellResult$iv = bufferedChannel.updateCellReceive(channelSegment, i$iv, r$iv, null);
            if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                    if (updCellResult$iv == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                        return bufferedChannel.receiveOnNoWaiterSuspend(channelSegment, i$iv, r$iv, continuation);
                    }
                    channelSegment.cleanPrev();
                    return updCellResult$iv;
                }
                if (r$iv < bufferedChannel.getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    channelSegment.cleanPrev();
                }
                channelSegment2 = channelSegment;
            } else {
                throw new java.lang.IllegalStateException("unexpected".toString());
            }
        }
        throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverStackTrace(bufferedChannel.getReceiveException());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v3, types: [kotlin.jvm.functions.Function1] */
    /* JADX WARN: Type inference failed for: r5v0, types: [kotlinx.coroutines.CancellableContinuationImpl] */
    /* JADX WARN: Type inference failed for: r7v19, types: [kotlinx.coroutines.Waiter] */
    /* JADX WARN: Type inference failed for: r9v7, types: [kotlin.jvm.functions.Function1] */
    public final java.lang.Object receiveOnNoWaiterSuspend(kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, int index, long r, kotlin.coroutines.Continuation<? super E> continuation) throws java.lang.Throwable {
        kotlinx.coroutines.channels.ChannelSegment segment$iv$iv;
        int i$iv$iv = 0;
        kotlin.coroutines.Continuation<? super E> continuation2 = continuation;
        int i = 0;
        ?? orCreateCancellableContinuation = kotlinx.coroutines.CancellableContinuationKt.getOrCreateCancellableContinuation(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation2));
        try {
            java.lang.Object updCellResult$iv = updateCellReceive(channelSegment, index, r, (kotlinx.coroutines.Waiter) orCreateCancellableContinuation);
            try {
                if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                    try {
                        if (updCellResult$iv == kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                            if (r < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                                channelSegment.cleanPrev();
                            }
                            kotlinx.coroutines.channels.ChannelSegment segment$iv$iv2 = (kotlinx.coroutines.channels.ChannelSegment) this.receiveSegment.getValue();
                            while (!isClosedForReceive()) {
                                long r$iv$iv = this.receivers.getAndIncrement();
                                long id$iv$iv = r$iv$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
                                int $i$f$suspendCancellableCoroutineReusable = i$iv$iv;
                                kotlin.coroutines.Continuation<? super E> continuation3 = continuation2;
                                try {
                                    int i$iv$iv2 = (int) (r$iv$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
                                    int i2 = i;
                                    if (segment$iv$iv2.id != id$iv$iv) {
                                        segment$iv$iv = findSegmentReceive(id$iv$iv, segment$iv$iv2);
                                        if (segment$iv$iv == null) {
                                            i$iv$iv = $i$f$suspendCancellableCoroutineReusable;
                                            continuation2 = continuation3;
                                            i = i2;
                                        }
                                    } else {
                                        segment$iv$iv = segment$iv$iv2;
                                    }
                                    java.lang.Object updCellResult$iv$iv = updateCellReceive(segment$iv$iv, i$iv$iv2, r$iv$iv, (kotlinx.coroutines.Waiter) orCreateCancellableContinuation);
                                    if (updCellResult$iv$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                                        if (updCellResult$iv$iv == kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                                            if (r$iv$iv < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                                                segment$iv$iv.cleanPrev();
                                            }
                                            segment$iv$iv2 = segment$iv$iv;
                                            i$iv$iv = $i$f$suspendCancellableCoroutineReusable;
                                            continuation2 = continuation3;
                                            i = i2;
                                        } else {
                                            if (updCellResult$iv$iv == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                                                throw new java.lang.IllegalStateException("unexpected".toString());
                                            }
                                            segment$iv$iv.cleanPrev();
                                            kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = this.onUndeliveredElement;
                                            orCreateCancellableContinuation.resume(updCellResult$iv$iv, function1 != null ? kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun(function1, updCellResult$iv$iv, orCreateCancellableContinuation.getContext()) : null);
                                        }
                                    } else {
                                        ?? r7 = ((kotlinx.coroutines.Waiter) orCreateCancellableContinuation) instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) orCreateCancellableContinuation : null;
                                        if (r7 != 0) {
                                            prepareReceiverForSuspension(r7, segment$iv$iv, i$iv$iv2);
                                        }
                                    }
                                } catch (java.lang.Throwable th) {
                                    e$iv = th;
                                    orCreateCancellableContinuation.releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                                    throw e$iv;
                                }
                            }
                            onClosedReceiveOnNoWaiterSuspend((kotlinx.coroutines.CancellableContinuation) orCreateCancellableContinuation);
                        } else {
                            channelSegment.cleanPrev();
                            kotlin.jvm.functions.Function1<E, kotlin.Unit> function12 = this.onUndeliveredElement;
                            orCreateCancellableContinuation.resume(updCellResult$iv, function12 != null ? kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun(function12, updCellResult$iv, orCreateCancellableContinuation.getContext()) : null);
                        }
                    } catch (java.lang.Throwable th2) {
                        e$iv = th2;
                    }
                } else {
                    try {
                        prepareReceiverForSuspension((kotlinx.coroutines.Waiter) orCreateCancellableContinuation, channelSegment, index);
                    } catch (java.lang.Throwable th3) {
                        e$iv = th3;
                        orCreateCancellableContinuation.releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                        throw e$iv;
                    }
                }
                java.lang.Object result = orCreateCancellableContinuation.getResult();
                if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                    kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
                }
                return result;
            } catch (java.lang.Throwable th4) {
                e$iv = th4;
            }
        } catch (java.lang.Throwable th5) {
            e$iv = th5;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void prepareReceiverForSuspension(kotlinx.coroutines.Waiter $this$prepareReceiverForSuspension, kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, int index) {
        onReceiveEnqueued();
        $this$prepareReceiverForSuspension.invokeOnCancellation(channelSegment, index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onClosedReceiveOnNoWaiterSuspend(kotlinx.coroutines.CancellableContinuation<? super E> cont) {
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(getReceiveException())));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0019  */
    /* JADX INFO: renamed from: receiveCatching-JP2dKIU$suspendImpl, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static /* synthetic */ <E> java.lang.Object m12811receiveCatchingJP2dKIU$suspendImpl(kotlinx.coroutines.channels.BufferedChannel<E> r17, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<? extends E>> r18) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 240
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BufferedChannel.m12811receiveCatchingJP2dKIU$suspendImpl(kotlinx.coroutines.channels.BufferedChannel, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0019  */
    /* JADX INFO: renamed from: receiveCatchingOnNoWaiterSuspend-GKJJFZk, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object m12812receiveCatchingOnNoWaiterSuspendGKJJFZk(kotlinx.coroutines.channels.ChannelSegment<E> r24, int r25, long r26, kotlin.coroutines.Continuation<? super kotlinx.coroutines.channels.ChannelResult<? extends E>> r28) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 554
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BufferedChannel.m12812receiveCatchingOnNoWaiterSuspendGKJJFZk(kotlinx.coroutines.channels.ChannelSegment, int, long, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onClosedReceiveCatchingOnNoWaiterSuspend(kotlinx.coroutines.CancellableContinuation<? super kotlinx.coroutines.channels.ChannelResult<? extends E>> cont) {
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        cont.resumeWith(kotlin.Result.m11307constructorimpl(kotlinx.coroutines.channels.ChannelResult.m12819boximpl(kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getCloseCause()))));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.channels.ReceiveChannel
    /* JADX INFO: renamed from: tryReceive-PtdJZtk, reason: not valid java name */
    public java.lang.Object mo12814tryReceivePtdJZtk() {
        kotlinx.coroutines.channels.ChannelSegment segment$iv;
        long r = this.receivers.getValue();
        long sendersAndCloseStatusCur = this.sendersAndCloseStatus.getValue();
        if (isClosedForReceive0(sendersAndCloseStatusCur)) {
            return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getCloseCause());
        }
        long $this$sendersCounter$iv = sendersAndCloseStatusCur & 1152921504606846975L;
        if (r >= $this$sendersCounter$iv) {
            return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12833failurePtdJZtk();
        }
        java.lang.Object waiter$iv = kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV;
        kotlinx.coroutines.channels.ChannelSegment segment$iv2 = (kotlinx.coroutines.channels.ChannelSegment) this.receiveSegment.getValue();
        while (!isClosedForReceive()) {
            long r$iv = this.receivers.getAndIncrement();
            long id$iv = r$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i$iv = (int) (r$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment$iv2.id != id$iv) {
                kotlinx.coroutines.channels.ChannelSegment segment$iv3 = findSegmentReceive(id$iv, segment$iv2);
                if (segment$iv3 == null) {
                    continue;
                } else {
                    segment$iv = segment$iv3;
                }
            } else {
                segment$iv = segment$iv2;
            }
            long r2 = r;
            kotlinx.coroutines.channels.ChannelSegment segment$iv4 = segment$iv;
            java.lang.Object updCellResult$iv = updateCellReceive(segment$iv, i$iv, r$iv, waiter$iv);
            if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                    if (updCellResult$iv == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                        throw new java.lang.IllegalStateException("unexpected".toString());
                    }
                    segment$iv4.cleanPrev();
                    return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(updCellResult$iv);
                }
                if (r$iv < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    segment$iv4.cleanPrev();
                }
                segment$iv2 = segment$iv4;
                r = r2;
            } else {
                kotlinx.coroutines.Waiter waiter = waiter$iv instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) waiter$iv : null;
                if (waiter != null) {
                    prepareReceiverForSuspension(waiter, segment$iv4, i$iv);
                }
                waitExpandBufferCompletion$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r$iv);
                segment$iv4.onSlotCleaned();
                return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12833failurePtdJZtk();
            }
        }
        return kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getCloseCause());
    }

    protected final void dropFirstElementUntilTheSpecifiedCellIsInTheBuffer(long globalCellIndex) {
        kotlinx.coroutines.internal.UndeliveredElementException it;
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED() && !isConflatedDropOldest()) {
            throw new java.lang.AssertionError();
        }
        kotlinx.coroutines.channels.ChannelSegment<E> value = this.receiveSegment.getValue();
        while (true) {
            long r = this.receivers.getValue();
            if (globalCellIndex < java.lang.Math.max(((long) this.capacity) + r, getBufferEndCounter())) {
                return;
            }
            if (this.receivers.compareAndSet(r, 1 + r)) {
                long id = r / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
                int i = (int) (r % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
                if (value.id != id) {
                    kotlinx.coroutines.channels.ChannelSegment<E> channelSegmentFindSegmentReceive = findSegmentReceive(id, value);
                    if (channelSegmentFindSegmentReceive == null) {
                        continue;
                    } else {
                        value = channelSegmentFindSegmentReceive;
                    }
                }
                java.lang.Object updCellResult = updateCellReceive(value, i, r, null);
                if (updCellResult != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                    value.cleanPrev();
                    kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = this.onUndeliveredElement;
                    if (function1 != null && (it = kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElementCatchingException$default(function1, updCellResult, null, 2, null)) != null) {
                        throw it;
                    }
                } else if (r < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    value.cleanPrev();
                }
            }
        }
    }

    static /* synthetic */ java.lang.Object receiveImpl$default(kotlinx.coroutines.channels.BufferedChannel $this, java.lang.Object waiter, kotlin.jvm.functions.Function1 onElementRetrieved, kotlin.jvm.functions.Function3 onSuspend, kotlin.jvm.functions.Function0 onClosed, kotlin.jvm.functions.Function3 onNoWaiterSuspend, int i, java.lang.Object obj) {
        kotlin.jvm.functions.Function3 onNoWaiterSuspend2;
        kotlinx.coroutines.channels.ChannelSegment segment;
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: receiveImpl");
        }
        if ((i & 16) == 0) {
            onNoWaiterSuspend2 = onNoWaiterSuspend;
        } else {
            onNoWaiterSuspend2 = new kotlin.jvm.functions.Function3() { // from class: kotlinx.coroutines.channels.BufferedChannel.receiveImpl.1
                @Override // kotlin.jvm.functions.Function3
                public /* bridge */ /* synthetic */ java.lang.Object invoke(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3) {
                    return invoke((kotlinx.coroutines.channels.ChannelSegment) p1, ((java.lang.Number) p2).intValue(), ((java.lang.Number) p3).longValue());
                }

                public final java.lang.Void invoke(kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, int i2, long j) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(channelSegment, "<anonymous parameter 0>");
                    throw new java.lang.IllegalStateException("unexpected".toString());
                }
            };
        }
        kotlinx.coroutines.channels.ChannelSegment segment2 = (kotlinx.coroutines.channels.ChannelSegment) $this.receiveSegment.getValue();
        while (!$this.isClosedForReceive()) {
            long r = $this.receivers.getAndIncrement();
            long id = r / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i2 = (int) (r % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment2.id != id) {
                kotlinx.coroutines.channels.ChannelSegment segment3 = $this.findSegmentReceive(id, segment2);
                if (segment3 == null) {
                    continue;
                } else {
                    segment = segment3;
                }
            } else {
                segment = segment2;
            }
            java.lang.Object updCellResult = $this.updateCellReceive(segment, i2, r, waiter);
            if (updCellResult != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                if (updCellResult != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                    if (updCellResult == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                        return onNoWaiterSuspend2.invoke(segment, java.lang.Integer.valueOf(i2), java.lang.Long.valueOf(r));
                    }
                    segment.cleanPrev();
                    return onElementRetrieved.invoke(updCellResult);
                }
                if (r < $this.getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    segment.cleanPrev();
                }
                segment2 = segment;
            } else {
                kotlinx.coroutines.Waiter waiter2 = waiter instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) waiter : null;
                if (waiter2 != null) {
                    $this.prepareReceiverForSuspension(waiter2, segment, i2);
                }
                return onSuspend.invoke(segment, java.lang.Integer.valueOf(i2), java.lang.Long.valueOf(r));
            }
        }
        return onClosed.invoke();
    }

    private final <R> R receiveImpl(java.lang.Object waiter, kotlin.jvm.functions.Function1<? super E, ? extends R> onElementRetrieved, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.channels.ChannelSegment<E>, ? super java.lang.Integer, ? super java.lang.Long, ? extends R> onSuspend, kotlin.jvm.functions.Function0<? extends R> onClosed, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.channels.ChannelSegment<E>, ? super java.lang.Integer, ? super java.lang.Long, ? extends R> onNoWaiterSuspend) {
        kotlinx.coroutines.channels.ChannelSegment segment;
        kotlinx.coroutines.channels.ChannelSegment segment2 = (kotlinx.coroutines.channels.ChannelSegment) this.receiveSegment.getValue();
        while (!isClosedForReceive()) {
            long r = this.receivers.getAndIncrement();
            long id = r / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i = (int) (r % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment2.id != id) {
                kotlinx.coroutines.channels.ChannelSegment segment3 = findSegmentReceive(id, segment2);
                if (segment3 == null) {
                    continue;
                } else {
                    segment = segment3;
                }
            } else {
                segment = segment2;
            }
            java.lang.Object updCellResult = updateCellReceive(segment, i, r, waiter);
            if (updCellResult != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                if (updCellResult != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                    if (updCellResult == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                        return onNoWaiterSuspend.invoke(segment, java.lang.Integer.valueOf(i), java.lang.Long.valueOf(r));
                    }
                    segment.cleanPrev();
                    return onElementRetrieved.invoke(updCellResult);
                }
                if (r < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    segment.cleanPrev();
                }
                segment2 = segment;
            } else {
                kotlinx.coroutines.Waiter waiter2 = waiter instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) waiter : null;
                if (waiter2 != null) {
                    prepareReceiverForSuspension(waiter2, segment, i);
                }
                return onSuspend.invoke(segment, java.lang.Integer.valueOf(i), java.lang.Long.valueOf(r));
            }
        }
        return onClosed.invoke();
    }

    private final void receiveImplOnNoWaiter(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, long r, kotlinx.coroutines.Waiter waiter, kotlin.jvm.functions.Function1<? super E, kotlin.Unit> onElementRetrieved, kotlin.jvm.functions.Function0<kotlin.Unit> onClosed) {
        kotlinx.coroutines.channels.ChannelSegment segment$iv;
        int $i$f$receiveImplOnNoWaiter = 0;
        java.lang.Object updCellResult = updateCellReceive(segment, index, r, waiter);
        if (updCellResult != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
            if (updCellResult == kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                if (r < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    segment.cleanPrev();
                }
                kotlinx.coroutines.channels.ChannelSegment segment$iv2 = (kotlinx.coroutines.channels.ChannelSegment) this.receiveSegment.getValue();
                while (!isClosedForReceive()) {
                    long r$iv = this.receivers.getAndIncrement();
                    long id$iv = r$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
                    int i$iv = (int) (r$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
                    if (segment$iv2.id != id$iv) {
                        kotlinx.coroutines.channels.ChannelSegment segment$iv3 = findSegmentReceive(id$iv, segment$iv2);
                        if (segment$iv3 == null) {
                            continue;
                        } else {
                            segment$iv = segment$iv3;
                        }
                    } else {
                        segment$iv = segment$iv2;
                    }
                    int $i$f$receiveImplOnNoWaiter2 = $i$f$receiveImplOnNoWaiter;
                    kotlinx.coroutines.channels.ChannelSegment segment$iv4 = segment$iv;
                    java.lang.Object updCellResult$iv = updateCellReceive(segment$iv, i$iv, r$iv, waiter);
                    if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                        if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                            if (updCellResult$iv == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                                throw new java.lang.IllegalStateException("unexpected".toString());
                            }
                            segment$iv4.cleanPrev();
                            onElementRetrieved.invoke(updCellResult$iv);
                            return;
                        }
                        if (r$iv < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                            segment$iv4.cleanPrev();
                        }
                        segment$iv2 = segment$iv4;
                        $i$f$receiveImplOnNoWaiter = $i$f$receiveImplOnNoWaiter2;
                    } else {
                        kotlinx.coroutines.Waiter waiter2 = waiter instanceof kotlinx.coroutines.Waiter ? waiter : null;
                        if (waiter2 != null) {
                            prepareReceiverForSuspension(waiter2, segment$iv4, i$iv);
                        }
                        kotlin.Unit unit = kotlin.Unit.INSTANCE;
                        return;
                    }
                }
                onClosed.invoke();
                return;
            }
            segment.cleanPrev();
            onElementRetrieved.invoke(updCellResult);
            return;
        }
        prepareReceiverForSuspension(waiter, segment, index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object updateCellReceive(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, long r, java.lang.Object waiter) {
        java.lang.Object state = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
        if (state == null) {
            long $this$sendersCounter$iv = this.sendersAndCloseStatus.getValue();
            long senders = $this$sendersCounter$iv & 1152921504606846975L;
            if (r >= senders) {
                if (waiter == null) {
                    return kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER;
                }
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, waiter)) {
                    expandBuffer();
                    return kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND;
                }
            }
        } else if (state == kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED && segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV)) {
            expandBuffer();
            return segment.retrieveElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
        }
        return updateCellReceiveSlow(segment, index, r, waiter);
    }

    private final java.lang.Object updateCellReceiveSlow(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, long r, java.lang.Object waiter) {
        while (true) {
            java.lang.Object state = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
            if (state == null || state == kotlinx.coroutines.channels.BufferedChannelKt.IN_BUFFER) {
                long $this$sendersCounter$iv = this.sendersAndCloseStatus.getValue();
                long senders = $this$sendersCounter$iv & 1152921504606846975L;
                if (r < senders) {
                    if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.POISONED)) {
                        expandBuffer();
                        return kotlinx.coroutines.channels.BufferedChannelKt.FAILED;
                    }
                } else {
                    if (waiter == null) {
                        return kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER;
                    }
                    if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, waiter)) {
                        expandBuffer();
                        return kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND;
                    }
                }
            } else if (state == kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED) {
                if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV)) {
                    expandBuffer();
                    return segment.retrieveElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
                }
            } else {
                if (state != kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND && state != kotlinx.coroutines.channels.BufferedChannelKt.POISONED) {
                    if (state != kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
                        if (state != kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_EB && segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_RCV)) {
                            boolean helpExpandBuffer = state instanceof kotlinx.coroutines.channels.WaiterEB;
                            java.lang.Object sender = state instanceof kotlinx.coroutines.channels.WaiterEB ? ((kotlinx.coroutines.channels.WaiterEB) state).waiter : state;
                            if (tryResumeSender(sender, segment, index)) {
                                segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV);
                                expandBuffer();
                                return segment.retrieveElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
                            }
                            segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND);
                            segment.onCancelledRequest(index, false);
                            if (helpExpandBuffer) {
                                expandBuffer();
                            }
                            return kotlinx.coroutines.channels.BufferedChannelKt.FAILED;
                        }
                    } else {
                        expandBuffer();
                        return kotlinx.coroutines.channels.BufferedChannelKt.FAILED;
                    }
                }
                return kotlinx.coroutines.channels.BufferedChannelKt.FAILED;
            }
        }
    }

    private final boolean tryResumeSender(java.lang.Object $this$tryResumeSender, kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, int index) {
        if ($this$tryResumeSender instanceof kotlinx.coroutines.CancellableContinuation) {
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$tryResumeSender, "null cannot be cast to non-null type kotlinx.coroutines.CancellableContinuation<kotlin.Unit>");
            return kotlinx.coroutines.channels.BufferedChannelKt.tryResume0$default((kotlinx.coroutines.CancellableContinuation) $this$tryResumeSender, kotlin.Unit.INSTANCE, null, 2, null);
        }
        if ($this$tryResumeSender instanceof kotlinx.coroutines.selects.SelectInstance) {
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$tryResumeSender, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectImplementation<*>");
            kotlinx.coroutines.selects.TrySelectDetailedResult trySelectResult = ((kotlinx.coroutines.selects.SelectImplementation) $this$tryResumeSender).trySelectDetailed(this, kotlin.Unit.INSTANCE);
            if (trySelectResult == kotlinx.coroutines.selects.TrySelectDetailedResult.REREGISTER) {
                channelSegment.cleanElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
            }
            return trySelectResult == kotlinx.coroutines.selects.TrySelectDetailedResult.SUCCESSFUL;
        }
        if ($this$tryResumeSender instanceof kotlinx.coroutines.channels.BufferedChannel.SendBroadcast) {
            return kotlinx.coroutines.channels.BufferedChannelKt.tryResume0$default(((kotlinx.coroutines.channels.BufferedChannel.SendBroadcast) $this$tryResumeSender).getCont(), true, null, 2, null);
        }
        throw new java.lang.IllegalStateException(("Unexpected waiter: " + $this$tryResumeSender).toString());
    }

    private final void expandBuffer() {
        long id;
        if (isRendezvousOrUnlimited()) {
            return;
        }
        kotlinx.coroutines.channels.ChannelSegment<E> value = this.bufferEndSegment.getValue();
        while (true) {
            long b = this.bufferEnd.getAndIncrement();
            long id2 = b / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            long s = getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (s <= b) {
                if (value.id < id2 && value.getNext() != 0) {
                    moveSegmentBufferEndToSpecifiedOrLast(id2, value);
                }
                incCompletedExpandBufferAttempts$default(this, 0L, 1, null);
                return;
            }
            if (value.id != id2) {
                id = 0;
                kotlinx.coroutines.channels.ChannelSegment<E> channelSegmentFindSegmentBufferEnd = findSegmentBufferEnd(id2, value, b);
                if (channelSegmentFindSegmentBufferEnd == null) {
                    continue;
                } else {
                    value = channelSegmentFindSegmentBufferEnd;
                }
            } else {
                id = 0;
            }
            int i = (int) (b % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (updateCellExpandBuffer(value, i, b)) {
                incCompletedExpandBufferAttempts$default(this, id, 1, null);
                return;
            }
            incCompletedExpandBufferAttempts$default(this, id, 1, null);
        }
    }

    private final boolean updateCellExpandBuffer(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, long b) {
        java.lang.Object state = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
        if ((state instanceof kotlinx.coroutines.Waiter) && b >= this.receivers.getValue() && segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_EB)) {
            if (!tryResumeSender(state, segment, index)) {
                segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND);
                segment.onCancelledRequest(index, false);
                return false;
            }
            segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED);
            return true;
        }
        return updateCellExpandBufferSlow(segment, index, b);
    }

    private final boolean updateCellExpandBufferSlow(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, long b) {
        while (true) {
            java.lang.Object state = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
            if (state instanceof kotlinx.coroutines.Waiter) {
                if (b >= this.receivers.getValue()) {
                    if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_EB)) {
                        if (!tryResumeSender(state, segment, index)) {
                            segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND);
                            segment.onCancelledRequest(index, false);
                            return false;
                        }
                        segment.setState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED);
                        return true;
                    }
                } else if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, new kotlinx.coroutines.channels.WaiterEB((kotlinx.coroutines.Waiter) state))) {
                    return true;
                }
            } else {
                if (state == kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND) {
                    return false;
                }
                if (state == null) {
                    if (segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.IN_BUFFER)) {
                        return true;
                    }
                } else {
                    if (state == kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED || state == kotlinx.coroutines.channels.BufferedChannelKt.POISONED || state == kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV || state == kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV || state == kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
                        return true;
                    }
                    if (state != kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_RCV) {
                        throw new java.lang.IllegalStateException(("Unexpected cell state: " + state).toString());
                    }
                }
            }
        }
    }

    static /* synthetic */ void incCompletedExpandBufferAttempts$default(kotlinx.coroutines.channels.BufferedChannel bufferedChannel, long j, int i, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: incCompletedExpandBufferAttempts");
        }
        if ((i & 1) != 0) {
            j = 1;
        }
        bufferedChannel.incCompletedExpandBufferAttempts(j);
    }

    private final void incCompletedExpandBufferAttempts(long nAttempts) {
        long $this$ebPauseExpandBuffers$iv;
        long it = this.completedExpandBuffersAndPauseFlag.addAndGet(nAttempts);
        if ((it & 4611686018427387904L) != 0) {
            do {
                $this$ebPauseExpandBuffers$iv = this.completedExpandBuffersAndPauseFlag.getValue();
            } while (($this$ebPauseExpandBuffers$iv & 4611686018427387904L) != 0);
        }
    }

    public final void waitExpandBufferCompletion$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(long globalIndex) {
        long cur$iv;
        long it;
        long upd$iv;
        if (isRendezvousOrUnlimited()) {
            return;
        }
        while (getBufferEndCounter() <= globalIndex) {
        }
        int i = kotlinx.coroutines.channels.BufferedChannelKt.EXPAND_BUFFER_COMPLETION_WAIT_ITERATIONS;
        for (int i2 = 0; i2 < i; i2++) {
            long b = getBufferEndCounter();
            long $this$ebCompletedCounter$iv = this.completedExpandBuffersAndPauseFlag.getValue();
            if (b == (4611686018427387903L & $this$ebCompletedCounter$iv) && b == getBufferEndCounter()) {
                return;
            }
        }
        kotlinx.atomicfu.AtomicLong $this$update$iv = this.completedExpandBuffersAndPauseFlag;
        do {
            cur$iv = $this$update$iv.getValue();
            long $this$ebCompletedCounter$iv2 = cur$iv & 4611686018427387903L;
            it = kotlinx.coroutines.channels.BufferedChannelKt.constructEBCompletedAndPauseFlag($this$ebCompletedCounter$iv2, true);
        } while (!$this$update$iv.compareAndSet(cur$iv, it));
        while (true) {
            upd$iv = getBufferEndCounter();
            long ebCompletedAndBit = this.completedExpandBuffersAndPauseFlag.getValue();
            long ebCompleted = ebCompletedAndBit & 4611686018427387903L;
            int $i$f$getEbPauseExpandBuffers = (ebCompletedAndBit & 4611686018427387904L) != 0 ? 1 : 0;
            if (upd$iv == ebCompleted && upd$iv == getBufferEndCounter()) {
                break;
            } else if ($i$f$getEbPauseExpandBuffers == 0) {
                this.completedExpandBuffersAndPauseFlag.compareAndSet(ebCompletedAndBit, kotlinx.coroutines.channels.BufferedChannelKt.constructEBCompletedAndPauseFlag(ebCompleted, true));
            }
        }
        kotlinx.atomicfu.AtomicLong $this$update$iv2 = this.completedExpandBuffersAndPauseFlag;
        while (true) {
            long cur$iv2 = $this$update$iv2.getValue();
            long b2 = upd$iv;
            long b3 = cur$iv2 & 4611686018427387903L;
            if ($this$update$iv2.compareAndSet(cur$iv2, kotlinx.coroutines.channels.BufferedChannelKt.constructEBCompletedAndPauseFlag(b3, false))) {
                return;
            } else {
                upd$iv = b2;
            }
        }
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public kotlinx.coroutines.selects.SelectClause2<E, kotlinx.coroutines.channels.BufferedChannel<E>> getOnSend() {
        kotlinx.coroutines.channels.BufferedChannel$onSend$1 bufferedChannel$onSend$1 = kotlinx.coroutines.channels.BufferedChannel$onSend$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onSend$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        kotlin.jvm.functions.Function3 function3 = (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onSend$1, 3);
        kotlinx.coroutines.channels.BufferedChannel$onSend$2 bufferedChannel$onSend$2 = kotlinx.coroutines.channels.BufferedChannel$onSend$2.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onSend$2, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'param')] kotlin.Any?, @[ParameterName(name = 'clauseResult')] kotlin.Any?, kotlin.Any?>{ kotlinx.coroutines.selects.SelectKt.ProcessResultFunction }");
        return new kotlinx.coroutines.selects.SelectClause2Impl(this, function3, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onSend$2, 3), null, 8, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void registerSelectForSend(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object element) {
        kotlinx.coroutines.channels.ChannelSegment segment$iv;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(select, "select");
        int $i$f$sendImpl = 0;
        kotlinx.coroutines.channels.ChannelSegment segment$iv2 = (kotlinx.coroutines.channels.ChannelSegment) this.sendSegment.getValue();
        while (true) {
            long sendersAndCloseStatusCur$iv = this.sendersAndCloseStatus.getAndIncrement();
            long $this$sendersCounter$iv$iv = sendersAndCloseStatusCur$iv & 1152921504606846975L;
            boolean closed$iv = isClosedForSend0(sendersAndCloseStatusCur$iv);
            long id$iv = $this$sendersCounter$iv$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i$iv = (int) ($this$sendersCounter$iv$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment$iv2.id != id$iv) {
                kotlinx.coroutines.channels.ChannelSegment segment$iv3 = findSegmentSend(id$iv, segment$iv2);
                if (segment$iv3 != null) {
                    segment$iv = segment$iv3;
                } else if (closed$iv) {
                    onClosedSelectOnSend(element, select);
                    return;
                }
            } else {
                segment$iv = segment$iv2;
            }
            kotlinx.coroutines.channels.ChannelSegment segment$iv4 = segment$iv;
            int $i$f$sendImpl2 = $i$f$sendImpl;
            switch (updateCellSend(segment$iv, i$iv, element, $this$sendersCounter$iv$iv, select, closed$iv)) {
                case 0:
                    segment$iv4.cleanPrev();
                    select.selectInRegistrationPhase(kotlin.Unit.INSTANCE);
                    return;
                case 1:
                    select.selectInRegistrationPhase(kotlin.Unit.INSTANCE);
                    return;
                case 2:
                    if (closed$iv) {
                        segment$iv4.onSlotCleaned();
                        onClosedSelectOnSend(element, select);
                        return;
                    } else {
                        kotlinx.coroutines.Waiter waiter = select instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) select : null;
                        if (waiter != null) {
                            prepareSenderForSuspension(waiter, segment$iv4, i$iv);
                        }
                        return;
                    }
                case 3:
                    throw new java.lang.IllegalStateException("unexpected".toString());
                case 4:
                    if ($this$sendersCounter$iv$iv < getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        segment$iv4.cleanPrev();
                    }
                    onClosedSelectOnSend(element, select);
                    return;
                case 5:
                    segment$iv4.cleanPrev();
                default:
                    segment$iv2 = segment$iv4;
                    $i$f$sendImpl = $i$f$sendImpl2;
                    break;
            }
        }
    }

    private final void onClosedSelectOnSend(E element, kotlinx.coroutines.selects.SelectInstance<?> select) {
        kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = this.onUndeliveredElement;
        if (function1 != null) {
            kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElement(function1, element, select.getContext());
        }
        select.selectInRegistrationPhase(kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object processResultSelectSend(java.lang.Object ignoredParam, java.lang.Object selectResult) throws java.lang.Throwable {
        if (selectResult == kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
            throw getSendException();
        }
        return this;
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public kotlinx.coroutines.selects.SelectClause1<E> getOnReceive() {
        kotlinx.coroutines.channels.BufferedChannel$onReceive$1 bufferedChannel$onReceive$1 = kotlinx.coroutines.channels.BufferedChannel$onReceive$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onReceive$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        kotlin.jvm.functions.Function3 function3 = (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onReceive$1, 3);
        kotlinx.coroutines.channels.BufferedChannel$onReceive$2 bufferedChannel$onReceive$2 = kotlinx.coroutines.channels.BufferedChannel$onReceive$2.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onReceive$2, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'param')] kotlin.Any?, @[ParameterName(name = 'clauseResult')] kotlin.Any?, kotlin.Any?>{ kotlinx.coroutines.selects.SelectKt.ProcessResultFunction }");
        return new kotlinx.coroutines.selects.SelectClause1Impl(this, function3, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onReceive$2, 3), this.onUndeliveredElementReceiveCancellationConstructor);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public kotlinx.coroutines.selects.SelectClause1<kotlinx.coroutines.channels.ChannelResult<E>> getOnReceiveCatching() {
        kotlinx.coroutines.channels.BufferedChannel$onReceiveCatching$1 bufferedChannel$onReceiveCatching$1 = kotlinx.coroutines.channels.BufferedChannel$onReceiveCatching$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onReceiveCatching$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        kotlin.jvm.functions.Function3 function3 = (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onReceiveCatching$1, 3);
        kotlinx.coroutines.channels.BufferedChannel$onReceiveCatching$2 bufferedChannel$onReceiveCatching$2 = kotlinx.coroutines.channels.BufferedChannel$onReceiveCatching$2.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onReceiveCatching$2, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'param')] kotlin.Any?, @[ParameterName(name = 'clauseResult')] kotlin.Any?, kotlin.Any?>{ kotlinx.coroutines.selects.SelectKt.ProcessResultFunction }");
        return new kotlinx.coroutines.selects.SelectClause1Impl(this, function3, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onReceiveCatching$2, 3), this.onUndeliveredElementReceiveCancellationConstructor);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public kotlinx.coroutines.selects.SelectClause1<E> getOnReceiveOrNull() {
        kotlinx.coroutines.channels.BufferedChannel$onReceiveOrNull$1 bufferedChannel$onReceiveOrNull$1 = kotlinx.coroutines.channels.BufferedChannel$onReceiveOrNull$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onReceiveOrNull$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        kotlin.jvm.functions.Function3 function3 = (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onReceiveOrNull$1, 3);
        kotlinx.coroutines.channels.BufferedChannel$onReceiveOrNull$2 bufferedChannel$onReceiveOrNull$2 = kotlinx.coroutines.channels.BufferedChannel$onReceiveOrNull$2.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(bufferedChannel$onReceiveOrNull$2, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'param')] kotlin.Any?, @[ParameterName(name = 'clauseResult')] kotlin.Any?, kotlin.Any?>{ kotlinx.coroutines.selects.SelectKt.ProcessResultFunction }");
        return new kotlinx.coroutines.selects.SelectClause1Impl(this, function3, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(bufferedChannel$onReceiveOrNull$2, 3), this.onUndeliveredElementReceiveCancellationConstructor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void registerSelectForReceive(kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object ignoredParam) {
        kotlinx.coroutines.channels.ChannelSegment segment$iv = (kotlinx.coroutines.channels.ChannelSegment) this.receiveSegment.getValue();
        while (!isClosedForReceive()) {
            long r$iv = this.receivers.getAndIncrement();
            long id$iv = r$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            int i$iv = (int) (r$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (segment$iv.id != id$iv) {
                kotlinx.coroutines.channels.ChannelSegment channelSegmentFindSegmentReceive = findSegmentReceive(id$iv, segment$iv);
                if (channelSegmentFindSegmentReceive == null) {
                    continue;
                } else {
                    segment$iv = channelSegmentFindSegmentReceive;
                }
            }
            java.lang.Object updCellResult$iv = updateCellReceive(segment$iv, i$iv, r$iv, select);
            if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                    if (updCellResult$iv == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                        throw new java.lang.IllegalStateException("unexpected".toString());
                    }
                    segment$iv.cleanPrev();
                    select.selectInRegistrationPhase(updCellResult$iv);
                    return;
                }
                if (r$iv < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    segment$iv.cleanPrev();
                }
            } else {
                kotlinx.coroutines.Waiter waiter = select instanceof kotlinx.coroutines.Waiter ? (kotlinx.coroutines.Waiter) select : null;
                if (waiter != null) {
                    prepareReceiverForSuspension(waiter, segment$iv, i$iv);
                }
                return;
            }
        }
        onClosedSelectOnReceive(select);
    }

    private final void onClosedSelectOnReceive(kotlinx.coroutines.selects.SelectInstance<?> select) {
        select.selectInRegistrationPhase(kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object processResultSelectReceive(java.lang.Object ignoredParam, java.lang.Object selectResult) throws java.lang.Throwable {
        if (selectResult == kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
            throw getReceiveException();
        }
        return selectResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object processResultSelectReceiveOrNull(java.lang.Object ignoredParam, java.lang.Object selectResult) throws java.lang.Throwable {
        if (selectResult == kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
            if (getCloseCause() == null) {
                return null;
            }
            throw getReceiveException();
        }
        return selectResult;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Object processResultSelectReceiveCatching(java.lang.Object ignoredParam, java.lang.Object selectResult) {
        return kotlinx.coroutines.channels.ChannelResult.m12819boximpl(selectResult == kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED() ? kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getCloseCause()) : kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12834successJP2dKIU(selectResult));
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public kotlinx.coroutines.channels.ChannelIterator<E> iterator() {
        return new kotlinx.coroutines.channels.BufferedChannel.BufferedChannelIterator();
    }

    /* JADX INFO: compiled from: BufferedChannel.kt */
    @kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0082\u0004\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u000e\u0010\t\u001a\u00020\u0006H\u0096B¢\u0006\u0002\u0010\nJ,\u0010\u000b\u001a\u00020\u00062\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@¢\u0006\u0002\u0010\u0012J\u001c\u0010\u0013\u001a\u00020\u00142\n\u0010\f\u001a\u0006\u0012\u0002\b\u00030\u00152\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u000e\u0010\u0016\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010\u0017J\b\u0010\u0018\u001a\u00020\u0006H\u0002J\b\u0010\u0019\u001a\u00020\u0014H\u0002J\u0013\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u001b\u001a\u00028\u0000¢\u0006\u0002\u0010\u001cJ\u0006\u0010\u001d\u001a\u00020\u0014R\u0016\u0010\u0004\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/channels/BufferedChannel$BufferedChannelIterator;", "Lkotlinx/coroutines/channels/ChannelIterator;", "Lkotlinx/coroutines/Waiter;", "(Lkotlinx/coroutines/channels/BufferedChannel;)V", "continuation", "Lkotlinx/coroutines/CancellableContinuationImpl;", "", "receiveResult", "", "hasNext", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hasNextOnNoWaiterSuspend", "segment", "Lkotlinx/coroutines/channels/ChannelSegment;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "", com.android.server.wm.ActivityTaskManagerService.DUMP_RECENTS_SHORT_CMD, "", "(Lkotlinx/coroutines/channels/ChannelSegment;IJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "invokeOnCancellation", "", "Lkotlinx/coroutines/internal/Segment;", "next", "()Ljava/lang/Object;", "onClosedHasNext", "onClosedHasNextNoWaiterSuspend", "tryResumeHasNext", "element", "(Ljava/lang/Object;)Z", "tryResumeHasNextOnClosedChannel", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private final class BufferedChannelIterator implements kotlinx.coroutines.channels.ChannelIterator<E>, kotlinx.coroutines.Waiter {
        private kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> continuation;
        private java.lang.Object receiveResult = kotlinx.coroutines.channels.BufferedChannelKt.NO_RECEIVE_RESULT;

        public BufferedChannelIterator() {
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        @kotlin.Deprecated(level = kotlin.DeprecationLevel.HIDDEN, message = "Since 1.3.0, binary compatibility with versions <= 1.2.x")
        public /* synthetic */ java.lang.Object next(kotlin.coroutines.Continuation $completion) {
            return kotlinx.coroutines.channels.ChannelIterator.DefaultImpls.next(this, $completion);
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        public java.lang.Object hasNext(kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) {
            kotlinx.coroutines.channels.ChannelSegment<E> channelSegment;
            kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel = kotlinx.coroutines.channels.BufferedChannel.this;
            kotlinx.coroutines.channels.ChannelSegment<E> channelSegment2 = (kotlinx.coroutines.channels.ChannelSegment) ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).receiveSegment.getValue();
            while (!bufferedChannel.isClosedForReceive()) {
                long r$iv = ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).receivers.getAndIncrement();
                long id$iv = r$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
                int i$iv = (int) (r$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
                if (channelSegment2.id != id$iv) {
                    kotlinx.coroutines.channels.ChannelSegment<E> channelSegmentFindSegmentReceive = bufferedChannel.findSegmentReceive(id$iv, channelSegment2);
                    if (channelSegmentFindSegmentReceive == null) {
                        continue;
                    } else {
                        channelSegment = channelSegmentFindSegmentReceive;
                    }
                } else {
                    channelSegment = channelSegment2;
                }
                java.lang.Object updCellResult$iv = bufferedChannel.updateCellReceive(channelSegment, i$iv, r$iv, null);
                if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                    if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                        if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                            channelSegment.cleanPrev();
                            this.receiveResult = updCellResult$iv;
                            return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(true);
                        }
                        return hasNextOnNoWaiterSuspend(channelSegment, i$iv, r$iv, continuation);
                    }
                    if (r$iv < bufferedChannel.getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        channelSegment.cleanPrev();
                    }
                    channelSegment2 = channelSegment;
                } else {
                    throw new java.lang.IllegalStateException(android.net.INetd.NEXTHOP_UNREACHABLE.toString());
                }
            }
            return kotlin.coroutines.jvm.internal.Boxing.boxBoolean(onClosedHasNext());
        }

        private final boolean onClosedHasNext() throws java.lang.Throwable {
            this.receiveResult = kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED();
            java.lang.Throwable cause = kotlinx.coroutines.channels.BufferedChannel.this.getCloseCause();
            if (cause == null) {
                return false;
            }
            throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverStackTrace(cause);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final java.lang.Object hasNextOnNoWaiterSuspend(kotlinx.coroutines.channels.ChannelSegment<E> channelSegment, int index, long r, kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation) throws java.lang.Throwable {
            kotlinx.coroutines.channels.ChannelSegment segment$iv$iv;
            kotlinx.coroutines.channels.BufferedChannel<E> bufferedChannel = kotlinx.coroutines.channels.BufferedChannel.this;
            int i$iv$iv = 0;
            kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation2 = continuation;
            int i = 0;
            kotlinx.coroutines.CancellableContinuationImpl cancellable$iv = kotlinx.coroutines.CancellableContinuationKt.getOrCreateCancellableContinuation(kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation2));
            int i2 = 0;
            try {
                this.continuation = cancellable$iv;
                java.lang.Object updCellResult$iv = bufferedChannel.updateCellReceive(channelSegment, index, r, this);
                try {
                    if (updCellResult$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                        try {
                            if (updCellResult$iv == kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                                if (r < bufferedChannel.getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                                    channelSegment.cleanPrev();
                                }
                                kotlinx.coroutines.channels.ChannelSegment segment$iv$iv2 = (kotlinx.coroutines.channels.ChannelSegment) ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).receiveSegment.getValue();
                                while (!bufferedChannel.isClosedForReceive()) {
                                    long r$iv$iv = ((kotlinx.coroutines.channels.BufferedChannel) bufferedChannel).receivers.getAndIncrement();
                                    long id$iv$iv = r$iv$iv / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
                                    int $i$f$suspendCancellableCoroutineReusable = i$iv$iv;
                                    kotlin.coroutines.Continuation<? super java.lang.Boolean> continuation3 = continuation2;
                                    try {
                                        int i$iv$iv2 = (int) (r$iv$iv % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
                                        int i3 = i;
                                        if (segment$iv$iv2.id != id$iv$iv) {
                                            segment$iv$iv = bufferedChannel.findSegmentReceive(id$iv$iv, segment$iv$iv2);
                                            if (segment$iv$iv == null) {
                                                i$iv$iv = $i$f$suspendCancellableCoroutineReusable;
                                                continuation2 = continuation3;
                                                i = i3;
                                            }
                                        } else {
                                            segment$iv$iv = segment$iv$iv2;
                                        }
                                        int i4 = i2;
                                        java.lang.Object updCellResult$iv2 = updCellResult$iv;
                                        java.lang.Object updCellResult$iv$iv = bufferedChannel.updateCellReceive(segment$iv$iv, i$iv$iv2, r$iv$iv, this);
                                        if (updCellResult$iv$iv != kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND) {
                                            if (updCellResult$iv$iv == kotlinx.coroutines.channels.BufferedChannelKt.FAILED) {
                                                if (r$iv$iv < bufferedChannel.getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                                                    segment$iv$iv.cleanPrev();
                                                }
                                                segment$iv$iv2 = segment$iv$iv;
                                                i2 = i4;
                                                i$iv$iv = $i$f$suspendCancellableCoroutineReusable;
                                                continuation2 = continuation3;
                                                i = i3;
                                                updCellResult$iv = updCellResult$iv2;
                                            } else {
                                                if (updCellResult$iv$iv == kotlinx.coroutines.channels.BufferedChannelKt.SUSPEND_NO_WAITER) {
                                                    throw new java.lang.IllegalStateException("unexpected".toString());
                                                }
                                                segment$iv$iv.cleanPrev();
                                                this.receiveResult = updCellResult$iv$iv;
                                                this.continuation = null;
                                                java.lang.Boolean boolBoxBoolean = kotlin.coroutines.jvm.internal.Boxing.boxBoolean(true);
                                                kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = bufferedChannel.onUndeliveredElement;
                                                cancellable$iv.resume(boolBoxBoolean, function1 != null ? kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun(function1, updCellResult$iv$iv, cancellable$iv.getContext()) : null);
                                            }
                                        } else {
                                            kotlinx.coroutines.channels.BufferedChannel.BufferedChannelIterator bufferedChannelIterator = this instanceof kotlinx.coroutines.Waiter ? this : null;
                                            if (bufferedChannelIterator != null) {
                                                bufferedChannel.prepareReceiverForSuspension(bufferedChannelIterator, segment$iv$iv, i$iv$iv2);
                                            }
                                        }
                                    } catch (java.lang.Throwable th) {
                                        e$iv = th;
                                        cancellable$iv.releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                                        throw e$iv;
                                    }
                                }
                                onClosedHasNextNoWaiterSuspend();
                            } else {
                                channelSegment.cleanPrev();
                                this.receiveResult = updCellResult$iv;
                                this.continuation = null;
                                java.lang.Boolean boolBoxBoolean2 = kotlin.coroutines.jvm.internal.Boxing.boxBoolean(true);
                                kotlin.jvm.functions.Function1<E, kotlin.Unit> function12 = bufferedChannel.onUndeliveredElement;
                                cancellable$iv.resume(boolBoxBoolean2, function12 != null ? kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun(function12, updCellResult$iv, cancellable$iv.getContext()) : null);
                            }
                        } catch (java.lang.Throwable th2) {
                            e$iv = th2;
                        }
                    } else {
                        try {
                            bufferedChannel.prepareReceiverForSuspension(this, channelSegment, index);
                        } catch (java.lang.Throwable th3) {
                            e$iv = th3;
                            cancellable$iv.releaseClaimedReusableContinuation$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                            throw e$iv;
                        }
                    }
                    java.lang.Object result = cancellable$iv.getResult();
                    if (result == kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                        kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
                    }
                    return result;
                } catch (java.lang.Throwable th4) {
                    e$iv = th4;
                }
            } catch (java.lang.Throwable th5) {
                e$iv = th5;
            }
        }

        @Override // kotlinx.coroutines.Waiter
        public void invokeOnCancellation(kotlinx.coroutines.internal.Segment<?> segment, int index) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(segment, "segment");
            kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> cancellableContinuationImpl = this.continuation;
            if (cancellableContinuationImpl != null) {
                cancellableContinuationImpl.invokeOnCancellation(segment, index);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void onClosedHasNextNoWaiterSuspend() {
            java.lang.Throwable thRecoverFromStackFrame;
            kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> cancellableContinuationImpl = this.continuation;
            kotlin.jvm.internal.Intrinsics.checkNotNull(cancellableContinuationImpl);
            this.continuation = null;
            this.receiveResult = kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED();
            java.lang.Throwable cause = kotlinx.coroutines.channels.BufferedChannel.this.getCloseCause();
            if (cause == null) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                cancellableContinuationImpl.resumeWith(kotlin.Result.m11307constructorimpl(false));
                return;
            }
            kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> cancellableContinuationImpl2 = cancellableContinuationImpl;
            if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (cancellableContinuationImpl instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                thRecoverFromStackFrame = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(cause, cancellableContinuationImpl);
            } else {
                thRecoverFromStackFrame = cause;
            }
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            cancellableContinuationImpl2.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(thRecoverFromStackFrame)));
        }

        @Override // kotlinx.coroutines.channels.ChannelIterator
        public E next() throws java.lang.Throwable {
            E e = (E) this.receiveResult;
            if (!(e != kotlinx.coroutines.channels.BufferedChannelKt.NO_RECEIVE_RESULT)) {
                throw new java.lang.IllegalStateException("`hasNext()` has not been invoked".toString());
            }
            this.receiveResult = kotlinx.coroutines.channels.BufferedChannelKt.NO_RECEIVE_RESULT;
            if (e == kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()) {
                throw kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverStackTrace(kotlinx.coroutines.channels.BufferedChannel.this.getReceiveException());
            }
            return e;
        }

        public final boolean tryResumeHasNext(E element) {
            kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> cancellableContinuationImpl = this.continuation;
            kotlin.jvm.internal.Intrinsics.checkNotNull(cancellableContinuationImpl);
            this.continuation = null;
            this.receiveResult = element;
            kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> cancellableContinuationImpl2 = cancellableContinuationImpl;
            kotlin.jvm.functions.Function1<E, kotlin.Unit> function1 = kotlinx.coroutines.channels.BufferedChannel.this.onUndeliveredElement;
            return kotlinx.coroutines.channels.BufferedChannelKt.tryResume0(cancellableContinuationImpl2, true, function1 != null ? kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun(function1, element, cancellableContinuationImpl.getContext()) : null);
        }

        public final void tryResumeHasNextOnClosedChannel() {
            java.lang.Throwable thRecoverFromStackFrame;
            kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> cancellableContinuationImpl = this.continuation;
            kotlin.jvm.internal.Intrinsics.checkNotNull(cancellableContinuationImpl);
            this.continuation = null;
            this.receiveResult = kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED();
            java.lang.Throwable cause = kotlinx.coroutines.channels.BufferedChannel.this.getCloseCause();
            if (cause == null) {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                cancellableContinuationImpl.resumeWith(kotlin.Result.m11307constructorimpl(false));
                return;
            }
            kotlinx.coroutines.CancellableContinuationImpl<? super java.lang.Boolean> cancellableContinuationImpl2 = cancellableContinuationImpl;
            if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (cancellableContinuationImpl instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
                thRecoverFromStackFrame = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(cause, cancellableContinuationImpl);
            } else {
                thRecoverFromStackFrame = cause;
            }
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            cancellableContinuationImpl2.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(thRecoverFromStackFrame)));
        }
    }

    protected final java.lang.Throwable getCloseCause() {
        return (java.lang.Throwable) this._closeCause.getValue();
    }

    protected final java.lang.Throwable getSendException() {
        java.lang.Throwable closeCause = getCloseCause();
        return closeCause == null ? new kotlinx.coroutines.channels.ClosedSendChannelException(kotlinx.coroutines.channels.ChannelsKt.DEFAULT_CLOSE_MESSAGE) : closeCause;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.Throwable getReceiveException() {
        java.lang.Throwable closeCause = getCloseCause();
        return closeCause == null ? new kotlinx.coroutines.channels.ClosedReceiveChannelException(kotlinx.coroutines.channels.ChannelsKt.DEFAULT_CLOSE_MESSAGE) : closeCause;
    }

    protected void onClosedIdempotent() {
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean close(java.lang.Throwable cause) {
        return closeOrCancelImpl(cause, false);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final boolean cancel(java.lang.Throwable cause) {
        return cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final void cancel() {
        cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(null);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public final void cancel(java.util.concurrent.CancellationException cause) {
        cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(cause);
    }

    public boolean cancelImpl$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(java.lang.Throwable cause) {
        return closeOrCancelImpl(cause == null ? new java.util.concurrent.CancellationException("Channel was cancelled") : cause, true);
    }

    protected boolean closeOrCancelImpl(java.lang.Throwable cause, boolean cancel) {
        if (cancel) {
            markCancellationStarted();
        }
        boolean closedByThisOperation = this._closeCause.compareAndSet(kotlinx.coroutines.channels.BufferedChannelKt.NO_CLOSE_CAUSE, cause);
        if (cancel) {
            markCancelled();
        } else {
            markClosed();
        }
        completeCloseOrCancel();
        onClosedIdempotent();
        if (closedByThisOperation) {
            invokeCloseHandler();
        }
        return closedByThisOperation;
    }

    private final void invokeCloseHandler() {
        java.lang.Object cur$iv;
        java.lang.Object it;
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this.closeHandler;
        do {
            cur$iv = atomicRef.getValue();
            it = cur$iv == null ? kotlinx.coroutines.channels.BufferedChannelKt.CLOSE_HANDLER_CLOSED : kotlinx.coroutines.channels.BufferedChannelKt.CLOSE_HANDLER_INVOKED;
        } while (!atomicRef.compareAndSet(cur$iv, it));
        if (cur$iv != null) {
            ((kotlin.jvm.functions.Function1) cur$iv).invoke(getCloseCause());
        }
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public void invokeOnClose(kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> handler) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(handler, "handler");
        if (this.closeHandler.compareAndSet(null, handler)) {
            return;
        }
        kotlinx.atomicfu.AtomicRef<java.lang.Object> atomicRef = this.closeHandler;
        do {
            java.lang.Object cur = atomicRef.getValue();
            if (cur != kotlinx.coroutines.channels.BufferedChannelKt.CLOSE_HANDLER_CLOSED) {
                if (cur != kotlinx.coroutines.channels.BufferedChannelKt.CLOSE_HANDLER_INVOKED) {
                    throw new java.lang.IllegalStateException(("Another handler is already registered: " + cur).toString());
                }
                throw new java.lang.IllegalStateException("Another handler was already registered and successfully invoked".toString());
            }
        } while (!this.closeHandler.compareAndSet(kotlinx.coroutines.channels.BufferedChannelKt.CLOSE_HANDLER_CLOSED, kotlinx.coroutines.channels.BufferedChannelKt.CLOSE_HANDLER_INVOKED));
        handler.invoke(getCloseCause());
    }

    private final void markClosed() {
        long cur$iv;
        long jConstructSendersAndCloseStatus;
        long cur;
        kotlinx.atomicfu.AtomicLong $this$update$iv = this.sendersAndCloseStatus;
        do {
            cur$iv = $this$update$iv.getValue();
            switch ((int) (cur$iv >> 60)) {
                case 0:
                    jConstructSendersAndCloseStatus = kotlinx.coroutines.channels.BufferedChannelKt.constructSendersAndCloseStatus(cur$iv & 1152921504606846975L, 2);
                    break;
                case 1:
                    jConstructSendersAndCloseStatus = kotlinx.coroutines.channels.BufferedChannelKt.constructSendersAndCloseStatus(cur$iv & 1152921504606846975L, 3);
                    break;
                default:
                    return;
            }
            cur = jConstructSendersAndCloseStatus;
        } while (!$this$update$iv.compareAndSet(cur$iv, cur));
    }

    private final void markCancelled() {
        long cur$iv;
        long cur;
        kotlinx.atomicfu.AtomicLong $this$update$iv = this.sendersAndCloseStatus;
        do {
            cur$iv = $this$update$iv.getValue();
            long $this$sendersCounter$iv = cur$iv & 1152921504606846975L;
            cur = kotlinx.coroutines.channels.BufferedChannelKt.constructSendersAndCloseStatus($this$sendersCounter$iv, 3);
        } while (!$this$update$iv.compareAndSet(cur$iv, cur));
    }

    private final void markCancellationStarted() {
        long cur$iv;
        long upd$iv;
        kotlinx.atomicfu.AtomicLong $this$update$iv = this.sendersAndCloseStatus;
        do {
            cur$iv = $this$update$iv.getValue();
            if (((int) (cur$iv >> 60)) == 0) {
                long $this$sendersCounter$iv = cur$iv & 1152921504606846975L;
                upd$iv = kotlinx.coroutines.channels.BufferedChannelKt.constructSendersAndCloseStatus($this$sendersCounter$iv, 1);
            } else {
                return;
            }
        } while (!$this$update$iv.compareAndSet(cur$iv, upd$iv));
    }

    private final void completeCloseOrCancel() {
        isClosedForSend();
    }

    protected boolean isConflatedDropOldest() {
        return false;
    }

    private final kotlinx.coroutines.channels.ChannelSegment<E> completeClose(long sendersCur) {
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegmentCloseLinkedList = closeLinkedList();
        if (isConflatedDropOldest()) {
            long lastBufferedCellGlobalIndex = markAllEmptyCellsAsClosed(channelSegmentCloseLinkedList);
            if (lastBufferedCellGlobalIndex != -1) {
                dropFirstElementUntilTheSpecifiedCellIsInTheBuffer(lastBufferedCellGlobalIndex);
            }
        }
        cancelSuspendedReceiveRequests(channelSegmentCloseLinkedList, sendersCur);
        return channelSegmentCloseLinkedList;
    }

    private final void completeCancel(long sendersCur) {
        removeUnprocessedElements(completeClose(sendersCur));
    }

    private final kotlinx.coroutines.channels.ChannelSegment<E> closeLinkedList() {
        java.lang.Object lastSegment = this.bufferEndSegment.getValue();
        kotlinx.coroutines.channels.ChannelSegment<E> value = this.sendSegment.getValue();
        if (value.id > ((kotlinx.coroutines.channels.ChannelSegment) lastSegment).id) {
            lastSegment = value;
        }
        kotlinx.coroutines.channels.ChannelSegment<E> value2 = this.receiveSegment.getValue();
        if (value2.id > ((kotlinx.coroutines.channels.ChannelSegment) lastSegment).id) {
            lastSegment = value2;
        }
        java.lang.Object it = lastSegment;
        return (kotlinx.coroutines.channels.ChannelSegment) kotlinx.coroutines.internal.ConcurrentLinkedListKt.close((kotlinx.coroutines.internal.ConcurrentLinkedListNode) it);
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0042, code lost:
    
        r1 = (kotlinx.coroutines.channels.ChannelSegment) r0.getPrev();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0048, code lost:
    
        if (r1 != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x004a, code lost:
    
        return -1;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final long markAllEmptyCellsAsClosed(kotlinx.coroutines.channels.ChannelSegment<E> r9) {
        /*
            r8 = this;
            r0 = r9
        L1:
            int r1 = kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE
            int r1 = r1 + (-1)
        L6:
            r2 = -1
            r4 = -1
            if (r4 >= r1) goto L42
            long r4 = r0.id
            int r6 = kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE
            long r6 = (long) r6
            long r4 = r4 * r6
            long r6 = (long) r1
            long r4 = r4 + r6
            long r6 = r8.getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()
            int r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r6 >= 0) goto L1c
            return r2
        L1c:
            java.lang.Object r2 = r0.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r1)
            if (r2 == 0) goto L31
            kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.channels.BufferedChannelKt.access$getIN_BUFFER$p()
            if (r2 != r3) goto L2b
            goto L31
        L2b:
            kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED
            if (r2 != r3) goto L30
            return r4
        L30:
            goto L3f
        L31:
            kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED()
            boolean r3 = r0.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(r1, r2, r3)
            if (r3 == 0) goto L1c
            r0.onSlotCleaned()
        L3f:
            int r1 = r1 + (-1)
            goto L6
        L42:
            kotlinx.coroutines.internal.ConcurrentLinkedListNode r1 = r0.getPrev()
            kotlinx.coroutines.channels.ChannelSegment r1 = (kotlinx.coroutines.channels.ChannelSegment) r1
            if (r1 != 0) goto L4b
            return r2
        L4b:
            r0 = r1
            goto L1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BufferedChannel.markAllEmptyCellsAsClosed(kotlinx.coroutines.channels.ChannelSegment):long");
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x00ba, code lost:
    
        r5 = (kotlinx.coroutines.channels.ChannelSegment) r4.getPrev();
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00c0, code lost:
    
        if (r5 != null) goto L65;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void removeUnprocessedElements(kotlinx.coroutines.channels.ChannelSegment<E> r13) {
        /*
            Method dump skipped, instruction units count: 250
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BufferedChannel.removeUnprocessedElements(kotlinx.coroutines.channels.ChannelSegment):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void cancelSuspendedReceiveRequests(kotlinx.coroutines.channels.ChannelSegment<E> lastSegment, long sendersCounter) {
        java.lang.Object suspendedReceivers = kotlinx.coroutines.internal.InlineList.m12855constructorimpl$default(null, 1, null);
        loop0: for (kotlinx.coroutines.channels.ChannelSegment<E> channelSegment = lastSegment; channelSegment != null; channelSegment = (kotlinx.coroutines.channels.ChannelSegment) channelSegment.getPrev()) {
            for (int index = kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE - 1; -1 < index; index--) {
                if ((channelSegment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE)) + ((long) index) < sendersCounter) {
                    break loop0;
                }
                while (true) {
                    java.lang.Object state = channelSegment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
                    if (state == null || state == kotlinx.coroutines.channels.BufferedChannelKt.IN_BUFFER) {
                        if (channelSegment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED())) {
                            channelSegment.onSlotCleaned();
                            break;
                        }
                    } else if (state instanceof kotlinx.coroutines.channels.WaiterEB) {
                        if (channelSegment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED())) {
                            suspendedReceivers = kotlinx.coroutines.internal.InlineList.m12860plusFjFbRPM(suspendedReceivers, ((kotlinx.coroutines.channels.WaiterEB) state).waiter);
                            channelSegment.onCancelledRequest(index, true);
                            break;
                        }
                    } else {
                        if (!(state instanceof kotlinx.coroutines.Waiter)) {
                            break;
                        }
                        if (channelSegment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED())) {
                            suspendedReceivers = kotlinx.coroutines.internal.InlineList.m12860plusFjFbRPM(suspendedReceivers, state);
                            channelSegment.onCancelledRequest(index, true);
                            break;
                        }
                    }
                }
            }
        }
        if (suspendedReceivers == null) {
            return;
        }
        if (!(suspendedReceivers instanceof java.util.ArrayList)) {
            kotlinx.coroutines.Waiter it = (kotlinx.coroutines.Waiter) suspendedReceivers;
            resumeReceiverOnClosedChannel(it);
            return;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(suspendedReceivers, "null cannot be cast to non-null type java.util.ArrayList<E of kotlinx.coroutines.internal.InlineList>{ kotlin.collections.TypeAliasesKt.ArrayList<E of kotlinx.coroutines.internal.InlineList> }");
        java.util.ArrayList list$iv = (java.util.ArrayList) suspendedReceivers;
        for (int i$iv = list$iv.size() - 1; -1 < i$iv; i$iv--) {
            kotlinx.coroutines.Waiter it2 = (kotlinx.coroutines.Waiter) list$iv.get(i$iv);
            resumeReceiverOnClosedChannel(it2);
        }
    }

    private final void resumeReceiverOnClosedChannel(kotlinx.coroutines.Waiter $this$resumeReceiverOnClosedChannel) {
        resumeWaiterOnClosedChannel($this$resumeReceiverOnClosedChannel, true);
    }

    private final void resumeSenderOnCancelledChannel(kotlinx.coroutines.Waiter $this$resumeSenderOnCancelledChannel) {
        resumeWaiterOnClosedChannel($this$resumeSenderOnCancelledChannel, false);
    }

    private final void resumeWaiterOnClosedChannel(kotlinx.coroutines.Waiter $this$resumeWaiterOnClosedChannel, boolean receiver) {
        if (!($this$resumeWaiterOnClosedChannel instanceof kotlinx.coroutines.channels.BufferedChannel.SendBroadcast)) {
            if (!($this$resumeWaiterOnClosedChannel instanceof kotlinx.coroutines.CancellableContinuation)) {
                if (!($this$resumeWaiterOnClosedChannel instanceof kotlinx.coroutines.channels.ReceiveCatching)) {
                    if (!($this$resumeWaiterOnClosedChannel instanceof kotlinx.coroutines.channels.BufferedChannel.BufferedChannelIterator)) {
                        if (!($this$resumeWaiterOnClosedChannel instanceof kotlinx.coroutines.selects.SelectInstance)) {
                            throw new java.lang.IllegalStateException(("Unexpected waiter: " + $this$resumeWaiterOnClosedChannel).toString());
                        }
                        ((kotlinx.coroutines.selects.SelectInstance) $this$resumeWaiterOnClosedChannel).trySelect(this, kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED());
                        return;
                    }
                    ((kotlinx.coroutines.channels.BufferedChannel.BufferedChannelIterator) $this$resumeWaiterOnClosedChannel).tryResumeHasNextOnClosedChannel();
                    return;
                }
                kotlinx.coroutines.CancellableContinuationImpl<kotlinx.coroutines.channels.ChannelResult<? extends E>> cancellableContinuationImpl = ((kotlinx.coroutines.channels.ReceiveCatching) $this$resumeWaiterOnClosedChannel).cont;
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                cancellableContinuationImpl.resumeWith(kotlin.Result.m11307constructorimpl(kotlinx.coroutines.channels.ChannelResult.m12819boximpl(kotlinx.coroutines.channels.ChannelResult.INSTANCE.m12832closedJP2dKIU(getCloseCause()))));
                return;
            }
            kotlin.coroutines.Continuation continuation = (kotlin.coroutines.Continuation) $this$resumeWaiterOnClosedChannel;
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            continuation.resumeWith(kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(receiver ? getReceiveException() : getSendException())));
            return;
        }
        kotlinx.coroutines.CancellableContinuation<java.lang.Boolean> cont = ((kotlinx.coroutines.channels.BufferedChannel.SendBroadcast) $this$resumeWaiterOnClosedChannel).getCont();
        kotlin.Result.Companion companion3 = kotlin.Result.INSTANCE;
        cont.resumeWith(kotlin.Result.m11307constructorimpl(false));
    }

    @Override // kotlinx.coroutines.channels.SendChannel
    public boolean isClosedForSend() {
        return isClosedForSend0(this.sendersAndCloseStatus.getValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isClosedForSend0(long $this$isClosedForSend0) {
        return isClosed($this$isClosedForSend0, false);
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public boolean isClosedForReceive() {
        return isClosedForReceive0(this.sendersAndCloseStatus.getValue());
    }

    private final boolean isClosedForReceive0(long $this$isClosedForReceive0) {
        return isClosed($this$isClosedForReceive0, true);
    }

    private final boolean isClosed(long sendersAndCloseStatusCur, boolean isClosedForReceive) {
        switch ((int) (sendersAndCloseStatusCur >> 60)) {
            case 0:
                return false;
            case 1:
                return false;
            case 2:
                completeClose(sendersAndCloseStatusCur & 1152921504606846975L);
                return (isClosedForReceive && hasElements$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) ? false : true;
            case 3:
                completeCancel(sendersAndCloseStatusCur & 1152921504606846975L);
                return true;
            default:
                throw new java.lang.IllegalStateException(("unexpected close status: " + ((int) (sendersAndCloseStatusCur >> 60))).toString());
        }
    }

    @Override // kotlinx.coroutines.channels.ReceiveChannel
    public boolean isEmpty() {
        if (isClosedForReceive() || hasElements$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
            return false;
        }
        return !isClosedForReceive();
    }

    public final boolean hasElements$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        while (true) {
            kotlinx.coroutines.channels.ChannelSegment<E> value = this.receiveSegment.getValue();
            long r = getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            long s = getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            if (s <= r) {
                return false;
            }
            long id = r / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE);
            if (value.id != id) {
                kotlinx.coroutines.channels.ChannelSegment<E> channelSegmentFindSegmentReceive = findSegmentReceive(id, value);
                if (channelSegmentFindSegmentReceive != null) {
                    value = channelSegmentFindSegmentReceive;
                } else if (this.receiveSegment.getValue().id < id) {
                    return false;
                }
            }
            value.cleanPrev();
            int i = (int) (r % ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (isCellNonEmpty(value, i, r)) {
                return true;
            }
            this.receivers.compareAndSet(r, 1 + r);
        }
    }

    private final boolean isCellNonEmpty(kotlinx.coroutines.channels.ChannelSegment<E> segment, int index, long globalIndex) {
        java.lang.Object state;
        do {
            state = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index);
            if (state != null && state != kotlinx.coroutines.channels.BufferedChannelKt.IN_BUFFER) {
                if (state == kotlinx.coroutines.channels.BufferedChannelKt.BUFFERED) {
                    return true;
                }
                if (state != kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND && state != kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED() && state != kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV && state != kotlinx.coroutines.channels.BufferedChannelKt.POISONED) {
                    if (state == kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_EB) {
                        return true;
                    }
                    return state != kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_RCV && globalIndex == getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                }
                return false;
            }
        } while (!segment.casState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(index, state, kotlinx.coroutines.channels.BufferedChannelKt.POISONED));
        expandBuffer();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final kotlinx.coroutines.channels.ChannelSegment<E> findSegmentSend(long id, kotlinx.coroutines.channels.ChannelSegment<E> startFrom) {
        java.lang.Object s$iv;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> atomicRef;
        kotlin.jvm.functions.Function2 createNewSegment$iv;
        boolean z;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> atomicRef2 = this.sendSegment;
        kotlin.jvm.functions.Function2 createNewSegment$iv2 = (kotlin.jvm.functions.Function2) kotlinx.coroutines.channels.BufferedChannelKt.createSegmentFunction();
        while (true) {
            s$iv = kotlinx.coroutines.internal.ConcurrentLinkedListKt.findSegmentInternal(startFrom, id, createNewSegment$iv2);
            if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s$iv)) {
                break;
            }
            kotlinx.coroutines.internal.Segment to$iv$iv = kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
            kotlinx.atomicfu.AtomicRef $this$moveForward$iv$iv = atomicRef2;
            while (true) {
                kotlinx.coroutines.internal.Segment cur$iv$iv = (kotlinx.coroutines.internal.Segment) $this$moveForward$iv$iv.getValue();
                atomicRef = atomicRef2;
                createNewSegment$iv = createNewSegment$iv2;
                if (cur$iv$iv.id >= to$iv$iv.id) {
                    z = true;
                    break;
                }
                if (!to$iv$iv.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    z = false;
                    break;
                }
                if ($this$moveForward$iv$iv.compareAndSet(cur$iv$iv, to$iv$iv)) {
                    if (cur$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        cur$iv$iv.remove();
                    }
                    z = true;
                } else {
                    if (to$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        to$iv$iv.remove();
                    }
                    atomicRef2 = atomicRef;
                    createNewSegment$iv2 = createNewSegment$iv;
                }
            }
            if (z) {
                break;
            }
            atomicRef2 = atomicRef;
            createNewSegment$iv2 = createNewSegment$iv;
        }
        if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s$iv)) {
            completeCloseOrCancel();
            if (startFrom.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE) >= getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                return null;
            }
            startFrom.cleanPrev();
            return null;
        }
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment = (kotlinx.coroutines.channels.ChannelSegment) kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
        if (channelSegment.id > id) {
            updateSendersCounterIfLower(channelSegment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (channelSegment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE) >= getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                return null;
            }
            channelSegment.cleanPrev();
            return null;
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(channelSegment.id == id)) {
                throw new java.lang.AssertionError();
            }
        }
        return channelSegment;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final kotlinx.coroutines.channels.ChannelSegment<E> findSegmentReceive(long id, kotlinx.coroutines.channels.ChannelSegment<E> startFrom) {
        java.lang.Object s$iv;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> atomicRef;
        kotlin.jvm.functions.Function2 createNewSegment$iv;
        boolean z;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> atomicRef2 = this.receiveSegment;
        kotlin.jvm.functions.Function2 createNewSegment$iv2 = (kotlin.jvm.functions.Function2) kotlinx.coroutines.channels.BufferedChannelKt.createSegmentFunction();
        while (true) {
            s$iv = kotlinx.coroutines.internal.ConcurrentLinkedListKt.findSegmentInternal(startFrom, id, createNewSegment$iv2);
            if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s$iv)) {
                break;
            }
            kotlinx.coroutines.internal.Segment to$iv$iv = kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
            kotlinx.atomicfu.AtomicRef $this$moveForward$iv$iv = atomicRef2;
            while (true) {
                kotlinx.coroutines.internal.Segment cur$iv$iv = (kotlinx.coroutines.internal.Segment) $this$moveForward$iv$iv.getValue();
                atomicRef = atomicRef2;
                createNewSegment$iv = createNewSegment$iv2;
                if (cur$iv$iv.id >= to$iv$iv.id) {
                    z = true;
                    break;
                }
                if (!to$iv$iv.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    z = false;
                    break;
                }
                if ($this$moveForward$iv$iv.compareAndSet(cur$iv$iv, to$iv$iv)) {
                    if (cur$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        cur$iv$iv.remove();
                    }
                    z = true;
                } else {
                    if (to$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        to$iv$iv.remove();
                    }
                    atomicRef2 = atomicRef;
                    createNewSegment$iv2 = createNewSegment$iv;
                }
            }
            if (z) {
                break;
            }
            atomicRef2 = atomicRef;
            createNewSegment$iv2 = createNewSegment$iv;
        }
        java.lang.Object it = s$iv;
        if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(it)) {
            completeCloseOrCancel();
            if (startFrom.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE) < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                startFrom.cleanPrev();
            }
            return null;
        }
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment = (kotlinx.coroutines.channels.ChannelSegment) kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(it);
        if (!isRendezvousOrUnlimited() && id <= getBufferEndCounter() / ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE)) {
            kotlinx.atomicfu.AtomicRef $this$moveForward$iv = this.bufferEndSegment;
            while (true) {
                kotlinx.coroutines.internal.Segment cur$iv = (kotlinx.coroutines.internal.Segment) $this$moveForward$iv.getValue();
                java.lang.Object it2 = it;
                if (cur$iv.id >= channelSegment.id || !channelSegment.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    break;
                }
                if ($this$moveForward$iv.compareAndSet(cur$iv, channelSegment)) {
                    if (cur$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        cur$iv.remove();
                    }
                } else {
                    if (channelSegment.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        channelSegment.remove();
                    }
                    it = it2;
                }
            }
        }
        if (channelSegment.id > id) {
            updateReceiversCounterIfLower(channelSegment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE));
            if (channelSegment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE) < getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                channelSegment.cleanPrev();
            }
            return null;
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(channelSegment.id == id)) {
                throw new java.lang.AssertionError();
            }
        }
        return channelSegment;
    }

    private final kotlinx.coroutines.channels.ChannelSegment<E> findSegmentBufferEnd(long id, kotlinx.coroutines.channels.ChannelSegment<E> startFrom, long currentBufferEndCounter) {
        java.lang.Object s$iv;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> atomicRef;
        kotlin.jvm.functions.Function2 createNewSegment$iv;
        boolean z;
        kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.channels.ChannelSegment<E>> atomicRef2 = this.bufferEndSegment;
        kotlin.jvm.functions.Function2 createNewSegment$iv2 = (kotlin.jvm.functions.Function2) kotlinx.coroutines.channels.BufferedChannelKt.createSegmentFunction();
        while (true) {
            s$iv = kotlinx.coroutines.internal.ConcurrentLinkedListKt.findSegmentInternal(startFrom, id, createNewSegment$iv2);
            if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s$iv)) {
                break;
            }
            kotlinx.coroutines.internal.Segment to$iv$iv = kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
            kotlinx.atomicfu.AtomicRef $this$moveForward$iv$iv = atomicRef2;
            while (true) {
                kotlinx.coroutines.internal.Segment cur$iv$iv = (kotlinx.coroutines.internal.Segment) $this$moveForward$iv$iv.getValue();
                atomicRef = atomicRef2;
                createNewSegment$iv = createNewSegment$iv2;
                if (cur$iv$iv.id >= to$iv$iv.id) {
                    z = true;
                    break;
                }
                if (!to$iv$iv.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                    z = false;
                    break;
                }
                if ($this$moveForward$iv$iv.compareAndSet(cur$iv$iv, to$iv$iv)) {
                    if (cur$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        cur$iv$iv.remove();
                    }
                    z = true;
                } else {
                    if (to$iv$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        to$iv$iv.remove();
                    }
                    atomicRef2 = atomicRef;
                    createNewSegment$iv2 = createNewSegment$iv;
                }
            }
            if (z) {
                break;
            }
            atomicRef2 = atomicRef;
            createNewSegment$iv2 = createNewSegment$iv;
        }
        if (kotlinx.coroutines.internal.SegmentOrClosed.m12872isClosedimpl(s$iv)) {
            completeCloseOrCancel();
            moveSegmentBufferEndToSpecifiedOrLast(id, startFrom);
            incCompletedExpandBufferAttempts$default(this, 0L, 1, null);
            return null;
        }
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment = (kotlinx.coroutines.channels.ChannelSegment) kotlinx.coroutines.internal.SegmentOrClosed.m12870getSegmentimpl(s$iv);
        if (channelSegment.id > id) {
            if (this.bufferEnd.compareAndSet(currentBufferEndCounter + 1, channelSegment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE))) {
                incCompletedExpandBufferAttempts((channelSegment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE)) - currentBufferEndCounter);
                return null;
            }
            incCompletedExpandBufferAttempts$default(this, 0L, 1, null);
            return null;
        }
        if (kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()) {
            if (!(channelSegment.id == id)) {
                throw new java.lang.AssertionError();
            }
        }
        return channelSegment;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final void moveSegmentBufferEndToSpecifiedOrLast(long id, kotlinx.coroutines.channels.ChannelSegment<E> startFrom) {
        boolean z;
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment;
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment2;
        kotlinx.coroutines.channels.ChannelSegment<E> channelSegment3 = startFrom;
        while (channelSegment3.id < id && (channelSegment2 = (kotlinx.coroutines.channels.ChannelSegment) channelSegment3.getNext()) != null) {
            channelSegment3 = channelSegment2;
        }
        while (true) {
            if (!channelSegment3.isRemoved() || (channelSegment = (kotlinx.coroutines.channels.ChannelSegment) channelSegment3.getNext()) == null) {
                kotlinx.atomicfu.AtomicRef $this$moveForward$iv = this.bufferEndSegment;
                while (true) {
                    kotlinx.coroutines.internal.Segment cur$iv = (kotlinx.coroutines.internal.Segment) $this$moveForward$iv.getValue();
                    z = true;
                    if (cur$iv.id >= channelSegment3.id) {
                        break;
                    }
                    if (!channelSegment3.tryIncPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        z = false;
                        break;
                    } else if ($this$moveForward$iv.compareAndSet(cur$iv, channelSegment3)) {
                        if (cur$iv.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                            cur$iv.remove();
                        }
                    } else if (channelSegment3.decPointers$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host()) {
                        channelSegment3.remove();
                    }
                }
                if (z) {
                    return;
                }
            } else {
                channelSegment3 = channelSegment;
            }
        }
    }

    private final void updateSendersCounterIfLower(long value) {
        long cur;
        long update;
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this.sendersAndCloseStatus;
        do {
            cur = $this$loop$iv.getValue();
            long curCounter = cur & 1152921504606846975L;
            if (curCounter >= value) {
                return;
            } else {
                update = kotlinx.coroutines.channels.BufferedChannelKt.constructSendersAndCloseStatus(curCounter, (int) (cur >> 60));
            }
        } while (!this.sendersAndCloseStatus.compareAndSet(cur, update));
    }

    private final void updateReceiversCounterIfLower(long value) {
        long cur;
        kotlinx.atomicfu.AtomicLong $this$loop$iv = this.receivers;
        do {
            cur = $this$loop$iv.getValue();
            if (cur >= value) {
                return;
            }
        } while (!this.receivers.compareAndSet(cur, value));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public java.lang.String toString() {
        int i;
        java.lang.String string;
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        long $this$sendersCloseStatus$iv = this.sendersAndCloseStatus.getValue();
        switch ((int) ($this$sendersCloseStatus$iv >> 60)) {
            case 2:
                sb.append("closed,");
                break;
            case 3:
                sb.append("cancelled,");
                break;
        }
        sb.append("capacity=" + this.capacity + ",");
        sb.append("data=[");
        java.lang.Iterable $this$filter$iv = kotlin.collections.CollectionsKt.listOf((java.lang.Object[]) new kotlinx.coroutines.channels.ChannelSegment[]{this.receiveSegment.getValue(), this.sendSegment.getValue(), this.bufferEndSegment.getValue()});
        java.util.ArrayList arrayList = new java.util.ArrayList();
        for (java.lang.Object element$iv$iv : $this$filter$iv) {
            kotlinx.coroutines.channels.ChannelSegment it = (kotlinx.coroutines.channels.ChannelSegment) element$iv$iv;
            if (it != kotlinx.coroutines.channels.BufferedChannelKt.NULL_SEGMENT) {
                arrayList.add(element$iv$iv);
            }
        }
        java.util.ArrayList $this$minBy$iv = arrayList;
        java.util.Iterator iterator$iv = $this$minBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        java.lang.Object minElem$iv = iterator$iv.next();
        if (iterator$iv.hasNext()) {
            kotlinx.coroutines.channels.ChannelSegment it2 = (kotlinx.coroutines.channels.ChannelSegment) minElem$iv;
            long minValue$iv = it2.id;
            do {
                java.lang.Object e$iv = iterator$iv.next();
                kotlinx.coroutines.channels.ChannelSegment it3 = (kotlinx.coroutines.channels.ChannelSegment) e$iv;
                long v$iv = it3.id;
                if (minValue$iv > v$iv) {
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                }
            } while (iterator$iv.hasNext());
        }
        kotlinx.coroutines.channels.ChannelSegment firstSegment = (kotlinx.coroutines.channels.ChannelSegment) minElem$iv;
        long r = getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        long s = getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        kotlinx.coroutines.channels.ChannelSegment segment = firstSegment;
        while (true) {
            int i2 = 0;
            int i3 = kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE;
            while (true) {
                if (i2 < i3) {
                    kotlinx.coroutines.channels.ChannelSegment firstSegment2 = firstSegment;
                    long globalCellIndex = (segment.id * ((long) kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE)) + ((long) i2);
                    if (globalCellIndex < s || globalCellIndex < r) {
                        java.lang.Object cellState = segment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(i2);
                        java.lang.Object element = segment.getElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(i2);
                        if (cellState instanceof kotlinx.coroutines.CancellableContinuation) {
                            string = (globalCellIndex >= r || globalCellIndex < s) ? (globalCellIndex >= s || globalCellIndex < r) ? "cont" : "send" : "receive";
                        } else if (cellState instanceof kotlinx.coroutines.selects.SelectInstance) {
                            string = (globalCellIndex >= r || globalCellIndex < s) ? (globalCellIndex >= s || globalCellIndex < r) ? "select" : "onSend" : "onReceive";
                        } else if (cellState instanceof kotlinx.coroutines.channels.ReceiveCatching) {
                            string = "receiveCatching";
                        } else if (cellState instanceof kotlinx.coroutines.channels.BufferedChannel.SendBroadcast) {
                            string = "sendBroadcast";
                        } else if (cellState instanceof kotlinx.coroutines.channels.WaiterEB) {
                            string = "EB(" + cellState + ")";
                        } else if (kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_RCV) ? true : kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.RESUMING_BY_EB)) {
                            string = "resuming_sender";
                        } else if (cellState == null ? true : kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.IN_BUFFER) ? true : kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.DONE_RCV) ? true : kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.POISONED) ? true : kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_RCV) ? true : kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.INTERRUPTED_SEND) ? true : kotlin.jvm.internal.Intrinsics.areEqual(cellState, kotlinx.coroutines.channels.BufferedChannelKt.getCHANNEL_CLOSED())) {
                            i = i3;
                            i2++;
                            firstSegment = firstSegment2;
                            i3 = i;
                        } else {
                            string = cellState.toString();
                        }
                        java.lang.String cellStateString = string;
                        if (element != null) {
                            i = i3;
                            sb.append("(" + cellStateString + "," + element + "),");
                        } else {
                            i = i3;
                            sb.append(cellStateString + ",");
                        }
                        i2++;
                        firstSegment = firstSegment2;
                        i3 = i;
                    }
                } else {
                    kotlinx.coroutines.channels.ChannelSegment firstSegment3 = firstSegment;
                    kotlinx.coroutines.channels.ChannelSegment channelSegment = (kotlinx.coroutines.channels.ChannelSegment) segment.getNext();
                    if (channelSegment != null) {
                        segment = channelSegment;
                        firstSegment = firstSegment3;
                    }
                }
            }
        }
        if (kotlin.text.StringsKt.last(sb) == ',') {
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sb.deleteCharAt(sb.length() - 1), "deleteCharAt(...)");
        }
        sb.append("]");
        java.lang.String string2 = sb.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string2, "toString(...)");
        return string2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final java.lang.String toStringDebug$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        long sendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = getSendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        long receiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = getReceiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        long bufferEndCounter = getBufferEndCounter();
        long value = this.completedExpandBuffersAndPauseFlag.getValue();
        long $this$sendersCloseStatus$iv = this.sendersAndCloseStatus.getValue();
        sb.append("S=" + sendersCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host + ",R=" + receiversCounter$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host + ",B=" + bufferEndCounter + ",B'=" + value + ",C=" + ((int) ($this$sendersCloseStatus$iv >> 60)) + ",");
        long $this$sendersCloseStatus$iv2 = this.sendersAndCloseStatus.getValue();
        int $i$f$getSendersCloseStatus = (int) ($this$sendersCloseStatus$iv2 >> 60);
        switch ($i$f$getSendersCloseStatus) {
            case 1:
                sb.append("CANCELLATION_STARTED,");
                break;
            case 2:
                sb.append("CLOSED,");
                break;
            case 3:
                sb.append("CANCELLED,");
                break;
        }
        sb.append("SEND_SEGM=" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this.sendSegment.getValue()) + ",RCV_SEGM=" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this.receiveSegment.getValue()));
        if (!isRendezvousOrUnlimited()) {
            sb.append(",EB_SEGM=" + kotlinx.coroutines.DebugStringsKt.getHexAddress(this.bufferEndSegment.getValue()));
        }
        sb.append("  ");
        java.lang.Iterable $this$filter$iv = kotlin.collections.CollectionsKt.listOf((java.lang.Object[]) new kotlinx.coroutines.channels.ChannelSegment[]{this.receiveSegment.getValue(), this.sendSegment.getValue(), this.bufferEndSegment.getValue()});
        java.util.ArrayList arrayList = new java.util.ArrayList();
        for (java.lang.Object element$iv$iv : $this$filter$iv) {
            kotlinx.coroutines.channels.ChannelSegment it = (kotlinx.coroutines.channels.ChannelSegment) element$iv$iv;
            if (it != kotlinx.coroutines.channels.BufferedChannelKt.NULL_SEGMENT) {
                arrayList.add(element$iv$iv);
            }
        }
        java.util.ArrayList $this$minBy$iv = arrayList;
        java.util.Iterator iterator$iv = $this$minBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        java.lang.Object minElem$iv = iterator$iv.next();
        if (iterator$iv.hasNext()) {
            kotlinx.coroutines.channels.ChannelSegment it2 = (kotlinx.coroutines.channels.ChannelSegment) minElem$iv;
            long minValue$iv = it2.id;
            do {
                java.lang.Object e$iv = iterator$iv.next();
                kotlinx.coroutines.channels.ChannelSegment it3 = (kotlinx.coroutines.channels.ChannelSegment) e$iv;
                long v$iv = it3.id;
                if (minValue$iv > v$iv) {
                    minElem$iv = e$iv;
                    minValue$iv = v$iv;
                }
            } while (iterator$iv.hasNext());
        }
        kotlinx.coroutines.channels.ChannelSegment firstSegment = (kotlinx.coroutines.channels.ChannelSegment) minElem$iv;
        kotlinx.coroutines.channels.ChannelSegment channelSegment = firstSegment;
        while (true) {
            java.lang.String hexAddress = kotlinx.coroutines.DebugStringsKt.getHexAddress(channelSegment);
            java.lang.String str = channelSegment.isRemoved() ? com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER : "";
            long j = channelSegment.id;
            kotlinx.coroutines.channels.ChannelSegment channelSegment2 = (kotlinx.coroutines.channels.ChannelSegment) channelSegment.getPrev();
            sb.append(hexAddress + "=[" + str + j + ",prev=" + (channelSegment2 != null ? kotlinx.coroutines.DebugStringsKt.getHexAddress(channelSegment2) : null) + ",");
            int i = kotlinx.coroutines.channels.BufferedChannelKt.SEGMENT_SIZE;
            for (int i2 = 0; i2 < i; i2++) {
                int i3 = i2;
                java.lang.Object cellState = channelSegment.getState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(i3);
                java.lang.Object element = channelSegment.getElement$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(i3);
                java.lang.String cellStateString = cellState instanceof kotlinx.coroutines.CancellableContinuation ? "cont" : cellState instanceof kotlinx.coroutines.selects.SelectInstance ? "select" : cellState instanceof kotlinx.coroutines.channels.ReceiveCatching ? "receiveCatching" : cellState instanceof kotlinx.coroutines.channels.BufferedChannel.SendBroadcast ? "send(broadcast)" : cellState instanceof kotlinx.coroutines.channels.WaiterEB ? "EB(" + cellState + ")" : java.lang.String.valueOf(cellState);
                sb.append("[" + i3 + "]=(" + cellStateString + "," + element + "),");
            }
            kotlinx.coroutines.channels.ChannelSegment channelSegment3 = (kotlinx.coroutines.channels.ChannelSegment) channelSegment.getNext();
            sb.append("next=" + (channelSegment3 != null ? kotlinx.coroutines.DebugStringsKt.getHexAddress(channelSegment3) : null) + "]  ");
            kotlinx.coroutines.channels.ChannelSegment channelSegment4 = (kotlinx.coroutines.channels.ChannelSegment) channelSegment.getNext();
            if (channelSegment4 == null) {
                java.lang.String string = sb.toString();
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
                return string;
            }
            channelSegment = channelSegment4;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0116  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void checkSegmentStructureInvariants() {
        /*
            Method dump skipped, instruction units count: 627
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.channels.BufferedChannel.checkSegmentStructureInvariants():void");
    }
}
