package com.android.server.broadcastradio.hal2;

/* JADX INFO: loaded from: classes.dex */
final class Convert {
    private static final android.util.SparseArray<com.android.server.broadcastradio.hal2.Convert.MetadataDef> METADATA_KEYS = new android.util.SparseArray<>();
    private static final java.lang.String TAG = "BcRadio2Srv.convert";

    private enum MetadataType {
        INT,
        STRING
    }

    private Convert() {
        throw new java.lang.UnsupportedOperationException("Convert class is noninstantiable");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    static void throwOnError(java.lang.String action, int result) throws android.os.ParcelableException {
        java.lang.String errorString = action + ": " + android.hardware.broadcastradio.V2_0.Result.toString(result);
        switch (result) {
            case 0:
                return;
            case 1:
            case 2:
            case 6:
                throw new android.os.ParcelableException(new java.lang.RuntimeException(errorString));
            case 3:
                throw new java.lang.IllegalArgumentException(errorString);
            case 4:
                throw new java.lang.IllegalStateException(errorString);
            case 5:
                throw new java.lang.UnsupportedOperationException(errorString);
            default:
                throw new android.os.ParcelableException(new java.lang.RuntimeException(action + ": unknown error (" + result + ")"));
        }
    }

    static int halResultToTunerResult(int result) {
        switch (result) {
            case 0:
                return 0;
            case 1:
            default:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
        }
    }

    static java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> vendorInfoToHal(java.util.Map<java.lang.String, java.lang.String> info) {
        if (info == null) {
            return new java.util.ArrayList<>();
        }
        java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> list = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : info.entrySet()) {
            android.hardware.broadcastradio.V2_0.VendorKeyValue elem = new android.hardware.broadcastradio.V2_0.VendorKeyValue();
            elem.key = entry.getKey();
            elem.value = entry.getValue();
            if (elem.key == null || elem.value == null) {
                com.android.server.utils.Slogf.w(TAG, "VendorKeyValue contains null pointers");
            } else {
                list.add(elem);
            }
        }
        return list;
    }

    static java.util.Map<java.lang.String, java.lang.String> vendorInfoFromHal(java.util.List<android.hardware.broadcastradio.V2_0.VendorKeyValue> info) {
        java.util.Map<java.lang.String, java.lang.String> vendorInfoMap = new android.util.ArrayMap<>();
        if (info == null) {
            return vendorInfoMap;
        }
        for (android.hardware.broadcastradio.V2_0.VendorKeyValue kvp : info) {
            if (kvp.key == null || kvp.value == null) {
                com.android.server.utils.Slogf.w(TAG, "VendorKeyValue contains null pointers");
            } else {
                vendorInfoMap.put(kvp.key, kvp.value);
            }
        }
        return vendorInfoMap;
    }

    private static int identifierTypeToProgramType(int idType) {
        switch (idType) {
            case 1:
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
            case 11:
            default:
                if (idType >= 1000 && idType <= 1999) {
                    return idType;
                }
                return 0;
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
        }
    }

    private static int[] identifierTypesToProgramTypes(int[] idTypes) {
        java.util.Set<java.lang.Integer> pTypes = new android.util.ArraySet<>();
        for (int idType : idTypes) {
            int pType = identifierTypeToProgramType(idType);
            if (pType != 0) {
                pTypes.add(java.lang.Integer.valueOf(pType));
                if (pType == 2) {
                    pTypes.add(1);
                }
                if (pType == 4) {
                    pTypes.add(3);
                }
            }
        }
        return pTypes.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
    }

