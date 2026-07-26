package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Maps.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000~\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010%\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010(\n\u0002\u0010)\n\u0002\u0010'\n\u0002\b\n\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0017\u001a`\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032\u0006\u0010\u0004\u001a\u00020\u00052%\b\u0001\u0010\u0006\u001a\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0002\b\nH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001\u001aX\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032%\b\u0001\u0010\u0006\u001a\u001f\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\u0012\u0004\u0012\u00020\t0\u0007¢\u0006\u0002\b\nH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\u001e\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\u001a1\u0010\f\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\rj\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001a_\u0010\f\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\rj\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u000e\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011¢\u0006\u0002\u0010\u0012\u001a1\u0010\u0013\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0014j\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u0015\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001a_\u0010\u0013\u001a\u001e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0014j\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003`\u0015\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011¢\u0006\u0002\u0010\u0016\u001a!\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001aO\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011¢\u0006\u0002\u0010\u0018\u001a!\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003H\u0087\b\u001aO\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u00032*\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010\"\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011¢\u0006\u0002\u0010\u0018\u001a*\u0010\u001a\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001bH\u0087\n¢\u0006\u0002\u0010\u001c\u001a*\u0010\u001d\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001bH\u0087\n¢\u0006\u0002\u0010\u001c\u001a9\u0010\u001e\u001a\u00020\u001f\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b \"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010\"\u001a1\u0010#\u001a\u00020\u001f\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b *\u000e\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0002\b\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u0010\"\u001a7\u0010$\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\t\b\u0001\u0010\u0003¢\u0006\u0002\b *\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010%\u001a\u0002H\u0003H\u0087\b¢\u0006\u0002\u0010\"\u001aV\u0010&\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\bø\u0001\u0000\u001aJ\u0010(\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010'\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\bø\u0001\u0000\u001aV\u0010)\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\bø\u0001\u0000\u001aq\u0010*\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010-\u001aq\u0010.\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010'\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010-\u001aJ\u0010/\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010'\u001a\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u001f0\u0007H\u0086\bø\u0001\u0000\u001a;\u00100\u001a\u0004\u0018\u0001H\u0003\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b \"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u00101\u001aC\u00102\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u00022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u000304H\u0087\bø\u0001\u0000¢\u0006\u0002\u00105\u001aC\u00106\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u00022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u000304H\u0080\bø\u0001\u0000¢\u0006\u0002\u00105\u001aC\u00107\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u00022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H\u000304H\u0086\bø\u0001\u0000¢\u0006\u0002\u00105\u001a1\u00108\u001a\u0002H\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0007¢\u0006\u0002\u00101\u001a?\u00109\u001a\u0002H:\"\u0014\b\u0000\u0010+*\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0001*\u0002H:\"\u0004\b\u0001\u0010:*\u0002H+2\f\u00103\u001a\b\u0012\u0004\u0012\u0002H:04H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010;\u001a'\u0010<\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\b\u001a:\u0010=\u001a\u00020\u001f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a9\u0010>\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b0?\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\n\u001a<\u0010>\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030A0@\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\bH\u0087\n¢\u0006\u0002\bB\u001a\\\u0010C\u001a\u000e\u0012\u0004\u0012\u0002H:\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\bø\u0001\u0000\u001aw\u0010E\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:\"\u0018\b\u0003\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H:\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010-\u001a\\\u0010F\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H:0\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\bø\u0001\u0000\u001aw\u0010G\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010:\"\u0018\b\u0003\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H:0\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+2\u001e\u0010D\u001a\u001a\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001b\u0012\u0004\u0012\u0002H:0\u0007H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010-\u001a@\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010!\u001a\u0002H\u0002H\u0087\u0002¢\u0006\u0002\u0010I\u001aH\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u000e\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010H\u0087\u0002¢\u0006\u0002\u0010K\u001aA\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020LH\u0087\u0002\u001aA\u0010H\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020MH\u0087\u0002\u001a2\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u0002H\u0087\n¢\u0006\u0002\u0010O\u001a:\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u000e\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0010H\u0087\n¢\u0006\u0002\u0010P\u001a3\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020LH\u0087\n\u001a3\u0010N\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\f\u0010J\u001a\b\u0012\u0004\u0012\u0002H\u00020MH\u0087\n\u001a0\u0010Q\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0000\u001a3\u0010R\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u0003\u0018\u00010\u0001H\u0087\b\u001aT\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u001a\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010H\u0086\u0002¢\u0006\u0002\u0010T\u001aG\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011H\u0086\u0002\u001aM\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110LH\u0086\u0002\u001aI\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0014\u0010V\u001a\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0086\u0002\u001aM\u0010S\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110MH\u0086\u0002\u001aJ\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u001a\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010H\u0087\n¢\u0006\u0002\u0010X\u001a=\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011H\u0087\n\u001aC\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110LH\u0087\n\u001a=\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0087\n\u001aC\u0010W\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110MH\u0087\n\u001aG\u0010Y\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u001a\u0010\u000f\u001a\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010¢\u0006\u0002\u0010X\u001a@\u0010Y\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110L\u001a@\u0010Y\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b2\u0018\u0010\u000f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110M\u001a;\u0010Z\u001a\u0004\u0018\u0001H\u0003\"\t\b\u0000\u0010\u0002¢\u0006\u0002\b \"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u0002H\u0087\b¢\u0006\u0002\u00101\u001a:\u0010[\u001a\u00020\t\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b2\u0006\u0010!\u001a\u0002H\u00022\u0006\u0010%\u001a\u0002H\u0003H\u0087\n¢\u0006\u0002\u0010\\\u001a;\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u0010¢\u0006\u0002\u0010\u0018\u001aQ\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110\u00102\u0006\u0010,\u001a\u0002H+¢\u0006\u0002\u0010^\u001a4\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110L\u001aO\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110L2\u0006\u0010,\u001a\u0002H+¢\u0006\u0002\u0010_\u001a2\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001aM\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00012\u0006\u0010,\u001a\u0002H+H\u0007¢\u0006\u0002\u0010`\u001a4\u0010]\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110M\u001aO\u0010]\u001a\u0002H+\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0018\b\u0002\u0010+*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0002\u0012\u0006\b\u0000\u0012\u0002H\u00030\b*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00110M2\u0006\u0010,\u001a\u0002H+¢\u0006\u0002\u0010a\u001a2\u0010b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\b\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u0010\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0001H\u0007\u001a1\u0010c\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0011\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u001bH\u0087\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006d"}, d2 = {"buildMap", "", "K", com.android.server.integrity.parser.RuleMetadataParser.VERSION_TAG, "capacity", "", "builderAction", "Lkotlin/Function1;", "", "", "Lkotlin/ExtensionFunctionType;", "emptyMap", "hashMapOf", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "pairs", "", "Lkotlin/Pair;", "([Lkotlin/Pair;)Ljava/util/HashMap;", "linkedMapOf", "Ljava/util/LinkedHashMap;", "Lkotlin/collections/LinkedHashMap;", "([Lkotlin/Pair;)Ljava/util/LinkedHashMap;", "mapOf", "([Lkotlin/Pair;)Ljava/util/Map;", "mutableMapOf", "component1", "", "(Ljava/util/Map$Entry;)Ljava/lang/Object;", "component2", "contains", "", "Lkotlin/internal/OnlyInputTypes;", "key", "(Ljava/util/Map;Ljava/lang/Object;)Z", "containsKey", "containsValue", "value", com.android.server.pm.verify.domain.DomainVerificationPersistence.ATTR_FILTER, "predicate", "filterKeys", "filterNot", "filterNotTo", "M", "destination", "(Ljava/util/Map;Ljava/util/Map;Lkotlin/jvm/functions/Function1;)Ljava/util/Map;", "filterTo", "filterValues", "get", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/lang/Object;", "getOrElse", "defaultValue", "Lkotlin/Function0;", "(Ljava/util/Map;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "getOrElseNullable", "getOrPut", "getValue", "ifEmpty", "R", "(Ljava/util/Map;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNotEmpty", "isNullOrEmpty", "iterator", "", "", "", "mutableIterator", "mapKeys", "transform", "mapKeysTo", "mapValues", "mapValuesTo", "minus", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/util/Map;", "keys", "(Ljava/util/Map;[Ljava/lang/Object;)Ljava/util/Map;", "", "Lkotlin/sequences/Sequence;", "minusAssign", "(Ljava/util/Map;Ljava/lang/Object;)V", "(Ljava/util/Map;[Ljava/lang/Object;)V", "optimizeReadOnlyMap", "orEmpty", "plus", "(Ljava/util/Map;[Lkotlin/Pair;)Ljava/util/Map;", "pair", "map", "plusAssign", "(Ljava/util/Map;[Lkotlin/Pair;)V", "putAll", "remove", "set", "(Ljava/util/Map;Ljava/lang/Object;Ljava/lang/Object;)V", "toMap", "([Lkotlin/Pair;Ljava/util/Map;)Ljava/util/Map;", "(Ljava/lang/Iterable;Ljava/util/Map;)Ljava/util/Map;", "(Ljava/util/Map;Ljava/util/Map;)Ljava/util/Map;", "(Lkotlin/sequences/Sequence;Ljava/util/Map;)Ljava/util/Map;", "toMutableMap", "toPair", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/collections/MapsKt")
public class MapsKt__MapsKt extends com.android.server.permission.jarjar.kotlin.collections.MapsKt__MapsJVMKt {
    public static final <K, V> java.util.Map<K, V> emptyMap() {
        com.android.server.permission.jarjar.kotlin.collections.EmptyMap emptyMap = com.android.server.permission.jarjar.kotlin.collections.EmptyMap.INSTANCE;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(emptyMap, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.emptyMap, V of kotlin.collections.MapsKt__MapsKt.emptyMap>");
        return emptyMap;
    }

    public static final <K, V> java.util.Map<K, V> mapOf(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>... pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        return pairArr.length > 0 ? com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(pairArr, new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(pairArr.length))) : com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
    }

    private static final <K, V> java.util.Map<K, V> mapOf() {
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
    }

    private static final <K, V> java.util.Map<K, V> mutableMapOf() {
        return new java.util.LinkedHashMap();
    }

    public static final <K, V> java.util.Map<K, V> mutableMapOf(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>... pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        java.util.LinkedHashMap $this$mutableMapOf_u24lambda_u240 = new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(pairArr.length));
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll($this$mutableMapOf_u24lambda_u240, pairArr);
        return $this$mutableMapOf_u24lambda_u240;
    }

    private static final <K, V> java.util.HashMap<K, V> hashMapOf() {
        return new java.util.HashMap<>();
    }

    public static final <K, V> java.util.HashMap<K, V> hashMapOf(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>... pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        java.util.HashMap<K, V> map = new java.util.HashMap<>(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(pairArr.length));
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll(map, pairArr);
        return map;
    }

    private static final <K, V> java.util.LinkedHashMap<K, V> linkedMapOf() {
        return new java.util.LinkedHashMap<>();
    }

    public static final <K, V> java.util.LinkedHashMap<K, V> linkedMapOf(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>... pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        return (java.util.LinkedHashMap) com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(pairArr, new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(pairArr.length)));
    }

    private static final <K, V> java.util.Map<K, V> buildMap(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map<K, V>, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "builderAction");
        java.util.Map mapCreateMapBuilder = com.android.server.permission.jarjar.kotlin.collections.MapsKt.createMapBuilder();
        function1.invoke(mapCreateMapBuilder);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.build(mapCreateMapBuilder);
    }

    private static final <K, V> java.util.Map<K, V> buildMap(int capacity, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map<K, V>, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "builderAction");
        java.util.Map mapCreateMapBuilder = com.android.server.permission.jarjar.kotlin.collections.MapsKt.createMapBuilder(capacity);
        function1.invoke(mapCreateMapBuilder);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.build(mapCreateMapBuilder);
    }

    private static final <K, V> boolean isNotEmpty(java.util.Map<? extends K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return !map.isEmpty();
    }

    private static final <K, V> boolean isNullOrEmpty(java.util.Map<? extends K, ? extends V> map) {
        return map == null || map.isEmpty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <K, V> java.util.Map<K, V> orEmpty(java.util.Map<K, ? extends V> map) {
        return map == 0 ? com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap() : map;
    }

    /* JADX WARN: Incorrect types in method signature: <M::Ljava/util/Map<**>;:TR;R:Ljava/lang/Object;>(TM;Lcom/android/server/permission/jarjar/kotlin/jvm/functions/Function0<+TR;>;)TR; */
    private static final java.lang.Object ifEmpty(java.util.Map $this$ifEmpty, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0 defaultValue) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return $this$ifEmpty.isEmpty() ? defaultValue.invoke() : $this$ifEmpty;
    }

    private static final <K, V> boolean contains(java.util.Map<? extends K, ? extends V> map, K k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.containsKey(k);
    }

    private static final <K, V> V get(java.util.Map<? extends K, ? extends V> map, K k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.get(k);
    }

    private static final <K, V> void set(java.util.Map<K, V> map, K k, V v) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        map.put(k, v);
    }

    private static final <K> boolean containsKey(java.util.Map<? extends K, ?> map, K k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.containsKey(k);
    }

    private static final <K, V> boolean containsValue(java.util.Map<K, ? extends V> map, V v) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.containsValue(v);
    }

    private static final <K, V> V remove(java.util.Map<? extends K, V> map, K k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return (V) com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.asMutableMap(map).remove(k);
    }

    private static final <K, V> K component1(java.util.Map.Entry<? extends K, ? extends V> entry) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(entry, "<this>");
        return entry.getKey();
    }

    private static final <K, V> V component2(java.util.Map.Entry<? extends K, ? extends V> entry) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(entry, "<this>");
        return entry.getValue();
    }

    private static final <K, V> com.android.server.permission.jarjar.kotlin.Pair<K, V> toPair(java.util.Map.Entry<? extends K, ? extends V> entry) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(entry, "<this>");
        return new com.android.server.permission.jarjar.kotlin.Pair<>(entry.getKey(), entry.getValue());
    }

    private static final <K, V> V getOrElse(java.util.Map<K, ? extends V> map, K k, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends V> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "defaultValue");
        V v = map.get(k);
        return v == null ? function0.invoke() : v;
    }

    public static final <K, V> V getOrElseNullable(java.util.Map<K, ? extends V> map, K k, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends V> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "defaultValue");
        V v = map.get(k);
        if (v == null && !map.containsKey(k)) {
            return function0.invoke();
        }
        return v;
    }

    public static final <K, V> V getValue(java.util.Map<K, ? extends V> map, K k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return (V) com.android.server.permission.jarjar.kotlin.collections.MapsKt.getOrImplicitDefaultNullable(map, k);
    }

    public static final <K, V> V getOrPut(java.util.Map<K, V> map, K k, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends V> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "defaultValue");
        V v = map.get(k);
        if (v == null) {
            V vInvoke = function0.invoke();
            map.put(k, vInvoke);
            return vInvoke;
        }
        return v;
    }

    private static final <K, V> java.util.Iterator<java.util.Map.Entry<K, V>> iterator(java.util.Map<? extends K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.entrySet().iterator();
    }

    private static final <K, V> java.util.Iterator<java.util.Map.Entry<K, V>> mutableIterator(java.util.Map<K, V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return map.entrySet().iterator();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V, R, M extends java.util.Map<? super K, ? super R>> M mapValuesTo(java.util.Map<? extends K, ? extends V> map, M m, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        java.lang.Iterable $this$associateByTo$iv = map.entrySet();
        for (java.lang.Object element$iv : $this$associateByTo$iv) {
            java.util.Map.Entry it = (java.util.Map.Entry) element$iv;
            m.put(it.getKey(), function1.invoke(element$iv));
        }
        return m;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V, R, M extends java.util.Map<? super R, ? super V>> M mapKeysTo(java.util.Map<? extends K, ? extends V> map, M m, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        java.lang.Iterable $this$associateByTo$iv = map.entrySet();
        for (java.lang.Object element$iv : $this$associateByTo$iv) {
            java.util.Map.Entry it = (java.util.Map.Entry) element$iv;
            m.put(function1.invoke(element$iv), it.getValue());
        }
        return m;
    }

    public static final <K, V> void putAll(java.util.Map<? super K, ? super V> map, com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>[] pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        for (com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V> pair : pairArr) {
            java.lang.Object key = pair.component1();
            java.lang.Object value = pair.component2();
            map.put(key, value);
        }
    }

    public static final <K, V> void putAll(java.util.Map<? super K, ? super V> map, java.lang.Iterable<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "pairs");
        for (com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V> pair : iterable) {
            java.lang.Object key = pair.component1();
            java.lang.Object value = pair.component2();
            map.put(key, value);
        }
    }

    public static final <K, V> void putAll(java.util.Map<? super K, ? super V> map, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "pairs");
        for (com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V> pair : sequence) {
            java.lang.Object key = pair.component1();
            java.lang.Object value = pair.component2();
            map.put(key, value);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V, R> java.util.Map<K, R> mapValues(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(map.size()));
        for (java.lang.Object obj : map.entrySet()) {
            linkedHashMap.put(((java.util.Map.Entry) obj).getKey(), function1.invoke(obj));
        }
        return linkedHashMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V, R> java.util.Map<R, V> mapKeys(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(map.size()));
        for (java.lang.Object obj : map.entrySet()) {
            linkedHashMap.put(function1.invoke(obj), ((java.util.Map.Entry) obj).getValue());
        }
        return linkedHashMap;
    }

    public static final <K, V> java.util.Map<K, V> filterKeys(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super K, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (function1.invoke(entry.getKey()).booleanValue()) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        return linkedHashMap;
    }

    public static final <K, V> java.util.Map<K, V> filterValues(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super V, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        java.util.LinkedHashMap linkedHashMap = new java.util.LinkedHashMap();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (function1.invoke(entry.getValue()).booleanValue()) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        return linkedHashMap;
    }

    public static final <K, V, M extends java.util.Map<? super K, ? super V>> M filterTo(java.util.Map<? extends K, ? extends V> map, M m, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (function1.invoke(entry).booleanValue()) {
                m.put(entry.getKey(), entry.getValue());
            }
        }
        return m;
    }

    public static final <K, V> java.util.Map<K, V> filter(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        java.util.Map destination$iv = new java.util.LinkedHashMap();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (function1.invoke(entry).booleanValue()) {
                destination$iv.put(entry.getKey(), entry.getValue());
            }
        }
        return destination$iv;
    }

    public static final <K, V, M extends java.util.Map<? super K, ? super V>> M filterNotTo(java.util.Map<? extends K, ? extends V> map, M m, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (!function1.invoke(entry).booleanValue()) {
                m.put(entry.getKey(), entry.getValue());
            }
        }
        return m;
    }

    public static final <K, V> java.util.Map<K, V> filterNot(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.util.Map.Entry<? extends K, ? extends V>, java.lang.Boolean> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "predicate");
        java.util.Map destination$iv = new java.util.LinkedHashMap();
        for (java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            if (!function1.invoke(entry).booleanValue()) {
                destination$iv.put(entry.getKey(), entry.getValue());
            }
        }
        return destination$iv;
    }

    public static final <K, V> java.util.Map<K, V> toMap(java.lang.Iterable<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        if (iterable instanceof java.util.Collection) {
            switch (((java.util.Collection) iterable).size()) {
                case 0:
                    return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
                case 1:
                    return com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapOf(iterable instanceof java.util.List ? (com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>) ((java.util.List) iterable).get(0) : iterable.iterator().next());
                default:
                    return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(iterable, new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(((java.util.Collection) iterable).size())));
            }
        }
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.optimizeReadOnlyMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(iterable, new java.util.LinkedHashMap()));
    }

    public static final <K, V, M extends java.util.Map<? super K, ? super V>> M toMap(java.lang.Iterable<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> iterable, M m) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll(m, iterable);
        return m;
    }

    public static final <K, V> java.util.Map<K, V> toMap(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>[] pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "<this>");
        switch (pairArr.length) {
            case 0:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
            case 1:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapOf(pairArr[0]);
            default:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(pairArr, new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(pairArr.length)));
        }
    }

    public static final <K, V, M extends java.util.Map<? super K, ? super V>> M toMap(com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>[] pairArr, M m) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll(m, pairArr);
        return m;
    }

    public static final <K, V> java.util.Map<K, V> toMap(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.optimizeReadOnlyMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(sequence, new java.util.LinkedHashMap()));
    }

    public static final <K, V, M extends java.util.Map<? super K, ? super V>> M toMap(com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> sequence, M m) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll(m, sequence);
        return m;
    }

    public static final <K, V> java.util.Map<K, V> toMap(java.util.Map<? extends K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        switch (map.size()) {
            case 0:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
            case 1:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toSingletonMap(map);
            default:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMutableMap(map);
        }
    }

    public static final <K, V> java.util.Map<K, V> toMutableMap(java.util.Map<? extends K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        return new java.util.LinkedHashMap(map);
    }

    public static final <K, V, M extends java.util.Map<? super K, ? super V>> M toMap(java.util.Map<? extends K, ? extends V> map, M m) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(m, "destination");
        m.putAll(map);
        return m;
    }

    public static final <K, V> java.util.Map<K, V> plus(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V> pair) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pair, "pair");
        if (map.isEmpty()) {
            return com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapOf(pair);
        }
        java.util.LinkedHashMap $this$plus_u24lambda_u2411 = new java.util.LinkedHashMap(map);
        $this$plus_u24lambda_u2411.put(pair.getFirst(), pair.getSecond());
        return $this$plus_u24lambda_u2411;
    }

    public static final <K, V> java.util.Map<K, V> plus(java.util.Map<? extends K, ? extends V> map, java.lang.Iterable<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "pairs");
        if (map.isEmpty()) {
            return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(iterable);
        }
        java.util.LinkedHashMap $this$plus_u24lambda_u2412 = new java.util.LinkedHashMap(map);
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll($this$plus_u24lambda_u2412, iterable);
        return $this$plus_u24lambda_u2412;
    }

    public static final <K, V> java.util.Map<K, V> plus(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>[] pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        if (map.isEmpty()) {
            return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap(pairArr);
        }
        java.util.LinkedHashMap $this$plus_u24lambda_u2413 = new java.util.LinkedHashMap(map);
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll($this$plus_u24lambda_u2413, pairArr);
        return $this$plus_u24lambda_u2413;
    }

    public static final <K, V> java.util.Map<K, V> plus(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "pairs");
        java.util.LinkedHashMap $this$plus_u24lambda_u2414 = new java.util.LinkedHashMap(map);
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll($this$plus_u24lambda_u2414, sequence);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.optimizeReadOnlyMap($this$plus_u24lambda_u2414);
    }

    public static final <K, V> java.util.Map<K, V> plus(java.util.Map<? extends K, ? extends V> map, java.util.Map<? extends K, ? extends V> map2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map2, "map");
        java.util.LinkedHashMap $this$plus_u24lambda_u2415 = new java.util.LinkedHashMap(map);
        $this$plus_u24lambda_u2415.putAll(map2);
        return $this$plus_u24lambda_u2415;
    }

    private static final <K, V> void plusAssign(java.util.Map<? super K, ? super V> map, com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V> pair) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pair, "pair");
        map.put(pair.getFirst(), pair.getSecond());
    }

    private static final <K, V> void plusAssign(java.util.Map<? super K, ? super V> map, java.lang.Iterable<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "pairs");
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll(map, iterable);
    }

    private static final <K, V> void plusAssign(java.util.Map<? super K, ? super V> map, com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>[] pairArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(pairArr, "pairs");
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll(map, pairArr);
    }

    private static final <K, V> void plusAssign(java.util.Map<? super K, ? super V> map, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends com.android.server.permission.jarjar.kotlin.Pair<? extends K, ? extends V>> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "pairs");
        com.android.server.permission.jarjar.kotlin.collections.MapsKt.putAll(map, sequence);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <K, V> void plusAssign(java.util.Map<? super K, ? super V> map, java.util.Map<K, ? extends V> map2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map2, "map");
        map.putAll(map2);
    }

    public static final <K, V> java.util.Map<K, V> minus(java.util.Map<? extends K, ? extends V> map, K k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        java.util.Map $this$minus_u24lambda_u2416 = com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMutableMap(map);
        $this$minus_u24lambda_u2416.remove(k);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.optimizeReadOnlyMap($this$minus_u24lambda_u2416);
    }

    public static final <K, V> java.util.Map<K, V> minus(java.util.Map<? extends K, ? extends V> map, java.lang.Iterable<? extends K> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "keys");
        java.util.Map $this$minus_u24lambda_u2417 = com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMutableMap(map);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll($this$minus_u24lambda_u2417.keySet(), iterable);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.optimizeReadOnlyMap($this$minus_u24lambda_u2417);
    }

    public static final <K, V> java.util.Map<K, V> minus(java.util.Map<? extends K, ? extends V> map, K[] kArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kArr, "keys");
        java.util.Map $this$minus_u24lambda_u2418 = com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMutableMap(map);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll($this$minus_u24lambda_u2418.keySet(), kArr);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.optimizeReadOnlyMap($this$minus_u24lambda_u2418);
    }

    public static final <K, V> java.util.Map<K, V> minus(java.util.Map<? extends K, ? extends V> map, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends K> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "keys");
        java.util.Map $this$minus_u24lambda_u2419 = com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMutableMap(map);
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll($this$minus_u24lambda_u2419.keySet(), sequence);
        return com.android.server.permission.jarjar.kotlin.collections.MapsKt.optimizeReadOnlyMap($this$minus_u24lambda_u2419);
    }

    private static final <K, V> void minusAssign(java.util.Map<K, V> map, K k) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        map.remove(k);
    }

    private static final <K, V> void minusAssign(java.util.Map<K, V> map, java.lang.Iterable<? extends K> iterable) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(iterable, "keys");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(map.keySet(), iterable);
    }

    private static final <K, V> void minusAssign(java.util.Map<K, V> map, K[] kArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kArr, "keys");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(map.keySet(), kArr);
    }

    private static final <K, V> void minusAssign(java.util.Map<K, V> map, com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends K> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "keys");
        com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.removeAll(map.keySet(), sequence);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <K, V> java.util.Map<K, V> optimizeReadOnlyMap(java.util.Map<K, ? extends V> map) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(map, "<this>");
        switch (map.size()) {
            case 0:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.emptyMap();
            case 1:
                return com.android.server.permission.jarjar.kotlin.collections.MapsKt.toSingletonMap(map);
            default:
                return map;
        }
    }
}
