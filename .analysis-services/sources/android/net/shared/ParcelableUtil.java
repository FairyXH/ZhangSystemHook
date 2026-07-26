package android.net.shared;

/* JADX INFO: loaded from: classes.dex */
public final class ParcelableUtil {
    public static <ParcelableType, BaseType> ParcelableType[] toParcelableArray(java.util.Collection<BaseType> collection, java.util.function.Function<BaseType, ParcelableType> function, java.lang.Class<ParcelableType> cls) {
        ParcelableType[] parcelabletypeArr = (ParcelableType[]) ((java.lang.Object[]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) cls, collection.size()));
        int i = 0;
        java.util.Iterator<BaseType> it = collection.iterator();
        while (it.hasNext()) {
            parcelabletypeArr[i] = function.apply(it.next());
            i++;
        }
        return parcelabletypeArr;
    }

    public static <ParcelableType, BaseType> java.util.ArrayList<BaseType> fromParcelableArray(ParcelableType[] parceled, java.util.function.Function<ParcelableType, BaseType> conv) {
        java.util.ArrayList<BaseType> out = new java.util.ArrayList<>(parceled.length);
        for (ParcelableType t : parceled) {
            out.add(conv.apply(t));
        }
        return out;
    }
}
