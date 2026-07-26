package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class StateMachine {
    private static final java.lang.String TAG = "StateMachine";
    private final java.util.Queue<com.android.server.wm.utils.StateMachine.Command> mCommands;
    private int mLastRequestedState;
    private int mState;
    private final android.util.SparseArray<com.android.server.wm.utils.StateMachine.Handler> mStateHandlers;
    private final android.util.IntArray mTmp;

    public interface Handler {
        default void enter() {
        }

        default void exit() {
        }

        default boolean handle(int event, java.lang.Object param) {
            return false;
        }
    }

    protected static class Command {
        static final int COMMIT = 1;
        static final int ENTER = 2;
        static final int EXIT = 3;
        final int mState;
        final int mType;

        private Command(int type, int state) {
            this.mType = type;
            com.android.internal.util.AnnotationValidations.validate(android.annotation.IntRange.class, (android.annotation.IntRange) null, state, "from", 0L);
            this.mState = state;
        }

        static com.android.server.wm.utils.StateMachine.Command newCommit(int state) {
            return new com.android.server.wm.utils.StateMachine.Command(1, state);
        }

        static com.android.server.wm.utils.StateMachine.Command newEnter(int state) {
            return new com.android.server.wm.utils.StateMachine.Command(2, state);
        }

        static com.android.server.wm.utils.StateMachine.Command newExit(int state) {
            return new com.android.server.wm.utils.StateMachine.Command(3, state);
        }

        public java.lang.String toString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("Command{ type: ");
            switch (this.mType) {
                case 1:
                    sb.append("commit");
                    break;
                case 2:
                    sb.append("enter");
                    break;
                case 3:
                    sb.append("exit");
                    break;
                default:
                    sb.append("UNKNOWN(");
                    sb.append(this.mType);
                    sb.append(")");
                    break;
            }
            sb.append(" state: ");
            sb.append(java.lang.Integer.toHexString(this.mState));
            sb.append(" }");
            return sb.toString();
        }
    }

    public StateMachine() {
        this(0);
    }

    public StateMachine(int initialState) {
        this.mTmp = new android.util.IntArray();
        this.mStateHandlers = new android.util.SparseArray<>();
        this.mCommands = new java.util.ArrayDeque();
        this.mState = initialState;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.IntRange.class, (android.annotation.IntRange) null, initialState, "from", 0L);
        this.mLastRequestedState = initialState;
    }

    public int getState() {
        return this.mLastRequestedState;
    }

    protected int getCurrentState() {
        return this.mState;
    }

    protected com.android.server.wm.utils.StateMachine.Command[] getCommands() {
        com.android.server.wm.utils.StateMachine.Command[] commands = new com.android.server.wm.utils.StateMachine.Command[this.mCommands.size()];
        this.mCommands.toArray(commands);
        return commands;
    }

    public com.android.server.wm.utils.StateMachine.Handler addStateHandler(int state, com.android.server.wm.utils.StateMachine.Handler handler) {
        com.android.server.wm.utils.StateMachine.Handler handlerOld = this.mStateHandlers.get(state);
        this.mStateHandlers.put(state, handler);
        return handlerOld;
    }

    public void handle(int event, java.lang.Object param) {
        int state = this.mState;
        while (true) {
            com.android.server.wm.utils.StateMachine.Handler h = this.mStateHandlers.get(state);
            if ((h == null || !h.handle(event, param)) && state != 0) {
                state >>= 4;
            } else {
                return;
            }
        }
    }

    protected void enter(int state) {
        com.android.internal.util.AnnotationValidations.validate(android.annotation.IntRange.class, (android.annotation.IntRange) null, state, "from", 0L);
        com.android.server.wm.utils.StateMachine.Handler h = this.mStateHandlers.get(state);
        if (h != null) {
            h.enter();
        }
    }

    protected void exit(int state) {
        com.android.internal.util.AnnotationValidations.validate(android.annotation.IntRange.class, (android.annotation.IntRange) null, state, "from", 0L);
        com.android.server.wm.utils.StateMachine.Handler h = this.mStateHandlers.get(state);
        if (h != null) {
            h.exit();
        }
    }

    public static boolean isIn(int subState, int superState) {
        while (subState > superState) {
            subState >>= 4;
        }
        return subState == superState;
    }

    public boolean isIn(int state) {
        return isIn(this.mLastRequestedState, state);
    }

    public void transit(int newState) {
        com.android.internal.util.AnnotationValidations.validate(android.annotation.IntRange.class, (android.annotation.IntRange) null, newState, "from", 0L);
        this.mCommands.add(com.android.server.wm.utils.StateMachine.Command.newCommit(newState));
        if (this.mLastRequestedState == newState) {
            this.mCommands.add(com.android.server.wm.utils.StateMachine.Command.newExit(newState));
            this.mCommands.add(com.android.server.wm.utils.StateMachine.Command.newEnter(newState));
        } else {
            for (int s = this.mLastRequestedState; !isIn(newState, s); s >>= 4) {
                this.mCommands.add(com.android.server.wm.utils.StateMachine.Command.newExit(s));
            }
            this.mTmp.clear();
            for (int s2 = newState; !isIn(this.mLastRequestedState, s2); s2 >>= 4) {
                this.mTmp.add(s2);
            }
            for (int i = this.mTmp.size() - 1; i >= 0; i--) {
                this.mCommands.add(com.android.server.wm.utils.StateMachine.Command.newEnter(this.mTmp.get(i)));
            }
        }
        this.mLastRequestedState = newState;
        while (!this.mCommands.isEmpty()) {
            com.android.server.wm.utils.StateMachine.Command cmd = this.mCommands.remove();
            switch (cmd.mType) {
                case 1:
                    this.mState = cmd.mState;
                    break;
                case 2:
                    enter(cmd.mState);
                    break;
                case 3:
                    exit(cmd.mState);
                    break;
                default:
                    android.util.Slog.e(TAG, "Unknown command type: " + cmd.mType);
                    break;
            }
        }
    }
}
