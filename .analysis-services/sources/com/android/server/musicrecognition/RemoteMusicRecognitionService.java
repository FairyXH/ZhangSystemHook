package com.android.server.musicrecognition;

/* JADX INFO: loaded from: classes2.dex */
public class RemoteMusicRecognitionService extends com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService<com.android.server.musicrecognition.RemoteMusicRecognitionService, android.media.musicrecognition.IMusicRecognitionService> {
    private static final long TIMEOUT_IDLE_BIND_MILLIS = 40000;
    private final com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.MusicRecognitionServiceCallback mServerCallback;

    interface Callbacks extends com.android.internal.infra.AbstractRemoteService.VultureCallback<com.android.server.musicrecognition.RemoteMusicRecognitionService> {
    }

    public RemoteMusicRecognitionService(android.content.Context context, android.content.ComponentName serviceName, int userId, com.android.server.musicrecognition.MusicRecognitionManagerPerUserService perUserService, com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.MusicRecognitionServiceCallback callback, boolean bindInstantServiceAllowed, boolean verbose) {
        super(context, "android.service.musicrecognition.MUSIC_RECOGNITION", serviceName, userId, perUserService, context.getMainThreadHandler(), (bindInstantServiceAllowed ? 4194304 : 0) | 4096, verbose, 1);
        this.mServerCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: getServiceInterface, reason: merged with bridge method [inline-methods] */
    public android.media.musicrecognition.IMusicRecognitionService m5492getServiceInterface(android.os.IBinder service) {
        return android.media.musicrecognition.IMusicRecognitionService.Stub.asInterface(service);
    }

    protected long getTimeoutIdleBindMillis() {
        return TIMEOUT_IDLE_BIND_MILLIS;
    }

    com.android.server.musicrecognition.MusicRecognitionManagerPerUserService.MusicRecognitionServiceCallback getServerCallback() {
        return this.mServerCallback;
    }

    public void onAudioStreamStarted(final android.os.ParcelFileDescriptor fd, final android.media.AudioFormat audioFormat) {
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.musicrecognition.RemoteMusicRecognitionService$$ExternalSyntheticLambda0
            public final void run(android.os.IInterface iInterface) throws android.os.RemoteException {
                this.f$0.lambda$onAudioStreamStarted$0(fd, audioFormat, (android.media.musicrecognition.IMusicRecognitionService) iInterface);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAudioStreamStarted$0(android.os.ParcelFileDescriptor fd, android.media.AudioFormat audioFormat, android.media.musicrecognition.IMusicRecognitionService binder) throws android.os.RemoteException {
        binder.onAudioStreamStarted(fd, audioFormat, this.mServerCallback);
    }

    public java.util.concurrent.CompletableFuture<java.lang.String> getAttributionTag() {
        final java.util.concurrent.CompletableFuture<java.lang.String> attributionTagFuture = new java.util.concurrent.CompletableFuture<>();
        scheduleAsyncRequest(new com.android.internal.infra.AbstractRemoteService.AsyncRequest() { // from class: com.android.server.musicrecognition.RemoteMusicRecognitionService$$ExternalSyntheticLambda1
            public final void run(android.os.IInterface iInterface) throws android.os.RemoteException {
                this.f$0.lambda$getAttributionTag$1(attributionTagFuture, (android.media.musicrecognition.IMusicRecognitionService) iInterface);
            }
        });
        return attributionTagFuture;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getAttributionTag$1(final java.util.concurrent.CompletableFuture attributionTagFuture, android.media.musicrecognition.IMusicRecognitionService binder) throws android.os.RemoteException {
        binder.getAttributionTag(new android.media.musicrecognition.IMusicRecognitionAttributionTagCallback.Stub() { // from class: com.android.server.musicrecognition.RemoteMusicRecognitionService.1
            public void onAttributionTag(java.lang.String tag) throws android.os.RemoteException {
                attributionTagFuture.complete(tag);
            }
        });
    }
}
