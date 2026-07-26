package android.net.ipmemorystore;

/* JADX INFO: loaded from: classes.dex */
public class SameL3NetworkResponse {
    public static final int NETWORK_DIFFERENT = 2;
    public static final int NETWORK_NEVER_CONNECTED = 3;
    public static final int NETWORK_SAME = 1;
    public final float confidence;
    public final java.lang.String l2Key1;
    public final java.lang.String l2Key2;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface NetworkSameness {
    }

    public final int getNetworkSameness() {
        if (this.confidence > 1.0d || this.confidence < 0.0d) {
            return 3;
        }
        return ((double) this.confidence) > 0.5d ? 1 : 2;
    }

    public SameL3NetworkResponse(java.lang.String l2Key1, java.lang.String l2Key2, float confidence) {
        this.l2Key1 = l2Key1;
        this.l2Key2 = l2Key2;
        this.confidence = confidence;
    }

    public SameL3NetworkResponse(android.net.ipmemorystore.SameL3NetworkResponseParcelable parceled) {
        this(parceled.l2Key1, parceled.l2Key2, parceled.confidence);
    }

    public android.net.ipmemorystore.SameL3NetworkResponseParcelable toParcelable() {
        android.net.ipmemorystore.SameL3NetworkResponseParcelable parcelable = new android.net.ipmemorystore.SameL3NetworkResponseParcelable();
        parcelable.l2Key1 = this.l2Key1;
        parcelable.l2Key2 = this.l2Key2;
        parcelable.confidence = this.confidence;
        return parcelable;
    }

    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.net.ipmemorystore.SameL3NetworkResponse)) {
            return false;
        }
        android.net.ipmemorystore.SameL3NetworkResponse other = (android.net.ipmemorystore.SameL3NetworkResponse) o;
        return this.l2Key1.equals(other.l2Key1) && this.l2Key2.equals(other.l2Key2) && this.confidence == other.confidence;
    }

    public int hashCode() {
        return java.util.Objects.hash(this.l2Key1, this.l2Key2, java.lang.Float.valueOf(this.confidence));
    }

    public java.lang.String toString() {
        switch (getNetworkSameness()) {
            case 1:
                return "\"" + this.l2Key1 + "\" same L3 network as \"" + this.l2Key2 + "\"";
            case 2:
                return "\"" + this.l2Key1 + "\" different L3 network from \"" + this.l2Key2 + "\"";
            case 3:
                return "\"" + this.l2Key1 + "\" can't be tested against \"" + this.l2Key2 + "\"";
            default:
                return "Buggy sameness value ? \"" + this.l2Key1 + "\", \"" + this.l2Key2 + "\"";
        }
    }
}
