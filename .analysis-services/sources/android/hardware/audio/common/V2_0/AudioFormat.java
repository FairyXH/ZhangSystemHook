package android.hardware.audio.common.V2_0;

/* JADX INFO: loaded from: classes.dex */
public final class AudioFormat {
    public static final int AAC = 67108864;
    public static final int AAC_ADIF = 335544320;
    public static final int AAC_ADTS = 503316480;
    public static final int AAC_ADTS_ELD = 503316992;
    public static final int AAC_ADTS_ERLC = 503316544;
    public static final int AAC_ADTS_HE_V1 = 503316496;
    public static final int AAC_ADTS_HE_V2 = 503316736;
    public static final int AAC_ADTS_LC = 503316482;
    public static final int AAC_ADTS_LD = 503316608;
    public static final int AAC_ADTS_LTP = 503316488;
    public static final int AAC_ADTS_MAIN = 503316481;
    public static final int AAC_ADTS_SCALABLE = 503316512;
    public static final int AAC_ADTS_SSR = 503316484;
    public static final int AAC_ELD = 67109376;
    public static final int AAC_ERLC = 67108928;
    public static final int AAC_HE_V1 = 67108880;
    public static final int AAC_HE_V2 = 67109120;
    public static final int AAC_LC = 67108866;
    public static final int AAC_LD = 67108992;
    public static final int AAC_LTP = 67108872;
    public static final int AAC_MAIN = 67108865;
    public static final int AAC_SCALABLE = 67108896;
    public static final int AAC_SSR = 67108868;
    public static final int AAC_SUB_ELD = 512;
    public static final int AAC_SUB_ERLC = 64;
    public static final int AAC_SUB_HE_V1 = 16;
    public static final int AAC_SUB_HE_V2 = 256;
    public static final int AAC_SUB_LC = 2;
    public static final int AAC_SUB_LD = 128;
    public static final int AAC_SUB_LTP = 8;
    public static final int AAC_SUB_MAIN = 1;
    public static final int AAC_SUB_SCALABLE = 32;
    public static final int AAC_SUB_SSR = 4;
    public static final int AC3 = 150994944;
    public static final int AC4 = 570425344;
    public static final int ALAC = 469762048;
    public static final int AMR_NB = 33554432;
    public static final int AMR_SUB_NONE = 0;
    public static final int AMR_WB = 50331648;
    public static final int AMR_WB_PLUS = 385875968;
    public static final int APE = 486539264;
    public static final int APTX = 536870912;
    public static final int APTX_HD = 553648128;
    public static final int DEFAULT = 0;
    public static final int DOLBY_TRUEHD = 234881024;
    public static final int DSD = 436207616;
    public static final int DTS = 184549376;
    public static final int DTS_HD = 201326592;
    public static final int EVRC = 268435456;
    public static final int EVRCB = 285212672;
    public static final int EVRCNW = 318767104;
    public static final int EVRCWB = 301989888;
    public static final int E_AC3 = 167772160;
    public static final int FLAC = 452984832;
    public static final int HE_AAC_V1 = 83886080;
    public static final int HE_AAC_V2 = 100663296;
    public static final int IEC61937 = 218103808;
    public static final int INVALID = -1;
    public static final int LDAC = 587202560;
    public static final int MAIN_MASK = -16777216;
    public static final int MP2 = 402653184;
    public static final int MP3 = 16777216;
    public static final int MP3_SUB_NONE = 0;
    public static final int OPUS = 134217728;
    public static final int PCM = 0;
    public static final int PCM_16_BIT = 1;
    public static final int PCM_24_BIT_PACKED = 6;
    public static final int PCM_32_BIT = 3;
    public static final int PCM_8_24_BIT = 4;
    public static final int PCM_8_BIT = 2;
    public static final int PCM_FLOAT = 5;
    public static final int PCM_SUB_16_BIT = 1;
    public static final int PCM_SUB_24_BIT_PACKED = 6;
    public static final int PCM_SUB_32_BIT = 3;
    public static final int PCM_SUB_8_24_BIT = 4;
    public static final int PCM_SUB_8_BIT = 2;
    public static final int PCM_SUB_FLOAT = 5;
    public static final int QCELP = 419430400;
    public static final int SBC = 520093696;
    public static final int SUB_MASK = 16777215;
    public static final int VORBIS = 117440512;
    public static final int VORBIS_SUB_NONE = 0;
    public static final int WMA = 352321536;
    public static final int WMA_PRO = 369098752;

