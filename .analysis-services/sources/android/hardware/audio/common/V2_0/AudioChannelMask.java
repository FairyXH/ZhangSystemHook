package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioChannelMask {
    public static final int COUNT_MAX = 30;
    public static final int INDEX_HDR = Integer.MIN_VALUE;
    public static final int INDEX_MASK_1 = -2147483647;
    public static final int INDEX_MASK_2 = -2147483645;
    public static final int INDEX_MASK_3 = -2147483641;
    public static final int INDEX_MASK_4 = -2147483633;
    public static final int INDEX_MASK_5 = -2147483617;
    public static final int INDEX_MASK_6 = -2147483585;
    public static final int INDEX_MASK_7 = -2147483521;
    public static final int INDEX_MASK_8 = -2147483393;
    public static final int INVALID = -1073741824;
    public static final int IN_6 = 252;
    public static final int IN_ALL = 65532;
    public static final int IN_BACK = 32;
    public static final int IN_BACK_PROCESSED = 512;
    public static final int IN_FRONT = 16;
    public static final int IN_FRONT_BACK = 48;
    public static final int IN_FRONT_PROCESSED = 256;
    public static final int IN_LEFT = 4;
    public static final int IN_LEFT_PROCESSED = 64;
    public static final int IN_MONO = 16;
    public static final int IN_PRESSURE = 1024;
    public static final int IN_RIGHT = 8;
    public static final int IN_RIGHT_PROCESSED = 128;
    public static final int IN_STEREO = 12;
    public static final int IN_VOICE_CALL_MONO = 49168;
    public static final int IN_VOICE_DNLINK = 32768;
    public static final int IN_VOICE_DNLINK_MONO = 32784;
    public static final int IN_VOICE_UPLINK = 16384;
    public static final int IN_VOICE_UPLINK_MONO = 16400;
    public static final int IN_X_AXIS = 2048;
    public static final int IN_Y_AXIS = 4096;
    public static final int IN_Z_AXIS = 8192;
    public static final int NONE = 0;
    public static final int OUT_2POINT1 = 11;
    public static final int OUT_5POINT1 = 63;
    public static final int OUT_5POINT1_BACK = 63;
    public static final int OUT_5POINT1_SIDE = 1551;
    public static final int OUT_6POINT1 = 319;
    public static final int OUT_7POINT1 = 1599;
    public static final int OUT_ALL = 262143;
    public static final int OUT_BACK_CENTER = 256;
    public static final int OUT_BACK_LEFT = 16;
    public static final int OUT_BACK_RIGHT = 32;
    public static final int OUT_FRONT_CENTER = 4;
    public static final int OUT_FRONT_LEFT = 1;
    public static final int OUT_FRONT_LEFT_OF_CENTER = 64;
    public static final int OUT_FRONT_RIGHT = 2;
    public static final int OUT_FRONT_RIGHT_OF_CENTER = 128;
    public static final int OUT_LOW_FREQUENCY = 8;
    public static final int OUT_MONO = 1;
    public static final int OUT_PENTA = 55;
    public static final int OUT_QUAD = 51;
    public static final int OUT_QUAD_BACK = 51;
    public static final int OUT_QUAD_SIDE = 1539;
    public static final int OUT_SIDE_LEFT = 512;
    public static final int OUT_SIDE_RIGHT = 1024;
    public static final int OUT_STEREO = 3;
    public static final int OUT_SURROUND = 263;
    public static final int OUT_TOP_BACK_CENTER = 65536;
    public static final int OUT_TOP_BACK_LEFT = 32768;
    public static final int OUT_TOP_BACK_RIGHT = 131072;
    public static final int OUT_TOP_CENTER = 2048;
    public static final int OUT_TOP_FRONT_CENTER = 8192;
    public static final int OUT_TOP_FRONT_LEFT = 4096;
    public static final int OUT_TOP_FRONT_RIGHT = 16384;
    public static final int REPRESENTATION_INDEX = 2;
    public static final int REPRESENTATION_POSITION = 0;

    public static final java.lang.String toString(int o) {
        if (o != 0) {
            if (o == 2) {
                return "REPRESENTATION_INDEX";
            }
            if (o != 0) {
                if (o != -1073741824) {
                    if (o == 1) {
                        return "OUT_FRONT_LEFT";
                    }
                    if (o != 2) {
                        if (o != 4) {
                            if (o != 8) {
                                if (o != 16) {
                                    if (o != 32) {
                                        if (o != 64) {
                                            if (o != 128) {
                                                if (o != 256) {
                                                    if (o != 512) {
                                                        if (o != 1024) {
                                                            if (o != 2048) {
                                                                if (o != 4096) {
                                                                    if (o != 8192) {
                                                                        if (o != 16384) {
                                                                            if (o != 32768) {
                                                                                if (o != 65536) {
                                                                                    if (o == 131072) {
                                                                                        return "OUT_TOP_BACK_RIGHT";
                                                                                    }
                                                                                    if (o != 1) {
                                                                                        if (o != 3) {
                                                                                            if (o != 11) {
                                                                                                if (o == 51) {
                                                                                                    return "OUT_QUAD";
                                                                                                }
                                                                                                if (o != 51) {
                                                                                                    if (o != 1539) {
                                                                                                        if (o != 263) {
                                                                                                            if (o != 55) {
                                                                                                                if (o == 63) {
                                                                                                                    return "OUT_5POINT1";
                                                                                                                }
                                                                                                                if (o != 63) {
                                                                                                                    if (o != 1551) {
                                                                                                                        if (o != 319) {
                                                                                                                            if (o != 1599) {
                                                                                                                                if (o == 262143) {
                                                                                                                                    return "OUT_ALL";
                                                                                                                                }
                                                                                                                                if (o == 4) {
                                                                                                                                    return "IN_LEFT";
                                                                                                                                }
                                                                                                                                if (o == 8) {
                                                                                                                                    return "IN_RIGHT";
                                                                                                                                }
                                                                                                                                if (o == 16) {
                                                                                                                                    return "IN_FRONT";
                                                                                                                                }
                                                                                                                                if (o == 32) {
                                                                                                                                    return "IN_BACK";
                                                                                                                                }
                                                                                                                                if (o == 64) {
                                                                                                                                    return "IN_LEFT_PROCESSED";
                                                                                                                                }
                                                                                                                                if (o == 128) {
                                                                                                                                    return "IN_RIGHT_PROCESSED";
                                                                                                                                }
                                                                                                                                if (o == 256) {
                                                                                                                                    return "IN_FRONT_PROCESSED";
                                                                                                                                }
                                                                                                                                if (o == 512) {
                                                                                                                                    return "IN_BACK_PROCESSED";
                                                                                                                                }
                                                                                                                                if (o == 1024) {
                                                                                                                                    return "IN_PRESSURE";
                                                                                                                                }
                                                                                                                                if (o == 2048) {
                                                                                                                                    return "IN_X_AXIS";
                                                                                                                                }
                                                                                                                                if (o == 4096) {
                                                                                                                                    return "IN_Y_AXIS";
                                                                                                                                }
                                                                                                                                if (o == 8192) {
                                                                                                                                    return "IN_Z_AXIS";
                                                                                                                                }
                                                                                                                                if (o != 16384) {
                                                                                                                                    if (o == 32768) {
                                                                                                                                        return "IN_VOICE_DNLINK";
                                                                                                                                    }
                                                                                                                                    if (o != 16) {
                                                                                                                                        if (o != 12) {
                                                                                                                                            if (o != 48) {
                                                                                                                                                if (o != 252) {
                                                                                                                                                    if (o != 16400) {
                                                                                                                                                        if (o != 32784) {
                                                                                                                                                            if (o != 49168) {
                                                                                                                                                                if (o != 65532) {
                                                                                                                                                                    if (o != 30) {
                                                                                                                                                                        if (o != Integer.MIN_VALUE) {
                                                                                                                                                                            if (o != -2147483647) {
                                                                                                                                                                                if (o != -2147483645) {
                                                                                                                                                                                    if (o != -2147483641) {
                                                                                                                                                                                        if (o != -2147483633) {
                                                                                                                                                                                            if (o != -2147483617) {
                                                                                                                                                                                                if (o != -2147483585) {
                                                                                                                                                                                                    if (o != -2147483521) {
                                                                                                                                                                                                        if (o == -2147483393) {
                                                                                                                                                                                                            return "INDEX_MASK_8";
                                                                                                                                                                                                        }
                                                                                                                                                                                                        return "0x" + java.lang.Integer.toHexString(o);
                                                                                                                                                                                                    }
                                                                                                                                                                                                    return "INDEX_MASK_7";
                                                                                                                                                                                                }
                                                                                                                                                                                                return "INDEX_MASK_6";
                                                                                                                                                                                            }
                                                                                                                                                                                            return "INDEX_MASK_5";
                                                                                                                                                                                        }
                                                                                                                                                                                        return "INDEX_MASK_4";
                                                                                                                                                                                    }
                                                                                                                                                                                    return "INDEX_MASK_3";
                                                                                                                                                                                }
                                                                                                                                                                                return "INDEX_MASK_2";
                                                                                                                                                                            }
                                                                                                                                                                            return "INDEX_MASK_1";
                                                                                                                                                                        }
                                                                                                                                                                        return "INDEX_HDR";
                                                                                                                                                                    }
                                                                                                                                                                    return "COUNT_MAX";
                                                                                                                                                                }
                                                                                                                                                                return "IN_ALL";
                                                                                                                                                            }
                                                                                                                                                            return "IN_VOICE_CALL_MONO";
                                                                                                                                                        }
                                                                                                                                                        return "IN_VOICE_DNLINK_MONO";
                                                                                                                                                    }
                                                                                                                                                    return "IN_VOICE_UPLINK_MONO";
                                                                                                                                                }
                                                                                                                                                return "IN_6";
                                                                                                                                            }
                                                                                                                                            return "IN_FRONT_BACK";
                                                                                                                                        }
                                                                                                                                        return "IN_STEREO";
                                                                                                                                    }
                                                                                                                                    return "IN_MONO";
                                                                                                                                }
                                                                                                                                return "IN_VOICE_UPLINK";
                                                                                                                            }
                                                                                                                            return "OUT_7POINT1";
                                                                                                                        }
                                                                                                                        return "OUT_6POINT1";
                                                                                                                    }
                                                                                                                    return "OUT_5POINT1_SIDE";
                                                                                                                }
                                                                                                                return "OUT_5POINT1_BACK";
                                                                                                            }
                                                                                                            return "OUT_PENTA";
                                                                                                        }
                                                                                                        return "OUT_SURROUND";
                                                                                                    }
                                                                                                    return "OUT_QUAD_SIDE";
                                                                                                }
                                                                                                return "OUT_QUAD_BACK";
                                                                                            }
                                                                                            return "OUT_2POINT1";
                                                                                        }
                                                                                        return "OUT_STEREO";
                                                                                    }
                                                                                    return "OUT_MONO";
                                                                                }
                                                                                return "OUT_TOP_BACK_CENTER";
                                                                            }
                                                                            return "OUT_TOP_BACK_LEFT";
                                                                        }
                                                                        return "OUT_TOP_FRONT_RIGHT";
                                                                    }
                                                                    return "OUT_TOP_FRONT_CENTER";
                                                                }
                                                                return "OUT_TOP_FRONT_LEFT";
                                                            }
                                                            return "OUT_TOP_CENTER";
                                                        }
                                                        return "OUT_SIDE_RIGHT";
                                                    }
                                                    return "OUT_SIDE_LEFT";
                                                }
                                                return "OUT_BACK_CENTER";
                                            }
                                            return "OUT_FRONT_RIGHT_OF_CENTER";
                                        }
                                        return "OUT_FRONT_LEFT_OF_CENTER";
                                    }
                                    return "OUT_BACK_RIGHT";
                                }
                                return "OUT_BACK_LEFT";
                            }
                            return "OUT_LOW_FREQUENCY";
                        }
                        return "OUT_FRONT_CENTER";
                    }
                    return "OUT_FRONT_RIGHT";
                }
                return "INVALID";
            }
            return "NONE";
        }
        return "REPRESENTATION_POSITION";
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        list.add("REPRESENTATION_POSITION");
        if ((o & 2) == 2) {
            list.add("REPRESENTATION_INDEX");
            flipped = 0 | 2;
        }
        list.add("NONE");
        if ((o & (-1073741824)) == -1073741824) {
            list.add("INVALID");
            flipped |= -1073741824;
        }
        if ((o & 1) == 1) {
            list.add("OUT_FRONT_LEFT");
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("OUT_FRONT_RIGHT");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("OUT_FRONT_CENTER");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("OUT_LOW_FREQUENCY");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("OUT_BACK_LEFT");
            flipped |= 16;
        }
        if ((o & 32) == 32) {
            list.add("OUT_BACK_RIGHT");
            flipped |= 32;
        }
        if ((o & 64) == 64) {
            list.add("OUT_FRONT_LEFT_OF_CENTER");
            flipped |= 64;
        }
        if ((o & 128) == 128) {
            list.add("OUT_FRONT_RIGHT_OF_CENTER");
            flipped |= 128;
        }
        if ((o & 256) == 256) {
            list.add("OUT_BACK_CENTER");
            flipped |= 256;
        }
        if ((o & 512) == 512) {
            list.add("OUT_SIDE_LEFT");
            flipped |= 512;
        }
        if ((o & 1024) == 1024) {
            list.add("OUT_SIDE_RIGHT");
            flipped |= 1024;
        }
        if ((o & 2048) == 2048) {
            list.add("OUT_TOP_CENTER");
            flipped |= 2048;
        }
        if ((o & 4096) == 4096) {
            list.add("OUT_TOP_FRONT_LEFT");
            flipped |= 4096;
        }
        if ((o & 8192) == 8192) {
            list.add("OUT_TOP_FRONT_CENTER");
            flipped |= 8192;
        }
        if ((o & 16384) == 16384) {
            list.add("OUT_TOP_FRONT_RIGHT");
            flipped |= 16384;
        }
        if ((o & 32768) == 32768) {
            list.add("OUT_TOP_BACK_LEFT");
            flipped |= 32768;
        }
        if ((o & 65536) == 65536) {
            list.add("OUT_TOP_BACK_CENTER");
            flipped |= 65536;
        }
        if ((o & 131072) == 131072) {
            list.add("OUT_TOP_BACK_RIGHT");
            flipped |= 131072;
        }
        if ((o & 1) == 1) {
            list.add("OUT_MONO");
            flipped |= 1;
        }
        if ((o & 3) == 3) {
            list.add("OUT_STEREO");
            flipped |= 3;
        }
        if ((o & 11) == 11) {
            list.add("OUT_2POINT1");
            flipped |= 11;
        }
        if ((o & 51) == 51) {
            list.add("OUT_QUAD");
            flipped |= 51;
        }
        if ((o & 51) == 51) {
            list.add("OUT_QUAD_BACK");
            flipped |= 51;
        }
        if ((o & 1539) == 1539) {
            list.add("OUT_QUAD_SIDE");
            flipped |= 1539;
        }
        if ((o & 263) == 263) {
            list.add("OUT_SURROUND");
            flipped |= 263;
        }
        if ((o & 55) == 55) {
            list.add("OUT_PENTA");
            flipped |= 55;
        }
        if ((o & 63) == 63) {
            list.add("OUT_5POINT1");
            flipped |= 63;
        }
        if ((o & 63) == 63) {
            list.add("OUT_5POINT1_BACK");
            flipped |= 63;
        }
        if ((o & OUT_5POINT1_SIDE) == 1551) {
            list.add("OUT_5POINT1_SIDE");
            flipped |= OUT_5POINT1_SIDE;
        }
        if ((o & 319) == 319) {
            list.add("OUT_6POINT1");
            flipped |= 319;
        }
        if ((o & OUT_7POINT1) == 1599) {
            list.add("OUT_7POINT1");
            flipped |= OUT_7POINT1;
        }
        if ((262143 & o) == 262143) {
            list.add("OUT_ALL");
            flipped |= OUT_ALL;
        }
        if ((o & 4) == 4) {
            list.add("IN_LEFT");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("IN_RIGHT");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("IN_FRONT");
            flipped |= 16;
        }
        if ((o & 32) == 32) {
            list.add("IN_BACK");
            flipped |= 32;
        }
        if ((o & 64) == 64) {
            list.add("IN_LEFT_PROCESSED");
            flipped |= 64;
        }
        if ((o & 128) == 128) {
            list.add("IN_RIGHT_PROCESSED");
            flipped |= 128;
        }
        if ((o & 256) == 256) {
            list.add("IN_FRONT_PROCESSED");
            flipped |= 256;
        }
        if ((o & 512) == 512) {
            list.add("IN_BACK_PROCESSED");
            flipped |= 512;
        }
        if ((o & 1024) == 1024) {
            list.add("IN_PRESSURE");
            flipped |= 1024;
        }
        if ((o & 2048) == 2048) {
            list.add("IN_X_AXIS");
            flipped |= 2048;
        }
        if ((o & 4096) == 4096) {
            list.add("IN_Y_AXIS");
            flipped |= 4096;
        }
        if ((o & 8192) == 8192) {
            list.add("IN_Z_AXIS");
            flipped |= 8192;
        }
        if ((o & 16384) == 16384) {
            list.add("IN_VOICE_UPLINK");
            flipped |= 16384;
        }
        if ((o & 32768) == 32768) {
            list.add("IN_VOICE_DNLINK");
            flipped |= 32768;
        }
        if ((o & 16) == 16) {
            list.add("IN_MONO");
            flipped |= 16;
        }
        if ((o & 12) == 12) {
            list.add("IN_STEREO");
            flipped |= 12;
        }
        if ((o & 48) == 48) {
            list.add("IN_FRONT_BACK");
            flipped |= 48;
        }
        if ((o & IN_6) == 252) {
            list.add("IN_6");
            flipped |= IN_6;
        }
        if ((o & IN_VOICE_UPLINK_MONO) == 16400) {
            list.add("IN_VOICE_UPLINK_MONO");
            flipped |= IN_VOICE_UPLINK_MONO;
        }
        if ((32784 & o) == 32784) {
            list.add("IN_VOICE_DNLINK_MONO");
            flipped |= IN_VOICE_DNLINK_MONO;
        }
        if ((49168 & o) == 49168) {
            list.add("IN_VOICE_CALL_MONO");
            flipped |= IN_VOICE_CALL_MONO;
        }
        if ((65532 & o) == 65532) {
            list.add("IN_ALL");
            flipped |= IN_ALL;
        }
        if ((o & 30) == 30) {
            list.add("COUNT_MAX");
            flipped |= 30;
        }
        if ((Integer.MIN_VALUE & o) == Integer.MIN_VALUE) {
            list.add("INDEX_HDR");
            flipped |= Integer.MIN_VALUE;
        }
        if (((-2147483647) & o) == -2147483647) {
            list.add("INDEX_MASK_1");
            flipped |= -2147483647;
        }
        if (((-2147483645) & o) == -2147483645) {
            list.add("INDEX_MASK_2");
            flipped |= -2147483645;
        }
        if (((-2147483641) & o) == -2147483641) {
            list.add("INDEX_MASK_3");
            flipped |= INDEX_MASK_3;
        }
        if (((-2147483633) & o) == -2147483633) {
            list.add("INDEX_MASK_4");
            flipped |= INDEX_MASK_4;
        }
        if (((-2147483617) & o) == -2147483617) {
            list.add("INDEX_MASK_5");
            flipped |= INDEX_MASK_5;
        }
        if (((-2147483585) & o) == -2147483585) {
            list.add("INDEX_MASK_6");
            flipped |= INDEX_MASK_6;
        }
        if (((-2147483521) & o) == -2147483521) {
            list.add("INDEX_MASK_7");
            flipped |= INDEX_MASK_7;
        }
        if (((-2147483393) & o) == -2147483393) {
            list.add("INDEX_MASK_8");
            flipped |= INDEX_MASK_8;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
