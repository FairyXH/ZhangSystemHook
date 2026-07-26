package com.android.server.trust;

/* JADX INFO: loaded from: classes3.dex */
public class TrustArchive {
    private static final int HISTORY_LIMIT = 200;
    private static final int TYPE_AGENT_CONNECTED = 4;
    private static final int TYPE_AGENT_DIED = 3;
    private static final int TYPE_AGENT_STOPPED = 5;
    private static final int TYPE_GRANT_TRUST = 0;
    private static final int TYPE_MANAGING_TRUST = 6;
    private static final int TYPE_POLICY_CHANGED = 7;
    private static final int TYPE_REVOKE_TRUST = 1;
    private static final int TYPE_TRUST_TIMEOUT = 2;
    java.util.ArrayDeque<com.android.server.trust.TrustArchive.Event> mEvents = new java.util.ArrayDeque<>();

    private static class Event {
        final android.content.ComponentName agent;
        final long duration;
        final long elapsedTimestamp;
        final int flags;
        final boolean managingTrust;
        final java.lang.String message;
        final int type;
        final int userId;

        private Event(int type, int userId, android.content.ComponentName agent, java.lang.String message, long duration, int flags, boolean managingTrust) {
            this.type = type;
            this.userId = userId;
            this.agent = agent;
            this.elapsedTimestamp = android.os.SystemClock.elapsedRealtime();
            this.message = message;
            this.duration = duration;
            this.flags = flags;
            this.managingTrust = managingTrust;
        }
    }

    public void logGrantTrust(int userId, android.content.ComponentName agent, java.lang.String message, long duration, int flags) {
        addEvent(new com.android.server.trust.TrustArchive.Event(0, userId, agent, message, duration, flags, false));
    }

    public void logRevokeTrust(int userId, android.content.ComponentName agent) {
        addEvent(new com.android.server.trust.TrustArchive.Event(1, userId, agent, null, 0L, 0, false));
    }

    public void logTrustTimeout(int userId, android.content.ComponentName agent) {
        addEvent(new com.android.server.trust.TrustArchive.Event(2, userId, agent, null, 0L, 0, false));
    }

    public void logAgentDied(int userId, android.content.ComponentName agent) {
        addEvent(new com.android.server.trust.TrustArchive.Event(3, userId, agent, null, 0L, 0, false));
    }

    public void logAgentConnected(int userId, android.content.ComponentName agent) {
        addEvent(new com.android.server.trust.TrustArchive.Event(4, userId, agent, null, 0L, 0, false));
    }

    public void logAgentStopped(int userId, android.content.ComponentName agent) {
        addEvent(new com.android.server.trust.TrustArchive.Event(5, userId, agent, null, 0L, 0, false));
    }

    public void logManagingTrust(int userId, android.content.ComponentName agent, boolean managing) {
        addEvent(new com.android.server.trust.TrustArchive.Event(6, userId, agent, null, 0L, 0, managing));
    }

    public void logDevicePolicyChanged() {
        addEvent(new com.android.server.trust.TrustArchive.Event(7, -1, null, null, 0L, 0, false));
    }

    private void addEvent(com.android.server.trust.TrustArchive.Event e) {
        if (this.mEvents.size() >= 200) {
            this.mEvents.removeFirst();
        }
        this.mEvents.addLast(e);
    }

    public void dump(java.io.PrintWriter writer, int limit, int userId, java.lang.String linePrefix, boolean duplicateSimpleNames) {
        int count = 0;
        java.util.Iterator<com.android.server.trust.TrustArchive.Event> iter = this.mEvents.descendingIterator();
        while (iter.hasNext() && count < limit) {
            com.android.server.trust.TrustArchive.Event ev = iter.next();
            if (userId == -1 || userId == ev.userId || ev.userId == -1) {
                writer.print(linePrefix);
                writer.printf("#%-2d %s %s: ", java.lang.Integer.valueOf(count), formatElapsed(ev.elapsedTimestamp), dumpType(ev.type));
                if (userId == -1) {
                    writer.print("user=");
                    writer.print(ev.userId);
                    writer.print(", ");
                }
                if (ev.agent != null) {
                    writer.print("agent=");
                    if (duplicateSimpleNames) {
                        writer.print(ev.agent.flattenToShortString());
                    } else {
                        writer.print(getSimpleName(ev.agent));
                    }
                }
                switch (ev.type) {
                    case 0:
                        writer.printf(", message=\"%s\", duration=%s, flags=%s", ev.message, formatDuration(ev.duration), dumpGrantFlags(ev.flags));
                        break;
                    case 6:
                        writer.printf(", managingTrust=" + ev.managingTrust, new java.lang.Object[0]);
                        break;
                }
                writer.println();
                count++;
            }
        }
    }

    public static java.lang.String formatDuration(long duration) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        android.util.TimeUtils.formatDuration(duration, sb);
        return sb.toString();
    }

    private static java.lang.String formatElapsed(long elapsed) {
        long delta = elapsed - android.os.SystemClock.elapsedRealtime();
        long wallTime = java.lang.System.currentTimeMillis() + delta;
        return android.util.TimeUtils.logTimeOfDay(wallTime);
    }

    static java.lang.String getSimpleName(android.content.ComponentName cn) {
        java.lang.String name = cn.getClassName();
        int idx = name.lastIndexOf(46);
        if (idx < name.length() && idx >= 0) {
            return name.substring(idx + 1);
        }
        return name;
    }

    private java.lang.String dumpType(int type) {
        switch (type) {
            case 0:
                return "GrantTrust";
            case 1:
                return "RevokeTrust";
            case 2:
                return "TrustTimeout";
            case 3:
                return "AgentDied";
            case 4:
                return "AgentConnected";
            case 5:
                return "AgentStopped";
            case 6:
                return "ManagingTrust";
            case 7:
                return "DevicePolicyChanged";
            default:
                return "Unknown(" + type + ")";
        }
    }

    private java.lang.String dumpGrantFlags(int flags) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if ((flags & 1) != 0) {
            if (sb.length() != 0) {
                sb.append('|');
            }
            sb.append("INITIATED_BY_USER");
        }
        if ((flags & 2) != 0) {
            if (sb.length() != 0) {
                sb.append('|');
            }
            sb.append("DISMISS_KEYGUARD");
        }
        if (sb.length() == 0) {
            sb.append('0');
        }
        return sb.toString();
    }
}
