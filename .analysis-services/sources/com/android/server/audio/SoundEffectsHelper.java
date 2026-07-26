package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
class SoundEffectsHelper {
    private static final java.lang.String ASSET_FILE_VERSION = "1.0";
    private static final java.lang.String ATTR_ASSET_FILE = "file";
    private static final java.lang.String ATTR_ASSET_ID = "id";
    private static final java.lang.String ATTR_GROUP_NAME = "name";
    private static final java.lang.String ATTR_VERSION = "version";
    private static final int EFFECT_NOT_IN_SOUND_POOL = 0;
    private static final java.lang.String GROUP_TOUCH_SOUNDS = "touch_sounds";
    private static final int MSG_LOAD_EFFECTS = 0;
    private static final int MSG_LOAD_EFFECTS_TIMEOUT = 3;
    private static final int MSG_PLAY_EFFECT = 2;
    private static final int MSG_UNLOAD_EFFECTS = 1;
    private static final int NUM_SOUNDPOOL_CHANNELS = 4;
    private static final int SOUND_EFFECTS_LOAD_TIMEOUT_MS = 15000;
    private static final java.lang.String SOUND_EFFECTS_PATH = "/media/audio/ui/";
    private static final java.lang.String TAG = "AS.SfxHelper";
    private static final java.lang.String TAG_ASSET = "asset";
    private static final java.lang.String TAG_AUDIO_ASSETS = "audio_assets";
    private static final java.lang.String TAG_GROUP = "group";
    private final android.content.Context mContext;
    private final java.util.function.Consumer<android.media.PlayerBase> mPlayerAvailableCb;
    private final int mSfxAttenuationDb;
    private com.android.server.audio.SoundEffectsHelper.SfxHandler mSfxHandler;
    private com.android.server.audio.SoundEffectsHelper.SfxWorker mSfxWorker;
    private android.media.SoundPool mSoundPool;
    private com.android.server.audio.SoundEffectsHelper.SoundPoolLoader mSoundPoolLoader;
    private final com.android.server.utils.EventLogger mSfxLogger = new com.android.server.utils.EventLogger(26, "Sound Effects Loading");
    private final java.util.List<com.android.server.audio.SoundEffectsHelper.Resource> mResources = new java.util.ArrayList();
    private final int[] mEffects = new int[16];

    interface OnEffectsLoadCompleteHandler {
        void run(boolean z);
    }

    private static final class Resource {
        final java.lang.String mFileName;
        boolean mLoaded;
        int mSampleId = 0;

        Resource(java.lang.String fileName) {
            this.mFileName = fileName;
        }

        void unload() {
            this.mSampleId = 0;
            this.mLoaded = false;
        }
    }

    SoundEffectsHelper(android.content.Context context, java.util.function.Consumer<android.media.PlayerBase> playerAvailableCb) {
        this.mContext = context;
        this.mSfxAttenuationDb = this.mContext.getResources().getInteger(android.R.integer.config_screenTimeoutOverride);
        this.mPlayerAvailableCb = playerAvailableCb;
        startWorker();
    }

    void loadSoundEffects(com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler onComplete) {
        sendMsg(0, 0, 0, onComplete, 0);
    }

    void unloadSoundEffects() {
        sendMsg(1, 0, 0, null, 0);
    }

    void playSoundEffect(int effect, int volume) {
        sendMsg(2, effect, volume, null, 0);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        if (this.mSfxHandler != null) {
            pw.println(prefix + "Message handler (watch for unhandled messages):");
            this.mSfxHandler.dump(new android.util.PrintWriterPrinter(pw), "  ");
        } else {
            pw.println(prefix + "Message handler is null");
        }
        pw.println(prefix + "Default attenuation (dB): " + this.mSfxAttenuationDb);
        this.mSfxLogger.dump(pw);
    }

