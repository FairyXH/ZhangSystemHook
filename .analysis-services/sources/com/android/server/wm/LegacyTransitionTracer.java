package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class LegacyTransitionTracer implements com.android.server.wm.TransitionTracer {
    private static final int ACTIVE_TRACING_BUFFER_CAPACITY = 5120000;
    private static final int ALWAYS_ON_TRACING_CAPACITY = 15360;
    private static final int CHUNK_SIZE = 64;
    private static final java.lang.String LOG_TAG = "TransitionTracer";
    private static final long MAGIC_NUMBER_VALUE = 4990904633914184276L;
    private static final java.lang.String TRACE_FILE = "/data/misc/wmtrace/wm_transition_trace.winscope";
    static final java.lang.String WINSCOPE_EXT = ".winscope";
    private final com.android.internal.util.TraceBuffer mTraceBuffer = new com.android.internal.util.TraceBuffer(ALWAYS_ON_TRACING_CAPACITY);
    private final java.lang.Object mEnabledLock = new java.lang.Object();
    private volatile boolean mActiveTracingEnabled = false;

    LegacyTransitionTracer() {
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logSentTransition(com.android.server.wm.Transition transition, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets) {
        try {
            android.util.proto.ProtoOutputStream outputStream = new android.util.proto.ProtoOutputStream(64);
            long protoToken = outputStream.start(2246267895810L);
            outputStream.write(1120986464257L, transition.getSyncId());
            outputStream.write(1112396529668L, transition.mLogger.mCreateTimeNs);
            outputStream.write(1112396529669L, transition.mLogger.mSendTimeNs);
            outputStream.write(1116691496962L, transition.getStartTransaction().getId());
            outputStream.write(1116691496963L, transition.getFinishTransaction().getId());
            dumpTransitionTargetsToProto(outputStream, transition, targets);
            outputStream.end(protoToken);
            this.mTraceBuffer.add(outputStream);
        } catch (java.lang.Exception e) {
            android.util.Log.e(LOG_TAG, "Unexpected exception thrown while logging transitions", e);
        }
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logFinishedTransition(com.android.server.wm.Transition transition) {
        try {
            android.util.proto.ProtoOutputStream outputStream = new android.util.proto.ProtoOutputStream(64);
            long protoToken = outputStream.start(2246267895810L);
            outputStream.write(1120986464257L, transition.getSyncId());
            outputStream.write(1112396529670L, transition.mLogger.mFinishTimeNs);
            outputStream.end(protoToken);
            this.mTraceBuffer.add(outputStream);
        } catch (java.lang.Exception e) {
            android.util.Log.e(LOG_TAG, "Unexpected exception thrown while logging transitions", e);
        }
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logAbortedTransition(com.android.server.wm.Transition transition) {
        try {
            android.util.proto.ProtoOutputStream outputStream = new android.util.proto.ProtoOutputStream(64);
            long protoToken = outputStream.start(2246267895810L);
            outputStream.write(1120986464257L, transition.getSyncId());
            outputStream.write(1112396529674L, transition.mLogger.mAbortTimeNs);
            outputStream.end(protoToken);
            this.mTraceBuffer.add(outputStream);
        } catch (java.lang.Exception e) {
            android.util.Log.e(LOG_TAG, "Unexpected exception thrown while logging transitions", e);
        }
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logRemovingStartingWindow(com.android.server.wm.StartingData startingData) {
        if (startingData.mTransitionId == 0) {
            return;
        }
        try {
            android.util.proto.ProtoOutputStream outputStream = new android.util.proto.ProtoOutputStream(64);
            long protoToken = outputStream.start(2246267895810L);
            outputStream.write(1120986464257L, startingData.mTransitionId);
            outputStream.write(1112396529675L, android.os.SystemClock.elapsedRealtimeNanos());
            outputStream.end(protoToken);
            this.mTraceBuffer.add(outputStream);
        } catch (java.lang.Exception e) {
            android.util.Log.e(LOG_TAG, "Unexpected exception thrown while logging transitions", e);
        }
    }

    private void dumpTransitionTargetsToProto(android.util.proto.ProtoOutputStream outputStream, com.android.server.wm.Transition transition, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets) {
        int layerId;
        android.os.Trace.beginSection("TransitionTracer#dumpTransitionTargetsToProto");
        if (this.mActiveTracingEnabled) {
            outputStream.write(1120986464257L, transition.getSyncId());
        }
        outputStream.write(1120986464263L, transition.mType);
        outputStream.write(1120986464265L, transition.getFlags());
        for (int i = 0; i < targets.size(); i++) {
            long changeToken = outputStream.start(2246267895816L);
            com.android.server.wm.Transition.ChangeInfo target = targets.get(i);
            if (target.mContainer.mSurfaceControl.isValid()) {
                layerId = target.mContainer.mSurfaceControl.getLayerId();
            } else {
                layerId = -1;
            }
            outputStream.write(1120986464257L, target.mReadyMode);
            outputStream.write(1120986464260L, target.mReadyFlags);
            outputStream.write(1120986464258L, layerId);
            if (this.mActiveTracingEnabled) {
                int windowId = java.lang.System.identityHashCode(target.mContainer);
                outputStream.write(1120986464259L, windowId);
            }
            outputStream.end(changeToken);
        }
        android.os.Trace.endSection();
    }

    @Override // com.android.server.wm.TransitionTracer
    public void startTrace(java.io.PrintWriter pw) {
        if (android.os.Build.IS_USER) {
            com.android.server.wm.LegacyTransitionTracer.LogAndPrintln.e(pw, "Tracing is not supported on user builds.");
            return;
        }
        android.os.Trace.beginSection("TransitionTracer#startTrace");
        com.android.server.wm.LegacyTransitionTracer.LogAndPrintln.i(pw, "Starting shell transition trace.");
        synchronized (this.mEnabledLock) {
            this.mActiveTracingEnabled = true;
            this.mTraceBuffer.resetBuffer();
            this.mTraceBuffer.setCapacity(ACTIVE_TRACING_BUFFER_CAPACITY);
        }
        android.os.Trace.endSection();
    }

    @Override // com.android.server.wm.TransitionTracer
    public void stopTrace(java.io.PrintWriter pw) {
        stopTrace(pw, new java.io.File(TRACE_FILE));
    }

    public void stopTrace(java.io.PrintWriter pw, java.io.File outputFile) {
        if (android.os.Build.IS_USER) {
            com.android.server.wm.LegacyTransitionTracer.LogAndPrintln.e(pw, "Tracing is not supported on user builds.");
            return;
        }
        android.os.Trace.beginSection("TransitionTracer#stopTrace");
        com.android.server.wm.LegacyTransitionTracer.LogAndPrintln.i(pw, "Stopping shell transition trace.");
        synchronized (this.mEnabledLock) {
            this.mActiveTracingEnabled = false;
            writeTraceToFileLocked(pw, outputFile);
            this.mTraceBuffer.resetBuffer();
            this.mTraceBuffer.setCapacity(ALWAYS_ON_TRACING_CAPACITY);
        }
        android.os.Trace.endSection();
    }

    @Override // com.android.server.wm.TransitionTracer
    public void saveForBugreport(java.io.PrintWriter pw) {
        if (android.os.Build.IS_USER) {
            com.android.server.wm.LegacyTransitionTracer.LogAndPrintln.e(pw, "Tracing is not supported on user builds.");
            return;
        }
        android.os.Trace.beginSection("TransitionTracer#saveForBugreport");
        synchronized (this.mEnabledLock) {
            java.io.File outputFile = new java.io.File(TRACE_FILE);
            writeTraceToFileLocked(pw, outputFile);
        }
        android.os.Trace.endSection();
    }

    @Override // com.android.server.wm.TransitionTracer
    public boolean isTracing() {
        return this.mActiveTracingEnabled;
    }

    private void writeTraceToFileLocked(java.io.PrintWriter pw, java.io.File file) {
        android.os.Trace.beginSection("TransitionTracer#writeTraceToFileLocked");
        try {
            android.util.proto.ProtoOutputStream proto = new android.util.proto.ProtoOutputStream(64);
            proto.write(1125281431553L, MAGIC_NUMBER_VALUE);
            long timeOffsetNs = java.util.concurrent.TimeUnit.MILLISECONDS.toNanos(java.lang.System.currentTimeMillis()) - android.os.SystemClock.elapsedRealtimeNanos();
            proto.write(1125281431555L, timeOffsetNs);
            int pid = android.os.Process.myPid();
            com.android.server.wm.LegacyTransitionTracer.LogAndPrintln.i(pw, "Writing file to " + file.getAbsolutePath() + " from process " + pid);
            this.mTraceBuffer.writeTraceToFile(file, proto);
        } catch (java.io.IOException e) {
            com.android.server.wm.LegacyTransitionTracer.LogAndPrintln.e(pw, "Unable to write buffer to file", e);
        }
        android.os.Trace.endSection();
    }

    private static class LogAndPrintln {
        private LogAndPrintln() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void i(java.io.PrintWriter pw, java.lang.String msg) {
            android.util.Log.i(com.android.server.wm.LegacyTransitionTracer.LOG_TAG, msg);
            if (pw != null) {
                pw.println(msg);
                pw.flush();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void e(java.io.PrintWriter pw, java.lang.String msg) {
            android.util.Log.e(com.android.server.wm.LegacyTransitionTracer.LOG_TAG, msg);
            if (pw != null) {
                pw.println("ERROR: " + msg);
                pw.flush();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void e(java.io.PrintWriter pw, java.lang.String msg, java.lang.Exception e) {
            android.util.Log.e(com.android.server.wm.LegacyTransitionTracer.LOG_TAG, msg, e);
            if (pw != null) {
                pw.println("ERROR: " + msg + " ::\n " + e);
                pw.flush();
            }
        }
    }
}
