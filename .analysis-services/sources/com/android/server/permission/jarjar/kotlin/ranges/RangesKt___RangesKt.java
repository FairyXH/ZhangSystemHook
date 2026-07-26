package com.android.server.permission.jarjar.kotlin.ranges;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: _Ranges.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000t\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u001d\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001¢\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012¢\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0087\n¢\u0006\u0002\u0010\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b \u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020!2\b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u0087\n¢\u0006\u0002\u0010\"\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\tH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\bH\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010\tH\u0087\n¢\u0006\u0002\u0010$\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060%2\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b \u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020**\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\f\u0010+\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u0010+\u001a\u00020\b*\u00020'H\u0007\u001a\f\u0010+\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007¢\u0006\u0002\u0010-\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\b*\u00020'H\u0007¢\u0006\u0002\u0010.\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\t*\u00020)H\u0007¢\u0006\u0002\u0010/\u001a\f\u00100\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u00100\u001a\u00020\b*\u00020'H\u0007\u001a\f\u00100\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u00101\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007¢\u0006\u0002\u0010-\u001a\u0013\u00101\u001a\u0004\u0018\u00010\b*\u00020'H\u0007¢\u0006\u0002\u0010.\u001a\u0013\u00101\u001a\u0004\u0018\u00010\t*\u00020)H\u0007¢\u0006\u0002\u0010/\u001a\r\u00102\u001a\u00020\u0018*\u00020\u0016H\u0087\b\u001a\u0014\u00102\u001a\u00020\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\b*\u00020!H\u0087\b\u001a\u0014\u00102\u001a\u00020\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\t*\u00020#H\u0087\b\u001a\u0014\u00102\u001a\u00020\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007\u001a\u0014\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u0016H\u0087\b¢\u0006\u0002\u00105\u001a\u001b\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u00106\u001a\u0014\u00104\u001a\u0004\u0018\u00010\b*\u00020!H\u0087\b¢\u0006\u0002\u00107\u001a\u001b\u00104\u001a\u0004\u0018\u00010\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u00108\u001a\u0014\u00104\u001a\u0004\u0018\u00010\t*\u00020#H\u0087\b¢\u0006\u0002\u00109\u001a\u001b\u00104\u001a\u0004\u0018\u00010\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u0010:\u001a\n\u0010;\u001a\u00020**\u00020*\u001a\n\u0010;\u001a\u00020'*\u00020'\u001a\n\u0010;\u001a\u00020)*\u00020)\u001a\u0015\u0010<\u001a\u00020**\u00020*2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020'*\u00020'2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020)*\u00020)2\u0006\u0010<\u001a\u00020\tH\u0086\u0004\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000¢\u0006\u0002\u0010>\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000¢\u0006\u0002\u0010?\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000¢\u0006\u0002\u0010@\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000¢\u0006\u0002\u0010A\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000¢\u0006\u0002\u0010B\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000¢\u0006\u0002\u0010D\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000¢\u0006\u0002\u0010E\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000¢\u0006\u0002\u0010F\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000¢\u0006\u0002\u0010H\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000¢\u0006\u0002\u0010I\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000¢\u0006\u0002\u0010K\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000¢\u0006\u0002\u0010L\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000¢\u0006\u0002\u0010M\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000¢\u0006\u0002\u0010N\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020\u0016*\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004¨\u0006P"}, d2 = {"coerceAtLeast", "T", "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "Lkotlin/ranges/CharRange;", "element", "", "(Lkotlin/ranges/CharRange;Ljava/lang/Character;)Z", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "Lkotlin/ranges/IntRange;", "(Lkotlin/ranges/IntRange;Ljava/lang/Integer;)Z", "Lkotlin/ranges/LongRange;", "(Lkotlin/ranges/LongRange;Ljava/lang/Long;)Z", "Lkotlin/ranges/OpenEndRange;", "downTo", "Lkotlin/ranges/IntProgression;", "to", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "first", "firstOrNull", "(Lkotlin/ranges/CharProgression;)Ljava/lang/Character;", "(Lkotlin/ranges/IntProgression;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongProgression;)Ljava/lang/Long;", "last", "lastOrNull", "random", "Lkotlin/random/Random;", "randomOrNull", "(Lkotlin/ranges/CharRange;)Ljava/lang/Character;", "(Lkotlin/ranges/CharRange;Lkotlin/random/Random;)Ljava/lang/Character;", "(Lkotlin/ranges/IntRange;)Ljava/lang/Integer;", "(Lkotlin/ranges/IntRange;Lkotlin/random/Random;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongRange;)Ljava/lang/Long;", "(Lkotlin/ranges/LongRange;Lkotlin/random/Random;)Ljava/lang/Long;", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/ranges/RangesKt")
public class RangesKt___RangesKt extends com.android.server.permission.jarjar.kotlin.ranges.RangesKt__RangesKt {
    public static final int first(com.android.server.permission.jarjar.kotlin.ranges.IntProgression $this$first) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final long first(com.android.server.permission.jarjar.kotlin.ranges.LongProgression $this$first) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final char first(com.android.server.permission.jarjar.kotlin.ranges.CharProgression $this$first) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final java.lang.Integer firstOrNull(com.android.server.permission.jarjar.kotlin.ranges.IntProgression $this$firstOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Integer.valueOf($this$firstOrNull.getFirst());
    }

