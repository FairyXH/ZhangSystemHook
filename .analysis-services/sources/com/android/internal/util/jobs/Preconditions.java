package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class Preconditions {
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, java.lang.Object errorMessage) {
        if (!expression) {
            throw new java.lang.IllegalArgumentException(java.lang.String.valueOf(errorMessage));
        }
    }

    public static void checkArgument(boolean expression, java.lang.String messageTemplate, java.lang.Object... messageArgs) {
        if (!expression) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(messageTemplate, messageArgs));
        }
    }

    public static <T extends java.lang.CharSequence> T checkStringNotEmpty(T string) {
        if (android.text.TextUtils.isEmpty(string)) {
            throw new java.lang.IllegalArgumentException();
        }
        return string;
    }

    public static <T extends java.lang.CharSequence> T checkStringNotEmpty(T string, java.lang.Object errorMessage) {
        if (android.text.TextUtils.isEmpty(string)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.valueOf(errorMessage));
        }
        return string;
    }

    public static <T extends java.lang.CharSequence> T checkStringNotEmpty(T string, java.lang.String messageTemplate, java.lang.Object... messageArgs) {
        if (android.text.TextUtils.isEmpty(string)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(messageTemplate, messageArgs));
        }
        return string;
    }

    @java.lang.Deprecated
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new java.lang.NullPointerException();
        }
        return reference;
    }

    @java.lang.Deprecated
    public static <T> T checkNotNull(T reference, java.lang.Object errorMessage) {
        if (reference == null) {
            throw new java.lang.NullPointerException(java.lang.String.valueOf(errorMessage));
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, java.lang.String messageTemplate, java.lang.Object... messageArgs) {
        if (reference == null) {
            throw new java.lang.NullPointerException(java.lang.String.format(messageTemplate, messageArgs));
        }
        return reference;
    }

    public static void checkState(boolean expression) {
        checkState(expression, null);
    }

    public static void checkState(boolean expression, java.lang.String errorMessage) {
        if (!expression) {
            throw new java.lang.IllegalStateException(errorMessage);
        }
    }

    public static void checkState(boolean expression, java.lang.String messageTemplate, java.lang.Object... messageArgs) {
        if (!expression) {
            throw new java.lang.IllegalStateException(java.lang.String.format(messageTemplate, messageArgs));
        }
    }

    public static void checkCallAuthorization(boolean expression) {
        if (!expression) {
            throw new java.lang.SecurityException("Calling identity is not authorized");
        }
    }

    public static void checkCallAuthorization(boolean expression, java.lang.String message) {
        if (!expression) {
            throw new java.lang.SecurityException(message);
        }
    }

    public static void checkCallAuthorization(boolean expression, java.lang.String messageTemplate, java.lang.Object... messageArgs) {
        if (!expression) {
            throw new java.lang.SecurityException(java.lang.String.format(messageTemplate, messageArgs));
        }
    }

    public static void checkCallingUser(boolean expression) {
        if (!expression) {
            throw new java.lang.SecurityException("Calling user is not authorized");
        }
    }

    public static int checkFlagsArgument(int requestedFlags, int allowedFlags) {
        if ((requestedFlags & allowedFlags) != requestedFlags) {
            throw new java.lang.IllegalArgumentException("Requested flags 0x" + java.lang.Integer.toHexString(requestedFlags) + ", but only 0x" + java.lang.Integer.toHexString(allowedFlags) + " are allowed");
        }
        return requestedFlags;
    }

    public static int checkArgumentNonnegative(int value, java.lang.String errorMessage) {
        if (value < 0) {
            throw new java.lang.IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static int checkArgumentNonnegative(int value) {
        if (value < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        return value;
    }

    public static long checkArgumentNonnegative(long value) {
        if (value < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        return value;
    }

    public static long checkArgumentNonnegative(long value, java.lang.String errorMessage) {
        if (value < 0) {
            throw new java.lang.IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static int checkArgumentPositive(int value, java.lang.String errorMessage) {
        if (value <= 0) {
            throw new java.lang.IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static float checkArgumentNonNegative(float value, java.lang.String errorMessage) {
        if (value < 0.0f) {
            throw new java.lang.IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static float checkArgumentPositive(float value, java.lang.String errorMessage) {
        if (value <= 0.0f) {
            throw new java.lang.IllegalArgumentException(errorMessage);
        }
        return value;
    }

    public static float checkArgumentFinite(float value, java.lang.String valueName) {
        if (java.lang.Float.isNaN(value)) {
            throw new java.lang.IllegalArgumentException(valueName + " must not be NaN");
        }
        if (java.lang.Float.isInfinite(value)) {
            throw new java.lang.IllegalArgumentException(valueName + " must not be infinite");
        }
        return value;
    }

    public static float checkArgumentInRange(float value, float lower, float upper, java.lang.String valueName) {
        if (java.lang.Float.isNaN(value)) {
            throw new java.lang.IllegalArgumentException(valueName + " must not be NaN");
        }
        if (value < lower) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%f, %f] (too low)", valueName, java.lang.Float.valueOf(lower), java.lang.Float.valueOf(upper)));
        }
        if (value > upper) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%f, %f] (too high)", valueName, java.lang.Float.valueOf(lower), java.lang.Float.valueOf(upper)));
        }
        return value;
    }

    public static double checkArgumentInRange(double value, double lower, double upper, java.lang.String valueName) {
        if (java.lang.Double.isNaN(value)) {
            throw new java.lang.IllegalArgumentException(valueName + " must not be NaN");
        }
        if (value < lower) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%f, %f] (too low)", valueName, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper)));
        }
        if (value > upper) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%f, %f] (too high)", valueName, java.lang.Double.valueOf(lower), java.lang.Double.valueOf(upper)));
        }
        return value;
    }

    public static int checkArgumentInRange(int value, int lower, int upper, java.lang.String valueName) {
        if (value < lower) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%d, %d] (too low)", valueName, java.lang.Integer.valueOf(lower), java.lang.Integer.valueOf(upper)));
        }
        if (value > upper) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%d, %d] (too high)", valueName, java.lang.Integer.valueOf(lower), java.lang.Integer.valueOf(upper)));
        }
        return value;
    }

    public static long checkArgumentInRange(long value, long lower, long upper, java.lang.String valueName) {
        if (value < lower) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%d, %d] (too low)", valueName, java.lang.Long.valueOf(lower), java.lang.Long.valueOf(upper)));
        }
        if (value > upper) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s is out of range of [%d, %d] (too high)", valueName, java.lang.Long.valueOf(lower), java.lang.Long.valueOf(upper)));
        }
        return value;
    }

    public static <T> T[] checkArrayElementsNotNull(T[] value, java.lang.String valueName) {
        if (value == null) {
            throw new java.lang.NullPointerException(valueName + " must not be null");
        }
        for (int i = 0; i < value.length; i++) {
            if (value[i] == null) {
                throw new java.lang.NullPointerException(java.lang.String.format("%s[%d] must not be null", valueName, java.lang.Integer.valueOf(i)));
            }
        }
        return value;
    }

    public static <C extends java.util.Collection<T>, T> C checkCollectionElementsNotNull(C value, java.lang.String valueName) {
        if (value == null) {
            throw new java.lang.NullPointerException(valueName + " must not be null");
        }
        long ctr = 0;
        java.util.Iterator it = value.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                throw new java.lang.NullPointerException(java.lang.String.format("%s[%d] must not be null", valueName, java.lang.Long.valueOf(ctr)));
            }
            ctr++;
        }
        return value;
    }

    public static <T> java.util.Collection<T> checkCollectionNotEmpty(java.util.Collection<T> value, java.lang.String valueName) {
        if (value == null) {
            throw new java.lang.NullPointerException(valueName + " must not be null");
        }
        if (value.isEmpty()) {
            throw new java.lang.IllegalArgumentException(valueName + " is empty");
        }
        return value;
    }

    public static byte[] checkByteArrayNotEmpty(byte[] value, java.lang.String valueName) {
        if (value == null) {
            throw new java.lang.NullPointerException(valueName + " must not be null");
        }
        if (value.length == 0) {
            throw new java.lang.IllegalArgumentException(valueName + " is empty");
        }
        return value;
    }

    public static java.lang.String checkArgumentIsSupported(java.lang.String[] supportedValues, java.lang.String value) {
        checkNotNull(value);
        checkNotNull(supportedValues);
        if (!contains(supportedValues, value)) {
            throw new java.lang.IllegalArgumentException(value + "is not supported " + java.util.Arrays.toString(supportedValues));
        }
        return value;
    }

    private static boolean contains(java.lang.String[] values, java.lang.String value) {
        if (values == null) {
            return false;
        }
        for (java.lang.String str : values) {
            if (java.util.Objects.equals(value, str)) {
                return true;
            }
        }
        return false;
    }

    public static float[] checkArrayElementsInRange(float[] value, float lower, float upper, java.lang.String valueName) {
        checkNotNull(value, "%s must not be null", valueName);
        for (int i = 0; i < value.length; i++) {
            float v = value[i];
            if (java.lang.Float.isNaN(v)) {
                throw new java.lang.IllegalArgumentException(valueName + "[" + i + "] must not be NaN");
            }
            if (v < lower) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("%s[%d] is out of range of [%f, %f] (too low)", valueName, java.lang.Integer.valueOf(i), java.lang.Float.valueOf(lower), java.lang.Float.valueOf(upper)));
            }
            if (v > upper) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("%s[%d] is out of range of [%f, %f] (too high)", valueName, java.lang.Integer.valueOf(i), java.lang.Float.valueOf(lower), java.lang.Float.valueOf(upper)));
            }
        }
        return value;
    }

    public static int[] checkArrayElementsInRange(int[] value, int lower, int upper, java.lang.String valueName) {
        checkNotNull(value, "%s must not be null", valueName);
        for (int i = 0; i < value.length; i++) {
            int v = value[i];
            if (v < lower) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("%s[%d] is out of range of [%d, %d] (too low)", valueName, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(lower), java.lang.Integer.valueOf(upper)));
            }
            if (v > upper) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("%s[%d] is out of range of [%d, %d] (too high)", valueName, java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(lower), java.lang.Integer.valueOf(upper)));
            }
        }
        return value;
    }

    public static <T> T requireNonNullViaRavenwoodRule(T t) {
        if (t == null) {
            throw new java.lang.IllegalStateException("This operation requires that a RavenwoodRule be configured to accurately define the expected test environment");
        }
        return t;
    }
}
