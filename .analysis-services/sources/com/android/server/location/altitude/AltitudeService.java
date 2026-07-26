package com.android.server.location.altitude;

/* JADX INFO: loaded from: classes2.dex */
public class AltitudeService extends android.frameworks.location.altitude.IAltitudeService.Stub {
    private final android.location.altitude.AltitudeConverter mAltitudeConverter = new android.location.altitude.AltitudeConverter();
    private final android.content.Context mContext;

    public AltitudeService(android.content.Context context) {
        this.mContext = context;
    }

    public android.frameworks.location.altitude.AddMslAltitudeToLocationResponse addMslAltitudeToLocation(android.frameworks.location.altitude.AddMslAltitudeToLocationRequest request) throws android.os.RemoteException {
        android.location.Location location = new android.location.Location("");
        location.setLatitude(request.latitudeDegrees);
        location.setLongitude(request.longitudeDegrees);
        location.setAltitude(request.altitudeMeters);
        location.setVerticalAccuracyMeters(request.verticalAccuracyMeters);
        android.frameworks.location.altitude.AddMslAltitudeToLocationResponse response = new android.frameworks.location.altitude.AddMslAltitudeToLocationResponse();
        try {
            this.mAltitudeConverter.addMslAltitudeToLocation(this.mContext, location);
            response.mslAltitudeMeters = location.getMslAltitudeMeters();
            response.mslAltitudeAccuracyMeters = location.getMslAltitudeAccuracyMeters();
            response.success = true;
            return response;
        } catch (java.io.IOException e) {
            response.success = false;
            return response;
        }
    }

    public android.frameworks.location.altitude.GetGeoidHeightResponse getGeoidHeight(android.frameworks.location.altitude.GetGeoidHeightRequest request) throws android.os.RemoteException {
        try {
            return this.mAltitudeConverter.getGeoidHeight(this.mContext, request);
        } catch (java.io.IOException e) {
            android.frameworks.location.altitude.GetGeoidHeightResponse response = new android.frameworks.location.altitude.GetGeoidHeightResponse();
            response.success = false;
            return response;
        }
    }

    public java.lang.String getInterfaceHash() {
        return "e47d23f579ff7a897fb03e7e7f1c3006cfc6036b";
    }

    public int getInterfaceVersion() {
        return 2;
    }

    public static class Lifecycle extends com.android.server.SystemService {
        private static final java.lang.String SERVICE_NAME = android.frameworks.location.altitude.IAltitudeService.DESCRIPTOR + "/default";
        private com.android.server.location.altitude.AltitudeService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mService = new com.android.server.location.altitude.AltitudeService(getContext());
            publishBinderService(SERVICE_NAME, this.mService);
        }
    }
}
