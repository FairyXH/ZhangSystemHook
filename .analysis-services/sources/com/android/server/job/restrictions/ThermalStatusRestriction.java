package com.android.server.job.restrictions;

/* JADX INFO: loaded from: classes2.dex */
public class ThermalStatusRestriction extends com.android.server.job.restrictions.JobRestriction {
    private static final int HIGHER_PRIORITY_THRESHOLD = 2;
    private static final int LOWER_THRESHOLD = 1;
    private static final int LOW_PRIORITY_THRESHOLD = 1;
    private static final java.lang.String TAG = "ThermalStatusRestriction";
    private static final int UPPER_THRESHOLD = 3;
    private volatile int mThermalStatus;

    public ThermalStatusRestriction(com.android.server.job.JobSchedulerService service) {
        super(service, 4, 12, 5);
        this.mThermalStatus = 0;
    }

    @Override // com.android.server.job.restrictions.JobRestriction
    public void onSystemServicesReady() {
        android.os.PowerManager powerManager = (android.os.PowerManager) this.mService.getTestableContext().getSystemService(android.os.PowerManager.class);
        powerManager.addThermalStatusListener(new android.os.PowerManager.OnThermalStatusChangedListener() { // from class: com.android.server.job.restrictions.ThermalStatusRestriction.1
            @Override // android.os.PowerManager.OnThermalStatusChangedListener
            public void onThermalStatusChanged(int status) {
                boolean significantChange = (status >= 1 && status <= 3) || (com.android.server.job.restrictions.ThermalStatusRestriction.this.mThermalStatus >= 1 && status < 1) || (com.android.server.job.restrictions.ThermalStatusRestriction.this.mThermalStatus < 3 && status > 3);
                boolean increased = com.android.server.job.restrictions.ThermalStatusRestriction.this.mThermalStatus < status;
                com.android.server.job.restrictions.ThermalStatusRestriction.this.mThermalStatus = status;
                if (significantChange) {
                    com.android.server.job.restrictions.ThermalStatusRestriction.this.mService.onRestrictionStateChanged(com.android.server.job.restrictions.ThermalStatusRestriction.this, increased);
                }
            }
        });
    }

    @Override // com.android.server.job.restrictions.JobRestriction
    public boolean isJobRestricted(com.android.server.job.controllers.JobStatus job, int bias) {
        if (com.android.server.job.Flags.thermalRestrictionsToFgsJobs()) {
            if (bias >= 40) {
                return false;
            }
        } else if (bias >= 35) {
            return false;
        }
        if (this.mThermalStatus >= 3) {
            return true;
        }
        int priority = job.getEffectivePriority();
        if (this.mThermalStatus >= 2) {
            if (job.shouldTreatAsUserInitiatedJob()) {
                return false;
            }
            if (job.shouldTreatAsExpeditedJob()) {
                return job.getNumPreviousAttempts() > 0 || (this.mService.isCurrentlyRunningLocked(job) && this.mService.isJobInOvertimeLocked(job));
            }
            if (com.android.server.job.Flags.thermalRestrictionsToFgsJobs() && bias >= 35 && job.getJob().isImportantWhileForeground()) {
                return job.getNumPreviousAttempts() > 0 || (this.mService.isCurrentlyRunningLocked(job) && this.mService.isJobInOvertimeLocked(job));
            }
            if (priority == 400) {
                return !this.mService.isCurrentlyRunningLocked(job) || this.mService.isJobInOvertimeLocked(job);
            }
            return true;
        }
        if (this.mThermalStatus < 1) {
            return false;
        }
        if (com.android.server.job.Flags.thermalRestrictionsToFgsJobs() && bias >= 35) {
            return false;
        }
        if (priority != 100) {
            if (priority != 200) {
                return false;
            }
            if (this.mService.isCurrentlyRunningLocked(job) && !this.mService.isJobInOvertimeLocked(job)) {
                return false;
            }
        }
        return true;
    }

    int getThermalStatus() {
        return this.mThermalStatus;
    }

    @Override // com.android.server.job.restrictions.JobRestriction
    public void dumpConstants(android.util.IndentingPrintWriter pw) {
        pw.print("Thermal status: ");
        pw.println(this.mThermalStatus);
    }
}
