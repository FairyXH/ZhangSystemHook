package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public interface Result$$ {
    static java.lang.String toString(int _aidl_v) {
        return _aidl_v == 0 ? "OK" : _aidl_v == 1 ? "INTERNAL_ERROR" : _aidl_v == 2 ? "INVALID_ARGUMENTS" : _aidl_v == 3 ? "INVALID_STATE" : _aidl_v == 4 ? "NOT_SUPPORTED" : _aidl_v == 5 ? "TIMEOUT" : _aidl_v == 6 ? "CANCELED" : _aidl_v == 7 ? "UNKNOWN_ERROR" : java.lang.Integer.toString(_aidl_v);
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
