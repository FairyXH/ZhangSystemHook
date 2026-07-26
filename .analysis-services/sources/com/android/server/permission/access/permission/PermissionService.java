package com.android.server.permission.access.permission;

/* JADX INFO: compiled from: PermissionService.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u009c\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 ð\u00012\u00020\u0001:\u0006ð\u0001ñ\u0001ò\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J(\u0010/\u001a\u0002002\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000204H\u0016J.\u00106\u001a\u0002072\u0006\u00108\u001a\u0002092\u0006\u0010:\u001a\u0002042\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00130<2\u0006\u00105\u001a\u000204H\u0002J\u0010\u0010=\u001a\u0002072\u0006\u0010>\u001a\u00020?H\u0016J\u0018\u0010@\u001a\u0002002\u0006\u0010A\u001a\u00020B2\u0006\u0010C\u001a\u000200H\u0016J\u0012\u0010D\u001a\u0004\u0018\u00010E2\u0006\u00105\u001a\u000204H\u0016J(\u0010F\u001a\u0002042\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J \u0010H\u001a\u0002042\u0006\u0010I\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u0013H\u0016J/\u0010J\u001a\u0002072\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020N2\u0010\u0010O\u001a\f\u0012\u0006\b\u0001\u0012\u00020\u0013\u0018\u00010PH\u0016¢\u0006\u0002\u0010QJ+\u0010R\u001a\u0002072\b\u0010S\u001a\u0004\u0018\u00010\u00132\u0012\u0010;\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130P\"\u00020\u0013H\u0002¢\u0006\u0002\u0010TJ*\u0010U\u001a\u0002072\u0006\u00105\u001a\u0002042\u0006\u0010V\u001a\u0002002\u0006\u0010W\u001a\u0002002\b\u0010S\u001a\u0004\u0018\u00010\u0013H\u0002J\u0010\u0010X\u001a\u0002002\u0006\u00102\u001a\u00020\u0013H\u0002J\"\u0010Y\u001a\u0014\u0012\u0004\u0012\u000204\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130[0Z2\u0006\u0010\\\u001a\u00020]H\u0002J\u001a\u0010^\u001a\u0014\u0012\u0004\u0012\u00020\u0013\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130`0_H\u0016J\u0016\u0010a\u001a\b\u0012\u0004\u0012\u00020b0<2\u0006\u0010c\u001a\u000204H\u0016J,\u0010d\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020e0_2\u0006\u00101\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u0016\u0010f\u001a\b\u0012\u0004\u0012\u00020B0<2\u0006\u0010g\u001a\u000204H\u0016J\u0016\u0010h\u001a\b\u0012\u0004\u0012\u00020B0<2\u0006\u0010i\u001a\u000204H\u0016J4\u0010j\u001a\u0016\u0012\u0004\u0012\u00020\u0013\u0018\u00010kj\n\u0012\u0004\u0012\u00020\u0013\u0018\u0001`l2\u0006\u00101\u001a\u00020\u00132\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000204H\u0016J4\u0010m\u001a\u0016\u0012\u0004\u0012\u00020\u0013\u0018\u00010kj\n\u0012\u0004\u0012\u00020\u0013\u0018\u0001`l2\u0006\u0010:\u001a\u0002042\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000204H\u0002J\u001b\u0010n\u001a\b\u0012\u0004\u0012\u00020\u00130P2\u0006\u00102\u001a\u00020\u0013H\u0016¢\u0006\u0002\u0010oJ\u0012\u0010p\u001a\u0004\u0018\u00010\u00132\u0006\u00105\u001a\u000204H\u0016J\u0010\u0010q\u001a\u00020r2\u0006\u0010I\u001a\u000204H\u0016J\u001e\u0010s\u001a\b\u0012\u0004\u0012\u00020\u00130`2\u0006\u00101\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u0016\u0010t\u001a\b\u0012\u0004\u0012\u00020\u00130`2\u0006\u00101\u001a\u00020\u0013H\u0016J\u0010\u0010u\u001a\u00020v2\u0006\u0010:\u001a\u000204H\u0016J\u000e\u0010w\u001a\b\u0012\u0004\u0012\u00020x0<H\u0016J(\u0010y\u001a\u0002042\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u0018\u0010z\u001a\u00020r2\u0006\u00102\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u001a\u0010{\u001a\u0004\u0018\u00010b2\u0006\u0010|\u001a\u00020\u00132\u0006\u0010c\u001a\u000204H\u0016J\"\u0010}\u001a\u0004\u0018\u00010B2\u0006\u00102\u001a\u00020\u00132\u0006\u0010c\u001a\u0002042\u0006\u0010~\u001a\u00020\u0013H\u0016J\u0013\u0010\u007f\u001a\u0005\u0018\u00010\u0080\u00012\u0006\u00102\u001a\u00020\u0013H\u0016J'\u0010\u0081\u0001\u001a\b\u0012\u0004\u0012\u00020B0<2\u0015\u0010\u0082\u0001\u001a\u0010\u0012\u0005\u0012\u00030\u0084\u0001\u0012\u0004\u0012\u0002000\u0083\u0001H\u0082\bJ\u0010\u0010\u0085\u0001\u001a\t\u0012\u0005\u0012\u00030\u0086\u00010<H\u0016J)\u0010\u0087\u0001\u001a\u0002072\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u0007\u0010\u0088\u0001\u001a\u000207J)\u0010\u0089\u0001\u001a\u0002002\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u0019\u0010\u008a\u0001\u001a\u0002002\u0006\u00101\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u0011\u0010\u008b\u0001\u001a\u0002002\u0006\u0010I\u001a\u000204H\u0002J\u0011\u0010\u008c\u0001\u001a\u0002002\u0006\u0010I\u001a\u000204H\u0002J\u0011\u0010\u008d\u0001\u001a\u0002002\u0006\u0010I\u001a\u000204H\u0002J\u0019\u0010\u008e\u0001\u001a\u0002002\u0006\u0010I\u001a\u0002042\u0006\u00102\u001a\u00020\u0013H\u0002J\u001a\u0010\u008f\u0001\u001a\u0002072\u0006\u0010I\u001a\u0002042\u0007\u0010\u0090\u0001\u001a\u00020\u0013H\u0002J'\u0010\u0091\u0001\u001a\u0002072\b\u0010\u0092\u0001\u001a\u00030\u0093\u00012\u0007\u0010\u0094\u0001\u001a\u0002002\t\u0010\u0095\u0001\u001a\u0004\u0018\u000109H\u0016J,\u0010\u0096\u0001\u001a\u0002072\u0006\u00108\u001a\u0002092\u0007\u0010\u0097\u0001\u001a\u0002042\b\u0010\u0098\u0001\u001a\u00030\u0099\u00012\u0006\u00105\u001a\u000204H\u0016J\u0011\u0010\u009a\u0001\u001a\u0002072\u0006\u00108\u001a\u000209H\u0016JD\u0010\u009b\u0001\u001a\u0002072\u0006\u00101\u001a\u00020\u00132\u0006\u0010:\u001a\u0002042\b\u0010\u0092\u0001\u001a\u00030\u0093\u00012\b\u00108\u001a\u0004\u0018\u0001092\r\u0010\u009c\u0001\u001a\b\u0012\u0004\u0012\u0002090<2\u0006\u00105\u001a\u000204H\u0016J\u001b\u0010\u009d\u0001\u001a\u0002072\u0007\u0010\u009e\u0001\u001a\u00020\u00132\u0007\u0010\u009f\u0001\u001a\u000200H\u0016J\t\u0010 \u0001\u001a\u000207H\u0016J\u0011\u0010¡\u0001\u001a\u0002072\u0006\u00105\u001a\u000204H\u0016J\u0011\u0010¢\u0001\u001a\u0002072\u0006\u00105\u001a\u000204H\u0016J#\u0010£\u0001\u001a\n\u0012\u0004\u0012\u00020B\u0018\u00010<2\b\u0010|\u001a\u0004\u0018\u00010\u00132\u0006\u0010c\u001a\u000204H\u0016J\t\u0010¤\u0001\u001a\u000207H\u0016J\u0013\u0010¥\u0001\u001a\u0002072\b\u0010¦\u0001\u001a\u00030§\u0001H\u0016J)\u0010¨\u0001\u001a\u0002002\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000204H\u0016J\u0011\u0010©\u0001\u001a\u0002072\u0006\u0010>\u001a\u00020?H\u0016J\u0011\u0010ª\u0001\u001a\u0002072\u0006\u00102\u001a\u00020\u0013H\u0016J\u0019\u0010«\u0001\u001a\u0002072\u0006\u00108\u001a\u0002092\u0006\u00105\u001a\u000204H\u0016J\u0011\u0010¬\u0001\u001a\u0002072\u0006\u00105\u001a\u000204H\u0016J\u0019\u0010\u00ad\u0001\u001a\u0002072\u0006\u00101\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J\u001a\u0010®\u0001\u001a\u0002072\u0007\u0010¯\u0001\u001a\u00020E2\u0006\u00105\u001a\u000204H\u0016J\u0019\u0010°\u0001\u001a\u0002072\u0006\u00101\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J4\u0010±\u0001\u001a\u0002072\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u0002042\t\u0010\u0090\u0001\u001a\u0004\u0018\u00010\u0013H\u0016J8\u0010²\u0001\u001a\u0002002\u0006\u00101\u001a\u00020\u00132\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00130<2\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u0002042\u0007\u0010³\u0001\u001a\u000200H\u0002J7\u0010´\u0001\u001a\u0002072\u0006\u00108\u001a\u0002092\u0006\u0010:\u001a\u0002042\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u00130<2\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u000204H\u0002J\u001a\u0010µ\u0001\u001a\u0002072\u0007\u0010¶\u0001\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J0\u0010·\u0001\u001a\u0002072\b\u0010\u0092\u0001\u001a\u00030\u0093\u00012\u0006\u00105\u001a\u0002042\u0013\u0010¸\u0001\u001a\u000e\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u0002040%H\u0002JJ\u0010¹\u0001\u001a\u0002072\u0006\u00101\u001a\u00020\u00132\u0006\u00105\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0007\u0010º\u0001\u001a\u0002002\t\b\u0002\u0010»\u0001\u001a\u0002002\u000b\b\u0002\u0010¼\u0001\u001a\u0004\u0018\u00010\u0013H\u0002J)\u0010½\u0001\u001a\u0002002\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J%\u0010¾\u0001\u001a\b\u0012\u0004\u0012\u00020x0<2\u0014\u0010¿\u0001\u001a\u000f\u0012\u0004\u0012\u00020\u0013\u0012\u0005\u0012\u00030\u0084\u00010ZH\u0002JD\u0010À\u0001\u001a\u0002072\u0006\u00101\u001a\u00020\u00132\u0006\u00102\u001a\u00020\u00132\u0007\u0010Á\u0001\u001a\u0002042\u0007\u0010Â\u0001\u001a\u0002042\u0007\u0010Ã\u0001\u001a\u0002002\u0006\u0010G\u001a\u00020\u00132\u0006\u00105\u001a\u000204H\u0016J#\u0010Ä\u0001\u001a\u0002072\u0007\u0010Á\u0001\u001a\u0002042\u0007\u0010Â\u0001\u001a\u0002042\u0006\u00105\u001a\u000204H\u0016J)\u0010Å\u0001\u001a\u0003HÆ\u0001\"\u0005\b\u0000\u0010Æ\u00012\u000f\u0010Ç\u0001\u001a\n\u0012\u0005\u0012\u0003HÆ\u00010È\u0001H\u0082\b¢\u0006\u0003\u0010É\u0001J\t\u0010Ê\u0001\u001a\u000207H\u0016J\u0013\u0010Ë\u0001\u001a\u0002072\b\u0010¦\u0001\u001a\u00030§\u0001H\u0016J\u0018\u0010Ì\u0001\u001a\u000204*\u00030Í\u00012\b\u0010Î\u0001\u001a\u00030\u0084\u0001H\u0002J0\u0010Ï\u0001\u001a\u000207*\u00030Ð\u00012\u0006\u0010:\u001a\u0002042\u0006\u0010\\\u001a\u00020]2\u0010\u0010Ñ\u0001\u001a\u000b\u0012\u0004\u0012\u00020\u0013\u0018\u00010Ò\u0001H\u0002J\u0016\u0010Ó\u0001\u001a\u000207*\u00030Ð\u00012\u0006\u0010\\\u001a\u00020]H\u0002J \u0010Ô\u0001\u001a\u000207*\u00030Í\u00012\u0006\u0010A\u001a\u00020B2\b\u0010Î\u0001\u001a\u00030\u0084\u0001H\u0002J \u0010Õ\u0001\u001a\u00030Ö\u0001*\u00030×\u00012\u0007\u0010Ø\u0001\u001a\u0002042\u0006\u00105\u001a\u000204H\u0002J\u0015\u0010Ù\u0001\u001a\u00020b*\u00020b2\u0006\u0010c\u001a\u000204H\u0002J!\u0010Ú\u0001\u001a\u00020B*\u00030\u0084\u00012\u0006\u0010c\u001a\u0002042\t\b\u0002\u0010Û\u0001\u001a\u000204H\u0002J\u0017\u0010Ü\u0001\u001a\u00030\u0084\u0001*\u00030Í\u00012\u0006\u00102\u001a\u00020\u0013H\u0002J\u0019\u0010Ý\u0001\u001a\u0005\u0018\u00010\u0093\u0001*\u00030×\u00012\u0006\u00101\u001a\u00020\u0013H\u0002J.\u0010Þ\u0001\u001a\u000204*\u00030Í\u00012\u0006\u0010:\u001a\u0002042\u0006\u00105\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u0013H\u0002J\u001e\u0010ß\u0001\u001a\u000200*\u00030×\u00012\u0006\u00101\u001a\u00020\u00132\u0006\u0010I\u001a\u000204H\u0002J&\u0010ß\u0001\u001a\u000200*\u00030×\u00012\u0006\u00101\u001a\u00020\u00132\u0006\u00105\u001a\u0002042\u0006\u0010I\u001a\u000204H\u0002J0\u0010à\u0001\u001a\u000200*\u00030Í\u00012\b\u0010\u0092\u0001\u001a\u00030\u0093\u00012\u0006\u00105\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u0013H\u0002J7\u0010á\u0001\u001a\u000200*\u00030Í\u00012\u0006\u0010:\u001a\u0002042\u0006\u00105\u001a\u0002042\u0007\u0010\u0094\u0001\u001a\u0002002\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u0013H\u0002J\u0016\u0010â\u0001\u001a\u000200*\u00030×\u00012\u0006\u0010I\u001a\u000204H\u0002J1\u0010ã\u0001\u001a\u000207*\u00030ä\u00012\b\u0010\u0092\u0001\u001a\u00030\u0093\u00012\u0006\u00105\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0007\u0010º\u0001\u001a\u000200H\u0002J6\u0010å\u0001\u001a\u000200*\u00030ä\u00012\u0006\u0010:\u001a\u0002042\u0006\u00105\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0006\u0010c\u001a\u000204H\u0002J]\u0010¹\u0001\u001a\u000207*\u00030ä\u00012\b\u0010\u0092\u0001\u001a\u00030\u0093\u00012\u0006\u00105\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0007\u0010º\u0001\u001a\u0002002\u0007\u0010æ\u0001\u001a\u0002002\u0007\u0010ç\u0001\u001a\u0002002\u0007\u0010è\u0001\u001a\u0002002\u0007\u0010é\u0001\u001a\u00020\u0013H\u0002Jl\u0010À\u0001\u001a\u000207*\u00030ä\u00012\u0006\u0010:\u001a\u0002042\u0006\u00105\u001a\u0002042\u0006\u00102\u001a\u00020\u00132\u0006\u0010G\u001a\u00020\u00132\u0007\u0010Á\u0001\u001a\u0002042\u0007\u0010Â\u0001\u001a\u0002042\u0007\u0010ê\u0001\u001a\u0002002\u0007\u0010ë\u0001\u001a\u0002002\u0007\u0010ì\u0001\u001a\u0002002\u0007\u0010é\u0001\u001a\u00020\u00132\u0006\u00101\u001a\u00020\u0013H\u0002J\u001f\u0010í\u0001\u001a\u00030Ö\u0001*\u00020\u001b2\u0007\u0010Ø\u0001\u001a\u0002042\u0006\u00105\u001a\u000204H\u0002J,\u0010î\u0001\u001a\u000207*\u00030Ð\u00012\u001b\u0010Ç\u0001\u001a\u0016\u0012\u0005\u0012\u00030Ð\u0001\u0012\u0004\u0012\u0002070\u0083\u0001¢\u0006\u0003\bï\u0001H\u0082\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.¢\u0006\u0002\n\u0000R\u0018\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00130\u00128\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0014\u001a\u00060\u0015R\u00020\u0000X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082\u0004¢\u0006\u0002\n\u0000R$\u0010$\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\u0013\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130&0%8\u0002X\u0083\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020(X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010-\u001a\u0004\u0018\u00010.X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006ó\u0001"}, d2 = {"Lcom/android/server/permission/access/permission/PermissionService;", "Lcom/android/server/pm/permission/PermissionManagerServiceInterface;", com.android.server.am.HostingRecord.HOSTING_TYPE_SERVICE, "Lcom/android/server/permission/access/AccessCheckingService;", "(Lcom/android/server/permission/access/AccessCheckingService;)V", "context", "Landroid/content/Context;", "devicePolicy", "Lcom/android/server/permission/access/permission/DevicePermissionPolicy;", "handler", "Landroid/os/Handler;", "handlerThread", "Landroid/os/HandlerThread;", "isDelayedPermissionBackupFinished", "Landroid/util/SparseBooleanArray;", "metricsLogger", "Lcom/android/internal/logging/MetricsLogger;", "mountedStorageVolumes", "Landroid/util/ArraySet;", "", "onPermissionFlagsChangedListener", "Lcom/android/server/permission/access/permission/PermissionService$OnPermissionFlagsChangedListener;", "onPermissionsChangeListeners", "Lcom/android/server/permission/access/permission/PermissionService$OnPermissionsChangeListeners;", "packageManagerInternal", "Landroid/content/pm/PackageManagerInternal;", "packageManagerLocal", "Lcom/android/server/pm/PackageManagerLocal;", "permissionControllerManager", "Landroid/permission/PermissionControllerManager;", "platformCompat", "Lcom/android/internal/compat/IPlatformCompat;", "policy", "Lcom/android/server/permission/access/permission/AppIdPermissionPolicy;", "storageVolumeLock", "", "storageVolumePackageNames", "Landroid/util/ArrayMap;", "", "systemConfig", "Lcom/android/server/SystemConfig;", "userManagerInternal", "Lcom/android/server/pm/UserManagerInternal;", "userManagerService", "Lcom/android/server/pm/UserManagerService;", "virtualDeviceManagerInternal", "Lcom/android/server/companion/virtual/VirtualDeviceManagerInternal;", "addAllowlistedRestrictedPermission", "", com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME, "permissionName", "allowlistedFlags", "", "userId", "addAllowlistedRestrictedPermissionsUnchecked", "", "androidPackage", "Lcom/android/server/pm/pkg/AndroidPackage;", "appId", "permissionNames", "", "addOnPermissionsChangeListener", "listener", "Landroid/permission/IOnPermissionsChangeListener;", "addPermission", "permissionInfo", "Landroid/content/pm/PermissionInfo;", "async", "backupRuntimePermissions", "", "checkPermission", "deviceId", "checkUidPermission", "uid", "dump", "fd", "Ljava/io/FileDescriptor;", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V", "enforceCallingOrSelfAnyPermission", "message", "(Ljava/lang/String;[Ljava/lang/String;)V", "enforceCallingOrSelfCrossUserPermission", "enforceFullPermission", "enforceShellRestriction", "enforceRestrictedPermission", "getAllAppIdPackageNames", "Lcom/android/server/permission/access/immutable/IndexedMap;", "Lcom/android/server/permission/access/immutable/MutableIndexedSet;", "state", "Lcom/android/server/permission/access/AccessState;", "getAllAppOpPermissionPackages", "", "", "getAllPermissionGroups", "Landroid/content/pm/PermissionGroupInfo;", "flags", "getAllPermissionStates", "Landroid/permission/PermissionManager$PermissionState;", "getAllPermissionsWithProtection", "protection", "getAllPermissionsWithProtectionFlags", "protectionFlags", "getAllowlistedRestrictedPermissions", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "getAllowlistedRestrictedPermissionsUnchecked", "getAppOpPermissionPackages", "(Ljava/lang/String;)[Ljava/lang/String;", "getDefaultPermissionGrantFingerprint", "getGidsForUid", "", "getGrantedPermissions", "getInstalledPermissions", "getLegacyPermissionState", "Lcom/android/server/pm/permission/LegacyPermissionState;", "getLegacyPermissions", "Lcom/android/server/pm/permission/LegacyPermission;", "getPermissionFlags", "getPermissionGids", "getPermissionGroupInfo", "permissionGroupName", "getPermissionInfo", "opPackageName", "getPermissionTEMP", "Lcom/android/server/pm/permission/Permission;", "getPermissionsWithProtectionOrProtectionFlags", "predicate", "Lkotlin/Function1;", "Lcom/android/server/permission/access/permission/Permission;", "getSplitPermissions", "Landroid/content/pm/permission/SplitPermissionInfoParcelable;", "grantRuntimePermission", "initialize", "isPermissionRevokedByPolicy", "isPermissionsReviewRequired", "isRootOrSystemOrShellUid", "isRootOrSystemUid", "isShellUid", "isSystemUidPermissionGranted", "killUid", com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, "onPackageAdded", "packageState", "Lcom/android/server/pm/pkg/PackageState;", "isInstantApp", "oldPackage", "onPackageInstalled", "previousAppId", "params", "Lcom/android/server/pm/permission/PermissionManagerServiceInternal$PackageInstalledParams;", "onPackageRemoved", "onPackageUninstalled", "sharedUserPkgs", "onStorageVolumeMounted", "volumeUuid", "fingerprintChanged", "onSystemReady", "onUserCreated", "onUserRemoved", "queryPermissionsByGroup", "readLegacyPermissionStateTEMP", "readLegacyPermissionsTEMP", "legacyPermissionSettings", "Lcom/android/server/pm/permission/LegacyPermissionSettings;", "removeAllowlistedRestrictedPermission", "removeOnPermissionsChangeListener", "removePermission", "resetRuntimePermissions", "resetRuntimePermissionsForUser", "restoreDelayedRuntimePermissions", "restoreRuntimePermissions", com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP, "revokePostNotificationPermissionWithoutKillForTest", "revokeRuntimePermission", "setAllowlistedRestrictedPermissions", "isAddingPermission", "setAllowlistedRestrictedPermissionsUnchecked", "setDefaultPermissionGrantFingerprint", "fingerprint", "setRequestedPermissionStates", "permissionStates", "setRuntimePermissionGranted", "isGranted", "skipKillUid", "revokeReason", "shouldShowRequestPermissionRationale", "toLegacyPermissions", "permissions", "updatePermissionFlags", "flagMask", "flagValues", "enforceAdjustPolicyPermission", "updatePermissionFlagsForAllApps", "withCorkedPackageInfoCache", "T", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "writeLegacyPermissionStateTEMP", "writeLegacyPermissionsTEMP", "calculatePermissionTreeFootprint", "Lcom/android/server/permission/access/GetStateScope;", "permissionTree", "dumpAppIdState", "Landroid/util/IndentingPrintWriter;", com.android.server.storage.DiskStatsFileLogger.PACKAGE_NAMES_KEY, "Lcom/android/server/permission/access/immutable/IndexedSet;", "dumpSystemState", "enforcePermissionTreeSize", "filtered", "Lcom/android/server/pm/PackageManagerLocal$FilteredSnapshot;", "Lcom/android/server/pm/PackageManagerLocal$UnfilteredSnapshot;", "callingUid", "generatePermissionGroupInfo", "generatePermissionInfo", "targetSdkVersion", "getAndEnforcePermissionTree", "getPackageState", "getPermissionFlagsWithPolicy", "isPackageVisibleToUid", "isPermissionGranted", "isSinglePermissionGranted", "isUidInstantApp", "setAppOpPermissionGranted", "Lcom/android/server/permission/access/MutateStateScope;", "setPermissionFlagsWithPolicy", "canManageRolePermission", "overridePolicyFixed", "reportError", "methodName", "canUpdateSystemFlags", "reportErrorForUnknownPermission", "isPermissionRequested", "withFilteredSnapshot", "withIndent", "Lkotlin/ExtensionFunctionType;", "Companion", "OnPermissionFlagsChangedListener", "OnPermissionsChangeListeners", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PermissionService implements com.android.server.pm.permission.PermissionManagerServiceInterface {
    private static final long BACKUP_TIMEOUT_MILLIS;
    private static final android.util.ArrayMap<java.lang.String, java.lang.String> FULLER_PERMISSIONS;
    private static final int MAX_PERMISSION_TREE_FOOTPRINT = 32768;
    private static final android.util.ArraySet<java.lang.String> NOTIFICATIONS_PERMISSIONS;
    private static final int PERMISSION_ALLOWLIST_MASK = 7;
    private static final int REVIEW_REQUIRED_FLAGS = 5120;
    private static final int UNREQUESTABLE_MASK = 262592;
    private final android.content.Context context;
    private final com.android.server.permission.access.permission.DevicePermissionPolicy devicePolicy;
    private android.os.Handler handler;
    private android.os.HandlerThread handlerThread;
    private final android.util.SparseBooleanArray isDelayedPermissionBackupFinished;
    private com.android.internal.logging.MetricsLogger metricsLogger;
    private final android.util.ArraySet<java.lang.String> mountedStorageVolumes;
    private com.android.server.permission.access.permission.PermissionService.OnPermissionFlagsChangedListener onPermissionFlagsChangedListener;
    private com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners onPermissionsChangeListeners;
    private android.content.pm.PackageManagerInternal packageManagerInternal;
    private com.android.server.pm.PackageManagerLocal packageManagerLocal;
    private android.permission.PermissionControllerManager permissionControllerManager;
    private com.android.internal.compat.IPlatformCompat platformCompat;
    private final com.android.server.permission.access.permission.AppIdPermissionPolicy policy;
    private final com.android.server.permission.access.AccessCheckingService service;
    private final java.lang.Object storageVolumeLock;
    private final android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> storageVolumePackageNames;
    private com.android.server.SystemConfig systemConfig;
    private com.android.server.pm.UserManagerInternal userManagerInternal;
    private com.android.server.pm.UserManagerService userManagerService;
    private com.android.server.companion.virtual.VirtualDeviceManagerInternal virtualDeviceManagerInternal;
    public static final com.android.server.permission.access.permission.PermissionService.Companion Companion = new com.android.server.permission.access.permission.PermissionService.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.permission.PermissionService.class.getSimpleName();
    private static final long BACKGROUND_RATIONALE_CHANGE_ID = 147316723;

    public PermissionService(com.android.server.permission.access.AccessCheckingService service) {
        this.service = service;
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("uid", com.android.server.permission.access.PermissionUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar, "null cannot be cast to non-null type com.android.server.permission.access.permission.AppIdPermissionPolicy");
        this.policy = (com.android.server.permission.access.permission.AppIdPermissionPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar;
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2 = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("uid", com.android.server.permission.access.DevicePermissionUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2, "null cannot be cast to non-null type com.android.server.permission.access.permission.DevicePermissionPolicy");
        this.devicePolicy = (com.android.server.permission.access.permission.DevicePermissionPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2;
        this.context = this.service.getContext();
        this.storageVolumeLock = new java.lang.Object();
        this.mountedStorageVolumes = new android.util.ArraySet<>();
        this.storageVolumePackageNames = new android.util.ArrayMap<>();
        this.isDelayedPermissionBackupFinished = new android.util.SparseBooleanArray();
    }

    public final void initialize() {
        this.metricsLogger = new com.android.internal.logging.MetricsLogger();
        this.packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.packageManagerLocal = (com.android.server.pm.PackageManagerLocal) com.android.server.LocalManagerRegistry.getManagerOrThrow(com.android.server.pm.PackageManagerLocal.class);
        this.platformCompat = com.android.internal.compat.IPlatformCompat.Stub.asInterface(android.os.ServiceManager.getService("platform_compat"));
        this.systemConfig = com.android.server.SystemConfig.getInstance();
        this.userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        this.userManagerService = com.android.server.pm.UserManagerService.getInstance();
        android.content.pm.PackageManager.invalidatePackageInfoCache();
        android.permission.PermissionManager.disablePackageNamePermissionCache();
        com.android.server.ServiceThread $this$initialize_u24lambda_u240 = new com.android.server.ServiceThread(LOG_TAG, 10, true);
        $this$initialize_u24lambda_u240.start();
        this.handlerThread = $this$initialize_u24lambda_u240;
        android.os.HandlerThread handlerThread = this.handlerThread;
        com.android.server.permission.access.permission.PermissionService.OnPermissionFlagsChangedListener onPermissionFlagsChangedListener = null;
        if (handlerThread == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("handlerThread");
            handlerThread = null;
        }
        this.handler = new android.os.Handler(handlerThread.getLooper());
        this.onPermissionsChangeListeners = new com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners(com.android.server.FgThread.get().getLooper());
        this.onPermissionFlagsChangedListener = new com.android.server.permission.access.permission.PermissionService.OnPermissionFlagsChangedListener();
        com.android.server.permission.access.permission.AppIdPermissionPolicy appIdPermissionPolicy = this.policy;
        com.android.server.permission.access.permission.PermissionService.OnPermissionFlagsChangedListener onPermissionFlagsChangedListener2 = this.onPermissionFlagsChangedListener;
        if (onPermissionFlagsChangedListener2 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("onPermissionFlagsChangedListener");
            onPermissionFlagsChangedListener2 = null;
        }
        appIdPermissionPolicy.addOnPermissionFlagsChangedListener(onPermissionFlagsChangedListener2);
        com.android.server.permission.access.permission.DevicePermissionPolicy devicePermissionPolicy = this.devicePolicy;
        com.android.server.permission.access.permission.PermissionService.OnPermissionFlagsChangedListener onPermissionFlagsChangedListener3 = this.onPermissionFlagsChangedListener;
        if (onPermissionFlagsChangedListener3 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("onPermissionFlagsChangedListener");
        } else {
            onPermissionFlagsChangedListener = onPermissionFlagsChangedListener3;
        }
        devicePermissionPolicy.addOnPermissionFlagsChangedListener(onPermissionFlagsChangedListener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionGroupInfo> getAllPermissionGroups(int flags) throws java.lang.Exception {
        android.content.pm.PermissionGroupInfo permissionGroupInfoGeneratePermissionGroupInfo;
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = unfilteredSnapshotWithUnfilteredSnapshot;
            int i = 0;
            int callingUid = android.os.Binder.getCallingUid();
            if (isUidInstantApp(snapshot, callingUid)) {
                java.util.List<android.content.pm.PermissionGroupInfo> listEmptyList = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.emptyList();
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                return listEmptyList;
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState = this_$iv.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            com.android.server.permission.access.GetStateScope $this$getAllPermissionGroups_u24lambda_u244_u24lambda_u242 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getAllPermissionGroups_u24lambda_u244_u24lambda_u242_u24lambda_u241 = this.policy;
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo> permissionGroups = $this$getAllPermissionGroups_u24lambda_u244_u24lambda_u242_u24lambda_u241.getPermissionGroups($this$getAllPermissionGroups_u24lambda_u244_u24lambda_u242);
            java.util.Collection destination$iv = new java.util.ArrayList();
            int index$iv$iv = 0;
            int size = permissionGroups.getSize();
            while (index$iv$iv < size) {
                java.lang.Object key$iv = permissionGroups.keyAt(index$iv$iv);
                java.lang.Object value$iv = permissionGroups.valueAt(index$iv$iv);
                android.content.pm.PermissionGroupInfo permissionGroup = (android.content.pm.PermissionGroupInfo) value$iv;
                int i2 = i;
                if (isPackageVisibleToUid(snapshot, permissionGroup.packageName, callingUid)) {
                    try {
                        permissionGroupInfoGeneratePermissionGroupInfo = generatePermissionGroupInfo(permissionGroup, flags);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        java.lang.Throwable th2 = th;
                        try {
                            throw th2;
                        } catch (java.lang.Throwable th3) {
                            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, th2);
                            throw th3;
                        }
                    }
                } else {
                    permissionGroupInfoGeneratePermissionGroupInfo = null;
                }
                if (permissionGroupInfoGeneratePermissionGroupInfo != null) {
                    destination$iv.add(permissionGroupInfoGeneratePermissionGroupInfo);
                }
                index$iv$iv++;
                i = i2;
            }
            java.util.ArrayList arrayList = (java.util.List) destination$iv;
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            return arrayList;
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v2, types: [T, android.content.pm.PermissionGroupInfo] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionGroupInfo getPermissionGroupInfo(java.lang.String permissionGroupName, int flags) throws java.lang.Exception {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef permissionGroup = new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef();
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = unfilteredSnapshotWithUnfilteredSnapshot;
            int callingUid = android.os.Binder.getCallingUid();
            if (isUidInstantApp(snapshot, callingUid)) {
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                return null;
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState = this_$iv.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            com.android.server.permission.access.GetStateScope $this$getPermissionGroupInfo_u24lambda_u247_u24lambda_u246 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionGroupInfo_u24lambda_u247_u24lambda_u246_u24lambda_u245 = this.policy;
            android.content.pm.PermissionGroupInfo permissionGroupInfo = $this$getPermissionGroupInfo_u24lambda_u247_u24lambda_u246_u24lambda_u245.getPermissionGroups($this$getPermissionGroupInfo_u24lambda_u247_u24lambda_u246).get(permissionGroupName);
            if (permissionGroupInfo == 0) {
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                return null;
            }
            permissionGroup.element = permissionGroupInfo;
            if (!isPackageVisibleToUid(snapshot, ((android.content.pm.PermissionGroupInfo) permissionGroup.element).packageName, callingUid)) {
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                return null;
            }
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            return generatePermissionGroupInfo((android.content.pm.PermissionGroupInfo) permissionGroup.element, flags);
        } finally {
        }
    }

    private final android.content.pm.PermissionGroupInfo generatePermissionGroupInfo(android.content.pm.PermissionGroupInfo $this$generatePermissionGroupInfo, int flags) {
        android.content.pm.PermissionGroupInfo $this$generatePermissionGroupInfo_u24lambda_u248 = new android.content.pm.PermissionGroupInfo($this$generatePermissionGroupInfo);
        if (!com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 128)) {
            $this$generatePermissionGroupInfo_u24lambda_u248.metaData = null;
        }
        return $this$generatePermissionGroupInfo_u24lambda_u248;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v2, types: [T, com.android.server.permission.access.permission.Permission] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public android.content.pm.PermissionInfo getPermissionInfo(java.lang.String permissionName, int flags, java.lang.String opPackageName) throws java.lang.Exception {
        java.lang.Throwable th;
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot;
        int callingUid;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef permission = new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef();
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            snapshot = unfilteredSnapshotWithUnfilteredSnapshot;
            callingUid = android.os.Binder.getCallingUid();
        } catch (java.lang.Throwable th2) {
            th = th2;
        }
        try {
            if (isUidInstantApp(snapshot, callingUid)) {
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                return null;
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState = this_$iv.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            com.android.server.permission.access.GetStateScope $this$getPermissionInfo_u24lambda_u2411_u24lambda_u2410 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionInfo_u24lambda_u2411_u24lambda_u2410_u24lambda_u249 = this.policy;
            try {
                com.android.server.permission.access.permission.Permission permission2 = $this$getPermissionInfo_u24lambda_u2411_u24lambda_u2410_u24lambda_u249.getPermissions($this$getPermissionInfo_u24lambda_u2411_u24lambda_u2410).get(permissionName);
                if (permission2 == 0) {
                    com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                    return null;
                }
                permission.element = permission2;
                com.android.server.permission.access.permission.Permission this_$iv2 = (com.android.server.permission.access.permission.Permission) permission.element;
                if (!isPackageVisibleToUid(snapshot, this_$iv2.getPermissionInfo().packageName, callingUid)) {
                    com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                    return null;
                }
                try {
                    com.android.server.pm.pkg.PackageState packageState = getPackageState(snapshot, opPackageName);
                    com.android.server.pm.pkg.AndroidPackage opPackage = packageState != null ? packageState.getAndroidPackage() : null;
                    int targetSdkVersion = 10000;
                    if (!isRootOrSystemOrShellUid(callingUid) && opPackage != null) {
                        targetSdkVersion = opPackage.getTargetSdkVersion();
                    }
                    int targetSdkVersion2 = targetSdkVersion;
                    com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                    com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
                    return generatePermissionInfo((com.android.server.permission.access.permission.Permission) permission.element, flags, targetSdkVersion2);
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
            th = th;
            throw th;
            throw th;
        } catch (java.lang.Throwable th5) {
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, th);
            throw th5;
        }
        th = th;
    }

    static /* synthetic */ android.content.pm.PermissionInfo generatePermissionInfo$default(com.android.server.permission.access.permission.PermissionService permissionService, com.android.server.permission.access.permission.Permission permission, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 10000;
        }
        return permissionService.generatePermissionInfo(permission, i, i2);
    }

    private final android.content.pm.PermissionInfo generatePermissionInfo(com.android.server.permission.access.permission.Permission $this$generatePermissionInfo, int flags, int targetSdkVersion) {
        int protection;
        android.content.pm.PermissionInfo $this$generatePermissionInfo_u24lambda_u2412 = new android.content.pm.PermissionInfo($this$generatePermissionInfo.getPermissionInfo());
        $this$generatePermissionInfo_u24lambda_u2412.flags |= 1073741824;
        if (!com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 128)) {
            $this$generatePermissionInfo_u24lambda_u2412.metaData = null;
        }
        if (targetSdkVersion < 26 && (protection = $this$generatePermissionInfo_u24lambda_u2412.getProtection()) != 2) {
            $this$generatePermissionInfo_u24lambda_u2412.protectionLevel = protection;
        }
        return $this$generatePermissionInfo_u24lambda_u2412;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00e6 A[Catch: all -> 0x00ff, TryCatch #1 {all -> 0x00ff, blocks: (B:6:0x0017, B:10:0x002b, B:12:0x0037, B:13:0x003e, B:15:0x0044, B:20:0x005a, B:24:0x0067, B:26:0x0087, B:28:0x00b1, B:30:0x00bf, B:36:0x00e6, B:37:0x00ea, B:38:0x00f2), top: B:49:0x0017 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00ea A[SYNTHETIC] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.List<android.content.pm.PermissionInfo> queryPermissionsByGroup(java.lang.String r27, int r28) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 264
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.PermissionService.queryPermissionsByGroup(java.lang.String, int):java.util.List");
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtection(int protection) {
        int index$iv$iv$iv;
        android.content.pm.PermissionInfo permissionInfoGeneratePermissionInfo$default;
        com.android.server.permission.access.AccessCheckingService this_$iv$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421$iv = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421_u24lambda_u2420$iv = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421_u24lambda_u2420$iv.getPermissions($this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421$iv);
        java.util.Collection destination$iv$iv = new java.util.ArrayList();
        int size = permissions.getSize();
        int index$iv$iv$iv2 = 0;
        while (index$iv$iv$iv2 < size) {
            java.lang.Object key$iv$iv = permissions.keyAt(index$iv$iv$iv2);
            java.lang.Object value$iv$iv = permissions.valueAt(index$iv$iv$iv2);
            com.android.server.permission.access.permission.Permission permission$iv = (com.android.server.permission.access.permission.Permission) value$iv$iv;
            if (permission$iv.getPermissionInfo().getProtection() == protection) {
                index$iv$iv$iv = index$iv$iv$iv2;
                permissionInfoGeneratePermissionInfo$default = generatePermissionInfo$default(this, permission$iv, 0, 0, 2, null);
            } else {
                index$iv$iv$iv = index$iv$iv$iv2;
                permissionInfoGeneratePermissionInfo$default = null;
            }
            if (permissionInfoGeneratePermissionInfo$default != null) {
                destination$iv$iv.add(permissionInfoGeneratePermissionInfo$default);
            }
            index$iv$iv$iv2 = index$iv$iv$iv + 1;
        }
        return (java.util.List) destination$iv$iv;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.PermissionInfo> getAllPermissionsWithProtectionFlags(int protectionFlags) {
        int index$iv$iv$iv;
        android.content.pm.PermissionInfo permissionInfoGeneratePermissionInfo$default;
        com.android.server.permission.access.AccessCheckingService this_$iv$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421$iv = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421_u24lambda_u2420$iv = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421_u24lambda_u2420$iv.getPermissions($this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421$iv);
        java.util.Collection destination$iv$iv = new java.util.ArrayList();
        int size = permissions.getSize();
        int index$iv$iv$iv2 = 0;
        while (index$iv$iv$iv2 < size) {
            java.lang.Object key$iv$iv = permissions.keyAt(index$iv$iv$iv2);
            java.lang.Object value$iv$iv = permissions.valueAt(index$iv$iv$iv2);
            com.android.server.permission.access.permission.Permission permission$iv = (com.android.server.permission.access.permission.Permission) value$iv$iv;
            if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission$iv.getPermissionInfo().getProtectionFlags(), protectionFlags)) {
                index$iv$iv$iv = index$iv$iv$iv2;
                permissionInfoGeneratePermissionInfo$default = generatePermissionInfo$default(this, permission$iv, 0, 0, 2, null);
            } else {
                index$iv$iv$iv = index$iv$iv$iv2;
                permissionInfoGeneratePermissionInfo$default = null;
            }
            if (permissionInfoGeneratePermissionInfo$default != null) {
                destination$iv$iv.add(permissionInfoGeneratePermissionInfo$default);
            }
            index$iv$iv$iv2 = index$iv$iv$iv + 1;
        }
        return (java.util.List) destination$iv$iv;
    }

    private final java.util.List<android.content.pm.PermissionInfo> getPermissionsWithProtectionOrProtectionFlags(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.access.permission.Permission, java.lang.Boolean> function1) {
        int index$iv$iv;
        android.content.pm.PermissionInfo permissionInfoGeneratePermissionInfo$default;
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421_u24lambda_u2420 = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421_u24lambda_u2420.getPermissions($this$getPermissionsWithProtectionOrProtectionFlags_u24lambda_u2421);
        java.util.Collection destination$iv = new java.util.ArrayList();
        int size = permissions.getSize();
        int index$iv$iv2 = 0;
        while (index$iv$iv2 < size) {
            java.lang.Object key$iv = permissions.keyAt(index$iv$iv2);
            java.lang.Object value$iv = permissions.valueAt(index$iv$iv2);
            com.android.server.permission.access.permission.Permission permission = (com.android.server.permission.access.permission.Permission) value$iv;
            if (function1.invoke(permission).booleanValue()) {
                index$iv$iv = index$iv$iv2;
                permissionInfoGeneratePermissionInfo$default = generatePermissionInfo$default(this, permission, 0, 0, 2, null);
            } else {
                index$iv$iv = index$iv$iv2;
                permissionInfoGeneratePermissionInfo$default = null;
            }
            if (permissionInfoGeneratePermissionInfo$default != null) {
                destination$iv.add(permissionInfoGeneratePermissionInfo$default);
            }
            index$iv$iv2 = index$iv$iv + 1;
        }
        return (java.util.List) destination$iv;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getPermissionGids(java.lang.String permissionName, int userId) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getPermissionGids_u24lambda_u2424 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionGids_u24lambda_u2424_u24lambda_u2423 = this.policy;
        com.android.server.permission.access.permission.Permission permission = $this$getPermissionGids_u24lambda_u2424_u24lambda_u2423.getPermissions($this$getPermissionGids_u24lambda_u2424).get(permissionName);
        return permission == null ? libcore.util.EmptyArray.INT : permission.getGidsForUser(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getInstalledPermissions(java.lang.String packageName) {
        if (packageName == null) {
            throw new java.lang.IllegalArgumentException("packageName cannot be null".toString());
        }
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getInstalledPermissions_u24lambda_u2427 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getInstalledPermissions_u24lambda_u2427_u24lambda_u2426 = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$getInstalledPermissions_u24lambda_u2427_u24lambda_u2426.getPermissions($this$getInstalledPermissions_u24lambda_u2427);
        java.util.Collection destination$iv = new android.util.ArraySet();
        int size = permissions.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            java.lang.Object key$iv = permissions.keyAt(index$iv$iv);
            java.lang.Object value$iv = permissions.valueAt(index$iv$iv);
            com.android.server.permission.access.permission.Permission permission = (com.android.server.permission.access.permission.Permission) value$iv;
            java.lang.String str = com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(permission.getPermissionInfo().packageName, packageName) ? permission.getPermissionInfo().name : null;
            if (str != null) {
                destination$iv.add(str);
            }
        }
        return (java.util.Set) destination$iv;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v6, types: [T, com.android.server.permission.access.permission.Permission] */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addPermission(android.content.pm.PermissionInfo permissionInfo, boolean async) throws java.lang.Exception {
        java.lang.String permissionName = permissionInfo.name;
        if (permissionName == null) {
            throw new java.lang.IllegalArgumentException("permissionName cannot be null".toString());
        }
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState accessState = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot it = unfilteredSnapshotWithUnfilteredSnapshot;
            boolean zIsUidInstantApp = isUidInstantApp(it, callingUid);
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            if (zIsUidInstantApp) {
                throw new java.lang.SecurityException("Instant apps cannot add permissions");
            }
            if (permissionInfo.labelRes == 0 && permissionInfo.nonLocalizedLabel == null) {
                throw new java.lang.SecurityException("Label must be specified in permission");
            }
            com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef oldPermission = new com.android.server.permission.jarjar.kotlin.jvm.internal.Ref.ObjectRef();
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            synchronized (this_$iv.stateLock) {
                com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
                if (accessState2 == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                com.android.server.permission.access.AccessState oldState$iv = accessState;
                com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
                com.android.server.permission.access.MutateStateScope $this$addPermission_u24lambda_u2433 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
                com.android.server.permission.access.permission.Permission permissionTree = getAndEnforcePermissionTree($this$addPermission_u24lambda_u2433, permissionName);
                enforcePermissionTreeSize($this$addPermission_u24lambda_u2433, permissionInfo, permissionTree);
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$addPermission_u24lambda_u2433_u24lambda_u2431 = this.policy;
                oldPermission.element = $this$addPermission_u24lambda_u2433_u24lambda_u2431.getPermissions($this$addPermission_u24lambda_u2433).get(permissionName);
                if (oldPermission.element != 0) {
                    if (!(((com.android.server.permission.access.permission.Permission) oldPermission.element).getType() == 2)) {
                        throw new java.lang.SecurityException("Not allowed to modify non-dynamic permission " + permissionName);
                    }
                }
                permissionInfo.packageName = permissionTree.getPermissionInfo().packageName;
                permissionInfo.protectionLevel = android.content.pm.PermissionInfo.fixProtectionLevel(permissionInfo.protectionLevel);
                com.android.server.permission.access.permission.Permission newPermission = new com.android.server.permission.access.permission.Permission(permissionInfo, true, 2, permissionTree.getAppId(), null, false, 48, null);
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$addPermission_u24lambda_u2433_u24lambda_u2432 = this.policy;
                $this$addPermission_u24lambda_u2433_u24lambda_u2432.addPermission($this$addPermission_u24lambda_u2433, newPermission, !async);
                this_$iv.persistence.write(newState$iv);
                this_$iv.state = newState$iv;
                com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
                $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            }
            return oldPermission.element == 0;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, th);
                throw th2;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removePermission(java.lang.String permissionName) throws java.lang.Exception {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState oldState$iv = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot it = unfilteredSnapshotWithUnfilteredSnapshot;
            boolean zIsUidInstantApp = isUidInstantApp(it, callingUid);
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            if (zIsUidInstantApp) {
                throw new java.lang.SecurityException("Instant applications don't have access to this method");
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            synchronized (this_$iv.stateLock) {
                com.android.server.permission.access.AccessState accessState = this_$iv.state;
                if (accessState == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    oldState$iv = accessState;
                }
                com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
                com.android.server.permission.access.MutateStateScope $this$removePermission_u24lambda_u2437 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
                getAndEnforcePermissionTree($this$removePermission_u24lambda_u2437, permissionName);
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$removePermission_u24lambda_u2437_u24lambda_u2435 = this.policy;
                com.android.server.permission.access.permission.Permission permission = $this$removePermission_u24lambda_u2437_u24lambda_u2435.getPermissions($this$removePermission_u24lambda_u2437).get(permissionName);
                if (permission != null) {
                    if (permission.getType() == 2) {
                        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$removePermission_u24lambda_u2437_u24lambda_u2436 = this.policy;
                        $this$removePermission_u24lambda_u2437_u24lambda_u2436.removePermission($this$removePermission_u24lambda_u2437, permission);
                    } else {
                        throw new java.lang.SecurityException("Not allowed to modify non-dynamic permission " + permissionName);
                    }
                }
                this_$iv.persistence.write(newState$iv);
                this_$iv.state = newState$iv;
                com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
                $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            }
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.android.server.permission.access.permission.Permission getAndEnforcePermissionTree(com.android.server.permission.access.GetStateScope $this$getAndEnforcePermissionTree, java.lang.String permissionName) {
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getAndEnforcePermissionTree_u24lambda_u2438 = this.policy;
        com.android.server.permission.access.permission.Permission permissionTree = $this$getAndEnforcePermissionTree_u24lambda_u2438.findPermissionTree($this$getAndEnforcePermissionTree, permissionName);
        if (permissionTree != null && permissionTree.getAppId() == android.os.UserHandle.getAppId(callingUid)) {
            return permissionTree;
        }
        throw new java.lang.SecurityException("Calling UID " + callingUid + " is not allowed to add to or remove from the permission tree");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void enforcePermissionTreeSize(com.android.server.permission.access.GetStateScope $this$enforcePermissionTreeSize, android.content.pm.PermissionInfo permissionInfo, com.android.server.permission.access.permission.Permission permissionTree) {
        if (permissionTree.getAppId() != 1000) {
            int permissionTreeFootprint = calculatePermissionTreeFootprint($this$enforcePermissionTreeSize, permissionTree);
            if (permissionInfo.calculateFootprint() + permissionTreeFootprint > 32768) {
                throw new java.lang.SecurityException("Permission tree size cap exceeded");
            }
        }
    }

    private final int calculatePermissionTreeFootprint(com.android.server.permission.access.GetStateScope $this$calculatePermissionTreeFootprint, com.android.server.permission.access.permission.Permission permissionTree) {
        int size = 0;
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$calculatePermissionTreeFootprint_u24lambda_u2440 = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$calculatePermissionTreeFootprint_u24lambda_u2440.getPermissions($this$calculatePermissionTreeFootprint);
        int size2 = permissions.getSize();
        for (int index$iv = 0; index$iv < size2; index$iv++) {
            permissions.keyAt(index$iv);
            com.android.server.permission.access.permission.Permission permission = permissions.valueAt(index$iv);
            if (permissionTree.getAppId() == permission.getAppId()) {
                size += permission.getPermissionInfo().name.length() + permission.getPermissionInfo().calculateFootprint();
            }
        }
        return size;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkUidPermission(int uid, java.lang.String permissionName, java.lang.String deviceId) {
        int userId = android.os.UserHandle.getUserId(uid);
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        com.android.server.permission.access.AccessState accessState = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(userId)) {
            return -1;
        }
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageManagerInternal.getPackage(uid);
        if (androidPackage != null) {
            android.content.pm.PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
            if (packageManagerInternal2 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                packageManagerInternal2 = null;
            }
            com.android.server.pm.pkg.PackageStateInternal packageState = packageManagerInternal2.getPackageStateInternal(androidPackage.getPackageName());
            if (packageState == null) {
                android.util.Slog.e(LOG_TAG, "checkUidPermission: PackageState not found for AndroidPackage " + androidPackage);
                return -1;
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
            if (accessState2 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            com.android.server.permission.access.GetStateScope $this$checkUidPermission_u24lambda_u2441 = new com.android.server.permission.access.GetStateScope(accessState);
            boolean isPermissionGranted = isPermissionGranted($this$checkUidPermission_u24lambda_u2441, packageState, userId, permissionName, deviceId);
            return isPermissionGranted ? 0 : -1;
        }
        boolean isPermissionGranted2 = isSystemUidPermissionGranted(uid, permissionName);
        return isPermissionGranted2 ? 0 : -1;
    }

    private final boolean isSystemUidPermissionGranted(int uid, java.lang.String permissionName) {
        com.android.server.SystemConfig systemConfig = this.systemConfig;
        if (systemConfig == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig = null;
        }
        android.util.ArraySet<java.lang.String> arraySet = systemConfig.getSystemPermissions().get(uid);
        if (arraySet == null) {
            return false;
        }
        if (arraySet.contains(permissionName)) {
            return true;
        }
        java.lang.String fullerPermissionName = FULLER_PERMISSIONS.get(permissionName);
        return fullerPermissionName != null && arraySet.contains(fullerPermissionName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int checkPermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String deviceId, int userId) throws java.lang.Exception {
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        com.android.server.permission.access.AccessState accessState = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(userId)) {
            return -1;
        }
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, android.os.Binder.getCallingUid(), userId);
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotWithFilteredSnapshot;
            com.android.server.pm.pkg.PackageState packageState = it.getPackageState(packageName);
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
            if (packageState == null) {
                return -1;
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
            if (accessState2 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            com.android.server.permission.access.GetStateScope $this$checkPermission_u24lambda_u2443 = new com.android.server.permission.access.GetStateScope(accessState);
            boolean isPermissionGranted = isPermissionGranted($this$checkPermission_u24lambda_u2443, packageState, userId, permissionName, deviceId);
            return isPermissionGranted ? 0 : -1;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, th);
                throw th2;
            }
        }
    }

    private final boolean isPermissionGranted(com.android.server.permission.access.GetStateScope $this$isPermissionGranted, com.android.server.pm.pkg.PackageState packageState, int userId, java.lang.String permissionName, java.lang.String deviceId) {
        int appId = packageState.getAppId();
        boolean isInstantApp = packageState.getUserStateOrDefault(userId).isInstantApp();
        if (isSinglePermissionGranted($this$isPermissionGranted, appId, userId, isInstantApp, permissionName, deviceId)) {
            return true;
        }
        java.lang.String fullerPermissionName = FULLER_PERMISSIONS.get(permissionName);
        return fullerPermissionName != null && isSinglePermissionGranted($this$isPermissionGranted, appId, userId, isInstantApp, fullerPermissionName, deviceId);
    }

    private final boolean isSinglePermissionGranted(com.android.server.permission.access.GetStateScope $this$isSinglePermissionGranted, int appId, int userId, boolean isInstantApp, java.lang.String permissionName, java.lang.String deviceId) {
        int flags = getPermissionFlagsWithPolicy($this$isSinglePermissionGranted, appId, userId, permissionName, deviceId);
        if (!com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(flags)) {
            return false;
        }
        if (isInstantApp) {
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$isSinglePermissionGranted_u24lambda_u2444 = this.policy;
            com.android.server.permission.access.permission.Permission permission = $this$isSinglePermissionGranted_u24lambda_u2444.getPermissions($this$isSinglePermissionGranted).get(permissionName);
            return permission != null && com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 4096);
        }
        return true;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Set<java.lang.String> getGrantedPermissions(java.lang.String packageName, int userId) throws java.lang.Exception {
        if (packageName == null) {
            throw new java.lang.IllegalArgumentException("packageName cannot be null".toString());
        }
        com.android.internal.util.Preconditions.checkArgumentNonnegative(userId, "userId");
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot it = unfilteredSnapshotWithUnfilteredSnapshot;
            com.android.server.pm.pkg.PackageState packageState = getPackageState(it, packageName);
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            if (packageState == null) {
                android.util.Slog.w(LOG_TAG, "getGrantedPermissions: Unknown package " + packageName);
                return com.android.server.permission.jarjar.kotlin.collections.SetsKt.emptySet();
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState = this_$iv.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            com.android.server.permission.access.GetStateScope $this$getGrantedPermissions_u24lambda_u2449 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getGrantedPermissions_u24lambda_u2449_u24lambda_u2447 = this.policy;
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> uidPermissionFlags = $this$getGrantedPermissions_u24lambda_u2449_u24lambda_u2447.getUidPermissionFlags($this$getGrantedPermissions_u24lambda_u2449, packageState.getAppId(), userId);
            if (uidPermissionFlags == null) {
                return com.android.server.permission.jarjar.kotlin.collections.SetsKt.emptySet();
            }
            java.util.Collection destination$iv = new android.util.ArraySet();
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap = uidPermissionFlags;
            int size = indexedMap.getSize();
            int index$iv$iv = 0;
            while (index$iv$iv < size) {
                java.lang.Object key$iv = indexedMap.keyAt(index$iv$iv);
                java.lang.Object value$iv = indexedMap.valueAt(index$iv$iv);
                ((java.lang.Number) value$iv).intValue();
                java.lang.String permissionName = (java.lang.String) key$iv;
                int index$iv$iv2 = index$iv$iv;
                int i = size;
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap2 = indexedMap;
                java.util.Collection destination$iv2 = destination$iv;
                java.lang.String str = isPermissionGranted($this$getGrantedPermissions_u24lambda_u2449, packageState, userId, permissionName, "default:0") ? permissionName : null;
                if (str != null) {
                    destination$iv2.add(str);
                }
                index$iv$iv = index$iv$iv2 + 1;
                destination$iv = destination$iv2;
                size = i;
                indexedMap = indexedMap2;
            }
            return (java.util.Set) destination$iv;
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int[] getGidsForUid(int uid) {
        com.android.server.permission.access.permission.PermissionService permissionService = this;
        int appId = android.os.UserHandle.getAppId(uid);
        int userId = android.os.UserHandle.getUserId(uid);
        com.android.server.SystemConfig systemConfig = permissionService.systemConfig;
        com.android.server.permission.access.AccessState accessState = null;
        if (systemConfig == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig = null;
        }
        int[] globalGids = systemConfig.getGlobalGids();
        com.android.server.permission.access.AccessCheckingService this_$iv = permissionService.service;
        com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
        if (accessState2 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
        } else {
            accessState = accessState2;
        }
        com.android.server.permission.access.GetStateScope $this$getGidsForUid_u24lambda_u2453 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getGidsForUid_u24lambda_u2453_u24lambda_u2450 = permissionService.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> uidPermissionFlags = $this$getGidsForUid_u24lambda_u2453_u24lambda_u2450.getUidPermissionFlags($this$getGidsForUid_u24lambda_u2453, appId, userId);
        if (uidPermissionFlags == null) {
            int[] iArrCopyOf = java.util.Arrays.copyOf(globalGids, globalGids.length);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(iArrCopyOf, "copyOf(...)");
            return iArrCopyOf;
        }
        android.util.IntArray gids = android.util.IntArray.wrap(globalGids);
        int index$iv = 0;
        int size = uidPermissionFlags.getSize();
        while (index$iv < size) {
            java.lang.String strKeyAt = uidPermissionFlags.keyAt(index$iv);
            int flags = uidPermissionFlags.valueAt(index$iv).intValue();
            java.lang.String permissionName = strKeyAt;
            int appId2 = appId;
            if (com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(flags)) {
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getGidsForUid_u24lambda_u2453_u24lambda_u2452_u24lambda_u2451 = permissionService.policy;
                com.android.server.permission.access.permission.Permission permission = $this$getGidsForUid_u24lambda_u2453_u24lambda_u2452_u24lambda_u2451.getPermissions($this$getGidsForUid_u24lambda_u2453).get(permissionName);
                if (permission != null) {
                    int[] permissionGids = permission.getGidsForUser(userId);
                    if (!(permissionGids.length == 0)) {
                        gids.addAll(permissionGids);
                    }
                }
            }
            index$iv++;
            permissionService = this;
            appId = appId2;
        }
        return gids.toArray();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void grantRuntimePermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String deviceId, int userId) throws java.lang.Exception {
        setRuntimePermissionGranted$default(this, packageName, userId, permissionName, deviceId, true, false, null, 96, null);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokeRuntimePermission(java.lang.String packageName, java.lang.String permissionName, java.lang.String deviceId, int userId, java.lang.String reason) throws java.lang.Exception {
        setRuntimePermissionGranted$default(this, packageName, userId, permissionName, deviceId, false, false, reason, 32, null);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void revokePostNotificationPermissionWithoutKillForTest(java.lang.String packageName, int userId) throws java.lang.Exception {
        setRuntimePermissionGranted$default(this, packageName, userId, "android.permission.POST_NOTIFICATIONS", "default:0", false, true, null, 64, null);
    }

    static /* synthetic */ void setRuntimePermissionGranted$default(com.android.server.permission.access.permission.PermissionService permissionService, java.lang.String str, int i, java.lang.String str2, java.lang.String str3, boolean z, boolean z2, java.lang.String str4, int i2, java.lang.Object obj) throws java.lang.Exception {
        boolean z3;
        java.lang.String str5;
        if ((i2 & 32) == 0) {
            z3 = z2;
        } else {
            z3 = false;
        }
        if ((i2 & 64) == 0) {
            str5 = str4;
        } else {
            str5 = null;
        }
        permissionService.setRuntimePermissionGranted(str, i, str2, str3, z, z3, str5);
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0105  */
    /* JADX WARN: Type inference failed for: r2v2, types: [T, com.android.server.pm.pkg.PackageState] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void setRuntimePermissionGranted(java.lang.String r33, int r34, java.lang.String r35, java.lang.String r36, boolean r37, boolean r38, java.lang.String r39) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 531
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.PermissionService.setRuntimePermissionGranted(java.lang.String, int, java.lang.String, java.lang.String, boolean, boolean, java.lang.String):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0138 A[Catch: all -> 0x020e, TryCatch #0 {, blocks: (B:5:0x000d, B:7:0x0013, B:8:0x001a, B:10:0x0035, B:11:0x0049, B:12:0x004c, B:42:0x01a7, B:43:0x01cd, B:13:0x0060, B:16:0x007d, B:19:0x009e, B:21:0x00b7, B:27:0x00cd, B:29:0x00e2, B:34:0x00f4, B:39:0x0138, B:44:0x01da), top: B:50:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0198  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void setRequestedPermissionStates(com.android.server.pm.pkg.PackageState r32, int r33, android.util.ArrayMap<java.lang.String, java.lang.Integer> r34) {
        /*
            Method dump skipped, instruction units count: 538
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.PermissionService.setRequestedPermissionStates(com.android.server.pm.pkg.PackageState, int, android.util.ArrayMap):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setRuntimePermissionGranted(com.android.server.permission.access.MutateStateScope $this$setRuntimePermissionGranted, com.android.server.pm.pkg.PackageState packageState, int userId, java.lang.String permissionName, java.lang.String deviceId, boolean isGranted, boolean canManageRolePermission, boolean overridePolicyFixed, boolean reportError, java.lang.String methodName) {
        int action;
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$setRuntimePermissionGranted_u24lambda_u2461 = this.policy;
        com.android.server.permission.access.permission.Permission permission = $this$setRuntimePermissionGranted_u24lambda_u2461.getPermissions($this$setRuntimePermissionGranted).get(permissionName);
        if (permission == null) {
            if (reportError) {
                throw new java.lang.IllegalArgumentException("Unknown permission " + permissionName);
            }
            return;
        }
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(androidPackage);
        java.lang.String packageName = packageState.getPackageName();
        if (!com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 32)) {
            if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 67108864)) {
                if (!canManageRolePermission) {
                    if (reportError) {
                        throw new java.lang.SecurityException("Permission " + permissionName + " is managed by role");
                    }
                    return;
                }
            } else {
                if (permission.getPermissionInfo().getProtection() == 1) {
                    if (androidPackage.getTargetSdkVersion() < 23) {
                        return;
                    }
                    if (isGranted && packageState.getUserStateOrDefault(userId).isInstantApp() && !com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 4096)) {
                        if (reportError) {
                            throw new java.lang.SecurityException("Cannot grant non-instant permission " + permissionName + " to package " + packageName);
                        }
                        return;
                    }
                } else {
                    if (reportError) {
                        throw new java.lang.SecurityException("Permission " + permissionName + " requested by package " + packageName + " is not a changeable permission type");
                    }
                    return;
                }
            }
        }
        int appId = packageState.getAppId();
        boolean z = true;
        int oldFlags = getPermissionFlagsWithPolicy($this$setRuntimePermissionGranted, appId, userId, permissionName, deviceId);
        if (!androidPackage.getRequestedPermissions().contains(permissionName) && oldFlags == 0) {
            if (reportError) {
                android.util.Slog.e(LOG_TAG, "Permission " + permissionName + " isn't requested by package " + packageName);
                return;
            }
            return;
        }
        if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(oldFlags, 256)) {
            if (reportError) {
                android.util.Slog.e(LOG_TAG, methodName + ": Cannot change system fixed permission " + permissionName + " for package " + packageName);
                return;
            }
            return;
        }
        if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(oldFlags, 128) && !overridePolicyFixed) {
            if (reportError) {
                android.util.Slog.e(LOG_TAG, methodName + ": Cannot change policy fixed permission " + permissionName + " for package " + packageName);
                return;
            }
            return;
        }
        if (isGranted && com.android.server.permission.access.util.IntExtensionsKt.hasBits(oldFlags, 262144)) {
            if (reportError) {
                android.util.Slog.e(LOG_TAG, methodName + ": Cannot grant hard-restricted non-exempt permission " + permissionName + " to package " + packageName);
                return;
            }
            return;
        }
        if (isGranted && com.android.server.permission.access.util.IntExtensionsKt.hasBits(oldFlags, 524288)) {
            if (reportError) {
                android.util.Slog.e(LOG_TAG, methodName + ": Cannot grant soft-restricted non-exempt permission " + permissionName + " to package " + packageName);
                return;
            }
            return;
        }
        int newFlags = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.updateRuntimePermissionGranted(oldFlags, isGranted);
        if (oldFlags == newFlags) {
            return;
        }
        setPermissionFlagsWithPolicy($this$setRuntimePermissionGranted, appId, userId, permissionName, deviceId, newFlags);
        if (permission.getPermissionInfo().getProtection() != 1) {
            z = false;
        }
        if (z) {
            if (isGranted) {
                action = 1243;
            } else {
                action = 1245;
            }
            android.metrics.LogMaker log = new android.metrics.LogMaker(action);
            log.setPackageName(packageName);
            log.addTaggedData(1241, permissionName);
            com.android.internal.logging.MetricsLogger metricsLogger = this.metricsLogger;
            if (metricsLogger == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("metricsLogger");
                metricsLogger = null;
            }
            metricsLogger.write(log);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setAppOpPermissionGranted(com.android.server.permission.access.MutateStateScope $this$setAppOpPermissionGranted, com.android.server.pm.pkg.PackageState packageState, int userId, java.lang.String permissionName, boolean isGranted) {
        com.android.server.permission.access.SchemePolicy schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar = this.service.getSchemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar("uid", com.android.server.permission.access.AppOpUri.SCHEME);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar, "null cannot be cast to non-null type com.android.server.permission.access.appop.AppIdAppOpPolicy");
        com.android.server.permission.access.appop.AppIdAppOpPolicy appOpPolicy = (com.android.server.permission.access.appop.AppIdAppOpPolicy) schemePolicy$frameworks__base__services__permission__android_common__services_permission_pre_jarjar;
        java.lang.String appOpName = android.app.AppOpsManager.permissionToOp(permissionName);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(appOpName);
        int mode = isGranted ? 0 : 2;
        appOpPolicy.setAppOpMode($this$setAppOpPermissionGranted, packageState.getAppId(), userId, appOpName, mode);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public int getPermissionFlags(java.lang.String packageName, java.lang.String permissionName, java.lang.String deviceId, int userId) throws java.lang.Exception {
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        com.android.server.permission.access.AccessState accessState = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (userManagerInternal.exists(userId)) {
            enforceCallingOrSelfCrossUserPermission(userId, true, false, "getPermissionFlags");
            enforceCallingOrSelfAnyPermission("getPermissionFlags", "android.permission.GRANT_RUNTIME_PERMISSIONS", "android.permission.REVOKE_RUNTIME_PERMISSIONS", "android.permission.GET_RUNTIME_PERMISSIONS");
            com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
            if (packageManagerLocal == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
                packageManagerLocal = null;
            }
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = packageManagerLocal.withFilteredSnapshot();
            try {
                com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotWithFilteredSnapshot;
                com.android.server.pm.pkg.PackageState packageState = it.getPackageState(packageName);
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
                if (packageState == null) {
                    android.util.Slog.w(LOG_TAG, "getPermissionFlags: Unknown package " + packageName);
                    return 0;
                }
                com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
                com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
                if (accessState2 == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                com.android.server.permission.access.GetStateScope $this$getPermissionFlags_u24lambda_u2466 = new com.android.server.permission.access.GetStateScope(accessState);
                com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionFlags_u24lambda_u2466_u24lambda_u2465 = this.policy;
                com.android.server.permission.access.permission.Permission permission = $this$getPermissionFlags_u24lambda_u2466_u24lambda_u2465.getPermissions($this$getPermissionFlags_u24lambda_u2466).get(permissionName);
                if (permission == null) {
                    android.util.Slog.w(LOG_TAG, "getPermissionFlags: Unknown permission " + permissionName);
                    return 0;
                }
                int flags = getPermissionFlagsWithPolicy($this$getPermissionFlags_u24lambda_u2466, packageState.getAppId(), userId, permissionName, deviceId);
                return com.android.server.permission.access.permission.PermissionFlags.INSTANCE.toApiFlags(flags);
            } finally {
            }
        } else {
            android.util.Slog.w(LOG_TAG, "getPermissionFlags: Unknown user " + userId);
            return 0;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, android.permission.PermissionManager.PermissionState> getAllPermissionStates(java.lang.String packageName, java.lang.String deviceId, int userId) throws java.lang.Exception {
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> allPermissionFlags;
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        com.android.server.permission.access.AccessState accessState = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (userManagerInternal.exists(userId)) {
            enforceCallingOrSelfCrossUserPermission(userId, true, false, "getAllPermissionStates");
            enforceCallingOrSelfAnyPermission("getAllPermissionStates", "android.permission.GRANT_RUNTIME_PERMISSIONS", "android.permission.REVOKE_RUNTIME_PERMISSIONS", "android.permission.GET_RUNTIME_PERMISSIONS");
            com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
            if (packageManagerLocal == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
                packageManagerLocal = null;
            }
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = packageManagerLocal.withFilteredSnapshot();
            try {
                com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotWithFilteredSnapshot;
                com.android.server.pm.pkg.PackageState packageState = it.getPackageState(packageName);
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
                if (packageState == null) {
                    android.util.Slog.w(LOG_TAG, "getAllPermissionStates: Unknown package " + packageName);
                    return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
                }
                com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
                com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
                if (accessState2 == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                com.android.server.permission.access.GetStateScope $this$getAllPermissionStates_u24lambda_u2471 = new com.android.server.permission.access.GetStateScope(accessState);
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(deviceId, "default:0")) {
                    com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getAllPermissionStates_u24lambda_u2471_u24lambda_u2468 = this.policy;
                    allPermissionFlags = $this$getAllPermissionStates_u24lambda_u2471_u24lambda_u2468.getAllPermissionFlags($this$getAllPermissionStates_u24lambda_u2471, packageState.getAppId(), userId);
                } else {
                    com.android.server.permission.access.permission.DevicePermissionPolicy $this$getAllPermissionStates_u24lambda_u2471_u24lambda_u2469 = this.devicePolicy;
                    allPermissionFlags = $this$getAllPermissionStates_u24lambda_u2471_u24lambda_u2469.getAllPermissionFlags($this$getAllPermissionStates_u24lambda_u2471, packageState.getAppId(), deviceId, userId);
                }
                if (allPermissionFlags != null) {
                    com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap = allPermissionFlags;
                    android.util.ArrayMap permissionStates = new android.util.ArrayMap();
                    com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap2 = indexedMap;
                    int size = indexedMap2.getSize();
                    int index$iv = 0;
                    while (index$iv < size) {
                        java.lang.String strKeyAt = indexedMap2.keyAt(index$iv);
                        int flags = indexedMap2.valueAt(index$iv).intValue();
                        java.lang.String permissionName = strKeyAt;
                        boolean granted = isPermissionGranted($this$getAllPermissionStates_u24lambda_u2471, packageState, userId, permissionName, deviceId);
                        int apiFlags = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.toApiFlags(flags);
                        permissionStates.put(permissionName, new android.permission.PermissionManager.PermissionState(granted, apiFlags));
                        index$iv++;
                        size = size;
                        indexedMap2 = indexedMap2;
                    }
                    return permissionStates;
                }
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
            } finally {
            }
        } else {
            android.util.Slog.w(LOG_TAG, "getAllPermissionStates: Unknown user " + userId);
            return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionRevokedByPolicy(java.lang.String packageName, java.lang.String permissionName, java.lang.String deviceId, int userId) throws java.lang.Exception {
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        com.android.server.permission.access.AccessState accessState = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(userId)) {
            android.util.Slog.w(LOG_TAG, "isPermissionRevokedByPolicy: Unknown user " + userId);
            return false;
        }
        enforceCallingOrSelfCrossUserPermission(userId, true, false, "isPermissionRevokedByPolicy");
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, android.os.Binder.getCallingUid(), userId);
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotWithFilteredSnapshot;
            try {
                com.android.server.pm.pkg.PackageState packageState = it.getPackageState(packageName);
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
                if (packageState == null) {
                    return false;
                }
                com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
                com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
                if (accessState2 == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                } else {
                    accessState = accessState2;
                }
                com.android.server.permission.access.GetStateScope $this$isPermissionRevokedByPolicy_u24lambda_u2473 = new com.android.server.permission.access.GetStateScope(accessState);
                if (isPermissionGranted($this$isPermissionRevokedByPolicy_u24lambda_u2473, packageState, userId, permissionName, deviceId)) {
                    return false;
                }
                int flags = getPermissionFlagsWithPolicy($this$isPermissionRevokedByPolicy_u24lambda_u2473, packageState.getAppId(), userId, permissionName, deviceId);
                return com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 128);
            } catch (java.lang.Throwable th) {
                th = th;
                java.lang.Throwable th2 = th;
                try {
                    throw th2;
                } catch (java.lang.Throwable th3) {
                    com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, th2);
                    throw th3;
                }
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean isPermissionsReviewRequired(java.lang.String packageName, int userId) throws java.lang.Exception {
        if (packageName == null) {
            throw new java.lang.IllegalArgumentException("packageName cannot be null".toString());
        }
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        com.android.server.permission.access.AccessState accessState = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot it = unfilteredSnapshotWithUnfilteredSnapshot;
            com.android.server.pm.pkg.PackageState packageState = getPackageState(it, packageName);
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            if (packageState == null) {
                return false;
            }
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            com.android.server.permission.access.AccessState accessState2 = this_$iv.state;
            if (accessState2 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            } else {
                accessState = accessState2;
            }
            com.android.server.permission.access.GetStateScope $this$isPermissionsReviewRequired_u24lambda_u2477 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$isPermissionsReviewRequired_u24lambda_u2477_u24lambda_u2476 = this.policy;
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> uidPermissionFlags = $this$isPermissionsReviewRequired_u24lambda_u2477_u24lambda_u2476.getUidPermissionFlags($this$isPermissionsReviewRequired_u24lambda_u2477, packageState.getAppId(), userId);
            if (uidPermissionFlags == null) {
                return false;
            }
            int size = uidPermissionFlags.getSize();
            for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
                java.lang.Object key$iv = uidPermissionFlags.keyAt(index$iv$iv);
                java.lang.Object value$iv = uidPermissionFlags.valueAt(index$iv$iv);
                int it2 = ((java.lang.Number) value$iv).intValue();
                if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(it2, 5120)) {
                    return true;
                }
            }
            return false;
        } catch (java.lang.Throwable th) {
            try {
                throw th;
            } catch (java.lang.Throwable th2) {
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, th);
                throw th2;
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean shouldShowRequestPermissionRationale(java.lang.String packageName, java.lang.String permissionName, java.lang.String deviceId, int userId) throws java.lang.Exception {
        int appId;
        boolean zIsChangeEnabledByPackageName;
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        com.android.internal.compat.IPlatformCompat iPlatformCompat = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (userManagerInternal.exists(userId)) {
            enforceCallingOrSelfCrossUserPermission(userId, true, false, "shouldShowRequestPermissionRationale");
            int callingUid = android.os.Binder.getCallingUid();
            com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
            if (packageManagerLocal == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
                packageManagerLocal = null;
            }
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, callingUid, userId);
            try {
                com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotWithFilteredSnapshot;
                com.android.server.pm.pkg.PackageState packageState = it.getPackageState(packageName);
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
                if (packageState == null || android.os.UserHandle.getAppId(callingUid) != (appId = packageState.getAppId())) {
                    return false;
                }
                com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
                com.android.server.permission.access.AccessState accessState = this_$iv.state;
                if (accessState == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                    accessState = null;
                }
                com.android.server.permission.access.GetStateScope $this$shouldShowRequestPermissionRationale_u24lambda_u2480 = new com.android.server.permission.access.GetStateScope(accessState);
                if (isPermissionGranted($this$shouldShowRequestPermissionRationale_u24lambda_u2480, packageState, userId, permissionName, deviceId)) {
                    return false;
                }
                int flags = getPermissionFlagsWithPolicy($this$shouldShowRequestPermissionRationale_u24lambda_u2480, appId, userId, permissionName, deviceId);
                if (com.android.server.permission.access.util.IntExtensionsKt.hasAnyBit(flags, UNREQUESTABLE_MASK)) {
                    return false;
                }
                if (com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(permissionName, "android.permission.ACCESS_BACKGROUND_LOCATION")) {
                    long token$iv = android.os.Binder.clearCallingIdentity();
                    try {
                        try {
                            com.android.internal.compat.IPlatformCompat iPlatformCompat2 = this.platformCompat;
                            if (iPlatformCompat2 == null) {
                                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("platformCompat");
                            } else {
                                iPlatformCompat = iPlatformCompat2;
                            }
                            zIsChangeEnabledByPackageName = iPlatformCompat.isChangeEnabledByPackageName(BACKGROUND_RATIONALE_CHANGE_ID, packageName, userId);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.e(LOG_TAG, "shouldShowRequestPermissionRationale: Unable to check if compatibility change is enabled", e);
                            zIsChangeEnabledByPackageName = false;
                        }
                        android.os.Binder.restoreCallingIdentity(token$iv);
                        boolean isBackgroundRationaleChangeEnabled = zIsChangeEnabledByPackageName;
                        if (isBackgroundRationaleChangeEnabled) {
                            return true;
                        }
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(token$iv);
                        throw th;
                    }
                }
                return com.android.server.permission.access.util.IntExtensionsKt.hasBits(flags, 32);
            } finally {
            }
        } else {
            android.util.Slog.w(LOG_TAG, "shouldShowRequestPermissionRationale: Unknown user " + userId);
            return false;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlags(java.lang.String packageName, java.lang.String permissionName, int flagMask, int flagValues, boolean enforceAdjustPolicyPermission, java.lang.String deviceId, int userId) throws java.lang.Throwable {
        boolean isPermissionRequested;
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(userId)) {
            android.util.Slog.w(LOG_TAG, "updatePermissionFlags: Unknown user " + userId);
            return;
        }
        enforceCallingOrSelfCrossUserPermission(userId, true, true, "updatePermissionFlags");
        enforceCallingOrSelfAnyPermission("updatePermissionFlags", "android.permission.GRANT_RUNTIME_PERMISSIONS", "android.permission.REVOKE_RUNTIME_PERMISSIONS");
        if (!isRootOrSystemUid(callingUid) && com.android.server.permission.access.util.IntExtensionsKt.hasBits(flagMask, 4)) {
            if (enforceAdjustPolicyPermission) {
                this.context.enforceCallingOrSelfPermission("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY", "Need android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY to change policy flags");
            } else {
                android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
                if (packageManagerInternal == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                    packageManagerInternal = null;
                }
                int targetSdkVersion = packageManagerInternal.getUidTargetSdkVersion(callingUid);
                if (!(targetSdkVersion < 29)) {
                    throw new java.lang.IllegalArgumentException("android.permission.ADJUST_RUNTIME_PERMISSIONS_POLICY needs to be checked for packages targeting 29 or later when changing policy flags".toString());
                }
            }
        }
        android.content.pm.PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
        if (packageManagerInternal2 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal2 = null;
        }
        com.android.server.pm.pkg.PackageStateInternal packageState = packageManagerInternal2.getPackageStateInternal(packageName);
        com.android.server.pm.pkg.AndroidPackage androidPackage = packageState != null ? packageState.getAndroidPackage() : null;
        if (androidPackage != null) {
            android.content.pm.PackageManagerInternal packageManagerInternal3 = this.packageManagerInternal;
            if (packageManagerInternal3 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                packageManagerInternal3 = null;
            }
            if (!packageManagerInternal3.filterAppAccess(packageName, callingUid, userId, false)) {
                boolean canUpdateSystemFlags = isRootOrSystemUid(callingUid);
                if (!androidPackage.getRequestedPermissions().contains(permissionName)) {
                    android.content.pm.PackageManagerInternal packageManagerInternal4 = this.packageManagerInternal;
                    if (packageManagerInternal4 == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                        packageManagerInternal4 = null;
                    }
                    java.lang.String[] sharedUserPackageNames = packageManagerInternal4.getSharedUserPackagesForPackage(packageName, userId);
                    int length = sharedUserPackageNames.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            isPermissionRequested = false;
                            break;
                        }
                        java.lang.String str = sharedUserPackageNames[i];
                        android.content.pm.PackageManagerInternal packageManagerInternal5 = this.packageManagerInternal;
                        if (packageManagerInternal5 == null) {
                            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                            packageManagerInternal5 = null;
                        }
                        com.android.server.pm.pkg.AndroidPackage sharedUserPackage = packageManagerInternal5.getPackage(str);
                        if (sharedUserPackage != null && sharedUserPackage.getRequestedPermissions().contains(permissionName)) {
                            isPermissionRequested = true;
                            break;
                        }
                        i++;
                    }
                } else {
                    isPermissionRequested = true;
                }
                int appId = packageState.getAppId();
                com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
                synchronized (this_$iv.stateLock) {
                    try {
                        com.android.server.permission.access.AccessState accessState = this_$iv.state;
                        if (accessState != null) {
                            com.android.server.permission.access.AccessState oldState$iv = accessState;
                            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
                            com.android.server.permission.access.MutateStateScope $this$updatePermissionFlags_u24lambda_u2484 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
                            updatePermissionFlags($this$updatePermissionFlags_u24lambda_u2484, appId, userId, permissionName, deviceId, flagMask, flagValues, canUpdateSystemFlags, true, isPermissionRequested, "updatePermissionFlags", packageName);
                            this_$iv.persistence.write(newState$iv);
                            this_$iv.state = newState$iv;
                            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
                            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
                            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                            return;
                        }
                        try {
                            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                            accessState = null;
                            com.android.server.permission.access.AccessState oldState$iv2 = accessState;
                            com.android.server.permission.access.MutableAccessState newState$iv2 = oldState$iv2.toMutable();
                            com.android.server.permission.access.MutateStateScope $this$updatePermissionFlags_u24lambda_u24842 = new com.android.server.permission.access.MutateStateScope(oldState$iv2, newState$iv2);
                            try {
                                updatePermissionFlags($this$updatePermissionFlags_u24lambda_u24842, appId, userId, permissionName, deviceId, flagMask, flagValues, canUpdateSystemFlags, true, isPermissionRequested, "updatePermissionFlags", packageName);
                                this_$iv.persistence.write(newState$iv2);
                                try {
                                    this_$iv.state = newState$iv2;
                                    com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv2 = this_$iv.policy;
                                    $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv2.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv2));
                                    com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                                    return;
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                }
                            } catch (java.lang.Throwable th2) {
                                th = th2;
                            }
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    } catch (java.lang.Throwable th4) {
                        th = th4;
                    }
                    throw th;
                }
            }
        }
        android.util.Slog.w(LOG_TAG, "updatePermissionFlags: Unknown package " + packageName);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void updatePermissionFlagsForAllApps(int flagMask, int flagValues, int userId) throws java.lang.Exception {
        com.android.server.permission.access.MutableAccessState newState$iv;
        com.android.server.permission.access.AccessState oldState$iv;
        com.android.server.permission.access.AccessCheckingService this_$iv;
        int callingUid;
        int callingUid2 = android.os.Binder.getCallingUid();
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        com.android.server.permission.access.AccessState accessState = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(userId)) {
            android.util.Slog.w(LOG_TAG, "updatePermissionFlagsForAllApps: Unknown user " + userId);
            return;
        }
        enforceCallingOrSelfCrossUserPermission(userId, true, true, "updatePermissionFlagsForAllApps");
        enforceCallingOrSelfAnyPermission("updatePermissionFlagsForAllApps", "android.permission.GRANT_RUNTIME_PERMISSIONS", "android.permission.REVOKE_RUNTIME_PERMISSIONS");
        boolean canUpdateSystemFlags = isRootOrSystemUid(callingUid2);
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot it = unfilteredSnapshotWithUnfilteredSnapshot;
            java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> packageStates = it.getPackageStates();
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            com.android.server.permission.access.AccessCheckingService this_$iv2 = this.service;
            synchronized (this_$iv2.stateLock) {
                try {
                    com.android.server.permission.access.AccessState accessState2 = this_$iv2.state;
                    if (accessState2 == null) {
                        try {
                            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } else {
                        accessState = accessState2;
                    }
                    com.android.server.permission.access.AccessState oldState$iv2 = accessState;
                    com.android.server.permission.access.MutableAccessState newState$iv2 = oldState$iv2.toMutable();
                    com.android.server.permission.access.MutateStateScope $this$updatePermissionFlagsForAllApps_u24lambda_u2488 = new com.android.server.permission.access.MutateStateScope(oldState$iv2, newState$iv2);
                    for (java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState> entry : packageStates.entrySet()) {
                        try {
                            java.lang.String packageName = entry.getKey();
                            com.android.server.pm.pkg.PackageState packageState = entry.getValue();
                            if (packageState.isApex()) {
                                newState$iv = newState$iv2;
                                oldState$iv = oldState$iv2;
                                this_$iv = this_$iv2;
                                callingUid = callingUid2;
                            } else {
                                com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
                                if (androidPackage == null) {
                                    newState$iv = newState$iv2;
                                    oldState$iv = oldState$iv2;
                                    this_$iv = this_$iv2;
                                    callingUid = callingUid2;
                                } else {
                                    java.lang.Iterable $this$forEach$iv = androidPackage.getRequestedPermissions();
                                    for (java.lang.Object element$iv : $this$forEach$iv) {
                                        java.lang.String permissionName = (java.lang.String) element$iv;
                                        com.android.server.permission.access.MutableAccessState newState$iv3 = newState$iv2;
                                        com.android.server.permission.access.AccessState oldState$iv3 = oldState$iv2;
                                        com.android.server.permission.access.AccessCheckingService this_$iv3 = this_$iv2;
                                        int callingUid3 = callingUid2;
                                        try {
                                            updatePermissionFlags($this$updatePermissionFlagsForAllApps_u24lambda_u2488, packageState.getAppId(), userId, permissionName, "default:0", flagMask, flagValues, canUpdateSystemFlags, false, true, "updatePermissionFlagsForAllApps", packageName);
                                            this_$iv2 = this_$iv3;
                                            callingUid2 = callingUid3;
                                            newState$iv2 = newState$iv3;
                                            oldState$iv2 = oldState$iv3;
                                        } catch (java.lang.Throwable th2) {
                                            th = th2;
                                            throw th;
                                        }
                                    }
                                    newState$iv = newState$iv2;
                                    oldState$iv = oldState$iv2;
                                    this_$iv = this_$iv2;
                                    callingUid = callingUid2;
                                }
                            }
                            this_$iv2 = this_$iv;
                            callingUid2 = callingUid;
                            newState$iv2 = newState$iv;
                            oldState$iv2 = oldState$iv;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                        }
                    }
                    com.android.server.permission.access.MutableAccessState newState$iv4 = newState$iv2;
                    com.android.server.permission.access.AccessCheckingService this_$iv4 = this_$iv2;
                    try {
                        this_$iv4.persistence.write(newState$iv4);
                        try {
                            this_$iv4.state = newState$iv4;
                            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv4.policy;
                            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv4));
                            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                            throw th;
                        }
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                    }
                } catch (java.lang.Throwable th6) {
                    th = th6;
                }
            }
        } finally {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updatePermissionFlags(com.android.server.permission.access.MutateStateScope $this$updatePermissionFlags, int appId, int userId, java.lang.String permissionName, java.lang.String deviceId, int flagMask, int flagValues, boolean canUpdateSystemFlags, boolean reportErrorForUnknownPermission, boolean isPermissionRequested, java.lang.String methodName, java.lang.String packageName) {
        int flagMask2;
        int flagValues2;
        if (canUpdateSystemFlags) {
            flagMask2 = flagMask;
            flagValues2 = flagValues;
        } else {
            flagMask2 = com.android.server.permission.access.util.IntExtensionsKt.andInv(flagMask, 30768);
            flagValues2 = com.android.server.permission.access.util.IntExtensionsKt.andInv(flagValues, 30768);
        }
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$updatePermissionFlags_u24lambda_u2489 = this.policy;
        com.android.server.permission.access.permission.Permission permission = $this$updatePermissionFlags_u24lambda_u2489.getPermissions($this$updatePermissionFlags).get(permissionName);
        if (permission == null) {
            if (reportErrorForUnknownPermission) {
                throw new java.lang.IllegalArgumentException("Unknown permission " + permissionName);
            }
            return;
        }
        int oldFlags = getPermissionFlagsWithPolicy($this$updatePermissionFlags, appId, userId, permissionName, deviceId);
        if (!isPermissionRequested && oldFlags == 0) {
            android.util.Slog.w(LOG_TAG, methodName + ": Permission " + permissionName + " isn't requested by package " + packageName);
        } else {
            int newFlags = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.updateFlags(permission, oldFlags, flagMask2, flagValues2);
            setPermissionFlagsWithPolicy($this$updatePermissionFlags, appId, userId, permissionName, deviceId, newFlags);
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.ArrayList<java.lang.String> getAllowlistedRestrictedPermissions(java.lang.String packageName, int allowlistedFlags, int userId) throws java.lang.Exception {
        com.android.server.pm.pkg.AndroidPackage androidPackage;
        if (packageName == null) {
            throw new java.lang.IllegalArgumentException("packageName cannot be null".toString());
        }
        com.android.internal.util.Preconditions.checkFlagsArgument(allowlistedFlags, 7);
        com.android.internal.util.Preconditions.checkArgumentNonnegative(userId, "userId cannot be null");
        com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
        android.content.pm.PackageManagerInternal packageManagerInternal = null;
        if (userManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
            userManagerInternal = null;
        }
        if (!userManagerInternal.exists(userId)) {
            android.util.Slog.w(LOG_TAG, "AllowlistedRestrictedPermission api: Unknown user " + userId);
            return null;
        }
        enforceCallingOrSelfCrossUserPermission(userId, false, false, "getAllowlistedRestrictedPermissions");
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, callingUid, userId);
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotWithFilteredSnapshot;
            com.android.server.pm.pkg.PackageState packageState = it.getPackageState(packageName);
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
            if (packageState == null || (androidPackage = packageState.getAndroidPackage()) == null) {
                return null;
            }
            boolean isCallerPrivileged = this.context.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
            if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(allowlistedFlags, 1) && !isCallerPrivileged) {
                throw new java.lang.SecurityException("Querying system allowlist requires android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
            }
            android.content.pm.PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
            if (packageManagerInternal2 == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            } else {
                packageManagerInternal = packageManagerInternal2;
            }
            boolean isCallerInstallerOnRecord = packageManagerInternal.isCallerInstallerOfRecord(androidPackage, callingUid);
            if (com.android.server.permission.access.util.IntExtensionsKt.hasAnyBit(allowlistedFlags, 6) && !isCallerPrivileged && !isCallerInstallerOnRecord) {
                throw new java.lang.SecurityException("Querying upgrade or installer allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
            }
            return getAllowlistedRestrictedPermissionsUnchecked(packageState.getAppId(), allowlistedFlags, userId);
        } finally {
        }
    }

    private final int getPermissionFlagsWithPolicy(com.android.server.permission.access.GetStateScope $this$getPermissionFlagsWithPolicy, int appId, int userId, java.lang.String permissionName, java.lang.String deviceId) {
        if (!com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.deviceAwarePermissionApisEnabled() || com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(deviceId, "default:0")) {
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionFlagsWithPolicy_u24lambda_u2492 = this.policy;
            return $this$getPermissionFlagsWithPolicy_u24lambda_u2492.getPermissionFlags($this$getPermissionFlagsWithPolicy, appId, userId, permissionName);
        }
        if (!android.permission.PermissionManager.DEVICE_AWARE_PERMISSIONS.contains(permissionName)) {
            android.util.Slog.i(LOG_TAG, permissionName + " is not device aware permission,  get the flags for default device.");
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionFlagsWithPolicy_u24lambda_u2493 = this.policy;
            return $this$getPermissionFlagsWithPolicy_u24lambda_u2493.getPermissionFlags($this$getPermissionFlagsWithPolicy, appId, userId, permissionName);
        }
        com.android.server.permission.access.permission.DevicePermissionPolicy $this$getPermissionFlagsWithPolicy_u24lambda_u2494 = this.devicePolicy;
        return $this$getPermissionFlagsWithPolicy_u24lambda_u2494.getPermissionFlags($this$getPermissionFlagsWithPolicy, appId, deviceId, userId, permissionName);
    }

    private final boolean setPermissionFlagsWithPolicy(com.android.server.permission.access.MutateStateScope $this$setPermissionFlagsWithPolicy, int appId, int userId, java.lang.String permissionName, java.lang.String deviceId, int flags) {
        if (!com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.deviceAwarePermissionApisEnabled() || com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(deviceId, "default:0")) {
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$setPermissionFlagsWithPolicy_u24lambda_u2495 = this.policy;
            return $this$setPermissionFlagsWithPolicy_u24lambda_u2495.setPermissionFlags($this$setPermissionFlagsWithPolicy, appId, userId, permissionName, flags);
        }
        if (!android.permission.PermissionManager.DEVICE_AWARE_PERMISSIONS.contains(permissionName)) {
            android.util.Slog.i(LOG_TAG, permissionName + " is not device aware permission,  set the flags for default device.");
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$setPermissionFlagsWithPolicy_u24lambda_u2496 = this.policy;
            return $this$setPermissionFlagsWithPolicy_u24lambda_u2496.setPermissionFlags($this$setPermissionFlagsWithPolicy, appId, userId, permissionName, flags);
        }
        com.android.server.permission.access.permission.DevicePermissionPolicy $this$setPermissionFlagsWithPolicy_u24lambda_u2497 = this.devicePolicy;
        return $this$setPermissionFlagsWithPolicy_u24lambda_u2497.setPermissionFlags($this$setPermissionFlagsWithPolicy, appId, deviceId, userId, permissionName, flags);
    }

    private final java.util.ArrayList<java.lang.String> getAllowlistedRestrictedPermissionsUnchecked(int appId, int allowlistedFlags, int userId) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getAllowlistedRestrictedPermissionsUnchecked_u24lambda_u2499 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getAllowlistedRestrictedPermissionsUnchecked_u24lambda_u2499_u24lambda_u2498 = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> uidPermissionFlags = $this$getAllowlistedRestrictedPermissionsUnchecked_u24lambda_u2499_u24lambda_u2498.getUidPermissionFlags($this$getAllowlistedRestrictedPermissionsUnchecked_u24lambda_u2499, appId, userId);
        if (uidPermissionFlags == null) {
            return null;
        }
        int queryFlags = com.android.server.permission.access.util.IntExtensionsKt.hasBits(allowlistedFlags, 1) ? 0 | 65536 : 0;
        if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(allowlistedFlags, 4)) {
            queryFlags |= 131072;
        }
        if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(allowlistedFlags, 2)) {
            queryFlags |= 32768;
        }
        java.util.Collection destination$iv = new java.util.ArrayList();
        int size = uidPermissionFlags.getSize();
        for (int index$iv$iv = 0; index$iv$iv < size; index$iv$iv++) {
            java.lang.Object key$iv = uidPermissionFlags.keyAt(index$iv$iv);
            java.lang.Object value$iv = uidPermissionFlags.valueAt(index$iv$iv);
            int flags = ((java.lang.Number) value$iv).intValue();
            java.lang.String permissionName = (java.lang.String) key$iv;
            if (!com.android.server.permission.access.util.IntExtensionsKt.hasAnyBit(flags, queryFlags)) {
                permissionName = null;
            }
            if (permissionName != null) {
                destination$iv.add(permissionName);
            }
        }
        return (java.util.ArrayList) destination$iv;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean addAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permissionName, int allowlistedFlags, int userId) throws java.lang.Exception {
        if (permissionName == null) {
            throw new java.lang.IllegalArgumentException("permissionName cannot be null".toString());
        }
        if (!enforceRestrictedPermission(permissionName)) {
            return false;
        }
        java.util.ArrayList<java.lang.String> allowlistedRestrictedPermissions = getAllowlistedRestrictedPermissions(packageName, allowlistedFlags, userId);
        if (allowlistedRestrictedPermissions == null) {
            allowlistedRestrictedPermissions = new java.util.ArrayList<>(1);
        }
        if (allowlistedRestrictedPermissions.contains(permissionName)) {
            return false;
        }
        allowlistedRestrictedPermissions.add(permissionName);
        return setAllowlistedRestrictedPermissions(packageName, allowlistedRestrictedPermissions, allowlistedFlags, userId, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void addAllowlistedRestrictedPermissionsUnchecked(com.android.server.pm.pkg.AndroidPackage r8, int r9, java.util.List<java.lang.String> r10, int r11) throws java.lang.Throwable {
        /*
            r7 = this;
            r0 = 2
            java.util.ArrayList r0 = r7.getAllowlistedRestrictedPermissionsUnchecked(r9, r0, r11)
            if (r0 == 0) goto L2b
        Ld:
            r1 = 0
            android.util.ArraySet r2 = new android.util.ArraySet
            r3 = r10
            java.util.Collection r3 = (java.util.Collection) r3
            r2.<init>(r3)
            r3 = r2
            r4 = 0
            r5 = r3
            java.util.Collection r5 = (java.util.Collection) r5
            r6 = r0
            java.lang.Iterable r6 = (java.lang.Iterable) r6
            com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.addAll(r5, r6)
            java.lang.Iterable r2 = (java.lang.Iterable) r2
            java.util.List r0 = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.toList(r2)
            if (r0 == 0) goto L2b
            r4 = r0
            goto L2c
        L2b:
            r4 = r10
        L2c:
            r5 = 2
            r1 = r7
            r2 = r8
            r3 = r9
            r6 = r11
            r1.setAllowlistedRestrictedPermissionsUnchecked(r2, r3, r4, r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.PermissionService.addAllowlistedRestrictedPermissionsUnchecked(com.android.server.pm.pkg.AndroidPackage, int, java.util.List, int):void");
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public boolean removeAllowlistedRestrictedPermission(java.lang.String packageName, java.lang.String permissionName, int allowlistedFlags, int userId) {
        java.util.ArrayList<java.lang.String> allowlistedRestrictedPermissions;
        if (permissionName == null) {
            throw new java.lang.IllegalArgumentException("permissionName cannot be null".toString());
        }
        if (enforceRestrictedPermission(permissionName) && (allowlistedRestrictedPermissions = getAllowlistedRestrictedPermissions(packageName, allowlistedFlags, userId)) != null && allowlistedRestrictedPermissions.remove(permissionName)) {
            return setAllowlistedRestrictedPermissions(packageName, allowlistedRestrictedPermissions, allowlistedFlags, userId, false);
        }
        return false;
    }

    private final boolean enforceRestrictedPermission(java.lang.String permissionName) throws java.lang.Exception {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$enforceRestrictedPermission_u24lambda_u24106 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$enforceRestrictedPermission_u24lambda_u24106_u24lambda_u24105 = this.policy;
        com.android.server.permission.access.permission.Permission permission = $this$enforceRestrictedPermission_u24lambda_u24106_u24lambda_u24105.getPermissions($this$enforceRestrictedPermission_u24lambda_u24106).get(permissionName);
        boolean isImmutablyRestrictedPermission = false;
        if (permission == null) {
            android.util.Slog.w(LOG_TAG, "permission definition for " + permissionName + " does not exist");
            return false;
        }
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = packageManagerLocal.withFilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotWithFilteredSnapshot;
            com.android.server.pm.pkg.PackageState packageState = it.getPackageState(permission.getPermissionInfo().packageName);
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
            if (packageState == null) {
                return false;
            }
            if ((com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().flags, 4) || com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().flags, 8)) && com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().flags, 16)) {
                isImmutablyRestrictedPermission = true;
            }
            if (!isImmutablyRestrictedPermission || this.context.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0) {
                return true;
            }
            throw new java.lang.SecurityException("Cannot modify allowlist of an immutably restricted permission: " + permission.getPermissionInfo().name);
        } finally {
        }
    }

    private final boolean setAllowlistedRestrictedPermissions(java.lang.String packageName, java.util.List<java.lang.String> list, int allowlistedFlags, int userId, boolean isAddingPermission) throws java.lang.Exception {
        com.android.internal.util.Preconditions.checkArgument(java.lang.Integer.bitCount(allowlistedFlags) == 1);
        boolean isCallerPrivileged = this.context.checkCallingOrSelfPermission("android.permission.WHITELIST_RESTRICTED_PERMISSIONS") == 0;
        int callingUid = android.os.Binder.getCallingUid();
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        android.content.pm.PackageManagerInternal packageManagerInternal = null;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotWithFilteredSnapshot = withFilteredSnapshot(packageManagerLocal, callingUid, userId);
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot snapshot = filteredSnapshotWithFilteredSnapshot;
            try {
                com.android.server.pm.pkg.PackageState packageState = snapshot.getPackageStates().get(packageName);
                if (packageState == null) {
                    com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
                    return false;
                }
                com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, null);
                com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
                if (androidPackage == null) {
                    return false;
                }
                android.content.pm.PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
                if (packageManagerInternal2 == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                } else {
                    packageManagerInternal = packageManagerInternal2;
                }
                boolean isCallerInstallerOnRecord = packageManagerInternal.isCallerInstallerOfRecord(androidPackage, callingUid);
                if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(allowlistedFlags, 4)) {
                    if (!isCallerPrivileged && !isCallerInstallerOnRecord) {
                        throw new java.lang.SecurityException("Modifying upgrade allowlist requires being installer on record or android.permission.WHITELIST_RESTRICTED_PERMISSIONS");
                    }
                    if (isAddingPermission && !isCallerPrivileged) {
                        throw new java.lang.SecurityException("Adding to upgrade allowlist requiresandroid.permission.WHITELIST_RESTRICTED_PERMISSIONS");
                    }
                }
                setAllowlistedRestrictedPermissionsUnchecked(androidPackage, packageState.getAppId(), list, allowlistedFlags, userId);
                return true;
            } catch (java.lang.Throwable th) {
                th = th;
                java.lang.Throwable th2 = th;
                try {
                    throw th2;
                } catch (java.lang.Throwable th3) {
                    com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotWithFilteredSnapshot, th2);
                    throw th3;
                }
            }
        } catch (java.lang.Throwable th4) {
            th = th4;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00c2  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00cd  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final void setAllowlistedRestrictedPermissionsUnchecked(com.android.server.pm.pkg.AndroidPackage r31, int r32, java.util.List<java.lang.String> r33, int r34, int r35) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 330
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.PermissionService.setAllowlistedRestrictedPermissionsUnchecked(com.android.server.pm.pkg.AndroidPackage, int, java.util.List, int, int):void");
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissions(com.android.server.pm.pkg.AndroidPackage androidPackage, int userId) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        synchronized (this_$iv.stateLock) {
            com.android.server.permission.access.AccessState oldState$iv = this_$iv.state;
            if (oldState$iv == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                oldState$iv = null;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$resetRuntimePermissions_u24lambda_u24114 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$resetRuntimePermissions_u24lambda_u24114_u24lambda_u24112 = this.policy;
            $this$resetRuntimePermissions_u24lambda_u24114_u24lambda_u24112.resetRuntimePermissions($this$resetRuntimePermissions_u24lambda_u24114, androidPackage.getPackageName(), userId);
            com.android.server.permission.access.permission.DevicePermissionPolicy $this$resetRuntimePermissions_u24lambda_u24114_u24lambda_u24113 = this.devicePolicy;
            $this$resetRuntimePermissions_u24lambda_u24114_u24lambda_u24113.resetRuntimePermissions($this$resetRuntimePermissions_u24lambda_u24114, androidPackage.getPackageName(), userId);
            this_$iv.persistence.write(newState$iv);
            this_$iv.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void resetRuntimePermissionsForUser(int userId) throws java.lang.Exception {
        int i;
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = unfilteredSnapshotWithUnfilteredSnapshot;
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            synchronized (this_$iv.stateLock) {
                int i2 = 0;
                com.android.server.permission.access.AccessState oldState$iv = this_$iv.state;
                if (oldState$iv == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                    oldState$iv = null;
                }
                com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
                com.android.server.permission.access.MutateStateScope $this$resetRuntimePermissionsForUser_u24lambda_u24119_u24lambda_u24118 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
                java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState>> it = snapshot.getPackageStates().entrySet().iterator();
                while (it.hasNext()) {
                    com.android.server.pm.pkg.PackageState packageState = it.next().getValue();
                    if (!packageState.isApex()) {
                        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$resetRuntimePermissionsForUser_u24lambda_u24119_u24lambda_u24118_u24lambda_u24117_u24lambda_u24115 = this.policy;
                        i = i2;
                        $this$resetRuntimePermissionsForUser_u24lambda_u24119_u24lambda_u24118_u24lambda_u24117_u24lambda_u24115.resetRuntimePermissions($this$resetRuntimePermissionsForUser_u24lambda_u24119_u24lambda_u24118, packageState.getPackageName(), userId);
                        com.android.server.permission.access.permission.DevicePermissionPolicy $this$resetRuntimePermissionsForUser_u24lambda_u24119_u24lambda_u24118_u24lambda_u24117_u24lambda_u24116 = this.devicePolicy;
                        $this$resetRuntimePermissionsForUser_u24lambda_u24119_u24lambda_u24118_u24lambda_u24117_u24lambda_u24116.resetRuntimePermissions($this$resetRuntimePermissionsForUser_u24lambda_u24119_u24lambda_u24118, packageState.getPackageName(), userId);
                    } else {
                        i = i2;
                    }
                    i2 = i;
                }
                this_$iv.persistence.write(newState$iv);
                this_$iv.state = newState$iv;
                com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
                $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            }
            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void addOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        this.context.enforceCallingOrSelfPermission("android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS", "addOnPermissionsChangeListener");
        com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners onPermissionsChangeListeners = this.onPermissionsChangeListeners;
        if (onPermissionsChangeListeners == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("onPermissionsChangeListeners");
            onPermissionsChangeListeners = null;
        }
        onPermissionsChangeListeners.addListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void removeOnPermissionsChangeListener(android.permission.IOnPermissionsChangeListener listener) {
        this.context.enforceCallingOrSelfPermission("android.permission.OBSERVE_GRANT_REVOKE_PERMISSIONS", "removeOnPermissionsChangeListener");
        com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners onPermissionsChangeListeners = this.onPermissionsChangeListeners;
        if (onPermissionsChangeListeners == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("onPermissionsChangeListeners");
            onPermissionsChangeListeners = null;
        }
        onPermissionsChangeListeners.removeListener(listener);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<android.content.pm.permission.SplitPermissionInfoParcelable> getSplitPermissions() {
        com.android.server.SystemConfig systemConfig = this.systemConfig;
        if (systemConfig == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("systemConfig");
            systemConfig = null;
        }
        return android.permission.PermissionManager.splitPermissionInfoListToParcelableList(systemConfig.getSplitPermissions());
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String[] getAppOpPermissionPackages(java.lang.String permissionName) throws java.lang.Exception {
        com.android.server.pm.pkg.AndroidPackage androidPackage;
        if (permissionName == null) {
            throw new java.lang.IllegalArgumentException("permissionName cannot be null".toString());
        }
        android.util.ArraySet packageNames = new android.util.ArraySet();
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getAppOpPermissionPackages_u24lambda_u24122 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getAppOpPermissionPackages_u24lambda_u24122_u24lambda_u24121 = this.policy;
        com.android.server.permission.access.permission.Permission permission = $this$getAppOpPermissionPackages_u24lambda_u24122_u24lambda_u24121.getPermissions($this$getAppOpPermissionPackages_u24lambda_u24122).get(permissionName);
        if (permission == null || !com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 64)) {
            android.util.ArraySet $this$toTypedArray$iv = packageNames;
            $this$toTypedArray$iv.toArray(new java.lang.String[0]);
        }
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot = unfilteredSnapshotWithUnfilteredSnapshot;
            java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState>> it = snapshot.getPackageStates().entrySet().iterator();
            while (it.hasNext()) {
                com.android.server.pm.pkg.PackageState packageState = it.next().getValue();
                if (!packageState.isApex() && (androidPackage = packageState.getAndroidPackage()) != null && androidPackage.getRequestedPermissions().contains(permissionName)) {
                    packageNames.add(androidPackage.getPackageName());
                }
            }
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            android.util.ArraySet $this$toTypedArray$iv2 = packageNames;
            return (java.lang.String[]) $this$toTypedArray$iv2.toArray(new java.lang.String[0]);
        } finally {
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getAllAppOpPermissionPackages() throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot;
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot2;
        android.util.ArrayMap appOpPermissionPackageNames = new android.util.ArrayMap();
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getAllAppOpPermissionPackages_u24lambda_u24126 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getAllAppOpPermissionPackages_u24lambda_u24126_u24lambda_u24125 = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$getAllAppOpPermissionPackages_u24lambda_u24126_u24lambda_u24125.getPermissions($this$getAllAppOpPermissionPackages_u24lambda_u24126);
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot snapshot3 = unfilteredSnapshotWithUnfilteredSnapshot;
            java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState>> it = snapshot3.getPackageStates().entrySet().iterator();
            while (it.hasNext()) {
                com.android.server.pm.pkg.PackageState packageState = it.next().getValue();
                if (packageState.isApex()) {
                    snapshot = snapshot3;
                } else {
                    com.android.server.pm.pkg.AndroidPackage androidPackage = packageState.getAndroidPackage();
                    if (androidPackage == null) {
                        snapshot = snapshot3;
                    } else {
                        java.lang.Iterable $this$forEach$iv = androidPackage.getRequestedPermissions();
                        for (java.lang.Object element$iv : $this$forEach$iv) {
                            java.lang.String permissionName = (java.lang.String) element$iv;
                            com.android.server.permission.access.permission.Permission permission = permissions.get(permissionName);
                            if (permission == null) {
                                snapshot2 = snapshot3;
                            } else {
                                snapshot2 = snapshot3;
                                if (com.android.server.permission.access.util.IntExtensionsKt.hasBits(permission.getPermissionInfo().getProtectionFlags(), 64)) {
                                    java.lang.Object obj = appOpPermissionPackageNames.get(permissionName);
                                    if (obj == null) {
                                        android.util.ArraySet arraySet = new android.util.ArraySet();
                                        appOpPermissionPackageNames.put(permissionName, arraySet);
                                        obj = arraySet;
                                    }
                                    android.util.ArraySet packageNames = (android.util.ArraySet) obj;
                                    packageNames.add(androidPackage.getPackageName());
                                }
                            }
                            snapshot3 = snapshot2;
                        }
                        snapshot = snapshot3;
                    }
                }
                snapshot3 = snapshot;
            }
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            return appOpPermissionPackageNames;
        } finally {
        }
    }

    public byte[] backupRuntimePermissions(int userId) throws java.lang.Exception {
        com.android.internal.util.Preconditions.checkArgumentNonnegative(userId, "userId cannot be null");
        final java.util.concurrent.CompletableFuture backup = new java.util.concurrent.CompletableFuture();
        android.permission.PermissionControllerManager permissionControllerManager = this.permissionControllerManager;
        if (permissionControllerManager == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("permissionControllerManager");
            permissionControllerManager = null;
        }
        permissionControllerManager.getRuntimePermissionBackup(android.os.UserHandle.of(userId), com.android.server.PermissionThread.getExecutor(), new java.util.function.Consumer() { // from class: com.android.server.permission.access.permission.PermissionService.backupRuntimePermissions.1
            @Override // java.util.function.Consumer
            public final void accept(byte[] p0) {
                backup.complete(p0);
            }
        });
        try {
            return (byte[]) backup.get(BACKUP_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (java.lang.Exception e) {
            if (e instanceof java.util.concurrent.TimeoutException ? true : e instanceof java.lang.InterruptedException ? true : e instanceof java.util.concurrent.ExecutionException) {
                android.util.Slog.e(LOG_TAG, "Cannot create permission backup for user " + userId, e);
                return null;
            }
            throw e;
        }
    }

    public void restoreRuntimePermissions(byte[] backup, int userId) {
        if (backup == null) {
            throw new java.lang.IllegalArgumentException(com.android.server.am.HostingRecord.HOSTING_TYPE_BACKUP.toString());
        }
        com.android.internal.util.Preconditions.checkArgumentNonnegative(userId, "userId");
        synchronized (this.isDelayedPermissionBackupFinished) {
            android.util.SparseBooleanArray $this$minusAssign$iv = this.isDelayedPermissionBackupFinished;
            $this$minusAssign$iv.delete(userId);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
        android.permission.PermissionControllerManager permissionControllerManager = this.permissionControllerManager;
        if (permissionControllerManager == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("permissionControllerManager");
            permissionControllerManager = null;
        }
        permissionControllerManager.stageAndApplyRuntimePermissionsBackup(backup, android.os.UserHandle.of(userId));
    }

    public void restoreDelayedRuntimePermissions(java.lang.String packageName, final int userId) {
        if (packageName == null) {
            throw new java.lang.IllegalArgumentException(com.android.server.pm.verify.domain.DomainVerificationLegacySettings.ATTR_PACKAGE_NAME.toString());
        }
        com.android.internal.util.Preconditions.checkArgumentNonnegative(userId, "userId");
        synchronized (this.isDelayedPermissionBackupFinished) {
            if (this.isDelayedPermissionBackupFinished.get(userId, false)) {
                return;
            }
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            android.permission.PermissionControllerManager permissionControllerManager = this.permissionControllerManager;
            if (permissionControllerManager == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("permissionControllerManager");
                permissionControllerManager = null;
            }
            permissionControllerManager.applyStagedRuntimePermissionBackup(packageName, android.os.UserHandle.of(userId), com.android.server.PermissionThread.getExecutor(), new java.util.function.Consumer() { // from class: com.android.server.permission.access.permission.PermissionService.restoreDelayedRuntimePermissions.3
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Boolean hasMoreBackup) {
                    if (!hasMoreBackup.booleanValue()) {
                        android.util.SparseBooleanArray sparseBooleanArray = com.android.server.permission.access.permission.PermissionService.this.isDelayedPermissionBackupFinished;
                        com.android.server.permission.access.permission.PermissionService permissionService = com.android.server.permission.access.permission.PermissionService.this;
                        int i = userId;
                        synchronized (sparseBooleanArray) {
                            permissionService.isDelayedPermissionBackupFinished.put(i, true);
                            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                        }
                    }
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x002a  */
    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void dump(java.io.FileDescriptor r17, java.io.PrintWriter r18, java.lang.String[] r19) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 370
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.permission.access.permission.PermissionService.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    private final com.android.server.permission.access.immutable.IndexedMap<java.lang.Integer, com.android.server.permission.access.immutable.MutableIndexedSet<java.lang.String>> getAllAppIdPackageNames(com.android.server.permission.access.AccessState state) throws java.lang.Exception {
        java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> map;
        com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> indexedReferenceMap;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap;
        com.android.server.permission.access.immutable.MutableIndexedSet appIds = new com.android.server.permission.access.immutable.MutableIndexedSet(null, 1, null);
        com.android.server.pm.PackageManagerLocal packageManagerLocal = this.packageManagerLocal;
        if (packageManagerLocal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerLocal");
            packageManagerLocal = null;
        }
        com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot unfilteredSnapshotWithUnfilteredSnapshot = packageManagerLocal.withUnfilteredSnapshot();
        try {
            com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot it = unfilteredSnapshotWithUnfilteredSnapshot;
            java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> packageStates = it.getPackageStates();
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(unfilteredSnapshotWithUnfilteredSnapshot, null);
            java.util.Map<java.lang.String, com.android.server.pm.pkg.PackageState> map2 = packageStates;
            com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = state.getUserStates();
            int size = userStates.getSize();
            for (int index$iv = 0; index$iv < size; index$iv++) {
                userStates.keyAt(index$iv);
                com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
                com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> appIdPermissionFlags = userState.getAppIdPermissionFlags();
                int size2 = appIdPermissionFlags.getSize();
                for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
                    int appId = appIdPermissionFlags.keyAt(index$iv2);
                    appIds.add(java.lang.Integer.valueOf(appId));
                }
                com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> appIdAppOpModes = userState.getAppIdAppOpModes();
                int size3 = appIdAppOpModes.getSize();
                for (int index$iv3 = 0; index$iv3 < size3; index$iv3++) {
                    int appId2 = appIdAppOpModes.keyAt(index$iv3);
                    appIds.add(java.lang.Integer.valueOf(appId2));
                }
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> packageVersions = userState.getPackageVersions();
                int index$iv4 = 0;
                int size4 = packageVersions.getSize();
                while (index$iv4 < size4) {
                    java.lang.String strKeyAt = packageVersions.keyAt(index$iv4);
                    packageVersions.valueAt(index$iv4).intValue();
                    java.lang.String packageName = strKeyAt;
                    com.android.server.pm.pkg.PackageState packageState = map2.get(packageName);
                    if (packageState == null) {
                        indexedMap = packageVersions;
                    } else {
                        int appId3 = packageState.getAppId();
                        indexedMap = packageVersions;
                        appIds.add(java.lang.Integer.valueOf(appId3));
                    }
                    index$iv4++;
                    packageVersions = indexedMap;
                }
                com.android.server.permission.access.immutable.IndexedReferenceMap<java.lang.String, com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> packageAppOpModes = userState.getPackageAppOpModes();
                int index$iv5 = 0;
                int size5 = packageAppOpModes.getSize();
                while (index$iv5 < size5) {
                    java.lang.String strKeyAt2 = packageAppOpModes.keyAt(index$iv5);
                    java.lang.String packageName2 = strKeyAt2;
                    com.android.server.pm.pkg.PackageState packageState2 = map2.get(packageName2);
                    if (packageState2 == null) {
                        indexedReferenceMap = packageAppOpModes;
                    } else {
                        int appId4 = packageState2.getAppId();
                        indexedReferenceMap = packageAppOpModes;
                        appIds.add(java.lang.Integer.valueOf(appId4));
                    }
                    index$iv5++;
                    packageAppOpModes = indexedReferenceMap;
                }
            }
            com.android.server.permission.access.immutable.MutableIndexedMap appIdPackageNames = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
            java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.pm.pkg.PackageState>> it2 = map2.entrySet().iterator();
            while (it2.hasNext()) {
                com.android.server.pm.pkg.PackageState packageState3 = it2.next().getValue();
                if (packageState3.isApex()) {
                    map = map2;
                } else {
                    java.lang.Integer numValueOf = java.lang.Integer.valueOf(packageState3.getAppId());
                    java.lang.Object obj = appIdPackageNames.get(numValueOf);
                    if (obj == null) {
                        map = map2;
                        com.android.server.permission.access.immutable.MutableIndexedSet mutableIndexedSet = new com.android.server.permission.access.immutable.MutableIndexedSet(null, 1, null);
                        appIdPackageNames.put(numValueOf, mutableIndexedSet);
                        obj = mutableIndexedSet;
                    } else {
                        map = map2;
                    }
                    ((com.android.server.permission.access.immutable.MutableIndexedSet) obj).add(packageState3.getPackageName());
                }
                map2 = map;
            }
            com.android.server.permission.access.immutable.MutableIndexedSet $this$forEachIndexed$iv = appIds;
            int size6 = $this$forEachIndexed$iv.getSize();
            for (int index$iv6 = 0; index$iv6 < size6; index$iv6++) {
                int appId5 = ((java.lang.Number) $this$forEachIndexed$iv.elementAt(index$iv6)).intValue();
                java.lang.Integer numValueOf2 = java.lang.Integer.valueOf(appId5);
                if (appIdPackageNames.get(numValueOf2) == 0) {
                    appIdPackageNames.put(numValueOf2, new com.android.server.permission.access.immutable.MutableIndexedSet(null, 1, null));
                }
            }
            return appIdPackageNames;
        } finally {
        }
    }

    private final void dumpSystemState(android.util.IndentingPrintWriter $this$dumpSystemState, com.android.server.permission.access.AccessState state) {
        $this$dumpSystemState.println("Permissions:");
        com.android.server.permission.access.permission.PermissionService this_$iv = this;
        int $i$f$withIndent = 0;
        $this$dumpSystemState.increaseIndent();
        int i = 0;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = state.getSystemState().getPermissions();
        int $i$f$forEachIndexed = 0;
        int index$iv = 0;
        int size = permissions.getSize();
        while (index$iv < size) {
            permissions.keyAt(index$iv);
            com.android.server.permission.access.permission.Permission permission = permissions.valueAt(index$iv);
            java.lang.String protectionLevel = android.content.pm.PermissionInfo.protectionToString(permission.getPermissionInfo().protectionLevel);
            com.android.server.permission.access.permission.PermissionService this_$iv2 = this_$iv;
            java.lang.String str = permission.getPermissionInfo().name;
            java.lang.String strTypeToString = com.android.server.permission.access.permission.Permission.Companion.typeToString(permission.getType());
            int $i$f$withIndent2 = $i$f$withIndent;
            java.lang.String str2 = permission.getPermissionInfo().packageName;
            int appId = permission.getAppId();
            int i2 = i;
            java.lang.String string = java.util.Arrays.toString(permission.getGids());
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            $this$dumpSystemState.println(str + ": type=" + strTypeToString + ", packageName=" + str2 + ", appId=" + appId + ", gids=" + string + ", protectionLevel=[" + protectionLevel + "], flags=" + android.content.pm.PermissionInfo.flagsToString(permission.getPermissionInfo().flags));
            index$iv++;
            this_$iv = this_$iv2;
            permissions = permissions;
            $i$f$withIndent = $i$f$withIndent2;
            i = i2;
            $i$f$forEachIndexed = $i$f$forEachIndexed;
        }
        $this$dumpSystemState.decreaseIndent();
        $this$dumpSystemState.println("Permission groups:");
        com.android.server.permission.access.permission.PermissionService this_$iv3 = this;
        $this$dumpSystemState.increaseIndent();
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, android.content.pm.PermissionGroupInfo> permissionGroups = state.getSystemState().getPermissionGroups();
        int index$iv2 = 0;
        int size2 = permissionGroups.getSize();
        while (index$iv2 < size2) {
            permissionGroups.keyAt(index$iv2);
            android.content.pm.PermissionGroupInfo permissionGroup = permissionGroups.valueAt(index$iv2);
            $this$dumpSystemState.println(permissionGroup.name + ": packageName=" + permissionGroup.packageName);
            index$iv2++;
            this_$iv3 = this_$iv3;
        }
        $this$dumpSystemState.decreaseIndent();
        $this$dumpSystemState.println("Permission trees:");
        com.android.server.permission.access.permission.PermissionService this_$iv4 = this;
        $this$dumpSystemState.increaseIndent();
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissionTrees = state.getSystemState().getPermissionTrees();
        int index$iv3 = 0;
        int size3 = permissionTrees.getSize();
        while (index$iv3 < size3) {
            permissionTrees.keyAt(index$iv3);
            com.android.server.permission.access.permission.Permission permissionTree = permissionTrees.valueAt(index$iv3);
            com.android.server.permission.access.permission.PermissionService this_$iv5 = this_$iv4;
            $this$dumpSystemState.println(permissionTree.getPermissionInfo().name + ": packageName=" + permissionTree.getPermissionInfo().packageName + ", appId=" + permissionTree.getAppId());
            index$iv3++;
            this_$iv4 = this_$iv5;
        }
        $this$dumpSystemState.decreaseIndent();
    }

    private final void dumpAppIdState(android.util.IndentingPrintWriter $this$dumpAppIdState, int appId, com.android.server.permission.access.AccessState state, com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> intReferenceMap;
        int $i$f$forEachIndexed;
        int i;
        int i2 = appId;
        $this$dumpAppIdState.println("App ID: " + i2);
        com.android.server.permission.access.permission.PermissionService this_$iv = this;
        int $i$f$withIndent = 0;
        $this$dumpAppIdState.increaseIndent();
        android.util.IndentingPrintWriter $this$dumpAppIdState_u24lambda_u24168 = $this$dumpAppIdState;
        int i3 = 0;
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = state.getUserStates();
        int $i$f$forEachIndexed2 = 0;
        int index$iv = 0;
        int size = userStates.getSize();
        while (index$iv < size) {
            int userId = userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            int i4 = 0;
            $this$dumpAppIdState_u24lambda_u24168.println("User: " + userId);
            android.util.IndentingPrintWriter $this$withIndent$iv = $this$dumpAppIdState_u24lambda_u24168;
            $this$withIndent$iv.increaseIndent();
            com.android.server.permission.access.permission.PermissionService this_$iv2 = this_$iv;
            android.util.IndentingPrintWriter $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166 = $this$withIndent$iv;
            $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.println("Permissions:");
            $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.increaseIndent();
            int $i$f$withIndent2 = $i$f$withIndent;
            com.android.server.permission.access.immutable.IndexedMap $this$forEachIndexed$iv = (com.android.server.permission.access.immutable.IndexedMap) userState.getAppIdPermissionFlags().get(i2);
            android.util.IndentingPrintWriter $this$dumpAppIdState_u24lambda_u241682 = $this$dumpAppIdState_u24lambda_u24168;
            int i5 = i3;
            if ($this$forEachIndexed$iv == null) {
                intReferenceMap = userStates;
                $i$f$forEachIndexed = $i$f$forEachIndexed2;
                i = size;
            } else {
                intReferenceMap = userStates;
                int size2 = $this$forEachIndexed$iv.getSize();
                $i$f$forEachIndexed = $i$f$forEachIndexed2;
                int $i$f$forEachIndexed3 = 0;
                while ($i$f$forEachIndexed3 < size2) {
                    java.lang.Object objKeyAt = $this$forEachIndexed$iv.keyAt($i$f$forEachIndexed3);
                    com.android.server.permission.access.immutable.IndexedMap $this$forEachIndexed$iv2 = $this$forEachIndexed$iv;
                    int flags = ((java.lang.Number) $this$forEachIndexed$iv.valueAt($i$f$forEachIndexed3)).intValue();
                    java.lang.String permissionName = (java.lang.String) objKeyAt;
                    int i6 = size2;
                    boolean isGranted = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(flags);
                    $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.println(permissionName + ": granted=" + isGranted + ", flags=" + com.android.server.permission.access.permission.PermissionFlags.INSTANCE.toString(flags));
                    $i$f$forEachIndexed3++;
                    $this$forEachIndexed$iv = $this$forEachIndexed$iv2;
                    size2 = i6;
                    size = size;
                    userId = userId;
                }
                i = size;
            }
            $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.decreaseIndent();
            com.android.server.permission.access.immutable.IndexedReferenceMap $this$forEachIndexed$iv3 = (com.android.server.permission.access.immutable.IndexedReferenceMap) userState.getAppIdDevicePermissionFlags().get(i2);
            if ($this$forEachIndexed$iv3 != null) {
                int $i$f$forEachIndexed4 = 0;
                int index$iv2 = 0;
                int size3 = $this$forEachIndexed$iv3.getSize();
                while (index$iv2 < size3) {
                    java.lang.Object objKeyAt2 = $this$forEachIndexed$iv3.keyAt(index$iv2);
                    com.android.server.permission.access.immutable.IndexedMap devicePermissionFlags = (com.android.server.permission.access.immutable.IndexedMap) $this$forEachIndexed$iv3.valueAt(index$iv2);
                    java.lang.String deviceId = (java.lang.String) objKeyAt2;
                    com.android.server.permission.access.immutable.IndexedReferenceMap $this$forEachIndexed$iv4 = $this$forEachIndexed$iv3;
                    int $i$f$forEachIndexed5 = $i$f$forEachIndexed4;
                    $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.println("Permissions (Device " + deviceId + "):");
                    $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.increaseIndent();
                    com.android.server.permission.access.immutable.IndexedMap $this$forEachIndexed$iv5 = devicePermissionFlags;
                    int size4 = $this$forEachIndexed$iv5.getSize();
                    int i7 = size3;
                    int index$iv3 = 0;
                    while (index$iv3 < size4) {
                        int i8 = size4;
                        com.android.server.permission.access.immutable.IndexedMap $this$forEachIndexed$iv6 = $this$forEachIndexed$iv5;
                        java.lang.Object objKeyAt3 = $this$forEachIndexed$iv6.keyAt(index$iv3);
                        int flags2 = ((java.lang.Number) $this$forEachIndexed$iv6.valueAt(index$iv3)).intValue();
                        java.lang.String deviceId2 = deviceId;
                        java.lang.String permissionName2 = (java.lang.String) objKeyAt3;
                        com.android.server.permission.access.immutable.IndexedMap devicePermissionFlags2 = devicePermissionFlags;
                        boolean isGranted2 = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(flags2);
                        $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.println(permissionName2 + ": granted=" + isGranted2 + ", flags=" + com.android.server.permission.access.permission.PermissionFlags.INSTANCE.toString(flags2));
                        index$iv3++;
                        size4 = i8;
                        deviceId = deviceId2;
                        $this$forEachIndexed$iv5 = $this$forEachIndexed$iv6;
                        devicePermissionFlags = devicePermissionFlags2;
                        i4 = i4;
                    }
                    $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.decreaseIndent();
                    index$iv2++;
                    $this$forEachIndexed$iv3 = $this$forEachIndexed$iv4;
                    $i$f$forEachIndexed4 = $i$f$forEachIndexed5;
                    size3 = i7;
                    i4 = i4;
                }
            }
            java.lang.String str = "App ops:";
            $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.println("App ops:");
            int $i$f$withIndent3 = 0;
            $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.increaseIndent();
            int i9 = 0;
            com.android.server.permission.access.immutable.IndexedMap $this$forEachIndexed$iv7 = (com.android.server.permission.access.immutable.IndexedMap) userState.getAppIdAppOpModes().get(i2);
            if ($this$forEachIndexed$iv7 != null) {
                int size5 = $this$forEachIndexed$iv7.getSize();
                int index$iv4 = 0;
                while (index$iv4 < size5) {
                    java.lang.Object objKeyAt4 = $this$forEachIndexed$iv7.keyAt(index$iv4);
                    int appOpMode = ((java.lang.Number) $this$forEachIndexed$iv7.valueAt(index$iv4)).intValue();
                    java.lang.String appOpName = (java.lang.String) objKeyAt4;
                    $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.println(appOpName + ": mode=" + android.app.AppOpsManager.modeToName(appOpMode));
                    index$iv4++;
                    size5 = size5;
                    $i$f$withIndent3 = $i$f$withIndent3;
                    i9 = i9;
                }
            }
            $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.decreaseIndent();
            if (indexedSet != null) {
                com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet2 = indexedSet;
                int $i$f$forEachIndexed6 = 0;
                int index$iv5 = 0;
                int size6 = indexedSet2.getSize();
                while (index$iv5 < size6) {
                    java.lang.String packageName = indexedSet2.elementAt(index$iv5);
                    $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166.println("Package: " + packageName);
                    android.util.IndentingPrintWriter $this$withIndent$iv2 = $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166;
                    $this$withIndent$iv2.increaseIndent();
                    com.android.server.permission.access.immutable.IndexedSet<java.lang.String> indexedSet3 = indexedSet2;
                    android.util.IndentingPrintWriter $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u241662 = $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166;
                    int $i$f$forEachIndexed7 = $i$f$forEachIndexed6;
                    $this$withIndent$iv2.println("version=" + userState.getPackageVersions().get(packageName));
                    $this$withIndent$iv2.println(str);
                    $this$withIndent$iv2.increaseIndent();
                    java.lang.String str2 = str;
                    com.android.server.permission.access.immutable.IndexedMap $this$forEachIndexed$iv8 = (com.android.server.permission.access.immutable.IndexedMap) userState.getPackageAppOpModes().get(packageName);
                    if ($this$forEachIndexed$iv8 != null) {
                        int size7 = $this$forEachIndexed$iv8.getSize();
                        int index$iv6 = 0;
                        while (index$iv6 < size7) {
                            java.lang.Object objKeyAt5 = $this$forEachIndexed$iv8.keyAt(index$iv6);
                            int appOpMode2 = ((java.lang.Number) $this$forEachIndexed$iv8.valueAt(index$iv6)).intValue();
                            com.android.server.permission.access.immutable.IndexedMap $this$forEachIndexed$iv9 = $this$forEachIndexed$iv8;
                            java.lang.String appOpName2 = (java.lang.String) objKeyAt5;
                            int i10 = size7;
                            java.lang.String modeName = android.app.AppOpsManager.modeToName(appOpMode2);
                            $this$withIndent$iv2.println(appOpName2 + ": mode=" + modeName);
                            index$iv6++;
                            $this$forEachIndexed$iv8 = $this$forEachIndexed$iv9;
                            size7 = i10;
                            size6 = size6;
                        }
                    }
                    $this$withIndent$iv2.decreaseIndent();
                    $this$withIndent$iv2.decreaseIndent();
                    index$iv5++;
                    indexedSet2 = indexedSet3;
                    $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u24166 = $this$dumpAppIdState_u24lambda_u24168_u24lambda_u24167_u24lambda_u241662;
                    $i$f$forEachIndexed6 = $i$f$forEachIndexed7;
                    str = str2;
                    size6 = size6;
                }
            }
            $this$withIndent$iv.decreaseIndent();
            index$iv++;
            i2 = appId;
            this_$iv = this_$iv2;
            $i$f$withIndent = $i$f$withIndent2;
            $this$dumpAppIdState_u24lambda_u24168 = $this$dumpAppIdState_u24lambda_u241682;
            i3 = i5;
            userStates = intReferenceMap;
            $i$f$forEachIndexed2 = $i$f$forEachIndexed;
            size = i;
        }
        $this$dumpAppIdState.decreaseIndent();
    }

    private final void withIndent(android.util.IndentingPrintWriter $this$withIndent, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super android.util.IndentingPrintWriter, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        $this$withIndent.increaseIndent();
        function1.invoke($this$withIndent);
        $this$withIndent.decreaseIndent();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.Permission getPermissionTEMP(java.lang.String permissionName) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getPermissionTEMP_u24lambda_u24170 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getPermissionTEMP_u24lambda_u24170_u24lambda_u24169 = this.policy;
        com.android.server.permission.access.permission.Permission permission = $this$getPermissionTEMP_u24lambda_u24170_u24lambda_u24169.getPermissions($this$getPermissionTEMP_u24lambda_u24170).get(permissionName);
        if (permission == null) {
            return null;
        }
        return new com.android.server.pm.permission.Permission(permission.getPermissionInfo(), permission.getType(), permission.isReconciled(), permission.getAppId(), permission.getGids(), permission.getAreGidsPerUser());
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.util.List<com.android.server.pm.permission.LegacyPermission> getLegacyPermissions() {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getLegacyPermissions_u24lambda_u24172 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getLegacyPermissions_u24lambda_u24172_u24lambda_u24171 = this.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$getLegacyPermissions_u24lambda_u24172_u24lambda_u24171.getPermissions($this$getLegacyPermissions_u24lambda_u24172);
        java.util.Collection destination$iv = new java.util.ArrayList();
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> indexedMap = permissions;
        int $i$f$mapIndexedTo = 0;
        int index$iv$iv = 0;
        int size = indexedMap.getSize();
        while (index$iv$iv < size) {
            java.lang.Object key$iv = indexedMap.keyAt(index$iv$iv);
            java.lang.Object value$iv = indexedMap.valueAt(index$iv$iv);
            com.android.server.permission.access.permission.Permission permission = (com.android.server.permission.access.permission.Permission) value$iv;
            destination$iv.add(new com.android.server.pm.permission.LegacyPermission(permission.getPermissionInfo(), permission.getType(), permission.getAppId(), permission.getGids()));
            index$iv$iv++;
            indexedMap = indexedMap;
            $i$f$mapIndexedTo = $i$f$mapIndexedTo;
        }
        return (java.util.List) destination$iv;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) throws java.lang.Exception {
        this.service.initialize();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionsTEMP(com.android.server.pm.permission.LegacyPermissionSettings legacyPermissionSettings) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$writeLegacyPermissionsTEMP_u24lambda_u24176 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$writeLegacyPermissionsTEMP_u24lambda_u24176_u24lambda_u24174 = this.policy;
        legacyPermissionSettings.replacePermissions(toLegacyPermissions($this$writeLegacyPermissionsTEMP_u24lambda_u24176_u24lambda_u24174.getPermissions($this$writeLegacyPermissionsTEMP_u24lambda_u24176)));
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$writeLegacyPermissionsTEMP_u24lambda_u24176_u24lambda_u24175 = this.policy;
        legacyPermissionSettings.replacePermissionTrees(toLegacyPermissions($this$writeLegacyPermissionsTEMP_u24lambda_u24176_u24lambda_u24175.getPermissionTrees($this$writeLegacyPermissionsTEMP_u24lambda_u24176)));
    }

    private final java.util.List<com.android.server.pm.permission.LegacyPermission> toLegacyPermissions(com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> indexedMap) {
        java.util.Collection destination$iv = new java.util.ArrayList();
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> indexedMap2 = indexedMap;
        int $i$f$mapIndexedTo = 0;
        int index$iv$iv = 0;
        int size = indexedMap2.getSize();
        while (index$iv$iv < size) {
            java.lang.Object key$iv = indexedMap2.keyAt(index$iv$iv);
            java.lang.Object value$iv = indexedMap2.valueAt(index$iv$iv);
            com.android.server.permission.access.permission.Permission permission = (com.android.server.permission.access.permission.Permission) value$iv;
            destination$iv.add(new com.android.server.pm.permission.LegacyPermission(permission.getPermissionInfo(), permission.getType(), 0, libcore.util.EmptyArray.INT));
            index$iv$iv++;
            indexedMap2 = indexedMap2;
            $i$f$mapIndexedTo = $i$f$mapIndexedTo;
        }
        return (java.util.List) destination$iv;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public com.android.server.pm.permission.LegacyPermissionState getLegacyPermissionState(int appId) {
        int[] userIds;
        com.android.server.permission.access.GetStateScope $this$getLegacyPermissionState_u24lambda_u24182;
        com.android.server.permission.access.AccessCheckingService this_$iv;
        int $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar;
        int i;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> indexedMap;
        com.android.server.permission.access.AccessCheckingService this_$iv2;
        int $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2;
        int i2;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> indexedMap2;
        com.android.server.permission.access.permission.PermissionService permissionService = this;
        com.android.server.pm.permission.LegacyPermissionState legacyState = new com.android.server.pm.permission.LegacyPermissionState();
        com.android.server.pm.UserManagerService userManagerService = permissionService.userManagerService;
        com.android.server.permission.access.AccessState accessState = null;
        if (userManagerService == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerService");
            userManagerService = null;
        }
        int[] userIds2 = userManagerService.getUserIdsIncludingPreCreated();
        com.android.server.permission.access.AccessCheckingService this_$iv3 = permissionService.service;
        int $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3 = 0;
        com.android.server.permission.access.AccessState accessState2 = this_$iv3.state;
        if (accessState2 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
        } else {
            accessState = accessState2;
        }
        com.android.server.permission.access.GetStateScope $this$getLegacyPermissionState_u24lambda_u241822 = new com.android.server.permission.access.GetStateScope(accessState);
        int i3 = 0;
        com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getLegacyPermissionState_u24lambda_u24182_u24lambda_u24178 = permissionService.policy;
        com.android.server.permission.access.immutable.IndexedMap<java.lang.String, com.android.server.permission.access.permission.Permission> permissions = $this$getLegacyPermissionState_u24lambda_u24182_u24lambda_u24178.getPermissions($this$getLegacyPermissionState_u24lambda_u241822);
        int index$iv = 0;
        int length = userIds2.length;
        int i4 = 0;
        while (i4 < length) {
            int item$iv = userIds2[i4];
            index$iv++;
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$getLegacyPermissionState_u24lambda_u24182_u24lambda_u24181_u24lambda_u24179 = permissionService.policy;
            com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> uidPermissionFlags = $this$getLegacyPermissionState_u24lambda_u24182_u24lambda_u24181_u24lambda_u24179.getUidPermissionFlags($this$getLegacyPermissionState_u24lambda_u241822, appId, item$iv);
            if (uidPermissionFlags == null) {
                userIds = userIds2;
                $this$getLegacyPermissionState_u24lambda_u24182 = $this$getLegacyPermissionState_u24lambda_u241822;
                this_$iv = this_$iv3;
                $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar = $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3;
                i = i3;
                indexedMap = permissions;
            } else {
                com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap3 = uidPermissionFlags;
                int size = indexedMap3.getSize();
                userIds = userIds2;
                int index$iv2 = 0;
                while (index$iv2 < size) {
                    int i5 = size;
                    com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> indexedMap4 = indexedMap3;
                    java.lang.String strKeyAt = indexedMap4.keyAt(index$iv2);
                    int flags = indexedMap4.valueAt(index$iv2).intValue();
                    com.android.server.permission.access.GetStateScope $this$getLegacyPermissionState_u24lambda_u241823 = $this$getLegacyPermissionState_u24lambda_u241822;
                    java.lang.String permissionName = strKeyAt;
                    com.android.server.permission.access.permission.Permission permission = permissions.get(permissionName);
                    if (permission == null) {
                        this_$iv2 = this_$iv3;
                        $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2 = $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3;
                        i2 = i3;
                        indexedMap2 = permissions;
                    } else {
                        this_$iv2 = this_$iv3;
                        $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2 = $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3;
                        int $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar4 = permission.getPermissionInfo().getProtection();
                        i2 = i3;
                        indexedMap2 = permissions;
                        com.android.server.pm.permission.LegacyPermissionState.PermissionState legacyPermissionState = new com.android.server.pm.permission.LegacyPermissionState.PermissionState(permissionName, $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar4 == 1, com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(flags), com.android.server.permission.access.permission.PermissionFlags.INSTANCE.toApiFlags(flags));
                        legacyState.putPermissionState(legacyPermissionState, item$iv);
                    }
                    index$iv2++;
                    size = i5;
                    $this$getLegacyPermissionState_u24lambda_u241822 = $this$getLegacyPermissionState_u24lambda_u241823;
                    indexedMap3 = indexedMap4;
                    this_$iv3 = this_$iv2;
                    permissions = indexedMap2;
                    i3 = i2;
                    $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3 = $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar2;
                }
                $this$getLegacyPermissionState_u24lambda_u24182 = $this$getLegacyPermissionState_u24lambda_u241822;
                this_$iv = this_$iv3;
                $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar = $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3;
                i = i3;
                indexedMap = permissions;
            }
            i4++;
            permissionService = this;
            userIds2 = userIds;
            $this$getLegacyPermissionState_u24lambda_u241822 = $this$getLegacyPermissionState_u24lambda_u24182;
            this_$iv3 = this_$iv;
            permissions = indexedMap;
            i3 = i;
            $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar3 = $i$f$getState$frameworks__base__services__permission__android_common__services_permission_pre_jarjar;
        }
        return legacyState;
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void readLegacyPermissionStateTEMP() {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void writeLegacyPermissionStateTEMP() {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public java.lang.String getDefaultPermissionGrantFingerprint(int userId) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        com.android.server.permission.access.AccessState accessState = this_$iv.state;
        if (accessState == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
            accessState = null;
        }
        com.android.server.permission.access.GetStateScope $this$getDefaultPermissionGrantFingerprint_u24lambda_u24183 = new com.android.server.permission.access.GetStateScope(accessState);
        com.android.server.permission.access.immutable.Immutable immutable = $this$getDefaultPermissionGrantFingerprint_u24lambda_u24183.getState().getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        return ((com.android.server.permission.access.UserState) immutable).getDefaultPermissionGrantFingerprint();
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void setDefaultPermissionGrantFingerprint(java.lang.String fingerprint, int userId) {
        com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
        synchronized (this_$iv.stateLock) {
            com.android.server.permission.access.AccessState oldState$iv = this_$iv.state;
            if (oldState$iv == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                oldState$iv = null;
            }
            com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
            com.android.server.permission.access.MutateStateScope $this$setDefaultPermissionGrantFingerprint_u24lambda_u24184 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
            com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$setDefaultPermissionGrantFingerprint_u24lambda_u24184.getNewState(), userId, 0, 2, null);
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default);
            mutableUserStateMutateUserState$default.setDefaultPermissionGrantFingerprintPublic(fingerprint);
            this_$iv.persistence.write(newState$iv);
            this_$iv.state = newState$iv;
            com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
            $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onSystemReady() {
        java.util.Set<java.lang.String> allPersistentDeviceIds;
        this.service.onSystemReady$frameworks__base__services__permission__android_common__services_permission_pre_jarjar();
        this.virtualDeviceManagerInternal = (com.android.server.companion.virtual.VirtualDeviceManagerInternal) com.android.server.LocalServices.getService(com.android.server.companion.virtual.VirtualDeviceManagerInternal.class);
        com.android.server.companion.virtual.VirtualDeviceManagerInternal virtualDeviceManagerInternal = this.virtualDeviceManagerInternal;
        if (virtualDeviceManagerInternal != null && (allPersistentDeviceIds = virtualDeviceManagerInternal.getAllPersistentDeviceIds()) != null) {
            com.android.server.permission.access.AccessCheckingService this_$iv = this.service;
            synchronized (this_$iv.stateLock) {
                com.android.server.permission.access.AccessState oldState$iv = this_$iv.state;
                if (oldState$iv == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                    oldState$iv = null;
                }
                com.android.server.permission.access.MutableAccessState newState$iv = oldState$iv.toMutable();
                com.android.server.permission.access.MutateStateScope $this$onSystemReady_u24lambda_u24187_u24lambda_u24186 = new com.android.server.permission.access.MutateStateScope(oldState$iv, newState$iv);
                com.android.server.permission.access.permission.DevicePermissionPolicy $this$onSystemReady_u24lambda_u24187_u24lambda_u24186_u24lambda_u24185 = this.devicePolicy;
                $this$onSystemReady_u24lambda_u24187_u24lambda_u24186_u24lambda_u24185.trimDevicePermissionStates($this$onSystemReady_u24lambda_u24187_u24lambda_u24186, allPersistentDeviceIds);
                this_$iv.persistence.write(newState$iv);
                this_$iv.state = newState$iv;
                com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv = this_$iv.policy;
                $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv));
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            }
        }
        com.android.server.companion.virtual.VirtualDeviceManagerInternal virtualDeviceManagerInternal2 = this.virtualDeviceManagerInternal;
        if (virtualDeviceManagerInternal2 != null) {
            virtualDeviceManagerInternal2.registerPersistentDeviceIdRemovedListener(new java.util.function.Consumer() { // from class: com.android.server.permission.access.permission.PermissionService.onSystemReady.2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.String deviceId) {
                    com.android.server.permission.access.AccessCheckingService this_$iv2 = com.android.server.permission.access.permission.PermissionService.this.service;
                    com.android.server.permission.access.permission.PermissionService permissionService = com.android.server.permission.access.permission.PermissionService.this;
                    synchronized (this_$iv2.stateLock) {
                        com.android.server.permission.access.AccessState oldState$iv2 = this_$iv2.state;
                        if (oldState$iv2 == null) {
                            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                            oldState$iv2 = null;
                        }
                        com.android.server.permission.access.MutableAccessState newState$iv2 = oldState$iv2.toMutable();
                        com.android.server.permission.access.MutateStateScope $this$accept_u24lambda_u241 = new com.android.server.permission.access.MutateStateScope(oldState$iv2, newState$iv2);
                        com.android.server.permission.access.permission.DevicePermissionPolicy $this$accept_u24lambda_u241_u24lambda_u240 = permissionService.devicePolicy;
                        $this$accept_u24lambda_u241_u24lambda_u240.onDeviceIdRemoved($this$accept_u24lambda_u241, deviceId);
                        this_$iv2.persistence.write(newState$iv2);
                        this_$iv2.state = newState$iv2;
                        com.android.server.permission.access.AccessPolicy $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv2 = this_$iv2.policy;
                        $this$mutateState_u24lambda_u2426_u24lambda_u2425$iv2.onStateMutated(new com.android.server.permission.access.GetStateScope(newState$iv2));
                        com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                    }
                }
            });
        }
        this.permissionControllerManager = new android.permission.PermissionControllerManager(this.context, com.android.server.PermissionThread.getHandler());
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserCreated(int userId) {
        android.content.pm.PackageManager.corkPackageInfoCache();
        try {
            this.service.onUserAdded$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(userId);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        } finally {
            android.content.pm.PackageManager.uncorkPackageInfoCache();
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onUserRemoved(int userId) {
        this.service.onUserRemoved$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(userId);
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onStorageVolumeMounted(java.lang.String volumeUuid, boolean fingerprintChanged) {
        java.util.List<java.lang.String> list;
        synchronized (this.storageVolumeLock) {
            java.util.List<java.lang.String> listRemove = this.storageVolumePackageNames.remove(volumeUuid);
            if (listRemove == null) {
                listRemove = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.emptyList();
            }
            list = listRemove;
            this.mountedStorageVolumes.add(volumeUuid);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
        android.content.pm.PackageManager.corkPackageInfoCache();
        try {
            this.service.onStorageVolumeMounted$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(volumeUuid, list, fingerprintChanged);
            com.android.server.permission.jarjar.kotlin.Unit unit2 = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        } finally {
            android.content.pm.PackageManager.uncorkPackageInfoCache();
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageAdded(com.android.server.pm.pkg.PackageState packageState, boolean isInstantApp, com.android.server.pm.pkg.AndroidPackage oldPackage) throws java.lang.Exception {
        if (packageState.isApex()) {
            return;
        }
        synchronized (this.storageVolumeLock) {
            android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> arrayMap = this.storageVolumePackageNames;
            java.lang.String volumeUuid = packageState.getVolumeUuid();
            java.lang.Object it$iv = arrayMap.get(volumeUuid);
            if (it$iv == null) {
                java.util.ArrayList arrayList = new java.util.ArrayList();
                arrayMap.put(volumeUuid, arrayList);
                it$iv = arrayList;
            }
            ((java.util.Collection) it$iv).add(packageState.getPackageName());
            if (this.mountedStorageVolumes.contains(packageState.getVolumeUuid())) {
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                this.service.onPackageAdded$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(packageState.getPackageName());
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageRemoved(com.android.server.pm.pkg.AndroidPackage androidPackage) {
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageInstalled(com.android.server.pm.pkg.AndroidPackage androidPackage, int previousAppId, com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams params, int userId) throws java.lang.Exception {
        int[] userIds;
        if (androidPackage.isApex() || params == com.android.server.pm.permission.PermissionManagerServiceInternal.PackageInstalledParams.DEFAULT) {
            return;
        }
        synchronized (this.storageVolumeLock) {
            if (this.mountedStorageVolumes.contains(androidPackage.getVolumeUuid())) {
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
                if (userId == -1) {
                    com.android.server.pm.UserManagerService userManagerService = this.userManagerService;
                    if (userManagerService == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerService");
                        userManagerService = null;
                    }
                    userIds = userManagerService.getUserIdsIncludingPreCreated();
                } else {
                    userIds = new int[]{userId};
                }
                for (int i : userIds) {
                    this.service.onPackageInstalled$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(androidPackage.getPackageName(), i);
                }
                int[] $this$forEach$iv = userIds;
                for (int element$iv : $this$forEach$iv) {
                    android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
                    if (packageManagerInternal == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
                        packageManagerInternal = null;
                    }
                    com.android.server.pm.pkg.PackageStateInternal packageState = packageManagerInternal.getPackageStateInternal(androidPackage.getPackageName());
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(packageState);
                    addAllowlistedRestrictedPermissionsUnchecked(androidPackage, packageState.getAppId(), params.getAllowlistedRestrictedPermissions(), element$iv);
                    setRequestedPermissionStates(packageState, element$iv, params.getPermissionStates());
                }
            }
        }
    }

    @Override // com.android.server.pm.permission.PermissionManagerServiceInterface
    public void onPackageUninstalled(java.lang.String packageName, int appId, com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage androidPackage, java.util.List<? extends com.android.server.pm.pkg.AndroidPackage> list, int userId) throws java.lang.Exception {
        int[] userIds;
        if (packageState.isApex()) {
            return;
        }
        android.content.pm.PackageManagerInternal packageManagerInternal = null;
        if (userId == -1) {
            com.android.server.pm.UserManagerService userManagerService = this.userManagerService;
            if (userManagerService == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerService");
                userManagerService = null;
            }
            userIds = userManagerService.getUserIdsIncludingPreCreated();
        } else {
            userIds = new int[]{userId};
        }
        int[] $this$forEach$iv = userIds;
        for (int element$iv : $this$forEach$iv) {
            this.service.onPackageUninstalled$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(packageName, appId, element$iv);
        }
        android.content.pm.PackageManagerInternal packageManagerInternal2 = this.packageManagerInternal;
        if (packageManagerInternal2 == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
        } else {
            packageManagerInternal = packageManagerInternal2;
        }
        com.android.server.pm.pkg.PackageStateInternal packageState2 = packageManagerInternal.getPackageStates().get(packageName);
        if (packageState2 == null) {
            this.service.onPackageRemoved$frameworks__base__services__permission__android_common__services_permission_pre_jarjar(packageName, appId);
        }
    }

    private final <T> T withCorkedPackageInfoCache(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends T> function0) {
        android.content.pm.PackageManager.corkPackageInfoCache();
        try {
            return function0.invoke();
        } finally {
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
            android.content.pm.PackageManager.uncorkPackageInfoCache();
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }

    private final boolean isRootOrSystemUid(int uid) {
        switch (android.os.UserHandle.getAppId(uid)) {
            case 0:
            case 1000:
                return true;
            default:
                return false;
        }
    }

    private final boolean isShellUid(int uid) {
        return android.os.UserHandle.getAppId(uid) == 2000;
    }

    private final boolean isRootOrSystemOrShellUid(int uid) {
        return isRootOrSystemUid(uid) || isShellUid(uid);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void killUid(int uid, java.lang.String reason) {
        android.app.IActivityManager activityManager = android.app.ActivityManager.getService();
        if (activityManager != null) {
            int appId = android.os.UserHandle.getAppId(uid);
            int userId = android.os.UserHandle.getUserId(uid);
            long token$iv = android.os.Binder.clearCallingIdentity();
            try {
                try {
                    activityManager.killUidForPermissionChange(appId, userId, reason);
                } catch (android.os.RemoteException e) {
                }
                com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
            } finally {
                android.os.Binder.restoreCallingIdentity(token$iv);
            }
        }
    }

    private final com.android.server.pm.PackageManagerLocal.FilteredSnapshot withFilteredSnapshot(com.android.server.pm.PackageManagerLocal $this$withFilteredSnapshot, int callingUid, int userId) {
        return $this$withFilteredSnapshot.withFilteredSnapshot(callingUid, android.os.UserHandle.of(userId));
    }

    private final com.android.server.pm.pkg.PackageState getPackageState(com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot $this$getPackageState, java.lang.String packageName) {
        return $this$getPackageState.getPackageStates().get(packageName);
    }

    private final boolean isUidInstantApp(com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot $this$isUidInstantApp, int uid) {
        android.content.pm.PackageManagerInternal packageManagerInternal = this.packageManagerInternal;
        if (packageManagerInternal == null) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("packageManagerInternal");
            packageManagerInternal = null;
        }
        return packageManagerInternal.getInstantAppPackageName(uid) != null;
    }

    private final boolean isPackageVisibleToUid(com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot $this$isPackageVisibleToUid, java.lang.String packageName, int uid) {
        return isPackageVisibleToUid($this$isPackageVisibleToUid, packageName, android.os.UserHandle.getUserId(uid), uid);
    }

    private final boolean isPackageVisibleToUid(com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot $this$isPackageVisibleToUid, java.lang.String packageName, int userId, int uid) throws java.lang.Exception {
        com.android.server.pm.PackageManagerLocal.FilteredSnapshot filteredSnapshotFiltered = filtered($this$isPackageVisibleToUid, uid, userId);
        try {
            com.android.server.pm.PackageManagerLocal.FilteredSnapshot it = filteredSnapshotFiltered;
            boolean z = it.getPackageState(packageName) != null;
            com.android.server.permission.jarjar.kotlin.jdk7.AutoCloseableKt.closeFinally(filteredSnapshotFiltered, null);
            return z;
        } finally {
        }
    }

    private final com.android.server.pm.PackageManagerLocal.FilteredSnapshot filtered(com.android.server.pm.PackageManagerLocal.UnfilteredSnapshot $this$filtered, int callingUid, int userId) {
        return $this$filtered.filtered(callingUid, android.os.UserHandle.of(userId));
    }

    private final void enforceCallingOrSelfCrossUserPermission(int userId, boolean enforceFullPermission, boolean enforceShellRestriction, java.lang.String message) {
        java.lang.String permissionName;
        if (!(userId >= 0)) {
            throw new java.lang.IllegalArgumentException(("userId " + userId + " is invalid").toString());
        }
        int callingUid = android.os.Binder.getCallingUid();
        int callingUserId = android.os.UserHandle.getUserId(callingUid);
        if (userId != callingUserId) {
            if (enforceFullPermission) {
                permissionName = "android.permission.INTERACT_ACROSS_USERS_FULL";
            } else {
                permissionName = "android.permission.INTERACT_ACROSS_USERS";
            }
            if (this.context.checkCallingOrSelfPermission(permissionName) != 0) {
                java.lang.StringBuilder $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200 = new java.lang.StringBuilder();
                if (message != null) {
                    $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append(message);
                    $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append(": ");
                }
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append("Neither user ");
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append(callingUid);
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append(" nor current process has ");
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append(permissionName);
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append(" to access user ");
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.append(userId);
                java.lang.String exceptionMessage = $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24200.toString();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(exceptionMessage, "toString(...)");
                throw new java.lang.SecurityException(exceptionMessage);
            }
        }
        if (enforceShellRestriction && isShellUid(callingUid)) {
            com.android.server.pm.UserManagerInternal userManagerInternal = this.userManagerInternal;
            if (userManagerInternal == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("userManagerInternal");
                userManagerInternal = null;
            }
            boolean isShellRestricted = userManagerInternal.hasUserRestriction("no_debugging_features", userId);
            if (isShellRestricted) {
                java.lang.StringBuilder $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24201 = new java.lang.StringBuilder();
                if (message != null) {
                    $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24201.append(message);
                    $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24201.append(": ");
                }
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24201.append("Shell is disallowed to access user ");
                $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24201.append(userId);
                java.lang.String exceptionMessage2 = $this$enforceCallingOrSelfCrossUserPermission_u24lambda_u24201.toString();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(exceptionMessage2, "toString(...)");
                throw new java.lang.SecurityException(exceptionMessage2);
            }
        }
    }

    private final void enforceCallingOrSelfAnyPermission(java.lang.String message, java.lang.String... permissionNames) {
        int length = permissionNames.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            if (this.context.checkCallingOrSelfPermission(permissionNames[i]) == 0) {
                z = true;
                break;
            }
            i++;
        }
        boolean hasAnyPermission = z;
        if (!hasAnyPermission) {
            java.lang.StringBuilder $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203 = new java.lang.StringBuilder();
            if (message != null) {
                $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203.append(message);
                $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203.append(": ");
            }
            $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203.append("Neither user ");
            $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203.append(android.os.Binder.getCallingUid());
            $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203.append(" nor current process has any of ");
            com.android.server.permission.jarjar.kotlin.collections.ArraysKt.joinTo(permissionNames, $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203, (50 & 2) != 0 ? ", " : ", ", (50 & 4) != 0 ? "" : null, (50 & 8) != 0 ? "" : null, (50 & 16) != 0 ? -1 : 0, (50 & 32) != 0 ? "..." : null, (50 & 64) != 0 ? null : null);
            java.lang.String exceptionMessage = $this$enforceCallingOrSelfAnyPermission_u24lambda_u24203.toString();
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(exceptionMessage, "toString(...)");
            throw new java.lang.SecurityException(exceptionMessage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: PermissionService.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0082\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u001f\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\u0012H\u0002¢\u0006\u0002\u0010\u0015J\u0010\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0012H\u0002J8\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u0012H\u0016J0\u0010\u001f\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u0012H\u0016J\b\u0010 \u001a\u00020\u0019H\u0016J\u0012\u0010!\u001a\u00020\u0019*\u00020\"2\u0006\u0010#\u001a\u00020\u000bJ\n\u0010$\u001a\u00020\u0019*\u00020\"R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u000e0\rX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006%"}, d2 = {"Lcom/android/server/permission/access/permission/PermissionService$OnPermissionFlagsChangedListener;", "Lcom/android/server/permission/access/permission/AppIdPermissionPolicy$OnPermissionFlagsChangedListener;", "Lcom/android/server/permission/access/permission/DevicePermissionPolicy$OnDevicePermissionFlagsChangedListener;", "(Lcom/android/server/permission/access/permission/PermissionService;)V", "gidsChangedUids", "Lcom/android/server/permission/access/immutable/MutableIntSet;", "isKillRuntimePermissionRevokedUidsSkipped", "", "isPermissionFlagsChanged", "killRuntimePermissionRevokedUidsReasons", "Landroid/util/ArraySet;", "", "runtimePermissionChangedUidDevices", "Lcom/android/server/permission/access/immutable/MutableIntMap;", "", "runtimePermissionRevokedUids", "Landroid/util/SparseBooleanArray;", "getSecureInt", "", "settingName", "userId", "(Ljava/lang/String;I)Ljava/lang/Integer;", "isAppBackupAndRestoreRunning", "uid", "onDevicePermissionFlagsChanged", "", "appId", "deviceId", "permissionName", "oldFlags", "newFlags", "onPermissionFlagsChanged", "onStateMutated", "addKillRuntimePermissionRevokedUidsReason", "Lcom/android/server/permission/access/MutateStateScope;", com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY, "skipKillRuntimePermissionRevokedUids", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    final class OnPermissionFlagsChangedListener implements com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener, com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener {
        private boolean isKillRuntimePermissionRevokedUidsSkipped;
        private boolean isPermissionFlagsChanged;
        private final com.android.server.permission.access.immutable.MutableIntMap<java.util.Set<java.lang.String>> runtimePermissionChangedUidDevices = new com.android.server.permission.access.immutable.MutableIntMap<>(null, 1, null);
        private final android.util.SparseBooleanArray runtimePermissionRevokedUids = new android.util.SparseBooleanArray();
        private final com.android.server.permission.access.immutable.MutableIntSet gidsChangedUids = new com.android.server.permission.access.immutable.MutableIntSet(null, 1, null);
        private final android.util.ArraySet<java.lang.String> killRuntimePermissionRevokedUidsReasons = new android.util.ArraySet<>();

        public OnPermissionFlagsChangedListener() {
        }

        public final void skipKillRuntimePermissionRevokedUids(com.android.server.permission.access.MutateStateScope $this$skipKillRuntimePermissionRevokedUids) {
            this.isKillRuntimePermissionRevokedUidsSkipped = true;
        }

        public final void addKillRuntimePermissionRevokedUidsReason(com.android.server.permission.access.MutateStateScope $this$addKillRuntimePermissionRevokedUidsReason, java.lang.String reason) {
            this.killRuntimePermissionRevokedUidsReasons.add(reason);
        }

        @Override // com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener
        public void onPermissionFlagsChanged(int appId, int userId, java.lang.String permissionName, int oldFlags, int newFlags) {
            onDevicePermissionFlagsChanged(appId, userId, "default:0", permissionName, oldFlags, newFlags);
        }

        @Override // com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener
        public void onDevicePermissionFlagsChanged(int appId, int userId, java.lang.String deviceId, java.lang.String permissionName, int oldFlags, int newFlags) {
            this.isPermissionFlagsChanged = true;
            int uid = android.os.UserHandle.getUid(userId, appId);
            com.android.server.permission.access.AccessCheckingService this_$iv = com.android.server.permission.access.permission.PermissionService.this.service;
            com.android.server.permission.access.permission.PermissionService permissionService = com.android.server.permission.access.permission.PermissionService.this;
            com.android.server.permission.access.AccessState accessState = this_$iv.state;
            if (accessState == null) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("state");
                accessState = null;
            }
            com.android.server.permission.access.GetStateScope $this$onDevicePermissionFlagsChanged_u24lambda_u241 = new com.android.server.permission.access.GetStateScope(accessState);
            com.android.server.permission.access.permission.AppIdPermissionPolicy $this$onDevicePermissionFlagsChanged_u24lambda_u241_u24lambda_u240 = permissionService.policy;
            com.android.server.permission.access.permission.Permission permission = $this$onDevicePermissionFlagsChanged_u24lambda_u241_u24lambda_u240.getPermissions($this$onDevicePermissionFlagsChanged_u24lambda_u241).get(permissionName);
            if (permission == null) {
                return;
            }
            boolean wasPermissionGranted = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(oldFlags);
            boolean isPermissionGranted = com.android.server.permission.access.permission.PermissionFlags.INSTANCE.isPermissionGranted(newFlags);
            if (permission.getPermissionInfo().getProtection() == 1) {
                if (wasPermissionGranted && !isPermissionGranted) {
                    android.util.SparseBooleanArray $this$set$iv = this.runtimePermissionRevokedUids;
                    boolean value$iv = com.android.server.permission.access.permission.PermissionService.NOTIFICATIONS_PERMISSIONS.contains(permissionName) && this.runtimePermissionRevokedUids.get(uid, true);
                    $this$set$iv.put(uid, value$iv);
                }
                com.android.server.permission.access.immutable.MutableIntMap<java.util.Set<java.lang.String>> mutableIntMap = this.runtimePermissionChangedUidDevices;
                java.util.Set<java.lang.String> linkedHashSet = mutableIntMap.get(uid);
                if (linkedHashSet == null) {
                    linkedHashSet = new java.util.LinkedHashSet();
                    mutableIntMap.put(uid, linkedHashSet);
                }
                linkedHashSet.add(deviceId);
            }
            int $i$f$getProtection = permission.getGids().length == 0 ? 1 : 0;
            if ((1 ^ $i$f$getProtection) == 0 || wasPermissionGranted || !isPermissionGranted) {
                return;
            }
            com.android.server.permission.access.immutable.IntSetExtensionsKt.plusAssign(this.gidsChangedUids, uid);
        }

        @Override // com.android.server.permission.access.permission.AppIdPermissionPolicy.OnPermissionFlagsChangedListener, com.android.server.permission.access.permission.DevicePermissionPolicy.OnDevicePermissionFlagsChangedListener
        public void onStateMutated() {
            final java.lang.String reason;
            android.os.Handler handler;
            com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners onPermissionsChangeListeners;
            if (this.isPermissionFlagsChanged) {
                android.content.pm.PackageManager.invalidatePackageInfoCache();
                this.isPermissionFlagsChanged = false;
            }
            com.android.server.permission.access.immutable.IntMap $this$forEachIndexed$iv = this.runtimePermissionChangedUidDevices;
            com.android.server.permission.access.permission.PermissionService permissionService = com.android.server.permission.access.permission.PermissionService.this;
            int size = $this$forEachIndexed$iv.getSize();
            for (int index$iv = 0; index$iv < size; index$iv++) {
                int uid = $this$forEachIndexed$iv.keyAt(index$iv);
                java.util.Set<java.lang.String> $this$forEach$iv = $this$forEachIndexed$iv.valueAt(index$iv);
                for (java.lang.Object element$iv : $this$forEach$iv) {
                    java.lang.String deviceId = (java.lang.String) element$iv;
                    com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners onPermissionsChangeListeners2 = permissionService.onPermissionsChangeListeners;
                    if (onPermissionsChangeListeners2 == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("onPermissionsChangeListeners");
                        onPermissionsChangeListeners = null;
                    } else {
                        onPermissionsChangeListeners = onPermissionsChangeListeners2;
                    }
                    onPermissionsChangeListeners.onPermissionsChanged(uid, deviceId);
                }
            }
            this.runtimePermissionChangedUidDevices.clear();
            if (!this.isKillRuntimePermissionRevokedUidsSkipped) {
                if (!this.killRuntimePermissionRevokedUidsReasons.isEmpty()) {
                    reason = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.joinToString$default(this.killRuntimePermissionRevokedUidsReasons, ", ", null, null, 0, null, null, 62, null);
                } else {
                    reason = "permissions revoked";
                }
                android.util.SparseBooleanArray $this$forEachIndexed$iv2 = this.runtimePermissionRevokedUids;
                final com.android.server.permission.access.permission.PermissionService permissionService2 = com.android.server.permission.access.permission.PermissionService.this;
                int size2 = $this$forEachIndexed$iv2.size();
                for (int index$iv2 = 0; index$iv2 < size2; index$iv2++) {
                    final int uid2 = $this$forEachIndexed$iv2.keyAt(index$iv2);
                    final boolean areOnlyNotificationsPermissionsRevoked = $this$forEachIndexed$iv2.valueAt(index$iv2);
                    android.os.Handler handler2 = permissionService2.handler;
                    if (handler2 == null) {
                        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("handler");
                        handler = null;
                    } else {
                        handler = handler2;
                    }
                    handler.post(new java.lang.Runnable() { // from class: com.android.server.permission.access.permission.PermissionService$OnPermissionFlagsChangedListener$onStateMutated$2$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            if (!areOnlyNotificationsPermissionsRevoked || !this.isAppBackupAndRestoreRunning(uid2)) {
                                permissionService2.killUid(uid2, reason);
                            }
                        }
                    });
                }
            }
            this.runtimePermissionRevokedUids.clear();
            com.android.server.permission.access.immutable.IntSet $this$forEachIndexed$iv3 = this.gidsChangedUids;
            final com.android.server.permission.access.permission.PermissionService permissionService3 = com.android.server.permission.access.permission.PermissionService.this;
            int size3 = $this$forEachIndexed$iv3.getSize();
            for (int index$iv3 = 0; index$iv3 < size3; index$iv3++) {
                final int uid3 = $this$forEachIndexed$iv3.elementAt(index$iv3);
                android.os.Handler handler3 = permissionService3.handler;
                if (handler3 == null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("handler");
                    handler3 = null;
                }
                handler3.post(new java.lang.Runnable() { // from class: com.android.server.permission.access.permission.PermissionService$OnPermissionFlagsChangedListener$onStateMutated$3$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        permissionService3.killUid(uid3, "permission grant or revoke changed gids");
                    }
                });
            }
            this.gidsChangedUids.clear();
            this.isKillRuntimePermissionRevokedUidsSkipped = false;
            this.killRuntimePermissionRevokedUidsReasons.clear();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final boolean isAppBackupAndRestoreRunning(int uid) {
            if (com.android.server.permission.access.permission.PermissionService.this.checkUidPermission(uid, "android.permission.BACKUP", "default:0") != 0) {
                return false;
            }
            int userId = android.os.UserHandle.getUserId(uid);
            java.lang.Integer secureInt = getSecureInt("user_setup_complete", userId);
            boolean isInSetup = secureInt != null && secureInt.intValue() == 0;
            if (isInSetup) {
                return true;
            }
            java.lang.Integer secureInt2 = getSecureInt("user_setup_personalization_state", userId);
            return secureInt2 != null && secureInt2.intValue() == 1;
        }

        private final java.lang.Integer getSecureInt(java.lang.String settingName, int userId) {
            try {
                return java.lang.Integer.valueOf(android.provider.Settings.Secure.getIntForUser(com.android.server.permission.access.permission.PermissionService.this.context.getContentResolver(), settingName, userId));
            } catch (android.provider.Settings.SettingNotFoundException e) {
                android.util.Slog.i(com.android.server.permission.access.permission.PermissionService.LOG_TAG, "Setting " + settingName + " not found", e);
                return null;
            }
        }
    }

    /* JADX INFO: compiled from: PermissionService.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0002\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007J\u0010\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0018\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0016\u0010\u0013\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u000e\u0010\u0014\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, d2 = {"Lcom/android/server/permission/access/permission/PermissionService$OnPermissionsChangeListeners;", "Landroid/os/Handler;", "looper", "Landroid/os/Looper;", "(Landroid/os/Looper;)V", "listeners", "Landroid/os/RemoteCallbackList;", "Landroid/permission/IOnPermissionsChangeListener;", "addListener", "", "listener", "handleMessage", "msg", "Landroid/os/Message;", "handleOnPermissionsChanged", "uid", "", "deviceId", "", "onPermissionsChanged", "removeListener", "Companion", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class OnPermissionsChangeListeners extends android.os.Handler {
        public static final com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners.Companion Companion = new com.android.server.permission.access.permission.PermissionService.OnPermissionsChangeListeners.Companion(null);
        private static final int MSG_ON_PERMISSIONS_CHANGED = 1;
        private final android.os.RemoteCallbackList<android.permission.IOnPermissionsChangeListener> listeners;

        public OnPermissionsChangeListeners(android.os.Looper looper) {
            super(looper);
            this.listeners = new android.os.RemoteCallbackList<>();
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                int uid = msg.arg1;
                java.lang.Object obj = msg.obj;
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(obj, "null cannot be cast to non-null type kotlin.String");
                java.lang.String deviceId = (java.lang.String) obj;
                handleOnPermissionsChanged(uid, deviceId);
            }
        }

        private final void handleOnPermissionsChanged(final int uid, final java.lang.String deviceId) {
            this.listeners.broadcast(new java.util.function.Consumer() { // from class: com.android.server.permission.access.permission.PermissionService$OnPermissionsChangeListeners$handleOnPermissionsChanged$1
                @Override // java.util.function.Consumer
                public final void accept(android.permission.IOnPermissionsChangeListener listener) {
                    try {
                        listener.onPermissionsChanged(uid, deviceId);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.permission.access.permission.PermissionService.LOG_TAG, "Error when calling OnPermissionsChangeListener", e);
                    }
                }
            });
        }

        public final void addListener(android.permission.IOnPermissionsChangeListener listener) {
            this.listeners.register((android.os.IInterface) listener);
        }

        public final void removeListener(android.permission.IOnPermissionsChangeListener listener) {
            this.listeners.unregister((android.os.IInterface) listener);
        }

        public final void onPermissionsChanged(int uid, java.lang.String deviceId) {
            if (this.listeners.getRegisteredCallbackCount() > 0) {
                obtainMessage(1, uid, 0, deviceId).sendToTarget();
            }
        }

        /* JADX INFO: compiled from: PermissionService.kt */
        @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/android/server/permission/access/permission/PermissionService$OnPermissionsChangeListeners$Companion;", "", "()V", "MSG_ON_PERMISSIONS_CHANGED", "", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
        public static final class Companion {
            public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            private Companion() {
            }
        }
    }

    /* JADX INFO: compiled from: PermissionService.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0012\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0013\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \n*\u0004\u0018\u00010\b0\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\b0\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0014"}, d2 = {"Lcom/android/server/permission/access/permission/PermissionService$Companion;", "", "()V", "BACKGROUND_RATIONALE_CHANGE_ID", "", "BACKUP_TIMEOUT_MILLIS", "FULLER_PERMISSIONS", "Landroid/util/ArrayMap;", "", "LOG_TAG", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "MAX_PERMISSION_TREE_FOOTPRINT", "", "NOTIFICATIONS_PERMISSIONS", "Landroid/util/ArraySet;", "PERMISSION_ALLOWLIST_MASK", "REVIEW_REQUIRED_FLAGS", "UNREQUESTABLE_MASK", "getFullerPermission", "permissionName", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final java.lang.String getFullerPermission(java.lang.String permissionName) {
            return (java.lang.String) com.android.server.permission.access.permission.PermissionService.FULLER_PERMISSIONS.get(permissionName);
        }
    }

    static {
        android.util.ArrayMap<java.lang.String, java.lang.String> arrayMap = new android.util.ArrayMap<>();
        arrayMap.put("android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION");
        arrayMap.put("android.permission.INTERACT_ACROSS_USERS", "android.permission.INTERACT_ACROSS_USERS_FULL");
        FULLER_PERMISSIONS = arrayMap;
        NOTIFICATIONS_PERMISSIONS = com.android.server.permission.access.collection.ArraySetExtensionsKt.arraySetOf("android.permission.POST_NOTIFICATIONS");
        BACKUP_TIMEOUT_MILLIS = java.util.concurrent.TimeUnit.SECONDS.toMillis(60L);
    }
}
