package android.hardware.broadcastradio;

/* JADX INFO: loaded from: classes.dex */
public interface AnnouncementType$$ {
    static java.lang.String toString(byte _aidl_v) {
        return _aidl_v == 0 ? "INVALID" : _aidl_v == 1 ? "EMERGENCY" : _aidl_v == 2 ? "WARNING" : _aidl_v == 3 ? "TRAFFIC" : _aidl_v == 4 ? "WEATHER" : _aidl_v == 5 ? "NEWS" : _aidl_v == 6 ? "EVENT" : _aidl_v == 7 ? "SPORT" : _aidl_v == 8 ? "MISC" : java.lang.Byte.toString(_aidl_v);
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
            if (_aidl_cls != byte[].class) {
                throw new java.lang.IllegalArgumentException("wrong type: " + _aidl_cls);
            }
            for (byte e : (byte[]) _aidl_v) {
                _aidl_sj.add(toString(e));
            }
        }
        return _aidl_sj.toString();
    }
}
