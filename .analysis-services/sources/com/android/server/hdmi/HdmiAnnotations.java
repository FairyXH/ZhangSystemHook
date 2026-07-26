package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiAnnotations {

    @java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface IoThreadOnly {
    }

    @java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface ServiceThreadOnly {
    }
}