    private void startWorker() {
        this.mSfxWorker = new com.android.server.audio.SoundEffectsHelper.SfxWorker();
        this.mSfxWorker.start();
        synchronized (this) {
            while (this.mSfxHandler == null) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                    android.util.Log.w(TAG, "Interrupted while waiting " + this.mSfxWorker.getName() + " to start");
                }
            }
        }
    }

    private void sendMsg(int msg, int arg1, int arg2, java.lang.Object obj, int delayMs) {
        this.mSfxHandler.sendMessageDelayed(this.mSfxHandler.obtainMessage(msg, arg1, arg2, obj), delayMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logEvent(java.lang.String msg) {
        this.mSfxLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(msg));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onLoadSoundEffects(com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler onComplete) {
        if (this.mSoundPoolLoader != null) {
            this.mSoundPoolLoader.addHandler(onComplete);
            return;
        }
        if (this.mSoundPool != null) {
            if (onComplete != null) {
                onComplete.run(true);
                return;
            }
            return;
        }
        logEvent("effects loading started");
        this.mSoundPool = new android.media.SoundPool.Builder().setMaxStreams(4).setAudioAttributes(new android.media.AudioAttributes.Builder().setUsage(13).setContentType(4).build()).build();
        this.mPlayerAvailableCb.accept(this.mSoundPool);
        loadSoundAssets();
        this.mSoundPoolLoader = new com.android.server.audio.SoundEffectsHelper.SoundPoolLoader();
        this.mSoundPoolLoader.addHandler(new com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler() { // from class: com.android.server.audio.SoundEffectsHelper.1
            @Override // com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler
            public void run(boolean success) {
                com.android.server.audio.SoundEffectsHelper.this.mSoundPoolLoader = null;
                if (!success) {
                    android.util.Log.w(com.android.server.audio.SoundEffectsHelper.TAG, "onLoadSoundEffects(), Error while loading samples");
                    com.android.server.audio.SoundEffectsHelper.this.onUnloadSoundEffects();
                }
            }
        });
        this.mSoundPoolLoader.addHandler(onComplete);
        int resourcesToLoad = 0;
        for (com.android.server.audio.SoundEffectsHelper.Resource res : this.mResources) {
            java.lang.String filePath = getResourceFilePath(res);
            int sampleId = this.mSoundPool.load(filePath, 0);
            if (sampleId > 0) {
                res.mSampleId = sampleId;
                res.mLoaded = false;
                resourcesToLoad++;
            } else {
                logEvent("effect " + filePath + " rejected by SoundPool");
                android.util.Log.w(TAG, "SoundPool could not load file: " + filePath);
            }
        }
        if (resourcesToLoad > 0) {
            sendMsg(3, 0, 0, null, 15000);
        } else {
            logEvent("effects loading completed, no effects to load");
            this.mSoundPoolLoader.onComplete(true);
        }
    }

    void onUnloadSoundEffects() {
        if (this.mSoundPool == null) {
            return;
        }
        if (this.mSoundPoolLoader != null) {
            this.mSoundPoolLoader.addHandler(new com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler() { // from class: com.android.server.audio.SoundEffectsHelper.2
                @Override // com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler
                public void run(boolean success) {
                    com.android.server.audio.SoundEffectsHelper.this.onUnloadSoundEffects();
                }
            });
        }
        logEvent("effects unloading started");
        for (com.android.server.audio.SoundEffectsHelper.Resource res : this.mResources) {
            if (res.mSampleId != 0) {
                this.mSoundPool.unload(res.mSampleId);
                res.unload();
            }
        }
        this.mSoundPool.release();
        this.mSoundPool = null;
        logEvent("effects unloading completed");
    }

    void onPlaySoundEffect(int effect, int volume) {
        float volFloat;
        if (volume < 0) {
            volFloat = (float) java.lang.Math.pow(10.0d, this.mSfxAttenuationDb / 20.0f);
        } else {
            float volFloat2 = volume;
            volFloat = volFloat2 / 1000.0f;
        }
        if (effect < 0 || effect >= 16) {
            return;
        }
        com.android.server.audio.SoundEffectsHelper.Resource res = this.mResources.get(this.mEffects[effect]);
        if (this.mSoundPool != null && res.mSampleId != 0 && res.mLoaded) {
            this.mSoundPool.play(res.mSampleId, volFloat, volFloat, 0, 0, 1.0f);
            return;
        }
        android.media.MediaPlayer mediaPlayer = new android.media.MediaPlayer();
        try {
            java.lang.String filePath = getResourceFilePath(res);
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(1);
            mediaPlayer.prepare();
            mediaPlayer.setVolume(volFloat);
            mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() { // from class: com.android.server.audio.SoundEffectsHelper.3
                @Override // android.media.MediaPlayer.OnCompletionListener
                public void onCompletion(android.media.MediaPlayer mp) {
                    com.android.server.audio.SoundEffectsHelper.cleanupPlayer(mp);
                }
            });
            mediaPlayer.setOnErrorListener(new android.media.MediaPlayer.OnErrorListener() { // from class: com.android.server.audio.SoundEffectsHelper.4
                @Override // android.media.MediaPlayer.OnErrorListener
                public boolean onError(android.media.MediaPlayer mp, int what, int extra) {
                    com.android.server.audio.SoundEffectsHelper.cleanupPlayer(mp);
                    return true;
                }
            });
            mediaPlayer.start();
        } catch (java.io.IOException ex) {
            android.util.Log.w(TAG, "MediaPlayer IOException: " + ex);
        } catch (java.lang.IllegalArgumentException ex2) {
            android.util.Log.w(TAG, "MediaPlayer IllegalArgumentException: " + ex2);
        } catch (java.lang.IllegalStateException ex3) {
            android.util.Log.w(TAG, "MediaPlayer IllegalStateException: " + ex3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void cleanupPlayer(android.media.MediaPlayer mp) {
        if (mp != null) {
            try {
                mp.stop();
                mp.release();
            } catch (java.lang.IllegalStateException ex) {
                android.util.Log.w(TAG, "MediaPlayer IllegalStateException: " + ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getResourceFilePath(com.android.server.audio.SoundEffectsHelper.Resource res) {
        java.lang.String filePath = android.os.Environment.getProductDirectory() + SOUND_EFFECTS_PATH + res.mFileName;
        if (!new java.io.File(filePath).isFile()) {
            return android.os.Environment.getRootDirectory() + SOUND_EFFECTS_PATH + res.mFileName;
        }
        return filePath;
    }

    private void loadSoundAssetDefaults() {
        int defaultResourceIdx = this.mResources.size();
        this.mResources.add(new com.android.server.audio.SoundEffectsHelper.Resource("Effect_Tick.ogg"));
        java.util.Arrays.fill(this.mEffects, defaultResourceIdx);
    }

    private void loadSoundAssets() {
        android.content.res.XmlResourceParser parser = null;
        if (this.mResources.isEmpty()) {
            loadSoundAssetDefaults();
            try {
                try {
                    try {
                        try {
                            parser = this.mContext.getResources().getXml(android.R.xml.audio_assets);
                            com.android.internal.util.XmlUtils.beginDocument(parser, TAG_AUDIO_ASSETS);
                            java.lang.String version = parser.getAttributeValue(null, ATTR_VERSION);
                            java.util.Map<java.lang.Integer, java.lang.Integer> parserCounter = new java.util.HashMap<>();
                            if (ASSET_FILE_VERSION.equals(version)) {
                                while (true) {
                                    com.android.internal.util.XmlUtils.nextElement(parser);
                                    java.lang.String element = parser.getName();
                                    if (element == null) {
                                        break;
                                    }
                                    if (!element.equals(TAG_GROUP)) {
                                        if (!element.equals(TAG_ASSET)) {
                                            break;
                                        }
                                        java.lang.String id = parser.getAttributeValue(null, ATTR_ASSET_ID);
                                        java.lang.String file = parser.getAttributeValue(null, ATTR_ASSET_FILE);
                                        try {
                                            java.lang.reflect.Field field = android.media.AudioManager.class.getField(id);
                                            int fx = field.getInt(null);
                                            int currentParserCount = parserCounter.getOrDefault(java.lang.Integer.valueOf(fx), 0).intValue() + 1;
                                            parserCounter.put(java.lang.Integer.valueOf(fx), java.lang.Integer.valueOf(currentParserCount));
                                            if (currentParserCount > 1) {
                                                android.util.Log.w(TAG, "Duplicate definition for sound ID: " + id);
                                            }
                                            this.mEffects[fx] = findOrAddResourceByFileName(file);
                                        } catch (java.lang.Exception e) {
                                            android.util.Log.w(TAG, "Invalid sound ID: " + id);
                                        }
                                    } else {
                                        java.lang.String name = parser.getAttributeValue(null, "name");
                                        if (!GROUP_TOUCH_SOUNDS.equals(name)) {
                                            android.util.Log.w(TAG, "Unsupported group name: " + name);
                                        }
                                    }
                                }
                                boolean navigationRepeatFxParsed = allNavigationRepeatSoundsParsed(parserCounter);
                                boolean homeSoundParsed = parserCounter.getOrDefault(11, 0).intValue() > 0;
                                if (navigationRepeatFxParsed || homeSoundParsed) {
                                    android.media.AudioManager audioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
                                    if (audioManager != null && navigationRepeatFxParsed) {
                                        audioManager.setNavigationRepeatSoundEffectsEnabled(true);
                                    }
                                    if (audioManager != null && homeSoundParsed) {
                                        audioManager.setHomeSoundEffectEnabled(true);
                                    }
                                }
                            }
                            if (parser == null) {
                                return;
                            }
                        } catch (java.lang.Throwable th) {
                            if (parser != null) {
                                parser.close();
                            }
                            throw th;
                        }
                    } catch (java.io.IOException e2) {
                        android.util.Log.w(TAG, "I/O exception reading sound assets", e2);
                        if (parser == null) {
                            return;
                        }
                    }
                } catch (android.content.res.Resources.NotFoundException e3) {
                    android.util.Log.w(TAG, "audio assets file not found", e3);
                    if (parser == null) {
                        return;
                    }
                }
            } catch (org.xmlpull.v1.XmlPullParserException e4) {
                android.util.Log.w(TAG, "XML parser exception reading sound assets", e4);
                if (parser == null) {
                    return;
                }
            }
            parser.close();
        }
    }

    private boolean allNavigationRepeatSoundsParsed(java.util.Map<java.lang.Integer, java.lang.Integer> parserCounter) {
        int numFastScrollSoundEffectsParsed = parserCounter.getOrDefault(12, 0).intValue() + parserCounter.getOrDefault(13, 0).intValue() + parserCounter.getOrDefault(14, 0).intValue() + parserCounter.getOrDefault(15, 0).intValue();
        return numFastScrollSoundEffectsParsed == 4;
    }

    private int findOrAddResourceByFileName(java.lang.String fileName) {
        for (int i = 0; i < this.mResources.size(); i++) {
            if (this.mResources.get(i).mFileName.equals(fileName)) {
                return i;
            }
        }
        int result = this.mResources.size();
        this.mResources.add(new com.android.server.audio.SoundEffectsHelper.Resource(fileName));
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.audio.SoundEffectsHelper.Resource findResourceBySampleId(int sampleId) {
        for (com.android.server.audio.SoundEffectsHelper.Resource res : this.mResources) {
            if (res.mSampleId == sampleId) {
                return res;
            }
        }
        return null;
    }

    private class SfxWorker extends java.lang.Thread {
        SfxWorker() {
            super("AS.SfxWorker");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            android.os.Looper.prepare();
            synchronized (com.android.server.audio.SoundEffectsHelper.this) {
                com.android.server.audio.SoundEffectsHelper.this.mSfxHandler = new com.android.server.audio.SoundEffectsHelper.SfxHandler();
                com.android.server.audio.SoundEffectsHelper.this.notify();
            }
            android.os.Looper.loop();
        }
    }

    private class SfxHandler extends android.os.Handler {
        private SfxHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    com.android.server.audio.SoundEffectsHelper.this.onLoadSoundEffects((com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler) msg.obj);
                    break;
                case 1:
                    com.android.server.audio.SoundEffectsHelper.this.onUnloadSoundEffects();
                    break;
                case 2:
                    final int effect = msg.arg1;
                    final int volume = msg.arg2;
                    com.android.server.audio.SoundEffectsHelper.this.onLoadSoundEffects(new com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler() { // from class: com.android.server.audio.SoundEffectsHelper.SfxHandler.1
                        @Override // com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler
                        public void run(boolean success) {
                            if (success) {
                                com.android.server.audio.SoundEffectsHelper.this.onPlaySoundEffect(effect, volume);
                            }
                        }
                    });
                    break;
                case 3:
                    if (com.android.server.audio.SoundEffectsHelper.this.mSoundPoolLoader != null) {
                        com.android.server.audio.SoundEffectsHelper.this.mSoundPoolLoader.onTimeout();
                    }
                    break;
            }
        }
    }

    private class SoundPoolLoader implements android.media.SoundPool.OnLoadCompleteListener {
        private java.util.List<com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler> mLoadCompleteHandlers = new java.util.ArrayList();

        SoundPoolLoader() {
            com.android.server.audio.SoundEffectsHelper.this.mSoundPool.setOnLoadCompleteListener(this);
        }

        void addHandler(com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler handler) {
            if (handler != null) {
                this.mLoadCompleteHandlers.add(handler);
            }
        }

        @Override // android.media.SoundPool.OnLoadCompleteListener
        public void onLoadComplete(android.media.SoundPool soundPool, int sampleId, int status) {
            java.lang.String filePath;
            if (status == 0) {
                int remainingToLoad = 0;
                for (com.android.server.audio.SoundEffectsHelper.Resource res : com.android.server.audio.SoundEffectsHelper.this.mResources) {
                    if (res.mSampleId == sampleId && !res.mLoaded) {
                        com.android.server.audio.SoundEffectsHelper.this.logEvent("effect " + res.mFileName + " loaded");
                        res.mLoaded = true;
                    }
                    if (res.mSampleId != 0 && !res.mLoaded) {
                        remainingToLoad++;
                    }
                }
                if (remainingToLoad == 0) {
                    onComplete(true);
                    return;
                }
                return;
            }
            com.android.server.audio.SoundEffectsHelper.Resource res2 = com.android.server.audio.SoundEffectsHelper.this.findResourceBySampleId(sampleId);
            if (res2 != null) {
                filePath = com.android.server.audio.SoundEffectsHelper.this.getResourceFilePath(res2);
            } else {
                filePath = "with unknown sample ID " + sampleId;
            }
            com.android.server.audio.SoundEffectsHelper.this.logEvent("effect " + filePath + " loading failed, status " + status);
            android.util.Log.w(com.android.server.audio.SoundEffectsHelper.TAG, "onLoadSoundEffects(), Error " + status + " while loading sample " + filePath);
            onComplete(false);
        }

        void onTimeout() {
            onComplete(false);
        }

        void onComplete(boolean success) {
            if (com.android.server.audio.SoundEffectsHelper.this.mSoundPool != null) {
                com.android.server.audio.SoundEffectsHelper.this.mSoundPool.setOnLoadCompleteListener(null);
            }
            for (com.android.server.audio.SoundEffectsHelper.OnEffectsLoadCompleteHandler handler : this.mLoadCompleteHandlers) {
                handler.run(success);
            }
            com.android.server.audio.SoundEffectsHelper.this.logEvent("effects loading " + (success ? "completed" : "failed"));
        }
    }
}