    private static android.hardware.radio.RadioManager.BandDescriptor[] amfmConfigToBands(android.hardware.broadcastradio.V2_0.AmFmRegionConfig config) {
        if (config == null) {
            return new android.hardware.radio.RadioManager.BandDescriptor[0];
        }
        int len = config.ranges.size();
        java.util.List<android.hardware.radio.RadioManager.BandDescriptor> bands = new java.util.ArrayList<>(len);
        for (android.hardware.broadcastradio.V2_0.AmFmBandRange range : config.ranges) {
            com.android.server.broadcastradio.hal2.FrequencyBand bandType = com.android.server.broadcastradio.hal2.Utils.getBand(range.lowerBound);
            if (bandType == com.android.server.broadcastradio.hal2.FrequencyBand.UNKNOWN) {
                com.android.server.utils.Slogf.e(TAG, "Unknown frequency band at " + range.lowerBound + "kHz");
            } else if (bandType != com.android.server.broadcastradio.hal2.FrequencyBand.FM) {
                bands.add(new android.hardware.radio.RadioManager.AmBandDescriptor(0, 0, range.lowerBound, range.upperBound, range.spacing, true));
            } else {
                bands.add(new android.hardware.radio.RadioManager.FmBandDescriptor(0, 1, range.lowerBound, range.upperBound, range.spacing, true, true, true, true, true));
            }
        }
        return (android.hardware.radio.RadioManager.BandDescriptor[]) bands.toArray(new android.hardware.radio.RadioManager.BandDescriptor[bands.size()]);
    }

