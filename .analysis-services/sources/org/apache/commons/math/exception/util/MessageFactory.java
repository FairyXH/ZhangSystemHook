package org.apache.commons.math.exception.util;

/* JADX INFO: loaded from: classes4.dex */
public class MessageFactory {
    private MessageFactory() {
    }

    public static java.lang.String buildMessage(java.util.Locale locale, org.apache.commons.math.exception.util.Localizable pattern, java.lang.Object... arguments) {
        return buildMessage(locale, null, pattern, arguments);
    }

    public static java.lang.String buildMessage(java.util.Locale locale, org.apache.commons.math.exception.util.Localizable specific, org.apache.commons.math.exception.util.Localizable general, java.lang.Object... arguments) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (general != null) {
            java.text.MessageFormat fmt = new java.text.MessageFormat(general.getLocalizedString(locale), locale);
            sb.append(fmt.format(arguments));
        }
        if (specific != null) {
            if (general != null) {
                sb.append(": ");
            }
            java.text.MessageFormat fmt2 = new java.text.MessageFormat(specific.getLocalizedString(locale), locale);
            sb.append(fmt2.format(arguments));
        }
        return sb.toString();
    }
}
