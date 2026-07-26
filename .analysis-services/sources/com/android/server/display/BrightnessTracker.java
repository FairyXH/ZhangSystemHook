package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class BrightnessTracker {
    private static final java.lang.String AMBIENT_BRIGHTNESS_STATS_FILE = "ambient_brightness_stats.xml";
    private static final java.lang.String ATTR_BATTERY_LEVEL = "batteryLevel";
    private static final java.lang.String ATTR_COLOR_SAMPLE_DURATION = "colorSampleDuration";
    private static final java.lang.String ATTR_COLOR_TEMPERATURE = "colorTemperature";
    private static final java.lang.String ATTR_COLOR_VALUE_BUCKETS = "colorValueBuckets";
    private static final java.lang.String ATTR_DEFAULT_CONFIG = "defaultConfig";
    private static final java.lang.String ATTR_LAST_NITS = "lastNits";
    private static final java.lang.String ATTR_LUX = "lux";
    private static final java.lang.String ATTR_LUX_TIMESTAMPS = "luxTimestamps";
    private static final java.lang.String ATTR_NIGHT_MODE = "nightMode";
    private static final java.lang.String ATTR_NITS = "nits";
    private static final java.lang.String ATTR_PACKAGE_NAME = "packageName";
    private static final java.lang.String ATTR_POWER_SAVE = "powerSaveFactor";
    private static final java.lang.String ATTR_REDUCE_BRIGHT_COLORS = "reduceBrightColors";
    private static final java.lang.String ATTR_REDUCE_BRIGHT_COLORS_OFFSET = "reduceBrightColorsOffset";
    private static final java.lang.String ATTR_REDUCE_BRIGHT_COLORS_STRENGTH = "reduceBrightColorsStrength";
    private static final java.lang.String ATTR_TIMESTAMP = "timestamp";
    private static final java.lang.String ATTR_UNIQUE_DISPLAY_ID = "uniqueDisplayId";
    private static final java.lang.String ATTR_USER = "user";
    private static final java.lang.String ATTR_USER_POINT = "userPoint";
    private static final int COLOR_SAMPLE_COMPONENT_MASK = 4;
    private static final java.lang.String EVENTS_FILE = "brightness_events.xml";
    private static final int MAX_EVENTS = 100;
    private static final int MSG_BACKGROUND_START = 0;
    private static final int MSG_BRIGHTNESS_CHANGED = 1;
    private static final int MSG_SENSOR_CHANGED = 5;
    private static final int MSG_SHOULD_COLLECT_COLOR_SAMPLE_CHANGED = 4;
    private static final int MSG_START_SENSOR_LISTENER = 3;
    private static final int MSG_STOP_SENSOR_LISTENER = 2;
    static final java.lang.String TAG = "BrightnessTracker";
    private static final java.lang.String TAG_EVENT = "event";
    private static final java.lang.String TAG_EVENTS = "events";
    private com.android.server.display.AmbientBrightnessStatsTracker mAmbientBrightnessStatsTracker;
    private final android.os.Handler mBgHandler;
    private android.content.BroadcastReceiver mBroadcastReceiver;
    private boolean mColorSamplingEnabled;
    private final android.content.ContentResolver mContentResolver;
    private final android.content.Context mContext;
    private com.android.server.display.BrightnessTracker.DisplayListener mDisplayListener;
    private boolean mEventsDirty;
    private float mFrameRate;
    private final com.android.server.display.BrightnessTracker.Injector mInjector;
    private android.hardware.Sensor mLightSensor;
    private int mNoFramesToSample;
    private com.android.server.display.BrightnessTracker.SensorListener mSensorListener;
    private boolean mSensorRegistered;
    private com.android.server.display.BrightnessTracker.SettingsObserver mSettingsObserver;
    private boolean mStarted;
    private final android.os.UserManager mUserManager;
    private volatile boolean mWriteBrightnessTrackerStateScheduled;
    static final boolean DEBUG = android.os.SystemProperties.getBoolean("dbg.dms.brighttrack", false);
    private static final long MAX_EVENT_AGE = java.util.concurrent.TimeUnit.DAYS.toMillis(30);
    private static final java.text.SimpleDateFormat FORMAT = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    private static final long COLOR_SAMPLE_DURATION = java.util.concurrent.TimeUnit.SECONDS.toSeconds(10);
    private final java.lang.Object mEventsLock = new java.lang.Object();
    private com.android.internal.util.RingBuffer<android.hardware.display.BrightnessChangeEvent> mEvents = new com.android.internal.util.RingBuffer<>(android.hardware.display.BrightnessChangeEvent.class, 100);
    private boolean mShouldCollectColorSample = false;
    private boolean mRegistedFlag = false;
    private int mCurrentUserId = -10000;
    private final java.lang.Object mDataCollectionLock = new java.lang.Object();
    private float mLastBatteryLevel = Float.NaN;
    private float mLastBrightness = -1.0f;

    public BrightnessTracker(android.content.Context context, com.android.server.display.BrightnessTracker.Injector injector) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        if (injector != null) {
            this.mInjector = injector;
        } else {
            this.mInjector = new com.android.server.display.BrightnessTracker.Injector();
        }
        this.mBgHandler = new com.android.server.display.BrightnessTracker.TrackerHandler(this.mInjector.getBackgroundHandler().getLooper());
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
    }

    public void start(float initialBrightness) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Start");
        }
        this.mCurrentUserId = android.app.ActivityManager.getCurrentUser();
        this.mBgHandler.obtainMessage(0, java.lang.Float.valueOf(initialBrightness)).sendToTarget();
    }

    public void setShouldCollectColorSample(boolean shouldCollectColorSample) {
        this.mBgHandler.obtainMessage(4, java.lang.Boolean.valueOf(shouldCollectColorSample)).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void backgroundStart(float f) {
        synchronized (this.mDataCollectionLock) {
            if (this.mStarted) {
                return;
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, "Background start");
            }
            readEvents();
            readAmbientBrightnessStats();
            this.mSensorListener = new com.android.server.display.BrightnessTracker.SensorListener();
            this.mSettingsObserver = new com.android.server.display.BrightnessTracker.SettingsObserver(this.mBgHandler);
            this.mInjector.registerBrightnessModeObserver(this.mContentResolver, this.mSettingsObserver);
            android.content.IntentFilter intentFilter = new android.content.IntentFilter();
            intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
            intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
            intentFilter.addAction("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            this.mBroadcastReceiver = new com.android.server.display.BrightnessTracker.Receiver();
            this.mInjector.registerReceiver(this.mContext, this.mBroadcastReceiver, intentFilter);
            this.mRegistedFlag = true;
            this.mInjector.scheduleIdleJob(this.mContext);
            synchronized (this.mDataCollectionLock) {
                this.mLastBrightness = f;
                this.mStarted = true;
            }
            enableColorSampling();
        }
    }

    public void screenOffAction() {
        this.mBgHandler.obtainMessage(2).sendToTarget();
    }

    void stop() {
        synchronized (this.mDataCollectionLock) {
            if (this.mStarted) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "Stop");
                }
                this.mBgHandler.removeMessages(0);
                if (this.mRegistedFlag) {
                    this.mInjector.unregisterSensorListener(this.mContext, this.mSensorListener);
                    this.mInjector.unregisterBrightnessModeObserver(this.mContext, this.mSettingsObserver);
                    this.mInjector.unregisterReceiver(this.mContext, this.mBroadcastReceiver);
                }
                this.mRegistedFlag = false;
                this.mInjector.cancelIdleJob(this.mContext);
                synchronized (this.mDataCollectionLock) {
                    this.mStarted = false;
                }
                disableColorSampling();
            }
        }
    }

    public void onSwitchUser(int newUserId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Used id updated from " + this.mCurrentUserId + " to " + newUserId);
        }
        this.mCurrentUserId = newUserId;
    }

    public android.content.pm.ParceledListSlice<android.hardware.display.BrightnessChangeEvent> getEvents(int userId, boolean includePackage) {
        android.hardware.display.BrightnessChangeEvent[] events;
        synchronized (this.mEventsLock) {
            events = (android.hardware.display.BrightnessChangeEvent[]) this.mEvents.toArray();
        }
        int[] profiles = this.mInjector.getProfileIds(this.mUserManager, userId);
        java.util.Map<java.lang.Integer, java.lang.Boolean> toRedact = new java.util.HashMap<>();
        int i = 0;
        while (true) {
            boolean redact = true;
            if (i >= profiles.length) {
                break;
            }
            int profileId = profiles[i];
            if (includePackage && profileId == userId) {
                redact = false;
            }
            toRedact.put(java.lang.Integer.valueOf(profiles[i]), java.lang.Boolean.valueOf(redact));
            i++;
        }
        java.util.ArrayList<android.hardware.display.BrightnessChangeEvent> out = new java.util.ArrayList<>(events.length);
        for (int i2 = 0; i2 < events.length; i2++) {
            java.lang.Boolean redact2 = toRedact.get(java.lang.Integer.valueOf(events[i2].userId));
            if (redact2 != null) {
                if (!redact2.booleanValue()) {
                    out.add(events[i2]);
                } else {
                    android.hardware.display.BrightnessChangeEvent event = new android.hardware.display.BrightnessChangeEvent(events[i2], true);
                    out.add(event);
                }
            }
        }
        return new android.content.pm.ParceledListSlice<>(out);
    }

    public void persistBrightnessTrackerState() {
        scheduleWriteBrightnessTrackerState();
    }

    public void notifyBrightnessChanged(float f, boolean z, float f2, boolean z2, boolean z3, java.lang.String str, float[] fArr, long[] jArr) {
        if (DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("notifyBrightnessChanged(brightness=%f, userInitiated=%b)", java.lang.Float.valueOf(f), java.lang.Boolean.valueOf(z)));
        }
        this.mBgHandler.obtainMessage(1, z ? 1 : 0, 0, new com.android.server.display.BrightnessTracker.BrightnessChangeValues(f, f2, z2, z3, this.mInjector.currentTimeMillis(), str, fArr, jArr)).sendToTarget();
    }

    public void setLightSensor(android.hardware.Sensor lightSensor) {
        this.mBgHandler.obtainMessage(5, 0, 0, lightSensor).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBrightnessChanged(float brightness, boolean userInitiated, float powerBrightnessFactor, boolean wasShortTermModelActive, boolean isDefaultBrightnessConfig, long timestamp, java.lang.String uniqueDisplayId, float[] luxValues, long[] luxTimestamps) throws android.os.RemoteException {
        android.hardware.display.DisplayedContentSample sample;
        synchronized (this.mDataCollectionLock) {
            try {
                try {
                    if (this.mStarted) {
                        float previousBrightness = this.mLastBrightness;
                        this.mLastBrightness = brightness;
                        if (userInitiated) {
                            android.hardware.display.BrightnessChangeEvent.Builder builder = new android.hardware.display.BrightnessChangeEvent.Builder();
                            builder.setBrightness(brightness);
                            builder.setTimeStamp(timestamp);
                            builder.setPowerBrightnessFactor(powerBrightnessFactor);
                            builder.setUserBrightnessPoint(wasShortTermModelActive);
                            try {
                                builder.setIsDefaultBrightnessConfig(isDefaultBrightnessConfig);
                                builder.setUniqueDisplayId(uniqueDisplayId);
                                if (luxValues.length == 0) {
                                    return;
                                }
                                long[] luxTimestampsMillis = new long[luxTimestamps.length];
                                long currentTimeMillis = this.mInjector.currentTimeMillis();
                                long elapsedTimeNanos = this.mInjector.elapsedRealtimeNanos();
                                for (int i = 0; i < luxTimestamps.length; i++) {
                                    luxTimestampsMillis[i] = currentTimeMillis - (java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(elapsedTimeNanos) - luxTimestamps[i]);
                                }
                                builder.setLuxValues(luxValues);
                                builder.setLuxTimestamps(luxTimestampsMillis);
                                builder.setBatteryLevel(this.mLastBatteryLevel);
                                builder.setLastBrightness(previousBrightness);
                                try {
                                    android.app.ActivityTaskManager.RootTaskInfo focusedTask = this.mInjector.getFocusedStack();
                                    if (focusedTask != null && focusedTask.topActivity != null) {
                                        builder.setUserId(focusedTask.userId);
                                        builder.setPackageName(focusedTask.topActivity.getPackageName());
                                        builder.setNightMode(this.mInjector.isNightDisplayActivated(this.mContext));
                                        builder.setColorTemperature(this.mInjector.getNightDisplayColorTemperature(this.mContext));
                                        builder.setReduceBrightColors(this.mInjector.isReduceBrightColorsActivated(this.mContext));
                                        builder.setReduceBrightColorsStrength(this.mInjector.getReduceBrightColorsStrength(this.mContext));
                                        builder.setReduceBrightColorsOffset(this.mInjector.getReduceBrightColorsOffsetFactor(this.mContext) * brightness);
                                        if (this.mColorSamplingEnabled && (sample = this.mInjector.sampleColor(this.mNoFramesToSample)) != null && sample.getSampleComponent(android.hardware.display.DisplayedContentSample.ColorComponent.CHANNEL2) != null) {
                                            float numMillis = (sample.getNumFrames() / this.mFrameRate) * 1000.0f;
                                            builder.setColorValues(sample.getSampleComponent(android.hardware.display.DisplayedContentSample.ColorComponent.CHANNEL2), java.lang.Math.round(numMillis));
                                        }
                                        android.hardware.display.BrightnessChangeEvent event = builder.build();
                                        if (DEBUG) {
                                            android.util.Slog.d(TAG, "Event: " + event.toString());
                                        }
                                        synchronized (this.mEventsLock) {
                                            this.mEventsDirty = true;
                                            this.mEvents.append(event);
                                        }
                                        return;
                                    }
                                    if (DEBUG) {
                                        android.util.Slog.d(TAG, "Ignoring event due to null focusedTask.");
                                    }
                                } catch (android.os.RemoteException e) {
                                }
                            } catch (java.lang.Throwable th) {
                                e = th;
                                throw e;
                            }
                        }
                    }
                } catch (java.lang.Throwable th2) {
                    e = th2;
                    throw e;
                }
            } catch (java.lang.Throwable th3) {
                e = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSensorChanged(android.hardware.Sensor lightSensor) {
        if (this.mLightSensor != lightSensor) {
            this.mLightSensor = lightSensor;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleWriteBrightnessTrackerState() {
        if (!this.mWriteBrightnessTrackerStateScheduled) {
            this.mBgHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.BrightnessTracker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleWriteBrightnessTrackerState$0();
                }
            });
            this.mWriteBrightnessTrackerStateScheduled = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleWriteBrightnessTrackerState$0() {
        this.mWriteBrightnessTrackerStateScheduled = false;
        writeEvents();
        writeAmbientBrightnessStats();
    }

    private void writeEvents() {
        synchronized (this.mEventsLock) {
            if (this.mEventsDirty) {
                android.util.AtomicFile writeTo = this.mInjector.getFile(EVENTS_FILE);
                if (writeTo == null) {
                    return;
                }
                if (this.mEvents.isEmpty()) {
                    if (writeTo.exists()) {
                        writeTo.delete();
                    }
                    this.mEventsDirty = false;
                } else {
                    java.io.FileOutputStream output = null;
                    try {
                        output = writeTo.startWrite();
                        writeEventsLocked(output);
                        writeTo.finishWrite(output);
                        this.mEventsDirty = false;
                    } catch (java.io.IOException e) {
                        writeTo.failWrite(output);
                        android.util.Slog.e(TAG, "Failed to write change mEvents.", e);
                    }
                }
            }
        }
    }

    private void writeAmbientBrightnessStats() {
        android.util.AtomicFile writeTo = this.mInjector.getFile(AMBIENT_BRIGHTNESS_STATS_FILE);
        if (writeTo == null) {
            return;
        }
        java.io.FileOutputStream output = null;
        try {
            output = writeTo.startWrite();
            this.mAmbientBrightnessStatsTracker.writeStats(output);
            writeTo.finishWrite(output);
        } catch (java.io.IOException e) {
            writeTo.failWrite(output);
            android.util.Slog.e(TAG, "Failed to write ambient brightness stats.", e);
        }
    }

    private android.util.AtomicFile getFileWithLegacyFallback(java.lang.String filename) {
        android.util.AtomicFile legacyFile;
        android.util.AtomicFile file = this.mInjector.getFile(filename);
        if (file != null && !file.exists() && (legacyFile = this.mInjector.getLegacyFile(filename)) != null && legacyFile.exists()) {
            android.util.Slog.i(TAG, "Reading " + filename + " from old location");
            return legacyFile;
        }
        return file;
    }

    private void readEvents() {
        synchronized (this.mEventsLock) {
            this.mEventsDirty = true;
            this.mEvents.clear();
            android.util.AtomicFile readFrom = getFileWithLegacyFallback(EVENTS_FILE);
            if (readFrom != null && readFrom.exists()) {
                java.io.FileInputStream input = null;
                try {
                    try {
                        input = readFrom.openRead();
                        readEventsLocked(input);
                    } catch (java.io.IOException e) {
                        readFrom.delete();
                        android.util.Slog.e(TAG, "Failed to read change mEvents.", e);
                    }
                } finally {
                    libcore.io.IoUtils.closeQuietly(input);
                }
            }
        }
    }

    private void readAmbientBrightnessStats() {
        this.mAmbientBrightnessStatsTracker = new com.android.server.display.AmbientBrightnessStatsTracker(this.mUserManager, null);
        android.util.AtomicFile readFrom = getFileWithLegacyFallback(AMBIENT_BRIGHTNESS_STATS_FILE);
        if (readFrom != null && readFrom.exists()) {
            java.io.FileInputStream input = null;
            try {
                try {
                    input = readFrom.openRead();
                    this.mAmbientBrightnessStatsTracker.readStats(input);
                } catch (java.io.IOException e) {
                    readFrom.delete();
                    android.util.Slog.e(TAG, "Failed to read ambient brightness stats.", e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(input);
            }
        }
    }

    void writeEventsLocked(java.io.OutputStream stream) throws java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(stream);
        out.startDocument((java.lang.String) null, true);
        out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        out.startTag((java.lang.String) null, TAG_EVENTS);
        android.hardware.display.BrightnessChangeEvent[] toWrite = (android.hardware.display.BrightnessChangeEvent[]) this.mEvents.toArray();
        this.mEvents.clear();
        if (DEBUG) {
            android.util.Slog.d(TAG, "Writing events " + toWrite.length);
        }
        long timeCutOff = this.mInjector.currentTimeMillis() - MAX_EVENT_AGE;
        for (int i = 0; i < toWrite.length; i++) {
            int userSerialNo = this.mInjector.getUserSerialNumber(this.mUserManager, toWrite[i].userId);
            if (userSerialNo != -1 && toWrite[i].timeStamp > timeCutOff) {
                this.mEvents.append(toWrite[i]);
                out.startTag((java.lang.String) null, TAG_EVENT);
                out.attributeFloat((java.lang.String) null, ATTR_NITS, toWrite[i].brightness);
                out.attributeLong((java.lang.String) null, "timestamp", toWrite[i].timeStamp);
                out.attribute((java.lang.String) null, "packageName", toWrite[i].packageName);
                out.attributeInt((java.lang.String) null, ATTR_USER, userSerialNo);
                java.lang.String uniqueDisplayId = toWrite[i].uniqueDisplayId;
                if (uniqueDisplayId == null) {
                    uniqueDisplayId = "";
                }
                out.attribute((java.lang.String) null, ATTR_UNIQUE_DISPLAY_ID, uniqueDisplayId);
                out.attributeFloat((java.lang.String) null, ATTR_BATTERY_LEVEL, toWrite[i].batteryLevel);
                out.attributeBoolean((java.lang.String) null, ATTR_NIGHT_MODE, toWrite[i].nightMode);
                out.attributeInt((java.lang.String) null, ATTR_COLOR_TEMPERATURE, toWrite[i].colorTemperature);
                out.attributeBoolean((java.lang.String) null, ATTR_REDUCE_BRIGHT_COLORS, toWrite[i].reduceBrightColors);
                out.attributeInt((java.lang.String) null, ATTR_REDUCE_BRIGHT_COLORS_STRENGTH, toWrite[i].reduceBrightColorsStrength);
                out.attributeFloat((java.lang.String) null, ATTR_REDUCE_BRIGHT_COLORS_OFFSET, toWrite[i].reduceBrightColorsOffset);
                out.attributeFloat((java.lang.String) null, ATTR_LAST_NITS, toWrite[i].lastBrightness);
                out.attributeBoolean((java.lang.String) null, ATTR_DEFAULT_CONFIG, toWrite[i].isDefaultBrightnessConfig);
                out.attributeFloat((java.lang.String) null, ATTR_POWER_SAVE, toWrite[i].powerBrightnessFactor);
                out.attributeBoolean((java.lang.String) null, ATTR_USER_POINT, toWrite[i].isUserSetBrightness);
                java.lang.StringBuilder luxValues = new java.lang.StringBuilder();
                java.lang.StringBuilder luxTimestamps = new java.lang.StringBuilder();
                for (int j = 0; j < toWrite[i].luxValues.length; j++) {
                    if (j > 0) {
                        luxValues.append(',');
                        luxTimestamps.append(',');
                    }
                    luxValues.append(java.lang.Float.toString(toWrite[i].luxValues[j]));
                    luxTimestamps.append(java.lang.Long.toString(toWrite[i].luxTimestamps[j]));
                }
                out.attribute((java.lang.String) null, ATTR_LUX, luxValues.toString());
                out.attribute((java.lang.String) null, ATTR_LUX_TIMESTAMPS, luxTimestamps.toString());
                if (toWrite[i].colorValueBuckets != null && toWrite[i].colorValueBuckets.length > 0) {
                    out.attributeLong((java.lang.String) null, ATTR_COLOR_SAMPLE_DURATION, toWrite[i].colorSampleDuration);
                    java.lang.StringBuilder buckets = new java.lang.StringBuilder();
                    for (int j2 = 0; j2 < toWrite[i].colorValueBuckets.length; j2++) {
                        if (j2 > 0) {
                            buckets.append(',');
                        }
                        buckets.append(java.lang.Long.toString(toWrite[i].colorValueBuckets[j2]));
                    }
                    out.attribute((java.lang.String) null, ATTR_COLOR_VALUE_BUCKETS, buckets.toString());
                }
                out.endTag((java.lang.String) null, TAG_EVENT);
            }
        }
        out.endTag((java.lang.String) null, TAG_EVENTS);
        out.endDocument();
        stream.flush();
    }

    void readEventsLocked(java.io.InputStream stream) throws java.io.IOException {
        int type;
        int i;
        java.lang.String str;
        com.android.modules.utils.TypedXmlPullParser parser;
        int type2;
        int outerDepth;
        java.lang.String str2;
        com.android.modules.utils.TypedXmlPullParser parser2;
        java.lang.String tag;
        int type3;
        int outerDepth2;
        java.lang.String str3 = ",";
        try {
            com.android.modules.utils.TypedXmlPullParser parser3 = android.util.Xml.resolvePullParser(stream);
            do {
                type = parser3.next();
                i = 1;
                if (type == 1) {
                    break;
                }
            } while (type != 2);
            java.lang.String tag2 = parser3.getName();
            if (!TAG_EVENTS.equals(tag2)) {
                throw new org.xmlpull.v1.XmlPullParserException("Events not found in brightness tracker file " + tag2);
            }
            long timeCutOff = this.mInjector.currentTimeMillis() - MAX_EVENT_AGE;
            int outerDepth3 = parser3.getDepth();
            while (true) {
                int type4 = parser3.next();
                if (type4 != i) {
                    if (type4 != 3 || parser3.getDepth() > outerDepth3) {
                        if (type4 == 3) {
                            str = str3;
                            parser = parser3;
                            type2 = type4;
                            outerDepth = outerDepth3;
                        } else if (type4 == 4) {
                            str = str3;
                            parser = parser3;
                            type2 = type4;
                            outerDepth = outerDepth3;
                        } else {
                            java.lang.String tag3 = parser3.getName();
                            if (!TAG_EVENT.equals(tag3)) {
                                str2 = str3;
                                parser2 = parser3;
                                tag = tag3;
                                type3 = type4;
                                outerDepth2 = outerDepth3;
                            } else {
                                android.hardware.display.BrightnessChangeEvent.Builder builder = new android.hardware.display.BrightnessChangeEvent.Builder();
                                builder.setBrightness(parser3.getAttributeFloat((java.lang.String) null, ATTR_NITS));
                                builder.setTimeStamp(parser3.getAttributeLong((java.lang.String) null, "timestamp"));
                                builder.setPackageName(parser3.getAttributeValue((java.lang.String) null, "packageName"));
                                builder.setUserId(this.mInjector.getUserId(this.mUserManager, parser3.getAttributeInt((java.lang.String) null, ATTR_USER)));
                                java.lang.String uniqueDisplayId = parser3.getAttributeValue((java.lang.String) null, ATTR_UNIQUE_DISPLAY_ID);
                                if (uniqueDisplayId == null) {
                                    uniqueDisplayId = "";
                                }
                                builder.setUniqueDisplayId(uniqueDisplayId);
                                builder.setBatteryLevel(parser3.getAttributeFloat((java.lang.String) null, ATTR_BATTERY_LEVEL));
                                builder.setNightMode(parser3.getAttributeBoolean((java.lang.String) null, ATTR_NIGHT_MODE));
                                builder.setColorTemperature(parser3.getAttributeInt((java.lang.String) null, ATTR_COLOR_TEMPERATURE));
                                builder.setReduceBrightColors(parser3.getAttributeBoolean((java.lang.String) null, ATTR_REDUCE_BRIGHT_COLORS));
                                builder.setReduceBrightColorsStrength(parser3.getAttributeInt((java.lang.String) null, ATTR_REDUCE_BRIGHT_COLORS_STRENGTH));
                                builder.setReduceBrightColorsOffset(parser3.getAttributeFloat((java.lang.String) null, ATTR_REDUCE_BRIGHT_COLORS_OFFSET));
                                builder.setLastBrightness(parser3.getAttributeFloat((java.lang.String) null, ATTR_LAST_NITS));
                                java.lang.String luxValue = parser3.getAttributeValue((java.lang.String) null, ATTR_LUX);
                                java.lang.String luxTimestamp = parser3.getAttributeValue((java.lang.String) null, ATTR_LUX_TIMESTAMPS);
                                java.lang.String[] luxValuesStrings = luxValue.split(str3);
                                java.lang.String[] luxTimestampsStrings = luxTimestamp.split(str3);
                                tag = tag3;
                                type3 = type4;
                                if (luxValuesStrings.length != luxTimestampsStrings.length) {
                                    str2 = str3;
                                    parser2 = parser3;
                                    outerDepth2 = outerDepth3;
                                } else {
                                    float[] luxValues = new float[luxValuesStrings.length];
                                    long[] luxTimestamps = new long[luxValuesStrings.length];
                                    outerDepth2 = outerDepth3;
                                    int outerDepth4 = 0;
                                    while (true) {
                                        java.lang.String uniqueDisplayId2 = uniqueDisplayId;
                                        if (outerDepth4 >= luxValues.length) {
                                            break;
                                        }
                                        luxValues[outerDepth4] = java.lang.Float.parseFloat(luxValuesStrings[outerDepth4]);
                                        luxTimestamps[outerDepth4] = java.lang.Long.parseLong(luxTimestampsStrings[outerDepth4]);
                                        outerDepth4++;
                                        uniqueDisplayId = uniqueDisplayId2;
                                    }
                                    builder.setLuxValues(luxValues);
                                    builder.setLuxTimestamps(luxTimestamps);
                                    builder.setIsDefaultBrightnessConfig(parser3.getAttributeBoolean((java.lang.String) null, ATTR_DEFAULT_CONFIG, false));
                                    builder.setPowerBrightnessFactor(parser3.getAttributeFloat((java.lang.String) null, ATTR_POWER_SAVE, 1.0f));
                                    builder.setUserBrightnessPoint(parser3.getAttributeBoolean((java.lang.String) null, ATTR_USER_POINT, false));
                                    long colorSampleDuration = parser3.getAttributeLong((java.lang.String) null, ATTR_COLOR_SAMPLE_DURATION, -1L);
                                    java.lang.String colorValueBucketsString = parser3.getAttributeValue((java.lang.String) null, ATTR_COLOR_VALUE_BUCKETS);
                                    if (colorSampleDuration == -1 || colorValueBucketsString == null) {
                                        str2 = str3;
                                        parser2 = parser3;
                                    } else {
                                        java.lang.String[] buckets = colorValueBucketsString.split(str3);
                                        long[] bucketValues = new long[buckets.length];
                                        str2 = str3;
                                        int i2 = 0;
                                        while (true) {
                                            parser2 = parser3;
                                            if (i2 >= bucketValues.length) {
                                                break;
                                            }
                                            bucketValues[i2] = java.lang.Long.parseLong(buckets[i2]);
                                            i2++;
                                            parser3 = parser2;
                                        }
                                        builder.setColorValues(bucketValues, colorSampleDuration);
                                    }
                                    android.hardware.display.BrightnessChangeEvent event = builder.build();
                                    if (DEBUG) {
                                        android.util.Slog.i(TAG, "Read event " + event.brightness + " " + event.packageName);
                                    }
                                    if (event.userId != -1 && event.timeStamp > timeCutOff && event.luxValues.length > 0) {
                                        this.mEvents.append(event);
                                    }
                                }
                            }
                            outerDepth3 = outerDepth2;
                            parser3 = parser2;
                            str3 = str2;
                            i = 1;
                        }
                        outerDepth3 = outerDepth;
                        parser3 = parser;
                        str3 = str;
                        i = 1;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        } catch (java.io.IOException | java.lang.NullPointerException | java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e) {
            this.mEvents = new com.android.internal.util.RingBuffer<>(android.hardware.display.BrightnessChangeEvent.class, 100);
            android.util.Slog.e(TAG, "Failed to parse brightness event", e);
            throw new java.io.IOException("failed to parse file", e);
        }
    }

    public void dump(final java.io.PrintWriter pw) {
        pw.println("BrightnessTracker state:");
        synchronized (this.mDataCollectionLock) {
            pw.println("  mStarted=" + this.mStarted);
            pw.println("  mLightSensor=" + this.mLightSensor);
            pw.println("  mLastBatteryLevel=" + this.mLastBatteryLevel);
            pw.println("  mLastBrightness=" + this.mLastBrightness);
        }
        synchronized (this.mEventsLock) {
            pw.println("  mEventsDirty=" + this.mEventsDirty);
            pw.println("  mEvents.size=" + this.mEvents.size());
            android.hardware.display.BrightnessChangeEvent[] events = (android.hardware.display.BrightnessChangeEvent[]) this.mEvents.toArray();
            for (int i = 0; i < events.length; i++) {
                pw.print("    " + FORMAT.format(new java.util.Date(events[i].timeStamp)));
                pw.print(", userId=" + events[i].userId);
                pw.print(", " + events[i].lastBrightness + "->" + events[i].brightness);
                pw.print(", isUserSetBrightness=" + events[i].isUserSetBrightness);
                pw.print(", powerBrightnessFactor=" + events[i].powerBrightnessFactor);
                pw.print(", isDefaultBrightnessConfig=" + events[i].isDefaultBrightnessConfig);
                pw.print(", recent lux values=");
                pw.print(" {");
                for (int j = 0; j < events[i].luxValues.length; j++) {
                    if (j != 0) {
                        pw.print(", ");
                    }
                    pw.print("(" + events[i].luxValues[j] + "," + events[i].luxTimestamps[j] + ")");
                }
                pw.println("}");
            }
        }
        pw.println("  mWriteBrightnessTrackerStateScheduled=" + this.mWriteBrightnessTrackerStateScheduled);
        this.mBgHandler.runWithScissors(new java.lang.Runnable() { // from class: com.android.server.display.BrightnessTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dump$1(pw);
            }
        }, 1000L);
        if (this.mAmbientBrightnessStatsTracker != null) {
            pw.println();
            this.mAmbientBrightnessStatsTracker.dump(pw);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dumpLocal, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$1(java.io.PrintWriter pw) {
        pw.println("  mSensorRegistered=" + this.mSensorRegistered);
        pw.println("  mColorSamplingEnabled=" + this.mColorSamplingEnabled);
        pw.println("  mNoFramesToSample=" + this.mNoFramesToSample);
        pw.println("  mFrameRate=" + this.mFrameRate);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableColorSampling() {
        if (!this.mInjector.isBrightnessModeAutomatic(this.mContentResolver) || !this.mInjector.isInteractive(this.mContext) || this.mColorSamplingEnabled || !this.mShouldCollectColorSample) {
            return;
        }
        this.mFrameRate = this.mInjector.getFrameRate(this.mContext);
        if (this.mFrameRate <= 0.0f) {
            android.util.Slog.wtf(TAG, "Default display has a zero or negative framerate.");
            return;
        }
        this.mNoFramesToSample = (int) (this.mFrameRate * COLOR_SAMPLE_DURATION);
        android.hardware.display.DisplayedContentSamplingAttributes attributes = this.mInjector.getSamplingAttributes();
        if (DEBUG && attributes != null) {
            android.util.Slog.d(TAG, "Color sampling mask=0x" + java.lang.Integer.toHexString(attributes.getComponentMask()) + " dataSpace=0x" + java.lang.Integer.toHexString(attributes.getDataspace()) + " pixelFormat=0x" + java.lang.Integer.toHexString(attributes.getPixelFormat()));
        }
        if (attributes != null && attributes.getPixelFormat() == 55 && (attributes.getComponentMask() & 4) != 0) {
            this.mColorSamplingEnabled = this.mInjector.enableColorSampling(true, this.mNoFramesToSample);
            if (DEBUG) {
                android.util.Slog.i(TAG, "turning on color sampling for " + this.mNoFramesToSample + " frames, success=" + this.mColorSamplingEnabled);
            }
        }
        if (this.mColorSamplingEnabled && this.mDisplayListener == null) {
            this.mDisplayListener = new com.android.server.display.BrightnessTracker.DisplayListener();
            this.mInjector.registerDisplayListener(this.mContext, this.mDisplayListener, this.mBgHandler);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableColorSampling() {
        if (!this.mColorSamplingEnabled) {
            return;
        }
        this.mInjector.enableColorSampling(false, 0);
        this.mColorSamplingEnabled = false;
        if (this.mDisplayListener != null) {
            this.mInjector.unRegisterDisplayListener(this.mContext, this.mDisplayListener);
            this.mDisplayListener = null;
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "turning off color sampling");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateColorSampling() {
        if (!this.mColorSamplingEnabled) {
            return;
        }
        float frameRate = this.mInjector.getFrameRate(this.mContext);
        if (frameRate != this.mFrameRate) {
            disableColorSampling();
            enableColorSampling();
        }
    }

    public android.content.pm.ParceledListSlice<android.hardware.display.AmbientBrightnessDayStats> getAmbientBrightnessStats(int userId) {
        java.util.ArrayList<android.hardware.display.AmbientBrightnessDayStats> stats;
        if (this.mAmbientBrightnessStatsTracker != null && (stats = this.mAmbientBrightnessStatsTracker.getUserStats(userId)) != null) {
            return new android.content.pm.ParceledListSlice<>(stats);
        }
        return android.content.pm.ParceledListSlice.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recordAmbientBrightnessStats(android.hardware.SensorEvent event) {
        this.mAmbientBrightnessStatsTracker.add(this.mCurrentUserId, event.values[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void batteryLevelChanged(int level, int scale) {
        synchronized (this.mDataCollectionLock) {
            this.mLastBatteryLevel = level / scale;
        }
    }

    private final class SensorListener implements android.hardware.SensorEventListener {
        private SensorListener() {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(android.hardware.SensorEvent event) {
            com.android.server.display.BrightnessTracker.this.recordAmbientBrightnessStats(event);
        }

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
        }
    }

    private final class DisplayListener implements android.hardware.display.DisplayManager.DisplayListener {
        private DisplayListener() {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            if (displayId == 0) {
                com.android.server.display.BrightnessTracker.this.updateColorSampling();
            }
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        public SettingsObserver(android.os.Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (com.android.server.display.BrightnessTracker.DEBUG) {
                android.util.Slog.v(com.android.server.display.BrightnessTracker.TAG, "settings change " + uri);
            }
            if (com.android.server.display.BrightnessTracker.this.mInjector.isBrightnessModeAutomatic(com.android.server.display.BrightnessTracker.this.mContentResolver)) {
                com.android.server.display.BrightnessTracker.this.mBgHandler.obtainMessage(3).sendToTarget();
            } else {
                com.android.server.display.BrightnessTracker.this.mBgHandler.obtainMessage(2).sendToTarget();
            }
        }
    }

    private final class Receiver extends android.content.BroadcastReceiver {
        private Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (com.android.server.display.BrightnessTracker.DEBUG) {
                android.util.Slog.d(com.android.server.display.BrightnessTracker.TAG, "Received " + intent.getAction());
            }
            java.lang.String action = intent.getAction();
            if ("android.intent.action.ACTION_SHUTDOWN".equals(action)) {
                com.android.server.display.BrightnessTracker.this.stop();
                com.android.server.display.BrightnessTracker.this.scheduleWriteBrightnessTrackerState();
                return;
            }
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                int level = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", 0);
                if (level != -1 && scale != 0) {
                    com.android.server.display.BrightnessTracker.this.batteryLevelChanged(level, scale);
                    return;
                }
                return;
            }
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                com.android.server.display.BrightnessTracker.this.mBgHandler.obtainMessage(2).sendToTarget();
            } else if ("android.intent.action.SCREEN_ON".equals(action)) {
                com.android.server.display.BrightnessTracker.this.mBgHandler.obtainMessage(3).sendToTarget();
            }
        }
    }

    private final class TrackerHandler extends android.os.Handler {
        public TrackerHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) throws android.os.RemoteException {
            switch (msg.what) {
                case 0:
                    com.android.server.display.BrightnessTracker.this.backgroundStart(((java.lang.Float) msg.obj).floatValue());
                    break;
                case 1:
                    com.android.server.display.BrightnessTracker.BrightnessChangeValues values = (com.android.server.display.BrightnessTracker.BrightnessChangeValues) msg.obj;
                    boolean userInitiatedChange = msg.arg1 == 1;
                    com.android.server.display.BrightnessTracker.this.handleBrightnessChanged(values.brightness, userInitiatedChange, values.powerBrightnessFactor, values.wasShortTermModelActive, values.isDefaultBrightnessConfig, values.timestamp, values.uniqueDisplayId, values.luxValues, values.luxTimestamps);
                    break;
                case 2:
                    com.android.server.display.BrightnessTracker.this.disableColorSampling();
                    break;
                case 3:
                    com.android.server.display.BrightnessTracker.this.enableColorSampling();
                    break;
                case 4:
                    com.android.server.display.BrightnessTracker.this.mShouldCollectColorSample = ((java.lang.Boolean) msg.obj).booleanValue();
                    if (com.android.server.display.BrightnessTracker.this.mShouldCollectColorSample && !com.android.server.display.BrightnessTracker.this.mColorSamplingEnabled) {
                        com.android.server.display.BrightnessTracker.this.enableColorSampling();
                        break;
                    } else if (!com.android.server.display.BrightnessTracker.this.mShouldCollectColorSample && com.android.server.display.BrightnessTracker.this.mColorSamplingEnabled) {
                        com.android.server.display.BrightnessTracker.this.disableColorSampling();
                        break;
                    }
                    break;
                case 5:
                    com.android.server.display.BrightnessTracker.this.handleSensorChanged((android.hardware.Sensor) msg.obj);
                    break;
            }
        }
    }

    private static class BrightnessChangeValues {
        public final float brightness;
        public final boolean isDefaultBrightnessConfig;
        public final long[] luxTimestamps;
        public final float[] luxValues;
        public final float powerBrightnessFactor;
        public final long timestamp;
        public final java.lang.String uniqueDisplayId;
        public final boolean wasShortTermModelActive;

        BrightnessChangeValues(float brightness, float powerBrightnessFactor, boolean wasShortTermModelActive, boolean isDefaultBrightnessConfig, long timestamp, java.lang.String uniqueDisplayId, float[] luxValues, long[] luxTimestamps) {
            this.brightness = brightness;
            this.powerBrightnessFactor = powerBrightnessFactor;
            this.wasShortTermModelActive = wasShortTermModelActive;
            this.isDefaultBrightnessConfig = isDefaultBrightnessConfig;
            this.timestamp = timestamp;
            this.uniqueDisplayId = uniqueDisplayId;
            this.luxValues = luxValues;
            this.luxTimestamps = luxTimestamps;
        }
    }

    static class Injector {
        Injector() {
        }

        public void registerSensorListener(android.content.Context context, android.hardware.SensorEventListener sensorListener, android.hardware.Sensor lightSensor, android.os.Handler handler) {
            android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) context.getSystemService(android.hardware.SensorManager.class);
            sensorManager.registerListener(sensorListener, lightSensor, 3, handler);
        }

        public void unregisterSensorListener(android.content.Context context, android.hardware.SensorEventListener sensorListener) {
            android.hardware.SensorManager sensorManager = (android.hardware.SensorManager) context.getSystemService(android.hardware.SensorManager.class);
            sensorManager.unregisterListener(sensorListener);
        }

        public void registerBrightnessModeObserver(android.content.ContentResolver resolver, android.database.ContentObserver settingsObserver) {
            resolver.registerContentObserver(android.provider.Settings.System.getUriFor("screen_brightness_mode"), false, settingsObserver, -1);
        }

        public void unregisterBrightnessModeObserver(android.content.Context context, android.database.ContentObserver settingsObserver) {
            context.getContentResolver().unregisterContentObserver(settingsObserver);
        }

        public void registerReceiver(android.content.Context context, android.content.BroadcastReceiver receiver, android.content.IntentFilter filter) {
            context.registerReceiver(receiver, filter, 2);
        }

        public void unregisterReceiver(android.content.Context context, android.content.BroadcastReceiver receiver) {
            context.unregisterReceiver(receiver);
        }

        public android.os.Handler getBackgroundHandler() {
            return com.android.internal.os.BackgroundThread.getHandler();
        }

        public boolean isBrightnessModeAutomatic(android.content.ContentResolver resolver) {
            return android.provider.Settings.System.getIntForUser(resolver, "screen_brightness_mode", 0, -2) == 1;
        }

        public int getSecureIntForUser(android.content.ContentResolver resolver, java.lang.String setting, int defaultValue, int userId) {
            return android.provider.Settings.Secure.getIntForUser(resolver, setting, defaultValue, userId);
        }

        public android.util.AtomicFile getFile(java.lang.String filename) {
            return new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), filename));
        }

        public android.util.AtomicFile getLegacyFile(java.lang.String filename) {
            return new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDeDirectory(), filename));
        }

        public long currentTimeMillis() {
            return java.lang.System.currentTimeMillis();
        }

        public long elapsedRealtimeNanos() {
            return android.os.SystemClock.elapsedRealtimeNanos();
        }

        public int getUserSerialNumber(android.os.UserManager userManager, int userId) {
            return userManager.getUserSerialNumber(userId);
        }

        public int getUserId(android.os.UserManager userManager, int userSerialNumber) {
            return userManager.getUserHandle(userSerialNumber);
        }

        public int[] getProfileIds(android.os.UserManager userManager, int userId) {
            if (userManager != null) {
                return userManager.getProfileIds(userId, false);
            }
            return new int[]{userId};
        }

        public android.app.ActivityTaskManager.RootTaskInfo getFocusedStack() throws android.os.RemoteException {
            return android.app.ActivityTaskManager.getService().getFocusedRootTaskInfo();
        }

        public void scheduleIdleJob(android.content.Context context) {
            com.android.server.display.BrightnessIdleJob.scheduleJob(context);
        }

        public void cancelIdleJob(android.content.Context context) {
            com.android.server.display.BrightnessIdleJob.cancelJob(context);
        }

        public boolean isInteractive(android.content.Context context) {
            return ((android.os.PowerManager) context.getSystemService(android.os.PowerManager.class)).isInteractive();
        }

        public int getNightDisplayColorTemperature(android.content.Context context) {
            return ((android.hardware.display.ColorDisplayManager) context.getSystemService(android.hardware.display.ColorDisplayManager.class)).getNightDisplayColorTemperature();
        }

        public boolean isNightDisplayActivated(android.content.Context context) {
            return ((android.hardware.display.ColorDisplayManager) context.getSystemService(android.hardware.display.ColorDisplayManager.class)).isNightDisplayActivated();
        }

        public int getReduceBrightColorsStrength(android.content.Context context) {
            return ((android.hardware.display.ColorDisplayManager) context.getSystemService(android.hardware.display.ColorDisplayManager.class)).getReduceBrightColorsStrength();
        }

        public float getReduceBrightColorsOffsetFactor(android.content.Context context) {
            return ((android.hardware.display.ColorDisplayManager) context.getSystemService(android.hardware.display.ColorDisplayManager.class)).getReduceBrightColorsOffsetFactor();
        }

        public boolean isReduceBrightColorsActivated(android.content.Context context) {
            return ((android.hardware.display.ColorDisplayManager) context.getSystemService(android.hardware.display.ColorDisplayManager.class)).isReduceBrightColorsActivated();
        }

        public android.hardware.display.DisplayedContentSample sampleColor(int noFramesToSample) {
            android.hardware.display.DisplayManagerInternal displayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
            return displayManagerInternal.getDisplayedContentSample(0, noFramesToSample, 0L);
        }

        public float getFrameRate(android.content.Context context) {
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
            android.view.Display display = displayManager.getDisplay(0);
            return display.getRefreshRate();
        }

        public android.hardware.display.DisplayedContentSamplingAttributes getSamplingAttributes() {
            android.hardware.display.DisplayManagerInternal displayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
            return displayManagerInternal.getDisplayedContentSamplingAttributes(0);
        }

        public boolean enableColorSampling(boolean enable, int noFrames) {
            android.hardware.display.DisplayManagerInternal displayManagerInternal = (android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class);
            return displayManagerInternal.setDisplayedContentSamplingEnabled(0, enable, 4, noFrames);
        }

        public void registerDisplayListener(android.content.Context context, android.hardware.display.DisplayManager.DisplayListener listener, android.os.Handler handler) {
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
            displayManager.registerDisplayListener(listener, handler);
        }

        public void unRegisterDisplayListener(android.content.Context context, android.hardware.display.DisplayManager.DisplayListener listener) {
            android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) context.getSystemService(android.hardware.display.DisplayManager.class);
            displayManager.unregisterDisplayListener(listener);
        }
    }
}
