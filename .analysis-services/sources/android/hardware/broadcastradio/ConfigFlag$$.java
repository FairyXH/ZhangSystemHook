package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public interface ConfigFlag$$ {
    static java.lang.String toString(int _aidl_v) {
        return _aidl_v == 1 ? "FORCE_MONO" : _aidl_v == 2 ? "FORCE_ANALOG" : _aidl_v == 3 ? "FORCE_DIGITAL" : _aidl_v == 4 ? "RDS_AF" : _aidl_v == 5 ? "RDS_REG" : _aidl_v == 6 ? "DAB_DAB_LINKING" : _aidl_v == 7 ? "DAB_FM_LINKING" : _aidl_v == 8 ? "DAB_DAB_SOFT_LINKING" : _aidl_v == 9 ? "DAB_FM_SOFT_LINKING" : _aidl_v == 10 ? "FORCE_ANALOG_FM" : _aidl_v == 11 ? "FORCE_ANALOG_AM" : java.lang.Integer.toString(_aidl_v);
    }

    static java.lang.String arrayToString(java.lang.Object _aidl_v) {
        if (_aidl_v == null) {
            return "null";
        }
        java.lang.Class<?> _aidl_cls = _aidl_v.getClass();
        if (!_aidl_cls.isArray()) {
            throw new java.lang.IllegalArgumentException("not an array: " + _aidl_v);
        }
        java.lang.Class<?> comp = _aidl_cls.getComponentType();
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "[", "]");
        if (comp.isArray()) {
            for (int _aidl_i = 0; _aidl_i < java.lang.reflect.Array.getLength(_aidl_v); _aidl_i++) {
                _aidl_sj.add(arrayToString(java.lang.reflect.Array.get(_aidl_v, _aidl_i)));
            }
        } else {
            if (_aidl_cls != int[].class) {
                throw new java.lang.IllegalArgumentException("wrong type: " + _aidl_cls);
            }
            for (int e : (int[]) _aidl_v) {
                _aidl_sj.add(toString(e));
            }
        }
        return _aidl_sj.toString();
    }
}
