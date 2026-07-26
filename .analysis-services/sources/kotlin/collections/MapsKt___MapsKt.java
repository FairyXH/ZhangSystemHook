package kotlin.collections;

/* JADX INFO: compiled from: _Maps.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0082\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010&\n\u0002\b\u0002\n\u0002\u0010\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000f\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u001aJ\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\bø\u0001\u0000\u001a$\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aJ\u0010\b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\bø\u0001\u0000\u001a9\u0010\t\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\n\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001a6\u0010\u000b\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00070\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001a'\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004H\u0087\b\u001aJ\u0010\r\u001a\u00020\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\bø\u0001\u0000\u001a[\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001a]\u0010\u0014\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001a\\\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\n0\u0006H\u0086\bø\u0001\u0000\u001aa\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f0\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\b\u0017\u001au\u0010\u0018\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\n0\u0006H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001aw\u0010\u0018\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192$\u0010\u0012\u001a \u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00100\f0\u0006H\u0087\bø\u0001\u0000¢\u0006\u0004\b\u001d\u0010\u001c\u001aJ\u0010\u001e\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010 \u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0\u0006H\u0087\bø\u0001\u0000\u001aV\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0086\bø\u0001\u0000\u001a\\\u0010\"\u001a\b\u0012\u0004\u0012\u0002H\u00100\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0086\bø\u0001\u0000\u001au\u0010#\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\b\b\u0002\u0010\u0010*\u00020\u0011\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192 \u0010\u0012\u001a\u001c\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0006\u0012\u0004\u0018\u0001H\u00100\u0006H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001ao\u0010$\u001a\u0002H\u0019\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010\"\u0010\b\u0003\u0010\u0019*\n\u0012\u0006\b\u0000\u0012\u0002H\u00100\u001a*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u0006\u0010\u001b\u001a\u0002H\u00192\u001e\u0010\u0012\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001ak\u0010%\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\b(\u001ah\u0010)\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000\u001a_\u0010*\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010+\u001aJ\u0010*\u001a\u00020,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\bø\u0001\u0000\u001aJ\u0010*\u001a\u00020-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\bø\u0001\u0000\u001aa\u0010.\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010+\u001aQ\u0010.\u001a\u0004\u0018\u00010,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010/\u001aQ\u0010.\u001a\u0004\u0018\u00010-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u00100\u001aq\u00101\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u00105\u001as\u00106\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u00105\u001al\u00107\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b¢\u0006\u0002\b8\u001ai\u00109\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b\u001ak\u0010:\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\b;\u001ah\u0010<\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000\u001a_\u0010=\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010+\u001aJ\u0010=\u001a\u00020,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\bø\u0001\u0000\u001aJ\u0010=\u001a\u00020-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\bø\u0001\u0000\u001aa\u0010>\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u000e\b\u0002\u0010\u0010*\b\u0012\u0004\u0012\u0002H\u00100&*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010+\u001aQ\u0010>\u001a\u0004\u0018\u00010,\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020,0\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010/\u001aQ\u0010>\u001a\u0004\u0018\u00010-\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020-0\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u00100\u001aq\u0010?\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u00105\u001as\u0010@\u001a\u0004\u0018\u0001H\u0010\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0010*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001a\u00102\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u001003j\n\u0012\u0006\b\u0000\u0012\u0002H\u0010`42\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u0002H\u00100\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u00105\u001al\u0010A\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b¢\u0006\u0002\bB\u001ai\u0010C\u001a\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000422\u00102\u001a.\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u000703j\u0016\u0012\u0012\b\u0000\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007`4H\u0087\b\u001a$\u0010D\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u001aJ\u0010D\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00042\u001e\u0010\u0005\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\bø\u0001\u0000\u001aY\u0010E\u001a\u0002HF\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010F*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002HF2\u001e\u0010 \u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0\u0006H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010G\u001an\u0010H\u001a\u0002HF\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0016\b\u0002\u0010F*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004*\u0002HF23\u0010 \u001a/\u0012\u0013\u0012\u00110\u000e¢\u0006\f\bJ\u0012\b\bK\u0012\u0004\b\b(L\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0007\u0012\u0004\u0012\u00020\u001f0IH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010M\u001a6\u0010N\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030O0\u0016\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0004\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006P"}, d2 = {"all", "", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "", "predicate", "Lkotlin/Function1;", "", "any", "asIterable", "", "asSequence", "Lkotlin/sequences/Sequence;", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_COUNT, "", "firstNotNullOf", "R", "", "transform", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "firstNotNullOfOrNull", "flatMap", "", "flatMapSequence", "flatMapTo", "C", "", "destination", "(Ljava/util/Map;Ljava/util/Collection;Lkotlin/jvm/functions/Function1;)Ljava/util/Collection;", "flatMapSequenceTo", "forEach", "", "action", "map", "mapNotNull", "mapNotNullTo", "mapTo", "maxBy", "", "selector", "maxByOrThrow", "maxByOrNull", "maxOf", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Comparable;", "", "", "maxOfOrNull", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Double;", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/lang/Float;", "maxOfWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Ljava/util/Map;Ljava/util/Comparator;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "maxOfWithOrNull", "maxWith", "maxWithOrThrow", "maxWithOrNull", "minBy", "minByOrThrow", "minByOrNull", "minOf", "minOfOrNull", "minOfWith", "minOfWithOrNull", "minWith", "minWithOrThrow", "minWithOrNull", "none", "onEach", "M", "(Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "onEachIndexed", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "(Ljava/util/Map;Lkotlin/jvm/functions/Function2;)Ljava/util/Map;", "toList", "Lkotlin/Pair;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/collections/MapsKt")
class MapsKt___MapsKt extends kotlin.collections.MapsKt___MapsJvmKt {
    private static final <K, V, R> R firstNotNullOf(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        R rInvoke;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                rInvoke = null;
                break;
            }
            rInvoke = transform.invoke(it.next());
            if (rInvoke != null) {
                break;
            }
        }
        if (rInvoke != null) {
            return rInvoke;
        }
        throw new java.util.NoSuchElementException("No element of the map was transformed to a non-null value.");
    }

    private static final <K, V, R> R firstNotNullOfOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            R rInvoke = transform.invoke(it.next());
            if (rInvoke != null) {
                return rInvoke;
            }
        }
        return null;
    }

    public static final <K, V> java.util.List<kotlin.Pair<K, V>> toList(java.util.Map<? extends K, ? extends V> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        if (map.size() == 0) {
            return kotlin.collections.CollectionsKt.emptyList();
        }
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return kotlin.collections.CollectionsKt.emptyList();
        }
        java.util.Map.Entry<? extends K, ? extends V> next = it.next();
        if (!it.hasNext()) {
            return kotlin.collections.CollectionsKt.listOf(new kotlin.Pair(next.getKey(), next.getValue()));
        }
        java.util.ArrayList result = new java.util.ArrayList(map.size());
        result.add(new kotlin.Pair(next.getKey(), next.getValue()));
        do {
            java.util.Map.Entry<? extends K, ? extends V> next2 = it.next();
            result.add(new kotlin.Pair(next2.getKey(), next2.getValue()));
        } while (it.hasNext());
        return result;
    }

    public static final <K, V, R> java.util.List<R> flatMap(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends java.lang.Iterable<? extends R>> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Collection destination$iv = new java.util.ArrayList();
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            kotlin.collections.CollectionsKt.addAll(destination$iv, transform.invoke(it.next()));
        }
        return (java.util.List) destination$iv;
    }

    public static final <K, V, R> java.util.List<R> flatMapSequence(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends kotlin.sequences.Sequence<? extends R>> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Collection destination$iv = new java.util.ArrayList();
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            kotlin.collections.CollectionsKt.addAll(destination$iv, transform.invoke(it.next()));
        }
        return (java.util.List) destination$iv;
    }

    public static final <K, V, R, C extends java.util.Collection<? super R>> C flatMapTo(java.util.Map<? extends K, ? extends V> map, C destination, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends java.lang.Iterable<? extends R>> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            kotlin.collections.CollectionsKt.addAll(destination, transform.invoke(it.next()));
        }
        return destination;
    }

    public static final <K, V, R, C extends java.util.Collection<? super R>> C flatMapSequenceTo(java.util.Map<? extends K, ? extends V> map, C destination, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends kotlin.sequences.Sequence<? extends R>> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            kotlin.collections.CollectionsKt.addAll(destination, transform.invoke(it.next()));
        }
        return destination;
    }

    public static final <K, V, R> java.util.List<R> map(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Collection destination$iv = new java.util.ArrayList(map.size());
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            destination$iv.add(transform.invoke(it.next()));
        }
        return (java.util.List) destination$iv;
    }

    public static final <K, V, R> java.util.List<R> mapNotNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Collection destination$iv = new java.util.ArrayList();
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            R rInvoke = transform.invoke(it.next());
            if (rInvoke != null) {
                destination$iv.add(rInvoke);
            }
        }
        return (java.util.List) destination$iv;
    }

    public static final <K, V, R, C extends java.util.Collection<? super R>> C mapNotNullTo(java.util.Map<? extends K, ? extends V> map, C destination, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            R rInvoke = transform.invoke(it.next());
            if (rInvoke != null) {
                destination.add(rInvoke);
            }
        }
        return destination;
    }

    public static final <K, V, R, C extends java.util.Collection<? super R>> C mapTo(java.util.Map<? extends K, ? extends V> map, C destination, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            destination.add(transform.invoke(it.next()));
        }
        return destination;
    }

    public static final <K, V> boolean all(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        if (map.isEmpty()) {
            return true;
        }
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (!predicate.invoke(it.next()).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <K, V> boolean any(java.util.Map<? extends K, ? extends V> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return !map.isEmpty();
    }

    public static final <K, V> boolean any(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        if (map.isEmpty()) {
            return false;
        }
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (predicate.invoke(it.next()).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    private static final <K, V> int count(java.util.Map<? extends K, ? extends V> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.size();
    }

    public static final <K, V> int count(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        if (map.isEmpty()) {
            return 0;
        }
        int count = 0;
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (predicate.invoke(it.next()).booleanValue()) {
                count++;
            }
        }
        return count;
    }

    public static final <K, V> void forEach(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            action.invoke(it.next());
        }
    }

    private static final <K, V, R extends java.lang.Comparable<? super R>> java.util.Map.Entry<K, V> maxByOrThrow(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.lang.Iterable $this$maxBy$iv = map.entrySet();
        java.util.Iterator iterator$iv = $this$maxBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        java.lang.Object maxElem$iv = iterator$iv.next();
        if (iterator$iv.hasNext()) {
            java.lang.Comparable maxValue$iv = selector.invoke(maxElem$iv);
            do {
                java.lang.Object e$iv = iterator$iv.next();
                R rInvoke = selector.invoke(e$iv);
                if (maxValue$iv.compareTo(rInvoke) < 0) {
                    maxElem$iv = e$iv;
                    maxValue$iv = rInvoke;
                }
            } while (iterator$iv.hasNext());
        }
        return (java.util.Map.Entry) maxElem$iv;
    }

    private static final <K, V, R extends java.lang.Comparable<? super R>> java.util.Map.Entry<K, V> maxByOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        java.lang.Object maxElem$iv;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.lang.Iterable $this$maxByOrNull$iv = map.entrySet();
        java.util.Iterator iterator$iv = $this$maxByOrNull$iv.iterator();
        if (iterator$iv.hasNext()) {
            maxElem$iv = iterator$iv.next();
            if (iterator$iv.hasNext()) {
                java.lang.Comparable maxValue$iv = selector.invoke(maxElem$iv);
                do {
                    java.lang.Object e$iv = iterator$iv.next();
                    R rInvoke = selector.invoke(e$iv);
                    if (maxValue$iv.compareTo(rInvoke) < 0) {
                        maxElem$iv = e$iv;
                        maxValue$iv = rInvoke;
                    }
                } while (iterator$iv.hasNext());
            }
        } else {
            maxElem$iv = null;
        }
        return (java.util.Map.Entry) maxElem$iv;
    }

    private static final <K, V> double maxOf(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Double> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        double dDoubleValue = selector.invoke((java.lang.Object) it.next()).doubleValue();
        while (it.hasNext()) {
            dDoubleValue = java.lang.Math.max(dDoubleValue, selector.invoke((java.lang.Object) it.next()).doubleValue());
        }
        return dDoubleValue;
    }

    /* JADX INFO: renamed from: maxOf, reason: collision with other method in class */
    private static final <K, V> float m11754maxOf(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Float> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        float fFloatValue = selector.invoke((java.lang.Object) it.next()).floatValue();
        while (it.hasNext()) {
            fFloatValue = java.lang.Math.max(fFloatValue, selector.invoke((java.lang.Object) it.next()).floatValue());
        }
        return fFloatValue;
    }

    /* JADX INFO: renamed from: maxOf, reason: collision with other method in class */
    private static final <K, V, R extends java.lang.Comparable<? super R>> R m11755maxOf(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (rInvoke.compareTo(rInvoke2) < 0) {
                rInvoke = rInvoke2;
            }
        }
        return rInvoke;
    }

    /* JADX INFO: renamed from: maxOfOrNull, reason: collision with other method in class */
    private static final <K, V> java.lang.Double m11756maxOfOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Double> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        double dDoubleValue = selector.invoke((java.lang.Object) it.next()).doubleValue();
        while (it.hasNext()) {
            dDoubleValue = java.lang.Math.max(dDoubleValue, selector.invoke((java.lang.Object) it.next()).doubleValue());
        }
        return java.lang.Double.valueOf(dDoubleValue);
    }

    /* JADX INFO: renamed from: maxOfOrNull, reason: collision with other method in class */
    private static final <K, V> java.lang.Float m11757maxOfOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Float> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        float fFloatValue = selector.invoke((java.lang.Object) it.next()).floatValue();
        while (it.hasNext()) {
            fFloatValue = java.lang.Math.max(fFloatValue, selector.invoke((java.lang.Object) it.next()).floatValue());
        }
        return java.lang.Float.valueOf(fFloatValue);
    }

    private static final <K, V, R extends java.lang.Comparable<? super R>> R maxOfOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (rInvoke.compareTo(rInvoke2) < 0) {
                rInvoke = rInvoke2;
            }
        }
        return rInvoke;
    }

    private static final <K, V, R> R maxOfWith(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super R> comparator, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (comparator.compare(rInvoke, rInvoke2) < 0) {
                rInvoke = rInvoke2;
            }
        }
        return rInvoke;
    }

    private static final <K, V, R> R maxOfWithOrNull(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super R> comparator, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (comparator.compare(rInvoke, rInvoke2) < 0) {
                rInvoke = (java.lang.Object) rInvoke2;
            }
        }
        return rInvoke;
    }

    private static final <K, V> java.util.Map.Entry<K, V> maxWithOrThrow(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super java.util.Map.Entry<? extends K, ? extends V>> comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.Map.Entry) kotlin.collections.CollectionsKt.maxWithOrThrow(map.entrySet(), comparator);
    }

    private static final <K, V> java.util.Map.Entry<K, V> maxWithOrNull(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super java.util.Map.Entry<? extends K, ? extends V>> comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.Map.Entry) kotlin.collections.CollectionsKt.maxWithOrNull(map.entrySet(), comparator);
    }

    private static final <K, V, R extends java.lang.Comparable<? super R>> java.util.Map.Entry<K, V> minByOrThrow(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.lang.Iterable $this$minBy$iv = map.entrySet();
        java.util.Iterator iterator$iv = $this$minBy$iv.iterator();
        if (!iterator$iv.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        java.lang.Object minElem$iv = iterator$iv.next();
        if (iterator$iv.hasNext()) {
            java.lang.Comparable minValue$iv = selector.invoke(minElem$iv);
            do {
                java.lang.Object e$iv = iterator$iv.next();
                R rInvoke = selector.invoke(e$iv);
                if (minValue$iv.compareTo(rInvoke) > 0) {
                    minElem$iv = e$iv;
                    minValue$iv = rInvoke;
                }
            } while (iterator$iv.hasNext());
        }
        return (java.util.Map.Entry) minElem$iv;
    }

    private static final <K, V, R extends java.lang.Comparable<? super R>> java.util.Map.Entry<K, V> minByOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        java.lang.Object minElem$iv;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.lang.Iterable $this$minByOrNull$iv = map.entrySet();
        java.util.Iterator iterator$iv = $this$minByOrNull$iv.iterator();
        if (iterator$iv.hasNext()) {
            minElem$iv = iterator$iv.next();
            if (iterator$iv.hasNext()) {
                java.lang.Comparable minValue$iv = selector.invoke(minElem$iv);
                do {
                    java.lang.Object e$iv = iterator$iv.next();
                    R rInvoke = selector.invoke(e$iv);
                    if (minValue$iv.compareTo(rInvoke) > 0) {
                        minElem$iv = e$iv;
                        minValue$iv = rInvoke;
                    }
                } while (iterator$iv.hasNext());
            }
        } else {
            minElem$iv = null;
        }
        return (java.util.Map.Entry) minElem$iv;
    }

    private static final <K, V> double minOf(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Double> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        double dDoubleValue = selector.invoke((java.lang.Object) it.next()).doubleValue();
        while (it.hasNext()) {
            dDoubleValue = java.lang.Math.min(dDoubleValue, selector.invoke((java.lang.Object) it.next()).doubleValue());
        }
        return dDoubleValue;
    }

    /* JADX INFO: renamed from: minOf, reason: collision with other method in class */
    private static final <K, V> float m11758minOf(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Float> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        float fFloatValue = selector.invoke((java.lang.Object) it.next()).floatValue();
        while (it.hasNext()) {
            fFloatValue = java.lang.Math.min(fFloatValue, selector.invoke((java.lang.Object) it.next()).floatValue());
        }
        return fFloatValue;
    }

    /* JADX INFO: renamed from: minOf, reason: collision with other method in class */
    private static final <K, V, R extends java.lang.Comparable<? super R>> R m11759minOf(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (rInvoke.compareTo(rInvoke2) > 0) {
                rInvoke = rInvoke2;
            }
        }
        return rInvoke;
    }

    /* JADX INFO: renamed from: minOfOrNull, reason: collision with other method in class */
    private static final <K, V> java.lang.Double m11760minOfOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Double> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        double dDoubleValue = selector.invoke((java.lang.Object) it.next()).doubleValue();
        while (it.hasNext()) {
            dDoubleValue = java.lang.Math.min(dDoubleValue, selector.invoke((java.lang.Object) it.next()).doubleValue());
        }
        return java.lang.Double.valueOf(dDoubleValue);
    }

    /* JADX INFO: renamed from: minOfOrNull, reason: collision with other method in class */
    private static final <K, V> java.lang.Float m11761minOfOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Float> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        float fFloatValue = selector.invoke((java.lang.Object) it.next()).floatValue();
        while (it.hasNext()) {
            fFloatValue = java.lang.Math.min(fFloatValue, selector.invoke((java.lang.Object) it.next()).floatValue());
        }
        return java.lang.Float.valueOf(fFloatValue);
    }

    private static final <K, V, R extends java.lang.Comparable<? super R>> R minOfOrNull(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (rInvoke.compareTo(rInvoke2) > 0) {
                rInvoke = rInvoke2;
            }
        }
        return rInvoke;
    }

    private static final <K, V, R> R minOfWith(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super R> comparator, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (comparator.compare(rInvoke, rInvoke2) > 0) {
                rInvoke = rInvoke2;
            }
        }
        return rInvoke;
    }

    private static final <K, V, R> R minOfWithOrNull(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super R> comparator, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> selector) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selector, "selector");
        java.util.Iterator<T> it = map.entrySet().iterator();
        if (!it.hasNext()) {
            return null;
        }
        R rInvoke = selector.invoke((java.lang.Object) it.next());
        while (it.hasNext()) {
            R rInvoke2 = selector.invoke((java.lang.Object) it.next());
            if (comparator.compare(rInvoke, rInvoke2) > 0) {
                rInvoke = (java.lang.Object) rInvoke2;
            }
        }
        return rInvoke;
    }

    private static final <K, V> java.util.Map.Entry<K, V> minWithOrThrow(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super java.util.Map.Entry<? extends K, ? extends V>> comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.Map.Entry) kotlin.collections.CollectionsKt.minWithOrThrow(map.entrySet(), comparator);
    }

    private static final <K, V> java.util.Map.Entry<K, V> minWithOrNull(java.util.Map<? extends K, ? extends V> map, java.util.Comparator<? super java.util.Map.Entry<? extends K, ? extends V>> comparator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(comparator, "comparator");
        return (java.util.Map.Entry) kotlin.collections.CollectionsKt.minWithOrNull(map.entrySet(), comparator);
    }

    public static final <K, V> boolean none(java.util.Map<? extends K, ? extends V> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.isEmpty();
    }

    public static final <K, V> boolean none(java.util.Map<? extends K, ? extends V> map, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        if (map.isEmpty()) {
            return true;
        }
        java.util.Iterator<java.util.Map.Entry<? extends K, ? extends V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            if (predicate.invoke(it.next()).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public static final <K, V, M extends java.util.Map<? extends K, ? extends V>> M onEach(M m, kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.util.Iterator<java.util.Map.Entry<K, V>> it = m.entrySet().iterator();
        while (it.hasNext()) {
            action.invoke(it.next());
        }
        return m;
    }

    public static final <K, V, M extends java.util.Map<? extends K, ? extends V>> M onEachIndexed(M m, kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.util.Map.Entry<? extends K, ? extends V>, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.lang.Iterable $this$forEachIndexed$iv = m.entrySet();
        int index$iv = 0;
        for (java.lang.Object item$iv : $this$forEachIndexed$iv) {
            int index$iv2 = index$iv + 1;
            if (index$iv < 0) {
                kotlin.collections.CollectionsKt.throwIndexOverflow();
            }
            action.invoke(java.lang.Integer.valueOf(index$iv), item$iv);
            index$iv = index$iv2;
        }
        return m;
    }

    private static final <K, V> java.lang.Iterable<java.util.Map.Entry<K, V>> asIterable(java.util.Map<? extends K, ? extends V> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.entrySet();
    }

    public static final <K, V> kotlin.sequences.Sequence<java.util.Map.Entry<K, V>> asSequence(java.util.Map<? extends K, ? extends V> map) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return kotlin.collections.CollectionsKt.asSequence(map.entrySet());
    }
}