    private static java.util.Map<java.lang.String, java.lang.Integer> dabConfigFromHal(java.util.List<android.hardware.broadcastradio.V2_0.DabTableEntry> config) {
        if (config == null) {
            return null;
        }
        return (java.util.Map) config.stream().collect(java.util.stream.Collectors.toMap(new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.hardware.broadcastradio.V2_0.DabTableEntry) obj).label;
            }
        }, new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((android.hardware.broadcastradio.V2_0.DabTableEntry) obj).frequency);
            }
        }));
    }

    static android.hardware.radio.RadioManager.ModuleProperties propertiesFromHal(int id, java.lang.String serviceName, android.hardware.broadcastradio.V2_0.Properties prop, android.hardware.broadcastradio.V2_0.AmFmRegionConfig amfmConfig, java.util.List<android.hardware.broadcastradio.V2_0.DabTableEntry> dabConfig) {
        java.util.Objects.requireNonNull(serviceName);
        java.util.Objects.requireNonNull(prop);
        int[] supportedIdentifierTypes = prop.supportedIdentifierTypes.stream().mapToInt(new com.android.server.audio.AudioService$$ExternalSyntheticLambda0()).toArray();
        int[] supportedProgramTypes = identifierTypesToProgramTypes(supportedIdentifierTypes);
        return new android.hardware.radio.RadioManager.ModuleProperties(id, serviceName, 0, prop.maker, prop.product, prop.version, prop.serial, 1, 1, false, false, amfmConfigToBands(amfmConfig), true, supportedProgramTypes, supportedIdentifierTypes, dabConfigFromHal(dabConfig), vendorInfoFromHal(prop.vendorInfo));
    }

    static void programIdentifierToHal(android.hardware.broadcastradio.V2_0.ProgramIdentifier hwId, android.hardware.radio.ProgramSelector.Identifier id) {
        hwId.type = id.getType();
        hwId.value = id.getValue();
    }

    static android.hardware.broadcastradio.V2_0.ProgramIdentifier programIdentifierToHal(android.hardware.radio.ProgramSelector.Identifier id) {
        android.hardware.broadcastradio.V2_0.ProgramIdentifier hwId = new android.hardware.broadcastradio.V2_0.ProgramIdentifier();
        programIdentifierToHal(hwId, id);
        return hwId;
    }

    static android.hardware.radio.ProgramSelector.Identifier programIdentifierFromHal(android.hardware.broadcastradio.V2_0.ProgramIdentifier id) {
        if (id.type == 0) {
            return null;
        }
        return new android.hardware.radio.ProgramSelector.Identifier(id.type, id.value);
    }

    static android.hardware.broadcastradio.V2_0.ProgramSelector programSelectorToHal(android.hardware.radio.ProgramSelector sel) {
        android.hardware.broadcastradio.V2_0.ProgramSelector hwSel = new android.hardware.broadcastradio.V2_0.ProgramSelector();
        programIdentifierToHal(hwSel.primaryId, sel.getPrimaryId());
        java.util.stream.Stream map = java.util.Arrays.stream(sel.getSecondaryIds()).map(new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.broadcastradio.hal2.Convert.programIdentifierToHal((android.hardware.radio.ProgramSelector.Identifier) obj);
            }
        });
        final java.util.ArrayList<android.hardware.broadcastradio.V2_0.ProgramIdentifier> arrayList = hwSel.secondaryIds;
        java.util.Objects.requireNonNull(arrayList);
        map.forEachOrdered(new java.util.function.Consumer() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                arrayList.add((android.hardware.broadcastradio.V2_0.ProgramIdentifier) obj);
            }
        });
        return hwSel;
    }

    private static boolean isEmpty(android.hardware.broadcastradio.V2_0.ProgramSelector sel) {
        return sel.primaryId.type == 0 && sel.primaryId.value == 0 && sel.secondaryIds.isEmpty();
    }

    static android.hardware.radio.ProgramSelector programSelectorFromHal(android.hardware.broadcastradio.V2_0.ProgramSelector sel) {
        if (isEmpty(sel)) {
            return null;
        }
        android.hardware.radio.ProgramSelector.Identifier[] secondaryIds = (android.hardware.radio.ProgramSelector.Identifier[]) sel.secondaryIds.stream().map(new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.broadcastradio.hal2.Convert.programIdentifierFromHal((android.hardware.broadcastradio.V2_0.ProgramIdentifier) obj);
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return (android.hardware.radio.ProgramSelector.Identifier) java.util.Objects.requireNonNull((android.hardware.radio.ProgramSelector.Identifier) obj);
            }
        }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda2
            @Override // java.util.function.IntFunction
            public final java.lang.Object apply(int i) {
                return com.android.server.broadcastradio.hal2.Convert.lambda$programSelectorFromHal$2(i);
            }
        });
        return new android.hardware.radio.ProgramSelector(identifierTypeToProgramType(sel.primaryId.type), (android.hardware.radio.ProgramSelector.Identifier) java.util.Objects.requireNonNull(programIdentifierFromHal(sel.primaryId)), secondaryIds, (long[]) null);
    }

    static /* synthetic */ android.hardware.radio.ProgramSelector.Identifier[] lambda$programSelectorFromHal$2(int x$0) {
        return new android.hardware.radio.ProgramSelector.Identifier[x$0];
    }

    private static class MetadataDef {
        private java.lang.String key;
        private com.android.server.broadcastradio.hal2.Convert.MetadataType type;

        private MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType type, java.lang.String key) {
            this.type = type;
            this.key = key;
        }
    }

    static {
        METADATA_KEYS.put(1, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.RDS_PS"));
        METADATA_KEYS.put(2, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.INT, "android.hardware.radio.metadata.RDS_PTY"));
        METADATA_KEYS.put(3, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.INT, "android.hardware.radio.metadata.RBDS_PTY"));
        METADATA_KEYS.put(4, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.RDS_RT"));
        METADATA_KEYS.put(5, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.TITLE"));
        METADATA_KEYS.put(6, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.ARTIST"));
        METADATA_KEYS.put(7, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.ALBUM"));
        METADATA_KEYS.put(8, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.INT, "android.hardware.radio.metadata.ICON"));
        METADATA_KEYS.put(9, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.INT, "android.hardware.radio.metadata.ART"));
        METADATA_KEYS.put(10, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.PROGRAM_NAME"));
        METADATA_KEYS.put(11, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.DAB_ENSEMBLE_NAME"));
        METADATA_KEYS.put(12, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.DAB_ENSEMBLE_NAME_SHORT"));
        METADATA_KEYS.put(13, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.DAB_SERVICE_NAME"));
        METADATA_KEYS.put(14, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.DAB_SERVICE_NAME_SHORT"));
        METADATA_KEYS.put(15, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.DAB_COMPONENT_NAME"));
        METADATA_KEYS.put(16, new com.android.server.broadcastradio.hal2.Convert.MetadataDef(com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING, "android.hardware.radio.metadata.DAB_COMPONENT_NAME_SHORT"));
    }

    private static android.hardware.radio.RadioMetadata metadataFromHal(java.util.ArrayList<android.hardware.broadcastradio.V2_0.Metadata> meta) {
        android.hardware.radio.RadioMetadata.Builder builder = new android.hardware.radio.RadioMetadata.Builder();
        for (android.hardware.broadcastradio.V2_0.Metadata entry : meta) {
            com.android.server.broadcastradio.hal2.Convert.MetadataDef keyDef = METADATA_KEYS.get(entry.key);
            if (keyDef == null) {
                com.android.server.utils.Slogf.i(TAG, "Ignored unknown metadata entry: " + android.hardware.broadcastradio.V2_0.MetadataKey.toString(entry.key));
            } else if (keyDef.type == com.android.server.broadcastradio.hal2.Convert.MetadataType.STRING) {
                builder.putString(keyDef.key, entry.stringValue);
            } else {
                builder.putInt(keyDef.key, (int) entry.intValue);
            }
        }
        return builder.build();
    }

    static android.hardware.radio.RadioManager.ProgramInfo programInfoFromHal(android.hardware.broadcastradio.V2_0.ProgramInfo info) {
        java.util.Collection<android.hardware.radio.ProgramSelector.Identifier> relatedContent = (java.util.Collection) info.relatedContent.stream().map(new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.broadcastradio.hal2.Convert.lambda$programInfoFromHal$3((android.hardware.broadcastradio.V2_0.ProgramIdentifier) obj);
            }
        }).collect(java.util.stream.Collectors.toList());
        return new android.hardware.radio.RadioManager.ProgramInfo((android.hardware.radio.ProgramSelector) java.util.Objects.requireNonNull(programSelectorFromHal(info.selector)), programIdentifierFromHal(info.logicallyTunedTo), programIdentifierFromHal(info.physicallyTunedTo), relatedContent, info.infoFlags, info.signalQuality, metadataFromHal(info.metadata), vendorInfoFromHal(info.vendorInfo));
    }

    static /* synthetic */ android.hardware.radio.ProgramSelector.Identifier lambda$programInfoFromHal$3(android.hardware.broadcastradio.V2_0.ProgramIdentifier id) {
        return (android.hardware.radio.ProgramSelector.Identifier) java.util.Objects.requireNonNull(programIdentifierFromHal(id));
    }

    static android.hardware.broadcastradio.V2_0.ProgramFilter programFilterToHal(android.hardware.radio.ProgramList.Filter filter) {
        if (filter == null) {
            filter = new android.hardware.radio.ProgramList.Filter();
        }
        final android.hardware.broadcastradio.V2_0.ProgramFilter hwFilter = new android.hardware.broadcastradio.V2_0.ProgramFilter();
        java.util.stream.Stream stream = filter.getIdentifierTypes().stream();
        final java.util.ArrayList<java.lang.Integer> arrayList = hwFilter.identifierTypes;
        java.util.Objects.requireNonNull(arrayList);
        stream.forEachOrdered(new java.util.function.Consumer() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                arrayList.add((java.lang.Integer) obj);
            }
        });
        filter.getIdentifiers().stream().forEachOrdered(new java.util.function.Consumer() { // from class: com.android.server.broadcastradio.hal2.Convert$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                hwFilter.identifiers.add(com.android.server.broadcastradio.hal2.Convert.programIdentifierToHal((android.hardware.radio.ProgramSelector.Identifier) obj));
            }
        });
        hwFilter.includeCategories = filter.areCategoriesIncluded();
        hwFilter.excludeModifications = filter.areModificationsExcluded();
        return hwFilter;
    }

    public static android.hardware.radio.Announcement announcementFromHal(android.hardware.broadcastradio.V2_0.Announcement hwAnnouncement) {
        return new android.hardware.radio.Announcement((android.hardware.radio.ProgramSelector) java.util.Objects.requireNonNull(programSelectorFromHal(hwAnnouncement.selector)), hwAnnouncement.type, vendorInfoFromHal(hwAnnouncement.vendorInfo));
    }

    static <T> java.util.ArrayList<T> listToArrayList(java.util.List<T> list) {
        if (list == null) {
            return null;
        }
        return list instanceof java.util.ArrayList ? (java.util.ArrayList) list : new java.util.ArrayList<>(list);
    }
}
