package com.pixelworks.hardware;

/* JADX INFO: loaded from: classes3.dex */
public class IrisHalGetResult {
    public java.lang.String json;
    public int ret;
    public int[] values;

    public IrisHalGetResult() {
        this.ret = -1;
        this.values = new int[0];
    }

    public IrisHalGetResult(int ret) {
        this.ret = ret;
        this.values = new int[0];
    }

    public IrisHalGetResult(int ret, int[] values) {
        this.ret = ret;
        this.values = values;
    }

    public IrisHalGetResult(int ret, java.lang.String jsonIn) {
        this.ret = ret;
        this.json = jsonIn;
    }
}
