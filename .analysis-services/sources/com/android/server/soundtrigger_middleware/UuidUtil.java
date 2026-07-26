package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class UuidUtil {
    static final java.lang.String FORMAT = "%08x-%04x-%04x-%04x-%02x%02x%02x%02x%02x%02x";
    static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("^([a-fA-F0-9]{8})-([a-fA-F0-9]{4})-([a-fA-F0-9]{4})-([a-fA-F0-9]{4})-([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})([a-fA-F0-9]{2})$");
}