    public static final java.lang.String toString(int o) {
        if (o == -1) {
            return "INVALID";
        }
        if (o == 0) {
            return "DEFAULT";
        }
        if (o == 0) {
            return "PCM";
        }
        if (o == 16777216) {
            return "MP3";
        }
        if (o == 33554432) {
            return "AMR_NB";
        }
        if (o == 50331648) {
            return "AMR_WB";
        }
        if (o == 67108864) {
            return "AAC";
        }
        if (o == 83886080) {
            return "HE_AAC_V1";
        }
        if (o == 100663296) {
            return "HE_AAC_V2";
        }
        if (o == 117440512) {
            return "VORBIS";
        }
        if (o == 134217728) {
            return "OPUS";
        }
        if (o == 150994944) {
            return "AC3";
        }
        if (o == 167772160) {
            return "E_AC3";
        }
        if (o == 184549376) {
            return "DTS";
        }
        if (o == 201326592) {
            return "DTS_HD";
        }
        if (o == 218103808) {
            return "IEC61937";
        }
        if (o == 234881024) {
            return "DOLBY_TRUEHD";
        }
        if (o == 268435456) {
            return "EVRC";
        }
        if (o == 285212672) {
            return "EVRCB";
        }
        if (o == 301989888) {
            return "EVRCWB";
        }
        if (o == 318767104) {
            return "EVRCNW";
        }
        if (o == 335544320) {
            return "AAC_ADIF";
        }
        if (o == 352321536) {
            return "WMA";
        }
        if (o == 369098752) {
            return "WMA_PRO";
        }
        if (o == 385875968) {
            return "AMR_WB_PLUS";
        }
        if (o == 402653184) {
            return "MP2";
        }
        if (o == 419430400) {
            return "QCELP";
        }
        if (o == 436207616) {
            return "DSD";
        }
        if (o == 452984832) {
            return "FLAC";
        }
        if (o == 469762048) {
            return "ALAC";
        }
        if (o == 486539264) {
            return "APE";
        }
        if (o == 503316480) {
            return "AAC_ADTS";
        }
        if (o == 520093696) {
            return "SBC";
        }
        if (o == 536870912) {
            return "APTX";
        }
        if (o == 553648128) {
            return "APTX_HD";
        }
        if (o == 570425344) {
            return "AC4";
        }
        if (o == 587202560) {
            return "LDAC";
        }
        if (o == -16777216) {
            return "MAIN_MASK";
        }
        if (o == 16777215) {
            return "SUB_MASK";
        }
        if (o == 1) {
            return "PCM_SUB_16_BIT";
        }
        if (o == 2) {
            return "PCM_SUB_8_BIT";
        }
        if (o == 3) {
            return "PCM_SUB_32_BIT";
        }
        if (o == 4) {
            return "PCM_SUB_8_24_BIT";
        }
        if (o == 5) {
            return "PCM_SUB_FLOAT";
        }
        if (o == 6) {
            return "PCM_SUB_24_BIT_PACKED";
        }
        if (o == 0) {
            return "MP3_SUB_NONE";
        }
        if (o == 0) {
            return "AMR_SUB_NONE";
        }
        if (o == 1) {
            return "AAC_SUB_MAIN";
        }
        if (o == 2) {
            return "AAC_SUB_LC";
        }
        if (o == 4) {
            return "AAC_SUB_SSR";
        }
        if (o == 8) {
            return "AAC_SUB_LTP";
        }
        if (o == 16) {
            return "AAC_SUB_HE_V1";
        }
        if (o == 32) {
            return "AAC_SUB_SCALABLE";
        }
        if (o == 64) {
            return "AAC_SUB_ERLC";
        }
        if (o == 128) {
            return "AAC_SUB_LD";
        }
        if (o == 256) {
            return "AAC_SUB_HE_V2";
        }
        if (o == 512) {
            return "AAC_SUB_ELD";
        }
        if (o == 0) {
            return "VORBIS_SUB_NONE";
        }
        if (o == 1) {
            return "PCM_16_BIT";
        }
        if (o == 2) {
            return "PCM_8_BIT";
        }
        if (o == 3) {
            return "PCM_32_BIT";
        }
        if (o == 4) {
            return "PCM_8_24_BIT";
        }
        if (o == 5) {
            return "PCM_FLOAT";
        }
        if (o == 6) {
            return "PCM_24_BIT_PACKED";
        }
        if (o == 67108865) {
            return "AAC_MAIN";
        }
        if (o == 67108866) {
            return "AAC_LC";
        }
        if (o == 67108868) {
            return "AAC_SSR";
        }
        if (o == 67108872) {
            return "AAC_LTP";
        }
        if (o == 67108880) {
            return "AAC_HE_V1";
        }
        if (o == 67108896) {
            return "AAC_SCALABLE";
        }
        if (o == 67108928) {
            return "AAC_ERLC";
        }
        if (o == 67108992) {
            return "AAC_LD";
        }
        if (o == 67109120) {
            return "AAC_HE_V2";
        }
        if (o == 67109376) {
            return "AAC_ELD";
        }
        if (o == 503316481) {
            return "AAC_ADTS_MAIN";
        }
        if (o == 503316482) {
            return "AAC_ADTS_LC";
        }
        if (o == 503316484) {
            return "AAC_ADTS_SSR";
        }
        if (o == 503316488) {
            return "AAC_ADTS_LTP";
        }
        if (o == 503316496) {
            return "AAC_ADTS_HE_V1";
        }
        if (o == 503316512) {
            return "AAC_ADTS_SCALABLE";
        }
        if (o == 503316544) {
            return "AAC_ADTS_ERLC";
        }
        if (o == 503316608) {
            return "AAC_ADTS_LD";
        }
        if (o == 503316736) {
            return "AAC_ADTS_HE_V2";
        }
        if (o == 503316992) {
            return "AAC_ADTS_ELD";
        }
        return "0x" + java.lang.Integer.toHexString(o);
    }

