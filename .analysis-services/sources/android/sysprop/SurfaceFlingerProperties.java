package android.sysprop;

/* JADX INFO: loaded from: classes.dex */
public final class SurfaceFlingerProperties {
    private SurfaceFlingerProperties() {
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
        throw new UnsupportedOperationException("Method not decompiled: android.sysprop.SurfaceFlingerProperties.tryParseBoolean(java.lang.String):java.lang.Boolean");
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

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.Double tryParseDouble(java.lang.String str) {
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

    public static java.util.Optional<java.lang.Long> vsync_event_phase_offset_ns() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.vsync_event_phase_offset_ns");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Long> vsync_sf_event_phase_offset_ns() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.vsync_sf_event_phase_offset_ns");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Boolean> use_context_priority() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.use_context_priority");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Long> max_frame_buffer_acquired_buffers() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.max_frame_buffer_acquired_buffers");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Integer> max_graphics_width() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.max_graphics_width");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Integer> max_graphics_height() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.max_graphics_height");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Boolean> has_wide_color_display() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.has_wide_color_display");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> running_without_sync_framework() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.running_without_sync_framework");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> has_HDR_display() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.has_HDR_display");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Long> present_time_offset_from_vsync_ns() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.present_time_offset_from_vsync_ns");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Boolean> force_hwc_copy_for_virtual_displays() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.force_hwc_copy_for_virtual_displays");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Long> max_virtual_display_dimension() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.max_virtual_display_dimension");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Boolean> use_vr_flinger() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.use_vr_flinger");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> start_graphics_allocator_service() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.start_graphics_allocator_service");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public enum primary_display_orientation_values {
        ORIENTATION_0("ORIENTATION_0"),
        ORIENTATION_90("ORIENTATION_90"),
        ORIENTATION_180("ORIENTATION_180"),
        ORIENTATION_270("ORIENTATION_270");

        private final java.lang.String propValue;

        primary_display_orientation_values(java.lang.String propValue) {
            this.propValue = propValue;
        }

        public java.lang.String getPropValue() {
            return this.propValue;
        }
    }

    public static java.util.Optional<android.sysprop.SurfaceFlingerProperties.primary_display_orientation_values> primary_display_orientation() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.primary_display_orientation");
        return java.util.Optional.ofNullable((android.sysprop.SurfaceFlingerProperties.primary_display_orientation_values) tryParseEnum(android.sysprop.SurfaceFlingerProperties.primary_display_orientation_values.class, value));
    }

    public static java.util.Optional<java.lang.Boolean> use_color_management() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.use_color_management");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Long> default_composition_dataspace() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.default_composition_dataspace");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Integer> default_composition_pixel_format() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.default_composition_pixel_format");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Long> wcg_composition_dataspace() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.wcg_composition_dataspace");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Integer> wcg_composition_pixel_format() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.wcg_composition_pixel_format");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Long> color_space_agnostic_dataspace() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.color_space_agnostic_dataspace");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.List<java.lang.Double> display_primary_red() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.display_primary_red");
        return tryParseList(new java.util.function.Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.sysprop.SurfaceFlingerProperties.tryParseDouble((java.lang.String) obj);
            }
        }, value);
    }

    public static java.util.List<java.lang.Double> display_primary_green() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.display_primary_green");
        return tryParseList(new java.util.function.Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.sysprop.SurfaceFlingerProperties.tryParseDouble((java.lang.String) obj);
            }
        }, value);
    }

    public static java.util.List<java.lang.Double> display_primary_blue() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.display_primary_blue");
        return tryParseList(new java.util.function.Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.sysprop.SurfaceFlingerProperties.tryParseDouble((java.lang.String) obj);
            }
        }, value);
    }

    public static java.util.List<java.lang.Double> display_primary_white() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.display_primary_white");
        return tryParseList(new java.util.function.Function() { // from class: android.sysprop.SurfaceFlingerProperties$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return android.sysprop.SurfaceFlingerProperties.tryParseDouble((java.lang.String) obj);
            }
        }, value);
    }

    @java.lang.Deprecated
    public static java.util.Optional<java.lang.Boolean> refresh_rate_switching() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.refresh_rate_switching");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Integer> set_idle_timer_ms() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.set_idle_timer_ms");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Integer> set_touch_timer_ms() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.set_touch_timer_ms");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Integer> set_display_power_timer_ms() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.set_display_power_timer_ms");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Boolean> use_content_detection_for_refresh_rate() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.use_content_detection_for_refresh_rate");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    @java.lang.Deprecated
    public static java.util.Optional<java.lang.Boolean> use_smart_90_for_video() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.use_smart_90_for_video");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> enable_protected_contents() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.protected_contents");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> support_kernel_idle_timer() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.support_kernel_idle_timer");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> supports_background_blur() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.supports_background_blur");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Integer> display_update_imminent_timeout_ms() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.display_update_imminent_timeout_ms");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }

    public static java.util.Optional<java.lang.Boolean> update_device_product_info_on_hotplug_reconnect() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.update_device_product_info_on_hotplug_reconnect");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> enable_frame_rate_override() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.enable_frame_rate_override");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> enable_layer_caching() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.enable_layer_caching");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> enable_sdr_dimming() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.enable_sdr_dimming");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Boolean> ignore_hdr_camera_layers() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.ignore_hdr_camera_layers");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Long> min_acquired_buffers() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.min_acquired_buffers");
        return java.util.Optional.ofNullable(tryParseLong(value));
    }

    public static java.util.Optional<java.lang.Boolean> clear_slots_with_set_layer_buffer() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.clear_slots_with_set_layer_buffer");
        return java.util.Optional.ofNullable(tryParseBoolean(value));
    }

    public static java.util.Optional<java.lang.Integer> game_default_frame_rate_override() {
        java.lang.String value = android.os.SystemProperties.get("ro.surface_flinger.game_default_frame_rate_override");
        return java.util.Optional.ofNullable(tryParseInteger(value));
    }
}
