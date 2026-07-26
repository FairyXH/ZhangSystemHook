package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public class LocalColorRepository {
    android.util.ArrayMap<android.os.IBinder, android.util.SparseArray<android.util.ArraySet<android.graphics.RectF>>> mLocalColorAreas = new android.util.ArrayMap<>();
    android.os.RemoteCallbackList<android.app.ILocalWallpaperColorConsumer> mCallbacks = new android.os.RemoteCallbackList<>();

    public void addAreas(final android.app.ILocalWallpaperColorConsumer consumer, java.util.List<android.graphics.RectF> areas, int displayId) {
        android.os.IBinder binder = consumer.asBinder();
        android.util.SparseArray<android.util.ArraySet<android.graphics.RectF>> displays = this.mLocalColorAreas.get(binder);
        android.util.ArraySet<android.graphics.RectF> displayAreas = null;
        if (displays == null) {
            try {
                consumer.asBinder().linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wallpaper.LocalColorRepository$$ExternalSyntheticLambda1
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        this.f$0.lambda$addAreas$0(consumer);
                    }
                }, 0);
            } catch (android.os.RemoteException e) {
                e.printStackTrace();
            }
            displays = new android.util.SparseArray<>();
            this.mLocalColorAreas.put(binder, displays);
        } else {
            android.util.ArraySet<android.graphics.RectF> displayAreas2 = displays.get(displayId);
            displayAreas = displayAreas2;
        }
        if (displayAreas == null) {
            displayAreas = new android.util.ArraySet<>(areas);
            displays.put(displayId, displayAreas);
        }
        for (int i = 0; i < areas.size(); i++) {
            displayAreas.add(areas.get(i));
        }
        this.mCallbacks.register(consumer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addAreas$0(android.app.ILocalWallpaperColorConsumer consumer) {
        this.mLocalColorAreas.remove(consumer.asBinder());
    }

    public java.util.List<android.graphics.RectF> removeAreas(android.app.ILocalWallpaperColorConsumer consumer, java.util.List<android.graphics.RectF> areas, int displayId) {
        android.os.IBinder binder = consumer.asBinder();
        android.util.SparseArray<android.util.ArraySet<android.graphics.RectF>> displays = this.mLocalColorAreas.get(binder);
        if (displays != null) {
            android.util.ArraySet<android.graphics.RectF> registeredAreas = displays.get(displayId);
            if (registeredAreas == null) {
                this.mCallbacks.unregister(consumer);
            } else {
                for (int i = 0; i < areas.size(); i++) {
                    registeredAreas.remove(areas.get(i));
                }
                int i2 = registeredAreas.size();
                if (i2 == 0) {
                    displays.remove(displayId);
                }
            }
            if (displays.size() == 0) {
                this.mLocalColorAreas.remove(binder);
                this.mCallbacks.unregister(consumer);
            }
        } else {
            this.mCallbacks.unregister(consumer);
        }
        android.util.ArraySet<android.graphics.RectF> purged = new android.util.ArraySet<>(areas);
        for (int i3 = 0; i3 < this.mLocalColorAreas.size(); i3++) {
            for (int j = 0; j < this.mLocalColorAreas.valueAt(i3).size(); j++) {
                for (int k = 0; k < this.mLocalColorAreas.valueAt(i3).valueAt(j).size(); k++) {
                    purged.remove(this.mLocalColorAreas.valueAt(i3).valueAt(j).valueAt(k));
                }
            }
        }
        return new java.util.ArrayList(purged);
    }

    public java.util.List<android.graphics.RectF> getAreasByDisplayId(int displayId) {
        android.util.ArraySet<android.graphics.RectF> displayAreas;
        java.util.ArrayList<android.graphics.RectF> areas = new java.util.ArrayList<>();
        for (int i = 0; i < this.mLocalColorAreas.size(); i++) {
            android.util.SparseArray<android.util.ArraySet<android.graphics.RectF>> displays = this.mLocalColorAreas.valueAt(i);
            if (displays != null && (displayAreas = displays.get(displayId)) != null) {
                for (int j = 0; j < displayAreas.size(); j++) {
                    areas.add(displayAreas.valueAt(j));
                }
            }
        }
        return areas;
    }

    public void forEachCallback(final java.util.function.Consumer<android.app.ILocalWallpaperColorConsumer> callback, final android.graphics.RectF area, final int displayId) {
        this.mCallbacks.broadcast(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.LocalColorRepository$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$forEachCallback$1(displayId, area, callback, (android.app.ILocalWallpaperColorConsumer) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forEachCallback$1(int displayId, android.graphics.RectF area, java.util.function.Consumer callback, android.app.ILocalWallpaperColorConsumer cb) {
        android.util.ArraySet<android.graphics.RectF> displayAreas;
        android.os.IBinder binder = cb.asBinder();
        android.util.SparseArray<android.util.ArraySet<android.graphics.RectF>> displays = this.mLocalColorAreas.get(binder);
        if (displays != null && (displayAreas = displays.get(displayId)) != null && displayAreas.contains(area)) {
            callback.accept(cb);
        }
    }

    protected boolean isCallbackAvailable(android.app.ILocalWallpaperColorConsumer callback) {
        return this.mLocalColorAreas.get(callback.asBinder()) != null;
    }
}
