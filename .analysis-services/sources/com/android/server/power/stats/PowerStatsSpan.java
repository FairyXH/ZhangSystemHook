package com.android.server.power.stats;

/* JADX INFO: loaded from: classes3.dex */
public class PowerStatsSpan {
    private static final java.time.format.DateTimeFormatter DATE_FORMAT = java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS").withZone(java.time.ZoneId.systemDefault());
    private static final java.lang.String TAG = "PowerStatsStore";
    private static final int VERSION = 2;
    private static final java.lang.String XML_ATTR_DURATION = "duration";
    private static final java.lang.String XML_ATTR_ID = "id";
    private static final java.lang.String XML_ATTR_MONOTONIC = "monotonic";
    private static final java.lang.String XML_ATTR_SECTION_TYPE = "type";
    private static final java.lang.String XML_ATTR_START_TIME = "start";
    private static final java.lang.String XML_ATTR_VERSION = "version";
    private static final java.lang.String XML_TAG_METADATA = "metadata";
    private static final java.lang.String XML_TAG_SECTION = "section";
    private static final java.lang.String XML_TAG_TIMEFRAME = "timeframe";
    private final com.android.server.power.stats.PowerStatsSpan.Metadata mMetadata;
    private final java.util.List<com.android.server.power.stats.PowerStatsSpan.Section> mSections;

    public interface SectionReader {
        com.android.server.power.stats.PowerStatsSpan.Section read(java.lang.String str, com.android.modules.utils.TypedXmlPullParser typedXmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;
    }

    static class TimeFrame {
        public final long duration;
        public final long startMonotonicTime;
        public final long startTime;

        TimeFrame(long startMonotonicTime, long startTime, long duration) {
            this.startMonotonicTime = startMonotonicTime;
            this.startTime = startTime;
            this.duration = duration;
        }

        void write(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.startTag((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_TAG_TIMEFRAME);
            serializer.attributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_START_TIME, this.startTime);
            serializer.attributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_MONOTONIC, this.startMonotonicTime);
            serializer.attributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_DURATION, this.duration);
            serializer.endTag((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_TAG_TIMEFRAME);
        }

