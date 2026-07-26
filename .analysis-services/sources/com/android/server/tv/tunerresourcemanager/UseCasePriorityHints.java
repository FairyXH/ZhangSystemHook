package com.android.server.tv.tunerresourcemanager;

/* JADX INFO: loaded from: classes3.dex */
public class UseCasePriorityHints {
    private static final int INVALID_PRIORITY_VALUE = -1;
    private static final int INVALID_USE_CASE = -1;
    private static final java.lang.String PATH_TO_VENDOR_CONFIG_XML = "/vendor/etc/tunerResourceManagerUseCaseConfig.xml";
    private static final java.lang.String TAG = "UseCasePriorityHints";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.lang.String NS = null;
    android.util.SparseArray<int[]> mPriorityHints = new android.util.SparseArray<>();
    java.util.Set<java.lang.Integer> mVendorDefinedUseCase = new java.util.HashSet();
    private int mDefaultForeground = 150;
    private int mDefaultBackground = 50;

    int getForegroundPriority(int useCase) {
        if (this.mPriorityHints.get(useCase) != null && this.mPriorityHints.get(useCase).length == 2) {
            return this.mPriorityHints.get(useCase)[0];
        }
        return this.mDefaultForeground;
    }

    int getBackgroundPriority(int useCase) {
        if (this.mPriorityHints.get(useCase) != null && this.mPriorityHints.get(useCase).length == 2) {
            return this.mPriorityHints.get(useCase)[1];
        }
        return this.mDefaultBackground;
    }

    boolean isDefinedUseCase(int useCase) {
        return this.mVendorDefinedUseCase.contains(java.lang.Integer.valueOf(useCase)) || isPredefinedUseCase(useCase);
    }

    public void parse() throws java.lang.Exception {
        java.io.File file = new java.io.File(PATH_TO_VENDOR_CONFIG_XML);
        if (file.exists()) {
            try {
                java.io.InputStream in = new java.io.FileInputStream(file);
                parseInternal(in);
                return;
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Error reading vendor file: " + file, e);
                return;
            } catch (org.xmlpull.v1.XmlPullParserException e2) {
                android.util.Slog.e(TAG, "Unable to parse vendor file: " + file, e2);
                return;
            }
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "no vendor priority configuration available. Using default priority");
        }
        addNewUseCasePriority(100, 180, 100);
        addNewUseCasePriority(200, 450, 200);
        addNewUseCasePriority(300, com.android.server.SystemService.PHASE_LOCK_SETTINGS_READY, 300);
        addNewUseCasePriority(400, 490, 400);
        addNewUseCasePriority(500, 600, 500);
    }

    protected void parseInternal(java.io.InputStream in) throws java.lang.Exception {
        try {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
            parser.nextTag();
            readUseCase(parser);
            in.close();
            for (int i = 0; i < this.mPriorityHints.size(); i++) {
                int useCase = this.mPriorityHints.keyAt(i);
                int[] priorities = this.mPriorityHints.get(useCase);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "{defaultFg=" + this.mDefaultForeground + ", defaultBg=" + this.mDefaultBackground + "}");
                    android.util.Slog.d(TAG, "{useCase=" + useCase + ", fg=" + priorities[0] + ", bg=" + priorities[1] + "}");
                }
            }
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            throw e;
        }
    }

    private void readUseCase(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, NS, "config");
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                java.lang.String name = parser.getName();
                if (name.equals("useCaseDefault")) {
                    this.mDefaultForeground = readAttributeToInt("fgPriority", parser);
                    this.mDefaultBackground = readAttributeToInt("bgPriority", parser);
                    parser.nextTag();
                    parser.require(3, NS, name);
                } else if (name.equals("useCasePreDefined")) {
                    int useCase = formatTypeToNum("type", parser);
                    if (useCase == -1) {
                        android.util.Slog.e(TAG, "Wrong predefined use case name given in the vendor config.");
                    } else {
                        addNewUseCasePriority(useCase, readAttributeToInt("fgPriority", parser), readAttributeToInt("bgPriority", parser));
                        parser.nextTag();
                        parser.require(3, NS, name);
                    }
                } else if (name.equals("useCaseVendor")) {
                    int useCase2 = readAttributeToInt("id", parser);
                    addNewUseCasePriority(useCase2, readAttributeToInt("fgPriority", parser), readAttributeToInt("bgPriority", parser));
                    this.mVendorDefinedUseCase.add(java.lang.Integer.valueOf(useCase2));
                    parser.nextTag();
                    parser.require(3, NS, name);
                } else {
                    skip(parser);
                }
            }
        }
    }

    private void skip(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (parser.getEventType() != 2) {
            throw new java.lang.IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case 2:
                    depth++;
                    break;
                case 3:
                    depth--;
                    break;
            }
        }
    }

    private int readAttributeToInt(java.lang.String attributeName, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
        return parser.getAttributeInt((java.lang.String) null, attributeName);
    }

    private void addNewUseCasePriority(int useCase, int fgPriority, int bgPriority) {
        int[] priorities = {fgPriority, bgPriority};
        this.mPriorityHints.append(useCase, priorities);
    }

    private static int formatTypeToNum(java.lang.String attributeName, com.android.modules.utils.TypedXmlPullParser parser) {
        byte b;
        java.lang.String useCaseName = parser.getAttributeValue((java.lang.String) null, attributeName);
        switch (useCaseName.hashCode()) {
            case -884787515:
                b = !useCaseName.equals("USE_CASE_BACKGROUND") ? (byte) -1 : (byte) 0;
                break;
            case 377959794:
                b = !useCaseName.equals("USE_CASE_PLAYBACK") ? (byte) -1 : (byte) 2;
                break;
            case 1222007747:
                b = !useCaseName.equals("USE_CASE_LIVE") ? (byte) -1 : (byte) 3;
                break;
            case 1222209876:
                b = !useCaseName.equals("USE_CASE_SCAN") ? (byte) -1 : (byte) 1;
                break;
            case 1990900072:
                b = !useCaseName.equals("USE_CASE_RECORD") ? (byte) -1 : (byte) 4;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return 100;
            case 1:
                return 200;
            case 2:
                return 300;
            case 3:
                return 400;
            case 4:
                return 500;
            default:
                return -1;
        }
    }

    private static boolean isPredefinedUseCase(int useCase) {
        switch (useCase) {
            case 100:
            case 200:
            case 300:
            case 400:
            case 500:
                return true;
            default:
                return false;
        }
    }
}