    public static final java.lang.Long firstOrNull(com.android.server.permission.jarjar.kotlin.ranges.LongProgression $this$firstOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Long.valueOf($this$firstOrNull.getFirst());
    }

    public static final java.lang.Character firstOrNull(com.android.server.permission.jarjar.kotlin.ranges.CharProgression $this$firstOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Character.valueOf($this$firstOrNull.getFirst());
    }

    public static final int last(com.android.server.permission.jarjar.kotlin.ranges.IntProgression $this$last) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final long last(com.android.server.permission.jarjar.kotlin.ranges.LongProgression $this$last) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final char last(com.android.server.permission.jarjar.kotlin.ranges.CharProgression $this$last) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final java.lang.Integer lastOrNull(com.android.server.permission.jarjar.kotlin.ranges.IntProgression $this$lastOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Integer.valueOf($this$lastOrNull.getLast());
    }

    public static final java.lang.Long lastOrNull(com.android.server.permission.jarjar.kotlin.ranges.LongProgression $this$lastOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Long.valueOf($this$lastOrNull.getLast());
    }

    public static final java.lang.Character lastOrNull(com.android.server.permission.jarjar.kotlin.ranges.CharProgression $this$lastOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Character.valueOf($this$lastOrNull.getLast());
    }

    private static final int random(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.random($this$random, com.android.server.permission.jarjar.kotlin.random.Random.Default);
    }

    private static final long random(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.random($this$random, com.android.server.permission.jarjar.kotlin.random.Random.Default);
    }

    private static final char random(com.android.server.permission.jarjar.kotlin.ranges.CharRange $this$random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.random($this$random, com.android.server.permission.jarjar.kotlin.random.Random.Default);
    }

