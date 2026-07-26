package com.android.server.broadcastradio.aidl;

/* JADX INFO: loaded from: classes.dex */
final class ConversionUtils {
    public static final long RADIO_U_VERSION_REQUIRED = 261770108;
    public static final long RADIO_V_VERSION_REQUIRED = 302589903;
    private static final java.lang.String TAG = "BcRadioAidlSrv.convert";

    private ConversionUtils() {
        throw new java.lang.UnsupportedOperationException("ConversionUtils class is noninstantiable");
    }

    static boolean isAtLeastU(int uid) {
        return android.app.compat.CompatChanges.isChangeEnabled(RADIO_U_VERSION_REQUIRED, uid);
    }

    static boolean isAtLeastV(int uid) {
        return android.app.compat.CompatChanges.isChangeEnabled(RADIO_V_VERSION_REQUIRED, uid);
    }

    static java.lang.RuntimeException throwOnError(java.lang.RuntimeException halException, java.lang.String action) {
        if (!(halException instanceof android.os.ServiceSpecificException)) {
            return new android.os.ParcelableException(new java.lang.RuntimeException(action + ": unknown error"));
        }
        int result = ((android.os.ServiceSpecificException) halException).errorCode;
        switch (result) {
            case 1:
                return new android.os.ParcelableException(new java.lang.RuntimeException(action + ": INTERNAL_ERROR"));
            case 2:
                return new java.lang.IllegalArgumentException(action + ": INVALID_ARGUMENTS");
            case 3:
                return new java.lang.IllegalStateException(action + ": INVALID_STATE");
            case 4:
                return new java.lang.UnsupportedOperationException(action + ": NOT_SUPPORTED");
            case 5:
                return new android.os.ParcelableException(new java.lang.RuntimeException(action + ": TIMEOUT"));
            case 6:
                return new java.lang.IllegalStateException(action + ": CANCELED");
            case 7:
                return new android.os.ParcelableException(new java.lang.RuntimeException(action + ": UNKNOWN_ERROR"));
            default:
                return new android.os.ParcelableException(new java.lang.RuntimeException(action + ": unknown error (" + result + ")"));
        }
    }

