package com.android.server.display.brightness.strategy;

/* JADX INFO: loaded from: classes2.dex */
public interface DisplayBrightnessStrategy {
    void dump(java.io.PrintWriter printWriter);

    java.lang.String getName();

    int getReason();

    void strategySelectionPostProcessor(com.android.server.display.brightness.StrategySelectionNotifyRequest strategySelectionNotifyRequest);

    com.android.server.display.DisplayBrightnessState updateBrightness(com.android.server.display.brightness.StrategyExecutionRequest strategyExecutionRequest);
}
