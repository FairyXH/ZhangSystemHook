package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public class HintsHelper {
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_NUMBER = "creditCardNumber";
    public static final java.lang.String AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
    public static final java.lang.String AUTOFILL_HINT_EMAIL_ADDRESS = "emailAddress";
    public static final java.lang.String AUTOFILL_HINT_NEW_PASSWORD = "newPassword";
    public static final java.lang.String AUTOFILL_HINT_NEW_USERNAME = "newUsername";
    public static final java.lang.String AUTOFILL_HINT_PASSWORD = "password";
    public static final java.lang.String AUTOFILL_HINT_PHONE = "phone";
    public static final java.lang.String AUTOFILL_HINT_PHONE_COUNTRY_CODE = "phoneCountryCode";
    public static final java.lang.String AUTOFILL_HINT_PHONE_NATIONAL = "phoneNational";
    public static final java.lang.String AUTOFILL_HINT_PHONE_NUMBER = "phoneNumber";
    public static final java.lang.String AUTOFILL_HINT_PHONE_NUMBER_DEVICE = "phoneNumberDevice";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS = "postalAddress";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_APT_NUMBER = "aptNumber";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_COUNTRY = "addressCountry";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_DEPENDENT_LOCALITY = "dependentLocality";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_ADDRESS = "extendedAddress";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_POSTAL_CODE = "extendedPostalCode";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_LOCALITY = "addressLocality";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_REGION = "addressRegion";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_ADDRESS_STREET_ADDRESS = "streetAddress";
    public static final java.lang.String AUTOFILL_HINT_POSTAL_CODE = "postalCode";
    public static final java.lang.String AUTOFILL_HINT_USERNAME = "username";

    private HintsHelper() {
    }

    public static java.util.Set<java.lang.String> getHintsForSaveType(int saveType) {
        android.util.ArraySet<java.lang.String> hintSet = new android.util.ArraySet<>();
        switch (saveType) {
            case 1:
                hintSet.add(AUTOFILL_HINT_NEW_USERNAME);
                hintSet.add(AUTOFILL_HINT_USERNAME);
                hintSet.add(AUTOFILL_HINT_NEW_PASSWORD);
                hintSet.add(AUTOFILL_HINT_PASSWORD);
                break;
            case 2:
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_APT_NUMBER);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_COUNTRY);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_DEPENDENT_LOCALITY);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_ADDRESS);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_EXTENDED_POSTAL_CODE);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_LOCALITY);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_REGION);
                hintSet.add(AUTOFILL_HINT_POSTAL_ADDRESS_STREET_ADDRESS);
                hintSet.add(AUTOFILL_HINT_POSTAL_CODE);
                break;
            case 4:
            case 32:
            case 64:
            case 128:
                hintSet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE);
                hintSet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY);
                hintSet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH);
                hintSet.add(AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR);
                hintSet.add(AUTOFILL_HINT_CREDIT_CARD_NUMBER);
                hintSet.add(AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE);
                break;
            case 8:
                hintSet.add(AUTOFILL_HINT_NEW_USERNAME);
                hintSet.add(AUTOFILL_HINT_USERNAME);
                break;
            case 16:
                hintSet.add(AUTOFILL_HINT_EMAIL_ADDRESS);
                break;
        }
        return hintSet;
    }
}
