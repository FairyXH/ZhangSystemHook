package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class NonCredentialProviderCallerException extends java.lang.RuntimeException {
    private static final java.lang.String MESSAGE = " is not an existing Credential Provider.";

    public NonCredentialProviderCallerException(java.lang.String caller) {
        super(caller + MESSAGE);
    }
}
