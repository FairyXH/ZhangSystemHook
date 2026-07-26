package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
abstract class PolicySerializer<V> {
    abstract android.app.admin.PolicyValue<V> readFromXml(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser);

    abstract void saveToXml(com.android.modules.utils.TypedXmlSerializer typedXmlSerializer, V v) throws java.io.IOException;

    PolicySerializer() {
    }
}
