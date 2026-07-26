package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: StringNumberConversionsJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlin/text/ScreenFloatValueRegEx;", "", "()V", "value", "Lkotlin/text/Regex;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ScreenFloatValueRegEx {
    public static final com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx INSTANCE = new com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx();
    public static final com.android.server.permission.jarjar.kotlin.text.Regex value;

    private ScreenFloatValueRegEx() {
    }

    static {
        com.android.server.permission.jarjar.kotlin.text.ScreenFloatValueRegEx screenFloatValueRegEx = INSTANCE;
        java.lang.String Exp = "[eE][+-]?(\\p{Digit}+)";
        java.lang.String HexString = "(0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+))";
        java.lang.String Number = "((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)(" + Exp + ")?)|(\\.((\\p{Digit}+))(" + Exp + ")?)|((" + HexString + ")[pP][+-]?(\\p{Digit}+))";
        java.lang.String fpRegex = "[\\x00-\\x20]*[+-]?(NaN|Infinity|((" + Number + ")[fFdD]?))[\\x00-\\x20]*";
        value = new com.android.server.permission.jarjar.kotlin.text.Regex(fpRegex);
    }
}
