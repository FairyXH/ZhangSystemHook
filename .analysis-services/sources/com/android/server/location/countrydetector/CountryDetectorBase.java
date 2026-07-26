package com.android.server.location.countrydetector;

/* JADX INFO: loaded from: classes2.dex */
public abstract class CountryDetectorBase {
    private static final java.lang.String ATTRIBUTION_TAG = "CountryDetector";
    protected final android.content.Context mContext;
    protected android.location.Country mDetectedCountry;
    protected final android.os.Handler mHandler = new android.os.Handler();
    protected android.location.CountryListener mListener;

    public abstract android.location.Country detectCountry();

    public abstract void stop();

    public CountryDetectorBase(android.content.Context context) {
        this.mContext = context.createAttributionContext(ATTRIBUTION_TAG);
    }

    public void setCountryListener(android.location.CountryListener listener) {
        this.mListener = listener;
    }

    protected void notifyListener(android.location.Country country) {
        if (this.mListener != null) {
            this.mListener.onCountryDetected(country);
        }
    }
}
