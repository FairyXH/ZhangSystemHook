package com.android.server.locksettings;

/* JADX INFO: loaded from: classes2.dex */
class PasswordSlotManager {
    private static final java.lang.String GSI_RUNNING_PROP = "ro.gsid.image_running";
    private static final java.lang.String SLOT_MAP_DIR = "/metadata/password_slots";
    private static final java.lang.String TAG = "PasswordSlotManager";
    private java.util.Set<java.lang.Integer> mActiveSlots;
    private java.util.Map<java.lang.Integer, java.lang.String> mSlotMap;

    protected java.lang.String getSlotMapDir() {
        return SLOT_MAP_DIR;
    }

    protected int getGsiImageNumber() {
        return android.os.SystemProperties.getInt(GSI_RUNNING_PROP, 0);
    }

    public void refreshActiveSlots(java.util.Set<java.lang.Integer> activeSlots) throws java.lang.RuntimeException {
        if (this.mSlotMap == null) {
            this.mActiveSlots = new java.util.HashSet(activeSlots);
            return;
        }
        java.util.HashSet<java.lang.Integer> slotsToDelete = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.Integer, java.lang.String> entry : this.mSlotMap.entrySet()) {
            if (entry.getValue().equals(getMode())) {
                slotsToDelete.add(entry.getKey());
            }
        }
        for (java.lang.Integer slot : slotsToDelete) {
            this.mSlotMap.remove(slot);
        }
        for (java.lang.Integer slot2 : activeSlots) {
            this.mSlotMap.put(slot2, getMode());
        }
        saveSlotMap();
    }

    public void markSlotInUse(int slot) throws java.lang.RuntimeException {
        ensureSlotMapLoaded();
        if (this.mSlotMap.containsKey(java.lang.Integer.valueOf(slot)) && !this.mSlotMap.get(java.lang.Integer.valueOf(slot)).equals(getMode())) {
            throw new java.lang.IllegalStateException("password slot " + slot + " is not available");
        }
        this.mSlotMap.put(java.lang.Integer.valueOf(slot), getMode());
        saveSlotMap();
    }

    public void markSlotDeleted(int slot) throws java.lang.RuntimeException {
        ensureSlotMapLoaded();
        if (this.mSlotMap.containsKey(java.lang.Integer.valueOf(slot)) && !this.mSlotMap.get(java.lang.Integer.valueOf(slot)).equals(getMode())) {
            throw new java.lang.IllegalStateException("password slot " + slot + " cannot be deleted");
        }
        this.mSlotMap.remove(java.lang.Integer.valueOf(slot));
        saveSlotMap();
    }

    public java.util.Set<java.lang.Integer> getUsedSlots() {
        ensureSlotMapLoaded();
        return java.util.Collections.unmodifiableSet(this.mSlotMap.keySet());
    }

    private java.io.File getSlotMapFile() {
        return java.nio.file.Paths.get(getSlotMapDir(), "slot_map").toFile();
    }

    private java.lang.String getMode() {
        int gsiIndex = getGsiImageNumber();
        if (gsiIndex > 0) {
            return "gsi" + gsiIndex;
        }
        return "host";
    }

    protected java.util.Map<java.lang.Integer, java.lang.String> loadSlotMap(java.io.InputStream stream) throws java.io.IOException {
        java.util.HashMap<java.lang.Integer, java.lang.String> map = new java.util.HashMap<>();
        java.util.Properties props = new java.util.Properties();
        props.load(stream);
        for (java.lang.String slotString : props.stringPropertyNames()) {
            int slot = java.lang.Integer.parseInt(slotString);
            java.lang.String owner = props.getProperty(slotString);
            map.put(java.lang.Integer.valueOf(slot), owner);
        }
        return map;
    }

    private java.util.Map<java.lang.Integer, java.lang.String> loadSlotMap() {
        java.io.File file = getSlotMapFile();
        if (file.exists()) {
            try {
                java.io.FileInputStream stream = new java.io.FileInputStream(file);
                try {
                    java.util.Map<java.lang.Integer, java.lang.String> mapLoadSlotMap = loadSlotMap(stream);
                    stream.close();
                    return mapLoadSlotMap;
                } finally {
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Could not load slot map file", e);
            }
        }
        return new java.util.HashMap();
    }

    private void ensureSlotMapLoaded() {
        if (this.mSlotMap == null) {
            this.mSlotMap = loadSlotMap();
            if (this.mActiveSlots != null) {
                refreshActiveSlots(this.mActiveSlots);
                this.mActiveSlots = null;
            }
        }
    }

    protected void saveSlotMap(java.io.OutputStream stream) throws java.io.IOException {
        if (this.mSlotMap == null) {
            return;
        }
        java.util.Properties props = new java.util.Properties();
        for (java.util.Map.Entry<java.lang.Integer, java.lang.String> entry : this.mSlotMap.entrySet()) {
            props.setProperty(entry.getKey().toString(), entry.getValue());
        }
        props.store(stream, "");
    }

    private void saveSlotMap() {
        if (this.mSlotMap == null) {
            return;
        }
        if (!getSlotMapFile().getParentFile().exists()) {
            android.util.Slog.w(TAG, "Not saving slot map, " + getSlotMapDir() + " does not exist");
            return;
        }
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(getSlotMapFile());
            try {
                saveSlotMap(fos);
                fos.close();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "failed to save password slot map", e);
        }
    }
}
