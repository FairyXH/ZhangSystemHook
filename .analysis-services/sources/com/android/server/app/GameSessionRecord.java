package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameSessionRecord {
    private final android.service.games.IGameSession mIGameSession;
    private final android.content.ComponentName mRootComponentName;
    private final com.android.server.app.GameSessionRecord.State mState;
    private final android.view.SurfaceControlViewHost.SurfacePackage mSurfacePackage;
    private final int mTaskId;

    private enum State {
        NO_GAME_SESSION_REQUESTED,
        GAME_SESSION_REQUESTED,
        GAME_SESSION_ATTACHED,
        GAME_SESSION_ENDED_PROCESS_DEATH
    }

    static com.android.server.app.GameSessionRecord awaitingGameSessionRequest(int taskId, android.content.ComponentName rootComponentName) {
        return new com.android.server.app.GameSessionRecord(taskId, com.android.server.app.GameSessionRecord.State.NO_GAME_SESSION_REQUESTED, rootComponentName, null, null);
    }

    private GameSessionRecord(int taskId, com.android.server.app.GameSessionRecord.State state, android.content.ComponentName rootComponentName, android.service.games.IGameSession gameSession, android.view.SurfaceControlViewHost.SurfacePackage surfacePackage) {
        this.mTaskId = taskId;
        this.mState = state;
        this.mRootComponentName = rootComponentName;
        this.mIGameSession = gameSession;
        this.mSurfacePackage = surfacePackage;
    }

    public boolean isAwaitingGameSessionRequest() {
        return this.mState == com.android.server.app.GameSessionRecord.State.NO_GAME_SESSION_REQUESTED;
    }

    public com.android.server.app.GameSessionRecord withGameSessionRequested() {
        return new com.android.server.app.GameSessionRecord(this.mTaskId, com.android.server.app.GameSessionRecord.State.GAME_SESSION_REQUESTED, this.mRootComponentName, null, null);
    }

    public boolean isGameSessionRequested() {
        return this.mState == com.android.server.app.GameSessionRecord.State.GAME_SESSION_REQUESTED;
    }

    public com.android.server.app.GameSessionRecord withGameSession(android.service.games.IGameSession gameSession, android.view.SurfaceControlViewHost.SurfacePackage surfacePackage) {
        java.util.Objects.requireNonNull(gameSession);
        return new com.android.server.app.GameSessionRecord(this.mTaskId, com.android.server.app.GameSessionRecord.State.GAME_SESSION_ATTACHED, this.mRootComponentName, gameSession, surfacePackage);
    }

    public com.android.server.app.GameSessionRecord withGameSessionEndedOnProcessDeath() {
        return new com.android.server.app.GameSessionRecord(this.mTaskId, com.android.server.app.GameSessionRecord.State.GAME_SESSION_ENDED_PROCESS_DEATH, this.mRootComponentName, null, null);
    }

    public boolean isGameSessionEndedForProcessDeath() {
        return this.mState == com.android.server.app.GameSessionRecord.State.GAME_SESSION_ENDED_PROCESS_DEATH;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public android.content.ComponentName getComponentName() {
        return this.mRootComponentName;
    }

    public android.service.games.IGameSession getGameSession() {
        return this.mIGameSession;
    }

    public android.view.SurfaceControlViewHost.SurfacePackage getSurfacePackage() {
        return this.mSurfacePackage;
    }

    public java.lang.String toString() {
        return "GameSessionRecord{mTaskId=" + this.mTaskId + ", mState=" + this.mState + ", mRootComponentName=" + this.mRootComponentName + ", mIGameSession=" + this.mIGameSession + ", mSurfacePackage=" + this.mSurfacePackage + '}';
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof com.android.server.app.GameSessionRecord)) {
            return false;
        }
        com.android.server.app.GameSessionRecord that = (com.android.server.app.GameSessionRecord) o;
        return this.mTaskId == that.mTaskId && this.mState == that.mState && this.mRootComponentName.equals(that.mRootComponentName) && java.util.Objects.equals(this.mIGameSession, that.mIGameSession) && java.util.Objects.equals(this.mSurfacePackage, that.mSurfacePackage);
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mTaskId), this.mState, this.mRootComponentName, this.mIGameSession, this.mState, this.mSurfacePackage);
    }
}
