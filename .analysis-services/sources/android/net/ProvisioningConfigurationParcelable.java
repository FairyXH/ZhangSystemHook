package android.net;

/* JADX INFO: loaded from: classes.dex */
public class ProvisioningConfigurationParcelable implements android.os.Parcelable {
    public static final android.os.Parcelable.Creator<android.net.ProvisioningConfigurationParcelable> CREATOR = new android.os.Parcelable.Creator<android.net.ProvisioningConfigurationParcelable>() { // from class: android.net.ProvisioningConfigurationParcelable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.ProvisioningConfigurationParcelable createFromParcel(android.os.Parcel _aidl_source) {
            android.net.ProvisioningConfigurationParcelable _aidl_out = new android.net.ProvisioningConfigurationParcelable();
            _aidl_out.readFromParcel(_aidl_source);
            return _aidl_out;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public android.net.ProvisioningConfigurationParcelable[] newArray(int _aidl_size) {
            return new android.net.ProvisioningConfigurationParcelable[_aidl_size];
        }
    };
    public android.net.apf.ApfCapabilities apfCapabilities;
    public java.lang.String displayName;
    public android.net.InitialConfigurationParcelable initialConfig;
    public android.net.Layer2InformationParcelable layer2Info;
    public android.net.Network network;
    public java.util.List<android.net.networkstack.aidl.dhcp.DhcpOption> options;
    public android.net.ScanResultInfoParcelable scanResultInfo;
    public android.net.StaticIpConfiguration staticIpConfig;

    @java.lang.Deprecated
    public boolean enableIPv4 = false;

    @java.lang.Deprecated
    public boolean enableIPv6 = false;
    public boolean usingMultinetworkPolicyTracker = false;
    public boolean usingIpReachabilityMonitor = false;
    public int requestedPreDhcpActionMs = 0;
    public int provisioningTimeoutMs = 0;
    public int ipv6AddrGenMode = 0;
    public boolean enablePreconnection = false;
    public int ipv4ProvisioningMode = 0;
    public int ipv6ProvisioningMode = 0;
    public boolean uniqueEui64AddressesOnly = false;
    public int creatorUid = 0;
    public int hostnameSetting = 0;

