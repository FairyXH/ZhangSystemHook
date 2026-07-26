package com.android.server.biometrics;

/* JADX INFO: loaded from: classes.dex */
public class SensorConfig {
    public final int id;
    final int modality;
    public final int strength;

    public SensorConfig(java.lang.String config) {
        java.lang.String[] elems = config.split(":");
        this.id = java.lang.Integer.parseInt(elems[0]);
        this.modality = java.lang.Integer.parseInt(elems[1]);
        this.strength = java.lang.Integer.parseInt(elems[2]);
    }
}
