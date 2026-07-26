package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
class IdmapDaemon {
    private static final java.lang.String IDMAP_DAEMON = "idmap2d";
    private static final int SERVICE_CONNECT_INTERVAL_SLEEP_MS = 5;
    private static final int SERVICE_CONNECT_UPTIME_TIMEOUT_MS = 5000;
    private static final int SERVICE_CONNECT_WALLTIME_TIMEOUT_MS = 30000;
    private static final int SERVICE_TIMEOUT_MS = 10000;
    private static com.android.server.om.IdmapDaemon sInstance;
    private volatile android.os.IIdmap2 mService;
    private final java.util.concurrent.atomic.AtomicInteger mOpenedCount = new java.util.concurrent.atomic.AtomicInteger();
    private final java.lang.Object mIdmapToken = new java.lang.Object();

    IdmapDaemon() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Connection implements java.lang.AutoCloseable {
        private final android.os.IIdmap2 mIdmap2;
        private boolean mOpened;

        private Connection(android.os.IIdmap2 idmap2) {
            this.mOpened = true;
            synchronized (com.android.server.om.IdmapDaemon.this.mIdmapToken) {
                com.android.server.om.IdmapDaemon.this.mOpenedCount.incrementAndGet();
                this.mIdmap2 = idmap2;
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            synchronized (com.android.server.om.IdmapDaemon.this.mIdmapToken) {
                if (this.mOpened) {
                    this.mOpened = false;
                    if (com.android.server.om.IdmapDaemon.this.mOpenedCount.decrementAndGet() != 0) {
                        return;
                    }
                    com.android.server.FgThread.getHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.om.IdmapDaemon$Connection$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$close$0();
                        }
                    }, com.android.server.om.IdmapDaemon.this.mIdmapToken, 10000L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$close$0() {
            synchronized (com.android.server.om.IdmapDaemon.this.mIdmapToken) {
                if (com.android.server.om.IdmapDaemon.this.mService != null && com.android.server.om.IdmapDaemon.this.mOpenedCount.get() == 0) {
                    com.android.server.om.IdmapDaemon.stopIdmapService();
                    com.android.server.om.IdmapDaemon.this.mService = null;
                }
            }
        }

        public android.os.IIdmap2 getIdmap2() {
            return this.mIdmap2;
        }
    }

    static com.android.server.om.IdmapDaemon getInstance() {
        if (sInstance == null) {
            sInstance = new com.android.server.om.IdmapDaemon();
        }
        return sInstance;
    }

    java.lang.String createIdmap(java.lang.String targetPath, java.lang.String overlayPath, java.lang.String overlayName, int policies, boolean enforce, int userId) throws java.lang.Throwable {
        com.android.server.om.IdmapDaemon.Connection c = connect();
        try {
            android.os.IIdmap2 idmap2 = c.getIdmap2();
            try {
                if (idmap2 == null) {
                    try {
                        try {
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                    try {
                        try {
                            try {
                                android.util.Slog.w("OverlayManager", "idmap2d service is not ready for createIdmap(\"" + targetPath + "\", \"" + overlayPath + "\", \"" + overlayName + "\", " + policies + ", " + enforce + ", " + userId + ")");
                                if (c != null) {
                                    c.close();
                                    return null;
                                }
                                return null;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                            }
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                        }
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                    }
                } else {
                    java.lang.String strCreateIdmap = idmap2.createIdmap(targetPath, overlayPath, android.text.TextUtils.emptyIfNull(overlayName), policies, enforce, userId);
                    if (c != null) {
                        c.close();
                    }
                    return strCreateIdmap;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
        } catch (java.lang.Throwable th7) {
            th = th7;
        }
        java.lang.Throwable th8 = th;
        if (c == null) {
            throw th8;
        }
        try {
            c.close();
            throw th8;
        } catch (java.lang.Throwable th9) {
            th8.addSuppressed(th9);
            throw th8;
        }
    }

    boolean removeIdmap(java.lang.String overlayPath, int userId) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
        com.android.server.om.IdmapDaemon.Connection c = connect();
        try {
            android.os.IIdmap2 idmap2 = c.getIdmap2();
            if (idmap2 == null) {
                android.util.Slog.w("OverlayManager", "idmap2d service is not ready for removeIdmap(\"" + overlayPath + "\", " + userId + ")");
                if (c != null) {
                    c.close();
                    return false;
                }
                return false;
            }
            boolean zRemoveIdmap = idmap2.removeIdmap(overlayPath, userId);
            if (c != null) {
                c.close();
            }
            return zRemoveIdmap;
        } catch (java.lang.Throwable th) {
            if (c != null) {
                try {
                    c.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    boolean verifyIdmap(java.lang.String targetPath, java.lang.String overlayPath, java.lang.String overlayName, int policies, boolean enforce, int userId) throws java.lang.Exception {
        com.android.server.om.IdmapDaemon.Connection c = connect();
        try {
            android.os.IIdmap2 idmap2 = c.getIdmap2();
            try {
                if (idmap2 == null) {
                    try {
                        try {
                        } catch (java.lang.Throwable th) {
                            th = th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                    try {
                        try {
                            try {
                                android.util.Slog.w("OverlayManager", "idmap2d service is not ready for verifyIdmap(\"" + targetPath + "\", \"" + overlayPath + "\", \"" + overlayName + "\", " + policies + ", " + enforce + ", " + userId + ")");
                                if (c != null) {
                                    c.close();
                                    return false;
                                }
                                return false;
                            } catch (java.lang.Throwable th3) {
                                th = th3;
                            }
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                        }
                    } catch (java.lang.Throwable th5) {
                        th = th5;
                    }
                } else {
                    boolean zVerifyIdmap = idmap2.verifyIdmap(targetPath, overlayPath, android.text.TextUtils.emptyIfNull(overlayName), policies, enforce, userId);
                    if (c != null) {
                        c.close();
                    }
                    return zVerifyIdmap;
                }
            } catch (java.lang.Throwable th6) {
                th = th6;
            }
        } catch (java.lang.Throwable th7) {
            th = th7;
        }
        java.lang.Throwable th8 = th;
        if (c == null) {
            throw th8;
        }
        try {
            c.close();
            throw th8;
        } catch (java.lang.Throwable th9) {
            th8.addSuppressed(th9);
            throw th8;
        }
    }

    boolean idmapExists(java.lang.String overlayPath, int userId) {
        try {
            com.android.server.om.IdmapDaemon.Connection c = connect();
            try {
                android.os.IIdmap2 idmap2 = c.getIdmap2();
                if (idmap2 == null) {
                    android.util.Slog.w("OverlayManager", "idmap2d service is not ready for idmapExists(\"" + overlayPath + "\", " + userId + ")");
                    if (c != null) {
                        c.close();
                    }
                    return false;
                }
                boolean zIsFile = new java.io.File(idmap2.getIdmapPath(overlayPath, userId)).isFile();
                if (c != null) {
                    c.close();
                }
                return zIsFile;
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf("OverlayManager", "failed to check if idmap exists for " + overlayPath, e);
            return false;
        }
    }

    android.os.FabricatedOverlayInfo createFabricatedOverlay(android.os.FabricatedOverlayInternal overlay) {
        try {
            com.android.server.om.IdmapDaemon.Connection c = connect();
            try {
                android.os.IIdmap2 idmap2 = c.getIdmap2();
                if (idmap2 == null) {
                    android.util.Slog.w("OverlayManager", "idmap2d service is not ready for createFabricatedOverlay()");
                    if (c != null) {
                        c.close();
                    }
                    return null;
                }
                android.os.FabricatedOverlayInfo fabricatedOverlayInfoCreateFabricatedOverlay = idmap2.createFabricatedOverlay(overlay);
                if (c != null) {
                    c.close();
                }
                return fabricatedOverlayInfoCreateFabricatedOverlay;
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf("OverlayManager", "failed to fabricate overlay " + overlay, e);
            return null;
        }
    }

    boolean deleteFabricatedOverlay(java.lang.String path) {
        try {
            com.android.server.om.IdmapDaemon.Connection c = connect();
            try {
                android.os.IIdmap2 idmap2 = c.getIdmap2();
                if (idmap2 == null) {
                    android.util.Slog.w("OverlayManager", "idmap2d service is not ready for deleteFabricatedOverlay(\"" + path + "\")");
                    if (c != null) {
                        c.close();
                    }
                    return false;
                }
                boolean zDeleteFabricatedOverlay = idmap2.deleteFabricatedOverlay(path);
                if (c != null) {
                    c.close();
                }
                return zDeleteFabricatedOverlay;
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf("OverlayManager", "failed to delete fabricated overlay '" + path + "'", e);
            return false;
        }
    }

    synchronized java.util.List<android.os.FabricatedOverlayInfo> getFabricatedOverlayInfos() {
        java.util.ArrayList<android.os.FabricatedOverlayInfo> allInfos = new java.util.ArrayList<>();
        com.android.server.om.IdmapDaemon.Connection c = null;
        try {
            try {
                c = connect();
                android.os.IIdmap2 service = c.getIdmap2();
                if (service == null) {
                    android.util.Slog.w("OverlayManager", "idmap2d service is not ready for getFabricatedOverlayInfos()");
                    return java.util.Collections.emptyList();
                }
                int iteratorId = service.acquireFabricatedOverlayIterator();
                while (true) {
                    java.util.List<android.os.FabricatedOverlayInfo> infos = service.nextFabricatedOverlayInfos(iteratorId);
                    if (infos.isEmpty()) {
                        break;
                    }
                    allInfos.addAll(infos);
                }
                if (c != null) {
                    try {
                        if (c.getIdmap2() != null && iteratorId != -1) {
                            c.getIdmap2().releaseFabricatedOverlayIterator(iteratorId);
                        }
                    } catch (android.os.RemoteException e) {
                    }
                    c.close();
                }
                return allInfos;
            } finally {
                if (c != null) {
                    try {
                        if (c.getIdmap2() != null && -1 != -1) {
                            c.getIdmap2().releaseFabricatedOverlayIterator(-1);
                        }
                    } catch (android.os.RemoteException e2) {
                    }
                    c.close();
                }
            }
        } catch (java.lang.Exception e3) {
            android.util.Slog.wtf("OverlayManager", "failed to get all fabricated overlays", e3);
            if (c != null) {
                try {
                    if (c.getIdmap2() != null && -1 != -1) {
                        c.getIdmap2().releaseFabricatedOverlayIterator(-1);
                    }
                } catch (android.os.RemoteException e4) {
                }
                c.close();
            }
            return allInfos;
        }
    }

    java.lang.String dumpIdmap(java.lang.String overlayPath) {
        try {
            com.android.server.om.IdmapDaemon.Connection c = connect();
            try {
                android.os.IIdmap2 service = c.getIdmap2();
                if (service == null) {
                    android.util.Slog.w("OverlayManager", "idmap2d service is not ready for dumpIdmap()");
                    if (c != null) {
                        c.close();
                    }
                    return "idmap2d service is not ready for dumpIdmap()";
                }
                java.lang.String dump = service.dumpIdmap(overlayPath);
                java.lang.String strNullIfEmpty = android.text.TextUtils.nullIfEmpty(dump);
                if (c != null) {
                    c.close();
                }
                return strNullIfEmpty;
            } finally {
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.wtf("OverlayManager", "failed to dump idmap", e);
            return null;
        }
    }

    private android.os.IBinder getIdmapService() throws java.util.concurrent.TimeoutException, android.os.RemoteException {
        long uptimeMillis;
        long jElapsedRealtime;
        try {
            android.os.SystemService.start(IDMAP_DAEMON);
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.wtf("OverlayManager", "Failed to enable idmap2 daemon", e);
            if (e.getMessage().contains("failed to set system property")) {
                return null;
            }
        }
        long endUptimeMillis = android.os.SystemClock.uptimeMillis() + 5000;
        long walltimeMillis = android.os.SystemClock.elapsedRealtime();
        long endWalltimeMillis = walltimeMillis + 30000;
        do {
            android.os.IBinder binder = android.os.ServiceManager.getService("idmap");
            if (binder != null) {
                binder.linkToDeath(new android.os.IBinder.DeathRecipient() { // from class: com.android.server.om.IdmapDaemon$$ExternalSyntheticLambda0
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        android.util.Slog.w("OverlayManager", android.text.TextUtils.formatSimple("service '%s' died", new java.lang.Object[]{"idmap"}));
                    }
                }, 0);
                return binder;
            }
            android.os.SystemClock.sleep(5L);
            uptimeMillis = android.os.SystemClock.uptimeMillis();
            if (uptimeMillis > endUptimeMillis) {
                break;
            }
            jElapsedRealtime = android.os.SystemClock.elapsedRealtime();
            walltimeMillis = jElapsedRealtime;
        } while (jElapsedRealtime <= endWalltimeMillis);
        throw new java.util.concurrent.TimeoutException(android.text.TextUtils.formatSimple("Failed to connect to '%s' in %d/%d ms (spent %d/%d ms)", new java.lang.Object[]{"idmap", 5000, 30000, java.lang.Long.valueOf((uptimeMillis - endUptimeMillis) + 5000), java.lang.Long.valueOf((walltimeMillis - endWalltimeMillis) + 30000)}));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void stopIdmapService() {
        try {
            android.os.SystemService.stop(IDMAP_DAEMON);
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.w("OverlayManager", "Failed to disable idmap2 daemon", e);
        }
    }

    private com.android.server.om.IdmapDaemon.Connection connect() throws java.util.concurrent.TimeoutException, android.os.RemoteException {
        synchronized (this.mIdmapToken) {
            com.android.server.FgThread.getHandler().removeCallbacksAndMessages(this.mIdmapToken);
            com.android.server.om.IdmapDaemon.ConnectionIA connectionIA = null;
            if (this.mService != null) {
                return new com.android.server.om.IdmapDaemon.Connection(this.mService);
            }
            android.os.IBinder binder = getIdmapService();
            if (binder == null) {
                return new com.android.server.om.IdmapDaemon.Connection(connectionIA);
            }
            this.mService = android.os.IIdmap2.Stub.asInterface(binder);
            return new com.android.server.om.IdmapDaemon.Connection(this.mService);
        }
    }
}