    @Override // android.os.Parcelable
    public final void writeToParcel(android.os.Parcel _aidl_parcel, int _aidl_flag) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.writeInt(0);
        _aidl_parcel.writeBoolean(this.enableIPv4);
        _aidl_parcel.writeBoolean(this.enableIPv6);
        _aidl_parcel.writeBoolean(this.usingMultinetworkPolicyTracker);
        _aidl_parcel.writeBoolean(this.usingIpReachabilityMonitor);
        _aidl_parcel.writeInt(this.requestedPreDhcpActionMs);
        _aidl_parcel.writeTypedObject(this.initialConfig, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.staticIpConfig, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.apfCapabilities, _aidl_flag);
        _aidl_parcel.writeInt(this.provisioningTimeoutMs);
        _aidl_parcel.writeInt(this.ipv6AddrGenMode);
        _aidl_parcel.writeTypedObject(this.network, _aidl_flag);
        _aidl_parcel.writeString(this.displayName);
        _aidl_parcel.writeBoolean(this.enablePreconnection);
        _aidl_parcel.writeTypedObject(this.scanResultInfo, _aidl_flag);
        _aidl_parcel.writeTypedObject(this.layer2Info, _aidl_flag);
        android.net.ProvisioningConfigurationParcelable._Parcel.writeTypedList(_aidl_parcel, this.options, _aidl_flag);
        _aidl_parcel.writeInt(this.ipv4ProvisioningMode);
        _aidl_parcel.writeInt(this.ipv6ProvisioningMode);
        _aidl_parcel.writeBoolean(this.uniqueEui64AddressesOnly);
        _aidl_parcel.writeInt(this.creatorUid);
        _aidl_parcel.writeInt(this.hostnameSetting);
        int _aidl_end_pos = _aidl_parcel.dataPosition();
        _aidl_parcel.setDataPosition(_aidl_start_pos);
        _aidl_parcel.writeInt(_aidl_end_pos - _aidl_start_pos);
        _aidl_parcel.setDataPosition(_aidl_end_pos);
    }

    public final void readFromParcel(android.os.Parcel _aidl_parcel) {
        int _aidl_start_pos = _aidl_parcel.dataPosition();
        int _aidl_parcelable_size = _aidl_parcel.readInt();
        try {
            if (_aidl_parcelable_size < 4) {
                throw new android.os.BadParcelableException("Parcelable too small");
            }
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.enableIPv4 = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.enableIPv6 = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.usingMultinetworkPolicyTracker = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.usingIpReachabilityMonitor = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.requestedPreDhcpActionMs = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.initialConfig = (android.net.InitialConfigurationParcelable) _aidl_parcel.readTypedObject(android.net.InitialConfigurationParcelable.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.staticIpConfig = (android.net.StaticIpConfiguration) _aidl_parcel.readTypedObject(android.net.StaticIpConfiguration.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.apfCapabilities = (android.net.apf.ApfCapabilities) _aidl_parcel.readTypedObject(android.net.apf.ApfCapabilities.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.provisioningTimeoutMs = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.ipv6AddrGenMode = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.network = (android.net.Network) _aidl_parcel.readTypedObject(android.net.Network.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.displayName = _aidl_parcel.readString();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.enablePreconnection = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.scanResultInfo = (android.net.ScanResultInfoParcelable) _aidl_parcel.readTypedObject(android.net.ScanResultInfoParcelable.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.layer2Info = (android.net.Layer2InformationParcelable) _aidl_parcel.readTypedObject(android.net.Layer2InformationParcelable.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.options = _aidl_parcel.createTypedArrayList(android.net.networkstack.aidl.dhcp.DhcpOption.CREATOR);
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.ipv4ProvisioningMode = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.ipv6ProvisioningMode = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.uniqueEui64AddressesOnly = _aidl_parcel.readBoolean();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
                return;
            }
            this.creatorUid = _aidl_parcel.readInt();
            if (_aidl_parcel.dataPosition() - _aidl_start_pos >= _aidl_parcelable_size) {
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            } else {
                this.hostnameSetting = _aidl_parcel.readInt();
                if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                    throw new android.os.BadParcelableException("Overflow in the size of parcelable");
                }
                _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            }
        } catch (java.lang.Throwable th) {
            if (_aidl_start_pos > Integer.MAX_VALUE - _aidl_parcelable_size) {
                throw new android.os.BadParcelableException("Overflow in the size of parcelable");
            }
            _aidl_parcel.setDataPosition(_aidl_start_pos + _aidl_parcelable_size);
            throw th;
        }
    }

    public java.lang.String toString() {
        java.util.StringJoiner _aidl_sj = new java.util.StringJoiner(", ", "{", "}");
        _aidl_sj.add("enableIPv4: " + this.enableIPv4);
        _aidl_sj.add("enableIPv6: " + this.enableIPv6);
        _aidl_sj.add("usingMultinetworkPolicyTracker: " + this.usingMultinetworkPolicyTracker);
        _aidl_sj.add("usingIpReachabilityMonitor: " + this.usingIpReachabilityMonitor);
        _aidl_sj.add("requestedPreDhcpActionMs: " + this.requestedPreDhcpActionMs);
        _aidl_sj.add("initialConfig: " + java.util.Objects.toString(this.initialConfig));
        _aidl_sj.add("staticIpConfig: " + java.util.Objects.toString(this.staticIpConfig));
        _aidl_sj.add("apfCapabilities: " + java.util.Objects.toString(this.apfCapabilities));
        _aidl_sj.add("provisioningTimeoutMs: " + this.provisioningTimeoutMs);
        _aidl_sj.add("ipv6AddrGenMode: " + this.ipv6AddrGenMode);
        _aidl_sj.add("network: " + java.util.Objects.toString(this.network));
        _aidl_sj.add("displayName: " + java.util.Objects.toString(this.displayName));
        _aidl_sj.add("enablePreconnection: " + this.enablePreconnection);
        _aidl_sj.add("scanResultInfo: " + java.util.Objects.toString(this.scanResultInfo));
        _aidl_sj.add("layer2Info: " + java.util.Objects.toString(this.layer2Info));
        _aidl_sj.add("options: " + java.util.Objects.toString(this.options));
        _aidl_sj.add("ipv4ProvisioningMode: " + this.ipv4ProvisioningMode);
        _aidl_sj.add("ipv6ProvisioningMode: " + this.ipv6ProvisioningMode);
        _aidl_sj.add("uniqueEui64AddressesOnly: " + this.uniqueEui64AddressesOnly);
        _aidl_sj.add("creatorUid: " + this.creatorUid);
        _aidl_sj.add("hostnameSetting: " + this.hostnameSetting);
        return "ProvisioningConfigurationParcelable" + _aidl_sj.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int _mask = 0 | describeContents(this.initialConfig);
        return _mask | describeContents(this.staticIpConfig) | describeContents(this.apfCapabilities) | describeContents(this.network) | describeContents(this.scanResultInfo) | describeContents(this.layer2Info) | describeContents(this.options);
    }

    private int describeContents(java.lang.Object _v) {
        if (_v == null) {
            return 0;
        }
        if (_v instanceof java.util.Collection) {
            int _mask = 0;
            for (java.lang.Object o : (java.util.Collection) _v) {
                _mask |= describeContents(o);
            }
            return _mask;
        }
        if (!(_v instanceof android.os.Parcelable)) {
            return 0;
        }
        return ((android.os.Parcelable) _v).describeContents();
    }

    static class _Parcel {
        _Parcel() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static <T extends android.os.Parcelable> void writeTypedList(android.os.Parcel parcel, java.util.List<T> value, int parcelableFlags) {
            if (value == null) {
                parcel.writeInt(-1);
                return;
            }
            int N = value.size();
            parcel.writeInt(N);
            for (int i = 0; i < N; i++) {
                parcel.writeTypedObject(value.get(i), parcelableFlags);
            }
        }
    }
}
