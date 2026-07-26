package kotlin.ranges;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: _Ranges.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000t\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u001d\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001¢\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012¢\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0087\n¢\u0006\u0002\u0010\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b \u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020!2\b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u0087\n¢\u0006\u0002\u0010\"\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\tH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\bH\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010\tH\u0087\n¢\u0006\u0002\u0010$\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060%2\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b \u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020**\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\f\u0010+\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u0010+\u001a\u00020\b*\u00020'H\u0007\u001a\f\u0010+\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007¢\u0006\u0002\u0010-\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\b*\u00020'H\u0007¢\u0006\u0002\u0010.\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\t*\u00020)H\u0007¢\u0006\u0002\u0010/\u001a\f\u00100\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u00100\u001a\u00020\b*\u00020'H\u0007\u001a\f\u00100\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u00101\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007¢\u0006\u0002\u0010-\u001a\u0013\u00101\u001a\u0004\u0018\u00010\b*\u00020'H\u0007¢\u0006\u0002\u0010.\u001a\u0013\u00101\u001a\u0004\u0018\u00010\t*\u00020)H\u0007¢\u0006\u0002\u0010/\u001a\r\u00102\u001a\u00020\u0018*\u00020\u0016H\u0087\b\u001a\u0014\u00102\u001a\u00020\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\b*\u00020!H\u0087\b\u001a\u0014\u00102\u001a\u00020\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\t*\u00020#H\u0087\b\u001a\u0014\u00102\u001a\u00020\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007\u001a\u0014\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u0016H\u0087\b¢\u0006\u0002\u00105\u001a\u001b\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u00106\u001a\u0014\u00104\u001a\u0004\u0018\u00010\b*\u00020!H\u0087\b¢\u0006\u0002\u00107\u001a\u001b\u00104\u001a\u0004\u0018\u00010\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u00108\u001a\u0014\u00104\u001a\u0004\u0018\u00010\t*\u00020#H\u0087\b¢\u0006\u0002\u00109\u001a\u001b\u00104\u001a\u0004\u0018\u00010\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u0010:\u001a\n\u0010;\u001a\u00020**\u00020*\u001a\n\u0010;\u001a\u00020'*\u00020'\u001a\n\u0010;\u001a\u00020)*\u00020)\u001a\u0015\u0010<\u001a\u00020**\u00020*2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020'*\u00020'2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020)*\u00020)2\u0006\u0010<\u001a\u00020\tH\u0086\u0004\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000¢\u0006\u0002\u0010>\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000¢\u0006\u0002\u0010?\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000¢\u0006\u0002\u0010@\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000¢\u0006\u0002\u0010A\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000¢\u0006\u0002\u0010B\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000¢\u0006\u0002\u0010D\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000¢\u0006\u0002\u0010E\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000¢\u0006\u0002\u0010F\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000¢\u0006\u0002\u0010H\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000¢\u0006\u0002\u0010I\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000¢\u0006\u0002\u0010K\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000¢\u0006\u0002\u0010L\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000¢\u0006\u0002\u0010M\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000¢\u0006\u0002\u0010N\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020\u0016*\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004¨\u0006P"}, d2 = {"coerceAtLeast", "T", "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "Lkotlin/ranges/CharRange;", "element", "", "(Lkotlin/ranges/CharRange;Ljava/lang/Character;)Z", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "Lkotlin/ranges/IntRange;", "(Lkotlin/ranges/IntRange;Ljava/lang/Integer;)Z", "Lkotlin/ranges/LongRange;", "(Lkotlin/ranges/LongRange;Ljava/lang/Long;)Z", "Lkotlin/ranges/OpenEndRange;", "downTo", "Lkotlin/ranges/IntProgression;", "to", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "first", "firstOrNull", "(Lkotlin/ranges/CharProgression;)Ljava/lang/Character;", "(Lkotlin/ranges/IntProgression;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongProgression;)Ljava/lang/Long;", "last", "lastOrNull", "random", "Lkotlin/random/Random;", "randomOrNull", "(Lkotlin/ranges/CharRange;)Ljava/lang/Character;", "(Lkotlin/ranges/CharRange;Lkotlin/random/Random;)Ljava/lang/Character;", "(Lkotlin/ranges/IntRange;)Ljava/lang/Integer;", "(Lkotlin/ranges/IntRange;Lkotlin/random/Random;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongRange;)Ljava/lang/Long;", "(Lkotlin/ranges/LongRange;Lkotlin/random/Random;)Ljava/lang/Long;", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/ranges/RangesKt")
public class RangesKt___RangesKt extends kotlin.ranges.RangesKt__RangesKt {
    public static final int first(kotlin.ranges.IntProgression $this$first) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final long first(kotlin.ranges.LongProgression $this$first) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final char first(kotlin.ranges.CharProgression $this$first) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$first, "<this>");
        if ($this$first.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$first + " is empty.");
        }
        return $this$first.getFirst();
    }

    public static final java.lang.Integer firstOrNull(kotlin.ranges.IntProgression $this$firstOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Integer.valueOf($this$firstOrNull.getFirst());
    }

    public static final java.lang.Long firstOrNull(kotlin.ranges.LongProgression $this$firstOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Long.valueOf($this$firstOrNull.getFirst());
    }

    public static final java.lang.Character firstOrNull(kotlin.ranges.CharProgression $this$firstOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$firstOrNull, "<this>");
        if ($this$firstOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Character.valueOf($this$firstOrNull.getFirst());
    }

    public static final int last(kotlin.ranges.IntProgression $this$last) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final long last(kotlin.ranges.LongProgression $this$last) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final char last(kotlin.ranges.CharProgression $this$last) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$last, "<this>");
        if ($this$last.isEmpty()) {
            throw new java.util.NoSuchElementException("Progression " + $this$last + " is empty.");
        }
        return $this$last.getLast();
    }

    public static final java.lang.Integer lastOrNull(kotlin.ranges.IntProgression $this$lastOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Integer.valueOf($this$lastOrNull.getLast());
    }

    public static final java.lang.Long lastOrNull(kotlin.ranges.LongProgression $this$lastOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Long.valueOf($this$lastOrNull.getLast());
    }

    public static final java.lang.Character lastOrNull(kotlin.ranges.CharProgression $this$lastOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastOrNull, "<this>");
        if ($this$lastOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Character.valueOf($this$lastOrNull.getLast());
    }

    private static final int random(kotlin.ranges.IntRange $this$random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return kotlin.ranges.RangesKt.random($this$random, kotlin.random.Random.INSTANCE);
    }

    private static final long random(kotlin.ranges.LongRange $this$random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return kotlin.ranges.RangesKt.random($this$random, kotlin.random.Random.INSTANCE);
    }

    private static final char random(kotlin.ranges.CharRange $this$random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        return kotlin.ranges.RangesKt.random($this$random, kotlin.random.Random.INSTANCE);
    }

    public static final int random(kotlin.ranges.IntRange $this$random, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return kotlin.random.RandomKt.nextInt(random, $this$random);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    public static final long random(kotlin.ranges.LongRange $this$random, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return kotlin.random.RandomKt.nextLong(random, $this$random);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    public static final char random(kotlin.ranges.CharRange $this$random, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$random, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        try {
            return (char) random.nextInt($this$random.getFirst(), $this$random.getLast() + 1);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.util.NoSuchElementException(e.getMessage());
        }
    }

    private static final java.lang.Integer randomOrNull(kotlin.ranges.IntRange $this$randomOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return kotlin.ranges.RangesKt.randomOrNull($this$randomOrNull, kotlin.random.Random.INSTANCE);
    }

    private static final java.lang.Long randomOrNull(kotlin.ranges.LongRange $this$randomOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return kotlin.ranges.RangesKt.randomOrNull($this$randomOrNull, kotlin.random.Random.INSTANCE);
    }

    private static final java.lang.Character randomOrNull(kotlin.ranges.CharRange $this$randomOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        return kotlin.ranges.RangesKt.randomOrNull($this$randomOrNull, kotlin.random.Random.INSTANCE);
    }

    public static final java.lang.Integer randomOrNull(kotlin.ranges.IntRange $this$randomOrNull, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Integer.valueOf(kotlin.random.RandomKt.nextInt(random, $this$randomOrNull));
    }

    public static final java.lang.Long randomOrNull(kotlin.ranges.LongRange $this$randomOrNull, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Long.valueOf(kotlin.random.RandomKt.nextLong(random, $this$randomOrNull));
    }

    public static final java.lang.Character randomOrNull(kotlin.ranges.CharRange $this$randomOrNull, kotlin.random.Random random) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$randomOrNull, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(random, "random");
        if ($this$randomOrNull.isEmpty()) {
            return null;
        }
        return java.lang.Character.valueOf((char) random.nextInt($this$randomOrNull.getFirst(), $this$randomOrNull.getLast() + 1));
    }

    private static final boolean contains(kotlin.ranges.IntRange $this$contains, java.lang.Integer element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return element != null && $this$contains.contains(element.intValue());
    }

    private static final boolean contains(kotlin.ranges.LongRange $this$contains, java.lang.Long element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return element != null && $this$contains.contains(element.longValue());
    }

    private static final boolean contains(kotlin.ranges.CharRange $this$contains, java.lang.Character element) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return element != null && $this$contains.contains(element.charValue());
    }

    public static final boolean intRangeContains(kotlin.ranges.ClosedRange<java.lang.Integer> closedRange, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(kotlin.ranges.ClosedRange<java.lang.Long> closedRange, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean shortRangeContains(kotlin.ranges.ClosedRange<java.lang.Short> closedRange, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Short.valueOf(value));
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(kotlin.ranges.ClosedRange $this$contains, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(kotlin.ranges.ClosedRange $this$contains, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean intRangeContains(kotlin.ranges.OpenEndRange<java.lang.Integer> openEndRange, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(kotlin.ranges.OpenEndRange<java.lang.Long> openEndRange, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean shortRangeContains(kotlin.ranges.OpenEndRange<java.lang.Short> openEndRange, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Short.valueOf(value));
    }

    private static final boolean contains(kotlin.ranges.IntRange $this$contains, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return kotlin.ranges.RangesKt.intRangeContains((kotlin.ranges.ClosedRange<java.lang.Integer>) $this$contains, value);
    }

    private static final boolean contains(kotlin.ranges.LongRange $this$contains, byte value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return kotlin.ranges.RangesKt.longRangeContains((kotlin.ranges.ClosedRange<java.lang.Long>) $this$contains, value);
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean intRangeContains(kotlin.ranges.ClosedRange $this$contains, double value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Integer it = kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean longRangeContains(kotlin.ranges.ClosedRange $this$contains, double value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Long it = kotlin.ranges.RangesKt.toLongExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean byteRangeContains(kotlin.ranges.ClosedRange $this$contains, double value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean shortRangeContains(kotlin.ranges.ClosedRange $this$contains, double value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Short it = kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    public static final boolean floatRangeContains(kotlin.ranges.ClosedRange<java.lang.Float> closedRange, double value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Float.valueOf((float) value));
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean intRangeContains(kotlin.ranges.ClosedRange $this$contains, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Integer it = kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean longRangeContains(kotlin.ranges.ClosedRange $this$contains, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Long it = kotlin.ranges.RangesKt.toLongExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean byteRangeContains(kotlin.ranges.ClosedRange $this$contains, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean shortRangeContains(kotlin.ranges.ClosedRange $this$contains, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        java.lang.Short it = kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return $this$contains.contains(it);
        }
        return false;
    }

    public static final boolean doubleRangeContains(kotlin.ranges.ClosedRange<java.lang.Double> closedRange, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Double.valueOf(value));
    }

    public static final boolean doubleRangeContains(kotlin.ranges.OpenEndRange<java.lang.Double> openEndRange, float value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Double.valueOf(value));
    }

    public static final boolean longRangeContains(kotlin.ranges.ClosedRange<java.lang.Long> closedRange, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(kotlin.ranges.ClosedRange<java.lang.Byte> closedRange, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(kotlin.ranges.ClosedRange<java.lang.Short> closedRange, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Short it = kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(kotlin.ranges.ClosedRange $this$contains, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(kotlin.ranges.ClosedRange $this$contains, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean longRangeContains(kotlin.ranges.OpenEndRange<java.lang.Long> openEndRange, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(kotlin.ranges.OpenEndRange<java.lang.Byte> openEndRange, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(kotlin.ranges.OpenEndRange<java.lang.Short> openEndRange, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Short it = kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    private static final boolean contains(kotlin.ranges.LongRange $this$contains, int value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return kotlin.ranges.RangesKt.longRangeContains((kotlin.ranges.ClosedRange<java.lang.Long>) $this$contains, value);
    }

    public static final boolean intRangeContains(kotlin.ranges.ClosedRange<java.lang.Integer> closedRange, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Integer it = kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    public static final boolean byteRangeContains(kotlin.ranges.ClosedRange<java.lang.Byte> closedRange, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(kotlin.ranges.ClosedRange<java.lang.Short> closedRange, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Short it = kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(kotlin.ranges.ClosedRange $this$contains, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(kotlin.ranges.ClosedRange $this$contains, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean intRangeContains(kotlin.ranges.OpenEndRange<java.lang.Integer> openEndRange, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Integer it = kotlin.ranges.RangesKt.toIntExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    public static final boolean byteRangeContains(kotlin.ranges.OpenEndRange<java.lang.Byte> openEndRange, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    public static final boolean shortRangeContains(kotlin.ranges.OpenEndRange<java.lang.Short> openEndRange, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Short it = kotlin.ranges.RangesKt.toShortExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    private static final boolean contains(kotlin.ranges.IntRange $this$contains, long value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return kotlin.ranges.RangesKt.intRangeContains((kotlin.ranges.ClosedRange<java.lang.Integer>) $this$contains, value);
    }

    public static final boolean intRangeContains(kotlin.ranges.ClosedRange<java.lang.Integer> closedRange, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(kotlin.ranges.ClosedRange<java.lang.Long> closedRange, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        return closedRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(kotlin.ranges.ClosedRange<java.lang.Byte> closedRange, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(closedRange, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return closedRange.contains(it);
        }
        return false;
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(kotlin.ranges.ClosedRange $this$contains, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Double.valueOf(value));
    }

    @kotlin.Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @kotlin.DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(kotlin.ranges.ClosedRange $this$contains, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return $this$contains.contains(java.lang.Float.valueOf(value));
    }

    public static final boolean intRangeContains(kotlin.ranges.OpenEndRange<java.lang.Integer> openEndRange, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Integer.valueOf(value));
    }

    public static final boolean longRangeContains(kotlin.ranges.OpenEndRange<java.lang.Long> openEndRange, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        return openEndRange.contains(java.lang.Long.valueOf(value));
    }

    public static final boolean byteRangeContains(kotlin.ranges.OpenEndRange<java.lang.Byte> openEndRange, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(openEndRange, "<this>");
        java.lang.Byte it = kotlin.ranges.RangesKt.toByteExactOrNull(value);
        if (it != null) {
            return openEndRange.contains(it);
        }
        return false;
    }

    private static final boolean contains(kotlin.ranges.IntRange $this$contains, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return kotlin.ranges.RangesKt.intRangeContains((kotlin.ranges.ClosedRange<java.lang.Integer>) $this$contains, value);
    }

    private static final boolean contains(kotlin.ranges.LongRange $this$contains, short value) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return kotlin.ranges.RangesKt.longRangeContains((kotlin.ranges.ClosedRange<java.lang.Long>) $this$contains, value);
    }

    public static final kotlin.ranges.IntProgression downTo(int $this$downTo, byte to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.LongProgression downTo(long $this$downTo, byte to) {
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$downTo, to, -1L);
    }

    public static final kotlin.ranges.IntProgression downTo(byte $this$downTo, byte to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.IntProgression downTo(short $this$downTo, byte to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.CharProgression downTo(char $this$downTo, char to) {
        return kotlin.ranges.CharProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.IntProgression downTo(int $this$downTo, int to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.LongProgression downTo(long $this$downTo, int to) {
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$downTo, to, -1L);
    }

    public static final kotlin.ranges.IntProgression downTo(byte $this$downTo, int to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.IntProgression downTo(short $this$downTo, int to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.LongProgression downTo(int $this$downTo, long to) {
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$downTo, to, -1L);
    }

    public static final kotlin.ranges.LongProgression downTo(long $this$downTo, long to) {
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$downTo, to, -1L);
    }

    public static final kotlin.ranges.LongProgression downTo(byte $this$downTo, long to) {
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$downTo, to, -1L);
    }

    public static final kotlin.ranges.LongProgression downTo(short $this$downTo, long to) {
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$downTo, to, -1L);
    }

    public static final kotlin.ranges.IntProgression downTo(int $this$downTo, short to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.LongProgression downTo(long $this$downTo, short to) {
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$downTo, to, -1L);
    }

    public static final kotlin.ranges.IntProgression downTo(byte $this$downTo, short to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.IntProgression downTo(short $this$downTo, short to) {
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$downTo, to, -1);
    }

    public static final kotlin.ranges.IntProgression reversed(kotlin.ranges.IntProgression $this$reversed) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final kotlin.ranges.LongProgression reversed(kotlin.ranges.LongProgression $this$reversed) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final kotlin.ranges.CharProgression reversed(kotlin.ranges.CharProgression $this$reversed) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$reversed, "<this>");
        return kotlin.ranges.CharProgression.INSTANCE.fromClosedRange($this$reversed.getLast(), $this$reversed.getFirst(), -$this$reversed.getStep());
    }

    public static final kotlin.ranges.IntProgression step(kotlin.ranges.IntProgression $this$step, int step) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Integer.valueOf(step));
        return kotlin.ranges.IntProgression.INSTANCE.fromClosedRange($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final kotlin.ranges.LongProgression step(kotlin.ranges.LongProgression $this$step, long step) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Long.valueOf(step));
        return kotlin.ranges.LongProgression.INSTANCE.fromClosedRange($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final kotlin.ranges.CharProgression step(kotlin.ranges.CharProgression $this$step, int step) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$step, "<this>");
        kotlin.ranges.RangesKt.checkStepIsPositive(step > 0, java.lang.Integer.valueOf(step));
        return kotlin.ranges.CharProgression.INSTANCE.fromClosedRange($this$step.getFirst(), $this$step.getLast(), $this$step.getStep() > 0 ? step : -step);
    }

    public static final java.lang.Byte toByteExactOrNull(int $this$toByteExactOrNull) {
        if (new kotlin.ranges.IntRange(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, 127).contains($this$toByteExactOrNull)) {
            return java.lang.Byte.valueOf((byte) $this$toByteExactOrNull);
        }
        return null;
    }

    public static final java.lang.Byte toByteExactOrNull(long $this$toByteExactOrNull) {
        if (new kotlin.ranges.LongRange(-128L, 127L).contains($this$toByteExactOrNull)) {
            return java.lang.Byte.valueOf((byte) $this$toByteExactOrNull);
        }
        return null;
    }

    public static final java.lang.Byte toByteExactOrNull(short $this$toByteExactOrNull) {
        if (kotlin.ranges.RangesKt.intRangeContains((kotlin.ranges.ClosedRange<java.lang.Integer>) new kotlin.ranges.IntRange(com.android.server.usb.descriptors.UsbEndpointDescriptor.MASK_ENDPOINT_DIRECTION, 127), $this$toByteExactOrNull)) {
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
        if (new kotlin.ranges.LongRange(-2147483648L, 2147483647L).contains($this$toIntExactOrNull)) {
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
        if (new kotlin.ranges.IntRange(-32768, 32767).contains($this$toShortExactOrNull)) {
            return java.lang.Short.valueOf((short) $this$toShortExactOrNull);
        }
        return null;
    }

    public static final java.lang.Short toShortExactOrNull(long $this$toShortExactOrNull) {
        if (new kotlin.ranges.LongRange(-32768L, 32767L).contains($this$toShortExactOrNull)) {
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

    public static final kotlin.ranges.IntRange until(int $this$until, byte to) {
        return new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.LongRange until(long $this$until, byte to) {
        return new kotlin.ranges.LongRange($this$until, ((long) to) - 1);
    }

    public static final kotlin.ranges.IntRange until(byte $this$until, byte to) {
        return new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.IntRange until(short $this$until, byte to) {
        return new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.CharRange until(char $this$until, char to) {
        return kotlin.jvm.internal.Intrinsics.compare((int) to, 0) <= 0 ? kotlin.ranges.CharRange.INSTANCE.getEMPTY() : new kotlin.ranges.CharRange($this$until, (char) (to - 1));
    }

    public static final kotlin.ranges.IntRange until(int $this$until, int to) {
        return to <= Integer.MIN_VALUE ? kotlin.ranges.IntRange.INSTANCE.getEMPTY() : new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.LongRange until(long $this$until, int to) {
        return new kotlin.ranges.LongRange($this$until, ((long) to) - 1);
    }

    public static final kotlin.ranges.IntRange until(byte $this$until, int to) {
        return to <= Integer.MIN_VALUE ? kotlin.ranges.IntRange.INSTANCE.getEMPTY() : new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.IntRange until(short $this$until, int to) {
        return to <= Integer.MIN_VALUE ? kotlin.ranges.IntRange.INSTANCE.getEMPTY() : new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.LongRange until(int $this$until, long to) {
        return to <= Long.MIN_VALUE ? kotlin.ranges.LongRange.INSTANCE.getEMPTY() : new kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final kotlin.ranges.LongRange until(long $this$until, long to) {
        return to <= Long.MIN_VALUE ? kotlin.ranges.LongRange.INSTANCE.getEMPTY() : new kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final kotlin.ranges.LongRange until(byte $this$until, long to) {
        return to <= Long.MIN_VALUE ? kotlin.ranges.LongRange.INSTANCE.getEMPTY() : new kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final kotlin.ranges.LongRange until(short $this$until, long to) {
        return to <= Long.MIN_VALUE ? kotlin.ranges.LongRange.INSTANCE.getEMPTY() : new kotlin.ranges.LongRange($this$until, to - 1);
    }

    public static final kotlin.ranges.IntRange until(int $this$until, short to) {
        return new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.LongRange until(long $this$until, short to) {
        return new kotlin.ranges.LongRange($this$until, ((long) to) - 1);
    }

    public static final kotlin.ranges.IntRange until(byte $this$until, short to) {
        return new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final kotlin.ranges.IntRange until(short $this$until, short to) {
        return new kotlin.ranges.IntRange($this$until, to - 1);
    }

    public static final <T extends java.lang.Comparable<? super T>> T coerceAtLeast(T t, T minimumValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(minimumValue, "minimumValue");
        return t.compareTo(minimumValue) < 0 ? minimumValue : t;
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

    public static final <T extends java.lang.Comparable<? super T>> T coerceAtMost(T t, T maximumValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(maximumValue, "maximumValue");
        return t.compareTo(maximumValue) > 0 ? maximumValue : t;
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
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
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

    public static final <T extends java.lang.Comparable<? super T>> T coerceIn(T t, kotlin.ranges.ClosedFloatingPointRange<T> range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return (!range.lessThanOrEquals(t, range.getStart()) || range.lessThanOrEquals(range.getStart(), t)) ? (!range.lessThanOrEquals(range.getEndInclusive(), t) || range.lessThanOrEquals(t, range.getEndInclusive())) ? t : range.getEndInclusive() : range.getStart();
    }

    public static final <T extends java.lang.Comparable<? super T>> T coerceIn(T t, kotlin.ranges.ClosedRange<T> range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range instanceof kotlin.ranges.ClosedFloatingPointRange) {
            return (T) kotlin.ranges.RangesKt.coerceIn((java.lang.Comparable) t, (kotlin.ranges.ClosedFloatingPointRange) range);
        }
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return t.compareTo(range.getStart()) < 0 ? (T) range.getStart() : t.compareTo(range.getEndInclusive()) > 0 ? (T) range.getEndInclusive() : t;
    }

    public static final int coerceIn(int $this$coerceIn, kotlin.ranges.ClosedRange<java.lang.Integer> range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range instanceof kotlin.ranges.ClosedFloatingPointRange) {
            return ((java.lang.Number) kotlin.ranges.RangesKt.coerceIn(java.lang.Integer.valueOf($this$coerceIn), (kotlin.ranges.ClosedFloatingPointRange<java.lang.Integer>) range)).intValue();
        }
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return $this$coerceIn < ((java.lang.Number) range.getStart()).intValue() ? ((java.lang.Number) range.getStart()).intValue() : $this$coerceIn > ((java.lang.Number) range.getEndInclusive()).intValue() ? ((java.lang.Number) range.getEndInclusive()).intValue() : $this$coerceIn;
    }

    public static final long coerceIn(long $this$coerceIn, kotlin.ranges.ClosedRange<java.lang.Long> range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        if (range instanceof kotlin.ranges.ClosedFloatingPointRange) {
            return ((java.lang.Number) kotlin.ranges.RangesKt.coerceIn(java.lang.Long.valueOf($this$coerceIn), (kotlin.ranges.ClosedFloatingPointRange<java.lang.Long>) range)).longValue();
        }
        if (range.isEmpty()) {
            throw new java.lang.IllegalArgumentException("Cannot coerce value to an empty range: " + range + '.');
        }
        return $this$coerceIn < ((java.lang.Number) range.getStart()).longValue() ? ((java.lang.Number) range.getStart()).longValue() : $this$coerceIn > ((java.lang.Number) range.getEndInclusive()).longValue() ? ((java.lang.Number) range.getEndInclusive()).longValue() : $this$coerceIn;
    }
}
