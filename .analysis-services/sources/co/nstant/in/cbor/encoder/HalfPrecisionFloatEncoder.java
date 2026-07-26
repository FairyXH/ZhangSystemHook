package co.nstant.in.cbor.encoder;

/* JADX INFO: loaded from: classes.dex */
public class HalfPrecisionFloatEncoder extends co.nstant.in.cbor.encoder.AbstractEncoder<co.nstant.in.cbor.model.HalfPrecisionFloat> {
    public HalfPrecisionFloatEncoder(co.nstant.in.cbor.CborEncoder encoder, java.io.OutputStream outputStream) {
        super(encoder, outputStream);
    }

    @Override // co.nstant.in.cbor.encoder.AbstractEncoder
    public void encode(co.nstant.in.cbor.model.HalfPrecisionFloat dataItem) throws co.nstant.in.cbor.CborException {
        write(249);
        int bits = fromFloat(dataItem.getValue());
        write((bits >> 8) & 255);
        write((bits >> 0) & 255);
    }

    public static int fromFloat(float fval) {
        int fbits = java.lang.Float.floatToIntBits(fval);
        int sign = (fbits >>> 16) & 32768;
        int val = (fbits + 4096) & Integer.MAX_VALUE;
        if (val >= 1199570944) {
            if ((Integer.MAX_VALUE & fbits) >= 1199570944) {
                if (val >= 2139095040) {
                    return sign | 31744 | ((8388607 & fbits) >>> 13);
                }
                return sign | 31744;
            }
            return sign | 31743;
        }
        if (val >= 947912704) {
            return ((val - 939524096) >>> 13) | sign;
        }
        if (val < 855638016) {
            return sign;
        }
        int val2 = (Integer.MAX_VALUE & fbits) >>> 23;
        return ((((fbits & 8388607) | 8388608) + (8388608 >>> (val2 + com.android.server.location.gnss.hal.GnssNative.GeofenceCallbacks.GEOFENCE_STATUS_ERROR_ID_UNKNOWN))) >>> (126 - val2)) | sign;
    }
}
