package com.android.server.credentials;

/* JADX INFO: loaded from: classes.dex */
public class PendingIntentResultHandler {
    public static boolean isValidResponse(android.credentials.selection.ProviderPendingIntentResponse pendingIntentResponse) {
        return pendingIntentResponse.getResultCode() == -1;
    }

    public static boolean isCancelledResponse(android.credentials.selection.ProviderPendingIntentResponse pendingIntentResponse) {
        return pendingIntentResponse.getResultCode() == 0;
    }

    public static android.service.credentials.BeginGetCredentialResponse extractResponseContent(android.content.Intent resultData) {
        if (resultData == null) {
            return null;
        }
        return (android.service.credentials.BeginGetCredentialResponse) resultData.getParcelableExtra("android.service.credentials.extra.BEGIN_GET_CREDENTIAL_RESPONSE", android.service.credentials.BeginGetCredentialResponse.class);
    }

    public static android.credentials.CreateCredentialResponse extractCreateCredentialResponse(android.content.Intent resultData) {
        if (resultData == null) {
            return null;
        }
        return (android.credentials.CreateCredentialResponse) resultData.getParcelableExtra("android.service.credentials.extra.CREATE_CREDENTIAL_RESPONSE", android.credentials.CreateCredentialResponse.class);
    }

    public static android.credentials.GetCredentialResponse extractGetCredentialResponse(android.content.Intent resultData) {
        if (resultData == null) {
            return null;
        }
        return (android.credentials.GetCredentialResponse) resultData.getParcelableExtra("android.service.credentials.extra.GET_CREDENTIAL_RESPONSE", android.credentials.GetCredentialResponse.class);
    }

    public static android.credentials.CreateCredentialException extractCreateCredentialException(android.content.Intent resultData) {
        if (resultData == null) {
            return null;
        }
        return (android.credentials.CreateCredentialException) resultData.getSerializableExtra("android.service.credentials.extra.CREATE_CREDENTIAL_EXCEPTION", android.credentials.CreateCredentialException.class);
    }

    public static android.credentials.GetCredentialException extractGetCredentialException(android.content.Intent resultData) {
        if (resultData == null) {
            return null;
        }
        return (android.credentials.GetCredentialException) resultData.getSerializableExtra("android.service.credentials.extra.GET_CREDENTIAL_EXCEPTION", android.credentials.GetCredentialException.class);
    }
}
