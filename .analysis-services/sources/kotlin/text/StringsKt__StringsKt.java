package kotlin.text;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: Strings.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0084\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b!\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0000\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0011\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u0015\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\n\u001a\u0018\u0010\u0017\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u0018\u0010\u0018\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a:\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001aE\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0010H\u0002¢\u0006\u0002\b!\u001a:\u0010\"\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010#\u001a\u00020\u0010*\u00020\u00022\u0006\u0010$\u001a\u00020\u0006\u001a7\u0010%\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010*\u001a7\u0010+\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010*\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a;\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010 \u001a\u00020\u0010H\u0002¢\u0006\u0002\b.\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u00100\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u00100\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\r\u00103\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00104\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00105\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a \u00106\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00107\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00108\u001a\u000209*\u00020\u0002H\u0086\u0002\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010;\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u0010;\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0010\u0010<\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u0002\u001a\u0010\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u0002\u001a\u0015\u0010@\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\f\u001a\u000f\u0010A\u001a\u00020\r*\u0004\u0018\u00010\rH\u0087\b\u001a\u001c\u0010B\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010B\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001aG\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u000e\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0004\bI\u0010J\u001a=\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u0006\u0010G\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0002\bI\u001a4\u0010K\u001a\u00020\u0010*\u00020\u00022\u0006\u0010L\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00062\u0006\u0010C\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u0002\u001a\u001a\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006\u001a\u0012\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001H\u0087\b\u001a\u0012\u0010R\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010R\u001a\u00020\r*\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a.\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0014\b\b\u0010V\u001a\u000e\u0012\u0004\u0012\u00020X\u0012\u0004\u0012\u00020\u00020WH\u0087\bø\u0001\u0000\u001a\u001d\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001d\u0010_\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140WH\u0087\bø\u0001\u0000¢\u0006\u0002\ba\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00020WH\u0087\bø\u0001\u0000¢\u0006\u0002\bb\u001a\"\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002\u001a\u001a\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002\u001a%\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a\u001d\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a=\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010e\u001a0\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a/\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010T\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0002\bf\u001a%\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a=\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010h\u001a0\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a%\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a$\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010j\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010j\u001a\u00020\u0002*\u00020\r2\u0006\u0010k\u001a\u00020\u00062\u0006\u0010l\u001a\u00020\u0006H\u0087\b\u001a\u001f\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0012\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u0012\u0010m\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\f\u0010r\u001a\u00020\u0010*\u00020\rH\u0007\u001a\u0013\u0010s\u001a\u0004\u0018\u00010\u0010*\u00020\rH\u0007¢\u0006\u0002\u0010t\u001a\n\u0010u\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010u\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010u\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010u\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010w\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010w\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010w\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010w\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010x\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010x\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010x\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010x\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006y"}, d2 = {"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "requireNonNegativeLimit", "", "limit", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "contentEqualsIgnoreCaseImpl", "contentEqualsImpl", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", "delimiter", "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceFirstChar", "replaceFirstCharWithChar", "replaceFirstCharWithCharSequence", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "toBooleanStrict", "toBooleanStrictOrNull", "(Ljava/lang/String;)Ljava/lang/Boolean;", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/text/StringsKt")
public class StringsKt__StringsKt extends kotlin.text.StringsKt__StringsJVMKt {
    public static final java.lang.CharSequence trim(java.lang.CharSequence $this$trim, kotlin.jvm.functions.Function1<? super java.lang.Character, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trim, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        int startIndex = 0;
        int endIndex = $this$trim.length() - 1;
        boolean startFound = false;
        while (startIndex <= endIndex) {
            int index = !startFound ? startIndex : endIndex;
            boolean match = predicate.invoke(java.lang.Character.valueOf($this$trim.charAt(index))).booleanValue();
            if (!startFound) {
                if (match) {
                    startIndex++;
                } else {
                    startFound = true;
                }
            } else {
                if (!match) {
                    break;
                }
                endIndex--;
            }
        }
        return $this$trim.subSequence(startIndex, endIndex + 1);
    }

    public static final java.lang.String trim(java.lang.String $this$trim, kotlin.jvm.functions.Function1<? super java.lang.Character, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trim, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        java.lang.String $this$trim$iv = $this$trim;
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim$iv.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            int index$iv = !startFound$iv ? startIndex$iv : endIndex$iv;
            boolean match$iv = predicate.invoke(java.lang.Character.valueOf($this$trim$iv.charAt(index$iv))).booleanValue();
            if (!startFound$iv) {
                if (match$iv) {
                    startIndex$iv++;
                } else {
                    startFound$iv = true;
                }
            } else {
                if (!match$iv) {
                    break;
                }
                endIndex$iv--;
            }
        }
        return $this$trim$iv.subSequence(startIndex$iv, endIndex$iv + 1).toString();
    }

    public static final java.lang.CharSequence trimStart(java.lang.CharSequence $this$trimStart, kotlin.jvm.functions.Function1<? super java.lang.Character, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        int length = $this$trimStart.length();
        for (int index = 0; index < length; index++) {
            if (!predicate.invoke(java.lang.Character.valueOf($this$trimStart.charAt(index))).booleanValue()) {
                return $this$trimStart.subSequence(index, $this$trimStart.length());
            }
        }
        return "";
    }

    public static final java.lang.String trimStart(java.lang.String $this$trimStart, kotlin.jvm.functions.Function1<? super java.lang.Character, java.lang.Boolean> predicate) {
        java.lang.String strSubSequence;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        java.lang.String $this$trimStart$iv = $this$trimStart;
        int index$iv = 0;
        int length = $this$trimStart$iv.length();
        while (true) {
            if (index$iv < length) {
                if (predicate.invoke(java.lang.Character.valueOf($this$trimStart$iv.charAt(index$iv))).booleanValue()) {
                    index$iv++;
                } else {
                    strSubSequence = $this$trimStart$iv.subSequence(index$iv, $this$trimStart$iv.length());
                    break;
                }
            } else {
                break;
            }
        }
        return strSubSequence.toString();
    }

    public static final java.lang.CharSequence trimEnd(java.lang.CharSequence $this$trimEnd, kotlin.jvm.functions.Function1<? super java.lang.Character, java.lang.Boolean> predicate) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        int length = $this$trimEnd.length() - 1;
        if (length >= 0) {
            do {
                int index = length;
                length--;
                if (!predicate.invoke(java.lang.Character.valueOf($this$trimEnd.charAt(index))).booleanValue()) {
                    return $this$trimEnd.subSequence(0, index + 1);
                }
            } while (length >= 0);
        }
        return "";
    }

    public static final java.lang.String trimEnd(java.lang.String $this$trimEnd, kotlin.jvm.functions.Function1<? super java.lang.Character, java.lang.Boolean> predicate) {
        java.lang.String strSubSequence;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(predicate, "predicate");
        java.lang.String $this$trimEnd$iv = $this$trimEnd;
        int length = $this$trimEnd$iv.length() - 1;
        if (length >= 0) {
            do {
                int index$iv = length;
                length--;
                if (!predicate.invoke(java.lang.Character.valueOf($this$trimEnd$iv.charAt(index$iv))).booleanValue()) {
                    strSubSequence = $this$trimEnd$iv.subSequence(0, index$iv + 1);
                    break;
                }
            } while (length >= 0);
        }
        return strSubSequence.toString();
    }

    public static final java.lang.CharSequence trim(java.lang.CharSequence $this$trim, char... chars) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trim, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            int index$iv = !startFound$iv ? startIndex$iv : endIndex$iv;
            char it = $this$trim.charAt(index$iv);
            boolean match$iv = kotlin.collections.ArraysKt.contains(chars, it);
            if (!startFound$iv) {
                if (match$iv) {
                    startIndex$iv++;
                } else {
                    startFound$iv = true;
                }
            } else {
                if (!match$iv) {
                    break;
                }
                endIndex$iv--;
            }
        }
        java.lang.CharSequence $this$trim$iv = $this$trim.subSequence(startIndex$iv, endIndex$iv + 1);
        return $this$trim$iv;
    }

    public static final java.lang.String trim(java.lang.String $this$trim, char... chars) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trim, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        java.lang.String $this$trim$iv$iv = $this$trim;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
            char it = $this$trim$iv$iv.charAt(index$iv$iv);
            boolean match$iv$iv = kotlin.collections.ArraysKt.contains(chars, it);
            if (!startFound$iv$iv) {
                if (match$iv$iv) {
                    startIndex$iv$iv++;
                } else {
                    startFound$iv$iv = true;
                }
            } else {
                if (!match$iv$iv) {
                    break;
                }
                endIndex$iv$iv--;
            }
        }
        java.lang.String $this$trim$iv = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
        return $this$trim$iv;
    }

    public static final java.lang.CharSequence trimStart(java.lang.CharSequence $this$trimStart, char... chars) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        int length = $this$trimStart.length();
        for (int index$iv = 0; index$iv < length; index$iv++) {
            char it = $this$trimStart.charAt(index$iv);
            if (!kotlin.collections.ArraysKt.contains(chars, it)) {
                return $this$trimStart.subSequence(index$iv, $this$trimStart.length());
            }
        }
        return "";
    }

    public static final java.lang.String trimStart(java.lang.String $this$trimStart, char... chars) {
        java.lang.String strSubSequence;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        java.lang.String $this$trimStart$iv$iv = $this$trimStart;
        int index$iv$iv = 0;
        int length = $this$trimStart$iv$iv.length();
        while (true) {
            if (index$iv$iv < length) {
                char it = $this$trimStart$iv$iv.charAt(index$iv$iv);
                if (kotlin.collections.ArraysKt.contains(chars, it)) {
                    index$iv$iv++;
                } else {
                    strSubSequence = $this$trimStart$iv$iv.subSequence(index$iv$iv, $this$trimStart$iv$iv.length());
                    break;
                }
            } else {
                break;
            }
        }
        java.lang.String $this$trimStart$iv = strSubSequence.toString();
        return $this$trimStart$iv;
    }

    public static final java.lang.CharSequence trimEnd(java.lang.CharSequence $this$trimEnd, char... chars) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        int length = $this$trimEnd.length() - 1;
        if (length >= 0) {
            do {
                int index$iv = length;
                length--;
                char it = $this$trimEnd.charAt(index$iv);
                if (!kotlin.collections.ArraysKt.contains(chars, it)) {
                    return $this$trimEnd.subSequence(0, index$iv + 1);
                }
            } while (length >= 0);
        }
        return "";
    }

    public static final java.lang.String trimEnd(java.lang.String $this$trimEnd, char... chars) {
        java.lang.String strSubSequence;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        java.lang.String $this$trimEnd$iv$iv = $this$trimEnd;
        int length = $this$trimEnd$iv$iv.length() - 1;
        if (length >= 0) {
            do {
                int index$iv$iv = length;
                length--;
                char it = $this$trimEnd$iv$iv.charAt(index$iv$iv);
                if (!kotlin.collections.ArraysKt.contains(chars, it)) {
                    strSubSequence = $this$trimEnd$iv$iv.subSequence(0, index$iv$iv + 1);
                    break;
                }
            } while (length >= 0);
        }
        java.lang.String $this$trimEnd$iv = strSubSequence.toString();
        return $this$trimEnd$iv;
    }

    public static final java.lang.CharSequence trim(java.lang.CharSequence $this$trim) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trim, "<this>");
        int startIndex$iv = 0;
        int endIndex$iv = $this$trim.length() - 1;
        boolean startFound$iv = false;
        while (startIndex$iv <= endIndex$iv) {
            int index$iv = !startFound$iv ? startIndex$iv : endIndex$iv;
            char p0 = $this$trim.charAt(index$iv);
            boolean match$iv = kotlin.text.CharsKt.isWhitespace(p0);
            if (!startFound$iv) {
                if (match$iv) {
                    startIndex$iv++;
                } else {
                    startFound$iv = true;
                }
            } else {
                if (!match$iv) {
                    break;
                }
                endIndex$iv--;
            }
        }
        java.lang.CharSequence $this$trim$iv = $this$trim.subSequence(startIndex$iv, endIndex$iv + 1);
        return $this$trim$iv;
    }

    private static final java.lang.String trim(java.lang.String $this$trim) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trim, "<this>");
        return kotlin.text.StringsKt.trim((java.lang.CharSequence) $this$trim).toString();
    }

    public static final java.lang.CharSequence trimStart(java.lang.CharSequence $this$trimStart) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        int length = $this$trimStart.length();
        for (int index$iv = 0; index$iv < length; index$iv++) {
            char p0 = $this$trimStart.charAt(index$iv);
            if (!kotlin.text.CharsKt.isWhitespace(p0)) {
                return $this$trimStart.subSequence(index$iv, $this$trimStart.length());
            }
        }
        return "";
    }

    private static final java.lang.String trimStart(java.lang.String $this$trimStart) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimStart, "<this>");
        return kotlin.text.StringsKt.trimStart((java.lang.CharSequence) $this$trimStart).toString();
    }

    public static final java.lang.CharSequence trimEnd(java.lang.CharSequence $this$trimEnd) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        int length = $this$trimEnd.length() - 1;
        if (length >= 0) {
            do {
                int index$iv = length;
                length--;
                char p0 = $this$trimEnd.charAt(index$iv);
                if (!kotlin.text.CharsKt.isWhitespace(p0)) {
                    return $this$trimEnd.subSequence(0, index$iv + 1);
                }
            } while (length >= 0);
        }
        return "";
    }

    private static final java.lang.String trimEnd(java.lang.String $this$trimEnd) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$trimEnd, "<this>");
        return kotlin.text.StringsKt.trimEnd((java.lang.CharSequence) $this$trimEnd).toString();
    }

    public static /* synthetic */ java.lang.CharSequence padStart$default(java.lang.CharSequence charSequence, int i, char c, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return kotlin.text.StringsKt.padStart(charSequence, i, c);
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [kotlin.collections.IntIterator] */
    public static final java.lang.CharSequence padStart(java.lang.CharSequence $this$padStart, int length, char padChar) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$padStart, "<this>");
        if (length < 0) {
            throw new java.lang.IllegalArgumentException("Desired length " + length + " is less than zero.");
        }
        if (length <= $this$padStart.length()) {
            return $this$padStart.subSequence(0, $this$padStart.length());
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(length);
        ?? it = new kotlin.ranges.IntRange(1, length - $this$padStart.length()).iterator();
        while (it.hasNext()) {
            it.nextInt();
            sb.append(padChar);
        }
        sb.append($this$padStart);
        return sb;
    }

    public static /* synthetic */ java.lang.String padStart$default(java.lang.String str, int i, char c, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return kotlin.text.StringsKt.padStart(str, i, c);
    }

    public static final java.lang.String padStart(java.lang.String $this$padStart, int length, char padChar) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$padStart, "<this>");
        return kotlin.text.StringsKt.padStart((java.lang.CharSequence) $this$padStart, length, padChar).toString();
    }

    public static /* synthetic */ java.lang.CharSequence padEnd$default(java.lang.CharSequence charSequence, int i, char c, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return kotlin.text.StringsKt.padEnd(charSequence, i, c);
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [kotlin.collections.IntIterator] */
    public static final java.lang.CharSequence padEnd(java.lang.CharSequence $this$padEnd, int length, char padChar) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$padEnd, "<this>");
        if (length < 0) {
            throw new java.lang.IllegalArgumentException("Desired length " + length + " is less than zero.");
        }
        if (length <= $this$padEnd.length()) {
            return $this$padEnd.subSequence(0, $this$padEnd.length());
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder(length);
        sb.append($this$padEnd);
        ?? it = new kotlin.ranges.IntRange(1, length - $this$padEnd.length()).iterator();
        while (it.hasNext()) {
            it.nextInt();
            sb.append(padChar);
        }
        return sb;
    }

    public static /* synthetic */ java.lang.String padEnd$default(java.lang.String str, int i, char c, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            c = ' ';
        }
        return kotlin.text.StringsKt.padEnd(str, i, c);
    }

    public static final java.lang.String padEnd(java.lang.String $this$padEnd, int length, char padChar) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$padEnd, "<this>");
        return kotlin.text.StringsKt.padEnd((java.lang.CharSequence) $this$padEnd, length, padChar).toString();
    }

    private static final boolean isNullOrEmpty(java.lang.CharSequence $this$isNullOrEmpty) {
        return $this$isNullOrEmpty == null || $this$isNullOrEmpty.length() == 0;
    }

    private static final boolean isEmpty(java.lang.CharSequence $this$isEmpty) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isEmpty, "<this>");
        return $this$isEmpty.length() == 0;
    }

    private static final boolean isNotEmpty(java.lang.CharSequence $this$isNotEmpty) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isNotEmpty, "<this>");
        return $this$isNotEmpty.length() > 0;
    }

    private static final boolean isNotBlank(java.lang.CharSequence $this$isNotBlank) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isNotBlank, "<this>");
        return !kotlin.text.StringsKt.isBlank($this$isNotBlank);
    }

    private static final boolean isNullOrBlank(java.lang.CharSequence $this$isNullOrBlank) {
        return $this$isNullOrBlank == null || kotlin.text.StringsKt.isBlank($this$isNullOrBlank);
    }

    public static final kotlin.collections.CharIterator iterator(final java.lang.CharSequence $this$iterator) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$iterator, "<this>");
        return new kotlin.collections.CharIterator() { // from class: kotlin.text.StringsKt__StringsKt.iterator.1
            private int index;

            @Override // kotlin.collections.CharIterator
            public char nextChar() {
                java.lang.CharSequence charSequence = $this$iterator;
                int i = this.index;
                this.index = i + 1;
                return charSequence.charAt(i);
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < $this$iterator.length();
            }
        };
    }

    private static final java.lang.String orEmpty(java.lang.String $this$orEmpty) {
        return $this$orEmpty == null ? "" : $this$orEmpty;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <C extends java.lang.CharSequence & R, R> R ifEmpty(C c, kotlin.jvm.functions.Function0<? extends R> defaultValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return c.length() == 0 ? defaultValue.invoke() : c;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <C extends java.lang.CharSequence & R, R> R ifBlank(C c, kotlin.jvm.functions.Function0<? extends R> defaultValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return kotlin.text.StringsKt.isBlank(c) ? defaultValue.invoke() : c;
    }

    public static final kotlin.ranges.IntRange getIndices(java.lang.CharSequence $this$indices) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$indices, "<this>");
        return new kotlin.ranges.IntRange(0, $this$indices.length() - 1);
    }

    public static final int getLastIndex(java.lang.CharSequence $this$lastIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastIndex, "<this>");
        return $this$lastIndex.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(java.lang.CharSequence $this$hasSurrogatePairAt, int index) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hasSurrogatePairAt, "<this>");
        return new kotlin.ranges.IntRange(0, $this$hasSurrogatePairAt.length() + (-2)).contains(index) && java.lang.Character.isHighSurrogate($this$hasSurrogatePairAt.charAt(index)) && java.lang.Character.isLowSurrogate($this$hasSurrogatePairAt.charAt(index + 1));
    }

    public static final java.lang.String substring(java.lang.String $this$substring, kotlin.ranges.IntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substring, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        java.lang.String strSubstring = $this$substring.substring(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static final java.lang.CharSequence subSequence(java.lang.CharSequence $this$subSequence, kotlin.ranges.IntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$subSequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        return $this$subSequence.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    @kotlin.Deprecated(message = "Use parameters named startIndex and endIndex.", replaceWith = @kotlin.ReplaceWith(expression = "subSequence(startIndex = start, endIndex = end)", imports = {}))
    private static final java.lang.CharSequence subSequence(java.lang.String $this$subSequence, int start, int end) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$subSequence, "<this>");
        return $this$subSequence.subSequence(start, end);
    }

    private static final java.lang.String substring(java.lang.CharSequence $this$substring, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substring, "<this>");
        return $this$substring.subSequence(startIndex, endIndex).toString();
    }

    static /* synthetic */ java.lang.String substring$default(java.lang.CharSequence $this$substring_u24default, int startIndex, int endIndex, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            endIndex = $this$substring_u24default.length();
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substring_u24default, "<this>");
        return $this$substring_u24default.subSequence(startIndex, endIndex).toString();
    }

    public static final java.lang.String substring(java.lang.CharSequence $this$substring, kotlin.ranges.IntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substring, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        return $this$substring.subSequence(range.getStart().intValue(), range.getEndInclusive().intValue() + 1).toString();
    }

    public static /* synthetic */ java.lang.String substringBefore$default(java.lang.String str, char c, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return kotlin.text.StringsKt.substringBefore(str, c, str2);
    }

    public static final java.lang.String substringBefore(java.lang.String $this$substringBefore, char delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringBefore, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$substringBefore, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringBefore.substring(0, index);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ java.lang.String substringBefore$default(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.substringBefore(str, str2, str3);
    }

    public static final java.lang.String substringBefore(java.lang.String $this$substringBefore, java.lang.String delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringBefore, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$substringBefore, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringBefore.substring(0, index);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ java.lang.String substringAfter$default(java.lang.String str, char c, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return kotlin.text.StringsKt.substringAfter(str, c, str2);
    }

    public static final java.lang.String substringAfter(java.lang.String $this$substringAfter, char delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringAfter, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$substringAfter, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringAfter.substring(index + 1, $this$substringAfter.length());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ java.lang.String substringAfter$default(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.substringAfter(str, str2, str3);
    }

    public static final java.lang.String substringAfter(java.lang.String $this$substringAfter, java.lang.String delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringAfter, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$substringAfter, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringAfter.substring(delimiter.length() + index, $this$substringAfter.length());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ java.lang.String substringBeforeLast$default(java.lang.String str, char c, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return kotlin.text.StringsKt.substringBeforeLast(str, c, str2);
    }

    public static final java.lang.String substringBeforeLast(java.lang.String $this$substringBeforeLast, char delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringBeforeLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringBeforeLast.substring(0, index);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ java.lang.String substringBeforeLast$default(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.substringBeforeLast(str, str2, str3);
    }

    public static final java.lang.String substringBeforeLast(java.lang.String $this$substringBeforeLast, java.lang.String delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringBeforeLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$substringBeforeLast, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringBeforeLast.substring(0, index);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ java.lang.String substringAfterLast$default(java.lang.String str, char c, java.lang.String str2, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str2 = str;
        }
        return kotlin.text.StringsKt.substringAfterLast(str, c, str2);
    }

    public static final java.lang.String substringAfterLast(java.lang.String $this$substringAfterLast, char delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringAfterLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringAfterLast.substring(index + 1, $this$substringAfterLast.length());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static /* synthetic */ java.lang.String substringAfterLast$default(java.lang.String str, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.substringAfterLast(str, str2, str3);
    }

    public static final java.lang.String substringAfterLast(java.lang.String $this$substringAfterLast, java.lang.String delimiter, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$substringAfterLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$substringAfterLast, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        java.lang.String strSubstring = $this$substringAfterLast.substring(delimiter.length() + index, $this$substringAfterLast.length());
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return strSubstring;
    }

    public static final java.lang.CharSequence replaceRange(java.lang.CharSequence $this$replaceRange, int startIndex, int endIndex, java.lang.CharSequence replacement) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        if (endIndex < startIndex) {
            throw new java.lang.IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sb.append($this$replaceRange, 0, startIndex), "append(...)");
        sb.append(replacement);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sb.append($this$replaceRange, endIndex, $this$replaceRange.length()), "append(...)");
        return sb;
    }

    private static final java.lang.String replaceRange(java.lang.String $this$replaceRange, int startIndex, int endIndex, java.lang.CharSequence replacement) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceRange, startIndex, endIndex, replacement).toString();
    }

    public static final java.lang.CharSequence replaceRange(java.lang.CharSequence $this$replaceRange, kotlin.ranges.IntRange range, java.lang.CharSequence replacement) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        return kotlin.text.StringsKt.replaceRange($this$replaceRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1, replacement);
    }

    private static final java.lang.String replaceRange(java.lang.String $this$replaceRange, kotlin.ranges.IntRange range, java.lang.CharSequence replacement) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceRange, range, replacement).toString();
    }

    public static final java.lang.CharSequence removeRange(java.lang.CharSequence $this$removeRange, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        if (endIndex < startIndex) {
            throw new java.lang.IndexOutOfBoundsException("End index (" + endIndex + ") is less than start index (" + startIndex + ").");
        }
        if (endIndex == startIndex) {
            return $this$removeRange.subSequence(0, $this$removeRange.length());
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder($this$removeRange.length() - (endIndex - startIndex));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sb.append($this$removeRange, 0, startIndex), "append(...)");
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(sb.append($this$removeRange, endIndex, $this$removeRange.length()), "append(...)");
        return sb;
    }

    private static final java.lang.String removeRange(java.lang.String $this$removeRange, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        return kotlin.text.StringsKt.removeRange((java.lang.CharSequence) $this$removeRange, startIndex, endIndex).toString();
    }

    public static final java.lang.CharSequence removeRange(java.lang.CharSequence $this$removeRange, kotlin.ranges.IntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        return kotlin.text.StringsKt.removeRange($this$removeRange, range.getStart().intValue(), range.getEndInclusive().intValue() + 1);
    }

    private static final java.lang.String removeRange(java.lang.String $this$removeRange, kotlin.ranges.IntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeRange, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        return kotlin.text.StringsKt.removeRange((java.lang.CharSequence) $this$removeRange, range).toString();
    }

    public static final java.lang.CharSequence removePrefix(java.lang.CharSequence $this$removePrefix, java.lang.CharSequence prefix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removePrefix, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (kotlin.text.StringsKt.startsWith$default($this$removePrefix, prefix, false, 2, (java.lang.Object) null)) {
            return $this$removePrefix.subSequence(prefix.length(), $this$removePrefix.length());
        }
        return $this$removePrefix.subSequence(0, $this$removePrefix.length());
    }

    public static final java.lang.String removePrefix(java.lang.String $this$removePrefix, java.lang.CharSequence prefix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removePrefix, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (kotlin.text.StringsKt.startsWith$default((java.lang.CharSequence) $this$removePrefix, prefix, false, 2, (java.lang.Object) null)) {
            java.lang.String strSubstring = $this$removePrefix.substring(prefix.length());
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            return strSubstring;
        }
        return $this$removePrefix;
    }

    public static final java.lang.CharSequence removeSuffix(java.lang.CharSequence $this$removeSuffix, java.lang.CharSequence suffix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeSuffix, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (kotlin.text.StringsKt.endsWith$default($this$removeSuffix, suffix, false, 2, (java.lang.Object) null)) {
            return $this$removeSuffix.subSequence(0, $this$removeSuffix.length() - suffix.length());
        }
        return $this$removeSuffix.subSequence(0, $this$removeSuffix.length());
    }

    public static final java.lang.String removeSuffix(java.lang.String $this$removeSuffix, java.lang.CharSequence suffix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeSuffix, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (kotlin.text.StringsKt.endsWith$default((java.lang.CharSequence) $this$removeSuffix, suffix, false, 2, (java.lang.Object) null)) {
            java.lang.String strSubstring = $this$removeSuffix.substring(0, $this$removeSuffix.length() - suffix.length());
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            return strSubstring;
        }
        return $this$removeSuffix;
    }

    public static final java.lang.CharSequence removeSurrounding(java.lang.CharSequence $this$removeSurrounding, java.lang.CharSequence prefix, java.lang.CharSequence suffix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(suffix, "suffix");
        if ($this$removeSurrounding.length() >= prefix.length() + suffix.length() && kotlin.text.StringsKt.startsWith$default($this$removeSurrounding, prefix, false, 2, (java.lang.Object) null) && kotlin.text.StringsKt.endsWith$default($this$removeSurrounding, suffix, false, 2, (java.lang.Object) null)) {
            return $this$removeSurrounding.subSequence(prefix.length(), $this$removeSurrounding.length() - suffix.length());
        }
        return $this$removeSurrounding.subSequence(0, $this$removeSurrounding.length());
    }

    public static final java.lang.String removeSurrounding(java.lang.String $this$removeSurrounding, java.lang.CharSequence prefix, java.lang.CharSequence suffix) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(suffix, "suffix");
        if ($this$removeSurrounding.length() >= prefix.length() + suffix.length() && kotlin.text.StringsKt.startsWith$default((java.lang.CharSequence) $this$removeSurrounding, prefix, false, 2, (java.lang.Object) null) && kotlin.text.StringsKt.endsWith$default((java.lang.CharSequence) $this$removeSurrounding, suffix, false, 2, (java.lang.Object) null)) {
            java.lang.String strSubstring = $this$removeSurrounding.substring(prefix.length(), $this$removeSurrounding.length() - suffix.length());
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            return strSubstring;
        }
        return $this$removeSurrounding;
    }

    public static final java.lang.CharSequence removeSurrounding(java.lang.CharSequence $this$removeSurrounding, java.lang.CharSequence delimiter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        return kotlin.text.StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    public static final java.lang.String removeSurrounding(java.lang.String $this$removeSurrounding, java.lang.CharSequence delimiter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$removeSurrounding, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        return kotlin.text.StringsKt.removeSurrounding($this$removeSurrounding, delimiter, delimiter);
    }

    public static /* synthetic */ java.lang.String replaceBefore$default(java.lang.String str, char c, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.replaceBefore(str, c, str2, str3);
    }

    public static final java.lang.String replaceBefore(java.lang.String $this$replaceBefore, char delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceBefore, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (java.lang.Object) null);
        return index == -1 ? missingDelimiterValue : kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceBefore, 0, index, (java.lang.CharSequence) replacement).toString();
    }

    public static /* synthetic */ java.lang.String replaceBefore$default(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return kotlin.text.StringsKt.replaceBefore(str, str2, str3, str4);
    }

    public static final java.lang.String replaceBefore(java.lang.String $this$replaceBefore, java.lang.String delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceBefore, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$replaceBefore, delimiter, 0, false, 6, (java.lang.Object) null);
        return index == -1 ? missingDelimiterValue : kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceBefore, 0, index, (java.lang.CharSequence) replacement).toString();
    }

    public static /* synthetic */ java.lang.String replaceAfter$default(java.lang.String str, char c, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.replaceAfter(str, c, str2, str3);
    }

    public static final java.lang.String replaceAfter(java.lang.String $this$replaceAfter, char delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceAfter, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceAfter, index + 1, $this$replaceAfter.length(), (java.lang.CharSequence) replacement).toString();
    }

    public static /* synthetic */ java.lang.String replaceAfter$default(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return kotlin.text.StringsKt.replaceAfter(str, str2, str3, str4);
    }

    public static final java.lang.String replaceAfter(java.lang.String $this$replaceAfter, java.lang.String delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceAfter, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.indexOf$default((java.lang.CharSequence) $this$replaceAfter, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceAfter, delimiter.length() + index, $this$replaceAfter.length(), (java.lang.CharSequence) replacement).toString();
    }

    public static /* synthetic */ java.lang.String replaceAfterLast$default(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return kotlin.text.StringsKt.replaceAfterLast(str, str2, str3, str4);
    }

    public static final java.lang.String replaceAfterLast(java.lang.String $this$replaceAfterLast, java.lang.String delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceAfterLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceAfterLast, delimiter.length() + index, $this$replaceAfterLast.length(), (java.lang.CharSequence) replacement).toString();
    }

    public static /* synthetic */ java.lang.String replaceAfterLast$default(java.lang.String str, char c, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.replaceAfterLast(str, c, str2, str3);
    }

    public static final java.lang.String replaceAfterLast(java.lang.String $this$replaceAfterLast, char delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceAfterLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$replaceAfterLast, delimiter, 0, false, 6, (java.lang.Object) null);
        if (index == -1) {
            return missingDelimiterValue;
        }
        return kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceAfterLast, index + 1, $this$replaceAfterLast.length(), (java.lang.CharSequence) replacement).toString();
    }

    public static /* synthetic */ java.lang.String replaceBeforeLast$default(java.lang.String str, char c, java.lang.String str2, java.lang.String str3, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str3 = str;
        }
        return kotlin.text.StringsKt.replaceBeforeLast(str, c, str2, str3);
    }

    public static final java.lang.String replaceBeforeLast(java.lang.String $this$replaceBeforeLast, char delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceBeforeLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (java.lang.Object) null);
        return index == -1 ? missingDelimiterValue : kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceBeforeLast, 0, index, (java.lang.CharSequence) replacement).toString();
    }

    public static /* synthetic */ java.lang.String replaceBeforeLast$default(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.String str4, int i, java.lang.Object obj) {
        if ((i & 4) != 0) {
            str4 = str;
        }
        return kotlin.text.StringsKt.replaceBeforeLast(str, str2, str3, str4);
    }

    public static final java.lang.String replaceBeforeLast(java.lang.String $this$replaceBeforeLast, java.lang.String delimiter, java.lang.String replacement, java.lang.String missingDelimiterValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceBeforeLast, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiter, "delimiter");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(missingDelimiterValue, "missingDelimiterValue");
        int index = kotlin.text.StringsKt.lastIndexOf$default((java.lang.CharSequence) $this$replaceBeforeLast, delimiter, 0, false, 6, (java.lang.Object) null);
        return index == -1 ? missingDelimiterValue : kotlin.text.StringsKt.replaceRange((java.lang.CharSequence) $this$replaceBeforeLast, 0, index, (java.lang.CharSequence) replacement).toString();
    }

    private static final java.lang.String replace(java.lang.CharSequence $this$replace, kotlin.text.Regex regex, java.lang.String replacement) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replace, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        return regex.replace($this$replace, replacement);
    }

    private static final java.lang.String replace(java.lang.CharSequence $this$replace, kotlin.text.Regex regex, kotlin.jvm.functions.Function1<? super kotlin.text.MatchResult, ? extends java.lang.CharSequence> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replace, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        return regex.replace($this$replace, transform);
    }

    private static final java.lang.String replaceFirst(java.lang.CharSequence $this$replaceFirst, kotlin.text.Regex regex, java.lang.String replacement) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceFirst, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(replacement, "replacement");
        return regex.replaceFirst($this$replaceFirst, replacement);
    }

    private static final java.lang.String replaceFirstCharWithChar(java.lang.String $this$replaceFirstChar, kotlin.jvm.functions.Function1<? super java.lang.Character, java.lang.Character> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceFirstChar, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        if (!($this$replaceFirstChar.length() > 0)) {
            return $this$replaceFirstChar;
        }
        char cCharValue = transform.invoke(java.lang.Character.valueOf($this$replaceFirstChar.charAt(0))).charValue();
        java.lang.String strSubstring = $this$replaceFirstChar.substring(1);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return cCharValue + strSubstring;
    }

    private static final java.lang.String replaceFirstCharWithCharSequence(java.lang.String $this$replaceFirstChar, kotlin.jvm.functions.Function1<? super java.lang.Character, ? extends java.lang.CharSequence> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$replaceFirstChar, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        if (!($this$replaceFirstChar.length() > 0)) {
            return $this$replaceFirstChar;
        }
        java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append((java.lang.Object) transform.invoke(java.lang.Character.valueOf($this$replaceFirstChar.charAt(0))));
        java.lang.String strSubstring = $this$replaceFirstChar.substring(1);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
        return sbAppend.append(strSubstring).toString();
    }

    private static final boolean matches(java.lang.CharSequence $this$matches, kotlin.text.Regex regex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$matches, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.matches($this$matches);
    }

    public static final boolean regionMatchesImpl(java.lang.CharSequence $this$regionMatchesImpl, int thisOffset, java.lang.CharSequence other, int otherOffset, int length, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$regionMatchesImpl, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        if (otherOffset < 0 || thisOffset < 0 || thisOffset > $this$regionMatchesImpl.length() - length || otherOffset > other.length() - length) {
            return false;
        }
        for (int index = 0; index < length; index++) {
            if (!kotlin.text.CharsKt.equals($this$regionMatchesImpl.charAt(thisOffset + index), other.charAt(otherOffset + index), ignoreCase)) {
                return false;
            }
        }
        return true;
    }

    public static /* synthetic */ boolean startsWith$default(java.lang.CharSequence charSequence, char c, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.startsWith(charSequence, c, z);
    }

    public static final boolean startsWith(java.lang.CharSequence $this$startsWith, char c, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        return $this$startsWith.length() > 0 && kotlin.text.CharsKt.equals($this$startsWith.charAt(0), c, ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(java.lang.CharSequence charSequence, char c, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.endsWith(charSequence, c, z);
    }

    public static final boolean endsWith(java.lang.CharSequence $this$endsWith, char c, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        return $this$endsWith.length() > 0 && kotlin.text.CharsKt.equals($this$endsWith.charAt(kotlin.text.StringsKt.getLastIndex($this$endsWith)), c, ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.startsWith(charSequence, charSequence2, z);
    }

    public static final boolean startsWith(java.lang.CharSequence $this$startsWith, java.lang.CharSequence prefix, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase && ($this$startsWith instanceof java.lang.String) && (prefix instanceof java.lang.String)) {
            return kotlin.text.StringsKt.startsWith$default((java.lang.String) $this$startsWith, (java.lang.String) prefix, false, 2, (java.lang.Object) null);
        }
        return kotlin.text.StringsKt.regionMatchesImpl($this$startsWith, 0, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean startsWith$default(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.startsWith(charSequence, charSequence2, i, z);
    }

    public static final boolean startsWith(java.lang.CharSequence $this$startsWith, java.lang.CharSequence prefix, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$startsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!ignoreCase && ($this$startsWith instanceof java.lang.String) && (prefix instanceof java.lang.String)) {
            return kotlin.text.StringsKt.startsWith$default((java.lang.String) $this$startsWith, (java.lang.String) prefix, startIndex, false, 4, (java.lang.Object) null);
        }
        return kotlin.text.StringsKt.regionMatchesImpl($this$startsWith, startIndex, prefix, 0, prefix.length(), ignoreCase);
    }

    public static /* synthetic */ boolean endsWith$default(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.endsWith(charSequence, charSequence2, z);
    }

    public static final boolean endsWith(java.lang.CharSequence $this$endsWith, java.lang.CharSequence suffix, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$endsWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (!ignoreCase && ($this$endsWith instanceof java.lang.String) && (suffix instanceof java.lang.String)) {
            return kotlin.text.StringsKt.endsWith$default((java.lang.String) $this$endsWith, (java.lang.String) suffix, false, 2, (java.lang.Object) null);
        }
        return kotlin.text.StringsKt.regionMatchesImpl($this$endsWith, $this$endsWith.length() - suffix.length(), suffix, 0, suffix.length(), ignoreCase);
    }

    public static /* synthetic */ java.lang.String commonPrefixWith$default(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.commonPrefixWith(charSequence, charSequence2, z);
    }

    public static final java.lang.String commonPrefixWith(java.lang.CharSequence $this$commonPrefixWith, java.lang.CharSequence other, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$commonPrefixWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int shortestLength = java.lang.Math.min($this$commonPrefixWith.length(), other.length());
        int i = 0;
        while (i < shortestLength && kotlin.text.CharsKt.equals($this$commonPrefixWith.charAt(i), other.charAt(i), ignoreCase)) {
            i++;
        }
        if (kotlin.text.StringsKt.hasSurrogatePairAt($this$commonPrefixWith, i - 1) || kotlin.text.StringsKt.hasSurrogatePairAt(other, i - 1)) {
            i--;
        }
        return $this$commonPrefixWith.subSequence(0, i).toString();
    }

    public static /* synthetic */ java.lang.String commonSuffixWith$default(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.commonSuffixWith(charSequence, charSequence2, z);
    }

    public static final java.lang.String commonSuffixWith(java.lang.CharSequence $this$commonSuffixWith, java.lang.CharSequence other, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$commonSuffixWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        int thisLength = $this$commonSuffixWith.length();
        int otherLength = other.length();
        int shortestLength = java.lang.Math.min(thisLength, otherLength);
        int i = 0;
        while (i < shortestLength && kotlin.text.CharsKt.equals($this$commonSuffixWith.charAt((thisLength - i) - 1), other.charAt((otherLength - i) - 1), ignoreCase)) {
            i++;
        }
        if (kotlin.text.StringsKt.hasSurrogatePairAt($this$commonSuffixWith, (thisLength - i) - 1) || kotlin.text.StringsKt.hasSurrogatePairAt(other, (otherLength - i) - 1)) {
            i--;
        }
        return $this$commonSuffixWith.subSequence(thisLength - i, thisLength).toString();
    }

    public static /* synthetic */ int indexOfAny$default(java.lang.CharSequence charSequence, char[] cArr, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.indexOfAny(charSequence, cArr, i, z);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    public static final int indexOfAny(java.lang.CharSequence $this$indexOfAny, char[] chars, int startIndex, boolean ignoreCase) {
        boolean z;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$indexOfAny, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        if (!ignoreCase && chars.length == 1 && ($this$indexOfAny instanceof java.lang.String)) {
            return ((java.lang.String) $this$indexOfAny).indexOf(kotlin.collections.ArraysKt.single(chars), startIndex);
        }
        ?? it = new kotlin.ranges.IntRange(kotlin.ranges.RangesKt.coerceAtLeast(startIndex, 0), kotlin.text.StringsKt.getLastIndex($this$indexOfAny)).iterator();
        while (it.hasNext()) {
            int index = it.nextInt();
            char charAtIndex = $this$indexOfAny.charAt(index);
            int length = chars.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    char element$iv = chars[i];
                    if (kotlin.text.CharsKt.equals(element$iv, charAtIndex, ignoreCase)) {
                        z = true;
                        break;
                    }
                    i++;
                } else {
                    z = false;
                    break;
                }
            }
            if (z) {
                return index;
            }
        }
        return -1;
    }

    public static /* synthetic */ int lastIndexOfAny$default(java.lang.CharSequence charSequence, char[] cArr, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = kotlin.text.StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.lastIndexOfAny(charSequence, cArr, i, z);
    }

    public static final int lastIndexOfAny(java.lang.CharSequence $this$lastIndexOfAny, char[] chars, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastIndexOfAny, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(chars, "chars");
        if (!ignoreCase && chars.length == 1 && ($this$lastIndexOfAny instanceof java.lang.String)) {
            return ((java.lang.String) $this$lastIndexOfAny).lastIndexOf(kotlin.collections.ArraysKt.single(chars), startIndex);
        }
        for (int index = kotlin.ranges.RangesKt.coerceAtMost(startIndex, kotlin.text.StringsKt.getLastIndex($this$lastIndexOfAny)); -1 < index; index--) {
            char charAtIndex = $this$lastIndexOfAny.charAt(index);
            int length = chars.length;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                char element$iv = chars[i];
                if (kotlin.text.CharsKt.equals(element$iv, charAtIndex, ignoreCase)) {
                    z = true;
                    break;
                }
                i++;
            }
            if (z) {
                return index;
            }
        }
        return -1;
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2, int i, int i2, boolean z, boolean z2, int i3, java.lang.Object obj) {
        if ((i3 & 16) != 0) {
            z2 = false;
        }
        return indexOf$StringsKt__StringsKt(charSequence, charSequence2, i, i2, z, z2);
    }

    private static final int indexOf$StringsKt__StringsKt(java.lang.CharSequence $this$indexOf, java.lang.CharSequence other, int startIndex, int endIndex, boolean ignoreCase, boolean last) {
        kotlin.ranges.IntRange intRangeDownTo;
        if (last) {
            intRangeDownTo = kotlin.ranges.RangesKt.downTo(kotlin.ranges.RangesKt.coerceAtMost(startIndex, kotlin.text.StringsKt.getLastIndex($this$indexOf)), kotlin.ranges.RangesKt.coerceAtLeast(endIndex, 0));
        } else {
            intRangeDownTo = new kotlin.ranges.IntRange(kotlin.ranges.RangesKt.coerceAtLeast(startIndex, 0), kotlin.ranges.RangesKt.coerceAtMost(endIndex, $this$indexOf.length()));
        }
        kotlin.ranges.IntProgression indices = intRangeDownTo;
        if (($this$indexOf instanceof java.lang.String) && (other instanceof java.lang.String)) {
            int index = indices.getFirst();
            int last2 = indices.getLast();
            int step = indices.getStep();
            if ((step > 0 && index <= last2) || (step < 0 && last2 <= index)) {
                while (!kotlin.text.StringsKt.regionMatches((java.lang.String) other, 0, (java.lang.String) $this$indexOf, index, other.length(), ignoreCase)) {
                    if (index == last2) {
                        return -1;
                    }
                    index += step;
                }
                return index;
            }
            return -1;
        }
        int index2 = indices.getFirst();
        int last3 = indices.getLast();
        int step2 = indices.getStep();
        if ((step2 > 0 && index2 <= last3) || (step2 < 0 && last3 <= index2)) {
            while (!kotlin.text.StringsKt.regionMatchesImpl(other, 0, $this$indexOf, index2, other.length(), ignoreCase)) {
                if (index2 == last3) {
                    return -1;
                }
                index2 += step2;
            }
            return index2;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final kotlin.Pair<java.lang.Integer, java.lang.String> findAnyOf$StringsKt__StringsKt(java.lang.CharSequence $this$findAnyOf, java.util.Collection<java.lang.String> collection, int startIndex, boolean ignoreCase, boolean last) {
        java.lang.Object element$iv;
        java.lang.Object element$iv2;
        if (!ignoreCase && collection.size() == 1) {
            java.lang.String string = (java.lang.String) kotlin.collections.CollectionsKt.single(collection);
            int index = !last ? kotlin.text.StringsKt.indexOf$default($this$findAnyOf, string, startIndex, false, 4, (java.lang.Object) null) : kotlin.text.StringsKt.lastIndexOf$default($this$findAnyOf, string, startIndex, false, 4, (java.lang.Object) null);
            if (index < 0) {
                return null;
            }
            return kotlin.TuplesKt.to(java.lang.Integer.valueOf(index), string);
        }
        kotlin.ranges.IntProgression indices = !last ? new kotlin.ranges.IntRange(kotlin.ranges.RangesKt.coerceAtLeast(startIndex, 0), $this$findAnyOf.length()) : kotlin.ranges.RangesKt.downTo(kotlin.ranges.RangesKt.coerceAtMost(startIndex, kotlin.text.StringsKt.getLastIndex($this$findAnyOf)), 0);
        if ($this$findAnyOf instanceof java.lang.String) {
            int index2 = indices.getFirst();
            int last2 = indices.getLast();
            int step = indices.getStep();
            if ((step > 0 && index2 <= last2) || (step < 0 && last2 <= index2)) {
                int index3 = index2;
                while (true) {
                    java.util.Collection<java.lang.String> $this$firstOrNull$iv = collection;
                    java.util.Iterator it = $this$firstOrNull$iv.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            element$iv2 = it.next();
                            java.lang.String it2 = (java.lang.String) element$iv2;
                            if (kotlin.text.StringsKt.regionMatches(it2, 0, (java.lang.String) $this$findAnyOf, index3, it2.length(), ignoreCase)) {
                                break;
                            }
                        } else {
                            element$iv2 = null;
                            break;
                        }
                    }
                    java.lang.String matchingString = (java.lang.String) element$iv2;
                    if (matchingString == null) {
                        if (index3 == last2) {
                            break;
                        }
                        index3 += step;
                    } else {
                        return kotlin.TuplesKt.to(java.lang.Integer.valueOf(index3), matchingString);
                    }
                }
            }
        } else {
            int index4 = indices.getFirst();
            int last3 = indices.getLast();
            int step2 = indices.getStep();
            if ((step2 > 0 && index4 <= last3) || (step2 < 0 && last3 <= index4)) {
                int index5 = index4;
                while (true) {
                    java.util.Collection<java.lang.String> $this$firstOrNull$iv2 = collection;
                    java.util.Iterator it3 = $this$firstOrNull$iv2.iterator();
                    while (true) {
                        if (it3.hasNext()) {
                            element$iv = it3.next();
                            java.lang.String it4 = (java.lang.String) element$iv;
                            if (kotlin.text.StringsKt.regionMatchesImpl(it4, 0, $this$findAnyOf, index5, it4.length(), ignoreCase)) {
                                break;
                            }
                        } else {
                            element$iv = null;
                            break;
                        }
                    }
                    java.lang.String matchingString2 = (java.lang.String) element$iv;
                    if (matchingString2 == null) {
                        if (index5 == last3) {
                            break;
                        }
                        index5 += step2;
                    } else {
                        return kotlin.TuplesKt.to(java.lang.Integer.valueOf(index5), matchingString2);
                    }
                }
            }
        }
        return null;
    }

    public static /* synthetic */ kotlin.Pair findAnyOf$default(java.lang.CharSequence charSequence, java.util.Collection collection, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.findAnyOf(charSequence, collection, i, z);
    }

    public static final kotlin.Pair<java.lang.Integer, java.lang.String> findAnyOf(java.lang.CharSequence $this$findAnyOf, java.util.Collection<java.lang.String> strings, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$findAnyOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(strings, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findAnyOf, strings, startIndex, ignoreCase, false);
    }

    public static /* synthetic */ kotlin.Pair findLastAnyOf$default(java.lang.CharSequence charSequence, java.util.Collection collection, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = kotlin.text.StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.findLastAnyOf(charSequence, collection, i, z);
    }

    public static final kotlin.Pair<java.lang.Integer, java.lang.String> findLastAnyOf(java.lang.CharSequence $this$findLastAnyOf, java.util.Collection<java.lang.String> strings, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$findLastAnyOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(strings, "strings");
        return findAnyOf$StringsKt__StringsKt($this$findLastAnyOf, strings, startIndex, ignoreCase, true);
    }

    public static /* synthetic */ int indexOfAny$default(java.lang.CharSequence charSequence, java.util.Collection collection, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.indexOfAny(charSequence, (java.util.Collection<java.lang.String>) collection, i, z);
    }

    public static final int indexOfAny(java.lang.CharSequence $this$indexOfAny, java.util.Collection<java.lang.String> strings, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$indexOfAny, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(strings, "strings");
        kotlin.Pair<java.lang.Integer, java.lang.String> pairFindAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$indexOfAny, strings, startIndex, ignoreCase, false);
        if (pairFindAnyOf$StringsKt__StringsKt != null) {
            return pairFindAnyOf$StringsKt__StringsKt.getFirst().intValue();
        }
        return -1;
    }

    public static /* synthetic */ int lastIndexOfAny$default(java.lang.CharSequence charSequence, java.util.Collection collection, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = kotlin.text.StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.lastIndexOfAny(charSequence, (java.util.Collection<java.lang.String>) collection, i, z);
    }

    public static final int lastIndexOfAny(java.lang.CharSequence $this$lastIndexOfAny, java.util.Collection<java.lang.String> strings, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastIndexOfAny, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(strings, "strings");
        kotlin.Pair<java.lang.Integer, java.lang.String> pairFindAnyOf$StringsKt__StringsKt = findAnyOf$StringsKt__StringsKt($this$lastIndexOfAny, strings, startIndex, ignoreCase, true);
        if (pairFindAnyOf$StringsKt__StringsKt != null) {
            return pairFindAnyOf$StringsKt__StringsKt.getFirst().intValue();
        }
        return -1;
    }

    public static /* synthetic */ int indexOf$default(java.lang.CharSequence charSequence, char c, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.indexOf(charSequence, c, i, z);
    }

    public static final int indexOf(java.lang.CharSequence $this$indexOf, char c, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$indexOf, "<this>");
        return (ignoreCase || !($this$indexOf instanceof java.lang.String)) ? kotlin.text.StringsKt.indexOfAny($this$indexOf, new char[]{c}, startIndex, ignoreCase) : ((java.lang.String) $this$indexOf).indexOf(c, startIndex);
    }

    public static /* synthetic */ int indexOf$default(java.lang.CharSequence charSequence, java.lang.String str, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.indexOf(charSequence, str, i, z);
    }

    public static final int indexOf(java.lang.CharSequence $this$indexOf, java.lang.String string, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$indexOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(string, "string");
        if (ignoreCase || !($this$indexOf instanceof java.lang.String)) {
            return indexOf$StringsKt__StringsKt$default($this$indexOf, string, startIndex, $this$indexOf.length(), ignoreCase, false, 16, null);
        }
        return ((java.lang.String) $this$indexOf).indexOf(string, startIndex);
    }

    public static /* synthetic */ int lastIndexOf$default(java.lang.CharSequence charSequence, char c, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = kotlin.text.StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.lastIndexOf(charSequence, c, i, z);
    }

    public static final int lastIndexOf(java.lang.CharSequence $this$lastIndexOf, char c, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastIndexOf, "<this>");
        return (ignoreCase || !($this$lastIndexOf instanceof java.lang.String)) ? kotlin.text.StringsKt.lastIndexOfAny($this$lastIndexOf, new char[]{c}, startIndex, ignoreCase) : ((java.lang.String) $this$lastIndexOf).lastIndexOf(c, startIndex);
    }

    public static /* synthetic */ int lastIndexOf$default(java.lang.CharSequence charSequence, java.lang.String str, int i, boolean z, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            i = kotlin.text.StringsKt.getLastIndex(charSequence);
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.lastIndexOf(charSequence, str, i, z);
    }

    public static final int lastIndexOf(java.lang.CharSequence $this$lastIndexOf, java.lang.String string, int startIndex, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lastIndexOf, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(string, "string");
        if (ignoreCase || !($this$lastIndexOf instanceof java.lang.String)) {
            return indexOf$StringsKt__StringsKt($this$lastIndexOf, string, startIndex, 0, ignoreCase, true);
        }
        return ((java.lang.String) $this$lastIndexOf).lastIndexOf(string, startIndex);
    }

    public static /* synthetic */ boolean contains$default(java.lang.CharSequence charSequence, java.lang.CharSequence charSequence2, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.contains(charSequence, charSequence2, z);
    }

    public static final boolean contains(java.lang.CharSequence $this$contains, java.lang.CharSequence other, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(other, "other");
        return other instanceof java.lang.String ? kotlin.text.StringsKt.indexOf$default($this$contains, (java.lang.String) other, 0, ignoreCase, 2, (java.lang.Object) null) >= 0 : indexOf$StringsKt__StringsKt$default($this$contains, other, 0, $this$contains.length(), ignoreCase, false, 16, null) >= 0;
    }

    public static /* synthetic */ boolean contains$default(java.lang.CharSequence charSequence, char c, boolean z, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return kotlin.text.StringsKt.contains(charSequence, c, z);
    }

    public static final boolean contains(java.lang.CharSequence $this$contains, char c, boolean ignoreCase) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        return kotlin.text.StringsKt.indexOf$default($this$contains, c, 0, ignoreCase, 2, (java.lang.Object) null) >= 0;
    }

    private static final boolean contains(java.lang.CharSequence $this$contains, kotlin.text.Regex regex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$contains, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.containsMatchIn($this$contains);
    }

    static /* synthetic */ kotlin.sequences.Sequence rangesDelimitedBy$StringsKt__StringsKt$default(java.lang.CharSequence charSequence, char[] cArr, int i, boolean z, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, cArr, i, z, i2);
    }

    private static final kotlin.sequences.Sequence<kotlin.ranges.IntRange> rangesDelimitedBy$StringsKt__StringsKt(java.lang.CharSequence $this$rangesDelimitedBy, final char[] delimiters, int startIndex, final boolean ignoreCase, int limit) {
        kotlin.text.StringsKt.requireNonNegativeLimit(limit);
        return new kotlin.text.DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new kotlin.jvm.functions.Function2<java.lang.CharSequence, java.lang.Integer, kotlin.Pair<? extends java.lang.Integer, ? extends java.lang.Integer>>() { // from class: kotlin.text.StringsKt__StringsKt$rangesDelimitedBy$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ kotlin.Pair<? extends java.lang.Integer, ? extends java.lang.Integer> invoke(java.lang.CharSequence charSequence, java.lang.Integer num) {
                return invoke(charSequence, num.intValue());
            }

            public final kotlin.Pair<java.lang.Integer, java.lang.Integer> invoke(java.lang.CharSequence $receiver, int currentIndex) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter($receiver, "$this$$receiver");
                int it = kotlin.text.StringsKt.indexOfAny($receiver, delimiters, currentIndex, ignoreCase);
                if (it < 0) {
                    return null;
                }
                return kotlin.TuplesKt.to(java.lang.Integer.valueOf(it), 1);
            }
        });
    }

    static /* synthetic */ kotlin.sequences.Sequence rangesDelimitedBy$StringsKt__StringsKt$default(java.lang.CharSequence charSequence, java.lang.String[] strArr, int i, boolean z, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            z = false;
        }
        if ((i3 & 8) != 0) {
            i2 = 0;
        }
        return rangesDelimitedBy$StringsKt__StringsKt(charSequence, strArr, i, z, i2);
    }

    private static final kotlin.sequences.Sequence<kotlin.ranges.IntRange> rangesDelimitedBy$StringsKt__StringsKt(java.lang.CharSequence $this$rangesDelimitedBy, java.lang.String[] delimiters, int startIndex, final boolean ignoreCase, int limit) {
        kotlin.text.StringsKt.requireNonNegativeLimit(limit);
        final java.util.List delimitersList = kotlin.collections.ArraysKt.asList(delimiters);
        return new kotlin.text.DelimitedRangesSequence($this$rangesDelimitedBy, startIndex, limit, new kotlin.jvm.functions.Function2<java.lang.CharSequence, java.lang.Integer, kotlin.Pair<? extends java.lang.Integer, ? extends java.lang.Integer>>() { // from class: kotlin.text.StringsKt__StringsKt$rangesDelimitedBy$2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ kotlin.Pair<? extends java.lang.Integer, ? extends java.lang.Integer> invoke(java.lang.CharSequence charSequence, java.lang.Integer num) {
                return invoke(charSequence, num.intValue());
            }

            public final kotlin.Pair<java.lang.Integer, java.lang.Integer> invoke(java.lang.CharSequence $receiver, int currentIndex) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter($receiver, "$this$$receiver");
                kotlin.Pair it = kotlin.text.StringsKt__StringsKt.findAnyOf$StringsKt__StringsKt($receiver, delimitersList, currentIndex, ignoreCase, false);
                if (it != null) {
                    return kotlin.TuplesKt.to(it.getFirst(), java.lang.Integer.valueOf(((java.lang.String) it.getSecond()).length()));
                }
                return null;
            }
        });
    }

    public static final void requireNonNegativeLimit(int limit) {
        if (!(limit >= 0)) {
            throw new java.lang.IllegalArgumentException(("Limit must be non-negative, but was " + limit).toString());
        }
    }

    public static /* synthetic */ kotlin.sequences.Sequence splitToSequence$default(java.lang.CharSequence charSequence, java.lang.String[] strArr, boolean z, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return kotlin.text.StringsKt.splitToSequence(charSequence, strArr, z, i);
    }

    public static final kotlin.sequences.Sequence<java.lang.String> splitToSequence(final java.lang.CharSequence $this$splitToSequence, java.lang.String[] delimiters, boolean ignoreCase, int limit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        return kotlin.sequences.SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (java.lang.Object) null), new kotlin.jvm.functions.Function1<kotlin.ranges.IntRange, java.lang.String>() { // from class: kotlin.text.StringsKt__StringsKt.splitToSequence.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final java.lang.String invoke(kotlin.ranges.IntRange it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                return kotlin.text.StringsKt.substring($this$splitToSequence, it);
            }
        });
    }

    public static /* synthetic */ java.util.List split$default(java.lang.CharSequence charSequence, java.lang.String[] strArr, boolean z, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return kotlin.text.StringsKt.split(charSequence, strArr, z, i);
    }

    public static final java.util.List<java.lang.String> split(java.lang.CharSequence $this$split, java.lang.String[] delimiters, boolean ignoreCase, int limit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$split, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        if (delimiters.length == 1) {
            java.lang.String delimiter = delimiters[0];
            if (!(delimiter.length() == 0)) {
                return split$StringsKt__StringsKt($this$split, delimiter, ignoreCase, limit);
            }
        }
        java.lang.Iterable $this$map$iv = kotlin.sequences.SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (java.lang.Object) null));
        java.util.Collection destination$iv$iv = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (java.lang.Object item$iv$iv : $this$map$iv) {
            kotlin.ranges.IntRange it = (kotlin.ranges.IntRange) item$iv$iv;
            destination$iv$iv.add(kotlin.text.StringsKt.substring($this$split, it));
        }
        return (java.util.List) destination$iv$iv;
    }

    public static /* synthetic */ kotlin.sequences.Sequence splitToSequence$default(java.lang.CharSequence charSequence, char[] cArr, boolean z, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return kotlin.text.StringsKt.splitToSequence(charSequence, cArr, z, i);
    }

    public static final kotlin.sequences.Sequence<java.lang.String> splitToSequence(final java.lang.CharSequence $this$splitToSequence, char[] delimiters, boolean ignoreCase, int limit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        return kotlin.sequences.SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default($this$splitToSequence, delimiters, 0, ignoreCase, limit, 2, (java.lang.Object) null), new kotlin.jvm.functions.Function1<kotlin.ranges.IntRange, java.lang.String>() { // from class: kotlin.text.StringsKt__StringsKt.splitToSequence.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final java.lang.String invoke(kotlin.ranges.IntRange it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                return kotlin.text.StringsKt.substring($this$splitToSequence, it);
            }
        });
    }

    public static /* synthetic */ java.util.List split$default(java.lang.CharSequence charSequence, char[] cArr, boolean z, int i, int i2, java.lang.Object obj) {
        if ((i2 & 2) != 0) {
            z = false;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return kotlin.text.StringsKt.split(charSequence, cArr, z, i);
    }

    public static final java.util.List<java.lang.String> split(java.lang.CharSequence $this$split, char[] delimiters, boolean ignoreCase, int limit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$split, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delimiters, "delimiters");
        if (delimiters.length == 1) {
            return split$StringsKt__StringsKt($this$split, java.lang.String.valueOf(delimiters[0]), ignoreCase, limit);
        }
        java.lang.Iterable $this$map$iv = kotlin.sequences.SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default($this$split, delimiters, 0, ignoreCase, limit, 2, (java.lang.Object) null));
        java.util.Collection destination$iv$iv = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (java.lang.Object item$iv$iv : $this$map$iv) {
            kotlin.ranges.IntRange it = (kotlin.ranges.IntRange) item$iv$iv;
            destination$iv$iv.add(kotlin.text.StringsKt.substring($this$split, it));
        }
        return (java.util.List) destination$iv$iv;
    }

    private static final java.util.List<java.lang.String> split$StringsKt__StringsKt(java.lang.CharSequence $this$split, java.lang.String delimiter, boolean ignoreCase, int limit) {
        kotlin.text.StringsKt.requireNonNegativeLimit(limit);
        int currentOffset = 0;
        int nextIndex = kotlin.text.StringsKt.indexOf($this$split, delimiter, 0, ignoreCase);
        if (nextIndex != -1) {
            if (limit != 1) {
                boolean isLimited = limit > 0;
                java.util.ArrayList result = new java.util.ArrayList(isLimited ? kotlin.ranges.RangesKt.coerceAtMost(limit, 10) : 10);
                do {
                    result.add($this$split.subSequence(currentOffset, nextIndex).toString());
                    currentOffset = nextIndex + delimiter.length();
                    if (isLimited && result.size() == limit - 1) {
                        break;
                    }
                    nextIndex = kotlin.text.StringsKt.indexOf($this$split, delimiter, currentOffset, ignoreCase);
                } while (nextIndex != -1);
                result.add($this$split.subSequence(currentOffset, $this$split.length()).toString());
                return result;
            }
        }
        return kotlin.collections.CollectionsKt.listOf($this$split.toString());
    }

    private static final java.util.List<java.lang.String> split(java.lang.CharSequence $this$split, kotlin.text.Regex regex, int limit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$split, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.split($this$split, limit);
    }

    static /* synthetic */ java.util.List split$default(java.lang.CharSequence $this$split_u24default, kotlin.text.Regex regex, int limit, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            limit = 0;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$split_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.split($this$split_u24default, limit);
    }

    private static final kotlin.sequences.Sequence<java.lang.String> splitToSequence(java.lang.CharSequence $this$splitToSequence, kotlin.text.Regex regex, int limit) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$splitToSequence, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.splitToSequence($this$splitToSequence, limit);
    }

    static /* synthetic */ kotlin.sequences.Sequence splitToSequence$default(java.lang.CharSequence $this$splitToSequence_u24default, kotlin.text.Regex regex, int limit, int i, java.lang.Object obj) {
        if ((i & 2) != 0) {
            limit = 0;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$splitToSequence_u24default, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regex, "regex");
        return regex.splitToSequence($this$splitToSequence_u24default, limit);
    }

    public static final kotlin.sequences.Sequence<java.lang.String> lineSequence(java.lang.CharSequence $this$lineSequence) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lineSequence, "<this>");
        return kotlin.text.StringsKt.splitToSequence$default($this$lineSequence, new java.lang.String[]{"\r\n", "\n", "\r"}, false, 0, 6, (java.lang.Object) null);
    }

    public static final java.util.List<java.lang.String> lines(java.lang.CharSequence $this$lines) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$lines, "<this>");
        return kotlin.sequences.SequencesKt.toList(kotlin.text.StringsKt.lineSequence($this$lines));
    }

    public static final boolean contentEqualsIgnoreCaseImpl(java.lang.CharSequence $this$contentEqualsIgnoreCaseImpl, java.lang.CharSequence other) {
        if (($this$contentEqualsIgnoreCaseImpl instanceof java.lang.String) && (other instanceof java.lang.String)) {
            return kotlin.text.StringsKt.equals((java.lang.String) $this$contentEqualsIgnoreCaseImpl, (java.lang.String) other, true);
        }
        if ($this$contentEqualsIgnoreCaseImpl == other) {
            return true;
        }
        if ($this$contentEqualsIgnoreCaseImpl == null || other == null || $this$contentEqualsIgnoreCaseImpl.length() != other.length()) {
            return false;
        }
        int length = $this$contentEqualsIgnoreCaseImpl.length();
        for (int i = 0; i < length; i++) {
            if (!kotlin.text.CharsKt.equals($this$contentEqualsIgnoreCaseImpl.charAt(i), other.charAt(i), true)) {
                return false;
            }
        }
        return true;
    }

    public static final boolean contentEqualsImpl(java.lang.CharSequence $this$contentEqualsImpl, java.lang.CharSequence other) {
        if (($this$contentEqualsImpl instanceof java.lang.String) && (other instanceof java.lang.String)) {
            return kotlin.jvm.internal.Intrinsics.areEqual($this$contentEqualsImpl, other);
        }
        if ($this$contentEqualsImpl == other) {
            return true;
        }
        if ($this$contentEqualsImpl == null || other == null || $this$contentEqualsImpl.length() != other.length()) {
            return false;
        }
        int length = $this$contentEqualsImpl.length();
        for (int i = 0; i < length; i++) {
            if ($this$contentEqualsImpl.charAt(i) != other.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static final boolean toBooleanStrict(java.lang.String $this$toBooleanStrict) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBooleanStrict, "<this>");
        if (kotlin.jvm.internal.Intrinsics.areEqual($this$toBooleanStrict, "true")) {
            return true;
        }
        if (kotlin.jvm.internal.Intrinsics.areEqual($this$toBooleanStrict, "false")) {
            return false;
        }
        throw new java.lang.IllegalArgumentException("The string doesn't represent a boolean value: " + $this$toBooleanStrict);
    }

    public static final java.lang.Boolean toBooleanStrictOrNull(java.lang.String $this$toBooleanStrictOrNull) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toBooleanStrictOrNull, "<this>");
        if (kotlin.jvm.internal.Intrinsics.areEqual($this$toBooleanStrictOrNull, "true")) {
            return true;
        }
        return kotlin.jvm.internal.Intrinsics.areEqual($this$toBooleanStrictOrNull, "false") ? false : null;
    }
}
