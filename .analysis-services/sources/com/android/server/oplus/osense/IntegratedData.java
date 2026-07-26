package com.android.server.oplus.osense;

/* JADX INFO: loaded from: classes2.dex */
public class IntegratedData implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<com.android.server.oplus.osense.IntegratedData> CREATOR = new android.os.Parcelable.Creator<com.android.server.oplus.osense.IntegratedData>() { // from class: com.android.server.oplus.osense.IntegratedData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public com.android.server.oplus.osense.IntegratedData createFromParcel(android.os.Parcel in) {
            return new com.android.server.oplus.osense.IntegratedData(in.readInt(), in.readLong(), in.readBundle());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public com.android.server.oplus.osense.IntegratedData[] newArray(int size) {
            return new com.android.server.oplus.osense.IntegratedData[size];
        }
    };
    private java.lang.String mData;
    private android.os.Bundle mInfo;
    private int mResId;
    private long mTime;

    public IntegratedData() {
        this(0, 0L, null);
    }

    public IntegratedData(int resid, long time, android.os.Bundle info) {
        this.mResId = resid;
        this.mTime = time;
        this.mInfo = info;
    }

    public int getResId() {
        return this.mResId;
    }

    public void setResId(int resId) {
        this.mResId = resId;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public android.os.Bundle getInfo() {
        return this.mInfo;
    }

    public void setInfo(android.os.Bundle info) {
        this.mInfo = info;
    }

    public java.lang.String toString() {
        return "IntegratedData{mResId=" + this.mResId + ", mTime=" + this.mTime + ", mInfo=" + this.mInfo + '}';
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(this.mResId);
        dest.writeLong(this.mTime);
        dest.writeBundle(this.mInfo);
    }

    private IntegratedData(android.os.Parcel in) {
        this.mResId = in.readInt();
        this.mTime = in.readLong();
        this.mInfo = in.readBundle();
    }
}