    public static final java.lang.String dumpBitfield(int o) {
        java.util.ArrayList<java.lang.String> list = new java.util.ArrayList<>();
        int flipped = 0;
        if ((o & (-1)) == -1) {
            list.add("INVALID");
            flipped = 0 | (-1);
        }
        list.add("DEFAULT");
        list.add("PCM");
        if ((o & 16777216) == 16777216) {
            list.add("MP3");
            flipped |= 16777216;
        }
        if ((o & 33554432) == 33554432) {
            list.add("AMR_NB");
            flipped |= 33554432;
        }
        if ((o & AMR_WB) == 50331648) {
            list.add("AMR_WB");
            flipped |= AMR_WB;
        }
        if ((o & 67108864) == 67108864) {
            list.add("AAC");
            flipped |= 67108864;
        }
        if ((o & HE_AAC_V1) == 83886080) {
            list.add("HE_AAC_V1");
            flipped |= HE_AAC_V1;
        }
        if ((o & HE_AAC_V2) == 100663296) {
            list.add("HE_AAC_V2");
            flipped |= HE_AAC_V2;
        }
        if ((o & VORBIS) == 117440512) {
            list.add("VORBIS");
            flipped |= VORBIS;
        }
        if ((o & 134217728) == 134217728) {
            list.add("OPUS");
            flipped |= 134217728;
        }
        if ((o & AC3) == 150994944) {
            list.add("AC3");
            flipped |= AC3;
        }
        if ((o & E_AC3) == 167772160) {
            list.add("E_AC3");
            flipped |= E_AC3;
        }
        if ((o & DTS) == 184549376) {
            list.add("DTS");
            flipped |= DTS;
        }
        if ((o & DTS_HD) == 201326592) {
            list.add("DTS_HD");
            flipped |= DTS_HD;
        }
        if ((o & IEC61937) == 218103808) {
            list.add("IEC61937");
            flipped |= IEC61937;
        }
        if ((o & DOLBY_TRUEHD) == 234881024) {
            list.add("DOLBY_TRUEHD");
            flipped |= DOLBY_TRUEHD;
        }
        if ((o & 268435456) == 268435456) {
            list.add("EVRC");
            flipped |= 268435456;
        }
        if ((285212672 & o) == 285212672) {
            list.add("EVRCB");
            flipped |= EVRCB;
        }
        if ((301989888 & o) == 301989888) {
            list.add("EVRCWB");
            flipped |= EVRCWB;
        }
        if ((318767104 & o) == 318767104) {
            list.add("EVRCNW");
            flipped |= EVRCNW;
        }
        if ((335544320 & o) == 335544320) {
            list.add("AAC_ADIF");
            flipped |= AAC_ADIF;
        }
        if ((352321536 & o) == 352321536) {
            list.add("WMA");
            flipped |= WMA;
        }
        if ((369098752 & o) == 369098752) {
            list.add("WMA_PRO");
            flipped |= WMA_PRO;
        }
        if ((385875968 & o) == 385875968) {
            list.add("AMR_WB_PLUS");
            flipped |= AMR_WB_PLUS;
        }
        if ((402653184 & o) == 402653184) {
            list.add("MP2");
            flipped |= MP2;
        }
        if ((419430400 & o) == 419430400) {
            list.add("QCELP");
            flipped |= QCELP;
        }
        if ((436207616 & o) == 436207616) {
            list.add("DSD");
            flipped |= DSD;
        }
        if ((452984832 & o) == 452984832) {
            list.add("FLAC");
            flipped |= FLAC;
        }
        if ((469762048 & o) == 469762048) {
            list.add("ALAC");
            flipped |= ALAC;
        }
        if ((486539264 & o) == 486539264) {
            list.add("APE");
            flipped |= APE;
        }
        if ((503316480 & o) == 503316480) {
            list.add("AAC_ADTS");
            flipped |= AAC_ADTS;
        }
        if ((520093696 & o) == 520093696) {
            list.add("SBC");
            flipped |= SBC;
        }
        if ((536870912 & o) == 536870912) {
            list.add("APTX");
            flipped |= 536870912;
        }
        if ((553648128 & o) == 553648128) {
            list.add("APTX_HD");
            flipped |= APTX_HD;
        }
        if ((570425344 & o) == 570425344) {
            list.add("AC4");
            flipped |= AC4;
        }
        if ((587202560 & o) == 587202560) {
            list.add("LDAC");
            flipped |= LDAC;
        }
        if (((-16777216) & o) == -16777216) {
            list.add("MAIN_MASK");
            flipped |= MAIN_MASK;
        }
        if ((16777215 & o) == 16777215) {
            list.add("SUB_MASK");
            flipped |= SUB_MASK;
        }
        if ((o & 1) == 1) {
            list.add("PCM_SUB_16_BIT");
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("PCM_SUB_8_BIT");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("PCM_SUB_32_BIT");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("PCM_SUB_8_24_BIT");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("PCM_SUB_FLOAT");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("PCM_SUB_24_BIT_PACKED");
            flipped |= 6;
        }
        list.add("MP3_SUB_NONE");
        list.add("AMR_SUB_NONE");
        if ((o & 1) == 1) {
            list.add("AAC_SUB_MAIN");
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("AAC_SUB_LC");
            flipped |= 2;
        }
        if ((o & 4) == 4) {
            list.add("AAC_SUB_SSR");
            flipped |= 4;
        }
        if ((o & 8) == 8) {
            list.add("AAC_SUB_LTP");
            flipped |= 8;
        }
        if ((o & 16) == 16) {
            list.add("AAC_SUB_HE_V1");
            flipped |= 16;
        }
        if ((o & 32) == 32) {
            list.add("AAC_SUB_SCALABLE");
            flipped |= 32;
        }
        if ((o & 64) == 64) {
            list.add("AAC_SUB_ERLC");
            flipped |= 64;
        }
        if ((o & 128) == 128) {
            list.add("AAC_SUB_LD");
            flipped |= 128;
        }
        if ((o & 256) == 256) {
            list.add("AAC_SUB_HE_V2");
            flipped |= 256;
        }
        if ((o & 512) == 512) {
            list.add("AAC_SUB_ELD");
            flipped |= 512;
        }
        list.add("VORBIS_SUB_NONE");
        if ((o & 1) == 1) {
            list.add("PCM_16_BIT");
            flipped |= 1;
        }
        if ((o & 2) == 2) {
            list.add("PCM_8_BIT");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("PCM_32_BIT");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("PCM_8_24_BIT");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("PCM_FLOAT");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("PCM_24_BIT_PACKED");
            flipped |= 6;
        }
        if ((67108865 & o) == 67108865) {
            list.add("AAC_MAIN");
            flipped |= AAC_MAIN;
        }
        if ((67108866 & o) == 67108866) {
            list.add("AAC_LC");
            flipped |= AAC_LC;
        }
        if ((67108868 & o) == 67108868) {
            list.add("AAC_SSR");
            flipped |= AAC_SSR;
        }
        if ((67108872 & o) == 67108872) {
            list.add("AAC_LTP");
            flipped |= AAC_LTP;
        }
        if ((67108880 & o) == 67108880) {
            list.add("AAC_HE_V1");
            flipped |= AAC_HE_V1;
        }
        if ((67108896 & o) == 67108896) {
            list.add("AAC_SCALABLE");
            flipped |= AAC_SCALABLE;
        }
        if ((67108928 & o) == 67108928) {
            list.add("AAC_ERLC");
            flipped |= AAC_ERLC;
        }
        if ((67108992 & o) == 67108992) {
            list.add("AAC_LD");
            flipped |= AAC_LD;
        }
        if ((67109120 & o) == 67109120) {
            list.add("AAC_HE_V2");
            flipped |= AAC_HE_V2;
        }
        if ((67109376 & o) == 67109376) {
            list.add("AAC_ELD");
            flipped |= AAC_ELD;
        }
        if ((503316481 & o) == 503316481) {
            list.add("AAC_ADTS_MAIN");
            flipped |= AAC_ADTS_MAIN;
        }
        if ((503316482 & o) == 503316482) {
            list.add("AAC_ADTS_LC");
            flipped |= AAC_ADTS_LC;
        }
        if ((503316484 & o) == 503316484) {
            list.add("AAC_ADTS_SSR");
            flipped |= AAC_ADTS_SSR;
        }
        if ((503316488 & o) == 503316488) {
            list.add("AAC_ADTS_LTP");
            flipped |= AAC_ADTS_LTP;
        }
        if ((503316496 & o) == 503316496) {
            list.add("AAC_ADTS_HE_V1");
            flipped |= AAC_ADTS_HE_V1;
        }
        if ((503316512 & o) == 503316512) {
            list.add("AAC_ADTS_SCALABLE");
            flipped |= AAC_ADTS_SCALABLE;
        }
        if ((503316544 & o) == 503316544) {
            list.add("AAC_ADTS_ERLC");
            flipped |= AAC_ADTS_ERLC;
        }
        if ((503316608 & o) == 503316608) {
            list.add("AAC_ADTS_LD");
            flipped |= AAC_ADTS_LD;
        }
        if ((503316736 & o) == 503316736) {
            list.add("AAC_ADTS_HE_V2");
            flipped |= AAC_ADTS_HE_V2;
        }
        if ((503316992 & o) == 503316992) {
            list.add("AAC_ADTS_ELD");
            flipped |= AAC_ADTS_ELD;
        }
        if (o != flipped) {
            list.add("0x" + java.lang.Integer.toHexString((~flipped) & o));
        }
        return java.lang.String.join(" | ", list);
    }
}
