package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public class FollowerBrightnessStrategy implements com.android.server.display.brightness.strategy.DisplayBrightnessStrategy {
    private float mBrightnessToFollow = Float.NaN;
    private boolean mBrightnessToFollowSlowChange = false;
    private final int mDisplayId;

    public FollowerBrightnessStrategy(int displayId) {
        this.mDisplayId = displayId;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest) {
        return com.android.server.display.brightness.BrightnessUtils.constructDisplayBrightnessState(10, this.mBrightnessToFollow, this.mBrightnessToFollow, getName(), this.mBrightnessToFollowSlowChange);
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public java.lang.String getName() {
        return "FollowerBrightnessStrategy";
    }

    public float getBrightnessToFollow() {
        return this.mBrightnessToFollow;
    }

    public void setBrightnessToFollow(float brightnessToFollow, boolean slowChange) {
        this.mBrightnessToFollow = brightnessToFollow;
        this.mBrightnessToFollowSlowChange = slowChange;
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void dump(java.io.PrintWriter writer) {
        writer.println("FollowerBrightnessStrategy:");
        writer.println("  mDisplayId=" + this.mDisplayId);
        writer.println("  mBrightnessToFollow:" + this.mBrightnessToFollow);
        writer.println("  mBrightnessToFollowSlowChange:" + this.mBrightnessToFollowSlowChange);
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest) {
    }

    @Override // com.android.server.display.brightness.strategy.DisplayBrightnessStrategy
    public int getReason() {
        return 10;
    }
}