        static com.android.server.power.stats.PowerStatsSpan.TimeFrame read(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
            return new com.android.server.power.stats.PowerStatsSpan.TimeFrame(parser.getAttributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_MONOTONIC), parser.getAttributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_START_TIME), parser.getAttributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_DURATION));
        }

        public void dump(android.util.IndentingPrintWriter pw) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append(com.android.server.power.stats.PowerStatsSpan.DATE_FORMAT.format(java.time.Instant.ofEpochMilli(this.startTime))).append(" (monotonic=").append(this.startMonotonicTime).append(") ").append(" duration=");
            java.lang.String durationString = android.util.TimeUtils.formatDuration(this.duration);
            if (durationString.startsWith("+")) {
                sb.append(durationString.substring(1));
            } else {
                sb.append(durationString);
            }
            pw.print(sb);
        }
    }

    static class Metadata {
        static final java.util.Comparator<com.android.server.power.stats.PowerStatsSpan.Metadata> COMPARATOR = java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.power.stats.PowerStatsSpan$Metadata$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Long.valueOf(((com.android.server.power.stats.PowerStatsSpan.Metadata) obj).getId());
            }
        });
        private final long mId;
        private final java.util.List<com.android.server.power.stats.PowerStatsSpan.TimeFrame> mTimeFrames = new java.util.ArrayList();
        private final java.util.List<java.lang.String> mSections = new java.util.ArrayList();

        Metadata(long id) {
            this.mId = id;
        }

        public long getId() {
            return this.mId;
        }

        public java.util.List<com.android.server.power.stats.PowerStatsSpan.TimeFrame> getTimeFrames() {
            return this.mTimeFrames;
        }

        public java.util.List<java.lang.String> getSections() {
            return this.mSections;
        }

        void addTimeFrame(com.android.server.power.stats.PowerStatsSpan.TimeFrame timeFrame) {
            this.mTimeFrames.add(timeFrame);
        }

        void addSection(java.lang.String sectionType) {
            if (!this.mSections.contains(sectionType)) {
                this.mSections.add(sectionType);
            }
        }

        void write(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            serializer.startTag((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_TAG_METADATA);
            serializer.attributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_ID, this.mId);
            serializer.attributeInt((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_VERSION, 2);
            for (com.android.server.power.stats.PowerStatsSpan.TimeFrame timeFrame : this.mTimeFrames) {
                timeFrame.write(serializer);
            }
            for (java.lang.String section : this.mSections) {
                serializer.startTag((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_TAG_SECTION);
                serializer.attribute((java.lang.String) null, "type", section);
                serializer.endTag((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_TAG_SECTION);
            }
            serializer.endTag((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_TAG_METADATA);
        }

        public static com.android.server.power.stats.PowerStatsSpan.Metadata read(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            com.android.server.power.stats.PowerStatsSpan.Metadata metadata = null;
            int eventType = parser.getEventType();
            while (eventType != 1 && (eventType != 3 || !parser.getName().equals(com.android.server.power.stats.PowerStatsSpan.XML_TAG_METADATA))) {
                if (eventType == 2) {
                    java.lang.String tagName = parser.getName();
                    if (tagName.equals(com.android.server.power.stats.PowerStatsSpan.XML_TAG_METADATA)) {
                        int version = parser.getAttributeInt((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_VERSION);
                        if (!com.android.server.power.stats.PowerStatsSpan.isCompatibleXmlFormat(version)) {
                            android.util.Slog.e(com.android.server.power.stats.PowerStatsSpan.TAG, "Incompatible version " + version + "; expected 2");
                            return null;
                        }
                        long id = parser.getAttributeLong((java.lang.String) null, com.android.server.power.stats.PowerStatsSpan.XML_ATTR_ID);
                        metadata = new com.android.server.power.stats.PowerStatsSpan.Metadata(id);
                    } else if (metadata != null && tagName.equals(com.android.server.power.stats.PowerStatsSpan.XML_TAG_TIMEFRAME)) {
                        metadata.addTimeFrame(com.android.server.power.stats.PowerStatsSpan.TimeFrame.read(parser));
                    } else if (metadata != null && tagName.equals(com.android.server.power.stats.PowerStatsSpan.XML_TAG_SECTION)) {
                        metadata.addSection(parser.getAttributeValue((java.lang.String) null, "type"));
                    }
                }
                eventType = parser.next();
            }
            return metadata;
        }

        public void dump(android.util.IndentingPrintWriter pw) {
            dump(pw, true);
        }

        void dump(android.util.IndentingPrintWriter pw, boolean includeSections) {
            pw.print("Span ");
            if (this.mTimeFrames.size() > 0) {
                this.mTimeFrames.get(0).dump(pw);
                pw.println();
            }
            for (int i = 1; i < this.mTimeFrames.size(); i++) {
                com.android.server.power.stats.PowerStatsSpan.TimeFrame timeFrame = this.mTimeFrames.get(i);
                pw.print("     ");
                timeFrame.dump(pw);
                pw.println();
            }
            if (includeSections) {
                pw.increaseIndent();
                for (java.lang.String section : this.mSections) {
                    pw.print(com.android.server.power.stats.PowerStatsSpan.XML_TAG_SECTION, section);
                    pw.println();
                }
                pw.decreaseIndent();
            }
        }

        public java.lang.String toString() {
            java.io.StringWriter sw = new java.io.StringWriter();
            android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(sw);
            ipw.print(com.android.server.power.stats.PowerStatsSpan.XML_ATTR_ID, java.lang.Long.valueOf(this.mId));
            for (int i = 0; i < this.mTimeFrames.size(); i++) {
                com.android.server.power.stats.PowerStatsSpan.TimeFrame timeFrame = this.mTimeFrames.get(i);
                ipw.print("timeframe=[");
                timeFrame.dump(ipw);
                ipw.print("] ");
            }
            for (java.lang.String section : this.mSections) {
                ipw.print(com.android.server.power.stats.PowerStatsSpan.XML_TAG_SECTION, section);
            }
            ipw.flush();
            return sw.toString().trim();
        }
    }

    public static abstract class Section {
        private final java.lang.String mType;

        abstract void write(com.android.modules.utils.TypedXmlSerializer typedXmlSerializer) throws java.io.IOException;

        Section(java.lang.String type) {
            this.mType = type;
        }

        public java.lang.String getType() {
            return this.mType;
        }

        public void dump(android.util.IndentingPrintWriter ipw) {
            ipw.println(this.mType);
        }
    }

    public PowerStatsSpan(long id) {
        this(new com.android.server.power.stats.PowerStatsSpan.Metadata(id));
    }

    private PowerStatsSpan(com.android.server.power.stats.PowerStatsSpan.Metadata metadata) {
        this.mSections = new java.util.ArrayList();
        this.mMetadata = metadata;
    }

    public com.android.server.power.stats.PowerStatsSpan.Metadata getMetadata() {
        return this.mMetadata;
    }

    public long getId() {
        return this.mMetadata.mId;
    }

    void addTimeFrame(long monotonicTime, long wallClockTime, long duration) {
        this.mMetadata.mTimeFrames.add(new com.android.server.power.stats.PowerStatsSpan.TimeFrame(monotonicTime, wallClockTime, duration));
    }

    void addSection(com.android.server.power.stats.PowerStatsSpan.Section section) {
        this.mMetadata.addSection(section.getType());
        this.mSections.add(section);
    }

    public java.util.List<com.android.server.power.stats.PowerStatsSpan.Section> getSections() {
        return this.mSections;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isCompatibleXmlFormat(int version) {
        return version == 2;
    }

    public void writeXml(java.io.OutputStream out, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.setOutput(out, java.nio.charset.StandardCharsets.UTF_8.name());
        serializer.startDocument((java.lang.String) null, true);
        this.mMetadata.write(serializer);
        for (com.android.server.power.stats.PowerStatsSpan.Section section : this.mSections) {
            serializer.startTag((java.lang.String) null, XML_TAG_SECTION);
            serializer.attribute((java.lang.String) null, "type", section.mType);
            section.write(serializer);
            serializer.endTag((java.lang.String) null, XML_TAG_SECTION);
        }
        serializer.endDocument();
    }

    static com.android.server.power.stats.PowerStatsSpan read(java.io.InputStream in, com.android.modules.utils.TypedXmlPullParser parser, com.android.server.power.stats.PowerStatsSpan.SectionReader sectionReader, java.lang.String... sectionTypes) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.Set<java.lang.String> neededSections = com.google.android.collect.Sets.newArraySet(sectionTypes);
        boolean selectSections = !neededSections.isEmpty();
        parser.setInput(in, java.nio.charset.StandardCharsets.UTF_8.name());
        com.android.server.power.stats.PowerStatsSpan.Metadata metadata = com.android.server.power.stats.PowerStatsSpan.Metadata.read(parser);
        if (metadata == null) {
            return null;
        }
        com.android.server.power.stats.PowerStatsSpan span = new com.android.server.power.stats.PowerStatsSpan(metadata);
        boolean skipSection = false;
        int nestingLevel = 0;
        int eventType = parser.getEventType();
        while (eventType != 1) {
            if (skipSection) {
                if (eventType == 3 && parser.getName().equals(XML_TAG_SECTION)) {
                    nestingLevel--;
                    if (nestingLevel == 0) {
                        skipSection = false;
                    }
                } else if (eventType == 2 && parser.getName().equals(XML_TAG_SECTION)) {
                    nestingLevel++;
                }
            } else if (eventType == 2) {
                java.lang.String tag = parser.getName();
                if (tag.equals(XML_TAG_SECTION)) {
                    final java.lang.String sectionType = parser.getAttributeValue((java.lang.String) null, "type");
                    if (!selectSections || neededSections.contains(sectionType)) {
                        com.android.server.power.stats.PowerStatsSpan.Section section = sectionReader.read(sectionType, parser);
                        if (section == null) {
                            if (selectSections) {
                                throw new org.xmlpull.v1.XmlPullParserException("Unsupported PowerStatsStore section type: " + sectionType);
                            }
                            section = new com.android.server.power.stats.PowerStatsSpan.Section(sectionType) { // from class: com.android.server.power.stats.PowerStatsSpan.1
                                @Override // com.android.server.power.stats.PowerStatsSpan.Section
                                public void dump(android.util.IndentingPrintWriter ipw) {
                                    ipw.println("Unsupported PowerStatsStore section type: " + sectionType);
                                }

                                @Override // com.android.server.power.stats.PowerStatsSpan.Section
                                void write(com.android.modules.utils.TypedXmlSerializer serializer) {
                                }
                            };
                        }
                        span.addSection(section);
                    } else {
                        skipSection = true;
                    }
                } else if (tag.equals(XML_TAG_METADATA)) {
                    com.android.server.power.stats.PowerStatsSpan.Metadata.read(parser);
                }
            }
            eventType = parser.next();
        }
        return span;
    }

    public void dump(android.util.IndentingPrintWriter ipw) {
        this.mMetadata.dump(ipw, false);
        for (com.android.server.power.stats.PowerStatsSpan.Section section : this.mSections) {
            ipw.increaseIndent();
            ipw.println(section.mType);
            section.dump(ipw);
            ipw.decreaseIndent();
        }
    }
}
