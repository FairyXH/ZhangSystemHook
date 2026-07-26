package com.android.server.devicestate;

/* JADX INFO: loaded from: classes.dex */
public class DeviceStateManagerShellCommand extends android.os.ShellCommand {
    private static android.hardware.devicestate.DeviceStateRequest sLastBaseStateRequest;
    private static android.hardware.devicestate.DeviceStateRequest sLastRequest;
    private final android.hardware.devicestate.DeviceStateManager mClient;
    private final com.android.server.devicestate.DeviceStateManagerService mService;

    public DeviceStateManagerShellCommand(com.android.server.devicestate.DeviceStateManagerService service) {
        this.mService = service;
        this.mClient = (android.hardware.devicestate.DeviceStateManager) service.getContext().getSystemService(android.hardware.devicestate.DeviceStateManager.class);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r3) {
        /*
            r2 = this;
            if (r3 != 0) goto L7
            int r0 = r2.handleDefaultCommands(r3)
            return r0
        L7:
            java.io.PrintWriter r0 = r2.getOutPrintWriter()
            int r1 = r3.hashCode()
            switch(r1) {
                case -1906524523: goto L3f;
                case -1422060175: goto L34;
                case -1134192350: goto L29;
                case -295380803: goto L1e;
                case 109757585: goto L13;
                default: goto L12;
            }
        L12:
            goto L49
        L13:
            java.lang.String r1 = "state"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 0
            goto L4a
        L1e:
            java.lang.String r1 = "print-states-simple"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 4
            goto L4a
        L29:
            java.lang.String r1 = "print-states"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 3
            goto L4a
        L34:
            java.lang.String r1 = "print-state"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 2
            goto L4a
        L3f:
            java.lang.String r1 = "base-state"
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L12
            r1 = 1
            goto L4a
        L49:
            r1 = -1
        L4a:
            switch(r1) {
                case 0: goto L66;
                case 1: goto L61;
                case 2: goto L5c;
                case 3: goto L57;
                case 4: goto L52;
                default: goto L4d;
            }
        L4d:
            int r1 = r2.handleDefaultCommands(r3)
            return r1
        L52:
            int r1 = r2.runPrintStatesSimple(r0)
            return r1
        L57:
            int r1 = r2.runPrintStates(r0)
            return r1
        L5c:
            int r1 = r2.runPrintState(r0)
            return r1
        L61:
            int r1 = r2.runBaseState(r0)
            return r1
        L66:
            int r1 = r2.runState(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicestate.DeviceStateManagerShellCommand.onCommand(java.lang.String):int");
    }

    private void printAllStates(java.io.PrintWriter pw) {
        java.util.Optional<android.hardware.devicestate.DeviceState> committedState = this.mService.getCommittedState();
        java.util.Optional<android.hardware.devicestate.DeviceState> baseState = this.mService.getBaseState();
        java.util.Optional<android.hardware.devicestate.DeviceState> overrideState = this.mService.getOverrideState();
        pw.println("Committed state: " + toString(committedState));
        if (overrideState.isPresent()) {
            pw.println("----------------------");
            pw.println("Base state: " + toString(baseState));
            pw.println("Override state: " + overrideState.get());
        }
    }

    private int runState(java.io.PrintWriter pw) {
        java.lang.String nextArg = getNextArg();
        if (nextArg == null) {
            printAllStates(pw);
            return 0;
        }
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            if (!"reset".equals(nextArg)) {
                int requestedState = java.lang.Integer.parseInt(nextArg);
                android.hardware.devicestate.DeviceStateRequest request = android.hardware.devicestate.DeviceStateRequest.newBuilder(requestedState).build();
                this.mClient.requestState(request, (java.util.concurrent.Executor) null, (android.hardware.devicestate.DeviceStateRequest.Callback) null);
                sLastRequest = request;
            } else if (sLastRequest != null) {
                this.mClient.cancelStateRequest();
                sLastRequest = null;
            }
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: requested state should be an integer");
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: " + e2.getMessage());
            getErrPrintWriter().println("-------------------");
            getErrPrintWriter().println("Run:");
            getErrPrintWriter().println("");
            getErrPrintWriter().println("    print-states");
            getErrPrintWriter().println("");
            getErrPrintWriter().println("to get the list of currently supported device states");
            return -1;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    private int runBaseState(java.io.PrintWriter pw) {
        java.lang.String nextArg = getNextArg();
        if (nextArg == null) {
            printAllStates(pw);
            return 0;
        }
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            if (!"reset".equals(nextArg)) {
                int requestedState = java.lang.Integer.parseInt(nextArg);
                android.hardware.devicestate.DeviceStateRequest request = android.hardware.devicestate.DeviceStateRequest.newBuilder(requestedState).build();
                this.mClient.requestBaseStateOverride(request, (java.util.concurrent.Executor) null, (android.hardware.devicestate.DeviceStateRequest.Callback) null);
                sLastBaseStateRequest = request;
            } else if (sLastBaseStateRequest != null) {
                this.mClient.cancelBaseStateOverride();
                sLastBaseStateRequest = null;
            }
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: requested state should be an integer");
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: " + e2.getMessage());
            getErrPrintWriter().println("-------------------");
            getErrPrintWriter().println("Run:");
            getErrPrintWriter().println("");
            getErrPrintWriter().println("    print-states");
            getErrPrintWriter().println("");
            getErrPrintWriter().println("to get the list of currently supported device states");
            return -1;
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    private int runPrintState(java.io.PrintWriter pw) {
        java.util.Optional<android.hardware.devicestate.DeviceState> deviceState = this.mService.getCommittedState();
        if (deviceState.isPresent()) {
            pw.println(deviceState.get().getIdentifier());
            return 0;
        }
        getErrPrintWriter().println("Error: device state not available.");
        return 1;
    }

    private int runPrintStates(java.io.PrintWriter pw) {
        java.util.List<android.hardware.devicestate.DeviceState> states = this.mService.getSupportedStates();
        pw.print("Supported states: [\n");
        for (int i = 0; i < states.size(); i++) {
            pw.print("  " + states.get(i) + ",\n");
        }
        pw.println("]");
        return 0;
    }

    private int runPrintStatesSimple(java.io.PrintWriter pw) {
        pw.print((java.lang.String) this.mService.getSupportedStates().stream().map(new java.util.function.Function() { // from class: com.android.server.devicestate.DeviceStateManagerShellCommand$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return java.lang.Integer.valueOf(((android.hardware.devicestate.DeviceState) obj).getIdentifier());
            }
        }).map(new java.util.function.Function() { // from class: com.android.server.devicestate.DeviceStateManagerShellCommand$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((java.lang.Integer) obj).toString();
            }
        }).collect(java.util.stream.Collectors.joining(",")));
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Device state manager (device_state) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  state [reset|OVERRIDE_DEVICE_STATE]");
        pw.println("    Return or override device state.");
        pw.println("  print-state");
        pw.println("    Return the current device state.");
        pw.println("  print-states");
        pw.println("    Return list of currently supported device states.");
        pw.println("  print-states-simple");
        pw.println("    Return the currently supported device states in comma separated format.");
    }

    private static java.lang.String toString(java.util.Optional<android.hardware.devicestate.DeviceState> state) {
        return state.isPresent() ? state.get().toString() : "(none)";
    }
}