    public static final int random(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$random, com.android.server.permission.jarjar.kotlin.random.Random random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return com.android.server.permission.jarjar.kotlin.random.RandomKt.nextInt(random, $this$random);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    public static final long random(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$random, com.android.server.permission.jarjar.kotlin.random.Random random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return com.android.server.permission.jarjar.kotlin.random.RandomKt.nextLong(random, $this$random);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    public static final char random(com.android.server.permission.jarjar.kotlin.ranges.CharRange $this$random, com.android.server.permission.jarjar.kotlin.random.Random random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return (char) random.nextInt($this$random.getFirst(), $this$random.getLast() + 1);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    private static final java.lang.Integer randomOrNull(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$randomOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.randomOrNull($this$randomOrNull, com.android.server.permission.jarjar.kotlin.random.Random.Default);
    }

    private static final java.lang.Long randomOrNull(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$randomOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.randomOrNull($this$randomOrNull, com.android.server.permission.jarjar.kotlin.random.Random.Default);
    }

    private static final java.lang.Character randomOrNull(com.android.server.permission.jarjar.kotlin.ranges.CharRange $this$randomOrNull) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.randomOrNull($this$randomOrNull, com.android.server.permission.jarjar.kotlin.random.Random.Default);
    }

    public static final java.lang.Integer randomOrNull(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$randomOrNull, com.android.server.permission.jarjar.kotlin.random.Random random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Integer.valueOf(com.android.server.permission.jarjar.kotlin.random.RandomKt.nextInt(random, $this$randomOrNull));
    }

    public static final java.lang.Long randomOrNull(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$randomOrNull, com.android.server.permission.jarjar.kotlin.random.Random random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Long.valueOf(com.android.server.permission.jarjar.kotlin.random.RandomKt.nextLong(random, $this$randomOrNull));
    }

    public static final java.lang.Character randomOrNull(com.android.server.permission.jarjar.kotlin.ranges.CharRange $this$randomOrNull, com.android.server.permission.jarjar.kotlin.random.Random random) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Character.valueOf((char) random.nextInt($this$randomOrNull.getFirst(), $this$randomOrNull.getLast() + 1));
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$contains, java.lang.Integer element) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return element != null && $this$contains.contains(element.intValue());
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$contains, java.lang.Long element) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return element != null && $this$contains.contains(element.longValue());
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.CharRange $this$contains, java.lang.Character element) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return element != null && $this$contains.contains(element.charValue());
    }

    public static final boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer> closedRange, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Long> closedRange, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Short> closedRange, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Short.valueOf(value));
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean doubleRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean floatRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Integer> openEndRange, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Long> openEndRange, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Short> openEndRange, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Short.valueOf(value));
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$contains, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.intRangeContains((com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer>) $this$contains, value);
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$contains, byte value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.longRangeContains((com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Long>) $this$contains, value);
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, double value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Integer it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, double value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Long it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toLongExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, double value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, double value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Short it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    public static final boolean floatRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Float> closedRange, double value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Float.valueOf((float) value));
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, float value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Integer it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, float value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Long it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toLongExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, float value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, float value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Short it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    public static final boolean doubleRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Double> closedRange, float value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Double.valueOf(value));
    }

    public static final boolean doubleRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Double> openEndRange, float value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Double.valueOf(value));
    }

    public static final boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Long> closedRange, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Byte> closedRange, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Short> closedRange, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Short it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean doubleRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean floatRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Long> openEndRange, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Byte> openEndRange, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Short> openEndRange, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Short it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$contains, int value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.longRangeContains((com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Long>) $this$contains, value);
    }

    public static final boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer> closedRange, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Integer it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    public static final boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Byte> closedRange, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Short> closedRange, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Short it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean doubleRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean floatRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Integer> openEndRange, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Integer it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    public static final boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Byte> openEndRange, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Short> openEndRange, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Short it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$contains, long value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.intRangeContains((com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer>) $this$contains, value);
    }

    public static final boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer> closedRange, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Long> closedRange, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Byte> closedRange, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean doubleRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @com.android.server.permission.jarjar.kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    @com.android.server.permission.jarjar.kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    public static final /* synthetic */ boolean floatRangeContains(com.android.server.permission.jarjar.kotlin.ranges.ClosedRange $this$contains, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean intRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Integer> openEndRange, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Long> openEndRange, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(com.android.server.permission.jarjar.kotlin.ranges.OpenEndRange<java.lang.Byte> openEndRange, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Byte it = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.IntRange $this$contains, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.intRangeContains((com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer>) $this$contains, value);
    }

    private static final boolean contains(com.android.server.permission.jarjar.kotlin.ranges.LongRange $this$contains, short value) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.RangesKt.longRangeContains((com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Long>) $this$contains, value);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(int $this$downTo, byte to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression downTo(long $this$downTo, byte to) {
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$downTo, to, -1L);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(byte $this$downTo, byte to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(short $this$downTo, byte to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.CharProgression downTo(char $this$downTo, char to) {
        return com.android.server.permission.jarjar.kotlin.ranges.CharProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(int $this$downTo, int to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression downTo(long $this$downTo, int to) {
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$downTo, to, -1L);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(byte $this$downTo, int to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(short $this$downTo, int to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression downTo(int $this$downTo, long to) {
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$downTo, to, -1L);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression downTo(long $this$downTo, long to) {
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$downTo, to, -1L);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression downTo(byte $this$downTo, long to) {
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$downTo, to, -1L);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression downTo(short $this$downTo, long to) {
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$downTo, to, -1L);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(int $this$downTo, short to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression downTo(long $this$downTo, short to) {
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$downTo, to, -1L);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(byte $this$downTo, short to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression downTo(short $this$downTo, short to) {
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$downTo, to, -1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression reversed(com.android.server.permission.jarjar.kotlin.ranges.IntProgression $this$reversed) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression reversed(com.android.server.permission.jarjar.kotlin.ranges.LongProgression $this$reversed) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.CharProgression reversed(com.android.server.permission.jarjar.kotlin.ranges.CharProgression $this$reversed) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return com.android.server.permission.jarjar.kotlin.ranges.CharProgression.Companion.fromClosedRange($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntProgression step(com.android.server.permission.jarjar.kotlin.ranges.IntProgression $this$step, int step) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        com.android.server.permission.jarjar.kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Integer.valueOf(step));
        return com.android.server.permission.jarjar.kotlin.ranges.IntProgression.Companion.fromClosedRange($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongProgression step(com.android.server.permission.jarjar.kotlin.ranges.LongProgression $this$step, long step) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        com.android.server.permission.jarjar.kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Long.valueOf(step));
        return com.android.server.permission.jarjar.kotlin.ranges.LongProgression.Companion.fromClosedRange($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.CharProgression step(com.android.server.permission.jarjar.kotlin.ranges.CharProgression $this$step, int step) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        com.android.server.permission.jarjar.kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Integer.valueOf(step));
        return com.android.server.permission.jarjar.kotlin.ranges.CharProgression.Companion.fromClosedRange($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final java.lang.Byte toByteExactOrNull(int $this$toByteExactOrNull) {
        if (new com.android.server.permission.jarjar.kotlin.ranges.IntRange(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, 127).contains($this$toByteExactOrNull)) {
            return java.lang.Byte.valueOf((byte) $this$toByteExactOrNull);
        }
        return null;
    }

    public static final java.lang.Byte toByteExactOrNull(long $this$toByteExactOrNull) {
        if (new com.android.server.permission.jarjar.kotlin.ranges.LongRange(-128L, 127L).contains($this$toByteExactOrNull)) {
            return java.lang.Byte.valueOf((byte) $this$toByteExactOrNull);
        }
        return null;
    }

    public static final java.lang.Byte toByteExactOrNull(short $this$toByteExactOrNull) {
        if (com.android.server.permission.jarjar.kotlin.ranges.RangesKt.intRangeContains((com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer>) new com.android.server.permission.jarjar.kotlin.ranges.IntRange(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, 127), $this$toByteExactOrNull)) {
            return java.lang.Byte.valueOf((byte) $this$toByteExactOrNull);
        }
        return null;
    }

    public static final java.lang.Byte toByteExactOrNull(double $this$toByteExactOrNull) {
        boolean z = false;
        if (-128.0d <= $this$toByteExactOrNull && $this$toByteExactOrNull <= 127.0d) {
            z = true;
        }
        if (z) {
            return java.lang.Byte.valueOf((byte) $this$toByteExactOrNull);
        }
        return null;
    }

    public static final java.lang.Byte toByteExactOrNull(float $this$toByteExactOrNull) {
        boolean z = false;
        if (-128.0f <= $this$toByteExactOrNull && $this$toByteExactOrNull <= 127.0f) {
            z = true;
        }
        if (z) {
            return java.lang.Byte.valueOf((byte) $this$toByteExactOrNull);
        }
        return null;
    }

    public static final java.lang.Integer toIntExactOrNull(long $this$toIntExactOrNull) {
        if (new com.android.server.permission.jarjar.kotlin.ranges.LongRange(-2147483648L, 2147483647L).contains($this$toIntExactOrNull)) {
            return java.lang.Integer.valueOf((int) $this$toIntExactOrNull);
        }
        return null;
    }

    public static final java.lang.Integer toIntExactOrNull(double $this$toIntExactOrNull) {
        boolean z = false;
        if (-2.147483648E9d <= $this$toIntExactOrNull && $this$toIntExactOrNull <= 2.147483647E9d) {
            z = true;
        }
        if (z) {
            return java.lang.Integer.valueOf((int) $this$toIntExactOrNull);
        }
        return null;
    }

    public static final java.lang.Integer toIntExactOrNull(float $this$toIntExactOrNull) {
        boolean z = false;
        if (-2.1474836E9f <= $this$toIntExactOrNull && $this$toIntExactOrNull <= 2.1474836E9f) {
            z = true;
        }
        if (z) {
            return java.lang.Integer.valueOf((int) $this$toIntExactOrNull);
        }
        return null;
    }

    public static final java.lang.Long toLongExactOrNull(double $this$toLongExactOrNull) {
        boolean z = false;
        if (-9.223372036854776E18d <= $this$toLongExactOrNull && $this$toLongExactOrNull <= 9.223372036854776E18d) {
            z = true;
        }
        if (z) {
            return java.lang.Long.valueOf((long) $this$toLongExactOrNull);
        }
        return null;
    }

    public static final java.lang.Long toLongExactOrNull(float $this$toLongExactOrNull) {
        boolean z = false;
        if (-9.223372E18f <= $this$toLongExactOrNull && $this$toLongExactOrNull <= 9.223372E18f) {
            z = true;
        }
        if (z) {
            return java.lang.Long.valueOf((long) $this$toLongExactOrNull);
        }
        return null;
    }

    public static final java.lang.Short toShortExactOrNull(int $this$toShortExactOrNull) {
        if (new com.android.server.permission.jarjar.kotlin.ranges.IntRange(-32768, 32767).contains($this$toShortExactOrNull)) {
            return java.lang.Short.valueOf((short) $this$toShortExactOrNull);
        }
        return null;
    }

    public static final java.lang.Short toShortExactOrNull(long $this$toShortExactOrNull) {
        if (new com.android.server.permission.jarjar.kotlin.ranges.LongRange(-32768L, 32767L).contains($this$toShortExactOrNull)) {
            return java.lang.Short.valueOf((short) $this$toShortExactOrNull);
        }
        return null;
    }

    public static final java.lang.Short toShortExactOrNull(double $this$toShortExactOrNull) {
        boolean z = false;
        if (-32768.0d <= $this$toShortExactOrNull && $this$toShortExactOrNull <= 32767.0d) {
            z = true;
        }
        if (z) {
            return java.lang.Short.valueOf((short) $this$toShortExactOrNull);
        }
        return null;
    }

    public static final java.lang.Short toShortExactOrNull(float $this$toShortExactOrNull) {
        boolean z = false;
        if (-32768.0f <= $this$toShortExactOrNull && $this$toShortExactOrNull <= 32767.0f) {
            z = true;
        }
        if (z) {
            return java.lang.Short.valueOf((short) $this$toShortExactOrNull);
        }
        return null;
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(int $this$until, byte to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongRange until(long $this$until, byte to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.LongRange($this$until, ((long) to) - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(byte $this$until, byte to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(short $this$until, byte to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.CharRange until(char $this$until, char to) {
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.compare((int) to, 0) <= 0 ? com.android.server.permission.jarjar.kotlin.ranges.CharRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.CharRange($this$until, (char) (to - 1));
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(int $this$until, int to) {
        return to <= Integer.MIN_VALUE ? com.android.server.permission.jarjar.kotlin.ranges.IntRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongRange until(long $this$until, int to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.LongRange($this$until, ((long) to) - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(byte $this$until, int to) {
        return to <= Integer.MIN_VALUE ? com.android.server.permission.jarjar.kotlin.ranges.IntRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(short $this$until, int to) {
        return to <= Integer.MIN_VALUE ? com.android.server.permission.jarjar.kotlin.ranges.IntRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongRange until(int $this$until, long to) {
        return to <= Long.MIN_VALUE ? com.android.server.permission.jarjar.kotlin.ranges.LongRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongRange until(long $this$until, long to) {
        return to <= Long.MIN_VALUE ? com.android.server.permission.jarjar.kotlin.ranges.LongRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongRange until(byte $this$until, long to) {
        return to <= Long.MIN_VALUE ? com.android.server.permission.jarjar.kotlin.ranges.LongRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongRange until(short $this$until, long to) {
        return to <= Long.MIN_VALUE ? com.android.server.permission.jarjar.kotlin.ranges.LongRange.Companion.getEMPTY() : new com.android.server.permission.jarjar.kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(int $this$until, short to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.LongRange until(long $this$until, short to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.LongRange($this$until, ((long) to) - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(byte $this$until, short to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final com.android.server.permission.jarjar.kotlin.ranges.IntRange until(short $this$until, short to) {
        return new com.android.server.permission.jarjar.kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final <T extends java.lang.Comparable<? super T>> T coerceAtLeast(T t, T t2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t2, "minimumValue");
        return t.compareTo(t2) < 0 ? t2 : t;
    }

    public static final byte coerceAtLeast(byte $this$coerceAtLeast, byte minimumValue) {
        return $this$coerceAtLeast < minimumValue ? minimumValue : $this$coerceAtLeast;
    }

    public static final short coerceAtLeast(short $this$coerceAtLeast, short minimumValue) {
        return $this$coerceAtLeast < minimumValue ? minimumValue : $this$coerceAtLeast;
    }

    public static final int coerceAtLeast(int $this$coerceAtLeast, int minimumValue) {
        return $this$coerceAtLeast < minimumValue ? minimumValue : $this$coerceAtLeast;
    }

    public static final long coerceAtLeast(long $this$coerceAtLeast, long minimumValue) {
        return $this$coerceAtLeast < minimumValue ? minimumValue : $this$coerceAtLeast;
    }

    public static final float coerceAtLeast(float $this$coerceAtLeast, float minimumValue) {
        return $this$coerceAtLeast < minimumValue ? minimumValue : $this$coerceAtLeast;
    }

    public static final double coerceAtLeast(double $this$coerceAtLeast, double minimumValue) {
        return $this$coerceAtLeast < minimumValue ? minimumValue : $this$coerceAtLeast;
    }

    public static final <T extends java.lang.Comparable<? super T>> T coerceAtMost(T t, T t2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t2, "maximumValue");
        return t.compareTo(t2) > 0 ? t2 : t;
    }

    public static final byte coerceAtMost(byte $this$coerceAtMost, byte maximumValue) {
        return $this$coerceAtMost > maximumValue ? maximumValue : $this$coerceAtMost;
    }

    public static final short coerceAtMost(short $this$coerceAtMost, short maximumValue) {
        return $this$coerceAtMost > maximumValue ? maximumValue : $this$coerceAtMost;
    }

    public static final int coerceAtMost(int $this$coerceAtMost, int maximumValue) {
        return $this$coerceAtMost > maximumValue ? maximumValue : $this$coerceAtMost;
    }

    public static final long coerceAtMost(long $this$coerceAtMost, long maximumValue) {
        return $this$coerceAtMost > maximumValue ? maximumValue : $this$coerceAtMost;
    }

    public static final float coerceAtMost(float $this$coerceAtMost, float maximumValue) {
        return $this$coerceAtMost > maximumValue ? maximumValue : $this$coerceAtMost;
    }

    public static final double coerceAtMost(double $this$coerceAtMost, double maximumValue) {
        return $this$coerceAtMost > maximumValue ? maximumValue : $this$coerceAtMost;
    }

    public static final <T extends java.lang.Comparable<? super T>> T coerceIn(T t, T t2, T t3) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        if (t2 != null && t3 != null) {
            if (t2.compareTo(t3) > 0) {
                throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + t3 + " is less than minimum " + t2 + '.');
            }
            if (t.compareTo(t2) < 0) {
                return t2;
            }
            if (t.compareTo(t3) > 0) {
                return t3;
            }
        } else {
            if (t2 != null && t.compareTo(t2) < 0) {
                return t2;
            }
            if (t3 != null && t.compareTo(t3) > 0) {
                return t3;
            }
        }
        return t;
    }

    public static final byte coerceIn(byte $this$coerceIn, byte minimumValue, byte maximumValue) {
        if (minimumValue <= maximumValue) {
            return $this$coerceIn < minimumValue ? minimumValue : $this$coerceIn > maximumValue ? maximumValue : $this$coerceIn;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((int) maximumValue) + " is less than minimum " + ((int) minimumValue) + '.');
    }

    public static final short coerceIn(short $this$coerceIn, short minimumValue, short maximumValue) {
        if (minimumValue <= maximumValue) {
            return $this$coerceIn < minimumValue ? minimumValue : $this$coerceIn > maximumValue ? maximumValue : $this$coerceIn;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + ((int) maximumValue) + " is less than minimum " + ((int) minimumValue) + '.');
    }

    public static final int coerceIn(int $this$coerceIn, int minimumValue, int maximumValue) {
        if (minimumValue <= maximumValue) {
            return $this$coerceIn < minimumValue ? minimumValue : $this$coerceIn > maximumValue ? maximumValue : $this$coerceIn;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
    }

    public static final long coerceIn(long $this$coerceIn, long minimumValue, long maximumValue) {
        if (minimumValue <= maximumValue) {
            return $this$coerceIn < minimumValue ? minimumValue : $this$coerceIn > maximumValue ? maximumValue : $this$coerceIn;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
    }

    public static final float coerceIn(float $this$coerceIn, float minimumValue, float maximumValue) {
        if (minimumValue <= maximumValue) {
            return $this$coerceIn < minimumValue ? minimumValue : $this$coerceIn > maximumValue ? maximumValue : $this$coerceIn;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
    }

    public static final double coerceIn(double $this$coerceIn, double minimumValue, double maximumValue) {
        if (minimumValue <= maximumValue) {
            return $this$coerceIn < minimumValue ? minimumValue : $this$coerceIn > maximumValue ? maximumValue : $this$coerceIn;
        }
        throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: maximum " + maximumValue + " is less than minimum " + minimumValue + '.');
    }

    public static final <T extends java.lang.Comparable<? super T>> T coerceIn(T t, com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange<T> closedFloatingPointRange) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedFloatingPointRange, "range");
        if (closedFloatingPointRange.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + closedFloatingPointRange + '.');
        }
        return (!closedFloatingPointRange.lessThanOrEquals(t, closedFloatingPointRange.getStart()) || closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getStart(), t)) ? (!closedFloatingPointRange.lessThanOrEquals(closedFloatingPointRange.getEndInclusive(), t) || closedFloatingPointRange.lessThanOrEquals(t, closedFloatingPointRange.getEndInclusive())) ? t : closedFloatingPointRange.getEndInclusive() : closedFloatingPointRange.getStart();
    }

    public static final <T extends java.lang.Comparable<? super T>> T coerceIn(T t, com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<T> closedRange) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange) {
            return (T) com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceIn((java.lang.Comparable) t, (com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange) closedRange);
        }
        if (closedRange.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + '.');
        }
        return t.compareTo(closedRange.getStart()) < 0 ? (T) closedRange.getStart() : t.compareTo(closedRange.getEndInclusive()) > 0 ? (T) closedRange.getEndInclusive() : t;
    }

    public static final int coerceIn(int $this$coerceIn, com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Integer> closedRange) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange) {
            return ((java.lang.Number) com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceIn(java.lang.Integer.valueOf($this$coerceIn), (com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange<java.lang.Integer>) closedRange)).intValue();
        }
        if (closedRange.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + '.');
        }
        return $this$coerceIn < ((java.lang.Number) closedRange.getStart()).intValue() ? ((java.lang.Number) closedRange.getStart()).intValue() : $this$coerceIn > ((java.lang.Number) closedRange.getEndInclusive()).intValue() ? ((java.lang.Number) closedRange.getEndInclusive()).intValue() : $this$coerceIn;
    }

    public static final long coerceIn(long $this$coerceIn, com.android.server.permission.jarjar.kotlin.ranges.ClosedRange<java.lang.Long> closedRange) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange) {
            return ((java.lang.Number) com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceIn(java.lang.Long.valueOf($this$coerceIn), (com.android.server.permission.jarjar.kotlin.ranges.ClosedFloatingPointRange<java.lang.Long>) closedRange)).longValue();
        }
        if (closedRange.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + closedRange + '.');
        }
        return $this$coerceIn < ((java.lang.Number) closedRange.getStart()).longValue() ? ((java.lang.Number) closedRange.getStart()).longValue() : $this$coerceIn > ((java.lang.Number) closedRange.getEndInclusive()).longValue() ? ((java.lang.Number) closedRange.getEndInclusive()).longValue() : $this$coerceIn;
    }
}
