package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
abstract class MediaRoute2Provider {
    com.android.server.media.MediaRoute2Provider.Callback mCallback;
    final android.content.ComponentName mComponentName;
    boolean mIsSystemRouteProvider;
    private volatile android.media.MediaRoute2ProviderInfo mProviderInfo;
    final java.lang.String mUniqueId;
    final java.lang.Object mLock = new java.lang.Object();
    final java.util.List<android.media.RoutingSessionInfo> mSessionInfos = new java.util.ArrayList();

    public interface Callback {
        void onProviderStateChanged(com.android.server.media.MediaRoute2Provider mediaRoute2Provider);

        void onRequestFailed(com.android.server.media.MediaRoute2Provider mediaRoute2Provider, long j, int i);

        void onSessionCreated(com.android.server.media.MediaRoute2Provider mediaRoute2Provider, long j, android.media.RoutingSessionInfo routingSessionInfo);

        void onSessionReleased(com.android.server.media.MediaRoute2Provider mediaRoute2Provider, android.media.RoutingSessionInfo routingSessionInfo);

        void onSessionUpdated(com.android.server.media.MediaRoute2Provider mediaRoute2Provider, android.media.RoutingSessionInfo routingSessionInfo);
    }

    public abstract void deselectRoute(long j, java.lang.String str, java.lang.String str2);

    protected abstract java.lang.String getDebugString();

    public abstract void prepareReleaseSession(java.lang.String str);

    public abstract void releaseSession(long j, java.lang.String str);

    public abstract void requestCreateSession(long j, java.lang.String str, java.lang.String str2, android.os.Bundle bundle, int i, android.os.UserHandle userHandle, java.lang.String str3);

    public abstract void selectRoute(long j, java.lang.String str, java.lang.String str2);

    public abstract void setRouteVolume(long j, java.lang.String str, int i);

    public abstract void setSessionVolume(long j, java.lang.String str, int i);

    public abstract void transferToRoute(long j, android.os.UserHandle userHandle, java.lang.String str, java.lang.String str2, java.lang.String str3, int i);

    public abstract void updateDiscoveryPreference(java.util.Set<java.lang.String> set, android.media.RouteDiscoveryPreference routeDiscoveryPreference);

    MediaRoute2Provider(android.content.ComponentName componentName) {
        this.mComponentName = (android.content.ComponentName) java.util.Objects.requireNonNull(componentName, "Component name must not be null.");
        this.mUniqueId = componentName.flattenToShortString();
    }

    public void setCallback(com.android.server.media.MediaRoute2Provider.Callback callback) {
        this.mCallback = callback;
    }

    public java.lang.String getUniqueId() {
        return this.mUniqueId;
    }

    public android.media.MediaRoute2ProviderInfo getProviderInfo() {
        return this.mProviderInfo;
    }

    public java.util.List<android.media.RoutingSessionInfo> getSessionInfos() {
        java.util.ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new java.util.ArrayList(this.mSessionInfos);
        }
        return arrayList;
    }

    void setProviderState(android.media.MediaRoute2ProviderInfo providerInfo) {
        if (providerInfo == null) {
            this.mProviderInfo = null;
        } else {
            this.mProviderInfo = new android.media.MediaRoute2ProviderInfo.Builder(providerInfo).setUniqueId(this.mComponentName.getPackageName(), this.mUniqueId).setSystemRouteProvider(this.mIsSystemRouteProvider).build();
        }
    }

    void notifyProviderState() {
        if (this.mCallback != null) {
            this.mCallback.onProviderStateChanged(this);
        }
    }

    void setAndNotifyProviderState(android.media.MediaRoute2ProviderInfo providerInfo) {
        setProviderState(providerInfo);
        notifyProviderState();
    }

    public boolean hasComponentName(java.lang.String packageName, java.lang.String className) {
        return this.mComponentName.getPackageName().equals(packageName) && this.mComponentName.getClassName().equals(className);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + getDebugString());
        java.lang.String prefix2 = prefix + "  ";
        if (this.mProviderInfo == null) {
            pw.println(prefix2 + "<provider info not received, yet>");
        } else if (this.mProviderInfo.getRoutes().isEmpty()) {
            pw.println(prefix2 + "<provider info has no routes>");
        } else {
            for (android.media.MediaRoute2Info route : this.mProviderInfo.getRoutes()) {
                pw.printf("%s%s | %s\n", prefix2, route.getId(), route.getName());
            }
        }
        pw.println(prefix2 + "Active routing sessions:");
        synchronized (this.mLock) {
            if (this.mSessionInfos.isEmpty()) {
                pw.println(prefix2 + "  <no active routing sessions>");
            } else {
                for (android.media.RoutingSessionInfo routingSessionInfo : this.mSessionInfos) {
                    routingSessionInfo.dump(pw, prefix2 + "  ");
                }
            }
        }
    }

    public java.lang.String toString() {
        return getDebugString();
    }

    protected static class SessionCreationOrTransferRequest {
        public final long mRequestId;
        public final java.lang.String mTargetOriginalRouteId;
        public final java.lang.String mTransferInitiatorPackageName;
        public final android.os.UserHandle mTransferInitiatorUserHandle;
        public final int mTransferReason;

        SessionCreationOrTransferRequest(long requestId, java.lang.String targetOriginalRouteId, int transferReason, android.os.UserHandle transferInitiatorUserHandle, java.lang.String transferInitiatorPackageName) {
            this.mRequestId = requestId;
            this.mTargetOriginalRouteId = targetOriginalRouteId;
            this.mTransferReason = transferReason;
            this.mTransferInitiatorUserHandle = transferInitiatorUserHandle;
            this.mTransferInitiatorPackageName = transferInitiatorPackageName;
        }

        public boolean isTargetRoute(android.media.MediaRoute2Info route2Info) {
            return route2Info != null && this.mTargetOriginalRouteId.equals(route2Info.getOriginalId());
        }

        public boolean isTargetRouteIdInRouteOriginalIdList(java.util.List<java.lang.String> originalRouteIdList) {
            java.util.stream.Stream<java.lang.String> stream = originalRouteIdList.stream();
            java.lang.String str = this.mTargetOriginalRouteId;
            java.util.Objects.requireNonNull(str);
            return stream.anyMatch(new com.android.server.media.MediaRoute2Provider$SessionCreationOrTransferRequest$$ExternalSyntheticLambda1(str));
        }

        public boolean isTargetRouteIdInRouteUniqueIdList(java.util.List<java.lang.String> uniqueRouteIdList) {
            java.util.stream.Stream<R> map = uniqueRouteIdList.stream().map(new java.util.function.Function() { // from class: com.android.server.media.MediaRoute2Provider$SessionCreationOrTransferRequest$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.media.MediaRouter2Utils.getOriginalId((java.lang.String) obj);
                }
            });
            java.lang.String str = this.mTargetOriginalRouteId;
            java.util.Objects.requireNonNull(str);
            return map.anyMatch(new com.android.server.media.MediaRoute2Provider$SessionCreationOrTransferRequest$$ExternalSyntheticLambda1(str));
        }
    }
}