    static int halResultToTunerResult(int result) {
        switch (result) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 7;
        }
    }

    static android.hardware.broadcastradio.VendorKeyValue[] vendorInfoToHalVendorKeyValues(java.util.Map<java.lang.String, java.lang.String> info) {
        if (info == null) {
            return new android.hardware.broadcastradio.VendorKeyValue[0];
        }
        java.util.ArrayList<android.hardware.broadcastradio.VendorKeyValue> list = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : info.entrySet()) {
            android.hardware.broadcastradio.VendorKeyValue elem = new android.hardware.broadcastradio.VendorKeyValue();
            elem.key = entry.getKey();
            elem.value = entry.getValue();
            if (elem.key == null || elem.value == null) {
                com.android.server.utils.Slogf.w(TAG, "VendorKeyValue contains invalid entry: key = %s, value = %s", elem.key, elem.value);
            } else {
                list.add(elem);
            }
        }
        return (android.hardware.broadcastradio.VendorKeyValue[]) list.toArray(new java.util.function.IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.broadcastradio.aidl.ConversionUtils.lambda$vendorInfoToHalVendorKeyValues$0(i);
            }
        });
    }

    static /* synthetic */ android.hardware.broadcastradio.VendorKeyValue[] lambda$vendorInfoToHalVendorKeyValues$0(int x$0) {
        return new android.hardware.broadcastradio.VendorKeyValue[x$0];
    }

    static java.util.Map<java.lang.String, java.lang.String> vendorInfoFromHalVendorKeyValues(android.hardware.broadcastradio.VendorKeyValue[] info) {
        if (info == null) {
            return java.util.Collections.emptyMap();
        }
        java.util.Map<java.lang.String, java.lang.String> map = new android.util.ArrayMap<>();
        for (android.hardware.broadcastradio.VendorKeyValue kvp : info) {
            if (kvp.key == null || kvp.value == null) {
                com.android.server.utils.Slogf.w(TAG, "VendorKeyValue contains invalid entry: key = %s, value = %s", kvp.key, kvp.value);
            } else {
                map.put(kvp.key, kvp.value);
            }
        }
        return map;
    }

    private static int identifierTypeToProgramType(int idType) {
        switch (idType) {
            case 1:
            case 2:
                return 2;
            case 3:
            case 10004:
                return 4;
            case 5:
            case 6:
            case 7:
            case 8:
            case 14:
                return 5;
            case 9:
            case 10:
                return 6;
            case 12:
            case 13:
                return 7;
            default:
                if (com.android.internal.hidden_from_bootclasspath.android.hardware.radio.Flags.hdRadioImproved() && idType == 15) {
                    return 4;
                }
                if (idType >= 1000 && idType <= 1999) {
                    return idType;
                }
                return 0;
        }
    }

    private static int[] identifierTypesToProgramTypes(int[] idTypes) {
        java.util.Set<java.lang.Integer> programTypes = new android.util.ArraySet<>();
        for (int i : idTypes) {
            int pType = identifierTypeToProgramType(i);
            if (pType != 0) {
                programTypes.add(java.lang.Integer.valueOf(pType));
                if (pType == 2) {
                    programTypes.add(1);
                }
                if (pType == 4) {
                    programTypes.add(3);
                }
            }
        }
        int i2 = programTypes.size();
        int[] programTypesArray = new int[i2];
        int i3 = 0;
        java.util.Iterator<java.lang.Integer> it = programTypes.iterator();
        while (it.hasNext()) {
            int programType = it.next().intValue();
            programTypesArray[i3] = programType;
            i3++;
        }
        return programTypesArray;
    }

    private static android.hardware.radio.RadioManager.BandDescriptor[] amfmConfigToBands(android.hardware.broadcastradio.AmFmRegionConfig config) {
        if (config == null) {
            return new android.hardware.radio.RadioManager.BandDescriptor[0];
        }
        int len = config.ranges.length;
        java.util.List<android.hardware.radio.RadioManager.BandDescriptor> bands = new java.util.ArrayList<>();
        for (int i = 0; i < len; i++) {
            com.android.server.broadcastradio.aidl.Utils.FrequencyBand bandType = com.android.server.broadcastradio.aidl.Utils.getBand(config.ranges[i].lowerBound);
            if (bandType == com.android.server.broadcastradio.aidl.Utils.FrequencyBand.UNKNOWN) {
                com.android.server.utils.Slogf.e(TAG, "Unknown frequency band at %d kHz", java.lang.Integer.valueOf(config.ranges[i].lowerBound));
            } else if (bandType != com.android.server.broadcastradio.aidl.Utils.FrequencyBand.FM) {
                bands.add(new android.hardware.radio.RadioManager.AmBandDescriptor(0, 0, config.ranges[i].lowerBound, config.ranges[i].upperBound, config.ranges[i].spacing, true));
            } else {
                bands.add(new android.hardware.radio.RadioManager.FmBandDescriptor(0, 1, config.ranges[i].lowerBound, config.ranges[i].upperBound, config.ranges[i].spacing, true, true, true, true, true));
            }
        }
        return (android.hardware.radio.RadioManager.BandDescriptor[]) bands.toArray(new java.util.function.IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i2) {
                return com.android.server.broadcastradio.aidl.ConversionUtils.lambda$amfmConfigToBands$1(i2);
            }
        });
    }

    static /* synthetic */ android.hardware.radio.RadioManager.BandDescriptor[] lambda$amfmConfigToBands$1(int x$0) {
        return new android.hardware.radio.RadioManager.BandDescriptor[x$0];
    }

    private static java.util.Map<java.lang.String, java.lang.Integer> dabConfigFromHalDabTableEntries(android.hardware.broadcastradio.DabTableEntry[] config) {
        if (config == null) {
            return null;
        }
        java.util.Map<java.lang.String, java.lang.Integer> dabConfig = new android.util.ArrayMap<>();
        for (int i = 0; i < config.length; i++) {
            dabConfig.put(config[i].label, java.lang.Integer.valueOf(config[i].frequencyKhz));
        }
        return dabConfig;
    }

    static android.hardware.radio.RadioManager.ModuleProperties propertiesFromHalProperties(int id, java.lang.String serviceName, android.hardware.broadcastradio.Properties prop, android.hardware.broadcastradio.AmFmRegionConfig amfmConfig, android.hardware.broadcastradio.DabTableEntry[] dabConfig) {
        java.util.Objects.requireNonNull(serviceName);
        java.util.Objects.requireNonNull(prop);
        int[] supportedProgramTypes = identifierTypesToProgramTypes(prop.supportedIdentifierTypes);
        return new android.hardware.radio.RadioManager.ModuleProperties(id, serviceName, 0, prop.maker, prop.product, prop.version, prop.serial, 1, 1, false, false, amfmConfigToBands(amfmConfig), true, supportedProgramTypes, prop.supportedIdentifierTypes, dabConfigFromHalDabTableEntries(dabConfig), vendorInfoFromHalVendorKeyValues(prop.vendorInfo));
    }

    static android.hardware.broadcastradio.ProgramIdentifier identifierToHalProgramIdentifier(android.hardware.radio.ProgramSelector.Identifier id) {
        android.hardware.broadcastradio.ProgramIdentifier hwId = new android.hardware.broadcastradio.ProgramIdentifier();
        if (id.getType() == 14) {
            hwId.type = 5;
        } else if (com.android.internal.hidden_from_bootclasspath.android.hardware.radio.Flags.hdRadioImproved() && id.getType() == 15) {
            hwId.type = 14;
        } else {
            hwId.type = id.getType();
        }
        long value = id.getValue();
        if (id.getType() == 5) {
            hwId.value = (65535 & value) | ((value >>> 16) << 32);
        } else {
            hwId.value = value;
        }
        return hwId;
    }

    static android.hardware.radio.ProgramSelector.Identifier identifierFromHalProgramIdentifier(android.hardware.broadcastradio.ProgramIdentifier id) {
        int idType;
        if (id.type == 0) {
            return null;
        }
        if (id.type == 5) {
            idType = 14;
        } else {
            int idType2 = id.type;
            if (idType2 == 14) {
                if (!com.android.internal.hidden_from_bootclasspath.android.hardware.radio.Flags.hdRadioImproved()) {
                    return null;
                }
                idType = 15;
            } else {
                idType = id.type;
            }
        }
        return new android.hardware.radio.ProgramSelector.Identifier(idType, id.value);
    }

    private static boolean isVendorIdentifierType(int idType) {
        return idType >= 1000 && idType <= 1999;
    }

    private static boolean isValidHalProgramSelector(android.hardware.broadcastradio.ProgramSelector sel) {
        return sel.primaryId.type == 1 || sel.primaryId.type == 2 || sel.primaryId.type == 3 || sel.primaryId.type == 5 || sel.primaryId.type == 9 || sel.primaryId.type == 12 || isVendorIdentifierType(sel.primaryId.type);
    }

    static android.hardware.broadcastradio.ProgramSelector programSelectorToHalProgramSelector(android.hardware.radio.ProgramSelector sel) {
        android.hardware.broadcastradio.ProgramSelector hwSel = new android.hardware.broadcastradio.ProgramSelector();
        hwSel.primaryId = identifierToHalProgramIdentifier(sel.getPrimaryId());
        android.hardware.radio.ProgramSelector.Identifier[] secondaryIds = sel.getSecondaryIds();
        java.util.ArrayList<android.hardware.broadcastradio.ProgramIdentifier> secondaryIdList = new java.util.ArrayList<>(secondaryIds.length);
        for (int i = 0; i < secondaryIds.length; i++) {
            android.hardware.broadcastradio.ProgramIdentifier hwId = identifierToHalProgramIdentifier(secondaryIds[i]);
            if (hwId.type != 0) {
                secondaryIdList.add(hwId);
            } else {
                com.android.server.utils.Slogf.w(TAG, "Invalid secondary id: %s", secondaryIds[i]);
            }
        }
        hwSel.secondaryIds = (android.hardware.broadcastradio.ProgramIdentifier[]) secondaryIdList.toArray(new java.util.function.IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i2) {
                return com.android.server.broadcastradio.aidl.ConversionUtils.lambda$programSelectorToHalProgramSelector$2(i2);
            }
        });
        if (!isValidHalProgramSelector(hwSel)) {
            return null;
        }
        return hwSel;
    }

    static /* synthetic */ android.hardware.broadcastradio.ProgramIdentifier[] lambda$programSelectorToHalProgramSelector$2(int x$0) {
        return new android.hardware.broadcastradio.ProgramIdentifier[x$0];
    }

    private static boolean isEmpty(android.hardware.broadcastradio.ProgramSelector sel) {
        return sel.primaryId.type == 0 && sel.primaryId.value == 0 && sel.secondaryIds.length == 0;
    }

    static android.hardware.radio.ProgramSelector programSelectorFromHalProgramSelector(android.hardware.broadcastradio.ProgramSelector sel) {
        if (isEmpty(sel) || !isValidHalProgramSelector(sel)) {
            return null;
        }
        java.util.List<android.hardware.radio.ProgramSelector.Identifier> secondaryIdList = new java.util.ArrayList<>();
        for (int i = 0; i < sel.secondaryIds.length; i++) {
            if (sel.secondaryIds[i] != null) {
                android.hardware.radio.ProgramSelector.Identifier id = identifierFromHalProgramIdentifier(sel.secondaryIds[i]);
                if (id == null) {
                    com.android.server.utils.Slogf.e(TAG, "invalid secondary id: %s", sel.secondaryIds[i]);
                } else {
                    secondaryIdList.add(id);
                }
            }
        }
        return new android.hardware.radio.ProgramSelector(identifierTypeToProgramType(sel.primaryId.type), (android.hardware.radio.ProgramSelector.Identifier) java.util.Objects.requireNonNull(identifierFromHalProgramIdentifier(sel.primaryId)), (android.hardware.radio.ProgramSelector.Identifier[]) secondaryIdList.toArray(new android.hardware.radio.ProgramSelector.Identifier[0]), (long[]) null);
    }

    static android.hardware.radio.RadioMetadata radioMetadataFromHalMetadata(android.hardware.broadcastradio.Metadata[] meta) {
        android.hardware.radio.RadioMetadata.Builder builder = new android.hardware.radio.RadioMetadata.Builder();
        for (int i = 0; i < meta.length; i++) {
            int tag = meta[i].getTag();
            switch (tag) {
                case 0:
                    builder.putString("android.hardware.radio.metadata.RDS_PS", meta[i].getRdsPs());
                    break;
                case 1:
                    builder.putInt("android.hardware.radio.metadata.RDS_PTY", meta[i].getRdsPty());
                    break;
                case 2:
                    builder.putInt("android.hardware.radio.metadata.RBDS_PTY", meta[i].getRbdsPty());
                    break;
                case 3:
                    builder.putString("android.hardware.radio.metadata.RDS_RT", meta[i].getRdsRt());
                    break;
                case 4:
                    builder.putString("android.hardware.radio.metadata.TITLE", meta[i].getSongTitle());
                    break;
                case 5:
                    builder.putString("android.hardware.radio.metadata.ARTIST", meta[i].getSongArtist());
                    break;
                case 6:
                    builder.putString("android.hardware.radio.metadata.ALBUM", meta[i].getSongAlbum());
                    break;
                case 7:
                    builder.putInt("android.hardware.radio.metadata.ICON", meta[i].getStationIcon());
                    break;
                case 8:
                    builder.putInt("android.hardware.radio.metadata.ART", meta[i].getAlbumArt());
                    break;
                case 9:
                    builder.putString("android.hardware.radio.metadata.PROGRAM_NAME", meta[i].getProgramName());
                    break;
                case 10:
                    builder.putString("android.hardware.radio.metadata.DAB_ENSEMBLE_NAME", meta[i].getDabEnsembleName());
                    break;
                case 11:
                    builder.putString("android.hardware.radio.metadata.DAB_ENSEMBLE_NAME_SHORT", meta[i].getDabEnsembleNameShort());
                    break;
                case 12:
                    builder.putString("android.hardware.radio.metadata.DAB_SERVICE_NAME", meta[i].getDabServiceName());
                    break;
                case 13:
                    builder.putString("android.hardware.radio.metadata.DAB_SERVICE_NAME_SHORT", meta[i].getDabServiceNameShort());
                    break;
                case 14:
                    builder.putString("android.hardware.radio.metadata.DAB_COMPONENT_NAME", meta[i].getDabComponentName());
                    break;
                case 15:
                    builder.putString("android.hardware.radio.metadata.DAB_COMPONENT_NAME_SHORT", meta[i].getDabComponentNameShort());
                    break;
                default:
                    if (com.android.internal.hidden_from_bootclasspath.android.hardware.radio.Flags.hdRadioImproved()) {
                        switch (tag) {
                            case 16:
                                builder.putString("android.hardware.radio.metadata.GENRE", meta[i].getGenre());
                                break;
                            case 17:
                                builder.putString("android.hardware.radio.metadata.COMMENT_SHORT_DESCRIPTION", meta[i].getCommentShortDescription());
                                break;
                            case 18:
                                builder.putString("android.hardware.radio.metadata.COMMENT_ACTUAL_TEXT", meta[i].getCommentActualText());
                                break;
                            case 19:
                                builder.putString("android.hardware.radio.metadata.COMMERCIAL", meta[i].getCommercial());
                                break;
                            case 20:
                                builder.putStringArray("android.hardware.radio.metadata.UFIDS", meta[i].getUfids());
                                break;
                            case 21:
                                builder.putString("android.hardware.radio.metadata.HD_STATION_NAME_SHORT", meta[i].getHdStationNameShort());
                                break;
                            case 22:
                                builder.putString("android.hardware.radio.metadata.HD_STATION_NAME_LONG", meta[i].getHdStationNameLong());
                                break;
                            case 23:
                                builder.putInt("android.hardware.radio.metadata.HD_SUBCHANNELS_AVAILABLE", meta[i].getHdSubChannelsAvailable());
                                break;
                            default:
                                com.android.server.utils.Slogf.w(TAG, "Ignored unknown metadata entry: %s with HD radio flag enabled", meta[i]);
                                break;
                        }
                    } else {
                        com.android.server.utils.Slogf.w(TAG, "Ignored unknown metadata entry: %s with HD radio flag disabled", meta[i]);
                        break;
                    }
                    break;
            }
        }
        return builder.build();
    }

    private static boolean isValidLogicallyTunedTo(android.hardware.broadcastradio.ProgramIdentifier id) {
        return id.type == 1 || id.type == 2 || id.type == 3 || id.type == 5 || id.type == 9 || id.type == 12 || isVendorIdentifierType(id.type);
    }

    private static boolean isValidPhysicallyTunedTo(android.hardware.broadcastradio.ProgramIdentifier id) {
        return id.type == 1 || id.type == 8 || id.type == 10 || id.type == 13 || isVendorIdentifierType(id.type);
    }

    private static boolean isValidHalProgramInfo(android.hardware.broadcastradio.ProgramInfo info) {
        return isValidHalProgramSelector(info.selector) && isValidLogicallyTunedTo(info.logicallyTunedTo) && isValidPhysicallyTunedTo(info.physicallyTunedTo);
    }

    static android.hardware.radio.RadioManager.ProgramInfo programInfoFromHalProgramInfo(android.hardware.broadcastradio.ProgramInfo info) {
        if (!isValidHalProgramInfo(info)) {
            return null;
        }
        java.util.Collection<android.hardware.radio.ProgramSelector.Identifier> relatedContent = new java.util.ArrayList<>();
        if (info.relatedContent != null) {
            for (int i = 0; i < info.relatedContent.length; i++) {
                android.hardware.radio.ProgramSelector.Identifier relatedContentId = identifierFromHalProgramIdentifier(info.relatedContent[i]);
                if (relatedContentId != null) {
                    relatedContent.add(relatedContentId);
                }
            }
        }
        return new android.hardware.radio.RadioManager.ProgramInfo((android.hardware.radio.ProgramSelector) java.util.Objects.requireNonNull(programSelectorFromHalProgramSelector(info.selector)), identifierFromHalProgramIdentifier(info.logicallyTunedTo), identifierFromHalProgramIdentifier(info.physicallyTunedTo), relatedContent, info.infoFlags, info.signalQuality, radioMetadataFromHalMetadata(info.metadata), vendorInfoFromHalVendorKeyValues(info.vendorInfo));
    }

    static android.hardware.broadcastradio.ProgramFilter filterToHalProgramFilter(android.hardware.radio.ProgramList.Filter filter) {
        if (filter == null) {
            filter = new android.hardware.radio.ProgramList.Filter();
        }
        android.hardware.broadcastradio.ProgramFilter hwFilter = new android.hardware.broadcastradio.ProgramFilter();
        android.util.IntArray identifierTypeList = new android.util.IntArray(filter.getIdentifierTypes().size());
        java.util.ArrayList<android.hardware.broadcastradio.ProgramIdentifier> identifiersList = new java.util.ArrayList<>();
        java.util.Iterator<java.lang.Integer> typeIterator = filter.getIdentifierTypes().iterator();
        while (typeIterator.hasNext()) {
            identifierTypeList.add(typeIterator.next().intValue());
        }
        for (android.hardware.radio.ProgramSelector.Identifier id : filter.getIdentifiers()) {
            android.hardware.broadcastradio.ProgramIdentifier hwId = identifierToHalProgramIdentifier(id);
            if (hwId.type != 0) {
                identifiersList.add(hwId);
            } else {
                com.android.server.utils.Slogf.w(TAG, "Invalid identifiers: %s", id);
            }
        }
        hwFilter.identifierTypes = identifierTypeList.toArray();
        hwFilter.identifiers = (android.hardware.broadcastradio.ProgramIdentifier[]) identifiersList.toArray(new java.util.function.IntFunction() { // from class: com.android.server.broadcastradio.aidl.ConversionUtils$$ExternalSyntheticLambda1
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.broadcastradio.aidl.ConversionUtils.lambda$filterToHalProgramFilter$3(i);
            }
        });
        hwFilter.includeCategories = filter.areCategoriesIncluded();
        hwFilter.excludeModifications = filter.areModificationsExcluded();
        return hwFilter;
    }

    static /* synthetic */ android.hardware.broadcastradio.ProgramIdentifier[] lambda$filterToHalProgramFilter$3(int x$0) {
        return new android.hardware.broadcastradio.ProgramIdentifier[x$0];
    }

    private static boolean identifierMeetsSdkVersionRequirement(android.hardware.radio.ProgramSelector.Identifier id, int uid) {
        if (com.android.internal.hidden_from_bootclasspath.android.hardware.radio.Flags.hdRadioImproved() && !isAtLeastV(uid) && id.getType() == 15) {
            return false;
        }
        return isAtLeastU(uid) || id.getType() != 14;
    }

    static boolean programSelectorMeetsSdkVersionRequirement(android.hardware.radio.ProgramSelector sel, int uid) {
        if (!identifierMeetsSdkVersionRequirement(sel.getPrimaryId(), uid)) {
            return false;
        }
        android.hardware.radio.ProgramSelector.Identifier[] secondaryIds = sel.getSecondaryIds();
        for (android.hardware.radio.ProgramSelector.Identifier identifier : secondaryIds) {
            if (!identifierMeetsSdkVersionRequirement(identifier, uid)) {
                return false;
            }
        }
        return true;
    }

    static boolean programInfoMeetsSdkVersionRequirement(android.hardware.radio.RadioManager.ProgramInfo info, int uid) {
        if (!programSelectorMeetsSdkVersionRequirement(info.getSelector(), uid) || !identifierMeetsSdkVersionRequirement(info.getLogicallyTunedTo(), uid) || !identifierMeetsSdkVersionRequirement(info.getPhysicallyTunedTo(), uid)) {
            return false;
        }
        java.util.Iterator<android.hardware.radio.ProgramSelector.Identifier> relatedContentIt = info.getRelatedContent().iterator();
        while (relatedContentIt.hasNext()) {
            if (!identifierMeetsSdkVersionRequirement(relatedContentIt.next(), uid)) {
                return false;
            }
        }
        return true;
    }

    static android.hardware.radio.ProgramList.Chunk convertChunkToTargetSdkVersion(android.hardware.radio.ProgramList.Chunk chunk, int uid) {
        java.util.Set<android.hardware.radio.RadioManager.ProgramInfo> modified = new android.util.ArraySet<>();
        for (android.hardware.radio.RadioManager.ProgramInfo info : chunk.getModified()) {
            if (programInfoMeetsSdkVersionRequirement(info, uid)) {
                modified.add(info);
            }
        }
        java.util.Set<android.hardware.radio.UniqueProgramIdentifier> removed = new android.util.ArraySet<>();
        for (android.hardware.radio.UniqueProgramIdentifier id : chunk.getRemoved()) {
            if (identifierMeetsSdkVersionRequirement(id.getPrimaryId(), uid)) {
                removed.add(id);
            }
        }
        return new android.hardware.radio.ProgramList.Chunk(chunk.isPurge(), chunk.isComplete(), modified, removed);
    }

    static boolean configFlagMeetsSdkVersionRequirement(int configFlag, int uid) {
        if (com.android.internal.hidden_from_bootclasspath.android.hardware.radio.Flags.hdRadioImproved() && isAtLeastV(uid)) {
            return true;
        }
        return (configFlag == 11 || configFlag == 10) ? false : true;
    }

    public static android.hardware.radio.Announcement announcementFromHalAnnouncement(android.hardware.broadcastradio.Announcement hwAnnouncement) {
        return new android.hardware.radio.Announcement((android.hardware.radio.ProgramSelector) java.util.Objects.requireNonNull(programSelectorFromHalProgramSelector(hwAnnouncement.selector), "Program selector can not be null"), hwAnnouncement.type, vendorInfoFromHalVendorKeyValues(hwAnnouncement.vendorInfo));
    }
}
