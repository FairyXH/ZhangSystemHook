package android.sysprop;

/* JADX INFO: loaded from: classes.dex */
public final class WatchdogProperties {
    private WatchdogProperties() {
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.Boolean tryParseBoolean(java.lang.String r3) {
        /*
            r0 = 0
            if (r3 != 0) goto L4
            return r0
        L4:
            java.util.Locale r1 = java.util.Locale.US
            java.lang.String r1 = r3.toLowerCase(r1)
            int r2 = r1.hashCode()
            switch(r2) {
                case 48: goto L31;
                case 49: goto L27;
                case 3569038: goto L1c;
                case 97196323: goto L12;
                default: goto L11;
            }
        L11:
            goto L3b
        L12:
            java.lang.String r2 = "false"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L11
            r1 = 3
            goto L3c
        L1c:
            java.lang.String r2 = "true"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L11
            r1 = 1
            goto L3c
        L27:
            java.lang.String r2 = "1"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L11
            r1 = 0
            goto L3c
        L31:
            java.lang.String r2 = "0"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L11
            r1 = 2
            goto L3c
        L3b:
            r1 = -1
        L3c:
            switch(r1) {
                case 0: goto L43;
                case 1: goto L43;
                case 2: goto L40;
                case 3: goto L40;
                default: goto L3f;
            }
        L3f:
            return r0
        L40:
            java.lang.Boolean r0 = java.lang.Boolean.FALSE
            return r0
        L43:
            java.lang.Boolean r0 = java.lang.Boolean.TRUE
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.sysprop.WatchdogProperties.tryParseBoolean(java.lang.String):java.lang.Boolean");
    }

    private static java.lang.Integer tryParseInteger(java.lang.String str) {
        try {
            return java.lang.Integer.valueOf(str);
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private static java.lang.Integer tryParseUInt(java.lang.String str) {
        try {
            return java.lang.Integer.valueOf(java.lang.Integer.parseUnsignedInt(str));
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private static java.lang.Long tryParseLong(java.lang.String str) {
        try {
            return java.lang.Long.valueOf(str);
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private static java.lang.Long tryParseULong(java.lang.String str) {
        try {
            return java.lang.Long.valueOf(java.lang.Long.parseUnsignedLong(str));
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private static java.lang.Double tryParseDouble(java.lang.String str) {
        try {
            return java.lang.Double.valueOf(str);
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    private static java.lang.String tryParseString(java.lang.String str) {
        if ("".equals(str)) {
            return null;
        }
        return str;
    }

    private static <T extends java.lang.Enum<T>> T tryParseEnum(java.lang.Class<T> cls, java.lang.String str) {
        try {
            return (T) java.lang.Enum.valueOf(cls, str.toUpperCase(java.util.Locale.US));
        } catch (java.lang.IllegalArgumentException e) {
            return null;
        }
    }

    private static <T> java.util.List<T> tryParseList(java.util.function.Function<java.lang.String, T> elementParser, java.lang.String str) {
        if ("".equals(str)) {
            return new java.util.ArrayList();
        }
        java.util.List<T> ret = new java.util.ArrayList<>();
        int p = 0;
        while (true) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            while (p < str.length() && str.charAt(p) != ',') {
                if (str.charAt(p) == '\\') {
                    p++;
                }
                if (p == str.length()) {
                    break;
                }
                sb.append(str.charAt(p));
                p++;
            }
            ret.add(elementParser.apply(sb.toString()));
            if (p == str.length()) {
                return ret;
            }
            p++;
        }
    }

    private static <T extends java.lang.Enum<T>> java.util.List<T> tryParseEnumList(java.lang.Class<T> enumType, java.lang.String str) {
        if ("".equals(str)) {
            return new java.util.ArrayList();
        }
        java.util.ArrayList arrayList = new java.util.ArrayList();
        for (java.lang.String element : str.split(",")) {
            arrayList.add(tryParseEnum(enumType, element));
        }
        return arrayList;
    }

    private static java.lang.String escape(java.lang.String str) {
        return str.replaceAll("([\\\\,])", "\\\\$1");
    }

    private static <T> java.lang.String formatList(java.util.List<T> list) {
        java.util.StringJoiner joiner = new java.util.StringJoiner(",");
        java.util.Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T element = it.next();
            joiner.add(element == null ? "" : escape(element.toString()));
        }
        return joiner.toString();
    }

    private static java.lang.String formatUIntList(java.util.List<java.lang.Integer> list) {
        java.util.StringJoiner joiner = new java.util.StringJoiner(",");
        java.util.Iterator<java.lang.Integer> it = list.iterator();
        while (it.hasNext()) {
            java.lang.Integer element = it.next();
            joiner.add(element == null ? "" : escape(java.lang.Integer.toUnsignedString(element.intValue())));
        }
        return joiner.toString();
    }

    private static java.lang.String formatULongList(java.util.List<java.lang.Long> list) {
        java.util.StringJoiner joiner = new java.util.StringJoiner(",");
        java.util.Iterator<java.lang.Long> it = list.iterator();
        while (it.hasNext()) {
            java.lang.Long element = it.next();
            joiner.add(element == null ? "" : escape(java.lang.Long.toUnsignedString(element.longValue())));
        }
        return joiner.toString();
    }

    private static <T extends java.lang.Enum<T>> java.lang.String formatEnumList(java.util.List<T> list, java.util.function.Function<T, java.lang.String> elementFormatter) {
        java.util.StringJoiner joiner = new java.util.StringJoiner(",");
        java.util.Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T element = it.next();
            joiner.add(element == null ? "" : elementFormatter.apply(element));
        }
        return joiner.toString();
    }

    public static java.util.Optional<java.lang.Integer> fatal_count() {
        java.lang.String value = android.os.SystemProperties.get("framework_watchdog.fatal_count");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Integer> fatal_window_seconds() {
        java.lang.String value = android.os.SystemProperties.get("framework_watchdog.fatal_window.second");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Boolean> should_ignore_fatal_count() {
        java.lang.String value = android.os.SystemProperties.get("persist.debug.framework_watchdog.fatal_ignore");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }
}
